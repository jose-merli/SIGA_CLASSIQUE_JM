package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DELITOSASISTENCIA
 * 
 * @author david.sanchezp
 * @since 25/01/2006
 */
public class ScsDelitosAsistenciaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDelitosAsistenciaAdm (UsrBean usuario) {
		super( ScsDelitosAsistenciaBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsDelitosAsistenciaBean.C_IDINSTITUCION, ScsDelitosAsistenciaBean.C_ANIO,
							ScsDelitosAsistenciaBean.C_NUMERO, ScsDelitosAsistenciaBean.C_IDDELITO,
							ScsDelitosAsistenciaBean.C_USUMODIFICACION, ScsDelitosAsistenciaBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsDelitosAsistenciaBean.C_IDINSTITUCION, ScsDelitosAsistenciaBean.C_ANIO,
							ScsDelitosAsistenciaBean.C_NUMERO, ScsDelitosAsistenciaBean.C_IDDELITO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDelitosAsistenciaBean bean = null;
		try{
			bean = new ScsDelitosAsistenciaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDelitosAsistenciaBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDelitosAsistenciaBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDelitosAsistenciaBean.C_NUMERO));			
			bean.setIdDelito(UtilidadesHash.getInteger(hash,ScsDelitosAsistenciaBean.C_IDDELITO));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsDelitosAsistenciaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDelitosAsistenciaBean.C_USUMODIFICACION));
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
			ScsDelitosAsistenciaBean b = (ScsDelitosAsistenciaBean) bean;
			hash.put(ScsDelitosAsistenciaBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsDelitosAsistenciaBean.C_ANIO, String.valueOf(b.getAnio()));
			hash.put(ScsDelitosAsistenciaBean.C_NUMERO, String.valueOf(b.getNumero()));			
			hash.put(ScsDelitosAsistenciaBean.C_IDDELITO, String.valueOf(b.getIdDelito()));

			hash.put(ScsDelitosAsistenciaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsDelitosAsistenciaBean.C_FECHAMODIFICACION, b.getFechaMod());
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
		String[] campos = {	ScsDelitosAsistenciaBean.C_IDINSTITUCION, ScsDelitosAsistenciaBean.C_ANIO,
				ScsDelitosAsistenciaBean.C_NUMERO, ScsDelitosAsistenciaBean.C_IDDELITO};
		return campos;
	}

	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
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
			throw new ClsExceptions (e, "Excepcion en ScsDelitosDesignaAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdDelitosAsistencia(Integer idInstitucion, Integer anio, Integer numero, Integer idTurno) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsDelitosAsistenciaBean.C_IDDELITO+")+1 AS ID FROM "+ScsDelitosAsistenciaBean.T_NOMBRETABLA+
					  " WHERE "+ScsDelitosAsistenciaBean.C_IDINSTITUCION+"="+idInstitucion+
					  " AND "+ScsDelitosAsistenciaBean.C_ANIO+"="+anio+
					  " AND "+ScsDelitosAsistenciaBean.C_NUMERO+"="+numero;
			
			datos = this.selectGenerico(select);
			String id = (String)((Hashtable)datos.get(0)).get("ID");
			
			if ( (datos == null) || (id!= null && id.equals("")) )
				nuevoId = new Integer("0");
			else
				nuevoId = new Integer(id);

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return nuevoId;
	}

	public Vector getDelitosAsitencia(Integer idInstitucion, Integer anio, Integer numero, String idioma) throws ClsExceptions {
		Vector vDelitos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT F_SIGA_GETRECURSO(delito." + ScsDelitoBean.C_DESCRIPCION + ", " + idioma  + ")" + ScsDelitoBean.C_DESCRIPCION +
					  " ,delito."+ScsDelitoBean.C_IDDELITO+
					  " FROM "+ScsDelitosAsistenciaBean.T_NOMBRETABLA+" delitoAsistencia"+
					  ","+ScsDelitoBean.T_NOMBRETABLA+" delito "+
					  " WHERE delitoAsistencia."+ScsDelitosAsistenciaBean.C_IDINSTITUCION+"="+idInstitucion+
					  " AND delitoAsistencia."+ScsDelitosAsistenciaBean.C_ANIO+"="+anio+
					  " AND delitoAsistencia."+ScsDelitosAsistenciaBean.C_NUMERO+"="+numero+
					  " AND delitoAsistencia."+ScsDelitosAsistenciaBean.C_IDDELITO+"=delito."+ScsDelitoBean.C_IDDELITO+
					  " AND delitoAsistencia."+ScsDelitosAsistenciaBean.C_IDINSTITUCION+"=delito."+ScsDelitoBean.C_IDINSTITUCION+
					  " ORDER BY delito."+ScsDelitoBean.C_DESCRIPCION;
			
			vDelitos = this.selectGenerico(select);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return vDelitos;
	}	
	public void borrarDelitosAsitencia(Integer idInstitucion, Integer anio, Integer numero) throws ClsExceptions {
		
		StringBuffer del = new StringBuffer();
		
		try {
			del.append("DELETE FROM ");
			del.append(ScsDelitosAsistenciaBean.T_NOMBRETABLA);
			del.append(" WHERE ");
			del.append(ScsDelitosAsistenciaBean.C_IDINSTITUCION);
			
			
			del.append("=");
			del.append(idInstitucion);
			del.append(" and ");
			del.append(ScsDelitosAsistenciaBean.C_ANIO);
			
			del.append(" = ");
			del.append(anio);
			del.append(" and ");
			del.append(ScsDelitosAsistenciaBean.C_NUMERO);
			
			del.append(" = ");
			del.append(numero);
			
			this.deleteSQL(del.toString());
		} 
		catch (ClsExceptions e) { 	
			throw (e); 
		}
		
	}
	
	
}