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

import java.util.Random;

public class SpawnListener implements Listener {

    private final Plugin plugin;


    public SpawnListener(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        LivingEntity entity = event.getEntity();
        EntityType type = event.getEntityType();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if(!plugin.getConfig().getBoolean("dupe_mobs.enabled")) return;

        if(reason != CreatureSpawnEvent.SpawnReason.CUSTOM && type != EntityType.SLIME){
            for(int i = 0; i < plugin.getConfig().getInt("dupe_mobs.count"); i++){
                World world = event.getEntity().getWorld();
                assert type.getEntityClass() != null;
                world.spawn(entity.getLocation(), type.getEntityClass());
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event){
        Projectile projectile = event.getEntity();

        double range = plugin.getConfig().getInt("dupe_projectiles.spawn_range");

        if((event.getEntityType() == EntityType.ENDER_PEARL && !plugin.getConfig().getBoolean("dupe_projectiles.do_ender_pearl")) || !plugin.getConfig().getBoolean("dupe_projectiles.enabled")) return;


        if(event.getEntityType() == EntityType.ENDER_PEARL && projectile.getShooter() instanceof  Player player){
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for(int i = 0; i < plugin.getConfig().getInt("dupe_projectiles.count"); i++){
                    World world = event.getEntity().getWorld();
                    Location location = getLocation(range, world, projectile);
                    EnderPearl pearl = world.spawn(location, EnderPearl.class);
                    pearl.setShooter(player);
                    pearl.setVelocity(projectile.getVelocity());
                }
            }, plugin.getConfig().getInt("dupe_projectiles.spawn_delay"));
        }
    }

    private static Location getLocation(double range, World world, Projectile projectile) {
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
}
