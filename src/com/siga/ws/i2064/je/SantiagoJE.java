package com.siga.ws.i2064.je;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.autogen.model.FcsFacturacionjgKey;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.FileHelper;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.LogBDDHandler;
import com.siga.beans.GenParametrosAdm;
import com.siga.ws.InformeXML;
import com.siga.ws.i2064.WSSantiagoAdm;
import com.siga.ws.i2064.je.axis.EnvioJustificacionesServicePortBindingStub;
import com.siga.ws.i2064.je.axis.EnvioJustificacionesService_ServiceLocator;
import com.siga.ws.i2064.je.error.ErrorEnvioWS;
import com.siga.ws.i2064.je.error.ErrorNegocioWS;
import com.siga.ws.i2064.je.error.ErrorValidacionXML;
import com.siga.ws.i2064.je.xsd.ATESTADOTYPE;
import com.siga.ws.i2064.je.xsd.COMISARIATYPE;
import com.siga.ws.i2064.je.xsd.DATOSXUDICIAISTYPE;
import com.siga.ws.i2064.je.xsd.DOCIDENTIFICADORTYPE;
import com.siga.ws.i2064.je.xsd.DOCIDENTIFICADORTYPE.DOCUMENTADO;
import com.siga.ws.i2064.je.xsd.DOCIDENTIFICADORTYPE.DOCUMENTADO.TIPOIDENTIFICADOR;
import com.siga.ws.i2064.je.xsd.DOCIDENTIFICADORTYPE.INDOCUMENTADO;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.OrganoJudicial;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.UnidadYJuzgado;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.UnidadYJuzgado.Juzgado;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.UnidadYJuzgado.UnidadPolicial;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.IDExpAXG;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.IDExpAXG.Prov;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Periodo;
import com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio;
import com.siga.ws.i2064.je.xsd.IMPORTETYPE;
import com.siga.ws.i2064.je.xsd.NOMEAPELIDOSTYPE;
import com.siga.ws.i2064.je.xsd.ORGANOXUDICIALTYPE;
import com.siga.ws.i2064.je.xsd.PERSOATYPE;
import com.siga.ws.i2064.je.xsd.PROCBAREMOTYPE;
import com.siga.ws.i2064.je.xsd.PROCBAREMOTYPE.PROCPORCENTUAL;
import com.siga.ws.i2064.je.xsd.SOXCLAVETYPE;
import com.siga.ws.i2064.je.xsd.TRIMESTRETYPE;
import com.siga.ws.i2064.je.xsd.resposta.CargaAsunto;

import es.satec.businessManager.BusinessManager;

public class SantiagoJE extends InformeXML implements PCAJGConstantes {

	private static String CODIGO_PETICION_CORRECTA = "C0001";
	private static String CODIGO_ASUNTO_CORRECTO = "AC0001";
	
	private static String PCAJG_JE_CODIGO_APLICACION = "PCAJG_JE_CODIGO_APLICACION";
	private static String PCAJG_JE_USUARIO = "PCAJG_JE_USUARIO";	
	
	private int idFacturacion = -1; 
	
		
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
		
		//si hab�a un fichero previo de incidencias lo borramos
		File file = getFileInformeIncidencias(idInstitucion, idFacturacion);
		if (file != null && file.exists()) {
			file.delete();
		}		
		
