package stellarapi.lib.render;

/**
 * @param Pass the immutable object to represent render pass
 * @param Model the model to render, need not inherit IRenderModel while it is recommended
 * @param RenderContext mutable render context information
 * */
public interface IGenericRenderer<Pass, Model, RenderContext> {
	/**
	 * Initialize Renderer before any pass
	 * */
	public void preRender(RenderContext info);

	/**
	 * Render for pass
	 * */
	public void renderPass(Model model, Pass pass, RenderContext info);

	/**
	 * Finalize(Reduce) Renderer after any pass
	 * */
	public void postRender(RenderContext info);
}