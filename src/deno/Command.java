package deno;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.TextFormat;
import deno.arena.Arena;
import deno.game.GamePlayers;

public class Command {
    
    public static boolean Farbarena(CommandSender sender, String[] args) {

        if (sender instanceof ConsoleCommandSender) {

            sender.sendMessage(TextFormat.RED + "Du kannst diesen Command nicht von der Konsole ausführen!");
            return true;

        }

        Player player = (Player) sender;
        
        if(!player.hasPermission("deno.farbarena.join")) {
            
            player.sendMessage(TextFormat.RED + "Du hast keine Rechte um Farbarena beizutreten!");
            return true;
            
        }

        if (args.length == 0) {

            if (!player.getLevel().equals(Arena.getLobbyWorld())) {

                player.sendMessage(TextFormat.RED + "Du bist nicht in der Lobby!");
                return true;

            }
            if (Arena.getGameWorld() == null) {

                player.sendMessage(TextFormat.colorize("&cDu hast die Farbarena Welt nicht erstellt, benutze &4/farb world create&c, um die Welt zu erstellen!"));
                return true;

            }
            if (!Arena.isFloorSaved()) {

                player.sendMessage(TextFormat.colorize("&cDu hast den Farbarena Boden nicht erstellt, benutze &4/farb floor create&c, um den Boden zu erstellen!"));
                return true;

            }
            if (!Arena.isBoardSaved()) {

                player.sendMessage(TextFormat.colorize("&cDu hast die Farbarena Tafel nicht erstellt, benutze &4/farb board create&c, um die Tafel zu erstellen!"));
                return true;

            }
            
            FormWindowSimple form = new FormWindowSimple(TextFormat.colorize("&2Gamemenü &4F&ca&4r&cb&4a&cr&4e&cn&4a"), "");
            
            if(Arena.isGameStarted()) {
                
                form.addButton(new ElementButton(TextFormat.AQUA + "Schick mich ins Spiel, damit ich zusehen kann :D"));
                form.setContent(TextFormat.DARK_GREEN + "Leider läuft gerade ein Spiel, du kannst aber zusehen :D");
            
            } else {
                
                if(GamePlayers.getWaiters().size() >= GamePlayers.getMaxPlayers()) {
                    
                    form.setContent(TextFormat.colorize("&aLeider ist die maximale Spieleranzahl von &6" + GamePlayers.getMaxPlayers() + " &aschon erreicht D:\n\n" + "§2Du kannst dich aber als Zuschauer eintragen, oder austragen :D"));
                
                } else {
                
                    form.setContent(TextFormat.colorize("&aHier kannst du dich in der Warteliste eintragen, oder austragen :D\n\n" + "&2Derzeit befinden sich &6" + GamePlayers.getWaiters().size() + " &2Spieler in der Warteliste"));
                }
            
                if(GamePlayers.getWaiters().size() >= GamePlayers.getMaxPlayers() && !(GamePlayers.getWatchers().contains(player))) {

                    form.addButton(new ElementButton(TextFormat.colorize("&3Schreib mich in der Zuschauerliste von &6Farbarena &3ein")));
                
                }
            
                if(GamePlayers.getWatchers().contains(player)) { 
                
                    form.addButton(new ElementButton(TextFormat.colorize("&4Aus der Zuschauerliste von &6Farbarena &4austreten")));
                
                }
            
                if(!(GamePlayers.getWaiters().contains(player)) && GamePlayers.getWaiters().size() < GamePlayers.getMaxPlayers()) {
                
                    form.addButton(new ElementButton(TextFormat.colorize("&3Schreib mich in der Warteliste von &6Farbarena &3ein")));
                
                }
            
                if(GamePlayers.getWaiters().contains(player)) {
                
                    form.addButton(new ElementButton(TextFormat.colorize("&4Aus der Warteliste von &6Farbarena &4austreten")));
                
                }
                
            }
            
            player.showFormWindow(form, 4444);

        }
        
        if(!player.hasPermission("deno.farbarena.master")) {
            
            player.sendMessage(TextFormat.RED + "Du hast keine Rechte um diesen Berreich zu bedienen!");
            return true;
            
        }
        
        if (args.length >= 1) {
            
            Item stick = Item.get(280);
            stick.addEnchantment(Enchantment.get(22));
            stick.setCustomName(TextFormat.GOLD + "Farbarenawerkzeug");
            
            switch (args[0].toLowerCase()) {
            
            case "setwatcherspawn": case "sws": case "watcherspawn": case "ws":
                
                if(!player.getLevel().equals(Arena.getGameWorld())) {
                    
                    player.sendMessage(TextFormat.RED + "Du bist nicht in der Farbarena Welt!");
                    return true;
                    
                }
                
                Arena.getConfig().set("watcherspawn", player.getLocation().toString());
                Arena.getConfig().save();
                
                player.sendMessage(TextFormat.colorize("&bSpawnposition für die Zuschauer erfolgreich bei &2" + (int) player.x + ", " + (int) player.y + ", " + (int) player.z + "&b gesetzt!"));
                
                break;
            
            case "world": case "w": case "level": case "lvl": case "l":

                if (args.length < 2) {
                    
                    player.sendMessage(TextFormat.RED + "Du hast nicht die nötigen Parameter eigegeben!");
                    return true;
                    
                }

                switch (args[1].toLowerCase()) {

                case "create": case "c":

                    if (Arena.isGameWorldCreated()) {

                        player.sendActionBar(TextFormat.colorize("&cDu hast bereits eine Farbarena Welt!"));
                        return true;

                    }
                    Arena.createGameWorld();
                    player.sendMessage(TextFormat.GREEN + "Die Welt wurde erfolgreich erstellt!");

                    break;

                default:
                    
                    player.sendMessage(TextFormat.colorize("&4" + args[1].toLowerCase() + " &cist kein gültiger Parameter in &4" + args[0].toLowerCase() + "&c!"));
                    break;
                }
                
                break;
                
            case "floor": case "f":
                
                if (args.length < 2) {
                    
                    player.sendMessage(TextFormat.RED + "Du hast nicht die nötigen Parameter eigegeben!");
                    return true;
                    
                }
                
                switch(args[1].toLowerCase()) {
                
                case "create": case "c":
                    
                    if(player.getInventory().contains(stick)) {
                        
                        player.sendMessage(TextFormat.YELLOW + "Du hast bereits ein Werkzeug in deinem Inventar.");
                        return true;
                        
                    }
                    
                    if(Arena.isFloorSaved()) {
                        
                        Arena.getFloor().delete();
                        player.sendMessage(TextFormat.YELLOW + "(Der Boden wurde zurückgesetzt)");
                        
                    }
                    
                    player.getInventory().setItemInHand(stick);
                    player.sendMessage(TextFormat.GREEN + "Du hast das Bodenwerkzeug erhalten!");
                    
                    break;
                case "delete": case "del": case "d":
                    
                    if(!Arena.isFloorSaved()) {
                        
                        player.sendMessage(TextFormat.colorize("&cDu hast noch keinen Boden erstellt, benutze &4/farb floor create&c, um einen Boden zu erstellen!"));
                        return true;
                    }

                    Arena.getFloor().delete();
                    
                    break;
                default:
                    
                    player.sendMessage(TextFormat.colorize("&4" + args[1].toLowerCase() + " &cist kein gültiger Parameter in &4" + args[0].toLowerCase() + "&c!"));
                    break;
                }
                break;
                
            case "board": case "b": 
                
                if(args.length < 2) {
                    
                    player.sendMessage(TextFormat.RED + "Du hast nicht die nötigen Parameter eigegeben!");
                    return true;
                    
                }
                
                switch(args[1].toLowerCase()) {
                
                case "create": case "c":
                    
                    if(player.getInventory().contains(stick)) {
                        
                        player.sendMessage(TextFormat.YELLOW + "Du hast bereits ein Werkzeug in deinem Inventar.");
                        return true;
                        
                    }
                    
                    if(!Arena.isFloorSaved()) {
                        
                        player.sendMessage(TextFormat.colorize("&cDu hast noch keinen Boden erstellt, benutze &4/farb floor create&c, um einen Boden zu erstellen!"));
                        return false;
                        
                    }
                    
                    if(Arena.isBoardSaved()) {
                        
                        Arena.getBoard().delete();
                        player.sendMessage(TextFormat.YELLOW + "(Die Tafel wurde zurückgesetzt)");
                        
                    }
                    
                    player.getInventory().setItemInHand(stick);
                    player.sendMessage(TextFormat.GREEN + "Du hast das Tafelwerkzeug erhalten!");
                    
                    break;
                case "delete": case "del": case "d":
                    
                    if(!Arena.isBoardSaved()) {
                        
                        player.sendMessage(TextFormat.colorize("&cDu hast noch keine Tafel erstellt, benutze &4/farb board create&c, um eine Tafel zu erstellen!"));
                        return true;
                    }

                    Arena.getBoard().delete();
                    
                    break;
                    
                default: 
                    
                    player.sendMessage(TextFormat.colorize("&4" + args[1].toLowerCase() + " &cist kein gültiger Parameter in &4" + args[0].toLowerCase() + "&c!"));
                    break;
                    
                }
                
                break;
                
            default:
                
                player.sendMessage(TextFormat.colorize("&4" + args[0].toLowerCase() + " &cist kein gültiger Parameter!"));
                break;

            }

        }
        return true;

    }

}