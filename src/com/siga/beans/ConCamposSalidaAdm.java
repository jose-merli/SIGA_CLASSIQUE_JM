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
public class ConCamposSalidaAdm extends MasterBeanAdministrador {
	
	public ConCamposSalidaAdm(UsrBean usuario)
	{
	    super(ConCamposSalidaBean.T_NOMBRETABLA, usuario);
	}
	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConCamposSalidaBean.C_IDINSTITUCION,
				ConCamposSalidaBean.C_IDCONSULTA,
				ConCamposSalidaBean.C_ORDEN,
				ConCamposSalidaBean.C_CABECERA,
				ConCamposSalidaBean.C_IDCAMPO,
				ConCamposSalidaBean.C_FECHAMODIFICACION,
				ConCamposSalidaBean.C_USUMODIFICACION};
				
			return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConCamposSalidaBean.C_IDINSTITUCION,
		        		   ConCamposSalidaBean.C_ORDEN,
		        		   ConCamposSalidaBean.C_IDCONSULTA,
						   ConCamposSalidaBean.C_IDCAMPO};

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
		
		ConCamposSalidaBean bean = null;

		try
		{
			bean = new ConCamposSalidaBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConCamposSalidaBean.C_IDINSTITUCION));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ConCamposSalidaBean.C_IDCAMPO));
			bean.setCabecera(UtilidadesHash.getString(hash, ConCamposSalidaBean.C_CABECERA));			
			bean.setOrden(UtilidadesHash.getInteger(hash, ConCamposSalidaBean.C_ORDEN));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, ConCamposSalidaBean.C_IDCONSULTA));
			bean.setFechaMod(UtilidadesHash.getString(hash, ConCamposSalidaBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ConCamposSalidaBean.C_USUMODIFICACION));
		
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

			ConCamposSalidaBean b = (ConCamposSalidaBean) bean;

			UtilidadesHash.set(htData, ConCamposSalidaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConCamposSalidaBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ConCamposSalidaBean.C_CABECERA, b.getCabecera());
			UtilidadesHash.set(htData, ConCamposSalidaBean.C_ORDEN, b.getOrden());
			UtilidadesHash.set(htData, ConCamposSalidaBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, ConCamposSalidaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ConCamposSalidaBean.C_USUMODIFICACION, b.getUsuMod());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/** Funcion getNewIdCamposSalida (String idInstitucion, String idConsulta)
	 * Genera el id de un nuevo campo
	 * @param hash con la clave primaria sin el idDenunciante
	 * @return nuevo idDenunciante
	 * */
/*	public Integer getNewIdDenunciante(String idInstitucion) throws ClsExceptions 
	{		
		int nuevoIdCamposSalida = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ConCamposSalidaBean.C_IDCamposSalida+") AS ULTIMACamposSalida ";
	        
			sql += " FROM "+ConCamposSalidaBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ConCamposSalidaBean.C_IDINSTITUCION+" = "+idInstitucion;			
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMACamposSalida")).equals("")) {
					Integer ultimaCamposSalidaInt = Integer.valueOf((String)htRow.get("ULTIMACamposSalida"));
					int ultimaCamposSalida=ultimaCamposSalidaInt.intValue();
					ultimaCamposSalida++;
					nuevoIdCamposSalida = ultimaCamposSalida;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdCamposSalida);
	}
	*/
	
	/** Funcion getCamposSalida (String idInstitucion, String idConsulta)
	 * Devuelve los campos de salida mas las descripciones del campo y del tipocampo
	 * @param idInstitucion
	 * @param idConsulta
	 * @return Vector con resultados
	 * */
	public Vector getCamposSalida(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
				
		//Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT CS.*, C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
	        sql += ",C."+ConCampoConsultaBean.C_IDTABLA;
	        sql += " FROM "+ConCamposSalidaBean.T_NOMBRETABLA+" CS, "+ConCampoConsultaBean.T_NOMBRETABLA+" C,"+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T";
	        //sql +=","+ConTablaConsultaBean.T_NOMBRETABLA+" TC ";	        
			sql += " WHERE ";
			sql += ConCamposSalidaBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCamposSalidaBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CS."+ConCamposSalidaBean.C_IDCAMPO;
			//sql += "AND C."+ConCampoConsultaBean.C_IDTABLA+"=TC."+ConTablaConsultaBean.C_IDTABLA;
			sql += " ORDER BY "+ConCamposSalidaBean.C_ORDEN;
			
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
	
	/** Funcion getCamposSalidaParaQuery (String idInstitucion, String idConsulta)
	 * Devuelve un string con la parte de la query correspondiente a los campos de salida
	 * @param idInstitucion
	 * @param idConsulta
	 * @return String
	 * */
	public String getCamposSalidaParaQuery(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
		//Acceso a BBDD
		String resultado = "";
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION+", CS."+ConCamposSalidaBean.C_CABECERA;
	        sql += " FROM "+ConCamposSalidaBean.T_NOMBRETABLA+" CS, "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T";	        
			sql += " WHERE ";
			sql += ConCamposSalidaBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCamposSalidaBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CS."+ConCamposSalidaBean.C_IDCAMPO;
			sql += " ORDER BY CS."+ConCamposSalidaBean.C_ORDEN;
			
			
			if (rc.query(sql)) {
				resultado = "SELECT ";
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					resultado += fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION)+"."+fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA);
					resultado += " AS "+fila.getString(ConCamposSalidaBean.C_CABECERA)+",";									
				}
				resultado = resultado.substring(0,resultado.length()-1);
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return resultado;
	}
	
	/** Funcion getCamposSalidaParaQuery (Campo[] campos)
	 * Devuelve un string con la parte de la query correspondiente a los campos de salida
	 * @param campos
	 * @return String
	 * */
	public String getCamposSalidaParaQuery(Campo[] campos) throws ClsExceptions 
	{		
		//Acceso a BBDD
		String resultado = "";
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			for (int i=0;i<campos.length&&campos[i]!=null&& !campos[i].getTc().equals("null");i++){
				rc = new RowsContainer(); 						
				
		        String sql = "SELECT "+ConCampoConsultaBean.C_NOMBREREAL;
		        sql += " FROM "+ConCampoConsultaBean.T_NOMBRETABLA;	        
				sql += " WHERE ";
				sql += ConCampoConsultaBean.C_IDCAMPO+" = "+campos[i].getIdC();
			
				if (rc.query(sql)) {
					Row fila = (Row) rc.get(0);
					resultado += fila.getString(ConCampoConsultaBean.C_NOMBREREAL);
					resultado += " AS \""+campos[i].getCab()+"\",";			
				}
			}
			if (!resultado.equals("")){
				resultado = "SELECT "+resultado;
				resultado = resultado.substring(0,resultado.length()-1);
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return resultado;
	}
}
