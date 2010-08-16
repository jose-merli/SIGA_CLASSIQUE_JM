package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class AdmTipoFiltroInformeAdm extends MasterBeanAdministrador {

	public AdmTipoFiltroInformeAdm(UsrBean usuario) {
		super(AdmTipoFiltroInformeBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = { AdmTipoFiltroInformeBean.C_IDTIPOINFORME,
				AdmTipoFiltroInformeBean.C_IDTIPOFILTROINFORME,
				AdmTipoFiltroInformeBean.C_NOMBRECAMPO,
				AdmTipoFiltroInformeBean.C_OBLIGATORIO,
				AdmTipoFiltroInformeBean.C_FECHAMODIFICACION,
				AdmTipoFiltroInformeBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String[] claves = { AdmTipoFiltroInformeBean.C_IDTIPOINFORME,
				AdmTipoFiltroInformeBean.C_IDTIPOFILTROINFORME };
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		AdmTipoFiltroInformeBean bean = null;

		try {
			bean = new AdmTipoFiltroInformeBean();
			bean.setIdTipoInforme(UtilidadesHash.getString(hash,
					AdmTipoFiltroInformeBean.C_IDTIPOINFORME));
			bean.setIdTipoFiltroInforme(UtilidadesHash.getInteger(hash,
					AdmTipoFiltroInformeBean.C_IDTIPOFILTROINFORME));
			bean.setNombreCampo(UtilidadesHash.getString(hash,
					AdmTipoFiltroInformeBean.C_NOMBRECAMPO));
			bean.setObligatorio(UtilidadesHash.getString(hash,
					AdmTipoFiltroInformeBean.C_OBLIGATORIO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,
					AdmTipoFiltroInformeBean.C_USUMODIFICACION));
			bean.setFechaMod(UtilidadesHash.getString(hash,
					AdmTipoFiltroInformeBean.C_FECHAMODIFICACION));
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions(e,
					"Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			AdmTipoFiltroInformeBean b = (AdmTipoFiltroInformeBean) bean;
			UtilidadesHash.set(htData,
					AdmTipoFiltroInformeBean.C_IDTIPOINFORME, b
							.getIdTipoInforme());
			UtilidadesHash.set(htData,
					AdmTipoFiltroInformeBean.C_IDTIPOFILTROINFORME, b
							.getIdTipoFiltroInforme());
			UtilidadesHash.set(htData, AdmTipoFiltroInformeBean.C_NOMBRECAMPO,
					b.getNombreCampo());
			UtilidadesHash.set(htData, AdmTipoFiltroInformeBean.C_OBLIGATORIO,
					b.getObligatorio());
			UtilidadesHash.set(htData,
					AdmTipoFiltroInformeBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData,
					AdmTipoFiltroInformeBean.C_FECHAMODIFICACION, b
							.getFechaMod());
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions(e,
					"Error al crear el hashTable a partir del bean");
		}
		return htData;
	}

	/**
	 * Obtiene los AdmTipoFiltroInformeBean por clave
	 */
	public Vector obtenerTipoInforme(String idTipoInforme) throws ClsExceptions {
		AdmTipoFiltroInformeBean salida = null;
		try {
			Vector v = this.select("WHERE IDTIPOINFORME='" + idTipoInforme
					+ "'");
			return v;
		} catch (ClsExceptions e) {
			throw e;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener el tipoplantilla: "
					+ e.toString());
		}
	}

}
