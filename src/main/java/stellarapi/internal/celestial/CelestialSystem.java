package stellarapi.internal.celestial;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import stellarapi.StellarAPI;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.ICelestialSystem;
import stellarapi.api.celestials.collection.CelestialCollection;
import stellarapi.api.celestials.collection.ICelestialProvider;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

public class CelestialSystem implements ICelestialSystem {

	private final WorldSet worldSet;
	private final IProviderRegistry<ICelestialProvider> providerRegistry;

	private Map<RegistryDelegate<CelestialType>, ResourceLocation> providerMap = Maps.newHashMap();
	private Map<RegistryDelegate<CelestialType>, CelestialCollection> collectionMap = Maps.newHashMap();

	public CelestialSystem(WorldSet worldSet) {
		this.worldSet = worldSet;
		this.providerRegistry = ProviderRegistry.findRegistry(ICelestialProvider.class);
	}

	@Override
	public boolean isAbsent(CelestialType type) {
		return !providerMap.containsKey(type.delegate);
	}

	@Override
	public CelestialCollection getCollection(CelestialType type) {
		return collectionMap.get(type.delegate);
	}

	@Override
	public ResourceLocation getProviderID(CelestialType type) {
		return providerMap.get(type.delegate);
	}

	@Override
	public void validateNset(CelestialType type, ResourceLocation providerID) {
		if(providerID == null) {
			providerMap.remove(type.delegate);
			collectionMap.remove(type.delegate);
			return;
		}

		if(!providerRegistry.containsKey(providerID))
			throw new IllegalArgumentException(String.format("There's no provider for providerID %s", providerID));

		providerMap.put(type.delegate, providerID);
		this.validate(type);

		if(providerMap.containsKey(type.delegate)) {
			ICelestialProvider provider = providerRegistry.getProvider(providerMap.get(type.delegate));
			collectionMap.put(type.delegate, provider.generateCollection(type, this.worldSet));
		}
	}

	private void validate(CelestialType startingType) {
		if(startingType.hasParent() && providerMap.containsKey(startingType.delegate)) {
			ICelestialProvider provider = providerRegistry.getProvider(providerMap.get(startingType.delegate));
			if(!provider.parentDependency(startingType, this.worldSet)
					.apply(providerMap.get(startingType.getParent().delegate))) {
				providerMap.remove(startingType.delegate);
				collectionMap.remove(startingType.delegate);
				// TODO CelestialSystem log here
			}
		}

		for(CelestialType childType : startingType.getChildren())
			this.validate(childType);
	}
}