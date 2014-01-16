package fr.alex.chess.model;

public class BoardSettings {
	/**
	 * Size of one chess board case
	 */
	public static final float caseSize = 5f;
	/**
	 * Number of case on each chess board line
	 */
	public static final int colCaseCount = 8;
	/**
	 * Number of case on each chess board row
	 */
	public static final int rowCaseCount = 8;
	/**
	 * Width of the board
	 */
	public static final float width = rowCaseCount * caseSize;
	
	/**
	 * Height of the board
	 */
	public static final float height = colCaseCount * caseSize;
}
