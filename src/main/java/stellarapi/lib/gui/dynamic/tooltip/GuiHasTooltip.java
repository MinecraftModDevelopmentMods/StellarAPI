package stellarapi.lib.gui.dynamic.tooltip;

import java.util.List;

import com.google.common.collect.Lists;

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
	private StringFormat info = null;
	
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
		this.info = null;
		wrapped.getType().checkMousePosition(mouseX, mouseY);
	}

	@Override
	public void render(IRenderer renderer) {
		wrapped.getType().render(renderer);
		
		if(this.info != null) {
			IRectangleBound tooltipBound = position.getAdditionalBound("tooltip");
			if(tooltipBound == null)
				tooltipBound = position.getClipBound();
			float tooltipX = this.mouseX;
			float tooltipY = this.mouseY;
			
			List<String> context = controller.getLineContext(this.info);
			List<String> toRender = Lists.newArrayList();
			List<IFontHelper> renderHelpers = Lists.newArrayList();
			int cnt;
			
			cnt = 0;
			for(String lineContext : context) {
				toRender.set(cnt, controller.toRenderableText(lineContext));
				renderHelpers.set(cnt, controller.lineSpecificFont(lineContext));
				cnt++;
			}

			float width = 0.0f, height = 0.0f;
			cnt = 0;
			for(String renderLine : toRender) {
				width = Math.max(renderHelpers.get(cnt).getStringWidth(renderLine), width);
				height += renderHelpers.get(cnt).getStringHeight();
				cnt++;
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
			
			cnt = 0;
			for(String renderLine : toRender) {
				height = renderHelpers.get(cnt).getStringHeight();
				temporal.set(tooltipX, tooltipY, width, height);
				temporalClip2.set(this.temporalClip);
				temporalClip2.setAsIntersection(this.temporal);

				controller.setupTooltip(context.get(cnt), renderer);
				renderer.render(renderLine, this.temporal, this.temporalClip2);

				tooltipY += height;
				cnt++;
			}

			renderer.endRender();
		}
	}
	
	protected void notifyRenderTooltip(float mouseX, float mouseY, StringFormat info) {
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
					public StringFormat getTooltipInfo(float ratioX, float ratioY) {
						return new StringFormat(info);
					}
		});
	}
}
