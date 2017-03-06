package com.siga.ws.i2003.je;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.redabogacia.sigaservices.app.AppConstants.PCAJG_ALC_ACT_NIVEL;
import org.redabogacia.sigaservices.app.AppConstants.PCAJG_ALC_ACT_TIPO_INCIDENCIA;
import org.redabogacia.sigaservices.app.autogen.model.PcajgAlcActIncidencia;
import org.redabogacia.sigaservices.app.autogen.model.PcajgAlcActIncidenciaExample;
import org.redabogacia.sigaservices.app.services.fac.PcajgAlcActIncidenciaService;

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

	private String idInstitucion;
	private String idFacturacion;
	private UsrBean usrBean;
	
	private Map<String, PcajgAlcActIncidencia> mapaIncidencias = new HashMap<String, PcajgAlcActIncidencia>();
	private List<String> listaColumnaNoImprimir = new ArrayList<String>();
		
	
	private void init() {
		
		PcajgAlcActIncidenciaService pcajgAlcActIncidenciaService = (PcajgAlcActIncidenciaService) BusinessManager.getInstance().getService(PcajgAlcActIncidenciaService.class);
		
		List<PcajgAlcActIncidencia> listaPcajgAlcActIncidencias = pcajgAlcActIncidenciaService.selectByExample(new PcajgAlcActIncidenciaExample());
		
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
			
			String rutaAlm = getDirectorioSalida(directorio, idInstitucion);			
			File file = new File(rutaAlm);
			file.delete();
			file.mkdirs();
			
			for (File f : file.listFiles()) {
				ClsLogging.writeFileLog("Fichero eliminado (" + f.delete() + ") " + f.getAbsolutePath(), 3);			
			}
			String nombreFichero = getNombreFichero(nombreSalida, idInstitucion, usrBean);
	
			file = new File(file, nombreFichero + ".txt");				
			boolean hayErrores = rellenaFichero(file);			
			ClsLogging.writeFileLog("Generando fichero txt en: " + file.getAbsolutePath(), 3);
	
			if (!hayErrores) {
				listaFicheros.add(file);
			} else {
				file.delete();
			}
			
			if (closeLogFile()) {
				listaFicheros.add(getFileInformeIncidencias(idInstitucion, idFacturacion));
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
	
	private boolean rellenaFichero(File file) throws Exception {
		
		boolean hayErrores = false;
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		
		String sql = "SELECT * FROM " + V_WS_JE_2003_DESIGNA + " WHERE " + IDINSTITUCION + " = " + idInstitucion + " AND " + IDFACTURACION + " = " + idFacturacion;
		
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
					hayErrores = hayErrores || valida(numEJG, anioEJG, desNumero, desAnio, numeroActuacion, columna, valor);
					
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
