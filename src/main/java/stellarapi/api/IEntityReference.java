package stellarapi.api;

import stellarapi.api.optics.IOpticalViewer;

/** Interface of per entity reference to improve independence of api. */
public interface IEntityReference extends IOpticalViewer {

	public void updateScope(Object... additionalParams);

	public void updateFilter(Object... additionalParams);

}
