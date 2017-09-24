package stellarapi.internal.reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;
import stellarapi.internal.viewer.PlayerView;

public class CPlayerViewReference implements ICapabilityProvider {

	private final PlayerView view;

	public CPlayerViewReference(EntityPlayer player) {
		this.view = new PlayerView(player);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SAPICapabilities.PLAYER_VIEW;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.PLAYER_VIEW)
			return SAPICapabilities.PLAYER_VIEW.cast(this.view);
		else return null;
	}

}
