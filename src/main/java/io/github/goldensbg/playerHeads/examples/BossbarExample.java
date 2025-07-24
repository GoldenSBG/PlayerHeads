package io.github.goldensbg.playerHeads.examples;

import io.github.goldensbg.playerHeads.api.PlayerHeadsAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BossbarExample implements Listener {

    private BossBar bossBar;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerHeadsAPI playerHeadsAPI = PlayerHeadsAPI.getInstance();

        Component head = playerHeadsAPI.getHead(player, true, PlayerHeadsAPI.defaultSource);

        String title = LegacyComponentSerializer.legacySection().serialize(head);

        bossBar = Bukkit.createBossBar(
                title.toString(),
                BarColor.BLUE,
                BarStyle.SOLID
        );

        bossBar.addPlayer(player);

    }

}
