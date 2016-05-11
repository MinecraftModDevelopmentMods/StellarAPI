package stellarapi.lib.gui.basicmodel;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import stellarapi.lib.gui.IFontHelper;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderModel;

public class ModelFont implements IRenderModel, IFontHelper {
	
	private final Matrix4f matrix;
	
	private WrappedFontRenderer font;
	private boolean shaded;
	private boolean centered;
	
	public ModelFont(boolean shaded, boolean centered) {
		this.font = new WrappedFontRenderer();
		this.shaded = shaded;
		this.centered = centered;
		this.matrix = new Matrix4f();
	}

	@Override
	public void renderModel(String info, IRectangleBound totalBound, IRectangleBound clipBound, Tessellator tessellator,
			TextureManager textureManager, float[] color) {
		float offset = this.centered? (totalBound.getWidth() - font.getStringWidth(info))*0.5f : 0.0f;
		
		float left = clipBound.getLeftX() - totalBound.getLeftX() - offset;
		float right = totalBound.getRightX() - clipBound.getRightX() + offset;
		float width = totalBound.getWidth();
		float yPos = totalBound.getMainY(0.5f) - font.FONT_HEIGHT * 0.5f;
		
		info = font.trimStringToWidth(info, (int)(width - right), false);
		info = font.trimStringToWidth(info, (int)(width - left - right), true);
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		FloatBuffer params = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, params);
        matrix.load (params);
		
		float leftX = (matrix.m00 * clipBound.getLeftX() + matrix.m30)* res.getScaleFactor();
		float upY = mc.displayHeight - (matrix.m11 * clipBound.getDownY() + matrix.m31) * res.getScaleFactor();
		float widthX = matrix.m00 * clipBound.getWidth() * res.getScaleFactor();
		float heightY = matrix.m11 * clipBound.getHeight() * res.getScaleFactor();
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor((int)leftX,
				(int)upY,
				(int)widthX,
				(int)heightY);

		font.setColorRGBA(color[0], color[1], color[2], color[3]);
		if(!this.shaded)
			font.drawString(info, (int)(clipBound.getLeftX()+offset), (int)yPos, 0xffffffff);
		else font.drawStringWithShadow(info, (int)(clipBound.getLeftX()+offset), (int)yPos, 0xffffffff);
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	
	@Override
	public float getStringWidth(String string) {
		return font.getStringWidth(string);
	}

	@Override
	public float getStringHeight() {
		return font.FONT_HEIGHT;
	}

	@Override
	public String trimStringToWidth(String toTrim, float width) {
		return font.trimStringToWidth(toTrim, (int)width);
	}

	@Override
	public String trimStringToWidth(String toTrim, float width, boolean fromEnd) {
		return font.trimStringToWidth(toTrim, (int)width, fromEnd);
	}
}
