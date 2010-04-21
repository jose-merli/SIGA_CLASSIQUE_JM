package com.atos.utils;

import java.util.ArrayList;
import java.util.List;

public enum SearchButtonsConstants {

	BUSCAR ("b","general.search","buscar()"),
	AVANZADA ("a","general.searchAvanzada","buscarAvanzada()"),
	SIMPLE ("s","general.boton.searchSimple","buscarSimple()"),
	NUEVO ("n","general.new","nuevo()"),
	LIMPIAR ("l","general.boton.clear","limpiar()"),
	BORRARLOG ("r","administracion.auditoria.borrarlog","borrarLog()"),
	NUEVA_REGULARIZACION ("nr","general.boton.nuevaRegularizacion","nuevaRegularizacion()"),
	GENERAR_PDF ("p","general.boton.generarPDF","generarPDF()"),
	GENERAR_PDFS ("gps","general.boton.generarPDFmostrados","generarPDFMostrados()"),
	ANADIR_POB ("an","general.boton.anadirPoblaciones","anadirPob()"),
	GENERAR_CARTA ("c","gratuita.EJG.botonComunicaciones","generarCarta()"),
	NUEVA_SOCIEDAD ("ns","general.boton.nuevaSociedad","nuevaSociedad()"),
	GENERAR_RECURSOS ("gr","general.boton.generarRecursos","generarRecursos()"),
	VALIDAR_VOLANTE ("vol","general.boton.validarVolante","validarVolante()"),
	INFORME_JUSTIFICACION ("ij","general.boton.informeJustificacion","informeJustificacion()"),
	LISTO_PARA_ENVIAR ("le","gratuita.busquedaEJG_CAJG.listoParaEnviar","accionListoParaEnviar()"),
	ANIADIR_A_REMESA ("ar","general.boton.aniadirARemesa","aniadirARemesa(true)"),
	VOLVER ("v","general.boton.volver","accionVolver()"),
	DESCARGA_EJG ("de","general.boton.descargaEejg","descargaEejg(true)"),
	GENERA_EXCEL ("ge","general.boton.exportarExcel","generaExcel()");
	
    private final String valor;   
	private final String label;   
    private final String accion;   
    
    SearchButtonsConstants(String valor, String label, String accion) {
        this.valor = valor;
        this.label = label;
        this.accion = accion;
    }

    public String getValor() {
		return valor;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getAccion() {
		return accion;
	}

	public static SearchButtonsConstants getEnum(String value){
		for(SearchButtonsConstants bc : values()){
			if (bc.getValor().equalsIgnoreCase(value)){
				return bc;
			}
		}
		return null;
	}
	
	public static List<SearchButtonsConstants> getEnums(String value, String separator){
		List<SearchButtonsConstants> list = new ArrayList<SearchButtonsConstants>();
		String [] datos = value.replaceAll(" ","").split(separator);
		for (String tipo : datos) {
			list.add(SearchButtonsConstants.getEnum(tipo));
		}
		return list;
	}
	
}
