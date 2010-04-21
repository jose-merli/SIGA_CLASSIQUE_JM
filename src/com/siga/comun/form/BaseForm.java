package com.siga.comun.form;

import java.util.ArrayList;
import java.util.List;

import com.atos.utils.ActionButtonsConstants;
import com.atos.utils.SearchButtonsConstants;
import com.siga.comun.vos.Vo;

public abstract class BaseForm extends AuxForm{
	
	private static final long serialVersionUID = 3366440277081702221L;

	private List<SearchButtonsConstants> botonesBusqueda = new ArrayList<SearchButtonsConstants>();;
	private List<ActionButtonsConstants> botonesAccion = new ArrayList<ActionButtonsConstants>();;
	

	private String volver;
	private String deleteForm;
	private String backupForm;
	private String actionModal;

	public String getVolver(){
		return volver;
	}
	public void setVolver(String volver){
		this.volver = volver;
	}

	


	
	public abstract Vo toVO();

	public abstract void fromVO(Vo vo);

	public List<SearchButtonsConstants> getBotonesBusqueda() {
		return botonesBusqueda;
	}
	
	public void setBotonesBusqueda(ArrayList<SearchButtonsConstants> botonesBusqueda) {
		this.botonesBusqueda = botonesBusqueda;
	}

	public void setBotonesBusqueda(SearchButtonsConstants ... botones) {
		for(SearchButtonsConstants bc : botones){
			this.botonesBusqueda.add(bc);
		}
	}
	
	public List<ActionButtonsConstants> getBotonesAccion() {
		return botonesAccion;
	}
	
	public void setBotonesAccion(ArrayList<ActionButtonsConstants> botonesAccion) {
		this.botonesAccion = botonesAccion;
	}
	
	public void setBotonesAccion(ActionButtonsConstants ... botones) {
		for(ActionButtonsConstants bc : botones){
			this.botonesAccion.add(bc);
		}
	}
	
	public void setDeleteForm(String deleteForm) {
		this.deleteForm = deleteForm;
	}
	public String getDeleteForm() {
		return deleteForm;
	}
	
	public void setBackupForm(String backupForm) {
		this.backupForm = backupForm;
	}
	public String getBackupForm() {
		return backupForm;
	}
	public void setActionModal(String actionModal) {
		this.actionModal = actionModal;
	}
	public String getActionModal() {
		return actionModal;
	}
	
}