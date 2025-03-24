package dev.oelohey.orion.infrastructure.wrapper_world.chunkspace;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class ChunkspacePosition {

    private final int x;
    private final int y;
    private final int z;

    private int hashCode;

    public ChunkspacePosition(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;

        this.hashCode = Objects.hash(x,y,z);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getZ(){
        return z;
    }

    public static ChunkspacePosition chunkPosFromBlockPos(BlockPos pos){
        return new ChunkspacePosition(chunkCoord(pos.getX()),chunkCoord(pos.getY()),chunkCoord(pos.getZ()));
    }

    public static int chunkCoord(int coord){
        float coordDiv = (float)coord/16;
        if (coordDiv > 0){
            return MathHelper.floor(coordDiv);
        }else{
            return MathHelper.ceil(coordDiv);
        }
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkspacePosition position = (ChunkspacePosition) o;
        return this.x == position.x && this.y == position.y && this.z == position.z;
    }

    @Override
    public int hashCode(){
        return this.hashCode;
    }
}
