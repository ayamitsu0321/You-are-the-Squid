package ayamitsu0321.urtsquid;

import ayamitsu0321.urtsquid.client.renderer.entity.SquidPlayerRenderer;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map;

@Mod(URTSquid.MODID)
public class URTSquid {

    public static final String MODID = "urtsquid";

    /** squid size */
    public static final EntitySize STANDING_SIZE = EntitySize.flexible(0.8F, 0.6F);
    public static final EntitySize SLEEPING_SIZE = EntitySize.fixed(0.2F, 0.2F);
    public static final Map<Pose, EntitySize> SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.SLEEPING, SLEEPING_SIZE).put(Pose.FALL_FLYING, EntitySize.flexible(0.3F, 0.3F)).put(Pose.SWIMMING, EntitySize.flexible(0.3F, 0.3F)).put(Pose.SPIN_ATTACK, EntitySize.flexible(0.3F, 0.3F)).put(Pose.CROUCHING, EntitySize.flexible(0.6F, 0.4F)).put(Pose.DYING, EntitySize.fixed(0.2F, 0.2F)).build();

    public URTSquid() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityType.PLAYER, new IRenderFactory<PlayerEntity>() {
            @Override
            public EntityRenderer<? super PlayerEntity> createRenderFor(EntityRendererManager manager) {
                PlayerRenderer defaultRenderer = new SquidPlayerRenderer(manager);
                PlayerRenderer slimRenderer = new SquidPlayerRenderer(manager, true);

                // playerRenderer - field_178637_m
                ObfuscationReflectionHelper.setPrivateValue(EntityRendererManager.class, manager, defaultRenderer, "field_178637_m");
                // skinMap - field_178636_l
                Map<String, PlayerRenderer> skinMap = ObfuscationReflectionHelper.getPrivateValue(EntityRendererManager.class, manager, "field_178636_l");
                skinMap.put("default", defaultRenderer);
                skinMap.put("slim", slimRenderer);

                return null;
            }
        });
    }
}
