package stellarapi;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.example.item.ItemFilteredTelescopeExample;
import stellarapi.example.item.ItemTelescopeExample;

public enum SAPIItems {
	INSTANCE;

	public Item telescope, filteredTelescope;

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> regEvent) {
		this.telescope = new ItemTelescopeExample().setUnlocalizedName("stellarapi.deftelescope")
				.setCreativeTab(CreativeTabs.TOOLS).setMaxStackSize(1);
		telescope.setRegistryName("defaulttelescope");
		regEvent.getRegistry().register(this.telescope);

		this.filteredTelescope = new ItemFilteredTelescopeExample()
				.setUnlocalizedName("stellarapi.deffilteredtelescope").setCreativeTab(CreativeTabs.TOOLS)
				.setMaxStackSize(1);
		filteredTelescope.setRegistryName("defaultfilteredtelescope");
		regEvent.getRegistry().register(this.filteredTelescope);

		StellarAPI.PROXY.registerModels();
	}
}
