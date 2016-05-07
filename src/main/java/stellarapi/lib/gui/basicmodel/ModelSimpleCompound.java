package stellarapi.lib.gui.basicmodel;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderModel;

public class ModelSimpleCompound implements IRenderModel {
	
	private Map<String, IRenderModel> mapping;
	
	private ModelSimpleCompound(Map<String, IRenderModel> mapping) {
		//Mappings of models nearly independent to the information
		this.mapping = ImmutableMap.copyOf(mapping);
	}

	@Override
	public void renderModel(String info, IRectangleBound totalBound, IRectangleBound clipBound, Tessellator tessellator,
			TextureManager textureManager) {
		mapping.get(info).renderModel(info, totalBound, clipBound, tessellator, textureManager);
	}

}
