package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

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
							CenBancosBean.C_USUMODIFICACION,
							CenBancosBean.C_BIC, 
							CenBancosBean.C_IDPAIS};

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

		try	{
			
			bean = new CenBancosBean();
			bean.setCodigo(UtilidadesHash.getString(hash, CenBancosBean.C_CODIGO));
			bean.setNombre(UtilidadesHash.getString(hash, CenBancosBean.C_NOMBRE));
			bean.setBic(UtilidadesHash.getString(hash, CenBancosBean.C_BIC));
			bean.setIdPais(UtilidadesHash.getString(hash, CenBancosBean.C_IDPAIS));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenBancosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenBancosBean.C_USUMODIFICACION));
		
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try {
			
			htData = new Hashtable();
			CenBancosBean b = (CenBancosBean) bean;
			UtilidadesHash.set(htData, CenBancosBean.C_CODIGO, b.getCodigo());
			UtilidadesHash.set(htData, CenBancosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenBancosBean.C_BIC, b.getBic());
			UtilidadesHash.set(htData, CenBancosBean.C_IDPAIS, b.getIdPais());
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
 	
 	public CenBancosBean getBanco(String idBanco) throws ClsExceptions{ 		
		RowsContainer rc = null;
		CenBancosBean bancoBean = new CenBancosBean();
		
		try{
    		
		    String sql = " SELECT "+CenBancosBean.T_NOMBRETABLA+"."+CenBancosBean.C_NOMBRE +
			 " FROM " + CenBancosBean.T_NOMBRETABLA +			 
			 " WHERE "+CenBancosBean.T_NOMBRETABLA+"."+CenBancosBean.C_CODIGO+"='"+idBanco + "'";

            rc = this.find(sql);
            
 			if (rc!=null&&rc.size()>0) { 	
 				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable)fila.getRow(); 
				if (registro != null) { 					
					bancoBean.setNombre(UtilidadesHash.getString(registro,bancoBean.C_NOMBRE));
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getBanco");
		}
		return bancoBean;
	}     	
 	
 	public CenBancosBean getBancoIBAN(String codBanco) throws ClsExceptions{ 		
		RowsContainer rc = null;
		CenBancosBean bancoBean = null;
		
		try{
    		
		    String sql = " SELECT * "+
			 " FROM " + CenBancosBean.T_NOMBRETABLA +			 
			 " WHERE "+CenBancosBean.T_NOMBRETABLA+"."+CenBancosBean.C_CODIGO+"='"+codBanco + "'";

            rc = this.find(sql);
            
 			if (rc!=null&&rc.size()>0) { 	
 				bancoBean = new CenBancosBean();
 				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable)fila.getRow(); 
				if (registro != null) { 					
					bancoBean.setNombre(UtilidadesHash.getString(registro,bancoBean.C_NOMBRE));
					bancoBean.setBic(UtilidadesHash.getString(registro,bancoBean.C_BIC));
					bancoBean.setIdPais(UtilidadesHash.getString(registro,bancoBean.C_IDPAIS));
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getBancoIBAN");
		}
		return bancoBean;
	}
 	
 	public CenBancosBean existeBancoExtranjero(String bic) throws ClsExceptions{ 		
		RowsContainer rc = null;
		CenBancosBean bancoBean = null;
		
		try{
    		
		    String sql = " SELECT * "+
			 " FROM " + CenBancosBean.T_NOMBRETABLA +			 
			 " WHERE "+CenBancosBean.T_NOMBRETABLA+"."+CenBancosBean.C_BIC+"='"+bic + "'";

            rc = this.find(sql);            
 			if (rc!=null&&rc.size()>0) {
 				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable)fila.getRow(); 
				if (registro != null) { 					
					bancoBean = (CenBancosBean) this.hashTableToBean(registro);
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getBancoBIC");
		}
		return bancoBean;
	} 	
 	
 	 public CenBancosBean insertarBancoExtranjero(String idPais,String bic) throws SIGAException{
		CenBancosBean bancoBean = new CenBancosBean();
		CenPaisAdm paisAdm = new CenPaisAdm(this.usrbean);
		try{
			bancoBean.setCodigo(getNuevoId());
			bancoBean.setNombre("BANCO EXTRANJERO");
			bancoBean.setBic(bic);
			bancoBean.setIdPais(idPais);

			if (!this.insert(bancoBean)) {
				throw new SIGAException(this.getError());
			}
			
		}
		catch(Exception e) {
			throw new SIGAException ("Error al crear un nuevo banco extranjero");
		}
		return bancoBean;
	}
 	 
	/**
	 * Obtiene un nuevo ID de un banco extranjero (5 digitos)
	 * @author Carlos Ruano
	 * @version 1	 
	 * @param Bean datos de la cuenta.
	 * @return nuevo ID.
	 */
	public String getNuevoId () throws SIGAException, ClsExceptions {
		RowsContainer rc = new RowsContainer();		
		try {		
			String sql = " SELECT LPAD((MAX(" + CenBancosBean.C_CODIGO + ") + 1),5,'0')  AS " + CenBancosBean.C_CODIGO + 
			  			 " FROM " + CenBancosBean.T_NOMBRETABLA +
						 " WHERE LENGTH("+CenBancosBean.C_CODIGO+") = 5";
			
			// RGG cambio visibilidad
			rc = this.findForUpdate(sql);
			if (rc!=null && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable hash = fila.getRow();
				String codigo = UtilidadesHash.getString(hash, CenBancosBean.C_CODIGO);
				if (codigo == null || codigo == "") {
					return "00001";
				} else{
					return codigo;
				}
			}
		
		} catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		
		return null;
	}	 	 
}