//Clase: DefinirMantenimientoEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005

package com.siga.gratuita.action;

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
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.BusquedaClientesFiltrosAdm;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsContrariosDesignaAdm;
import com.siga.beans.ScsContrariosDesignaBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEJGDESIGNAAdm;
import com.siga.beans.ScsEJGDESIGNABean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsProcuradorAdm;
import com.siga.beans.ScsProcuradorBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.certificados.Plantilla;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.form.DefinirMantenimientoEJGForm;
import com.siga.informes.InformeDefinirMantenimientoEJG;
import com.siga.ws.CajgConfiguracion;


/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_EJG
*/
public class DefinirMantenimientoEJGAction extends MasterAction 
{
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		MasterForm miform = (MasterForm)formulario;
		String mapDestino = "exception";
		try{
			do {
				if (miform == null) {
					return mapping.findForward(this.abrir(mapping, miform, request, response));
				}
	
				String accion = miform.getModo();
				if (accion != null && !accion.equalsIgnoreCase("")) {
					if (accion.equalsIgnoreCase("relacionarConDesigna")) {
						mapDestino = relacionarConDesignaExt (true, miform, request);
						break;
					}
					if (accion.equalsIgnoreCase("borrarRelacionConAsistencia")) {
						mapDestino = borrarRelacionarConAsistencia(miform, request);
						break;
					}
					if (accion.equalsIgnoreCase("borrarRelacionConDesigna")) {
						mapDestino = relacionarConDesigna(false, miform, request);
						break;
					}
					if (accion.equalsIgnoreCase("modificarDefensa")) {
						mapDestino = modificarDefensa(mapping, miform, request, response);
						break;
					}
				}
				return super.executeInternal(mapping, formulario, request, response);

			} while (false);

			return mapping.findForward(mapDestino);

		}catch(SIGAException e){
			throw e;
		}
		catch(Exception es){
			throw new SIGAException("messages.general.error",es,new String[] {"modulo.gratuita"}); 
		}
	}

	
	/** 
	 *  Funcion que atiende la accion imprimir. Imprime listas de cola de guardias
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	//protected String imprimir (
	protected String editar (
			ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		String generacionPdfOK = "ERROR";
		String salida = null;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		String idioma = usr.getLanguage().toUpperCase();
		
		try {		
			//obtener plantilla
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");			
		    String rutaPlantilla = Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoSolicitudAsistenciaJava")+rp.returnProperty("sjcs.directorioSolicitudAsistenciaJava"))+ClsConstants.FILE_SEP+institucion;
		    
		    // RGG cambio de codigos 
		    String lenguajeExt ="es";
		    AdmLenguajesAdm al = new AdmLenguajesAdm(this.getUserBean(request));
		    lenguajeExt=al.getLenguajeExt(idioma);
		    
		    
		    String nombrePlantilla=ClsConstants.PLANTILLA_FO_SOLICITUDASISTENCIA+"_"+lenguajeExt+".fo";
		    
		    InformeDefinirMantenimientoEJG informe = new InformeDefinirMantenimientoEJG();
		    
		    String contenidoPlantilla = informe.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);
		    
		    //obtener la ruta de descarga
		    String rutaServidor =
		    	Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoSJCSJava")+rp.returnProperty("sjcs.directorioSJCSJava"))+
		    	ClsConstants.FILE_SEP+institucion;
			File rutaFin=new File(rutaServidor);
			if (!rutaFin.exists()){
				if(!rutaFin.mkdirs()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
				}
			}    
			String nombreFicheroPDF="SolicitudAsistencia_"+System.currentTimeMillis();
			
			//Obtengo el bean
	        DefinirMantenimientoEJGForm form = (DefinirMantenimientoEJGForm)formulario;
    		Hashtable datosBase=form.getDatos();
    		UtilidadesHash.set(datosBase,"RUTA_LOGO",rutaPlantilla+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"logo.gif");


			//Generamos el Informe si la hash no es null:			
    		File fPdf = informe.generarInforme(request,datosBase,rutaPlantilla,contenidoPlantilla,rutaServidor,nombreFicheroPDF);
			if ((fPdf!=null))
				generacionPdfOK = "OK";			
			else
				generacionPdfOK = "ERROR";
			
			request.setAttribute("generacionOK",generacionPdfOK);			
			request.setAttribute("rutaFichero", rutaServidor+ClsConstants.FILE_SEP+nombreFicheroPDF+".pdf");			
			request.setAttribute("borrarFichero", "true");			
			salida = "descarga";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return salida;	
	}
	

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		UserTransaction tx = null;
		Hashtable hash = new Hashtable();
		Hashtable hashTemporal = new Hashtable();	
		
		try
		{		
			// Dependiendo de donde vengamos tenemos que modificar unos campos u otros.
			UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsEJGAdm ejgAdm = new ScsEJGAdm(usr);
			DefinirMantenimientoEJGForm miForm 	= (DefinirMantenimientoEJGForm)formulario;

			//Modificamos el TipoEJGColegio, FechaPresentacion, FechaLimitePresentacion, ProcuradorNecesario, Procurador, Observaciones y Delitos
			String[] campos = {ScsEJGBean.C_IDTIPOEJGCOLEGIO, 			ScsEJGBean.C_FECHAPRESENTACION,
							   ScsEJGBean.C_FECHALIMITEPRESENTACION,	ScsEJGBean.C_PROCURADORNECESARIO,
							   ScsEJGBean.C_CALIDAD,					ScsEJGBean.C_OBSERVACIONES,
							   ScsEJGBean.C_DELITOS,					ScsEJGBean.C_PROCURADOR,					   
							   ScsEJGBean.C_IDPROCURADOR, 				ScsEJGBean.C_IDINSTITUCIONPROCURADOR,
							   ScsEJGBean.C_NUMERO_CAJG,				ScsEJGBean.C_ANIO_CAJG,
							   //ScsEJGBean.C_JUZGADO, 					ScsEJGBean.C_JUZGADOIDINSTITUCION,
							   //ScsEJGBean.C_COMISARIA, 					ScsEJGBean.C_COMISARIAIDINSTITUCION,
							   ScsEJGBean.C_NUMEROPROCEDIMIENTO, 		ScsEJGBean.C_NUMERODILIGENCIA,
							   ScsEJGBean.C_IDPERSONA,                  ScsEJGBean.C_GUARDIATURNO_IDTURNO,
							   ScsEJGBean.C_GUARDIATURNO_IDGUARDIA,     ScsEJGBean.C_FECHAAPERTURA,
							   ScsEJGBean.C_IDORIGENCAJG};

			// Campos a modificar
			hash = miForm.getDatos();
			
			// JBD INC-5682-SIGA 
			// Pasamos la fecha recibida del form al formato de la base de datos
			hash.put(ScsEJGBean.C_FECHAAPERTURA, GstDate.getApplicationFormatDate("",(String)hash.get(ScsEJGBean.C_FECHAAPERTURA)));
			
			// Obtengo el idProcurador y la idInstitucion del procurador:
			Integer idProcurador, idInstitucionProcurador;
			idProcurador = null;
			idInstitucionProcurador = null;			
			String procurador = (String)hash.get(ScsEJGBean.C_PROCURADOR);
			if (procurador!=null && !procurador.equals("")){
				try{
				idProcurador = new Integer(procurador.substring(0,procurador.indexOf(",")));
				idInstitucionProcurador = new Integer(procurador.substring(procurador.indexOf(",")+1));
				hash.put(ScsEJGBean.C_IDPROCURADOR, idProcurador);
				hash.put(ScsEJGBean.C_IDINSTITUCIONPROCURADOR, idInstitucionProcurador);
				}catch (Exception e){
					;// controlamos la excepcion que se produce cuando no hemos seleccionado ningun procurador y se hace un new Integer de vacío;
				}
			}
			
			if ((hash.containsKey("FECHAPRESENTACION")) && (!hash.get("FECHAPRESENTACION").toString().equals(""))) hash.put("FECHAPRESENTACION",GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getDatos().get("FECHAPRESENTACION").toString()).toString());
			if ((hash.containsKey("FECHALIMITEPRESENTACION")) && (!hash.get("FECHALIMITEPRESENTACION").toString().equals(""))) hash.put("FECHALIMITEPRESENTACION",GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getDatos().get("FECHALIMITEPRESENTACION").toString()).toString());			 
			
			
			String idPersona = request.getParameter("idPersona");
			if (idPersona != null && !idPersona.equals("")) {
				String a[] = idPersona.split(",");
				UtilidadesHash.set(hash, ScsEJGBean.C_IDPERSONA, a[0].trim());
			}			
			
			String juzgado = miForm.getJuzgado();
			if (juzgado != null && !juzgado.equals("")) {
				String a[] = juzgado.split(",");
				UtilidadesHash.set(hash, ScsEJGBean.C_JUZGADO, a[0].trim());
				UtilidadesHash.set(hash, ScsEJGBean.C_JUZGADOIDINSTITUCION, a[1].trim());
			}

			String comisaria = miForm.getComisaria();
			if (comisaria != null && !comisaria.equals("")) {
				String a[] = comisaria.split(",");
				UtilidadesHash.set(hash, ScsEJGBean.C_COMISARIA, a[0].trim());
				UtilidadesHash.set(hash, ScsEJGBean.C_COMISARIAIDINSTITUCION, a[1].trim());
			}
			UtilidadesHash.set(hash, ScsEJGBean.C_NUMERODILIGENCIA,    miForm.getNumeroDilegencia());
			UtilidadesHash.set(hash, ScsEJGBean.C_NUMEROPROCEDIMIENTO, miForm.getNumeroProcedimiento());
			
			String idOrigenCAJG=miForm.getIdOrigenCAJG();
			if (idOrigenCAJG != null && !idOrigenCAJG.equals("")) {
				
				UtilidadesHash.set(hash, ScsEJGBean.C_IDORIGENCAJG, miForm.getIdOrigenCAJG());

			}else{
				UtilidadesHash.set(hash, ScsEJGBean.C_IDORIGENCAJG, "");
			}
			
			tx.begin();
			ejgAdm.updateDirect(hash,null,campos);
			// Saltos y compensaciones
			if (hash.get(ScsEJGBean.C_TIPOLETRADO).toString().equalsIgnoreCase("P")) {
				
					String idsaltosturno = "";
					Vector registros = new Vector();	
		            //Consulta para ver si tiene compensaciones           
		            String where = " WHERE " + ScsSaltosCompensacionesBean.C_IDINSTITUCION + " = " + hash.get(ScsEJGBean.C_IDINSTITUCION) +
								   " AND " + ScsSaltosCompensacionesBean.C_IDTURNO + " = " + hash.get(ScsEJGBean.C_GUARDIATURNO_IDTURNO) +
								   " AND " + ScsSaltosCompensacionesBean.C_IDGUARDIA + " = " + hash.get(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA) +
								   " AND " + ScsSaltosCompensacionesBean.C_IDPERSONA + " = " + hash.get(ScsEJGBean.C_IDPERSONA) +
								   " AND " + ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION + " = 'C'" +
								   " AND " + ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO + " IS NULL ";
		            
		            registros.clear();
					ScsSaltosCompensacionesAdm admSaltosCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
		            registros = admSaltosCompensaciones.selectForUpdate(where);                                                                                                                  //Si hay compensacion
		            if (registros.size() > 0) {
		                idsaltosturno = (((ScsSaltosCompensacionesBean)registros.get(0)).getIdSaltosTurno()).toString();	                
		                //Anoto la fecha de cumplimiento
		                hashTemporal.clear();
		                hashTemporal.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,hash.get(ScsEJGBean.C_IDINSTITUCION));
		                hashTemporal.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,idsaltosturno);
		                hashTemporal.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,hash.get(ScsEJGBean.C_FECHAAPERTURA));
		                String claves[] = {ScsSaltosCompensacionesBean.C_IDINSTITUCION, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO};
		                String campos1[] = {ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO};
		                //Actualizo la fecha de cumplimiento                
		                admSaltosCompensaciones.updateDirect(hashTemporal,claves,campos1);
		            }
				}
			
			
			///////////////////////////////////////////////////////////////////////////////////////////
			// RGG 21-03-2006 : Cambios debidos a la nueva asignacion de colegiados desde Busqueda SJCS
			// ----------------------------------------------------------------------------------------
			
			//-----------------------------------------------------
			// obtencion de valores a utilizar (MODIFICAR SEGUN ACTION)
			String idInstitucionSJCS=usr.getLocation();
			String idTurnoSJCS=miForm.getIdTurnoEJG();
			String idGuardiaSJCS=miForm.getIdGuardiaEJG();
			String anioSJCS=miForm.getAnio();
			String numeroSJCS=miForm.getNumero();
			String idPersonaSJCS=request.getParameter("idPersona");
			String origenSJCS = "gratuita.operarEJG.boton.EditarEJG"; 
			//-----------------------------------------------------
			
			
			// Obtención parametros de la busqueda SJCS (FIJOS, NO TOCAR)
			String flagSalto = request.getParameter("flagSalto");
			String flagCompensacion = request.getParameter("flagCompensacion");
			String checkSalto = request.getParameter("checkSalto");
			//String checkCompensacion = request.getParameter("checkCompensacion");
			String motivoSaltoSJCS = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarSaltoPor") + " " +
			UtilidadesString.getMensajeIdioma(usr,origenSJCS);
			//String motivoCompensacionSJCS = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarCompensacionPor") + " " +
			//UtilidadesString.getMensajeIdioma(usr,origenSJCS);
			
			// Aplicar cambios (COMENTAR LO QUE NO PROCEDA) Revisar que no se hace algo ya en el action. 
			BusquedaClientesFiltrosAdm admFiltros = new BusquedaClientesFiltrosAdm(this.getUserBean(request)); 
			// Primero: Actualiza si ha sido automático o manual (Designaciones)0
			//admFiltros.actualizaManualDesigna(idInstitucionSJCS,idTurnoSJCS,idPersonaSJCS,anioSJCS, numeroSJCS, flagSalto,flagCompensacion);
			// Segundo: Tratamiento de último (Designaciones)
			//admFiltros.tratamientoUltimo(idInstitucionSJCS,idTurnoSJCS,idPersonaSJCS,flagSalto,flagCompensacion);
			// Tercero: Generación de salto (Designaciones y asistencias)
			admFiltros.crearSalto(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS,checkSalto, motivoSaltoSJCS);
			// Cuarto: Generación de compensación (Designaciones NO ALTAS)
			//admFiltros.crearCompensacion(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS,checkCompensacion,motivoCompensacionSJCS);
			///////////////////////////////////////////////////////////////////////////////////////////
			
			tx.commit();			
		}
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoRefresco("messages.updated.success",request);
	}

	protected String modificarDefensa(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
			UserTransaction tx = null;
			Hashtable hash = new Hashtable();
			Hashtable hashTemporal = new Hashtable();	
			
			try
			{		
				// Dependiendo de donde vengamos tenemos que modificar unos campos u otros.
				UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
				tx = usr.getTransaction();
				
				ScsEJGAdm ejgAdm = new ScsEJGAdm(usr);
				DefinirMantenimientoEJGForm miForm 	= (DefinirMantenimientoEJGForm)formulario;

				//Modificamos el TipoEJGColegio, FechaPresentacion, FechaLimitePresentacion, ProcuradorNecesario, Procurador, Observaciones y Delitos
				String[] campos = {//	ScsEJGBean.C_PROCURADORNECESARIO,
								   ScsEJGBean.C_CALIDAD,		            ScsEJGBean.C_OBSERVACIONES,
								   ScsEJGBean.C_DELITOS,					ScsEJGBean.C_PROCURADOR,					   
								   ScsEJGBean.C_IDPROCURADOR, 				ScsEJGBean.C_IDINSTITUCIONPROCURADOR,
								   ScsEJGBean.C_IDPRETENSION,				ScsEJGBean.C_IDPRETENSIONINSTITUCION,
								   ScsEJGBean.C_JUZGADO, 					ScsEJGBean.C_JUZGADOIDINSTITUCION,
								   ScsEJGBean.C_COMISARIA, 					ScsEJGBean.C_COMISARIAIDINSTITUCION,
								   ScsEJGBean.C_NUMEROPROCEDIMIENTO, 		ScsEJGBean.C_NUMERODILIGENCIA,
								   ScsEJGBean.C_FECHADESIGPROC,             ScsEJGBean.C_PRECEPTIVO,
								   ScsEJGBean.C_SITUACION,                  ScsEJGBean.C_IDRENUNCIA,
								   ScsEJGBean.C_IDTIPOENCALIDAD,            ScsEJGBean.C_CALIDADIDINSTITUCION};

				// Campos a modificar
				hash = miForm.getDatos();
				
				// Obtengo el idProcurador y la idInstitucion del procurador:
				Integer idProcurador, idInstitucionProcurador;
				idProcurador = null;
				idInstitucionProcurador = null;			
				String procurador = (String)hash.get(ScsEJGBean.C_PROCURADOR);
				if (procurador!=null && !procurador.equals("")){
					try{
					idProcurador = new Integer(procurador.substring(0,procurador.indexOf(",")));
					idInstitucionProcurador = new Integer(procurador.substring(procurador.indexOf(",")+1));
					hash.put(ScsEJGBean.C_IDPROCURADOR, idProcurador);
					hash.put(ScsEJGBean.C_IDINSTITUCIONPROCURADOR, idInstitucionProcurador);
					}catch (Exception e){
						;// controlamos la excepcion que se produce cuando no hemos seleccionado ningun procurador y se hace un new Integer de vacío;
					}
				}
				
//				
//				String idPersona = request.getParameter("idPersona");
//				if (idPersona != null && !idPersona.equals("")) {
//					String a[] = idPersona.split(",");
//					UtilidadesHash.set(hash, ScsEJGBean.C_IDPERSONA, a[0].trim());
//				}			
				
				String juzgado = miForm.getJuzgado();
				if (juzgado != null && !juzgado.equals("")) {
					String a[] = juzgado.split(",");
					UtilidadesHash.set(hash, ScsEJGBean.C_JUZGADO, a[0].trim());
					UtilidadesHash.set(hash, ScsEJGBean.C_JUZGADOIDINSTITUCION, a[1].trim());
				}
				
				String fechaProc = miForm.getFechaProc();
				if (fechaProc != null && !fechaProc.equals("")) {
					
					UtilidadesHash.set(hash, ScsEJGBean.C_FECHADESIGPROC, GstDate.getApplicationFormatDate("",fechaProc));
				}
				
				String pretension = miForm.getPretension();
				if (pretension != null && !pretension.equals("")) {
					String a[] = pretension.split(",");
					UtilidadesHash.set(hash, ScsEJGBean.C_IDPRETENSION, a[0].trim());
					UtilidadesHash.set(hash, ScsEJGBean.C_IDPRETENSIONINSTITUCION, usr.getLocation());
				}

				String comisaria = miForm.getComisaria();
				if (comisaria != null && !comisaria.equals("")) {
					String a[] = comisaria.split(",");
					UtilidadesHash.set(hash, ScsEJGBean.C_COMISARIA, a[0].trim());
					UtilidadesHash.set(hash, ScsEJGBean.C_COMISARIAIDINSTITUCION, a[1].trim());
				}
				UtilidadesHash.set(hash, ScsEJGBean.C_NUMERODILIGENCIA,    miForm.getNumeroDilegencia());
				UtilidadesHash.set(hash, ScsEJGBean.C_NUMEROPROCEDIMIENTO, miForm.getNumeroProcedimiento());

				String idPreceptivo=miForm.getIdPreceptivo();
				if (idPreceptivo != null && !idPreceptivo.equals("")) {
					UtilidadesHash.set(hash, ScsEJGBean.C_PRECEPTIVO, miForm.getIdPreceptivo());
				}else{
					UtilidadesHash.set(hash, ScsEJGBean.C_PRECEPTIVO, "");
				}
				String idSituacion=miForm.getIdSituacion();
				if (idSituacion != null && !idSituacion.equals("")) {
					UtilidadesHash.set(hash, ScsEJGBean.C_SITUACION, miForm.getIdSituacion());
				}else{
					UtilidadesHash.set(hash, ScsEJGBean.C_SITUACION, "");
				}
				
				String idRenuncia= miForm.getidRenuncia();
				if (idRenuncia != null && !idRenuncia.equals("")) {
					UtilidadesHash.set(hash, ScsEJGBean.C_IDRENUNCIA,miForm.getidRenuncia());
				}else{
					UtilidadesHash.set(hash, ScsEJGBean.C_IDRENUNCIA, "");
				}
				
				/**INC_07443_SIGA ,se guarda el campo idtipoencalidad que se ha elegido en la jsp**/                      
				String idTipoenCalidad=miForm.getIdTipoenCalidad();
				if (idTipoenCalidad != null && !idTipoenCalidad.equals("")) {
					UtilidadesHash.set(hash, ScsEJGBean.C_IDTIPOENCALIDAD,idTipoenCalidad);						
					UtilidadesHash.set(hash, ScsEJGBean.C_CALIDADIDINSTITUCION,usr.getLocation());
					UtilidadesHash.set(hash, ScsEJGBean.C_CALIDAD,idTipoenCalidad);	
				}else{
					UtilidadesHash.set(hash, ScsEJGBean.C_IDTIPOENCALIDAD, "");
					UtilidadesHash.set(hash, ScsEJGBean.C_CALIDADIDINSTITUCION,"");
					UtilidadesHash.set(hash, ScsEJGBean.C_CALIDAD,"");	
				}
				
				tx.begin();
				ejgAdm.updateDirect(hash,null,campos);
				
				
				///////////////////////////////////////////////////////////////////////////////////////////
				// RGG 21-03-2006 : Cambios debidos a la nueva asignacion de colegiados desde Busqueda SJCS
				// ----------------------------------------------------------------------------------------
				
				//-----------------------------------------------------
				// obtencion de valores a utilizar (MODIFICAR SEGUN ACTION)
				String idInstitucionSJCS=usr.getLocation();
				String idTurnoSJCS=miForm.getIdTurnoEJG();
				String anioSJCS=miForm.getAnio();
				String numeroSJCS=miForm.getNumero();
