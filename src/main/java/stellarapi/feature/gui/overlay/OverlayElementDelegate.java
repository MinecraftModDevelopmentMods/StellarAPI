package stellarapi.feature.gui.overlay;

import net.minecraft.client.Minecraft;
import stellarapi.api.gui.overlay.IOverlayElement;
import stellarapi.api.gui.overlay.IOverlayType;
import stellarapi.api.gui.overlay.IRawHandler;
import stellarapi.api.gui.overlay.IRawOverlayElement;
import stellarapi.api.gui.overlay.PerOverlaySettings;
import stellarapi.api.gui.pos.ElementPos;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;
import stellarapi.api.lib.config.DynamicConfigManager;

public class OverlayElementDelegate<Element extends IOverlayElement<Settings>, Settings extends PerOverlaySettings>
		implements IRawOverlayElement {

	private OverlayManager manager;

	private String id, modid;

	private ElementPos pos;
	private final IOverlayType<Element, Settings> type;
	private final Element element;
	private final Settings settings;
	private final IRawHandler<Element> handler;
	private final DynamicConfigManager notified;

	OverlayElementDelegate(IOverlayType<Element, Settings> type, Settings settings, DynamicConfigManager config,
			OverlayManager manager, String id, String modid) {
		this.manager = manager;
		this.id = id;
		this.modid = modid;

		this.type = type;
		this.element = type.generateElement();
		this.handler = type.generateRawHandler();
		this.settings = settings;
		this.notified = config;
	}

	void initialize(Minecraft mc) {
		element.initialize(mc, this.settings);
		if (this.handler != null)
			handler.initialize(mc, this.manager, this.element);

		this.pos = new ElementPos(settings.getHorizontal(), settings.getVertical());
	}

	Element getElement() {
		return this.element;
	}

	public IRawHandler<Element> getHandler() {
		return this.handler;
	}

	ElementPos getPosition() {
		return this.pos;
	}

	void notifyChange() {
		notified.syncFromFields();
	}

	boolean visibleOnMain() {
		return settings.isVisibleOnMain();
	}

	@Override
	public void setVisibleOnMain(boolean visibleOnMain) {
		settings.setVisibleOnMain(visibleOnMain);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ElementPos) {
			return pos.equals(obj);
		} else if (obj instanceof OverlayElementDelegate) {
			return pos.equals(((OverlayElementDelegate) obj).pos);

		}
		return false;
	}

	@Override
	public IOverlayType getType() {
		return this.type;
	}

	@Override
	public EnumHorizontalPos getCurrentHorizontalPos() {
		return pos.getHorizontalPos();
	}

	@Override
	public EnumVerticalPos getCurrentVerticalPos() {
		return pos.getVerticalPos();
	}

	@Override
	public boolean acceptPos(EnumHorizontalPos horizontal, EnumVerticalPos vertical) {
		if (!type.accepts(horizontal, vertical))
			return false;
		return manager.hasNoDuplication(this.id, horizontal, vertical);
	}

	@Override
	public void setPos(EnumHorizontalPos horizontal, EnumVerticalPos vertical) {
		ElementPos pos = new ElementPos(horizontal, vertical);

		settings.setHorizontal(pos.getHorizontalPos());
		settings.setVertical(pos.getVerticalPos());
		this.pos = pos;

		manager.setElementPosOnDisplaySets(this.id, pos);
	}

	@Override
	public int getWidth() {
		return element.getWidth();
	}

	@Override
	public int getHeight() {
		return element.getHeight();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getModId() {
		return this.modid;
	}
}
