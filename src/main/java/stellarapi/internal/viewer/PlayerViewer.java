package stellarapi.internal.viewer;

import net.minecraft.entity.player.EntityPlayer;
import stellarapi.api.instruments.IDetector;
import stellarapi.api.instruments.IMount;
import stellarapi.api.instruments.IOpticFilter;
import stellarapi.api.instruments.IOpticInstrument;
import stellarapi.api.viewer.IOpticalViewer;

public class PlayerViewer implements IOpticalViewer {

	private EntityPlayer viewer;

	public PlayerViewer(EntityPlayer player) {
		this.viewer = player;
	}

	@Override
	public IOpticInstrument getInstrument(EntityPlayer origin) {
		// TODO PlayerViewer fill in these methods
		return null;
	}

	@Override
	public IOpticFilter getFilter(EntityPlayer origin) {
		return null;
	}

	@Override
	public IMount getMount(EntityPlayer origin) {
		return null;
	}

	@Override
	public IDetector getEyeDetector(EntityPlayer origin) {
		return null;
	}
}
