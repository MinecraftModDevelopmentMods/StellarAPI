package stellarapi.api.helper;

import java.lang.reflect.Field;

import com.google.common.base.Throwables;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class LivingItemAccessHelper {

	private static final Field itemUseField = ReflectionHelper.findField(EntityLivingBase.class,
			ObfuscationReflectionHelper.remapFieldNames(EntityLivingBase.class.getName(), "activeItemStack",
					"field_184627_bm"));

	/**
	 * Gets using item(Using on the main hand) of the player.
	 * 
	 * @param player
	 *            the player
	 */
	public static ItemStack getUsingItem(EntityLivingBase entity) {
		return entity.getActiveItemStack();
	}

	/**
	 * Sets using item of the player. Used to fix problems with updated using
	 * items.
	 * <p>
	 * DO NOT use this method externally.
	 */
	@Deprecated
	public static void setUsingItem(EntityLivingBase entity, ItemStack stack) {
		try {
			itemUseField.set(entity, stack);
		} catch (Exception exc) {
			Throwables.propagate(exc);
		}
	}

}
