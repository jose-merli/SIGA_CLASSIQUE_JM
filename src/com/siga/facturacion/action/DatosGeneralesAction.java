/*
 * VERSIONES:
 * yolanda.garcia - 11-01-2005 - Creación
 */

/**
 * <p>Clase que gestiona las series de facturación.</p>
 */

package com.siga.facturacion.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.AdmContadorAdm;
import com.siga.beans.AdmContadorBean;
import com.siga.beans.FacBancoInstitucionAdm;
import com.siga.beans.FacFormaPagoSerieAdm;
import com.siga.beans.FacPlantillaFacturacionAdm;
import com.siga.beans.FacPlantillaFacturacionBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.FacSufijoAdm;
import com.siga.beans.FacSufijoBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacion.form.DatosGeneralesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class DatosGeneralesAction extends MasterAction{

	/**
	 * Muestra la pestanha de Datos Generales de la pantalla de mantenimiento.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");					
			
			AdmContadorAdm admContador = new AdmContadorAdm(user);
			FacPlantillaFacturacionAdm admPlantillaFacturacion = new FacPlantillaFacturacionAdm(user);
			FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(user);
			FacBancoInstitucionAdm admBancoInstitucion = new FacBancoInstitucionAdm(user);
		
			/** ---------- 1. OBTIENE LA SERIE DE FACTURACION ----------*/
			String idInstitucion = user.getLocation();
			String idSerieFacturacion = (String)request.getSession().getAttribute("idSerieFacturacion");
			String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = " + idInstitucion +
								" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +
									(idSerieFacturacion!=null && !idSerieFacturacion.equals("") ? " = " + idSerieFacturacion : " IS NULL ");
			Vector<FacSerieFacturacionBean> vSeriesFacturacion = admSerieFacturacion.select(where);
												
			Hashtable<String,Object> hContador = new Hashtable<String,Object>();			
			Vector<AdmContadorBean> vContador = null;
			if (vSeriesFacturacion!=null && vSeriesFacturacion.size()>0) {
				FacSerieFacturacionBean beanSerieFacturacion = vSeriesFacturacion.get(0);
				request.setAttribute("beanSerieFacturacion", beanSerieFacturacion);
				
				/** ---------- 2. OBTIENE LA PLANTILLA DE FACTURACION ----------*/
				Integer iPlantilla = beanSerieFacturacion.getIdPlantilla();				
				where = " WHERE " + FacPlantillaFacturacionBean.T_NOMBRETABLA + "." + FacPlantillaFacturacionBean.C_IDINSTITUCION + " = " + idInstitucion +
							" AND " + FacPlantillaFacturacionBean.T_NOMBRETABLA + "." + FacPlantillaFacturacionBean.C_IDPLANTILLA + " = " + iPlantilla;
								
				Vector<FacPlantillaFacturacionBean> vPlantilla = admPlantillaFacturacion.select(where);
				if (vPlantilla!=null && vPlantilla.size()>0) {
					FacPlantillaFacturacionBean beanPlantillaFacturacion = vPlantilla.get(0);
					request.setAttribute("beanPlantillaFacturacion", beanPlantillaFacturacion);
				}
				
				/** ---------- 3. OBTIENE LAS FORMAS DE PAGO ----------*/
				idSerieFacturacion = String.valueOf(beanSerieFacturacion.getIdSerieFacturacion());
				Vector<Hashtable<String, Object>> vFormasPago = admSerieFacturacion.obtenerFormasPago(idInstitucion, idSerieFacturacion);
				request.setAttribute("vFormasPago", vFormasPago);
				
				/** ---------- 4. OBTIENE EL CONTADOR ----------*/
				hContador.put(AdmContadorBean.C_IDINSTITUCION, idInstitucion);
				hContador.put(AdmContadorBean.C_IDCONTADOR, beanSerieFacturacion.getIdContador());
				vContador = admContador.selectByPK(hContador);
				
			} else {
				hContador.put(AdmContadorBean.C_IDINSTITUCION,idInstitucion);
				hContador.put(AdmContadorBean.C_GENERAL, "1"); // Se obtiene el general de la institucion
				vContador = admContador.select(hContador);
			}									
			if (vContador!=null && vContador.size()>0) {
				AdmContadorBean beanContador = vContador.get(0);
				request.setAttribute("beanContador", beanContador);
			}
			
			/** ---------- 5. OBTIENE LOS BANCOS DEL COLEGIO Y LA RELACION CON LA SERIE DE FACTURACION ----------*/			
			Vector<Hashtable<String, Object>> vBancosInstitucion = admBancoInstitucion.obtenerBancosSerieFacturacion(idInstitucion,idSerieFacturacion);
			request.setAttribute("vBancosInstitucion", vBancosInstitucion);
			
			/** ---------- 6. OBTIENE LOS SUFIJOS ----------*/	
			FacSufijoAdm admSufijo = new FacSufijoAdm(user);
			Hashtable<String,Object> claves = new Hashtable<String,Object>();
			UtilidadesHash.set(claves, FacSufijoBean.C_IDINSTITUCION, idInstitucion);
			Vector<FacSufijoBean> vsufijos = admSufijo.select(claves);
			
			List<FacSufijoBean> lSufijos= new ArrayList<FacSufijoBean>();
			for (int numSufijo=0; numSufijo<vsufijos.size(); numSufijo++){
				FacSufijoBean sufijosBean = vsufijos.get(numSufijo);
				lSufijos.add(sufijosBean);
			}
	
			request.setAttribute("lSufijos", lSufijos);
			request.getSession().setAttribute("idSerieFacturacion", idSerieFacturacion);
			
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		} 	

		return "inicio";
	}
	
	  /**
	   * Ejecuta un sentencia INSERT en la Base de Datos para series de facturación.
	   *    
	   * @param  mapping - Mapeo de los struts
	   * @param  formulario -  Action Form asociado a este Action
	   * @param  request - objeto llamada HTTP 
	   * @param  response - objeto respuesta HTTP
	   * 
	   * @return String que indicará la siguiente acción a llevar a cabo.
	   *   
	   * @exception  ClsExceptions  En cualquier caso de error
	   */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		try {
			DatosGeneralesForm formDatosGenerales = (DatosGeneralesForm) formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");					
								
			AdmContadorAdm admContador = new AdmContadorAdm(user);
			FacBancoInstitucionAdm admBancoInstitucion = new FacBancoInstitucionAdm(user);
			FacFormaPagoSerieAdm admFormaPagoSerie = new FacFormaPagoSerieAdm(user);			
			FacSerieFacturacionAdm admSerieFacturacion =  new FacSerieFacturacionAdm(user);			
			GenParametrosAdm admParametros = new GenParametrosAdm(user);
			
			/** ---------- 1. VERIFICA EXISTENCIA DEL NOMBRE ABREVIADO DE LA SERIE DE FACTURACION ----------*/			
			String idInstitucion = user.getLocation();
			String nombreAbreviado = formDatosGenerales.getNombreAbreviado();
			String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." +  FacSerieFacturacionBean.C_IDINSTITUCION + " = " + idInstitucion +
				 				" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO + " = '" + nombreAbreviado + "'";
			Vector<FacSerieFacturacionBean> vSerieFacturacion = admSerieFacturacion.select(where);
			
			if (vSerieFacturacion!=null && vSerieFacturacion.size()>0) {
				throw new SIGAException("facturacion.datosGenerales.literal.mensajeExisteNombreAbreviadoSerFac");
			}
			
			FacSerieFacturacionBean beanSerieFacturacion = new FacSerieFacturacionBean();
			
			/** ---------- 2. OBTIENE EL CONTADOR GENERICO ---------- */
			Hashtable<String,Object> hContador = new Hashtable<String,Object>();
			hContador.put(AdmContadorBean.C_IDINSTITUCION, idInstitucion);
			hContador.put(AdmContadorBean.C_GENERAL, "1");
			Vector<AdmContadorBean> vContador = admContador.select(hContador);
			if (vContador!=null && vContador.size()>0) {
				AdmContadorBean b1 = vContador.get(0);
				beanSerieFacturacion.setIdContador(b1.getIdContador());
				
			} else if (vContador!=null && vContador.size()>1) {
				throw new SIGAException("Messages.Facturacion.NoContadorGenerico");
				
			} else {
				throw new SIGAException("Messages.Facturacion.MasDeUnContadorGenerico");
			}
			
			/** ---------- 3. OBTIENE NUEVO IDSERIEFACTURACION ---------- */
			String nuevoidSerieFacturacion = admSerieFacturacion.getNuevoId(idInstitucion);
			
			/** ---------- 4. OBTIENE PARAMETROS FAC ---------- */
			beanSerieFacturacion.setCuentaClientes(admParametros.getValor(idInstitucion, "FAC", "CONTABILIDAD_CLIENTES", ""));
			beanSerieFacturacion.setCuentaIngresos(admParametros.getValor(idInstitucion, "FAC", "CONTABILIDAD_VENTAS", ""));
			
			/** ---------- 5. INSERTA FAC_SERIEFACTURACION ---------- */
			String visible = formDatosGenerales.getVisible();
			String envioFacturas = formDatosGenerales.getEnvioFacturas(); 				
			
			beanSerieFacturacion.setIdInstitucion(Integer.valueOf(idInstitucion));
			beanSerieFacturacion.setIdSerieFacturacion(Long.valueOf(nuevoidSerieFacturacion));
			beanSerieFacturacion.setIdPlantilla(formDatosGenerales.getIdPlantilla());
			beanSerieFacturacion.setDescripcion(formDatosGenerales.getDescripcion());
			beanSerieFacturacion.setObservaciones(formDatosGenerales.getObservaciones());
			beanSerieFacturacion.setVisible(visible!=null && !visible.equals("") ? visible : "S"); // Por defecto visible
			beanSerieFacturacion.setNombreAbreviado(nombreAbreviado);				
			beanSerieFacturacion.setEnvioFactura(envioFacturas!=null && !envioFacturas.equals("") ? "1" : "0");								
			if (beanSerieFacturacion.getEnvioFactura().equals("1")) { 
				beanSerieFacturacion.setGenerarPDF("1"); 
			} else {
				String generarPDF = formDatosGenerales.getGenerarPDF();
				beanSerieFacturacion.setGenerarPDF(generarPDF!=null && !generarPDF.equals("") ? "1" : "0");
			}

			beanSerieFacturacion.setConfigDeudor(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO);
			beanSerieFacturacion.setConfigIngresos(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO);
			
			String idTipoPlantillaMail = formDatosGenerales.getIdTipoPlantillaMail();
			if (idTipoPlantillaMail!=null && !idTipoPlantillaMail.equals("")){
				idTipoPlantillaMail = idTipoPlantillaMail.split(",")[0];
				beanSerieFacturacion.setIdTipoPlantillaMail(Integer.parseInt(idTipoPlantillaMail));
				beanSerieFacturacion.setIdTipoEnvios(1);
			} 			
			
			if(formDatosGenerales.getIdSerieFacturacionPrevia()!=null && !formDatosGenerales.getIdSerieFacturacionPrevia().equals("")){
				beanSerieFacturacion.setIdSerieFacturacionPrevia(Long.valueOf(formDatosGenerales.getIdSerieFacturacionPrevia()));
			}
			
			tx = user.getTransaction();
			tx.begin();
			if (!admSerieFacturacion.insert(beanSerieFacturacion)) {
				throw new SIGAException("messages.inserted.error");
			}
				
			/** ---------- 6. INSERTA FAC_SERIEFACTURACION_BANCO ---------- */
			String ids = request.getParameter("ids");					
			String idsufijo;
			
			if (ids==null || ids.equals("")) {
				throw new SIGAException("messages.inserted.error");
			}
			
			// Inserto los recibidos
			StringTokenizer st = new StringTokenizer(ids,"%");
			while (st.hasMoreElements()) {
				
				String idBancoConSuf = (String)st.nextElement();
				
				String idBanco = idBancoConSuf.split(",")[0];
							
				//Si la serie no tiene ningún sufijo asociado
				if (idBancoConSuf.endsWith(","))
					idsufijo=null;
				else
					idsufijo = idBancoConSuf.split(",")[1];
				
				if (!admBancoInstitucion.insertaBancosSerieFacturacion(idInstitucion, nuevoidSerieFacturacion, idBanco, idsufijo)) {
					throw new SIGAException("messages.inserted.error");
				}
			}
			
			/** ---------- 7. INSERTA FAC_FORMAPAGOSERIE ---------- */										
			String[] arrayFormasPagoAuto = formDatosGenerales.getFormaPagoAutomática();
			if (arrayFormasPagoAuto==null) {
				throw new SIGAException("messages.inserted.error");
			}
				
			for (int i=0; i<arrayFormasPagoAuto.length; i++) {
				String sFormaPago = arrayFormasPagoAuto[i];							
				
				if (sFormaPago!=null && !sFormaPago.equals("") && !sFormaPago.equals("-1")) {
					Hashtable<String, Object> hFormaPago = new Hashtable<String, Object>();
					hFormaPago.put(FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
					hFormaPago.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION, nuevoidSerieFacturacion);
					hFormaPago.put(FacSerieFacturacionBean.C_IDFORMAPAGO, sFormaPago);
					
					if (!admFormaPagoSerie.insert(hFormaPago)) {
						throw new SIGAException("messages.inserted.error");
					}
				}
			}							
			
			tx.commit();
				
			// Almacenamos en sesion el registro de la serie de facturación
			Hashtable<String,Object> backupSerFac = new Hashtable<String,Object>();
			UtilidadesHash.set(backupSerFac, "IDINSTITUCION", idInstitucion);
			UtilidadesHash.set(backupSerFac, "IDSERIEFACTURACION",nuevoidSerieFacturacion);
			UtilidadesHash.set(backupSerFac, "IDPLANTILLA",formDatosGenerales.getIdPlantilla());
			UtilidadesHash.set(backupSerFac, "IDTIPOPLANTILLAMAIL", idTipoPlantillaMail);
			UtilidadesHash.set(backupSerFac, "DESCRIPCION",formDatosGenerales.getDescripcion());
			UtilidadesHash.set(backupSerFac, "NOMBREABREVIADO",formDatosGenerales.getNombreAbreviado());
			UtilidadesHash.set(backupSerFac, "OBSERVACIONES", formDatosGenerales.getObservaciones());
			UtilidadesHash.set(backupSerFac, "VISIBLE", formDatosGenerales.getVisible());
			request.getSession().setAttribute("DATABACKUP", backupSerFac);
			
			request.getSession().setAttribute("idSerieFacturacion", nuevoidSerieFacturacion);
			request.getSession().setAttribute("accion", "editar");
			
		} catch (Exception e) { 
		   throwExcp("messages.general.error", new String[] {"modulo.facturacion.asignacionConceptos"}, e, tx); 
		} 
		
		return this.exitoRefresco("messages.inserted.success", request);	
	}

	/**
	   * Ejecuta un sentencia UPDATE en la Base de Datos para series de facturación.
	   *    
	   * @param  mapping - Mapeo de los struts
	   * @param  formulario -  Action Form asociado a este Action
	   * @param  request - objeto llamada HTTP 
	   * @param  response - objeto respuesta HTTP
	   * 
	   * @return String que indicará la siguiente acción a llevar a cabo.
	   *   
	   * @exception  ClsExceptions  En cualquier caso de error
	   */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");	
			String idInstitucion = user.getLocation();
			tx = user.getTransaction();
			tx.begin();
			DatosGeneralesForm formDatosGenerales = (DatosGeneralesForm) formulario;
			
			FacFormaPagoSerieAdm admFormaPagoSerie = new FacFormaPagoSerieAdm(user);
			FacSerieFacturacionAdm admSerieFacturacion =  new FacSerieFacturacionAdm(user);
			AdmContadorAdm admContador = new AdmContadorAdm(user);
			FacBancoInstitucionAdm admBancoInstitucion = new FacBancoInstitucionAdm(user);
						
			String sIdSerieFacturacion = formDatosGenerales.getIdSerieFacturacion().toString();
			Hashtable<String,Object> hSerieFacturacion =  new Hashtable<String,Object>();
			hSerieFacturacion.put(FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
			hSerieFacturacion.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION, sIdSerieFacturacion);
			/** ---------- 1. VERIFICA EXISTENCIA DE LA SERIE DE FACTURACION ----------*/
			Vector<FacSerieFacturacionBean> vSerieFacturacion = admSerieFacturacion.selectByPK(hSerieFacturacion);
			
			if (vSerieFacturacion==null || vSerieFacturacion.size()<1) {
				throw new SIGAException("messages.updated.error");
			}
						
			String idTipoPlantillaMail = null;
			FacSerieFacturacionBean beanSerieFacturacionBeanOld = vSerieFacturacion.get(0);
			
			if (!beanSerieFacturacionBeanOld.getNombreAbreviado().equals(formDatosGenerales.getNombreAbreviado())) {
				/** ---------- 2. VERIFICA EXISTENCIA DEL NOMBRE ABREVIADO DE LA SERIE DE FACTURACION ----------*/			
				String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." +  FacSerieFacturacionBean.C_IDINSTITUCION + " = " + idInstitucion +
					 				" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO + " = '" + formDatosGenerales.getNombreAbreviado() + "'";
				vSerieFacturacion = admSerieFacturacion.select(where);
				
				if (vSerieFacturacion!=null && vSerieFacturacion.size()>0) {
					throw new SIGAException("facturacion.datosGenerales.literal.mensajeExisteNombreAbreviadoSerFac");
				}				
			}
						
			Hashtable<String,Object> hashOld =  new Hashtable<String,Object>();				
			hashOld.put(FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
			hashOld.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION, sIdSerieFacturacion);
			hashOld.put(FacSerieFacturacionBean.C_IDPLANTILLA, beanSerieFacturacionBeanOld.getIdPlantilla());
			hashOld.put(FacSerieFacturacionBean.C_NOMBREABREVIADO, beanSerieFacturacionBeanOld.getNombreAbreviado());
			hashOld.put(FacSerieFacturacionBean.C_DESCRIPCION, beanSerieFacturacionBeanOld.getDescripcion());
			hashOld.put(FacSerieFacturacionBean.C_OBSERVACIONES, beanSerieFacturacionBeanOld.getObservaciones());
			hashOld.put(FacSerieFacturacionBean.C_VISIBLE, beanSerieFacturacionBeanOld.getVisible());
				
			Hashtable<String,Object> hashNew = new Hashtable<String,Object>();
			hashNew.put(FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
			hashNew.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION, sIdSerieFacturacion);
			hashNew.put(FacSerieFacturacionBean.C_IDPLANTILLA, formDatosGenerales.getIdPlantilla());
			hashNew.put(FacSerieFacturacionBean.C_NOMBREABREVIADO, formDatosGenerales.getNombreAbreviado());
			hashNew.put(FacSerieFacturacionBean.C_DESCRIPCION, formDatosGenerales.getDescripcion());
			hashNew.put(FacSerieFacturacionBean.C_OBSERVACIONES, formDatosGenerales.getObservaciones());
			hashNew.put(FacSerieFacturacionBean.C_VISIBLE, formDatosGenerales.getVisible());
			
			String envio = (formDatosGenerales.getEnvioFacturas()!=null && !formDatosGenerales.getEnvioFacturas().equals("") ? "1" : "0");
			hashNew.put(FacSerieFacturacionBean.C_ENVIOFACTURA, envio);
			
			if (envio.equals("1")) { 
				hashNew.put(FacSerieFacturacionBean.C_GENERARPDF,"1");
			} else {
				hashNew.put(FacSerieFacturacionBean.C_GENERARPDF, (formDatosGenerales.getGenerarPDF()!=null && !formDatosGenerales.getGenerarPDF().equals("") ? "1" : "0"));
			}
			
			if (formDatosGenerales.getIdTipoPlantillaMail()!=null && !formDatosGenerales.getIdTipoPlantillaMail().equals("")) {
				idTipoPlantillaMail = formDatosGenerales.getIdTipoPlantillaMail().split(",")[0];
				hashNew.put(FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL, idTipoPlantillaMail);
				hashNew.put(FacSerieFacturacionBean.C_IDTIPOENVIOS, 1);
			} else {
				hashNew.put(FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL, "");
				hashNew.put(FacSerieFacturacionBean.C_IDTIPOENVIOS, "");
			}
			
			if (formDatosGenerales.getIdSerieFacturacionPrevia()!=null && !formDatosGenerales.getIdSerieFacturacionPrevia().equals("")) {
				hashNew.put(FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA, formDatosGenerales.getIdSerieFacturacionPrevia());
			} else {
				hashNew.put(FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA, "");
			}
			
			boolean eliminarContador = false;
			// RGG 10/09/2007 tratamiento del contador
			if (formDatosGenerales.getConfigurarContador()!=null && formDatosGenerales.getConfigurarContador().equals("on")) {
				if (formDatosGenerales.getContadorExistente().equals("")) {
					
					/** ---------- 3. OBTIENE NUEVO CONTADOR ----------*/
					// cogemos el nuevo. (de los tres campos)						
					String nuevoIdContador = "FAC_" + sIdSerieFacturacion + "_" + admContador.obtenerNuevoMax(idInstitucion);

					// Lo damos de alta en contadores y lo relacionamos.
					Hashtable<String,Object> htContador = new Hashtable<String,Object>();
					htContador.put(AdmContadorBean.C_IDINSTITUCION,idInstitucion);
					htContador.put(AdmContadorBean.C_IDCONTADOR,nuevoIdContador);
					htContador.put(AdmContadorBean.C_NOMBRE,beanSerieFacturacionBeanOld.getNombreAbreviado());
					htContador.put(AdmContadorBean.C_DESCRIPCION,beanSerieFacturacionBeanOld.getNombreAbreviado());
					htContador.put(AdmContadorBean.C_MODIFICABLECONTADOR,"1");
					htContador.put(AdmContadorBean.C_MODO,"0");
					htContador.put(AdmContadorBean.C_CONTADOR,formDatosGenerales.getContador_nuevo());
					htContador.put(AdmContadorBean.C_PREFIJO,formDatosGenerales.getPrefijo_nuevo());
					htContador.put(AdmContadorBean.C_SUFIJO,formDatosGenerales.getSufijo_nuevo());
					htContador.put(AdmContadorBean.C_LONGITUDCONTADOR,(formDatosGenerales.getContador_nuevo().length()<=5) ? "5" : "10");
					//htContador.put(AdmContadorBean.C_FECHARECONFIGURACION,NULL);
					htContador.put(AdmContadorBean.C_RECONFIGURACIONCONTADOR,"0");
					//htContador.put(AdmContadorBean.C_RECONFIGURACIONPREFIJO,NULL);
					//htContador.put(AdmContadorBean.C_RECONFIGURACIONSUFIJO,NULL);
					htContador.put(AdmContadorBean.C_IDMODULO,ClsConstants.IDMODULOFACTURACION);//Modulo facturacion.
					htContador.put(AdmContadorBean.C_IDTABLA,"FAC_FACTURA");
					htContador.put(AdmContadorBean.C_IDCAMPOCONTADOR,"NUMEROFACTURA");
					htContador.put(AdmContadorBean.C_IDCAMPOPREFIJO,"NUMEROFACTURA");
					htContador.put(AdmContadorBean.C_IDCAMPOSUFIJO,"NUMEROFACTURA");
					htContador.put(AdmContadorBean.C_GENERAL,"0");
					htContador.put(AdmContadorBean.C_FECHAMODIFICACION,"SYSDATE");
			        htContador.put(AdmContadorBean.C_USUMODIFICACION,(new Integer(user.getUserName())));
			        htContador.put(AdmContadorBean.C_FECHACREACION,"SYSDATE");
			        htContador.put(AdmContadorBean.C_USUCREACION,(new Integer(user.getUserName())));			
			        
			        /** ---------- 4. INSERTA ADM_CONTADOR ----------*/
			        if (!admContador.insert(htContador)){
			        	throw new SIGAException("messages.updated.error");
					}
			        
					hashNew.put(FacSerieFacturacionBean.C_IDCONTADOR, nuevoIdContador);
					eliminarContador = true;
					
				} else {
					// cogemos el de la lista
					hashNew.put(FacSerieFacturacionBean.C_IDCONTADOR, formDatosGenerales.getContadorExistente());
					eliminarContador = true;
				}
				
			} else {
				// se deja el que había
				hashNew.put(FacSerieFacturacionBean.C_IDCONTADOR, formDatosGenerales.getIdContador());
			}
					
			/** ---------- 5. ACTUALIZA FAC_SERIEFACTURACION ----------*/
			if (!admSerieFacturacion.update(hashNew, hashOld)) {
				throw new SIGAException("messages.updated.error");
			}

			/** ---------- 6. ELIMINA ADM_CONTADOR ----------*/
			// Borramos el contador que estaba relacionado si no es general ni tiene otras relaciones
			if (eliminarContador && !admContador.borrarContadorLibre(idInstitucion, formDatosGenerales.getIdContador())) {
				throw new SIGAException("messages.updated.error");
			}

			/** ---------- 7. ELIMINA FAC_SERIEFACTURACION_BANCO ----------*/					
			// borro los bancos asociados a la serie de facturacion
			if (!admBancoInstitucion.borrarBancosSerieFacturacion(idInstitucion, sIdSerieFacturacion)) {
				throw new SIGAException("messages.updated.error");
			}
			
			// RGG 05/09/2007 Inserto las relaciones con bancos
			String ids = request.getParameter("ids");
			String idsufijo;
			
			if (ids==null || ids.equals("")) {
				throw new SIGAException("messages.updated.error");
			}
			
			// Inserto los recibidos
			StringTokenizer st = new StringTokenizer(ids,"%");
			while (st.hasMoreElements()) {
				
				String idBancoConSuf = (String)st.nextElement();
				
				String idBanco =idBancoConSuf.split(",")[0];
							
				//Si la serie no tiene ningún sufijo asociado
				if(idBancoConSuf.endsWith(","))
					idsufijo=null;
				else
					idsufijo = idBancoConSuf.split(",")[1];

				/** ---------- 8. INSERTA FAC_SERIEFACTURACION_BANCO ----------*/	
				if (!admBancoInstitucion.insertaBancosSerieFacturacion(idInstitucion, sIdSerieFacturacion, idBanco, idsufijo)) {
					throw new SIGAException("messages.updated.error");
				}
			}		
			
			/** ---------- 9. OBTIENE LAS FORMAS DE PAGO DE LA SERIE DE FACTURACION ----------*/
			// Obtengo el IDSERVICIOSINSTITUCION y los campos SOLICITARBAJA y SOLICITARALTA si no fueron seleccionados
			Vector<Hashtable<String, Object>> vFormasPago = admSerieFacturacion.obtenerFormasPago(idInstitucion, sIdSerieFacturacion);
	    				
			if (vFormasPago!=null && vFormasPago.size()>0) {				
		    	for (int contFormasPago=0; contFormasPago<vFormasPago.size(); contFormasPago++){
		    		Hashtable<String, Object> hFormaPago = vFormasPago.get(contFormasPago);
		    				
		    		/** ---------- 10. ELIMINA FAC_FORMAPAGOSERIE ----------*/
	    			if (!admFormaPagoSerie.delete(hFormaPago)) {
	    				throw new SIGAException("messages.updated.error");
	    			}
				}
			}
	    				
	    	String[] arrayFormasPagoAuto = formDatosGenerales.getFormaPagoAutomática();
	    	if (arrayFormasPagoAuto==null) {
	    		throw new SIGAException("messages.updated.error");
	    	}
	    		
			for (int i=0; i<arrayFormasPagoAuto.length; i++) {
				String sFormaPago = arrayFormasPagoAuto[i];
				
				if (sFormaPago!=null && !sFormaPago.equals("") && !sFormaPago.equals("-1")) {
					Hashtable<String, Object> hFormaPago = new Hashtable<String, Object>();
					hFormaPago.put(FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
					hFormaPago.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION, sIdSerieFacturacion);
					hFormaPago.put(FacSerieFacturacionBean.C_IDFORMAPAGO, sFormaPago);
		
					/** ---------- 11. INSERTA FAC_FORMAPAGOSERIE ---------- */
					if (!admFormaPagoSerie.insert(hFormaPago)) {
						throw new SIGAException("messages.updated.error");
					}
				}					
			}	
				
			tx.commit();
			
		    // Almacenamos en sesion el registro de la serie de facturacion
			Hashtable<String,Object> backupSerFac = new Hashtable<String,Object>();
			UtilidadesHash.set(backupSerFac, "IDINSTITUCION", idInstitucion);
			UtilidadesHash.set(backupSerFac, "IDSERIEFACTURACION",sIdSerieFacturacion);
			UtilidadesHash.set(backupSerFac, "IDPLANTILLA",formDatosGenerales.getIdPlantilla());
			UtilidadesHash.set(backupSerFac, "IDTIPOPLANTILLAMAIL", idTipoPlantillaMail);
			UtilidadesHash.set(backupSerFac, "DESCRIPCION",formDatosGenerales.getDescripcion());
			UtilidadesHash.set(backupSerFac, "NOMBREABREVIADO",formDatosGenerales.getNombreAbreviado());
			UtilidadesHash.set(backupSerFac, "OBSERVACIONES", formDatosGenerales.getObservaciones());
			UtilidadesHash.set(backupSerFac, "VISIBLE", formDatosGenerales.getVisible());
			request.getSession().setAttribute("DATABACKUP",backupSerFac);
			
		} catch (Exception e) { 
		   throwExcp("messages.general.error", new String[] {"modulo.facturacion.asignacionConceptos"}, e, tx); 
		} 
		
		return this.exito("messages.updated.success", request);				
	}
}