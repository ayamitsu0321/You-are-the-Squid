package ayamitsu.urtsquid;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = URTSquid.MODID,
        name = URTSquid.NAME,
        version = URTSquid.VERSION
)
public class URTSquid {

    public static final String MODID = "urtsquid";
    public static final String NAME = "URTSquid";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static URTSquid instance;

    @SidedProxy(clientSide = "ayamitsu.urtsquid.client.ClientProxy", serverSide = "ayamitsu.urtsquid.server.ServerProxy")
    public static AbstractProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

}