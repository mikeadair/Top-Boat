package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.Util.ShipPlacementHandler;
import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Ship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class ComputerPlayer extends Player {
    private static final String[] names = new String[] {
            "Sheila", "Burger Bob", "Steve the Scuba", "Capt. Oscar Meyer", "BombBot",
            "Chief Squanto", "Pvt. Johnson", "Peter Dragon", "Data", "The Borg", "Gryphon Peter", "Spoderman"
    };
    private static final Random random = new Random();
    private ArrayList<Board.Tile> hits = new ArrayList<>();
    private FiringMode firingMode;

    public ComputerPlayer() {
        super();
        setName(names[random.nextInt(names.length)]);
        firingMode = RANDOM;
    }

    @Override
    public void takeTurn() {
        System.out.println(getName() + " is taking their turn");
        Board.Tile target = firingMode.getTarget();
        if(target.hit()) {
            hits.add(target);
            firingMode.onHit();
        } else {
            firingMode.onMiss();
        }
    }

    public void placeShips() {
        ShipPlacementHandler placementHandler = new ShipPlacementHandler(this);
        new Thread(() -> {
            while(!placementHandler.allShipsPlaced()) {
                Board.Tile tryTile = getRandomTile(getBoard());
                if (placementHandler.isValidPlacementOrigin(tryTile)) {
                    Ship.Orientation randomOrientation = getRandomOrientationFromList(placementHandler.getValidOrientations());
                    placementHandler.confirmShipPlacement(tryTile, randomOrientation);
                }
            }
            long waitAmount = (random.nextInt(10) + 5) * 1000;
            try {
                Thread.sleep(waitAmount);
            } catch (InterruptedException e) {}
            setReady(true, true);
        }, "ComputerShipPlacementThread").start();
    }

    private Board.Tile getRandomTile(Board board) {
        int randomX = random.nextInt(Board.WIDTH);
        int randomY = random.nextInt(Board.HEIGHT);

        return board.getTile(randomX, randomY);
    }

    private Ship.Orientation getRandomOrientationFromList(ArrayList<Ship.Orientation> validOrientations) {
        int randomIndex = random.nextInt(validOrientations.size());
        return validOrientations.get(randomIndex);
    }

    abstract class FiringMode {
        abstract Board.Tile getTarget();
        abstract void onHit();
        abstract void onMiss();
    }


    final FiringMode RANDOM = new FiringMode() {
        @Override
        Board.Tile getTarget() {
            Board.Tile randomTarget = getRandomTile(Board.playerBoard());
            while(randomTarget.hasBeenHit()) {
                randomTarget = getRandomTile(Board.playerBoard());
            }
            return randomTarget;
        }

        @Override
        void onHit() {
            firingMode = SEEKING;
        }

        @Override
        void onMiss() {}
    };


    final FiringMode SEEKING = new FiringMode() {
        Stack<Board.Tile> potentialTargets = null;

        @Override
        Board.Tile getTarget() {
            if(potentialTargets == null) {
                getPotentialTargets();
            } else if(potentialTargets.size() == 0) {
                backToRandom();
                return RANDOM.getTarget();
            }

            Board.Tile target = potentialTargets.pop();
            while(target.hasBeenHit()) {
                if(potentialTargets.size() == 0) {
                    backToRandom();
                    return RANDOM.getTarget();
                } else {
                    target = potentialTargets.pop();
                }
            }
            return target;
        }

        private void getPotentialTargets() {
            Board.Tile hit = hits.get(0);
            potentialTargets = new Stack<>();

            for(int x = hit.x - 1; x < hit.x + 2; x++) {
                for(int y = hit.y - 1 + Math.abs(hit.x - x); y < hit.y + 2; y += 2) {
                    Board.Tile potentialTarget = Board.playerBoard().getTile(x, y);
                    if(potentialTarget != null && !potentialTarget.hasBeenHit()) {
                        potentialTargets.push(potentialTarget);
                    }
                }
            }
            Collections.shuffle(potentialTargets);
        }

        private void backToRandom() {
            firingMode = RANDOM;
            potentialTargets = null;
            hits.clear();
        }

        @Override
        void onHit() {
            firingMode = DESTROYING;
        }

        @Override
        void onMiss() {
            if(potentialTargets.size() == 0) {
                backToRandom();
            }
        }
    };


    final FiringMode DESTROYING = new FiringMode() {
        private final int[] UP = {0 , -1};
        private final int[] DOWN = {0, 1};
        private final int[] LEFT = {-1, 0};
        private final int[] RIGHT = {1, 0};

        private int[] direction = null;

        @Override
        Board.Tile getTarget() {
            if(direction == null) determineDirection();

            Board.Tile initialHit = hits.get(0);
            Board.Tile mostRecentHit = hits.get(hits.size()-1);
            int x = mostRecentHit.x + direction[0];
            int y = mostRecentHit.y + direction[1];

            Board.Tile target = Board.playerBoard().getTile(x, y);
            if(target != null && !target.hasBeenHit()) {
                return target;
            } else {
                determineDirection();
                int newX = initialHit.x + direction[0];
                int newY = initialHit.y + direction[1];

                target = Board.playerBoard().getTile(newX, newY);
                if(target != null && !target.hasBeenHit()) {
                    return target;
                } else {
                    backToSeeking();
                    return SEEKING.getTarget();
                }
            }
        }

        private void determineDirection() {
            if(direction == null) {
                Board.Tile hit1 = hits.get(0);
                Board.Tile hit2 = hits.get(1);

                if (hit1.x == hit2.x) {
                    if (hit1.y > hit2.y) {
                        direction = UP;
                    } else {
                        direction = DOWN;
                    }
                } else if (hit1.y == hit2.y) {
                    if (hit1.x > hit2.x) {
                        direction = RIGHT;
                    } else {
                        direction = LEFT;
                    }
                }
            } else {
                if(direction == UP) {
                    direction = DOWN;
                } else if(direction == DOWN) {
                    direction = UP;
                } else if(direction == LEFT) {
                    direction = RIGHT;
                } else if(direction == RIGHT) {
                    direction = LEFT;
                }
            }
        }

        private void backToSeeking() {
            direction = null;
            firingMode = SEEKING;
        }

        @Override
        void onHit() {}

        @Override
        void onMiss() {
            for(int i = 1; i < hits.size(); i++) {
                hits.remove(i);
            }
            determineDirection();
        }
    };
}
