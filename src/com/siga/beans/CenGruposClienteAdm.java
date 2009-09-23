/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;


public class CenGruposClienteAdm extends MasterBeanAdministrador {
	
	public CenGruposClienteAdm (UsrBean usu) {
		super (CenGruposClienteBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {CenGruposClienteBean.C_IDGRUPO, 		
				    		CenGruposClienteBean.C_NOMBRE,
				    		CenGruposClienteBean.C_IDINSTITUCION,
							CenGruposClienteBean.C_FECHAMODIFICACION,
							CenGruposClienteBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenGruposClienteBean.C_IDGRUPO,
							CenGruposClienteBean.C_IDINSTITUCION};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenGruposClienteBean bean = null;
		
		try {
			bean = new CenGruposClienteBean();
			bean.setIdGrupo		  (UtilidadesHash.getInteger(hash, CenGruposClienteBean.C_IDGRUPO));
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash, CenGruposClienteBean.C_IDINSTITUCION));
			bean.setNombre		  (UtilidadesHash.getString(hash, CenGruposClienteBean.C_NOMBRE));
			bean.setFechaMod	  (UtilidadesHash.getString(hash, CenGruposClienteBean.C_FECHAMODIFICACION));
			bean.setUsuMod		  (UtilidadesHash.getInteger(hash, CenGruposClienteBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenGruposClienteBean b = (CenGruposClienteBean) bean;
			UtilidadesHash.set(htData, CenGruposClienteBean.C_IDGRUPO, b.getIdGrupo());
			UtilidadesHash.set(htData, CenGruposClienteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CenGruposClienteBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenGruposClienteBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenGruposClienteBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/** Funcion select (String where). Devuelve los campos: IDPROCURADOR, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Paginador busquedaGruposFijos(String nombre, String idInstitucion,String lenguaje) throws ClsExceptions 
	{
		/*Vector datos = new Vector();*/
		String select = null;
		Paginador paginador=null;
		
		try {
			select  =" SELECT i."+CenInstitucionBean.C_IDINSTITUCION;
			select += " , i."+CenInstitucionBean.C_ABREVIATURA+" INSTITUCION";
			select += " , F_SIGA_GETRECURSO(grupo."+CenGruposClienteBean.C_NOMBRE+", "+lenguaje+") "+CenGruposClienteBean.C_NOMBRE ;
			select += " , grupo."+CenGruposClienteBean.C_IDGRUPO;
			//FROM:
			select += " FROM "+CenGruposClienteBean.T_NOMBRETABLA+" grupo ,";
			select += CenInstitucionBean.T_NOMBRETABLA+" i ";
			
			//WHERE:
			if(idInstitucion.equals("2000")){
				//salen las de todas las instituciones
				select += " WHERE grupo."+CenGruposClienteBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			}else{
				//salen las de la 2000 y las propias
				
				
				
				select += " WHERE grupo.IDINSTITUCION IN (SELECT IDINSTITUCION" +
						" FROM CEN_INSTITUCION START WITH IDINSTITUCION =" +idInstitucion;
				select += " CONNECT BY PRIOR  CEN_INST_IDINSTITUCION = IDINSTITUCION)";
				select += " AND grupo."+CenGruposClienteBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			}
			
			//Filtro del nombre:
			if (nombre !=null && !nombre.equals(""))
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"F_SIGA_GETRECURSO(grupo."+CenGruposClienteBean.C_NOMBRE+", "+lenguaje+")" ) ;

			
			
			//ORDER BY:
			select += " ORDER BY grupo."+CenGruposClienteBean.C_IDINSTITUCION+",grupo."+CenGruposClienteBean.C_NOMBRE;
			
			//Consulta:
			//datos = this.selectGenerico(select);
			/*** PAGINACION ***/ 
	        paginador = new Paginador(select);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}else{
				int registrosPorPagina = paginador.getNumeroRegistrosPorPagina();	    		
	    		Vector datos = paginador.obtenerPagina(1);
	    	
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return paginador;
	}	

	/** Funcion select (String where). Devuelve los campos: IDPROCURADOR, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaGrupofijo(String idInstitucion, String idGrupo,String lenguaje) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT ";
			select += " F_SIGA_GETRECURSO(grupo."+CenGruposClienteBean.C_NOMBRE+", "+lenguaje+") "+CenGruposClienteBean.C_NOMBRE ;
			
			select += " , grupo."+CenGruposClienteBean.C_IDGRUPO;
			
			//FROM:
			select += " FROM "+CenGruposClienteBean.T_NOMBRETABLA+" grupo";

			//Filtro:
			select += " WHERE grupo."+CenGruposClienteBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND grupo."+CenGruposClienteBean.C_IDGRUPO+"="+idGrupo;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
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
			if (rc.queryNLS(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenGruposClientesAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	/**
	 * Comprueba si ya existe un procurador con el mismo nombre y apellidos
	 * Si la institución es el CGAE comprueba la duplicidad para todas las instituciones
	 * Si es otra, la comprueba para esa institución y para el CGAE
	 * @param idInstitucion
	 * @param nombre
	 * @param apellido1
	 * @param apellido2
	 * @return true si no existe ya en el sistema
	 * @throws ClsExceptions
	 */	
	public static boolean comprobarDuplicidad(String idInstitucion, String nombre)
	throws ClsExceptions{
		boolean sinDuplicar=true;
		Vector existe=null;
	    Hashtable codigos = new Hashtable();
	    codigos.put(new Integer(1),idInstitucion);
	    codigos.put(new Integer(2),nombre);
		String sql=
			"select pkg_siga_valida_mto_3.FUN_SCS_GRUPOS(:1,:2) VALOR from DUAL";
			
		RowsContainer rc= new RowsContainer();
		if(rc.findBind(sql,codigos)){
		    Vector v = rc.getAll();
		    if (v!=null&&v.size()>0) { 
		        sinDuplicar=(ClsConstants.DB_TRUE.equals((String)((Row)v.get(0)).getString("VALOR")));
		    }
		}else{
			ClsLogging.writeFileLog(sql, 10);
			throw new ClsExceptions("error al acceder a la función de validación");
		}
		return sinDuplicar;
	}
	
	public Integer getNuevoIdGrupo(String idInstitucion) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+CenGruposClienteBean.C_IDGRUPO+")+1 AS ID FROM "+CenGruposClienteBean.T_NOMBRETABLA+
					  " WHERE "+CenGruposClienteBean.C_IDINSTITUCION+"="+idInstitucion;			
			
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
}
