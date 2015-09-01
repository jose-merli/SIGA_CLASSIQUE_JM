package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.redabogacia.sigaservices.app.autogen.model.GenFichero;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author Carlos Ruano Martínez 
 * @date 11/06/2015
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class GenFicheroAdm  extends MasterBeanAdministrador {

	public GenFicheroAdm(UsrBean usuario) {
		super(GenFichero.T_GEN_FICHERO, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = { GenFichero.C_IDFICHERO, GenFichero.C_IDINSTITUCION, GenFichero.C_EXTENSION, GenFichero.C_DESCRIPCION, GenFichero.C_DIRECTORIO, GenFichero.C_FECHAMODIFICACION, GenFichero.C_USUMODIFICACION};
		return campos;
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	@Override
	protected String[] getClavesBean() {
		String[] claves = { GenFichero.C_IDFICHERO, GenFichero.C_IDINSTITUCION};
		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	@Override
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		GenFichero bean = null;
		GenFicheroBean ficheroBean = new GenFicheroBean();
		try {
			bean = new GenFichero();
			bean.setIdfichero(UtilidadesHash.getLong(hash, GenFichero.C_IDFICHERO));
			bean.setIdinstitucion(UtilidadesHash.getShort(hash, GenFichero.C_IDINSTITUCION));
			bean.setExtension(UtilidadesHash.getString(hash, GenFichero.C_EXTENSION));
			bean.setDescripcion(UtilidadesHash.getString(hash, GenFichero.C_DESCRIPCION));
			bean.setDirectorio(UtilidadesHash.getString(hash, GenFichero.C_DIRECTORIO));
			bean.setUsumodificacion(UtilidadesHash.getInteger(hash, GenFichero.C_USUMODIFICACION));
			String dateFechaMod = UtilidadesHash.getString(hash, GenFichero.C_FECHAMODIFICACION);
			if(dateFechaMod != null && !dateFechaMod.equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				Date fechaMod = sdf.parse(dateFechaMod);
				bean.setFechamodificacion(fechaMod);
			}
			ficheroBean.setFichero(bean);

		}catch (Exception e) {
			bean = null;
			throw new ClsExceptions(e, "Error al construir el bean a partir del hashTable");
		}

		return ficheroBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			GenFichero b = ((GenFicheroBean) bean).getFichero();
			UtilidadesHash.set(htData, GenFichero.C_IDFICHERO, b.getIdfichero());
			UtilidadesHash.set(htData, GenFichero.C_IDINSTITUCION, b.getIdinstitucion());
			UtilidadesHash.set(htData, GenFichero.C_EXTENSION, b.getExtension());
			UtilidadesHash.set(htData, GenFichero.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, GenFichero.C_DIRECTORIO, b.getDirectorio());
			UtilidadesHash.set(htData, GenFichero.C_USUMODIFICACION, b.getUsumodificacion());
			UtilidadesHash.set(htData, GenFichero.C_FECHAMODIFICACION, b.getFechamodificacion());			

		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions(e, "Error al crear el hashTable a partir del bean");
		}
		return htData;
	}

	/**
	 * @param idinstitucion
	 * @param idfichero
	 * @return
	 * @throws ClsExceptions 
	 */
	public GenFichero select(Short idinstitucion, Long idfichero) throws ClsExceptions {
		GenFichero genFichero = null;
		Hashtable<String, Object> hash = new Hashtable<String, Object>();
		hash.put(GenFichero.C_IDINSTITUCION, idinstitucion);
		hash.put(GenFichero.C_IDFICHERO, idfichero);
		try {
			Vector vector = this.select(hash);
			if (vector != null && vector.size() > 0) {
				GenFicheroBean bean = (GenFicheroBean) vector.get(0);
				genFichero = bean.getFichero();
			}

		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en ScsDocumentacionEJGAdm");
		}

		return genFichero;
	}

	/** 
	 * Prepara una tabla hash para insertarla en la tabla. <br/>
	 * Obtiene el campo IDDOCUMENTACION, <br/>	 
	 * @return  Long  - IdSolicitud secuencial  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getNuevoId() throws ClsExceptions {
		RowsContainer rc = null;
		Long id = null;
		String sql = "  SELECT SEQ_GENFICHERO.NEXTVAL FROM DUAL ";

		try {
			rc = new RowsContainer();
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				id = Long.valueOf((String) fila.getRow().get("NEXTVAL"));
			}

		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'getNuevoId' en BBDD");
		}
		return id;
	}	
	
}
