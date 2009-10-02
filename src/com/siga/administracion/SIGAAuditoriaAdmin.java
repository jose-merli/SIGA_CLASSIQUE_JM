/*
 * Created on Nov 3, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.administracion;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
//import java.util.StringTokenizer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;

import com.atos.utils.ReadProperties;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.general.CenVisibilidad;


/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SIGAAuditoriaAdmin{
	private static Integer NSEM = new Integer(0);
	private static String fileName;
	private static File ficLog;
	private static long nLastMod = 0;
	Hashtable dataInstitucion = new Hashtable();
	private UsrBean usrbean =null;
	
	List listaResultado = new ArrayList();
	private String linea;
	
	public SIGAAuditoriaAdmin(UsrBean usrb) throws IOException{
			ficheroLog();	
			this.usrbean = usrb;
	}
	
	public void obtenerInstituciones(Integer usuario){
		CenInstitucionAdm institucion = new CenInstitucionAdm(this.usrbean);
		Vector v=null;
		try {
			v=institucion.select();
			Enumeration enumer = v.elements();
			CenInstitucionBean institucionBean;
			 
			while(enumer.hasMoreElements()){
				institucionBean = (CenInstitucionBean)enumer.nextElement();
				dataInstitucion.put(String.valueOf(institucionBean.getIdInstitucion()), institucionBean.getNombre());
			}			
		} catch (ClsExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void ficheroLog(){
//		File file = new File(ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+"SIGA.properties");
		long lst=0; 
		try {
			File file = SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.SIGA);
			if (file!=null)
				lst = file.lastModified();
		} catch (Exception e){
		}
		synchronized (NSEM) {
			if(nLastMod != lst) {
				nLastMod = lst;
			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			    ReadProperties rp=new ReadProperties("SIGA.properties");			       
			    fileName=rp.returnProperty("LogAdmin.archivo");				    
			}			
		}
	}
	
	
	public List obtenerResultados(String direccionIP, String usuario, String accion, HttpServletRequest request) throws IOException, NumberFormatException, ClsExceptions
	{
		File ficLog = new File(fileName+File.separator + "LogAdmin.out");
		
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
		String idInstitucion=user.getLocation();
		String[] cadena = null;
		String ip="";
		String usu="";
		String ac="";		
		
		Pattern p;
		Matcher m;
		if (!ficLog.exists()){
		 	return null;
		 }else{	
		 	obtenerInstituciones(new Integer(user.getUserName()));
		  	BufferedReader sarchivo = new BufferedReader(new FileReader(ficLog)); 
		  	if(!direccionIP.equals("")){
		  		ip=obtenerExpresion(direccionIP);	
		  	}
		  	if(!usuario.equals("")){
		  		usu=obtenerExpresion(usuario);
		  	}
		  	if(!accion.equals("")){
    			ac=obtenerExpresion(accion);
		  	}
		  	
	    	while ((linea=sarchivo.readLine()) != null){					
	    		cadena=obtenerCadenas(linea, ";");	
	    		/*
	    		cadena[0]-->IDInstitucion
	    		cadena[1]-->IPServidor
	    		cadena[2]-->IP
	    		cadena[3]-->Nombre y Apellidos del Usuario
	    		cadena[4]-->NIF Usuario
	    		cadena[5]-->Rol Usuario
	    		cadena[6]-->Fecha
	    		cadena[7]-->Accion
	     		*/
	    		boolean encontrado=true;
	    		
	    		// RGG 15/06/2005 cambio para controlar que venga bien formada la linea del log
	    		if (cadena==null || cadena.length<7) {
	    			encontrado = false;
	    		}
	    		
	    		//				
	    		//////////////////////////////////////
	    		// Anhadimos la restricción para la institución
	    		
	    		//Administrador generico:user.getLevel()=1. Muestra todos 
	    		// El nivel del usuario lo vamos al obtener de CenVisibilidad
	    		// if(user.getLevel()<1){ // No es administador, no se puede dar
	    		if(Integer.valueOf(CenVisibilidad.getNivelInstitucion(user.getLocation())).intValue()<1){ // No es administador, no se puede dar	    		
	    			
	    			encontrado=false;
	    		}
	    		else 
