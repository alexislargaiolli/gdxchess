package fr.alex.chess.screens;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import fr.alex.chess.net.GameListItem;
import fr.alex.chess.net.Net;
import fr.alex.chess.player.GameInfo;
import fr.alex.chess.utils.Game;

public class NetScreen extends BaseScreen {

	protected Stage stage;
	protected List gameList;
	protected Label gameListInfoLabel;
	protected boolean oldGameList;
	protected boolean gameIdAsked;
	protected TextButton btJoin;
	protected GameListItem curItem;
	protected Table gameListTable;
	protected TextButton btCreate;

	public NetScreen(final UiApp app) {
		super(app);

		//------------------- screen properties
		mainTable.setBackground(app.skin.getDrawable("window1"));
		mainTable.debug();

		//------------------- game list table container
		gameListTable = new Table();

		//------------------- game list header
		Table headerList = new Table();
		headerList.add(new Label(Game.localize.getUiText("net.gamelist"), app.skin, "title"));
		final TextButton btRefresh = new TextButton(Game.localize.getUiText("net.button.refresh"), app.skin);
		btRefresh.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (btRefresh.isChecked()) {
					try {
						refresh();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					btRefresh.setChecked(false);
				}
			}
		});
		headerList.add(btRefresh);
		gameListTable.add(headerList);
		gameListTable.row();
		//------------------- game list info
		gameListInfoLabel = new Label(Game.localize.getUiText("net.gamelist.empty"), app.skin, "text");
		gameListTable.add(gameListInfoLabel);

		//---- Game list content

		gameListTable.row().minWidth(Gdx.graphics.getWidth() * .9f).minHeight(Gdx.graphics.getHeight() * .4f).maxWidth(Gdx.graphics.getWidth() * .9f).maxHeight(Gdx.graphics.getHeight() * .6f);

		//------------------- game list content
		gameList = new List(new Object[0], app.skin);
		gameList.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (gameList.getSelectedIndex() != -1) {
					curItem = GameListItem.fromString(gameList.getSelection());
				} else {
					curItem = null;
				}
			}

		});
		ScrollPane pan = new ScrollPane(gameList, app.skin);
		pan.setSize(Gdx.graphics.getWidth() * .9f, Gdx.graphics.getHeight() * .6f);
		gameListTable.add(pan);
		mainTable.add(gameListTable);
		mainTable.row();;
		//------------------- game list footer
		Table tableFooter = new Table();

		btJoin = new TextButton(Game.localize.getUiText("net.button.join"), app.skin);
		btJoin.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (btJoin.isChecked()) {
					GameInfo.gameid = curItem.getGameid();
					app.switchScreens(Game.screens.getScreen("game"));
					btCreate.setChecked(false);
				}
			}
		});
		btCreate = new TextButton(Game.localize.getUiText("net.button.create"), app.skin);
		btCreate.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (btCreate.isChecked()) {
					try {
						Net.askGameId();
						gameIdAsked = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					btCreate.setChecked(false);
				}
			}
		});
		tableFooter.add(btCreate);
		tableFooter.add(btJoin);
		mainTable.add(tableFooter);
		mainTable.row();

		

		//------------------- anim settings
		dur = .5f;
		interpolation = Interpolation.pow2;
	}

	/**
	 * Refresh the game list Update info message as refreshing
	 * 
	 * @throws IOException
	 */
	private void refresh() throws IOException {
		showMessage(Game.localize.getUiText("net.refresh"));
		Net.askGameList();
		oldGameList = true;
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

	/**
	 * Hide the info label
	 */
	private void hideMessage() {
		gameListInfoLabel.setVisible(false);
	}

	public BaseScreen show() {
		hideMessage();
		try {
			refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.show();
	}

	@Override
	public void act(float delta) {
		btJoin.setVisible(curItem != null);
		if (oldGameList && Net.isGameListReceived()) {
			updateGameList();
		}
		if (gameIdAsked && Net.isGameIdReceived()) {
			app.switchScreens(Game.screens.getScreen("game"));
			gameIdAsked = false;
		}
		super.act(delta);
	}

	private void updateGameList() {
		if (Net.getGameids() != null) {
			GameListItem[] items = new GameListItem[Net.getGameids().length];
			for (int i = 0; i < items.length; i++) {
				items[i] = new GameListItem(Net.getGameids()[i]);
			}
			gameList.setItems(items);
			gameList.fire(new ChangeEvent());
			hideMessage();
		} else {
			showMessage(Game.localize.getUiText("net.gamelist.empty"));
			gameList.setItems(new Object[0]);
			curItem = null;
		}
		oldGameList = false;
	}

	@Override
	public void loadContent() {

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPress() {
		Net.disconnect();
		app.switchScreens(Game.screens.getScreen("connect"));
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
