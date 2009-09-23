/*
 * VERSIONES:
 * raul.ggonzalez 10-02-2005 creacion
 */
package com.siga.Utilidades;

import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atos.utils.*;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenGruposClienteClienteBean;
import com.siga.beans.CenPersonaBean;

/**
 * Clase de utilidades para Productos y servicios
 * @author atosOrigin
 */
public class UtilidadesProductosServicios {

	
	  /**
	   * Devuelve la descripción asociada a la clave estado Pago
	   * @param clave Con la clave del estado
	   * @return String con la descripcion en forma de recurso
	   * @throws ClsException en caso de error
	   */
	  public static String getEstadoPago(String clave) throws ClsExceptions {
	  	
	  	
	  	if (clave==null) {
	  		return "pys.estadoPago.pendienteFactura";
	  		//return null;
	  	}

	  	// RGG 28-04-2005 Cambio para trim de las claves
  		clave = clave.trim();
		
		if (clave.equals("PAGADO")) {
			return  "pys.estadoPago.pagado";
		} else 
		if (clave.equals("ENFACTURA")) {
			return  "pys.estadoPago.enFactura";
		} else 
		if (clave.equals("PENDIENTE")) {
			return  "pys.estadoPago.pendienteFactura";
		} else {
	  		return "pys.estadoPago.pendienteFactura";
			//return null;
		}

	  }

	  /**
	   * Devuelve la descripción asociada a la clave estado del producto o del estado del servicio
	   * @param clave Con la clave del estado
	   * @return String con la descripcion en forma de recurso
	   * @throws ClsException en caso de error
	   */
	  public static String getEstadoProductoServicio(String clave) throws ClsExceptions {
	  	
	  	if (clave==null) return null;

		if (clave.equals(ClsConstants.PRODUCTO_ACEPTADO)) {
			return  "pys.estadoProducto.aceptado";
		} else 
		if (clave.equals(ClsConstants.PRODUCTO_PENDIENTE)) {
			return  "pys.estadoProducto.pendiente";
		} else 
		if (clave.equals(ClsConstants.PRODUCTO_DENEGADO)) {
			return  "pys.estadoProducto.denegado";
		} else 
		if (clave.equals(ClsConstants.PRODUCTO_BAJA)) {
			return "pys.estadoProducto.baja";
		} else {
			return null;
		}
	  
	  }
	  
