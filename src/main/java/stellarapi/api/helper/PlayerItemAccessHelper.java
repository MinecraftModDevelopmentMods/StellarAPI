package stellarapi.api.helper;

import java.lang.reflect.Field;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerItemAccessHelper {
	
	private static final Field itemUseField = ReflectionHelper.findField(EntityPlayer.class,
			ObfuscationReflectionHelper.remapFieldNames(EntityPlayer.class.getName(), "itemInUse", "field_71074_e"));
	
	public static ItemStack getUsingItem(EntityPlayer player) {
		try {
			return (ItemStack) itemUseField.get(player);
		} catch (Exception exc) {
			Throwables.propagate(exc);
			
			// Code cannot reach here
			return null;
		}
	}

}
