package stellarapi.lib.gui.list;

import stellarapi.lib.gui.IGuiPosition;

public interface IHasFixedListController extends ISimpleListController {

	public boolean isModifiableFirst();

	public IGuiPosition wrapFixedPosition(IGuiPosition position, IGuiPosition listPos);

	public IGuiPosition wrapModifiablePosition(IGuiPosition position, IGuiPosition listPos);

}
