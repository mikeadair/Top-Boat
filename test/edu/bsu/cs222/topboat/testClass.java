package edu.bsu.cs222.topboat;

import cs222.topboat.controllers.GameBoardController;
import cs222.topboat.controllers.MainMenuController;
import org.junit.Assert;
import org.junit.Test;

public class testClass {


    @Test
    public void TestBasicTestFalse(){
        boolean fail = false;
        Assert.assertFalse(fail);
    }

    @Test
    public void TestBasicTestTrue(){
        boolean pass = true;
        Assert.assertTrue(pass);
    }
}
