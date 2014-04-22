/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 07-03-2005 - Inicio
 */
package com.siga.facturacion.action;

import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FacDisqueteCargosAdm;
import com.siga.beans.FacEstadoConfirmFactBean;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacion.form.ProgramarFacturacionForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * Clase action para la programacion de series de Facturacion.<br/>
 * Gestiona Borrar, modificar, nuevo
 */
public class ProgramarFacturacionAction extends MasterAction{
	/** 
	 *  Funcion que atiende la accion abrir. Muestra todas las facturas programadas que no estén confirmadas 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
					
			Integer idInstitucion	= this.getIDInstitucion(request);			
			Integer usuario			= this.getUserName(request);
			
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion;
			sWhere += " and (";
			sWhere += FacFacturacionProgramadaBean.C_FECHACONFIRMACION + " IS NULL";
			sWhere += " or ";
			sWhere += FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADAERRORES;
			sWhere += ") ";
			// RGG No mostrar las temporales
			//sWhere += "   and FAC_SERIEFACTURACION.tiposerie is null ";
			sWhere += "   and FAC_FACTURACIONPROGRAMADA.VISIBLE='S' ";
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPROGRAMACION};
			
			Vector vDatos = adm.selectDatosFacturacion(sWhere, orden);
			request.getSession().setAttribute("DATABACKUP", vDatos);	

			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
	return "abrir";
	}
	
	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action 	
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try
		{				
			Integer usuario = this.getUserName(request);
			HttpSession ses = request.getSession();
			ProgramarFacturacionForm form 	= (ProgramarFacturacionForm)formulario;
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			Long idSerieFacturacion = Long.valueOf((String)ocultos.elementAt(0));			
			Long idProgramacion 	= Long.valueOf((String)ocultos.elementAt(1));	
			Integer idInstitucion	= this.getIDInstitucion(request);
									
			String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion;
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion;
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + idProgramacion;
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};

			Vector vDatos = adm.selectDatosFacturacion(sWhere, orden);
			request.getSession().setAttribute("DATABACKUP", vDatos);	
			
			ses.setAttribute("ModoAction","editar");
			
			/** CR7 - Control de fechas de presentación y cargo en ficheros SEPA **/
			Hashtable hash = (Hashtable) vDatos.get(0);
			GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
			String fechaPresentacion = GstDate.getFormatedDateShort("es",(String)hash.get("FECHAPRESENTACION"));
			String fechaUnicaCargos = "";
			String fechaPrimerosRecibos = "";
			String fechaRecibosRecurrentes = "";
			String fechaRecibosCOR1 = "";
			String fechaRecibosB2B = "";
			String idInstitucionCGAE = String.valueOf(ClsConstants.INSTITUCION_CGAE);
			String habilesUnicaCargos = admParametros.getValor(idInstitucion.toString(), "FAC", "DIAS_HABILES_UNICA_CARGOS", "7");
			String habilesPrimerosRecibos = admParametros.getValor(idInstitucion.toString(), "FAC", "DIAS_HABILES_PRIMEROS_RECIBOS", "7");
			String habilesRecibosRecurrentes = admParametros.getValor(idInstitucion.toString(), "FAC", "DIAS_HABILES_RECIBOS_RECURRENTES", "4");
			String habilesRecibosCOR1 = admParametros.getValor(idInstitucion.toString(), "FAC", "DIAS_HABILES_RECIBOS_COR1", "3");
			String habilesRecibosB2B = admParametros.getValor(idInstitucion.toString(), "FAC", "DIAS_HABILES_RECIBOS_B2B", "3");
			
