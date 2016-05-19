package stellarapi.feature.gui.loading;

import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import stellarapi.api.gui.loading.ICombinedProgressUpdate;

public class CombinedLoadingScreenRenderer extends LoadingScreenRenderer implements ICombinedProgressUpdate {
	
	private static String vanillaKey = "Minecraft";
	
	private static float sizePerElement = 50.0f;
	
	private Minecraft mc;
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
    public void displayProgressMessage(String p_73720_1_) {
		if(delegates.size() == 1)
			super.displayProgressMessage(p_73720_1_);
		else delegates.get(vanillaKey).displayProgressMessage(p_73720_1_);
    }

	@Override
    public void func_73722_d(String p_73722_1_) {
		if(delegates.size() == 1)
			super.func_73722_d(p_73722_1_);
		else delegates.get(vanillaKey).func_73722_d(p_73722_1_);
    }

	@Override
    public void resetProgresAndWorkingMessage(String p_73719_1_) {
		if(delegates.size() == 1)
			super.resetProgresAndWorkingMessage(p_73719_1_);
		else delegates.get(vanillaKey).resetProgresAndWorkingMessage(p_73719_1_);
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
			delegates.remove(id).resetProgresAndWorkingMessage("");
		}
	}
	
	
	public void reRender(String changedId) {
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
    	int k = scaledresolution.getScaleFactor();
    	int l = scaledresolution.getScaledWidth();
    	int i1 = scaledresolution.getScaledHeight();

    	if (OpenGlHelper.isFramebufferEnabled())
    	{
    		rendered.framebufferClear();
    	}
    	else
    	{
    		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    	}

    	rendered.bindFramebuffer(false);
    	GL11.glMatrixMode(GL11.GL_PROJECTION);
    	GL11.glLoadIdentity();
    	GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
    	GL11.glMatrixMode(GL11.GL_MODELVIEW);
    	GL11.glLoadIdentity();
    	GL11.glTranslatef(0.0F, 0.0F, -200.0F);

    	if (!OpenGlHelper.isFramebufferEnabled())
    	{
    		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    	}
    	
    	if (!FMLClientHandler.instance().handleLoadingScreen(scaledresolution)) {
    		Tessellator tessellator = Tessellator.instance;
    		mc.getTextureManager().bindTexture(Gui.optionsBackground);
    		float f = 32.0F;
    		tessellator.startDrawingQuads();
    		tessellator.setColorOpaque_I(4210752);
    		tessellator.addVertexWithUV(0.0D, (double)i1, 0.0D, 0.0D, (double)((float)i1 / f));
    		tessellator.addVertexWithUV((double)l, (double)i1, 0.0D, (double)((float)l / f), (double)((float)i1 / f));
    		tessellator.addVertexWithUV((double)l, 0.0D, 0.0D, (double)((float)l / f), 0.0D);
    		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    		tessellator.draw();

    		GL11.glTranslatef(0.0f, -sizePerElement*(currentShown.size()-1)/2, 0.0f);
    		GL11.glEnable(GL11.GL_BLEND);
    		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    		
    		for(String shown : ordering.sortedCopy(currentShown))
    		{
    			delegates.get(shown).renderProgress(scaledresolution);
    			GL11.glTranslatef(0.0f, sizePerElement, 0.0f);
    		}
    	}
		
    	rendered.unbindFramebuffer();

    	if (OpenGlHelper.isFramebufferEnabled())
    	{
    		rendered.framebufferRender(l * k, i1 * k);
    	}

		mc.func_147120_f();

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
		private long updateTick = Minecraft.getSystemTime();

		public LoadingDelegate(String id) {
			super(mc);
			this.id = id;
		}

		public void resetProgressAndMessage(String p_73721_1_) {
	        super.resetProgressAndMessage(p_73721_1_);
	        
	    }

	    public void displayProgressMessage(String p_73720_1_) {
	        super.displayProgressMessage(p_73720_1_);
	    }

	    public void func_73722_d(String p_73722_1_)
	    {
	        if(p_73722_1_.isEmpty())
	        	currentShown.remove(this.id);
	        else if(!currentShown.contains(this.id))
	        	currentShown.add(this.id);
	        
	        this.currentDisplayed = p_73722_1_;
	        super.func_73722_d(p_73722_1_);
	    }

	    public void resetProgresAndWorkingMessage(String p_73719_1_) {
	        if(p_73719_1_.isEmpty())
	        	currentShown.remove(this.id);
	        else if(!currentShown.contains(this.id))
	        	currentShown.add(this.id);
	       	
	        this.updateTick = 0L;
	        this.currentContext = p_73719_1_;
	        super.resetProgresAndWorkingMessage(p_73719_1_);
	        this.updateTick = 0L;
	    }

	    public void setLoadingProgress(int p_73718_1_) {
	    	long j = Minecraft.getSystemTime();
	    	
	    	if (j - this.updateTick >= 100L) {
		    	this.progress = p_73718_1_;
	    		this.updateTick = j;
	    		reRender(this.id);
	    	}
	    }
	    
	    public void renderProgress(ScaledResolution scaledresolution) {
	    	int k = scaledresolution.getScaleFactor();
	    	int l = scaledresolution.getScaledWidth();
	    	int i1 = scaledresolution.getScaledHeight();

	    	Tessellator tessellator = Tessellator.instance;

	    	if (this.progress >= 0)
	    	{
	    		byte b0 = 100;
	    		byte b1 = 2;
	    		int j1 = l / 2 - b0 / 2;
	    		int k1 = i1 / 2 + 12;
	    		GL11.glDisable(GL11.GL_TEXTURE_2D);
	    		tessellator.startDrawingQuads();
	    		tessellator.setColorOpaque_I(8421504);
	    		tessellator.addVertex((double)j1, (double)k1, 0.0D);
	    		tessellator.addVertex((double)j1, (double)(k1 + b1), 0.0D);
	    		tessellator.addVertex((double)(j1 + b0), (double)(k1 + b1), 0.0D);
	    		tessellator.addVertex((double)(j1 + b0), (double)k1, 0.0D);
	    		tessellator.setColorOpaque_I(8454016);
	    		tessellator.addVertex((double)j1, (double)k1, 0.0D);
	    		tessellator.addVertex((double)j1, (double)(k1 + b1), 0.0D);
	    		tessellator.addVertex((double)(j1 + this.progress), (double)(k1 + b1), 0.0D);
	    		tessellator.addVertex((double)(j1 + this.progress), (double)k1, 0.0D);
	    		tessellator.draw();
	    		GL11.glEnable(GL11.GL_TEXTURE_2D);
	    	}
	    	
    		mc.fontRenderer.drawStringWithShadow(this.currentDisplayed, (l - mc.fontRenderer.getStringWidth(this.currentDisplayed)) / 2, i1 / 2 - 4 - 12, 16777215);
    		mc.fontRenderer.drawStringWithShadow(this.currentContext, (l - mc.fontRenderer.getStringWidth(this.currentContext)) / 2, i1 / 2 - 4 + 4, 16777215);
	    }

	    public void func_146586_a() {}
	}
}
