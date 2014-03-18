package fr.alex.chess;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import fr.alex.chess.screens.ChessGameScreen;

public class ChessCamController extends GestureDetector implements TweenAccessor<ChessCamController> {

	public State getState() {
		return state;
	}

	public static enum State {
		IDLE, ROTATE_LEFT, ROTATE_RIGHT, ZOOM, ROTATE_AROUND, CINEMATIC_BEGIN
	}

	public static final int POS_XYZ = 1;
	public static final int ZOOM = 2;
	public static final int ANGLE_Y = 3;
	public static final int ANGLE_Z = 4;
	public static final int ANGLE_X = 5;
	public static final int LOOKAT = 6;

	protected Vector3 left, right, top, bot, focus, direction;
	protected State state;
	protected final CameraGestureListener gestureListener;
	/** The camera. */
	protected Camera camera;
	protected Vector3 tmpV1;
	protected float rotateAngleY = 0;
	protected float rotateAngleZ = 0;
	protected float rotateAngleX = 0;
	protected float x=0, y=0, z=0;
	protected float lookAtX=0, lookAtY=0, lookAtZ=0;
	protected float zoom = 1;
	protected boolean zoomed;
	protected ChessGame game;
	protected ChessGameScreen gameScreen;
	
	protected ChessCamController(ChessGameScreen gameScreen, final ChessGame game, final CameraGestureListener gestureListener, final Camera camera) {
		super(gestureListener);
		this.gameScreen = gameScreen;
		this.game = game;
		this.gestureListener = gestureListener;
		this.gestureListener.controller = this;
		this.camera = camera;
		this.tmpV1 = new Vector3();
	}

	public ChessCamController(ChessGameScreen gameScreen, final ChessGame game, Camera camera) {
		this(gameScreen, game, new CameraGestureListener(), camera);
		state = State.IDLE;
		left = new Vector3(20, 15, 10);
		focus = new Vector3();
		direction = new Vector3();
	}

	public void update() {
		camera.update();
	}

	private void switchState(State next) {
		switch (state) {
		case IDLE:
			break;
		case ROTATE_AROUND:
			break;
		case ROTATE_LEFT:
			break;
		case ROTATE_RIGHT:
			break;
		case ZOOM:
			break;
		default:
			break;

		}
		state = next;
		switch (next) {
		case IDLE:
			break;
		case ROTATE_LEFT:
			rotateAngleY = 0;
			break;
		case ROTATE_RIGHT:
			rotateAngleY = 0;
			break;
		case CINEMATIC_BEGIN:
			rotateAngleZ = 0;
			rotateAngleY = 0;
			rotateAngleX = 0;
			zoom=1;
			break;
		default:
			break;

		}
	}

	public ChessGameScreen getGameScreen() {
		return gameScreen;
	}

