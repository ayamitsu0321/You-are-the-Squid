package ayamitsu.urtsquid.player;

import ayamitsu.urtsquid.client.renderer.RenderPlayerSquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.MobEffects;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by ayamitsu0321 on 2016/03/20.
 */
public class EntityPlayerSquidSP extends EntityPlayerSP {

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

    public EntityPlayerSquidSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile) {
        super(mcIn, worldIn, netHandler, statFile);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void preparePlayerToSpawn() {
        this.setSize(this.getDefaultWidth(), this.getDefaultHeight());

        if (this.worldObj != null) {
            while (this.posY > 0.0D && this.posY < 256.0D) {
                this.setPosition(this.posX, this.posY, this.posZ);

                if (this.worldObj.getCubes(this, this.getEntityBoundingBox()).isEmpty()) {
                    break;
                }

                ++this.posY;
            }

            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.rotationPitch = 0.0F;
        }


        this.setHealth(this.getMaxHealth());
        this.deathTime = 0;
    }

    private boolean isHeadspaceFree(BlockPos pos, int height) {
        for (int y = 0; y < height; y++) {
            if (isOpenBlockSpace(pos.add(0, y, 0))) return false;
        }

        return true;
    }

    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
        if (this.noClip) return false;
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = x - (double)blockpos.getX();
        double d1 = z - (double)blockpos.getZ();

        int entHeight = Math.max(Math.round(this.height), 1);

        boolean inTranslucentBlock = this.isHeadspaceFree(blockpos, entHeight);

        if (inTranslucentBlock) {
            int i = -1;
            double d2 = 9999.0D;

            if (!this.isHeadspaceFree(blockpos.west(), entHeight) && d0 < d2) {
                d2 = d0;
                i = 0;
            }

            if (!this.isHeadspaceFree(blockpos.east(), entHeight) && 1.0D - d0 < d2) {
                d2 = 1.0D - d0;
                i = 1;
            }

            if (!this.isHeadspaceFree(blockpos.north(), entHeight) && d1 < d2) {
                d2 = d1;
                i = 4;
            }

            if (!this.isHeadspaceFree(blockpos.south(), entHeight) && 1.0D - d1 < d2) {
                d2 = 1.0D - d1;
                i = 5;
            }

            float f = 0.1F;

            if (i == 0) {
                this.motionX = (double)(-f);
            }

            if (i == 1) {
                this.motionX = (double)f;
            }

            if (i == 4) {
                this.motionZ = (double)(-f);
            }

            if (i == 5) {
                this.motionZ = (double)f;
            }
        }

        return false;
    }

    private boolean isOpenBlockSpace(BlockPos pos) {
        return !this.worldObj.getBlockState(pos).isNormalCube();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public float getBreakSpeed(IBlockState state, BlockPos pos) {
        float breakSpeed = super.getBreakSpeed(state, pos);

        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            breakSpeed *= 5.0F;
        }

        return breakSpeed;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.onUpdateSquid();
        //System.out.println("Client:[x,y,z]=[" + String.format("%3f", this.posX) + ", " + String.format("%3f", this.posY) + ", " + String.format("%3f", this.posZ) + "]");
    }

    @Override
    public void onEntityUpdate() {
        int air = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInsideOfMaterial(Material.water) && !this.isPotionActive(MobEffects.waterBreathing) && !this.capabilities.disableDamage) {
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
