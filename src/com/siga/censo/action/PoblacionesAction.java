package com.siga.censo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPoblacionesBean;
import com.siga.beans.CenProvinciaAdm;
import com.siga.beans.CenProvinciaBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.general.MasterAction;
import com.siga.general.SIGAException;

public class PoblacionesAction extends MasterAction{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		try {				    		
			String modo = (String) request.getParameter("modo");

			if ("getAjaxPoblacionesCP".equalsIgnoreCase(modo)){
				getAjaxPoblacionesCP (request, response);
				
			} else if ("getAjaxPoblacionesCPFiltro".equalsIgnoreCase(modo)){
				getAjaxPoblacionesCPFiltro (request, response);
			} else if ("getAjaxPoblacionesDeProvincia".equalsIgnoreCase(modo)){
				getAjaxPoblacionesDeProvincia (request, response);
			} else if("getAjaxNombreDePoblacion".equalsIgnoreCase(modo)){
				getAjaxNombreDePoblacion(request, response);
			}
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxPoblacionesCPFiltro (HttpServletRequest request, HttpServletResponse response) throws Exception {			
		String valorPoblacion = request.getParameter("valorPoblacion");
		if (valorPoblacion==null)
			throw new SIGAException("Filtro de poblaciones requerido");
		if (valorPoblacion.trim().length()<3)
			throw new SIGAException("El filtro de poblaciones debe ser mayor o igual a tres letras");
		
		String valorGuiones = request.getParameter("valorGuiones");
		if (valorGuiones==null)
			throw new SIGAException("Valor para guiones requerido");
		
		String sNumMaxPoblaciones = request.getParameter("numMaxPoblaciones");
		if (sNumMaxPoblaciones==null || sNumMaxPoblaciones.trim().equalsIgnoreCase(""))
			throw new SIGAException("Número máximo de poblaciones requerido");
		
		Integer numMaxPoblaciones=0;
		try {
			numMaxPoblaciones = Integer.parseInt(sNumMaxPoblaciones);
		}
		catch (Exception exc) {
			throw new SIGAException("El número máximo de poblaciones debe ser numérico");
		}
		
		String valorCP = request.getParameter("valorCP");
		
		ArrayList arrayHtml = new ArrayList();
		JSONObject json = new JSONObject();
		RowsContainer rc = new RowsContainer();
		
		UsrBean usuario=this.getUserBean(request);
				        
        String sHtml="";
		Integer numPoblaciones = 0;				
		String idProvinciaCP = "";
		Boolean poblacionSeleccionada=false;
		
		String tablaPersonaJG=ScsPersonaJGBean.T_NOMBRETABLA;
		String campoPersonaJGIdPoblacion=ScsPersonaJGBean.C_IDPOBLACION;
		String campoPersonaJGIdProvincia=ScsPersonaJGBean.C_IDPROVINCIA;
		String campoPersonaJGCodigoPostal=ScsPersonaJGBean.C_CODIGOPOSTAL;
		
		String tablaPoblaciones=CenPoblacionesBean.T_NOMBRETABLA;
		String campoPoblacionesNombre=CenPoblacionesBean.C_NOMBRE;
		String campoPoblacionesIdPoblacion=CenPoblacionesBean.C_IDPOBLACION;
		String campoPoblacionesIdProvincia=CenPoblacionesBean.C_IDPROVINCIA;		
		
		String tablaProvincias=CenProvinciaBean.T_NOMBRETABLA;
		String campoProvinciasNombre=CenProvinciaBean.C_NOMBRE;
		String campoProvinciasIdProvincia=CenProvinciaBean.C_IDPROVINCIA;
		
		String campoProvinciasNombreAlias="NOMBRE_PROVINCIA";		
		
		// ---------------------------------------------------------
		// - OBTENGO LOS DATOS DE LA PRIMERA PARTE DEL DESPLEGABLE -
		// ---------------------------------------------------------
		
		if (valorCP!=null && !valorCP.trim().equalsIgnoreCase("")) {
			valorCP.trim();		
		
			int intCP=0;
			try {
				intCP = Integer.parseInt(valorCP);
			}
			catch (Exception exc) {
				throw new SIGAException("El código postal debe ser numérico");
			}
			
			if (intCP<1000 || intCP>=53000 || valorCP.substring(2).equalsIgnoreCase("000"))
				throw new SIGAException("Código postal incorrecto");
			
			idProvinciaCP = valorCP.substring(0,2);
			
			/*
			SELECT * FROM (
				SELECT COUNT(*), PERSONA.IDPOBLACION, POBLACIONES.NOMBRE, PERSONA.IDPROVINCIA, PROVINCIAS.NOMBRE AS NOMBRE_PROVINCIA
				FROM (
				    SELECT CODIGOPOSTAL, IDPROVINCIA, IDPOBLACION
				    FROM USCGAE2.SCS_PERSONAJG -- INDEX_CP_PERSONAJG
				    WHERE CODIGOPOSTAL = 'XXX' 
				        AND IDPROVINCIA = SUBSTR(CODIGOPOSTAL,1,2)
				        AND IDPOBLACION IS NOT NULL
				        AND IDPOBLACION != '0' -- Poblacion desconocida
				    ) PERSONA,
				    CEN_POBLACIONES POBLACIONES,
				    CEN_PROVINCIAS PROVINCIAS 
				WHERE POBLACIONES.IDPOBLACION = PERSONA.IDPOBLACION
				    AND PROVINCIAS.IDPROVINCIA = PERSONA.IDPROVINCIA
				    AND REGEXP_LIKE(POBLACIONES.NOMBRE, 'XXX')
				GROUP BY PERSONA.IDPOBLACION, POBLACIONES.NOMBRE, PERSONA.IDPROVINCIA, PROVINCIAS.NOMBRE
				ORDER BY 1 DESC
			) WHERE ROWNUM < XXX					
			 */
			
			String sql="SELECT COUNT(*), " +
				" PERSONA."+campoPersonaJGIdPoblacion+", " +
				" POBLACIONES."+campoPoblacionesNombre+", " +
				" PERSONA."+campoPersonaJGIdProvincia+", " +
				" PROVINCIAS."+campoProvinciasNombre+" AS "+campoProvinciasNombreAlias+
				" FROM ( " +
				" SELECT "+campoPersonaJGCodigoPostal+", "+
				campoPersonaJGIdProvincia+", "+
				campoPersonaJGIdPoblacion+
				" FROM "+tablaPersonaJG+
				" WHERE "+campoPersonaJGCodigoPostal+" = '"+valorCP+"' " +
				" AND "+campoPersonaJGIdProvincia+" = SUBSTR("+campoPersonaJGCodigoPostal+",1,2) " +
				" AND "+campoPersonaJGIdPoblacion+" IS NOT NULL " +
				" AND "+campoPersonaJGIdPoblacion+" != '0' "+
				" ) PERSONA, " +
				tablaPoblaciones+" POBLACIONES, " +
				tablaProvincias+" PROVINCIAS " +
				" WHERE POBLACIONES."+campoPoblacionesIdPoblacion+" = PERSONA."+campoPersonaJGIdPoblacion+
				" AND PROVINCIAS."+campoProvinciasIdProvincia+" = PERSONA."+campoPersonaJGIdProvincia+
				" AND REGEXP_LIKE(POBLACIONES."+campoPoblacionesNombre+", '"+valorPoblacion+"') "+
				" GROUP BY PERSONA."+campoPersonaJGIdPoblacion+", " +
				" POBLACIONES."+campoPoblacionesNombre+", " +
				" PERSONA."+campoPersonaJGIdProvincia+", " +
				" PROVINCIAS."+campoProvinciasNombre+
				" ORDER BY 1 DESC, 3 ASC";
			
			// Control de numero de poblaciones
			sql="SELECT * FROM ("+sql+") WHERE ROWNUM < "+numMaxPoblaciones;
				
	        rc.findNLS(sql);    
			
			if (rc!=null) {			
				sHtml="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
				numPoblaciones=rc.size()+1;			
				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					
					if (registro != null) { 
						String nombrePoblacion = UtilidadesHash.getString(registro, campoPoblacionesNombre);
						
						if (!poblacionSeleccionada && nombrePoblacion.length()==valorPoblacion.length()) {
							sHtml+="<option selected value='"+UtilidadesHash.getString(registro, campoPersonaJGIdPoblacion)+
									"#"+UtilidadesHash.getString(registro, campoPersonaJGIdProvincia)+
									"#"+UtilidadesHash.getString(registro, campoProvinciasNombreAlias)+"'>"+
									nombrePoblacion+"</option>";
							poblacionSeleccionada=true;
						} else {
							sHtml+="<option value='"+UtilidadesHash.getString(registro, campoPersonaJGIdPoblacion)+
								"#"+UtilidadesHash.getString(registro, campoPersonaJGIdProvincia)+
								"#"+UtilidadesHash.getString(registro, campoProvinciasNombreAlias)+"'>"+
								nombrePoblacion+"</option>";
						}
					}
				}
			}			
		}
		