	public void startCinematicBlackBegin() {
		camera.rotate(Vector3.Y, 180);
		camera.position.set(focus.x, 60f, focus.z);
		camera.lookAt(focus.x, 0f, focus.z);
		if (state == State.IDLE) {
			Tween.to(this, ANGLE_X, 1f).target(45f).ease(Cubic.OUT).start(ChessGame.tween);
			Tween.to(this, ZOOM, 2f).target(20f).ease(Cubic.OUT).start(ChessGame.tween).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					switchState(State.IDLE);
				}
			});
			switchState(State.CINEMATIC_BEGIN);
		}
	}
	
	public void startCinematicWhiteBegin(){
		//camera.rotate(Vector3.Y, 180);
		camera.position.set(focus.x, 60f, focus.z);
		camera.lookAt(focus.x, 0f, focus.z);
		if (state == State.IDLE) {
			Tween.to(this, ANGLE_X, 1f).target(-45f).ease(Cubic.OUT).start(ChessGame.tween);
			Tween.to(this, ZOOM, 2f).target(20f).ease(Cubic.OUT).start(ChessGame.tween).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					switchState(State.IDLE);
				}
			});
			switchState(State.CINEMATIC_BEGIN);
		}
	}
	
	public void startEndCinematic(Vector3 target){
		if (state == State.IDLE) {
			
			Tween.to(this, LOOKAT, 2).target(target.x, target.y, target.z).start(ChessGame.tween);
			//Tween.to(this, POS_XYZ, 2).target(target.x, camera.position.y, target.z).start(Game.tween);
			//Tween.to(this, ANGLE_X, 2f).target(45f).delay(2f).ease(Cubic.OUT).start(Game.tween);
			Tween.to(this, ZOOM, 6f).target(30f).ease(Linear.INOUT).start(ChessGame.tween);
			Tween.to(this, ANGLE_Y, 15f).delay(2f).target(360).ease(Linear.INOUT).repeat(800, 0).start(ChessGame.tween);
			
			/*camera.lookAt(target.x, target.y, target.z);
			
			focus = new Vector3(target);
			rotateAround();*/
		}
	}
	
	private void rotateAround() {
		if (state == State.IDLE) {
			Tween.to(this, ZOOM, 3).target(35f).start(ChessGame.tween);
			Tween.to(this, ANGLE_Y, 8).target(360).ease(Linear.INOUT).repeat(800, 0).start(ChessGame.tween).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					switchState(State.IDLE);
				}
			});

			switchState(State.ROTATE_AROUND);
		}
	}

	private void toggleZoom() {
		if (zoomed) {
			zoomOut();
		} else {
			zoomIn();
		}
	}

	private void zoomIn() {
		if (state == State.IDLE) {
			Tween.to(this, ZOOM, 1).target(10f).ease(Back.OUT).start(ChessGame.tween).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					switchState(State.IDLE);
					zoomed = true;
				}
			});
			switchState(State.ZOOM);
		}
	}

	private void zoomOut() {
		if (state == State.IDLE) {
			Tween.to(this, ZOOM, 1).target(20f).ease(Back.OUT).start(ChessGame.tween).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					switchState(State.IDLE);
					zoomed = false;
				}
			});
			switchState(State.ZOOM);
		}
	}

	private void rotateLeft() {
		if (state == State.IDLE) {
			Tween.to(this, ANGLE_Y, 1).target(-90).start(ChessGame.tween).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					switchState(State.IDLE);
				}
			});
			switchState(State.ROTATE_LEFT);
		}
	}

	private void rotateRight() {
		if (state == State.IDLE) {
			Tween.to(this, ANGLE_Y, 1).target(90).start(ChessGame.tween).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					switchState(State.IDLE);
				}
			});
			switchState(State.ROTATE_RIGHT);
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.A) {
			rotateAround();
			return true;
		}
		return super.keyUp(keycode);
	}

	@Override
	public int getValues(ChessCamController target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case ANGLE_Y:
			returnValues[0] = rotateAngleY;
			return 1;
		case ANGLE_Z:
			returnValues[0] = rotateAngleZ;
			return 1;
		case ANGLE_X:
			returnValues[0] = rotateAngleX;
			return 1;
		case ZOOM:
			returnValues[0] = zoom;
			return 1;
		case POS_XYZ:
			returnValues[0] = camera.position.x;
			returnValues[1] = camera.position.y;
			returnValues[2] = camera.position.z;
			return 3;
		case LOOKAT:
			returnValues[0] = focus.x;
			returnValues[1] = focus.y;
			returnValues[2] = focus.z;
			return 3;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(ChessCamController target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case ANGLE_Y:
			float deltaAngleY = newValues[0] - rotateAngleY;
			rotateAngleY = newValues[0];
			camera.rotateAround(focus, Vector3.Y, deltaAngleY);
			break;
		case ANGLE_Z:
			float deltaAngleZ = newValues[0] - rotateAngleZ;
			rotateAngleZ = newValues[0];
			camera.rotateAround(focus, Vector3.Z, deltaAngleZ);
			break;
		case ANGLE_X:
			float deltaAngleX = newValues[0] - rotateAngleX;
			rotateAngleX = newValues[0];
			camera.rotateAround(focus, Vector3.X, deltaAngleX);
			break;
		case ZOOM:
			float amount = newValues[0] - zoom;
			zoom = newValues[0];
			camera.translate(tmpV1.set(camera.direction).scl(amount));
			break;
		case POS_XYZ:
			float deltaX = newValues[0] - camera.position.x;
			//x = newValues[0];
			float deltaY = newValues[1] - camera.position.y;
		//	y = newValues[1];
			float deltaZ = newValues[2] - camera.position.z;
			//z = newValues[2];
			camera.translate(deltaX, deltaY, deltaZ);
			break;
		case LOOKAT:
			focus.x = newValues[0];
			focus.y = newValues[1];
			focus.z = newValues[2];
			camera.lookAt(focus.x, focus.y, focus.z);
			break;
		default:
			assert false;
		}
	}

	protected static class CameraGestureListener extends GestureAdapter {
		public ChessCamController controller;

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {

			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			if(count == 1){
				controller.getGameScreen().handleClick(x, y);
			}
			if (count == 2) { //Double tap
				controller.toggleZoom();
			}
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			if (velocityX > 500) {
				controller.rotateLeft();
			} else if (velocityX < -500) {
				controller.rotateRight();
			}
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {

			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			return false;
		}
	}

	public Vector3 getFocus() {
		return focus;
	}

	public void setFocus(Vector3 focus) {
		this.focus = focus;
	};

}
