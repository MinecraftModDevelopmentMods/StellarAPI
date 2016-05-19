package stellarapi.api.helper;

import java.lang.reflect.Field;

import com.google.common.base.Throwables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PlayerItemAccessHelper {
	
	private static final Field itemUseField = ReflectionHelper.findField(EntityPlayer.class,
			ObfuscationReflectionHelper.remapFieldNames(EntityPlayer.class.getName(), "itemInUse", "field_71074_e"));
	
	/**
	 * Gets using item of the player.
	 * This exists because {@link EntityPlayer#getItemInUse()} is client-only method,
	 * while {@link EntityPlayer#itemInUse} is a private universal field.
	 * @param player the player
	 * */
	public static ItemStack getUsingItem(EntityPlayer player) {
		try {
			return (ItemStack) itemUseField.get(player);
		} catch (Exception exc) {
			Throwables.propagate(exc);
			
			// Code cannot reach here
			return null;
		}
	}
	
	/**
	 * Sets using item of the player.
	 * Used to fix problems with updated using items. <p>
	 * DO NOT use this method externally.
	 * */
	@Deprecated
	public static void setUsingItem(EntityPlayer player, ItemStack stack) {
		try {
			itemUseField.set(player, stack);
		} catch (Exception exc) {
			Throwables.propagate(exc);
		}
	}

}
