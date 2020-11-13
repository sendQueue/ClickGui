package de.vinii.management.ui.clickgui.components;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import de.vinii.management.ui.clickgui.ClickGui;
import de.vinii.management.ui.clickgui.Panel;
import de.vinii.management.ui.clickgui.util.RenderUtil;
import de.vinii.management.utils.render.GL11Util;

/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at
 *         11.11.2020. Use is only authorized if given credit!
 * 
 */
public class GuiFrame implements Frame {
	private ArrayList<GuiButton> buttons = new ArrayList<GuiButton>();

	private boolean isExpaned, isDragging;

	private int id, posX, posY, prevPosX, prevPosY, scrollHeight;
	public static int dragID;
	
	private String title;

	/**
	 * 
	 */
	public GuiFrame(String title, int posX, int posY, boolean expanded) {
		this.title = title;
		this.posX = posX;
		this.posY = posY;
		this.isExpaned = expanded;
		this.id = ClickGui.compID += 1;
		this.scrollHeight = 0;
	}

	@Override
	public void render(int mouseX, int mouseY) {
		switch (Panel.theme) {
		case "Caesium":
			renderCaesium(mouseX, mouseY);
			break;

		default:
			break;
		}
	}

	/**
	 * Handles Caesium theme
	 * 
	 * @param mouseX
	 * @param mouseY
	 */
	private void renderCaesium(int mouseX, int mouseY) {
		final int color = Panel.color;
		final int fontColor = Panel.fontColor;
		int width = Math.max(Panel.FRAME_WIDTH, Panel.fR.getStringWidth(title) + 15);
		if (isDragging && Mouse.isButtonDown(0)) {
			posX = mouseX - prevPosX;
			posY = mouseY - prevPosY;
			dragID = id;
		} else {
			isDragging = false;
			dragID = -1;
		}
		for (GuiButton button : buttons) {
			width = Math.max(width, button.getWidth() + 15);
		}
		RenderUtil.drawRect(posX + 1, posY - 5, posX + width, posY + 12, color);
		GL11Util.drawVerticalGradient(posX + 1, posY - 5, width - 1, 17, new Color(color).brighter().getRGB(),
				new Color(color).darker().getRGB());
		Panel.fR.drawStringWithShadow(title, (posX + (width / 2)) - Panel.fR.getStringWidth(title) / 2, posY,
				fontColor);
		Panel.fR.drawStringWithShadow(isExpaned ? "-" : "+",
				posX + width - Panel.fR.getStringWidth(isExpaned ? "-" : "+") - 4, posY, fontColor);
		if (isExpaned) {
			int height = 0;
			final int background = Panel.grey40_240;
			for (GuiButton button : buttons) {
				button.render(posX + 1, posY + height + 12, width, mouseX, mouseY);
				// check if -1
				if (button.getButtonID() == GuiButton.expandedID) {
					ArrayList<GuiComponent> components = button.getComponents();
					if (!components.isEmpty()) {
						int xOffset = 10;
						int yOffset = 0;
						for (GuiComponent component : components) {
							xOffset = Math.max(xOffset, component.getWidth());
							yOffset += component.getHeight();
						}
						final int left = posX + width + 2;
						final int right = left + xOffset;
						final int top = posY + height + 12;
						final int bottom = top + yOffset + 1;
						// 8 is the scroll reduction
						int wheelY = Mouse.getDWheel() * -1 / 8;
						if (bottom + scrollHeight < 30) {
							wheelY *= -1;
							scrollHeight += 10;
						}
						scrollHeight += wheelY;
						RenderUtil.drawRect(left + 1, top + 1 + scrollHeight, right, bottom + scrollHeight,
								Panel.black100);
						int height2 = 0;
						for (GuiComponent component : components) {
							component.render(left, top + height2 + 2 + scrollHeight, xOffset, mouseX, mouseY);
							height2 += component.getHeight();
						}
						RenderUtil.drawVerticalLine(left, top + scrollHeight, bottom + scrollHeight, color);
						RenderUtil.drawVerticalLine(right, top + scrollHeight, bottom + scrollHeight, color);
						RenderUtil.drawHorizontalLine(left, right, top + scrollHeight, color);
						RenderUtil.drawHorizontalLine(left, right, bottom + scrollHeight, color);
					}
				}
				height += button.getHeight();
			}
			RenderUtil.drawHorizontalLine(posX + 1, posX + width - 1, posY + height + 12, color);

			RenderUtil.drawVerticalLine(posX + width, posY - 5, posY + height + 14, Panel.black100);
			RenderUtil.drawVerticalLine(posX + width, posY - 4, posY + height + 14, Panel.black100);
			RenderUtil.drawVerticalLine(posX + width + 1, posY - 4, posY + height + 15, Panel.black100);
			RenderUtil.drawHorizontalLine(posX + 2, posX + width - 1, posY + height + 13, Panel.black100);
			RenderUtil.drawHorizontalLine(posX + 2, posX + width - 1, posY + height + 13, Panel.black100);
			RenderUtil.drawHorizontalLine(posX + 3, posX + width, posY + height + 14, Panel.black100);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		int width = Panel.FRAME_WIDTH;
		if (isExpaned) {

			for (GuiButton button : buttons) {
				// sort for the max needed width
				width = Math.max(width, button.getWidth());
				button.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		if (RenderUtil.isHovered(posX, posY, width, 13, mouseX, mouseY)) {
			if (mouseButton == 0) {
				prevPosX = mouseX - posX;
				prevPosY = mouseY - posY;
				isDragging = true;
				dragID = id;
			} else if (mouseButton == 1) {
				isExpaned = !isExpaned;
				scrollHeight = 0;
				isDragging = false;
				dragID = -1;
			}
		}
	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {
		if (isExpaned) {
			for (GuiButton button : buttons) {
				button.keyTyped(keyCode, typedChar);
			}
		}
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	public void addButton(GuiButton button) {
		if (!buttons.contains(button)) {
			buttons.add(button);
		}
	}

	public int getButtonID() {
		return id;
	}
		/**
	 * @return isExpaned
	 */
	public boolean isExpaned() {
		return isExpaned;
	}

	/**
	 * @return the posX
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * @return the posY
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

}
