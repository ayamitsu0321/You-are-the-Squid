package ayamitsu0321.urtsquid.asm.mixin;

import ayamitsu0321.urtsquid.client.entity.player.ClientSquidPlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stats.StatisticsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerController.class)
public class PlayerControllerMixin {

    // createPlayer/func_239167_a_
    @Redirect(
            method = "func_239167_a_",
            at = @At(
                    value = "NEW",
                    args = "class=net/minecraft/client/entity/player/ClientPlayerEntity",
                    remap = false
            )
    )
    private ClientPlayerEntity injectCreatePlayer(Minecraft mc, ClientWorld world, ClientPlayNetHandler connection, StatisticsManager stats, ClientRecipeBook recipeBook, boolean clientSneakState, boolean clientSprintState) {
        System.out.println("inject createPlayer.");
        return new ClientSquidPlayerEntity(mc, world, connection, stats, recipeBook, clientSneakState, clientSprintState);
    }

}
