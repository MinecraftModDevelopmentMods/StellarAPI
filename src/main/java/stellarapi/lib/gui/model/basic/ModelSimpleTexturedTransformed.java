package stellarapi.lib.gui.model.basic;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderModel;

/**
 * Simple textured model which is reflected horizontally.
 * */
public class ModelSimpleTexturedTransformed implements IRenderModel {
	
	private ResourceLocation location;
	private SimpleBoundTransformer transformer = new SimpleBoundTransformer();

	public ModelSimpleTexturedTransformed(ResourceLocation location) {
		this.location = location;
	}
	
	public void setTransformer(SimpleBoundTransformer newTransformer) {
		transformer.reset(newTransformer);
	}

	@Override
	public void renderModel(String info, IRectangleBound totalBound, IRectangleBound clipBound, Tessellator tessellator,
			TextureManager textureManager, float[] color) {
		float leftX = clipBound.getLeftX();
		float upY = clipBound.getUpY();
		float rightX = clipBound.getRightX();
		float downY = clipBound.getDownY();
		
		transformer.setBound(totalBound.getRatioX(leftX),
				totalBound.getRatioY(upY),
				totalBound.getRatioX(rightX),
				totalBound.getRatioY(downY));
		
		float minU = transformer.transformLeft();
		float minV = transformer.transformUp();
		float maxU = transformer.transformRight();
		float maxV = transformer.transformDown();
		
		GL11.glColor4f(color[0], color[1], color[2], color[3]);
		
		textureManager.bindTexture(this.location);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(leftX), (double)(downY), 0.0, minU, maxV);
        tessellator.addVertexWithUV((double)(rightX), (double)(downY), 0.0, maxU, maxV);
        tessellator.addVertexWithUV((double)(rightX), (double)(upY), 0.0, maxU, minV);
        tessellator.addVertexWithUV((double)(leftX), (double)(upY), 0.0, minU, minV);
        tessellator.draw();
	}

}
