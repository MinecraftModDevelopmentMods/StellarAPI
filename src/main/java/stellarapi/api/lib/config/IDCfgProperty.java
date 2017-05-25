package stellarapi.api.lib.config;

public interface IDCfgProperty {

	public void setDefaultValue(Object value);

	public void setValue(Object value);
	public Object getValue();

	public boolean isChanged();
}