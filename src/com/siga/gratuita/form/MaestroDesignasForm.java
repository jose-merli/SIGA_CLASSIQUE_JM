package com.siga.gratuita.form;

import java.util.List;

import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 9/2/2005
 */



public class MaestroDesignasForm extends MasterForm {
	
	private String ANIO="ANIO";
	private String NUMERO="NUMERO";
	private String TURNO="TURNO";
	private String IDTURNO="IDTURNO";
	private String FECHA="FECHA";
	private String TIPO="TIPO";
	private String ESTADO="ESTADO";
	private String FECHACIERRE="FECHACIERRE";
	private String PROCURADOR="PROCURADOR";
	private String ASUNTO="ASUNTO";
	private String OBSERVACIONES="OBSERVACIONES";
	private String DELITOS="DELITOS";
	private String JUZGADO="JUZGADO";
	private String FECHAANULACION="FECHAANULACION";
	private String CODIGO="CODIGO";
	private String IDPROCEDIMIENTO="IDPROCEDIMIENTO";
	private String FECHAESTADO="FECHAESTADO";
	private String IDPRETENSION="IDPRETENSION";
	private String FECHARECEPCIONCOLEGIO = 	"FECHARECEPCIONCOLEGIO";
	private String FECHAOFICIOJUZGADO 	= 	"FECHAOFICIOJUZGADO";	
	private String procedimiento;
	private String letrado;
	private String nig;
	private List<ScsJuzgadoBean> juzgados;  
	private String idJuzgado;
	private List<ScsProcedimientosBean> modulos;
	private List<ScsEJGBean> ejgs;  	

	public List<ScsEJGBean> getEjgs() {
		return ejgs;
	}
	public void setEjgs(List<ScsEJGBean> ejgs) {
		this.ejgs = ejgs;
	}

	
	public String getLetrado() {
		return letrado;
	}
	public void setLetrado(String letrado) {
		this.letrado = letrado;
	}
	/**
	 * @return Returns the procedimiento.
	 */
	public String getIdProcedimiento() {
		return (String) this.datos.get(this.IDPROCEDIMIENTO);
	}
	/**
	 * @param procedimiento The procedimiento to set.
	 */
	public void setIdProcedimiento(String valor) {
		
		this.datos.put(this.IDPROCEDIMIENTO, valor);
	}
	//Metodos set de los campos del formulario

	/**
	 * Almacena en la Hashtable el anho de la designa que se quiere buscar 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setJuzgado(String valor){
		this.datos.put(this.JUZGADO, valor);
	}
	
	/**
	 * Almacena en la Hashtable el anho de la designa que se quiere buscar 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setAnio	(String valor){
		this.datos.put(this.ANIO, valor);
	}
	
	/**
	 * Almacena en la Hashtable el numero de la designa que se quiere buscar 
	 * 
	 * @param Numero  
	 * @return void 
	 */
	public void setNumero	(String valor)	{
		this.datos.put(this.NUMERO, valor);
	}
	
	/**
	 * Almacena en la Hashtable el campo Turno de la designa 
	 * 
	 * @param Turno 
	 * @return void 
	 */
	public void setTurno	(String valor)	{
		this.datos.put(this.TURNO, valor);
	}
	/**
	 * Almacena en la Hashtable el campo Turno de la designa 
	 * 
	 * @param Turno 
	 * @return void 
	 */
	public void setIdTurno	(String valor)	{
		this.datos.put(this.IDTURNO, valor);
	}
	
	/**
	 * Almacena en la Hashtable el campo de fecha Cierre introducida en el formulario 
	 * 
	 * @param Fecha Cierre 
	 * @return void 
	 */
	public void setFecha	(String valor)	{
		this.datos.put(this.FECHA, valor);
	}
	
	/**
	 * Almacena en la Hashtable el TIPO de la designa que se quiere buscar 
	 * 
	 * @param Tipo  
	 * @return void 
	 */
	public void setTipo (String valor)	{
		this.datos.put(this.TIPO, valor);
	}
	
	/**
	 * Almacena en la Hashtable el Estado de la designa que se quiere buscar 
	 * 
	 * @param Tipo  
	 * @return void 
	 */
	public void setEstado (String valor)	{
		this.datos.put(this.ESTADO, valor);
	}
	
	/**
	 * Almacena en la Hashtable el campo de fecha Cierre introducida en el formulario 
	 * 
	 * @param Fecha Cierre 
	 * @return void 
	 */
	public void setFechaCierre	(String valor)	{
		this.datos.put(this.FECHACIERRE, valor);
	}
	
