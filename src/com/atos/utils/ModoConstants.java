package com.atos.utils;

import java.util.ArrayList;

public enum ModoConstants {

	CONSULTA ("EDICION","EDITAR","NUEVO"),
	EDICION  ("VER","CONSULTA");

	private ArrayList<String> valores = new ArrayList<String>();

	ModoConstants(String ... valores) {
		for(String valor: valores){
			this.valores.add(valor);
		}
	}

	public ArrayList<String> getValores(){
		return valores;
	}

	public boolean esConsulta(String valor){
		if(valor == null)
			return false;
		return valores.contains(valor.toUpperCase().trim());
	}

	public static ModoConstants getEnum(String value){
		if (value != null){
			for(ModoConstants mc : values()){
				if (mc.valores.contains(value.toUpperCase())){
					return mc;
				}
			}
		}
		return null;
	}

}
