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

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.UtilidadesString;
import com.siga.ws.InformeXML;
import com.siga.ws.i2064.je.error.ErrorNegocioWS;
import com.siga.ws.i2064.je.error.ErrorValidacionXML;

public class AlcalaJE extends InformeXML implements PCAJGConstantes {

	private String idInstitucion;
	private String idFacturacion;
	private UsrBean usrBean;
	
	private Map<String, String> obligatorios = new HashMap<String, String>();
	private List<String> listaColumnaNoImprimir = new ArrayList<String>();
		
	
	private void init() {
		this.listaColumnaNoImprimir.add(IDINSTITUCION);
		this.listaColumnaNoImprimir.add(IDFACTURACION);
		this.listaColumnaNoImprimir.add(IS_ENVIADO_ENREMESA);
		this.listaColumnaNoImprimir.add(IS_CAMBIO_JUZGADO);
		this.listaColumnaNoImprimir.add(IS_CAMBIO_PROCEDIMIENTO);
		this.listaColumnaNoImprimir.add(EJG_ANIO);
		this.listaColumnaNoImprimir.add(EJG_NUMERO);
		this.listaColumnaNoImprimir.add(DESIGNA_ANIO);
		this.listaColumnaNoImprimir.add(DESIGNA_CODIGO);
		this.listaColumnaNoImprimir.add(NUMERO_ACTUACION);
		
		
		this.obligatorios.put(AXP1_TIPO_ACTUALIZACION, "Debe rellenar el tipo de actuliazación");
		this.obligatorios.put(AXP2_NUM_EXPEDIENTE, "La designación debe estar asociada a un expediente enviado a la comisión");
		this.obligatorios.put(IS_ENVIADO_ENREMESA, "El expediente no ha sido enviado a la comisión en una remesa");
		this.obligatorios.put(PRC1_ORGANO_JUDICIAL, "Debe rellenar el código del órgano judicial de la designación o del EJG");
		this.obligatorios.put(PRC2_TIPO_PROCED_JUDICIAL, "Debe rellenar el código del procedimiento de la designación o del EJG");
		this.obligatorios.put(PRC3_NUM_PROCEDIMIENTO, "Debe rellenar correctamente el número de procedimiento");
		this.obligatorios.put(PRC4_ANIO_PROCEDIMIENTO, "Debe rellenar correctamente el año del procedimiento");
		this.obligatorios.put(PRC6_ESTADO_PROCEDIMIENTO, "Debe rellenar el campo en calidad de");		
		this.obligatorios.put(DPA1_ABOGADO_PROCURADOR, "Debe indicar si se trata de un abogado o un procurador");
		this.obligatorios.put(DPA2_COLEGIO_PROFESIONAL, "Debe rellenar el código del colegio de abogados");
		this.obligatorios.put(DPA4_FECHA_DESIGNA, "Debe rellenar la fecha de la designación del abogado");
		this.obligatorios.put(DPA5_NUMERO_DESIGNA, "Debe rellenar el número de la designación");
		this.obligatorios.put(DPA6_ANIO_DESIGNA, "Debe rellenar el año de la designación");
		this.obligatorios.put(DAC1_TIPO_ACTUACION_SUPLEM, "Debe indicar el tipo de actuación o suplemento");
		this.obligatorios.put(DAC2_FECHA_ACTUACION_SUPLEM, "Debe indicar la fecha de actuación o suplemento");
	}
	
	@Override
	public void envioWS(String idInstitucion, String idFacturacion) {		
		throw new IllegalArgumentException("Funcionalidad no implementada.");
		
	}
	
	@Override
	public File execute(String directorio, String nombreSalida, String idInstitucion, String idFacturacion, UsrBean usrBean) throws Exception {
		this.idInstitucion = idInstitucion;
		this.idFacturacion = idFacturacion;
		this.usrBean = usrBean;
		
		try {
			init();	
			setCabeceraLog("AÑO EJG;NÚMERO EJG;AÑO DESIGNACIÓN;NÚMERO DESIGNACIÓN;NÚMERO ACTUACIÓN;INCIDENCIA");
			
			String rutaAlm = getDirectorioSalida(directorio, idInstitucion);			
			File file = new File(rutaAlm);
			file.delete();
			file.mkdirs();
			
			for (File f : file.listFiles()) {
				ClsLogging.writeFileLog("Fichero eliminado (" + f.delete() + ") " + f.getAbsolutePath(), 3);			
			}
			String nombreFichero = getNombreFichero(nombreSalida, idInstitucion, usrBean);
	
			file = new File(file, nombreFichero + ".txt");				
			rellenaFichero(file);			
			ClsLogging.writeFileLog("Generando fichero txt en: " + file.getAbsolutePath(), 3);
	
			if (closeLogFile()) {
				return getFileInformeIncidencias(idInstitucion, idFacturacion);
			} else {
				return file;
			}
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
		if ((valor == null || valor.trim().equals("")) && obligatorios.containsKey(columna)) {
			String texto = getTexto(anioEJG, numEJG, desAnio, desNumero, numeroActuacion, obligatorios.get(columna));
			escribeLog(idInstitucion, idFacturacion, usrBean, texto);
			return false;
		}
		
		if (IS_CAMBIO_JUZGADO.equalsIgnoreCase(columna) && "1".equals(valor)) {
			String texto = getTexto(anioEJG, numEJG, desAnio, desNumero, numeroActuacion, "El juzgado de la actuación es diferente al juzgado de la designación. Debe indicar el motivo del cambio en la actuación.");
			escribeLog(idInstitucion, idFacturacion, usrBean, texto);
			return false;
		} else if (IS_CAMBIO_PROCEDIMIENTO.equalsIgnoreCase(columna) && "1".equals(valor)) {
			String texto = getTexto(anioEJG, numEJG, desAnio, desNumero, numeroActuacion, "El procedimiento de la actuación es diferente al procedimiento de la designación. Debe indicar el motivo del cambio en la actuación.");
			escribeLog(idInstitucion, idFacturacion, usrBean, texto);
			return false;
		}
		return true;
	}
	
	private String getTexto(String anioEJG, String numEJG, String desAnio, String desNumero, String numeroActuacion, String texto) {		
		return anioEJG + ";" + numEJG + ";" + desAnio + ";" + desNumero + ";" + numeroActuacion + ";" + texto;
	}
	
	private void rellenaFichero(File file) throws Exception {
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
						
					if (valida(numEJG, anioEJG, desNumero, desAnio, numeroActuacion, columna, valor) && imprimirColumna(columna)) {						
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
	}
	
}
