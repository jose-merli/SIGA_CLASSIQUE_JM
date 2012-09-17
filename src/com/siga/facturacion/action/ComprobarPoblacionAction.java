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
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenPoblacionesBean;
import com.siga.general.ComboAutoComplete;
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

			if ( accion.equalsIgnoreCase("getAjaxPoblaciones")){
				getAjaxPoblaciones (mapping, miForm, request, response);
				return null;
				
			} else if ( accion.equalsIgnoreCase("getAjaxPoblacionesFiltro")){
				getAjaxPoblacionesFiltro (mapping, miForm, request, response);
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
	
	protected void getAjaxPoblacionesFiltro (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws Exception {
		UsrBean usuario=this.getUserBean(request);
		String campoTabla=CenPoblacionesBean.T_NOMBRETABLA;
		String campoPrioridad=CenPoblacionesBean.C_PRIORIDAD;
		String campoIdentificador=CenPoblacionesBean.C_IDPOBLACION;
		String campoIdentificadorPadre=CenPoblacionesBean.C_IDPROVINCIA;
		String campoNombre=CenPoblacionesBean.C_NOMBRE;			
		String guiones="---";
		String valorFiltro=request.getParameter("poblacion_input"); //Recogemos el parametro enviado por ajax
		String valorIdentificadorPadre=request.getParameter("provincia"); //Recogemos el parametro enviado por ajax
		Integer numeroMaximoOpciones=1000;
		if (valorFiltro==null||valorFiltro.trim().equalsIgnoreCase(""))	valorFiltro="*";     		    
	    		
	    String fromSql=" FROM " + campoTabla+
	    	" WHERE REGEXP_LIKE("+campoTabla+"."+campoNombre+", '"+valorFiltro+"') "+
	    	" AND "+campoTabla+"."+campoIdentificadorPadre+"='"+valorIdentificadorPadre+"' ";
	        	    	    
	    String sql = " SELECT "+campoTabla+"."+campoIdentificador+", "+
		    	campoTabla+"."+campoNombre+", "+
		    	campoTabla+"."+campoPrioridad+
		    	fromSql;	   
	    String countSql="(SELECT COUNT(*) AS CONTADOR "+fromSql+") ";
	    
    	sql+=" AND ("+numeroMaximoOpciones+">="+countSql+" OR "+campoTabla+"."+campoPrioridad+" IS NOT NULL) "+
    		" ORDER BY "+campoTabla+"."+campoPrioridad+" ASC, "+
	    	campoTabla+"."+campoNombre+" ASC ";
		    
    	RowsContainer rc = null;
		rc = new RowsContainer();

        rc.findNLS(sql);    

        List<CenPoblacionesBean> poblaciones = new ArrayList<CenPoblacionesBean>();		
			
		if (rc!=null) {
			CenPoblacionesBean poblacionBean = new CenPoblacionesBean();
			poblacionBean.setNombre(UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar"));
			poblacionBean.setIdPoblacion("");
			poblaciones.add(poblacionBean);
			
			Boolean bGuiones=true;
			Boolean bPrioridad = false;
			
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);
				Hashtable registro = (Hashtable)fila.getRow(); 
				
				if (registro != null) { 
					Integer prioridad = UtilidadesHash.getInteger(registro, campoPrioridad);
					
					if(bGuiones&&bPrioridad&&prioridad==null) {
						poblacionBean = new CenPoblacionesBean();
						poblacionBean.setNombre(guiones);
						poblacionBean.setIdPoblacion("");
						poblaciones.add(poblacionBean);
						bGuiones=false;
					}
					
					if (!bPrioridad)				 
 						bPrioridad = (prioridad!=null);  	
					
					poblacionBean = new CenPoblacionesBean();
					poblacionBean.setNombre(UtilidadesHash.getString(registro, campoNombre));
					poblacionBean.setIdPoblacion(UtilidadesHash.getString(registro, campoIdentificador));
					poblaciones.add(poblacionBean);
				}
			}
		}		
	    respuestaAjax(new AjaxCollectionXmlBuilder<CenPoblacionesBean>(), poblaciones, response);
	}	
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws ClsExceptions
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void getAjaxPoblaciones (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {		
		
		String idProvincia = request.getParameter("codigoPadre");
		if (idProvincia==null||idProvincia.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador de la provincia");				
		
		String filtro = request.getParameter("valorFiltro");
		String guiones = request.getParameter("valorGuiones");
		Integer numOpciones = Integer.parseInt(request.getParameter("numeroMaximoOpciones"));
		
		ComboAutoComplete.getAjaxPoblaciones(
			response, this.getUserBean(request), 
			CenPoblacionesBean.T_NOMBRETABLA, CenPoblacionesBean.C_PRIORIDAD, CenPoblacionesBean.C_IDPOBLACION, 
			CenPoblacionesBean.C_IDPROVINCIA, CenPoblacionesBean.C_NOMBRE, 
			guiones, filtro, idProvincia, numOpciones);		
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
