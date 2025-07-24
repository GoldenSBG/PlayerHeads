package io.github.goldensbg.playerHeads.api;

import io.github.goldensbg.playerHeads.api.implementation.CrafatarSource;
import io.github.goldensbg.playerHeads.api.implementation.McHeadsSource;
import io.github.goldensbg.playerHeads.api.implementation.MojangSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerHeadsAPI {

    public static SkinSource defaultSource;

    private static PlayerHeadsAPI instance;

    private final JavaPlugin plugin;
    private final HeadCache headCache;

    public PlayerHeadsAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        this.headCache = new HeadCache(plugin);
    }

    public static PlayerHeadsAPI getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("PlayerHeadsAPI has not been initialized.");
        }
        return instance;
    }

    public static void initialize(JavaPlugin plugin) {
        if (instance != null) {
            throw new IllegalStateException("PlayerHeadAPI has already been initialized.");
        }

        String skinSourceConfig = plugin.getConfig().getString("skin-source", "MOJANG");
        defaultSource = switch (skinSourceConfig.toUpperCase()) {
            case "CRAFATAR" -> new CrafatarSource();
            case "MCHEADS" -> new McHeadsSource();
            default -> new MojangSource();
        };

        instance = new PlayerHeadsAPI(plugin);
    }

    public Component getHead(UUID uuid) {
        return headCache.getCachedHead(uuid, true, defaultSource);
    }

    public Component getHead(UUID uuid, boolean overlay) {
        return headCache.getCachedHead(uuid, overlay, defaultSource);
    }

    public Component getHead(UUID uuid, boolean overlay, SkinSource skinSource) {
        return headCache.getCachedHead(uuid, overlay, skinSource);
    }

    public Component getHead(OfflinePlayer player) {
        return headCache.getCachedHead(player, true, defaultSource);
    }

    public Component getHead(OfflinePlayer player, boolean overlay) {
        return headCache.getCachedHead(player, overlay, defaultSource);
    }

    public Component getHead(OfflinePlayer player, boolean overlay, SkinSource skinSource) {
        return headCache.getCachedHead(player, overlay, skinSource);
    }

    public String getHeadAsString(UUID uuid, boolean overlay, SkinSource skinSource) {
        return getHeadAsString(Bukkit.getOfflinePlayer(uuid), true, defaultSource);
    }

    public String getHeadAsString(OfflinePlayer player, boolean overlay, SkinSource skinSource) {
        Component component = getHead(player, overlay, skinSource);
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public String getHeadAsString(OfflinePlayer player) {
        return getHeadAsString(player, true, defaultSource);
    }

    // Optional
    /*
    public BaseComponent[] getHeadLegacy(OfflinePlayer player, boolean overlay, SkinSource skinSource) {
        String legacyText = getHeadAsString(player, overlay, skinSource);
        return new BaseComponent[]{ new TextComponent(legacyText) };
    }
    */
}
