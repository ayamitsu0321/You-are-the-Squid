package ayamitsu0321.urtsquid.client.renderer.entity;

import ayamitsu0321.urtsquid.URTSquid;
import ayamitsu0321.urtsquid.client.renderer.entity.model.SquidPlayerModel;
import ayamitsu0321.urtsquid.entity.player.ISquidPlayerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
    protected void applyRotations(ISquidPlayerEntity entityLiving, MatrixStack p_225621_2_, float ageInTicks, float rotationYaw, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, entityLiving.getPrevSquidPitch(), entityLiving.getSquidPitch());
        float f1 = MathHelper.lerp(partialTicks, entityLiving.getPrevSquidYaw(), entityLiving.getSquidYaw());
        p_225621_2_.translate(0.0F, 0.5F, 0.0F);
        p_225621_2_.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        p_225621_2_.rotate(Vector3f.XP.rotationDegrees(f));
        p_225621_2_.rotate(Vector3f.YP.rotationDegrees(f1));
        p_225621_2_.translate(0.0D, -1.2D, 0.0D);
    }

    @Override
    protected void applyRotations(AbstractClientPlayerEntity entityLiving, MatrixStack p_225621_2_, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving instanceof ISquidPlayerEntity) {
            // squid player
            this.applyRotations((ISquidPlayerEntity)entityLiving, p_225621_2_, ageInTicks, rotationYaw, partialTicks);
        } else {
            // default player
            super.applyRotations(entityLiving, p_225621_2_, ageInTicks, rotationYaw, partialTicks);
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
    public void renderRightArm(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn) {
        SquidPlayerModel<AbstractClientPlayerEntity> playermodel = (SquidPlayerModel<AbstractClientPlayerEntity>)this.getEntityModel();
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, playermodel.squidTentacles[0], playermodel.bipedRightArmwear);
    }

    @Override
    public void renderLeftArm(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn) {
        SquidPlayerModel<AbstractClientPlayerEntity> playermodel = (SquidPlayerModel<AbstractClientPlayerEntity>)this.getEntityModel();
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, playermodel.squidTentacles[playermodel.squidTentacles.length / 2], playermodel.bipedLeftArmwear);
    }

    /**
     * from net.minecraft.client.renderer.entity.PlayerRenderer.java#renderItem
     */
    private void renderItem(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, ModelRenderer rendererArmIn, ModelRenderer rendererArmwearIn) {
        PlayerModel<AbstractClientPlayerEntity> playermodel = this.getEntityModel();
        this.setModelVisibilities(playerIn);
        playermodel.swingProgress = 0.0F;
        playermodel.isSneak = false;
        playermodel.swimAnimation = 0.0F;
        playermodel.setRotationAngles(playerIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        rendererArmIn.rotateAngleX = 0.0F;
        rendererArmIn.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntitySolid(playerIn.getLocationSkin())), combinedLightIn, OverlayTexture.NO_OVERLAY);
        //rendererArmwearIn.rotateAngleX = 0.0F;
        //rendererArmwearIn.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucent(playerIn.getLocationSkin())), combinedLightIn, OverlayTexture.NO_OVERLAY);
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
            playermodel.isSneak = clientPlayer.isCrouching();
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
    public BipedModel.ArmPose getArmPose(AbstractClientPlayerEntity clientPlayer, ItemStack mainHandItem, ItemStack offHandItem, Hand handType) {
        BipedModel.ArmPose armPose = BipedModel.ArmPose.EMPTY;
        try {
            // changed modifier with AccessTransformer(private -> public)
            //armPose = super.getArmPose(clientPlayer, mainHandItem, offHandItem, handType);
            armPose = func_241741_a_(clientPlayer, handType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return armPose;
    }

}
