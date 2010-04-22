package com.siga.envios;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.EnvDestProgramInformesAdm;
import com.siga.beans.EnvDestProgramInformesBean;
import com.siga.beans.EnvEnvioProgramadoAdm;
import com.siga.beans.EnvEnvioProgramadoBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvInformesGenericosAdm;
import com.siga.beans.EnvInformesGenericosBean;
import com.siga.beans.EnvProgramIRPFAdm;
import com.siga.beans.EnvProgramIRPFBean;
import com.siga.beans.EnvProgramInformesAdm;
import com.siga.beans.EnvProgramInformesBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.beans.EnvValorCampoClaveAdm;
import com.siga.beans.EnvValorCampoClaveBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.HelperInformesAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.certificados.Plantilla;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeCertificadoIRPF;
import com.siga.informes.InformeColegiadosPagos;
import com.siga.informes.MasterReport;
import com.siga.informes.MasterWords;
import com.siga.informes.form.InformesGenericosForm;


/**
 * 
 * Se crea esta clase para gestionar las comunicaciones masivas
 * Esta intimamente relacionada con DefinirEnviosAction e InformesGenercosAction. 
 * 
 * 
 * @author jorgeta
 *
 */
public class EnvioInformesGenericos extends MasterReport {
	
	
	private static Boolean alguienEjecutando=Boolean.FALSE;
	private static Boolean algunaEjecucionDenegada=Boolean.FALSE;
	
	
	public static final int docFile = 1;
	public static final int docDocument = 2;
	public static final String comunicacionesCenso = "CENSO";
	public static final String comunicacionesMorosos = "COBRO";
	public static final String comunicacionesDesigna = "OFICI";
	public static final String comunicacionesExpedientes = "EXP";
	public static final String comunicacionesPagoColegiados = "CPAGO";
	public static final int tipoPlantillaWord= 1;
	public static final int tipoPlantillaFo = 2;
	
	private String datosEnvios;
	boolean envioBatch = false;

	public String getDatosEnvios() {
		return datosEnvios;
	}

	private void setDatosEnvios(String datosEnvios) {
		this.datosEnvios = datosEnvios;
	}
	public boolean isEnvioBatch() {
		return envioBatch;
	}

	public void setEnvioBatch(boolean envioBatch) {
		this.envioBatch = envioBatch;
	}

