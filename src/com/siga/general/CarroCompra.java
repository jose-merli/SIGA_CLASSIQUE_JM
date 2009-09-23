/*
 * Created on 02-feb-2005
 *	nuria.rodirguezg modificado 28-02-05. Incorpora getCalculoPrecioServicio.
 * 
 */
package com.siga.general;

import java.util.*;

import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;

/**
 * @author daniel.campos
 * 
 * Modificado el 9/03/2005 por david.sanchezp para incluir nuevos atributos y métodos set y get:<br>
 *  -usrBean<br>
 *  -numOperacion<br>
 *  -importe
 * 
 */

/**
 * Clase Carrito
 * <BR>
 * Representa un carro de la compra con sus elementos
 */

public class CarroCompra 
{
	private Long idPersona;
	private Long idPeticion;
	private Integer idInstitucion;
	private boolean compraCertificado;
	private Integer idInstitucionPresentador;
	private Hashtable listaArticulos = null;
	private UsrBean usrBean;//necesario para el TPV
	private String numOperacion = null;//numero de operacion para el TPV.
	private String importe = null;//importe para el total de pago con tarjeta con el TPV.
	
	/**
	 * Carrito ()
	 * @param idPersona que compra
	 * @param idIntuticion que compra
	 * Constructor
	 */
	CarroCompra (Long idP, Integer idInst, Integer idInstPresentador, UsrBean us) {
	    this.idPersona = idP;
	    this.idPeticion = null;
		this.idInstitucion = idInst;
		this.usrBean = us;
		this.idInstitucionPresentador = idInstPresentador;
		this.listaArticulos = new Hashtable();
		this.compraCertificado= false;
	}

	public boolean getCompraCertificado() {
		return compraCertificado;
	}
	
	public void setCompraCertificado(boolean valor) {
		this.compraCertificado=valor;
	}

	/**
	 * @return Returns the importe.
	 */
	public String getImporte() {
		return importe;
	}
	/**
	 * @param importe The importe to set.
	 */
	public void setImporte(String importe) {
		this.importe = importe;
	}

	public Long getIdPeticion() {
		return idPeticion;
	}
	public void setIdPeticion(Long valor) {
		this.idPeticion = valor;
	}

