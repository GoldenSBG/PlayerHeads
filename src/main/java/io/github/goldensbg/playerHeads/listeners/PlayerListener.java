package io.github.goldensbg.playerHeads.listeners;

import io.github.goldensbg.playerHeads.PlayerHeads;
import io.github.goldensbg.playerHeads.api.PlayerHeadsAPI;
import io.github.goldensbg.playerHeads.api.SkinSource;
import io.github.goldensbg.playerHeads.api.implementation.MojangSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final PlayerHeads plugin;

    public PlayerListener(PlayerHeads plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        PlayerHeadsAPI.getInstance().getHead(event.getPlayer());
    }


    //ToDo add the new Resourcepack with the spacings

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getPluginConfig().getAutoDownloadPackEnabled() && plugin.getServer().getResourcePack().isEmpty())
            event.getPlayer().setResourcePack(PlayerHeads.RESOURCE_PACK);

        if (plugin.getPluginConfig().getJoinMessagesEnabled()) {
            event.setJoinMessage(null);

            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                event.setJoinMessage(insertPlayerHead(event.getJoinMessage(), event.getPlayer()));
            }, 20 * plugin.getPluginConfig().getJoinMessagesDelaySeconds());
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.getPluginConfig().getLeaveMessagesEnabled()) return;
        event.setQuitMessage(insertPlayerHead(event.getQuitMessage(), event.getPlayer()));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!plugin.getPluginConfig().getChatMessagesEnabled()) return;

        String msg = String.format(event.getFormat(), event.getPlayer().getName(), event.getMessage());
        event.setMessage(insertPlayerHead(msg, event.getPlayer()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getPluginConfig().getDeathMessagesEnabled()) return;
        event.setDeathMessage(insertPlayerHead(event.getDeathMessage(), event.getEntity()));
    }

    private String insertPlayerHead(String message, Player player) {
        SkinSource skinSource = plugin.getPluginConfig().getServerOnlineMode()
                ? PlayerHeadsAPI.defaultSource
                : new MojangSource(false);

        PlayerHeadsAPI api = PlayerHeadsAPI.getInstance();
        Component head = api.getHead(player, plugin.getPluginConfig().getSkinOverlayEnabled(), skinSource);

        if (head != null) {
            String headString = LegacyComponentSerializer.legacySection().serialize(head);
            return headString + " " + message;
        } else {
            return message;
        }
    }
}