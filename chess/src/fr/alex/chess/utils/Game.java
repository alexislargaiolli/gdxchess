package fr.alex.chess.utils;

import aurelienribon.tweenengine.TweenManager;
import fr.alex.chess.net.ComClient;
import fr.alex.chess.player.Player;
import fr.alex.chess.screens.GameScreen;
import fr.alex.chess.screens.ScreenManager;

/**
 * Allow access to global classes
 * @author Jahal
 *
 */
public class Game {
	public static GameScreen chess;
	
	public static LocalizationManager localize;
	
	public static TweenManager tween;
	
	public static ScreenManager screens;
	
	public static Player player;
	
	public static ComClient com;
}
