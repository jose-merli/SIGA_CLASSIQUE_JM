package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class EcomColaAdm extends MasterBeanAdministrador {

	
	public enum OPERACION {
		OBTENER_PROCURADOR_ASIGNA (1);	
		
		private int id = -1;
		
		private OPERACION(int id) {
			this.id = id;
		}
		public int getId() {
			return this.id;
		}
	}
	
	protected EcomColaAdm(UsrBean usrBean) {
		super(EcomColaBean.T_NOMBRETABLA, usrBean);
	}

	
	@Override
	protected String[] getCamposBean() {
		String [] campos = {EcomColaBean.C_IDECOMCOLA,
				EcomColaBean.C_IDINSTITUCION,
				EcomColaBean.C_IDESTADOCOLA,
				EcomColaBean.C_IDOPERACION,
				EcomColaBean.C_REINTENTO,
				EcomColaBean.C_FECHACREACION,			
				
				EcomColaBean.C_FECHAMODIFICACION,	
				EcomColaBean.C_USUMODIFICACION};
		return campos;
	}
	
	@Override
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			EcomColaBean b = (EcomColaBean) bean;
			
			UtilidadesHash.set(hash, EcomColaBean.C_IDECOMCOLA, b.getIdEcomCola());
			UtilidadesHash.set(hash, EcomColaBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(hash, EcomColaBean.C_IDESTADOCOLA, b.getIdEstadoCola());
			UtilidadesHash.set(hash, EcomColaBean.C_IDOPERACION, b.getIdOperacion());
			UtilidadesHash.set(hash, EcomColaBean.C_REINTENTO, b.getReintento());
			UtilidadesHash.set(hash, EcomColaBean.C_FECHACREACION, b.getFechaCreacion());
			
			UtilidadesHash.set(hash, EcomColaBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, EcomColaBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	

	@Override
	protected String[] getClavesBean() {		
		return new String[]{EcomColaBean.C_IDECOMCOLA};
	}

	@Override
	protected String[] getOrdenCampos() {		
		return new String[]{EcomColaBean.C_IDECOMCOLA};
	}

	@Override
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		EcomColaBean bean = null;
		try{
			bean = new EcomColaBean();
			bean.setIdEcomCola(UtilidadesHash.getSigaSequence(bean.getIdEcomCola(), hash, EcomColaBean.C_IDECOMCOLA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EcomColaBean.C_IDINSTITUCION));
			bean.setIdEstadoCola(UtilidadesHash.getInteger(hash, EcomColaBean.C_IDESTADOCOLA));
			bean.setIdOperacion(UtilidadesHash.getInteger(hash, EcomColaBean.C_IDOPERACION));
			bean.setReintento(UtilidadesHash.getInteger(hash, EcomColaBean.C_REINTENTO));
			bean.setFechaCreacion(UtilidadesHash.getString (hash,EcomColaBean.C_FECHACREACION));
			
			bean.setFechaMod(UtilidadesHash.getString (hash,EcomColaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,EcomColaBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

}
