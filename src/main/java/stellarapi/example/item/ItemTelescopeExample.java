package stellarapi.example.item;

import net.minecraft.entity.EntityLivingBase;
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
import stellarapi.api.StellarAPICapabilities;
import stellarapi.api.interact.IOpticalProperties;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedScope;
import stellarapi.api.optics.Wavelength;

/**
 * Example for telescope item which gets activated any time the player press the
 * right click to use the item.
 */
public class ItemTelescopeExample extends Item {

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		this.onUse(player, hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
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
        return new ScopeProvider();
    }

	public class ScopeProvider implements ICapabilityProvider, IOpticalProperties {
		@Override
		public boolean isFilter() {
			return false;
		}

		@Override
		public IOpticalFilter getFilter(EntityLivingBase viewer) {
			return null;
		}

		@Override
		public boolean isScope() {
			return true;
		}

		@Override
		public IViewScope getScope(EntityLivingBase viewer) {
			return scope;
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == StellarAPICapabilities.OPTICAL_PROPERTY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == StellarAPICapabilities.OPTICAL_PROPERTY) {
				return (T) this;
			} else return null;
		}
	}

	private IViewScope scope = new IViewScope() {
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
