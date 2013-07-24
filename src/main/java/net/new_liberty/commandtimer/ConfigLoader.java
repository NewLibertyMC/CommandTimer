package net.new_liberty.commandtimer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import net.new_liberty.commandtimer.models.CommandSet;
import org.bukkit.configuration.ConfigurationSection;
import static net.new_liberty.commandtimer.CommandTimer.log;

/**
 * Loads the configuration.
 */
public final class ConfigLoader {
    /**
     * C'tor
     */
    private ConfigLoader() {
    }

    /**
     * Loads a CommandSet.
     *
     * @param id
     * @param section
     * @param commandMappings
     * @return
     */
    public static CommandSet loadSet(String id, ConfigurationSection section, Map<String, CommandSet> commandMappings) {
        if (section == null) {
            return null;
        }

        // Set messages
        Map<String, String> messages = new HashMap<String, String>();
        ConfigurationSection setMessagesSection = section.getConfigurationSection("messages");
        if (setMessagesSection != null) {
            for (Map.Entry<String, Object> setMessage : setMessagesSection.getValues(false).entrySet()) {
                messages.put(setMessage.getKey(), setMessage.getValue().toString());
            }
        }

        // Set commands
        Set<String> commands = new HashSet<String>();
        List<String> setCmdConfig = section.getStringList("commands");

        cmd:
        for (String setCmd : setCmdConfig) {
            // Lowercase the commands to make sure
            setCmd = setCmd.toLowerCase();

            // Check if the command has already been added in a different form to prevent conflicts

            // In this command set
            for (String cmd : commands) {
                if (cmd.startsWith(setCmd) || setCmd.startsWith(cmd)) {
                    log(Level.WARNING, "The command '" + setCmd + "' from set '" + id + "' conflicts with the command '" + cmd + "' from the same set.");
                    continue cmd;
                }
            }

            // In previous command sets
            for (String cmd : commandMappings.keySet()) {
                if (cmd.startsWith(setCmd) || setCmd.startsWith(cmd)) {
                    log(Level.WARNING, "The command '" + setCmd + "' from set '" + id + "' conflicts with the command '" + cmd + "' from set '" + commandMappings.get(cmd).getId() + "'.");
                    continue cmd;
                }
            }
            commands.add(setCmd);
        }

        return new CommandSet(id, messages, commands);
    }
}