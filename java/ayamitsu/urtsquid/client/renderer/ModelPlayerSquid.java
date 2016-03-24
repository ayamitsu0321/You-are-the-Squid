package ayamitsu.urtsquid.client.renderer;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * copy from ModelSquid
 */
public class ModelPlayerSquid extends ModelPlayer {

    public ModelRenderer squidBody;
    public final ModelRenderer[] squidTentacles = new ModelRenderer[8];

    public ModelPlayerSquid() {
        this(1.0F, false);
    }

    public ModelPlayerSquid(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
        this.textureWidth = 64;
        this.textureHeight = 32;

        int i = -16;
        this.squidBody = new ModelRenderer(this, 0, 0);
        this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
        this.squidBody.rotationPointY += (float)(24 + i);

        for (int j = 0; j < this.squidTentacles.length; ++j) {
            this.squidTentacles[j] = new ModelRenderer(this, 48, 0);
            double d0 = (double)j * Math.PI * 2.0D / (double)this.squidTentacles.length;
            float f = (float)Math.cos(d0) * 5.0F;
            float f1 = (float)Math.sin(d0) * 5.0F;
            this.squidTentacles[j].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
            this.squidTentacles[j].rotationPointX = f;
            this.squidTentacles[j].rotationPointZ = f1;
            this.squidTentacles[j].rotationPointY = (float)(31 + i);
            d0 = (double)j * Math.PI * -2.0D / (double)this.squidTentacles.length + (Math.PI / 2D);
            this.squidTentacles[j].rotateAngleY = (float)d0;
        }
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        for (ModelRenderer modelrenderer : this.squidTentacles) {
            modelrenderer.rotateAngleX = ageInTicks;
        }
    }

    public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(p_78088_2_, limbSwing, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.squidBody.render(scale);

        for (int i = 0; i < this.squidTentacles.length; ++i) {
            this.squidTentacles[i].render(scale);
        }
    }

}
