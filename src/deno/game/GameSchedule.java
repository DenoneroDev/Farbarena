package deno.game;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.TextFormat;
import deno.Main;
import deno.arena.Arena;

public class GameSchedule {
    
    public static TaskHandler task;
    public static TaskHandler nowRunnedTask;
    public static List<TaskHandler> allTasks = new ArrayList<>();
    public static int randomInt;
    public static int i = 5;
    
    public static int time = 6;
    public static int newtime = time;
    
    public static int runden = 0;
    
    public static synchronized void RoundStart() {
        
        task = Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
            
            GamePlayers.getGamers().addAll(GamePlayers.getWaiters());
            GamePlayers.getWaiters().clear();
            GamePlayers.clearInventorys();
            GamePlayers.teleport(Arena.getGameWorld().getSpawnLocation());
            WaitBeforeGameStarts();
            
        }, Arena.getBeforeGameStartsTime() * 20);
        
    }
    public static void WaitBeforeGameStarts() {
        
        task.cancel();
        
        allTasks.add((nowRunnedTask = Server.getInstance().getScheduler().scheduleRepeatingTask(Main.plugin, ()-> {
            
                
            GamePlayers.sendPopupToAll(TextFormat.GOLD + "Spiel beginnt in " + i + " Sekunden");
            
            i--;
            
        }, 20)));
        
        Arena.getFloor().mask();
        
        allTasks.add(Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
            
            nowRunnedTask.cancel();
            
            randomInt = Arena.getFloor().usedIntegers.get(Integer.valueOf((int) (Math.random() * ((Arena.getFloor().usedIntegers.size() - 0)))));
            
            Arena.getBoard().setColor(randomInt);
            
            GamePlayers.showColor(randomInt);
            
            JustPickOneColor();
                
        }, 5 * 20));
    }
    
    public static void JustPickOneColor() {
        
        newtime = time;

        allTasks.add(nowRunnedTask = Server.getInstance().getScheduler().scheduleRepeatingTask(Main.plugin, ()-> {
            
                
            GamePlayers.PlaySound(Sound.NOTE_PLING);
            
            GamePlayers.sendCountPopupToAll(TextFormat.colorize("&" + Arena.getColorCodeByBlockColor(randomInt) + newtime));
            GamePlayers.sendPopupToAll(TextFormat.colorize("&" + Arena.getColorCodeByBlockColor(randomInt) + "« « « " + Arena.getColorNameByInt(randomInt) + " » » »"));
            
            newtime -= 1;
            
        }, 20));
        
        allTasks.add(Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, () -> {
            
            GamePlayers.sendCountPopupToAll("");
            GamePlayers.sendPopupToAll(TextFormat.DARK_RED + "" + TextFormat.BOLD + "✘ Stop ✘");
            
            nowRunnedTask.cancel();
            
            GamePlayers.PlaySound(Sound.NOTE_SNARE);
            GamePlayers.clearInventorys();
            Arena.getFloor().pickColor(randomInt);
            MakeFloorBack();
            
            runden++;
            
            if(runden == 6 && time != 3.0) {
                
                runden = 0;
                time -= 1;
                
            }
            
        }, time*20));
        
    }
    
    public static void MakeFloorBack() {
        
        allTasks.add(Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
            
            GamePlayers.sendCountPopupToAll(TextFormat.colorize("&bNoch &3" + GamePlayers.getGamers().size() + " &bSpieler"));
            GamePlayers.sendPopupToAll(TextFormat.AQUA + "Warte...");
            
        }, 2 * 20));
        
        allTasks.add(Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
            GamePlayers.sendCountPopupToAll("");
            GamePlayers.sendPopupToAll("");
            Arena.getFloor().back();
            GamePlayers.PlaySound(Sound.NOTE_BELL);
            
            Waiting();
            
        }, 3 * 20));
        
    }
    public static void Waiting() {
        
        allTasks.add(Server.getInstance().getScheduler().scheduleDelayedTask(Main.plugin, ()-> {
                
            randomInt = Arena.getFloor().usedIntegers.get(Integer.valueOf((int) (Math.random() * ((Arena.getFloor().usedIntegers.size() - 0)))));
            Arena.getBoard().setColor(randomInt);
            GamePlayers.showColor(randomInt);
            
            JustPickOneColor();
            
        }, 2 * 20));
        
    }
    
}
