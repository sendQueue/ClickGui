package de.vinii.management.ui.clickgui;

import java.awt.Color;
import java.util.HashMap;

import de.vinii.management.module.Category;
import de.vinii.management.module.Module;
import de.vinii.management.module.ModuleManager;
import de.vinii.management.ui.clickgui.components.Frame;
import de.vinii.management.ui.clickgui.components.GuiButton;
import de.vinii.management.ui.clickgui.components.GuiFrame;
import de.vinii.management.ui.clickgui.listeners.ClickListener;
import de.vinii.management.ui.clickgui.listeners.ComponentsListener;
import de.vinii.management.ui.clickgui.util.FramePosition;
import de.vinii.management.utils.render.FontRenderer;


/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at 11.11.2020. 
 *         Use is only authorized if given credit!
 * 
 * 		   Some renderstuff requires https://github.com/sendQueue/LWJGLUtil
 * 
 */
public class Panel extends ClickGui {
	public static HashMap<String, FramePosition> framePositions = new HashMap<String, FramePosition>();

	public static FontRenderer fR = new FontRenderer("Arial", 18);

	public static String theme;
	
	public static int FRAME_WIDTH = 100;
	
	// colors
	public static int color = new Color(193, 105, 170, 220).getRGB();
	public static int fontColor = Color.white.getRGB();
	public static int grey40_240 = new Color(40, 40, 40, 140).getRGB();
	public static int black195 = new Color(0, 0, 0, 195).getRGB();
	public static int black100 = new Color(0, 0, 0, 100).getRGB();
	
	
	/**
	 * Initializes Panel
	 * 
	 * @param theme
	 * @param fontSize
	 */
	public Panel(String theme, int fontSize) {
		this.theme = theme;
		fR = new FontRenderer("BigNoodleTitling", fontSize);
	}

	@Override
	public void initGui() {
		int x = 25;
		for (Category cat : Category.values()) {
			GuiFrame frame;
			// load frame positions
			if (framePositions.containsKey(cat.name())) {
				FramePosition curPos = framePositions.get(cat.name());
				frame = new GuiFrame(cat.name(), curPos.getPosX(), curPos.getPosY(), curPos.isExpanded());
			} else {
				frame = new GuiFrame(cat.name(), x, 50, true);
			}
			for (Module m : ModuleManager.moduleList) {
				if (cat == m.category && (m.shown)) {
					GuiButton button = new GuiButton(m.getName());
					button.addClickListener(new ClickListener(button));
					button.addExtendListener(new ComponentsListener(button));
					frame.addButton(button);
				}
			}
			addFrame(frame);
			x += 140;
		}
		super.initGui();
	}

	public void onGuiClosed() {
		// save positions to framePositions
		if (!getFrames().isEmpty()) {
			for (Frame frame : getFrames()) {
				GuiFrame guiFrame = ((GuiFrame) frame);
				framePositions.put(guiFrame.getTitle(),
						new FramePosition(guiFrame.getPosX(), guiFrame.getPosY(), guiFrame.isExpaned()));
			}
		}
		ModuleManager.getModule(de.vinii.modules.render.ClickGui.class).onDisable();
	}
}