//				String idPersonaSJCS=request.getParameter("idPersona");
				String origenSJCS = "gratuita.operarEJG.boton.EditarEJG"; 
				//-----------------------------------------------------
				
				
				// Obtención parametros de la busqueda SJCS (FIJOS, NO TOCAR)
//				String flagSalto = request.getParameter("flagSalto");
//				String flagCompensacion = request.getParameter("flagCompensacion");
//				String checkSalto = request.getParameter("checkSalto");
				//String checkCompensacion = request.getParameter("checkCompensacion");
//				String motivoSaltoSJCS = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarSaltoPor") + " " +
//				UtilidadesString.getMensajeIdioma(usr,origenSJCS);
				//String motivoCompensacionSJCS = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarCompensacionPor") + " " +
				//UtilidadesString.getMensajeIdioma(usr,origenSJCS);
				
				// Aplicar cambios (COMENTAR LO QUE NO PROCEDA) Revisar que no se hace algo ya en el action. 
//				BusquedaClientesFiltrosAdm admFiltros = new BusquedaClientesFiltrosAdm(this.getUserBean(request)); 
				// Primero: Actualiza si ha sido automático o manual (Designaciones)0
				//admFiltros.actualizaManualDesigna(idInstitucionSJCS,idTurnoSJCS,idPersonaSJCS,anioSJCS, numeroSJCS, flagSalto,flagCompensacion);
				// Segundo: Tratamiento de último (Designaciones)
				//admFiltros.tratamientoUltimo(idInstitucionSJCS,idTurnoSJCS,idPersonaSJCS,flagSalto,flagCompensacion);
				// Tercero: Generación de salto (Designaciones y asistencias)
