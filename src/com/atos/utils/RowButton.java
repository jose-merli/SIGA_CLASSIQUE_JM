package com.atos.utils;

public class RowButton {

	private static final String OFF = "_off.gif";
	private static final String ON = "_on.gif";
	private static final String DISABLED = "_disable.gif";
	private static final String PATH = "/SIGA/html/imagenes/b";
	
	private RowButtonsConstants button;
	private String icon;
	private String action;
	private String label;
	private String alt;
	private String width;
	private AccessConstants access = AccessConstants.ACCESS_NONE;
	private Estado estado = Estado.ON;
	
	public RowButton(RowButtonsConstants button){
		this.button = button;
		this.action = button.getAction();
		this.icon = button.getIcon();
		this.label = button.getLabel();
		this.alt = button.getAlt();
	}

	public RowButton(RowButtonsConstants button, Estado estado){
		this(button);
		this.estado = estado;
	}

	public RowButton(RowButtonsConstants button, AccessConstants access){
		this(button);
		this.access = access;
	}

	public RowButton(RowButtonsConstants button, String action, String icon, String label, String alt, String width){
		this(button);
		this.action = action;
		this.icon =icon;
		this.label = label;
		this.alt = alt;
		this.width = width;
	}

	public RowButton(RowButtonsConstants button, String width){
		this(button);
		this.width = width;
	}

	
	public RowButtonsConstants getButton() {
		return button;
	}
	public void setButton(RowButtonsConstants button) {
		this.button = button;
	}
	
    public String getAction() {
    	return action;
    }

    public String getIconOn(){
    	return PATH + icon + ON;
    }
    public String getIconOff(){
    	return PATH + icon + OFF;
    }
    public String getIconDisabled(){
    	return PATH + icon + DISABLED;
    }
    public String getIconVoid(){
    	return PATH + "Espacio" + DISABLED;
    }
    
    public String getLabel() {
		return label;
	}
    
	public String getAlt(){
		return alt;
	}
	
	public String getWidth() {
		return width;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setAccess(AccessConstants access) {
		this.access = access;
	}
	public AccessConstants getAccess() {
		return access;
	}
	
	
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Estado getEstado() {
		return estado;
	}


	public enum Estado {
		ON (),
		OFF (),
		DISABLED (),
		VOID ();
	}
	
}
