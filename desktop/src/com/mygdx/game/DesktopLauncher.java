package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.FlappyDemo;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Barrel Bird");
		config.setWindowIcon(("barrelbird_icon.png"));
		config.setWindowedMode(FlappyDemo.WIDTH, FlappyDemo.HEIGHT);
		config.setTitle(FlappyDemo.TITLE);
		config.setResizable(false);
		new Lwjgl3Application(new FlappyDemo(), config);
	}
}
