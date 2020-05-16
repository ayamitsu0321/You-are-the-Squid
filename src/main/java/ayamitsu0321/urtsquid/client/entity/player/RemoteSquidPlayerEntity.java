package ayamitsu0321.urtsquid.client.entity.player;

import ayamitsu0321.urtsquid.URTSquid;
import ayamitsu0321.urtsquid.client.renderer.entity.SquidPlayerRenderer;
import ayamitsu0321.urtsquid.entity.player.ISquidPlayerEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class RemoteSquidPlayerEntity extends RemoteClientPlayerEntity implements ISquidPlayerEntity {

    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    public float squidRotation;
    public float prevSquidRotation;
    public float tentacleAngle;
    public float lastTentacleAngle;
    //private float randomMotionSpeed;
    private float rotationVelocity;
    private float rotateSpeed;
    //private float randomMotionVecX;
    //private float randomMotionVecY;
    //private float randomMotionVecZ;

    public RemoteSquidPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
        this.rand.setSeed((long)(1 + this.getEntityId()));
        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
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
            case CROUCHING:
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

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if(id == 19) {
            this.squidRotation = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }
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
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;

        /** rotation */
        if((double)this.squidRotation > (Math.PI * 2D)) {
            if (this.world.isRemote) {
                this.squidRotation -= ((float)Math.PI * 2F);
            } else {
                this.squidRotation = (float)((double)this.squidRotation - (Math.PI * 2D));

                if(this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
                }

                this.world.setEntityState(this, (byte)19);
            }
        }

        if (this.isInWaterOrBubbleColumn()) {
            /** tentacle */
            if(this.squidRotation < (float)Math.PI) {
                float lvt_1_3_ = this.squidRotation / (float)Math.PI;
                this.tentacleAngle = MathHelper.sin(lvt_1_3_ * lvt_1_3_ * (float)Math.PI) * (float)Math.PI * 0.25F;

                if((double)lvt_1_3_ > 0.75D) {
                    //this.randomMotionSpeed = 1.0F;
                    this.rotateSpeed = 1.0F;
                } else {
                    this.rotateSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                //this.randomMotionSpeed *= 0.9F;
                this.rotateSpeed *= 0.99F;
            }

            Vec3d motionVec = this.getMotion();
            float lvt_2_1_ = MathHelper.sqrt(horizontalMag(motionVec));
            double yDistance = motionVec.y;

            /** yaw */
            this.squidYaw = (float)((double)this.squidYaw + Math.PI * (double)this.rotateSpeed * 1.5D);

            /** pitch */
            this.squidPitch += (-((float)MathHelper.atan2((double)lvt_2_1_, yDistance)) * (180F / (float)Math.PI) - this.squidPitch) * 0.1F;
        } else {

            /** tentacle */
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float)Math.PI * 0.25F;

            /** yaw */
            if (this.isPassenger()) {
                // front direction
                this.squidYaw = (float)((double)this.squidYaw + (double)(360.0F - this.squidYaw) * 0.02D);
            }

            /** pitch */
            Vec3d motionVec = this.getMotion();
            float lvt_2_1_ = MathHelper.sqrt(horizontalMag(motionVec));
            double yDistance = motionVec.y;

            if (this.isPassenger()) {
                // up direction
                this.squidPitch = (float)((double)this.squidPitch + (double)(0.0F - this.squidPitch) * 0.02D);
            } else if (this.onGround) {
                // side direction
                this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0F - this.squidPitch) * 0.02D);
            } else {
                // move direction
                this.squidPitch += (-((float)MathHelper.atan2((double)lvt_2_1_, yDistance)) * (180F / (float)Math.PI) - this.squidPitch) * 0.1F;
            }
        }

        /** normalize(0 - 360) */
        if (this.squidYaw > 360.0F) {
            this.prevSquidYaw = (float)(((double)prevSquidYaw - 360.0D) % 360.0D);
        }
        this.squidYaw %= 360.0F;
        if (this.squidPitch > 360.0F) {
            this.prevSquidPitch = (float)(((double)prevSquidPitch - 360.0D) % 360.0D);
        }
        this.squidPitch %= 360.0F;
    }

    @Override
    public ResourceLocation getLocationSkin() {
        return SquidPlayerRenderer.SQUID_TEXTURE;
    }

    /** ISquidPlayerEntity */

    @Override
    public float getSquidPitch() {
        return this.squidPitch;
    }

    @Override
    public float getPrevSquidPitch() {
        return this.prevSquidPitch;
    }

    @Override
    public float getSquidYaw() {
        return this.squidYaw;
    }

    @Override
    public float getPrevSquidYaw() {
        return this.prevSquidYaw;
    }

    @Override
    public float getTentacleAngle() {
        return this.tentacleAngle;
    }

    @Override
    public float getLastTentacleAngle() {
        return this.lastTentacleAngle;
    }
}
