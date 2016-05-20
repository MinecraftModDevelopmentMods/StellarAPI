package stellarapi.example.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stellarapi.api.interact.IViewScopeItem;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.Wavelength;

/**
 * Example for telescope item which gets activated any time the player press the right click to use the item.
 * */
public class ItemTelescopeExample extends Item implements IViewScopeItem {
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(hand == EnumHand.MAIN_HAND) {
			this.onUse(player, hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		} else return ActionResult.newResult(EnumActionResult.PASS, stack);
	}
	
	@Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if(hand == EnumHand.MAIN_HAND) {
			this.onUse(player, hand);
			return EnumActionResult.SUCCESS;
		} else return EnumActionResult.PASS;
    }
	
	public void onUse(EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
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
	}

	@Override
	public boolean isSame(ItemStack instance, ItemStack another) {
		return instance == another;
	}

}
