package com.siga.gratuita.form;

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
		idTipoRatificacionEJG 	= null;
		idFundamentoJuridico 	= null;
		guardaActa 		= false;
		guardaPonente 	= false;
		guardaFundamento 	= false;
		guardaRatificacion 	= false;
	}
}