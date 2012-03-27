package com.siga.beans;

import java.util.Calendar;
import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;

public class CajgRemesaResolucionAdm extends MasterBeanAdministrador {

	public CajgRemesaResolucionAdm(UsrBean usu) {
		super(CajgRemesaResolucionBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String[] campos = { CajgRemesaResolucionBean.C_IDREMESARESOLUCION
				, CajgRemesaResolucionBean.C_IDINSTITUCION
				, CajgRemesaResolucionBean.C_PREFIJO
				, CajgRemesaResolucionBean.C_NUMERO
				, CajgRemesaResolucionBean.C_SUFIJO
				, CajgRemesaResolucionBean.C_NOMBREFICHERO
				, CajgRemesaResolucionBean.C_OBSERVACIONES
				, CajgRemesaResolucionBean.C_FECHACARGA
				, CajgRemesaResolucionBean.C_FECHARESOLUCION
				, CajgRemesaResolucionBean.C_FECHAMODIFICACION
				, CajgRemesaResolucionBean.C_USUMODIFICACION
				, CajgRemesaResolucionBean.C_LOGGENERADO
				, CajgRemesaResolucionBean.C_IDTIPOREMESA};
		return campos;
	}

	protected String[] getClavesBean() {
		String[] campos = { CajgRemesaResolucionBean.C_IDREMESARESOLUCION, CajgRemesaResolucionBean.C_IDINSTITUCION };
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	/**
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgRemesaResolucionBean bean = null;
		try {
			bean = new CajgRemesaResolucionBean();
			bean.setIdRemesaResolucion(UtilidadesHash.getInteger(hash, CajgRemesaResolucionBean.C_IDREMESARESOLUCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CajgRemesaResolucionBean.C_IDINSTITUCION));

			bean.setPrefijo(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_PREFIJO));
			bean.setNumero(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_NUMERO));
			bean.setSufijo(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_SUFIJO));

			bean.setNombreFichero(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_NOMBREFICHERO));
			bean.setObservaciones(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_OBSERVACIONES));
			bean.setFechaCarga(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_FECHACARGA));
			bean.setFechaResolucion(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_FECHARESOLUCION));
			bean.setLogGenerado(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_LOGGENERADO));
			bean.setIdTipoRemesa(UtilidadesHash.getInteger(hash, CajgRemesaResolucionBean.C_IDTIPOREMESA));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, CajgRemesaResolucionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CajgRemesaResolucionBean.C_USUMODIFICACION));
			

		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions(e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try {
			hash = new Hashtable();
			CajgRemesaResolucionBean b = (CajgRemesaResolucionBean) bean;

			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_IDREMESARESOLUCION, b.getIdRemesaResolucion());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_IDINSTITUCION, b.getIdInstitucion());

			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_PREFIJO, b.getPrefijo());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_SUFIJO, b.getSufijo());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_NOMBREFICHERO, b.getNombreFichero());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_OBSERVACIONES, b.getObservaciones());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_FECHACARGA, b.getFechaCarga());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_FECHARESOLUCION, b.getFechaResolucion());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_LOGGENERADO, b.getLogGenerado());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_IDTIPOREMESA, b.getIdTipoRemesa());

			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, CajgRemesaResolucionBean.C_USUMODIFICACION, b.getUsuMod());
		} catch (Exception e) {
			hash = null;
			throw new ClsExceptions(e, "Error al construir el hashTable a partir del bean");
		}
		return hash;
	}


	/**
	 * 
	 * @param idinstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public String seleccionarMaximo(String idinstitucion) throws ClsExceptions {
		RowsContainer rc = null;
		String numeroMaximo = null;

		try {
			rc = new RowsContainer();

			String sql = "SELECT (MAX(" + CajgRemesaResolucionBean.C_IDREMESARESOLUCION + ") + 1) AS " + CajgRemesaResolucionBean.C_IDREMESARESOLUCION +
					" FROM " + nombreTabla + " WHERE IDINSTITUCION = " + idinstitucion;
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				if (ht.get(CajgRemesaResolucionBean.C_IDREMESARESOLUCION).equals("")) {
					numeroMaximo = "1";
				} else {
					numeroMaximo = ht.get(CajgRemesaResolucionBean.C_IDREMESARESOLUCION).toString();
				}
			}
		} catch (ClsExceptions e) {
			throw e;
		}

		return numeroMaximo;
	}
	
	/**
	 * Devuelve la maxima fecha de carga de un colegio o null si es la primera
	 * @param idinstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public Calendar getMaximaFechaCarga(String idinstitucion) throws ClsExceptions {
		RowsContainer rc = null;
		Calendar cal = null;

		try {
			rc = new RowsContainer();

			String sql = "SELECT MAX(" + CajgRemesaResolucionBean.C_FECHACARGA + ") AS " + CajgRemesaResolucionBean.C_FECHACARGA +
					" FROM " + nombreTabla + " WHERE " + CajgRemesaResolucionBean.C_IDINSTITUCION + " = " + idinstitucion;
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				if (!ht.get(CajgRemesaResolucionBean.C_FECHACARGA).equals("")) {
					cal = UtilidadesFecha.stringToCalendar((String)ht.get(CajgRemesaResolucionBean.C_FECHACARGA));				
				} 
			}
		} catch (ClsExceptions e) {
			throw e;
		}

		return cal;
	}
	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idTipoRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	public String getIdContador(String idInstitucion, String idTipoRemesa) throws ClsExceptions {
		String idContador = null;
		String IDCONTADOR = "IDCONTADOR";
		RowsContainer rc = new RowsContainer();
		String sql = "SELECT " + IDCONTADOR +
				" FROM CAJG_TIPOREMESA" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDTIPOREMESA = " + idTipoRemesa;
		if (rc.query(sql)) {
			Row fila = (Row) rc.get(0);
			Hashtable ht = fila.getRow();			
			idContador = (String) ht.get(IDCONTADOR);
		}
		return idContador;
	}

}