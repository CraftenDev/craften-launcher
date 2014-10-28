package de.craften.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        String[] args = {"--key=val","--key2=val2","--key3=val3","--key4"};

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

    /**
     * Tests if hasArg returns true when the key has a value.
     */
    @Test
    public void testHasArgWithArgAndValue() {
        assertTrue(parser.hasArg("key2"));
    }
}
