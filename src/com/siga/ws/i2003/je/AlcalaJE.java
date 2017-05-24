package com.siga.ws.i2003.je;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.PCAJG_ALC_ACT_NIVEL;
import org.redabogacia.sigaservices.app.AppConstants.PCAJG_ALC_ACT_TIPO_INCIDENCIA;
import org.redabogacia.sigaservices.app.autogen.model.PcajgAlcActErrorCam;
import org.redabogacia.sigaservices.app.autogen.model.PcajgAlcActErrorCamExample;
import org.redabogacia.sigaservices.app.autogen.model.PcajgAlcActIncidencia;
import org.redabogacia.sigaservices.app.autogen.model.PcajgAlcActIncidenciaExample;
import org.redabogacia.sigaservices.app.autogen.model.VPcajgAlcActErrorCam;
import org.redabogacia.sigaservices.app.services.fac.PcajgAlcActService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.UtilidadesString;
import com.siga.ws.InformeXML;

import es.satec.businessManager.BusinessManager;

public class AlcalaJE extends InformeXML implements PCAJGConstantes {

	private static final String NOMBRE_INFORME_ERRORES_CAM = "InformeErroresCAM.xlsx";
	private String idInstitucion;
	private String idFacturacion;
	private UsrBean usrBean;
	
	private Map<String, PcajgAlcActIncidencia> mapaIncidencias = new HashMap<String, PcajgAlcActIncidencia>();
	private List<String> listaColumnaNoImprimir = new ArrayList<String>();
	private XSSFWorkbook workbook = null;
	
	
	public enum TIPO_FICHERO_CAM {
		TODOS,
		NINGUNO
	}
		
	
	private void init() {
		
		PcajgAlcActService pcajgAlcActService = (PcajgAlcActService) BusinessManager.getInstance().getService(PcajgAlcActService.class);
		
		List<PcajgAlcActIncidencia> listaPcajgAlcActIncidencias = pcajgAlcActService.selectByExample(new PcajgAlcActIncidenciaExample());
		
		if (listaPcajgAlcActIncidencias != null) {
			for (PcajgAlcActIncidencia pcajgAlcActIncidencia : listaPcajgAlcActIncidencias) {
				if (PCAJG_ALC_ACT_TIPO_INCIDENCIA.NO_IMPRIMIR.getId() == pcajgAlcActIncidencia.getIdtipoinc()) {
					this.listaColumnaNoImprimir.add(pcajgAlcActIncidencia.getCampo());
				} else {
					this.mapaIncidencias.put(pcajgAlcActIncidencia.getCampo(), pcajgAlcActIncidencia);
				}
			}
		}
		
	}
	
	@Override
	public void envioWS(String idInstitucion, String idFacturacion) {		
		throw new IllegalArgumentException("Funcionalidad no implementada.");
		
	}
	
