package io.github.goldensbg.playerHeads.hooks;

import io.github.goldensbg.playerHeads.PlayerHeads;
import io.github.goldensbg.playerHeads.api.PlayerHeadsAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    private final JavaPlugin plugin;

    public PlaceholderAPIHook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "playerheads";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }


    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        PlayerHeadsAPI api = PlayerHeadsAPI.getInstance();

        // %playerheads% or %playerheadds_self% - Returns the head of the player who requested the placeholder.
        if (params.isEmpty() || params.equalsIgnoreCase("self")) {
            if (offlinePlayer == null) return "No player found!";
            Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
            if (player == null) return "You must be online!";

            return api.getHeadAsString(player, true, PlayerHeadsAPI.defaultSource);
        }

        // %playerheads_other<player>% - Returns the head of the specified player.
        if (params.startsWith("other:")) {
            String targetName = params.substring(6);
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);
            if (targetPlayer == null) return "Player not found!";

            return api.getHeadAsString(targetPlayer, true, PlayerHeadsAPI.defaultSource);
        }

        return "Invalid placeholder!";
    }


    public static void registerHook(PlayerHeads plugin) {
        new PlaceholderAPIHook(plugin).register();
    }
}