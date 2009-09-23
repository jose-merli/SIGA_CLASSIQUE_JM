/*
 * VERSIONES:
 *
 * raul.ggonzalez 09-02-2005 creacion
 */
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * Administrador de la tabla PYS_SUSCRIPCION
 */
public class PysSuscripcionAdm extends MasterBeanAdministrador {

	/**
	 * @param usuario
	 */
	public PysSuscripcionAdm(UsrBean usuario) {
		super(PysSuscripcionBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * @return array con los nombres de los campos
	 */
	protected String[] getCamposBean() {
		String[] campos = {	PysSuscripcionBean.C_CANTIDAD,				PysSuscripcionBean.C_DESCRIPCION, 			
							PysSuscripcionBean.C_FECHASUSCRIPCION,		PysSuscripcionBean.C_FECHABAJA,
							PysSuscripcionBean.C_FECHAMODIFICACION, 	PysSuscripcionBean.C_IDPERSONA,
							PysSuscripcionBean.C_IDFORMAPAGO, 			PysSuscripcionBean.C_IDINSTITUCION,
							PysSuscripcionBean.C_IDPETICION, 			PysSuscripcionBean.C_IDSERVICIO,
							PysSuscripcionBean.C_IDSERVICIOSINSTITUCION,PysSuscripcionBean.C_IDTIPOSERVICIOS,
							PysSuscripcionBean.C_IDSUSCRIPCION, 		PysSuscripcionBean.C_IMPORTEUNITARIO,
							PysSuscripcionBean.C_IMPORTEANTICIPADO,		PysSuscripcionBean.C_IDCUENTA,
							PysSuscripcionBean.C_USUMODIFICACION};
		return campos;
	}

	/**
	 * @return array con los nombres de los campos clave
	 */
	protected String[] getClavesBean() {
		String[] campos = {	PysSuscripcionBean.C_IDINSTITUCION,
							PysSuscripcionBean.C_IDTIPOSERVICIOS, 			PysSuscripcionBean.C_IDSERVICIO,
							PysSuscripcionBean.C_IDSERVICIOSINSTITUCION, 	PysSuscripcionBean.C_IDSUSCRIPCION};
		return campos;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/**
	 * Crea un bean a partir de un hastable con los campos de la tabla
	 * @param hash Hashtable con los datos
	 * @return MasterBean con el bean creado
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		PysSuscripcionBean bean = null;
		try{
			bean = new PysSuscripcionBean();
			bean.setCantidad(UtilidadesHash.getInteger(hash,PysSuscripcionBean.C_CANTIDAD));
			bean.setDescripcion(UtilidadesHash.getString(hash,PysSuscripcionBean.C_DESCRIPCION));
			bean.setFechaBaja(UtilidadesHash.getString(hash,PysSuscripcionBean.C_FECHABAJA));
			bean.setFechaSuscripcion(UtilidadesHash.getString(hash,PysSuscripcionBean.C_FECHASUSCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,PysSuscripcionBean.C_FECHAMODIFICACION));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,PysSuscripcionBean.C_IDFORMAPAGO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysSuscripcionBean.C_IDINSTITUCION));
			bean.setIdPeticion(UtilidadesHash.getLong(hash,PysSuscripcionBean.C_IDPETICION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,PysSuscripcionBean.C_IDPERSONA));
			bean.setIdServicio(UtilidadesHash.getLong(hash,PysSuscripcionBean.C_IDSERVICIO));
			bean.setIdSuscripcion(UtilidadesHash.getInteger(hash,PysSuscripcionBean.C_IDSUSCRIPCION));
			bean.setIdServicioInstitucion(UtilidadesHash.getLong(hash,PysSuscripcionBean.C_IDSERVICIOSINSTITUCION));
			bean.setIdTipoServicios(UtilidadesHash.getInteger(hash,PysSuscripcionBean.C_IDTIPOSERVICIOS));
			bean.setImporteAnticipado(UtilidadesHash.getDouble(hash,PysSuscripcionBean.C_IMPORTEANTICIPADO));
			bean.setImporteUnitario(UtilidadesHash.getDouble(hash,PysSuscripcionBean.C_IMPORTEUNITARIO));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,PysSuscripcionBean.C_IDCUENTA));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysSuscripcionBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * Crea un hastable con los campos de la tabla a partir de un bean
	 * @param bean MasterBean con el bean creado
	 * @return Hashtable con los datos 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			PysSuscripcionBean b = (PysSuscripcionBean) bean;
			UtilidadesHash.set(hash, PysSuscripcionBean.C_CANTIDAD, b.getCantidad());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_FECHASUSCRIPCION, b.getFechaSuscripcion());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_FECHABAJA, b.getFechaBaja());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDFORMAPAGO, b.getIdFormaPago());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDPETICION, b.getIdPeticion());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDSERVICIO, b.getIdServicio());			
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDSUSCRIPCION, b.getIdSuscripcion());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDSERVICIOSINSTITUCION, b.getIdServicioInstitucion());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDTIPOSERVICIOS, b.getIdTipoServicios());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IMPORTEUNITARIO, b.getImporteUnitario());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IMPORTEANTICIPADO, b.getImporteAnticipado());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_IDCUENTA, b.getIdCuenta());
			UtilidadesHash.set(hash, PysSuscripcionBean.C_USUMODIFICACION, b.getUsuMod());

		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/**
	 * Obtiene un nuevo ID de suscripcion 
	 * @author daniel.campos 12-01-05
	 * @version 1	 
	 * @param BeanCV datos del CV.
	 * @return nuevo ID.
	 */
	public Integer getNuevoID (PysSuscripcionBean bean)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + PysSuscripcionBean.C_IDSUSCRIPCION + ") + 1) AS " + PysSuscripcionBean.C_IDSUSCRIPCION + 
			  			 " FROM " + this.nombreTabla +
						 " WHERE "+ PysSuscripcionBean.C_IDINSTITUCION +" = " + bean.getIdInstitucion() +
						 " AND "+ PysSuscripcionBean.C_IDTIPOSERVICIOS +" = " + bean.getIdTipoServicios() +
						 " AND "+ PysSuscripcionBean.C_IDSERVICIO +" = " + bean.getIdServicio() +
						 " AND "+ PysSuscripcionBean.C_IDSERVICIOSINSTITUCION +" = " + bean.getIdServicioInstitucion();
						 
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Integer idSuscripcion = UtilidadesHash.getInteger(prueba, PysSuscripcionBean.C_IDSUSCRIPCION);
				if (idSuscripcion == null) {
					return new Integer(1);
				}
				else return idSuscripcion;								
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
	   				throw new ClsExceptions(e,"Error al ejecutar el 'getNuevoID' en B.D.");
	   			}
	   		}	
	    }
		return null;
	}	


}
