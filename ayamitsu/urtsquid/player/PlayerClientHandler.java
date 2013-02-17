package ayamitsu.urtsquid.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.PlayerBase;

public class PlayerClientHandler extends PlayerBase {

	public PlayerClientHandler(PlayerAPI api) {
		super(api);
		System.out.println(api.isGetCurrentPlayerStrVsBlockModded);
	}

	@Override
	public float getCurrentPlayerStrVsBlock(Block block) {
		System.out.println("hoge");
		float var0 = this.player.localGetCurrentPlayerStrVsBlock(block);

		if (this.player.isInsideOfMaterial(Material.water)) {
			var0 *= 5.0F;
		}

		return var0;
	}

}
