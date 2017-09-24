package stellarapi.api.atmosphere;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.WaveFilterType;

/**
 * Local atmosphere.
 * Settings can be applied directly, and can read from nbt.
 * */
public interface ILocalAtmosphere extends INBTSerializable<NBTTagCompound> {
	/** Gets refracted direction of specified direction */
	public SpCoord applyRefraction(Vec3d loc, SpCoord dir, WaveFilterType wave);

	/** Gets rate of resulted intensity from the extinction */
	public double extincted(Vec3d loc, SpCoord dir, WaveFilterType wave);

	/** Seeing in radians */
	public double seeing(Vec3d loc, SpCoord dir, WaveFilterType wave);


	/** Height of the location */
	public double getHeight(Vec3d loc, SpCoord dir);

	/** Converts a direction to the local direction */
	public SpCoord localDirection(Vec3d loc, SpCoord dir);
}