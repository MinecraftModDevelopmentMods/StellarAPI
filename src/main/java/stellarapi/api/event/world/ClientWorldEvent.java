package stellarapi.api.event.world;

import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import stellarapi.api.event.PerWorldEvent;
import stellarapi.api.gui.loading.ICombinedProgressUpdate;

/**
 * World events on client.
 * */
public class ClientWorldEvent extends PerWorldEvent {
	
	private final ICombinedProgressUpdate loading;
	
	public ClientWorldEvent(World world, ICombinedProgressUpdate loading) {
		super(world);
		this.loading = loading;
	}
	
	public IProgressUpdate getProgressUpdate(String id) {
		return loading.getProgress(id);
	}
	
	public void removeProgressUpdate(String id) {
		loading.removeProgress(id);
	}
	
	/**
	 * Load event.
	 * */
	public static class Load extends ClientWorldEvent {
		public Load(World world, ICombinedProgressUpdate loading) {
			super(world, loading);
		}
	}
	
	/**
	 * World Loaded event. Cancel this event to delay loading.
	 * It's recommended to receive this event even if this event had canceled.
	 * */
	@Cancelable
	public static class Loaded extends ClientWorldEvent {
		private int attempt;
		
		public Loaded(World world, ICombinedProgressUpdate loading) {
			super(world, loading);
		}
		
		public Loaded(World world, ICombinedProgressUpdate loading, int attempt) {
			super(world, loading);
			this.attempt = attempt;
		}
		
		public int getAttemptNumber() {
			return this.attempt;
		}
	}
	
	/**
	 * Unload event.
	 * */
	public static class Unload extends ClientWorldEvent {
		public Unload(World world, ICombinedProgressUpdate loading) {
			super(world, loading);
		}
	}

}
