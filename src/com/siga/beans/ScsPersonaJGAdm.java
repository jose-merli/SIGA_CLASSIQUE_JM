/*
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_PERSONAJG
 * 
 * Creado: julio.vicente 04/02/2005
 *  
 */

package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BusquedaPersonaJGForm;


public class ScsPersonaJGAdm extends MasterBeanAdministrador {
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsPersonaJGAdm (UsrBean usuario) {
		super( ScsPersonaJGBean.T_NOMBRETABLA, usuario);
	}
	
	/** Funcion selectGenerico (String consulta). Ejecuta la consulta que se le pasa en un string 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			

			if (rc.query(consulta)) {
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

	
	/** Funcion select (String where). Devuele todos los campos de los registros que cumplan los criterios.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectAll(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT * FROM " + ScsPersonaJGBean.T_NOMBRETABLA + " " + where ;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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
	
	/**
	 * Efectúa un SELECT en la tabla SCS_SOJ con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsPersonaJGBean.C_IDINSTITUCION + " = " + hash.get(ScsSOJBean.C_IDINSTITUCION) + " AND " + ScsPersonaJGBean.C_IDPERSONA + " = " + hash.get(ScsPersonaJGBean.C_IDPERSONA);
			datos = this.selectAll(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}	
	
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador del teléfono que se va a insertar.
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;					
		int contador = 0;
		
		try { 				
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsPersonaJGBean.C_IDPERSONA + ") + 1) AS IDPERSONA FROM " + nombreTabla;
			sql += " WHERE " + ScsPersonaJGBean.C_IDINSTITUCION + " = " + entrada.get(ScsPersonaJGBean.C_IDINSTITUCION); 

			Vector v = new Vector();
			v = this.ejecutaSelect(sql);			
			
			String idpersona = (String)((Hashtable)v.get(0)).get("IDPERSONA");
			if (idpersona.equals(""))
				entrada.put(ScsPersonaJGBean.C_IDPERSONA,"1");
			else
				entrada.put(ScsPersonaJGBean.C_IDPERSONA,((Hashtable)v.get(0)).get("IDPERSONA"));
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDPERSONA");
		};		
		
		return entrada;
	}
	
	public ScsPersonaJGBean prepararInsert (ScsPersonaJGBean entrada)throws ClsExceptions 
	{
		String values;					
		int contador = 0;
		
		try { 				
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsPersonaJGBean.C_IDPERSONA + ") + 1) AS IDPERSONA FROM " + nombreTabla;
			sql += " WHERE " + ScsPersonaJGBean.C_IDINSTITUCION + " = " + entrada.getIdInstitucion(); 

			Vector v = new Vector();
			v = this.ejecutaSelect(sql);			
			
			String idpersona = (String)((Hashtable)v.get(0)).get("IDPERSONA");
			if (idpersona.equals(""))
				entrada.setIdPersona(new Integer(1));
			else
				entrada.setIdPersona(UtilidadesHash.getInteger((Hashtable)v.get(0), "IDPERSONA"));
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDPERSONA");
		};		
		
		return entrada;
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	public String[] getCamposBean() {
		String[] campos = {	ScsPersonaJGBean.C_IDINSTITUCION,		ScsPersonaJGBean.C_IDPERSONA,
							ScsPersonaJGBean.C_NOMBRE,				ScsPersonaJGBean.C_APELLIDO1,
							ScsPersonaJGBean.C_APELLIDO2,			ScsPersonaJGBean.C_DIRECCION,
							ScsPersonaJGBean.C_CODIGOPOSTAL,		ScsPersonaJGBean.C_FECHANACIMIENTO,
							ScsPersonaJGBean.C_IDPROFESION,			ScsPersonaJGBean.C_IDPAIS,							
							ScsPersonaJGBean.C_IDPROVINCIA,			ScsPersonaJGBean.C_IDPOBLACION,
							ScsPersonaJGBean.C_ESTADOCIVIL,			ScsPersonaJGBean.C_REGIMENCONYUGAL,
							ScsPersonaJGBean.C_FECHAMODIFICACION,	ScsPersonaJGBean.C_USUMODIFICACION,
							ScsPersonaJGBean.C_TIPOPERSONAJG,	    ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,
							ScsPersonaJGBean.C_ENCALIDADDE,	        ScsPersonaJGBean.C_OBSERVACIONES,
							ScsPersonaJGBean.C_NIF,                 ScsPersonaJGBean.C_IDREPRESENTANTEJG,
							ScsPersonaJGBean.C_SEXO,                ScsPersonaJGBean.C_IDIOMA,
							ScsPersonaJGBean.C_HIJOS, 				ScsPersonaJGBean.C_FAX,
							ScsPersonaJGBean.C_CORREOELECTRONICO
							
						};

		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsPersonaJGBean.C_IDINSTITUCION,	ScsPersonaJGBean.C_IDPERSONA};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsPersonaJGBean bean = null;
		try{
			bean = new ScsPersonaJGBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsPersonaJGBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsPersonaJGBean.C_IDPERSONA));			
			bean.setNif(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_NIF));
			bean.setNombre(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_NOMBRE));
			bean.setApellido1(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_APELLIDO1));
			bean.setApellido2(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_APELLIDO2));
			bean.setDireccion(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_DIRECCION));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_CODIGOPOSTAL));
			bean.setFechaNacimiento(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_FECHANACIMIENTO));
			bean.setIdProfesion(UtilidadesHash.getInteger(hash,ScsPersonaJGBean.C_IDPROFESION));
			bean.setIdPais(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_IDPOBLACION));
			bean.setIdEstadoCivil(UtilidadesHash.getInteger(hash,ScsPersonaJGBean.C_ESTADOCIVIL));
			bean.setRegimenConyugal(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_REGIMENCONYUGAL));						
			bean.setTipo(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_TIPOPERSONAJG));						
			bean.setTipoIdentificacion(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION));						
			bean.setEnCalidadDe(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_ENCALIDADDE));						
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_OBSERVACIONES));						
			bean.setIdRepresentanteJG(UtilidadesHash.getInteger(hash,ScsPersonaJGBean.C_IDREPRESENTANTEJG));
			bean.setSexo(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_SEXO));
			bean.setIdioma(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_IDIOMA));
			bean.setHijos(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_HIJOS));
			bean.setFax(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_FAX));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash,ScsPersonaJGBean.C_CORREOELECTRONICO));
			
			
			
			
			
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
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = new Hashtable();
		
		try{
			ScsPersonaJGBean miBean = (ScsPersonaJGBean) bean;			
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDINSTITUCION,miBean.getIdInstitucion().toString());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDPERSONA,miBean.getIdPersona().toString());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_NIF,miBean.getNif().toString());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_NOMBRE,miBean.getNombre());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_APELLIDO1,miBean.getApellido1());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_APELLIDO2,miBean.getApellido2());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_DIRECCION,miBean.getDireccion());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_CODIGOPOSTAL,miBean.getCodigoPostal());						
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_FECHANACIMIENTO,miBean.getFechaNacimiento());			
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDPROFESION,miBean.getIdProfesion());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDPAIS,miBean.getIdPais());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDPROVINCIA,miBean.getIdProvincia());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDPOBLACION,miBean.getIdPoblacion());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_ESTADOCIVIL,miBean.getIdEstadoCivil());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_REGIMENCONYUGAL,miBean.getRegimenConyugal());			 
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_TIPOPERSONAJG,miBean.getTipo());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,miBean.getTipoIdentificacion());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_ENCALIDADDE,miBean.getEnCalidadDe());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_OBSERVACIONES,miBean.getObservaciones());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDREPRESENTANTEJG,miBean.getIdRepresentanteJG());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_SEXO,miBean.getSexo());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDIOMA,miBean.getIdioma());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_IDPROFESION,miBean.getIdProfesion());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_HIJOS,miBean.getHijos());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_FAX,miBean.getFax());
			UtilidadesHash.set(hash,ScsPersonaJGBean.C_CORREOELECTRONICO,miBean.getCorreoElectronico());
			
			
		
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
	protected String[] getOrdenCampos() {
		String[] orden = {ScsPersonaJGBean.C_IDINSTITUCION , ScsPersonaJGBean.C_IDPERSONA};
		return orden;
	}
	
	/** Funcion getCamposActualizablesBean ()
	 *  @return conjunto de datos con los nombres de los campos actualizables
	 * */
	protected String[] getCamposActualizablesBean ()
	{
	    return getCamposBean();
	}
	
