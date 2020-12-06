package server_side;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileCacheManager implements CacheManager<String, String> {
	
	HashMap<String,String> hmap;
		
	public FileCacheManager() { 
		LoadMap();
	}

	@Override
	public String FindSolution(String P) {
		return hmap.get(P);
	}

	@Override
	public void SaveSolution(String P, String S) {
		hmap.put(P, S);
		SaveMap();

	}
	
	private void SaveMap()
	{
		File myFile=new File("FileCacheManager.bin");
		if(myFile.exists())
			myFile.delete();
		
		try {
			ObjectOutputStream hwriter=new ObjectOutputStream(new FileOutputStream(myFile));
			hwriter.writeObject(hmap);
			hwriter.close();
		} catch (IOException e) {System.out.println("IO Exception");}
	}
	
	@SuppressWarnings("unchecked")
	private void LoadMap()
	{
		File myFile=new File("FileCacheManager.bin");
		if(myFile.exists()){
			try {
				ObjectInputStream hreader=new ObjectInputStream(new FileInputStream(myFile));
				hmap=(HashMap<String, String>) hreader.readObject();
				hreader.close();
			} catch (IOException | ClassNotFoundException e) {System.out.println("IO Exception or class not found");}
		}
		else {
			this.hmap=new HashMap<String,String>();
		}	
	}

}
