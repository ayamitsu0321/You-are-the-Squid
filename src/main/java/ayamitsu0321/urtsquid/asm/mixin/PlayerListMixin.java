package ayamitsu0321.urtsquid.asm.mixin;

import ayamitsu0321.urtsquid.entity.player.ServerSquidPlayerEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    // createPlayerForUser/func_148545_a
    @Redirect(
            method = "createPlayerForUser",
            at = @At(
                    value = "NEW",
                    args = "class=net/minecraft/entity/player/ServerPlayerEntity",
                    remap = false
            )
    )
    private ServerPlayerEntity injectCreatePlayerForUser(MinecraftServer server, ServerWorld worldIn, GameProfile profile, PlayerInteractionManager interactionManagerIn) {
        System.out.println("inject createPlayerForUser.");
        return new ServerSquidPlayerEntity(server, worldIn, profile, interactionManagerIn);
    }

    // recreatePlayerEntity/func_232644_a_
    @Redirect(
            method = "func_232644_a_",
            at = @At(
                    value = "NEW",
                    args = "class=net/minecraft/entity/player/ServerPlayerEntity",
                    remap = false
            )
    )
    private ServerPlayerEntity injectRecreatePlayerEntity(MinecraftServer server, ServerWorld worldIn, GameProfile profile, PlayerInteractionManager interactionManagerIn) {
        System.out.println("inject recreatePlayerEntity.");
        return new ServerSquidPlayerEntity(server, worldIn, profile, interactionManagerIn);
    }

}
