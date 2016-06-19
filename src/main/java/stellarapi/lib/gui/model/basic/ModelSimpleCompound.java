package stellarapi.lib.gui.model.basic;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderModel;

public class ModelSimpleCompound implements IRenderModel {

	private Map<String, IRenderModel> mapping;
	private Map<String, float[]> colorMap;
	float[] cache = new float[4];

	private ModelSimpleCompound(Map<String, IRenderModel> mapping) {
		// Mappings of models nearly independent to the information
		this.mapping = ImmutableMap.copyOf(mapping);
		this.colorMap = ImmutableMap.of();
	}

	private ModelSimpleCompound(Map<String, IRenderModel> mapping, Map<String, float[]> colorMap) {
		// Mappings of models nearly independent to the information
		this.mapping = ImmutableMap.copyOf(mapping);
		this.colorMap = ImmutableMap.copyOf(colorMap);
	}

	@Override
	public void renderModel(String info, IRectangleBound totalBound, IRectangleBound clipBound, Tessellator tessellator,
			VertexBuffer worldRenderer, TextureManager textureManager, float[] color) {
		System.arraycopy(color, 0, cache, 0, cache.length);
		if (colorMap.containsKey(info))
			for (int i = 0; i < cache.length; i++)
				cache[i] *= colorMap.get(info)[i];

		mapping.get(info).renderModel(info, totalBound, clipBound, tessellator, worldRenderer, textureManager,
				this.cache);
	}

}
