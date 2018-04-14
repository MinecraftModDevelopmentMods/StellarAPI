package stellarapi.api.coordinates;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CoordContext {
	/** The world. */
	public final World world;

	/** The viewer which will contain the data. */
	public final ICapabilityProvider viewerProvider;

	/** The actual position. This should be always <code>null</code> unless it's specified/altered. */
	public final @Nullable BlockPos pos;

	public CoordContext(World world, ICapabilityProvider provider, @Nullable BlockPos pos) {
		this.world = world;
		this.viewerProvider = provider;
		this.pos = pos;
	}
}