	/**
	 * @return Returns the numOperacion.
	 */
	public String getNumOperacion() {
		return numOperacion;
	}
	/**
	 * @param numOperacion The numOperacion to set.
	 */
	public void setNumOperacion(String numOperacion) {
		this.numOperacion = numOperacion;
	}
	/**
	 * @return Returns the usrBean.
	 */
	public UsrBean getUsrBean() {
		return usrBean;
	}
	/**
	 * @param usrBean The usrBean to set.
	 */
	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}
	
	public Long getIdPersona () 			{ return this.idPersona; }
	public Integer getIdinstitucion () 		{ return this.idInstitucion; }
	public Integer getIdinstitucionPresentador () 		{ return this.idInstitucionPresentador; }
	
	/**
	 * Asigna una clave a un articulo
	 * @param Articulo a del cual obtener su clave
	 * @return la clave del articulo
	 */
	private String getClave (Articulo a) {
		return "" + a.getClaseArticulo() + this.idInstitucion + "_" + a.getIdTipo() + "_" + a.getIdArticulo() + "_" + a.getIdArticuloInstitucion();
	}
	
	/**
	 * Almacena un articulo en la lista del carro
	 * @param Articulo a articulo a insertar
	 * @throws SIGAException
	 */
	private void setArticulo (Articulo a) throws SIGAException {
		try {
			this.listaArticulos.put(getClave(a), a);
		}
		catch (Exception e) {
			throw new SIGAException ("Error al insertar el articulo en la lista del carro");
		}
	}

	/**
	 * Buscar un articulo en la lista del carro
	 * @param Articulo a articulo a buscar
	 * @return el articulo si esta, null si no esta
	 * @exception SigaException
	 */
	private Articulo buscarArticulo (Articulo a) throws SIGAException {
		try {
			return (Articulo) this.listaArticulos.get(getClave(a));
		}
		catch (Exception e){
			throw new SIGAException ("No existe en artiuclo en el carro", e);
		}
	}

	/**
	 * buscarArticuloProducto
	 * Busca un producto en el carro de la compra
	 * @param Long idproducto del producto a buscar
	 * @param Long idproductoInstitucion del producto a buscar
	 * @param Integer idTipoproducto del producto a buscar
	 * @return el articulo en contrado, null si no esta
	 * @exception SigaException
	 */
	public Articulo buscarArticuloProducto (Long idArticulo,Long idArticuloInstitucion, Integer idTipoArticulo) throws SIGAException {
		Articulo a = this.crearProducto(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.buscarArticulo(a);
	}

	/**
	 * buscarArticuloServicio
	 * Busca un servicio en el carro de la compra
	 * @param Long idservicio del servicio a buscar
	 * @param Long idservicioInstitucion del servicio a buscar
	 * @param Integer idTiposervicio del servicio a buscar
	 * @return el articulo en contrado, null si no esta
	 * @exception SigaException
	 */
	public Articulo buscarArticuloServicio (Long idArticulo, Long idArticuloInstitucion, Integer idTipoArticulo) throws SIGAException {
		Articulo a = this.crearServicio(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.buscarArticulo(a);
	}

	/**
	 * insertarArticulo
	 * Inserta un articulo en el carro de la compra
	 * @param Articulo a
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	private boolean insertarArticulo (Articulo a) throws SIGAException {
		
		try {
			if (this.listaArticulos == null) {
				this.listaArticulos = new Hashtable();
			}
			
			Articulo b = this.buscarArticulo(a);
			if (b == null) {
				this.setArticulo(a);							// Si no esta lo insertamos
			}
			else {
				b.setCantidad(b.getCantidad()+1);				// Si esta incrementamos
				this.setArticulo(b);
			}
			return true;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al insertar el articulo en el carro", e);
		}
	}
	
	/**
	 * insertarProducto
	 * Inserta un producto en el carro de la compra
	 * @param Long idProducto del produto a insertar
	 * @param Long idProductoInstitucion del produto a insertar
	 * @param Integer idTipoProducto del produto a insertar
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean insertarProducto (Long idArticulo, Long idArticuloInstitucion,Integer idTipoArticulo) throws SIGAException {
		Articulo a = this.crearProducto(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.insertarArticulo(a);		
	}

	/**
	 * insertarProducto
	 * Inserta un servicio en el carro de la compra
	 * @param Long idServicio del servicio a insertar
	 * @param Long idServicoInstitucion del servicio a insertar
	 * @param Integer idTipoServicio del servicio a insertar
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean insertarServicio (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo) throws SIGAException {
		Articulo a = this.crearServicio(idArticulo, idArticuloInstitucion, idTipoArticulo);
		Vector v = this.getCalculoPrecioServicio(a);
		Hashtable hash = (Hashtable)v.elementAt(0);
		a.setPrecio(UtilidadesHash.getDouble(hash, "VALOR"));
		a.setIdPeriodicidad(UtilidadesHash.getInteger(hash,"SERVICIO_IDPERIODICIDAD"));		
		a.setPeriodicidad(UtilidadesHash.getString(hash, "SERVICIO_DESCRIPCION_PERIODICIDAD"));
		return this.insertarArticulo(a);		
	}

	/**z
	 * vaciarCarro
	 * Borra todos los articulos del carro de la compra
	 * @return true si ok, false si error
	 */
	public boolean vaciarCarro () {
		this.listaArticulos.clear();
		return true;
	}
	
	/**
	 * borrarArticulo
	 * Borra un articulo en el carro de la compra
	 * @param Articulo a
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	private boolean borrarArticulo (Articulo a) throws SIGAException {
		try {
			if (a == null) 
				return true;
			this.listaArticulos.remove(this.getClave(a));
			return true;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al borrar el elemento del carro", e);
		}
	}
	
	/**
	 * borrarProducto
	 * Borrar un producto en el carro de la compra
	 * @param Long idProducto del produto a borrar
	 * @param Long idProductoInstitucion del produto a insertar
	 * @param Integer idTipoProducto del produto a borrar
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean borrarProducto (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo) throws SIGAException {
		Articulo a = this.buscarArticuloProducto(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.borrarArticulo(a);
	}
	
	/**
	 * borrarServicio
	 * Borrar un servicio en el carro de la compra
	 * @param Long idServicio del servicio a borrar
	 * @param Long idServicioInstitucion del servicio a borrar
	 * @param Integer idTipoServicio del servicio a borrar
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean borrarServicio (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo) throws SIGAException {
		Articulo a = this.buscarArticuloServicio(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.borrarArticulo(a);
	}
	
	/**
	 * actualizarCantidadArticulo
	 * Actualiza la cantidad de un articulo en el carro de la compra
	 * @param Articulo a
	 * @param int nueva cantidad. Si es 0 el elemento se borra del carro
	 * @return true si se ha actualizado, false en caso de error
	 * @exception SigaException
	 */
	private boolean actualizarCantidadArticulo (Articulo a, int nuevaCantidad) throws SIGAException {
		try {
			if (nuevaCantidad < 1) {
				return this.borrarArticulo(a);
			}
			a.setCantidad(nuevaCantidad);
			this.setArticulo(a);
			return true;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al actualizar la cantidad de articulos", e);
		}
	}

	/**
	 * actualizarCantidadProducto
	 * Actualiza la cantidad de un producto en el carro de la compra
	 * @param Long idArticulo del producto a actualizar
	 * @param Long idArticuloInstitucion del producto a actualizar
	 * @param Integer idTipoArticulo del producto a actualizar
	 * @param int dato
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean actualizarCantidadProducto (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo, int dato) throws SIGAException {
		Articulo a = this.buscarArticuloProducto(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.actualizarCantidadArticulo(a, dato);
	}
	
	/**
	 * actualizarCantidadServicio
	 * Actualiza la cantidad de un servicio en el carro de la compra
	 * @param Long idservicio del servicio a actualizar
	 * @param Long idroductoInstitucion del servicio a actualizar
	 * @param Integer idTiporoducto del servicio a actualizar
	 * @param int dato
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean actualizarCantidadServicio (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo, int dato) throws SIGAException {
		Articulo a = this.buscarArticuloServicio(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.actualizarCantidadArticulo(a, dato);
	}

	/**
	 * actualizarNCuentaArticulo
	 * Actualiza el numero de cuenta de un articulo en el carro de la compra
	 * @param Articulo a
	 * @param String nueva numero de cuenta.
	 * @return true si se ha actualizado, false en caso de error
	 * @exception SigaException
	 */
	private boolean actualizarNCuentaArticulo (Articulo a, String nuevaCuenta) throws SIGAException {
		try {
			if ((nuevaCuenta == null) || nuevaCuenta.equals("")) {
				return false;
			}
			a.setNumeroCuenta(nuevaCuenta);
			this.setArticulo(a);
			return true;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al actualizar la cantidad de articulos", e);
		}
	}

	/**
	 * actualizarNCuentaProducto
	 * Actualiza el numero de cuenta de un producto en el carro de la compra
	 * @param Long idproducto del producto a actualizar
	 * @param Long idroductoInstitucion del producto a actualizar
	 * @param Integer idTiporoducto del producto a actualizar
	 * @param String dato
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean actualizarNCuentaProducto (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo, String dato) throws SIGAException {
		Articulo a = this.buscarArticuloProducto(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.actualizarNCuentaArticulo(a, dato);
	}
	
	/**
	 * actualizarNCuentaservicio
	 * Actualiza el numero de cuenta de un servicio en el carro de la compra
	 * @param Long idservicio del servicio a actualizar
	 * @param Long idservicioInstitucion del servicio a actualizar
	 * @param Integer idTiposervicio del servicio a actualizar
	 * @param String dato
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean actualizarNCuentaServicio (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo, String dato) throws SIGAException {
		Articulo a = this.buscarArticuloServicio(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.actualizarNCuentaArticulo(a, dato);
	}
	public void actualizarInstitucionPresentacion (Integer idinstitucion) throws SIGAException {
		this.idInstitucionPresentador = idinstitucion;
		
	}

	/**
	 * actualizarFormaPagoArticulo
	 * Actualiza el id de la forma de pago de un articulo en el carro de la compra
	 * @param Articulo
	 * @param id con la nueva forma de pago.
	 * @return true si se ha actualizado, false en caso de error
	 * @exception SigaException
	 */
	private boolean actualizarFormaPagoArticulo (Articulo a, Integer idFormaPago) throws SIGAException {
		try { 
			if (idFormaPago == null) {
				return false;
			}
			a.setIdFormaPago(idFormaPago);
			this.setArticulo(a);
			return true;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al actualizar la cantidad de articulos", e);
		}
	}

	/**
	 * actualizarFormaPagoProducto
	 * Actualiza el id de la forma de pago de un producto en el carro de la compra
	 * @param Long idproducto del producto a actualizar
	 * @param Long idroductoInstitucion del producto a actualizar
	 * @param Integer idTiporoducto del producto a actualizar
	 * @param Integer dato
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean actualizarFormaPagoProducto (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo, Integer dato) throws SIGAException {
		Articulo a = this.buscarArticuloProducto(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.actualizarFormaPagoArticulo(a, dato);
	}
	
	/**
	 * actualizarFormaPagoServicio
	 * Actualiza el id de la forma de pago de un servicio en el carro de la compra
	 * @param Long idservicio del servicio a actualizar
	 * @param Long idservicioInstitucion del servicio a actualizar
	 * @param Integer idTiposervicio del servicio a actualizar
	 * @param Integer dato
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	public boolean actualizarFormaPagoServicio (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo, Integer dato) throws SIGAException {
		Articulo a = this.buscarArticuloServicio(idArticulo, idArticuloInstitucion, idTipoArticulo);
		return this.actualizarFormaPagoArticulo(a, dato);
	}
	
	
	/**
	 * crearproducto
	 * Crea un producto en el carro de la compra
	 * @param Long idproducto del producto a crear
	 * @param Long idroductoInstitucion del producto a crear
	 * @param Integer idTiproducto del producto a crear
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	private Articulo crearProducto (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo) throws SIGAException {
		try {
			Articulo a = new Articulo (Articulo.CLASE_PRODUCTO, idArticulo, this.idInstitucion, idArticuloInstitucion, idTipoArticulo);
			return a;
		}
		catch (SIGAException e) {
			throw e;
		}
	}

	/**
	 * crearServicio
	 * Crea un servicio en el carro de la compra
	 * @param Long idservicio del servicio a crear
	 * @param Long idroductoInstitucion del servicio a crear
	 * @param Integer idTiservicio del servicio a creae
	 * @return true si ok, false si error
	 * @exception SigaException
	 */
	private Articulo crearServicio (Long idArticulo,Long idArticuloInstitucion,Integer idTipoArticulo) throws SIGAException {
		try {
			Articulo a = new Articulo (Articulo.CLASE_SERVICIO, idArticulo, this.idInstitucion, idArticuloInstitucion, idTipoArticulo);
			return a;
		}
		catch (SIGAException e) {
			throw e;
		}
	}
	
	/**
	 * estaArticulo
	 * Comprueba si un articulo esta en el carro
	 * @param Articulo a comprobar
	 * @return true si ok, false si error
	 */
	public boolean estaArticulo (Articulo a) throws SIGAException {
		try {
			Articulo b = this.buscarArticulo(a);
			if (b == null) return false;
			return this.listaArticulos.contains(b);
		}
		catch (Exception e) {
			throw new SIGAException (e);
		}
	}
	
	/**
	 * getListaArticulos
	 * Devulve la lista con los articulos del carro.
	 * @return vector con los articulos
	 * @exception SigaException
	 */
	public Vector getListaArticulos () throws SIGAException{
		try {
			Vector v = new Vector ();
			Enumeration e = this.listaArticulos.keys();
			while (e.hasMoreElements()) {
				v.add(this.listaArticulos.get(e.nextElement()));
			}
			return v;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al recuperar los articulos del carro");
		}
	}

	/**
	 * Obtiene el precio de un servicio
	 * @param Articulo a articulo del que se obtiene el precio
	 * @return el precio del servicio
	 * @throws SIGAException
	 */
	private Double getPrecioServicio(Articulo a) throws SIGAException {
		Double precio = null;
		Hashtable codigos = new Hashtable();
		try {
		    codigos.put(new Integer(1),a.getIdInstitucion().toString());
		    codigos.put(new Integer(2),a.getIdTipo().toString());
		    codigos.put(new Integer(3),a.getIdArticulo().toString());
		    codigos.put(new Integer(4),a.getIdArticuloInstitucion().toString());
		    codigos.put(new Integer(5),this.idPersona.toString());
		    codigos.put(new Integer(6),this.usrBean.getLanguageInstitucion());
			String sql = "SELECT F_SIGA_CALCULOPRECIOSERVICIO(:1, " + 
															  ":2, " + 
															  ":3, " + 
															  ":4, " + 
															  ":5, " +
															  ":6" +
						  ") AS PRECIO_SERVICIO FROM DUAL";
			
			RowsContainer rc = null;
			rc = new RowsContainer(); 
			if (rc.queryBind(sql,codigos)) {
				if (rc.size() ==  1) {
					// Tratamos los datos de la funcion 'F_SIGA_CALCULOPRECIOSERVICIO'
					Hashtable hash = (Hashtable)((Row) rc.get(0)).getRow();
					String valor = UtilidadesHash.getString(hash, "PRECIO_SERVICIO");
					
					// "-1" --> Error no existen datos en la tabla Pys_ServicioInstitucion
					if (!valor.equalsIgnoreCase("-1")){
						String datosPrecio[] =  UtilidadesString.split(valor, "#");
						// RGG cambio para 10g
						String diezg = datosPrecio[0];
						diezg = diezg.replaceAll(",",".");
						precio = new Double(diezg);
						//precio = new Double(datosPrecio[0]);
					}
				}
			}
		}
		catch(Exception e){
			throw new SIGAException ("Error al obtener el precio del servicio", e);
		}
		return precio;
	}
	
	/**
	 * Obtiene el precio el porcentajeIva, el idPrecio y periodicidad del servicio
	 * @param Articulo a articulo del que se obtiene el precio
	 * @return los valores del servicio
	 * @throws SIGAException
	 */
	private Vector getCalculoPrecioServicio(Articulo a) throws SIGAException {
		Double precio = null;
		Vector resultados = new Vector (); 
		try {
			String sql = "SELECT F_SIGA_CALCULOPRECIOSERVICIO(" + a.getIdInstitucion()			+ ", " + 
																  a.getIdTipo() 				+ ", " + 
																  a.getIdArticulo() 			+ ", " + 
																  a.getIdArticuloInstitucion() 	+ ", " + 
																  this.idPersona 				+ ", " +
																  this.usrBean.getLanguageInstitucion()+
						  ") AS PRECIO_SERVICIO FROM DUAL";
			
			RowsContainer rc = null;
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				if (rc.size() ==  1) {
					// Tratamos los datos de la funcion 'F_SIGA_CALCULOPRECIOSERVICIO'
					Hashtable hash = (Hashtable)((Row) rc.get(0)).getRow();
					String valor = UtilidadesHash.getString(hash, "PRECIO_SERVICIO");
					
					// "-1" --> Error no existen datos en la tabla Pys_ServicioInstitucion
					if (!valor.equalsIgnoreCase("-1")){
						String datosPrecio[] =  UtilidadesString.split(valor, "#");
						// RGG cambio para 10g
						String diezg = datosPrecio[0];
						diezg = diezg.replaceAll(",",".");
						

						UtilidadesHash.set(hash, "VALOR", new Double(diezg));
						UtilidadesHash.set(hash, "PORCENTAJEIVA", new Float(datosPrecio[1]));
						UtilidadesHash.set(hash, "SERVICIO_IDPRECIOSSERVICIOS", new Integer(datosPrecio[2]));
						UtilidadesHash.set(hash, "SERVICIO_IDPERIODICIDAD", new Integer(datosPrecio[3]));
						if (datosPrecio.length == 5) {
							UtilidadesHash.set(hash, "SERVICIO_DESCRIPCION_PERIODICIDAD", datosPrecio[4]);
						}
					}
					resultados.add(hash);
				}
			}
		}
		catch(Exception e){
			throw new SIGAException ("Error al obtener el precio del servicio", e);
		}
		return resultados;
	}

	public boolean tieneServicios() throws SIGAException {
		
	    Enumeration enumer = this.listaArticulos.elements();
	    while (enumer.hasMoreElements()) {
	        Articulo a = (Articulo)enumer.nextElement();
	        if (a.getClaseArticulo()==Articulo.CLASE_SERVICIO) {
	            return true;
	        }
	    }
		return false;
	}


}
