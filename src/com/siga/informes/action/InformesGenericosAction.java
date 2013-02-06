package com.siga.informes.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.administracion.form.InformeForm;
import com.siga.administracion.service.InformesService;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmTipoFiltroInformeBean;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.GenRecursosAdm;
import com.siga.beans.HelperInformesAdm;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsDocumentacionEJGAdm;
import com.siga.beans.ScsDocumentacionEJGBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsInclusionGuardiasEnListasAdm;
import com.siga.beans.ScsInclusionGuardiasEnListasBean;
import com.siga.beans.ScsListaGuardiasAdm;
import com.siga.beans.ScsListaGuardiasBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTipoDocumentoEJGBean;
import com.siga.certificados.Plantilla;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.CrystalReportMaster;
import com.siga.informes.InformeAbono;
import com.siga.informes.InformeCertificadoIRPF;
import com.siga.informes.InformePersonalizable;
import com.siga.informes.MasterReport;
import com.siga.informes.MasterWords;
import com.siga.informes.form.InformesGenericosForm;

import es.satec.businessManager.BusinessManager;

/**
 * Clase para la generación de informes genéricos configurados en ADM_INFORME
 * @author RGG
 */
public class InformesGenericosAction extends MasterAction {
	
	public ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String mapDestino = null;
		MasterForm miForm = null;

