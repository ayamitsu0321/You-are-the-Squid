package ayamitsu0321.urtsquid.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class SquidPlayerModel<T extends LivingEntity> extends PlayerModel<T> {

    public final ModelRenderer squidBody;
    public final ModelRenderer[] squidTentacles  = new ModelRenderer[8];
    private final ImmutableList<ModelRenderer> modelList;

    public SquidPlayerModel() {
        this(1.0F, false);
    }

    public SquidPlayerModel(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
        // for squid texture size
        this.textureWidth = 64;
        this.textureHeight = 32;

        // from net.minecraft.client.renderer.entity.model.SquidModel.java
        this.squidBody = new ModelRenderer(this, 0, 0);
        this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
        this.squidBody.rotationPointY += 8.0F;

        for(int j = 0; j < this.squidTentacles.length; ++j) {
            this.squidTentacles[j] = new ModelRenderer(this, 48, 0);
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

        // list of models
        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.squidBody);
        builder.addAll(Arrays.asList(this.squidTentacles));
        modelList = builder.build();
    }

    // setRotationAngles
    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        for(ModelRenderer renderermodel : this.squidTentacles) {
            renderermodel.rotateAngleX = ageInTicks;
        }
    }

    @Override
    public void setVisible(boolean visibility) {
        // if biped model always true, item location makes bad
        // hide super model
        super.setVisible(false);
        // squid
        this.squidBody.showModel = visibility;
        for(ModelRenderer renderermodel : this.squidTentacles) {
            renderermodel.showModel = visibility;
        }
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return this.modelList;
    }
}
