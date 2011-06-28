package com.siga.gratuita.form;

import java.util.List;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 3/2/2005
 */



public class BuscarDesignasForm extends MasterForm {
	
	
	public String getModulo	()	{
		return UtilidadesHash.getString(datos,"MODULO");
	}
	public void setModulo	(String a)	{
		UtilidadesHash.set(datos, "MODULO", a);
	}
	public String getJuzgadoActu	()	{
		return UtilidadesHash.getString(datos,"JUZGADOACTU");
	}
	public void setJuzgadoActu	(String a)	{
		UtilidadesHash.set(datos, "JUZGADOACTU", a);
	}
	public String getAcreditacion	()	{
		return UtilidadesHash.getString(datos,"ACREDITACION");
	}
	public void setAcreditacion	(String a)	{
		UtilidadesHash.set(datos, "ACREDITACION", a);
	}
	public String getAsuntoActuacion	()	{
		return UtilidadesHash.getString(datos,"ASUNTOACTUACION");
	}
	public void setAsuntoActuacion	(String a)	{
		UtilidadesHash.set(datos, "ASUNTOACTUACION", a);
	}
	
	
	public void setAsunto	(String a)	{
		UtilidadesHash.set(datos, "ASUNTO", a);
	}
	public String getAsunto	()	{
		return UtilidadesHash.getString(datos,"ASUNTO");
	}
	public void setCodigo	(String a)	{
		UtilidadesHash.set(datos, "CODIGO", a);
	}
	public void setSufijo	(String a)	{
		UtilidadesHash.set(datos, "SUFIJO", a);
	}
	public String getActuacionesPendientes	()	{
		return UtilidadesHash.getString(datos,"ACTUACIONES_PENDIENTES");
	}
	public void setActuacionesPendientes	(String a)	{
		UtilidadesHash.set(datos, "ACTUACIONES_PENDIENTES", a);
	}
	public String getCodigo	()	{
		return UtilidadesHash.getString(datos,"CODIGO");
	}
	public String getSufijo	()	{
		return UtilidadesHash.getString(datos,"SUFIJO");
	}
	public void setAnioAsistencia	(String a)	{
		UtilidadesHash.set(datos, "ANIO_ASISTENCIA", a);
	}
	public String getAnioAsistencia	()	{
		return UtilidadesHash.getString(datos,"ANIO_ASISTENCIA");
	}

	public void setEstado	(String a)	{
		UtilidadesHash.set(datos, "ESTADO", a);
	}
	public String getEstado	()	{
		return UtilidadesHash.getString(datos,"ESTADO");
	}
	public void setAnioEjg	(String a)	{
		UtilidadesHash.set(datos, "ANIO_EJG", a);
	}
	public String getAnioEjg	()	{
		return UtilidadesHash.getString(datos,"ANIO_EJG");
	}
	
	public void setCalidad	(String a)	{
		UtilidadesHash.set(datos, "CALIDAD", a);
	}
	public String getCalidad	()	{
		return UtilidadesHash.getString(datos,"CALIDAD");
	}
	public void setIdSolicitante	(String a)	{
		UtilidadesHash.set(datos, "SOLICITANTE", a);
	}
	public String getIdSolicitante	()	{
		return UtilidadesHash.getString(datos,"SOLICITANTE");
	}
	
	