		try {
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion != null && accion.equalsIgnoreCase("descargaFicheroGlobal")) {
						return super.executeInternal(mapping, formulario,
								request, response);
					}else if (accion != null && accion.equalsIgnoreCase("preSeleccionInformes")) {
						return preSeleccionInformes(mapping, formulario,
								request, response);
					}else if (accion != null && accion.equalsIgnoreCase("download")) {
						return download(mapping, formulario,
								request, response);
					} 
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo INFORMES GENERICOS");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}
	}
	
	
	

	/**
	 * Metodo que permite la generación del informe homónimo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abono (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{

		try {
			UsrBean usr = this.getUserBean(request);
			//Obtenemos el formulario y sus datos:
			InformesGenericosForm miform = (InformesGenericosForm)formulario;
			EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
			File ficheroSalida = null;
			Vector informesRes = new Vector(); 
			// Obtiene del campo idInforme los ids separados por ## y devuelve sus beans
			InformeAbono informeAbono = new InformeAbono(usr);
			Vector plantillas = envioInformesGenericos.getPlantillasInforme(miform.getIdInforme(),"##",usr);
			// Obtiene del campo datosInforme los campos del formulario primcipal
			// para obtener la clave para el informe. LOs datos se obtienen en una cadena como los ocultos
			// y se sirven como un vector de hashtables por si se trata de datos multiregistro.
			Vector datos = informeAbono.obtenerDatosFormulario(miform);
			// --- acceso a paths y nombres 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			////////////////////////////////////////////////
			// MODELO DE TIPO FOP: LLAMADA A MASTER REPORT



			for (int i=0;i<plantillas.size();i++) {
				AdmInformeBean b = (AdmInformeBean) plantillas.get(i); 
				for (int j=0;j<datos.size();j++) {
					Hashtable ht = (Hashtable) datos.get(j); 
					String idAbono = (String)ht.get("idAbono");
					informesRes.add(informeAbono.generarAbono(request,usr.getLanguage(),usr.getLocation(),idAbono,b.getIdPlantilla()));
				}
			}



			/////////////////////////////////////////////////
			if (informesRes.size()!=0) {
				if (informesRes.size()<2) {
					ficheroSalida = (File) informesRes.get(0);
				} else {
					AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));
					AdmTipoInformeBean beanT = admT.obtenerTipoInforme(miform.getIdTipoInforme());
					ArrayList ficherosPDF= new ArrayList();
					for (int i=0;i<informesRes.size();i++) {
						File f = (File) informesRes.get(i);
						ficherosPDF.add(f);
					}
					String nombreFicheroZIP=beanT.getDescripcion().trim() + "_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
				rutaServidorDescargasZip += ClsConstants.FILE_SEP+miform.getIdInstitucion()+ClsConstants.FILE_SEP+"temp"+ File.separator;
					File ruta = new File(rutaServidorDescargasZip);
					ruta.mkdirs();
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
					ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				}
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}
		request.setAttribute("generacionOK","OK");
		return "descarga";	
	}
	protected String generaInfPersonalizableFacJG (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException, ClsExceptions{
		UsrBean usr = this.getUserBean(request);
		EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
		InformesGenericosForm miform = (InformesGenericosForm) formulario;
		Vector informesList = envioInformesGenericos.getPlantillasInforme(miform.getIdInforme(),"##",usr);
		
		ArrayList<HashMap<String, String>> filtrosInforme = 
			obtenerDatosFormInformeFacturacionJG(formulario, request);
		InformePersonalizable inf = new InformePersonalizable();
		File ficheroSalida = inf.getFicheroGenerado(usr, InformePersonalizable.I_INFORMEFACTSJCS, informesList,filtrosInforme);
		
			request.setAttribute("nombreFichero", ficheroSalida.getName());
			request.setAttribute("rutaFichero", ficheroSalida.getPath());
			request.setAttribute("borrarFichero", "true");
			request.setAttribute("generacionOK","OK");
			return "descarga";
		

	}
	
	protected String irpf (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		InformesGenericosForm miForm = (InformesGenericosForm) formulario;
		InformeCertificadoIRPF informeIRPF = new InformeCertificadoIRPF();
		File ficheroSalida=null;
		try {
			HttpSession session = request.getSession(false);
			String pepe = session.getId();
			SimpleDateFormat  fm = new SimpleDateFormat("ddMMyyyyhhmmssSSSS");
			String fecha = fm.format(new Date(session.getCreationTime()));
			
			
			ficheroSalida = informeIRPF.getInformeIRPF(formulario, this.getUserBean(request));

		}
		
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}

		

			request.setAttribute("nombreFichero", ficheroSalida.getName());
			request.setAttribute("rutaFichero", ficheroSalida.getPath());
			request.setAttribute("borrarFichero", "true");
			request.setAttribute("generacionOK","OK");
			return "descarga";
		




	}
	
	/**
	 * Genera informes de comunicaciones (designaciones, ejgs, morosos, etc.)
	 */
	protected String informeGenerico (ActionMapping mapping,
									  MasterForm formulario,
									  HttpServletRequest request, 
									  HttpServletResponse response,
									  String tipoInforme) 
		throws SIGAException
	{
		InformesGenericosForm miForm = (InformesGenericosForm) formulario;
		if (tipoInforme.equals(EnvioInformesGenericos.comunicacionesMorosos)){
			miForm.setClavesIteracion("idFactura");
		}
		boolean isAEnviar =  miForm.getEnviar()!=null && miForm.getEnviar().equals(ClsConstants.DB_TRUE);
		boolean isADescargar =  miForm.getDescargar()!=null && miForm.getDescargar().equals(ClsConstants.DB_TRUE);
		EnvioInformesGenericos informeGenerico = new EnvioInformesGenericos();
		
		String accessEnvio="";
		boolean isPermisoEnvio = true;
		try {
			accessEnvio = testAccess(request.getContextPath()+"/ENV_DefinirEnvios.do",null,request);
			if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
				//miForm.setEnviar(ClsConstants.DB_FALSE);
				isPermisoEnvio = false;
				ClsLogging.writeFileLog("Acceso denegado al modulo de envios, descargamos el informe",request,3);
			}
			//hacemos lo siguiente para setear el permiso de esta accion
			testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
		} catch (ClsExceptions e) {
			throw new SIGAException(e.getMsg());
		}
		File ficheroSalida=null;
		try {
			String idsesion = request.getSession().getId();
			ficheroSalida = informeGenerico.getInformeGenerico(
					miForm, idsesion, this.getUserBean(request), isAEnviar,isPermisoEnvio);
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.informes"}, e, null);
		}
		
		if (isAEnviar && isPermisoEnvio) {
			request.setAttribute("datosEnvios", informeGenerico.getDatosEnvios());
			//Para redirigeDefinicionenvios
			request.setAttribute("subModo", tipoInforme);
			request.setAttribute("idTipoEnvio", miForm.getIdTipoEnvio());
			if (isADescargar)
				request.setAttribute("descargar","1");
			else
				request.setAttribute("descargar","0");
			return "definirEnvios";
		}
		else {
			request.setAttribute("nombreFichero", ficheroSalida.getName());
			request.setAttribute("rutaFichero", ficheroSalida.getPath());
			request.setAttribute("borrarFichero", "true");
			request.setAttribute("generacionOK","OK");
			return "descarga";
		}
	} //informeGenerico()
	
	
	/**
	 * Metodo que permite la generación del informe homónimo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ejgca (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		Date inicio = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO InformesGenericos.InformeCalificacion",10);
		try {

			UsrBean usr = this.getUserBean(request);

			ScsGuardiasTurnoAdm clase=new ScsGuardiasTurnoAdm(usr);
			//Obtenemos el formulario y sus datos:
			InformesGenericosForm miform = (InformesGenericosForm)formulario;
			File ficheroSalida = null;
			Vector informesRes = new Vector(); 
			// Obtiene del campo idInforme los ids separados por ## y devuelve sus beans
			InformeAbono admInf = new InformeAbono(usr);
			EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
			Vector plantillas = envioInformesGenericos.getPlantillasInforme(miform.getIdInforme(),"##",usr);
			// Obtiene del campo datosInforme los campos del formulario primcipal
			// para obtener la clave para el informe. LOs datos se obtienen en una cadena como los ocultos
			// y se sirven como un vector de hashtables por si se trata de datos multiregistro.

			Vector datos = admInf.obtenerDatosFormulario(miform);
			// --- acceso a paths y nombres 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			Vector datosconsulta=new Vector();
			ScsEJGAdm ejgAdm= new ScsEJGAdm(usr);
			String idioma=usr.getLanguageInstitucion();
			Hashtable datoscomunes=new Hashtable();
			String codigoext="";
			String idintitucionactual=""+this.getIDInstitucion(request);			
			Vector vSalida = null;	
			String idPersonaJG="";
			Vector infosolicitante=new Vector();
			String ididioma="";
			String idTipoEJG="";
			String anio="";
			String numero="";
			////////////////////////////////////////////////
			// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS
			for (int i=0;i<plantillas.size();i++) {
				AdmInformeBean b = (AdmInformeBean) plantillas.get(i);	
				//if(b.getIdPlantilla().equals("EJGCA1")){//para los modelos con diferentes regiones
				if(b.getIdTipoInforme().equals("EJGCA")){/**se coloca para que coja los tipos informe =EJGCA(informe calificación)**/					
					//Carga en el doc los datos comunes del informe (Institucion, idfacturacion,fecha)
					if (miform.getDatosInforme() != null && !miform.getDatosInforme().trim().equals("")) {
						Hashtable aux  = (Hashtable)datos.get(0);
						if (aux!=null) {
							idTipoEJG= (String)aux.get("idTipoEJG");
							anio= (String)aux.get("anio");
							numero= (String)aux.get("numero");						
							HelperInformesAdm helperInformes = new HelperInformesAdm();								
							vSalida = ejgAdm.getCalificacionEjgSalida(idintitucionactual,idTipoEJG,anio, numero,idioma);
							/**Como accedemos al ejg por clave primaria solo nos saldra un registro**/
							Hashtable registro = (Hashtable) vSalida.get(0);			
							
							Hashtable htFuncion = new Hashtable();
							htFuncion.put(new Integer(1), idintitucionactual);
							htFuncion.put(new Integer(2), idTipoEJG);
							htFuncion.put(new Integer(3), anio);
							htFuncion.put(new Integer(4), numero);
							helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
									htFuncion, "F_SIGA_GETCONTRARIOS_EJG", "CONTRARIOS_DEFENSA_JURIDICA"));
							
							/**Aniadimos el solicitante del ejg**/
							idPersonaJG = (String)registro.get("IDPERSONAJG");
							infosolicitante=ejgAdm.getSolicitanteCalificacionEjgSalida(idintitucionactual,anio,idTipoEJG,numero,idPersonaJG,idioma);
							Hashtable datoscalificacion = (Hashtable) infosolicitante.get(0);
							/**Lenguaje del Solcitante**/
							ididioma = (String)datoscalificacion.get("IDLENGUAJE");
							if(ididioma==null || ididioma.trim().equals(""))
								ididioma="1";
							codigoext = (String)datoscalificacion.get("CODIGOLENGUAJE");		
							if(codigoext==null || codigoext.trim().equals(""))
								codigoext="ES";
							/**Datos del informe Calificacion, que son necesarios para imprimir el informe
							   y depende del idioma que tenga el solicitante,se imprimira en ese idioma el documento, si no tiene idioma
							   se imprime en el idioma de la institucion**/
							datosconsulta=ejgAdm.getDatosInformeCalificacion(idintitucionactual, idTipoEJG,anio,numero,ididioma, registro, vSalida, infosolicitante);
							
							if (datosconsulta!=null && datosconsulta.size()>0) {
								datoscomunes = (Hashtable)datosconsulta.get(0);
							}
						}
					}
    				/**se guarda en el documento con el lenguaje del solicitante**/
					String carpetaInstitucion = "";
					if(b.getIdInstitucion()==null || b.getIdInstitucion().compareTo(Integer.valueOf(0))==0){
						carpetaInstitucion = "2000";
					}else{
						carpetaInstitucion = ""+b.getIdInstitucion();
					}
					
					String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+carpetaInstitucion+ClsConstants.FILE_SEP+b.getDirectorio()+ClsConstants.FILE_SEP;
					String nombrePlantilla=b.getNombreFisico()+"_"+codigoext+".doc";
					String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+carpetaInstitucion+ClsConstants.FILE_SEP+b.getDirectorio();			

					File crear = new File(rutaAlm);
					if(!crear.exists())
						crear.mkdirs();
				
					MasterWords words=new MasterWords(rutaPl+nombrePlantilla);
					Document doc=words.nuevoDocumento(); 
					
					if (datosconsulta!=null && datosconsulta.size()>0) {
						doc=words.sustituyeRegionDocumento(doc,"Region",datosconsulta);
						doc = words.sustituyeDocumento(doc,datoscomunes);
					}
					
					String identificador=idintitucionactual+"_"+idTipoEJG+"_"+numero+".doc";
					File archivo = words.grabaDocumento(doc,rutaAlm,b.getNombreSalida()+"_"+identificador);
					informesRes.add(archivo);
				}			
				
			}

			/////////////////////////////////////////////////
			if (informesRes.size()!=0) { 
				if (informesRes.size()<2) {
					ficheroSalida = (File) informesRes.get(0);
				} else {
					AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));
					AdmTipoInformeBean beanT = admT.obtenerTipoInforme(miform.getIdTipoInforme());
					ArrayList ficherosPDF= new ArrayList();
					for (int i=0;i<informesRes.size();i++) {
						File f = (File) informesRes.get(i);
						ficherosPDF.add(f);
					}
					String nombreFicheroZIP=beanT.getDescripcion().trim() + "_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
					rutaServidorDescargasZip += ClsConstants.FILE_SEP+miform.getIdInstitucion()+ClsConstants.FILE_SEP+"temp"+ File.separator;
					File ruta = new File(rutaServidorDescargasZip);
					ruta.mkdirs();
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
					ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				}
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");
			}
			else
				throw new SIGAException("messages.general.error.ficheroNoExiste");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}
		Date fin = new Date(); 

		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN InformesGenericos.InformeCalificacion",10);
		ClsLogging.writeFileLog(fin.getTime()-inicio.getTime() + " MILISEGUNDOS ,==> SIGA: TIEMPO TOTAL",10);


		request.setAttribute("generacionOK","OK");
		return "descarga";	
	}


	/**
	 * Metodo que permite la generación del informe homónimo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String cajg (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		Date inicio = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO InformesGenericos.InformeEjg",10);
		StringBuffer avisoFicherosNoGenerado = new StringBuffer();
		try {
			UsrBean usr = this.getUserBean(request);

			ScsGuardiasTurnoAdm clase=new ScsGuardiasTurnoAdm(usr);
			//Obtenemos el formulario y sus datos:
			InformesGenericosForm miform = (InformesGenericosForm)formulario;
			File ficheroSalida = null;
			Vector informesRes = new Vector(); 
			// Obtiene del campo idInforme los ids separados por ## y devuelve sus beans
			InformeAbono admInf = new InformeAbono(usr); 
			EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
			Vector plantillas = envioInformesGenericos.getPlantillasInforme(miform.getIdInforme(),"##",usr);
			
			// Obtiene del campo datosInforme los campos del formulario primcipal
			// para obtener la clave para el informe. LOs datos se obtienen en una cadena como los ocultos
			// y se sirven como un vector de hashtables por si se trata de datos multiregistro.

			Vector datos = admInf.obtenerDatosFormulario(miform);
			// --- acceso a paths y nombres 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			////////////////////////////////////////////////
			// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS



			Hashtable hashConsultasHechas = new Hashtable();
			if(plantillas!=null &&plantillas.size()>0){
				for (int i=0;i<plantillas.size();i++) {
					AdmInformeBean beanInforme = null;
					try{
						beanInforme = (AdmInformeBean) plantillas.get(i); 
						String numero="";
						String idTipoEJG="";
						String anio="";

						for (int z=0; z<datos.size(); z++){							
							ScsEJGAdm ejgAdm= new ScsEJGAdm(usr);
							String idioma=usr.getLanguageInstitucion();
							String idiomaDatos="1";

							Hashtable clonDatoscomunes=null;							
	

							//Carga en el doc los datos comunes del informe (Institucion, idfacturacion,fecha)
							if (miform.getDatosInforme() != null && !miform.getDatosInforme().trim().equals("")) {
								Hashtable aux  = (Hashtable)datos.get(z);
								if (aux!=null) {
									idTipoEJG= (String)aux.get("idtipo");
									anio= (String)aux.get("anio");
									numero= (String)aux.get("numero");
									String idPersonaJG=null;
									if ((String)aux.get("idPersonaJG")!=null){
										idPersonaJG = (String)aux.get("idPersonaJG");
									}

									boolean isSolicitantes = beanInforme.getASolicitantes()!=null && beanInforme.getASolicitantes().equalsIgnoreCase("S");
									String keyConsultasHechas = usr.getLocation()+anio+idTipoEJG+numero+isSolicitantes;
									Vector datosconsulta = null, regionUF = null, regionConyuge = null;									
									if(hashConsultasHechas.containsKey(keyConsultasHechas)){
									// como esto se recorre para cada plantilla (for (int i=0;i<plantillas.size();i++))
									// no hace falta hacer de nuevo las consultas para cada una
										datosconsulta = (Vector) hashConsultasHechas.get(keyConsultasHechas);
										regionUF = (Vector) hashConsultasHechas.get("regionUF");
										regionConyuge = (Vector) hashConsultasHechas.get("regionConyuge");

									}else{
										datosconsulta=ejgAdm.getDatosInformeEjg(usr.getLocation(),idTipoEJG,anio,numero,
												idioma,isSolicitantes,idPersonaJG);
										regionUF = ejgAdm.getDatosRegionUF(usr.getLocation(),idTipoEJG,anio,numero);
										regionConyuge = ejgAdm.getDatosRegionConyuge(usr.getLocation(),idTipoEJG,anio,numero);
										hashConsultasHechas.put(keyConsultasHechas, datosconsulta);
										hashConsultasHechas.put("regionUF", regionUF);
										hashConsultasHechas.put("regionConyuge", regionConyuge);

									}

									HelperInformesAdm helperInformes = new HelperInformesAdm();
									String idiomainteresado="";
									String idiomainforme="";
									if (datosconsulta!=null && datosconsulta.size()>0) {
										for (int j=0;j<datosconsulta.size();j++) {
											clonDatoscomunes=(Hashtable)((Hashtable)datosconsulta.get(j)).clone();
											//Seleccionamos el idioma del interesado para seleccionar la plantilla
											//String idiomainforme=usr.getLanguageExt();		             				
											 idiomainteresado= (String) clonDatoscomunes.get("CODIGOLENGUAJE");
											if (idiomainteresado!=null && !idiomainteresado.trim().equals("")){
												idiomainforme=idiomainteresado;												
											}
											
											String carpetaInstitucion = "";
											if(beanInforme.getIdInstitucion()==null ||beanInforme.getIdInstitucion().compareTo(Integer.valueOf(0))==0){
												carpetaInstitucion = "2000";
											}else{
												carpetaInstitucion = ""+beanInforme.getIdInstitucion();
											}
											
											String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+carpetaInstitucion+ClsConstants.FILE_SEP+beanInforme.getDirectorio()+ClsConstants.FILE_SEP;
											String nombrePlantilla=beanInforme.getNombreFisico()+"_"+idiomainforme+".doc";
											String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+carpetaInstitucion+ClsConstants.FILE_SEP+beanInforme.getDirectorio();
											MasterWords words=new MasterWords(rutaPl+nombrePlantilla);
											Document doc=words.nuevoDocumento(); 
											File crear = new File(rutaAlm);
											if(!crear.exists())
												crear.mkdirs();

											doc = words.sustituyeDocumento(doc,clonDatoscomunes);
											if(regionUF!=null)doc = words.sustituyeRegionDocumento(doc, "unidadfamiliar", regionUF);
											if(regionConyuge!=null)doc = words.sustituyeRegionDocumento(doc, "conyuge", regionConyuge);

											String idinstitucion = ""+this.getIDInstitucion(request);

											String identificador=idinstitucion+"_"+idTipoEJG+"_"+numero+"_"+i+"_"+j+"_"+z+".doc";
											File archivo = words.grabaDocumento(doc,rutaAlm,beanInforme.getNombreSalida()+"_"+identificador);
											informesRes.add(archivo);
										}
									}
								}
							}
						}	
					}catch(Exception p){
						ClsLogging.writeFileLogError("Error generando un informe EJG: "+beanInforme.getDescripcion(), p, 3);
						avisoFicherosNoGenerado.append(beanInforme.getDescripcion());
						avisoFicherosNoGenerado.append(",");
					}
				}
			}else{
				throw new SIGAException("messages.informes.noPlantillas");
			}
			/////////////////////////////////////////////////
			if (informesRes.size()!=0) { 
				if (informesRes.size()<2) {
					ficheroSalida = (File) informesRes.get(0);
				} else {
					AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));
					AdmTipoInformeBean beanT = admT.obtenerTipoInforme(miform.getIdTipoInforme());
					ArrayList ficherosPDF= new ArrayList();
					for (int i=0;i<informesRes.size();i++) {
						File f = (File) informesRes.get(i);
						ficherosPDF.add(f);
					}
					String nombreFicheroZIP=beanT.getDescripcion().trim() + "_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
					rutaServidorDescargasZip += ClsConstants.FILE_SEP+miform.getIdInstitucion()+ClsConstants.FILE_SEP+"temp"+ File.separator;
					File ruta = new File(rutaServidorDescargasZip);
					ruta.mkdirs();
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
					ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				}
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");
			}
			else{
				if(!avisoFicherosNoGenerado.toString().trim().equals(""))
					throw new SIGAException("messages.informes.ningunInformeGenerado");
				else
					throw new SIGAException("messages.informes.ficheroVacio");
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}
		Date fin = new Date(); 

		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN InformesGenericos.InformeEjg",10);
		ClsLogging.writeFileLog(fin.getTime()-inicio.getTime() + " MILISEGUNDOS ,==> SIGA: TIEMPO TOTAL",10);

		if(avisoFicherosNoGenerado!=null && !avisoFicherosNoGenerado.toString().trim().equals(""))
			request.setAttribute("avisoFicherosNoGenerado",avisoFicherosNoGenerado.toString().substring(0,avisoFicherosNoGenerado.length()-1));
		request.setAttribute("generacionOK","OK");
		return "descarga";	
	}	
	
	
	
	protected String soj (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		Date inicio = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO InformesGenericos.InformeSoj",10);
		StringBuffer avisoFicherosNoGenerado = new StringBuffer();
		try {
			UsrBean usr = this.getUserBean(request);

			//Obtenemos el formulario y sus datos:
			InformesGenericosForm miform = (InformesGenericosForm)formulario;
			File ficheroSalida = null;
			Vector informesRes = new Vector(); 
			// Obtiene del campo idInforme los ids separados por ## y devuelve sus beans
			InformeAbono admInf = new InformeAbono(usr); 
			EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
			Vector plantillas = envioInformesGenericos.getPlantillasInforme(miform.getIdInforme(),"##",usr);
			
			// Obtiene del campo datosInforme los campos del formulario primcipal
			// para obtener la clave para el informe. LOs datos se obtienen en una cadena como los ocultos
			// y se sirven como un vector de hashtables por si se trata de datos multiregistro.

			Vector datos = admInf.obtenerDatosFormulario(miform);
			// --- acceso a paths y nombres 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			////////////////////////////////////////////////
			// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS



			Hashtable hashConsultasHechas = new Hashtable();
			if(plantillas!=null &&plantillas.size()>0){
				for (int i=0;i<plantillas.size();i++) {
					AdmInformeBean beanInforme = null;
					try{
						beanInforme = (AdmInformeBean) plantillas.get(i); 
						String numero="";
						String idTipoSOJ="";
						String anio="";

						for (int z=0; z<datos.size(); z++){							
							ScsDefinirSOJAdm definirSOJAdm= new ScsDefinirSOJAdm(usr);
							String idioma=usr.getLanguageInstitucion();
							String idiomaDatos="1";
							//Carga en el doc los datos comunes del informe (Institucion, idfacturacion,fecha)
							if (miform.getDatosInforme() != null && !miform.getDatosInforme().trim().equals("")) {
								Hashtable aux  = (Hashtable)datos.get(z);
								if (aux!=null) {
										if ((String)aux.get("idtipo")!=null){
											idTipoSOJ= (String)aux.get("idtipo");
										}else{
											idTipoSOJ= (String)aux.get("idTipoSOJ");
										}
										
									
										anio= (String)aux.get("anio");
										numero= (String)aux.get("numero");


									Hashtable sojHashtable =definirSOJAdm.getDatosInformeSOJ(new Integer(usr.getLocation()),
											new Integer(idTipoSOJ),new Integer(anio),new Integer(numero));

									 

									HelperInformesAdm helperInformes = new HelperInformesAdm();
									
									String idiomainforme="ES";
									if (sojHashtable!=null && sojHashtable.size()>0) {
										
										
											//Seleccionamos el idioma del interesado para seleccionar la plantilla
											//String idiomainforme=usr.getLanguageExt();		             				
											String carpetaInstitucion = "";
											if(beanInforme.getIdInstitucion()==null ||beanInforme.getIdInstitucion().compareTo(Integer.valueOf(0))==0){
												carpetaInstitucion = "2000";
											}else{
												carpetaInstitucion = ""+beanInforme.getIdInstitucion();
											}
								
											String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+carpetaInstitucion+ClsConstants.FILE_SEP+beanInforme.getDirectorio()+ClsConstants.FILE_SEP;
											String nombrePlantilla=beanInforme.getNombreFisico()+"_"+idiomainforme+".doc";
											String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+carpetaInstitucion+ClsConstants.FILE_SEP+beanInforme.getDirectorio();
											MasterWords words=new MasterWords(rutaPl+nombrePlantilla);
											Document doc=words.nuevoDocumento(); 
											File crear = new File(rutaAlm);
											if(!crear.exists())
												crear.mkdirs();
											Vector documentosSoj =  (Vector)sojHashtable.get("DOCUMENTACION_SOJ");
											sojHashtable.remove("DOCUMENTACION_SOJ");
											doc = words.sustituyeDocumento(doc,sojHashtable);
											doc = words.sustituyeRegionDocumento(doc,"documentos",documentosSoj);
											

											String idinstitucion = ""+this.getIDInstitucion(request);

											String identificador=idinstitucion+"_"+idTipoSOJ+"_"+numero+"_"+i+"_"+z+".doc";
											File archivo = words.grabaDocumento(doc,rutaAlm,beanInforme.getNombreSalida()+"_"+identificador);
											informesRes.add(archivo);

										
									}

								}
							}


						}	
					}catch(Exception p){
						ClsLogging.writeFileLogError("Error generando un informe SOJ: "+beanInforme.getDescripcion(), p, 3);
						avisoFicherosNoGenerado.append(beanInforme.getDescripcion());
						avisoFicherosNoGenerado.append(",");



					}



				}
			}else{
				throw new SIGAException("messages.informes.noPlantillas");
			}
			/////////////////////////////////////////////////
			if (informesRes.size()!=0) { 
				if (informesRes.size()<2) {
					ficheroSalida = (File) informesRes.get(0);
				} else {
					AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));
					AdmTipoInformeBean beanT = admT.obtenerTipoInforme(miform.getIdTipoInforme());
					ArrayList ficherosPDF= new ArrayList();
					for (int i=0;i<informesRes.size();i++) {
						File f = (File) informesRes.get(i);
						ficherosPDF.add(f);
					}
					String nombreFicheroZIP=beanT.getDescripcion().trim() + "_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
					rutaServidorDescargasZip += ClsConstants.FILE_SEP+miform.getIdInstitucion()+ClsConstants.FILE_SEP+"temp"+ File.separator;
					File ruta = new File(rutaServidorDescargasZip);
					ruta.mkdirs();
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
					ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				}
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");
			}
			else{
				if(!avisoFicherosNoGenerado.toString().trim().equals(""))
					throw new SIGAException("messages.informes.ningunInformeGenerado");
				else
					throw new SIGAException("messages.informes.ficheroVacio");
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}
		Date fin = new Date(); 

		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN InformesGenericos.InformeSoj",10);
		ClsLogging.writeFileLog(fin.getTime()-inicio.getTime() + " MILISEGUNDOS ,==> SIGA: TIEMPO TOTAL",10);

		if(avisoFicherosNoGenerado!=null && !avisoFicherosNoGenerado.toString().trim().equals(""))
			request.setAttribute("avisoFicherosNoGenerado",avisoFicherosNoGenerado.toString().substring(0,avisoFicherosNoGenerado.length()-1));
		request.setAttribute("generacionOK","OK");
		return "descarga";	
	}	

	/**
	 * Obtiene los filtros del formulario para generar el Informe de Facturacion de JG
	 */
	private ArrayList<HashMap<String, String>> obtenerDatosFormInformeFacturacionJG(
			ActionForm formulario, HttpServletRequest request) throws SIGAException
	{
		// Controles y Variables
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		ArrayList<HashMap<String, String>> filtros;
		HashMap<String, String> filtro;
		String idinstitucion = null;
		String idfacturaciones = null;
		String idioma = null;
		
		// obteniendo valores del formulario
		try {			
			Hashtable aux = (Hashtable) this.obtenerDatosFormulario((InformesGenericosForm) formulario).get(0);
			if (aux.containsKey("idInstitucion"))
				idinstitucion = (String) aux.get("idInstitucion");
			else
				idinstitucion = usr.getLocation();
			
			if (aux.size()==4)
				idfacturaciones = EjecucionPLs.ejecutarFuncFacturacionesIntervaloGrupos(idinstitucion, (String) aux.get("idFacturacionIni"), (String) aux.get("idFacturacionFin"), (String) aux.get("grupoFacturacion"));
			else
				idfacturaciones = (String) aux.get ("idFacturacion");
				
			idioma = usr.getLanguage();
		} catch(Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.facturacionSJCS" }, e, null);
		}

		// generando lista de filtros
		filtros = new ArrayList<HashMap<String, String>>();
		
		filtro = new HashMap<String, String>();
		filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDINSTITUCION");
		filtro.put("VALOR", idinstitucion);
		filtros.add(filtro);
		filtro = new HashMap<String, String>();
		filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "FACTURACIONES");
		filtro.put("VALOR", idfacturaciones);
		filtros.add(filtro);
		filtro = new HashMap<String, String>();
		filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDIOMA");
		filtro.put("VALOR", idioma);
		filtros.add(filtro);

		return filtros;
	} // obtenerDatosFormInformeFacturacionJG()

	/**
	 * Genera el Informe de Facturacion (SJCS > Informes > Informe de Facturacion)
	 * Este informe o carta incorpora la informacion detallada de
	 *  guardias, asistencias y asuntos de oficio entre 2 facturaciones
	 * Se trata de un informe generado con Aspose Words,
	 *  a partir de plantilla DOC multiidioma (con sufijo _ES, _CA, ...)
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String generaInfFacJG (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response)
	throws SIGAException
	{
		//Controles generales
		String idinstitucion, facturaciones="", idioma;

		UsrBean usr;
		InformesGenericosForm miform;
		AdmInformeAdm infAdm;
		Vector plantillas, datos;
		Vector informesRes;
		AdmInformeBean infBean;
		boolean multiple = false;
		String idfacturacionIni = "", idfacturacionFin = "";

		//Variables
		String baseRutaPlantilla, baseRutaAlmacen;
		String rutaPlantilla, nombrePlantilla, rutaAlmacen;
		File fRutaAlmacen;

		try
		{
			//Controles generales
			idinstitucion = "" + this.getIDInstitucion (request);
			usr = this.getUserBean (request);
			miform = (InformesGenericosForm) formulario;
			infAdm = new AdmInformeAdm (usr);
			//plantillas = infAdm.obtenerInformesTipo(idinstitucion, miform.getIdTipoInforme(), miform.getAsolicitantes(), miform.getDestinatarios());
			EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
			plantillas = envioInformesGenericos.getPlantillasInforme(miform.getIdInforme(),"##",usr);
			// del merge (revisar)
			datos = this.obtenerDatosFormulario (miform);
			//idfacturacion = "";
			idioma = "";
			if (miform.getDatosInforme() != null && 
					!miform.getDatosInforme().trim().equals(""))
			{
				Hashtable aux = (Hashtable) datos.get(0);
				if (aux != null) {
					idioma = usr.getLanguage();
					if (aux.size()==1) {
						facturaciones = (String) aux.get ("idFacturacion");
					} else {
						idfacturacionIni = (String) aux.get ("idFacturacionIni");
						idfacturacionFin = (String) aux.get ("idFacturacionFin");
						facturaciones = EjecucionPLs.ejecutarFuncFacturacionesIntervalo(idinstitucion, idfacturacionIni, idfacturacionFin);
						multiple = true;
					}
				}
			}

			//obteniendo rutas y nombres de las plantillas y ficheros a usar 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties ("SIGA.properties");
			baseRutaPlantilla = 
				rp.returnProperty ("informes.directorioFisicoPlantillaInformesJava") + 
				rp.returnProperty ("informes.directorioPlantillaInformesJava");
			baseRutaAlmacen = 
				rp.returnProperty ("informes.directorioFisicoSalidaInformesJava") + 
				rp.returnProperty ("informes.directorioPlantillaInformesJava");

			//creando la lista de informes generados
			informesRes = new Vector();

			for (int i=0; i<plantillas.size(); i++)
			{
				infBean = (AdmInformeBean) plantillas.get (i); 
				
				String carpetaInstitucion = "";
				if(infBean.getIdInstitucion()==null ||infBean.getIdInstitucion().compareTo(Integer.valueOf(0))==0){
					carpetaInstitucion = "2000";
				}else{
					carpetaInstitucion = ""+infBean.getIdInstitucion();
				}
				
				//obteniendo rutas de plantilla y fichero a generar
				rutaPlantilla = baseRutaPlantilla + ClsConstants.FILE_SEP +
				carpetaInstitucion + ClsConstants.FILE_SEP +
				infBean.getDirectorio() + ClsConstants.FILE_SEP;
				nombrePlantilla = infBean.getNombreFisico() + "_" +
				(new AdmLenguajesAdm(usr)).getLenguajeExt(idioma) + ".doc";
				rutaAlmacen = baseRutaAlmacen + ClsConstants.FILE_SEP +
				carpetaInstitucion + ClsConstants.FILE_SEP +
				infBean.getDirectorio();

				//creando la ruta del fichero a generar
				fRutaAlmacen = new File (rutaAlmacen);
				if (! fRutaAlmacen.exists()) fRutaAlmacen.mkdirs();

				//Controles de este tipo de plantilla
				MasterWords words = new MasterWords (rutaPlantilla+nombrePlantilla);
				Document doc = words.nuevoDocumento(); 
				FcsFacturacionJGAdm fac = new FcsFacturacionJGAdm (usr);
				double totalTO = 0.0d;
				double totalGA = 0.0d;

				//Campos comunes (sencillos como nombre del colegio)
				Hashtable datoscomunes = fac.obtenerComunes 
				(idinstitucion, facturaciones, idioma);

				//Campos de Turno de Oficio
				totalTO += generaRegionesTO 
				(fac, idinstitucion, facturaciones, 
						idioma, doc, words, datoscomunes);

				//Regiones de Guardias y Asistencias/Actuaciones
				if (infBean.getCodigo().equals ("INFJGM3")||infBean.getCodigo().equals ("INFJG3")) {
					totalGA += generaRegionesGA3 (fac, idinstitucion, facturaciones, 
							idioma, doc, words, datoscomunes);
					
					//comprobacion de importe de Guardias
					Double importeGuardiaAux = UtilidadesHash.getDouble ((Hashtable) datoscomunes, "IMPORTEGUARDIA");
					double importeGuardia = importeGuardiaAux == null ? 0 : importeGuardiaAux;
					if (totalGA != importeGuardia) {
						GenRecursosAdm recAdm = new GenRecursosAdm (usr);
						Vector recursos = recAdm.select (
								" where idrecurso = 'factSJCS.informes.informeFacturacion.error'" +
								"   and idlenguaje = "+idioma+" ");
						datoscomunes.put("AVISOERRORGUARDIAS", recursos.isEmpty() ? 
								"ERROR. REVISAR EXCELS" : 
								((com.siga.beans.GenRecursosBean) recursos.firstElement()).getDescripcion());
					}
					else {
						datoscomunes.put("AVISOERRORGUARDIAS", " ");
					}
				}
				else if (infBean.getCodigo().equals ("INFJGM3001")||infBean.getCodigo().equals ("INFJG3001")) {
					totalGA += generaRegionesGA3001 (fac, idinstitucion, facturaciones, 
							idioma, doc, words, datoscomunes);
				}
				
				//Total de totales
				datoscomunes.put ("TTOTAL", UtilidadesNumero.formato 
						(UtilidadesNumero.redondea (totalTO + totalGA, 2)));
				double porcentajeConsejo = Double.parseDouble (
						(new GenParametrosAdm (usr).getValor (idinstitucion, "INF", 
								"PORCENTAJE_GASTOS_CONSEJO_FACTURACION_SJCS", "0")).replace(',', '.')
				);
				datoscomunes.put ("GastosTramitacionConsell", UtilidadesNumero.formato 
						(UtilidadesNumero.redondea ((totalTO + totalGA)*porcentajeConsejo, 2)));

				//generando datos comunes en documento
				doc = words.sustituyeDocumento (doc, datoscomunes);

				//grabando el fichero fisico
				String nombreFichAlmacen;
				if (multiple){
					nombreFichAlmacen = infBean.getNombreSalida() + "_" +
					idinstitucion + "_" + idfacturacionIni+ "_" + idfacturacionFin+".doc";
				}else{
					nombreFichAlmacen = infBean.getNombreSalida() + "_" +
					idinstitucion + "_" + facturaciones+".doc";
				}
				informesRes.add (words.grabaDocumento 
						(doc, rutaAlmacen, nombreFichAlmacen));
			} //for

			//devolviendo los ficheros generados al usuario
			if (informesRes.size() > 0) {
				File ficheroSalida;

				if (informesRes.size() == 1) {
					ficheroSalida = (File) informesRes.get(0);
				}
				else {
					//si son varios ficheros, se compone un ZIP
					AdmTipoInformeAdm admT = new AdmTipoInformeAdm (usr);
					AdmTipoInformeBean beanT = admT.obtenerTipoInforme 
					(miform.getIdTipoInforme());
					ArrayList ficherosPDF = new ArrayList();
					File f;
					for (int i=0; i<informesRes.size(); i++) {
						f = (File) informesRes.get (i);
						ficherosPDF.add (f);
					}
					String nombreFicheroZIP = beanT.getDescripcion().trim() + 
					"_" + UtilidadesBDAdm.getFechaCompletaBD ("").
					replaceAll ("/", "").replaceAll (":", "").
					replaceAll (" ", "");
					String rutaServidorDescargasZip = 
						rp.returnProperty ("informes.directorioFisicoSalidaInformesJava") +
						rp.returnProperty ("informes.directorioPlantillaInformesJava");
					rutaServidorDescargasZip += ClsConstants.FILE_SEP +
					miform.getIdInstitucion() + ClsConstants.FILE_SEP +
							"temp" + File.separator;
					// RGG
					File fDelete = new File(rutaServidorDescargasZip+File.separator+nombreFicheroZIP);
					if (fDelete.exists()) {
					    fDelete.delete();
					}
					
					File ruta = new File (rutaServidorDescargasZip);
					ruta.mkdirs();
					Plantilla.doZip (rutaServidorDescargasZip, nombreFicheroZIP, ficherosPDF);
					ficheroSalida = new File (rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				}

				request.setAttribute ("nombreFichero", ficheroSalida.getName());
				request.setAttribute ("rutaFichero", ficheroSalida.getPath());
				request.setAttribute ("borrarFichero", "true");
			}
			else
				throw new SIGAException ("messages.general.error.ficheroNoExiste");
		}
		catch (Exception e) {
			throwExcp ("messages.general.error", 
					new String[] {"modulo.informes"}, e, null);
		}

		request.setAttribute ("generacionOK", "OK");
		return "descarga";
	} //generaInfFacJG()


	private double generaRegionesTO (FcsFacturacionJGAdm fac,
			String idinstitucion,
			String facturaciones,
			String idioma,
			Document doc,
			MasterWords words,
			Hashtable datoscomunes)
	throws ClsExceptions
	{
		//Variables
		Vector datosconsulta;
		int tati70=0, tatf30=0, tat100=0, tat75=0;
		double tata=0.0d, totalExtrajud=0.0d;
		Hashtable aux;

		//Regiones
		String jurisdiccion[][] =
		{
				{"1", "penal"},
				{"2", "civil"},
				{"3", "militar"},
				{"4", "social"},
				{"5", "contenciosoAdm"},
				{"6", "adm"},
				{"7", "recursos"},
				{"8", "general"},
				{"9", "laboral"},
				{"10", "extranjeria"}
		};

		for (int jj = 0; jj < jurisdiccion.length; jj++) {
			datosconsulta = fac.informeFacturaPag3_1 
			(idinstitucion, facturaciones, jurisdiccion[jj][0], idioma);
			if (datosconsulta == null || datosconsulta.size() < 1) {
				datosconsulta = new Vector();
				aux = new Hashtable();
				aux.put ("NOMBRE_MODULO", "");
				aux.put ("CODIGO_MODULO", "");
				aux.put ("VALOR_MODULO", "");
				aux.put ("INICIO", "");
				aux.put ("FIN", "");
				aux.put ("INICIOFIN", "");
				aux.put ("EXTRAJUD", "");
				aux.put ("NUM_ASUNTOS", "");
				aux.put ("VALOR", "");
				aux.put ("VALOR_NUM", "");
				aux.put ("VALOR_EXTRAJUD", "");
				aux.put ("VALOR_NUM_EXTRAJUD", "");
				aux.put ("VALOR_NORMALYEXTRAJUD", "");
				datosconsulta.add (aux);
			}else{
				//jbd // Sobreescribimos los valores con los valores numericos para trabajar siempre con los mismos datos
				for (Iterator iter=datosconsulta.iterator(); iter.hasNext(); ){
					Hashtable hash = (Hashtable)iter.next();
					String valor = UtilidadesNumero.formato((String)hash.get("VALOR_NUM"));
					hash.put("VALOR", valor);
					hash.put("VALOR_EXTRAJUD", UtilidadesNumero.formato((String)hash.get("VALOR_NUM_EXTRAJUD")));
					hash.put("VALOR_NORMALYEXTRAJUD", UtilidadesNumero.formato (UtilidadesNumero.redondea (
							Double.parseDouble((String)hash.get("VALOR_NUM")) + 
							Double.parseDouble((String)hash.get("VALOR_NUM_EXTRAJUD")), 2)));
				}
			}

			doc = words.sustituyeRegionDocumento
			(doc, jurisdiccion[jj][1], datosconsulta);
			tati70			+= this.getTotal (datosconsulta, "INICIO");
			tatf30			+= this.getTotal (datosconsulta, "FIN");
			tat100			+= this.getTotal (datosconsulta, "INICIOFIN");
			tat75			+= this.getTotal (datosconsulta, "EXTRAJUD");
			tata			+= this.getTotal (datosconsulta, "VALOR_NUM");
			tata			+= this.getTotal (datosconsulta, "VALOR_NUM_EXTRAJUD");
			totalExtrajud	+= this.getTotal (datosconsulta, "VALOR_NUM_EXTRAJUD");
		}

		//Campos de totales
		datoscomunes.put ("TATI70", String.valueOf (tati70));
		datoscomunes.put ("TATF30", String.valueOf (tatf30));
		datoscomunes.put ("TAT100", String.valueOf (tat100));
		datoscomunes.put ("TAT75", String.valueOf (tat75));
		datoscomunes.put ("TATTODOS", String.valueOf (tati70+tatf30+tat100+tat75));
		datoscomunes.put ("TATA", UtilidadesNumero.formato 
				(UtilidadesNumero.redondea (tata, 2)));
		datoscomunes.put ("TOTALEXTRAJUD", UtilidadesNumero.formato 
				(UtilidadesNumero.redondea (totalExtrajud, 2)));

		return tata;
	} //generaRegionesTO()

	
	private double generaRegionesGA3 (FcsFacturacionJGAdm fac,
									  String idinstitucion,
									  String facturaciones,
									  String idioma,
									  Document doc,
									  MasterWords words,
									  Hashtable datoscomunes)
		throws ClsExceptions, SIGAException
	{
		//Variables
		Vector datosconsulta;
		double total=0.0d;
		
		//Regiones del documento
		String[] region =
		{
				"ApuntesPorHitoYGuardia"
		};
		int regionUnica = 0;
		
		//Sustitucion en el documento
		datosconsulta = fac.informeFJGPorHitos 
				(idinstitucion, facturaciones, region[regionUnica], idioma);
		if (datosconsulta==null || datosconsulta.size()<1) {
			datosconsulta = new Vector();
			Hashtable aux = new Hashtable();
			aux.put ("TURNO", "");
			aux.put ("GUARDIA", "");
			aux.put ("VG", "");
			aux.put ("HITO", "");
			aux.put ("HITODESC", "");
			aux.put ("PRECIO", "");
			aux.put ("PRECIO_NUM", "");
			aux.put ("NUMERO", "");
			aux.put ("IMPORTE", "");
			aux.put ("IMPORTE_NUM", "");
			datosconsulta.add (aux);
		}
		doc = words.sustituyeRegionDocumento
				(doc, region[regionUnica], datosconsulta);
		
		total += this.getTotal (datosconsulta, "IMPORTE_NUM");
		
		//Campos de totales de Violencia de Genero
		datoscomunes.put ("TOTAL", UtilidadesNumero.formato
				(UtilidadesNumero.redondea (total, 2)));
		
		return (total);
	} //generaRegionesGA3()

	private double generaRegionesGA3001 (FcsFacturacionJGAdm fac,
			String idinstitucion,
			String facturaciones,
			String idioma,
			Document doc,
			MasterWords words,
			Hashtable datoscomunes)
	throws ClsExceptions
	{
		//Variables
		Vector datosconsulta;
		Hashtable aux;
		double totalGA=0.0d;

		String[] region =
		{
				"GuardiasSimples_NoVG", "GuardiasSimples_SiVG", 
				"GuardiasDobladas_NoVG", "GuardiasDobladas_SiVG", 
				"MaximosPorAsistencias_NoVG", "MaximosPorAsistencias_SiVG", 
				"MaximosPorActuaciones_NoVG", "MaximosPorActuaciones_SiVG", 
				"Asistencias_NoVG", "Asistencias_SiVG", 
				"Actuaciones_NoVG", "Actuaciones_SiVG",
				"ActuacionesFG_NoVG", "ActuacionesFG_SiVG",

				//las siguientes regiones son uniones de las de arriba
				"GuardiasDobladas+Maximos_NoVG", "GuardiasDobladas+Maximos_SiVG", 
				"Actuac+ActuacFG_NoVG", "Actuac+ActuacFG_SiVG"
		};
		int inicioRegionesUniones = 14;
		String regionSuma;
		Object objSumaAnterior;
		String strSumaAnterior;
		int intSumaAnterior, intSumaActual;

		for (int jj = 0; jj < region.length; jj++) {
			datosconsulta = fac.informeFJGAsistencias (idinstitucion, facturaciones, region[jj], idioma);
			if (datosconsulta == null || datosconsulta.size() < 1) {
				datosconsulta = new Vector();
				aux = new Hashtable();
				aux.put ("CANTIDAD", "0");
				aux.put ("PRECIO", UtilidadesNumero.formato ("0"));
				aux.put ("IMPORTE", UtilidadesNumero.formato ("0"));
				datosconsulta.add (aux);
			}

			//obteniendo cantidad actual
			intSumaActual = (int)this.getTotal (datosconsulta, "CANTIDAD");
			datoscomunes.put ("CAN_"+region[jj], (""+intSumaActual));

			//obteniendo cantidad independiente de VG
			regionSuma = "CAN_"+region[jj].substring(0, region[jj].length()-5)+"_InVG";
			objSumaAnterior = datoscomunes.get (regionSuma);
			strSumaAnterior = ((String) (objSumaAnterior == null ? "0" : objSumaAnterior));
			intSumaAnterior = Integer.parseInt (strSumaAnterior.equals ("") ? "0" : strSumaAnterior);
			datoscomunes.put (regionSuma, "" + (intSumaAnterior + intSumaActual));

			//obteniendo importe actual
			datoscomunes.put ("IMP_"+region[jj],
					UtilidadesNumero.formato (UtilidadesNumero.redondea
							(this.getTotal (datosconsulta, "IMPORTE_NUM"), 2)));

			//no hay que sumar las regiones que son uniones
			if (jj < inicioRegionesUniones)
				totalGA += this.getTotal (datosconsulta, "IMPORTE_NUM");

			//sustituyendo regiones en el documento Word
			doc = words.sustituyeRegionDocumento 
			(doc, region[jj], datosconsulta);
		}

		//Campos de totales de Guardas y Asistencias
		datoscomunes.put ("TANUMA", UtilidadesNumero.formato 
				(UtilidadesNumero.redondea (totalGA, 2)));

		return totalGA; 
	} //generaRegionesGA3001()



	private double getTotal (Vector v, String campo)
	{
		double total = 0.0d;

		try {
			if (v != null) {
				for(int i=0; i<v.size(); i++) {
					Double d = UtilidadesHash.getDouble 
					((Hashtable) v.get(i), campo);
					if (d != null)
						total += d.doubleValue();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			total = 0.0d;
		}

		return total;
	} //getTotal()

	
	/**
	 * Metodo que permite la generación del informe homónimo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String listaGuardias (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{

		try {
			UsrBean usr = this.getUserBean(request);
			//Obtenemos el formulario y sus datos:
			InformesGenericosForm miform = (InformesGenericosForm)formulario;
			File ficheroSalida = null;
			Vector informesRes = new Vector(); 
			// Obtiene del campo idInforme los ids separados por ## y devuelve sus beans
			InformeAbono admInf = new InformeAbono(usr);
			Vector plantillas = admInf.obtenerPlantillasFormulario(miform, usr);
			// Obtiene del campo datosInforme los campos del formulario primcipal
			// para obtener la clave para el informe. LOs datos se obtienen en una cadena como los ocultos
			// y se sirven como un vector de hashtables por si se trata de datos multiregistro.
			Vector datos = admInf.obtenerDatosFormulario(miform);
			// --- acceso a paths y nombres 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");

			////////////////////////////////////////////////
			// MODELO DE TIPO CR: LLAMADA A CRYSTALREPORTMASTER



			for (int i=0;i<plantillas.size();i++) {
				AdmInformeBean b = (AdmInformeBean) plantillas.get(i); 
				for (int j=0;j<datos.size();j++) {
					Hashtable ht = (Hashtable) datos.get(j); 
					String fechaInicio = (String)ht.get("fechaInicio");
					String fechaFin = (String)ht.get("fechaFin");
					String idInstitucion = miform.getIdInstitucion();
					String idLista = (String)ht.get("idLista");

					// Las listas de guardias a analizar
					ScsListaGuardiasAdm admL =new ScsListaGuardiasAdm(this.getUserBean(request));
					Hashtable paramBusqueda=new Hashtable();
					paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,idInstitucion);
					paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,idLista);
					Vector vLista=admL.selectByPK(paramBusqueda);
					ScsListaGuardiasBean bLista=null;
					if (vLista!=null && vLista.size()>0) {
						bLista = (ScsListaGuardiasBean) vLista.get(0);
					}

					String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+miform.getIdInstitucion()+ClsConstants.FILE_SEP+b.getDirectorio()+ClsConstants.FILE_SEP;
					String nombrePlantilla=b.getNombreFisico()+"_"+usr.getLanguageExt()+".rpt";
					String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+b.getDirectorio();
					File rutaPDF=new File(rutaAlm);
					rutaPDF.mkdirs();
					if(!rutaPDF.canWrite()){
						throw new SIGAException("messages.informes.generico.noPlantilla");					
					}
					rutaAlm+=ClsConstants.FILE_SEP;
					String nombrePDF = b.getNombreSalida()+"_"+fechaInicio.replaceAll("/","")+"_"+fechaFin.replaceAll("/","")+"_"+idInstitucion+"_"+idLista+".pdf";

					Hashtable param = new Hashtable();
					param.put("fecha_ini",fechaInicio);
					param.put("fecha_fin",fechaFin);
					param.put("IDINSTITUCION",new Integer(idInstitucion));
					param.put("idlista",bLista.getIdLista());
					param.put("LUGAR",bLista.getLugar());
					param.put("OBSERVACIONES",bLista.getObservaciones());
					param.put("FECHAINICIO_CALENDARIO",fechaInicio);
					param.put("FECHAFIN_CALENDARIO",fechaFin);

					informesRes.add(CrystalReportMaster.generarPDF(rutaPl+nombrePlantilla, rutaAlm+nombrePDF, param));
				}
			}



			/////////////////////////////////////////////////
			if (informesRes.size()!=0) {
				if (informesRes.size()<2) {
					ficheroSalida = (File) informesRes.get(0);
				} else {
					AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));
					AdmTipoInformeBean beanT = admT.obtenerTipoInforme(miform.getIdTipoInforme());
					ArrayList ficherosPDF= new ArrayList();
					for (int i=0;i<informesRes.size();i++) {
						File f = (File) informesRes.get(i);
						ficherosPDF.add(f);
					}
					String nombreFicheroZIP=beanT.getDescripcion().trim() + "_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
				rutaServidorDescargasZip += ClsConstants.FILE_SEP+miform.getIdInstitucion()+ClsConstants.FILE_SEP+"temp"+ File.separator;
					File ruta = new File(rutaServidorDescargasZip);
					ruta.mkdirs();
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
					ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				}
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");	
			}
			else
				throw new SIGAException("messages.general.error.ficheroNoExiste");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}
		request.setAttribute("generacionOK","OK");
		return "descarga";	
	}



	private Vector obtenerDatosFormulario(InformesGenericosForm form) throws ClsExceptions {
		Vector salida = new Vector ();
		String datosInforme = form.getDatosInforme();
		if(datosInforme.endsWith("%%%")){
			int indice = datosInforme.lastIndexOf("%%%");
			datosInforme = datosInforme.substring(0,indice);
		}



		try {
			GstStringTokenizer st1 = new GstStringTokenizer(datosInforme,"%%%");
			while (st1.hasMoreTokens()) {
				Hashtable ht = new Hashtable();
				String registro = st1.nextToken();
				GstStringTokenizer st = new GstStringTokenizer(registro,"##");
				while (st.hasMoreTokens()) {
					String dupla = st.nextToken();
					String d[]= dupla.split("==");
					if (d.length == 2)
						ht.put(d[0],d[1]);    
				}
				salida.add(ht);
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al obtener los datos del formulario.");
		}
		return salida;
	}

	private Vector obtenerPlantillasFormulario(InformesGenericosForm form, UsrBean usr) throws ClsExceptions {
		Vector salida = new Vector ();
		try {
			AdmInformeAdm adm = new AdmInformeAdm(usr);
			StringTokenizer st = new StringTokenizer(form.getIdInforme(),"##");
			while (st.hasMoreTokens()) {
				String id = st.nextToken();
				salida.add(adm.obtenerInforme(form.getIdInstitucion(),id));
			}
		} catch (ClsExceptions e) {
			throw e;
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al obtener los objetos plantillas del formulario.");
		}
		return salida;
	}


	
	/**
	 * Este metodo no convierte el vector de datos que viene de la jsp y los trasforma en
	 * un hashtable donde las clavs son las persona. Luego los envios a morosos se haran por persona.
	 * @return
	 * @throws ClsExceptions 
	 */
	private Hashtable getFacturasPersonaAComunicar(InformesGenericosForm form) throws ClsExceptions{
		Hashtable htFacturasPersona = new Hashtable();
		ArrayList alFacturas = null;
		MasterReport masterReport = new MasterReport();
		Vector vCampos = masterReport.obtenerDatosFormulario(form);


		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			String idPersona = (String) ht.get("idPersona");
			String idFactura = (String) ht.get("idFactura");

			if(htFacturasPersona.containsKey(idPersona)){
				alFacturas = (ArrayList) htFacturasPersona.get(idPersona);

			}else{
				alFacturas = new ArrayList();

			}
			alFacturas.add(idFactura);
			htFacturasPersona.put(idPersona, alFacturas);


		}


		return htFacturasPersona;


	}
	private String getDatosInformePersonaFactura(String idPersona, ArrayList alFacturas, String pathDocumento){
		StringBuffer sReturn = new StringBuffer();


		for (int i = 0; i < alFacturas.size(); i++) {
			String idFactura = (String) alFacturas.get(i);
			sReturn.append("idPersona==");
			sReturn.append(idPersona);
			sReturn.append("##idFactura==");
			sReturn.append(idFactura);
			sReturn.append("##pathDocumento==");
			sReturn.append(pathDocumento);
			sReturn.append("%%%");	


		}

		return sReturn.toString();

	}

	private String dejg (ActionMapping mapping, InformesGenericosForm form, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException, ClsExceptions {
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
		String idioma =  userBean.getLanguageExt();
		String idiomaInt =  userBean.getLanguage();			
		
		MasterReport masterReport = new MasterReport();
		Vector datos = masterReport.obtenerDatosFormulario(form.getDatosInforme().toUpperCase());
		//IDINSTITUCION==2040##IDTIPO==1##ANIO==2012##NUMERO==80##IDTIPOINFORME==DEJG%%%;
		
		
		Hashtable ht = (Hashtable)datos.get(0);
		String idInstitucion = (String)ht.get("IDINSTITUCION");
		String idTipoEJG = (String)ht.get("IDTIPO");
		String anio = (String)ht.get("ANIO");
		String numero = (String)ht.get("NUMERO");
		ht.put("IDTIPOEJG",idTipoEJG);

		try {
			ScsPersonaJGAdm perAdm = new ScsPersonaJGAdm(userBean);					
			String idlenguajeSolicitante = perAdm.getLenguajeSolicitante(ht);
			
			if (idlenguajeSolicitante!=null && !idlenguajeSolicitante.trim().equals("")){
				idiomaInt=idlenguajeSolicitante;
				
				switch (Integer.parseInt(idiomaInt)) {
					case 1:  idioma="ES"; break;
					case 2:  idioma="CA"; break;
					case 3:  idioma="EU"; break;
					case 4:  idioma="GL"; break;	
				}
			}		
			
			GstDate gstDate = new GstDate();
			String hoy = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request));
			ArrayList informesRes = new ArrayList(); 
			EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
			Vector plantillas = envioInformesGenericos.getPlantillasInforme(form.getIdInforme(),"@@",userBean);
			
			String presentador = null;
			String solicitanteParentesco, documentacion;

			if (plantillas == null || plantillas.size() == 0) {
				throw new SIGAException("messages.informes.noPlantillas");
			}

			String rutaAlm = null;
			int cuentaArchivos = 0;

			// --- acceso a paths y nombres 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");


			ScsDocumentacionEJGAdm scsDocumentacionEJGAdm = new ScsDocumentacionEJGAdm(getUserBean(request));
			RowsContainer rowscontainer = scsDocumentacionEJGAdm.getDocumentacionEJG(idInstitucion, idTipoEJG, anio, numero, idiomaInt);

			if (rowscontainer == null || rowscontainer.size() == 0) {
				throw new SIGAException("messages.informes.noDocumentos");
			}

			if (rowscontainer != null) {

				for (int i=0;i<plantillas.size();i++) {
					//por cada plantilla hago un word

					int j = 0;

					Row rowAnterior = new Row();
					Row rowActual = (Row)rowscontainer.get(j);
					String datosDoc = null;
					String idiomaIntPer = rowActual.getString("IDLENGUAJE");
					String idiomaPer = ""; 
					if (idiomaIntPer==null || idiomaIntPer.equals("")) {
						idiomaIntPer = idiomaInt;
						idiomaPer = idioma;
					} else {
						AdmLenguajesAdm admL = new AdmLenguajesAdm(this.getUserBean(request));
						idiomaPer=admL.getCodigoExt(idiomaIntPer);
					}

					AdmInformeBean b = (AdmInformeBean) plantillas.get(i);
					String directorioInforme = b.getDirectorio();
					String nombreFisicoInforme = b.getNombreFisico();
					String nombreSalidaInforme = b.getNombreSalida();
					String carpetaInstitucion = "";
					if(b.getIdInstitucion()==null ||b.getIdInstitucion().compareTo(Integer.valueOf(0))==0){
						carpetaInstitucion = "2000";
					}else{
						carpetaInstitucion = ""+b.getIdInstitucion();
					}
					String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+carpetaInstitucion+ClsConstants.FILE_SEP+directorioInforme+ClsConstants.FILE_SEP;
					String nombrePlantilla=nombreFisicoInforme+"_"+idiomaPer+".doc";
					rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+carpetaInstitucion+ClsConstants.FILE_SEP+directorioInforme;

					File crear = new File(rutaAlm);
					if(!crear.exists())
						crear.mkdirs();


					Hashtable htCabeceraInforme = null;

					Vector vDatosDoc = new Vector();
					MasterWords words = null;
					Document doc = null;

					boolean continua = true;



					words=new MasterWords(rutaPl+nombrePlantilla);			
					doc = words.nuevoDocumento();
					doc.getMailMerge().setRemoveEmptyParagraphs(true);
					htCabeceraInforme = new Hashtable();
					htCabeceraInforme.put("NEXPEDIENTE", rowActual.getString(ScsEJGBean.C_NUMEJG));	
					htCabeceraInforme.put(ScsEJGBean.C_ANIO, rowActual.getString(ScsEJGBean.C_ANIO));	
					htCabeceraInforme.put(ScsPersonaJGBean.C_DIRECCION, rowActual.getString(ScsPersonaJGBean.C_DIRECCION));	
					htCabeceraInforme.put(ScsPersonaJGBean.C_CODIGOPOSTAL, rowActual.getString(ScsPersonaJGBean.C_CODIGOPOSTAL));	
					htCabeceraInforme.put("PROVINCIA", rowActual.getString("PROVINCIA"));	
					htCabeceraInforme.put("PAIS", rowActual.getString("PAIS"));	
					htCabeceraInforme.put("POBLACION", rowActual.getString("POBLACION"));	

					String fechaLimitePresentacion = (String)rowActual.getValue(ScsEJGBean.C_FECHALIMITEPRESENTACION);
					if (fechaLimitePresentacion == null) {
						fechaLimitePresentacion = "";
					} else {
						fechaLimitePresentacion = GstDate.getFormatedDateShort("", fechaLimitePresentacion);
					}
					
					htCabeceraInforme.put("FECLIMITEPRESENTA", fechaLimitePresentacion);		
					
					String fechaPresentacion = (String)rowActual.getValue(ScsEJGBean.C_FECHAPRESENTACION);
					if (fechaPresentacion == null) {
						fechaPresentacion = "";
					} else {
						fechaPresentacion = GstDate.getFormatedDateShort("", fechaPresentacion);
					}
					htCabeceraInforme.put("FECPRESENTA", fechaPresentacion);				

					String nombre = (String)rowActual.getValue(ScsPersonaJGBean.C_NOMBRE);
					String apellido1 = (String)rowActual.getValue(ScsPersonaJGBean.C_APELLIDO1);
					String apellido2 = (String)rowActual.getValue(ScsPersonaJGBean.C_APELLIDO2);

					if (nombre != null) {
						if (apellido1 != null) {
							nombre += " " + apellido1;
							if (apellido2 != null) {
								nombre += " " + apellido2;
							}
						}
						htCabeceraInforme.put("NOMBRE", nombre);
					}

					htCabeceraInforme.put("SYSDATE", hoy);
					doc.getMailMerge().setRemoveEmptyParagraphs(true);

					words.sustituyeDocumento(doc, htCabeceraInforme);
					vDatosDoc = new Vector();


					while (continua) {			

						boolean distintoPresentador = !rowActual.getString(ScsDocumentacionEJGBean.C_PRESENTADOR).equals(rowAnterior.getString(ScsDocumentacionEJGBean.C_PRESENTADOR));
						boolean distintoIdtipoDoc = !rowActual.getString(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG).equals(rowAnterior.getString(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG));						

						Hashtable hash = new Hashtable();
						hash.put("PRESENTADOR", "");
						hash.put("DES_TIPODOC", "");						

						if (distintoPresentador) {
							nombre = (String)rowActual.getValue(ScsPersonaJGBean.C_NOMBRE);
							apellido1 = (String)rowActual.getValue(ScsPersonaJGBean.C_APELLIDO1);
							apellido2 = (String)rowActual.getValue(ScsPersonaJGBean.C_APELLIDO2);
							solicitanteParentesco = (String)rowActual.getValue("SOLICITANTE_PARENTESCO");

							if (nombre != null) {
								if (apellido1 != null) {
									nombre += " " + apellido1;
									if (apellido2 != null) {
										nombre += " " + apellido2;
									}
								}
								presentador = "\r\n\r\n" + UtilidadesString.getMensajeIdioma(idiomaIntPer,"gratuita.documentacionEJG.literal.documentacionPresentar") + nombre.trim();
								if (solicitanteParentesco != null && !solicitanteParentesco.trim().equals("")) {
									presentador += " (" + solicitanteParentesco + ")";
								}
								hash.put("PRESENTADOR", presentador);
							}

						}

						if (distintoIdtipoDoc || distintoPresentador) {			
							hash.put("DES_TIPODOC", "\r\n" + rowActual.getString("DES_TIPODOC"));
						}				

						datosDoc = "* " + rowActual.getString("DES_DOCU");
						documentacion = (String)rowActual.getValue(ScsDocumentacionEJGBean.C_DOCUMENTACION);							

						if (documentacion != null && !documentacion.trim().equals("")) {
							datosDoc.trim();
							datosDoc += "\n" + documentacion.trim();
						}

						hash.put("DES_DOCU", datosDoc.trim());
					
						String fechalimitedoc = (String)rowActual.getString("FECLIMITEPRESENTADOC");
						if (fechalimitedoc == null) {
							fechalimitedoc = "";
						} else {
							fechalimitedoc = GstDate.getFormatedDateShort("", fechalimitedoc);
						}
				
						String fechaentregadoc = (String)rowActual.getString("FECPRESENTACIONDOC");
						if (fechaentregadoc == null) {
							fechaentregadoc = "";
						} else {
							fechaentregadoc = GstDate.getFormatedDateShort("", fechaentregadoc);
						}
					
						hash.put("FECLIMITEPRESENTADOC",fechalimitedoc);
						hash.put("FECPRESENTACIONDOC",fechaentregadoc);
					
						vDatosDoc.add(hash);						
						

						j++;

						rowAnterior = rowActual;

						if (j < rowscontainer.size()) {
							rowActual = (Row)rowscontainer.get(j);
						} else {
							break;
						}						

					}

					doc.getMailMerge().setRemoveEmptyParagraphs(true);
					words.sustituyeRegionDocumento(doc, "R0a", vDatosDoc);

//					DataMailMergeDataSource dataMerge = new DataMailMergeDataSource("R0a", vDatosDoc);
//					doc.getMailMerge().setRemoveEmptyParagraphs(true);
//					doc.getMailMerge().executeWithRegions(dataMerge);

//					dataMerge = new DataMailMergeDataSource("R0a", vDatosDoc);

//					doc.getMailMerge().executeWithRegions(dataMerge);

					StringBuffer nombreArchivo = new StringBuffer(); 
					nombreArchivo.append(nombreSalidaInforme);
					nombreArchivo.append("_");
					nombreArchivo.append(idInstitucion);
					nombreArchivo.append("_");

					File file = new File(rutaAlm + ClsConstants.FILE_SEP + nombreArchivo.toString() + (++cuentaArchivos) + ".doc");
					while (file.exists()) {
						file = new File(rutaAlm + ClsConstants.FILE_SEP + nombreArchivo.toString() + (++cuentaArchivos) + ".doc");
					}
					nombreArchivo.append((cuentaArchivos) + ".doc");

					File archivo = words.grabaDocumento(doc,rutaAlm,nombreArchivo.toString());
					informesRes.add(archivo);


				}
			}

			File ficheroSalida = null;

			if (informesRes.size() == 1) {
				ficheroSalida = (File)informesRes.get(0);
			} else if (informesRes.size() > 1){

				StringBuffer nombreArchivo = new StringBuffer(); 
				nombreArchivo.append("documentacionEJG");
				nombreArchivo.append("_");
				nombreArchivo.append(idInstitucion);
				nombreArchivo.append("_");

				String pathdocumento = rutaAlm+ClsConstants.FILE_SEP+nombreArchivo;
				cuentaArchivos = 0;

				File file = new File(pathdocumento + (++cuentaArchivos));
				while (file.exists()) {
					file = new File(pathdocumento + (++cuentaArchivos));
				}
				pathdocumento += cuentaArchivos;

				ficheroSalida = MasterWords.doZip(informesRes, pathdocumento);
			}

			if (ficheroSalida != null) {			
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");
			}


		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		request.setAttribute("generacionOK","OK");
		return "descarga";		


	}
	public ActionForward preSeleccionInformes (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		UsrBean usr = this.getUserBean(request);
		try { 
			InformesGenericosForm miForm = (InformesGenericosForm) formulario;
			String idTipoInforme = miForm.getIdTipoInforme();
			
			if (idTipoInforme.equals("") &&  miForm.getIdInforme().equals("")) 
				throw new ClsExceptions("Error en generación de informe: No se ha indicado el tipo de informe ni el informe.");
			

			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));
			// mostramos la ventana con la pregunta
			Vector informeBeans=adm.obtenerInformesTipo(miForm.getIdInstitucion(),idTipoInforme,miForm.getAsolicitantes(), miForm.getDestinatarios());
			if(informeBeans==null||informeBeans.size()==0)
				throw new SIGAException("messages.informes.noPlantillas");
			
			
			
			if(miForm.getEnviar()!=null && miForm.getEnviar().equals(ClsConstants.DB_TRUE) ){
				String accessEnvio = testAccess(request.getContextPath()+"/ENV_DefinirEnvios.do",null,request);
				if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
						miForm.setEnviar(ClsConstants.DB_FALSE);
						ClsLogging.writeFileLog("Acceso denegado al modulo de envios, descargamos el informe",request,3);
				}
				//hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
				
			}
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			List<InformeForm> informeForms = new ArrayList<InformeForm>();
			for (int i = 0; i < informeBeans.size(); i++) {
				AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(i);
				InformeForm informe = informeBean.getInforme(); 
				if(miForm.getEnviar()!=null && miForm.getEnviar().equals(ClsConstants.DB_TRUE) ){
					List<EnvTipoEnviosBean> tipoEnviosBeans = informeService.getTiposEnvioPermitidos(informeBean,usr);
					informe.setTiposEnvioPermitidos(tipoEnviosBeans);
				}
				informeForms.add(informe);

			}
			request.setAttribute("informeForms",informeForms);
			
			
			
			
			String asunto = "";
			if(idTipoInforme.equals(EnvioInformesGenericos.comunicacionesDesigna)){
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "informes.genericos.designas.asunto");
				
				
			}
			else if(idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesEjg)){
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "informes.genericos.ejg.asunto");
			}else if(idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesMorosos)){
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "facturacion.consultamorosos.mail.asunto");
			}else if (idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesCenso)){
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "informes.genericos.censo.asunto");
			}else if (idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesFacturacionesColegiados)){
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "informes.sjcs.facturacion.envio.asunto");
			}else if (idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesPagoColegiados)){
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "informes.sjcs.pagos.envio.asunto");
			}else if (idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesJustificacion)){
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "informes.genericos.justificacion.asunto");
			}else if (idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesExpedientes)){
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "informes.genericos.expedientes.asunto");
			} else if (idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesListadoGuardias)){
				
				String datosInforme = getDatosInformeListaGuardias(miForm.getDatosInforme(), usr);
				miForm.setDatosInforme(datosInforme);
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "informes.sjcs.lista.envio.guardias");
			}
			else if (idTipoInforme.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesIRPF)){
				
				asunto = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "gratuita.retencionesIRPF.informe.envio.asunto");
				InformeCertificadoIRPF certificadoIRPF = new InformeCertificadoIRPF();
				Vector datosInformeVector = certificadoIRPF.obtenerDatosFormulario(miForm.getDatosInforme());
				Hashtable datosInformeHashtable = (Hashtable)datosInformeVector.get(0);
				String idPersona = (String) datosInformeHashtable.get("idPersona");
				String periodo = (String) datosInformeHashtable.get("periodo");
				String anyoInformeIRPF = (String) datosInformeHashtable.get("anyoInformeIRPF");
				String idInstitucionAux = (String) datosInformeHashtable.get("idInstitucion");
				String idioma = (String) datosInformeHashtable.get("idioma");
				String datosAEnviar = certificadoIRPF.getDatosInforme(idInstitucionAux, periodo,
						anyoInformeIRPF, idioma, idPersona, usr);
				miForm.setDatosInforme(datosAEnviar);
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
				
			}else if (idTipoInforme.equalsIgnoreCase("FACJG")||idTipoInforme.equalsIgnoreCase("FJGM")||idTipoInforme.equalsIgnoreCase("FACJ2")){
				
				if(informeBeans.size()==1){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
			}else if(idTipoInforme.equals(EnvioInformesGenericos.comunicacionesCAJG)){
				miForm.setEnviar("0");
				/*if(informeBeans.size()==1){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+ "";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}*/
				
			}else if(idTipoInforme.equals("ABONO")){
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
			}else if(idTipoInforme.equals("ACTAC")){
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
			}else if(idTipoInforme.equals("SOJ")){
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
			}else if(idTipoInforme.equals("DEJG")){
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
			}else if(idTipoInforme.equals("EJGCA")){
				if(informeBeans.size()==1&&miForm.getEnviar().equals(ClsConstants.DB_FALSE)){
					AdmInformeBean informeBean = (AdmInformeBean)informeBeans.get(0);
					String idInformes = informeBean.getIdPlantilla()+","+informeBean.getIdInstitucion()+"";
					miForm.setIdInforme(idInformes);
					return download(mapping, formulario, request, response);
				}
			}
			request.setAttribute("asunto", asunto);
			//Vamos a ver si, aunque el informe generico tenga configurado el envio, el usuario tiene permiso para ello.
			//Fecha Programada:
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			Calendar cal = Calendar.getInstance();   
			String fecha;
			fecha = sdf.format(cal.getTime()).toString();
			request.setAttribute("fecha", fecha);
			
			return mapping.findForward("seleccionInformes");


		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null); 
		}
		return mapping.findForward(mapDestino);

	}
	/*protected String envioModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   

		String idInstitucion = userBean.getLocation();	
		String idSolicitud = form.getIdSolicitud();
		String idFactura = form.getIdFactura();
		String idPersona = form.getIdPersona();
		String desc = form.getDescEnvio();
		String subModo = form.getSubModo();
		String datosEnvios = form.getDatosEnvios();
		String descargar = form.getDescargar();

		String nombreyapellidos = "";
		String idEnvio=null;
		String idTipoEnvio=form.getIdTipoEnvio();
		boolean isEnvioSMS = idTipoEnvio!=null && !idTipoEnvio.equals("") && (Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_SMS || Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_BUROSMS);

		try {
			//SOLICITUD CERTIFICADO:
			// Si el envío procede de una solicitud de certificado, seteamos el tipo
			// de envío según el tipo que se eligió al solicitar el certificado.
			if (subModo!=null && subModo.equalsIgnoreCase("SolicitudCertificado")){
				CerSolicitudCertificadosAdm solCerAdm = new CerSolicitudCertificadosAdm(this.getUserBean(request));
				Hashtable htPk = new Hashtable();
				htPk.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,idInstitucion);
				htPk.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
				CerSolicitudCertificadosBean solCerBean = (CerSolicitudCertificadosBean) solCerAdm.selectByPK(htPk).firstElement();
				idTipoEnvio = String.valueOf(solCerBean.getIdTipoEnvios());
				//form.setComboTipoEnvio(idInstitucion+","+idTipoEnvio);
				ArrayList comboTipoEnvio = new ArrayList();
				comboTipoEnvio.add(idInstitucion+","+idTipoEnvio);
				request.setAttribute("comboTipoEnvio",comboTipoEnvio);

				PysProductosInstitucionAdm prodAdm = new PysProductosInstitucionAdm(this.getUserBean(request));
				Integer idTipoProducto = solCerBean.getPpn_IdTipoProducto();
				Long idProducto = solCerBean.getPpn_IdProducto();
				Long idProductoInstitucion = solCerBean.getPpn_IdProductoInstitucion();
				Hashtable htPk2 = new Hashtable();
				htPk2.put(PysProductosInstitucionBean.C_IDINSTITUCION,idInstitucion);
				htPk2.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO,idTipoProducto);
				htPk2.put(PysProductosInstitucionBean.C_IDPRODUCTO,idProducto);
				htPk2.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,idProductoInstitucion);
				PysProductosInstitucionBean prodBean = (PysProductosInstitucionBean)prodAdm.selectByPK(htPk2).firstElement();
				String tipoCert = prodBean.getTipoCertificado();
				if (tipoCert.equalsIgnoreCase(PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION)||
						tipoCert.equalsIgnoreCase(PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA)){
					request.setAttribute("ComDil","true");
				}
				//SOLICITUD INCORPORACION:
			} else if (subModo!=null && subModo.equalsIgnoreCase("solicitudIncorporacion")){
				CenSolicitudIncorporacionAdm admSolic = new CenSolicitudIncorporacionAdm(this.getUserBean(request));
				Hashtable hashSolic = new Hashtable();
				hashSolic.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD,idSolicitud);
				Vector aux = admSolic.selectByPK(hashSolic);
				CenSolicitudIncorporacionBean beanSolic = null;
				if (aux!=null && aux.size()>0) {
					beanSolic = (CenSolicitudIncorporacionBean) aux.get(0);
					nombreyapellidos = beanSolic.getNombre()+" "+beanSolic.getApellido1()+" "+beanSolic.getApellido2(); 
				}
			} else if (subModo!=null && subModo.equalsIgnoreCase("envioFactura")){
				FacFacturaAdm admFac = new FacFacturaAdm(this.getUserBean(request));
				Hashtable hashFac = new Hashtable();
				hashFac.put(FacFacturaBean.C_IDINSTITUCION,userBean.getLocation());
				hashFac.put(FacFacturaBean.C_IDFACTURA,idFactura);
				Vector aux = admFac.selectByPK(hashFac);
				FacFacturaBean beanFac = null;
				if (aux!=null && aux.size()>0) {
					beanFac = (FacFacturaBean) aux.get(0);
					desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(),"envios.literal.facturaNumero") + " " + beanFac.getNumeroFactura();
					idPersona=beanFac.getIdPersona().toString();
				}
			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesMorosos)){
				//Hashtable htFacturasPersona  = getFacturasPersonaAComunicar(form);
				idPersona = getIdColegiadoUnico(form);

				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS ));//
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null)); 


				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "facturacion.consultamorosos.mail.asunto");

			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesDesigna)){
				MasterReport masterReport = new  MasterReport(); 
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				idPersona = getIdColegiadoUnicoDesignas(vCampos,userBean);
				//si la persona es null es que hay mas de un colegiado de las distintas designas
				//si solo hay uno comprobaremos que si hay mas de un solicitante(siempre y cuando algun informe sea
				// de tipo solicitante)
				boolean isPersonaUnica = idPersona!=null;
				//Vamos a permitir editar cuando sea solo a colegiados
				
				
				
				boolean isASolicitantes = false;
				boolean isAprocurador = false;
				if(isPersonaUnica){
					
					
					Hashtable ht = (Hashtable) vCampos.get(0); 
					String plantillas = (String)ht.get("plantillas");
					EnvioInformesGenericos informesAdm = new EnvioInformesGenericos();
					Vector vPlantillas = informesAdm.getPlantillasInforme(plantillas,  userBean);
					
					for (int j = 0; j < vPlantillas.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean)vPlantillas.get(j);
						
						String tiposDestinatario = informeBean.getDestinatarios();
						if(tiposDestinatario!=null){
							char[] tipoDestinatario = tiposDestinatario.toCharArray();
							for (int k = 0; k < tipoDestinatario.length; k++) {
								
								if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)){
									isASolicitantes = true;
									break;
								}else if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)){
									isAprocurador = true;
									break;
								}
							}

						}
						//Comprbamos que al ser a solicitantes haya una persona unica
						
						
					}
					
					
					
				}
				
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",Boolean.valueOf(isPersonaUnica&&!isASolicitantes&&!isAprocurador));
				//request.setAttribute("isEditarEnvio",Boolean.valueOf(false));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.designas.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesEjg)){
				MasterReport masterReport = new  MasterReport(); 
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				EnvioInformesGenericos informesAdm = new EnvioInformesGenericos();
				informesAdm.setPersonasEjg(vCampos,userBean);
				Hashtable destinatariosHashtable = informesAdm.getDestinatariosEjg(vCampos);
				
				

				//si la persona es null es que hay mas de un colegiado de las distintas designas
				//si solo hay uno comprobaremos que si hay mas de un solicitante(siempre y cuando algun informe sea
				// de tipo solicitante)
				boolean isDestinatarioUnico = destinatariosHashtable!=null && destinatariosHashtable.size()==1;
				//Vamos a permitir editar cuando sea solo a colegiados
				
				boolean isASolicitantes = false;
				if(isDestinatarioUnico &&!isEnvioSMS){
					
					
					Hashtable ht = (Hashtable) vCampos.get(0); 
					String plantillas = (String)ht.get("plantillas");
					
					Vector vPlantillas = informesAdm.getPlantillasInforme(plantillas,  userBean);
					
					for (int j = 0; j < vPlantillas.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean)vPlantillas.get(j);
						
						String tiposDestinatario = informeBean.getDestinatarios();
						if(tiposDestinatario!=null){
							char[] tipoDestinatario = tiposDestinatario.toCharArray();
							for (int k = 0; k < tipoDestinatario.length; k++) {
								
								if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)){
									isASolicitantes = true;
									break;
								}
							}

						}
						//Comprbamos que al ser a solicitantes haya una persona unica
						
						
					}
					
					
					
				}
				
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",Boolean.valueOf(isDestinatarioUnico&&!isEnvioSMS));
				
				if(form.getIdTipoEnvio()!=null && !form.getIdTipoEnvio().equals("")){
					ArrayList comboTipoEnvio = new ArrayList();
					comboTipoEnvio.add(idInstitucion+","+form.getIdTipoEnvio());
					request.setAttribute("comboTipoEnvio",comboTipoEnvio);
				}
				
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.ejg.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesJustificacion)){
				
				Hashtable htPersonas = new Hashtable();
				MasterReport masterReport = new  MasterReport(); 
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				Hashtable ht = (Hashtable) vCampos.get(0); 
				idPersona = (String) ht.get("idPersona");
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));//
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null)); 


				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.justificacion.asunto");

			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesCenso)){
				idPersona = getIdColegiadoUnico(form);

				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.censo.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesListadoGuardias)){
				request.setAttribute("exitenDatos","");
				try{
					idPersona = getIdColegiados(form, request);

				} catch (SIGAException e) {
					request.setAttribute("exitenDatos","messages.general.error.noExistenDatos");
				}
				datosEnvios = form.getDatosEnvios();
	    		Hashtable backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");
	    		request.setAttribute("DATABACKUP",backupHash);
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.sjcs.lista.envio.guardias");

			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesPagoColegiados)){
				idPersona = getIdPersonaUnica(form);
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.sjcs.pagos.envio.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesFacturacionesColegiados)){
				idPersona = getIdPersonaUnica(form);
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.sjcs.facturacion.envio.asunto");


			}
			else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesExpedientes)){
				idPersona = getIdColegiadoUnico(form);

				request.setAttribute("isDescargar",new Boolean(idPersona!=null&&!isEnvioSMS));
				form.setDescargar((idPersona!=null)?"1":"");
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				// RGG también se añade que, incluso siendo un aúnica persona, no sea asolicitantes
				MasterReport masterReport = new  MasterReport(); 
				EnvioInformesGenericos informesAdm = new EnvioInformesGenericos();
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				Hashtable datosInforme = null;
				if (vCampos.size()>0) {
					datosInforme = (Hashtable) vCampos.get(0);
				}
				String plantillas = (String) datosInforme.get("plantillas");
				Vector vPlantillas = informesAdm.getPlantillasInforme(plantillas,userBean);
				boolean aSolicitantes = informesAdm.esAlgunaASolicitantes(vPlantillas);

				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null && !aSolicitantes));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.expedientes.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase("pagoColegiados")){
				idPersona = getIdPersonaUnica(form);
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.sjcs.pagos.envio.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase("certificadoIRPF")){
				idPersona = getIdPersonaUnica(form);
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "gratuita.retencionesIRPF.informe.envio.asunto");


			}

			//obtener idEnvio
			EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
			//idEnvio = String.valueOf(envAdm.getNewIdEnvio(idInstitucion));


			//obtener nombreyapellidos
			CenPersonaAdm persAdm = new CenPersonaAdm(this.getUserBean(request));		
			if ( (idPersona!=null && !idPersona.equals("")) && 
					(subModo!=null && !subModo.equalsIgnoreCase("solicitudIncorporacion")
					) )
				nombreyapellidos = persAdm.obtenerNombreApellidos(idPersona);

			


			//Nombre:
			// Antes: String nombre = idEnvio + "-" + desc + "-" + nombreyapellidos;
			// Ahora INC 4403:
			String nombre = ""  + desc + " " + nombreyapellidos;
			//////////////////////////////

			if (nombre.length()>100){
				nombre = nombre.substring(0,99);
			}
			form.setNombre(nombre);

			//Fecha Programada:
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);

			if (form.getModo().equalsIgnoreCase("envioModal"))
			{
				sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			}

			else
			{
				sdf.applyPattern(ClsConstants.DATE_FORMAT_LONG_SPANISH);
			}

			Calendar cal = Calendar.getInstance();   
			String fecha;
			fecha = sdf.format(cal.getTime()).toString();
			form.setFechaProgramada(fecha);


			//request.setAttribute(EnvEnviosBean.C_IDENVIO,idEnvio);
			request.setAttribute(CerSolicitudCertificadosBean.C_IDPERSONA_DES,idPersona);
			request.setAttribute(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
			request.setAttribute(FacFacturaBean.C_IDFACTURA,idFactura);
			request.setAttribute("subModo",subModo);
			request.setAttribute("datosEnvios",datosEnvios);
			
			AdmInformeAdm informeAdm = new AdmInformeAdm(userBean);
			List<InformeForm> plantillasList = new ArrayList<InformeForm>();
			String[] plantillas = datosEnvios.split("plantillas==");
			if(plantillas.length>0){
				String aux = plantillas[1];
				String[] informesList = aux.substring(0,aux.indexOf("##")).split("@@");
				for (int i = 0; i < informesList.length; i++) {
					String[] idsPlantilla = informesList[i].split(",") ;
					String idInforme = idsPlantilla[0];
					String idInstitucionInforme = idsPlantilla[1];
					AdmInformeBean informeBean = informeAdm.obtenerInforme(idInstitucionInforme, idInforme);
					plantillasList.add(informeBean.getInforme(new InformeForm()));
					
				}
				
				
			}
			request.setAttribute("plantillasList", plantillasList);
			
//			EnvTipoEnviosAdm tipoEnviosAdm
				
			
			GenParametrosAdm param = new GenParametrosAdm(userBean);
			request.setAttribute("smsHabilitado",param.getValor(idInstitucion, "ENV", "HABILITAR_SMS_BUROSMS", "N"));
		
		
		} catch (SIGAException e) {
			throwExcp(e.getLiteral(),new String[] {},e,null);
		} catch (Exception ex) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},ex,null);
		}

		return "envioModal";
	}
 */
	
	
	
	public ActionForward download (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		InformesGenericosForm miForm = null;
		UsrBean usr = this.getUserBean(request);

		try { 

			miForm = (InformesGenericosForm) formulario;
			
			String idTipoInforme = miForm.getIdTipoInforme();
			
			
			AdmTipoInformeAdm tipoInformeAdm = new AdmTipoInformeAdm(this.getUserBean(request));
			AdmTipoInformeBean tipoInformeBean = tipoInformeAdm.obtenerTipoInforme(idTipoInforme);

			if (tipoInformeBean.getTipoFormato().equals("F")) {
				// tratamiento particular para informes FOP
				// Es necesaria la llamada particular (MASTERREPORT)

				if (idTipoInforme.equals("ABONO")) {
					mapDestino = abono(mapping, miForm, request, response);
				} else if (idTipoInforme.equals("CPAGO")) {

					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesPagoColegiados);
				} else if (idTipoInforme.equals("CFACT")) {
					//						miForm.setClavesIteracion("idPersona");
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesFacturacionesColegiados);
				} else {
					throw new ClsExceptions("ERROR: El tipo de informe seleccionado no está configurado.");
				}

			} else if (tipoInformeBean.getTipoFormato().equals("W")) {
				// tratamiento particular para informes WORD
				// Es necesaria la consulta particular y la llamada generica (PENDIENTE DE DESARROLLO)
				if (idTipoInforme.equals("OFICI")) {
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesDesigna);
				} else 	if (idTipoInforme.equals("FACJG")) {
					mapDestino = generaInfFacJG(mapping, miForm, request, response);
				} else if (idTipoInforme.equals("FJGM")) {
					mapDestino = generaInfFacJG(mapping, miForm, request, response);
				} else if (idTipoInforme.equals(InformePersonalizable.I_INFORMEFACTSJCS)) {
					mapDestino = generaInfPersonalizableFacJG(mapping, miForm, request, response);
				} else if (idTipoInforme.equals("EJGCA")) {
					mapDestino = ejgca(mapping, miForm, request, response);
				} else if (idTipoInforme.equals("EJG")){
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesEjg);
				}else if (idTipoInforme.equals("SOJ")){
					mapDestino = soj(mapping, miForm, request, response);
				} else if (idTipoInforme.equalsIgnoreCase("CAJG")) {
					mapDestino = cajg(mapping, miForm, request, response);
				} else if (idTipoInforme.equals("COBRO")) {
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesMorosos);
				}else if (idTipoInforme.equals("IRPF")) {
					mapDestino = irpf(mapping, miForm, request, response);
				}else if (idTipoInforme.equals("CENSO")) {
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesCenso);
				}else if (idTipoInforme.equals("EXP")) {
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesExpedientes);
				}else if (idTipoInforme.equals("LIGUA")) {
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesListadoGuardias);
				} else if (idTipoInforme.equals("JUSDE")) {
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesJustificacion);
				} else if (idTipoInforme.equals("DEJG")) {
					mapDestino = dejg(mapping, miForm, request, response);
				} else if (idTipoInforme.equals("ACTAC")) {
					mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesEjg);
				} else {
					throw new ClsExceptions("ERROR: El tipo de informe seleccionado no está configurado.");
				}

			} else {
				throw new ClsExceptions("ERROR: El tipo de formato no está configurado.");
			}


		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null); 
		}

		return mapping.findForward(mapDestino);

	}
	private String getDatosInformeListaGuardias(String datosInforme,UsrBean usr) throws ClsExceptions, SIGAException{
		Hashtable htPersonas = new Hashtable();
		 
		Vector guardias = new Vector();
		MasterReport masterReport = new  MasterReport(); 
		Vector datosFormularioVector = masterReport.obtenerDatosFormulario(datosInforme);
		Hashtable ht = (Hashtable) datosFormularioVector.get(0); 
		String idInstitucion = (String) ht.get("idInstitucion");
		String fechaInicio = (String) ht.get("fechaIni");
		String fechaFin = (String) ht.get("fechaFin");
		String idlista = (String) ht.get("idLista");

		ScsInclusionGuardiasEnListasAdm admIGL =new ScsInclusionGuardiasEnListasAdm(usr);
		Hashtable paramBusqueda=new Hashtable();
		paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,idInstitucion);
		paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,idlista);
		Vector listasIncluidas=admIGL.select(paramBusqueda);
		Enumeration listaResultados=listasIncluidas.elements();
		
		while(listaResultados.hasMoreElements()){
			ScsInclusionGuardiasEnListasBean fila=(ScsInclusionGuardiasEnListasBean)listaResultados.nextElement();
			Vector guardia = new Vector();
			guardia.add(fila.getIdTurno().toString());
			guardia.add(fila.getIdGuardia().toString());
			guardias.add(guardia);
			
		}
		ScsGuardiasTurnoAdm admGT = new ScsGuardiasTurnoAdm(usr);
		Vector datos = admGT.getDatosPersonasGuardias(idInstitucion,idlista,guardias,fechaInicio,fechaFin);
		if(datos.size()==0)
	    	throw new SIGAException("messages.general.error.noExistenDatos");
		
		
		String datosInformeReturn = "";
		for (int j=0; j<datos.size(); j++){		    						
			Hashtable letradoOut=(Hashtable)datos.get(j);	
			Long letrado =new Long((String)letradoOut.get("IDPERSONA")).longValue();	
			datosInformeReturn+="idPersona==" +letrado+"##"+datosInforme+"%%%";
	    	

		}		
		return datosInformeReturn;
				
 

		
	}
	




}