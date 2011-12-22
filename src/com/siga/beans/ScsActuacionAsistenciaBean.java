package com.siga.beans;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.gratuita.form.ActuacionAsistenciaForm;

//Clase: ScsActuacionAsistenciaBean 
//Autor: carlos.vidal@atosorigin.com
//Ultima modificación: 20/12/2004

/**
 * @version: 05/04/2006 (david.sanchezp): revision de tipos y anhado nuevos campos.
 * 
 * Implementa las operaciones sobre el bean de la tabla SCS_ACTUACIONASISTENCIA
 */
public class ScsActuacionAsistenciaBean extends MasterBean {
	
	/* Variables */
	private String  fecha, diaDespues, descripcionBreve, lugar, numeroAsunto;
	private String observaciones, observacionesJustificacion, validada;
	private String  fechaJustificacion, facturado, pagado;
	
	private Integer idFacturacion;
	private Integer idJuzgado, idComisaria;
	private Integer idInstitucion, anio, acuerdoExtrajudicial, anulacion;
	private Integer idPrision, idTipoAsistencia, idTipoActuacion;

	private Long idInstitucionJuzgado, idInstitucionComisaria, idInstitucionPrision;
	private Long numero, idActuacion;
	private String NIG;

	ActuacionAsistenciaForm actuacionAsistenciaForm;
	

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_ACTUACIONASISTENCIA";
	
	/* Nombre campos de la tabla */
	static public final String 	C_IDINSTITUCION="IDINSTITUCION";
	static public final String 	C_ANIO="ANIO";     
	static public final String 	C_NUMERO="NUMERO";   
	static public final String 	C_IDACTUACION="IDACTUACION";
	static public final String 	C_FECHA="FECHA";    
	static public final String 	C_DIADESPUES="DIADESPUES"; 
	static public final String 	C_ACUERDOEXTRAJUDICIAL="ACUERDOEXTRAJUDICIAL";
	static public final String 	C_FECHAMODIFICACION="FECHAMODIFICACION";
	static public final String 	C_USUMODIFICACION="USUMODIFICACION";
	static public final String 	C_FECHAJUSTIFICACION="FECHAJUSTIFICACION";
	static public final String 	C_DESCRIPCIONBREVE="DESCRIPCIONBREVE"; 
	static public final String 	C_LUGAR="LUGAR";    
	static public final String 	C_NUMEROASUNTO="NUMEROASUNTO";
	static public final String 	C_ANULACION="ANULACION";
	static public final String 	C_OBSERVACIONESJUSTIFICACION="OBSERVACIONESJUSTIFICACION";
	static public final String 	C_OBSERVACIONES="OBSERVACIONES";
	static public final String  C_FACTURADO = "FACTURADO";
	static public final String  C_PAGADO = "PAGADO";
	static public final String  C_IDFACTURACION = "IDFACTURACION";
	static public final String  C_IDPRISION = "IDPRISION";
	static public final String  C_IDINSTITUCIONPRISION = "IDINSTITUCION_PRIS";
	static public final String  C_IDJUZGADO = "IDJUZGADO";
	static public final String  C_IDINSTITUCIONJUZGADO = "IDINSTITUCION_JUZG";
	static public final String  C_IDCOMISARIA = "IDCOMISARIA";
	static public final String  C_IDINSTITUCIONCOMISARIA = "IDINSTITUCION_COMIS";
	static public final String  C_VALIDADA = "VALIDADA";
	static public final String  C_IDTIPOASISTENCIA = "IDTIPOASISTENCIA";
	static public final String  C_IDTIPOACTUACION = "IDTIPOACTUACION";
	static public final String  C_NIG   				= 	"NIG";	
	
	
	
