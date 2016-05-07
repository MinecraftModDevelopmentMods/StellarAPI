package stellarapi.api.gui.overlay;

import net.minecraft.client.Minecraft;
import stellarapi.api.gui.overlay.OverlayContainer.Delegate;
import stellarapi.api.gui.pos.ElementPos;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;
import stellarapi.api.lib.config.ConfigManager;

public class PerOverlayContainer<Element extends IGuiOverlay<Settings>, Settings extends PerOverlaySettings> {
	
	ElementPos pos;
	final IGuiOverlayType<Element, Settings> type;
	final Element element;
	final Settings settings;
	final IRawHandler<Element> handler;
	final ConfigManager notified;
	
	private PerOverlayContainer(IGuiOverlayType<Element, Settings> type, ConfigManager config) {
		this.type = type;
		this.element = type.generateElement();
		this.settings = type.generateSettings();
		this.handler = type.generateRawHandler();
		this.notified = config;
		
		notified.register(type.getName(), this.settings);
		settings.initializeSetttings(type.defaultHorizontalPos(), type.defaultVerticalPos());
	}
	
	private void initialize(Minecraft mc) {
		element.initialize(mc, this.settings);
		if(this.handler != null)
			handler.initialize(mc, this, this.element);
		
		this.pos = new ElementPos(settings.getHorizontal(), settings.getVertical());
	}
	
	public boolean canSetPos(EnumHorizontalPos horizontal, EnumVerticalPos vertical) {
		if(!type.accepts(horizontal, vertical))
			return false;
		
		ElementPos pos = new ElementPos(horizontal, vertical);
		for(Delegate delegate : elementList) {
			if(delegate.equals(pos))
				return false;
		}
		
		return true;
	}
	
	public boolean trySetPos(EnumHorizontalPos horizontal, EnumVerticalPos vertical) {
		if(!canSetPos(horizontal, vertical))
			return false;
		
		ElementPos pos = new ElementPos(horizontal, vertical);
		
		settings.setHorizontal(pos.getHorizontalPos());
		settings.setVertical(pos.getVerticalPos());
		this.pos = pos;
		return true;
	}
	
	public int getWidth() {
		return element.getWidth();
	}
	
	public int getHeight() {
		return element.getHeight();
	}
	
	public ElementPos getCurrentPos() {
		return this.pos;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ElementPos) {
			return pos.equals(obj);
		} else if(obj instanceof Delegate) {
			return pos.equals(((Delegate) obj).pos);
			
		} return false;
	}
}
