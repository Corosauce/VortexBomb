package com.corosus.vortexbomb;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class EventListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEggThrow(final PlayerEggThrowEvent event) {
        event.setHatching(false);
        VortexBomb.getInstance().getEffectManager().addEffect(new Effect(event.getEgg().getLocation()));
    }

}
