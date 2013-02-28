package ayamitsu.urtsquid.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.network.PacketHandler;

public class EntityPlayerSquidMP extends EntityPlayerMP {

	public PlayerStatus playerStatus = new PlayerStatus();

	public EntityPlayerSquidMP(MinecraftServer mcServer, World world, String username, ItemInWorldManager itemInWorldManager) {
		super(mcServer, world, username, itemInWorldManager);
		this.texture = "/mob/squid.png";
		this.setSize(0.75F, 0.95F);
		this.yOffset = 0.0F;
		this.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
	}

	/*@Override
	public double getYOffset() {
		return this.yOffset + 0.5D;
	}*/

	@Override
	public float getEyeHeight() {
		return 0.425F;//this.yOffset;//0.425F;//this.height * 0.85F;// this.yOffset// 0.12F
	}

	@Override
	protected void resetHeight() {
		super.resetHeight();
		this.setSize(0.75F, 0.95F);
		this.yOffset = 0.0F;
	}

	@Override
	public void preparePlayerToSpawn() {
		this.yOffset = 0.0F;//
		this.setSize(0.75F, 0.95F);

		if (this.worldObj != null) {
			while (this.posY > 0.0D) {
				this.setPosition(this.posX, this.posY, this.posZ);

				if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
					break;
				}

				++this.posY;
			}

			this.motionX = this.motionY = this.motionZ = 0.0D;
			this.rotationPitch = 0.0F;
		}

		this.setEntityHealth(this.getMaxHealth());
		this.deathTime = 0;
	}

	@Override
	public EnumStatus sleepInBedAt(int par1, int par2, int par3) {
		EnumStatus status = super.sleepInBedAt(par1, par2, par3);

		if (status == EnumStatus.OK) {
			//this.setPosition(this.posX, this.posY - 0.375D, this.posZ);
			//this.setPosition(this.posX, this.posY - 0.9375D, this.posZ);
			//this.setPosition(this.posX, this.posY - 0.5625D, this.posZ);
		}

		return status;
	}

	/*private boolean isBlockTranslucent(int par1, int par2, int par3)
    {
        return this.worldObj.isBlockNormalCube(par1, par2, par3);
    }

    protected boolean pushOutOfBlocks(double par1, double par3, double par5)
    {
        int var7 = MathHelper.floor_double(par1);
        int var8 = MathHelper.floor_double(par3);
        int var9 = MathHelper.floor_double(par5);
        double var10 = par1 - (double)var7;
        double var12 = par5 - (double)var9;

        if (this.isBlockTranslucent(var7, var8, var9))
        {
            boolean var14 = !this.isBlockTranslucent(var7 - 1, var8, var9) && !this.isBlockTranslucent(var7 - 1, var8 + 1, var9);
            boolean var15 = !this.isBlockTranslucent(var7 + 1, var8, var9) && !this.isBlockTranslucent(var7 + 1, var8 + 1, var9);
            boolean var16 = !this.isBlockTranslucent(var7, var8, var9 - 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 - 1);
            boolean var17 = !this.isBlockTranslucent(var7, var8, var9 + 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 + 1);
            byte var18 = -1;
            double var19 = 9999.0D;

            if (var14 && var10 < var19)
            {
                var19 = var10;
                var18 = 0;
            }

            if (var15 && 1.0D - var10 < var19)
            {
                var19 = 1.0D - var10;
                var18 = 1;
            }

            if (var16 && var12 < var19)
            {
                var19 = var12;
                var18 = 4;
            }

            if (var17 && 1.0D - var12 < var19)
            {
                var19 = 1.0D - var12;
                var18 = 5;
            }

            float var21 = 0.1F;

            if (var18 == 0)
            {
                this.motionX = (double)(-var21);
            }

            if (var18 == 1)
            {
                this.motionX = (double)var21;
            }

            if (var18 == 4)
            {
                this.motionZ = (double)(-var21);
            }

            if (var18 == 5)
            {
                this.motionZ = (double)var21;
            }
        }

        return false;
    }*/

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.onUpdateSquid();
		System.out.println("Server:[x,y,z]=[" + String.format("%3f", this.posX) + ", " + String.format("%3f", this.posY) + ", " + String.format("%3f", this.posZ) + "]");
	}

	public void onUpdateSquid() {}

	@Override
	public void onEntityUpdate() {
		int air = this.getAir();
		super.onEntityUpdate();

		if (this.isEntityAlive() && !this.isInsideOfMaterial(Material.water) && !this.isPotionActive(Potion.waterBreathing) && !this.capabilities.disableDamage) {
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
	public void onDeath(DamageSource damage) {
		super.onDeath(damage);
	}

	@Override
	public float getCurrentPlayerStrVsBlock(Block block) {
		return super.getCurrentPlayerStrVsBlock(block) * (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this) ? 5.0F : 1.0F);
	}

	public float getCurrentPlayerStrVsBlock(Block block, int meta) {
		return super.getCurrentPlayerStrVsBlock(block, meta) * (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this) ? 5.0F : 1.0F);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.playerStatus.readStatus(nbttagcompound.getCompoundTag("URTS.status"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		NBTTagCompound statusNBT = new NBTTagCompound();
		this.playerStatus.writeStatus(statusNBT);
		nbttagcompound.setCompoundTag("URTS.status", statusNBT);
	}

}
