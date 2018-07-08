package stellarapi.reference;

import java.util.Collections;
import java.util.concurrent.Callable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.StellarAPI;
import stellarapi.api.IReference;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.celestials.CelestialCollections;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.interact.IFilter;
import stellarapi.api.interact.IScope;
import stellarapi.api.interact.NakedFilter;
import stellarapi.api.interact.NakedScope;
import stellarapi.api.pack.ICelestialScene;
import stellarapi.api.view.IAtmosphereEffect;
import stellarapi.api.view.ICCoordinates;
import stellarapi.api.world.ICelestialWorld;
import stellarapi.api.world.IWorldProviderReplacer;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.api.world.worldset.WorldSets;
import stellarapi.example.world.WorldReplacerDefault;

public class SAPIReferenceHandler implements IReference {

	public void initialize() {
		CapabilityManager.INSTANCE.register(ICelestialWorld.class, new Capability.IStorage<ICelestialWorld>() {
			@Override
			public NBTBase writeNBT(Capability<ICelestialWorld> capability, ICelestialWorld instance, EnumFacing side) { return null; }
			@Override
			public void readNBT(Capability<ICelestialWorld> capability, ICelestialWorld instance, EnumFacing side, NBTBase nbt) { }
		}, new Callable<ICelestialWorld>() {
			// Mundane default implementation
			@Override
			public ICelestialWorld call() throws Exception {
				return new ICelestialWorld() {
					@Override
					public ICCoordinates getCoordinate() { return null; }
					@Override
					public IAtmosphereEffect getSkyEffect() { return null; }
					private final CelestialCollections collections = new CelestialCollections(ImmutableList.of());
					@Override
					public CelestialCollections getCollections() { return this.collections; }
					@Override
					public ImmutableSet<IEffectorType> getEffectorTypeSet() { return ImmutableSet.of(); }
					private final CelestialEffectors effectors = new CelestialEffectors(Collections.emptyList());
					@Override
					public CelestialEffectors getCelestialEffectors(IEffectorType type) {
						return this.effectors;
					}
				};
			}
		});

		CapabilityManager.INSTANCE.register(IScope.class, new Capability.IStorage<IScope>() {
			@Override
			public NBTBase writeNBT(Capability<IScope> capability, IScope instance, EnumFacing side) { return null; }
			@Override
			public void readNBT(Capability<IScope> capability, IScope instance, EnumFacing side, NBTBase nbt) { }
		}, NakedScope::new);

		CapabilityManager.INSTANCE.register(IFilter.class, new Capability.IStorage<IFilter>() {
			@Override
			public NBTBase writeNBT(Capability<IFilter> capability, IFilter instance, EnumFacing side) { return null; }
			@Override
			public void readNBT(Capability<IFilter> capability, IFilter instance, EnumFacing side, NBTBase nbt) { }
		}, NakedFilter::new);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public ICelestialWorld getCelestialWorld(World world) {
		return world.getCapability(SAPICapabilities.CELESTIAL_CAPABILITY, null);
	}

	@SubscribeEvent
	public void onGatherWorldCapability(AttachCapabilitiesEvent<World> event) {
		boolean hasPack = false;
		for(WorldSet wSet : WorldSets.appliedWorldSets(event.getObject())) {
			if(SAPIReferences.getCelestialPack(wSet) != null) {
				hasPack = true;
				break;
			}
		}

		if(hasPack)
			event.addCapability(new ResourceLocation(SAPIReferences.MODID, "celestials"),
					new SAPIWorldCaps(event.getObject()));
	}

	@Override
	public IWorldProviderReplacer getDefaultReplacer() {
		return WorldReplacerDefault.INSTANCE;
	}

	@Override
	public World getDefaultWorld(boolean isRemote) {
		if(isRemote)
			return StellarAPI.PROXY.getClientWorld();
		else return DimensionManager.getWorld(0);
	}


	@Override
	public ICelestialScene getActivePack(World world) {
		ICelestialWorld celWorld = world.getCapability(SAPICapabilities.CELESTIAL_CAPABILITY, EnumFacing.UP);
		if(celWorld instanceof CelestialPackManager)
			return ((CelestialPackManager) celWorld).getScene();
		else return null;
	}
}
