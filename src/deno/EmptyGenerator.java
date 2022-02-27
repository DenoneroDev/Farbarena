package deno;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import deno.arena.Arena;

import java.util.HashMap;
import java.util.Map;

public class EmptyGenerator extends Generator {
    
    private ChunkManager level;

    public EmptyGenerator(Map<String, Object> options) {
    
        //LEER
    
    }
    public int getId() {
    
        return 99;
    
    }
    public void init(ChunkManager chunkManager, NukkitRandom nukkitRandom) {
    
        this.level = chunkManager;

    }
    public void generateChunk(int chunkX, int chunkZ) {
       
        //Leer
    
    }
    public void populateChunk(int chunkX, int chunkZ) {

        //Leer
    
    }
    public Map<String, Object> getSettings() {

        return new HashMap<>();
    
    }
    public String getName() {
    
        return Arena.getGameWorldName();
    }
    public Vector3 getSpawn() {
    
        return new Vector3(0, 7, 0);
    
    }
    public ChunkManager getChunkManager() {
    
        return level;
    
    }
}