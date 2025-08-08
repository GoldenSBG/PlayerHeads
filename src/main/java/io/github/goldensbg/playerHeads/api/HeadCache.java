package io.github.goldensbg.playerHeads.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HeadCache {
    private static final long CACHE_EXPIRATION = 5 * 60 * 1000; // 5 minutes

    private final JavaPlugin plugin;
    private final Map<String, CachedHead> cache = new ConcurrentHashMap<>();
    private final Map<String, Boolean> pendingRequests = new ConcurrentHashMap<>();
    private BukkitTask cacheCleanupTask;

    public HeadCache(JavaPlugin plugin) {
        this.plugin = plugin;
        startCacheCleanupTask();
    }

    public Component getCachedHead(UUID uuid, boolean overlay, SkinSource skinSource) {
        return getCachedHead(Bukkit.getOfflinePlayer(uuid), overlay, skinSource);
    }

    public Component getCachedHead(OfflinePlayer player, boolean overlay, SkinSource skinSource) {
        UUID uuid = player.getUniqueId();
        String cacheKey = getCacheKey(uuid, overlay);
        CachedHead cachedHead = cache.get(cacheKey);
        if (cachedHead != null && !isExpired(cachedHead)) {
            return cachedHead.getHead();
        }

        Component lastHead = cachedHead != null ? cachedHead.getHead() : Component.empty();

        if (pendingRequests.putIfAbsent(cacheKey, true) == null) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                Component legacyHead = skinSource.getHead(player, overlay);
                if (legacyHead != null && plugin.isEnabled()) {
                    String legacyText = LegacyComponentSerializer.legacySection().serialize(legacyHead);
                    Component modernHead = LegacyComponentSerializer.legacySection().deserialize(legacyText);
                    cache.put(cacheKey, new CachedHead(modernHead, overlay, System.currentTimeMillis()));
                }
                pendingRequests.remove(cacheKey);
            });
        }

        return lastHead;
    }

    private boolean isExpired(CachedHead cachedHead) {
        return System.currentTimeMillis() - cachedHead.getTimestamp() > CACHE_EXPIRATION;
    }

     private void startCacheCleanupTask() {
        if (cacheCleanupTask != null) {
            cacheCleanupTask.cancel();
        }

        cacheCleanupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            cache.entrySet().removeIf(entry -> isExpired(entry.getValue()));
        }, CACHE_EXPIRATION / 20, CACHE_EXPIRATION / 20);
    }

    private String getCacheKey(UUID uuid, boolean overlay) {
        return uuid.toString() + ":" + overlay;
    }

    private static class CachedHead {
        private final Component head;

        private final boolean overlay;

        private final long timestamp;

        CachedHead(Component head, boolean overlay, long timestamp) {
            this.head = head;
            this.overlay = overlay;
            this.timestamp = timestamp;
        }

        public Component getHead() {
            return head;
        }

        public boolean hasOverlay() {
            return overlay;
        }
        public long getTimestamp() {
            return timestamp;
        }
    }
}