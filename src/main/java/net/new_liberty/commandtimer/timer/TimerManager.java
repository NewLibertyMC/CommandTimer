package net.new_liberty.commandtimer.timer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.new_liberty.commandtimer.models.CommandSet;
import net.new_liberty.commandtimer.models.CommandSetGroup;
import net.new_liberty.commandtimer.CommandTimer;

/**
 * Manages player timers.
 */
public class TimerManager {
    private final CommandTimer plugin;

    /**
     * Task to check all warmup timers.
     */
    private final WarmupExecutor warmupExecutor;

    /**
     * Stores warmups. Each player can only have one warmup running at a time.
     */
    private Map<String, CommandExecution> warmups;

    /**
     * Stores cooldowns.
     */
    private Map<String, Set<CommandExecution>> cooldowns;

    /**
     * C'tor
     *
     * @param plugin
     */
    public TimerManager(CommandTimer plugin) {
        this.plugin = plugin;

        warmupExecutor = new WarmupExecutor(this);
    }

    /**
     * Starts all tasks.
     */
    public void startTasks() {
        warmupExecutor.runTaskTimer(plugin, 2L, 2L);
    }

    /**
     * Clears all timers.
     */
    public void clear() {
        warmups.clear();
    }

    /**
     * Checks if this player is warming up.
     *
     * @param player
     * @return
     */
    public boolean isWarmingUp(String player) {
        return warmups.containsKey(player);
    }

    /**
     * Gets a list of all warmup timers.
     *
     * @return
     */
    public List<CommandExecution> getWarmups() {
        return new ArrayList<CommandExecution>(warmups.values());
    }

    /**
     * Gets the time remaining for the player's cooldown to expire for a
     * CommandSet.
     *
     * @param player
     * @param set
     * @return
     */
    public int getCooldownTime(String player, CommandSet set) {
        // Find the CommandExecution
        CommandExecution c = null;
        Set<CommandExecution> cds = getCooldownsInternal(player);
        for (CommandExecution ce : cds) {
            if (ce.getSet().equals(set)) {
                c = ce;
                break;
            }
        }

        // If none then we aren't cooling down
        if (c == null) {
            return -1;
        }

        // If expired then we aren't cooling down
        int cd = c.getCdTimeLeft();
        if (cd == -1) {
            cds.remove(c); // Remove the now useless cooldown
        }
        return cd;
    }

    /**
     * Gets the current cooldowns a player is waiting on.
     *
     * @param player
     * @return
     */
    private Set<CommandExecution> getCooldownsInternal(String player) {
        Set<CommandExecution> cds = cooldowns.get(player);
        if (cds == null) {
            cds = new HashSet<CommandExecution>();
            cooldowns.put(player, cds);
        }
        return cds;
    }

    /**
     * Starts a warmup for a player.
     *
     * @param player
     * @param command
     * @param set
     * @param group
     */
    public void startWarmup(String player, String command, CommandSet set, CommandSetGroup group) {
        warmups.put(player, new CommandExecution(player, command, set, group));
    }
}
