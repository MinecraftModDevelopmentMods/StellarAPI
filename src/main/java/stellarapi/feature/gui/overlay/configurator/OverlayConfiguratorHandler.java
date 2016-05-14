package stellarapi.feature.gui.overlay.configurator;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import stellarapi.api.gui.overlay.EnumOverlayMode;
import stellarapi.api.gui.overlay.IOverlayManager;
import stellarapi.api.gui.overlay.IRawHandler;
import stellarapi.api.gui.overlay.IRawOverlayElement;
import stellarapi.api.gui.overlay.IRawOverlaySet;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;
import stellarapi.feature.gui.overlay.OverlaySetMain;

public class OverlayConfiguratorHandler implements IRawHandler<OverlayConfigurator> {
	
	private Minecraft mc;
	private IOverlayManager manager;
	private OverlayConfigurator element;
	
	private IRawOverlayElement currentSelected = null;
	private EnumHorizontalPos horizontal;
	private EnumVerticalPos vertical;
	
	private ImmutableList<IRawOverlaySet> overlays;
	private IRawOverlaySet mainOverlay;
	private int eventButton;

	@Override
	public void initialize(Minecraft mc, IOverlayManager manager, OverlayConfigurator element) {
		this.mc = mc;
		this.manager = manager;
		this.element = element;
		
		this.overlays = manager.getDisplayedSets();
		for(IRawOverlaySet set : this.overlays)
			if(set.getType().isMain())
				this.mainOverlay = set;
	}
	
	@Override
	public void updateHandler() {
		element.currentSet = manager.getCurrentDisplayedSet();
		element.gamePaused = manager.isGamePaused();
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int eventButton) {
		if(element.markForUpdate) {
			manager.switchMode(element.currentMode.focused()?
					EnumOverlayMode.POSITION : EnumOverlayMode.FOCUS);
			element.markForUpdate = false;
		} else if(element.markForUpdateSet) {
			int index = overlays.indexOf(element.currentSet);
			IRawOverlaySet set = overlays.get((index+1)%overlays.size());
			set.setDisplayed();
			element.markForUpdateSet = false;
		}
		
		if(element.markForUpdatePause) {
			manager.setGamePaused(!manager.isGamePaused());
			element.markForUpdatePause = false;
		}
		
		if(element.currentMode != EnumOverlayMode.POSITION)
			return false;
		
		this.currentSelected = manager.getCurrentElement(mouseX, mouseY);
		if(this.currentSelected != null) {
			this.horizontal = currentSelected.getCurrentHorizontalPos();
			this.vertical = currentSelected.getCurrentVerticalPos();
			
			if(this.horizontal == EnumHorizontalPos.RIGHT
					&& this.vertical == EnumVerticalPos.CENTER)
				this.currentSelected = null;
		}
		
		this.eventButton = eventButton;
		
		return false;
	}

	@Override
	public boolean mouseMovedOrUp(int mouseX, int mouseY, int eventButton) {
		if(element.currentMode != EnumOverlayMode.POSITION)
			return false;
		
		if(this.currentSelected != null) {
			boolean flag = false;
			
			if(eventButton == 0) {
				if(currentSelected.acceptPos(this.horizontal, this.vertical)) {
					currentSelected.setPos(this.horizontal, this.vertical);
					flag = true;
				}
			} else if(eventButton == 1) {
				if(mainOverlay.doesContain(currentSelected)) {
					mainOverlay.removeElement(currentSelected);
					flag = true;
				} else flag = mainOverlay.addElement(currentSelected);
				if(element.currentSet == this.mainOverlay)
					mainOverlay.setDisplayed();
			}
			this.currentSelected = null;
			this.horizontal = null;
			this.vertical = null;
			
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
		if(element.currentSet == null)
			element.currentSet = manager.getCurrentDisplayedSet();
		
		if(element.currentMode != EnumOverlayMode.POSITION)
			return;
		
		int currentWidth = manager.getCurrentWidth();
		int currentHeight = manager.getCurrentHeight();
		
		if(this.currentSelected != null) {
			int elementWidth = currentSelected.getWidth();
			int elementHeight = currentSelected.getHeight();
			
			if(this.eventButton == 0 && (!horizontal.inRange(mouseX, currentWidth, elementWidth)
					|| !vertical.inRange(mouseY, currentHeight, elementHeight))) {
				this.horizontal = EnumHorizontalPos.getNearest(mouseX, currentWidth, elementWidth);
				this.vertical = EnumVerticalPos.getNearest(mouseY, currentHeight, elementHeight);
			}
						
			int offsetX = horizontal.getOffset(currentWidth, elementWidth);
			int offsetY = vertical.getOffset(currentHeight, elementHeight);
			
			if(this.eventButton == 0) {
				boolean possible = currentSelected.acceptPos(this.horizontal, this.vertical);

				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				Gui.drawRect(offsetX+1, offsetY+1,
						offsetX + elementWidth-1, offsetY + elementHeight-1,
						possible? 0xff00ff00 : 0xffff0000);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

				GL11.glDisable(GL11.GL_ALPHA_TEST);
				Gui.drawRect(offsetX+1, offsetY+1,
						offsetX + elementWidth-1, offsetY + elementHeight-1,
						possible? 0x0f00ff00 : 0x0fff0000);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			} else if(this.eventButton == 1) {
				boolean contain = mainOverlay.doesContain(this.currentSelected);

				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				Gui.drawRect(offsetX+3, offsetY+3,
						offsetX + elementWidth-3, offsetY + elementHeight-3,
						!contain? 0xff0066ff : 0xffff0000);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

				GL11.glDisable(GL11.GL_ALPHA_TEST);
				Gui.drawRect(offsetX+3, offsetY+3,
						offsetX + elementWidth-3, offsetY + elementHeight-3,
						!contain? 0x0f0099ff : 0x0fff0000);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				
				if(!contain)
					Gui.drawRect(offsetX + elementWidth/2 - 1, offsetY + elementHeight/2 - 8,
							offsetX + elementWidth/2 + 1, offsetY + elementHeight/2 + 8,
							0xff0000ff);
				Gui.drawRect(offsetX + elementWidth/2 - 8, offsetY + elementHeight/2 - 1,
						offsetX + elementWidth/2 + 8, offsetY + elementHeight/2 + 1,
						!contain? 0xff0000ff : 0xffff0000);
			}
		}
		
		for(IRawOverlayElement element : mainOverlay.getContainedElements()) {
			int elementWidth = element.getWidth();
			int elementHeight = element.getHeight();
			
			EnumHorizontalPos horizontal = element.getCurrentHorizontalPos();
			EnumVerticalPos vertical = element.getCurrentVerticalPos();
			
			if(horizontal == EnumHorizontalPos.RIGHT
					&& vertical == EnumVerticalPos.CENTER)
				continue;
			
			if(horizontal == this.horizontal
					&& vertical == this.vertical)
				continue;
			
			int offsetX = horizontal.getOffset(currentWidth, elementWidth);
			int offsetY = vertical.getOffset(currentHeight, elementHeight);
			
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			Gui.drawRect(offsetX+3, offsetY+3,
					offsetX + elementWidth-3, offsetY + elementHeight-3,
					0xff0000ff);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

			GL11.glDisable(GL11.GL_ALPHA_TEST);
			Gui.drawRect(offsetX+3, offsetY+3,
					offsetX + elementWidth-3, offsetY + elementHeight-3,
					0x0f0000ff);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

}
