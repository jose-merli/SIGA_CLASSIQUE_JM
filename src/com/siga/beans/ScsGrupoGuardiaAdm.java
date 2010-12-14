package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

/**
 * Implementa las operaciones sobre la base de datos a la tabla SCS_INSCRIPCIONGUARDIA
 */
public class ScsGrupoGuardiaAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor 
	 */
	public ScsGrupoGuardiaAdm (UsrBean usuario) {
		super(ScsGrupoGuardiaBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean()
	{
		String[] campos =
		{
				ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA,
				ScsGrupoGuardiaBean.C_IDINSTITUCION,
				ScsGrupoGuardiaBean.C_IDTURNO,
				ScsGrupoGuardiaBean.C_IDGUARDIA,
				ScsGrupoGuardiaBean.C_NUMEROGRUPO,
				ScsGrupoGuardiaBean.C_FECHAMODIFICACION,
				ScsGrupoGuardiaBean.C_USUMODIFICACION,
				ScsGrupoGuardiaBean.C_FECHACREACION,
				ScsGrupoGuardiaBean.C_USUCREACION
		};
		return campos;
	}
	
	/**
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos =
		{
				ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA
		};
		return campos;
	}
	
	/**
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsGrupoGuardiaBean bean = null;
		try{
			bean = new ScsGrupoGuardiaBean();
			bean.setIdGrupoGuardia(Long.valueOf((String)hash.get(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA)));
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsGrupoGuardiaBean.C_IDINSTITUCION)));
			bean.setIdTurno(Integer.valueOf((String)hash.get(ScsGrupoGuardiaBean.C_IDTURNO)));
			bean.setIdGuardia(Integer.valueOf((String)hash.get(ScsGrupoGuardiaBean.C_IDGUARDIA)));
			bean.setNumeroGrupo((String)hash.get(ScsGrupoGuardiaBean.C_NUMEROGRUPO));
			bean.setFechaCreacion((String)hash.get(ScsGrupoGuardiaBean.C_FECHACREACION));
			bean.setUsuCreacion(Integer.valueOf((String)hash.get(ScsGrupoGuardiaBean.C_USUCREACION)));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}
	
	/**
	 * Funcion beanToHashTable (MasterBean bean)
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsGrupoGuardiaBean b = (ScsGrupoGuardiaBean) bean;
			hash.put(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA,String.valueOf(b.getIdGrupoGuardia()));
			hash.put(ScsGrupoGuardiaBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			hash.put(ScsGrupoGuardiaBean.C_NUMEROGRUPO,String.valueOf(b.getNumeroGrupo()));
			hash.put(ScsGrupoGuardiaBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsGrupoGuardiaBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsGrupoGuardiaBean.C_FECHACREACION, String.valueOf(b.getFechaCreacion()));
			hash.put(ScsGrupoGuardiaBean.C_USUCREACION, String.valueOf(b.getUsuCreacion()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 * Funcion getOrdenCampos ()
	 * @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 * que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return null;
	}
	
	/**
	 * Funcion ejecutaSelect(String select)
	 * @param select sentencia "select" sql valida, sin terminar en ";"
	 * @return Vector todos los registros que se seleccionen 
	 * en BBDD debido a la ejecucion de la sentencia select
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


	public Hashtable getGrupoGuardia(String idInstitucion, String idTurno, String idGuardia, String numeroGrupo) throws ClsExceptions{
		Hashtable hash = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select idgrupoguardia, idinstitucion, idturno, idguardia, ");
		sql.append(" numerogrupo, fechacreacion, usucreacion, fechamodificacion, usumodificacion ");
		sql.append(" from scs_grupoguardia ");
		sql.append(" where idinstitucion="+idInstitucion+" and idturno ="+idTurno+" and idguardia="+idGuardia+" and numerogrupo="+numeroGrupo);
		RowsContainer rc = new RowsContainer(); 
		try {
			if (rc.query(sql.toString()) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable) fila.getRow(); 
				if (registro != null) 
					hash = (Hashtable) registro.clone();
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar la posicion del ultimo letrado");
		}
		return hash;
	}
	
	public boolean getNuevoIdGrupo(String idInstitucion, String idTurno, String idGuardia, String numeroGrupo) throws ClsExceptions{
		Hashtable hash = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select idgrupoguardia, idinstitucion, idturno, idguardia, ");
		sql.append(" numerogrupo, fechacreacion, usucreacion, fechamodificacion, usumodificacion ");
		sql.append(" from scs_grupoguardia ");
		sql.append(" where idinstitucion="+idInstitucion+" and idturno ="+idTurno+" and idguardia="+idGuardia+" and numerogrupo="+numeroGrupo);
		RowsContainer rc = new RowsContainer(); 
		try {
			if (rc.query(sql.toString()) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable) fila.getRow(); 
				if (registro != null) 
					hash = (Hashtable) registro.clone();
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar la posicion del ultimo letrado");
		}
		return true;
	}

}