package stellarapi.lib.gui;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import stellarapi.StellarAPI;

public class GuiRenderer implements IRenderer {

	private IRenderModel currentModel = null;

	private Tessellator tessellator;
	private VertexBuffer worldRenderer;
	private TextureManager textureManager;

	private float partialTicks;

	private boolean matrixPushedTillNextRender;
	private List<IMatrixTransformation> transformation = Lists.newArrayList();
	private List<IMatrixTransformation> innerTrans = Lists.newArrayList();
	private float[] generalColor = new float[4], currentColor = new float[4];

	private RectangleBound temp = new RectangleBound(0, 0, 0, 0), tempClip = new RectangleBound(0, 0, 0, 0);

	public GuiRenderer(Minecraft minecraft) {
		this.tessellator = Tessellator.getInstance();
		this.worldRenderer = tessellator.getBuffer();
		this.textureManager = minecraft.getTextureManager();
	}

	public GuiRenderer(Tessellator tessellator, TextureManager textureManager, VertexBuffer worldRenderer) {
		this.tessellator = tessellator;
		this.worldRenderer = worldRenderer;
		this.textureManager = textureManager;
	}

	@Override
	public void bindModel(IRenderModel model) {
		this.currentModel = model;
	}

	@Override
	public void translate(float posX, float posY) {
		if (this.matrixPushedTillNextRender)
			innerTrans.add(new Translation(posX, posY));
		else
			transformation.add(new Translation(posX, posY));
	}

	@Override
	public void rotate(float angle, float x, float y, float z) {
		if (this.matrixPushedTillNextRender)
			innerTrans.add(new Rotation(angle, x, y, z));
		else
			transformation.add(new Rotation(angle, x, y, z));
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		if (this.matrixPushedTillNextRender)
			innerTrans.add(new Scale(scaleX, scaleY));
		else
			transformation.add(new Scale(scaleX, scaleY));
	}

	@Override
	public void color(float red, float green, float blue, float alpha) {
		if (this.matrixPushedTillNextRender) {
			this.currentColor[0] *= red;
			this.currentColor[1] *= green;
			this.currentColor[2] *= blue;
			this.currentColor[3] *= alpha;
		} else {
			this.generalColor[0] *= red;
			this.generalColor[1] *= green;
			this.generalColor[2] *= blue;
			this.generalColor[3] *= alpha;
		}
	}

	@Override
	public void pushSettingTillNextRender() {
		if (this.matrixPushedTillNextRender)
			return;

		this.matrixPushedTillNextRender = true;
		System.arraycopy(this.generalColor, 0, this.currentColor, 0, currentColor.length);
	}

	@Override
	public void render(String info, IRectangleBound totalBound, IRectangleBound clipBound) {
		if (currentModel == null)
			throw new IllegalStateException("The model doesn't have got bound!");
		if (info == null) {
			StellarAPI.logger.warn(
					"Found invalid null argument as information on gui rendering." + "Replacing it with empty String.");
			info = "";
		}

		float centerX = totalBound.getMainX(0.5f);
		float centerY = totalBound.getMainY(0.5f);

		GL11.glPushMatrix();
		GL11.glTranslatef(centerX, centerY, 0.0f);

		for (IMatrixTransformation trans : this.transformation)
			trans.doTransform();
		for (IMatrixTransformation trans : this.innerTrans)
			trans.doTransform();

		temp.set(totalBound);
		temp.posX -= centerX;
		temp.posY -= centerY;
		tempClip.set(clipBound);
		tempClip.posX -= centerX;
		tempClip.posY -= centerY;

		if (!this.matrixPushedTillNextRender)
			System.arraycopy(this.generalColor, 0, this.currentColor, 0, currentColor.length);

		currentModel.renderModel(info, this.temp, this.tempClip, this.tessellator, this.worldRenderer,
				this.textureManager, this.currentColor);
		GL11.glPopMatrix();

		if (this.matrixPushedTillNextRender) {
			innerTrans.clear();
			System.arraycopy(this.generalColor, 0, this.currentColor, 0, currentColor.length);
			this.matrixPushedTillNextRender = false;
		}
	}

	@Override
	public void startRender() {
		Arrays.fill(this.generalColor, 1.0f);
		System.arraycopy(this.generalColor, 0, this.currentColor, 0, currentColor.length);
	}

	@Override
	public void endRender() {
		transformation.clear();
		innerTrans.clear();
		this.currentModel = null;
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private interface IMatrixTransformation {
		public void doTransform();
	}

	private class Translation implements IMatrixTransformation {

		private float x, y;

		public Translation(float x, float y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public void doTransform() {
			GL11.glTranslatef(x, y, 0.0f);
		}
	}

	private class Scale implements IMatrixTransformation {

		private float x, y;

		public Scale(float x, float y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public void doTransform() {
			GL11.glScalef(x, y, 1.0f);
		}
	}

	private class Rotation implements IMatrixTransformation {

		private float x, y, z, angle;

		public Rotation(float angle, float x, float y, float z) {
			this.angle = angle;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void doTransform() {
			GL11.glRotatef(angle, x, y, z);
		}
	}

	@Override
	public float getPartialTicks() {
		return this.partialTicks;
	}

	@Override
	public void preRender(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	@Override
	public void postRender(float partialTicks) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
