package stellarapi.api.lib.config;

public interface IDCfgProperty {

	// For custom handlers
	
	/** Gets the value. Only for leaves. */
	public Object getValue();

	/** Sets the value. */
	public void setValue(Object value);


	// For default handlers in string type

	/** Checks if certain string is valid. */
	public boolean isValid(String strValue);
	/** Gets the most relevant valid string with the parameter. */
	public String toValidString(String incomplete);
	/** Gets value as string. */
	public String getValueAsString();
	/** Sets value with string. if it failed, returns false. */
	public boolean setValue(String strValue);


	// For composite properties with no specific handling

	/** Gives alternative expansion defined on native types. It's immutable. */
	public ITypeExpansion<?> alternative();
}