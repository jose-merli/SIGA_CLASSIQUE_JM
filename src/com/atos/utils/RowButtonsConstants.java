package com.atos.utils;


public enum RowButtonsConstants {
	

	ESPACIO ("","","Espacio",""),
	CONSULTAR ("consultar","consultar","consultar","consultar"),
	EDITAR ("editar","editar","editar","editar"),
	BORRAR ("borrar","borrar","borrar","borrar"),
	ENVIAR ("enviar","enviar","enviar","enviar"),
	COMUNICAR ("comunicar","comunicar","comunicar","comunicar"),
	LOPD ("lopd","lopd","lopd","lopd"),
	INFORMACIONLETRADO ("informacionLetrado","informacionLetrado","informacionLetrado","informacionLetrado");
	
	private static final String PREFIX_LABEL = "general.boton.";
	private static final String PREFIX_ALT = "general.boton.";

	private final String action;
	private final String label;
	private final String icon;
	private final String alt;
    
	
    RowButtonsConstants(String action, String icon, String label, String alt) {
    	this.action = action;
    	this.icon = icon;
        this.label = label;
        this.alt = alt;
    }

    public String getAction() {
    	return action;
    }

    public String getIcon() {
    	return icon;
    }

    public String getLabel() {
		return PREFIX_LABEL + label;
	}
	
	public String getAlt(){
		return PREFIX_ALT + alt;
	}
	
}
