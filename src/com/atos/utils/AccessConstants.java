package com.atos.utils;

public enum AccessConstants {

	ACCESS_NONE (0,"Sin acceso"), 
	ACCESS_DENY (1,"Acceso Denegado"),
	ACCESS_READ	(2,"Sólo Lectura"),
	ACCESS_FULL (3,"Acceso Total"),
	ACCESS_SIGAENPRODUCCION (40,"Acceso SIGA en produccion");

	private final int priority;
	private final String label;   
    
	AccessConstants(int priority, String label) {
        this.priority = priority;
        this.label = label;
    }

	public String getLabel() {
		return label;
	}

	public int getPriority() {
		return priority;
	}
	
	public static AccessConstants getEnum(String value){
		for(AccessConstants sc : values()){
			if (sc.getLabel().equalsIgnoreCase(value)){
				return sc;
			}
		}
		return null;
	}
}