	  /**
	   * Devuelve los campos de salida de la query
	   * @param Vector con las tablas que formarán parte de la query
	   * @return String con los campos que se consultarán en la query
	   * @throws ClsException en caso de error
	   */
	  public static String getCamposSalida(Vector tablas) throws ClsExceptions {
	  	
	  	//String resultado
	  	String resultado = "";
	  	
	  	try{
		  	// para saber si se ha incluido ya el primer campo 
		  	// para incluir "," antes de los campos
		  	boolean hayPrimero = false;
		  	
		  	// COMPROBAMOS LA TABLA CEN_CLIENTE
		  	resultado += (tablas.contains(CenClienteBean.T_NOMBRETABLA)?" "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+", "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" ":"");
		  	// comprobamos si ya se han insertado los primeros campos
		  	if(!resultado.equals("")) hayPrimero=true; 
		  	
		  	// COMPROBAMOS LA TABLA CEN_COLEGIADO
		  	resultado += (tablas.contains(CenColegiadoBean.T_NOMBRETABLA)&&(!hayPrimero)?" "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+", "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+" ":"");
		  	// comprobamos si ya se han insertado los primeros campos
		  	if(!resultado.equals("")) hayPrimero=true;
		  	
		  	// COMPROBAMOS LA TABLA CEN_GRUPOSCLIENTE_CLIENTE
		  	resultado += (tablas.contains(CenGruposClienteClienteBean.T_NOMBRETABLA)&&(!hayPrimero)?" "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION+", "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDPERSONA+" ":"");
		  	// comprobamos si ya se han insertado los primeros campos
		  	if(!resultado.equals("")) hayPrimero=true;
		  	
		  	// SI NO SE HAN INCLUIDO NINGUNA DE LAS DOS TABLAS, 
		  	// POR DEFECTO, INCLUIMOS LA DE CEN_CLIENTE
		  	if (!hayPrimero) resultado += CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+", "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" ";
	  	}catch(Exception e){
	  		throw new ClsExceptions (e, "Error en UtilidadesProductosServicios.getCamposSalida()");
	  	}
	  	
	  	// Devolvemos el resultado final
	  	return resultado;
	  }
	  
	  
	  /**
	   * Devuleve un String con el campo FROM de la query, dependiendo de la tablas que se
	   * quieran incluir en la misma. 
	   * 
	   * @param Vector tablas, con las tablas que se quieren incluir en la query
	   * @return String con el campo from de la query
	   * @throws ClsExceptions
	   */
	  public static String getFrom (Vector tablas) throws ClsExceptions {
	  	
	  	// String resultado final 
	  	String resultado = "";
	  	
	  	try{
		  	// para saber si se ha incluido ya la primera tabla 
		  	// para incluir la tabla por defecto
		  	boolean hayPrimero = false;

		  	// COMPROBAMOS LA TABLA CEN_CLIENTE
		  	resultado += (tablas.contains(CenClienteBean.T_NOMBRETABLA)?" "+CenClienteBean.T_NOMBRETABLA+" ":"");
		  	// comprobamos si ya se ha insertado la primera tabla
		  	if(!resultado.equals("")) hayPrimero=true; 
		  	
		  	// COMPROBAMOS LA TABLA CEN_COLEGIADO
		  	resultado += (tablas.contains(CenColegiadoBean.T_NOMBRETABLA)?(hayPrimero?", ":" ")+CenColegiadoBean.T_NOMBRETABLA+" ":"");
		  	// comprobamos si ya se ha insertado la primera tabla
		  	if(!resultado.equals("")) hayPrimero=true;

		  	// COMPROBAMOS LA TABLA CEN_PERSONA
		  	resultado += (tablas.contains(CenPersonaBean.T_NOMBRETABLA)?(hayPrimero?", ":" ")+CenPersonaBean.T_NOMBRETABLA+" ":"");
		  	// comprobamos si ya se ha insertado la primera tabla
		  	if(!resultado.equals("")) hayPrimero=true;

		  	// COMPROBAMOS LA TABLA CEN_GRUPOSCLIENTE_CLIENTE
		  	resultado += (tablas.contains(CenGruposClienteClienteBean.T_NOMBRETABLA)?(hayPrimero?", ":" ")+CenGruposClienteClienteBean.T_NOMBRETABLA+" ":"");
		  	// comprobamos si ya se ha insertado la primera tabla
		  	if(!resultado.equals("")) hayPrimero=true;
		  	
		  	// SI NO SE HAN INCLUIDO NINGUNA DE LAS DOS TABLAS, 
		  	// POR DEFECTO, INCLUIMOS LA DE CEN_CLIENTE
		  	if (!hayPrimero) resultado += CenClienteBean.T_NOMBRETABLA+" ";
		  	
	  	}catch(Exception e){
	  		throw new ClsExceptions (e, "Error en UtilidadesProductosServicios.getFrom()");
	  	}
	  	return resultado;
	  }
	  
	  /**
	   * Devuelve la parte fija del where, de la query que se va a lanzar. 
	   * 
	   * @param Vector tablas, con las tablas que se quieren incluir en la query
	   * @return String con la parte del campo where de la query, que siempre se va a incluir
	   * @throws ClsExceptions
	   */
	  public static String getWhereFijo (Vector tablas) throws ClsExceptions {

	  	// String resultado
	  	String resultado = "";
	  	
	  	try{
		  	// para saber si se ha incluido ya la primera cláusula del where 
		  	// para incluir "AND" antes de la siguiente, o para anhadir la consulta por defecto.
		  	boolean hayPrimero = false;
		  	
		  	// COMPROBAMOS LA TABLA CEN_CLIENTE
		  	if (tablas.contains(CenClienteBean.T_NOMBRETABLA)){
		  		resultado += " "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+"@IDINSTITUCION@ ";
		  		resultado += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+"@IDPERSONA@ ";
		  	}
		  	// comprobamos si ya se han insertado los primeros campos
		  	if(!resultado.equals("")) hayPrimero=true; 
		  	
		  	// COMPROBAMOS LA TABLA CEN_COLEGIADO
		  	if(tablas.contains(CenColegiadoBean.T_NOMBRETABLA)&&(!hayPrimero)){
		  		resultado += (hayPrimero?" AND ":" ")+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+" = "+"@IDINSTITUCION@ ";
		  		resultado += " AND " + CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+" = "+"@IDPERSONA@ ";
		  	}
		  	if(!resultado.equals("")) hayPrimero=true; 

		  	// COMPROBAMOS LA TABLA CEN_GRUPOSCLIENTE_CLIENTE
		  	if(tablas.contains(CenGruposClienteClienteBean.T_NOMBRETABLA)&&(!hayPrimero)){
		  		resultado += (hayPrimero?" AND ":" ")+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION+" = "+"@IDINSTITUCION@ ";
		  		resultado += " AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDPERSONA+" = "+"@IDPERSONA@ ";
		  	}
		  	if(!resultado.equals("")) hayPrimero=true; 
		  	
		  	// SI NO SE HAN INCLUIDO NINGUNA DE LAS DOS TABLAS, 
		  	// POR DEFECTO, INCLUIMOS LA DE CEN_CLIENTE
		  	if (!hayPrimero) {
		  		resultado += " "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+"@IDINSTITUCION@ ";
		  		resultado += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+"@IDPERSONA@ ";
		  	}

	  	}catch(Exception e){
	  		throw new ClsExceptions (e, "Error en UtilidadesProductosServicios.getWhereFijo()");
	  	}
	  	
	  	// Devolvemos el resultado final
	  	return resultado;
	  }
	  