	@Override
	public List<File> execute(String directorio, String nombreSalida, String idInstitucion, String idFacturacion, UsrBean usrBean) throws Exception {
		this.idInstitucion = idInstitucion;
		this.idFacturacion = idFacturacion;
		this.usrBean = usrBean;
		
		List<File> listaFicheros = new ArrayList<File>();
		
		try {
			init();	
			setCabeceraLog("AÑO EJG;NÚMERO EJG;AÑO DESIGNACIÓN;NÚMERO DESIGNACIÓN;NÚMERO ACTUACIÓN;TIPO INCIDENCIA;INCIDENCIA");
			
			String rutaAlm = getDirectorioSalida(directorio, idInstitucion, idFacturacion);			
			File parentFile = new File(rutaAlm);
			parentFile.delete();
			parentFile.mkdirs();
			
			for (File f : parentFile.listFiles()) {
				ClsLogging.writeFileLog("Fichero eliminado (" + f.delete() + ") " + f.getAbsolutePath(), 3);			
			}
			String nombreFichero = getNombreFichero(nombreSalida, idInstitucion, usrBean);
	
			File file = new File(parentFile, nombreFichero + ".txt");				
			boolean hayErrores = rellenaFichero(file);			
			ClsLogging.writeFileLog("Generando fichero txt en: " + file.getAbsolutePath(), 3);
			
			if (getTipoFicheroCAM() != null && !getTipoFicheroCAM().trim().equals("") && !getTipoFicheroCAM().trim().equals(TIPO_FICHERO_CAM.NINGUNO.name())) {
//				actualizar todos o el tipo seleccionado
				PcajgAlcActService pcajgAlcActService = (PcajgAlcActService) BusinessManager.getInstance().getService(PcajgAlcActService.class);
				PcajgAlcActErrorCam pcajgAlcActErrorCam = new PcajgAlcActErrorCam();
				pcajgAlcActErrorCam.setBorrado((short)1);//borrado
				pcajgAlcActErrorCam.setUsumodificacion(AppConstants.USUMODIFICACIONAUTO);
				pcajgAlcActErrorCam.setFechamodificacion(new Date());
				
				PcajgAlcActErrorCamExample pcajgAlcActErrorCamExample = new PcajgAlcActErrorCamExample();
				PcajgAlcActErrorCamExample.Criteria criteria = pcajgAlcActErrorCamExample.createCriteria().andIdinstitucionEqualTo(Short.parseShort(this.idInstitucion)).andIdfacturacionEqualTo(Integer.parseInt(this.idFacturacion));
				if (!getTipoFicheroCAM().trim().equals(TIPO_FICHERO_CAM.TODOS.name())) {
					criteria.andCodigoErrorEqualTo(getTipoFicheroCAM());
				}
				
				pcajgAlcActService.updatePcajgAlcActErrorCamByExampleSelective(pcajgAlcActErrorCam, pcajgAlcActErrorCamExample);
			}
	
			if (!hayErrores && (getTipoFicheroCAM() == null || getTipoFicheroCAM().trim().equals("") || !getTipoFicheroCAM().trim().equals(TIPO_FICHERO_CAM.NINGUNO.name()))) {
				listaFicheros.add(file);
			} else {
				file.delete();
			}
			
			if (closeLogFile()) {
				listaFicheros.add(getFileInformeIncidencias(idInstitucion, idFacturacion));
			}
			
			crearInformeResumenErroresCAM(parentFile);
			File fileErroresCAM = crearInformeErroresCAM(parentFile);
			if (fileErroresCAM != null && fileErroresCAM.exists()) {
				listaFicheros.add(fileErroresCAM);
			}			

			return listaFicheros;
			
		} finally {
			closeLogFile();
		}
	}
	
	
	
	
	

