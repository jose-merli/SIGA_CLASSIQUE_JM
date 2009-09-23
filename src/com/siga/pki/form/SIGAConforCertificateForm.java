/*
Class Name: PSSCCreateCAForm
Description:
Public methods:
	- perform.
Private methods:
Author: Enrique Arribas San Bruno
Creation date: 8/11/2002
Program Description:
*/

package com.siga.pki.form;

import java.util.Hashtable;

import org.apache.struts.action.ActionForm;

public class SIGAConforCertificateForm extends ActionForm {

  Hashtable reg = null;

  public Hashtable getData(){
	  return reg;
  }

  public void setData(Hashtable ht){
	  reg = ht;
  }
  
  /*
    public void setStatus(String status) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_STATUS,status);

    }
    public String getStatus() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_STATUS));
    }
    public void setContract(String contract) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CONTRACT,contract);

    }
    public String getContract() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CONTRACT));
    }
    public void setOriginator(String originator) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_ORIGINATOR,originator);

    }
    public String getOriginator() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_ORIGINATOR));
    }
    public void setEngine(String engine) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_ENG_BUILD_STND,engine);

    }
    public String getEngine() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_ENG_BUILD_STND));
    }
    public void setTest(String test) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_TEST_SPEC_NUMBER,test);

    }
    public String getTest() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_TEST_SPEC_NUMBER));
    }
    public void setAcceptance(String acceptance) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_TECH_ACCEPT_NUMBER,acceptance);

    }
    public String getAcceptance() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_TECH_ACCEPT_NUMBER));
    }
    public void setPacking(String packing) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_PACK_PROC_REF,packing);
    }
    public String getPacking() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_PACK_PROC_REF));
    }
    public void setWeight(String weight) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_WEIGHT,weight);

    }
    public String getWeight() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_WEIGHT));
    }
    public void setUnitweight(String unitweight) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_UNIT_OF_WEIGHT,unitweight);

    }
    public String getUnitweight() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_UNIT_OF_WEIGHT));
    }
    public void setCentregrav(String centregrav) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CENT_OF_GRAV,centregrav);

    }
    public String getCentregrav() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CENT_OF_GRAV));
    }
    public void setUnitgravity(String unitgravity) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_UNIT_CENT_OF_GRAV,unitgravity);

    }
    public String getUnitgravity() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_UNIT_CENT_OF_GRAV));
    }
    public void setRefgrav(String refgrav) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CENT_OF_GRAV_REF,refgrav);

    }
    public String getRefgrav() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CENT_OF_GRAV_REF));
    }
    public void setConfirmation(String confirmation) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CONFIRM_STAT,confirmation);

    }
    public String getConfirmation() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CONFIRM_STAT));
    }
    public void setDatecertif(String datecertif) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CERT_DATE,datecertif);

    }
    public String getDatecertif() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CERT_DATE));
    }
    public void setCompanycert(String companycert) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CERT_COMPANY,companycert);

    }
    public String getCompanycert() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CERT_COMPANY));
    }
    public void setAgentcert(String agentcert) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CERT_AGENT,agentcert);

    }
    public String getAgentcert() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_CERT_AGENT));
    }
    public void setNqar(String nqar) {
		if (reg == null)
		 reg = new Hashtable();
		 reg.put(ColumnConstants.SIGA_GENERAL_DESCRIPTION_AUTH_NQAR,nqar);

    }
    public String getNqar() {
		return ((reg == null) ? "" : (String) reg.get(ColumnConstants.SIGA_GENERAL_DESCRIPTION_AUTH_NQAR));
    }

*/
}