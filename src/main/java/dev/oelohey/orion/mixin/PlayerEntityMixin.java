package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.ScreenshakeNBTAcessor;
import dev.oelohey.orion.internal_util.NbtHelperOrion;
import dev.oelohey.orion.infrastructure.ScreenshakeInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity implements ScreenshakeNBTAcessor {

	@Unique
	private static final TrackedData<NbtCompound> INSTANCES_DATA_TRACKER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

	public PlayerEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void orion$tick(CallbackInfo ci) {
		List<ScreenshakeInstance> INSTANCES = orion$getInstancestoModify();
		if (!INSTANCES.isEmpty()) {
			List<ScreenshakeInstance> NewInstances = new ArrayList<>();
			for (ScreenshakeInstance instance : INSTANCES) {
				if (instance != null) {
					instance.duration--;
					if (instance.duration > 0) {
						NewInstances.add(instance);
					}
				}
			}
			INSTANCES = NewInstances;
		}
		orion$applyInstancesModification(INSTANCES);
	}

	@Unique
	private List<ScreenshakeInstance> orion$getInstancestoModify(){
		NbtCompound compound = this.getDataTracker().get(INSTANCES_DATA_TRACKER);
		return orion$decompileList(compound.getList("nbtList", NbtElement.COMPOUND_TYPE));
	}

	@Unique
	private void orion$applyInstancesModification(List<ScreenshakeInstance> list){
		NbtCompound newCompound = new NbtCompound();
		NbtList nbtList = orion$encodeList(list);
		newCompound.put("nbtList", nbtList);
		this.getDataTracker().set(INSTANCES_DATA_TRACKER, newCompound);
	}

	@Unique
	private List<ScreenshakeInstance> orion$decompileList(NbtList nbt){
		List<ScreenshakeInstance> INSTANCES = new ArrayList<>();
		for (NbtElement list : nbt){
			if (list.getType() == NbtElement.COMPOUND_TYPE){
				NbtCompound compound = (NbtCompound) list;
				ScreenshakeInstance instance = NbtHelperOrion.toScreenshakeInstance(compound);
				INSTANCES.add(instance);
			}
		}
		return INSTANCES;
	}

	@Unique
	private NbtList orion$encodeList(List<ScreenshakeInstance> list){
		NbtList nbtList = new NbtList();
		for (ScreenshakeInstance instance : list){
			nbtList.add(NbtHelperOrion.fromScreenshakeInstance(instance));
		}
		return nbtList;
	}

	@Inject(at = @At("HEAD"), method = "initDataTracker")
	private void orion$appendDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(INSTANCES_DATA_TRACKER, new NbtCompound());
	}

	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	private void orion$writeNBTScreenshake(NbtCompound nbt, CallbackInfo ci) {
		NbtCompound compound = this.getDataTracker().get(INSTANCES_DATA_TRACKER);
		NbtList list = compound.getList("nbtList", NbtElement.COMPOUND_TYPE);
		nbt.put("ScreenshakeInstances", list);
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	private void orion$readNBTScreenshake(NbtCompound nbt, CallbackInfo ci) {
		NbtCompound newCompound = new NbtCompound();
		newCompound.put("nbtList", nbt.getList("ScreenshakeInstances", NbtElement.COMPOUND_TYPE));
		this.getDataTracker().set(INSTANCES_DATA_TRACKER, newCompound);
	}

	@Override
	public List<ScreenshakeInstance> orion$getInstances(){
		return orion$getInstancestoModify();
	}
	@Override
	public void orion$addInstance(ScreenshakeInstance instance){
		List<ScreenshakeInstance> INSTANCES = orion$getInstancestoModify();
		INSTANCES.add(instance);
		orion$applyInstancesModification(INSTANCES);
	}
	@Override
	public void orion$resetInstances(){
		List<ScreenshakeInstance> INSTANCES = orion$getInstancestoModify();
		INSTANCES.clear();
		orion$applyInstancesModification(INSTANCES);
	}
}