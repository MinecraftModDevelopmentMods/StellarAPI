package stellarapi;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPIReference;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.internal.coordinates.CCoordSystem;
import stellarapi.internal.reference.CWorldReference;

public class SAPICapsEventHook {

	public static final ResourceLocation CAPS =
			new ResourceLocation(SAPIReference.modid, "capabilities");

	@SubscribeEvent
	public void attachWorldCaps(AttachCapabilitiesEvent<World> worldCaps) {
		ICoordHandler handler = null; // TODO fill in handler
		CCoordSystem system = new CCoordSystem();
		system.setupSystem(handler);

		CWorldReference reference = new CWorldReference(worldCaps.getObject(), system);
		worldCaps.addCapability(CAPS, reference);
	}

}