//				admFiltros.crearSalto(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS,checkSalto, motivoSaltoSJCS);
				// Cuarto: Generación de compensación (Designaciones NO ALTAS)
				//admFiltros.crearCompensacion(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS,checkCompensacion,motivoCompensacionSJCS);
				///////////////////////////////////////////////////////////////////////////////////////////
				
				tx.commit();			
			}
			catch (Exception e) 
			{
				throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
			} 
			return exitoRefresco("messages.updated.success",request);
		}
	
	
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			
			Hashtable miHash = new Hashtable();
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String nombreTurnoAsistencia="", nombreGuardiaAsistencia="", consultaTurnoAsistencia="", consultaGuardiaAsistencia="";
			ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
			
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			// Recuperamos los datos de la clave del EJG. Pueden venir de la request si accedemos por primera vez a esa pestanha
			try {
				miHash.put(ScsEJGBean.C_IDINSTITUCION,usr.getLocation());
				miHash.put(ScsEJGBean.C_NUMERO,(String)request.getParameter("NUMERO"));
				miHash.put(ScsEJGBean.C_ANIO,(String)request.getParameter("ANIO"));
				miHash.put(ScsEJGBean.C_IDTIPOEJG,(String)request.getParameter("IDTIPOEJG"));
			}
			// O si venimos al recargar la página después de modificarla habrá que recuperarlo del formulario
			catch (Exception e){
				miHash.put(ScsEJGBean.C_IDINSTITUCION,usr.getLocation());
				miHash.put(ScsEJGBean.C_NUMERO,((DefinirMantenimientoEJGForm)formulario).getNumero());
				miHash.put(ScsEJGBean.C_ANIO,((DefinirMantenimientoEJGForm)formulario).getAnio());
				miHash.put(ScsEJGBean.C_IDTIPOEJG,((DefinirMantenimientoEJGForm)formulario).getIdTipoEJG());				
			}
			
			// Ahora realizamos la consulta. Primero cogemos los campos que queremos recuperar 
			String consulta = "select ejg.ANIO, ejg.NUMEJG,designa.ESTADO,ejg.IDTIPOEJG AS IDTIPOEJG,ejg.NUMERO_CAJG AS NUMERO_CAJG, ejg.NUMERO, turno.ABREVIATURA AS NOMBRETURNO, guardia.NOMBRE AS NOMBREGUARDIA, guardia.IDGUARDIA AS IDGUARDIA, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tipoejg.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOEJG, ejg.IDTIPOEJGCOLEGIO AS IDTIPOEJGCOLEGIO," +
							  "decode(ejg.ORIGENAPERTURA,'M','Manual','S','SOJ','A','ASISTENCIA','DESIGNA'), ejg.IDPRETENSION as IDPRETENSION, ejg.IDPRETENSIONINSTITUCION as IDPRETENSIONINSTITUCION, ejg.idtipodictamenejg as IDTIPODICTAMENEJG, " + 
							  "ejg.FECHAAPERTURA AS FECHAAPERTURA, personajg.NIF AS NIFASISTIDO, personajg.NOMBRE AS NOMBREASISTIDO, personajg.APELLIDO1 AS APELLIDO1ASISTIDO, personajg.APELLIDO2 AS APELLIDO2ASISTIDO, " +
							  " (Select Decode(Ejg.Idtipoencalidad, Null,'', f_Siga_Getrecurso(Tipcal.Descripcion,"+ this.getUserBean(request).getLanguage() + ")) "+
                              "  From Scs_Tipoencalidad Tipcal Where Tipcal.Idtipoencalidad = Ejg.Idtipoencalidad "+
                              "  And Tipcal.Idinstitucion = Ejg.Calidadidinstitucion) as calidad, Ejg.Idtipoencalidad, Ejg.Calidadidinstitucion, "+							  
                              "colegiado.NCOLEGIADO AS NCOLEGIADO, PERSONA.IDPERSONA AS IDPERSONA, persona.NOMBRE AS NOMBRELETRADO, " +
							  "persona.APELLIDOS1 AS APELLIDO1LETRADO, persona.APELLIDOS2 AS APELLIDO2LETRADO, soj.ANIO AS ANIOSOJ, soj.NUMERO AS NUMEROSOJ, soj.NUMSOJ AS CODIGOSOJ, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tiposoj.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOSOJ, tiposoj.IDTIPOSOJ AS IDTIPOSOJ, " +
							  "soj.FECHAAPERTURA AS FECHAAPERTURASOJ, asistencia.ANIO AS ANIOASISTENCIA, asistencia.NUMERO AS ASISTENCIANUMERO, asistencia.FECHAHORA AS ASISTENCIAFECHA, " +
							  " ejgd.aniodesigna AS DESIGNA_ANIO,ejgd.idturno AS DESIGNA_IDTURNO,ejgd.numerodesigna AS DESIGNA_NUMERO," +
							  "(SELECT ABREVIATURA FROM scs_turno t WHERE t.idturno = ejgd.IDTURNO and t.IDINSTITUCION = ejg.IDINSTITUCION) DESIGNA_TURNO_NOMBRE, " +
							  "designa.FECHAENTRADA AS FECHAENTRADADESIGNA, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tipoejgcolegio.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOEJGCOLEGIO," +
							  "ejg.FECHAPRESENTACION, ejg.FECHALIMITEPRESENTACION, ejg.PROCURADORNECESARIO, ejg.PROCURADOR, ejg.OBSERVACIONES, ejg.DELITOS"+
							  ",ejg."+ScsEJGBean.C_IDPROCURADOR+
							  ",ejg."+ScsEJGBean.C_IDINSTITUCIONPROCURADOR+
							  ",ejg."+ScsEJGBean.C_NUMERO_CAJG+
							  ",ejg."+ScsEJGBean.C_ANIO_CAJG +
							  ",ejg."+ScsEJGBean.C_NUMERODILIGENCIA + " " + ScsEJGBean.C_NUMERODILIGENCIA +
							  ",ejg."+ScsEJGBean.C_NUMEROPROCEDIMIENTO + " " + ScsEJGBean.C_NUMEROPROCEDIMIENTO +
							  ",ejg."+ScsEJGBean.C_JUZGADO + " " + ScsEJGBean.C_JUZGADO +
							  ",ejg."+ScsEJGBean.C_JUZGADOIDINSTITUCION + " " + ScsEJGBean.C_JUZGADOIDINSTITUCION +
							  ",ejg."+ScsEJGBean.C_COMISARIA + " " + ScsEJGBean.C_COMISARIA +
							  ",ejg."+ScsEJGBean.C_COMISARIAIDINSTITUCION + " " + ScsEJGBean.C_COMISARIAIDINSTITUCION +
							  ",ejg."+ScsEJGBean.C_GUARDIATURNO_IDTURNO + " IDTURNO " +
							  ",ejg."+ScsEJGBean.C_SUFIJO + " SUFIJO " + 
							  ",ejg."+ScsEJGBean.C_IDORIGENCAJG + " IDORIGENCAJG " + 
							  ",designa.codigo codigo";
			// Ahora las tablas de donde se sacan los campos
			consulta += " from scs_ejg ejg, scs_personajg personajg, cen_colegiado colegiado, scs_turno turno, scs_guardiasturno guardia, " +
					   "scs_soj soj, scs_designa designa, scs_tipoejg tipoejg, scs_tiposoj tiposoj, scs_asistencia asistencia, scs_tipoejgcolegio tipoejgcolegio, " +
					   "cen_persona persona,scs_ejgdesigna ejgd";
			// Y por último efectuamos la join
			consulta += " where ejg.idinstitucion             = turno.idinstitucion(+) and " +
					      " ejg.guardiaturno_idturno      = turno.idturno(+) and " +     
					      " ejg.IDTIPOEJG                 = tipoejg.IDTIPOEJG and "+
						  "ejg.idinstitucion             = guardia.idinstitucion(+) and " +
						  "ejg.guardiaturno_idturno      = guardia.idturno(+) and " +
						  "ejg.guardiaturno_idguardia    = guardia.idguardia(+) and " +   
						  "personajg.idinstitucion    (+)= ejg.idinstitucion and " +       
						  "personajg.idpersona        (+)= ejg.idpersonajg and " +         
						  "ejg.idinstitucion             = colegiado.idinstitucion(+) and " +    
						  "ejg.idpersona                 = colegiado.idpersona(+) and " +        
						  "soj.idinstitucion (+)= ejg.idinstitucion and " +
						  "soj.ejgidtipoejg	 (+)= ejg.idtipoejg and " +
						  "soj.ejganio (+)         = ejg.anio and " +
						  "soj.ejgnumero(+)        = ejg.numero and " +
						  "asistencia.idinstitucion (+)= ejg.idinstitucion and " +
						  "asistencia.ejganio (+)= ejg.anio and " +
						  "asistencia.ejgnumero (+)= ejg.numero " +
						 
						  "and designa.idinstitucion(+) = ejgd.idinstitucion " +
						  "and designa.anio(+) = ejgd.aniodesigna " +
						  "and designa.numero(+) = ejgd.numerodesigna " +
						  "and designa.idturno(+) = ejgd.idturno " +
						  
						  "and ejg.idinstitucion=ejgd.idinstitucion(+) " +
						  "and ejg.anio=ejgd.anioejg(+) " +
						  "and ejg.numero=ejgd.numeroejg(+) " +
						  "and ejg.idtipoejg=ejgd.idtipoejg(+) and " +

						  "tipoejgcolegio.idinstitucion (+)= ejg.idinstitucion and " +
						  "tipoejgcolegio.idtipoejgcolegio (+)= ejg.idtipoejgcolegio and "+
						  "tiposoj.idtiposoj (+)= soj.idtiposoj and "+
						  "ejg.idpersona = persona.idpersona(+) and ";
			
			
			// Y por último filtramos por la clave principal de ejg
			consulta += " ejg.idtipoejg = " + miHash.get("IDTIPOEJG") + " and ejg.idinstitucion = " + miHash.get("IDINSTITUCION") + " and ejg.anio = " + miHash.get("ANIO") + " and ejg.numero = " + miHash.get("NUMERO");
			
			// jbd inc-6803 Ordenamos para quedarnos solo con la mas moderna
			consulta += " order by designa.fechaentrada desc";
			
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos			
			Vector resultado = admBean.selectGenerico(consulta);
			Hashtable ejg = (Hashtable)resultado.get(0);
			