	//Metodos set de los campos del formulario
	public void setDesdeAsistencia	(String anio)	{
		this.datos.put("DESDEASISTENCIA", anio);
	}
	public void setDesdeEjg	(String anio)	{
		this.datos.put("DESDEEJG", anio);
	}
	public String getDesdeAsistencia	()	{
		return (String)this.datos.get("DESDEASISTENCIA");
	}
	public String getDesdeEjg	()	{
		return (String)this.datos.get("DESDEEJG");
	}
	public void setNumeroAsistencia	(String valor)	{
		this.datos.put("NUMEROASISTENCIA", valor);
	}
	public void setNumeroEjg	(String valor)	{
		this.datos.put("NUMEROEJG", valor);
	}
	public void setIdTipoEjg	(String valor)	{
		this.datos.put("IDTIPOEJG", valor);
	}
	public void setIdTurnoEJG	(String valor)	{
		this.datos.put("IDTURNOEJG", valor);
	}
	public String getIdTurnoEJG	()	{
		return (String)this.datos.get("IDTURNOEJG");
	}
	public void setJuzgadoAsi	(String valor)	{
		this.datos.put("JUZGADOASI", valor);
	}
	public String getJuzgadoAsi	()	{
		return (String)this.datos.get("JUZGADOASI");
	}
	public void setNumProcedimiento	(String valor)	{
		this.datos.put("NUMPROCEDIMIENTO", valor);
	}
	public String getNumProcedimiento	()	{
		return (String)this.datos.get("NUMPROCEDIMIENTO");
	}
	public void setJuzgadoInstitucionAsi	(String valor)	{
		this.datos.put("JUZGADOINSTITUCIONASI", valor);
	}
	public String getJuzgadoInstitucionAsi()	{
		return (String)this.datos.get("JUZGADOINSTITUCIONASI");
	}
	public String getNumeroAsistencia	()	{
		return (String)this.datos.get("NUMEROASISTENCIA");
	}
	public String getNumeroEjg	()	{
		return (String)this.datos.get("NUMEROEJG");
	}
	public String getIdTipoEjg	()	{
		return (String)this.datos.get("IDTIPOEJG");
	}
	public void setCodigoExtJuzgado	(String a)	{
		UtilidadesHash.set(datos, "CODIGOEXT", a);
	}
	public String getCodigoExtJuzgado	()	{
		return UtilidadesHash.getString(datos,"CODIGOEXT");
	}
	
	/**
	 * Almacena en la Hashtable el numero de diligencia de la designa que se quiere buscar 
	 * 
	 * @param diligencia  
	 * @return void 
	 */
	public void setDiligencia	(String diligencia)	{
		this.datos.put("DILIGENCIA", diligencia);
	}
	/**
	 * Almacena en la Hashtable el numero de procedimiento de la designa que se quiere buscar 
	 * 
	 * @param procedimiento  
	 * @return void 
	 */
	public void setProcedimiento	(String procedimiento)	{
		this.datos.put("PROCEDIMIENTO", procedimiento);
	}
	/**
	 * Almacena en la Hashtable el numero de comisaria de la designa que se quiere buscar 
	 * 
	 * @param comisaria  
	 * @return void 
	 */
	public void setComisaria	(String comisaria)	{
		this.datos.put("COMISARIA", comisaria);
	}
	
	
	
	/**
	 * Almacena en la Hashtable el anho de la designa que se quiere buscar 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setAnio	(String anio)	{
		this.datos.put("ANIO", anio);
	}
	
	/**
	 * Almacena en la Hashtable el numero de la designa que se quiere buscar 
	 * 
	 * @param Numero  
	 * @return void 
	 */
	public void setNumero	(String numero)	{
		this.datos.put("NUMERO", numero);
	}
	
	/**
	 * Almacena en la Hashtable el campo de fecha Apertura inicial introducida en el formulario 
	 * 
	 * @param Fecha Apertura Inicio 
	 * @return void 
	 */
	public void setFechaAperturaInicio	(String fechaAperturaInicio)	{
		this.datos.put("FECHAENTRADAINICIO", fechaAperturaInicio);
	}
	
	/**
	 * Almacena en la Hashtable el campo de fecha Apertura final introducida en el formulario 
	 * 
	 * @param Fecha Apertura Fin 
	 * @return void 
	 */
	public void setFechaAperturaFin	(String fechaAperturaFin)	{
		this.datos.put("FECHAENTRADAFIN", fechaAperturaFin);
	}
	
	/**
	 * Almacena en la Hashtable el turno de la designa que se quiere buscar 
	 * 
	 * @param Turno  
	 * @return void 
	 */
	/*public void setIdTurno	(String turno)	{
		String aux = "";
		if (turno.indexOf(",")>0)this.datos.put("IDTURNO", turno.substring(turno.indexOf(",")+1,turno.length()));
		else this.datos.put("IDTURNO","");
	}*/
	
	/**
	 * Almacena en la Hashtable el TIPO de la designa que se quiere buscar 
	 * 
	 * @param Tipo  
	 * @return void 
	 */
	public void setTipoDesigna	(String tipo)	{
		this.datos.put(ScsDesignaBean.C_IDTIPODESIGNACOLEGIO, tipo);
	}
	
	/**
	 * Almacena en la Hashtable el numero del colegiado de la designa que se quiere buscar 
	 * 
	 * @param NColegiado  
	 * @return void 
	 */
	public void setNcolegiado	(String nColegiado)	{
		this.datos.put("NCOLEGIADO", nColegiado);
	}
	
