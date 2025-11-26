package com.mariomastr.mobDuper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class MobSpawnListener implements Listener {

    private final Plugin plugin;

    public MobSpawnListener(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        LivingEntity entity = event.getEntity();
        EntityType type = event.getEntityType();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if(reason != CreatureSpawnEvent.SpawnReason.CUSTOM && type != EntityType.SLIME){
            for(int i = 0; i <= plugin.getConfig().getInt("mob_count"); i++){
                World world = event.getEntity().getWorld();
                assert type.getEntityClass() != null;
                world.spawn(entity.getLocation(), type.getEntityClass());
            }
        }
    }


}
