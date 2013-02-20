package ayamitsu.urtsquid.client.widget;

import cpw.mods.fml.client.FMLClientHandler;
import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.player.EntityPlayerSquidSP;

public class WidgetHealthCount extends Widget {

	@Override
	public void draw(int width, int height, float partialTickTime) {
		String heartCount = "x" + Integer.toString(this.getPlayer().playerStatus.getMaxHeartCount() - this.getPlayer().playerStatus.getHeartCount());
		this.getFontRenderer().drawString(heartCount, width - this.getFontRenderer().getStringWidth(heartCount), height - 39, 0xffffff);
	}

	private EntityPlayerSquidSP getPlayer() {
		return (EntityPlayerSquidSP)FMLClientHandler.instance().getClient().thePlayer;
	}

}
