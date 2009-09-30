//VERSIONES
//david.sanchezp Creacion: 21-03-2005
//

package com.siga.facturacionSJCS.form;

import java.util.Vector;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ColegiadosPagosBean;
import com.siga.beans.CriteriosPagosBean;
import com.siga.general.MasterForm;

public class DatosGeneralesPagoForm extends MasterForm {

	static public final String accion = "ACCION";
	static public final String nombre = "NOMBRE";
	static public final String abreviatura = "ABREVIATURA";
	static public final String estado = "ESTADO";
	static public final String fechaDesde = "FECHADESDE";
	static public final String fechaHasta = "FECHAHASTA";
	static public final String fechaEstado = "FECHAESTADO";
	static public final String idPagosJG = "IDPAGOSJG";
	static public final String idFacturacion = "IDFACTURACION";
	static public final String importeRepartir = "IMPORTEREPARTIR";
	static public final String PorcentajeOficio = "PORCENTAJEOFICIO";
	static public final String PorcentajeGuardias = "PORCENTAJEGUARDIAS";
	static public final String PorcentajeEJG = "PORCENTAJEEJG";
	static public final String PorcentajeSOJ = "PORCENTAJESOJ";
	static public final String ImporteOficio = "IMPORTEOFICIO";
	static public final String ImporteGuardias = "IMPORTEGUARDIAS";
	static public final String ImporteEJG = "IMPORTEEJG";
	static public final String ImporteSOJ = "IMPORTESOJ";
	static public final String idInstitucion = "IDINSTITUCION";
	static public final String criterios = "CRITERIOS";
	static public final String criterioPago = "CRITERIOPAGO";
	static public final String criterioPagoTurno = "CRITERIOPAGOTURNO";
	static public final String idEstadoPagosJG = "IDESTADOPAGOSJG";
	static public final String comboFacturacion = "COMBOFACTURACION";
	static public final String valoresFacturacion = "VALORESFACTURACION";
	static public final String cobroAutomatico = "COBROAUTOMATICO";
	static public final String accionPrevia = "ACCIONPREVIA";
	static public final String idPersona = "IDPERSONA";
	static public final String colegiado = "COLEGIADO";
	static public final String nColegiado = "NCOLEGIADO";
	static public final String importeFacturado = "IMPORTEFACTURADO";
	static public final String importePagado = "IMPORTEPAGADO";
	
	//Para los criterios del iframe:
	private Vector vCriterios = new Vector();
	private Vector vcolegiados = new Vector();
	
	
	// Metodos Set (Formulario (*.jsp))
	public void setAccion (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.accion, dato);}
	public void setNombre (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.nombre, dato);}
	public void setAbreviatura (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.abreviatura, dato);}
	public void setEstado(String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.estado, dato);} 
	public void setFechaDesde (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.fechaDesde, dato);} 
	public void setFechaHasta (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.fechaHasta, dato);} 
	public void setFechaEstado (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.fechaEstado, dato);}
	public void setIdPagosJG (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.idPagosJG, dato);}
	public void setIdFacturacion (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.idFacturacion, dato);}
	public void setImporteRepartir (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.importeRepartir, dato);}
	public void setPorcentajeOficio (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.PorcentajeOficio, dato);}
	public void setPorcentajeGuardias (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.PorcentajeGuardias, dato);}
	public void setPorcentajeEJG (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.PorcentajeEJG, dato);}
	public void setPorcentajeSOJ (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.PorcentajeSOJ, dato);}	
	public void setImporteOficio (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.ImporteOficio, dato);}
	public void setImporteGuardias (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.ImporteGuardias, dato);}
	public void setImporteEJG (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.ImporteEJG, dato);}
	public void setImporteSOJ (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.ImporteSOJ, dato);}	
	public void setIdInstitucion (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.idInstitucion, dato);} 	
