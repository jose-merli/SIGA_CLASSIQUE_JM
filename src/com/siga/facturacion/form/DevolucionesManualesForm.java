/*
 * VERSIONES:
 * RGG 03/01/2007  Creación
 */
package com.siga.facturacion.form;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.siga.general.MasterForm;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Clase form de struts para el mantenimiento de devoluciones manuales
 * @author RGG AtosOrigin
 */
public class DevolucionesManualesForm extends MasterForm{

	public void setFechaCargoDesde (String dato) {
		UtilidadesHash.set(datos, "FechaCargoDesde", dato);
 	}
	public String getFechaCargoDesde() {
 		return UtilidadesHash.getString(datos, "FechaCargoDesde");
 	}
 		
	public void setFechaCargoHasta (String dato) {
		UtilidadesHash.set(datos, "FechaCargoHasta", dato);
 	}
	public String getFechaCargoHasta() {
 		return UtilidadesHash.getString(datos, "FechaCargoHasta");
 	}
 		
	public void setNumeroRemesa (String dato) {
		UtilidadesHash.set(datos, "NumeroRemesa", dato);
 	}
	public String getNumeroRemesa() {
 		return UtilidadesHash.getString(datos, "NumeroRemesa");
 	}
	
	public void setNumeroFactura (String dato) {
		UtilidadesHash.set(datos, "NumeroFactura", dato);
 	}
	public String getNumeroFactura() {
 		return UtilidadesHash.getString(datos, "NumeroFactura");
 	}

	public void setNumeroRecibo (String dato) {
		UtilidadesHash.set(datos, "NumeroRecibo", dato);
 	}
	public String getNumeroRecibo() {
 		return UtilidadesHash.getString(datos, "NumeroRecibo");
 	}
 		
	public void setTitular (String dato) {
		UtilidadesHash.set(datos, "Titular", dato);
 	}
	public String getTitular() {
 		return UtilidadesHash.getString(datos, "Titular");
 	}
 		
	public void setNombreTitular (String dato) {
		UtilidadesHash.set(datos, "NombreTitular", dato);
 	}
	public String getNombreTitular() {
 		return UtilidadesHash.getString(datos, "NombreTitular");
 	}
 		
	public void setFechaDevolucion (String dato) {
		UtilidadesHash.set(datos, "FechaDevolucion", dato);
 	}
	public String getFechaDevolucion() {
 		return UtilidadesHash.getString(datos, "FechaDevolucion");
 	}
 		
	public void setBanco (String dato) {
		UtilidadesHash.set(datos, "Banco", dato);
 	}
	public String getBanco() {
 		return UtilidadesHash.getString(datos, "Banco");
 	}
 		
	public void setAplicarComisiones (String dato) {
		UtilidadesHash.set(datos, "AplicarComisiones", dato);
 	}
	public String getAplicarComisiones () {
 		return UtilidadesHash.getString(datos, "AplicarComisiones");
 	}
 		
	public void setHayMotivos (String dato) {
		UtilidadesHash.set(datos, "HayMotivos", dato);
 	}
	public String getHayMotivos () {
 		return UtilidadesHash.getString(datos, "HayMotivos");
 	}
 		
	public void setIdFactura (String dato) {
		UtilidadesHash.set(datos, "IdFactura", dato);
 	}
	public String getIdFactura () {
 		return UtilidadesHash.getString(datos, "IdFactura");
 	}

	public void setRecibos (String dato) {
		UtilidadesHash.set(datos, "Recibos", dato);
 	}
	public String getRecibos () {
 		return UtilidadesHash.getString(datos, "Recibos");
 	}
	
	
	/**
	 * Obtiene del form solamente los idrecibos.
	 * @return arraylist con los ids de recibos solamente
	 */
	public ArrayList getIdRecibos () {
	
		ArrayList salida = new ArrayList();
	    StringTokenizer st = null;
	    int contadorReg=1;
	    String tok=getRecibos();
	    try {
	    	st = new StringTokenizer(tok, ";");
		    contadorReg=st.countTokens();
	    } catch (java.util.NoSuchElementException nee) {
	    	// solamente existe un token
	    }

	    while (st.hasMoreElements())
	    {
	        StringTokenizer st2 = new StringTokenizer(st.nextToken(), "%%");
	        // token: motivo, idfactura, idrecibo
	        // solamente se obtiene el idrecibo, que vendria el tercero
	        st2.nextToken();
			st2.nextToken();
	        salida.add(st2.nextToken());
	    }
	    return salida;
 	}
 		
}