//			consulta = "SELECT " + UtilidadesMultidioma.getCampoMultidioma("descripcion",this.getUserBean(request).getLanguage()) + " FROM SCS_MAESTROESTADOSEJG WHERE IDESTADOEJG = ( SELECT DISTINCT IDESTADOEJG " +
//					   "FROM SCS_ESTADOEJG ejg1 WHERE ejg1.idtipoejg = " + miHash.get("IDTIPOEJG") + " and ejg1.idinstitucion = " + miHash.get("IDINSTITUCION") + " and ejg1.anio = " + miHash.get("ANIO") + " and ejg1.numero = " + miHash.get("NUMERO") + " and ejg1.fechainicio =( SELECT MAX(FECHAINICIO) FROM SCS_ESTADOEJG ejg WHERE ejg.idtipoejg = " 
//					   + miHash.get("IDTIPOEJG") + " and ejg.idinstitucion = " + miHash.get("IDINSTITUCION") + " and ejg.anio = " + miHash.get("ANIO") + " and ejg.numero = " + miHash.get("NUMERO") + ") and rownum=1 )";
			
			consulta = "SELECT F_SIGA_GETRECURSO(F_SIGA_GET_ULTIMOESTADOEJG(" +
					miHash.get("IDINSTITUCION") +
					", " + miHash.get("IDTIPOEJG") +
					", " + miHash.get("ANIO") +
					", " + miHash.get("NUMERO") + "), " + this.getUserBean(request).getLanguage() + ") as DESCRIPCION FROM DUAL";
			
			resultado.clear();
			resultado = admBean.selectGenerico(consulta);
			String estado = "";
			//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
			if (!resultado.isEmpty()) {estado = ((Hashtable)resultado.get(0)).get("DESCRIPCION").toString();}		

			// Obtenemos el procurador seleccionado:
			// Obtengo el idProcurador y la idInstitucion del procurador:
			String idProcurador = (String)ejg.get(ScsEJGBean.C_IDPROCURADOR);
			String idInstitucionProcurador = (String)ejg.get(ScsEJGBean.C_IDINSTITUCIONPROCURADOR);
			
			try {
				ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.getUserBean(request));
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDPROCURADOR, idProcurador);
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDINSTITUCION, idInstitucionProcurador);
				ScsProcuradorBean b = (ScsProcuradorBean)(procuradorAdm.select(h)).get(0);
				request.setAttribute("PROCURADOR_NUM_COLEGIADO", b.getNColegiado());
				request.setAttribute("PROCURADOR_NOMBRE_COMPLETO", b.getNombre() + " " + b.getApellido1() + " " + b.getApellido2());
			}
			catch (Exception e) {}
			
			String procurador = idProcurador+","+idInstitucionProcurador;
			ejg.put(ScsEJGBean.C_PROCURADOR,procurador);
			
			// En DATABACKUP se almacenan todos los campos de la consulta para mostralos en la jsp de edición
			request.getSession().setAttribute("DATABACKUP",ejg);
			// En NOMBREESTADO se almacenan el nombre del estado
			request.setAttribute("NOMBREESTADO",estado);
			
			// RGG 17-03-2006
			if (ejg!=null) {
				
				if (!((String)ejg.get("ANIOASISTENCIA")).trim().equals("")) {
					String consultaAsistencia=" where anio = " + ejg.get("ANIOASISTENCIA") + " and numero = " + ejg.get("ASISTENCIANUMERO") + " and idinstitucion="+usr.getLocation()+" ";
					ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm(this.getUserBean(request)); 				
					ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request)); 				
					ScsGuardiasTurnoAdm guardia = new ScsGuardiasTurnoAdm(this.getUserBean(request)); 				
					ScsAsistenciasBean asistenciaBean = (ScsAsistenciasBean)((Vector)asistenciaAdm.select(consultaAsistencia)).get(0);
					
					consultaTurnoAsistencia=" where idTurno = " + asistenciaBean.getIdTurno() + " and idinstitucion="+usr.getLocation()+" ";
					consultaGuardiaAsistencia=" where idTurno = " + asistenciaBean.getIdTurno() + " and idGuardia = " + asistenciaBean.getIdGuardia() + " and idinstitucion="+usr.getLocation()+" ";
		
					nombreTurnoAsistencia = ((ScsTurnoBean)((Vector)turno.select(consultaTurnoAsistencia)).get(0)).getNombre();
					nombreGuardiaAsistencia = ((ScsGuardiasTurnoBean)((Vector)guardia.select(consultaGuardiaAsistencia)).get(0)).getNombre();
					request.setAttribute("nombreTurnoAsistencia",nombreTurnoAsistencia);
					request.setAttribute("nombreGuardiaAsistencia",nombreGuardiaAsistencia);
				}
			}

			
			
			
			request.setAttribute("MODO",request.getSession().getAttribute("accion"));
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}			
		return "inicio";	
	}
		
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			DefinirEJGForm miForm = (DefinirEJGForm)formulario;			
			
			ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
			//Entramos al formulario en modo 'modificación'
			request.getSession().setAttribute("accion","modificar");
		
			String consulta =" select ejg.ANIO, ejg.NUMERO, turno.ABREVIATURA AS NOMBRETURNO, guardia.NOMBRE AS NOMBREGUARDIA, tipoejg.DESCRIPCION AS TIPOEJG, decode(ejg.ORIGENAPERTURA,'M','Manual','S','SOJ','A','ASISTENCIA','DESIGNA'), ejg.FECHAAPERTURA, " + UtilidadesMultidioma.getCampoMultidioma("estadoejg.DESCRIPCION",this.getUserBean(request).getLanguage()) + ", personajg.NIF, personajg.NOMBRE, personajg.APELLIDO1, personajg.APELLIDO2, ejg.IDTIPOEJGCOLEGIO AS IDTIPOEJGCOLEGIO," +
						     " decode(ejg.CALIDAD,'D','DEMANDANTE','DEMANDADO'), ejg.CALIDAD, colegiado.NCOLEGIADO, persona.NOMBRE, persona.APELLIDOS1, persona.APELLIDOS2, soj.ANIOSOJ, soj.NUMEROSOJ, " + UtilidadesMultidioma.getCampoMultidioma("tiposoj.DESCRIPCION",this.getUserBean(request).getLanguage()) + ", soj.FECHAAPERTURA, ejg.ASISTENCIA_ANIO, ejg.ASISTENCIA_NUMERO, asistencia.FECHAHORA, ejg.DESIGNA_ANIO," +
						     " ejg.DESIGNA_NUMERO, designa.FECHAENTRADA AS FECHAAPERTURA, tipoejgcolegio.DESCRIPCION, ejg.FECHAPRESENTACION, ejg.PROCURADORNECESARIO, ejg.OBSERVACIONES, ejg.DELITOS " +
							 " from scs_ejg ejg, scs_personajg personajg, cen_colegiado colegiado, scs_turno turno, scs_guardiasturno guardia, scs_maestroestadosejg estadoejg, scs_estadoejg  estado, scs_soj soj, scs_designa designa, scs_tipoejg tipoejg, scs_tiposoj tiposoj, scs_asistencia asistencia, scs_tipoejgcolegio tipoejgcolegio, cen_persona persona " +
							 " where ejg.idinstitucion = turno.idinstitucion and " +
						     " ejg.guardiaturno_idturno = turno.idturno and " +
						     " ejg.idinstitucion = guardia.idinstitucion and " +
						     " ejg.guardiaturno_idguardia = guardia.idguardia and " +
						     " estado.idinstitucion (+)= ejg.idinstitucion and " +
						     " estado.idtipoejg (+)= ejg.idtipoejg and " +
						     " estado.anio (+)= ejg.anio and " +
						     " estado.numero (+)= ejg.numero and " +      
						     " estado.idtipoejg = estadoejg.idestadoejg and " +
						     " personajg.idinstitucion (+)= ejg.idinstitucion and " +
						     " personajg.idpersona (+)= ejg.idpersonajg and " +
						     " ejg.idinstitucion = colegiado.idinstitucion and " +
						     " ejg.idpersona = colegiado.idpersona and " +
						     " soj.idinstitucion (+)= ejg.idinstitucion and " +
						     " soj.idtiposoj (+)= ejg.soj_idtiposoj and " +
						     " asistencia.idinstitucion (+)= ejg.idinstitucion and " +
						     " asistencia.anio (+)= ejg.anio and " +
						     " asistencia.numero (+)= ejg.numero and " +      
						     " designa.idinstitucion (+)= ejg.idinstitucion and " +
						     " designa.anio (+)= ejg.anio and " +
						     " designa.numero (+)= ejg.numero and " + 
						     " designa.idturno (+)= ejg.guardiaturno_idturno and " + 
						     " tipoejgcolegio.idinstitucion (+)= ejg.idinstitucion and " +
						     " tipoejgcolegio.idtipoejgcolegio (+)= ejg.idtipoejgcolegio ";
			
			consulta += " and ejg.idtipoejg = " + miForm.getIdTipoEJG() + " and ejg.idinstitucion = " + miForm.getIdInstitucion() + " and ejg.anio = " + miForm.getAnio() + " and ejg.numero = " + miForm.getNumero();
			
			Hashtable miHash = new Hashtable();
			miHash.put(ScsEJGBean.C_IDTIPOEJG,miForm.getIdTipoEJG());
			miHash.put(ScsEJGBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			miHash.put(ScsEJGBean.C_ANIO,miForm.getAnio());
			miHash.put(ScsEJGBean.C_NUMERO,miForm.getNumero());
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos			
			Vector resultado = admBean.selectGenerico(consulta);
			ScsEJGBean ejg = (ScsEJGBean)resultado.get(0);
			
			request.setAttribute("DATABACKUP",admBean.beanToHashTable(ejg));
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}			
		return "editar";
	}
	
	protected String borrarRelacionarConAsistencia(MasterForm formulario, HttpServletRequest request) throws SIGAException{
		
		try {
			DefinirMantenimientoEJGForm miForm 	= (DefinirMantenimientoEJGForm)formulario;
			
			Hashtable miHash = new Hashtable ();

			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDINSTITUCION, 	miForm.getIdInstitucion());
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_NUMERO, 		miForm.getNumero());
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_ANIO, 			miForm.getAnio());

			// Borramos la relacion
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGIDTIPOEJG, 	"");
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGANIO, "");
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGNUMERO, "");

			String [] campos = {ScsAsistenciasBean.C_EJGANIO, ScsAsistenciasBean.C_EJGIDTIPOEJG,ScsAsistenciasBean.C_EJGNUMERO};
			ScsAsistenciasAdm asisAdm = new ScsAsistenciasAdm(this.getUserBean(request));  
			if (!asisAdm.updateDirect(miHash, null, campos)) {
				throw new ClsExceptions ("Error de la relacion EJG - Asignación");
			}
		}
		catch (Exception e) 
		{
			throw new SIGAException("Error de la relacion EJG - Asignación", e, new String[] {"modulo.gratuita"});
		} 

		return "exito";
	}
	
	/**
	 * Metodo al que se llama al crear la relacionar un EJG con una designacion.
	 * Tendra que comprobar si la desginacion tiene relacion con una asistencia, y en caso de tenerla se crea la relacion EJG - Asistencia
	 * @param bCrear
	 * @param formulario
	 * @param request
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String relacionarConDesignaExt (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
		DefinirMantenimientoEJGForm miForm 	= (DefinirMantenimientoEJGForm)formulario;
		String mapDestino;
		Hashtable datosAsis= new Hashtable();
		
		// Relacionamos el ejg con la designacion
		mapDestino = relacionarConDesigna (true, formulario, request);

		// Recuperamos del formulario los datos de la designa que acabamos de relacionar
		String desAnio = miForm.getDesigna_anio();
		String desInst = miForm.getDesigna_idInstitucion();
		String desTurno = miForm.getDesigna_turno();
		String desNum = miForm.getDesigna_numero();
		// Buscamos las posibles relaciones de la designacion con alguna asistencia 
		ScsAsistenciasAdm asisAdm = new ScsAsistenciasAdm(this.getUserBean(request));
		datosAsis = asisAdm.getRelacionadoDesigna(desAnio, desNum, desInst, desTurno);
		Hashtable miHash = new Hashtable ();
		Hashtable datosAsisDesig= new Hashtable();
		// Si existe una asistencia relacionada recuperamos su clave
		UserTransaction tx = null;
		if (datosAsis!=null){
			String asisAnio = (String)datosAsis.get(ScsAsistenciasBean.C_ANIO);
			String asisInst = (String)datosAsis.get(ScsAsistenciasBean.C_IDINSTITUCION);
			String asisNum  = (String)datosAsis.get(ScsAsistenciasBean.C_NUMERO);
			
			
			try{
				tx=this.getUserBean(request).getTransaction();
				
				// Preparamos los datos para actualizar la asistencia
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDINSTITUCION, 	asisInst);
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_ANIO,			asisAnio);
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_NUMERO,			asisNum);
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGANIO,  		miForm.getAnio());
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGNUMERO,		miForm.getNumero());
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGIDTIPOEJG,	miForm.getIdTipoEJG());

				tx.begin();
				
				String [] campos = {ScsAsistenciasBean.C_EJGANIO, ScsAsistenciasBean.C_EJGNUMERO,ScsAsistenciasBean.C_EJGIDTIPOEJG,ScsAsistenciasBean.C_USUMODIFICACION,ScsAsistenciasBean.C_FECHAMODIFICACION};
				ScsAsistenciasAdm asiAdm = new ScsAsistenciasAdm(this.getUserBean(request));  
				if (!asiAdm.updateDirect(miHash, null, campos)) {
					throw new ClsExceptions ("Error de la relacion Asistencia - EJG: "+asiAdm.getError() );
				}
				tx.commit();
			}catch (Exception e) 
			{
			    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
			} 
			
		}else{
			String ejgAnio = miForm.getAnio();
			String ejgInst = this.getUserBean(request).getLocation();
			String ejgIdTipo = miForm.getIdTipoEJG();
			String ejgNum = miForm.getNumero();
			datosAsisDesig = asisAdm.getRelacionadoEJG(ejgAnio, ejgNum, ejgInst, ejgIdTipo);
			if (datosAsisDesig!=null){
				try{
					String asisAnio1 = (String)datosAsisDesig.get(ScsAsistenciasBean.C_ANIO);
					String asisInst1 = (String)datosAsisDesig.get(ScsAsistenciasBean.C_IDINSTITUCION);
					String asisNum1  = (String)datosAsisDesig.get(ScsAsistenciasBean.C_NUMERO);
					tx=this.getUserBean(request).getTransaction();
					
					// Preparamos los datos para actualizar la asistencia
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDINSTITUCION, 	asisInst1);
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_ANIO,			asisAnio1);
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_NUMERO,			asisNum1);
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_ANIO, 	miForm.getDesigna_anio());
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_NUMERO, miForm.getDesigna_numero());
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_TURNO, 	miForm.getDesigna_turno());
					tx.begin();
					
					String [] campos = {ScsAsistenciasBean.C_DESIGNA_ANIO, ScsAsistenciasBean.C_DESIGNA_NUMERO, ScsAsistenciasBean.C_DESIGNA_TURNO};
					ScsAsistenciasAdm asiAdm = new ScsAsistenciasAdm(this.getUserBean(request));  
					if (!asiAdm.updateDirect(miHash, null, campos)) {
						throw new ClsExceptions ("Error de la relacion Asistencia - Designa: "+asiAdm.getError() );
					}
					tx.commit();
				}catch (Exception e) 
				{
				    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
				} 
			}
			
		}
		return mapDestino;
	}
	
	
	protected String relacionarConDesigna (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
		try {
			DefinirMantenimientoEJGForm miForm 	= (DefinirMantenimientoEJGForm)formulario;
			Hashtable datosEJG=miForm.getDatos();
			
			ScsPersonaJGAdm personaAdm=new ScsPersonaJGAdm(this.getUserBean(request));
			
			Hashtable miHash = new Hashtable ();
			Vector contrarios=new Vector();
			Vector testigo=new Vector();
			Hashtable contrariosHash = new Hashtable ();
/*
			UtilidadesHash.set(miHash, ScsEJGBean.C_IDINSTITUCION, 	miForm.getIdInstitucion());
			UtilidadesHash.set(miHash, ScsEJGBean.C_IDTIPOEJG, 		miForm.getIdTipoEJG());
			UtilidadesHash.set(miHash, ScsEJGBean.C_NUMERO, 		miForm.getNumero());
			UtilidadesHash.set(miHash, ScsEJGBean.C_ANIO, 			miForm.getNumero());

			if (bCrear) {
				// Creamos la relacion
				UtilidadesHash.set(miHash, ScsEJGBean.C_DESIGNA_ANIO, 	miForm.getDesigna_anio());
				UtilidadesHash.set(miHash, ScsEJGBean.C_DESIGNA_NUMERO, miForm.getDesigna_numero());
				UtilidadesHash.set(miHash, ScsEJGBean.C_DESIGNA_IDTURNO,miForm.getDesigna_turno());
			}
			else {
				// Borramos la relacion
				UtilidadesHash.set(miHash, ScsEJGBean.C_DESIGNA_ANIO, 	"");
				UtilidadesHash.set(miHash, ScsEJGBean.C_DESIGNA_NUMERO, "");
				UtilidadesHash.set(miHash, ScsEJGBean.C_DESIGNA_IDTURNO,"");
			}

			String [] campos = {ScsEJGBean.C_DESIGNA_ANIO, ScsEJGBean.C_DESIGNA_NUMERO, ScsEJGBean.C_DESIGNA_IDTURNO};
			ScsEJGAdm ejgAdm = new ScsEJGAdm(this.getUserBean(request));  
			if (!ejgAdm.updateDirect(miHash, null, campos)) {
				throw new ClsExceptions ("Error de la relacion EJG - Designación");
			}

*/
			
			ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm(this.getUserBean(request)); 
			ScsContrariosDesignaAdm contrariosAdm =new ScsContrariosDesignaAdm(this.getUserBean(request));
			

			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_IDINSTITUCION, 	miForm.getIdInstitucion());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_ANIOEJG,			miForm.getAnio());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_NUMEROEJG,		miForm.getNumero());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_IDTIPOEJG,		miForm.getIdTipoEJG());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_ANIODESIGNA,  	miForm.getDesigna_anio());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_NUMERODESIGNA,	miForm.getDesigna_numero());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_IDTURNO,			miForm.getDesigna_turno());
  
			String sql="select ce.idpersona as IDPERSONA from scs_contrariosejg ce where ce.anio="+(String)miForm.getAnio()+" and ce.idinstitucion="+(String)miForm.getIdInstitucion()+" and ce.idtipoejg="+(String)miForm.getIdTipoEJG()+" and ce.numero="+miForm.getNumero() ;
			
			contrarios=ejgDesignaAdm.selectGenerico(sql); //Contrarios del EJG que crea/relaciona la designa
			
			contrariosHash.put(ScsContrariosDesignaBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			contrariosHash.put(ScsContrariosDesignaBean.C_ANIO,miForm.getDesigna_anio());
			contrariosHash.put(ScsContrariosDesignaBean.C_IDTURNO,miForm.getDesigna_turno());
			contrariosHash.put(ScsContrariosDesignaBean.C_NUMERO,miForm.getDesigna_numero());
			
	       // Obtenemos los solicitantes del EJG donde nos encontramos para insertarlos como interesados de la designa relacionada siempre y cuando 
			// no estén ya dados de alta.
			
			if (bCrear) {
				
				ScsUnidadFamiliarEJGAdm ejgAdm=new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
				Hashtable hUF=new Hashtable();
				hUF.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,miForm.getIdInstitucion());
				hUF.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,miForm.getIdTipoEJG());
				hUF.put(ScsUnidadFamiliarEJGBean.C_ANIO,miForm.getAnio());
				hUF.put(ScsUnidadFamiliarEJGBean.C_NUMERO,miForm.getNumero());
				hUF.put(ScsUnidadFamiliarEJGBean.C_SOLICITANTE,ClsConstants.DB_TRUE);
				Vector vSolicitantes=ejgAdm.select(hUF);
				
				// Los solicitantes del EJG los insertamos como interesados en la Designacion siempre que no existan ya.
				
				ScsDefendidosDesignaBean defendidosDesignaBean = new ScsDefendidosDesignaBean();
				defendidosDesignaBean.setAnio(new Integer(miForm.getDesigna_anio()));
				defendidosDesignaBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
				defendidosDesignaBean.setIdTurno(new Integer(miForm.getDesigna_turno()));
				defendidosDesignaBean.setNumero(new Integer(miForm.getDesigna_numero()));
				if (miForm.getDatos().get("CALIDAD")!=null && !miForm.getDatos().get("CALIDAD").equals("")){
				   defendidosDesignaBean.setCalidad((String)miForm.getDatos().get("CALIDAD"));
				}
				// Falta hacer la validacion de la persona, si no existe el idpersonajg pero existe una persona con el mismo nif o con el mismo nombre
				// y apellidos no se inserta
				
				ScsDefendidosDesignaAdm defendidosDesignaAdm = new ScsDefendidosDesignaAdm (this.getUserBean(request));
				if (vSolicitantes!=null && vSolicitantes.size()>0){
					for (int i=0;i<vSolicitantes.size();i++){
				      defendidosDesignaBean.setIdPersona(((ScsUnidadFamiliarEJGBean)vSolicitantes.get(i)).getIdPersona());	
				      try{
				      defendidosDesignaAdm.insert(defendidosDesignaBean);
				      }catch (Exception e){
				      	;
				      }
					}
				
				} 
				
				// Copiamos los contrarios del EJG, si una persona ya esta como contrario en la designa no se copia
				
				if (contrarios!=null && contrarios.size()>0){
					for (int i=0;i<contrarios.size();i++){
						String idPersonaContrario = (String)((Hashtable)contrarios.get(i)).get("IDPERSONA");
						//personaAdm.existePersona(idPersonaContrario,"","","");
						String consulta = "select * from scs_contrariosdesigna cd where cd.anio ="+(String)miForm.getDesigna_anio()+" and cd.idinstitucion ="+miForm.getIdInstitucion()+" and cd.idturno="+miForm.getDesigna_turno()+" and cd.numero="+miForm.getDesigna_numero()+" and cd.idpersona="+idPersonaContrario;
						testigo=ejgDesignaAdm.selectGenerico(consulta);
						if(testigo==null || testigo.size()==0){ //no existe como contrario en la designa
							contrariosHash.put(ScsContrariosDesignaBean.C_IDPERSONA,idPersonaContrario);
							contrariosAdm.insert(contrariosHash);
						}
					}
				}
				
				
				// Creamos la relacion
				
				//String [] campos = {ScsEJGBean.C_IDINSTITUCION,ScsEJGBean.C_ANIO,ScsEJGBean.C_NUMERO,ScsEJGBean.C_IDTIPOEJG,ScsEJGDESIGNABean.C_ANIODESIGNA, ScsEJGDESIGNABean.C_NUMERODESIGNA, ScsEJGDESIGNABean.C_IDTURNO};
				ejgDesignaAdm.insert(miHash);
				
			}
			else {
				// Borramos la relacion
				ejgDesignaAdm.delete(miHash);
			}
			
		}
		catch (Exception e) 
		{
			throw new ClsExceptions ("Error de la relacion EJG - Designación");
		} 

		return "exito";
	}

}