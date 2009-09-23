/*
 * VERSIONES:
 * yolanda.garcia - 11-01-2005 - Creación
 */

/**
 * <p>Clase que gestiona las series de facturación.</p>
 */

package com.siga.facturacion.action;

import java.util.Hashtable;
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
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.facturacion.form.DatosGeneralesForm;

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
		
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			String idSerieFacturacion=(String)request.getSession().getAttribute("idSerieFacturacion");
		
			FacSerieFacturacionAdm admSerieFac = new FacSerieFacturacionAdm(this.getUserBean(request));
			String Where = " where ";
			Where += FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+idInstitucion;
				  	
			if (idSerieFacturacion!=null){
				Where +=" and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion;
			}else{
				Where +=" and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION+" is null";
			}
		
			Vector datosSerie = admSerieFac.select(Where);
			request.setAttribute("datosSerie", datosSerie);
			
			
			AdmContadorAdm admCon = new AdmContadorAdm(this.getUserBean(request));
			if (datosSerie!=null && datosSerie.size()>0) { 
				FacSerieFacturacionBean b = (FacSerieFacturacionBean)datosSerie.get(0);
				Hashtable ht = new Hashtable();
				ht.put(AdmContadorBean.C_IDINSTITUCION,idInstitucion);
				ht.put(AdmContadorBean.C_IDCONTADOR,b.getIdContador());
				Vector datosContador = admCon.selectByPK(ht);
				request.setAttribute("datosContador", datosContador);
			} else {
				// Se obtiene el general de la institucion
				Hashtable ht = new Hashtable();
				ht.put(AdmContadorBean.C_IDINSTITUCION,idInstitucion);
				ht.put(AdmContadorBean.C_GENERAL,"1");
				Vector datosContador = admCon.select(ht);
				request.setAttribute("datosContador", datosContador);
			}
			

			FacBancoInstitucionAdm admBancoFac = new FacBancoInstitucionAdm(this.getUserBean(request));
			Vector datosBancos = admBancoFac.obtenerBancosSerieFacturacion(idInstitucion,idSerieFacturacion);
			request.setAttribute("bancosInstitucion", datosBancos);

			request.getSession().setAttribute("idSerieFacturacion", idSerieFacturacion);
		} 
		  catch (Exception e) { 
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
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
		
			DatosGeneralesForm formDGen = (DatosGeneralesForm) formulario;
			String nombreAbreviado = formDGen.getNombreAbreviado();
		
			
			FacSerieFacturacionAdm admFac =  new FacSerieFacturacionAdm(this.getUserBean(request));
			String where1 = " Where ";
			where1 += FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
				 	" and "+
					FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_NOMBREABREVIADO+"='"+nombreAbreviado+"'";
			Vector datosNomAbr = admFac.select(where1);
			
			if (datosNomAbr==null || datosNomAbr.size()==0)
			{
				UserTransaction tx = null;
				tx = ((UsrBean)request.getSession().getAttribute("USRBEAN")).getTransaction();
						
				FacSerieFacturacionBean beanFac = new FacSerieFacturacionBean();
		
				String where2 = 	" Where ";
				where2 += FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+idInstitucion;
				Vector vec = admFac.selectTabla_2(where2);
				Hashtable hashMaximo = (Hashtable)vec.get(0);
				Long idSerieFacturacion = UtilidadesHash.getLong(hashMaximo, FacSerieFacturacionBean.C_IDSERIEFACTURACION);
				
				Integer idPlantilla = formDGen.getIdPlantilla();
				String descripcion = formDGen.getDescripcion();
				String observaciones = formDGen.getObservaciones();
														
				// RGG 10/09/2007 obtenemos el contador genérico
				AdmContadorAdm admCont = new AdmContadorAdm(this.getUserBean(request));
				Hashtable ht1 = new Hashtable();
				ht1.put(AdmContadorBean.C_IDINSTITUCION,idInstitucion);
				ht1.put(AdmContadorBean.C_GENERAL,"1");
				Vector v1 = admCont.select(ht1);
				if (v1!=null && v1.size()>0) {
					AdmContadorBean b1 = (AdmContadorBean) v1.get(0);
					beanFac.setIdContador(b1.getIdContador());
				} else if (v1!=null && v1.size()>1) {
					throw new SIGAException("Messages.Facturacion.NoContadorGenerico");
				} else {
					throw new SIGAException("Messages.Facturacion.MasDeUnContadorGenerico");
				}
				
				beanFac.setIdInstitucion(Integer.valueOf(idInstitucion));
				beanFac.setIdSerieFacturacion(idSerieFacturacion);
				beanFac.setIdPlantilla(idPlantilla);
				beanFac.setDescripcion(descripcion);
				beanFac.setObservaciones(observaciones);
				beanFac.setNombreAbreviado(nombreAbreviado);
				String envioFacturas = formDGen.getEnvioFacturas(); 
				beanFac.setEnvioFactura((((envioFacturas != null) && (!envioFacturas.equals("")))?"1":"0"));
				String generarPDF = formDGen.getGenerarPDF(); 
				beanFac.setGenerarPDF((((generarPDF != null) && (!generarPDF.equals("")))?"1":"0"));
				if (beanFac.getEnvioFactura().equals("1")) beanFac.setGenerarPDF("1");

				beanFac.setConfigDeudor(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO);
				beanFac.setConfigIngresos(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO);
				
				GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
				beanFac.setCuentaClientes(paramAdm.getValor(idInstitucion, "FAC", "CONTABILIDAD_CLIENTES", ""));
				beanFac.setCuentaIngresos(paramAdm.getValor(idInstitucion, "FAC", "CONTABILIDAD_VENTAS", ""));
				
				tx.begin();
				boolean result = admFac.insert(beanFac);
				if (result)
				{

					// RGG 05/09/2007 Inserto las relaciones con bancos
					String ids = request.getParameter("ids");
					FacBancoInstitucionAdm admBancos= new FacBancoInstitucionAdm(this.getUserBean(request));

					if (ids!=null && !ids.equals("")) {
						// Inserto los recibidos
						StringTokenizer st = new StringTokenizer(ids,"%");
						while (st.hasMoreElements()) {
							String idBanco = (String)st.nextElement();
							admBancos.insertaBancosSerieFacturacion(idInstitucion, idSerieFacturacion.toString(),idBanco);
						}
					}

					
					request.setAttribute("idInstitucion",idInstitucion);
					request.setAttribute("mensaje","messages.inserted.success");
					request.setAttribute("idSerieFacturacion", idSerieFacturacion.toString());
					tx.commit();
				}
				else
				{	
					request.setAttribute("idInstitucion",idInstitucion);
					request.setAttribute("mensaje","messages.inserted.error");
					request.setAttribute("idSerieFacturacion", idSerieFacturacion.toString());
					tx.rollback();
				}
					
				// Almacenamos en sesion el registro de la serie de facturación
				Hashtable backupSerFac = new Hashtable();
				backupSerFac.put("IDINSTITUCION",idInstitucion);
				backupSerFac.put("IDSERIEFACTURACION",idSerieFacturacion);
				backupSerFac.put("IDPLANTILLA",formDGen.getIdPlantilla());
				backupSerFac.put("DESCRIPCION",formDGen.getDescripcion());
				backupSerFac.put("NOMBREABREVIADO",formDGen.getNombreAbreviado());
				backupSerFac.put("OBSERVACIONES",formDGen.getObservaciones());
				request.getSession().setAttribute("DATABACKUP",backupSerFac);
				
				request.getSession().setAttribute("idSerieFacturacion", idSerieFacturacion.toString());
			}
			else
			{
//				request.setAttribute("mensaje","facturacion.datosGenerales.literal.mensajeExisteNombreAbreviadoSerFac");
				return exito("facturacion.datosGenerales.literal.mensajeExisteNombreAbreviadoSerFac", request);
			}
		} 
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		} 
		
		// Refrescamos la pantalla con los valores insertados
		request.setAttribute("sinrefresco","sinrefresco");
		return "exitoInsercion";
