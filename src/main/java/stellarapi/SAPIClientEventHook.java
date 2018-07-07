package stellarapi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import stellarapi.api.ICelestialWorld;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.event.FOVEvent;
import stellarapi.api.event.QEEvent;
import stellarapi.api.event.RenderQEEvent;
import stellarapi.api.optics.EnumRGBA;
import stellarapi.api.optics.Wavelength;
import stellarapi.api.render.IAdaptiveRenderer;
import stellarapi.feature.gui.overlay.OverlayHandler;
import stellarapi.reference.CelestialPackManager;

@Mod.EventBusSubscriber(modid = SAPIReferences.MODID, value = Side.CLIENT)
public class SAPIClientEventHook {

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
			throw new RuntimeException(exc);
		}
	}

	public static final OverlayHandler OVERLAY = new OverlayHandler();
	public static final KeyBinding KEY_FOCUSGUI = new KeyBinding("key.stellarapi.focusgui.description", Keyboard.KEY_U,
			"key.stellarapi");

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onRenderWorldLast(RenderWorldLastEvent event) {
		World world = Minecraft.getMinecraft().world;
		ICelestialWorld cWorld = world.getCapability(SAPICapabilities.CELESTIAL_CAPABILITY, null);
		if(cWorld instanceof CelestialPackManager) {
			IAdaptiveRenderer renderer = ((CelestialPackManager) cWorld).getRenderer();
			IRenderHandler wrapped = world.provider.getSkyRenderer();
			if(renderer != null && !(wrapped instanceof IAdaptiveRenderer)) {
				renderer.setReplacedRenderer(wrapped);
				world.provider.setSkyRenderer(renderer);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onDecideFOV(EntityViewRenderEvent.FOVModifier e) {
		FOVEvent event = new FOVEvent(e.getEntity(), e.getFOV());
		MinecraftForge.EVENT_BUS.post(event);
		e.setFOV(event.getFOV());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onDecideQE(RenderQEEvent e) {
		QEEvent event = new QEEvent(e.getEntity(), e.getWavelength(), e.getQE());
		MinecraftForge.EVENT_BUS.post(event);
		e.setQE(event.getQE());
	}


	private static float getFilterQE(EntityViewRenderEvent event, Wavelength wavelengthIn, float initialQE) {
		return getFilterQE(event.getRenderer(), event.getEntity(), event.getState(), event.getRenderPartialTicks(), wavelengthIn, initialQE);
	}

	private static float getFilterQE(EntityRenderer renderer, Entity entity, IBlockState state, double renderPartialTicks, Wavelength wavelengthIn, float initialQE) {
		RenderQEEvent event = new RenderQEEvent(renderer, entity, state, renderPartialTicks, wavelengthIn, initialQE);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getQE();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onDecideFogColor(EntityViewRenderEvent.FogColors event) {
		float[] eff = new float[3];
		for(EnumRGBA color : EnumRGBA.RGB)
			eff[color.ordinal()] = getFilterQE(event, Wavelength.colorWaveMap.get(color), 1.0f);

		event.setRed((float) Math.min(event.getRed() * eff[0], 1.0));
		event.setGreen((float) Math.min(event.getGreen() * eff[1], 1.0));
		event.setBlue((float) Math.min(event.getBlue() * eff[2], 1.0));

		if (eff[0] != 1.0f || eff[1] != 1.0f || eff[2] != 1.0f) {
			DynamicTexture texture;
			try {
				texture = (DynamicTexture) lightMapField.get(event.getRenderer());

				for (int i = 0; i < 255; i++) {
					int data = texture.getTextureData()[i];
					int red = ((data & 0x00ff0000) >> 16);
					int green = ((data & 0x0000ff00) >> 8);
					int blue = data & 0x000000ff;

					red = Math.min(0xff, (int) (red * eff[0]));
					green = Math.min(0xff, (int) (green * eff[1]));
					blue = Math.min(0xff, (int) (blue * eff[2]));

					texture.getTextureData()[i] = 255 << 24 | red << 16 | green << 8 | blue;
				}

				texture.updateDynamicTexture();

				lightMapUpdatedField.set(event.getRenderer(), true);

			} catch (Exception exc) {
				throw new RuntimeException(exc);
			}
		}
	}

	@SubscribeEvent
	public static void renderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
			OVERLAY.renderGameOverlay(event.getResolution(), event.getPartialTicks());
	}

	@SubscribeEvent
	public static void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			OVERLAY.updateOverlay();
		}
	}

	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {
		if (KEY_FOCUSGUI.isPressed())
			OVERLAY.openGui(Minecraft.getMinecraft(), KEY_FOCUSGUI);
	}
}
