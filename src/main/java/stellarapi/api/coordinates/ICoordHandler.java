package stellarapi.api.coordinates;

import com.google.common.base.Function;

import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.lib.math.Matrix4;
import stellarapi.api.lib.math.Vector3;

// TODO how to override the existing coordinates?
public interface ICoordHandler {
	public void registerCoords(IForgeRegistry<Coordinates> registry);

	public Function<CelestialType, Matrix4> coordGetter(World world, long worldTime, Object context);
	public Object getContext(ICapabilityProvider provider, Vector3 worldPos);
}