package stellarapi.feature.gui.overlay.configurator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import stellarapi.api.gui.overlay.EnumOverlayMode;
import stellarapi.api.gui.overlay.IRawOverlayElement;
import stellarapi.api.gui.overlay.IOverlayManager;
import stellarapi.api.gui.overlay.IRawHandler;
import stellarapi.api.gui.pos.ElementPos;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;

public class OverlayConfiguratorHandler implements IRawHandler<OverlayConfigurator> {
	
	private Minecraft mc;
	private IOverlayManager manager;
	private OverlayConfigurator element;
	
	private IRawOverlayElement currentSelected = null;
	private EnumHorizontalPos horizontal;
	private EnumVerticalPos vertical;

	@Override
	public void initialize(Minecraft mc, IOverlayManager manager, OverlayConfigurator element) {
		this.mc = mc;
		this.manager = manager;
		this.element = element;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int eventButton) {
		if(element.markForUpdate) {
			manager.switchMode(element.currentMode.focused()?
					EnumOverlayMode.POSITION : EnumOverlayMode.FOCUS);
			element.markForUpdate = false;
		}
		
		if(element.currentMode != EnumOverlayMode.POSITION)
			return false;
		
		this.currentSelected = manager.getCurrentElement(mouseX, mouseY);
		if(this.currentSelected != null) {
			this.horizontal = currentSelected.getCurrentHorizontalPos();
			this.vertical = currentSelected.getCurrentVerticalPos();
		}
		
		return false;
	}

	@Override
	public boolean mouseMovedOrUp(int mouseX, int mouseY, int eventButton) {
		if(element.currentMode != EnumOverlayMode.POSITION)
			return false;

		if(eventButton != -1 && this.currentSelected != null) {
			boolean flag = false;
			if(currentSelected.acceptPos(this.horizontal, this.vertical)) {
				currentSelected.setPos(this.horizontal, this.vertical);
				flag = true;
			}
			this.currentSelected = null;
			return flag;
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char eventChar, int eventKey) {
		return false;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if(element.currentMode != EnumOverlayMode.POSITION)
			return;
		
		if(this.currentSelected != null) {
			int elementWidth = currentSelected.getWidth();
			int elementHeight = currentSelected.getHeight();
			int currentWidth = manager.getCurrentWidth();
			int currentHeight = manager.getCurrentHeight();
			
			if(!horizontal.inRange(mouseX, currentWidth, elementWidth)
					|| !vertical.inRange(mouseY, currentHeight, elementHeight)) {
				this.horizontal = EnumHorizontalPos.getNearest(mouseX, currentWidth, elementWidth);
				this.vertical = EnumVerticalPos.getNearest(mouseY, currentHeight, elementHeight);
			}
			
			boolean possible = currentSelected.acceptPos(this.horizontal, this.vertical);
			
			int offsetX = horizontal.getOffset(currentWidth, elementWidth);
			int offsetY = vertical.getOffset(currentHeight, elementHeight);
			
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			Gui.drawRect(offsetX+1, offsetY+1,
					offsetX + elementWidth, offsetY + elementHeight,
					possible? 0xff00ff00 : 0xffff0000);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			Gui.drawRect(offsetX+1, offsetY+1,
					offsetX + elementWidth, offsetY + elementHeight,
					possible? 0x0700ff00 : 0x07ff0000);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

}
