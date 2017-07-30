package stellarapi;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.world.ClientWorldEvent;
import stellarapi.feature.gui.overlay.OverlayHandler;

public class StellarAPIClientFMLEventHook {
	private KeyBinding focusGuiKey = new KeyBinding("key.stellarapi.focusgui.description", Keyboard.KEY_U,
			"key.stellarapi");

	private OverlayHandler overlay;
	private int attempt = 1;

	public StellarAPIClientFMLEventHook(OverlayHandler overlay) {
		ClientRegistry.registerKeyBinding(this.focusGuiKey);

		this.overlay = overlay;
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			if (checking && mc.world != null) {
				ClientWorldEvent.Loaded loaded = new ClientWorldEvent.Loaded(mc.world,
						StellarAPI.PROXY.getLoadingProgress(), this.attempt);
				if (!StellarAPIReference.getEventBus().post(loaded)) {
					Minecraft.getMinecraft().displayGuiScreen(null);
					checking = false;
					this.attempt = 1;
				} else
					this.attempt++;
			}
			overlay.updateOverlay();
		}
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if (focusGuiKey.isPressed())
			overlay.openGui(Minecraft.getMinecraft(), this.focusGuiKey);
	}

	private static boolean checking = false;

	public static void startChecking() {
		checking = true;
	}
}
