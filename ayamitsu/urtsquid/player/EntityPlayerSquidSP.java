package ayamitsu.urtsquid.player;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Session;
import net.minecraft.world.World;

public class EntityPlayerSquidSP extends EntityClientPlayerMP {

	private int air;
	private int prevAir;

	public EntityPlayerSquidSP(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4NetClientHandler) {
		super(par1Minecraft, par2World, par3Session, par4NetClientHandler);
		System.out.println("Spawn Override Player SP");
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
	public void setAir(int i) {
		this.air = i;
	}

}
