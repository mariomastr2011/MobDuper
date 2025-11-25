package com.mariomastr.mobDuper;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.World;

import javax.swing.text.html.parser.Entity;

public class MobSpawnListener implements Listener {
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        LivingEntity entity = event.getEntity();
        EntityType type = event.getEntityType();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if(reason != CreatureSpawnEvent.SpawnReason.CUSTOM){
            for(int i = 0; i <= 8; i++){
                World world = event.getEntity().getWorld();
                assert type.getEntityClass() != null;
                world.spawn(entity.getLocation(), type.getEntityClass());
            }
        }
    }
}
