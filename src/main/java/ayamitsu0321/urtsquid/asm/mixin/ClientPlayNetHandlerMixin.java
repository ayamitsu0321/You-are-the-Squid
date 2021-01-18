package ayamitsu0321.urtsquid.asm.mixin;

import ayamitsu0321.urtsquid.client.entity.player.RemoteSquidPlayerEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetHandler.class)
public class ClientPlayNetHandlerMixin {

    // handleSpawnPlayer/func_147237_a
    @Redirect(
            method = "handleSpawnPlayer",
            at = @At(
                    value = "NEW",
                    args = "class=net/minecraft/client/entity/player/RemoteClientPlayerEntity",
                    remap = false
            )
    )
    private RemoteClientPlayerEntity injectHandleSpawnPlayer(ClientWorld world, GameProfile profile) {
        System.out.println("inject handleSpawnPlayer.");
        return new RemoteSquidPlayerEntity(world, profile);
    }

}
