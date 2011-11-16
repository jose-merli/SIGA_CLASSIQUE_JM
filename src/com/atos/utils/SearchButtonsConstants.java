package com.atos.utils;

import java.util.ArrayList;
import java.util.List;

public enum SearchButtonsConstants {

	EDITAR_SEL ("es","general.boton.editarSel","accionEditarSel()",0),
	BUSCAR ("b","general.search","buscar()",1),
	AVANZADA ("a","general.searchAvanzada","buscarAvanzada()",2),
	SIMPLE ("s","general.boton.searchSimple","buscarSimple()",3),
	NUEVO ("n","general.new","nuevo()",4),
	LIMPIAR ("l","general.boton.clear","limpiar()",5),
	BORRARLOG ("r","administracion.auditoria.borrarlog","borrarLog()",6),
	NUEVA_REGULARIZACION ("nr","general.boton.nuevaRegularizacion","nuevaRegularizacion()",7),
	NUEVA_SOCIEDAD ("ns","general.boton.nuevaSociedad","nuevaSociedad()",8),
	GENERAR_PDF ("p","general.boton.generarPDF","generarPDF()",9),
	GENERAR_PDFS ("gps","general.boton.generarPDFmostrados","generarPDFMostrados()",10),
	GENERA_EXCEL ("ge","general.boton.exportarExcel","generaExcel()",11),
	VALIDAR_VOLANTE ("vol","general.boton.validarVolante","validarVolante()",12),
	INFORME_JUSTIFICACION ("ij","general.boton.informeJustificacion","informeJustificacion()",13),
	GENERAR_CARTA ("c","gratuita.EJG.botonComunicaciones","generarCarta()",14),
	ANADIR_POB ("an","general.boton.anadirPoblaciones","anadirPob()",15),
	VOLVER ("v","general.boton.volver","accionVolver()",16),
	GENERAR_RECURSOS ("gr","general.boton.generarRecursos","generarRecursos()",17),
	LISTO_PARA_ENVIAR ("le","gratuita.busquedaEJG_CAJG.listoParaEnviar","accionListoParaEnviar()",18),
	ANIADIR_A_REMESA ("ar","general.boton.aniadirARemesa","aniadirARemesa(true)",19),
	DESCARGA_EJG ("dee","general.boton.descargaEejg","descargaEejg(true)",20),
	RENEGOCIAR ("rn","general.boton.renegociar","renegociar(true)",21),
	NUEVO_GRUPOS ("ng","general.boton.nuevoGrupos","nuevoGrupos()",22),
	CERRAR ("ce","general.boton.close","cerrar()",23);	
	
	
    private final String valor;   
	private final String label;   
    private final String accion;   
    private final int	 orden;
    
    SearchButtonsConstants(String valor, String label, String accion, int orden) {
        this.valor 	= valor;
        this.label 	= label;
        this.accion = accion;
        this.orden 	= orden;
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

	public int getOrden(){
		return orden;
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
