package de.craften.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the UIParser class.
 *
 */
public class UIParserTest {

    private UIParser parser;

    @Before
    public void buildUIParser() {
        String[] args = {"--key=val"};

        parser = new UIParser(args);
    }

    /**
     *  Tests if the correct value is returned for the given key.
     */
    @Test
    public void testGetArgForKey() {
        String val = parser.getArg("key");

        assertEquals("val",val);
    }
}