//	    			if(Integer.valueOf(CenVisibilidad.getNivelInstitucion(user.getLocation())).intValue() > 1){	 // Administrador de un colegio.

	    			// Cada institucion solo vera sus datos. CGAE solo vera los suyos
	    			if(Integer.valueOf(CenVisibilidad.getNivelInstitucion(user.getLocation())).intValue() > 0){	 // Administrador de un colegio.
	    				p = Pattern.compile(idInstitucion);
	    				m = p.matcher(cadena[0]); 
	    				encontrado=m.matches();	    				
	    			}	    			
	    		//////////////////////////////////////	
	    		//		 creamos el Matcher a partir del patron, la cadena como parametro    		
	    		if(encontrado && (!direccionIP.equals(""))){	    			
	    		  	p = Pattern.compile(ip);
	    			m = p.matcher(cadena[1]);// se mantiene para la IP del servidor porque no cambia su posicion
		    		encontrado=m.matches();
	    		}
	    		if(encontrado && (!usuario.equals(""))){	    			
	    		  	p = Pattern.compile(usu);
	    			m = p.matcher(cadena[3]); // cambia de posicion
		    		encontrado=m.matches();
	    		}
	    		if(encontrado && (!accion.equals(""))){	    			
	    		  	p = Pattern.compile(ac);
	    			m = p.matcher(dataInstitucion.get(cadena[0])+cadena[7]);
		    		encontrado=m.matches();
	    		}	    		
	    			
	    		if(encontrado){
	    		//	listaResultado.add(cadena[0]+"; "+cadena[1]+"; "+cadena[2]+"; "+cadena[3]);
	    			//listaResultado.add(cadena[3]+"; "+cadena[1]+"; "+cadena[2]+"; "+dataInstitucion.get(cadena[0])+": "+cadena[4]);
	    		    listaResultado.add(dataInstitucion.get(cadena[0]) + ";" + cadena[1] + ";" + cadena[2] + ";" + cadena[3] + ";" + cadena[4] + ";" + cadena[5] + ";" + cadena[6] + ";" + cadena[7]);
	    		}//	
	    	}
	    	sarchivo.close(); 	
	    	
	    	//Invierto el orden para que la lista este ordenada por fecha mas reciente:
	    	//NOTA: se ha filtrado por institucion.
	    	List listaReverse = new ArrayList();
	    	int a=listaResultado.size();
	    	for (int j=listaResultado.size()-1;j>=0;j--){
	    		listaReverse.add(listaResultado.get(j));	    		
	    		
	    	}
	    	
	    	listaResultado = listaReverse;
	    //	Comparator comp = Collections.reverseOrder();	    	
	     //   Collections.sort(listaResultado, comp);

	        //Collections.sort(listaResultado);
		  	return listaResultado;
		 }
			
	}
	
	public static String[] obtenerCadenas(String cadena, String comodin){
		StringTokenizer tokens=new StringTokenizer(cadena, comodin);
		int nCadena=tokens.countTokens();
	    String[] sCadena=new String[nCadena];
	    int i=0;
	    while(tokens.hasMoreTokens()){
	       sCadena[i]=tokens.nextToken().trim().toUpperCase();	      
	       i++;
	    }
		return sCadena;
	}
	
	public String obtenerExpresion(String cadena){	
		String result = null;
		// Reemplazamos los careacteres por otros para poder hacer expresione regulares
		result=reemplazar(cadena, ".","\\.");
		result=reemplazar(result, "*",".*");
		result=reemplazar(result,"?", ".");	
		return result.toUpperCase();
	}
	
	public String reemplazar(String cadena, String origen, String destino){
		 	String s = "";
			while(true) {
				int i = cadena.indexOf(origen);
				if (i == -1) break;
				s = s + cadena.substring(0,i) + destino;
				cadena = cadena.substring(i+origen.length());
			}
			s = s + cadena;		
		return s.trim();		
	}
	/**
	 * @return Returns the dataInstitucion.
	 */
	public Hashtable getDataInstitucion() {
		return dataInstitucion;
	}
	/**
	 * @param dataInstitucion The dataInstitucion to set.
	 */
	public void setDataInstitucion(Hashtable dataInstitucion) {
		this.dataInstitucion = dataInstitucion;
	}
}
	

