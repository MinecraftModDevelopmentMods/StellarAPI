package stellarapi.feature.gui.loading;

import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;
import net.minecraftforge.fml.client.FMLClientHandler;
import stellarapi.api.gui.loading.ICombinedProgressUpdate;

public class CombinedLoadingScreenRenderer extends LoadingScreenRenderer implements ICombinedProgressUpdate {
	
	private static String vanillaKey = "Minecraft";
	
	private static float sizePerElement = 50.0f;
	
	private Minecraft mc;
	private ScaledResolution scaledResolution;
    private Framebuffer rendered;
	
	private Map<String, LoadingDelegate> delegates = Maps.newHashMap();
	private Set<String> currentShown = Sets.newHashSet();
	private Ordering<String> ordering = Ordering.<Boolean>natural().onResultOf(new Function<String, Boolean>() {
		@Override
		public Boolean apply(String input) {
			return !input.equals(vanillaKey);
		}
	}).compound(Ordering.natural());
		
	public CombinedLoadingScreenRenderer(Minecraft minecraft) {
		super(minecraft);
		this.mc = minecraft;
		this.scaledResolution = new ScaledResolution(minecraft);
        this.rendered = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
        rendered.setFramebufferFilter(9728);
		this.addProgress(vanillaKey);
	}
	
	@Override
    public void resetProgressAndMessage(String p_73721_1_) {
		if(delegates.size() == 1)
			super.resetProgressAndMessage(p_73721_1_);
		else delegates.get(vanillaKey).resetProgressAndMessage(p_73721_1_);
    }
	
	@Override
    public void displaySavingString(String p_73719_1_) {
		if(delegates.size() == 1)
			super.displaySavingString(p_73719_1_);
		else delegates.get(vanillaKey).displaySavingString(p_73719_1_);
    }
	

	@Override
    public void displayLoadingString(String p_73720_1_) {
		if(delegates.size() == 1)
			super.displayLoadingString(p_73720_1_);
		else delegates.get(vanillaKey).displayLoadingString(p_73720_1_);
    }

	@Override
    public void setLoadingProgress(int p_73718_1_) {
		if(delegates.size() == 1)
			super.setLoadingProgress(p_73718_1_);
		else delegates.get(vanillaKey).setLoadingProgress(p_73718_1_);
    }


	@Override
	public IProgressUpdate getProgress(String id) {
		if(id.equals(vanillaKey))
			throw new IllegalArgumentException("Can't get vanilla progress!");
		if(delegates.containsKey(id))
			return delegates.get(id);
		else return this.addProgress(id);
	}
	
	private IProgressUpdate addProgress(String id) {
		LoadingDelegate delegate = new LoadingDelegate(id);
		delegates.put(id, delegate);
		return delegate;
	}

	@Override
	public void removeProgress(String id) {
		if(id.equals(vanillaKey))
			throw new IllegalArgumentException("Can't remove vanilla progress!");
		if(delegates.containsKey(id)) {
			delegates.remove(id).displayLoadingString("");
		}
	}
	
	
	public void reRender(String changedId) {
		ScaledResolution scaledresolution = new ScaledResolution(this.mc);
    	int j = scaledresolution.getScaleFactor();
    	int k = scaledresolution.getScaledWidth();
    	int l = scaledresolution.getScaledHeight();

    	if (OpenGlHelper.isFramebufferEnabled())
    	{
    		rendered.framebufferClear();
    	}
    	else
    	{
    		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
    	}

    	rendered.bindFramebuffer(false);
    	GlStateManager.matrixMode(GL11.GL_PROJECTION);
    	GlStateManager.loadIdentity();
    	GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
    	GlStateManager.matrixMode(GL11.GL_MODELVIEW);
    	GlStateManager.loadIdentity();
    	GlStateManager.translate(0.0F, 0.0F, -200.0F);

    	if (!OpenGlHelper.isFramebufferEnabled())
    	{
    		GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    	}
    	
    	try {
    	if (!FMLClientHandler.instance().handleLoadingScreen(scaledresolution)) {
    		Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    		mc.getTextureManager().bindTexture(Gui.optionsBackground);
    		float f = 32.0F;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(0.0D, (double)l, 0.0D).tex(0.0D, (double)((float)l / f)).color(64, 64, 64, 255).endVertex();
            worldrenderer.pos((double)k, (double)l, 0.0D).tex((double)((float)k / f), (double)((float)l / f)).color(64, 64, 64, 255).endVertex();
            worldrenderer.pos((double)k, 0.0D, 0.0D).tex((double)((float)k / f), 0.0D).color(64, 64, 64, 255).endVertex();
            worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
    		tessellator.draw();

    		GlStateManager.translate(0.0f, -sizePerElement*(currentShown.size()-1)/2, 0.0f);
    		GlStateManager.enableBlend();
    		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    		
    		for(String shown : ordering.sortedCopy(currentShown))
    		{
    			delegates.get(shown).renderProgress(scaledresolution);
    			GL11.glTranslatef(0.0f, sizePerElement, 0.0f);
    		}
    	}
    	}catch (java.io.IOException e)
        {
            com.google.common.base.Throwables.propagate(e);
        }
        
    	rendered.unbindFramebuffer();

    	if (OpenGlHelper.isFramebufferEnabled())
    	{
            rendered.framebufferRender(k * j, l * j);
    	}

		mc.updateDisplay();

		try
		{
			Thread.yield();
		}
		catch (Exception exception) {
			;
		}
	}
	
