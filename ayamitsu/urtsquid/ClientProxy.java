package ayamitsu.urtsquid;

import ayamitsu.urtsquid.client.StatusRenderer;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	@Override
	public void load() {
		TickRegistry.registerTickHandler(new StatusRenderer(), Side.CLIENT);
	}

}
