package de.vinii.management.ui.clickgui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.vinii.management.module.Module;
import de.vinii.management.module.ModuleManager;
import de.vinii.management.ui.clickgui.components.GuiButton;


/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at 11.11.2020. 
 *         Use is only authorized if given credit!
 * 
 */
public class ClickListener implements ActionListener {

	private GuiButton button;

	public ClickListener(GuiButton button) {
		this.button = button;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Module m = ModuleManager.getModule(button.getText());
		m.toggleModule();
	}
}
