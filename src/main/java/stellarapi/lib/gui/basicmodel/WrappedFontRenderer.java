package stellarapi.lib.gui.basicmodel;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class WrappedFontRenderer extends FontRenderer {
	
	private float redWrapped, greenWrapped, blueWrapped, alphaWrapped;
	
	public WrappedFontRenderer() {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
				Minecraft.getMinecraft().renderEngine, false);
		this.onResourceManagerReload(null);
	}
	
	public void setColorRGBA(float red, float green, float blue, float alpha) {
		this.redWrapped = red;
		this.greenWrapped = green;
		this.blueWrapped = blue;
		this.alphaWrapped = alpha;
	}
	
	@Override
    protected void setColor(float r, float g, float b, float a) {
		GL11.glColor4f(this.redWrapped * r, this.greenWrapped * g, this.blueWrapped * b, this.alphaWrapped * a);
	}

}
