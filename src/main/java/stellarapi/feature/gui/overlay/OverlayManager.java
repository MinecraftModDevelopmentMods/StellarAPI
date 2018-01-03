package stellarapi.feature.gui.overlay;

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import stellarapi.api.gui.overlay.EnumOverlayMode;
import stellarapi.api.gui.overlay.IOverlayElement;
import stellarapi.api.gui.overlay.IOverlayInjectable;
import stellarapi.api.gui.overlay.IOverlayManager;
import stellarapi.api.gui.overlay.IOverlaySetType;
import stellarapi.api.gui.overlay.IOverlayType;
import stellarapi.api.gui.overlay.IRawOverlayElement;
import stellarapi.api.gui.overlay.IRawOverlaySet;
import stellarapi.api.gui.overlay.PerOverlaySettings;
import stellarapi.api.gui.pos.ElementPos;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;
import worldsets.api.lib.config.ConfigManager;

@SuppressWarnings("rawtypes")
public class OverlayManager implements IOverlayManager, IOverlayInjectable {
	private Map<String, OverlayElementDelegate> elementMap = Maps.newHashMap();
	private List<OverlaySetDelegate> displaySets = Lists.newArrayList();
	private OverlaySetDelegate currentlyDisplayed;
	private OverlayContainer container;

	public OverlayManager(OverlayContainer container) {
		this.container = container;
	}

	@Override
	public void injectOverlaySet(IOverlaySetType type) {
		displaySets.add(new OverlaySetDelegate(this, type));
	}

	@Override
	public <E extends IOverlayElement<S>, S extends PerOverlaySettings> void injectOverlay(String id, String modid,
			IOverlayType<E, S> type, S settings, ConfigManager notified) {
		OverlayElementDelegate delegate = new OverlayElementDelegate<E, S>(type, settings, notified, this, id, modid);
		elementMap.put(id, delegate);
	}

	void initialize(Minecraft mc) {
		for (OverlayElementDelegate delegate : elementMap.values())
			delegate.initialize(mc);

		for (OverlaySetDelegate setDelegate : displaySets) {
			for (Map.Entry<String, OverlayElementDelegate> entry : elementMap.entrySet()) {
				if ((entry.getValue().getType().isUniversal())
						&& setDelegate.canSetPos(entry.getKey(), entry.getValue().getPosition()))
					setDelegate.addToDisplay(entry.getKey(), entry.getValue().getPosition());
			}

			if (!setDelegate.getType().isMain()) {
				for (Map.Entry<String, OverlayElementDelegate> entry : elementMap.entrySet()) {
					if ((setDelegate.getType().acceptOverlayByDefault(entry.getValue()))
							&& setDelegate.canSetPos(entry.getKey(), entry.getValue().getPosition()))
						setDelegate.addToDisplay(entry.getKey(), entry.getValue().getPosition());
				}
			} else {
				for (Map.Entry<String, OverlayElementDelegate> entry : elementMap.entrySet()) {
					if (entry.getValue().visibleOnMain()
							&& setDelegate.canSetPos(entry.getKey(), entry.getValue().getPosition()))
						setDelegate.addToDisplay(entry.getKey(), entry.getValue().getPosition());
				}
			}
		}
	}

	boolean hasNoDuplication(String elementId, EnumHorizontalPos horizontal, EnumVerticalPos vertical) {
		ElementPos pos = new ElementPos(horizontal, vertical);
		for (OverlaySetDelegate setDelegate : displaySets)
			if (setDelegate.containsId(elementId) && !setDelegate.canSetPos(elementId, pos))
				return false;

		return true;
	}

	void setElementPosOnDisplaySets(String elementId, ElementPos pos) {
		for (OverlaySetDelegate setDelegate : displaySets)
			if (setDelegate.containsId(elementId))
				setDelegate.resetPosition(elementId, pos);
	}

	void switchDisplaySet(OverlaySetDelegate setDelegate) {
		container.resetDisplayList(this.getContainedElements(setDelegate));
		this.currentlyDisplayed = setDelegate;
	}

	Iterable<OverlayElementDelegate> getContainedElements(OverlaySetDelegate setDelegate) {
		return Iterables.transform(setDelegate.getDisplayedIds(), new Function<String, OverlayElementDelegate>() {
			@Override
			public OverlayElementDelegate apply(String input) {
				return elementMap.get(input);
			}
		});
	}

	@Override
	public ImmutableList<IRawOverlaySet> getDisplayedSets() {
		return ImmutableList.<IRawOverlaySet> copyOf(displaySets);
	}

	@Override
	public IRawOverlaySet getCurrentDisplayedSet() {
		return this.currentlyDisplayed;
	}

	@Override
	public IRawOverlayElement getCurrentElement(int mouseX, int mouseY) {
		for (IRawOverlayElement element : container.getCurrentDisplayedList()) {
			int elementWidth = element.getWidth();
			int elementHeight = element.getHeight();

			if (element.getCurrentHorizontalPos().inRange(mouseX, container.getWidth(), elementWidth))
				if (element.getCurrentVerticalPos().inRange(mouseY, container.getHeight(), elementHeight))
					return element;
		}

		return null;
	}

	@Override
	public int getCurrentWidth() {
		return container.getWidth();
	}

	@Override
	public int getCurrentHeight() {
		return container.getHeight();
	}

	@Override
	public void switchMode(EnumOverlayMode enumOverlayMode) {
		container.switchMode(enumOverlayMode);
	}

	@Override
	public boolean isGamePaused() {
		return container.isGamePaused();
	}

	@Override
	public void setGamePaused(boolean pause) {
		container.setGamePaused(pause);
	}
}
