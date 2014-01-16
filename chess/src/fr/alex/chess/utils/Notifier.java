package fr.alex.chess.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fr.alex.chess.screens.UiApp;

public class Notifier extends Group {
	private Notif chess;
	private Notif playerTurn;
	private Notif opponentTurn;
	private Vector2 HIDDEN_POSITION;
	private Vector2 SHOWN_POSITION;
	
	public enum NotifType{
		CHESS, PLAYER_TURN, OPPONENT_TURN
	}

	public Notifier(UiApp app) {
		HIDDEN_POSITION = new Vector2(Gdx.graphics.getWidth() *.1f, Gdx.graphics.getHeight() * 1.2f);
		SHOWN_POSITION = new Vector2(Gdx.graphics.getWidth() *.1f, Gdx.graphics.getHeight() * .90f);
		
		chess = new Notif(Game.localize.getUiText("game.notif.chess"), app.skin, new Vector2(SHOWN_POSITION.x, SHOWN_POSITION.y * 0.95f), new Vector2(HIDDEN_POSITION));				
		playerTurn = new Notif(Game.localize.getUiText("game.notif.playerturn"), app.skin, new Vector2(SHOWN_POSITION), new Vector2(HIDDEN_POSITION));
		opponentTurn = new Notif(Game.localize.getUiText("game.notif.opponentturn"), app.skin, new Vector2(SHOWN_POSITION), new Vector2(HIDDEN_POSITION));
		Tween.registerAccessor(Notif.class, chess);
		
		addActor(chess);
		addActor(playerTurn);
		addActor(opponentTurn);
	}

	public void initialize() {
		
	}

	public void show(NotifType type, boolean show){
		if(show){
			show(type);
		}else{
			hide(type);
		}
	}
	
	public void show(NotifType type){
		switch(type){
		case CHESS:
			chess.show();
			break;
		case OPPONENT_TURN:
			opponentTurn.show();
			break;
		case PLAYER_TURN:
			playerTurn.show();
			break;
		default:
			break;			
		}
	}
	
	public void hide(NotifType type){
		switch(type){
		case CHESS:
			chess.hide();
			break;
		case OPPONENT_TURN:
			opponentTurn.hide();
			break;
		case PLAYER_TURN:
			playerTurn.hide();
			break;
		default:
			break;			
		}
	}

	class Notif extends Label implements TweenAccessor<Actor> {
		public static final int POSITION_X = 1;
		public static final int POSITION_Y = 2;
		public static final int POSITION_XY = 3;

		private Vector2 shownPosition;
		private Vector2 hiddenPosition;
		private int mode;

		public Notif(String content, Skin skin, Vector2 shownPos, Vector2 hiddenPos) {
			super(content, skin);
			this.shownPosition = shownPos;
			this.hiddenPosition = hiddenPos;
			mode = POSITION_XY;
			setPosition(hiddenPos.x, hiddenPos.y);
		}

		public void show(){
			switch(mode){
			case 1:
				Tween.to(this, mode, 1f).target(shownPosition.x).start(Game.tween);
				break;
			case 2:
				Tween.to(this, mode, 1f).target(shownPosition.y).start(Game.tween);
				break;
			case 3:
				Tween.to(this, mode, 1f).target(shownPosition.x, shownPosition.y).start(Game.tween);
				break;
			}			
		}
		
		public void hide(){
			switch(mode){
			case 1:
				Tween.to(this, mode, 1f).target(hiddenPosition.x).start(Game.tween);
				break;
			case 2:
				Tween.to(this, mode, 1f).target(hiddenPosition.y).start(Game.tween);
				break;
			case 3:
				Tween.to(this, mode, 1f).target(hiddenPosition.x, hiddenPosition.y).start(Game.tween);
				break;
			}
		}
		
		/*@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			float x = getX() + getWidth() * .5f - contentBounds.width * .5f;
			float y = getY() + getHeight() * .5f - contentBounds.height * .5f;
			font.draw(batch, content, x, y);
			super.draw(batch, parentAlpha);
		}*/

		@Override
		public int getValues(Actor target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case POSITION_X:
				returnValues[0] = target.getX();
				return 1;
			case POSITION_Y:
				returnValues[0] = target.getY();
				return 1;
			case POSITION_XY:
				returnValues[0] = target.getX();
				returnValues[1] = target.getY();
				return 2;
			default:
				assert false;
				return -1;
			}
		}

		@Override
		public void setValues(Actor target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case POSITION_X:
				target.setX(newValues[0]);
				break;
			case POSITION_Y:
				target.setY(newValues[0]);
				break;
			case POSITION_XY:
				target.setX(newValues[0]);
				target.setY(newValues[1]);
				break;
			default:
				assert false;
				break;
			}
		}

		public int getMode() {
			return mode;
		}

		public void setMode(int mode) {
			this.mode = mode;
		}		

	}
}
