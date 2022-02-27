package deno.arena;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.nukkit.block.Block;
import cn.nukkit.level.Location;

public class Board {
    
    private String ConfigPath = "BoardAngles";
    private CopyOnWriteArrayList<Location> Blocks = new CopyOnWriteArrayList<>();
    private boolean isMarked = false;
    private Location FirstAngle;
    private Location SecondAngle;
    
    public boolean isSaved() {
        
        return Arena.getConfig().exists(ConfigPath);
        
    }
    public boolean isMarked() {
        
        return isMarked;
    
    }
    public Location getFirstAngle() {
        
        return FirstAngle;
        
    }
    public void setFirstAngle(Location l) {
        
        isMarked = true;
        FirstAngle = l;
        
    }
    public Location getSecondAngle() {
        
        return SecondAngle;
        
    }
    public void setSecondAngle(Location l) {
        
        SecondAngle = l;
        
    }
    public void create() {
        
        CompletableFuture.runAsync(() -> {
            
            int FirstX = (int) ((FirstAngle.getX() < SecondAngle.getX()) ? FirstAngle.getX() : SecondAngle.getX());
            int FirstY = (int) ((FirstAngle.getY() < SecondAngle.getY()) ? FirstAngle.getY() : SecondAngle.getY());
            int FirstZ = (int) ((FirstAngle.getZ() < SecondAngle.getZ()) ? FirstAngle.getZ() : SecondAngle.getZ());
            int SecondX = (int) ((FirstAngle.getX() > SecondAngle.getX()) ? FirstAngle.getX() : SecondAngle.getX());
            int SecondY = (int) ((FirstAngle.getY() > SecondAngle.getY()) ? FirstAngle.getY() : SecondAngle.getY());
            int SecondZ = (int) ((FirstAngle.getZ() > SecondAngle.getZ()) ? FirstAngle.getZ() : SecondAngle.getZ());
            
            String newFirstLocation = new Location(FirstX, FirstY, FirstZ, Arena.getGameWorld()).toString();
            String newSecondLocation = new Location(SecondX, SecondY, SecondZ, Arena.getGameWorld()).toString();
            
            Arena.getConfig().set(ConfigPath, Arrays.asList(newFirstLocation, newSecondLocation));
            Arena.getConfig().save();
            load();
            
        });
        
        
    }
    public void delete() {
        
        CompletableFuture.runAsync(() -> {
            
            for(Location loc : Blocks) {
                    
                Arena.getGameWorld().setBlock(loc, Block.get(Block.AIR));
                    
            }

            isMarked = false;
            Blocks.clear();
            Arena.getConfig().remove(ConfigPath);
            Arena.getConfig().save();
                
        });
        
        
    }
    public void load() {
        
        List<Location> Angles = Arena.DecodeToLocation(Arena.getConfig().getStringList(ConfigPath));
        
        System.out.println(Angles);
        
        Location FirstAngle = Angles.get(0);
        Location SecondAngle = Angles.get(1);
        
        CompletableFuture.runAsync(() -> {
            
            double FirstY = FirstAngle.getY();
            double SecondY = SecondAngle.getY();
            
            while(FirstY <= SecondY) {
                
                double FirstX = FirstAngle.getX();
                double SecondX = SecondAngle.getX();
                
                double FirstZ = FirstAngle.getZ();
                double SecondZ = SecondAngle.getZ();
                
                if(FirstAngle.getX() == SecondAngle.getX()) {
                    
                    while(FirstZ <= SecondZ) {
                        
                        Location l = new Location(FirstX, FirstY, FirstZ);
                        
                        Blocks.add(l);
                        FirstZ++;
                        
                    }
                    
                } else {
                    
                    while(FirstX <= SecondX) {
                        
                        Location l = new Location(FirstX, FirstY, FirstZ);
                        
                        Blocks.add(l);
                        FirstX++;
                        
                    }
                    
                }
                
                FirstY++;
                
            }
            
        });
        
    }
    public void setColor(int c) {
        
        CompletableFuture.runAsync(() -> {
            
            for(Location loc : Blocks) {
                    
                Arena.getGameWorld().setBlock(loc, Block.get(Block.WOOL, c));
                    
            }
                
        });
        
    }
    
    public void reset() {
        
        CompletableFuture.runAsync(() -> {
            
            for(Location loc : Blocks) {
                    
                Arena.getGameWorld().setBlock(loc, Arena.getDefaultBlock());
                    
            }
                
        });
        
    }
}
