package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;

// Clase: ScsRetencionesIRPFAdm 
// Autor: carlos.vidal@atosorigin.com
// Ultima modificación: 20/12/2004
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...  
 */
public class ScsRetencionesIRPFAdm extends MasterBeanAdministrador {
		
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsRetencionesIRPFAdm(UsrBean usuario) {
		super(ScsRetencionesIRPFBean.T_NOMBRETABLA, usuario);		
	}
	
    /**
	 * Efectúa un SELECT en la tabla SCS_RETENCIONESIRPF con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector();		
				
		try { 			 		
			String where = " WHERE " + ScsRetencionesIRPFBean.C_IDINSTITUCION+ " = " + hash.get(ScsRetencionesIRPFBean.C_IDINSTITUCION)+ "AND "+
					ScsRetencionesIRPFBean.C_IDPERSONA + " = " + hash.get(ScsRetencionesIRPFBean.C_IDPERSONA)+ "AND "+
					ScsRetencionesIRPFBean.C_IDRETENCION + " = " + hash.get(ScsRetencionesIRPFBean.C_IDRETENCION)+ "AND "+
					ScsRetencionesIRPFBean.C_FECHAINICIO + " = " + hash.get(ScsRetencionesIRPFBean.C_FECHAINICIO);
			datos = this.select(where);
		} 
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}
	
	
	/**
	 * Efectúa un DELETE en la tabla SCS_RETENCIONESIRPF del registro seleccionado 
	 * 
	 * @param hash Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return boleano que indica si la inserción fue correcta o no. 
	 */
	public boolean delete(Hashtable hash) throws ClsExceptions{
	
		try {
			Row row = new Row();	
			row.load(hash);
	
			String [] campos = this.getClavesBean();
			
			if (row.delete(this.nombreTabla, campos) == 1) {
				return true;
			}		
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BORRADO");
		}
		return false;
	}	
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	public String[] getCamposBean() {
		String[] campos= {	ScsRetencionesIRPFBean.C_IDINSTITUCION, 
				ScsRetencionesIRPFBean.C_IDPERSONA, 
				ScsRetencionesIRPFBean.C_IDRETENCION,
				ScsRetencionesIRPFBean.C_FECHAINICIO,
				ScsRetencionesIRPFBean.C_FECHAFIN,
				ScsRetencionesIRPFBean.C_FECHAMODIFICACION,
				ScsRetencionesIRPFBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	public String[] getClavesBean() {
		String[] campos= {	ScsRetencionesIRPFBean.C_IDINSTITUCION, 
				ScsRetencionesIRPFBean.C_IDPERSONA, 
				ScsRetencionesIRPFBean.C_IDRETENCION,
				ScsRetencionesIRPFBean.C_FECHAINICIO};
		return campos;
	}
	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsRetencionesIRPFBean bean = null;
		
		try {
			bean = new ScsRetencionesIRPFBean();		
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsRetencionesIRPFBean.C_IDINSTITUCION));		
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsRetencionesIRPFBean.C_IDPERSONA));				
			bean.setIdRetencion(UtilidadesHash.getInteger(hash,ScsRetencionesIRPFBean.C_IDRETENCION));
			bean.setFechaInicio(UtilidadesHash.getString(hash,ScsRetencionesIRPFBean.C_FECHAINICIO));				
			bean.setFechaFin(UtilidadesHash.getString(hash,ScsRetencionesIRPFBean.C_FECHAFIN));				
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsRetencionesIRPFBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsRetencionesIRPFBean.C_USUMODIFICACION));
			
		}		
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR HASHTABLE A BEAN");
		}		
		return bean;
	
	}
	
	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsRetencionesIRPFBean b = (ScsRetencionesIRPFBean) bean;
			htData.put(ScsRetencionesIRPFBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			htData.put(ScsRetencionesIRPFBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			htData.put(ScsRetencionesIRPFBean.C_IDRETENCION, String.valueOf(b.getIdRetencion()));
			htData.put(ScsRetencionesIRPFBean.C_FECHAINICIO, b.getFechaInicion());
			htData.put(ScsRetencionesIRPFBean.C_FECHAFIN, b.getFechaFin());
			htData.put(ScsRetencionesIRPFBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(ScsRetencionesIRPFBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return htData;
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_RETENCIONESIRPF con los datos introducidos. 
	 * 
	 * @param where String la clausula where del select 
	 * @return Vector de Hashtable con los registros que cumplan con la clausula where 
	 */
	public Vector selectTabla(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(ScsRetencionesIRPFBean.T_NOMBRETABLA, this.getCamposSelect());
			sql += where;
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_RETENCIONESIRPF con los datos introducidos. 
	 * 
	 * @param sql String  
	 * @return Vector de Hashtable con los registros que cumplan con la clausula where 
	 */
	public Vector select(String sql){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}

	/**
	 * Devuelve los campos de en la consulta
	 * @param entrada indicador del permiso del usuario logado
	 * @return String con los campos del select
	 */
	
	protected String[] getCamposSelect(){
			String[] campos = {	"IDINSTITUCION","IDPERSONA","IDRETENCION","FECHAINICIO","FECHAFIN","FECHAMODIFICACION","USUMODIFICACION" };
			return campos;
	}

	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenCampos() {
		
		String[] campos= {	ScsRetencionesBean.C_RETENCION};	
		return campos;
	}
}