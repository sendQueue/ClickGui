package de.vinii.management.ui.clickgui.components;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import de.vinii.management.ui.clickgui.ClickGui;
import de.vinii.management.ui.clickgui.Panel;
import de.vinii.management.ui.clickgui.components.listeners.ValueListener;
import de.vinii.management.ui.clickgui.util.MathUtil;
import de.vinii.management.ui.clickgui.util.RenderUtil;
import de.vinii.management.utils.render.GL11Util;
import net.minecraft.util.MathHelper;

/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at
 *         11.11.2020. Use is only authorized if given credit!
 * 
 */
public class GuiSlider implements GuiComponent {
	private static int dragId = -1;
	private int round, id, width, posX, posY;
	private float min, max, current;
	private double c;

	private boolean wasSliding, hovered;

	private String text;

	private ArrayList<ValueListener> valueListeners = new ArrayList<ValueListener>();

	public GuiSlider(String text, float min, float max, float current, int round) {
		this.text = text;
		this.min = min;
		this.max = max;
		this.current = current;
		c = current / max;
		this.round = round;
		this.id = Panel.compID += 1;
	}

	public void addValueListener(ValueListener vallistener) {
		valueListeners.add(vallistener);
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY, int wheelY) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		hovered = RenderUtil.isHovered(posX, posY, width, getHeight(), mouseX, mouseY);
		if (hovered && wheelY != 0) {
			double diff = min < 0 ? Math.abs(min - max) : max - min;
			double w = wheelY / 15;
			// 100 => per scroll 1/100 of the diff is being added/subtracted
			w *= diff / 100;
			if (round == 0) {
				current = (int) MathHelper.clamp_double(current + w, min, max);
			} else {
				current = (float) MathHelper.clamp_double(current + w, min, max);
			}
		}
		if (Mouse.isButtonDown(0) && (dragId == id || dragId == -1) && hovered) {
			if (mouseX < posX + 2) {
				current = min;
			} else if (mouseX > posX + width) {
				current = max;
			} else {
				double diff = max - min;
				double val = min + (MathHelper.clamp_double((mouseX - posX + 3) / (double) width, 0, 1)) * diff;
				if (round == 0) {
					current = (int) val;
				} else {
					current = (float) val;
				}
			}
			dragId = id;
			for (ValueListener listener : valueListeners) {
				listener.valueUpdated(current);
			}
			wasSliding = true;

		} else if (!Mouse.isButtonDown(0) && wasSliding) {
			for (ValueListener listener : valueListeners) {
				listener.valueChanged(current);
			}
			dragId = -1;
			wasSliding = false;
		}
		double percent = (current - min) / (max - min);
		switch (Panel.theme) {
		case "Caesium":
			renderCaesium(percent);
			break;

		default:
			break;
		}
	}

	private void renderCaesium(double percent) {
		String value;
		if (round == 0) {
			value = "" + Math.round(current);
		} else {
			value = "" + MathUtil.round(current, round);
		}

		final Color color = new Color(Panel.color);

		Panel.fR.drawStringWithShadow(text + ":", posX + 3, posY + 3, Panel.fontColor);
		Panel.fR.drawStringWithShadow(value, posX + width - Panel.fR.getStringWidth(value) - 3, posY + 3,
				Panel.fontColor);

		GL11Util.drawRect(posX, posY + Panel.fR.FONT_HEIGHT + 3, posX + width - 2, posY + Panel.fR.FONT_HEIGHT + 5,
				Color.black.getRGB());
		GL11Util.drawHorizontalGradient(posX, posY + Panel.fR.FONT_HEIGHT + 3, (float) (percent * width - 2), 2,
				color.darker().getRGB(), color.brighter().getRGB());

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {
	}

	@Override
	public int getWidth() {
		return Panel.fR.getStringWidth(text + (round == 0 ? Math.round(current) : MathUtil.round(current, round))) + 68;
	}

	@Override
	public int getHeight() {
		return Panel.fR.FONT_HEIGHT + 6;
	}

	@Override
	public boolean allowScroll() {
		return !hovered;
	}

}
