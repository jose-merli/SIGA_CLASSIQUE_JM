/*
 * Created on Apr 11, 2005
 * @author juan.grau
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
import com.siga.consultas.Campo;

/**
 * Administrador del Bean de Consultas 
 */
public class ConCamposAgregacionAdm extends MasterBeanAdministrador {
	
	public ConCamposAgregacionAdm(UsrBean usuario)
	{
	    super(ConCamposAgregacionBean.T_NOMBRETABLA, usuario);
	}
	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConCamposAgregacionBean.C_IDINSTITUCION,
				ConCamposAgregacionBean.C_IDCONSULTA,
				ConCamposAgregacionBean.C_ORDEN,
				ConCamposAgregacionBean.C_IDCAMPO,
				ConCamposAgregacionBean.C_FECHAMODIFICACION,
				ConCamposAgregacionBean.C_USUMODIFICACION};
				
			return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConCamposAgregacionBean.C_IDINSTITUCION,
						   ConCamposAgregacionBean.C_IDCONSULTA,
						   ConCamposAgregacionBean.C_IDCAMPO};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ConCamposAgregacionBean bean = null;

		try
		{
			bean = new ConCamposAgregacionBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConCamposAgregacionBean.C_IDINSTITUCION));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ConCamposAgregacionBean.C_IDCAMPO));
			bean.setOrden(UtilidadesHash.getInteger(hash, ConCamposAgregacionBean.C_ORDEN));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, ConCamposAgregacionBean.C_IDCONSULTA));
			bean.setFechaMod(UtilidadesHash.getString(hash, ConCamposAgregacionBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ConCamposAgregacionBean.C_USUMODIFICACION));
		
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

			ConCamposAgregacionBean b = (ConCamposAgregacionBean) bean;

			UtilidadesHash.set(htData, ConCamposAgregacionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConCamposAgregacionBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ConCamposAgregacionBean.C_ORDEN, b.getOrden());
			UtilidadesHash.set(htData, ConCamposAgregacionBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, ConCamposAgregacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ConCamposAgregacionBean.C_USUMODIFICACION, b.getUsuMod());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/** Funcion getNewIdCamposAgregacion (String idInstitucion, String idConsulta)
	 * Genera el id de un nuevo campo
	 * @param hash con la clave primaria sin el idDenunciante
	 * @return nuevo idDenunciante
	 * */
/*	public Integer getNewIdDenunciante(String idInstitucion) throws ClsExceptions 
	{		
		int nuevoIdCamposAgregacion = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ConCamposAgregacionBean.C_IDCamposAgregacion+") AS ULTIMACamposAgregacion ";
	        
			sql += " FROM "+ConCamposAgregacionBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ConCamposAgregacionBean.C_IDINSTITUCION+" = "+idInstitucion;			
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMACamposAgregacion")).equals("")) {
					Integer ultimaCamposAgregacionInt = Integer.valueOf((String)htRow.get("ULTIMACamposAgregacion"));
					int ultimaCamposAgregacion=ultimaCamposAgregacionInt.intValue();
					ultimaCamposAgregacion++;
					nuevoIdCamposAgregacion = ultimaCamposAgregacion;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdCamposAgregacion);
	}
	*/
	
	/** Funcion getCamposAgregacion (String idInstitucion, String idConsulta)
	 * Devuelve los campos de agregacion mas las descripciones del campo y del tipocampo
	 * @param idInstitucion
	 * @param idConsulta
	 * @return Vector con resultados
	 * */
	public Vector getCamposAgregacion(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
				
		//Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT CA.*, C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
	        sql += " FROM "+ConCamposAgregacionBean.T_NOMBRETABLA+" CA, "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T";	        
			sql += " WHERE ";
			sql += ConCamposAgregacionBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCamposAgregacionBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CA."+ConCamposAgregacionBean.C_IDCAMPO;
			sql += " ORDER BY "+ConCamposAgregacionBean.C_ORDEN;
			
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
	
	/** Funcion getGroupBy (String idInstitucion, String idConsulta)
	 * Devuelve un string con la parte de la query correspondiente al group by
	 * @param idInstitucion
	 * @param idConsulta
	 * @return String
	 * */
	public String getGroupBy(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
		//Acceso a BBDD
		String resultado = "";
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
	        sql += " FROM "+ConCamposAgregacionBean.T_NOMBRETABLA+" CA, "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T";	        
			sql += " WHERE ";
			sql += ConCamposAgregacionBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCamposAgregacionBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CA."+ConCamposAgregacionBean.C_IDCAMPO;
			sql += " ORDER BY "+ConCamposAgregacionBean.C_ORDEN;
			
			
			
			if (rc.query(sql)) {
				resultado = "GROUP BY ";
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					resultado += fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION)+"."+fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA)+",";									
				}
				resultado = resultado.substring(0,resultado.length()-1);
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return resultado;
	}
	
	/** Funcion getGroupBy (Campo[] campos)
	 * Devuelve un string con la parte de la query correspondiente a los campos de agregación
	 * @param campos
	 * @return String
	 * */
	public String getGroupBy (Campo[] campos) throws ClsExceptions 
	{		
		//Acceso a BBDD
		String resultado = "";
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			for (int i=0;i<campos.length&&campos[i]!=null&& !campos[i].getTc().equals("null");i++){
				rc = new RowsContainer(); 						
				
		        // Modificacion MAV (Sustituir nombres consultas por reales Ej. residente -> situacionResidente) 7/7/05
				// String sql = "SELECT C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
				String sql = "SELECT C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION+", C."+ConCampoConsultaBean.C_NOMBREREAL;
				// Fin modificacion
		        sql += " FROM "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T";	        
				sql += " WHERE ";
				sql += "C."+ConCampoConsultaBean.C_IDCAMPO+" = "+campos[i].getIdC()+" AND ";
				sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO;
			
			
				if (rc.query(sql)) {
					Row fila = (Row) rc.get(0);
			        // Modificacion MAV (Sustituir nombres consultas por reales Ej. residente -> situacionResidente) 7/7/05
					// resultado += fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION)+"."+fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA)+",";
					resultado += fila.getString(ConCampoConsultaBean.C_NOMBREREAL)+",";
					// Fin modificacion
				}
			}
			if (!resultado.equals("")){
				resultado = "GROUP BY "+resultado;
				resultado = resultado.substring(0,resultado.length()-1);
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return resultado;
	}

}
