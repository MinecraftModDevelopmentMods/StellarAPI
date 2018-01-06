package stellarapi.feature.celestial.tweakable;

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
import stellarapi.example.CelestialHelperExample;
import stellarapi.example.WorldProviderExample;
import stellarapi.impl.celestial.DefaultSkyVanilla;

public class SAPICelestialScene implements ICelestialScene {
	private final World world;

	private final CelestialHelperExample helper;
	private final SAPICollection collection;
	private final ICelestialCoordinates coordinate;
	private final ISkyEffect skyEffect;

	public SAPICelestialScene(World world, double dayLength, double monthInDay) {
		this.world = world;

		this.collection = new SAPICollection(this.world, dayLength, monthInDay);
		this.coordinate = new SAPICoordinate(this.world, dayLength);
		this.skyEffect = new DefaultSkyVanilla();
		this.helper = new CelestialHelperExample(
				1.0f, 1.0f, collection.sun, collection.moon, null, null);
	}

	@Override
	public void onRegisterCollection(Consumer<ICelestialCollection> colRegistry,
			BiConsumer<IEffectorType, ICelestialObject> effRegistry) {
		colRegistry.accept(this.collection);
		effRegistry.accept(IEffectorType.Light, collection.sun);
		effRegistry.accept(IEffectorType.Tide, collection.moon);
	}

	@Override
	public ICelestialCoordinates createCoordinates() {
		return this.coordinate;
	}

	@Override
	public ISkyEffect createSkyEffect() {
		return this.skyEffect;
	}

	@Override
	public WorldProvider replaceWorldProvider(WorldProvider provider) {
		return new WorldProviderExample(provider, this.helper);
	}

}