	  /**Devuelve un String con los criterios que se quieran anhadir en la query.
	   * 
	   * @param Vector criterios, vector de Hashtables que contendrá las siguientes keys:<br>
	   * 	-OPERADOR, "OR","AND"<br>
	   * 	-NOMBREREAL, con formato NOMBRETABLA.NOMBRECAMPO<br>
	   * 	-OPERACION "=","<>",">","<",...<br>
	   * 	-VALOR<br>
	   * Con sus correspondientes valores.
	   * 
	   * @return String resultado, con las clausulas que se quieran anhadir a la consulta.
	   * @throws ClsExceptions
	   */
	  public static String getWhereVariable (Vector criterios)throws ClsExceptions{

	  	//variable resultado
	  	String resultado="";
	  	

	  	try{

		  	// recorremos el vector de los criterios, accediendo a cada Hashtable
		  	// y recuperamos la key y el valor 
		  	for(int cont=0;cont<criterios.size();cont++){
		  		
		  		//variable auxiliar para recuperar cada criterio
		  		Hashtable hash = (Hashtable)criterios.get(cont);
		  		String operador = "";
	  			String abrirPar = (String)hash.get("ABRIRPAR");
	  			if (abrirPar!=null && abrirPar.equals("1")) abrirPar = "("; else abrirPar = ""; 
	  			String cerrarPar = (String)hash.get("CERRARPAR");
	  			if (cerrarPar!=null && cerrarPar.equals("1")) cerrarPar = ")"; else cerrarPar = ""; 
	  			String value = (String)hash.get("VALOR");
		  		if (value!=null && value.indexOf("$")!=-1) {
		  			// viene de GruposCliente_Cliente
		  			String idGrupo = value.substring(0,value.indexOf("$")); 
		  			String idInstGrupo = value.substring(value.indexOf("$")+1,value.length()); 
		  			if ((String)hash.get("OPERADOR")!=null&&(cont>0)){
			  			if(((String)hash.get("OPERADOR")).equalsIgnoreCase("Y"))
							operador = " AND ";
			  			else if (((String)hash.get("OPERADOR")).equalsIgnoreCase("O"))
							operador = " OR ";
			  		}
		  			String nombreReal=hash.get("NOMBREREAL").toString().replaceAll("@IDGRUPO@",""+idGrupo+","+idInstGrupo+"");
			  		if (operador.equalsIgnoreCase("")&&cont>0)operador=" AND ";
			  		//resultado += " " + operador + " " + abrirPar + " " + (String)hash.get("NOMBREREAL") + " " + (String)hash.get("OPERACION") + " " + idGrupo + " " + cerrarPar + " ";
			  		resultado += " " + operador + " " + abrirPar + " " + nombreReal + " " + (String)hash.get("OPERACION") + " 1 " + cerrarPar + " ";
		  			
		  			// criterio idInstitucionGrupo
			  		if ((String)hash.get("OPERADOR")!=null&&(cont>0)){
			  			if(((String)hash.get("OPERADOR")).equalsIgnoreCase("Y"))
							operador = " AND ";
			  			else if (((String)hash.get("OPERADOR")).equalsIgnoreCase("O"))
							operador = " OR ";
			  		}
			  		
			  		//resultado += " AND CEN_GRUPOSCLIENTE_CLIENTE.IDINSTITUCION_GRUPO = " + idInstGrupo + " ";
		  			
		  		} else {
			  		if ((String)hash.get("OPERADOR")!=null&&(cont>0)){
			  			if(((String)hash.get("OPERADOR")).equalsIgnoreCase("Y"))
							operador = " AND ";
			  			else if (((String)hash.get("OPERADOR")).equalsIgnoreCase("O"))
							operador = " OR ";
			  		}
			  		if (operador.equalsIgnoreCase("")&&cont>0)operador=" AND ";
			  		
			  		resultado += " " + operador + " " + abrirPar + " " + (String)hash.get("NOMBREREAL") + " ";
			  		
			  		String operacion = (String)hash.get("OPERACION");
			  		if (operacion.trim().equalsIgnoreCase("is null")) {
			  		    String valor = (String)hash.get("VALOR");
			  		    if (valor.trim().equalsIgnoreCase("1")||valor.trim().equalsIgnoreCase("'1'"))
			  		      resultado += operacion;
			  		    if (valor.trim().equalsIgnoreCase("0")||valor.trim().equalsIgnoreCase("'0'"))
			  		      resultado += " is not null ";
			  		}
			  		else {
			  		    resultado += operacion + " " + (String)hash.get("VALOR") + " ";
			  		}
			  		resultado += cerrarPar + " "; 
		  		}
		  	}
	  	}catch(Exception e){
	  		throw new ClsExceptions (e, "Error en UtilidadesProductosServicios.getWhereVaiable()");
	  	}

	  	//devolvemos el resultado 
	  	return resultado;
	  }
	  
