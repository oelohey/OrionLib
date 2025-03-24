package dev.oelohey.orion.infrastructure.wrapper_world.chunkspace;

import dev.oelohey.orion.OrionLib;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

public class ChunkspaceManager {
    private final Chunkspace[][][] CHUNKSPACE_STORAGE = new Chunkspace[15][15][15];

    public @Nullable Chunkspace getChunk(int x, int y , int z) {
        if (x+8 > 15 || y+8 > 15 || z+8 > 15) {
            OrionLib.LOGGER.info("Unable to load chunk on Wrapper due to request exceeding the size limit");
            return null;
        }
        return CHUNKSPACE_STORAGE[x+8][y+8][z+8];
    }

    public @Nullable Chunkspace getChunk(ChunkspacePosition pos) {
        return getChunk(pos.getX(), pos.getY(), pos.getZ());
    }

    public void setChunk(Chunkspace chunk){
        CHUNKSPACE_STORAGE[chunk.getChunkPos().getX()+8][chunk.getChunkPos().getY()+8][chunk.getChunkPos().getZ()+8] = chunk;
    }

    public boolean doesChunkExist(ChunkspacePosition spatialChunkPos){
        boolean bool = this.doesChunkExist(spatialChunkPos.getX(), spatialChunkPos.getY(), spatialChunkPos.getZ());
        OrionLib.LOGGER.info(String.valueOf(bool));
        return bool;
    }

    public boolean doesChunkExist(int x, int y, int z){
        return CHUNKSPACE_STORAGE[x+8][y+8][z+8] != null;
    }

    public void tick(){
        for (int x = 0; x < 15; x++){
            for (int y = 0; y < 15; y++){
                for (int z = 0; z < 15; z++){
                    Chunkspace chunkspace = CHUNKSPACE_STORAGE[x][y][z];
                    if (chunkspace != null) chunkspace.tick();
                }
            }
        }
    }

    private void tick(BooleanSupplier shouldKeepTicking, boolean tickChunks) {

    }
}
