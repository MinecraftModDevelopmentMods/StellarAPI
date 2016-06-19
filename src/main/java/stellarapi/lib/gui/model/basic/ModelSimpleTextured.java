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

public class ModelSimpleTextured implements IRenderModel {

	private ResourceLocation location;

	public ModelSimpleTextured(ResourceLocation location) {
		this.location = location;
	}

	@Override
	public void renderModel(String info, IRectangleBound totalBound, IRectangleBound clipBound, Tessellator tessellator,
			VertexBuffer worldRenderer, TextureManager textureManager, float[] color) {
		float leftX = clipBound.getLeftX();
		float upY = clipBound.getUpY();
		float rightX = clipBound.getRightX();
		float downY = clipBound.getDownY();

		float minU = totalBound.getRatioX(leftX);
		float minV = totalBound.getRatioY(upY);
		float maxU = totalBound.getRatioX(rightX);
		float maxV = totalBound.getRatioY(downY);

		GlStateManager.color(color[0], color[1], color[2], color[3]);

		textureManager.bindTexture(this.location);
		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos((double) (leftX), (double) (downY), 0.0).tex(minU, maxV).endVertex();
		worldRenderer.pos((double) (rightX), (double) (downY), 0.0).tex(maxU, maxV).endVertex();
		worldRenderer.pos((double) (rightX), (double) (upY), 0.0).tex(maxU, minV).endVertex();
		worldRenderer.pos((double) (leftX), (double) (upY), 0.0).tex(minU, minV).endVertex();
		tessellator.draw();
	}

}
