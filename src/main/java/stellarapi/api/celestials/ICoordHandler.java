package stellarapi.api.celestials;

import java.util.Map;

import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.lib.math.Vector3;

// TODO how to override the existing coordinates?
public interface ICoordHandler {
	public Map<CelestialType, Matrix4> coordinates(World world, long worldTime, Object context);
	public Object getContext(ICapabilityProvider provider, Vector3 worldPos);
	public ICapabilityProvider storage(World world, ICapabilityProvider provider);
}