		for (Hashtable<String, String> hash : listMapAsis) {
			String asistencia = hash.get(ANIO_ASISTENCIA) + "/" + hash.get(NUMERO_ASISTENCIA);
			try {
				Short numColegiado = SIGAServicesHelper.getShort("n�mero de colegiado", hash.get(A_CODCOLEGIADO));
				if (!codColegiado.equals(numColegiado)) {
					codColegiado = numColegiado;
					colegiadoAsistencia = asistencias.addNewColegiado();
					colegiadoAsistencia.setCodColegiado(codColegiado); 
				}			
				Asuntos asuntos = colegiadoAsistencia.addNewAsuntos();
				asuntos.setFechaActuacion(SIGAServicesHelper.getCalendar(hash.get(A_FECHAACTUACION)));
				
				rellenaSoxClaveType(asuntos.addNewSOXCLAVE(), SIGAServicesHelper.getInteger("sox a�o", hash.get(A_SOX_ANO)), SIGAServicesHelper.getBigInteger("sox n�mero", hash.get(A_SOX_NUMERO)));
				
				IDExpAXG idExpAXG = asuntos.addNewIDExpAXG();
				idExpAXG.setCons(hash.get(A_EXP_CONS));
				idExpAXG.setProc(hash.get(A_EXP_PROC));
				Integer in = SIGAServicesHelper.getInteger("a�o exp AXG", hash.get(A_EXP_ANO));
				if (in != null) idExpAXG.setAno(in);
				idExpAXG.setNum(SIGAServicesHelper.getBigInteger("n�mero exp AXG", hash.get(A_EXP_NUM)));
				in = SIGAServicesHelper.getInteger("prov exp AXG", hash.get(A_EXP_PROV));
				if (in != null) idExpAXG.setProv(Prov.Enum.forInt(in+1));//El enumerado empieza en 1
				
				asuntos.setFechaResolAXG(SIGAServicesHelper.getCalendar(hash.get(A_FECHARESOLAXG)));
				
				Datosatestado datosatestado = asuntos.addNewDatosatestado();
				
				if (!rellenaUnidadYJuzgado(datosatestado, hash)) {
					rellenaUnidadPolicial(datosatestado, hash);
					rellenaOrganoJudicial(datosatestado, hash);
				}
				
				rellenaPersonaType(asuntos.addNewDetido(), hash.get(A_D_NOME), hash.get(A_D_PRIMER_APELLIDO), hash.get(A_D_SEGUNDO_APELLIDO), hash.get(A_D_TIPOIDENTIFICADOR), hash.get(A_D_NIF));
				rellenaImporteType(asuntos.addNewIMPORTE(), SIGAServicesHelper.getBigDecimal("importe", hash.get(A_I_IMPORTE)), SIGAServicesHelper.getBigDecimal("irpf", hash.get(A_I_IRPF)));
				
				List<String> erroresList = SIGAServicesHelper.validate(asuntos);
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
				Short numColegiado = SIGAServicesHelper.getShort("n�mero de colegiado", hash.get(TO_CODCOLEGIADO));
				if (!codColegiado.equals(numColegiado)) {
					codColegiado = numColegiado;
					colegiadoTurnoOficio = turnoOficio.addNewColegiado();
					colegiadoTurnoOficio.setCodColegiado(codColegiado);
				}
				com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado.Asuntos asuntos = colegiadoTurnoOficio.addNewAsuntos();
				asuntos.setFechaActuacion(SIGAServicesHelper.getCalendar(hash.get(TO_FECHAACTUACION)));
				
				rellenaSoxClaveType(asuntos.addNewSOXCLAVE(), SIGAServicesHelper.getInteger("sox a�o", hash.get(TO_SOX_ANO)), SIGAServicesHelper.getBigInteger("sox n�mero", hash.get(TO_SOX_NUMERO)));
				
				com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado.Asuntos.IDExpAXG idExpAXG = asuntos.addNewIDExpAXG();
				idExpAXG.setCons(hash.get(TO_EXP_CONS));
				idExpAXG.setProc(hash.get(TO_EXP_PROC));
				Integer in = SIGAServicesHelper.getInteger("a�o exp AXG", hash.get(TO_EXP_ANO));
				if (in != null) idExpAXG.setAno(in);
				idExpAXG.setNum(SIGAServicesHelper.getBigInteger("n�mero exp AXG", hash.get(TO_EXP_NUM)));
				in = SIGAServicesHelper.getInteger("prov exp AXG", hash.get(TO_EXP_PROV));
				if (in != null) {
					idExpAXG.setProv(com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado.Asuntos.IDExpAXG.Prov.Enum.forInt(in+1));//El enumerado empieza en 1
				}
								
				asuntos.setFechaResolAXG(SIGAServicesHelper.getCalendar(hash.get(TO_FECHARESOLAXG)));
				
				rellenaDatosJudiciales(asuntos.addNewDatosxudiciais(), hash);
				rellenaPersonaType(asuntos.addNewSolicitante(), hash.get(TO_S_NOME), hash.get(TO_S_PRIMER_APELLIDO), hash.get(TO_S_SEGUNDO_APELLIDO), hash.get(TO_S_TIPOIDENTIFICADOR), hash.get(TO_S_NIF));
				rellenaImporteType(asuntos.addNewIMPORTE(), SIGAServicesHelper.getBigDecimal("importe", hash.get(TO_I_IMPORTE)), SIGAServicesHelper.getBigDecimal("irpf", hash.get(TO_I_IRPF)));
				
				List<String> erroresList = SIGAServicesHelper.validate(asuntos);
				for (String error : erroresList) {
					String msg = "Error en actuaci�n n�mero " + actuacion + " de la designa " + designa + ";";
					msg += error;
					escribeLog(idInstitucion, idFacturacion, usrBean, msg);
				}
			} catch (IllegalArgumentException e) {
				String msg = "Error en actuaci�n n�mero " + actuacion + " de la designa " + designa + ";;";
				msg += e.getMessage();
				escribeLog(idInstitucion, idFacturacion, usrBean, msg);
			}
		}
		