	/**
	 * Almacena en la Hashtable el campo de fecha de anulación introducida en el formulario 
	 * 
	 * @param Fecha Cierre 
	 * @return void 
	 */
	public void setFechaAnulacion	(String valor)	{
		this.datos.put(this.FECHAANULACION, valor);
	}
	public void setFechaEstado	(String valor)	{
		this.datos.put(this.FECHAESTADO, valor);
	}
	
	/**
	 * Almacena en la Hashtable el turno de la designa que se quiere buscar 
	 * 
	 * @param Turno  
	 * @return void 
	 */
	public void setProcurador (String valor) {
		this.datos.put(this.PROCURADOR,valor);
	}
	
	
	/**
	 * Almacena en la Hashtable el asunto de la designa que se quiere buscar 
	 * 
	 * @param Asunto  
	 * @return void 
	 */
	public void setAsunto	(String valor)	{
		this.datos.put(this.ASUNTO, valor);
	}
	
	/**
	 * Almacena en la Hashtable las observaciones de la designa que se quiere buscar 
	 * 
	 * @param Observaciones  
	 * @return void 
	 */
	public void setObservaciones	(String valor)	{
		this.datos.put(this.OBSERVACIONES, valor);
	}
	
	/**
	 * Almacena en la Hashtable los delitos de la designa que se quiere buscar 
	 * 
	 * @param Delitos  
	 * @return void 
	 */
	public void setDelitos (String valor)	{
		this.datos.put(this.DELITOS, valor);
	}
	
	/**
	 * Almacena en la Hashtable los códigos de la designa que se quiere buscar 
	 * 
	 * @param Codigo  
	 * @return void 
	 */
	public void setCodigo (String valor)	{
		this.datos.put(this.CODIGO, valor);
	}
	//	Metodos get de los campos del formulario

	public void setFechaJuicio (String valor)	{
		this.datos.put("FechaJuicio", valor);
	}
	public void setHorasJuicio (String valor)	{
		this.datos.put("HorasJuicio", valor);
	}
	public void setMinutosJuicio (String valor)	{
		this.datos.put("MinutosJuicio", valor);
	}

	/**
	 * Recupera Hashtable el anho de la designa que se quiere buscar 
	 * 
	 * @param   
	 * @return Anio  
	 */
	public String getAnio	()	{
		return (String)this.datos.get("ANIO");
	}
	
	/**
	 * Recupera de la Hashtable el numero de la designa que se quiere buscar 
	 * 
	 * @param   
	 * @return  Numero
	 */
	public String getNumero	()	{
		return (String)this.datos.get("NUMERO");
	}
	
	/**
	 * Recupera de la Hashtable el campo Turno de la designa 
	 * 
	 * @param
	 * @return String Turno 
	 */
	public String getTurno	()	{
		return (String) this.datos.get(this.TURNO);
	}
	/**
	 * Recupera de la Hashtable el campo Turno de la designa 
	 * 
	 * @param
	 * @return String Turno 
	 */
	public String getIdTurno	()	{
		return (String) this.datos.get(this.IDTURNO);
	}
	
	/**
	 * Recupera de la Hashtable el campo de fecha introducida en el formulario 
	 * 
	 * @param 
	 * @return String Fecha 
	 */
	public String getFecha	()	{
		return (String) this.datos.get(this.FECHA);
	}
	
	/**
	 * Recupera de la Hashtable el TIPO de la designa que se quiere buscar 
	 * 
	 * @param   
	 * @return String Tipo
	 */
	public String getTipo ()	{
		return (String) this.datos.get(this.TIPO);
	}
	
	/**
	 * Recupera de la Hashtable el Estado de la designa que se quiere buscar 
	 * 
	 * @param
	 * @return String Tipo  
	 */
	public String getEstado ()	{
		return (String) this.datos.get(this.ESTADO);
	}
	
	/**
	 * Recupera de la Hashtable el campo de fecha Cierre introducida en el formulario 
	 * 
	 * @param
	 * @return String Fecha Cierre 
	 */
	public String getFechaCierre	()	{
		return (String) this.datos.get(this.FECHACIERRE);
	}
	
	/**
	 * Recupera de la Hashtable el campo de fecha Cierre introducida en el formulario 
	 * 
	 * @param
	 * @return String Fecha Cierre 
	 */
	public String getFechaAnulacion	()	{
		return (String) this.datos.get(this.FECHAANULACION);
	}
	public String getFechaEstado	()	{
		return (String) this.datos.get(this.FECHAESTADO);
	}
	/**
	 * Recupera de la Hashtable el turno de la designa que se quiere buscar 
	 * 
	 * @param 
	 * @return String Procurador
	 */
	public String getProcurador () {
		return (String) this.datos.get(this.PROCURADOR);
	}
	
	
	/**
	 * Recupera de la Hashtable el asunto de la designa que se quiere buscar 
	 * 
	 * @param  
	 * @return String Asunto
	 */
	public String getAsunto	()	{
		return (String) this.datos.get(this.ASUNTO);
	}
	
