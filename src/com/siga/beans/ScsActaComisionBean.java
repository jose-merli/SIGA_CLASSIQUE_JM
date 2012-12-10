package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla CAJG_REMESA
 * 
 * @author jose.barrientos
 * @version 21-10-2011
 */

public class ScsActaComisionBean extends MasterBean{

	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "SCS_ACTACOMISION";

	/* Variables */ 
	private	Integer	idActa;
	private	Integer	idInstitucion;
	private	Integer	idPresidente;
	private	Integer	idSecretario;
	private	Integer	anioActa;
	private	String	numeroActa;
	private	String	fechaResolucionCAJG;
	private	String	fechaReunion;
	private	String	horaInicioReunion;
	private	String	horaFinReunion;
	private	String	observaciones;
	private	String	miembrosComision;
	private	String	pendientes;

	/* Nombre de campos de la tabla*/
	static public final String C_IDACTA = "IDACTA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_NUMEROACTA = "NUMEROACTA";
	static public final String C_ANIOACTA = "ANIOACTA";
	static public final String C_FECHAREUNION = "FECHAREUNION";
	static public final String C_FECHARESOLUCION = "FECHARESOLUCION";
	static public final String C_HORAINICIOREUNION = "HORAINICIOREUNION";
	static public final String C_HORAFINREUNION = "HORAFINREUNION";
	static public final String C_IDPRESIDENTE = "IDPRESIDENTE";
	static public final String C_IDSECRETARIO = "IDSECRETARIO";
	static public final String C_MIEMBROSCOMISION = "MIEMBROSCOMISION";
	static public final String C_OBSERVACIONES = "OBSERVACIONES";
	static public final String C_PENDIENTES = "PENDIENTES";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";

	/* Getters */
	public Integer getIdActa () {return idActa;}
	public Integer getIdInstitucion () {return idInstitucion;}
	public Integer getIdPresidente () {return idPresidente;}
	public Integer getIdSecretario () {return idSecretario;}
	public Integer getAnioActa () {return anioActa;}
	public String getNumeroActa () {return numeroActa;}
	public String getFechaResolucionCAJG () {return fechaResolucionCAJG;}
	public String getFechaReunion () {return fechaReunion;}
	public String getHoraInicioReunion () {return horaInicioReunion;}
	public String getHoraFinReunion () {return horaFinReunion;}
	public String getObservaciones () {return observaciones;}
	public String getPendientes () {return pendientes;}
	public String getMiembrosComision () {return miembrosComision;}

	/* Setters */
	public void setIdActa (Integer idActa) {this.idActa = idActa;}
	public void setIdInstitucion (Integer idInstitucion) {this.idInstitucion = idInstitucion;}
	public void setIdPresidente (Integer idPresidente) {this.idPresidente = idPresidente;}
	public void setIdSecretario (Integer idSecretario) {this.idSecretario = idSecretario;}
	public void setAnioActa (Integer anioActa) {this.anioActa = anioActa;}
	public void setNumeroActa (String numeroActa) {this.numeroActa = numeroActa;}
	public void setFechaResolucionCAJG (String fechaResolucionCAJG) {this.fechaResolucionCAJG = fechaResolucionCAJG;}
	public void setFechaReunion (String fechaReunion) {this.fechaReunion = fechaReunion;}
	public void setHoraInicioReunion (String horaInicioReunion) {this.horaInicioReunion = horaInicioReunion;}
	public void setHoraFinReunion (String horaFinReunion) {this.horaFinReunion = horaFinReunion;}
	public void setObservaciones (String observaciones) {this.observaciones = observaciones;}
	public void setPendientes (String pendientes) {this.pendientes = pendientes;}
	public void setMiembrosComision (String miembrosComision) {this.miembrosComision = miembrosComision;}

}