package stellarapi.impl.celestial;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stellarapi.api.celestials.CelestialCollection;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.pack.ICelestialScene;
import stellarapi.api.render.IAdaptiveRenderer;
import stellarapi.api.view.IAtmosphereEffect;
import stellarapi.api.view.ICCoordinates;
import stellarapi.api.world.ICelestialHelper;

public class DefaultCelestialScene implements ICelestialScene {
	private final World world;
	private final CelestialObject sun, moon;

	public DefaultCelestialScene(World world) {
		this.world = world;
		this.sun = new DefaultSun();
		this.moon = new DefaultMoon(world);
	}

	@Override
	public void onRegisterCollection(Consumer<CelestialCollection> colRegistry,
			BiConsumer<IEffectorType, CelestialObject> effRegistry) {
		DefaultCollectionVanilla vanillaCollection = new DefaultCollectionVanilla(this.sun, this.moon);
		colRegistry.accept(vanillaCollection);
		effRegistry.accept(IEffectorType.Light, this.sun);
		effRegistry.accept(IEffectorType.Tide, this.moon);
	}

	@Override
	public ICCoordinates createCoordinates() {
		return new DefaultCoordinateVanilla(this.world);
	}

	@Override
	public IAtmosphereEffect createAtmosphereEffect() {
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
