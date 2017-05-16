package stellarapi.api.lib.config;

/**
 * Simple interface for configuration listeners.
 * @see ListenableConfig
 * */
public interface IConfigListener<T> {

	public void onConfigLoad(T configData);
	public void onConfigSave(T configData);

}