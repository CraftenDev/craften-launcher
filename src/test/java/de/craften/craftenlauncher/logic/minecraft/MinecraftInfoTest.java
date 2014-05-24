package de.craften.craftenlauncher.logic.minecraft;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinecraftInfoTest {
    MinecraftInfo mcinfo;

    @Before
    public void setUp() throws Exception {
        System.setProperty("os.arch", "x86"); // Force 32 bit
        mcinfo = new MinecraftInfo("1.7.2");
    }

    @Test
    public void testgetXMX(){
        mcinfo.setXMX("4g");                  // Too much for 32 bit
        if(System.getProperty("os.name").contains("Win"))
            assertEquals("2048m",mcinfo.getXMX());
        else
            assertEquals("4096m",mcinfo.getXMX());
    }

    @Test
    public void testgetXMX2(){
        mcinfo.setXMX("4096m");              // Too much for 32 bit
        if(System.getProperty("os.name").contains("Win"))
            assertEquals("2048m",mcinfo.getXMX());
        else
            assertEquals("4096m",mcinfo.getXMX());
    }
}