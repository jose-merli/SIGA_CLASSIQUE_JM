/*
 * Created on 31-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.general;

import java.util.Hashtable;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;

/**
 * @author daniel.campos
 *
 */
public class Articulo 
{
	// Tipos de producto
	public static final int CLASE_PRODUCTO = 1;
	public static final int CLASE_SERVICIO = 2;
	
	private int claseArticulo;				// Producto o servicio
	
	private Integer idInstitucion;

	private Integer idTipo;					//idTipoProducto o idTipoServicio
	private String  idTipoDescripcion;

	private Long idArticulo;				// idProducto o idServicio
	private String  idArticuloDescripcion;
	
	private Long idArticuloInstitucion;		// idProductoInstitucion o idServicioInstitucion
	private String idArticuloInstitucionDescripcion;
	
	private Double precio; // VALOR
	private Float valorIva;
	private Float idIva;
	private int cantidad;
	private Integer idFormaPago;
	private String formaPago;
	
	private String momentoCargo;

	private Integer idCuenta;
	private String numeroCuenta;
	
	private Integer idPeriodicidad;
	private Integer idPrecios;
	private String periodicidad;
	
	// Anhadidas para productos de tipo certificado
	private String tipoCertificado;
	private Integer idTipoEnvios;
	private Long idDireccion;
	private Long idPeticion;
	private Integer metodoSolicitud;
	private String fechaSolicitud;
	
	//Fecha Efectiva Compra
	private String fechaEfectiva;	
	
	private Boolean anticipar;
	private Double importeAnticipado;
	/**
	 * Constructor para la clase Articulo
	 * @param claseArticulo tipo de articulo (producto o servicio)
	 * @param idArticulo del articulo
	 * @param idInstitucion del articulo
	 * @param idArticuloInstitucion del articulo
	 * @param idTipo del articulo
	 * @throws SIGAException
	 */
	public Articulo (int claseArticulo, Long idArticulo, Integer idInstitucion, Long idArticuloInstitucion, Integer idTipo) throws SIGAException { 
		try {
		    
		    UsrBean usr = UsrBean.UsrBeanAutomatico(idInstitucion.toString());
		    
			// Por defecto producto
			if (claseArticulo == Articulo.CLASE_SERVICIO) this.setClaseArticuloServicio();
			else 										  this.setClaseArticuloProducto();

			// Fijamos los datos del articulo
			this.cantidad = 1;
			this.setIdInstitucion(idInstitucion);
			this.setIdTipo(idTipo);
			this.setIdArticulo(idArticulo);
			this.setIdArticuloInstitucion(idArticuloInstitucion);
			
			if (claseArticulo == Articulo.CLASE_PRODUCTO) {
				PysProductosInstitucionAdm pAdm = new PysProductosInstitucionAdm (usr);
				Hashtable producto = pAdm.getProducto (idInstitucion, idArticulo, idArticuloInstitucion, idTipo);
				this.setIdArticuloDescripcion(UtilidadesHash.getString(producto, "DESCRIPCION_PRODUCTO"));
				this.setIdTipoDescripcion(UtilidadesHash.getString(producto, "DESCRIPCION_TIPO"));
				this.setIdArticuloInstitucionDescripcion(UtilidadesHash.getString(producto, "DESCRIPCION_P_INSTITUCION"));
				this.setPrecio(UtilidadesHash.getDouble(producto, PysProductosInstitucionBean.C_VALOR));
				this.setValorIva(UtilidadesHash.getFloat(producto, "VALORIVA"));
				this.setIdIva(UtilidadesHash.getFloat(producto, PysProductosInstitucionBean.C_PORCENTAJEIVA));
				this.setMomentoCargo(UtilidadesHash.getString(producto, PysProductosInstitucionBean.C_MOMENTOCARGO));
								
				// UNICAMENTE PARA PRODUCTOS QUE SON CERTIFICADOS
				
					this.setTipoCertificado(UtilidadesHash.getString(producto, PysProductosInstitucionBean.C_TIPOCERTIFICADO));
					this.setIdTipoEnvios(UtilidadesHash.getInteger(producto, "IDTIPOENVIOS"));
					this.setIdDireccion(UtilidadesHash.getLong(producto, "IDDIRECCION"));
					this.setMetodoSolicitud(UtilidadesHash.getInteger(producto, "METODOSOLICITUD"));
					this.setFechaSolicitud(UtilidadesHash.getString(producto, "FECHASOLICITUD"));
				
			}
			
			if (claseArticulo == Articulo.CLASE_SERVICIO) {
				PysServiciosInstitucionAdm pAdm = new PysServiciosInstitucionAdm (usr);
				Hashtable servicio = pAdm.getServicio (idInstitucion, idArticulo, idArticuloInstitucion, idTipo);
				this.setIdArticuloDescripcion(UtilidadesHash.getString(servicio, "DESCRIPCION_SERVICIO"));
				this.setIdTipoDescripcion(UtilidadesHash.getString(servicio, "DESCRIPCION_TIPO"));
				this.setIdArticuloInstitucionDescripcion(UtilidadesHash.getString(servicio, "DESCRIPCION_S_INSTITUCION"));
				this.setValorIva(UtilidadesHash.getFloat(servicio, "VALORIVA"));
				this.setIdIva(UtilidadesHash.getFloat(servicio, PysServiciosInstitucionBean.C_PORCENTAJEIVA));
				this.setMomentoCargo(UtilidadesHash.getString(servicio, PysServiciosInstitucionBean.C_MOMENTOCARGO));
			}
		}
		catch (Exception e) {
			throw new SIGAException ("Error al crear el articulo"); 
		}
	}
	
