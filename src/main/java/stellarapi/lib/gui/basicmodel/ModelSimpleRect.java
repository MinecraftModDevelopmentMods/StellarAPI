package stellarapi.lib.gui.basicmodel;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderModel;

public class ModelSimpleRect implements IRenderModel {
	
	private static final ModelSimpleRect instance = new ModelSimpleRect();
	
	public static ModelSimpleRect getInstance() {
		return instance;
	}
	
	@Override
	public void renderModel(String info, IRectangleBound totalBound, IRectangleBound clipBound, Tessellator tessellator,
			TextureManager textureManager, float[] color) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float posX = clipBound.getLeftX();
		float posY = clipBound.getUpY();
		float width = clipBound.getWidth();
		float height = clipBound.getHeight();
		
		GL11.glColor4f(color[0], color[1], color[2], color[3]);
		
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)(posX), (double)(posY + height), 0.0);
        tessellator.addVertex((double)(posX + width), (double)(posY + height), 0.0);
        tessellator.addVertex((double)(posX + width), (double)(posY), 0.0);
        tessellator.addVertex((double)(posX), (double)(posY), 0.0);
        tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

}
