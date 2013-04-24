/*
 * Created on Mar 15, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.gratuita.form.ConjuntoGuardiasForm;
import com.siga.gratuita.form.ProgrCalendariosForm;


public class EnvTipoEnviosAdm extends MasterBeanAdministrador {

	public static final String K_CORREO_ELECTRONICO = "1";
	public static final String K_CORREO_ORDINARIO = "2";
	public static final String K_FAX = "3";
	public static final String K_SMS = "4";
	public static final String K_BUROSMS = "5";
	public static final String K_ENVIOTELEMATICO = "6";
	static public final String CONS_TIPOENVIO="%%TIPOENVIO%%";
	
	public EnvTipoEnviosAdm(UsrBean usuario)
	{
	    super(EnvTipoEnviosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvTipoEnviosBean.C_NOMBRE,
            	EnvTipoEnviosBean.C_IDTIPOENVIOS,
            	EnvTipoEnviosBean.C_FECHAMODIFICACION,
            	EnvTipoEnviosBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvTipoEnviosBean.C_IDTIPOENVIOS};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvTipoEnviosBean.C_NOMBRE};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvTipoEnviosBean bean = null;

		try
		{
			bean = new EnvTipoEnviosBean();
			
			bean.setNombre(UtilidadesHash.getString(hash, EnvTipoEnviosBean.C_NOMBRE));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvTipoEnviosBean.C_IDTIPOENVIOS));
			
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

			EnvTipoEnviosBean b = (EnvTipoEnviosBean) bean;
			
			UtilidadesHash.set(htData, EnvTipoEnviosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvTipoEnviosBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdTIPOENVIOS(UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + EnvTipoEnviosBean.C_IDTIPOENVIOS + 
        		") AS MAXVALOR FROM " + EnvTipoEnviosBean.T_NOMBRETABLA;
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
   
    
	public List<EnvTipoEnviosBean> select(List<String> excluidosList, String idioma) throws ClsExceptions {
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT IDTIPOENVIOS,F_SIGA_GETRECURSO(NOMBRE,");
		sql.append(idioma);
		sql.append(" ) NOMBRE FROM ENV_TIPOENVIOS ");
		if(excluidosList!=null && excluidosList.size()>0){
			sql.append(" WHERE IDTIPOENVIOS NOT IN (");
			for (String idTipoEnvio : excluidosList) {
				sql.append(idTipoEnvio);
				sql.append(",");
			}
			//quitamos la coma
			sql.substring(0,sql.length()-1);
			sql.append(" )");
			
		}
		sql.append(" ORDER  BY IDTIPOENVIOS");
		List<EnvTipoEnviosBean> tipoEnviosBeans = null;
		
		RowsContainer rc = new RowsContainer(); 
		if (rc.find(sql.toString())) {
			tipoEnviosBeans = new ArrayList<EnvTipoEnviosBean>();
			
			EnvTipoEnviosBean tipoEnviosBean = null;
			ConjuntoGuardiasForm conjuntoGuardiaForm = null;
			for (int i = 0; i < rc.size(); i++){
				Row fila = (Row) rc.get(i);
				Hashtable<String, Object> htFila=fila.getRow();
				tipoEnviosBean =  (EnvTipoEnviosBean)this.hashTableToBean(htFila);
				
				tipoEnviosBeans.add(tipoEnviosBean);
				
			}
		}else{
			tipoEnviosBeans = new ArrayList<EnvTipoEnviosBean>();
		} 
		
 
		return tipoEnviosBeans;
	}
	public List<EnvTipoEnviosBean> getEnviosPermitidos(AdmInformeBean informeBean, UsrBean usrBean) throws ClsExceptions {
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT TE.IDTIPOENVIOS,F_SIGA_GETRECURSO(TE.NOMBRE,");
		sql.append(usrBean.getLanguage());
		sql.append(" ) NOMBRE,ei.idplantillaenviodef,ei.defecto FROM ENV_TIPOENVIOS TE,adm_envioinforme ei");
		sql.append(" where ei.idtipoenvios = te.idtipoenvios");
		sql.append(" AND  ei.idinstitucion =");
		sql.append(informeBean.getIdInstitucion());
		sql.append(" and ei.idplantilla =");
		sql.append(informeBean.getIdPlantilla());
		
		//mhg - INC_10698_SIGA Comprobamos el valor del parametro HABILITAR_SMS_BUROSMS y según su valor quitamos o ponemos los SMS en los envios.
		GenParametrosAdm param = new GenParametrosAdm(usrBean);
		boolean isEnvioSmsConfigurado = UtilidadesString.stringToBoolean(param.getValor(usrBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));
		if(!isEnvioSmsConfigurado){
			sql.append(" and te.idtipoenvios not in (4,5)");
		}
		sql.append(" ORDER  BY IDTIPOENVIOS");
		List<EnvTipoEnviosBean> tipoEnviosBeans = null;
		
		RowsContainer rc = new RowsContainer(); 
		if (rc.find(sql.toString())) {
			tipoEnviosBeans = new ArrayList<EnvTipoEnviosBean>();
			
			EnvTipoEnviosBean tipoEnviosBean = null;
			ConjuntoGuardiasForm conjuntoGuardiaForm = null;
			for (int i = 0; i < rc.size(); i++){
				Row fila = (Row) rc.get(i);
				Hashtable<String, Object> htFila=fila.getRow();
				tipoEnviosBean =  (EnvTipoEnviosBean)this.hashTableToBean(htFila);
				tipoEnviosBean.setIdPlantillaDefecto(UtilidadesHash.getString(htFila,"IDPLANTILLAENVIODEF"));
				tipoEnviosBean.setDefecto(UtilidadesHash.getString(htFila,"DEFECTO"));
				tipoEnviosBeans.add(tipoEnviosBean);
				
			}
		}else{
			tipoEnviosBeans = new ArrayList<EnvTipoEnviosBean>();
		} 
		
 
		return tipoEnviosBeans;
	}
	
	
}
