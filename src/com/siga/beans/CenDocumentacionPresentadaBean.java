/*
 * Created on 14-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 */
public class CenDocumentacionPresentadaBean extends MasterBean {
	/* Variables */
	private Long idSolicitud;
	private Integer idDocumentacion, idInstitucion;
	

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_DOCUMENTACIONPRESENTADA";

	/* Nombre campos de la tabla */
	static public final String C_IDDOCUMENTACION 		= "IDDOCUMENTACION";
	static public final String C_IDSOLICITUD	 		= "IDSOLICITUD";
	//static public final String C_IDINSTITUCION 		= "IDINSTITUCION";

	public Integer getIdDocumentacion() 	{	return idDocumentacion;	}
	public Long getIdSolicitud() 		{	return idSolicitud;		}
	//public Integer getIdInstitucion() {		return idInstitucion;	}

	public void setIdSolicitud(Long idSolicitud) 		{	this.idSolicitud = idSolicitud;	}
	public void setIdDocumentacion(Integer idDocumentacion){	this.idDocumentacion = idDocumentacion;	}
	//public void setIdInstitucion(Integer idInstitucion) {		this.idInstitucion = idInstitucion;	}
}
