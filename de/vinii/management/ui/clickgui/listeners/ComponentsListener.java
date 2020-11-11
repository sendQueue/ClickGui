package de.vinii.management.ui.clickgui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.vinii.management.module.Module;
import de.vinii.management.module.ModuleManager;
import de.vinii.management.settings.Setting;
import de.vinii.management.settings.SettingManager;
import de.vinii.management.ui.clickgui.components.GuiButton;
import de.vinii.management.ui.clickgui.components.GuiComboBox;
import de.vinii.management.ui.clickgui.components.GuiGetKey;
import de.vinii.management.ui.clickgui.components.GuiLabel;
import de.vinii.management.ui.clickgui.components.GuiSlider;
import de.vinii.management.ui.clickgui.components.GuiToggleButton;
import de.vinii.management.ui.clickgui.components.listeners.ComboListener;
import de.vinii.management.ui.clickgui.components.listeners.ComponentListener;
import de.vinii.management.ui.clickgui.components.listeners.KeyListener;
import de.vinii.management.ui.clickgui.components.listeners.ValueListener;

/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at
 *         11.11.2020. Use is only authorized if given credit!
 * 
 */
public class ComponentsListener extends ComponentListener {
	private GuiButton button;

	public ComponentsListener(GuiButton button) {
		this.button = button;
	}

	@Override
	public void addComponents() {
		add(new GuiLabel("Settings >"));
		final Module m = ModuleManager.getModule(button.getText());

		for (Setting set : SettingManager.getSettingArrayList()) {
			if (set.getModule() == m) {
				switch (set.getSettingType()) {
				case BOOLEAN:
					GuiToggleButton toggleButton = new GuiToggleButton(set.getName());
					toggleButton.setToggled(set.isState());
					toggleButton.addClickListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							set.setState(toggleButton.isToggled());
							new Thread(() -> {
								ModuleManager.saveDetails();
							}).start();
						}
					});
					add(toggleButton);
					break;
				case VALUE:
					GuiSlider slider = new GuiSlider(set.getName(), set.getMin(), set.getMax(), set.getCurrent(),
							set.isOnlyInt() ? 0 : 2);
					slider.addValueListener(new ValueListener() {

						@Override
						public void valueUpdated(float value) {
							set.setCurrent(value);
						}

						@Override
						public void valueChanged(float value) {
							set.setCurrent(value);
							new Thread(() -> {
								ModuleManager.saveDetails();
							}).start();
						}
					});
					add(slider);
					break;
				case COMBO:
					GuiComboBox comboBox = new GuiComboBox(set);
					comboBox.addComboListener(new ComboListener() {

						@Override
						public void comboChanged(String combo) {
							new Thread(() -> {
								ModuleManager.saveDetails();
							}).start();
						}
					});
					add(comboBox);
					break;
				default:
					break;
				}

			}
		}
		GuiGetKey ggk = new GuiGetKey("KeyBind", m.getKeyBind());
		ggk.addKeyListener(new KeyListener() {

			@Override
			public void keyChanged(int key) {
				m.setKeyBind(key);
				new Thread(() -> {
					ModuleManager.saveDetails();
				}).start();

			}
		});
		add(ggk);
	}

}
