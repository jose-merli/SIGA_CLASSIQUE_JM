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
				PysProductosSolicitadosBean.C_PORCENTAJEIVA,				
				PysProductosSolicitadosBean.C_IDTIPOENVIOS,
				PysProductosSolicitadosBean.C_IDDIRECCION,
				PysProductosSolicitadosBean.C_FECHAMODIFICACION,
				PysProductosSolicitadosBean.C_NOFACTURABLE,
				PysProductosSolicitadosBean.C_USUMODIFICACION,
				PysProductosSolicitadosBean.C_METODOSOLICITUD,
				PysProductosSolicitadosBean.C_FECHASOLICITUD};
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
			bean.setPorcentajeIVA(UtilidadesHash.getFloat(hash,PysProductosSolicitadosBean.C_PORCENTAJEIVA));
			bean.setCantidad(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_CANTIDAD));
			bean.setAceptado(UtilidadesHash.getString(hash,PysProductosSolicitadosBean.C_ACEPTADO));
			bean.setValor(UtilidadesHash.getDouble(hash,PysProductosSolicitadosBean.C_VALOR));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_IDTIPOENVIOS));
			bean.setIdDireccion(UtilidadesHash.getLong(hash,PysProductosSolicitadosBean.C_IDDIRECCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,PysProductosSolicitadosBean.C_FECHAMODIFICACION));
			bean.setMetodoSolicitud(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_METODOSOLICITUD));
			bean.setFechaSolicitud(UtilidadesHash.getString(hash,PysProductosSolicitadosBean.C_FECHASOLICITUD));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysProductosSolicitadosBean.C_USUMODIFICACION));
			bean.setNoFacturable(UtilidadesHash.getString(hash,PysProductosSolicitadosBean.C_NOFACTURABLE));
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
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA, b.getPorcentajeIVA());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_CANTIDAD, b.getCantidad());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTADO, b.getAceptado());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_VALOR, b.getValor());			
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDDIRECCION, b.getIdDireccion());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_METODOSOLICITUD, b.getMetodoSolicitud());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_FECHASOLICITUD, b.getFechaSolicitud());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_NOFACTURABLE, b.getNoFacturable());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	public PaginadorBind getProductosSolicitadosPaginador (Hashtable datos, Integer idInstitucion) throws ClsExceptions, SIGAException {
		PaginadorBind paginador=null;
		try {
			Hashtable codigos = new Hashtable();
			String select = getQueryProductosSolicitadosBind(datos, idInstitucion, codigos);
			paginador = new PaginadorBind(select,codigos);


		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getProductosSolicitadosPaginador.");
		}
		return paginador;  
		
		
		
	}
	/**
	 * getFechaEfectivaCompraProducto Este metodo es parte de la consulta de productos que relentiza la query. 
	 
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
	public Vector getFechaEfectivaCompraProducto(String idInstitucion, String idTipoProducto,
			String idProducto,String idProductoInstitucion,String idPeticionConsulta,String idPersona) throws ClsExceptions  
	{
		try {
			int contador=0;
			Hashtable codigos = new Hashtable();
			StringBuffer sql =  new StringBuffer();
			
			//mhg - INC_10542_SIGA Para sacar la fecha efectiva añadimos la fecha de baja.
			sql.append(" select NVL((SELECT NVL(PYS_COMPRA.FECHABAJA, PYS_COMPRA.FECHA) ");
			sql.append(" FROM PYS_COMPRA ");
			sql.append(" WHERE PYS_COMPRA.IDINSTITUCION = ");
			contador++;
			codigos.put(new Integer(contador),idInstitucion);
			sql.append(":"+contador);

			sql.append(" AND PYS_COMPRA.IDPETICION = ");
			contador++;
			codigos.put(new Integer(contador),idPeticionConsulta);
			sql.append(":"+contador);
			sql.append(" AND PYS_COMPRA.IDTIPOPRODUCTO = ");
			contador++;
			codigos.put(new Integer(contador),idTipoProducto);
			sql.append(":"+contador);
			sql.append(" AND PYS_COMPRA.IDPRODUCTO = ");
			contador++;
			codigos.put(new Integer(contador),idProducto);
			sql.append(":"+contador);
			sql.append(" AND PYS_COMPRA.IDPRODUCTOINSTITUCION = ");
			contador++;
			codigos.put(new Integer(contador),idProductoInstitucion);
			sql.append(":"+contador);
			sql.append(" ), ");
			sql.append(" (SELECT max(petco.fecha) ");
			sql.append(" FROM PYS_PETICIONCOMPRASUSCRIPCION petco, ");
			sql.append(" PYS_PRODUCTOSSOLICITADOS      prodsol ");
			sql.append(" WHERE petco.IDINSTITUCION = "); 
			contador++;
			codigos.put(new Integer(contador),idInstitucion);
			sql.append(":"+contador);
			sql.append(" AND petco.IDPERSONA =  ");
			contador++;
			codigos.put(new Integer(contador),idPersona);
			sql.append(":"+contador);
			sql.append(" and petco.idestadopeticion <> 10 ");
			sql.append(" AND petco.TIPOPETICION = 'B' ");
			sql.append(" AND petco.IDPETICIONALTA = ");
			contador++;
			codigos.put(new Integer(contador),idPeticionConsulta);
			sql.append(":"+contador);
			sql.append(" AND prodsol.IDTIPOPRODUCTO = ");
			contador++;
			codigos.put(new Integer(contador),idTipoProducto);
			sql.append(":"+contador);
			sql.append(" AND prodsol.IDPRODUCTO = ");
			contador++;
			codigos.put(new Integer(contador),idProducto);
			sql.append(":"+contador);
			sql.append(" AND prodsol.IDPRODUCTOINSTITUCION = ");
			contador++;
			codigos.put(new Integer(contador),idProductoInstitucion);
			sql.append(":"+contador);
			sql.append(" AND prodsol.IDINSTITUCION = petco.IDINSTITUCION ");
			sql.append(" AND prodsol.IDPETICION = petco.IDPETICION ");
			sql.append(" AND prodsol.IDPERSONA = petco.IDPERSONA)) AS FECHAEFEC ");

			sql.append(" FROM  DUAL ");
			return this.selectGenericoBind(sql.toString(), codigos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getFechaEfectivaCompra");
		}
	}
	/**
	 * getEstadoCompraProducto Este metodo es parte de la consulta de productos que relentiza la query.
	 * @param idInstitucion
	 * @param idTipoProducto
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @param idPeticionConsulta
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getEstadoCompra(String idInstitucion, String idTipoProducto,
			String idProducto,String idProductoInstitucion,String idPeticionConsulta) throws ClsExceptions  
	{
		try {
			int contador=0;
			Hashtable codigos = new Hashtable();
			


			StringBuffer sql =  new StringBuffer();
			sql.append(" select F_SIGA_ESTADOCOMPRA( ");
			contador++;
			codigos.put(new Integer(contador),idInstitucion);
			sql.append(":"+contador);
			sql.append(",");
			contador++;
			codigos.put(new Integer(contador),idPeticionConsulta);
			sql.append(":"+contador);
			sql.append(",");
			contador++;
			codigos.put(new Integer(contador),idProducto);
			sql.append(":"+contador);
			sql.append(",");
			contador++;
			codigos.put(new Integer(contador),idTipoProducto);
			sql.append(":"+contador);
			sql.append(",");
			contador++;
			codigos.put(new Integer(contador),idProductoInstitucion);
			sql.append(":"+contador);
			
			sql.append(" ) AS ESTADOPAGO FROM DUAL ");
			

				
			return this.selectGenericoBind(sql.toString(), codigos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getEstadoCompraProducto");
		}
	}
	
	/**
	 * Funcion getProductosSolicitados
	 * Obtiene los datos de los productos solicitados a partir de unos criterios
	 * @param Hashtable con los datos necesarios para la consulta
	 * @param idInstitucion
	 * @param peticionBaja en funcion de este parametro se obtienen o no los servicios pendientes de confirmar su solicitud.
	 * 
	 * @return vector con todos los registros recuperados
	 * @throws SIGAException, ClsExceptions
	 */
	public Vector getProductosSolicitados (Hashtable datos, Integer idInstitucion, boolean peticionBaja) throws ClsExceptions, SIGAException {

		try { 
			Long idPeticion = UtilidadesHash.getLong(datos, PysProductosSolicitadosBean.C_IDPETICION);
			Long idPersona = UtilidadesHash.getLong(datos, PysPeticionCompraSuscripcionBean.C_IDPERSONA);
			String tipoPeticion = UtilidadesHash.getString(datos, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			String fechaDesde = UtilidadesHash.getString(datos, "FECHA_DESDE");
			String fechaHasta = UtilidadesHash.getString(datos, "FECHA_HASTA");
			//Si viene con este parametro es que se hace el select desde Solicitudbajaaction.
			//No lo metemos como parametro en el metodo porque se llama desde distinto sitios
			boolean isSolicitudBaja = UtilidadesHash.getString(datos, "ES_SOLICITUD_BAJA")!=null;
			
			
			String select = "SELECT 'PRODUCTO' AS CONSULTA, " + 
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCIONORIGEN + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDCUENTA + ", " + 
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_CANTIDAD + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_VALOR 	+ ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_PORCENTAJEIVA + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_ACEPTADO + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDFORMAPAGO + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOENVIOS + ", " +
									PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_NOFACTURABLE + ", " +
									
									PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + ", " +
									PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + ", " +
					
         							" CASE WHEN (" + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = 'B') THEN " +
         									PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA +
         								" ELSE ( " +
         									" SELECT PC." + PysPeticionCompraSuscripcionBean.C_IDPETICION + 
         									" FROM " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + " PC, " +
         									PysProductosSolicitadosBean.T_NOMBRETABLA + " PS " + 
     										" WHERE PS." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = PC." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + 
     											" AND PS." + PysProductosSolicitadosBean.C_IDPETICION + " = PC." + PysPeticionCompraSuscripcionBean.C_IDPETICION +                                   
     											" AND PC." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION +
     											" AND PS." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION +
     											" AND PS." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO +
     											" AND PS." + PysProductosSolicitadosBean.C_IDPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO +
     											" AND PS." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION +
     											" AND PS." + PysProductosSolicitadosBean.C_IDPERSONA + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPERSONA +
										" ) " +
									" END AS IDPETICIONRELACIONADA, " +     
										
									
									//INC_07291_SIGA Porcentajes de IVA
									PysTipoIvaBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_VALOR + " AS VALORIVA, " +
									
									// Forma Pago
									" ( " +
										"SELECT " + UtilidadesMultidioma.getCampoMultidioma(PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_DESCRIPCION, this.usrbean.getLanguage()) +
										" FROM " + PysFormaPagoBean.T_NOMBRETABLA + 
										" WHERE " + PysFormaPagoBean.T_NOMBRETABLA + "." + PysFormaPagoBean.C_IDFORMAPAGO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDFORMAPAGO +
									") AS FORMAPAGO, " +
									

									// Fecha Efectiva:
									" NVL( " + 
										" ( " +
											" SELECT " + PysCompraBean.T_NOMBRETABLA + "." +PysCompraBean.C_FECHA +
											" FROM " + PysCompraBean.T_NOMBRETABLA +
											" WHERE " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION +
												" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPETICION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION +
												" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDTIPOPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO +
												" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO +
												" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTOINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION +
										" ) , ( " +
											" SELECT PC." + PysPeticionCompraSuscripcionBean.C_FECHA + 
											" FROM " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + " PC, " +
												PysProductosSolicitadosBean.T_NOMBRETABLA + " PS " +
											" WHERE PS." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = PC." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION + 
     											" AND PS." + PysProductosSolicitadosBean.C_IDPETICION + " = PC." + PysPeticionCompraSuscripcionBean.C_IDPETICION +
     											" AND PS." + PysProductosSolicitadosBean.C_IDPERSONA + " = PC." + PysPeticionCompraSuscripcionBean.C_IDPERSONA +
												" AND PC." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + " <> " + ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE +
												" AND PC." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = 'B' " +
												" AND PC." + PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA + " IS NOT NULL " +
												" AND PS." + PysProductosSolicitadosBean.C_ACEPTADO + " = 'B' " +	
     											" AND PS." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION +
     											" AND PS." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO +
     											" AND PS." + PysProductosSolicitadosBean.C_IDPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO +
     											" AND PS." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION +
     											" AND PS." + PysProductosSolicitadosBean.C_IDPETICION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION +
     											" AND PS." + PysProductosSolicitadosBean.C_IDPERSONA + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPERSONA +
										" ) " +
									" ) AS FECHAEFEC, " +									
									
									// Concepto
									" ( " +
										" SELECT " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION + 
										" FROM " + PysProductosInstitucionBean.T_NOMBRETABLA + 
										" WHERE " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + 
											" AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO +
											" AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO +
											" AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION +
									" ) AS CONCEPTO, " +

									// Solicitar Baja
									" ( " +
										" SELECT " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_SOLICITARBAJA + 
										" FROM " + PysProductosInstitucionBean.T_NOMBRETABLA + 
										" WHERE " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + 
											" AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO +
											" AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO +
											" AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION +
									" ) AS SOLICITARBAJA, " +

									// Cuenta
									" NVL( " +
										" ( " +
											" SELECT " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN  +
											" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + 
											" WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDCUENTA +
												" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + 
												" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPERSONA + 
										" ),'-' " +
									" ) AS NCUENTA, " +

									" F_SIGA_ESTADOCOMPRA( " + 
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + 
									" ) AS ESTADOPAGO ," +
									
									" F_SIGA_COMPROBAR_ANTICIPAR( " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + ", " +
										" 'P', " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPERSONA + ", " +
										PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_VALOR +
											" || '#' || " +
											PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_PORCENTAJEIVA +
							        " ) AS ANTICIPAR ";
										
			String from  =  " FROM " + PysProductosSolicitadosBean.T_NOMBRETABLA + ", " + 
								PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + ", " + 
								PysTipoIvaBean.T_NOMBRETABLA;
			
			String where =  " WHERE " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + idInstitucion +
								" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPETICION + 
								" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDINSTITUCION +
								" AND " + PysTipoIvaBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_IDTIPOIVA + " = " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_PORCENTAJEIVA;
			
			if (idPeticion != null) {
				where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + " = " + idPeticion;
			}
			
			if (idPersona != null) {
				where += " AND " +  PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDPERSONA + " = " + idPersona;
			}
			
			if (tipoPeticion != null) {
				where += " AND " +  PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_TIPOPETICION + " = '" + tipoPeticion + "' ";
			}
		
			// Fechas
			if (fechaDesde != null) {
				if (fechaDesde.indexOf("SYSDATE")!=-1) {
					where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " >= " + fechaDesde ;
				} else {
					where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " >= " + " TO_DATE('" + fechaDesde + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
				}
			}
			if (fechaHasta != null) {
				if (fechaHasta.indexOf("SYSDATE")!=-1) {
					where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " <= " + fechaHasta;
				} else {
					where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " <= " + " TO_DATE('" + fechaHasta + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
				}
			}
			
			if (peticionBaja){
				where += " AND " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION + " = " + ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA;
			}
			
			String orderBy = " ORDER BY " + PysPeticionCompraSuscripcionBean.T_NOMBRETABLA + "." + PysPeticionCompraSuscripcionBean.C_FECHA + " DESC ";
			
			String consulta = select + from + where + orderBy; 
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta)) {
				Vector resultados = new Vector (); 
				for (int i = 0; i < rc.size(); i++)	{
					Hashtable hashTemp = new Hashtable();
					hashTemp = (Hashtable)((Row) rc.get(i)).getRow();
					
					//Consulto si la baja ha sido solicitada:
					String idTipoProducto = (String)hashTemp.get(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
					String idProducto = (String)hashTemp.get(PysProductosSolicitadosBean.C_IDPRODUCTO);
					String idProductoInstitucion = (String)hashTemp.get(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
					String idPeticionConsulta = (String)hashTemp.get(PysProductosSolicitadosBean.C_IDPETICION);
					//Si no es solicitud de baja nos ahorramos el siguiente acceso a base de datos
					//aunque esto se haya metido por lo de los estados de pago
					if(isSolicitudBaja){
						if (this.getTipoPeticion(idInstitucion,idPersona,idTipoProducto,idProducto,idProductoInstitucion,idPeticionConsulta))
							hashTemp.put("ESTADO_BAJA","SI");
						else
							hashTemp.put("ESTADO_BAJA","NO");
						
						String estadoPago = (String)hashTemp.get("ESTADOPAGO");
						// JTA segun requeriemiento se puede anular siempre que no este facturado(mirar el procediemineto F_SIGA_ESTADOCOMPRA
						//ya que cuando esta cobrado con tarjeta no tiene por que estar facturado!) 
						if(estadoPago==null || estadoPago.equalsIgnoreCase("estados.compra.pendiente"))
							resultados.add((Hashtable)(hashTemp));
						
					} else {
						resultados.add((Hashtable)(hashTemp));
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
	
	public Vector getDireccionLetradoSalida(String idPersona,String idInstitucion) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			
			String sql = "SELECT DIR.Domicilio DOMICILIO_LETRADO," +
					" dir.codigopostal CP_LETRADO," +
					" nvl(dir.poblacionextranjera, pob.nombre) POBLACION_LETRADO" +
					" from CEN_DIRECCIONES DIR, CEN_DIRECCION_TIPODIRECCION TIP,CEN_POBLACIONES pob" +
					" where dir.idinstitucion = tip.idinstitucion " +
					" and dir.idpersona = tip.idpersona  " +
					" and dir.iddireccion = tip.iddireccion " +
					" and dir.idpoblacion = pob.IDPOBLACION(+)" +
					" and tip.idtipodireccion = 2 " +
					" and dir.fechabaja is null "+
					" and dir.idinstitucion = :1 "+
					" and dir.idpersona = :2 "+
					" and rownum = 1 ";

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDireccionLetrado");
		}
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
	public boolean confirmarProducto (Integer idInstitucion, Long idPeticion, Integer idTipoProducto, Long idProducto, Long idProductoInstitucion, Double importeAnticipado,String fechaEfectiva) throws ClsExceptions, SIGAException {

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
						double importeAnticipadoTarjeta = new Double(""+productoBean.getCantidad()).doubleValue() * (productoBean.getValor().doubleValue() + ( (productoBean.getValor().doubleValue()*productoBean.getPorcentajeIVA().floatValue()) / 100 ));
						compraBean.setImporteAnticipado(new Double(importeAnticipadoTarjeta));	
					}
					else {
						if (productoBean.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_METALICO)
							compraBean.setImporteAnticipado(importeAnticipado);
						else
							compraBean.setImporteAnticipado(new Double(0));
					}
				}
				

				compraBean.setIva(productoBean.getPorcentajeIVA());
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
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA, a.getIdIva());
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
		PysProductosSolicitadosAdm adm = new PysProductosSolicitadosAdm(this.usrbean);
		
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
		if (a.getIdFormaPago()!=null && a.getIdFormaPago().intValue()==ClsConstants.TIPO_FORMAPAGO_METALICO){
		   
		    UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, "");
		}else{
			UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, a.getIdCuenta());
		}
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO, a.getIdFormaPago());
		//INC_07291_SIGA Porcentajes de IVA.
		UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA, a.getIdIva());
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
	 * Obtiene si hay una peticion de baja.
	 * @param idinstitucion
	 * @param idpeticion
	 * @return boolean
	 */
	public boolean getTipoPeticion(Integer idinstitucion, Long idpersona, String idTipoProducto, String idProducto, String idProductoInstitucion, String idpeticion){
		boolean hay = false;

		try {
			String where = " SELECT 1 "+
						   " FROM "+
						   PysPeticionCompraSuscripcionBean.T_NOMBRETABLA+" pet,"+
						   PysProductosSolicitadosBean.T_NOMBRETABLA+" prod"+
						   " WHERE " +
						   " pet."+PysPeticionCompraSuscripcionBean.C_IDINSTITUCION+"="+idinstitucion.toString()+
						   " AND pet."+PysPeticionCompraSuscripcionBean.C_IDPERSONA+"="+idpersona.toString()+
						   " AND pet."+PysPeticionCompraSuscripcionBean.C_TIPOPETICION+"='"+ClsConstants.TIPO_PETICION_COMPRA_BAJA+"'"+
						   " AND pet."+PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION+"="+ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE+
						   " AND pet."+PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA+"="+idpeticion+
						   //PRODUCTO:
						   " AND prod."+PysProductosSolicitadosBean.C_IDTIPOPRODUCTO+"="+idTipoProducto+
						   " AND prod."+PysProductosSolicitadosBean.C_IDPRODUCTO+"="+idProducto+
						   " AND prod."+PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION+"="+idProductoInstitucion+
						   //JOIN:
						   " AND prod."+PysProductosSolicitadosBean.C_IDINSTITUCION+"=pet."+PysPeticionCompraSuscripcionBean.C_IDINSTITUCION+
						   " AND prod."+PysProductosSolicitadosBean.C_IDPETICION+"=pet."+PysPeticionCompraSuscripcionBean.C_IDPETICION+
						   " AND prod."+PysProductosSolicitadosBean.C_IDPERSONA+"=pet."+PysPeticionCompraSuscripcionBean.C_IDPERSONA;
			Vector v = this.selectGenerico(where);
			if (!v.isEmpty())
				hay = true;
		} catch(Exception e){
			hay = false;
		}
		return hay;
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
					Hashtable registro2 = new Hashtable();
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
	

	public Vector obtenerProductosSolicitados(PysPeticionCompraSuscripcionBean beanPeticion) throws ClsExceptions {
		Vector salida = new Vector();
		try {
			salida = this.select("where idinstitucion="+beanPeticion.getIdInstitucion().toString()+ " and idpeticion="+beanPeticion.getIdPeticion().toString());
		} catch(Exception e){
			throw new ClsExceptions(e, "Error al obtener productos solicitados.");
		}
		return salida;
	}		
	
	private String getQueryProductosSolicitadosBind(Hashtable datos,
			Integer idInstitucion,Hashtable codigos) throws ClsExceptions, SIGAException {

		int contador=0;
		
		Long idPeticion = UtilidadesHash.getLong(datos,
				PysProductosSolicitadosBean.C_IDPETICION);
		Long idPersona = UtilidadesHash.getLong(datos,
				PysPeticionCompraSuscripcionBean.C_IDPERSONA);
		String tipoPeticion = UtilidadesHash.getString(datos,
				PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
		String fechaDesde = UtilidadesHash.getString(datos, "FECHA_DESDE");
		String fechaHasta = UtilidadesHash.getString(datos, "FECHA_HASTA");
		String distintoCampoAceptado = UtilidadesHash.getString(datos, PysProductosSolicitadosBean.C_ACEPTADO);

		StringBuffer select = new StringBuffer(
				"SELECT 'PRODUCTO' AS CONSULTA, ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPETICION);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDINSTITUCIONORIGEN);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDCUENTA);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_CANTIDAD);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_VALOR);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_PORCENTAJEIVA);
		select.append(", ");
		//INC_07291_SIGA Porcentajes de IVA.
		select.append(PysTipoIvaBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysTipoIvaBean.C_VALOR);
		select.append(" VALORIVA, ");
		
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_ACEPTADO);
		select.append(", ");
		select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
		select.append(", ");
		select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysPeticionCompraSuscripcionBean.C_FECHA);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDFORMAPAGO);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDTIPOENVIOS);
		select.append(", ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_NOFACTURABLE);
		select.append(", ");

		// Forma Pago
		select.append("(SELECT ");
		select.append(UtilidadesMultidioma.getCampoMultidioma(
				PysFormaPagoBean.T_NOMBRETABLA + "."
						+ PysFormaPagoBean.C_DESCRIPCION, this.usrbean
						.getLanguage()));
		select.append(" FROM ");
		select.append(PysFormaPagoBean.T_NOMBRETABLA);
		select.append(" WHERE ");
		select.append(PysFormaPagoBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysFormaPagoBean.C_IDFORMAPAGO);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDFORMAPAGO);
		select.append(") AS FORMAPAGO, ");

		// Fecha Efectiva. Lo comento ya que tarda mucho. La sacare accediendo con datos
		/*select.append("NVL((SELECT ");
		select.append(PysCompraBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysCompraBean.C_FECHA);
		select.append(" FROM ");
		select.append(PysCompraBean.T_NOMBRETABLA);
		select.append(" WHERE ");
		select.append(PysCompraBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysCompraBean.C_IDINSTITUCION);
		select.append("=");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		select.append(" AND ");
		select.append(PysCompraBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysCompraBean.C_IDPETICION);
		select.append("=");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPETICION);
		select.append(" AND ");
		select.append(PysCompraBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysCompraBean.C_IDTIPOPRODUCTO);
		select.append("=");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
		select.append(" AND ");
		select.append(PysCompraBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysCompraBean.C_IDPRODUCTO);
		select.append("=");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
		select.append(" AND ");
		select.append(PysCompraBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysCompraBean.C_IDPRODUCTOINSTITUCION);
		select.append("=");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
		select.append("),(SELECT max(petco.fecha)");
		select.append(" FROM PYS_PETICIONCOMPRASUSCRIPCION petco,PYS_PRODUCTOSSOLICITADOS prodsol");
		
		select.append(" WHERE  petco.IDINSTITUCION=");
		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		select.append(":"+contador);
		select.append(" AND petco.IDPERSONA=");
		contador++;
		codigos.put(new Integer(contador),idPersona.toString());
		select.append(":"+contador);
		select.append(" and petco.idestadopeticion<>10");
		select.append(" AND petco.TIPOPETICION='B'");
		select.append(" AND petco.IDPETICIONALTA=PYS_PETICIONCOMPRASUSCRIPCION.Idpeticion");
		select.append(" AND prodsol.IDTIPOPRODUCTO=PYS_PRODUCTOSSOLICITADOS.IDTIPOPRODUCTO");
		select.append(" AND prodsol.IDPRODUCTO=PYS_PRODUCTOSSOLICITADOS.Idproducto");
		select.append(" AND prodsol.IDPRODUCTOINSTITUCION=PYS_PRODUCTOSSOLICITADOS.Idproductoinstitucion");
		select.append(" AND prodsol.IDINSTITUCION=petco.IDINSTITUCION");
		select.append(" AND prodsol.IDPETICION=petco.IDPETICION");
		select.append(" AND prodsol.IDPERSONA=petco.IDPERSONA)) AS FECHAEFEC, ");
		*/
		// Concepto
		select.append("(SELECT ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_DESCRIPCION);
		select.append(" FROM ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(" WHERE ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_IDINSTITUCION);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		select.append(" AND ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
		select.append(" AND ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_IDPRODUCTO);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
		select.append(" AND ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
		select.append(") AS CONCEPTO, ");

		
		// Solicitar Baja. Lo comento ya que desde aqui no se solicita la baja
		/*
		select.append("(SELECT ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_SOLICITARBAJA);
		select.append(" FROM ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(" WHERE ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_IDINSTITUCION);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		select.append(" AND ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
		select.append(" AND ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_IDPRODUCTO);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPRODUCTO);
		select.append(" AND ");
		select.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
		select.append(") AS SOLICITARBAJA, ");
		*/
		// Cuenta
		select.append("NVL((SELECT ");
		select.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		select.append(".");
		select.append(CenCuentasBancariasBean.C_IBAN);
		select.append(" FROM ");
		select.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		select.append(" WHERE ");
		select.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		select.append(".");
		select.append(CenCuentasBancariasBean.C_IDCUENTA);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDCUENTA);
		select.append(" AND ");
		select.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		select.append(".");
		select.append(CenCuentasBancariasBean.C_IDINSTITUCION);
		select.append(" = ");
		select.append(idInstitucion);
		select.append(" AND ");
		select.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		select.append(".");
		select.append(CenCuentasBancariasBean.C_IDPERSONA);
		select.append(" = ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPERSONA);
		select.append("),'-') AS NCUENTA ");
		//Estado Pago. Tarda mucho Lo comento. Accedere a ello con datos
		//select.append(" F_SIGA_ESTADOCOMPRA(PYS_PRODUCTOSSOLICITADOS.IDINSTITUCION,PYS_PRODUCTOSSOLICITADOS.IDPETICION,PYS_PRODUCTOSSOLICITADOS.IDPRODUCTO,PYS_PRODUCTOSSOLICITADOS.IDTIPOPRODUCTO,PYS_PRODUCTOSSOLICITADOS.IDPRODUCTOINSTITUCION) AS ESTADOPAGO ");

		select.append(" FROM ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(", ");
		select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		select.append(" ");
		//INC_07291_SIGA Porcentajes de IVA.
		select.append(", ");
		select.append(PysTipoIvaBean.T_NOMBRETABLA);
		select.append(" ");

		select.append(" WHERE ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		select.append(" = ");
		select.append(idInstitucion);
		select.append(" AND ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDPETICION);
		select.append(" = ");
		select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysPeticionCompraSuscripcionBean.C_IDPETICION);
		select.append(" AND ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_IDINSTITUCION);
		select.append(" = ");
		select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION);
		
		select.append(" AND ");
		select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysProductosSolicitadosBean.C_PORCENTAJEIVA);
		select.append(" = ");
		select.append(PysTipoIvaBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysTipoIvaBean.C_IDTIPOIVA);

		if (idPeticion != null) {
			select.append(" AND ");
			select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			select.append(".");
			select.append(PysProductosSolicitadosBean.C_IDPETICION);
			select.append(" =");
			contador++;
			codigos.put(new Integer(contador),idPeticion.toString());
			select.append(":"+contador);
			
		}
		if (idPersona != null) {
			select.append(" AND ");
			select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			select.append(".");
			select.append(PysPeticionCompraSuscripcionBean.C_IDPERSONA);
			select.append(" =");
			contador++;
			codigos.put(new Integer(contador),idPersona.toString());
			select.append(":"+contador);
		}
		if (tipoPeticion != null) {
			select.append(" AND ");
			select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
			select.append(".");
			select.append(PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
			select.append(" =");
			contador++;
			codigos.put(new Integer(contador),tipoPeticion);
			select.append(":"+contador);
		}

		if (distintoCampoAceptado != null) {
			select.append( " AND ");  
			select.append(PysProductosSolicitadosBean.T_NOMBRETABLA);
			select.append(".");
			select.append(PysProductosSolicitadosBean.C_ACEPTADO); select.append(" <> "); 
			contador++;
			codigos.put(new Integer(contador),distintoCampoAceptado);
			select.append(":"+contador);
			
		}

		// Fechas
		if (fechaDesde != null) {
			if (fechaDesde.indexOf("SYSDATE") != -1) {
				select.append(" AND ");
				select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				select.append(".");
				select.append(PysPeticionCompraSuscripcionBean.C_FECHA);
				select.append(" >=");
				select.append(fechaDesde);
				
				
				
			} else {
				select.append(" AND ");
				select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				select.append(".");
				select.append(PysPeticionCompraSuscripcionBean.C_FECHA);
				select.append(" >= ");
				select.append(" TO_DATE('");
				select.append(fechaDesde);
				select.append("', '");
				select.append(ClsConstants.DATE_FORMAT_SQL);
				select.append("') ");
			}
		}
		if (fechaHasta != null) {
			if (fechaHasta.indexOf("SYSDATE") != -1) {
				select.append(" AND ");
				select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				select.append(".");
				select.append(PysPeticionCompraSuscripcionBean.C_FECHA);
				select.append(" <=");
				select.append(fechaHasta);
				
			} else {
				select.append(" AND ");
				select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
				select.append(".");
				select.append(PysPeticionCompraSuscripcionBean.C_FECHA);
				select.append(" <= ");
				select.append(" TO_DATE('");
				select.append(fechaHasta);
				select.append("', '");
				select.append(ClsConstants.DATE_FORMAT_SQL);
				select.append("') ");
			}
		}

		select.append(" ORDER BY ");
		select.append(PysPeticionCompraSuscripcionBean.T_NOMBRETABLA);
		select.append(".");
		select.append(PysPeticionCompraSuscripcionBean.C_FECHA);
		select.append(" DESC ");

		// String consulta = select); select.append( from); select.append(
		// where); select.append( orderBy;

		return select.toString();
		

	}
}