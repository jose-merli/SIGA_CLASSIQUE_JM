/*
 * Created on Mar 21, 2005
 * @author emilio.grau
 * 
 */
package com.siga.informes.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.paginadores.PaginadorCaseSensitive;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.form.MantenimientoInformesForm;

/**
 * Action para la consulta de morosos.
 */
public class InformeFacturacionColegiadoAction extends MasterAction {
	final String[] clavesBusqueda={"IDFACTURACION","IDPERSONA",};
	/**
	 * Funcion que atiende a las peticiones. Segun el valor del parametro modo
	 * del formulario ejecuta distintas acciones
	 * 
	 * @param mapping -
	 *            Mapeo de los struts
	 * @param formulario -
	 *            Action Form asociado a este Action
	 * @param request -
	 *            objeto llamada HTTP
	 * @param response -
	 *            objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception SIGAException
	 *                En cualquier caso de error
	 */
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();

			// La primera vez que se carga el formulario
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
				this.limpiarFormulario(miForm);
				mapDestino = abrir(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("buscarInicio")){
				miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				mapDestino = buscarPor(mapping, miForm, request, response); 
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				// mapDestino = "exception";
				if (miForm.getModal().equalsIgnoreCase("TRUE")) {
					request.setAttribute("exceptionTarget", "parent.modal");
				}

				// throw new SIGAException("El ActionMapping no puede ser
				// nulo");
				throw new ClsExceptions("El ActionMapping no puede ser nulo","", "0", "GEN00", "15");
			}

		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.facturacionSJCS" }); // o el recurso
																// del modulo
																// que sea
		}
		return mapping.findForward(mapDestino);
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {

		UsrBean user = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
		
		MantenimientoInformesForm form = (MantenimientoInformesForm) formulario;
		form.setIdInstitucion(user.getLocation());

		return "inicio";
	}
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "resultado";
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();
			String facturaciones = "";

			// casting del formulario
			MantenimientoInformesForm miFormulario = (MantenimientoInformesForm) formulario;
			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null && !miFormulario.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {					
						clavesRegSeleccinados = alRegistros;
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null &&!isSeleccionarTodos){ 
				PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)databackup.get("paginador");
				//Si no es la primera llamada, obtengo la p�gina del request y la busco con el paginador
				
				Vector datos=new Vector();
				if (paginador!=null){
					String pagina = (String)request.getParameter("pagina");
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	

				databackup=new HashMap();
				 			
				PaginadorCaseSensitive resultado = null;
				Vector datos = null;
				if(!miFormulario.getIdFacturacionFinal().equals("")){
					facturaciones = EjecucionPLs.ejecutarFuncFacturacionesIntervalo(idInstitucion, miFormulario.getIdFacturacion(), miFormulario.getIdFacturacionFinal());
				}else{
					facturaciones = miFormulario.getIdFacturacion();
				}
				
				FcsFacturacionJGAdm factAdm = new FcsFacturacionJGAdm(user);
				resultado = factAdm.getPaginadorDetalleFacturacion(miFormulario,idInstitucion, facturaciones,miFormulario.getLetrado());

				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)factAdm.selectGenerico(resultado.getQueryInicio()));
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
						datos = resultado.obtenerPagina(Integer.parseInt(miFormulario.getSeleccionarTodos()));
						miFormulario.setSeleccionarTodos("");
						
					}else{
						miFormulario.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
					databackup.put("datos",datos);
					
				}else{
					miFormulario.setRegistrosSeleccionados(new ArrayList());
				} 
				miFormulario.setDatosPaginador(databackup);
			}

		}catch (ClsExceptions e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
		}
		return destino;
	}
	
	private void limpiarFormulario(MasterForm formulario){
		MantenimientoInformesForm form = (MantenimientoInformesForm) formulario;
		form.setLetrado("");
		form.setInteresadoApellido1("");
		form.setInteresadoApellido2("");
		form.setInteresadoNif("");
		form.setInteresadoNombre("");
	}
}
