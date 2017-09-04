package stellarapi.reference;

import java.util.concurrent.Callable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import stellarapi.api.IClientReference;
import stellarapi.api.IReference;
import stellarapi.api.IUpdatedOpticalViewer;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReference;
import stellarapi.api.event.interact.CheckEntityOpticalViewerEvent;
import stellarapi.api.interact.IOpticalProperties;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedFilter;
import stellarapi.api.optics.NakedScope;

public class SAPIReferenceHandler implements IReference {

	public void initialize() {
		CapabilityManager.INSTANCE.register(IOpticalViewer.class, new Capability.IStorage<IOpticalViewer>() {
			public NBTBase writeNBT(Capability<IOpticalViewer> capability, IOpticalViewer instance, EnumFacing side) {
				return null;
			}

			public void readNBT(Capability<IOpticalViewer> capability, IOpticalViewer instance, EnumFacing side,
					NBTBase nbt) {
			}
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
	public IUpdatedOpticalViewer getUpdatedViewerSafe(Entity entity) {
		IOpticalViewer viewer = entity.getCapability(SAPICapabilities.VIEWER_CAPABILITY, EnumFacing.DOWN);
		if (viewer instanceof IUpdatedOpticalViewer)
			return (IUpdatedOpticalViewer) viewer;
		else return null;
	}

	@Override
	public IClientReference getPerClientReference() {
		return StellarAPI.proxy;
	}

	@SubscribeEvent
	public void onGatherEntityCapability(AttachCapabilitiesEvent<Entity> event) {
		CheckEntityOpticalViewerEvent check = new CheckEntityOpticalViewerEvent(event.getObject());
		SAPIReference.getEventBus().post(check);
		if (check.isOpticalEntity())
			event.addCapability(new ResourceLocation(StellarAPI.modid, "viewer"),
					new PerEntityManager(event.getObject()));
	}

	@Override
	public IViewScope getDefaultScope() {
		return new NakedScope();
	}

	@Override
	public IOpticalFilter getDefaultFilter() {
		return new NakedFilter();
	}

	@SubscribeEvent
	public void onGatherWorldCapability(AttachCapabilitiesEvent<World> event) {
		event.addCapability(new ResourceLocation(StellarAPI.modid, "celestials"),
				new PerWorldManager(event.getObject()));
	}
}