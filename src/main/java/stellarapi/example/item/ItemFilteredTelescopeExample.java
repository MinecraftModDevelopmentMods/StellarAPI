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
import stellarapi.api.optics.EnumRGBA;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedScope;
import stellarapi.api.optics.RGBFilter;
import stellarapi.api.optics.Wavelength;

/**
 * Example for filtered telescope item.
 */
public class ItemFilteredTelescopeExample extends Item {

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

	private IOpticalFilter filter = new RGBFilter() {

		@Override
		public double getFilterEfficiency(EnumRGBA color) {
			switch (color) {
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
        return new FilterProvider();
    }
	
	public class FilterProvider implements ICapabilityProvider, IOpticalProperties {
		@Override
		public boolean isFilter() {
			return true;
		}

		@Override
		public IOpticalFilter getFilter(EntityLivingBase viewer) {
			return filter;
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
				return StellarAPICapabilities.OPTICAL_PROPERTY.cast(this);
			} else return null;
		}
	}

}
