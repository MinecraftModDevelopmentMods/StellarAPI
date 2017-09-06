package stellarapi;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPIReference;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.internal.coordinates.CCoordSystem;
import stellarapi.internal.reference.CWorldReference;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetManager;

public class SAPICapsEventHook {

	private static final ResourceLocation CAPS =
			new ResourceLocation(SAPIReference.modid, "capabilities");

	@SubscribeEvent
	public void attachWorldCaps(AttachCapabilitiesEvent<World> worldCaps) {
		World world = worldCaps.getObject();
		WorldSet worldSet = WorldSetManager.getPrimaryWorldSet(world);

		if(worldSet == null)
			return;

		ICoordProvider provider = null; // TODO load settings and fill in provider
		ICoordHandler handler = provider.provideCoordHandler(worldSet);
		CCoordSystem system = new CCoordSystem();
		system.setupSystem(handler);

		CWorldReference reference = new CWorldReference(worldCaps.getObject(), system);
		worldCaps.addCapability(CAPS, reference);
	}

}