
package com.siga.general;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

/*
 * Created on 18-feb-2008
 *
 */

/**
 * @author danielc
 *
 */
public class InfoDirectorio {	
	
	public static Vector getInfoDirectorio(String path, HttpServletRequest request) {
		if (!isAllowed(request)){
			return null;
		}
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

	public static Vector busqueda(String p, String s, HttpServletRequest request) 
	{
		if (!isAllowed(request)){
			return null;
		}
		
     	if (s == null || s.equals("")){
			return null;
		} 
		
     	File f = new File (p);
		if (!f.exists()) { 
			return null;
		}
		
		Vector v = new Vector();
		getApariciones(f, s ,0, v);
		return v;
	}
	
	public static boolean isAllowed(HttpServletRequest request){
		boolean bAllowed = false;
		HttpSession ses= request.getSession();
		if (ses != null && ses.getAttribute("USRBEAN") != null){
			UsrBean usrBean = (UsrBean) ses.getAttribute("USRBEAN");
			if (usrBean.getLocation() != null && "2000".equals(usrBean.getLocation().trim()))
				bAllowed = true;
		} else {
			ClsLogging.writeFileLog("InfoDirectorio.isAllowed: Sesion o userbean en sesion no encontrados", 0);
		}
		return bAllowed;
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
				
				Arrays.sort(children);
				
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
				    //     pintaInfoDirectorio(aux2, nivel, v);
				    Date dat = new Date(aux2.lastModified());
			        SimpleDateFormat fec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String fech = sdf.format(dat);
				    traza (aux2.getName(), aux2.getAbsolutePath(), "dd", acceso, fech, nivel+1, v);
				}
		}
		else {
			
				traza (f.getName(), f.getAbsolutePath(), "f", acceso, fecha, nivel, v);
	//			traza ("|- " + f.getName(), (f.canExecute()?"+X":"-X") + (f.canRead()?"+R":"-R") + (f.canWrite()?"+W":"+W"), fecha, nivel+1, v);
		}
		
	}			

	private static void getApariciones (File f, String s, int nivel, Vector v) 
	{
		
			Date d = new Date(f.lastModified());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fecha = sdf.format(d);
			String acceso = "";
			
			try {
				acceso += f.canRead()?"+r":"-r";
				acceso += f.canWrite()?"+w":"+w";
			}
			catch (Exception e) {
				acceso = "";
			}
			catch (Throwable e) {
				acceso = "";
			}
			
			if(f.getName().contains(s)){
				String path=f.getAbsolutePath();
				if (path.startsWith("C:")) {
					// windows
					path = path.substring(2,path.length());
					path = UtilidadesString.replaceAllIgnoreCase(path,"\\","/");
				}
				if (f.isDirectory()) {
					traza (path, f.getAbsolutePath(), "dd", acceso, fecha, nivel, v);
				}else{
					traza (path, f.getAbsolutePath(), "f", acceso, fecha, nivel, v);
				}
			}
			
			if (f.isDirectory()) {		    
					String[] children = f.list();
				    ArrayList directorios = new ArrayList();
					for (int i = 0; i<children.length; i++) {
					    File aux = new File(f, children[i]);
				        getApariciones(aux, s, nivel+1, v);
					}
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
