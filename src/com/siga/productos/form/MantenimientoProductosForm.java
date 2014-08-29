/*
 * VERSIONES:
 * 
 * daniel.casla	- 4-11-2004 - Inicio
 * miguel.villegas - 25-11-2004 - Continuacion
 * miguel.villegas - 1-2-2005 - Renovacion completa
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de mantenimiento de los productos <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.productos.form;

import com.siga.general.MasterForm;
import com.siga.beans.*;


public class MantenimientoProductosForm extends MasterForm{
	

	private String producto="";
	private String tipoProducto="";	
	private String categoriaProducto="";
	private String pagoProducto="";
	private String estadoProducto="";
	private String idProdInst="";
	private String[] formaPagoInternet;
	private String[] formaPagoSecretaria;
	//private String facturable="";
	
	// Metodos set
	
	// Formulario Busqueda Productos	
	
	public void setBusquedaProducto(String v){
		this.producto=v;
	}
	
	/*public void setFacturable(String v){
		this.facturable=v;
	}*/
	
	public void setBusquedaCategoria(String v){
		this.categoriaProducto=v;		
	}
	
	public void setBusquedaTipo(String v){
		this.tipoProducto=v;		
	}
	
	public void setBusquedaPago(String v){
		this.pagoProducto=v;		
	}
		
	public void setBusquedaEstado(String v){
		this.estadoProducto=v;		
	}

	// Formulario Mantenimiento Productos	
	
	public void setIdInstitucion(String idInst){
		datos.put(PysProductosInstitucionBean.C_IDINSTITUCION,idInst);
	}

	public void setIdContador(String val){
		datos.put(PysProductosInstitucionBean.C_IDCONTADOR,val);
	}

	public void setIdTipoProducto(String idTipoProd){
		datos.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO,idTipoProd);
	}

	public void setIdProducto(String idProd){
		datos.put(PysProductosInstitucionBean.C_IDPRODUCTO,idProd);
	}

	public void setIdProductoInstitucion(String idProdInst){
		datos.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,idProdInst);
	}

	public void setNombre(String v){
		datos.put(PysProductosInstitucionBean.C_DESCRIPCION,v);
	}

	public void setCuentaContable(String v){
		datos.put(PysProductosInstitucionBean.C_CUENTACONTABLE,v);
	}
	public void setNoFacturable(String v){
		datos.put(PysProductosInstitucionBean.C_NOFACTURABLE,v);
	}

	public void setPrecio(double v){
		datos.put(PysProductosInstitucionBean.C_VALOR,Double.toString(v));
	}

	public void setIva(float v){		
		datos.put(PysProductosInstitucionBean.C_PORCENTAJEIVA,Float.toString(v));
	}

	public void setAltaInternet(String v){
		datos.put(PysProductosInstitucionBean.C_SOLICITARALTA,v);
	}

	public void setBajaInternet(String v){
		datos.put(PysProductosInstitucionBean.C_SOLICITARBAJA,v);
	}

	public void setCargo(String v){
		datos.put(PysProductosInstitucionBean.C_MOMENTOCARGO,v);
	}
	
	public void setFechaMod(String fechaMod){ 
		this.datos.put(PysProductosInstitucionBean.C_FECHAMODIFICACION,fechaMod);
	}
	
	public void setUsuMod(int usuMod){ 
		this.datos.put(PysProductosInstitucionBean.C_USUMODIFICACION,Integer.toString(usuMod));	
	}	
		
	public void setProducto(String prod){
		datos.put(PysProductosInstitucionBean.C_IDPRODUCTO,prod);
	}
	
	public void setTipoProducto(String tipo){
		datos.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO,tipo);		
	}	
	
	public void setFormaPagoSecretaria(String[] v){
		this.formaPagoSecretaria=v;
	}	
	
	public void setFormaPagoInternet(String[] v){
		this.formaPagoInternet=v;
	}	

	public void setIdProdInst(String v){
		this.idProdInst=v;		
	}
			
	public void setTipoCertificado(String tipo){ 
		this.datos.put(PysProductosInstitucionBean.C_TIPOCERTIFICADO,tipo);	
	}
	
	public void setFechaBaja(String aux){ 
		this.datos.put(PysProductosInstitucionBean.C_FECHABAJA,aux);	
	}
	
	public void setTipoCertificadoComision(String tipo){ 
		this.datos.put("TIPOCERTIFICADOCOMISION",tipo);	
	}
	// Metodos get	
	
	// Formulario Busqueda Productos
	
	public String getBusquedaProducto(){
		return this.producto;
	}
	
	public String getBusquedaCategoria(){
		return this.categoriaProducto;		
	}
	
	public String getBusquedaTipo(){
		return this.tipoProducto;		
	}
	
	public String getBusquedaPago(){
		return this.pagoProducto;		
	}	
	
	/*public String getFacturable(){
		return this.facturable;		
	}*/	

	public String getBusquedaEstado(){
		return this.estadoProducto;		
	}	

	// Formulario Mantenimiento Productos	
	
	public String getIdInstitucion(){
		return (String)datos.get(PysProductosInstitucionBean.C_IDINSTITUCION);
	}

	public String getIdContador(){
		return (String)datos.get(PysProductosInstitucionBean.C_IDCONTADOR);
	}

	public String getIdTipoProducto(){
		return (String)datos.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
	}

	public String getNoFacturable(){
		return (String)datos.get(PysProductosInstitucionBean.C_NOFACTURABLE);
	}
	
	public String getIdProducto(){
		return (String)datos.get(PysProductosInstitucionBean.C_IDPRODUCTO);
	}

	public String getIdProductoInstitucion(){
		return (String)datos.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
	}

	public String getNombre(){
		return (String)datos.get(PysProductosInstitucionBean.C_DESCRIPCION);				
	}

	public String getCuentaContable(){
		return (String)datos.get(PysProductosInstitucionBean.C_CUENTACONTABLE);
	}

	public double getPrecio(){
		return Double.parseDouble((String)this.datos.get(PysProductosInstitucionBean.C_VALOR));
	}

	public float getIva(){
		return Float.parseFloat((String)this.datos.get(PysProductosInstitucionBean.C_PORCENTAJEIVA));
	}

	public String getAltaInternet(){
		return (String)datos.get(PysProductosInstitucionBean.C_SOLICITARALTA);
	}

	public String getBajaInternet(){
		return (String)datos.get(PysProductosInstitucionBean.C_SOLICITARBAJA);
	}

	public String getCargo(){
		return (String)datos.get(PysProductosInstitucionBean.C_MOMENTOCARGO);
	}
	
	public String getFechaMod(String fechaMod){
		return (String)this.datos.get(PysProductosInstitucionBean.C_FECHAMODIFICACION);
	}
	
	public int getUsuMod(){
		return Integer.parseInt((String)this.datos.get(PysProductosInstitucionBean.C_USUMODIFICACION));	
	}	

	public String getProducto(){
		return (String)datos.get(PysProductosInstitucionBean.C_IDPRODUCTO);
	}
	
	public String getTipoProducto(){
		return (String)datos.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);		
	}	
	
	public String[] getFormaPagoInternet(){
		// Al cmabiar el tipo de campo en la jsp es necesario partir la cadena
		if (formaPagoInternet!=null && formaPagoInternet.length == 1) {
			return formaPagoInternet[0].split(",");
		}
		return formaPagoInternet;
	}
	
	public String[] getFormaPagoSecretaria(){
		// Al cmabiar el tipo de campo en la jsp es necesario partir la cadena
		if (formaPagoInternet!=null && formaPagoSecretaria.length == 1) {
			return formaPagoSecretaria[0].split(",");
		}
		return formaPagoSecretaria;
	}	

	public String getIdProdInst(){
		return idProdInst;		
	}	
	
	public String getTipoCertificado(){ 
		return (String)datos.get(PysProductosInstitucionBean.C_TIPOCERTIFICADO);		
	}
	
	public String getFechaBaja(){ 
		return (String)datos.get(PysProductosInstitucionBean.C_FECHABAJA);		
	}
			
	public String getTipoCertificadoComision(){ 
		return (String)datos.get("TIPOCERTIFICADOCOMISION");		
	}

}