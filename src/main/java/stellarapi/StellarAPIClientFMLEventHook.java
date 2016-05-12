package stellarapi;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.world.ClientWorldEvent;
import stellarapi.feature.gui.overlay.OverlayHandler;

public class StellarAPIClientFMLEventHook {
	private KeyBinding focusGuiKey = new KeyBinding("key.stellarapi.focusgui.description", Keyboard.KEY_U, "key.stellarapi");
	
	private OverlayHandler overlay;
	private int attempt = 1;
	
	public StellarAPIClientFMLEventHook(OverlayHandler overlay) {
		ClientRegistry.registerKeyBinding(this.focusGuiKey);
		
		this.overlay = overlay;
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {		
		if(event.phase == TickEvent.Phase.START) {
			if(checking) {
				Minecraft mc = Minecraft.getMinecraft();
				ClientWorldEvent.Loaded loaded = new ClientWorldEvent.Loaded(mc.theWorld, StellarAPI.proxy.getLoadingProgress(), this.attempt);
				if(!StellarAPIReference.getEventBus().post(loaded)) {
					Minecraft.getMinecraft().displayGuiScreen(null);
					checking = false;
					this.attempt = 1;
				} else this.attempt++;
			}
			overlay.updateOverlay();
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if(focusGuiKey.isPressed())
			overlay.openGui(Minecraft.getMinecraft(), this.focusGuiKey);
	}
	
	private static boolean checking = false;

	public static void startChecking() {
		checking = true;
	}
}
