package deno.game;

import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.TextFormat;
import deno.Main;
import deno.arena.Arena;

public class GameSchedule {
    
    public static TaskHandler nowRunnedTask;
    public static TaskHandler RoundTask;
    public static int randomInt;
    public static int i = 5;
    
    public static int time = 6;
    public static int newtime = time;
    
    public static int runden = 0;
    
    public static synchronized void RoundStart() {
        
        RoundTask = Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
            
            GamePlayers.clearInventorys();
            Arena.setIsGameStarted(true);

            Arena.getFloor().mask();
            GamePlayers.teleport(Arena.getGameWorld().getSpawnLocation());
            GamePlayers.getGamers().addAll(GamePlayers.getWaiters());
            GamePlayers.getWaiters().clear();

            Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> GamePlayers.PlaySound(Sound.RANDOM_LEVELUP), 2);
            
            nowRunnedTask = Server.getInstance().getScheduler().scheduleRepeatingTask(Main.plugin, ()-> {
                
                GamePlayers.sendPopupToAll(TextFormat.GOLD + "Spiel beginnt in " + i + " Sekunden");
                i--;
                
            }, 20);
            
            Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
                
                nowRunnedTask.cancel();
                GameSystem();
                
            }, 100);

        }, Arena.getBeforeGameStartsTime() * 20);
        
    }
    
    public static void GameSystem() {
        
        if(Arena.isGameStarted()) {
            
            randomInt = Arena.getFloor().usedIntegers.get((int) (Math.random() * ((Arena.getFloor().usedIntegers.size()))));
            newtime = time;
            
            Arena.getBoard().setColor(randomInt);
            GamePlayers.showColor(randomInt);
            
            
            nowRunnedTask = Server.getInstance().getScheduler().scheduleRepeatingTask(Main.plugin, ()-> {
                
                String VisualTimer = "▇▆▅▄▃▂";
                StringBuilder VT = new StringBuilder();
                
                for(int i = newtime; i != 0; i--)
                    VT.append(VisualTimer.charAt(i - 1));
                
                if(newtime == 6) GamePlayers.PlaySound(Sound.NOTE_DIDGERIDOO, 3);
                if(newtime == 5) GamePlayers.PlaySound(Sound.NOTE_BASS, 3);
                if(newtime == 4) GamePlayers.PlaySound(Sound.NOTE_DIDGERIDOO, 2);
                if(newtime == 3) GamePlayers.PlaySound(Sound.NOTE_BASS, 2);
                if(newtime == 2) GamePlayers.PlaySound(Sound.NOTE_DIDGERIDOO, 1);
                if(newtime == 1) GamePlayers.PlaySound(Sound.NOTE_BASS, 1);
                
                int textcolor = (time > 3) ? randomInt : Arena.getFloor().usedIntegers.get((int) (Math.random() * ((Arena.getFloor().usedIntegers.size()))));
                
                GamePlayers.sendPlayerCountPopupToAll(TextFormat.colorize("&f" + GamePlayers.getGamers().size() + "&7/&8" + GamePlayers.getMaxPlayers()));
                GamePlayers.sendPopupToAll(TextFormat.colorize("&" + Arena.getColorCodeByBlockColor(textcolor) + VT + " " + Arena.getColorNameByInt(randomInt) + Arena.rotate(VT.toString())));
                
                newtime--;
                
            }, 20);
            
            Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, () -> {
                
                GamePlayers.sendPlayerCountPopupToAll(TextFormat.colorize("&f" + GamePlayers.getGamers().size() + "&7/&8" + GamePlayers.getMaxPlayers()));
                GamePlayers.sendPopupToAll(TextFormat.DARK_RED + "" + TextFormat.BOLD + "✘ Stop ✘");
                
                nowRunnedTask.cancel();
                
                GamePlayers.PlaySound(Sound.NOTE_SNARE);
                GamePlayers.clearInventorys();
                Arena.getFloor().pickColor(randomInt);
                
                Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> GamePlayers.sendPopupToAll(TextFormat.DARK_RED + "" + TextFormat.BOLD + "✘ Stop ✘"), 20);
                
                Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
                    
                    GamePlayers.sendPlayerCountPopupToAll(TextFormat.colorize("&f" + GamePlayers.getGamers().size() + "&7/&8" + GamePlayers.getMaxPlayers()));
                    GamePlayers.sendPopupToAll(TextFormat.AQUA + "Warte...");
                    
                }, 2 * 20);
                
                Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
                    
                    GamePlayers.sendPopupToAll(TextFormat.AQUA + "Warte...");
                    GamePlayers.sendPlayerCountPopupToAll(TextFormat.colorize("&f" + GamePlayers.getGamers().size() + "&7/&8" + GamePlayers.getMaxPlayers()));
                    Arena.getFloor().back();
                    GamePlayers.PlaySound(Sound.NOTE_BELL);
                    
                    Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> GamePlayers.sendPopupToAll(TextFormat.AQUA + "Warte..."), 20);
                    
                    Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
                        
                        randomInt = Arena.getFloor().usedIntegers.get((int) (Math.random() * ((Arena.getFloor().usedIntegers.size()))));
                        
                        GameSystem();
                        
                    }, 2 * 20);
                    
                }, 3 * 20);
                
                runden++;
                
                if(runden == 6 && time != 2) {
                    
                    runden = 0;
                    time--;
                    
                }
                
            }, time*20);
            
        }
                
    }
    
    
}
