/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is modified by Dimitry Polivaev in 2008.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.map.mindmapmode;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.freeplane.features.note.NoteModel;

public class DeleteAction extends AFreeplaneAction {
	public static final String NAME = "DeleteAction";
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DeleteAction() {
		super(NAME);
	}

	public void actionPerformed(final ActionEvent e) {
		final ModeController modeController = Controller.getCurrentModeController();
		for (final NodeModel node : modeController.getMapController().getSelectedNodes()) {
			if (node.isRoot() || NoteModel.isTrashRoot(node)) {
				return;
			}
		}
		final Controller controller = Controller.getCurrentController();
		final boolean deletingTrash = modeController.getMapController().getSelectedNodes().stream()
				.anyMatch(NoteModel::isInTrash);
		final String message = deletingTrash
				? "Delete the entire selected Trash chain permanently? Individual nodes inside Trash cannot be deleted."
				: "Move the selected node(s) to Trash?";
		final String title = deletingTrash ? "Delete trashed node" : "Delete node";
		final int showResult = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (showResult != JOptionPane.OK_OPTION) {
			return;
		}
		final MMapController mapController = (MMapController) modeController.getMapController();
		if (deletingTrash)
			mapController.deleteNodes(controller.getSelection().getSortedSelection(true));
		else
			for (final NodeModel node : controller.getSelection().getSortedSelection(true))
				mapController.moveNodeToTrash(node);
		controller.getMapViewManager().obtainFocusForSelected();
	}
}
