package stellarapi.api.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.IRenderHandler;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.instruments.IDetector;
import stellarapi.api.viewer.IPlayerView;

/**
 * Sky renderer by eye.
 * */
public class SkyRendererByEye extends IRenderHandler {

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		// TODO SkyRenderer Fill in this renderer
		EntityPlayer player = mc.player;
		IPlayerView view = player.getCapability(SAPICapabilities.PLAYER_VIEW, null);
		IDetector detector = view.getEyeDetector();
		
	}

}