	/** 
	 * Recoge informacion sobre el interesado de una EJG 
	 * @param  institucion - identificador de la institucion	 	  	 	  
	 * @param  persona - identificador de la persona
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosInteresadoEJG (String institucion, String persona) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
			    			"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NIF + " || ' ' || " +
			    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NOMBRE + " || ' ' || " +
			    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || " +
			    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO2 + ") AS DATOS_INTERESADO," +
			    			"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_DIRECCION + " || ' ' || " +
			    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_CODIGOPOSTAL + " || ' ' || " +
			    			CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_INTERESADO" +
							" FROM " + ScsPersonaJGBean.T_NOMBRETABLA +
							" LEFT JOIN " + CenPoblacionesBean.T_NOMBRETABLA +
								" ON " + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_IDPROVINCIA + "=" + CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_IDPROVINCIA +
										 " AND " +
										 ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_IDPOBLACION + "=" + CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_IDPOBLACION +
							" WHERE " +			 
							ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA + "=" + persona;
	            
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre los datos de un interesado en EJG.");
	       }
	       return datos;                        
	    }
	
	/**
	 * Obtiene el id de persona desde los datos de asistencias 
	 * @param institucion
	 * @param anio
	 * @param numero
	 * @return 
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getIdPersonaJgDesdeAsistencia (String institucion, String anio, String numero) throws ClsExceptions,SIGAException {
	   String salida = null;
	   Vector datos=new Vector();
       try {
       		ScsAsistenciasAdm asistAdm = new ScsAsistenciasAdm(this.usrbean); 
	    	Hashtable criterios = new Hashtable();
	    	criterios.put(ScsAsistenciasBean.C_IDINSTITUCION,institucion);
	    	criterios.put(ScsAsistenciasBean.C_ANIO,anio);
	    	criterios.put(ScsAsistenciasBean.C_NUMERO,numero);
	    	Vector asistencias = asistAdm.selectByPK(criterios);
	    	if (asistencias!=null && asistencias.size()>0) {
	    		ScsAsistenciasBean asistencia = (ScsAsistenciasBean) asistencias.get(0);
	    		if (asistencia.getIdPersonaJG()!=null) {
	    			salida = asistencia.getIdPersonaJG().toString();
	    		}
	    	} 
       }
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al obtener el idPersona desde las asistencias");
       }
       return salida;                        
    }

	
	/**
	 * Obtiene el nombre y apellidos de una persona jg 
	 * @param idPersona
	 * @return String con el nombre y apellidos
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getNombreApellidos (String id, String institucion) throws ClsExceptions,SIGAException {
	   String salida = null;
	   Vector datos=new Vector();
       try {
	    	Hashtable criterios = new Hashtable();
	    	criterios.put(ScsPersonaJGBean.C_IDPERSONA,id);
	    	criterios.put(ScsPersonaJGBean.C_IDINSTITUCION,institucion);
	    	Vector res = this.selectByPK(criterios);
	    	if (res!=null && res.size()>0) {
	    		ScsPersonaJGBean r = (ScsPersonaJGBean) res.get(0);
	    		if (r.getNombre()!=null) {
	    			salida = r.getNombre().toString() + " " +  r.getApellido1() + " " + ((r.getApellido2()!=null)?r.getApellido2():"");
	    		}
	    	} 
       }
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al obtener el nombre y apellido de la personaJG");
       }
       return salida;                        
    }


	/**
	 * Obtiene el id de persona desde los datos de Actuaciones designa 
	 * @param institucion
	 * @param turno
	 * @param anio
	 * @param numero
	 * @return 
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getIdPersonaJgDesdeTurno (String institucion, String turno, String anio, String numero) throws ClsExceptions,SIGAException {
	   String salida = null;
	   Vector datos=new Vector();
       try {
       		ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(this.usrbean); 
	    	Hashtable criterios = new Hashtable();
	    	criterios.put(ScsDefendidosDesignaBean.C_IDINSTITUCION,institucion);
	    	criterios.put(ScsDefendidosDesignaBean.C_ANIO,anio);
	    	criterios.put(ScsDefendidosDesignaBean.C_NUMERO,numero);
	    	criterios.put(ScsDefendidosDesignaBean.C_IDTURNO,turno);
	    	Vector defendidos = defendidosAdm.select(criterios);

	    	if (defendidos!=null && defendidos.size()>0) {
	    		ScsDefendidosDesignaBean defendido = (ScsDefendidosDesignaBean) defendidos.get(0);
	    		if (defendido.getIdPersona()!=null) {
	    			salida = defendido.getIdPersona().toString();
	    		}
	    	}

/*
	    	if (defendidos!=null && defendidos.size()>0) {
	    		Hashtable defendido = (Hashtable) defendidos.get(0);
	    		salida = (String) defendido.get(ScsDefendidosDesignaBean.C_IDPERSONA);
	    	}
	*/    	
       }
       catch (Exception e) {
       	throw new ClsExceptions (e, "Error al obtener el idPersona desde turnos");
       }
       return salida;                        
    }

	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
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

	/** 
	 * Busca en personas JG en funcion de los datos del form 
	 * @param  formulario - formulario de entrada de busqueda modal de personas JG	 	  	 	  
	 * @return  Vector - registros encontrados  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public PaginadorBind getPersonas (BusquedaPersonaJGForm formulario) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {

	       		RowsContainer rc = new RowsContainer(); 
	            int contador = 0;	            
	            Hashtable codigos = new Hashtable();
	            
	            String sql = " SELECT idpersona,nombre,apellido1,apellido2,nif FROM "+ScsPersonaJGBean.T_NOMBRETABLA +" WHERE " + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + formulario.getIdInstitucion();
	            
			   if (!formulario.getNombre().trim().equals("")) {
				contador ++;   
			   	sql += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNombre().trim(),ScsPersonaJGBean.T_NOMBRETABLA+"."+ScsPersonaJGBean.C_NOMBRE,contador,codigos)+") ";
			   }
			   if (!formulario.getApellido1().trim().equals("")) {
				contador++;   
			   	sql += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido1().trim(),ScsPersonaJGBean.T_NOMBRETABLA+"."+ScsPersonaJGBean.C_APELLIDO1,contador,codigos)+") ";
			   }
			   if (!formulario.getApellido2().trim().equals("")) {
				contador++;   
			   	sql += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getApellido2().trim(),ScsPersonaJGBean.T_NOMBRETABLA+"."+ScsPersonaJGBean.C_APELLIDO2,contador,codigos)+") ";
			   }
//			// Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso
				// de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
				// la consulta adecuada.    
			   if (!formulario.getNIdentificacion().trim().equals("")) {
			   	if (ComodinBusquedas.hasComodin(formulario.getNIdentificacion())){
			   	  contador++;	
			   	  sql += " AND ("+ComodinBusquedas.prepararSentenciaCompletaBind(formulario.getNIdentificacion().trim(),ScsPersonaJGBean.T_NOMBRETABLA+"."+ScsPersonaJGBean.C_NIF,contador,codigos )+") ";
			   	}else{
			   		contador ++;
			   	  sql +="AND "+ComodinBusquedas.prepararSentenciaNIFBind(formulario.getNIdentificacion(),ScsPersonaJGBean.T_NOMBRETABLA+"."+ScsPersonaJGBean.C_NIF,contador,codigos);	
			   	}
			   }
	           sql+=" ORDER BY "+ScsPersonaJGBean.C_IDINSTITUCION+", "+ ScsPersonaJGBean.C_IDPERSONA;
	           // datos = this.select(sql);
	            PaginadorBind paginador = new PaginadorBind(sql,codigos);				
				int totalRegistros = paginador.getNumeroTotalRegistros();
				
				if (totalRegistros==0){					
					paginador =null;
				}
		       	
				
				return paginador;
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al realizar la busqueda de personas JG");
	       }
	      // return datos;                        
	    }
	public boolean existePersona (String idPersona,String nombre,String apellido1,String apellido2,String nif) throws ClsExceptions,SIGAException {
		boolean existe=false;
		Vector resultado=new Vector();
		
		String sql="select * from scs_personajg p where p.idinstitucion="+this.usrbean.getLocation();
		if(idPersona!=null && !idPersona.equals(""))
		 	sql=sql+" and p.idpersona="+idPersona;
		if(nombre!=null && !nombre.equals(""))
		 	sql=sql+" and p.nombre='"+nombre+"'";
		if(apellido1!=null && !apellido1.equals(""))
		 	sql=sql+" and p.apellido1='"+apellido1+"'";		
		if(apellido2!=null && !apellido2.equals(""))
		 	sql=sql+" and p.apellido2='"+apellido2+"'";		
		if(nif!=null && !nif.equals(""))
		 	sql=sql+" and p.nif="+nif;
		 
		resultado=selectGenerico(sql);
		
		
		
		if(resultado!=null && resultado.size()>0)
			existe=true;
		return existe;
	}
	public ScsPersonaJGBean getPersonaJG(Long idPersonaJG, Integer idInstitucion)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT PER.NOMBRE, PER.APELLIDO1,  PER.APELLIDO2, ");
		sql.append(" PER.DIRECCION, PER.CODIGOPOSTAL, PER.IDPAIS, PER.IDPROVINCIA, ");
		sql.append("  PER.NIF, PER.IDPOBLACION,PER.FAX,PER.CORREOELECTRONICO,");
		sql.append("  POB.NOMBRE POBLACION,PRO.IDPROVINCIA, PRO.NOMBRE PROVINCIA ");
		sql.append(" FROM SCS_PERSONAJG PER,CEN_POBLACIONES POB,CEN_PROVINCIAS PRO ");
		sql.append(" WHERE  ");
		sql.append(" PER.IDPOBLACION = POB.IDPOBLACION(+) ");
		sql.append(" AND PER.IDPROVINCIA  = PRO.IDPROVINCIA(+) ");
		sql.append(" AND IDPERSONA = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idPersonaJG);
		sql.append(" AND IDINSTITUCION = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		
		Hashtable<String, Object> htPKTelefonos = new Hashtable<String, Object>();
		htPKTelefonos.put(ScsTelefonosPersonaBean.C_IDPERSONA, idPersonaJG);
		htPKTelefonos.put(ScsTelefonosPersonaBean.C_IDINSTITUCION, idInstitucion);
		
		ScsPersonaJGBean personaJG = null;
		ScsTelefonosPersonaJGAdm telefonosAdm = new ScsTelefonosPersonaJGAdm(usrbean);
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	
            	
    			
        		Row fila = (Row) rc.get(0);
        		Hashtable<String, Object> htFila=fila.getRow();
        		personaJG = (ScsPersonaJGBean) this.hashTableToBean(htFila);
        		if(!UtilidadesHash.getString(htFila,"IDPOBLACION").equals("")){
	        		CenPoblacionesBean poblacion = new CenPoblacionesBean();
	        		personaJG.setPoblacion(poblacion);
	        		poblacion.setNombre(UtilidadesHash.getString(htFila,"POBLACION"));
        		}
        		if(!UtilidadesHash.getString(htFila,"IDPROVINCIA").equals("")){
	        		CenProvinciaBean provincia = new CenProvinciaBean();
	        		personaJG.setProvincia(provincia);
	        		provincia.setNombre(UtilidadesHash.getString(htFila,"PROVINCIA"));
        		}
        		Vector<ScsTelefonosPersonaJGBean> vTelefonos = telefonosAdm.select(htPKTelefonos);
        		personaJG.setTelefonos(vTelefonos);
        		
        		
            	
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return personaJG;
		
	} 
	
	/**
	 * Devuelve un vector con las relaciones de
	 * @param idInstitucion
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions 
	 */
	public Vector getRelacionesPersonaJG(String idPersona, String idInstitucion,
			String asuntoActual, String tipo, String anioActual, String numeroActual) throws ClsExceptions{
		
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
			String longitudNumero=paramAdm.getValor(idInstitucion, "SCS", "LONGITUD_CODDESIGNA", "5");
			
		Vector resultado=new Vector();
		StringBuffer sql = new StringBuffer();
		
				sql.append(" select 'gratuita.operarEJG.literal.EJG' as asunto, e.anio as anio, lpad(e.numeJG,"+longitudNumero+",'0') as numero");
				sql.append("   from scs_ejg e, scs_personajg per, scs_unidadfamiliarejg uf ");
				sql.append("  where per.idinstitucion = " + idInstitucion + " ");
				sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
				sql.append("    and e.idinstitucion = per.idinstitucion ");
				sql.append("    and uf.anio = e.anio ");
				sql.append("    and uf.idinstitucion = e.idinstitucion ");
				sql.append("    and uf.numero = e.numero ");
				sql.append("    and uf.idtipoejg = e.idtipoejg ");
				sql.append("    and uf.idpersona = per.idpersona ");
				sql.append(" ");
				sql.append(" union ");
				sql.append(" ");
				sql.append(" select 'gratuita.operarEJG.literal.EJG' as asunto, e.anio as anio, lpad(e.numeJG,"+longitudNumero+",'0') as numero");
				sql.append("   from scs_ejg e, scs_personajg per, scs_contrariosejg cont ");
				sql.append("  where per.idinstitucion = " + idInstitucion + " ");
				sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
				sql.append("    and e.idinstitucion = per.idinstitucion ");
				sql.append("    and cont.anio = e.anio ");
				sql.append("    and cont.idinstitucion = e.idinstitucion ");
				sql.append("    and cont.numero = e.numero ");
				sql.append("    and cont.idtipoejg = e.idtipoejg ");
				sql.append("    and cont.idpersona = per.idpersona ");
				sql.append(" ");
				sql.append(" union ");
				sql.append(" ");
				sql.append(" select 'gratuita.operarEJG.literal.designa' as asunto, d.anio as anio, lpad(d.codigo,"+longitudNumero+",'0') as numero");
				sql.append("   from scs_designa d, scs_personajg per, scs_defendidosdesigna def ");
				sql.append("  where per.idinstitucion = " + idInstitucion + " ");
				sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
				sql.append("    and d.idinstitucion = per.idinstitucion ");
				sql.append("    and def.anio = d.anio ");
				sql.append("    and def.idinstitucion = d.idinstitucion ");
				sql.append("    and def.numero = d.numero ");
				sql.append("    and def.idturno = d.idturno ");
				sql.append("    and def.idpersona = per.idpersona ");
				sql.append(" ");
				sql.append(" union ");
				sql.append(" ");
				sql.append(" select 'gratuita.operarEJG.literal.designa' as asunto, d.anio as anio, lpad(d.codigo,"+longitudNumero+",'0') as numero");
				sql.append("   from scs_designa d, scs_personajg per, scs_contrariosdesigna cont ");
				sql.append("  where per.idinstitucion = " + idInstitucion + " ");
				sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
				sql.append("    and d.idinstitucion = per.idinstitucion ");
				sql.append("    and cont.anio = d.anio ");
				sql.append("    and cont.idinstitucion = d.idinstitucion ");
				sql.append("    and cont.numero = d.numero ");
				sql.append("    and cont.idturno = d.idturno ");
				sql.append("    and cont.idpersona = per.idpersona ");
				sql.append(" ");
				sql.append(" union ");
				sql.append(" ");
				sql.append(" select 'gratuita.operarEJG.literal.asistencia' as asunto, a.anio as anio, to_char(a.numero) as numero");
				sql.append("   from scs_asistencia a, scs_personajg per ");
				sql.append("  where per.idinstitucion = " + idInstitucion + " ");
				sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
				sql.append("    and a.idinstitucion = per.idinstitucion ");
				sql.append("    and a.idpersonajg = per.idpersona ");
				sql.append(" ");
				sql.append(" union ");
				sql.append(" ");
				sql.append(" select 'gratuita.operarEJG.literal.asistencia' as asunto, a.anio as anio, to_char(a.numero) as numero");
				sql.append("   from scs_asistencia a, scs_personajg per, scs_contrariosdesigna cont ");
				sql.append("  where per.idinstitucion = " + idInstitucion + " ");
				sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
				sql.append("    and a.idinstitucion = per.idinstitucion ");
				sql.append("    and cont.anio = a.anio ");
				sql.append("    and cont.idinstitucion = a.idinstitucion ");
				sql.append("    and cont.numero = a.numero ");
				sql.append("    and cont.idturno = a.idturno ");
				sql.append("    and cont.idpersona = per.idpersona ");
				sql.append(" ");
				sql.append(" union ");
				sql.append(" ");
				sql.append(" select 'gratuita.operarEJG.literal.SOJ' as asunto, s.anio as anio, lpad(s.numsoj,"+longitudNumero+",'0') as numero");
				sql.append("   from scs_soj s, scs_personajg per ");
				sql.append("  where per.idinstitucion = " + idInstitucion + " ");
				sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
				sql.append("    and s.idinstitucion = per.idinstitucion ");
				sql.append("    and s.idpersonajg = per.idpersona ");
				sql.append(" ");
				
				if(asuntoActual.equalsIgnoreCase("guardarEJG")){
					sql.append(" minus ");
					sql.append(" ");
					sql.append(" select 'gratuita.operarEJG.literal.EJG' as asunto, e.anio as anio, lpad(e.numeJG,"+longitudNumero+",'0') as numero");
					sql.append("   from scs_ejg e ");
					sql.append("   where e.idinstitucion= " + idInstitucion + " and e.anio=" + anioActual + " and e.numero =" + numeroActual + " and e.idtipoejg= "+tipo  );
				}else if(asuntoActual.equalsIgnoreCase("guardarSOJ")){
					sql.append(" minus ");
					sql.append(" ");
					sql.append(" select 'gratuita.operarEJG.literal.SOJ' as asunto, s.anio as anio, lpad(s.numsoj,"+longitudNumero+",'0') as numero");
					sql.append("   from scs_soj s");
					sql.append("   where s.idinstitucion= " + idInstitucion + " and s.anio=" + anioActual + " and s.numero =" + numeroActual + " and s.idtiposoj= "+tipo  );
				}else if(asuntoActual.equalsIgnoreCase("guardarDesigna")){
					sql.append(" minus ");
					sql.append(" ");
					sql.append(" select 'gratuita.operarEJG.literal.designa' as asunto, d.anio as anio, lpad(d.codigo,"+longitudNumero+",'0') as numero");
					sql.append("   from scs_designa d");
					sql.append("   where d.idinstitucion= " + idInstitucion + " and d.anio=" + anioActual + " and d.numero =" + numeroActual + " and d.idturno= "+tipo  );
				}else if(asuntoActual.equalsIgnoreCase("guardarAsistencia")){
					sql.append(" minus ");
					sql.append(" ");
					sql.append(" select 'gratuita.operarEJG.literal.asistencia' as asunto, a.anio as anio, lpad(to_char(a.numero),"+longitudNumero+",'0') as numero");
					sql.append("   from scs_asistencia a");
					sql.append("   where a.idinstitucion= " + idInstitucion + " and a.anio=" + anioActual + " and a.numero =" + numeroActual );
				} else if(asuntoActual.equalsIgnoreCase("guardarContrariosEJG")){
					sql.append(" minus ");
					sql.append(" ");
					sql.append(" select 'gratuita.operarEJG.literal.EJG' as asunto, e.anio as anio, lpad(e.numeJG,"+longitudNumero+",'0') as numero");
					sql.append("   from scs_ejg e ");
					sql.append("   where e.idinstitucion= " + idInstitucion + " and e.anio=" + anioActual + " and e.numero =" + numeroActual + " and e.idtipoejg= "+tipo  );
				} 
				
				sql.append("    order by asunto, anio desc, numero desc ");
				Vector<String> prueba = new Vector<String>();
				try {
					resultado=selectGenerico(sql.toString());

				} catch (ClsExceptions e) {

					throw new ClsExceptions (e, "Error al consultar relaciones de personaJG");
				}
				
		return resultado;
	}
	

	/**
	 * Devuelve un vector con las relaciones de
	 * @param idInstitucion
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions 
	 */
	public Vector getRelacionesPersonaJGAsistencia(String idPersona, String idInstitucion,
			String tipo, String anioActual, String numeroActual) throws ClsExceptions{
		
		
			Vector resultado=new Vector();
			StringBuffer sql = new StringBuffer();
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
			String longitudNumero=paramAdm.getValor(idInstitucion, "SCS", "LONGITUD_CODDESIGNA", "5");
			
		
			//sql.append(" select 'gratuita.operarEJG.literal.EJG' as asunto, e.anio as anio, lpad(e.numeJG,"+longitudNumero+",'0') as numero");
			sql.append(" select 'gratuita.operarEJG.literal.EJG' as asunto, e.anio as anio, lpad(e.numeJG,"+longitudNumero+",'0') as numero");
			sql.append("   from scs_ejg e, scs_personajg per, scs_unidadfamiliarejg uf ");
			sql.append("  where per.idinstitucion = " + idInstitucion + " ");
			sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
			sql.append("    and e.idinstitucion = per.idinstitucion ");
			sql.append("    and uf.anio = e.anio ");
			sql.append("    and uf.idinstitucion = e.idinstitucion ");
			sql.append("    and uf.numero = e.numero ");
			sql.append("    and uf.idtipoejg = e.idtipoejg ");
			sql.append("    and uf.idpersona = per.idpersona ");
			sql.append(" ");
			sql.append(" union ");
			sql.append(" ");
			sql.append(" select 'gratuita.operarEJG.literal.EJG' as asunto, e.anio as anio, lpad(e.numeJG,"+longitudNumero+",'0') as numero");
			sql.append("   from scs_ejg e, scs_personajg per, scs_contrariosejg cont ");
			sql.append("  where per.idinstitucion = " + idInstitucion + " ");
			sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
			sql.append("    and e.idinstitucion = per.idinstitucion ");
			sql.append("    and cont.anio = e.anio ");
			sql.append("    and cont.idinstitucion = e.idinstitucion ");
			sql.append("    and cont.numero = e.numero ");
			sql.append("    and cont.idtipoejg = e.idtipoejg ");
			sql.append("    and cont.idpersona = per.idpersona ");
			sql.append(" ");
			sql.append(" union ");
			sql.append(" ");
			sql.append(" select 'gratuita.operarEJG.literal.designa' as asunto, d.anio as anio, lpad(d.codigo,"+longitudNumero+",'0') as numero");
			sql.append("   from scs_designa d, scs_personajg per, scs_defendidosdesigna def ");
			sql.append("  where per.idinstitucion = " + idInstitucion + " ");
			sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
			sql.append("    and d.idinstitucion = per.idinstitucion ");
			sql.append("    and def.anio = d.anio ");
			sql.append("    and def.idinstitucion = d.idinstitucion ");
			sql.append("    and def.numero = d.numero ");
			sql.append("    and def.idturno = d.idturno ");
			sql.append("    and def.idpersona = per.idpersona ");
			sql.append(" ");
			sql.append(" union ");
			sql.append(" ");
			sql.append(" select 'gratuita.operarEJG.literal.designa' as asunto, d.anio as anio, lpad(d.codigo,"+longitudNumero+",'0') as numero");
			sql.append("   from scs_designa d, scs_personajg per, scs_contrariosdesigna cont ");
			sql.append("  where per.idinstitucion = " + idInstitucion + " ");
			sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
			sql.append("    and d.idinstitucion = per.idinstitucion ");
			sql.append("    and cont.anio = d.anio ");
			sql.append("    and cont.idinstitucion = d.idinstitucion ");
			sql.append("    and cont.numero = d.numero ");
			sql.append("    and cont.idturno = d.idturno ");
			sql.append("    and cont.idpersona = per.idpersona ");
			sql.append(" ");
			sql.append(" union ");
			sql.append(" ");
			sql.append(" select 'gratuita.operarEJG.literal.asistencia' as asunto, a.anio as anio, to_char(a.numero) as numero");
			sql.append("   from scs_asistencia a, scs_personajg per ");
			sql.append("  where per.idinstitucion = " + idInstitucion + " ");
			sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
			sql.append("    and a.idinstitucion = per.idinstitucion ");
			sql.append("    and a.idpersonajg = per.idpersona ");
			sql.append(" ");
			sql.append(" union ");
			sql.append(" ");
			sql.append(" select 'gratuita.operarEJG.literal.asistencia' as asunto, a.anio as anio, to_char(a.numero) as numero");
			sql.append("   from scs_asistencia a, scs_personajg per, scs_contrariosdesigna cont ");
			sql.append("  where per.idinstitucion = " + idInstitucion + " ");
			sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
			sql.append("    and a.idinstitucion = per.idinstitucion ");
			sql.append("    and cont.anio = a.anio ");
			sql.append("    and cont.idinstitucion = a.idinstitucion ");
			sql.append("    and cont.numero = a.numero ");
			sql.append("    and cont.idturno = a.idturno ");
			sql.append("    and cont.idpersona = per.idpersona ");
			sql.append(" ");
			sql.append(" union ");
			sql.append(" ");
			sql.append(" select 'gratuita.operarEJG.literal.SOJ' as asunto, s.anio as anio, lpad(s.numsoj,"+longitudNumero+",'0') as numero");
			sql.append("   from scs_soj s, scs_personajg per ");
			sql.append("  where per.idinstitucion = " + idInstitucion + " ");
			sql.append("    and (per.idpersona = " + idPersona + " or per.idrepresentantejg = " + idPersona + ")");
			sql.append("    and s.idinstitucion = per.idinstitucion ");
			sql.append("    and s.idpersonajg = per.idpersona ");
			sql.append(" ");

			// Aqui restamos la Asistencia y sus relaciones
			sql.append(" minus ");
			sql.append(" ");
			sql.append(" select 'gratuita.operarEJG.literal.asistencia' as asunto, a.anio as anio, to_char(a.numero) as numero");
			sql.append("   from scs_asistencia a");
			sql.append("   where a.idinstitucion= " + idInstitucion + " and a.anio=" + anioActual + " and a.numero =" + numeroActual );
			sql.append("  minus ");
			sql.append(" select 'gratuita.operarEJG.literal.EJG' as asunto, ");
			sql.append("       e.anio as anio, ");
			sql.append("       lpad(e.numeJG, 5, '0') as numero ");
			sql.append("  from scs_ejg e, scs_asistencia a ");
			sql.append(" where a.idinstitucion = " + idInstitucion + " ");
			sql.append("   and a.anio = " + anioActual + " ");
			sql.append("   and a.numero = " + numeroActual + " ");
			sql.append("   and a.idinstitucion = e.idinstitucion ");
			sql.append("   and e.idtipoejg = a.ejgidtipoejg ");
			sql.append("   and e.anio = a.ejganio ");
			sql.append("   and e.numero = a.ejgnumero ");
			sql.append(" ");
			sql.append("minus ");
			sql.append("select 'gratuita.operarEJG.literal.designa' as asunto, ");
			sql.append("       d.anio as anio, ");
			sql.append("       lpad(d.codigo, 5, '0') as numero ");
			sql.append("  from scs_designa d, scs_asistencia a ");
			sql.append(" where a.idinstitucion = " + idInstitucion + " ");
			sql.append("   and a.anio = " + anioActual + " ");
			sql.append("   and a.numero = " + numeroActual + " ");
			sql.append("   and a.idinstitucion = d.idinstitucion ");
			sql.append("   and d.idturno = a.designa_turno ");
			sql.append("   and d.anio = a.designa_anio ");
			sql.append("   and d.numero = a.designa_numero ");
				
				sql.append("    order by asunto, anio desc, numero desc ");
			try {
				resultado=selectGenerico(sql.toString());

			} catch (ClsExceptions e) {

				throw new ClsExceptions (e, "Error al consultar relaciones de personaJG");
			}
				
		return resultado;
	}
	
	
		public String getLenguajeSolicitante(Hashtable ht) throws ClsExceptions{
			
		String idlenguajeSolicitante = "";
		int idinstitucion = Integer.parseInt((String) ht.get("IDINSTITUCION"));
		int idtipoejg = Integer.parseInt((String) ht.get("IDTIPOEJG"));
		int anio = Integer.parseInt((String) ht.get("ANIO"));
		int numero = Integer.parseInt((String) ht.get("NUMERO"));		
		
		String sql=	" select P.IDLENGUAJE AS IDLENGUAJE_SOLICITANTE  From Scs_Personajg p, Scs_Ejg a  Where a.Idinstitucion = "+idinstitucion+ " "
		+ " And a.Anio = "+anio+"   And a.Numero =" + numero+"   And a.Idtipoejg =" +idtipoejg +" "
		+ " And a.Idpersonajg = p.Idpersona(+)"
		+ " And a.Idinstitucion = p.Idinstitucion(+) ";
          
		try {

			RowsContainer rc = new RowsContainer();

			if (rc.find(sql)) {
				Row r = (Row) rc.get(0);
				idlenguajeSolicitante = r.getString("IDLENGUAJE_SOLICITANTE");
			}

		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al getLenguajeSolicitante de personaJG");
		}

		return idlenguajeSolicitante;
	}
	
	

	
	
	
}