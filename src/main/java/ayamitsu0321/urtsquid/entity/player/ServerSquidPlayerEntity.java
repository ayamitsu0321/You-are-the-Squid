package ayamitsu0321.urtsquid.entity.player;

import ayamitsu0321.urtsquid.URTSquid;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class ServerSquidPlayerEntity extends ServerPlayerEntity implements ISquidPlayerEntity {

    public ServerSquidPlayerEntity(MinecraftServer server, ServerWorld world, GameProfile profile, PlayerInteractionManager interactionManager) {
        super(server, world, profile, interactionManager);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public double getYOffset() {
        return this.isPassenger() ? 0.6D : super.getYOffset();
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return URTSquid.SIZE_BY_POSE.getOrDefault(poseIn, STANDING_SIZE);
    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        switch(poseIn) {
            case SWIMMING:
            case FALL_FLYING:
            case SPIN_ATTACK:
                return sizeIn.height / 2.0F * 0.85F;
            case SNEAKING:
                return sizeIn.height / 3.0F * 2.0F * 0.85F;
            default:
                return sizeIn.height * 0.85F;
        }
    }

    @Override
    public float getDigSpeed(BlockState state, @Nullable BlockPos pos) {
        float digSpeed = super.getDigSpeed(state, pos);

        if (this.isInWater() && !EnchantmentHelper.hasAquaAffinity(this)) {
            digSpeed *= 5.0F;
        }

        return digSpeed;
    }

    @Override
    public void baseTick() {
        int prevAir = this.getAir();
        super.baseTick();
        this.updateAir(prevAir);
    }

    /**
     * from:net.minecraft.entity.passive.WatetMobWntity.java#updateAir
     * @param prevAir
     */
    protected void updateAir(int prevAir) {
        if (this.isAlive() && !this.isInWaterOrBubbleColumn()) {
            this.setAir(prevAir - 1);
            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAir(300);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.squidTick();
    }

    public void squidTick() {
    }

    @Override
    public void travel(Vec3d p_213352_1_) {
        Vec3d motionVec = this.getMotion();

        if (!this.isPassenger()) {
            if (!this.isInWater() || this.abilities.isFlying) {
                ;
            } else {
                this.setMotion(motionVec.getX() * 1.1375D, motionVec.getY(), motionVec.getZ() * 1.1375D);
            }
        }

        super.travel(p_213352_1_);
    }

    /** ISquidPlayerEntity */

    @Override
    public float getSquidPitch() {
        return 0.0F;
    }

    @Override
    public float getPrevSquidPitch() {
        return 0.0F;
    }

    @Override
    public float getSquidYaw() {
        return 0.0F;
    }

    @Override
    public float getPrevSquidYaw() {
        return 0.0F;
    }

    @Override
    public float getTentacleAngle() {
        return 0.0F;
    }

    @Override
    public float getLastTentacleAngle() {
        return 0.0F;
    }
}
