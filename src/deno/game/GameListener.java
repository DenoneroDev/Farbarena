package deno.game;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {
    
    
    @EventHandler
    public void on(PlayerQuitEvent e) {
        
        Player p = e.getPlayer();
        if(GamePlayers.getGamers().contains(p))
            GamePlayers.GameOver(p);
        
        
    }
    @SuppressWarnings("deprecation")
    @EventHandler
    public void on(EntityDamageEvent e) {
        
        if(!(e.getEntity() instanceof Player))
            return;
        
            
        Player p = (Player) e.getEntity();
        
        p.setScale(1);
        p.setAllowFlight(false);
        
        if(!GamePlayers.getGamers().contains(p))
            return;
        
        e.setCancelled();
        GamePlayers.GameOver(p);
        
    }
}
