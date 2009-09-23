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
import com.siga.Utilidades.UtilidadesHash;
import com.siga.gratuita.form.MantenimientoPrisionForm;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_PRISION
 * 
 * @author david.sanchezp
 * @since 20/01/2006
 */
public class ScsPrisionAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsPrisionAdm (UsrBean usuario) {
		super( ScsPrisionBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsPrisionBean.C_IDINSTITUCION, ScsPrisionBean.C_IDPRISION,	
							ScsPrisionBean.C_NOMBRE, ScsPrisionBean.C_DIRECCION, ScsPrisionBean.C_CODIGOEXT,
							ScsPrisionBean.C_CODIGOPOSTAL, ScsPrisionBean.C_IDPROVINCIA,
							ScsPrisionBean.C_IDPOBLACION, ScsPrisionBean.C_TELEFONO1,
							ScsPrisionBean.C_TELEFONO2, ScsPrisionBean.C_FAX1,
							ScsPrisionBean.C_USUMODIFICACION, ScsPrisionBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsPrisionBean.C_IDINSTITUCION, ScsPrisionBean.C_IDPRISION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsPrisionBean bean = null;
		try{
			bean = new ScsPrisionBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsPrisionBean.C_IDINSTITUCION));
			bean.setIdPrision(UtilidadesHash.getInteger(hash,ScsPrisionBean.C_IDPRISION));
			
