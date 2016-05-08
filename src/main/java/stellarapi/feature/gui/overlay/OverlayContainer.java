package stellarapi.feature.gui.overlay;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.gui.ScaledResolution;
import stellarapi.api.gui.overlay.EnumOverlayMode;
import stellarapi.api.gui.overlay.IOverlay;
import stellarapi.api.gui.overlay.IRawOverlayElement;
import stellarapi.api.gui.pos.ElementPos;

public class OverlayContainer {

	private int width;
	private int height;

	private EnumOverlayMode currentMode = EnumOverlayMode.OVERLAY;
	private ImmutableList<OverlayElementDelegate> currentlyDisplayedList;
	
	public void resetDisplayList(Iterable<OverlayElementDelegate> iterable) {
		this.currentlyDisplayedList = ImmutableList.copyOf(iterable);
	}
	
	ImmutableList<? extends IRawOverlayElement> getCurrentDisplayedList() {
		return this.currentlyDisplayedList; 
	}
	
	public void setResolution(ScaledResolution resolution) {
		this.width = resolution.getScaledWidth();
		this.height = resolution.getScaledHeight();
	}

	public void switchMode(EnumOverlayMode mode) {
		for(OverlayElementDelegate delegate : this.currentlyDisplayedList)
			delegate.getElement().switchMode(mode);
	}

	public void updateOverlay() {
		for(OverlayElementDelegate delegate : this.currentlyDisplayedList)
			delegate.getElement().updateOverlay();
	}

	public void mouseClicked(int mouseX, int mouseY, int eventButton) {
		for(OverlayElementDelegate delegate : this.currentlyDisplayedList) {
			boolean changed = false;

			ElementPos pos = delegate.getPosition();
			IOverlay element = delegate.getElement();
			int width = element.getWidth();
			int height = element.getHeight();
			int scaledMouseX = pos.getHorizontalPos().translateInto(mouseX, this.width, width);
			int scaledMouseY = pos.getVerticalPos().translateInto(mouseY, this.height, height);
			scaledMouseX -= element.animationOffsetX(0.0f);
			scaledMouseY -= element.animationOffsetY(0.0f);
			
			changed = element.mouseClicked(scaledMouseX, scaledMouseY, eventButton) || changed;
			
			if(delegate.getHandler() != null)
				changed = delegate.getHandler().mouseClicked(mouseX, mouseY, eventButton) || changed;
			
			if(changed)
				delegate.notifyChange();
		}
	}

	public void mouseMovedOrUp(int mouseX, int mouseY, int eventButton) {
		for(OverlayElementDelegate delegate : this.currentlyDisplayedList) {
			boolean changed = false;

			ElementPos pos = delegate.getPosition();
			IOverlay element = delegate.getElement();
			int width = element.getWidth();
			int height = element.getHeight();
			int scaledMouseX = pos.getHorizontalPos().translateInto(mouseX, this.width, width);
			int scaledMouseY = pos.getVerticalPos().translateInto(mouseY, this.height, height);
			scaledMouseX -= element.animationOffsetX(0.0f);
			scaledMouseY -= element.animationOffsetY(0.0f);
			
			changed = element.mouseMovedOrUp(scaledMouseX, scaledMouseY, eventButton) || changed;
			
			if(delegate.getHandler() != null)
				changed = delegate.getHandler().mouseMovedOrUp(mouseX, mouseY, eventButton) || changed;
			
			if(changed)
				delegate.notifyChange();
		}
	}

	public void keyTyped(char eventChar, int eventKey) {
		for(OverlayElementDelegate delegate : this.currentlyDisplayedList) {
			boolean changed = false;
			
			changed = delegate.getElement().keyTyped(eventChar, eventKey) || changed;
			
			if(delegate.getHandler() != null)
				changed = delegate.getHandler().keyTyped(eventChar, eventKey) || changed;
			
			if(changed)
				delegate.notifyChange();
		}
	}

	public void render(int mouseX, int mouseY, float partialTicks) {
		for(OverlayElementDelegate delegate : this.currentlyDisplayedList) {
			ElementPos pos = delegate.getPosition();
			IOverlay element = delegate.getElement();
			int width = element.getWidth();
			int height = element.getHeight();
			float animationOffsetX = element.animationOffsetX(partialTicks);
			float animationOffsetY = element.animationOffsetY(partialTicks);
			
			int scaledMouseX = pos.getHorizontalPos().translateInto(mouseX, this.width, width);
			int scaledMouseY = pos.getVerticalPos().translateInto(mouseY, this.height, height);
			scaledMouseX -= animationOffsetX;
			scaledMouseY -= animationOffsetY;
			
			GL11.glPushMatrix();
			GL11.glTranslatef((pos.getHorizontalPos().getOffset(this.width, width) + animationOffsetX),
					(pos.getVerticalPos().getOffset(this.height, height) + animationOffsetY),
					0.0f);
			
			element.render(scaledMouseX, scaledMouseY, partialTicks);
			GL11.glPopMatrix();
			
			if(delegate.getHandler() != null)
				delegate.getHandler().render(mouseX, mouseY, partialTicks);
		}
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
}
