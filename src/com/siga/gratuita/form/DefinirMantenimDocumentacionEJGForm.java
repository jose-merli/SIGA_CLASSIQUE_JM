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
public class DefinirMantenimDocumentacionEJGForm extends MasterForm{

public String getDescripcion() {									return UtilidadesHash.getString(this.datos,ScsTipoDocumentoEJGBean.C_DESCRIPCION);}

public void setDescripcion(String descripcion) {					UtilidadesHash.set(this.datos, ScsTipoDocumentoEJGBean.C_DESCRIPCION, descripcion);}

public String getAbreviatura() {									return UtilidadesHash.getString(this.datos,ScsTipoDocumentoEJGBean.C_ABREVIATURA);}

public void setAbreviatura(String abreviatura) {					UtilidadesHash.set(this.datos, ScsTipoDocumentoEJGBean.C_ABREVIATURA, abreviatura);}

public String getCodigoExt() {									    return UtilidadesHash.getString(this.datos,ScsTipoDocumentoEJGBean.C_CODIGOEXT);}

public void setCodigoExt(String codigoExt) {					   UtilidadesHash.set(this.datos, ScsTipoDocumentoEJGBean.C_CODIGOEXT, codigoExt);}
 	
public String getDocumento() {										return UtilidadesHash.getString(this.datos,ScsDocumentacionEJGBean.C_IDDOCUMENTO);}

public void setDocumento(String documento) {						UtilidadesHash.set(this.datos, ScsDocumentacionEJGBean.C_IDDOCUMENTO, documento);}

public String getTipoDocumento() {									return UtilidadesHash.getString(this.datos,ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO);}

public void setTipoDocumento(String tipoDocumento) {				UtilidadesHash.set(this.datos, ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO, tipoDocumento);}
	
public String getDocumentoAbreviado() { 							return UtilidadesHash.getString(this.datos,"DOCUMENTOABREVIADO");}
	
public void setDocumentoAbreviado(String DocumentoAbreviado){ 		UtilidadesHash.set(this.datos,"DOCUMENTOABREVIADO", DocumentoAbreviado);}

public String getDoc(){												return UtilidadesHash.getString(this.datos,"DESCRIPCIONDOC");}

public void setDoc(String Doc){										UtilidadesHash.set(this.datos, "DESCRIPCIONDOC", Doc);}
}