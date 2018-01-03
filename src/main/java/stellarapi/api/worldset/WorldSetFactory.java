package stellarapi.api.worldset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

/**
 * Factory for worldsets.
 * Should be registered on preInit.
 * */
public abstract class WorldSetFactory {
	private final ResourceLocation location;
	private final @Nullable String title;

	protected WorldSetFactory(ResourceLocation location) {
		this(location, null);
	}

	protected WorldSetFactory(ResourceLocation location, String title) {
		this.location = location;
		this.title = title;
	}

	public ResourceLocation getLocation() {
		return this.location;
	}

	// The title for the config - null when it doesn't exist
	public @Nullable String getTitle() {
		return this.title;
	}

	/**
	 * Configures the category. Won't be called when the title is <code>null</code>.
	 * */
	public abstract void configure(Configuration config, @Nonnull ConfigCategory category);

	/**
	 * Generates WorldSet from category.
	 * The category is <code>null</code> when the title is <code>null</code> as well.
	 * */
	public abstract WorldSet[] generate(@Nullable ConfigCategory category);
}