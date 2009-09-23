/*
 * Created on Dec 27, 2004
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpCampoConfAdm extends MasterBeanAdministrador {

	public ExpCampoConfAdm(UsrBean usuario)
	{
	    super(ExpCampoConfBean.T_NOMBRETABLA, usuario);
	}
    
    protected String[] getCamposBean() {
        
        String[] campos = {
				ExpCampoConfBean.C_IDINSTITUCION,
				ExpCampoConfBean.C_IDTIPOEXPEDIENTE,
				ExpCampoConfBean.C_IDCAMPO,
				ExpCampoConfBean.C_IDPESTANACONF,
				ExpCampoConfBean.C_IDCAMPOCONF,
				ExpCampoConfBean.C_ORDEN,
				ExpCampoConfBean.C_SELECCIONADO,
				ExpCampoConfBean.C_TIPO,
				ExpCampoConfBean.C_NOMBRE,
				ExpCampoConfBean.C_FECHAMODIFICACION,
            	ExpCampoConfBean.C_USUMODIFICACION          	
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpCampoConfBean.C_IDINSTITUCION,
				ExpCampoConfBean.C_IDTIPOEXPEDIENTE,
				ExpCampoConfBean.C_IDCAMPO,
				ExpCampoConfBean.C_IDPESTANACONF,
				ExpCampoConfBean.C_IDCAMPOCONF};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        String[] orden = {ExpCampoConfBean.C_ORDEN};
        return orden;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpCampoConfBean bean = null;

		try
		{
			bean = new ExpCampoConfBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpCampoConfBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpCampoConfBean.C_IDTIPOEXPEDIENTE));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ExpCampoConfBean.C_IDCAMPO));
			bean.setIdPestanaConf(UtilidadesHash.getInteger(hash, ExpCampoConfBean.C_IDPESTANACONF));
			bean.setIdCampoConf(UtilidadesHash.getInteger(hash, ExpCampoConfBean.C_IDCAMPOCONF));
			bean.setSeleccionado(UtilidadesHash.getInteger(hash, ExpCampoConfBean.C_SELECCIONADO));
			bean.setOrden(UtilidadesHash.getInteger(hash, ExpCampoConfBean.C_ORDEN));
			bean.setTipo(UtilidadesHash.getString(hash, ExpCampoConfBean.C_TIPO));
			bean.setNombre(UtilidadesHash.getString(hash, ExpCampoConfBean.C_NOMBRE));
			
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

			ExpCampoConfBean b = (ExpCampoConfBean) bean;
			
			UtilidadesHash.set(htData, ExpCampoConfBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpCampoConfBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpCampoConfBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ExpCampoConfBean.C_IDPESTANACONF, b.getIdPestanaConf());
			UtilidadesHash.set(htData, ExpCampoConfBean.C_IDCAMPOCONF, b.getIdCampoConf());
			UtilidadesHash.set(htData, ExpCampoConfBean.C_SELECCIONADO, b.getSeleccionado());
			UtilidadesHash.set(htData, ExpCampoConfBean.C_ORDEN, b.getOrden());
			UtilidadesHash.set(htData, ExpCampoConfBean.C_TIPO, b.getTipo());
			UtilidadesHash.set(htData, ExpCampoConfBean.C_NOMBRE, b.getNombre());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    
    public Vector getCamposPlantilla(String idInstitucion,String idTipoExpediente, String idCampo, String idPestanaConf) throws ClsExceptions{
        
        Hashtable criterios = new Hashtable();
        Vector salida = null; 
        
        try{
            criterios.put(ExpCampoConfBean.C_IDINSTITUCION,idInstitucion);
            criterios.put(ExpCampoConfBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
            criterios.put(ExpCampoConfBean.C_IDCAMPO,idCampo);
            criterios.put(ExpCampoConfBean.C_IDPESTANACONF,idPestanaConf);
            Vector v = this.select(criterios);
 	        if (v!=null && v.size()>0) {
 	            salida = v;
 	        }
        }catch (Exception e) { 	
 			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
 		}
 	        
        return salida; 
 	    
    }    
     
    public ExpCampoConfBean getCampoConfigurable(String idInstitucion,String idTipoExpediente, String idCampo, String idPestanaConf, String idCampoConf) throws ClsExceptions{
        
        Hashtable criterios = new Hashtable();
        ExpCampoConfBean salida = null; 
        
        try{
            criterios.put(ExpCampoConfBean.C_IDINSTITUCION,idInstitucion);
            criterios.put(ExpCampoConfBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
            criterios.put(ExpCampoConfBean.C_IDCAMPO,idCampo);
            criterios.put(ExpCampoConfBean.C_IDPESTANACONF,idPestanaConf);
            criterios.put(ExpCampoConfBean.C_IDCAMPOCONF,idCampoConf);
            Vector v = this.selectByPK(criterios);
 	        if (v!=null && v.size()>0) {
 	            salida = (ExpCampoConfBean) v.elementAt(0);
 	        }
        }catch (Exception e) { 	
 			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
 		}
 	        
        return salida; 
 	    
    }    
     
    public Integer getNuevoId (String idInstitucion,String idTipoExpediente, String idCampo, String idpestanaConf)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + ExpCampoConfBean.C_IDCAMPOCONF+ ") + 1) AS " + ExpCampoConfBean.C_IDCAMPOCONF + 
			  			 " FROM " + ExpCampoConfBean.T_NOMBRETABLA + 
						 " WHERE " + ExpCampoConfBean.T_NOMBRETABLA +"." + ExpCampoConfBean.C_IDINSTITUCION + " = " + idInstitucion +
						 " AND " + ExpCampoConfBean.T_NOMBRETABLA +"." + ExpCampoConfBean.C_IDTIPOEXPEDIENTE + " = " + idTipoExpediente +
						 " AND " + ExpCampoConfBean.T_NOMBRETABLA +"." + ExpCampoConfBean.C_IDCAMPO + " = " + idCampo +
						 " AND " + ExpCampoConfBean.T_NOMBRETABLA +"." + ExpCampoConfBean.C_IDPESTANACONF + " = " + idpestanaConf;

			// RGG cambio visibilidad
			rc = this.findForUpdate(sql);
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				Integer id = UtilidadesHash.getInteger(ht, ExpCampoConfBean.C_IDCAMPOCONF);
				if (id == null) {
					return new Integer(1);
				}
				else return id;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return null;
	}	
    
    public Vector getCamposValoresPlantilla (String idInstitucion,String idTipoExpediente, String numeroExpediente, String anioExpediente, String idpestanaConf)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		Vector salida = new Vector();
		Hashtable codigos = new  Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idTipoExpediente);
		codigos.put(new Integer(3),idpestanaConf);
		codigos.put(new Integer(4),numeroExpediente);
		codigos.put(new Integer(5),anioExpediente);
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " select cc.nombre as ETIQUETA, cc.idcampoconf as IDCAMPOCONF, v.idcampo as IDCAMPO, v.valor as VALOR " +
				" from exp_campoconf cc, exp_camposvalor v " +
			    " where cc.idinstitucion = v.idinstitucion_tipoexpediente (+) " +
			    " and   cc.idtipoexpediente = v.idtipoexpediente (+)  " +
			    " and   cc.idcampo = v.idcampo (+)  " +
			    " and   cc.idpestanaconf = v.idpestanaconf (+)  " +
			    " and   cc.idcampoconf = v.idcampoconf (+)  " +
			    " and   seleccionado=1 " +
			    " and   cc.idinstitucion = :1  " +
			    " and   cc.idtipoexpediente = :2 " +
			    " and   cc.idpestanaconf = :3 " +
			    " and   v.numeroexpediente (+) = :4  " +
			    " and   v.anioexpediente (+) = :5 " +
			    " order by cc.orden ";

			// RGG cambio visibilidad
			rc = this.findBind(sql,codigos);
			if (rc!=null) {
			    for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable ht = fila.getRow();
					salida.add(ht);
			    }
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getCamposValoresPlantilla' en B.D.");		
		}
		return salida;
	}	

    public Vector obtenerCamposConfigurados (String idInstitucion,String idTipoExpediente,String idPestanaConf)throws ClsExceptions, SIGAException 
	{
		Vector salida = new Vector();

		Hashtable codigos = new  Hashtable();
		codigos.put("IDINSTITUCION",idInstitucion);
		codigos.put("IDTIPOEXPEDIENTE",idTipoExpediente);
		codigos.put("IDPESTANACONF",idPestanaConf);
		codigos.put("SELECCIONADO",new Integer(1));
			
		try {		

			// RGG cambio visibilidad
			salida = this.select(codigos);
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'obtenerCamposConfigurados' en B.D.");		
		}
		return salida;
	}

	public boolean ordenRepetido(ExpCampoConfBean bean) throws ClsExceptions, SIGAException {
		StringBuffer where = new StringBuffer();
		
		where.append("where ").append("IDINSTITUCION=").append(bean.getIdInstitucion())
			 .append(" and ").append("IDTIPOEXPEDIENTE=").append(bean.getIdTipoExpediente())
			 .append(" and ").append("IDPESTANACONF=").append(bean.getIdPestanaConf())
			 .append(" and ").append("ORDEN=").append(bean.getOrden());
		
		if (bean.getIdCampoConf()!=null)
			 where.append(" and ").append("IDCAMPOCONF <>").append(bean.getIdCampoConf());
		
		try {
			Vector resultados = this.select(where.toString());
			
			if (resultados==null || resultados.size()==0){
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'ordenRepetido' en B.D.");		
		}
	}

	public boolean nombreRepetido(ExpCampoConfBean bean) throws ClsExceptions, SIGAException {
		StringBuffer where = new StringBuffer();
		
		where.append("where ").append("IDINSTITUCION=").append(bean.getIdInstitucion())
			 .append(" and ").append("IDTIPOEXPEDIENTE=").append(bean.getIdTipoExpediente())
			 .append(" and ").append("IDPESTANACONF=").append(bean.getIdPestanaConf())
			 .append(" and ").append("NOMBRE='").append(bean.getNombre()).append("'");
		
		if (bean.getIdCampoConf()!=null)
			 where.append(" and ").append("IDCAMPOCONF <>").append(bean.getIdCampoConf());
		
		try {
			Vector resultados = this.select(where.toString());
			
			if (resultados==null || resultados.size()==0){
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'nombreRepetido' en B.D.");		
		}
	}	
}