	/**
	 * Almacena en la Hashtable el NIF del defendido de la designa que se quiere buscar 
	 * 
	 * @param NIF  
	 * @return void 
	 */
	public void setNif	(String nif)	{
		this.datos.put("NIF", nif);
	}
	
	/**
	 * Almacena en la Hashtable el Nombre del defendido de la designa que se quiere buscar 
	 * 
	 * @param Nombre  
	 * @return void 
	 */
	public void setNombre	(String nombre)	{
		this.datos.put("NOMBRE", nombre);
	}
	
	/**
	 * Almacena en la Hashtable el Primer apellido del defendido de la designa que se quiere buscar 
	 * 
	 * @param Ap1  
	 * @return void 
	 */
	public void setApellido1	(String ap1)	{
		this.datos.put("APELLIDO1", ap1);
	}
	
	/**
	 * Almacena en la Hashtable el Segundo apellido del defendido de la designa que se quiere buscar 
	 * 
	 * @param Ap12
	 * @return void 
	 */
	public void setApellido2	(String ap2)	{
		this.datos.put("APELLIDO2", ap2);
	}
	
	/**
	 * Almacena en la Hashtable el identificador del letrado de la designa que se quiere buscar 
	 * 
	 * @param valor
	 * @return void 
	 */
	public void setIdPersona	(String valor)	{
		this.datos.put("IDPERSONA", valor);
	}
	
	
	/**
	 * Almacena en la Hashtable el valor del campo manual 
	 * 
	 * @param valor
	 * @return void 
	 */
	public void setManual	(String valor)	{
		this.datos.put("MANUAL", valor);
	}
	//	Metodos get de los campos del formulario


	
	/**
	 * Recupera en la Hashtable el numero de diligencia de la designa que se quiere buscar 
	 * 
	 * @param diligencia  
	 * @return void 
	 */
	public String getDiligencia	()	{
		return (String)this.datos.get("DILIGENCIA");
	}
	/**
	 * Recupera en la Hashtable el numero de procedimiento de la designa que se quiere buscar 
	 * 
	 * @param procedimiento  
	 * @return void 
	 */
	public String getProcedimiento	()	{
		return (String)this.datos.get("PROCEDIMIENTO");
	}
	/**
	 * Recupera en la Hashtable el numero de comisaria de la designa que se quiere buscar 
	 * 
	 * @param comisaria  
	 * @return void 
	 */
	public String getComisaria	()	{
		return (String)this.datos.get("COMISARIA");
	}	
	
	
	
	
	
	/**
	 * Recupera Hashtable el anho de la designa que se quiere buscar 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getAnio	()	{
		return (String)this.datos.get("ANIO");
	}
	
	/**
	 * Recupera de la Hashtable el numero de la designa que se quiere buscar 
	 * 
	 * @param Numero  
	 * @return  
	 */
	public String getNumero	()	{
		return (String)this.datos.get("NUMERO");
	}
	
	/**
	 * Recupera de la Hashtable el campo de fecha Apertura inicial introducida en el formulario 
	 * 
	 * @param Fecha Apertura Inicio 
	 * @return  
	 */
	public String getFechaAperturaInicio	()	{
		return (String)this.datos.get("FECHAENTRADAINICIO");
	}
	
	/**
	 * Recupera de la Hashtable el campo de fecha Apertura final introducida en el formulario 
	 * 
	 * @param Fecha Apertura Fin 
	 * @return  
	 */
	public String getFechaAperturaFin	()	{
		return (String)this.datos.get("FECHAENTRADAFIN");
	}
	
	/**
	 * Recupera de la Hashtable el turno de la designa que se quiere buscar 
	 * 
	 * @param Turno  
	 * @return  
	 */
	/*public String getIdTurno	()	{
		return (String)this.datos.get("IDTURNO");
	}*/
	
	/**
	 * Recupera de la Hashtable el TIPO de la designa que se quiere buscar 
	 * 
	 * @param Tipo  
	 * @return  
	 */
	public String getTipoDesigna	()	{
		return (String)this.datos.get(ScsDesignaBean.C_IDTIPODESIGNACOLEGIO);
	}
	
	/**
	 * Recupera de la Hashtable el numero del colegiado de la designa que se quiere buscar 
	 * 
	 * @param NColegiado  
	 * @return  
	 */
	public String getNcolegiado	()	{
		return (String)this.datos.get("NCOLEGIADO");
	}
	
