package stellarapi.feature.gui.overlay.time;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Vec3;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.PeriodHelper;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.daywake.DaytimeChecker;
import stellarapi.api.daywake.EnumDaytimeDescriptor;
import stellarapi.api.gui.overlay.EnumOverlayMode;
import stellarapi.api.gui.overlay.IOverlayElement;
import stellarapi.api.gui.overlay.PerOverlaySettings;

public class OverlayTime implements IOverlayElement<PerOverlaySettings> {
	private static final int WIDTH = 100;
	private static final int HEIGHT = 10;
	private static final int ANIMATION_DURATION = 10;

	private Minecraft mc;
	EnumOverlayMode currentMode = EnumOverlayMode.OVERLAY;

	private List<EnumDaytimeDescriptor> descriptors = Lists.newArrayList();
	private boolean isDay = true;
	private int icolor;

	boolean markForUpdate = false;

	@Override
	public void initialize(Minecraft mc, PerOverlaySettings settings) {
		this.mc = mc;
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT * (descriptors.size()+3);
	}

	@Override
	public float animationOffsetX(float partialTicks) {
		return 0.0f;
	}

	@Override
	public float animationOffsetY(float partialTicks) {
		return 0.0f;
	}

	@Override
	public void switchMode(EnumOverlayMode mode) {
		this.currentMode = mode;
	}

	@Override
	public void updateOverlay() {
		if(mc.theWorld == null)
			return;
		
		CelestialPeriod dayPeriod = PeriodHelper.getDayPeriod(mc.theWorld);
		if(dayPeriod == null)
			return;
				
		descriptors.clear();
		DaytimeChecker checker = StellarAPIReference.getDaytimeChecker();
		
		double dawn = dayPeriod.getOffset(checker.timeForCertainDescriptor(mc.theWorld, EnumDaytimeDescriptor.DAWN, 23000L), 0.0f);
		double dusk = dayPeriod.getOffset(checker.timeForCertainDescriptor(mc.theWorld, EnumDaytimeDescriptor.DUSK, 13000L), 0.0f);

		double current = dayPeriod.getOffset(mc.theWorld.getWorldTime(), 0.0f);
		
		this.isDay = current > dawn - 1.0 / 32 && current < dusk + 1.0 / 32;
		
		float[] colors = mc.theWorld.provider.calcSunriseSunsetColors(mc.theWorld.getCelestialAngle(0.0f), 0.0f);
		if(colors != null)
			this.icolor = ((int)(colors[0] * 255.0)<<16)+((int)(colors[1] * 255.0)<<8)+(int)(colors[2] * 255.0);
		else this.icolor = this.isDay? 0x77ff77 : 0xbb3333;
		
		for(EnumDaytimeDescriptor descriptor : EnumDaytimeDescriptor.values())
			if(checker.isDescriptorApply(mc.theWorld, descriptor, mc.theWorld.getWorldTime(), (int)(dayPeriod.getPeriodLength() / 16), false))
				descriptors.add(descriptor);
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int eventButton) {
		return false;
	}

	@Override
	public boolean mouseMovedOrUp(int mouseX, int mouseY, int eventButton) {
		return false;
	}

	@Override
	public boolean keyTyped(char eventChar, int eventKey) {
		return false;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int yOffset = 0;

		this.drawString(mc.fontRenderer, "display", WIDTH / 2, 10*(yOffset++)+5, 255, 0xffffff);
		this.drawString(mc.fontRenderer, this.isDay? "day":"night", WIDTH / 2, 10*(yOffset++)+5, 255, this.isDay? 0xffff77 : 0x5555aa);
		for(EnumDaytimeDescriptor descriptor : this.descriptors)
			if(mc.theWorld != null)
				this.drawString(mc.fontRenderer, descriptor.name(), WIDTH / 2, 10*(yOffset++)+5, 255, this.icolor);
	}
	
	private void drawString(FontRenderer fontRenderer, String str, int x, int y, int alpha, int color) {
		str = I18n.format("gui.time."+str.toLowerCase());
		fontRenderer.drawStringWithShadow(str, x - fontRenderer.getStringWidth(str) / 2, y, color + (alpha<<24));
	}

}
