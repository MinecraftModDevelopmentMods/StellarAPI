package stellarapi.api.lib.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.config.Configuration;

/**
 * Wrapper of config handler for listening purpose.
 * Listeners will be notified of config loading/saving.
 * TODO is this interface right?
 * */
public class ListenableConfig<T extends IConfigHandler> implements IConfigHandler {

	private List<IConfigListener<T>> listenerList = Lists.newArrayList();
	private T wrapped;

	public ListenableConfig(T wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void setupConfig(Configuration config, String category) {
		wrapped.setupConfig(config, category);
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		wrapped.loadFromConfig(config, category);

		for(IConfigListener<T> listener : this.listenerList)
			listener.onConfigLoad(this.wrapped);
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		wrapped.saveToConfig(config, category);

		for(IConfigListener<T> listener : this.listenerList)
			listener.onConfigSave(this.wrapped);
	}

}
