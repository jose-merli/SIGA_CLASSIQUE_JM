package com.siga.gratuita.form;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.atos.utils.Validaciones;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.MasterForm;

/**
 * @author jose.barrientos
 * @version 19-10-2011
 */

public class ActaComisionForm extends MasterForm {

	private String numeroActa;
	private String anioActa;
	private String fechaResolucion;
	private String fechaReunion;
	private String horaIni, minuIni;
	private String horaFin, minuFin;
	private String miembros;
	private String observaciones;
	private String idInstitucion;
	private String idSecretario;
	private String idPresidente;
	private String idActa;
	private String seleccionados;
	private String pendientes;
	
	private String idPonente;
	private String idTipoRatificacionEJG;
	private String idFundamentoJuridico;
	
	private boolean guardaActa = false;
	private boolean guardaPonente = false;
	private boolean guardaFundamento = false;
	private boolean guardaRatificacion = false;

	public void setNumeroActa(String valor) 	{this.numeroActa = valor;}
	public void setAnioActa(String valor) 		{this.anioActa = valor;}
	public void setFechaResolucion(String valor){this.fechaResolucion = valor;}
	public void setFechaReunion(String valor) 	{this.fechaReunion = valor;}
	public void setHoraIni(String valor) 		{this.horaIni = valor;}
	public void setMinuIni(String valor) 		{this.minuIni = valor;}
	public void setHoraFin(String valor) 		{this.horaFin = valor;}
	public void setMinuFin(String valor) 		{this.minuFin = valor;}
	public void setMiembros(String valor) 		{this.miembros = valor;}
	public void setObservaciones(String valor) 	{this.observaciones = valor;}
	public void setIdInstitucion(String valor) 	{this.idInstitucion = valor;}
	public void setIdSecretario(String idSecretario) {this.idSecretario = idSecretario;}
	public void setIdPresidente(String idPresidente) {this.idPresidente = idPresidente;}
	public void setIdActa(String idActa) 		{this.idActa = idActa;}
	public void setSeleccionados(String valor) 	{this.seleccionados = valor;}
	public void setIdPonente(String valor) 		{this.idPonente = valor;}
	public void setIdTipoRatificacionEJG(String valor) 	{this.idTipoRatificacionEJG = valor;}
	public void setIdFundamentoJuridico(String valor) 	{this.idFundamentoJuridico = valor;}
	public void setGuardaActa(boolean valor) 	{this.guardaActa = valor;}
	public void setGuardaPonente(boolean valor) {this.guardaPonente = valor;}
	public void setGuardaFundamento(boolean valor) 		{this.guardaFundamento = valor;}
	public void setGuardaRatificacion(boolean valor)	{this.guardaRatificacion = valor;}
	public void setPendientes(String valor)	{this.pendientes = valor;}
	
	public String getNumeroActa() 		{return numeroActa;}
	public String getAnioActa() 		{return anioActa;}
	public String getFechaResolucion() 	{return fechaResolucion;}
	public String getFechaReunion() 	{return fechaReunion;}
	public String getHoraIni() 			{return horaIni;}
	public String getMinuIni() 			{return minuIni;}
	public String getHoraFin() 			{return horaFin;}
	public String getMinuFin() 			{return minuFin;}
	public String getMiembros() 		{return miembros;}
	public String getObservaciones()	{return observaciones;}
	public String getIdInstitucion()	{return idInstitucion;}
	public String getIdSecretario() 	{return idSecretario;}
	public String getIdPresidente() 	{return idPresidente;}
	public String getIdActa() 			{return idActa;}
	public String getSeleccionados() 	{return seleccionados;}
	public String getIdPonente() 		{return idPonente;}
	public String getIdTipoRatificacionEJG() 	{return idTipoRatificacionEJG;}
	public String getIdFundamentoJuridico() 	{return idFundamentoJuridico;}
	public boolean getGuardaActa() 		{return guardaActa;}
	public boolean getGuardaPonente() 	{return guardaPonente;}
	public boolean getGuardaFundamento() 	{return guardaFundamento;}
	public boolean getGuardaRatificacion() 	{return guardaRatificacion;}
	public String getPendientes() 	{return pendientes;}
	
