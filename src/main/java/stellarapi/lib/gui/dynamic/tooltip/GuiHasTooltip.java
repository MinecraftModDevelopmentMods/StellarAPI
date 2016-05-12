package stellarapi.lib.gui.dynamic.tooltip;

import java.util.List;

import stellarapi.lib.gui.GuiElement;
import stellarapi.lib.gui.GuiPositionHierarchy;
import stellarapi.lib.gui.IFontHelper;
import stellarapi.lib.gui.IGuiElementType;
import stellarapi.lib.gui.IGuiPosition;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderer;
import stellarapi.lib.gui.RectangleBound;

/**
 * Gui with tooptip.
 * Note that position provided should have tooltip bounds.
 *  */
public class GuiHasTooltip implements IGuiElementType<ITooltipController> {
	
	private static final float mouseSize = 10.0f;
	
	private IGuiPosition position;
	private GuiElement wrapped;
	private ITooltipController controller;
	
	private float mouseX, mouseY;
	private String info = "";
	
	private RectangleBound temporal = new RectangleBound(0,0,0,0);
	private RectangleBound temporalClip = new RectangleBound(0,0,0,0);
	private RectangleBound temporalClip2 = new RectangleBound(0,0,0,0);
	
	public void setWrappedGui(GuiElement wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void initialize(GuiPositionHierarchy positions, ITooltipController controller) {
		this.position = positions.getPosition();
		this.controller = controller;
		wrapped.initialize(positions);
	}

	@Override
	public void updateElement() {
		wrapped.getType().updateElement();
	}

	@Override
	public void mouseClicked(float mouseX, float mouseY, int eventButton) {
		wrapped.getType().mouseClicked(mouseX, mouseY, eventButton);
	}

	@Override
	public void mouseMovedOrUp(float mouseX, float mouseY, int eventButton) {
		wrapped.getType().mouseMovedOrUp(mouseX, mouseY, eventButton);
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		wrapped.getType().keyTyped(eventChar, eventKey);
	}

	@Override
	public void checkMousePosition(float mouseX, float mouseY) {
		info = "";
		wrapped.getType().checkMousePosition(mouseX, mouseY);
	}

	@Override
	public void render(IRenderer renderer) {
		wrapped.getType().render(renderer);
		
		if(!info.isEmpty()) {
			IFontHelper helper = controller.getFontHelper();
			IRectangleBound tooltipBound = position.getAdditionalBound("tooltip");
			if(tooltipBound == null)
				tooltipBound = position.getClipBound();
			float tooltipX = this.mouseX;
			float tooltipY = this.mouseY;
			
			List<String> tooltip = controller.getRenderContext(this.info);
			
			float width = 0.0f, height = 0.0f;
			for(String context : tooltip) {
				width = Math.max(helper.getStringWidth(context), width);
				height += helper.getStringHeight();
			}
			
			float spacingX = controller.getSpacingX();
			float spacingY = controller.getSpacingY();
			
			width += 2 * spacingX;
			height += 2 * spacingY;
			
			
			boolean hasClip = controller.hasClip();
			
			if(tooltipBound.getRatioX(tooltipX) > 0.5f)
				tooltipX -= width;
			if(tooltipBound.getRatioY(tooltipY) > 0.5f)
				tooltipY -= height;
			if(hasClip) {
				if(tooltipX < tooltipBound.getLeftX())
					tooltipX = tooltipBound.getLeftX();
				else if(tooltipX + width > tooltipBound.getRightX())
					tooltipX = tooltipBound.getRightX() - width;
				if(tooltipY < tooltipBound.getUpY())
					tooltipY = tooltipBound.getUpY();
				else if(tooltipY + height > tooltipBound.getDownY())
					tooltipY = tooltipBound.getDownY() - height;
			}
			
			if(Math.abs(tooltipX - this.mouseX) < mouseSize)
				tooltipX = tooltipX < this.mouseX? this.mouseX - mouseSize : this.mouseX + mouseSize;
			
			temporal.set(tooltipX, tooltipY, width, height);
			temporalClip.set(this.temporal);
			if(hasClip)
				temporalClip.setAsIntersection(position.getClipBound());

			renderer.startRender();

			String background = controller.setupBackground(this.info, renderer);
			renderer.render(background, this.temporal, this.temporalClip);
			
			tooltipX += spacingX;
			tooltipY += spacingY;
			width -= 2 * spacingX;
			
			for(String context : tooltip) {
				temporal.set(tooltipX, tooltipY, width, helper.getStringHeight());
				temporalClip2.set(this.temporalClip);
				temporalClip2.setAsIntersection(this.temporal);

				controller.setupTooltip(context, renderer);
				renderer.render(context, this.temporal, this.temporalClip2);

				tooltipY += helper.getStringHeight();
			}

			renderer.endRender();
		}
	}
	
	protected void notifyRenderTooltip(float mouseX, float mouseY, String info) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.info = info;
	}
	
	
	public GuiElement wrapElement(GuiElement element, ITooltipElementController controller) {
		return new GuiElement<ITooltipElementController>(new GuiTooltipElementWrapper(element, this), controller);
	}
	
	public GuiElement wrapElement(GuiElement element, final String info) {
		return new GuiElement<ITooltipElementController>(new GuiTooltipElementWrapper(element, this),
				new ITooltipElementController() {
					@Override
					public boolean canDisplayTooltip() {
						return true;
					}
					@Override
					public int getTooltipDisplayWaitTime() {
						return 800;
					}
					@Override
					public String getTooltipInfo(float ratioX, float ratioY) {
						return info;
					}
		});
	}
}