			if(hash.get("FECHACARGOUNICA") != null && !((String)hash.get("FECHACARGOUNICA")).equals("")){  //El radio button seleccionado es Unica
				fechaUnicaCargos = GstDate.getFormatedDateShort("es",(String)hash.get("FECHACARGOUNICA"));
				fechaPrimerosRecibos = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesPrimerosRecibos);
				fechaRecibosRecurrentes = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosRecurrentes);
				fechaRecibosCOR1 = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosCOR1);
				fechaRecibosB2B = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosB2B);
				request.setAttribute("radio","1"); // El radio seleccionado será Unica
				
			} else if(hash.get("FECHARECIBOSPRIMEROS") != null && !((String)hash.get("FECHARECIBOSPRIMEROS")).equals("")){ //El radio button seleccionado es Minimas
				fechaUnicaCargos = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesUnicaCargos);
				fechaPrimerosRecibos = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSPRIMEROS"));
				fechaRecibosRecurrentes = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSRECURRENTES"));
				fechaRecibosCOR1 = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSCOR1"));
				fechaRecibosB2B = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSB2B"));
				request.setAttribute("radio","0"); // El radio seleccionado será Minimas
			
			} else { //Son las programacioens antiguas, por tanto no tienen ni fecha de presentacion ni ningun tipo de fecha SEPA
				String fechaActual = GstDate.getHoyJsp(); // Obtengo la fecha actual
				fechaPresentacion = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE, fechaActual, "1"); // Fecha actual + 1

				fechaUnicaCargos = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesUnicaCargos);
				fechaPrimerosRecibos = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesPrimerosRecibos);
				fechaRecibosRecurrentes = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosRecurrentes);
				fechaRecibosCOR1 = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosCOR1);
				fechaRecibosB2B = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosB2B);
				request.setAttribute("radio","1"); // El radio seleccionado será Unica
			}
			
			request.setAttribute("fechaPresentacion",fechaPresentacion);
			request.setAttribute("fechaUnicaCargos",fechaUnicaCargos);
			request.setAttribute("fechaPrimerosRecibos",fechaPrimerosRecibos);
			request.setAttribute("fechaRecibosRecurrentes",fechaRecibosRecurrentes);
			request.setAttribute("fechaRecibosCOR1",fechaRecibosCOR1);
			request.setAttribute("fechaRecibosB2B",fechaRecibosB2B);
			
			request.setAttribute("habilesUnicaCargos",habilesUnicaCargos);
			request.setAttribute("habilesPrimerosRecibos",habilesPrimerosRecibos);
			request.setAttribute("habilesRecibosRecurrentes",habilesRecibosRecurrentes);
			request.setAttribute("habilesRecibosCOR1",habilesRecibosCOR1);
			request.setAttribute("habilesRecibosB2B",habilesRecibosB2B);			
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
	return "editar";

	}
	/** 
	 *  Funcion que atiende la accion buscarPor (Busca los valores de generar PDF y Envio facturas en la serie de facturacion)
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action 	
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try
		{				
			Integer usuario = this.getUserName(request);
			
			ProgramarFacturacionForm form 	= (ProgramarFacturacionForm)formulario;
			FacSerieFacturacionAdm adm = new FacSerieFacturacionAdm(this.getUserBean(request));
			
			Long idSerieFacturacion = form.getSerieFacturacion();			
			Integer idInstitucion	= this.getIDInstitucion(request);
									
			String sWhere=" where " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = " + idInstitucion;
			sWhere += " and ";
			sWhere += FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion;
			
			Vector vDatos = adm.select(sWhere);
			if (vDatos!=null && vDatos.size()>0) {
				FacSerieFacturacionBean b = (FacSerieFacturacionBean) vDatos.get(0);
				request.setAttribute("generarPDF",b.getGenerarPDF());	
				request.setAttribute("envioFactura",b.getEnvioFactura());
				request.setAttribute("idTipoPlantilla",b.getIdTipoPlantillaMail());
				request.setAttribute("idInstitucionSerie",b.getIdInstitucion());
			}
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
	return "actualizaDatos";

	}
	/** 
	 *  Funcion que atiende la accion nuevo. Permite dar de alta una nueva serie de facturación 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			HttpSession ses = request.getSession();
			request.getSession().setAttribute("DATABACKUP", null);	
			ses.setAttribute("ModoAction","editar");
			Integer idInstitucion	= this.getIDInstitucion(request);
			String idInstitucionCGAE = String.valueOf(ClsConstants.INSTITUCION_CGAE);
			
			/** CR7 - Control de fechas de presentación y cargo en ficheros SEPA **/
			GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
			String habilesUnicaCargos = admParametros.getValor(idInstitucionCGAE, "FAC", "DIAS_HABILES_UNICA_CARGOS", "7");
			String habilesPrimerosRecibos = admParametros.getValor(idInstitucionCGAE, "FAC", "DIAS_HABILES_PRIMEROS_RECIBOS", "7");
			String habilesRecibosRecurrentes = admParametros.getValor(idInstitucionCGAE, "FAC", "DIAS_HABILES_RECIBOS_RECURRENTES", "4");
			String habilesRecibosCOR1 = admParametros.getValor(idInstitucionCGAE, "FAC", "DIAS_HABILES_RECIBOS_COR1", "3");
			String habilesRecibosB2B = admParametros.getValor(idInstitucionCGAE, "FAC", "DIAS_HABILES_RECIBOS_B2B", "3");
			
			String fechaActual = GstDate.getHoyJsp(); // Obtengo la fecha actual
			String fechaPresentacion = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE, fechaActual, "1"); // Fecha actual + 1
			
			String fechaUnicaCargos = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesUnicaCargos);
			String fechaPrimerosRecibos = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesPrimerosRecibos);
			String fechaRecibosRecurrentes = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosRecurrentes);
			String fechaRecibosCOR1 = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosCOR1);
			String fechaRecibosB2B = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE,fechaPresentacion,habilesRecibosB2B);
			
			request.setAttribute("radio","1"); // El radio seleccionado será Unica
			request.setAttribute("fechaPresentacion",fechaPresentacion);
			request.setAttribute("fechaUnicaCargos",fechaUnicaCargos);
			request.setAttribute("fechaPrimerosRecibos",fechaPrimerosRecibos);
			request.setAttribute("fechaRecibosRecurrentes",fechaRecibosRecurrentes);
			request.setAttribute("fechaRecibosCOR1",fechaRecibosCOR1);
			request.setAttribute("fechaRecibosB2B",fechaRecibosB2B);
			
			request.setAttribute("habilesUnicaCargos",habilesUnicaCargos);
			request.setAttribute("habilesPrimerosRecibos",habilesPrimerosRecibos);
			request.setAttribute("habilesRecibosRecurrentes",habilesRecibosRecurrentes);
			request.setAttribute("habilesRecibosCOR1",habilesRecibosCOR1);
			request.setAttribute("habilesRecibosB2B",habilesRecibosB2B);			
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 
		
		return "nuevo";
	}
	
	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;	
		Integer usuario = this.getUserName(request);
		ProgramarFacturacionForm form = (ProgramarFacturacionForm)formulario;
		Long idSerieFacturacion;
		String salida = "";
		HttpSession ses = request.getSession();
		
		try
		{	
			tx = this.getUserBean(request).getTransaction();			
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			{	// Comprobamos si existe ese nombre para la institucion. Debe ser unico
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDINSTITUCION, this.getIDInstitucion(request));
				UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_DESCRIPCION,   form.getDescripcionProgramacion());
				Vector v = adm.select(h);
				if ((v != null) && (v.size() > 0)) {
					throw new SIGAException(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "facturacion.seriesFacturacion.error.descripcionDuplicada"));
				}
			}
			
			tx.begin();	

			FacFacturacionProgramadaBean bean = getDatos(form, request);

			// Obtenemos el idSerieFacturacion
			bean.setIdProgramacion(adm.getNuevoID(bean));
			
			// tratamiento de estados de la programacion 
			bean = adm.tratamientoEstadosProgramacion(bean);
			
			/** JPT - Control de fechas de presentación y cargo en ficheros SEPA **/
			String idInstitucion = this.getIDInstitucion(request).toString();
			String fechaEntrega = form.getFechaPresentacion();
			bean.setFechaPresentacion(GstDate.getApplicationFormatDate("en", fechaEntrega));
			
			String fechaUnica="", fechaRecibosPrimeros="", fechaRecibosRecurrentes="", fechaRecibosCOR1="", fechaRecibosB2B="";
			if (form.getFechaTipoUnica().equals("1")) {
				fechaUnica = form.getFechaCargoUnica();
				bean.setFechaCargoUnica(GstDate.getApplicationFormatDate("en", fechaUnica));
				
			} else { 
				fechaRecibosPrimeros = form.getFechaRecibosPrimeros();
				fechaRecibosRecurrentes = form.getFechaRecibosRecurrentes();
				fechaRecibosCOR1 = form.getFechaRecibosCOR1();
				fechaRecibosB2B = form.getFechaRecibosB2B();
				bean.setFechaRecibosPrimeros(GstDate.getApplicationFormatDate("en", fechaRecibosPrimeros));
				bean.setFechaRecibosRecurrentes(GstDate.getApplicationFormatDate("en", fechaRecibosRecurrentes));
				bean.setFechaRecibosCOR1(GstDate.getApplicationFormatDate("en", fechaRecibosCOR1));
				bean.setFechaRecibosB2B(GstDate.getApplicationFormatDate("en", fechaRecibosB2B));
			}			
			
			// Controlar que las fechas cumplen los dias habiles introducidos en parametros generales
			FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(this.getUserBean(request));	
			if (!admDisqueteCargos.controlarFechasFicheroBancario(idInstitucion, fechaEntrega, fechaUnica, fechaRecibosPrimeros, fechaRecibosRecurrentes, fechaRecibosCOR1, fechaRecibosB2B, form.getFechaTipoUnica())) {
				throw new SIGAException("fecha.error.valida");
			}				
			
			// Comprobaciones antes de confirmacion 
			Vector ret = adm.comprobarRecursosProgramacion(bean);
			
			// tratamiento del mensaje
			String mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.ok"); 			
			if (ret.size()>0) {
				mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.avisos");
				for (int i=0;i<ret.size();i++) {
					mensaje += "\n" + UtilidadesString.getMensajeIdioma(this.getUserBean(request),(String)ret.get(i));
				}
			} 
			
			salida = exitoModal(mensaje,request);

			// RGG 20/11/2007 añadimos los campos nuevos de contabilidad
			FacSerieFacturacionAdm admS = new FacSerieFacturacionAdm(this.getUserBean(request));
			Hashtable ht = new Hashtable();
			ht.put(FacFacturacionProgramadaBean.C_IDINSTITUCION, bean.getIdInstitucion());
			ht.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, bean.getIdSerieFacturacion());
			Vector v = admS.selectByPK(ht);
			if (v!=null && v.size()>0) {
			    FacSerieFacturacionBean bSF = (FacSerieFacturacionBean) v.get(0);
			    bean.setConfDeudor(bSF.getConfigDeudor());
			    bean.setConfIngresos(bSF.getConfigIngresos());
			    bean.setCtaClientes(bSF.getCuentaClientes());
			    bean.setCtaIngresos(bSF.getCuentaIngresos());
			}

			
			// Insertamos el nuevo registro.
			if(!adm.insert(bean)){
				throw new SIGAException (adm.getError());
			}			
			tx.commit();
			ses.setAttribute("ModoAction","editar");
			
		} catch (SIGAException e) {
			String sms = e.getLiteral();
			if (sms == null || sms.equals("")) {
				sms = "messages.general.error";
			}
			
			throwExcp(sms, new String[] {"modulo.facturacion"}, e, tx);				

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}
		return salida;				
	}
	
	/** 
	 *  Funcion que atiende la accion modificarr. Permiote modificar los datos del registro seleccionado 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;	
		Integer usuario = this.getUserName(request);
		ProgramarFacturacionForm form = (ProgramarFacturacionForm)formulario;
		Long idSerieFacturacion;
		String salida="";
		
		try
		{	
			tx = this.getUserBean(request).getTransaction();			
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			FacFacturacionProgramadaBean bean = getDatos(form, request);
			Enumeration en = ((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
			Hashtable hash = (Hashtable)en.nextElement();
			
			{	// Comprobamos si existe ese nombre para la institucion. Debe ser unico
				String where="";
				where =" where "+FacFacturacionProgramadaBean.C_IDINSTITUCION+"="+this.getIDInstitucion(request)+
			       " and "+ FacFacturacionProgramadaBean.C_DESCRIPCION+"='"+form.getDescripcionProgramacion()+"'"+
				   " and "+FacFacturacionProgramadaBean.C_IDPROGRAMACION +" not in (select "+FacFacturacionProgramadaBean.C_IDPROGRAMACION +
                       "																from "+FacFacturacionProgramadaBean.T_NOMBRETABLA+
					   "																where "+FacFacturacionProgramadaBean.C_IDINSTITUCION+"="+this.getIDInstitucion(request)+
				       "                                                                  and "+ FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+"="+bean.getIdSerieFacturacion()+
					   " 															      and "+FacFacturacionProgramadaBean.C_IDPROGRAMACION+"="+UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDPROGRAMACION)+")";																																		                                                                      
				
				Vector v = adm.select(where);
				if ((v != null) && (v.size() > 0)) {
					throw new SIGAException(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "facturacion.seriesFacturacion.error.descripcionDuplicada"));
				}
			}
			
			tx.begin();			
			
			// Recogemos la hashOriginal
			
			
			
			bean.setOriginalHash(hash);
			bean.setIdProgramacion(UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDPROGRAMACION));

			// para que no se pierda la hora de programacion (Creacion)
			bean.setFechaProgramacion(null);
			
			/** JPT - Control de fechas de presentación y cargo en ficheros SEPA **/
			String idInstitucion = this.getIDInstitucion(request).toString();
			String fechaEntrega = form.getFechaPresentacion();
			bean.setFechaPresentacion(GstDate.getApplicationFormatDate("en", fechaEntrega));
			
			String fechaUnica="", fechaRecibosPrimeros="", fechaRecibosRecurrentes="", fechaRecibosCOR1="", fechaRecibosB2B="";
			if (form.getFechaTipoUnica().equals("1")) {
				fechaUnica = form.getFechaCargoUnica();
				bean.setFechaCargoUnica(GstDate.getApplicationFormatDate("en", fechaUnica));
				
			} else { 
				fechaRecibosPrimeros = form.getFechaRecibosPrimeros();
				fechaRecibosRecurrentes = form.getFechaRecibosRecurrentes();
				fechaRecibosCOR1 = form.getFechaRecibosCOR1();
				fechaRecibosB2B = form.getFechaRecibosB2B();
				bean.setFechaRecibosPrimeros(GstDate.getApplicationFormatDate("en", fechaRecibosPrimeros));
				bean.setFechaRecibosRecurrentes(GstDate.getApplicationFormatDate("en", fechaRecibosRecurrentes));
				bean.setFechaRecibosCOR1(GstDate.getApplicationFormatDate("en", fechaRecibosCOR1));
				bean.setFechaRecibosB2B(GstDate.getApplicationFormatDate("en", fechaRecibosB2B));
			}			
			
			// Controlar que las fechas cumplen los dias habiles introducidos en parametros generales
			FacDisqueteCargosAdm admDisqueteCargos = new FacDisqueteCargosAdm(this.getUserBean(request));	
			if (!admDisqueteCargos.controlarFechasFicheroBancario(idInstitucion, fechaEntrega, fechaUnica, fechaRecibosPrimeros, fechaRecibosRecurrentes, fechaRecibosCOR1, fechaRecibosB2B, form.getFechaTipoUnica())) {
				throw new SIGAException("fecha.error.valida");
			}					
			
			// tratamiento de estados de la programacion 
			bean = adm.tratamientoEstadosProgramacion(bean);
			
			// Comprobaciones antes de confirmacion 
			Vector ret = adm.comprobarRecursosProgramacion(bean);
			
			// tratamiento del mensaje
			String mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.ok"); 			
			if (ret.size()>0) {
				mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.avisos");
				for (int i=0;i<ret.size();i++) {
					mensaje += "\n" + UtilidadesString.getMensajeIdioma(this.getUserBean(request),(String)ret.get(i));
				}
			} 
			
			salida = exitoModal(mensaje,request);
			
			// Modificamos el registro.
			if(!adm.update(bean)){
				throw new SIGAException (adm.getError());
			}			
			tx.commit();
			
		} catch (SIGAException e) {
			String sms = e.getLiteral();
			if (sms == null || sms.equals("")) {
				sms = "messages.general.error";
			}
			
			throwExcp(sms, new String[] {"modulo.facturacion"}, e, tx);					
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}
		return salida;		
	}	
	
	
	/** 
	 *  Funcion que atiende la accion borrar. borra un registro de Fac_FacturacionProgramada
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;		
		try
		{				
			tx = this.getUserBean(request).getTransaction();	
			
			Integer usuario = this.getUserName(request);
			ProgramarFacturacionForm form = (ProgramarFacturacionForm)formulario;
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idSerieFacturacion = Long.valueOf((String)ocultos.elementAt(0));			
			Long idProgramacion 	= Long.valueOf((String)ocultos.elementAt(1));		
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			Hashtable clavesFacturacionProgramada = new Hashtable();
			UtilidadesHash.set (clavesFacturacionProgramada, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion);
			UtilidadesHash.set (clavesFacturacionProgramada, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
			UtilidadesHash.set (clavesFacturacionProgramada, FacFacturacionProgramadaBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			
			tx.begin();			
			if(!adm.delete(clavesFacturacionProgramada)){
				throw new SIGAException (adm.getError());
			}			
			
			// borramos tambien el fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p = new ReadProperties ("SIGA.properties");
			String pathFichero 		= p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
			String nombreFichero = "";
			nombreFichero = "LOG_CONFIRM_FAC_"+ this.getIDInstitucion(request)+"_"+idSerieFacturacion+"_"+idProgramacion+".log.xls"; 
			File fichero = new File(pathFichero+sBarra+this.getIDInstitucion(request)+sBarra+nombreFichero);
			if (fichero.exists()) {
				fichero.delete();
			}
			
			tx.commit();			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}
		return exitoRefresco("messages.deleted.success",request);			
	}
	
	
	/**
	 * Funcion para obtener los datos a insertar en BD
	 * @param form -  Action Form asociado a este Action
	 * @param request - objeto llamada HTTP 
	 * @return FacFacturacionProgramadaBean contiene los datos a insertar en BD
	 * @throws SIGAException
	 */
	protected FacFacturacionProgramadaBean getDatos(ProgramarFacturacionForm form, HttpServletRequest request) throws SIGAException {
		FacFacturacionProgramadaBean bean = null;
		String idTipoPlantillaMail = "";
		try {
			bean = new FacFacturacionProgramadaBean();
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			bean.setIdInstitucion(this.getIDInstitucion(request));
			bean.setIdSerieFacturacion(form.getSerieFacturacion());
			bean.setFechaInicioProductos(form.getFechaInicialProducto());
			bean.setFechaFinProductos(form.getFechaFinalProducto());
			bean.setFechaInicioServicios(form.getFechaInicialServicio());
			bean.setFechaFinServicios(form.getFechaFinalServicio());
			String sFechaProgramacion = UtilidadesString.formatoFecha(new Date(),"yyyy/MM/dd HH:mm:ss"); 
			bean.setFechaProgramacion(sFechaProgramacion);
			bean.setArchivarFact("0");
			bean.setLocked("0");
			
			bean.setVisible("S");
			
			// tratamos las fechas con minutos y segundos
			String aux = "";
			String auxfechaCargo="";

			aux = form.getFechaPrevistaGeneracion().substring(0,form.getFechaPrevistaGeneracion().length()-9) + " " + ((form.getHorasGeneracion().trim().equals(""))?"00":form.getHorasGeneracion())+":"+((form.getMinutosGeneracion().trim().equals(""))?"00":form.getMinutosGeneracion())+":00";
			bean.setFechaPrevistaGeneracion(aux);			

			if (form.getFechaPrevistaConfirmacion()!=null && !form.getFechaPrevistaConfirmacion().equals("")) {
				aux = form.getFechaPrevistaConfirmacion().substring(0,form.getFechaPrevistaConfirmacion().length()-9) + " " + ((form.getHorasConfirmacion().trim().equals(""))?"00":form.getHorasConfirmacion())+":"+((form.getMinutosConfirmacion().trim().equals(""))?"00":form.getMinutosConfirmacion())+":00";
				bean.setFechaPrevistaConfirmacion(aux);			
			}else{
				bean.setFechaPrevistaConfirmacion("");		
			}
			
			if (form.getFechaCargo()!=null && !form.getFechaCargo().equals("")) {							
				bean.setFechaCargo(form.getFechaCargo());
			}else{
				bean.setFechaCargo("");
			}

			
			bean.setGenerarPDF((form.getGenerarPDF()!=null)?"1":"0");			
			bean.setEnvio((form.getEnviarFacturas()!=null)?"1":"0");

			if(form.getIdTipoPlantillaMail()!=null && !form.getIdTipoPlantillaMail().equals("")){
				idTipoPlantillaMail = form.getIdTipoPlantillaMail().split(",")[0];
				bean.setIdTipoPlantillaMail(Integer.parseInt(idTipoPlantillaMail));
				bean.setIdTipoEnvios(1);
			} else{
				bean.setIdTipoPlantillaMail(null);
				bean.setIdTipoEnvios(null);
			}
			
			if (bean.getEnvio().equals("1"))
				bean.setGenerarPDF("1");
			bean.setDescripcion(form.getDescripcionProgramacion());
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, null);
		}
		return bean;
	}
	
		
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)	throws ClsExceptions, SIGAException {
		return this.abrir(mapping, formulario, request, response);
	}
	
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try
		{				
			Integer usuario = this.getUserName(request);
			HttpSession ses = request.getSession();
			ProgramarFacturacionForm form 	= (ProgramarFacturacionForm)formulario;
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			Long idSerieFacturacion = Long.valueOf((String)ocultos.elementAt(0));			
			Long idProgramacion 	= Long.valueOf((String)ocultos.elementAt(1));	
			Integer idInstitucion	= this.getIDInstitucion(request);
									
			String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion;
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion;
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + idProgramacion;
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
			
			Vector vDatos = adm.selectDatosFacturacion(sWhere, orden);
			
			/** CR7 - Control de fechas de presentación y cargo en ficheros SEPA **/
			Hashtable hash = (Hashtable) vDatos.get(0);
			
			String fechaPresentacion = GstDate.getFormatedDateShort("es",(String)hash.get("FECHAPRESENTACION"));
			String fechaUnicaCargos = "";
			String fechaPrimerosRecibos = "";
			String fechaRecibosRecurrentes = "";
			String fechaRecibosCOR1 = "";
			String fechaRecibosB2B = "";
			
			if(hash.get("FECHACARGOUNICA") != null && !((String)hash.get("FECHACARGOUNICA")).equals("")){  //El radio button seleccionado es Unica
				fechaUnicaCargos = GstDate.getFormatedDateShort("es",(String)hash.get("FECHACARGOUNICA"));
				request.setAttribute("radio","1"); // El radio seleccionado será Unica
				
			} else if(hash.get("FECHARECIBOSPRIMEROS") != null && !((String)hash.get("FECHARECIBOSPRIMEROS")).equals("")){ //El radio button seleccionado es Minimas
				fechaPrimerosRecibos = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSPRIMEROS"));
				fechaRecibosRecurrentes = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSRECURRENTES"));
				fechaRecibosCOR1 = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSCOR1"));
				fechaRecibosB2B = GstDate.getFormatedDateShort("es",(String)hash.get("FECHARECIBOSB2B"));
				request.setAttribute("radio","0"); // El radio seleccionado será Minimas
			
			} else { //Son las programacioens antiguas, por tanto no tienen ni fecha de presentacion ni ningun tipo de fecha SEPA
				request.setAttribute("radio","1"); // El radio seleccionado será Unica
			}
			
			request.setAttribute("fechaPresentacion",fechaPresentacion);
			request.setAttribute("fechaUnicaCargos",fechaUnicaCargos);
			request.setAttribute("fechaPrimerosRecibos",fechaPrimerosRecibos);
			request.setAttribute("fechaRecibosRecurrentes",fechaRecibosRecurrentes);
			request.setAttribute("fechaRecibosCOR1",fechaRecibosCOR1);
			request.setAttribute("fechaRecibosB2B",fechaRecibosB2B);		
			
			request.setAttribute("habilesUnicaCargos","");
			request.setAttribute("habilesPrimerosRecibos","");
			request.setAttribute("habilesRecibosRecurrentes","");
			request.setAttribute("habilesRecibosCOR1","");
			request.setAttribute("habilesRecibosB2B","");			

			request.getSession().setAttribute("DATABACKUP", vDatos);	

			ses.setAttribute("ModoAction","ver");
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
	return "ver";

	}
}
