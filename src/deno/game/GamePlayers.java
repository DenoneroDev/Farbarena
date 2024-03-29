package deno.game;

import java.util.concurrent.CopyOnWriteArrayList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.utils.TextFormat;
import deno.Main;
import deno.arena.Arena;

public class GamePlayers {
    
    private static final CopyOnWriteArrayList<Player> Waiting = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Player> Watcher = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Player> Gamer = new CopyOnWriteArrayList<>();
    private static final int max = 10;
    private static final int min = 2; //TODO 2
    private static boolean isEnoughPlayers = false;
    private static boolean canRun = true;
    
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
    public static void sendPlayerCountPopup(Player p, String msg) {
        
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
    public static void sendPlayerCountPopupToAll(String message) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> sendPlayerCountPopup(waiter, message));
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> sendPlayerCountPopup(watcher, message));
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> sendPlayerCountPopup(gamer, message));
            
        }
        
    }
    public static void sendPopupToAll(String message) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> sendPopup(waiter, message));
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> sendPopup(watcher, message));
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> sendPopup(gamer, message));
            
        }
        
    }
    public static void playSoundFor(Player p, Sound s) {
        
        playSoundFor(p, s, 1);
        
    }
    public static void playSoundFor(Player p, Sound s, int pitch) {
        
        PlaySoundPacket pk = new PlaySoundPacket();
        
        pk.name = s.getSound();
        pk.volume = 1;
        pk.pitch = pitch;
        pk.x = (int) p.x;
        pk.y = (int) p.y;
        pk.z = (int) p.z;
        
        p.dataPacket(pk);
        
    }
    
    public static void PlaySound(Sound s, int p) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> playSoundFor(waiter, s, p));
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> playSoundFor(watcher, s, p));
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> playSoundFor(gamer, s, p));
            
        }
        
    }
    
    public static void PlaySound(Sound s) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> playSoundFor(waiter, s, 1));
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> playSoundFor(watcher, s, 1));
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> playSoundFor(gamer, s, 1));
            
        }
        
    }
    
    public static void teleport(Position l) {
        
        if(!Waiting.isEmpty()) {
            
            Waiting.forEach(waiter -> waiter.teleport(l));
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> {
                
                if(watcher.getLevel() != Arena.getLobbyWorld())
                    watcher.teleport(l);
                
            });
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> {
                
                if(gamer.getLevel() != Arena.getLobbyWorld())
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
            
            Waiting.forEach(waiter -> waiter.sendMessage(msg));
            
        }
        if(!Watcher.isEmpty()) {
            
            Watcher.forEach(watcher -> watcher.sendMessage(msg));
            
        }
        if(!Gamer.isEmpty()) {
            
            Gamer.forEach(gamer -> gamer.sendMessage(msg));
            
        }
        
    }
    @SuppressWarnings("deprecation")
    public synchronized static void GameOver(Player p) {
        
        if(!p.getInventory().isEmpty()) 
            p.getInventory().clearAll();
        if(Arena.isWatcherSpawnSaved()) {
            
            if(p.isOnline())
                p.teleport(Arena.getWatcherSpawn());
            
        } else {
            
            if(p.isOnline()) {
                
                p.teleport(Arena.getLobbyWorld().getSpawnLocation());
                
            }
            
        }
        if(Gamer.size() > 0) {
            
            sendMessageToAll(TextFormat.GOLD + "#" + Gamer.size() + "  " + TextFormat.DARK_RED + p.getName() + " hat verloren!");
            Gamer.remove(p);
            
            if(p.isOnline()) {
                
                if(Arena.isWatcherSpawnSaved()) {
                    
                    Watcher.add(p);
                    p.setAllowFlight(true);
                    
                }
                Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> GamePlayers.PlaySound(Sound.AMBIENT_WEATHER_THUNDER), 2);
                
            }
            
            if(Gamer.size() == 0 && canRun) {
                
                end();
                canRun = false;
                
            }
                
            return;
        }
        if(canRun)
            end();
        
    }
    @SuppressWarnings("deprecation")
    public synchronized static void end() {
        
        Arena.getFloor().placedBlockLocations.clear();
        Arena.getFloor().usedIntegers.clear();
        Arena.getFloor().removedBlocks.clear();
        GameSchedule.runden = 0;
        GameSchedule.time = 6;
        GameSchedule.i = 5;
        
        Arena.getFloor().reset();
        Arena.getBoard().reset();
        
        Main.plugin.getServer().getScheduler().cancelTask(Main.plugin);
        
        if(Gamer.get(0).isOnline()) {
            
            Gamer.get(0).sendTitle(TextFormat.GREEN + "Du hast gewonnen :D");
            sendMessageToAll(TextFormat.colorize("&3" + Gamer.get(0).getName() + " &dhat das Spiel gewonnen!"));
            Gamer.get(0).setScale(20);
            
            Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> playSoundFor(Gamer.get(0), Sound.FIREWORK_LAUNCH), 2);
            
        }
            
        
        Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
            
            canRun = true;
            Arena.setIsGameStarted(false);
            isEnoughPlayers = false;
            teleport(Arena.getLobbyWorld().getSpawnLocation());
                
            if(!Gamer.isEmpty()) {
                
                if(Waiting.size() >= max) {
                    
                    sendPopup(Gamer.get(0), TextFormat.DARK_RED + "Leider musst du Zusehen, da die Warteliste voll ist :(");
                    return;
                    
                }
                if(Gamer.get(0).isOnline()) {
                    
                    Waiting.add(Gamer.get(0));
                    sendPopup(Gamer.get(0), TextFormat.DARK_GREEN + "Du wurdes automatisch eingetragen :D");
                    
                }
                
                if(Waiting.size() >= min) {
                    
                    if(!isEnoughPlayers()) {
                        
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
                        return;
                        
                    }
                    
                    Waiting.add(watcher);
                    sendPopup(watcher, TextFormat.DARK_GREEN + "Du wurdes automatisch eingetragen :D");
                    
                    if(Waiting.size() >= min) {
                        
                        if(!isEnoughPlayers()) {
                            
                            sendPopupToAll(TextFormat.DARK_GREEN + "Es sind genug Spieler in der Warteliste eingetragen, das Spiel beginnt gleich...");
                            PlaySound(Sound.NOTE_HAT);
                            GameSchedule.RoundStart();
                            GamePlayers.setEnoughPlayers(true);
                            
                        }
                        
                    }
                    
                });
                
            }
            Gamer.get(0).setScale(1);
            Gamer.clear();
            for(Player watcher : Watcher) {
                
                watcher.setAllowFlight(false);
                
            }
            Watcher.clear();
                
        }, 200);
        
    }
    
}
