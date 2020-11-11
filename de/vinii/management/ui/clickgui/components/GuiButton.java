package de.vinii.management.ui.clickgui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import de.vinii.management.module.ModuleManager;
import de.vinii.management.ui.clickgui.ClickGui;
import de.vinii.management.ui.clickgui.Panel;
import de.vinii.management.ui.clickgui.components.listeners.ComponentListener;
import de.vinii.management.ui.clickgui.util.RenderUtil;


/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at 11.11.2020. 
 *         Use is only authorized if given credit!
 * 
 */
public class GuiButton implements GuiComponent {
	public static int expandedID = -1;
	private int id;

	private String text;

	private ArrayList<ActionListener> clickListeners = new ArrayList<ActionListener>();
	private ArrayList<GuiComponent> guiComponents = new ArrayList<GuiComponent>();

	private int width, textWidth, posX, posY;

	/**
	 * 
	 */
	public GuiButton(String text) {
		this.text = text;
		textWidth = Panel.fR.getStringWidth(text);
		id = ClickGui.compID += 1;
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;

		final int height = getHeight();
		switch (Panel.theme) {
		case "Caesium":
			renderCaesium(posX, posY, width, height);
			break;
		default:
			break;
		}

	}

	/**
	 * Renders button for theme Caesium
	 */
	private void renderCaesium(int posX, int posY, int width, int height) {
		
		RenderUtil.drawRect(posX, posY, posX + width - 1, posY + height, Panel.black100);
		
		int color = Panel.fontColor;

		if (ModuleManager.getModule(getText()).getState())
			color = Panel.color;

		Panel.fR.drawStringWithShadow(getText(), posX + (width / 2) - Panel.fR.getStringWidth(getText()) / 2, posY + 2,
				color);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (GuiFrame.dragID == -1 && RenderUtil.isHovered(posX, posY, width, getHeight(), mouseX, mouseY)) {
			if (mouseButton == 1) {
				if (expandedID != id) {
					expandedID = id;
				} else {
					expandedID = -1;
				}
			} else if (mouseButton == 0) {
				for (ActionListener listener : clickListeners) {
					listener.actionPerformed(new ActionEvent(this, id, "click", System.currentTimeMillis(), 0));
				}
			}
		}

		if (expandedID == id) {
			for (GuiComponent component : guiComponents) {
				component.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}

	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {
		if (expandedID == id) {
			for (GuiComponent component : guiComponents) {
				component.keyTyped(keyCode, typedChar);
			}
		}

	}

	@Override
	public int getWidth() {
		return 5 + textWidth;
	}

	@Override
	public int getHeight() {
		return Panel.fR.FONT_HEIGHT + 3;
	}

	public String getText() {
		return text;
	}

	public int getButtonID() {
		return id;
	}

	public ArrayList<GuiComponent> getComponents() {
		return guiComponents;
	}

	public void addClickListener(ActionListener actionlistener) {
		clickListeners.add(actionlistener);
	}

	public void addExtendListener(ComponentListener listener) {
		listener.addComponents();
		guiComponents.addAll(listener.getComponents());
	}

}
