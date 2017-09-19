package stellarium.display;

import com.google.common.collect.ImmutableList;

import stellarium.client.ClientSettings;
import stellarium.display.ecgrid.EcGridType;
import stellarium.display.eqgrid.EqGridType;
import stellarium.display.horgrid.HorGridType;

public enum DisplayRegistry {
	INSTANCE;

	DisplayRegistry() {
		// TODO Interaction with existing objects
		register(new HorGridType());
		register(new EqGridType());
		register(new EcGridType());
	}

	private ImmutableList.Builder<RegistryDelegate> builder = ImmutableList.builder();
	
	public <Cfg extends PerDisplaySettings, Cache extends IDisplayCache<Cfg>>
	void register(IDisplayElementType<Cfg, Cache> type) {
		builder.add(new RegistryDelegate<Cfg, Cache>(type));
	}
	
	public void setupDisplay(ClientSettings settings, IDisplayInjectable injectable) {
		SimpleHierarchicalConfig displaySettings = injectable.getSubSettings(settings);
		for(RegistryDelegate delegate : builder.build())
			delegate.inject(displaySettings, injectable);
	}
	
	private static class RegistryDelegate<Cfg extends PerDisplaySettings, Cache extends IDisplayCache<Cfg>> {
		private RegistryDelegate(IDisplayElementType<Cfg, Cache> input) {
			this.type = input;
		}

		private IDisplayElementType<Cfg, Cache> type;
		
		public void inject(SimpleHierarchicalConfig settings, IDisplayInjectable injectable) {
			Cfg perDisplay = type.generateSettings();
			settings.putSubConfig(type.getName(), perDisplay);
			injectable.injectDisplay(this.type, perDisplay);
		}
	}
}
