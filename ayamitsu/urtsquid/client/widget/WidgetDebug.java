package ayamitsu.urtsquid.client.widget;

import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.player.EntityPlayerSquidSP;
import cpw.mods.fml.client.FMLClientHandler;

public class WidgetDebug extends Widget {

	@Override
	public void draw(int width, int height, float partialTickTime) {
		this.getFontRenderer().drawString("air:" + Integer.toString(FMLClientHandler.instance().getClient().thePlayer.getAir()), 1, 1, 0xeeeeee);
		this.getFontRenderer().drawString("parasite:" + Boolean.toString(this.getPlayer().playerStatus.isParasiteStat()), 1, 11, 0xeeeeee);
	}

	private EntityPlayerSquidSP getPlayer() {
		return (EntityPlayerSquidSP)FMLClientHandler.instance().getClient().thePlayer;
	}

}
