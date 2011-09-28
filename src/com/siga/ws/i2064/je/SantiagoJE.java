package com.siga.ws.i2064.je;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.xmlbeans.XmlOptions;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.Utilidades.LogBDDHandler;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.ws.InformeXML;
import com.siga.ws.SigaWSHelper;
import com.siga.ws.i2064.WSSantiagoAdm;
import com.siga.ws.i2064.je.axis.EnvioJustificacionesServicePortBindingStub;
import com.siga.ws.i2064.je.axis.EnvioJustificacionesService_ServiceLocator;
import com.siga.ws.i2064.je.error.ErrorEnvioWS;
import com.siga.ws.i2064.je.error.ErrorNegocioWS;
import com.siga.ws.i2064.je.error.ErrorValidacionXML;
import com.siga.ws.i2064.je.xsd.ATESTADOTYPE;
import com.siga.ws.i2064.je.xsd.COMISARIATYPE;
import com.siga.ws.i2064.je.xsd.DATOSXUDICIAISTYPE;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument;
import com.siga.ws.i2064.je.xsd.EnvioJustificacion;
import com.siga.ws.i2064.je.xsd.IMPORTETYPE;
import com.siga.ws.i2064.je.xsd.NOMEAPELIDOSTYPE;
import com.siga.ws.i2064.je.xsd.ORGANOXUDICIALTYPE;
import com.siga.ws.i2064.je.xsd.PERSOATYPE;
import com.siga.ws.i2064.je.xsd.PROCBAREMOTYPE;
import com.siga.ws.i2064.je.xsd.SOXCLAVETYPE;
import com.siga.ws.i2064.je.xsd.TRIMESTRETYPE;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Periodo;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.IDExpAXG;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.OrganoJudicial;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.UnidadYJuzgado;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.UnidadYJuzgado.Juzgado;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.UnidadYJuzgado.UnidadPolicial;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.IDExpAXG.Prov;
import com.siga.ws.i2064.je.xsd.PROCBAREMOTYPE.PROCPORCENTUAL;

public class SantiagoJE extends InformeXML implements PCAJGConstantes {

	private static String CODIGO_PETICION_CORRECTA = "C0001";
	private static int CODIGO_APLICACION = 1;
	private static String USUARIO = "Pepe";
	
	
	
	private int idFacturacion = -1; 
	private BufferedWriter bw = null;
	private File fileError;
	
	public int getIdFacturacion() {
		return idFacturacion;
	}

