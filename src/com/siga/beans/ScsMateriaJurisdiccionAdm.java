package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_JUZGADOPROCEDIMIENTO
 * 
 * @author david.sanchezp
 * @since 08/02/2006
 */
public class ScsMateriaJurisdiccionAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsMateriaJurisdiccionAdm (UsrBean usuario) {
		super( ScsMateriaJurisdiccionBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsMateriaJurisdiccionBean.C_IDINSTITUCION, ScsMateriaJurisdiccionBean.C_IDAREA,	
				ScsMateriaJurisdiccionBean.C_IDMATERIA, ScsMateriaJurisdiccionBean.C_IDJURISDICCION,
				ScsMateriaJurisdiccionBean.C_USUMODIFICACION, ScsMateriaJurisdiccionBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsMateriaJurisdiccionBean.C_IDINSTITUCION, ScsMateriaJurisdiccionBean.C_IDAREA,	
				            ScsMateriaJurisdiccionBean.C_IDMATERIA, ScsMateriaJurisdiccionBean.C_IDJURISDICCION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsMateriaJurisdiccionBean bean = null;
		try{
			bean = new ScsMateriaJurisdiccionBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsMateriaJurisdiccionBean.C_IDINSTITUCION));
			bean.setIdArea(UtilidadesHash.getInteger(hash,ScsMateriaJurisdiccionBean.C_IDAREA));
			bean.setIdMateria(UtilidadesHash.getInteger(hash,ScsMateriaJurisdiccionBean.C_IDMATERIA));
			bean.setIdJurisdiccion(UtilidadesHash.getInteger(hash,ScsMateriaJurisdiccionBean.C_IDJURISDICCION));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsJuzgadoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsJuzgadoBean.C_USUMODIFICACION));
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
			ScsMateriaJurisdiccionBean b = (ScsMateriaJurisdiccionBean) bean;
			hash.put(ScsMateriaJurisdiccionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsMateriaJurisdiccionBean.C_IDAREA, String.valueOf(b.getIdArea()));
			hash.put(ScsMateriaJurisdiccionBean.C_IDMATERIA, String.valueOf(b.getIdMateria()));
			hash.put(ScsMateriaJurisdiccionBean.C_IDJURISDICCION, String.valueOf(b.getIdJurisdiccion()));

			hash.put(ScsMateriaJurisdiccionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsMateriaJurisdiccionBean.C_FECHAMODIFICACION, b.getFechaMod());
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
	public String[] getOrdenCampos() {
		String[] campos = {	ScsMateriaJurisdiccionBean.C_IDINSTITUCION, ScsMateriaJurisdiccionBean.C_IDAREA,	
				ScsMateriaJurisdiccionBean.C_IDMATERIA, ScsMateriaJurisdiccionBean.C_IDJURISDICCION};
		return campos;
	}
	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
/*	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsJuzgadoProcedimientoAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}		
*/

	
	public Vector busquedaJurisdiccionMateriaQueNoEstenEnMateria(Integer idMateria, Integer idArea,String idioma) throws ClsExceptions
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT"+UtilidadesMultidioma.getCampoMultidioma( ScsJurisdiccionBean.C_DESCRIPCION ,idioma)+", " +
			                       ScsJurisdiccionBean.C_IDJURISDICCION+ 
			                      
					    " FROM " + ScsJurisdiccionBean.T_NOMBRETABLA;
			
				select+= " WHERE " + ScsJurisdiccionBean.C_IDJURISDICCION + " NOT IN " +   
							 		" (SELECT "+ScsJurisdiccionBean.C_IDJURISDICCION+
									   " FROM " + ScsMateriaJurisdiccionBean.T_NOMBRETABLA + 
									  " WHERE " + ScsMateriaJurisdiccionBean.C_IDMATERIA+ " = " + idMateria +
									  "   and " + ScsMateriaJurisdiccionBean.C_IDAREA+ " = " + idArea+")";
			
					  
								    
			 select+=" ORDER BY " + ScsJurisdiccionBean.C_DESCRIPCION;

			datos = this.selectGenerico(select);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}	
	
	public Vector busquedaJurisdiccionMateria (String idArea,String institucion, String materia, String idioma) throws ClsExceptions{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT "+ ScsJurisdiccionBean.C_IDJURISDICCION+" IDJURISDICCION,"+
					  " (select "+UtilidadesMultidioma.getCampoMultidioma(ScsJurisdiccionBean.C_DESCRIPCION,idioma)+
					  "  from  "+ScsJurisdiccionBean.T_NOMBRETABLA+
					  "  where "+ScsJurisdiccionBean.T_NOMBRETABLA+"."+ScsJurisdiccionBean.C_IDJURISDICCION+"="+ScsMateriaJurisdiccionBean.T_NOMBRETABLA+"."+ScsMateriaJurisdiccionBean.C_IDJURISDICCION+") DESCRIPCION, "+
					  ScsMateriaJurisdiccionBean.C_IDINSTITUCION+" IDINSTITUCION, "+ScsMateriaJurisdiccionBean.C_IDAREA+" IDAREA, "+ScsMateriaJurisdiccionBean.C_IDMATERIA+" IDMATERIA"+
			                      
					   " FROM " + ScsMateriaJurisdiccionBean.T_NOMBRETABLA+
			 		   " WHERE " + ScsMateriaJurisdiccionBean.C_IDINSTITUCION+"=" +institucion+
					   "    and "+ ScsMateriaJurisdiccionBean.C_IDMATERIA+"="+materia+
					   "    and "+ ScsMateriaJurisdiccionBean.C_IDAREA+"="+idArea;
					   
					  
								    
			 select+=" ORDER BY 2 ASC" ;

			datos = this.selectGenerico(select);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	
}