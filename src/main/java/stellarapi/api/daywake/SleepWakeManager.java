package stellarapi.api.daywake;

import java.lang.annotation.Annotation;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.SAPIReferences;
import stellarapi.api.lib.config.DynamicConfig;

public class SleepWakeManager {
	// TODO Remove this placeholder example - not needed anymore.
	@Config.Name("Custom_Wake_Enabled")
	@Config.LangKey("config.property.wakeenable")
	@Config.Comment("Enable/Disable wake system provided by Stellar API")
	@Config.RequiresWorldRestart
	private boolean enabled = true;

	// true for first, false for last
	@Config.Name("Wake_Mode")
	@DynamicConfig.BooleanCycle({"latest", "earliest"})
	@Config.LangKey("config.property.wakemode")
	@Config.Comment("You can choose earliest or latest available wake time"
			+ "among these wake properties")
	@Config.RequiresWorldRestart
	private boolean mode = true;

	@DynamicConfig.DynamicElementProperty(
			affected={Config.Name.class, Config.LangKey.class, Config.Comment.class},
			id = "wakeHandlers")
	@DynamicConfig.Collection
	@Config.RequiresWorldRestart
	private List<WakeHandler> wakeHandlers = Lists.newArrayList();

	private class WakeHandler {
		public WakeHandler(String name, IWakeHandler handler, boolean enabled) {
			this.name = name;
			this.handler = handler;
			this.enabled = enabled;
		}

		@DynamicConfig.NameField
		protected transient String name;

		@Config.Name("Enabled")
		@Config.LangKey("config.property.enablewake")
		@Config.Comment("Enable this wake property.")
		@Config.RequiresWorldRestart
		protected boolean enabled;

		@DynamicConfig.Expand
		protected IWakeHandler handler;
	}

	/*@DynamicConfig.EvaluatorID("wakeHandlers")
	public Config.Name getName(final String key, final WakeHandler individual) {
		return new Config.Name() {
			@Override
			public Class<? extends Annotation> annotationType() { return Config.Name.class; }

			@Override
			public String value() { return individual.name; }
		};
	}*/

	@DynamicConfig.EvaluatorID("wakeHandlers")
	public Config.LangKey getLangKey(final String key, final WakeHandler individual) {
		return null;
	}

	@DynamicConfig.EvaluatorID("wakeHandlers")
	public Config.Comment getComment(final String key, final WakeHandler individual) {
		return null;
	}

	/**
	 * Registers wake handler.
	 * 
	 * @param handler
	 *            the wake handler to register
	 */
	public void register(String name, IWakeHandler handler, boolean defaultEnabled) {
		wakeHandlers.add(0, new WakeHandler(name, handler, defaultEnabled));
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configuration for Waking System.");
		config.setCategoryLanguageKey(category, "config.category.server.wake");
		config.setCategoryRequiresWorldRestart(category, true);
	}

	/**
	 * Calculates time to wake.
	 * 
	 * @param world
	 *            the world to wake up
	 * @param defaultWakeTime
	 *            the default wake time
	 */
	public long getWakeTime(World world, long defaultWakeTime) {
		ICelestialCoordinate coordinate = SAPIReferences.getCoordinate(world);
		CelestialEffectors lightSources = SAPIReferences.getEffectors(world, IEffectorType.Light);

		long wakeTime;
		boolean accepted = false;

		if (this.mode) {
			wakeTime = Integer.MAX_VALUE;
			for (WakeHandler handler : wakeHandlers) {
				if (handler.enabled && handler.handler.accept(world, lightSources, coordinate)) {
					wakeTime = Math.min(wakeTime,
							handler.handler.getWakeTime(world, lightSources, coordinate, world.getWorldTime()));
					accepted = true;
				}
			}
		} else {
			wakeTime = Integer.MIN_VALUE;
			for (WakeHandler handler : wakeHandlers) {
				if (handler.enabled && handler.handler.accept(world, lightSources, coordinate)) {
					wakeTime = Math.max(wakeTime,
							handler.handler.getWakeTime(world, lightSources, coordinate, world.getWorldTime()));
					accepted = true;
				}
			}
		}

		if (accepted)
			return wakeTime;

		return defaultWakeTime;
	}

	/**
	 * Checks if sleep is possible or not.
	 * 
	 * @param world
	 *            the world to wake up
	 * @param defaultStatus
	 *            the default status
	 * @return sleep possibility status, should be one of
	 *         {@code EntityPlayer.SleepResult.OK} or
	 *         {@code EntityPlayer.SleepResult.NOT_POSSIBLE_NOW} or
	 *         {@code EntityPlayer.SleepResult.NOT_POSSIBLE_HERE}
	 */
	public EntityPlayer.SleepResult getSleepPossibility(World world, EntityPlayer.SleepResult defaultStatus) {
		ICelestialCoordinate coordinate = SAPIReferences.getCoordinate(world);
		CelestialEffectors lightSources = SAPIReferences.getEffectors(world, IEffectorType.Light);

		EntityPlayer.SleepResult status = EntityPlayer.SleepResult.OK;
		boolean accepted = false;

		for (WakeHandler handler : this.wakeHandlers) {
			if (status == EntityPlayer.SleepResult.OK && handler.enabled
					&& handler.handler.accept(world, lightSources, coordinate)) {
				status = handler.handler.getSleepPossibility(world, lightSources, coordinate, world.getWorldTime());
				accepted = true;
			}
		}

		if (accepted)
			return status;

		return defaultStatus;
	}
}
