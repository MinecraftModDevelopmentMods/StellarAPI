package stellarapi.example.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import stellarapi.api.interact.IOpticalFilterItem;
import stellarapi.api.interact.IViewScopeItem;
import stellarapi.api.optics.EnumRGBA;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.RGBFilter;
import stellarapi.api.optics.Wavelength;

/**
 * Example for filtered telescope item.
 * */
public class ItemFilteredTelescopeExample extends Item implements IViewScopeItem, IOpticalFilterItem {
	
	private IViewScope scope = new IViewScope() {
		
		@Override
		public double getLGP() {
			return 200.0;
		}

		@Override
		public double getResolution(Wavelength wl) {
			return 0.1;
		}

		@Override
		public double getMP() {
			return 10.0;
		}

		@Override
		public boolean forceChange() {
			return true;
		}

		@Override
		public boolean isFOVCoverSky() {
			return true;
		}
		
	};
	
	private IOpticalFilter filter = new RGBFilter() {

		@Override
		public double getFilterEfficiency(EnumRGBA color) {
			switch(color) {
			case Red:
				return 0.3;
			case Green:
				return 0.9;
			case Blue:
				return 0.6;
			case Alpha:
				return 0.7;
			}
			
			return 1.0;
		}
		
	};
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		this.onUse(stack, player);
		return stack;
	}
	
	@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		this.onUse(stack, player);
    	return true;
    }
	
	public void onUse(ItemStack stack, EntityPlayer player) {
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return Integer.MAX_VALUE;
	}

	@Override
	public IOpticalFilter getFilter(EntityPlayer player, ItemStack item) {
		return this.filter;
	}

	@Override
	public IViewScope getScope(EntityPlayer player, ItemStack item) {
		return this.scope;
	}

	@Override
	public boolean isSame(ItemStack instance, ItemStack another) {
		return instance == another;
	}


}
