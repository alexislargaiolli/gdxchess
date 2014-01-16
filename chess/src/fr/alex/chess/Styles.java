package fr.alex.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * @author trey miller
 */
public class Styles {

	public void styleSkin(Skin skin, TextureAtlas atlas) {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/showcard32.fnt"), false);
		BitmapFont fonttitle = new BitmapFont(Gdx.files.internal("fonts/showcard48.fnt"), false);
		BitmapFont fonttext = new BitmapFont(Gdx.files.internal("fonts/showcard24.fnt"), false);
		skin.add("default", font);
		skin.add("title", fonttitle);
		skin.add("texte", fonttext);

		skin.add("lt-blue", new Color(.6f, .8f, 1f, 1f));
		skin.add("lt-green", new Color(.6f, .9f, .6f, 1f));
		skin.add("dark-blue", new Color(.1f, .3f, 1f, 1f));
		
		NinePatchDrawable panel = new NinePatchDrawable(new NinePatch(atlas.findRegion("panel"), 10, 10, 10, 10));
		panel.setMinHeight(20);
		panel.setMinWidth(20);
		NinePatchDrawable btn1up = new NinePatchDrawable(new NinePatch(atlas.findRegion("button"), 20, 15, 15, 20));
		btn1up.setMinHeight(30);
		btn1up.setMinWidth(30);
		NinePatchDrawable btn1down = new NinePatchDrawable(new NinePatch(atlas.findRegion("button_in"), 20, 15, 15, 20));
		btn1down.setMinHeight(30);
		btn1down.setMinWidth(30);
		//NinePatchDrawable listItem = new NinePatchDrawable(new NinePatch(atlas.findRegion("listItem"), 15, 10, 10, 15));
		NinePatchDrawable cursor = new NinePatchDrawable(new NinePatch(atlas.findRegion("cursor"), 10, 8, 8, 10));
		cursor.setMinHeight(20);
		cursor.setMinWidth(16);
		NinePatchDrawable listItemSelected = new NinePatchDrawable(new NinePatch(atlas.findRegion("listItem_selected"), 15, 10, 10, 15));
		NinePatchDrawable inputText = new NinePatchDrawable(new NinePatch(atlas.findRegion("inputText"), 15, 15, 15, 15));
		inputText.setMinHeight(30);
		inputText.setMinWidth(30);
		NinePatchDrawable inputTextFocus = new NinePatchDrawable(new NinePatch(atlas.findRegion("inputTextFocused"), 15, 15, 15, 15));
		inputTextFocus.setMinHeight(30);
		inputTextFocus.setMinWidth(30);
		NinePatch dialog = new NinePatch(atlas.findRegion("promote_background"), 30, 30, 30, 30);
		NinePatch window1patch = new NinePatch(atlas.findRegion("windows"), 30, 30, 30, 30);
		
		skin.add("btn1up", btn1up);
		skin.add("btn1down", btn1down);
		skin.add("window1", window1patch);
		skin.add("dialog", dialog);
		//skin.add("white-pixel", atlas.findRegion("white-pixel"), TextureRegion.class);

		//---Default label
		LabelStyle lbs = new LabelStyle();
		lbs.font = font;
		lbs.fontColor = Color.WHITE;
		skin.add("default", lbs);
		//---Label for title
		LabelStyle lbst = new LabelStyle();
		lbst.font = fonttitle;
		lbst.fontColor = Color.WHITE;
		skin.add("title", lbst);
		//---Label for paragraph
		LabelStyle lbstext = new LabelStyle();
		lbstext.font = fonttext;
		lbstext.fontColor = Color.WHITE;
		skin.add("text", lbstext);

		TextButtonStyle tbs = new TextButtonStyle(btn1up, btn1down, btn1down, fonttext);
		//tbs.fontColor = skin.getColor("dark-blue");
		tbs.pressedOffsetX = Math.round(1f * Gdx.graphics.getDensity());
		tbs.pressedOffsetY = tbs.pressedOffsetX * -1f;
		skin.add("default", tbs);
		
		ListStyle ls = new ListStyle(font, Color.BLUE, Color.WHITE, listItemSelected);
		skin.add("default", ls);
		
		TextFieldStyle tfs = new TextFieldStyle();
		tfs.font = fonttext;
		tfs.fontColor = Color.WHITE;
		tfs.focusedFontColor = Color.CYAN;
		tfs.background = inputText;
		tfs.cursor = cursor;
		skin.add("default", tfs);
		
		ScrollPaneStyle sps = new ScrollPaneStyle();
		sps.background = panel;
		skin.add("default", sps);
		
	}
}
