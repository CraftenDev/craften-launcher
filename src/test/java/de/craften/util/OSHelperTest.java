package de.craften.util;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class OSHelperTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty("os.name", "Mac-SuperDuper-Machine");
        System.setProperty("os.arch", "x64");
    }

    @Test
    public void testIs32bit() throws Exception {
        assertFalse(OSHelper.isJava32bit());
    }

    @Test
    public void testIs64bit() throws Exception {
        assertTrue(OSHelper.isJava64bit());
    }

    @Test
    @Ignore("Test does not wook properly on different test machines!")
    public void testGetOperatingSystem() throws Exception {
        assertNotEquals(OS.WINDOWS, OSHelper.getOSasEnum());
        assertNotEquals(OS.LINUX, OSHelper.getOSasEnum());
        assertEquals(OS.OSX, OSHelper.getOSasEnum());
    }

    @Test
    @Ignore("Test does not wook properly on different test machines!")
    public void testGetOperatingSystemString() throws Exception {
        assertNotEquals("windows", OSHelper.getOSasString());
        assertNotEquals("linux", OSHelper.getOSasString());
        assertEquals("osx", OSHelper.getOSasString());
    }
}