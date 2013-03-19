package ayamitsu.urtsquid.client.widget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.FMLClientHandler;

public abstract class Widget {

	protected int zLevel = 0;

	public abstract void draw(int width, int height, float partialTickTime);

	public boolean isDead() {
		return false;
	}

	protected void bindTexture(String path) {
		RenderEngine renderEngine = FMLClientHandler.instance().getClient().renderEngine;

		if (renderEngine != null) {
			//renderEngine.bindTexture(renderEngine.getTexture(path));
			renderEngine.func_98187_b(path);
		}
	}

	protected FontRenderer getFontRenderer() {
		return FMLClientHandler.instance().getClient().fontRenderer;
	}

	/**
	 * for x256
	 */
	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
		this.drawTexturedModalRect(x, y, u, v, width, height, 256, 256);
	}

	public void drawTexturedModalRect(int x, int y, int u, int v, int drawWidth, int drawHeight, int texWidth, int texHeight) {
		float var1 = 1.0F / texWidth;
		float var2 = 1.0F / texHeight;
		Tessellator var3 = Tessellator.instance;
		var3.startDrawingQuads();
		var3.addVertexWithUV((double)(x + 0), (double)(y + drawHeight), (double)this.zLevel, (double)((float)(u + 0) * var1), (double)((float)(v + drawHeight) * var2));
		var3.addVertexWithUV((double)(x + drawWidth), (double)(y + drawHeight), (double)this.zLevel, (double)((float)(u + drawWidth) * var1), (double)((float)(v + drawHeight) * var2));
		var3.addVertexWithUV((double)(x + drawWidth), (double)(y + 0), (double)this.zLevel, (double)((float)(u + drawWidth) * var1), (double)((float)(v + 0) * var2));
		var3.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(u + 0) * var1), (double)((float)(v + 0) * var2));
		var3.draw();
	}

}
