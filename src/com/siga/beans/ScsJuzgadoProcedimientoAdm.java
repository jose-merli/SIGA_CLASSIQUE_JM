package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_JUZGADOPROCEDIMIENTO
 * 
 * @author david.sanchezp
 * @since 08/02/2006
 */
public class ScsJuzgadoProcedimientoAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsJuzgadoProcedimientoAdm (UsrBean usuario) {
		super( ScsJuzgadoProcedimientoBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsJuzgadoProcedimientoBean.C_IDINSTITUCION, ScsJuzgadoProcedimientoBean.C_IDJUZGADO,	
							ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG, ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO,
							ScsJuzgadoProcedimientoBean.C_USUMODIFICACION, ScsJuzgadoProcedimientoBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsJuzgadoProcedimientoBean.C_IDINSTITUCION, ScsJuzgadoProcedimientoBean.C_IDJUZGADO,	
							ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG, ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsJuzgadoProcedimientoBean bean = null;
		try{
			bean = new ScsJuzgadoProcedimientoBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsJuzgadoProcedimientoBean.C_IDINSTITUCION));
			bean.setIdJuzgado(UtilidadesHash.getInteger(hash,ScsJuzgadoProcedimientoBean.C_IDJUZGADO));
			bean.setIdInstitucionJuzgado(UtilidadesHash.getInteger(hash,ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG));
			bean.setIdProcedimiento(UtilidadesHash.getString(hash,ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO));
			
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
			ScsJuzgadoProcedimientoBean b = (ScsJuzgadoProcedimientoBean) bean;
			hash.put(ScsJuzgadoProcedimientoBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsJuzgadoProcedimientoBean.C_IDJUZGADO, String.valueOf(b.getIdJuzgado()));
			hash.put(ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG, String.valueOf(b.getIdInstitucionJuzgado()));
			hash.put(ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO, String.valueOf(b.getIdProcedimiento()));

			hash.put(ScsJuzgadoProcedimientoBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsJuzgadoProcedimientoBean.C_FECHAMODIFICACION, b.getFechaMod());
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
		String[] campos = {	ScsJuzgadoProcedimientoBean.C_IDINSTITUCION, ScsJuzgadoProcedimientoBean.C_IDJUZGADO,	
				ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG, ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO};
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
	/** Funcion select (String where). Devuelve los campos: IDJUZGADO, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaProcedimientosJuzgado(String idInstitucion, String idJuzgado) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT procedimiento."+ScsProcedimientosBean.C_NOMBRE+
					  " , procedimiento."+ScsProcedimientosBean.C_PRECIO+	
					  " , procedimiento."+ScsProcedimientosBean.C_CODIGO+
					  " , juzgadoProc."+ScsJuzgadoProcedimientoBean.C_IDINSTITUCION+" AS IDINSTITUCION_PROC"+
					  " , juzgadoProc."+ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG+" AS IDINSTITUCION_JUZG"+
					  " , juzgadoProc."+ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO+
					  " , juzgadoProc."+ScsJuzgadoProcedimientoBean.C_IDJUZGADO+
					  " , (select f_siga_getrecurso("+ScsJurisdiccionBean.C_DESCRIPCION+",'1')"+
				      "    from "+ScsJurisdiccionBean.T_NOMBRETABLA+
				      "    where "+ScsJurisdiccionBean.C_IDJURISDICCION+"=procedimiento."+ScsProcedimientosBean.C_IDJURISDICCION+") as JURISDICCION";
			
			
			//FROM:
			select += " FROM "+ScsJuzgadoProcedimientoBean.T_NOMBRETABLA+" juzgadoProc,"+
						       ScsProcedimientosBean.T_NOMBRETABLA+" procedimiento";

			//Filtro:
			select += " WHERE juzgadoProc."+ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG+"="+idInstitucion+
					  " AND juzgadoProc."+ScsJuzgadoProcedimientoBean.C_IDJUZGADO+"="+idJuzgado+
					  " AND juzgadoProc."+ScsJuzgadoProcedimientoBean.C_IDINSTITUCION+"=procedimiento."+ScsProcedimientosBean.C_IDINSTITUCION+
					  " AND juzgadoProc."+ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO+"=procedimiento."+ScsProcedimientosBean.C_IDPROCEDIMIENTO;

			select +=" ORDER BY "+ScsProcedimientosBean.C_NOMBRE;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}	
	
	public Vector busquedaProcedimientosJuzgadoQueNoEstenEnJuzgado(String idInstitucion, String idJuzgado) throws ClsExceptions
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT " + ScsProcedimientosBean.C_NOMBRE + ", " +
								   ScsProcedimientosBean.C_IDINSTITUCION + ","  + 
								   ScsProcedimientosBean.C_IDPROCEDIMIENTO + 
					    " FROM " + ScsProcedimientosBean.T_NOMBRETABLA + 
					   " WHERE " + ScsProcedimientosBean.C_IDINSTITUCION + " = " + idInstitucion +  
					     " AND " + ScsProcedimientosBean.C_IDPROCEDIMIENTO + " NOT IN " +   
						 		" (SELECT idprocedimiento " +
								   " FROM " + ScsJuzgadoProcedimientoBean.T_NOMBRETABLA + 
								  " WHERE " + ScsJuzgadoProcedimientoBean.C_IDINSTITUCION + " = " + idInstitucion + 
								    " AND " + ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG + " = " + idInstitucion +
									" AND " + ScsJuzgadoProcedimientoBean.C_IDJUZGADO + " = " + idJuzgado + " ) " +
						" ORDER BY " + ScsProcedimientosBean.C_NOMBRE;

			datos = this.selectGenerico(select);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}	
	
}