	/**
	 * 
	 * @return el tipo de articulo (producto o servicio)
	 */
	public int getClaseArticulo () { return this.claseArticulo; }
	
	/**
	 * Fija el tipo de articulo como producto 
	 */
	void setClaseArticuloProducto () { this.claseArticulo = Articulo.CLASE_PRODUCTO;	}

	/**
	 * Fija el tipo de articulo como servicio 
	 */
	void setClaseArticuloServicio () { this.claseArticulo = Articulo.CLASE_SERVICIO;	}
	
	/**
	 * @return Returns the cantidad.
	 */
	public int getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return Returns the idArticulo.
	 */
	public Long getIdArticulo() {
		return idArticulo;
	}
	/**
	 * @param idArticulo The idArticulo to set.
	 */
	public void setIdArticulo(Long idArticulo) {
		this.idArticulo = idArticulo;
	}
	/**
	 * @return Returns the idArticuloDescripcion.
	 */
	public String getIdArticuloDescripcion() {
		return idArticuloDescripcion;
	}
	/**
	 * @param idArticuloDescripcion The idArticuloDescripcion to set.
	 */
	public void setIdArticuloDescripcion(String idArticuloDescripcion) {
		this.idArticuloDescripcion = idArticuloDescripcion;
	}
	/**
	 * @return Returns the idArticuloInstitucion.
	 */
	public Long getIdArticuloInstitucion() {
		return idArticuloInstitucion;
	}
	/**
	 * @param idArticuloInstitucion The idArticuloInstitucion to set.
	 */
	public void setIdArticuloInstitucion(Long idArticuloInstitucion) {
		this.idArticuloInstitucion = idArticuloInstitucion;
	}
	/**
	 * @return Returns the idArticuloInstitucionDescripcion.
	 */
	public String getIdArticuloInstitucionDescripcion() {
		return idArticuloInstitucionDescripcion;
	}
	/**
	 * @param idArticuloInstitucionDescripcion The idArticuloInstitucionDescripcion to set.
	 */
	public void setIdArticuloInstitucionDescripcion(
			String idArticuloInstitucionDescripcion) {
		this.idArticuloInstitucionDescripcion = idArticuloInstitucionDescripcion;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public Integer getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Returns the idFormaPago.
	 */
	public Integer getIdFormaPago() {
		return idFormaPago;
	}
	/**
	 * @param idFormaPago The idFormaPago to set.
	 */
	public void setIdFormaPago(Integer idFormaPago) {
		this.idFormaPago = idFormaPago;
	}
	
	/**
	 * @return Returns the formaPago.
	 */
	public String getFormaPago() {
		return formaPago;
	}
	/**
	 * @param formaPago The formaPago to set.
	 */
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idPeriodicidad.
	 */
	public Integer getIdPeriodicidad() {
		return idPeriodicidad;
	}
	/**
	 * @param idPeriodicidad The idPeriodicidad to set.
	 */
	public void setIdPeriodicidad(Integer idPeriodicidad) {
		this.idPeriodicidad = idPeriodicidad;
	}
	/**
	 * @return Returns the idPrecios.
	 */
	public Integer getIdPrecios() {
		return idPrecios;
	}
	/**
	 * @param idPrecios The idPrecios to set.
	 */
	public void setIdPrecios(Integer idPrecios) {
		this.idPrecios = idPrecios;
	}
	/**
	 * @return Returns the Periodicidad.
	 */
	public String getPeriodicidad() {
		return periodicidad;
	}
	/**
	 * @param Periodicidad The Periodicidad to set.
	 */
	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}
	/**
	 * @return Returns the idTipo.
	 */
	public Integer getIdTipo() {
		return idTipo;
	}
	/**
	 * @param idTipo The idTipo to set.
	 */
	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}
	/**
	 * @return Returns the idTipoDescripcion.
	 */
	public String getIdTipoDescripcion() {
		return idTipoDescripcion;
	}
	/**
	 * @param idTipoDescripcion The idTipoDescripcion to set.
	 */
	public void setIdTipoDescripcion(String idTipoDescripcion) {
		this.idTipoDescripcion = idTipoDescripcion;
	}
	/**
	 * @return Returns the iva.
	 */
	
	/**
	 * @return Returns the numeroCuenta.
	 */
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	/**
	 * @param numeroCuenta The numeroCuenta to set.
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	/**
	 * @return Returns the precio.
	 */
	public Double getPrecio() {
		return precio;
	}
	/**
	 * @param precio The precio to set.
	 */
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	/**
	 * @return Returns the momentoCargo.
	 */
	public String getMomentoCargo() {
		return momentoCargo;
	}
	/**
	 * @param momentoCargo The momentoCargo to set.
	 */
	public void setMomentoCargo(String momentoCargo) {
		this.momentoCargo = momentoCargo;
	}
	/**
	 * @return Returns the idDireccion.
	 */
	public Long getIdDireccion() {
		return idDireccion;
	}
	/**
	 * @param idDireccion The idDireccion to set.
	 */
	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
	}
	/**
	 * @return Returns the idTipoEnvios.
	 */
	public Integer getIdTipoEnvios() {
		return idTipoEnvios;
	}
	/**
	 * @param idTipoEnvios The idTipoEnvios to set.
	 */
	public void setIdTipoEnvios(Integer idTipoEnvios) {
		this.idTipoEnvios = idTipoEnvios;
	}
	/**
	 * @return Returns the tipoCertificado.
	 */
	public String getTipoCertificado() {
		return tipoCertificado;
	}	
	
	/**
	 * @param tipoCertificado The tipoCertificado to set.
	 */
	public void setTipoCertificado(String tipoCertificado) {
		this.tipoCertificado = tipoCertificado;
	}
	/**
	 * @return Returns the tipoCertificado.
	 */
	public Long getIdPeticion() {
		return idPeticion;
	}	
	
	/**
	 * @param tipoCertificado The tipoCertificado to set.
	 */
	public void setIdPeticion(Long idPeticion) {
		this.idPeticion = idPeticion;
	}
	
	/**
	 * @return Returns the fechaEfectiva.
	 */
	public String getFechaEfectiva() {
		return fechaEfectiva;
	}
	/**
	 * @param fechaEfectiva The fechaEfectiva to set.
	 */
	public void setFechaEfectiva(String fechaEfectiva) {
		this.fechaEfectiva = fechaEfectiva;
	}

	public Boolean getAnticipar() {
		return anticipar;
	}

	public void setAnticipar(Boolean anticipar) {
		this.anticipar = anticipar;
	}

	public Double getImporteAnticipado() {
		return importeAnticipado;
	}

	public void setImporteAnticipado(Double importeAnticipado) {
		this.importeAnticipado = importeAnticipado;
	}
	
	public Integer getMetodoSolicitud() {
		return metodoSolicitud;
	}

	public void setMetodoSolicitud(Integer metodoSolicitud) {
		this.metodoSolicitud = metodoSolicitud;
	}

	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public Float getValorIva() {
		return valorIva;
	}

	public void setValorIva(Float valorIva) {
		this.valorIva = valorIva;
	}

	public Float getIdIva() {
		return idIva;
	}

	public void setIdIva(Float idIva) {
		this.idIva = idIva;
	}
}
