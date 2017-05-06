package logic;

public class MeshBox {
	private float minX,minY,minZ=minY=minX=Float.MAX_VALUE;
	private float maxX,maxY,maxZ=maxY=maxX=-Float.MAX_VALUE;
	
	public MeshBox(float[] vertices)
	{
		if(vertices.length%3!=0)
		{
			System.out.println("Wrong meshbox parameters");
		}
		else
		{
			for(int i=0;i<vertices.length/3;i++)
			{
				minX=vertices[i*3]<minX?vertices[i*3]:minX;
				minY=vertices[i*3+1]<minY?vertices[i*3+1]:minY;
				minZ=vertices[i*3+2]<minZ?vertices[i*3+2]:minZ;
				
				maxX=vertices[i*3]>maxX?vertices[i*3]:maxX;
				maxY=vertices[i*3+1]>maxY?vertices[i*3+1]:maxY;
				maxZ=vertices[i*3+2]>maxZ?vertices[i*3+2]:maxZ;
			}
		}
	}

	public float getMinX() {
		return minX;
	}

	public void setMinX(float minX) {
		this.minX = minX;
	}

	public float getMaxX() {
		return maxX;
	}

	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	public float getMinY() {
		return minY;
	}

	public void setMinY(float minY) {
		this.minY = minY;
	}

	public float getMaxY() {
		return maxY;
	}

	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}

	public float getMinZ() {
		return minZ;
	}

	public void setMinZ(float minZ) {
		this.minZ = minZ;
	}

	public float getMaxZ() {
		return maxZ;
	}

	public void setMaxZ(float maxZ) {
		this.maxZ = maxZ;
	}
	
	public String toString()
	{
		return "[X: "+minX+"; "+maxX+" Y: "+minY+"; "+maxY+" Z: "+minZ+"; "+maxZ+"]\n";
		
	}
	
}
