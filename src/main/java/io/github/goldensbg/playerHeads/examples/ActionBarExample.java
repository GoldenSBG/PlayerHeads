package io.github.goldensbg.playerHeads.examples;

import io.github.goldensbg.playerHeads.api.PlayerHeadsAPI;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ActionBarExample implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerHeadsAPI playerHeadsAPI = PlayerHeadsAPI.getInstance();

        Component head = playerHeadsAPI.getHead(player, true, PlayerHeadsAPI.defaultSource);

        player.sendActionBar(head);

    }

}