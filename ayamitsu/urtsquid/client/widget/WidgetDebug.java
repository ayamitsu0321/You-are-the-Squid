package ayamitsu.urtsquid.client.widget;

import ayamitsu.urtsquid.URTSquid;
import cpw.mods.fml.client.FMLClientHandler;

public class WidgetDebug extends Widget {

	@Override
	public void draw(int width, int height, float partialTickTime) {
		this.getFontRenderer().drawString("air:" + Integer.toString(FMLClientHandler.instance().getClient().thePlayer.getAir()), 1, 1, 0xeeeeee);
		this.getFontRenderer().drawString("heart:" + Integer.toString(URTSquid.instance.playerStatus.getMaxHeartCount() - URTSquid.instance.playerStatus.getHeartCount()), 1, 11, 0xeeeeee);
		this.getFontRenderer().drawString("parasite:" + Boolean.toString(URTSquid.instance.playerStatus.isParasiteStat()), 1, 21, 0xeeeeee);
	}

}
