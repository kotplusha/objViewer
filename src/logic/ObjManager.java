package logic;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.javafx.fxml.expression.Expression;

import renderer.Camera;

public class ObjManager {
	
	private JFileChooser fc=new JFileChooser();
	private Component parent;
	private List<Float> vertices;
	private List<Surface> surfaces;
	private List<Integer> faces;
	private List<Float> normals;
	private int currentSurface;
	private boolean hasNormals;
	
	private Mesh mesh;
	private Camera camera;
	private Lighting lighting;
	
	public ObjManager(Component parent)
	{
		this.parent=parent;
		FileNameExtensionFilter filter=new FileNameExtensionFilter("OBJ Files", "obj");
		fc.setFileFilter(filter);
		fc.setCurrentDirectory(new File("d:\\3D\\"));
		camera=new Camera();
	}

	public boolean loadObj()
	{
		mesh=null;
		hasNormals=false;
		camera=new Camera();
		lighting=new Lighting();
		vertices=new LinkedList<>();
		surfaces=new LinkedList<>();
		surfaces.add(new Surface());
		faces=new LinkedList<>();
		normals=new LinkedList<>();
		int returnVal=fc.showOpenDialog(parent);
		if(returnVal==JFileChooser.APPROVE_OPTION)
		{
			File file=fc.getSelectedFile();
			FileReader fr;
			BufferedReader br;
			try
			{
				fr=new FileReader(file);
				br=new BufferedReader(fr);
				String line=br.readLine();
				while(line!=null)
				{
					//System.out.println(line);
					if(line.length()>=2 && line.startsWith("v "))
						addVert(line);
					if(line.length()>=2 && line.startsWith("f "))
						addFace(line);
					if(line.length()>=2 && line.startsWith("vn "))
						addNormal(line);
					if(line.length()>=2 && line.startsWith("#a "))
						addAmbient(line);
					if(line.length()>=2 && line.startsWith("#d "))
						addDiffuse(line);
					if(line.length()>=2 && line.startsWith("#sp "))
						addSpecular(line);
					if(line.length()>=2 && line.startsWith("#sh "))
						addShininess(line);
					if(line.length()>=2 && line.startsWith("#p "))
						addPosition(line);
					if(line.length()>=2 && line.startsWith("s "))
						setCurrentSurface(line);
					if(line.length()>=2 && line.startsWith("#angle "))
						addAngle(line);
					line=br.readLine();
				}
				mesh=new Mesh(toArrayF(vertices), toArrayF(normals), toArrayS(surfaces), toArrayI(faces));
				if(!hasNormals)
				{
					//System.out.println(normalIndex);
					//System.out.println(mesh.getFaceCount()*3);
					mesh.calculateNormals();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				mesh=null;
			}
		}
		try{
			BufferedWriter bw=new BufferedWriter(new FileWriter("D://teapot.txt"));
			System.out.println(mesh.getFaceCount());
			for(int i=0;i<mesh.getFaceCount();i++)
			{
				
				bw.write(0.0f+System.lineSeparator());
				bw.write(0.0f+System.lineSeparator());
				bw.write(1f+System.lineSeparator());
				
				bw.write(mesh.getVC(mesh.getFV(i, 0), 0)+System.lineSeparator());
				bw.write(mesh.getVC(mesh.getFV(i, 0), 1)+System.lineSeparator()); 
				bw.write(mesh.getVC(mesh.getFV(i, 0), 2)+System.lineSeparator());
				
				bw.write(mesh.getVC(mesh.getFV(i, 1), 0)+System.lineSeparator());
				bw.write(mesh.getVC(mesh.getFV(i, 1), 1)+System.lineSeparator()); 
				bw.write(mesh.getVC(mesh.getFV(i, 1), 2)+System.lineSeparator());
				
				bw.write(mesh.getVC(mesh.getFV(i, 2), 0)+System.lineSeparator());
				bw.write(mesh.getVC(mesh.getFV(i, 2), 1)+System.lineSeparator()); 
				bw.write(mesh.getVC(mesh.getFV(i, 2), 2)+System.lineSeparator());
				
				bw.write(System.lineSeparator());
				
			}
			bw.flush();
			bw.close();
		}
		catch (Exception e) {
			System.out.println("6874623846");
			e.printStackTrace();
		}
		return mesh!=null;
	}

	private void setCurrentSurface(String line) {
		String[] strS=line.split(" ");
		try
		{
			currentSurface=Integer.parseInt(strS[1])-1;
			while(surfaces.size()<=currentSurface)
			{
				surfaces.add(new Surface());
			}
		}
		catch (Exception e) 
		{
			System.out.println("Wrong face declaration!");
		}
		
	}

	private void addVert(String line) {
		String[] strV=line.split(" ");
		for(int i=1;i<strV.length;i++)
		{
			try
			{
				vertices.add(Float.parseFloat(strV[i]));
			}
			catch(Exception e)
			{}
		}
		//System.out.println(vertices);
	}
	
	private void addFace(String line) {
		String[] strF=line.split(" ");
		for(int i=1;i<=3;i++)
		{
			
			try
			{
				if(hasNormals)
				{
					String[] face=strF[i].split("/");
					faces.add(Integer.parseInt(face[0])-1);
					faces.add(Integer.parseInt(face[2])-1);
				}
				else 
				{
					//if(Integer.parseInt(strF[i])==814)
						//{
						//	System.out.println("814!!!");
						//}
					faces.add(Integer.parseInt(strF[i])-1);
					//System.out.println(faces.get(faces.size()-1));
					faces.add(Integer.parseInt(strF[i])-1);
					//System.out.println(faces.get(faces.size()-1));
				}
			}
			catch(Exception e)
			{
				System.out.println("Wrong face declaration!");
			}
		}
		faces.add(currentSurface);
		//System.out.println(faces.get(faces.size()-1));
		//System.out.println(faces);
	}
	
	private void addNormal(String line) {
		hasNormals=true;
		String[] strN=line.split(" ");
		for(int i=1;i<strN.length;i++)
		{
			try
			{
				normals.add(Float.parseFloat(strN[i]));
			}
			catch(Exception e)
			{}
		}
		//System.out.println(normals);
	}
	
	private void addAmbient(String line) 
	{
		String[] parts=line.split(" ");
		try
		{
			float[] component=new float[4];
			for(int i=2;i<6;i++)
			{
				component[i-2]=Float.parseFloat(parts[i]);
			}
			if(parts[1].compareTo("l")==0)
				lighting.setAmbient(component);
			else
				if(parts[1].compareTo("m")==0)
					lighting.setLmAmbient(component);
				else
				{
				int s=Integer.parseInt(parts[1])-1;
				surfaces.get(s).setAmbient(component);
				}
		}
		catch (Exception e) 
		{
			System.out.println("Wrogn ambient declaration!");
		}
	}
	
	private void addDiffuse(String line) 
	{
		String[] parts=line.split(" ");
		try
		{
			float[] component=new float[4];
			for(int i=2;i<6;i++)
			{
				component[i-2]=Float.parseFloat(parts[i]);
			}
			if(parts[1].compareTo("l")==0)
			{
				lighting.setDiffuse(component);
			}
			else
			{
				int s=Integer.parseInt(parts[1])-1;
				surfaces.get(s).setDiffuse(component);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Wrogn diffuse declaration!");
		}
	}
	
	private void addSpecular(String line) 
	{
		String[] parts=line.split(" ");
		try
		{
			float[] component=new float[4];
			for(int i=2;i<6;i++)
			{
				component[i-2]=Float.parseFloat(parts[i]);
			}
			if(parts[1].compareTo("l")==0)
			{
				lighting.setSpecular(component);
			}
			else
			{
				int s=Integer.parseInt(parts[1])-1;
				surfaces.get(s).setSpecular(component);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Wrogn Specular declaration!");
		}
	}
	
	private void addShininess(String line) 
	{
		String[] parts=line.split(" ");
		try
		{
			float[] component={Float.parseFloat(parts[2])};
			int s=Integer.parseInt(parts[1])-1;
			surfaces.get(s).setShininess(component);
		}
		catch (Exception e) 
		{
			System.out.println("Wrogn Shininess declaration!");
		}
	}
	
	private void addPosition(String line) 
	{
		String[] parts=line.split(" ");
		try
		{
			float[] component=new float[3];
			for(int i=2;i<5;i++)
			{
				component[i-2]=Float.parseFloat(parts[i]);
			}
			if(parts[1].compareTo("l")==0)
				lighting.setPosition(component);
			else
				if(parts[1].compareTo("ceye")==0)
					camera.setEye(new Vector(component[0], component[1], component[2]));
				else
					if(parts[1].compareTo("cat")==0)
						camera.setAt(new Vector(component[0], component[1], component[2]));
			
		}
		catch (Exception e) 
		{
			System.out.println("Wrogn position declaration!");
		}
	}
	
	private void addAngle(String line) 
	{
		String[] parts=line.split(" ");
		try
		{
			camera.setAngle(Float.parseFloat(parts[1]));
		}
		catch (Exception e) 
		{
			System.out.println("Wrogn angle declaration!");
		}
	}
	
	private float[] toArrayF(List<Float> list)
	{
		float[] array=new float[list.size()];
		for(int i=0;i<list.size();i++)
		{
			array[i]=list.get(i);
		}
		return array;
	}
	
	private int[] toArrayI(List<Integer> list)
	{
		int[] array=new int[list.size()];
		for(int i=0;i<list.size();i++)
		{
			array[i]=list.get(i);
		}
		return array;
	}
	private Surface[] toArrayS(List<Surface> list)
	{
		Surface[] array=new Surface[list.size()];
		for(int i=0;i<array.length;i++)
		{
			array[i]=list.remove(0);
		}
		return array;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Camera getCamera() {
		return camera;
	}

	public Lighting getLighting() {
		return lighting;
	}

	public void saveObj(Mesh mesh, Lighting lighting, Camera camera)
	{
		if(mesh!=null && lighting!=null && camera!=null && fc.showSaveDialog(parent)==JFileChooser.APPROVE_OPTION)
		{
			StringBuffer sb=new StringBuffer();
			sb.append("-vertices: "+mesh.getVertexCount()+System.lineSeparator());
			for(int i=0; i<mesh.getVertexCount();i++)
			{
				sb.append("v ");
				for(int j=0;j<Mesh.VERTEX_SIZE;j++)
				{
					sb.append(mesh.getVertices()[i*Mesh.VERTEX_SIZE+j]+" ");
				}
				//sb.deleteCharAt(sb.length()-1);
				sb.append(System.lineSeparator());
			}
			sb.append(System.lineSeparator());
			
			sb.append("-normals: "+mesh.getNormalCount()+System.lineSeparator());
			for(int i=0; i<mesh.getNormalCount();i++)
			{
				sb.append("vn ");
				for(int j=0;j<Mesh.NORMAL_SIZE;j++)
				{
					sb.append(mesh.getNormals()[i*Mesh.NORMAL_SIZE+j]+" ");
				}
				//sb.deleteCharAt(sb.length()-1);
				sb.append(System.lineSeparator());
			}
			sb.append(System.lineSeparator());
			
			sb.append("-faces: "+mesh.getFaceCount()+System.lineSeparator());
			int surface=-1;
			for(int i=0;i<mesh.getFaceCount();i++)
			{
				if(surface!=mesh.getFS(i))
				{
					surface=mesh.getFS(i);
					sb.append("s "+(surface+1));
					sb.append(System.lineSeparator());
				}
				sb.append("f ");
				sb.append((mesh.getFV(i, 0)+1)+"//"+(mesh.getFN(i, 0)+1)+" ");
				sb.append((mesh.getFV(i, 1)+1)+"//"+(mesh.getFN(i, 1)+1)+" ");
				sb.append((mesh.getFV(i, 2)+1)+"//"+(mesh.getFN(i, 2)+1)+" ");
				sb.append(System.lineSeparator());
			}
			sb.append(System.lineSeparator());
			
			sb.append("-surfaces: "+mesh.getFaceCount()+System.lineSeparator());
			for(int i=0; i<mesh.getSurfaces().length; i++)
			{
				sb.append("-surface "+(i+1)+System.lineSeparator());
				float[] a;
				a=mesh.getSurfaces()[i].getDiffuse();
				sb.append("#d "+(i+1)+" "+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+System.lineSeparator());
				
				a=mesh.getSurfaces()[i].getAmbient();
				sb.append("#a "+(i+1)+" "+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+System.lineSeparator());
				
				a=mesh.getSurfaces()[i].getSpecular();
				sb.append("#sp "+(i+1)+" "+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+System.lineSeparator());
				
				a=mesh.getSurfaces()[i].getShininess();
				sb.append("#sh "+(i+1)+" "+a[0]+System.lineSeparator());
			}
			
			sb.append("-lighting: "+mesh.getFaceCount()+System.lineSeparator());
			float[] a;
			a=lighting.getAmbient();
			sb.append("#a l "+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+System.lineSeparator());
			
			a=lighting.getDiffuse();
			sb.append("#d l "+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+System.lineSeparator());
			
			a=lighting.getLmAmbient();
			sb.append("#a m "+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+System.lineSeparator());
			
			a=lighting.getSpecular();
			sb.append("#sp l "+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+System.lineSeparator());
			
			a=lighting.getPosition();
			sb.append("#p l "+a[0]+" "+a[1]+" "+a[2]+System.lineSeparator());
			
			sb.append("-camera: "+mesh.getFaceCount()+System.lineSeparator());
			Vector v;
			v=camera.getEye();
			sb.append("#p ceye "+v.getX()+" "+v.getY()+" "+v.getZ()+System.lineSeparator());
			
			v=camera.getAt();
			sb.append("#p cat "+v.getX()+" "+v.getY()+" "+v.getZ()+System.lineSeparator());

			sb.append("#angle "+camera.getAngle()+System.lineSeparator());
			
			FileWriter fw=null;
			try
			{
				fw= new FileWriter(fc.getSelectedFile()+".obj");
				fw.write(sb.toString());
				fw.close();
			}
			catch (Exception e) 
			{
				System.out.println("Error while saving!");
			}
			finally 
			{
				try 
				{
					fw.close();
				}
				catch (Exception e2) 
				{
					System.out.println("asdasdasd");
				}
			}
		}
	}
}
