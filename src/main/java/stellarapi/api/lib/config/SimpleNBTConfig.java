package stellarapi.api.lib.config;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import stellarapi.api.lib.config.property.ConfigProperty;

public abstract class SimpleNBTConfig extends SimpleConfigHandler implements INBTConfig<NBTTagCompound> {

	@Override
	public void setupConfig(Configuration config, String category) {
		super.setupConfig(config, category);
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		super.loadFromConfig(config, category);
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		super.saveToConfig(config, category);
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		for (ConfigProperty property : this.listProperties)
			property.readFromNBT(compound);
	}

	/**
	 * Placeholder for writing to pre-existing compound on subclasses.
	 * */
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		for (ConfigProperty property : this.listProperties)
			property.writeToNBT(compound);
		return compound;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public abstract INBTConfig copy();

	protected void applyCopy(SimpleNBTConfig config) {
		for (ConfigProperty property : config.listProperties)
			property.setAsProperty(mapProperties.get(property.getConfigName()));
	}

}
