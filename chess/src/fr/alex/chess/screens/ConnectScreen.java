package fr.alex.chess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fr.alex.chess.net.Net;
import fr.alex.chess.utils.Game;

public class ConnectScreen extends BaseScreen {

	protected TextField inputName;
	protected Table tableName;
	protected TextButton btConnect;
	protected Label gameListInfoLabel;
	protected boolean switched;

	public ConnectScreen(UiApp app) {
		super(app);
		mainTable.setBackground(app.skin.getDrawable("window1"));
		//------------------- Input name table
		tableName = new Table();
		inputName = new TextField(Game.player.getName(), app.skin);
		inputName.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char key) {
				if (key == '\r') {
					Game.player.setName(textField.getText());
					getStage().unfocus(textField);
				}
			}
		});
		tableName.add(new Label(Game.localize.getUiText("net.pseudo") + ": ", app.skin));
		tableName.add(inputName);
		mainTable.add(tableName);
		mainTable.row();

		btConnect = new TextButton(Game.localize.getUiText("net.button.connect"), app.skin);
		btConnect.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (btConnect.isChecked()) {
					connect();
					btConnect.setChecked(false);
				}
			}
		});
		mainTable.add(btConnect);
		mainTable.row();
		gameListInfoLabel = new Label(Game.localize.getUiText("net.gamelist.empty"), app.skin, "text");
		mainTable.add(gameListInfoLabel);

		//------------------- anim settings
		dur = .5f;
		interpolation = Interpolation.pow2;
	}

	/**
	 * Try to connect to game hall Update info message as connecting
	 */
	private void connect() {
		showMessage(Game.localize.getUiText("net.connection"));
		Net.connect();
	}

	/**
	 * Show the message in info label
	 * 
	 * @param message
	 *            message to show
	 */
	private void showMessage(String message) {
		gameListInfoLabel.setText(message);
		gameListInfoLabel.setVisible(true);
	}

	@Override
	public void act(float delta) {
		if (Net.isConnected() && !switched) {
			app.switchScreens(Game.screens.getScreen("net"));
			switched = true;
		}
		super.act(delta);
	}

	/**
	 * Hide the info label
	 */
	private void hideMessage() {
		gameListInfoLabel.setVisible(false);
	}

	public BaseScreen show() {
		hideMessage();
		switched = false;
		return super.show();
	}

	@Override
	public void onBackPress() {
		Gdx.app.exit();
	}

	@Override
	public void loadContent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
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
