package ayamitsu.urtsquid.client;

import ayamitsu.urtsquid.AbstractProxy;
import ayamitsu.urtsquid.client.renderer.RenderPlayerSquid;
import ayamitsu.urtsquid.player.EntityPlayerSquidSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Map;

/**
 * Created by ayamitsu0321 on 2016/03/19.
 */
public class ClientProxy extends AbstractProxy {

    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayerSquidSP.class, new IRenderFactory<EntityPlayerSquidSP>() {
            @Override
            public Render<? super EntityPlayerSquidSP> createRenderFor(RenderManager manager) {
                RenderPlayer renderPlayer = new RenderPlayerSquid(manager);
                ObfuscationReflectionHelper.setPrivateValue(RenderManager.class, manager, renderPlayer, "playerRenderer", "field_178637_m", "m");

                Map<String, RenderPlayer> skinMap = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, manager, "skinMap", "field_178636_l", "l");
                skinMap.put("default", renderPlayer);
                skinMap.put("slim", new RenderPlayerSquid(manager, true));

                return renderPlayer;
            }
        });
    }

}
