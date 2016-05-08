package stellarapi.feature.gui.overlay.time;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.daywake.DaytimeChecker;
import stellarapi.api.daywake.EnumDaytimeDescriptor;
import stellarapi.api.gui.overlay.EnumOverlayMode;
import stellarapi.api.gui.overlay.IOverlay;
import stellarapi.api.gui.overlay.PerOverlaySettings;

public class OverlayTime implements IOverlay<PerOverlaySettings> {
	private static final int WIDTH = 60;
	private static final int HEIGHT = 20;
	private static final int ANIMATION_DURATION = 10;

	private Minecraft mc;
	EnumOverlayMode currentMode = EnumOverlayMode.OVERLAY;

	private int animationTick = 0;

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
		return HEIGHT;
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
		if(currentMode.displayed() != mode.displayed()) {
			if(mode.displayed())
				animationTick = 0;
			else animationTick = ANIMATION_DURATION;
		}
		this.currentMode = mode;
	}

	@Override
	public void updateOverlay() {
		if(this.animationTick > 0 && !currentMode.displayed())
			this.animationTick--;
		else if(this.animationTick < ANIMATION_DURATION && currentMode.displayed())
			this.animationTick++;
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
		//partialTicks = currentMode.displayed()? partialTicks : -partialTicks;
		//float alpha = (this.animationTick + partialTicks) / ANIMATION_DURATION;
		//int ialpha = MathHelper.clamp_int((int)(alpha * 255), 0, 255);
		
		int yOffset = 0;

		DaytimeChecker checker = StellarAPIReference.getDaytimeChecker();
		for(EnumDaytimeDescriptor descriptor : EnumDaytimeDescriptor.values())
			if(checker.isDescriptorApply(mc.theWorld, descriptor, mc.theWorld.getWorldTime(), 3000, false))
				this.drawString(mc.fontRenderer, descriptor.name(), 5, 10*(yOffset++)+5, 255, 0xffff77);
	}
	
	private void drawString(FontRenderer fontRenderer, String str, int x, int y, int alpha, int color) {
		fontRenderer.drawString(str, x, y, color + (alpha<<24));
	}

}
