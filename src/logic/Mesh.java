package logic;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.sun.prism.impl.BufferUtil;

public class Mesh {
	public static final int VERTEX_SIZE=3;
	public static final int FACE_SIZE=7;
	public static final int NORMAL_SIZE=3;
	/*
	public static final int VERTEX_SIZE=3;
	public static final int FACE_SIZE=4;
	public static final int SURFACE_SIZE=3;
	public static final int NORMAL_SIZE=3;
	 */
	private float[] vertices;
	private Surface[] surfaces;
	private float[] normals;
	private int[] faces;
	private MeshBox box;
	
	public Mesh(float[] vertices, Surface[] surfaces, int[] faces)
	{
		if(vertices.length%VERTEX_SIZE==0 && surfaces!=null && faces.length%FACE_SIZE==0)
		{
			this.vertices=vertices;
			this.faces=faces;
			this.surfaces=surfaces;
			this.box=new MeshBox(vertices);
		}
		else
		{
			System.out.println("Wrong Mesh argumnets");
		}
	}
	
	public Mesh(float[] vertices, float[] normals, Surface[] surfaces, int[] faces)
	{
		if(	vertices.length%VERTEX_SIZE==0 && 
			surfaces!=null && 
			faces.length%FACE_SIZE==0 && 
			normals.length%NORMAL_SIZE==0 )
		{
			this.vertices=vertices;
			this.faces=faces;
			this.surfaces=surfaces;
			this.normals=normals;
			this.box=new MeshBox(vertices);
		}
		else
		{
			System.out.println("Wrong Mesh argumnets");
		}
	}
	
	public float[] getVertices() {
		return vertices;
	}
	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}
	public Surface[] getSurfaces() {
		return surfaces;
	}
	public void setSurfaces(Surface[] surfaces) {
		this.surfaces = surfaces;
	}
	public int[] getFaces() {
		return faces;
	}
	public void setFaces(int[] faces) {
		this.faces = faces;
	}
	public float[] getNormals() {
		return normals;
	}
	public void setNormals(float[] normals) {
		this.normals = normals;
	}
	
	public MeshBox getBox() {
		return box;
	}
	public void setBox(MeshBox box) {
		this.box = box;
	}
	
	public int getVertexCount()
	{
		return vertices.length/VERTEX_SIZE;
	}
	public int getFaceCount()
	{
		return faces.length/FACE_SIZE;
	}
	
	public int getNormalCount()
	{
		return normals.length/NORMAL_SIZE;
	}
	
	public float getVC(int vertex, int component)
	{
		return vertices[vertex*VERTEX_SIZE+component];
	}
	
	public float getNC(int normal, int component)
	{
		return normals[normal*NORMAL_SIZE+component];
	}
	
	public int getFV(int face, int vertex)
	{
		return faces[face*FACE_SIZE+vertex*2];
	}
	
	public int getFN(int face, int normal)
	{
		return faces[face*FACE_SIZE+normal*2+1];
	}
	
	public int getFS(int face)
	{
		return faces[face*FACE_SIZE+FACE_SIZE-1];
	}
	
	public float[] getSC(int surface, int component)
	{
		float[] sc=null;
		switch (component){
			case Surface.AMBIENT_COMPONENT:
				sc=surfaces[surface].getAmbient();
				break;
			case Surface.DIFFUSE_COMPONENT:
				sc=surfaces[surface].getDiffuse();
				break;
			case Surface.SPECULAR_COMPONENT:
				sc=surfaces[surface].getSpecular();
				break;
			case Surface.SHININESS_COMPONENT:
				sc=surfaces[surface].getShininess();
				break;
			default:
				System.out.println("Error while geting surface components!");
		}
		return sc;
	}
	
	public void calculateNormals()
	{
		//System.out.println(getFV(1551,0) + " "+getFV(1551,1));
		
		
		normals=new float[getVertexCount()*NORMAL_SIZE];
		int[] vertexMultiples=new int[getVertexCount()];
		for(int i=0;i<getFaceCount();i++)
		{	
			Vector v0=new Vector(getVC(getFV(i, 0), 0),getVC(getFV(i, 0), 1),getVC(getFV(i, 0), 2));
			Vector v1=new Vector(getVC(getFV(i, 1), 0),getVC(getFV(i, 1), 1),getVC(getFV(i, 1), 2));
			Vector v2=new Vector(getVC(getFV(i, 2), 0),getVC(getFV(i, 2), 1),getVC(getFV(i, 2), 2));
			
			
			
			Vector norm=Vector.cross(new Vector(v0, v1),new Vector(v1, v2));
			norm.norm();

			normals[getFV(i, 0)*NORMAL_SIZE]+=norm.getX();
			normals[getFV(i, 0)*NORMAL_SIZE+1]+=norm.getY();
			normals[getFV(i, 0)*NORMAL_SIZE+2]+=norm.getZ();
			vertexMultiples[getFV(i, 0)]+=1;
			
			normals[getFV(i, 1)*NORMAL_SIZE]+=norm.getX();
			normals[getFV(i, 1)*NORMAL_SIZE+1]+=norm.getY();
			normals[getFV(i, 1)*NORMAL_SIZE+2]+=norm.getZ();
			vertexMultiples[getFV(i, 1)]+=1;
			
			normals[getFV(i, 2)*NORMAL_SIZE]+=norm.getX();
			normals[getFV(i, 2)*NORMAL_SIZE+1]+=norm.getY();
			normals[getFV(i, 2)*NORMAL_SIZE+2]+=norm.getZ();
			vertexMultiples[getFV(i, 2)]+=1;
		}
		for(int i=0;i<getNormalCount();i++)
		{
			
			
			normals[i*NORMAL_SIZE]=normals[i*NORMAL_SIZE]/vertexMultiples[i];
			normals[i*NORMAL_SIZE+1]=normals[i*NORMAL_SIZE+1]/vertexMultiples[i];
			normals[i*NORMAL_SIZE+2]=normals[i*NORMAL_SIZE+2]/vertexMultiples[i];
			float len=	normals[i*NORMAL_SIZE]*normals[i*NORMAL_SIZE]+
					normals[i*NORMAL_SIZE+1]*normals[i*NORMAL_SIZE+1]+
					normals[i*NORMAL_SIZE+2]*normals[i*NORMAL_SIZE+2];
			len=(float) Math.sqrt(len);
			
			normals[i*NORMAL_SIZE]=normals[i*NORMAL_SIZE]/len;
			normals[i*NORMAL_SIZE+1]=normals[i*NORMAL_SIZE+1]/len;
			normals[i*NORMAL_SIZE+2]=normals[i*NORMAL_SIZE+2]/len;
		}
		System.out.println("d");
	}
}
