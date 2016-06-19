package stellarapi.lib.gui.model.basic;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderModel;

public class ModelSimpleRect implements IRenderModel {

	private static final ModelSimpleRect instance = new ModelSimpleRect();

	public static ModelSimpleRect getInstance() {
		return instance;
	}

	@Override
	public void renderModel(String info, IRectangleBound totalBound, IRectangleBound clipBound, Tessellator tessellator,
			VertexBuffer worldRenderer, TextureManager textureManager, float[] color) {
		GlStateManager.disableTexture2D();

		float posX = clipBound.getLeftX();
		float posY = clipBound.getUpY();
		float posRight = clipBound.getRightX();
		float posDown = clipBound.getDownY();

		GlStateManager.color(color[0], color[1], color[2], color[3]);

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos((double) posX, (double) posDown, 0.0D).endVertex();
		worldRenderer.pos((double) posRight, (double) posDown, 0.0D).endVertex();
		worldRenderer.pos((double) posRight, (double) posY, 0.0D).endVertex();
		worldRenderer.pos((double) posX, (double) posY, 0.0D).endVertex();
		tessellator.draw();

		GlStateManager.enableTexture2D();
	}

}
