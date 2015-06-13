package de.craften.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        String[] args = {"--key=val","--key2=val2","--key3=val3","--key4","key5"};

        parser = new UIParser(args);
    }

    /**
     *  Tests if the correct value is returned for the given key.
     */
    @Test
    public void testGetArgForKey() {
        String val = parser.getValue("key");

        assertEquals("val",val);
    }

    /**
     * Tests if hasValue returns true when the key has a value.
     */
    @Test
    public void testHasKeyWithKeyAndValue() {
        assertTrue(parser.hasKey("key2"));
    }

    /**
     * Tests if hasValue returns true when the key has no value.
     */
    @Test
    public void testHasKeyWithKeyWithoutValue() {
        assertTrue(parser.hasKey("key4"));
    }

    /**
     * Tests if hasKey return false if there is no key
     */
    @Test
    public void testHasNoKey() {
        assertFalse(parser.hasKey("testkey"));
    }

    /**
     * Tests if wrong key is not known after parsing
     */
    @Test
    public void testHasNotWrongKey() {
        assertFalse(parser.hasKey("key5"));
    }

    /**
     * Tests if a given key has a value
     */
    @Test
    public void testHasValue() {
        assertTrue(parser.hasValue("key3"));
    }

    /**
     * Tests if the hasValue method returns false if the key has not a value.
     */
    @Test
    public void testHasNotValue() {
        assertFalse(parser.hasValue("key4"));
    }
}
