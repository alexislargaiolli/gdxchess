package fr.alex.chess.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Simplified resource management
 * @author jpons
 */
public class Assets {
	
	/**
	 * Underlying asset manager
	 */
	private static AssetManager manager = new AssetManager();
	
	/**
	 * @param fileName the path to the texture file
	 * @return the specified texture, or <code>null</code> if no texture match the specified name
	 */
	public static Texture getTexture(String fileName) {
		Texture t = manager.get(fileName, Texture.class);
		return t;
	}
	
	public static Texture getLinearTexture(String fileName) {
		Texture t = manager.get(fileName, Texture.class);
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return t;
	}
	
	/**
	 * @param fileName the path to the music file
	 * @return the specified music, or <code>null</code> if no music match the specified name
	 */
	public static Music getMusic(String fileName) {
		return manager.get(fileName, Music.class);
	}
	
	/**
	 * @param fileName the path to the sound file
	 * @return the specified sound, or <code>null</code> if no sound match the specified name
	 */
	public static Sound getSound(String fileName) {
		return manager.get(fileName, Sound.class);
	}
	
	/**
	 * @param fileName the path to the font file
	 * @return the specified font, or <code>null</code> if no font match the specified name
	 */
	public static BitmapFont getBitmapFont(String fileName) {
		return manager.get(fileName, BitmapFont.class);
	}
	
	/**
	 * @param fileName the path to the Skin file
	 * @return the specified Skin, or <code>null</code> if no font match the specified name
	 */
	public static Skin getSkin(String fileName) {
		return manager.get(fileName, Skin.class);
	}
	
	/**
	 * @param fileName the path to the TextureAtlas file
	 * @return the specified TextureAtlas, or <code>null</code> if no font match the specified name
	 */
	public static TextureAtlas getTextureAtlas(String fileName) {
		return manager.get(fileName, TextureAtlas.class);
	}
	
	/**
	 * Prepare to load a texture based on the specified file name
	 * @param fileName the path to the texture file
	 */
	public static void loadTexture(String fileName) {		
		manager.load(fileName, Texture.class);
	}
	
	/**
	 * Prepare to load a music based on the specified file name
	 * @param fileName the path to the music file
	 */
	public static void loadMusic(String fileName) {
		manager.load(fileName, Music.class);
	}
	
	/**
	 * Prepare to load a sound based on the specified file name
	 * @param fileName the path to the sound file
	 */
	public static void loadSound(String fileName) {
		manager.load(fileName, Sound.class);
	}
	
	/**
	 * Prepare to load a font based on the specified file name
	 * @param fileName the path to the font file
	 */
	public static void loadBitmapFont(String fileName) {
		manager.load(fileName, BitmapFont.class);
	}
	
	/**
	 * Prepare to load a font based on the specified file name
	 * @param fileName the path to the skin JSON file
	 */
	public static void loadSkin(String fileName) {
		manager.load(fileName, Skin.class);
	}
	
	/**
	 * Prepare to load a font based on the specified file name
	 * @param fileName the path to the atlas JSON file
	 */
	public static void loadTextureAtlas(String fileName) {
		manager.load(fileName, TextureAtlas.class);
	}
	
	/**
	 * Prepare to unload an asset based on its file name
	 * @param fileName the path to the asset file
	 */
	public static void unloadAsset(String fileName) {
		if (manager.isLoaded(fileName)) {
			manager.unload(fileName);
		}
	}
	
	/**
	 * @param fileName the path to the texture file
	 * @return <code>true</code> if the texture is loaded, <code>false</code> otherwise
	 */
	public static boolean isTextureLoaded(String fileName) {
		return manager.isLoaded(fileName, Texture.class);
	}
	
	/**
	 * @param fileName the path to the music file
	 * @return <code>true</code> if the music is loaded, <code>false</code> otherwise
	 */
	public static boolean isMusicLoaded(String fileName) {
		return manager.isLoaded(fileName, Music.class);
	}
	
	/**
	 * @param fileName the path to the sound file
	 * @return <code>true</code> if the sound is loaded, <code>false</code> otherwise
	 */
	public static boolean isSoundLoaded(String fileName) {
		return manager.isLoaded(fileName, Sound.class);
	}
	
	/**
	 * @param fileName the path to the font file
	 * @return <code>true</code> if the font is loaded, <code>false</code> otherwise
	 */
	public static boolean isBitmapFontLoaded(String fileName) {
		return manager.isLoaded(fileName, BitmapFont.class);
	}
	
	/**
	 * @param fileName the path to the skin file
	 * @return <code>true</code> if the skin is loaded, <code>false</code> otherwise
	 */
	public static boolean isSkinLoaded(String fileName) {
		return manager.isLoaded(fileName, Skin.class);
	}
	
	/**
	 * @param fileName the path to the texture atlas file
	 * @return <code>true</code> if the font is loaded, <code>false</code> otherwise
	 */
	public static boolean isTextureAtlasLoaded(String fileName) {
		return manager.isLoaded(fileName, TextureAtlas.class);
	}
	
	/**
	 * Process loading tasks (if any)
	 * @return <code>true</code> if all assets are loaded, <code>false</code> otherwise
	 */
	public static boolean update() {
		return manager.update();
	}
	
	/**
	 * @return the loading completion ratio (between 0.0 and 1.0)
	 */
	public static float getCompletionRate() {
		return manager.getProgress();
	}

	
	public static AssetManager getManager(){
		return manager;
	}
	
}
