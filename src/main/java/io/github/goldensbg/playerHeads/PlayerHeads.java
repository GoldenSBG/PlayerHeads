package io.github.goldensbg.playerHeads;

import io.github.goldensbg.playerHeads.api.PlayerHeadsAPI;
import io.github.goldensbg.playerHeads.config.Config;
import io.github.goldensbg.playerHeads.examples.ActionBarExample;
import io.github.goldensbg.playerHeads.examples.BossbarExample;
import io.github.goldensbg.playerHeads.examples.ServerPingListener;
import io.github.goldensbg.playerHeads.hooks.PlaceholderAPIHook;
import io.github.goldensbg.playerHeads.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getPluginManager;

public final class PlayerHeads extends JavaPlugin {

    public static final String RESOURCE_PACK = "https://github.com/GoldenSBG/PlayerHeads/raw/main/PlayerHeads%20ResourcePack.zip\n";
    private Config config;

    @Override
    public void onEnable() {
        PlayerHeadsAPI.initialize(this);
        this.config = new Config(this);
        this.config.init();
        this.registerListeners();

        if (getPluginManager().getPlugin("PlaceholderAPI") != null){
            PlaceholderAPIHook.registerHook(this);
            getLogger().info("Hooked into PlaceholderAPI!");
        }

        registerExamples();
    }

    private void registerExamples() {
        getPluginManager().registerEvents(new ActionBarExample(), this);
        getPluginManager().registerEvents(new ServerPingListener(), this);
        getPluginManager().registerEvents(new BossbarExample(), this);
    }

    private void registerListeners() {
        getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @NotNull
    public Config getPluginConfig() {
        return config;
    }
}
