/* VERSIONES:
 * david.sanchezp - 21/03/2005 - Creacion
 */

/**
 * Clase action para la visualizacion y mantenimiento de los datos de pago para una Institución.<br/>
 * Gestiona la edicion, consulta y mantenimiento de pagos.  
 */

package com.siga.facturacionSJCS.action;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ColegiadosPagosBean;
import com.siga.beans.CriteriosPagosBean;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacLineaAbonoAdm;
import com.siga.beans.FacLineaAbonoBean;
import com.siga.beans.FcsCobrosRetencionJudicialAdm;
import com.siga.beans.FcsEstadosFacturacionBean;
import com.siga.beans.FcsEstadosPagosBean;
import com.siga.beans.FcsFactGrupoFactHitoAdm;
import com.siga.beans.FcsFactGrupoFactHitoBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.beans.FcsMovimientosVariosAdm;
import com.siga.beans.FcsMovimientosVariosBean;
import com.siga.beans.FcsPagoColegiadoAdm;
import com.siga.beans.FcsPagoColegiadoBean;
import com.siga.beans.FcsPagoGrupoFactHitoAdm;
import com.siga.beans.FcsPagoGrupoFactHitoBean;
import com.siga.beans.FcsPagosEstadosPagosAdm;
import com.siga.beans.FcsPagosEstadosPagosBean;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacion.action.AbonosPagosAction;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.facturacionSJCS.form.DatosGeneralesPagoForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class DatosGeneralesPagoAction extends MasterAction {

	
	//Método interno para ejecutar el action del caso de uso.
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		String accion = (String)request.getParameter("accion");
		//String accion = miForm.getModo();
		try {
			if((accion != null)&&(accion.equalsIgnoreCase("nuevo"))){
				return mapping.findForward(this.nuevo(mapping, miForm, request, response));
			} else {
				if ((accion != null)&&((accion.equalsIgnoreCase("edicion"))||(accion.equalsIgnoreCase("consulta")))){
					return mapping.findForward(this.abrir(mapping, miForm, request, response));
				}
				else {
					if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("insertarPago")))
						return mapping.findForward(this.insertarPago(mapping, miForm, request, response));
					else {
						if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("modificarPago")))
								return mapping.findForward(this.modificarPago(mapping, miForm, request, response));
						else {
							if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("ejecutarPago")))
								return mapping.findForward(this.ejecutarPago(mapping, miForm, request, response));
							else {
								if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("cerrarPago")))
									return mapping.findForward(this.cerrarPago(mapping, miForm, request, response));
								else
									if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("mostrarColegiadosAPagar")))
										return mapping.findForward(this.mostrarColegiadosAPagar(mapping, miForm, request, response));
									else
										if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("cerrarPagoManual")))
											return mapping.findForward(this.cerrarPagoManual(mapping, miForm, request, response));
										else
											if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("insertarCriteriosPagos")))
												return mapping.findForward(this.insertarCriteriosPagos(mapping, miForm, request, response));
											else
												if (((String)miForm.getModo()!=null)&&(((String)miForm.getModo()).equalsIgnoreCase("abrirModal")))
													return mapping.findForward(this.abrirModal(mapping, miForm, request, response));
												else
													return super.executeInternal(mapping, formulario, request, response);
							}
						}
					}
				}	
			}
		}
		catch(SIGAException e){
			throw e;
		}
		catch(Exception e){
			return mapping.findForward("exception");
		}
	}
	
	/** 
	 *  Funcion que atiende la accion abrirAvanzada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 * Método que atiende la accion modificarPago. Modifica los valores de un pago. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificarPago(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(this.getUserBean(request));
		UsrBean usr;
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		String forward = "";
		UserTransaction tx = null;
		
		try	{
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			//obtiene el bean a actualizar de BD
			String where = " where "+FcsPagosJGBean.C_IDINSTITUCION+"="+ miform.getIdInstitucion()+" and "+FcsPagosJGBean.C_IDPAGOSJG+"="+ miform.getIdPagosJG() + " ";			
			FcsPagosJGBean pagosBean = (FcsPagosJGBean)pagosAdm.select(where).elementAt(0);
			pagosBean.setNombre(miform.getNombre());
			pagosBean.setAbreviatura(miform.getAbreviatura());
			
			//actualiza la BD
			tx.begin();
			pagosAdm.updateDirect(pagosBean);
			tx.commit();

			//Consulta el registro modificado tal cual esta en base de datos y lo almacena en sesion:
			Hashtable registroModificado = new Hashtable();
			registroModificado = ((FcsPagosJGBean)pagosAdm.select(where).elementAt(0)).getOriginalHash();
			request.getSession().setAttribute("DATABACKUP",registroModificado);

			request.setAttribute("modo","modificarPago");
			forward = exito("messages.updated.success",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		}
		return forward;
	}
	/** 
	 * Método que atiende la accion abrir. Atiende a las acciones del boton editar y consultar de la <br>
	 * pantalla inicial del caso de uso.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 S* @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm (this.getUserBean(request));
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		
		String forward = "inicio";	
		UsrBean usr;
		String modo=null, idEstadoFacturacion=null, idInstitucion=null, idFacturacion=null, idPagosJG=null, accion=null, nombreFacturacion=null;
		Hashtable registroOriginal = new Hashtable();
		FcsPagosJGBean pagosBean = new FcsPagosJGBean();
		String importeFacturado=null;
		
		try 
		{
			//Recuperamos el USRBEAN:
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			//Recogemos los parámetros de las pestanhas 
			idInstitucion = request.getParameter("idInstitucion");
			idPagosJG = request.getParameter("idPagosJG");
			accion = request.getParameter("accion");
			//Si estamos en abrir depues de haber insertado las pestanhas no nos pasa el modo
			if (accion==null || accion.equals("")){
				accion = "edicion";				
				modo = "modificarPago";
			} else {			
				if (accion.equals("edicion")) modo = "modificarPago";
				else if (accion.equals("consulta")) modo = "abrir";
			}
			//Creamos una clausula where que nos servirá para consultar por idPagosJG e idInstitucion
			String where = " where "+FcsPagosJGBean.C_IDINSTITUCION+"="+ idInstitucion +" and "+FcsPagosJGBean.C_IDPAGOSJG+"="+ idPagosJG + " ";

			//Traemos de base de datos la factura seleccionada, con los datos recogidos de la pestanha
			Vector registros = pagosAdm.select(where);
			pagosBean = (FcsPagosJGBean)registros.get(0);
			registroOriginal = pagosAdm.beanToHashTable(pagosBean);

			//Tratamiento de los importes:
			//IMPORTE REPARTIR:
			registroOriginal.put(FcsPagosJGBean.C_IMPORTEREPARTIR, UtilidadesString.tratarImporte(pagosBean.getImporteRepartir()));
			//IMPORTE PAGADO:
			registroOriginal.put(FcsPagosJGBean.C_IMPORTEPAGADO, UtilidadesString.tratarImporte(pagosBean.getImportePagado()));
			//Resto de importes:
			registroOriginal.put(FcsPagosJGBean.C_IMPORTEEJG, UtilidadesString.tratarImporte(pagosBean.getImporteEJG()));
			registroOriginal.put(FcsPagosJGBean.C_IMPORTESOJ, UtilidadesString.tratarImporte(pagosBean.getImporteSOJ()));
			registroOriginal.put(FcsPagosJGBean.C_IMPORTEOFICIO, UtilidadesString.tratarImporte(pagosBean.getImporteOficio()));
			registroOriginal.put(FcsPagosJGBean.C_IMPORTEGUARDIA, UtilidadesString.tratarImporte(pagosBean.getImporteGuardia()));
			registroOriginal.put(FcsPagosJGBean.C_PORCENTAJEEJG, UtilidadesString.tratarImporte(pagosBean.getPorcentajeEJG()));
			registroOriginal.put(FcsPagosJGBean.C_PORCENTAJESOJ, UtilidadesString.tratarImporte(pagosBean.getPorcentajeSOJ()));
			registroOriginal.put(FcsPagosJGBean.C_PORCENTAJEOFICIO, UtilidadesString.tratarImporte(pagosBean.getPorcentajeOficio()));
			registroOriginal.put(FcsPagosJGBean.C_PORCENTAJEGUARDIAS, UtilidadesString.tratarImporte(pagosBean.getPorcentajeGuardias()));
			registroOriginal.put(FcsPagosJGBean.C_IMPORTEMINIMO, UtilidadesString.tratarImporte(pagosBean.getImporteMinimo()));
			
			// Recupera los importes totales y pendientes de cada concepto del pago
			Hashtable hashConceptos = pagosAdm.getConceptosPendientesYTotal(new Integer(idInstitucion),pagosBean.getIdFacturacion());
			request.setAttribute("CONCEPTOS",hashConceptos);
			
			//Recuperamos el estado, la fecha y el id del estado del pago:
			Hashtable hash = pagosAdm.getEstadoPago(new Integer(idInstitucion),new Integer(idPagosJG));
			String nombreEstado = (String)hash.get(FcsEstadosPagosBean.C_DESCRIPCION);
			String fechaEstado = (String)hash.get(FcsPagosEstadosPagosBean.C_FECHAESTADO);
			String idEstadoPagosJG = (String)hash.get(FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG);
			
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
			
			//Consultamos el estado de la facturacion:
			idFacturacion = pagosBean.getIdFacturacion().toString();
			idInstitucion = pagosBean.getIdInstitucion().toString();
			Hashtable hashFacturacion = facturacionAdm.getEstadoFacturacion(idInstitucion, idFacturacion); 
			idEstadoFacturacion = (String)hashFacturacion.get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION);
			
			//Nombre de la facturacion:
			where = " WHERE "+FcsFacturacionJGBean.C_IDINSTITUCION+"="+pagosBean.getIdInstitucion().toString()+
					" AND "+FcsFacturacionJGBean.C_IDFACTURACION+"="+pagosBean.getIdFacturacion().toString();
			nombreFacturacion = ((FcsFacturacionJGBean)(facturacionAdm.select(where).get(0))).getNombre(); 
			//Importe Facturado:
			importeFacturado = ((FcsFacturacionJGBean)(facturacionAdm.select(where).get(0))).getImporteTotal().toString();
			
			//Actualizamos el criterio:
//			miform.setCriterioPago(pagosBean.getCriterioPago());
			miform.setCriterioPagoTurno(pagosBean.getCriterioPagoTurno());
			
			//Actualizamos el criterio de valor de facturacion (para saber si recuperamos los puntos de cuando 
			// hicimos la factura o del momento del pago):			
//			if (pagosBean.getValoresFacturacion()==null || pagosBean.getValoresFacturacion().equals(ClsConstants.DB_FALSE))
//				valoresFacturacion = null;//ClsConstants.DB_FALSE;
//			else
//				valoresFacturacion = ClsConstants.DB_TRUE;
//			miform.setValoresFacturacion(valoresFacturacion);

			//COnsultamos el Parámetro COBRO AUTOMATICO
			//para el caso de que se quiera Cerrar el Pago
			//y lo pasamos por la request
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String cobroAutomatico = (String)paramAdm.getValor(usr.getLocation(), ClsConstants.MODULO_FACTURACION_SJCS , "DEDUCIR_COBROS_AUTOMATICO", null);
			String automatico = (cobroAutomatico.equalsIgnoreCase(ClsConstants.DB_TRUE)?"si":"no");
			request.setAttribute("cobroAutomatico",automatico);
			
			//Pasamos en el request los siguientes datos:
			//BEAN PAGOSJG:
			request.setAttribute("PAGOSBEAN",pagosBean);
			//NOMBRE INSTITUCION:
			request.setAttribute("nombreInstitucion",nombreInstitucion);
			//DATOS DEL ESTADO:
			request.setAttribute("nombreEstado",nombreEstado);
			request.setAttribute("fechaEstado",fechaEstado);
			request.setAttribute("idEstadoPagosJG",idEstadoPagosJG);
			//DATOS DE LA FACTURACION:
			request.setAttribute("idEstadoFacturacion",idEstadoFacturacion);
			request.setAttribute("nombreFacturacion",nombreFacturacion);
			request.setAttribute("importeFacturado",importeFacturado);
			//FORMULARIO:
			request.setAttribute("formularioPagos",miform);

			//Importe pagado hasta ahora:
			request.setAttribute("importePendienteDePago", pagosAdm.getImportePendienteDePago(pagosBean.getIdInstitucion(), pagosBean.getIdFacturacion()));
			
			//Almaceno en sesion el bean para futuras modificaciones:
			request.getSession().setAttribute("DATABACKUP",registroOriginal);
			
			//Accio y Modo:
			request.setAttribute("accion",accion);
			request.setAttribute("modo",modo);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		} 									
		return forward;
	}

	/** 
	 * No implementada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm (this.getUserBean(request));
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		
		String forward = "inicio";	
		UsrBean usr;
		String idEstadoFacturacion=null, idInstitucion=null, idFacturacion=null, idPagosJG=null, nombreFacturacion=null;
		Hashtable registroOriginal = new Hashtable();
		FcsPagosJGBean pagosBean = new FcsPagosJGBean();
		String importeFacturado=null;
		
		try 
		{
			//Recuperamos el USRBEAN:
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			//Recogemos los parámetros de las pestanhas 
			idInstitucion = miform.getIdInstitucion();
			idPagosJG = (String)request.getSession().getAttribute("idPagosJG");
			//Si vengo de insertar un pago (recien creado) tendre en sesion el idPagosJG).
			//Si vengo de un pago editado tomo el pago del formulario.
			if (idPagosJG == null) idPagosJG = miform.getIdPagosJG();

			//Recuperamos el importe Facturado:
			importeFacturado = miform.getImporteFacturado();
			
			//Creamos una clausula where que nos servirá para consultar por idPagosJG e idInstitucion
			String where = " where "+FcsPagosJGBean.C_IDINSTITUCION+"="+ idInstitucion +" and "+FcsPagosJGBean.C_IDPAGOSJG+"="+ idPagosJG + " ";

			//Traemos de base de datos la factura seleccionada, con los datos recogidos de la pestanha
			Vector registros = pagosAdm.select(where);
			pagosBean = (FcsPagosJGBean)registros.get(0);
			registroOriginal = pagosAdm.beanToHashTable(pagosBean);

			//Tratamiento de los importes:
			//IMPORTE REPARTIR:
			registroOriginal.put(FcsPagosJGBean.C_IMPORTEREPARTIR, UtilidadesString.tratarImporte(pagosBean.getImporteRepartir()));
			//IMPORTE PAGADO:
			registroOriginal.put(FcsPagosJGBean.C_IMPORTEPAGADO, UtilidadesString.tratarImporte(pagosBean.getImportePagado()));
			
			//Recuperamos el estado, la fecha y el id del estado del pago:
			Hashtable hash = pagosAdm.getEstadoPago(new Integer(idInstitucion),new Integer(idPagosJG));
			String nombreEstado = (String)hash.get(FcsEstadosPagosBean.C_DESCRIPCION);
			String fechaEstado = (String)hash.get(FcsPagosEstadosPagosBean.C_FECHAESTADO);
			String idEstadoPagosJG = (String)hash.get(FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG);
					
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
			
			//Consultamos el estado de la facturacion:
			idFacturacion = pagosBean.getIdFacturacion().toString();
			idInstitucion = pagosBean.getIdInstitucion().toString();
			Hashtable hashFacturacion = facturacionAdm.getEstadoFacturacion(idInstitucion, idFacturacion); 
			idEstadoFacturacion = (String)hashFacturacion.get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION);
			
			//Nombre de la facturacion:
			where = " WHERE "+FcsFacturacionJGBean.C_IDINSTITUCION+"="+pagosBean.getIdInstitucion().toString()+
					" AND "+FcsFacturacionJGBean.C_IDFACTURACION+"="+pagosBean.getIdFacturacion().toString();
			nombreFacturacion = ((FcsFacturacionJGBean)(facturacionAdm.select(where).get(0))).getNombre(); 
			
			//Actualizamos el criterio:
//			miform.setCriterioPago(pagosBean.getCriterioPago());
			miform.setCriterioPagoTurno(pagosBean.getCriterioPagoTurno());
			
			//Actualizamos el criterio de valor de facturacion (para saber si recuperamos los puntos de cuando 
			// hicimos la factura o del momento del pago):
//			if (pagosBean.getValoresFacturacion()==null || pagosBean.getValoresFacturacion().equals(ClsConstants.DB_FALSE))
//				valoresFacturacion = null;//ClsConstants.DB_FALSE;
//			else
//				valoresFacturacion = ClsConstants.DB_TRUE;
//			miform.setValoresFacturacion(valoresFacturacion);

			//Consultamos el Parámetro COBRO AUTOMATICO
			//para el caso de que se quiera Cerrar el Pago
			//y lo pasamos por la request
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String cobroAutomatico = (String)paramAdm.getValor(usr.getLocation(), ClsConstants.MODULO_FACTURACION_SJCS , "DEDUCIR_COBROS_AUTOMATICO", null);
			String automatico = (cobroAutomatico.equalsIgnoreCase(ClsConstants.DB_TRUE)?"si":"no");
			request.setAttribute("cobroAutomatico",automatico);
			
			//Pasamos en el request los siguientes datos:
			//BEAN PAGOSJG:
			request.setAttribute("PAGOSBEAN",pagosBean);
			//NOMBRE INSTITUCION:
			request.setAttribute("nombreInstitucion",nombreInstitucion);
			//DATOS DEL ESTADO:
			request.setAttribute("nombreEstado",nombreEstado);
			request.setAttribute("fechaEstado",fechaEstado);
			request.setAttribute("idEstadoPagosJG",idEstadoPagosJG);
			//DATOS DE LA FACTURACION:
			request.setAttribute("idEstadoFacturacion",idEstadoFacturacion);
			request.setAttribute("nombreFacturacion",nombreFacturacion);
			request.setAttribute("importeFacturado",importeFacturado);
			//FORMULARIO:
			request.setAttribute("formularioPagos",miform);
			
			//Almaceno en sesion el bean para futuras modificaciones:
			request.getSession().setAttribute("DATABACKUP",registroOriginal);
			
			//Accio y Modo:
			request.setAttribute("accion","edicion");
			request.setAttribute("modo","modificarPago");
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		} 									
		return forward;
	}

	/** 
	 * Método que implementa la accion ejecutarFacturacion. Modifica el estado del pago a confirmado.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ejecutarPago (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		FcsPagosEstadosPagosAdm estadoPagosAdm = new FcsPagosEstadosPagosAdm(this.getUserBean(request));
		FcsPagosJGAdm pagosJGAdm = new FcsPagosJGAdm(this.getUserBean(request)); 
		UsrBean usr;
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		String forward="";
		Hashtable registroSesion;
		String estadoPago=null, criterioTurno=null;
		UserTransaction  tx = null;
				
		try	{
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		    tx = usr.getTransactionPesada();
			
			//Datos del pago:
			estadoPago = miform.getIdEstadoPagosJG();
			criterioTurno = miform.getCriterioPagoTurno();
			
			//Validacion de los datos antes de ejecutar el pago:
			//1. El estado del pago debe ser abierto:
			if (!estadoPago.equals(ClsConstants.ESTADO_PAGO_ABIERTO))
				return exito("messages.factSJCS.error.EstadoPagoNoCorrecto",request);	
			
			//2. Criterios correctos del Turno:
			if (!criterioTurno.equals(ClsConstants.CRITERIOS_PAGO_FACTURACION))
				return exito("messages.factSJCS.error.criterioPagoTurno",request);
		
			//INICIO TRANSACCION
			tx.begin();
    					
			//Obtenemos el Pago modificado del JSP:			
			Hashtable datosEntrada = (Hashtable)miform.getDatos();			
			datosEntrada.put(FcsEstadosPagosBean.C_FECHAMODIFICACION,"SYSDATE");
			datosEntrada.put(FcsEstadosPagosBean.C_USUMODIFICACION,usr.getUserName());
			FcsPagosEstadosPagosBean pagosEstadosBean = (FcsPagosEstadosPagosBean)estadoPagosAdm.hashTableToBean(datosEntrada);
			pagosEstadosBean.setIdEstadoPagosJG(new Integer(ClsConstants.ESTADO_PAGO_EJECUTADO));
			pagosEstadosBean.setFechaEstado("SYSDATE");
		
			//Insertamos el estado del pago:
			estadoPagosAdm.insert(pagosEstadosBean);
			
			//Recuperamos de sesion el registro editado:
			registroSesion = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			
			// Proceso de facturacion
			Integer idInstitucion = UtilidadesHash.getInteger(registroSesion, FcsPagosJGBean.C_IDINSTITUCION),
					idFacturacion = UtilidadesHash.getInteger(registroSesion, FcsPagosJGBean.C_IDFACTURACION);

			double  importeTotal = 0;
			Double  importeOficio = null, 
					importeGuardia = null, 
					importeSOJ = null,  
					importeEJG = null; 

			// crea el bean de pago colegiado e inicializa los datos comunes 
			Integer idPagoJG = new Integer(miform.getIdPagosJG());
			Integer idPersona = new Integer(usr.getUserName());
			
			//Se llama a los paquetes que ejecutan los pagos para cada concepto
			//Estas funciones sólo actualizan los importes de los conceptos 
			//del registro creado anteriormente
			importeOficio = EjecucionPLs.ejecutarPL_PagoTurnosOficio(idInstitucion, idPagoJG, idPersona);
			importeTotal += importeOficio.doubleValue();
			
			importeGuardia = EjecucionPLs.ejecutarPL_PagoGuardias(idInstitucion, idPagoJG, idPersona);
			importeTotal += importeGuardia.doubleValue();
			
			importeSOJ = EjecucionPLs.ejecutarPL_PagoSOJ(idInstitucion, idPagoJG, idPersona);
			importeTotal += importeSOJ.doubleValue();
			
			importeEJG = EjecucionPLs.ejecutarPL_PagoEJG(idInstitucion, idPagoJG, idPersona);
			importeTotal += importeEJG.doubleValue();
			
			// Calcula y actualiza IRPF, retenciones, movimientos varios y personaSociedad (idperdestino)
			
			
			tx.commit();

		
			// Exportacion de datos a EXCEL
			UtilidadesFacturacionSJCS.exportarDatosPagos(idInstitucion, idFacturacion, idPagoJG, null, usr);
			
			//Consultamos el registro modificado tal cual esta en base de datos y lo almacenamos en sesion:
			String where = 	" where " + FcsPagosJGBean.C_IDINSTITUCION + " = " + miform.getIdInstitucion()+ 
							" and " + FcsPagosJGBean.C_IDPAGOSJG + " = " + miform.getIdPagosJG() + " ";
			Hashtable registroModificado = new Hashtable();
			registroModificado = ((FcsPagosJGBean)pagosJGAdm.select(where).elementAt(0)).getOriginalHash();
			request.getSession().setAttribute("DATABACKUP",registroModificado);
					
			//Terminamos:
			//Paso los parametros al jsp del refresco especifico para este caso de uso:
			request.setAttribute("mensaje","messages.updated.success"); 
			request.setAttribute("modo","abrirAvanzada");					
			request.setAttribute("idPagosJG",miform.getIdPagosJG());
			request.setAttribute("idInstitucion",miform.getIdInstitucion());
			forward = "exitoInsertarPago";						
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacionSJCS"}, e, tx); 
		}
		return forward;
	}

	/** 
	 * No implementada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}

	/** 
	 * Método que implementa la accion nuevo. Implementa la acción al pulsar el botón nuevo en la <br>
	 * pantalla inicial del caso de uso.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		String forward="inicio";
		String nombreInstitucion = "";
		UsrBean usr;
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		
		try {
			//Recuperamos el USRBEAN:
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			//Recogemos los parámetros de las pestanhas 
			String idInstitucion = (String)request.getParameter("idInstitucion");
			String accion = (String)request.getParameter("accion");
			
			//Consultamos el nombre de la institucion de la pestanha:
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());

			//Seleccionamos el Criterio de Pago por Facturacion:
//			miform.setCriterioPago(ClsConstants.CRITERIOS_PAGO_FACTURACION);
			miform.setCriterioPagoTurno(ClsConstants.CRITERIOS_PAGO_FACTURACION);
			
			//Seleccionamos el criterio de valor de facturacion (para saber si recuperamos los puntos de cuando 
			// hicimos la factura o del momento del pago): por defecto seleccionado.
//			miform.setValoresFacturacion(ClsConstants.DB_TRUE);
			
			//pasamos por el request el modo y el nombre De la institucion
			request.setAttribute("modo","insertarPago");
			request.setAttribute("accion",accion);
			request.setAttribute("idInstitucionRegistro",idInstitucion);
			request.setAttribute("nombreInstitucion",nombreInstitucion);
			request.setAttribute("formularioPagos",miform);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return forward;
	}

	/** 
	 * No implementada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}

	/** 
	 * No implementada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 * No implementada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 * No implementada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 * Método que implementa la accion insertarPago. Crea un nuevo pago con el estado abierto.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertarPago (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		//Pagos y Facturaciones:
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(this.getUserBean(request));
		FcsPagosEstadosPagosAdm estadoPagosAdm = new FcsPagosEstadosPagosAdm(this.getUserBean(request));
		//Hitos y Grupos de Pagos y Facturas:
		FcsPagoGrupoFactHitoAdm pagoGrupoAdm = new FcsPagoGrupoFactHitoAdm(this.getUserBean(request));
		FcsFactGrupoFactHitoAdm factGrupoAdm = new FcsFactGrupoFactHitoAdm(this.getUserBean(request));
		
		UsrBean usr;
		String forward="";
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		Hashtable registro;
		String idPagosJG=null, idEstadoPagosJG=null, where=null;
		UserTransaction tx = null;
		FcsFacturacionJGBean factBean;
		Vector vCriterios;
		
		try 
		{
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			String idInstitucion = miform.getIdInstitucion();
			
			//Chequeamos que no exista ningun pago con estado distinto a cerrado para esa facturacion:
			if (pagosAdm.hayEstadoPagoNoCerrados(idInstitucion,miform.getIdFacturacion()))
				forward = exito("messages.factSJCS.error.existePagoAbierto",request);
			else {
				//Chequeamos que el importe + facturado > pagado:
				//if (pagosAdm.excedePagoFacturacion(idInstitucion,miform.getIdFacturacion(),Double.valueOf(miform.getImporteRepartir()).doubleValue()))
				//	forward = exito("messages.factSJCS.error.pagoExcedido",request);
				//else {
					//Consulto las fechas de la facturacion:
					where = " WHERE " + FcsFacturacionJGBean.C_IDINSTITUCION + "=" + idInstitucion+
							" AND "   + FcsFacturacionJGBean.C_IDFACTURACION + "=" + miform.getIdFacturacion();						
					factBean = (FcsFacturacionJGBean)facturacionAdm.select(where).get(0);
					
					//1. INSERTAMOS EL PAGO:
					//preparamos el nuevo registro del pago:
					registro = new Hashtable();
					registro.put(FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
					idPagosJG = pagosAdm.getNuevoId(idInstitucion);
					registro.put(FcsPagosJGBean.C_IDPAGOSJG, idPagosJG);
					registro.put(FcsPagosJGBean.C_IDFACTURACION, miform.getIdFacturacion());
					registro.put(FcsPagosJGBean.C_NOMBRE, miform.getNombre());
					registro.put(FcsPagosJGBean.C_ABREVIATURA, miform.getAbreviatura());
					registro.put(FcsPagosJGBean.C_FECHADESDE, GstDate.getApplicationFormatDate(usr.getLanguage(),GstDate.getFormatedDateShort(usr.getLanguage(),factBean.getFechaDesde())));
					registro.put(FcsPagosJGBean.C_FECHAHASTA, GstDate.getApplicationFormatDate(usr.getLanguage(),GstDate.getFormatedDateShort(usr.getLanguage(),factBean.getFechaHasta())));
//					registro.put(FcsPagosJGBean.C_CRITERIOPAGO, miform.getCriterioPago());
					registro.put(FcsPagosJGBean.C_CRITERIOPAGOTURNO, miform.getCriterioPagoTurno());
					registro.put(FcsPagosJGBean.C_PORCENTAJEOFICIO, tratarValor(miform.getPorcentajeOficio()));
					registro.put(FcsPagosJGBean.C_PORCENTAJEGUARDIAS,  tratarValor(miform.getPorcentajeGuardias()));
					registro.put(FcsPagosJGBean.C_PORCENTAJEEJG,  tratarValor(miform.getPorcentajeEJG()));
					registro.put(FcsPagosJGBean.C_PORCENTAJESOJ,  tratarValor(miform.getPorcentajeSOJ()));
					//Tratamiento de los importes:
					registro.put(FcsPagosJGBean.C_IMPORTEREPARTIR, new Double(miform.getImporteRepartir()));
					registro.put(FcsPagosJGBean.C_IMPORTEPAGADO, miform.getImportePagado());
					registro.put(FcsPagosJGBean.C_CONTABILIZADO, "0");
					
					GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
					String paramConcepto = paramAdm.getValor(idInstitucion, "FCS", "CONCEPTO_ABONO", "");
					if (!paramConcepto.equalsIgnoreCase("1")&&!paramConcepto.equalsIgnoreCase("8")&&!paramConcepto.equalsIgnoreCase("9")){
						throw new SIGAException("administracion.parametrosGenerales.error.conceptoAbono");
					}
					registro.put(FcsPagosJGBean.C_CONCEPTO, paramConcepto);
					registro.put(FcsPagosJGBean.C_BANCOS_CODIGO, paramAdm.getValor(idInstitucion, "FCS", "BANCOS_CODIGO_ABONO", "")); 
					
					UtilidadesHash.set(registro, FcsPagosJGBean.C_IMPORTEMINIMO, 	new Double(0));
					UtilidadesHash.set(registro, FcsPagosJGBean.C_IMPORTEEJG, 		tratarValor((miform.getImporteEJG())));
					UtilidadesHash.set(registro, FcsPagosJGBean.C_IMPORTEGUARDIA, 	tratarValor(miform.getImporteGuardias()));
					UtilidadesHash.set(registro, FcsPagosJGBean.C_IMPORTEOFICIO, 	tratarValor(miform.getImporteOficio()));
					UtilidadesHash.set(registro, FcsPagosJGBean.C_IMPORTESOJ, 		tratarValor(miform.getImporteSOJ()));
					
					
//					if (miform.getValoresFacturacion() == null || miform.getValoresFacturacion().equals(ClsConstants.DB_FALSE))
//						valoresFacturacion = ClsConstants.DB_FALSE;
//					else
//						valoresFacturacion = ClsConstants.DB_TRUE;
//					miform.setValoresFacturacion(valoresFacturacion);
//					registro.put(FcsPagosJGBean.C_VALORESFACTURACION, valoresFacturacion);

					//insertamos:
					tx.begin();
					pagosAdm.insert(registro);
					//Almacenamos en sesion el bean del pago:
					request.getSession().setAttribute("DATABACKUP",registro);
					
					//Insercion del estado del registro como abierto:
					registro = new Hashtable();
					registro.put(FcsPagosEstadosPagosBean.C_IDINSTITUCION,idInstitucion);
					registro.put(FcsPagosEstadosPagosBean.C_IDPAGOSJG,idPagosJG);
					idEstadoPagosJG = ClsConstants.ESTADO_PAGO_ABIERTO;
					registro.put(FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG,idEstadoPagosJG);
					registro.put(FcsPagosEstadosPagosBean.C_FECHAESTADO,"SYSDATE");
					registro.put(FcsPagosEstadosPagosBean.C_USUMODIFICACION,usr.getUserName());
					registro.put(FcsPagosEstadosPagosBean.C_FECHAMODIFICACION,"SYSDATE");
					//insertamos:
					estadoPagosAdm.insert(registro);
					
					//2. INSERTAMOS EL CRITERIO DE PAGO:
					//Preparamos el nuevo registro del pago:
					where = " WHERE "+FcsFactGrupoFactHitoBean.C_IDINSTITUCION+"="+idInstitucion+
						    " AND "+FcsFactGrupoFactHitoBean.C_IDFACTURACION+"="+miform.getIdFacturacion();				
					vCriterios = factGrupoAdm.select(where);
					for (int i=0; i < vCriterios.size();i++) {
						FcsFactGrupoFactHitoBean bean = (FcsFactGrupoFactHitoBean)vCriterios.get(i);
						registro = new Hashtable();
						registro.put(FcsPagoGrupoFactHitoBean.C_IDINSTITUCION, idInstitucion);
						registro.put(FcsPagoGrupoFactHitoBean.C_IDPAGOSJG, idPagosJG);
						registro.put(FcsPagoGrupoFactHitoBean.C_IDHITOGENERAL, bean.getIdHitoGeneral().toString());
						registro.put(FcsPagoGrupoFactHitoBean.C_IDGRUPOFACTURACION, bean.getIdGrupoFacturacion().toString());				
						//Primero borramos el posible registro:
						pagoGrupoAdm.delete(registro);				
						//Luego Insertamos:
						registro.put(FcsPagoGrupoFactHitoBean.C_USUMODIFICACION, usr.getUserName());
						registro.put(FcsPagoGrupoFactHitoBean.C_FECHAMODIFICACION, "sysdate");
						pagoGrupoAdm.insert(registro);
					}
					
					//Actualizamos el criterio:
//					miform.setCriterioPago(miform.getCriterioPago());
					tx.commit();

					//Paso los parametros al jsp del refresco especifico para este caso de uso:
					request.setAttribute("mensaje","messages.inserted.success"); 
					request.setAttribute("modo","abrirAvanzada");					
					request.setAttribute("idPagosJG",idPagosJG);
					request.setAttribute("idInstitucion",idInstitucion);
					forward = "exitoInsertarPago";						
				}
			//}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return forward;
	}

	/** 
	 * Método que implementa la accion cerrarPago. Modifica el estado del pago a cerrado.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String cerrarPago (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		FcsPagosEstadosPagosAdm estadoPagosAdm = new FcsPagosEstadosPagosAdm(this.getUserBean(request));
		UsrBean usr;
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		String forward="";		
		String idPago ="";
		String idFacturacion ="";
		UserTransaction tx=null;	
		
		try	{
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransactionPesada();
	
			//Obtenemos el Pago modificado del JSP:			
			Hashtable datosEntrada = (Hashtable)miform.getDatos();
			datosEntrada.put(FcsEstadosPagosBean.C_FECHAMODIFICACION,"SYSDATE");
			datosEntrada.put(FcsEstadosPagosBean.C_USUMODIFICACION,usr.getUserName());
			FcsPagosEstadosPagosBean estadoPagosBean = (FcsPagosEstadosPagosBean)estadoPagosAdm.hashTableToBean(datosEntrada);
			estadoPagosBean.setIdEstadoPagosJG(new Integer(ClsConstants.ESTADO_PAGO_CERRADO));
			estadoPagosBean.setFechaEstado("SYSDATE");
			
			tx.begin();
			//Insertamos el estado del pago:
			estadoPagosAdm.insert(estadoPagosBean);
			request.setAttribute("modo","modificarPago");
			
			//Pasamos automaticamente a deducir los pagos
			String idInstitucion = usr.getLocation();
			idPago = miform.getIdPagosJG();
			idFacturacion=miform.getIdFacturacion();
			generarAbonos(idInstitucion, idPago, request, null);

			request.setAttribute("mensaje","messages.updated.success");
			tx.commit();			

			// Exportacion de datos a EXCEL
			UtilidadesFacturacionSJCS.exportarDatosPagos(new Integer(idInstitucion), new Integer(idFacturacion),new Integer(idPago),null, usr);

			
			//Paso los parametros al jsp del refresco especifico para este caso de uso:
			request.setAttribute("modo","abrirAvanzada");					
			request.setAttribute("idPagosJG",idPago);
			request.setAttribute("idInstitucion",idInstitucion);
			forward = "exitoInsertarPago";						
			
		} 
		catch (ClsExceptions e) {
			throwExcp(e.getMessage(),e,tx);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return forward;
	}

	/** 
	 * Método que implementa la accion cerrarPagoManual. Modifica el estado del pago a cerrado.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String cerrarPagoManual (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		FcsPagosEstadosPagosAdm estadoPagosAdm = new FcsPagosEstadosPagosAdm(this.getUserBean(request));
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		String forward="";
		String idPago ="";
		UserTransaction tx=null;
		
		try	{
			tx = usr.getTransaction();
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
			//Obtenemos el Pago modificado del JSP:			
			Hashtable datosEntrada = (Hashtable)miform.getDatos();
			datosEntrada.put(FcsEstadosPagosBean.C_FECHAMODIFICACION,"SYSDATE");
			datosEntrada.put(FcsEstadosPagosBean.C_USUMODIFICACION,usr.getUserName());
			FcsPagosEstadosPagosBean estadoPagosBean = (FcsPagosEstadosPagosBean)estadoPagosAdm.hashTableToBean(datosEntrada);
			estadoPagosBean.setIdEstadoPagosJG(new Integer(ClsConstants.ESTADO_PAGO_CERRADO));
			estadoPagosBean.setFechaEstado("SYSDATE");
			
			tx.begin();
			//Insertamos el estado del pago:
			estadoPagosAdm.insert(estadoPagosBean);
			request.setAttribute("modo","modificarPago");
	
			//ahora pasamos a generar abonos
			String idInstitucion = (String)usr.getLocation();
			idPago = (String)miform.getIdPagosJG();
			//Vector letradosAPagar = (Vector)miform.getVcolegiados();
			Vector letradosAPagar = new Vector();
		    String tok=request.getParameter("idsParaEnviar");
	    	StringTokenizer st = new StringTokenizer(tok, ";");
	    	while (st.hasMoreElements()) {
	    	    String el = (String) st.nextElement();
			    String idPersona         = el;
			    String idInst         = this.getUserBean(request).getLocation();
			    ColegiadosPagosBean b = new ColegiadosPagosBean();
			    b.setIdInstitucion(idInst);
			    b.setIdPersona(idPersona);
			    b.setMarcado("1");
			    letradosAPagar.add(b);
			}
			
			this.generarAbonos(idInstitucion, idPago, request, letradosAPagar);
			
			//Paso los parametros al jsp del refresco especifico para este caso de uso:
			request.setAttribute("modo","abrirAvanzada");					
			request.setAttribute("idPagosJG",idPago);
			request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("cerrarModal","SI");
			forward = "exitoInsertarPago";						
	
			request.setAttribute("mensaje","messages.updated.success");
			tx.commit();
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return forward;
	}

	/**
	 *
	 * 	Por cada colegiado aplicamos el proceso de generar Abonos
	 * 		1. Obtener el total SJCS
	 * 		2. Aplicar los movimientos varios
	 * 		3. Obtener importe bruto como la suma de los movimientos varios y el total SJCS
	 * 		4. Obtener el importe neto aplicando el IRPF
	 * 		5. Aplicar retenciones judiciales y no judiciales
	 * 			- Aplicar tramos LEC ????
	 * 		6. Generar abono	 
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @param request
	 * @param colegiadosMarcados
	 * @return
	 * @throws ClsExceptions
	 */
	protected void generarAbonos(String idInstitucion, String idPago, HttpServletRequest request, 
			Vector colegiadosMarcados) throws ClsExceptions {
		//Controles
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm(usr);
		Hashtable importes = new Hashtable();

		//variables para hacer el calculo del importe final a pagar
		String idPersonaSociedad="";
		double importeSJCS=0.0d;
		double importeTurnos=0.0d, importeGuardias=0.0d, importeSoj=0.0d, importeEjg=0.0d;
		double importeMovimientos=0.0d, importeRetenciones=0.0d;	
		Double porcentajeIRPF;
		double importeIrpfTotal=0.0d;

		// Recuperamos los colegiados a los que tenemos que pagar
		// aquellos incluidos en el pago o con movimientos varios pendientes
		Vector colegiados = (Vector) pagoAdm.getColegiadosAPagar(idInstitucion, idPago);

		for (Iterator iter = colegiados.iterator(); iter.hasNext(); ) {
			//recupera el colegiado
			String idPersona = UtilidadesHash.getString((Hashtable) iter.next(), "IDPERSONA_SJCS");

			//obtiene el pago del colegiado
			FcsPagoColegiadoAdm pcAdm = new FcsPagoColegiadoAdm(usr);
			Hashtable hash = new Hashtable();
			hash.put(FcsPagoColegiadoBean.C_IDINSTITUCION, idInstitucion);
			hash.put(FcsPagoColegiadoBean.C_IDPAGOSJG, idPago);
			hash.put(FcsPagoColegiadoBean.C_IDPERORIGEN, idPersona);
			Vector vector = pcAdm.selectByPK(hash);
			// Si no existe un pago para el colegiado debe existir al menos un MV
			// por lo que pasa a tratar los movimientos varios
			if (!vector.isEmpty()){
				FcsPagoColegiadoBean pcBean = (FcsPagoColegiadoBean) vector.get(0);
				importeTurnos		= pcBean.getImpOficio().doubleValue();
				importeGuardias		= pcBean.getImpAsistencia().doubleValue();
				importeSoj			= pcBean.getImpSOJ().doubleValue();
				importeEjg			= pcBean.getImpEJG().doubleValue();		
				idPersonaSociedad 	= pcBean.getIdPerDestino() == null ? "" : pcBean.getIdPerDestino().toString();

				// 1. Calcula el IMPORTE SJCS BRUTO
				importeSJCS = importeTurnos + importeGuardias + importeSoj + importeEjg;
			}

			//obtiene el porcentajeIRPF del colegiado para utilizarlo al aplicar 
			//los movimientos varios y calcular el IRPF del importe bruto.
			porcentajeIRPF = obtenerIrpf(idInstitucion, idPersona, !idPersonaSociedad.equals(""));

			// 2. Aplicar los movimientos varios
			// Asocia todos los movimientos sin idpago al pago actual.
			// Actualiza el porcentaje e importe IRPF para cada movimiento.
			FcsMovimientosVariosAdm movimientosAdm = new FcsMovimientosVariosAdm(usr);
			movimientosAdm.updatePago(idInstitucion, idPago, idPersona, porcentajeIRPF.toString());
			importeMovimientos = aplicarMovimientosVarios(idInstitucion, idPago, idPersona, usr);

			// 3. Obtener importe bruto como la suma de los movimientos varios y el total SJCS
			double importeBruto = importeSJCS + importeMovimientos;

			// 4. Obtener el importe neto aplicando el IRPF	
			importeIrpfTotal = -1 * (importeBruto * porcentajeIRPF / 100);
			double importeNeto = importeBruto + importeIrpfTotal;

			// 5. Aplicar retenciones judiciales y no judiciales
			aplicarRetencionesJudiciales( idInstitucion, idPago, idPersona, 
					Double.toString(importeNeto), Long.toString(usr.getIdPersona()));
			// obtener el importe de las retenciones judiciales
			FcsCobrosRetencionJudicialAdm crjAdm = new FcsCobrosRetencionJudicialAdm(usr); 
			importeRetenciones = crjAdm.getSumaRetenciones(idInstitucion, idPago, idPersona);

			//Actualizar el irpf, movimientos varios y retenciones en fcs_pago_colegiado
			pcAdm.updateCierrePago(idInstitucion, idPago, idPersona, 
					porcentajeIRPF, importeMovimientos, importeRetenciones);


			// 6. Generar abono	si corresponde
			if (importeMovimientos > 0 || importeNeto > 0){
				try {
					//consultamos la cuenta
					CenClienteAdm clienteAdm = new CenClienteAdm (usr);
					ArrayList cuenta = clienteAdm.getCuentaAbono (idInstitucion, 
							(!idPersonaSociedad.equals("") ? idPersonaSociedad : idPersona));

					//Guardamos los importes:
					importes.put("importeTurnos", String.valueOf(importeTurnos));
					importes.put("importeGuardias", String.valueOf(importeGuardias));
					importes.put("importeSoj", String.valueOf (importeSoj));
					importes.put("importeEjg", String.valueOf (importeEjg));
					importes.put("importeMovimientos", String.valueOf(importeMovimientos));
					importes.put("importeRetenciones", String.valueOf(importeRetenciones));

					//Creamos el Abono:
					this.crearAbonos (cuenta, request, colegiadosMarcados, 
							(!idPersonaSociedad.equals("") ? idPersonaSociedad: idPersona),
							idPago, idInstitucion, importes, importeIrpfTotal, idPersona);
				} catch (Exception e) {
					throw new ClsExceptions(e,"DatosGeneralesPagoAction.generarAbonos");
				} 
			}
		} //fin del for de colegiados

	}

	/**
	 * Devuelve el porcentaje de irpf a aplicar en un pago
	 * @param idInstitucion
	 * @param idPersonaSociedad
	 * @param esSociedad 
	 * @return
	 * @throws ClsExceptions
	 */
	private Double obtenerIrpf(String idInstitucion, String idPersonaSociedad, boolean esSociedad) throws ClsExceptions {
		String resultado[] = EjecucionPLs.ejecutarPLCalcularIRPF_Pagos(
				idInstitucion, idPersonaSociedad, esSociedad);
		//comprueba si el pl se ha ejecutado correctamente
		if (!resultado[1].equals("0")){
			throw new ClsExceptions("Error al obtener importes de colegiado: " + resultado[2]);
		}
		return new Double((String) resultado[0]);
	}
	
	
	/**
	 * Devuelve el porcentaje de irpf a aplicar en un pago
	 * @param idInstitucion
	 * @param idPersonaSociedad
	 * @param usuario de modificacion 
	 * @return
	 * @throws ClsExceptions
	 */
	private void aplicarRetencionesJudiciales(
			String idInstitucion, String idPagoJg, String idPersonaSociedad, 
			String importeNeto, String usuMod) throws ClsExceptions {


		// Aplicar las retenciones judiciales
		String resultado[] = EjecucionPLs.ejecutarPLAplicarRetencionesJudiciales(
				idInstitucion, idPagoJg, idPersonaSociedad, importeNeto, usuMod);
		//comprueba si el pl se ha ejecutado correctamente
		if (!resultado[0].equals("0")){
			throw new ClsExceptions("Error al obtener importes de colegiado: " + resultado[1]);
		}
		
	}
	
	
	/**
	 *  Devuelve el importe total de los movimientos varios.
	 *  El algoritmos utilizado es el siguiente:
	 *	1 Aplicar todos los MVs positivos
	 *	2 Ordenar los MVs negativos por fecha
	 *	3 Intentar aplicar MV
	 *		· Si la cantidad resultante es igual a 0 entonces
	 *			Terminar
	 *		· Si la cantidad resultante es menor que 0 entonces
	 *			Dejar cantidad resultante en 0
	 *			Crear MV con la diferencia
	 *			Terminar
	 *	4 Seguir con otro MV en el paso 2.3

	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 */
	private double aplicarMovimientosVarios(
			String idInstitucion, String idPago, String idPersona, UsrBean usr) throws ClsExceptions{
		
		// en esta variable se guarda el importe final de los movimientos varios
		double importeMovimientos = 0.0d;
		
		//obtiene todos los movimientos varios del colegiado ordenados por fecha de alta
		FcsMovimientosVariosAdm movimientosAdm = new FcsMovimientosVariosAdm(usr);
		Vector movimientos =  movimientosAdm.getMovimientosRW(idInstitucion, idPago, idPersona);

		//obtiene la suma de todos los movimientos varios positivos
		for(Iterator iterMov = movimientos.iterator(); iterMov.hasNext();){
			double aux = UtilidadesHash.getDouble((Hashtable) iterMov.next(), "CANTIDAD").doubleValue();
			if (aux > -1){
				importeMovimientos += aux;
				iterMov.remove();
			}
		}
		// Intentar aplicar MV negativos
		for(Iterator iterMov = movimientos.iterator(); iterMov.hasNext();){
			double aux = UtilidadesHash.getDouble((Hashtable) iterMov.next(), "CANTIDAD").doubleValue();
			importeMovimientos += aux;
			// Si no se pueden aplicar mas movimientos negativos, sale de la iteracion
			if (importeMovimientos == 0)
				break;
			// Si el importe del movimiento es mayor que el restante de movimientos positivos
			// deja la cantidad resultante en 0, crea un MV con la diferencia y termina.
			if (importeMovimientos < 0){
				double importeNuevoMovimiento = importeMovimientos;
				importeMovimientos = 0;
				Hashtable movimientoBean = new Hashtable ();
				movimientoBean.put(FcsMovimientosVariosBean.C_CANTIDAD, String.valueOf(importeNuevoMovimiento));
				movimientoBean.put(FcsMovimientosVariosBean.C_DESCRIPCION,
						UtilidadesString.getMensajeIdioma( usr.getLanguage(), "factSJCS.abonos.literal.descripcionMovimiento"));
				movimientoBean.put(FcsMovimientosVariosBean.C_FECHAALTA, "SYSDATE");
				movimientoBean.put(FcsMovimientosVariosBean.C_FECHAMODIFICACION, "SYSDATE");
				movimientoBean.put(FcsMovimientosVariosBean.C_IDINSTITUCION, idInstitucion);
				movimientoBean.put(FcsMovimientosVariosBean.C_IDPERSONA, idPersona);
				movimientoBean.put(FcsMovimientosVariosBean.C_CONTABILIZADO, "0");
				movimientoBean = movimientosAdm	.prepararInsert(movimientoBean);
				movimientosAdm.insert (movimientoBean);
				// Seguir con otro MV
				continue;
			}
		}
		
		return importeMovimientos;
	}
	
	/**
	 * Funcion que comprueba si hay que deducir cobros para una persona
	 * 
	 * @param idInstitucion
	 * @param idPersona
	 * @param request
	 * @return
	 */
	protected boolean deducirCobros (String idInstitucion, String idPersona, HttpServletRequest request, Vector vColegiados){
		boolean resultado = false;
		GenParametrosAdm paramAdm = new GenParametrosAdm (this.getUserBean(request));
		try{
			String cobroAutomatico = (String)paramAdm.getValor(idInstitucion, ClsConstants.MODULO_FACTURACION_SJCS , "DEDUCIR_COBROS_AUTOMATICO", null);
			boolean automatico = (cobroAutomatico.equals(ClsConstants.DB_TRUE));
			if (automatico) resultado = true;
			else{
				if (colegiadosDeducibles(idInstitucion, idPersona, vColegiados)) resultado = true;
				else resultado = false;
			}
		}catch(Exception e){}
		return resultado;
	}
	
	
	/**
	 * Funcion que comprueba que el letrado identificado con esa IdInstitucion y ese idPersona esté marcado.
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected boolean colegiadosDeducibles (String idInstitucion, String idPersona, Vector vColegiados){
		boolean resultado=false, continuar=true;
		ColegiadosPagosBean colegiado = new ColegiadosPagosBean();

		//buscamos dentor del Vector algun letrado con ese idPersona y el idInstitucion
		for (int contador=0; contador < vColegiados.size() && continuar; contador++){			
			colegiado = (ColegiadosPagosBean)vColegiados.get(contador);
			if (((String)colegiado.getIdPersona()).equals(idPersona) && ((String)colegiado.getIdInstitucion()).equals(idInstitucion)) {
				if (colegiado.getMarcado()==null)
					resultado = false;
				else
					resultado = true;				
				continuar = false;
			}						
		}
		return resultado;
	}
	
	protected Hashtable prepararListaAbono (Hashtable entrada, String precioUnitario, int idLinea, String descripcion){
		
		Hashtable salida = new Hashtable();
		
		salida.put(FacLineaAbonoBean.C_IDINSTITUCION, entrada.get(FacLineaAbonoBean.C_IDINSTITUCION));
		salida.put(FacLineaAbonoBean.C_IDABONO, entrada.get(FacLineaAbonoBean.C_IDABONO));
		salida.put(FacLineaAbonoBean.C_IVA, entrada.get(FacLineaAbonoBean.C_IVA));
		salida.put(FacLineaAbonoBean.C_PRECIOUNITARIO, precioUnitario);
		salida.put(FacLineaAbonoBean.C_CANTIDAD, entrada.get(FacLineaAbonoBean.C_CANTIDAD));
		salida.put(FacLineaAbonoBean.C_NUMEROLINEA, String.valueOf(idLinea));
		salida.put(FacLineaAbonoBean.C_DESCRIPCIONLINEA, descripcion);
		
		return salida;
	}
	
	/** 
	 * Método que implementa la accion insertarCriteriosPagos. Crea un nuevo criterio de pago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertarCriteriosPagos (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		FcsPagoGrupoFactHitoAdm pagoGrupoAdm = new FcsPagoGrupoFactHitoAdm(this.getUserBean(request));  
		UsrBean usr;
		String forward="";
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		Hashtable registro;
		UserTransaction tx = null;
		Vector vCriterios=null;
		
		try 
		{
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			tx = usr.getTransaction();
			
			//Recuperamos las filas:
			vCriterios = miform.getCriterios();
			
			tx.begin();
			for (int i=0; i < vCriterios.size();i++) {
				CriteriosPagosBean bean = new CriteriosPagosBean();
				bean = (CriteriosPagosBean)vCriterios.get(i);
				
				if (bean != null) {
					//Preparamos el nuevo registro del pago:
					registro = new Hashtable();
					registro.put(FcsPagoGrupoFactHitoBean.C_IDINSTITUCION, miform.getIdInstitucion());
					registro.put(FcsPagoGrupoFactHitoBean.C_IDPAGOSJG, bean.getIdPagosJG());
					registro.put(FcsPagoGrupoFactHitoBean.C_IDHITOGENERAL, bean.getIdHitoGeneral());
					registro.put(FcsPagoGrupoFactHitoBean.C_IDGRUPOFACTURACION, bean.getIdGrupoFacturacion());
					
					//Primero borramos el posible registro:
					pagoGrupoAdm.delete(registro);
					
					//Si esta marcado lo insertamos: 
					if ((bean.getCheckCriterio()!=null) && bean.getCheckCriterio().equals("SI")) {					
						//Luego Insertamos:
						registro.put(FcsPagoGrupoFactHitoBean.C_USUMODIFICACION, usr.getUserName());
						registro.put(FcsPagoGrupoFactHitoBean.C_FECHAMODIFICACION, "sysdate");
						pagoGrupoAdm.insert(registro);
					}
				}
			}
			tx.commit();
			forward = exitoModalSinRefresco("messages.inserted.success",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return forward;
	}
	
	/** 
	 * Método que implementa la accion abrirModal. Abre la modal para insertar Criterios de Pago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirModal (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		FcsFactGrupoFactHitoAdm factHitoAdm = new FcsFactGrupoFactHitoAdm(this.getUserBean(request));		
		Vector vResultados=null;		
		String forward=null;
		String idInstitucion=null, idPagosJG=null;
		
		try {
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			idInstitucion = miform.getIdInstitucion();
			idPagosJG = miform.getIdPagosJG();
			vResultados = factHitoAdm.consultarCriteriosFacturacion(idInstitucion, miform.getIdFacturacion(), idPagosJG, usr);
			miform.setCriterios(vResultados);
			request.setAttribute("resultadosCriteriosPago",vResultados);
			request.setAttribute("modo",miform.getAccionPrevia());
			forward = "resultado";
		} 
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return forward;
	}

	/** 
	 * Método que implementa la accion mostrarColegiadosAPagar. Implementa la acción al pulsar el botón Cerrar Pago en la <br>
	 * pantalla inicial del caso de uso.<br>
	 * En caso de que el valor del Parametro Cobro Automatico sea false.
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String mostrarColegiadosAPagar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		//recogemos el formulario, el usrBean y el valor de idPagosJg
		DatosGeneralesPagoForm miform = (DatosGeneralesPagoForm)formulario;
		String idPago = (String)miform.getIdPagosJG(); 
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN"); 
		
		//antes hay que consultar los colegiados a los que hay que pagar
		FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm(this.getUserBean(request));
		
		//vector que guardará todos los colegiados a pagar
		Vector resultado = new Vector();
		try{
			//vector con los colegiados que hay que pagar
			idPago = (String)miform.getIdPagosJG();
			resultado = (Vector)pagoAdm.getColegiadosAPagar((String)usr.getLocation(),idPago);
		} catch(Exception e) {
			//si hay sucedido un error al recuperar los colegiados que se van a facturar
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		//vector que le pasaremos a la jsp para que rellene la tabla
		Vector colegiados = new Vector();
		int posicion = 0;
		for (int contador=0; contador<resultado.size();contador++){
			//recogemos cada colegiado a pagar
			Hashtable colegiadoActual = (Hashtable)resultado.get(contador);
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			Long idPersona = Long.valueOf((String)colegiadoActual.get("IDPERSONA_SJCS"));
			try{
					
				CenClienteAdm cliente = new CenClienteAdm(this.getUserBean(request));
				Integer idInstitucion = new Integer((String)usr.getLocation());
				Hashtable hash = (Hashtable)((Vector)cliente.getDatosPersonales(idPersona,idInstitucion)).get(0);
				CenColegiadoBean colegiadoBean = (CenColegiadoBean)colegiadoAdm.getDatosColegiales(idPersona, idInstitucion);
				
				//por cada colegiado construimos un ColegiadosPagosBean y lo metemos en el vector colegiados
				ColegiadosPagosBean colegiadoNew = new ColegiadosPagosBean();
				colegiadoNew.setNombreColegiado(((String)hash.get(CenPersonaBean.C_NOMBRE)+" "+(String)hash.get(CenPersonaBean.C_APELLIDOS1)+" "+(String)hash.get(CenPersonaBean.C_APELLIDOS2)));
				colegiadoNew.setIdInstitucion((String)usr.getLocation());
				colegiadoNew.setIdPersona(((Long)colegiadoBean.getIdPersona()).toString());
				colegiadoNew.setNcolegiado(colegiadoAdm.getIdentificadorColegiado(colegiadoBean));
				
				//lo anhadimos
				colegiados.add(posicion, colegiadoNew);
				posicion++;
			}catch(Exception e){
				//si ha dado un error en una persona podremos seguir con el resto 
			}
		}
		miform.setVcolegiados(colegiados);
		request.setAttribute("resultado", colegiados);
		request.setAttribute("idPago",idPago);
		return "abrirDeducirCobros";
	}	
	
	/**
	 * 
	 * @param cuenta
	 * @param request
	 * @param colegiadosMarcados
	 * @param idPersonaSoc
	 * @param idPago
	 * @param idInstitucion
	 * @param importes
	 * @param irpfs
	 * @param idPersonaSJCS
	 * @throws ClsExceptions
	 */
	//Crea los abonos a partir de los importes e irpfs calculados.
	private void crearAbonos(ArrayList cuenta,HttpServletRequest request, Vector colegiadosMarcados,
							 String idPersonaSoc, String idPago, String idInstitucion, 
							 Hashtable importes, double irpfTotal, String idPersonaSJCS) throws ClsExceptions {
		UsrBean usr = null;
		boolean resultado = false;
		Vector pagos = new Vector();
	 	String importeTurnos="", importeGuardias="", importeSoj="", importeEjg="", importeMovimientos ="", importeRetenciones="", importeRetencionesPersona=""; 
	 	String personaPago = "";
	 	String sociedadPago = "";
	 	CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request)); 

		try {
						
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			//Recuperamos los importes:
			importeTurnos = (String)importes.get("importeTurnos");
			importeGuardias = (String)importes.get("importeGuardias");
			importeSoj = (String)importes.get("importeSoj");
			importeEjg = (String)importes.get("importeEjg");
			importeMovimientos = (String)importes.get("importeMovimientos");
			importeRetenciones = (String)importes.get("importeRetenciones");
			
			//Perparamos el motivo del pago
			String motivoPago = UtilidadesString.getMensajeIdioma((String)usr.getLanguage(),"factSJCS.abonos.literal.motivo");
			CenPersonaBean sociedadBean = personaAdm.getIdentificador(new Long(idPersonaSoc));
			sociedadPago = sociedadBean.getNombre();
			CenPersonaBean personaBean = personaAdm.getIdentificador(new Long(idPersonaSoc));
			personaPago = personaBean.getNombre()+" "+personaBean.getApellido1()+" "+personaBean.getApellido2();
			if (idPersonaSJCS.equalsIgnoreCase(idPersonaSJCS)){
				// Si coinciden idPersonaSJCS e idPersonaSoc el pago es a una persona 
				motivoPago += " " + personaPago;
			}else{
				// Si no coinciden el será a una sociedad a traves de un letrado  
				motivoPago += " " + sociedadPago;
				motivoPago += " " + UtilidadesString.getMensajeIdioma((String)usr.getLanguage(),"factSJCS.abonos.literal.motivo.letrado");
				motivoPago += " " + personaPago;
			}
			
			//preparamos el abono
			Hashtable hash = new Hashtable();
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(this.getUserBean(request));
			FcsPagosJGBean pagosBean = new FcsPagosJGBean();
			String idAbono = ((Long)abonoAdm.getNuevoID(usr.getLocation())).toString();
			GestorContadores gc = new GestorContadores(this.getUserBean(request)); 
			Hashtable contadorTablaHash=gc.getContador(new Integer(usr.getLocation()),ClsConstants.FAC_ABONOS);
			String numeroAbono=gc.getNuevoContadorConPrefijoSufijo(contadorTablaHash);
			hash.put(FacAbonoBean.C_IDINSTITUCION,(String)usr.getLocation());
			hash.put(FacAbonoBean.C_IDABONO, idAbono);
			hash.put(FacAbonoBean.C_NUMEROABONO, numeroAbono);
			hash.put(FacAbonoBean.C_MOTIVOS, motivoPago);
			hash.put(FacAbonoBean.C_FECHA,"SYSDATE");
			hash.put(FacAbonoBean.C_CONTABILIZADA,ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA);
			hash.put(FacAbonoBean.C_IDPERSONA,cuenta.get(1));
			hash.put(FacAbonoBean.C_IDCUENTA,cuenta.get(2).equals("-1")?"null":cuenta.get(2));
			hash.put(FacAbonoBean.C_IDPAGOSJG,idPago);
			
			// Recuperamos el nombre del pago si esta disponible y lo metemos en las observaciones
			Hashtable criterios = new Hashtable();
			criterios.put(FcsPagosJGBean.C_IDPAGOSJG, idPago);
			criterios.put(FcsPagosJGBean.C_IDINSTITUCION, (String)usr.getLocation());
			pagos = pagosAdm.getPagosParaCerrar(criterios, (String)usr.getLocation());
			if(!pagos.isEmpty()){
				//pagosBean = (FcsPagosJGBean)pagos.get(0);
				Hashtable registro = (Hashtable) pagos.get(0);
				UtilidadesHash.set(hash,FacAbonoBean.C_OBSERVACIONES, registro.get(FcsPagosJGBean.C_NOMBRE).toString());
			}

			//preparamos las listas de abono
			Hashtable htLista = new Hashtable();
			Hashtable htTurnos = new Hashtable();
			Hashtable htGuardias = new Hashtable();
			Hashtable htEjg = new Hashtable();
			Hashtable htSoj = new Hashtable();
			Hashtable htMovimientos = new Hashtable();
			Hashtable htRetencionPersona = new Hashtable();
			
			FacLineaAbonoAdm lineaAbonoAdm = new FacLineaAbonoAdm (this.getUserBean(request));
			String idLineaAbono = ((Long)lineaAbonoAdm.getNuevoID(idInstitucion, idAbono)).toString();
			int idLinea = Integer.parseInt(idLineaAbono);
			
			//campos comunes
			htLista.put(FacLineaAbonoBean.C_IDINSTITUCION, idInstitucion);
			htLista.put(FacLineaAbonoBean.C_IDABONO, idAbono);
			htLista.put(FacLineaAbonoBean.C_IVA, "0");
			htLista.put(FacLineaAbonoBean.C_CANTIDAD, "1");
								
			//insertamos el abono
			resultado = abonoAdm.insert(hash);
			gc.setContador(contadorTablaHash,gc.getNuevoContador(contadorTablaHash));
			
			double importeNeto=0;
			double importeIVA=0;

			//Hay que llamar al procedimiento PROC_SIGA_ACTESTADOABONO para que cambie el estado.
//			String salidaPL[] = new String[2];
//			salidaPL = EjecucionPLs.ejecutarProcPROC_SIGA_ACTESTADOABONO(idInstitucion, idAbono, usr.getUserName());
//			if (!salidaPL[0].equals("0"))
//				throw new ClsExceptions ("Error en PL ejecutarProcPROC_SIGA_ACTESTADOABONO al egenrar el abono");

			if (Double.parseDouble(importeTurnos) != 0){
				htTurnos = this.prepararListaAbono(htLista, importeTurnos, idLinea, UtilidadesString.getMensajeIdioma(usr.getLanguage(),"factSJCS.abonos.literal.turnos"));
				idLinea++;
				lineaAbonoAdm.insert(htTurnos);
				importeNeto += new Double(importeTurnos).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue();
				importeIVA += ((new Double(importeTurnos).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue())*new Double((String)htLista.get(FacLineaAbonoBean.C_IVA)).doubleValue())/100;
			}
			if (Double.parseDouble(importeGuardias) != 0){
				htGuardias = this.prepararListaAbono(htLista, importeGuardias, idLinea, UtilidadesString.getMensajeIdioma(usr.getLanguage(),"factSJCS.abonos.literal.guardiasPresenciales"));
				idLinea++;
				lineaAbonoAdm.insert(htGuardias);
				importeNeto += new Double(importeGuardias).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue();
				importeIVA += ((new Double(importeGuardias).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue())*new Double((String)htLista.get(FacLineaAbonoBean.C_IVA)).doubleValue())/100;
			}
			if (Double.parseDouble(importeSoj) != 0){
				htSoj = this.prepararListaAbono(htLista, importeSoj, idLinea, UtilidadesString.getMensajeIdioma(usr.getLanguage(),"factSJCS.abonos.literal.SOJ"));
				idLinea++;
				lineaAbonoAdm.insert(htSoj);
				importeNeto += new Double(importeSoj).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue();
				importeIVA += ((new Double(importeSoj).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue())*new Double((String)htLista.get(FacLineaAbonoBean.C_IVA)).doubleValue())/100;
			}
			if (Double.parseDouble(importeEjg) != 0){
				htEjg = this.prepararListaAbono(htLista, importeEjg, idLinea, UtilidadesString.getMensajeIdioma(usr.getLanguage(),"factSJCS.abonos.literal.EJG"));
				idLinea++;
				lineaAbonoAdm.insert(htEjg);
				importeNeto += new Double(importeEjg).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue();
				importeIVA += ((new Double(importeEjg).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue())*new Double((String)htLista.get(FacLineaAbonoBean.C_IVA)).doubleValue())/100;
			}
			if (Double.parseDouble(importeMovimientos) != 0){
				htMovimientos = this.prepararListaAbono(htLista, importeMovimientos, idLinea, UtilidadesString.getMensajeIdioma(usr.getLanguage(),"factSJCS.abonos.literal.movimientos"));
				idLinea++;
				lineaAbonoAdm.insert(htMovimientos);
				importeNeto += new Double(importeMovimientos).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue();
				importeIVA += ((new Double(importeMovimientos).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue())*new Double((String)htLista.get(FacLineaAbonoBean.C_IVA)).doubleValue())/100;
			}
			if (irpfTotal != 0){
				Hashtable hAux = this.prepararListaAbono(htLista, ""+irpfTotal, idLinea, UtilidadesString.getMensajeIdioma(usr.getLanguage(),"factSJCS.abonos.literal.IRPFTotal"));
				idLinea++;
				lineaAbonoAdm.insert(hAux);
				importeNeto += new Double(irpfTotal).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue();
				importeIVA += ((new Double(irpfTotal).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue())*new Double((String)htLista.get(FacLineaAbonoBean.C_IVA)).doubleValue())/100;
			}
			
			//Retenciones por persona:
			//NOTA: Si el importe de retenciones es 0 no generamos el abono:
			if (Double.parseDouble(importeRetenciones) != 0) {
				importeRetencionesPersona = String.valueOf(Double.parseDouble(importeRetenciones));
				htRetencionPersona = this.prepararListaAbono(htLista, importeRetencionesPersona, idLinea, UtilidadesString.getMensajeIdioma(usr.getLanguage(),"factSJCS.abonos.literal.retenciones"));
				idLinea++;
				lineaAbonoAdm.insert(htRetencionPersona);
				importeNeto += new Double(importeRetencionesPersona).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue();
				importeIVA += ((new Double(importeRetencionesPersona).doubleValue() * new Double((String)htLista.get(FacLineaAbonoBean.C_CANTIDAD)).doubleValue())*new Double((String)htLista.get(FacLineaAbonoBean.C_IVA)).doubleValue())/100;
			}
			
			// RGG 
			  // Obtengo el abono insertado
			Hashtable htA = new Hashtable();
			htA.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
			htA.put(FacAbonoBean.C_IDABONO,idAbono);
			Vector vAbono = abonoAdm.selectByPK(htA);
			FacAbonoBean bAbono = null;
			if (vAbono!=null && vAbono.size()>0) {
			    bAbono = (FacAbonoBean) vAbono.get(0);
			}
			
			// RGG 29/05/2009 Cambio de funciones de abono
			bAbono.setImpTotal(new Double(importeNeto+importeIVA));
		    bAbono.setImpPendientePorAbonar(new Double(importeNeto+importeIVA));
		    bAbono.setImpTotalAbonado(new Double(0));
		    bAbono.setImpTotalAbonadoEfectivo(new Double(0));
		    bAbono.setImpTotalAbonadoPorBanco(new Double(0));
		    bAbono.setImpTotalIva(new Double(importeIVA));
		    bAbono.setImpTotalNeto(new Double(importeNeto));
		    if ((importeNeto+importeIVA)<=0) {
		        // pagado
		        bAbono.setEstado(new Integer(1));
		    } else {
		        if (bAbono.getIdCuenta()!=null) {
		            // pendiente pago banco
			        bAbono.setEstado(new Integer(5));
		        } else {
		            // pendiente pago caja
			        bAbono.setEstado(new Integer(6));
		        }
		    }
		    if (!abonoAdm.update(bAbono)){
			    throw new ClsExceptions("Error al actualizar estado e importes del abono: "+abonoAdm.getError());
			}

		    //Hay que deducir cobros si es true:
			if (this.deducirCobros(idInstitucion, idPersonaSoc, request, colegiadosMarcados))
				AbonosPagosAction.cantidadCompensada(idInstitucion, idAbono, this.getUserBean(request));
		} 
		catch(Exception e) {
			throw new ClsExceptions (e,"DatosGeneralesPagoAction.generarAbonos");
		}
	}
	
	
	/**
	 * Retorna el valor Double de una cadena o 0 si no se puede obtener un numero
	 * @return
	 */
	private Double tratarValor(String valor){
		try{
			return new Double(valor.replaceAll(",","."));
		}
		catch (Exception e){
			return new Double(0);
		}
	}
}
