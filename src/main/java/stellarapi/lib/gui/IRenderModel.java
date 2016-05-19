package stellarapi.lib.gui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;

public interface IRenderModel {

	void renderModel(String info, IRectangleBound totalBound, IRectangleBound clipBound,
			Tessellator tessellator, WorldRenderer worldRenderer, TextureManager textureManager, float[] colors);

}
