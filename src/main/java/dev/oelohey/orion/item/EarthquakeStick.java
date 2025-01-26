package dev.oelohey.orion.item;

import dev.oelohey.orion.accesor.ScreenshakeNBTAcessor;
import dev.oelohey.orion.util.screenshake.GlobalScreenshakeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EarthquakeStick extends Item {
    public EarthquakeStick(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        if (user instanceof ScreenshakeNBTAcessor nbtAcessor){
            nbtAcessor.orion$addInstance(GlobalScreenshakeInstance.createInstance(200, 1.5f, 6, true));
        }

        return ActionResult.SUCCESS;
    }
}
