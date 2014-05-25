package de.craften.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OSHelperTest{
    OSHelper oshelper;

    @Before
    public void setUp() throws Exception {
        System.setProperty("os.name","Mac-SuperDuper-Machine");
        System.setProperty("os.arch","x64");
        oshelper = OSHelper.TEST_CreateInstance();
    }

    @Test
    public void testIs32bit() throws Exception {
        assertFalse(oshelper.isJava32bit());
    }

    @Test
    public void testIs64bit() throws Exception {
        assertTrue(oshelper.isJava64bit());
    }

    @Test
    public void testGetOperatingSystem() throws Exception {
        assertNotEquals("windows",oshelper.getOperatingSystem());
        assertNotEquals("linux",oshelper.getOperatingSystem());
        assertEquals("osx",oshelper.getOperatingSystem());
    }
}