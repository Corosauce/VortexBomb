package com.corosus.vortexbomb;

import org.bukkit.scheduler.BukkitRunnable;

public class TickRunner extends BukkitRunnable {

    public void run() {
        VortexBomb.getInstance().getEffectManager().tick();
    }
}
