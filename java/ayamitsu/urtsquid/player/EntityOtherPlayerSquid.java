package ayamitsu.urtsquid.player;

import ayamitsu.urtsquid.client.renderer.RenderPlayerSquid;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Created by ayamitsu0321 on 2016/03/21.
 */
public class EntityOtherPlayerSquid extends EntityOtherPlayerMP {

    /**
     * from squid fields
     */
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
    private float field_70871_bB;
    //private float randomMotionVecX;
    //private float randomMotionVecY;
    //private float randomMotionVecZ;

    public EntityOtherPlayerSquid(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
        this.setSize(this.getDefaultWidth(), this.getDefaultHeight());
        this.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
        this.rand.setSeed((long)(1 + this.getEntityId()));
        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
    }

    @Override
    public float getEyeHeight() {
        float f = this.eyeHeight;

        if (this.isPlayerSleeping()) {
            f = 0.2F;
        } else if (!this.isSneaking() && this.height != this.getDefaultEyeHeight()) {
            if (this.isElytraFlying() || this.height == (this.getDefaultHeight() / 2)) {
                f = 0.4F;
            }
        } else {
            f -= 0.08F;
        }

        return f;
    }

    @Override
    protected void resetHeight() {
        super.resetHeight();
        this.setSize(this.getDefaultWidth(), this.getDefaultHeight());
        //this.yOffset = 0.0F;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.onUpdateSquid();
    }

    @Override
    public void onEntityUpdate() {
        int air = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING) && !this.capabilities.disableDamage) {
            --air;
            this.setAir(air);

            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.drown, 2);
            }
        } else {
            this.setAir(300);
        }
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (this.isServerWorld() || this.canPassengerSteer()) {
            if (!this.isInWater() || this.capabilities.isFlying) {
                ;
            } else {
                //this.motionY += 0.02D;
                this.motionX *= 1.1375D;
                //this.motionY *= 1.1375D;
                this.motionZ *= 1.1375D;

            }
        }

        super.moveEntityWithHeading(strafe, forward);
    }

    public void onUpdateSquid() {
        //System.out.println("onUpdateSquid");
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;

        if ((double)this.squidRotation > (Math.PI * 2D)) {
            this.squidRotation -= ((float)Math.PI * 2F);

            if (this.rand.nextInt(10) == 0) {
                this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
            }
        }

        if (!this.onGround) {
            if (this.squidRotation < (float)Math.PI) {
                float f = this.squidRotation / (float)Math.PI;
                this.tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;

                if ((double)f > 0.75D) {
                    //this.randomMotionSpeed = 1.0F;
                    this.field_70871_bB = 1.0F;
                } else {
                    this.field_70871_bB *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                //this.randomMotionSpeed *= 0.9F;
                this.field_70871_bB *= 0.99F;
            }


            float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            //this.renderYawOffset += (-((float)MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float)Math.PI) - this.renderYawOffset) * 0.1F;
            //this.rotationYaw = this.renderYawOffset;
            //this.squidYaw += (float)(Math.PI * (double)this.field_70871_bB * 1.5D);

            if (this.getRidingEntity() == null) {
                this.squidYaw += (float)(Math.PI * (double)this.field_70871_bB * 1.5D);
                this.squidPitch += (-((float)MathHelper.atan2((double)f1, this.motionY)) * (180F / (float)Math.PI) - this.squidPitch) * 0.1F;
            } else {
                this.squidYaw = 0F;
                float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.squidPitch += (-((float)MathHelper.atan2((double)f, (this.posY - this.lastTickPosY))) * 180.0F / (float)Math.PI - this.squidPitch) * 0.1F;
            }
        } else {
            if (this.isInWater()) {
                if (this.squidRotation < (float)Math.PI) {
                    float f = this.squidRotation / (float)Math.PI;
                    this.tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;

                    if ((double)f > 0.75D) {
                        //this.randomMotionSpeed = 1.0F;
                        this.field_70871_bB = 1.0F;
                    } else {
                        this.field_70871_bB *= 0.8F;
                    }
                } else {
                    this.tentacleAngle = 0.0F;
                    //this.randomMotionSpeed *= 0.9F;
                    this.field_70871_bB *= 0.99F;
                }
            } else {
                this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float)Math.PI * 0.25F;
            }

            if (this.getRidingEntity() == null) {
                this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0F - this.squidPitch) * 0.02D);
            } else {
                float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.squidPitch += (-((float)MathHelper.atan2((double)f, (this.posY - this.lastTickPosY))) * 180.0F / (float)Math.PI - this.squidPitch) * 0.1F;
            }
        }
    }

    public float getDefaultWidth() {
        return 0.6F; // default player's height
    }

    public float getDefaultHeight() {
        return 0.8F;
    }

    @Override
    public float getDefaultEyeHeight() {
        return 0.425F;
    }

    @Override
    public double getYOffset() {
        return this.getRidingEntity() != null ? 0.6D : super.getYOffset();
    }

    @Override
    protected void updateSize() {
        float f = this.getDefaultWidth();
        float f1 = this.getDefaultHeight();

        if (this.isElytraFlying()) {
            f = this.getDefaultWidth();
            f1 = this.getDefaultHeight() / 2;
        } else if (this.isPlayerSleeping()) {
            f = 0.2F;
            f1 = 0.2F;
        } else if (this.isSneaking()) {
            f = this.getDefaultWidth();
            f1 = this.getDefaultHeight() - 0.15F;
        } else {
            f = this.getDefaultWidth();
            f1 = this.getDefaultHeight();
        }

        if (f != this.width || f1 != this.height) {
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)f, axisalignedbb.minY + (double)f1, axisalignedbb.minZ + (double)f);

            if (!this.worldObj.collidesWithAnyBlock(axisalignedbb)) {
                this.setSize(f, f1);
            }
        }
        net.minecraftforge.fml.common.FMLCommonHandler.instance().onPlayerPostTick(this);
    }

    @Override
    public ResourceLocation getLocationSkin() {
        return RenderPlayerSquid.squidTextures;
    }

}
