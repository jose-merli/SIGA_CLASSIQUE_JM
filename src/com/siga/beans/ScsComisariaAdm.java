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
import com.siga.gratuita.form.MantenimientoComisariaForm;
import com.siga.gratuita.vos.VolantesExpressVo;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_COMISARIA
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsComisariaAdm extends MasterBeanAdministrador {
  

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsComisariaAdm (UsrBean usuario) {
		super( ScsComisariaBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsComisariaBean.C_IDINSTITUCION, ScsComisariaBean.C_IDCOMISARIA,	
							ScsComisariaBean.C_NOMBRE, ScsComisariaBean.C_DIRECCION,
							ScsComisariaBean.C_CODIGOPOSTAL, ScsComisariaBean.C_IDPROVINCIA,
							ScsComisariaBean.C_IDPOBLACION, ScsComisariaBean.C_TELEFONO1,
							ScsComisariaBean.C_TELEFONO2, ScsComisariaBean.C_FAX1,ScsComisariaBean.C_FECHABAJA,
							ScsComisariaBean.C_USUMODIFICACION, ScsComisariaBean.C_FECHAMODIFICACION,
							ScsComisariaBean.C_CODIGOEXT};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsComisariaBean.C_IDINSTITUCION, ScsComisariaBean.C_IDCOMISARIA};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsComisariaBean bean = null;
		try{
			bean = new ScsComisariaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsComisariaBean.C_IDINSTITUCION));
			bean.setIdComisaria(UtilidadesHash.getInteger(hash,ScsComisariaBean.C_IDCOMISARIA));
			
			bean.setNombre(UtilidadesHash.getString(hash,ScsComisariaBean.C_NOMBRE));
			bean.setDireccion(UtilidadesHash.getString(hash,ScsComisariaBean.C_DIRECCION));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,ScsComisariaBean.C_CODIGOPOSTAL));
			bean.setIdProvincia(UtilidadesHash.getString(hash,ScsComisariaBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,ScsComisariaBean.C_IDPOBLACION));
			bean.setTelefono1(UtilidadesHash.getString(hash,ScsComisariaBean.C_TELEFONO1));
			bean.setTelefono2(UtilidadesHash.getString(hash,ScsComisariaBean.C_TELEFONO2));
			bean.setFax1(UtilidadesHash.getString(hash,ScsComisariaBean.C_FAX1));
			bean.setFechabaja(UtilidadesHash.getString(hash,ScsComisariaBean.C_FECHABAJA));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsComisariaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsComisariaBean.C_USUMODIFICACION));
			bean.setCodigoExt(UtilidadesHash.getString(hash,ScsComisariaBean.C_CODIGOEXT));
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
			ScsComisariaBean b = (ScsComisariaBean) bean;
			hash.put(ScsComisariaBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsComisariaBean.C_IDCOMISARIA, String.valueOf(b.getIdComisaria()));
			hash.put(ScsComisariaBean.C_NOMBRE, b.getNombre());
			hash.put(ScsComisariaBean.C_DIRECCION, b.getDireccion());
			hash.put(ScsComisariaBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			hash.put(ScsComisariaBean.C_IDPROVINCIA, b.getIdProvincia());
			hash.put(ScsComisariaBean.C_IDPOBLACION, b.getIdPoblacion());
			hash.put(ScsComisariaBean.C_TELEFONO1, b.getTelefono1());
			hash.put(ScsComisariaBean.C_TELEFONO2, b.getTelefono2());
			hash.put(ScsComisariaBean.C_FAX1, b.getFax1());		
			hash.put(ScsComisariaBean.C_FECHABAJA, b.getFechabaja());		
			hash.put(ScsComisariaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsComisariaBean.C_FECHAMODIFICACION, b.getFechaMod());
			hash.put(ScsComisariaBean.C_CODIGOEXT, b.getCodigoExt());
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
		String[] vector = {ScsComisariaBean.C_IDCOMISARIA,ScsComisariaBean.C_IDINSTITUCION};
		return vector;
	}
	
	/** Funcion getOrdenPorNombre ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenPorNombre() {		
		String[] vector = {"UPPER ("+ScsComisariaBean.T_NOMBRETABLA+"." + ScsComisariaBean.C_NOMBRE + ")"};
		return vector;
	}

	/** Funcion select (String where). Devuelve los campos: IDCOMISARIA, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaComisarias(MantenimientoComisariaForm miForm, String idInstitucion) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		String nombre="", idInstitucionComisaria="", idPoblacion="", idProvincia="", codigoExt="";
		
		try {
			nombre = miForm.getNombreBusqueda();
			idInstitucionComisaria = miForm.getIdInstitucionComisaria();
			idPoblacion = miForm.getIdPoblacion();
			idProvincia = miForm.getIdProvincia();
			codigoExt=miForm.getCodigoExtBusqueda();
			
			select  =" SELECT i."+CenInstitucionBean.C_IDINSTITUCION;
			select += " , i."+CenInstitucionBean.C_ABREVIATURA+" INSTITUCION";
			select += " , comisaria."+ScsComisariaBean.C_NOMBRE;
			select += " , comisaria."+ScsComisariaBean.C_IDCOMISARIA;
			select += " , comisaria."+ScsComisariaBean.C_IDPROVINCIA;
			select += " , comisaria."+ScsComisariaBean.C_IDPOBLACION;
			select += " , comisaria."+ScsComisariaBean.C_DIRECCION;
			select += " , comisaria."+ScsComisariaBean.C_CODIGOPOSTAL;
			select += " , comisaria."+ScsComisariaBean.C_TELEFONO1;
			select += " , comisaria."+ScsComisariaBean.C_TELEFONO2;
			select += " , comisaria."+ScsComisariaBean.C_FAX1;
			select += " , comisaria."+ScsComisariaBean.C_FECHABAJA;
			select += " , comisaria."+ScsComisariaBean.C_CODIGOEXT;
			select += " , (SELECT provincia."+CenProvinciaBean.C_NOMBRE+
					  "    FROM "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE provincia."+CenProvinciaBean.C_IDPROVINCIA+"=comisaria."+ScsComisariaBean.C_IDPROVINCIA+
					  ") AS PROVINCIA";
			select += " , (SELECT poblacion."+CenPoblacionesBean.C_NOMBRE+
					  "    FROM "+CenPoblacionesBean.T_NOMBRETABLA +" poblacion "+
					  "    ,    "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE poblacion."+CenPoblacionesBean.C_IDPROVINCIA+"=provincia."+CenProvinciaBean.C_IDPROVINCIA+					  
					  "    AND   comisaria."+ScsComisariaBean.C_IDPROVINCIA+"=poblacion."+CenPoblacionesBean.C_IDPROVINCIA+
					  "    AND   comisaria."+ScsComisariaBean.C_IDPOBLACION+"=poblacion."+CenPoblacionesBean.C_IDPOBLACION+
					  ") AS POBLACION";
			
			//FROM:
			select += " FROM "+ScsComisariaBean.T_NOMBRETABLA+" comisaria, ";
			select += CenInstitucionBean.T_NOMBRETABLA+" i ";
			
			//WHERE:
			if (idInstitucionComisaria!=null && !idInstitucionComisaria.equals(""))
				select += " WHERE comisaria."+ScsComisariaBean.C_IDINSTITUCION+"="+idInstitucionComisaria+
						  " AND comisaria."+ScsComisariaBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
			else {
				if(idInstitucion.equals("2000")){
					//salen las de todas las instituciones
					select += " WHERE comisaria."+ScsComisariaBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
				} else {
					//salen las de la 2000 y las propias
					select += " WHERE (comisaria."+ScsComisariaBean.C_IDINSTITUCION+"="+idInstitucion;
					select += "     OR comisaria."+ScsComisariaBean.C_IDINSTITUCION+"=2000)";
					select += "   AND comisaria."+ScsComisariaBean.C_IDINSTITUCION+"= i."+CenInstitucionBean.C_IDINSTITUCION;
				}
			}

			//Filtro por nombre:
			if (nombre !=null && !nombre.equals(""))				

				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"comisaria."+ScsComisariaBean.C_NOMBRE);
								

				

			//Filtro por idPoblacion:
			if (idPoblacion !=null && !idPoblacion.equals(""))				
				select += " AND comisaria."+ScsComisariaBean.C_IDPOBLACION+"="+idPoblacion;
			
			//Filtro por idProvincia:
			if (idProvincia !=null && !idProvincia.equals(""))				
				select += " AND comisaria."+ScsComisariaBean.C_IDPROVINCIA+"="+idProvincia;		
			
//			Filtro por codigo externo:
			if ( codigoExt!=null && !codigoExt.equals(""))				
				select += " AND "+ComodinBusquedas.prepararSentenciaCompleta(codigoExt.trim().toUpperCase(),"upper(comisaria."+ScsComisariaBean.C_CODIGOEXT+")");	
			
			

			//ORDER BY
			select += " ORDER BY comisaria."+ScsComisariaBean.C_IDINSTITUCION+",comisaria."+ScsComisariaBean.C_NOMBRE;
			
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
			throw new ClsExceptions (e, "Excepcion en ScsComisariaAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdComisaria(String idInstitucion) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsComisariaBean.C_IDCOMISARIA+")+1 AS ID FROM "+ScsComisariaBean.T_NOMBRETABLA+
					  " WHERE "+ScsComisariaBean.C_IDINSTITUCION+"="+idInstitucion;			
			
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
	
	/** Funcion select (String where). Devuelve los campos: IDCOMISARIA, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector busquedaComisaria(String idInstitucion, String idComisaria) throws ClsExceptions 
	{
		Vector datos = new Vector();
		String select = null;
		
		try {
			select  = " SELECT comisaria."+ScsComisariaBean.C_NOMBRE;
			select += " , comisaria."+ScsComisariaBean.C_IDCOMISARIA;
			select += " , comisaria."+ScsComisariaBean.C_IDPROVINCIA;
			select += " , comisaria."+ScsComisariaBean.C_IDPOBLACION;
			select += " , comisaria."+ScsComisariaBean.C_DIRECCION;
			select += " , comisaria."+ScsComisariaBean.C_CODIGOPOSTAL;
			select += " , comisaria."+ScsComisariaBean.C_TELEFONO1;
			select += " , comisaria."+ScsComisariaBean.C_TELEFONO2;
			select += " , comisaria."+ScsComisariaBean.C_FAX1;
			select += " , comisaria."+ScsComisariaBean.C_FECHABAJA;
			select += " , comisaria."+ScsComisariaBean.C_CODIGOEXT;
			select += " , (SELECT provincia."+CenProvinciaBean.C_NOMBRE+
					  "    FROM "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE provincia."+CenProvinciaBean.C_IDPROVINCIA+"=comisaria."+ScsComisariaBean.C_IDPROVINCIA+
					  ") AS PROVINCIA";
			select += " , (SELECT poblacion."+CenPoblacionesBean.C_NOMBRE+
					  "    FROM "+CenPoblacionesBean.T_NOMBRETABLA +" poblacion "+
					  "    ,    "+CenProvinciaBean.T_NOMBRETABLA +" provincia "+
					  "    WHERE poblacion."+CenPoblacionesBean.C_IDPROVINCIA+"=provincia."+CenProvinciaBean.C_IDPROVINCIA+					  
					  "    AND   comisaria."+ScsComisariaBean.C_IDPROVINCIA+"=poblacion."+CenPoblacionesBean.C_IDPROVINCIA+
					  "    AND   comisaria."+ScsComisariaBean.C_IDPOBLACION+"=poblacion."+CenPoblacionesBean.C_IDPOBLACION+
					  ") AS POBLACION";
			
			//FROM:
			select += " FROM "+ScsComisariaBean.T_NOMBRETABLA+" comisaria";

			//Filtro:
			select += " WHERE comisaria."+ScsComisariaBean.C_IDINSTITUCION+"="+idInstitucion;
			select += " AND comisaria."+ScsComisariaBean.C_IDCOMISARIA+"="+idComisaria;
			
			//Consulta:
			datos = this.selectGenerico(select);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	/** Funcion select (String where). Devuelve los campos: IDCOMISARIA, IDINSTITUCION, NOMBRE, DIRECCION, <BR>
	 * CODIGOPOSTAL, POBLACION ,PROVINCIA, TELEFONO1, TELEFONO2, FAX1.
	 * @param criteros para filtrar el select, campo where 
	 *  @return Hashtable con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Hashtable obtenerDatosComisaria(String idInstitucion, String idComisaria) throws ClsExceptions 
	{
		Hashtable datos=null;
		
		try {
			String sql=
				"select "+
				"j."+ScsComisariaBean.C_IDCOMISARIA+","+
				"j."+ScsComisariaBean.C_NOMBRE+","+
				"j."+ScsComisariaBean.C_DIRECCION+"||' '||j."+ScsComisariaBean.C_CODIGOPOSTAL+"||' '||p."+CenPoblacionesBean.C_NOMBRE+" DIRECCION_COMISARIA,"+
				"pj."+CenPartidoJudicialBean.C_NOMBRE+" PARTIDO_JUDICIAL "+
				" from "+
				ScsComisariaBean.T_NOMBRETABLA+" j,"+
				CenPoblacionesBean.T_NOMBRETABLA+" p,"+
				CenPartidoJudicialBean.T_NOMBRETABLA+" pj "+
				"where j."+ScsComisariaBean.C_IDPOBLACION+"=p."+CenPoblacionesBean.C_IDPOBLACION+"(+)"+
				"  and p."+CenPoblacionesBean.C_IDPARTIDO+"=pj."+CenPartidoJudicialBean.C_IDPARTIDO+"(+)"+
				"  and j."+ScsComisariaBean.C_IDINSTITUCION+"="+idInstitucion+
				"  and j."+ScsComisariaBean.C_IDCOMISARIA+"="+idComisaria;

			
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

	/**
	 * Comprueba si ya existe una comisaria con el mismo nombre para la misma poblacion
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
			"select pkg_siga_valida_mto_3.fun_scs_Comisaria(:1,:2,:3) VALOR from DUAL";
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
	public List<ScsComisariaBean> getComisarias(VolantesExpressVo volanteExpres)throws ClsExceptions{
		return getComisarias(volanteExpres.getIdInstitucion(),volanteExpres.getIdTurno(),true, true, "-1");
		
		
	}
	public List<ScsComisariaBean> getComisarias(Integer idInstitucion, Integer idTurno,boolean isObligatorio, boolean isBusqueda, String idComisaria)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT C.IDCOMISARIA , C.IDINSTITUCION, ");
		
		if(isBusqueda)
			sql.append(" decode(c.fechabaja, NULL,c.NOMBRE || ' (' || po.nombre || ')',c.NOMBRE || ' (' || po.nombre || ') (BAJA)') AS NOMBRE");
		else
			sql.append(" c.NOMBRE || ' (' || po.nombre || ')' AS NOMBRE ");		
		
		sql.append(" FROM SCS_COMISARIA       c, ");
		sql.append(" cen_poblaciones     po, ");
		sql.append(" cen_partidojudicial par, ");
		sql.append(" scs_subzonapartido  spar, ");
		sql.append(" scs_subzona         szo, ");
		sql.append(" scs_zona            zo, ");
		sql.append(" scs_turno           tu ");
		sql.append(" where tu.idzona = zo.idzona ");
		sql.append(" AND tu.idinstitucion = zo.idinstitucion ");
		sql.append(" AND zo.idzona = szo.idzona ");
		sql.append(" AND zo.idinstitucion = szo.idinstitucion ");
		sql.append(" AND szo.idinstitucion = spar.idinstitucion ");
		sql.append(" AND szo.idsubzona = spar.idsubzona ");
		sql.append(" AND szo.idzona = spar.idzona ");
		sql.append(" AND spar.idpartido = par.idpartido ");
		sql.append(" AND par.idpartido = po.idpartido ");
		sql.append(" AND c.idpoblacion = po.idpoblacion ");
		sql.append(" and TU.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		sql.append(" and tu.idinstitucion = c.idinstitucion ");
		sql.append(" AND TU.IDTURNO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idTurno);
		if(isBusqueda){
			sql.append("  ORDER BY c.fechabaja DESC, NOMBRE ");
		} else {
			sql.append(" AND (c.fechabaja is null OR c.idComisaria = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idComisaria);
			sql.append(" ) ORDER BY NOMBRE ");		
		}
		
		List<ScsComisariaBean> alComisarias = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alComisarias = new ArrayList<ScsComisariaBean>();
            	ScsComisariaBean comisariaBean = new ScsComisariaBean();
            	if(isObligatorio){
	            	comisariaBean.setNombre(UtilidadesString.getMensajeIdioma(this.usrbean, "general.combo.seleccionar"));
	            	comisariaBean.setIdComisaria(new Integer(-1));
            	}else{
            		comisariaBean.setNombre("");
            		
            	}
    			alComisarias.add(comisariaBean);
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		comisariaBean = new ScsComisariaBean();
            		comisariaBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsComisariaBean.C_IDINSTITUCION));
            		comisariaBean.setIdComisaria(UtilidadesHash.getInteger(htFila,ScsComisariaBean.C_IDCOMISARIA));
            		comisariaBean.setNombre(UtilidadesHash.getString(htFila,ScsComisariaBean.C_NOMBRE));
            		alComisarias.add(comisariaBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alComisarias;
		
	}
	
	
	
	
	
	
	
	
	
}