package fr.alex.chess.model.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import fr.alex.chess.ChessGame;

public class EndGameWidget extends Table {
	
	private TextButton exitButton;
	public EndGameWidget(Skin skin, String message){
		super(skin);
		
		Label lbMessage = new Label(message, skin);
		add(lbMessage);
		row();
		exitButton = new TextButton(ChessGame.localize.getUiText("game.exit"), skin);
		add(exitButton).width(150);
		row();
	}
	public TextButton getExitButton() {
		return exitButton;
	}
}
