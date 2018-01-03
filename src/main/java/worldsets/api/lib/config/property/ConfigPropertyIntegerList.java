package worldsets.api.lib.config.property;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Property.Type;

public class ConfigPropertyIntegerList extends ConfigPropertyList {

	private int[] defaultValues;
	private int[] currentValues;

	public ConfigPropertyIntegerList(String configKey, String dataKey, int[] defaultValue) {
		super(configKey, dataKey);
		this.currentValues = this.defaultValues = defaultValue;
	}

	public int[] getIntList() {
		return this.currentValues;
	}

	public void setIntList(int[] value) {
		this.currentValues = value;
	}

	@Override
	protected String[] getDefaultValues() {
		String[] temp = new String[defaultValues.length];
		for (int i = 0; i < defaultValues.length; i++)
			temp[i] = Double.toString(defaultValues[i]);

		return temp;
	}

	@Override
	protected void resizeValuesToLength(boolean fixed, int length) {
		if (length != -1) {
			if (currentValues != null && currentValues.length != length)
				if (fixed || currentValues.length > length)
					currentValues = Arrays.copyOf(currentValues, length);

			if (defaultValues != null && defaultValues.length != length)
				if (fixed || defaultValues.length > length)
					defaultValues = Arrays.copyOf(defaultValues, length);
		}
	}

	@Override
	public void setAsDefault() {
		this.currentValues = this.defaultValues;
	}

	@Override
	public void setAsProperty(ConfigProperty property) {
		if (property instanceof ConfigPropertyIntegerList)
			this.currentValues = ((ConfigPropertyIntegerList) property).currentValues;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey(this.dataKey))
			this.currentValues = compound.getIntArray(this.dataKey);
		else
			this.currentValues = this.defaultValues;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setIntArray(this.dataKey, this.currentValues);
	}

	@Override
	public void loadFromConfig() {
		this.currentValues = property.isIntList() ? property.getIntList() : this.defaultValues;
	}

	@Override
	public void saveToConfig() {
		property.set(this.currentValues);
	}

	@Override
	protected Type getType() {
		return Type.INTEGER;
	}
}
