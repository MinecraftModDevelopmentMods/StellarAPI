package stellarium.world;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import stellarapi.api.lib.config.DynamicConfig;
import stellarapi.api.lib.config.property.ConfigPropertyBoolean;
import stellarapi.api.lib.config.property.ConfigPropertyDouble;
import stellarapi.api.lib.config.property.ConfigPropertyDoubleList;
import stellarapi.api.lib.config.property.ConfigPropertyString;
import stellarium.api.EnumSkyProperty;
import stellarium.api.ISkyRenderType;
import stellarium.api.ISkyType;
import stellarium.api.StellarSkyAPI;

public class PerDimensionSettings {

	private transient DimensionType dimensionType;

	@Config.Name("Patch_Provider")
	@Config.LangKey("config.property.dimension.patchprovider")
	@Config.Comment("Determine whether or not patch provider. "
			+ "Cannot adjust longitude and latitude when this is false.")
	@Config.RequiresWorldRestart
	private boolean patchProvider = true;


	@Config.Name("Latitude")
	@Config.RangeDouble(min = -90.0, max = +90.0)
	@Config.LangKey("config.property.dimension.latitude")
	@Config.Comment("Latitude on this world, in Degrees.")
	@Config.RequiresWorldRestart
	public double latitude;

	@Config.Name("Longitude")
	@Config.RangeDouble(min = 0.0, max = 360.0)
	@Config.LangKey("config.property.dimension.longitude")
	@Config.Comment("Longitude on this world, in Degrees. (East is +)")
	@Config.RequiresWorldRestart
	public double longitude;


	@Config.Name("Sky_Renderer_Type")
	@DynamicConfig.DynamicProperty({DynamicConfig.StringCycle.class})
	@Config.LangKey("config.property.dimension.skyrenderertype")
	@Config.Comment("Sky renderer type for this dimension.\n"
        		+ "There are 'Overworld Sky', 'End Sky' type by default.")
	@Config.RequiresWorldRestart
	private String skyRenderType;


	@Config.Name("Hide_Objects_Under_Horizon")
	@Config.LangKey("config.property.dimension.hidehorizonobj")
	@Config.Comment("Determine whether or not hide objects under horizon.")
	@Config.RequiresWorldRestart
	private boolean hideObjectsUnderHorizon;


	@Config.Name("Atmosphere_Scale_Height")
	@Config.RangeDouble(min = 1.0e-4, max = 1.0)
	@Config.LangKey("config.property.dimension.atmscaleheight")
	@Config.Comment("Scale Height of the atmosphere relative to the radius.\n"
       			+ "This determines the thickness of the atmosphere.")
	@Config.RequiresWorldRestart
	private double atmScaleHeight = 1 / 800.0;
	@Config.Name("Atmosphere_Total_Height")
	private double atmTotalHeight = 20.0 / 800.0;
	@Config.Name("Atmosphere_Height_Offset")
	private double atmHeightOffet = 0.2;
	@Config.Name("Atmosphere_Height_Increase_Scale")
	private double atmHeightIncScale = 1.0;

	@Config.Name("Sky_Extinction_Factors")
	private double[] atmExtinctionFactor;

	@Config.Name("Allow_Atmospheric_Refraction")
	private boolean allowRefraction;

	// FIXME Fix Sunlight Multiplier Property
	@Config.Name("SunLight_Multiplier")
	private double sunlightMultiplier = 1.0;

	@Config.Name("Sky_Dispersion_Rate")
	private double skyDispersionRate;
	@Config.Name("Light_Pollution_Rate")
	private double lightPollutionRate = 1.0;

	@Config.Name("Minimum_Sky_Render_Brightness")
	private double minimumSkyRenderBrightness;

	@Config.Name("Landscape_Enabled")
	private boolean landscapeEnabled = false;


	private transient ISkyType skyType;
	
	public PerDimensionSettings(DimensionType dimensionType) {
		this.dimensionType = dimensionType;
		
		this.skyType = StellarSkyAPI.getSkyType(dimensionType.getName());

		this.skyRenderType = skyType.possibleTypes().get(0).getName();

		this.latitude = skyType.getDefaultDouble(EnumSkyProperty.Latitude);
		this.longitude = skyType.getDefaultDouble(EnumSkyProperty.Longitude);

		this.hideObjectsUnderHorizon = skyType.getDefaultBoolean(EnumSkyProperty.HideObjectsUnderHorizon);

		this.atmExtinctionFactor = skyType.getDefaultDoubleList(EnumSkyProperty.SkyExtinctionFactors);

		this.allowRefraction = skyType.getDefaultBoolean(EnumSkyProperty.AllowRefraction);

		this.skyDispersionRate = skyType.getDefaultDouble(EnumSkyProperty.SkyDispersionRate);

		this.minimumSkyRenderBrightness = skyType.getDefaultDouble(EnumSkyProperty.SkyRenderBrightness);
	}

	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configurations for this dimension.");
		config.setCategoryRequiresWorldRestart(category, true);
        
		String[] transformed = Lists.transform(skyType.possibleTypes(), new Function<ISkyRenderType, String>() {
			@Override
			public String apply(ISkyRenderType input) {
				return input.getName();
			}
		}).toArray(new String[0]); // TODO Render Type Dynamic Valid Values

       	
       	propAtmTotalHeight.setComment("Total Height of the atmosphere relative to the radius.\n"
       			+ "This determines the accuracy of the atmosphere, relative to the scale height.");
       	propAtmTotalHeight.setRequiresWorldRestart(true);
       	propAtmTotalHeight.setLanguageKey("config.property.dimension.atmtotalheight");
       	propAtmTotalHeight.setMinValue(1.0e-4);
       	propAtmTotalHeight.setMaxValue(5.0);
       	
