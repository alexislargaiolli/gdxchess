package fr.alex.chess.utils;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


/**
 * Allow multiple language management for UI components and dialogues
 * @author alex
 */
public class LocalizationManager {
	
	/**
	 * 
	 */
	public static enum Language {
		fr("fr"),
		en("en");
		
		/**
		 * Alias of the language
		 */
		private final String alias;
		
		/**
		 * Private constructor
		 */
		private Language(String alias) {
			this.alias = alias;
		}
		
		/**
		 * @param alias the alias of the desired language
		 * @return the language matching the specified alias
		 */
		public static Language Get(String alias) {
			if (fr.alias.equals(alias)) return fr;
			else if (en.alias.equals(alias)) return en;
			else return null;
		}
	}
	
	/**
	 * Fichier contenant les textes des éléments de l'interface utilisateur
	 */
	private PropertiesParser _uiFileParser;
	
	private Language currentLanguage;
	
	/**
	 * 
	 */
	public LocalizationManager() {
		
	}
	
	/**
	 * Initialization of the current language of the game
	 * @param lg 
	 */
	public void setLanguage(Language lg) {
		String fileName = "";
		currentLanguage = lg;
		if(lg == Language.Get("fr"))
		{
			fileName = "languages/ui_fr.properties";
		}
		else if(lg == Language.Get("en"))
		{
			fileName = "languages/ui_en.properties";
		}
		_uiFileParser = new PropertiesParser();					
		try {
			FileHandle handle = Gdx.files.internal(fileName);
			_uiFileParser.parse(handle.readString());
		} catch (IOException e) {
			e.printStackTrace();
			Gdx.app.log("WALApplication constructor", "Erreur lors de la lecture du fichier "+ fileName + " : " + e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return current language as a string : fr, en
	 */
	public String getCurrentLanguage(){
		return currentLanguage.alias;
	}
	
	/**
	 * @param pUiEltName name of the UI component
	 * @return text of the UI component in the right language
	 */
	public String getUiText(String pUiEltName) {
		return _uiFileParser.getProperty(pUiEltName);
	}
	
}
