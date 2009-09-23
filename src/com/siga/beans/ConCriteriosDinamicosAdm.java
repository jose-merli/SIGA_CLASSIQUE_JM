/*
 * Created on Apr 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del bean de la tabla de criterios dinámicos
 */
public class ConCriteriosDinamicosAdm extends MasterBeanAdministrador {
	
	//	Constantes	
	static public final String CONS_DINAMICOS="%%DINAMICOS%%";

	public ConCriteriosDinamicosAdm(UsrBean usuario)
	{
	    super(ConCriteriosDinamicosBean.T_NOMBRETABLA, usuario);
	}
	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConCriteriosDinamicosBean.C_IDINSTITUCION,
				ConCriteriosDinamicosBean.C_IDCONSULTA,
				ConCriteriosDinamicosBean.C_ORDEN,
				ConCriteriosDinamicosBean.C_IDCAMPO,
				ConCriteriosDinamicosBean.C_FECHAMODIFICACION,
				ConCriteriosDinamicosBean.C_USUMODIFICACION};
				
			return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConCriteriosDinamicosBean.C_IDINSTITUCION,
						   ConCriteriosDinamicosBean.C_IDCONSULTA,
						   ConCriteriosDinamicosBean.C_ORDEN,
						   ConCriteriosDinamicosBean.C_IDCAMPO};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		
//		 TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ConCriteriosDinamicosBean bean = null;

		try
		{
			bean = new ConCriteriosDinamicosBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConCriteriosDinamicosBean.C_IDINSTITUCION));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ConCriteriosDinamicosBean.C_IDCAMPO));
			bean.setOrden(UtilidadesHash.getInteger(hash, ConCriteriosDinamicosBean.C_ORDEN));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, ConCriteriosDinamicosBean.C_IDCONSULTA));
			bean.setFechaMod(UtilidadesHash.getString(hash, ConCriteriosDinamicosBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ConCriteriosDinamicosBean.C_USUMODIFICACION));
		
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;

	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ConCriteriosDinamicosBean b = (ConCriteriosDinamicosBean) bean;

			UtilidadesHash.set(htData, ConCriteriosDinamicosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConCriteriosDinamicosBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ConCriteriosDinamicosBean.C_ORDEN, b.getOrden());
			UtilidadesHash.set(htData, ConCriteriosDinamicosBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, ConCriteriosDinamicosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ConCriteriosDinamicosBean.C_USUMODIFICACION, b.getUsuMod());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/** Funcion getCriteriosDinamicos (String idInstitucion, String idConsulta)
	 * Devuelve los criterios dinamicos mas las descripciones del campo y del tipocampo
	 * @param idInstitucion
	 * @param idConsulta
	 * @return Vector con resultados
	 * */
	public Vector getCriteriosDinamicos(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
				
		//Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT CD.*, C."+ConCampoConsultaBean.C_TIPOCAMPO+", C."+ConCampoConsultaBean.C_NOMBREENCONSULTA;
	        sql += ", C."+ConCampoConsultaBean.C_SELECTAYUDA+", C."+ConCampoConsultaBean.C_LONGITUD+", C."+ConCampoConsultaBean.C_ESCALA;
	        sql += ", T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
	        sql += " FROM "+ConCriteriosDinamicosBean.T_NOMBRETABLA+" CD, "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T";	        
			sql += " WHERE ";
			sql += ConCriteriosDinamicosBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCriteriosDinamicosBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CD."+ConCriteriosDinamicosBean.C_IDCAMPO;
			sql += " ORDER BY "+ConCriteriosDinamicosBean.C_ORDEN;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return datos;
	}

	/** Funcion getNombreCampo (String idCampo)
	 * Devuelve los criterios dinamicos mas las descripciones del campo y del tipocampo
	 * @param idCampo
	 * @return String
	 * */
	public String getNombreCampo(String idCampo) throws ClsExceptions 
	{		
				
		//Acceso a BBDD
		RowsContainer rc = null;
		String retorno=null;
		try { 
			rc = new RowsContainer(); 						
			
			String sql = "SELECT "+ConCampoConsultaBean.C_NOMBREREAL;
	        sql += " FROM "+ConCampoConsultaBean.T_NOMBRETABLA;	        
			sql += " WHERE ";
			sql += ConCampoConsultaBean.C_IDCAMPO+" = "+idCampo;
			
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);										
				retorno = fila.getString(ConCampoConsultaBean.C_NOMBREREAL);	
			}
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return retorno;
	}	

}
