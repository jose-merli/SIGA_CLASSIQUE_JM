/*
 * Created on Apr 21, 2005
 * @author emilio.grau
 *
 * 
 */
package com.siga.consultas;

import java.io.Serializable;

/**
 * Criterio Dinámico de consulta
 */
public class CriterioDinamico implements Serializable {

	private String idC="";
	private String op="";
	private String val="";
	private String tc="";
	private String lg="";
	private String dc="";
	private String st="";
	private String hp="";
	
		
	
	
	public String getIdC() {
		return idC;
	}
	public void setIdC(String idC) {
		this.idC = idC;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc;
	}	
	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public String getLg() {
		return lg;
	}
	public void setLg(String lg) {
		this.lg = lg;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp= hp;
	}
}
