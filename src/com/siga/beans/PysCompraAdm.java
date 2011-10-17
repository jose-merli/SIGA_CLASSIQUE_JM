/*
 * Created on 08-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 */
public class PysCompraAdm extends MasterBeanAdministrador {

	/**
	 * @param usuario
	 */
	public PysCompraAdm(UsrBean usuario) {
		super(PysCompraBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = {	PysCompraBean.C_ACEPTADO,				PysCompraBean.C_CANTIDAD,
							PysCompraBean.C_DESCRIPCION, 			PysCompraBean.C_FECHA,
							PysCompraBean.C_FECHABAJA,			
							PysCompraBean.C_FECHAMODIFICACION, 		PysCompraBean.C_IDFACTURA,
							PysCompraBean.C_IDFORMAPAGO, 			PysCompraBean.C_IDINSTITUCION,
							PysCompraBean.C_IDPETICION, 			PysCompraBean.C_IDPRODUCTO,
							PysCompraBean.C_IDPRODUCTOINSTITUCION, 	PysCompraBean.C_IDTIPOPRODUCTO,
							PysCompraBean.C_IMPORTEUNITARIO, 		PysCompraBean.C_IMPORTEANTICIPADO,
							PysCompraBean.C_NUMEROLINEA, 			PysCompraBean.C_PORCENTAJEIVA,
							PysCompraBean.C_IDCUENTA, 				PysCompraBean.C_IDPERSONA,
							PysCompraBean.C_IDCUENTADEUDOR,			PysCompraBean.C_IDPERSONADEUDOR,
							PysCompraBean.C_USUMODIFICACION,		PysCompraBean.C_NOFACTURABLE};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String[] campos = {	PysCompraBean.C_IDINSTITUCION,
							PysCompraBean.C_IDPETICION, 			PysCompraBean.C_IDPRODUCTO,
							PysCompraBean.C_IDPRODUCTOINSTITUCION, 	PysCompraBean.C_IDTIPOPRODUCTO};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		PysCompraBean bean = null;
		try{
			bean = new PysCompraBean();
			bean.setAceptado(UtilidadesHash.getString(hash,PysCompraBean.C_ACEPTADO));
			bean.setCantidad(UtilidadesHash.getInteger(hash,PysCompraBean.C_CANTIDAD));
			bean.setDescripcion(UtilidadesHash.getString(hash,PysCompraBean.C_DESCRIPCION));
			bean.setFecha(UtilidadesHash.getString(hash,PysCompraBean.C_FECHA));
			bean.setFechaBaja(UtilidadesHash.getString(hash,PysCompraBean.C_FECHABAJA));
			bean.setFechaMod(UtilidadesHash.getString(hash,PysCompraBean.C_FECHAMODIFICACION));
			bean.setIdFactura(UtilidadesHash.getString(hash,PysCompraBean.C_IDFACTURA));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,PysCompraBean.C_IDFORMAPAGO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysCompraBean.C_IDINSTITUCION));
			bean.setIdPeticion(UtilidadesHash.getLong(hash,PysCompraBean.C_IDPETICION));
			bean.setIdProducto(UtilidadesHash.getLong(hash,PysCompraBean.C_IDPRODUCTO));
			bean.setIdProductoInstitucion(UtilidadesHash.getLong(hash,PysCompraBean.C_IDPRODUCTOINSTITUCION));
			bean.setIdTipoProducto(UtilidadesHash.getInteger(hash,PysCompraBean.C_IDTIPOPRODUCTO));
			bean.setImporteUnitario(UtilidadesHash.getDouble(hash,PysCompraBean.C_IMPORTEUNITARIO));
			bean.setImporteAnticipado(UtilidadesHash.getDouble(hash,PysCompraBean.C_IMPORTEANTICIPADO));
			bean.setIva(UtilidadesHash.getFloat(hash,PysCompraBean.C_PORCENTAJEIVA));
			bean.setIdPersona(UtilidadesHash.getLong(hash,PysCompraBean.C_IDPERSONA));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,PysCompraBean.C_IDCUENTA));
			bean.setIdPersonaDeudor(UtilidadesHash.getLong(hash,PysCompraBean.C_IDPERSONADEUDOR));
			bean.setIdCuentaDeudor(UtilidadesHash.getInteger(hash,PysCompraBean.C_IDCUENTADEUDOR));
			bean.setNumeroLinea(UtilidadesHash.getLong(hash,PysCompraBean.C_NUMEROLINEA));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysCompraBean.C_USUMODIFICACION));
			bean.setNoFacturable(UtilidadesHash.getString(hash,PysCompraBean.C_NOFACTURABLE));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			PysCompraBean b = (PysCompraBean) bean;
			UtilidadesHash.set(hash, PysCompraBean.C_ACEPTADO, b.getAceptado());
			UtilidadesHash.set(hash, PysCompraBean.C_CANTIDAD, b.getCantidad());
			UtilidadesHash.set(hash, PysCompraBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, PysCompraBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(hash, PysCompraBean.C_FECHABAJA, b.getFechaBaja());
			UtilidadesHash.set(hash, PysCompraBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, PysCompraBean.C_IDFACTURA, b.getIdFactura());
			UtilidadesHash.set(hash, PysCompraBean.C_IDFORMAPAGO, b.getIdFormaPago());
			UtilidadesHash.set(hash, PysCompraBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, PysCompraBean.C_IDPETICION, b.getIdPeticion());
			UtilidadesHash.set(hash, PysCompraBean.C_IDPRODUCTO, b.getIdProducto());			
			UtilidadesHash.set(hash, PysCompraBean.C_IDPRODUCTOINSTITUCION, b.getIdProductoInstitucion());
			UtilidadesHash.set(hash, PysCompraBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(hash, PysCompraBean.C_IMPORTEUNITARIO, b.getImporteUnitario());
			UtilidadesHash.set(hash, PysCompraBean.C_IMPORTEANTICIPADO, b.getImporteAnticipado());
			UtilidadesHash.set(hash, PysCompraBean.C_PORCENTAJEIVA, b.getIva());
			UtilidadesHash.set(hash, PysCompraBean.C_IDCUENTA, b.getIdCuenta());
			UtilidadesHash.set(hash, PysCompraBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, PysCompraBean.C_IDCUENTADEUDOR, b.getIdCuentaDeudor());
			UtilidadesHash.set(hash, PysCompraBean.C_IDPERSONADEUDOR, b.getIdPersonaDeudor());
			UtilidadesHash.set(hash, PysCompraBean.C_NUMEROLINEA, b.getNumeroLinea());
			UtilidadesHash.set(hash, PysCompraBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, PysCompraBean.C_NOFACTURABLE, b.getNoFacturable());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	public void eliminarFacturacionCompra(String idInstitucion, String idFactura) throws SIGAException
	{
		try
		{
			
			String sql = " UPDATE "+PysCompraBean.T_NOMBRETABLA+
			 " SET "+PysCompraBean.C_IDFACTURA+" = NULL,"+
			 PysCompraBean.C_NUMEROLINEA+" = NULL " +
			 " WHERE "+PysCompraBean.C_IDINSTITUCION+" = "+idInstitucion+
			 " AND "+PysCompraBean.C_IDFACTURA+" = "+idFactura;

			ClsMngBBDD.executeUpdate(sql);
			
/*
			String sWhereCompra=" WHERE " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + " = " + idInstitucion;
			sWhereCompra += " AND ";
			sWhereCompra += PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDFACTURA + " = " + idFactura;

			Vector vCompras = selectForUpdate(sWhereCompra);

			Enumeration eCompras = vCompras.elements();
			
			while(eCompras.hasMoreElements())
			{
				PysCompraBean bCompra= (PysCompraBean)eCompras.nextElement();
				
//				 Hash de modificación con los nuevos datos.
				Hashtable htCompra = new Hashtable();
				
				// Claves.
				htCompra.put(PysCompraBean.C_IDINSTITUCION,bCompra.getIdInstitucion());
				htCompra.put(PysCompraBean.C_IDPETICION,bCompra.getIdPeticion());
				htCompra.put(PysCompraBean.C_IDPRODUCTO,bCompra.getIdProducto());
				htCompra.put(PysCompraBean.C_IDPRODUCTOINSTITUCION,bCompra.getIdProductoInstitucion());
				htCompra.put(PysCompraBean.C_IDTIPOPRODUCTO,bCompra.getIdTipoProducto());

				// Nuevos valores.
				htCompra.put(PysCompraBean.C_IDFACTURA,"");
				htCompra.put(PysCompraBean.C_NUMEROLINEA,"");
				
				updateDirect(htCompra, getClavesBean(), new String[]{PysCompraBean.C_IDFACTURA, PysCompraBean.C_NUMEROLINEA});
			
			}
*/			
		}
		
		catch (Exception e)
		{
			throw new SIGAException(getError());
		}
	}
	
		/** Funcion selectGenerico (String consulta). Ejecuta la consulta que se le pasa en un string 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			

			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					PysCompraBean registro = (PysCompraBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	public Vector obtenerComprasPorPeticion(PysPeticionCompraSuscripcionBean beanPeticion) throws ClsExceptions {
	    Vector salida = new Vector();
	    try{
	        salida = this.selectGenerico("SELECT ACEPTADO, CANTIDAD, DESCRIPCION,FECHA,FECHABAJA,FECHAMODIFICACION, IDFACTURA, "+
									     " IDFORMAPAGO,IDINSTITUCION,IDPETICION, IDPRODUCTO,IDPRODUCTOINSTITUCION, "+
									     " IDTIPOPRODUCTO, IMPORTEUNITARIO, IMPORTEANTICIPADO, NUMEROLINEA,PORCENTAJEIVA, "+
									     " IDCUENTA, IDPERSONA,IDCUENTADEUDOR,IDPERSONADEUDOR,USUMODIFICACION,NOFACTURABLE "+
					" FROM PYS_COMPRA " +
	        		" WHERE idinstitucion="+beanPeticion.getIdInstitucion().toString()+" and idpeticion="+beanPeticion.getIdPeticion().toString() +
	        		" ORDER BY IDINSTITUCION,IDPETICION, IDTIPOPRODUCTO,IDPRODUCTO,IDPRODUCTOINSTITUCION");
		}
		catch(Exception e){
			throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
		return salida;
	}		
}
