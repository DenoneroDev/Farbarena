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

        Player p = (Player) sender;
        
        if(!p.hasPermission("deno.farbarena.join") && !p.hasPermission("deno.farbarena.master")) {
            
            p.sendMessage(TextFormat.RED + "Du hast keine Rechte um Farbarena beizutreten!");
            return true;
            
        }

        if (args.length == 0) {

            if (!p.getLevel().equals(Arena.getLobbyWorld())) {

                p.sendMessage(TextFormat.RED + "Du bist nicht in der Lobby!");
                return true;

            }
            if (Arena.getGameWorld() == null) {

                p.sendMessage(TextFormat.colorize("&cDu hast die Farbarena Welt nicht erstellt, benutze &4/farb world create&c, um die Welt zu erstellen!"));
                return true;

            }
            if (!Arena.isFloorSaved()) {

                p.sendMessage(TextFormat.colorize("&cDu hast den Farbarena Boden nicht erstellt, benutze &4/farb floor create&c, um den Boden zu erstellen!"));
                return true;

            }
            if (!Arena.isBoardSaved()) {

                p.sendMessage(TextFormat.colorize("&cDu hast die Farbarena Tafel nicht erstellt, benutze &4/farb board create&c, um die Tafel zu erstellen!"));
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
            
                if(GamePlayers.getWaiters().size() >= GamePlayers.getMaxPlayers() && !(GamePlayers.getWatchers().contains(p))) {

                    form.addButton(new ElementButton(TextFormat.colorize("&3Schreib mich in der Zuschauerliste von &6Farbarena &3ein")));
                
                }
            
                if(GamePlayers.getWatchers().contains(p)) { 
                
                    form.addButton(new ElementButton(TextFormat.colorize("&4Aus der Zuschauerliste von &6Farbarena &4austreten")));
                
                }
            
                if(!(GamePlayers.getWaiters().contains(p)) && GamePlayers.getWaiters().size() < GamePlayers.getMaxPlayers()) {
                
                    form.addButton(new ElementButton(TextFormat.colorize("&3Schreib mich in der Warteliste von &6Farbarena &3ein")));
                
                }
            
                if(GamePlayers.getWaiters().contains(p)) {
                
                    form.addButton(new ElementButton(TextFormat.colorize("&4Aus der Warteliste von &6Farbarena &4austreten")));
                
                }
                
            }
            
            p.showFormWindow(form, 4444);

        }
        
        if(!p.hasPermission("deno.farbarena.master")) {
            
            p.sendMessage(TextFormat.RED + "Du hast keine Rechte um diesen Berreich zu bedienen!");
            return true;
            
        }
        
        if (args.length >= 1) {
            
            Item stick = Item.get(280);
            stick.addEnchantment(Enchantment.get(22));
            stick.setCustomName(TextFormat.GOLD + "Farbarenawerkzeug");
            
            switch (args[0].toLowerCase()) {
            
            case "watcherspawn": case "wspawn":
                
                if(args.length < 2) {
                    
                    Arena.getConfig().set("watcherspawn", Arena.Location(p.getX(), p.getY(), p.getZ(), p.getLevel()));
                    Arena.getConfig().save();
                    p.sendMessage(TextFormat.colorize("&bSpawnposition für die Zuschauer erfolgreich bei &2" + (int) p.x + ", " + (int) p.y + ", " + (int) p.z + "&b gesetzt!"));
                    
                }
                if(args.length >= 2) {

                    switch(args[1].toLowerCase()) {
                    
                    case "set":
                        
                        Arena.getConfig().set("watcherspawn", Arena.Location(p.getX(), p.getY(), p.getZ(), p.getLevel()));
                        Arena.getConfig().save();
                        p.sendMessage(TextFormat.colorize("&bSpawnposition für die Zuschauer erfolgreich bei &2" + (int) p.x + ", " + (int) p.y + ", " + (int) p.z + "&b gesetzt!"));
                        
                        break;
                        
                    case "delete": case "del":
                        
                        Arena.getConfig().remove("watcherspawn");
                        Arena.getConfig().save();
                        p.sendMessage(TextFormat.DARK_RED + "Zuschauerspawn wurde gelöscht!");
                        
                        break;
                    }
                    
                }
                
                break;
            
            case "world": case "level": case "lvl":

                if (args.length < 2) {
                    
                    p.sendMessage(TextFormat.RED + "Du hast nicht die nötigen Parameter eigegeben!");
                    return true;
                    
                }

                switch (args[1].toLowerCase()) {

                case "create": case "c":

                    if (Arena.isGameWorldCreated()) {

                        p.sendActionBar(TextFormat.colorize("&cDu hast bereits eine Farbarena Welt!"));
                        return true;

                    }
                    Arena.createGameWorld();
                    p.sendMessage(TextFormat.GREEN + "Die Welt wurde erfolgreich erstellt!");

                    break;

                default:
                    
                    p.sendMessage(TextFormat.colorize("&4" + args[1].toLowerCase() + " &cist kein gültiger Parameter in &4" + args[0].toLowerCase() + "&c!"));
                    break;
                }
                
                break;
                
            case "floor":
                
                if (args.length < 2) {
                    
                    p.sendMessage(TextFormat.RED + "Du hast nicht die nötigen Parameter eigegeben!");
                    return true;
                    
                }
                
                switch(args[1].toLowerCase()) {
                
                case "create": case "c":
                    
                    if(p.getInventory().contains(stick)) {
                        
                        p.sendMessage(TextFormat.YELLOW + "Du hast bereits ein Werkzeug in deinem Inventar.");
                        return true;
                        
                    }
                    
                    if(Arena.isFloorSaved()) {
                        
                        Arena.getFloor().delete();
                        p.sendMessage(TextFormat.YELLOW + "(Der Boden wurde zurückgesetzt)");
                        
                    }
                    
                    p.getInventory().setItemInHand(stick);
                    p.sendMessage(TextFormat.GREEN + "Du hast das Bodenwerkzeug erhalten!");
                    
                    break;
                case "delete": case "del":
                    
                    if(!Arena.isFloorSaved()) {
                        
                        p.sendMessage(TextFormat.colorize("&cDu hast noch keinen Boden erstellt, benutze &4/farb floor create&c, um einen Boden zu erstellen!"));
                        return true;
                    }

                    Arena.getFloor().delete();
                    
                    break;
                default:
                    
                    p.sendMessage(TextFormat.colorize("&4" + args[1].toLowerCase() + " &cist kein gültiger Parameter in &4" + args[0].toLowerCase() + "&c!"));
                    break;
                }
                break;
                
            case "board":
                
                if(args.length < 2) {
                    
                    p.sendMessage(TextFormat.RED + "Du hast nicht die nötigen Parameter eigegeben!");
                    return true;
                    
                }
                
                switch(args[1].toLowerCase()) {
                
                case "create": case "c":
                    
                    if(p.getInventory().contains(stick)) {
                        
                        p.sendMessage(TextFormat.YELLOW + "Du hast bereits ein Werkzeug in deinem Inventar.");
                        return true;
                        
                    }
                    
                    if(!Arena.isFloorSaved()) {
                        
                        p.sendMessage(TextFormat.colorize("&cDu hast noch keinen Boden erstellt, benutze &4/farb floor create&c, um einen Boden zu erstellen!"));
                        return false;
                        
                    }
                    
                    if(Arena.isBoardSaved()) {
                        
                        Arena.getBoard().delete();
                        p.sendMessage(TextFormat.YELLOW + "(Die Tafel wurde zurückgesetzt)");
                        
                    }
                    
                    p.getInventory().setItemInHand(stick);
                    p.sendMessage(TextFormat.GREEN + "Du hast das Tafelwerkzeug erhalten!");
                    
                    break;
                case "delete": case "del":
                    
                    if(!Arena.isBoardSaved()) {
                        
                        p.sendMessage(TextFormat.colorize("&cDu hast noch keine Tafel erstellt, benutze &4/farb board create&c, um eine Tafel zu erstellen!"));
                        return true;
                    }

                    Arena.getBoard().delete();
                    
                    break;
                    
                default: 
                    
                    p.sendMessage(TextFormat.colorize("&4" + args[1].toLowerCase() + " &cist kein gültiger Parameter in &4" + args[0].toLowerCase() + "&c!"));
                    break;
                    
                }
                
                break;
                
            default:
                
                p.sendMessage(TextFormat.colorize("&4" + args[0].toLowerCase() + " &cist kein gültiger Parameter!"));
                break;

            }

        }
        return true;

    }

}