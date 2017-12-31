package stellarapi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Throwables;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import stellarapi.api.SAPIReferences;
import stellarapi.api.event.world.ClientWorldEvent;
import stellarapi.api.optics.EyeDetector;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedFilter;
import stellarapi.feature.gui.overlay.OverlayHandler;

public class SAPIClientForgeEventHook {

	private static final Field lightMapField = ReflectionHelper.findField(EntityRenderer.class,
			ObfuscationReflectionHelper.remapFieldNames(EntityRenderer.class.getName(), "lightmapTexture",
					"field_78513_d"));

	private static final Field lightMapUpdatedField = ReflectionHelper.findField(EntityRenderer.class,
			ObfuscationReflectionHelper.remapFieldNames(EntityRenderer.class.getName(), "lightmapUpdateNeeded",
					"field_78536_aa"));

	static {
		try {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(lightMapField, lightMapField.getModifiers() & ~Modifier.FINAL);
		} catch (Exception exc) {
			Throwables.propagate(exc);
		}
	}

	private OverlayHandler overlay;
	private KeyBinding focusGuiKey = new KeyBinding("key.stellarapi.focusgui.description", Keyboard.KEY_U,
			"key.stellarapi");

	public SAPIClientForgeEventHook(OverlayHandler overlay) {
		ClientRegistry.registerKeyBinding(this.focusGuiKey);

		this.overlay = overlay;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onUpdateFOV(EntityViewRenderEvent.FOVModifier event) {
		if(!SAPIReferences.hasOpticalInformation(event.getEntity()))
			return;

		IViewScope scope = SAPIReferences.getScope(event.getEntity());

		if (scope.forceChange())
			event.setFOV(70.0F / (float) scope.getMP());
		else
			event.setFOV(event.getFOV() / (float) scope.getMP());
	}

	private int counter = 0;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDecideFogColor(EntityViewRenderEvent.FogColors event) {
		if(!SAPIReferences.hasOpticalInformation(event.getEntity()))
			return;

		IViewScope scope = SAPIReferences.getScope(event.getEntity());
		IOpticalFilter filter = SAPIReferences.getFilter(event.getEntity());

		double multiplier = scope.getLGP() / (scope.getMP() * scope.getMP());

		double[] value = EyeDetector.getInstance().process(multiplier, filter,
				new double[] { event.getRed(), event.getGreen(), event.getBlue() });
		event.setRed((float) Math.min(value[0], 1.0));
		event.setGreen((float) Math.min(value[1], 1.0));
		event.setBlue((float) Math.min(value[2], 1.0));

		if (multiplier != 1.0 || !(filter instanceof NakedFilter)) {
			DynamicTexture texture;
			try {
				texture = (DynamicTexture) lightMapField.get(event.getRenderer());

				for (int i = 0; i < 255; i++) {
					int data = texture.getTextureData()[i];
					int red = ((data & 0x00ff0000) >> 16);
					int green = ((data & 0x0000ff00) >> 8);
					int blue = data & 0x000000ff;

					double[] modified = EyeDetector.getInstance().process(multiplier, filter,
							new double[] { red / 255.0, green / 255.0, blue / 255.0 });

					red = Math.min(0xff, (int) (modified[0] * 0xff));
					green = Math.min(0xff, (int) (modified[1] * 0xff));
					blue = Math.min(0xff, (int) (modified[2] * 0xff));

					texture.getTextureData()[i] = 255 << 24 | red << 16 | green << 8 | blue;
				}

				texture.updateDynamicTexture();

				lightMapUpdatedField.set(event.getRenderer(), true);

			} catch (Exception exc) {
				Throwables.propagate(exc);
			}
		}
	}

	@SubscribeEvent
	public void renderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
			overlay.renderGameOverlay(event.getResolution(), event.getPartialTicks());
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			overlay.updateOverlay();
		}
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if (focusGuiKey.isPressed())
			overlay.openGui(Minecraft.getMinecraft(), this.focusGuiKey);
	}
}
