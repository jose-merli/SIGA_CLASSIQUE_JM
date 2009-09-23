package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/*
* Bean de administracion de la tabla CEN_PARTIDOJUDICIAL.
* 
* @author ruben.fernandez. 
* Modificado el 21-12-2004 por david.sanchezp
* Modificado el 27-12-2004 por julio.vicente Anhadida metodo getNombrePartido()
* @version 1.0
*/

public class CenPartidoJudicialAdm extends MasterBeanAdministrador {


	/**
	 * Constructor del bean de administracion de la tabla.
	 * @param Integer usuario: usuario.
	 */
	public CenPartidoJudicialAdm (UsrBean usuario) {
		super( CenPartidoJudicialBean.T_NOMBRETABLA, usuario);		
	}

	protected String[] getCamposBean() {
		String[] campos = {	CenPartidoJudicialBean.C_IDPARTIDO,			CenPartidoJudicialBean.C_NOMBRE,	CenPartidoJudicialBean.C_CODIGOEXT,
						 	CenPartidoJudicialBean.C_IDINSTITUCIONPROPIETARIO,
							CenPartidoJudicialBean.C_USUMODIFICACION,	CenPartidoJudicialBean.C_FECHAMODIFICACION};
		return campos;
	}
	protected String[] getClavesBean() {
		String[] campos = {	CenPartidoJudicialBean.C_IDPARTIDO};
		return campos;
	}

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenPartidoJudicialBean bean = null;
		try{
			bean = new CenPartidoJudicialBean();
			bean.setIdPartido(UtilidadesHash.getInteger(hash,CenPartidoJudicialBean.C_IDPARTIDO));
			bean.setNombre(UtilidadesHash.getString(hash,CenPartidoJudicialBean.C_NOMBRE));
			bean.setCodigoExt(UtilidadesHash.getString(hash,CenPartidoJudicialBean.C_CODIGOEXT));
			bean.setFechaMod	  (UtilidadesHash.getString(hash, CenPartidoJudicialBean.C_FECHAMODIFICACION));
			bean.setUsuMod		  (UtilidadesHash.getInteger(hash, CenPartidoJudicialBean.C_USUMODIFICACION));
			bean.setIdInstitucionPropietario (UtilidadesHash.getString(hash, CenPartidoJudicialBean.C_IDINSTITUCIONPROPIETARIO));

		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenPartidoJudicialBean b = (CenPartidoJudicialBean) bean;
			hash.put(CenPartidoJudicialBean.C_IDPARTIDO, String.valueOf(b.getIdPartido()));
			hash.put(CenPartidoJudicialBean.C_NOMBRE, b.getNombre());
			hash.put(CenPartidoJudicialBean.C_CODIGOEXT, b.getCodigoExt());
			hash.put(CenPartidoJudicialBean.C_IDINSTITUCIONPROPIETARIO, b.getIdInstitucionPropietario());
			UtilidadesHash.set(hash, CenPartidoJudicialBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, CenPartidoJudicialBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/**
	 * Devuelve un String con la consulta select SQL para saber el nuevo idpartido de un partido judicial.
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */
	public String selectIdPartido() throws ClsExceptions {
		String consulta = "";

		try {
			consulta = "select max("+CenPartidoJudicialBean.C_IDPARTIDO+") + 1  as idPartido from "+CenPartidoJudicialBean.T_NOMBRETABLA;
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Excepcion en selectIdPartido(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	
	/**
	 * Devuelve un String con la consulta select SQL para saber las poblaciones que tiene un determinado partido judicial.
	 * El select devuelve las columnas etiquetadas como: PARTIDOJUDICIAL,POBLACION,IDPARTIDO.
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */
	public String selectBusquedaPoblacionesPJ(String idpartido) {
		String consulta = "";

		consulta = " select "+CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_NOMBRE+" PARTIDOJUDICIAL , ";
		consulta += CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_NOMBRE+" IDPARTIDOJUDICIAL, ";
		consulta += CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_NOMBRE+" POBLACION, ";
		consulta += CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_IDPOBLACION+" IDPOBLACION";

		consulta += " from "+CenPartidoJudicialBean.T_NOMBRETABLA+" , "+CenPoblacionesBean.T_NOMBRETABLA;

		consulta += " where "+CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_IDPARTIDO+" = "+CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_IDPARTIDO;		
		consulta += " and "+CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_IDPARTIDO+" = '"+idpartido+"'";
		/*INC-2709: ordenacion de las poblaciones**/
		consulta +="order by poblacion";
				
		return consulta;
	}

	
	
	private String[] getOrderSelectBusqueda(){		
		String[] campos = { "provi."+CenProvinciaBean.C_NOMBRE+" DESC",
							"parti."+CenPartidoJudicialBean.C_NOMBRE};
		return campos;
	}
		
	/** 
	 * Devuelve un String con la consulta SQL para saber los partidos judiciales de una provincia y una poblacion(opcional)
	 * El select devuelve las columnas etiquetadas como: PROVINCIA,PARTIDOJUDICIAL,IDPARTIDO. Tambien: USUMODIFICACION y FECHAMODIFICACION.
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */
	public String selectBusqueda(Hashtable miHash) throws ClsExceptions, SIGAException {
		String consulta ="";
		String partido="";		

		try {
			partido = (String)miHash.get("PARTIDOJUDICIAL");
			consulta = "SELECT " + CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_NOMBRE+","+CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_IDPARTIDO+ " ";
			consulta += " FROM " + CenPartidoJudicialBean.T_NOMBRETABLA;
			if (!partido.equals("")) {
				consulta += " where "+ComodinBusquedas.prepararSentenciaCompleta(partido.trim(),CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE );
			}
	        consulta +=" ORDER BY "+CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE;	        	        
			
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Excepcion en CenPartidoJudicialAdm.selectBusqueda(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	
	/** 
	 * Devuelve un String con la consulta SQL para saber los partidos judiciales de una provincia, una poblacion(opcional) y la Institucion que tiene influencia sobre los PJ
	 * El select devuelve las columnas etiquetadas como: PROVINCIA,PARTIDOJUDICIAL,IDPARTIDO. Tambien: USUMODIFICACION y FECHAMODIFICACION.
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param institucion la institucion para la que hay que filtrar
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */
	public String selectBusquedaInstitucion(Hashtable miHash,String idInstitucion) throws ClsExceptions, SIGAException {
		String consulta ="";
		String partido="";		

		try {
			partido = (String)miHash.get("PARTIDOJUDICIAL");
			consulta = "SELECT " + CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_NOMBRE+","+CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_IDPARTIDO+ " ";
			consulta += " FROM " + CenPartidoJudicialBean.T_NOMBRETABLA+" , "+ CenInfluenciaBean.T_NOMBRETABLA + " " ;
			consulta += " where " + CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_IDPARTIDO + "="  + CenInfluenciaBean.T_NOMBRETABLA + "." + CenInfluenciaBean.C_IDPARTIDO + " "; 
			consulta += " and " + CenInfluenciaBean.T_NOMBRETABLA + "." + CenInfluenciaBean.C_IDINSTITUCION+"="+idInstitucion;
			if (!partido.equals("")) {
				consulta += " and "+ComodinBusquedas.prepararSentenciaCompleta(partido.trim(),CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE );
				
			}
			
	        consulta +=" ORDER BY "+CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE;	        	        
			
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Excepcion en CenPartidoJudicialAdm.selectBusqueda(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	
	
	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions,SIGAException {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try {
			RowsContainer rc = new RowsContainer(); 	
			if (rc.queryNLS(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}			
		} 
		catch (ClsExceptions e) {
			throw e;		
		}	
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenPartidoJudicialAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}


	
	/**
	 * Devuelve el id para un nuevo partido judicial calcullado a partir de la consulta SQL. 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return String con el ID del partido judicial.
	 * @throws ClsExceptions
	 */
	public String getIdPartido(String consulta) throws ClsExceptions, SIGAException {
		Hashtable miHash = new Hashtable();
		RowsContainer rc = null;
		String idpartido = "";
		
		try { 
			rc = new RowsContainer(); 
			//Se realiza una consulta en la base de datos para calcular el identificador del nuevo Partido Judicial. 
			if (rc.query(consulta)) {
				Row fila = (Row) rc.get(0);
				miHash = fila.getRow();			
				idpartido = (String)miHash.get("IDPARTIDO");			
			}
		}	
		catch (ClsExceptions e) {
			throw e;		
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenPartidoJudicialAdm.getIdPartido(). Consulta SQL:"+consulta);
		}	
		
		return idpartido;
	}
	

	/**
	 * devuelve un string con los partidos judiciales concatenados 
	 * @return String 
	 * @throws ClsExceptions
	 */
	public String getPartidos(String idinstitucion, String idzona, String idsubzona) throws ClsExceptions, SIGAException {
		Hashtable miHash = new Hashtable();
		RowsContainer rc = null;
		String idpartido = "";
		
		String auxZona = "";
		if (idzona!=null && !idzona.trim().equals("")) {
			auxZona = idzona.substring(idzona.indexOf(",")+1,idzona.length());
		}
	    Hashtable codigos = new Hashtable();
	    codigos.put(new Integer(1),idinstitucion);
	    codigos.put(new Integer(2),idsubzona);
	    codigos.put(new Integer(3),auxZona);

		String consulta = "select   Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES(:1,:2,:3) PARTIDOS FROM DUAL ";		
		try { 
			
			if (idinstitucion==null || idinstitucion.trim().equals("") || idsubzona==null || idsubzona.trim().equals("") || idzona==null || idzona.trim().equals("") )
					return "";
			
			rc = new RowsContainer(); 
			if (rc.queryBind(consulta,codigos)) {
				Row fila = (Row) rc.get(0);
				miHash = fila.getRow();			
				idpartido = (String)miHash.get("PARTIDOS");			
			}
		}	
		catch (ClsExceptions e) {
			throw e;		
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenPartidoJudicialAdm.getIdPartido(). Consulta SQL:"+consulta);
		}	
		
		return idpartido;
	}
		
	/**
	 * Devuelve la consulta SQL para obtener a partir de un id su fecha y usuario de modificacion. 
	 *
	 * @param String id: identidicador de la tabla.
	 * 
	 * @return String con la consulta SQL. 
	 */
	public String getFechaYUsu(String id) {
		String consulta = "select "+CenPartidoJudicialBean.C_FECHAMODIFICACION+","+CenPartidoJudicialBean.C_USUMODIFICACION+" FROM "+CenPartidoJudicialBean.T_NOMBRETABLA+" WHERE "+CenPartidoJudicialBean.C_IDPARTIDO+" = '"+id+"'";
		return consulta;
	}
	
	/**
	 * Devuelve la consulta SQL para obtener a partir de un id el nombre del partido. 
	 *
	 * @param String id: identidicador de la tabla.
	 * 
	 * @return String con la consulta SQL. 
	 */
	public String getNombrePartido(String id) {
		String consulta = "select "+CenPartidoJudicialBean.C_NOMBRE+ " FROM " + CenPartidoJudicialBean.T_NOMBRETABLA+" WHERE "+CenPartidoJudicialBean.C_IDPARTIDO+" = '"+id+"'";
		return consulta;
	}

	/**
	 * Devuelve la consulta SQL para obtener a partir de un id  
	 *
	 * @param String id: identidicador de la tabla.
	 * 
	 * @return String con la consulta SQL. 
	 */
	public String getDatosPartido(String id) {
		String consulta = "select * FROM " + CenPartidoJudicialBean.T_NOMBRETABLA+" WHERE "+CenPartidoJudicialBean.C_IDPARTIDO+" = '"+id+"'";
		return consulta;
	}
	
	/**
	 * Devuelve la consulta SQL para obtener a partir del nombre del partido su id . 
	 *
	 * @param String nombre: nombre el partido en la tabla.
	 * 
	 * @return String con la consulta SQL. 
	 */
	public String obtenerIdPartido(String nombre) {
		String consulta = "select "+CenPartidoJudicialBean.C_IDPARTIDO+ " FROM " + CenPartidoJudicialBean.T_NOMBRETABLA+" WHERE "+CenPartidoJudicialBean.C_NOMBRE+" = '"+nombre+"'";
		return consulta;		
	}	
	
	/**
	 * Devuelve el campo buscado de un registro de una tabla en la consulta SQL a partir de la clave dada.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @param String clave: campo con la clave de la tabla hash que coincide con el nombre de la columna donde esta el campo a buscar.
	 * @return String con el campo de la busqueda a partir de la clave dada.
	 * @throws ClsExceptions
	 */
	public String getCampoRegistro(String consulta, String clave) throws ClsExceptions, SIGAException {
		Hashtable miHash = new Hashtable();
		RowsContainer rc = null;
		String campo = "";
		
		try { 
			rc = new RowsContainer(); 
			//Se realiza una consulta en la base de datos para calcular el identificador del nuevo Partido Judicial. 
			if (rc.query(consulta)) {
				Row fila = (Row) rc.get(0);
				miHash = fila.getRow();			
				campo = (String)miHash.get(clave);			
			}
		}	
		catch (ClsExceptions e) {
			throw e;		
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenPartidoJudicialAdm.getCampoRegistro(). Consulta SQL:"+consulta);
		}	
		
		return campo;
	}	
	
}