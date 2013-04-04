package ayamitsu.urtsquid.player;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.item.Item;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ayamitsu.urtsquid.api.BreathRecoveryItemAPI;
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
		this.yOffset = 0.0F;
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
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.onUpdateSquid();
		//System.out.println("Server:[x,y,z]=[" + String.format("%3f", this.posX) + ", " + String.format("%3f", this.posY) + ", " + String.format("%3f", this.posZ) + "]");
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
	public float getCurrentPlayerStrVsBlock(Block block, boolean flag) {
		return super.getCurrentPlayerStrVsBlock(block, flag) * (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this) ? 5.0F : 1.0F);
	}

	public float getCurrentPlayerStrVsBlock(Block block, boolean flag, int meta) {
		return super.getCurrentPlayerStrVsBlock(block, flag, meta) * (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this) ? 5.0F : 1.0F);
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

	protected ItemStack getItemInUseMod() {
		Field itemInUse = null;

		try {
			itemInUse = EntityPlayer.class.getDeclaredFields()[31];
		} catch (Exception e) {
			e.printStackTrace();
			itemInUse = null;
		}

		if (itemInUse != null) {
			itemInUse.setAccessible(true);

			try {
				return (ItemStack)itemInUse.get(this);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	protected void onItemUseFinish() {
		ItemStack prevItem = this.getItemInUseMod();
		super.onItemUseFinish();

		// water bottle
		if (prevItem != null && BreathRecoveryItemAPI.match(prevItem)) {
			int prevAir = this.getAir();

			if (prevAir + 100 < 300) {
				this.setAir(prevAir + 100);
			} else {
				this.setAir(300);
			}
		}
	}

}
