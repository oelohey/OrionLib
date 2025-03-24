package dev.oelohey.orion.entity;

import dev.oelohey.orion.infrastructure.wrapper_world.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class WrapperEntity extends Entity {

    private Wrapper WRAPPER_WORLD;

    public WrapperEntity(EntityType<?> type, World world) {
        super(type, world);
        if (!this.getWorld().isClient) {
            this.WRAPPER_WORLD = new Wrapper(this);
        }
    }

    @Override
    public void tick() {
        this.baseTick();
        if (!this.getWorld().isClient()) {
            if (WRAPPER_WORLD != null) {
                WRAPPER_WORLD.tick();
            }
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    public Wrapper getWrapperWorld(){
        return this.WRAPPER_WORLD;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
