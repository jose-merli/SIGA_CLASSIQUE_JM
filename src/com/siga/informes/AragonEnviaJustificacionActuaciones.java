package com.siga.informes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.redabogacia.pcajg.aragon.just.CentroDetencionOrganoJudicialType;
import org.redabogacia.pcajg.aragon.just.CodPeriodoType;
import org.redabogacia.pcajg.aragon.just.ColegiadoType;
import org.redabogacia.pcajg.aragon.just.ColegioType;
import org.redabogacia.pcajg.aragon.just.DesignacionType;
import org.redabogacia.pcajg.aragon.just.EjgType;
import org.redabogacia.pcajg.aragon.just.GuardiaType;
import org.redabogacia.pcajg.aragon.just.ImporteType;
import org.redabogacia.pcajg.aragon.just.InstanciaProcesalType;
import org.redabogacia.pcajg.aragon.just.JustificacionActuacionesDocument;
import org.redabogacia.pcajg.aragon.just.JustificacionActuacionesDocument.JustificacionActuaciones;
import org.redabogacia.pcajg.aragon.just.OficioType;
import org.redabogacia.pcajg.aragon.just.PartidoJudicialType;
import org.redabogacia.pcajg.aragon.just.PeriodoType;
import org.redabogacia.pcajg.aragon.just.ResolucionType;
import org.redabogacia.pcajg.aragon.just.SolicitanteType;
import org.redabogacia.pcajg.aragon.just.TipoEconomicoType;
import org.redabogacia.pcajg.aragon.just.TipoGuardiaType;
import org.redabogacia.pcajg.aragon.just.TipoJustificacionType;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;