//	public void setCriterioPago (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.criterioPago, dato);}
	public void setCriterioPagoTurno (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.criterioPagoTurno, dato);}
	public void setIdEstadoPagosJG(String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.idEstadoPagosJG, dato);}
	public void setComboFacturacion (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.comboFacturacion, dato);}
	public void setValoresFacturacion (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.valoresFacturacion, dato);}
	public void setCobroAutomatico (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.cobroAutomatico, dato);}
	public void setAccionPrevia (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.accionPrevia, dato);}
	public void setColegiado(String valor) {this.datos.put(colegiado, valor);}
	public void setNColegiado(String valor) {	this.datos.put(nColegiado, valor);	}
	public void setVcolegiados(Vector valor) {	this.vcolegiados = valor;	}
	public void setImporteFacturado (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.importeFacturado, dato);}
	public void setImportePagado (String dato) {UtilidadesHash.set(this.datos, DatosGeneralesPagoForm.importePagado, dato);}
	
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	public String getAccion	() {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.accion);}
	public String getNombre	() {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.nombre);}
	public String getAbreviatura () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.abreviatura);}
	public String getEstado	() {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.estado);}
	public String getFechaDesde () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.fechaDesde);}	
	public String getFechaHasta () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.fechaHasta);}		
	public String getFechaEstado () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.fechaEstado);}
	public String getIdPagosJG	() 	{return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.idPagosJG);}
	public String getIdFacturacion () 	{return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.idFacturacion);}
	public String getImporteRepartir () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.importeRepartir);}
	public String getPorcentajeOficio () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.PorcentajeOficio);}
	public String getPorcentajeGuardias () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.PorcentajeGuardias);}
	public String getPorcentajeEJG () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.PorcentajeEJG);}
	public String getPorcentajeSOJ () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.PorcentajeSOJ);}
	public String getImporteOficio () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.ImporteOficio);}
	public String getImporteGuardias () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.ImporteGuardias);}
	public String getImporteEJG () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.ImporteEJG);}
	public String getImporteSOJ () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.ImporteSOJ);}
	public String getIdInstitucion	() 	{return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.idInstitucion);}
//	public String getCriterioPago () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.criterioPago);}
	public String getCriterioPagoTurno () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.criterioPagoTurno);}
	public String getIdEstadoPagosJG	() {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.idEstadoPagosJG);}
	public String getComboFacturacion	() {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.comboFacturacion);}
	public String getValoresFacturacion	() {
		if (datos.get(DatosGeneralesPagoForm.valoresFacturacion) == null)
			return "0";
		else
			return "1";
	}
	public String getCobroAutomatico 	() {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.cobroAutomatico);}
	public String getAccionPrevia () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.accionPrevia);}
	public String getColegiado() {return (String)this.datos.get(colegiado);}
	public String getNColegiado() {	return (String)this.datos.get(nColegiado);	}
	public Vector getVcolegiados() {	return vcolegiados;	}
	public String getImporteFacturado () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.importeFacturado);}
	public String getImportePagado () {return UtilidadesHash.getString(this.datos, DatosGeneralesPagoForm.importePagado);}
	
	
	//Criterios para el iframe:
	public void setCriterios (Vector datos) {
		this.vCriterios = datos;
	}
	public Vector getCriterios () {
		return this.vCriterios;
	}
	
    public CriteriosPagosBean getCriterio(int index) {
        if(index>=vCriterios.size()){
        	vCriterios.setSize(index==vCriterios.size()?index+1:index);
        	vCriterios.add(index,new CriteriosPagosBean());
            }
        
        Object obj=vCriterios.get(index);
        
        if(obj==null) {
            obj=new CriteriosPagosBean();
            vCriterios.remove(index);
            vCriterios.add(index,(CriteriosPagosBean)obj);
        }
        return (CriteriosPagosBean)obj;
    }
    public void setCriterio(int index, CriteriosPagosBean criterio) {
    	vCriterios.add(index, criterio);        
    }
    
    public ColegiadosPagosBean getColegiado(int index) {
		if(index>=vcolegiados.size()){
			vcolegiados.setSize(index==vcolegiados.size()?index+1:index);
			vcolegiados.add(index,new ColegiadosPagosBean());
            }

		Object obj=vcolegiados.get(index);
        
        if(obj==null) {
        	obj=new ColegiadosPagosBean();
            vcolegiados.remove(index);
            vcolegiados.add(index,(ColegiadosPagosBean)obj);
        }
        return (ColegiadosPagosBean)obj;
	}

	public void setColegiado(int index, ColegiadosPagosBean valor) {
		vcolegiados.add(index, valor);
	}
}