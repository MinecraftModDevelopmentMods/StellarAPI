package stellarapi.reference;

import java.util.concurrent.Callable;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.StellarAPI;
import stellarapi.api.IPerClientReference;
import stellarapi.api.IPerEntityReference;
import stellarapi.api.IPerWorldReference;
import stellarapi.api.IReference;
import stellarapi.api.StellarAPICapabilities;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.interact.CheckEntityOpticalViewerEvent;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.api.optics.IViewScope;

public class StellarAPIReferenceHandler implements IReference {
	
	public void initialize() {
		CapabilityManager.INSTANCE.register(IOpticalViewer.class,
				new Capability.IStorage<IOpticalViewer>() {
			public NBTBase writeNBT(Capability<IOpticalViewer> capability, IOpticalViewer instance, EnumFacing side) { return null; }
			public void readNBT(Capability<IOpticalViewer> capability, IOpticalViewer instance, EnumFacing side, NBTBase nbt) {}
		}, new Callable<IOpticalViewer>() {
			@Override
			public IOpticalViewer call() throws Exception {
				return new IOpticalViewer() {
					@Override
					public IViewScope getScope() { return null; }
					@Override
					public IOpticalFilter getFilter() { return null; }
				};
			}
		});
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public IPerWorldReference getPerWorldReference(World world) {
		return PerWorldManager.getPerWorldManager(world);
	}

	@Override
	public IPerEntityReference getPerEntityReference(Entity entity) {
		IOpticalViewer viewer = entity.getCapability(StellarAPICapabilities.VIEWER_CAPABILITY, EnumFacing.DOWN);
		if(viewer instanceof IPerEntityReference)
			return (IPerEntityReference) viewer;
		else return null;
	}

	@Override
	public IPerClientReference getPerClientReference() {
		return StellarAPI.proxy;
	}
	
	@SubscribeEvent
	public void onGatherEntityCapability(AttachCapabilitiesEvent.Entity event) {
		CheckEntityOpticalViewerEvent check = new CheckEntityOpticalViewerEvent(event.getEntity());
		StellarAPIReference.getEventBus().post(check);
		if(check.isOpticalEntity())
			event.addCapability(new ResourceLocation(StellarAPI.modid, "viewer"), new PerEntityManager(event.getEntity()));
	}

}