public class AragonEnviaJustificacionActuaciones {
	private static final Logger log = Logger.getLogger(AragonEnviaJustificacionActuaciones.class);
	public static final String[] camposExcelOficio ={"ANO","CODPERIODO","NUMPERIODO","FECHADESDE","FECHAHASTA","COLEGIO_COD","COLEGIO_NOMBRE","TIPOJUSTIFICACION","VERSIONJUSTIFICACION","FECHAJUSTIFICACION","NEPAG_AÑO","NEPAG_NUMERO","NOMBRE_ABOGADO","APELLIDOS_ABOGADO","Nº_COLEGIADO","NOMBRE_SOLICITANTE","APELLIDOS_SOLICITANTE","DOC_IDENTIFICATIVO","NIG","Nº_AUTOS_DILIG","ASUNTO","INSTANCIA_PROCESAL","COD_MOD","CUANTIA","DESI_ANIO","DESI_NUMERO","NUM_ACTU","RESOL_FAVORABLE","RESOL_FECHA","RESOL_CODIGO","RESOL_NOMBRE","RESOL_MOTIVO"};
	public static final String[] camposExcelGuardias ={"ANO","CODPERIODO","NUMPERIODO","FECHADESDE","FECHAHASTA","COLEGIO_COD","COLEGIO_NOMBRE","TIPOJUSTIFICACION","VERSIONJUSTIFICACION","FECHAJUSTIFICACION","GUARDIA_FECHA","GUARDIA_PJ_NOMBRE","GUARDIA_COLEGIADO_NOMBRE","GUARDIA_COLEGIADO_APELLIDOS","GUARDIA_COLEGIADO_NUMERO","GUARDIA_CODTIPO","GUARDIA_IMPORTE_CODMODULO","GUARDIA_IMPORTE_CUANTIA","TURNO","GUARDIA","IDGUARDIA"};
	public File getFile(String nombreSalida, Vector datosInforme) throws BusinessException{
		SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
		JustificacionActuacionesDocument justificacionActuacionesDocument =JustificacionActuacionesDocument.Factory.newInstance();
		JustificacionActuaciones justificacionActuaciones = null;
		TipoEconomicoType tipoEconomico = null;
		for (int i = 0; i < datosInforme.size(); i++) {
			Hashtable datosHashtable = (Hashtable)datosInforme.get(i);
			
			
			if(i==0) {
				justificacionActuaciones = justificacionActuacionesDocument.addNewJustificacionActuaciones();
				
				justificacionActuaciones.setNumeroDetallesJustificacion((short)datosInforme.size());
				justificacionActuaciones.setTipoJustificacion(TipoJustificacionType.Enum.forString(datosHashtable.get("TIPOJUSTIFICACION").toString()));
				justificacionActuaciones.setVersionJustificacion(datosHashtable.get("VERSIONJUSTIFICACION").toString());
				
				Calendar calFechaJustificacion = Calendar.getInstance();
				try {
					calFechaJustificacion.setTime(dateFormat.parse(datosHashtable.get("FECHAJUSTIFICACION").toString()));
				} catch (ParseException e) {
					log.error("Error al parsear la fecha justificacion",e);
					throw new BusinessException("Error al obtener la fecha justificacion de la justificacion de guardias");
				}
				
				justificacionActuaciones.setFechaJustificacion(calFechaJustificacion);
				
				ColegioType colegio = justificacionActuaciones.addNewColegio();
				colegio.setCodigo(datosHashtable.get("COLEGIO_COD").toString());
				colegio.setNombre(datosHashtable.get("COLEGIO_NOMBRE").toString());
				PeriodoType periodo = justificacionActuaciones.addNewPeriodo();
				
				periodo.setAno(Integer.parseInt(datosHashtable.get("ANO").toString()));
				periodo.setCodPeriodo(CodPeriodoType.Enum.forString(datosHashtable.get("CODPERIODO").toString()));
				periodo.setNumPeriodo(Short.parseShort(datosHashtable.get("NUMPERIODO").toString()));
				Calendar calFechaDesde = Calendar.getInstance();
				try {
					calFechaDesde.setTime(dateFormat.parse(datosHashtable.get("FECHADESDE").toString()));
				} catch (ParseException e) {
					log.error("Error al parsear la fecha desde",e);
					throw new BusinessException("Error al obtener la fecha desde de la justificacion de guardias");
				}
				periodo.setFechaDesde(calFechaDesde);
				Calendar calFechaHasta = Calendar.getInstance();
				try {
					calFechaHasta.setTime(dateFormat.parse(datosHashtable.get("FECHAHASTA").toString()));
				} catch (ParseException e) {
					log.error("Error al parsear la fecha hasta",e);
					throw new BusinessException("Error al obtener la fecha hasta de la justificacion de guardias");
				}
				periodo.setFechaHasta(calFechaHasta);
				tipoEconomico = justificacionActuaciones.addNewTipoEconomico();
			}
			
		    
			if(justificacionActuaciones.getTipoJustificacion()==null||justificacionActuaciones.getTipoJustificacion().toString().equals("PG")) {
				
								
				GuardiaType guardia = tipoEconomico.addNewGuardia();
				
				ColegiadoType colegiado = guardia.addNewColegiado();
				colegiado.setApellidos(datosHashtable.get("GUARDIA_COLEGIADO_APELLIDOS").toString());
				colegiado.setColegio(justificacionActuaciones.getColegio());
				colegiado.setNombre(datosHashtable.get("GUARDIA_COLEGIADO_NOMBRE").toString());
				colegiado.setNumColegiado(Short.parseShort(datosHashtable.get("GUARDIA_COLEGIADO_NUMERO").toString()));
				
				ImporteType importe = guardia.addNewImporte();
				importe.setCodModulo(datosHashtable.get("GUARDIA_IMPORTE_CODMODULO").toString());
				importe.setCuantia(new BigDecimal(datosHashtable.get("GUARDIA_IMPORTE_CUANTIA").toString()));
				
				PartidoJudicialType partidoJudicial = guardia.addNewPartidoJudicial();
				partidoJudicial.setCodigo(datosHashtable.get("GUARDIA_PJ_NOMBRE").toString());
				partidoJudicial.setNombre(datosHashtable.get("GUARDIA_PJ_NOMBRE").toString());
		//		
				
				guardia.setCodTipoGuardia(TipoGuardiaType.Enum.forString(datosHashtable.get("GUARDIA_CODTIPO").toString()));
				Calendar calFechaGuardia = Calendar.getInstance();
				try {
					calFechaGuardia.setTime(dateFormat.parse(datosHashtable.get("GUARDIA_FECHA").toString()));
				} catch (ParseException e) {
					log.error("Error al parsear la fecha guardia",e);
					throw new BusinessException("Error al obtener la fecha desde de la justificacion de guardia");
				}
				
				guardia.setFecha(calFechaGuardia);
			
			}else {
			
				
				OficioType oficio = tipoEconomico.addNewOficio();
				CentroDetencionOrganoJudicialType lugar = oficio.addNewCentroDetencionOrganoJudicial();
				lugar.setCodigo(datosHashtable.get("INSTANCIA_PROCESAL").toString());
				lugar.setNombre(datosHashtable.get("INSTANCIA_PROCESAL").toString());
				//juzgados
				lugar.setTipo((short)20);
				
				ColegiadoType colegiado = oficio.addNewColegiado();
				colegiado.setApellidos(datosHashtable.get("APELLIDOS_ABOGADO").toString());
				colegiado.setColegio(justificacionActuaciones.getColegio());
				colegiado.setNombre(datosHashtable.get("NOMBRE_ABOGADO").toString());
				colegiado.setNumColegiado(Short.parseShort(datosHashtable.get("Nº_COLEGIADO").toString()));
		
				DesignacionType designacionType = oficio.addNewDesignacion();
				InstanciaProcesalType instanciaProcesalType = designacionType.addNewInstanciaProcesal();
				instanciaProcesalType.setCodigo(datosHashtable.get("INSTANCIA_PROCESAL").toString());
				instanciaProcesalType.setNombre(datosHashtable.get("INSTANCIA_PROCESAL").toString());
				
				designacionType.setAnio(Short.parseShort(datosHashtable.get("DESI_ANIO").toString()));
				designacionType.setCodigo(datosHashtable.get("DESI_NUMERO").toString());
				designacionType.setAsunto(datosHashtable.get("ASUNTO").toString());
				
				designacionType.setNig(datosHashtable.get("NIG").toString());
				designacionType.setNumActuacion(Short.parseShort(datosHashtable.get("NUM_ACTU").toString()));
				designacionType.setNunAutosDilig(datosHashtable.get("Nº_AUTOS_DILIG").toString());
				
				
				EjgType ejgType = oficio.addNewExpediente();
				ejgType.setAno(Short.parseShort(datosHashtable.get("NEPAG_AÑO").toString()));
				ejgType.setNumero(datosHashtable.get("NEPAG_NUMERO").toString());
				
				ResolucionType resolucion = ejgType.addNewResolucion();
				resolucion.setFavorable(Short.parseShort(datosHashtable.get("RESOL_FAVORABLE").toString()));
				resolucion.setCodigo(datosHashtable.get("RESOL_CODIGO").toString());
				
				
				resolucion.setMotivo(datosHashtable.get("RESOL_MOTIVO").toString());
				Calendar calFechaResolucion = Calendar.getInstance();
				if(datosHashtable.get("RESOL_FECHA")!=null && !datosHashtable.get("RESOL_FECHA").toString().equals("")) {
					try {
						calFechaResolucion.setTime(dateFormat.parse(datosHashtable.get("RESOL_FECHA").toString()));
					} catch (ParseException e) {
						log.error("Error al parsear la fecha Resolucion",e);
						throw new BusinessException("Error al obtener la fecha Resolucion de la justificacion de actuacion");
					}
				}
				
				resolucion.setFecha(calFechaResolucion);
				resolucion.setNombre(datosHashtable.get("RESOL_NOMBRE").toString());
				
				
				
				
				ImporteType importe = oficio.addNewImporte();
				importe.setCodModulo(datosHashtable.get("COD_MOD").toString());
				importe.setCuantia(new BigDecimal(datosHashtable.get("CUANTIA").toString()));
				SolicitanteType solicitanteType = oficio.addNewSolicitante();
				solicitanteType.setApellidos(datosHashtable.get("APELLIDOS_SOLICITANTE").toString());
				solicitanteType.setDocIdentificativo(datosHashtable.get("DOC_IDENTIFICATIVO").toString());
				solicitanteType.setNombre(datosHashtable.get("NOMBRE_SOLICITANTE").toString());
			}
			
			
		}
//		List<String> erroresValidacion = SIGAServicesHelper.validate(justificacionActuacionesDocument, true);
//		
//		if(erroresValidacion == null || erroresValidacion.size() == 0) {
//			for (String error : erroresValidacion) {
//				log.info(error);	
//			}
//			
//		}
//		log.info(justificacionActuacionesDocument.xmlText());	
		return createFileXML(justificacionActuacionesDocument ,nombreSalida+".xml");
		
		
	}
	private File createFileXML(XmlObject xmlObject, String fileName ) throws BusinessException {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrintIndent(4);
		xmlOptions.setSavePrettyPrint();
//		File file =  new File();
		File file;
		try {
			File tempFile = File.createTempFile("Aragon",fileName);
			log.debug("Guardando fichero temporal de "+fileName);
			xmlObject.save(tempFile, xmlOptions);
			file = new File(fileName);
			log.debug("Renombrando fichero");
			tempFile.renameTo(file);
			if(!tempFile.delete())tempFile.deleteOnExit();
		} catch (IOException e1) {
			log.error("Error al crear el fichero temporal",e1);
			throw new BusinessException("Error al crear el fichero temporal de justificaciones de Aragon");
		}  
//		file = new File(file, fileName);
//


		
		return file;

	}
	
	

	
	
	
}

