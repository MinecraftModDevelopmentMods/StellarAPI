package stellarapi.feature.gui.overlay;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import stellarapi.api.gui.overlay.IOverlaySetType;
import stellarapi.api.gui.overlay.IRawOverlayElement;
import stellarapi.api.gui.overlay.IRawOverlaySet;
import stellarapi.api.gui.pos.ElementPos;

public class OverlaySetDelegate implements IRawOverlaySet {

	private OverlayManager manager;
	private IOverlaySetType type;
	private Map<ElementPos, String> displayedIdMap = Maps.newHashMap();

	public OverlaySetDelegate(OverlayManager manager, IOverlaySetType type) {
		this.manager = manager;
		this.type = type;
	}

	Collection<String> getDisplayedIds() {
		return displayedIdMap.values();
	}

	boolean containsId(String elementId) {
		return displayedIdMap.containsValue(elementId);
	}

	void resetPosition(String elementId, ElementPos pos) {
		displayedIdMap.values().remove(elementId);
		displayedIdMap.put(pos, elementId);
	}

	boolean containsPosition(ElementPos pos) {
		return displayedIdMap.containsKey(pos);
	}

	boolean canSetPos(String elementId, ElementPos pos) {
		return !displayedIdMap.containsKey(pos) || displayedIdMap.get(pos) == elementId;
	}

	void addToDisplay(String elementId, ElementPos position) {
		displayedIdMap.put(position, elementId);
	}

	@Override
	public IOverlaySetType getType() {
		return this.type;
	}

	@Override
	public void setDisplayed() {
		manager.switchDisplaySet(this);
	}

	@Override
	public boolean doesContain(IRawOverlayElement element) {
		return displayedIdMap.containsValue(element.getId());
	}

	@Override
	public ImmutableList<IRawOverlayElement> getContainedElements() {
		return ImmutableList.<IRawOverlayElement> copyOf(manager.getContainedElements(this));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean addElement(IRawOverlayElement element) {
		ElementPos pos = new ElementPos(element.getCurrentHorizontalPos(), element.getCurrentVerticalPos());
		if (this.canSetPos(element.getId(), pos)) {
			if (type.isMain())
				element.setVisibleOnMain(true);
			this.addToDisplay(element.getId(), pos);
			return true;
		} else
			return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void removeElement(IRawOverlayElement element) {
		if (!this.doesContain(element))
			return;

		if (type.isMain())
			element.setVisibleOnMain(false);

		ElementPos pos = new ElementPos(element.getCurrentHorizontalPos(), element.getCurrentVerticalPos());
		displayedIdMap.remove(pos);
	}
}
