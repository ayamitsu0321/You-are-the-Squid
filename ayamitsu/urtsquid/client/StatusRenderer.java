package ayamitsu.urtsquid.client;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import ayamitsu.urtsquid.client.widget.WidgetDebug;
import ayamitsu.urtsquid.client.widget.WidgetHealthCount;
import ayamitsu.urtsquid.client.widget.Widget;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class StatusRenderer implements ITickHandler {

	Deque<Widget> widgetList = new ArrayDeque<Widget>();

	public StatusRenderer() {
		this.widgetList.add(new WidgetHealthCount());
		this.widgetList.add(new WidgetDebug());
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		Minecraft mc = FMLClientHandler.instance().getClient();

		if (mc.theWorld == null/* || mc.isGamePaused*/) {
			return;
		}

		float partialTickTime = 0.0F;

		for (Object obj : tickData) {
			if (obj instanceof Float) {
				partialTickTime = ((Float)obj).floatValue();
			}
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -0.5F);

		int width = mc.isFullScreen() ? mc.displayWidth / 3 : mc.displayWidth / 2;
		int height = mc.isFullScreen() ? mc.displayHeight / 3 : mc.displayHeight / 2;

		for (Iterator<Widget> iterator = this.widgetList.iterator(); iterator.hasNext();) {
			Widget widget = iterator.next();

			if (!widget.isDead()) {
				widget.draw(width, height, partialTickTime);
			} else {
				iterator.remove();
			}
		}

		GL11.glTranslatef(0.0F, 0.0F, 0.5F);
		GL11.glPopMatrix();
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);// mc.entityRenderer.updateCameraAndRender
	}

	@Override
	public String getLabel() {
		return "URTSquid.status";
	}

}
