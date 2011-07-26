package com.siga.gratuita.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.administracion.SIGAConstants;
import com.siga.general.MasterForm;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.tlds.FilaExtElement;

/**
 * 
 * @author jorgeta
 *
 */
public class ProgrCalendariosForm extends MasterForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idConjuntoGuardia;
	private String idProgrCalendario;
	private String idInstitucion;
	private String estado;
	private String fechaProgrDesde;
	private String fechaProgrHasta;
	private String fechaProgramacion;
	private String 	horaProgramacion;
	private String minutoProgramacion;
	ConjuntoGuardiasForm conjuntoGuardias;
	
	private String fechaCalInicio;
	private String fechaCalFin;
	private FilaExtElement[] elementosFila;
	
	private String botones;
	
	
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFechaProgrDesde() {
		return fechaProgrDesde;
	}
	public void setFechaProgrDesde(String fechaProgrDesde) {
		this.fechaProgrDesde = fechaProgrDesde;
	}
	public String getFechaProgrHasta() {
		return fechaProgrHasta;
	}
	public void setFechaProgrHasta(String fechaProgrHasta) {
		this.fechaProgrHasta = fechaProgrHasta;
	}
	public String getFechaCalInicio() {
		return fechaCalInicio;
	}
	public void setFechaCalInicio(String fechaCalInicio) {
		this.fechaCalInicio = fechaCalInicio;
	}
	public String getFechaCalFin() {
		return fechaCalFin;
	}
	public void setFechaCalFin(String fechaCalFin) {
		this.fechaCalFin = fechaCalFin;
	}
	public String getIdProgrCalendario() {
		return idProgrCalendario;
	}
	public void setIdProgrCalendario(String idProgrCalendario) {
		this.idProgrCalendario = idProgrCalendario;
	}
	public String getFechaProgramacion() {
		return fechaProgramacion;
	}
	public void setFechaProgramacion(String fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}
	
	
	public ScsProgCalendariosBean getProgCalendariosVO() {
		ScsProgCalendariosBean progCalendariosBean = new ScsProgCalendariosBean();
		if(idConjuntoGuardia!=null && !idConjuntoGuardia.equals(""))
			progCalendariosBean.setIdConjuntoGuardia(new Long(idConjuntoGuardia));
		
		if(idInstitucion!=null && !idInstitucion.equals(""))
			progCalendariosBean.setIdInstitucion(new Integer(idInstitucion));
		
		if(idProgrCalendario!=null && !idProgrCalendario.equals(""))
			progCalendariosBean.setIdProgrCalendario(new Long(idProgrCalendario));
		if(estado!=null  && !estado.equals(""))
			progCalendariosBean.setEstado(new Short(estado));
		if(fechaProgramacion!=null && !fechaProgramacion.equals("")){
			try {
				Calendar myCalendar = Calendar.getInstance();
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
				Date fechaProgramacionDate = simpleDateFormat.parse(fechaProgramacion);
				myCalendar.setTime(fechaProgramacionDate);
				myCalendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(horaProgramacion));
				myCalendar.set(Calendar.MINUTE,Integer.parseInt(minutoProgramacion));
				myCalendar.set(Calendar.SECOND,0);
				myCalendar.set(Calendar.MILLISECOND,0);
				simpleDateFormat = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				progCalendariosBean.setFechaProgramacion(simpleDateFormat.format(myCalendar.getTime()));
			}catch (ParseException e) {
			}
		}
		if(fechaCalInicio!=null&& !fechaCalInicio.equals(""))
			try {
				progCalendariosBean.setFechaCalInicio(GstDate.getApplicationFormatDate("", fechaCalInicio) );
			} catch (ClsExceptions e1) {}
		if(fechaCalFin!=null&& !fechaCalFin.equals(""))
			try {
				progCalendariosBean.setFechaCalFin(GstDate.getApplicationFormatDate("", fechaCalFin) );
			} catch (ClsExceptions e) {}
		
		
		return progCalendariosBean;
	}
	public ConjuntoGuardiasForm getConjuntoGuardias() {
		return conjuntoGuardias;
	}
	public void setConjuntoGuardias(ConjuntoGuardiasForm conjuntoGuardias) {
		this.conjuntoGuardias = conjuntoGuardias;
	}
	
	
//	static public final Short estadoProgramado = new Short("0"); 
//	static public final Short estadoProcesando = new Short("1");
//	static public final Short estadoError = new Short("2");
//	static public final Short estadoFinalizado = new Short("3");
//	static public final Short estadoCancelado = new Short("4");
//	static public final Short estadoReprogramado = new Short("5");
	
	public FilaExtElement[] getElementosFila() {

		switch (Integer.parseInt(estado)) {
		
		case 0:
//			botones  = "B,E";
			elementosFila = new FilaExtElement[3];
			elementosFila[2]=new FilaExtElement("generar",  "adelantarProgrCalendarios", "gratuita.calendarios.programacion.estado.programar.accion", SIGAConstants.ACCESS_READ);

			break;
		case 1 : 
//			botones  = "C";
			elementosFila = new FilaExtElement[2];
			elementosFila[1]=new FilaExtElement("cancelar",  "cancelarGeneracionCalendarios", "gratuita.calendarios.programacion.estado.cancelar.accion", SIGAConstants.ACCESS_READ);
			break;
		case 2:case 4:
//			botones  = "B,C";
			elementosFila = new FilaExtElement[3];
			elementosFila[2]=new FilaExtElement("generar","reprogramarCalendarios", "gratuita.calendarios.programacion.estado.reprogramar.accion", SIGAConstants.ACCESS_READ);
	
			break;
		case 3:
			
//			botones  = "B,C";
			elementosFila = new FilaExtElement[2];
			
			break;
		case 5:
//			botones  = "B,C";
			elementosFila = new FilaExtElement[2];

			break;
		default:
			break;
		}
		return elementosFila;
	}
	
	public String getBotones() {
//		<html:option value="0">Programada</html:option>
//		<html:option value="1">En proceso</html:option>
//		<html:option value="2">Generada con Errores</html:option>
//		<html:option value="3">Generada</html:option>
		switch (Integer.parseInt(estado)) {
		
		case 0:
			botones  = "B,E";
			break;
		case 1 :
			botones  = "C";
			break;
		case 2: case 3:case 4:case 5:
			botones  = "B,C";
			break;
		default:
			break;
		}
		return botones;
	}
	public String getHoraProgramacion() {
		return horaProgramacion;
	}
	public void setHoraProgramacion(String horaProgramacion) {
		this.horaProgramacion = horaProgramacion;
	}
	public String getMinutoProgramacion() {
		return minutoProgramacion;
	}
	public void setMinutoProgramacion(String minutoProgramacion) {
		this.minutoProgramacion = minutoProgramacion;
	}
	public String getFechaProgramacionConHoras() {
		return fechaProgramacion+" "+horaProgramacion+":"+minutoProgramacion;
	}
	public String getIdConjuntoGuardia() {
		return idConjuntoGuardia;
	}
	public void setIdConjuntoGuardia(String idConjuntoGuardia) {
		this.idConjuntoGuardia = idConjuntoGuardia;
	}
	public void clear() {
		fechaProgrDesde="";
		fechaProgrHasta="";
		fechaCalInicio="";
		fechaCalFin="";
		idConjuntoGuardia="";
		estado="";
		
	}

	
	
	
}
