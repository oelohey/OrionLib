package dev.oelohey.orion.item;

import dev.oelohey.orion.accesor.ScreenshakeNBTAcessor;
import dev.oelohey.orion.entity.WrapperEntity;
import dev.oelohey.orion.internal_util.WindlassCargoHelper;
import dev.oelohey.orion.util.screenshake.GlobalScreenshakeInstance;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestStick extends Item {
    public TestStick(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (!context.getWorld().isClient) {
            WrapperEntity cargo = WindlassCargoHelper.createWindlassCargo((ServerWorld) context.getWorld(), context.getBlockPos().add(-9,-9,-9), context.getBlockPos().add(9,9,9), true);
            context.getWorld().spawnEntity(cargo);
            cargo.calculateDimensions();
        }

        return ActionResult.SUCCESS;
    }
}
