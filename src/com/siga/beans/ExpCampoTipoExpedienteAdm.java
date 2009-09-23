/*
 * Created on Dec 27, 2004
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpCampoTipoExpedienteAdm extends MasterBeanAdministrador {

	public ExpCampoTipoExpedienteAdm(UsrBean usuario)
	{
	    super(ExpCampoTipoExpedienteBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                ExpCampoTipoExpedienteBean.C_IDINSTITUCION,
				ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE,
				ExpCampoTipoExpedienteBean.C_IDCAMPO,
				ExpCampoTipoExpedienteBean.C_VISIBLE,
				ExpCampoTipoExpedienteBean.C_FECHAMODIFICACION,
				ExpCampoTipoExpedienteBean.C_USUMODIFICACION,ExpCampoTipoExpedienteBean.C_NOMBRE
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpCampoTipoExpedienteBean.C_IDINSTITUCION,ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE, ExpCampoTipoExpedienteBean.C_IDCAMPO};
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
        ExpCampoTipoExpedienteBean bean = null;

		try
		{
			bean = new ExpCampoTipoExpedienteBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpCampoTipoExpedienteBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ExpCampoTipoExpedienteBean.C_IDCAMPO));
			bean.setVisible(UtilidadesHash.getString(hash, ExpCampoTipoExpedienteBean.C_VISIBLE));
			bean.setNombre(UtilidadesHash.getString(hash, ExpCampoTipoExpedienteBean.C_NOMBRE));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpCampoTipoExpedienteBean.C_FECHAMODIFICACION));
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpCampoTipoExpedienteBean.C_USUMODIFICACION));			
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

			ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean) bean;

			UtilidadesHash.set(htData, ExpCampoTipoExpedienteBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(htData, ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpCampoTipoExpedienteBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ExpCampoTipoExpedienteBean.C_VISIBLE, b.getVisible());
			UtilidadesHash.set(htData, ExpCampoTipoExpedienteBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpCampoTipoExpedienteBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpCampoTipoExpedienteBean.C_USUMODIFICACION, b.getUsuModificacion());
			}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;
	}
    
    /** Funcion getNewIdCampo (String _idtipoexpediente, UsrBean _usr)
	 * Genera un nuevo idCampo para un tipo de expediente
	 * @param idtipoexpediente
	 * @param user
	 * @return newIdCampo
	 * */
    public Integer getNewIdCampo(String _idtipoexpediente, UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpCampoTipoExpedienteBean.C_IDCAMPO + 
        		") AS MAXVALOR FROM " + ExpCampoTipoExpedienteBean.T_NOMBRETABLA + 
        		" WHERE " + ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE + "=" +_idtipoexpediente +
        		" AND " + ExpCampoTipoExpedienteBean.C_IDINSTITUCION + "="+ _usr.getLocation(); 
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
    

    /** Funcion obtenerPestanasOcultas(String idInstitucion_TipoExpediente,String idTipoExpediente)
	 * Obtiene las pestanhas ocultas para un tipo de expediente
	 * @param idInstitucion_TipoExpediente
	 * @param idTipoExpediente
	 * @return String[] pestanhas ocultas
	 * */
 public String[] obtenerPestanasOcultas(String idInstitucion_TipoExpediente,String idTipoExpediente) throws ClsExceptions{
    	
    	//String[] resultado = null;
    	RowsContainer rc=new RowsContainer();

    	Hashtable codigos = new Hashtable();
    	codigos.put(new Integer(1),idInstitucion_TipoExpediente);
    	codigos.put(new Integer(2),idTipoExpediente);
    	codigos.put(new Integer(3),idInstitucion_TipoExpediente);
    	codigos.put(new Integer(4),idTipoExpediente);
        
    	String sql="SELECT P."+ ExpCamposExpedientesBean.C_PROCESO+ 
        		" FROM " + ExpCampoTipoExpedienteBean.T_NOMBRETABLA + " C, " + ExpCamposExpedientesBean.T_NOMBRETABLA + " P" +
        		" WHERE C." + ExpCampoTipoExpedienteBean.C_IDINSTITUCION + "=:1" +
        		" AND C." + ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE + "=:2" +
        		" AND C." + ExpCampoTipoExpedienteBean.C_VISIBLE + "='N'"+ 
        		" AND C." + ExpCampoTipoExpedienteBean.C_IDCAMPO + "=P."+ ExpCamposExpedientesBean.C_IDCAMPO +
        		" AND P." + ExpCamposExpedientesBean.C_TIPOCAMPO + "='P'" +
        		
        		" union " +
        		" SELECT P.PROCESO " +
        		" FROM EXP_PESTANACONF PC, EXP_CAMPOSEXPEDIENTES P " +
        		" WHERE PC.IDINSTITUCION =:3 " +
        		" AND PC.IDTIPOEXPEDIENTE =:4 " +
        		" AND PC.SELECCIONADO = 0 " +
        		" AND PC.IDCAMPO = P.IDCAMPO " +
        		" AND P.TIPOCAMPO = 'P'    ";
                
    	GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.usrbean);
 		String valor = parametrosAdm.getValor(this.usrbean.getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
 		
 		int tamanyo=0;	
 		int inicio=0;	
 		String[] resultado =null; 
        if (rc.queryBind(sql,codigos)) {
        	
        	if (valor!=null && valor.equals("0")){//Si el parametro REGTEL=0 (no se quiere conexion con DocuShare) entonces no 
        		                                  // permitimos visualizar la pestanya de RegTel
        		tamanyo=rc.size()+1;
        		resultado=new String[tamanyo];
        		inicio=1;
        		resultado[0]=ClsConstants.IDPROCESO_REGTEL_EXP;// proceso de la pestanya de RegTel
        	}else{
        		tamanyo=rc.size();
        		resultado=new String[tamanyo];
        		inicio=0;
        	}	
        	
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);															
				resultado[inicio] = fila.getString(ExpCamposExpedientesBean.C_PROCESO);
				inicio++;
			}
			
			return resultado;
		}else{
			if (valor!=null && valor.equals("0")){
				resultado=new String[1];
				resultado[0]=ClsConstants.IDPROCESO_REGTEL_EXP;
				return resultado;
			}else{
			   return null;
			}
		}
       
			
		
    }
 public String getNombrePestana(String idInstitucion_TipoExpediente,String idTipoExpediente, String campo) throws ClsExceptions{

	 String resultado = null;


	 Hashtable codigos = new Hashtable();
	 codigos.put(ExpCampoTipoExpedienteBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
	 codigos.put(ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
	 codigos.put(ExpCampoTipoExpedienteBean.C_IDCAMPO,campo);
	 Vector v = this.selectByPK(codigos);
	 ExpCampoTipoExpedienteBean bean = new ExpCampoTipoExpedienteBean();

	 if(v!=null &&  v.size()>0)
	 {
		 bean = (ExpCampoTipoExpedienteBean)v.get(0);
		 resultado = bean.getNombre();
	 }
	 return resultado;



 }
}
