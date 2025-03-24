package com.github.zyypj.simplewhitelist.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.github.zyypj.simplewhitelist.WhitelistPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.List;

import org.bukkit.ChatColor;

@CommandAlias("whitelist")
public class WhitelistCommand extends BaseCommand {

    private final WhitelistPlugin plugin;

    public WhitelistCommand(WhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    private String getMessage(String key, String def) {
        return ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("messages." + key, def));
    }

    @Subcommand("add")
    @CommandCompletion("<jogador>")
    @Description("Adiciona um jogador à whitelist")
    public void onAdd(CommandSender sender, String playerName) {
        String perm = plugin.getConfig().getString("permissions.add", "simplewhitelist.whitelist.add");
        if (!sender.hasPermission(perm)) {
            sender.sendMessage(getMessage("no_permission", "Você não tem permissão para usar esse comando."));
            return;
        }
        List<String> whitelist = plugin.getConfig().getStringList("whitelist.players");
        if (whitelist.contains(playerName)) {
            sender.sendMessage(getMessage("player_already_whitelisted", "Jogador já está na whitelist."));
            return;
        }
        whitelist.add(playerName);
        plugin.getConfig().set("whitelist.players", whitelist);
        plugin.saveConfig();
        sender.sendMessage(getMessage("player_added", "Jogador adicionado à whitelist."));
    }

    @Subcommand("remove")
    @CommandCompletion("<jogador>")
    @Description("Remove um jogador da whitelist")
    public void onRemove(CommandSender sender, String playerName) {
        String perm = plugin.getConfig().getString("permissions.remove", "simplewhitelist.whitelist.remove");
        if (!sender.hasPermission(perm)) {
            sender.sendMessage(getMessage("no_permission", "Você não tem permissão para usar esse comando."));
            return;
        }
        List<String> whitelist = plugin.getConfig().getStringList("whitelist.players");
        if (!whitelist.contains(playerName)) {
            sender.sendMessage(getMessage("player_not_in_whitelist", "Jogador não está na whitelist."));
            return;
        }
        whitelist.remove(playerName);
        plugin.getConfig().set("whitelist.players", whitelist);
        plugin.saveConfig();
        sender.sendMessage(getMessage("player_removed", "Jogador removido da whitelist."));
    }

    @Subcommand("list")
    @Description("Lista os jogadores na whitelist")
    public void onList(CommandSender sender) {
        String perm = plugin.getConfig().getString("permissions.list", "simplewhitelist.whitelist.list");
        if (!sender.hasPermission(perm)) {
            sender.sendMessage(getMessage("no_permission", "Você não tem permissão para usar esse comando."));
            return;
        }
        List<String> whitelist = plugin.getConfig().getStringList("whitelist.players");
        if (whitelist.isEmpty()) {
            sender.sendMessage(getMessage("empty_whitelist", "A whitelist está vazia."));
            return;
        }
        sender.sendMessage(getMessage("whitelist_list_header", "Jogadores na whitelist:"));
        for (String player : whitelist) {
            sender.sendMessage(ChatColor.AQUA + "- " + player);
        }
    }

    @Subcommand("on")
    @Description("Ativa a whitelist e kicka jogadores não autorizados")
    public void onEnableWhitelist(CommandSender sender) {
        String perm = plugin.getConfig().getString("permissions.on", "simplewhitelist.whitelist.on");
        if (!sender.hasPermission(perm)) {
            sender.sendMessage(getMessage("no_permission", "Você não tem permissão para usar esse comando."));
            return;
        }
        plugin.getConfig().set("whitelist.enabled", true);
        plugin.saveConfig();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(plugin.getConfig().getString("permissions.bypass", "simplewhitelist.bypass"))) {
                continue;
            }
            List<String> whitelistList = plugin.getConfig().getStringList("whitelist.players");
            if (!whitelistList.contains(player.getName())) {
                String kickMsg = formatMessageList(plugin.getConfig().getStringList("whitelist.kickMessage"));
                player.kickPlayer(kickMsg);
            }
        }
        sender.sendMessage(getMessage("whitelist_enabled", "Whitelist ativada."));
    }

    @Subcommand("off")
    @Description("Desativa a whitelist")
    public void onDisableWhitelist(CommandSender sender) {
        String perm = plugin.getConfig().getString("permissions.off", "simplewhitelist.whitelist.off");
        if (!sender.hasPermission(perm)) {
            sender.sendMessage(getMessage("no_permission", "Você não tem permissão para usar esse comando."));
            return;
        }
        plugin.getConfig().set("whitelist.enabled", false);
        plugin.saveConfig();
        sender.sendMessage(getMessage("whitelist_disabled", "Whitelist desativada."));
    }

    private String formatMessageList(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(ChatColor.translateAlternateColorCodes('&', line)).append("\n");
        }
        return sb.toString().trim();
    }
}