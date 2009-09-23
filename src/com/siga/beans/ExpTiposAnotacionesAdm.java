/*
 * Created on Dec 27, 2004
 * @author jmgrau 
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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpTiposAnotacionesAdm extends MasterBeanAdministrador {
    public final static Integer codigoTipoComunicacion = new Integer("-1");
    public final static Integer codigoTipoAutomatico = new Integer("-2");
    public final static Integer codigoTipoCambioEstado = new Integer("-3");
	
	public ExpTiposAnotacionesAdm(UsrBean usuario)
	{
	    super(ExpTiposAnotacionesBean.T_NOMBRETABLA, usuario);	    
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                ExpTiposAnotacionesBean.C_IDTIPOANOTACION,
                ExpTiposAnotacionesBean.C_NOMBRE,
				ExpTiposAnotacionesBean.C_IDINSTITUCION,
				ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE,
				ExpTiposAnotacionesBean.C_IDESTADO,
				ExpTiposAnotacionesBean.C_IDFASE,
				ExpTiposAnotacionesBean.C_MENSAJE,
				ExpTiposAnotacionesBean.C_FECHAMODIFICACION,
				ExpTiposAnotacionesBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpTiposAnotacionesBean.C_IDINSTITUCION, ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE, ExpTiposAnotacionesBean.C_IDTIPOANOTACION};
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
        ExpTiposAnotacionesBean bean = null;

		try
		{
			bean = new ExpTiposAnotacionesBean();
			
			bean.setIdTipoAnotacion(UtilidadesHash.getInteger(hash, ExpTiposAnotacionesBean.C_IDTIPOANOTACION));
			bean.setNombre(UtilidadesHash.getString(hash, ExpTiposAnotacionesBean.C_NOMBRE));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpTiposAnotacionesBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, ExpTiposAnotacionesBean.C_IDESTADO));
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpTiposAnotacionesBean.C_IDFASE));
			bean.setMensaje(UtilidadesHash.getString(hash, ExpTiposAnotacionesBean.C_MENSAJE));
			
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

			ExpTiposAnotacionesBean b = (ExpTiposAnotacionesBean) bean;

			UtilidadesHash.set(htData, ExpTiposAnotacionesBean.C_IDTIPOANOTACION, b.getIdTipoAnotacion());
			UtilidadesHash.set(htData, ExpTiposAnotacionesBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpTiposAnotacionesBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(htData, ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpTiposAnotacionesBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(htData, ExpTiposAnotacionesBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpTiposAnotacionesBean.C_MENSAJE, b.getMensaje());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdTipoAnotacion(String _idtipoexpediente, UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpTiposAnotacionesBean.C_IDTIPOANOTACION + 
        		") AS MAXVALOR FROM " + ExpTiposAnotacionesBean.T_NOMBRETABLA + 
        		" WHERE " + ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE + "=" +_idtipoexpediente +
        		" AND " + ExpTiposAnotacionesBean.C_IDINSTITUCION + "="+ _usr.getLocation(); 
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
    
    public Vector selectBusqAnot(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_FASES="EXP_FASES F";
		String T_EXP_ESTADO="EXP_ESTADO E";
		
		//NOMBRES COLUMNAS PARA LA JOIN
		
		//Tabla exp_fases
		String F_NOMBRE="F.NOMBRE AS NOMBREFASE";
		String F_IDFASE="F.IDFASE";
		
		//Tabla exp_estados
		String E_NOMBRE="E.NOMBRE AS NOMBREESTADO";
		String E_IDESTADO="E.IDESTADO";
		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String [] fields = this.getCamposBean();
	        
	        String sql = "SELECT ";
	        //todos los campos de la tabla exp_tipoanotacion separados por coma
		    for(int i=0;i<fields.length; i++){
			  sql += "TA."+fields[i]+", ";
			}
		    
		    sql += F_NOMBRE+", ";	
		    sql += F_IDFASE+", ";	
		    sql += E_NOMBRE+", ";	
		    sql += E_IDESTADO+" ";
		    
			sql += " FROM ";
		    sql += ExpTiposAnotacionesBean.T_NOMBRETABLA+" TA, "+T_EXP_FASES+", "+T_EXP_ESTADO;
		    		    		
			sql += " " + where;
			sql += " ORDER BY TA."+ExpTiposAnotacionesBean.C_NOMBRE;

			ClsLogging.writeFileLog("ExpTiposAnotacionesAdm -> QUERY: "+sql,3);
	        
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
    
    public Vector selectTiposAnotaciones(String idInstitucion, String idTipoExpediente, String idFase, String idEstado) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
	        String sql = "SELECT * FROM "+ExpTiposAnotacionesBean.T_NOMBRETABLA;
	        sql += " WHERE (";		    		    		
			sql += "("+ExpTiposAnotacionesBean.C_IDINSTITUCION+"="+idInstitucion+" AND ";
			sql += ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE+"="+idTipoExpediente+" AND ";
			sql += ExpTiposAnotacionesBean.C_IDFASE+" IS null AND "+ExpTiposAnotacionesBean.C_IDESTADO+" IS null)";
			
			sql += " OR ";
			
			sql += "("+ExpTiposAnotacionesBean.C_IDINSTITUCION+"="+idInstitucion+" AND ";
			sql += ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE+"="+idTipoExpediente+" AND ";
			if (idFase!=null){
				sql += ExpTiposAnotacionesBean.C_IDFASE+"="+idFase+" AND ";
			}else{
				sql += ExpTiposAnotacionesBean.C_IDFASE+" IS null AND ";
			}
			sql += ExpTiposAnotacionesBean.C_IDESTADO+" IS null)";
			
			sql += " OR ";
			
			sql += "("+ExpTiposAnotacionesBean.C_IDINSTITUCION+"="+idInstitucion+" AND ";
			sql += ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE+"="+idTipoExpediente+" AND ";
			if (idFase!=null){
				sql += ExpTiposAnotacionesBean.C_IDFASE+"="+idFase+" AND ";
			}else{
				sql += ExpTiposAnotacionesBean.C_IDFASE+" IS null AND ";
			}
			if (idEstado!=null){
				sql += ExpTiposAnotacionesBean.C_IDESTADO+"="+idEstado+"))";	
			}else{
				sql += ExpTiposAnotacionesBean.C_IDESTADO+" IS null))";	
			}
			
			ClsLogging.writeFileLog("ExpTiposAnotacionesAdm -> QUERY: "+sql,3);
	        
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
}
