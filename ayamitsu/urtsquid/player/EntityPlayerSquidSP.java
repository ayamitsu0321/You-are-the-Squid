package ayamitsu.urtsquid.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.util.Session;
import net.minecraft.world.World;

public class EntityPlayerSquidSP extends EntityClientPlayerMP {

	public EntityPlayerSquidSP(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4NetClientHandler) {
		super(par1Minecraft, par2World, par3Session, par4NetClientHandler);
		System.out.println("Spawn Override Player SP");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

}