       	propAtmHeightOffset.setComment("Height on horizon in the Atmosphere, in Scale Height unit.");
       	propAtmHeightOffset.setRequiresWorldRestart(true);
       	propAtmHeightOffset.setLanguageKey("config.property.dimension.atmheightoffset");
       	propAtmHeightOffset.setMinValue(0.0);
       	propAtmHeightOffset.setMaxValue(100.0);
       	
       	propAtmHeightIncScale.setComment("Increase scale of height in the atmosphere, with Default 1.0.");
       	propAtmHeightIncScale.setRequiresWorldRestart(true);
       	propAtmHeightIncScale.setLanguageKey("config.property.dimension.atmheightincscale");
       	propAtmHeightIncScale.setMinValue(-1.0);
       	propAtmHeightIncScale.setMaxValue(10.0);
       	
       	propAtmExtinctionFactor.setComment("Extinction Factor for RVB(or RGB) of the atmosphere,"
       			+ "affects both sky rendering and extinction of stellar objects.");
       	propAtmExtinctionFactor.setRequiresWorldRestart(true);
       	propAtmExtinctionFactor.setLanguageKey("config.property.dimension.atmextinctionfactor");
       	propAtmExtinctionFactor.setIsListLengthFixed(true);
       	propAtmExtinctionFactor.setMaxListLength(3);
       	
        propAllowRefraction.setComment("Determine whether or not apply the atmospheric refraction.");
        propAllowRefraction.setRequiresWorldRestart(true);
        propAllowRefraction.setLanguageKey("config.property.dimension.allowrefraction");

        propSunlightMultiplier.setComment("Relative amount of sunlight on the dimension.\n"
        		+ "Setting this to 0.0 will make the world very dark.");
        propSunlightMultiplier.setRequiresWorldRestart(true);
        propSunlightMultiplier.setLanguageKey("config.property.dimension.sunlightmultiplier");
        propSunlightMultiplier.setMinValue(0.0);
        propSunlightMultiplier.setMaxValue(1.0);
        
        propSkyDispersionRate.setComment("Relative strength of sky dispersion on the dimension.\n"
        		+ "The effect is similar with sunlight multiplier on client, but usually don't affect the server, e.g. do not spawn mobs.");
        propSkyDispersionRate.setRequiresWorldRestart(true);
        propSkyDispersionRate.setLanguageKey("config.property.dimension.skydispersionrate");
        propSkyDispersionRate.setMinValue(0.0);
        propSkyDispersionRate.setMaxValue(1.0);
        
        propLightPollutionRate.setComment("Relative strength of light pollution on the dimension.\n"
        		+ "Only affects the sky color and visibility of stars/milky way.");
        propLightPollutionRate.setRequiresWorldRestart(true);
        propLightPollutionRate.setLanguageKey("config.property.dimension.lightpollutionrate");
        propLightPollutionRate.setMinValue(0.0);
        propLightPollutionRate.setMaxValue(1.0);
        
        propMinimumSkyRenderBrightness.setComment("Minimum brightness of skylight which (only) affects the rendering.");
        propMinimumSkyRenderBrightness.setRequiresWorldRestart(true);
        propMinimumSkyRenderBrightness.setLanguageKey("config.property.dimension.minimumskybrightness");
        propMinimumSkyRenderBrightness.setMinValue(0.0);
        propMinimumSkyRenderBrightness.setMaxValue(1.0);
        
        propLandscapeEnabled.setComment("Whether landscape rendering on this world is enabled.");
        propLandscapeEnabled.setRequiresWorldRestart(true);
        propLandscapeEnabled.setLanguageKey("config.property.dimension.enablelandscape");
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		super.loadFromConfig(config, category);

		if(!this.doesPatchProvider()) {
			propLatitude.setAsDefault();
			propLongitude.setAsDefault();
			propAtmExtinctionFactor.setAsDefault();
			propAllowRefraction.setAsDefault();
			propSunlightMultiplier.setAsDefault();
			propSkyDispersionRate.setAsDefault();
			propLightPollutionRate.setAsDefault();
			propMinimumSkyRenderBrightness.setAsDefault();
		}

       	this.latitude = propLatitude.getDouble();
   		this.longitude = propLongitude.getDouble();
	}


	public boolean doesPatchProvider() {
		return propPatchProvider.getBoolean();
	}
	
	public boolean allowRefraction() {
		return propAllowRefraction.getBoolean();
	}
	
	public boolean hideObjectsUnderHorizon() {
		return propHideObjectsUnderHorizon.getBoolean();
	}
	
	public double getSunlightMultiplier() {
		return propSunlightMultiplier.getDouble();
	}
	
	public double getSkyDispersionRate() {
		return propSkyDispersionRate.getDouble();
	}
	
	public double getLightPollutionRate() {
		return propLightPollutionRate.getDouble();
	}

	public double getMinimumSkyRenderBrightness() {
		return propMinimumSkyRenderBrightness.getDouble();
	}
	
	public String getSkyRendererType() {
		return propRenderType.getString();
	}
	
	public boolean isLandscapeEnabled() {
		return propLandscapeEnabled.getBoolean();
	}

	public double getInnerRadius() {
		return 1.0 / propAtmScaleHeight.getDouble();
	}

	public double getOuterRadius() {
		return (1.0 + propAtmTotalHeight.getDouble()) / propAtmScaleHeight.getDouble();
	}

	public double getHeightOffset() {
		return propAtmHeightOffset.getDouble();
	}
	
	public double getHeightIncScale() {
		return propAtmHeightIncScale.getDouble();
	}
	
	public double[] extinctionRates() {
		return propAtmExtinctionFactor.getDoubleList();
	}
}