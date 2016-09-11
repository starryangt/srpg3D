package com.game.srpg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.srpg.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		config.vSyncEnabled = false;
		config.width = 1280;
		config.height = 720;

		new LwjglApplication(new MyGdxGame(), config);
	}
}
