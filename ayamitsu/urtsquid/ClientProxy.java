package ayamitsu.urtsquid;

import net.minecraft.client.settings.KeyBinding;
import ayamitsu.urtsquid.client.KeyHandler;
import ayamitsu.urtsquid.client.StatusRenderer;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	@Override
	public void load() {
		TickRegistry.registerTickHandler(new StatusRenderer(), Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new KeyHandler(new KeyBinding[] { new KeyBinding("ToggleParasite", 19) }, new boolean[] { false }));// key R
	}

}
