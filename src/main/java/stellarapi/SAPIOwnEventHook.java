package stellarapi;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.event.UpdateFilterEvent;
import stellarapi.api.event.UpdateScopeEvent;
import stellarapi.api.event.interact.CheckEntityOpticalViewerEvent;
import stellarapi.api.interact.IOpticalProperties;

public class SAPIOwnEventHook {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUpdateScope(UpdateScopeEvent event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase viewer = (EntityLivingBase) event.getEntity();
			ItemStack itemToCheck = viewer.getActiveItemStack();
			

			if (itemToCheck != null && itemToCheck.hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = itemToCheck.getCapability(
						SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				if(property.isScope())
					event.setScope(property.getScope(viewer));
			}

			if (viewer.getRidingEntity() != null && viewer.getRidingEntity().hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = viewer.getRidingEntity().getCapability(
						SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				if(property.isScope())
					event.setScope(property.getScope(viewer));
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUpdateFilter(UpdateFilterEvent event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase viewer = (EntityLivingBase) event.getEntity();
			ItemStack itemToCheck = viewer.getActiveItemStack();

			if (itemToCheck != null && itemToCheck.hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = itemToCheck.getCapability(
						SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				if(property.isFilter())
					event.setFilter(property.getFilter(viewer));
			}

			if (viewer.getRidingEntity() != null && viewer.getRidingEntity().hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = viewer.getRidingEntity().getCapability(
						SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				if(property.isFilter())
					event.setFilter(property.getFilter(viewer));
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void checkOpticalEntity(CheckEntityOpticalViewerEvent event) {
		// TODO better implementation & logic of checking for the optical entities.
		event.setIsOpticalEntity(event.getEntity() instanceof EntityPlayer);
	}
}