	/**
	 * Recupera de la Hashtable el NIF del defendido de la designa que se quiere buscar 
	 * 
	 * @param NIF  
	 * @return  
	 */
	public String getNif	()	{
		return (String)this.datos.get("NIF");
	}
	
	/**
	 * Recupera de la Hashtable el Nombre del defendido de la designa que se quiere buscar 
	 * 
	 * @param Nombre  
	 * @return  
	 */
	public String getNombre	()	{
		return (String)this.datos.get("NOMBRE");
	}
	
	/**
	 * Recupera de la Hashtable el Primer apellido del defendido de la designa que se quiere buscar 
	 * 
	 * @param Ap1  
	 * @return  
	 */
	public String getApellido1	()	{
		return (String)this.datos.get("APELLIDO1");
	}
	
	/**
	 * Recupera de la Hashtable el Segundo apellido del defendido de la designa que se quiere buscar 
	 * 
	 * @param Ap12
	 * @return  
	 */
	public String getApellido2	()	{
		return (String)this.datos.get("APELLIDO2");
	}
	
	/**
	 * Recupera de la Hashtable el el identificador del letrado de la designa que se quiere buscar 
	 * 
	 * @param Turno  
	 * @return  
	 */
	public String getIdPersona	()	{
		return (String)this.datos.get("IDPERSONA");
	}
	
	/**
	 * Recupera de la Hashtable el campo manual, para saber si se busca manualmente al colegiado 
	 * 
	 * @param Turno  
	 * @return  
	 */
	public String getManual	()	{
		return (String)this.datos.get("MANUAL");
	}
	
	// PARA LA GENERACION DE INFORMES
	
	private String cabeceraCarta="";
	private String motivoCarta="";
	private String pieCarta="";
	private String idTipoTurno = "";
	List<ScsTurnoBean> turnos;
	private String idTurno = "";
	
	public void setCabeceraCarta(String id){
		this.cabeceraCarta=id;
	}
	
	public void setMotivoCarta(String id){
		this.motivoCarta=id;
	}
	
	public void setPieCarta(String id){
		this.pieCarta=id;		
	}
	
	public String getCabeceraCarta(){
		return this.cabeceraCarta;
	}
	
	public String getMotivoCarta(){
		return this.motivoCarta;
	}
	
	public String getPieCarta(){
		return this.pieCarta;		
	}
	
	public void setJuzgado(String a)	{
		UtilidadesHash.set(datos, "JUZGADO", a);
	}
	public String getJuzgado()	{
		return UtilidadesHash.getString(datos,"JUZGADO");
	}
	
	public String getIdTipoTurno() {
		return idTipoTurno;
	}
	public void setIdTipoTurno(String idTipoTurno) {
		this.idTipoTurno = idTipoTurno;
	}

	public List<ScsTurnoBean> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<ScsTurnoBean> turnos) {
		this.turnos = turnos;
	}
	
	public String getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}

	String rutaFicheroDownload, ficheroDownload, borrarFicheroDownload;
	
    /**
     * @return Returns the borrarFicheroDownload.
     */
    public String getBorrarFicheroDownload() {
        return borrarFicheroDownload;
    }
    /**
     * @param borrarFicheroDownload The borrarFicheroDownload to set.
     */
    public void setBorrarFicheroDownload(String borrarFicheroDownload) {
        this.borrarFicheroDownload = borrarFicheroDownload;
    }
    /**
     * @return Returns the ficheroDownload.
     */
    public String getFicheroDownload() {
        return ficheroDownload;
    }
    /**
     * @param ficheroDownload The ficheroDownload to set.
     */
    public void setFicheroDownload(String ficheroDownload) {
        this.ficheroDownload = ficheroDownload;
    }
    /**
     * @return Returns the rutaFicheroDownload.
     */
    public String getRutaFicheroDownload() {
        return rutaFicheroDownload;
    }
    /**
     * @param rutaFicheroDownload The rutaFicheroDownload to set.
     */
    public void setRutaFicheroDownload(String rutaFicheroDownload) {
        this.rutaFicheroDownload = rutaFicheroDownload;
    }
    
	public void setNombreMostrado	(String a)	{
		UtilidadesHash.set(datos, "NombreMostrado", a);
	}	
	public String getNombreMostrado()	{
		return UtilidadesHash.getString(datos,"NombreMostrado");
	}
	
}