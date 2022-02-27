package deno.arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cn.nukkit.block.Block;
import cn.nukkit.level.Location;

public class Floor {
    
    private String ConfigPath = "FloorAngles";
    private List<Location> Blocks = new ArrayList<>();
    private boolean isMarked = false;
    private Location FirstAngle = null;
    private Location SecondAngle = null;
    private int MaskSize = 3;
    public List<Integer> usedIntegers = new ArrayList<>();
    public List<Location> placedBlockLocations = new ArrayList<>();
    public List<Block> removedBlocks = new ArrayList<>();

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
            
            double y = FirstAngle.getY();
            double FirstX = (FirstAngle.getX() < SecondAngle.getX()) ? FirstAngle.getX() : SecondAngle.getX();
            double FirstZ = (FirstAngle.getZ() < SecondAngle.getZ()) ? FirstAngle.getZ() : SecondAngle.getZ();
            double SecondX = (FirstAngle.getX() > SecondAngle.getX()) ? FirstAngle.getX() : SecondAngle.getX();
            double SecondZ = (FirstAngle.getZ() > SecondAngle.getZ()) ? FirstAngle.getZ() : SecondAngle.getZ();
            
            String newFirstLocation = Arena.Location(FirstX, y, FirstZ);
            String newSecondLocation = Arena.Location(SecondX, y, SecondZ);
            
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
            
            double FirstX = FirstAngle.getX();
            double SecondX = SecondAngle.getX();
            
            
            while(FirstX <= SecondX) {
                
                double FirstZ = FirstAngle.getZ();
                double SecondZ = SecondAngle.getZ();
                
                while(FirstZ <= SecondZ) {
                    
                    Location l = new Location(FirstX, FirstAngle.getY(), FirstZ);
                    
                    Blocks.add(l);
                    FirstZ++;
                    
                }
                FirstX++;

            }
                
        });
                
    }
    public void mask() {
        
        List<Integer> x = new ArrayList<Integer>();
        List<Integer> z = new ArrayList<Integer>();
        
        for(Location loc : Blocks) {
            
            x.add(Arena.getXZ(loc)[0]);
            z.add(Arena.getXZ(loc)[1]);
            
        }
        
        CompletableFuture.runAsync(() -> {
            
            int placedColorBlockCount = 0;
            int placedColorBlockRowCount = 0;
            
            List<Integer> integers = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
            
            int row = 0;
            
            for(int i = Collections.min(z); i <= Collections.max(z); i++) {
                
                int block = 0;
                
                for(int j = Collections.max(x); j >= Collections.min(x); j--) {
                    
                    Location loc = new Location(Collections.max(x) - block, Blocks.get(0).y, Collections.min(z) + row);
                    
                    if(placedColorBlockRowCount == 0) {
                        
                        if(placedColorBlockCount == 0) {
                            
                            int LastBlockDiv = Arena.getGameWorld().getBlock(loc.add(MaskSize, 0, 0)).getDamage();
                            int LastRowDiv = Arena.getGameWorld().getBlock(loc.add(0, 0, -MaskSize)).getDamage();
                            
                            if(integers.contains(LastBlockDiv) && Blocks.contains(loc.add(MaskSize, 0, 0))) {
                                
                                integers.remove(Integer.valueOf(LastBlockDiv));
                                
                            }
                            
                            if(integers.contains(LastRowDiv) && Blocks.contains(loc.add(0, 0, -MaskSize))) {
                                
                                integers.remove(Integer.valueOf(LastRowDiv));
                                
                            }
                            
                            int randomInt = integers.get((int) (Math.random() * ((integers.size() - 0))));
                            
                            if(!usedIntegers.contains(randomInt)) {
                                
                                usedIntegers.add(randomInt);
                                
                            }
                            
                            placedBlockLocations.add(loc);
                            
                            if(!integers.contains(LastBlockDiv) && Blocks.contains(loc.add(MaskSize, 0, 0))) {
                                
                                integers.add(LastBlockDiv);
                                
                            }
                            
                            if(!integers.contains(LastRowDiv) && Blocks.contains(loc.add(0, 0, -MaskSize))) {
                                
                                integers.add(LastRowDiv);
                                
                            }
                            
                            Block randomColorBlockBlock = Block.get(35, randomInt);
                            
                            if(Blocks.contains(loc)) {
                                
                                Arena.getGameWorld().setBlock(loc, randomColorBlockBlock);
                            
                            }
                            
                        }
                        else {
                            
                            Block LocationBlock = Arena.getGameWorld().getBlock(loc.clone().add(1, 0, 0));
                            
                            placedBlockLocations.add(loc);
                            
                            if(Blocks.contains(LocationBlock.getLocation())) {
                            
                                Arena.getGameWorld().setBlock(loc, LocationBlock);
                            
                            }
                            
                        }
                           
                        
                    } 
                    else {
                        
                        Block LocationBlock = Arena.getGameWorld().getBlock(loc.clone().add(0, 0, -1));
                        
                        placedBlockLocations.add(loc);
                        
                        if(Blocks.contains(LocationBlock.getLocation())) {
                        
                            Arena.getGameWorld().setBlock(loc, LocationBlock);
                        
                        }
                        
                   }
                       
                    
                        
                        placedColorBlockCount++;
                        
                        if(placedColorBlockCount == MaskSize) {
                            
                            placedColorBlockCount = 0;
                            
                        }
                        
                        block++;
                        
                    }
                
                placedColorBlockRowCount++;
                
                if(placedColorBlockRowCount == MaskSize) {
                    
                    placedColorBlockRowCount = 0;
                    
                }
                
                row++;
            }
            
        });
                    
    }
    public void pickColor(int color) {
        
        CompletableFuture.runAsync(() -> {
                
            for(Location BlockLocation : placedBlockLocations) {
                    
                if(Arena.getGameWorld().getBlock(BlockLocation).getDamage() != color) {
                        
                    removedBlocks.add(Arena.getGameWorld().getBlock(BlockLocation));
                    Arena.getGameWorld().setBlock(BlockLocation, Block.get(0));
                        
                }
                    
            }
                
        });
    }
    public void back() {
        
        CompletableFuture.runAsync(() -> {
                
            for(Block removedBlock : removedBlocks) {
                    
                Arena.getGameWorld().setBlock(removedBlock, removedBlock);
                    
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