		List<String> erroresList = SIGAServicesHelper.validate(datosJustificacionesDocument);
		for (String error : erroresList) {	
			String msg = "Error en el fichero generado;";
			msg += error;
			escribeLog(idInstitucion, idFacturacion, usrBean, msg);
		}
		return datosJustificacionesDocument;

	}

	@Override
	public List<File> execute(String directorio, String nombreSalida, String idInstitucion, String idFacturacion, UsrBean usrBean) throws Exception {

		try {		
			List<File> listaFicheros = new ArrayList<File>();
			
			String rutaAlm = getDirectorioSalida(directorio, idInstitucion, idFacturacion);			
			File file = new File(rutaAlm);
			file.delete();
			FileHelper.mkdirs(rutaAlm);
			
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
				listaFicheros.add(getFileInformeIncidencias(idInstitucion, idFacturacion));
			} else {
				listaFicheros.add(file);
			}
			return listaFicheros;
		} finally {
			closeLogFile();
		}
	}

		
	private void rellenaDatosJudiciales(DATOSXUDICIAISTYPE datosxudiciais, Hashtable<String, String> hash) {
		rellenaOrganoXudicial(datosxudiciais.addNewJuzgado(), hash.get(TO_J_NUMEROSALASECCION), SIGAServicesHelper.getShort("partido judicial", hash.get(TO_J_PARTIDOXUDICIAL)), hash.get(TO_J_COD_ORGANO));
		PROCBAREMOTYPE procbaremotype = datosxudiciais.addNewProcBaremo();
		Short sh = SIGAServicesHelper.getShort("tipo baremo", hash.get(TO_J_TIPO_BAREMO));
		if (sh != null) procbaremotype.setTIPOBAREMO(sh);
		sh = SIGAServicesHelper.getShort("c�digo baremo", hash.get(TO_J_COD_BAREMO));
		if (sh != null) procbaremotype.setCODBAREMO(sh);
		Integer in = SIGAServicesHelper.getInteger("a�o del procedimiento", hash.get(TO_J_ANO_PROC));
		if (in != null) procbaremotype.setANOPROC(in);
		Long l = SIGAServicesHelper.getLong("n�mero del procedimiento", hash.get(TO_J_NUM_PROC));
		if (l != null) procbaremotype.setNUMPROC(l);
		String st = hash.get(TO_J_DESC_PROC);
		if (st != null) {
			procbaremotype.setDESCPROC(st);
		}
		String porcentaje = hash.get(TO_J_PORCENTAJE);
		String codBaremoPorcentaje = hash.get(TO_J_COD_BAREM_POR);
		if (!vacio(porcentaje) && !vacio(codBaremoPorcentaje)) {
			PROCPORCENTUAL procPorcentual = procbaremotype.addNewPROCPORCENTUAL();
			sh = SIGAServicesHelper.getShort("porcentaje", porcentaje);
			if (sh != null) {
				procPorcentual.setPORCENTAJE(sh);
			}
			sh = SIGAServicesHelper.getShort("c�digo acreditaci�n", codBaremoPorcentaje);
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

	private void rellenaPersonaType(PERSOATYPE persoatype, String nome, String primerApellido, String segundoApellido, String tipoIdentificador, String nif) {		
		NOMEAPELIDOSTYPE nomeapelidostype = persoatype.addNewNOMEAPELIDOS();
		nomeapelidostype.setNome(nome);
		nomeapelidostype.setPRIMERAPELLIDO(primerApellido);
		nomeapelidostype.setSEGUNDOAPELLIDO(segundoApellido);
		
		DOCIDENTIFICADORTYPE identificacion = persoatype.addNewIDENTIFICACION();
		
		if (tipoIdentificador == null || tipoIdentificador.trim().equals("")) {
			identificacion.setINDOCUMENTADO(INDOCUMENTADO.S);
		} else {
			DOCUMENTADO documentado = identificacion.addNewDOCUMENTADO();
			documentado.setTIPOIDENTIFICADOR(TIPOIDENTIFICADOR.Enum.forString(tipoIdentificador));
			documentado.setIDENTIFICADOR(nif);
		}
		
	}

	private void rellenaOrganoJudicial(Datosatestado datosatestado, Hashtable<String, String> hash) {
		if (!vacio(hash.get(A_J_COD_ORGANO))) {
			OrganoJudicial organoJudicial = datosatestado.addNewOrganoJudicial();
			rellenaOrganoXudicial(organoJudicial.addNewJuzgado(), hash.get(A_J_NUMEROSALASECCION), SIGAServicesHelper.getShort("partido judicial", hash.get(A_J_PARTIDOXUDICIAL)), hash.get(A_J_COD_ORGANO));
			Long l = SIGAServicesHelper.getLong("n�mero de procedimiento", hash.get(A_J_NUM_PROC));
			if (l != null) organoJudicial.setNUMPROC(l);
			Integer in = SIGAServicesHelper.getInteger("a�o del procedimiento", hash.get(A_J_ANO_PROC));
			if (in != null)	organoJudicial.setANOPROC(in);
		}
	}

	private void rellenaUnidadPolicial(Datosatestado datosatestado, Hashtable<String, String> hash) {
		if (!vacio(hash.get(A_U_COD_UNIDADE))) {
			com.siga.ws.i2064.je.xsd.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos.Datosatestado.UnidadPolicial unidadPolicial = datosatestado.addNewUnidadPolicial();
			rellenaComisaria(unidadPolicial.addNewComisaria(), hash.get(A_U_DESC_UNIDADE), hash.get(A_U_COD_UNIDADE));
			rellenaAtestado(unidadPolicial.addNewAtestado(), SIGAServicesHelper.getInteger("a�o del atestado", hash.get(A_U_ANO_ATESTADO)), SIGAServicesHelper.getLong("n�mero del atestado", hash.get(A_U_NUM_ATESTADO)), hash.get(A_U_CAUSADETENCION));
		}		
	}

	private boolean rellenaUnidadYJuzgado(Datosatestado datosAtestado, Hashtable<String, String> hash) {
		boolean rellenaUnidadYJuzgado = !vacio(hash.get(A_U_COD_UNIDADE)) && !vacio(hash.get(A_J_COD_ORGANO)); 
		if (rellenaUnidadYJuzgado) {
			UnidadYJuzgado unidadYJuzgado = datosAtestado.addNewUnidadYJuzgado();
			UnidadPolicial unidadPolicial = unidadYJuzgado.addNewUnidadPolicial();
			rellenaComisaria(unidadPolicial.addNewComisaria(), hash.get(A_U_DESC_UNIDADE), hash.get(A_U_COD_UNIDADE));
			rellenaAtestado(unidadPolicial.addNewAtestado(), SIGAServicesHelper.getInteger("a�o atestado", hash.get(A_U_ANO_ATESTADO)), SIGAServicesHelper.getLong("n�mero atestado", hash.get(A_U_NUM_ATESTADO)), hash.get(A_U_CAUSADETENCION));
			Juzgado juzgado = unidadYJuzgado.addNewJuzgado();
			rellenaOrganoXudicial(juzgado.addNewJuzgado(), hash.get(A_J_NUMEROSALASECCION), SIGAServicesHelper.getShort("partido judicial", hash.get(A_J_PARTIDOXUDICIAL)), hash.get(A_J_COD_ORGANO));
			Long l = SIGAServicesHelper.getLong("num proc", hash.get(A_J_NUM_PROC));
			if (l != null) juzgado.setNUMPROC(l);
			Integer in = SIGAServicesHelper.getInteger("a�o proc", hash.get(A_J_ANO_PROC));
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
		Integer in = SIGAServicesHelper.getInteger("periodo a�o", mapaFacturacion.get(PERIODO_ANO));
		if (in != null) periodo.setAno(in);
		in = SIGAServicesHelper.getInteger("periodo trimestre", mapaFacturacion.get(PERIODO_TRIMESTRE));
		if (in != null) periodo.setTrimestre(TRIMESTRETYPE.Enum.forInt(in));
		periodo.setDende(SIGAServicesHelper.getCalendar(mapaFacturacion.get(PERIODO_DENDE)));
		periodo.setAta(SIGAServicesHelper.getCalendar(mapaFacturacion.get(PERIODO_ATA)));
		
		datosJustificaciones.addNewColegio().setIDColegio(mapaFacturacion.get(COL_IDCOLEGIO));
	}
	

	
	/**
	 * 
	 * @return
	 */
	private EngineConfiguration createClientConfig(UsrBean usrBean, String idInstitucion, String idFacturacion) {
		
		SimpleProvider clientConfig = new SimpleProvider();		
		Handler logSIGAasignaHandler = (Handler) new LogBDDHandler(usrBean, idInstitucion, "Identificador de la facturaci�n = " + idFacturacion);		
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
	public void envioWS(String idInstitucion, String idFacturacion) {
		FcsFacturacionjgKey fcsFacturacionjgKey = new FcsFacturacionjgKey();
		fcsFacturacionjgKey.setIdinstitucion(Short.valueOf(idInstitucion));
		fcsFacturacionjgKey.setIdfacturacion(Integer.valueOf(idFacturacion));
		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdinstitucion(Short.valueOf(idInstitucion));		
		ecomCola.setIdoperacion(AppConstants.OPERACION.XUNTA_JE.getId());
		
		EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
		ecomColaService.insertaColaFcsFacturacionJG(ecomCola, fcsFacturacionjgKey);
	}
	
	/**
	 * M�todo antiguo que ejecutaba el env�o por webservice pero ahora lo har� ecom
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param usrBean
	 * @throws ErrorValidacionXML
	 * @throws ErrorNegocioWS
	 * @throws ErrorEnvioWS
	 * @throws Exception
	 */
	public void envioWS(String idInstitucion, String idFacturacion, UsrBean usrBean) throws ErrorValidacionXML, ErrorNegocioWS, ErrorEnvioWS, Exception {
		
			try {
				String urlWS = getUrlWsJE(idInstitucion, usrBean);		
						
				DatosJustificacionesDocument datosJustificacionesDocument = getDatosJustificacionesDocument(idInstitucion, idFacturacion, usrBean);
//				DatosJustificacionesDocument datosJustificacionesDocument = DatosJustificacionesDocument.Factory.parse(new File("c:/Editar.xml"));
				
				XmlOptions xmlOptions = new XmlOptions();
				xmlOptions.setSavePrettyPrintIndent(4);
				xmlOptions.setSavePrettyPrint();
				xmlOptions.setCharacterEncoding("ISO-8859-1");
				
				Map<String, String> mapa = new HashMap<String, String>();
				mapa.put(datosJustificacionesDocument.getDatosJustificaciones().getDomNode().getNamespaceURI(), "");
				xmlOptions.setSaveSuggestedPrefixes(mapa);
				
				String xml = "<?xml version=\"1.0\" encoding=\"ISO8859-1\"?>" + datosJustificacionesDocument.xmlText(xmlOptions);
				ClsLogging.writeFileLog("Fichero justificaci�n econ�mica de la Xunta del colegio " + idInstitucion + ":\n" + xml, 3);
				
				if (closeLogFile()) {					
					throw new ErrorValidacionXML("El fichero xml generado no ha sido validado correctamente para la instituci�n " + idInstitucion);
				}		
				
				EnvioJustificacionesService_ServiceLocator locator = new EnvioJustificacionesService_ServiceLocator(createClientConfig(usrBean, idInstitucion, idFacturacion));
				EnvioJustificacionesServicePortBindingStub stub = new EnvioJustificacionesServicePortBindingStub(new java.net.URL(urlWS), locator);
				
								
				com.siga.ws.i2064.je.axis.Resposta resposta = null;
				
				GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);		
				int codAplicacion = Integer.parseInt(admParametros.getValor(idInstitucion, MODULO_SCS, PCAJG_JE_CODIGO_APLICACION, ""));
				String usuario = admParametros.getValor(idInstitucion, MODULO_SCS, PCAJG_JE_USUARIO, "");
				
				try {
					resposta = stub.envioJustificacion(codAplicacion, usuario, xml);
				} catch (Exception e) {
					String s = "Se ha producido un error en el env�o de WebService de Justificaci�n econ�mica para la instituci�n " + idInstitucion;
					ClsLogging.writeFileLogError(s, e, 3);
					throw new ErrorEnvioWS(s, e);
				}
								
				if (!CODIGO_PETICION_CORRECTA.equals(resposta.getCodResposta())) {
					escribeLog(idInstitucion, idFacturacion, usrBean, resposta.getCodResposta() + ";" + resposta.getDescricion());
					String ficheiroResposta = resposta.getFicheiroResposta();
					if (ficheiroResposta != null && !ficheiroResposta.trim().equals("")) {
						XmlOptions xmlOptionsRes = new XmlOptions();
												
//						Map<String, String> mapaRes = new HashMap<String, String>();						
//						mapaRes.put("", com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.Factory.newInstance().addNewDatosJustificaciones().getDomNode().getNamespaceURI());
//						xmlOptionsRes.setLoadSubstituteNamespaces(mapaRes);
						
						com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument res = com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.Factory.parse(ficheiroResposta, xmlOptionsRes);						
						
						if (res != null) {
							if (!res.validate()) {
								StringBuffer s = new StringBuffer("Se ha producido un error en el env�o de WebService de Justificaci�n econ�mica para la instituci�n " + idInstitucion + ". La respuesta obtenida no cumple con el esquema establecido.");
								List<String> errores = SIGAServicesHelper.validate(res);
								if (errores != null) {
									for (String error:errores) {
										s.append("\n" + error);
									}
								}
								throw new ErrorEnvioWS(s.toString());
							}
							
							com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones datosJustificaciones = res.getDatosJustificaciones();
							if (datosJustificaciones != null) {								
								com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.Asistencias asistencias = datosJustificaciones.getAsistencias();
								
								if (asistencias != null) {
									com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado[] cols = asistencias.getColegiadoArray();
									if (cols != null && cols.length > 0) {
										for (com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado col : cols) {
											short nColegiado = col.getCodColegiado();
											com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos asuntos[] = col.getAsuntosArray();
											if (asuntos != null && asuntos.length > 0) {
												for (com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.Asistencias.Colegiado.Asuntos asunto : asuntos) {
													com.siga.ws.i2064.je.xsd.resposta.SOXCLAVETYPE soxclavetype = asunto.getSOXCLAVE();
													if (soxclavetype != null) {
														int ejgAnio = soxclavetype.getSOXANO();
														BigInteger ejgNum = soxclavetype.getSOXNUMERO();
														rellenaError(idInstitucion, idFacturacion, usrBean, asunto.getCargaAsunto(), ejgAnio, ejgNum, nColegiado);														
													}
												}
											}
										}
									}
								}
								
								com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio turnoOficio = datosJustificaciones.getTurnoOficio();
								if (turnoOficio != null) {
									com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado cols[] = turnoOficio.getColegiadoArray();
									if (cols != null && cols.length > 0) {
										for (com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado col:cols) {
											short nColegiado = col.getCodColegiado();
											com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado.Asuntos[] asuntos = col.getAsuntosArray();
											if (asuntos != null && asuntos.length > 0) {
												for (com.siga.ws.i2064.je.xsd.resposta.DatosJustificacionesDocument.DatosJustificaciones.TurnoOficio.Colegiado.Asuntos asunto : asuntos) {
													com.siga.ws.i2064.je.xsd.resposta.SOXCLAVETYPE soxclavetype = asunto.getSOXCLAVE();
													if (soxclavetype != null) {
														int ejgAnio = soxclavetype.getSOXANO();
														BigInteger ejgNum = soxclavetype.getSOXNUMERO();														
														rellenaError(idInstitucion, idFacturacion, usrBean, asunto.getCargaAsunto(), ejgAnio, ejgNum, nColegiado);
													}
												}
											}
										}
									}
								}
							}
						}
					}
					throw new ErrorNegocioWS(resposta.getCodResposta() + ": " + resposta.getDescricion());					
				}			
			} finally {
				closeLogFile();			
			}
			

	}

	private void rellenaError(String idInstitucion, String idFacturacion, UsrBean usrBean, CargaAsunto cargaAsunto, int ejgAnio, BigInteger ejgNum, short nColegiado) throws ClsExceptions, IOException {
		
		if (cargaAsunto != null) {
			String desc = cargaAsunto.getDescripcion();
			String codigo = cargaAsunto.getCodigo();
			if (!CODIGO_ASUNTO_CORRECTO.equals(codigo) && desc != null && !desc.trim().equals("")) {
				String texto = "Error en el EJG;" + ejgAnio + "/" + ejgNum + ";para el colegiado;" + nColegiado + ";" + desc;																
				escribeLog(idInstitucion, idFacturacion, usrBean, texto);
			}
		}
		
	}

}
