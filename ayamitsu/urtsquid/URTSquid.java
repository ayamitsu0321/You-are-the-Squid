package ayamitsu.urtsquid;

import net.minecraft.src.PlayerAPI;
import net.minecraft.src.ServerPlayerAPI;
import ayamitsu.urtsquid.network.PacketHandler;
import ayamitsu.urtsquid.player.PlayerClientHandler;
import ayamitsu.urtsquid.player.PlayerServerHandler;
import ayamitsu.urtsquid.player.PlayerStatus;
import ayamitsu.urtsquid.player.PlayerTickHandler;
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
	version = "0.0.1"
)
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false,
	packetHandler = ayamitsu.urtsquid.network.PacketHandler.class,
	connectionHandler = ayamitsu.urtsquid.network.ConnectionHandler.class,
	channels = { PacketHandler.KEY_CHANNEL, PacketHandler.STATUS_CHANNEL }
)
public class URTSquid {

	@Mod.Instance("URTSquid")
	public static URTSquid instance;

	@SidedProxy(clientSide = "ayamitsu.urtsquid.ClientProxy", serverSide = "ayamitsu.urtsquid.CommonProxy")
	public static CommonProxy proxy;

	public PlayerStatus playerStatus = new PlayerStatus();

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
		TickRegistry.registerTickHandler(new PlayerTickHandler(), Side.SERVER);
		ServerPlayerAPI.register("URTSquid.server", PlayerServerHandler.class);

		if (event.getSide() == Side.CLIENT) {
			PlayerAPI.register("URTSquid.client", PlayerClientHandler.class);
		}
	}

	@Mod.Init
	public void init(FMLInitializationEvent event) {
		this.proxy.load();
	}

}