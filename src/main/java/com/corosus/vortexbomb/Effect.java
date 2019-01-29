package com.corosus.vortexbomb;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.*;

public class Effect {

    private Location location;

    private HashMap<Entity, EntityState> lookupEntityToState = new HashMap<>();

    private int timer = 0;
    private int timerMax = 200;

    private double maxRange = 45;

    Random rand = new Random();

    Effect(Location location) {
        this.location = location;

        location.getWorld().playEffect(location, org.bukkit.Effect.DRAGON_BREATH, 20);
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
    }

    public void addEntity(Entity entity) {
        lookupEntityToState.put(entity, new EntityState(entity));
    }

    public void tick() {
        if (timer < timerMax) {
            timer++;
        }

        spawnParticleEffects();

        addNewEntitiesInRange();

        tickEntities();
    }

    public void tickEntities() {

        for (EntityState entState : lookupEntityToState.values()) {

            Entity ent = entState.getEntity();

            Location locXZ = ent.getLocation();
            locXZ.setY(location.getY());
            if (!entState.hitMiddle) {
                if (locXZ.distance(location) < 3) {
                    entState.hitMiddle = true;
                }
            }

            double xVec = ent.getLocation().getX() - location.getX();
            double zVec = ent.getLocation().getZ() - location.getZ();

            double yVec = Math.max(ent.getLocation().getY() - location.getY(), 20);

            double angle = Math.atan2(zVec, xVec);

            double speed = 0.1D;

            Vector motion = ent.getVelocity();

            if (!entState.hitMiddle) {
                angle += Math.toRadians(90);
                motion.setY(0.05D);

            } else {

                if (timer > timerMax-40) {
                    angle += Math.toRadians(65+yVec);

                    double xzDistFactor = 5D / Math.min(Math.max(0.1D, locXZ.distance(location)), 5D);

                    if (location.getY()-1 < ent.getLocation().getY()) {
                        motion.setY(-0.4D * xzDistFactor);
                    } else {
                        motion.setY(0.4D);
                    }
                } else {
                    angle += Math.toRadians(65-yVec);
                    motion.setY(0.1D);
                }

            }

            double xMotion = -Math.sin(angle) * speed;
            double zMotion = Math.cos(angle) * speed;

            motion.add(new Vector(xMotion, 0.0, zMotion));

            ent.setVelocity(motion);

            ent.setFallDistance(0);

        }
    }

    public void addNewEntitiesInRange() {
        double range = (((double)timer / (double)timerMax)) * maxRange;
        Collection<Entity> listScanEntities = location.getWorld().getNearbyEntities(location, range, range, range);

        for (Entity ent : listScanEntities) {
            if ((ent instanceof Creature) && !lookupEntityToState.containsKey(ent)) {
                addEntity(ent);
            }
        }
    }

    public void spawnParticleEffects() {

        double range = (((double)timer / (double)timerMax)) * maxRange;
        double particleSpeed = 0.02D * range;

        for (int i = 0; i < 10; i++) {
            double xVec = (rand.nextDouble() - rand.nextDouble()) * particleSpeed;
            double yVec = (rand.nextDouble() - rand.nextDouble()) * particleSpeed;
            double zVec = (rand.nextDouble() - rand.nextDouble()) * particleSpeed;

            double xPos = (rand.nextDouble() - rand.nextDouble()) * range;
            double yPos = (rand.nextDouble() - rand.nextDouble()) * range;
            double zPos = (rand.nextDouble() - rand.nextDouble()) * range;

            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 0, xVec, yVec, zVec);
            location.getWorld().spawnParticle(Particle.PORTAL, location.clone().add(xPos, yPos, zPos), 0, xVec, yVec, zVec);
        }
    }

    public void onComplete() {
        double particleSpeed = 3D;
        for (EntityState entState : lookupEntityToState.values()) {

            double xVec = (rand.nextDouble() - rand.nextDouble()) * particleSpeed;
            double yVec = rand.nextDouble() * particleSpeed;
            double zVec = (rand.nextDouble() - rand.nextDouble()) * particleSpeed;

            entState.getEntity().setVelocity(new Vector(xVec, yVec, zVec));
        }

        for (int i = 0; i < 80; i++) {
            double xVec = (rand.nextDouble() - rand.nextDouble()) * particleSpeed;
            double yVec = (rand.nextDouble() - rand.nextDouble()) * particleSpeed;
            double zVec = (rand.nextDouble() - rand.nextDouble()) * particleSpeed;

            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 0, xVec, yVec, zVec);
        }

        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
    }

    public boolean isComplete() {
        return timer == timerMax;
    }

    public void detonate() {
        timer = timerMax;
    }

}
