package stellarapi.api.atmosphere;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import stellarapi.api.SAPIRegistries;

public class AtmosphereInfo {
	private final ResourceLocation atmId;
	private final AtmosphereType atmType;
	private final Atmosphere atmosphere;

	private final IForgeRegistry<AtmosphereType> registry;

	public AtmosphereInfo(ResourceLocation id, AtmosphereType type, Atmosphere atm) {
		this.atmId = id;
		this.atmType = type;
		this.atmosphere = atm;

		this.registry = SAPIRegistries.getAtmTypeRegistry();
	}


	public AtmosphereType getAtmosphereType() {
		return this.atmType;
	}

	public Atmosphere getAtmosphere() {
		return this.atmosphere;
	}
}
