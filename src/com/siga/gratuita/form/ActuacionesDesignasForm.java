package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 3/2/2005
 * @version 27/01/2006 (david.sanchezp): nuevos campos.
 */
public class ActuacionesDesignasForm extends MasterForm {
	
	protected String defensaJuridica= "DEFENSAJURIDICA";
	protected String codigo="CODIGO";	
	protected String idturno = "IDTURNO";
	protected String turno = "TURNO";
	protected String anio = "ANIO";
	protected String fecha = "FECHA";
	protected String numero = "NUMERO";
	protected String lugar = "LUGAR";
	protected String ncolegiado = "NCOLEGIADO";
	protected String nombre = "NOMBRE";
	protected String apellido1 = "APELLIDO1";
	protected String apellido2 = "APELLIDO2";
	protected String nactuacion = "NACTUACION";
	protected String fechaActuacion = "FECHAACTUACION";
	protected String procedimiento = "PROCEDIMIENTO";
	protected String acuerdoExtrajudicial = "ACUERDOEXTRAJUDICIAL";
	protected String anulacion = "ANULACION";
	protected String observaciones = "OBSERVACIONES";
	protected String fechaJustificacion = "FECHAJUSTIFICACION";
	protected String observacionesJustificacion = "OBSERVACIONESJUSTIFICACION";
	protected String juzgado = "JUZGADO";
	protected String comisaria = "COMISARIA";
	protected String prision = "PRISION";
	protected String acreditacion = "ACREDITACION";
	private String actuacionValidada = "ACTUACION_VALIDADA";
	private final String IDPERSONA = "IDPERSONA";
	protected String pretension = "PRETENSION";
	protected String talonario = "TALONARIO";
	protected String talon = "TALON";
	protected String numeroProcedimiento = "NUMEROPROCEDIMIENTO";
	protected String nig = "NIG";
	private boolean fichaColegial = false;
	public boolean getFichaColegial() {
		return fichaColegial;
	}
	public void setFichaColegial(boolean fichaColegial) {
		this.fichaColegial = fichaColegial;
	}
	
		/**
	 * Almacena en la Hashtablla el nombre del talon de la Designa 
	 * 
	 * @param talon 
	 * @return void 
	 */
	public void setTalon(String valor) {
		this.datos.put(this.talon, valor); 
	}
	
