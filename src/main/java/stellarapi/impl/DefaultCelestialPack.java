package stellarapi.impl;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.world.World;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.ICelestialPack;
import stellarapi.api.ISkyEffect;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;

public class DefaultCelestialPack implements ICelestialPack {

	@Override
	public String getPackName() {
		return "Stellar API";
	}

	@Override
	public void onRegisterCollection(World world, Consumer<ICelestialCollection> colRegistry,
			BiConsumer<IEffectorType, ICelestialObject> effRegistry) {
		DefaultCollectionVanilla vanillaCollection = new DefaultCollectionVanilla(world);
		colRegistry.accept(vanillaCollection);
		effRegistry.accept(IEffectorType.Light, vanillaCollection.sun);
		effRegistry.accept(IEffectorType.Tide, vanillaCollection.moon);
	}

	@Override
	public ICelestialCoordinates createCoordinates(World world) {
		return new DefaultCoordinateVanilla(world);
	}

	@Override
	public ISkyEffect createSkyEffect(World world) {
		return new DefaultSkyVanilla();
	}

}
