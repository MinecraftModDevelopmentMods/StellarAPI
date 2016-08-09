package stellarapi.work.celestial;

import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;
import stellarapi.work.basis.impl.target.AbstractTarget;

/**
 * Celestial target which represents the region which could be covered by a 'world'.
 * */
public class WorldCover extends AbstractTarget<WorldCover.WorldStage> {

	private MinecraftServer server;
	@Nullable
	private World world;

	@Override
	public void process(WorldStage stage, ICompound inspect, IModifiableCompound additional) {
		// TODO Auto-generated method stub
		
	}

	public static class WorldStage extends IAccuracyStage<WorldStage> {

		@Override
		public int compareTo(WorldStage toCompare) {
			// TODO World loading stages
			return 0;
		}
	}

	@Override
	protected long captureCurrentTime() {
		return System.currentTimeMillis();
	}

}