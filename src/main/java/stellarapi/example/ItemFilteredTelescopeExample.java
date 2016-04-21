package stellarapi.example;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.optics.EnumRGBA;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.IWaveFilter;
import stellarapi.api.optics.RGBFilter;
import stellarapi.api.optics.Wavelength;

/**
 * Example for filtered telescope item.
 * Note that the item should have max stack size 1.
 * */
public class ItemFilteredTelescopeExample extends Item implements IViewScope, IOpticalFilter {
	
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
			default:
				return 1.0;
			}
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
		player.setItemInUse(stack, Integer.MAX_VALUE);
		
		StellarAPIReference.updateScope(player);
		StellarAPIReference.updateFilter(player);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int p_77659_4_)
    {
		player.clearItemInUse();
		StellarAPIReference.updateScope(player);
		StellarAPIReference.updateFilter(player);
    }


	@Override
	public double getLGP() {
		return scope.getLGP();
	}

	@Override
	public double getResolution(Wavelength wl) {
		return scope.getResolution(wl);
	}

	@Override
	public double getMP() {
		return scope.getMP();
	}

	@Override
	public boolean forceChange() {
		return scope.forceChange();
	}

	@Override
	public boolean isFOVCoverSky() {
		return scope.isFOVCoverSky();
	}

	@Override
	public boolean isRGB() {
		return filter.isRGB();
	}

	@Override
	public ImmutableList<? extends IWaveFilter> getFilterList() {
		return filter.getFilterList();
	}

}
