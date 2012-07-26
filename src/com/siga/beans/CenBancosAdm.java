package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;

public class CenBancosAdm extends MasterBeanAdministrador
{
	public CenBancosAdm(UsrBean usuario)
	{
	    super(CenBancosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CenBancosBean.C_CODIGO,
							CenBancosBean.C_FECHAMODIFICACION,
							CenBancosBean.C_NOMBRE, 
							CenBancosBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CenBancosBean.C_CODIGO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		CenBancosBean bean = null;

		try
		{
			bean = new CenBancosBean();
			
			bean.setCodigo(UtilidadesHash.getString(hash, CenBancosBean.C_CODIGO));
			bean.setNombre(UtilidadesHash.getString(hash, CenBancosBean.C_NOMBRE));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenBancosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenBancosBean.C_USUMODIFICACION));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			CenBancosBean b = (CenBancosBean) bean;

			UtilidadesHash.set(htData, CenBancosBean.C_CODIGO, b.getCodigo());
			UtilidadesHash.set(htData, CenBancosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenBancosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenBancosBean.C_USUMODIFICACION, b.getUsuMod());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}


    protected String[] getOrdenCampos()
    {
        return null;
    }
    
 	public List<CenBancosBean> getBancos(UsrBean usuario) throws ClsExceptions{
 		List<CenBancosBean> listaBancos = null; 		
		RowsContainer rc = null;
		
		try{
    		
		    String sql = " SELECT "+CenBancosBean.T_NOMBRETABLA+"."+CenBancosBean.C_CODIGO+","+CenBancosBean.T_NOMBRETABLA+"."+CenBancosBean.C_NOMBRE +
			 " FROM " + CenBancosBean.T_NOMBRETABLA +
			 " order by 2 ASC ";

            rc = this.find(sql);
            
 			if (rc!=null) { 				 				
 				listaBancos = new ArrayList<CenBancosBean>();
 				CenBancosBean bancoBean = new CenBancosBean();
				bancoBean.setNombre(UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar"));
				bancoBean.setCodigo("-1");
				listaBancos.add(bancoBean);
 				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) { 
						bancoBean = new CenBancosBean();
						bancoBean.setNombre(UtilidadesHash.getString(registro,bancoBean.C_NOMBRE));
						bancoBean.setCodigo(UtilidadesHash.getString(registro,bancoBean.C_CODIGO));
	            		listaBancos.add(bancoBean);
					}
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getBancos");
		}
		return listaBancos;
	}    
}