	/**
	 * En este metodo vamos ha invocar el acceso a datos de cada uno de los informes que
	 * se van a generar. El hashtabel que devuleve tendra dos elemento (row y region).
	 * region( masterWord.sustituyeRegionDocumento)-->El elemento es un vector de hastable cuyas 
	 * 		claves seran los nombres de la region.
	 * row (masterWord.sustituyeRegionDocumento) --> Sera el vector de datos
	 * 
	 * @param datosInforme
	 * @param usrBean
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private Hashtable getDatosInformeFinal(Hashtable datosInforme,
			UsrBean usrBean) throws ClsExceptions, SIGAException {

		Hashtable htDatosInforme = new Hashtable();

		//Velores comunes para la obttencion de los datos del informe
		String idTipoInforme = (String) datosInforme.get("idTipoInforme");
		
		String idioma = (String) datosInforme.get("idioma");

		if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesCenso)) {

			String idTipoPersonas = (String) datosInforme.get("idTipoPersonas");
			String idPersona = (String) datosInforme.get("idPersona");
			String idInstitucion = (String) datosInforme.get("idInstitucion");

			Vector vDatosInformeFinal = null;
			CenColegiadoAdm colegiadoAdm = null;
			switch (Integer.parseInt(idTipoPersonas)) {
			//Caso de No Colegiados
			case 0:
				CenNoColegiadoAdm noColegiadoAdm = new CenNoColegiadoAdm(
						usrBean);
				vDatosInformeFinal = noColegiadoAdm.getInformeNoColegiado(
						idInstitucion, idPersona, idioma, true);

				break;
				//Caso de colegiados
			case 1:

				colegiadoAdm = new CenColegiadoAdm(usrBean);
				vDatosInformeFinal = colegiadoAdm.getInformeColegiado(
						idInstitucion, idPersona, idioma, true);
				break;
				//Caso de letrados
			case 2:

				colegiadoAdm = new CenColegiadoAdm(usrBean);
				vDatosInformeFinal = colegiadoAdm.getInformeLetrado(
						idInstitucion, idPersona, idioma, true);
				break;

			}

			htDatosInforme.put("row", vDatosInformeFinal);

		}else if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesPagoColegiados)) {
			InformeColegiadosPagos infColegiado = new InformeColegiadosPagos();
			datosInforme  = infColegiado.getDatosInformeColegiado(usrBean,datosInforme);
			htDatosInforme.put("row", datosInforme);

		}
		
		else if (idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesMorosos)) {
			String idPersona = (String) datosInforme.get("idPersona");
			String idInstitucion = (String) datosInforme.get("idInstitucion");
			String idFactura = (String) datosInforme.get("idFactura");
			// Como sabemos la clave por la que puede iterar, si existe estara en plural
			Object oFacturasPersona = datosInforme.get("idFacturas");
			ArrayList alFacturasPersona = null;
			if(oFacturasPersona instanceof  ArrayList){
				alFacturasPersona = (ArrayList)oFacturasPersona; 
			}else if(oFacturasPersona instanceof  String){
				//si viene por el proceso automatico
				alFacturasPersona = new ArrayList(getDatosSeparados((String)oFacturasPersona,"@@"));


			}
			if (alFacturasPersona == null) {
				alFacturasPersona = new ArrayList();
				alFacturasPersona.add(idFactura);

			}
			//Esto lo añadimos al hash para que luego se inserte en la tabla env_comunicacion_morosos
			datosInforme.put("idFacturas", alFacturasPersona);



			Date hoy = new Date();
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
			String sHoy = sdf2.format(hoy);

			FacFacturaAdm facturaAdm = new FacFacturaAdm(usrBean);
			CenColegiadoAdm admCol = new CenColegiadoAdm(usrBean);

			Vector vDatosInforme = facturaAdm.selectFacturasMoroso(
					idInstitucion, idPersona, null, null, alFacturasPersona,
					null, false, true,usrBean.getLanguage());

			Hashtable htCabeceraInforme = new Hashtable();

			Hashtable htCol = admCol.obtenerDatosColegiado(idInstitucion
					.toString(), idPersona, usrBean.getLanguage());
			String direccion = "";
			if (htCol != null && htCol.size() > 0) {
				direccion = (String) htCol.get("DIRECCION_LETR");
			}
			htCabeceraInforme.put("DIRECCION", direccion);
			String codPostal = "";
			if (htCol != null && htCol.size() > 0) {
				codPostal = (String) htCol.get("CP_LETR");
			}
			htCabeceraInforme.put("CP", codPostal);
			String localidad = "";
			if (htCol != null && htCol.size() > 0) {
				localidad = (String) htCol.get("POBLACION_LETR");
			}
			htCabeceraInforme.put("CIUDAD", localidad);
			String provincia = "";
			if (htCol != null && htCol.size() > 0) {
				provincia = (String) htCol.get("PROVINCIA_LETR");
			}
			htCabeceraInforme.put("PROVINCIA", provincia);

			String nombre = "";
			if (htCol != null && htCol.size() > 0) {
				nombre = (String) htCol.get("NOMBRE_LETRADO");
			}
			htCabeceraInforme.put("NOMBRE", nombre);
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			htCabeceraInforme.put("FECHA", sHoy);
			Hashtable htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), (String)sHoy);
			htFuncion.put(new Integer(2), "m");
			htFuncion.put(new Integer(3), usrBean.getLanguage());
			helperInformes.completarHashSalida(htCabeceraInforme,helperInformes.ejecutaFuncionSalida(
					htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAMESLETRA"));

			

			double importeTotal = 0;
			double deudaTotal = 0;
			
			for (int j = 0; j < vDatosInforme.size(); j++) {
				Hashtable fila = (Hashtable) vDatosInforme.get(j);
				double importe = Double.parseDouble((String) fila.get("TOTAL"));
				importeTotal += importe;
				double deuda = Double.parseDouble((String) fila.get("DEUDA"));
				deudaTotal += deuda;
				
				//INC_06198_SIGA
				//se separan las comunicaciones desde la 1 hasta la 5, si existen
				//estas etiquetas podran usarse en cualquier parte de la plantilla
				String sComunicaciones = (String)fila.get("COMUNICACIONES");
				String[] lComunicaciones = sComunicaciones.split("\n\r");
				String sComunicacionesMesLetra = (String)fila.get("COMUNICACIONESMESLETRA");
				String[] lComunicacionesMesLetra = sComunicacionesMesLetra.split("\n\r");
				int k;
				int p;
				for(k = 0; k < lComunicaciones.length && k < 5; k++){
					fila.put("FECHA_"+(k+1)+"COMUNICACION", lComunicaciones[k]);
					htCabeceraInforme.put("FECHA_"+(k+1)+"COMUNICACION", lComunicaciones[k]);
					
					fila.put("FECHA_"+(k+1)+"COMUNICACIONMESLETRA", lComunicacionesMesLetra[k]);
					htCabeceraInforme.put("FECHA_"+(k+1)+"COMUNICACIONMESLETRA", lComunicacionesMesLetra[k]);
				}
				
				for(; k < 5; k++){
					fila.put("FECHA_"+(k+1)+"COMUNICACION", "");
					htCabeceraInforme.put("FECHA_"+(k+1)+"COMUNICACION", "");
					fila.put("FECHA_"+(k+1)+"COMUNICACIONMESLETRA", "");
					htCabeceraInforme.put("FECHA_"+(k+1)+"COMUNICACIONMESLETRA", "");
					
				}
			

				

			}
			
			
			
			htCabeceraInforme.put("BRUTO", UtilidadesNumero
					.formatoCampo(UtilidadesNumero.redondea(importeTotal, 2)));
			htCabeceraInforme.put("LIQUIDO", UtilidadesNumero
					.formatoCampo(UtilidadesNumero.redondea(deudaTotal, 2)));

			htDatosInforme.put("row", htCabeceraInforme);
			htDatosInforme.put("region", vDatosInforme);


		}else if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesDesigna)) {
			ScsDesignaAdm scsDesignaAdm = new ScsDesignaAdm(usrBean);
			String idinstitucion = (String) datosInforme.get("idInstitucion");
			String anio = (String) datosInforme.get("anio");
			String idTurno=(String) datosInforme.get("idTurno");
			String numero = (String) datosInforme.get("numero");
			String aSolicitantes = (String) datosInforme.get("aSolicitantes");
			String idPersonaJG = (String) datosInforme.get("idPersonaJG");

			boolean isSolicitantes = aSolicitantes!=null && aSolicitantes.equalsIgnoreCase("S");

			Vector datosconsulta= scsDesignaAdm.getDatosSalidaOficio(idinstitucion,idTurno,anio,numero,null,isSolicitantes,idPersonaJG);
			htDatosInforme.put("row", datosconsulta);


		}else if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesExpedientes)) {
			
			String idInstitucion = (String)datosInforme.get("idInstitucion");
			String idInstitucionTipoExp = (String)datosInforme.get("idInstitucionTipoExp");
    		String idTipoExp = (String)datosInforme.get("idTipoExp");
    		boolean isSolicitantes = this.esAlgunaASolicitantes(idInstitucion, (String)datosInforme.get("plantillas"));
			//String aSolicitantes = (String) datosInforme.get("aSolicitantes");
    		//boolean isSolicitantes = aSolicitantes!=null && aSolicitantes.equalsIgnoreCase("S");
			
			String anio = (String)datosInforme.get("anioExpediente");
			
			String numero = (String)datosInforme.get("numeroExpediente");
			
			String idPersona = (String)datosInforme.get("idPersona");
			
			
			ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm (usrBean);
			Vector vDatosInformeFinal = expedienteAdm.getDatosInformeExpediente(idInstitucion, idInstitucionTipoExp, 
					idTipoExp, anio, numero, idPersona, true, isSolicitantes);

			htDatosInforme.put("row", vDatosInformeFinal);
		}
		return htDatosInforme;

	}
	/**
	 * Devuelve un Vector con los documentos a Enviar(o descargar) ya generados en formato File 
	 * o Document dependiendo del parametro de entrada tipodocumento. Generara un documento por cada
	 * una de las plantillas del parametro vPlantillas(AdmInformeBean)
	 *
	 * 
	 * @param datosInforme
	 * @param vPlantillas Informes a generar
	 * @param usrBean
	 * @param tipoDocumento
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDocumentosAEnviar(Hashtable datosInforme,
			Vector vPlantillas, UsrBean usrBean, int tipoDocumento,String tipoComunicacion)
	throws ClsExceptions, SIGAException {

		Vector vDocumentos = new Vector();
		String idiomaExt = (String) datosInforme.get("idiomaExt");
		if (idiomaExt == null || idiomaExt.equals(""))
			idiomaExt = usrBean.getLanguageExt();
		datosInforme.put("idiomaExt", idiomaExt);

		String idioma = (String) datosInforme.get("idioma");
		if (idioma == null || idioma.equals(""))
			idioma = usrBean.getLanguage();
		datosInforme.put("idioma", idioma);
		String idPersona = (String) datosInforme.get("idPersona");
		String idInstitucion = (String) datosInforme.get("idInstitucion");
		StringBuffer identificador = new StringBuffer();
		identificador.append(idInstitucion);
		identificador.append("_");
		identificador.append(idPersona);
		Hashtable htDatosInformeFinal = null;

		if(!tipoComunicacion.equals(EnvioInformesGenericos.comunicacionesDesigna) && !tipoComunicacion.equals(EnvioInformesGenericos.comunicacionesExpedientes))
			htDatosInformeFinal = getDatosInformeFinal(datosInforme,
					usrBean);
		
		Hashtable hashConsultasHechas = new Hashtable();
		for (int i = 0; i < vPlantillas.size(); i++) {
			AdmInformeBean beanInforme = (AdmInformeBean) vPlantillas.get(i);

			if(tipoComunicacion.equals(EnvioInformesGenericos.comunicacionesDesigna)){
				idInstitucion = (String) datosInforme.get("idInstitucion");
				String anio = (String) datosInforme.get("anio");
				String idTurno=(String) datosInforme.get("idTurno");
				String numero = (String) datosInforme.get("numero");
				boolean isSolicitantes = beanInforme.getASolicitantes()!=null && beanInforme.getASolicitantes().equalsIgnoreCase("S");
				String keyConsultasHechas = idInstitucion+anio+idTurno+numero+isSolicitantes;
				datosInforme.put("aSolicitantes", beanInforme.getASolicitantes());

				if(hashConsultasHechas.containsKey(keyConsultasHechas)){
					htDatosInformeFinal = (Hashtable) hashConsultasHechas.get(keyConsultasHechas);

				}else{
					//datosconsulta =	clase.getDatosPlantillas(idinstitucion,anio,idturno,numero,
					//usr.getLanguage(),isSolicitantes,idPersonaJG, codigo);

					htDatosInformeFinal = getDatosInformeFinal(datosInforme,
							usrBean);
					hashConsultasHechas.put(keyConsultasHechas, htDatosInformeFinal);
				}
				
				Vector datosInformeDesigna = (Vector)htDatosInformeFinal.get("row");
				for (int k = 0; k < datosInformeDesigna.size(); k++) {
					Hashtable datosInformeK = (Hashtable)datosInformeDesigna.get(k);
					Hashtable htDatosInforme = new Hashtable();
					htDatosInforme.put("row", datosInformeK);
					String codigo = (String) datosInformeK.get("CODIGO");
					String ncolegiado ="" ;
					if (datosInformeK.get("NCOLEGIADO_LETRADO")!=null){
						ncolegiado=(String)UtilidadesHash.getString(datosInformeK,"NCOLEGIADO_LETRADO");
					}else{
						if(datosInformeK.get("")!=null)
							ncolegiado=((String)datosInformeK.get("")).split(" - ")[0];


					}
					identificador= new StringBuffer();
					identificador.append(ncolegiado);
					identificador.append("-");
					identificador.append(k);
					identificador.append("_");
					identificador.append(codigo);
					identificador.append("_");
					identificador.append(anio);
					identificador.append("_");
					identificador.append(idTurno);
					identificador.append("_");
					identificador.append(anio);
					identificador.append("_");
					identificador.append(idInstitucion);
					identificador.append("_");
					identificador.append(new Integer(i).toString());
					identificador.append("_");
					String hoy = UtilidadesString.formatoFecha(new Date(),"yyyyMMddhhmmssSSS");
					identificador.append(hoy);
					

					
					
					File fileDocumento = getInformeGenerico(beanInforme,
							htDatosInforme, idiomaExt, identificador.toString(), usrBean,tipoPlantillaWord);
					String pathDocumento = fileDocumento.getPath();
					// Creacion documentos
					int indice = pathDocumento.lastIndexOf(ClsConstants.FILE_SEP);
					String descDocumento = "";
					if (indice > 0)
						descDocumento = pathDocumento.substring(indice + 1);


					switch (tipoDocumento) {
					case 1:
						vDocumentos.add(fileDocumento);
						break;
					case 2:
						Documento documento = new Documento(pathDocumento,
								descDocumento);
						vDocumentos.add(documento);
						break;

					default:
						break;
					}


				}



			}else{
					if(tipoComunicacion.equals(EnvioInformesGenericos.comunicacionesExpedientes)){
							
						//Vector datosInformeExpediente = (Vector)htDatosInformeFinal.get("row");
						//for (int k = 0; k < datosInformeExpediente.size(); k++) {
							//Hashtable datosInformeK = (Hashtable)datosInformeExpediente.get(k);
							Hashtable datosInformeK =  datosInforme;
							Hashtable htDatosInforme = new Hashtable();
							htDatosInforme.put("row", datosInformeK);
													
		
							String anio = (String) datosInforme.get("anioExpediente");
							String numero = (String) datosInforme.get("numeroExpediente");
							String idInstitucionTipoExp = (String) datosInforme.get("idInstitucionTipoExp");
							String idTipoExp = (String) datosInforme.get("idTipoExp");
							String idPersonaK = (String) datosInformeK.get("IDPERSONA_DEST");
							identificador = new StringBuffer();
							identificador.append(idInstitucion);
							identificador.append("_");
							identificador.append(anio);
							identificador.append("_");
							identificador.append(numero);
							identificador.append("_");
							identificador.append(idTipoExp);
							identificador.append("_");
							identificador.append(idInstitucionTipoExp);
							identificador.append("_");
							identificador.append(idPersonaK);
							identificador.append("_");
							identificador.append(new Integer(i).toString());
							identificador.append("_");
							String hoy = UtilidadesString.formatoFecha(new Date(),"yyyyMMddhhmmssSSS");
							identificador.append(hoy);
							htDatosInformeFinal = htDatosInforme;
					}
					File fileDocumento = null;
					//Para las comunicacion con plantilla Fo hay que tiene clase propia de generacion
					//Por lo que ¿Deberiamos haberlo metido en genericos?
					if(tipoComunicacion.equals(EnvioInformesGenericos.comunicacionesPagoColegiados)){
						String nColegiado = (String) datosInforme.get("NCOLEGIADO");
//						String idPago = (String) datosInforme.get("IDPAGOS");
						String idTipoInforme = (String) datosInforme.get("idTipoInforme");
						identificador = new StringBuffer();
						identificador.append(nColegiado);
						identificador.append("_");
						identificador.append(idInstitucion);
						identificador.append("_");
						identificador.append(idPersona);
						identificador.append("_");
						String hoy = UtilidadesString.formatoFecha(new Date(),"yyyyMMddhhmmssSSS");
						identificador.append(hoy);
						fileDocumento = getInformeGenerico(beanInforme,
								htDatosInformeFinal, idiomaExt, identificador.toString(), usrBean,tipoPlantillaFo);
					}else{
						if(tipoComunicacion.equals(EnvioInformesGenericos.comunicacionesMorosos)){
							if(htDatosInformeFinal.get("region")!=null){
								Vector v = (Vector)htDatosInformeFinal.get("region"); 
								if(v.get(0)!=null){
									Hashtable htV = (Hashtable)v.get(0);
									String nColegiado = (String)htV.get("NCOLEGIADO");
									identificador = new StringBuffer();
									if(nColegiado!=null){
										identificador.append(nColegiado);
										identificador.append("_");
									}
								}
							}
							identificador.append(idInstitucion);
							identificador.append("_");
							identificador.append(idPersona);
							identificador.append("_");
							String hoy = UtilidadesString.formatoFecha(new Date(),"yyyyMMddhhmmssSSS");
							identificador.append(hoy);
						}
						fileDocumento = getInformeGenerico(beanInforme,
								htDatosInformeFinal, idiomaExt, identificador.toString(), usrBean,tipoPlantillaWord);
					}
					
					
					String pathDocumento = fileDocumento.getPath();
					// Creacion documentos
					int indice = pathDocumento.lastIndexOf(ClsConstants.FILE_SEP);
					String descDocumento = "";
					if (indice > 0)
						descDocumento = pathDocumento.substring(indice + 1);


					switch (tipoDocumento) {
					case 1:
						vDocumentos.add(fileDocumento);
						break;
					case 2:
						Documento documento = new Documento(pathDocumento,
								descDocumento);
						vDocumentos.add(documento);
						break;

					default:
						break;
					}


				//}
			}
		}	
		return vDocumentos;

	}
	/**
	 * Este metodo es el encargado de generar el envio ordinario de correos.
	 * Por definicion de requisitos se generara UN UNICO ENVIO(EnvEnvios) cuyos destinatarios 
	 * sera cada uno de los destinatarios programados y los documentos seran la suma de todos los 
	 * docuemntos de cada uno de los destinatarios
	 * 
	 * @param usrBean
	 * @param vDestProgramInfBean Destinatarios del envio
	 * @param programInfBean Programacion de informes genericos
	 * @param vPlantillasInforme  Informes a generar por destinatario
	 * @param envioProgramadoBean Maestro de programacion de envios
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	
	public void enviarInformeGenericoOrdinario(UsrBean usrBean,
			Vector vDestProgramInfBean, EnvProgramInformesBean programInfBean,
			Vector vPlantillasInforme,
			EnvEnvioProgramadoBean envioProgramadoBean) throws ClsExceptions,
			SIGAException {

		Envio envio = new Envio(usrBean, envioProgramadoBean.getNombre());
		if(envio.getEnviosBean()!=null)			
			envio.getEnviosBean().setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
		
		// Bean envio
		EnvEnviosBean enviosBean = envio.getEnviosBean();
		enviosBean.setDescripcion(enviosBean.getIdEnvio() + " "
				+ enviosBean.getDescripcion());
		// Preferencia del tipo de envio si el usuario tiene uno:
		enviosBean.setIdTipoEnvios(envioProgramadoBean.getIdTipoEnvios());

		enviosBean.setIdPlantillaEnvios(envioProgramadoBean
				.getIdPlantillaEnvios());
		if (envioProgramadoBean.getIdPlantilla() != null
				&& !envioProgramadoBean.getIdPlantilla().equals("")) {
			enviosBean.setIdPlantilla(envioProgramadoBean.getIdPlantilla());
		} else {
			enviosBean.setIdPlantilla(null);
		}
		List lComunicacionesMorosos = new ArrayList();
		Hashtable htPersonas = new Hashtable();
		for (int j = 0; j < vDestProgramInfBean.size(); j++) {
			EnvDestProgramInformesBean destProgramInfBean = (EnvDestProgramInformesBean) vDestProgramInfBean
			.get(j);
			Hashtable datosInforme = new Hashtable();

			//(JTA) IMPORTANTE: ES MUY FACIL PENSAR QUE ESTE CAMPO DE CLAVES SE VAYA A LLENAR EN
			//ALGUN ENVIO MASIVO. SI SE QUIERE EVITAR ESTE COLAPSO SE PODRIA CREAR UNA TABLA DE 
			//CLAVES AL ESTILO ENV_INFORMESGENERICOS PARA CADA UNO DE LOS CAMPOS DE CLAVES.
			//SERVIRIA TANTO PARA LAS CLAVES DE LA PROGRAMACION COMO LA DE LOS DESTINATARIOS
			//CON TIEMPO SE DEBERIA HACER PARA EVITAR COMPLICACIONES FUTURAS
			Hashtable htClavesProgramacion = getClaves(programInfBean.getClaves());

			//Hashtable htClavesDestinatario = getClaves(destProgramInfBean.getClaves());
			ArrayList alClavesDestinatario = destProgramInfBean.getClavesDestinatario();


			String idPersona = destProgramInfBean.getIdPersona().toString();
			datosInforme.put("idioma", programInfBean.getIdioma().toString());
			datosInforme.put("idiomaExt", programInfBean.getIdiomaCodigoExt());
			datosInforme.put("idPersona", idPersona);
			datosInforme.put("idInstitucion", destProgramInfBean
					.getIdInstitucionPersona().toString());
			datosInforme.put("idTipoInforme", programInfBean.getIdTipoInforme());

			datosInforme.putAll(htClavesProgramacion);
			Vector vDocumentos = null;
			if(!programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesExpedientes)){
			
			
			
			
			
			datosInforme.putAll(htClavesProgramacion);
			if(alClavesDestinatario==null){
				//datosInforme.putAll(htClavesDestinatario);
				vDocumentos = getDocumentosAEnviar(datosInforme,
						vPlantillasInforme, usrBean,
						EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme());
			}else{
				
				
				vDocumentos = new Vector();
	
				ArrayList alFacturas = new ArrayList();
				
				for (int i = 0; i < alClavesDestinatario.size(); i++) {
					if(!programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
						Hashtable  htClaves =   (Hashtable) alClavesDestinatario.get(i);
						datosInforme.putAll(htClaves);
						vDocumentos.addAll(getDocumentosAEnviar(datosInforme,vPlantillasInforme, usrBean,
								EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme()));
					}else{
						Hashtable  htClaves =   (Hashtable) alClavesDestinatario.get(i);
						String idFactura = (String)htClaves.get("idFactura");
						alFacturas.add(idFactura);
						
					}
					
					
				}
				if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
					datosInforme.put("idFacturas", alFacturas);
					vDocumentos.addAll(getDocumentosAEnviar(datosInforme,vPlantillasInforme, usrBean,
							EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme()));
				}
			}
			
			
			}else{
// caso de envios de expedientes
				
				
				vDocumentos = new Vector();
				if(alClavesDestinatario==null){
					//datosInforme.putAll(htClavesDestinatario);
					//vDocumentos = getDocumentosAEnviar(datosInforme,
					//		vPlantillasInforme, usrBean, EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme());
				}else{
					//List aClavesMultiple = (ArrayList)htClavesDestinatario.get("clavesMultiple");
					//datosInforme.putAll(htClavesDestinatario);
					//vDocumentos = new Vector();
					for (int i = 0; i < alClavesDestinatario.size(); i++) {
						Hashtable  htClaves =   (Hashtable) alClavesDestinatario.get(i);
						datosInforme.putAll(htClaves);
						//vDocumentos.addAll(getDocumentosAEnviar(datosInforme,vPlantillasInforme, usrBean,
						//		EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme()));
						
					}
				}
							
				////////////////////*************************************************************
				
				Vector vPlantillas = null;
				// recorro los expedientes a enviar
				//for (int i = 0; i < vCampos.size(); i++) {
				//	datosInforme = (Hashtable) vCampos.get(i);
					
					// Obtengo los datos del expediente
					String idiomaExt = (String) datosInforme.get("idiomaExt");
					if (idiomaExt == null || idiomaExt.equals(""))
						idiomaExt = usrBean.getLanguageExt();
					datosInforme.put("idiomaExt", idiomaExt);
					String idioma = (String) datosInforme.get("idioma");
					if (idioma == null || idioma.equals(""))
						idioma = usrBean.getLanguage();
					datosInforme.put("idioma", idioma);
					String idPersona2 = (String) datosInforme.get("idPersona");
					String idInstitucion2 = (String) datosInforme.get("idInstitucion");
					String anio = (String) datosInforme.get("anioExpediente");
					String numero = (String) datosInforme.get("numeroExpediente");
					String idInstitucionTipoExp = (String) datosInforme.get("idInstitucionTipoExp");
					String idTipoExp = (String) datosInforme.get("idTipoExp");

					// Obtengo las plantillas afectadas
					// if(vPlantillas==null){
					// 	String plantillas = (String) datosInforme.get("plantillas");
					// 	vPlantillas = this.getPlantillas(plantillas,usrBean.getLocation(),usrBean);
					// }
					vPlantillas = vPlantillasInforme;
					// voy a mirar si alguno de los informes es asolicitantes
					boolean aSolicitantes = this.esAlgunaASolicitantes(vPlantillas);
					
					
					// Obtengo los datos de la consulta. 
					// A CADA UNO DE LOS REGISTROS DE ESTA CONSULTA SE LE ENVÍAN TODOS LOS DOCUMENTOS
					// DE LAS PLANTILLAS OBTENIDAS ARRIBA, RESUELTOS PARA EL DESTINATARIO.
					ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm(usrBean);
					Vector vDatosInformeFinal = expedienteAdm.getDatosInformeExpediente(idInstitucion2, idInstitucionTipoExp, 
							idTipoExp, anio, numero, idPersona2, true, aSolicitantes );

					// Anotación en cada expediente
					if (vDatosInformeFinal.size()==1) {
						Envio.generarComunicacionExpediente(idInstitucion2,new Integer(idInstitucionTipoExp),
								new Integer(idTipoExp),new Integer(numero),new Integer(anio),idPersona2,usrBean);
					} else {
						Envio.generarComunicacionExpediente(idInstitucion2,new Integer(idInstitucionTipoExp),
								new Integer(idTipoExp),new Integer(numero),new Integer(anio),null,usrBean);

					}
					// RECORRER LOS DATOSINFORMEFINAL 
					// Por cada destinatario real
					//if (vDatosInformeFinal.size()>0){
					for (int h=0;h<vDatosInformeFinal.size();h++){
						Hashtable datoReal = (Hashtable)vDatosInformeFinal.get(h);
						String idPersonaReal = (String) datoReal.get("IDPERSONA");
						String idDireccionReal = (String) datoReal.get("IDDIRECCION_PRIN");
						datoReal.put("idTipoInforme", "EXP");
						datoReal.put("idPersona", (String) datosInforme.get("idPersona"));
						datoReal.put("idInstitucion", (String) datosInforme.get("idInstitucion"));
						datoReal.put("anioExpediente", (String) datosInforme.get("anioExpediente"));
						datoReal.put("numeroExpediente", (String) datosInforme.get("numeroExpediente"));
						datoReal.put("idInstitucionTipoExp", (String) datosInforme.get("idInstitucionTipoExp"));
						datoReal.put("idTipoExp", (String) datosInforme.get("idTipoExp"));
						//datoReal.put("plantillas", (String) datosInforme.get("plantillas"));
						datoReal.put("aSolicitantes", (aSolicitantes)?"S":"N");
						
						if (idPersonaReal!=null && !idPersonaReal.trim().equals("")) {
							
							vDocumentos.addAll(this.getDocumentosAEnviar(datoReal,vPlantillas, usrBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesExpedientes));
							// Genera el envio:
							
						}
					}
				
			}
			

			
			//en el metodo getDatosInformeFinal metemos la key definitiva idFacturas en el datosInforme
			ArrayList alFacturas = ((ArrayList)datosInforme.get("idFacturas"));
			//Vamos a acumular las comunicaciones para luego insertarlas ya que en el proceso de envio se
			//les setea el idEnvio
			if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
				ComunicacionMoroso comunicacion = new ComunicacionMoroso();
				comunicacion.setIdPersona(destProgramInfBean.getIdPersona());
				comunicacion.setDocumentos(vDocumentos);
				comunicacion.setFacturas(alFacturas);
				comunicacion.setIdInstitucion(programInfBean.getIdInstitucion());
				comunicacion.setDescripcion(enviosBean.getDescripcion());
				lComunicacionesMorosos.add(comunicacion);
			}

			if (htPersonas.containsKey(idPersona)) {
				Vector vAuxDocumentos = (Vector) htPersonas.get(idPersona);
				vDocumentos.addAll(vAuxDocumentos);

			}
			htPersonas.put(idPersona, vDocumentos);

		}

		envio.generarEnvioOrdinario(envio.getEnviosBean(), htPersonas);
		if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
			for (int i = 0; i < lComunicacionesMorosos.size(); i++) {
				ComunicacionMoroso comunicacion = (ComunicacionMoroso)lComunicacionesMorosos.get(i);
				envio.generarComunicacionMoroso(comunicacion.getIdPersona().toString(),
						comunicacion.getDocumentos(),comunicacion.getFacturas(),
						comunicacion.getIdInstitucion().toString(),comunicacion.getDescripcion());	
				
			}
			
			
		}
		


	}
	
	
	/**
	 * Este metodo es el encargado de generar envios NO ordinarios de correos(e-mail, fax).
	 * 
	 * @param usrBean
	 * @param vDestProgramInfBean Destinatarios del envio
	 * @param programInfBean Programacion de informes genericos
	 * @param vPlantillasInforme  Informes a generar por destinatario
	 * @param envioProgramadoBean Maestro de programacion de envios
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */

