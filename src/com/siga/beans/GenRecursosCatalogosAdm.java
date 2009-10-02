
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MultidiomaGenCatalogosMultidioma;


/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla GEN_RECURSOS_CATALOGOS
 */
public class GenRecursosCatalogosAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public GenRecursosCatalogosAdm (UsrBean usuario) {
		super( GenRecursosCatalogosBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposTabla ()
	 * @return String campos que recupera desde la select
	 */
	protected String[] getCamposBean(){
		String[] campos = { GenRecursosCatalogosBean.C_CAMPOTABLA, 			GenRecursosCatalogosBean.C_DESCRIPCION,
							GenRecursosCatalogosBean.C_FECHAMODIFICACION, 	GenRecursosCatalogosBean.C_IDINSTITUCION,
							GenRecursosCatalogosBean.C_IDLENGUAJE,			GenRecursosCatalogosBean.C_IDRECURSO,
							GenRecursosCatalogosBean.C_NOMBRETABLA, 		GenRecursosCatalogosBean.C_USUMODIFICACION,
							GenRecursosCatalogosBean.C_IDRECURSOALIAS};
		return campos;
	}

	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos = {	GenRecursosCatalogosBean.C_IDRECURSO, GenRecursosCatalogosBean.C_IDLENGUAJE};
		return campos;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return this.getClavesBean();
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		GenRecursosCatalogosBean bean = null;
		try {
			bean = new GenRecursosCatalogosBean();
			bean.setCampoTabla(UtilidadesHash.getString(hash, GenRecursosCatalogosBean.C_CAMPOTABLA));
			bean.setDescripcion(UtilidadesHash.getString(hash, GenRecursosCatalogosBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash, GenRecursosCatalogosBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, GenRecursosCatalogosBean.C_IDINSTITUCION));
			bean.setIdLenguaje(UtilidadesHash.getString(hash, GenRecursosCatalogosBean.C_IDLENGUAJE));
			bean.setIdRecurso(UtilidadesHash.getString(hash, GenRecursosCatalogosBean.C_IDRECURSO));
			bean.setIdRecursoAlias(UtilidadesHash.getString(hash, GenRecursosCatalogosBean.C_IDRECURSOALIAS));
			bean.setNombreTabla(UtilidadesHash.getString(hash, GenRecursosCatalogosBean.C_NOMBRETABLA));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, GenRecursosCatalogosBean.C_USUMODIFICACION));
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
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try
		{
			hash = new Hashtable();
			GenRecursosCatalogosBean b = (GenRecursosCatalogosBean) bean;
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_CAMPOTABLA,	b.getCampoTabla());
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_IDLENGUAJE, b.getIdLenguaje());
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_IDRECURSO, b.getIdRecurso());
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_IDRECURSOALIAS, b.getIdRecursoAlias());
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_NOMBRETABLA, b.getNombreTabla());
			UtilidadesHash.set(hash, GenRecursosCatalogosBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	public static String getNombreIdRecurso (String tabla, String campo, Integer idInstitucion, String idCodigo) 
	{
		try {
			if (idInstitucion == null) 
				idInstitucion = new Integer(0);
			
			return  "" + MultidiomaGenCatalogosMultidioma.getCodigo(tabla, campo) + idInstitucion + idCodigo;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static String getNombreIdRecursoAlias (String tabla, String campo, Integer idInstitucion, String idCodigo) 
	{
		if (idInstitucion == null) 
			idInstitucion = new Integer(0);
		
		return (tabla + "." + campo + "." + idInstitucion + "." + idCodigo).toLowerCase();
	}
	
	private Vector getLenguajes () 
	{
		Vector v = new Vector();
		try {
			AdmLenguajesAdm adm = new AdmLenguajesAdm (this.usrbean);
			v = adm.select();
		}
		catch (Exception e) {
		}
		return v;
	}
	
	//******************************
	// Hay que usar el insert(Hashtable o GenRecursosCatalogosBean, UsrBean) para insertar
	// 4 registros por registro (1 por idioma)
	public boolean insert(Hashtable bean) throws ClsExceptions {
		return false;
	}
	public boolean insert(MasterBean bean) throws ClsExceptions {
		return false;
	}
	public boolean insertSQL(String sql) throws ClsExceptions {
		return false;
	}
	//******************************
	
	public boolean insert(Hashtable hash, String idiomaInstitucion) throws ClsExceptions
	{
		return this.insert ((GenRecursosCatalogosBean)this.hashTableToBean(hash), idiomaInstitucion);
	}
	
	public boolean insert(GenRecursosCatalogosBean bean, String idiomaInstitucion) throws ClsExceptions 
	{
		// Hacemos un insert por cada uno de los lenguajes que estan dados de alta en el sistema 
		String  descripcion = bean.getDescripcion();

		Vector vLenguajes = this.getLenguajes();
		for (int i = 0; i < vLenguajes.size(); i++) {
			
			AdmLenguajesBean len = (AdmLenguajesBean)vLenguajes.get(i);
			
			if (!idiomaInstitucion.equalsIgnoreCase(len.getIdLenguaje())) {
				bean.setDescripcion(descripcion + "#" + len.getCodigoExt().toUpperCase());
			}
			else {
				bean.setDescripcion(descripcion);
			}
			bean.setIdLenguaje(len.getIdLenguaje());
		
			if (!this.insertGenericoRecursosCatalogos(bean)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean insertGenericoRecursosCatalogos(GenRecursosCatalogosBean bean) throws ClsExceptions {
		return this.insertGenericoRecursosCatalogos (this.beanToHashTable(bean));
	}
	
	private boolean insertGenericoRecursosCatalogos(Hashtable hash) throws ClsExceptions
	{
		try
		{
			// Establecemos las datos de insercion
			UtilidadesHash.set(hash, MasterBean.C_USUMODIFICACION, this.usuModificacion); 
			UtilidadesHash.set(hash, MasterBean.C_FECHAMODIFICACION, "sysdate");

			Row row = new Row();
			row.load(hash);

			//String [] campos = this.getCamposBean();
			String [] campos = this.getCamposActualizablesBean();
			
			if (row.add(this.nombreTabla, campos) == 1)
				return true;
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el \"insert\" en B.D");
		}
		return false;
	}

	
	public boolean delete(Hashtable hash) throws ClsExceptions 
	{
		// Borramos todos los registros, independientemente del idioma
		hash.remove(GenRecursosCatalogosBean.C_IDINSTITUCION);
		String campos [] = {GenRecursosCatalogosBean.C_IDRECURSO};
		return super.deleteDirect(hash, campos);
	}
	
	//******************************
	// Hay que usar el insert(Hashtable o GenRecursosCatalogosBean, UsrBean) para insertar
	// 4 registros por registro (1 por idioma)
	public boolean update(Hashtable hashDataNew, Hashtable hashDataOld) throws ClsExceptions 			{ return false; }
	public boolean update(MasterBean beanNew, MasterBean beanOld) throws ClsExceptions 					{ return false; }
	public boolean update(MasterBean bean) throws ClsExceptions 										{ return false; }
	public boolean updateDirect(Hashtable hash, String[] claves, String[] campos) throws ClsExceptions 	{ return false; }
	public boolean updateDirect(MasterBean bean) throws ClsExceptions 									{ return false; }
	//******************************

	public boolean update(GenRecursosCatalogosBean datos, UsrBean user) throws ClsExceptions {
		return this.update(this.beanToHashTable(datos), user);
	}

	public boolean update(Hashtable datos, UsrBean user) throws ClsExceptions 
	{
		// Hacemos un update por cada uno de los lenguajes que estan dados de alta en el sistema 
		String campos [] = {GenRecursosCatalogosBean.C_DESCRIPCION};

		Hashtable h = new Hashtable();
		UtilidadesHash.set (h, GenRecursosCatalogosBean.C_IDRECURSO,   UtilidadesHash.getString(datos, GenRecursosCatalogosBean.C_IDRECURSO));

		String  descripcion = UtilidadesHash.getString(datos, GenRecursosCatalogosBean.C_DESCRIPCION);

		Vector vLenguajes = this.getLenguajes();
		for (int i = 0; i < vLenguajes.size(); i++) {
			AdmLenguajesBean len = (AdmLenguajesBean)vLenguajes.get(i);
			
			if (!user.getLanguageInstitucion().equalsIgnoreCase(len.getIdLenguaje())) {
				UtilidadesHash.set (h, GenRecursosCatalogosBean.C_DESCRIPCION, descripcion + "#" + len.getCodigoExt().toUpperCase());
			}
			else {
				UtilidadesHash.set (h, GenRecursosCatalogosBean.C_DESCRIPCION, descripcion);
			}
			
			UtilidadesHash.set (h, GenRecursosCatalogosBean.C_IDLENGUAJE, len.getIdLenguaje());
			if (!this.updateDirectRecursosCatalogos(h, this.getClavesBean(), campos)) 
				return false;
		}
		return true;
	}
	
	public boolean updateDirectRecursosCatalogos(Hashtable hash, String[] claves, String [] campos) throws ClsExceptions 
	{
		try {
			if (claves==null) {
				claves = this.getClavesBean();
			}
			if (campos==null)  {
				campos = this.getCamposActualizablesBean();
			}
			Row row = new Row();	
			
			// Establecemos las datos de modificacion
			UtilidadesHash.set(hash, MasterBean.C_USUMODIFICACION, this.usuModificacion); 
			UtilidadesHash.set(hash, MasterBean.C_FECHAMODIFICACION, "sysdate");

			// Cargamos el registro nuevo el que tiene las modificaciones
			row.load(hash);
			
			if (row.updateDirect(this.nombreTabla, claves, campos) >= 0) {				
				return true;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el \"update\" en B.D.");	
		}
		return false;	
	}
	
	// Se necestia para modificar la descripcion en multidioma/catalogos maestros
	
	public boolean updateDirect(MasterBean bean, String[] claves, String [] campos) throws ClsExceptions {
		try {

			Hashtable hash = this.beanToHashTable(bean);
			if (claves==null) {
				claves = this.getClavesBean();
			}
			if (campos==null)  {
				campos = this.getCamposActualizablesBean();
			}
			Row row = new Row();	
			
			// Establecemos las datos de modificacion
			UtilidadesHash.set(hash, MasterBean.C_USUMODIFICACION, this.usuModificacion); 
			UtilidadesHash.set(hash, MasterBean.C_FECHAMODIFICACION, "sysdate");

			// Cargamos el registro nuevo el que tiene las modificaciones
			row.load(hash);
			
			int filas = row.updateDirect(this.nombreTabla, claves, campos);
			if (filas > 0) {				
				return true;
			}
			if (filas < 0) {
				return false;
			}

			// filas == 0
			// Insertaremos el elemento si no estaba
			String where = " WHERE " + GenRecursosCatalogosBean.C_IDRECURSO + " = " + ((GenRecursosCatalogosBean)bean).getIdRecurso() +  
			               " AND rownum = 1 ";
			Vector v = this.select(where);
			if (v == null || v.size() < 1) {
				return false;
			}
			GenRecursosCatalogosBean bInsert = (GenRecursosCatalogosBean) v.get(0);
			bInsert.setDescripcion(((GenRecursosCatalogosBean)bean).getDescripcion());
			bInsert.setIdLenguaje(((GenRecursosCatalogosBean)bean).getIdLenguaje());
			bInsert.setIdInstitucion(((GenRecursosCatalogosBean)bean).getIdInstitucion());
			return this.insertGenericoRecursosCatalogos(bInsert);
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el \"update\" en B.D.");	
		}
	}
	
	public Vector selectPorDescripcion (String nombreTablaBusqueda, String idiomaATraducir, String idiomaUsuario) throws ClsExceptions 
	{
		Vector v = new Vector ();
		try {
			int numMaxRegistros = 0;
			try {
			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//	        	ReadProperties rp = new ReadProperties("SIGA.properties");
	        	String numMaxReg = rp.returnProperty("certificados.numMaxRegistros");
	        	numMaxRegistros = Integer.parseInt(numMaxReg);
			}
			catch (Exception e) {}

			String sql0 = " SELECT COUNT(*) TAM FROM " + GenRecursosCatalogosBean.T_NOMBRETABLA + " R " + 
					       " WHERE R." + GenRecursosCatalogosBean.C_IDLENGUAJE + " = '" + idiomaUsuario + "'" +
							 " AND R." + GenRecursosCatalogosBean.C_NOMBRETABLA + " = '" + nombreTablaBusqueda + "'";

			if (this.usrbean.getLocation().equalsIgnoreCase("2000")) {
				sql0 += " AND (R." + GenRecursosCatalogosBean.C_IDINSTITUCION + " IS NULL OR R." + GenRecursosCatalogosBean.C_IDINSTITUCION + " = " + this.usrbean.getLocation() + ")";  
			}
			else {
				sql0 += " AND R." + GenRecursosCatalogosBean.C_IDINSTITUCION + " = " + this.usrbean.getLocation();  
			}


			RowsContainer rc0 = this.findNLS(sql0);
			if (rc0.size() > 0) {
				Row fila = (Row) rc0.get(0);
				Hashtable h = fila.getRow(); 
				if (h != null){ 
					Integer tam = UtilidadesHash.getInteger(h, "TAM");
					if (tam.intValue() > numMaxRegistros) {
						throw new ClsExceptions ("messages.certificados.numMaxReg");
					}
				}
			}
			
			String sql = " SELECT " + GenRecursosCatalogosBean.C_IDRECURSO + ", " + 
									  GenRecursosCatalogosBean.C_DESCRIPCION + ", " +
					                "(SELECT " + GenRecursosCatalogosBean.C_DESCRIPCION + 
									  " FROM " + GenRecursosCatalogosBean.T_NOMBRETABLA + 
									 " WHERE " + GenRecursosCatalogosBean.C_IDRECURSO + " = R." + GenRecursosCatalogosBean.C_IDRECURSO + 
									   " AND " + GenRecursosCatalogosBean.C_IDLENGUAJE + " =TRIM('" + idiomaATraducir + "') ) DESCRIPCION_TRADUCIR, " +
									   " TRIM('" + idiomaUsuario + "') IDIOMA, " + 
									   " TRIM('" +idiomaATraducir + "') IDIOMA_TRADUCIR, " +
									 GenRecursosCatalogosBean.C_IDRECURSO + " || '  ' || " + GenRecursosCatalogosBean.C_IDRECURSOALIAS + " AYUDA " +
									 
									   


						   " FROM " + GenRecursosCatalogosBean.T_NOMBRETABLA + " R " + 
						  " WHERE R." + GenRecursosCatalogosBean.C_IDLENGUAJE + " = '" + idiomaUsuario + "'" +
							" AND R." + GenRecursosCatalogosBean.C_NOMBRETABLA + " = '" + nombreTablaBusqueda + "'";
			
			if (this.usrbean.getLocation().equalsIgnoreCase("2000")) {
				sql += " AND (R." + GenRecursosCatalogosBean.C_IDINSTITUCION + " IS NULL OR R." + GenRecursosCatalogosBean.C_IDINSTITUCION + " = " + this.usrbean.getLocation() + ")";  
			}
			else {
				sql += " AND R." + GenRecursosCatalogosBean.C_IDINSTITUCION + " = " + this.usrbean.getLocation();  
			}
			
			sql += " ORDER BY R." + GenRecursosCatalogosBean.C_DESCRIPCION + ", " + GenRecursosCatalogosBean.C_IDRECURSO;
			
			RowsContainer rc = this.findNLS(sql);
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);
				Hashtable h = fila.getRow(); 
				if (h != null){ 
					v.add(h);
				}
			}
		}
		
		catch (ClsExceptions e1) {
			throw e1;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}
}