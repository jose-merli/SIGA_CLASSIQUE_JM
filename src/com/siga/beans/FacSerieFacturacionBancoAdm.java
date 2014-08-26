package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * Administrador de FAC_SUFIJO
 * @author david.sanchezp
 * @since 26-10-2005
 */
public class FacSerieFacturacionBancoAdm extends MasterBeanAdmVisible {

	
	/** 
	 * Constructor
	 * @param  usu - Usuario
	 */	
	public FacSerieFacturacionBancoAdm(UsrBean usu) {
		super(FacSerieFacturacionBancoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 * Devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */	
	protected String[] getCamposBean() {
		String [] campos = {FacSerieFacturacionBancoBean.C_IDINSTITUCION, 
							FacSerieFacturacionBancoBean.C_IDSERIEFACTURACION, 
							FacSerieFacturacionBancoBean.C_BANCOS_CODIGO, 
							FacSerieFacturacionBancoBean.C_IDSUFIJO,
							FacSerieFacturacionBancoBean.C_USUMODIFICACION, 
							FacSerieFacturacionBancoBean.C_FECHAMODIFICACION};
		return campos;
	}

	/** 
	 * Devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */	
	protected String[] getClavesBean() {
		String [] claves = {FacSerieFacturacionBancoBean.C_IDINSTITUCION, FacSerieFacturacionBancoBean.C_IDSERIEFACTURACION};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  FacSufijoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacSerieFacturacionBancoBean bean = null;
		
		try {
			bean = new FacSerieFacturacionBancoBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, FacSerieFacturacionBancoBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion(UtilidadesHash.getInteger(hash, FacSerieFacturacionBancoBean.C_IDSERIEFACTURACION));
			bean.setBancos_codigo(UtilidadesHash.getString(hash, FacSerieFacturacionBancoBean.C_BANCOS_CODIGO));
			bean.setIdSufijo(UtilidadesHash.getInteger(hash, FacSerieFacturacionBancoBean.C_IDSUFIJO));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacSerieFacturacionBancoBean b = (FacSerieFacturacionBancoBean) bean;
			htData.put(FacSerieFacturacionBancoBean.C_IDINSTITUCION,b.getIdInstitucion());
			htData.put(FacSerieFacturacionBancoBean.C_IDSERIEFACTURACION,b.getIdSerieFacturacion());
			htData.put(FacSerieFacturacionBancoBean.C_BANCOS_CODIGO,b.getBancos_codigo());	
			htData.put(FacSerieFacturacionBancoBean.C_IDSUFIJO,b.getIdSufijo());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		
		String [] orden = {FacSerieFacturacionBancoBean.C_IDINSTITUCION, FacSerieFacturacionBancoBean.C_IDSERIEFACTURACION,FacSerieFacturacionBancoBean.C_BANCOS_CODIGO};
		return orden;
	}
	
	/** 
	 * Obtiene la lista de bancos-sufijos asignados a las distintas series.
	 * @param  idInstitucion - identificador de la institucion 
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector getBancosSufijosSeries (Integer idInstitucion) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            
	    	   	RowsContainer rc = new RowsContainer(); 
	            String 	sql ="SELECT DISTINCT " +FacSerieFacturacionBancoBean.T_NOMBRETABLA + "." + FacSerieFacturacionBancoBean.C_IDSUFIJO;
	            		sql += ","+ FacSufijoBean.T_NOMBRETABLA+"."+FacSufijoBean.C_SUFIJO;
		    			sql += ","+FacBancoInstitucionBean.T_NOMBRETABLA + ".* ";
	            		sql += " FROM " + FacBancoInstitucionBean.T_NOMBRETABLA + ","+ FacSerieFacturacionBancoBean.T_NOMBRETABLA+","+FacSufijoBean.T_NOMBRETABLA;
		    			sql += " WHERE " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION + " = " +  FacSerieFacturacionBancoBean.T_NOMBRETABLA + "." + FacSerieFacturacionBancoBean.C_IDINSTITUCION;
		    			sql += " AND "+ FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_BANCOS_CODIGO + " = " +  FacSerieFacturacionBancoBean.T_NOMBRETABLA + "." + FacSerieFacturacionBancoBean.C_BANCOS_CODIGO;
		    			sql += " AND " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION + " = " + idInstitucion;
		    			sql += " AND " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_FECHABAJA + " IS NULL ";
		    			sql += " AND " + FacSufijoBean.T_NOMBRETABLA+"."+FacSufijoBean.C_IDSUFIJO + " = " + FacSerieFacturacionBancoBean.T_NOMBRETABLA + "." + FacSerieFacturacionBancoBean.C_IDSUFIJO;
		    			sql += " AND " + FacSufijoBean.T_NOMBRETABLA+"."+FacSufijoBean.C_IDINSTITUCION + " = " + FacSerieFacturacionBancoBean.T_NOMBRETABLA + "." + FacSerieFacturacionBancoBean.C_IDINSTITUCION;
	            					
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener los bancos-sufijo relacionados con las series.");
	       		}	
		   }
	       return datos;                        
	    }		

}