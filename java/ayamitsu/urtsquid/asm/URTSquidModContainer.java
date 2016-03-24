package ayamitsu.urtsquid.asm;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Arrays;

/**
 * Created by ayamitsu0321 on 2016/03/19.
 */
public class URTSquidModContainer extends DummyModContainer {

    public URTSquidModContainer() {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "urtsquidplugin";
        meta.name = "URTSquidPlugin";
        meta.version = "1.0.0";
        meta.authorList = Arrays.asList("ayamitsu");
        meta.description = "";
        meta.url = "";
        meta.credits = "";
    }

}
