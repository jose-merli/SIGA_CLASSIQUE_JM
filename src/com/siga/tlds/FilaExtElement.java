/*
 * Created on 15-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.tlds;

/**
 * @author Carmen.Garcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FilaExtElement {
	protected String iconName=null;
	protected String note=null;
	protected String alt=null;
	protected String accion=null;
	protected String accesoMin=null;

	public FilaExtElement(String _iconName,  String _action, String _alt, String _accesoMin) {
		this(_iconName,  _action, _accesoMin);
		if (alt != null && !alt.equals("")) {
			alt=_alt;
		}
	}

	public FilaExtElement(String _iconName,  String _action, String _accesoMin) {
		iconName=_iconName;
		note=_action;
		accion=_action;
		accesoMin=_accesoMin;
		alt="general.boton." + iconName;
	}
	
	String getIconName() {
		return iconName;
	}
	
	String getNote() {
		return note;
	}
	
	String getAction() {
		return accion;
	}
	
	String getAccesoMin() {
		return accesoMin;
	}

	String getAlt() {
		return alt;
	}	

}