	public Integer getAcuerdoExtrajudicial() {
		return acuerdoExtrajudicial;
	}
	public void setAcuerdoExtrajudicial(Integer acuerdoExtrajudicial) {
		this.acuerdoExtrajudicial = acuerdoExtrajudicial;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getAnulacion() {
		return anulacion;
	}
	public void setAnulacion(Integer anulacion) {
		this.anulacion = anulacion;
	}
	public String getDescripcionBreve() {
		return descripcionBreve;
	}
	public void setDescripcionBreve(String descripcionBreve) {
		this.descripcionBreve = descripcionBreve;
	}
	public String getDiaDespues() {
		return diaDespues;
	}
	public void setDiaDespues(String diaDespues) {
		this.diaDespues = diaDespues;
	}
	public String getFacturado() {
		return facturado;
	}
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	public Long getIdActuacion() {
		return idActuacion;
	}
	public void setIdActuacion(Long idActuacion) {
		this.idActuacion = idActuacion;
	}
	public Integer getIdComisaria() {
		return idComisaria;
	}
	public void setIdComisaria(Integer idComisaria) {
		this.idComisaria = idComisaria;
	}
	
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Long getIdInstitucionComisaria() {
		return idInstitucionComisaria;
	}
	public void setIdInstitucionComisaria(Long idInstitucionComisaria) {
		this.idInstitucionComisaria = idInstitucionComisaria;
	}
	public Long getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}
	public void setIdInstitucionJuzgado(Long idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}
	public Long getIdInstitucionPrision() {
		return idInstitucionPrision;
	}
	public void setIdInstitucionPrision(Long idInstitucionPrision) {
		this.idInstitucionPrision = idInstitucionPrision;
	}
	public Integer getIdJuzgado() {
		return idJuzgado;
	}
	public void setIdJuzgado(Integer idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	public Integer getIdPrision() {
		return idPrision;
	}
	public void setIdPrision(Integer idPrision) {
		this.idPrision = idPrision;
	}
	public Integer getIdTipoActuacion() {
		return idTipoActuacion;
	}
	public void setIdTipoActuacion(Integer idTipoActuacion) {
		this.idTipoActuacion = idTipoActuacion;
	}
	public Integer getIdTipoAsistencia() {
		return idTipoAsistencia;
	}
	public void setIdTipoAsistencia(Integer idTipoAsistencia) {
		this.idTipoAsistencia = idTipoAsistencia;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getNumeroAsunto() {
		return numeroAsunto;
	}
	public void setNumeroAsunto(String numeroAsunto) {
		this.numeroAsunto = numeroAsunto;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getObservacionesJustificacion() {
		return observacionesJustificacion;
	}
	public void setObservacionesJustificacion(String observacionesJustificacion) {
		this.observacionesJustificacion = observacionesJustificacion;
	}
	public String getPagado() {
		return pagado;
	}
	public void setPagado(String pagado) {
		this.pagado = pagado;
	}
	public String getValidada() {
		return validada;
	}
	public void setValidada(String validada) {
		this.validada = validada;
	}
	public ActuacionAsistenciaForm getActuacionAsistenciaForm() {
		if(actuacionAsistenciaForm==null)
			actuacionAsistenciaForm  = new ActuacionAsistenciaForm();
		if(idInstitucion!=null && !idInstitucion.equals(""))
			actuacionAsistenciaForm.setIdInstitucion(idInstitucion.toString());
		if(anio!=null&& !anio.equals(""))
			actuacionAsistenciaForm.setAnio(anio.toString());
		if(numero!=null&& !numero.equals(""))
			actuacionAsistenciaForm.setNumero(numero.toString());
		if(idActuacion!=null&& !idActuacion.equals(""))
			actuacionAsistenciaForm.setIdActuacion(idActuacion.toString());
		if(fecha!=null&& !fecha.equals("")){
			try {
				actuacionAsistenciaForm.setFecha(GstDate.getFormatedDateShort("", fecha) );
			} catch (ClsExceptions e1) {}
		}

		if(diaDespues!=null)
			actuacionAsistenciaForm.setDiaDespues(diaDespues);
		if(acuerdoExtrajudicial!=null&& !acuerdoExtrajudicial.equals(""))
			actuacionAsistenciaForm.setAcuerdoExtrajudicial(acuerdoExtrajudicial.toString());
		if(idTipoActuacion!=null&& !idTipoActuacion.equals(""))
			actuacionAsistenciaForm.setIdTipoActuacion(idTipoActuacion.toString());
		if(idTipoAsistencia!=null&& !idTipoAsistencia.equals(""))
			actuacionAsistenciaForm.setIdTipoAsistencia(idTipoAsistencia.toString());

		if(fechaJustificacion!=null && !fechaJustificacion.equals("")){
			try {
				actuacionAsistenciaForm.setFechaJustificacion(GstDate.getFormatedDateShort("",  fechaJustificacion));
			} catch (ClsExceptions e1) {}
		}else{
			actuacionAsistenciaForm.setFechaJustificacion("");
			
		}

		if(descripcionBreve!=null)
			actuacionAsistenciaForm.setDescripcionBreve(descripcionBreve);
		if(lugar!=null)
			actuacionAsistenciaForm.setLugar(lugar);
		if(numeroAsunto!=null)
			actuacionAsistenciaForm.setNumeroAsunto(numeroAsunto);
		if(anulacion!=null && !anulacion.equals(""))
			actuacionAsistenciaForm.setAnulacion(anulacion.toString());
		else
			actuacionAsistenciaForm.setAnulacion(ClsConstants.DB_FALSE);
		if(observacionesJustificacion!=null)
			actuacionAsistenciaForm.setObservacionesJustificacion(observacionesJustificacion);
		if(observaciones!=null)
			actuacionAsistenciaForm.setObservaciones(observaciones);
		if(facturado!=null)
			actuacionAsistenciaForm.setFacturado(facturado);
		else
			actuacionAsistenciaForm.setFacturado(ClsConstants.DB_FALSE);
		if(pagado!=null)
			actuacionAsistenciaForm.setPagado(pagado);
		if(idFacturacion!=null && !idFacturacion.equals(""))
			actuacionAsistenciaForm.setIdFacturacion(idFacturacion.toString());
		if(idPrision!=null && !idPrision.equals("")){
			actuacionAsistenciaForm.setIdPrision(idPrision.toString());
			actuacionAsistenciaForm.setIdInstitucionPris(idInstitucionPrision.toString());
		}
		if(idComisaria!=null && !idComisaria.equals("")){
			actuacionAsistenciaForm.setIdComisaria(idComisaria.toString());
			actuacionAsistenciaForm.setIdInstitucionComis(idInstitucionComisaria.toString());
		}
		if(idJuzgado!=null && !idJuzgado.equals("")){
			actuacionAsistenciaForm.setIdJuzgado(idJuzgado.toString());
			actuacionAsistenciaForm.setIdInstitucionJuzg(idInstitucionJuzgado.toString());
		}
		
		if(NIG!=null && !NIG.equals("")){
			actuacionAsistenciaForm.setNig(NIG);
		}
		
		if(validada!=null)
			actuacionAsistenciaForm.setValidada(validada);
		else
			actuacionAsistenciaForm.setValidada(ClsConstants.DB_FALSE);

		return actuacionAsistenciaForm;
	}

	public String getNIG() {
		return NIG;
	}
	public void setNIG(String nIG) {
		NIG = nIG;
	}
	
	

}