/*
 * Created on 31-mar-2005
 *
 */
package com.siga.censo.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.censo.form.DatosFacturacionForm;
import com.siga.censo.form.FacturasClienteForm;
import com.siga.facturacion.form.BusquedaFacturaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 */
public class FacturasClienteAction extends MasterAction {
	
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */

	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
				
				String accion = miForm.getModo();

		  		// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					borrarPaginador(request, paginadorPenstania);
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				} 
				else if (accion.equalsIgnoreCase("ver")){
					mapDestino = ver(mapping, miForm, request, response);
					break;
				}
				if (accion.equalsIgnoreCase("abrirOtra")){
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirOLD(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		try {
			// Obtengo el UserBean y el identificador de la institucion
			String accion = (String)request.getParameter("accion");

			// Obtengo el identificador de persona, la accion y el identificador de institucion del cliente
			Long idPersona = new Long(request.getParameter("idPersona"));
			Integer idInstitucionPersona =  new Integer(request.getParameter("idInstitucion"));
			Integer idInstitucion = this.getIDInstitucion(request);

			if (accion.equals("nuevo") || (idPersona == null)) {
				return "clienteNoExiste";
			}
			
			// Obtengo la informacion relacionada con ls facturas
			BusquedaFacturaForm bFacturaForm = new BusquedaFacturaForm ();
			bFacturaForm.setBuscarIdPersona(idPersona);
			FacFacturaAdm admFactura = new FacFacturaAdm(this.getUserBean(request));
			Vector vFacturas = admFactura.getFacturasClientePeriodo(idInstitucion, idPersona, new Integer(366));
			request.setAttribute("facturas", vFacturas);

			// Obtengo los datos del cliente
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
			String numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			String nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));		
			//////////////////////
			
			// Paso de parametros empleando request
//			request.setAttribute("idPersona", idPersona);
//			request.setAttribute("idInstitucion", idInstitucion);
//			request.setAttribute("idInstitucionPersona", idInstitucionPersona);
			request.setAttribute("accion", accion);
			request.setAttribute("nombre", nombre);
			request.setAttribute("numero", numero);
		} 
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null); 
		}
		
		return "inicio";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		try 
		{		
			// Obtengo valores del formulario y los estructuro
			FacturasClienteForm form = (FacturasClienteForm) formulario;
			Vector vOcultos = (Vector)form.getDatosTablaOcultos(0);
			String idInstitucion = (String)vOcultos.get(0); 
			String idFactura = (String)vOcultos.get(1); 
			Double total = new Double((String)vOcultos.get(2));
			Double totalPagado = new Double((String)vOcultos.get(3));
			String accion = (String)vOcultos.get(4);
			String idPersona = (String)vOcultos.get(5);

			if (!accion.equalsIgnoreCase("ver")) {
				if (total.doubleValue() == totalPagado.doubleValue()) {
					accion = "ver";
				}
				else accion = "editar";
			}
			
			Hashtable datosFac = new Hashtable();
			UtilidadesHash.set(datosFac,"accion", accion);
			UtilidadesHash.set(datosFac,"idFactura", idFactura);
			UtilidadesHash.set(datosFac,"idInstitucion", idInstitucion);
			UtilidadesHash.set(datosFac,"idPersona", idPersona);

			// Paso de parametros a las pestanhas
			request.setAttribute("datosFacturas", datosFac);		
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}

		return "ver";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		
		String accion = request.getParameter("accion");
		Long idPersona = new Long(request.getParameter("idPersona"));
		Integer idInstitucion = this.getIDInstitucion(request);				
						
		request.getSession().setAttribute("IDPERSONA",idPersona);
		request.getSession().setAttribute("IDINSTITUCION",idInstitucion);
		request.getSession().setAttribute("MODO",accion);
		
		idPersona = (Long) request.getAttribute("idPersona");
		if (idPersona == null){
			idPersona = (Long) request.getSession().getAttribute("idPersona");
			if (idPersona == null){
				idPersona = new Long (request.getParameter("idPersona"));
			}
		}
		
		idInstitucion = (Integer) request.getAttribute("idInstitucion");
		if(idInstitucion == null){
			idInstitucion = (Integer) request.getSession().getAttribute("idInstitucion");
			if(idInstitucion == null){
				idInstitucion = new Integer(request.getParameter("idInstitucion"));	
			
			}
		
		}
				
		
		// Paginador ->
		request.setAttribute(ClsConstants.PARAM_PAGINACION, paginadorPenstania);
		//DatosFacturacionForm miform = (DatosFacturacionForm) formulario;
		FacturasClienteForm miform = (FacturasClienteForm) formulario;
		FacFacturaAdm facturasAdm = new FacFacturaAdm(
				this.getUserBean(request));
		String sBajaLogica = miform.getIncluirRegistrosConBajaLogica();
		if (sBajaLogica == null){
			sBajaLogica = request.getParameter("bIncluirRegistrosConBajaLogica");
		}
		boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(sBajaLogica);
		
		try {
			HashMap databackup = getPaginador(request, paginadorPenstania);
			 Integer anyosMostrados=null;
			if (databackup != null) {
				
				PaginadorBind paginador = (PaginadorBind) databackup
						.get("paginador");
				Vector datos = new Vector();
				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = (String) request.getParameter("pagina");	
				
				
				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
							// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador
								.getPaginaActual()));
						
					}
					//String idPersona = (String) request.getSession().getAttribute("IDPERSONA");
					//Miramos si es la primera vez que accede a esta pagina, ya que si es asi hay
					//que actualizar los datos pesados ,PRECIO_SERVICIO,SERVICIO_ANTICIPADO y ESTADOPAGO. 
					//Para ello miramos si existe el dato de estadoPago en el primer registro
					//(no miramos) la fecha efectiva ya que puede ser nula
					Row fila = (Row)datos.get(0);
					Hashtable registro = (Hashtable) fila.getRow();
					
				}
				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {
				//String idPersona = request.getParameter("idPersona");
				//String idInstitucion = request.getParameter("idInstitucion");
				
				
				Hashtable criterios = new Hashtable();
				criterios.put(FacFacturaBean.C_IDPERSONA, miform
						.getIdPersona());
				criterios.put(PysPeticionCompraSuscripcionBean.C_TIPOPETICION,
						ClsConstants.TIPO_PETICION_COMPRA_ALTA);
				if(!bIncluirRegistrosConBajaLogica){
				  anyosMostrados=new Integer(730);
				}
				request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);
				
								
				databackup = new HashMap();

				
				//PaginadorBind paginador = productosAdm.getProductosSolicitadosPaginador(criterios,
					//			new Integer(miform.getIdInstitucion()));
				
				PaginadorBind paginador = facturasAdm.getFacturasClientePeriodoPaginador(idInstitucion, idPersona, anyosMostrados);
				// Paginador paginador = new Paginador(sql);
				int totalRegistros = paginador.getNumeroTotalRegistros();
				if (totalRegistros == 0) {
					paginador = null;
				}
				//String idInstitucion = (String) request.getAttribute("idInstitucionPestanha");
				databackup.put("paginador", paginador);
				if (paginador != null) {
					Vector datos = paginador.obtenerPagina(1);
					//datos = actualizarFacturasPaginados(serviciosAdm,new PysProductosSolicitadosAdm(
					//		this.getUserBean(request)),idPersona,this.getUserBean(request),datos);
					databackup.put("datos", datos);
					setPaginador(request, paginadorPenstania, databackup);
				}

			}
			
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			String nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona,idInstitucion);
			String numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
		
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			Hashtable datosColegiado = new Hashtable();
			datosColegiado.put("NOMBRECOLEGIADO",nombre);
			datosColegiado.put("NUMEROCOLEGIADO",numero);
			request.setAttribute("nombre", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("IDPERSONA",idPersona);
			request.setAttribute("IDINSTITUCION",idInstitucion);
			request.setAttribute("accion", accion);
			request.getSession().setAttribute("DATOSCOLEGIADO", datosColegiado);
			request.getSession().setAttribute("accion", accion);
			request.getSession().setAttribute("nombre", nombre);
			request.getSession().setAttribute("numero", numero);
			request.getSession().setAttribute("bIncluirRegistrosConBajaLogica",new Boolean(bIncluirRegistrosConBajaLogica).toString());
			
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}

		return "inicio";
	}
	
	
	public Hashtable completarHashSalida(Hashtable htSalida, Vector vParcial){
		
		if (vParcial!=null && vParcial.size()>0) {
			Hashtable registro = (Hashtable) vParcial.get(0);
			htSalida.putAll(registro);
		}
	
		return htSalida;
		
		
	}
	
	
}
