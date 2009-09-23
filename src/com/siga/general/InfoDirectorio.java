
package com.siga.general;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/*
 * Created on 18-feb-2008
 *
 */

/**
 * @author danielc
 *
 */
public class InfoDirectorio {

	public static Vector getInfoDirectorio(String path) 
	{
     	if (path == null || path.equals("")){
			return null;
		} 

    	File f = new File (path);
		if (!f.exists()) { 
			return null;
		}
		
		Vector v = new Vector();
		pintaInfoDirectorio(f ,0, v);
		return v;
	}

	private static void pintaInfoDirectorio (File f, int nivel, Vector v) 
	{
		Date d = new Date(f.lastModified());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fecha = sdf.format(d);
		String acceso = "";

		try {
			//acceso += f.canExecute()?"+x":"-x";
			acceso += f.canRead()?"+r":"-r";
			acceso += f.canWrite()?"+w":"+w";
		}
		catch (Exception e) {
			acceso = "";
		}
		catch (Throwable e) {
			acceso = "";
		}
		
		
		if (f.isDirectory()) {
		    
		    //			traza ("(+) ["+f.getName() + "]", (f.canExecute()?"+X":"-X") + (f.canRead()?"+R":"-R") + (f.canWrite()?"+W":"+W"), fecha, nivel, v);
			
				traza (f.getName(), f.getAbsolutePath(), "d", acceso, fecha, nivel, v);
				
				String[] children = f.list();
			    ArrayList directorios = new ArrayList();
				for (int i = 0; i<children.length; i++) {
				    File aux = new File(f, children[i]);
				    if (aux.isDirectory()) {
				        directorios.add(aux);
				    } else {
				        pintaInfoDirectorio(aux, nivel+1, v);
				    }
				}
				for (int i = 0; i<directorios.size(); i++) {
				    File aux2 = (File) directorios.get(i);
				    pintaInfoDirectorio(aux2, nivel+1, v);
				    
				}
		}
		else {
			
				traza (f.getName(), f.getAbsolutePath(), "f", acceso, fecha, nivel, v);
	//			traza ("|- " + f.getName(), (f.canExecute()?"+X":"-X") + (f.canRead()?"+R":"-R") + (f.canWrite()?"+W":"+W"), fecha, nivel+1, v);
		}
		
	}

	private static void traza (String nombre, String path, String tipo, String acceso, String fecha, int nivel, Vector v) 
	{
		Hashtable h = new Hashtable ();
		h.put("nombre", nombre);
		h.put("path", URLEncoder.encode(path));
		h.put("tipo", tipo);
		h.put("acceso", acceso);
		h.put("fecha", fecha);
		h.put("nivel", ""+nivel);
		v.add (h);
	}

}
