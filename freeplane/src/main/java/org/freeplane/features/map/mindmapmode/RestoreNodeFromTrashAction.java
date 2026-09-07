package org.freeplane.features.map.mindmapmode;

import java.awt.event.ActionEvent;

import org.freeplane.core.ui.AMultipleNodeAction;
import org.freeplane.core.ui.EnabledAction;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.note.NoteModel;

@EnabledAction(checkOnNodeChange = true)
public class RestoreNodeFromTrashAction extends AMultipleNodeAction {
	private static final long serialVersionUID = 1L;

	public RestoreNodeFromTrashAction() {
		super("RestoreNodeFromTrashAction");
	}

	@Override
	protected void actionPerformed(ActionEvent e, NodeModel node) {
		((MMapController) Controller.getCurrentModeController().getMapController()).restoreNodeFromTrash(node);
	}

	@Override
	public void setEnabled() {
		setEnabled(Controller.getCurrentModeController().getMapController().getSelectedNodes().stream()
				.anyMatch(node -> NoteModel.isInTrash(node)
						&& NoteModel.getNote(node) != null
						&& NoteModel.getNote(node).getTrashedFromNodeIndex() >= 0));
	}
}
