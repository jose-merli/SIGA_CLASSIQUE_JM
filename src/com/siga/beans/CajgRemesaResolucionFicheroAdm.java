package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class CajgRemesaResolucionFicheroAdm extends MasterBeanAdministrador {

	public CajgRemesaResolucionFicheroAdm(UsrBean usu) {
		super(CajgRemesaResolucionFicheroBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String[] campos = { CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCIONFICHERO
				, CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION
				, CajgRemesaResolucionFicheroBean.C_IDINSTITUCION
				, CajgRemesaResolucionFicheroBean.C_NUMEROLINEA
				, CajgRemesaResolucionFicheroBean.C_LINEA
				, CajgRemesaResolucionFicheroBean.C_IDERRORESREMESARESOL
				, CajgRemesaResolucionFicheroBean.C_PARAMETROSERROR
				};
		return campos;
	}

	protected String[] getClavesBean() {
		String[] campos = { CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCIONFICHERO };
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
		CajgRemesaResolucionFicheroBean bean = null;
		try {
			bean = new CajgRemesaResolucionFicheroBean();
			bean.setIdRemesaResolucionFichero(UtilidadesHash.getInteger(hash, CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCIONFICHERO));
			bean.setIdRemesaResolucion(UtilidadesHash.getInteger(hash, CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CajgRemesaResolucionFicheroBean.C_IDINSTITUCION));
			bean.setNumeroLinea(UtilidadesHash.getInteger(hash, CajgRemesaResolucionFicheroBean.C_NUMEROLINEA));
			bean.setLinea(UtilidadesHash.getString(hash, CajgRemesaResolucionFicheroBean.C_LINEA));			
			bean.setIdErroresRemesaResol(UtilidadesHash.getInteger(hash, CajgRemesaResolucionFicheroBean.C_IDERRORESREMESARESOL));
			bean.setParametrosError(UtilidadesHash.getString(hash, CajgRemesaResolucionFicheroBean.C_PARAMETROSERROR));
			
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
			CajgRemesaResolucionFicheroBean b = (CajgRemesaResolucionFicheroBean) bean;

			UtilidadesHash.set(hash, CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCIONFICHERO, b.getIdRemesaResolucionFichero());
			UtilidadesHash.set(hash, CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION, b.getIdRemesaResolucion());
			UtilidadesHash.set(hash, CajgRemesaResolucionFicheroBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CajgRemesaResolucionFicheroBean.C_NUMEROLINEA, b.getNumeroLinea());
			UtilidadesHash.set(hash, CajgRemesaResolucionFicheroBean.C_LINEA, b.getLinea());
			UtilidadesHash.set(hash, CajgRemesaResolucionFicheroBean.C_IDERRORESREMESARESOL, b.getIdErroresRemesaResol());
			UtilidadesHash.set(hash, CajgRemesaResolucionFicheroBean.C_PARAMETROSERROR, b.getParametrosError());

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
	public int seleccionarMaximo() throws ClsExceptions {
		RowsContainer rc = null;
		int numeroMaximo = 1;

		try {
			rc = new RowsContainer();

			String sql = "SELECT nvl((MAX(" + CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCIONFICHERO + ") + 1), 1) AS " + CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCIONFICHERO +
					" FROM " + nombreTabla;
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				
				numeroMaximo = Integer.parseInt(ht.get(CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCIONFICHERO).toString());
				
			}
		} catch (ClsExceptions e) {
			throw e;
		}

		return numeroMaximo;
	}

}