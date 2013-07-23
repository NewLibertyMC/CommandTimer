package net.new_liberty.commandtimer;

import net.new_liberty.commandtimer.models.CommandSet;
import net.new_liberty.commandtimer.models.CommandSetGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Listener
 */
public class CTListener implements Listener {
    /**
     * Plugin instance
     */
    private final CommandTimer plugin;

    /**
     * C'tor
     *
     * @param plugin
     */
    public CTListener(CommandTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        cmd = cmd.substring(1, cmd.length());

        CommandSet set = plugin.getCommandSet(cmd);
        if (set == null) {
            return; // Ignore if this command isn't part of a set
        }

        Player player = e.getPlayer();
        CTPlayer p = plugin.getPlayer(player.getName());
        CommandSetGroup g = p.getGroup();
        if (g == null) {
            return; // Ignore if player isn't in a group
        }

        // Check if we're already warming up. Don't let them use commands while doing so.
        if (p.isWarmingUp()) {
            player.sendMessage(plugin.getMessage("warmup-in-progress"));
            e.setCancelled(true);
            return;
        }
    }
}
