package stellarapi.lib.render.internal;

import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import stellarapi.lib.hierarchy.HierarchyDistributor;
import stellarapi.lib.hierarchy.IFieldElementDescription;
import stellarapi.lib.render.IGenericRenderer;
import stellarapi.lib.render.RendererRegistry;

public class DefaultRenderer implements IGenericRenderer {

	private Map<Object, IGenericRenderer> subModelRenderers = Maps.newHashMap();
	
	public DefaultRenderer(Class<?> modelClass) {
		Map<Object, IFieldElementDescription> fields = HierarchyDistributor.INSTANCE.fields(modelClass);
		this.subModelRenderers = Maps.transformValues(
				fields, new Function<IFieldElementDescription, IGenericRenderer>() {
			@Override
			public IGenericRenderer apply(IFieldElementDescription input) {
				return RendererRegistry.INSTANCE.evaluateRenderer(input.getElementType());
			}
		});
	}

	@Override
	public void initialize(Object settings) {
		for(IGenericRenderer renderer : subModelRenderers.values()) {
			renderer.initialize(settings);
		}
	}

	@Override
	public void preRender(Object settings, Object info) {
		for(IGenericRenderer renderer : subModelRenderers.values()) {
			renderer.preRender(settings, info);
		}
	}

	@Override
	public void renderPass(Object model, Object pass, Object info) {
		for(Map.Entry<Object, IGenericRenderer> idRenderer : subModelRenderers.entrySet()) {
			Iterator ite = HierarchyDistributor.INSTANCE.iteratorFor(model, idRenderer.getKey());
			while(ite.hasNext())
				idRenderer.getValue().renderPass(ite.next(), pass, info);
		}
	}

	@Override
	public void postRender(Object settings, Object info) {
		for(IGenericRenderer renderer : subModelRenderers.values()) {
			renderer.postRender(settings, info);
		}
	}
}