	public void reset(){
		numeroActa		= null;
		anioActa		= null;
		fechaResolucion	= null;
		fechaReunion	= null;
		horaIni			= null;
		minuIni  		= null;
		horaFin			= null;
		minuFin			= null;
		miembros		= null;
		observaciones	= null;
		idInstitucion	= null;
		idSecretario	= null;
		idPresidente	= null;
		idActa			= null;
		seleccionados	= null;
		idPonente 		= null;
		pendientes 		= null;
		idTipoRatificacionEJG 	= null;
		idFundamentoJuridico 	= null;
		guardaActa 		= false;
		guardaPonente 	= false;
		guardaFundamento 	= false;
		guardaRatificacion 	= false;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request ) {		
		ActionErrors errors = new ActionErrors();
		UsrBean user = (UsrBean)request.getSession().getAttribute("USRBEAN");			

		try {
			if (this.numeroActa==null || this.numeroActa.equalsIgnoreCase(""))
				errors.add(UtilidadesString.getMensajeIdioma(user, "sjcs.actas.numeroActa"),new ActionMessage("errors.required"));
			
			if (this.anioActa==null || this.anioActa.equalsIgnoreCase(""))
				errors.add(UtilidadesString.getMensajeIdioma(user, "sjcs.actas.anio"),new ActionMessage("errors.required"));
			
			if (this.fechaResolucion!=null && !this.fechaResolucion.equalsIgnoreCase("") && this.fechaReunion!=null && !this.fechaReunion.equalsIgnoreCase("")) {
				Validaciones validator = new Validaciones();				
				String msg = "";
				if (!validator.validaFecha(this.fechaResolucion, msg, true) || !validator.validaFecha(this.fechaReunion, msg, true)) 
					errors.add(UtilidadesString.getMensajeIdioma(user, "sjcs.actas.fechaResolucion"),new ActionMessage("sjcs.actas.fechasErroneas"));
				else {
					Locale locale = new Locale(user.getLanguage());
					GstDate gstDate = new GstDate();					
					Date dFechaResolucion = gstDate.parseStringToDate(this.fechaResolucion, "dd/MM/yyyy", locale);
					Date dFechaReunion = gstDate.parseStringToDate(this.fechaReunion, "dd/MM/yyyy", locale);
					if (!dFechaResolucion.after(dFechaReunion))
						errors.add(UtilidadesString.getMensajeIdioma(user, "sjcs.actas.fechaResolucion"),new ActionMessage("sjcs.actas.fechasErroneas"));
				}
			}
			
			if (this.horaIni!=null && !this.horaIni.equalsIgnoreCase("") && Integer.parseInt(this.horaIni,10)>23) 
				errors.add(UtilidadesString.getMensajeIdioma(user, "sjcs.actas.horaInicio"),new ActionMessage("sjcs.actas.horaInicioError01"));
			
			if (this.minuIni!=null && !this.minuIni.equalsIgnoreCase("") && Integer.parseInt(this.minuIni,10)>59) 
				errors.add(UtilidadesString.getMensajeIdioma(user, "sjcs.actas.horaInicio"),new ActionMessage("sjcs.actas.horaInicioError02"));

			if (this.horaFin!=null && !this.horaFin.equalsIgnoreCase("") && Integer.parseInt(this.horaFin,10)>23)
				errors.add(UtilidadesString.getMensajeIdioma(user, "sjcs.actas.horaFin"),new ActionMessage("sjcs.actas.horaFinError01"));
			
			if (this.minuFin!=null && !this.minuFin.equalsIgnoreCase("") && Integer.parseInt(this.minuFin,10)>59) 
				errors.add(UtilidadesString.getMensajeIdioma(user, "sjcs.actas.horaFin"),new ActionMessage("sjcs.actas.horaFinError02"));
			
		}
		catch (Exception e) {
			errors.add(user.getUserName(), new ActionMessage(e.getMessage()));
		}	
		
		if (!errors.isEmpty()) {
			this.reset();
		}

		return errors;
	}	
}