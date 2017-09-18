package stellarapi.internal.settings;

import stellarapi.api.atmosphere.IAtmProvider;
import stellarapi.api.atmosphere.IAtmSetProvider;
import stellarapi.api.lib.config.DynamicConfig;

public class AtmWorldSettings {

	private final transient AtmSettings parentSettings;

	private transient IAtmProvider provider;
	private transient IAtmSetProvider setProvider;
	private transient boolean setProviderApplied = false;

	@DynamicConfig.Expand
	@DynamicConfig.Dependence(id = "settings")
	public Object atmSettings;

	public AtmWorldSettings(AtmSettings parent) {
		this.parentSettings = parent;
		this.provider = parentSettings.getCurrentProvider();

		this.atmSettings = this.getSettings(null);
	}

	public void checkNupdateProvider() {
		if(provider.getReadableName().equals(parentSettings.atmProviderName))
			return;

		this.provider = parentSettings.getCurrentProvider();
		this.setProvider = provider.perSetProvider(parentSettings.theWorldSet);
		this.setProviderApplied = false;
	}

	@DynamicConfig.EvaluatorID("settings")
	public Object getSettings(Object previous) {
		this.checkNupdateProvider();

		if(!this.setProviderApplied) {
			this.setProviderApplied = true;
			return setProvider.generateSettings();
		} else return previous;
	}
}
