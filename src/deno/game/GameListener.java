package deno.game;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import deno.arena.Arena;

public class GameListener implements Listener {
    
    
    @EventHandler
    public void on(PlayerQuitEvent e) {
        
        Player p = e.getPlayer();
        if(GamePlayers.getGamers().contains(p))
            GamePlayers.GameOver(p);
        
        
    }
    @EventHandler
    public void on(EntityDamageEvent e) {
        
        if(!(e.getEntity() instanceof Player))
            return;
        
            
        Player p = (Player) e.getEntity();
        
        if(GamePlayers.getWatchers().contains(p)) {
            
            e.setCancelled();
            p.teleport(Arena.getWatcherSpawn());
            return;
            
        }
        
        if(!GamePlayers.getGamers().contains(p))
            return;
        
        e.setCancelled();
        GamePlayers.GameOver(p);
        
    }
    @EventHandler
    public void on(PlayerItemHeldEvent e) {
        
        if(!GamePlayers.getGamers().contains(e.getPlayer()))
            return;
        
        e.setCancelled();
        
    }
}
