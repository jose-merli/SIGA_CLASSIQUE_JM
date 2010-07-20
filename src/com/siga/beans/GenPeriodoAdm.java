package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

public class GenPeriodoAdm extends MasterBeanAdministrador {

	/**
	 * Constructor 
	 */
	public GenPeriodoAdm (UsrBean usuario) {
		super( GenPeriodoBean.T_NOMBRETABLA, usuario);
	}
  
	@Override
	protected String[] getCamposBean() {
		String[] campos = 
		{
				GenPeriodoBean.C_IDPERIODO,
				GenPeriodoBean.C_NOMBRE,
				GenPeriodoBean.C_DIAINICIO,
				GenPeriodoBean.C_MESINICIO,
				GenPeriodoBean.C_ANIOINICIO,
				GenPeriodoBean.C_DIAFIN,
				GenPeriodoBean.C_MESFIN,
				GenPeriodoBean.C_ANIOFIN
		};
		return campos;
	}

	@Override
	protected String[] getClavesBean() {
		String[] campos = {GenPeriodoBean.C_IDPERIODO};
		return campos;
	}

	@Override
	protected String[] getOrdenCampos() {
		String[] vector = {GenPeriodoBean.C_IDPERIODO};
		return vector;
	}

	@Override
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			GenPeriodoBean b = (GenPeriodoBean) bean;
			
			hash.put(GenPeriodoBean.C_IDPERIODO, String.valueOf(b.getIdPeriodo()));
			hash.put(GenPeriodoBean.C_NOMBRE, b.getNombre());
			hash.put(GenPeriodoBean.C_DIAINICIO, String.valueOf(b.getDiaInicio()));
			hash.put(GenPeriodoBean.C_MESINICIO, String.valueOf(b.getMesInicio()));
			hash.put(GenPeriodoBean.C_ANIOINICIO, String.valueOf(b.getAnioInicio()));
			hash.put(GenPeriodoBean.C_DIAFIN, String.valueOf(b.getDiaFin()));
			hash.put(GenPeriodoBean.C_MESFIN, String.valueOf(b.getMesFin()));
			hash.put(GenPeriodoBean.C_ANIOFIN, String.valueOf(b.getAnioFin()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	@Override
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		GenPeriodoBean bean = null;
		try{
			bean = new GenPeriodoBean();
			
			bean.setIdPeriodo(UtilidadesHash.getInteger(hash,GenPeriodoBean.C_IDPERIODO));
			bean.setNombre(UtilidadesHash.getString(hash,GenPeriodoBean.C_NOMBRE));
			bean.setDiaInicio(UtilidadesHash.getInteger(hash,GenPeriodoBean.C_DIAINICIO));
			bean.setMesInicio(UtilidadesHash.getInteger(hash,GenPeriodoBean.C_MESINICIO));
			bean.setAnioInicio(UtilidadesHash.getInteger(hash,GenPeriodoBean.C_ANIOINICIO));
			bean.setDiaFin(UtilidadesHash.getInteger(hash,GenPeriodoBean.C_DIAFIN));
			bean.setMesFin(UtilidadesHash.getInteger(hash,GenPeriodoBean.C_MESFIN));
			bean.setAnioFin(UtilidadesHash.getInteger(hash,GenPeriodoBean.C_ANIOFIN));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable obtenerPeriodo(String periodo, String idioma)
			throws ClsExceptions, SIGAException
	{
		String select = 
			"Select Idperiodo, " +
			"       f_Siga_Getrecurso(Nombre, "+idioma+") As Nombre, " +
			"       Diainicio, " +
			"       Mesinicio, " +
			"       Anioinicio, " +
			"       Diafin, " +
			"       Mesfin, " +
			"       Aniofin " +
			"  From Gen_Periodo " +
			" Where Idperiodo = "+periodo+" ";
		
		Vector result = this.selectGenerico(select);
		if (result == null)
			throw new ClsExceptions ("Error al buscar el periodo");
		if (result.size() != 1)
			throw new ClsExceptions ("Error al buscar el periodo");
		
		return (Hashtable) result.get(0);
	}

}
