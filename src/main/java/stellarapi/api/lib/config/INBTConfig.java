package stellarapi.api.lib.config;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

public interface INBTConfig<T extends NBTBase> extends IConfigHandler, INBTSerializable<T> {

	public INBTConfig copy();

}