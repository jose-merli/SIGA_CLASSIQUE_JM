/*
 * Created on 03-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.general;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.comun.form.AuxForm;

public class MasterForm extends AuxForm  {
	
	protected Hashtable datos = new Hashtable();

	public Hashtable getDatos() 			 { return (Hashtable)datos.clone();	}
	public void setDatos(Hashtable datos) 	 { this.datos.putAll(datos);	}

	public void 	setModo(String modo) 	 { this.datos.put("MODO", modo);	}
	public String 	getModo() 				 { return (String) this.datos.get("MODO"); 		}

	public void 	setModal(String modal) 	 { this.datos.put("MODAL", modal);	}
	public String 	getModal() 				 { return (String) this.datos.get("MODAL"); 	}	
	
	public void 	setLimpiarFilaSeleccionada(String dato){ UtilidadesHash.set(this.datos, "LIMPIAR_FILA_SELECCIONADA", dato); }
	public String 	getLimpiarFilaSeleccionada() 		   { return UtilidadesHash.getString(this.datos, "LIMPIAR_FILA_SELECCIONADA"); 	}
	
	
	public void 	setFilaSelD(String dato){ UtilidadesHash.set(this.datos, "FILA_SEL", dato); }
	public String 	getFilaSelD() 			 { return UtilidadesHash.getString(this.datos, "FILA_SEL"); }	

	public void setTablaDatosDinamicosD(String dato)	
	{
		Vector vector = new Vector();

		try {
			// Cogemos las filas de la tabla
			StringTokenizer filas = new StringTokenizer(dato, "#");
			for (int i = 0; filas.hasMoreElements(); i++) {
				String fila = filas.nextToken();
				
				Vector vFila = new Vector();
				
				StringTokenizer campos = new StringTokenizer(fila, "%");
				for (int k = 0; campos.hasMoreElements(); k++) {
					String campo = campos.nextToken();
					if ((fila.indexOf("%") == 0) || (k == 1)){
						Vector aux = new Vector();
						StringTokenizer celdas = new StringTokenizer(campo, ",");
						for (int j = 0; celdas.hasMoreElements(); j++) {
							String celda = celdas.nextToken();
							aux.add(celda.trim());
						}
						if (vFila.size() == 0) {
							Vector aux1 = new Vector();
							vFila.add(aux1);			
						}
						vFila.add(aux);			
					}
					else {
						Vector aux = new Vector();
						StringTokenizer celdas = new StringTokenizer(campo, ",");
						for (int j = 0; celdas.hasMoreElements(); j++) {
							String celda = celdas.nextToken();
							aux.add(celda.trim());
						}
						vFila.add(aux);			
					}
				}
				
				vector.add(vFila);
				
/*				// Cogemos las celdas de la fila
				Vector aux = new Vector();
				StringTokenizer celdas = new StringTokenizer(fila, ",");
				for (int j = 0; celdas.hasMoreElements(); j++) {
					String celda = celdas.nextToken();
					aux.add(celda);
				}
				vector.add(aux);			
*/			
				
			}
			this.datos.put("DATOSTABLA", vector);
		}
		catch (Exception e) {
			this.datos.put("DATOSTABLA", vector);
		}
	}
	
	public Vector getDatosTabla() 		{ return (Vector) this.datos.get("DATOSTABLA"); 		}
	public Vector getDatosTablaOcultos (int fila) {
		try {
			Vector v = (Vector) this.datos.get("DATOSTABLA");
			return (Vector)((Vector) v.get(fila)).get(0);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public Vector getDatosTablaVisibles (int fila) { 
		try {
			Vector v = (Vector) this.datos.get("DATOSTABLA");
			return (Vector)((Vector) v.get(fila)).get(1);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Limpia los campos del formulario, a no ser que exista en el request un param. <b>noReset</b>="true"
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) 
	{
		String noreset=(String)request.getParameter("noReset");
		if (noreset==null||(noreset!=null&&!noreset.equalsIgnoreCase("true"))){
			Enumeration e = this.datos.keys();
			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();
				if (!key.equalsIgnoreCase("FILA_SEL")&&!key.equalsIgnoreCase("datosPaginador")&&!key.equalsIgnoreCase("registrosSeleccionados")) {
					this.datos.put(key, "");
				}
			}
		}
	}
	public void reset(String[] keys) 
	{
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			Object objeto = datos.get(key);
			if(objeto instanceof Collection){
				
				 ((Collection)objeto).clear();
			}
			else if(objeto instanceof Map){ 
				((Map)objeto).clear();
			}else if(objeto instanceof String){ 
				objeto="";
			}
		}
		
		
		
	}
	
	public ArrayList getRegistrosSeleccionados() {
		return UtilidadesHash.getArrayList(this.datos, "registrosSeleccionados");	
		
	}
	public void setRegistrosSeleccionados(Object registrosSeleccionados) {
		UtilidadesHash.set(this.datos,"registrosSeleccionados", (ArrayList)registrosSeleccionados);
		//this.registrosSeleccionados = registrosSeleccionados;
	}
	public HashMap getDatosPaginador() {
		return UtilidadesHash.getHashMap(this.datos, "datosPaginador");	
		
	}
	
	public void setDatosPaginador(Object datosPaginador) {
		UtilidadesHash.set(this.datos,"datosPaginador", (HashMap)datosPaginador);
	}
//	public void setDatosPaginador(HashMap datosPaginador) {
//		UtilidadesHash.set(this.datos,"datosPaginador", datosPaginador);
		//this.registrosSeleccionados = registrosSeleccionados;
//	}
	String seleccionarTodos;

	public String getSeleccionarTodos() {
		return UtilidadesHash.getString(this.datos, "seleccionarTodos");
	}
	public void setSeleccionarTodos(String seleccionarTodos) {
		UtilidadesHash.set(this.datos,"seleccionarTodos", seleccionarTodos);
	}
	String rutaFichero;

	public String getRutaFichero() {
		return rutaFichero;
	}
	public void setRutaFichero(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}

	
	
	
	
}
