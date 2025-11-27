package com.mariomastr.mobDuper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.World;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class SpawnListener implements Listener {

    private final Plugin plugin;
    public final Set<UUID> duplicates = new HashSet<>();


    public SpawnListener(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        LivingEntity entity = event.getEntity();
        EntityType type = event.getEntityType();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        double range = plugin.getConfig().getInt("dupe_mobs.range");

        if(!plugin.getConfig().getBoolean("dupe_mobs.enabled")) return;

        if(reason != CreatureSpawnEvent.SpawnReason.CUSTOM && type != EntityType.SLIME){
            World world = event.getEntity().getWorld();
            assert type.getEntityClass() != null;

            for(int i = 0; i < plugin.getConfig().getInt("dupe_mobs.count") - 1; i++){
                if(plugin.getConfig().getBoolean("dupe_mobs.randomize_mob_location")){
                    world.spawn(getLocationMob(range, entity.getWorld(), entity), type.getEntityClass());
                }
                else{
                    world.spawn(entity.getLocation(), type.getEntityClass());
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event){
        Projectile projectile = event.getEntity();

        double range = plugin.getConfig().getInt("dupe_projectiles.spawn_range");

        if((event.getEntityType() == EntityType.ENDER_PEARL && !plugin.getConfig().getBoolean("dupe_projectiles.do_ender_pearl"))
                || !plugin.getConfig().getBoolean("dupe_projectiles.enabled")) return;


        if(event.getEntityType() == EntityType.ENDER_PEARL && projectile.getShooter() instanceof  Player player){
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for(int i = 0; i < plugin.getConfig().getInt("dupe_projectiles.count") - 1; i++){
                    World world = event.getEntity().getWorld();
                    Location location = getLocationProjectile(range, world, projectile);
                    EnderPearl pearl = world.spawn(location, EnderPearl.class);
                    pearl.setShooter(player);
                    pearl.setVelocity(projectile.getVelocity());
                }
            }, plugin.getConfig().getInt("dupe_projectiles.spawn_delay"));
        }

        if(event.getEntityType() != EntityType.ENDER_PEARL){
            if(duplicates.contains(event.getEntity().getUniqueId())) return;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for(int i = 0; i < plugin.getConfig().getInt("dupe_projectiles.count") - 1; i++){
                    World world = event.getEntity().getWorld();
                    Class<? extends Entity> projectileClass = projectile.getType().getEntityClass();
                    if(projectileClass == null) return;

                    Projectile dup = world.spawn(getLocationProjectile(range, world, projectile),
                            projectileClass.asSubclass(Projectile.class),
                            entity -> {
                                duplicates.add(entity.getUniqueId());
                            });


                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        duplicates.remove(dup.getUniqueId());
                    }, 200);

                    dup.setVelocity(projectile.getVelocity());

                     if(projectile.getShooter() instanceof LivingEntity shooter){
                         dup.setShooter(shooter);
                     }
                }
            }, plugin.getConfig().getInt("dupe_projectiles.spawn_delay"));
        }
    }


    private static Location getLocationProjectile(double range, World world, Projectile projectile) {
        Random random = new Random();
        double offsetX = (random.nextDouble()  * 2 * range) - range;
        double offsetY = (random.nextDouble()  * 2 * range) - range;
        double offsetZ = (random.nextDouble()  * 2 * range) - range;
        return new Location(
                world,
                projectile.getLocation().getX() + offsetX,
                projectile.getLocation().getY() + offsetY,
                projectile.getLocation().getZ() + offsetZ);
    }

    private static Location getLocationMob(double range, World world, Entity entity) {
        Random random = new Random();
        double offsetX = (random.nextDouble()  * 2 * range) - range;
        double offsetY = (random.nextDouble()  * 2 * range) - range;
        double offsetZ = (random.nextDouble()  * 2 * range) - range;
        return new Location(
                world,
                entity.getLocation().getX() + offsetX,
                entity.getLocation().getY() + offsetY,
                entity.getLocation().getZ() + offsetZ);
    }
}
