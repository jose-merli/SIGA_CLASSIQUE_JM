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
public class ScsGrupoGuardiaColegiadoAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor 
	 */
	public ScsGrupoGuardiaColegiadoAdm (UsrBean usuario) {
		super(ScsGrupoGuardiaColegiadoBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean()
	{
		String[] campos =
		{
				ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO,
				ScsGrupoGuardiaColegiadoBean.C_IDPERSONA,
				ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION,
				ScsGrupoGuardiaColegiadoBean.C_IDTURNO,
				ScsGrupoGuardiaColegiadoBean.C_IDGUARDIA,
				ScsGrupoGuardiaColegiadoBean.C_FECHASUSCRIPCION,
				ScsGrupoGuardiaColegiadoBean.C_IDGRUPO,
				ScsGrupoGuardiaColegiadoBean.C_ORDEN,
				ScsGrupoGuardiaColegiadoBean.C_FECHAMODIFICACION,
				ScsGrupoGuardiaColegiadoBean.C_USUMODIFICACION,
				ScsGrupoGuardiaColegiadoBean.C_FECHACREACION,
				ScsGrupoGuardiaColegiadoBean.C_USUCREACION
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
				ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO
		};
		return campos;
	}
	
	/**
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsGrupoGuardiaColegiadoBean bean = null;
		try{
			bean = new ScsGrupoGuardiaColegiadoBean();
			bean.setIdGrupoGuardiaColegiado(Long.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO)));
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION)));
			bean.setIdPersona(Long.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDPERSONA)));
			bean.setIdTurno(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDTURNO)));
			bean.setIdGuardia(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDGUARDIA)));
			bean.setFechaSuscripcion((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_FECHASUSCRIPCION));
			bean.setIdGrupo(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO)));
			bean.setOrden(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_ORDEN)));
			bean.setFechaCreacion((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_FECHACREACION));
			bean.setUsuCreacion(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_USUCREACION)));
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
			ScsGrupoGuardiaColegiadoBean b = (ScsGrupoGuardiaColegiadoBean) bean;
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO,String.valueOf(b.getIdGrupoGuardiaColegiado()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDPERSONA,String.valueOf(b.getIdPersona()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_FECHASUSCRIPCION, String.valueOf(b.getFechaSuscripcion()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO, String.valueOf(b.getIdGrupo()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_ORDEN, String.valueOf(b.getOrden()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_FECHACREACION, String.valueOf(b.getFechaCreacion()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_USUCREACION, String.valueOf(b.getUsuCreacion()));
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

}