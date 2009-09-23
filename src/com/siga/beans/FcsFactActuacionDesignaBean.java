//VERSIONES:
//ruben.fernandez 29-03-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_FACT_ACTUACIONDESIGNA
* 
* @author AtosOrigin
* @since 09/03/2005 
*/

public class FcsFactActuacionDesignaBean extends MasterBean{
	
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idFacturacion;
	private Long		numeroAsunto;
	private Long		numero;
	private Integer		anio;
	private Integer		idTurno;
	private Long		idPersona;
	private String		procedimiento;
	private String		acreditacion;
	private String		fechaActuacion;
	private String		fechaJustificacion;
	private Double		precioAplicado;
	private Integer		porcentajeFacturado;
	private Integer		puntosAplicados;
	private String		codigoProcedimiento;
	
	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_FACT_ACTUACIONDESIGNA";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDFACTURACION = 			"IDFACTURACION";
	static public final String 	C_NUMEROASUNTO = 			"NUMEROASUNTO";
	static public final String 	C_NUMERO = 					"NUMERO";
	static public final String 	C_ANIO = 					"ANIO";
	static public final String 	C_IDTURNO = 				"IDTURNO";
	static public final String 	C_PRECIOAPLICADO = 			"PRECIOAPLICADO";
	static public final String 	C_PORCENTAJEFACTURADO = 	"PORCENTAJEFACTURADO";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_PROCEDIMIENTO = 			"PROCEDIMIENTO";
	static public final String 	C_ACREDITACION = 			"ACREDITACION";
	static public final String 	C_FECHA_ACTUACION = 		"FECHAACTUACION";
	static public final String 	C_FECHA_JUSTIFICACION = 	"FECHAJUSTIFICACION";
	static public final String 	C_PUNTOS_APLICADOS = 		"PUNTOSAPLICADOS";
	static public final String 	C_CODIGOPROCEDIMIENTO = 	"CODIGOPROCEDIMIENTO";
	
	
	
	
	
	/**
	 * @return Returns the anio.
	 */
	public Integer getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	/**
	 * @return Returns the idFacturacion.
	 */
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	/**
	 * @param idFacturacion The idFacturacion to set.
	 */
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the idProcedimiento.
	 */
	public String getProcedimiento() {
		return procedimiento;
	}
	/**
	 * @param idProcedimiento The idProcedimiento to set.
	 */
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	/**
	 * @return Returns the idTurno.
	 */
	public Integer getIdTurno() {
		return idTurno;
	}
	/**
	 * @param idTurno The idTurno to set.
	 */
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	
	/**
	 * @return Returns the numero.
	 */
	public Long getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	/**
	 * @return Returns the numeroAsunto.
	 */
	public Long getNumeroAsunto() {
		return numeroAsunto;
	}
	/**
	 * @param numeroAsunto The numeroAsunto to set.
	 */
	public void setNumeroAsunto(Long numeroAsunto) {
		this.numeroAsunto = numeroAsunto;
	}
	/**
	 * @return Returns the porcentajeFacturado.
	 */
	public Integer getPorcentajeFacturado() {
		return porcentajeFacturado;
	}
	/**
	 * @param porcentajeFacturado The porcentajeFacturado to set.
	 */
	public void setPorcentajeFacturado(Integer porcentajeFacturado) {
		this.porcentajeFacturado = porcentajeFacturado;
	}
	/**
	 * @return Returns the precioAplicado.
	 */
	public Double getPrecioAplicado() {
		return precioAplicado;
	}
	/**
	 * @param precioAplicado The precioAplicado to set.
	 */
	public void setPrecioAplicado(Double precioAplicado) {
		this.precioAplicado = precioAplicado;
	}
	public String getAcreditacion() {
		return acreditacion;
	}
	public void setAcreditacion(String acreditacion) {
		this.acreditacion = acreditacion;
	}
	public String getCodigoProcedimiento() {
		return codigoProcedimiento;
	}
	public void setCodigoProcedimiento(String codigoProcedimiento) {
		this.codigoProcedimiento = codigoProcedimiento;
	}
	public String getFechaActuacion() {
		return fechaActuacion;
	}
	public void setFechaActuacion(String fechaActuacion) {
		this.fechaActuacion = fechaActuacion;
	}
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	public Integer getPuntosAplicados() {
		return puntosAplicados;
	}
	public void setPuntosAplicados(Integer puntosAplicados) {
		this.puntosAplicados = puntosAplicados;
	}
}