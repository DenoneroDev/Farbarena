package deno;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import deno.arena.Arena;
import deno.game.GameListener;

public class Main extends PluginBase {
    
    public static Main plugin;

    public void onLoad() {
        
        plugin = this;
        
        this.getLogger().info(TextFormat.colorize("&d" + this.getDescription().getName() + " Version &4" + 
                this.getDescription().getVersion() + " &aladen... \t &dCopyrights by &c" + String.join(", ", this.getDescription().getAuthors())));
        
    }
    public void onEnable() {
        
        if(Arena.isFloorSaved()) Arena.getFloor().load();
        if(Arena.isBoardSaved()) Arena.getBoard().load();
        
        loadCommandParams();
        
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getPluginManager().registerEvents(new GameListener(), this);
        
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        switch(cmd.getName()) {
        
        case "farbarena":
            
            return deno.Command.Farbarena(sender, args);
        
        }
        return false;
        
    }
    public void loadCommandParams() {
        
        Command cmd2 = this.getServer().getCommandMap().getCommand("farbarena");
        
        cmd2.getCommandParameters().clear();
        cmd2.getCommandParameters().put("default", new CommandParameter[]{
                
                CommandParameter.newEnum("subject", new CommandEnum("watcherspawn|wspawn", "watcherspawn", "wspawn")),
                CommandParameter.newEnum("action", new CommandEnum("set|del", "set", "del")),

        });
        cmd2.getCommandParameters().put("Floor|Board", new CommandParameter[]{
        
                CommandParameter.newEnum("subject", new CommandEnum("floor|board", "floor", "board")),
                CommandParameter.newEnum("action", new CommandEnum("create|delete", "create", "c", "delete", "del")),

        });
        cmd2.getCommandParameters().put("World", new CommandParameter[]{
                
                CommandParameter.newEnum("subject", new CommandEnum("world", "world", "level", "lvl")),
                CommandParameter.newEnum("action", new CommandEnum("create", "create", "c")),

        });
        
    }
    
}
/*#####################################################################
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!               !!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!  DENONERODEV  !!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!               !!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
#####################################################################*/