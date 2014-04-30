package com.siga.envios;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.redabogacia.sigaservices.app.AppConstants.TipoIntercambioEnum;
import org.redabogacia.sigaservices.app.autogen.model.EcomComunicacionresolucionajgWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.EcomDesignaprovisionalWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.EcomSolimpugresolucionajgWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.EcomSolsusprocedimientoWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.ScsDesigna;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjg;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenAnexosCuentasBancariasAdm;
import com.siga.beans.CenAnexosCuentasBancariasBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDatosCVAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenMandatosCuentasBancariasAdm;
import com.siga.beans.CenMandatosCuentasBancariasBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenPoblacionesAdm;
import com.siga.beans.CenProvinciaAdm;
import com.siga.beans.EnvDestProgramInformesAdm;
import com.siga.beans.EnvDestProgramInformesBean;
import com.siga.beans.EnvDestinatariosBean;
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
import com.siga.beans.EnvValorCampoClaveAdm;
import com.siga.beans.EnvValorCampoClaveBean;
import com.siga.beans.ExpAlertaAdm;
import com.siga.beans.ExpAlertaBean;
import com.siga.beans.ExpDenunciadoAdm;
import com.siga.beans.ExpDenunciadoBean;
import com.siga.beans.ExpDenuncianteAdm;
import com.siga.beans.ExpDenuncianteBean;
import com.siga.beans.ExpDestinatariosAvisosAdm;
import com.siga.beans.ExpDestinatariosAvisosBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpFasesAdm;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpPartesAdm;
import com.siga.beans.ExpPartesBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.HelperInformesAdm;
import com.siga.beans.ScsActaComisionAdm;
import com.siga.beans.ScsContrariosEJGAdm;
import com.siga.beans.ScsContrariosEJGBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.beans.ScsDesignasLetradoBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsInclusionGuardiasEnListasAdm;
import com.siga.beans.ScsInclusionGuardiasEnListasBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsListaGuardiasAdm;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsProcuradorAdm;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.certificados.Plantilla;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AcreditacionForm;
import com.siga.gratuita.form.ActuacionDesignaForm;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.form.DesignaForm;
import com.siga.gratuita.form.InformeJustificacionMasivaForm;
import com.siga.informes.InformeAbono;
import com.siga.informes.InformeCertificadoIRPF;
import com.siga.informes.InformeColegiadosFacturaciones;
import com.siga.informes.InformeColegiadosPagos;
import com.siga.informes.InformeFacturasEmitidas;
import com.siga.informes.MasterReport;
import com.siga.informes.MasterWords;
import com.siga.informes.form.InformesGenericosForm;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;

/**
 * 
 * Se crea esta clase para gestionar las comunicaciones masivas Esta intimamente
 * relacionada con DefinirEnviosAction e InformesGenercosAction.
 * 
 * 
 * @author jorgeta
 * 
 */
public class EnvioInformesGenericos extends MasterReport {

	private static Boolean alguienEjecutando = Boolean.FALSE;
	private static Boolean algunaEjecucionDenegada = Boolean.FALSE;

	public static final int docFile = 1;
	public static final int docDocument = 2;
	public static final String comunicacionesEjg = "EJG";
	public static final String comunicacionesCAJG = "CAJG";
	public static final String comunicacionesDesigna = "OFICI";
	public static final String comunicacionesMorosos = "COBRO";
	public static final String comunicacionesCenso = "CENSO";
	public static final String comunicacionesFacturacionesColegiados = "CFACT";
	public static final String comunicacionesPagoColegiados = "CPAGO";
	public static final String comunicacionesJustificacion = "JUSDE";
	public static final String comunicacionesExpedientes = "EXP";
	public static final String comunicacionesListadoGuardias = "LIGUA";
	public static final String comunicacionesActaComision = "ACTAC";
	public static final String comunicacionesAvisoExpedientes = "EXPAV";
	public static final String comunicacionesIRPF = "IRPF";
	public static final String comunicacionesFacturaRectificativa = "ABONO";
	public static final String comunicacionesResolucionEjg = "REJG";
	public static final String comunicacionesOrdenDomicializacion = "OSEPA";
	public static final String comunicacionesAnexoOrdenDomiciliacion = "ASEPA";
	
	public static final int tipoPlantillaWord = 1;
	public static final int tipoPlantillaFo = 2;
	
	private String datosEnvios;
	boolean envioBatch = false;
	
	boolean algunRepresentanteLegal = false;
	boolean algunInformeNoGenerado = false;
	
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
	 * En este metodo vamos ha invocar el acceso a datos de cada uno de los
	 * informes que se van a generar. El hashtable que devuleve tendra dos
	 * elemento (row y region). region(
	 * masterWord.sustituyeRegionDocumento)-->El elemento es un vector de
	 * hastable cuyas claves seran los nombres de la region. row
	 * (masterWord.sustituyeRegionDocumento) --> Sera el vector de datos
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

		// Velores comunes para la obttencion de los datos del informe
		String idTipoInforme = (String) datosInforme.get("idTipoInforme");
		String idioma = (String) datosInforme.get("idioma");

		if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesCenso)) {

			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(usrBean);
			CenNoColegiadoAdm noColegiadoAdm = new CenNoColegiadoAdm(usrBean);
			CenDatosCVAdm datosCVAdm = new CenDatosCVAdm(usrBean);
			String idTipoPersonas = (String) datosInforme.get("idTipoPersonas");
			String idPersona = (String) datosInforme.get("idPersona");
			String idInstitucion = (String) datosInforme.get("idInstitucion");

			Vector vDatosInformeFinal = null;
			Hashtable total=null;
			switch (Integer.parseInt(idTipoPersonas)) {

			case 0: // Caso de No Colegiados
				total = noColegiadoAdm.getInformeNoColegiadoInforme(
						idInstitucion, idPersona, idioma, true);
				break;

			case 1: // Caso de colegiados
				
				vDatosInformeFinal = colegiadoAdm.getInformeColegiado(idInstitucion, idPersona, idioma, true, htDatosInforme);	
				break;

			case 2: // Caso de letrados
				vDatosInformeFinal = colegiadoAdm.getInformeLetrado(
						idInstitucion, idPersona, idioma, true);
				break;

			}
			
			if (htDatosInforme.size()==0) {
				Vector vEstadosColegiales = new Vector();
				
				Hashtable registroEstadosColegiales = new Hashtable();
				registroEstadosColegiales.put("ESTADO_DESCRIPCION", "");
				registroEstadosColegiales.put("ESTADO_FECHA", "");
				registroEstadosColegiales.put("ESTADO_OBSERVACIONES", "");
				vEstadosColegiales.add(registroEstadosColegiales);	
				
				htDatosInforme.put("EstadosColegiales", vEstadosColegiales);	
			}		

			datosCVAdm.updateInformeDatosCV(usrBean,
					Integer.parseInt(idInstitucion), Long.parseLong(idPersona),
					htDatosInforme);
			
			//Si no hubiese componenentes
			Hashtable registro = new Hashtable();
			registro.put("NIF_COMPONENTE", "");
			registro.put("NOMBRE_COMPONENTE", "");
			registro.put("CARGO", "");
			registro.put("FECHACARGO", "");
			registro.put("EJERCIENTE", "");
			registro.put("PARTICIPACION_SOCIEDAD_%", "");
			registro.put("NIFCIF","");
			registro.put("NOMBRE","");
			registro.put("APELLIDOS1","");
			registro.put("APELLIDOS2","");
			registro.put("FECHACARGOINFORME","");
			registro.put("SOCIEDAD","");
			Vector vInformeCompAux = new Vector();
			vInformeCompAux.add(registro);
			
			if(total!=null) {
				if(total.size()>0 && total.get("vInformeComp") != null)
					htDatosInforme.put("componentes", total.get("vInformeComp"));
				else
					htDatosInforme.put("componentes", vInformeCompAux);
				htDatosInforme.put("row", total.get("vInforme"));
			} else{		
				htDatosInforme.put("row", vDatosInformeFinal);
				htDatosInforme.put("componentes",vInformeCompAux);
			}

		} else if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesPagoColegiados)) {
			InformeColegiadosPagos infColegiado = new InformeColegiadosPagos();
			datosInforme = infColegiado.getDatosInformeColegiado(usrBean, datosInforme);
			htDatosInforme.put("row", datosInforme);

		} else if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesFacturaRectificativa)) {
			InformeAbono infColegiado = new InformeAbono();
			datosInforme = infColegiado.getDatosInformeAbono(usrBean,datosInforme);
			htDatosInforme.put("row", datosInforme);

		} else if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesFacturacionesColegiados)) {
			InformeColegiadosFacturaciones infColegiado = new InformeColegiadosFacturaciones();
			datosInforme = infColegiado.getDatosInformeColegiado(usrBean,datosInforme);
			htDatosInforme.put("row", datosInforme);

		} else if (idTipoInforme.equals(EnvioInformesGenericos.comunicacionesListadoGuardias)) {

			/*
			 * Vector datosFormulario = (Vector)
			 * datosInforme.get("datosFormulario"); Hashtable datoInforme =
			 * (Hashtable) datosFormulario.get(0); Vector vTotal = new Vector();
			 * String aSolicitantes = (String)
			 * datosInforme.get("aSolicitantes"); boolean isSolicitantes =
			 * aSolicitantes!=null && aSolicitantes.equalsIgnoreCase("S");
			 * if(isSolçicitantes==false){
			 */
			String aSolicitantes = (String) datosInforme.get("aSolicitantes");
			boolean isSolicitantes = aSolicitantes != null && aSolicitantes.equalsIgnoreCase("S");
			Hashtable datosconsulta = getDatosSalidaListaGuardias(datosInforme,	usrBean, isSolicitantes);
			htDatosInforme.put("row", datosconsulta.get("htCabeceraInforme"));
			htDatosInforme.put("region", datosconsulta.get("vDatosInforme"));
			htDatosInforme.put("hDatosListaGuardias", datosconsulta.get("hDatosListaGuardias"));
			/*
			 * }else{ for (int j = 0; j < datosFormulario.size(); j++) {
			 * Hashtable htDatoInforme = new Hashtable();
			 * 
			 * Hashtable datoInform = (Hashtable) datosFormulario.get(j);
			 * Hashtable datosconsulta= getDatosSalidaListaGuardias(datoInform,
			 * usrBean, backupHash, isSolicitantes); htDatoInforme.put("row",
			 * datosconsulta.get("htCabeceraInforme"));
			 * htDatoInforme.put("region", datosconsulta.get("vDatosInforme"));
			 * vTotal.add(htDatosInforme); } htDatosInforme.put("row", vTotal);
			 * }
			 */

		} else if (idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesMorosos)) {
			String idPersona = (String) datosInforme.get("idPersona");
			String idInstitucion = (String) datosInforme.get("idInstitucion");
			String idFactura = (String) datosInforme.get("idFactura");
			// Como sabemos la clave por la que puede iterar, si existe estara
			// en plural
			Object oFacturasPersona = datosInforme.get("idFacturas");
			ArrayList alFacturasPersona = null;
			if (oFacturasPersona instanceof ArrayList) {
				alFacturasPersona = (ArrayList) oFacturasPersona;
			} else if (oFacturasPersona instanceof String) {
				// si viene por el proceso automatico
				alFacturasPersona = new ArrayList(getDatosSeparados(
						(String) oFacturasPersona, "@@"));
			}
			if (alFacturasPersona == null) {
				alFacturasPersona = new ArrayList();
				alFacturasPersona.add(idFactura);

			}
			// Esto lo añadimos al hash para que luego se inserte en la tabla
			// env_comunicacion_morosos
			datosInforme.put("idFacturas", alFacturasPersona);

			Date hoy = new Date();
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
			String sHoy = sdf2.format(hoy);

			FacFacturaAdm facturaAdm = new FacFacturaAdm(usrBean);

			Vector vDatosInforme = facturaAdm.selectFacturasMoroso(
					idInstitucion, idPersona, null, null, alFacturasPersona,
					null, false, true, usrBean.getLanguage());

			Hashtable htCabeceraInforme = new Hashtable();
			
			CenClienteAdm admCli = new CenClienteAdm(usrBean);


			Hashtable htCol = admCli.obtenerDatosColegiadoONo(true,
					idInstitucion.toString(), idPersona, usrBean.getLanguage());
			// se añade esta linea para que recupere todos los campos de la
			// select que esta en la función obtenerDatosColegiado(..)
			// y posteriormente utilizarla en las plantillas.
			//aalg: se controla que no sea colegiado. INC_10396_SIGA
			if (htCol == null){
				htCol = admCli.obtenerDatosColegiadoONo(false,
						idInstitucion.toString(), idPersona, usrBean.getLanguage());
			}
			//aalg: por si no hay datos
			if (htCol != null){
				htCabeceraInforme.putAll(htCol);
			}

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

			String poblacionExtranjera = "";
			if (htCol != null && htCol.size() > 0) {
				poblacionExtranjera = (String) htCol
						.get("POBLACION_EXTRANJERA");
			}

			if ((!poblacionExtranjera.equals("")) && (localidad.equals(""))) {
				htCabeceraInforme.put("CIUDAD", poblacionExtranjera);
			}

			if ((direccion.equals("")) && (codPostal.equals(""))
					&& (localidad.equals("")) && (provincia.equals(""))) {
				Vector direcciones = null;
				String idprovincia = "";
				String idpoblacion = "";

				// se llama a la funcion de getDirecciones donde si no tiene
				// dirección de tipo despacho, coge la
				// la dirección preferente y si no tiene ninguna dirección como
				// preferente coge la primera dirección que encuentra.
				// el parametro de valor 2, es valor que se le pasa a la funcion
				// para que sepa que tipo de dirección queremos.
				EnvEnviosAdm enviosAdm = new EnvEnviosAdm(usrBean);
				direcciones = enviosAdm.getDirecciones(
						String.valueOf(idInstitucion), idPersona, "2");

				CenPersonaAdm personaAdm = new CenPersonaAdm(usrBean);
				Hashtable htPersona = new Hashtable();
				htPersona.put(CenPersonaBean.C_IDPERSONA, idPersona);
				CenPersonaBean persona = (CenPersonaBean) ((Vector) personaAdm
						.selectByPK(htPersona)).get(0);

				if (direcciones != null && direcciones.size() > 0) {
					Hashtable htDir = (Hashtable) direcciones.firstElement();
					htCabeceraInforme.put("DIRECCION",
							(String) htDir.get(CenDireccionesBean.C_DOMICILIO));
					htCabeceraInforme.put("CP", (String) htDir
							.get(CenDireccionesBean.C_CODIGOPOSTAL));

					idpoblacion = (String) htDir
							.get(CenDireccionesBean.C_IDPOBLACION);
					CenPoblacionesAdm admpoblacion = new CenPoblacionesAdm(
							usrBean);
					if (!idpoblacion.equals("")) {
						htCabeceraInforme.put("CIUDAD",
								admpoblacion.getDescripcion(idpoblacion));
					}
					idprovincia = (String) htDir
							.get(CenDireccionesBean.C_IDPROVINCIA);
					CenProvinciaAdm admprovincia = new CenProvinciaAdm(usrBean);
					if (!idprovincia.equals("")) {
						htCabeceraInforme.put("PROVINCIA",
								admprovincia.getDescripcion(idprovincia));
					}
				}
			}

			String nombre = "";
			if (htCol != null && htCol.size() > 0) {
				nombre = (String) htCol.get("NOMBRE_LETRADO");
			}
			htCabeceraInforme.put("NOMBRE", nombre);
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			htCabeceraInforme.put("FECHA", sHoy);
			Hashtable htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), (String) sHoy);
			htFuncion.put(new Integer(2), "m");
			htFuncion.put(new Integer(3), usrBean.getLanguage());
			helperInformes
					.completarHashSalida(
							htCabeceraInforme,
							helperInformes
									.ejecutaFuncionSalida(
											htFuncion,
											"PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA",
											"FECHAMESLETRA"));

			double importeTotal = 0;
			double deudaTotal = 0;

			for (int j = 0; j < vDatosInforme.size(); j++) {
				Hashtable fila = (Hashtable) vDatosInforme.get(j);
				double importe = Double.parseDouble((String) fila.get("TOTAL"));
				importeTotal += importe;
				double deuda = Double.parseDouble((String) fila.get("DEUDA"));
				deudaTotal += deuda;

				// Descripcion de concepto que se pedia que saliera.
				htCabeceraInforme.put("DESCRIPCION_PROGRAMACION",
						(String) fila.get("DESCRIPCION_PROGRAMACION"));
				// INC_06198_SIGA
				// se separan las comunicaciones desde la 1 hasta la 5, si
				// existen
				// estas etiquetas podran usarse en cualquier parte de la
				// plantilla
				String sComunicaciones = (String) fila.get("COMUNICACIONES");
				String[] lComunicaciones = sComunicaciones.split("\n\r");
				String sComunicacionesMesLetra = (String) fila
						.get("COMUNICACIONESMESLETRA");
				String[] lComunicacionesMesLetra = sComunicacionesMesLetra
						.split("\n\r");
				int k;
				int p;
				for (k = 0; k < lComunicaciones.length && k < 5; k++) {
					fila.put("FECHA_" + (k + 1) + "COMUNICACION",
							lComunicaciones[k]);
					htCabeceraInforme.put("FECHA_" + (k + 1) + "COMUNICACION",
							lComunicaciones[k]);

					fila.put("FECHA_" + (k + 1) + "COMUNICACIONMESLETRA",
							lComunicacionesMesLetra[k]);
					htCabeceraInforme.put("FECHA_" + (k + 1)
							+ "COMUNICACIONMESLETRA",
							lComunicacionesMesLetra[k]);
				}

				for (; k < 5; k++) {
					fila.put("FECHA_" + (k + 1) + "COMUNICACION", "");
					htCabeceraInforme.put("FECHA_" + (k + 1) + "COMUNICACION",
							"");
					fila.put("FECHA_" + (k + 1) + "COMUNICACIONMESLETRA", "");
					htCabeceraInforme.put("FECHA_" + (k + 1)
							+ "COMUNICACIONMESLETRA", "");

				}

			}

			htCabeceraInforme.put("BRUTO", UtilidadesNumero
					.formatoCampo(UtilidadesNumero.redondea(importeTotal, 2)));
			htCabeceraInforme.put("LIQUIDO", UtilidadesNumero
					.formatoCampo(UtilidadesNumero.redondea(deudaTotal, 2)));

			htDatosInforme.put("row", htCabeceraInforme);
			htDatosInforme.put("region", vDatosInforme);

		} else if (idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesDesigna)) {
			ScsDesignaAdm scsDesignaAdm = new ScsDesignaAdm(usrBean);
			String idinstitucion = (String) datosInforme.get("idInstitucion");
			String anio = (String) datosInforme.get("anio");
			String idTurno = (String) datosInforme.get("idTurno");
			String numero = (String) datosInforme.get("numero");
			String aSolicitantes = (String) datosInforme.get("aSolicitantes");
			String aContrarios = (String) datosInforme.get("aContrarios"); //Esto solo se usa para incluir las etiquetas de EJG en designas
			String idPersonaJG = (String) datosInforme.get("idPersonaJG");

			boolean isSolicitantes = aSolicitantes != null
					&& aSolicitantes.equalsIgnoreCase("S");
			boolean isContrarios = aContrarios != null
					&& aContrarios.equalsIgnoreCase("S");
			String idiomaExt =(String) datosInforme.get("idiomaExt");
			boolean agregarEtiqEJG=true;
			
			String tipoDestinatarioInforme = (String) datosInforme.get("tipoDestinatarioInforme"); 
			
			Vector datosconsulta = scsDesignaAdm.getDatosSalidaOficio(
					idinstitucion, idTurno, anio, numero, null, isSolicitantes, isContrarios,
					idPersonaJG, idioma,idiomaExt,tipoDestinatarioInforme,agregarEtiqEJG);
			if(!isSolicitantes && datosconsulta.size()>0){
				Hashtable registro = (Hashtable) datosconsulta.get(0);
				if(registro.get("defendido")!=null){
					htDatosInforme.put("defendido", registro.get("defendido"));
					registro.remove("defendido");
				}
			}
			htDatosInforme.put("row", datosconsulta);

		} else if (idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesEjg)||idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesCAJG)) {
			ScsEJGAdm ejgAdm = new ScsEJGAdm(usrBean);
			String idTipoEJG = "s";
			if (datosInforme.get("idtipo") != null) {
				idTipoEJG = (String) datosInforme.get("idtipo");
			} else {
				idTipoEJG = (String) datosInforme.get("idTipoEJG");
			}
			String anio = (String) datosInforme.get("anio");
			String numero = (String) datosInforme.get("numero");
			String idPersona = null;
			String tipoDestinatario = null;
			if (datosInforme.get("idPersonaJG") != null) {
				idPersona = (String) datosInforme.get("idPersonaJG");
				tipoDestinatario = EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG;
			}
			String idPersonaContrario = null;
			if (datosInforme.get("idContrario") != null) {
				idPersona = (String) datosInforme.get("idContrario");
				tipoDestinatario = EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG;
			}
			
			String aSolicitantes = (String) datosInforme.get("aSolicitantes");
			String aContrarios = (String) datosInforme.get("aContrarios");
			String generarInformeSinDireccion = (String) datosInforme.get("generarInformeSinDireccion");
			boolean isSolicitantes = aSolicitantes != null
					&& aSolicitantes.equalsIgnoreCase("S")||(String) datosInforme.get("sms")!=null;
			boolean isAcontrarios = aContrarios != null
					&& aContrarios.equalsIgnoreCase("S");
			boolean isGenerarInformeSinDireccion = generarInformeSinDireccion != null
					&& generarInformeSinDireccion.equalsIgnoreCase("S");
					
			String tipoDestinatarioInforme = (String) datosInforme.get("tipoDestinatarioInforme");
//			String languageInstitucion = (String) usrBean
//					.getLanguageInstitucion();
			//quitar el idioma de la siguiente consulta, sacarlo abajo
			boolean agregarEtiqDesigna=true;
			Vector datosconsulta = ejgAdm.getDatosInformeEjg(
					usrBean.getLocation(), idTipoEJG, anio, numero, isSolicitantes, isAcontrarios, 
					idPersona,tipoDestinatario,isGenerarInformeSinDireccion,tipoDestinatarioInforme, agregarEtiqDesigna);
			Hashtable<String,Vector> htIdiomasUF = new Hashtable<String, Vector>();
			Hashtable<String,Vector> htIdiomasConyuge = new Hashtable<String,Vector>();
			 if( datosconsulta.size()>0){
				 Hashtable registro = (Hashtable) datosconsulta.get(0);
				 
				 for (int i = 0; i < datosconsulta.size(); i++) {
					 registro = (Hashtable) datosconsulta.get(i);
					 idioma = (String)registro.get("idioma");
					 if(i==0){
						 setAlgunRepresentanteLegal(registro.get("isAlgunRepresentanteLegal")!=null && (Boolean)registro.get("isAlgunRepresentanteLegal"));
						 setAlgunInformeNoGenerado(registro.get("isAlgunInformeNoGenerado")!=null && (Boolean)registro.get("isAlgunInformeNoGenerado"));
					 }
					 Vector regionUF = null;
					 Vector regionConyuge = null;
					 if(htIdiomasUF.containsKey(idioma)){
						  regionUF =   htIdiomasUF.get(idioma);
						  regionConyuge =   htIdiomasConyuge.get(idioma);
					 }else{
						 regionUF = ejgAdm.getDatosRegionUF(usrBean.getLocation(),
									idTipoEJG, anio, numero,idioma);
						 regionConyuge = ejgAdm.getDatosRegionConyuge(
									usrBean.getLocation(), idTipoEJG, anio, numero,idioma);
						 htIdiomasUF.put(idioma,regionUF);
						 htIdiomasConyuge.put(idioma,regionConyuge);
					 }
						 
					 registro.put("unidadfamiliar", regionUF);
					 registro.put("conyuge", regionConyuge);
						 
					 
//					if(!isSolicitantes ){
						
						if(registro.get("defendido")!=null){
							htDatosInforme.put("defendido", registro.get("defendido"));
							registro.remove("defendido");
						}
//					}
//					if(!isAcontrarios ){
//						Hashtable registro = (Hashtable) datosconsulta.get(0);
						if(registro.get("contrarios")!=null){
							htDatosInforme.put("contrarios", registro.get("contrarios"));
							registro.remove("contrarios");
						}
//					}
				 }
				
			 }
			
			
			
			
			
			htDatosInforme.put("row", datosconsulta);

		}else if (idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesActaComision)) {

			String idInstitucion = (String) datosInforme.get("idInstitucion");
			String idActa = (String) datosInforme.get("idActa");
			String anioActa = (String) datosInforme.get("anioActa");
			String numeroActa = (String) datosInforme.get("numeroActa");

			ScsActaComisionAdm actaAdm = new ScsActaComisionAdm(usrBean);
			Vector vDatosInformeFinal = actaAdm.getDatosInforme(idInstitucion,idActa, anioActa);
			
			Vector vDatosEJGs = actaAdm.getEJGsInforme(idInstitucion, idActa, anioActa);
			htDatosInforme.put("ejgs", vDatosEJGs);
			
			Vector vDatosEJGPendientes = actaAdm.getEJGsPendientes(idInstitucion, idActa, anioActa);
			Vector vDatosEJGPendientesPonentes = actaAdm.getEJGsPendientesPonentes(idInstitucion, idActa, anioActa);

			htDatosInforme.put("row", vDatosInformeFinal);
			htDatosInforme.put("ejgspendientes", vDatosEJGPendientes);
			htDatosInforme.put("ejgspendientesponentes", vDatosEJGPendientesPonentes);

		} else if (idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesOrdenDomicializacion)) {

			String idInstitucion = (String) datosInforme.get("idInstitucion");
			String idPersona = (String) datosInforme.get("idPersona");
			String idMandato = (String) datosInforme.get("idMandato");
			String idCuenta = (String) datosInforme.get("idCuenta");
			CenMandatosCuentasBancariasBean beanMandato = new CenMandatosCuentasBancariasBean();
			CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usrBean);
			beanMandato.setIdInstitucion(idInstitucion);
			beanMandato.setIdPersona(idPersona);
			beanMandato.setIdCuenta(idCuenta);
			beanMandato.setIdMandato(idMandato);
			Hashtable mandatoHashtable = mandatosAdm.getMandato(beanMandato);
			htDatosInforme.put("row", mandatoHashtable);

		} else if (idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesAnexoOrdenDomiciliacion)) {

			String idInstitucion = (String) datosInforme.get("idInstitucion");
			String idPersona = (String) datosInforme.get("idPersona");
			String idMandato = (String) datosInforme.get("idMandato");
			String idCuenta = (String) datosInforme.get("idCuenta");
			String idAnexo = (String) datosInforme.get("idAnexo");
			CenMandatosCuentasBancariasBean beanMandato = new CenMandatosCuentasBancariasBean();
			CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usrBean);
			beanMandato.setIdInstitucion(idInstitucion);
			beanMandato.setIdPersona(idPersona);
			beanMandato.setIdCuenta(idCuenta);
			beanMandato.setIdMandato(idMandato);
			Hashtable mandatoHashtable = mandatosAdm.getMandato(beanMandato);
			
			// Transformo los datos del formulario en un bean
			CenAnexosCuentasBancariasBean beanAnexo = new CenAnexosCuentasBancariasBean();
			beanAnexo.setIdInstitucion(idInstitucion);
			beanAnexo.setIdPersona(idPersona);
			beanAnexo.setIdCuenta(idCuenta);
			beanAnexo.setIdMandato(idMandato);
			beanAnexo.setIdAnexo(idAnexo);

			CenAnexosCuentasBancariasAdm anexosAdm = new CenAnexosCuentasBancariasAdm(usrBean);
			Hashtable anexoHashtable  = anexosAdm.getAnexo(beanAnexo);				
			mandatoHashtable.putAll(anexoHashtable);
			
			
			htDatosInforme.put("row", mandatoHashtable);


		} else if (idTipoInforme
				.equals(EnvioInformesGenericos.comunicacionesJustificacion)) {
			
			
			ScsDesignasLetradoAdm admDesignas = new ScsDesignasLetradoAdm(
					usrBean);
			InformeJustificacionMasivaForm informeJustificacionMasivaForm = new InformeJustificacionMasivaForm();
			String fichaColegial = (String) datosInforme.get("fichaColegial");
			informeJustificacionMasivaForm.setFichaColegial(Boolean
					.valueOf(fichaColegial));
			informeJustificacionMasivaForm
					.setIdInstitucion((String) datosInforme
							.get("idInstitucion"));
			String mostrarTodas = (String) datosInforme.get("mostrarTodas");
			informeJustificacionMasivaForm.setMostrarTodas(mostrarTodas);
			String activarRestriccionesFicha = (String) datosInforme
					.get("activarRestriccionesFicha");
			informeJustificacionMasivaForm.setActivarRestriccionesFicha(Boolean
					.valueOf(activarRestriccionesFicha));
			informeJustificacionMasivaForm.setIdPersona((String) datosInforme
					.get("idPersona"));
			informeJustificacionMasivaForm.setAnio((String) datosInforme
					.get("anio"));
			informeJustificacionMasivaForm
					.setActuacionesPendientes((String) datosInforme
							.get("actuacionesPendientes"));

			informeJustificacionMasivaForm.setEstado((String) datosInforme
					.get("estado"));
			informeJustificacionMasivaForm
					.setFechaJustificacionDesde((String) datosInforme
							.get("fechaJustificacionDesde"));
			informeJustificacionMasivaForm
					.setFechaJustificacionHasta((String) datosInforme
							.get("fechaJustificacionHasta"));
			informeJustificacionMasivaForm.setFechaDesde((String) datosInforme
					.get("fechaDesde"));
			informeJustificacionMasivaForm.setFechaHasta((String) datosInforme
					.get("fechaHasta"));
			informeJustificacionMasivaForm
					.setInteresadoApellidos((String) datosInforme
							.get("interesadoApellidos"));
			informeJustificacionMasivaForm
					.setInteresadoNombre((String) datosInforme
							.get("interesadoNombre"));
			informeJustificacionMasivaForm
					.setIncluirEjgNoFavorable((String) datosInforme
							.get("incluirEjgNoFavorable"));
			informeJustificacionMasivaForm
					.setIncluirEjgSinResolucion((String) datosInforme
							.get("incluirEjgSinResolucion"));
			informeJustificacionMasivaForm
					.setIncluirSinEJG((String) datosInforme
							.get("incluirSinEJG"));
			informeJustificacionMasivaForm
					.setIncluirEjgPteCAJG((String) datosInforme
							.get("incluirEjgPteCAJG"));

			Vector personasVector = new Vector();

			Hashtable htCabeceraInforme = null;
			Hashtable htPersonas = admDesignas
					.getPersonasSalidaInformeJustificacion(
							informeJustificacionMasivaForm, true);
			if (htPersonas == null || htPersonas.size() < 1) {
				throw new SIGAException("messages.informes.ficheroVacio");

			} else {
				String hoy = GstDate.getHoyJsp();
				if (idioma == null || idioma.equalsIgnoreCase("")) {
					hoy = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(hoy,
							"dma", usrBean.getLanguage());
					idioma = usrBean.getLanguageExt();

				} else {
					hoy = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(hoy,
							"dma", idioma);
					AdmLenguajesAdm admLenguajes = new AdmLenguajesAdm(usrBean);
					idioma = admLenguajes.getLenguajeExt(idioma);

				}

				// Hashtable htPersonas =
				// getHashPersonaInforme(vRowsInforme,usr,idInstitucion);
				Iterator itePersonas = htPersonas.keySet().iterator();
				int j = 0;
				while (itePersonas.hasNext()) {
					String keyPersona = (String) itePersonas.next();
					TreeMap tmRowsInformePorPersona = (TreeMap) htPersonas
							.get(keyPersona);
					Vector vRowsInformePorPersona = new Vector();
					Iterator itRowsDesigna = tmRowsInformePorPersona.keySet()
							.iterator();

					while (itRowsDesigna.hasNext()) {
						String keyRowDesigna = (String) itRowsDesigna.next();
						Hashtable htRowDesigna = (Hashtable) tmRowsInformePorPersona
								.get(keyRowDesigna);
						// esto es de lo viejo lo meto
						htRowDesigna.put("PENDIENTE", " ");
						htRowDesigna.put("ACREDITACION_INI", " ");
						htRowDesigna.put("ACREDITACION_FIN", " ");
						String estado = (String) htRowDesigna
								.get(ScsDesignaBean.C_ESTADO);
						if (estado != null && estado.equals("F"))
							htRowDesigna.put("BAJA", "X");
						else
							htRowDesigna.put("BAJA", " ");

						List<DesignaForm> designaList = (List<DesignaForm>) htRowDesigna
								.get("designaList");
						for (DesignaForm designaForm : designaList) {
							String tipoResolucionDesigna = designaForm
									.getTipoResolucionDesigna();

							String expedientes = getExpedienteIJ(designaForm
									.getExpedientes());
							htRowDesigna.put("EXPEDIENTES", expedientes);
							if (!designaForm.getPermitidoJustificar()
									&& informeJustificacionMasivaForm
											.isActivarRestriccionesFicha()) {
								String acreditacion = "";
								Hashtable htRowDesignaClone = (Hashtable) htRowDesigna
										.clone();
								htRowDesignaClone.put("PROCEDIMIENTO", "");
								htRowDesignaClone.put("CATEGORIA", "");
								htRowDesignaClone.put("FECHAJUSTIFICACION", "");
								htRowDesignaClone.put("VALIDADA", "");
								htRowDesignaClone.put("N_ACTUACION", "");
								htRowDesignaClone.put("NUMEROPROCEDIMIENTOACT",
										"");
								htRowDesignaClone.put("DESCRIPCIONFACTURACION",
										"");

								if (tipoResolucionDesigna
										.equals(admDesignas.resolucionDesignaNoFavorable)) {
									acreditacion = UtilidadesString
											.getMensajeIdioma(usrBean,
													"gratuita.informeJustificacionMasiva.resolucionDesignaNoFavorable");
								} else if (tipoResolucionDesigna
										.equals(admDesignas.resolucionDesignaSinResolucion)) {
									acreditacion = UtilidadesString
											.getMensajeIdioma(usrBean,
													"gratuita.informeJustificacionMasiva.resolucionDesignaSinResolucion");

								} else if (tipoResolucionDesigna
										.equals(admDesignas.resolucionDesignaPteCAJG)) {
									acreditacion = UtilidadesString
											.getMensajeIdioma(usrBean,
													"gratuita.informeJustificacionMasiva.resolucionDesignaPteCAJG");
								} else if (tipoResolucionDesigna
										.equals(admDesignas.resolucionDesignaSinEjg)) {
									acreditacion = UtilidadesString
											.getMensajeIdioma(usrBean,
													"gratuita.informeJustificacionMasiva.resolucionDesignaSinEjg");

								}
								htRowDesignaClone.put("ACREDITACION",
										acreditacion);
								vRowsInformePorPersona.add(htRowDesignaClone);

							} else {
								boolean isPrimero = true;
								StringBuffer asuntoDesigna = new StringBuffer(
										designaForm.getAsunto() == null ? ""
												: designaForm.getAsunto());
								asuntoDesigna.append(" ");
								if (designaForm.getActuaciones() != null
										&& designaForm.getActuaciones().size() > 0) {
									Map<String, List<ActuacionDesignaForm>> actuacionesMap = designaForm
											.getActuaciones();
									Iterator actuacionesIterator = actuacionesMap
											.keySet().iterator();
									String categoria = "";
									String procedimiento = "";

									while (actuacionesIterator.hasNext()) {
										String idProcedimineto = (String) actuacionesIterator
												.next();
										List<ActuacionDesignaForm> actuacionesList = actuacionesMap
												.get(idProcedimineto);
										if (actuacionesList != null
												&& actuacionesList.size() > 0) {

											for (ActuacionDesignaForm actuacionForm : actuacionesList) {
												categoria = actuacionForm
														.getCategoria();

												procedimiento = actuacionForm
														.getDescripcionProcedimiento();
												if (actuacionForm
														.getNumeroProcedimiento() != null
														&& !actuacionForm
																.getNumeroProcedimiento()
																.equals("")) {
													asuntoDesigna
															.append(actuacionForm
																	.getNumeroProcedimiento());
													asuntoDesigna.append(" ");
												}

												// String acreditacion =
												// actuacionForm.getDescripcion();
												String acreditacion = actuacionForm
														.getAcreditacion()
														.getDescripcion();
												String fechaJustificacion = "";
												String validada = "";
												if (actuacionForm
														.getFechaJustificacion() != null
														&& !actuacionForm
																.getFechaJustificacion()
																.equals("")) {
													fechaJustificacion = actuacionForm
															.getFechaJustificacion();
													if (actuacionForm
															.getValidada()
															.equals("1")) {
														validada = "X";
													}

												}
												String numeroAsunto = "";
												if (actuacionForm.getNumero() != null
														&& !actuacionForm
																.getNumero()
																.equals("")) {
													numeroAsunto = actuacionForm
															.getNumero();
												}
												String numeroProcediminetoAct = "";
												if (actuacionForm
														.getNumeroProcedimiento() != null
														&& !actuacionForm
																.getNumeroProcedimiento()
																.equals("")) {
													numeroProcediminetoAct = actuacionForm
															.getNumeroProcedimiento();
												}

												String descripcionFacturacion = "";
												if (actuacionForm
														.getDescripcionFacturacion() != null
														&& !actuacionForm
																.getDescripcionFacturacion()
																.equals("")) {
													descripcionFacturacion = actuacionForm
															.getDescripcionFacturacion();
												}

												Hashtable htRowDesignaClone = (Hashtable) htRowDesigna
														.clone();
												if (isPrimero) {
													isPrimero = false;
												} else {
													htRowDesignaClone
															.put("CODIGODESIGNA",
																	"");
													htRowDesignaClone.put(
															"EXPEDIENTES", "");
													htRowDesignaClone.put(
															"IDJUZGADO", "");
													htRowDesignaClone.put(
															"FECHADESIGNA", "");
													htRowDesignaClone.put(
															"ASUNTO", "");
													htRowDesignaClone.put(
															"CLIENTE", "");

												}
												htRowDesignaClone.put(
														"CATEGORIA", categoria);
												htRowDesignaClone.put(
														"PROCEDIMIENTO",
														procedimiento);
												htRowDesignaClone.put(
														"ACREDITACION",
														acreditacion);
												htRowDesignaClone.put(
														"FECHAJUSTIFICACION",
														fechaJustificacion);
												htRowDesignaClone.put(
														"VALIDADA", validada);
												htRowDesignaClone.put(
														"N_ACTUACION",
														numeroAsunto);
												htRowDesignaClone
														.put("NUMEROPROCEDIMIENTOACT",
																numeroProcediminetoAct);
												htRowDesignaClone
														.put("DESCRIPCIONFACTURACION",
																descripcionFacturacion);
												vRowsInformePorPersona
														.add(htRowDesignaClone);

											}
											if (designaForm.getAcreditaciones() != null
													&& designaForm
															.getAcreditaciones()
															.size() > 0) {
												Map<String, List<AcreditacionForm>> acreditacionesMap = designaForm
														.getAcreditaciones();
												List<AcreditacionForm> acreditacionesList = acreditacionesMap
														.get(idProcedimineto);
												if (acreditacionesList != null
														&& acreditacionesList
																.size() > 0) {
													for (AcreditacionForm acreditacionForm : acreditacionesList) {
														// String categoria =
														// actuacionForm.getCategoria();
														String fechaJustificacion = "";
														String validada = "";
														String numeroAsunto = "";
														String numeroProcediminetoAct = "";
														String descripcionFacturacion = "";
														String acreditacion = acreditacionForm
																.getDescripcion();
														Hashtable htRowDesignaClone2 = (Hashtable) htRowDesigna
																.clone();
														if (isPrimero) {
															isPrimero = false;
														} else {
															htRowDesignaClone2
																	.put("CODIGODESIGNA",
																			"");
															htRowDesignaClone2
																	.put("EXPEDIENTES",
																			"");
															htRowDesignaClone2
																	.put("IDJUZGADO",
																			"");
															htRowDesignaClone2
																	.put("FECHADESIGNA",
																			"");
															htRowDesignaClone2
																	.put("ASUNTO",
																			"");
															htRowDesignaClone2
																	.put("CLIENTE",
																			"");

														}
														htRowDesignaClone2
																.put("PROCEDIMIENTO",
																		procedimiento);
														htRowDesignaClone2.put(
																"CATEGORIA",
																categoria);
														htRowDesignaClone2.put(
																"ACREDITACION",
																acreditacion);
														htRowDesignaClone2
																.put("FECHAJUSTIFICACION",
																		fechaJustificacion);
														htRowDesignaClone2.put(
																"VALIDADA",
																validada);
														htRowDesignaClone2.put(
																"N_ACTUACION",
																numeroAsunto);
														htRowDesignaClone2
																.put("NUMEROPROCEDIMIENTOACT",
																		numeroProcediminetoAct);
														htRowDesignaClone2
																.put("DESCRIPCIONFACTURACION",
																		descripcionFacturacion);
														vRowsInformePorPersona
																.add(htRowDesignaClone2);
													}
												}
											}
										}
									}

								} else {
									if (designaForm.getIdJuzgado() == null
											|| designaForm.getIdJuzgado()
													.equals("")) {

										String acreditacion = UtilidadesString
												.getMensajeIdioma(usrBean,
														"gratuita.informeJustificacionMasiva.aviso.sinJuzgado");
										Hashtable htRowDesignaClone = (Hashtable) htRowDesigna
												.clone();
										htRowDesignaClone.put("CATEGORIA", "");
										htRowDesignaClone.put("PROCEDIMIENTO",
												"");
										htRowDesignaClone.put("ACREDITACION",
												acreditacion);
										htRowDesignaClone.put(
												"FECHAJUSTIFICACION", "");
										htRowDesignaClone.put("VALIDADA", "");
										htRowDesignaClone
												.put("N_ACTUACION", "");
										htRowDesignaClone.put(
												"NUMEROPROCEDIMIENTOACT", "");
										htRowDesignaClone.put(
												"DESCRIPCIONFACTURACION", "");
										vRowsInformePorPersona
												.add(htRowDesignaClone);

									} else if (designaForm.getIdProcedimiento() == null
											|| designaForm.getIdProcedimiento()
													.equals("")) {
										String acreditacion = UtilidadesString
												.getMensajeIdioma(usrBean,
														"gratuita.informeJustificacionMasiva.aviso.sinModulo");
										Hashtable htRowDesignaClone = (Hashtable) htRowDesigna
												.clone();
										htRowDesignaClone.put("CATEGORIA", "");
										htRowDesignaClone.put("PROCEDIMIENTO",
												"");

										htRowDesignaClone.put("ACREDITACION",
												acreditacion);
										htRowDesignaClone.put(
												"FECHAJUSTIFICACION", "");
										htRowDesignaClone.put("VALIDADA", "");
										htRowDesignaClone
												.put("N_ACTUACION", "");
										htRowDesignaClone.put(
												"NUMEROPROCEDIMIENTOACT", "");
										htRowDesignaClone.put(
												"DESCRIPCIONFACTURACION", "");
										vRowsInformePorPersona
												.add(htRowDesignaClone);
									} else {
										String categoria = designaForm
												.getCategoria();
										String procedimiento = designaForm
												.getDescripcionProcedimiento();

										if (designaForm.getAcreditaciones() != null
												&& designaForm
														.getAcreditaciones()
														.size() > 0) {
											Map<String, List<AcreditacionForm>> acreditacionesMap = designaForm
													.getAcreditaciones();
											Iterator acreditacionesIterator = acreditacionesMap
													.keySet().iterator();
											while (acreditacionesIterator
													.hasNext()) {
												String idProcedimineto = (String) acreditacionesIterator
														.next();
												List<AcreditacionForm> acreditacionesList = acreditacionesMap
														.get(idProcedimineto);
												if (acreditacionesList != null
														&& acreditacionesList
																.size() > 0) {
													for (AcreditacionForm acreditacionForm : acreditacionesList) {
														String acreditacion = acreditacionForm
																.getDescripcion();
														Hashtable htRowDesignaClone = (Hashtable) htRowDesigna
																.clone();
														if (isPrimero) {
															isPrimero = false;
														} else {
															htRowDesignaClone
																	.put("CODIGODESIGNA",
																			"");
															htRowDesignaClone
																	.put("EXPEDIENTES",
																			"");
															htRowDesignaClone
																	.put("IDJUZGADO",
																			"");
															htRowDesignaClone
																	.put("FECHADESIGNA",
																			"");
															htRowDesignaClone
																	.put("ASUNTO",
																			"");
															htRowDesignaClone
																	.put("CLIENTE",
																			"");

														}
														htRowDesignaClone.put(
																"CATEGORIA",
																categoria);
														htRowDesignaClone
																.put("PROCEDIMIENTO",
																		procedimiento);
														htRowDesignaClone.put(
																"ACREDITACION",
																acreditacion);
														htRowDesignaClone
																.put("FECHAJUSTIFICACION",
																		"");
														htRowDesignaClone.put(
																"VALIDADA", "");
														htRowDesignaClone.put(
																"N_ACTUACION",
																"");
														htRowDesignaClone
																.put("NUMEROPROCEDIMIENTOACT",
																		"");
														htRowDesignaClone
																.put("DESCRIPCIONFACTURACION",
																		"");
														vRowsInformePorPersona
																.add(htRowDesignaClone);

													}
												}

											}

										} else {

											String acreditacion = UtilidadesString
													.getMensajeIdioma(usrBean,
															"gratuita.informeJustificacionMasiva.literal.moduloSinAcreditaciones");
											Hashtable htRowDesignaClone = (Hashtable) htRowDesigna
													.clone();
											htRowDesignaClone.put("CATEGORIA",
													categoria);
											htRowDesignaClone.put(
													"PROCEDIMIENTO",
													procedimiento);
											htRowDesignaClone.put(
													"ACREDITACION",
													acreditacion);
											htRowDesignaClone.put(
													"FECHAJUSTIFICACION", "");
											htRowDesignaClone.put("VALIDADA",
													"");
											htRowDesignaClone.put(
													"N_ACTUACION", "");
											htRowDesignaClone.put(
													"NUMEROPROCEDIMIENTOACT",
													"");
											htRowDesignaClone.put(
													"DESCRIPCIONFACTURACION",
													"");
											vRowsInformePorPersona
													.add(htRowDesignaClone);
										}
									}

								}

							}

						}
					}
					// En toda las filas tenemos la descripcion del colegiado
					// asi que cogemos la
					// primera para sacar la cabecera
					Hashtable htRow = (Hashtable) vRowsInformePorPersona.get(0);
					htCabeceraInforme = new Hashtable();
					String nColegiado = (String) htRow
							.get(CenColegiadoBean.C_NCOLEGIADO);
					htCabeceraInforme.put("NCOLEGIADO", nColegiado);
					htCabeceraInforme.put("ETIQUETA", "Nº Colegiado");
					htCabeceraInforme.put("NOMBRE",
							(String) htRow.get(CenPersonaBean.C_NOMBRE));
					htCabeceraInforme.put("FECHA", hoy);

					String direccion = "";
					String codPostal = "";
					String pobLetrado = "";
					String provLetrado = "";

					if (htRow != null && htRow.size() > 0) {
						codPostal = (String) htRow.get("CP_LETRADO");
						pobLetrado = (String) htRow.get("POBLACION_LETRADO");
						direccion = (String) htRow.get("DOMICILIO_LETRADO");
						provLetrado = (String) htRow.get("PROVINCIA_LETRADO");
					}
					htCabeceraInforme.put("DIRECCION", direccion);
					htCabeceraInforme.put("CP", codPostal);
					htCabeceraInforme.put("POBLACION", pobLetrado);
					htCabeceraInforme.put("PROVINCIA", provLetrado);
					htCabeceraInforme.put("CRONOLOGICO", nColegiado);
					Hashtable aux = new Hashtable();
					aux.put("row", htCabeceraInforme);
					aux.put("region", vRowsInformePorPersona);
					htDatosInforme.put(keyPersona, aux);
				}
			}

		}
		return htDatosInforme;

	}
	
	
	/**
	 * Devuelve un Vector con los documentos a Enviar(o descargar) ya generados
	 * en formato File o Document dependiendo del parametro de entrada
	 * tipodocumento. Generara un documento por cada una de las plantillas del
	 * parametro vPlantillas(AdmInformeBean)
	 * 
	 * 
	 * @param datosInforme
	 * @param vPlantillas
	 *            Informes a generar
	 * @param usrBean
	 * @param tipoDocumento
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDocumentosAEnviar(Hashtable datosInforme,
			Vector vPlantillas, UsrBean usrBean, int tipoDocumento,
			String tipoComunicacion) throws ClsExceptions, SIGAException {

		Vector vDocumentos = new Vector();
		String idiomaExt = (String) datosInforme.get("idiomaExt");
		/*
		 * if (idiomaExt == null || idiomaExt.equals("")) idiomaExt =
		 * usrBean.getLanguageExt(); datosInforme.put("idiomaExt", idiomaExt);
		 */

		String idioma = (String) datosInforme.get("idioma");
		if (idioma == null || idioma.equals(""))
			idioma = usrBean.getLanguageInstitucion();// .getLanguage();

		switch (Integer.parseInt(idioma)) {
		case 1:
			idiomaExt = "ES";
			break;
		case 2:
			idiomaExt = "CA";
			break;
		case 3:
			idiomaExt = "EU";
			break;
		case 4:
			idiomaExt = "GL";
			break;
		}

		if (idiomaExt != null || !idiomaExt.equals("")) {
			datosInforme.put("idiomaExt", idiomaExt);
		}

		datosInforme.put("idioma", idioma);
		
		String idPersona = (String) datosInforme.get("idPersona");
		String idInstitucion = (String) datosInforme.get("idInstitucion");
		StringBuffer identificador = new StringBuffer();
		identificador.append(idInstitucion);
		if(idPersona!=null){
			identificador.append("_");
			identificador.append(idPersona);
		}
		String idPersonaJG = (String) datosInforme.get("idPersonaJG");
		if (idPersonaJG != null) {
			identificador.append("_");
			identificador.append(idPersonaJG);

		}
		String idProcurador = (String) datosInforme.get("idProcurador");
		if (idProcurador != null) {
			identificador.append("_");
			identificador.append(idProcurador);

		}
		String idContrario = (String) datosInforme.get("idContrario");
		if (idContrario != null) {
			identificador.append("_");
			identificador.append(idContrario);

		}
		

		Hashtable htDatosInformeFinal = null;
		Vector datosInformeFinalVector = null;

		if (!tipoComunicacion
				.equals(EnvioInformesGenericos.comunicacionesDesigna)
				&& !tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesExpedientes)
				&& !tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesListadoGuardias)
				&& !tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesEjg)
				&& !tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesResolucionEjg)
				&& !tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesCAJG)
				&& !tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesCenso)){
			htDatosInformeFinal = getDatosInformeFinal(datosInforme, usrBean);
		}else if(tipoComunicacion	.equals(EnvioInformesGenericos.comunicacionesCenso)){
			HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
			helperInformesAdm.setIdiomaInforme(idInstitucion, idPersona, AdmInformeBean.TIPODESTINATARIO_CENPERSONA,datosInforme, usrBean);
			idioma =  (String)datosInforme.get("idioma");
			idiomaExt = (String)datosInforme.get("idiomaExt");
			htDatosInformeFinal = getDatosInformeFinal(datosInforme, usrBean);
		}

		Hashtable hashConsultasHechas = new Hashtable();
		for (int i = 0; i < vPlantillas.size(); i++) {
			AdmInformeBean beanInforme = (AdmInformeBean) vPlantillas.get(i);

			if (tipoComunicacion
					.equals(EnvioInformesGenericos.comunicacionesListadoGuardias)) {

				Hashtable datos = new Hashtable();

				Vector datosFormulario = (Vector) datosInforme
						.get("datosFormulario");
				Vector vTotal = new Vector();

				boolean isSolicitantes = beanInforme.getASolicitantes() != null
						&& beanInforme.getASolicitantes().equalsIgnoreCase("S");
				Hashtable atoInforme = new Hashtable();

				if (datosFormulario != null) { // Descarga de fichero

					Hashtable datoInforme = (Hashtable) datosFormulario.get(0);

					if (isSolicitantes) {// Descarga de ficheros

						for (int k = 0; k < datosFormulario.size(); k++) {
							Hashtable htDatoInforme = new Hashtable();
							Hashtable datoInform = (Hashtable) datosFormulario.get(k);
							
							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							helperInformesAdm.setIdiomaInforme((String)datoInform.get("idInstitucion") ,(String)datoInform.get("idPersona") , AdmInformeBean.TIPODESTINATARIO_CENPERSONA,datoInform, usrBean);
							datoInforme.put("idioma", datoInform.get("idioma"));
							datoInforme.put("idiomaExt", datoInform.get("idiomaExt"));
						
							
							
							
							datoInform.put("aSolicitantes",
									beanInforme.getASolicitantes());
							datoInform.put("idTipoInforme", tipoComunicacion);
							// String nombreGlobal = (String)
							// htDatoInforme.get("NOMBRE");
							// if(nombreGlobal!=null){
							// datoInform.put("OBSERVACIONES", nombreGlobal);

							htDatoInforme = getDatosInformeFinal(datoInform,
									usrBean);

							if (datoInforme.get("NOMBRE") == null) {
								atoInforme = (Hashtable) htDatoInforme
										.get("hDatosListaGuardias");
								datoInforme.put("NOMBRE",
										atoInforme.get("NOMBRE"));
								datoInforme.put("LUGAR",
										atoInforme.get("LUGAR"));
								datoInforme.put("OBSERVACIONES",
										atoInforme.get("OBSERVACIONES"));
								datoInforme.put("USUMODIFICACION",
										atoInforme.get("USUMODIFICACION"));
								datoInforme.put("FECHAMODIFICACION",
										atoInforme.get("FECHAMODIFICACION"));
								datoInforme.put("IDINSTITUCION",
										atoInforme.get("IDINSTITUCION"));
								datoInforme.put("IDLISTA",
										atoInforme.get("IDLISTA"));
							}
							htDatoInforme.remove("hDatosListaGuardias");
							identificador = new StringBuffer();
							identificador.append((String) datoInforme
									.get("idLista"));
							identificador.append("_");
							identificador.append((String) datoInforme
									.get("idInstitucion"));
							identificador.append("_");
							identificador.append(new Integer(k).toString());
							identificador.append("_");
							String hoy = UtilidadesString.formatoFecha(
									new Date(), "yyyyMMddhhmmssSSS");
							identificador.append(hoy);

							File fileDocumento = getInformeGenerico(
									beanInforme, htDatoInforme, (String)datoInform.get("idiomaExt"),
									identificador.toString(), usrBean,
									tipoPlantillaWord);
							String pathDocumento = fileDocumento.getPath();
							// Creacion documentos
							int indice = pathDocumento
									.lastIndexOf(ClsConstants.FILE_SEP);
							String descDocumento = "";
							if (indice > 0)
								descDocumento = pathDocumento
										.substring(indice + 1);

							switch (tipoDocumento) {
							case 1:
								vDocumentos.add(fileDocumento);
								break;
							case 2:
								Documento documento = new Documento(
										pathDocumento, descDocumento);
								vDocumentos.add(documento);
								break;

							default:
								break;
							}

						}

					} else {// Descarga de un fichero total
						datos.put("idInstitucion",
								(String) datoInforme.get("idInstitucion"));
						datos.put("fechaIni",
								(String) datoInforme.get("fechaIni"));
						datos.put("fechaFin",
								(String) datoInforme.get("fechaFin"));
						datos.put("idLista",
								(String) datoInforme.get("idLista"));
						datos.put("idPersona",
								(String) datoInforme.get("idPersona"));
					
						
						datos.put("aSolicitantes",
								beanInforme.getASolicitantes());
						datos.put("idTipoInforme", tipoComunicacion);
						datos.put("idioma", datosInforme.get("idioma"));
						datos.put("idiomaExt", datosInforme.get("idiomaExt"));

						htDatosInformeFinal = getDatosInformeFinal(datos,
								usrBean);
						if (datos.get("NOMBRE") == null) {

							atoInforme = (Hashtable) htDatosInformeFinal
									.get("hDatosListaGuardias");
							datos.put("NOMBRE", atoInforme.get("NOMBRE"));
							datos.put("LUGAR", atoInforme.get("LUGAR"));
							datos.put("OBSERVACIONES",
									atoInforme.get("OBSERVACIONES"));
							datos.put("USUMODIFICACION",
									atoInforme.get("USUMODIFICACION"));
							datos.put("FECHAMODIFICACION",
									atoInforme.get("FECHAMODIFICACION"));
							datos.put("IDINSTITUCION",
									atoInforme.get("IDINSTITUCION"));
							datos.put("IDLISTA", atoInforme.get("IDLISTA"));

						}
						htDatosInformeFinal.remove("hDatosListaGuardias");
						identificador = new StringBuffer();
						identificador.append((String) datoInforme
								.get("idLista"));
						identificador.append("_");
						identificador.append((String) datoInforme
								.get("idPersona"));
						identificador.append("_");
						identificador.append((String) datoInforme
								.get("idInstitucion"));
						identificador.append("_");
						String hoy = UtilidadesString.formatoFecha(new Date(),
								"yyyyMMddhhmmssSSS");
						identificador.append(hoy);

						File fileDocumento = getInformeGenerico(beanInforme,
								htDatosInformeFinal, (String)datosInforme.get("idiomaExt"),
								identificador.toString(), usrBean,
								tipoPlantillaWord);
						String pathDocumento = fileDocumento.getPath();
						// Creacion documentos
						int indice = pathDocumento
								.lastIndexOf(ClsConstants.FILE_SEP);
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
				} else {// Envio de ficheros

					datos.put("idInstitucion",
							(String) datosInforme.get("idInstitucion"));
					HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
					
					
					datos.put("fechaIni", (String) datosInforme.get("fechaIni"));
					datos.put("fechaFin", (String) datosInforme.get("fechaFin"));
					datos.put("idLista", (String) datosInforme.get("idLista"));
					datos.put("idPersona",
							(String) datosInforme.get("idPersona"));
					datos.put("aSolicitantes", beanInforme.getASolicitantes());
					datos.put("idTipoInforme", tipoComunicacion);
					helperInformesAdm.setIdiomaInforme((String)datos.get("idInstitucion") ,(String)datos.get("idPersona") , AdmInformeBean.TIPODESTINATARIO_CENPERSONA,datos, usrBean);
					datosInforme.put("idioma", datos.get("idioma"));
					datosInforme.put("idiomaExt", datos.get("idiomaExt"));
					
					

					htDatosInformeFinal = getDatosInformeFinal(datos, usrBean);
					htDatosInformeFinal.remove("hDatosListaGuardias");
					identificador = new StringBuffer();
					identificador.append((String) datosInforme.get("idLista"));
					identificador.append("_");
					identificador
							.append((String) datosInforme.get("idPersona"));
					identificador.append("_");
					identificador.append((String) datosInforme
							.get("idInstitucion"));
					identificador.append("_");
					String hoy = UtilidadesString.formatoFecha(new Date(),
							"yyyyMMddhhmmssSSS");
					identificador.append(hoy);

					File fileDocumento = getInformeGenerico(beanInforme,
							htDatosInformeFinal, (String)datosInforme.get("idiomaExt"),
							identificador.toString(), usrBean,
							tipoPlantillaWord);
					String pathDocumento = fileDocumento.getPath();
					// Creacion documentos
					int indice = pathDocumento
							.lastIndexOf(ClsConstants.FILE_SEP);
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
			} else if (tipoComunicacion
					.equals(EnvioInformesGenericos.comunicacionesDesigna)) {
				String idiomacolegiado = "";
				String tipoDestinatarioEnvio = (String) datosInforme
						.get("tipoDestinatario");
				String tipoDestinatario = beanInforme.getDestinatarios().toString();
				boolean añadir = tipoDestinatarioEnvio == null;
				if(!añadir){
					if (tipoDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_CENPERSONA)) {
						añadir = tipoDestinatarioEnvio.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);

					} else if (tipoDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
						añadir = tipoDestinatarioEnvio
								.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

					} else if (tipoDestinatario
							.equalsIgnoreCase(
									AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)) {
						añadir = tipoDestinatarioEnvio
								.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR);

					} else if (tipoDestinatario
							.equalsIgnoreCase(
									AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)) {
						añadir = tipoDestinatarioEnvio.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO);
					}
				}
				if(añadir){
					idInstitucion = (String) datosInforme.get("idInstitucion");
					String anio = (String) datosInforme.get("anio");
					String idTurno = (String) datosInforme.get("idTurno");
					String numero = (String) datosInforme.get("numero");
					Hashtable datosInformeSeleccionado = new Hashtable();
					datosInformeSeleccionado.put("idTipoInforme", (String)datosInforme.get("idTipoInforme"));
					if(tipoDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)){
						ScsDesignaAdm designaAdm = new ScsDesignaAdm(usrBean);
						Vector solicitantesVector = designaAdm.getDefendidosDesignaInforme(idInstitucion, anio,idTurno, numero,idPersonaJG );
						if(solicitantesVector==null || solicitantesVector.size()==0)
							throw new SIGAException("gratuita.envio.ordinario.noDestinatario");
						for (int j = 0; j < solicitantesVector.size(); j++) {
							Hashtable solicitante = (Hashtable)solicitantesVector.get(j);
							String idPersonaJGInteresarado = (String)solicitante.get("IDPERSONAINTERESADO");
							datosInformeSeleccionado.put("idPersonaJG",idPersonaJGInteresarado);
							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							helperInformesAdm.setIdiomaInforme(idInstitucion, idPersonaJGInteresarado, AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG,datosInforme, usrBean);
							idioma = (String) datosInforme.get("idioma");
							idiomaExt = (String) datosInforme.get("idiomaExt");
							
							datosInformeSeleccionado.put("idioma", (String)datosInforme.get("idioma"));
							datosInformeSeleccionado.put("idiomaExt", (String)datosInforme.get("idiomaExt"));
							datosInformeSeleccionado.put("aSolicitantes", "S");
							
							//String keyConsultasHechas = idInstitucion + anio + idTurno	+ numero + idPersonaJG+idioma; --> idPersonaJG es null y no es el item del for
							String keyConsultasHechas = idInstitucion + anio + idTurno	+ numero + idPersonaJGInteresarado+idioma;
							
							
							
							if (hashConsultasHechas.containsKey(keyConsultasHechas)) {
								htDatosInformeFinal = (Hashtable) hashConsultasHechas
										.get(keyConsultasHechas);
							} else {
								htDatosInformeFinal = getDatosInformeFinalOficio(
										datosInforme, datosInformeSeleccionado,usrBean);
								hashConsultasHechas.put(keyConsultasHechas,
										htDatosInformeFinal);
							}
							
		
							Vector datosInformeDesigna = (Vector) htDatosInformeFinal
									.get("row");
							for (int k = 0; k < datosInformeDesigna.size(); k++) {
								Hashtable datosInformeK = (Hashtable) datosInformeDesigna
										.get(k);
								Hashtable htDatosInforme = new Hashtable();
								htDatosInforme.put("row", datosInformeK);
								Vector regionDefendido = (Vector) htDatosInformeFinal
								.get("defendido");
								if(regionDefendido!=null)
								htDatosInforme.put("defendido", regionDefendido);
								
								String codigo = (String) datosInformeK.get("CODIGO");
								String ncolegiado = "";
//								String idiomaLetrado = (String) datosInformeK
//										.get("IDIOMA_LETRADO");
								if (datosInformeK.get("NCOLEGIADO_LETRADO") != null) {
									ncolegiado = (String) UtilidadesHash.getString(
											datosInformeK, "NCOLEGIADO_LETRADO");
								} else {
									if (datosInformeK.get("") != null)
										ncolegiado = ((String) datosInformeK.get(""))
												.split(" - ")[0];
								}
								// esta dato lo devuelve la funcion getDatosInformeFinal
								// y nos dice que plantilla debemos coger, pudiendo ser
								// CA,ES,EU,GA
//								idiomaExt = (String) UtilidadesHash.getString(
//										datosInformeK, "CODIGOLENGUAJE");
		
								identificador = new StringBuffer();
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
								String hoy = UtilidadesString.formatoFecha(new Date(),
										"yyyyMMddhhmmssSSS");
								identificador.append(hoy);
								File fileDocumento = null;
		//						if(beanInforme.getTipoformato()!=null&&beanInforme.getTipoformato().equals("X")){
		//							TODO               AAAAAAAAAAAAAAAAAAAAAAACAMBIAR
		//							InformeFacturasEmitidas informeFacturasEmitidas = new InformeFacturasEmitidas(usrBean);
		//							fileDocumento = informeFacturasEmitidas.generarComunicacionOficioPdfFromXmlToFoXsl(beanInforme,identificador.toString());
		//						}else{
								
									fileDocumento = getInformeGenerico(beanInforme,
											htDatosInforme, idiomaExt,
											identificador.toString(), usrBean,
											tipoPlantillaWord);
									
		//						}
								String pathDocumento = fileDocumento.getPath();
								// Creacion documentos
								int indice = pathDocumento
										.lastIndexOf(ClsConstants.FILE_SEP);
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
						
						
					}else{
						//si es colegiado buscamos su idioma y lo seteamos como idioma del informe, si no ira en el de la institucion
						if(tipoDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_CENPERSONA)){
							ScsDesignaAdm designaAdm = new ScsDesignaAdm(usrBean);
							idPersona =  designaAdm.getIDLetradoDesig(idInstitucion, idTurno, anio, numero);
							if(idPersona==null || idPersona.equalsIgnoreCase(""))
								throw new SIGAException("gratuita.envio.ordinario.noDestinatario");
							
							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							helperInformesAdm.setIdiomaInforme(idInstitucion, idPersona, AdmInformeBean.TIPODESTINATARIO_CENPERSONA,datosInforme, usrBean);
							idioma = (String) datosInforme.get("idioma");
							idiomaExt = (String) datosInforme.get("idiomaExt");
						}else if(tipoDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)){
							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							helperInformesAdm.setIdiomaInforme(idInstitucion, null, AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR,datosInforme, usrBean);
							idioma = (String) datosInforme.get("idioma");
							idiomaExt = (String) datosInforme.get("idiomaExt");
						}else if(tipoDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)){
							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							helperInformesAdm.setIdiomaInforme(idInstitucion, null, AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO,datosInforme, usrBean);
							idioma = (String) datosInforme.get("idioma");
							idiomaExt = (String) datosInforme.get("idiomaExt");
						}
						datosInformeSeleccionado.put("idioma", (String)datosInforme.get("idioma"));
						datosInformeSeleccionado.put("idiomaExt", (String)datosInforme.get("idiomaExt"));
						datosInformeSeleccionado.put("aSolicitantes", beanInforme.getASolicitantes() != null? beanInforme.getASolicitantes():"N");
						boolean isSolicitantes = beanInforme.getASolicitantes() != null	&& beanInforme.getASolicitantes().equalsIgnoreCase("S");
						String keyConsultasHechas = idInstitucion + anio + idTurno	+ numero + isSolicitantes;
						datosInforme.put("aSolicitantes",beanInforme.getASolicitantes());
						datosInforme.put("aContrarios",beanInforme.getaContrarios()); //Add MJM etiquetas informes EJG en informe designas
						datosInformeSeleccionado.put("tipoDestinatarioInforme",tipoDestinatario);

						
						if (hashConsultasHechas.containsKey(keyConsultasHechas)) {
							htDatosInformeFinal = (Hashtable) hashConsultasHechas
									.get(keyConsultasHechas);
						} else {
							htDatosInformeFinal = getDatosInformeFinalOficio(
									datosInforme, datosInformeSeleccionado,usrBean);
							hashConsultasHechas.put(keyConsultasHechas,
									htDatosInformeFinal);
						}
	
						Vector datosInformeDesigna = (Vector) htDatosInformeFinal
								.get("row");
						for (int k = 0; k < datosInformeDesigna.size(); k++) {
							Hashtable datosInformeK = (Hashtable) datosInformeDesigna
									.get(k);
							Hashtable htDatosInforme = new Hashtable();
							htDatosInforme.put("row", datosInformeK);
							Vector regionDefendido = (Vector) htDatosInformeFinal
							.get("defendido");
							if(regionDefendido!=null)
							htDatosInforme.put("defendido", regionDefendido);
							
							String codigo = (String) datosInformeK.get("CODIGO");
							String ncolegiado = "";
//							String idiomaLetrado = (String) datosInformeK
//									.get("IDIOMA_LETRADO");
							if (datosInformeK.get("NCOLEGIADO_LETRADO") != null) {
								ncolegiado = (String) UtilidadesHash.getString(
										datosInformeK, "NCOLEGIADO_LETRADO");
							} else {
								if (datosInformeK.get("") != null)
									ncolegiado = ((String) datosInformeK.get(""))
											.split(" - ")[0];
							}
							// esta dato lo devuelve la funcion getDatosInformeFinal
							// y nos dice que plantilla debemos coger, pudiendo ser
							// CA,ES,EU,GA
//							idiomaExt = (String) UtilidadesHash.getString(
//									datosInformeK, "CODIGOLENGUAJE");
	
							identificador = new StringBuffer();
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
							String hoy = UtilidadesString.formatoFecha(new Date(),
									"yyyyMMddhhmmssSSS");
							identificador.append(hoy);
							File fileDocumento = null;
	//						if(beanInforme.getTipoformato()!=null&&beanInforme.getTipoformato().equals("X")){
	//							TODO               AAAAAAAAAAAAAAAAAAAAAAACAMBIAR
	//							InformeFacturasEmitidas informeFacturasEmitidas = new InformeFacturasEmitidas(usrBean);
	//							fileDocumento = informeFacturasEmitidas.generarComunicacionOficioPdfFromXmlToFoXsl(beanInforme,identificador.toString());
	//						}else{
							
								fileDocumento = getInformeGenerico(beanInforme,
										htDatosInforme, idiomaExt,
										identificador.toString(), usrBean,
										tipoPlantillaWord);
								
	//						}
							String pathDocumento = fileDocumento.getPath();
							// Creacion documentos
							int indice = pathDocumento
									.lastIndexOf(ClsConstants.FILE_SEP);
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
				}
			} else if (tipoComunicacion
					.equals(EnvioInformesGenericos.comunicacionesEjg)||tipoComunicacion
					.equals(EnvioInformesGenericos.comunicacionesCAJG)||tipoComunicacion
					.equals(EnvioInformesGenericos.comunicacionesResolucionEjg)) {
				String tipoDestinatarioEnvio = (String) datosInforme
						.get("tipoDestinatario");
				String tipoDestinatario = beanInforme.getDestinatarios();
				boolean añadir = tipoDestinatarioEnvio == null;
				if(!añadir){
					if (String.valueOf(tipoDestinatario).equalsIgnoreCase(
							AdmInformeBean.TIPODESTINATARIO_CENPERSONA)) {
						añadir = tipoDestinatarioEnvio
								.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);

					} else if (String
							.valueOf(tipoDestinatario)
							.equalsIgnoreCase(
									AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
						añadir = tipoDestinatarioEnvio
								.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

					} else if (String.valueOf(tipoDestinatario)
							.equalsIgnoreCase(
									AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)) {
						añadir = tipoDestinatarioEnvio
								.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO);
					}else if (String.valueOf(tipoDestinatario)
							.equalsIgnoreCase(
									AdmInformeBean.TIPODESTINATARIO_SCSCONTRARIOSJG)) {
						añadir = tipoDestinatarioEnvio
								.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG);
					}


				}

				if (añadir) {
					
					Hashtable datosInformeSeleccionado = new Hashtable();
					datosInformeSeleccionado.put("idTipoInforme", (String)datosInforme.get("idTipoInforme"));
					
					idInstitucion = (String) datosInforme.get("idinstitucion");
					if (idInstitucion == null)
						idInstitucion = (String) datosInforme
								.get("idInstitucion");

					String anio = (String) datosInforme.get("anio");
					String idTipoEJG = "";
					if ((String) datosInforme.get("idtipo") != null) {
						idTipoEJG = (String) datosInforme.get("idtipo");
					} else {
						idTipoEJG = (String) datosInforme.get("idTipoEJG");
					}
					// BEGIN BNS INC_08446_SIGA SUSTITUIMOS EL ID DE LA TABLA DE EJG POR EL CÓDIGO DE EJG
					String numero = "";
					String id = (String) datosInforme.get("numero");
					if (idInstitucion!=null&&!"".equals(idInstitucion)&&idTipoEJG!=null&&!"".equals(idTipoEJG)&&
							anio!=null&&!"".equals(anio)&&id!=null&&!"".equals(id)){
						ScsEJGAdm ejgAdm = new ScsEJGAdm(usrBean);
						Hashtable datosEjg = ejgAdm.getDatosEjg(idInstitucion, anio, id, idTipoEJG);
						numero = datosEjg.get("CODIGO").toString();
					}					
					// END BNS
					
					
					
					datosInformeSeleccionado.put("aSolicitantes", beanInforme.getASolicitantes() != null? beanInforme.getASolicitantes():"N");
					
					boolean isSolicitantes = beanInforme.getASolicitantes() != null
							&& beanInforme.getASolicitantes().equalsIgnoreCase(
									"S");
					boolean isAContrarios = beanInforme.getaContrarios() != null
							&& beanInforme.getaContrarios().equalsIgnoreCase(
									"S");
					boolean isGenerarInformesSindireccion = beanInforme.getGenerarInformeSinDireccion()!=null && beanInforme.getGenerarInformeSinDireccion().equalsIgnoreCase("S");
					
					String keyConsultasHechas = idInstitucion + anio
							+ idTipoEJG + numero + isSolicitantes+isAContrarios+isGenerarInformesSindireccion+tipoDestinatario;
					
					datosInformeSeleccionado.put("aContrarios",	beanInforme.getaContrarios());
					datosInformeSeleccionado.put("generarInformeSinDireccion",	beanInforme.getGenerarInformeSinDireccion());
					datosInformeSeleccionado.put("tipoDestinatarioInforme",tipoDestinatario);
					
					if (hashConsultasHechas.containsKey(keyConsultasHechas)) {
						// como esto se recorre para cada plantilla (for (int
						// i=0;i<plantillas.size();i++))
						// no hace falta hacer de nuevo las consultas para cada
						// una
						htDatosInformeFinal = (Hashtable) hashConsultasHechas
								.get(keyConsultasHechas);

					} else {
						htDatosInformeFinal = getDatosInformeEjgFinal(
								datosInforme,datosInformeSeleccionado, usrBean);

						hashConsultasHechas.put(keyConsultasHechas,
								htDatosInformeFinal);

					}

					Vector datosInformeEjg = (Vector) htDatosInformeFinal
							.get("row");
					
					

//					Vector regionUF = (Vector) datosInformeEjg
//							.get("unidadfamiliar");
//					Vector regionConyuge = (Vector) htDatosInformeFinal
//							.get("conyuge");
					Vector regionDefendido = (Vector) htDatosInformeFinal.get("defendido");
					Vector regionContrarios = (Vector) htDatosInformeFinal.get("contrarios");
					
					HelperInformesAdm helperInformes = new HelperInformesAdm();
//					String idiomainteresado = "";
//					String idiomainforme = "";
					if (datosInformeEjg != null && datosInformeEjg.size() > 0) {
						
						
						for (int k = 0; k < datosInformeEjg.size(); k++) {
							

							Hashtable datosInformeK = (Hashtable) datosInformeEjg.get(k);
							
//							idioma =(String) datosInformeK.get("idioma");
							idiomaExt =(String) datosInformeK.get("idiomaExt");
							Vector regionUF = (Vector) datosInformeK.get("unidadfamiliar");
							Vector regionConyuge = (Vector) datosInformeK.get("conyuge");
							
							
							String idPersonaDesigna = (String) datosInformeK
									.get("IDPERSONA_DESIGNA");
//							if (idPersona != null && idPersonaDesigna != null
//									&& !idPersona.equals(idPersonaDesigna))
//								continue;
							// idPersona
							Hashtable htDatosInforme = new Hashtable();
							htDatosInforme.put("row", datosInformeK);
							htDatosInforme.put("unidadfamiliar", regionUF);
							htDatosInforme.put("conyuge", regionConyuge);
							if(regionDefendido!=null)
								htDatosInforme.put("defendido", regionDefendido);
							if(regionContrarios!=null)
								htDatosInforme.put("contrarios", regionContrarios);
							else
								htDatosInforme.put("contrarios", new Vector());
							identificador = new StringBuffer();
							identificador.append(idInstitucion);
							identificador.append("_");
							identificador.append(idTipoEJG);
							identificador.append("_");
							identificador.append(anio);
							identificador.append("_");
							identificador.append(numero);
							identificador.append("_");
							identificador.append(i);
							identificador.append("_");
							identificador.append(k);

							File fileDocumento = getInformeGenerico(
									beanInforme, htDatosInforme, idiomaExt,
									identificador.toString(), usrBean,
									tipoPlantillaWord);
							String pathDocumento = fileDocumento.getPath();
							// Creacion documentos
							int indice = pathDocumento
									.lastIndexOf(ClsConstants.FILE_SEP);
							String descDocumento = "";
							if (indice > 0)
								descDocumento = pathDocumento
										.substring(indice + 1);

							switch (tipoDocumento) {
							case 1:
								vDocumentos.add(fileDocumento);
								break;
							case 2:
								Documento documento = new Documento(
										pathDocumento, descDocumento);
								vDocumentos.add(documento);
								break;

							default:
								break;
							}

						}
					}

				}

			} else if (tipoComunicacion
					.equals(EnvioInformesGenericos.comunicacionesJustificacion)) {
				Iterator itePersonas = htDatosInformeFinal.keySet().iterator();
				int j = 0;
				while (itePersonas.hasNext()) {

					String keyPersona = (String) itePersonas.next();
					Hashtable htDatosInforme = (Hashtable) htDatosInformeFinal
							.get(keyPersona);

					identificador = new StringBuffer();
					identificador.append(idInstitucion);
					identificador.append("_");
					identificador.append(j);

					String nColegiado = (String) ((Hashtable) htDatosInforme
							.get("row")).get("NCOLEGIADO");

					if (nColegiado != null && !nColegiado.trim().equals("")) {
						identificador.append("_");
						identificador.append(nColegiado);
					}

					File fileDocumento = getInformeGenerico(beanInforme,
							htDatosInforme, idiomaExt,
							identificador.toString(), usrBean,
							tipoPlantillaWord);
					String pathDocumento = fileDocumento.getPath();
					// Creacion documentos
					int indice = pathDocumento
							.lastIndexOf(ClsConstants.FILE_SEP);
					String descDocumento = "";
					if (indice > 0)
						descDocumento = pathDocumento.substring(indice + 1);

					j++;
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

			}else if (tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesExpedientes)) {
 
				String tipoDestinatarioEnvio = (String) datosInforme
						.get("tipoDestinatario");
				char[] tipoDestinatario = beanInforme.getDestinatarios()
						.toCharArray();
				boolean añadir = tipoDestinatarioEnvio == null;
				if(!añadir){
					for (int jta = 0; jta < tipoDestinatario.length && !añadir; jta++) {
						if (String.valueOf(tipoDestinatario[jta]).equalsIgnoreCase(
								AdmInformeBean.TIPODESTINATARIO_CENPERSONA)) {
							añadir = tipoDestinatarioEnvio
									.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
	
						} else if (String
								.valueOf(tipoDestinatario[jta])
								.equalsIgnoreCase(
										AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
							añadir = tipoDestinatarioEnvio
									.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);
	
						} else if (String
								.valueOf(tipoDestinatario[jta])
								.equalsIgnoreCase(
										AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)) {
							añadir = tipoDestinatarioEnvio
									.equalsIgnoreCase(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR);
	
						} 
					}
				}
				if (añadir) {
				
					String idInstitucion2 = (String) datosInforme.get("idInstitucion");
					if(idInstitucion2 ==null)
						idInstitucion2 = (String) datosInforme.get("IDINSTITUCION");
					String anio = (String) datosInforme.get("anioExpediente");
					if(anio ==null)
						anio = (String) datosInforme.get("ANIOEXPEDIENTE");
					String numero = (String) datosInforme.get("numeroExpediente");
					if(numero ==null)
						numero = (String) datosInforme.get("NUMEROEXPEDIENTE");
					String idInstitucionTipoExp = (String) datosInforme.get("idInstitucionTipoExp");
					if(idInstitucionTipoExp ==null)
						idInstitucionTipoExp = (String) datosInforme.get("IDINSTITUCIONTIPOEXP");
					String idTipoExp = (String) datosInforme.get("idTipoExp");
					if(idTipoExp ==null)
						idTipoExp = (String) datosInforme.get("IDTIPOEXP");
		//			boolean aSolicitantes = this.esAlgunaASolicitantes(vPlantillas);
					boolean aSolicitantes = beanInforme.getASolicitantes()!=null && beanInforme.getASolicitantes().equals("S"); 
		
					// consultando los datos
					// A CADA UNO DE LOS REGISTROS DE ESTA CONSULTA SE
					// LE ENVÍAN TODOS LOS DOCUMENTOS
					// DE LAS PLANTILLAS OBTENIDAS ARRIBA, RESUELTOS
					// PARA EL DESTINATARIO.
					
					String keyConsultasHechas = idInstitucion2 + idInstitucionTipoExp + idTipoExp+anio+ numero+beanInforme.getDestinatarios();
					 
					if (hashConsultasHechas.containsKey(keyConsultasHechas)) {
						datosInformeFinalVector = (Vector) hashConsultasHechas.get(keyConsultasHechas);
		
					} else {
						ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm(usrBean);
						datosInformeFinalVector = expedienteAdm.getDatosInformeExpediente(idInstitucion2,	idInstitucionTipoExp, idTipoExp,anio, numero, idPersona!=null?idPersona:idPersonaJG!=null?idPersonaJG:idProcurador,beanInforme.getDestinatarios(),true,aSolicitantes);
						hashConsultasHechas.put(keyConsultasHechas,	datosInformeFinalVector);
					}
					
				
					// generando los documentos para cada persona
					Hashtable datoReal;
					String idPersonaReal, idDireccionReal;
					for (int k = 0; k < datosInformeFinalVector.size(); k++) {
						
						datoReal = (Hashtable) datosInformeFinalVector.get(k);
						
						
						idPersonaReal = (String) datoReal
								.get("IDPERSONA_DEST");
						idDireccionReal = (String) datoReal
								.get("IDDIRECCION_DEST");
		//				datosInforme.put("IDDIRECCION_DEST", idDireccionReal);
						
						datoReal.put("idTipoInforme", "EXP");
		//				datoReal.put("idPersona",
		//						(String) datosInforme.get("idPersona"));
						datoReal.put("idInstitucion",
								(String) datosInforme
										.get("idInstitucion"));
						datoReal.put("anioExpediente",
								(String) datosInforme
										.get("anioExpediente"));
						datoReal.put("numeroExpediente",
								(String) datosInforme
										.get("numeroExpediente"));
						datoReal.put("idInstitucionTipoExp",
								(String) datosInforme
										.get("idInstitucionTipoExp"));
						datoReal.put("idTipoExp",
								(String) datosInforme.get("idTipoExp"));
		////				datoReal.put("plantillas",
		////						(String) datosInforme.get("plantillas"));
		//				datoReal.put("aSolicitantes",
		//						(aSolicitantes) ? "S" : "N");
						String material= (String)datoReal.get("MATERIA");
						if(material.equals(" ()"))
							datoReal.put("MATERIA", "");
						// inc-6975 No es obligatorio que tenga
						// direccion para meterlo en el zip
						// if (idPersonaReal!=null &&
						// !idPersonaReal.trim().equals("")) {
						
						Hashtable htDatosInforme = new Hashtable();
						if(datoReal.get("regiondenunciados")!=null){
							htDatosInforme.put("regiondenunciados", (Vector)datoReal.get("regiondenunciados"));
							datoReal.remove("regiondenunciados");
						}
						if(datoReal.get("regiondenunciantes")!=null){
							htDatosInforme.put("regiondenunciantes", (Vector)datoReal.get("regiondenunciantes"));
							datoReal.remove("regiondenunciantes");
						}
						if(datoReal.get("regionpartes")!=null){
							htDatosInforme.put("regionpartes", (Vector)datoReal.get("regionpartes"));
							datoReal.remove("regionpartes");
						}
						htDatosInforme.put("row", datoReal);
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
						if (idPersonaReal!=null && !idPersonaReal.trim().equals(""))
							identificador.append(idPersonaReal);
						identificador.append("_");
						identificador.append(new Integer(i).toString());
						identificador.append("_");
						String hoy = UtilidadesString.formatoFecha(new Date(),
								"yyyyMMddhhmmssSSS");
						identificador.append(hoy);
						
						File fileDocumento = getInformeGenerico(beanInforme,
								htDatosInforme, idiomaExt,
								identificador.toString(), usrBean,
								tipoPlantillaWord);
						String pathDocumento = fileDocumento.getPath();
						// Creacion documentos
						int indice = pathDocumento
								.lastIndexOf(ClsConstants.FILE_SEP);
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
		//				if(!aSolicitantes)
		//					break;
						
						
						
					}

				}
			
			
			}else {
				
				File fileDocumento = null;
				// Para las comunicacion con plantilla Fo hay que tiene clase
				// propia de generacion
				// Por lo que ¿Deberiamos haberlo metido en genericos?
				if (tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesPagoColegiados)) {
					String nColegiado = (String) datosInforme.get("NCOLEGIADO");
					// String idPago = (String) datosInforme.get("IDPAGOS");
					String idTipoInforme = (String) datosInforme
							.get("idTipoInforme");
					identificador = new StringBuffer();
					identificador.append(nColegiado);
					identificador.append("_");
					identificador.append(idInstitucion);
					identificador.append("_");
					identificador.append(idPersona);
					identificador.append("_");
					String hoy = UtilidadesString.formatoFecha(new Date(),
							"yyyyMMddhhmmssSSS");
					identificador.append(hoy);
					fileDocumento = getInformeGenerico(beanInforme,
							htDatosInformeFinal, idiomaExt,
							identificador.toString(), usrBean, tipoPlantillaFo);

				}else if (tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesFacturaRectificativa)) {
//					String nColegiado = (String) datosInforme.get("NCOLEGIADO");
					// String idPago = (String) datosInforme.get("IDPAGOS");
					String idTipoInforme = (String) datosInforme
							.get("idTipoInforme");
					identificador = new StringBuffer();
//					identificador.append(nColegiado);
//					identificador.append("_");
					identificador.append(idInstitucion);
					identificador.append("_");
					identificador.append(idPersona);
					identificador.append("_");
					String hoy = UtilidadesString.formatoFecha(new Date(),
							"yyyyMMddhhmmssSSS");
					identificador.append(hoy);
					fileDocumento = getInformeGenerico(beanInforme,
							htDatosInformeFinal, idiomaExt,
							identificador.toString(), usrBean, tipoPlantillaFo);

				} else if (tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesFacturacionesColegiados)) {
					String nColegiado = (String) datosInforme.get("NCOLEGIADO");
					// String idPago = (String) datosInforme.get("IDPAGOS");
					String idTipoInforme = (String) datosInforme
							.get("idTipoInforme");
					identificador = new StringBuffer();
					identificador.append(nColegiado);
					identificador.append("_");
					identificador.append(idInstitucion);
					identificador.append("_");
					identificador.append(idPersona);
					identificador.append("_");
					String hoy = UtilidadesString.formatoFecha(new Date(),
							"yyyyMMddhhmmssSSS");
					identificador.append(hoy);
					fileDocumento = getInformeGenerico(beanInforme,
							htDatosInformeFinal, idiomaExt,
							identificador.toString(), usrBean, tipoPlantillaFo);

				} else if (tipoComunicacion
						.equals(EnvioInformesGenericos.comunicacionesActaComision)) {
					String numeroActa = (String) datosInforme.get("numeroActa");
					String anioActa = (String) datosInforme.get("anioActa");
					identificador = new StringBuffer();
					identificador.append(anioActa);
					identificador.append("_");
					identificador.append(numeroActa);
					identificador.append("_");
					String hoy = UtilidadesString.formatoFecha(new Date(),
							"yyyyMMddhhmmssSSS");
					identificador.append(hoy);
					fileDocumento = getInformeGenerico(beanInforme,
							htDatosInformeFinal, idiomaExt,
							identificador.toString(), usrBean,
							tipoPlantillaWord);

				} else {
					if (tipoComunicacion
							.equals(EnvioInformesGenericos.comunicacionesMorosos)) {
						if (htDatosInformeFinal.get("region") != null) {
							Vector v = (Vector) htDatosInformeFinal
									.get("region");
							if (v.get(0) != null) {
								Hashtable htV = (Hashtable) v.get(0);
								String nColegiado = (String) htV
										.get("NCOLEGIADO");
								identificador = new StringBuffer();
								if (nColegiado != null) {
									identificador.append(nColegiado);
									identificador.append("_");
								}
							}
						}
						identificador.append(idInstitucion);
						identificador.append("_");
						identificador.append(idPersona);
						identificador.append("_");
						String hoy = UtilidadesString.formatoFecha(new Date(),
								"yyyyMMddhhmmssSSS");
						identificador.append(hoy);
					}

					fileDocumento = getInformeGenerico(beanInforme,
							htDatosInformeFinal, idiomaExt,
							identificador.toString(), usrBean,
							tipoPlantillaWord);
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

				// }
			}
		}
		return vDocumentos;

	}

	/**
	 * Este metodo es el encargado de generar el envio ordinario de correos. Por
	 * definicion de requisitos se generara UN UNICO ENVIO(EnvEnvios) cuyos
	 * destinatarios sera cada uno de los destinatarios programados y los
	 * documentos seran la suma de todos los docuemntos de cada uno de los
	 * destinatarios
	 * 
	 * @param usrBean
	 * @param vDestProgramInfBean
	 *            Destinatarios del envio
	 * @param programInfBean
	 *            Programacion de informes genericos
	 * @param vPlantillasInforme
	 *            Informes a generar por destinatario
	 * @param envioProgramadoBean
	 *            Maestro de programacion de envios
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */

	public void enviarInformeGenericoOrdinario(UsrBean usrBean,
			Vector vDestProgramInfBean, EnvProgramInformesBean programInfBean,
			Vector vPlantillasInforme,
			EnvEnvioProgramadoBean envioProgramadoBean) throws ClsExceptions,
			SIGAException {

		Envio envio = new Envio(usrBean, envioProgramadoBean.getNombre());
		if (envio.getEnviosBean() != null)
			envio.getEnviosBean().setIdEstado(
					new Integer(EnvEnviosAdm.ESTADO_INICIAL));

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
		Hashtable htPersonasJG = new Hashtable();
		Hashtable htJuzgado = new Hashtable();
		Hashtable htProcuradores = new Hashtable();
		for (int j = 0; j < vDestProgramInfBean.size(); j++) {
			EnvDestProgramInformesBean destProgramInfBean = (EnvDestProgramInformesBean) vDestProgramInfBean
					.get(j);
			Hashtable datosInforme = new Hashtable();

			// (JTA) IMPORTANTE: ES MUY FACIL PENSAR QUE ESTE CAMPO DE CLAVES SE
			// VAYA A LLENAR EN
			// ALGUN ENVIO MASIVO. SI SE QUIERE EVITAR ESTE COLAPSO SE PODRIA
			// CREAR UNA TABLA DE
			// CLAVES AL ESTILO ENV_INFORMESGENERICOS PARA CADA UNO DE LOS
			// CAMPOS DE CLAVES.
			// SERVIRIA TANTO PARA LAS CLAVES DE LA PROGRAMACION COMO LA DE LOS
			// DESTINATARIOS
			// CON TIEMPO SE DEBERIA HACER PARA EVITAR COMPLICACIONES FUTURAS
			Hashtable htClavesProgramacion = getClaves(programInfBean
					.getClaves());

			// Hashtable htClavesDestinatario =
			// getClaves(destProgramInfBean.getClaves());
			ArrayList alClavesDestinatario = destProgramInfBean
					.getClavesDestinatario();

			String idPersona = destProgramInfBean.getIdPersona().toString();
			datosInforme.put("idioma", programInfBean.getIdioma().toString());
			datosInforme.put("idiomaExt", programInfBean.getIdiomaCodigoExt());
			datosInforme.put("tipoDestinatario",
					destProgramInfBean.getTipoDestinatario());
			if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)) {
				datosInforme.put("idPersona", destProgramInfBean.getIdPersona()
						.toString());

			} else if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)) {
				datosInforme.put("idPersonaJG", destProgramInfBean
						.getIdPersona().toString());

			} else if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)) {
					datosInforme.put("idPersona", destProgramInfBean.getIdPersona().toString());
			
			} else if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)) {
					datosInforme.put("idPersona", destProgramInfBean.getIdPersona().toString());
			}else if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG)) {
					datosInforme.put("idPersona", destProgramInfBean.getIdPersona().toString());
			}

			datosInforme.put("idInstitucion", destProgramInfBean
					.getIdInstitucionPersona().toString());
			datosInforme
					.put("idTipoInforme", programInfBean.getIdTipoInforme());

			datosInforme.putAll(htClavesProgramacion);
			Vector vDocumentos = null;

				datosInforme.putAll(htClavesProgramacion);
				if (alClavesDestinatario == null) {
					// datosInforme.putAll(htClavesDestinatario);
					vDocumentos = getDocumentosAEnviar(datosInforme,
							vPlantillasInforme, usrBean,
							EnvioInformesGenericos.docDocument,
							programInfBean.getIdTipoInforme());
				} else {

					vDocumentos = new Vector();

					ArrayList alFacturas = new ArrayList();
					List<ScsDesigna> designas = new ArrayList<ScsDesigna>();
					List<ScsEjg> ejgs = new ArrayList<ScsEjg>();
					for (int i = 0; i < alClavesDestinatario.size(); i++) {
						
						
						if (programInfBean.getIdTipoInforme().equals(
								EnvioInformesGenericos.comunicacionesMorosos)) {
							Hashtable htClaves = (Hashtable) alClavesDestinatario
									.get(i);
							String idFactura = (String) htClaves
									.get("idFactura");
							alFacturas.add(idFactura);
							
						} else {
							Hashtable htClaves = (Hashtable) alClavesDestinatario
									.get(i);
							datosInforme.putAll(htClaves);
							vDocumentos.addAll(getDocumentosAEnviar(
									datosInforme, vPlantillasInforme, usrBean,
									EnvioInformesGenericos.docDocument,
									programInfBean.getIdTipoInforme()));
							
							if (programInfBean.getIdTipoInforme().equals(
									EnvioInformesGenericos.comunicacionesDesigna)) {
								String idInstitucion = (String) datosInforme.get("idInstitucion");
								String anio = (String) datosInforme.get("anio");
								String idTurno = (String) datosInforme.get("idTurno");
								String numero = (String) datosInforme.get("numero");
								ScsDesigna designa = new ScsDesigna();
								designa.setIdinstitucion(Short.parseShort(idInstitucion));
								designa.setAnio(Short.parseShort(anio));
								designa.setIdturno(Integer.parseInt(idTurno));
								designa.setNumero(Long.parseLong(numero));
								designas.add(designa);
							}else if (programInfBean.getIdTipoInforme().equals(
									EnvioInformesGenericos.comunicacionesEjg)||programInfBean.getIdTipoInforme().equals(
											EnvioInformesGenericos.comunicacionesCAJG)) {
								
								
								// BEGIN BNS INC_08446_SIGA SUSTITUIMOS EL ID DE LA TABLA DE EJG POR EL CÓDIGO DE EJG
								String idInstitucion = (String) datosInforme.get("idInstitucion");
								String anio = (String) datosInforme.get("anio");
								String idTipoEJG = (String) datosInforme.get("idTipoEJG");
								String numero = (String) datosInforme.get("numero");
								ScsEjg ejg = new ScsEjg();
								ejg.setIdinstitucion(Short.parseShort(idInstitucion));
								ejg.setAnio(Short.parseShort(anio));
								ejg.setIdtipoejg(Short.parseShort(idTipoEJG));
								ejg.setNumero(Long.parseLong(numero));
								ejgs.add(ejg);
								
							}
							
							
							
							
							

						}

					}
					envio.setDesignas(designas);
					envio.setEjgs(ejgs);
					if (programInfBean.getIdTipoInforme().equals(
							EnvioInformesGenericos.comunicacionesMorosos)) {
						datosInforme.put("idFacturas", alFacturas);
						vDocumentos.addAll(getDocumentosAEnviar(datosInforme,
								vPlantillasInforme, usrBean,
								EnvioInformesGenericos.docDocument,
								programInfBean.getIdTipoInforme()));
					}
				}

			

			// en el metodo getDatosInformeFinal metemos la key definitiva
			// idFacturas en el datosInforme
			ArrayList alFacturas = ((ArrayList) datosInforme.get("idFacturas"));
			// Vamos a acumular las comunicaciones para luego insertarlas ya que
			// en el proceso de envio se
			// les setea el idEnvio
			if (programInfBean.getIdTipoInforme().equals(
					EnvioInformesGenericos.comunicacionesMorosos)) {
				ComunicacionMoroso comunicacion = new ComunicacionMoroso();
				comunicacion.setIdPersona(destProgramInfBean.getIdPersona());
				comunicacion.setDocumentos(vDocumentos);
				comunicacion.setFacturas(alFacturas);
				comunicacion
						.setIdInstitucion(programInfBean.getIdInstitucion());
				comunicacion.setDescripcion(enviosBean.getDescripcion());
				lComunicacionesMorosos.add(comunicacion);
			}
			if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)) {

				if (htPersonas.containsKey(idPersona)) {
					Vector vAuxDocumentos = (Vector) htPersonas.get(idPersona);
					vDocumentos.addAll(vAuxDocumentos);

				}
				htPersonas.put(idPersona, vDocumentos);
			} else if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)) {

				if (htPersonasJG.containsKey(idPersona)) {
					Vector vAuxDocumentos = (Vector) htPersonasJG
							.get(idPersona);
					vDocumentos.addAll(vAuxDocumentos);

				}
				htPersonasJG.put(idPersona, vDocumentos);
			
			} else if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)) {

				if (htProcuradores.containsKey(idPersona)) {
					Vector vAuxDocumentos = (Vector) htProcuradores
							.get(idPersona);
					vDocumentos.addAll(vAuxDocumentos);

				}
				htProcuradores.put(idPersona, vDocumentos);
				
			} else if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)) {

				if (htJuzgado.containsKey(idPersona)) {
					Vector vAuxDocumentos = (Vector) htJuzgado
							.get(idPersona);
					vDocumentos.addAll(vAuxDocumentos);

				}
				htJuzgado.put(idPersona, vDocumentos);
			}else if (destProgramInfBean.getTipoDestinatario().equals(
					EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG)) {

				if (htPersonasJG.containsKey(idPersona)) {
					Vector vAuxDocumentos = (Vector) htPersonasJG
							.get(idPersona);
					vDocumentos.addAll(vAuxDocumentos);

				}
				htPersonasJG.put(idPersona, vDocumentos);
			}

		}
		if (programInfBean.getIdTipoInforme().equals(
				EnvioInformesGenericos.comunicacionesExpedientes)) {
			if(htPersonasJG!=null)
				htPersonas.putAll(htPersonasJG);
			if(htProcuradores!=null)
				htPersonas.putAll(htProcuradores);
			
			envio.generarEnvioOrdinario(envio.getEnviosBean(), htPersonas,
					null,null,null);
		}else{
			envio.generarEnvioOrdinario(envio.getEnviosBean(), htPersonas,
					htPersonasJG,htJuzgado,htProcuradores);	
		}
		
		

		if (programInfBean.getIdTipoInforme().equals(
				EnvioInformesGenericos.comunicacionesMorosos)) {
			for (int i = 0; i < lComunicacionesMorosos.size(); i++) {
				ComunicacionMoroso comunicacion = (ComunicacionMoroso) lComunicacionesMorosos
						.get(i);
				envio.generarComunicacionMoroso(comunicacion.getIdPersona()
						.toString(), comunicacion.getDocumentos(), comunicacion
						.getFacturas(), comunicacion.getIdInstitucion()
						.toString(), comunicacion.getDescripcion());

			}

		}

	}
	
	
	public void enviarInformeGenericoTelematico(UsrBean usrBean,EnvDestProgramInformesBean destProgramInfBean, EnvProgramInformesBean programInfBean,Vector vPlantillasInforme,EnvEnvioProgramadoBean envioProgramadoBean) throws ClsExceptions,SIGAException {
//		for (int j = 0; j < vDestProgramInfBean.size(); j++) {//Se recorren los destinatarios (aunque en principio siempre será único para envios telematicos)
//			EnvDestProgramInformesBean destProgramInfBean = (EnvDestProgramInformesBean) vDestProgramInfBean.get(j);
			ArrayList alClavesDestinatario = destProgramInfBean.getClavesDestinatario();
			List<ScsDesigna> designas = new ArrayList<ScsDesigna>();
			List<ScsEjg> ejgs = new ArrayList<ScsEjg>();
			for (int k = 0; k < alClavesDestinatario.size(); k++) {//Para cada destinatario se recorrer sus claves de itreacion
				Hashtable htClavesProgramacion = (Hashtable) alClavesDestinatario.get(k);
				designas = new ArrayList<ScsDesigna>();
				ejgs = new ArrayList<ScsEjg>();
				for (int i = 0; i < vPlantillasInforme.size(); i++) {//Por ultimo se recorren las posible plantillas de cada registro de destinatario
					AdmInformeBean beanInforme = (AdmInformeBean) vPlantillasInforme.get(i);	
					
					
					Hashtable htPersonas = new Hashtable();
					List<Object> listObjetosTelematicos = new ArrayList<Object>();
				
					String idPersona = destProgramInfBean.getIdPersona().toString();
					StringBuffer descripcionEnvios = new StringBuffer(envioProgramadoBean.getNombre());
				
					if (destProgramInfBean.getTipoDestinatario().equals(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)) {
						htPersonas.put(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
						CenPersonaAdm persAdm = new CenPersonaAdm(this.getUsuario());
						descripcionEnvios.append(" ");
						descripcionEnvios.append(persAdm.obtenerNombreApellidos(destProgramInfBean.getIdPersona().toString()));
		
					} else if (destProgramInfBean.getTipoDestinatario().equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)) {
						htPersonas.put(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);
						ScsPersonaJGAdm persJGAdm = new ScsPersonaJGAdm(this.getUsuario());
						descripcionEnvios.append(" ");
						descripcionEnvios.append(persJGAdm.getNombreApellidos(destProgramInfBean.getIdPersona().toString(),destProgramInfBean.getIdInstitucion().toString()));
		
					} else if (destProgramInfBean.getTipoDestinatario().equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)) {
						ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.getUsuario());
						descripcionEnvios.append(" ");
						Vector procuradorVector = procuradorAdm.busquedaProcurador(destProgramInfBean.getIdInstitucion().toString(),destProgramInfBean.getIdPersona().toString());
						Hashtable procuradorHashtable = (Hashtable) procuradorVector.get(0);
						descripcionEnvios.append(procuradorHashtable.get("NOMBRE"));
						descripcionEnvios.append(" ");
						descripcionEnvios.append(procuradorHashtable.get("APELLIDOS1"));
						descripcionEnvios.append(" ");
						descripcionEnvios.append(procuradorHashtable.get("APELLIDOS2"));
		
					} else if (destProgramInfBean.getTipoDestinatario().equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)) {
						htPersonas.put(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO);
						ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm(this.getUsuario());
						descripcionEnvios.append(" ");
						Vector juzgadoVector = juzgadoAdm.busquedaJuzgado(destProgramInfBean.getIdInstitucion().toString(),	destProgramInfBean.getIdPersona().toString());
						Hashtable procuradorHashtable = (Hashtable) juzgadoVector.get(0);
						descripcionEnvios.append(procuradorHashtable.get("NOMBRE"));
					}
					
					
					
					
					
					List<Object[]> listAuxiliar = new ArrayList();  
					Object[] object = null; 
					
					if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesDesigna)){
	                    ScsDesignaAdm designaAdm = new ScsDesignaAdm(usrBean);
	                    String idInstitucion = (String) htClavesProgramacion.get("idInstitucion");
	                    String idTurnoDesigna = (String) htClavesProgramacion.get("idTurno");
	                    String anioDesigna= (String) htClavesProgramacion.get("anio");
	                    String numeroDesigna = (String) htClavesProgramacion.get("numero");
	                    
							
							ScsDesigna designa = new ScsDesigna();
							designa.setIdinstitucion(Short.parseShort(idInstitucion));
							designa.setAnio(Short.parseShort(anioDesigna));
							designa.setIdturno(Integer.parseInt(idTurnoDesigna));
							designa.setNumero(Long.parseLong(numeroDesigna));
							designas.add(designa);
						
							
							
							// BEGIN BNS INC_08446_SIGA SUSTITUIMOS EL ID DE LA TABLA DE EJG POR EL CÓDIGO DE EJG
						
						
	                    //En este punto no se van a agregar las etiquetas del informe EJG en la designa,
						// por esto estos parámetros son fal
	                    boolean agregarEtiqEJG=false;
	                    boolean isSolicitantes = false;
	                    boolean isContrarios = false;
	                    
	                    String tipoDestinatarioInforme = null;
	                    Vector designaVector = designaAdm.getDatosSalidaOficio(idInstitucion, idTurnoDesigna, anioDesigna, numeroDesigna, null, isSolicitantes,isContrarios, null, "1","ES",tipoDestinatarioInforme,agregarEtiqEJG);
	                    Hashtable designaHash = (Hashtable) designaVector.get(0);
	                   
	                    if(beanInforme.getIdTipoIntercambioTelematico().toString().equals(TipoIntercambioEnum.ICA_SGP_COM_DES_PROV_ABG_PRO.getCodigo())){	                       
	                        EcomDesignaprovisionalWithBLOBs ecomDesignaprovisional = new EcomDesignaprovisionalWithBLOBs();
	                        ecomDesignaprovisional.setIdinstitucion(UtilidadesHash.getShort(designaHash,ScsDesignaBean.C_IDINSTITUCION));          
	                        ecomDesignaprovisional.setIdturno(UtilidadesHash.getInteger(designaHash,ScsDesignaBean.C_IDTURNO));            
	                        ecomDesignaprovisional.setAnio(UtilidadesHash.getShort(designaHash,"ANIO_DESIGNA"));                
	                        ecomDesignaprovisional.setNumero(UtilidadesHash.getLong(designaHash,ScsDesignaBean.C_NUMERO));               
	                        ecomDesignaprovisional.setIdjuzgado(destProgramInfBean.getIdPersona());               
	                        ecomDesignaprovisional.setIdinstitucionJuzg(Short.parseShort(destProgramInfBean.getIdInstitucion().toString()));
	                        
	                        
	                        if(UtilidadesHash.getShort(designaHash,"IDINSTITUCIONORIGEN")!=null)
	                            ecomDesignaprovisional.setIdinstitucionLetrado(UtilidadesHash.getShort(designaHash,"IDINSTITUCIONORIGEN"));
	                        else
	                            ecomDesignaprovisional.setIdinstitucionLetrado(UtilidadesHash.getShort(designaHash,"IDINSTITUCION"));
	                       
	                        ecomDesignaprovisional.setIdpersona(UtilidadesHash.getLong(designaHash,ScsDesignasLetradoBean.C_IDPERSONA));
	                       
	                        if(designaHash.get("IDPROCURADORDESIGNA")!=null)
	                            ecomDesignaprovisional.setIdprocurador(UtilidadesHash.getLong(designaHash,"IDPROCURADORDESIGNA"));
	                       
	                        ecomDesignaprovisional.setIdplantilla(new Integer(beanInforme.getIdPlantilla()));
	                        ecomDesignaprovisional.setIdinstitucionPlantilla(new Short(beanInforme.getIdInstitucion().shortValue()));
	    //                    ecomDesignaprovisional.setFechapeticion("sysdate");
	                        
	                        
							
	                        
	                        
	                        
	                        
	                        Vector<Hashtable> ejgsAsociado = designaAdm.getDatosEJG(idInstitucion, numeroDesigna, idTurnoDesigna, anioDesigna);	        
	                        StringBuffer descripcion = new StringBuffer();
	                        descripcion.append(TipoIntercambioEnum.ICA_SGP_COM_DES_PROV_ABG_PRO.getDescripcion().split(":")[1]);
	                        descripcion.append(" - ");
	                        descripcion.append("Nº OFICIO:");
	                        descripcion.append(anioDesigna);
	                        descripcion.append("/");
	                        descripcion.append(designaHash.get("CODIGO"));
	                        descripcion.append(" ");     
	                           
	                        
	                        
	                        //Se comprueba si hay EJGs asociados
	                        if(ejgsAsociado != null && ejgsAsociado.size() > 0){
	                        	StringBuffer ejg = null;
	                        	for (Hashtable ejgAsociado : ejgsAsociado) {
	                        		ecomDesignaprovisional.setIdtipoejg(UtilidadesHash.getShort(ejgAsociado,"TIPO_EJG"));                   
		                            ecomDesignaprovisional.setAnioejg(UtilidadesHash.getShort(ejgAsociado,"ANIO_EJG"));
		                            ecomDesignaprovisional.setNumeroejg(UtilidadesHash.getLong(ejgAsociado,"NUMERO_EJG"));
		                            ejg = new StringBuffer();
		                            ejg.append("Nº EJG:");
		                            ejg.append(UtilidadesHash.getShort(ejgAsociado,"ANIO_EJG"));
		                            ejg.append("/");
		                            ejg.append(UtilidadesHash.getString(ejgAsociado,"NUM_EJG"));
		                            
		                            object = new Object[2];
			                        object[0] = descripcion.toString() + ejg.toString();
			                        object[1] = ecomDesignaprovisional;
			                        listAuxiliar.add(object);
									
								}
	                        
	                        	
	                        	
		                       
		                           
		                     }else{
		                    	 object = new Object[2];
			                     object[0] = descripcion.toString();
			                     object[1] = ecomDesignaprovisional;
			                     listAuxiliar.add(object);
		                     }
	                        
	   
	                       
	                        
	                       
	                        //Se van añadiendo a la lista para despues setearles el envio
//	                        listObjetosTelematicos.add(ecomDesignaprovisional);
	                       
	                        
	                        
	                        
	                        
	                    }
	                   
	                }else if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesEjg)||programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesCAJG)){
	                	
	                    ScsEJGAdm ejgAdm = new ScsEJGAdm(usrBean);
	                    String idInstitucion = (String) htClavesProgramacion.get("idInstitucion");
	                    String idTipoEJG = (String) htClavesProgramacion.get("idTipoEJG");
	                    String anioEJG = (String) htClavesProgramacion.get("anio");
	                    String numeroEJG = (String) htClavesProgramacion.get("numero");
	                	
						ScsEjg ejg = new ScsEjg();
						ejg.setIdinstitucion(Short.parseShort(idInstitucion));
						ejg.setAnio(Short.parseShort(anioEJG));
						ejg.setIdtipoejg(Short.parseShort(idTipoEJG));
						ejg.setNumero(Long.parseLong(numeroEJG));
						ejgs.add(ejg);
	                    
	                    boolean agregarEtiqDesigna=false;
	                    
	                    Vector ejgVector = ejgAdm.getDatosInformeEjg(usrBean.getLocation(), idTipoEJG, anioEJG, numeroEJG, false,false, null,null,true,AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO,agregarEtiqDesigna);
	                    Hashtable ejgHash = (Hashtable) ejgVector.get(0);
	                   
	                    if(beanInforme.getIdTipoIntercambioTelematico().toString().equals(TipoIntercambioEnum.ICA_SGP_COM_DES_PROV_ABG_PRO.getCodigo())){
	                       
	                        EcomDesignaprovisionalWithBLOBs designaProvisionalBean = new EcomDesignaprovisionalWithBLOBs();
	                        designaProvisionalBean.setIdinstitucion(UtilidadesHash.getShort(ejgHash,"DES_INSTITUCION"));          
	                        designaProvisionalBean.setIdturno(UtilidadesHash.getInteger(ejgHash,"DES_IDTURNO"));            
	                        designaProvisionalBean.setAnio(UtilidadesHash.getShort(ejgHash,"DES_ANIO"));                
	                        designaProvisionalBean.setNumero(UtilidadesHash.getLong(ejgHash,"DES_NUMERO"));               
	   
	    //                   designaProvisionalBean.setIdEstado(Integer.valueOf((short)EcomColaBean.EstadosCola.INICIAL.getId()));               
	                       
	                        designaProvisionalBean.setIdjuzgado(destProgramInfBean.getIdPersona());               
	                        designaProvisionalBean.setIdinstitucionJuzg(new Short(destProgramInfBean.getIdInstitucion().shortValue()));  
	                        if(designaProvisionalBean.getIdinstitucion()==null)
	                            designaProvisionalBean.setIdinstitucion(new Short(idInstitucion));
	                        
	                        designaProvisionalBean.setIdtipoejg(new Short(idTipoEJG));                   
	                        designaProvisionalBean.setAnioejg(new Short(anioEJG));
	                        designaProvisionalBean.setNumeroejg(new Long(numeroEJG));
	   
	                        // El id envio se setea despues
	                       
	                        designaProvisionalBean.setIdinstitucionLetrado(UtilidadesHash.getShort(ejgHash,"IDINSTITUCION_LETDESIGNA"));
	                        designaProvisionalBean.setIdpersona(UtilidadesHash.getLong(ejgHash,"IDPERSONA_DESIGNA"));
	                        if(UtilidadesHash.getLong(ejgHash,"IDPROCURADOR")!=null)
	                            designaProvisionalBean.setIdprocurador(UtilidadesHash.getLong(ejgHash,"IDPROCURADOR"));
	                       
	                        designaProvisionalBean.setIdplantilla(new Integer(beanInforme.getIdPlantilla()));
	                        designaProvisionalBean.setIdinstitucionPlantilla(new Short(beanInforme.getIdInstitucion().shortValue()));
	    //                    designaProvisionalBean.setFechaPeticion("sysdate");
	                        
	                        
	                        //Se van añadiendo a la lista para despues setearles el envio
//	                        listObjetosTelematicos.add(designaProvisionalBean);
	                        StringBuffer descripcion = new StringBuffer();
	                        descripcion.append(TipoIntercambioEnum.ICA_SGP_COM_DES_PROV_ABG_PRO.getDescripcion().split(":")[1]);
	                        descripcion.append(" - ");
	                        descripcion.append("Nº EJG:");
//	                        descripcion.append(anioEJG);
//	                        descripcion.append("/");
	                        descripcion.append(UtilidadesHash.getString(ejgHash,"NUMERO_EJG"));
	                        descripcion.append(" ");
	                        if(designaProvisionalBean.getAnio()!=null){
	                            descripcion.append("Nº OFICIO:");
	                            descripcion.append(UtilidadesHash.getString(ejgHash,"NUMERO_DESIGNA"));
		                        descripcion.append(" ");
//	                            descripcion.append(designaProvisionalBean.getAnio());
//	                            descripcion.append("/");
//	                            descripcion.append(designaProvisionalBean.getNumero());
	                        }else{
	                            throw new SIGAException("No se puede comunicar una designacion provisional desde EJG sin designacion asociada!!"+descripcion);
	                           
	                        }
	
	                        
	                        object = new Object[2];
	                        object[0] = descripcion.toString();
	                        object[1] = designaProvisionalBean;
	                        listAuxiliar.add(object);
	                        
	                        
	                       
	                       
	                    }else if(beanInforme.getIdTipoIntercambioTelematico().toString().equals(TipoIntercambioEnum.ICA_SGP_ENV_SOL_SUSP_PROC.getCodigo())){
	                       
	                        EcomSolsusprocedimientoWithBLOBs solSusProcedimientoBean = new EcomSolsusprocedimientoWithBLOBs();
	                        solSusProcedimientoBean.setIdjuzgado(destProgramInfBean.getIdPersona());               
	                        solSusProcedimientoBean.setIdinstitucionJuzg(new Short(destProgramInfBean.getIdInstitucion().shortValue()));  
	                        solSusProcedimientoBean.setIdinstitucion(new Short(idInstitucion));
	                        solSusProcedimientoBean.setIdtipoejg(new Short(idTipoEJG));                   
	                        solSusProcedimientoBean.setAnioejg(new Short(anioEJG));
	                        solSusProcedimientoBean.setNumeroejg(new Long(numeroEJG));
	                        solSusProcedimientoBean.setIdplantilla(new Integer(beanInforme.getIdPlantilla()));
	                        solSusProcedimientoBean.setIdinstitucionPlantilla(new Short(beanInforme.getIdInstitucion().shortValue()));
	    //                  solSusProcedimientoBean.setFechaPeticion("sysdate");
	                       
	                        //Se van añadiendo a la lista para despues setearles el envio
//	                        listObjetosTelematicos.add(solSusProcedimientoBean);
	                       
	                        StringBuffer descripcion = new StringBuffer();
	                        descripcion.append(TipoIntercambioEnum.ICA_SGP_ENV_SOL_SUSP_PROC.getDescripcion().split(":")[1]);
	                        descripcion.append(" - ");
	                        descripcion.append("Nº EJG:");
//	                        descripcion.append(anioEJG);
//	                        descripcion.append("/");
	                        descripcion.append(UtilidadesHash.getString(ejgHash,"NUMERO_EJG"));
	                       
	                        
	                        
	                        object = new Object[2];
	                        object[0] =descripcion.toString();
	                        object[1] = solSusProcedimientoBean;
	                        listAuxiliar.add(object);
	                        
	                    }else if(beanInforme.getIdTipoIntercambioTelematico().toString().equals(TipoIntercambioEnum.CAJG_SGP_SOL_IMP_AJG.getCodigo())){
	                       
	                    	//CRM	                    	
	                        EcomSolimpugresolucionajgWithBLOBs solimpugresolucionajgWithBLOBs = new EcomSolimpugresolucionajgWithBLOBs();
	                        solimpugresolucionajgWithBLOBs.setIdjuzgado(destProgramInfBean.getIdPersona());               
	                        solimpugresolucionajgWithBLOBs.setIdinstitucionJuzg(new Short(destProgramInfBean.getIdInstitucion().shortValue()));  
	                        solimpugresolucionajgWithBLOBs.setIdinstitucion(new Short(idInstitucion));
	                        solimpugresolucionajgWithBLOBs.setIdtipoejg(new Short(idTipoEJG));                   
	                        solimpugresolucionajgWithBLOBs.setAnioejg(new Short(anioEJG));
	                        solimpugresolucionajgWithBLOBs.setNumeroejg(new Long(numeroEJG));
	                        solimpugresolucionajgWithBLOBs.setIdplantilla(new Integer(beanInforme.getIdPlantilla()));
	                        solimpugresolucionajgWithBLOBs.setIdinstitucionPlantilla(new Short(beanInforme.getIdInstitucion().shortValue()));
	                        solimpugresolucionajgWithBLOBs.setIdinstitucionLetrado(UtilidadesHash.getShort(ejgHash,"IDINSTITUCION_LETDESIGNA"));
	                        solimpugresolucionajgWithBLOBs.setIdpersona(UtilidadesHash.getLong(ejgHash,"IDPERSONA"));
	                        if(UtilidadesHash.getLong(ejgHash,"IDPROCURADOR")!=null)
	                            solimpugresolucionajgWithBLOBs.setIdprocurador(UtilidadesHash.getLong(ejgHash,"IDPROCURADOR"));	                        
                       
	                        StringBuffer descripcion = new StringBuffer();
	                        descripcion.append(TipoIntercambioEnum.CAJG_SGP_SOL_IMP_AJG.getDescripcion().split(":")[1]);
	                        descripcion.append(" - ");
	                        descripcion.append("Nº EJG:");
//	                        descripcion.append(anioEJG);
//	                        descripcion.append("/");
//	                        descripcion.append(UtilidadesHash.getString(ejgHash,"NUMERO"));
	                        descripcion.append(UtilidadesHash.getString(ejgHash,"NUMERO_EJG"));
	                       
	                        object = new Object[2];
	                        object[0] =descripcion.toString();
	                        object[1] = solimpugresolucionajgWithBLOBs;
	                        listAuxiliar.add(object);
	                        
	                    }else if(beanInforme.getIdTipoIntercambioTelematico().toString().equals(TipoIntercambioEnum.CAJG_SGP_COM_RESOL_SOL_AJG.getCodigo())){
	                       
	                    	//CRM	                    	
	                        EcomComunicacionresolucionajgWithBLOBs ecomComunicacionresolucionajgWithBLOBs = new EcomComunicacionresolucionajgWithBLOBs();
	                        ecomComunicacionresolucionajgWithBLOBs.setIdjuzgado(destProgramInfBean.getIdPersona());               
	                        ecomComunicacionresolucionajgWithBLOBs.setIdinstitucionJuzg(new Short(destProgramInfBean.getIdInstitucion().shortValue()));  
	                        ecomComunicacionresolucionajgWithBLOBs.setIdinstitucion(new Short(idInstitucion));
	                        ecomComunicacionresolucionajgWithBLOBs.setIdtipoejg(new Short(idTipoEJG));                   
	                        ecomComunicacionresolucionajgWithBLOBs.setAnioejg(new Short(anioEJG));
	                        ecomComunicacionresolucionajgWithBLOBs.setNumeroejg(new Long(numeroEJG));
	                        ecomComunicacionresolucionajgWithBLOBs.setIdplantilla(new Integer(beanInforme.getIdPlantilla()));
	                        ecomComunicacionresolucionajgWithBLOBs.setIdinstitucionPlantilla(new Short(beanInforme.getIdInstitucion().shortValue()));
	                        ecomComunicacionresolucionajgWithBLOBs.setIdinstitucionLetrado(UtilidadesHash.getShort(ejgHash,"IDINSTITUCION_LETDESIGNA"));
	                        ecomComunicacionresolucionajgWithBLOBs.setIdpersona(UtilidadesHash.getLong(ejgHash,"IDPERSONA"));
	                        if(UtilidadesHash.getLong(ejgHash,"IDPROCURADOR")!=null)
	                            ecomComunicacionresolucionajgWithBLOBs.setIdprocurador(UtilidadesHash.getLong(ejgHash,"IDPROCURADOR"));	                        
                       
	                        StringBuffer descripcion = new StringBuffer();
	                        descripcion.append(TipoIntercambioEnum.CAJG_SGP_COM_RESOL_SOL_AJG.getDescripcion().split(":")[1]);
	                        descripcion.append(" - ");
	                        descripcion.append("Nº EJG:");
//	                        descripcion.append(anioEJG);
//	                        descripcion.append("/");
//	                        descripcion.append(UtilidadesHash.getString(ejgHash,"NUMERO"));
	                        descripcion.append(UtilidadesHash.getString(ejgHash,"NUMERO_EJG"));
	                       
	                        object = new Object[2];
	                        object[0] =descripcion.toString();
	                        object[1] = ecomComunicacionresolucionajgWithBLOBs;
	                        listAuxiliar.add(object);
	                    }
                    }
					
					for (int l = 0; l < listAuxiliar.size(); l++) {
						Object[] objeto = (Object[]) listAuxiliar.get(l);
						String descripcion = (String) objeto[0];
						Object objetoTelematico =  objeto[1];
						listObjetosTelematicos = new ArrayList<Object>();
						listObjetosTelematicos.add(objetoTelematico);
						
						Envio envio = new Envio(usrBean, descripcion);
						envio.setDesignas(designas);
						envio.setEjgs(ejgs);
						
						if (envio.getEnviosBean() != null)
							envio.getEnviosBean().setIdEstado(new Integer(EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));
						
						// Bean envio
						EnvEnviosBean enviosBean = envio.getEnviosBean();
						// Preferencia del tipo de envio si el usuario tiene uno:
						enviosBean.setIdTipoEnvios(envioProgramadoBean.getIdTipoEnvios());
						enviosBean.setIdPlantillaEnvios(envioProgramadoBean.getIdPlantillaEnvios());
						
						if (envioProgramadoBean.getIdPlantilla() != null && !envioProgramadoBean.getIdPlantilla().equals("")) {
							enviosBean.setIdPlantilla(envioProgramadoBean.getIdPlantilla());
						} else {
							enviosBean.setIdPlantilla(null);
						}
						
						enviosBean.setIdTipoIntercambioTelematico(beanInforme.getIdTipoIntercambioTelematico());
						enviosBean.setIdEnvioProgramado(Long.valueOf(envioProgramadoBean.getIdEnvio()));
						envio.generarIntercambioTelematico(enviosBean, htPersonas ,listObjetosTelematicos);
						
					}
					
					
				
				} //Fin for plantillas
			
			}//Fin for claves destinatarios
		
//		}//Fin for destinatarios
	}	
	
	public void enviarInformeGenericoSms(UsrBean usrBean,
			EnvDestProgramInformesBean destProgramInfBean,
			EnvProgramInformesBean programInfBean, Vector vPlantillasInforme,
			EnvEnvioProgramadoBean envioProgramadoBean) throws ClsExceptions,
			SIGAException {

		Envio envio = new Envio(usrBean, envioProgramadoBean.getNombre());
		Hashtable datosInforme = new Hashtable();

		// Bean envio
		EnvEnviosBean enviosBean = envio.getEnviosBean();
		// pdm

		StringBuffer descripcionEnvios = new StringBuffer(
				enviosBean.getDescripcion());
		datosInforme.put("tipoDestinatario",
				destProgramInfBean.getTipoDestinatario());
		if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)) {
			datosInforme.put("idPersona", destProgramInfBean.getIdPersona()
					.toString());

			CenPersonaAdm persAdm = new CenPersonaAdm(this.getUsuario());
			descripcionEnvios.append(" ");
			descripcionEnvios.append(persAdm
					.obtenerNombreApellidos(destProgramInfBean.getIdPersona()
							.toString()));

		} else if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)) {
			datosInforme.put("idPersonaJG", destProgramInfBean.getIdPersona()
					.toString());
			ScsPersonaJGAdm persJGAdm = new ScsPersonaJGAdm(this.getUsuario());
			descripcionEnvios.append(" ");
			descripcionEnvios.append(persJGAdm.getNombreApellidos(
					destProgramInfBean.getIdPersona().toString(),
					destProgramInfBean.getIdInstitucion().toString()));

		} else if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)) {
			datosInforme.put("idProcurador", destProgramInfBean.getIdPersona()
					.toString());
			ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(
					this.getUsuario());
			descripcionEnvios.append(" ");
			Vector procuradorVector = procuradorAdm.busquedaProcurador(
					destProgramInfBean.getIdInstitucion().toString(),
					destProgramInfBean.getIdPersona().toString());
			Hashtable procuradorHashtable = (Hashtable) procuradorVector.get(0);
			descripcionEnvios.append(procuradorHashtable.get("NOMBRE"));
			descripcionEnvios.append(" ");
			descripcionEnvios.append(procuradorHashtable.get("APELLIDOS1"));
			descripcionEnvios.append(" ");
			descripcionEnvios.append(procuradorHashtable.get("APELLIDOS2"));

		} else if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)) {
			datosInforme.put("idPersona", destProgramInfBean.getIdPersona().toString());
			ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm(this.getUsuario());
			descripcionEnvios.append(" ");
			Vector juzgadoVector = juzgadoAdm.busquedaJuzgado(
					destProgramInfBean.getIdInstitucion().toString(),
					destProgramInfBean.getIdPersona().toString());
			Hashtable procuradorHashtable = (Hashtable) juzgadoVector.get(0);
			descripcionEnvios.append(procuradorHashtable.get("NOMBRE"));
		}

		enviosBean.setDescripcion(descripcionEnvios.toString());
		// trunco la descripción
		if (enviosBean.getDescripcion().length() > 200)
			enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,
					99));
		// Preferencia del tipo de envio si el usuario tiene uno:
		enviosBean.setIdTipoEnvios(envioProgramadoBean.getIdTipoEnvios());
		// if(envioProgramadoBean!=null &&
		// envioProgramadoBean.getIdTipoEnvios()!=null
		// &&!envioProgramadoBean.getIdTipoEnvios().toString().equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))
		envio.getEnviosBean().setIdEstado(
				new Integer(EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));

		enviosBean.setFechaProgramada(envioProgramadoBean.getFechaProgramada());
		enviosBean.setIdPlantillaEnvios(envioProgramadoBean
				.getIdPlantillaEnvios());
		if (envioProgramadoBean.getIdPlantilla() != null
				&& !envioProgramadoBean.getIdPlantilla().equals("")) {
			enviosBean.setIdPlantilla(envioProgramadoBean.getIdPlantilla());
		} else {
			enviosBean.setIdPlantilla(null);
		}

		Hashtable htClavesProgramacion = getClaves(programInfBean.getClaves());

		ArrayList alClavesDestinatario = destProgramInfBean
				.getClavesDestinatario();
		datosInforme.put("idioma", programInfBean.getIdioma().toString());
		datosInforme.put("idiomaExt", programInfBean.getIdiomaCodigoExt());
		datosInforme.put("idInstitucion", destProgramInfBean
				.getIdInstitucionPersona().toString());
		datosInforme.put("idTipoInforme", programInfBean.getIdTipoInforme());

		// (JTA) IDEA!!!!! Ahora mismo el idioma de las comunicaciones es el de
		// el usuario que la genera. si por necesidades se va a meter
		// en algun formulario el idioma seleccionable, se puede meter como
		// clave de la
		// programacion. de este modo el idioma inicial se macahacara con este
		// ultimo, por eso es
		// importante que el putAll este aqui y no antes.

		datosInforme.putAll(htClavesProgramacion);
		if (alClavesDestinatario == null) {
			for (int j = 0; j < vPlantillasInforme.size(); j++) {
				AdmInformeBean beanInforme = (AdmInformeBean) vPlantillasInforme
						.get(j);
				// boolean isSolicitantes = beanInforme.getASolicitantes()!=null
				// && beanInforme.getASolicitantes().equalsIgnoreCase("S");

				envio.generarEnvio(
						destProgramInfBean.getIdPersona().toString(),
						destProgramInfBean.getTipoDestinatario(), new Vector());

			}

		} else {
			List<ScsEjg> ejgs = new ArrayList<ScsEjg>();
			for (int i = 0; i < alClavesDestinatario.size(); i++) {
				Hashtable htClaves = (Hashtable) alClavesDestinatario.get(i);
				ejgs = new ArrayList<ScsEjg>();
				datosInforme.putAll(htClaves);
				if (programInfBean.getIdTipoInforme().equals(
						EnvioInformesGenericos.comunicacionesEjg)||programInfBean.getIdTipoInforme().equals(
								EnvioInformesGenericos.comunicacionesCAJG)) {
					datosInforme.put("sms", "S");
					datosInforme.put("generarInformeSinDireccion", "S");
					String idInstitucion = (String) datosInforme.get("idInstitucion");
					String anio = (String) datosInforme.get("anio");
					String idTipoEJG = (String) datosInforme.get("idTipoEJG");
					String numero = (String) datosInforme.get("numero");
					ScsEjg ejg = new ScsEjg();
					ejg.setIdinstitucion(Short.parseShort(idInstitucion));
					ejg.setAnio(Short.parseShort(anio));
					ejg.setIdtipoejg(Short.parseShort(idTipoEJG));
					ejg.setNumero(Long.parseLong(numero));
					ejgs.add(ejg);
					
					Hashtable htDatosInformeFinal = getDatosInformeFinal(
							datosInforme, usrBean);
					Vector datosInformeEjg = (Vector) htDatosInformeFinal
							.get("row");
					ScsEJGBean ejgBean = new ScsEJGBean();
					if(datosInformeEjg!=null && datosInformeEjg.size()>0)
						ejgBean.setOriginalHash((Hashtable) datosInformeEjg.get(0));
					for (int j = 0; j < vPlantillasInforme.size(); j++) {
						AdmInformeBean beanInforme = (AdmInformeBean) vPlantillasInforme
								.get(j);
						// boolean isSolicitantes =
						// beanInforme.getASolicitantes()!=null &&
						// beanInforme.getASolicitantes().equalsIgnoreCase("S");
						envio.setEjgs(ejgs);
						envio.generarEnvio(destProgramInfBean.getIdPersona()
								.toString(), destProgramInfBean
								.getTipoDestinatario(), ejgBean);

					}
				} else {
					for (int j = 0; j < vPlantillasInforme.size(); j++) {
						AdmInformeBean beanInforme = (AdmInformeBean) vPlantillasInforme
								.get(j);
						// boolean isSolicitantes =
						// beanInforme.getASolicitantes()!=null &&
						// beanInforme.getASolicitantes().equalsIgnoreCase("S");
						envio.setEjgs(ejgs);
						envio.generarEnvio(destProgramInfBean.getIdPersona()
								.toString(), destProgramInfBean
								.getTipoDestinatario(), new Vector());

					}

				}

			}
			

		}

	}

	/**
	 * Este metodo es el encargado de generar envios NO ordinarios de
	 * correos(e-mail, fax).
	 * 
	 * @param usrBean
	 * @param vDestProgramInfBean
	 *            Destinatarios del envio
	 * @param programInfBean
	 *            Programacion de informes genericos
	 * @param vPlantillasInforme
	 *            Informes a generar por destinatario
	 * @param envioProgramadoBean
	 *            Maestro de programacion de envios
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */

	public void enviarInformeGenerico(UsrBean usrBean,
			EnvDestProgramInformesBean destProgramInfBean,
			EnvProgramInformesBean programInfBean, Vector vPlantillasInforme,
			EnvEnvioProgramadoBean envioProgramadoBean) throws ClsExceptions,
			SIGAException {

		Envio envio = new Envio(usrBean, envioProgramadoBean.getNombre());
		Hashtable datosInforme = new Hashtable();
		Vector vDocumentos = null;

		// Bean envio
		EnvEnviosBean enviosBean = envio.getEnviosBean();
		enviosBean.setAcuseRecibo(envioProgramadoBean.getAcuseRecibo());
		// pdm

		StringBuffer descripcionEnvios = new StringBuffer(
				enviosBean.getDescripcion());
		datosInforme.put("tipoDestinatario",
				destProgramInfBean.getTipoDestinatario());
		if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)) {
			
			datosInforme.put("idPersona", destProgramInfBean.getIdPersona()
					.toString());

			CenPersonaAdm persAdm = new CenPersonaAdm(this.getUsuario());
			descripcionEnvios.append(" ");
			descripcionEnvios.append(persAdm
					.obtenerNombreApellidos(destProgramInfBean.getIdPersona()
							.toString()));

		} else if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)) {
			datosInforme.put("idPersonaJG", destProgramInfBean.getIdPersona()
					.toString());
			//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE SOLICITANTES EJG COMO DENUNCIANTES, QUE SON CLIENTES
			if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesExpedientes)){
				CenPersonaAdm persAdm = new CenPersonaAdm(this.getUsuario());
				descripcionEnvios.append(" ");
				descripcionEnvios.append(persAdm
						.obtenerNombreApellidos(destProgramInfBean.getIdPersona()
								.toString()));
			}else{
				ScsPersonaJGAdm persJGAdm = new ScsPersonaJGAdm(this.getUsuario());
				descripcionEnvios.append(" ");
				descripcionEnvios.append(persJGAdm.getNombreApellidos(
						destProgramInfBean.getIdPersona().toString(),
						destProgramInfBean.getIdInstitucion().toString()));
			}
		} else if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)) {
			datosInforme.put("idProcurador", destProgramInfBean.getIdPersona()
					.toString());
			
			descripcionEnvios.append(" ");
			
			
			//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE PROCURADOR COM PARTES QUE SON PERSONAS
			if(programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesExpedientes)){
				CenPersonaAdm persAdm = new CenPersonaAdm(this.getUsuario());
				descripcionEnvios.append(" ");
				descripcionEnvios.append(persAdm
						.obtenerNombreApellidos(destProgramInfBean.getIdPersona()
								.toString()));
			}else{
				ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(
						this.getUsuario());
				Vector procuradorVector = procuradorAdm.busquedaProcurador(
						destProgramInfBean.getIdInstitucion().toString(),
						destProgramInfBean.getIdPersona().toString());
				Hashtable procuradorHashtable = (Hashtable) procuradorVector.get(0);
				descripcionEnvios.append(procuradorHashtable.get("NOMBRE"));
				descripcionEnvios.append(" ");
				descripcionEnvios.append(procuradorHashtable.get("APELLIDOS1"));
				descripcionEnvios.append(" ");
				descripcionEnvios.append(procuradorHashtable.get("APELLIDOS2"));
			}

		} else if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)) {
			datosInforme.put("idPersona", destProgramInfBean.getIdPersona().toString());
			ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm(this.getUsuario());
			descripcionEnvios.append(" ");
			Vector juzgadoVector = juzgadoAdm.busquedaJuzgado(
					destProgramInfBean.getIdInstitucion().toString(),
					destProgramInfBean.getIdPersona().toString());
			Hashtable procuradorHashtable = (Hashtable) juzgadoVector.get(0);
			descripcionEnvios.append(procuradorHashtable.get("NOMBRE"));
		}else if (destProgramInfBean.getTipoDestinatario().equals(
				EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG)) {
			datosInforme.put("idContrario", destProgramInfBean.getIdPersona()
					.toString());
			//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE SOLICITANTES EJG COMO DENUNCIANTES, QUE SON CLIENTES
			
				ScsPersonaJGAdm persJGAdm = new ScsPersonaJGAdm(this.getUsuario());
				descripcionEnvios.append(" ");
				descripcionEnvios.append(persJGAdm.getNombreApellidos(
						destProgramInfBean.getIdPersona().toString(),
						destProgramInfBean.getIdInstitucion().toString()));
			
		}

		enviosBean.setDescripcion(descripcionEnvios.toString());
		// trunco la descripción
		if (enviosBean.getDescripcion().length() > 200)
			enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,
					99));
		// Preferencia del tipo de envio si el usuario tiene uno:
		enviosBean.setIdTipoEnvios(envioProgramadoBean.getIdTipoEnvios());
		if (envioProgramadoBean != null
				&& envioProgramadoBean.getIdTipoEnvios() != null
				&& !envioProgramadoBean.getIdTipoEnvios().toString()
						.equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))
			envio.getEnviosBean().setIdEstado(
					new Integer(EnvEnviosAdm.ESTADO_INICIAL));

		enviosBean.setFechaProgramada(envioProgramadoBean.getFechaProgramada());
		enviosBean.setIdPlantillaEnvios(envioProgramadoBean
				.getIdPlantillaEnvios());
		if (envioProgramadoBean.getIdPlantilla() != null
				&& !envioProgramadoBean.getIdPlantilla().equals("")) {
			enviosBean.setIdPlantilla(envioProgramadoBean.getIdPlantilla());
		} else {
			enviosBean.setIdPlantilla(null);
		}

		Hashtable htClavesProgramacion = getClaves(programInfBean.getClaves());

		ArrayList alClavesDestinatario = destProgramInfBean
				.getClavesDestinatario();

		datosInforme.put("idioma", programInfBean.getIdioma().toString());
		datosInforme.put("idiomaExt", programInfBean.getIdiomaCodigoExt());

		datosInforme.put("idInstitucion", destProgramInfBean
				.getIdInstitucionPersona().toString());
		datosInforme.put("idTipoInforme", programInfBean.getIdTipoInforme());

		if (programInfBean.getIdTipoInforme().equals(
				EnvioInformesGenericos.comunicacionesExpedientes)) {
			
			
			datosInforme.putAll(htClavesProgramacion);
			if (alClavesDestinatario == null) {
				if (vPlantillasInforme != null && vPlantillasInforme.size() > 0)
					vDocumentos = getDocumentosAEnviar(datosInforme,
							vPlantillasInforme, usrBean,
							EnvioInformesGenericos.docDocument,
							programInfBean.getIdTipoInforme());
			} else {
				vDocumentos = new Vector();
				ArrayList alFacturas = new ArrayList();
				for (int i = 0; i < alClavesDestinatario.size(); i++) {

					
						Hashtable htClaves = (Hashtable) alClavesDestinatario
								.get(i);
						datosInforme.putAll(htClaves);
						
								
						Envio.generarComunicacionExpediente((String)htClaves.get("idInstitucion"),
								new Integer((String)htClaves.get("idInstitucionTipoExp")), new Integer(
										(String)htClaves.get("idTipoExp")), new Integer((String)htClaves.get("numeroExpediente")),
								new Integer((String)htClaves.get("anioExpediente")), destProgramInfBean.getIdPersona().toString(), usrBean);

						
						vDocumentos.addAll(getDocumentosAEnviar(datosInforme,
								vPlantillasInforme, usrBean,
								EnvioInformesGenericos.docDocument,
								programInfBean.getIdTipoInforme()));
					
				}
				
			}
			
			
			
			// Genera el envio:
			envio.generarEnvio(destProgramInfBean.getIdPersona().toString(),
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA, vDocumentos);
			
		} else if (programInfBean.getIdTipoInforme().equals(
				EnvioInformesGenericos.comunicacionesAvisoExpedientes)) {
			datosInforme.putAll(htClavesProgramacion);
			// Genera el envio:
			ExpAlertaAdm alertaAdm = new ExpAlertaAdm(usrBean);
			Hashtable hashAlerta = new Hashtable();
			for (int i = 0; i < alClavesDestinatario.size(); i++) {
				Hashtable htClave = (Hashtable) alClavesDestinatario.get(i);
				if (htClave.get("idInstitucionTipoExpediente") != null) {
					hashAlerta
							.put(ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE,
									(String) htClave
											.get("idInstitucionTipoExpediente"));

				} else if (htClave.get("idTipoExpediente") != null) {
					hashAlerta.put(ExpAlertaBean.C_IDTIPOEXPEDIENTE,
							(String) htClave.get("idTipoExpediente"));

				} else if (htClave.get("idInstitucion") != null) {
					hashAlerta.put(ExpAlertaBean.C_IDINSTITUCION,
							(String) htClave.get("idInstitucion"));

				} else if (htClave.get("idAlerta") != null) {
					hashAlerta.put(ExpAlertaBean.C_IDALERTA,
							(String) htClave.get("idAlerta"));

				} else if (htClave.get("numeroExpediente") != null) {
					hashAlerta.put(ExpAlertaBean.C_NUMEROEXPEDIENTE,
							(String) htClave.get("numeroExpediente"));

				} else if (htClave.get("anioExpediente") != null) {
					hashAlerta.put(ExpAlertaBean.C_ANIOEXPEDIENTE,
							(String) htClave.get("anioExpediente"));

				}

			}

			Vector alertaVector = alertaAdm.selectByPK(hashAlerta);
			ExpAlertaBean alertaBean = (ExpAlertaBean) alertaVector.get(0);
			ExpFasesAdm fasesAdm = new ExpFasesAdm(usrBean);
			ExpEstadosAdm estadosAdm = new ExpEstadosAdm(usrBean);
			ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm(
					usrBean);
			if (alertaBean.getIdFase() != null) {
				hashAlerta.put(ExpAlertaBean.C_IDFASE, alertaBean.getIdFase());
				Vector faseVector = fasesAdm.selectByPK(hashAlerta);
				alertaBean.setFase((ExpFasesBean) faseVector.get(0));
			}
			if (alertaBean.getIdEstado() != null) {
				hashAlerta.put(ExpAlertaBean.C_IDESTADO, alertaBean.getIdEstado());
				Vector estadoVector = estadosAdm.selectByPK(hashAlerta);
				alertaBean.setEstado((ExpEstadosBean) estadoVector.get(0));
			}
			Vector tipoExpVector = tipoExpedienteAdm.selectByPK(hashAlerta);
			alertaBean.setTipoExpediente((ExpTipoExpedienteBean) tipoExpVector
					.get(0));

			envio.generarEnvio(destProgramInfBean.getIdPersona().toString(),
					destProgramInfBean.getTipoDestinatario(), alertaBean);
		} else {

			// (JTA) IDEA!!!!! Ahora mismo el idioma de las comunicaciones es el
			// de el usuario que la genera. si por necesidades se va a meter
			// en algun formulario el idioma seleccionable, se puede meter como
			// clave de la
			// programacion. de este modo el idioma inicial se macahacara con
			// este ultimo, por eso es
			// importante que el putAll este aqui y no antes.

			datosInforme.putAll(htClavesProgramacion);
			List<ScsDesigna> designas = new ArrayList<ScsDesigna>();
			List<ScsEjg> ejgs = new ArrayList<ScsEjg>();
			if (alClavesDestinatario == null) {
				// datosInforme.putAll(htClavesDestinatario);
				if (vPlantillasInforme != null && vPlantillasInforme.size() > 0)
					vDocumentos = getDocumentosAEnviar(datosInforme,
							vPlantillasInforme, usrBean,
							EnvioInformesGenericos.docDocument,
							programInfBean.getIdTipoInforme());
				
			} else {
				// List aClavesMultiple =
				// (ArrayList)htClavesDestinatario.get("clavesMultiple");
				// datosInforme.putAll(htClavesDestinatario);
				vDocumentos = new Vector();
				ArrayList alFacturas = new ArrayList();
				
				for (int i = 0; i < alClavesDestinatario.size(); i++) {

					if (!programInfBean.getIdTipoInforme().equals(
							EnvioInformesGenericos.comunicacionesMorosos)) {
						Hashtable htClaves = (Hashtable) alClavesDestinatario
								.get(i);
						datosInforme.putAll(htClaves);
						if (programInfBean.getIdTipoInforme().equals(
								EnvioInformesGenericos.comunicacionesDesigna)) {
							String idInstitucion = (String) datosInforme.get("idInstitucion");
							String anio = (String) datosInforme.get("anio");
							String idTurno = (String) datosInforme.get("idTurno");
							String numero = (String) datosInforme.get("numero");
							ScsDesigna designa = new ScsDesigna();
							designa.setIdinstitucion(Short.parseShort(idInstitucion));
							designa.setAnio(Short.parseShort(anio));
							designa.setIdturno(Integer.parseInt(idTurno));
							designa.setNumero(Long.parseLong(numero));
							designas.add(designa);
						}else if (programInfBean.getIdTipoInforme().equals(
								EnvioInformesGenericos.comunicacionesEjg)||programInfBean.getIdTipoInforme().equals(
										EnvioInformesGenericos.comunicacionesCAJG)) {
							
							
							// BEGIN BNS INC_08446_SIGA SUSTITUIMOS EL ID DE LA TABLA DE EJG POR EL CÓDIGO DE EJG
							String idInstitucion = (String) datosInforme.get("idInstitucion");
							String anio = (String) datosInforme.get("anio");
							String idTipoEJG = (String) datosInforme.get("idTipoEJG");
							String numero = (String) datosInforme.get("numero");
							ScsEjg ejg = new ScsEjg();
							ejg.setIdinstitucion(Short.parseShort(idInstitucion));
							ejg.setAnio(Short.parseShort(anio));
							ejg.setIdtipoejg(Short.parseShort(idTipoEJG));
							ejg.setNumero(Long.parseLong(numero));
							ejgs.add(ejg);
						}
						
						
						vDocumentos.addAll(getDocumentosAEnviar(datosInforme,
								vPlantillasInforme, usrBean,
								EnvioInformesGenericos.docDocument,
								programInfBean.getIdTipoInforme()));
					} else {
						Hashtable htClaves = (Hashtable) alClavesDestinatario
								.get(i);
						String idFactura = (String) htClaves.get("idFactura");
						alFacturas.add(idFactura);

					}

				}
				if (programInfBean.getIdTipoInforme().equals(
						EnvioInformesGenericos.comunicacionesMorosos)) {
					datosInforme.put("idFacturas", alFacturas);
					vDocumentos.addAll(getDocumentosAEnviar(datosInforme,
							vPlantillasInforme, usrBean,
							EnvioInformesGenericos.docDocument,
							programInfBean.getIdTipoInforme()));
				}
			}

			// Genera el envio:
			String tipoDestinatario = destProgramInfBean.getTipoDestinatario();
			
			if(tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG))
				tipoDestinatario = EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG;
			
			envio.setDesignas(designas);
			envio.setEjgs(ejgs);
			
			if (programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesEjg)||programInfBean.getIdTipoInforme().equals(EnvioInformesGenericos.comunicacionesCAJG)) {
				datosInforme.put("aSolicitantes", "N");
				datosInforme.put("tipoDestinatarioInforme",AdmInformeBean.TIPODESTINATARIO_CENPERSONA);
				datosInforme.put("generarInformeSinDireccion", "S");

				
				Hashtable htDatosInformeFinal = getDatosInformeFinal(datosInforme, usrBean);
				
				
				
				Vector datosInformeEjg = (Vector) htDatosInformeFinal.get("row");
				ScsEJGBean ejgBean = new ScsEJGBean();
				if(datosInformeEjg!=null && datosInformeEjg.size()>0)
					ejgBean.setOriginalHash((Hashtable) datosInformeEjg.get(0));

				envio.generarEnvioBean(destProgramInfBean.getIdPersona().toString(), destProgramInfBean.getTipoDestinatario(), vDocumentos, ejgBean);
				
			} else {
			
			
			
				envio.generarEnvio(destProgramInfBean.getIdPersona().toString(),tipoDestinatario, vDocumentos);
			}
		}

		// Generamos la comunicacion de morosos
		if (programInfBean.getIdTipoInforme().equals(
				EnvioInformesGenericos.comunicacionesMorosos)) {
			ArrayList alFacturas = (ArrayList) datosInforme.get("idFacturas");
			envio.generarComunicacionMoroso(destProgramInfBean.getIdPersona()
					.toString(), vDocumentos, alFacturas, programInfBean
					.getIdInstitucion().toString(), enviosBean.getDescripcion());
		}
		

	}

	/**
	 * Este metodo convierte una cadena de caracteres(pepe=2#juan=6)--> en un
	 * hashtable de claves({[pepe,6],[juan,4]})
	 * 
	 * @param claves
	 * @return
	 * @throws ClsExceptions
	 */
	private Hashtable getClaves(String claves) throws ClsExceptions {
		Hashtable htClaves = new Hashtable();
		GstStringTokenizer st = new GstStringTokenizer(claves, "##");
		GstStringTokenizer stClaveMultiple = null;
		List aDatosMultiple = new ArrayList();
		while (st.hasMoreTokens()) {
			String dupla = st.nextToken();
			String d[] = dupla.split("==");
			// Cuando no vienen datos completos es porque hay alguna
			// funcionalidad que no requiere estos datos
			// ejemplo:idPersona del Certificado de IRPF. cuando se saca
			// individulamente
			// desde la icha colegial si tiene persona. cuando se saca desde la
			// opcion de menu de
			// informes no porque se supone que es para todos los colegiados que
			// hayan tenido pagos el
			// anyo del informe

			if (d.length > 1) {
				String clavesMultiples = d[1];
				stClaveMultiple = new GstStringTokenizer(clavesMultiples, "||");
				Hashtable aux = new Hashtable();
				while (stClaveMultiple.hasMoreTokens()) {

					String dupla2 = stClaveMultiple.nextToken();

					String d2[] = dupla2.split("=");

					if (d2.length > 1) {

						// htClaves.put(d2[0], d2[1]);

						aux.put(d2[0], d2[1]);

					}

				}
				if (aux.size() > 0)
					aDatosMultiple.add(aux);

				htClaves.put(d[0], d[1]);

			}
		}
		if (aDatosMultiple.size() > 0)
			htClaves.put("clavesMultiple", aDatosMultiple);

		return htClaves;
	}

	/**
	 * Metodo que convierte una cadena de caracteres separados por
	 * @@(COB1@@COB2) en un array de AdmInformeBean([COB1,,COB2])
	 * 
	 * @param plantillas
	 * @param idInstitucion
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */

	public Vector getPlantillasInforme(String plantillas,String separador,  UsrBean usr)
			throws ClsExceptions, SIGAException {

		String d[] = plantillas.split(separador);

		Vector vPlantillas = new Vector();
		AdmInformeAdm admInforme = new AdmInformeAdm(usr);

		for (int i = 0; i < d.length; i++) {
			String[] idsPlantilla = d[i].split(",");
			AdmInformeBean beanInforme = admInforme.obtenerInforme(
					idsPlantilla[1], idsPlantilla[0]);
			vPlantillas.add(beanInforme);

		}
		return vPlantillas;

	}

	public Vector<AdmInformeBean> getInformes(List informesList, UsrBean usr)
			throws ClsExceptions, SIGAException {
		Vector vPlantillas = new Vector();
		AdmInformeAdm admInforme = new AdmInformeAdm(usr);
		for (int i = 0; i < informesList.size(); i++) {
			String[] idsInforme = ((String) informesList.get(i)).split(",");
			AdmInformeBean beanInforme = admInforme.obtenerInforme(
					idsInforme[1], idsInforme[0]);
			vPlantillas.add(beanInforme);

		}
		return vPlantillas;

	}

	public boolean esAlgunaASolicitantes(Vector plantillas)
			throws ClsExceptions, SIGAException {

		for (int i = 0; i < plantillas.size(); i++) {
			AdmInformeBean plantillaInforme = (AdmInformeBean) plantillas
					.get(i);
			if (plantillaInforme.getASolicitantes().equals("S")) {
				return true;
			}
		}
		return false;

	}

	public boolean esAlgunaASolicitantes(String idInstitucion, String plantillas)
			throws ClsExceptions, SIGAException {
		AdmInformeAdm informeAdm = new AdmInformeAdm(this.getUsuario());
		String plan[] = plantillas.split("##");
		for (int i = 0; i < plan.length; i++) {
			String pl = plan[i];
			AdmInformeBean plantillaInforme = informeAdm.obtenerInforme(
					idInstitucion, pl);
			if (plantillaInforme.getASolicitantes().equals("S")) {
				return true;
			}
		}
		return false;

	}

	// GENEXP@@GENEXP2
	/**
	 * Metodo que nos convierte una cadena de caracteres
	 * separados(pepe||juan||pedro) por un string en un vector de
	 * datos([pepe,juan,pedro])
	 * 
	 * @param cadenaUnida
	 * @param separador
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */

	public Vector getDatosSeparados(String cadenaUnida, String separador)
			throws ClsExceptions, SIGAException {

		String d[] = cadenaUnida.split(separador);
		Vector vDatos = new Vector();

		for (int i = 0; i < d.length; i++) {
			String idDato = d[i];
			vDatos.add(idDato);

		}
		return vDatos;

	}

	/**
	 * Metodo que nos incluye un vector de plantillas transformado en texto
	 * plano y nos lo incluye en el texto plano a tratar delos datos a enviar
	 * 
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
	 * Funcion principal de la clase que nos gestiona el envio o descarga de los
	 * informes genericos. En caso de que sea descargar es el metodo que se
	 * encarga que generar y empaquetar los archivos necesarios. En el caso de
	 * que sea enviar, es el encargado de preparar los datos que mas tarde se
	 * manejaran. Este metodo se invocara desde InformesGenericosAction y
	 * generara los informes o preparada los datos para los futuros envios. En
	 * este caso el encargado de invocar la genetacion de envios sera
	 * DefinirEnviosAction
	 */
	public File getInformeGenerico(InformesGenericosForm informeGenerico,
			String idsesion, UsrBean usr, boolean isEnviar,
			boolean isPermisoEnvio) throws SIGAException, Exception {
		File ficheroSalida = null; // Fichero para devolver

		// comprobando que hay que generar algun informe
		if (informeGenerico.getDatosInforme() == null)
			throw new SIGAException("messages.informes.ningunInformeGenerado");
		if (informeGenerico.getDatosInforme().trim().equals(""))
			throw new SIGAException("messages.informes.ningunInformeGenerado");

		if (isEnviar && isPermisoEnvio) { // se prepara el envio, pero nada mas
			setDatosEnvios(getDatosAEnviar(informeGenerico,
					getDatosSeparados(informeGenerico.getIdInforme(), "##")));
			ficheroSalida = null;
		} else { // se generan los ficheros para descargar, comprueba cpago
					// cuando se accede por medio de abonos a las cartas de pago
		// if(!isPermisoEnvio ||
		// informeGenerico.getIdTipoInforme().equalsIgnoreCase("CPAGO") ||
		// informeGenerico.getIdTipoInforme().equalsIgnoreCase("CFACT"))
		// informeGenerico.setDatosInforme(getDatosAEnviar(informeGenerico,getDatosSeparados(informeGenerico.getIdInforme(),
		// "##")));
		// if(informeGenerico.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesEjg)
		// ||
		// informeGenerico.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesJustificacion)){
			String datos = getDatosAEnviar(informeGenerico,
					getDatosSeparados(informeGenerico.getIdInforme(), "##"));
			informeGenerico.setDatosInforme(datos);
			// }

			Vector informesRes = new Vector();
			Vector datosFormulario = this.obtenerDatosFormulario(informeGenerico);

			// En el caso de que la iteracion de los datos del informe(tablas
			// 1-->n),
			// la clave iterante sera la clave por la que iteraran las personas.
			// Este sera el caso de que los docuemntos tengan regiones
			String claveIterante = informeGenerico.getClavesIteracion();
			if (claveIterante != null && !claveIterante.equals("")) {
				datosFormulario = setCamposIterantes(datosFormulario,
						claveIterante);
			}

			Vector vPlantillas = null;
			Hashtable datosInforme = (Hashtable) datosFormulario.get(0);
			String idTipoInforme = (String) datosInforme.get("idTipoInforme");
			if (idTipoInforme
					.equals(EnvioInformesGenericos.comunicacionesListadoGuardias)) {
				Hashtable datosTotales = new Hashtable();
				datosTotales.put("datosFormulario", datosFormulario);
				if (datosInforme != null) {
					// cargando plantillas una sola vez
					if (vPlantillas == null) {
						vPlantillas = getPlantillasInforme(
								informeGenerico.getIdInforme(),"##", usr);
					}
					informesRes.addAll(this.getDocumentosAEnviar(datosTotales,
							vPlantillas, usr, EnvioInformesGenericos.docFile,
							idTipoInforme));
				}
			} else {
				for (int j = 0; j < datosFormulario.size(); j++) {

					datosInforme = (Hashtable) datosFormulario.get(j);
					idTipoInforme = (String) datosInforme.get("idTipoInforme");

					if (datosInforme != null) {
						// cargando plantillas una sola vez
						if (vPlantillas == null) {
							vPlantillas = getPlantillasInforme(
									(String) datosInforme.get("plantillas"),"@@",
									usr);
						}

			
							informesRes.addAll(this.getDocumentosAEnviar(
									datosInforme, vPlantillas, usr,
									EnvioInformesGenericos.docFile,
									idTipoInforme));
						
					}
				}

			}// else listaGuardias
				// como es para descargar, se generan los ficheros y se
				// comprimen en ZIP
			if (informesRes.size() == 0)
				throw new SIGAException(
						"messages.informes.ficheroVacio");
			else if (informesRes.size() == 1)
				ficheroSalida = (File) informesRes.get(0);
			else {
				AdmTipoInformeAdm admT = new AdmTipoInformeAdm(usr);
				AdmTipoInformeBean beanT = admT
						.obtenerTipoInforme(informeGenerico.getIdTipoInforme());
				ArrayList ficherosPDF = new ArrayList();
				for (int i = 0; i < informesRes.size(); i++) {
					File f = (File) informesRes.get(i);
					ficherosPDF.add(f);
				}

				String nombreFicheroZIP = UtilidadesBDAdm.getFechaCompletaBD("")
								.replaceAll("/", "").replaceAll(":", "")
								.replaceAll(" ", "").replaceAll("-", "");
				ReadProperties rp = new ReadProperties(
						SIGAReferences.RESOURCE_FILES.SIGA);
				String rutaServidorDescargasZip = rp
						.returnProperty("informes.directorioFisicoSalidaInformesJava")
						+ rp.returnProperty("informes.directorioPlantillaInformesJava")
						+ ClsConstants.FILE_SEP
						+ usr.getLocation()
						+ ClsConstants.FILE_SEP + "temp" 
						+ ClsConstants.FILE_SEP
						+ idsesion.replaceAll("!", "")  + File.separatorChar;
				File ruta = new File(rutaServidorDescargasZip);
				ruta.mkdirs();
				Plantilla.doZip(rutaServidorDescargasZip, nombreFicheroZIP, ficherosPDF);
				ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
			}
		}

		
		return ficheroSalida;
	} // getInformeGenerico()

	/**
	 * Metodo que nos agrupa en un vector de hastable los datos por los que
	 * itera. Se incluira en un elemento de este hashtable de tipo ArtrayList y
	 * con la clave que es el PLURAL de la clave iterante
	 * [idpersona=1,idFactura=
	 * 22],[idPersona=1,idFactura=33]-->{idPersona=1,[idFactura=2,idFactura=33]}
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

	private Vector setCamposIterantes(Vector vCampos, ArrayList clavesIterante,
			String nombre) throws ClsExceptions {
		Hashtable htPersona = new Hashtable();
		ArrayList alClaves = null;
		Vector vCamposConArray = new Vector();
		Map htClave = null;

		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			String idPersona = (String) ht.get("idPersona");
			htClave = new Hashtable();
			for (int j = 0; j < clavesIterante.size(); j++) {
				String clave = (String) clavesIterante.get(j);
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
			UsrBean usr, int tipoPlantillaInforme) throws SIGAException,
			ClsExceptions {
		File f = null;
		Hashtable htDatosInformeClone = (Hashtable) htDatosInforme.clone();
		switch (tipoPlantillaInforme) {
		case tipoPlantillaWord:
			f = getInformeGenericoWord(beanInforme, htDatosInformeClone, idiomaExt,
					identificador, usr);
			break;
		case tipoPlantillaFo:
			if (beanInforme.getIdTipoInforme().equals(
					comunicacionesPagoColegiados)) {
				InformeColegiadosPagos infColegiadoFo = new InformeColegiadosPagos();
				htDatosInformeClone = (Hashtable) htDatosInformeClone.get("row");
				f = infColegiadoFo.getInformeGenericoFo(beanInforme,
						htDatosInformeClone, idiomaExt, identificador, usr);

			} else if (beanInforme.getIdTipoInforme().equals(
					comunicacionesFacturacionesColegiados)) {
				InformeColegiadosFacturaciones infColegiadoFo = new InformeColegiadosFacturaciones();
				htDatosInformeClone = (Hashtable) htDatosInformeClone.get("row");
				f = infColegiadoFo.getInformeGenericoFo(beanInforme,
						htDatosInformeClone, idiomaExt, identificador, usr);
			}else if (beanInforme.getIdTipoInforme().equals(
					comunicacionesFacturaRectificativa)) {
				InformeAbono informeAbono = new InformeAbono();
				htDatosInformeClone = (Hashtable) htDatosInformeClone.get("row");
				f = informeAbono.getInformeGenericoFo(beanInforme,
						htDatosInformeClone, idiomaExt, identificador, usr);
			}
			break;

		}
		return f;

	}
	
	public File getPlantillaGenerica(AdmInformeBean beanInforme, String idioma, String extension)throws SIGAException{
		// --- acceso a paths y nombres
		ReadProperties rp = new ReadProperties(
				SIGAReferences.RESOURCE_FILES.SIGA);
		
		String 	idiomaExt="";
		switch (Integer.parseInt(idioma)) {
			case 1:  idiomaExt="ES"; break;
			case 2:  idiomaExt="CA"; break;
			case 3:  idiomaExt="EU"; break;
			case 4:  idiomaExt="GL"; break;	
		}	
		
		StringBuffer rutaPlantilla = new StringBuffer();
		rutaPlantilla.append(rp.returnProperty("informes.directorioFisicoPlantillaInformesJava"));
		rutaPlantilla.append(rp.returnProperty("informes.directorioPlantillaInformesJava"));

		
		// //////////////////////////////////////////////
		// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS
		
//		Le ageragamos el lugar donde esta los informes generico
		rutaPlantilla.append(ClsConstants.FILE_SEP);
		//Si no trae institucion le ponemos general, sino la suya personalizada
		if (beanInforme.getIdInstitucion() == null||beanInforme.getIdInstitucion().compareTo(Integer.valueOf(0)) == 0) {
			rutaPlantilla.append("2000");
		} else {
			rutaPlantilla.append(beanInforme.getIdInstitucion().toString());
			
		}
		rutaPlantilla.append(ClsConstants.FILE_SEP);
		rutaPlantilla.append(beanInforme.getDirectorio()); 
		rutaPlantilla.append(ClsConstants.FILE_SEP);
		//La sumamos el nombre de la plantilla
		rutaPlantilla.append(beanInforme.getNombreFisico());
		rutaPlantilla.append("_");
		rutaPlantilla.append(idiomaExt); 
		rutaPlantilla.append(".");
		rutaPlantilla.append(extension);
		
		return new File(rutaPlantilla.toString());
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
		ReadProperties rp = new ReadProperties(
				SIGAReferences.RESOURCE_FILES.SIGA);
		// ReadProperties rp = new ReadProperties("SIGA.properties");
		String rutaPlantilla = rp
				.returnProperty("informes.directorioFisicoPlantillaInformesJava")
				+ rp.returnProperty("informes.directorioPlantillaInformesJava");
		String rutaAlmacen = rp
				.returnProperty("informes.directorioFisicoSalidaInformesJava")
				+ rp.returnProperty("informes.directorioPlantillaInformesJava");
		// //////////////////////////////////////////////
		// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS
		String carpetaInstitucion = "";
		if (beanInforme.getIdInstitucion() == null
				|| beanInforme.getIdInstitucion().compareTo(Integer.valueOf(0)) == 0) {
			carpetaInstitucion = "2000";
		} else {
			carpetaInstitucion = "" + beanInforme.getIdInstitucion();
		}

		String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP
				+ carpetaInstitucion + ClsConstants.FILE_SEP
				+ beanInforme.getDirectorio() + ClsConstants.FILE_SEP;
		String nombrePlantilla = beanInforme.getNombreFisico() + "_"
				+ idiomaExt + ".doc";
		String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP
				+ carpetaInstitucion + ClsConstants.FILE_SEP
				+ beanInforme.getDirectorio();

		File crear = new File(rutaAlm);
		if (!crear.exists())
			crear.mkdirs();

		MasterWords masterWord;
		Document documento;
		try {
			masterWord = new MasterWords(rutaPl + nombrePlantilla);
			documento = masterWord.nuevoDocumento();
		} catch (Exception fe) {
			String recurso = UtilidadesString.getMensajeIdioma(usr.getLanguage(), "messages.informes.noPlantillas");
			throw new SIGAException(recurso+" La plantilla es: " +nombrePlantilla);
		}

		Iterator iteDatos = htDatosInforme.keySet().iterator();
		while (iteDatos.hasNext()) {
			String clave = (String) iteDatos.next();
			if (!clave.equals("row")){ 
				// Estos son regiones
				Vector vDatosRegion = (Vector) htDatosInforme.get(clave);
				documento = masterWord.sustituyeRegionDocumento(documento,
						clave, vDatosRegion);
				iteDatos.remove();

			}

		}
		iteDatos = htDatosInforme.keySet().iterator();
		
		while (iteDatos.hasNext()) {
			String clave = (String) iteDatos.next();
//			if (clave.equals("row")) {
				Object oDatosInforme = (Object) htDatosInforme.get(clave);
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
//			} 
		}
		

//		identificador = identificador + ".doc";
		if(beanInforme.getTipoformato()!=null && beanInforme.getTipoformato().equals("P"))
			identificador= identificador + ".pdf";
		else
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
	 * Metodo que nos informa si de los datos de envios son para un unico
	 * colegiado
	 * 
	 * @param form
	 * @return
	 * @throws ClsExceptions
	 */
	private String getIdColegiadoUnico(DefinirEnviosForm form)
			throws ClsExceptions {
		MasterReport masterReport = new MasterReport();
		Vector vCampos = masterReport.obtenerDatosFormulario(form);
		String idPersona = getIdColegiadoUnico(vCampos);

		return idPersona;

	}

	public String getIdColegiadoUnico(Vector vCampos) {
		Hashtable htPersonas = new Hashtable();
		String idPersona = null;
		String idInstitucion = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			idPersona = (String) ht.get("idPersona");
			idInstitucion = (String) ht.get("idInstitucion");
			String key = idPersona + "||" + idInstitucion;
			if (!htPersonas.containsKey(key) && i != 0) {
				idPersona = null;
				break;

			} else {
				if (idPersona == null)
					return null;
				htPersonas.put(key, idPersona);

			}

		}

		return idPersona;

	}
//	
//	private List getDestinatariosExpedientes(Vector vCampos, UsrBean userBean)
//			throws ClsExceptions, SIGAException {
//		ExpDenunciadoAdm denunciadoAdm = new ExpDenunciadoAdm(	userBean);
//		
//		ExpExpedienteBean beanExp = null;
//		List alSolicitantes = new ArrayList();
//		for (int i = 0; i < vCampos.size(); i++) {
//			Hashtable ht = (Hashtable) vCampos.get(i);
//			String idInstitucion = (String) ht.get("idInstitucion");
//			String anioExpediente = (String) ht.get("anioExpediente");
//			String numeroExpediente = (String) ht.get("numeroExpediente");
//			String idTipoExp = (String) ht.get("idTipoExp");
//			String idInstitucionTipoExp = (String) ht.get("idInstitucionTipoExp");
//			beanExp = new ExpExpedienteBean();
//			beanExp.setIdInstitucion(new Integer(idInstitucion));
//			beanExp.setIdInstitucion_tipoExpediente(new Integer(idInstitucionTipoExp));
//			beanExp.setIdTipoExpediente(new Integer(idTipoExp));
//			beanExp.setNumeroExpediente(new Integer(numeroExpediente));
//			beanExp.setAnioExpediente(new Integer(anioExpediente));
//			
//			List denunciadosVector = denunciadoAdm.getDenunciados(beanExp);
//			if (denunciadosVector != null)
//				alSolicitantes.addAll(denunciadosVector);
//			if (alSolicitantes.size() > 1) {
//				break;
//			}
//
//		}
//		return alSolicitantes;
//
//	}
	
	public Hashtable getDestinatariosExpedientes(Vector vCampos) {

		Hashtable htPersonas = new Hashtable();
		List denunciados = null;
		List denunciantes = null;
		List partes = null;
		String idInstitucion = null;
		Vector solicitantes = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			// Colegiado Tramitador
			denunciados = (List) ht.get("denunciados");
			denunciantes = (List) ht.get("denunciantes");
			partes = (List) ht.get("partes");
			
			if (denunciados != null) {
				for (int j = 0; j < denunciados.size(); j++) {
					ExpDenunciadoBean denunciadoBean = (ExpDenunciadoBean) denunciados.get(j);
					htPersonas.put(denunciadoBean.getIdPersona().toString(),EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
				}
			}
			//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE SOLICITANTES EJG COMO DENUNCIANTES
			if (denunciantes != null) {
				for (int j = 0; j < denunciantes.size(); j++) {
					ExpDenuncianteBean denuncianteBean = (ExpDenuncianteBean) denunciantes.get(j);
					htPersonas.put(denuncianteBean.getIdPersona().toString(),EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);
				}
			}
			//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE PROCURADOR COM PARTES
			if (partes != null) {
				for (int j = 0; j < partes.size(); j++) {
					ExpPartesBean partesBean = (ExpPartesBean) partes.get(j);
					htPersonas.put(partesBean.getIdPersona().toString(),EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR);
				}
			}
			

//			if (solicitantes != null) {
//				for (int j = 0; j < solicitantes.size(); j++) {
//					Hashtable personaHashtable = (Hashtable) solicitantes
//							.get(j);
//					String idPersonaJG = (String) personaHashtable
//							.get("IDPERSONA");
//					htPersonas.put(idPersonaJG,
//							EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);
//				}
//			}

		}

		return htPersonas;

	}
	
	public void setPersonasExpedientes(Vector vCampos, UsrBean userBean)
			throws ClsExceptions, SIGAException {
		//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE PROCURADOR COMO PARTES
		//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE SOLICITANTESEJG COMO DENUNCIANTES
		ExpDenunciadoAdm denunciadoAdm = new ExpDenunciadoAdm(	userBean);
		ExpDenuncianteAdm denuncianteAdm = new ExpDenuncianteAdm(userBean);
		ExpPartesAdm partesAdm = new ExpPartesAdm(userBean);
		
		List auxiliar = new ArrayList();
		ExpExpedienteBean beanExp = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			String idInstitucion = (String) ht.get("idInstitucion");
			String anioExpediente = (String) ht.get("anioExpediente");
			String numeroExpediente = (String) ht.get("numeroExpediente");
			String idTipoExp = (String) ht.get("idTipoExp");
			String idInstitucionTipoExp = (String) ht.get("idInstitucionTipoExp");
			beanExp = new ExpExpedienteBean();
			
			beanExp = new ExpExpedienteBean();
			beanExp.setIdInstitucion(new Integer(idInstitucion));
			beanExp.setIdInstitucion_tipoExpediente(new Integer(idInstitucionTipoExp));
			beanExp.setIdTipoExpediente(new Integer(idTipoExp));
			beanExp.setNumeroExpediente(new Integer(numeroExpediente));
			beanExp.setAnioExpediente(new Integer(anioExpediente));
			List denunciados = denunciadoAdm.getDenunciados(beanExp);
			ht.put("denunciados", denunciados);
			List denunciantes = denuncianteAdm.getDenunciantes(beanExp);
			ht.put("denunciantes", denunciantes);
			List partes = partesAdm.getPartes(beanExp);
			ht.put("partes", partes);

		}

	}
	
	public Hashtable getDestinatariosEjg(Vector vCampos) {

		Hashtable htPersonas = new Hashtable();
		Vector personasDesignasEjg = null;
		String idInstitucion = null;
		Vector solicitantes = null;
		Vector contrarios = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			// Colegiado Tramitador
			personasDesignasEjg = (Vector) ht.get("personasDesignasEjg");
			solicitantes = (Vector) ht.get("solicitantesEjg");
			contrarios = (Vector) ht.get("contrariosEjg");
			if (personasDesignasEjg != null) {
				for (int j = 0; j < personasDesignasEjg.size(); j++) {
					Hashtable personaHashtable = (Hashtable) personasDesignasEjg
							.get(j);
					String idPersona = (String) personaHashtable
							.get("IDPERSONA");
					htPersonas.put(idPersona,
							EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
				}
			}

			if (solicitantes != null) {
				for (int j = 0; j < solicitantes.size(); j++) {
					Hashtable personaHashtable = (Hashtable) solicitantes
							.get(j);
					String idPersonaJG = (String) personaHashtable
							.get("IDPERSONA");
					htPersonas.put(idPersonaJG,
							EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);
				}
			}
			
			if (contrarios != null) {
				for (int j = 0; j < contrarios.size(); j++) {
					Hashtable personaHashtable = (Hashtable) contrarios
							.get(j);
					String idPersonaJG = (String) personaHashtable
							.get("IDPERSONA");
					htPersonas.put(idPersonaJG,	EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG);
				}
			}
			

		}

		return htPersonas;

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
	private String getFechaProgramada(String fechaFormulario, Locale locale,
			UsrBean userBean) throws ClsExceptions {
		// obtener fechaProgramada
		String fechaProgramada = null;
		String fechaProg = fechaFormulario + " " + new Date().getHours() + ":"
				+ new Date().getMinutes() + ":" + new Date().getSeconds();

		String language = userBean.getLanguage();
		String format = language.equalsIgnoreCase("EN") ? ClsConstants.DATE_FORMAT_LONG_ENGLISH
				: ClsConstants.DATE_FORMAT_LONG_SPANISH;
		GstDate gstDate = new GstDate();
		if (fechaProg != null && !fechaProg.equals("")) {
			Date date = gstDate.parseStringToDate(fechaProg, format, locale);
			// A peticion de Luis pedro retraso 15 minutos el envio
			date.setTime(date.getTime() + 900000);
			// date.
			SimpleDateFormat sdf = new SimpleDateFormat(
					ClsConstants.DATE_FORMAT_JAVA);
			fechaProgramada = sdf.format(date);
		} else
			fechaProgramada = null;
		return fechaProgramada;
	}

	/**
	 * * Define el bean envio que insertaremos
	 * 
	 * @param form
	 * @param isEnvioUnico
	 * @param locale
	 * @param userBean
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private Envio getEnvio(DefinirEnviosForm form, boolean isEnvioUnico,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {
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
		// obtener plantilla de generacion
		String idPlantillaGeneracion = form.getIdPlantillaGeneracion();
		String acuseRecibo = form.getAcuseRecibo();
		// obtener fechaProgramada
		String fechaProgramada = getFechaProgramada(form.getFechaProgramada(),
				locale, userBean);
		EnvEnviosAdm envioAdm = new EnvEnviosAdm(userBean);

		enviosBean.setIdInstitucion(Integer.valueOf(idInstitucion));

		if (isEnvioUnico) {
			// String idEnvio = form.getIdEnvio();
			// enviosBean.setIdEnvio(Integer.valueOf(form.getIdEnvio()));
			Integer idEnvio = envioAdm.getNewIdEnvio(idInstitucion);
			enviosBean.setIdEnvio(idEnvio);
			form.setIdEnvio(idEnvio.toString());
		}
		enviosBean.setAcuseRecibo(acuseRecibo);

		enviosBean.setDescripcion(nombreEnvio);
		// trunco la descripción
		if (enviosBean.getDescripcion().length() > 200)
			enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,
					99));

		enviosBean.setIdTipoEnvios(Integer.valueOf(idTipoEnvio));
		enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
		if (idPlantillaGeneracion != null && !idPlantillaGeneracion.equals("")) {
			enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
		} else {
			enviosBean.setIdPlantilla(null);
		}
		enviosBean.setFechaProgramada(fechaProgramada);
		enviosBean.setFechaCreacion("SYSDATE");
		enviosBean.setGenerarDocumento(paramAdm.getValor(idInstitucion, "ENV",
				"GENERAR_DOCUMENTO_ENVIO", "C"));
		enviosBean.setImprimirEtiquetas(paramAdm.getValor(idInstitucion, "ENV",
				"IMPRIMIR_ETIQUETAS_ENVIO", "1"));
		if (fechaProgramada == null || fechaProgramada.equals(""))
			enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
		else {
			if (idTipoEnvio != null
					&& !idTipoEnvio
							.equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))
				enviosBean
						.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
			else
				enviosBean.setIdEstado(new Integer(
						EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));
		}

		// trunco la descripción
		if (enviosBean.getDescripcion().length() > 200)
			enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,
					99));
		// Preferencia del tipo de envio si el usuario tiene uno:
		enviosBean.setIdTipoEnvios(new Integer(idTipoEnvio));

		enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
		if (idPlantillaGeneracion != null && !idPlantillaGeneracion.equals("")) {
			enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
		} else {
			enviosBean.setIdPlantilla(null);
		}
		// Bean envio
		Envio envio = new Envio(enviosBean, userBean);
		envio.setDesignas(form.getDesignas());
		envio.setEjgs(form.getEjgs());

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
	public void gestionarComunicacionCenso(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {

		// Obtenemos la información pertinente relacionada
		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		
		String idPersona = getIdColegiadoUnico(datosInformeVector);
		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}
		
		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		if (idPersona != null && enviosHashtable.size() == 1) {

			Vector vDocumentos = new Vector();
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			informesVector = this.getInformes(informesList, userBean);			
			Hashtable datosInforme = (Hashtable) datosInformeVector.get(0);
			vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
					informesVector, userBean,
						EnvioInformesGenericos.docDocument,
						EnvioInformesGenericos.comunicacionesCenso));

			
			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);

			Envio envio = getEnvio(form, true, locale, userBean);
			// Genera el envio:
			envio.generarEnvio(idPersona,
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
					vDocumentos);
			

		} else {
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			String idioma = null;
			String idTipoInforme = null;
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;

			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList, userBean);	
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];
			
				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer
						.valueOf(idPlantillaEnvio));
				
				envioProgramado.setAcuseRecibo(acuseRecibo);
				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));
	
				envioProgramadoAdm.insert(envioProgramado);
	
				boolean isInformeProgramado = false;
	
				for (int i = 0; i < datosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) datosInformeVector.get(i);
					idPersona = (String) ht.get("idPersona");
					idInstitucion = (String) ht.get("idInstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					String idTipoPersonas = (String) ht.get("idTipoPersonas");
					StringBuffer claves = new StringBuffer();
					claves.append("idTipoPersonas==");
					claves.append(idTipoPersonas);
					claves.append("##");
	
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						// envioProgramado.setProgramInformes(programInformes);
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());
						// idioma =
						// ((AdmLenguajesBean)admIdioma.getLenguajeInstitucion(idInstitucion)).getIdLenguaje();
						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setClaves(claves.toString());
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);
	
						programInformesAdm.insert(programInformes);
						isInformeProgramado = true;
	
					}
	
					destProgramInformes = new EnvDestProgramInformesBean();
					destProgramInformes
							.setIdProgram(programInformes.getIdProgram());
					destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
					destProgramInformes.setIdInstitucion(programInformes
							.getIdInstitucion());
					destProgramInformes.setIdPersona(new Long(idPersona));
					destProgramInformes.setIdInstitucionPersona(new Integer(
							idInstitucion));
	
					destProgramInformesAdm.insert(destProgramInformes);
					countEnvios++;
	
				}
	
				
				informesBean = new EnvInformesGenericosBean();
				informesBean.setIdProgram(programInformes.getIdProgram());
				informesBean.setIdEnvio(programInformes.getIdEnvio());
	
				for (int j = 0; j < informesVector.size(); j++) {
					AdmInformeBean beanInforme = (AdmInformeBean) informesVector
							.get(j);
					informesBean.setIdPlantilla(beanInforme.getIdPlantilla());
					informesBean.setIdInstitucion(beanInforme.getIdInstitucion());
					informesGenericoAdm.insert(informesBean);
				}
			}
			setEnvioBatch(true);
		}

	}
	public void gestionarComunicacionIRPF(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {
		
		// Obtenemos la información pertinente relacionada
		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		
		String idPersona = getIdColegiadoUnico(datosInformeVector);
		String idInstitucion = userBean.getLocation();

		InformeCertificadoIRPF informeCertificado = new InformeCertificadoIRPF();
		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}
		
		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		
		if (idPersona != null && enviosHashtable.size() == 1) {
		
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			informesVector = this.getInformes(informesList, userBean);			
			Hashtable datosInforme = (Hashtable) datosInformeVector.get(0);
			String periodo = (String)datosInforme.get("periodo");
			String anyoInformeIRPF= (String)datosInforme.get("anyoInformeIRPF");
			String idioma = (String)datosInforme.get("idioma");
			
			Vector vDocumentos = informeCertificado.getDocumentosAEnviar(informesVector, periodo, anyoInformeIRPF, idPersona, idioma, idInstitucion, userBean);

			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);

			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			Envio envio = getEnvio(form, true, locale, userBean);
			// Genera el envio:
			envio.generarEnvio(idPersona,
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
					vDocumentos);

			

			

		}else{
			//idInstitucion = userBean.getLocation();
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			
			String periodo = null;
			String anyoInformeIRPF = null;
			String idioma = null;
			EnvEnvioProgramadoAdm envioProgramadoAdm  = new EnvEnvioProgramadoAdm(userBean);
			EnvProgramIRPFAdm programIRPFAdm = new EnvProgramIRPFAdm(userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramIRPFBean programIRPF = null;
			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				String plantillas = "";
				for (int i = 0; i < informesList.size(); i++) {
					plantillas+=informesList.get(i)+"##";

				}
				plantillas = plantillas.substring(0,plantillas.length()-2);
				
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];
				for (int i = 0; i < datosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) datosInformeVector.get(i); 
					idInstitucion = (String) ht.get("idInstitucion");
					periodo = (String) ht.get("periodo");
					anyoInformeIRPF = (String) ht.get("anyoInformeIRPF");
					idioma = (String) ht.get("idioma");
					idPersona = (String) ht.get("idPersona");
	
					envioProgramado = new EnvEnvioProgramadoBean();
	
					envioProgramado.setIdEnvio(envioProgramadoAdm.getNewIdEnvio(idInstitucion));
					envioProgramado.setIdInstitucion(new Integer(idInstitucion));
					envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
					envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvio));
					
					envioProgramado.setAcuseRecibo(acuseRecibo);
					//mhg 
					CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
					String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
					envioProgramado.setNombre(form.getNombre()+" "+nombreyapellidos);
					//envioProgramado.setNombre(form.getNombre());
					
					envioProgramado.setEstado(ClsConstants.DB_FALSE);
					envioProgramado.setFechaProgramada(getFechaProgramada(
							form.getFechaProgramada(), locale, userBean));
	
					programIRPF = new EnvProgramIRPFBean();
					programIRPF.setIdProgram(programIRPFAdm.getNewIdProgramIrpf(idInstitucion));
					programIRPF.setIdEnvio(envioProgramado.getIdEnvio());
					programIRPF.setIdInstitucion(envioProgramado.getIdInstitucion());
					programIRPF.setIdPersona(new Long(idPersona));
					programIRPF.setIdioma(new Integer(idioma));
					programIRPF.setEstado(ClsConstants.DB_FALSE);
					programIRPF.setPeriodo(new Integer(periodo));
					programIRPF.setAnyoIRPF(new Integer(anyoInformeIRPF));
					
					programIRPF.setPlantillas(plantillas);
	
					envioProgramadoAdm.insert(envioProgramado);
					programIRPFAdm.insert(programIRPF);
					//Obtenemos el bean del envio:
				}
			}
			setEnvioBatch(true);
		}

	}


	public void gestionarComunicacionListadoGuardias(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {

		// /hasta aki
		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		
		String idInstitucion = userBean.getLocation();
		

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}
		
		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		
		Iterator iteEnvios = enviosHashtable.keySet().iterator();
		int countEnvios = 0;
		
		

		String idioma = null;
		String plantillas = null;
		EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
				userBean);
		EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
				userBean);
		EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
				userBean);
		EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
				userBean);
		EnvEnvioProgramadoBean envioProgramado = null;
		EnvProgramInformesBean programInformes = null;
		EnvDestProgramInformesBean destProgramInformes = null;
		EnvInformesGenericosBean informesBean = null;

		EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
				userBean);

		while (iteEnvios.hasNext()) {
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			informesVector = this.getInformes(informesList,
					userBean);
			String[] keysEnvio = keyEnvios.split("_");
			String idTipoEnvio = keysEnvio[0];
			String idPlantillaEnvio = keysEnvio[1];
			String acuseRecibo = keysEnvio[2];
		
		
			envioProgramado = new EnvEnvioProgramadoBean();
			envioProgramado.setIdEnvio(envioProgramadoAdm
					.getNewIdEnvio(idInstitucion));
			envioProgramado.setIdInstitucion(new Integer(idInstitucion));
			envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
			envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvio));
			envioProgramado.setAcuseRecibo(acuseRecibo);
	
			envioProgramado.setNombre(form.getNombre());
			envioProgramado.setEstado(ClsConstants.DB_FALSE);
			envioProgramado.setFechaProgramada(getFechaProgramada(
					form.getFechaProgramada(), locale, userBean));
	
			envioProgramadoAdm.insert(envioProgramado);
	
			boolean isInformeProgramado = false;
			boolean isInformesInsertados = false;
			ArrayList alClavesLista = new ArrayList();
			alClavesLista.add("idLista");
			alClavesLista.add("fechaIni");
			alClavesLista.add("fechaFin");
	
			List<String> lPersonas = new ArrayList<String>();
			

			if (datosInformeVector.size() > 0) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(0);
				String idLista = (String) datosInforme.get("idLista");
				String fechaFin = (String) datosInforme.get("fechaFin");
				String fechaIni = (String) datosInforme.get("fechaIni");
				String idTipoInforme = (String) datosInforme.get("idTipoInforme");	
	
				List<EnvDestProgramInformesBean> lDestinatarios = new ArrayList<EnvDestProgramInformesBean>();
				List<String> lDestPersonas = new ArrayList<String>();
				if (!isInformeProgramado) {
					programInformes = new EnvProgramInformesBean();
					programInformes.setIdProgram(programInformesAdm
							.getNewIdProgramInformes(idInstitucion));
					programInformes.setIdEnvio(envioProgramado.getIdEnvio());
					programInformes.setIdInstitucion(envioProgramado
							.getIdInstitucion());
					idioma = userBean.getLanguage();
					programInformes.setIdioma(new Integer(idioma));
					programInformes.setEstado(ClsConstants.DB_FALSE);
					programInformes.setIdTipoInforme(idTipoInforme);
					programInformes.setPlantillas("MULTIPLE");
					programInformesAdm.insert(programInformes);
	
					informesBean = new EnvInformesGenericosBean();
					informesBean.setIdProgram(programInformes.getIdProgram());
					informesBean.setIdEnvio(programInformes.getIdEnvio());
	
	
					for (int j = 0; j < informesVector.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean) informesVector.get(j);
						informesBean.setIdPlantilla(informeBean.getIdPlantilla());
						informesBean.setIdInstitucion(informeBean
								.getIdInstitucion());
						informesGenericoAdm.insert(informesBean);
	
					}
	
					isInformeProgramado = true;
	
				}
	
				for (int j = 0; j < informesVector.size(); j++) {
					AdmInformeBean informeBean = (AdmInformeBean) informesVector
							.get(j);
	
					String tiposDestinatario = informeBean.getDestinatarios();
					informesBean.setIdPlantilla(informeBean.getIdPlantilla());
					if (tiposDestinatario != null) {
						char[] tipoDestinatario = tiposDestinatario.toCharArray();
						for (int i = 0; i < datosInformeVector.size(); i++) { // Si hubiera mas
																	// de una
																	// persona
							Hashtable hPersonas = (Hashtable) datosInformeVector.get(i);
							String idPersona = (String) hPersonas.get("idPersona");
							destProgramInformes = new EnvDestProgramInformesBean();
							destProgramInformes.setIdProgram(programInformes
									.getIdProgram());
							destProgramInformes.setIdEnvio(programInformes
									.getIdEnvio());
							destProgramInformes.setIdInstitucion(programInformes
									.getIdInstitucion());
							destProgramInformes.setIdPersona(new Long(idPersona));
							destProgramInformes
									.setIdInstitucionPersona(new Integer(
											idInstitucion));
							destProgramInformes
									.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
	
							if (!lPersonas.contains(idPersona)) {
								destProgramInformesAdm.insert(destProgramInformes);
								lPersonas.add(idPersona);
								lDestinatarios.add(destProgramInformes);
	
							}
	
						}// for
	
					}
				}// for
	
				EnvValorCampoClaveBean valorCampoClave = null;
	
				// Iterator itClave = htClaves.keySet().iterator();
				valorCampoClave = new EnvValorCampoClaveBean();
				for (int k = 0; k < lDestinatarios.size(); k++) {
					destProgramInformes = (EnvDestProgramInformesBean) lDestinatarios
							.get(k);
	
					valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
					for (int j = 0; j < alClavesLista.size(); j++) {
						String clave = (String) alClavesLista.get(j);
						String valorClave = (String) datosInforme.get(clave);
						valorCampoClave.setIdProgram(destProgramInformes
								.getIdProgram());
						valorCampoClave
								.setIdEnvio(destProgramInformes.getIdEnvio());
						valorCampoClave.setIdInstitucion(destProgramInformes
								.getIdInstitucion());
						valorCampoClave.setIdPersona(destProgramInformes
								.getIdPersona());
						valorCampoClave.setIdInstitucionPersona(destProgramInformes
								.getIdInstitucionPersona());
	
						valorCampoClave.setIdTipoInforme(idTipoInforme);
						valorCampoClave.setClave("idListas");
						valorCampoClave.setCampo(clave);
						valorCampoClave.setValor(valorClave);
						valorCampoClaveAdm.insert(valorCampoClave);
	
					}
				}
	
				
	
			}// for vCampos
		}
		setEnvioBatch(true);

	}

	public void gestionarComunicacionPagoColegiados(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {

		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		String idPersona = getIdColegiadoUnico(datosInformeVector);
		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}
		
		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		
		
		if (idPersona != null && enviosHashtable.size() == 1) {

			Vector vDocumentos = new Vector();
			
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			informesVector = this.getInformes(informesList, userBean);			
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesPagoColegiados));

			}
			
			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			Envio envio = getEnvio(form, true, locale, userBean);

			// Genera el envio:
			envio.generarEnvio(idPersona,
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
					vDocumentos);

		} else {
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			String idioma = null;
			String idTipoInforme = null;
			
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;
			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
					userBean);
			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList, userBean);	
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];
			
				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvio));
				
				envioProgramado.setAcuseRecibo(acuseRecibo);
	
				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));
	
				envioProgramadoAdm.insert(envioProgramado);
	
				boolean isInformeProgramado = false;
				ArrayList alClavesPago = new ArrayList();
				alClavesPago.add("idPago");
				Vector auxDatosInformeVector = this.setCamposIterantes(datosInformeVector, alClavesPago, "idPagos");
	
				for (int i = 0; i < auxDatosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) auxDatosInformeVector.get(i);
					idPersona = (String) ht.get("idPersona");
					idInstitucion = (String) ht.get("idInstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					ArrayList alPagos = (ArrayList) ht.get("idPagos");
	
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());

						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);
	
						programInformesAdm.insert(programInformes);
						isInformeProgramado = true;
	
					}
	
					destProgramInformes = new EnvDestProgramInformesBean();
					destProgramInformes
							.setIdProgram(programInformes.getIdProgram());
					destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
					destProgramInformes.setIdInstitucion(programInformes
							.getIdInstitucion());
					destProgramInformes.setIdPersona(new Long(idPersona));
					// destProgramInformes.setClaves(claves.toString());
					destProgramInformes.setIdInstitucionPersona(new Integer(
							idInstitucion));
	
					destProgramInformesAdm.insert(destProgramInformes);
	
					EnvValorCampoClaveBean valorCampoClave = null;
	
					for (int j = 0; j < alPagos.size(); j++) {
						Map htClaves = (Hashtable) alPagos.get(j);
						Iterator itClave = htClaves.keySet().iterator();
						valorCampoClave = new EnvValorCampoClaveBean();
						valorCampoClave.setIdValor(valorCampoClaveAdm
								.getNewIdEnvio());
						while (itClave.hasNext()) {
							String clave = (String) itClave.next();
							String valorClave = (String) htClaves.get(clave);
	
							// String idFactura2 = (String)alExpedientes.get(j);
	
							valorCampoClave.setIdProgram(destProgramInformes
									.getIdProgram());
							valorCampoClave.setIdEnvio(destProgramInformes
									.getIdEnvio());
							valorCampoClave.setIdInstitucion(destProgramInformes
									.getIdInstitucion());
							valorCampoClave.setIdPersona(destProgramInformes
									.getIdPersona());
							// destProgramInformes.setClaves(claves.toString());
							valorCampoClave
									.setIdInstitucionPersona(destProgramInformes
											.getIdInstitucionPersona());
	
							valorCampoClave.setIdTipoInforme(idTipoInforme);
							valorCampoClave.setClave("idPagos");
							valorCampoClave.setCampo(clave);
							valorCampoClave.setValor(valorClave);
							valorCampoClaveAdm.insert(valorCampoClave);
	
						}
					}
				}
				
				informesBean = new EnvInformesGenericosBean();
				informesBean.setIdProgram(programInformes.getIdProgram());
				informesBean.setIdEnvio(programInformes.getIdEnvio());
	
				for (int j = 0; j < informesVector.size(); j++) {
	
					AdmInformeBean beanInforme = (AdmInformeBean) informesVector
							.get(j);
					informesBean.setIdPlantilla(beanInforme.getIdPlantilla());
					informesBean.setIdInstitucion(beanInforme.getIdInstitucion());
					informesGenericoAdm.insert(informesBean);
				}
			}
			setEnvioBatch(true);

		}
		// return isEnvioBatch;

	}
	public void gestionarComunicacionFacturasRectificativas(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {

		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		String idPersona = getIdColegiadoUnico(datosInformeVector);
		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}
		
		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		
		

			Vector vDocumentos = new Vector();
			
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			informesVector = this.getInformes(informesList, userBean);			
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesFacturaRectificativa));

			}
			
			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			Envio envio = getEnvio(form, true, locale, userBean);

			// Genera el envio:
			envio.generarEnvio(idPersona,
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
					vDocumentos);

	
		// return isEnvioBatch;

	}
	

	public void gestionarComunicacionFacturacionColegiados(
			DefinirEnviosForm form, Locale locale, UsrBean userBean)
			throws ClsExceptions, SIGAException {

		
		// Obtenemos la información pertinente relacionada
		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		String idPersona = getIdColegiadoUnico(datosInformeVector);
		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}
		
		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		
		
		if (idPersona != null && enviosHashtable.size() == 1) {

			Vector vDocumentos = new Vector();
			
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			informesVector = this.getInformes(informesList, userBean);			
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,EnvioInformesGenericos.docDocument,EnvioInformesGenericos.comunicacionesPagoColegiados));

			}
			
			
			
			
			
				
			
				form.setIdTipoEnvio(keyEnvios.split("_")[0]);
				form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
				form.setAcuseRecibo(keyEnvios.split("_")[2]);
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
				
			Envio envio = getEnvio(form, true, locale, userBean);

			// Genera el envio:
			envio.generarEnvio(idPersona,
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
					vDocumentos);

		} else {
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			String idioma = null;
			String idTipoInforme = null;
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;
			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
					userBean);

			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList,
						userBean);
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];
			
				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvio));
				
				envioProgramado.setAcuseRecibo(acuseRecibo);
	
				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));
	
				envioProgramadoAdm.insert(envioProgramado);
	
				boolean isInformeProgramado = false;
				ArrayList alClavesPago = new ArrayList();
				alClavesPago.add("idFacturacion");
				Vector auxDatosInformeVector = this.setCamposIterantes(datosInformeVector, alClavesPago,
						"idFacturaciones");
	
				for (int i = 0; i < auxDatosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) auxDatosInformeVector.get(i);
					idPersona = (String) ht.get("idPersona");
					idInstitucion = (String) ht.get("idInstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					ArrayList alFacturaciones = (ArrayList) ht.get("idFacturaciones");
	
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());
						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);
	
						programInformesAdm.insert(programInformes);
						isInformeProgramado = true;
	
					}
	
					destProgramInformes = new EnvDestProgramInformesBean();
					destProgramInformes
							.setIdProgram(programInformes.getIdProgram());
					destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
					destProgramInformes.setIdInstitucion(programInformes
							.getIdInstitucion());
					destProgramInformes.setIdPersona(new Long(idPersona));
					// destProgramInformes.setClaves(claves.toString());
					destProgramInformes.setIdInstitucionPersona(new Integer(
							idInstitucion));
	
					destProgramInformesAdm.insert(destProgramInformes);
	
					EnvValorCampoClaveBean valorCampoClave = null;
	
					for (int j = 0; j < alFacturaciones.size(); j++) {
						Map htClaves = (Hashtable) alFacturaciones.get(j);
						Iterator itClave = htClaves.keySet().iterator();
						valorCampoClave = new EnvValorCampoClaveBean();
						valorCampoClave.setIdValor(valorCampoClaveAdm
								.getNewIdEnvio());
						while (itClave.hasNext()) {
							String clave = (String) itClave.next();
							String valorClave = (String) htClaves.get(clave);
	
	
							valorCampoClave.setIdProgram(destProgramInformes
									.getIdProgram());
							valorCampoClave.setIdEnvio(destProgramInformes
									.getIdEnvio());
							valorCampoClave.setIdInstitucion(destProgramInformes
									.getIdInstitucion());
							valorCampoClave.setIdPersona(destProgramInformes
									.getIdPersona());
							valorCampoClave.setIdInstitucionPersona(destProgramInformes
											.getIdInstitucionPersona());
	
							valorCampoClave.setIdTipoInforme(idTipoInforme);
							valorCampoClave.setClave("idFacturaciones");
							valorCampoClave.setCampo(clave);
							valorCampoClave.setValor(valorClave);
							valorCampoClaveAdm.insert(valorCampoClave);
	
						}
					}
				}
	
				
				informesBean = new EnvInformesGenericosBean();
				informesBean.setIdProgram(programInformes.getIdProgram());
				informesBean.setIdEnvio(programInformes.getIdEnvio());
	
				for (int j = 0; j < informesVector.size(); j++) {
	
					AdmInformeBean beanInforme = (AdmInformeBean) informesVector
							.get(j);
					informesBean.setIdPlantilla(beanInforme.getIdPlantilla());
					informesBean.setIdInstitucion(beanInforme.getIdInstitucion());
					informesGenericoAdm.insert(informesBean);
				}
			}
			setEnvioBatch(true);
			

		}
	}// return isEnvioBatch;

	/**
	 * Metodo que gestiona las comunicaciones de cobros y recobros(morosos)
	 * 
	 * @param form
	 * @param isEnvioBatch
	 *            parametro que sobreescribimos
	 * @param locale
	 * @param userBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */

	public void gestionarComunicacionMorosos(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {

		// String idPersona = getIdColegiadoUnico(form);
		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		String idPersona = getIdColegiadoUnico(datosInformeVector);

		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)&&true) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}

		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;

		if (idPersona != null&& enviosHashtable.size() == 1) {
			String claveIterante = "idFactura";
			form.setClavesIteracion(claveIterante);

			if (claveIterante != null) {
				datosInformeVector = this.setCamposIterantes(
						datosInformeVector, claveIterante);
			}

			Vector vDocumentos = new Vector();
			ArrayList alFacturas = new ArrayList();
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);

			informesVector = this.getInformes(informesList, userBean);

			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				// en el metodo getDatosInformeFinal metemos la key definitiva
				// idFacturas en el datosInforme
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,
						EnvioInformesGenericos.docDocument,
						EnvioInformesGenericos.comunicacionesMorosos));
				alFacturas.addAll((ArrayList) datosInforme.get("idFacturas"));

			}
			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);

			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			Envio envio = getEnvio(form, true, locale, userBean);

			// Genera el envio:
			envio.generarEnvio(idPersona,
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
					vDocumentos);

			envio.generarComunicacionMoroso(idPersona, vDocumentos, alFacturas,
					idInstitucion, envio.getEnviosBean().getDescripcion());

		} else {
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;

			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
					userBean);
			ArrayList alClavesFactura = new ArrayList();
			alClavesFactura.add("idFactura");
			datosInformeVector = this.setCamposIterantes(
					datosInformeVector, alClavesFactura, "idFacturas");
			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList,
						userBean);
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];

				String idioma = null;
				String idTipoInforme = null;

				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvio));
				envioProgramado.setAcuseRecibo(acuseRecibo);

				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));
				envioProgramadoAdm.insert(envioProgramado);
				boolean isInformeProgramado = false;
				

				for (int i = 0; i < datosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) datosInformeVector.get(i);
					idPersona = (String) ht.get("idPersona");
					idInstitucion = (String) ht.get("idInstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					ArrayList alFacturas = (ArrayList) ht.get("idFacturas");
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes
								.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());
						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);
						programInformesAdm.insert(programInformes);
						isInformeProgramado = true;

					}
					destProgramInformes = new EnvDestProgramInformesBean();
					destProgramInformes.setIdProgram(programInformes
							.getIdProgram());
					destProgramInformes
							.setIdEnvio(programInformes.getIdEnvio());
					destProgramInformes.setIdInstitucion(programInformes
							.getIdInstitucion());
					destProgramInformes.setIdPersona(new Long(idPersona));
					destProgramInformes.setIdInstitucionPersona(new Integer(
							idInstitucion));
					destProgramInformesAdm.insert(destProgramInformes);
					countEnvios++;
					EnvValorCampoClaveBean valorCampoClave = null;
					for (int j = 0; j < alFacturas.size(); j++) {
						Map htClaves = (Hashtable) alFacturas.get(j);
						Iterator itClave = htClaves.keySet().iterator();
						valorCampoClave = new EnvValorCampoClaveBean();
						valorCampoClave.setIdValor(valorCampoClaveAdm
								.getNewIdEnvio());
						while (itClave.hasNext()) {
							String clave = (String) itClave.next();
							String valorClave = (String) htClaves.get(clave);
							valorCampoClave.setIdProgram(destProgramInformes
									.getIdProgram());
							valorCampoClave.setIdEnvio(destProgramInformes
									.getIdEnvio());
							valorCampoClave
									.setIdInstitucion(destProgramInformes
											.getIdInstitucion());
							valorCampoClave.setIdPersona(destProgramInformes
									.getIdPersona());
							valorCampoClave
									.setIdInstitucionPersona(destProgramInformes
											.getIdInstitucionPersona());
							valorCampoClave.setIdTipoInforme(idTipoInforme);
							valorCampoClave.setClave("idFacturas");
							valorCampoClave.setCampo(clave);
							valorCampoClave.setValor(valorClave);
							valorCampoClaveAdm.insert(valorCampoClave);
						}
					}
				}
				informesBean = new EnvInformesGenericosBean();
				informesBean.setIdProgram(programInformes.getIdProgram());
				informesBean.setIdEnvio(programInformes.getIdEnvio());
				for (int j = 0; j < informesVector.size(); j++) {
					AdmInformeBean informeBean = (AdmInformeBean) informesVector
							.get(j);
					informesBean.setIdPlantilla(informeBean.getIdPlantilla());
					informesBean.setIdInstitucion(informeBean
							.getIdInstitucion());
					informesGenericoAdm.insert(informesBean);
				}
			}
			setEnvioBatch(true);
		}
		// return isEnvioBatch;

	}

	private String getIdDestinatarioUnico(Vector vCampos, Vector vPlantillas) {
		Hashtable htPersonas = new Hashtable();
		String idPersona = null;
		String idInstitucion = null;
		Vector vDefendidos = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			idPersona = (String) ht.get("idPersona");
			idInstitucion = (String) ht.get("idInstitucion");
			vDefendidos = (Vector) ht.get("defendidosDesigna");
			String key = idPersona + "||" + idInstitucion;
			if (!htPersonas.containsKey(key) && i != 0) {
				idPersona = null;
				break;

			} else {
				htPersonas.put(key, idPersona);
			}
		}
		return idPersona;
	}

	public Hashtable getDatosSalidaListaGuardias(Hashtable datosInforme,
			UsrBean usrBean, boolean isSolicitantes) throws ClsExceptions {

		Hashtable vSalida = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		Hashtable datosListaGuardias = null;
		try {
			vSalida = new Hashtable();
			String idInstitucion = (String) datosInforme.get("idInstitucion");
			String idLista = (String) datosInforme.get("idLista");
			Hashtable htE = new Hashtable();
			Hashtable htCabeceraInforme = new Hashtable();
			Vector vDatosInforme = new Vector();
			Date hoy = new Date();
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
			String sHoy = sdf2.format(hoy);
			TreeMap datosLista = null;
			// for (int j = 0; j < vGuardias.size(); j++) {
			if (isSolicitantes) {
				datosLista = getIdColegiados(datosInforme, usrBean);
			} else {
				datosLista = getIdColegiadosTotal(datosInforme, usrBean);
			}
			Iterator iteradorListaGuardias = datosLista.keySet().iterator();
			Object keyListaGuardia;

			if (datosInforme.get("NOMBRE") == null) {
				datosListaGuardias = new Hashtable();
				ScsListaGuardiasAdm admListaGuardias = new ScsListaGuardiasAdm(
						usrBean);
				Vector fechaYUsu = admListaGuardias
						.selectGenerico(admListaGuardias.getFechaYUsu(
								idInstitucion, idLista));
				// Almacenamos en sesion el registro de la lista de guardias
				datosListaGuardias.put("NOMBRE",
						(String) ((Hashtable) fechaYUsu.elementAt(0))
								.get("NOMBRE"));
				datosListaGuardias.put("LUGAR", (String) ((Hashtable) fechaYUsu
						.elementAt(0)).get("LUGAR"));
				datosListaGuardias.put("OBSERVACIONES",
						(String) ((Hashtable) fechaYUsu.elementAt(0))
								.get("OBSERVACIONES"));
				datosListaGuardias.put("USUMODIFICACION",
						(String) ((Hashtable) fechaYUsu.elementAt(0))
								.get("USUMODIFICACION"));
				datosListaGuardias.put("FECHAMODIFICACION",
						(String) ((Hashtable) fechaYUsu.elementAt(0))
								.get("FECHAMODIFICACION"));
				datosListaGuardias.put("IDINSTITUCION",
						(String) ((Hashtable) fechaYUsu.elementAt(0))
								.get("IDINSTITUCION"));
				datosListaGuardias.put("IDLISTA",
						(String) ((Hashtable) fechaYUsu.elementAt(0))
								.get("IDLISTA"));
			} else {
				datosListaGuardias = new Hashtable();
				datosListaGuardias.put("NOMBRE", datosInforme.get("NOMBRE"));
				datosListaGuardias.put("LUGAR", datosInforme.get("LUGAR"));
				datosListaGuardias.put("OBSERVACIONES",
						datosInforme.get("OBSERVACIONES"));
				datosListaGuardias.put("USUMODIFICACION",
						datosInforme.get("USUMODIFICACION"));
				datosListaGuardias.put("FECHAMODIFICACION",
						datosInforme.get("FECHAMODIFICACION"));
				datosListaGuardias.put("IDINSTITUCION",
						datosInforme.get("IDINSTITUCION"));
				datosListaGuardias.put("IDLISTA", datosInforme.get("IDLISTA"));

			}
			while (iteradorListaGuardias.hasNext()) {

				keyListaGuardia = iteradorListaGuardias.next();
				// Hashtable aux = (Hashtable) datosLista.get(obj);
				String idPersona = UtilidadesString
						.split(keyListaGuardia.toString(), "|||")[1];
				Vector list = (Vector) datosLista.get(keyListaGuardia);
				int tam = 0;
				CenColegiadoAdm admCol = new CenColegiadoAdm(usrBean);
//				HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
//				helperInformesAdm.setIdiomaInforme(idInstitucion, idPersona, AdmInformeBean.TIPODESTINATARIO_CENPERSONA,datosInforme, usrBean);
				Hashtable htCol = admCol.obtenerDatosColegiado(
						idInstitucion.toString(), idPersona,
						(String)datosInforme.get("idioma"));
				// se añade esta linea para que recupere todos los campos de la
				// select que esta en la función obtenerDatosColegiado(..)
				// y posteriormente utilizarla en las plantillas.
				htCabeceraInforme.putAll(htCol);

				String direccion = "";
				if (htCol != null && htCol.size() > 0) {
					direccion = (String) htCol.get("DIRECCION_LETR");
				}
				htCabeceraInforme.put("DOMICILIO_LETRADO", direccion);
				String codPostal = "";
				if (htCol != null && htCol.size() > 0) {
					codPostal = (String) htCol.get("CP_LETR");
				}
				htCabeceraInforme.put("CP_LETRADO", codPostal);
				String localidad = "";
				if (htCol != null && htCol.size() > 0) {
					localidad = (String) htCol.get("POBLACION_LETR");
				}
				htCabeceraInforme.put("POBLACION_LETRADO", localidad);
				String provincia = "";
				if (htCol != null && htCol.size() > 0) {
					provincia = (String) htCol.get("PROVINCIA_LETR");
				}
				htCabeceraInforme.put("PROVINCIA_LETRADO", provincia);

				if ((direccion.equals("")) && (codPostal.equals(""))
						&& (localidad.equals("")) && (provincia.equals(""))) {
					Vector direcciones = null;
					String idprovincia = "";
					String idpoblacion = "";

					// se llama a la funcion de getDirecciones donde si no tiene
					// dirección de tipo despacho, coge la
					// la dirección preferente y si no tiene ninguna dirección
					// como preferente coge la primera dirección que encuentra.
					// el parametro de valor 2, es valor que se le pasa a la
					// funcion para que sepa que tipo de dirección queremos.
					EnvEnviosAdm enviosAdm = new EnvEnviosAdm(usrBean);
					direcciones = enviosAdm.getDirecciones(
							String.valueOf(idInstitucion), idPersona, "2");

					CenPersonaAdm personaAdm = new CenPersonaAdm(usrBean);
					Hashtable htPersona = new Hashtable();
					htPersona.put(CenPersonaBean.C_IDPERSONA, idPersona);
					CenPersonaBean persona = (CenPersonaBean) ((Vector) personaAdm
							.selectByPK(htPersona)).get(0);

					if (direcciones != null && direcciones.size() > 0) {
						Hashtable htDir = (Hashtable) direcciones
								.firstElement();
						htCabeceraInforme.put("DOMICILIO_LETRADO",
								(String) htDir
										.get(CenDireccionesBean.C_DOMICILIO));
						htCabeceraInforme.put("CP_LETRADO", (String) htDir
								.get(CenDireccionesBean.C_CODIGOPOSTAL));

						idpoblacion = (String) htDir
								.get(CenDireccionesBean.C_IDPOBLACION);
						CenPoblacionesAdm admpoblacion = new CenPoblacionesAdm(
								usrBean);
						if (!idpoblacion.equals("")) {
							htCabeceraInforme.put("POBLACION_LETRADO",
									admpoblacion.getDescripcion(idpoblacion));
						}
						idprovincia = (String) htDir
								.get(CenDireccionesBean.C_IDPROVINCIA);
						CenProvinciaAdm admprovincia = new CenProvinciaAdm(
								usrBean);
						if (!idprovincia.equals("")) {
							htCabeceraInforme.put("PROVINCIA_LETRADO",
									admprovincia.getDescripcion(idprovincia));
						}
					}
				}

				String nombreLista = UtilidadesHash.getString(
						datosListaGuardias, "NOMBRE");
				String lugar = UtilidadesHash.getString(datosListaGuardias,
						"LUGAR");
				String observaciones = UtilidadesHash.getString(
						datosListaGuardias, "OBSERVACIONES");
				htCabeceraInforme.put("IDLISTA", nombreLista);

				htCabeceraInforme.put("DIA_ACTUAL", sHoy.substring(0, 2));
				htCabeceraInforme.put("MES_ACTUAL", sHoy.substring(3, 5));
				htCabeceraInforme.put("ANIO_ACTUAL", sHoy.substring(6, 10));
				String fechaInicio = (String) datosInforme.get("fechaIni");
				String fechaFin = (String) datosInforme.get("fechaFin");
				htCabeceraInforme.put("FECHA_INI", fechaInicio);
				htCabeceraInforme.put("FECHA_FIN", fechaFin);
				// Obtenemos nombre de la institucion
				CenInstitucionAdm admInstitucion = new CenInstitucionAdm(
						usrBean);
				String nombreInstitucion = admInstitucion
						.getNombreInstitucion(idInstitucion);
				htCabeceraInforme.put("NOMBRE_COLEGIO_PERIODO",
						nombreInstitucion + "  " + fechaInicio + " - "
								+ fechaFin);
				htCabeceraInforme.put("NOMBRE_LISTA_GUARDIA", nombreLista);
				htCabeceraInforme.put("LUGAR", lugar);
				htCabeceraInforme.put("OBSERVACIONES", observaciones);
				htCabeceraInforme.put("LETRADO",
						(String) htCol.get("APELLIDO1_LETRADO") + " "
								+ (String) htCol.get("APELLIDO2_LETRADO")
								+ ", " + (String) htCol.get("N_LETRADO"));
				while (tam < list.size()) {

					Hashtable fila = new Hashtable();
					Hashtable f = new Hashtable();
					f = (Hashtable) list.elementAt(tam);
					String nombre = "";

					fila.put("LETRADO", (String) f.get("LETRADO"));
					fila.put("GUARDIA_DIA_TEXTO", (String) f.get("DIA_FECHA_INICIO"));
					fila.put("GUARDIA", (String) f.get("GUARDIA"));
					Date fecha_inicio = new Date((String) f.get("FECHA_INICIO"));
					Date fecha_fin = new Date((String) f.get("FECHA_FIN"));
					SimpleDateFormat formateador = new SimpleDateFormat(
							"dd/MM/yyyy");
					String fechaIni = formateador.format(fecha_inicio);
					String fechaF = formateador.format(fecha_fin);

					fila.put("DIA_GUA", fechaIni.substring(0, 2));
					fila.put("MES_GUA", fechaIni.substring(3, 5));
					fila.put("ANIO_GUA", fechaIni.substring(6, 10));
					fila.put("FECHA_INICIO", fechaIni);
					fila.put("FECHAFIN", fechaF);
					fila.put("TURNO", (String) f.get("TURNO"));
					fila.put("TFNO_OFICINA1",
							(String) htCol.get("TELEFONO1_LETRADO"));
					fila.put("TFNO_OFICINA2",
							(String) htCol.get("TELEFONO2_LETRADO"));
					fila.put("MOVIL", (String) htCol.get("MOVIL_LETRADO"));
					fila.put("FAX1", (String) htCol.get("FAX1"));
					fila.put("NCOLEGIADO",
							(String) htCol.get("NCOLEGIADO_LETRADO"));
					fila.put("NUM_COLEGIADO_COMPA", (String) f.get("NUM_COLEGIADO_COMPA"));
					fila.put("NOMBRE_COMPA", (String) f.get("NOMBRE_COMPA"));
					// ScsCabeceraGuardiasAdm admCab = new
					// ScsCabeceraGuardiasAdm(usrBean);
					// Integer posicion =
					// admCab.getPosicion(idInstitucion,idLista, idturno,
					// fecha_inicio,idPersona);

					String desguar = (String) f.get("DESCRIPCIONGUARDIA");
					if (desguar != null)
						fila.put("DESCRIPCION_GUARDIA", desguar);
					else
						fila.put("DESCRIPCION_GUARDIA", " ");

					String posicion = (String) f.get("POSICION");
					if (posicion != null)
						fila.put("POSICION", posicion);
					else
						fila.put("POSICION", " ");
					String grupo = (String) f.get("GRUPO");
					if (grupo != null)
						fila.put("GRUPO", grupo);
					else
						fila.put("GRUPO", " ");
					vDatosInforme.add(fila);
					tam++;
				}
			}

			vSalida.put("htCabeceraInforme", htCabeceraInforme);
			vSalida.put("vDatosInforme", vDatosInforme);
			vSalida.put("hDatosListaGuardias", datosListaGuardias);

			// }

		} catch (Exception e) {
			throw new ClsExceptions(e,
					"Error al obtener la informacion en getDatosSalidaOficio");
		}
		return vSalida;

	}

	private TreeMap getIdColegiados(Hashtable datosInforme, UsrBean usrBean)
			throws ClsExceptions {
		TreeMap htPersonas = new TreeMap();
		Vector datos = new Vector();
		Vector guardias = new Vector();
		String fechaInicio = null;
		String fechaFin = null;
		String idlista = null;
		String idPersona = null;
		String idInstitucion = null;
		TreeMap listaPorUsu = null;
		try {
			// MasterReport masterReport = new MasterReport();
			// Vector vCampos = masterReport.obtenerDatosFormulario(form);
			if (datosInforme.size() > 0) {
				// Hashtable ht=(Hashtable)datosInforme.get(j);
				// Long letrado =new
				// Long((String)ht.get("IDPERSONA")).longValue();
				idInstitucion = (String) datosInforme.get("idInstitucion");
				fechaInicio = (String) datosInforme.get("fechaIni");
				fechaFin = (String) datosInforme.get("fechaFin");
				idlista = (String) datosInforme.get("idLista");
				idPersona = (String) datosInforme.get("idPersona");

			}
			ScsInclusionGuardiasEnListasAdm admIGL = new ScsInclusionGuardiasEnListasAdm(
					usrBean);
			Hashtable paramBusqueda = new Hashtable();
			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,
					idInstitucion);
			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,
					idlista);
			Vector listasIncluidas = admIGL.select(paramBusqueda);
			Enumeration listaResultados = listasIncluidas.elements();

			while (listaResultados.hasMoreElements()) {
				ScsInclusionGuardiasEnListasBean fila = (ScsInclusionGuardiasEnListasBean) listaResultados
						.nextElement();
				// Recopilacion datos
				/*
				 * RGG 08/10/2007 cambio para que obtenga todas las guardias del
				 * tiron Vector datosParciales=
				 * admGT.getDatosListaGuardias(fila.
				 * getIdInstitucion().toString()
				 * ,fila.getIdTurno().toString(),fila
				 * .getIdGuardia().toString(),fechaInicio,fechaFin); int i=0;
				 * while (i<datosParciales.size()){
				 * datos.add(datosParciales.elementAt(i)); i++; }
				 */
				Vector guardia = new Vector();
				guardia.add(fila.getIdTurno().toString());
				guardia.add(fila.getIdGuardia().toString());

				guardias.add(guardia);

			}

			ScsGuardiasTurnoAdm admGT = new ScsGuardiasTurnoAdm(usrBean);

			datos = admGT.getDatosListaGuardias(idInstitucion, idlista, guardias, fechaInicio, fechaFin, idPersona);
			listaPorUsu = calcularPersona(datos);

		} catch (SIGAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaPorUsu;
	}

	private TreeMap getIdColegiadosTotal(Hashtable datosInforme, UsrBean usrBean)
			throws ClsExceptions {
		Vector datos = new Vector();
		Vector guardias = new Vector();
		String fechaInicio = null;
		String fechaFin = null;
		String idlista = null;
		String idPersona = null;
		String idInstitucion = null;
		TreeMap lista = null;
		try {
			// MasterReport masterReport = new MasterReport();
			// Vector vCampos = masterReport.obtenerDatosFormulario(form);
			if (datosInforme.size() > 0) {
				// Hashtable ht=(Hashtable)datosInforme.get(j);
				// Long letrado =new
				// Long((String)ht.get("IDPERSONA")).longValue();
				idInstitucion = (String) datosInforme.get("idInstitucion");
				fechaInicio = (String) datosInforme.get("fechaIni");
				fechaFin = (String) datosInforme.get("fechaFin");
				idlista = (String) datosInforme.get("idLista");
				idPersona = (String) datosInforme.get("idPersona");

			}
			ScsInclusionGuardiasEnListasAdm admIGL = new ScsInclusionGuardiasEnListasAdm(
					usrBean);
			Hashtable paramBusqueda = new Hashtable();
			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,
					idInstitucion);
			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,
					idlista);
			Vector listasIncluidas = admIGL.select(paramBusqueda);
			Enumeration listaResultados = listasIncluidas.elements();

			while (listaResultados.hasMoreElements()) {
				ScsInclusionGuardiasEnListasBean fila = (ScsInclusionGuardiasEnListasBean) listaResultados
						.nextElement();
				// Recopilacion datos
				/*
				 * RGG 08/10/2007 cambio para que obtenga todas las guardias del
				 * tiron Vector datosParciales=
				 * admGT.getDatosListaGuardias(fila.
				 * getIdInstitucion().toString()
				 * ,fila.getIdTurno().toString(),fila
				 * .getIdGuardia().toString(),fechaInicio,fechaFin); int i=0;
				 * while (i<datosParciales.size()){
				 * datos.add(datosParciales.elementAt(i)); i++; }
				 */
				Vector guardia = new Vector();
				guardia.add(fila.getIdTurno().toString());
				guardia.add(fila.getIdGuardia().toString());

				guardias.add(guardia);

			}

			ScsGuardiasTurnoAdm admGT = new ScsGuardiasTurnoAdm(usrBean);

			datos = admGT.getDatosListaGuardias(idInstitucion, idlista, guardias, fechaInicio, fechaFin, null);
			lista = calcularPersonasTotal(datos);

		} catch (SIGAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lista;
	}

	protected TreeMap calcularPersona(Vector datos) throws SIGAException {

		TreeMap todos = new TreeMap();
		// vector guardiaPers= null;
		TreeMap map = new TreeMap();
		try {
			List guardiasList = null;
			for (int j = 0; j < datos.size(); j++) {
				Hashtable lineaGuardia = (Hashtable) datos.get(j);
				String letrado = "litos|||"
						+ (String) lineaGuardia.get("IDPERSONA");
				if (map.containsKey(letrado)) {
					guardiasList = (List) map.get(letrado);
				} else {
					guardiasList = new ArrayList();
				}
				guardiasList.add(lineaGuardia);
				map.put(letrado, guardiasList);
			}
			Iterator it = map.keySet().iterator();
			List guardiasOutList = null;
			while (it.hasNext()) {
				String letradoOut = (String) it.next();

				guardiasOutList = (List) map.get(letradoOut);
				Vector v = new Vector();
				for (int i = 0; i < guardiasOutList.size(); i++) {
					Hashtable lineaGuardiaHashtable = ((Hashtable) guardiasOutList
							.get(i));
					v.add(lineaGuardiaHashtable);
				}
				todos.put(letradoOut, v);
			}

		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}
		return todos;
	}

	protected TreeMap calcularPersonasTotal(Vector datos) throws SIGAException {

		TreeMap todos = new TreeMap();
		// vector guardiaPers= null;
		TreeMap map = new TreeMap();
		try {
			List guardiasList = null;
			for (int j = 0; j < datos.size(); j++) {
				Hashtable lineaGuardia = (Hashtable) datos.get(j);
//				sql += " ORDER BY FECHA_INICIO,FECHA_FIN, SCS_INCLUSIONGUARDIASENLISTAS.ORDEN, GUARDIA, POSICION, LETRADO";
				
				String key = (GstDate.convertirFecha(
						(String) lineaGuardia.get("FECHA_INICIO"),
						"yyyy/MM/dd HH:mm:ss").getTime())
						+ "##"
						+ (String) lineaGuardia.get("ORDEN")+
						 "##"
						 +(String) lineaGuardia.get("GUARDIA")
						 + "##"
						+ (String) lineaGuardia.get("POSICION")
						+ "|||"
						+ (String) lineaGuardia.get("IDPERSONA");
				// Long letrado =new Long().longValue();
				if (map.containsKey(key)) {
					guardiasList = (List) map.get(key);
				} else {
					guardiasList = new ArrayList();
				}
				guardiasList.add(lineaGuardia);
				map.put(key, guardiasList);
			}
			Iterator it = map.keySet().iterator();
			List guardiasOutList = null;
			while (it.hasNext()) {
				String keyoOut = (String) it.next();

				guardiasOutList = (List) map.get(keyoOut);
				Vector v = new Vector();
				for (int i = 0; i < guardiasOutList.size(); i++) {
					Hashtable lineaGuardiaHashtable = ((Hashtable) guardiasOutList
							.get(i));
					v.add(lineaGuardiaHashtable);
				}
				todos.put(keyoOut, v);
			}

		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}
		return todos;
	}

	public void setPersonasDesignas(Vector vCampos, UsrBean userBean)
			throws ClsExceptions, SIGAException {
		ScsDesignaAdm desigAdm = new ScsDesignaAdm(userBean);
		ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(
				userBean);
		String idTurno = null;
		String idInstitucion = null;
		String anio = null;
		String numero = null;
		List auxiliar = new ArrayList();

		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			idInstitucion = (String) ht.get("idInstitucion");
			anio = (String) ht.get("anio");
			idTurno = (String) ht.get("idTurno");
			numero = (String) ht.get("numero");
			String idPersonaDesigna = desigAdm.getIDLetradoDesig(idInstitucion,
					idTurno, anio, numero);
			Vector vDefendidos = defendidosAdm.getDefendidosDesigna(
					new Integer(idInstitucion), new Integer(anio), new Integer(
							numero), new Integer(idTurno),null);
			ht.put("defendidosDesigna", vDefendidos);
			ht.put("idPersona", idPersonaDesigna);
			String idProcurador = desigAdm.getIDProcuradorDesig(idInstitucion,
					idTurno, anio, numero);
			if (idProcurador != null && !idProcurador.equals(""))
				ht.put("idProcurador", idProcurador);
			
			Long idJuzgado = desigAdm.getIdJuzgadoDesigna(idInstitucion,anio,numero,idTurno);
			if (idJuzgado != null && !idJuzgado.equals(""))
				ht.put("idJuzgado", idJuzgado.toString());

		}

	}

	private void setPersonasDesignas(String datosInforme, UsrBean userBean)
			throws ClsExceptions, SIGAException {
		ScsDesignaAdm desigAdm = new ScsDesignaAdm(userBean);
		ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(
				userBean);
		String idTurno = null;
		String idInstitucion = null;
		String anio = null;
		String numero = null;
		List auxiliar = new ArrayList();

		/*
		 * Hashtable datosInforme = (Hashtable) vCampos.get(0); String
		 * plantillas = (String) datosInforme.get("plantillas");
		 * 
		 * String d[] = plantillas.split("@@"); Vector vPlantillas = new
		 * Vector(); AdmInformeAdm admInforme = new AdmInformeAdm(userBean); for
		 * (int i = 0; i < d.length; i++) { String plantilla = d[i]; String
		 * strPlantilla[] = plantilla.split(","); String idPlantilla =
		 * strPlantilla[0]; String destinatario = strPlantilla[1];
		 * AdmInformeBean beanInforme = admInforme.obtenerInforme(
		 * idInstitucion, idPlantilla); vPlantillas.add(beanInforme);
		 * 
		 * }
		 */
		String[] parametrosDesigna = datosInforme.split(",");

		idInstitucion = (String) parametrosDesigna[0];
		anio = (String) parametrosDesigna[0];
		idTurno = (String) parametrosDesigna[0];
		numero = (String) parametrosDesigna[0];
		String idPersonaDesigna = desigAdm.getIDLetradoDesig(idInstitucion,
				idTurno, anio, numero);
		Vector vDefendidos = defendidosAdm.getDefendidosDesigna(new Integer(
				idInstitucion), new Integer(anio), new Integer(numero),
				new Integer(idTurno),null);
		// ht.put("defendidosDesigna", vDefendidos);
		// ht.put("idPersona", idPersonaDesigna);
		// String idProcurador =
		// desigAdm.getIDProcuradorDesig(idInstitucion,idTurno,anio,numero);
		// if(idProcurador!=null&&!idProcurador.equals(""))
		// ht.put("idProcurador", idProcurador);

	}

	public void setPersonasEjg(Vector vCampos, UsrBean userBean) throws ClsExceptions, SIGAException {
        ScsEJGAdm ejgAdm = new ScsEJGAdm(userBean);
        ScsContrariosEJGAdm scsContrariosEJGAdm = new ScsContrariosEJGAdm(userBean);
        ScsDesignaAdm designaAdm = new ScsDesignaAdm(userBean);
        ScsUnidadFamiliarEJGAdm unidadFamiliarEJGAdm = new ScsUnidadFamiliarEJGAdm(userBean);
        String idTipoEJG = null;
        String idInstitucion = null;
        String anio = null;
        String numero = null;
        List auxiliar = new ArrayList();
        Iterator iteCampos = vCampos.iterator();
        
        while (iteCampos.hasNext()) {
            Hashtable ht = (Hashtable) iteCampos.next();
            idInstitucion = (String) ht.get("idinstitucion");
            String idPersonaJG = (String) ht.get("idPersonaJG");
            anio = (String) ht.get("anio");
            if ((String) ht.get("idtipo") != null) {
                idTipoEJG = (String) ht.get("idtipo");
            } else {
                idTipoEJG = (String) ht.get("idTipoEJG");
            }
            numero = (String) ht.get("numero");
            Vector solicitantesEjg = null;
            
            if (idPersonaJG != null) {
                // Si viene persona jg //Lo añadimos
                solicitantesEjg = new Vector();
                Hashtable personaJGHashtable = new Hashtable();
                personaJGHashtable.put("IDPERSONA", idPersonaJG);
                solicitantesEjg.add(personaJGHashtable);
                ht.put("solicitantesEjg", solicitantesEjg);

            } else {
                // Este netodo nos debe devolver un vector de hashtable con las
                // personas de las designaciones relacionadas
                Vector personasDesignasEjg = designaAdm.getPersonasDesignadasEjg(new Integer(idInstitucion),new Integer(idTipoEJG), new Integer(anio),new Integer(numero));
                solicitantesEjg = unidadFamiliarEJGAdm.getSolicitantesEjg(new Integer(idInstitucion), new Integer(idTipoEJG),new Integer(anio), new Integer(numero));
                
                
                Vector<ScsContrariosEJGBean> contrariosEjg = scsContrariosEJGAdm.getPersonasContrariosEjg(new Integer(idInstitucion),new Integer(idTipoEJG), new Integer(anio),new Integer(numero));
                
                if (solicitantesEjg != null)
                    ht.put("solicitantesEjg", solicitantesEjg);
                if (personasDesignasEjg != null)
                    ht.put("personasDesignasEjg", personasDesignasEjg);

                Long idJuzgado = ejgAdm.getIdJuzgadoEjg(idInstitucion,anio,numero,idTipoEJG);
                if (idJuzgado != null && !idJuzgado.equals(""))
                    ht.put("idJuzgado", idJuzgado.toString());
                if (contrariosEjg != null)
                    ht.put("contrariosEjg", contrariosEjg);
                
                if (personasDesignasEjg == null || personasDesignasEjg.size() == 0 && (solicitantesEjg == null || solicitantesEjg.size() == 0) &&(idJuzgado == null || idJuzgado.equals(""))&& (contrariosEjg == null || contrariosEjg.size() == 0))
                    iteCampos.remove();
            }
        }
    }

	private List getSolicitantesDesignas(Vector vCampos, UsrBean userBean)
			throws ClsExceptions, SIGAException {
		ScsDesignaAdm desigAdm = new ScsDesignaAdm(userBean);
		ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(
				userBean);

		String idPersonaJG = null;
		String idTurno = null;
		String idInstitucion = null;
		String anio = null;
		String numero = null;
		boolean isSolicitanteUnicoDesignas = true;
		List alSolicitantes = new ArrayList();
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i);
			idInstitucion = (String) ht.get("idInstitucion");
			anio = (String) ht.get("anio");
			idTurno = (String) ht.get("idTurno");
			numero = (String) ht.get("numero");
			Vector vDefendidos = defendidosAdm.getDefendidosDesigna(
					new Integer(idInstitucion), new Integer(anio), new Integer(
							numero), new Integer(idTurno),null);
			if (vDefendidos != null)
				alSolicitantes.addAll(vDefendidos);
			if (alSolicitantes.size() > 1) {
				break;
			}

		}
		return alSolicitantes;

	}

	/**
	 * Metodo que gestiona el envio y comunicacion de Designaciones
	 * 
	 * @param form
	 * @param locale
	 * @param userBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void gestionarComunicacionDesignas(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {

		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		// Tenemos que obtener el letrado de la designa ya que no se saca en la
		// query del
		// paginador(¡¡¡¡Esta persona se obtiene en la jsp!!!!)
		setPersonasDesignas(datosInformeVector, userBean);

		String idPersonaUnica = getIdColegiadoUnico(datosInformeVector);

		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}

		// si la persona es null es que hay mas de un colegiado de las distintas
		// designas
		// si solo hay uno comprobaremos que si hay mas de un
		// solicitante(siempre y cuando algun informe sea
		// de tipo solicitante)
		boolean isPersonaUnica = idPersonaUnica != null;

		boolean isASolicitantes = false;
		boolean isAProcurador = false;
		boolean isAJuzgado = false;
		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		if (isPersonaUnica && enviosHashtable.size() == 1) {

			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);

			informesVector = this.getInformes(informesList, userBean);

			for (int j = 0; j < informesVector.size(); j++) {
				AdmInformeBean informeBean = (AdmInformeBean) informesVector
						.get(j);
				String tiposDestinatario = informeBean.getDestinatarios();
				if (tiposDestinatario != null) {
					char[] tipoDestinatario = tiposDestinatario.toCharArray();
					for (int k = 0; k < tipoDestinatario.length; k++) {

						if (String
								.valueOf(tipoDestinatario[k])
								.equalsIgnoreCase(
										AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
							isASolicitantes = true;
							break;
						}
						if (String
								.valueOf(tipoDestinatario[k])
								.equalsIgnoreCase(
										AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)) {
							isAProcurador = true;
							break;
						}
						if (String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)) {
							form.setIsCodigoEjis("1");
							isAJuzgado = true;
							break;
						}						
					}

				}
				// Comprbamos que al ser a solicitantes haya una persona unica

			}

		}

		if (isPersonaUnica && enviosHashtable.size() == 1 && !isASolicitantes
				&& !isAProcurador && !isAJuzgado) {
			Vector vDocumentos = new Vector();
			List<ScsDesigna> designas = new ArrayList<ScsDesigna>();
			Hashtable htDatosInformeFinal = null; //Add MJM para que se muestren las etiquetas del informe designas en el mail
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				String idinstitucion = (String) datosInforme.get("idInstitucion");
				String anio = (String) datosInforme.get("anio");
				String idTurno = (String) datosInforme.get("idTurno");
				String numero = (String) datosInforme.get("numero");
				ScsDesigna designa = new ScsDesigna();
				designa.setIdinstitucion(Short.parseShort(idinstitucion));
				designa.setAnio(Short.parseShort(anio));
				designa.setIdturno(Integer.parseInt(idTurno));
				designa.setNumero(Long.parseLong(numero));
				designas.add(designa);
				
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,
						EnvioInformesGenericos.docDocument,
						EnvioInformesGenericos.comunicacionesDesigna));
				
				//Ini Mod MJM para que se muestren las etiquetas del informe designas en el mail
				ScsDesignaAdm designaAdm = new ScsDesignaAdm(userBean);
				String idPersonaJG = (String) datosInforme.get("idPersonaJG");
				Vector solicitantesVector = designaAdm.getDefendidosDesignaInforme(idInstitucion, anio,idTurno, numero,idPersonaJG );
				Hashtable hashConsultasHechas = new Hashtable();
				
				for (int j = 0; j < solicitantesVector.size(); j++){
					
					Hashtable solicitante = (Hashtable)solicitantesVector.get(j);
					String idPersonaJGInteresado = (String)solicitante.get("IDPERSONAINTERESADO");
					
					Hashtable datosInformeSeleccionado = new Hashtable();
					datosInformeSeleccionado.put("idTipoInforme", (String)datosInforme.get("idTipoInforme"));
					datosInformeSeleccionado.put("idPersonaJG",idPersonaJGInteresado);
					HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
					helperInformesAdm.setIdiomaInforme(idInstitucion, idPersonaJGInteresado, AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG,datosInforme, userBean);
					
					datosInformeSeleccionado.put("idioma", (String)datosInforme.get("idioma"));
					datosInformeSeleccionado.put("idiomaExt", (String)datosInforme.get("idiomaExt"));
					datosInformeSeleccionado.put("aSolicitantes", "S");

					String keyConsultasHechas = idInstitucion + anio + idTurno	+ numero + idPersonaJGInteresado+(String)datosInforme.get("idioma");

					if (hashConsultasHechas.containsKey(keyConsultasHechas)) {
						htDatosInformeFinal = (Hashtable) hashConsultasHechas
								.get(keyConsultasHechas);
					} else {
						htDatosInformeFinal = getDatosInformeFinalOficio(
								datosInforme, datosInformeSeleccionado,userBean);
						hashConsultasHechas.put(keyConsultasHechas,
								htDatosInformeFinal);
					}
					
				}	
				//Fin Mod MJM para que se muestren las etiquetas del informe designas en el mail

			}
			
			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);
			form.setDesignas(designas);
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersonaUnica);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			Envio envio = getEnvio(form, true, locale, userBean);
			
			//Ini Mod MJM para que se muestren las etiquetas del informe designas en el mail
			// Genera el envio:
			//envio.generarEnvio(idPersonaUnica,
			//		EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
			//		vDocumentos);
			Vector datosInformeDesigna = (Vector) htDatosInformeFinal.get("row");
			ScsDesignaBean designaBean = new ScsDesignaBean();
			
			if(datosInformeDesigna!=null && datosInformeDesigna.size()>0)
				designaBean.setOriginalHash((Hashtable) datosInformeDesigna.get(0));

			envio.generarEnvioBean(idPersonaUnica, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA, vDocumentos, designaBean);
			//Fin Mod MJM para que se muestren las etiquetas del informe designas en el mail
			
			

		} else {

			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;

			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
					userBean);
			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList,
						userBean);
				
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];

				String idioma = null;
				String idTipoInforme = null;

				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer
						.valueOf(idPlantillaEnvio));
				
				envioProgramado.setAcuseRecibo(acuseRecibo);

				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));
				
				
				envioProgramadoAdm.insert(envioProgramado);

				boolean isInformeProgramado = false;
				boolean isInformesInsertados = false;
				ArrayList alClavesDesigna = new ArrayList();
				alClavesDesigna.add("idInstitucion");
				alClavesDesigna.add("anio");
				alClavesDesigna.add("idTurno");
				alClavesDesigna.add("numero");

				
				
				// Ponemos esta lista para comprobar que no se ha insertado el
				// destinatario

				List<String> lPersonas = new ArrayList<String>();
				for (int i = 0; i < datosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) datosInformeVector.get(i);
					String idPersona = (String) ht.get("idPersona");
					String idProcurador = (String) ht.get("idProcurador");
					Vector vDefendifosDesigna = (Vector) ht
							.get("defendidosDesigna");
					String idJuzgado = (String) ht.get("idJuzgado");
					idInstitucion = (String) ht.get("idInstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					String idSolicitanteJG = (String) ht.get("idPersonaJG");

					List<EnvDestProgramInformesBean> lDestinatarios = new ArrayList<EnvDestProgramInformesBean>();
					List<String> lDestPersonas = new ArrayList<String>();
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes
								.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());
						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);

						programInformesAdm.insert(programInformes);

						informesBean = new EnvInformesGenericosBean();
						informesBean.setIdProgram(programInformes
								.getIdProgram());
						informesBean.setIdEnvio(programInformes.getIdEnvio());

						

						for (int j = 0; j < informesVector.size(); j++) {
							AdmInformeBean informeBean = (AdmInformeBean) informesVector
									.get(j);
							informesBean.setIdPlantilla(informeBean
									.getIdPlantilla());
							informesBean.setIdInstitucion(informeBean
									.getIdInstitucion());
							informesGenericoAdm.insert(informesBean);

						}

						isInformeProgramado = true;

					}

					for (int j = 0; j < informesVector.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean) informesVector
								.get(j);

						String tiposDestinatario = informeBean
								.getDestinatarios();
						informesBean.setIdPlantilla(informeBean
								.getIdPlantilla());
						if (tiposDestinatario != null) {
							char[] tipoDestinatario = tiposDestinatario
									.toCharArray();
							for (int k = 0; k < tipoDestinatario.length; k++) {
								if (idSolicitanteJG != null
										&& !idSolicitanteJG.equals("")) {

									if (String
											.valueOf(tipoDestinatario[k])
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
										for (int jta = 0; jta < vDefendifosDesigna
												.size(); jta++) {
											Hashtable htDefendido = (Hashtable) vDefendifosDesigna
													.get(jta);
											String idPersonaJG = (String) htDefendido
													.get(ScsDefendidosDesignaBean.C_IDPERSONA);
											if (idPersonaJG
													.equals(idSolicitanteJG)) {
												destProgramInformes = new EnvDestProgramInformesBean();
												destProgramInformes
														.setIdProgram(programInformes
																.getIdProgram());
												destProgramInformes
														.setIdEnvio(programInformes
																.getIdEnvio());
												destProgramInformes
														.setIdInstitucion(programInformes
																.getIdInstitucion());
												destProgramInformes
														.setIdPersona(new Long(
																idPersonaJG));
												destProgramInformes
														.setIdInstitucionPersona(new Integer(
																idInstitucion));
												destProgramInformes
														.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

												if (!lPersonas
														.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG
																+ idPersonaJG)) {
													destProgramInformesAdm
															.insert(destProgramInformes);
													countEnvios++;
													lPersonas
															.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG
																	+ idPersonaJG);

												}
												if (!lDestPersonas
														.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG
																+ idPersonaJG)) {
													lDestinatarios
															.add(destProgramInformes);
													lDestPersonas
															.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG
																	+ idPersonaJG);

												}

											}

										}
									}

								} else {
									if (String
											.valueOf(tipoDestinatario[k])
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_CENPERSONA)) {
										destProgramInformes = new EnvDestProgramInformesBean();
										destProgramInformes
												.setIdProgram(programInformes
														.getIdProgram());
										destProgramInformes
												.setIdEnvio(programInformes
														.getIdEnvio());
										destProgramInformes
												.setIdInstitucion(programInformes
														.getIdInstitucion());
										destProgramInformes
												.setIdPersona(new Long(
														idPersona));
										destProgramInformes
												.setIdInstitucionPersona(new Integer(
														idInstitucion));
										destProgramInformes
												.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
										if (!lPersonas
												.contains(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA
														+ idPersona)) {
											destProgramInformesAdm
													.insert(destProgramInformes);
											countEnvios++;
											lPersonas
													.add(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA
															+ idPersona);

										}
										if (!lDestPersonas
												.contains(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA
														+ idPersona)) {
											lDestinatarios
													.add(destProgramInformes);
											lDestPersonas
													.add(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA
															+ idPersona);

										}

									} else if (String
											.valueOf(tipoDestinatario[k])
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)) {
										if (idProcurador == null)
											continue;
										destProgramInformes = new EnvDestProgramInformesBean();
										destProgramInformes
												.setIdProgram(programInformes
														.getIdProgram());
										destProgramInformes
												.setIdEnvio(programInformes
														.getIdEnvio());
										destProgramInformes
												.setIdInstitucion(programInformes
														.getIdInstitucion());
										destProgramInformes
												.setIdPersona(new Long(
														idProcurador));
										destProgramInformes
												.setIdInstitucionPersona(new Integer(
														idInstitucion));
										destProgramInformes
												.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR);
										if (!lPersonas
												.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR
														+ idProcurador)) {
											destProgramInformesAdm
													.insert(destProgramInformes);
											countEnvios++;
											lPersonas
													.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR
															+ idProcurador);

										}
										if (!lDestPersonas
												.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR
														+ idProcurador)) {
											lDestinatarios
													.add(destProgramInformes);
											lDestPersonas
													.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR
															+ idProcurador);

										}

									} else if (String
											.valueOf(tipoDestinatario[k])
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
										for (int jta = 0; jta < vDefendifosDesigna
												.size(); jta++) {
											Hashtable htDefendido = (Hashtable) vDefendifosDesigna
													.get(jta);
											String idPersonaJG = (String) htDefendido
													.get(ScsDefendidosDesignaBean.C_IDPERSONA);
											if(idPersonaJG==null)
												idPersonaJG =(String) htDefendido.get("IDPERSONAINTERESADO");
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(new Long(
															idPersonaJG));
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes
													.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

											if (!lPersonas
													.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG
															+ idPersonaJG)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas
														.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG
																+ idPersonaJG);
											}
											if (!lDestPersonas
													.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG
															+ idPersonaJG)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas
														.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG
																+ idPersonaJG);

											}

										}
									} else if (String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)) {

										if (idJuzgado == null)
											continue;

										destProgramInformes = new EnvDestProgramInformesBean();
										destProgramInformes.setIdProgram(programInformes.getIdProgram());
										destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
										form.setIdEnvio(String.valueOf(programInformes.getIdEnvio()));
										destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
										destProgramInformes.setIdPersona(new Long(idJuzgado));
										destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));
										destProgramInformes.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO);
										
										if (!lPersonas.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado)) {
											destProgramInformesAdm.insert(destProgramInformes);
											countEnvios++;
											lPersonas.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado);
										}
										
										if (!lDestPersonas.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado)) {
											lDestinatarios.add(destProgramInformes);
											lDestPersonas.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado);
										}
										
										if(form.getIsCodigoEjis() != null && form.getIsCodigoEjis().equals("1")){
											//Se comprueba si el registro tiene codigo ejis o no
											ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm(userBean);
											Vector juzgadoVector = juzgadoAdm.busquedaJuzgado(programInformes.getIdInstitucion().toString(),
																						idJuzgado);
											if(juzgadoVector != null){
												Hashtable procuradorHashtable = (Hashtable) juzgadoVector.get(0);
												String isCodigoEJIS = (String)procuradorHashtable.get("ISCODIGOEJIS"); 
												if(isCodigoEJIS.equals("0")){
													form.setIsCodigoEjis("0");
												}
											}
										}
									}
								}
							}
						}
					}

					// if(lDestinatarios==null || lDestinatarios.size()==0)
					// throw new
					// SIGAException("messages.envios.aviso.sinDestinatarios");

					EnvValorCampoClaveBean valorCampoClave = null;

					// Iterator itClave = htClaves.keySet().iterator();
					valorCampoClave = new EnvValorCampoClaveBean();
					for (int k = 0; k < lDestinatarios.size(); k++) {
						destProgramInformes = (EnvDestProgramInformesBean) lDestinatarios
								.get(k);

						valorCampoClave.setIdValor(valorCampoClaveAdm
								.getNewIdEnvio());
						for (int j = 0; j < alClavesDesigna.size(); j++) {
							String clave = (String) alClavesDesigna.get(j);
							String valorClave = (String) ht.get(clave);
							valorCampoClave.setIdProgram(destProgramInformes
									.getIdProgram());
							valorCampoClave.setIdEnvio(destProgramInformes
									.getIdEnvio());
							valorCampoClave
									.setIdInstitucion(destProgramInformes
											.getIdInstitucion());
							valorCampoClave.setIdPersona(destProgramInformes
									.getIdPersona());
							valorCampoClave
									.setIdInstitucionPersona(destProgramInformes
											.getIdInstitucionPersona());

							valorCampoClave.setIdTipoInforme(idTipoInforme);
							valorCampoClave.setClave("idDesignas");
							valorCampoClave.setCampo(clave);
							valorCampoClave.setValor(valorClave);
							valorCampoClaveAdm.insert(valorCampoClave);

						}
					}

				}
			}
			if (countEnvios == 0)
				throw new SIGAException("messages.envios.aviso.sinDestinatarios");
			setEnvioBatch(true);

		}
		 

	}

	public void gestionarComunicacionEjg(DefinirEnviosForm form, Locale locale,
			UsrBean userBean) throws ClsExceptions, SIGAException {

		MasterReport masterReport = new MasterReport();
		// Vector vCampos = masterReport.obtenerDatosFormulario(form);
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());

		// Tenemos que obtener el letrado de la designa ya que no se saca en la
		// query del
		// paginador(¡¡¡¡Esta persona se obtiene en la jsp!!!!)
		setPersonasEjg(datosInformeVector, userBean);
		if (datosInformeVector.size() == 0)
			throw new SIGAException("messages.envios.aviso.sinDestinatarios");

		Hashtable destinatariosHashtable = getDestinatariosEjg(datosInformeVector);

		// String idTipoEnvio = form.getIdTipoEnvio();
		// boolean isEnvioSMS =
		// Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_BUROSMS ||
		// Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_SMS;
//		boolean isEnvioSMS = false;

		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		// String datosInforme = form.getDatosInforme();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		String keyEnvios = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}

		// si la persona es null es que hay mas de un colegiado de las distintas
		// designas
		// si solo hay uno comprobaremos que si hay mas de un
		// solicitante(siempre y cuando algun informe sea
		// de tipo solicitante)
		boolean isDestinatarioUnico = destinatariosHashtable != null
				&& destinatariosHashtable.size() == 1;
		Vector<AdmInformeBean> informesVector = null;
		boolean isASolicitantes = false;
		boolean isAJuzgado = false;
		boolean isAContrario = false;
		if (isDestinatarioUnico && enviosHashtable.size() == 1) {

			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);

			informesVector = this.getInformes(informesList, userBean);

			for (int j = 0; j < informesVector.size(); j++) {
				AdmInformeBean informeBean = (AdmInformeBean) informesVector
						.get(j);
				String tiposDestinatario = informeBean.getDestinatarios();
				if (tiposDestinatario != null) {
					char[] tipoDestinatario = tiposDestinatario.toCharArray();
					for (int k = 0; k < tipoDestinatario.length; k++) {

						if (String
								.valueOf(tipoDestinatario[k])
								.equalsIgnoreCase(
										AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
							isASolicitantes = true;
							break;
						}
						
						if (String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)) {
							isAJuzgado = true;
							break;
						}
						
						if (String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSCONTRARIOSJG)) {
							isAContrario = true;
							break;
						}	

					}

				}
				// Comprbamos que al ser a solicitantes haya una persona unica

			}

		}

		if (isDestinatarioUnico && enviosHashtable.size() == 1
				&& !isASolicitantes &&!isAJuzgado && !isAContrario) {
			List<ScsEjg> ejgs = new ArrayList<ScsEjg>();
			Vector vDocumentos = new Vector();
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				idInstitucion = (String) datosInforme.get("idinstitucion");
				if (idInstitucion == null)
					idInstitucion = (String) datosInforme.get("idInstitucion");

				String anio = (String) datosInforme.get("anio");
				String idTipoEJG = "";
				if ((String) datosInforme.get("idtipo") != null) {
					idTipoEJG = (String) datosInforme.get("idtipo");
				} else {
					idTipoEJG = (String) datosInforme.get("idTipoEJG");
				}
				// BEGIN BNS INC_08446_SIGA SUSTITUIMOS EL ID DE LA TABLA DE EJG POR EL CÓDIGO DE EJG
				String numero = (String) datosInforme.get("numero");
				ScsEjg ejg = new ScsEjg();
				ejg.setIdinstitucion(Short.parseShort(idInstitucion));
				ejg.setAnio(Short.parseShort(anio));
				ejg.setIdtipoejg(Short.parseShort(idTipoEJG));
				ejg.setNumero(Long.parseLong(numero));
				ejgs.add(ejg);
				
				
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,
						EnvioInformesGenericos.docDocument,
						EnvioInformesGenericos.comunicacionesEjg));

			}

			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);
			form.setEjgs(ejgs);

			// Tenemos que mirar si es persona JG o Persona
			String idPersonaUnica = (String) destinatariosHashtable.keySet()
					.iterator().next();
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersonaUnica);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			Envio envio = getEnvio(form, true, locale, userBean);

			// Genera el envio:
			
			envio.generarEnvio(idPersonaUnica,
					(String) destinatariosHashtable.get(idPersonaUnica),
					vDocumentos);

		} else {

			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;

			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
					userBean);
			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList,
						userBean);
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];

				String idioma = null;
				String idTipoInforme = null;
				String plantillas = null;

				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer
						.valueOf(idPlantillaEnvio));
				envioProgramado.setAcuseRecibo(acuseRecibo);
				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));

				envioProgramadoAdm.insert(envioProgramado);

				boolean isInformeProgramado = false;
				boolean isInformesInsertados = false;
				ArrayList alClavesEjg = new ArrayList();
				alClavesEjg.add("idInstitucion");
				alClavesEjg.add("anio");
				alClavesEjg.add("idTipoEJG");
				alClavesEjg.add("numero");

				// Ponemos esta lista para comprobar que no se ha insertado el
				// destinatario

				List<String> lPersonas = new ArrayList<String>();
				for (int i = 0; i < datosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) datosInformeVector.get(i);
					Vector personasDesignasEjg = (Vector) ht
							.get("personasDesignasEjg");

					Vector vSolicitantesEjg = (Vector) ht
							.get("solicitantesEjg");
					Vector vContrariosEjg = (Vector) ht
							.get("contrariosEjg");

					String idJuzgado = (String) ht.get("idJuzgado");
					idInstitucion = (String) ht.get("idinstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					String idTipoEjg = "";
					if ((String) ht.get("idtipo") != null) {
						idTipoEjg = (String) ht.get("idtipo");
					} else {
						idTipoEjg = (String) ht.get("idTipoEJG");
					}
					ht.put("idTipoEJG", idTipoEjg);
					ht.put("idInstitucion", idInstitucion);
					// ht.put("anio", (String) ht.get("anio"));
					// ht.put("numero", (String) ht.get("numero"));

					// ArrayList alDesignas = (ArrayList) ht.get("idDesignas");
					plantillas = (String) ht.get("plantillas");
					String idSolicitanteJG = (String) ht.get("idPersonaJG");

					List<EnvDestProgramInformesBean> lDestinatarios = new ArrayList<EnvDestProgramInformesBean>();
					List<String> lDestPersonas = new ArrayList<String>();
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes
								.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());
						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);

						programInformesAdm.insert(programInformes);

						informesBean = new EnvInformesGenericosBean();
						informesBean.setIdProgram(programInformes
								.getIdProgram());
						informesBean.setIdEnvio(programInformes.getIdEnvio());
						informesBean.setIdInstitucion(programInformes
								.getIdInstitucion());
						

						for (int j = 0; j < informesVector.size(); j++) {
							AdmInformeBean informeBean = (AdmInformeBean) informesVector
									.get(j);
							informesBean.setIdPlantilla(informeBean
									.getIdPlantilla());
							informesBean.setIdInstitucion(informeBean
									.getIdInstitucion());
							informesGenericoAdm.insert(informesBean);

						}

						isInformeProgramado = true;

					}

					for (int j = 0; j < informesVector.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean) informesVector
								.get(j);
						String tiposDestinatario = informeBean
								.getDestinatarios();
						informesBean.setIdPlantilla(informeBean
								.getIdPlantilla());
						if (tiposDestinatario != null) {
							
							
								if (idSolicitanteJG != null
										&& !idSolicitanteJG.equals("")) {

									if (String
											.valueOf(tiposDestinatario)
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
										for (int jta = 0; jta < vSolicitantesEjg
												.size(); jta++) {
											Hashtable htSolicitante = (Hashtable) vSolicitantesEjg
													.get(jta);
											String idPersonaJG = (String) htSolicitante
													.get(ScsDefendidosDesignaBean.C_IDPERSONA);
											if (idPersonaJG
													.equals(idSolicitanteJG)) {
												destProgramInformes = new EnvDestProgramInformesBean();
												destProgramInformes
														.setIdProgram(programInformes
																.getIdProgram());
												destProgramInformes
														.setIdEnvio(programInformes
																.getIdEnvio());
												destProgramInformes
														.setIdInstitucion(programInformes
																.getIdInstitucion());
												destProgramInformes
														.setIdPersona(new Long(
																idPersonaJG));
												destProgramInformes
														.setIdInstitucionPersona(new Integer(
																idInstitucion));
												destProgramInformes
														.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

												if (!lPersonas
														.contains(idPersonaJG)) {
													destProgramInformesAdm
															.insert(destProgramInformes);
													countEnvios++;
													lPersonas.add(idPersonaJG);
												}

												if (!lDestPersonas
														.contains(idPersonaJG)) {
													lDestinatarios
															.add(destProgramInformes);
													lDestPersonas
															.add(idPersonaJG);

												}
											}

										}
									}

								} else {
									if (String
											.valueOf(tiposDestinatario)
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_CENPERSONA)
											&& personasDesignasEjg != null) {
										for (int jta = 0; jta < personasDesignasEjg
												.size(); jta++) {
											Hashtable htPersonaDesigna = (Hashtable) personasDesignasEjg
													.get(jta);
											String idPersona = (String) htPersonaDesigna
													.get("IDPERSONA");
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(new Long(
															idPersona));
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes
													.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);

											if (!lPersonas.contains(idPersona)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersona);
											}

											if (!lDestPersonas
													.contains(idPersona)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersona);

											}

										}

									} else if (String
											.valueOf(tiposDestinatario)
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)
											&& vSolicitantesEjg != null) {
										for (int jta = 0; jta < vSolicitantesEjg
												.size(); jta++) {
											Hashtable htSolicitante = (Hashtable) vSolicitantesEjg
													.get(jta);
											String idPersonaJG = (String) htSolicitante
													.get(ScsDefendidosDesignaBean.C_IDPERSONA);
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(new Long(
															idPersonaJG));
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes
													.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

											if (!lPersonas
													.contains(idPersonaJG)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersonaJG);
											}

											if (!lDestPersonas
													.contains(idPersonaJG)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersonaJG);

											}

										}
									} else if (String.valueOf(tiposDestinatario).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)) {
	
										if (idJuzgado == null)
											continue;

										destProgramInformes = new EnvDestProgramInformesBean();
										destProgramInformes.setIdProgram(programInformes.getIdProgram());
										destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
										destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
										destProgramInformes.setIdPersona(new Long(idJuzgado));
										destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));
										destProgramInformes.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO);
										
										if (!lPersonas.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado)) {
											destProgramInformesAdm.insert(destProgramInformes);
											countEnvios++;
											lPersonas.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado);
										}
										
										if (!lDestPersonas.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado)) {
											lDestinatarios.add(destProgramInformes);
											lDestPersonas.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado);
										}										
									}else if (String
											.valueOf(tiposDestinatario)
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSCONTRARIOSJG)
											&& vContrariosEjg != null) {
										for (int jta = 0; jta < vContrariosEjg
												.size(); jta++) {
											Hashtable htContrario = (Hashtable) vContrariosEjg
													.get(jta);
											String idPersonaJG = (String) htContrario
													.get(ScsContrariosEJGBean.C_IDPERSONA);
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(new Long(
															idPersonaJG));
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes
													.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG);

											if (!lPersonas
													.contains(idPersonaJG)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersonaJG);
											}

											if (!lDestPersonas
													.contains(idPersonaJG)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersonaJG);

											}

										}
									}
								}
							
						}
					}

					// if(lDestinatarios==null || lDestinatarios.size()==0)
					// throw new
					// SIGAException("messages.envios.aviso.sinDestinatarios");

					EnvValorCampoClaveBean valorCampoClave = null;

					// Iterator itClave = htClaves.keySet().iterator();
					valorCampoClave = new EnvValorCampoClaveBean();
					for (int k = 0; k < lDestinatarios.size(); k++) {
						destProgramInformes = (EnvDestProgramInformesBean) lDestinatarios
								.get(k);

						valorCampoClave.setIdValor(valorCampoClaveAdm
								.getNewIdEnvio());
						for (int pp = 0; pp < alClavesEjg.size(); pp++) {
							String clave = (String) alClavesEjg.get(pp);
							String valorClave = (String) ht.get(clave);
							valorCampoClave.setIdProgram(destProgramInformes
									.getIdProgram());
							valorCampoClave.setIdEnvio(destProgramInformes
									.getIdEnvio());
							valorCampoClave
									.setIdInstitucion(destProgramInformes
											.getIdInstitucion());
							valorCampoClave.setIdPersona(destProgramInformes
									.getIdPersona());
							valorCampoClave
									.setIdInstitucionPersona(destProgramInformes
											.getIdInstitucionPersona());

							valorCampoClave.setIdTipoInforme(idTipoInforme);
							valorCampoClave.setClave("idEjgs");
							valorCampoClave.setCampo(clave);
							valorCampoClave.setValor(valorClave);
							valorCampoClaveAdm.insert(valorCampoClave);

						}
					}

				}
				if (countEnvios == 0)
					throw new SIGAException(
							"messages.envios.aviso.sinDestinatarios");

				setEnvioBatch(true);

			}

		}

	}

	public void gestionarComunicacionJustificaciones(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {

		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		// Tenemos que obtener el letrado de la designa ya que no se saca en la
		// query del
		// paginador(¡¡¡¡Esta persona se obtiene en la jsp!!!!)
		if (datosInformeVector.size() == 0)
			throw new SIGAException("No se ha generado ningún envio");

		String idPersona = getIdColegiadoUnico(datosInformeVector);

		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}
		
		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		
		
		if (idPersona != null && enviosHashtable.size() == 1) {
			// String claveIterante = this.getClaveIterante(vCampos);
			// if(claveIterante!=null){
			// vCampos = this.setCamposIterantes(vCampos,"idPersona");
			// }
			Vector vDocumentos = new Vector();
			
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			informesVector = this.getInformes(informesList, userBean);			
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,
						EnvioInformesGenericos.docDocument,
						EnvioInformesGenericos.comunicacionesJustificacion));

			}
			
			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			
			Envio envio = getEnvio(form, true, locale, userBean);

			// Genera el envio:
			// Tenemos que mirar si es persona JG o Persona
			envio.generarEnvio(idPersona,
					EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
					vDocumentos);

		} else {
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			
			
			// vCampos = this.obtenerDatosFormulario(form);
			ScsDesignasLetradoAdm admDesignas = new ScsDesignasLetradoAdm(
					userBean);
			InformeJustificacionMasivaForm informeJustificacionMasivaForm = new InformeJustificacionMasivaForm();
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;

			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
					userBean);
			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList,
						userBean);
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];
				for (int i = 0; i < datosInformeVector.size(); i++) {
					// solo habria uno ya solo hay un formularim, pero daigua
					// iteramos
					Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
					String fichaColegial = (String) datosInforme
							.get("fichaColegial");
					informeJustificacionMasivaForm.setFichaColegial(Boolean
							.valueOf(fichaColegial));
					idInstitucion = (String) datosInforme.get("idInstitucion");
					informeJustificacionMasivaForm.setIdInstitucion(idInstitucion);
					String mostrarTodas = (String) datosInforme.get("mostrarTodas");
					informeJustificacionMasivaForm.setMostrarTodas(mostrarTodas);
					String activarRestriccionesFicha = (String) datosInforme
							.get("activarRestriccionesFicha");
					informeJustificacionMasivaForm
							.setActivarRestriccionesFicha(Boolean
									.valueOf(activarRestriccionesFicha));
					informeJustificacionMasivaForm
							.setIdPersona((String) datosInforme.get("idPersona"));
					informeJustificacionMasivaForm.setAnio((String) datosInforme
							.get("anio"));
					informeJustificacionMasivaForm
							.setActuacionesPendientes((String) datosInforme
									.get("actuacionesPendientes"));
	
					informeJustificacionMasivaForm.setEstado((String) datosInforme
							.get("estado"));
					informeJustificacionMasivaForm
							.setFechaJustificacionDesde((String) datosInforme
									.get("fechaJustificacionDesde"));
					informeJustificacionMasivaForm
							.setFechaJustificacionHasta((String) datosInforme
									.get("fechaJustificacionHasta"));
					informeJustificacionMasivaForm
							.setFechaDesde((String) datosInforme.get("fechaDesde"));
					informeJustificacionMasivaForm
							.setFechaHasta((String) datosInforme.get("fechaHasta"));
	
					informeJustificacionMasivaForm
							.setInteresadoApellidos((String) datosInforme
									.get("interesadoApellidos"));
					informeJustificacionMasivaForm
							.setInteresadoNombre((String) datosInforme
									.get("interesadoNombre"));
					informeJustificacionMasivaForm
							.setIncluirEjgNoFavorable((String) datosInforme
									.get("incluirEjgNoFavorable"));
					informeJustificacionMasivaForm
							.setIncluirEjgSinResolucion((String) datosInforme
									.get("incluirEjgSinResolucion"));
					informeJustificacionMasivaForm
							.setIncluirSinEJG((String) datosInforme
									.get("incluirSinEJG"));
					informeJustificacionMasivaForm
							.setIncluirEjgPteCAJG((String) datosInforme
									.get("incluirEjgPteCAJG"));
					String idTipoInforme = (String) datosInforme
							.get("idTipoInforme");
	
					Hashtable htPersonas = admDesignas
							.getPersonasSalidaInformeJustificacion(
									informeJustificacionMasivaForm, false);
	
					String idioma = null;
					
	
					envioProgramado = new EnvEnvioProgramadoBean();
					envioProgramado.setIdEnvio(envioProgramadoAdm
							.getNewIdEnvio(idInstitucion));
					envioProgramado.setIdInstitucion(new Integer(idInstitucion));
					envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
					envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvio));
					
					envioProgramado.setAcuseRecibo(acuseRecibo);
					envioProgramado.setNombre(form.getNombre());
					envioProgramado.setEstado(ClsConstants.DB_FALSE);
					envioProgramado.setFechaProgramada(getFechaProgramada(
							form.getFechaProgramada(), locale, userBean));
	
					envioProgramadoAdm.insert(envioProgramado);
	
					boolean isInformeProgramado = false;
					boolean isInformesInsertados = false;
					ArrayList alClavesJustificacion = new ArrayList();
					alClavesJustificacion.add("fichaColegial");
					alClavesJustificacion.add("idInstitucion");
					alClavesJustificacion.add("mostrarTodas");
					alClavesJustificacion.add("activarRestriccionesFicha");
					alClavesJustificacion.add("incluirEjgNoFavorable");
					alClavesJustificacion.add("incluirEjgSinResolucion");
					alClavesJustificacion.add("incluirSinEJG");
					alClavesJustificacion.add("incluirEjgPteCAJG");
	
					alClavesJustificacion.add("idPersona");
					alClavesJustificacion.add("actuacionesPendientes");
					alClavesJustificacion.add("anio");
					alClavesJustificacion.add("estado");
					alClavesJustificacion.add("fechaJustificacionDesde");
					alClavesJustificacion.add("fechaJustificacionHasta");
					alClavesJustificacion.add("fechaDesde");
					alClavesJustificacion.add("fechaHasta");
					alClavesJustificacion.add("interesadoApellidos");
					alClavesJustificacion.add("interesadoNombre");
	
					// vCampos =
					// this.setCamposIterantes(vCampos,alClavesDesigna,"idDesignas");
	
					// Ponemos esta lista para comprobar que no se ha insertado el
					// destinatario
	
					List<String> lPersonas = new ArrayList<String>();
					if (htPersonas == null || htPersonas.size() < 1) {
						throw new SIGAException("messages.informes.ficheroVacio");
	
					} else {
						// Hashtable htPersonas =
						// getHashPersonaInforme(vRowsInforme,usr,idInstitucion);
						Iterator itePersonas = htPersonas.keySet().iterator();
	
						while (itePersonas.hasNext()) {
							idPersona = (String) itePersonas.next();
	
							
							// List<EnvDestProgramInformesBean> lDestinatarios = new
							// ArrayList<EnvDestProgramInformesBean>();
							// List<String> lDestPersonas = new ArrayList<String>();
							if (!isInformeProgramado) {
								programInformes = new EnvProgramInformesBean();
								programInformes.setIdProgram(programInformesAdm
										.getNewIdProgramInformes(idInstitucion));
								programInformes.setIdEnvio(envioProgramado
										.getIdEnvio());
								programInformes.setIdInstitucion(envioProgramado
										.getIdInstitucion());
								idioma = userBean.getLanguage();
								programInformes.setIdioma(new Integer(idioma));
								programInformes.setEstado(ClsConstants.DB_FALSE);
								programInformes.setPlantillas("MULTIPLES");
								programInformes.setIdTipoInforme(idTipoInforme);
	
								programInformesAdm.insert(programInformes);
	
								informesBean = new EnvInformesGenericosBean();
								informesBean.setIdProgram(programInformes
										.getIdProgram());
								informesBean.setIdEnvio(programInformes
										.getIdEnvio());
								for (int j = 0; j < informesVector.size(); j++) {
									AdmInformeBean informeBean = (AdmInformeBean) informesVector
											.get(j);
									informesBean.setIdPlantilla(informeBean
											.getIdPlantilla());
									informesBean.setIdInstitucion(informeBean
											.getIdInstitucion());
									informesGenericoAdm.insert(informesBean);
	
								}
	
								isInformeProgramado = true;
	
							}
	
							destProgramInformes = new EnvDestProgramInformesBean();
							destProgramInformes.setIdProgram(programInformes
									.getIdProgram());
							destProgramInformes.setIdEnvio(programInformes
									.getIdEnvio());
							destProgramInformes.setIdInstitucion(programInformes
									.getIdInstitucion());
							destProgramInformes.setIdPersona(new Long(idPersona));
							destProgramInformes
									.setIdInstitucionPersona(new Integer(
											idInstitucion));
							destProgramInformes
									.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
							destProgramInformesAdm.insert(destProgramInformes);
	
							EnvValorCampoClaveBean valorCampoClave = null;
							// Iterator itClave = htClaves.keySet().iterator();
							valorCampoClave = new EnvValorCampoClaveBean();
							// for (int k = 0; k < lDestinatarios.size(); k++) {
							// destProgramInformes = (EnvDestProgramInformesBean)
							// lDestinatarios.get(k);
	
							valorCampoClave.setIdValor(valorCampoClaveAdm
									.getNewIdEnvio());
							for (int j = 0; j < alClavesJustificacion.size(); j++) {
								String clave = (String) alClavesJustificacion
										.get(j);
								String valorClave = (String) datosInforme
										.get(clave);
								if (valorClave == null
										|| valorClave.trim().equals(""))
									continue;
	
								valorCampoClave.setIdProgram(destProgramInformes
										.getIdProgram());
								valorCampoClave.setIdEnvio(destProgramInformes
										.getIdEnvio());
								valorCampoClave
										.setIdInstitucion(destProgramInformes
												.getIdInstitucion());
								valorCampoClave.setIdPersona(destProgramInformes
										.getIdPersona());
								valorCampoClave
										.setIdInstitucionPersona(destProgramInformes
												.getIdInstitucionPersona());
	
								valorCampoClave.setIdTipoInforme(idTipoInforme);
								valorCampoClave.setClave("justificaciones");
								valorCampoClave.setCampo(clave);
								valorCampoClave.setValor(valorClave);
								valorCampoClaveAdm.insert(valorCampoClave);
	
							}
							// }
						}
					}
	
				}
			}
			setEnvioBatch(true);

		}
		// return isEnvioBatch;

	}

	/**
	 * Metodo que gestiona el envio y comunicacion de Expedientes
	 * 
	 * @param form
	 * @param locale
	 * @param userBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void gestionarComunicacionExpedientes(DefinirEnviosForm form,
			Locale locale, UsrBean userBean) throws ClsExceptions,
			SIGAException {

		
		MasterReport masterReport = new MasterReport();
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());
		
		setPersonasExpedientes(datosInformeVector, userBean);
		
		if (datosInformeVector.size() == 0)
			throw new SIGAException("messages.envios.aviso.sinDestinatarios");

		Hashtable destinatariosHashtable = getDestinatariosExpedientes(datosInformeVector);
		

		
		

		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}

		Vector<AdmInformeBean> informesVector = null;
		String keyEnvios = null;
		
		
		if (enviosHashtable.size() == 1 && destinatariosHashtable!=null && destinatariosHashtable.size()==1) {
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			informesVector = this.getInformes(informesList, userBean);
			
			String idPersona = (String)destinatariosHashtable.keySet().iterator().next();
			String tiposDestinatario = (String)destinatariosHashtable.get(idPersona);
			if(tiposDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)){
				tiposDestinatario = "idPersona";
			}
			else if(tiposDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)){
				tiposDestinatario = "idPersonaJG";
			}
			else if(tiposDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)){
				tiposDestinatario = "idProcurador";
			}
			
			Vector vDocumentos = new Vector();
			ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm(userBean);
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				datosInforme.put(tiposDestinatario, idPersona);
				
				
				

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
				String idInstitucion2 = (String) datosInforme
						.get("idInstitucion");
				String anio = (String) datosInforme.get("anioExpediente");
				String numero = (String) datosInforme.get("numeroExpediente");
				String idInstitucionTipoExp = (String) datosInforme
						.get("idInstitucionTipoExp");
				String idTipoExp = (String) datosInforme.get("idTipoExp");

				
				// voy a mirar si alguno de los informes es asolicitantes

				// Obtengo los datos de la consulta.
				// A CADA UNO DE LOS REGISTROS DE ESTA CONSULTA SE LE ENVÍAN
				// TODOS LOS DOCUMENTOS
				// DE LAS PLANTILLAS OBTENIDAS ARRIBA, RESUELTOS PARA EL
				// DESTINATARIO.
				
//				Vector vDatosInformeFinal = expedienteAdm
//						.getDatosInformeExpediente(idInstitucion2,
//								idInstitucionTipoExp, idTipoExp, anio, numero,
//								idPersona,tiposDestinatario, true);
				
				// generando los documentos para cada persona
				
				
				
				
				
				
				
					vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,informesVector,
									userBean,
									EnvioInformesGenericos.docDocument,
									EnvioInformesGenericos.comunicacionesExpedientes));
				
					
				
				

			}

			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);
			
			
			
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			Envio envio = getEnvio(form, true, locale, userBean);

			// Genera el envio:
			
			envio.generarEnvio(idPersona,EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA, vDocumentos);
			

			/*
			// //////////////////*************************************************************

			// recorro los expedientes a enviar
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);
			
			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);
			
			informesVector = this.getInformes(informesList, userBean);
			
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);

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
				String idInstitucion2 = (String) datosInforme
						.get("idInstitucion");
				String anio = (String) datosInforme.get("anioExpediente");
				String numero = (String) datosInforme.get("numeroExpediente");
				String idInstitucionTipoExp = (String) datosInforme
						.get("idInstitucionTipoExp");
				String idTipoExp = (String) datosInforme.get("idTipoExp");

				
				// voy a mirar si alguno de los informes es asolicitantes

				// Obtengo los datos de la consulta.
				// A CADA UNO DE LOS REGISTROS DE ESTA CONSULTA SE LE ENVÍAN
				// TODOS LOS DOCUMENTOS
				// DE LAS PLANTILLAS OBTENIDAS ARRIBA, RESUELTOS PARA EL
				// DESTINATARIO.
				ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm(userBean);
				Vector vDatosInformeFinal = expedienteAdm
						.getDatosInformeExpediente(idInstitucion2,
								idInstitucionTipoExp, idTipoExp, anio, numero,
								idPersona2,null, true);

				// Anotación en cada expediente
				
				// RECORRER LOS DATOSINFORMEFINAL
				// Por cada destinatario real
				for (int j = 0; j < vDatosInformeFinal.size(); j++) {
					Hashtable datoReal = (Hashtable) vDatosInformeFinal.get(j);
					String idPersonaReal = (String) datoReal
							.get("IDPERSONA_DEST");
					String idDireccionReal = (String) datoReal
							.get("IDDIRECCION_DEST");
					datoReal.put("idTipoInforme", "EXP");
					datoReal.put("idPersona",
							(String) datosInforme.get("idPersona"));
					datoReal.put("idInstitucion",
							(String) datosInforme.get("idInstitucion"));
					datoReal.put("anioExpediente",
							(String) datosInforme.get("anioExpediente"));
					datoReal.put("numeroExpediente",
							(String) datosInforme.get("numeroExpediente"));
					datoReal.put("idInstitucionTipoExp",
							(String) datosInforme.get("idInstitucionTipoExp"));
					datoReal.put("idTipoExp",
							(String) datosInforme.get("idTipoExp"));
//					datoReal.put("plantillas",
//							(String) datosInforme.get("plantillas"));
//					datoReal.put("aSolicitantes", (aSolicitantes) ? "S" : "N");

					if (idPersonaReal != null
							&& !idPersonaReal.trim().equals("")) {
						// Antes de crear el envío voy a poner el nombre
						// adecuado
						form.setNombre(UtilidadesString.getMensajeIdioma(
								userBean,
								"informes.genericos.expedientes.asunto")
								+ " " + (String) datoReal.get("NOMBRE_DEST"));
						form.setIdPersona(idPersonaReal);
						// Hago un envio
						CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
						String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersonaReal);
						form.setNombre(form.getNombre()+" "+nombreyapellidos);
						
						Envio envio = getEnvio(form, true, locale, userBean);
						// obtengo sus documentos según vPlantillas

						Vector vDocumentos = new Vector();
						vDocumentos
								.addAll(this
										.getDocumentosAEnviar(
												datoReal,
												informesVector,
												userBean,
												EnvioInformesGenericos.docDocument,
												EnvioInformesGenericos.comunicacionesExpedientes));
						// Genera el envio:

						envio.generarEnvioDireccionEspecifica(idPersonaReal,
								idDireccionReal, vDocumentos);
						Envio.generarComunicacionExpediente(idInstitucion2,
									new Integer(idInstitucionTipoExp), new Integer(
											idTipoExp), new Integer(numero),
									new Integer(anio), idPersonaReal, userBean);

					}
				}

			}*/
			

		} else {
			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;

			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
					userBean);
			
			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList,
						userBean);
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];
			
				String idioma = null;
				String idTipoInforme = null;
				String plantillas = null;
				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvio));
				
				envioProgramado.setAcuseRecibo(acuseRecibo);
				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));
	
				envioProgramadoAdm.insert(envioProgramado);
	
				boolean isInformeProgramado = false;
				boolean isInformesInsertados = false;
				ArrayList alClaves = new ArrayList();
				alClaves.add("idInstitucion");
				alClaves.add("anioExpediente");
				alClaves.add("numeroExpediente");
				alClaves.add("idInstitucionTipoExp");
				alClaves.add("idTipoExp");

				// Ponemos esta lista para comprobar que no se ha insertado el
				// destinatario

				List<String> lPersonas = new ArrayList<String>();
				
				
				
				for (int i = 0; i < datosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) datosInformeVector.get(i);
					
					
					List denunciados = (List) ht.get("denunciados");
					List denunciantes = (List) ht.get("denunciantes");
					List partes = (List) ht.get("partes");
					
//					String idPersona = (String) ht.get("idPersona");
					idInstitucion = (String) ht.get("idInstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					String anio = (String) ht.get("anioExpediente");
					String numero = (String) ht.get("numeroExpediente");
					String idInstitucionTipoExp = (String) ht	.get("idInstitucionTipoExp");
					String idTipoExp = (String) ht.get("idTipoExp");
					List<EnvDestProgramInformesBean> lDestinatarios = new ArrayList<EnvDestProgramInformesBean>();
					List<String> lDestPersonas = new ArrayList<String>();
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes
								.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());
						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);

						programInformesAdm.insert(programInformes);

						informesBean = new EnvInformesGenericosBean();
						informesBean.setIdProgram(programInformes
								.getIdProgram());
						informesBean.setIdEnvio(programInformes.getIdEnvio());
						informesBean.setIdInstitucion(programInformes
								.getIdInstitucion());
						

						for (int j = 0; j < informesVector.size(); j++) {
							AdmInformeBean informeBean = (AdmInformeBean) informesVector
									.get(j);
							String tiposDestinatario = informeBean.getDestinatarios();
							
							informesBean.setIdPlantilla(informeBean
									.getIdPlantilla());
							informesBean.setIdInstitucion(informeBean
									.getIdInstitucion());
							informesGenericoAdm.insert(informesBean);

						}

						isInformeProgramado = true;

					}
					
					for (int j = 0; j < informesVector.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean) informesVector
								.get(j);
						String tiposDestinatario = informeBean
								.getDestinatarios();
						informesBean.setIdPlantilla(informeBean
								.getIdPlantilla());
													

								
									if (tiposDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_CENPERSONA)
											&& denunciados != null && denunciados.size()>0) {
										for (int jta = 0; jta < denunciados.size(); jta++) {
											ExpDenunciadoBean denunciante = (ExpDenunciadoBean) denunciados.get(jta);
											String idPersona = denunciante.getIdPersona().toString();
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes.setIdProgram(programInformes.getIdProgram());
											destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
											destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
											destProgramInformes.setIdPersona(new Long(idPersona));
											destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));
											destProgramInformes.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);
											if (!lPersonas.contains(idPersona)) {
												destProgramInformesAdm.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersona);
											}

											if (!lDestPersonas
													.contains(idPersona)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersona);

											}

										}
										
										//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE SOLICITANTESEJG COMO DENUNCIANTES
									} else if (tiposDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)
											&& denunciantes != null && denunciantes.size()>0 ) {
										for (int jta = 0; jta < denunciantes
												.size(); jta++) {
											ExpDenuncianteBean denuncianteBean = (ExpDenuncianteBean) denunciantes
													.get(jta);
											String idPersonaJG = denuncianteBean.getIdPersona().toString();
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(denuncianteBean.getIdPersona());
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

											if (!lPersonas
													.contains(idPersonaJG)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersonaJG);
											}

											if (!lDestPersonas
													.contains(idPersonaJG)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersonaJG);

											}

										}
										
										
										//ATENCION EN INFORMES DE EXPEDIENTES USAMOS LAS CONSTANTES DE PROCURADORS
									} else if (tiposDestinatario.equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)
											&& partes!=null && partes.size()>0) {
										
										for (int jta = 0; jta < partes
												.size(); jta++) {
											ExpPartesBean partesBean = (ExpPartesBean) partes.get(jta);
											String idPersonaJG = partesBean.getIdPersona().toString();
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(new Long(partesBean.getIdPersona()));
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR);

											if (!lPersonas
													.contains(idPersonaJG)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersonaJG);
											}

											if (!lDestPersonas
													.contains(idPersonaJG)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersonaJG);

											}

										}
										
										
									}
							
						
					}
					
					
					

					EnvValorCampoClaveBean valorCampoClave = null;

					// Iterator itClave = htClaves.keySet().iterator();
					valorCampoClave = new EnvValorCampoClaveBean();
					for (int k = 0; k < lDestinatarios.size(); k++) {
						destProgramInformes = (EnvDestProgramInformesBean) lDestinatarios
								.get(k);

						valorCampoClave.setIdValor(valorCampoClaveAdm
								.getNewIdEnvio());
						for (int pp = 0; pp < alClaves.size(); pp++) {
							String clave = (String) alClaves.get(pp);
							String valorClave = (String) ht.get(clave);
							valorCampoClave.setIdProgram(destProgramInformes
									.getIdProgram());
							valorCampoClave.setIdEnvio(destProgramInformes
									.getIdEnvio());
							valorCampoClave
									.setIdInstitucion(destProgramInformes
											.getIdInstitucion());
							valorCampoClave.setIdPersona(destProgramInformes
									.getIdPersona());
							valorCampoClave
									.setIdInstitucionPersona(destProgramInformes
											.getIdInstitucionPersona());

							valorCampoClave.setIdTipoInforme(idTipoInforme);
							valorCampoClave.setClave("idExpedientes");
							valorCampoClave.setCampo(clave);
							valorCampoClave.setValor(valorClave);
							valorCampoClaveAdm.insert(valorCampoClave);

						}
					}

				}
				if (countEnvios == 0)
					throw new SIGAException(
							"messages.envios.aviso.sinDestinatarios");

				setEnvioBatch(true);
		/*		keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList,
						userBean);
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];
			
				String idioma = null;
				String idTipoInforme = null;
				String plantillas = null;
				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvio));
				
				envioProgramado.setAcuseRecibo(acuseRecibo);
				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));
	
				envioProgramadoAdm.insert(envioProgramado);
	
				boolean isInformeProgramado = false;
				ArrayList alClaves = new ArrayList();
				alClaves.add("idInstitucion");
				alClaves.add("anioExpediente");
				alClaves.add("numeroExpediente");
				alClaves.add("idInstitucionTipoExp");
				alClaves.add("idTipoExp");

				Vector auxDatosInformeVector = this.setCamposIterantes(datosInformeVector, alClaves,
						"idExpedientes");

	
				for (int i = 0; i < auxDatosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) auxDatosInformeVector.get(i);
					String idPersona = (String) ht.get("idPersona");
					idInstitucion = (String) ht.get("idInstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					ArrayList alExpedientes = (ArrayList) ht.get("idExpedientes");
					String idPersona2 = (String) ht.get("idPersona");
					String idInstitucion2 = (String) ht.get("idInstitucion");
					String anio = (String) ht.get("anioExpediente");
					String numero = (String) ht.get("numeroExpediente");
					String idInstitucionTipoExp = (String) ht
							.get("idInstitucionTipoExp");
					String idTipoExp = (String) ht.get("idTipoExp");
					// Anotación en cada expediente
					// Envio.generarComunicacionExpediente(idInstitucion2,new
					// Integer(idInstitucionTipoExp),
					// new Integer(idTipoExp),new Integer(numero),new
					// Integer(anio),idPersona2,userBean);
	
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						// envioProgramado.setProgramInformes(programInformes);
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());
						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);
	
						programInformesAdm.insert(programInformes);
						isInformeProgramado = true;
	
					}
	
					destProgramInformes = new EnvDestProgramInformesBean();
					destProgramInformes
							.setIdProgram(programInformes.getIdProgram());
					destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
					destProgramInformes.setIdInstitucion(programInformes
							.getIdInstitucion());
					destProgramInformes.setIdPersona(new Long(idPersona));
					// destProgramInformes.setClaves(claves.toString());
					destProgramInformes.setIdInstitucionPersona(new Integer(
							idInstitucion));
	
					destProgramInformesAdm.insert(destProgramInformes);
	
					EnvValorCampoClaveBean valorCampoClave = null;
	
					for (int j = 0; j < alExpedientes.size(); j++) {
						Map htClaves = (Hashtable) alExpedientes.get(j);
						Iterator itClave = htClaves.keySet().iterator();
						valorCampoClave = new EnvValorCampoClaveBean();
						valorCampoClave.setIdValor(valorCampoClaveAdm
								.getNewIdEnvio());
						while (itClave.hasNext()) {
							String clave = (String) itClave.next();
							String valorClave = (String) htClaves.get(clave);
	
							// String idFactura2 = (String)alExpedientes.get(j);
	
							valorCampoClave.setIdProgram(destProgramInformes
									.getIdProgram());
							valorCampoClave.setIdEnvio(destProgramInformes
									.getIdEnvio());
							valorCampoClave.setIdInstitucion(destProgramInformes
									.getIdInstitucion());
							valorCampoClave.setIdPersona(destProgramInformes
									.getIdPersona());
							// destProgramInformes.setClaves(claves.toString());
							valorCampoClave
									.setIdInstitucionPersona(destProgramInformes
											.getIdInstitucionPersona());
	
							valorCampoClave.setIdTipoInforme(idTipoInforme);
							valorCampoClave.setClave("idExpedientes");
							valorCampoClave.setCampo(clave);
							valorCampoClave.setValor(valorClave);
							valorCampoClaveAdm.insert(valorCampoClave);
	
						}
	
					}
	
				}
	
				
				informesBean = new EnvInformesGenericosBean();
				informesBean.setIdProgram(programInformes.getIdProgram());
				informesBean.setIdEnvio(programInformes.getIdEnvio());
	
				for (int j = 0; j < informesVector.size(); j++) {
					AdmInformeBean beanInforme = (AdmInformeBean) informesVector
							.get(j);
					informesBean.setIdPlantilla(beanInforme.getIdPlantilla());
					informesBean.setIdInstitucion(beanInforme.getIdInstitucion());
					informesGenericoAdm.insert(informesBean);
				}
			}
			setEnvioBatch(true);*/
			}
		}
		// return isEnvioBatch;

	}

	private String getExpedienteIJ(List<DefinirEJGForm> ejgList) {
		StringBuffer expedientes = new StringBuffer("");
		if (ejgList != null && ejgList.size() > 0) {
			for (DefinirEJGForm ejgForm : ejgList) {
				expedientes.append(ejgForm.getNombre());
				expedientes.append(", ");

			}
			// quitamos la ltima coma
			expedientes.delete(expedientes.lastIndexOf(","),
					expedientes.length());

		}

		return expedientes.toString();

	}

	public boolean isAlguienEjecutando() {
		synchronized (EnvioInformesGenericos.alguienEjecutando) {
			if (!EnvioInformesGenericos.alguienEjecutando) {
				EnvioInformesGenericos.alguienEjecutando = Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}

	private void setNadieEjecutando() {
		synchronized (EnvioInformesGenericos.alguienEjecutando) {
			EnvioInformesGenericos.alguienEjecutando = Boolean.FALSE;
		}
	}

	private boolean isAlgunaEjecucionDenegada() {
		synchronized (EnvioInformesGenericos.algunaEjecucionDenegada) {
			if (!EnvioInformesGenericos.algunaEjecucionDenegada) {
				EnvioInformesGenericos.algunaEjecucionDenegada = Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}

	private void setNingunaEjecucionDenegada() {
		synchronized (EnvioInformesGenericos.algunaEjecucionDenegada) {
			EnvioInformesGenericos.algunaEjecucionDenegada = Boolean.FALSE;
		}
	}

	private void setAlgunaEjecucionDenegada() {
		synchronized (EnvioInformesGenericos.algunaEjecucionDenegada) {
			EnvioInformesGenericos.algunaEjecucionDenegada = Boolean.TRUE;
		}
	}

	public void procesarAutomaticamenteGeneracionEnvios() throws Exception {
		if (isAlguienEjecutando()) {
			ClsLogging
					.writeFileLogWithoutSession(
							"YA SE ESTA EJECUTANDO LA GENERACION DE ENVIOS EN BACKGROUND. CUANDO TERMINE SE INICIARA OTRA VEZ EL PROCESO.",
							3);
			// ClsLogging.writeFileLogError("gratuita.eejg.message.isAlguienEjecutando",new
			// SchedulerException("gratuita.eejg.message.isAlguienEjecutando"),
			// 3);
			setAlgunaEjecucionDenegada();
			return;
		}

		try {
			procesoAutomaticoGeneracionEnvios();

		} catch (Exception e) {
			throw e;
		} finally {
			setNadieEjecutando();
			if (isAlgunaEjecucionDenegada()) {
				setNingunaEjecucionDenegada();
				procesarAutomaticamenteGeneracionEnvios();

			}
		}
	}

	private void procesoAutomaticoGeneracionEnvios() throws ClsExceptions {
		EnvProgramIRPFAdm admProgramiRPF = new EnvProgramIRPFAdm(new UsrBean()); 
		
		// Este
																					// usrbean
																					// esta
																					// controlado
																					// que
																					// no
																					// se
																					// necesita
																					// el
																					// valor
		// Sacamos los datos de los datos programados
		// pendientes(ClsConstants.DB_FALSE) de
		// todas las instituciones(null)
		Vector vCertificadosIRPFProgramados = admProgramiRPF
				.getCertificadosIRPFProgramados(ClsConstants.DB_FALSE, null);

		InformeCertificadoIRPF informeCertificadoIRPF = null;
		if (vCertificadosIRPFProgramados != null
				&& vCertificadosIRPFProgramados.size() > 0) {
			ClsLogging.writeFileLogWithoutSession(
					" ---------- ENVIOS PROGRAMADOS DE CERTIFICADOS IRPF PENDIENTES:"
							+ vCertificadosIRPFProgramados.size(), 3);
			EnvProgramIRPFBean programIRPFBean = null;
			informeCertificadoIRPF = new InformeCertificadoIRPF();
			for (int i = 0; i < vCertificadosIRPFProgramados.size(); i++) {

				try {
					programIRPFBean = (EnvProgramIRPFBean) vCertificadosIRPFProgramados
							.get(i);
					UsrBean usr = UsrBean.UsrBeanAutomatico(programIRPFBean
							.getIdInstitucion().toString());
					usr.setComision(programIRPFBean.getEnvioProgramado().getComisionAJG()!=null&&programIRPFBean.getEnvioProgramado().getComisionAJG().toString().equals(ClsConstants.DB_TRUE)?true:false);
					informeCertificadoIRPF.enviarCertificadoIRPFColegiado(usr,
							programIRPFBean,
							programIRPFBean.getEnvioProgramado());

					programIRPFBean.setOriginalHash(admProgramiRPF
							.beanToHashTable(programIRPFBean));
					programIRPFBean.setEstado(ClsConstants.DB_TRUE);

					admProgramiRPF.update(programIRPFBean);

					ClsLogging.writeFileLogWithoutSession(
							" ---------- OK ENVIO DE CERTIFICADOS IRPF PENDIENTES IDPERSONA: "
									+ programIRPFBean.getIdPersona(), 3);

				} catch (Exception e) {

					if (programIRPFBean != null
							&& programIRPFBean.getIdInstitucion() != null) {
						ClsLogging
								.writeFileLogWithoutSession(
										" ----------ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES IDINSTITUCION: "
												+ programIRPFBean
														.getIdInstitucion(), 3);
						if (programIRPFBean.getIdPersona() != null)
							ClsLogging
									.writeFileLogWithoutSession(
											" ----------ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES IDPERSONA: "
													+ programIRPFBean
															.getIdPersona(), 3);
					} else
						ClsLogging
								.writeFileLogWithoutSession(
										" ---------- ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES.",
										3);
				}
			}
		}

		ClsLogging
				.writeFileLogWithoutSession(
						" FIN ENVIOS PROGRAMADOS DE CERTIFICADOS IRPF A COLEGIADOS ",
						3);

		ClsLogging.writeFileLogWithoutSession(
				" ---------- INICIO ENVIOS PROGRAMADOS DE INFORMES GENERICOS ",
				3);
		EnvProgramInformesAdm admProgramInfGenericos = new EnvProgramInformesAdm(
				new UsrBean()); // Este usrbean esta controlado que no se
								// necesita el valor
		// Sacamos los datos de los datos programados
		// pendientes(ClsConstants.DB_FALSE) de
		// todas las instituciones(null)
		Vector vInfGenericosProgramados = admProgramInfGenericos
				.getInformesGenericosProgramados(ClsConstants.DB_FALSE, null);

		EnvioInformesGenericos envioInformeGenerico = new EnvioInformesGenericos();
		if (vInfGenericosProgramados != null
				&& vInfGenericosProgramados.size() > 0) {
			ClsLogging.writeFileLogWithoutSession(
					" ---------- ENVIOS PROGRAMADOS DE INFORMES GENERICOS PENDIENTES:"
							+ vInfGenericosProgramados.size(), 3);
			EnvProgramInformesBean programInfGenericoBean = null;
			EnvDestProgramInformesAdm admDestProgram = new EnvDestProgramInformesAdm(
					new UsrBean());
			EnvInformesGenericosAdm admInformesGenericos = new EnvInformesGenericosAdm(
					new UsrBean());
			for (int i = 0; i < vInfGenericosProgramados.size(); i++) {

				try {
					programInfGenericoBean = (EnvProgramInformesBean) vInfGenericosProgramados
							.get(i);

					Integer idTipoEnvio = programInfGenericoBean
							.getEnvioProgramado().getIdTipoEnvios();
					UsrBean usr = UsrBean.UsrBeanAutomatico(programInfGenericoBean.getIdInstitucion().toString());
					usr.setComision(programInfGenericoBean.getEnvioProgramado().getComisionAJG()!=null&&programInfGenericoBean.getEnvioProgramado().getComisionAJG().toString().equals(ClsConstants.DB_TRUE)?true:false);
					if (idTipoEnvio.toString().equals(
							EnvTipoEnviosAdm.K_CORREO_ORDINARIO)) {

						
						Vector vDestinatarios = admDestProgram
								.getDestinatariosInformesGenericosProgramados(programInfGenericoBean);
						Vector vPlantillas = admInformesGenericos
								.getPlantillasInformesGenericosProgramados(programInfGenericoBean);

						try {
							usr.setUserName(String.valueOf(programInfGenericoBean.getUsuMod()));
							envioInformeGenerico
									.enviarInformeGenericoOrdinario(usr,
											vDestinatarios,
											programInfGenericoBean,
											vPlantillas, programInfGenericoBean
													.getEnvioProgramado());
						} catch (Exception e) {
							// if(destProgramInfBean.getIdPersona()!=null)
							ClsLogging
									.writeFileLogWithoutSession(
											" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES CORREO ORDINARIO: ",
											3);
						}

					} else if (idTipoEnvio.toString().equals(
							EnvTipoEnviosAdm.K_CORREO_ELECTRONICO)
							|| idTipoEnvio.toString().equals(
									EnvTipoEnviosAdm.K_FAX)) {

						
						Vector vDestinatarios = admDestProgram
								.getDestinatariosInformesGenericosProgramados(programInfGenericoBean);
						Vector vPlantillas = admInformesGenericos
								.getPlantillasInformesGenericosProgramados(programInfGenericoBean);
						for (int j = 0; j < vDestinatarios.size(); j++) {
							EnvDestProgramInformesBean destProgramInfBean = (EnvDestProgramInformesBean) vDestinatarios
									.get(j);
//							UserTransaction transaction = usr.getTransactionPesada();
							try {
								usr.setUserName(String.valueOf(programInfGenericoBean.getUsuMod()));
//								transaction.begin();
								envioInformeGenerico.enviarInformeGenerico(usr,
										destProgramInfBean,
										programInfGenericoBean, vPlantillas,
										programInfGenericoBean
												.getEnvioProgramado());
//								transaction.commit();
							} catch (Exception e) {
//								transaction.rollback();
								
								if (destProgramInfBean.getIdPersona() != null)
									ClsLogging.writeFileLogError(" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES IDPERSONA: "
													+ destProgramInfBean
															.getIdPersona()
													+ " ", e, 3);
							}
						}

					} else if (idTipoEnvio.toString().equals(
							EnvTipoEnviosAdm.K_SMS)
							|| idTipoEnvio.toString().equals(
									EnvTipoEnviosAdm.K_BUROSMS)) {

						
						Vector vDestinatarios = admDestProgram
								.getDestinatariosInformesGenericosProgramados(programInfGenericoBean);
						Vector vPlantillas = admInformesGenericos
								.getPlantillasInformesGenericosProgramados(programInfGenericoBean);
						for (int j = 0; j < vDestinatarios.size(); j++) {
							EnvDestProgramInformesBean destProgramInfBean = (EnvDestProgramInformesBean) vDestinatarios
									.get(j);
							try {
								usr.setUserName(String.valueOf(programInfGenericoBean.getUsuMod()));
								envioInformeGenerico.enviarInformeGenericoSms(
										usr, destProgramInfBean,
										programInfGenericoBean, vPlantillas,
										programInfGenericoBean
												.getEnvioProgramado());
							} catch (Exception e) {
								if (destProgramInfBean.getIdPersona() != null)
									ClsLogging.writeFileLogWithoutSession(
											" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES IDPERSONA: "
													+ destProgramInfBean
															.getIdPersona()
													+ " " + e.toString(), 3);
							}
						}

					}else if (idTipoEnvio.toString().equals(
									EnvTipoEnviosAdm.K_ENVIOTELEMATICO)) {

						//juzga
						
						Vector vDestinatarios = admDestProgram
								.getDestinatariosInformesGenericosProgramados(programInfGenericoBean);
						Vector vPlantillas = admInformesGenericos
								.getPlantillasInformesGenericosProgramados(programInfGenericoBean);
						Vector<EnvDestProgramInformesBean> destProgramInformesBeanOrdinarios = new Vector<EnvDestProgramInformesBean>();
						for (int j = 0; j < vDestinatarios.size(); j++) {
							EnvDestProgramInformesBean destProgramInfBean = (EnvDestProgramInformesBean) vDestinatarios
									.get(j);
							try {
								
								if (destProgramInfBean.getTipoDestinatario().equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)) {
										ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm(usr);
										Vector juzgadoVector = juzgadoAdm.busquedaJuzgado(destProgramInfBean.getIdInstitucion().toString(),
																					destProgramInfBean.getIdPersona().toString());
										if(juzgadoVector != null){
											Hashtable procuradorHashtable = (Hashtable) juzgadoVector.get(0);
											String isCodigoEJIS = (String)procuradorHashtable.get("ISCODIGOEJIS"); 
											if(isCodigoEJIS.equals("1")){
 												//Tiene codigo ejis
												try {
                                                    envioInformeGenerico.enviarInformeGenericoTelematico(usr,destProgramInfBean,programInfGenericoBean,vPlantillas, programInfGenericoBean.getEnvioProgramado());   
                                                
												} catch (SIGAException e) {
                                                    ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES IDPERSONA: "	+ destProgramInfBean.getIdPersona() + " " + e.getLiteral(), 3);
                                                }
												
											}else if(isCodigoEJIS.equals("0")){
												destProgramInformesBeanOrdinarios.add(destProgramInfBean);
												
												
												
											}
										}
								}else{	
									//no esta 
									ClsLogging.writeFileLogWithoutSession(
											" ----------INFO ENVIOS TELEMATICOS SOLO PARA JUZGADOS ", 3);
									
								}
								
							} catch (Exception e) {
								if (destProgramInfBean.getIdPersona() != null)
									ClsLogging.writeFileLogWithoutSession(
											" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES IDPERSONA: "
													+ destProgramInfBean
															.getIdPersona()
													+ " " + e.toString(), 3);
							}
						}
						try {
							//No tiene codigo ejis
							if(destProgramInformesBeanOrdinarios.size()>0){
								programInfGenericoBean.getEnvioProgramado().setIdTipoEnvios(new Integer(EnvTipoEnviosAdm.K_CORREO_ORDINARIO));
								envioInformeGenerico.enviarInformeGenericoOrdinario(usr,destProgramInformesBeanOrdinarios,	programInfGenericoBean,vPlantillas, 
										programInfGenericoBean.getEnvioProgramado());
							}
						} catch (Exception e) {
							ClsLogging.writeFileLogWithoutSession(
									" ----------ERROR ENVIO DE INFORMES GENERICOS telematicos de manera ordinaria PENDIENTES " + e.toString(), 3);
						}
 
											

					}

					programInfGenericoBean
							.setOriginalHash(admProgramInfGenericos
									.beanToHashTable(programInfGenericoBean));
					programInfGenericoBean.setEstado(ClsConstants.DB_TRUE);

					admProgramInfGenericos.update(programInfGenericoBean);

					ClsLogging
							.writeFileLogWithoutSession(
									" ---------- OK ENVIO DE INFORMES GENERICOS PENDIENTES. TIPO DE INFORME : "
											+ programInfGenericoBean
													.getIdTipoInforme(), 3);

				} catch (Exception e) {
					// FIXME AAAAAAAAAAAAAAAAA CONTROLAR EXCEPCION ENVIO
					if (programInfGenericoBean != null
							&& programInfGenericoBean.getIdInstitucion() != null) {
						ClsLogging.writeFileLogWithoutSession(
								" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES. TIPO DE INFORME: "
										+ programInfGenericoBean
												.getIdTipoInforme() + " "
										+ e.toString(), 3);

					} else
						ClsLogging.writeFileLogWithoutSession(
								" ---------- ERROR ENVIO DE INFORMES GENERICOS PENDIENTESS."
										+ " " + e.toString(), 3);
				}
			}
		}

		ClsLogging.writeFileLogWithoutSession(
				" FIN ENVIOS PROGRAMADOS DE INFORMES GENERICOS ", 3);

	}

	public void enviarAvisoAlerta(ExpTipoExpedienteBean tipoExpedienteBean,
			ExpAlertaBean alertaBean, UsrBean userBean) throws Exception {

		ExpDestinatariosAvisosAdm destinatariosAvisosAdm = new ExpDestinatariosAvisosAdm(
				userBean);
		EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
				userBean);
		Hashtable tipoExpHashtable = new Hashtable();
		tipoExpHashtable.put(ExpTipoExpedienteBean.C_IDINSTITUCION,
				alertaBean.getIdInstitucionTipoExpediente());
		tipoExpHashtable.put(ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE,
				alertaBean.getIdTipoExpediente());
		Vector destinatariosAvisosVector = destinatariosAvisosAdm
				.select(tipoExpHashtable);
		String idioma = userBean.getLanguage();
		if (userBean.getLanguage() == null || userBean.getLanguage().equals("")) {
			idioma = UsrBean.UsrBeanAutomatico(
					alertaBean.getIdInstitucion().toString()).getLanguage();
		}

		EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
				userBean);
		EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
				userBean);
		EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
				userBean);
		EnvEnvioProgramadoBean envioProgramado = null;
		EnvProgramInformesBean programInformes = null;
		EnvDestProgramInformesBean destProgramInformes = null;
		envioProgramado = new EnvEnvioProgramadoBean();
		envioProgramado.setIdEnvio(envioProgramadoAdm.getNewIdEnvio(alertaBean
				.getIdInstitucion().toString()));
		envioProgramado.setIdInstitucion(alertaBean.getIdInstitucion());
		envioProgramado.setIdTipoEnvios(tipoExpedienteBean.getIdTipoEnvios());
		envioProgramado.setIdPlantillaEnvios(tipoExpedienteBean
				.getIdPlantillaEnvios());
		if (tipoExpedienteBean.getIdPlantilla() != null
				&& !tipoExpedienteBean.getIdPlantilla().toString().equals("")) {
			envioProgramado.setIdPlantilla(tipoExpedienteBean.getIdPlantilla());
		} else {
			envioProgramado.setIdPlantilla(null);
		}

		envioProgramado.setNombre(UtilidadesString.getMensajeIdioma(idioma,
				"expedientes.tiposexpedientes.alertas.envio.nombre")
				+ " "
				+ tipoExpedienteBean.getNombre());
		envioProgramado.setEstado(ClsConstants.DB_FALSE);
		envioProgramado.setFechaProgramada("sysdate");

		envioProgramadoAdm.insert(envioProgramado);

		programInformes = new EnvProgramInformesBean();
		// envioProgramado.setProgramInformes(programInformes);
		programInformes.setIdProgram(programInformesAdm
				.getNewIdProgramInformes(alertaBean.getIdInstitucion()
						.toString()));
		programInformes.setIdEnvio(envioProgramado.getIdEnvio());
		programInformes.setIdInstitucion(envioProgramado.getIdInstitucion());

		programInformes.setIdioma(new Integer(idioma));
		programInformes.setEstado(ClsConstants.DB_FALSE);
		programInformes.setClaves("");
		programInformes.setPlantillas(tipoExpedienteBean.getIdPlantillaEnvios()
				.toString());
		programInformes
				.setIdTipoInforme(EnvioInformesGenericos.comunicacionesAvisoExpedientes);
		programInformesAdm.insert(programInformes);

		for (int i = 0; i < destinatariosAvisosVector.size(); i++) {
			ExpDestinatariosAvisosBean destinatariosAvisosBean = (ExpDestinatariosAvisosBean) destinatariosAvisosVector
					.get(i);
			destProgramInformes = new EnvDestProgramInformesBean();
			destProgramInformes.setIdProgram(programInformes.getIdProgram());
			destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
			destProgramInformes.setIdInstitucion(programInformes
					.getIdInstitucion());
			destProgramInformes.setIdPersona(destinatariosAvisosBean
					.getIdPersona());
			destProgramInformes.setIdInstitucionPersona(destinatariosAvisosBean
					.getIdInstitucion());
			destProgramInformesAdm.insert(destProgramInformes);
			EnvValorCampoClaveBean valorCampoClave = null;
			valorCampoClave = new EnvValorCampoClaveBean();

			valorCampoClave.setIdProgram(destProgramInformes.getIdProgram());
			valorCampoClave.setIdEnvio(destProgramInformes.getIdEnvio());
			valorCampoClave.setIdInstitucion(destProgramInformes
					.getIdInstitucion());
			valorCampoClave.setIdPersona(destProgramInformes.getIdPersona());
			valorCampoClave.setIdInstitucionPersona(destProgramInformes
					.getIdInstitucionPersona());

			valorCampoClave
					.setIdTipoInforme(EnvioInformesGenericos.comunicacionesAvisoExpedientes);
			valorCampoClave.setClave("idAvisos");
			valorCampoClave.setCampo("idInstitucion");
			valorCampoClave.setValor("" + alertaBean.getIdInstitucion());
			valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
			valorCampoClaveAdm.insert(valorCampoClave);

			valorCampoClave.setCampo("idTipoExpediente");
			valorCampoClave.setValor("" + alertaBean.getIdTipoExpediente());
			valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
			valorCampoClaveAdm.insert(valorCampoClave);

			valorCampoClave.setCampo("idInstitucionTipoExpediente");
			valorCampoClave.setValor(""
					+ alertaBean.getIdInstitucionTipoExpediente());
			valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
			valorCampoClaveAdm.insert(valorCampoClave);

			valorCampoClave.setCampo("anioExpediente");
			valorCampoClave.setValor("" + alertaBean.getAnioExpediente());
			valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
			valorCampoClaveAdm.insert(valorCampoClave);

			valorCampoClave.setCampo("numeroExpediente");
			valorCampoClave.setValor("" + alertaBean.getNumeroExpediente());
			valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
			valorCampoClaveAdm.insert(valorCampoClave);

			valorCampoClave.setCampo("idAlerta");
			valorCampoClave.setValor("" + alertaBean.getIdAlerta());
			valorCampoClave.setIdValor(valorCampoClaveAdm.getNewIdEnvio());
			valorCampoClaveAdm.insert(valorCampoClave);

		}

		SIGASvlProcesoAutomaticoRapido
				.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoGeneracionEnvio);

	}
	public void gestionarComunicacionCAJG(DefinirEnviosForm form, Locale locale,
			UsrBean userBean) throws ClsExceptions, SIGAException {

		MasterReport masterReport = new MasterReport();
		// Vector vCampos = masterReport.obtenerDatosFormulario(form);
		Vector datosInformeVector = masterReport.getDatosInforme(form
				.getDatosInforme());

		// Tenemos que obtener el letrado de la designa ya que no se saca en la
		// query del
		// paginador(¡¡¡¡Esta persona se obtiene en la jsp!!!!)
		setPersonasEjg(datosInformeVector, userBean);
		if (datosInformeVector.size() == 0)
			throw new SIGAException("messages.envios.aviso.sinDestinatarios");

		Hashtable destinatariosHashtable = getDestinatariosEjg(datosInformeVector);

		// String idTipoEnvio = form.getIdTipoEnvio();
		// boolean isEnvioSMS =
		// Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_BUROSMS ||
		// Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_SMS;
//		boolean isEnvioSMS = false;

		String idInstitucion = userBean.getLocation();

		String datosEnvios = form.getDatosEnvios();
		// String datosInforme = form.getDatosInforme();
		String[] datosEnvio = datosEnvios.split("##");
		Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
		List<String> informesList = null;
		String keyEnvios = null;
		for (int i = 0; i < datosEnvio.length; i++) {
			String lineaEnvio = datosEnvio[i];
			String[] parametrosEnvios = lineaEnvio.split(",");

			String idTipoEnvio = parametrosEnvios[0];
			String idPlantillaEnvio = parametrosEnvios[1];
			String idInforme = parametrosEnvios[2];
			String idInstitucionInforme = parametrosEnvios[3];
			String acuseRecibo = parametrosEnvios[4];
			String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
			if (enviosHashtable.containsKey(key)) {
				informesList = enviosHashtable.get(key);
			} else {
				informesList = new ArrayList<String>();

			}
			informesList.add(idInforme + "," + idInstitucionInforme);
			enviosHashtable.put(key, informesList);

		}

		// si la persona es null es que hay mas de un colegiado de las distintas
		// designas
		// si solo hay uno comprobaremos que si hay mas de un
		// solicitante(siempre y cuando algun informe sea
		// de tipo solicitante)
		boolean isDestinatarioUnico = destinatariosHashtable != null
				&& destinatariosHashtable.size() == 1;
		Vector<AdmInformeBean> informesVector = null;
		boolean isASolicitantes = false;
		boolean isAJuzgado = false;
		boolean isAContrario = false;
		if (isDestinatarioUnico && enviosHashtable.size() == 1) {

			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			keyEnvios = (String) iteEnvios.next();
			informesList = enviosHashtable.get(keyEnvios);

			informesVector = this.getInformes(informesList, userBean);

			for (int j = 0; j < informesVector.size(); j++) {
				AdmInformeBean informeBean = (AdmInformeBean) informesVector
						.get(j);
				String tiposDestinatario = informeBean.getDestinatarios();
				if (tiposDestinatario != null) {
					char[] tipoDestinatario = tiposDestinatario.toCharArray();
					for (int k = 0; k < tipoDestinatario.length; k++) {

						if (String
								.valueOf(tipoDestinatario[k])
								.equalsIgnoreCase(
										AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
							isASolicitantes = true;
							break;
						}
						
						if (String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)) {
							isAJuzgado = true;
							break;
						}
						
						if (String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSCONTRARIOSJG)) {
							isAContrario = true;
							break;
						}	

					}

				}
				// Comprbamos que al ser a solicitantes haya una persona unica

			}

		}

		if (isDestinatarioUnico && enviosHashtable.size() == 1
				&& !isASolicitantes &&!isAJuzgado && !isAContrario) {

			Vector vDocumentos = new Vector();
			for (int i = 0; i < datosInformeVector.size(); i++) {
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(i);
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,
						EnvioInformesGenericos.docDocument,
						EnvioInformesGenericos.comunicacionesEjg));

			}

			form.setIdTipoEnvio(keyEnvios.split("_")[0]);
			form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
			form.setAcuseRecibo(keyEnvios.split("_")[2]);

			// Tenemos que mirar si es persona JG o Persona
			String idPersonaUnica = (String) destinatariosHashtable.keySet()
					.iterator().next();
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
			String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersonaUnica);
			form.setNombre(form.getNombre()+" "+nombreyapellidos);
			
			Envio envio = getEnvio(form, true, locale, userBean);

			// Genera el envio:
			
			envio.generarEnvio(idPersonaUnica,
					(String) destinatariosHashtable.get(idPersonaUnica),
					vDocumentos);

		} else {

			Iterator iteEnvios = enviosHashtable.keySet().iterator();
			int countEnvios = 0;
			EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
					userBean);
			EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
					userBean);
			EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
					userBean);
			EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
					userBean);
			EnvEnvioProgramadoBean envioProgramado = null;
			EnvProgramInformesBean programInformes = null;
			EnvDestProgramInformesBean destProgramInformes = null;
			EnvInformesGenericosBean informesBean = null;

			EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(
					userBean);
			while (iteEnvios.hasNext()) {
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList,
						userBean);
				String[] keysEnvio = keyEnvios.split("_");
				String idTipoEnvio = keysEnvio[0];
				String idPlantillaEnvio = keysEnvio[1];
				String acuseRecibo = keysEnvio[2];

				String idioma = null;
				String idTipoInforme = null;
				String plantillas = null;

				envioProgramado = new EnvEnvioProgramadoBean();
				envioProgramado.setIdEnvio(envioProgramadoAdm
						.getNewIdEnvio(idInstitucion));
				envioProgramado.setIdInstitucion(new Integer(idInstitucion));
				envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
				envioProgramado.setIdPlantillaEnvios(Integer
						.valueOf(idPlantillaEnvio));
				envioProgramado.setAcuseRecibo(acuseRecibo);
				envioProgramado.setNombre(form.getNombre());
				envioProgramado.setEstado(ClsConstants.DB_FALSE);
				envioProgramado.setFechaProgramada(getFechaProgramada(
						form.getFechaProgramada(), locale, userBean));

				envioProgramadoAdm.insert(envioProgramado);

				boolean isInformeProgramado = false;
				boolean isInformesInsertados = false;
				ArrayList alClavesEjg = new ArrayList();
				alClavesEjg.add("idInstitucion");
				alClavesEjg.add("anio");
				alClavesEjg.add("idTipoEJG");
				alClavesEjg.add("numero");

				// Ponemos esta lista para comprobar que no se ha insertado el
				// destinatario

				List<String> lPersonas = new ArrayList<String>();
				for (int i = 0; i < datosInformeVector.size(); i++) {
					Hashtable ht = (Hashtable) datosInformeVector.get(i);
					Vector personasDesignasEjg = (Vector) ht
							.get("personasDesignasEjg");

					Vector vSolicitantesEjg = (Vector) ht
							.get("solicitantesEjg");
					Vector vContrariosEjg = (Vector) ht
							.get("contrariosEjg");

					String idJuzgado = (String) ht.get("idJuzgado");
					idInstitucion = (String) ht.get("idinstitucion");
					idTipoInforme = (String) ht.get("idTipoInforme");
					String idTipoEjg = "";
					if ((String) ht.get("idtipo") != null) {
						idTipoEjg = (String) ht.get("idtipo");
					} else {
						idTipoEjg = (String) ht.get("idTipoEJG");
					}
					ht.put("idTipoEJG", idTipoEjg);
					ht.put("idInstitucion", idInstitucion);
					// ht.put("anio", (String) ht.get("anio"));
					// ht.put("numero", (String) ht.get("numero"));

					// ArrayList alDesignas = (ArrayList) ht.get("idDesignas");
					plantillas = (String) ht.get("plantillas");
					String idSolicitanteJG = (String) ht.get("idPersonaJG");

					List<EnvDestProgramInformesBean> lDestinatarios = new ArrayList<EnvDestProgramInformesBean>();
					List<String> lDestPersonas = new ArrayList<String>();
					if (!isInformeProgramado) {
						programInformes = new EnvProgramInformesBean();
						programInformes.setIdProgram(programInformesAdm
								.getNewIdProgramInformes(idInstitucion));
						programInformes
								.setIdEnvio(envioProgramado.getIdEnvio());
						programInformes.setIdInstitucion(envioProgramado
								.getIdInstitucion());
						idioma = userBean.getLanguage();
						programInformes.setIdioma(new Integer(idioma));
						programInformes.setEstado(ClsConstants.DB_FALSE);
						programInformes.setPlantillas("MULTIPLES");
						programInformes.setIdTipoInforme(idTipoInforme);

						programInformesAdm.insert(programInformes);

						informesBean = new EnvInformesGenericosBean();
						informesBean.setIdProgram(programInformes
								.getIdProgram());
						informesBean.setIdEnvio(programInformes.getIdEnvio());
						informesBean.setIdInstitucion(programInformes
								.getIdInstitucion());
						

						for (int j = 0; j < informesVector.size(); j++) {
							AdmInformeBean informeBean = (AdmInformeBean) informesVector
									.get(j);
							informesBean.setIdPlantilla(informeBean
									.getIdPlantilla());
							informesBean.setIdInstitucion(informeBean
									.getIdInstitucion());
							informesGenericoAdm.insert(informesBean);

						}

						isInformeProgramado = true;

					}

					for (int j = 0; j < informesVector.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean) informesVector
								.get(j);
						String tiposDestinatario = informeBean
								.getDestinatarios();
						informesBean.setIdPlantilla(informeBean
								.getIdPlantilla());
						if (tiposDestinatario != null) {
							
							
								if (idSolicitanteJG != null
										&& !idSolicitanteJG.equals("")) {

									if (String
											.valueOf(tiposDestinatario)
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)) {
										for (int jta = 0; jta < vSolicitantesEjg
												.size(); jta++) {
											Hashtable htSolicitante = (Hashtable) vSolicitantesEjg
													.get(jta);
											String idPersonaJG = (String) htSolicitante
													.get(ScsDefendidosDesignaBean.C_IDPERSONA);
											if (idPersonaJG
													.equals(idSolicitanteJG)) {
												destProgramInformes = new EnvDestProgramInformesBean();
												destProgramInformes
														.setIdProgram(programInformes
																.getIdProgram());
												destProgramInformes
														.setIdEnvio(programInformes
																.getIdEnvio());
												destProgramInformes
														.setIdInstitucion(programInformes
																.getIdInstitucion());
												destProgramInformes
														.setIdPersona(new Long(
																idPersonaJG));
												destProgramInformes
														.setIdInstitucionPersona(new Integer(
																idInstitucion));
												destProgramInformes
														.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

												if (!lPersonas
														.contains(idPersonaJG)) {
													destProgramInformesAdm
															.insert(destProgramInformes);
													countEnvios++;
													lPersonas.add(idPersonaJG);
												}

												if (!lDestPersonas
														.contains(idPersonaJG)) {
													lDestinatarios
															.add(destProgramInformes);
													lDestPersonas
															.add(idPersonaJG);

												}
											}

										}
									}

								} else {
									if (String
											.valueOf(tiposDestinatario)
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_CENPERSONA)
											&& personasDesignasEjg != null) {
										for (int jta = 0; jta < personasDesignasEjg
												.size(); jta++) {
											Hashtable htPersonaDesigna = (Hashtable) personasDesignasEjg
													.get(jta);
											String idPersona = (String) htPersonaDesigna
													.get("IDPERSONA");
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(new Long(
															idPersona));
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes
													.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA);

											if (!lPersonas.contains(idPersona)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersona);
											}

											if (!lDestPersonas
													.contains(idPersona)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersona);

											}

										}

									} else if (String
											.valueOf(tiposDestinatario)
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)
											&& vSolicitantesEjg != null) {
										for (int jta = 0; jta < vSolicitantesEjg
												.size(); jta++) {
											Hashtable htSolicitante = (Hashtable) vSolicitantesEjg
													.get(jta);
											String idPersonaJG = (String) htSolicitante
													.get(ScsDefendidosDesignaBean.C_IDPERSONA);
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(new Long(
															idPersonaJG));
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes
													.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG);

											if (!lPersonas
													.contains(idPersonaJG)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersonaJG);
											}

											if (!lDestPersonas
													.contains(idPersonaJG)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersonaJG);

											}

										}
									} else if (String.valueOf(tiposDestinatario).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)) {
	
										if (idJuzgado == null)
											continue;

										destProgramInformes = new EnvDestProgramInformesBean();
										destProgramInformes.setIdProgram(programInformes.getIdProgram());
										destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
										destProgramInformes.setIdInstitucion(programInformes.getIdInstitucion());
										destProgramInformes.setIdPersona(new Long(idJuzgado));
										destProgramInformes.setIdInstitucionPersona(new Integer(idInstitucion));
										destProgramInformes.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO);
										
										if (!lPersonas.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado)) {
											destProgramInformesAdm.insert(destProgramInformes);
											countEnvios++;
											lPersonas.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado);
										}
										
										if (!lDestPersonas.contains(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado)) {
											lDestinatarios.add(destProgramInformes);
											lDestPersonas.add(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO + idJuzgado);
										}										
									}else if (String
											.valueOf(tiposDestinatario)
											.equalsIgnoreCase(
													AdmInformeBean.TIPODESTINATARIO_SCSCONTRARIOSJG)
											&& vContrariosEjg != null) {
										for (int jta = 0; jta < vContrariosEjg
												.size(); jta++) {
											Hashtable htContrario = (Hashtable) vContrariosEjg
													.get(jta);
											String idPersonaJG = (String) htContrario
													.get(ScsContrariosEJGBean.C_IDPERSONA);
											destProgramInformes = new EnvDestProgramInformesBean();
											destProgramInformes
													.setIdProgram(programInformes
															.getIdProgram());
											destProgramInformes
													.setIdEnvio(programInformes
															.getIdEnvio());
											destProgramInformes
													.setIdInstitucion(programInformes
															.getIdInstitucion());
											destProgramInformes
													.setIdPersona(new Long(
															idPersonaJG));
											destProgramInformes
													.setIdInstitucionPersona(new Integer(
															idInstitucion));
											destProgramInformes
													.setTipoDestinatario(EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG);

											if (!lPersonas
													.contains(idPersonaJG)) {
												destProgramInformesAdm
														.insert(destProgramInformes);
												countEnvios++;
												lPersonas.add(idPersonaJG);
											}

											if (!lDestPersonas
													.contains(idPersonaJG)) {
												lDestinatarios
														.add(destProgramInformes);
												lDestPersonas.add(idPersonaJG);

											}

										}
									}
								}
							
						}
					}

					// if(lDestinatarios==null || lDestinatarios.size()==0)
					// throw new
					// SIGAException("messages.envios.aviso.sinDestinatarios");

					EnvValorCampoClaveBean valorCampoClave = null;

					// Iterator itClave = htClaves.keySet().iterator();
					valorCampoClave = new EnvValorCampoClaveBean();
					for (int k = 0; k < lDestinatarios.size(); k++) {
						destProgramInformes = (EnvDestProgramInformesBean) lDestinatarios
								.get(k);

						valorCampoClave.setIdValor(valorCampoClaveAdm
								.getNewIdEnvio());
						for (int pp = 0; pp < alClavesEjg.size(); pp++) {
							String clave = (String) alClavesEjg.get(pp);
							String valorClave = (String) ht.get(clave);
							valorCampoClave.setIdProgram(destProgramInformes
									.getIdProgram());
							valorCampoClave.setIdEnvio(destProgramInformes
									.getIdEnvio());
							valorCampoClave
									.setIdInstitucion(destProgramInformes
											.getIdInstitucion());
							valorCampoClave.setIdPersona(destProgramInformes
									.getIdPersona());
							valorCampoClave
									.setIdInstitucionPersona(destProgramInformes
											.getIdInstitucionPersona());

							valorCampoClave.setIdTipoInforme(idTipoInforme);
							valorCampoClave.setClave("idEjgs");
							valorCampoClave.setCampo(clave);
							valorCampoClave.setValor(valorClave);
							valorCampoClaveAdm.insert(valorCampoClave);

						}
					}

				}
				if (countEnvios == 0)
					throw new SIGAException(
							"messages.envios.aviso.sinDestinatarios");

				setEnvioBatch(true);

			}

		}

	}
	
	public File generarListadoFacturasEmitidasPdfFromXmlToFoXsl (HttpServletRequest request, Hashtable datosFormulario) throws ClsExceptions,SIGAException 
	{
	
		File fPdf = null;
		
	
		File rutaTmp=null;
	
			
		try {
			UsrBean usr = this.getUsuario();
			String idioma = usr.getLanguage();
			String idInstitucion = usr.getLocation();
			
	
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			
			// RGG 26/02/2007 cambio en los codigos de lenguajes
			AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
			String idiomaExt = a.getLenguajeExt(idioma);
			
			// YYYY/MM/DD HH24:MI:SS
			String fecha = UtilidadesBDAdm.getFechaCompletaBD("formato_ingles");
			fecha = fecha.replaceAll("/","");
			fecha = fecha.replaceAll(":","");
			fecha = fecha.replaceAll(" ","_");
			
			String directorioPlatillas         = rp.returnProperty("facturacion.directorioFisicoPlantillaListadoFacturaEmitidasJava"),
				   directorioEspecificoInforme = rp.returnProperty("facturacion.directorioPlantillaListadoFacturaEmitidaJava"),
				   directorioSalida            = rp.returnProperty("facturacion.directorioFisicoSalidaListadoFacturaEmitidasJava");

			// Directorios y nombres de trabajo
			String plantillaNombre = "facturaEmitida_"   + idiomaExt + ".xsl";
			String plantillaRuta   = directorioPlatillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			String pdfNombre       = "listFactEmitidas"  + "_" + idInstitucion + "_" + usr.getUserName() + "_" + fecha+".pdf";
			String pdfRuta         = directorioSalida    + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			
			String pathXml = plantillaRuta+ClsConstants.FILE_SEP+"facturaEmitida_"   + idiomaExt + ".xml";
			
			
			
			// obtener ruta almacen
			File rutaPDF = new File(pdfRuta);
			rutaPDF.mkdirs();
			if(!rutaPDF.exists()){
				throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
			} 
			else {
				if(!rutaPDF.canWrite()){
					throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
				}
			}
			pdfRuta += ClsConstants.FILE_SEP;
			
			//File fileXml = getXmlFacturasEmitidas(idInstitucion, fechaDesde, fechaHasta, pathXml)
			
			
			InformeFacturasEmitidas facturasEmitidas = new InformeFacturasEmitidas(usr);
			fPdf = facturasEmitidas.generarInformePdfFromXmlToFoXsl(pathXml,plantillaRuta,plantillaNombre,pdfRuta,pdfNombre, datosFormulario);
		}
		catch (SIGAException se) {
			throw se;
		}
		catch (ClsExceptions ex) {
			throw ex;
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe: "+e.getLocalizedMessage());
		} 
		finally {
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
        return fPdf;
	}
	private Hashtable getDatosInformeFinalOficio(Hashtable datosDesignacion,Hashtable datosInformeSeleccionado,UsrBean usrBean) 
			throws ClsExceptions, SIGAException {

		Hashtable htDatosInforme = new Hashtable();

		// Velores comunes para la obttencion de los datos del informe
		String idTipoInforme = (String) datosInformeSeleccionado.get("idTipoInforme");
		String idioma = (String) datosInformeSeleccionado.get("idioma");
		String idiomaExt =(String) datosInformeSeleccionado.get("idiomaExt");
		String aSolicitantes = (String) datosInformeSeleccionado.get("aSolicitantes");
		String aContrarios = (String) datosInformeSeleccionado.get("aContrarios"); //Add MJM 30/04/14
		String idPersonaJG = (String) datosInformeSeleccionado.get("idPersonaJG");
		String tipoDestinatarioInforme = (String) datosInformeSeleccionado.get("tipoDestinatarioInforme");

		
		ScsDesignaAdm scsDesignaAdm = new ScsDesignaAdm(usrBean);
		String idinstitucion = (String) datosDesignacion.get("idInstitucion");
		String anio = (String) datosDesignacion.get("anio");
		String idTurno = (String) datosDesignacion.get("idTurno");
		String numero = (String) datosDesignacion.get("numero");
		

		boolean isSolicitantes = aSolicitantes.equalsIgnoreCase("S");
		
		//Add MJM 30/04/14 para las etiquetas de EJG se añade el parámetro isContrarios 
		//solo está informado para informe designas
		boolean isContrarios=false;
		
		if(aContrarios!=null)
			 isContrarios = aContrarios.equalsIgnoreCase("S");		
		
		boolean agregarEtiqEJG=true;
		
		Vector datosconsulta = scsDesignaAdm.getDatosSalidaOficio(
				idinstitucion, idTurno, anio, numero, null, isSolicitantes, isContrarios,
				idPersonaJG, idioma,idiomaExt,tipoDestinatarioInforme,agregarEtiqEJG);
		if(!isSolicitantes && datosconsulta.size()>0){
			Hashtable registro = (Hashtable) datosconsulta.get(0);
			if(registro.get("defendido")!=null){
				htDatosInforme.put("defendido", registro.get("defendido"));
				registro.remove("defendido");
			}
		}
		htDatosInforme.put("row", datosconsulta);
		return htDatosInforme;

	}
	
	private Hashtable getDatosInformeEjgFinal(Hashtable datosInforme,Hashtable datosInformeSeleccionado,
			UsrBean usrBean) throws ClsExceptions, SIGAException {

		Hashtable htDatosInforme = new Hashtable();

		// Velores comunes para la obttencion de los datos del informe
		String idTipoInforme = (String) datosInformeSeleccionado.get("idTipoInforme");
		String tipoDestinatarioInforme = (String) datosInformeSeleccionado.get("tipoDestinatarioInforme");
		String aSolicitantes = (String) datosInformeSeleccionado.get("aSolicitantes");
		String aContrarios = (String) datosInformeSeleccionado.get("aContrarios");
		String generarInformeSinDireccion = (String) datosInformeSeleccionado.get("generarInformeSinDireccion");


		ScsEJGAdm ejgAdm = new ScsEJGAdm(usrBean);
		String idTipoEJG = "s";
		if (datosInforme.get("idtipo") != null) {
			idTipoEJG = (String) datosInforme.get("idtipo");
		} else {
			idTipoEJG = (String) datosInforme.get("idTipoEJG");
		}
		String anio = (String) datosInforme.get("anio");
		String numero = (String) datosInforme.get("numero");

		String idPersona = null;
		String tipoDestinatario = null;
		if (datosInforme.get("idPersonaJG") != null) {
			idPersona = (String) datosInforme.get("idPersonaJG");
			tipoDestinatario = EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG;
		}
		String idPersonaContrario = null;
		if (datosInforme.get("idContrario") != null) {
			idPersona = (String) datosInforme.get("idContrario");
			tipoDestinatario = EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG;
		}


		boolean isSolicitantes = aSolicitantes != null	&& aSolicitantes.equalsIgnoreCase("S")||(String) datosInforme.get("sms")!=null;
		boolean isAcontrarios = aContrarios != null	&& aContrarios.equalsIgnoreCase("S");
		boolean isGenerarInformeSinDireccion = generarInformeSinDireccion != null && generarInformeSinDireccion.equalsIgnoreCase("S");


		//			String languageInstitucion = (String) usrBean
		//					.getLanguageInstitucion();
		//quitar el idioma de la siguiente consulta, sacarlo abajo
		boolean agregarEtiqDesigna=true;
		Vector datosconsulta = ejgAdm.getDatosInformeEjg(
				usrBean.getLocation(), idTipoEJG, anio, numero, isSolicitantes, isAcontrarios, 
				idPersona,tipoDestinatario,isGenerarInformeSinDireccion,tipoDestinatarioInforme,agregarEtiqDesigna);
		Hashtable<String,Vector> htIdiomasUF = new Hashtable<String, Vector>();
		Hashtable<String,Vector> htIdiomasConyuge = new Hashtable<String,Vector>();
		if(datosconsulta.size()>0){
			Hashtable registro = (Hashtable) datosconsulta.get(0);

			for (int i = 0; i < datosconsulta.size(); i++) {
				registro = (Hashtable) datosconsulta.get(i);
				String idioma = (String)registro.get("idioma");
				if(i==0){
					setAlgunRepresentanteLegal(registro.get("isAlgunRepresentanteLegal")!=null && (Boolean)registro.get("isAlgunRepresentanteLegal"));
					setAlgunInformeNoGenerado(registro.get("isAlgunInformeNoGenerado")!=null && (Boolean)registro.get("isAlgunInformeNoGenerado"));
				}
				Vector regionUF = null;
				Vector regionConyuge = null;
				if(htIdiomasUF.containsKey(idioma)){
					regionUF =   htIdiomasUF.get(idioma);
					regionConyuge =   htIdiomasConyuge.get(idioma);
				}else{
					regionUF = ejgAdm.getDatosRegionUF(usrBean.getLocation(),
							idTipoEJG, anio, numero,idioma);
					regionConyuge = ejgAdm.getDatosRegionConyuge(
							usrBean.getLocation(), idTipoEJG, anio, numero,idioma);
					htIdiomasUF.put(idioma,regionUF);
					htIdiomasConyuge.put(idioma,regionConyuge);
				}

				registro.put("unidadfamiliar", regionUF);
				registro.put("conyuge", regionConyuge);



				if(registro.get("defendido")!=null){
					htDatosInforme.put("defendido", registro.get("defendido"));
					registro.remove("defendido");
				}

				if(registro.get("contrarios")!=null){
					htDatosInforme.put("contrarios", registro.get("contrarios"));
					registro.remove("contrarios");
				}
			}

		}
		htDatosInforme.put("row", datosconsulta);
		return htDatosInforme;

	}
	

	public boolean isAlgunRepresentanteLegal() {
		return algunRepresentanteLegal;
	}

	public void setAlgunRepresentanteLegal(boolean algunRepresentanteLegal) {
		this.algunRepresentanteLegal = algunRepresentanteLegal;
	}

	public boolean isAlgunInformeNoGenerado() {
		return algunInformeNoGenerado;
	}

	public void setAlgunInformeNoGenerado(boolean algunInformeNoGenerado) {
		this.algunInformeNoGenerado = algunInformeNoGenerado;
	}

	public void gestionarComunicacionOrdenDomiciliacion(DefinirEnviosForm form,String idTipoComunicacion,
			Locale locale, UsrBean userBean)throws ClsExceptions,
			SIGAException  {
		// Obtenemos la información pertinente relacionada
		MasterReport masterReport = new MasterReport();
			Vector datosInformeVector = masterReport.getDatosInforme(form
					.getDatosInforme());
			
			String idPersona = getIdColegiadoUnico(datosInformeVector);
			String idInstitucion = userBean.getLocation();

			String datosEnvios = form.getDatosEnvios();
			String[] datosEnvio = datosEnvios.split("##");
			Hashtable<String, List> enviosHashtable = new Hashtable<String, List>();
			List<String> informesList = null;
			for (int i = 0; i < datosEnvio.length; i++) {
				String lineaEnvio = datosEnvio[i];
				String[] parametrosEnvios = lineaEnvio.split(",");

				String idTipoEnvio = parametrosEnvios[0];
				String idPlantillaEnvio = parametrosEnvios[1];
				String idInforme = parametrosEnvios[2];
				String idInstitucionInforme = parametrosEnvios[3];
				String acuseRecibo = parametrosEnvios[4];
				String key = idTipoEnvio + "_" + idPlantillaEnvio+"_"+acuseRecibo;
				if (enviosHashtable.containsKey(key)) {
					informesList = enviosHashtable.get(key);
				} else {
					informesList = new ArrayList<String>();

				}
				informesList.add(idInforme + "," + idInstitucionInforme);
				enviosHashtable.put(key, informesList);

			}
			
			Vector<AdmInformeBean> informesVector = null;
			String keyEnvios = null;
			if (idPersona != null && enviosHashtable.size() == 1) {

				Vector vDocumentos = new Vector();
				Iterator iteEnvios = enviosHashtable.keySet().iterator();
				keyEnvios = (String) iteEnvios.next();
				informesList = enviosHashtable.get(keyEnvios);
				informesVector = this.getInformes(informesList, userBean);			
				Hashtable datosInforme = (Hashtable) datosInformeVector.get(0);
				vDocumentos.addAll(this.getDocumentosAEnviar(datosInforme,
						informesVector, userBean,
							EnvioInformesGenericos.docDocument,
							idTipoComunicacion));

				
				form.setIdTipoEnvio(keyEnvios.split("_")[0]);
				form.setIdPlantillaEnvios(keyEnvios.split("_")[1]);
				form.setAcuseRecibo(keyEnvios.split("_")[2]);
				
				CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);	
				String nombreyapellidos = personaAdm.obtenerNombreApellidos(idPersona);
				form.setNombre(form.getNombre()+" "+nombreyapellidos);

				Envio envio = getEnvio(form, true, locale, userBean);
				// Genera el envio:
				envio.generarEnvio(idPersona,
						EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,
						vDocumentos);
				

			} else {
				Iterator iteEnvios = enviosHashtable.keySet().iterator();
				int countEnvios = 0;
				String idioma = null;
				String idTipoInforme = null;
				EnvEnvioProgramadoAdm envioProgramadoAdm = new EnvEnvioProgramadoAdm(
						userBean);
				EnvProgramInformesAdm programInformesAdm = new EnvProgramInformesAdm(
						userBean);
				EnvDestProgramInformesAdm destProgramInformesAdm = new EnvDestProgramInformesAdm(
						userBean);
				EnvInformesGenericosAdm informesGenericoAdm = new EnvInformesGenericosAdm(
						userBean);
				EnvEnvioProgramadoBean envioProgramado = null;
				EnvProgramInformesBean programInformes = null;
				EnvDestProgramInformesBean destProgramInformes = null;
				EnvInformesGenericosBean informesBean = null;

				while (iteEnvios.hasNext()) {
					keyEnvios = (String) iteEnvios.next();
					informesList = enviosHashtable.get(keyEnvios);
					informesVector = this.getInformes(informesList, userBean);	
					String[] keysEnvio = keyEnvios.split("_");
					String idTipoEnvio = keysEnvio[0];
					String idPlantillaEnvio = keysEnvio[1];
					String acuseRecibo = keysEnvio[2];
				
					envioProgramado = new EnvEnvioProgramadoBean();
					envioProgramado.setIdEnvio(envioProgramadoAdm
							.getNewIdEnvio(idInstitucion));
					envioProgramado.setIdInstitucion(new Integer(idInstitucion));
					envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
					envioProgramado.setIdPlantillaEnvios(Integer
							.valueOf(idPlantillaEnvio));
					
					envioProgramado.setAcuseRecibo(acuseRecibo);
					envioProgramado.setNombre(form.getNombre());
					envioProgramado.setEstado(ClsConstants.DB_FALSE);
					envioProgramado.setFechaProgramada(getFechaProgramada(
							form.getFechaProgramada(), locale, userBean));
		
					envioProgramadoAdm.insert(envioProgramado);
		
					boolean isInformeProgramado = false;
		
					for (int i = 0; i < datosInformeVector.size(); i++) {
						Hashtable ht = (Hashtable) datosInformeVector.get(i);
						idPersona = (String) ht.get("idPersona");
						idInstitucion = (String) ht.get("idInstitucion");
						idTipoInforme = (String) ht.get("idTipoInforme");
						String idTipoPersonas = (String) ht.get("idTipoPersonas");
						StringBuffer claves = new StringBuffer();
						claves.append("idTipoPersonas==");
						claves.append(idTipoPersonas);
						claves.append("##");
		
						if (!isInformeProgramado) {
							programInformes = new EnvProgramInformesBean();
							// envioProgramado.setProgramInformes(programInformes);
							programInformes.setIdProgram(programInformesAdm
									.getNewIdProgramInformes(idInstitucion));
							programInformes.setIdEnvio(envioProgramado.getIdEnvio());
							programInformes.setIdInstitucion(envioProgramado
									.getIdInstitucion());
							// idioma =
							// ((AdmLenguajesBean)admIdioma.getLenguajeInstitucion(idInstitucion)).getIdLenguaje();
							idioma = userBean.getLanguage();
							programInformes.setIdioma(new Integer(idioma));
							programInformes.setEstado(ClsConstants.DB_FALSE);
							programInformes.setClaves(claves.toString());
							programInformes.setPlantillas("MULTIPLES");
							programInformes.setIdTipoInforme(idTipoInforme);
		
							programInformesAdm.insert(programInformes);
							isInformeProgramado = true;
		
						}
		
						destProgramInformes = new EnvDestProgramInformesBean();
						destProgramInformes
								.setIdProgram(programInformes.getIdProgram());
						destProgramInformes.setIdEnvio(programInformes.getIdEnvio());
						destProgramInformes.setIdInstitucion(programInformes
								.getIdInstitucion());
						destProgramInformes.setIdPersona(new Long(idPersona));
						destProgramInformes.setIdInstitucionPersona(new Integer(
								idInstitucion));
		
						destProgramInformesAdm.insert(destProgramInformes);
						countEnvios++;
		
					}
		
					
					informesBean = new EnvInformesGenericosBean();
					informesBean.setIdProgram(programInformes.getIdProgram());
					informesBean.setIdEnvio(programInformes.getIdEnvio());
		
					for (int j = 0; j < informesVector.size(); j++) {
						AdmInformeBean beanInforme = (AdmInformeBean) informesVector
								.get(j);
						informesBean.setIdPlantilla(beanInforme.getIdPlantilla());
						informesBean.setIdInstitucion(beanInforme.getIdInstitucion());
						informesGenericoAdm.insert(informesBean);
					}
				}
				setEnvioBatch(true);
			}

		}
		
	

	
	

}

class ComunicacionMoroso {
	public ComunicacionMoroso() {

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