package com.corosus.vortexbomb;

import org.bukkit.entity.Entity;

public class EntityState {

    private Entity entity;

    public boolean hitMiddle = false;

    public EntityState(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
