/*
 * Created on Mar 15, 2005
 * @author jtacosta
*/
package com.siga.beans;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class EnvValorCampoClaveAdm extends MasterBeanAdministrador {


	public EnvValorCampoClaveAdm(UsrBean usuario)
	{
	    super(EnvValorCampoClaveBean.T_NOMBRETABLA, usuario);
	}

	
	
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
        		EnvValorCampoClaveBean.C_IDVALOR,
        		
        		EnvValorCampoClaveBean.C_IDINSTITUCION,
        		EnvValorCampoClaveBean.C_IDENVIO,
        		EnvValorCampoClaveBean.C_IDPROGRAM,
        		EnvValorCampoClaveBean.C_IDPERSONA,
        		EnvValorCampoClaveBean.C_IDINSTITUCION_PERSONA,
            	
                EnvValorCampoClaveBean.C_IDTIPOINFORME,
      		    EnvValorCampoClaveBean.C_CLAVE,
      		    
      		    EnvValorCampoClaveBean.C_CAMPO,
      		  	
      		    EnvValorCampoClaveBean.C_VALOR,
      		   
      		   
            	EnvValorCampoClaveBean.C_FECHAMODIFICACION,
            	EnvValorCampoClaveBean.C_USUMODIFICACION
				};
        
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {EnvValorCampoClaveBean.C_IDVALOR};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {EnvValorCampoClaveBean.C_IDPROGRAM};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvValorCampoClaveBean bean = null;

		try
		{
			bean = new EnvValorCampoClaveBean();
			bean.setIdValor(UtilidadesHash.getLong(hash, EnvValorCampoClaveBean.C_IDVALOR));
			
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvValorCampoClaveBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvValorCampoClaveBean.C_IDENVIO));
			bean.setIdProgram(UtilidadesHash.getInteger(hash, EnvValorCampoClaveBean.C_IDPROGRAM));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvValorCampoClaveBean.C_IDPERSONA));
			bean.setIdInstitucionPersona(UtilidadesHash.getInteger(hash, EnvValorCampoClaveBean.C_IDINSTITUCION_PERSONA));
			
			bean.setIdTipoInforme(UtilidadesHash.getString(hash, EnvValorCampoClaveBean.C_IDTIPOINFORME));
			bean.setClave(UtilidadesHash.getString(hash, EnvValorCampoClaveBean.C_CLAVE));
			
			bean.setCampo(UtilidadesHash.getString(hash, EnvValorCampoClaveBean.C_CAMPO));
			bean.setValor(UtilidadesHash.getString(hash, EnvValorCampoClaveBean.C_VALOR));
			
			
			

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

			EnvValorCampoClaveBean b = (EnvValorCampoClaveBean) bean;

			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_IDVALOR, b.getIdValor());
			
			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_IDPROGRAM, b.getIdProgram());
			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_IDINSTITUCION_PERSONA, b.getIdInstitucionPersona());

			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_IDTIPOINFORME, b.getIdTipoInforme());
			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_CLAVE, b.getClave());
			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_CAMPO, b.getCampo());
			
			UtilidadesHash.set(htData, EnvValorCampoClaveBean.C_VALOR, b.getValor());
			
			
			

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Long getNewIdEnvio() throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + EnvValorCampoClaveBean.C_IDVALOR +
        		") AS MAXVALOR FROM " + EnvValorCampoClaveBean.T_NOMBRETABLA ;
        		
        int valor=1; // Si no hay registros, es el valor que tomará
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será "" Si no hay registros
            if(!((String)htRow.get("MAXVALOR")).equals("")) {
            	Long valorInt=Long.valueOf((String)htRow.get("MAXVALOR"));
                valor=valorInt.intValue();
                valor++;
            }
        }
        return new Long(valor);
    }
    
    public List getValoresClavesCampos(EnvDestProgramInformesBean destInfGenerico)
	throws ClsExceptions {
		
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT VC.CLAVE,VC.CAMPO,VC.VALOR,VC.IDVALOR FROM ENV_VALORCAMPOCLAVE VC");
		
		
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		
		
		sql.append(" WHERE VC.IDPROGRAM = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), destInfGenerico.getIdProgram());
		sql.append(keyContador);
		sql.append(" AND VC.IDENVIO = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), destInfGenerico.getIdEnvio());
		sql.append(keyContador);
		sql.append(" AND VC.IDINSTITUCION = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), destInfGenerico.getIdInstitucion());
		sql.append(keyContador);
		sql.append(" AND VC.IDPERSONA = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), destInfGenerico.getIdPersona());
		sql.append(keyContador);
		sql.append(" AND VC.IDINSTITUCION_PERSONA = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), destInfGenerico.getIdInstitucionPersona());
		sql.append(keyContador);
	
	
	// Acceso a BBDD
	RowsContainer rc = null;
	List alValores = null;
	try {
		rc = new RowsContainer();
		if (rc.queryBind(sql.toString(),htCodigos)) {
			//Map valorCampoClave = null;
			
			Map htAcumulaValores = new Hashtable();
			Map htAuxValores = null;//new Hashtable();
			for (int i = 0; i < rc.size(); i++) {
				
				Row row = (Row) rc.get(i);
				Hashtable htFila = row.getRow(); 
				
				
//				valorCampoClave = new EnvValorCampoClaveBean();
//				valorCampoClave.setClaves(UtilidadesHash.getString(htFila, EnvValorCampoClaveBean.C_CLAVE));
//				valorCampoClave.setCampo(UtilidadesHash.getString(htFila, EnvValorCampoClaveBean.C_CAMPO));
//				valorCampoClave.setValor(UtilidadesHash.getString(htFila, EnvValorCampoClaveBean.C_VALOR));
//				valorCampoClave.setIdValor(UtilidadesHash.getLong(htFila, EnvValorCampoClaveBean.C_IDVALOR));
				Long idValor = (Long)UtilidadesHash.getLong(htFila, EnvValorCampoClaveBean.C_IDVALOR);
				//datos.add(destInfGenerico);
				if(!htAcumulaValores.containsKey(idValor)){
					htAuxValores = new Hashtable();
					
					
					
				}else{
					htAuxValores = (Hashtable)htAcumulaValores.get(idValor);
					
					
					
				}
				htAuxValores.put((String)htFila.get(EnvValorCampoClaveBean.C_CAMPO), (String)htFila.get(EnvValorCampoClaveBean.C_VALOR));
				htAcumulaValores.put(idValor,htAuxValores);
				//alValores.add(htAuxValores);
			}
			Collection al = (Collection) htAcumulaValores.values();
			if(al!=null && al.size()>0){
				alValores = new ArrayList();
				alValores.addAll(al);
			}
		}
	} catch (Exception e) {
		throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.getValoresClavesCampos");
	}
	return alValores;
}

  
   
   


}