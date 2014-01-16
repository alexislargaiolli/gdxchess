package fr.alex.chess.screens;

import java.util.HashMap;

/**
 * Allow easy screen management
 * @author Jahal
 *
 */
public class ScreenManager {
	private HashMap<String,BaseScreen> screens;	

	public ScreenManager() {
		super();
		screens = new HashMap<String, BaseScreen>();
	}
	
	/**
	 * Add a screen to the manager
	 * @param name
	 * @param screen
	 */
	public void addScreen(String name, BaseScreen screen){
		screens.put(name, screen);
	}
	
	public BaseScreen getScreen(String name){
		return screens.get(name);
	}
	
	/**
	 * Call loadContent of all screens
	 */
	public void loadContent(){
		for(BaseScreen s : screens.values()){
			s.loadContent();
		}
	}
	
	/**
	 * Call initialize method of all screens
	 */
	public void initialize(){
		for(BaseScreen s : screens.values()){
			s.initialize();
		}
	}
	
	/**
	 * Dispose all screens resources
	 */
	public void dispose(){
		for(BaseScreen s : screens.values()){
			s.dispose();
		}
	}
	
}
