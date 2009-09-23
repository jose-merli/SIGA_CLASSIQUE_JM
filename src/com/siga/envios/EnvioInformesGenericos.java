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
import com.siga.beans.EnvDestProgramInformesAdm;
import com.siga.beans.EnvDestProgramInformesBean;
import com.siga.beans.EnvEnvioProgramadoAdm;
import com.siga.beans.EnvEnvioProgramadoBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvInformesGenericosAdm;
import com.siga.beans.EnvInformesGenericosBean;
import com.siga.beans.EnvProgramInformesAdm;
import com.siga.beans.EnvProgramInformesBean;
import com.siga.beans.EnvValorCampoClaveAdm;
import com.siga.beans.EnvValorCampoClaveBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.certificados.Plantilla;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.general.SIGAException;
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
	public static final int docFile = 1;
	public static final int docDocument = 2;
	public static final String comunicacionesCenso = "CENSO";
	public static final String comunicacionesMorosos = "COBRO";
	public static final String comunicacionesDesigna = "OFICI";
	public static final String comunicacionesExpedientes = "EXP";
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

		} else if (idTipoInforme
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
					null, false, true);

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

			htCabeceraInforme.put("FECHA", sHoy);

			double importeTotal = 0;
			double deudaTotal = 0;
			for (int j = 0; j < vDatosInforme.size(); j++) {
				Hashtable fila = (Hashtable) vDatosInforme.get(j);
				double importe = Double.parseDouble((String) fila.get("TOTAL"));
				importeTotal += importe;
				double deuda = Double.parseDouble((String) fila.get("DEUDA"));
				deudaTotal += deuda;

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
			
			String anio = (String)datosInforme.get("anioExpediente");
			
			String numero = (String)datosInforme.get("numeroExpediente");
			
			String idPersona = (String)datosInforme.get("idPersona");
			
			
			ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm (usrBean);
			Vector vDatosInformeFinal = expedienteAdm.getDatosInformeExpediente(idInstitucion, idInstitucionTipoExp, 
					idTipoExp, anio, numero, idPersona, true);

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

		if(!tipoComunicacion.equals(EnvioInformesGenericos.comunicacionesDesigna))
			htDatosInformeFinal = getDatosInformeFinal(datosInforme,
					usrBean);
		
		Hashtable hashConsultasHechas = new Hashtable();
		String hoy = UtilidadesString.formatoFecha(new Date(),"yyyyMMddhhmmss");
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
					identificador.append(hoy);
					

					
					
					File fileDocumento = getInformeGenerico(beanInforme,
							htDatosInforme, idiomaExt, identificador.toString(), usrBean);
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
					String anio = (String) datosInforme.get("anioExpediente");
					String numero = (String) datosInforme.get("numeroExpediente");
					String idInstitucionTipoExp = (String) datosInforme.get("idInstitucionTipoExp");
					String idTipoExp = (String) datosInforme.get("idTipoExp");
					identificador = new StringBuffer();
					identificador.append(idInstitucion);
					identificador.append("_");
					identificador.append(idPersona);
					identificador.append("_");
					identificador.append(anio);
					identificador.append("_");
					identificador.append(numero);
					identificador.append("_");
					identificador.append(idTipoExp);
					identificador.append("_");
					identificador.append(idInstitucionTipoExp);
					
					
					
				}
				File fileDocumento = getInformeGenerico(beanInforme,
						htDatosInformeFinal, idiomaExt, identificador.toString(), usrBean);
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
			
			datosInforme.putAll(htClavesProgramacion);
			if(alClavesDestinatario==null){
				//datosInforme.putAll(htClavesDestinatario);
				vDocumentos = getDocumentosAEnviar(datosInforme,
						vPlantillasInforme, usrBean,
						EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme());
			}else{
				
				
				vDocumentos = new Vector();
	
				for (int i = 0; i < alClavesDestinatario.size(); i++) {
					Hashtable  htClaves =   (Hashtable) alClavesDestinatario.get(i);
					datosInforme.putAll(htClaves);
					vDocumentos.addAll(getDocumentosAEnviar(datosInforme,
							vPlantillasInforme, usrBean,
							EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme()));
					
				}
			}
			
			
			
			

			
			//en el metodo getDatosInformeFinal metemos la key definitiva idFacturas en el datosInforme
			ArrayList alFacturas = ((ArrayList)datosInforme.get("idFacturas"));
			if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
				envio.generarComunicacionMoroso(destProgramInfBean.getIdPersona().toString(),
						vDocumentos,alFacturas,programInfBean.getIdInstitucion().toString(),
						enviosBean.getDescripcion());
			}


			if (htPersonas.containsKey(idPersona)) {
				Vector vAuxDocumentos = (Vector) htPersonas.get(idPersona);
				vDocumentos.addAll(vAuxDocumentos);

			}
			htPersonas.put(idPersona, vDocumentos);

		}

		envio.generarEnvioOrdinario(envio.getEnviosBean(), htPersonas);


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
		Hashtable datosInforme = new Hashtable();

		Hashtable htClavesProgramacion = getClaves(programInfBean.getClaves());

		
		ArrayList alClavesDestinatario = destProgramInfBean.getClavesDestinatario();
		



		datosInforme.put("idioma", programInfBean.getIdioma().toString());
		datosInforme.put("idiomaExt", programInfBean.getIdiomaCodigoExt());
		datosInforme.put("idPersona", destProgramInfBean.getIdPersona()
				.toString());
		datosInforme.put("idInstitucion", destProgramInfBean
				.getIdInstitucionPersona().toString());
		datosInforme.put("idTipoInforme", programInfBean.getIdTipoInforme());


		// (JTA) IDEA!!!!! Ahora mismo el idioma de las comunicaciones es el de el usuario que la genera. si por necesidades se va a meter
		// en algun formulario el idioma seleccionable, se puede meter como clave de la
		// programacion. de este modo el idioma inicial se macahacara con este ultimo, por eso es
		// importante que el putAll este aqui y no antes.
		Vector vDocumentos = null;
		
		datosInforme.putAll(htClavesProgramacion);
		if(alClavesDestinatario==null){
			//datosInforme.putAll(htClavesDestinatario);
			vDocumentos = getDocumentosAEnviar(datosInforme,
					vPlantillasInforme, usrBean, EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme());
		}else{
			//List aClavesMultiple = (ArrayList)htClavesDestinatario.get("clavesMultiple");
			//datosInforme.putAll(htClavesDestinatario);
			vDocumentos = new Vector();
			for (int i = 0; i < alClavesDestinatario.size(); i++) {
				Hashtable  htClaves =   (Hashtable) alClavesDestinatario.get(i);
				datosInforme.putAll(htClaves);
				vDocumentos.addAll(getDocumentosAEnviar(datosInforme,vPlantillasInforme, usrBean,
						EnvioInformesGenericos.docDocument,programInfBean.getIdTipoInforme()));
				
			}
		}
		

		

		// Genera el envio:
		envio.generarEnvio(destProgramInfBean.getIdPersona().toString(),
				vDocumentos);
		//Generamos la comunicacion de morosos
		if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesMorosos)){
			ArrayList alFacturas = (ArrayList)datosInforme.get("idFacturas");
			envio.generarComunicacionMoroso(destProgramInfBean.getIdPersona().toString(),
					vDocumentos,alFacturas,programInfBean.getIdInstitucion().toString(),enviosBean.getDescripcion());
		}
		//Generamos la comunicacion de morosos
		if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesExpedientes)){
			String idInstitucion = (String)datosInforme.get("idInstitucion");
			String idInstitucionTipoExp = (String)datosInforme.get("idInstitucionTipoExp");
    		String idTipoExp = (String)datosInforme.get("idTipoExp");
			
			String anio = (String)datosInforme.get("anioExpediente");
			
			String numero = (String)datosInforme.get("numeroExpediente");
			
			String idPersona = (String)datosInforme.get("idPersona");
			
			
			envio.generarComunicacionExpediente(idInstitucion,new Integer(idInstitucionTipoExp)
			,new Integer(idTipoExp),new Integer(numero),new Integer(anio),idPersona,usrBean);
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
	 * 
	 * @param informeGenerico
	 * @param usr
	 * @param isEnviar
	 * @return
	 * @throws SIGAException
	 * @throws Exception
	 */

	public File getInformeGenerico(InformesGenericosForm informeGenerico,
			UsrBean usr, boolean isEnviar) throws SIGAException, Exception {

		File ficheroSalida = null;

		Vector informesRes = new Vector();
		// Obtiene del campo idInforme los ids separados por ## y devuelve sus
		// beans
		String datosAEnviar = null;
		// Carga en el doc los datos comunes del informe (Institucion,idfacturacion,fecha)
		if (informeGenerico.getDatosInforme() != null
				&& !informeGenerico.getDatosInforme().trim().equals("")) {

			// Obtiene del campo datosInforme los campos del formulario primcipal
			// para obtener la clave para el informe. Los datos se obtienen en
			// una cadena como los ocultos y se sirven como un vector de hashtables por si se trata de datos
			// multiregistro.

			//No prepara los datos necesarios para el envio de informes
			if (isEnviar) {
				Vector vPlantillas = getDatosSeparados(informeGenerico.getIdInforme(),"##");

				datosAEnviar = getDatosAEnviar(informeGenerico, vPlantillas);

				//Caso de escarga de informes
			} else {

				Vector datosFormulario = this.obtenerDatosFormulario(informeGenerico);

				//En el caso de que la iteracion de los datos del informe(tablas 1-->n),
				//la clave iterante sera la clave por la que iteraran las personas. 
				//Este sera el caso de que los docuemntos tengan regiones
				String claveIterante = informeGenerico.getClavesIteracion();
				if (claveIterante != null && !claveIterante.equals("")) {
					datosFormulario = setCamposIterantes(datosFormulario,
							claveIterante);
				}


				Vector vPlantillas = null;
				for (int j = 0; j < datosFormulario.size(); j++) {
					Hashtable datosInforme = (Hashtable) datosFormulario.get(j);
					String idTipoInforme = (String) datosInforme.get("idTipoInforme");
					if (datosInforme != null) {
						if (vPlantillas == null) {
							String plantillas = (String) datosInforme
							.get("plantillas");

							vPlantillas = getPlantillas(plantillas, usr
									.getLocation(), usr);
						}

						Vector vDocumentos = getDocumentosAEnviar(datosInforme,
								vPlantillas, usr,
								EnvioInformesGenericos.docFile,idTipoInforme);

						informesRes.addAll(vDocumentos);

					}
				}

			}

		} else {
			throw new SIGAException("messages.informes.ningunInformeGenerado");

		}
		//Seteamos los daros que se utilizaran en DefinirenviosAction
		if (isEnviar) {
			setDatosEnvios(datosAEnviar.toString());

		} else {
			// Si no es enviar es que va a generar el fichero luego lo comprimimos en un zip
			if (informesRes.size() != 0) {
				if (informesRes.size() < 2) {
					ficheroSalida = (File) informesRes.get(0);
				} else {
					AdmTipoInformeAdm admT = new AdmTipoInformeAdm(usr);
					AdmTipoInformeBean beanT = admT
					.obtenerTipoInforme(informeGenerico
							.getIdTipoInforme());
					ArrayList ficherosPDF = new ArrayList();
					for (int i = 0; i < informesRes.size(); i++) {
						File f = (File) informesRes.get(i);
						ficherosPDF.add(f);
					}

					String nombreFicheroZIP = beanT.getDescripcion().trim()
					+ "_"
					+ UtilidadesBDAdm.getFechaCompletaBD("")
					.replaceAll("/", "").replaceAll(":", "")
					.replaceAll(" ", "");
					ReadProperties rp = new ReadProperties("SIGA.properties");
					String rutaServidorDescargasZip = rp
					.returnProperty("informes.directorioFisicoSalidaInformesJava")
					+ rp
					.returnProperty("informes.directorioPlantillaInformesJava");
					rutaServidorDescargasZip += ClsConstants.FILE_SEP
					+ informeGenerico.getIdInstitucion()
					+ ClsConstants.FILE_SEP + "temp"
					+ File.separatorChar;
					File ruta = new File(rutaServidorDescargasZip);
					ruta.mkdirs();
					Plantilla.doZip(rutaServidorDescargasZip, nombreFicheroZIP,
							ficherosPDF);
					ficheroSalida = new File(rutaServidorDescargasZip
							+ nombreFicheroZIP + ".zip");
				}

			} else
				throw new SIGAException(
				"messages.general.error.ficheroNoExiste");
		}

		return ficheroSalida;

	}
	/**
	 * Metodo que nos encuentra en caso de que hubiera la clave por la que luego se
	 * tendra que iterar([idpersona=1,idFactura=22],[idPersona=1,idFactura=33] la clave iterante sera idFactura)
	 *  
	 * @param vCampos
	 * @return
	 * @throws ClsExceptions
	 */
//	public String getClaveIterante(Vector vCampos) throws ClsExceptions {

//	String claveIterante = null;
//	Hashtable htAuxiliar = new Hashtable();

//	boolean findIt = false;
//	List lAux = new ArrayList();

//	for (int i = 0; i < vCampos.size(); i++) {
//	Hashtable ht = (Hashtable) vCampos.get(i);
//	Iterator itCampos = ht.keySet().iterator();
//	while (itCampos.hasNext()) {
//	String clave = (String) itCampos.next();
//	String value = clave + "||" + ht.get(clave);
//	if (i == 0) {
//	htAuxiliar.put(value, value);
//	} else {
//	if (!htAuxiliar.contains(value)) {
//	claveIterante = clave;
//	findIt = true;

//	//break;
//	}else{
//	lAux.add(value);
//	}
//	}

//	}


//	}
//	return claveIterante;

//	}
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
	private File getInformeGenerico(AdmInformeBean beanInforme,
			Hashtable htDatosInforme, String idiomaExt, String identificador,
			UsrBean usr) throws SIGAException, ClsExceptions {

		Date inicio = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis()
				+ ",==> SIGA: INICIO InformesGenericos.getInformeGenerico", 10);

		File ficheroSalida = null;
		// --- acceso a paths y nombres
		ReadProperties rp = new ReadProperties("SIGA.properties");
		String rutaPlantilla = rp
		.returnProperty("informes.directorioFisicoPlantillaInformesJava")
		+ rp.returnProperty("informes.directorioPlantillaInformesJava");
		String rutaAlmacen = rp
		.returnProperty("informes.directorioFisicoSalidaInformesJava")
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


		enviosBean.setIdInstitucion(Integer.valueOf(idInstitucion));

		if(isEnvioUnico){
			//String idEnvio = form.getIdEnvio();
			enviosBean.setIdEnvio(Integer.valueOf(form.getIdEnvio()));
		}


		enviosBean.setDescripcion(nombreEnvio);
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


		enviosBean.setDescripcion(enviosBean.getIdEnvio()+" "+enviosBean.getDescripcion());
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
		//Tenemos que obtener el letrado de la designa ya que no se saca en la query del
		//paginador(¡¡¡¡Esta persona se obtiene en la jsp!!!!)


		String idPersona = getIdColegiadoUnico(vCampos);


		String idInstitucion = userBean.getLocation();


		if(idPersona!=null){



			//String claveIterante = this.getClaveIterante(vCampos);
			//if(claveIterante!=null){
			//vCampos = this.setCamposIterantes(vCampos,"idPersona");
			//}


			Vector vDocumentos = new Vector();
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
			
			for (int i = 0; i < alExpedientes.size(); i++) {
				Hashtable datosExpediente = (Hashtable)alExpedientes.get(i);
				String idInstitucionLocal = (String)datosExpediente.get("idInstitucion");
				String idInstitucionTipoExp = (String)datosExpediente.get("idInstitucionTipoExp");
	    		String idTipoExp = (String)datosExpediente.get("idTipoExp");
				
				String anio = (String)datosExpediente.get("anioExpediente");
				
				String numero = (String)datosExpediente.get("numeroExpediente");
				
				String idPersonaLocal = (String)datosExpediente.get("idPersona");
				
				
				envio.generarComunicacionExpediente(idInstitucionLocal,new Integer(idInstitucionTipoExp)
				,new Integer(idTipoExp),new Integer(numero),new Integer(anio),idPersonaLocal,userBean);
				
			}
			
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



}