//		return "exito";
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
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
				
			DatosGeneralesForm formDGen = (DatosGeneralesForm) formulario;
			String nombreAbreviado = formDGen.getNombreAbreviado();
			Long idSerieFac = formDGen.getIdSerieFacturacion();
		
			FacSerieFacturacionAdm admFac =  new FacSerieFacturacionAdm(this.getUserBean(request));
			String where1 = " Where ";
			where1 += FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
				 	" and "+
					FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_NOMBREABREVIADO+"='"+nombreAbreviado+"'";
			Vector datosNomAbr = admFac.select(where1);
			
			String nombreAbreviadoActual = "";
			Long idSerieFacturacionActual = null;
			if (datosNomAbr!=null && datosNomAbr.size()!=0){
			  FacSerieFacturacionBean facSerieFacturacionBean = (FacSerieFacturacionBean) datosNomAbr.get(0);
			  if (facSerieFacturacionBean!=null){
				nombreAbreviadoActual = facSerieFacturacionBean.getNombreAbreviado();
				idSerieFacturacionActual = facSerieFacturacionBean.getIdSerieFacturacion();
			  }
			}
			
			if (datosNomAbr==null || datosNomAbr.size()==0 || (nombreAbreviado.equals(nombreAbreviadoActual) && idSerieFac.equals(idSerieFacturacionActual)))
			{
				String idSerieFacturacion=(String)request.getSession().getAttribute("idSerieFacturacion");
				
				UserTransaction tx = null;
				tx = ((UsrBean)request.getSession().getAttribute("USRBEAN")).getTransaction();
							
				Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
				Hashtable hashNew = new Hashtable();
				
				hashNew.put(FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
				hashNew.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION, idSerieFacturacion);
				hashNew.put(FacSerieFacturacionBean.C_IDPLANTILLA, formDGen.getIdPlantilla());
				hashNew.put(FacSerieFacturacionBean.C_NOMBREABREVIADO, nombreAbreviado);
				hashNew.put(FacSerieFacturacionBean.C_DESCRIPCION, formDGen.getDescripcion());
				hashNew.put(FacSerieFacturacionBean.C_OBSERVACIONES, formDGen.getObservaciones());
				hashNew.put(FacSerieFacturacionBean.C_FECHAMODIFICACION, "sysdate");
				String envioFacturas = formDGen.getEnvioFacturas(); 
				String envio = (((envioFacturas != null) && (!envioFacturas.equals("")))?"1":"0");
				hashNew.put(FacSerieFacturacionBean.C_ENVIOFACTURA, envio);
				String generarPDF = formDGen.getGenerarPDF(); 
				hashNew.put(FacSerieFacturacionBean.C_GENERARPDF, (((generarPDF != null) && (!generarPDF.equals("")))?"1":"0"));
				if (envio.equals("1")) hashNew.put(FacSerieFacturacionBean.C_GENERARPDF,"1");

				boolean eliminarContador = false;
				// RGG 10/09/2007 tratamiento del contador
				if (formDGen.getConfigurarContador()!=null && formDGen.getConfigurarContador().equals("on")) {
					if (formDGen.getContadorExistente().equals("")) {
						
						// cogemos el nuevo. (de los tres campos)
						AdmContadorAdm admC = new AdmContadorAdm(this.getUserBean(request));
						String nuevoIdContador = "FAC_"+idSerieFacturacion+"_"+admC.obtenerNuevoMax(idInstitucion);

						// Lo damos de alta en contadores y lo relacionamos.
						Hashtable htContador = new Hashtable();
						htContador.put(AdmContadorBean.C_IDINSTITUCION,idInstitucion);
						htContador.put(AdmContadorBean.C_IDCONTADOR,nuevoIdContador);
						htContador.put(AdmContadorBean.C_NOMBRE,nombreAbreviado);
						htContador.put(AdmContadorBean.C_DESCRIPCION,nombreAbreviado);
						htContador.put(AdmContadorBean.C_MODIFICABLECONTADOR,"1");
						htContador.put(AdmContadorBean.C_MODO,"0");
						htContador.put(AdmContadorBean.C_CONTADOR,formDGen.getContador_nuevo());
						htContador.put(AdmContadorBean.C_PREFIJO,formDGen.getPrefijo_nuevo());
						htContador.put(AdmContadorBean.C_SUFIJO,formDGen.getSufijo_nuevo());
						htContador.put(AdmContadorBean.C_LONGITUDCONTADOR,(formDGen.getContador_nuevo().length()<=5)?"5":"10");
						//htContador.put(AdmContadorBean.C_FECHARECONFIGURACION,NULL);
						htContador.put(AdmContadorBean.C_RECONFIGURACIONCONTADOR,"0");
						//htContador.put(AdmContadorBean.C_RECONFIGURACIONPREFIJO,NULL);
						//htContador.put(AdmContadorBean.C_RECONFIGURACIONSUFIJO,NULL);
						htContador.put(AdmContadorBean.C_IDTABLA,"FAC_FACTURA");
						htContador.put(AdmContadorBean.C_IDCAMPOCONTADOR,"NUMEROFACTURA");
						htContador.put(AdmContadorBean.C_IDCAMPOPREFIJO,"NUMEROFACTURA");
						htContador.put(AdmContadorBean.C_IDCAMPOSUFIJO,"NUMEROFACTURA");
						htContador.put(AdmContadorBean.C_GENERAL,"0");
						if (!admC.insert(htContador)){
							throw new ClsExceptions("Error al insertar el nuevo contador");
						}
						hashNew.put(FacSerieFacturacionBean.C_IDCONTADOR, nuevoIdContador);

						eliminarContador = true;
						
					} else {
						// cogemos el de la lista
						hashNew.put(FacSerieFacturacionBean.C_IDCONTADOR, formDGen.getContadorExistente());

						eliminarContador = true;

					}
				} else {
					// se deja el que había
					hashNew.put(FacSerieFacturacionBean.C_IDCONTADOR, formDGen.getIdContador());
				}
				
				tx.begin();
				boolean result = admFac.update(hashNew,hashOld);
				if (result)
				{

					// RGG 12/09/2007 
					// Borramos el contador que estaba relacionado si no es general ni tiene 
					// otras relaciones
					if (eliminarContador) {
						AdmContadorAdm admC = new AdmContadorAdm(this.getUserBean(request));
						admC.borrarContadorLibre(idInstitucion, formDGen.getIdContador());
					}

					// RGG 05/09/2007 Inserto las relaciones con bancos
					String ids = request.getParameter("ids");
					FacBancoInstitucionAdm admBancos= new FacBancoInstitucionAdm(this.getUserBean(request));

					// borro los que hubiera (Modificacion)
					admBancos.borrarBancosSerieFacturacion(idInstitucion, idSerieFacturacion.toString());
					
					if (ids!=null && !ids.equals("")) {
						// Inserto los recibidos
						StringTokenizer st = new StringTokenizer(ids,"%");
						while (st.hasMoreElements()) {
							String idBanco = (String)st.nextElement();
							admBancos.insertaBancosSerieFacturacion(idInstitucion, idSerieFacturacion.toString(),idBanco);
						}
					}
					
					request.setAttribute("mensaje","messages.updated.success");
					tx.commit();
				}
				else
				{
					request.setAttribute("mensaje","messages.updated.error");
					tx.rollback();
				}
			
			    // Almacenamos en sesion el registro de la serie de facturacion
				Hashtable backupSerFac = new Hashtable();
				backupSerFac.put("IDINSTITUCION",idInstitucion);
				backupSerFac.put("IDSERIEFACTURACION",idSerieFacturacion);
				backupSerFac.put("IDPLANTILLA",formDGen.getIdPlantilla());
				backupSerFac.put("DESCRIPCION",formDGen.getDescripcion());
				backupSerFac.put("NOMBREABREVIADO",formDGen.getNombreAbreviado());
				request.getSession().setAttribute("DATABACKUP",backupSerFac);
			}
			else
			{
				request.setAttribute("mensaje","facturacion.datosGenerales.literal.mensajeExisteNombreAbreviadoSerFac");
			}
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		} 
		  
		// Refrescamos la pantalla con los valores modificados
		//request.setAttribute("sinrefresco","sinrefresco");
		return this.exitoRefresco("messages.updated.success",request);	
	}

}