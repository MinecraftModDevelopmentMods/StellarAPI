package stellarapi.lib.gui.model.basic;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderModel;

/**
 * Simple textured model which is reflected horizontally.
 */
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
			VertexBuffer worldRenderer, TextureManager textureManager, float[] color) {
		float leftX = clipBound.getLeftX();
		float upY = clipBound.getUpY();
		float rightX = clipBound.getRightX();
		float downY = clipBound.getDownY();

		if (transformer.isRotated()) {
			transformer.setBound(totalBound.getRatioY(downY), totalBound.getRatioX(leftX), totalBound.getRatioY(upY),
					totalBound.getRatioX(rightX));
		} else {
			transformer.setBound(totalBound.getRatioX(leftX), totalBound.getRatioY(upY), totalBound.getRatioX(rightX),
					totalBound.getRatioY(downY));
		}

		float minU = transformer.transformLeft();
		float minV = transformer.transformUp();
		float maxU = transformer.transformRight();
		float maxV = transformer.transformDown();

		GlStateManager.color(color[0], color[1], color[2], color[3]);

		textureManager.bindTexture(this.location);
		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		if (transformer.isRotated()) {
			worldRenderer.pos((double) (leftX), (double) (downY), 0.0).tex(minV, maxU).endVertex();
			worldRenderer.pos((double) (rightX), (double) (downY), 0.0).tex(minV, minU).endVertex();
			worldRenderer.pos((double) (rightX), (double) (upY), 0.0).tex(maxV, minU).endVertex();
			worldRenderer.pos((double) (leftX), (double) (upY), 0.0).tex(maxV, maxU).endVertex();
		} else {
			worldRenderer.pos((double) (leftX), (double) (downY), 0.0).tex(minU, maxV).endVertex();
			worldRenderer.pos((double) (rightX), (double) (downY), 0.0).tex(maxU, maxV).endVertex();
			worldRenderer.pos((double) (rightX), (double) (upY), 0.0).tex(maxU, minV).endVertex();
			worldRenderer.pos((double) (leftX), (double) (upY), 0.0).tex(minU, minV).endVertex();
		}
		tessellator.draw();
	}

}
