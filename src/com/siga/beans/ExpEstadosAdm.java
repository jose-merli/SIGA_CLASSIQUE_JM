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
import com.siga.expedientes.form.EstadosForm;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpEstadosAdm extends MasterBeanAdministrador {

	public ExpEstadosAdm(UsrBean usuario)
	{
	    super(ExpEstadosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
				ExpEstadosBean.C_IDESTADO,
            	ExpEstadosBean.C_NOMBRE,
            	ExpEstadosBean.C_ESEJECUCIONSANCION,
            	ExpEstadosBean.C_ESAUTOMATICO,
            	ExpEstadosBean.C_DESCRIPCION,
            	ExpEstadosBean.C_IDFASE,
            	ExpEstadosBean.C_FECHAMODIFICACION,
            	ExpEstadosBean.C_USUMODIFICACION,
            	ExpEstadosBean.C_IDINSTITUCION,
            	ExpEstadosBean.C_IDTIPOEXPEDIENTE,
            	ExpEstadosBean.C_IDFASESIGUIENTE,
            	ExpEstadosBean.C_IDESTADOSIGUIENTE,
            	ExpEstadosBean.C_MENSAJE,
            	ExpEstadosBean.C_PRESANCIONADO,
            	ExpEstadosBean.C_PREVISIBLE,
            	ExpEstadosBean.C_PREVISIBLEFICHA,
            	ExpEstadosBean.C_POSTACTUACIONESPRESCRITAS,
            	ExpEstadosBean.C_POSTSANCIONPRESCRITA,
            	ExpEstadosBean.C_POSTSANCIONFINALIZADA,
            	ExpEstadosBean.C_POSTANOTACIONESCANCELADAS,
            	ExpEstadosBean.C_POSTVISIBLE,
            	ExpEstadosBean.C_ESTADOFINAL,
            	ExpEstadosBean.C_ACTIVARALERTAS,
            	ExpEstadosBean.C_DIASANTELACION,
            	ExpEstadosBean.C_POSTVISIBLEFICHA            	
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpEstadosBean.C_IDINSTITUCION, ExpEstadosBean.C_IDFASE, ExpEstadosBean.C_IDESTADO, ExpEstadosBean.C_IDTIPOEXPEDIENTE};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpEstadosBean bean = null;

		try
		{
			bean = new ExpEstadosBean();
			
			bean.setIdEstado(UtilidadesHash.getInteger(hash, ExpEstadosBean.C_IDESTADO));
			bean.setNombre(UtilidadesHash.getString(hash, ExpEstadosBean.C_NOMBRE));
			bean.setEjecucionSancion(UtilidadesHash.getString(hash, ExpEstadosBean.C_ESEJECUCIONSANCION));
			bean.setAutomatico(UtilidadesHash.getString(hash, ExpEstadosBean.C_ESAUTOMATICO));
			bean.setDescripcion(UtilidadesHash.getString(hash, ExpEstadosBean.C_DESCRIPCION));
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpEstadosBean.C_IDFASE));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpEstadosBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpEstadosBean.C_IDTIPOEXPEDIENTE));
			bean.setIdFaseSiguiente(UtilidadesHash.getInteger(hash, ExpEstadosBean.C_IDFASESIGUIENTE));
			bean.setIdEstadoSiguiente(UtilidadesHash.getInteger(hash, ExpEstadosBean.C_IDESTADOSIGUIENTE));
			bean.setMensaje(UtilidadesHash.getString(hash, ExpEstadosBean.C_MENSAJE));
			bean.setPreSancionado(UtilidadesHash.getString(hash, ExpEstadosBean.C_PRESANCIONADO));
			bean.setPreVisible(UtilidadesHash.getString(hash, ExpEstadosBean.C_PREVISIBLE));
			bean.setPreVisibleFicha(UtilidadesHash.getString(hash, ExpEstadosBean.C_PREVISIBLEFICHA));
			bean.setPostActPrescritas(UtilidadesHash.getString(hash, ExpEstadosBean.C_POSTACTUACIONESPRESCRITAS));
			bean.setPostSancionPrescrita(UtilidadesHash.getString(hash, ExpEstadosBean.C_POSTSANCIONPRESCRITA));
			bean.setPostSancionFinalizada(UtilidadesHash.getString(hash, ExpEstadosBean.C_POSTSANCIONFINALIZADA));
			bean.setPostAnotCanceladas(UtilidadesHash.getString(hash, ExpEstadosBean.C_POSTANOTACIONESCANCELADAS));
			bean.setPostVisible(UtilidadesHash.getString(hash, ExpEstadosBean.C_POSTVISIBLE));
			bean.setEstadoFinal(UtilidadesHash.getString(hash, ExpEstadosBean.C_ESTADOFINAL));
			bean.setActivarAlertas(UtilidadesHash.getString(hash, ExpEstadosBean.C_ACTIVARALERTAS));
			bean.setDiasAntelacion(UtilidadesHash.getInteger(hash, ExpEstadosBean.C_DIASANTELACION));
			bean.setPostVisibleFicha(UtilidadesHash.getString(hash, ExpEstadosBean.C_POSTVISIBLEFICHA));
			
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
    public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
        Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ExpEstadosBean b = (ExpEstadosBean) bean;
			
			UtilidadesHash.set(htData, ExpEstadosBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(htData, ExpEstadosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpEstadosBean.C_ESEJECUCIONSANCION, b.getEjecucionSancion());
			UtilidadesHash.set(htData, ExpEstadosBean.C_ESAUTOMATICO, b.getAutomatico());
			UtilidadesHash.set(htData, ExpEstadosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ExpEstadosBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpEstadosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpEstadosBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpEstadosBean.C_IDFASESIGUIENTE, b.getIdFaseSiguiente());
			UtilidadesHash.set(htData, ExpEstadosBean.C_IDESTADOSIGUIENTE, b.getIdEstadoSiguiente());
			UtilidadesHash.set(htData, ExpEstadosBean.C_MENSAJE, b.getMensaje());
			UtilidadesHash.set(htData, ExpEstadosBean.C_PRESANCIONADO, b.getPreSancionado());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_PREVISIBLE, b.getPreVisible());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_PREVISIBLEFICHA, b.getPreVisibleFicha());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_POSTACTUACIONESPRESCRITAS, b.getPostActPrescritas());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_POSTSANCIONPRESCRITA, b.getPostSancionPrescrita());
			UtilidadesHash.set(htData, ExpEstadosBean.C_POSTSANCIONFINALIZADA, b.getPostSancionFinalizada());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_POSTANOTACIONESCANCELADAS, b.getPostAnotCanceladas());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_POSTVISIBLE, b.getPostVisible());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_ESTADOFINAL, b.getEstadoFinal());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_DIASANTELACION, b.getDiasAntelacion());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_ACTIVARALERTAS, b.getActivarAlertas());		
			UtilidadesHash.set(htData, ExpEstadosBean.C_POSTVISIBLEFICHA, b.getPostVisibleFicha());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    public Integer getNewIdEstado(String _idtipoexpediente, String _idfase, UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpEstadosBean.C_IDESTADO + 
        		") AS MAXVALOR FROM " + ExpEstadosBean.T_NOMBRETABLA + 
        		" WHERE " + ExpEstadosBean.C_IDTIPOEXPEDIENTE + "=" +_idtipoexpediente +
        		" AND " + ExpEstadosBean.C_IDINSTITUCION + "="+ _usr.getLocation() +
        		" AND " + ExpEstadosBean.C_IDFASE + "="+ _idfase; 
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
    
    public Vector selectBusqEstado(EstadosForm form) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_FASES=ExpFasesBean.T_NOMBRETABLA + " F";
//		String T_EXP_ESTADO=ExpEstadosBean.T_NOMBRETABLA + " E";
		
		//NOMBRES COLUMNAS PARA LA JOIN
		
		//Tabla exp_fases
		String F_NOMBRE="F." + ExpFasesBean.C_NOMBRE + " AS FASE";
		String F_IDFASE="F." + ExpFasesBean.C_IDFASE;
		
		//Tabla exp_estados
		String E_NOMBRE="E." + ExpEstadosBean.C_NOMBRE + " AS ESTADO";
		String E_IDESTADO="E." + ExpEstadosBean.C_IDESTADO;
		String E_MENSAJE="E." + ExpEstadosBean.C_MENSAJE;
		String E_AUTOMATICO="E." + ExpEstadosBean.C_ESAUTOMATICO;
		String E_ESTADOFINAL="E." + ExpEstadosBean.C_ESTADOFINAL;
		String E_ACTIVARALERTAS="E." + ExpEstadosBean.C_ACTIVARALERTAS;
		String E_DIASANTELACION="E." + ExpEstadosBean.C_DIASANTELACION;
		String E_EJECUCIONSANCION="E." + ExpEstadosBean.C_ESEJECUCIONSANCION;
		
		String where = " WHERE ";
        
//      campos de búsqueda
        where += "E." + ExpTiposAnotacionesBean.C_IDINSTITUCION + " = " + form.getIdInstitucion() + " AND E." + ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
//      join de las tablas ESTADOS E, FASES F
        where += " AND E."+ExpEstadosBean.C_IDFASE+" = "+ "F." + ExpFasesBean.C_IDFASE;
        where += " AND E."+ExpEstadosBean.C_IDINSTITUCION + " = " + "F." + ExpFasesBean.C_IDINSTITUCION;
        where += " AND E."+ExpEstadosBean.C_IDTIPOEXPEDIENTE + " = " + "F." + ExpFasesBean.C_IDTIPOEXPEDIENTE;
        if(form.getIdFase()!=null && !form.getIdFase().equals(""))
        	where += " AND E."+ExpEstadosBean.C_IDFASE+" ="+form.getIdFase() ;
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT ";	        
		    sql += F_NOMBRE+", ";	
		    sql += F_IDFASE+" AS IDFASE, ";	
		    sql += E_NOMBRE+", ";	
		    sql += E_IDESTADO+" AS IDESTADO, ";
		    sql += "(SELECT ES." + ExpEstadosBean.C_NOMBRE+"||' ('||FA."+ExpFasesBean.C_NOMBRE+"||')' "+
		             "FROM "+ExpEstadosBean.T_NOMBRETABLA + " ES," + ExpFasesBean.T_NOMBRETABLA + " FA " + 
		    		"WHERE ES." + ExpEstadosBean.C_IDINSTITUCION + " = E." + ExpEstadosBean.C_IDINSTITUCION +
		    		  " AND ES." + ExpEstadosBean.C_IDTIPOEXPEDIENTE + " = E." + ExpEstadosBean.C_IDTIPOEXPEDIENTE +
		    		  " AND ES." + ExpEstadosBean.C_IDFASE + "= E." + ExpEstadosBean.C_IDFASESIGUIENTE +
		    		  " AND ES." + ExpEstadosBean.C_IDESTADO + "= E." + ExpEstadosBean.C_IDESTADOSIGUIENTE +
		    		  " AND ES." + ExpEstadosBean.C_IDINSTITUCION + "=FA." + ExpFasesBean.C_IDINSTITUCION +
		    		  " AND ES." + ExpEstadosBean.C_IDTIPOEXPEDIENTE + "=FA." + ExpFasesBean.C_IDTIPOEXPEDIENTE +
		    		  " AND ES." + ExpEstadosBean.C_IDFASE + "=FA." + ExpFasesBean.C_IDFASE + ") AS SIGUIENTE, ";
		    sql += E_MENSAJE+" AS MENSAJE, ";
		    sql += E_AUTOMATICO+" AS AUTOMATICO, ";
		    sql += E_ESTADOFINAL+" AS ESTADOFINAL, ";
		    sql += E_DIASANTELACION+" AS DIASANTELACION, ";
		    sql += E_ACTIVARALERTAS+" AS ACTIVARALERTAS, ";
		    sql += E_EJECUCIONSANCION+" AS EJECUCIONSANCION";
		    
			sql += " FROM ";
		    sql += ExpEstadosBean.T_NOMBRETABLA+" E, "+T_EXP_FASES;
		    		    		
			sql += " " + where;
			sql += " ORDER BY FASE,ESTADO";

			ClsLogging.writeFileLog("ExpEstadosAdm -> LA QUERY ES: "+sql,3);
	        
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
    public void updateEstadosSiguientesAsociados(Hashtable htEstado) throws ClsExceptions 
	{ 
    
    	StringBuffer sqlUpdate= new StringBuffer();
    	int indiceCodigo = 0;
    	Hashtable codigosUpdate = new Hashtable();
    	sqlUpdate.append(" ");
    	sqlUpdate.append(" UPDATE ");
    	sqlUpdate.append(ExpEstadosBean.T_NOMBRETABLA);
    	sqlUpdate.append(" SET ");
    	sqlUpdate.append(ExpEstadosBean.C_IDESTADOSIGUIENTE);
    	sqlUpdate.append(" = NULL, ");
    	sqlUpdate.append(ExpEstadosBean.C_IDFASESIGUIENTE);
    	sqlUpdate.append(" = NULL ");
    	sqlUpdate.append(" WHERE ");
    	sqlUpdate.append(ExpEstadosBean.C_IDESTADOSIGUIENTE);
    	sqlUpdate.append(" = :");
    	indiceCodigo++;
    	codigosUpdate.put(new Integer(indiceCodigo),(String)htEstado.get(ExpEstadosBean.C_IDESTADO));
    	sqlUpdate.append(indiceCodigo);
    	sqlUpdate.append(" AND " );
    	sqlUpdate.append(ExpEstadosBean.C_IDFASESIGUIENTE);
    	sqlUpdate.append(" = :");
    	indiceCodigo++;
    	codigosUpdate.put(new Integer(indiceCodigo),(String)htEstado.get(ExpEstadosBean.C_IDFASE));
    	sqlUpdate.append(indiceCodigo);
    	sqlUpdate.append(" AND " );
 		sqlUpdate.append(ExpEstadosBean.C_IDINSTITUCION);
    	sqlUpdate.append(" =  :");
    	indiceCodigo++;
    	codigosUpdate.put(new Integer(indiceCodigo),(String)htEstado.get(ExpEstadosBean.C_IDINSTITUCION));
    	sqlUpdate.append(indiceCodigo);
    	sqlUpdate.append(" AND ");
    	sqlUpdate.append(ExpEstadosBean.C_IDTIPOEXPEDIENTE);
    	sqlUpdate.append(" = :");
    	indiceCodigo++;
    	codigosUpdate.put(new Integer(indiceCodigo),(String)htEstado.get(ExpEstadosBean.C_IDTIPOEXPEDIENTE));
    	sqlUpdate.append(indiceCodigo);

		
		try{
			insertSQLBind(sqlUpdate.toString(),codigosUpdate);
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'update' en B.D."); 
		}
	}
    
   
}