		if (numPoblaciones>1) {
			// Control de numero de poblaciones
			numMaxPoblaciones=numMaxPoblaciones-numPoblaciones;
		}
		
		if (numMaxPoblaciones>1) {
		
			// ---------------------------------------------------------
			// - OBTENGO LOS DATOS DE LA SEGUNDA PARTE DEL DESPLEGABLE -
			// ---------------------------------------------------------
			
			/*
			 	SELECT * FROM (
				 	SELECT POBLACIONES.NOMBRE, POBLACIONES.IDPOBLACION, POBLACIONES.IDPROVINCIA, PROVINCIAS.NOMBRE AS NOMBRE_PROVINCIA
					FROM CEN_POBLACIONES POBLACIONES, CEN_PROVINCIAS PROVINCIAS
					WHERE PROVINCIAS.IDPROVINCIA = POBLACIONES.IDPROVINCIA
		    			AND REGEXP_LIKE(POBLACIONES.NOMBRE, 'XXX')
		    			AND POBLACIONES.IDPROVINCIA=XXX
					ORDER BY 1 ASC
				) WHERE ROWNUM < XXX
			 */
			
		    String sql = " SELECT POBLACIONES."+campoPoblacionesNombre+", "+
		    	" POBLACIONES."+campoPoblacionesIdPoblacion+", "+
		    	" POBLACIONES."+campoPoblacionesIdProvincia+", "+
		    	" PROVINCIAS."+campoProvinciasNombre+" AS "+campoProvinciasNombreAlias+    		    
		    	" FROM " + tablaPoblaciones+" POBLACIONES, " +
				tablaProvincias+" PROVINCIAS " +
		    	" WHERE PROVINCIAS."+campoProvinciasIdProvincia+" = POBLACIONES."+campoPoblacionesIdProvincia+
	    		" AND REGEXP_LIKE(POBLACIONES."+campoPoblacionesNombre+", '"+valorPoblacion+"') ";
		    
		    if (idProvinciaCP!="")
		    	sql+=" AND POBLACIONES."+campoPoblacionesIdProvincia+"="+idProvinciaCP;
			
		    sql+=" ORDER BY 1 ASC";
		    
		    // Control de numero de poblaciones
		    sql="SELECT * FROM ("+sql+") WHERE ROWNUM < "+numMaxPoblaciones;
	
		    rc = new RowsContainer();
	        rc.findNLS(sql);    
			
			if (rc!=null) {	
				if (numPoblaciones<2) {
					sHtml="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
					numPoblaciones=rc.size()+1;
				} else {
					sHtml+="<option value='' disabled>"+valorGuiones+"</option>";
					numPoblaciones+=rc.size()+1;
				}
				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					
					if (registro != null) { 
						String nombrePoblacion = UtilidadesHash.getString(registro, campoPoblacionesNombre);
						
						if (!poblacionSeleccionada && nombrePoblacion.length()==valorPoblacion.length()) {
							sHtml+="<option selected value='"+UtilidadesHash.getString(registro, campoPoblacionesIdPoblacion)+
									"#"+UtilidadesHash.getString(registro, campoPoblacionesIdProvincia)+
									"#"+UtilidadesHash.getString(registro, campoProvinciasNombreAlias)+"'>"+
									nombrePoblacion+"</option>";
							poblacionSeleccionada=true;
						} else {
							sHtml+="<option value='"+UtilidadesHash.getString(registro, campoPoblacionesIdPoblacion)+
									"#"+UtilidadesHash.getString(registro, campoPoblacionesIdProvincia)+
									"#"+UtilidadesHash.getString(registro, campoProvinciasNombreAlias)+"'>"+
									nombrePoblacion+"</option>";
						}						
					}
				}
			}
		}
		
