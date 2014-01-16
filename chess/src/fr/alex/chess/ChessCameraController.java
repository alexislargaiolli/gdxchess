package fr.alex.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

public class ChessCameraController extends CameraInputController {

	private final Vector3 tmpV1 = new Vector3();
	
	public ChessCameraController(Camera camera) {
		super(camera);
		
	}
	
	public void update() {
		if (rotateRightPressed || rotateLeftPressed || forwardPressed || backwardPressed) {
			final float delta = Gdx.graphics.getDeltaTime();
			if (rotateRightPressed)
				camera.rotate(camera.up, -delta * rotateAngle);
			if (rotateLeftPressed)
				camera.rotate(camera.up, delta * rotateAngle);
			if (forwardPressed) {
				camera.translate(tmpV1.set(camera.direction).scl(delta * translateUnits));
				if (forwardTarget)
					target.add(tmpV1);
			}
			if (backwardPressed) {
				camera.translate(tmpV1.set(camera.direction).scl(-delta * translateUnits));
				if (forwardTarget)
					target.add(tmpV1);
			}
			if (autoUpdate)
				camera.update();
		}
	}

	@Override
	protected boolean process(float deltaX, float deltaY, int button) {
		if (button == rotateButton) {
			tmpV1.set(camera.direction).crs(camera.up).y = 0f;
			if(camera.position.y -  deltaY * rotateAngle > 7)
				camera.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
			camera.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
		}
		if (autoUpdate)
			camera.update();
		return true;
	}	
	
}
