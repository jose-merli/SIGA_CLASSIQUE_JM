
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
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_CONTRARIOSASISTENCIA
 * 
 * @author Luis Miguel Sánchez PIÑA
 * @version David Sanchez Pina: modificaion del tipo de 2 campos de tipo number(10) en base de datos.
 * @since 20/02/2006 
 */
public class ScsContrariosAsistenciaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsContrariosAsistenciaAdm (UsrBean usuario) {
		super( ScsContrariosAsistenciaBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsContrariosAsistenciaBean.C_IDINSTITUCION,
							ScsContrariosAsistenciaBean.C_NUMERO,
							ScsContrariosAsistenciaBean.C_ANIO,					
							ScsContrariosAsistenciaBean.C_IDPERSONA,
							ScsContrariosAsistenciaBean.C_OBSERVACIONES,
							ScsContrariosAsistenciaBean.C_FECHAMODIFICACION,
							ScsContrariosAsistenciaBean.C_USUMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsContrariosAsistenciaBean.C_IDINSTITUCION,
							ScsContrariosAsistenciaBean.C_ANIO,
							ScsContrariosAsistenciaBean.C_NUMERO,
							ScsContrariosAsistenciaBean.C_IDPERSONA};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsContrariosAsistenciaBean bean = new ScsContrariosAsistenciaBean();;
		try{
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsContrariosAsistenciaBean.C_IDINSTITUCION));
			bean.setNumero(UtilidadesHash.getLong(hash,ScsContrariosAsistenciaBean.C_NUMERO));
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsContrariosAsistenciaBean.C_ANIO));
			bean.setIdPersona(UtilidadesHash.getLong(hash,ScsContrariosAsistenciaBean.C_IDPERSONA));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsContrariosAsistenciaBean.C_OBSERVACIONES));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsContrariosAsistenciaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsContrariosAsistenciaBean.C_USUMODIFICACION));			
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
			ScsContrariosAsistenciaBean b = (ScsContrariosAsistenciaBean) bean;
			UtilidadesHash.set(hash, ScsContrariosAsistenciaBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsContrariosAsistenciaBean.C_NUMERO,String.valueOf(b.getNumero()));
			UtilidadesHash.set(hash, ScsContrariosAsistenciaBean.C_ANIO,String.valueOf(b.getAnio()));
			UtilidadesHash.set(hash, ScsContrariosAsistenciaBean.C_IDPERSONA,String.valueOf(b.getIdPersona()));
			UtilidadesHash.set(hash, ScsContrariosAsistenciaBean.C_OBSERVACIONES,String.valueOf(b.getObservaciones()));
			UtilidadesHash.set(hash, ScsContrariosAsistenciaBean.C_FECHAMODIFICACION,String.valueOf(b.getFechaMod()));
			UtilidadesHash.set(hash, ScsContrariosAsistenciaBean.C_USUMODIFICACION,String.valueOf(b.getUsuMod()));
		}
		catch (Exception e){
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return null;
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
	 * Recoge los identificadores de las personas de la parte contraria 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  turno - identificador del turno
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getContrarios(String institucion, String anio, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
				    			ScsContrariosAsistenciaBean.T_NOMBRETABLA + "." + ScsContrariosAsistenciaBean.C_IDINSTITUCION + "," +
				    			ScsContrariosAsistenciaBean.T_NOMBRETABLA + "." + ScsContrariosAsistenciaBean.C_ANIO + "," +
				    			ScsContrariosAsistenciaBean.T_NOMBRETABLA + "." + ScsContrariosAsistenciaBean.C_NUMERO + "," +
				    			ScsContrariosAsistenciaBean.T_NOMBRETABLA + "." + ScsContrariosAsistenciaBean.C_IDPERSONA + "," +
				    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NIF + " AS NIF," +
				    			"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NOMBRE + " || ' ' || " +
				    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || " +
				    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO2 + ") AS NOMBRE " +
							" FROM " + ScsContrariosAsistenciaBean.T_NOMBRETABLA +","+ScsPersonaJGBean.T_NOMBRETABLA+							
							" WHERE "+ScsContrariosAsistenciaBean.T_NOMBRETABLA +"."+ ScsContrariosAsistenciaBean.C_IDINSTITUCION + "=" + institucion +
							" AND "+ScsContrariosAsistenciaBean.T_NOMBRETABLA +"."+ ScsContrariosAsistenciaBean.C_ANIO + "=" + anio +
							" AND "+ScsContrariosAsistenciaBean.T_NOMBRETABLA +"."+ ScsContrariosAsistenciaBean.C_NUMERO + "=" + numero+	            
	            			" AND "+ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION+"="+ScsContrariosAsistenciaBean.T_NOMBRETABLA+"."+ScsContrariosAsistenciaBean.C_IDINSTITUCION+
	            			" AND "+ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA+"="+ScsContrariosAsistenciaBean.T_NOMBRETABLA+"."+ScsContrariosAsistenciaBean.C_IDPERSONA;
	            			
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre la parte contraria de una asistencia.");
	       }
	       return datos;                        
	    }
	/** 
	 * Recoge los identificadores de las personas de la parte contraria 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  anio - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getListaContrarios(Integer institucion, Integer anio, Integer numero) throws ClsExceptions {

		Vector datos=new Vector();
	       try {
	            	            
	    	   String sql =
	    		   "select "+
	    		   "p.nif  NIF_CONTRARIO, "+
	    		   "p.apellido1||' '||p.apellido2||', '||p.nombre NOMBRE_CONTRARIO "+
	    		   "from scs_contrariosAsistencia ca, scs_personajg p "+
	    		   "where ca.idpersona=p.idpersona(+)"+
	    		   "  and ca.idinstitucion=p.idinstitucion(+)"+
	    		   "  and ca.idinstitucion="+institucion+
	    		   "  and ca.anio="+anio+
	    		   "  and ca.numero="+numero+
	    		   " order by NOMBRE_CONTRARIO";
	            	
	            RowsContainer rc = new RowsContainer(); 
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre la parte contraria de una designa.");
	       }
	       return datos;                        
	    }
		
}