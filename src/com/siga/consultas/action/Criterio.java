/*
 * Created on Apr 12, 2005
 * @author emilio.grau
 *
 * 
 */
package com.siga.consultas.action;

import java.io.Serializable;

/**
 * 
 */
public class Criterio implements Serializable {

	private String tc="";
	private String idC="";
	private String op="";
	private String sepI="";
	private String val="";
	private String sepF="";
	private String idOp="";
	
	
	public String getIdC() {
		return idC;
	}
	public void setIdC(String idC) {
		this.idC = idC;
	}
	public String getIdOp() {
		return idOp;
	}
	public void setIdOp(String idOp) {
		this.idOp = idOp;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getSepF() {
		return sepF;
	}
	public void setSepF(String sepF) {
		this.sepF = sepF;
	}
	public String getSepI() {
		return sepI;
	}
	public void setSepI(String sepI) {
		this.sepI = sepI;
	}
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
}
