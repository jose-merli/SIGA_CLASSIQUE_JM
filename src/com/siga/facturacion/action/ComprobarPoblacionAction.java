/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * <p>Clase que gestiona la población de clientes.</p>
 */

package com.siga.facturacion.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenPoblacionesBean;
import com.siga.beans.CenProvinciaAdm;
import com.siga.beans.CenProvinciaBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class ComprobarPoblacionAction extends MasterAction{
	
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

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {		
		    
			miForm = (MasterForm) formulario;
			if (miForm == null)
				return mapping.findForward(mapDestino);
			
			String accion = miForm.getModo();

			if ( accion.equalsIgnoreCase("getAjaxPoblacionesCP")){
				getAjaxPoblacionesCP (request, response);
				return null;
				
			} else if ( accion.equalsIgnoreCase("getAjaxPoblacionesCPFiltro")){
				getAjaxPoblacionesCPFiltro (request, response);
				return null;								
				
			} else {
				return super.executeInternal(mapping,
						      formulario,
						      request, 
						      response);
			}
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
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
				" ORDER BY 1 DESC";
			
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
		
		// Control de numero de poblaciones
		numMaxPoblaciones=numMaxPoblaciones-numPoblaciones;		
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
					sHtml+="<option value=''>"+valorGuiones+"</option>";
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
		 response.setContentType("text/x-json;charset=ISO-8859-15");
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
			" ORDER BY 1 DESC";				

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
		 response.setContentType("text/x-json;charset=ISO-8859-15");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 		
	}		

	/** 
	 *  Funcion que atiende la accion abrirConParametros
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * 
	 * @return  String  Destino del action
	 *   
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirConParametros (ActionMapping mapping, 		
										  	 MasterForm formulario, 
											 HttpServletRequest request, 
											 HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

	}
	
	/**
	 * Muestra la pestanha de Comprobar Población de la pantalla de mantenimiento.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String forward = "";		
		UserTransaction tx = null;
		
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			String idSerieFacturacion=(String)request.getSession().getAttribute("idSerieFacturacion");
			
			if (idSerieFacturacion == null)
			{
				forward = "serieFacturacionNoExiste";
			}
			else
			{		
				Object[] param_in = new Object[2];
				param_in[0] = idInstitucion;
				param_in[1] = idSerieFacturacion;
				String resultado[] = new String[3];
				tx = this.getUserBean(request).getTransactionPesada();
				tx.begin();
			    resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.OBTENCIONPOBLACIONCLIENTES(?,?,?,?,?)}", 3, param_in);
				
				String contador = resultado[0];
				
				CenPersonaAdm admPers = new CenPersonaAdm(this.getUserBean(request));
				String Where = " Where ";
				/*Where += CenPersonaBean.T_NOMBRETABLA+"."+ CenPersonaBean.C_IDPERSONA+" in (Select "+GenClientesTemporalBean.T_NOMBRETABLA+"."+GenClientesTemporalBean.C_IDPERSONA+
	                 																		" From "+GenClientesTemporalBean.T_NOMBRETABLA+
																							" Where "+GenClientesTemporalBean.T_NOMBRETABLA+"."+GenClientesTemporalBean.C_IDINSTITUCION+"="+idInstitucion+
																							" And "+GenClientesTemporalBean.T_NOMBRETABLA+"."+ GenClientesTemporalBean.C_CONTADOR+"="+contador+")"+
                        " And "+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+"(+)="+idInstitucion+
						" And "+CenColegiadoBean.T_NOMBRETABLA+"."+ CenColegiadoBean.C_IDPERSONA+"(+)="+CenPersonaBean.T_NOMBRETABLA+"."+ CenPersonaBean.C_IDPERSONA+
						" Order By NCOLEGIADO, APELLIDOS1, APELLIDOS2, NOMBRE";*/
				/**Queremos que la consulta nos saque todas las personas y no solo los colegiados**/
				Where += CenPersonaBean.T_NOMBRETABLA+"."+ CenPersonaBean.C_IDPERSONA+" =GEN_CLIENTESTEMPORAL.Idpersona"+
//			             " and GEN_CLIENTESTEMPORAL.IDINSTITUCION="+idInstitucion+
			             " and GEN_CLIENTESTEMPORAL.Contador="+contador+
			             " Order By NCOLEGIADO, APELLIDOS1, APELLIDOS2, NOMBRE";
				
			
			     Vector datosPobCli = admPers.selectTabla1(Where);
			     
			     tx.rollback();
			     
			     request.setAttribute("datosPobCli", datosPobCli);
			     request.setAttribute("mensaje", "false");
			
			     forward = "inicio";
			}
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,tx); 
		} 
		
		return forward;
	}
	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
		return null;	
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
		return null;
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
		return null;
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}

	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}

	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}
}
