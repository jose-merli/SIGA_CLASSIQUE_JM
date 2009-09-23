package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author RGG
 */
	public class AdmTipoInformeAdm extends MasterBeanAdministrador{

		public AdmTipoInformeAdm(UsrBean usuario) 
		{
			super(AdmTipoInformeBean.T_NOMBRETABLA, usuario);
		}

		protected String[] getCamposBean() {
			String [] campos = {AdmTipoInformeBean.C_IDTIPOINFORME,
								AdmTipoInformeBean.C_DESCRIPCION,
								AdmTipoInformeBean.C_IDTIPOINFORMEPADRE,
								AdmTipoInformeBean.C_TIPOFORMATO,
								AdmTipoInformeBean.C_FECHAMODIFICACION,
								AdmTipoInformeBean.C_USUMODIFICACION
								};
			return campos;
		}

		protected String[] getClavesBean() {
			String [] claves = {AdmTipoInformeBean.C_IDTIPOINFORME};
			return claves;
		}

		protected String[] getOrdenCampos() {
			return getClavesBean();
		}

		protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
			AdmTipoInformeBean bean = null;
			
			try {
				bean = new AdmTipoInformeBean();
				bean.setIdTipoInforme(UtilidadesHash.getString(hash, AdmTipoInformeBean.C_IDTIPOINFORME));
				bean.setDescripcion(UtilidadesHash.getString(hash, AdmTipoInformeBean.C_DESCRIPCION));
				bean.setIdTipoInformePadre(UtilidadesHash.getString(hash, AdmTipoInformeBean.C_IDTIPOINFORMEPADRE));
				bean.setTipoFormato(UtilidadesHash.getString(hash, AdmTipoInformeBean.C_TIPOFORMATO));
				bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmTipoInformeBean.C_USUMODIFICACION));
				bean.setFechaMod(UtilidadesHash.getString(hash, AdmTipoInformeBean.C_FECHAMODIFICACION));
			}
			catch (Exception e) { 
				bean = null;	
				throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
			}
			return bean;
		}

		protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
			Hashtable htData = null;
			try {
				htData = new Hashtable();
				AdmTipoInformeBean b = (AdmTipoInformeBean) bean;
				UtilidadesHash.set(htData, AdmTipoInformeBean.C_IDTIPOINFORME, 		b.getIdTipoInforme());
				UtilidadesHash.set(htData, AdmTipoInformeBean.C_DESCRIPCION, 	b.getDescripcion());
				UtilidadesHash.set(htData, AdmTipoInformeBean.C_IDTIPOINFORMEPADRE, 	b.getIdTipoInformePadre());
				UtilidadesHash.set(htData, AdmTipoInformeBean.C_TIPOFORMATO, 	b.getTipoFormato());
				UtilidadesHash.set(htData, AdmTipoInformeBean.C_USUMODIFICACION, 	b.getUsuMod());
				UtilidadesHash.set(htData, AdmTipoInformeBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			}
			catch (Exception e) {
				htData = null;
				throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
			}
			return htData;	
		}
	
		/**
		 * Obtiene un AdmTipoInformeBean por clave
		 * @param idInstitucion
		 * @param idTipoInforme
		 * @return Vector de AdmInformeBean
		 * @throws ClsExceptions
		 */
		public AdmTipoInformeBean obtenerTipoInforme(String idTipoInforme) throws ClsExceptions {
		    AdmTipoInformeBean salida = null;
		    try {
		        Vector v = this.select("WHERE IDTIPOINFORME='"+idTipoInforme+"'");
		        if (v!=null && v.size()>0) {
		            salida = (AdmTipoInformeBean)v.get(0); 
		        }
		    } catch (ClsExceptions e) {
		        throw e;
		    } catch (Exception e) {
		        throw new ClsExceptions(e,"Error al obtener el tipoplantilla: "+ e.toString());
		    }
		    return salida;
		}
		
}	

