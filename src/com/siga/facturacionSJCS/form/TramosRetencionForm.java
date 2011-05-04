
package com.siga.facturacionSJCS.form;
import java.util.List;

import com.siga.Utilidades.UtilidadesNumero;
import com.siga.beans.FcsTramosRetencionBean;
import com.siga.general.MasterForm;

/**
 * 
 * @author jorgeta
 *
 */
public class TramosRetencionForm extends MasterForm {
	
	private String 	anio;
	
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	private String 	porcentaje;
	private String	numeroTramo;
	private String	idInstitucion;
	private String	descripcion;
	private String 	numerosSmi;
	private String 	numeroMeses;
	private String 	importe;
	private String 	importeRetencion;
	
	public String getImporteRetencion() {
		return importeRetencion;
	}
	public void setImporteRetencion(String importeRetencion) {
		this.importeRetencion = importeRetencion;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getLimiteTramo() {
		
		if(smi!=null && !smi.equals("") && numeroMeses!=null && !numeroMeses.equals("") && numerosSmi!=null && !numerosSmi.equals("") &&  !numerosSmi.equals("-") )
			return UtilidadesNumero.formato(UtilidadesNumero.getDouble(smi)*Integer.parseInt(numeroMeses)*Integer.parseInt(numerosSmi));
		else if(numerosSmi==null || numerosSmi.equals("-")|| numerosSmi.equals("")){
			
			return "Sin límite";
		}
		else
		return "";
	}
	
	List<TramosRetencionForm> tramosRetencion;
	public List<TramosRetencionForm> getTramosRetencion() {
		return tramosRetencion;
	}
	public void setTramosRetencion(List<TramosRetencionForm> tramosRetencion) {
		this.tramosRetencion = tramosRetencion;
	}
	public String getNumeroMeses() {
		return numeroMeses;
	}
	public void setNumeroMeses(String numeroMeses) {
		this.numeroMeses = numeroMeses;
	}
	private String 	smi;
	public String getSmi() {
		return smi;
	}
	public void setSmi(String smi) {
		this.smi = smi;
	}
	public String getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	public String getNumeroTramo() {
		return numeroTramo;
	}
	public void setNumeroTramo(String numeroTramo) {
		this.numeroTramo = numeroTramo;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNumerosSmi() {
		return numerosSmi;
	}
	public void setNumerosSmi(String numerosSmi) {
		this.numerosSmi = numerosSmi;
	}
	
	
	 

}