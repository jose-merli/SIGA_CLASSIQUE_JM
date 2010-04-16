/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 14-03-2005 - Inicio
 */
package com.siga.facturacion.action;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FacEstadoConfirmFactAdm;
import com.siga.beans.FacEstadoConfirmFactBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.facturacion.Facturacion;
import com.siga.facturacion.form.ConfirmarFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;


/**
 * Clase action para confirmar la facturacion.<br/>
 * Gestiona abrir y confirmar Facturas
 * @version david.sanchezp: cambios para pedir y tener en cuenta la fecha de cargo.
 */
public class ConfirmarFacturacionAction extends MasterAction{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

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
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);	
					
				}else if (accion.equalsIgnoreCase("confirmarFactura")){
					mapDestino = confirmarFactura(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("descargaLog")){
					mapDestino = descargaLog(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("archivarFactura")){
					mapDestino = archivarFactura(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("confirmacionInmediata")){
					mapDestino = confirmacionInmediata(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("generarFacturaSolo")){
					mapDestino = generarFacturaSolo(mapping, miForm, request, response);
				}

				else {
					return super.executeInternal(mapping, formulario, request, response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 				
			{ 				
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }			    
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}			
			
		 }catch (SIGAException es) { 
		    throw es; 
		 }catch (Exception e) { 
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		 } 
		 
		   return mapping.findForward(mapDestino);   	
	}
	
	/** 
	 *  Funcion que atiende la accion buscar.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ConfirmarFacturacionForm miForm = null;

		try {
			miForm = (ConfirmarFacturacionForm) formulario;

			//Debo calcular si necesita o no la fecha de cargo por cada registro enviado.
			Integer idInstitucion	= this.getIDInstitucion(request);			
			String fechaInicial 	= "01/01/2000";
			
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FacEstadoConfirmFactAdm admEstados = new FacEstadoConfirmFactAdm(user);
			Hashtable htEstados = admEstados.getEstadosConfirmacionFacturacion(user.getLanguage());
			request.setAttribute("ESTADOS",htEstados);
			
			//Este select interno si devuelve un numero > 0 querra decir que debo pedir la fecha de cargo
			String selectInterno = "SELECT count(*) FROM "+FacFacturaBean.T_NOMBRETABLA+" fac "+
								   " WHERE fac."+FacFacturaBean.C_IDINSTITUCION+" = facProg."+FacFacturacionProgramadaBean.C_IDINSTITUCION+
								   " AND fac."+FacFacturaBean.C_IDSERIEFACTURACION+" = facProg."+FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+
								   " AND fac."+FacFacturaBean.C_IDPROGRAMACION+" = facProg."+FacFacturacionProgramadaBean.C_IDPROGRAMACION+
								   " AND fac."+FacFacturaBean.C_IDFORMAPAGO+"="+ClsConstants.TIPO_FORMAPAGO_FACTURA;
								   
			String select = "SELECT facProg.*, "+
							"("+selectInterno+") AS FECHACARGO,"+
							" serieFac."+FacSerieFacturacionBean.C_NOMBREABREVIADO+
							" FROM "+FacFacturacionProgramadaBean.T_NOMBRETABLA+" facProg, "+
								    FacSerieFacturacionBean.T_NOMBRETABLA+" serieFac"+
							" WHERE facProg." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion+
							//" and facProg."+FacFacturacionProgramadaBean.C_FECHACONFIRMACION + " IS NULL"+
							" and facProg."+FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL "+
							" and facProg."+FacFacturacionProgramadaBean.C_FECHAREALGENERACION + ">= TO_DATE ('" + fechaInicial + "', 'DD/MM/YYYY')"+
							//JOIN:
							" AND facProg."+FacFacturacionProgramadaBean.C_IDINSTITUCION+"= serieFac."+FacSerieFacturacionBean.C_IDINSTITUCION+
							" AND facProg."+FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+"= serieFac."+FacSerieFacturacionBean.C_IDSERIEFACTURACION;
							
			// filtros
			if (!miForm.getEstadoConfirmacion().trim().equals("")) {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION+"="+miForm.getEstadoConfirmacion();
			}
			if (!miForm.getEstadoPDF().trim().equals("")) {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOPDF+"="+miForm.getEstadoPDF();
			}
			if (!miForm.getEstadoEnvios().trim().equals("")) {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_IDESTADOENVIO+"="+miForm.getEstadoEnvios();
			}
			if (miForm.getArchivadas()!=null && miForm.getArchivadas().trim().equals("1")) {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_ARCHIVARFACT+"='1'";
			} else {
				select+=" AND facProg."+FacFacturacionProgramadaBean.C_ARCHIVARFACT+"='0'";
			} 

			select += " ORDER BY "+FacFacturacionProgramadaBean.C_FECHAREALGENERACION+" DESC";
			
			Vector vDatos = adm.selectGenerico(select);
			request.getSession().setAttribute("DATABACKUP", vDatos);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		return "resultados";
	}

	/** 
	 *  Funcion que atiende la accion abrir. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		return "abrir";
	}

	
	/**
	 * Descarga el fichero zip de la carpeta temporal. Si no existe, muestra un mensaje al usuario indicándolo.
	 */
	protected String download(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {
		String sNombreFichero = "";
		String sRutaTemporal = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm) formulario;
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idInstitucion = user.getLocation();
			String idSerieFacturacion = (String) vOcultos.elementAt(0);
			String idProgramacion=(String) vOcultos.elementAt(1);

		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");
			sRutaTemporal = rp.returnProperty("sjcs.directorioFisicoTemporalSJCSJava") +
			rp.returnProperty("facturacion.directorioTemporalFacturasJava");

			sNombreFichero = idSerieFacturacion+"_"+idProgramacion;
			String sExtension = ".zip";
			sNombreFichero += sExtension;
			sRutaTemporal += File.separator + idInstitucion+ File.separator;

			//Control de que no exista el fichero a descargar:
			File ficheroZip = new File(sRutaTemporal+sNombreFichero);
			if (!ficheroZip.exists()){
				FacFacturaAdm facturas = new FacFacturaAdm(user);
				Vector resultado = facturas.getSerieFacturacionConfirmada(idInstitucion, idSerieFacturacion, idProgramacion);
				if(!resultado.isEmpty()){
					//Se accede por clave referenciada de la tabla que hace join por lo 
					//que todos los registro tienen el mismo estado de generacion de pdf.
					//Cogemos por tanto el estado del priemr registro
					Hashtable hashPrimeraFacturaSerieProgramacion = (Hashtable)resultado.get(0); 
					String estadoPDF  = UtilidadesHash.getString(hashPrimeraFacturaSerieProgramacion,FacFacturacionProgramadaBean.C_IDESTADOPDF);
					if(estadoPDF.equals(FacEstadoConfirmFactBean.PDF_PROGRAMADA.toString())||estadoPDF.equals(FacEstadoConfirmFactBean.PDF_PROCESANDO.toString())){
						String mensaje = UtilidadesString.getMensajeIdioma(user,"messages.facturacion.PDFFacturaYaProgramada");
						return exitoRefresco(mensaje,request);
					}else{
					ClsLogging.writeFileLog("NO EXISTE EL ZIP, SE PASA A PROGRAMAR SU GENERACION",10);
					generarFacturaSolo(mapping, formulario, request, response);
					SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoRapido);
					String mensaje = UtilidadesString.getMensajeIdioma(user,"messages.facturacion.generacionPDFFacturaProgramada"); 			
					return exitoRefresco(mensaje,request);
				}
				}
				else{
					String mensaje = UtilidadesString.getMensajeIdioma(user,"messages.facturacion.descargaFacturas"); 			
					return exitoRefresco(mensaje,request);					
				}
					
	//				throw new SIGAException("messages.facturacion.generacionPDFFacturaProgramada");
			}

			request.setAttribute("nombreFichero", sNombreFichero);
			request.setAttribute("rutaFichero", sRutaTemporal+sNombreFichero);
//			request.setAttribute("borrarFichero", "true");
		}catch(SIGAException e){	
			throw e;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error");
		}

		return "descargaFichero";
	}
	
	
	
	
	/** 
	 * Atiende la accion confirmarFactura.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String confirmarFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mensaje = "";
		String salida = "";
		
		UserTransaction tx = null;	
		
		try {
			tx = this.getUserBean(request).getTransaction();
			
			ConfirmarFacturacionForm form 	= (ConfirmarFacturacionForm)formulario;
			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();

			tx.begin();
			
			Hashtable ht = new Hashtable();
			ht.put(FacFacturacionProgramadaBean.C_IDINSTITUCION,idInstitucion);
			ht.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion);
			ht.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION,idProgramacion);
			Vector v = adm.selectByPK(ht);
			FacFacturacionProgramadaBean beanP = null; 
			if (v!=null && v.size()>0) {
				beanP = (FacFacturacionProgramadaBean) v.get(0);
			}
			if (beanP!=null) {
				beanP.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA);
				beanP.setFechaPrevistaConfirmacion("sysdate");
				if (beanP.getGenerarPDF().equals("1")) {
					beanP.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_PROGRAMADA);
				} else {
					beanP.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_NOAPLICA);
				}
				if (beanP.getEnvio().equals("1")) {
					beanP.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_PROGRAMADA);
				} else {
					beanP.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
				}
				if (form.getFechaCargo() != null && !form.getFechaCargo().equals("")) {
					beanP.setFechaCargo(GstDate.getApplicationFormatDate("",form.getFechaCargo()));
				}
				
				if (!adm.updateDirect(beanP)) {
					throw new ClsExceptions("Error al actualizar estados de la programación: "+adm.getError());
				}
			}
			
			
			tx.commit();
			
			// Comprobaciones antes de confirmacion 
			Vector ret = adm.comprobarRecursosProgramacion(beanP);
			
			// tratamiento del mensaje
			mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.okConfirmacion"); 			
			if (ret.size()>0) {
				mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.avisosConfirmacion");
				for (int i=0;i<ret.size();i++) {
					mensaje += "\n" + UtilidadesString.getMensajeIdioma(this.getUserBean(request),(String)ret.get(i));
				}
			} 
			
			salida = exitoRefresco(mensaje,request);
			
			
/* RGG 05/02/2007 CAMBIO DE CONFIRMACION A BACKGROUND
 * Este es el proceso que hacía antes y que ahora se pasa a background
 *  			
			//FacFacturacionProgramadaBean bean = new FacFacturacionProgramadaBean();
			FacFacturaAdm admFactura = new FacFacturaAdm(usuario);
			ReadProperties p = new ReadProperties ("SIGA.properties");
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String usuMod			= (String)ocultos.elementAt(2);
			String idInstitucion	= this.getIDInstitucion(request).toString();
			String pathFichero 		= p.returnProperty(keyPath);
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
    		pathFichero += sBarra+idInstitucion;
			
    		String fechaCargo = form.getFechaCargo();
    		
			boolean noEncontrado = true;
			
			// Confirmar la facturación.
			Enumeration en = ((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
			Hashtable hashOld= new Hashtable();
			while(en.hasMoreElements() && noEncontrado){
				hashOld = (Hashtable)en.nextElement();
				if(((String)hashOld.get(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION)).equalsIgnoreCase(idSerieFacturacion)){
					if(((String)hashOld.get(FacFacturacionProgramadaBean.C_IDPROGRAMACION)).equalsIgnoreCase(idProgramacion)){
						noEncontrado=false;
					}
				}				
			} // While

			
			if (noEncontrado) {
				// ERROR no ha encontrado el registro. No se puede dar
				throw new SIGAException (adm.getError());
			} else {

				tx.begin();	
				

				// Se confirma la facturación
				Hashtable hashNew = (Hashtable)hashOld.clone();				
				UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "sysdate");
				if(!adm.update(hashNew, hashOld)){
					throw new SIGAException (adm.getError());
				}

				//Se genera numero de factura definitivo
				Object[] param_in_confirmacion = new Object[4];
				param_in_confirmacion[0] = idInstitucion;
				param_in_confirmacion[1] = idSerieFacturacion;
				param_in_confirmacion[2] = idProgramacion;
				param_in_confirmacion[3] = usuMod;
	        	
				String resultadoConfirmar[] = new String[2];
				resultadoConfirmar = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.CONFIRMACIONFACTURACION(?,?,?,?,?,?)}", 2, param_in_confirmacion);
	        	String codretorno = resultadoConfirmar[0];
	        	if (!codretorno.equals("0")){
	        		throw new ClsExceptions ("Error al generar números de facturación y disquetes bancarios.");
	        	}
				// Se envían a banco para su cobro
				Object[] param_in_banco = new Object[6];
				param_in_banco[0] = idInstitucion;
				param_in_banco[1] = idSerieFacturacion;
				param_in_banco[2] = idProgramacion;
				//Fecha de Cargo (DDMMAA):
				String fechaTMP = null;
				try {
					fechaTMP = fechaCargo.substring(0,2)+fechaCargo.substring(3,5)+fechaCargo.substring(8,10); 
				} catch (Exception e){
					fechaTMP = "";
				}
				param_in_banco[3] = fechaTMP;
				param_in_banco[4] = pathFichero;
				param_in_banco[5] = usuMod;
	        	
	        	String resultado[] = new String[3];
	        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.PRESENTACION(?,?,?,?,?,?,?,?,?)}", 3, param_in_banco);
	        	codretorno = resultado[1];
	        	if (!codretorno.equals("0")){
	        		throw new ClsExceptions ("Error al generar números de facturación y disquetes bancarios.");
	        	}
				
	        	cont = resultado[0];

	        	// DAC  //////////////////////////
	        	boolean bEnviarFactura = false;
        		Hashtable datos = new Hashtable();
        		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
        		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, idSerieFacturacion);
        		FacSerieFacturacionAdm admSerieFac = new FacSerieFacturacionAdm (usuario);
        		Vector vD = admSerieFac.select(datos);
        		if ((vD != null) && (vD.size() == 1)){
        			String generarEnvio = "";
        			FacSerieFacturacionBean serieFacturacionBean = (FacSerieFacturacionBean) vD.get(0);
        			generarEnvio = serieFacturacionBean.getEnvioFactura();
        			if ((generarEnvio != null) && (generarEnvio.equals("1"))) bEnviarFactura = true;
        			else bEnviarFactura = false;
        		}
	        	//////////////////////////////////

    			
    			tx.commit();

        		if (bEnviarFactura) {
		        	// Se guardan las facturas impresas.
		        	// Chequeo si ha existido algun error (retorno != 0)
		        	int errorAlmacenar = admFactura.almacenar(usr,Integer.valueOf(idInstitucion), Long.valueOf(idSerieFacturacion), Long.valueOf(idProgramacion),usuario, bEnviarFactura);
					if(errorAlmacenar!=0){					
						// Error de facturacion:
						if (errorAlmacenar == 1) 
							throw new ClsExceptions ("Error en el almacenamiento de la factura");
						// Error de envíos: 
						else
							throw new ClsExceptions ("No se ha podido realizar el envío porque no existe ninguna plantilla definida");
					}
        		}
			}	
	* FIN DEL PROCESO ANTERIOR
	**/
					
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}
		
		return salida; 
	}
	
	/** 
	 * Atiende la accion archivarFactura.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String archivarFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mensaje = "";
		
		UserTransaction tx = null;	
		
		try {
			tx = this.getUserBean(request).getTransaction();
			
			ConfirmarFacturacionForm form 	= (ConfirmarFacturacionForm)formulario;
			FacFacturacionProgramadaAdm admProgra = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();

			tx.begin();
			
			Hashtable hash = new Hashtable();
			hash.put(FacFacturacionProgramadaBean.C_IDINSTITUCION,idInstitucion);
			hash.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion);
			hash.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION,idProgramacion);
			Vector v = admProgra.selectByPK(hash);
			FacFacturacionProgramadaBean be = null;
			if (v!=null && v.size()>0) {
				be = (FacFacturacionProgramadaBean) v.get(0);
			}
			if (be!=null) {
				if (be.getArchivarFact().trim().equals("1")) {
					be.setArchivarFact("0");
					mensaje="messages.facturacion.desarchivar";
				} else {
					be.setArchivarFact("1");
					mensaje="messages.facturacion.archivar";
				}
				if (!admProgra.updateDirect(be)) {
					throw new ClsExceptions("Error al actualizar archviar: "+admProgra.getError());
				}
			}
			
			tx.commit();
			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}

		return this.exitoRefresco(mensaje,request); 
	}
	
	/** 
	 * Abre la ventana de la fecha de cargo. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "abrirFechaCargo";
	}

	/** 
	 * Atiende la accion descargaLog.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String descargaLog(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;	
		
		try {
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p = new ReadProperties ("SIGA.properties");
			String pathFichero 		= p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
			String nombreFichero = "";
			ConfirmarFacturacionForm form 	= (ConfirmarFacturacionForm)formulario;
			
			Vector ocultos = new Vector();		
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();

			nombreFichero = "LOG_CONFIRM_FAC_"+idInstitucion+"_"+idSerieFacturacion+"_"+idProgramacion+".log.xls"; 
			
			File fichero = new File(pathFichero+sBarra+idInstitucion+sBarra+nombreFichero);
			if (!fichero.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoErrores");
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"}, e, tx);
		}

		return "descargaFichero"; 
	}
	
	protected String confirmacionInmediata(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		//String mensaje = "";
		
		//UserTransaction tx = null;	
		
		try {
			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm)formulario;
			
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();

			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDINSTITUCION, idInstitucion); 
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion); 
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion); 

			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));
			Vector v = adm.selectByPK(h);
			if (v == null || v.size() != 1) {
				throw new ClsExceptions("Facturacion programada no encontrada");
			}
			
			FacFacturacionProgramadaBean bean = (FacFacturacionProgramadaBean) v.get(0);
			if (bean.getFechaConfirmacion()==null||bean.getFechaConfirmacion().equals("")){//Así nos aseguramos de que si ya se ha confirmado la facturacion, no se vuelva a lanzar.
				Facturacion facturacion = new Facturacion(this.getUserBean(request));
				facturacion.confirmarProgramacionFactura(bean, request,false,null,false,true);
			}
		}
		catch (ClsExceptions e) {
			return this.exitoRefresco(e.getMsg(),request);
		}
		catch (Exception e) {
			return this.error("messages.general.error",new ClsExceptions(e.getMessage()),request);
		}
		return this.exitoRefresco("messages.facturacion.confirmada", request); 
	}
	
	/**
	 * Marca una facturación programada confirmada para que el proceso background de 
	 * generación de facturas genere los pdf de las facturas.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String generarFacturaSolo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		//String mensaje = "";
		
		//UserTransaction tx = null;	
		
		try {
			ConfirmarFacturacionForm form = (ConfirmarFacturacionForm)formulario;
			
			Vector ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)ocultos.elementAt(0);			
			String idProgramacion 	= (String)ocultos.elementAt(1);
			String idInstitucion	= this.getIDInstitucion(request).toString();

			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDINSTITUCION, idInstitucion); 
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion); 
			UtilidadesHash.set(h, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion); 

			FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm(this.getUserBean(request));

			Vector v = adm.selectByPK(h);
			if (v == null || v.size() != 1) {
				throw new ClsExceptions("Facturacion programada no encontrada");
			}
			
			FacFacturacionProgramadaBean bean = (FacFacturacionProgramadaBean) v.get(0);
			bean.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_PROGRAMADA);
			bean.setFechaPrevistaConfirmacion("SYSDATE");
			bean.setGenerarPDF("1");
			
			if (!adm.updateDirect(bean)) {
				throw new ClsExceptions("Error al actualizar estados de la programación: "+adm.getError());
			}

		}
		catch (ClsExceptions e) {
			return this.exitoRefresco(e.getMsg(),request);
		}
		catch (Exception e) {
			return this.error("messages.general.error",new ClsExceptions(e.getMessage()),request);
		}
		return this.exitoRefresco("messages.facturacion.generacionPDFFacturaProgramada", request); 
	}
}