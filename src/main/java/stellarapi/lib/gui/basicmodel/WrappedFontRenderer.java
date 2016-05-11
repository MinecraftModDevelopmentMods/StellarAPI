package stellarapi.lib.gui.basicmodel;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class WrappedFontRenderer extends FontRenderer {
	
	private float red, green, blue, alpha;
	
	public WrappedFontRenderer() {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
				Minecraft.getMinecraft().renderEngine, false);
		this.onResourceManagerReload(null);
	}
	
	public void setColorRGBA(float red, float green, float blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	@Override
    protected void setColor(float r, float g, float b, float a) {
		GL11.glColor4f(this.red * r, this.green * g, this.blue * b, this.alpha * a);
	}
	
	@Override
    protected float renderDefaultChar(int p_78266_1_, boolean p_78266_2_)
    {
        float f = (float)(p_78266_1_ % 16 * 8);
        float f1 = (float)(p_78266_1_ / 16 * 8);
        float f2 = p_78266_2_ ? 1.0F : 0.0F;
        bindTexture(this.locationFontTexture);
        float f3 = (float)this.charWidth[p_78266_1_] - 0.01F;
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(f / 128.0F, f1 / 128.0F);
        GL11.glVertex3f(this.posX + f2, this.posY, 0.0F);
        GL11.glTexCoord2f(f / 128.0F, (f1 + 7.99F) / 128.0F);
        GL11.glVertex3f(this.posX - f2, this.posY + 7.99F, 0.0F);
        GL11.glTexCoord2f((f + f3 - 1.0F) / 128.0F, f1 / 128.0F);
        GL11.glVertex3f(this.posX + f3 - 1.0F + f2, this.posY, 0.0F);
        GL11.glTexCoord2f((f + f3 - 1.0F) / 128.0F, (f1 + 7.99F) / 128.0F);
        GL11.glVertex3f(this.posX + f3 - 1.0F - f2, this.posY + 7.99F, 0.0F);
        GL11.glEnd();
        return (float)this.charWidth[p_78266_1_];
    }
	

	private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
	
	@Override
    protected float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_)
    {
        if (this.glyphWidth[p_78277_1_] == 0)
        {
            return 0.0F;
        }
        else
        {
            int i = p_78277_1_ / 256;
            this.loadGlyphTexture(i);
            int j = this.glyphWidth[p_78277_1_] >>> 4;
            int k = this.glyphWidth[p_78277_1_] & 15;
            float f = (float)j;
            float f1 = (float)(k + 1);
            float f2 = (float)(p_78277_1_ % 16 * 16) + f;
            float f3 = (float)((p_78277_1_ & 255) / 16 * 16);
            float f4 = f1 - f - 0.02F;
            float f5 = p_78277_2_ ? 1.0F : 0.0F;
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
            GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);
            GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
            GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
            GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
            GL11.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
            GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
            GL11.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
            GL11.glEnd();
            return (f1 - f) / 2.0F + 1.0F;
        }
    }
	
	
    private ResourceLocation getUnicodePageLocation(int p_111271_1_)
    {
        if (unicodePageLocations[p_111271_1_] == null)
        {
            unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] {Integer.valueOf(p_111271_1_)}));
        }

        return unicodePageLocations[p_111271_1_];
    }

    private void loadGlyphTexture(int p_78257_1_)
    {
        bindTexture(this.getUnicodePageLocation(p_78257_1_));
    }

}
