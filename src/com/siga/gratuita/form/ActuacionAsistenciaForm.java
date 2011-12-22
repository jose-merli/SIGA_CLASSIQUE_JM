package com.siga.gratuita.form;

/**
 * @author jorgeta
 */
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsComisariaBean;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsPrisionBean;
import com.siga.beans.ScsTipoActuacionBean;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.MasterForm;

public class ActuacionAsistenciaForm extends MasterForm 
{

      String idInstitucion; 
	  String anio;   
	  String numero;               
	  String idActuacion;        
	  String fecha;              
	  String diaDespues;         
	  String acuerdoExtrajudicial;  
	  String fechaJustificacion;    
	  String descripcionBreve;      
	  String lugar;                 
	  String numeroAsunto;          
	  String anulacion;             
	  String observacionesJustificacion;
	  String observaciones;             
	  String facturado;                 
	  String pagado;                    
	  String idFacturacion;             
	  String idInstitucionPris;        
	  String idPrision;                 
	  String idInstitucionComis;       
	  String idComisaria;           
	  String idInstitucionJuzg;    
	  String idJuzgado;             
	  String validada;              
	  String idTipoAsistencia;      
	  String idTipoActuacion;   

	  String descripcionActuacion;
	  String nombreFacturacion;
	  String fechaHoraAsistencia;
	  String letradoActuaciones;
	  
	  String fichaColegial;
	  boolean isLetrado= true;
	  String botones="";
	  String tipoPcajg="";
	  String numeroProcedimientoAsistencia;
	  String numeroDiligenciaAsistencia;
	  String comisariaAsistencia;
	  String juzgadoAsistencia;
	  String modo="";
	  String fechaAnulacionAsistencia;
	  List<ScsTipoActuacionBean> tiposActuacion;
	  List<ValueKeyVO> tipoCosteFijoActuaciones;
	  String idCosteFijoActuacion;
	  List<ScsComisariaBean> comisarias;
	   List<ScsJuzgadoBean> juzgados;	  
	   List<ScsPrisionBean> prisiones;
	private String modoPestanha=null;
		private String nig;
	
