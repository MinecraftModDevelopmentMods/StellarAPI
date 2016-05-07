package stellarapi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Throwables;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.optics.FilterHelper;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedFilter;
import stellarapi.feature.gui.overlay.OverlayHandler;

public class StellarAPIClientFMLEventHook {
	private KeyBinding focusGuiKey = new KeyBinding("key.stellarapi.focusgui.description", Keyboard.KEY_U, "key.stellarapi");
	
	private OverlayHandler overlay;
	
	public StellarAPIClientFMLEventHook(OverlayHandler overlay) {
		ClientRegistry.registerKeyBinding(this.focusGuiKey);
		
		this.overlay = overlay;
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.START)
			overlay.updateOverlay();
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if(focusGuiKey.isPressed())
			overlay.openGui(Minecraft.getMinecraft(), this.focusGuiKey);
	}
}
