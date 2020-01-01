package ayamitsu0321.urtsquid.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SquidPlayerModel<T extends LivingEntity> extends PlayerModel<T> {

    public final RendererModel squidBody;
    public final RendererModel[] squidTentacles  = new RendererModel[8];

    public SquidPlayerModel() {
        this(1.0F, false);
    }

    public SquidPlayerModel(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
        // for squid texture size
        this.textureWidth = 64;
        this.textureHeight = 32;

        // from net.minecraft.client.renderer.entity.model.SquidModel.java
        int i = -16;
        this.squidBody = new RendererModel(this, 0, 0);
        this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
        this.squidBody.rotationPointY += 8.0F;

        for(int j = 0; j < this.squidTentacles.length; ++j) {
            this.squidTentacles[j] = new RendererModel(this, 48, 0);
            double d0 = (double)j * Math.PI * 2.0D / (double)this.squidTentacles.length;
            float f = (float)Math.cos(d0) * 5.0F;
            float f1 = (float)Math.sin(d0) * 5.0F;
            this.squidTentacles[j].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
            this.squidTentacles[j].rotationPointX = f;
            this.squidTentacles[j].rotationPointZ = f1;
            this.squidTentacles[j].rotationPointY = 15.0F;
            d0 = (double)j * Math.PI * -2.0D / (double)this.squidTentacles.length + (Math.PI / 2D);
            this.squidTentacles[j].rotateAngleY = (float)d0;
        }
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        for(RendererModel renderermodel : this.squidTentacles) {
            renderermodel.rotateAngleX = ageInTicks;
        }
    }

    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.squidBody.render(scale);

        for(RendererModel renderermodel : this.squidTentacles) {
            renderermodel.render(scale);
        }
    }

    @Override
    public void setVisible(boolean visibility) {
        super.setVisible(visibility);
        // biped    if always true, item location makes bad
//        this.bipedHead.showModel = false;
//        this.bipedHeadwear.showModel = false;
//        this.bipedBody.showModel = false;
//        this.bipedRightArm.showModel = false;
//        this.bipedLeftArm.showModel = false;
//        this.bipedRightLeg.showModel = false;
//        this.bipedLeftLeg.showModel = false;
        // armar
        this.bipedLeftArmwear.showModel = false;
        this.bipedRightArmwear.showModel = false;
        this.bipedLeftLegwear.showModel = false;
        this.bipedRightLegwear.showModel = false;
        this.bipedBodyWear.showModel = false;
        // squid
        this.squidBody.showModel = visibility;
        for(RendererModel renderermodel : this.squidTentacles) {
            renderermodel.showModel = visibility;
        }
    }

}
