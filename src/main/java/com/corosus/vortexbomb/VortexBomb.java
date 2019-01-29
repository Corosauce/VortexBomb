package com.corosus.vortexbomb;

import org.bukkit.plugin.java.JavaPlugin;

public class VortexBomb extends JavaPlugin {

    private static VortexBomb instance;

    private EventListener eventListener;

    private EffectManager effectManager;

    private TickRunner tickRunner;

    public static VortexBomb getInstance() {
        return instance;
    }

    public static void setInstance(VortexBomb instance) {
        VortexBomb.instance = instance;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    @Override
    public void onEnable() {
        setInstance(this);
        eventListener = new EventListener();
        effectManager = new EffectManager();
        tickRunner = new TickRunner();

        getServer().getPluginManager().registerEvents(eventListener, this);

        tickRunner.runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        tickRunner.cancel();
    }
}
