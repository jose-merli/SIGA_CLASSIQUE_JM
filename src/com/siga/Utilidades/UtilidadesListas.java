package com.siga.Utilidades;

import java.util.List;

public class UtilidadesListas {

	
	public static List<String> add(List<String> list, String ... columns) {
		for (String column: columns){
			list.add(column);
		}
		return list;
	}
	
}
