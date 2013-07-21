package ayamitsu.urtsquid.client;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ayamitsu.urtsquid.player.EntityPlayerSquidSP;

public class RenderPlayerSquid extends RenderPlayer {

	protected static final ResourceLocation TEXTURE_SQUID = new ResourceLocation("textures/entity/squid.png");

	public RenderPlayerSquid() {
		super();
		this.initRender();
	}

	protected void initRender() {
		this.mainModel = new ModelPlayerSquid();
		this.setRenderPassModel((ModelBase)null);
	}

	@Override
	protected int setArmorModel(AbstractClientPlayer par1AbstractClientPlayer, int par2, float par3) {
		return -1;
	}

	@Override
	protected ResourceLocation func_110775_a(Entity par1Entity) {
		return TEXTURE_SQUID;
	}

	@Override
	protected void rotatePlayer(AbstractClientPlayer player, float par2, float par3, float par4) {
		EntityPlayerSquidSP squid = (EntityPlayerSquidSP)player;

        if (player.isEntityAlive() && player.isPlayerSleeping())
        {
            GL11.glRotatef(player.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
        }
        else
        {
        	float var5 = squid.field_70862_e + (squid.field_70861_d - squid.field_70862_e) * par4;
            float var6 = squid.field_70860_g + (squid.field_70859_f - squid.field_70860_g) * par4;
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.2F, 0.0F);
        }
    }

	@Override
	public void renderFirstPersonArm(EntityPlayer par1EntityPlayer) {// func_82441_a
		this.renderArm(par1EntityPlayer);
	}

	protected void renderArm(EntityPlayer player) {
		float var2 = 1.0F;
        GL11.glColor3f(var2, var2, var2);
        this.mainModel.onGround = 0.0F;
        this.mainModel.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        GL11.glTranslatef(0.0F, 0.0F, 1.0F);
        ((ModelPlayerSquid)this.mainModel).squidTentacles[0].render(0.0625F);
	}

	protected float handleRotationFloat(EntityPlayerSquidSP par1EntitySquid, float par2) {
        return par1EntitySquid.lastTentacleAngle + (par1EntitySquid.tentacleAngle - par1EntitySquid.lastTentacleAngle) * par2;
    }

	@Override
    protected float handleRotationFloat(EntityLivingBase living, float par2) {
        return this.handleRotationFloat((EntityPlayerSquidSP)living, par2);
    }

}
