/*
 * Fecha creación: 17/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
* Maneja el formulario que mantiene la tabla SCS_EJGFORM
*/
 public class DefinirMantenimDictamenesEJGForm extends MasterForm{

	public String getDescripcion() {							return UtilidadesHash.getString(this.datos,ScsDictamenEJGBean.C_DESCRIPCION);}

	public void setDescripcion(String descripcion) {			UtilidadesHash.set(this.datos, ScsDictamenEJGBean.C_DESCRIPCION, descripcion);}

 	public String getAbreviatura() {							return UtilidadesHash.getString(this.datos,ScsDictamenEJGBean.C_ABREVIATURA);}

	public void setAbreviatura(String abreviatura) {			UtilidadesHash.set(this.datos, ScsDictamenEJGBean.C_ABREVIATURA, abreviatura);}
 	
	public String getCodigoExt() {							return UtilidadesHash.getString(this.datos,ScsDictamenEJGBean.C_CODIGOEXT);}

	public void setCodigoExt(String codigoExt) {			UtilidadesHash.set(this.datos, ScsDictamenEJGBean.C_CODIGOEXT, codigoExt);}
	
	
	
	public String getIdDictamen() {							return UtilidadesHash.getString(this.datos,ScsDictamenEJGBean.C_IDDICTAMEN);}

	public void setIdDictamen(String idDictamen) {			UtilidadesHash.set(this.datos, ScsDictamenEJGBean.C_IDDICTAMEN, idDictamen);}

	public String getIdFundamento() {						return UtilidadesHash.getString(this.datos,ScsDictamenEJGBean.C_IDFUNDAMENTO);}

	public void setIdFundamento(String idFundamento) {	UtilidadesHash.set(this.datos, ScsDictamenEJGBean.C_IDFUNDAMENTO, idFundamento);}

	public String getIdTipoDictamen() {						return UtilidadesHash.getString(this.datos,ScsDictamenEJGBean.C_IDTIPODICTAMEN);}

	public void setIdTipoDictamen(String idTipoDictamen) {	UtilidadesHash.set(this.datos, ScsDictamenEJGBean.C_IDTIPODICTAMEN, idTipoDictamen);}
 
 }
 