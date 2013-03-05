/*
 * Created on Jan 25, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean de Partes de un expediente
 */
public class ExpPartesAdm extends MasterBeanAdministrador {
	
	public ExpPartesAdm(UsrBean usuario)
	{
	    super(ExpPartesBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {

		String[] campos = {ExpPartesBean.C_IDINSTITUCION,
				ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpPartesBean.C_IDTIPOEXPEDIENTE,
				ExpPartesBean.C_NUMEROEXPEDIENTE,
				ExpPartesBean.C_ANIOEXPEDIENTE,
				ExpPartesBean.C_IDPARTE,
				ExpPartesBean.C_IDPERSONA,
				ExpPartesBean.C_IDROL,
				ExpPartesBean.C_IDDIRECCION,
				ExpPartesBean.C_FECHAMODIFICACION,
				ExpPartesBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ExpPartesBean.C_IDINSTITUCION,
				ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpPartesBean.C_IDTIPOEXPEDIENTE,
				ExpPartesBean.C_NUMEROEXPEDIENTE,
				ExpPartesBean.C_ANIOEXPEDIENTE,
				ExpPartesBean.C_IDPARTE};
				
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
		
		ExpPartesBean bean = null;

		try
		{
			bean = new ExpPartesBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpPartesBean.C_IDINSTITUCION));
			bean.setIdInstitucion_TipoExpediente(UtilidadesHash.getInteger(hash, ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpPartesBean.C_IDTIPOEXPEDIENTE));
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpPartesBean.C_NUMEROEXPEDIENTE));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpPartesBean.C_ANIOEXPEDIENTE));
			bean.setIdParte(UtilidadesHash.getInteger(hash, ExpPartesBean.C_IDPARTE));
			bean.setIdPersona(UtilidadesHash.getInteger(hash, ExpPartesBean.C_IDPERSONA));
			bean.setIdDireccion(UtilidadesHash.getString(hash, ExpPartesBean.C_IDDIRECCION));
			bean.setIdRol(UtilidadesHash.getInteger(hash, ExpPartesBean.C_IDROL));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpPartesBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpPartesBean.C_USUMODIFICACION));
		
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

			ExpPartesBean b = (ExpPartesBean) bean;

			UtilidadesHash.set(htData, ExpPartesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_TipoExpediente());
			UtilidadesHash.set(htData, ExpPartesBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpPartesBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpPartesBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpPartesBean.C_IDPARTE, b.getIdParte());
			UtilidadesHash.set(htData, ExpPartesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, ExpPartesBean.C_IDDIRECCION, b.getIdDireccion());
			UtilidadesHash.set(htData, ExpPartesBean.C_IDROL, b.getIdRol());
			UtilidadesHash.set(htData, ExpPartesBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpPartesBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

	
	/** Funcion selectPartesRolPersonaExp (String where)
	 * Consulta sobre la tabla de expediente,partes,rolparte y persona, para búsqueda de datos de partes
	 * @param criteros para filtrar el select, campo where 
	 * @return vector con los datos de un expediente  
	 * */
	public Vector selectPartesRolPersonaExp(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
				
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String [] fields = this.getCamposBean();
	        
	        String sql = "SELECT ";	 
	        //todos los campos de la tabla exp_parte separados por coma
		    for(int i=0;i<fields.length; i++){
			  sql += "PR."+fields[i]+", ";
			}
		    		    
		    sql += "RP."+ExpRolesBean.C_NOMBRE+" AS ROL, ";
		    sql += "P."+CenPersonaBean.C_IDPERSONA+", ";
		    sql += "(P."+CenPersonaBean.C_NOMBRE+"||' '||P."+CenPersonaBean.C_APELLIDOS1+"||' '||P."+CenPersonaBean.C_APELLIDOS2+") AS NOMBREYAPELLIDO, ";
		    sql += "P."+CenPersonaBean.C_NIFCIF;
		    
			sql += " FROM ";
		    sql += ExpPartesBean.T_NOMBRETABLA+" PR, "+ExpExpedienteBean.T_NOMBRETABLA+" E, "+CenPersonaBean.T_NOMBRETABLA+" P, EXP_ROLPARTE RP";
		    		    		
			sql += " " + where;
			sql += " ORDER BY RP."+ExpRolesBean.C_NOMBRE+", P."+CenPersonaBean.C_NOMBRE+", P."+CenPersonaBean.C_APELLIDOS1+", P."+CenPersonaBean.C_APELLIDOS2;
						
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	/** Funcion getNewIdParte (Hashtable hash)
	 * Genera el id de una nueva Parte de un expediente
	 * @param hash con la clave primaria sin el idParte
	 * @return nuevo idPare
	 * */
	public Integer getNewIdParte(Hashtable hash) throws ClsExceptions 
	{		
		int nuevoIdParte = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ExpPartesBean.C_IDPARTE+") AS ULTIMOIDPARTE ";	        
		    
			sql += " FROM "+ExpPartesBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ExpPartesBean.C_IDINSTITUCION+" = "+hash.get(ExpPartesBean.C_IDINSTITUCION)+" AND ";
			sql += ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+hash.get(ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+" AND ";
			sql += ExpPartesBean.C_IDTIPOEXPEDIENTE+" = "+hash.get(ExpPartesBean.C_IDTIPOEXPEDIENTE)+" AND ";
			sql += ExpPartesBean.C_NUMEROEXPEDIENTE+" = "+hash.get(ExpPartesBean.C_NUMEROEXPEDIENTE)+" AND ";
			sql += ExpPartesBean.C_ANIOEXPEDIENTE+" = "+hash.get(ExpPartesBean.C_ANIOEXPEDIENTE);
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMOIDPARTE")).equals("")) {
					Integer ultimoParteInt = Integer.valueOf((String)htRow.get("ULTIMOIDPARTE"));
					int ultimoParte=ultimoParteInt.intValue();
					ultimoParte++;
					nuevoIdParte = ultimoParte;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdParte);
	}
	public List getPartes(ExpExpedienteBean beanExp) throws ClsExceptions {
		try {
				Hashtable htCodigos = new Hashtable();
				int keyContador = 0;
				StringBuffer where = new StringBuffer(" WHERE ");
			
				where.append(ExpPartesBean.C_IDINSTITUCION);
				keyContador++;
				htCodigos.put(new Integer(keyContador), beanExp.getIdInstitucion());
				where.append("=:");
				where.append(keyContador);
				where.append(" AND ");
				where.append(ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE);
				keyContador++;
				htCodigos.put(new Integer(keyContador), beanExp.getIdInstitucion_tipoExpediente());
				where.append("=:");
				where.append(keyContador);
				where.append(" AND ");
				where.append(ExpPartesBean.C_IDTIPOEXPEDIENTE);
				keyContador++;
				htCodigos.put(new Integer(keyContador), beanExp.getIdTipoExpediente());
				where.append("=:");
				where.append(keyContador);
				where.append(" AND ");
				where.append(ExpPartesBean.C_NUMEROEXPEDIENTE);
				keyContador++;
				htCodigos.put(new Integer(keyContador), beanExp.getNumeroExpediente());
				where.append("=:");
				where.append(keyContador);
				where.append(" AND ");
				where.append(ExpPartesBean.C_ANIOEXPEDIENTE);
				keyContador++;
				htCodigos.put(new Integer(keyContador), beanExp.getAnioExpediente());
				where.append("=:");
				where.append(keyContador);
			
				return selectBind(where.toString(), htCodigos);
				
			} catch (Exception e) {
				throw new ClsExceptions (e, "Error ExpDenuncianteAdm.getPartes.");
			}
	}
}
