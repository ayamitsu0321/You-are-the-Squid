package ayamitsu.urtsquid.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Created by ayamitsu0321 on 2016/03/19.
 */
@IFMLLoadingPlugin.TransformerExclusions("ayamitsu.urtsquid.asm")
public class URTSquidCorePlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        String prefix = "ayamitsu.urtsquid.asm.transformer.";

        return new String[] {
                prefix + "TransformerGuiIngameForge",
                prefix + "TransformerPlayerList",
                prefix + "TransformerPlayerControllerMP",
                prefix + "TransformerEntityRenderer",
                prefix + "TransformerBlockBed",
                prefix + "TransformerNetHandlerPlayClient",
                prefix + "TransformerGuiInventory"
        };
    }

    @Override
    public String getModContainerClass() {
        return "ayamitsu.urtsquid.asm.URTSquidModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
