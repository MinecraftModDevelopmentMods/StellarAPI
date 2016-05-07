package stellarapi.api.gui.content.list;

import stellarapi.api.gui.content.IGuiPosition;

public interface IHasFixedListController extends ISimpleListController {

	public boolean isModifiableFirst();
	public IGuiPosition wrapFixedPosition(IGuiPosition position, IGuiPosition listPos);
	public IGuiPosition wrapModifiablePosition(IGuiPosition position, IGuiPosition listPos);

}