	/**
	 * Almacena en la Hashtablla el nombre del talonario de la Designa 
	 * 
	 * @param talonario
	 * @return void 
	 */
	public void setTalonario(String valor) {
		this.datos.put(this.talonario, valor);
	}
	//Metodos set de los campos del formulario
	/**
	 * Almacena en la Hashtablela fecha de la Designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setFecha	(String valor)	{
		this.datos.put(this.fecha, valor);
	}
	/**
	 * Almacena en la Hashtablla el nombre del turno de la Designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setTurno	(String valor)	{
		this.datos.put(this.turno, valor);
	}
	/**
	 * Almacena en la Hashtable el numero del colegiado de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setNcolegiado	(String valor)	{
		this.datos.put(this.ncolegiado, valor);
	}
	/**
	 * Almacena en la Hashtable nombre del colegiado de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setNombre	(String valor)	{
		this.datos.put(this.nombre, valor);
	}
	/**
	 * Almacena en la Hashtable el apellido1 del letrado de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setApellido1	(String valor)	{
		this.datos.put(this.apellido1, valor);
	}
	/**
	 * Almacena en la Hashtable el apellido1 del letrado  de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setApellido2	(String valor)	{
		this.datos.put(this.apellido2, valor);
	}
	/**
	 * Almacena en la Hashtable el numero de facturacion de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setNactuacion	(String valor)	{
		this.datos.put(this.nactuacion, valor);
	}
	/**
	 * Almacena en la Hashtable la fecha de facturacion de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setFechaActuacion	(String valor)	{
		this.datos.put(this.fechaActuacion, valor);
	}
	/**
	 * Almacena en la Hashtable el procedimineto de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setProcedimiento	(String valor)	{
		this.datos.put(this.procedimiento, valor);
	}
	/**
	 * Almacena en la Hashtable el lugar de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setLugar	(String valor)	{
		this.datos.put(this.lugar, valor);
	}
	/**
	 * Almacena en la Hashtable el acuerdo extrajudicial de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setAcuerdoExtrajudicial	(String valor)	{
		this.datos.put(this.acuerdoExtrajudicial, valor);
	}
	/**
	 * Almacena en la Hashtable la anulacion de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setAnulacion	(String valor)	{
		this.datos.put(this.anulacion, valor);
	}
	/**
	 * Almacena en la Hashtable el Campo Observaciones de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setObservaciones	(String valor)	{
		this.datos.put(this.observaciones, valor);
	}
	/**
	 * Almacena en la Hashtable el Campo observaciones de justificacion de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setObservacionesJustificacion	(String valor)	{
		this.datos.put(this.observacionesJustificacion, valor);
	}
	/**
	 * Almacena en la Hashtable el Campo fecha justificacion de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setFechaJustificacion	(String valor)	{
		this.datos.put(this.fechaJustificacion, valor);
	}
	
	/**
	 * Almacena en la Hashtable el Campo defensa Juridica de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setDefensaJuridica	(String valor)	{
		this.datos.put(this.defensaJuridica, valor);
	}
	/**
	 * Almacena en la Hashtable el anho de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setAnio	(String valor)	{
		this.datos.put(this.anio, valor);
	}
	/**
	 * Almacena en la Hashtable el numero de la designa  
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setNumero	(String valor)	{
		this.datos.put(this.numero, valor);
	}
	/**
	 * Almacena en la Hashtable idturno de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setIdTurno	(String valor)	{
		this.datos.put(this.idturno, valor);
	}
	
	//	Metodos get de los campos del formulario

	/**
	 * Recupera Hashtable el campo designa Talonario de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getTalonario() {
	  return (String)this.datos.get(this.talonario);	
	}
		
	/**
	 * Recupera Hashtable el campo designa Talon de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getTalon() {
		return (String)this.datos.get(this.talon);
	}
	
	
	/**
	 * Recupera Hashtable el campo designa juridica de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getDefensaJuridica	()	{
		return (String)this.datos.get(this.defensaJuridica);
	}
	/**
	 * Recupera Hashtable el anho de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getAnio	()	{
		return (String)this.datos.get(this.anio);
	}
	/**
	 * Recupera Hashtable el numero de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getNumero	()	{
		return (String)this.datos.get(this.numero);
	}
	/**
	 * Recupera Hashtable el idTunro de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getIdTurno	()	{
		return (String)this.datos.get(this.idturno);
	}
	

	
	public String getFecha	()	{
		return (String)this.datos.get(this.fecha);
	}
	public String getNcolegiado	()	{
		return (String)this.datos.get(this.ncolegiado);
	}
	public String getNombre	()	{
		return (String)this.datos.get(this.nombre);
	}
	public String getApellido1	()	{
		return (String)this.datos.get(this.apellido1);
	}
	public String getApellido2	()	{
		return (String)this.datos.get(this.apellido2);
	}
	public String getNactuacion	()	{
		return (String)this.datos.get(this.nactuacion);
	}
	public String getFechaActuacion	()	{
		return (String)this.datos.get(this.fechaActuacion);
	}
	public String getProcedimiento	()	{
		return (String)this.datos.get(this.procedimiento);
	}
	public String getLugar	()	{
		return (String)this.datos.get(this.lugar);
	}
	public String getAcuerdoExtrajudicial	()	{
		return (String)this.datos.get(this.acuerdoExtrajudicial);
	}
	public String getAnulacion	()	{
		return (String)this.datos.get(this.anulacion);
	}
	public String getObservaciones	()	{
		return (String)this.datos.get(this.observaciones);
	}
	public String getObservacionesJustificacion	()	{
		return (String)this.datos.get(this.observacionesJustificacion);
	}
	public String getFechaJustificacion	()	{
		return (String)this.datos.get(this.fechaJustificacion);
	}
	public String getTurno	()	{
		return (String)this.datos.get(this.turno);
	}

	/**
	 * @return Returns the comisaria.
	 */
	public String getComisaria() {
		return (String)this.datos.get(this.comisaria);
	}
	/**
	 * @param comisaria The comisaria to set.
	 */
	public void setComisaria(String comisaria) {
		this.datos.put(this.comisaria, comisaria);
	}
	/**
	 * @return Returns the idturno.
	 */
	public String getIdturno() {
		return (String)this.datos.get(this.idturno);
	}
	/**
	 * @param idturno The idturno to set.
	 */
	public void setIdturno(String idturno) {
		this.datos.put(this.idturno, idturno);
	}
	/**
	 * @return Returns the juzgado.
	 */
	public String getJuzgado() {
		return (String)this.datos.get(this.juzgado);
	}
	/**
	 * @param juzgado The juzgado to set.
	 */
	public void setJuzgado(String juzgado) {
		this.datos.put(this.juzgado, juzgado);
	}
	/**
	 * @return Returns the prision.
	 */
	public String getPrision() {
		return (String)this.datos.get(this.prision);
	}
	/**
	 * @param prision The prision to set.
	 */
	public void setPrision(String prision) {
		this.datos.put(this.prision, prision);
	}
	
	/**
	 * @return Returns the pretension.
	 */
	public String getPretension() {
		return (String)this.datos.get(this.pretension);
	}
	/**
	 * @param prision The pretension to set.
	 */
	public void setPretension(String pretension) {
		this.datos.put(this.pretension, pretension);
	}
	/**
	 * @return Returns the acreditacion.
	 */
	public String getAcreditacion() {
		return (String)this.datos.get(this.acreditacion);
	}
	/**
	 * @param acreditacion to set.
	 */
	public void setAcreditacion(String acreditacion) {
		this.datos.put(this.acreditacion, acreditacion);
	}
	
	/**
	 * @return Returns the actuacionValidada.
	 */
	public String getActuacionValidada() {
		return UtilidadesHash.getString(datos, actuacionValidada);
	}
	/**
	 * @param actuacionValidada The actuacionValidada to set.
	 */
	public void setActuacionValidada(String _s) {
		UtilidadesHash.set (datos, actuacionValidada, _s);
	}

	/**
	 * Almacena en la Hashtablela fecha de la Designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setIdPersona	(String valor)	{
		this.datos.put(this.IDPERSONA, valor);
	}
	/**
	 * @return Returns the idpersona.
	 */
	public String getIdPersona() {
		return UtilidadesHash.getString(this.datos, this.IDPERSONA);
	}

	/**
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}

		/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
		public String getNumeroProcedimiento() {
			return UtilidadesHash.getString(this.datos, this.numeroProcedimiento);
		}
		public void setNumeroProcedimiento(String numeroProcedimiento) {
			this.datos.put(this.numeroProcedimiento, numeroProcedimiento);
		}
		
		public String getNig() {
			return UtilidadesHash.getString(this.datos, this.nig);
		}
		public void setNig(String nig) {
			this.datos.put(this.nig, nig);
		}		

}