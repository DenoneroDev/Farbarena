package deno.arena;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.nukkit.block.Block;
import cn.nukkit.level.Location;

public class Board {
    
    private final String ConfigPath = "BoardAngles";
    private final CopyOnWriteArrayList<Location> Blocks = new CopyOnWriteArrayList<>();
    private boolean isMarked = false;
    private Location FirstAngle;
    private Location SecondAngle;
    
    public boolean isSaved() {
        
        return Arena.getConfig().exists(ConfigPath);
        
    }
    public boolean isMarked() {
        
        return isMarked;
    
    }

    public void setFirstAngle(Location l) {
        
        isMarked = true;
        FirstAngle = l;
        
    }

    public void setSecondAngle(Location l) {
        
        SecondAngle = l;
        
    }
    public void create() {
        
        CompletableFuture.runAsync(() -> {
            
            int FirstX = (int) (Math.min(FirstAngle.getX(), SecondAngle.getX()));
            int FirstY = (int) (Math.min(FirstAngle.getY(), SecondAngle.getY()));
            int FirstZ = (int) (Math.min(FirstAngle.getZ(), SecondAngle.getZ()));
            int SecondX = (int) (Math.max(FirstAngle.getX(), SecondAngle.getX()));
            int SecondY = (int) (Math.max(FirstAngle.getY(), SecondAngle.getY()));
            int SecondZ = (int) (Math.max(FirstAngle.getZ(), SecondAngle.getZ()));
            
            String newFirstLocation = Arena.Location(FirstX, FirstY, FirstZ);
            String newSecondLocation = Arena.Location(SecondX, SecondY, SecondZ);
            
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
            
            for(Location l : Blocks)
                Arena.getGameWorld().setBlock(l, Arena.getDefaultBlock());
            
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
