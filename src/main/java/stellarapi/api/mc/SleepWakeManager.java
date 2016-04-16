package stellarapi.api.mc;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.config.IConfigHandler;

public class SleepWakeManager implements IConfigHandler {
	
	private boolean enabled;
	//true for first, flase for last
	private boolean mode;
	private List<WakeHandler> wakeHandlers = Lists.newArrayList();
	
	public void register(String name, IWakeHandler handler) {
		wakeHandlers.add(new WakeHandler(name, handler));
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
		allEnabled.comment = "Enable/Disable wake system provided by Stellar Sky";
		allEnabled.setRequiresWorldRestart(true);
		allEnabled.setLanguageKey("config.property.server.wakeenable");
		
		Property mode = config.get(category, "Wake_Mode", "latest")
				.setValidValues(new String[]{"earliest", "latest"});
		mode.comment = "You can choose earliest or latest available wake time"
				+ "among these wake properties";
		mode.setRequiresWorldRestart(true);
		mode.setLanguageKey("config.property.server.wakemode");
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		this.enabled = config.getCategory(category).get("Custom_Wake_Enabled").getBoolean();
		this.mode = config.getCategory(category).get("Wake_Mode").getString().equals("first");
	}

	@Override
	public void saveToConfig(Configuration config, String category) { }
	
	public long getWakeTime(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate, long sleepTime) {
		long wakeTime;
		if(this.mode)
		{
			wakeTime=Integer.MAX_VALUE;
			for(WakeHandler handler : wakeHandlers) {
				if(handler.handler.accept(world, lightSource, coordinate))
					wakeTime = Math.min(wakeTime, handler.handler.getWakeTime(world, lightSource, coordinate, sleepTime));
			}
		} else {
			wakeTime=Integer.MIN_VALUE;
			for(WakeHandler handler : wakeHandlers) {
				if(handler.handler.accept(world, lightSource, coordinate))
					wakeTime = Math.max(wakeTime, handler.handler.getWakeTime(world, lightSource, coordinate, sleepTime));
			}
		}
		return wakeTime;
	}
	
	public boolean canSkipTime(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate, long sleepTime) {
		boolean canSkip = true;
		boolean wakeHandlerExist = false;
		for(WakeHandler handler : wakeHandlers) {
			if(handler.handler.accept(world, lightSource, coordinate)) {
				wakeHandlerExist = true;
				if(!handler.handler.canSleep(world, lightSource, coordinate, sleepTime))
					canSkip = false;
			}
		}
		return wakeHandlerExist && canSkip;
	}
	
	private class WakeHandler {
		public WakeHandler(String name, IWakeHandler handler) {
			this.name = name;
			this.handler = handler;
		}
		protected String name;
		protected IWakeHandler handler;
	}
}
