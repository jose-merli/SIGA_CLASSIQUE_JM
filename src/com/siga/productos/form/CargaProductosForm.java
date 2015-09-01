package com.siga.productos.form;

import com.siga.censo.form.CargaMasivaForm;

public class CargaProductosForm extends CargaMasivaForm {

	private static final long serialVersionUID = -5917596689900757913L;

	private String numColegiadoCliente;
	private String nifCliente;
	private String nombreCliente;
	private String apellidosCliente;
	private String cantidadProducto;
	private String nombreProducto;
	private String idCategoriaProducto;
	private String idTipoProducto;
	private String idProducto;
	
	public void clear() {
		setModo("");
	}

	public CargaProductosForm clone() {
		CargaProductosForm miForm = new CargaProductosForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setFechaCarga(this.getFechaCarga());
		miForm.setTheFile(this.getTheFile());
		return miForm;
	}	

	public String getNumColegiadoCliente() {
		return numColegiadoCliente;
	}

	public void setNumColegiadoCliente(String numColegiadoCliente) {
		this.numColegiadoCliente = numColegiadoCliente;
	}

	public String getNifCliente() {
		return nifCliente;
	}

	public void setNifCliente(String nifCliente) {
		this.nifCliente = nifCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getApellidosCliente() {
		return apellidosCliente;
	}

	public void setApellidosCliente(String apellidosCliente) {
		this.apellidosCliente = apellidosCliente;
	}

	public String getCantidadProducto() {
		return cantidadProducto;
	}

	public void setCantidadProducto(String cantidadProducto) {
		this.cantidadProducto = cantidadProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public String getIdCategoriaProducto() {
		return idCategoriaProducto;
	}

	public void setIdCategoriaProducto(String idCategoriaProducto) {
		this.idCategoriaProducto = idCategoriaProducto;
	}

	public String getIdTipoProducto() {
		return idTipoProducto;
	}

	public void setIdTipoProducto(String idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}

}
