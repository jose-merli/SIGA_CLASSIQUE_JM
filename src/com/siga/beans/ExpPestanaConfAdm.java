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
import com.siga.general.SIGAException;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpPestanaConfAdm extends MasterBeanAdministrador {

	public ExpPestanaConfAdm(UsrBean usuario)
	{
	    super(ExpPestanaConfBean.T_NOMBRETABLA, usuario);
	}
    
    protected String[] getCamposBean() {
        
        String[] campos = {
				ExpPestanaConfBean.C_IDINSTITUCION,
				ExpPestanaConfBean.C_IDTIPOEXPEDIENTE,
				ExpPestanaConfBean.C_IDCAMPO,
				ExpPestanaConfBean.C_IDPESTANACONF,
				ExpPestanaConfBean.C_NOMBRE,
				ExpPestanaConfBean.C_SELECCIONADO,
				ExpPestanaConfBean.C_FECHAMODIFICACION,
            	ExpPestanaConfBean.C_USUMODIFICACION         	
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpPestanaConfBean.C_IDINSTITUCION,
				ExpPestanaConfBean.C_IDTIPOEXPEDIENTE,
				ExpPestanaConfBean.C_IDCAMPO,
				ExpPestanaConfBean.C_IDPESTANACONF};
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
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpPestanaConfBean bean = null;

		try
		{
			bean = new ExpPestanaConfBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpPestanaConfBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpPestanaConfBean.C_IDTIPOEXPEDIENTE));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ExpPestanaConfBean.C_IDCAMPO));
			bean.setIdPestanaConf(UtilidadesHash.getInteger(hash, ExpPestanaConfBean.C_IDPESTANACONF));
			bean.setSeleccionado(UtilidadesHash.getInteger(hash, ExpPestanaConfBean.C_SELECCIONADO));
			bean.setNombre(UtilidadesHash.getString(hash, ExpPestanaConfBean.C_NOMBRE));
			
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

			ExpPestanaConfBean b = (ExpPestanaConfBean) bean;
			
			UtilidadesHash.set(htData, ExpPestanaConfBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpPestanaConfBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpPestanaConfBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ExpPestanaConfBean.C_IDPESTANACONF, b.getIdPestanaConf());
			UtilidadesHash.set(htData, ExpPestanaConfBean.C_SELECCIONADO, b.getSeleccionado());
			UtilidadesHash.set(htData, ExpPestanaConfBean.C_NOMBRE, b.getNombre());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    
   public ExpPestanaConfBean getPestana(int numero, String idInstitucion,String idTipoExpediente, String idCampo) throws ClsExceptions{
       
       Hashtable htRow = new Hashtable();
       ExpPestanaConfBean bean = null; 
       
       try{
	        htRow.put(ExpPestanaConfBean.C_IDINSTITUCION,idInstitucion);
	        htRow.put(ExpPestanaConfBean.C_IDTIPOEXPEDIENTE,idTipoExpediente); 
	        if (idCampo!=null) {
	            htRow.put(ExpPestanaConfBean.C_IDCAMPO,idCampo);
	        }
	        htRow.put(ExpPestanaConfBean.C_IDPESTANACONF,new Integer(numero)); 
	        Vector v = this.select(htRow);
	        if (v!=null && v.size()>0) {
	            bean = (ExpPestanaConfBean) v.get(0);
	        }
       }catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
	        
       return bean; 
	    
   }    

   public String getNombrePestanaDesdeProceso (String idInstitucion,String idTipoExpediente, String idProceso)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idTipoExpediente);
		codigos.put(new Integer(3),idProceso);

		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " select p.NOMBRE from exp_pestanaconf p , exp_camposexpedientes c " +
			    	" where p.idcampo = c.idcampo " +
			    	" and p.idinstitucion=:1 " +
			    	" and p.idtipoexpediente=:2 " +
			    	" and c.proceso=:3 ";

			// RGG cambio visibilidad
			rc = this.findBind(sql,codigos);
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				String nombre = UtilidadesHash.getString(ht, "NOMBRE");
				return nombre;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNombrePestanaDesdeProceso' en B.D.");		
		}
		return null;
	}	
   
   
   public String obtenerNombrePetanaGeneral (String idInstitucion,String idTipoExpediente)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idTipoExpediente);		

		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " select p.NOMBRE from exp_pestanaconf p , exp_camposexpedientes c " +
			    	" where p.idcampo = c.idcampo " +
			    	" and p.idinstitucion=:1 " +
			    	" and p.idtipoexpediente=:2 " +
			    	" and p.idcampo = 14 ";

			// RGG cambio visibilidad
			rc = this.findBind(sql,codigos);
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				String nombre = UtilidadesHash.getString(ht, "NOMBRE");
				return nombre;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNombrePestanaDesdeProceso' en B.D.");		
		}
		return null;
	}	
}
