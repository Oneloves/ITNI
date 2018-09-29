package main;

import ui.FrameControl;
import ui.FramePicture;

public class Main {

	public static void main(String[] args) {
		FramePicture framePicture = new FramePicture();
		new FrameControl(framePicture);
	}

}
