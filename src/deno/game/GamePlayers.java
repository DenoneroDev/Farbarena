package deno.game;

import java.util.concurrent.CopyOnWriteArrayList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.TextFormat;
import deno.Main;
import deno.arena.Arena;

public class GamePlayers {
    
    private static CopyOnWriteArrayList<Player> Waiting = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<Player> Watcher = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<Player> Gamer = new CopyOnWriteArrayList<>();
    private static int max = 10;
    private static int min = 1; //TODO 2
    private static boolean isEnoughPlayers = false;
    
    public static CopyOnWriteArrayList<Player> getWaiters() {
        
        return Waiting;
        
    }
    
    public static int getMaxPlayers() {
        
        return max;
        
    }
    public static int getMinPlayers() {
        
        return min;
        
    }
    public static CopyOnWriteArrayList<Player> getWatchers() {
        
        return Watcher;
        
    }
    public static CopyOnWriteArrayList<Player> getGamers() {
        
        return Gamer;
        
    }
    public static boolean isEnoughPlayers() {
        
        return isEnoughPlayers;
        
    }
    public static void setEnoughPlayers(boolean b) {
        
        isEnoughPlayers = b;
        
    }
    public static void sendPopup(Player p, String msg) {
        
        p.sendTip(msg);
        
    }
    public static void sendCountPopup(Player p, String msg) {
        
        TextPacket pk = new TextPacket();
        pk.type = TextPacket.TYPE_JUKEBOX_POPUP;
        
        if(p.getGamemode() == 1) {
            
            pk.message = msg + "\n\n ";
            
        } else {
            
            pk.message = msg;
            
        }
        p.dataPacket(pk);
        
        /* p.sendActionBar(msg); */ //Das gleiche, nur mit schwarzen Hintergrund.
        
    }
    public static void sendCountPopupToAll(String message) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> {
                
                sendCountPopup(waiter, message);
                
            });
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> {
                
                sendCountPopup(watcher, message);
                
            });
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> {
                
                sendCountPopup(gamer, message);
                
            });
            
        }
        
    }
    public static void sendPopupToAll(String message) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> {
                
                sendPopup(waiter, message);
                
            });
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> {
                
                sendPopup(watcher, message);
                
            });
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> {
                
                sendPopup(gamer, message);
                
            });
            
        }
        
    }
    public static void playSoundFor(Player p, Sound s) {
        
        PlaySoundPacket pk = new PlaySoundPacket();
        
        pk.name = s.getSound();
        pk.volume = 1;
        pk.pitch = 1;
        pk.x = (int) p.x;
        pk.y = (int) p.y;
        pk.z = (int) p.z;
        
        p.dataPacket(pk);
        
    }
    
    public static void PlaySound(Sound s) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> {
                
                playSoundFor(waiter, s);
                
            });
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> {
                
                playSoundFor(watcher, s);
                
            });
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> {
                
                playSoundFor(gamer, s);
                
            });
            
        }
        
    }
    
    public static void teleport(Position l) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> {
                
                waiter.teleport(l);
                
            });
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> {
                
                watcher.teleport(l);
                
            });
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> {
                
                gamer.teleport(l);
                
            });
            
        }
        
    }
    public static void clearInventorys() {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> {
                
                if(waiter.getInventory() != null) waiter.getInventory().clearAll();
                
            });
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> {
                
                if(watcher.getInventory() != null) watcher.getInventory().clearAll();
                
            });
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> {
                
                if(gamer.getInventory() != null) gamer.getInventory().clearAll();
                
            });
            
        }
        
    }
    public static void showColor(int c) {
        
        Gamer.forEach(gamer -> {
            
            gamer.getInventory().clearAll();
            
            for(int i = 0; i != gamer.getInventory().getHotbarSize(); i++) {
                
                Item item = Item.get(Item.WOOL, c);
                
                item.setCustomName("");
                
                gamer.getInventory().setItem(i, item);
                
            }
            
        });
        
    }
    public static void sendMessageToAll(String msg) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> {
                
                waiter.sendMessage(msg);
                
            });
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> {
                
                watcher.sendMessage(msg);
            
            });
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> {
                
                gamer.sendMessage(msg);
                
            });
            
        }
        
    }
    public static void GameOver(Player p) {
        
        if(!p.getInventory().isEmpty()) 
            p.getInventory().clearAll();
        if(p.isOnline())
            p.teleport(Arena.getWatcherSpawn());
        if(Gamer.size() > 1) {
            
            sendMessageToAll(TextFormat.GOLD + "#" + Gamer.size() + "  " + TextFormat.DARK_RED + p.getName() + " hat verloren!");
            Gamer.remove(p);
            
            if(p.isOnline()) {
                
                Watcher.add(p);
                
            }
            
            return;
        }

        end();
        
    }
    public static void end() {
        
        Arena.getFloor().placedBlockLocations.clear();
        Arena.getFloor().usedIntegers.clear();
        Arena.getFloor().removedBlocks.clear();
        GameSchedule.runden = 0;
        GameSchedule.time = 6;
        GameSchedule.i = 5;
        
        Arena.getFloor().reset();
        Arena.getBoard().reset();
        
        for(TaskHandler Task : GameSchedule.allTasks) {
            
            Task.cancel();
            
        }
        GameSchedule.allTasks.clear();
            
        if(Gamer.get(0).isOnline()) {
            
            Gamer.get(0).sendTitle(TextFormat.GREEN + "Du hast gewonnen :D");
            sendMessageToAll(TextFormat.colorize("&3" + Gamer.get(0).getName() + " &dhat das Spiel gewonnen!"));
            
        }
            
        
        Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
            
            isEnoughPlayers = false;
            teleport(Arena.getLobbyWorld().getSpawnLocation());
                
            if(!Gamer.isEmpty()) {
                
                if(Waiting.size() >= max) {
                    
                    sendPopup(Gamer.get(0), TextFormat.DARK_RED + "Leider musst du Zusehen, da die Warteliste voll ist :(");
                    return;
                    
                }
                Waiting.add(Gamer.get(0));
                sendPopup(Gamer.get(0), TextFormat.DARK_GREEN + "Du wurdes automatisch eingetragen :D");
                
                if(Waiting.size() >= min) {
                    
                    if(isEnoughPlayers() == false) {
                        
                        sendPopupToAll(TextFormat.DARK_GREEN + "Es sind genug Spieler in der Warteliste eingetragen, das Spiel beginnt gleich...");
                        
                        PlaySound(Sound.NOTE_HAT);
                        
                        GameSchedule.RoundStart();
                        
                        GamePlayers.setEnoughPlayers(true);
                        
                    }
                    
                }
                    
            }
            
            if(!Watcher.isEmpty()) {
                
                Watcher.forEach(watcher -> {
                    
                    if(Waiting.size() >= max) {
                        
                        sendPopup(watcher, TextFormat.DARK_RED + "Leider musst du Zusehen, da die Warteliste voll ist :(");
                        
                    }
                    
                    Waiting.add(watcher);
                    sendPopup(watcher, TextFormat.DARK_GREEN + "Du wurdes automatisch eingetragen :D");
                    
                    if(Waiting.size() >= min) {
                        
                        if(isEnoughPlayers() == false) {
                            
                            sendPopupToAll(TextFormat.DARK_GREEN + "Es sind genug Spieler in der Warteliste eingetragen, das Spiel beginnt gleich...");
                            
                            PlaySound(Sound.NOTE_HAT);
                            
                            GameSchedule.RoundStart();
                            
                            GamePlayers.setEnoughPlayers(true);
                            
                        }
                        
                    }
                    
                });
                
            }
                
            Gamer.clear();
            Watcher.clear();
                
        }, 200);
        
    }
    
}
