/*
 * Created on Dec 27, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpEstadosBean extends MasterBean {
    
	//Variables
	private Integer idEstado;
	private String nombre;
	private String ejecucionSancion;
	private String automatico;	
	private String descripcion;	
	private Integer idFase;
	private Integer idInstitucion;
	private Integer idTipoExpediente;
	private Integer idFaseSiguiente;
	private Integer idEstadoSiguiente;
	private String mensaje;
	private String preSancionado="";
	private String preVisible;
	private String preVisibleFicha;
	private String postActPrescritas;
	private String postSancionPrescrita;
	private String postSancionFinalizada;
	private String postAnotCanceladas;
	private String postVisible;
	private String estadoFinal;
	private String postVisibleFicha;

	private Integer diasAntelacion;
	private String activarAlertas;

	
	// Nombre campos de la tabla 
	static public final String C_IDESTADO = "IDESTADO";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_ESEJECUCIONSANCION = "ESEJECUCIONSANCION";
	static public final String C_ESAUTOMATICO = "ESAUTOMATICO";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_IDFASE = "IDFASE";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE="IDTIPOEXPEDIENTE";
	static public final String C_IDFASESIGUIENTE = "IDFASE_SIGUIENTE";
	static public final String C_IDESTADOSIGUIENTE = "IDESTADO_SIGUIENTE";
	static public final String C_MENSAJE = "MENSAJE";
	static public final String C_PRESANCIONADO = "PRE_SANCIONADO";
	static public final String C_PREVISIBLE = "PRE_VISIBLE";
	static public final String C_PREVISIBLEFICHA = "PRE_VISIBLEENFICHA";
	static public final String C_POSTACTUACIONESPRESCRITAS = "POST_ACTPRESCRITAS";
	static public final String C_POSTSANCIONPRESCRITA = "POST_SANCIONPRESCRITA";
	static public final String C_POSTSANCIONFINALIZADA = "POST_SANCIONFINALIZADA";
	static public final String C_POSTANOTACIONESCANCELADAS = "POST_ANOTCANCELADAS";
	static public final String C_POSTVISIBLE = "POST_VISIBLE";
	static public final String C_ESTADOFINAL = "ESTADOFINAL";
	static public final String C_ACTIVARALERTAS = "ACTIVARALERTAS";
	static public final String C_DIASANTELACION = "DIASANTELACION";
	static public final String C_POSTVISIBLEFICHA = "POST_VISIBLEENFICHA";	
	
	static public final String T_NOMBRETABLA = "EXP_ESTADO";
	

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getAutomatico() {
        return automatico;
    }
    public void setAutomatico(String esAutomatico) {
        this.automatico = esAutomatico;
    }
    public String getEjecucionSancion() {
        return ejecucionSancion;
    }
    public void setEjecucionSancion(String esEjecucionSancion) {
        this.ejecucionSancion = esEjecucionSancion;
    }
    public Integer getIdEstado() {
        return idEstado;
    }
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
    public Integer getIdEstadoSiguiente() {
        return idEstadoSiguiente;
    }
    public void setIdEstadoSiguiente(Integer idEstadoSiguiente) {
        this.idEstadoSiguiente = idEstadoSiguiente;
    }
    public Integer getIdFase() {
        return idFase;
    }
    public void setIdFase(Integer idFase) {
        this.idFase = idFase;
    }
    public Integer getIdFaseSiguiente() {
        return idFaseSiguiente;
    }
    public void setIdFaseSiguiente(Integer idFaseSiguiente) {
        this.idFaseSiguiente = idFaseSiguiente;
    }
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }    

    public String getPostVisible() {
        return postVisible;
    }
    public void setPostVisible(String postVisible) {
        this.postVisible = postVisible;
    }
    public String getPostVisibleFicha() {
        return postVisibleFicha;
    }
    public void setPostVisibleFicha(String postVisibleFicha) {
        this.postVisibleFicha = postVisibleFicha;
    }
    public String getPreVisible() {
        return preVisible;
    }
    public void setPreVisible(String preVisible) {
        this.preVisible = preVisible;
    }
    public String getPreVisibleFicha() {
        return preVisibleFicha;
    }
    public void setPreVisibleFicha(String preVisibleFicha) {
        this.preVisibleFicha = preVisibleFicha;
    }
    public String getPreSancionado() {
        return preSancionado;
    }
    public void setPreSancionado(String preSancionado) {
        this.preSancionado = preSancionado;
    }   
    public String getPostActPrescritas() {
        return postActPrescritas;
    }
    public void setPostActPrescritas(String postActPrescritas) {
        this.postActPrescritas = postActPrescritas;
    }
    public String getPostAnotCanceladas() {
        return postAnotCanceladas;
    }
    public void setPostAnotCanceladas(String postAnotCanceladas) {
        this.postAnotCanceladas = postAnotCanceladas;
    }
    public String getPostSancionFinalizada() {
        return postSancionFinalizada;
    }
    public void setPostSancionFinalizada(String postSancionFinalizada) {
        this.postSancionFinalizada = postSancionFinalizada;
    }
    public String getPostSancionPrescrita() {
        return postSancionPrescrita;
    }
    public void setPostSancionPrescrita(String postSancionPrescrita) {
        this.postSancionPrescrita = postSancionPrescrita;
    }
    public String getEstadoFinal() {
        return estadoFinal;
    }
    public void setEstadoFinal(String valor) {
        this.estadoFinal = valor;
    }
    public String getActivarAlertas() {
        return activarAlertas;
    }
    public void setActivarAlertas(String valor) {
        this.activarAlertas = valor;
    }
    public Integer getDiasAntelacion() {
        return diasAntelacion;
    }
    public void setDiasAntelacion(Integer valor) {
        this.diasAntelacion = valor;
    }
}