	  /**Devuleve la para de la query que hace el Join entre las tablas que se van a incluir
	   * 
	   * @param Vector tablas, , con las tablas que se quieren incluir en la query
	   * @return String resultado, con el Join
	   * @throws ClsExceptions
	   */
	  public static String getCriteriosJoin (Vector tablas)throws ClsExceptions{
	  	
	  	// Variable con el resultado final 
	  	String resultado="";
	  	try{

	  		// para saber si se ha incluido ya la primera cláusula del where 
		  	// para incluir "AND" antes de la siguiente, o para anhadir la consulta por defecto.
		  	boolean hayPrimero = false;
		  	
		  	// COMPROBAMOS LA TABLA CEN_CLIENTE con CEN_PERSONA
		  	if (tablas.contains(CenClienteBean.T_NOMBRETABLA) && tablas.contains(CenPersonaBean.T_NOMBRETABLA))
		  		resultado += " "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" ";
		  	// comprobamos si ya se han insertado los primeros campos
		  	if(!resultado.equals("")) hayPrimero=true; 
		  	
//		  COMPROBAMOS LA TABLA CEN_CLIENTE con CEN_COLEGIADO
		  	if(tablas.contains(CenClienteBean.T_NOMBRETABLA) && tablas.contains(CenColegiadoBean.T_NOMBRETABLA)){
		  		resultado += (hayPrimero?" AND ":" ")+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+"(+) ";
		  		resultado += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+"(+) ";
		  	}
		  	if(!resultado.equals("")) hayPrimero=true; 
		  	
		  	// COMPROBAMOS LA TABLA CEN_COLEGIADO con CEN_PERSONA
		  	if (!tablas.contains(CenClienteBean.T_NOMBRETABLA)){//  hacemos join de las tablas cen_colegiado y cen_persona 
		  		                                                // siempre que no exista join entre cen_cliente y cen_colegiado
			  	if(tablas.contains(CenColegiadoBean.T_NOMBRETABLA) && tablas.contains(CenPersonaBean.T_NOMBRETABLA)){
			  		resultado += (hayPrimero?" AND ":" ")+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+"(+) = "+ CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA +" ";
			  	}
			  	if(!resultado.equals("")) hayPrimero=true; 
		  	}

		  	
            //PDM: Ya no hacemos la comprobacion con la tabla  CEN_GRUPOSCLIENTE_CLIENTE porque si se consultase por ella, ahora se sustituye por la 
		  	//     funcion F_SIGA_PERTENECEGRUPOFIJO
		  	
		  	
		  	// COMPROBAMOS LA TABLA CEN_GRUPOSCLIENTE_CLIENTE con CEN_CLIENTE
		  	/*if(tablas.contains(CenGruposClienteClienteBean.T_NOMBRETABLA) && tablas.contains(CenClienteBean.T_NOMBRETABLA)){
		  		resultado += (hayPrimero?" AND ":" ")+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+" ";
		  		resultado += " AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDPERSONA+" = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" ";
		  	}
		  	if(!resultado.equals("")) hayPrimero=true; 

		  	// COMPROBAMOS LA TABLA CEN_GRUPOSCLIENTE_CLIENTE con CEN_PERSONA
		  	if(tablas.contains(CenGruposClienteClienteBean.T_NOMBRETABLA) && tablas.contains(CenPersonaBean.T_NOMBRETABLA)){
		  		resultado += (hayPrimero?" AND ":" ")+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDPERSONA+" = "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" ";
		  	}
		  	if(!resultado.equals("")) hayPrimero=true; 

		  	// COMPROBAMOS LA TABLA CEN_GRUPOSCLIENTE_CLIENTE con CEN_COLEGIADO
		  	if(tablas.contains(CenGruposClienteClienteBean.T_NOMBRETABLA) && tablas.contains(CenColegiadoBean.T_NOMBRETABLA)){
		  		resultado += (hayPrimero?" AND ":" ")+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDINSTITUCION+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+" ";
		  		resultado += " AND "+CenGruposClienteClienteBean.T_NOMBRETABLA+"."+CenGruposClienteClienteBean.C_IDPERSONA+" = "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+" ";
		  	}*/
	  	} catch(Exception e) {
	  		throw new ClsExceptions (e, "Error en UtilidadesProductosServicios.getCriteriosJoin()");
	  	}
	  	return resultado;
	  }
	  
