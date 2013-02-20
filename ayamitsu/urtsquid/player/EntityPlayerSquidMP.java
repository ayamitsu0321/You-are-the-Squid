package ayamitsu.urtsquid.player;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.network.PacketHandler;

public class EntityPlayerSquidMP extends EntityPlayerMP {

	protected int air;
	protected int prevAir;

	public EntityPlayerSquidMP(MinecraftServer mcServer, World world, String username, ItemInWorldManager itemInWorldManager) {
		super(mcServer, world, username, itemInWorldManager);
		System.out.println("Spawn Override Player MP:" + username);
	}

	@Override
	public void onUpdate() {
		this.prevAir = this.getAir();
		super.onUpdate();

		if (this.isEntityAlive() && !this.isInsideOfMaterial(Material.water) && !this.isPotionActive(Potion.waterBreathing) && !this.capabilities.disableDamage) {
			this.setAir(this.decreaseAirSupply(this.prevAir));

			if (this.getAir() == -20) {
				this.setAir(0);
				this.attackEntityFrom(DamageSource.drown, 2);
			}
		} else if (this.isInsideOfMaterial(Material.water)) {
			this.setAir(300);
		}

		super.setAir(this.air);
	}

	@Override
	public void onDeath(DamageSource damage) {
		if (URTSquid.instance.playerStatus.getHeartCount() < URTSquid.instance.playerStatus.getMaxHeartCount() - 1) {
			URTSquid.instance.playerStatus.setHeartCount((byte)(1 + URTSquid.instance.playerStatus.getHeartCount()));
			this.setEntityHealth(this.getMaxHealth());
		} else {
			URTSquid.instance.playerStatus.setHeartCount((byte)0);
			this.setAir(300);
			super.onDeath(damage);
		}

		PacketHandler.sendStatusToClient(this);
	}

	@Override
	public void setAir(int i) {
		this.air = i;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		URTSquid.instance.playerStatus.readStatus(nbttagcompound.getCompoundTag("URTS.status"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		NBTTagCompound statusNBT = new NBTTagCompound();
		URTSquid.instance.playerStatus.writeStatus(statusNBT);
		nbttagcompound.setCompoundTag("URTS.status", statusNBT);
	}

}