	/**
	 * @return Returns the modoPestanha.
	 */
	public String getModoPestanha() {
		return modoPestanha;
	}
	/**
	 * @param modoPestanha The modoPestanha to set.
	 */
	public void setModoPestanha(String modoPestanha) {
		this.modoPestanha = modoPestanha;
	}
	
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getIdActuacion() {
		return idActuacion;
	}
	public void setIdActuacion(String idActuacion) {
		this.idActuacion = idActuacion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getDiaDespues() {
		return diaDespues;
	}
	public void setDiaDespues(String diaDespues) {
		this.diaDespues = diaDespues;
	}
	public String getAcuerdoExtrajudicial() {
		return acuerdoExtrajudicial;
	}
	public void setAcuerdoExtrajudicial(String acuerdoExtrajudicial) {
		this.acuerdoExtrajudicial = acuerdoExtrajudicial;
	}
	
	public String getDescripcionBreve() {
		return descripcionBreve;
	}
	public void setDescripcionBreve(String descripcionBreve) {
		this.descripcionBreve = descripcionBreve;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getNumeroAsunto() {
		return numeroAsunto;
	}
	public void setNumeroAsunto(String numeroAsunto) {
		this.numeroAsunto = numeroAsunto;
	}
	public String getAnulacion() {
		return anulacion;
	}
	public void setAnulacion(String anulacion) {
		this.anulacion = anulacion;
	}
	public String getObservacionesJustificacion() {
		return observacionesJustificacion;
	}
	public void setObservacionesJustificacion(String observacionesJustificacion) {
		this.observacionesJustificacion = observacionesJustificacion;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getFacturado() {
		return facturado;
	}
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}
	public String getPagado() {
		return pagado;
	}
	public void setPagado(String pagado) {
		this.pagado = pagado;
	}
	public String getIdFacturacion() {
		return idFacturacion;
	}
	public void setIdFacturacion(String idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	public String getIdInstitucionPris() {
		return idInstitucionPris;
	}
	public void setIdInstitucionPris(String idInstitucionPris) {
		this.idInstitucionPris = idInstitucionPris;
	}
	public String getIdPrision() {
		return idPrision;
	}
	public void setIdPrision(String idPrision) {
		this.idPrision = idPrision;
	}
	public String getIdInstitucionComis() {
		return idInstitucionComis;
	}
	public void setIdInstitucionComis(String idInstitucionComis) {
		this.idInstitucionComis = idInstitucionComis;
	}
	public String getIdComisaria() {
		return idComisaria;
	}
	public void setIdComisaria(String idComisaria) {
		this.idComisaria = idComisaria;
	}
	public String getIdInstitucionJuzg() {
		return idInstitucionJuzg;
	}
	public void setIdInstitucionJuzg(String idInstitucionJuzg) {
		this.idInstitucionJuzg = idInstitucionJuzg;
	}
	public String getIdJuzgado() {
		return idJuzgado;
	}
	public void setIdJuzgado(String idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	public String getValidada() {
		return validada;
	}
	public void setValidada(String validada) {
		this.validada = validada;
	}
	public String getIdTipoAsistencia() {
		return idTipoAsistencia;
	}
	public void setIdTipoAsistencia(String idTipoAsistencia) {
		this.idTipoAsistencia = idTipoAsistencia;
	}
	public String getIdTipoActuacion() {
		return idTipoActuacion;
	}
	public void setIdTipoActuacion(String idTipoActuacion) {
		this.idTipoActuacion = idTipoActuacion;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	public ScsActuacionAsistenciaBean getActuacionAsistenciaVO() {
		ScsActuacionAsistenciaBean	actuacionAsistenciaBean  = new ScsActuacionAsistenciaBean();
		
/*IDINSTITUCION              NUMBER(4) not null,
  ANIO                       NUMBER(4) not null,
  NUMERO                     NUMBER(10) not null,
  IDACTUACION                NUMBER(10) not null,
  FECHA                      DATE not null,
  DIADESPUES                 VARCHAR2(1) default 'N' not null,
  ACUERDOEXTRAJUDICIAL       NUMBER(1) not null,
  FECHAMODIFICACION          DATE not null,
  USUMODIFICACION            NUMBER(5) not null,
  FECHAJUSTIFICACION         DATE,
  DESCRIPCIONBREVE           VARCHAR2(1024),
  LUGAR                      VARCHAR2(100),
  NUMEROASUNTO               VARCHAR2(20),
  ANULACION                  NUMBER(1),
  OBSERVACIONESJUSTIFICACION VARCHAR2(1024),
  OBSERVACIONES              VARCHAR2(4000),
  FACTURADO                  VARCHAR2(1),
  PAGADO                     VARCHAR2(1),
  IDFACTURACION              NUMBER(5),
  IDINSTITUCION_PRIS         NUMBER(4),
  IDPRISION                  NUMBER(10),
  IDINSTITUCION_COMIS        NUMBER(4),
  IDCOMISARIA                NUMBER(10),
  IDINSTITUCION_JUZG         NUMBER(4),
  IDJUZGADO                  NUMBER(10),
  VALIDADA                   VARCHAR2(1),
  IDTIPOASISTENCIA           NUMBER(3) not null,
  IDTIPOACTUACION            NUMBER(3) not null*/		
		
			actuacionAsistenciaBean.setIdInstitucion(new Integer(idInstitucion));
			if(anio!=null&&!anio.equals(""))
				actuacionAsistenciaBean.setAnio(new Integer(anio));
			if(numero!=null&&!numero.equals(""))
				actuacionAsistenciaBean.setNumero(new Long(numero));
			
			if(idActuacion!=null&&!idActuacion.equals(""))
				actuacionAsistenciaBean.setIdActuacion(new Long(idActuacion));
			try {
				actuacionAsistenciaBean.setFecha(GstDate.getApplicationFormatDate("", fecha) );
			} catch (ClsExceptions e1) {}
			actuacionAsistenciaBean.setDiaDespues(diaDespues);
			if(acuerdoExtrajudicial!=null && !acuerdoExtrajudicial.equals(""))
				actuacionAsistenciaBean.setAcuerdoExtrajudicial(new Integer(acuerdoExtrajudicial));
			if(idTipoActuacion!=null && !idTipoActuacion.equals(""))
				actuacionAsistenciaBean.setIdTipoActuacion(new Integer(idTipoActuacion));
			if(idTipoAsistencia!=null && !idTipoAsistencia.equals(""))
			actuacionAsistenciaBean.setIdTipoAsistencia(new Integer(idTipoAsistencia));
			
			if(fechaJustificacion!=null&& !fechaJustificacion.equals("")){
				try {
					if(!fechaJustificacion.equals("sysdate"))
						actuacionAsistenciaBean.setFechaJustificacion(GstDate.getApplicationFormatDate("", fechaJustificacion) );
					else
						actuacionAsistenciaBean.setFechaJustificacion(fechaJustificacion);
				} catch (ClsExceptions e1) {}
			}
			
			
			actuacionAsistenciaBean.setDescripcionBreve(descripcionBreve);
			actuacionAsistenciaBean.setLugar(lugar);
			actuacionAsistenciaBean.setNumeroAsunto(numeroAsunto);
			if(anulacion!=null && !anulacion.equals(""))
				actuacionAsistenciaBean.setAnulacion(new Integer(anulacion));
			actuacionAsistenciaBean.setObservacionesJustificacion(observacionesJustificacion);
			actuacionAsistenciaBean.setObservaciones(observaciones);
			actuacionAsistenciaBean.setFacturado(facturado);
			actuacionAsistenciaBean.setPagado(pagado);
			if(idFacturacion!=null && !idFacturacion.equals(""))
				actuacionAsistenciaBean.setIdFacturacion(new Integer(idFacturacion));
			if(idPrision!=null && !idPrision.equals("")){
				actuacionAsistenciaBean.setIdPrision(new Integer(idPrision));
				actuacionAsistenciaBean.setIdInstitucionPrision(new Long(idInstitucionPris));
			}
			if(idComisaria!=null && !idComisaria.equals("")){
				actuacionAsistenciaBean.setIdComisaria(new Integer(idComisaria));
				actuacionAsistenciaBean.setIdInstitucionComisaria(new Long(idInstitucion));
			}
			if(idJuzgado!=null && !idJuzgado.equals("")){
				actuacionAsistenciaBean.setIdJuzgado(new Integer(idJuzgado));
				actuacionAsistenciaBean.setIdInstitucionJuzgado(new Long(idInstitucion));
			}
			
			if(nig!=null && !nig.equals("")){
				actuacionAsistenciaBean.setNIG(nig);
			}
			actuacionAsistenciaBean.setValidada(validada);
		return actuacionAsistenciaBean;
	}
	public String getDescripcionActuacion() {
		return descripcionActuacion;
	}
	public void setDescripcionActuacion(String descripcionActuacion) {
		this.descripcionActuacion = descripcionActuacion;
	}
	public String getNombreFacturacion() {
		return nombreFacturacion;
	}
	public void setNombreFacturacion(String nombreFacturacion) {
		this.nombreFacturacion = nombreFacturacion;
	}
	
	public String getLetradoActuaciones() {
		return letradoActuaciones;
	}
	public void setLetradoActuaciones(String letradoActuaciones) {
		this.letradoActuaciones = letradoActuaciones;
	}
	public String getFichaColegial() {
		return fichaColegial;
	}
	public void setFichaColegial(String fichaColegial) {
		this.fichaColegial = fichaColegial;
	}
	public String getFechaHoraAsistencia() {
		return fechaHoraAsistencia;
	}
	public void setFechaHoraAsistencia(String fechaHoraAsistencia) {
		this.fechaHoraAsistencia = fechaHoraAsistencia;
	}
	public String getBotones() {
		if(modoPestanha!=null && modoPestanha.equalsIgnoreCase("ver")){
			botones = "C";
		}else{
			if(fechaAnulacionAsistencia!=null && !fechaAnulacionAsistencia.equals("")||(idFacturacion!=null && !idFacturacion.equals(""))){
				botones = "C";
			}else if(isLetrado&&((validada!=null && validada.equals("1"))||(anulacion!=null && anulacion.equals("1")))){
				botones = "C";
			
			}else{
				if(fichaColegial!=null && fichaColegial.equals("0")){
					botones = "B,C,E";
				}else{
					if(letradoActuaciones!=null && letradoActuaciones.equals("1"))
						botones = "B,C,E";
					else
						botones = "C";
				}
			}
		}
		return botones;
	}
	
	public String getTipoPcajg() {
		return tipoPcajg;
	}
	public void setTipoPcajg(String tipoPcajg) {
		this.tipoPcajg = tipoPcajg;
	}
	public String getModo() {
		return modo;
	}
	public void setModo(String modo) {
		this.modo = modo;
	}
	public String getFechaAnulacionAsistencia() {
		return fechaAnulacionAsistencia;
	}
	public void setFechaAnulacionAsistencia(String fechaAnulacionAsistencia) {
		this.fechaAnulacionAsistencia = fechaAnulacionAsistencia;
	}
	
	
	public String getIdCosteFijoActuacion() {
		return idCosteFijoActuacion;
	}
	public void setIdCosteFijoActuacion(String idCosteFijoActuacion) {
		this.idCosteFijoActuacion = idCosteFijoActuacion;
	}
	public List<ValueKeyVO> getTipoCosteFijoActuaciones() {
		return tipoCosteFijoActuaciones;
	}
	public void setTipoCosteFijoActuaciones(
			List<ValueKeyVO> tipoCosteFijoActuaciones) {
		this.tipoCosteFijoActuaciones = tipoCosteFijoActuaciones;
	}
	public List<ScsComisariaBean> getComisarias() {
		return comisarias;
	}
	public void setComisarias(List<ScsComisariaBean> comisarias) {
		this.comisarias = comisarias;
	}
	public List<ScsJuzgadoBean> getJuzgados() {
		return juzgados;
	}
	public void setJuzgados(List<ScsJuzgadoBean> juzgados) {
		this.juzgados = juzgados;
	}
	public List<ScsPrisionBean> getPrisiones() {
		return prisiones;
	}
	public void setPrisiones(List<ScsPrisionBean> prisiones) {
		this.prisiones = prisiones;
	}
	public List<ScsTipoActuacionBean> getTiposActuacion() {
		return tiposActuacion;
	}
	public void setTiposActuacion(List<ScsTipoActuacionBean> tiposActuacion) {
		this.tiposActuacion = tiposActuacion;
	}
	
	public String getNumeroProcedimientoAsistencia() {
		return numeroProcedimientoAsistencia;
	}
	public void setNumeroProcedimientoAsistencia(
			String numeroProcedimientoAsistencia) {
		this.numeroProcedimientoAsistencia = numeroProcedimientoAsistencia;
	}
	public String getNumeroDiligenciaAsistencia() {
		return numeroDiligenciaAsistencia;
	}
	public void setNumeroDiligenciaAsistencia(String numeroDiligenciaAsistencia) {
		this.numeroDiligenciaAsistencia = numeroDiligenciaAsistencia;
	}
	public String getComisariaAsistencia() {
		return comisariaAsistencia;
	}
	public void setComisariaAsistencia(String comisariaAsistencia) {
		this.comisariaAsistencia = comisariaAsistencia;
	}
	public String getJuzgadoAsistencia() {
		return juzgadoAsistencia;
	}
	public void setJuzgadoAsistencia(String juzgadoAsistencia) {
		this.juzgadoAsistencia = juzgadoAsistencia;
	}
	public boolean isLetrado() {
		return isLetrado;
	}
	public void setLetrado(boolean isLetrado) {
		this.isLetrado = isLetrado;
	}
	
	public String getNig() {
		return nig;
	}
	public void setNig(String nig) {
		this.nig = nig;
	}
	
	
	
	
	
}