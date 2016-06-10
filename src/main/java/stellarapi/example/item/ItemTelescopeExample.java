package stellarapi.example.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import stellarapi.api.interact.IViewScopeItem;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedScope;
import stellarapi.api.optics.Wavelength;

/**
 * Example for telescope item which gets activated any time the player press the right click to use the item.
 * */
public class ItemTelescopeExample extends Item implements IViewScopeItem {
	
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
	public IViewScope getScope(EntityPlayer player, ItemStack item) {
		return new IViewScope() {
			@Override
			public double getLGP() {
				return 200.0;
			}

			@Override
			public double getResolution(Wavelength wl) {
				return NakedScope.DEFAULT_RESOLUTION / 3.0;
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
	}

	@Override
	public boolean isSame(ItemStack instance, ItemStack another) {
		return instance == another;
	}

}
