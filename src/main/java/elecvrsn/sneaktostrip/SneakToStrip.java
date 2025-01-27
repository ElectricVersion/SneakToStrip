package elecvrsn.sneaktostrip;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.tags.BlockTags.LOGS;

@Mod("sneaktostrip")
public class SneakToStrip
{
    public SneakToStrip()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static BlockHitResult getPlayerPOVHitResult(Player player) {
        //Basically ported over from Item class so I can use it to determine the targeted block
        float xRot = player.getXRot();
        float yRot = player.getYRot();
        Vec3 eyePosition = player.getEyePosition();
        float f1 = Mth.cos(-yRot * ((float)Math.PI / 180F) - (float)Math.PI);
        float f2 = Mth.sin(-yRot * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = -Mth.cos(-xRot * ((float)Math.PI / 180F));
        float f4 = Mth.sin(-xRot * ((float)Math.PI / 180F));
        float f5 = f2 * f3;
        float f6 = f1 * f3;
        double reachDistance = player.getReachDistance();
        Vec3 targetVector = eyePosition.add((double)f5 * reachDistance, (double)f4 * reachDistance, (double)f6 * reachDistance);
        return player.level.clip(new ClipContext(eyePosition, targetVector, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUse(InputEvent.ClickInputEvent event)
    {
        if (event.isUseItem()) {
            Player player = Minecraft.getInstance().player;
            if (player != null && player.getItemInHand(event.getHand()).getItem() instanceof AxeItem && !player.isShiftKeyDown() && player.level.getBlockState(getPlayerPOVHitResult(player).getBlockPos()).is(LOGS)) {
                event.setCanceled(true);
            }
        }
    }
}
