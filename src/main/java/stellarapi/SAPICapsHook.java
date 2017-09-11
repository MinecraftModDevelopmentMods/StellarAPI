package stellarapi;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPIReference;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.coordinates.ICoordSystem;
import stellarapi.api.coordinates.ILocalCoordinates;
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
		// TODO default implementations; they are not right for now
		CapabilityManager.INSTANCE.register(ILocalCoordinates.class,
				new IStorage<ILocalCoordinates>() {
					@Override
					public NBTBase writeNBT(Capability<ILocalCoordinates> capability, ILocalCoordinates instance, EnumFacing side) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void readNBT(Capability<ILocalCoordinates> capability, ILocalCoordinates instance, EnumFacing side, NBTBase nbt) {
						// TODO Auto-generated method stub
						
					}
		}, CLocalCoordinates.class);

		CapabilityManager.INSTANCE.register(ICoordSystem.class,
				new IStorage<ICoordSystem>() {
			@Override
			public NBTBase writeNBT(Capability<ICoordSystem> capability, ICoordSystem instance, EnumFacing side) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setString("providerID", instance.getProviderID().toString());
				compound.setTag("provider", instance.getHandler(ICoordHandler.class).serializeNBT());
				return compound;
			}
			@Override
			public void readNBT(Capability<ICoordSystem> capability, ICoordSystem instance, EnumFacing side, NBTBase nbt) {
				NBTTagCompound compound = (NBTTagCompound) nbt;
				if(compound.hasKey("providerID")) {
					instance.setProviderID(new ResourceLocation(compound.getString("providerID")));
					if(compound.hasKey("provider"))
						instance.getHandler(ICoordHandler.class).deserializeNBT(compound.getCompoundTag("provider"));
				}
			}
		}, CCoordSystem.class);
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