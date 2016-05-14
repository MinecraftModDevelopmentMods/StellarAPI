package stellarapi.lib.gui.model.font;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

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
            stringbuilder.append(EnumChatFormatting.BOLD);

        if (style.isItalic())
            stringbuilder.append(EnumChatFormatting.ITALIC);

        if (style.isUnderlined())
            stringbuilder.append(EnumChatFormatting.UNDERLINE);

        if (style.isStrikeThrough())
            stringbuilder.append(EnumChatFormatting.STRIKETHROUGH);
        
        return stringbuilder.toString();
	}
	
	/**
	 * Recommended method to draw string
	 * */
    public int drawString(String str, int posX, int posY) {
    	if(!style.isShaded())
    		return this.drawString(str, posX, posY, 0xffffffff);
    	else return this.drawStringWithShadow(str, posX, posY, 0xffffffff);
    }
	
	@Override
    public int drawString(String str, int posX, int posY, int color, boolean dropShadow) {
		return super.drawString(this.getStyleContext() + str, posX, posY, color, dropShadow);
	}
	
	@Override
    protected void setColor(float r, float g, float b, float a) {
		GL11.glColor4f(this.redWrapped * r, this.greenWrapped * g, this.blueWrapped * b, this.alphaWrapped * a);
	}

}
