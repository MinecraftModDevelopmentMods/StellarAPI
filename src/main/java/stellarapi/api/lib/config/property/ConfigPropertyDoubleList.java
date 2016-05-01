package stellarapi.api.lib.config.property;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.config.Property.Type;

public class ConfigPropertyDoubleList extends ConfigPropertyList {
	
	private double[] defaultValues;
	private double[] currentValues;
	
	public ConfigPropertyDoubleList(String configKey, String dataKey, double[] defaultValues) {
		super(configKey, dataKey);
		this.currentValues = this.defaultValues = defaultValues;
	}
	
	public double[] getDoubleList() {
		return this.currentValues;
	}
	
	public void setDoubleList(double[] value) {
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
        if (length != -1)
        {
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
		if(property instanceof ConfigPropertyDoubleList)
			this.currentValues = ((ConfigPropertyDoubleList) property).currentValues;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey(this.dataKey))
		{
			NBTTagList list = compound.getTagList(this.dataKey, 6);
			this.currentValues = new double[list.tagCount()];
			for(int i = 0; i < list.tagCount(); i++)
				this.currentValues[i] = list.func_150309_d(i);
		}
		else this.currentValues = this.defaultValues;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < currentValues.length; i++)
			list.appendTag(new NBTTagDouble(this.currentValues[i]));
		compound.setTag(this.dataKey, list);
	}

	@Override
	public void loadFromConfig() {
		this.currentValues = property.isDoubleList()? property.getDoubleList() : this.defaultValues;
	}
	
	@Override
	public void saveToConfig() {
		property.set(this.currentValues);
	}

	@Override
	protected Type getType() {
		return Type.DOUBLE;
	}
}
