package fr.alex.chess.screens;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import fr.alex.chess.utils.Assets;
import fr.alex.chess.utils.Game;

/**
 * Allow to load all screens resources while drawing a loading %
 * Initialize all screens when loading ends
 * @author Jahal
 *
 */
public class LoadingScreen extends BaseScreen {

	public String nextScreen;
	public String loadingMessage;
	public boolean trasitioned = false;
	private Label loadingLabel;
	
	public LoadingScreen(UiApp app) {
		super(app);
		mainTable.setBackground(app.skin.getDrawable("window1"));
		//Localized loading message
		loadingMessage = Game.localize.getUiText("loading") + " ";		
		loadingLabel = new Label(loadingMessage, app.skin, "title");
		mainTable.add(loadingLabel);
		
		dur = .5f;
		interpolation = Interpolation.pow2;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		int rate = (int) ((Assets.getCompletionRate() * 10000) / 100);
		loadingLabel.setText(loadingMessage + rate + "%");
		if(Assets.update() && !trasitioned){
			Game.screens.initialize();
			System.out.println("loading screen: act()");
			app.switchScreens(Game.screens.getScreen(nextScreen));
			trasitioned = true;
		} 
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize() {
		
	}

	public String getNextScreen() {
		return nextScreen;
	}

	public void setNextScreen(String nextScreen) {
		this.nextScreen = nextScreen;
	}

	@Override
	public void onBackPress() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}	

}
