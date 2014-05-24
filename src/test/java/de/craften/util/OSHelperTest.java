package de.craften.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OSHelperTest{

    @Before
    public void setUp() throws Exception {
        System.setProperty("os.name","Mac-SuperDuper-Machine");
        System.setProperty("os.arch","x64");
    }

    @Test
    public void testIs32bit() throws Exception {
        assertFalse(OSHelper.getInstance().is32bit());
    }

    @Test
    public void testIs64bit() throws Exception {
        assertTrue(OSHelper.getInstance().is64bit());
    }

    @Test
    public void testGetOperatingSystem() throws Exception {
        assertNotEquals(OSHelper.getInstance().getOperatingSystem(),"windows");
        assertNotEquals(OSHelper.getInstance().getOperatingSystem(),"linux");
        assertEquals(OSHelper.getInstance().getOperatingSystem(),"osx");
    }
}