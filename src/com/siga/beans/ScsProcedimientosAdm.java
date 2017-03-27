//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.exceptions.BusinessException;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


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
							ScsProcedimientosBean.C_FECHADESDEVIGOR,		ScsProcedimientosBean.C_FECHAHASTAVIGOR,
							ScsProcedimientosBean.C_PERMITIRANIADIRLETRADO,ScsProcedimientosBean.C_CODIGOEXT};
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
			bean.setCodigoExt(    UtilidadesHash.getString(hash,ScsProcedimientosBean.C_CODIGOEXT));
			bean.setComplemento		(UtilidadesHash.getString(hash,ScsProcedimientosBean.C_COMPLEMENTO));
			bean.setFechaDesdeVigor	(UtilidadesHash.getString(hash,ScsProcedimientosBean.C_FECHADESDEVIGOR));
			bean.setFechaHastaVigor	(UtilidadesHash.getString(hash,ScsProcedimientosBean.C_FECHAHASTAVIGOR));
			bean.setPermitirAniadirLetrado(UtilidadesHash.getString(hash,ScsProcedimientosBean.C_PERMITIRANIADIRLETRADO));
			
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
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_COMPLEMENTO, b.getComplemento().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_FECHADESDEVIGOR, b.getFechaDesdeVigor().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_FECHAHASTAVIGOR, b.getFechaHastaVigor().toString());
			UtilidadesHash.set(htData, ScsProcedimientosBean.C_PERMITIRANIADIRLETRADO, b.getPermitirAniadirLetrado().toString());
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
	
	/**
	 * getPrpcedimientosModulos
	 * @param idInstitucion
	 * @param idProcedimiento
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector<Hashtable<String, Object>> getPretensionesDeProcedimiento(Integer idInstitucion,
			String idProcedimiento) throws ClsExceptions, SIGAException {
		Vector<Hashtable<String, Object>> returnVector = new Vector<Hashtable<String,Object>>();
		RowsContainer rc = null;
		try {
			StringBuilder query = new StringBuilder();

			query.append("SELECT P.IDPRETENSION,F_SIGA_GETRECURSO(P.DESCRIPCION, 1) DESCRIPCION, P.CODIGOEXT, P.IDINSTITUCION ");
			query.append("FROM SCS_PRETENSIONESPROCED PP, SCS_PRETENSION P ");
			query.append("WHERE PP.IDINSTITUCION = P.IDINSTITUCION ");
			query.append("AND PP.IDPRETENSION = P.IDPRETENSION ");
			query.append("AND PP.IDPROCEDIMIENTO = ");
			query.append(idProcedimiento);
			query.append("AND PP.IDINSTITUCION =  ");
			query.append(idInstitucion);
			query.append(" ORDER BY DESCRIPCION ");

			rc = this.find(query.toString());
			if (rc != null) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> registro = (Hashtable<String, Object>) fila.getRow();
					if (registro != null) {
						returnVector.add(registro);
					}
				}
			}
		}

		catch (Exception e) {
			throw new ClsExceptions(e, "Error en getPretensionesDeProcedimiento");
		}
		return returnVector;
	}
	
	public void borraPretensionesDeProcedimiento(String pretensionesDeProcedimiento)throws BusinessException  {
		String[] registrosBorrarStrings = pretensionesDeProcedimiento.split("#");

		UserTransaction tx = usrbean.getTransaction();
		try {
			tx.begin();
			for (int i = 0; i < registrosBorrarStrings.length; i++) {
				String lineaBorrar = registrosBorrarStrings[i];
				String[] identificadores = lineaBorrar.split(",");
				String idInstitucionProcedimiento = identificadores[0];
				String idProcedimiento = identificadores[1];
				String idPretension = identificadores[2];
				borraPretensionesDeProcedimiento( new Integer(idInstitucionProcedimiento),idProcedimiento,new Integer(idPretension));
			}
			tx.commit();
		
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch  (Exception e1) {

			}
			throw new BusinessException("Error al borrar los procedimientos del modulo");
		}
		
		
	}
	public void insertaPretensionesDeProcedimiento(String pretensionesDeProcedimiento)throws BusinessException  {
		String[] registrosInsertarStrings = pretensionesDeProcedimiento.split("#");

		UserTransaction tx = usrbean.getTransaction();
		try {
			tx.begin();
			for (int i = 0; i < registrosInsertarStrings.length; i++) {
				String lineaInsertar = registrosInsertarStrings[i];
				String[] identificadores = lineaInsertar.split(",");
				String idInstitucionProcedimiento = identificadores[0];
				String idProcedimiento = identificadores[1];
				String idPretension = identificadores[2];
				insertaPretensionesDeProcedimiento( new Integer(idInstitucionProcedimiento),idProcedimiento,new Integer(idPretension));
			}
			tx.commit();
		
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch  (Exception e1) {

			}
			throw new BusinessException("Error al borrar los procedimientos del modulo");
		}
		
		
	}
	
	public void borraPretensionesDeProcedimiento(Integer idInstitucion, String idProcedimiento, Integer idPretension)throws BusinessException  {
		
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE SCS_PRETENSIONESPROCED");
		
		builder.append(" WHERE IDINSTITUCION =");
		
		builder.append(idInstitucion);
		builder.append(" AND IDPROCEDIMIENTO = ");

		builder.append(idProcedimiento);
		builder.append(" AND IDPRETENSION = ");
		
		builder.append(idPretension);
		
		
		try {
			deleteSQL(builder.toString());
		} catch (ClsExceptions e) {
			throw new BusinessException("Error al borra el procedimiento del módulo");
		}
				
	}
	public void insertaPretensionesDeProcedimiento(Integer idInstitucion, String idProcedimiento, Integer idPretension)throws BusinessException  {
		
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO SCS_PRETENSIONESPROCED (IDINSTITUCION, IDPRETENSION, IDPROCEDIMIENTO, FECHAMODIFICACION, USUMODIFICACION) VALUES (");
		builder.append(idInstitucion);
		builder.append(",");

		builder.append(idPretension);
		builder.append(",");
		
		builder.append(idProcedimiento);
		builder.append(",");
		builder.append("Sysdate");
		
		builder.append(",");		
		
		
		builder.append(usrbean.getUserName());
		
		builder.append(")");
		
		try {
			insertSQL(builder.toString());
		} catch (ClsExceptions e) {
			throw new BusinessException("Error al borra el procedimiento del módulo");
		}
				
	}
	public Vector getPretensionesQueNoEstenEnProcedimiento(String idInstitucion, String idProcedimiento) throws ClsExceptions
	{
		Vector datos = new Vector();
		
		try {
			StringBuilder query = new StringBuilder();
			query.append("SELECT P.IDPRETENSION,F_SIGA_GETRECURSO(P.DESCRIPCION, 1) DESCRIPCION, P.CODIGOEXT, P.IDINSTITUCION ") ;
			query.append("FROM  SCS_PRETENSION P WHERE ") ;
			query.append(" P.IDINSTITUCION =  ");
			query.append(idInstitucion);
			query.append(" AND P.IDPRETENSION NOT IN (SELECT PP.IDPRETENSION ");
			query.append("FROM SCS_PRETENSIONESPROCED PP ");
			query.append(" WHERE ");
			query.append(" PP.IDPROCEDIMIENTO = ");
			query.append(idProcedimiento);
			query.append(" AND PP.IDINSTITUCION =  ");
			query.append(idInstitucion);
			query.append(")");
			query.append(" ORDER BY DESCRIPCION ");
			datos = this.selectGenerico(query.toString());
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}	
	
	

}


