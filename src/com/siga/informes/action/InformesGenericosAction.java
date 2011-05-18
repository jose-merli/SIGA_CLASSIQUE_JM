package com.siga.informes.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmTipoFiltroInformeBean;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
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
import com.siga.beans.ScsInclusionGuardiasEnListasBean;
import com.siga.beans.ScsListaGuardiasAdm;
import com.siga.beans.ScsListaGuardiasBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsSOJBean;
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

/**
 * Clase para la generación de informes genéricos configurados en ADM_INFORME
 * @author RGG
 */
public class InformesGenericosAction extends MasterAction {
	
	public ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion != null && accion.equalsIgnoreCase("descargaFicheroGlobal")) {
						return super.executeInternal(mapping, formulario,
								request, response);
					} else {
						return ejecutarAccion(mapping, formulario,
								request, response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
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
	 * Método que atiende a las peticiones. 
	 * Segun el valor de los parametros idInforme e ifTipoInforme del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public ActionForward ejecutarAccion (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		InformesGenericosForm miForm = null;
		UsrBean usr = this.getUserBean(request);

		try { 

			miForm = (InformesGenericosForm) formulario;
			String idInforme = miForm.getIdInforme();
			String idTipoInforme = miForm.getIdTipoInforme();
			String idInstitucion = miForm.getIdInstitucion();
			String seleccionados = miForm.getSeleccionados();
			String aSolicitantes =  miForm.getAsolicitantes();
			String destinatarios =  miForm.getDestinatarios();
			String enviar =  miForm.getEnviar();

			String idPersonaJG = null;
			String idinstitucion = null;
			String idPK = null;
			String anio = null;
			String numero = null;

			if((aSolicitantes!=null && aSolicitantes.equalsIgnoreCase("S")) &&
					(miForm.getDatosTabla()!=null && miForm.getDatosTabla().size()>0) &&
					(idTipoInforme.equals("OFICIOLD") || idTipoInforme.equals("EJG"))){
				Vector vCampos = miForm.getDatosTablaOcultos(0);
				idPersonaJG = (String) vCampos.get(0);
				idinstitucion = (String) vCampos.get(1);
				idPK = (String) vCampos.get(2);
				anio = (String) vCampos.get(3);
				numero = (String) vCampos.get(4);
			}
			// Para la comision tenemos que usar las comunicaciones de CAJG en vez de EJG
			if(idTipoInforme.equals("EJG") && usr.isComision())
				idTipoInforme="CAJG";

			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));

			if (seleccionados!=null && seleccionados.equals("3")) {
				// mostramos la ventana con la pregunta
				Vector infs=adm.obtenerInformesTipo(idInstitucion,idTipoInforme,aSolicitantes, destinatarios);
				request.setAttribute("plantillas",infs);
				return mapping.findForward("seleccionPlantillasModal");
			}
			

			if (idTipoInforme.equals("") && idInforme.equals("")) {
				// ERROR
				throw new ClsExceptions("Error en generación de informe: No se ha indicado el tipo de informe ni el informe.");
			} else
				if (idTipoInforme.equals("") && !idInforme.equals("")) {
					idTipoInforme = adm.obtenerInforme(idInstitucion,idInforme).getIdTipoInforme();
				} else	if (!idTipoInforme.equals("") && seleccionados.equals("0")) {
					// Si existes varios informes para el mismo tipo
					Vector infs=adm.obtenerInformesTipo(idInstitucion,idTipoInforme,aSolicitantes, destinatarios);
					if (infs!=null && infs.size()>0) {
						//añadimos al final de los datos del informe a la persona seleccionada si la hubiera
						
						
						StringBuffer datosInforme = new StringBuffer(miForm.getDatosInforme());
						if(idPersonaJG!=null){
							if(idTipoInforme.equals("OFICIOLD")){
								datosInforme = new StringBuffer();




								//datosInforme.append("idPersonaJG==");
								datosInforme.append("idPersona==");
								datosInforme.append(idPersonaJG);
								datosInforme.append("##");
								datosInforme.append("idinstitucion==");
								datosInforme.append(idinstitucion);
								datosInforme.append("##");
								datosInforme.append("idturno==");
								datosInforme.append(idPK);
								datosInforme.append("##");
								datosInforme.append("anio==");
								datosInforme.append(anio);
								datosInforme.append("##");
								datosInforme.append("numero==");
								datosInforme.append(numero);
							}else if(idTipoInforme.equals("EJG") || idTipoInforme.equals("CAJG")){
								datosInforme = new StringBuffer();
								datosInforme.append("idPersonaJG==");
								datosInforme.append(idPersonaJG);
								datosInforme.append("##");
								datosInforme.append("idinstitucion==");
								datosInforme.append(idinstitucion);
								datosInforme.append("##");
								datosInforme.append("idTipoEJG==");
								datosInforme.append(idPK);
								datosInforme.append("##");
								datosInforme.append("anio==");
								datosInforme.append(anio);
								datosInforme.append("##");
								datosInforme.append("numero==");
								datosInforme.append(numero);
							}
	                        

						}
						
					
						if (idTipoInforme.equals("CPAGO")) {
                            Vector vCampos = miForm.getDatosTablaOcultos(0);
                            //idInstitucion==2040##idPago==308##idPersona==2046000001
                            if(vCampos!=null&&vCampos.size()==3){
                                String idInstitucion2 = (String) vCampos.get(0);
                                String idPago =  (String) vCampos.get(1);
                                String idPersona = (String) vCampos.get(2);
                                datosInforme = new StringBuffer();
                                datosInforme.append("idPersona==");
                                datosInforme.append(idPersona);
                                datosInforme.append("##");
                                datosInforme.append("idInstitucion==");
                                datosInforme.append(idInstitucion2);
                                datosInforme.append("##");
                                datosInforme.append("idPago==");
                                datosInforme.append(idPago);
                                
                            }
						}else if (idTipoInforme.equals("DEJG")) {
							datosInforme = new StringBuffer();
							datosInforme.append(ScsEJGBean.C_IDINSTITUCION + "==");
							datosInforme.append(miForm.getIdInstitucion());
							datosInforme.append("##");
							datosInforme.append(ScsEJGBean.C_IDTIPOEJG + "==");
							datosInforme.append(miForm.getIdTipoEJG());
							datosInforme.append("##");
							datosInforme.append(ScsEJGBean.C_ANIO + "==");
							datosInforme.append(miForm.getAnio());
							datosInforme.append("##");
							datosInforme.append(ScsEJGBean.C_NUMERO + "==");
							datosInforme.append(miForm.getNumero());
						}

						miForm.setDatosInforme(datosInforme.toString());

						if (infs.size()==1) {
							// solo hay una, no se pregunta
							idInforme = ((AdmInformeBean)infs.get(0)).getIdPlantilla();

							miForm.setIdInforme(idInforme);

						} else if (infs.size()>1) {
							idInforme = "";

							for (int i=0;i<infs.size();i++) {
								AdmInformeBean b = (AdmInformeBean)infs.get(i);
								idInforme+=b.getIdPlantilla() + "##";
							}
							idInforme = idInforme.substring(0,idInforme.lastIndexOf("##"));

							// hay que mostrar las plantillas para que las genere.
							miForm.setIdInforme(idInforme);
							miForm.setIdTipoInforme(idTipoInforme);
							miForm.setEnviar(enviar);
							//miForm.setNombreSalida(nombreSalida);
							//miForm.setDirectorio(directorio);

							return mapping.findForward("seleccionPlantillas");
						}

					} else {
						// ERROR
						throw new ClsExceptions("Error en generación de informe: No se encuentra el informe configurado.");
					}
				}

			// aqui ya tenemos el tipo de informe y los informes seleccionados
			miForm.setIdInforme(idInforme);
			miForm.setIdTipoInforme(idTipoInforme);
			//miForm.setDatosInforme(idPersonaJG);

			
			
			
			
			// Obtengo el tipo de formato
			AdmTipoInformeBean b = admT.obtenerTipoInforme(idTipoInforme);


			// SELECCION DE METODO
			if (b.getTipoFormato().equals("CR")) {
				// tratamiento genérico para Informes de Crystal
				// Solo es necesaria la llamada genérica con parámetos (CRYSTALREPORTMASTER)
				if (idTipoInforme.equals("EJEM")) {
					mapDestino = ejem(mapping, miForm, request, response);
				} else 
					if (idTipoInforme.equals("LGUAR")) {
						mapDestino = listaGuardias(mapping, miForm, request, response);
					} else {
						throw new ClsExceptions("ERROR: El tipo de informe seleccionado no está configurado.");
					}

			} else 
				if (b.getTipoFormato().equals("F")) {
					// tratamiento particular para informes FOP
					// Es necesaria la llamada particular (MASTERREPORT)

					if (idTipoInforme.equals("ABONO")) {
						mapDestino = abono(mapping, miForm, request, response);
					} else if (idTipoInforme.equals("CPAGO")) {
						mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesPagoColegiados);
					} else {
						throw new ClsExceptions("ERROR: El tipo de informe seleccionado no está configurado.");
					}

				} else 
					if (b.getTipoFormato().equals("W")) {
						// tratamiento particular para informes WORD
						// Es necesaria la consulta particular y la llamada generica (PENDIENTE DE DESARROLLO)
						if (idTipoInforme.equals("OFICI")) {
							mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesDesigna);
							//mapDestino = ofici(mapping, miForm, request, response);
						} else 

							if (idTipoInforme.equals("FACJG")) {
								mapDestino = generaInfFacJG(mapping, miForm, request, response);
							} else if (idTipoInforme.equals("FJGM")) {
								mapDestino = generaInfFacJG(mapping, miForm, request, response);
							} else if (idTipoInforme.equals(InformePersonalizable.I_INFORMEFACTSJCS)) {
								ArrayList<HashMap<String, String>> filtrosInforme = 
										obtenerDatosFormInformeFacturacionJG(miForm, request);
								InformePersonalizable inf = new InformePersonalizable();
								mapDestino = inf.generarInformes(miForm, request, 
										InformePersonalizable.I_INFORMEFACTSJCS, filtrosInforme);
								//mapDestino = generaInfFacJG(mapping, miForm, request, response);
							} else if (idTipoInforme.equals("EJGCA")) {
								mapDestino = ejgca(mapping, miForm, request, response);
							} else if (idTipoInforme.equals("EJG")){
								mapDestino = ejg(mapping, miForm, request, response);
							}else if (idTipoInforme.equals("SOJ")){
								mapDestino = soj(mapping, miForm, request, response);
							} else if (idTipoInforme.equalsIgnoreCase("CAJG")) {
								mapDestino = ejg(mapping, miForm, request, response);
							} else if (idTipoInforme.equals("COBRO")) {
								mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesMorosos);
							}else if (idTipoInforme.equals("IRPF")) {
								mapDestino = irpf(mapping, miForm, request, response);
							}else if (idTipoInforme.equals("CENSO")) {
								mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesCenso);
							}else if (idTipoInforme.equals("EXP")) {
								mapDestino = informeGenerico(mapping, miForm, request, response,EnvioInformesGenericos.comunicacionesExpedientes);
							} 
							else if (idTipoInforme.equals("DEJG")) {
								mapDestino = dejg(mapping, miForm, request, response);
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
			// MODELO DE TIPO FOP: LLAMADA A MASTER REPORT



			for (int i=0;i<plantillas.size();i++) {
				AdmInformeBean b = (AdmInformeBean) plantillas.get(i); 
				for (int j=0;j<datos.size();j++) {
					Hashtable ht = (Hashtable) datos.get(j); 
					String idAbono = (String)ht.get("idAbono");
					informesRes.add(admInf.generarAbono(request,usr.getLanguage(),usr.getLocation(),idAbono,b.getIdPlantilla()));
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
	protected String irpf (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		InformesGenericosForm miForm = (InformesGenericosForm) formulario;
		boolean isAEnviar =  miForm.getEnviar()!=null && miForm.getEnviar().equals(ClsConstants.DB_TRUE);
		InformeCertificadoIRPF informeIRPF = new InformeCertificadoIRPF();
		File ficheroSalida=null;
		try {
			ficheroSalida = informeIRPF.getInformeIRPF(formulario, this.getUserBean(request),isAEnviar);

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}

		if(isAEnviar){
			request.setAttribute("datosEnvios",informeIRPF.getDatosEnvios());
			//Para redirigeDefinicionenvios
			request.setAttribute("subModo","certificadoIRPF");

			return "definirEnvios";

		}else{

			request.setAttribute("nombreFichero", ficheroSalida.getName());
			request.setAttribute("rutaFichero", ficheroSalida.getPath());
			request.setAttribute("borrarFichero", "true");
			request.setAttribute("generacionOK","OK");
			return "descarga";
		}




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
			Vector datosconsulta=new Vector();
			ScsEJGAdm ejgAdm= new ScsEJGAdm(usr);
			String idioma=usr.getLanguageInstitucion();
			String numero="";
			String idTipoEJG="";					
			Hashtable datoscomunes=new Hashtable();
			String codigoext="";
			String idintitucionactual=""+this.getIDInstitucion(request);
			String anio="";
			Vector vSalida = null;	
			String idPersonaJG="";
			Vector infosolicitante=new Vector();
			String ididioma="";
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
							vSalida = ejgAdm.getCalificacionEjgSalida(""+this.getIDInstitucion(request),idTipoEJG,anio, numero,idioma);
							/**Como accedemos al ejg por clave primaria solo nos saldra un registro**/
							Hashtable registro = (Hashtable) vSalida.get(0);			
							/**Aniadimos el solicitante del ejg**/
							idPersonaJG = (String)registro.get("IDPERSONAJG");
							infosolicitante=ejgAdm.getSolicitanteCalificacionEjgSalida(idintitucionactual,anio,idTipoEJG,numero,idPersonaJG,idioma);
							Hashtable datoscalificacion = (Hashtable) infosolicitante.get(0);
							/**Lenguaje del Solcitante**/
							ididioma = (String)datoscalificacion.get("IDLENGUAJE");
							codigoext = (String)datoscalificacion.get("CODIGOLENGUAJE");							
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
					String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+b.getDirectorio()+ClsConstants.FILE_SEP;
					String nombrePlantilla=b.getNombreFisico()+"_"+codigoext+".doc";
					String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+b.getDirectorio();			

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
	protected String ejg (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
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
									if ((String)aux.get("idtipo")!=null){
										idTipoEJG= (String)aux.get("idtipo");
									}else{
										idTipoEJG= (String)aux.get("idTipoEJG");
									}
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
								
											String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+beanInforme.getDirectorio()+ClsConstants.FILE_SEP;
											String nombrePlantilla=beanInforme.getNombreFisico()+"_"+idiomainforme+".doc";
											String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+beanInforme.getDirectorio();
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
											
								
											String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+beanInforme.getDirectorio()+ClsConstants.FILE_SEP;
											String nombrePlantilla=beanInforme.getNombreFisico()+"_"+idiomainforme+".doc";
											String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+beanInforme.getDirectorio();
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
			idinstitucion = usr.getLocation();
			Hashtable aux = (Hashtable) this.obtenerDatosFormulario((InformesGenericosForm) formulario).get(0);
			if (aux.size()==2)
				idfacturaciones = EjecucionPLs.ejecutarFuncFacturacionesIntervalo(idinstitucion, (String) aux
						.get("idFacturacionIni"), (String) aux.get("idFacturacionFin"));
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
			plantillas = infAdm.obtenerInformesTipo(idinstitucion, miform.getIdTipoInforme(), miform.getAsolicitantes(), miform.getDestinatarios());
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
				//obteniendo rutas de plantilla y fichero a generar
				rutaPlantilla = baseRutaPlantilla + ClsConstants.FILE_SEP +
				usr.getLocation() + ClsConstants.FILE_SEP +
				infBean.getDirectorio() + ClsConstants.FILE_SEP;
				nombrePlantilla = infBean.getNombreFisico() + "_" +
				(new AdmLenguajesAdm(usr)).getLenguajeExt(idioma) + ".doc";
				rutaAlmacen = baseRutaAlmacen + ClsConstants.FILE_SEP +
				usr.getLocation() + ClsConstants.FILE_SEP +
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
	protected String ejem (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
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
					String fechaInicio = (String)ht.get("fechaDesde");
					String fechaFin = (String)ht.get("fechaHasta");
					String idInstitucion = miform.getIdInstitucion();

					String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+miform.getIdInstitucion()+ClsConstants.FILE_SEP+b.getDirectorio()+ClsConstants.FILE_SEP;
					String nombrePlantilla=b.getNombreFisico()+"_"+usr.getLanguageExt()+".rpt";
					String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+b.getDirectorio();
					File rutaPDF=new File(rutaAlm);
					rutaPDF.mkdirs();
					if(!rutaPDF.canWrite()){
						throw new SIGAException("messages.informes.generico.noPlantilla");					
					}
					rutaAlm+=ClsConstants.FILE_SEP;
					String nombrePDF = b.getNombreSalida()+"_"+fechaInicio.replaceAll("/","")+"_"+fechaFin.replaceAll("/","")+"_"+idInstitucion+".pdf";

					Hashtable param = new Hashtable();
					param.put("fecha_inicio",fechaInicio);
					param.put("fecha_fin",fechaFin);
					param.put("idinstitucion",idInstitucion);
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
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}
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

		String idInstitucion = form.getIdInstitucion();
		String idTipoEJG = form.getIdTipoEJG();
		String anio = form.getAnio();
		String numero = form.getNumero();
		MasterReport masterReport = new MasterReport();
		Vector datos = masterReport.obtenerDatosFormulario(form);
		if (datos != null && datos.size() > 0) {
			Hashtable hashtable = (Hashtable)datos.get(0);
			idInstitucion = (String)hashtable.get(ScsEJGBean.C_IDINSTITUCION);
			idTipoEJG = (String)hashtable.get(ScsEJGBean.C_IDTIPOEJG);
			anio = (String)hashtable.get(ScsEJGBean.C_ANIO);
			numero = (String)hashtable.get(ScsEJGBean.C_NUMERO);
		}

		try {

			GstDate gstDate = new GstDate();
			String hoy = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request));
			ArrayList informesRes = new ArrayList(); 

			Vector plantillas = masterReport.obtenerPlantillasFormulario(form, userBean);
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
			RowsContainer rowscontainer = scsDocumentacionEJGAdm.getDocumentacionEJG(idInstitucion, idTipoEJG, anio, numero, userBean.getLanguage());

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

					String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+userBean.getLocation()+ClsConstants.FILE_SEP+directorioInforme+ClsConstants.FILE_SEP;
					String nombrePlantilla=nombreFisicoInforme+"_"+idiomaPer+".doc";
					rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+userBean.getLocation()+ClsConstants.FILE_SEP+directorioInforme;

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




}