	public void setIdFacturacion(int idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	
	private DatosJustificacionesDocument getDatosJustificacionesDocument(String idInstitucion, String idFacturacion, UsrBean usrBean) throws Exception  {
		
		WSSantiagoAdm wsSantiagoAdm = new WSSantiagoAdm();
		Map<String, String> mapaFacturacion = wsSantiagoAdm.getFacturacion(idInstitucion, idFacturacion);
		List<Hashtable<String, String>> listMapAsis = wsSantiagoAdm.getFacturacionAsistencias(idInstitucion, idFacturacion);
		List<Hashtable<String, String>> listMapTurnoOficio = wsSantiagoAdm.getFacturacionTurnoOficio(idInstitucion, idFacturacion);
	
		DatosJustificacionesDocument datosJustificacionesDocument = DatosJustificacionesDocument.Factory.newInstance();
		DatosJustificaciones datosJustificaciones = datosJustificacionesDocument.addNewDatosJustificaciones();
		rellenaDatosFacturacion(datosJustificaciones, mapaFacturacion);		
		
		Short codColegiado = -1;
		
		Asistencias asistencias = datosJustificaciones.addNewAsistencias();
		Colegiado colegiadoAsistencia = null;
		
		//si había un fichero previo de incidencias lo borramos
		File file = getFileInformeIncidencias(idInstitucion, idFacturacion);
		if (file != null && file.exists()) {
			file.delete();
		}		
		
		for (Hashtable<String, String> hash : listMapAsis) {
			String asistencia = hash.get(ANIO_ASISTENCIA) + "/" + hash.get(NUMERO_ASISTENCIA);
			try {
				Short numColegiado = SigaWSHelper.getShort("número de colegiado", hash.get(A_CODCOLEGIADO));
				if (!codColegiado.equals(numColegiado)) {
					codColegiado = numColegiado;
					colegiadoAsistencia = asistencias.addNewColegiado();
					colegiadoAsistencia.setCodColegiado(codColegiado); 
				}			
				Asuntos asuntos = colegiadoAsistencia.addNewAsuntos();
				asuntos.setFecha(SigaWSHelper.getCalendar(hash.get(A_FECHA)));
				
				rellenaSoxClaveType(asuntos.addNewSOXCLAVE(), SigaWSHelper.getInteger("sox año", hash.get(A_SOX_ANO)), SigaWSHelper.getBigInteger("sox número", hash.get(A_SOX_NUMERO)));
				
				IDExpAXG idExpAXG = asuntos.addNewIDExpAXG();
				idExpAXG.setCons(hash.get(A_EXP_CONS));
				idExpAXG.setProc(hash.get(A_EXP_PROC));
				Integer in = SigaWSHelper.getInteger("año exp AXG", hash.get(A_EXP_ANO));
				if (in != null) idExpAXG.setAno(in);
				idExpAXG.setNum(SigaWSHelper.getBigInteger("número exp AXG", hash.get(A_EXP_NUM)));
				in = SigaWSHelper.getInteger("prov exp AXG", hash.get(A_EXP_PROV));
				if (in != null) idExpAXG.setProv(Prov.Enum.forInt(in+1));//El enumerado empieza en 1
				
				Datosatestado datosatestado = asuntos.addNewDatosatestado();
				
				if (!rellenaUnidadYJuzgado(datosatestado, hash)) {
					rellenaUnidadPolicial(datosatestado, hash);
					rellenaOrganoJudicial(datosatestado, hash);
				}
				
				rellenaPersonaType(asuntos.addNewDetido(), hash.get(A_D_NOME), hash.get(A_D_PRIMER_APELLIDO), hash.get(A_D_SEGUNDO_APELLIDO), hash.get(A_D_NIF));
				rellenaImporteType(asuntos.addNewIMPORTE(), SigaWSHelper.getBigDecimal("importe", hash.get(A_I_IMPORTE)), SigaWSHelper.getBigDecimal("irpf", hash.get(A_I_IRPF)));
				
				List<String> erroresList = SigaWSHelper.validate(asuntos);
				for (String error : erroresList) {
					String msg = "Error en la asistencia " + asistencia + ";";
					msg += error;
					escribeLog(idInstitucion, idFacturacion, usrBean, msg);
				}
			} catch (IllegalArgumentException e) {
				String msg = "Error en la asistencia " + asistencia + ";;";
				msg += e.getMessage();
				escribeLog(idInstitucion, idFacturacion, usrBean, msg);
			}
		}
		
		codColegiado = -1;
		
		TurnoOficio turnoOficio = datosJustificaciones.addNewTurnoOficio();
		com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado colegiadoTurnoOficio = null;
		
		for (Hashtable<String, String> hash : listMapTurnoOficio) {
			String actuacion = hash.get(NUMEROASUNTO);
			String designa = hash.get(ANIO_DESIGNA) + "/" + hash.get(NUMERO_DESIGNA);
			
			try {
				Short numColegiado = SigaWSHelper.getShort("número de colegiado", hash.get(TO_CODCOLEGIADO));
				if (!codColegiado.equals(numColegiado)) {
					codColegiado = numColegiado;
					colegiadoTurnoOficio = turnoOficio.addNewColegiado();
					colegiadoTurnoOficio.setCodColegiado(codColegiado);
				}
				com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado.Asuntos asuntos = colegiadoTurnoOficio.addNewAsuntos();
				asuntos.setFecha(SigaWSHelper.getCalendar(hash.get(TO_FECHA)));
				
				rellenaSoxClaveType(asuntos.addNewSOXCLAVE(), SigaWSHelper.getInteger("sox año", hash.get(TO_SOX_ANO)), SigaWSHelper.getBigInteger("sox número", hash.get(TO_SOX_NUMERO)));
				
				com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado.Asuntos.IDExpAXG idExpAXG = asuntos.addNewIDExpAXG();
				idExpAXG.setCons(hash.get(TO_EXP_CONS));
				idExpAXG.setProc(hash.get(TO_EXP_PROC));
				Integer in = SigaWSHelper.getInteger("año exp AXG", hash.get(TO_EXP_ANO));
				if (in != null) idExpAXG.setAno(in);
				idExpAXG.setNum(SigaWSHelper.getBigInteger("número exp AXG", hash.get(TO_EXP_NUM)));
				in = SigaWSHelper.getInteger("prov exp AXG", hash.get(TO_EXP_PROV));
				idExpAXG.setProv(com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado.Asuntos.IDExpAXG.Prov.Enum.forInt(in+1));//El enumerado empieza en 1
				
				rellenaDatosJudiciales(asuntos.addNewDatosxudiciais(), hash);
				rellenaPersonaType(asuntos.addNewSolicitante(), hash.get(TO_S_NOME), hash.get(TO_S_PRIMER_APELLIDO), hash.get(TO_S_SEGUNDO_APELLIDO), hash.get(TO_S_NIF));
				rellenaImporteType(asuntos.addNewIMPORTE(), SigaWSHelper.getBigDecimal("importe", hash.get(TO_I_IMPORTE)), SigaWSHelper.getBigDecimal("irpf", hash.get(TO_I_IRPF)));
				
				List<String> erroresList = SigaWSHelper.validate(asuntos);
				for (String error : erroresList) {
					String msg = "Error en actuación número " + actuacion + " de la designa " + designa + ";";
					msg += error;
					escribeLog(idInstitucion, idFacturacion, usrBean, msg);
				}
			} catch (IllegalArgumentException e) {
				String msg = "Error en actuación número " + actuacion + " de la designa " + designa + ";;";
				msg += e.getMessage();
				escribeLog(idInstitucion, idFacturacion, usrBean, msg);
			}
		}
		
		List<String> erroresList = SigaWSHelper.validate(datosJustificacionesDocument);
		for (String error : erroresList) {	
			String msg = "Error en el fichero generado;";
			msg += error;
			escribeLog(idInstitucion, idFacturacion, usrBean, msg);
		}
		return datosJustificacionesDocument;

	}

	@Override
	public File execute(String directorio, String nombreSalida, String idInstitucion, String idFacturacion, UsrBean usrBean) throws Exception {

		try {			
			String rutaAlm = getDirectorioSalida(directorio, idInstitucion);			
			File file = new File(rutaAlm);
			file.delete();
			file.mkdirs();
			
			for (File f : file.listFiles()) {
				ClsLogging.writeFileLog("Fichero eliminado (" + f.delete() + ") " + f.getAbsolutePath(), 3);			
			}			
		
			DatosJustificacionesDocument datosJustificacionesDocument = getDatosJustificacionesDocument(idInstitucion, idFacturacion, usrBean);
			XmlOptions xmlOptions = new XmlOptions();
			xmlOptions.setSavePrettyPrintIndent(4);
			xmlOptions.setSavePrettyPrint();

			String nombreFichero = getNombreFichero(nombreSalida, idInstitucion, usrBean);

			file = new File(file, nombreFichero + ".xml");
			datosJustificacionesDocument.save(file, xmlOptions);
			ClsLogging.writeFileLog("Generando fichero xml en: " + file.getAbsolutePath(), 3);

			if (closeLogFile()) {
				return fileError;
			} else {
				return file;
			}
		} finally {
			closeLogFile();
		}
	}

	private String getNombreFichero(String nombreSalida, String idInstitucion, UsrBean usrBean) throws ClsExceptions {
		return nombreSalida + "_" + idInstitucion + "_" + usrBean.getUserName() + "_"
			+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
	}

	private String getDirectorioSalida(String directorio, String idInstitucion) {
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlm = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
			+ ClsConstants.FILE_SEP
			+ directorio + ClsConstants.FILE_SEP
			+ (idInstitucion.equals("0") ? "2000" : idInstitucion) + ClsConstants.FILE_SEP;
		return rutaAlm;
	}
	


	private void rellenaDatosJudiciales(DATOSXUDICIAISTYPE datosxudiciais, Hashtable<String, String> hash) {
		rellenaOrganoXudicial(datosxudiciais.addNewJuzgado(), hash.get(TO_J_NUMEROSALASECCION), SigaWSHelper.getShort("partido judicial", hash.get(TO_J_PARTIDOXUDICIAL)), hash.get(TO_J_COD_ORGANO));
		PROCBAREMOTYPE procbaremotype = datosxudiciais.addNewProcBaremo();
		Short sh = SigaWSHelper.getShort("tipo baremo", hash.get(TO_J_TIPO_BAREMO));
		if (sh != null) procbaremotype.setTIPOBAREMO(sh);
		sh = SigaWSHelper.getShort("código baremo", hash.get(TO_J_COD_BAREMO));
		if (sh != null) procbaremotype.setCODBAREMO(sh);
		Integer in = SigaWSHelper.getInteger("año del procedimiento", hash.get(TO_J_ANO_PROC));
		if (in != null) procbaremotype.setANOPROC(in);
		Long l = SigaWSHelper.getLong("número del procedimiento", hash.get(TO_J_NUM_PROC));
		if (l != null) procbaremotype.setNUMPROC(l);
		String st = hash.get(TO_J_DESC_PROC);
		if (st != null) {
			procbaremotype.setDESCPROC(st);
		}
		String porcentaje = hash.get(TO_J_PORCENTAJE);
		String codBaremoPorcentaje = hash.get(TO_J_COD_BAREM_POR);
		if (!vacio(porcentaje) && !vacio(codBaremoPorcentaje)) {
			PROCPORCENTUAL procPorcentual = procbaremotype.addNewPROCPORCENTUAL();
			sh = SigaWSHelper.getShort("porcentaje", porcentaje);
			if (sh != null) {
				procPorcentual.setPORCENTAJE(sh);
			}
			sh = SigaWSHelper.getShort("código acreditación", codBaremoPorcentaje);
			if (sh != null) {
				procPorcentual.setCODBAREMO(sh);
			}
		}
	}

	private void rellenaSoxClaveType(SOXCLAVETYPE soxClave, Integer soxAno, BigInteger soxNumero) {
		if (soxAno != null)	soxClave.setSOXANO(soxAno);
		if (soxNumero != null) soxClave.setSOXNUMERO(soxNumero);		
	}

	private void rellenaImporteType(IMPORTETYPE importetype, BigDecimal importe, BigDecimal irpf) {		
		if (importe != null) importetype.setIMPORTE(importe);
		if (irpf != null) importetype.setIRPF(irpf);
	}

	private void rellenaPersonaType(PERSOATYPE persoatype, String nome, String primerApellido, String segundoApellido, String nif) {		
		NOMEAPELIDOSTYPE nomeapelidostype = persoatype.addNewNOMEAPELIDOS();
		nomeapelidostype.setNome(nome);
		nomeapelidostype.setPRIMERAPELLIDO(primerApellido);
		nomeapelidostype.setSEGUNDOAPELLIDO(segundoApellido);
		persoatype.setNIF(nif);
	}

	private void rellenaOrganoJudicial(Datosatestado datosatestado, Hashtable<String, String> hash) {
		if (!vacio(hash.get(A_J_COD_ORGANO))) {
			OrganoJudicial organoJudicial = datosatestado.addNewOrganoJudicial();
			rellenaOrganoXudicial(organoJudicial.addNewJuzgado(), hash.get(A_J_NUMEROSALASECCION), SigaWSHelper.getShort("partido judicial", hash.get(A_J_PARTIDOXUDICIAL)), hash.get(A_J_COD_ORGANO));
			Long l = SigaWSHelper.getLong("número de procedimiento", hash.get(A_J_NUM_PROC));
			if (l != null) organoJudicial.setNUMPROC(l);
			Integer in = SigaWSHelper.getInteger("año del procedimiento", hash.get(A_J_ANO_PROC));
			if (in != null)	organoJudicial.setANOPROC(in);
		}
	}

	private void rellenaUnidadPolicial(Datosatestado datosatestado, Hashtable<String, String> hash) {
		if (!vacio(hash.get(A_U_COD_UNIDADE))) {
			com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.UnidadPolicial unidadPolicial = datosatestado.addNewUnidadPolicial();
			rellenaComisaria(unidadPolicial.addNewComisaria(), hash.get(A_U_DESC_UNIDADE), hash.get(A_U_COD_UNIDADE));
			rellenaAtestado(unidadPolicial.addNewAtestado(), SigaWSHelper.getInteger("año del atestado", hash.get(A_U_ANO_ATESTADO)), SigaWSHelper.getLong("número del atestado", hash.get(A_U_NUM_ATESTADO)), hash.get(A_U_CAUSADETENCION));
		}		
	}

	private boolean rellenaUnidadYJuzgado(Datosatestado datosAtestado, Hashtable<String, String> hash) {
		boolean rellenaUnidadYJuzgado = !vacio(hash.get(A_U_COD_UNIDADE)) && !vacio(hash.get(A_J_COD_ORGANO)); 
		if (rellenaUnidadYJuzgado) {
			UnidadYJuzgado unidadYJuzgado = datosAtestado.addNewUnidadYJuzgado();
			UnidadPolicial unidadPolicial = unidadYJuzgado.addNewUnidadPolicial();
			rellenaComisaria(unidadPolicial.addNewComisaria(), hash.get(A_U_DESC_UNIDADE), hash.get(A_U_COD_UNIDADE));
			rellenaAtestado(unidadPolicial.addNewAtestado(), SigaWSHelper.getInteger("año atestado", hash.get(A_U_ANO_ATESTADO)), SigaWSHelper.getLong("número atestado", hash.get(A_U_NUM_ATESTADO)), hash.get(A_U_CAUSADETENCION));
			Juzgado juzgado = unidadYJuzgado.addNewJuzgado();
			rellenaOrganoXudicial(juzgado.addNewJuzgado(), hash.get(A_J_NUMEROSALASECCION), SigaWSHelper.getShort("partido judicial", hash.get(A_J_PARTIDOXUDICIAL)), hash.get(A_J_COD_ORGANO));
			Long l = SigaWSHelper.getLong("num proc", hash.get(A_J_NUM_PROC));
			if (l != null) juzgado.setNUMPROC(l);
			Integer in = SigaWSHelper.getInteger("año proc", hash.get(A_J_ANO_PROC));
			if (in != null) juzgado.setANOPROC(in);
		}
		return rellenaUnidadYJuzgado;
	}

	private boolean vacio(String st) {		
		return st == null || st.trim().equals("");
	}

	private void rellenaOrganoXudicial(ORGANOXUDICIALTYPE organoxudicialtype, String numeroSalaSeccion, Short partidoJudicial, String codOrgano) {		 
		organoxudicialtype.setNUMEROSALASECCION(numeroSalaSeccion);
		if (partidoJudicial != null) organoxudicialtype.setPARTIDOXUDICIAL(partidoJudicial);
		organoxudicialtype.setCODORGANO(codOrgano);
	}

	private void rellenaAtestado(ATESTADOTYPE atestadotype, Integer anoAtestado, Long numAtestado, String causaDetencion) {
		if (anoAtestado != null) atestadotype.setANOATESTADO(anoAtestado);
		if (numAtestado != null) atestadotype.setNUMATESTADO(numAtestado);
		atestadotype.setCausaDetencion(causaDetencion);
	}

	private void rellenaComisaria(COMISARIATYPE comisariatype, String descUnidade, String codUnidade) {		 
		comisariatype.setDESCUNIDADE(descUnidade);
		comisariatype.setCODUNIDADE(codUnidade);
	}

	private void rellenaDatosFacturacion(DatosJustificaciones datosJustificaciones, Map<String, String> mapaFacturacion) throws Exception {
		Periodo periodo = datosJustificaciones.addNewPeriodo();
		Integer in = SigaWSHelper.getInteger("periodo año", mapaFacturacion.get(PERIODO_ANO));
		if (in != null) periodo.setAno(in);
		in = SigaWSHelper.getInteger("periodo trimestre", mapaFacturacion.get(PERIODO_TRIMESTRE));
		if (in != null) periodo.setTrimestre(TRIMESTRETYPE.Enum.forInt(in));
		periodo.setDende(SigaWSHelper.getCalendar(mapaFacturacion.get(PERIODO_DENDE)));
		periodo.setAta(SigaWSHelper.getCalendar(mapaFacturacion.get(PERIODO_ATA)));
		
		datosJustificaciones.addNewColegio().setIDColegio(mapaFacturacion.get(COL_IDCOLEGIO));
	}
	
	/**
	 * 
	 * @param informe 
	 * @param bufferedWriter
	 * @param texto
	 * @param msg 
	 * @param usrBean 
	 * @throws IOException
	 * @throws ClsExceptions 
	 */
	private void escribeLog(String idInstitucion, String idFacturacion, UsrBean usrBean, String texto) throws IOException, ClsExceptions {
		if (bw == null) {
			FileWriter fileWriter = new FileWriter(getFileInformeIncidencias(idInstitucion, idFacturacion), true);
			bw = new BufferedWriter(fileWriter);
		}				
		
		bw.write(texto);
		bw.write("\n");
		bw.flush();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private boolean closeLogFile() {
		boolean abierto = false;
		if (bw != null) {
			abierto = true;
			try {
				bw.flush();			
				bw.close();
				bw = null;
			} catch (IOException e) {
				ClsLogging.writeFileLogError("Error al cerrar el fichero de LOG", e, 3);
			}
		}
		return abierto;
	}
	
	/**
	 * 
	 * @return
	 */
	private EngineConfiguration createClientConfig(UsrBean usrBean, String idInstitucion, String idFacturacion) {
		
		SimpleProvider clientConfig = new SimpleProvider();		
		Handler logSIGAasignaHandler = (Handler) new LogBDDHandler(usrBean, idInstitucion, "Identificador de la facturación = " + idFacturacion);		
		SimpleChain reqHandler = new SimpleChain();
		SimpleChain respHandler = new SimpleChain();		
		reqHandler.addHandler(logSIGAasignaHandler);
		respHandler.addHandler(logSIGAasignaHandler);
		Handler pivot = (Handler) new HTTPSender();
				
		Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
		clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
				
		return clientConfig;
	}	
	


	@Override
	public void envioWS(String idInstitucion, String idFacturacion, UsrBean usrBean) throws ErrorValidacionXML, ErrorNegocioWS, ErrorEnvioWS, Exception {
		
			try {
				String urlWS = getUrlWsJE(idInstitucion, usrBean);		
						
				DatosJustificacionesDocument datosJustificacionesDocument = getDatosJustificacionesDocument(idInstitucion, idFacturacion, usrBean);
				
				if (closeLogFile()) {
					//TODO
					throw new ErrorValidacionXML("El fichero xml generado no ha sido validado correctamente para la institución " + idInstitucion);
				}		
				
				EnvioJustificacionesService_ServiceLocator locator = new EnvioJustificacionesService_ServiceLocator(createClientConfig(usrBean, idInstitucion, idFacturacion));
				EnvioJustificacionesServicePortBindingStub stub = new EnvioJustificacionesServicePortBindingStub(new java.net.URL(urlWS), locator);
				
				String datosJustificaciones = datosJustificacionesDocument.xmlText();

				EnvioJustificacion envioJustificacion = EnvioJustificacion.Factory.newInstance();
				envioJustificacion.setCodAplicacion(CODIGO_APLICACION);
				envioJustificacion.setUsuario(USUARIO);
				envioJustificacion.setDatosJustificaciones(datosJustificacionesDocument.getDatosJustificaciones());
								
				com.siga.ws.i2064.je.axis.Resposta resposta = null;
				
				try {
					resposta = stub.envioJustificacion(CODIGO_APLICACION, USUARIO, datosJustificaciones);
				} catch (Exception e) {
					String s = "Se ha producido un error en el envío de WebService para la institución " + idInstitucion;
					ClsLogging.writeFileLogError(s, e, 3);
					throw new ErrorEnvioWS(s, e);
//					throw new Exception(s, e);
				}
								
				if (!CODIGO_PETICION_CORRECTA.equals(resposta.getCodResposta())) {
//					escribeLog(idInstitucion, idFacturacion, usrBean, resposta.getCodResposta() + ";" + resposta.getDescResposta());
//					throw new ErrorNegocioWS(resposta.getCodResposta() + ": " + resposta.getDescResposta());
					escribeLog(idInstitucion, idFacturacion, usrBean, resposta.getCodResposta());
					throw new ErrorNegocioWS(resposta.getCodResposta());
				}			
			} finally {
				closeLogFile();			
			}
			

	}

	



}
