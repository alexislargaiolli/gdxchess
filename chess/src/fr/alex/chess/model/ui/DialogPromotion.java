package fr.alex.chess.model.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fr.alex.chess.screens.GameScreen;
import fr.alex.chess.screens.UiApp;
import fr.alex.chess.utils.Game;

public class DialogPromotion extends Group {
	
	private Vector2 outPosition;
	private Vector2 inPosition;

	public DialogPromotion(final GameScreen screen, final boolean white, UiApp app) {
		super();
		outPosition = new Vector2(app.w * 1.1f, app.h*.5f);
		inPosition = new Vector2(app.w * .5f, app.h*.5f);
		Table table = new Table();
		
		setSize(app.w * .5f, app.h * .5f);
		setPosition(outPosition.x, outPosition.y);		
		
		final TextButton btQueen = new TextButton(Game.localize.getUiText("piece.queen"), app.skin);
		btQueen.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(btQueen.isChecked()){
					screen.handlePromotion(white ? 'Q' : 'q');
					btQueen.setChecked(false);
					hide();
				}
			}			
		});
		final TextButton btRook = new TextButton(Game.localize.getUiText("piece.rook"), app.skin);
		btRook.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(btRook.isChecked()){
					screen.handlePromotion(white ? 'R' : 'r');
					btRook.setChecked(false);
					hide();
				}
			}			
		});
		final TextButton btBishop = new TextButton(Game.localize.getUiText("piece.bishop"), app.skin);
		btBishop.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(btBishop.isChecked()){
					screen.handlePromotion(white ? 'B' : 'b');
					btBishop.setChecked(false);
					hide();
				}
			}			
		});
		final TextButton btKnight = new TextButton(Game.localize.getUiText("piece.knight"), app.skin);
		btKnight.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(btKnight.isChecked()){
					screen.handlePromotion(white ? 'K' : 'k');
					btKnight.setChecked(false);
					hide();
				}
			}			
		});
		
		table.add(btQueen).row();
		table.add(btRook).row();
		table.add(btBishop).row();
		table.add(btKnight);
		this.addActor(table);
	}
	
	public void show(){
		MoveToAction action = Actions.moveTo(inPosition.x, inPosition.y, .8f, Interpolation.swing);
		addAction(action);
	}
	
	public void hide(){
		MoveToAction action = Actions.moveTo(outPosition.x, outPosition.y, .8f, Interpolation.swing);
		addAction(action);
	}
}
