/*
 * Created on Dec 22, 2004
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
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean de Tipo de Expediente
 */
public class ExpTipoExpedienteAdm extends MasterBeanAdministrador {
	
	public ExpTipoExpedienteAdm(UsrBean usuario)
	{
	    super(ExpTipoExpedienteBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = {ExpTipoExpedienteBean.C_NOMBRE,
				ExpTipoExpedienteBean.C_FECHAMODIFICACION,
				ExpTipoExpedienteBean.C_USUMODIFICACION,
				ExpTipoExpedienteBean.C_IDINSTITUCION,
				ExpTipoExpedienteBean.C_ESGENERAL,
				ExpTipoExpedienteBean.C_TIEMPOCADUCIDAD,
				ExpTipoExpedienteBean.C_DIASANTELACIONCAD,
				ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE,
				ExpTipoExpedienteBean.C_RELACIONEXPEDIENTE
				,ExpTipoExpedienteBean.C_RELACIONEJG
				,ExpTipoExpedienteBean.C_ENVIARAVISOS
				,ExpTipoExpedienteBean.C_IDTIPOENVIOS
				,ExpTipoExpedienteBean.C_IDPLANTILLAENVIOS
				,ExpTipoExpedienteBean.C_IDPLANTILLA
				};

		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ExpTipoExpedienteBean.C_IDINSTITUCION, ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
	    String[] orden = {ExpTipoExpedienteBean.C_NOMBRE};
		return orden;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ExpTipoExpedienteBean bean = null;

		try
		{
			bean = new ExpTipoExpedienteBean();
			
			bean.setNombre(UtilidadesHash.getString(hash, ExpTipoExpedienteBean.C_NOMBRE));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_IDINSTITUCION));
			bean.setEsGeneral(UtilidadesHash.getString(hash, ExpTipoExpedienteBean.C_ESGENERAL));
			bean.setTiempoCaducidad(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_TIEMPOCADUCIDAD));
			bean.setDiasAntelacionCad(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_DIASANTELACIONCAD));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE));
			bean.setRelacionExpediente(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_RELACIONEXPEDIENTE));
			bean.setRelacionEjg(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_RELACIONEJG));
			bean.setEnviarAvisos(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_ENVIARAVISOS));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_IDTIPOENVIOS));
			bean.setIdPlantillaEnvios(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_IDPLANTILLAENVIOS));
			bean.setIdPlantilla(UtilidadesHash.getInteger(hash, ExpTipoExpedienteBean.C_IDPLANTILLA));
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

			ExpTipoExpedienteBean b = (ExpTipoExpedienteBean) bean;

			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_ESGENERAL, b.getEsGeneral());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_TIEMPOCADUCIDAD, b.getTiempoCaducidad());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_DIASANTELACIONCAD, b.getDiasAntelacionCad());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_RELACIONEJG, b.getRelacionEjg());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_RELACIONEXPEDIENTE, b.getRelacionExpediente());			
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_ENVIARAVISOS, b.getEnviarAvisos());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_IDPLANTILLAENVIOS, b.getIdPlantillaEnvios());
			UtilidadesHash.set(htData, ExpTipoExpedienteBean.C_IDPLANTILLA, b.getIdPlantilla());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

	/** Funcion getNewIdTipoExpediente (UsrBean _usr)
	 * Genera el id de un nuevo tipo de expediente
	 * @param usrBean
	 * @return nuevo idTipoExpediente
	 * */
    public Integer getNewIdTipoExpediente(UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE + 
        		") AS MAXVALOR FROM " + ExpTipoExpedienteBean.T_NOMBRETABLA + 
        		" WHERE " + ExpTipoExpedienteBean.C_IDINSTITUCION + "="+ _usr.getLocation(); 
        int valor=1; // Si no hay registros, es el valor que tomará
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será "" Si no hay registros
            if(!((String)htRow.get("MAXVALOR")).equals("")) {
                Integer valorInt=Integer.valueOf((String)htRow.get("MAXVALOR"));
                valor=valorInt.intValue();
                valor++;
            }
            
        }
        return new Integer(valor);        
    }
    
    /** Funcion getCamposVisibles (String idInstitucion_TipoExpediente, String idTipoExpediente)
	 * Obtiene una hash con los campos por tipo de expediente, y su estado (visible o no)
	 * @param idInstitucion_TipoExpediente
	 * @param idTipoExpediente
	 * @return hash(campo,estado)
	 * */
    public Hashtable getCamposVisibles (String idInstitucion_TipoExpediente, String idTipoExpediente) throws ClsExceptions{
        RowsContainer rc=new RowsContainer();
        Hashtable resultado = new Hashtable();
        Hashtable htRow = new Hashtable();
		
        String sql = "SELECT ";
        sql += ExpCampoTipoExpedienteBean.C_IDCAMPO+", "+ExpCampoTipoExpedienteBean.C_VISIBLE;
        sql += " FROM "+ExpCampoTipoExpedienteBean.T_NOMBRETABLA;
        sql += " WHERE ";
        sql += ExpCampoTipoExpedienteBean.C_IDINSTITUCION+"="+idInstitucion_TipoExpediente+" AND ";
        sql += ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE+"="+idTipoExpediente+" AND ";
        sql += ExpCampoTipoExpedienteBean.C_IDCAMPO+" IN ";
        sql += "(SELECT "+ExpCamposExpedientesBean.C_IDCAMPO+" FROM "+ExpCamposExpedientesBean.T_NOMBRETABLA+" WHERE "+ExpCamposExpedientesBean.C_TIPOCAMPO+"='C')";
        
        try{
        	if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					htRow=((Row)rc.get(i)).getRow();
					resultado.put(htRow.get(ExpCampoTipoExpedienteBean.C_IDCAMPO),htRow.get(ExpCampoTipoExpedienteBean.C_VISIBLE));				
				}
			}
        }catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
	        
        return resultado; 
	    
    }
    
    public Vector select(String idInstitucion,String idTipoExpediente) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			String where = " WHERE ";        
	        where += ExpFasesBean.C_IDINSTITUCION + " = " + idInstitucion+" AND "+ExpFasesBean.C_IDTIPOEXPEDIENTE + " = " + idTipoExpediente;
			
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}
    
	public boolean updateRelacion(UsrBean user) throws ClsExceptions
	{

		StringBuffer sql = new StringBuffer();
		boolean salida= false;
		try {

			sql.append(" update " + ExpTipoExpedienteBean.T_NOMBRETABLA);
			sql.append("    set " + ExpTipoExpedienteBean.C_RELACIONEJG + " = 0");
			sql.append("  where " + ExpTipoExpedienteBean.C_IDINSTITUCION + "=" + user.getLocation());

			updateSQL(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}
	
	/** Funcion getNewIdTipoExpediente (UsrBean _usr)
	 * Genera el id de un nuevo tipo de expediente
	 * @param usrBean
	 * @return nuevo idTipoExpediente
	 * */
    public Integer getImporteCaducidad(String idInstitucion,String idTipoExpediente) throws ClsExceptions{
        RowsContainer rows = new RowsContainer();
        String sql="SELECT " + ExpTipoExpedienteBean.C_TIEMPOCADUCIDAD + 
        		" AS CADUCIDAD FROM " + ExpTipoExpedienteBean.T_NOMBRETABLA + 
        		" WHERE " + ExpTipoExpedienteBean.C_IDINSTITUCION + "="+ idInstitucion +
        		" AND " + ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE + "="+ idTipoExpediente;
        int valor=0; // Si no hay registros, es el valor que tomará
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será "" Si no hay registros
            if(!((String)htRow.get("CADUCIDAD")).equals("")) {
                valor=Integer.valueOf((String)htRow.get("CADUCIDAD")).intValue();
            }
            
        }
        return new Integer(valor);        
    }	
	
    //mhg Incidencia EJGs
    /** 
     * Devuelve true si existe algun Tipo de expediente en la institucion pasada como primer parametro, 
     * tal que puede relacionarse con un EJG o crearse a partir de un EJG
     * 
     * @param idInstitucion
     * @return boolean
     * @throws ClsExceptions
     */
	public boolean existeEJGs(String idTipoExpediente, String idInstitucion) throws ClsExceptions{
		 
		 boolean valor = false;
		
		 try {
			ExpTipoExpedienteAdm tipoExptAdm = new ExpTipoExpedienteAdm(usrbean);
			/*Hashtable datosTipoExp = new Hashtable();
			datosTipoExp.put(ExpTipoExpedienteBean.C_IDINSTITUCION,idInstitucion);
			datosTipoExp.put(ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			datosTipoExp.put(ExpTipoExpedienteBean.C_RELACIONEJG, 1);
			*/
			String where = " WHERE idInstitucion = " + idInstitucion + " and idTipoExpediente <> " + idTipoExpediente + " and relacionejg = 1";
			
			Vector v = tipoExptAdm.select(where);
			if (v != null && !v.isEmpty())
				valor = true;
		
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D.");
		}
		
		return valor;  
   }

}