			bean.setNombre(UtilidadesHash.getString(hash,ScsPrisionBean.C_NOMBRE));
			bean.setDireccion(UtilidadesHash.getString(hash,ScsPrisionBean.C_DIRECCION));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,ScsPrisionBean.C_CODIGOPOSTAL));
			bean.setCodigoExt(UtilidadesHash.getString(hash,ScsPrisionBean.C_CODIGOEXT));
			bean.setIdProvincia(UtilidadesHash.getString(hash,ScsPrisionBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,ScsPrisionBean.C_IDPOBLACION));
			bean.setTelefono1(UtilidadesHash.getString(hash,ScsPrisionBean.C_TELEFONO1));
			bean.setTelefono2(UtilidadesHash.getString(hash,ScsPrisionBean.C_TELEFONO2));
			bean.setFax1(UtilidadesHash.getString(hash,ScsPrisionBean.C_FAX1));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsPrisionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsPrisionBean.C_USUMODIFICACION));
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
			ScsPrisionBean b = (ScsPrisionBean) bean;
			hash.put(ScsPrisionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsPrisionBean.C_IDPRISION, String.valueOf(b.getIdPrision()));
			
			hash.put(ScsPrisionBean.C_NOMBRE, b.getNombre());
			hash.put(ScsPrisionBean.C_DIRECCION, b.getDireccion());
			hash.put(ScsPrisionBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			hash.put(ScsPrisionBean.C_CODIGOEXT, b.getCodigoExt());
			hash.put(ScsPrisionBean.C_IDPROVINCIA, b.getIdProvincia());
			hash.put(ScsPrisionBean.C_IDPOBLACION, b.getIdPoblacion());
			hash.put(ScsPrisionBean.C_TELEFONO1, b.getTelefono1());
			hash.put(ScsPrisionBean.C_TELEFONO2, b.getTelefono2());
			hash.put(ScsPrisionBean.C_FAX1, b.getFax1());			

			hash.put(ScsPrisionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsPrisionBean.C_FECHAMODIFICACION, b.getFechaMod());
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
		String[] vector = {ScsPrisionBean.C_IDPRISION,ScsPrisionBean.C_IDINSTITUCION};
		return vector;
	}
	
	/** Funcion getOrdenPorNombre ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenPorNombre() {		
		String[] vector = {"UPPER ("+ScsPrisionBean.T_NOMBRETABLA+"." + ScsPrisionBean.C_NOMBRE + ")"};
		return vector;
	}

	/** Funcion select (String where). Devuelve los campos: IDPRISION, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaPrisiones(MantenimientoPrisionForm miForm, String idInstitucion) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		String nombre="", idInstitucionPrision="", idPoblacion="", idProvincia="", codigoext="";
		
		try {
			nombre = miForm.getNombreBusqueda();
			idInstitucionPrision = miForm.getIdInstitucionPrision();
			idPoblacion = miForm.getIdPoblacion();
			idProvincia = miForm.getIdProvincia();
			codigoext=miForm.getCodigoExtBusqueda();
				
			select  =" SELECT i."+CenInstitucionBean.C_IDINSTITUCION;
			select += " , i."+CenInstitucionBean.C_ABREVIATURA+" INSTITUCION";
			select += " , prision."+ScsPrisionBean.C_NOMBRE;
			select += " , prision."+ScsPrisionBean.C_IDPRISION;
			select += " , prision."+ScsPrisionBean.C_CODIGOEXT;
			select += " , prision."+ScsPrisionBean.C_IDPROVINCIA;
			select += " , prision."+ScsPrisionBean.C_IDPOBLACION;
			select += " , prision."+ScsPrisionBean.C_DIRECCION;
			select += " , prision."+ScsPrisionBean.C_CODIGOPOSTAL;
			select += " , prision."+ScsPrisionBean.C_TELEFONO1;
			select += " , prision."+ScsPrisionBean.C_TELEFONO2;
			select += " , prision."+ScsPrisionBean.C_FAX1;
			select += " , (SELECT provincia."+CenProvinciaBean.C_NOMBRE+
					  "    FROM "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE provincia."+CenProvinciaBean.C_IDPROVINCIA+"=prision."+ScsPrisionBean.C_IDPROVINCIA+
					  ") AS PROVINCIA";
			select += " , (SELECT poblacion."+CenPoblacionesBean.C_NOMBRE+
					  "    FROM "+CenPoblacionesBean.T_NOMBRETABLA +" poblacion "+
					  "    ,    "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE poblacion."+CenPoblacionesBean.C_IDPROVINCIA+"=provincia."+CenProvinciaBean.C_IDPROVINCIA+					  
					  "    AND   prision."+ScsPrisionBean.C_IDPROVINCIA+"=poblacion."+CenPoblacionesBean.C_IDPROVINCIA+
					  "    AND   prision."+ScsPrisionBean.C_IDPOBLACION+"=poblacion."+CenPoblacionesBean.C_IDPOBLACION+
					  ") AS POBLACION";
			
			//FROM:
			select += " FROM "+ScsPrisionBean.T_NOMBRETABLA+" prision, ";
			select += CenInstitucionBean.T_NOMBRETABLA+" i ";
			
			//WHERE:
			if (idInstitucionPrision!=null && !idInstitucionPrision.equals(""))
				select += " WHERE prision."+ScsPrisionBean.C_IDINSTITUCION+"="+idInstitucionPrision+
						  " AND prision."+ScsPrisionBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			else {
				if(idInstitucion.equals("2000")){
					//salen las de todas las instituciones
					select += " WHERE prision."+ScsPrisionBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
				} else {
					//salen las de la 2000 y las propias
					select += " WHERE (prision."+ScsPrisionBean.C_IDINSTITUCION+"="+idInstitucion;
					select += "     OR prision."+ScsPrisionBean.C_IDINSTITUCION+"=2000)";
					select += "   AND prision."+ScsPrisionBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
				}
			}

			//Filtro por nombre:
			if (nombre !=null && !nombre.equals(""))				
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"prision."+ScsPrisionBean.C_NOMBRE);
			
			//Filtro por codigoExt:
			if (codigoext !=null && !codigoext.equals(""))				
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(codigoext.trim(),"prision."+ScsPrisionBean.C_CODIGOEXT);
			
			//Filtro por idPoblacion:
			if (idPoblacion !=null && !idPoblacion.equals(""))				
				select += " AND prision."+ScsPrisionBean.C_IDPOBLACION+"="+idPoblacion;
			
			//Filtro por idProvincia:
			if (idProvincia !=null && !idProvincia.equals(""))				
				select += " AND prision."+ScsPrisionBean.C_IDPROVINCIA+"="+idProvincia;			
			
			//ORDER BY:
			select += " ORDER BY prision."+ScsPrisionBean.C_IDINSTITUCION+",prision."+ScsPrisionBean.C_NOMBRE;
			
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
			throw new ClsExceptions (e, "Excepcion en ScsPrisionAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdPrision(String idInstitucion) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsPrisionBean.C_IDPRISION+")+1 AS ID FROM "+ScsPrisionBean.T_NOMBRETABLA+
					  " WHERE "+ScsPrisionBean.C_IDINSTITUCION+"="+idInstitucion;			
			
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
	
	/** Funcion select (String where). Devuelve los campos: IDPRISION, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaPrision(String idInstitucion, String idPrision) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT prision."+ScsPrisionBean.C_NOMBRE;
			select += " , prision."+ScsPrisionBean.C_IDPRISION;
			select += " , prision."+ScsPrisionBean.C_IDPROVINCIA;
			select += " , prision."+ScsPrisionBean.C_CODIGOEXT;
			select += " , prision."+ScsPrisionBean.C_IDPOBLACION;
			select += " , prision."+ScsPrisionBean.C_DIRECCION;
			select += " , prision."+ScsPrisionBean.C_CODIGOPOSTAL;
			select += " , prision."+ScsPrisionBean.C_TELEFONO1;
			select += " , prision."+ScsPrisionBean.C_TELEFONO2;
			select += " , prision."+ScsPrisionBean.C_FAX1;
			select += " , (SELECT provincia."+CenProvinciaBean.C_NOMBRE+
					  "    FROM "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE provincia."+CenProvinciaBean.C_IDPROVINCIA+"=prision."+ScsPrisionBean.C_IDPROVINCIA+
					  ") AS PROVINCIA";
			select += " , (SELECT poblacion."+CenPoblacionesBean.C_NOMBRE+
					  "    FROM "+CenPoblacionesBean.T_NOMBRETABLA +" poblacion "+
					  "    ,    "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE poblacion."+CenPoblacionesBean.C_IDPROVINCIA+"=provincia."+CenProvinciaBean.C_IDPROVINCIA+					  
					  "    AND   prision."+ScsPrisionBean.C_IDPROVINCIA+"=poblacion."+CenPoblacionesBean.C_IDPROVINCIA+
					  "    AND   prision."+ScsPrisionBean.C_IDPOBLACION+"=poblacion."+CenPoblacionesBean.C_IDPOBLACION+
					  ") AS POBLACION";
			
			//FROM:
			select += " FROM "+ScsPrisionBean.T_NOMBRETABLA+" prision";

			//Filtro:
			select += " WHERE prision."+ScsPrisionBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND prision."+ScsPrisionBean.C_IDPRISION+"="+idPrision;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	/**
	 * Comprueba si ya existe una prision con el mismo nombre
	 * Si la institución es el CGAE comprueba la duplicidad para todas las instituciones
	 * Si es otra, la comprueba para esa institución y para el CGAE
	 * @param idInstitucion
	 * @param nombre
	 * @return true si no existe ya en el sistema
	 * @throws ClsExceptions
	 */	
	public static boolean comprobarDuplicidad(String idInstitucion, String nombre)
	throws ClsExceptions{
		boolean sinDuplicar=true;
	    Hashtable codigos = new Hashtable();
	    codigos.put(new Integer(1),idInstitucion);
	    codigos.put(new Integer(2),nombre);
		String sql=
			"select pkg_siga_valida_mto_3.fun_scs_Prision(:1,:2) VALOR from DUAL";
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

	
	public boolean comprobarCodigoExt(String institucion, String idPrision, String codigoExt) throws ClsExceptions 
	{
		boolean salida=false;
		
		try {
			String sql=
				"select "+
				" j."+ScsPrisionBean.C_IDPRISION+" "+
				" from "+
				ScsPrisionBean.T_NOMBRETABLA+" j "+
				" where j."+ScsPrisionBean.C_CODIGOEXT+"='"+codigoExt + "'";
				
			if (!institucion.equals("2000")) {
				sql +=" and (j."+ScsPrisionBean.C_IDINSTITUCION+"="+institucion+" OR j."+ScsPrisionBean.C_IDINSTITUCION+"=2000)" ;
			}
			
			if (idPrision!=null && !idPrision.trim().equals("")) {
				sql+="  and j."+ScsPrisionBean.C_IDPRISION+"<>"+idPrision+"";
			}

			
			//Consulta:
			RowsContainer rc = this.find(sql);		
			if(rc!=null && rc.size()>0){
				salida=true;
			}
			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return salida;
	}

}