	/**
	 * Recupera de la Hashtable las observaciones de la designa que se quiere buscar 
	 * 
	 * @param
	 * @return String Observaciones
	 */
	public String getObservaciones	()	{
		return (String) this.datos.get(this.OBSERVACIONES);
	}
	
	/**
	 * Recupera de la Hashtable los delitos de la designa que se quiere buscar 
	 * 
	 * @param
	 * @return String Delitos
	 */
	public String getDelitos ()	{
		return (String) this.datos.get(this.DELITOS);
	}

	/**
	 * Recupera de la Hashtable los codigos de la designa que se quiere buscar 
	 * 
	 * @param
	 * @return String Delitos
	 */
	public String getCodigo ()	{
		return (String) this.datos.get(this.CODIGO);
	}

	/**
	 * Recupera de la Hashtable los delitos de la designa que se quiere buscar 
	 * 
	 * @param
	 * @return String Delitos
	 */
	public String getJuzgado ()	{
		return (String) this.datos.get(this.JUZGADO);
	}
	
	public void setDesdeEjg(String valor){
		UtilidadesHash.set(this.datos,"DesdeEjg", valor);
	}
	public String getDesdeEjg(){
		return UtilidadesHash.getString(this.datos,"DesdeEjg");
	}

	public String getEstadoOriginal(){
		return UtilidadesHash.getString(this.datos, "ESTADO_ORIGINAL");
	}
	
	public String getFechaJuicio(){
		return UtilidadesHash.getString(this.datos, "FechaJuicio");
	}
	
	public String getHorasJuicio(){
		return UtilidadesHash.getString(this.datos, "HorasJuicio");
	}
	
	public String getMinutosJuicio(){
		return UtilidadesHash.getString(this.datos, "MinutosJuicio");
	}
	
	/**
	 * Almacena en la Hashtable el anho de la designa que se quiere buscar 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setEstadoOriginal(String valor){
		UtilidadesHash.set(this.datos, "ESTADO_ORIGINAL", valor);
	}		

	
	public void setNumeroProcedimiento(String v){
		UtilidadesHash.set(this.datos, "NUMERO_PROCEDIMIENTO", v);
	}
	public String getNumeroProcedimiento(){
		return UtilidadesHash.getString(this.datos, "NUMERO_PROCEDIMIENTO");
	}
	
	/**
	 * @return
	 */
	public String getIdPretension() {
		return UtilidadesHash.getString(this.datos, this.IDPRETENSION);
	}
	/**
	 * @param idpretension
	 */
	public void setIdPretension(String idpretension) {
		UtilidadesHash.set(this.datos, this.IDPRETENSION, idpretension);
	}
	
	public String getFechaRecepcionColegio() {
		return UtilidadesHash.getString(this.datos, this.FECHARECEPCIONCOLEGIO);
	}
	public void setFechaRecepcionColegio(String fechaRecepcionColegio) {
		UtilidadesHash.set(this.datos, this.FECHARECEPCIONCOLEGIO, fechaRecepcionColegio);
	}
	public String getFechaOficioJuzgado() {
		return UtilidadesHash.getString(this.datos, this.FECHAOFICIOJUZGADO);
	}
	public void setFechaOficioJuzgado(String fechaOficioJuzgado) {
		UtilidadesHash.set(this.datos, this.FECHAOFICIOJUZGADO, fechaOficioJuzgado);
	}
	public void setFormulario(ScsDesignaBean beanDesigna){
		setCodigo(beanDesigna.getAnio()+"/"+beanDesigna.getCodigo());
		try {
			setFecha(GstDate.getFormatedDateShort("",beanDesigna.getFechaEntrada()));	
		} catch (Exception e) {}
		
		setProcedimiento(beanDesigna.getProcedimiento());
		if(beanDesigna.getIdJuzgado()!=null)
			setIdJuzgado(beanDesigna.getIdJuzgado().toString());
	}
	public List<ScsJuzgadoBean> getJuzgados() {
		return juzgados;
	}
	public void setJuzgados(List<ScsJuzgadoBean> juzgados) {
		this.juzgados = juzgados;
	}
	public String getIdJuzgado() {
		return idJuzgado;
	}
	public void setIdJuzgado(String idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	public List<ScsProcedimientosBean> getModulos() {
		return modulos;
	}
	public void setModulos(List<ScsProcedimientosBean> modulos) {
		this.modulos = modulos;
	}
	public String getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	public String getNig() {
		return nig;
	}
	public void setNig(String nig) {
		this.nig = nig;
	}
	
}