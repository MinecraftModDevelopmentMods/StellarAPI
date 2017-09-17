package stellarapi;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import stellarapi.api.SAPIReference;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.atmosphere.IAtmSystem;
import stellarapi.api.atmosphere.IWorldAtmosphere;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.ICelestialScene;
import stellarapi.api.celestials.ICelestialSystem;
import stellarapi.api.coordinates.ICoordSystem;
import stellarapi.api.coordinates.ILocalCoordinates;
import stellarapi.internal.atmosphere.CAtmSystem;
import stellarapi.internal.atmosphere.CAtmosphere;
import stellarapi.internal.celestial.CelestialScene;
import stellarapi.internal.celestial.CelestialSystem;
import stellarapi.internal.coordinates.CCoordSystem;
import stellarapi.internal.coordinates.CLocalCoordinates;
import stellarapi.internal.reference.CWorldReference;
import stellarapi.internal.reference.CWorldSetReference;
import worldsets.api.WAPIReference;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

public class SAPICapsHook {

	private static final ResourceLocation CAPS =
			new ResourceLocation(SAPIReference.modid, "capabilities");

	public void registerCapabilities() {
		// TODO default implementations & some save/load. they are not right for now
		CapabilityManager.INSTANCE.register(ILocalCoordinates.class,
				new IStorage<ILocalCoordinates>() {
					@Override
					public NBTBase writeNBT(Capability<ILocalCoordinates> capability, ILocalCoordinates instance, EnumFacing side) {
						return null;
					}
					@Override
					public void readNBT(Capability<ILocalCoordinates> capability, ILocalCoordinates instance, EnumFacing side, NBTBase nbt) {
					}
		}, CLocalCoordinates.class);

		CapabilityManager.INSTANCE.register(ICoordSystem.class,
				new IStorage<ICoordSystem>() {
			@Override
			public NBTBase writeNBT(Capability<ICoordSystem> capability, ICoordSystem instance, EnumFacing side) {
				return new NBTTagString(instance.getProviderID().toString());
			}
			@Override
			public void readNBT(Capability<ICoordSystem> capability, ICoordSystem instance, EnumFacing side, NBTBase nbt) {
				if(nbt instanceof NBTTagString) {
					NBTTagString property = (NBTTagString) nbt;
					instance.setProviderID(new ResourceLocation(property.getString()));
				}
			}
		}, CCoordSystem.class);
	

		CapabilityManager.INSTANCE.register(ICelestialScene.class,
				new IStorage<ICelestialScene>() {
			@Override
			public NBTBase writeNBT(Capability<ICelestialScene> capability, ICelestialScene instance, EnumFacing side) {
				return null;
			}
			@Override
			public void readNBT(Capability<ICelestialScene> capability, ICelestialScene instance, EnumFacing side, NBTBase nbt) {
			}
		}, CelestialScene.class);

		CapabilityManager.INSTANCE.register(ICelestialSystem.class,
				new IStorage<ICelestialSystem>() {
			@Override
			public NBTBase writeNBT(Capability<ICelestialSystem> capability, ICelestialSystem instance, EnumFacing side) {
				NBTTagCompound comp = new NBTTagCompound();
				for(CelestialType type : SAPIRegistries.getCelestialTypeRegistry()) {
					if(instance.isAbsent(type))
						continue;
					NBTTagCompound subComp = instance.getCollection(type).serializeNBT();
					subComp.setString("theProviderID", instance.getProviderID(type).toString());
					comp.setTag(type.delegate.name().toString(), subComp);
				}
				return comp;
			}
			@Override
			public void readNBT(Capability<ICelestialSystem> capability, ICelestialSystem instance, EnumFacing side, NBTBase nbt) {
				NBTTagCompound comp = (NBTTagCompound) nbt;
				for(CelestialType type : SAPIRegistries.getOrderedTypes()) {
					if(!comp.hasKey(type.delegate.name().toString(), comp.getId())) {
						instance.validateNset(type, null);
						continue;
					}
					NBTTagCompound subComp = comp.getCompoundTag(type.delegate.name().toString());
					instance.validateNset(type, new ResourceLocation(subComp.getString("theProviderID")));
					instance.getCollection(type).deserializeNBT(subComp);
				}
			}
		}, CelestialSystem.class);


		CapabilityManager.INSTANCE.register(IWorldAtmosphere.class,
				new IStorage<IWorldAtmosphere>() {
			@Override
			public NBTBase writeNBT(Capability<IWorldAtmosphere> capability, IWorldAtmosphere instance, EnumFacing side) {
				return null;
			}
			@Override
			public void readNBT(Capability<IWorldAtmosphere> capability, IWorldAtmosphere instance, EnumFacing side, NBTBase nbt) {
			}
		}, CAtmosphere.class);

		CapabilityManager.INSTANCE.register(IAtmSystem.class,
				new IStorage<IAtmSystem>() {
			@Override
			public NBTBase writeNBT(Capability<IAtmSystem> capability, IAtmSystem instance, EnumFacing side) {
				return null;
			}
			@Override
			public void readNBT(Capability<IAtmSystem> capability, IAtmSystem instance, EnumFacing side, NBTBase nbt) {
			}
		}, CAtmSystem.class);
	}


	public void attachWorldCaps(AttachCapabilitiesEvent<World> worldCaps) {
		World world = worldCaps.getObject();
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		worldCaps.addCapability(CAPS, new CWorldReference(world));
	}

	public void attachWorldSetCaps(AttachCapabilitiesEvent<WorldSetInstance> worldSetCaps) {
		WorldSetInstance worldSetInstance = worldSetCaps.getObject();
		WorldSet worldSet = worldSetInstance.getWorldSet();
		worldSetCaps.addCapability(CAPS, new CWorldSetReference(worldSetInstance));
	}

	/*@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load loadEvent) {
		World world = loadEvent.getWorld();

		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);

		if(worldSet == null)
			return;

		WorldSetInstance instance = WAPIReference.getWorldSetInstance(world, worldSet);
	}*/

}