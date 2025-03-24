package com.github.zyypj.simplewhitelist.listener;

import com.github.zyypj.simplewhitelist.WhitelistPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class WhitelistListener implements Listener {

    private final WhitelistPlugin plugin;

    public WhitelistListener(WhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        FileConfiguration config = plugin.getConfig();
        boolean enabled = config.getBoolean("whitelist.enabled", false);
        if (!enabled) {
            return;
        }
        String playerName = event.getName();
        List<String> whitelist = config.getStringList("whitelist.players");
        if (!whitelist.contains(playerName)) {
            String joinMsg = formatMessageList(config.getStringList("whitelist.joinMessage"));
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, joinMsg);
        }
    }

    private String formatMessageList(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(ChatColor.translateAlternateColorCodes('&', line)).append("\n");
        }
        return sb.toString().trim();
    }
}