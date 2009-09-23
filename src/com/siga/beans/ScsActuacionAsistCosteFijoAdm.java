package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author A203486/david.sanchezp
 * @since 05/04/2006  
 */
public class ScsActuacionAsistCosteFijoAdm extends MasterBeanAdministrador {
		
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsActuacionAsistCosteFijoAdm(UsrBean usuario) {
		super(ScsActuacionAsistCosteFijoBean.T_NOMBRETABLA, usuario);		
	}	
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	public String[] getCamposBean() {
		String[] campos =  {ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION, 
							ScsActuacionAsistCosteFijoBean.C_ANIO,
							ScsActuacionAsistCosteFijoBean.C_NUMERO,
							ScsActuacionAsistCosteFijoBean.C_IDACTUACION,
							ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION,
							ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA,
							ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO,
							ScsActuacionAsistCosteFijoBean.C_FACTURADO,
							ScsActuacionAsistCosteFijoBean.C_PAGADO,
							ScsActuacionAsistCosteFijoBean.C_FECHAMODIFICACION,
							ScsActuacionAsistCosteFijoBean.C_USUMODIFICACION
							};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	public String[] getClavesBean() {
		String[] campos= {	ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION, 
							ScsActuacionAsistCosteFijoBean.C_ANIO, 
							ScsActuacionAsistCosteFijoBean.C_NUMERO,
							ScsActuacionAsistCosteFijoBean.C_IDACTUACION,
							ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION,
							ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA,
							ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO};
		return campos;
	}
	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsActuacionAsistCosteFijoBean bean = null;
		
		try {
			bean = new ScsActuacionAsistCosteFijoBean();		
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsActuacionAsistCosteFijoBean.C_ANIO));				
			bean.setNumero(UtilidadesHash.getLong(hash,ScsActuacionAsistCosteFijoBean.C_NUMERO));				
			bean.setIdActuacion(UtilidadesHash.getLong(hash,ScsActuacionAsistCosteFijoBean.C_IDACTUACION));				
			bean.setIdTipoActuacion(UtilidadesHash.getInteger(hash,ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION));
			bean.setIdTipoAsistencia(UtilidadesHash.getInteger(hash,ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA));
			bean.setIdCosteFijo(UtilidadesHash.getInteger(hash,ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO));
			bean.setFacturado(UtilidadesHash.getString(hash,ScsActuacionAsistCosteFijoBean.C_FACTURADO));
			bean.setPagado(UtilidadesHash.getString(hash,ScsActuacionAsistCosteFijoBean.C_PAGADO));
			bean.setFechaMod(UtilidadesHash.getString (hash,ScsActuacionAsistCosteFijoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsActuacionAsistCosteFijoBean.C_USUMODIFICACION));
		} catch (Exception e){
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
			ScsActuacionAsistCosteFijoBean b = (ScsActuacionAsistCosteFijoBean) bean;
			htData.put(ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION             , String.valueOf(b.getIdInstitucion()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_ANIO                      , String.valueOf(b.getAnio()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_NUMERO                    , String.valueOf(b.getNumero()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_IDACTUACION               , String.valueOf(b.getIdActuacion()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION           , String.valueOf(b.getIdTipoActuacion()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA          , String.valueOf(b.getIdTipoAsistencia()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO			      , String.valueOf(b.getIdCosteFijo()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_FACTURADO		          , String.valueOf(b.getFacturado()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_PAGADO			          , String.valueOf(b.getPagado()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_USUMODIFICACION	  		  , String.valueOf(b.getUsuMod()));
			htData.put(ScsActuacionAsistCosteFijoBean.C_FECHAMODIFICACION		  , String.valueOf(b.getFechaMod()));
		} catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return htData;
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_ACTUACIONASISTENCIA con los datos introducidos. 
	 * 
	 * @param sql String  
	 * @return Vector de Hashtable con los registros que cumplan con la clausula where 
	 */
	public Vector select(String sql) throws ClsExceptions {
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
		} catch(ClsExceptions e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return v;
	}

	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenCampos() {
		String[] campos= {ScsActuacionAsistCosteFijoBean.C_IDCOSTEFIJO};	
		return campos;
	}
}