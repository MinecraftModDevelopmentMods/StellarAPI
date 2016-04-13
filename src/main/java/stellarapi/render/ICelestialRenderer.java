package stellarapi.render;

import net.minecraft.client.Minecraft;
import stellarapi.util.math.SpCoord;

public interface ICelestialRenderer {
	public void renderSphere(Minecraft mc, float partialTicks);
	public void renderForDirection(Minecraft mc, float partialTicks, SpCoord viewPos, float radius);
}
