package stellarapi.api;

import stellarapi.api.optics.IOpticalViewer;

/** Interface of per entity reference to improve independence of api. */
public interface IPerEntityReference extends IOpticalViewer {

	public void updateFilter(Object... additionalParams);

}
