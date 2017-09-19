package stellarium.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.IRenderHandler;
import stellarapi.api.SAPIReferences;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarium.StellarSky;
import stellarium.render.sky.SkyModel;
import stellarium.render.sky.SkyRenderInformation;
import stellarium.view.ViewerInfo;

public class NewSkyRenderer extends IRenderHandler {

	private SkyModel model;
	
	public NewSkyRenderer(SkyModel model) {
		this.model = model;
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		IGenericRenderer renderer = RendererRegistry.INSTANCE.evaluateRenderer(SkyModel.class);

		Entity viewer = mc.getRenderViewEntity();

		ICelestialCoordinate coordinate = SAPIReferences.getCoordinate(world);
		ISkyEffect sky = SAPIReferences.getSkyEffect(world);
		IViewScope scope = SAPIReferences.getScope(viewer);
		IOpticalFilter filter = SAPIReferences.getFilter(viewer);
		
		SkyRenderInformation info = new SkyRenderInformation(mc, world, partialTicks,
				new ViewerInfo(coordinate, sky, scope, filter, viewer));

		renderer.preRender(StellarSky.proxy.getClientSettings(), info);
		renderer.renderPass(this.model, null, info);
		renderer.postRender(StellarSky.proxy.getClientSettings(), info);
	}
}