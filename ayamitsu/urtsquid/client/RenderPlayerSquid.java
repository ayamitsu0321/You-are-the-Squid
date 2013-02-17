package ayamitsu.urtsquid.client;

import ayamitsu.urtsquid.util.Reflector;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

// TODO:override armors
public class RenderPlayerSquid extends RenderPlayer {

	public RenderPlayerSquid() {
		super();
		this.initRender();
	}

	protected void initRender() {
		this.mainModel = new ModelPlayerSquid();
		this.setRenderPassModel((ModelBase)null);// for armor
		// clear armors
	}

	@Override
	protected int setArmorModel(EntityPlayer par1EntityPlayer, int par2, float par3) {
		return -1;
	}

	@Override
	public void func_82441_a(EntityPlayer par1EntityPlayer) {
		this.renderArm(par1EntityPlayer);
	}

	protected void renderArm(EntityPlayer player) {

	}
}
