package de.craften.craftenlauncher;

import com.tngtech.configbuilder.ConfigBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Simple contract test for config builder lib.
 */
public class ConfigTest {

    private String[] args = {"--server=10.0.0.0", "--fullscreen"};

    @Test
    public void testGetArgumentConfigParameter() {
        Config config = ConfigBuilder.on(Config.class).withCommandLineArgs(args).build();

        assertEquals("10.0.0.0",config.getServer());
    }

    @Test
    public void testGetSwitchConfigParameters() {
        Config config = ConfigBuilder.on(Config.class).withCommandLineArgs(args).build();

        assertEquals(true,config.isFullscreen());
    }
}
