package ayamitsu.urtsquid.client.widget;

import ayamitsu.urtsquid.URTSquid;

public class WidgetHealthCount extends Widget {

	@Override
	public void draw(int width, int height, float partialTickTime) {
		String heartCount = "x" + Integer.toString(URTSquid.instance.playerStatus.getMaxHeartCount() - URTSquid.instance.playerStatus.getHeartCount());
		this.getFontRenderer().drawString(heartCount, width - this.getFontRenderer().getStringWidth(heartCount), height - 39, 0xffffff);
	}

}
