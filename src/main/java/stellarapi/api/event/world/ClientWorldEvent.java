package stellarapi.api.event.world;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
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
		public Loaded(World world, ICombinedProgressUpdate loading) {
			super(world, loading);
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
