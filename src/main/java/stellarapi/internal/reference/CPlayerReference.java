package stellarapi.internal.reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;
import stellarapi.internal.viewer.PlayerViewer;

public class CPlayerReference implements ICapabilityProvider {

	private final PlayerViewer viewer;

	public CPlayerReference(EntityPlayer player) {
		this.viewer = new PlayerViewer(player);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SAPICapabilities.OPTICAL_VIEWER;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.OPTICAL_VIEWER)
			return SAPICapabilities.OPTICAL_VIEWER.cast(this.viewer);
		else return null;
	}

}
