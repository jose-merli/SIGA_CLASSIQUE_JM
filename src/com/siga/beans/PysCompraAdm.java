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
							PysCompraBean.C_NUMEROLINEA, 			PysCompraBean.C_IDTIPOIVA,
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
			bean.setIdTipoIva(UtilidadesHash.getFloat(hash,PysCompraBean.C_IDTIPOIVA));
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
			UtilidadesHash.set(hash, PysCompraBean.C_IDTIPOIVA, b.getIdTipoIva());
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
	
	/**
	 * Obtener las compras ordenadas de una peticion
	 * @param beanPeticion
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<PysCompraBean> obtenerComprasPeticion(PysPeticionCompraSuscripcionBean beanPeticion) throws ClsExceptions {
	    Vector<PysCompraBean> salida = new Vector<PysCompraBean>();
	    try {
    		String[] campos = this.getCamposBean();
    		
    		String sql = " SELECT ";
    		for (int i=0; i<campos.length; i++) {
    			sql += (i>0 ? ", " : " ") + PysCompraBean.T_NOMBRETABLA + "." + campos[i];
    		}

    		sql += " FROM " + PysCompraBean.T_NOMBRETABLA + ", " +
					PysProductosSolicitadosBean.T_NOMBRETABLA +
        		" WHERE " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + " = " + beanPeticion.getIdInstitucion().toString() +
        			" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPETICION + " = " + beanPeticion.getIdPeticion().toString() +
        			" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + 
        			" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPETICION +  
        			" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTO +  
        			" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDTIPOPRODUCTO +  
        			" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTOINSTITUCION +  
        		" ORDER BY " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + ", " +
        			PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPETICION + ", " +
        			PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_ORDEN + ", " +
        			PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDTIPOPRODUCTO + ", " +
        			PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTO + ", " +
        			PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTOINSTITUCION;
	    	
	        salida = this.selectSQL(sql);
	        
		} catch(Exception e) {
			throw new ClsExceptions(e,"Error al buscar las series de facturacion candidatas.");
		}
	    
		return salida;
	}
	
	/**
	 * Obtiene la compra de una solicitud
	 * @param idInstitucion
	 * @param idSolicitudCertificado
	 * @return
	 * @throws ClsExceptions
	 */
    public PysCompraBean obtenerCompraCertificado(String idInstitucion, String idSolicitudCertificado) throws ClsExceptions {
        PysCompraBean salida = null;
    	try {
    		String[] campos = this.getCamposBean();
    		
    		String sql = " SELECT ";
    		for (int i=0; i<campos.length; i++) {
    			sql += (i>0 ? ", " : " ") + PysCompraBean.T_NOMBRETABLA + "." + campos[i];
    		}
    						
			sql += " FROM " + PysCompraBean.T_NOMBRETABLA + ", " +
						CerSolicitudCertificadosBean.T_NOMBRETABLA +
					" WHERE " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + " = " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCION +
						" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPETICION + " = " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO +
						" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDTIPOPRODUCTO + " = " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO +
						" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTO + " = " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO +
						" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTOINSTITUCION + " = " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION +
						" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCION + " = " + idInstitucion +
						" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDSOLICITUD + " = " + idSolicitudCertificado;
    		
    		Hashtable<String, Object> hash = this.selectGenericoHash(sql);
    		if (hash==null) {
    			throw new ClsExceptions("No se ha encontrado la compra relacionada con la solicitud de certificado");
    		}
    		salida = (PysCompraBean) this.hashTableToBeanInicial(hash); 

    	}  catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener Historico.");
    	}

    	return salida;
    }	
}
