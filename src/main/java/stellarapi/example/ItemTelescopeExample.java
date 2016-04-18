package stellarapi.example;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import stellarapi.api.IViewScope;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.wavecolor.Wavelength;

/**
 * Example for telescope item.
 * Note that the item should have max stack size 1.
 * */
public class ItemTelescopeExample extends Item implements IViewScope {
	
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
		p_77659_3_.setItemInUse(p_77659_1_, Integer.MAX_VALUE);
        
        return p_77659_1_;
    }
	
	@Override
    public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_) {
		
	}


	@Override
	public double getLGP() {
		return 200.0;
	}

	@Override
	public double getResolution(Wavelength wl) {
		return 0.03;
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

}
