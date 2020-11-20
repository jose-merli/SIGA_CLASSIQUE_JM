package com.siga.informes.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

public class MantenimientoInformesForm extends MasterForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4793556503269961472L;
	// Atributos
	private String idInstitucion;
	private String letrado;
	private String numeroNifTagBusquedaPersonas;
	private String nombrePersona;
	private String interesadoNombre;
	private String interesadoApellido1;
	private String interesadoApellido2;
	private String interesadoNif;
	private String idPersona;
	String[] parametrosComboPago = null;
	String[] parametrosComboPagoFin = null;
	private String nombrePago, abreviatura;

	
	// Getters
	public String getIdInstitucion() {return idInstitucion;}
	public String getLetrado() {return letrado;}
	public String getNumeroNifTagBusquedaPersonas() {return numeroNifTagBusquedaPersonas;}
	public String getNombrePersona() {return nombrePersona;}
	public String getInteresadoNombre() {return interesadoNombre;}
	public String getInteresadoApellido1() {return interesadoApellido1;}
	public String getInteresadoApellido2() {return interesadoApellido2;}
	public String getInteresadoNif() {return interesadoNif;}
	public String getIdPersona() {return idPersona;}
	public String[] getParametrosComboPago() {return parametrosComboPago;}
	public String[] getParametrosComboPagoFin() {return parametrosComboPagoFin;}
	public String getNombrePago() {return nombrePago;}
	public String getAbreviatura() {return abreviatura;}
	
	public String getIdioma() {return UtilidadesHash.getString(this.datos, "IDIOMA");}
	public String getIdPago() {return UtilidadesHash.getString(this.datos, "IDPAGO");}
	public String getIdPagoFinal() {return UtilidadesHash.getString(this.datos, "IDPAGOFINAL");}
	public String getIdFacturacion() {return UtilidadesHash.getString(this.datos, "IDFACTURACION");}
	public String getIdFacturacionFinal() {return UtilidadesHash.getString(this.datos, "IDFACTURACIONFINAL");}
	public String getNombreFichero() {return UtilidadesHash.getString(this.datos, "NOMBREFICHERO");}
	public String getRutaFichero() {return UtilidadesHash.getString(this.datos, "RUTAFICHERO");}
	public String getTipoInforme() {return UtilidadesHash.getString(this.datos, "TIPOINFORME");}
	
	
	// Setters
	public void setIdInstitucion(String valor) {this.idInstitucion = valor;}
	public void setLetrado(String valor) {this.letrado = valor;}
	public void setNumeroNifTagBusquedaPersonas(String valor) {this.numeroNifTagBusquedaPersonas = valor;}
	public void setNombrePersona(String valor) {this.nombrePersona = valor;}
	public void setInteresadoNombre(String valor) {this.interesadoNombre = valor;}
	public void setInteresadoApellido1(String valor) {this.interesadoApellido1 = valor;}
	public void setInteresadoApellido2(String valor) {this.interesadoApellido2 = valor;}
	public void setInteresadoNif(String valor) {this.interesadoNif = valor;}
	public void setIdPersona(String valor) {this.idPersona = valor;}
	public void setParametrosComboPago(String[] valor) {this.parametrosComboPago = valor;}
	public void setParametrosComboPagoFin(String[] valor) {this.parametrosComboPagoFin = valor;}
	public void setNombrePago(String nombrePago) {this.nombrePago = nombrePago;}
	public void setAbreviatura(String abreviatura) {this.abreviatura = abreviatura;}
	
	public void setIdioma(String valor) {UtilidadesHash.set(this.datos, "IDIOMA", valor);}
	public void setIdPago(String valor) {UtilidadesHash.set(this.datos, "IDPAGO", valor);}
	public void setIdFacturacionFinal(String valor) {UtilidadesHash.set(this.datos, "IDFACTURACIONFINAL", valor);}
	public void setIdPagoFinal(String valor) {UtilidadesHash.set(this.datos, "IDPAGOFINAL", valor);}
	public void setIdFacturacion(String valor) {UtilidadesHash.set(this.datos, "IDFACTURACION", valor);}
	public void setNombreFichero(String valor) {UtilidadesHash.set(this.datos, "NOMBREFICHERO", valor);}
	public void setRutaFichero(String valor) {UtilidadesHash.set(this.datos, "RUTAFICHERO", valor);}
	public void setTipoInforme(String valor) {UtilidadesHash.set(this.datos, "TIPOINFORME", valor);}
}
