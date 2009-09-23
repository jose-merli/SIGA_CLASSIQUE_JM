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
import javax.transaction.UserTransaction;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FacEstadoConfirmFactBean;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.facturacion.form.ProgramarFacturacionForm;
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
			request.getSession().setAttribute("DATABACKUP", null);
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
			

		}
		catch (Exception e) {
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
			
		}
		catch (Exception e) {
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
			ReadProperties p = new ReadProperties ("SIGA.properties");
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

			aux = form.getFechaPrevistaGeneracion().substring(0,form.getFechaPrevistaGeneracion().length()-9) + " " + ((form.getHorasGeneracion().trim().equals(""))?"00":form.getHorasGeneracion())+":"+((form.getMinutosGeneracion().trim().equals(""))?"00":form.getMinutosGeneracion())+":00";
			bean.setFechaPrevistaGeneracion(aux);			

			if (form.getFechaPrevistaConfirmacion()!=null && !form.getFechaPrevistaConfirmacion().equals("")) {
				aux = form.getFechaPrevistaConfirmacion().substring(0,form.getFechaPrevistaConfirmacion().length()-9) + " " + ((form.getHorasConfirmacion().trim().equals(""))?"00":form.getHorasConfirmacion())+":"+((form.getMinutosConfirmacion().trim().equals(""))?"00":form.getMinutosConfirmacion())+":00";
				bean.setFechaPrevistaConfirmacion(aux);			
			}

			bean.setGenerarPDF((form.getGenerarPDF()!=null)?"1":"0");			
			bean.setEnvio((form.getEnviarFacturas()!=null)?"1":"0");			
			if (bean.getEnvio().equals("1")) bean.setGenerarPDF("1");

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
}
