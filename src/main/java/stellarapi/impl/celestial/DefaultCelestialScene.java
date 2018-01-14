package stellarapi.impl.celestial;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.ICelestialHelper;
import stellarapi.api.ICelestialScene;
import stellarapi.api.ISkyEffect;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.render.IAdaptiveRenderer;

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
	public ICelestialHelper createCelestialHelper() { return null; }

	@Override
	public NBTTagCompound serializeNBT() { return new NBTTagCompound(); }

	@Override
	public void deserializeNBT(NBTTagCompound nbt) { }

	@Override
	public void prepare() { }

	@Override
	public IAdaptiveRenderer createSkyRenderer() { return null; }

}
