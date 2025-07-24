package io.github.goldensbg.playerHeads.examples;

import io.github.goldensbg.playerHeads.api.PlayerHeadsAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerPingListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        PlayerHeadsAPI playerHeadsAPI = PlayerHeadsAPI.getInstance();

        Component head = playerHeadsAPI.getHead(player, true, PlayerHeadsAPI.defaultSource);
        Component msg = Component.text(" " + player.getName() + " joined the game", NamedTextColor.YELLOW);
        Component fullMessage = Component.empty().append(head).append(msg);

        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(fullMessage));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        PlayerHeadsAPI playerHeadsAPI = PlayerHeadsAPI.getInstance();

        Component head = playerHeadsAPI.getHead(player, true, PlayerHeadsAPI.defaultSource);
        Component msg = Component.text(" " + player.getName() + " left the game", NamedTextColor.YELLOW);
        Component fullMessage = Component.empty().append(head).append(msg);

        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(fullMessage));

    }


}