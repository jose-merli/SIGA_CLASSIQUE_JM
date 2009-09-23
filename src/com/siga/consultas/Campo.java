/*
 * Created on Apr 12, 2005
 * @author emilio.grau
 *
 * 
 */
package com.siga.consultas;

import java.io.Serializable;

/**
 * 
 */
public class Campo implements Serializable {

	private String tc="";
	private String idC="";
	private String cab="";
	
	
	
	public String getCab() {
		return cab;
	}
	public void setCab(String cab) {
		this.cab = cab;
	}
	public String getIdC() {
		return idC;
	}
	public void setIdC(String idC) {
		this.idC = idC;
	}
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc;
	}
}
