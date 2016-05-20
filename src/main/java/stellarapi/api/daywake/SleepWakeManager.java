package stellarapi.api.daywake;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.lib.config.IConfigHandler;

public class SleepWakeManager implements IConfigHandler {
	
	private boolean enabled;
	//true for first, false for last
	private boolean mode;
	private List<WakeHandler> wakeHandlers = Lists.newArrayList();
	
	/**
	 * Registers wake handler.
	 * @param handler the wake handler to register
	 * */
	public void register(String name, IWakeHandler handler, boolean defaultEnabled) {
		wakeHandlers.add(0, new WakeHandler(name, handler, defaultEnabled));
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configuration for Waking System.");
		config.setCategoryLanguageKey(category, "config.category.server.wake");
		config.setCategoryRequiresWorldRestart(category, true);
		
		Property allEnabled = config.get(category, "Custom_Wake_Enabled", true);
		allEnabled.setComment("Enable/Disable wake system provided by Stellar API");
		allEnabled.setRequiresWorldRestart(true);
		allEnabled.setLanguageKey("config.property.wakeenable");
		
		Property mode = config.get(category, "Wake_Mode", "latest")
				.setValidValues(new String[]{"earliest", "latest"});
		mode.setComment("You can choose earliest or latest available wake time"
				+ "among these wake properties");
		mode.setRequiresWorldRestart(true);
		mode.setLanguageKey("config.property.wakemode");
		
		for(WakeHandler entry : this.wakeHandlers) {
			String cat2 = category + Configuration.CATEGORY_SPLITTER + entry.name.toLowerCase();
			Property enabled = config.get(cat2, "Enabled", entry.enabled);
			enabled.setComment("Enable this wake property.");
			enabled.setRequiresWorldRestart(true);
			enabled.setLanguageKey("config.property.enablewake");
			entry.handler.setupConfig(config, cat2);
		}
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		this.enabled = config.getCategory(category).get("Custom_Wake_Enabled").getBoolean();
		this.mode = config.getCategory(category).get("Wake_Mode").getString().equals("first");
		
		for(WakeHandler entry : this.wakeHandlers) {
			String cat2 = category + Configuration.CATEGORY_SPLITTER + entry.name.toLowerCase();
			entry.enabled = config.getCategory(cat2).get("Enabled").getBoolean();
			entry.handler.loadFromConfig(config, cat2);
		}
	}

	@Override
	public void saveToConfig(Configuration config, String category) { }
	
	/**
	 * Calculates time to wake.
	 * @param world the world to wake up
	 * @param defaultWakeTime the default wake time
	 * */
	public long getWakeTime(World world, long defaultWakeTime) {
		
		ICelestialCoordinate coordinate = StellarAPIReference.getCoordinate(world);
		CelestialEffectors lightSources = StellarAPIReference.getEffectors(world, IEffectorType.Light);
		
		long wakeTime;
		boolean accepted = false;

		if(this.mode)
		{
			wakeTime=Integer.MAX_VALUE;
			for(WakeHandler handler : wakeHandlers) {
				if(handler.enabled && handler.handler.accept(world, lightSources, coordinate))
				{
					wakeTime = Math.min(wakeTime, handler.handler.getWakeTime(world, lightSources, coordinate, world.getWorldTime()));
					accepted = true;
				}
			}
		} else {
			wakeTime=Integer.MIN_VALUE;
			for(WakeHandler handler : wakeHandlers) {
				if(handler.enabled && handler.handler.accept(world, lightSources, coordinate)) {
					wakeTime = Math.max(wakeTime, handler.handler.getWakeTime(world, lightSources, coordinate, world.getWorldTime()));
					accepted = true;
				}
			}
		}

		if(accepted)
			return wakeTime;
		
		return defaultWakeTime;
	}
	
	/**
	 * Checks if sleep is possible or not.
	 * @param world the world to wake up
	 * @param defaultStatus the default status
	 * @return sleep possibility status, should be one of
	 * {@code EntityPlayer.EnumStatus.OK} or
	 * {@code EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW} or
	 * {@code EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE}
	 * */
	public EntityPlayer.EnumStatus getSleepPossibility(World world, EntityPlayer.EnumStatus defaultStatus) {
		ICelestialCoordinate coordinate = StellarAPIReference.getCoordinate(world);
		CelestialEffectors lightSources = StellarAPIReference.getEffectors(world, IEffectorType.Light);
		
		EntityPlayer.EnumStatus status = EntityPlayer.EnumStatus.OK;
		boolean accepted = false;

		for(WakeHandler handler : this.wakeHandlers) {
			if(status == EntityPlayer.EnumStatus.OK && handler.enabled && handler.handler.accept(world, lightSources, coordinate)) {
				status = handler.handler.getSleepPossibility(world, lightSources, coordinate, world.getWorldTime());
				accepted = true;
			}
		}

		if(accepted)
			return status;
		
		return defaultStatus;
	}
	
	private class WakeHandler {
		public WakeHandler(String name, IWakeHandler handler, boolean enabled) {
			this.name = name;
			this.handler = handler;
			this.enabled = enabled;
		}
		protected String name;
		protected IWakeHandler handler;
		protected boolean enabled;
	}
}
