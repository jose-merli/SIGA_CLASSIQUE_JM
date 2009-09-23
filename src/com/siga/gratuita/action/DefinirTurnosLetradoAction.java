package com.siga.gratuita.action;

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
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysProductosSolicitadosAdm;
import com.siga.beans.PysProductosSolicitadosBean;
import com.siga.beans.PysServiciosSolicitadosBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.censo.form.DatosFacturacionForm;
import com.siga.censo.form.FacturasClienteForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirTurnosLetradoForm;


/**
 * @author ruben.fernandez
 */

public class DefinirTurnosLetradoAction extends MasterAction {
	
	public ActionForward executeInternal (ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		String forward="";
		String numero = "";
		String nombre = "";
		CenColegiadoBean datosColegiales;	

		try{
			//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
			if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
				try {
					// Preparo para obtener la informacion del colegiado:
					Long idPers = new Long(request.getParameter("idPersonaPestanha"));
					Integer idInstPers = new Integer(request.getParameter("idInstitucionPestanha"));
					CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
		
					// Obtengo la informacion del colegiado:
					nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
					datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
					numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
				} catch (Exception e1){
					try {
						Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
						nombre = (String)datosColegiado.get("NOMBRECOLEGIADO");
						numero = (String)datosColegiado.get("NUMEROCOLEGIADO");
					}catch(Exception e2){
						nombre = "";
						numero = "";
					}
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			Hashtable datosColegiado = new Hashtable();
			datosColegiado.put("NOMBRECOLEGIADO",nombre);
			datosColegiado.put("NUMEROCOLEGIADO",numero);
			request.getSession().setAttribute("DATOSCOLEGIADO", datosColegiado);
		
			MasterForm miForm = (MasterForm)formulario;
			//forward =this.abrir(mapping,miForm,request,response);
			String accion = miForm.getModo();
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir") || accion.equalsIgnoreCase("abrirTurnosLimpiar")){
				borrarPaginador(request, paginadorPenstania);
				forward =this.abrirTurnosPaginados(mapping,miForm,request,response);
			}else if (accion.equalsIgnoreCase("abrirTurnosPaginados")){
				forward =this.abrirTurnosPaginados(mapping,miForm,request,response);
			} 
			
			return mapping.findForward(forward);
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
		
	}
	
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		try {
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
			
			ScsTurnoAdm turno = new ScsTurnoAdm(usr);

			Vector vTurno = turno.getTurnosLetrado(""+this.getIDInstitucion(request), idPersona);
			request.getSession().setAttribute("entrada","2");	
			request.setAttribute("resultado",vTurno);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en la select");
		}
		return "listado";
	}
	
	
	/** 
	 *  Funcion que atiende la accion abrir cargando un paginador.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirTurnosPaginados(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		String accion = request.getParameter("accion");
		Long idPersona = new Long ((String) request.getSession().getAttribute("idPersonaTurno"));
		Integer idInstitucion = this.getIDInstitucion(request);				
						
		request.getSession().setAttribute("IDPERSONA",idPersona);
		request.getSession().setAttribute("IDINSTITUCION",idInstitucion);
		request.getSession().setAttribute("MODO",accion);
		
		//idPersona = new Long (request.getParameter("idPersona"));
		//idInstitucion = new Integer(request.getParameter("idInstitucion"));	
		
		// Paginador ->
		request.setAttribute(ClsConstants.PARAM_PAGINACION, paginadorPenstania);
		//DatosFacturacionForm miform = (DatosFacturacionForm) formulario;
		DefinirTurnosLetradoForm miform = (DefinirTurnosLetradoForm) formulario;
		ScsTurnoAdm turnosAdm = new ScsTurnoAdm(this.getUserBean(request));
		
		try {
			HashMap databackup = getPaginador(request, paginadorPenstania);
			boolean mostrarHistorico = false;
			boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean((String)request.getSession().getAttribute("bIncluirRegistrosConBajaLogica"));
			if (databackup != null) {
				
				PaginadorBind paginador = (PaginadorBind) databackup.get("paginador");
				Vector datos = new Vector();
				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = (String) request.getParameter("pagina");	
				
				
				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
							// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
						
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
				String sBajaLogica = miform.getIncluirRegistrosConBajaLogica();
				if (sBajaLogica == null){
					sBajaLogica = request.getParameter("bIncluirRegistrosConBajaLogica");
				}
				bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(sBajaLogica);
				
				Hashtable criterios = new Hashtable();
				criterios.put("IDPERSONA", miform.getIdPersona());
				
				databackup = new HashMap();
				
				PaginadorBind paginador = turnosAdm.getTurnosClientePaginador(idInstitucion, idPersona, bIncluirRegistrosConBajaLogica);
				
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
			request.getSession().setAttribute("entrada","2");
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

		return "listado";
	}

}