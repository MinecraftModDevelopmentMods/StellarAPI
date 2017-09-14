package stellarapi.internal.coordinates;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.CoordContext;
import stellarapi.api.coordinates.EnumContextType;
import stellarapi.api.coordinates.ILocalCoordinates;
import stellarapi.api.lib.math.Matrix4;
import stellarapi.api.patch.BasePatchHandler;

public class CLocalCoordinates implements ILocalCoordinates {

	/** The provider */
	private ICapabilityProvider provider;

	/** True iff this capability is on world */
	private boolean isForWorld;


	private long lastTracked;
	private Map<CCoordinates, Matrix4> lastResultCache;

	private long offsetLastTracked;
	private Map<CCoordinates, Matrix4> offsetLastResultCache;

	// TODO remove these params for default implementation?
	public CLocalCoordinates(ICapabilityProvider provider, boolean isForWorld) {
		this.provider = provider;
		this.isForWorld = isForWorld;
	}

	@Override
	public Matrix4 getTransformMatrix(CCoordinates coord, CoordContext baseContext) {
		if(processContext(baseContext))
			return CoordContext.getEvaluator(baseContext).getTransformMatrix(coord, baseContext);

		// TODO LocalCoordinates calculate
		return null;
	}

	@Override
	public Matrix4 getTransformMatrix(CCoordinates coord, CoordContext baseContext, float partialTicks) {
		if(processContext(baseContext))
			return CoordContext.getEvaluator(baseContext).getTransformMatrix(coord, baseContext, partialTicks);

		baseContext.setTime(baseContext.getTime()+1L);

		/// TODO LocalCoordinates calculate
		return null;
	}

	/** 
	 * Processes context.
	 * returns true if this should be called again on the position-specific instance.
	 *  */
	private boolean processContext(CoordContext base) {
		if(BasePatchHandler.isLocationSpecific()) {
			if(this.isForWorld) {
				if(!base.supportContext(EnumContextType.POSITION)) {
					EntityPlayer defaultPlayer = BasePatchHandler.getDefaultPlayer();

					if(defaultPlayer != null) {
						base.setPosition(defaultPlayer, defaultPlayer.getPositionVector());
						return true;
					} else {
						base.setPosition(this.provider, new Vec3d(0,0,0));
						return false;
					}
				} else return !(base.getLocalProvider() instanceof World);
			} else return false;
		} else return !this.isForWorld;
	}

}
