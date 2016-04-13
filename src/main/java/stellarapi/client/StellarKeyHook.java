package stellarapi.client;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.settings.KeyBinding;
import stellarapi.StellarAPI;

public class StellarKeyHook {
	
	private KeyBinding modeKey = new KeyBinding("key.stellarsky.viewmod.description", Keyboard.KEY_U, "key.stellarsky");
	
	public StellarKeyHook() {
		ClientRegistry.registerKeyBinding(this.modeKey);
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if(modeKey.isPressed()) {
			StellarAPI.proxy.getClientSettings().incrementViewMode();
		}
	}

}
