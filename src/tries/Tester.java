package tries;

import logic.Mesh;
import logic.MeshBox;
import logic.Vector;
import renderer.Camera;

public class Tester {

	public static void main(String[] args) {
		Camera camera=new Camera(0,0,0,new Vector(10, 10, 10), new Vector(0, 10, 10));
		System.out.println(camera.getUp());

	}

}
