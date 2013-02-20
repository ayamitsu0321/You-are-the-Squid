package ayamitsu.urtsquid.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions("ayamitsu.urtsquid.asm")
public class URTSquidCorePlugin implements IFMLLoadingPlugin {

	static File location;

	@Override
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			"ayamitsu.urtsquid.asm.TransformerServerConfigurationManager",
			"ayamitsu.urtsquid.asm.TransformerGuiIngame",
			"ayamitsu.urtsquid.asm.TransformerPlayerControllerMP"
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
		if (data.containsKey("coremodLocation")) {
			location = (File)data.get("coremodLocation");
		}
	}

}
