package stellarapi.feature.gui.overlay.time;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
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
	private boolean invalidDay = false;
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
		return HEIGHT * (descriptors.size() + 3);
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
		if (mc.world == null)
			return;

		CelestialPeriod dayPeriod = PeriodHelper.getDayPeriod(mc.world);
		if (dayPeriod == null) {
			descriptors.clear();
			this.invalidDay = true;
			return;
		}

		descriptors.clear();
		DaytimeChecker checker = StellarAPIReference.getDaytimeChecker();

		long dawnTime = checker.timeForCertainDescriptor(mc.world, EnumDaytimeDescriptor.DAWN, -1L);
		long duskTime = checker.timeForCertainDescriptor(mc.world, EnumDaytimeDescriptor.DUSK, -1L);

		if (dawnTime == -1 || duskTime == -1)
			this.invalidDay = true;
		else
			this.invalidDay = false;

		double dawn = dayPeriod.getOffset(dawnTime, 0.0f);
		double dusk = dayPeriod.getOffset(duskTime, 0.0f);

		double current = dayPeriod.getOffset(mc.world.getWorldTime(), 0.0f);

		this.isDay = current > dawn - 1.0 / 32 && current < dusk + 1.0 / 32;

		float[] colors = mc.world.provider.calcSunriseSunsetColors(mc.world.getCelestialAngle(0.0f), 0.0f);
		if (colors != null)
			this.icolor = ((int) (colors[0] * 255.0) << 16) + ((int) (colors[1] * 255.0) << 8)
					+ (int) (colors[2] * 255.0);
		else
			this.icolor = this.isDay ? 0x77ff77 : 0xbb3333;

		for (EnumDaytimeDescriptor descriptor : EnumDaytimeDescriptor.values())
			if (checker.isDescriptorApply(mc.world, descriptor, mc.world.getWorldTime(),
					(int) (dayPeriod.getPeriodLength() / 16), false))
				descriptors.add(descriptor);
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int eventButton) {
		return false;
	}

	@Override
	public boolean mouseReleased(int mouseX, int mouseY, int eventButton) {
		return false;
	}

	@Override
	public boolean keyTyped(char eventChar, int eventKey) {
		return false;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int yOffset = 0;

		this.drawString(mc.fontRendererObj, "display", WIDTH / 2, 10 * (yOffset++) + 5, 255, 0xffffff);
		this.drawString(mc.fontRendererObj, this.invalidDay ? "invalid" : this.isDay ? "day" : "night", WIDTH / 2,
				10 * (yOffset++) + 5, 255, this.invalidDay ? 0x770000 : this.isDay ? 0xffff77 : 0x5555aa);
		for (EnumDaytimeDescriptor descriptor : this.descriptors)
			if (mc.world != null)
				this.drawString(mc.fontRendererObj, descriptor.name(), WIDTH / 2, 10 * (yOffset++) + 5, 255,
						this.icolor);
	}

	private void drawString(FontRenderer fontRenderer, String str, int x, int y, int alpha, int color) {
		str = I18n.format("gui.time." + str.toLowerCase());
		fontRenderer.drawStringWithShadow(str, x - fontRenderer.getStringWidth(str) / 2, y, color + (alpha << 24));
	}

	@Override
	public boolean mouseClickMove(int scaledMouseX, int scaledMouseY, int eventButton, long timeSinceLastClick) {
		return false;
	}

}
