/*
 * Created on Mar 21, 2005
 * @author emilio.grau
 * 
 */
package com.siga.informes.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.administracion.form.InformeForm;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.FcsPagoColegiadoBean;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformePersonalizable;
import com.siga.informes.form.MantenimientoInformesForm;

/**
 * Action para la consulta de morosos.
 */
public class InformePagosColegiadoAction extends MasterAction {
	final String[] clavesBusqueda={FcsPagoColegiadoBean.C_IDINSTITUCION,"IDPERSONASJCS","IDPAGOS"
			};
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
			}else if (accion.equalsIgnoreCase("imprimir")){
				mapDestino = imprimir(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("detallePago")) {
				mapDestino = detallePago(mapping, miForm, request, response);
			}/* else if (accion.equalsIgnoreCase("download")) {
				InformeColegiadosPagos inf = new InformeColegiadosPagos();
				mapDestino = inf.generarColegiadoPago(mapping, formulario,
						request, response);
				// Controlamos si debemos avisar del error:
				if (mapDestino.equals("error"))
					mapDestino = exitoModalSinRefresco(
							"gratuita.retenciones.noResultados", request);
				return mapping.findForward(mapDestino);
			} */else {
				return super.executeInternal(mapping, formulario, request,
						response);
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

		UsrBean user = ((UsrBean) request.getSession()
				.getAttribute(("USRBEAN")));
		
		String parametrosComboPago[] = new String[2];
		parametrosComboPago[0] = user.getLocation();
		parametrosComboPago[1] = ClsConstants.ESTADO_PAGO_EJECUTADO;

		MantenimientoInformesForm form = (MantenimientoInformesForm) formulario;
		form.setIdInstitucion(user.getLocation());
		form.setParametrosComboPago(parametrosComboPago);
		form.setParametrosComboPagoFin(parametrosComboPago);

		return "inicio";
	}
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "resultado";
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();

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
				PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind)databackup.get("paginador");
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
				 			
				PaginadorCaseSensitiveBind resultado = null;
				Vector datos = null;
				FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(user);
				
				
				String idPagos = EjecucionPLs.ejecutarFuncPagosIntervalo(idInstitucion, miFormulario.getIdPago(),miFormulario.getIdPagoFinal());
				
				resultado = pagosAdm.getPaginadorDetallePago(miFormulario,idPagos, idInstitucion, user.getLanguage());
				
				
				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)pagosAdm.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
						datos = resultado.obtenerPagina(Integer.parseInt(miFormulario.getSeleccionarTodos()));
						miFormulario.setSeleccionarTodos("");
						
					}else{
//					
						miFormulario.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
					databackup.put("datos",datos);
					
				}else{
					miFormulario.setRegistrosSeleccionados(new ArrayList());
				} 
				miFormulario.setDatosPaginador(databackup);
			}

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
		}
		return destino;
	}
	protected String imprimir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// casting del formulario
			MantenimientoInformesForm miFormulario = (MantenimientoInformesForm) formulario;
			
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null){ 
				PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind)databackup.get("paginador");
				//Si no es la primera llamada, obtengo la p�gina del request y la busco con el paginador
				AdmInformeAdm adm = new AdmInformeAdm(user);
				Vector datos = adm.selectGenericoBind(paginador.getQueryOriginal(), paginador.getCodigos());
				//La linea de debajo serviria si no se necesita Orden
				//String[] cabeceras = UtilidadesHash.getClaves(datosHashtable);
				String[] cabeceras = new String[9];
				cabeceras[0] = "NCOLEGIADO";
				cabeceras[1] = "NOMBRE";
				cabeceras[2] = "NOMBREPAGO";
				cabeceras[3] = "TOTALIMPORTESJCS";
				cabeceras[4] = "IMPORTETOTALMOVIMIENTOS";
				cabeceras[5] = "TOTALIMPORTEBRUTO";//TOTALIMPORTESJCS+IMPORTETOTALMOVIMIENTOS
				cabeceras[6] = "TOTALIMPORTEIRPF";
				cabeceras[7] = "IMPORTETOTALRETENCIONES";
							
				cabeceras[8] = "IMPORTETOTAL";//IMPORTEBRUTO + TOTALIMPORTEIRPF + IMPORTETOTALRETENCIONES
				//Como alguno de los metodos set, formatoCampo o redondea esta mal no es posible
				//que UtilidadesHash.set(filaHashtable, "TOTALIMPORTESJCS", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("TOTALIMPORTESJCS"), 2))); 
				//nos guarde la direccion de memoria correcta
				for (int i = 0; i < datos.size(); i++) {
					Hashtable filaHashtable = (Hashtable) datos.elementAt(i);
					//Hashtable filaHashtable = fila.getRow();
					
					UtilidadesHash.set(filaHashtable, "TOTALIMPORTESJCS", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("TOTALIMPORTESJCS"), 2)));
					UtilidadesHash.set(filaHashtable, "IMPORTETOTALMOVIMIENTOS", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("IMPORTETOTALMOVIMIENTOS"), 2)));
					UtilidadesHash.set(filaHashtable, "TOTALIMPORTEBRUTO", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("TOTALIMPORTEBRUTO"), 2)));
					UtilidadesHash.set(filaHashtable, "TOTALIMPORTEIRPF", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("TOTALIMPORTEIRPF"), 2)));
					UtilidadesHash.set(filaHashtable, "IMPORTETOTALRETENCIONES", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("IMPORTETOTALRETENCIONES"), 2)));
					UtilidadesHash.set(filaHashtable, "IMPORTETOTAL", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("IMPORTETOTAL"), 2)));
					
					
				}

				InformePersonalizable informePersonalizable = new InformePersonalizable();
				List<InformeForm> informesForms = new ArrayList<InformeForm>();
				InformeForm informeForm = new InformeForm();
				informeForm.setDirectorio("");
				informeForm.setIdInstitucion(user.getLocation());
				informeForm.setNombreSalida("excelPago");
				informeForm.setAlias("");
				informeForm.setTipoFormato(AdmInformeBean.TIPOFORMATO_EXCEL);
				informesForms.add(informeForm);
				
				

				File ficheroSalida = informePersonalizable.getFicheroGenerado(informesForms, datos,cabeceras, user);
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");
				request.setAttribute("generacionOK","OK");
				
				return "descarga";	
				
				
			}

		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
		}
		return exitoRefresco("messages.noRecordFound",request);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping,
	 *      com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected String detallePago(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		try {
			MantenimientoInformesForm form = (MantenimientoInformesForm) formulario;
			UsrBean user = ((UsrBean) request.getSession().getAttribute(
					("USRBEAN")));
			String idInstitucion = user.getLocation();
			form.setIdInstitucion(idInstitucion);
			form.setLetrado(form.getIdPersona());
			FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(user);

			Vector datos = pagosAdm.getDetallePago(form, user.getLanguage());
			Hashtable filaDetalle = (Hashtable) datos.get(0);
			String importeSjcs = UtilidadesNumero.redondea(UtilidadesHash.getString(filaDetalle,
				"TOTALIMPORTESJCS"),2);
				String importeRetencion = UtilidadesNumero.redondea(UtilidadesHash.getString(filaDetalle,
			"IMPORTETOTALRETENCIONES"),2);
					String importeMvtos = UtilidadesNumero.redondea(UtilidadesHash.getString(filaDetalle,
			"IMPORTETOTALMOVIMIENTOS"),2);
					String aux = UtilidadesHash.getString(filaDetalle,"TOTALIMPORTEIRPF");
					double dAux = Double.parseDouble(aux);
					String importeIRPF = String.valueOf(-1 * UtilidadesNumero.redondea(dAux, 2));
					UtilidadesHash.set(filaDetalle,"TOTALIMPORTEIRPF",UtilidadesNumero.formatoCampo(importeIRPF));
					
		//Importe de Retenciones y Total SJCS:
	
		//String importeTotal = UtilidadesNumero.redondea((new Float(impTotal)).toString(),2);
			float totalBrutos = Float.parseFloat(importeSjcs)
					+ Float.parseFloat(importeMvtos);
			float totalRetenido = Float.parseFloat(importeRetencion)
					+ Float.parseFloat(importeIRPF);
			float neto = totalBrutos + totalRetenido;
			
			UtilidadesHash.set(filaDetalle,"BRUTO",UtilidadesNumero.formatoCampo(totalBrutos));
			UtilidadesHash.set(filaDetalle,"TOTALRETENIDO",UtilidadesNumero.formatoCampo(totalRetenido));
			UtilidadesHash.set(filaDetalle,"NETO",UtilidadesNumero.formatoCampo(neto));
		
			

			
			request.setAttribute("detallePago", filaDetalle);
			
			// request.setAttribute("lineasComunicaciones", vComunicaciones);

		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.facturacion" });
		}

		return "detallePagoColegiado";
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
