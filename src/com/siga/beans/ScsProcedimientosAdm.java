//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class ScsProcedimientosAdm extends MasterBeanAdministrador {

	
	public ScsProcedimientosAdm(UsrBean usuario) {
		super(ScsProcedimientosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {ScsProcedimientosBean.C_FECHAMODIFICACION,		ScsProcedimientosBean.C_IDINSTITUCION,
							ScsProcedimientosBean.C_IDPROCEDIMIENTO,		ScsProcedimientosBean.C_NOMBRE,		
							ScsProcedimientosBean.C_PRECIO,					
							ScsProcedimientosBean.C_USUMODIFICACION, 		ScsProcedimientosBean.C_IDJURISDICCION,
							ScsProcedimientosBean.C_CODIGO, 				ScsProcedimientosBean.C_COMPLEMENTO,
							ScsProcedimientosBean.C_VIGENTE};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {ScsProcedimientosBean.C_IDINSTITUCION,			ScsProcedimientosBean.C_IDPROCEDIMIENTO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsProcedimientosBean bean = null;
		
		try {
			bean = new ScsProcedimientosBean();
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,ScsProcedimientosBean.C_IDINSTITUCION));
			bean.setIdJurisdiccion	(UtilidadesHash.getInteger(hash,ScsProcedimientosBean.C_IDJURISDICCION));
			bean.setIdProcedimiento	(UtilidadesHash.getInteger(hash,ScsProcedimientosBean.C_IDPROCEDIMIENTO));
			bean.setNombre			(UtilidadesHash.getString(hash,ScsProcedimientosBean.C_NOMBRE));
			bean.setPrecio			(UtilidadesHash.getFloat(hash,ScsProcedimientosBean.C_PRECIO));
			bean.setFechaMod		(UtilidadesHash.getString(hash,ScsProcedimientosBean.C_FECHAMODIFICACION));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,ScsProcedimientosBean.C_USUMODIFICACION));
			bean.setCodigo		    (UtilidadesHash.getString(hash,ScsProcedimientosBean.C_CODIGO));
			bean.setComplemento		(UtilidadesHash.getString(hash,ScsProcedimientosBean.C_COMPLEMENTO));
			bean.setVigente  		(UtilidadesHash.getString(hash,ScsProcedimientosBean.C_VIGENTE));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsProcedimientosBean b = (ScsProcedimientosBean) bean;
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_IDJURISDICCION, b.getIdJurisdiccion().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_IDPROCEDIMIENTO, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_NOMBRE, b.getNombre().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_PRECIO, b.getPrecio().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_USUMODIFICACION, b.getUsuMod().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_CODIGO, b.getCodigo().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_COMPLEMENTO, b.getComplemento().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_VIGENTE, b.getVigente().toString());
		}
		catch (Exception e) {
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
			if (rc.queryNLS(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsProcedimientosAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/**
	 * Calcular el identificador del movimiento que se va a insertar. Necesita que el hashtable que se le pasa
	 * tenga una key IdInstitucion con el cod de institucion del usuario logado  
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	
	public Hashtable prepararInsert (Hashtable entrada, Integer idInstitucion)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql = " SELECT (MAX(TO_NUMBER(" + ScsProcedimientosBean.C_IDPROCEDIMIENTO + ")) + 1) AS IDPROCEDIMIENTO " +
					 " FROM " + nombreTabla + 
					 " WHERE " + ScsProcedimientosBean.C_IDINSTITUCION + " = " + idInstitucion;	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDPROCEDIMIENTO").equals("")) {
					entrada.put(ScsProcedimientosBean.C_IDPROCEDIMIENTO,"1");
				}
				else entrada.put(ScsProcedimientosBean.C_IDPROCEDIMIENTO,(String)prueba.get("IDPROCEDIMIENTO"));								
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error en clase ScsProcedimientosAdm.prepararInsert()" + e.getErrorType());		
		}
		return entrada;
	}

	
	public boolean comprobarCodigoExt(String institucion, String idProcedimiento, String codigoExt) throws ClsExceptions 
	{
		boolean salida=false;
		
		try {
			String sql=
				"select "+
				" j."+ScsProcedimientosBean.C_IDPROCEDIMIENTO+" "+
				" from "+
				ScsProcedimientosBean.T_NOMBRETABLA+" j "+
				" where j."+ScsProcedimientosBean.C_CODIGO+"='"+codigoExt + "'";
				
				sql +=" and j."+ScsProcedimientosBean.C_IDINSTITUCION+"="+institucion;
			
				if (idProcedimiento!=null && !idProcedimiento.trim().equals("")) {
					sql+="  and j."+ScsProcedimientosBean.C_IDPROCEDIMIENTO+"<>"+idProcedimiento+"";
				}

			
			//Consulta:
			RowsContainer rc = this.find(sql);		
			if(rc!=null && rc.size()>0){
				salida=true;
			}
			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return salida;
	}


}