	public void enviarInformeGenerico(UsrBean usrBean,
			EnvDestProgramInformesBean destProgramInfBean,
			EnvProgramInformesBean programInfBean, Vector vPlantillasInforme,
			EnvEnvioProgramadoBean envioProgramadoBean) throws ClsExceptions,
			SIGAException {

		Envio envio = new Envio(usrBean, envioProgramadoBean.getNombre());
		Hashtable datosInforme = null;
		Vector vDocumentos = null;
		
		// Bean envio
		EnvEnviosBean enviosBean = envio.getEnviosBean();
		//pdm
		CenPersonaAdm persAdm = new CenPersonaAdm(this.getUsuario());
		
		enviosBean.setDescripcion(enviosBean.getIdEnvio() + " "
				+ enviosBean.getDescripcion()+" "+persAdm.obtenerNombreApellidos(destProgramInfBean.getIdPersona().toString()));
		// trunco la descripción
		if (enviosBean.getDescripcion().length()>200)  enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,99));
		// Preferencia del tipo de envio si el usuario tiene uno:
		enviosBean.setIdTipoEnvios(envioProgramadoBean.getIdTipoEnvios());
		if(envioProgramadoBean.getIdTipoEnvios()!=null && envioProgramadoBean.getIdTipoEnvios()!=null &&!envioProgramadoBean.getIdTipoEnvios().toString().equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))			
			envio.getEnviosBean().setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
		
		enviosBean.setFechaProgramada(envioProgramadoBean.getFechaProgramada());
		enviosBean.setIdPlantillaEnvios(envioProgramadoBean
				.getIdPlantillaEnvios());
		if (envioProgramadoBean.getIdPlantilla() != null
				&& !envioProgramadoBean.getIdPlantilla().equals("")) {
			enviosBean.setIdPlantilla(envioProgramadoBean.getIdPlantilla());
		} else {
			enviosBean.setIdPlantilla(null);
		}
		datosInforme = new Hashtable();

		Hashtable htClavesProgramacion = getClaves(programInfBean.getClaves());

		
		ArrayList alClavesDestinatario = destProgramInfBean.getClavesDestinatario();
		



		datosInforme.put("idioma", programInfBean.getIdioma().toString());
		datosInforme.put("idiomaExt", programInfBean.getIdiomaCodigoExt());
		datosInforme.put("idPersona", destProgramInfBean.getIdPersona()
				.toString());
		datosInforme.put("idInstitucion", destProgramInfBean
				.getIdInstitucionPersona().toString());
		datosInforme.put("idTipoInforme", programInfBean.getIdTipoInforme());
		
		if(!programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesExpedientes)){
		
			
	
			// (JTA) IDEA!!!!! Ahora mismo el idioma de las comunicaciones es el de el usuario que la genera. si por necesidades se va a meter
			// en algun formulario el idioma seleccionable, se puede meter como clave de la
			// programacion. de este modo el idioma inicial se macahacara con este ultimo, por eso es
			// importante que el putAll este aqui y no antes.
			
			datosInforme.putAll(htClavesProgramacion);
			if(alClavesDestinatario==null){
				//datosInforme.putAll(htClavesDestinatario);
				vDocumentos = getDocumentosAEnviar(datosInforme,
						vPlantillasInforme, usrBean, EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme());
			}else{
				//List aClavesMultiple = (ArrayList)htClavesDestinatario.get("clavesMultiple");
				//datosInforme.putAll(htClavesDestinatario);
				vDocumentos = new Vector();
				ArrayList alFacturas = new ArrayList();
				for (int i = 0; i < alClavesDestinatario.size(); i++) {
					
					
					if(!programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
						Hashtable  htClaves =   (Hashtable) alClavesDestinatario.get(i);
						datosInforme.putAll(htClaves);
						vDocumentos.addAll(getDocumentosAEnviar(datosInforme,vPlantillasInforme, usrBean,
								EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme()));
					}else{
						Hashtable  htClaves =   (Hashtable) alClavesDestinatario.get(i);
						String idFactura = (String)htClaves.get("idFactura");
						alFacturas.add(idFactura);
						
					}
					
				}
				if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
					datosInforme.put("idFacturas", alFacturas);
					vDocumentos.addAll(getDocumentosAEnviar(datosInforme,vPlantillasInforme, usrBean,
							EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme()));
				}
			}
			
	
	
			// Genera el envio:
			envio.generarEnvio(destProgramInfBean.getIdPersona().toString(),
					vDocumentos);

		} else {
			// caso de envio de expedientes

			
			vDocumentos = null;
			
			datosInforme.putAll(htClavesProgramacion);
			if(alClavesDestinatario==null){
				//datosInforme.putAll(htClavesDestinatario);
				//vDocumentos = getDocumentosAEnviar(datosInforme,
				//		vPlantillasInforme, usrBean, EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme());
			}else{
				//List aClavesMultiple = (ArrayList)htClavesDestinatario.get("clavesMultiple");
				//datosInforme.putAll(htClavesDestinatario);
				//vDocumentos = new Vector();
				for (int i = 0; i < alClavesDestinatario.size(); i++) {
					Hashtable  htClaves =   (Hashtable) alClavesDestinatario.get(i);
					datosInforme.putAll(htClaves);
					//vDocumentos.addAll(getDocumentosAEnviar(datosInforme,vPlantillasInforme, usrBean,
					//		EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme()));
					
				}
			}
						
			////////////////////*************************************************************
			
			Vector vPlantillas = null;
			// recorro los expedientes a enviar
			//for (int i = 0; i < vCampos.size(); i++) {
			//	datosInforme = (Hashtable) vCampos.get(i);
				
				// Obtengo los datos del expediente
				String idiomaExt = (String) datosInforme.get("idiomaExt");
				if (idiomaExt == null || idiomaExt.equals(""))
					idiomaExt = usrBean.getLanguageExt();
				datosInforme.put("idiomaExt", idiomaExt);
				String idioma = (String) datosInforme.get("idioma");
				if (idioma == null || idioma.equals(""))
					idioma = usrBean.getLanguage();
				datosInforme.put("idioma", idioma);
				String idPersona2 = (String) datosInforme.get("idPersona");
				String idInstitucion2 = (String) datosInforme.get("idInstitucion");
				String anio = (String) datosInforme.get("anioExpediente");
				String numero = (String) datosInforme.get("numeroExpediente");
				String idInstitucionTipoExp = (String) datosInforme.get("idInstitucionTipoExp");
				String idTipoExp = (String) datosInforme.get("idTipoExp");

				// Obtengo las plantillas afectadas
				//if(vPlantillas==null){
				//	String plantillas = (String) datosInforme.get("plantillas");
				//	vPlantillas = this.getPlantillas(plantillas,usrBean.getLocation(),usrBean);
				//}
				vPlantillas = vPlantillasInforme;
				
				// voy a mirar si alguno de los informes es asolicitantes
				boolean aSolicitantes = this.esAlgunaASolicitantes(vPlantillas);
				
				
				// Obtengo los datos de la consulta. 
				// A CADA UNO DE LOS REGISTROS DE ESTA CONSULTA SE LE ENVÍAN TODOS LOS DOCUMENTOS
				// DE LAS PLANTILLAS OBTENIDAS ARRIBA, RESUELTOS PARA EL DESTINATARIO.
				ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm(usrBean);
				Vector vDatosInformeFinal = expedienteAdm.getDatosInformeExpediente(idInstitucion2, idInstitucionTipoExp, 
						idTipoExp, anio, numero, idPersona2, true, aSolicitantes );

				// Anotación en cada expediente
				if (vDatosInformeFinal.size()==1) {
					Envio.generarComunicacionExpediente(idInstitucion2,new Integer(idInstitucionTipoExp),
							new Integer(idTipoExp),new Integer(numero),new Integer(anio),idPersona2,usrBean);
				} else {
					Envio.generarComunicacionExpediente(idInstitucion2,new Integer(idInstitucionTipoExp),
							new Integer(idTipoExp),new Integer(numero),new Integer(anio),null,usrBean);
				}

				// RECORRER LOS DATOSINFORMEFINAL 
				// Por cada destinatario real
				for (int j=0;j<vDatosInformeFinal.size();j++){
					Hashtable datoReal = (Hashtable)vDatosInformeFinal.get(j);
					String idPersonaReal = (String) datoReal.get("IDPERSONA_DEST");
					String idDireccionReal = (String) datoReal.get("IDDIRECCION_DEST");
					datoReal.put("idTipoInforme", "EXP");
					datoReal.put("idPersona", (String) datosInforme.get("idPersona"));
					datoReal.put("idInstitucion", (String) datosInforme.get("idInstitucion"));
					datoReal.put("anioExpediente", (String) datosInforme.get("anioExpediente"));
					datoReal.put("numeroExpediente", (String) datosInforme.get("numeroExpediente"));
					datoReal.put("idInstitucionTipoExp", (String) datosInforme.get("idInstitucionTipoExp"));
					datoReal.put("idTipoExp", (String) datosInforme.get("idTipoExp"));
					//datoReal.put("plantillas", (String) datosInforme.get("plantillas"));
					datoReal.put("aSolicitantes", (aSolicitantes)?"S":"N");
					
					if (idPersonaReal!=null && !idPersonaReal.trim().equals("")) {
						//Antes de crear el envío voy a poner el nombre adecuado
						envioProgramadoBean.setNombre(UtilidadesString.getMensajeIdioma(usrBean, "informes.genericos.expedientes.asunto")+ " "+(String) datoReal.get("NOMBRE_DEST"));
						// Hago un envio
						Envio envio2 = new Envio(usrBean, envioProgramadoBean.getNombre());
						// Bean envio
						enviosBean = envio2.getEnviosBean();
						
						// Preferencia del tipo de envio si el usuario tiene uno:
						enviosBean.setIdTipoEnvios(envioProgramadoBean.getIdTipoEnvios());
						enviosBean.setFechaProgramada(envioProgramadoBean.getFechaProgramada());
						enviosBean.setIdPlantillaEnvios(envioProgramadoBean
								.getIdPlantillaEnvios());
						if (envioProgramadoBean.getIdPlantilla() != null
								&& !envioProgramadoBean.getIdPlantilla().equals("")) {
							enviosBean.setIdPlantilla(envioProgramadoBean.getIdPlantilla());
						} else {
							enviosBean.setIdPlantilla(null);
						}
						enviosBean.setDescripcion(enviosBean.getIdEnvio()+" "+enviosBean.getDescripcion());
						// trunco la descripción
						if (enviosBean.getDescripcion().length()>200)  enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,99));
						
						// obtengo sus documentos según vPlantillas
												
						vDocumentos = new Vector();
						vDocumentos.addAll(this.getDocumentosAEnviar(datoReal,vPlantillas, usrBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesExpedientes));
						// Genera el envio:
						
						envio2.generarEnvioDireccionEspecifica(idPersonaReal, idDireccionReal, vDocumentos);				
					}
				}
				
			//}
			////////////////////*************************************************************		

			
		}
		
		
		//Generamos la comunicacion de morosos
		if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
			ArrayList alFacturas = (ArrayList)datosInforme.get("idFacturas");
			envio.generarComunicacionMoroso(destProgramInfBean.getIdPersona().toString(),
					vDocumentos,alFacturas,programInfBean.getIdInstitucion().toString(),enviosBean.getDescripcion());
		}
		//Generamos la comunicacion de expedientes
		if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesExpedientes)){
			String idInstitucion = (String)datosInforme.get("idInstitucion");
			String idInstitucionTipoExp = (String)datosInforme.get("idInstitucionTipoExp");
    		String idTipoExp = (String)datosInforme.get("idTipoExp");
			
			String anio = (String)datosInforme.get("anioExpediente");
			
			String numero = (String)datosInforme.get("numeroExpediente");
			
			String idPersona = (String)datosInforme.get("idPersona");
			
			
			//envio.generarComunicacionExpediente(idInstitucion,new Integer(idInstitucionTipoExp)
			//,new Integer(idTipoExp),new Integer(numero),new Integer(anio),idPersona,usrBean);
		}

	}
	/**
	 * Este metodo convierte una cadena de caracteres(pepe=2#juan=6)--> en un hashtable de claves({[pepe,6],[juan,4]})
	 * @param claves
	 * @return
	 * @throws ClsExceptions
	 */
	private Hashtable getClaves(String claves) throws ClsExceptions {
		Hashtable htClaves = new Hashtable();
		GstStringTokenizer st = new GstStringTokenizer(claves, "##");
		GstStringTokenizer stClaveMultiple = null; 
		List aDatosMultiple =  new ArrayList();
		while (st.hasMoreTokens()) {
			String dupla = st.nextToken();
			String d[] = dupla.split("==");
			// Cuando no vienen datos completos es porque hay alguna funcionalidad que no requiere estos datos
			// ejemplo:idPersona del Certificado de IRPF. cuando se saca individulamente
			// desde la icha colegial si tiene persona. cuando se saca desde la opcion de menu de
			// informes no porque se supone que es para todos los colegiados que hayan tenido pagos el
			// anyo del informe
			
			if (d.length > 1){
				String clavesMultiples = d[1];
				stClaveMultiple = new GstStringTokenizer(clavesMultiples, "||");
				Hashtable aux = new Hashtable();
				while (stClaveMultiple.hasMoreTokens()) {
					
					String dupla2 = stClaveMultiple.nextToken();
					
					String d2[] = dupla2.split("=");
					
					if (d2.length > 1){
						
						//htClaves.put(d2[0], d2[1]);
						
						aux.put(d2[0], d2[1]);
						
					}
					
					
					
				}
				if(aux.size()>0)
					aDatosMultiple.add(aux);
				
				
					
				htClaves.put(d[0], d[1]);
				
			}
		}
		if(aDatosMultiple.size()>0)
			htClaves.put("clavesMultiple", aDatosMultiple);

		return htClaves;
	}
	
	/**
	 * Metodo que convierte una cadena de caracteres separados por @@(COB1@@COB2) en un array de AdmInformeBean([COB1,,COB2])
	 * @param plantillas
	 * @param idInstitucion
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */

	public Vector getPlantillas(String plantillas, String idInstitucion,
			UsrBean usr) throws ClsExceptions, SIGAException {

		String d[] = plantillas.split("@@");
		Vector vPlantillas = new Vector();
		AdmInformeAdm admInforme = new AdmInformeAdm(usr);

		for (int i = 0; i < d.length; i++) {
			String idPlantilla = d[i];
			AdmInformeBean beanInforme = admInforme.obtenerInforme(
					idInstitucion, idPlantilla);
			vPlantillas.add(beanInforme);

		}
		return vPlantillas;

	}
	
	public boolean esAlgunaASolicitantes(Vector plantillas) throws ClsExceptions, SIGAException {

		for (int i = 0; i < plantillas.size(); i++) {
			AdmInformeBean plantillaInforme = (AdmInformeBean)plantillas.get(i);
			if (plantillaInforme.getASolicitantes().equals("S")) {
				return true;
			}
		}
		return false;

	}	
	
	public boolean esAlgunaASolicitantes(String idInstitucion, String plantillas) throws ClsExceptions, SIGAException {
		AdmInformeAdm informeAdm = new AdmInformeAdm(this.getUsuario());
		String plan[] = plantillas.split("@@");
		for (int i = 0; i < plan.length; i++) {
			String pl = plan[i];
			AdmInformeBean plantillaInforme = informeAdm.obtenerInforme(idInstitucion, pl);
			if (plantillaInforme.getASolicitantes().equals("S")) {
				return true;
			}
		}
		return false;

	}
	
	//GENEXP@@GENEXP2
	/**
	 * Metodo que nos convierte una cadena de caracteres separados(pepe||juan||pedro)
	 *  por un string en un vector de datos([pepe,juan,pedro])
	 * @param cadenaUnida
	 * @param separador
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */

	public Vector getDatosSeparados(String cadenaUnida,String separador) throws ClsExceptions,
	SIGAException {

		String d[] = cadenaUnida.split(separador);
		Vector vDatos = new Vector();

		for (int i = 0; i < d.length; i++) {
			String idDato = d[i];
			vDatos.add(idDato);

		}
		return vDatos;

	}
	/**
	 * Metodo que nos incluye un vector de plantillas transformado en texto plano y nos lo
	 * incluye en el texto plano a tratar delos datos a enviar  
	 * @param form
	 * @param vPlantillas
	 * @return
	 * @throws SIGAException
	 * @throws Exception
	 */

	private String getDatosAEnviar(InformesGenericosForm form,
			Vector vPlantillas) throws SIGAException, Exception {

		String datosInforme = form.getDatosInforme();
		StringBuffer datosEnvios = new StringBuffer();
		GstStringTokenizer st1 = new GstStringTokenizer(datosInforme, "%%%");
		while (st1.hasMoreTokens()) {

			String registro = st1.nextToken();
			if (registro != null && !registro.equals("")) {
				datosEnvios.append(registro);
				datosEnvios.append("##");
				datosEnvios.append("plantillas==");
				for (int j = 0; j < vPlantillas.size(); j++) {
					String idPlantilla = (String) vPlantillas.get(j);

					datosEnvios.append(idPlantilla);
					if (j != vPlantillas.size() - 1)
						datosEnvios.append("@@");

				}
				datosEnvios.append("##");
				datosEnvios.append("idTipoInforme==");
				datosEnvios.append(form.getIdTipoInforme());

				if (form.getTipoPersonas() != null
						&& !form.getTipoPersonas().equals("")) {
					datosEnvios.append("##");
					datosEnvios.append("idTipoPersonas==");
					datosEnvios.append(form.getTipoPersonas());
				}
				datosEnvios.append("%%%");
			}

		}

		return datosEnvios.toString();

	}
	
	/**
	 * Funcion principal de la clase que nos gestiona el envio o descarga de los informes genericos.
	 * En caso de que sea descargar es el metodo que se encarga que generar y empaquetar los archivos
	 * necesarios. En el caso de que sea enviar, es el encargado de preparar los datos que mas tarde 
	 * se manejaran. 
	 * Este metodo se invocara desde InformesGenericosAction y generara los informes o preparada los datos
	 * para los futuros envios. En este caso el encargado de invocar la genetacion de envios sera  DefinirEnviosAction 
	 */
	public File getInformeGenerico (InformesGenericosForm informeGenerico,
									String idsesion,
									UsrBean usr, 
									boolean isEnviar,boolean isPermisoEnvio)
		throws SIGAException, Exception
	{
		File ficheroSalida = null; //Fichero para devolver
		
		//comprobando que hay que generar algun informe
		if (informeGenerico.getDatosInforme() == null)
			throw new SIGAException("messages.informes.ningunInformeGenerado");
		if (informeGenerico.getDatosInforme().trim().equals(""))
			throw new SIGAException("messages.informes.ningunInformeGenerado");
		
		if (isEnviar &&isPermisoEnvio) { //se prepara el envio, pero nada mas
			setDatosEnvios(getDatosAEnviar(
							informeGenerico, 
							getDatosSeparados(informeGenerico.getIdInforme(), "##")));
			ficheroSalida = null;
		}
		else { //se generan los ficheros para descargar
			if(!isPermisoEnvio)
				informeGenerico.setDatosInforme(getDatosAEnviar(informeGenerico, 
							getDatosSeparados(informeGenerico.getIdInforme(), "##")));
			Vector informesRes = new Vector();
			Vector datosFormulario = this.obtenerDatosFormulario(informeGenerico);
			
			//En el caso de que la iteracion de los datos del informe(tablas 1-->n),
			//la clave iterante sera la clave por la que iteraran las personas. 
			//Este sera el caso de que los docuemntos tengan regiones
			String claveIterante = informeGenerico.getClavesIteracion();
			if (claveIterante != null && !claveIterante.equals("")) {
				datosFormulario = setCamposIterantes(
						datosFormulario, claveIterante);
			}
			
			Vector vPlantillas = null;
			Hashtable datosInforme;
			String idTipoInforme;
			for (int j = 0; j < datosFormulario.size(); j++) {
				datosInforme = (Hashtable) datosFormulario.get(j);
				idTipoInforme = (String) datosInforme.get("idTipoInforme");
				
				if (datosInforme != null) {
					//cargando plantillas una sola vez
					if (vPlantillas == null) {
						vPlantillas = getPlantillas((String) datosInforme.get("plantillas"), usr
								.getLocation(), usr);
					}
					
					//Informe de comunicaciones de EJGs
					if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesExpedientes)) {
						
						//obteniendo los datos del expediente
						String idiomaExt = (String) datosInforme.get("idiomaExt");
						if (idiomaExt == null || idiomaExt.equals(""))
							idiomaExt = usr.getLanguageExt();
						datosInforme.put("idiomaExt", idiomaExt);
						String idioma = (String) datosInforme.get("idioma");
						if (idioma == null || idioma.equals(""))
							idioma = usr.getLanguage();
						datosInforme.put("idioma", idioma);
						String idPersona2 = (String) datosInforme.get("idPersona");
						String idInstitucion2 = (String) datosInforme.get("idInstitucion");
						String anio = (String) datosInforme.get("anioExpediente");
						String numero = (String) datosInforme.get("numeroExpediente");
						String idInstitucionTipoExp = (String) datosInforme.get("idInstitucionTipoExp");
						String idTipoExp = (String) datosInforme.get("idTipoExp");
						boolean aSolicitantes = this.esAlgunaASolicitantes(vPlantillas);
						
						//consultando los datos 
						// A CADA UNO DE LOS REGISTROS DE ESTA CONSULTA SE LE ENVÍAN TODOS LOS DOCUMENTOS
						// DE LAS PLANTILLAS OBTENIDAS ARRIBA, RESUELTOS PARA EL DESTINATARIO.
						ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm(usr);
						Vector vDatosInformeFinal = expedienteAdm.getDatosInformeExpediente(
								idInstitucion2, idInstitucionTipoExp, 
								idTipoExp, anio, numero, idPersona2, true, aSolicitantes);
						
						//anotando cada expediente
						if (vDatosInformeFinal.size()==1) {
							Envio.generarComunicacionExpediente(
									idInstitucion2, new Integer(idInstitucionTipoExp),
									new Integer(idTipoExp), new Integer(numero),
									new Integer(anio), idPersona2, usr);
						}
						else {
							Envio.generarComunicacionExpediente(
									idInstitucion2, new Integer(idInstitucionTipoExp),
									new Integer(idTipoExp), new Integer(numero),
									new Integer(anio), null, usr);
						}
						
						//generando los documentos para cada persona
						Hashtable datoReal;
						String idPersonaReal, idDireccionReal;
						for (int k=0; k<vDatosInformeFinal.size(); k++) {
							datoReal = (Hashtable)vDatosInformeFinal.get(k);
							idPersonaReal = (String) datoReal.get("IDPERSONA_DEST");
							idDireccionReal = (String) datoReal.get("IDDIRECCION_DEST");
							datoReal.put("idTipoInforme", "EXP");
							datoReal.put("idPersona", (String) datosInforme.get("idPersona"));
							datoReal.put("idInstitucion", (String) datosInforme.get("idInstitucion"));
							datoReal.put("anioExpediente", (String) datosInforme.get("anioExpediente"));
							datoReal.put("numeroExpediente", (String) datosInforme.get("numeroExpediente"));
							datoReal.put("idInstitucionTipoExp", (String) datosInforme.get("idInstitucionTipoExp"));
							datoReal.put("idTipoExp", (String) datosInforme.get("idTipoExp"));
							datoReal.put("plantillas", (String) datosInforme.get("plantillas"));
							datoReal.put("aSolicitantes", (aSolicitantes)?"S":"N");
							
							// inc-6975 No es obligatorio que tenga direccion para meterlo en el zip
							//if (idPersonaReal!=null && !idPersonaReal.trim().equals("")) {
								Vector vDocumentos = new Vector();
								vDocumentos.addAll(this.getDocumentosAEnviar(
										datoReal, vPlantillas, usr,
										EnvioInformesGenericos.docFile,
										EnvioInformesGenericos.comunicacionesExpedientes));
								informesRes.addAll(vDocumentos);
							//}
						}
						
					//Otros Informes de comunicaciones
					}
					else {
						informesRes.addAll(this.getDocumentosAEnviar(
								datosInforme, vPlantillas, usr,
								EnvioInformesGenericos.docFile, idTipoInforme));
					}
				}
			}

			//como es para descargar, se generan los ficheros y se comprimen en ZIP
			if (informesRes.size() == 0)
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			else if (informesRes.size() == 1)
				ficheroSalida = (File) informesRes.get(0);
			else {
				AdmTipoInformeAdm admT = new AdmTipoInformeAdm(usr);
				AdmTipoInformeBean beanT = admT.obtenerTipoInforme(
						informeGenerico.getIdTipoInforme());
				ArrayList ficherosPDF = new ArrayList();
				for (int i = 0; i < informesRes.size(); i++) {
					File f = (File) informesRes.get(i);
					ficherosPDF.add(f);
				}
				
				String nombreFicheroZIP = 
					idsesion.replaceAll("!", "") + "_" +
					UtilidadesBDAdm.getFechaCompletaBD("").
							replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
			    ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				String rutaServidorDescargasZip = 
					rp.returnProperty("informes.directorioFisicoSalidaInformesJava") +
					rp.returnProperty("informes.directorioPlantillaInformesJava") + ClsConstants.FILE_SEP +
					usr.getLocation() + ClsConstants.FILE_SEP +
					"temp" + File.separatorChar;
				File ruta = new File(rutaServidorDescargasZip);
				ruta.mkdirs();
				Plantilla.doZip(rutaServidorDescargasZip, nombreFicheroZIP, ficherosPDF);
				ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
			}
		}
		
		return ficheroSalida;
	} //getInformeGenerico()
	
	/**
	 * Metodo que nos agrupa en un vector de hastable los datos por los que itera. 
	 * Se incluira en un elemento de este hashtable de tipo ArtrayList 
	 * y con la clave que es el PLURAL de la clave iterante
	 * [idpersona=1,idFactura=22],[idPersona=1,idFactura=33]-->{idPersona=1,[idFactura=2,idFactura=33]}
	 * 
	 * @param vCampos
	 * @param claveIterante
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector setCamposIterantes(Vector vCampos, String claveIterante)
	throws ClsExceptions {
		Hashtable htPersona = new Hashtable();
		ArrayList alClaves = null;
		Vector vCamposConArray = new Vector();
		String claveArrayIterante = claveIterante + "s";
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			String idPersona = (String) ht.get("idPersona");
			String idClave = (String) ht.get(claveIterante);

			if (htPersona.containsKey(idPersona)) {
				alClaves = (ArrayList) htPersona.get(idPersona);

			} else {
				alClaves = new ArrayList();
				vCamposConArray.add(ht);

			}
			if (!alClaves.contains(idClave))
				alClaves.add(idClave);
			htPersona.put(idPersona, alClaves);

			ht.put(claveArrayIterante, alClaves);

		}

		return vCamposConArray;

	}
	private Vector setCamposIterantes(Vector vCampos, ArrayList clavesIterante,String nombre)
	throws ClsExceptions {
		Hashtable htPersona = new Hashtable();
		ArrayList alClaves = null;
		Vector vCamposConArray = new Vector();
		Map htClave = null;
		
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			String idPersona = (String) ht.get("idPersona");
			htClave = new Hashtable();
			for (int j = 0; j < clavesIterante.size(); j++) {
				String clave = (String)clavesIterante.get(j);
				
				String idClave = (String) ht.get(clave);
				htClave.put(clave, idClave);
				
				
			}
			

			if (htPersona.containsKey(idPersona)) {
				alClaves = (ArrayList) htPersona.get(idPersona);

			} else {
				alClaves = new ArrayList();
				vCamposConArray.add(ht);

			}
			alClaves.add(htClave);
			htPersona.put(idPersona, alClaves);

			ht.put(nombre, alClaves);

		}

		return vCamposConArray;

	}
	private File getInformeGenerico(AdmInformeBean beanInforme,
			Hashtable htDatosInforme, String idiomaExt, String identificador,
			UsrBean usr,int tipoPlantillaInforme) throws SIGAException, ClsExceptions {
		File f = null;
		switch (tipoPlantillaInforme) {
		case tipoPlantillaWord:
			f = getInformeGenericoWord(beanInforme,	htDatosInforme, idiomaExt, identificador,
					usr);
			break;
		case tipoPlantillaFo:
			if(beanInforme.getIdTipoInforme().equals(comunicacionesPagoColegiados)){
				InformeColegiadosPagos infColegiadoFo = new InformeColegiadosPagos();
				htDatosInforme = (Hashtable) htDatosInforme.get("row");
				f = infColegiadoFo.getInformeGenericoFo(beanInforme,	htDatosInforme, idiomaExt, identificador,usr);
				
			}
			break;
		
		}
		return f;
		
	}
	
	
	
	/**
	 * Metodo encargado de crea un informe generico de tipo ASPOSE WORD
	 * 
	 * @param beanInforme
	 * @param htDatosInforme
	 * @param idiomaExt
	 * @param identificador
	 * @param usr
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	private File getInformeGenericoWord(AdmInformeBean beanInforme,
			Hashtable htDatosInforme, String idiomaExt, String identificador,
			UsrBean usr) throws SIGAException, ClsExceptions {
		Date inicio = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis()
				+ ",==> SIGA: INICIO InformesGenericos.getInformeGenerico", 10);

		File ficheroSalida = null;
		// --- acceso a paths y nombres
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");
		String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")
							+ rp.returnProperty("informes.directorioPlantillaInformesJava");
		String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
							+ rp.returnProperty("informes.directorioPlantillaInformesJava");
		// //////////////////////////////////////////////
		// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS

		String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP
		+ usr.getLocation() + ClsConstants.FILE_SEP
		+ beanInforme.getDirectorio() + ClsConstants.FILE_SEP;
		String nombrePlantilla = beanInforme.getNombreFisico() + "_"
		+ idiomaExt + ".doc";
		String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP
		+ usr.getLocation() + ClsConstants.FILE_SEP
		+ beanInforme.getDirectorio();

		File crear = new File(rutaAlm);
		if (!crear.exists())
			crear.mkdirs();

		MasterWords masterWord = new MasterWords(rutaPl + nombrePlantilla);
		Document documento = masterWord.nuevoDocumento();

		Iterator iteDatos = htDatosInforme.keySet().iterator();
		while (iteDatos.hasNext()) {
			String clave = (String) iteDatos.next();
			if (clave.equals("row")) {
				Object oDatosInforme = (Object) htDatosInforme.get("row");
				// Esto son cabecera
				if (oDatosInforme instanceof Vector) {
					Vector vDatosInforme = (Vector) oDatosInforme;
					if (oDatosInforme != null) {
						for (int i = 0; i < vDatosInforme.size(); i++) {
							Hashtable htRowDatosInforme = (Hashtable) vDatosInforme
							.get(i);
							documento = masterWord.sustituyeDocumento(
									documento, htRowDatosInforme);

						}
					}
				} else if (oDatosInforme instanceof Hashtable) {
					Hashtable htRowDatosInforme = (Hashtable) oDatosInforme;
					documento = masterWord.sustituyeDocumento(documento,
							htRowDatosInforme);

				}
			} else {
				// Estos son regiones
				Vector vDatosRegion = (Vector) htDatosInforme.get(clave);
				documento = masterWord.sustituyeRegionDocumento(documento,
						clave, vDatosRegion);

			}

		}

		identificador = identificador + ".doc";

		String nombreArchivo = beanInforme.getNombreSalida() + "_"
		+ identificador;
		ficheroSalida = masterWord.grabaDocumento(documento, rutaAlm,
				nombreArchivo);
		Date fin = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis()
				+ ",==> SIGA: FIN InformesGenericos.getInformeGenerico", 10);
		ClsLogging.writeFileLog(fin.getTime() - inicio.getTime()
				+ " MILISEGUNDOS ,==> SIGA: TIEMPO TOTAL", 10);
		return ficheroSalida;
	}
	
	/**
	 * Metodo que nos informa si de los datos de envios son para un unico colegiado
	 * @param form
	 * @return
	 * @throws ClsExceptions
	 */
	private String getIdColegiadoUnico(DefinirEnviosForm form) throws ClsExceptions{
		MasterReport masterReport = new  MasterReport(); 
		Vector vCampos = masterReport.obtenerDatosFormulario(form);
		String idPersona  = getIdColegiadoUnico(vCampos);

		return idPersona;

	}
	private String getIdColegiadoUnico(Vector vCampos){
		Hashtable htPersonas = new Hashtable();
		String idPersona  = null;
		String idInstitucion  = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idPersona = (String) ht.get("idPersona");
			idInstitucion = (String) ht.get("idInstitucion");
			String key = idPersona+"||"+idInstitucion;
			if(!htPersonas.containsKey(key)&&i!=0){
				idPersona = null;
				break;

			}else{
				htPersonas.put(key, idPersona);

			}

		}

		return idPersona;



	}
	/**
	 * formateamos la fecha programada que viene del formulario
	 * 
	 * @param fechaFormulario
	 * @param locale
	 * @param userBean
	 * @return
	 * @throws ClsExceptions
	 */
	private String getFechaProgramada(String fechaFormulario,Locale locale,UsrBean userBean)throws ClsExceptions{
		// obtener fechaProgramada
		String fechaProgramada = null;
		String fechaProg = fechaFormulario + " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds();

		String language = userBean.getLanguage();
		String format = language.equalsIgnoreCase("EN")?ClsConstants.DATE_FORMAT_LONG_ENGLISH:ClsConstants.DATE_FORMAT_LONG_SPANISH;		    
		GstDate gstDate = new GstDate();
		if (fechaProg != null && !fechaProg.equals("")) {
			Date date = gstDate.parseStringToDate(fechaProg,format,locale);
			//A peticion de Luis pedro retraso 15 minutos el envio
			date.setTime(date.getTime()+900000);
			//date.
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			fechaProgramada = sdf.format(date);
		} else
			fechaProgramada = null;
		return fechaProgramada;
	}
	/**
	 * 	 * Define el bean envio que insertaremos
	 * @param form
	 * @param isEnvioUnico
	 * @param locale
	 * @param userBean
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private Envio getEnvio(DefinirEnviosForm form,boolean isEnvioUnico,Locale locale, UsrBean userBean)throws ClsExceptions,SIGAException{
		EnvEnviosBean enviosBean = new EnvEnviosBean();
		GenParametrosAdm paramAdm = new GenParametrosAdm(userBean);
		// obtener idInstitucion
		String idInstitucion = userBean.getLocation();
		// obtener nombre
		String nombreEnvio = form.getNombre();
		// obtener tipoEnvio
		String idTipoEnvio = form.getIdTipoEnvio();
		// obtener plantilla
		String idPlantilla = form.getIdPlantillaEnvios();
		//obtener plantilla de generacion
		String idPlantillaGeneracion = form.getIdPlantillaGeneracion();

		// obtener fechaProgramada
		String fechaProgramada = getFechaProgramada(form.getFechaProgramada(), locale, userBean);
		EnvEnviosAdm envioAdm = new EnvEnviosAdm(userBean);

		enviosBean.setIdInstitucion(Integer.valueOf(idInstitucion));

		if(isEnvioUnico){
			//String idEnvio = form.getIdEnvio();
			//enviosBean.setIdEnvio(Integer.valueOf(form.getIdEnvio()));
			Integer idEnvio = envioAdm.getNewIdEnvio(idInstitucion);
			enviosBean.setIdEnvio(idEnvio);
			form.setIdEnvio(idEnvio.toString());
		}


		enviosBean.setDescripcion(nombreEnvio);
		// trunco la descripción
		if (enviosBean.getDescripcion().length()>200)  enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,99));

		enviosBean.setIdTipoEnvios(Integer.valueOf(idTipoEnvio));
		enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
		if (idPlantillaGeneracion!=null && !idPlantillaGeneracion.equals("")) {
			enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
		} else {
			enviosBean.setIdPlantilla(null);
		}
		enviosBean.setFechaProgramada(fechaProgramada);
		enviosBean.setFechaCreacion("SYSDATE");
		enviosBean.setGenerarDocumento(paramAdm.getValor(idInstitucion,"ENV","GENERAR_DOCUMENTO_ENVIO","C"));
		enviosBean.setImprimirEtiquetas(paramAdm.getValor(idInstitucion,"ENV","IMPRIMIR_ETIQUETAS_ENVIO","1"));
		if (fechaProgramada==null || fechaProgramada.equals(""))
			enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
		else
			enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));


		// trunco la descripción
		if (enviosBean.getDescripcion().length()>200)  enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,99));
		// Preferencia del tipo de envio si el usuario tiene uno:
		enviosBean.setIdTipoEnvios(new Integer(idTipoEnvio));

		enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
		if (idPlantillaGeneracion!=null && !idPlantillaGeneracion.equals("")) {
			enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
		} else {
			enviosBean.setIdPlantilla(null);
		}
		// Bean envio
		Envio envio = new Envio(enviosBean,userBean);

		return envio;

	}
	/**
	 * Metodo que gestiona el envio de las comunicaciones de Censo
	 * 
	 * @param form
	 * @param isEnvioBatch
	 * @param locale
	 * @param userBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void gestionarComunicacionCenso(DefinirEnviosForm form,Locale locale, UsrBean userBean)throws ClsExceptions,SIGAException{

		// Obtenemos la información pertinente relacionada
		String idPersona = getIdColegiadoUnico(form);
		String idInstitucion = userBean.getLocation();


		if(idPersona!=null){

			Vector vCampos = this.obtenerDatosFormulario(form);
			Vector vDocumentos = new Vector();
			Vector vPlantillas = null;
			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable datosInforme = (Hashtable) vCampos.get(i);
				if(vPlantillas==null){
					String plantillas = (String) datosInforme.get("plantillas");

					vPlantillas = this.getPlantillas(plantillas,userBean.getLocation(),userBean);
				}
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,vPlantillas, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesCenso));							

			} 
			Envio envio = getEnvio(form,true,locale, userBean);


			// Genera el envio:
			envio.generarEnvio(idPersona,vDocumentos);



		}else{
			Vector vCampos = this.obtenerDatosFormulario(form);
			String idioma = null;
			String idTipoInforme = null;
			String plantillas = null;
			EnvEnvioProgramadoAdm envioProgramadoAdm  = new EnvEnvioProgramadoAdm(userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;


			envioProgramado = new EnvEnvioProgramadoBean();
			envioProgramado.setIdEnvio(envioProgramadoAdm.getNewIdEnvio(idInstitucion));
			envioProgramado.setIdInstitucion(new Integer(idInstitucion));
			envioProgramado.setIdTipoEnvios(new Integer(form.getIdTipoEnvio()));
			envioProgramado.setIdPlantillaEnvios(Integer.valueOf(form.getIdPlantillaEnvios()));
			if (form.getIdPlantillaGeneracion()!=null && !form.getIdPlantillaGeneracion().equals("")) {
				envioProgramado.setIdPlantilla(Integer.valueOf(form.getIdPlantillaGeneracion()));
			} else {
				envioProgramado.setIdPlantilla(null);
			}

			envioProgramado.setNombre(form.getNombre());
			envioProgramado.setEstado(ClsConstants.DB_FALSE);
			envioProgramado.setFechaProgramada(getFechaProgramada(form.getFechaProgramada(), locale, userBean));

			envioProgramadoAdm.insert(envioProgramado);

			boolean isInformeProgramado = false;

			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable ht = (Hashtable) vCampos.get(i); 
				idPersona = (String) ht.get("idPersona");
				idInstitucion = (String) ht.get("idInstitucion");
				idTipoInforme = (String) ht.get("idTipoInforme");
				String idTipoPersonas = (String) ht.get("idTipoPersonas");
				plantillas = (String) ht.get("plantillas");
				StringBuffer claves = new StringBuffer();
				claves.append("idTipoPersonas==");
				claves.append(idTipoPersonas);
				claves.append("##");


				if(!isInformeProgramado){
					programInformes = new EnvProgramInformesBean();
					//envioProgramado.setProgramInformes(programInformes);
					programInformes.setIdProgram(programInformesAdm.getNewIdProgramInformes(idInstitucion));
					programInformes.setIdEnvio(envioProgramado.getIdEnvio());
					programInformes.setIdInstitucion(envioProgramado.getIdInstitucion());
					//idioma = ((AdmLenguajesBean)admIdioma.getLenguajeInstitucion(idInstitucion)).getIdLenguaje();
					idioma = userBean.getLanguage();
					programInformes.setIdioma(new Integer(idioma));
					programInformes.setEstado(ClsConstants.DB_FALSE);
					programInformes.setClaves(claves.toString());
					programInformes.setPlantillas(plantillas);
					programInformes.setIdTipoInforme(idTipoInforme);

					programInformesAdm.insert(programInformes);
					isInformeProgramado = true;

				}




				destProgramInformes = new EnvDestProgramInformesBean();
				destProgramInformes.setIdProgram(programInformes.getIdProgram());
				destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
				destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
				destProgramInformes.setIdPersona(new Long(idPersona));
				destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));

				destProgramInformesAdm.insert(destProgramInformes);


			}

			Vector vPlantillas = this.getDatosSeparados(plantillas, "@@");
			informesBean = new EnvInformesGenericosBean();
			informesBean.setIdProgram(programInformes.getIdProgram());
			informesBean.setIdEnvio(programInformes.getIdEnvio());
			informesBean.setIdInstitucion(programInformes.getIdInstitucion());


			for (int j = 0; j < vPlantillas.size(); j++) {
				String idPlantillaInforme = (String)vPlantillas.get(j);
				informesBean.setIdPlantilla(idPlantillaInforme);
				informesGenericoAdm.insert(informesBean);
			}

			setEnvioBatch(true);
		}

	}
	public void gestionarComunicacionPagoColegiados(DefinirEnviosForm form,Locale locale, UsrBean userBean)throws ClsExceptions,SIGAException{

		// Obtenemos la información pertinente relacionada
		String idPersona = getIdColegiadoUnico(form);
		String idInstitucion = userBean.getLocation();


		if(idPersona!=null){

			Vector vCampos = this.obtenerDatosFormulario(form);
			Vector vDocumentos = new Vector();
			Vector vPlantillas = null;
			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable datosInforme = (Hashtable) vCampos.get(i);
				if(vPlantillas==null){
					String plantillas = (String) datosInforme.get("plantillas");

					vPlantillas = this.getPlantillas(plantillas,userBean.getLocation(),userBean);
				}
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,vPlantillas, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesPagoColegiados));							

			} 
			Envio envio = getEnvio(form,true,locale, userBean);


			// Genera el envio:
			envio.generarEnvio(idPersona,vDocumentos);



		}else{
			Vector vCampos = this.obtenerDatosFormulario(form);
			String idioma = null;
			String idTipoInforme = null;
			String plantillas = null;
			EnvEnvioProgramadoAdm envioProgramadoAdm  = new EnvEnvioProgramadoAdm(userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;


			envioProgramado = new EnvEnvioProgramadoBean();
			envioProgramado.setIdEnvio(envioProgramadoAdm.getNewIdEnvio(idInstitucion));
			envioProgramado.setIdInstitucion(new Integer(idInstitucion));
			envioProgramado.setIdTipoEnvios(new Integer(form.getIdTipoEnvio()));
			envioProgramado.setIdPlantillaEnvios(Integer.valueOf(form.getIdPlantillaEnvios()));
			if (form.getIdPlantillaGeneracion()!=null && !form.getIdPlantillaGeneracion().equals("")) {
				envioProgramado.setIdPlantilla(Integer.valueOf(form.getIdPlantillaGeneracion()));
			} else {
				envioProgramado.setIdPlantilla(null);
			}

			envioProgramado.setNombre(form.getNombre());
			envioProgramado.setEstado(ClsConstants.DB_FALSE);
			envioProgramado.setFechaProgramada(getFechaProgramada(form.getFechaProgramada(), locale, userBean));

			envioProgramadoAdm.insert(envioProgramado);

			boolean isInformeProgramado = false;

			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable ht = (Hashtable) vCampos.get(i); 
				idPersona = (String) ht.get("idPersona");
				idInstitucion = (String) ht.get("idInstitucion");
				idTipoInforme = (String) ht.get("idTipoInforme");
				plantillas = (String) ht.get("plantillas");
				String idTipoPersonas = (String) ht.get("idPago");
				plantillas = (String) ht.get("plantillas");
				StringBuffer claves = new StringBuffer();
				claves.append("idPago==");
				claves.append(idTipoPersonas);
				claves.append("##");
				


				if(!isInformeProgramado){
					programInformes = new EnvProgramInformesBean();
					//envioProgramado.setProgramInformes(programInformes);
					programInformes.setIdProgram(programInformesAdm.getNewIdProgramInformes(idInstitucion));
					programInformes.setIdEnvio(envioProgramado.getIdEnvio());
					programInformes.setIdInstitucion(envioProgramado.getIdInstitucion());
					//idioma = ((AdmLenguajesBean)admIdioma.getLenguajeInstitucion(idInstitucion)).getIdLenguaje();
					idioma = userBean.getLanguage();
					programInformes.setIdioma(new Integer(idioma));
					programInformes.setEstado(ClsConstants.DB_FALSE);
					programInformes.setPlantillas(plantillas);
					programInformes.setIdTipoInforme(idTipoInforme);
					programInformes.setClaves(claves.toString());

					programInformesAdm.insert(programInformes);
					isInformeProgramado = true;

				}




				destProgramInformes = new EnvDestProgramInformesBean();
				destProgramInformes.setIdProgram(programInformes.getIdProgram());
				destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
				destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
				destProgramInformes.setIdPersona(new Long(idPersona));
				destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));

				destProgramInformesAdm.insert(destProgramInformes);


			}

			Vector vPlantillas = this.getDatosSeparados(plantillas, "@@");
			informesBean = new EnvInformesGenericosBean();
			informesBean.setIdProgram(programInformes.getIdProgram());
			informesBean.setIdEnvio(programInformes.getIdEnvio());
			informesBean.setIdInstitucion(programInformes.getIdInstitucion());


			for (int j = 0; j < vPlantillas.size(); j++) {
				String idPlantillaInforme = (String)vPlantillas.get(j);
				informesBean.setIdPlantilla(idPlantillaInforme);
				informesGenericoAdm.insert(informesBean);
			}

			setEnvioBatch(true);
		}

	}
	/**
	 * Metodo que gestiona las comunicaciones de cobros y recobros(morosos)
	 * @param form
	 * @param isEnvioBatch parametro que sobreescribimos
	 * @param locale
	 * @param userBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */



	public void gestionarComunicacionMorosos(DefinirEnviosForm form, Locale locale, UsrBean userBean)throws ClsExceptions,SIGAException{


		String idPersona = getIdColegiadoUnico(form);
		String idInstitucion = userBean.getLocation();
		
		

		if(idPersona!=null){


			Vector vCampos = this.obtenerDatosFormulario(form);
			String claveIterante = form.getClavesIteracion();
			if(claveIterante!=null){
				vCampos = this.setCamposIterantes(vCampos,claveIterante);
			}



			Vector vDocumentos = new Vector();
			Vector vPlantillas = null;
			ArrayList alFacturas = new ArrayList();;
			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable datosInforme = (Hashtable) vCampos.get(i);


				if(vPlantillas==null){
					String plantillas = (String) datosInforme.get("plantillas");

					vPlantillas = this.getPlantillas(plantillas,userBean.getLocation(),userBean);
				}
				//en el metodo getDatosInformeFinal metemos la key definitiva idFacturas en el datosInforme
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,vPlantillas, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesMorosos));
				alFacturas.addAll((ArrayList)datosInforme.get("idFacturas"));

			} 

			Envio envio = getEnvio(form,true,locale, userBean);

			// Genera el envio:
			envio.generarEnvio(idPersona,vDocumentos);

			envio.generarComunicacionMoroso(idPersona,vDocumentos,alFacturas,idInstitucion,envio.getEnviosBean().getDescripcion());



		}else{
			Vector vCampos = this.obtenerDatosFormulario(form);
			String idioma = null;
			String idTipoInforme = null;
			String plantillas = null;
			EnvEnvioProgramadoAdm envioProgramadoAdm  = new EnvEnvioProgramadoAdm(userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;
			
			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(userBean);

			envioProgramado = new EnvEnvioProgramadoBean();
			envioProgramado.setIdEnvio(envioProgramadoAdm.getNewIdEnvio(idInstitucion));
			envioProgramado.setIdInstitucion(new Integer(idInstitucion));
			envioProgramado.setIdTipoEnvios(new Integer(form.getIdTipoEnvio()));
			envioProgramado.setIdPlantillaEnvios(Integer.valueOf(form.getIdPlantillaEnvios()));
			if (form.getIdPlantillaGeneracion()!=null && !form.getIdPlantillaGeneracion().equals("")) {
				envioProgramado.setIdPlantilla(Integer.valueOf(form.getIdPlantillaGeneracion()));
			} else {
				envioProgramado.setIdPlantilla(null);
			}

			envioProgramado.setNombre(form.getNombre());
			envioProgramado.setEstado(ClsConstants.DB_FALSE);
			envioProgramado.setFechaProgramada(getFechaProgramada(form.getFechaProgramada(), locale, userBean));

			envioProgramadoAdm.insert(envioProgramado);

			boolean isInformeProgramado = false;
			ArrayList alClavesFactura = new ArrayList();
			alClavesFactura.add("idFactura");
			vCampos = this.setCamposIterantes(vCampos,alClavesFactura,"idFacturas");
			

			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable ht = (Hashtable) vCampos.get(i); 
				idPersona = (String) ht.get("idPersona");
				idInstitucion = (String) ht.get("idInstitucion");
				idTipoInforme = (String) ht.get("idTipoInforme");
				ArrayList alFacturas = (ArrayList) ht.get("idFacturas");
				plantillas = (String) ht.get("plantillas");



				if(!isInformeProgramado){
					programInformes = new EnvProgramInformesBean();
					//envioProgramado.setProgramInformes(programInformes);
					programInformes.setIdProgram(programInformesAdm.getNewIdProgramInformes(idInstitucion));
					programInformes.setIdEnvio(envioProgramado.getIdEnvio());
					programInformes.setIdInstitucion(envioProgramado.getIdInstitucion());
					//idioma = ((AdmLenguajesBean)admIdioma.getLenguajeInstitucion(idInstitucion)).getIdLenguaje();
					idioma = userBean.getLanguage();
					programInformes.setIdioma(new Integer(idioma));
					programInformes.setEstado(ClsConstants.DB_FALSE);
					//programInformes.setClaves(claves.toString());
					programInformes.setPlantillas(plantillas);
					programInformes.setIdTipoInforme(idTipoInforme);

					programInformesAdm.insert(programInformes);
					isInformeProgramado = true;

				}


				


				destProgramInformes = new EnvDestProgramInformesBean();
				destProgramInformes.setIdProgram(programInformes.getIdProgram());
				destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
				destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
				destProgramInformes.setIdPersona(new Long(idPersona));
				//destProgramInformes.setClaves(claves.toString());
				destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));
				
				destProgramInformesAdm.insert(destProgramInformes);
				

				EnvValorCampoClaveBean valorCampoClave = null;
				
				for (int j = 0; j < alFacturas.size(); j++) {
					Map htClaves = (Hashtable)alFacturas.get(j);
					Iterator itClave = htClaves.keySet().iterator();
					valorCampoClave = new EnvValorCampoClaveBean();
					valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
					while (itClave.hasNext()) {
						String clave = (String) itClave.next();
						String valorClave = (String)htClaves.get(clave);
						
						//String idFactura2 = (String)alExpedientes.get(j);
						
						valorCampoClave.setIdProgram(destProgramInformes.getIdProgram());
						valorCampoClave.setIdEnvio(destProgramInformes.getIdEnvio());
						valorCampoClave.setIdInstitucion(destProgramInformes.getIdInstitucion());
						valorCampoClave.setIdPersona(destProgramInformes.getIdPersona());
						//destProgramInformes.setClaves(claves.toString());
						valorCampoClave.setIdInstitucionPersona(destProgramInformes.getIdInstitucionPersona());
						
						valorCampoClave.setIdTipoInforme(idTipoInforme);
						valorCampoClave.setClave("idFacturas");
						valorCampoClave.setCampo(clave);
						valorCampoClave.setValor(valorClave);
						valorCampoClaveAdm.insert(valorCampoClave);
						
					}
					
					
					

				}
			}

			Vector vPlantillas = this.getDatosSeparados(plantillas,"@@");
			informesBean = new EnvInformesGenericosBean();
			informesBean.setIdProgram(programInformes.getIdProgram());
			informesBean.setIdEnvio(programInformes.getIdEnvio());
			informesBean.setIdInstitucion(programInformes.getIdInstitucion());


			for (int j = 0; j < vPlantillas.size(); j++) {
				String idPlantillaInforme = (String)vPlantillas.get(j);
				informesBean.setIdPlantilla(idPlantillaInforme);
				informesGenericoAdm.insert(informesBean);
			}
			setEnvioBatch(true);

		}
		//return isEnvioBatch;

	}
	private void setPersonasDesignas(Vector vCampos,UsrBean userBean)throws ClsExceptions,SIGAException{
		ScsDesignaAdm desigAdm = new ScsDesignaAdm(userBean);
		String idTurno  = null;
		String idInstitucion  = null;
		String anio  = null;
		String numero  = null;
		List auxiliar = new ArrayList();
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 

			idInstitucion = (String) ht.get("idInstitucion");
			anio = (String)ht.get("anio");
			idTurno = (String) ht.get("idTurno");
			numero = (String)ht.get("numero");
			String idPersonaDesigna =  desigAdm.getIDLetradoDesig(idInstitucion,idTurno,anio,numero);
			ht.put("idPersona", idPersonaDesigna);
			if(!auxiliar.contains(idPersonaDesigna))
				auxiliar.add(idPersonaDesigna);

		}	

	}
	/**
	 * Metodo que gestiona el envio y comunicacion de Designaciones
	 * @param form
	 * @param locale
	 * @param userBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void gestionarComunicacionDesignas(DefinirEnviosForm form, Locale locale, UsrBean userBean)throws ClsExceptions,SIGAException{



		MasterReport masterReport = new  MasterReport(); 
		Vector vCampos = masterReport.obtenerDatosFormulario(form);
		//Tenemos que obtener el letrado de la designa ya que no se saca en la query del
		//paginador(¡¡¡¡Esta persona se obtiene en la jsp!!!!)
		setPersonasDesignas(vCampos,userBean); 

		String idPersona = getIdColegiadoUnico(vCampos);


		String idInstitucion = userBean.getLocation();


		if(idPersona!=null){



			//String claveIterante = this.getClaveIterante(vCampos);
			//if(claveIterante!=null){
			//vCampos = this.setCamposIterantes(vCampos,"idPersona");
			//}


			Vector vDocumentos = new Vector();
			Vector vPlantillas = null;
			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable datosInforme = (Hashtable) vCampos.get(i);
				if(vPlantillas==null){
					String plantillas = (String) datosInforme.get("plantillas");

					vPlantillas = this.getPlantillas(plantillas,userBean.getLocation(),userBean);
				}
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,vPlantillas, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesDesigna));							

			} 
			Envio envio = getEnvio(form,true,locale, userBean);


			// Genera el envio:
			envio.generarEnvio(idPersona,vDocumentos);

		}else{
			//vCampos = this.obtenerDatosFormulario(form);
			String idioma = null;
			String idTipoInforme = null;
			String plantillas = null;
			EnvEnvioProgramadoAdm envioProgramadoAdm  = new EnvEnvioProgramadoAdm(userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;

			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(userBean);

			envioProgramado = new EnvEnvioProgramadoBean();
			envioProgramado.setIdEnvio(envioProgramadoAdm.getNewIdEnvio(idInstitucion));
			envioProgramado.setIdInstitucion(new Integer(idInstitucion));
			envioProgramado.setIdTipoEnvios(new Integer(form.getIdTipoEnvio()));
			envioProgramado.setIdPlantillaEnvios(Integer.valueOf(form.getIdPlantillaEnvios()));
			if (form.getIdPlantillaGeneracion()!=null && !form.getIdPlantillaGeneracion().equals("")) {
				envioProgramado.setIdPlantilla(Integer.valueOf(form.getIdPlantillaGeneracion()));
			} else {
				envioProgramado.setIdPlantilla(null);
			}

			envioProgramado.setNombre(form.getNombre());
			envioProgramado.setEstado(ClsConstants.DB_FALSE);
			envioProgramado.setFechaProgramada(getFechaProgramada(form.getFechaProgramada(), locale, userBean));

			envioProgramadoAdm.insert(envioProgramado);

			boolean isInformeProgramado = false;
			ArrayList alClavesDesigna = new ArrayList();
			alClavesDesigna.add("idInstitucion");
			alClavesDesigna.add("anio");
			alClavesDesigna.add("idTurno");
			alClavesDesigna.add("numero");
			
//			String claveIterante = form.getClavesIteracion();
//			if(claveIterante!=null){
			vCampos = this.setCamposIterantes(vCampos,alClavesDesigna,"idDesignas");
//			}

			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable ht = (Hashtable) vCampos.get(i); 
				idPersona = (String) ht.get("idPersona");
				idInstitucion = (String) ht.get("idInstitucion");
				idTipoInforme = (String) ht.get("idTipoInforme");
				ArrayList alDesignas = (ArrayList) ht.get("idDesignas");
				plantillas = (String) ht.get("plantillas");



				if(!isInformeProgramado){
					programInformes = new EnvProgramInformesBean();
					//envioProgramado.setProgramInformes(programInformes);
					programInformes.setIdProgram(programInformesAdm.getNewIdProgramInformes(idInstitucion));
					programInformes.setIdEnvio(envioProgramado.getIdEnvio());
					programInformes.setIdInstitucion(envioProgramado.getIdInstitucion());
					//idioma = ((AdmLenguajesBean)admIdioma.getLenguajeInstitucion(idInstitucion)).getIdLenguaje();
					idioma = userBean.getLanguage();
					programInformes.setIdioma(new Integer(idioma));
					programInformes.setEstado(ClsConstants.DB_FALSE);
					//programInformes.setClaves(claves.toString());
					programInformes.setPlantillas(plantillas);
					programInformes.setIdTipoInforme(idTipoInforme);

					programInformesAdm.insert(programInformes);
					isInformeProgramado = true;

				}





				destProgramInformes = new EnvDestProgramInformesBean();
				destProgramInformes.setIdProgram(programInformes.getIdProgram());
				destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
				destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
				destProgramInformes.setIdPersona(new Long(idPersona));
//				destProgramInformes.setClaves(claves.toString());
				destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));

				destProgramInformesAdm.insert(destProgramInformes);
				
				EnvValorCampoClaveBean valorCampoClave = null;
				
				for (int j = 0; j < alDesignas.size(); j++) {
					Map htClaves = (Hashtable)alDesignas.get(j);
					Iterator itClave = htClaves.keySet().iterator();
					valorCampoClave = new EnvValorCampoClaveBean();
					
					valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
					while (itClave.hasNext()) {
						String clave = (String) itClave.next();
						String valorClave = (String)htClaves.get(clave);
						
						//String idFactura2 = (String)alExpedientes.get(j);
						
						valorCampoClave.setIdProgram(destProgramInformes.getIdProgram());
						valorCampoClave.setIdEnvio(destProgramInformes.getIdEnvio());
						valorCampoClave.setIdInstitucion(destProgramInformes.getIdInstitucion());
						valorCampoClave.setIdPersona(destProgramInformes.getIdPersona());
						//destProgramInformes.setClaves(claves.toString());
						valorCampoClave.setIdInstitucionPersona(destProgramInformes.getIdInstitucionPersona());
						
						valorCampoClave.setIdTipoInforme(idTipoInforme);
						valorCampoClave.setClave("idDesignas");
						valorCampoClave.setCampo(clave);
						valorCampoClave.setValor(valorClave);
						valorCampoClaveAdm.insert(valorCampoClave);
						
					}
					
					
					

				}
				
				
				


			}

			Vector vPlantillas = this.getDatosSeparados(plantillas,"@@");
			informesBean = new EnvInformesGenericosBean();
			informesBean.setIdProgram(programInformes.getIdProgram());
			informesBean.setIdEnvio(programInformes.getIdEnvio());
			informesBean.setIdInstitucion(programInformes.getIdInstitucion());


			for (int j = 0; j < vPlantillas.size(); j++) {
				String idPlantillaInforme = (String)vPlantillas.get(j);
				informesBean.setIdPlantilla(idPlantillaInforme);
				informesGenericoAdm.insert(informesBean);
			}
			setEnvioBatch(true);

		}
		//return isEnvioBatch;

	}
	/**
	 * Metodo que gestiona el envio y comunicacion de Expedientes
	 * @param form
	 * @param locale
	 * @param userBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void gestionarComunicacionExpedientes(DefinirEnviosForm form, Locale locale, UsrBean userBean)throws ClsExceptions,SIGAException{



		MasterReport masterReport = new  MasterReport(); 
		Vector vCampos = masterReport.obtenerDatosFormulario(form);
		this.setUsuario(userBean);


		String idPersona = getIdColegiadoUnico(vCampos);


		String idInstitucion = userBean.getLocation();


		if(idPersona!=null){

			////////////////////*************************************************************
			
			Vector vPlantillas = null;
			// recorro los expedientes a enviar
			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable datosInforme = (Hashtable) vCampos.get(i);
				
				// Obtengo los datos del expediente
				String idiomaExt = (String) datosInforme.get("idiomaExt");
				if (idiomaExt == null || idiomaExt.equals(""))
					idiomaExt = userBean.getLanguageExt();
				datosInforme.put("idiomaExt", idiomaExt);
				String idioma = (String) datosInforme.get("idioma");
				if (idioma == null || idioma.equals(""))
					idioma = userBean.getLanguage();
				datosInforme.put("idioma", idioma);
				String idPersona2 = (String) datosInforme.get("idPersona");
				String idInstitucion2 = (String) datosInforme.get("idInstitucion");
				String anio = (String) datosInforme.get("anioExpediente");
				String numero = (String) datosInforme.get("numeroExpediente");
				String idInstitucionTipoExp = (String) datosInforme.get("idInstitucionTipoExp");
				String idTipoExp = (String) datosInforme.get("idTipoExp");

				// Obtengo las plantillas afectadas
				if(vPlantillas==null){
					String plantillas = (String) datosInforme.get("plantillas");
					vPlantillas = this.getPlantillas(plantillas,userBean.getLocation(),userBean);
				}
				// voy a mirar si alguno de los informes es asolicitantes
				boolean aSolicitantes = this.esAlgunaASolicitantes(vPlantillas);
				
				
				// Obtengo los datos de la consulta. 
				// A CADA UNO DE LOS REGISTROS DE ESTA CONSULTA SE LE ENVÍAN TODOS LOS DOCUMENTOS
				// DE LAS PLANTILLAS OBTENIDAS ARRIBA, RESUELTOS PARA EL DESTINATARIO.
				ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm(userBean);
				Vector vDatosInformeFinal = expedienteAdm.getDatosInformeExpediente(idInstitucion2, idInstitucionTipoExp, 
						idTipoExp, anio, numero, idPersona2, true, aSolicitantes );

				// Anotación en cada expediente
				if (vDatosInformeFinal.size()==1) {
					Envio.generarComunicacionExpediente(idInstitucion2,new Integer(idInstitucionTipoExp),
							new Integer(idTipoExp),new Integer(numero),new Integer(anio),idPersona2,userBean);
				} else {
					Envio.generarComunicacionExpediente(idInstitucion2,new Integer(idInstitucionTipoExp),
							new Integer(idTipoExp),new Integer(numero),new Integer(anio),null,userBean);
				}
				// RECORRER LOS DATOSINFORMEFINAL 
				// Por cada destinatario real
				for (int j=0;j<vDatosInformeFinal.size();j++){
					Hashtable datoReal = (Hashtable)vDatosInformeFinal.get(j);
					String idPersonaReal = (String) datoReal.get("IDPERSONA_DEST");
					String idDireccionReal = (String) datoReal.get("IDDIRECCION_DEST");
					datoReal.put("idTipoInforme", "EXP");
					datoReal.put("idPersona", (String) datosInforme.get("idPersona"));
					datoReal.put("idInstitucion", (String) datosInforme.get("idInstitucion"));
					datoReal.put("anioExpediente", (String) datosInforme.get("anioExpediente"));
					datoReal.put("numeroExpediente", (String) datosInforme.get("numeroExpediente"));
					datoReal.put("idInstitucionTipoExp", (String) datosInforme.get("idInstitucionTipoExp"));
					datoReal.put("idTipoExp", (String) datosInforme.get("idTipoExp"));
					datoReal.put("plantillas", (String) datosInforme.get("plantillas"));
					datoReal.put("aSolicitantes", (aSolicitantes)?"S":"N");
					
					if (idPersonaReal!=null && !idPersonaReal.trim().equals("")) {
						//Antes de crear el envío voy a poner el nombre adecuado
						form.setNombre(UtilidadesString.getMensajeIdioma(userBean, "informes.genericos.expedientes.asunto")+ " "+(String) datoReal.get("NOMBRE_DEST"));
						form.setIdPersona(idPersonaReal);
						// Hago un envio
						Envio envio = getEnvio(form,true,locale, userBean);
						// obtengo sus documentos según vPlantillas
												
						Vector vDocumentos = new Vector();
						vDocumentos.addAll(this.getDocumentosAEnviar(datoReal,vPlantillas, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesExpedientes));
						// Genera el envio:
						
						envio.generarEnvioDireccionEspecifica(idPersonaReal, idDireccionReal, vDocumentos);				
					}
				}
				
			}
			////////////////////*************************************************************

				
			// ELIMINADO POR RGG EL DIA 23/10/2009 Y SUSTITUIDO POR LO DE ARRIBA
			/*Vector vDocumentos = new Vector();
			Vector vPlantillas = null;
			ArrayList alExpedientes = new ArrayList();;
			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable datosInforme = (Hashtable) vCampos.get(i);
				if(vPlantillas==null){
					String plantillas = (String) datosInforme.get("plantillas");
					vPlantillas = this.getPlantillas(plantillas,userBean.getLocation(),userBean);
				}
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,vPlantillas, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesExpedientes));
				alExpedientes.add(datosInforme);
			} 
			Envio envio = getEnvio(form,true,locale, userBean);
			*/
			//////////////////////////////////////////////////////////////
			// Esto es solo para dar de alta la anotación en el expediente
			/*for (int i = 0; i < alExpedientes.size(); i++) {
				Hashtable datosExpediente = (Hashtable)alExpedientes.get(i);
				String idInstitucionLocal = (String)datosExpediente.get("idInstitucion");
				String idInstitucionTipoExp = (String)datosExpediente.get("idInstitucionTipoExp");
	    		String idTipoExp = (String)datosExpediente.get("idTipoExp");
				
				String anio = (String)datosExpediente.get("anioExpediente");
				
				String numero = (String)datosExpediente.get("numeroExpediente");
				
				String idPersonaLocal = (String)datosExpediente.get("idPersona");
				
				
				envio.generarComunicacionExpediente(idInstitucionLocal,new Integer(idInstitucionTipoExp)
				,new Integer(idTipoExp),new Integer(numero),new Integer(anio),idPersonaLocal,userBean);
				
			}*/
			//////////////////////////////////////////////////////////////
			// Genera el envio:
			//envio.generarEnvioDireccionEspecifica(idPersona, idDireccion, vDocumentos);

		}else{
			//vCampos = this.obtenerDatosFormulario(form);
			String idioma = null;
			String idTipoInforme = null;
			String plantillas = null;
			EnvEnvioProgramadoAdm envioProgramadoAdm  = new EnvEnvioProgramadoAdm(userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;
			
			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(userBean);


			envioProgramado = new EnvEnvioProgramadoBean();
			envioProgramado.setIdEnvio(envioProgramadoAdm.getNewIdEnvio(idInstitucion));
			envioProgramado.setIdInstitucion(new Integer(idInstitucion));
			envioProgramado.setIdTipoEnvios(new Integer(form.getIdTipoEnvio()));
			envioProgramado.setIdPlantillaEnvios(Integer.valueOf(form.getIdPlantillaEnvios()));
			if (form.getIdPlantillaGeneracion()!=null && !form.getIdPlantillaGeneracion().equals("")) {
				envioProgramado.setIdPlantilla(Integer.valueOf(form.getIdPlantillaGeneracion()));
			} else {
				envioProgramado.setIdPlantilla(null);
			}

			envioProgramado.setNombre(form.getNombre());
			envioProgramado.setEstado(ClsConstants.DB_FALSE);
			envioProgramado.setFechaProgramada(getFechaProgramada(form.getFechaProgramada(), locale, userBean));

			envioProgramadoAdm.insert(envioProgramado);

			boolean isInformeProgramado = false;
			ArrayList alClaves = new ArrayList();
			alClaves.add("idInstitucion");
			alClaves.add("anioExpediente");
			alClaves.add("numeroExpediente");
			alClaves.add("idInstitucionTipoExp");
			alClaves.add("idTipoExp");
			
			
			
//			String claveIterante = form.getClavesIteracion();
//			if(claveIterante!=null){
			vCampos = this.setCamposIterantes(vCampos,alClaves,"idExpedientes");
//			}

			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable ht = (Hashtable) vCampos.get(i); 
				idPersona = (String) ht.get("idPersona");
				idInstitucion = (String) ht.get("idInstitucion");
				idTipoInforme = (String) ht.get("idTipoInforme");
				ArrayList alExpedientes = (ArrayList) ht.get("idExpedientes");
				plantillas = (String) ht.get("plantillas");

				String idPersona2 = (String) ht.get("idPersona");
				String idInstitucion2 = (String) ht.get("idInstitucion");
				String anio = (String) ht.get("anioExpediente");
				String numero = (String) ht.get("numeroExpediente");
				String idInstitucionTipoExp = (String) ht.get("idInstitucionTipoExp");
				String idTipoExp = (String) ht.get("idTipoExp");
				// Anotación en cada expediente
				//Envio.generarComunicacionExpediente(idInstitucion2,new Integer(idInstitucionTipoExp),
				//		new Integer(idTipoExp),new Integer(numero),new Integer(anio),idPersona2,userBean);

				if(!isInformeProgramado){
					programInformes = new EnvProgramInformesBean();
					//envioProgramado.setProgramInformes(programInformes);
					programInformes.setIdProgram(programInformesAdm.getNewIdProgramInformes(idInstitucion));
					programInformes.setIdEnvio(envioProgramado.getIdEnvio());
					programInformes.setIdInstitucion(envioProgramado.getIdInstitucion());
					//idioma = ((AdmLenguajesBean)admIdioma.getLenguajeInstitucion(idInstitucion)).getIdLenguaje();
					idioma = userBean.getLanguage();
					programInformes.setIdioma(new Integer(idioma));
					programInformes.setEstado(ClsConstants.DB_FALSE);
					//programInformes.setClaves(claves.toString());
					programInformes.setPlantillas(plantillas);
					programInformes.setIdTipoInforme(idTipoInforme);

					programInformesAdm.insert(programInformes);
					isInformeProgramado = true;

				}


				destProgramInformes = new EnvDestProgramInformesBean();
				destProgramInformes.setIdProgram(programInformes.getIdProgram());
				destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
				destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
				destProgramInformes.setIdPersona(new Long(idPersona));
				//destProgramInformes.setClaves(claves.toString());
				destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));

				destProgramInformesAdm.insert(destProgramInformes);
				
				EnvValorCampoClaveBean valorCampoClave = null;
				
				for (int j = 0; j < alExpedientes.size(); j++) {
					Map htClaves = (Hashtable)alExpedientes.get(j);
					Iterator itClave = htClaves.keySet().iterator();
					valorCampoClave = new EnvValorCampoClaveBean();
					valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
					while (itClave.hasNext()) {
						String clave = (String) itClave.next();
						String valorClave = (String)htClaves.get(clave);
						
						//String idFactura2 = (String)alExpedientes.get(j);
						
						valorCampoClave.setIdProgram(destProgramInformes.getIdProgram());
						valorCampoClave.setIdEnvio(destProgramInformes.getIdEnvio());
						valorCampoClave.setIdInstitucion(destProgramInformes.getIdInstitucion());
						valorCampoClave.setIdPersona(destProgramInformes.getIdPersona());
						//destProgramInformes.setClaves(claves.toString());
						valorCampoClave.setIdInstitucionPersona(destProgramInformes.getIdInstitucionPersona());
						
						valorCampoClave.setIdTipoInforme(idTipoInforme);
						valorCampoClave.setClave("idExpedientes");
						valorCampoClave.setCampo(clave);
						valorCampoClave.setValor(valorClave);
						valorCampoClaveAdm.insert(valorCampoClave);
						
					}
					
					
					

				}
				


			}

			Vector vPlantillas = this.getDatosSeparados(plantillas,"@@");
			informesBean = new EnvInformesGenericosBean();
			informesBean.setIdProgram(programInformes.getIdProgram());
			informesBean.setIdEnvio(programInformes.getIdEnvio());
			informesBean.setIdInstitucion(programInformes.getIdInstitucion());


			for (int j = 0; j < vPlantillas.size(); j++) {
				String idPlantillaInforme = (String)vPlantillas.get(j);
				informesBean.setIdPlantilla(idPlantillaInforme);
				informesGenericoAdm.insert(informesBean);
			}
			setEnvioBatch(true);

		}
		//return isEnvioBatch;

	}
	public boolean isAlguienEjecutando(){
		synchronized(EnvioInformesGenericos.alguienEjecutando){
			if (!EnvioInformesGenericos.alguienEjecutando){
				EnvioInformesGenericos.alguienEjecutando=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}
	private void setNadieEjecutando(){
		synchronized(EnvioInformesGenericos.alguienEjecutando){
			EnvioInformesGenericos.alguienEjecutando=Boolean.FALSE;
		}
	}
	private boolean isAlgunaEjecucionDenegada(){
		synchronized(EnvioInformesGenericos.algunaEjecucionDenegada){
			if (!EnvioInformesGenericos.algunaEjecucionDenegada){
				EnvioInformesGenericos.algunaEjecucionDenegada=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}

	private void setNingunaEjecucionDenegada(){
		synchronized(EnvioInformesGenericos.algunaEjecucionDenegada){
			EnvioInformesGenericos.algunaEjecucionDenegada=Boolean.FALSE;
		}
	}
	private void setAlgunaEjecucionDenegada(){
		synchronized(EnvioInformesGenericos.algunaEjecucionDenegada){
			EnvioInformesGenericos.algunaEjecucionDenegada=Boolean.TRUE;
		}
	}


	public void procesarAutomaticamenteGeneracionEnvios()throws Exception{
		if (isAlguienEjecutando()){
			ClsLogging.writeFileLogWithoutSession("YA SE ESTA EJECUTANDO LA GENERACION DE ENVIOS EN BACKGROUND. CUANDO TERMINE SE INICIARA OTRA VEZ EL PROCESO.", 3);
			//ClsLogging.writeFileLogError("gratuita.eejg.message.isAlguienEjecutando",new SchedulerException("gratuita.eejg.message.isAlguienEjecutando"), 3);
			setAlgunaEjecucionDenegada();
			return;
		}

		try {
			procesoAutomaticoGeneracionEnvios();

		} catch(Exception e){
			throw e;
		}
		finally {
			setNadieEjecutando();
			if(isAlgunaEjecucionDenegada()){
				setNingunaEjecucionDenegada();
				procesarAutomaticamenteGeneracionEnvios();

			}
		}
	}



	private void procesoAutomaticoGeneracionEnvios() throws ClsExceptions{
		EnvProgramIRPFAdm admProgramiRPF = new EnvProgramIRPFAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor
		// Sacamos los datos de los datos programados pendientes(ClsConstants.DB_FALSE) de
		//todas las instituciones(null)
		Vector vCertificadosIRPFProgramados = admProgramiRPF.getCertificadosIRPFProgramados(ClsConstants.DB_FALSE, null);

		InformeCertificadoIRPF informeCertificadoIRPF = null;
		if (vCertificadosIRPFProgramados!=null && vCertificadosIRPFProgramados.size()>0)
		{
			ClsLogging.writeFileLogWithoutSession(" ---------- ENVIOS PROGRAMADOS DE CERTIFICADOS IRPF PENDIENTES:"+vCertificadosIRPFProgramados.size(), 3);
			EnvProgramIRPFBean programIRPFBean = null;
			informeCertificadoIRPF = new InformeCertificadoIRPF();
			for (int i=0; i<vCertificadosIRPFProgramados.size(); i++)
			{

				try {
					programIRPFBean = (EnvProgramIRPFBean)vCertificadosIRPFProgramados.get(i);
					UsrBean usr = UsrBean.UsrBeanAutomatico(programIRPFBean.getIdInstitucion().toString());
					informeCertificadoIRPF.enviarCertificadoIRPFColegiado(usr, programIRPFBean, programIRPFBean.getEnvioProgramado());

					programIRPFBean.setOriginalHash(admProgramiRPF.beanToHashTable(programIRPFBean));
					programIRPFBean.setEstado(ClsConstants.DB_TRUE);

					admProgramiRPF.update(programIRPFBean);

					ClsLogging.writeFileLogWithoutSession(" ---------- OK ENVIO DE CERTIFICADOS IRPF PENDIENTES IDPERSONA: "+programIRPFBean.getIdPersona(), 3);

				} catch (Exception e) {
					if(programIRPFBean != null && programIRPFBean.getIdInstitucion()!=null){
						ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES IDINSTITUCION: "+programIRPFBean.getIdInstitucion(), 3);
						if(programIRPFBean.getIdPersona()!=null)
							ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES IDPERSONA: "+programIRPFBean.getIdPersona(), 3);
					}
					else
						ClsLogging.writeFileLogWithoutSession(" ---------- ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES.", 3);
				}
			}
		}

		ClsLogging.writeFileLogWithoutSession(" FIN ENVIOS PROGRAMADOS DE CERTIFICADOS IRPF A COLEGIADOS ", 3);


		ClsLogging.writeFileLogWithoutSession(" ---------- INICIO ENVIOS PROGRAMADOS DE INFORMES GENERICOS ", 3);
		EnvProgramInformesAdm admProgramInfGenericos = new EnvProgramInformesAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor
		// Sacamos los datos de los datos programados pendientes(ClsConstants.DB_FALSE) de
		//todas las instituciones(null)
		Vector vInfGenericosProgramados = admProgramInfGenericos.getInformesGenericosProgramados(ClsConstants.DB_FALSE, null);

		EnvioInformesGenericos envioInformeGenerico = new EnvioInformesGenericos();
		if (vInfGenericosProgramados!=null && vInfGenericosProgramados.size()>0)
		{
			ClsLogging.writeFileLogWithoutSession(" ---------- ENVIOS PROGRAMADOS DE INFORMES GENERICOS PENDIENTES:"+vInfGenericosProgramados.size(), 3);
			EnvProgramInformesBean programInfGenericoBean =  null;
			EnvDestProgramInformesAdm admDestProgram =   new EnvDestProgramInformesAdm(new UsrBean());
			EnvInformesGenericosAdm admInformesGenericos =   new EnvInformesGenericosAdm(new UsrBean());
			for (int i=0; i<vInfGenericosProgramados.size(); i++)
			{

				try {
					programInfGenericoBean = (EnvProgramInformesBean)vInfGenericosProgramados.get(i);

					Integer idTipoEnvio = programInfGenericoBean.getEnvioProgramado().getIdTipoEnvios();
					if(idTipoEnvio.toString().equals(EnvTipoEnviosAdm.K_CORREO_ORDINARIO)){

						UsrBean usr = UsrBean.UsrBeanAutomatico(programInfGenericoBean.getIdInstitucion().toString());
						Vector vDestinatarios = admDestProgram.getDestinatariosInformesGenericosProgramados(programInfGenericoBean);
						Vector vPlantillas = admInformesGenericos.getPlantillasInformesGenericosProgramados(programInfGenericoBean);

						try {
							envioInformeGenerico.enviarInformeGenericoOrdinario(usr, vDestinatarios,  programInfGenericoBean, vPlantillas, programInfGenericoBean.getEnvioProgramado());
						} catch (Exception e) {
							//if(destProgramInfBean.getIdPersona()!=null)
							ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES CORREO ORDINARIO: ", 3);
						}






					}else{							

						UsrBean usr = UsrBean.UsrBeanAutomatico(programInfGenericoBean.getIdInstitucion().toString());
						Vector vDestinatarios = admDestProgram.getDestinatariosInformesGenericosProgramados(programInfGenericoBean);
						Vector vPlantillas = admInformesGenericos.getPlantillasInformesGenericosProgramados(programInfGenericoBean);
						for (int j = 0; j < vDestinatarios.size(); j++) {
							EnvDestProgramInformesBean destProgramInfBean = (EnvDestProgramInformesBean)vDestinatarios.get(j);
							try {
								envioInformeGenerico.enviarInformeGenerico(usr, destProgramInfBean,  programInfGenericoBean, vPlantillas, programInfGenericoBean.getEnvioProgramado());
							} catch (Exception e) {
								if(destProgramInfBean.getIdPersona()!=null)
									ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES IDPERSONA: "+destProgramInfBean.getIdPersona() + " "+e.toString(), 3);
							}
						}

					}



					programInfGenericoBean.setOriginalHash(admProgramInfGenericos.beanToHashTable(programInfGenericoBean));
					programInfGenericoBean.setEstado(ClsConstants.DB_TRUE);

					admProgramInfGenericos.update(programInfGenericoBean);

					ClsLogging.writeFileLogWithoutSession(" ---------- OK ENVIO DE INFORMES GENERICOS PENDIENTES. TIPO DE INFORME : "+programInfGenericoBean.getIdTipoInforme(), 3);

				} catch (Exception e) {
					if(programInfGenericoBean != null && programInfGenericoBean.getIdInstitucion()!=null){
						ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES. TIPO DE INFORME: "+programInfGenericoBean.getIdTipoInforme() + " "+e.toString(), 3);

					}
					else
						ClsLogging.writeFileLogWithoutSession(" ---------- ERROR ENVIO DE INFORMES GENERICOS PENDIENTESS." + " "+e.toString(), 3);
				}
			}
		}

		ClsLogging.writeFileLogWithoutSession(" FIN ENVIOS PROGRAMADOS DE INFORMES GENERICOS ", 3);

	}



}
class ComunicacionMoroso{
	public ComunicacionMoroso()
    {        
        	    
    }
	Long idPersona;
	Vector documentos;
	ArrayList facturas;
	Integer idInstitucion;
	String descripcion;
	
	public Vector getDocumentos() {
		return documentos;
	}
	public void setDocumentos(Vector documentos) {
		this.documentos = documentos;
	}

	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	public ArrayList getFacturas() {
		return facturas;
	}
	public void setFacturas(ArrayList facturas) {
		this.facturas = facturas;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}