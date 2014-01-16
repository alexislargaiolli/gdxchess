package fr.alex.chess.model.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fr.alex.chess.screens.UiApp;
import fr.alex.chess.utils.Game;

public class EndDialog extends Group {
	private Vector2 outPosition;
	private Vector2 inPosition;

	/**
	 * End game dialog
	 * 
	 * @param app
	 * @param gameState
	 *            1 : white wins, -1 black wins, 99 draw
	 */
	public EndDialog(final UiApp app, int gameState) {
		super();
		setName("endDialog");
		outPosition = new Vector2(app.w * 1.1f, app.h * .5f);
		inPosition = new Vector2(app.w * .5f, app.h * .5f);
		Table table = new Table();
		if (gameState == 1 || gameState == -1) {
			table.add(new Label(Game.localize.getUiText("game.end.mat"), app.skin));
			table.row();
		}

		if (gameState == -2) {
			String suffix = Game.player.isWhite() ? "white" : "black";
			table.add(new Label(Game.localize.getUiText("game.end." + suffix), app.skin));
			table.row();
			table.add(new Label(Game.localize.getUiText("game.end.opponentleft"), app.skin));
			table.row();
		} else {
			String suffix = gameState == 1 ? "white" : gameState == -1 ? "black" : "draw";
			table.add(new Label(Game.localize.getUiText("game.end." + suffix), app.skin));
			table.row();
		}

		final TextButton button = new TextButton(Game.localize.getUiText("game.end.menu"), app.skin);
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (button.isChecked()) {
					app.switchScreens(Game.screens.getScreen("net"));
				}
			}
		});
		table.add(button);
		this.addActor(table);
	}

	public void reset(){
		this.setPosition(outPosition.x, outPosition.y);
	}
	
	public void show() {
		MoveToAction action = Actions.moveTo(inPosition.x, inPosition.y, .8f, Interpolation.swing);
		addAction(action);
	}

	public void hide() {
		MoveToAction action = Actions.moveTo(outPosition.x, outPosition.y, .8f, Interpolation.swing);
		addAction(action);
	}

}
