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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
import stellarapi.api.event.FilterQEEvent;
import stellarapi.api.interact.IFilter;
import stellarapi.api.interact.IScope;
import stellarapi.api.optics.EnumRGBA;
import stellarapi.api.optics.Wavelength;
import stellarapi.api.render.IAdaptiveRenderer;
import stellarapi.feature.gui.overlay.OverlayHandler;
import stellarapi.reference.CelestialPackManager;

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

	private OverlayHandler overlay;
	private KeyBinding focusGuiKey = new KeyBinding("key.stellarapi.focusgui.description", Keyboard.KEY_U,
			"key.stellarapi");

	public SAPIClientEventHook(OverlayHandler overlay) {
		ClientRegistry.registerKeyBinding(this.focusGuiKey);
		this.overlay = overlay;
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderWorldLast(RenderWorldLastEvent event) {
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
	public void onDecideFOV(EntityViewRenderEvent.FOVModifier event) {
		if(event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase viewer = (EntityLivingBase) event.getEntity();
			ItemStack active = viewer.getActiveItemStack();
			if(active != null) {
				IScope scope = active.getCapability(SAPICapabilities.SCOPE_CAPABILITY, null);
				if(scope != null)
					event.setFOV(scope.transformFOV(event.getFOV()));
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDecideQE(FilterQEEvent event) {
		if(event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase viewer = (EntityLivingBase) event.getEntity();
			ItemStack active = viewer.getActiveItemStack();
			if(active != null) {
				IFilter filter = active.getCapability(SAPICapabilities.FILTER_CAPABILITY, null);
				if(filter != null)
					event.setQE(filter.transformQE(event.getWavelength(), event.getQE()));
			}
		}
	}


	private float getFilterQE(EntityViewRenderEvent event, Wavelength wavelengthIn, float initialQE) {
		return getFilterQE(event.getRenderer(), event.getEntity(), event.getState(), event.getRenderPartialTicks(), wavelengthIn, initialQE);
	}

	private float getFilterQE(EntityRenderer renderer, Entity entity, IBlockState state, double renderPartialTicks, Wavelength wavelengthIn, float initialQE) {
		FilterQEEvent event = new FilterQEEvent(renderer, entity, state, renderPartialTicks, wavelengthIn, initialQE);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getQE();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDecideFogColor(EntityViewRenderEvent.FogColors event) {
		//IOpticalProp filter = SAPIReferences.getFilter(event.getEntity());
		float[] eff = new float[3];
		for(EnumRGBA color : EnumRGBA.RGB)
			eff[color.ordinal()] = this.getFilterQE(event, Wavelength.colorWaveMap.get(color), 1.0f);

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
	public void renderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
			overlay.renderGameOverlay(event.getResolution(), event.getPartialTicks());
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			overlay.updateOverlay();
		}
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if (focusGuiKey.isPressed())
			overlay.openGui(Minecraft.getMinecraft(), this.focusGuiKey);
	}
}
