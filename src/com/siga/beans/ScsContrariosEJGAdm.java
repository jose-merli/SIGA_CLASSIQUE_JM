/*
 * VERSIONES:
 * 
 * miguel.villegas - 26-05-2005 - Creacion
 * 					Implementa las operaciones sobre la base de datos, es decir: select, insert, update... 
 * 					a la tabla SCS_CONTRARIOSEJG
 *	
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

public class ScsContrariosEJGAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsContrariosEJGAdm (UsrBean usuario) {
		super( ScsContrariosEJGBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsContrariosEJGBean.C_IDINSTITUCION,			ScsContrariosEJGBean.C_IDTIPOEJG,
							ScsContrariosEJGBean.C_ANIO,					ScsContrariosEJGBean.C_NUMERO,
							ScsContrariosEJGBean.C_FECHAMODIFICACION,		ScsContrariosEJGBean.C_USUMODIFICACION,
							ScsContrariosEJGBean.C_IDPERSONA,				ScsContrariosEJGBean.C_OBSERVACIONES,
							ScsContrariosEJGBean.C_IDINSTITUCION_PROCU,		ScsContrariosEJGBean.C_IDPROCURADOR};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsContrariosEJGBean.C_IDINSTITUCION,			ScsContrariosEJGBean.C_IDTIPOEJG,
							ScsContrariosEJGBean.C_ANIO,					ScsContrariosEJGBean.C_NUMERO,
							ScsContrariosEJGBean.C_IDPERSONA};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsContrariosEJGBean bean = null;
		try{
			bean = new ScsContrariosEJGBean();
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsContrariosEJGBean.C_ANIO));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsContrariosEJGBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsContrariosEJGBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsContrariosEJGBean.C_IDPERSONA));
			bean.setIdTipoEJG(UtilidadesHash.getInteger(hash,ScsContrariosEJGBean.C_IDTIPOEJG));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsContrariosEJGBean.C_NUMERO));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsContrariosEJGBean.C_OBSERVACIONES));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsContrariosEJGBean.C_USUMODIFICACION));
			bean.setIdProcurador(UtilidadesHash.getLong(hash,ScsContrariosEJGBean.C_IDPROCURADOR));
			bean.setIdInstitucionProcurador(UtilidadesHash.getInteger(hash,ScsContrariosEJGBean.C_IDINSTITUCION_PROCU));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsContrariosEJGBean b = (ScsContrariosEJGBean) bean;
			UtilidadesHash.set(hash, ScsContrariosEJGBean.C_ANIO,String.valueOf(b.getAnio()));
			UtilidadesHash.set(hash, ScsContrariosEJGBean.C_FECHAMODIFICACION,String.valueOf(b.getFechaMod()));
			UtilidadesHash.set(hash, ScsContrariosEJGBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsContrariosEJGBean.C_IDTIPOEJG,String.valueOf(b.getIdTipoEJG()));
			UtilidadesHash.set(hash, ScsContrariosEJGBean.C_NUMERO,String.valueOf(b.getNumero()));
			try{
				UtilidadesHash.set(hash, ScsContrariosEJGBean.C_OBSERVACIONES,String.valueOf(b.getObservaciones()));
			}catch(Exception e){
				UtilidadesHash.set(hash, ScsContrariosEJGBean.C_OBSERVACIONES,"");
			}
			try{
				UtilidadesHash.set(hash, ScsContrariosEJGBean.C_IDPERSONA,String.valueOf(b.getIdPersona()));
			}catch(Exception e){
				UtilidadesHash.set(hash, ScsContrariosEJGBean.C_IDPERSONA,"");
			}
			UtilidadesHash.set(hash, ScsContrariosEJGBean.C_USUMODIFICACION,String.valueOf(b.getUsuMod()));
			UtilidadesHash.set(hash, ScsContrariosEJGBean.C_IDINSTITUCION_PROCU,String.valueOf(b.getIdInstitucionProcurador()));
			UtilidadesHash.set(hash, ScsContrariosEJGBean.C_IDPROCURADOR,String.valueOf(b.getIdProcurador()));
																		
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
	
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	/** 
	 * Recoge informacion sobre los contrarios 
	 * @param  institucion - identificador de la institucion
	 * @param  tipoEJG - tipo de EJG
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Datos contrarios  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosContrariosEJG (String institucion, String tipoEJG, String anio, String numero) throws ClsExceptions,SIGAException {
		   Vector vContrarios=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT p." + ScsPersonaJGBean.C_NIF+" NIF, "+
			    			"(p." +  ScsPersonaJGBean.C_NOMBRE+"||' '|| p."+ ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || p." +
			    			 ScsPersonaJGBean.C_APELLIDO2 +") AS DATOS_CONTRARIO, " +
							"(select p1."+ ScsPersonaJGBean.C_NOMBRE+"||' '||p1."+ScsPersonaJGBean.C_APELLIDO1+"||' '||p1."+ScsPersonaJGBean.C_APELLIDO2+
							" from " +ScsPersonaJGBean.T_NOMBRETABLA+" p1 "+
							" where p1."+ ScsPersonaJGBean.C_IDPERSONA+"=p."+ ScsPersonaJGBean.C_IDREPRESENTANTEJG+
							" and p1."+ ScsPersonaJGBean.C_IDINSTITUCION+"=p."+ ScsPersonaJGBean.C_IDINSTITUCION+") REPRESENTANTE, "+
							" p."+ScsPersonaJGBean.C_IDPERSONA+" IDPERSONA "+
			    			
							" FROM " + ScsContrariosEJGBean.T_NOMBRETABLA + "," + ScsPersonaJGBean.T_NOMBRETABLA +" p "+ 
							" WHERE " +
							ScsContrariosEJGBean.T_NOMBRETABLA +"."+ ScsContrariosEJGBean.C_IDINSTITUCION + "=p." + ScsPersonaJGBean.C_IDINSTITUCION +
							" AND " +
							ScsContrariosEJGBean.T_NOMBRETABLA +"."+ ScsContrariosEJGBean.C_IDPERSONA + "=p." +  ScsPersonaJGBean.C_IDPERSONA +
							" AND " +
							ScsContrariosEJGBean.T_NOMBRETABLA +"."+ ScsContrariosEJGBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsContrariosEJGBean.T_NOMBRETABLA +"."+ ScsContrariosEJGBean.C_IDTIPOEJG + "=" + tipoEJG +
							" AND " +
							ScsContrariosEJGBean.T_NOMBRETABLA +"."+ ScsContrariosEJGBean.C_ANIO + "=" + anio +
							" AND " +
							ScsContrariosEJGBean.T_NOMBRETABLA +"."+ ScsContrariosEJGBean.C_NUMERO + "=" + numero;
	            vContrarios=this.selectGenerico(sql);
	            
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las actuaciosnes de una designa.");
	       }
	       return vContrarios;                        
	    }
	
	
}
		
	
		
