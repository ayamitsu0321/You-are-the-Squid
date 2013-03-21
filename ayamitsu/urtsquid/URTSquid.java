package ayamitsu.urtsquid;

import ayamitsu.urtsquid.network.PacketHandler;
import ayamitsu.urtsquid.player.PlayerStatus;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(
	modid = "URTSquid",
	name = "URTSquid",
	version = "0.1.2"
)
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false,
	packetHandler = ayamitsu.urtsquid.network.PacketHandler.class,
	connectionHandler = ayamitsu.urtsquid.network.ConnectionHandler.class,
	channels = { PacketHandler.KEY_CHANNEL, PacketHandler.STATUS_CHANNEL, PacketHandler.PARASITE_CHANNEL }
)
public class URTSquid {

	@Mod.Instance("URTSquid")
	public static URTSquid instance;

	@SidedProxy(clientSide = "ayamitsu.urtsquid.ClientProxy", serverSide = "ayamitsu.urtsquid.CommonProxy")
	public static CommonProxy proxy;

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Mod.Init
	public void init(FMLInitializationEvent event) {
		this.proxy.load();
	}

}