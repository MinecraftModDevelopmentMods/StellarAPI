package stellarapi.api;

import stellarapi.api.optics.IOpticalViewer;

/** Optical Viewer which needs to be updated manually to apply changes. */
public interface IUpdatedOpticalViewer extends IOpticalViewer {

	public void updateScope(Object... additionalParams);

	public void updateFilter(Object... additionalParams);

}
