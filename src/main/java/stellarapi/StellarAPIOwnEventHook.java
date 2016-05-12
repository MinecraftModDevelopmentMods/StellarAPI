package stellarapi;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.event.ConstructCelestialsEvent;
import stellarapi.api.event.ResetCoordinateEvent;
import stellarapi.api.event.ResetSkyEffectEvent;
import stellarapi.api.event.UpdateFilterEvent;
import stellarapi.api.event.UpdateScopeEvent;
import stellarapi.api.event.interact.ApplyOpticalEntityEvent;
import stellarapi.api.event.interact.ApplyOpticalItemEvent;
import stellarapi.api.event.interact.CheckSameOpticalItemEvent;
import stellarapi.api.event.world.ClientWorldEvent;
import stellarapi.api.event.world.ServerWorldEvent;
import stellarapi.api.helper.PlayerItemAccessHelper;
import stellarapi.api.interact.IOpticalFilterItem;
import stellarapi.api.interact.IOpticalFilterSimulatorEntity;
import stellarapi.api.interact.IViewScopeItem;
import stellarapi.api.interact.IViewScopeSimulatorEntity;
import stellarapi.api.lib.math.Spmath;
import stellarapi.impl.DefaultCollectionVanilla;
import stellarapi.impl.DefaultCoordinateVanilla;
import stellarapi.impl.DefaultSkyVanilla;
import stellarapi.reference.PerWorldManager;

public class StellarAPIOwnEventHook {
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onConstructCelestials(ConstructCelestialsEvent event) {
		if(event.getCollections().isEmpty()) {
			if(this.isOverworld(event.getWorld()))
			{
				DefaultCollectionVanilla collection = new DefaultCollectionVanilla(event.getWorld());
				event.getCollections().add(collection);
				event.getEffectors(IEffectorType.Light).add(collection.sun);
				event.getEffectors(IEffectorType.Tide).add(collection.moon);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onResetCoordinate(ResetCoordinateEvent event) {
		if(event.getCoordinate() == null)
			event.setCoordinate(new DefaultCoordinateVanilla(event.getWorld()));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onResetEffect(ResetSkyEffectEvent event) {
		if(event.getSkyEffect() == null)
			if(!event.getWorld().provider.hasNoSky)
				event.setSkyEffect(new DefaultSkyVanilla());
	}
	
	private boolean isOverworld(World world) {
		return "Overworld".equals(world.provider.getDimensionName()) && world.provider.isSurfaceWorld()
				&& Spmath.fmod(world.getCelestialAngle(0.5f)*2, 1.0f) != 0.0f;
	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUpdateScope(UpdateScopeEvent event) {
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ItemStack itemToCheck = PlayerItemAccessHelper.getUsingItem(player);
			
			if(itemToCheck != null && itemToCheck.getItem() instanceof IViewScopeItem)
				event.setScope(((IViewScopeItem) itemToCheck.getItem()).getScope(player, itemToCheck));
			else if(player.ridingEntity instanceof IViewScopeSimulatorEntity)
				event.setScope(((IViewScopeSimulatorEntity)player.ridingEntity).getScope(player));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUpdateFilter(UpdateFilterEvent event) {
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ItemStack itemToCheck = PlayerItemAccessHelper.getUsingItem(player);
			
			if(itemToCheck != null && itemToCheck.getItem() instanceof IOpticalFilterItem)
				event.setFilter(((IOpticalFilterItem)itemToCheck.getItem()).getFilter(player, itemToCheck));
			else if(player.ridingEntity instanceof IOpticalFilterSimulatorEntity)
				event.setFilter(((IOpticalFilterSimulatorEntity)player.ridingEntity).getFilter(player));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void applyOpticalItem(ApplyOpticalItemEvent event) {
		EntityPlayer player = event.getPlayer();
		
		event.setIsViewScope(event.getItem().getItem() instanceof IViewScopeItem);
		event.setIsOpticalFilter(event.getItem().getItem() instanceof IOpticalFilterItem);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void checkSameOpticalItem(CheckSameOpticalItemEvent event) {
		ItemStack first = event.getFirstItem();
		ItemStack second = event.getSecondItem();

		if(first == null && second == null)
		{
			event.markAsSame();
			return;
		} else if(first == null || second == null)
			return;
		
		if(first.getItem() instanceof IViewScopeItem && ((IViewScopeItem)first.getItem()).isSame(first, second))
			event.markAsSame();
		if(second.getItem() instanceof IViewScopeItem && ((IViewScopeItem)second.getItem()).isSame(second, first))
			event.markAsSame();
		
		if(first.getItem() instanceof IOpticalFilterItem && ((IOpticalFilterItem)first.getItem()).isSame(first, second))
			event.markAsSame();
		if(second.getItem() instanceof IOpticalFilterItem && ((IOpticalFilterItem)second.getItem()).isSame(second, first))
			event.markAsSame();
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void applyOpticalEntity(ApplyOpticalEntityEvent event) {
		event.setIsViewScope(event.getRidingEntity() instanceof IViewScopeSimulatorEntity);
		event.setIsOpticalFilter(event.getRidingEntity() instanceof IOpticalFilterSimulatorEntity);
	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onClientWorldLoad(ClientWorldEvent.Load event) {
		IProgressUpdate progress = event.getProgressUpdate("StellarAPI");
		progress.displayProgressMessage(I18n.format("progress.stellarapi.loading.main"));
		progress.resetProgresAndWorkingMessage(I18n.format("progress.stellarapi.loading.worldhook"));
		PerWorldManager.initiatePerWorldManager(event.getWorld());
		progress.resetProgresAndWorkingMessage("");
	}
	
	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public void onClientWorldLoadedPre(ClientWorldEvent.Loaded event) {
		IProgressUpdate progress = event.getProgressUpdate("StellarAPI");
		progress.displayProgressMessage(I18n.format("progress.stellarapi.pending.main", event.getAttemptNumber()));
		progress.resetProgresAndWorkingMessage(I18n.format("progress.stellarapi.pending.text", event.getAttemptNumber()));
	}
	
	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
	public void onClientWorldLoadedPost(ClientWorldEvent.Loaded event) {
		IProgressUpdate progress = event.getProgressUpdate("StellarAPI");
		if(event.isCanceled()) {
			progress.resetProgresAndWorkingMessage(I18n.format("progress.stellarapi.other.pending.text"));
		} else progress.resetProgresAndWorkingMessage("");
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onServerLoad(ServerWorldEvent.Load event) {
		PerWorldManager.initiatePerWorldManager(event.getWorld());
	}
	
	@SubscribeEvent
	public void onServerInitial(ServerWorldEvent.Initial event) { }
}
