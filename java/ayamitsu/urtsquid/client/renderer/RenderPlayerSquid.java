package ayamitsu.urtsquid.client.renderer;

import ayamitsu.urtsquid.player.EntityOtherPlayerSquid;
import ayamitsu.urtsquid.player.EntityPlayerSquidSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.util.ResourceLocation;

/**
 * Created by ayamitsu0321 on 2016/03/20.
 */
public class RenderPlayerSquid extends RenderPlayer {

    public static final ResourceLocation squidTextures = new ResourceLocation("urtsquid", "textures/entity/squid.png");
    protected ModelPlayerSquid squidModel;

    public RenderPlayerSquid(RenderManager renderManager) {
        super(renderManager);
        this.mainModel = this.squidModel = new ModelPlayerSquid();
        this.clearLayerRenderers();
        //this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        //this.addLayer(new LayerDeadmau5Head(this));
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        this.addLayer(new LayerElytra(this));
    }

    public RenderPlayerSquid(RenderManager renderManager, boolean useSmallArms) {
        super(renderManager, useSmallArms);
        this.mainModel = this.squidModel = new ModelPlayerSquid();
        this.clearLayerRenderers();
        //this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        //this.addLayer(new LayerDeadmau5Head(this));
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        this.addLayer(new LayerElytra(this));
    }

    /*protected ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
        return squidTextures;//entity.getLocationSkin();
    }*/

    @Override
    protected void rotateCorpse(AbstractClientPlayer player, float p_77043_2_, float p_77043_3_, float partialTicks) {
        if (player.isEntityAlive() && player.isPlayerSleeping())
        {
            GlStateManager.rotate(player.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);//
            GlStateManager.rotate(this.getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0F, 0.0F);
        } else {
            if ((player instanceof EntityPlayerSquidSP)) {
                EntityPlayerSquidSP squidPlayer = (EntityPlayerSquidSP)player;
                float f = squidPlayer.prevSquidPitch + (squidPlayer.squidPitch - squidPlayer.prevSquidPitch) * partialTicks;
                float f1 = squidPlayer.prevSquidYaw + (squidPlayer.squidYaw - squidPlayer.prevSquidYaw) * partialTicks;
                GlStateManager.translate(0.0F, 0.5F, 0.0F);
                GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, -1.2F, 0.0F);
            } else if ((player instanceof EntityOtherPlayerSquid)) {
                EntityOtherPlayerSquid squidPlayer = (EntityOtherPlayerSquid)player;
                float f = squidPlayer.prevSquidPitch + (squidPlayer.squidPitch - squidPlayer.prevSquidPitch) * partialTicks;
                float f1 = squidPlayer.prevSquidYaw + (squidPlayer.squidYaw - squidPlayer.prevSquidYaw) * partialTicks;
                GlStateManager.translate(0.0F, 0.5F, 0.0F);
                GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, -1.2F, 0.0F);
            }
        }
    }

    @Override
    protected float handleRotationFloat(AbstractClientPlayer livingBase, float partialTicks) {
        if ((livingBase instanceof EntityPlayerSquidSP)) {
            EntityPlayerSquidSP squidPlayer = (EntityPlayerSquidSP)livingBase;
            return squidPlayer.lastTentacleAngle + (squidPlayer.tentacleAngle - squidPlayer.lastTentacleAngle) * partialTicks;
        } else if ((livingBase instanceof EntityOtherPlayerSquid)) {
            EntityOtherPlayerSquid squidPlayer = (EntityOtherPlayerSquid)livingBase;
            return squidPlayer.lastTentacleAngle + (squidPlayer.tentacleAngle - squidPlayer.lastTentacleAngle) * partialTicks;
        } else {
            return super.handleRotationFloat(livingBase, partialTicks);
        }
    }

    @Override
    public void renderRightArm(AbstractClientPlayer clientPlayer) {
        float f = 1.0F;
        GlStateManager.color(f, f, f);
        float f1 = 0.0625F;// 1.0F / 16F

        GlStateManager.enableBlend();
        GlStateManager.translate(0.0F, 0.0F, 1.0F);
        this.squidModel.swingProgress = 0.0F;
        this.squidModel.isSneak = false;
        this.squidModel.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1, clientPlayer);
        this.squidModel.squidTentacles[0].render(f1);
        GlStateManager.disableBlend();
    }

    @Override
    public void renderLeftArm(AbstractClientPlayer clientPlayer) {
        float f = 1.0F;
        GlStateManager.color(f, f, f);
        float f1 = 0.0625F;// 1.0F / 16F

        GlStateManager.enableBlend();
        GlStateManager.translate(0.0F, 0.0F, 1.0F);
        this.squidModel.swingProgress = 0.0F;
        this.squidModel.isSneak = false;
        this.squidModel.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1, clientPlayer);
        this.squidModel.squidTentacles[this.squidModel.squidTentacles.length / 2].render(f1);
        GlStateManager.disableBlend();
    }


    protected void clearLayerRenderers() {
        this.layerRenderers.clear();
    }

}
