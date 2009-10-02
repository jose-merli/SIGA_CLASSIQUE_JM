package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
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
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.certificados.Plantilla;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.InformeJustificacionMasivaForm;
import com.siga.informes.MasterWords;


public class InformeJustificacionAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("")
							|| accion.equalsIgnoreCase("abrir")) {
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("informe")) {
						mapDestino = generaInforme(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("")) {
						mapDestino = "";
					} else {
						return super.executeInternal(mapping, formulario,
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
	
	protected String generaInforme(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions, SIGAException {
		Date inicio = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO InformesJustificacion.GeneraInforme",10);
		InformeJustificacionMasivaForm f = (InformeJustificacionMasivaForm) formulario;
		UsrBean usr = this.getUserBean(request);
		ScsDesignasLetradoAdm admDesignas = new ScsDesignasLetradoAdm(usr);
		Integer idInstitucion = this.getIDInstitucion(request);
		//Ijniciamos esta variable para ir metiendo los juzgados. Se hace para evitar el acceso masivo a BBDD
		
		
		try {
			String idioma = f.getIdioma();
			GstDate gstDate = new GstDate();
			String hoy = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request));
			if(idioma ==null ||idioma.equalsIgnoreCase("")){
				hoy = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(hoy,"dma",usr.getLanguage());
				idioma = usr.getLanguageExt();
			
			}else{
				hoy = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(hoy,"dma",idioma);
				AdmLenguajesAdm admLenguajes = new AdmLenguajesAdm(usr);
				idioma = admLenguajes.getLenguajeExt(idioma);
			
			}
			
			File ficheroSalida = null;
			Vector informesRes = new Vector(); 
			// El id del informe es:
			Vector plantillas = this.obtenerPlantillasFormulario("JUS1",idInstitucion.toString(), usr);
			// Obtiene el Array de las plantillas. En este caso solo es una por lo que sacamos el 
			// elemneto 0 del vector
			AdmInformeBean b = (AdmInformeBean) plantillas.get(0);
			
			// --- acceso a paths y nombres 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			////////////////////////////////////////////////
			// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS
			
			
			String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+b.getDirectorio()+ClsConstants.FILE_SEP;
			String nombrePlantilla=b.getNombreFisico()+"_"+idioma+".doc";
			String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+b.getDirectorio();
			
			
			File crear = new File(rutaAlm);
			if(!crear.exists())
				crear.mkdirs();
			
			//					if(b.getIdPlantilla().equals("EJGCA1")){//para los modelos con diferentes regiones
			
			
			
			
			MasterWords words=new MasterWords(rutaPl+nombrePlantilla);
			//MasterWords words=new MasterWords("C:\\justificacion_letrado_ES.doc");
			
			 
			
			//			          Carga en el doc los datos comunes del informe (NCOLEGIADO, NOMBRE)
			
			Hashtable htCabeceraInforme = null;
			
			/*Vector vRowsInforme = admDesignas.getDesignasLetrado(idInstitucion, f
					.getLetrado(), f.getFechaDesde(), f.getFechaHasta(), f
					.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),true);*/
			/*Vector vRowsInforme = admDesignas.getDatosSalidaJustificacion(idInstitucion, f
					.getLetrado(), f.getFechaDesde(), f.getFechaHasta(), f
					.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),true);*/
			/*Hashtable htPersonas = admDesignas.getPersonasSalidaJustificacion(idInstitucion, f
					.getLetrado(), f.getFechaDesde(), f.getFechaHasta(), f
					.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),true);*/
			
			Hashtable htPersonas = admDesignas.getPersonasSalidaJustificacion(idInstitucion, f
					.getLetrado(), f.getFechaDesde(), f.getFechaHasta(), f
					.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),true);
			
			
			if(htPersonas==null ||htPersonas.size()<1){
				throw new SIGAException("messages.informes.ficheroVacio");
				
			}else{

				/*CenColegiadoAdm admCol = new CenColegiadoAdm(usr);
				if(f.getLetrado()!=null && !f.getLetrado().equalsIgnoreCase("")){
					htCabeceraInforme = new Hashtable();
					String nColegiado =  f.getNumeroNifTagBusquedaPersonas();
					
					htCabeceraInforme.put("NCOLEGIADO",nColegiado);
					htCabeceraInforme.put("ETIQUETA","Nº Colegiado");
					htCabeceraInforme.put("NOMBRE",f.getNombrePersona());
					htCabeceraInforme.put("FECHA",hoy);
					Hashtable htCol = admCol.obtenerDatosColegiado(idInstitucion.toString(),f.getLetrado(),usr.getLanguage());
					String direccion = "";
					if (htCol!=null && htCol.size()>0) {
						direccion = (String)htCol.get("DIRECCION_LETR");
					}
					htCabeceraInforme.put("DIRECCION",direccion);
					String codPostal = "";
					if (htCol!=null && htCol.size()>0) {
						codPostal = (String)htCol.get("CP_LETR");
					}
					htCabeceraInforme.put("CP",codPostal);
					String localidad = "";
					if (htCol!=null && htCol.size()>0) {
						localidad = (String)htCol.get("POBLACION_LETR");
					}
					
					htCabeceraInforme.put("PROVINCIA",localidad);
					htCabeceraInforme.put("CRONOLOGICO",nColegiado);



					//vRowsInforme = formatearVectorInforme(vRowsInforme,usr,idInstitucion,htAcumuladorJuzgados);
					Document doc=words.nuevoDocumento();
					doc = words.sustituyeDocumento(doc,htCabeceraInforme);
					doc = words.sustituyeRegionDocumento(doc,"region",vRowsInforme);


					StringBuffer nombreArchivo = new StringBuffer(); 
					nombreArchivo.append(nColegiado);
					nombreArchivo.append("-");
					nombreArchivo.append(b.getNombreSalida());
					nombreArchivo.append("_");
					nombreArchivo.append(idInstitucion);
					nombreArchivo.append(".doc");
					File archivo = words.grabaDocumento(doc,rutaAlm,nombreArchivo.toString());
					informesRes.add(archivo);

				}else{*/
					//Hashtable htPersonas = getHashPersonaInforme(vRowsInforme,usr,idInstitucion);
					Iterator itePersonas = htPersonas.keySet().iterator();
					int j = 0;
					while (itePersonas.hasNext()) {
						String keyPersona = (String) itePersonas.next();
						TreeMap tmRowsInformePorPersona = (TreeMap) htPersonas.get(keyPersona);
						Vector vRowsInformePorPersona = new Vector(tmRowsInformePorPersona.values());
						//Vector vRowsInformePorPersona = (Vector) htPersonas.get(keyPersona);
						
						//En toda las filas tenemos la descripcion del colegiado asi que cogemos la
						//primera para sacar la cabecera
						Hashtable htRow = (Hashtable)vRowsInformePorPersona.get(0);
						htCabeceraInforme = new Hashtable();
						String nColegiado =  (String)htRow.get(CenColegiadoBean.C_NCOLEGIADO);
						htCabeceraInforme.put("NCOLEGIADO",nColegiado);
						htCabeceraInforme.put("ETIQUETA","Nº Colegiado");
						htCabeceraInforme.put("NOMBRE",(String)htRow.get(CenPersonaBean.C_NOMBRE));
						htCabeceraInforme.put("FECHA",hoy);
			
						String direccion = "";
						String codPostal = "";
						String pobLetrado = "";
						
						if (htRow!=null && htRow.size()>0) {
							codPostal = (String)htRow.get("CP_LETRADO");
							pobLetrado = (String)htRow.get("POBLACION_LETRADO");
							direccion = (String)htRow.get("DOMICILIO_LETRADO");
							
						}
												
						htCabeceraInforme.put("DIRECCION",direccion);
						htCabeceraInforme.put("CP",codPostal);
						htCabeceraInforme.put("PROVINCIA",pobLetrado);
						
						htCabeceraInforme.put("CRONOLOGICO",nColegiado);

						Document doc=words.nuevoDocumento();
						doc = words.sustituyeDocumento(doc,htCabeceraInforme);
						doc = words.sustituyeRegionDocumento(doc,"region",vRowsInformePorPersona);

						StringBuffer nombreArchivo = new StringBuffer(); 
						nombreArchivo.append(nColegiado);
						nombreArchivo.append("-");
						
						nombreArchivo.append(b.getNombreSalida());
						nombreArchivo.append("_");
						nombreArchivo.append(idInstitucion);
						nombreArchivo.append("_");
						nombreArchivo.append(j);
						nombreArchivo.append(".doc");
						j++;
						File archivo = words.grabaDocumento(doc,rutaAlm,nombreArchivo.toString());
						informesRes.add(archivo);


					}

				


	       


				/////////////////////////////////////////////////
				if (informesRes.size()!=0) { 
					if (informesRes.size()<2) {
						ficheroSalida = (File) informesRes.get(0);
					} else {
						AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));
						AdmTipoInformeBean beanT = admT.obtenerTipoInforme("JUSDE");
						ArrayList ficheros= new ArrayList();
						for (int i=0;i<informesRes.size();i++) {
							File file = (File) informesRes.get(i);
							ficheros.add(file);
						}
						String nombreFicheroZIP=beanT.getDescripcion().trim() + "_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
						String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
						rutaServidorDescargasZip += ClsConstants.FILE_SEP+idInstitucion+ClsConstants.FILE_SEP+"temp"+ File.separator;
						File ruta = new File(rutaServidorDescargasZip);
						ruta.mkdirs();
						Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficheros);
						ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
					}
					request.setAttribute("nombreFichero", ficheroSalida.getName());
					request.setAttribute("rutaFichero", ficheroSalida.getPath());
					request.setAttribute("borrarFichero", "true");
				}
				else
					throw new SIGAException("messages.informes.ficheroVacio");
			}
		}catch (ClsExceptions e) {
			throwExcp(e.getMessage(),new String[] {"modulo.informes"},e,null);
		}catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.informes"},e,null);
		}
		
		Date fin = new Date(); 

		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN InformesJustificacion.GeneraInforme",10);
		ClsLogging.writeFileLog(fin.getTime()-inicio.getTime() + " MILISEGUNDOS ,==> SIGA: TIEMPO TOTAL",10);

		request.setAttribute("generacionOK","OK");
		return "descarga";
	}
	
	
	
	
	/**
	 * formatearVectorInforme : 
	 * 
	 * @param vRowsInforme
	 * @param usr
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	/*private Vector formatearVectorInforme(Vector vRowsInforme,UsrBean usr,Integer idInstitucion,Hashtable htAcumuladorJuzgados)throws ClsExceptions{
		ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(usr);
		for (int i = 0; i < vRowsInforme.size(); i++) {
			Hashtable htRows = (Hashtable)vRowsInforme.get(i);
			formatearHashInforme(htRows, usr, idInstitucion,htAcumuladorJuzgados);
			
			
		}
		return vRowsInforme;
		
	}*/
	/**
	 * Este metodo nos sirve para formatear los datos que vienes de la select 
	 * a la forma que los coge el informe.
	 * Meter X si el estado es  finalizado
	 * Recoger la descripcion de los juzgados
	 * 
	 * @param htRows
	 * @param usr
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	
	
	/**
	 * Creamos un acumulado de juzgados para evitar conexiones innecesarias a base de datos
	 * 
	 * @param idInstitucion
	 * @param idJuzgado
	 * @param acumuladorJuzgados
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 */
	/*private Hashtable getJuzgado(String idInstitucion, String idJuzgado, Hashtable acumuladorJuzgados,UsrBean usr)throws ClsExceptions{
		Hashtable htJuzgado = null;
		
		String keyAcumulador = idInstitucion+"||"+idJuzgado;
		if(acumuladorJuzgados.containsKey(keyAcumulador))
			htJuzgado = (Hashtable) acumuladorJuzgados.get(keyAcumulador);
		else{
			ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(usr);
			htJuzgado = admJuzgados.obtenerDatosJuzgado(idInstitucion,idJuzgado);
			acumuladorJuzgados.put(keyAcumulador,htJuzgado);
		}
			
		
			return htJuzgado;
		 
			
		
	}*/
	
	
	private Vector obtenerPlantillasFormulario(String idInforme, String idInstitucion, UsrBean usr) throws ClsExceptions {
	    Vector salida = new Vector ();
	    try {
		    AdmInformeAdm adm = new AdmInformeAdm(usr);
		    salida.add(adm.obtenerInforme(idInstitucion,idInforme));
		    
	    } catch (ClsExceptions e) {
	        throw e;
	    } catch (Exception e) {
	        throw new ClsExceptions(e,"Error al obtener los objetos plantillas del formulario.");
	    }
	    return salida;
	}
	

}