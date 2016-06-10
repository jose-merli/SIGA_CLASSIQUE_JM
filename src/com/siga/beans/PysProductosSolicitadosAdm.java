/*
 * VERSIONES:
 * 
 * nuria.rodriguezg	- 27-01-2005 - Inicio
 */
 
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.Articulo;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysProductosSolicitadosAdm extends MasterBeanAdministrador {

	/**	
	 * @param usuario
	 */
	public PysProductosSolicitadosAdm(UsrBean usuario) {
		super( PysProductosSolicitadosBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * Devuelve un array con el nombre de los campos de la tabla PYS_PRODUCTOSSOLICITADOS 
	 * @author nuria.rgonzalez 27-01-2005	 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	PysProductosSolicitadosBean.C_IDINSTITUCION,	
				PysProductosSolicitadosBean.C_IDINSTITUCIONORIGEN,
				PysProductosSolicitadosBean.C_IDINSTITUCIONCOLEGIACION,
				PysProductosSolicitadosBean.C_IDTIPOPRODUCTO,
				PysProductosSolicitadosBean.C_IDPRODUCTO,
				PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION,
				PysProductosSolicitadosBean.C_IDPETICION,	
				PysProductosSolicitadosBean.C_IDPERSONA,
				PysProductosSolicitadosBean.C_IDCUENTA,	
				PysProductosSolicitadosBean.C_IDFORMAPAGO,
				PysProductosSolicitadosBean.C_CANTIDAD,				
				PysProductosSolicitadosBean.C_ACEPTADO,	
				PysProductosSolicitadosBean.C_VALOR,
				PysProductosSolicitadosBean.C_IDTIPOIVA,				
				PysProductosSolicitadosBean.C_IDTIPOENVIOS,
				PysProductosSolicitadosBean.C_IDDIRECCION,
				PysProductosSolicitadosBean.C_FECHAMODIFICACION,
				PysProductosSolicitadosBean.C_NOFACTURABLE,
				PysProductosSolicitadosBean.C_USUMODIFICACION,
				PysProductosSolicitadosBean.C_METODOSOLICITUD,
				PysProductosSolicitadosBean.C_ACEPTACESIONMUTUALIDAD,
				PysProductosSolicitadosBean.C_FECHASOLICITUD,
				PysProductosSolicitadosBean.C_ORDEN};
		return campos;
	}

	/**
	 * Devuelve un array con el nombre de los campos clave de la tabla PYS_PRODUCTOSSOLICITADOS 
	 * @author nuria.rgonzalez 27-01-2005	  
	 */
	protected String[] getClavesBean() {
		String[] campos = {	PysProductosSolicitadosBean.C_IDINSTITUCION,
				PysProductosSolicitadosBean.C_IDTIPOPRODUCTO,	
				PysProductosSolicitadosBean.C_IDPRODUCTO,
				PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION,
				PysProductosSolicitadosBean.C_IDPETICION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/**
	 * Devuelve un PysProductosSolicitadosBean con los campos de la tabla PYS_PRODUCTOSSOLICITADOS 
	 * @author nuria.rgonzalez 27-01-2005	 
	 * @param Hashtable 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		PysProductosSolicitadosBean bean = null;
		try{
			bean = new PysProductosSolicitadosBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_IDINSTITUCION));
			bean.setIdInstitucionOrigen(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_IDINSTITUCIONORIGEN));
			bean.setIdInstitucionColegiacion(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_IDINSTITUCIONCOLEGIACION));
			bean.setIdTipoProducto(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_IDTIPOPRODUCTO));
			bean.setIdProducto(UtilidadesHash.getLong(hash,PysProductosSolicitadosBean.C_IDPRODUCTO));
			bean.setIdProductoInstitucion(UtilidadesHash.getLong(hash,PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION));
			bean.setIdPeticion(UtilidadesHash.getLong(hash,PysProductosSolicitadosBean.C_IDPETICION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,PysProductosSolicitadosBean.C_IDPERSONA));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_IDCUENTA));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_IDFORMAPAGO));
			bean.setIdTipoIva(UtilidadesHash.getFloat(hash,PysProductosSolicitadosBean.C_IDTIPOIVA));
			bean.setCantidad(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_CANTIDAD));
			bean.setAceptado(UtilidadesHash.getString(hash,PysProductosSolicitadosBean.C_ACEPTADO));
			bean.setValor(UtilidadesHash.getDouble(hash,PysProductosSolicitadosBean.C_VALOR));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_IDTIPOENVIOS));
			bean.setIdDireccion(UtilidadesHash.getLong(hash,PysProductosSolicitadosBean.C_IDDIRECCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,PysProductosSolicitadosBean.C_FECHAMODIFICACION));
			bean.setMetodoSolicitud(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_METODOSOLICITUD));
			bean.setAceptaCesionMutualidad(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_ACEPTACESIONMUTUALIDAD));
			bean.setFechaSolicitud(UtilidadesHash.getString(hash,PysProductosSolicitadosBean.C_FECHASOLICITUD));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_USUMODIFICACION));
			bean.setNoFacturable(UtilidadesHash.getString(hash,PysProductosSolicitadosBean.C_NOFACTURABLE));
			bean.setOrden(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_ORDEN));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * Devuelve un Hashtable con los campos de la tabla PYS_PRODUCTOSSOLICITADOS 
	 * @author nuria.rgonzalez 27-01-2005	 
	 * @param PysProductosSolicitadosBean 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			PysProductosSolicitadosBean b = (PysProductosSolicitadosBean) bean;
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCION, b.getIdInstitucion());	
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCIONORIGEN, b.getIdInstitucionOrigen());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCIONCOLEGIACION, b.getIdInstitucionColegiacion());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTO, b.getIdProducto());			
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, b.getIdProductoInstitucion());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPETICION, b.getIdPeticion());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, b.getIdCuenta());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO, b.getIdFormaPago());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOIVA, b.getIdTipoIva());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_CANTIDAD, b.getCantidad());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTADO, b.getAceptado());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_VALOR, b.getValor());			
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDDIRECCION, b.getIdDireccion());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_METODOSOLICITUD, b.getMetodoSolicitud());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTACESIONMUTUALIDAD, b.getAceptaCesionMutualidad());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_FECHASOLICITUD, b.getFechaSolicitud());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_NOFACTURABLE, b.getNoFacturable());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ORDEN, b.getOrden());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 * 
	 * @param datos
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public PaginadorBind getProductosSolicitadosPaginador (Hashtable datos, Integer idInstitucion) throws ClsExceptions, SIGAException {
		PaginadorBind paginador=null;
		try {
			String select = this.getQueryProductosSolicitadosBind(datos, idInstitucion);
			paginador = new PaginadorBind(select, new Hashtable());


		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getProductosSolicitadosPaginador.");
		}
		
		return paginador;  
	}
	
	/**
	 * Este metodo es parte de la consulta de productos que relentiza la query. 	 
	 * @param idInstitucion
	 * @param idTipoProducto
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @param idPeticionConsulta
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	//
	public Vector getFechaEfectivaCompraProducto(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion, String idPeticionConsulta, String idPersona) throws ClsExceptions {
		try {
			String sql = " SELECT NVL( " +
								" ( " +
									" SELECT NVL(" + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_FECHABAJA + ", " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_FECHA + ") " +
									" FROM " + PysCompraBean.T_NOMBRETABLA +
									" WHERE " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + " = " + idInstitucion + 
										" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPETICION + " = " + idPeticionConsulta + 
										" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDTIPOPRODUCTO + " = " +  idTipoProducto + 
										" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTO + " = " + idProducto +
										" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTOINSTITUCION + " = " + idProductoInstitucion + 
								" ), ( " +
									" SELECT MAX(" + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + ") " +
									" FROM " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + 
									" WHERE " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + " = " + idInstitucion +
										" AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA + " = " + idPersona +
										" AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + " <> 10 " +
										" AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = 'B' " +
										" AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA + " = " + idPeticionConsulta +
										" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + " = " + idTipoProducto +
										" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO + " = " + idProducto + 
										" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + " = " + idProductoInstitucion + 
										" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + 
										" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION +
										" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPERSONA + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA +
								" ) " +
							" ) AS FECHAEFEC " +
						" FROM  DUAL ";
			
			return this.selectGenerico(sql);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion de getFechaEfectivaCompraProducto");
		}
	}
	
	/**
	 * Este metodo es parte de la consulta de productos que relentiza la query.
	 * @param idInstitucion
	 * @param idTipoProducto
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @param idPeticionConsulta
	 * @return
	 * @throws ClsExceptions
	 */
	public String getEstadoCompra(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion, String idPeticionConsulta) throws ClsExceptions {
		String estadoCompra = "";
		
		try {
			String sql = " SELECT F_SIGA_ESTADOCOMPRA(" + idInstitucion + ", " + idPeticionConsulta + ", " + idProducto + "," + idTipoProducto + "," + idProductoInstitucion + ") AS ESTADOPAGO FROM DUAL";
			
			Vector vEstados = this.selectGenerico(sql);
			if (vEstados!=null && vEstados.size()>0) {
				estadoCompra = (String) ((Hashtable) vEstados.get(0)).get("ESTADOPAGO");				
			}			
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getEstadoCompra");
		}
		
		return estadoCompra;
	}
	
	/**
	 * Obtiene los datos de los productos solicitados a partir de unos criterios
	 * 
	 * JPT - Accesos:
	 * 1. PysPeticionCompraSuscripcionAdm.getPeticionDetalle()
	 * - datos => PysPeticionCompraSuscripcionBean.C_IDPETICION	
	 * 
	 * 2. DatosFacturacionAction.abrirProductos()
	 * - datos => PysProductosSolicitadosBean.C_IDPERSONA
	 * - datos => PysPeticionCompraSuscripcionBean.C_TIPOPETICION
	 * - datos => criterios.put("FECHA_DESDE"
	 * - datos => criterios.put("FECHA_HASTA"
	 * 
	 * 3. SolicitudBajaAction.getProductosServicios()
	 * - datos => PysPeticionCompraSuscripcionBean.C_IDPERSONA
	 * - datos => PysPeticionCompraSuscripcionBean.C_TIPOPETICION
	 * - datos => "ES_SOLICITUD_BAJA"
	 * 
	 * @param datos con los datos necesarios para la consulta
	 * @param idInstitucion
	 * 
	 * @return vector con todos los registros recuperados
	 * @throws SIGAException, ClsExceptions
	 */
	public Vector getProductosSolicitados (Hashtable datos, Integer idInstitucion) throws ClsExceptions, SIGAException {

		try { 
			Long idPeticion = UtilidadesHash.getLong(datos, PysProductosSolicitadosBean.C_IDPETICION);
			Long idPersona = UtilidadesHash.getLong(datos, PysPeticionCompraSuscripcionBean.C_IDPERSONA);
			String tipoPeticion = UtilidadesHash.getString(datos, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			String fechaDesde = UtilidadesHash.getString(datos, "FECHA_DESDE");
			String fechaHasta = UtilidadesHash.getString(datos, "FECHA_HASTA");
			//Si viene con este parametro es que se hace el select desde SolicitudBajaAction.
			boolean isSolicitudBaja = (UtilidadesHash.getString(datos, "ES_SOLICITUD_BAJA") != null);
			
			StringBuilder consulta = new StringBuilder(); 
			consulta.append("SELECT 'PRODUCTO' AS CONSULTA, "); 
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCIONORIGEN);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDCUENTA);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_CANTIDAD);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_VALOR);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDTIPOIVA);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_ACEPTADO);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDFORMAPAGO);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDTIPOENVIOS);
			consulta.append(", ");
			
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_NOFACTURABLE);
			consulta.append(", ");
									
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			consulta.append(", ");
									
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
			consulta.append(", ");
			
			if (isSolicitudBaja) {
				consulta.append("( SELECT COUNT(*) ");
				consulta.append(" FROM ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" PCSB, ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(" PSB ");
				consulta.append(" WHERE PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" AND PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(" AND PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
				consulta.append(" = '");
				consulta.append(ClsConstants.TIPO_PETICION_COMPRA_BAJA);
				consulta.append("' AND PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION);
				consulta.append(" = ");
				consulta.append(ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE);
				consulta.append(" AND PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" = ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
				consulta.append(" AND PSB.");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" AND PSB.");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" AND PSB.");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" AND PSB.");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" AND PSB.");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" = PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
				consulta.append(" AND PSB.");
				consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
				consulta.append(" = PCSB.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(") AS ESTADO_BAJA, ");			
				
			} else {
				consulta.append(" CASE WHEN (");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
				consulta.append(" = 'B') THEN ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" ELSE ( ");
				consulta.append(" SELECT PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
				consulta.append(" FROM ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" PC, ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(" PS "); 
				consulta.append(" WHERE PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);                                 
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);			
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION);
				consulta.append(" = ");
				consulta.append(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA);
				consulta.append(" AND ROWNUM = 1 "); // En principio solo deberia haber una baja procesada
				consulta.append(" ) ");
				consulta.append(" END AS IDPETICIONRELACIONADA, ");
				
				// Fecha Efectiva:
				consulta.append(" NVL( "); 
				consulta.append(" ( ");
				consulta.append(" SELECT ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_FECHA);
				consulta.append(" FROM ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(" WHERE ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" AND ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" AND ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDTIPOPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" AND ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" AND ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" ) , ( ");
				consulta.append(" SELECT PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA); 
				consulta.append(" FROM ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(" PC, ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(" PS ");
				consulta.append(" WHERE PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION); 
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
				consulta.append(" = PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION);
				consulta.append(" <> ");
				consulta.append(ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE);
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
				consulta.append(" = 'B' ");
				consulta.append(" AND PC.");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA);
				consulta.append(" IS NOT NULL ");
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_ACEPTADO);
				consulta.append(" = 'B' ");	
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" AND PS.");
				consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
				consulta.append(" ) ");
				consulta.append(" ) AS FECHAEFEC, ");	
				
				consulta.append(" F_SIGA_COMPROBAR_ANTICIPAR( ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(", ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(", ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(", ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(", ");
				consulta.append(" 'P', ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(", ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
				consulta.append(", ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_VALOR);
				consulta.append(" || '#' || ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOIVA);
				consulta.append(" ) AS ANTICIPAR, ");
			}
																			
			//INC_07291_SIGA Porcentajes de IVA
			consulta.append(PysTipoIvaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysTipoIvaBean.C_VALOR);
			consulta.append(" AS VALORIVA, ");
									
			// Forma Pago
			consulta.append(" ( ");
			consulta.append("SELECT ");
			consulta.append(UtilidadesMultidioma.getCampoMultidioma(PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_DESCRIPCION, this.usrbean.getLanguage()));
			consulta.append(" FROM ");
			consulta.append(PysFormaPagoBean.T_NOMBRETABLA); 
			consulta.append(" WHERE ");
			consulta.append(PysFormaPagoBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysFormaPagoBean.C_IDFORMAPAGO);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDFORMAPAGO);
			consulta.append(") AS FORMAPAGO, ");
									
			// Concepto
			consulta.append(" ( ");
			consulta.append(" SELECT ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_DESCRIPCION); 
			consulta.append(" FROM ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA); 
			consulta.append(" WHERE ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION); 
			consulta.append(" AND ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
			consulta.append(" AND ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_IDPRODUCTO);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
			consulta.append(" AND ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
			consulta.append(" ) AS CONCEPTO, ");

			// Solicitar Baja
			consulta.append(" ( ");
			consulta.append(" SELECT ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_SOLICITARBAJA); 
			consulta.append(" FROM ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA); 
			consulta.append(" WHERE ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION); 
			consulta.append(" AND ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
			consulta.append(" AND ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_IDPRODUCTO);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
			consulta.append(" AND ");
			consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
			consulta.append(" ) AS SOLICITARBAJA, ");

			// Cuenta
			consulta.append(" NVL( ");
			consulta.append(" ( ");
			consulta.append(" SELECT ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(CenCuentasBancariasBean.C_IBAN);
			consulta.append(" FROM ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA); 
			consulta.append(" WHERE ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDCUENTA);
			consulta.append(" AND ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION); 
			consulta.append(" AND ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(CenCuentasBancariasBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPERSONA); 
			consulta.append(" ),'-' ");
			consulta.append(" ) AS NCUENTA ");
										
			consulta.append(" FROM ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(", "); 
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(", "); 
			consulta.append(PysTipoIvaBean.T_NOMBRETABLA);
			
			consulta.append(" WHERE ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion);
			consulta.append(" AND ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
			consulta.append(" = ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION); 
			consulta.append(" AND ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
			consulta.append(" AND ");
			consulta.append(PysTipoIvaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysTipoIvaBean.C_IDTIPOIVA);
			consulta.append(" = ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDTIPOIVA);
			
			if (idPeticion != null) {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(idPeticion);
			}
			
			if (idPersona != null) {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
				consulta.append(" = ");
				consulta.append(idPersona);
			}
			
			if (tipoPeticion != null) {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
				consulta.append(" = '");
				consulta.append(tipoPeticion);
				consulta.append("' ");
			}
		
			// Fechas
			if (fechaDesde != null) {
				if (fechaDesde.indexOf("SYSDATE")!=-1) {
					consulta.append(" AND ");
					consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
					consulta.append(".");
					consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
					consulta.append(" >= ");
					consulta.append(fechaDesde);
				} else {
					consulta.append(" AND ");
					consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
					consulta.append(".");
					consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
					consulta.append(" >= TO_DATE('");
					consulta.append(fechaDesde);
					consulta.append("', '");
					consulta.append(ClsConstants.DATE_FORMAT_SQL);
					consulta.append("') ");
				}
			}
			if (fechaHasta != null) {
				if (fechaHasta.indexOf("SYSDATE")!=-1) {
					consulta.append(" AND ");
					consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
					consulta.append(".");
					consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
					consulta.append(" <= ");
					consulta.append(fechaHasta);
				} else {
					consulta.append(" AND ");
					consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
					consulta.append(".");
					consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
					consulta.append(" <= TO_DATE('");
					consulta.append(fechaHasta);
					consulta.append("', '");
					consulta.append(ClsConstants.DATE_FORMAT_SQL);
					consulta.append("') ");
				}
			}
			
			if (isSolicitudBaja) {
				consulta.append(" AND "); // Debe tener la peticion de compra procesada (como tiene tipoPeticion ya comprueba que es una peticion de alta)
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION);
				consulta.append(" = ");
				consulta.append(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA);
				consulta.append(" AND "); // No debe ser una solicitud denegada o de baja
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_ACEPTADO);
				consulta.append(" NOT IN ('");
				consulta.append(ClsConstants.PRODUCTO_DENEGADO);
				consulta.append("',' ");
				consulta.append(ClsConstants.PRODUCTO_BAJA);
				consulta.append("')");
				consulta.append(" AND NOT EXISTS ( "); // No deben aparecen los certificados
				consulta.append(" SELECT 1 ");
				consulta.append(" FROM ");
				consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
				consulta.append(" WHERE ");
				consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosInstitucionBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" AND ");
				consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" AND ");
				consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosInstitucionBean.C_IDPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" AND ");
				consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" AND ");
				consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosInstitucionBean.C_TIPOCERTIFICADO);
				consulta.append(" IN ('C','M','D') ");
				consulta.append(" ) ");
				consulta.append(" AND NOT EXISTS ( "); // No puede tener factura, ni fecha de baja
				consulta.append(" SELECT 1 ");
				consulta.append(" FROM ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(" WHERE ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
				consulta.append(" AND ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDPETICION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
				consulta.append(" AND ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
				consulta.append(" AND ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDTIPOPRODUCTO);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
				consulta.append(" AND ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" = ");
				consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				consulta.append(" AND (");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_IDFACTURA);
				consulta.append(" IS NOT NULL ");
				consulta.append(" OR ");
				consulta.append(PysCompraBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysCompraBean.C_FECHABAJA);
				consulta.append(" IS NOT NULL ))");
			}
			
			consulta.append(" ORDER BY ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
			consulta.append(" DESC ");
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta.toString())) {
				Vector resultados = new Vector (); 
				for (int i = 0; i < rc.size(); i++)	{
					Hashtable hashTemp = new Hashtable();
					hashTemp = (Hashtable)((Row) rc.get(i)).getRow();
					
					//Consulto si la baja ha sido solicitada:
					String idTipoProducto = (String)hashTemp.get(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
					String idProducto = (String)hashTemp.get(PysProductosSolicitadosBean.C_IDPRODUCTO);
					String idProductoInstitucion = (String)hashTemp.get(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
					String idPeticionConsulta = (String)hashTemp.get(PysProductosSolicitadosBean.C_IDPETICION);
					
					String estadoPago = this.getEstadoCompra(idInstitucion.toString(), idTipoProducto, idProducto, idProductoInstitucion, idPeticionConsulta);
					UtilidadesHash.set(hashTemp, "ESTADOPAGO", estadoPago);	
					
					//Si no es solicitud de baja nos ahorramos el siguiente acceso a base de datos aunque esto se haya metido por lo de los estados de pago
					if (isSolicitudBaja) {
						// JTA segun requeriemiento se puede anular siempre que no este facturado(mirar el procediemineto F_SIGA_ESTADOCOMPRA
						//ya que cuando esta cobrado con tarjeta no tiene por que estar facturado!) 
						if (estadoPago==null || estadoPago.equalsIgnoreCase("estados.compra.pendiente"))
							resultados.add(hashTemp);
						
					} else {
						resultados.add(hashTemp);
					}
				}
				
				return resultados;
			}
			
		} catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   			
	   		} else{
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   				
	   			} else {
	   				throw new ClsExceptions("Error al obtener productos solicitados");
	   			}
	   		}	
	    }
		
		return null;
	}
	
	/**
	 * confirmarProducto
	 * Confirma la solicitud (de alta o de baja) de un producto.
	 * @param idInstitucion
	 * @param idPeticion 
	 * @param idTipoProducto
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @return true si todo va bien, false si error
	 * @throws SIGAException, ClsExceptions
	 */
	public boolean confirmarProducto (Integer idInstitucion, Long idPeticion, Integer idTipoProducto, Long idProducto, Long idProductoInstitucion, Double importeAnticipado, String fechaEfectiva) throws ClsExceptions, SIGAException {

		try {
			
			PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.usrbean);
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDPETICION, idPeticion);
			PysPeticionCompraSuscripcionBean peticionBean = (PysPeticionCompraSuscripcionBean) ppcsa.selectByPK(claves).get(0);
			//GstDate gstDate=new GstDate();
			//Date fecha=null;
			if(fechaEfectiva==null)
				fechaEfectiva="0";

			// Peticion de ALTA
			if (peticionBean.getTipoPeticion().equals(ClsConstants.TIPO_PETICION_COMPRA_ALTA)) {

				// 1. Marcamos el producto como ACEPTADO
				claves.clear();
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, idTipoProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTO, idProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
				PysProductosSolicitadosAdm productoAdm = new PysProductosSolicitadosAdm(this.usrbean);
				PysProductosSolicitadosBean productoBean = (PysProductosSolicitadosBean) productoAdm.select(claves).get(0);
				productoBean.setAceptado(ClsConstants.PRODUCTO_ACEPTADO);
				if (!productoAdm.update(productoBean)) {
					return false;
				}
				
				// 2. Creamos el apunte -> Insertamos en la tabla PysCompra el registro
				PysCompraBean compraBean = new PysCompraBean ();
				compraBean.setCantidad(productoBean.getCantidad());
				
				
				if(fechaEfectiva.equals("0")||fechaEfectiva==null){
					compraBean.setFecha("sysdate");
				}
				else{
					fechaEfectiva=GstDate.getApplicationFormatDate("ES",fechaEfectiva);
					compraBean.setFecha(fechaEfectiva);
				}
				compraBean.setIdFormaPago(productoBean.getIdFormaPago());
				if(compraBean.getIdFormaPago()!=null){
					compraBean.setNoFacturable("0");
				}else{
					compraBean.setNoFacturable("1");
				}
				compraBean.setIdInstitucion(productoBean.getIdInstitucion());
				compraBean.setIdPeticion(productoBean.getIdPeticion());
				compraBean.setIdProducto(productoBean.getIdProducto());
				compraBean.setIdProductoInstitucion(productoBean.getIdProductoInstitucion());
				compraBean.setIdTipoProducto(productoBean.getIdTipoProducto());
				compraBean.setImporteUnitario(productoBean.getValor());
				compraBean.setIdCuenta(productoBean.getIdCuenta());
				compraBean.setIdPersona(productoBean.getIdPersona());
				
				// RGG 29-04-2005 cambio para insertar la descripcion
				// buscamos la descripcion
				PysProductosInstitucionAdm pyspiAdm = new PysProductosInstitucionAdm(this.usrbean);
				Hashtable claves2 = new Hashtable();
				claves2.put(PysProductosInstitucionBean.C_IDINSTITUCION,productoBean.getIdInstitucion());
				claves2.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO,productoBean.getIdTipoProducto());
				claves2.put(PysProductosInstitucionBean.C_IDPRODUCTO,productoBean.getIdProducto());
				claves2.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,productoBean.getIdProductoInstitucion());
				Vector vpi = pyspiAdm.selectByPK(claves2);
				String descripcion = "";
				if (vpi!=null && vpi.size()>0) {
					PysProductosInstitucionBean b = (PysProductosInstitucionBean) vpi.get(0);
					descripcion = b.getDescripcion();
				}
				compraBean.setDescripcion(descripcion);
//				System.out.println("PAGO --> "+productoBean.getIdFormaPago());
				
				if(productoBean.getNoFacturable().equals("0")){
				
					if (productoBean.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_TARJETA) {
						double importeAnticipadoTarjeta = UtilidadesNumero.redondea(productoBean.getCantidad().doubleValue() * productoBean.getValor().doubleValue() * (1 + (productoBean.getIdTipoIva().floatValue() / 100)),2);
						compraBean.setImporteAnticipado(new Double(importeAnticipadoTarjeta));	
					}
					else {
						if (productoBean.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_METALICO)
							compraBean.setImporteAnticipado(importeAnticipado);
						else
							compraBean.setImporteAnticipado(new Double(0));
					}
				}

				compraBean.setIdTipoIva(productoBean.getIdTipoIva());				
				
				PysCompraAdm compraAdm = new PysCompraAdm (this.usrbean);
				if (!compraAdm.insert(compraBean)) {
					return false;
				}
				
				// 3. Verificamos si los articulos de la peticion estan en un estado distinto de PENDIENTE
				long productos_serviciosPendientes = ppcsa.getNumProductosServiciosPendientes (idInstitucion, idPeticion);
				if (productos_serviciosPendientes > 0) {
					return true;
				}
				// 3.1 no hay articulos pendientes, cambiamos el estado a PROCESADA
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				return ppcsa.update(peticionBean);
			}
			
			// Peticion de BAJA
			else {
				// 1. Comprobamos si esta en Compra el producto solicitado:
				
				PysCompraAdm compraAdm = new PysCompraAdm(this.usrbean);
				
				String where = " WHERE " + PysCompraBean.C_IDINSTITUCION + " = " + idInstitucion +
								" AND " + PysProductosSolicitadosBean.C_IDPETICION + " = " + peticionBean.getIdPeticionAlta() +
								" AND " + PysCompraBean.C_IDTIPOPRODUCTO + " = " + idTipoProducto +
								" AND " + PysCompraBean.C_IDPRODUCTO + " = " + idProducto +
								" AND " + PysCompraBean.C_IDPRODUCTOINSTITUCION + " = " + idProductoInstitucion +
								" AND " + PysCompraBean.C_IDFACTURA + " IS NOT NULL";
				
				Vector v = compraAdm.select(where);
				if ((v != null) && (v.size() > 0)){
					throw new SIGAException ("messages.pys.gestionSolicitudes.errorBaja");
				}

				
				PysProductosSolicitadosAdm productoAdm = new PysProductosSolicitadosAdm(this.usrbean);
				
				/*
				if (!productoAdm.delete(claves)) {
					return false;
				}
				*/
				
				// 2. Cambiamos el estado de peticion de baja a PROCESADA
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				//peticionBean.setFecha(GstDate.getApplicationFormatDate("ES",fechaEfectiva));
				if (!ppcsa.update(peticionBean)) {
					return false;
				}
				
				// 3. Cambiamos el estado de producto de peticion de baja a PRODUCTO_BAJA
				claves.clear();
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, idTipoProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTO, idProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
				PysProductosSolicitadosBean productoBean = (PysProductosSolicitadosBean) productoAdm.select(claves).get(0);
				productoBean.setAceptado(ClsConstants.PRODUCTO_BAJA);
				if (!productoAdm.update(productoBean)) {
					return false;
				}
				
				// 4. Cambiamos el estado de producto de peticion de alta a PRODUCTO_BAJA
				claves.clear();
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPETICION, peticionBean.getIdPeticionAlta());
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, idTipoProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTO, idProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
				productoBean = (PysProductosSolicitadosBean) productoAdm.select(claves).get(0);
				productoBean.setAceptado(ClsConstants.PRODUCTO_BAJA);
				if (!productoAdm.update(productoBean)) {
					return false;
				}

				// 5. Verificamos si los articulos de la peticion estan en un estado distinto de PENDIENTE
				long productos_serviciosPendientes = ppcsa.getNumProductosServiciosPendientes (idInstitucion, peticionBean.getIdPeticionAlta());
				if (productos_serviciosPendientes > 0) {
					return true;
				}
				
				//mhg - Cogemos el producto que queremos dar de baja para actualizar la fecha de baja/efectiva
				String where2 = " WHERE " + PysCompraBean.C_IDINSTITUCION + " = " + idInstitucion +
						" AND " + PysProductosSolicitadosBean.C_IDPETICION + " = " + peticionBean.getIdPeticionAlta() +
						" AND " + PysCompraBean.C_IDTIPOPRODUCTO + " = " + idTipoProducto +
						" AND " + PysCompraBean.C_IDPRODUCTO + " = " + idProducto +
						" AND " + PysCompraBean.C_IDPRODUCTOINSTITUCION + " = " + idProductoInstitucion;
		
				Vector vProducto = compraAdm.select(where2);
				if ((vProducto != null) && (vProducto.size() > 0)){
					PysCompraBean compraBean = (PysCompraBean) vProducto.get(0);
					if(fechaEfectiva.equals("0")){
						compraBean.setFechaBaja("sysdate");
					}
					//mhg - El siguiente caso nunca deberia pasar ya que en una anulación no se deberia seleccionar la fecha de baja.
					else{
						fechaEfectiva=GstDate.getApplicationFormatDate("ES",fechaEfectiva);
						compraBean.setFechaBaja(fechaEfectiva);
					}	

				    if (!compraAdm.updateDirect(compraBean)) {
					    return false;
				    }
				}
				
				// 5.1 No hay articulos pendientes, cambiamos el estado a PROCESADA
				claves.clear();
				UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDPETICION, peticionBean.getIdPeticionAlta());
				peticionBean = (PysPeticionCompraSuscripcionBean) ppcsa.selectByPK(claves).get(0);
				//peticionBean.setFecha(GstDate.getApplicationFormatDate("ES",fechaEfectiva));
				
				return ppcsa.update(peticionBean);
				
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else{
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e,"Error al confirmar la solicitud de un producto");
	   			}
	   		}	
	    }
	}
	
	
	/**
	 * denegarProducto
	 * Deniega la solicitud (de alta o de baja) de un producto.
	 * @param idInstitucion
	 * @param idPeticion
	 * @param idTipoProducto
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @param mensaje mesaje de salida que se mostrara
	 * @return
	 * @throws SIGAException, ClsExceptions
	 */
	public boolean denegarProducto (Integer idInstitucion, Long idPeticion, Integer idTipoProducto, Long idProducto, Long idProductoInstitucion, String []mensaje) throws SIGAException, ClsExceptions {

		try {
			PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.usrbean);
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, PysPeticionCompraSuscripcionBean.C_IDPETICION, idPeticion);
			PysPeticionCompraSuscripcionBean peticionBean = (PysPeticionCompraSuscripcionBean) ppcsa.selectByPK(claves).get(0);

			// Peticion de ALTA
			if (peticionBean.getTipoPeticion().equals(ClsConstants.TIPO_PETICION_COMPRA_ALTA)) {
				// 1. Marcamos el producto como DENEGADO
				claves.clear();
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, idTipoProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTO, idProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
				PysProductosSolicitadosAdm productoAdm = new PysProductosSolicitadosAdm(this.usrbean);
				PysProductosSolicitadosBean productoBean = (PysProductosSolicitadosBean) productoAdm.select(claves).get(0);
				productoBean.setAceptado(ClsConstants.PRODUCTO_DENEGADO);
				if (!productoAdm.update(productoBean)) {
					return false;
				}

				// 2 Si la forma de pado es TARJETA -> mensaje especial, sino OK
				if (productoBean.getIdFormaPago()!=null && productoBean.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_TARJETA) {
					mensaje[0] = new String("messages.pys.gestionSolicitudes.abono");					
				}
				else {
					mensaje[0] = new String("messages.updated.success");
				}

				// 3. Verificamos si los articulos de la peticion estan en un estado distinto de PENDIENTE
				long productos_serviciosPendientes = ppcsa.getNumProductosServiciosPendientes (idInstitucion, idPeticion);
				if (productos_serviciosPendientes > 0) {
					return true;
				}
				// 3.1 no hay articulos pendientes, cambiamos el estado a PROCESADA
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				if(!ppcsa.update(peticionBean)) {
					return false;
				}
				return true;
			}

			// Peticion de BAJA
			else {
				// 1. Cambiamos el estado de peticion de baja a PROCESADA
				peticionBean.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
				if (!ppcsa.update(peticionBean)) {
					return false;
				}

				// 2. Cambiamos el estado de producto de peticion de baja a PRODUCTO_DENEGADO
				claves.clear();
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, idTipoProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTO, idProducto);
				UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
				PysProductosSolicitadosAdm productoAdm = new PysProductosSolicitadosAdm(this.usrbean);
				PysProductosSolicitadosBean productoBean = (PysProductosSolicitadosBean) productoAdm.select(claves).get(0);
				productoBean.setAceptado(ClsConstants.PRODUCTO_DENEGADO);
				return productoAdm.update(productoBean);
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else{
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions("Error al denegar solicitudes de productos");
	   			}
	   		}	
	    }
	}
	
	/**
	 * Inserta el producto del carrito en la tabla Pys_ProductosSolicitados	 
	 * @param a - Producto que vamos a insertar 
	 * @param idPeticion - identificador de la petición de compra a la que está asociado  
	 * @param idPersona - identificador de la persona para la que se realiza la peticion 
	 * @param usuario - identificador del usuario que realiza la peticion  
	 * @return  Hashtable  contiene todos los datos sobre el producto que se ha insertado
	 * @throws Exception 
	 * @exception  SIGAException  En cualquier caso de error
	 */
	public Hashtable insertProducto(Articulo a, Long idPeticion, Integer idInstitucionPresentador,Integer idInstitucionColegiacion, Long idPersona) throws Exception {
		Hashtable hash = new Hashtable();
		PysProductosSolicitadosAdm adm = new PysProductosSolicitadosAdm(this.usrbean);
		
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCION, a.getIdInstitucion());	
		if (idInstitucionColegiacion!=null && !idInstitucionColegiacion.equals(new Integer(0))) {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCIONCOLEGIACION, idInstitucionColegiacion);	
		}
		if (idInstitucionPresentador!=null && !idInstitucionPresentador.equals(new Integer(0))) {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCIONORIGEN, idInstitucionPresentador);	
		}		
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, a.getIdTipo());
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTO, a.getIdArticulo());			
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, a.getIdArticuloInstitucion());
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPERSONA, idPersona);
		if (a.getIdFormaPago()!=null && a.getIdFormaPago().intValue()==ClsConstants.TIPO_FORMAPAGO_METALICO){
		   
		    UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, "");
		}else{
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, a.getIdCuenta());
		}
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO, a.getIdFormaPago());
		//INC_07291_SIGA Porcentajes de IVA.
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOIVA, a.getIdTipoIva());
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_CANTIDAD, new Integer(a.getCantidad()));
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_PENDIENTE);
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_VALOR, a.getPrecio());		
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOENVIOS, a.getIdTipoEnvios());
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDDIRECCION, a.getIdDireccion());
		if(a.getIdFormaPago()!=null){
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_NOFACTURABLE, "0");
		}else{
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_NOFACTURABLE, "1");
		}
		if(a.getFechaSolicitud()!=null){
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_FECHASOLICITUD, UtilidadesString.formatoFecha(a.getFechaSolicitud(),"dd/MM/yyyy","yyyy/MM/dd hh:mm:ss"));
		}else{
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_FECHASOLICITUD, "SYSDATE");
		}
		if(a.getMetodoSolicitud()!=null){
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_METODOSOLICITUD, a.getMetodoSolicitud());
		}else{
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_METODOSOLICITUD, "");
		}
		
		if(a.getAceptaCesionMut()!=null){
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTACESIONMUTUALIDAD, a.getAceptaCesionMut());
		}else{
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTACESIONMUTUALIDAD, "");
		}		
	
		try {
			if(adm.insert(hash)){
				UtilidadesHash.set(hash, "DESCRIPCION_ARTICULO", a.getIdArticuloInstitucionDescripcion());	
				UtilidadesHash.set(hash, "DESCRIPCION_FORMAPAGO", a.getFormaPago());	
				UtilidadesHash.set(hash, "DESCRIPCION_CUENTA", a.getNumeroCuenta());				
			} else {
				return null;
			}				
		} catch (Exception e) { 
			throw new ClsExceptions("Error al insertar Productos");
			//throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		return hash;
	}		
	
	/**
	 * Inserta el producto del carrito en la tabla Pys_ProductosSolicitados	 
	 * @param a - Producto que vamos a insertar 
	 * @param idPeticion - identificador de la petición de compra a la que está asociado  
	 * @param idPersona - identificador de la persona para la que se realiza la peticion 
	 * @param usuario - identificador del usuario que realiza la peticion  
	 * @return  Hashtable  contiene todos los datos sobre el producto que se ha insertado
	 * @throws Exception 
	 * @exception  SIGAException  En cualquier caso de error
	 */
	public Hashtable insertProducto(Articulo a, Long idPeticion, Integer idInstitucionPresentador, Long idPersona) throws Exception {
		Hashtable hash = new Hashtable();		
		
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCION, a.getIdInstitucion());	
		if (idInstitucionPresentador!=null && !idInstitucionPresentador.equals(new Integer(0))) {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCIONORIGEN, idInstitucionPresentador);	
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCIONCOLEGIACION, idInstitucionPresentador);	
		}		
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, a.getIdTipo());
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTO, a.getIdArticulo());			
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, a.getIdArticuloInstitucion());
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPERSONA, idPersona);
		if (a.getIdFormaPago()!=null && a.getIdFormaPago().intValue()==ClsConstants.TIPO_FORMAPAGO_METALICO) {		   
		    UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, "");
		} else {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, a.getIdCuenta());
		}
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO, a.getIdFormaPago());
		//INC_07291_SIGA Porcentajes de IVA.
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOIVA, a.getIdTipoIva());
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_CANTIDAD, new Integer(a.getCantidad()));
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_PENDIENTE);
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_VALOR, a.getPrecio());		
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOENVIOS, a.getIdTipoEnvios());
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDDIRECCION, a.getIdDireccion());
		if (a.getIdFormaPago()!=null) {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_NOFACTURABLE, "0");
		} else {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_NOFACTURABLE, "1");
		}		
		if (a.getFechaSolicitud()!=null) {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_FECHASOLICITUD, UtilidadesString.formatoFecha(a.getFechaSolicitud(),"dd/MM/yyyy","yyyy/MM/dd hh:mm:ss"));
		} else {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_FECHASOLICITUD, "SYSDATE");
		}		
		if (a.getMetodoSolicitud()!=null) {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_METODOSOLICITUD, a.getMetodoSolicitud());
		} else {
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_METODOSOLICITUD, "");
		}		
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ORDEN, a.getOrden());
	
		try {			
			PysProductosSolicitadosAdm adm = new PysProductosSolicitadosAdm(this.usrbean);
			if(adm.insert(hash)){
				UtilidadesHash.set(hash, "DESCRIPCION_ARTICULO", a.getIdArticuloInstitucionDescripcion());	
				UtilidadesHash.set(hash, "DESCRIPCION_FORMAPAGO", a.getFormaPago());	
				UtilidadesHash.set(hash, "DESCRIPCION_CUENTA", a.getNumeroCuenta());				
			} else {
				return null;
			}				
		} catch (Exception e) { 
			throw new ClsExceptions("Error al insertar Productos");
			//throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		return hash;
	}	
	
	/**
	 * Inserta en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en PysProductosSolicitadosAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	/**
	 * 
	 * @param datos
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private String getQueryProductosSolicitadosBind(Hashtable datos, Integer idInstitucion) throws ClsExceptions, SIGAException {
		Long idPeticion = UtilidadesHash.getLong(datos, PysCompraBean.C_IDPETICION);
		Long idPersona = UtilidadesHash.getLong(datos, PysPeticionCompraSuscripcionBean.C_IDPERSONA);
		String tipoPeticion = UtilidadesHash.getString(datos, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
		String fechaDesde = UtilidadesHash.getString(datos, "FECHA_DESDE");
		String fechaHasta = UtilidadesHash.getString(datos, "FECHA_HASTA");
		String distintoCampoAceptado = UtilidadesHash.getString(datos, PysCompraBean.C_ACEPTADO);
		
		StringBuilder consulta = new StringBuilder();
		
		consulta.append("SELECT 'PRODUCTO' AS CONSULTA, ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		consulta.append(", ");
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
		consulta.append(", "); 
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
		consulta.append(", ");
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
		consulta.append(", ");
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
		consulta.append(", ");
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_CANTIDAD);
		consulta.append(", "); 
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_VALOR);
		consulta.append(", ");
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_ACEPTADO);
		consulta.append(", "); 		
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_NOFACTURABLE);
		consulta.append(", ");	
		
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
		consulta.append(", "); 
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
		consulta.append(", ");	
		
		consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosInstitucionBean.C_DESCRIPCION);
		consulta.append(" AS CONCEPTO, ");
		
		consulta.append(PysTipoIvaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysTipoIvaBean.C_VALOR);
		consulta.append(" AS VALORIVA, ");	
		
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDINSTITUCION);
		consulta.append(" AS EXISTE_PYS_COMPRA, ");
		
		consulta.append("DECODE(");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDINSTITUCION);
		consulta.append(", NULL, ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDFORMAPAGO);
		consulta.append(", ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDFORMAPAGO);
		consulta.append(") AS ");
		consulta.append(PysProductosSolicitadosBean.C_IDFORMAPAGO);
		consulta.append(", ");
		
		consulta.append("(SELECT ");
		consulta.append(UtilidadesMultidioma.getCampoMultidioma(PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_DESCRIPCION, this.usrbean.getLanguage()));
		consulta.append(" FROM ");
		consulta.append(PysFormaPagoBean.T_NOMBRETABLA);
		consulta.append(" WHERE ");
		consulta.append(PysFormaPagoBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysFormaPagoBean.C_IDFORMAPAGO);
		consulta.append(" = DECODE(");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDINSTITUCION);
		consulta.append(", NULL, ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDFORMAPAGO);
		consulta.append(", ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDFORMAPAGO);
		consulta.append(")) AS FORMAPAGO, ");	
		
		consulta.append("DECODE(");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDINSTITUCION);
		consulta.append(", NULL, ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDCUENTA);
		consulta.append(", NVL(");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDCUENTADEUDOR);
		consulta.append(", ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDCUENTA);
		consulta.append(")) AS ");
		consulta.append(PysProductosSolicitadosBean.C_IDCUENTA);
		consulta.append(", ");	
		
		consulta.append("NVL((SELECT ");
		consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(CenCuentasBancariasBean.C_IBAN); 
		consulta.append(" FROM ");
		consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA); 
		consulta.append(" WHERE ");
		consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);	
		consulta.append(" AND ");
		consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(CenCuentasBancariasBean.C_IDPERSONA);		
		consulta.append(" = DECODE(");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDINSTITUCION);
		consulta.append(", NULL, ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
		consulta.append(", DECODE(");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDCUENTADEUDOR);
		consulta.append(", NULL, ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDPERSONA);
		consulta.append(", ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDPERSONADEUDOR);
		consulta.append(")) ");				
		consulta.append(" AND ");
		consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
		consulta.append(" = DECODE(");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDINSTITUCION);
		consulta.append(", NULL, ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDCUENTA);
		consulta.append(", NVL(");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDCUENTADEUDOR);
		consulta.append(", ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDCUENTA);
		consulta.append("))), '-') AS NCUENTA ");
		
		consulta.append(" FROM ");
		consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		consulta.append(", "); 
		
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(", ");
		
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(", ");
		
		consulta.append(PysTipoIvaBean.T_NOMBRETABLA);
		consulta.append(", ");
		
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		
		consulta.append(" WHERE ");
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		
		consulta.append(" AND ");
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
		consulta.append(" = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
		
		consulta.append(" AND ");
		consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosInstitucionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		
		consulta.append(" AND ");
		consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
		consulta.append(" = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
		
		consulta.append(" AND ");
		consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosInstitucionBean.C_IDPRODUCTO);
		consulta.append(" = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
		
		consulta.append(" AND ");
		consulta.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
		consulta.append(" = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION); 	
		
		consulta.append(" AND ");
		consulta.append(PysTipoIvaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysTipoIvaBean.C_IDTIPOIVA);
		consulta.append(" = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDTIPOIVA); 
		
		consulta.append(" AND ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDINSTITUCION);
		consulta.append("(+) = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		
		consulta.append(" AND ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDTIPOPRODUCTO);
		consulta.append("(+) = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
		
		consulta.append(" AND ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDPRODUCTO);
		consulta.append("(+) = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
		
		consulta.append(" AND ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDPRODUCTOINSTITUCION);
		consulta.append("(+) = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
		
		consulta.append(" AND ");
		consulta.append(PysCompraBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysCompraBean.C_IDPETICION);
		consulta.append("(+) = ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDPETICION);		
		
		consulta.append(" AND ");
		consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion);

		if (idPeticion != null) {
			consulta.append(" AND ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPETICION);
			consulta.append(" = '");
			consulta.append(idPeticion);
			consulta.append("' ");
		}			

		if (distintoCampoAceptado != null) {
			consulta.append(" AND ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_ACEPTADO);
			consulta.append(" <> '");
			consulta.append(distintoCampoAceptado);
			consulta.append("' ");
		}
		
		if (idPersona != null) {
			consulta.append(" AND ");
			consulta.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysProductosSolicitadosBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(idPersona);
		}
		
		if (tipoPeticion != null) {
			consulta.append(" AND ");
			consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			consulta.append(" = '");
			consulta.append(tipoPeticion);
			consulta.append("' ");
		}		

		if (fechaDesde != null) {
			if (fechaDesde.indexOf("SYSDATE") != -1) {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
				consulta.append(" >= ");
				consulta.append(fechaDesde);
				
			} else {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
				consulta.append(" >= ");
				consulta.append(" TO_DATE('");
				consulta.append(fechaDesde);
				consulta.append("', '");
				consulta.append(ClsConstants.DATE_FORMAT_SQL);
				consulta.append("') ");
			}
		}
		
		if (fechaHasta != null) {
			if (fechaHasta.indexOf("SYSDATE") != -1) {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
				consulta.append(" <= ");
				consulta.append(fechaHasta);
				
			} else {
				consulta.append(" AND ");
				consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				consulta.append(".");
				consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
				consulta.append(" <= ");
				consulta.append(" TO_DATE('");
				consulta.append(fechaHasta);
				consulta.append("', '");
				consulta.append(ClsConstants.DATE_FORMAT_SQL);
				consulta.append("') ");
			}
		}		

		consulta.append(" ORDER BY ");
		consulta.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(PysPeticionCompraSuscripcionBean.C_FECHA);
		consulta.append(" DESC ");

		return consulta.toString();
	}
}