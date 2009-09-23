/*
 * Created on Apr 13, 2005
 * @author emilio.grau
 *
 * 
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean ConCampoConsultaBean
 */
public class ConCampoConsultaAdm extends MasterBeanAdministrador {
	
//	Constantes	
	static public final String CONS_FORMATO="%%FORMATO%%";

	public ConCampoConsultaAdm(UsrBean usuario)
	{
	    super(ConCampoConsultaBean.T_NOMBRETABLA, usuario);
	}
	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConCampoConsultaBean.C_IDCAMPO,
				ConCampoConsultaBean.C_DESCRIPCION,
				ConCampoConsultaBean.C_NOMBREREAL,
				ConCampoConsultaBean.C_NOMBREENCONSULTA,
				ConCampoConsultaBean.C_OBSERVACIONES,
				ConCampoConsultaBean.C_TIPOCAMPO,
				ConCampoConsultaBean.C_SELECTAYUDA,
				ConCampoConsultaBean.C_FORMATO,
				ConCampoConsultaBean.C_IDTIPOCAMPO,
				ConCampoConsultaBean.C_IDTABLA,
				ConCampoConsultaBean.C_VISIBILIDAD,
				ConCampoConsultaBean.C_LONGITUD,
				ConCampoConsultaBean.C_ESCALA,
				ConCampoConsultaBean.C_FECHAMODIFICACION,
				ConCampoConsultaBean.C_USUMODIFICACION};
				
			return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConCampoConsultaBean.C_IDCAMPO};

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
		
		ConCampoConsultaBean bean = null;

		try
		{
			bean = new ConCampoConsultaBean();
						
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ConCampoConsultaBean.C_IDCAMPO));
			bean.setDescripcion(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_DESCRIPCION));
			bean.setNombreReal(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_NOMBREREAL));
			bean.setNombreEnConsulta(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_NOMBREENCONSULTA));
			bean.setObservaciones(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_OBSERVACIONES));
			bean.setTipoCampo(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_TIPOCAMPO));
			bean.setSelectAyuda(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_SELECTAYUDA));
			bean.setFormato(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_FORMATO));
			bean.setIdTipoCampo(UtilidadesHash.getInteger(hash, ConCampoConsultaBean.C_IDTIPOCAMPO));
			bean.setIdTabla(UtilidadesHash.getInteger(hash, ConCampoConsultaBean.C_IDTABLA));
			bean.setVisibilidad(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_VISIBILIDAD));
			bean.setLongitud(UtilidadesHash.getInteger(hash, ConCampoConsultaBean.C_LONGITUD));
			bean.setEscala(UtilidadesHash.getInteger(hash, ConCampoConsultaBean.C_ESCALA));
			bean.setFechaMod(UtilidadesHash.getString(hash, ConCampoConsultaBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ConCampoConsultaBean.C_USUMODIFICACION));
		
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

			ConCampoConsultaBean b = (ConCampoConsultaBean) bean;

			UtilidadesHash.set(htData, ConCampoConsultaBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_NOMBREREAL, b.getNombreReal());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_NOMBREENCONSULTA, b.getNombreEnConsulta());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_OBSERVACIONES, b.getObservaciones());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_TIPOCAMPO, b.getTipoCampo());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_SELECTAYUDA, b.getSelectAyuda());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_FORMATO, b.getFormato());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_IDTIPOCAMPO, b.getIdTipoCampo());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_IDTABLA, b.getIdTabla());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_VISIBILIDAD, b.getVisibilidad());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_LONGITUD, b.getLongitud());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_ESCALA, b.getEscala());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ConCampoConsultaBean.C_USUMODIFICACION, b.getUsuMod());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsFactEjgAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	
	/** Funcion getTablaConInstitucion (Vector tablas)
	 * Para las tablas que existen en el vector, busca si alguna contiene un campo idInstitucion,<br>
	 * y si es así, devuelve devuelve el nombre del campo para usarlo en una consulta.
	 * @param tablas Vector con los identificadores de las tablas.
	 * @return String 'nombreTabla.nombreCampo' si existe, o blanco si no existe.  
	 * @exception ClsExceptions 
	 * */
	public String getTablaConInstitucion (Vector tablas) throws ClsExceptions 
	{
		
		String resultado = "";
		String sql = "";
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			for (int i=0;i<tablas.size();i++){
				rc = new RowsContainer(); 
				
		        sql = "SELECT ";
		        	        
		        sql += ConCampoConsultaBean.C_NOMBREREAL;	
			    
				sql += " FROM ";
			    sql += ConCampoConsultaBean.T_NOMBRETABLA;
			    		    		
				sql += " WHERE ";
				sql += ConCampoConsultaBean.C_IDTABLA+"="+tablas.get(i)+" AND ";
				sql += ConCampoConsultaBean.C_NOMBREENCONSULTA+" = 'IDINSTITUCION'";
				
				ClsLogging.writeFileLog("ConCampoConsultaAdm, sql: "+sql,3);
				
				if (rc.query(sql)) {
					Row fila = (Row) rc.get(0);										
					resultado = fila.getString(ConCampoConsultaBean.C_NOMBREREAL);
					break;
					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return resultado;
	}

	
}
