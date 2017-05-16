package stellarapi.lib.render.internal;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicates;

import stellarapi.lib.render.RendererRegistry;
import stellarapi.lib.render.hierarchy.IDistributionConfigurable;
import stellarapi.lib.render.hierarchy.IRenderState;
import stellarapi.lib.render.hierarchy.IRenderTransition;
import stellarapi.lib.render.hierarchy.IRenderedCollection;
import stellarapi.lib.render.hierarchy.ITransitionBuilder;

public class DistributionConfigurable implements IDistributionConfigurable {
	
	private Class<?> modelClass;

	public DistributionConfigurable(Class<?> modelClass) {
		this.modelClass = modelClass;
	}

	@Override
	public IRenderedCollection overallCollection() {
		return new RenderedCollection(this.modelClass, Predicates.alwaysTrue(), Functions.identity());
	}

	@Override
	public ITransitionBuilder transitionBuilder(Function transformer, IRenderState initialState) {
		return new TransitionBuilder(transformer, initialState);
	}

	@Override
	public void endSetup(IRenderTransition transition) {
		if(transition instanceof RenderingFSM)
			RendererRegistry.INSTANCE.bind(this.modelClass, (RenderingFSM)transition);
	}

}
