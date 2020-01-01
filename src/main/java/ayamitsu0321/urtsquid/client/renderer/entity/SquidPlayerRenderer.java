package ayamitsu0321.urtsquid.client.renderer.entity;

import ayamitsu0321.urtsquid.URTSquid;
import ayamitsu0321.urtsquid.client.renderer.entity.model.SquidPlayerModel;
import ayamitsu0321.urtsquid.entity.player.ISquidPlayerEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SquidPlayerRenderer extends PlayerRenderer {

    public static final ResourceLocation SQUID_TEXTURE = new ResourceLocation(URTSquid.MODID, "textures/entity/squid.png");

    public SquidPlayerRenderer(EntityRendererManager rendererManager) {
        this(rendererManager, false);
    }

    public SquidPlayerRenderer(EntityRendererManager rendererManager, boolean useSmallArms) {
        super(rendererManager, useSmallArms);
        // override player model
        this.entityModel = new SquidPlayerModel<>();
        // remove biped armor layer
        this.layerRenderers.removeIf(BipedArmorLayer.class::isInstance);
    }

    /**
     * from:net.minecraft.client.renderer.entity.SquidRenderer.java#applyRotations
     * @param entityLiving
     * @param ageInTicks
     * @param rotationYaw
     * @param partialTicks
     */
    protected void applyRotations(ISquidPlayerEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, entityLiving.getPrevSquidPitch(), entityLiving.getSquidPitch());
        float f1 = MathHelper.lerp(partialTicks, entityLiving.getPrevSquidYaw(), entityLiving.getSquidYaw());
        GlStateManager.translatef(0.0F, 0.5F, 0.0F);
        GlStateManager.rotatef(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(f, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(f1, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(0.0F, -1.2F, 0.0F);
    }

    @Override
    protected void applyRotations(AbstractClientPlayerEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving instanceof ISquidPlayerEntity) {
            // squid player
            this.applyRotations((ISquidPlayerEntity)entityLiving, ageInTicks, rotationYaw, partialTicks);
        } else {
            // default player
            super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        }
    }

    /**
     * from:net.minecraft.client.renderer.entity.SquidRenderer.java#handleRotationFloat
     * @param entityLiving
     * @param partialTicks
     * @return
     */
    protected float handleRotationFloat(ISquidPlayerEntity entityLiving, float partialTicks) {
        return MathHelper.lerp(partialTicks, entityLiving.getLastTentacleAngle(), entityLiving.getTentacleAngle());
    }

    @Override
    protected float handleRotationFloat(AbstractClientPlayerEntity entityLiving, float partialTicks) {
        if (entityLiving instanceof ISquidPlayerEntity) {
            // squid player
            return this.handleRotationFloat((ISquidPlayerEntity)entityLiving, partialTicks);
        } else {
            // default player can't move tentacles
            //return super.handleRotationFloat(entityLiving, partialTicks);
            return 0.0F;
        }
    }

    @Override
    public void renderRightArm(AbstractClientPlayerEntity clientPlayer) {
        float f = 1.0F;
        float f1 = 0.0625F;// 1.0F / 16.0F

        GlStateManager.color3f(f, f, f);
        SquidPlayerModel<AbstractClientPlayerEntity> playermodel = (SquidPlayerModel<AbstractClientPlayerEntity>)this.getEntityModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        GlStateManager.translatef(0.0F, 0.0F, 1.0F);
        playermodel.swingProgress = 0.0F;
        playermodel.isSneak = false;
        playermodel.swimAnimation = 0.0F;
        playermodel.swingProgress = 0.0F;
        playermodel.isSneak = false;
        playermodel.setRotationAngles(clientPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1);
        playermodel.squidTentacles[0].render(f1);
        //this.squidModel.bipedRightArmwear.render(f1);
        GlStateManager.disableBlend();
    }

    @Override
    public void renderLeftArm(AbstractClientPlayerEntity clientPlayer) {
        float f = 1.0F;
        float f1 = 0.0625F;// 1.0F / 16.0F

        GlStateManager.color3f(f, f, f);
        SquidPlayerModel<AbstractClientPlayerEntity> playermodel = (SquidPlayerModel<AbstractClientPlayerEntity>)this.getEntityModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        GlStateManager.translatef(0.0F, 0.0F, 1.0F);
        playermodel.swingProgress = 0.0F;
        playermodel.isSneak = false;
        playermodel.swimAnimation = 0.0F;
        playermodel.swingProgress = 0.0F;
        playermodel.isSneak = false;
        playermodel.setRotationAngles(clientPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1);
        playermodel.squidTentacles[playermodel.squidTentacles.length / 2].render(f1);
        //this.squidModel.bipedRightArmwear.render(f1);
        GlStateManager.disableBlend();
    }

    /**
     * プレイヤーの可視性をEntityの状態から更新する
     * from:net.minecraft.client.renderer.entity.PlayerRenderer.java#setModelVisibilities
     * changed modifier with AccessTransformer(private -> protected)
     * @param clientPlayer
     */
    @Override
    protected void setModelVisibilities(AbstractClientPlayerEntity clientPlayer) {
        PlayerModel playermodel = (PlayerModel)this.getEntityModel();
        if (clientPlayer.isSpectator()) {
            playermodel.setVisible(false);
            //playermodel.bipedHead.showModel = true;
            //playermodel.bipedHeadwear.showModel = true;
        } else {
            ItemStack itemstack = clientPlayer.getHeldItemMainhand();
            ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
            playermodel.setVisible(true);
            //playermodel.bipedHeadwear.showModel = clientPlayer.isWearing(PlayerModelPart.HAT);
            //playermodel.bipedBodyWear.showModel = clientPlayer.isWearing(PlayerModelPart.JACKET);
            //playermodel.bipedLeftLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_PANTS_LEG);
            //playermodel.bipedRightLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_PANTS_LEG);
            //playermodel.bipedLeftArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_SLEEVE);
            //playermodel.bipedRightArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.isSneak = clientPlayer.shouldRenderSneaking();
            BipedModel.ArmPose bipedmodel$armpose = this.getArmPose(clientPlayer, itemstack, itemstack1, Hand.MAIN_HAND);
            BipedModel.ArmPose bipedmodel$armpose1 = this.getArmPose(clientPlayer, itemstack, itemstack1, Hand.OFF_HAND);
            if (clientPlayer.getPrimaryHand() == HandSide.RIGHT) {
                playermodel.rightArmPose = bipedmodel$armpose;
                playermodel.leftArmPose = bipedmodel$armpose1;
            } else {
                playermodel.rightArmPose = bipedmodel$armpose1;
                playermodel.leftArmPose = bipedmodel$armpose;
            }
        }
    }

    /**
     * 腕のポーズタイプを取得する
     * from:net.minecraft.client.renderer.entity.PlayerRenderer.java#func_217766_a
     * @param clientPlayer
     * @param mainHandItem
     * @param offHandItem
     * @param handType
     * @return 腕のポーズタイプ
     */
    private BipedModel.ArmPose getArmPose(AbstractClientPlayerEntity clientPlayer, ItemStack mainHandItem, ItemStack offHandItem, Hand handType) {
        BipedModel.ArmPose armPose = BipedModel.ArmPose.EMPTY;
        try {
            // changed modifier with AccessTransformer(private -> public)
            armPose = this.func_217766_a(clientPlayer, mainHandItem, offHandItem, handType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return armPose;
    }

}
