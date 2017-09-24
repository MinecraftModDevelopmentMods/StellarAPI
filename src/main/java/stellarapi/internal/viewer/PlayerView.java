package stellarapi.internal.viewer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import stellarapi.StellarAPI;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.instruments.IDetector;
import stellarapi.api.instruments.IMount;
import stellarapi.api.instruments.IOpticFilter;
import stellarapi.api.instruments.IOpticInstrument;
import stellarapi.api.viewer.IOpticalViewer;
import stellarapi.api.viewer.IPlayerView;

public class PlayerView implements IPlayerView {

	private final EntityPlayer player;

	public PlayerView(EntityPlayer player) {
		this.player = player;
	}

	private Entity getViewer() {
		if(this.player instanceof EntityPlayerMP) {
			EntityPlayerMP mpPlayer = (EntityPlayerMP) this.player;
			// TODO PlayerView provider here to provide the appropriate viewer entity
			return mpPlayer.getSpectatingEntity();
		} else return StellarAPI.proxy.getRenderViewEntity();
	}

	@Override
	public IOpticInstrument getInstrument() {
		Entity viewer = this.getViewer();

		if(viewer.hasCapability(SAPICapabilities.OPTICAL_VIEWER, null)) {
			IOpticalViewer opViewer = viewer.getCapability(SAPICapabilities.OPTICAL_VIEWER, null);
			return opViewer.getInstrument(this.player);
		}

		// If the viewer has no optical properties, falls back to no instrument at all
		else return null;
	}

	@Override
	public IOpticFilter getFilter() {
		Entity viewer = this.getViewer();

		if(viewer.hasCapability(SAPICapabilities.OPTICAL_VIEWER, null)) {
			IOpticalViewer opViewer = viewer.getCapability(SAPICapabilities.OPTICAL_VIEWER, null);
			return opViewer.getFilter(this.player);
		}

		// If the viewer has no optical properties, falls back to no filter at all
		return null;
	}

	@Override
	public IMount getMount() {
		Entity viewer = this.getViewer();
		IOpticalViewer opViewer;

		if(viewer.hasCapability(SAPICapabilities.OPTICAL_VIEWER, null))
			opViewer = viewer.getCapability(SAPICapabilities.OPTICAL_VIEWER, null);
		else opViewer = player.getCapability(SAPICapabilities.OPTICAL_VIEWER, null);

		// Mount is determined based on the player.
		return opViewer.getMount(this.player);
	}

	@Override
	public IDetector getEyeDetector() {
		Entity viewer = this.getViewer();
		IOpticalViewer opViewer;

		if(viewer.hasCapability(SAPICapabilities.OPTICAL_VIEWER, null))
			opViewer = viewer.getCapability(SAPICapabilities.OPTICAL_VIEWER, null);
		else opViewer = player.getCapability(SAPICapabilities.OPTICAL_VIEWER, null);

		// Detector is determined based on the player.
		return opViewer.getEyeDetector(this.player);
	}
}
