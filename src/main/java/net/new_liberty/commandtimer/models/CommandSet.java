package net.new_liberty.commandtimer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.new_liberty.commandtimer.CommandTimer;

/**
 * Represents a set of commands.
 */
public final class CommandSet {
    private final String id;

    private final Map<String, String> messages;

    private final Set<String> cmds;

    public CommandSet(String id, Map<String, String> messages, Set<String> cmds) {
        this.id = id;
        this.messages = messages;
        this.cmds = cmds;
    }

    /**
     * Gets the id of this CommandSet.
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Gets a message from its key.
     *
     * @param key
     * @return
     */
    public String getMessage(String key) {
        String ret = messages.get(key);
        if (ret == null) {
            ret = CommandTimer.getInstance().getMessage(key);
        }
        return ret;
    }

    /**
     * Gets a list of all commands in this CommandSet.
     *
     * @return
     */
    public List<String> getCommands() {
        return new ArrayList<String>(cmds);
    }
}
