package com.github.zyypj.simplewhitelist;

import co.aikar.commands.BukkitCommandManager;
import com.github.zyypj.simplewhitelist.command.WhitelistCommand;
import com.github.zyypj.simplewhitelist.listener.WhitelistListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhitelistPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        debug("");
        debug("&aIniciando SimpleWhitelist...");
        setupFiles();

        setupCommands();
        setupListeners();
        debug("&aSimpleWhitelist iniciado com sucesso!");
        debug("");
    }

    @Override
    public void onDisable() {

    }

    public void debug(String message) {
        message = ChatColor.stripColor(message);
        getServer().getConsoleSender().sendMessage("§8§l[SimpleWhitelist-DEBUG] §f" + message);
    }

    private void setupFiles() {
        saveDefaultConfig();
    }

    private void setupCommands() {
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new WhitelistCommand(this));
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new WhitelistListener(this), this);
    }
}