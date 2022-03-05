package deno;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;
import cn.nukkit.utils.TextFormat;
import deno.arena.Arena;
import deno.game.GamePlayers;
import deno.game.GameSchedule;

public class EventListener implements Listener {
    
    @EventHandler
    public void on(BlockBreakEvent e) {
        
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Item ItemInHand = p.getInventory().getItemInHand();
        
        if(ItemInHand.getId() == 280 && ItemInHand.hasEnchantment(22) && ItemInHand.getCustomName().equals(TextFormat.GOLD + "Farbarenawerkzeug")) {
            
            e.setCancelled();
            
            
            if(!p.getLevel().equals(Arena.getGameWorld())) {
                
                p.sendMessage(TextFormat.RED + "Du bist nicht in der Farbarena Welt!");
                return;
                
            }
            if(Arena.isFloorSaved()) {
                
                if(!Arena.getBoard().isMarked()) {
                    
                    Arena.getBoard().setFirstAngle(b.getLocation());
                    p.sendMessage(TextFormat.GREEN + "Erste Position makiert!");
                    return;
                }
                

                Arena.getBoard().setSecondAngle(b.getLocation());
                p.getInventory().removeItem(ItemInHand);
                Arena.getBoard().create();
                p.sendMessage(TextFormat.GREEN + "Tafel erfolgreich erstellt!");
                
                return;
                
            }
            if(!Arena.getFloor().isMarked()) {
                
                Arena.getFloor().setFirstAngle(b.getLocation());
                p.sendMessage(TextFormat.GREEN + "Erste Position makiert!");
                return;
            }
            

            Arena.getFloor().setSecondAngle(b.getLocation());
            p.getInventory().removeItem(ItemInHand);
            Arena.getFloor().create();
            p.sendMessage(TextFormat.GREEN + "Boden erfolgreich erstellt!");
        }
        
    }
    @EventHandler
    public void on(PlayerFormRespondedEvent e) {
        
        Player p = (Player) e.getPlayer();
        
        if(e.getFormID() == 4444 && !(e.wasClosed())) {
            
            if(GamePlayers.getWaiters().size() >= GamePlayers.getMaxPlayers() && !(GamePlayers.getWaiters().contains(p))) { 
                
                GamePlayers.getWatchers().add(p);
                GamePlayers.sendPopup(p, TextFormat.DARK_GREEN + "Du hast dich erfolgreich in der" + TextFormat.GOLD + " Farbarena " + TextFormat.DARK_GREEN + "Zuschauerliste eingetragen.");
                
                return;
                
            }
            if(Arena.isGameStarted() == false) {
                
                if(GamePlayers.getWatchers().contains(p)) { 
                    
                    GamePlayers.getWatchers().remove(p);
                    GamePlayers.sendPopup(p, TextFormat.DARK_GREEN + "Du bist erfolgreich von der " + TextFormat.GOLD + " Farbarena " + TextFormat.DARK_GREEN + "Zuschauerliste ausgetreten.");
                    
                    return;
                    
                }
                
                if(!GamePlayers.getWaiters().contains(p)) {
                    
                    GamePlayers.getWaiters().add(p);
                    
                    String text = (GamePlayers.getWaiters().size() > 1) ? "nehmen " : "nimmt ";
                    
                    if(GamePlayers.getWaiters().size() >= GamePlayers.getMinPlayers()) {
                        
                        if(GamePlayers.isEnoughPlayers() == false) {
                            
                            GamePlayers.sendPopupToAll(TextFormat.DARK_GREEN + "Es sind genug Spieler in der Warteliste eingetragen, das Spiel beginnt gleich...");
                            
                            GamePlayers.PlaySound(Sound.NOTE_HAT);
                            
                            GameSchedule.RoundStart();
                            
                            GamePlayers.setEnoughPlayers(true);
                            
                        }
                        
                        return;
                    }
                    
                    GamePlayers.sendPopup(p, TextFormat.DARK_GREEN + "Das Spiel startet, sobald genug Spieler teilnehmen möchten :D\nDu wirst dann umgehend informiert. Derzeit " + text + TextFormat.GOLD + GamePlayers.getWaiters().size() + TextFormat.DARK_GREEN + " Spieler teil");
                    
                    return;
                    
                }
                
                if(GamePlayers.getWaiters().contains(p)) GamePlayers.getWaiters().remove(p);
                
                if(GamePlayers.getWaiters().size() < GamePlayers.getMinPlayers()) {
                    
                    Arena.setBeforeGameStartsTime(15);
                    GamePlayers.setEnoughPlayers(false);
                    GameSchedule.RoundTask.cancel();
                    
                }
                
                GamePlayers.sendPopup(p, TextFormat.DARK_GREEN + "Du bist erfolgreich von der" + TextFormat.GOLD + " Farbarena " + TextFormat.DARK_GREEN + "Warteliste ausgetreten.");
                
            } else if(!GamePlayers.getGamers().contains(p)) {
                
                if(!Arena.isWatcherSpawnSaved()) {
                    
                    p.sendMessage(TextFormat.RED + "Du kannst leider nicht zusehen, es gibt keinen Zuschauerspawn :/");
                    return;
                    
                }
                
                GamePlayers.getWatchers().add(p);
                
                p.teleport(Arena.getWatcherSpawn());
            }
            
        }
        
    }
    
    @EventHandler
    public void on(PlayerJoinEvent e) {
        
        e.getPlayer().setGamemode(2);
        e.getPlayer().setFoodEnabled(false);
        e.getPlayer().getFoodData().reset();
        
    }
    @EventHandler
    public synchronized void on(DataPacketReceiveEvent  e) {
        
        if (e.getPacket() instanceof SetLocalPlayerAsInitializedPacket) {
            
            e.getPlayer().teleport(Arena.getLobbyWorld().getSpawnLocation());
            
        }
        
    }
    @EventHandler
    public void on(InventoryClickEvent e) {
        
        e.setCancelled();
        
    }
    @EventHandler
    public void on(PlayerDropItemEvent e) {
        
        e.setCancelled();
        
    }
    @EventHandler
    public void on(PlayerQuitEvent e) {
        
        Player p = e.getPlayer();
        
        if(GamePlayers.getWaiters().contains(p))
            GamePlayers.getWaiters().remove(p);
        if(GamePlayers.getWatchers().contains(p))
            GamePlayers.getWatchers().remove(p);        
        if(Arena.isGameStarted() && GamePlayers.getWaiters().size() < GamePlayers.getMinPlayers()) {
            
            Arena.setBeforeGameStartsTime(15);
            GamePlayers.setEnoughPlayers(false);
            GameSchedule.RoundTask.cancel();
            
        }
    }
    
}
