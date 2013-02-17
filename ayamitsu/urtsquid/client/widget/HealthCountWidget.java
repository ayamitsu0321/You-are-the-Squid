package ayamitsu.urtsquid.client.widget;

import ayamitsu.urtsquid.URTSquid;

public class HealthCountWidget extends Widget {

	@Override
	public void draw(int width, int height, float partialTickTime) {
		this.getFontRenderer().drawString("x" + (URTSquid.instance.playerStatus.getMaxHeartCount() - URTSquid.instance.playerStatus.getHeartCount()), width / 2 - 6, height - 39, 0xffffff);
		//(var28, var29, var26 + 36, 9 * var30, 9, 9);
	}

}