	private class LoadingDelegate extends LoadingScreenRenderer {
		
		private final String id;
	    private String currentDisplayed, currentContext;
	    private int progress;
		private long updateTime = Minecraft.getSystemTime();

		private boolean field_73724_e;

		public LoadingDelegate(String id) {
			super(mc);
			this.id = id;
		}

		@Override
		public void resetProgressAndMessage(String p_73721_1_) {
	        this.field_73724_e = false;
	        this.displayString(p_73721_1_);
	    }

		@Override
	    public void displaySavingString(String p_73720_1_) {
	        this.field_73724_e = true;
	        this.displayString(p_73720_1_);
	    }

	    public void displayString(String p_73722_1_)
	    {
	        if(p_73722_1_.isEmpty())
	        	currentShown.remove(this.id);
	        else if(!currentShown.contains(this.id))
	        	currentShown.add(this.id);
	        
	        this.currentDisplayed = p_73722_1_;

	        GlStateManager.clear(256);
	        GlStateManager.matrixMode(5889);
	        GlStateManager.loadIdentity();

	        if (OpenGlHelper.isFramebufferEnabled())
	        {
	        	int i = scaledResolution.getScaleFactor();
	        	GlStateManager.ortho(0.0D, (double)(scaledResolution.getScaledWidth() * i), (double)(scaledResolution.getScaledHeight() * i), 0.0D, 100.0D, 300.0D);
	        }
	        else
	        {
	        	ScaledResolution scaledresolution = new ScaledResolution(mc);
	        	GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
	        }

	        GlStateManager.matrixMode(5888);
	        GlStateManager.loadIdentity();
	        GlStateManager.translate(0.0F, 0.0F, -200.0F);
	    }

	    @Override
	    public void displayLoadingString(String p_73719_1_) {
	        if(p_73719_1_.isEmpty())
	        	currentShown.remove(this.id);
	        else if(!currentShown.contains(this.id))
	        	currentShown.add(this.id);
	       	
	        this.updateTime = 0L;
	        this.currentContext = p_73719_1_;
	        this.setLoadingProgress(-1);
	        this.updateTime = 0L;
	    }

	    @Override
	    public void setLoadingProgress(int p_73718_1_) {
	    	long j = Minecraft.getSystemTime();
	    	
	    	if (j - this.updateTime >= 100L) {
		    	this.progress = p_73718_1_;
	    		this.updateTime = j;
	    		reRender(this.id);
	    	}
	    }
	    
	    public void renderProgress(ScaledResolution scaledresolution) {
            int j = scaledresolution.getScaleFactor();
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();

	    	Tessellator tessellator = Tessellator.getInstance();
	    	WorldRenderer worldRenderer = tessellator.getWorldRenderer();
	    	
	    	if (this.progress >= 0)
	    	{
                int i1 = 100;
                int j1 = 2;
                int k1 = k / 2 - i1 / 2;
                int l1 = l / 2 + 16;
	    		GlStateManager.disableTexture2D();
	    		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
	    		worldRenderer.pos((double)k1, (double)l1, 0.0D).color(128, 128, 128, 255).endVertex();
	    		worldRenderer.pos((double)k1, (double)(l1 + j1), 0.0D).color(128, 128, 128, 255).endVertex();
	    		worldRenderer.pos((double)(k1 + i1), (double)(l1 + j1), 0.0D).color(128, 128, 128, 255).endVertex();
	    		worldRenderer.pos((double)k1, (double)l1, 0.0D).color(128, 255, 128, 255).endVertex();
	    		worldRenderer.pos((double)k1, (double)(l1 + j1), 0.0D).color(128, 255, 128, 255).endVertex();
                worldRenderer.pos((double)(k1 + progress), (double)(l1 + j1), 0.0D).color(128, 255, 128, 255).endVertex();
                worldRenderer.pos((double)(k1 + progress), (double)l1, 0.0D).color(128, 255, 128, 255).endVertex();
	    		tessellator.draw();
	    		GlStateManager.enableTexture2D();
	    	}
	    	
    		mc.fontRendererObj.drawStringWithShadow(this.currentDisplayed, (k - mc.fontRendererObj.getStringWidth(this.currentDisplayed)) / 2, l / 2 - 4 - 12, 16777215);
    		mc.fontRendererObj.drawStringWithShadow(this.currentContext, (k - mc.fontRendererObj.getStringWidth(this.currentContext)) / 2, l / 2 - 4 + 4, 16777215);
	    }
	}
}
