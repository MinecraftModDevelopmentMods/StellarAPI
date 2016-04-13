package stellarapi.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import stellarapi.StellarAPI;
import stellarapi.api.ICelestialRenderer;
import stellarapi.client.ClientSettings;
import stellarapi.stellars.StellarManager;
import stellarapi.stellars.layer.CelestialManager;
import stellarapi.stellars.view.StellarDimensionManager;

public class SkyRenderCelestial implements ICelestialRenderer {
	
	private ClientSettings settings;
	private CelestialRenderer renderer;
	private boolean updated = false;
	
	public SkyRenderCelestial() {
		this.settings = StellarAPI.proxy.getClientSettings();
		this.renderer = new CelestialRenderer();
		
		CelestialManager celManager = StellarAPI.proxy.getClientCelestialManager();
		renderer.refreshRenderer(celManager.getLayers());
		settings.checkDirty();
		celManager.reloadClientSettings(this.settings);
		this.onSettingsUpdated(Minecraft.getMinecraft());
	}
	
	public void renderCelestial(Minecraft mc, float bglight, float weathereff, float partialTicks) {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		
		CelestialManager manager = StellarAPI.proxy.getClientCelestialManager();

		if(settings.checkDirty())
		{
			manager.reloadClientSettings(this.settings);
			this.updated = false;
		}
		
		if(!this.updated)
			this.onSettingsUpdated(mc);
		
		renderer.render(new StellarRenderInfo(mc, Tessellator.instance, bglight, weathereff, partialTicks), manager.getLayers());
	}
	
	private void onSettingsUpdated(Minecraft mc) {
		//Initialization update
		World world = mc.theWorld;
		
		if(world != null) {
			StellarManager manager = StellarManager.getManager(true);
			if(manager.getCelestialManager() != null) {
				this.updated = true;
				manager.update(world.getWorldTime());
				StellarDimensionManager dimManager = StellarDimensionManager.get(world);
				if(dimManager != null)
				{
					dimManager.update(world, world.getWorldTime());
					manager.updateClient(StellarAPI.proxy.getClientSettings(),
							dimManager.getViewpoint());
				}
			}
		}
	}

}
