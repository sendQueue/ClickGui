package de.vinii.management.ui.clickgui;

import java.io.IOException;
import java.util.ArrayList;

import de.vinii.management.ui.clickgui.components.Frame;
import de.vinii.management.utils.render.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at 11.11.2020. 
 *         Use is only authorized if given credit!
 * 
 */
public class ClickGui extends GuiScreen {

	public static int compID = 0;

	private ArrayList<Frame> frames = new ArrayList<Frame>();
	// dont change
	private final FontRenderer fr = new FontRenderer("Arial", 12);

	/**
	 * 
	 */
	public ClickGui() {
		compID = 0;

	}

	protected void addFrame(Frame frame) {
		if (!frames.contains(frame)) {
			frames.add(frame);
		}
	}

	protected ArrayList<Frame> getFrames() {
		return frames;
	}

	@Override
	public void initGui() {
		for (Frame frame : frames) {
			frame.initialize();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (Frame frame : frames) {
			frame.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);

		for (Frame frame : frames) {
			frame.keyTyped(keyCode, typedChar);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// removing will give you cancer
		ScaledResolution sR = new ScaledResolution(mc);
		fr.drawString("ClickGui by Vinii | sendQueue", 2, sR.getScaledHeight() - fr.FONT_HEIGHT, Panel.fontColor);
		for (Frame frame : frames) {
			frame.render(mouseX, mouseY);
		}
	}
}

