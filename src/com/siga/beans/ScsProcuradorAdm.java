package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_PROCURADOR
 * 
 * @author david.sanchezp
 * @since 17/01/2006
 */
public class ScsProcuradorAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsProcuradorAdm (UsrBean usuario) {
		super( ScsProcuradorBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	
				ScsProcuradorBean.C_IDINSTITUCION, 		ScsProcuradorBean.C_IDPROCURADOR,	
				ScsProcuradorBean.C_NCOLEGIADO,			ScsProcuradorBean.C_NOMBRE,
				ScsProcuradorBean.C_APELLIDO1,			ScsProcuradorBean.C_APELLIDO2,
				ScsProcuradorBean.C_DIRECCION,			ScsProcuradorBean.C_CODIGOPOSTAL, 
				ScsProcuradorBean.C_IDPROVINCIA,		ScsProcuradorBean.C_IDPOBLACION, 
				ScsProcuradorBean.C_TELEFONO1,			ScsProcuradorBean.C_TELEFONO2, 
				ScsProcuradorBean.C_FAX1,				ScsProcuradorBean.C_EMAIL,
				ScsProcuradorBean.C_USUMODIFICACION,	ScsProcuradorBean.C_FECHAMODIFICACION,
				ScsProcuradorBean.C_CODPROCURADOR,		ScsProcuradorBean.C_IDCOLPROCURADOR
		};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsProcuradorBean.C_IDINSTITUCION, ScsProcuradorBean.C_IDPROCURADOR};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsProcuradorBean bean = null;
		try{
			bean = new ScsProcuradorBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsProcuradorBean.C_IDINSTITUCION));
			bean.setIdProcurador(UtilidadesHash.getInteger(hash,ScsProcuradorBean.C_IDPROCURADOR));
			bean.setNColegiado(UtilidadesHash.getString(hash,ScsProcuradorBean.C_NCOLEGIADO));
			bean.setIdColProcurador(UtilidadesHash.getString(hash,ScsProcuradorBean.C_IDCOLPROCURADOR));
			
			bean.setNombre(UtilidadesHash.getString(hash,ScsProcuradorBean.C_NOMBRE));
			bean.setApellido1(UtilidadesHash.getString(hash,ScsProcuradorBean.C_APELLIDO1));
			bean.setApellido2(UtilidadesHash.getString(hash,ScsProcuradorBean.C_APELLIDO2));
			bean.setDireccion(UtilidadesHash.getString(hash,ScsProcuradorBean.C_DIRECCION));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,ScsProcuradorBean.C_CODIGOPOSTAL));
			bean.setIdProvincia(UtilidadesHash.getString(hash,ScsProcuradorBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,ScsProcuradorBean.C_IDPOBLACION));
			bean.setTelefono1(UtilidadesHash.getString(hash,ScsProcuradorBean.C_TELEFONO1));
			bean.setTelefono2(UtilidadesHash.getString(hash,ScsProcuradorBean.C_TELEFONO2));
			bean.setEmail(UtilidadesHash.getString(hash,ScsProcuradorBean.C_EMAIL));
			bean.setFax1(UtilidadesHash.getString(hash,ScsProcuradorBean.C_FAX1));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsProcuradorBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsProcuradorBean.C_USUMODIFICACION));
			bean.setCodProcurador(UtilidadesHash.getString(hash,ScsProcuradorBean.C_CODPROCURADOR));
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
			ScsProcuradorBean b = (ScsProcuradorBean) bean;
			hash.put(ScsProcuradorBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsProcuradorBean.C_IDPROCURADOR, String.valueOf(b.getIdProcurador()));
			hash.put(ScsProcuradorBean.C_NCOLEGIADO, b.getNColegiado());
			hash.put(ScsProcuradorBean.C_IDCOLPROCURADOR, b.getIdColProcurador());
			hash.put(ScsProcuradorBean.C_NOMBRE, b.getNombre());
			hash.put(ScsProcuradorBean.C_APELLIDO1, b.getApellido1());
			hash.put(ScsProcuradorBean.C_APELLIDO2, b.getApellido2());
			hash.put(ScsProcuradorBean.C_DIRECCION, b.getDireccion());
			hash.put(ScsProcuradorBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			hash.put(ScsProcuradorBean.C_IDPROVINCIA, b.getIdProvincia());
			hash.put(ScsProcuradorBean.C_IDPOBLACION, b.getIdPoblacion());
			hash.put(ScsProcuradorBean.C_TELEFONO1, b.getTelefono1());
			hash.put(ScsProcuradorBean.C_TELEFONO2, b.getTelefono2());
			hash.put(ScsProcuradorBean.C_EMAIL, b.getEmail());
			hash.put(ScsProcuradorBean.C_FAX1, b.getFax1());			
			hash.put(ScsProcuradorBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsProcuradorBean.C_FECHAMODIFICACION, b.getFechaMod());
			hash.put(ScsProcuradorBean.C_CODPROCURADOR, String.valueOf(b.getCodProcurador()));
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
		String[] vector = {ScsProcuradorBean.C_IDPROCURADOR,ScsProcuradorBean.C_IDINSTITUCION};
		return vector;
	}
	
	/** Funcion getOrdenPorNombre ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenPorNombre() {		
		String[] vector = {"UPPER ("+ScsProcuradorBean.T_NOMBRETABLA+"." + ScsProcuradorBean.C_NOMBRE + ")"};
		return vector;
	}

	/** Funcion select (String where). Devuelve los campos: IDPROCURADOR, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaProcuradores(String nombre, String apellido1, String apellido2, String idInstitucion, String codProcurador) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  =" SELECT i."+CenInstitucionBean.C_IDINSTITUCION;
			select += " , i."+CenInstitucionBean.C_ABREVIATURA+" INSTITUCION";
			select += " , procurador."+ScsProcuradorBean.C_NOMBRE;
			select += " , procurador."+ScsProcuradorBean.C_APELLIDO1;
			select += " , procurador."+ScsProcuradorBean.C_APELLIDO2;
			select += " , procurador."+ScsProcuradorBean.C_IDPROCURADOR;
			select += " , procurador."+ScsProcuradorBean.C_IDPROVINCIA;
			select += " , procurador."+ScsProcuradorBean.C_IDPOBLACION;
			select += " , procurador."+ScsProcuradorBean.C_DIRECCION;
			select += " , procurador."+ScsProcuradorBean.C_CODIGOPOSTAL;
			select += " , procurador."+ScsProcuradorBean.C_TELEFONO1;
			select += " , procurador."+ScsProcuradorBean.C_TELEFONO2;
			select += " , procurador."+ScsProcuradorBean.C_FAX1;
			select += " , procurador."+ScsProcuradorBean.C_EMAIL;
			select += " , procurador."+ScsProcuradorBean.C_CODPROCURADOR;
			select += " , (SELECT provincia."+CenProvinciaBean.C_NOMBRE+
					  "    FROM "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE provincia."+CenProvinciaBean.C_IDPROVINCIA+"=procurador."+ScsProcuradorBean.C_IDPROVINCIA+
					  ") AS PROVINCIA";
			select += " , (SELECT poblacion."+CenPoblacionesBean.C_NOMBRE+
					  "    FROM "+CenPoblacionesBean.T_NOMBRETABLA +" poblacion "+
					  "    ,    "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE poblacion."+CenPoblacionesBean.C_IDPROVINCIA+"=provincia."+CenProvinciaBean.C_IDPROVINCIA+					  
					  "    AND   procurador."+ScsProcuradorBean.C_IDPROVINCIA+"=poblacion."+CenPoblacionesBean.C_IDPROVINCIA+
					  "    AND   procurador."+ScsProcuradorBean.C_IDPOBLACION+"=poblacion."+CenPoblacionesBean.C_IDPOBLACION+
					  ") AS POBLACION";
			
			//FROM:
			select += " FROM "+ScsProcuradorBean.T_NOMBRETABLA+" procurador ,";
			select += CenInstitucionBean.T_NOMBRETABLA+" i ";
			
			//WHERE:
			if(idInstitucion.equals("2000")){
				//salen las de todas las instituciones
				select += " WHERE procurador."+ScsProcuradorBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			}else{
				//salen las de la 2000 y las propias
				select += " WHERE (procurador."+ScsProcuradorBean.C_IDINSTITUCION+"="+idInstitucion;
				select += "     OR procurador."+ScsProcuradorBean.C_IDINSTITUCION+"=2000)";
				select += "   AND procurador."+ScsProcuradorBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			}
			
//			Filtro del codigo del procurador:
			if (codProcurador !=null && !codProcurador.equals(""))
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(codProcurador.trim(),"procurador."+ScsProcuradorBean.C_CODPROCURADOR);
			//Filtro del nombre:
			if (nombre !=null && !nombre.equals(""))
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"procurador."+ScsProcuradorBean.C_NOMBRE);

			//Filtro del apellido1:
			if (apellido1 !=null && !apellido1.equals(""))
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(),"procurador."+ScsProcuradorBean.C_APELLIDO1);

			//Filtro del apellido2:
			if (apellido2 !=null && !apellido2.equals(""))
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido2.trim(),"procurador."+ScsProcuradorBean.C_APELLIDO2);
			
			//ORDER BY:
			select += " ORDER BY INSTITUCION, procurador."+ScsProcuradorBean.C_APELLIDO1+", procurador."+ScsProcuradorBean.C_APELLIDO2+", procurador."+ScsProcuradorBean.C_NOMBRE;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
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
			throw new ClsExceptions (e, "Excepcion en ScsProcuradorAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdProcurador(String idInstitucion) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsProcuradorBean.C_IDPROCURADOR+")+1 AS ID FROM "+ScsProcuradorBean.T_NOMBRETABLA+
					  " WHERE "+ScsProcuradorBean.C_IDINSTITUCION+"="+idInstitucion;			
			
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
	
	/** Funcion select (String where). Devuelve los campos: IDPROCURADOR, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaProcurador(String idInstitucion, String idProcurador) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT procurador."+ScsProcuradorBean.C_NOMBRE;
			select += " , procurador."+ScsProcuradorBean.C_NCOLEGIADO;
			select += " , procurador."+ScsProcuradorBean.C_IDCOLPROCURADOR;
			select += " , procurador."+ScsProcuradorBean.C_APELLIDO1;
			select += " , procurador."+ScsProcuradorBean.C_APELLIDO2;
			select += " , procurador."+ScsProcuradorBean.C_IDPROCURADOR;
			select += " , procurador."+ScsProcuradorBean.C_IDPROVINCIA;
			select += " , procurador."+ScsProcuradorBean.C_IDPOBLACION;
			select += " , procurador."+ScsProcuradorBean.C_DIRECCION;
			select += " , procurador."+ScsProcuradorBean.C_CODIGOPOSTAL;
			select += " , procurador."+ScsProcuradorBean.C_TELEFONO1;
			select += " , procurador."+ScsProcuradorBean.C_TELEFONO2;
			select += " , procurador."+ScsProcuradorBean.C_FAX1;
			select += " , procurador."+ScsProcuradorBean.C_EMAIL;
			select += " , procurador."+ScsProcuradorBean.C_CODPROCURADOR;
			select += " , (SELECT provincia."+CenProvinciaBean.C_NOMBRE+
					  "    FROM "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE provincia."+CenProvinciaBean.C_IDPROVINCIA+"=procurador."+ScsProcuradorBean.C_IDPROVINCIA+
					  ") AS PROVINCIA";
			select += " , (SELECT poblacion."+CenPoblacionesBean.C_NOMBRE+
					  "    FROM "+CenPoblacionesBean.T_NOMBRETABLA +" poblacion "+
					  "    ,    "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE poblacion."+CenPoblacionesBean.C_IDPROVINCIA+"=provincia."+CenProvinciaBean.C_IDPROVINCIA+					  
					  "    AND   procurador."+ScsProcuradorBean.C_IDPROVINCIA+"=poblacion."+CenPoblacionesBean.C_IDPROVINCIA+
					  "    AND   procurador."+ScsProcuradorBean.C_IDPOBLACION+"=poblacion."+CenPoblacionesBean.C_IDPOBLACION+
					  ") AS POBLACION";
			
			//FROM:
			select += " FROM "+ScsProcuradorBean.T_NOMBRETABLA+" procurador";

			//Filtro:
			select += " WHERE procurador."+ScsProcuradorBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND procurador."+ScsProcuradorBean.C_IDPROCURADOR+"="+idProcurador;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
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
	public static boolean comprobarDuplicidad(String idInstitucion, String nombre, String apellido1, String apellido2)
	throws ClsExceptions{
		boolean sinDuplicar=true;
		Hashtable codigos = new Hashtable();
	    codigos.put(new Integer(1),idInstitucion);
	    codigos.put(new Integer(2),nombre);
	    codigos.put(new Integer(3),apellido1);
	    codigos.put(new Integer(4),apellido2);
	    
		String sql=
			"select pkg_siga_valida_mto_3.fun_scs_Procurador(:1,:2,:3,:4) VALOR from DUAL";
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
	
	
	
	
	public Paginador getProcuradoresModal (String idInstitucion, String nombre, String apellido1, String apellido2, String nColegiado, String codigo, boolean bBusqueda) throws ClsExceptions, SIGAException 
	{
		Vector salida = null;
	  	
		RowsContainer rcClientes = null;
		try { 
			String sql = "";
			
			sql += ScsProcuradorBean.C_IDINSTITUCION  + " IN (" + ClsConstants.INSTITUCION_CGAE;
			if (idInstitucion!=null && !idInstitucion.trim().equals("")) {
				sql += ", " + idInstitucion; 
			}
			sql +=  " ) ";
			
			if (nColegiado!=null && !nColegiado.trim().equals("")) {
				if (bBusqueda) {
					sql += " AND (" + ComodinBusquedas.tratarNumeroColegiado(nColegiado,ScsProcuradorBean.C_NCOLEGIADO) + " ) ";
				}
				else{
					sql += " AND (" + ComodinBusquedas.tratarNumeroColegiadoAproximado(nColegiado.trim(),ScsProcuradorBean.C_NCOLEGIADO) + " ) ";
				}
			}
			
			if (nombre != null && !nombre.trim().equals("")) {
		       	if (bBusqueda) {
		       		sql += " AND (" + ScsProcuradorBean.C_NOMBRE + " " + ComodinBusquedas.prepararSentenciaExacta(nombre.trim())+") ";
		       	}
		       	else{
		       		sql += " AND (" + ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(), ScsProcuradorBean.C_NOMBRE) + ") ";
		       	}
			}
			  
			if (apellido1 != null && !apellido1.trim().equals("")) {
		       	if (bBusqueda) {
		       		sql += " AND (" + ScsProcuradorBean.C_APELLIDO1 + " " + ComodinBusquedas.prepararSentenciaExacta(apellido1.trim())+") ";
		       	}
		       	else{
		       		sql += " AND (" + ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(), ScsProcuradorBean.C_APELLIDO1) + ") ";
		       	}
			}
				
			if (apellido2!= null && !apellido2.trim().equals("")) {
		       	if (bBusqueda) {
		       		sql += " AND (" + ScsProcuradorBean.C_APELLIDO2 + " " + ComodinBusquedas.prepararSentenciaExacta(apellido2.trim())+") ";
		       	}
		       	else{
		       		sql += " AND (" + ComodinBusquedas.prepararSentenciaCompleta(apellido2.trim(), ScsProcuradorBean.C_APELLIDO2) + ") ";
		       	}
			}
			
			if (codigo != null && !codigo.trim().equals("")) {
		       	if (bBusqueda) {
		       		sql += " AND (" + ScsProcuradorBean.C_CODPROCURADOR + " " + ComodinBusquedas.prepararSentenciaExacta(codigo.trim())+") ";
		       	}
		       	else{
		       		sql += " AND (" + ComodinBusquedas.prepararSentenciaCompleta(codigo.trim(), ScsProcuradorBean.C_CODPROCURADOR) + ") ";
		       	}
			}
				
			sql = UtilidadesBDAdm.sqlSelect(ScsProcuradorBean.T_NOMBRETABLA, this.getCamposBean()) +
				  (sql.equals("")?"": " WHERE " + sql) + " ORDER BY " + ScsProcuradorBean.C_APELLIDO1+", "+ScsProcuradorBean.C_APELLIDO2+", "+ScsProcuradorBean.C_NOMBRE;
	       
	       Paginador paginador = new Paginador(sql);				
		   int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			else{
				int registrosPorPagina = paginador.getNumeroRegistrosPorPagina();	    		
	    		Vector datos = paginador.obtenerPagina(1);
			}
			return paginador;
		} 
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes "); 
		}
	}
	
	
	
	
	
}