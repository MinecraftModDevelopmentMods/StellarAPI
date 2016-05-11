package stellarapi.reference;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.IPerWorldReference;
import stellarapi.api.ISkyEffect;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.event.ConstructCelestialsEvent;
import stellarapi.api.event.ResetCoordinateEvent;
import stellarapi.api.event.ResetSkyEffectEvent;
import stellarapi.api.event.SortCelestialsEvent;

/**
 * Per server manager to contain the per-server objects.
 * */
public class PerServerManager extends WorldSavedData {

	private static final String ID = "stellarapiperservermanager";

	public static void initiatePerServerManager(MinecraftServer server) {
		server.getEntityWorld().mapStorage.setData(ID, new PerServerManager());
	}
	
	public static boolean isInitiated(MinecraftServer server) {
		return server.getEntityWorld().mapStorage.loadData(PerServerManager.class, ID) instanceof PerServerManager;
	}

	public static PerServerManager getPerServerManager(MinecraftServer server) {
		return (PerServerManager) server.getEntityWorld().mapStorage.loadData(PerServerManager.class, ID);
	}

	private PerServerManager() {
		super(ID);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) { }

	@Override
	public void writeToNBT(NBTTagCompound compound) { }

}
