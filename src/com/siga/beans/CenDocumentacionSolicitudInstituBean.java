/*
 * Created on 15-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenDocumentacionSolicitudInstituBean extends MasterBean {

	/* Variables */
	private Integer idInstitutcion, idTipoSolicitud, idTipoColegiacion, idDocumSoliInstitu, idModalidad;
	
	private CenDocumentacionSolicitudBean docSolicitud; // Tabla Cen_DocumentacionSolicitud

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_DOCUMENTSOLICITUDINSTITU";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDTIPOSOLICITUD	= "IDTIPOSOLICITUD";
	static public final String C_IDTIPOCOLEGIACION	= "IDTIPOCOLEGIACION";
	static public final String C_IDDOCUMSOLIINSTITU	= "IDDOCUMSOLIINSTITU";
	static public final String C_IDMODALIDAD		= "IDMODALIDAD";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)						{ this.idInstitutcion = id; }
	public void setIdTipoSolicitud (Integer id)						{ this.idTipoSolicitud = id; }
	public void setIdTipoColegiacion (Integer id)					{ this.idTipoColegiacion = id; }
	public void setIdDocumentacionSolicitudInstitucion (Integer id)	{ this.idDocumSoliInstitu = id; }
	public void setIdModalidad(Integer idModalidad) 				{ this.idModalidad = idModalidad; }
	
	// Metodos GET
	public Integer getIdInstitucion 						() 	{ return this.idInstitutcion; }	
	public Integer getIdTipoSolicitud 						() 	{ return this.idTipoSolicitud; }	
	public Integer getIdTipoColegiacion						() 	{ return this.idTipoColegiacion; }	
	public Integer getIdDocumentacionSolicitudInstitucion 	() 	{ return this.idDocumSoliInstitu; }	
	public Integer getIdModalidad							() 	{ return idModalidad; }

	
	public CenDocumentacionSolicitudBean getDocumentacionSolicitud () {return this.docSolicitud; }
	public void setDocumentacionSolicitud (CenDocumentacionSolicitudBean b) {this.docSolicitud = b; }
}
