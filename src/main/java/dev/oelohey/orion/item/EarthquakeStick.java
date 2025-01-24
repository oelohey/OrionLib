package dev.oelohey.orion.item;

import dev.oelohey.orion.accesor.ScreenshakeNBTAcessor;
import dev.oelohey.orion.util.screenshake.GlobalScreenshakeInstance;
import dev.oelohey.orion.util.screenshake.PositionedScreenshakeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class EarthquakeStick extends Item {
    public EarthquakeStick(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user instanceof ScreenshakeNBTAcessor nbtAcessor){
            nbtAcessor.orion$addInstance(GlobalScreenshakeInstance.createInstance(200, 3, 6, true));
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
