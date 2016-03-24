package ayamitsu.urtsquid.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * Created by ayamitsu0321 on 2016/03/20.
 */
public class EntityPlayerSquidMP extends EntityPlayerMP {

    public EntityPlayerSquidMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, PlayerInteractionManager interactionManagerIn) {
        super(server, worldIn, profile, interactionManagerIn);
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

    /*
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
    */
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
        //System.out.println("Server:[x,y,z]=[" + String.format("%3f", this.posX) + ", " + String.format("%3f", this.posY) + ", " + String.format("%3f", this.posZ) + "]");
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
                this.motionX *= 1.1375D;// 0.91F / 0.8F
                //this.motionY *= 1.1375D;
                this.motionZ *= 1.1375D;

            }
        }

        super.moveEntityWithHeading(strafe, forward);
    }

    public void onUpdateSquid() {
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

}
