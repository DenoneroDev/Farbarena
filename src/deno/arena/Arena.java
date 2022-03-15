package deno.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.utils.Config;
import deno.Main;

public class Arena {

    private static final String GameWorldName = "farbarena";
    private static final File file = new File(Main.plugin.getDataFolder(), "Farbarena.yml");
    private static final Config config = new Config(file, Config.YAML);
    private static final Block DefaultBlock = Block.get(Block.WOOL);
    private static final Floor floor = new Floor();
    private static final Board board = new Board();
    private static boolean isGameStarted = false;
    private static int BeforeGameStartsTime = 60;
    
    public static Level getLobbyWorld() {

        String lobbyWorldName = "minigames";
        Server.getInstance().loadLevel(lobbyWorldName);
        return Server.getInstance().getLevelByName(lobbyWorldName);
        
    }
    public static Level getGameWorld() {
        
        Server.getInstance().loadLevel(GameWorldName);
        return Server.getInstance().getLevelByName(GameWorldName);
        
    }
    public static String getGameWorldName() {
        
        return GameWorldName;
        
    }
    public static void createGameWorld() {
        
        long seed = new Random().nextLong();
        Class<? extends Generator> generator = Generator.getGenerator(5);
        Server.getInstance().generateLevel(getGameWorldName(), seed, generator);
        getGameWorld().setSpawnLocation(new Location(0.5, 7, 0.5));
        getGameWorld().setBlock(Server.getInstance().getLevelByName(getGameWorldName()).getSpawnLocation().add(0, -7), Block.get(Block.STONE));
        getGameWorld().gameRules.setGameRule(GameRule.FALL_DAMAGE, false);
        getGameWorld().gameRules.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        getGameWorld().gameRules.setGameRule(GameRule.NATURAL_REGENERATION, false);
        getGameWorld().gameRules.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        getGameWorld().gameRules.setGameRule(GameRule.PVP, false);
        getGameWorld().gameRules.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        getGameWorld().gameRules.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        
    }
    public static boolean isGameWorldCreated() {
        
        return Server.getInstance().isLevelGenerated(GameWorldName);
        
    }
    public static Config getConfig() {
        
        return config;
        
    }
    public static Floor getFloor() {
        
        return floor;
        
    }
    public static Board getBoard() {
        
        return board;
        
    }
    public static boolean isFloorSaved() {
        
        return getFloor().isSaved();
        
    }
    public static boolean isBoardSaved() {
        
        return getBoard().isSaved();
        
    }
    public static Block getDefaultBlock() {
        
        return DefaultBlock;
        
    }
    public static void setIsGameStarted(boolean b) {
        
        isGameStarted = b;
        
    }
    public static List<Location> DecodeToLocation(List<String> s) {
        
        List<Location> list = new ArrayList<>();
        
        s.forEach(str -> {
            
            String[] newS = str.split(", ");
            Location loc = new Location(Double.parseDouble(newS[0].replace("x=", "")), Double.parseDouble(newS[1].replace("y=", "")), Double.parseDouble(newS[2].replace("z=", "")));

            list.add(loc);
            
        });
        
        return list;
    }
    public static boolean isGameStarted() {
        
        return isGameStarted;
        
    }
    public static int getBeforeGameStartsTime() {
        
        return BeforeGameStartsTime;
        
    }
    public static void setBeforeGameStartsTime(int t) {
        
        BeforeGameStartsTime = t;
        
    }
    public static Location getWatcherSpawn() {
        
        String[] s = getConfig().getString("watcherspawn").split(", ");

        return new Location(Double.parseDouble(s[1].replace("x=", "")), Double.parseDouble(s[2].replace("y=", "")), Double.parseDouble(s[3].replace("z=", "")), Server.getInstance().getLevelByName(s[0].replace("level=", "")));
        
    }
    public static Integer[] getXZ(Location loc) {
        
        Integer[] xz = new Integer[2];
        
        xz[0] = (int) loc.getX();
        xz[1] = (int) loc.getZ();
        
        return xz;
        
    }
    public static String getColorCodeByBlockColor(int i) {
        
        String color = "f,8,5,b,e,a,d,8,7,3,8,9,8,2,4,0";
        return color.split(",")[i];
        
    }
    public static String getColorNameByInt(int i) {
        
        String color = "Weiß,Orange,Magenta,Hellblau,Gelb,Hellgrün,Rosa,Dunkelgrau,Hellgrau,Türkis,Lila,Blau,Braun,Dunkelgrün,Rot,Schwarz";
        return color.split(",")[i];
        
    }
    public static String Location(double x, double y, double z) {
        
        return "x=" + x + ", y=" + y + ", z=" + z;
        
    }
    public static String Location(double x, double y, double z, Level l) {
        
        return "level=" + l.getName() + ", x=" + x + ", y=" + y + ", z=" + z;
        
    }
    public static String rotate(String s) {
        
        StringBuilder ReturnValue = new StringBuilder(" ");
        
        for(int i = s.length(); i != 0; i--)
            ReturnValue.append(s.charAt(i - 1));
            
        return ReturnValue.toString();
        
    }
    public static boolean isWatcherSpawnSaved() {
        
        return getConfig().exists("watcherspawn");
        
    }
}
