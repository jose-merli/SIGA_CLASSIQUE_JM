/*
 * Created on 07-mar-2005
 *
 */
package com.siga.facturacion.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.facturacion.form.BusquedaFacturaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 */
public class BusquedaFacturaAction extends MasterAction {

	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		try {
			
			// Si no vengo del boton volver reseteo los formulario
			BusquedaFacturaForm miFormSession = (BusquedaFacturaForm)request.getSession().getAttribute("BusquedaFacturaForm");
			BusquedaFacturaForm miForm = (BusquedaFacturaForm)formulario;
			String buscar = request.getParameter("botonVolver_Buscar");
			if (buscar==null) buscar = request.getParameter("buscar"); // Si vuelve de envios
			if (miFormSession != null) {
				if (buscar == null){
					// Reseteo los formularios
					miFormSession.reset(mapping,request);
					miForm.reset(mapping,request);
				}
			
				// Pasamos los datos del sigaCombo y el nombre del cliente
				Vector v = miFormSession.getDatosTablaVisibles(0);
				if (v != null) {
					
					if (!miFormSession.getBuscarIdPersona().toString().equals("-1")) {
						CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
						String nombre = personaAdm.obtenerNombreApellidos(miFormSession.getBuscarIdPersona().toString());
						request.setAttribute("nombreClienteAnteriorBusqueda", nombre);
						request.setAttribute("idPersonaAnteriorBusqueda", miFormSession.getBuscarIdPersona());
					}
				}
				request.setAttribute("idSereFacturacionAnteriorBusqueda", miFormSession.getBuscarIdSerieFacturacion());
				request.setAttribute("deudor", miFormSession.getDeudor());
				request.setAttribute("idEstado", miFormSession.getBuscarIdEstado());
			}
			request.setAttribute("botonVolver_Buscar", buscar);
			request.setAttribute("idInstitucion", this.getIDInstitucion(request));
			
			// Establezco el valor para el boton volver 
			request.getSession().setAttribute("CenBusquedaClientesTipo","BF"); // busqueda factura
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, null); 
		}
		return super.abrir(mapping, formulario, request, response);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			BusquedaFacturaForm miForm = (BusquedaFacturaForm) formulario;
			FacFacturaAdm admFactura = new FacFacturaAdm(this.getUserBean(request));
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			
			 HashMap databackup=new HashMap();
				
				 	if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
					     PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind)databackup.get("paginador");
					     Vector datos=new Vector();
					
					
					//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
					String pagina = (String)request.getParameter("pagina");
					
					 
					
				 if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				 }	
					
					
				 datos = actualizarFacturaciones(admFactura,abonoAdm, this.getIDInstitucion(request),this.getUserBean(request).getLanguage(), datos);
					databackup.put("paginador",paginador);
					databackup.put("datos",datos);
					
						
					
					
			  }else{	
					
			  	    databackup=new HashMap();
					
					//obtengo datos de la consulta 			
			  	  PaginadorCaseSensitiveBind resultado = null;
				Vector datos = null;
				
			resultado = admFactura.getFacturas (miForm, this.getIDInstitucion(request),this.getLenguaje(request));
			databackup.put("paginador",resultado);
			if (resultado!=null){ 
			   datos = resultado.obtenerPagina(1);
			   datos = actualizarFacturaciones(admFactura, abonoAdm, this.getIDInstitucion(request), this.getUserBean(request).getLanguage(), datos);
			   databackup.put("datos",datos);
			   request.getSession().setAttribute("DATAPAGINADOR",databackup);
			} 
		  }
			//request.setAttribute("facturas", vFacturas);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, null); 
		}
		return "buscarPor";
	}
	
	private Vector actualizarFacturaciones(FacFacturaAdm admFac,FacAbonoAdm admAbono ,Integer idInstitucion,String idioma,Vector datos) throws SIGAException,ClsExceptions{
		
		
		Hashtable htAbono = new Hashtable();
		htAbono.put(FacAbonoBean.C_IDINSTITUCION, idInstitucion);
		for (int i=0;i<datos.size();i++) 
		{
			Row fila = (Row)datos.get(i);
			
			Hashtable registro = (Hashtable) fila.getRow();

			String idFactura = UtilidadesHash.getString(registro, FacFacturaBean.C_IDFACTURA);
			String idEstado = UtilidadesHash.getString(registro, FacFacturaBean.C_ESTADO);
			String estado = UtilidadesHash.getString(registro, "DESC_ESTADO_ORIGINAL");
			htAbono.put(FacAbonoBean.C_IDFACTURA,idFactura);
			
			//Cogemos el total de la factura
			//Double total = admFac.getTotalFactura(idInstitucion,idFactura);
			Double total = new Double(UtilidadesHash.getString(registro, "IMPTOTAL"));
			//Miramos la descripcion del estado de la factura
			String sEstado =  UtilidadesString.getMensajeIdioma(idioma, estado);
			if(idEstado.equals("8")){
				
				Vector vAbonos = admAbono.select(htAbono);
				
				// JPT: Las facturas devueltas por comision, generan facturas anuladas sin abono, porque se crea una factura nueva
				if (vAbonos!=null && vAbonos.size()==1) {
					//Es unique key por lo que habra solo un registro
					FacAbonoBean beanAbono = (FacAbonoBean)vAbonos.get(0);
				 
					sEstado += " ("+beanAbono.getNumeroAbono()+")";
				}
			}
			
			registro.put("DESCRIPCION_ESTADO", sEstado);
			registro.put("TOTAL", ""+total);
		}
		
		return datos;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		try {
			BusquedaFacturaForm miForm = (BusquedaFacturaForm) formulario;
	
			Vector fila = miForm.getDatosTablaOcultos(0);
			String idInstitucion = (String)fila.get(0);
			String idFactura = (String)fila.get(1);
			String idPersona = (String)fila.get(2);

			Hashtable datosFac = new Hashtable();
			
			datosFac.put("accion", "editar");
			datosFac.put("idFactura", idFactura);
			datosFac.put("idInstitucion", idInstitucion);
			datosFac.put("idPersona", idPersona);
			datosFac.put("botonVolver", "SI");
	
			request.setAttribute("datosFacturas", datosFac);		
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null);
		}
		return "administrarPestanas";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			BusquedaFacturaForm miForm = (BusquedaFacturaForm) formulario;
	
			Vector fila = miForm.getDatosTablaOcultos(0);
			String idInstitucion = (String)fila.get(0);
			String idFactura = (String)fila.get(1);
			String idPersona = (String)fila.get(2);

			Hashtable datosFac = new Hashtable();
			
			UtilidadesHash.set(datosFac,"accion", "ver");
			UtilidadesHash.set(datosFac,"idFactura", idFactura);
			UtilidadesHash.set(datosFac,"idInstitucion", idInstitucion);
			UtilidadesHash.set(datosFac,"idPersona", idPersona);
			UtilidadesHash.set(datosFac,"botonVolver", "SI");
	
			request.setAttribute("datosFacturas", datosFac);		
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null);
		}
		return "administrarPestanas";
	}
}
