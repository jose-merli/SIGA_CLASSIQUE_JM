package com.siga.gratuita.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.MasterBean;
import com.siga.gratuita.form.ProgrCalendariosForm;

	public class ScsProgCalendariosBean extends MasterBean{
		static public final Short estadoProgramado = new Short("0"); 
		static public final Short estadoProcesando = new Short("1");
		static public final Short estadoError = new Short("2");
		static public final Short estadoFinalizado = new Short("3");
		static public final Short estadoCancelado = new Short("4");
		static public final Short estadoReprogramado = new Short("5");

		static public String T_NOMBRETABLA = "SCS_PROG_CALENDARIOS";
		static public final String SEQ_SCS_PROG_CALENDARIOS = "SEQ_SCSPROGCALENDARIOS";
		UsrBean usrBean;

		// Nombre campos de la tabla
		/*

  FECHA_GENERACION  DATE not null,
         DATE not null,
  FECHAFIN          DATE not null,
  ESTADO 
		 * */
		
		static public final String C_IDPROGCALENDARIO = "IDPROGCALENDARIO";
		static public final String C_IDCONJUNTOGUARDIA = "IDCONJUNTOGUARDIA";
		static public final String C_IDINSTITUCION = "IDINSTITUCION";
		
		static public final String C_FECHA_PROGRAMACION = "FECHAPROGRAMACION";
		static public final String C_FECHACALINICIO = "FECHACALINICIO";
		static public final String C_FECHACALFIN = "FECHACALFIN";
		static public final String C_ESTADO = "ESTADO";
		static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
		static public final String C_USUMODIFICACION = "USUMODIFICACION";
		
		private Long idProgrCalendario;
		private Long idConjuntoGuardia;
		private Integer idInstitucion;
		private String fechaProgramacion;
		private Short estado;
		private String fechaCalInicio;
		private String fechaCalFin;
		public Long getIdConjuntoGuardia() {
			return idConjuntoGuardia;
		}
		public void setIdConjuntoGuardia(Long idConjuntoGuardia) {
			this.idConjuntoGuardia = idConjuntoGuardia;
		}
		public Integer getIdInstitucion() {
			return idInstitucion;
		}
		public void setIdInstitucion(Integer idInstitucion) {
			this.idInstitucion = idInstitucion;
		}
		
		public Long getIdProgrCalendario() {
			return idProgrCalendario;
		}
		public void setIdProgrCalendario(Long idProgrCalendario) {
			this.idProgrCalendario = idProgrCalendario;
		}
		public String getFechaProgramacion() {
			return fechaProgramacion;
		}
		public void setFechaProgramacion(String fechaProgramacion) {
			this.fechaProgramacion = fechaProgramacion;
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
		public ProgrCalendariosForm getProgrCalendariosForm() {
			ProgrCalendariosForm progrConjuntoGuardiasForm = new ProgrCalendariosForm();
			if(idConjuntoGuardia!=null)
				progrConjuntoGuardiasForm.setIdConjuntoGuardia(idConjuntoGuardia.toString());
			if(idInstitucion!=null)
				progrConjuntoGuardiasForm.setIdInstitucion(idInstitucion.toString());
			if(idProgrCalendario!=null)
				progrConjuntoGuardiasForm.setIdProgrCalendario(idProgrCalendario.toString());
			if(estado!=null)
				progrConjuntoGuardiasForm.setEstado(estado.toString());
			if(fechaProgramacion!=null)
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
					Date fechaProgramacionDate = simpleDateFormat.parse(fechaProgramacion);
					simpleDateFormat.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
	                String fechaProgramacionString = simpleDateFormat.format(fechaProgramacionDate);
	                simpleDateFormat.applyPattern("HH");
	                String horas = simpleDateFormat.format(fechaProgramacionDate);
	                simpleDateFormat.applyPattern("mm");
	                String minutos = simpleDateFormat.format(fechaProgramacionDate);
	                progrConjuntoGuardiasForm.setFechaProgramacion(fechaProgramacionString);
	                progrConjuntoGuardiasForm.setHoraProgramacion(horas);
	                progrConjuntoGuardiasForm.setMinutoProgramacion(minutos);
					
					
				} catch (ParseException e) {
				}
			if(fechaCalInicio!=null)
				try {
					progrConjuntoGuardiasForm.setFechaCalInicio(GstDate.getFormatedDateShort("",  fechaCalInicio));
				} catch (ClsExceptions e) {
					
				}
			if(fechaCalFin!=null)
				try {
					progrConjuntoGuardiasForm.setFechaCalFin(GstDate.getFormatedDateShort("",  fechaCalFin));
				} catch (ClsExceptions e) {
					
				}
			
			
			return progrConjuntoGuardiasForm;
		}
		public Short getEstado() {
			return estado;
		}
		public void setEstado(Short estado) {
			this.estado = estado;
		}
		
}