package com.siga.beans;

import java.util.HashMap;
import java.util.Hashtable;

public class GenProcesoBean extends MasterBean{

	static public String T_NOMBRETABLA="GEN_PROCESOS";

	static public final String C_IDPROCESO 		= "IDPROCESO";
	static public final String C_IDMODULO 		= "IDMODULO";
	static public final String C_TRAZA 			= "TRAZA";
	static public final String C_TARGET 		= "TARGET";
	static public final String C_DESCRIPCION 	= "DESCRIPCION";
	static public final String C_TRANSACCION 	= "TRANSACCION";
	static public final String C_IDPARENT 		= "IDPARENT";
	static public final String C_NIVEL 			= "NIVEL";
	
	static public final HashMap<String, String> map = new HashMap();
	
	String idProceso, idModulo, traza, target, descripcion, transaccion, idParent, nivel;
	
	protected static String[] getClavesBean() {
		String[] claves = { C_IDPROCESO };
		return claves;
	}
	
	protected static String[] getCamposBean() {
		String[] campos = { C_IDPROCESO, C_IDMODULO, C_TRAZA, C_TARGET,C_DESCRIPCION, C_TRANSACCION, C_IDPARENT, C_NIVEL };
		return campos;
	}

	public String getIdProceso() {
		return map.get(C_IDPROCESO);
	}

	public void setIdProceso(String valor) {
		this.map.put(C_IDPROCESO, valor);
	}

	public String getIdModulo() {
		return map.get(C_IDMODULO);
	}

	public void setIdModulo(String valor) {
		this.map.put(C_IDMODULO, valor);
	}

	public String getTraza() {
		return map.get(C_TRAZA);
	}

	public void setTraza(String valor) {
		this.map.put(C_TRAZA, valor);
	}

	public String getTarget() {
		return map.get(C_TARGET);
	}

	public void setTarget(String valor) {
		this.map.put(C_TARGET, valor);
	}

	public String getDescripcion() {
		return map.get(C_DESCRIPCION);
	}

	public void setDescripcion(String valor) {
		this.map.put(C_DESCRIPCION, valor);
	}

	public String getTransaccion() {
		return map.get(C_TRANSACCION);
	}

	public void setTransaccion(String valor) {
		this.map.put(C_TRANSACCION, valor);
	}

	public String getIdParent() {
		return map.get(C_IDPARENT);
	}

	public void setIdParent(String valor) {
		this.map.put(C_IDPARENT, valor);
	}

	public String getNivel() {
		return map.get(C_NIVEL);
	}

	public void setNivel(String valor) {
		this.map.put(C_NIVEL, valor);
	}

	public HashMap getMap() {
		return this.map;
	}
	
	public void setMap(Hashtable hash) {
		map.putAll(hash);
	}
	
		
}