		arrayHtml.add(sHtml);		
		json.put("htmlPoblaciones", arrayHtml);
		json.put("numPoblaciones", numPoblaciones);		
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
		response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 		
	}			
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxPoblacionesCP (HttpServletRequest request, HttpServletResponse response) throws Exception {				
		String valorCP = request.getParameter("valorCP");
		if (valorCP==null||valorCP.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el código postal");		
		
		valorCP.trim();		
		
		int intCP=0;
		try {
			intCP = Integer.parseInt(valorCP);
		}
		catch (Exception exc) {
			throw new SIGAException("El código postal debe ser numérico");
		}
		
		if (intCP<1000 || intCP>=53000 || valorCP.substring(2).equalsIgnoreCase("000"))
			throw new SIGAException("Código postal incorrecto");
		
		String idProvinciaCP = valorCP.substring(0,2);
		
		String tablaPersonaJG=ScsPersonaJGBean.T_NOMBRETABLA;
		String campoPersonaJGIdPoblacion=ScsPersonaJGBean.C_IDPOBLACION;
		String campoPersonaJGIdProvincia=ScsPersonaJGBean.C_IDPROVINCIA;
		String campoPersonaJGCodigoPostal=ScsPersonaJGBean.C_CODIGOPOSTAL;
		
		String tablaPoblaciones=CenPoblacionesBean.T_NOMBRETABLA;
		String campoPoblacionesNombre=CenPoblacionesBean.C_NOMBRE;
		String campoPoblacionesIdPoblacion=CenPoblacionesBean.C_IDPOBLACION;
		
		String tablaProvincias=CenProvinciaBean.T_NOMBRETABLA;
		String campoProvinciasNombre=CenProvinciaBean.C_NOMBRE;
		String campoProvinciasIdProvincia=CenProvinciaBean.C_IDPROVINCIA;
		String campoProvinciasNombreAlias="NOMBRE_PROVINCIA";
		
		/*
		SELECT COUNT(*), PERSONA.IDPOBLACION, POBLACIONES.NOMBRE, PERSONA.IDPROVINCIA, PROVINCIAS.NOMBRE AS NOMBRE_PROVINCIA
		FROM (
		    SELECT CODIGOPOSTAL, IDPROVINCIA, IDPOBLACION
		    FROM USCGAE2.SCS_PERSONAJG -- INDEX_CP_PERSONAJG
		    WHERE CODIGOPOSTAL = 'XXX' 
		        AND IDPROVINCIA = SUBSTR(CODIGOPOSTAL,1,2)
		        AND IDPOBLACION IS NOT NULL
		        AND IDPOBLACION != '0' -- Poblacion desconocida
		    ) PERSONA,
		    CEN_POBLACIONES POBLACIONES,
		    CEN_PROVINCIAS PROVINCIAS 
		WHERE POBLACIONES.IDPOBLACION = PERSONA.IDPOBLACION
		    AND PROVINCIAS.IDPROVINCIA = PERSONA.IDPROVINCIA
		GROUP BY PERSONA.IDPOBLACION, POBLACIONES.NOMBRE, PERSONA.IDPROVINCIA, PROVINCIAS.NOMBRE
		ORDER BY 1 DESC;		
		 */
		
		String sql="SELECT COUNT(*), " +
			" PERSONA."+campoPersonaJGIdPoblacion+", " +
			" POBLACIONES."+campoPoblacionesNombre+", " +
			" PERSONA."+campoPersonaJGIdProvincia+", " +
			" PROVINCIAS."+campoProvinciasNombre+" AS "+campoProvinciasNombreAlias+
			" FROM ( " +
			" SELECT "+campoPersonaJGCodigoPostal+", "+
			campoPersonaJGIdProvincia+", "+
			campoPersonaJGIdPoblacion+
			" FROM "+tablaPersonaJG+
			" WHERE "+campoPersonaJGCodigoPostal+" = '"+valorCP+"' " +
			" AND "+campoPersonaJGIdProvincia+" = SUBSTR("+campoPersonaJGCodigoPostal+",1,2) " +
			" AND "+campoPersonaJGIdPoblacion+" IS NOT NULL " +
			" AND "+campoPersonaJGIdPoblacion+" != '0' "+
			" ) PERSONA, " +
			tablaPoblaciones+" POBLACIONES, " +
			tablaProvincias+" PROVINCIAS " +
			" WHERE POBLACIONES."+campoPoblacionesIdPoblacion+" = PERSONA."+campoPersonaJGIdPoblacion+
			" AND PROVINCIAS."+campoProvinciasIdProvincia+" = PERSONA."+campoPersonaJGIdProvincia+			
			" GROUP BY PERSONA."+campoPersonaJGIdPoblacion+", " +
			" POBLACIONES."+campoPoblacionesNombre+", " +
			" PERSONA."+campoPersonaJGIdProvincia+", " +
			" PROVINCIAS."+campoProvinciasNombre+
			" ORDER BY 1 DESC, 3 ASC";				

		ArrayList arrayHtml = new ArrayList();
		JSONObject json = new JSONObject();
		RowsContainer rc = new RowsContainer();
		
		UsrBean usuario=this.getUserBean(request);
		CenProvinciaAdm provinciaAdm = new CenProvinciaAdm(usuario);

        rc.findNLS(sql);    
        
        String sHtml="";
		Integer numPoblaciones = 0;
		
		if (rc!=null) {			
			sHtml="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			numPoblaciones=rc.size()+1;			
			
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);
				Hashtable registro = (Hashtable)fila.getRow(); 
				
				if (registro != null) { 
					sHtml+="<option value='"+UtilidadesHash.getString(registro, campoPersonaJGIdPoblacion)+
						"#"+UtilidadesHash.getString(registro, campoPersonaJGIdProvincia)+
						"#"+UtilidadesHash.getString(registro, campoProvinciasNombreAlias)+"'>"+
						UtilidadesHash.getString(registro, campoPoblacionesNombre)+"</option>";
				}
			}
		}		
						
		arrayHtml.add(sHtml);
		
		json.put("htmlPoblaciones", arrayHtml);
		json.put("numPoblaciones", numPoblaciones);
		json.put("nombreProvinciaCp", provinciaAdm.getDescripcion(idProvinciaCP));
		json.put("idProvinciaCp", idProvinciaCP);
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 		
	}		
	
	protected void getAjaxPoblacionesDeProvincia (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Date tiempoIni = new Date();
		ClsLogging.writeFileLog("++++++    INICIO getAjaxPoblacionesDeProvincia   ++++++",10);
		final String tipo = "poblacion";
		String valorProvincia = request.getParameter("valorProvincia");
		ClsLogging.writeFileLog("++++++    PARAMETRO getAjaxPoblacionesDeProvincia.provincia: " + valorProvincia,10);
		String[] parametrosWhere = {valorProvincia};
		StringBuilder comboHTMLOptions = getComboHTMLOptions(request, tipo, parametrosWhere);
		//respuestaComboHTMLOptionsJson(comboHTMLOptions, response);
		respuestaComboHTMLoptionsHTML(comboHTMLOptions, response);
		Date tiempoFin = new Date();
		ClsLogging.writeFileLog("++++++    FIN getAjaxPoblacionesDeProvincia   ++++++++  TIEMPO:" +new Long((tiempoFin.getTime()-tiempoIni.getTime())).toString() + " milisegundos. Longitud de options: " + comboHTMLOptions.length(),10);
	}
	
	protected void getAjaxNombreDePoblacion (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String valorProvincia = request.getParameter("valorProvincia");
		String valorPoblacion = request.getParameter("valorPoblacion");
		String nombreDePoblacion = "Error B.D.";
		String sql = "Select NOMBRE as DESCRIPCION From CEN_POBLACIONES Where IDPROVINCIA = "+valorProvincia+" AND IDPOBLACION = "+valorPoblacion+" Order by NOMBRE";
		RowsContainer rc = new RowsContainer();
		
		rc.findNLS(sql);
		
		if (rc!=null && rc.size() == 1) {
			Row fila = (Row) rc.get(0);
			nombreDePoblacion = fila.getString("DESCRIPCION");
		}
		JSONObject json = new JSONObject();
		json.put("nombrePoblacion", nombreDePoblacion);
		respuestaJson(json, response);
	}
}
