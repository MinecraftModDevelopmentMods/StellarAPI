package stellarapi.impl.celestial;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.ICelestialScene;
import stellarapi.api.ISkyEffect;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;

public class DefaultCelestialScene implements ICelestialScene {
	private final World world;

	public DefaultCelestialScene(World world) {
		this.world = world;
		
	}

	@Override
	public void onRegisterCollection(Consumer<ICelestialCollection> colRegistry,
			BiConsumer<IEffectorType, ICelestialObject> effRegistry) {
		DefaultCollectionVanilla vanillaCollection = new DefaultCollectionVanilla(this.world);
		colRegistry.accept(vanillaCollection);
		effRegistry.accept(IEffectorType.Light, vanillaCollection.sun);
		effRegistry.accept(IEffectorType.Tide, vanillaCollection.moon);
	}

	@Override
	public ICelestialCoordinates createCoordinates() {
		return new DefaultCoordinateVanilla(this.world);
	}

	@Override
	public ISkyEffect createSkyEffect() {
		return new DefaultSkyVanilla();
	}

	@Override
	public WorldProvider replaceWorldProvider(WorldProvider provider) {
		return null;
	}

}
