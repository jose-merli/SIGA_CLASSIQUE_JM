package com.siga.beans;



import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gratuita.form.MantenimientoJuzgadoForm;
import com.siga.gratuita.vos.VolantesExpressVo;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_JUZGADO
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsJuzgadoAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsJuzgadoAdm (UsrBean usuario) {
		super( ScsJuzgadoBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsJuzgadoBean.C_IDINSTITUCION, ScsJuzgadoBean.C_IDJUZGADO,	
				ScsJuzgadoBean.C_NOMBRE, ScsJuzgadoBean.C_DIRECCION, ScsJuzgadoBean.C_CODIGOEXT,
							ScsJuzgadoBean.C_CODIGOPOSTAL, ScsJuzgadoBean.C_IDPROVINCIA,
							ScsJuzgadoBean.C_IDPOBLACION, ScsJuzgadoBean.C_TELEFONO1,
							ScsJuzgadoBean.C_TELEFONO2, ScsJuzgadoBean.C_FAX1,ScsJuzgadoBean.C_FECHABAJA,
							ScsJuzgadoBean.C_USUMODIFICACION, ScsJuzgadoBean.C_FECHAMODIFICACION,
							ScsJuzgadoBean.C_CODPROCURADOR, ScsJuzgadoBean.C_VISIBLE, ScsJuzgadoBean.C_MOVIL,
							ScsJuzgadoBean.C_EMAIL, ScsJuzgadoBean.C_CODIGOEXT2, ScsJuzgadoBean.C_ISCODIGOEJIS};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsJuzgadoBean.C_IDINSTITUCION, ScsJuzgadoBean.C_IDJUZGADO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsJuzgadoBean bean = null;
		try{
			bean = new ScsJuzgadoBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsJuzgadoBean.C_IDINSTITUCION));
			bean.setIdJuzgado(UtilidadesHash.getInteger(hash,ScsJuzgadoBean.C_IDJUZGADO));
			
			bean.setNombre(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_NOMBRE));
			bean.setDireccion(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_DIRECCION));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_CODIGOPOSTAL));
			bean.setCodigoExt(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_CODIGOEXT));
			bean.setCodigoExt2(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_CODIGOEXT2));
			bean.setIdProvincia(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_IDPOBLACION));
			bean.setTelefono1(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_TELEFONO1));
			bean.setTelefono2(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_TELEFONO2));
			bean.setFax1(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_FAX1));
			bean.setFechabaja(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_FECHABAJA));
			bean.setCodProcurador(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_CODPROCURADOR));
			bean.setVisible(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_VISIBLE));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsJuzgadoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsJuzgadoBean.C_USUMODIFICACION));
			bean.setMovil(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_MOVIL));
			bean.setIsCodigoEjis(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_ISCODIGOEJIS));
			//bean.setEmail(UtilidadesHash.getString(hash,ScsJuzgadoBean.C_EMAIL));
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
			ScsJuzgadoBean b = (ScsJuzgadoBean) bean;
			hash.put(ScsJuzgadoBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsJuzgadoBean.C_IDJUZGADO, String.valueOf(b.getIdJuzgado()));
			
			hash.put(ScsJuzgadoBean.C_NOMBRE, b.getNombre());
			hash.put(ScsJuzgadoBean.C_DIRECCION, b.getDireccion());
			hash.put(ScsJuzgadoBean.C_CODIGOEXT, b.getCodigoExt());
			hash.put(ScsJuzgadoBean.C_CODIGOEXT2, b.getCodigoExt2());
			hash.put(ScsJuzgadoBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			hash.put(ScsJuzgadoBean.C_IDPROVINCIA, b.getIdProvincia());
			hash.put(ScsJuzgadoBean.C_IDPOBLACION, b.getIdPoblacion());
			hash.put(ScsJuzgadoBean.C_TELEFONO1, b.getTelefono1());
			hash.put(ScsJuzgadoBean.C_TELEFONO2, b.getTelefono2());
			hash.put(ScsJuzgadoBean.C_FAX1, b.getFax1());	
			hash.put(ScsJuzgadoBean.C_FECHABAJA, b.getFechabaja());	
			hash.put(ScsJuzgadoBean.C_CODPROCURADOR, b.getCodProcurador());
			hash.put(ScsJuzgadoBean.C_VISIBLE, b.getVisible());
			hash.put(ScsJuzgadoBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsJuzgadoBean.C_FECHAMODIFICACION, b.getFechaMod());
			hash.put(ScsJuzgadoBean.C_MOVIL, b.getMovil());
			if (b.getIsCodigoEjis() != null){
				hash.put(ScsJuzgadoBean.C_ISCODIGOEJIS, b.getIsCodigoEjis());
			}else{
				hash.put(ScsJuzgadoBean.C_ISCODIGOEJIS, "0"); //Valor por defecto
			}
			//hash.put(ScsJuzgadoBean.C_EMAIL, b.getEmail());
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
		String[] vector = {ScsJuzgadoBean.C_IDJUZGADO,ScsJuzgadoBean.C_IDINSTITUCION};
		return vector;
	}
	
	/** Funcion getOrdenPorNombre ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenPorNombre() {		
		String[] vector = {"UPPER ("+ScsJuzgadoBean.T_NOMBRETABLA+"." + ScsJuzgadoBean.C_NOMBRE + ")"};
		return vector;
	}

	/** Funcion select (String where). Devuelve los campos: IDJUZGADO, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaJuzgados(MantenimientoJuzgadoForm miForm, String idInstitucionUsuario) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		String nombre="", idInstitucionJuzgado="", idPoblacion="", idProvincia="", codigoExt="";
		
		try {
			nombre = miForm.getNombreBusqueda();
			idInstitucionJuzgado = miForm.getIdInstitucionJuzgado();
			idPoblacion = miForm.getIdPoblacion();
			idProvincia = miForm.getIdProvincia();
			codigoExt = miForm.getCodigoExtBusqueda();
			
			select  =" SELECT i."+CenInstitucionBean.C_IDINSTITUCION;
			select += " , i."+CenInstitucionBean.C_ABREVIATURA+" INSTITUCION";
			select += " , juzgado."+ScsJuzgadoBean.C_NOMBRE;
			select += " , juzgado."+ScsJuzgadoBean.C_IDJUZGADO;
			select += " , juzgado."+ScsJuzgadoBean.C_IDPROVINCIA;
			select += " , juzgado."+ScsJuzgadoBean.C_IDPOBLACION;
			select += " , juzgado."+ScsJuzgadoBean.C_DIRECCION;
			select += " , juzgado."+ScsJuzgadoBean.C_CODIGOEXT;
			select += " , juzgado."+ScsJuzgadoBean.C_CODIGOEXT2;
			select += " , juzgado."+ScsJuzgadoBean.C_CODIGOPOSTAL;
			select += " , juzgado."+ScsJuzgadoBean.C_TELEFONO1;
			select += " , juzgado."+ScsJuzgadoBean.C_TELEFONO2;
			select += " , juzgado."+ScsJuzgadoBean.C_FAX1;
			select += " , juzgado."+ScsJuzgadoBean.C_FECHABAJA;
			select += " , juzgado."+ScsJuzgadoBean.C_MOVIL;
			select += " , juzgado."+ScsJuzgadoBean.C_EMAIL;
			select += " , (SELECT provincia."+CenProvinciaBean.C_NOMBRE+
					  "    FROM "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE provincia."+CenProvinciaBean.C_IDPROVINCIA+"=juzgado."+ScsJuzgadoBean.C_IDPROVINCIA+
					  ") AS PROVINCIA";
			select += " , (SELECT poblacion."+CenPoblacionesBean.C_NOMBRE+
					  "    FROM "+CenPoblacionesBean.T_NOMBRETABLA +" poblacion "+
					  "    ,    "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE poblacion."+CenPoblacionesBean.C_IDPROVINCIA+"=provincia."+CenProvinciaBean.C_IDPROVINCIA+					  
					  "    AND   juzgado."+ScsJuzgadoBean.C_IDPROVINCIA+"=poblacion."+CenPoblacionesBean.C_IDPROVINCIA+
					  "    AND   juzgado."+ScsJuzgadoBean.C_IDPOBLACION+"=poblacion."+CenPoblacionesBean.C_IDPOBLACION+
					  ") AS POBLACION";
			
			//FROM:
			select += " FROM "+ScsJuzgadoBean.T_NOMBRETABLA+" juzgado, ";
			select += CenInstitucionBean.T_NOMBRETABLA+" i ";

			//WHERE:
			if (idInstitucionJuzgado!=null && !idInstitucionJuzgado.equals(""))
				select += " WHERE juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+"="+idInstitucionJuzgado+
						  " AND juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			else {
				if(idInstitucionUsuario.equals("2000")){
					//salen las de todas las instituciones
					select += " WHERE juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
				} else {
					//salen las de la 2000 y las propias
					select += " WHERE (juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+"="+idInstitucionUsuario;
					select += "     OR juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+"=2000)";
					select += "   AND juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
				}
			}
			
			//Filtro por nombre:
			if (nombre !=null && !nombre.equals(""))				
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim().toUpperCase(),"UPPER(juzgado."+ScsJuzgadoBean.C_NOMBRE+")");

			//Filtro por codigoext:
			if (codigoExt !=null && !codigoExt.equals(""))				
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(codigoExt.trim().toUpperCase(),"UPPER(juzgado."+ScsJuzgadoBean.C_CODIGOEXT+")");
			//Filtro por idPoblacion:
			if (idPoblacion !=null && !idPoblacion.equals(""))				
				select += " AND juzgado."+ScsJuzgadoBean.C_IDPOBLACION+"="+idPoblacion;
			
			//Filtro por idProvincia:
			if (idProvincia !=null && !idProvincia.equals(""))				
				select += " AND juzgado."+ScsJuzgadoBean.C_IDPROVINCIA+"="+idProvincia;
			//ORDER BY
			select += " ORDER BY juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+", juzgado."+ScsJuzgadoBean.C_NOMBRE;
			
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
			throw new ClsExceptions (e, "Excepcion en ScsJuzgadoAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdJuzgado(String idInstitucion) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsJuzgadoBean.C_IDJUZGADO+")+1 AS ID FROM "+ScsJuzgadoBean.T_NOMBRETABLA+
					  " WHERE "+ScsJuzgadoBean.C_IDINSTITUCION+"="+idInstitucion;			
			
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
	
	/** Funcion select (String where). Devuelve los campos: IDJUZGADO, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaJuzgado(String idInstitucion, String idJuzgado) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT juzgado."+ScsJuzgadoBean.C_NOMBRE;
			select += " , juzgado."+ScsJuzgadoBean.C_IDJUZGADO;
			select += " , juzgado."+ScsJuzgadoBean.C_IDPROVINCIA;
			select += " , juzgado."+ScsJuzgadoBean.C_IDPOBLACION;
			select += " , juzgado."+ScsJuzgadoBean.C_DIRECCION;
			select += " , juzgado."+ScsJuzgadoBean.C_CODIGOEXT;
			select += " , juzgado."+ScsJuzgadoBean.C_CODIGOEXT2;
			select += " , juzgado."+ScsJuzgadoBean.C_CODIGOPOSTAL;
			select += " , juzgado."+ScsJuzgadoBean.C_TELEFONO1;
			select += " , juzgado."+ScsJuzgadoBean.C_TELEFONO2;
			select += " , juzgado."+ScsJuzgadoBean.C_FAX1;
			select += " , juzgado."+ScsJuzgadoBean.C_FECHABAJA;
			select += " , juzgado."+ScsJuzgadoBean.C_CODPROCURADOR;
			select += " , juzgado."+ScsJuzgadoBean.C_VISIBLE;
			select += " , juzgado."+ScsJuzgadoBean.C_MOVIL;
			select += " , juzgado."+ScsJuzgadoBean.C_EMAIL;
			select += " , juzgado."+ScsJuzgadoBean.C_ISCODIGOEJIS;
			select += " , (SELECT provincia."+CenProvinciaBean.C_NOMBRE+
					  "    FROM "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE provincia."+CenProvinciaBean.C_IDPROVINCIA+"=juzgado."+ScsJuzgadoBean.C_IDPROVINCIA+
					  ") AS PROVINCIA";
			select += " , (SELECT poblacion."+CenPoblacionesBean.C_NOMBRE+
					  "    FROM "+CenPoblacionesBean.T_NOMBRETABLA +" poblacion "+
					  "    ,    "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE poblacion."+CenPoblacionesBean.C_IDPROVINCIA+"=provincia."+CenProvinciaBean.C_IDPROVINCIA+					  
					  "    AND   juzgado."+ScsJuzgadoBean.C_IDPROVINCIA+"=poblacion."+CenPoblacionesBean.C_IDPROVINCIA+
					  "    AND   juzgado."+ScsJuzgadoBean.C_IDPOBLACION+"=poblacion."+CenPoblacionesBean.C_IDPOBLACION+
					  ") AS POBLACION";
			
			//FROM:
			select += " FROM "+ScsJuzgadoBean.T_NOMBRETABLA+" juzgado";

			//Filtro:
			select += " WHERE juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND juzgado."+ScsJuzgadoBean.C_IDJUZGADO+"="+idJuzgado;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	/** Funcion select (String where). Devuelve los campos: IDJUZGADO, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return Hashtable con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Hashtable obtenerDatosJuzgado(String idInstitucion, String idJuzgado) throws ClsExceptions 
	{
		Hashtable datos=null;
		
		try {
			String sql=
				"select "+
				"j."+ScsJuzgadoBean.C_IDJUZGADO+","+
				"j."+ScsJuzgadoBean.C_NOMBRE+","+
				"j."+ScsJuzgadoBean.C_DIRECCION+"||' '||j."+ScsJuzgadoBean.C_CODIGOPOSTAL+"||' '||p."+CenPoblacionesBean.C_NOMBRE+" DIRECCION_JUZGADO,"+
				"pj."+CenPartidoJudicialBean.C_NOMBRE+" PARTIDO_JUDICIAL "+
				" from "+
				ScsJuzgadoBean.T_NOMBRETABLA+" j,"+
				CenPoblacionesBean.T_NOMBRETABLA+" p,"+
				CenPartidoJudicialBean.T_NOMBRETABLA+" pj "+
				"where j."+ScsJuzgadoBean.C_IDPOBLACION+"=p."+CenPoblacionesBean.C_IDPOBLACION+"(+)"+
				"  and p."+CenPoblacionesBean.C_IDPARTIDO+"=pj."+CenPartidoJudicialBean.C_IDPARTIDO+"(+)"+
				"  and j."+ScsJuzgadoBean.C_IDINSTITUCION+"="+idInstitucion+
				"  and j."+ScsJuzgadoBean.C_IDJUZGADO+"="+idJuzgado;

			
			//Consulta:
			RowsContainer rc = this.find(sql);		
			if(rc!=null && rc.size()==1){
				datos= ((Row)rc.get(0)).getRow();
			}
			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	
	public boolean comprobarCodigoExt(String institucion, String idJuzgado, String codigoExt) throws ClsExceptions 
	{
		boolean salida=false;
		
		try {
			String sql=
				"select "+
				" j."+ScsJuzgadoBean.C_IDJUZGADO+" "+
				" from "+
				ScsJuzgadoBean.T_NOMBRETABLA+" j "+
				" where j."+ScsJuzgadoBean.C_CODIGOEXT+"='"+codigoExt + "'";
				
			if (!institucion.equals("2000")) {
				sql +=" and (j."+ScsJuzgadoBean.C_IDINSTITUCION+"="+institucion+" OR j."+ScsJuzgadoBean.C_IDINSTITUCION+"=2000)" ;
			}
			
			if (idJuzgado!=null && !idJuzgado.trim().equals("")) {
				sql+="  and j."+ScsJuzgadoBean.C_IDJUZGADO+"<>"+idJuzgado+"";
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
	
	/**
	 * Comprueba si ya existe un juzgado con el mismo nombre para la misma poblacion
	 * Si la institución es el CGAE comprueba la duplicidad para todas las instituciones
	 * Si es otra, la comprueba para esa institución y para el CGAE
	 * @param idInstitucion
	 * @param nombre
	 * @param idPoblacion
	 * @return true si no existe ya en el sistema
	 * @throws ClsExceptions
	 */	
	public static boolean comprobarDuplicidad(String idInstitucion, String idPoblacion, String nombre)
	throws ClsExceptions{
		boolean sinDuplicar=true;
		String pobl=(idPoblacion==null || idPoblacion.trim().length()==0?"null":idPoblacion.trim());
		Hashtable codigos = new Hashtable();
	    codigos.put(new Integer(1),idInstitucion);
	    codigos.put(new Integer(2),pobl);
	    codigos.put(new Integer(3),nombre);

		String sql=
			"select pkg_siga_valida_mto_3.fun_scs_Juzgado(:1,:2,:3) VALOR from DUAL";
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
	public List<ScsJuzgadoBean> getJuzgados(VolantesExpressVo volanteExpres)throws ClsExceptions{

		
       return getJuzgados(volanteExpres.getIdInstitucion().toString(),volanteExpres.getIdTurno().toString(),volanteExpres.getUsrBean(),true, true,"-1");
		
       
	} 
	
	public List<ScsJuzgadoBean> getJuzgados(String idInstitucion,String idTurno,UsrBean usrBean,boolean isObligatorio, boolean isBusqueda, String idJuzgado)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select scs_JUZGADO.Idjuzgado , scs_juzgado.IDINSTITUCION, ");
		
		if(isBusqueda)
			sql.append(" decode(scs_JUZGADO.fechabaja, NULL,scs_JUZGADO.NOMBRE || ' (' || cen_poblaciones.nombre || ')',scs_JUZGADO.NOMBRE || ' (' || cen_poblaciones.nombre || ') (BAJA)') AS NOMBRE");
		else
			sql.append(" scs_juzgado.NOMBRE || ' (' || cen_poblaciones.nombre || ')' AS NOMBRE ");
		
		sql.append(" from scs_juzgado, ");
		sql.append(" cen_poblaciones ");
		sql.append(" where scs_juzgado.idpoblacion = cen_poblaciones.idpoblacion(+) ");
		sql.append("   AND EXISTS (SELECT * ");
		sql.append(" from scs_turno, ");
		sql.append(" scs_materiajurisdiccion, ");
		sql.append(" scs_procedimientos, ");
		sql.append(" scs_juzgadoprocedimiento ");
		sql.append(" where scs_turno.idinstitucion = scs_materiajurisdiccion.idinstitucion ");
		sql.append(" and scs_turno.idmateria = scs_materiajurisdiccion.idmateria ");
		sql.append(" and scs_turno.idarea = scs_materiajurisdiccion.idarea ");
		sql.append(" and scs_materiajurisdiccion.idjurisdiccion = ");
		sql.append(" scs_procedimientos.idjurisdiccion ");
		sql.append(" and scs_materiajurisdiccion.idinstitucion = ");
		sql.append(" scs_procedimientos.idinstitucion ");
		sql.append(" and scs_procedimientos.idinstitucion = ");
		sql.append(" scs_juzgadoprocedimiento.idinstitucion ");
		sql.append(" and scs_procedimientos.idprocedimiento = ");
		sql.append(" scs_juzgadoprocedimiento.idprocedimiento ");
		sql.append(" and scs_juzgadoprocedimiento.idinstitucion_juzg = ");
		sql.append(" scs_juzgado.idinstitucion ");
		sql.append(" and scs_juzgadoprocedimiento.idjuzgado = scs_juzgado.idjuzgado ");

		sql.append(" and scs_turno.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		sql.append(" and scs_turno.idturno = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idTurno);
		//sql.append(" and nvl(scs_procedimientos.vigente, '0') = '1' ");
		
		
        sql.append("    AND scs_procedimientos.fechadesdevigor <= sysdate  ");
        sql.append("    AND (scs_procedimientos.fechahastavigor >= sysdate OR scs_procedimientos.fechahastavigor IS NULL) ");		
		
		if(isBusqueda){
			sql.append(" ) ORDER BY scs_juzgado.fechabaja DESC, NOMBRE ");
		} else {
			sql.append(" AND (scs_juzgado.fechabaja is null OR scs_juzgado.idjuzgado = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idJuzgado);
			sql.append(" )) ORDER BY NOMBRE ");		
		}
			
		
		List<ScsJuzgadoBean> alJuzgados= null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alJuzgados = new ArrayList<ScsJuzgadoBean>();
            	ScsJuzgadoBean juzgadoBean = new ScsJuzgadoBean();
            	if(isObligatorio){
	            	juzgadoBean.setNombre(UtilidadesString.getMensajeIdioma(usrBean, "general.combo.seleccionar"));
	            	juzgadoBean.setIdJuzgado(new Integer(-1));
            	}else{
            		juzgadoBean.setNombre("");
            		
            	}
            	alJuzgados.add(juzgadoBean);
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		juzgadoBean = new ScsJuzgadoBean();
            		juzgadoBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsJuzgadoBean.C_IDINSTITUCION));
            		juzgadoBean.setIdJuzgado(UtilidadesHash.getInteger(htFila,ScsJuzgadoBean.C_IDJUZGADO));
            		juzgadoBean.setNombre(UtilidadesHash.getString(htFila,ScsJuzgadoBean.C_NOMBRE));
            		alJuzgados.add(juzgadoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alJuzgados;
		
       
	} 
	
	public List<ScsJuzgadoBean> getJuzgados(String idInstitucion,UsrBean usrBean,boolean isObligatorio, boolean isBusqueda, String idJuzgado)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT scs_JUZGADO.Idjuzgado || ',' || scs_juzgado.IDINSTITUCION AS ID,scs_JUZGADO.Idjuzgado,scs_juzgado.idinstitucion, ");
		if(isBusqueda)
			sql.append(" decode(scs_JUZGADO.fechabaja, NULL,scs_JUZGADO.NOMBRE || ' (' || cen_poblaciones.nombre || ')',scs_JUZGADO.NOMBRE || ' (' || cen_poblaciones.nombre || ') (BAJA)') AS NOMBRE");
		else
			sql.append(" scs_juzgado.NOMBRE || ' (' || cen_poblaciones.nombre || ')' AS NOMBRE ");
		
		sql.append(" from scs_juzgado, ");
		sql.append(" cen_poblaciones ");
		sql.append(" where scs_juzgado.idpoblacion = cen_poblaciones.idpoblacion(+) ");
		sql.append("   AND EXISTS (SELECT * ");
        sql.append(" FROM scs_procedimientos, ");
        sql.append(" scs_juzgadoprocedimiento ");
        sql.append(" WHERE scs_procedimientos.idinstitucion = ");
        sql.append(" scs_juzgadoprocedimiento.idinstitucion ");
        sql.append(" AND scs_procedimientos.idprocedimiento = ");
        sql.append(" scs_juzgadoprocedimiento.idprocedimiento ");
        sql.append(" AND scs_juzgadoprocedimiento.idinstitucion_juzg = ");
        sql.append(" scs_juzgado.idinstitucion ");
        sql.append(" AND scs_juzgadoprocedimiento.idjuzgado = scs_juzgado.idjuzgado ");
        
        sql.append(" AND scs_juzgado.IDINSTITUCION =:");
        contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		
		
		if(isBusqueda){
			sql.append("  ) ORDER BY scs_juzgado.fechabaja DESC, NOMBRE ");
		} else {
			sql.append(" AND (scs_juzgado.fechabaja is null OR scs_juzgado.idjuzgado = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idJuzgado);
			sql.append(" )) ORDER BY NOMBRE ");		
		}
		
		
        
		
		List<ScsJuzgadoBean> alJuzgados= null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alJuzgados = new ArrayList<ScsJuzgadoBean>();
            	ScsJuzgadoBean juzgadoBean = new ScsJuzgadoBean();
            	if(isObligatorio){
	            	juzgadoBean.setNombre(UtilidadesString.getMensajeIdioma(usrBean, "general.combo.seleccionar"));
	            	juzgadoBean.setIdJuzgado(new Integer(-1));
            	}else{
            		juzgadoBean.setNombre("");
            		
            	}
            	alJuzgados.add(juzgadoBean);
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		juzgadoBean = new ScsJuzgadoBean();
            		juzgadoBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsJuzgadoBean.C_IDINSTITUCION));
            		juzgadoBean.setIdJuzgado(UtilidadesHash.getInteger(htFila,ScsJuzgadoBean.C_IDJUZGADO));
            		juzgadoBean.setNombre(UtilidadesHash.getString(htFila,ScsJuzgadoBean.C_NOMBRE));
            		alJuzgados.add(juzgadoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alJuzgados;
		
       
	} 
      
       
	 
	

}