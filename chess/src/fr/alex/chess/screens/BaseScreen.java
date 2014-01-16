package fr.alex.chess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * the abstract base screen class similar to gdx.Screen but used for UiApp and includes screen transitions
 * 
 * @author trey miller
 * 
 */
public abstract class BaseScreen extends Group{

	protected final UiApp app;

	/** a table that covers the whole screen by default */
	protected final Table mainTable = new Table();

	/** the default padding of the mainTable */
	public static float defaultPad;
	
	/** the duration of the screen transition for the screenOut method */
	public float dur;
	
	protected Interpolation interpolation;

	public BaseScreen(UiApp app) {
		interpolation = Interpolation.linear;
		this.app = app;
		this.dur = app.defaultDur;
		defaultPad = Math.round(Math.max(app.h, app.w) * .02f);
		mainTable.defaults().pad(defaultPad);
		mainTable.size(app.w, app.h);
		this.addActor(mainTable);
	}

	/** override for custom screen transitions, otherwise current screen just slides to the left */
	protected void screenOut() {
		//float xPos = -app.w;
		//MoveToAction action = Actions.moveTo(xPos, 0f, dur, interpolation);
		AlphaAction action = Actions.fadeOut(dur, interpolation);
		addAction(action);
	}
	
	protected void screenInt() {
		//MoveToAction action = Actions.moveTo(xPos, 0f, dur, interpolation);
		AlphaAction action = Actions.fadeIn(dur, interpolation);
		addAction(action);
	}

	/** what happens when the back button is pressed on Android */
	public abstract void onBackPress();

	/** default ad width from admob */
	public static int getAdPxW() {
		return (int) (320f * Gdx.graphics.getDensity());
	}

	/** default ad height from admob */
	public static int getAdPxH() {
		return (int) (50f * Gdx.graphics.getDensity());
	}

	public void draw(){
		
	}
	
	public void hide() {
	}

	public BaseScreen show() {
		return this;		
	}	
	
	public abstract void loadContent();
	
	public abstract void initialize();
	
	public abstract void dispose();
	
	public abstract void pause();
	
	public abstract void resume();
}
