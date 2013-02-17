package ayamitsu.urtsquid.player;

import java.util.EnumSet;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PlayerTickHandler implements ITickHandler {

	protected int prevAir;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		for (Object obj : tickData) {
			if (obj instanceof EntityPlayer) {
				this.prevAir = ((EntityPlayer)obj).getAir();
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		for (Object obj : tickData) {
			if (obj instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP)obj;

				if (player.isEntityAlive() && !player.isInsideOfMaterial(Material.water) && !player.isPotionActive(Potion.waterBreathing) && !player.capabilities.disableDamage) {
					player.setAir(player.superDecreaseAirSupply(this.prevAir));

					if (player.getAir() == -20) {
						player.setAir(0);
						player.attackEntityFrom(DamageSource.drown, 2);
					}
				} else if (player.isInsideOfMaterial(Material.water)) {
					player.setAir(300);
				}
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "URTSquid.player";
	}

}
