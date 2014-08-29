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
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
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
	
	public void eliminarFacturacionCompra(String idInstitucion, String idFactura) throws SIGAException {
		try {			
			String sql = " UPDATE " + PysCompraBean.T_NOMBRETABLA +
					" SET " + PysCompraBean.C_IDFACTURA + " = NULL," +
						PysCompraBean.C_NUMEROLINEA + " = NULL " +
					" WHERE " + PysCompraBean.C_IDINSTITUCION + " = " + idInstitucion +
						" AND " + PysCompraBean.C_IDFACTURA + " = " + idFactura;

			ClsMngBBDD.executeUpdate(sql);	
			
		} catch (Exception e) {
			throw new SIGAException(getError());
		}
	}
	
		/** Funcion selectGenerico (String consulta). Ejecuta la consulta que se le pasa en un string 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
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
			
		}  catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return datos;
	}
	
	/**
	 * Obtener las compras ordenadas de una peticion
	 * @param beanPeticion
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector obtenerComprasPorPeticion(PysPeticionCompraSuscripcionBean beanPeticion) throws ClsExceptions {
	    Vector salida = new Vector();
	    try {
	        salida = this.selectGenerico(
        			"SELECT PC." + PysCompraBean.C_ACEPTADO + ", " +
    					" PC." + PysCompraBean.C_CANTIDAD + ", " +
        				" PC." + PysCompraBean.C_DESCRIPCION + ", " +
    					" PC." + PysCompraBean.C_FECHA + ", " +
        				" PC." + PysCompraBean.C_FECHABAJA + ", " +
    					" PC." + PysCompraBean.C_FECHAMODIFICACION + ", " +
        				" PC." + PysCompraBean.C_IDFACTURA + ", " +
    					" PC." + PysCompraBean.C_IDFORMAPAGO + ", " + 
        				" PC." + PysCompraBean.C_IDINSTITUCION + ", " +
    					" PC." + PysCompraBean.C_IDPETICION + ", " +
        				" PC." + PysCompraBean.C_IDPRODUCTO + ", " +
    					" PC." + PysCompraBean.C_IDPRODUCTOINSTITUCION + ", " +
    					" PC." + PysCompraBean.C_IDTIPOPRODUCTO + ", " +
    					" PC." + PysCompraBean.C_IMPORTEUNITARIO + ", " +
    					" PC." + PysCompraBean.C_IMPORTEANTICIPADO + ", " +
    					" PC." + PysCompraBean.C_NUMEROLINEA + ", " +
    					" PC." + PysCompraBean.C_PORCENTAJEIVA + ", "+
    					" PC." + PysCompraBean.C_IDCUENTA + ", " +
    					" PC." + PysCompraBean.C_IDPERSONA + ", " + 
    					" PC." + PysCompraBean.C_IDCUENTADEUDOR + ", " +
    					" PC." + PysCompraBean.C_IDPERSONADEUDOR + ", " +
    					" PC." + PysCompraBean.C_USUMODIFICACION + ", " +
    					" PC." + PysCompraBean.C_NOFACTURABLE + 
					" FROM " + PysCompraBean.T_NOMBRETABLA + " PC, " +
						PysProductosSolicitadosBean.T_NOMBRETABLA + " PPS " +
	        		" WHERE PC." + PysCompraBean.C_IDINSTITUCION + " = " + beanPeticion.getIdInstitucion().toString() +
	        			" AND PC." + PysCompraBean.C_IDPETICION + " = " + beanPeticion.getIdPeticion().toString() +
	        			" AND PPS." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = PC." + PysCompraBean.C_IDINSTITUCION + 
	        			" AND PPS." + PysProductosSolicitadosBean.C_IDPETICION + " = PC." + PysCompraBean.C_IDPETICION +  
	        			" AND PPS." + PysProductosSolicitadosBean.C_IDPRODUCTO + " = PC." + PysCompraBean.C_IDPRODUCTO +  
	        			" AND PPS." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + " = PC." + PysCompraBean.C_IDTIPOPRODUCTO +  
	        			" AND PPS." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + " = PC." + PysCompraBean.C_IDPRODUCTOINSTITUCION +  
	        		" ORDER BY PC." + PysCompraBean.C_IDINSTITUCION + ", " +
	        			" PC." + PysCompraBean.C_IDPETICION + ", " +
	        			" PPS." + PysProductosSolicitadosBean.C_ORDEN + ", " +
	        			" PC." + PysCompraBean.C_IDTIPOPRODUCTO + ", " +
	        			" PC." + PysCompraBean.C_IDPRODUCTO + ", " +
	        			" PC." + PysCompraBean.C_IDPRODUCTOINSTITUCION);
	        
		} catch(Exception e) {
			throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
	    
		return salida;
	}
	
    public int compruebaNumeroFacturas(PysCompraBean compra) throws ClsExceptions {
        int salida = 0;
    	try {
    	    CerSolicitudCertificadosAdm adm = new CerSolicitudCertificadosAdm(this.usrbean);
    	    
    		String sql = " SELECT count(*) AS NUMERO " +
    				" FROM " + PysCompraBean.T_NOMBRETABLA +
    				" WHERE " + PysCompraBean.C_FECHABAJA + " IS NULL " +
    					" AND TRUNC(" + PysCompraBean.C_FECHA + ") = TRUNC(SYSDATE) " +
    					" AND " + PysCompraBean.C_IDPERSONA + " = " + compra.getIdPersona() +
    					" AND " + PysCompraBean.C_IDTIPOPRODUCTO + " = " + compra.getIdTipoProducto() + 
    					" AND " + PysCompraBean.C_IDPRODUCTO + " = " + compra.getIdProducto() + 
    					" AND " + PysCompraBean.C_IDINSTITUCION + " = " + compra.getIdInstitucion() +
    					" AND " + PysCompraBean.C_IDFACTURA + " IS NULL";
		    	
    		Vector v = adm.selectGenerico(sql);
    		if (v.size()>0) {
    			Hashtable h = (Hashtable) v.get(0);
    			salida = new Integer(UtilidadesHash.getString(h, "NUMERO")).intValue();
    		}

    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener Numero de facturas por compra.");
    	}

    	return salida;
    }	
}