	  /**
	   * 
	   * @param Vector tablas, vector de String que contendrá el nombre de las tablas a consultar<br>
	   * @param Vector criterios,  vector de Hashtables que contendrá las siguientes keys:<br>
	   * 	-OPERADOR, "OR","AND" (el primer criterio, no tendrá operador)<br>
	   * 	-NOMBREREAL, con formato NOMBRETABLA.NOMBRECAMPO<br>
	   * 	-OPERACION "=","<>",">","<",...<br>
	   * 	-VALOR<br>
	   * @return String resultado, consulta SQL
	   * @throws ClsExceptions
	   */
	  public static String getQuery(Vector tablas, Vector criterios)throws ClsExceptions{
	  	
	  	// variable resultado
	  	String consulta=" SELECT ";
	  	try{
	  		//campos de consulta
	  		consulta += " " + UtilidadesProductosServicios.getCamposSalida(tablas);
	  		
	  		//tablas a consultar
	  		consulta += " FROM " + UtilidadesProductosServicios.getFrom(tablas);
	  		
	  		//where fijo
	  		consulta += " WHERE " + UtilidadesProductosServicios.getWhereFijo(tablas);
	  		
	  		//where variable
	  		if ((criterios!=null)&&(criterios.size()>0))
	  			consulta += " AND (" + UtilidadesProductosServicios.getWhereVariable(criterios) + ") ";
	  		
	  		//join de las tablas
	  		consulta += (tablas.size()>1?" AND (" + UtilidadesProductosServicios.getCriteriosJoin(tablas) + ") ":"");
	  		
	  	}catch(Exception e){
	  		throw new ClsExceptions (e, "Error en UtilidadesProductosServicios.getQuery()");
	  	}
	  	return consulta;
	  }

	  /**
		 * Reemplaza una cadea de caracteres por otro
		 * 
		 * @param cadenaOld, cadena a reemplazar
		 * @param cadenaNew, nueva cadea de caracteres
		 */
		public static String reemplazaString (String cadenaOld, String cadenaNew, String frase){
			
			final Pattern pattern = Pattern.compile(cadenaOld);
			final Matcher matcher = pattern.matcher( frase );
			frase = matcher.replaceAll(cadenaNew);
			return frase;
		}
		
	  
	  /**Devuelve la query por defecto a la tabla CEN_CLIENTE
	   *  
	   * @return String resultado
	   * @throws ClsExceptions
	   */
	  public static String getQueryPorDefecto ()throws ClsExceptions{
	  	
	  	//devuelve una consulta por defecto
	  	String consulta = " SELECT " +
							CenClienteBean.C_IDINSTITUCION + ", " +CenClienteBean.C_IDPERSONA +
							" FROM " +
							CenClienteBean.T_NOMBRETABLA +
							" WHERE " +
							CenClienteBean.C_IDINSTITUCION + " = @IDINSTITUCION@" +
							" AND " +
							CenClienteBean.C_IDPERSONA + " = @IDPERSONA@";
	  	
	  	return consulta;
	  }
}