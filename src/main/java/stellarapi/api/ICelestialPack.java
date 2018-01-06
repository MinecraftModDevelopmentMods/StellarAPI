package stellarapi.api;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.INBTSerializable;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;

/**
 * Celestial pack which can be provided by mods.
 * */
public interface ICelestialPack extends INBTSerializable<NBTTagCompound> {
	/**
	 * Gets the pack name.
	 * */
	public String getPackName();

	/**
	 * Gets the world-specific scene.
	 * */
	public ICelestialScene getScene(World world);
}
