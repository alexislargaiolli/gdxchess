package fr.alex.chess.model;

import com.badlogic.gdx.math.collision.Ray;

public abstract class MChessEntity {
	public abstract boolean isClick(Ray ray);
	
	public abstract void hightlight(boolean hightlight);
	
	public abstract boolean isWhite();
}