	private boolean imprimirColumna(String columna) {
		for (String s : this.listaColumnaNoImprimir) {
			if (s.equalsIgnoreCase(columna)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean valida(String numEJG, String anioEJG, String desNumero,	String desAnio, String numeroActuacion, String columna, String valor) throws ClsExceptions, IOException {
		
		boolean hayError = false;
		PcajgAlcActIncidencia pcajgAlcActIncidencia = mapaIncidencias.get(columna);
		
		if (pcajgAlcActIncidencia != null) {
			boolean muestraMensaje = false;
			if (pcajgAlcActIncidencia.getIdtipoinc() == PCAJG_ALC_ACT_TIPO_INCIDENCIA.OBLIGATORIO.getId() && (valor == null || valor.trim().equals(""))) {
				muestraMensaje = true;
			} else if (pcajgAlcActIncidencia.getIdtipoinc() == PCAJG_ALC_ACT_TIPO_INCIDENCIA.BOOLEANO.getId() && "1".equals(valor)) {
				muestraMensaje = true;
			}
			
			if (muestraMensaje) {
				String texto = getTexto(anioEJG, numEJG, desAnio, desNumero, numeroActuacion, pcajgAlcActIncidencia.getNivel(), pcajgAlcActIncidencia.getMensaje());
				escribeLog(idInstitucion, idFacturacion, usrBean, texto);
				hayError = PCAJG_ALC_ACT_NIVEL.ERROR.name().equals(pcajgAlcActIncidencia.getNivel());
			}
		}
		
		return hayError;
	}
	
	private String getTexto(String anioEJG, String numEJG, String desAnio, String desNumero, String numeroActuacion, String tipoError, String texto) {		
		return anioEJG + ";" + numEJG + ";" + desAnio + ";" + desNumero + ";" + numeroActuacion + ";" + tipoError + ";" + texto;
	}
	
	
	private File crearInformeResumenErroresCAM(File parentFile) throws Exception {		
		String sql = "SELECT * FROM " + VPcajgAlcActErrorCam.T_V_PCAJG_ALC_ACT_ERROR_CAM + " WHERE " + VPcajgAlcActErrorCam.C_IDINSTITUCION + " = " + idInstitucion + " AND " + VPcajgAlcActErrorCam.C_IDFACTURACION + " = " + idFacturacion;				
		return crearPestana(parentFile, sql, "Resumen");
	}
	
	private File crearPestana(File parentFile, String sql, String sheetName) throws Exception {
		File file  = null;
		RowsContainer rc = new RowsContainer();
		if (rc.find(sql)) {		
			if (workbook == null) {								
				workbook = new XSSFWorkbook();
			}
			
			XSSFSheet sheet = workbook.createSheet(sheetName);
			
			int rowNumber = 0;
			int cellNumber = 0;
			
			List<String> tableHeaders = new ArrayList<String>();
			
			String[] columnas = rc.getFieldNames();
			
			//cabecera
			if (columnas != null && columnas.length > 0) {
				org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNumber++);
				cellNumber = 0;
				
				XSSFCellStyle styleHeader = workbook.createCellStyle();
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				//font.setColor(HSSFColor.WHITE.index);
				styleHeader.setFont(font);
				XSSFColor color = new XSSFColor(new Color(149,175,207));
				styleHeader.setFillForegroundColor(color);
				styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
				Cell cell = null; 
						
				for (String columna : columnas) {
					if (imprimirColumna(columna)) {
						cell = row.createCell(cellNumber++);
						cell.setCellValue(columna);
						cell.setCellStyle(styleHeader);
						sheet.autoSizeColumn(cellNumber-1);
						tableHeaders.add(columna);						
					}
					
				}
				
				sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, cell.getColumnIndex()));
				sheet.createFreezePane(0,1);
				
			}
			
			XSSFCellStyle style1 = workbook.createCellStyle();
			XSSFColor color = new XSSFColor(new Color(234,232,240));
			style1.setFillForegroundColor(color);
			style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
			
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);
				org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNumber++);
				cellNumber = 0;
				for (String columna : columnas) {
					String valor = fila.getString(columna);					
					if (imprimirColumna(columna)) {						
						Cell cell = row.createCell(cellNumber++);
						cell.setCellValue(valor);
						if (i%2 == 1) {
							cell.setCellStyle(style1);
						}
					}
				}
			}
			
			
			file = new File(parentFile, NOMBRE_INFORME_ERRORES_CAM);
		    FileOutputStream fileOut = new FileOutputStream(file);
		    workbook.write(fileOut);
		    fileOut.close();			
			
		}
		
		return file;
	}

	

	private File crearInformeErroresCAM(File parentFile) throws Exception {
		
		String sql = "SELECT E.IDINSTITUCION, E.IDFACTURACION, E.REGISTRO_ERROR_CAM, E.CODIGO_ERROR, TE.ERROR_DESCRIPCION, E.CODIGO_CAMPO_ERROR" +
						", TC.CAMPO_DESCRIPCION, E.OBSERVACIONES_ERROR, E.EJG_ANIO AS ANIO_EJG, E.EJG_NUMEJG AS NUMBERO_EJG, DECODE(E.BORRADO, 1, 'SI', 'NO') AS HISTORICO" +
				" FROM PCAJG_ALC_ACT_ERROR_CAM E, PCAJG_ALC_TIPOERRORINTERCAMBIO TE, PCAJG_ALC_TIPOCAMPOCARGA TC" +
				" WHERE E.CODIGO_ERROR = TE.ERROR_CODIGO(+)" +
				" AND E.CODIGO_CAMPO_ERROR = TC.CAMPO_CODIGO(+)" +
				" AND E.IDINSTITUCION = " + idInstitucion + 
				" AND E.IDFACTURACION = " + idFacturacion +
				" ORDER BY E.IDENTIFICADOR";
		
		return crearPestana(parentFile, sql, "Errores CAM");
	}
	
	private boolean rellenaFichero(File file) throws Exception {
		
		boolean hayErrores = false;
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		
		String sql = "SELECT V.* FROM " + V_WS_JE_2003_DESIGNA + " V WHERE V." + IDINSTITUCION + " = " + idInstitucion + " AND V." + IDFACTURACION + " = " + idFacturacion;
		
		if (getTipoFicheroCAM() != null && !getTipoFicheroCAM().trim().equals("")) {
			if (getTipoFicheroCAM().trim().equals("024")) {//este tipo es pq no tenía ejg asociado así que no podemos filtrar por ejg
				sql += " AND V." + EJG_ANIO + " IS NULL";
			} else if (!getTipoFicheroCAM().trim().equals(TIPO_FICHERO_CAM.TODOS.name()) && !getTipoFicheroCAM().trim().equals(TIPO_FICHERO_CAM.NINGUNO.name())) {
				sql += " AND (V." + EJG_ANIO + " || LPAD(V." + EJG_NUMERO + ", 8, '0')) IN " +
					"(SELECT E.EJG_ANIO || LPAD(E.EJG_NUMEJG, 8, '0')" +
					" FROM PCAJG_ALC_ACT_ERROR_CAM E" +
					" WHERE E.IDINSTITUCION = V." + IDINSTITUCION +
					" AND E.IDFACTURACION = V." + IDFACTURACION +
					" AND E.BORRADO = 0" +
					" AND E.CODIGO_ERROR = '" + getTipoFicheroCAM() + "')";
			}
		}
		
		RowsContainer rc = new RowsContainer(); 
		if (rc.find(sql)) {
			String[] columnas = rc.getFieldNames();
			String anioEJG, numEJG, desAnio, desNumero, numeroActuacion = "";
			
			GestorContadores gcRemesa = new GestorContadores(this.usrBean);
			
			Hashtable contadorHash = gcRemesa.getContador(Integer.valueOf(this.idInstitucion), "INTERCAMBIOICM");			
			String anioSufijo = (String) contadorHash.get("SUFIJO");
			Long idIntercambio = (Long) contadorHash.get("CONTADOR");			
			int longitudContador = (Integer) contadorHash.get("LONGITUDCONTADOR");
			
			for (int i = 0; i < rc.size(); i++)	{				
				anioEJG = "";
				numEJG = "";
				desAnio = "";
				desNumero = "";
				numeroActuacion = "";
				Row fila = (Row) rc.get(i);
				for (String columna : columnas) {
					
					String valor = fila.getString(columna);
					
					if (EJG_NUMERO.equalsIgnoreCase(columna)) {
						numEJG = valor;
					} else if (EJG_ANIO.equalsIgnoreCase(columna)){
						anioEJG = valor;
					} else if (DESIGNA_CODIGO.equalsIgnoreCase(columna)){
						desNumero = valor;
					} else if (DESIGNA_ANIO.equalsIgnoreCase(columna)){
						desAnio = valor;
					} else if (NUMERO_ACTUACION.equalsIgnoreCase(columna)) {
						numeroActuacion = valor;
					}
						
					//aunque esté mal los datos y no cumplan los campos obligatorios debemos luego imprimirloos qp se generan los dos ficheros
					hayErrores = valida(numEJG, anioEJG, desNumero, desAnio, numeroActuacion, columna, valor) || hayErrores;
					
					if (imprimirColumna(columna)) {						
						if ("$$".equals(valor)) {
							bw.newLine();
						} else if (CAB2_NUMERO_INTERCAMBIO.equalsIgnoreCase(columna)) {							
							bw.write(UtilidadesString.formatea(++idIntercambio, longitudContador, true) + anioSufijo);							
						} else {
							bw.write(valor);
						}
					}
				}
				if (i < rc.size()-1) {
					bw.newLine();
				}
			}
			
			gcRemesa.setContador(contadorHash, UtilidadesString.formatea(idIntercambio, longitudContador, true));
		} else {
			escribeLog(idInstitucion, idFacturacion, usrBean, "No se ha recuperado ningún registro. Revise que todas las designaciones están asociadas a un EJG.");
		}
		bw.flush();
		bw.close();
		fw.close();
		
		return hayErrores;
	}
	
}
