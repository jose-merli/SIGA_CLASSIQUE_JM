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
public class ConCamposOrdenacionAdm extends MasterBeanAdministrador {
	
	public ConCamposOrdenacionAdm(UsrBean usuario)
	{
	    super(ConCamposOrdenacionBean.T_NOMBRETABLA, usuario);
	}
	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConCamposOrdenacionBean.C_IDINSTITUCION,
				ConCamposOrdenacionBean.C_IDCONSULTA,
				ConCamposOrdenacionBean.C_ORDEN,
				ConCamposOrdenacionBean.C_IDCAMPO,
				ConCamposOrdenacionBean.C_FECHAMODIFICACION,
				ConCamposOrdenacionBean.C_USUMODIFICACION};
				
			return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConCamposOrdenacionBean.C_IDINSTITUCION,
						   ConCamposOrdenacionBean.C_IDCONSULTA,
						   ConCamposOrdenacionBean.C_IDCAMPO,
						   ConCamposOrdenacionBean.C_ORDEN};

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
		
		ConCamposOrdenacionBean bean = null;

		try
		{
			bean = new ConCamposOrdenacionBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConCamposOrdenacionBean.C_IDINSTITUCION));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ConCamposOrdenacionBean.C_IDCAMPO));
			bean.setOrden(UtilidadesHash.getInteger(hash, ConCamposOrdenacionBean.C_ORDEN));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, ConCamposOrdenacionBean.C_IDCONSULTA));
			bean.setFechaMod(UtilidadesHash.getString(hash, ConCamposOrdenacionBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ConCamposOrdenacionBean.C_USUMODIFICACION));
		
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

			ConCamposOrdenacionBean b = (ConCamposOrdenacionBean) bean;

			UtilidadesHash.set(htData, ConCamposOrdenacionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConCamposOrdenacionBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ConCamposOrdenacionBean.C_ORDEN, b.getOrden());
			UtilidadesHash.set(htData, ConCamposOrdenacionBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, ConCamposOrdenacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ConCamposOrdenacionBean.C_USUMODIFICACION, b.getUsuMod());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/** Funcion getNewIdCamposOrdenacion (String idInstitucion, String idConsulta)
	 * Genera el id de un nuevo campo
	 * @param hash con la clave primaria sin el idDenunciante
	 * @return nuevo idDenunciante
	 * */
/*	public Integer getNewIdDenunciante(String idInstitucion) throws ClsExceptions 
	{		
		int nuevoIdCamposOrdenacion = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ConCamposOrdenacionBean.C_IDCamposOrdenacion+") AS ULTIMACamposOrdenacion ";
	        
			sql += " FROM "+ConCamposOrdenacionBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ConCamposOrdenacionBean.C_IDINSTITUCION+" = "+idInstitucion;			
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMACamposOrdenacion")).equals("")) {
					Integer ultimaCamposOrdenacionInt = Integer.valueOf((String)htRow.get("ULTIMACamposOrdenacion"));
					int ultimaCamposOrdenacion=ultimaCamposOrdenacionInt.intValue();
					ultimaCamposOrdenacion++;
					nuevoIdCamposOrdenacion = ultimaCamposOrdenacion;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdCamposOrdenacion);
	}
	*/
	
	/** Funcion getCamposOrdenacion (String idInstitucion, String idConsulta)
	 * Devuelve los campos de ordenacion mas las descripciones del campo y del tipocampo
	 * @param idInstitucion
	 * @param idConsulta
	 * @return Vector con resultados
	 * */
	public Vector getCamposOrdenacion(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
				
		//Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT CO.*, C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
	        sql += " FROM "+ConCamposOrdenacionBean.T_NOMBRETABLA+" CO, "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T";	        
			sql += " WHERE ";
			sql += ConCamposOrdenacionBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCamposOrdenacionBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CO."+ConCamposOrdenacionBean.C_IDCAMPO;
			sql += " ORDER BY "+ConCamposOrdenacionBean.C_ORDEN;
			
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
	
	/** Funcion getOrderBy (String idInstitucion, String idConsulta)
	 * Devuelve un string con la parte de la query correspondiente al order by
	 * @param idInstitucion
	 * @param idConsulta
	 * @return String
	 * */
	public String getOrderBy(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
		//Acceso a BBDD
		String resultado = "";
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
	        sql += " FROM "+ConCamposOrdenacionBean.T_NOMBRETABLA+" CO, "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T";	        
			sql += " WHERE ";
			sql += ConCamposOrdenacionBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCamposOrdenacionBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CO."+ConCamposOrdenacionBean.C_IDCAMPO;
			sql += " ORDER BY "+ConCamposOrdenacionBean.C_ORDEN;
			
			
			if (rc.query(sql)) {
				resultado = "ORDER BY ";
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
	
	/** Funcion getOrderBy (Campo[] campos)
	 * Devuelve un string con la parte de la query correspondiente a los campos de ordenación
	 * @param campos
	 * @return String
	 * */
	public String getOrderBy (Campo[] campos) throws ClsExceptions 
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
				resultado = "ORDER BY "+resultado;
				resultado = resultado.substring(0,resultado.length()-1);
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return resultado;
	}
}
