package stellarapi.example.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.interact.IFilter;
import stellarapi.api.interact.IScope;
import stellarapi.api.interact.RGBFilter;
import stellarapi.api.interact.SimpleScope;
import stellarapi.api.optics.EnumRGBA;

/**
 * Example for telescope item which gets activated any time the player press the
 * right click to use the item.
 * TODO AA Implement 10 times multiplication via events
 */
public class ItemTelescopeExample extends Item {
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		this.onUse(player, hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		this.onUse(player, hand);
		return EnumActionResult.SUCCESS;
	}

	public void onUse(EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return Integer.MAX_VALUE;
	}
	
	@Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new TelescopeProvider();
    }

	public class TelescopeProvider implements ICapabilityProvider {
		private IScope scope = new SimpleScope(10.0f);
		private IFilter filter = new RGBFilter() {
			@Override
			public double getFilterEfficiency(EnumRGBA color) {
				return 2.0;
			}
		};

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == SAPICapabilities.SCOPE_CAPABILITY || capability == SAPICapabilities.FILTER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == SAPICapabilities.SCOPE_CAPABILITY) {
				return SAPICapabilities.SCOPE_CAPABILITY.cast(this.scope);
			} else if(capability == SAPICapabilities.FILTER_CAPABILITY) {
				return SAPICapabilities.FILTER_CAPABILITY.cast(this.filter);
			} else return null;
		}
	}
}
