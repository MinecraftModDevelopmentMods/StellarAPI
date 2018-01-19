package stellarapi.reference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.StellarAPI;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.ICelestialScene;
import stellarapi.api.ICelestialWorld;
import stellarapi.api.IPerEntityReference;
import stellarapi.api.IPerWorldReference;
import stellarapi.api.IReference;
import stellarapi.api.ISkyEffect;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.event.interact.CheckEntityOpticalViewerEvent;
import stellarapi.api.interact.IOpticalProperties;
import stellarapi.api.lib.config.IConfigHandler;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedFilter;
import stellarapi.api.optics.NakedScope;
import stellarapi.api.world.IWorldProviderReplacer;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.api.world.worldset.WorldSetFactory;
import stellarapi.example.world.WorldReplacerDefault;

public class SAPIReferenceHandler implements IReference, IConfigHandler {

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
					public ICelestialCoordinates getCoordinate() {
						return null;
					}

					@Override
					public ISkyEffect getSkyEffect() {
						return null;
					}

					@Override
					public ImmutableSet<IEffectorType> getEffectorTypeSet() {
						return ImmutableSet.of();
					}

					private final CelestialEffectors effectors = new CelestialEffectors(Collections.emptyList());
					@Override
					public CelestialEffectors getCelestialEffectors(IEffectorType type) {
						return this.effectors;
					}
				};
			}
		});

		CapabilityManager.INSTANCE.register(IOpticalViewer.class, new Capability.IStorage<IOpticalViewer>() {
			@Override
			public NBTBase writeNBT(Capability<IOpticalViewer> capability, IOpticalViewer instance, EnumFacing side) { return null; }
			@Override
			public void readNBT(Capability<IOpticalViewer> capability, IOpticalViewer instance, EnumFacing side, NBTBase nbt) { }
		}, new Callable<IOpticalViewer>() {
			@Override
			public IOpticalViewer call() throws Exception {
				return new IOpticalViewer() {
					@Override
					public IViewScope getScope() {
						return null;
					}

					@Override
					public IOpticalFilter getFilter() {
						return null;
					}
				};
			}
		});
		
		CapabilityManager.INSTANCE.register(IOpticalProperties.class, new Capability.IStorage<IOpticalProperties>() {
			public NBTBase writeNBT(Capability<IOpticalProperties> capability, IOpticalProperties instance, EnumFacing side) {
				return null;
			}

			public void readNBT(Capability<IOpticalProperties> capability, IOpticalProperties instance, EnumFacing side,
					NBTBase nbt) {
			}
		}, new Callable<IOpticalProperties>() {
			@Override
			public IOpticalProperties call() throws Exception {
				return new IOpticalProperties() {
					@Override
					public boolean isFilter() {
						return false;
					}

					@Override
					public IOpticalFilter getFilter(EntityLivingBase viewer) {
						return null;
					}

					@Override
					public boolean isScope() {
						return false;
					}

					@Override
					public IViewScope getScope(EntityLivingBase viewer) {
						return null;
					}
				};
			}
		});

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public IPerWorldReference getPerWorldReference(World world) {
		ICelestialWorld celWorld = world.getCapability(SAPICapabilities.CELESTIAL_CAPABILITY, EnumFacing.UP);
		if(celWorld instanceof IPerWorldReference)
			return (IPerWorldReference) celWorld;
		else return null;
	}

	@Override
	public IPerEntityReference getPerEntityReference(Entity entity) {
		IOpticalViewer viewer = entity.getCapability(SAPICapabilities.VIEWER_CAPABILITY, EnumFacing.DOWN);
		if (viewer instanceof IPerEntityReference)
			return (IPerEntityReference) viewer;
		else return null;
	}

	@SubscribeEvent
	public void onGatherWorldCapability(AttachCapabilitiesEvent<World> event) {
		boolean hasPack = false;
		for(WorldSet wSet : SAPIReferences.appliedWorldSets(event.getObject())) {
			if(SAPIReferences.getCelestialPack(wSet) != null) {
				hasPack = true;
				break;
			}
		}

		event.addCapability(new ResourceLocation(SAPIReferences.MODID, "celestials"),
				new SAPIWorldCaps(event.getObject()));
	}

	@SubscribeEvent
	public void onGatherEntityCapability(AttachCapabilitiesEvent<Entity> event) {
		CheckEntityOpticalViewerEvent check = new CheckEntityOpticalViewerEvent(event.getObject());
		SAPIReferences.getEventBus().post(check);
		if (check.isOpticalEntity())
			event.addCapability(new ResourceLocation(SAPIReferences.MODID, "viewer"),
					new SAPIEntityCaps(event.getObject()));
	}

	@Override
	public IViewScope getDefaultScope() {
		return new NakedScope();
	}

	@Override
	public IOpticalFilter getDefaultFilter() {
		return new NakedFilter();
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


	// TODO Move this worldset related things to somewhere else
	private Map<ResourceLocation, WorldSetFactory> factories = Maps.newHashMap();
	private List<WorldSet> worldSets = Lists.newArrayList();
	private Map<ResourceLocation, WorldSet[]> generated = Maps.newHashMap();

	@Override
	public void registerFactory(WorldSetFactory factory) {
		factories.put(factory.getLocation(), factory);
	}


	@Override
	public ImmutableList<WorldSet> getAllWorldSets() {
		return ImmutableList.copyOf(this.worldSets);
	}

	@Override
	public WorldSet[] getGeneratedWorldSets(ResourceLocation location) {
		return generated.get(location);
	}

	@Override
	public WorldSet getPrimaryWorldSet(World world) {
		return PerWorldData.getWorldSets(world).appliedWorldSets.get(0);
	}

	@Override
	public ImmutableList<WorldSet> appliedWorldSets(World world) {
		return PerWorldData.getWorldSets(world).appliedWorldSets;
	}


	@Override
	public ICelestialScene getActivePack(World world) {
		ICelestialWorld celWorld = world.getCapability(SAPICapabilities.CELESTIAL_CAPABILITY, EnumFacing.UP);
		if(celWorld instanceof CelestialPackManager)
			return ((CelestialPackManager) celWorld).getScene();
		else return null;
	}


	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configuration for WorldSets.");
		config.setCategoryLanguageKey(category, "config.category.worldset");
		config.setCategoryRequiresMcRestart(category, true);

		for(WorldSetFactory factory : factories.values()) {
			String title = factory.getTitle();
			if(title != null) {
				factory.configure(config, config.getCategory(
						category + Configuration.CATEGORY_SPLITTER + title));
			}
		}
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		// This requires MC restart. Don't respond any attempts after the first loading.
		if(!worldSets.isEmpty())
			return;

		for(Map.Entry<ResourceLocation, WorldSetFactory> entry : factories.entrySet()) {
			WorldSetFactory factory = entry.getValue();
			String title = factory.getTitle();
			WorldSet[] sets = factory.generate(title != null? config.getCategory(
					category + Configuration.CATEGORY_SPLITTER + title) : null);
			worldSets.addAll(Arrays.asList(sets));
			generated.put(entry.getKey(), sets);
		}
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		// Unable to save to config
	}

}
