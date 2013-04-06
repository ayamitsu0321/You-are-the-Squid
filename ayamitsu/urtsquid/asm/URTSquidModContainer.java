package ayamitsu.urtsquid.asm;

import java.util.Arrays;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

public class URTSquidModContainer extends DummyModContainer {

	public URTSquidModContainer() {
		super(new ModMetadata());
		ModMetadata meta = this.getMetadata();
		meta.modId       = "urtsquidplugin";
		meta.name        = "URTSquidPlugin";
		meta.version     = "0.1.3";
		meta.authorList  = Arrays.asList("ayamitsu");
		meta.description = "";
		meta.url         = "";
		meta.credits     = "";
	}

}
