package dev.oelohey.orion.internal_util;

import dev.oelohey.orion.OrionLib;
import dev.oelohey.orion.entity.WrapperEntity;
import dev.oelohey.orion.register.OrionEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class WindlassCargoHelper {

    //WILL REDO

    public static WrapperEntity createWindlassCargo(ServerWorld world, BlockPos pos1, BlockPos pos2, boolean destroyOriginal){

        WrapperEntity cargo = new WrapperEntity(OrionEntities.TEST, world);
        HashMap<BlockPos, BlockState> cargoList = getCargo(world.toServerWorld(), pos1, pos2);
        OrionLib.LOGGER.info(cargoList.toString());
        setCargoToEntity(cargoList, cargo);
        cargo.setPosition(WindlassCargoHelper.getCenterPos(pos1, pos2));
        return cargo;
    }

    public static HashMap<BlockPos, BlockState> getCargo(ServerWorld world, BlockPos pos1, BlockPos pos2){
        HashMap<BlockPos, BlockState> cargo = new HashMap<>();

        for (int iX = pos1.getX(); blockCheck(iX-pos1.getX(), pos1.getX()-pos2.getX());){
            for (int iZ = pos1.getZ(); blockCheck(iZ-pos1.getZ(), pos1.getZ()-pos2.getZ());){
                for (int iY = pos1.getY(); blockCheck(iY-pos1.getY(), pos1.getY()-pos2.getY());){
                    BlockPos gridBlock = pos1.add(iX-pos1.getX(), iY-pos1.getY(), iZ-pos1.getZ());
                    if (!world.getBlockState(gridBlock).isAir()){
                        cargo.put(new BlockPos(iX-pos1.getX(), iY-pos1.getY(), iZ-pos1.getZ()), world.getBlockState(gridBlock));
                        world.setBlockState(gridBlock, Blocks.AIR.getDefaultState());
                    }
                    iY+=getOffset(pos1.getY(), pos2.getY());
                }
                iZ+=getOffset(pos1.getZ(), pos2.getZ());
            }
            iX+=getOffset(pos1.getX(), pos2.getX());
        }
        return cargo;
    }

    public static void setCargoToEntity(HashMap<BlockPos, BlockState> cargo, WrapperEntity wrapper){
        for (BlockPos pos : cargo.keySet()){
            BlockState state = cargo.get(pos);
            wrapper.getWrapperWorld().setBlockState(pos, state);
        }
    }

    public static Vec3d getCenterPos(BlockPos pos1, BlockPos pos2){
        Vec3d pos1VEC = pos1.toCenterPos();
        Vec3d pos2VEC = pos2.toCenterPos();

        Vec3d originVEC = pos2VEC.subtract(pos1VEC);
        originVEC = originVEC.multiply(0.5);
        originVEC = originVEC.add(pos1VEC);
        originVEC = originVEC.add(0, 0, 0);
        return originVEC;
    }

    private static boolean blockCheck(int i, int k){
        return Math.abs(i) <= Math.abs(k);
    }

    private static int getOffset(int i, int k){
        if (k == i){
            return 1;
        }
        return Integer.compare(k, i);
    }
}
