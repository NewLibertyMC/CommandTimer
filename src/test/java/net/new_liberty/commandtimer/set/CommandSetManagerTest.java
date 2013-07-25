package net.new_liberty.commandtimer.set;

import java.io.File;
import java.net.URL;
import java.util.Map;
import net.new_liberty.commandtimer.CommandTimer;
import net.new_liberty.commandtimer.ConfigLoader;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests CommandSetManager
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CommandTimer.class})
public class CommandSetManagerTest {
    private CommandSetManager i;

    @Before
    public void setup() throws Exception {
        PowerMockito.mockStatic(CommandTimer.class);

        URL url = getClass().getResource("/config.yml");
        Configuration config = YamlConfiguration.loadConfiguration(new File(url.getFile()));

        // Yeah, this isn't really a unit test but i'm lazy
        Map[] m = ConfigLoader.loadConfig(config);
        i = new CommandSetManager(m[1], m[2], m[3]);
    }

    @Test
    public void testGetGroup() {
        String command = "home nether";
        CommandSet set = i.getSet(command);

        assertNotNull(set);
        assertTrue(set.getId().equals("teleport"));
    }
}
