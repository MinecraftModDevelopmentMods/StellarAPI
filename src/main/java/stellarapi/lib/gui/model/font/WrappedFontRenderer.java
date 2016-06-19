package stellarapi.lib.gui.model.font;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class WrappedFontRenderer extends FontRenderer {

	private float redWrapped, greenWrapped, blueWrapped, alphaWrapped;
	private TextStyle style;

	public WrappedFontRenderer() {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
				Minecraft.getMinecraft().renderEngine, false);
		this.onResourceManagerReload(null);
		this.style = new TextStyle();
	}

	public TextStyle getStyle() {
		return this.style;
	}

	public void setStyle(TextStyle newStyle) {
		style.set(newStyle);
	}

	public void setColorRGBA(float red, float green, float blue, float alpha) {
		this.redWrapped = red;
		this.greenWrapped = green;
		this.blueWrapped = blue;
		this.alphaWrapped = alpha;
	}

	private String getStyleContext() {
		StringBuilder stringbuilder = new StringBuilder();

		if (style.isBold())
			stringbuilder.append(TextFormatting.BOLD);

		if (style.isItalic())
			stringbuilder.append(TextFormatting.ITALIC);

		if (style.isUnderlined())
			stringbuilder.append(TextFormatting.UNDERLINE);

		if (style.isStrikeThrough())
			stringbuilder.append(TextFormatting.STRIKETHROUGH);

		return stringbuilder.toString();
	}

	/**
	 * Recommended method to draw string
	 */
	public int drawString(String str, float posX, float posY) {
		if (!style.isShaded())
			return this.drawString(str, posX, posY, 0xffffffff, false);
		else
			return this.drawStringWithShadow(str, posX, posY, 0xffffffff);
	}

	@Override
	public int drawString(String str, float posX, float posY, int color, boolean dropShadow) {
		return super.drawString(this.getStyleContext() + str, posX, posY, color, dropShadow);
	}

	@Override
	protected void setColor(float r, float g, float b, float a) {
		GL11.glColor4f(this.redWrapped * r, this.greenWrapped * g, this.blueWrapped * b, this.alphaWrapped * a);
	}

}
