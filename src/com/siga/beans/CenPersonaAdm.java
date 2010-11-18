/*
 * Created on 20-oct-2004
 *  Modified on 10-ene-2005 by miguel.villegas
 * 			incorporacion borrarPersona
 *
 *	Modified on 25-ene-2005 by nuria.rodriguezg
 * 			incorporacion getIdentificador
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.form.BusquedaClientesForm;
import com.siga.censo.form.MantenimientoDuplicadosForm;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
// RGG cambio visibilidad public class CenPersonaAdm extends MasterBeanAdministrador {
public class CenPersonaAdm extends MasterBeanAdmVisible {

    public static Long K_PERSONA_GENERICA = new Long(-1);
	
	
	public CenPersonaAdm (UsrBean usu) {
		super(CenPersonaBean.T_NOMBRETABLA, usu);
	}

	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenPersonaAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( CenPersonaBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	

	protected String[] getCamposBean() {
		String [] campos = {CenPersonaBean.C_APELLIDOS1,			CenPersonaBean.C_APELLIDOS2,
							CenPersonaBean.C_FECHAMODIFICACION,		CenPersonaBean.C_FECHANACIMIENTO,
							CenPersonaBean.C_IDESTADOCIVIL, 		CenPersonaBean.C_IDPERSONA,
							CenPersonaBean.C_IDTIPOIDENTIFICACION, 	CenPersonaBean.C_NATURALDE,
							CenPersonaBean.C_NIFCIF,				CenPersonaBean.C_NOMBRE,
							CenPersonaBean.C_USUMODIFICACION,       CenPersonaBean.C_SEXO,
							CenPersonaBean.C_FALLECIDO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenPersonaBean.C_IDPERSONA};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions{
		CenPersonaBean bean = null;
		
		try {
			bean = new CenPersonaBean();
			if((String)hash.get(CenPersonaBean.C_NOMBRE)!=null)
				bean.setNombre(((String)hash.get(CenPersonaBean.C_NOMBRE)).trim());
			if((String)hash.get(CenPersonaBean.C_APELLIDOS1)!=null)
				bean.setApellido1(((String)hash.get(CenPersonaBean.C_APELLIDOS1)).trim());
			if((String)hash.get(CenPersonaBean.C_APELLIDOS2)!=null)
				bean.setApellido2(((String)hash.get(CenPersonaBean.C_APELLIDOS2)).trim());
			
			bean.setFechaMod((String)hash.get(CenPersonaBean.C_FECHAMODIFICACION));		
			bean.setFechaNacimiento((String)hash.get(CenPersonaBean.C_FECHANACIMIENTO));
			bean.setIdEstadoCivil(UtilidadesHash.getInteger(hash, CenPersonaBean.C_IDESTADOCIVIL));
			bean.setIdPersona(UtilidadesHash.getLong(hash, CenPersonaBean.C_IDPERSONA));	
			bean.setIdTipoIdentificacion(UtilidadesHash.getInteger(hash, CenPersonaBean.C_IDTIPOIDENTIFICACION));			
			bean.setNaturalDe((String)hash.get(CenPersonaBean.C_NATURALDE));
			bean.setNIFCIF((String)hash.get(CenPersonaBean.C_NIFCIF));		
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenPersonaBean.C_USUMODIFICACION));	
			bean.setSexo(UtilidadesHash.getString(hash,CenPersonaBean.C_SEXO));
			bean.setFallecido(UtilidadesHash.getString(hash,CenPersonaBean.C_FALLECIDO));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions{

		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenPersonaBean b = (CenPersonaBean) bean;
			UtilidadesHash.set(htData, CenPersonaBean.C_APELLIDOS1, b.getApellido1());
			UtilidadesHash.set(htData, CenPersonaBean.C_APELLIDOS2, b.getApellido2());
			UtilidadesHash.set(htData, CenPersonaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenPersonaBean.C_FECHANACIMIENTO, b.getFechaNacimiento());
			UtilidadesHash.set(htData, CenPersonaBean.C_IDESTADOCIVIL, b.getIdEstadoCivil());
			UtilidadesHash.set(htData, CenPersonaBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, CenPersonaBean.C_IDTIPOIDENTIFICACION, b.getIdTipoIdentificacion());
			UtilidadesHash.set(htData, CenPersonaBean.C_NATURALDE, b.getNaturalDe());
			UtilidadesHash.set(htData, CenPersonaBean.C_NIFCIF, b.getNIFCIF());
			UtilidadesHash.set(htData, CenPersonaBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenPersonaBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, CenPersonaBean.C_SEXO, 			 b.getSexo());
			UtilidadesHash.set(htData, CenPersonaBean.C_FALLECIDO, 		 b.getFallecido());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/**
	 * Devuelve una persona a partir de un NIF/CIF  
	 * @version 1	 
	 * @param nifcif de la persona a buscar 
	 */	
	public CenPersonaBean getPersona (String nifcif, String nombre, String apellido1, String apellido2) throws ClsExceptions, SIGAException{
		String nombrePersona="", apellido1Persona="", apellido2Persona=""; 
		
		try {
			//LMS 22/08/2006
			//Se quitan los ceros de la izquierda.
			/*while (nifcif.startsWith("0"))
			{
				nifcif = nifcif.substring(1);
			} pdm 22/01/2007 ya no se quitan los 0 por la izquierda*/ 
			
			CenPersonaBean perBean = null;
			
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),UtilidadesString.LTrim(nifcif.toUpperCase(),"0"));
			Vector personas = selectBind("WHERE ltrim(UPPER(" +CenPersonaBean.C_NIFCIF+"),'0') = :1",codigos);			
			
			if ((personas != null) && personas.size() == 1) {
				perBean = (CenPersonaBean)personas.get(0);
				nombrePersona = perBean.getNombre().toUpperCase(); 
				apellido1Persona = perBean.getApellido1().toUpperCase(); 
				apellido2Persona = perBean.getApellido2().toUpperCase();
				
				// Compruebo que los nombre y apellidos son iguales 2 a 2:
				//-Nombre con Apellido1
				//-Nombre con Apellido2
				//-Apellido1 con Apellido2
				if ( ( nombrePersona.equals(nombre.toUpperCase()) && apellido1Persona.equals(apellido1.toUpperCase()) ) || 
					 ( nombrePersona.equals(nombre.toUpperCase()) && apellido2Persona.equals(apellido2.toUpperCase()) ) ||
					 ( apellido1Persona.equals(apellido1.toUpperCase()) && apellido2Persona.equals(apellido2.toUpperCase()) ) 
					) {
					// No hacemos nada (es el mismo y los devolvemos
				} else {
					// No es el mismo
					
					
					throw new SIGAException("messages.censo.nifcifExiste2");
				}
			}
			
			return perBean;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
	
	/**
	 * Devuelve una persona a partir de un NIF/CIF  
	 * @version 1	 
	 * @param nifcif de la persona a buscar 
	 */	
	public CenPersonaBean getPersonaNew (String nifcif, String nombre, String apellido1, String apellido2) throws ClsExceptions, SIGAException{
		String nombrePersona="", apellido1Persona="", apellido2Persona=""; 
		
		try {
			//LMS 22/08/2006
			//Se quitan los ceros de la izquierda.
			/*while (nifcif.startsWith("0"))
			{
				nifcif = nifcif.substring(1);
			} pdm 22/01/2007 ya no se quitan los 0 por la izquierda*/ 
			
			CenPersonaBean perBean = null;
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),UtilidadesString.LTrim(nifcif.toUpperCase(),"0"));
			Vector personas = selectBind("WHERE ltrim(UPPER(" +CenPersonaBean.C_NIFCIF+"),'0') = :1", codigos);			
			
			if ((personas != null) && personas.size() == 1) {
				perBean = (CenPersonaBean)personas.get(0);
				nombrePersona = perBean.getNombre().toUpperCase(); 
				apellido1Persona = perBean.getApellido1().toUpperCase(); 
				apellido2Persona = perBean.getApellido2().toUpperCase();
				
				// Compruebo que los nombre y apellidos son iguales 2 a 2:
				//-Nombre con Apellido1
				//-Nombre con Apellido2
				//-Apellido1 con Apellido2
				if ( ( ComodinBusquedas.sustituirVocales(nombrePersona).equals(ComodinBusquedas.sustituirVocales(nombre.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido1Persona).equals(ComodinBusquedas.sustituirVocales(apellido1.toUpperCase())) ) || 
					 ( ComodinBusquedas.sustituirVocales(nombrePersona).equals(ComodinBusquedas.sustituirVocales(nombre.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido2Persona).equals(ComodinBusquedas.sustituirVocales(apellido2.toUpperCase())) ) ||
					 ( ComodinBusquedas.sustituirVocales(apellido1Persona).equals(ComodinBusquedas.sustituirVocales(apellido1.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido2Persona).equals(ComodinBusquedas.sustituirVocales(apellido2.toUpperCase())) ) 
					) {
					// No hacemos nada (es el mismo y los devolvemos
				} else {
					// No es el mismo
					
					// PDM: No lanzamos excepcion, continuamos para delante con la solicitud, haremos esta 
					// validacion a la hora de aprobar la solicitud
					//throw new SIGAException("messages.censo.nifcifExiste2");
				}
			}
			
			return perBean;
		}
		/*catch (SIGAException e) {
			throw e;
		}*/
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
	/**
	 * Devuelve una persona a partir de un NIF/CIF  
	 * @version 1	 
	 * @param nifcif de la persona a buscar 
	 */	
	public CenPersonaBean getPersonaSolicitud (String nifcif, String nombre, String apellido1, String apellido2) throws ClsExceptions, SIGAException{
		String nombrePersona="", apellido1Persona="", apellido2Persona=""; 
		
		try {
			CenPersonaBean perBean = null;
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),UtilidadesString.LTrim(nifcif.toUpperCase(),"0"));
			Vector personas = selectBind("WHERE ltrim(UPPER(" +CenPersonaBean.C_NIFCIF+"),'0') = :1",codigos);			
			
			if ((personas != null) && personas.size() == 1) {
				perBean = (CenPersonaBean)personas.get(0);
				nombrePersona = perBean.getNombre().toUpperCase(); 
				apellido1Persona = perBean.getApellido1().toUpperCase(); 
				apellido2Persona = perBean.getApellido2().toUpperCase();
				
				// Compruebo que los nombre y apellidos son iguales 2 a 2:
				//-Nombre con Apellido1
				//-Nombre con Apellido2
				//-Apellido1 con Apellido2
				if ( ( nombrePersona.equals(nombre.toUpperCase()) && apellido1Persona.equals(apellido1.toUpperCase()) ) || 
					 ( nombrePersona.equals(nombre.toUpperCase()) && apellido2Persona.equals(apellido2.toUpperCase()) ) ||
					 ( apellido1Persona.equals(apellido1.toUpperCase()) && apellido2Persona.equals(apellido2.toUpperCase()) ) 
					) {
					// No hacemos nada (es el mismo y los devolvemos
				} else {
					// No es el mismo
					throw new SIGAException("messages.censo.nifcifExiste2");
				}
			}
			
			return perBean;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
	/**
	 * Comprueba si existe el NIF/CIF  
	 * @param nifcif de la persona a buscar 
	 */	
	public boolean existeNif (String nifcif, String nombre, String apellido1, String apellido2) throws ClsExceptions, SIGAException{
		try {
			CenPersonaBean persona = this.getPersona(nifcif, nombre, apellido1, apellido2); 
			if (persona==null) {
				return false;
			} else {
				return true;
			}
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al comprobar existencia de NIF/CIF");
		}
	}
	/**
	 * Comprueba si existe el NIF/CIF  
	 * @param nifcif de la persona a buscar 
	 */	
	public boolean existeNifPersona (String nifcif, String nombre, String apellido1, String apellido2) throws ClsExceptions, SIGAException{
		try {
			CenPersonaBean persona = this.getPersonaNew(nifcif, nombre, apellido1, apellido2); 
			if (persona==null) {
				return false;
			} else {
				return true;
			}
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al comprobar existencia de NIF/CIF");
		}
	}
	
	/**
	 * Comprueba si existe el una Persona  
	 * @param nifcif de la persona a buscar 
	 */	
	public CenPersonaBean existePersonaAlta (String nifcif, String nombre, String apellido1, String apellido2, String continuar) throws ClsExceptions, SIGAException{
		String nombrePersona="", apellido1Persona="", apellido2Persona=""; 
		try {
			/*CenPersonaBean persona = this.getPersona(nifcif, nombre, apellido1, apellido2); 
			return persona;*/
			CenPersonaBean perBean = null;
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),UtilidadesString.LTrim(nifcif.toUpperCase(),"0"));
			Vector personas = selectBind("WHERE ltrim(UPPER(" + CenPersonaBean.C_NIFCIF + "),'0') = :1",codigos);			
			
			if ((personas != null) && personas.size() == 1) {
				perBean = (CenPersonaBean)personas.get(0);
				nombrePersona = perBean.getNombre().toUpperCase(); 
				apellido1Persona = perBean.getApellido1().toUpperCase(); 
				apellido2Persona = perBean.getApellido2().toUpperCase();
				
				// Compruebo que los nombre y apellidos son iguales 2 a 2:
				//-Nombre con Apellido1
				//-Nombre con Apellido2
				//-Apellido1 con Apellido2
			 // Solo se hace la comprobacion si el usuario decide continuar
			if (continuar==null || continuar.equals("")){	
				if ( ( ComodinBusquedas.sustituirVocales(nombrePersona).equals(ComodinBusquedas.sustituirVocales(nombre.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido1Persona).equals(ComodinBusquedas.sustituirVocales(apellido1.toUpperCase())) ) || 
					 ( ComodinBusquedas.sustituirVocales(nombrePersona).equals(ComodinBusquedas.sustituirVocales(nombre.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido2Persona).equals(ComodinBusquedas.sustituirVocales(apellido2.toUpperCase())) ) ||
					 ( ComodinBusquedas.sustituirVocales(apellido1Persona).equals(ComodinBusquedas.sustituirVocales(apellido1.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido2Persona).equals(ComodinBusquedas.sustituirVocales(apellido2.toUpperCase())) ) 
					) {
					// No hacemos nada (es el mismo y los devolvemos
				} else {
					// No es el mismo
				
					
					throw new SIGAException("messages.censo.nifcifExiste2");
				}
			}
			}
			
			return perBean;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al comprobar existencia de NIF/CIF");
		}
	}
	
	
	/**
	 * Comprueba si existe el una Persona  
	 * @param nifcif de la persona a buscar 
	 */	
	public CenPersonaBean existePersona (String nifcif, String nombre, String apellido1, String apellido2) throws ClsExceptions, SIGAException{
		String nombrePersona="", apellido1Persona="", apellido2Persona=""; 
		try {
			/*CenPersonaBean persona = this.getPersona(nifcif, nombre, apellido1, apellido2); 
			return persona;*/
			CenPersonaBean perBean = null;
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),UtilidadesString.LTrim(nifcif.toUpperCase(),"0"));
			Vector personas = selectBind("WHERE ltrim(UPPER(" +CenPersonaBean.C_NIFCIF+"),'0') = :1",codigos);			
			
			if ((personas != null) && personas.size() == 1) {
				perBean = (CenPersonaBean)personas.get(0);
				nombrePersona = perBean.getNombre().toUpperCase(); 
				apellido1Persona = perBean.getApellido1().toUpperCase(); 
				apellido2Persona = perBean.getApellido2().toUpperCase();
				if (apellido2==null){
					apellido2="";
				}
				
				// Compruebo que los nombre y apellidos son iguales 2 a 2:
				//-Nombre con Apellido1
				//-Nombre con Apellido2
				//-Apellido1 con Apellido2
			 // Solo se hace la comprobacion si el usuario decide continuar
				
				if ( ( ComodinBusquedas.sustituirVocales(nombrePersona).equals(ComodinBusquedas.sustituirVocales(nombre.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido1Persona).equals(ComodinBusquedas.sustituirVocales(apellido1.toUpperCase())) ) || 
					 ( ComodinBusquedas.sustituirVocales(nombrePersona).equals(ComodinBusquedas.sustituirVocales(nombre.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido2Persona).equals(ComodinBusquedas.sustituirVocales(apellido2.toUpperCase())) ) ||
					 ( ComodinBusquedas.sustituirVocales(apellido1Persona).equals(ComodinBusquedas.sustituirVocales(apellido1.toUpperCase())) && ComodinBusquedas.sustituirVocales(apellido2Persona).equals(ComodinBusquedas.sustituirVocales((""+apellido2).toUpperCase())) ) 
					) {
					// No hacemos nada (es el mismo y los devolvemos
				} else {
					// No es el mismo
				
					
					throw new SIGAException(UtilidadesString.getMensajeIdioma(this.getLenguaje(), "messages.censo.nifcifExiste4", new String[]{nifcif}));
				}
			}
			
			
			return perBean;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al comprobar existencia de NIF/CIF");
		}
	}

	public CenPersonaBean getPersona (String nifcif) throws ClsExceptions{
		 
		try {
			
			CenPersonaBean perBean = null;
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),UtilidadesString.LTrim(nifcif.toUpperCase(),"0"));
			Vector personas = selectBind("WHERE ltrim(UPPER(" +CenPersonaBean.C_NIFCIF+"),'0') = :1",codigos);			
			
			if ((personas != null) && personas.size() == 1) {
				perBean = (CenPersonaBean)personas.get(0);
				
			}
			
			
			return perBean;
		}
		
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al comprobar existencia de NIF/CIF");
		}
	}

	/**
	 * Devuelve un cadena con el nombre y apellidos de una persona. 
	 * @author nuria.rgonzalez 14-12-04
	 * @version 1	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 */	
	public String obtenerNombreApellidos(String idPersona) throws ClsExceptions, SIGAException{
		CenPersonaBean personaBean;	
		Vector vPersonas = null;
		String nombre = "";
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idPersona);
		String sWhere = " where " + CenPersonaBean.C_IDPERSONA + "=:1";
		try{
			vPersonas = selectBind(sWhere,codigos);
			Enumeration enumer = vPersonas.elements();
		// Comprobamos que exista alguna persona con ese idPersona
			if(vPersonas.size()>0){
				personaBean = (CenPersonaBean)enumer.nextElement();
				nombre = personaBean.getNombre() + " " ;
				if (!personaBean.getApellido1().equals("#NA")){
					nombre=nombre+personaBean.getApellido1()+ " ";
				}
				if (!personaBean.getApellido2().equals("#NA")){
					nombre=nombre+personaBean.getApellido2();
				}
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener el nombre y apellidos");
		}
		return nombre;
	}

	public static String obtenerNombreApellidosJSP(String idPersona) throws ClsExceptions, SIGAException{
		String nombre = "";
		try{
		    RowsContainer rc = new RowsContainer(); 

		    Hashtable codigos = new Hashtable();
		    int contador=0;
		    
			String sql = "SELECT NOMBRE, APELLIDOS1, APELLIDOS2 FROM CEN_PERSONA where ";
			contador ++;
			codigos.put(new Integer(contador),idPersona);
			sql += CenPersonaBean.C_IDPERSONA + "=:" + contador;
			

			if (rc.findBind(sql,codigos)) {
               for (int i = 0; i < rc.size(); i++){
                    Row fila = (Row) rc.get(i);
                    Hashtable resultado=fila.getRow();
                  
					nombre = ((String)resultado.get("NOMBRE")) + " " ;
					if (!((String)resultado.get("APELLIDOS1")).equals("#NA")){
						nombre=nombre+((String)resultado.get("APELLIDOS1"))+ " ";
					}
					if (!((String)resultado.get("APELLIDOS2")).equals("#NA")){
						nombre=nombre+((String)resultado.get("APELLIDOS2"));
					}
               }
		    }

		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener el nombre y apellidos");
		}
		return nombre;
	}

	public String obtenerNombre(String idPersona) throws ClsExceptions, SIGAException{
		CenPersonaBean personaBean;	
		Vector vPersonas = null;
		String nombre = "";
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idPersona);
		String sWhere = " where " + CenPersonaBean.C_IDPERSONA + "=:1";
		try{
			vPersonas = selectBind(sWhere,codigos);
			Enumeration enumer = vPersonas.elements();
		// Comprobamos que exista alguna persona con ese idPersona
			if(vPersonas.size()>0){
				personaBean = (CenPersonaBean)enumer.nextElement();
				nombre = personaBean.getNombre() ;
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener el nombre y apellidos");
		}
		return nombre;
	}
	public String obtenerApellidos1(String idPersona) throws ClsExceptions, SIGAException{
		CenPersonaBean personaBean;	
		Vector vPersonas = null;
		String apellidos1 = "";
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idPersona);
		String sWhere = " where " + CenPersonaBean.C_IDPERSONA + "=:1";
		try{
			vPersonas = selectBind(sWhere,codigos);
			Enumeration enumer = vPersonas.elements();
		// Comprobamos que exista alguna persona con ese idPersona
			if(vPersonas.size()>0){
				personaBean = (CenPersonaBean)enumer.nextElement();
				apellidos1 = personaBean.getApellido1();
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener el nombre y apellidos");
		}
		return apellidos1;
	}
	
	public String obtenerApellidos2(String idPersona) throws ClsExceptions, SIGAException{
		CenPersonaBean personaBean;	
		Vector vPersonas = null;
		String apellidos2 = "";
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idPersona);
		String sWhere = " where " + CenPersonaBean.C_IDPERSONA + "=:1";
		try{
			vPersonas = selectBind(sWhere,codigos);
			Enumeration enumer = vPersonas.elements();
		// Comprobamos que exista alguna persona con ese idPersona
			if(vPersonas.size()>0){
				personaBean = (CenPersonaBean)enumer.nextElement();
				apellidos2 = personaBean.getApellido2();
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener el nombre y apellidos");
		}
		return apellidos2;
	}
	
	/**
	 * Devuelve un cadena con el NIF. 
	 * @author miguel.villegas 31-03-05
	 * @version 1	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 */	
	public String obtenerNIF(String idPersona) throws ClsExceptions, SIGAException{
		CenPersonaBean personaBean;	
		Vector vPersonas = null;
		String nif = "";
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idPersona);
		String sWhere = " where " + CenPersonaBean.C_IDPERSONA + "=:1";
		try{
			vPersonas = selectBind(sWhere,codigos);
			Enumeration enumer = vPersonas.elements();
		// Comprobamos que exista alguna persona con ese idPersona
			if(vPersonas.size()>0){
				personaBean = (CenPersonaBean)enumer.nextElement();
				nif = personaBean.getNIFCIF();
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener el nif");
		}
		return nif;
	}
	
	/** 
	 * Funcion que elimina una entrada de la tabla  
	 * @param  idPersona - identificador del usuario
	 * @param  usuario - UserBean del usuario
	 * @return  boolean - Si se ha realizado correctamente o no la operacion 	 
	 * @exception  ClsExceptions  En cualquier caso de error   
	 */	
	public boolean borrarPersona(String idPersona)throws ClsExceptions, SIGAException {
		
		boolean borrarPersona=false;		
		Hashtable hash=new Hashtable();
		
		try{ 
					
			// Cargo la tabla hash con el identifiacdor de la persona		
			hash.put("IDPERSONA",idPersona);
			// Procedo a borrar en la tabla				
			borrarPersona=this.delete(hash);
		}	
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (Exception ee){
			throw new ClsExceptions(ee,"Ha fallado el proceso de eliminacion de una persona en la tabla CEN_PERSONA ");				
		}		
		return borrarPersona;
	}	

	public Vector selectTabla(String where) throws ClsExceptions, SIGAException {
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = "Select Nvl("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+" , ' ') NCOLEGIADO, "+
	         					 CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+" NOMBRE, "+
								 CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" APELLIDOS1, "+
								 CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" APELLIDOS2 ";
			sql += " From "+CenPersonaBean.T_NOMBRETABLA+", "+CenColegiadoBean.T_NOMBRETABLA;
			sql += where;

			// RGG cambio visibilidad
			rc = this.find(sql);
//			if (rc.query(sql)) {
			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error al consultar datos.");
		}
		return v;
	}
	
	public Vector selectTabla1(String where) throws ClsExceptions, SIGAException {
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = "Select Nvl(f_siga_calculoncolegiado(GEN_CLIENTESTEMPORAL.IDINSTITUCION ,"+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+") , ' ') NCOLEGIADO, "+
	         					 CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+" NOMBRE, "+
								 CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" APELLIDOS1, "+
								 CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" APELLIDOS2 ";
			sql += " From "+CenPersonaBean.T_NOMBRETABLA+", GEN_CLIENTESTEMPORAL";
			sql += where;

			// RGG cambio visibilidad
			rc = this.find(sql);
//			if (rc.query(sql)) {
			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error al consultar datos.");
		}
		return v;
	}
	/**
	 * Devuelve una persona a partir de un idPersona  
	 * @version 1	 
	 * @param idPersona de la persona a buscar 
	 * @return  CenPersonaBean - Bean con los datos de la persona 	 
	 */	
	public CenPersonaBean getIdentificador(Long idPersona) throws ClsExceptions, SIGAException{
		try {
		    Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),idPersona.toString());
			Vector personas = selectBind("WHERE " + CenPersonaBean.C_IDPERSONA + " =:1",codigos);
			if ((personas != null) && personas.size() == 1) {
				return (CenPersonaBean)personas.get(0);
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
		return null;
	}

	
	/**
	 * Devuelve un nuevo identificador de persona para insercion  
	 * @param idInstitucion  
	 * @return  Long - Con el nuevo id de persona 	 
	 */	
	public Long getIdPersona(Integer idInstitucion) throws ClsExceptions, SIGAException {
		try {
			Long salida = null;
			
			// Acceso a BBDD
			RowsContainer rc = null;
			rc = new RowsContainer(); 
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),idInstitucion.toString());
			String sql = " SELECT (MAX("+CenPersonaBean.C_IDPERSONA+") + 1) AS NUMERO FROM " + CenPersonaBean.T_NOMBRETABLA + 
						 " WHERE SUBSTR("+CenPersonaBean.C_IDPERSONA+", 0, 4) =:1 ";
			
			// RGG cambio visibilidad 
			rc = this.findBind(sql,codigos);
			if (rc!=null) {
				if (rc.size()>0) {
					Row fila = (Row) rc.get(0);
					Hashtable resultado = fila.getRow(); 
					if (resultado != null){
						String salidaIdPersona=(String)resultado.get("NUMERO");
						if (!salidaIdPersona.trim().equals("")) {
							salida = new Long(salidaIdPersona);
						}
					}
				}
			}
			if (salida!=null) {
				return salida;
			} else {
				return new Long(idInstitucion + "000001");
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
	
	/**
	 * Actualiza los datos de persona y rellena la tabla de historicos (CEN_HISTORICO)
	 * @param BeanPersona datos generales cliente
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean updateConHistorico (CenPersonaBean beanPersona, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			if (update(beanToHashTable(beanPersona), beanPersona.getOriginalHash())) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanPersona, CenHistoricoAdm.ACCION_UPDATE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}

	
	/**
	 * actualiza los datos de la persona con los de la solicitud de incorporacion
	 * @param beanPersona
	 * @param beanSolicitud
	 * @return CenPersonaBean
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public CenPersonaBean updatePersona (CenPersonaBean beanPersona, CenSolicitudIncorporacionBean beanSolicitud) throws ClsExceptions, SIGAException 
	{
		CenPersonaBean beanPersonaTmp = null;
		
		try {
			//Clono el objeto persona			
			beanPersonaTmp = (CenPersonaBean) this.hashTableToBean((Hashtable)(beanPersona.getOriginalHash()).clone());
			
			//Actualizo los campos de la solicitud
			beanPersonaTmp.setFechaNacimiento(beanSolicitud.getFechaNacimiento());
			beanPersonaTmp.setIdEstadoCivil(beanSolicitud.getIdEstadoCivil());
			beanPersonaTmp.setNaturalDe(beanSolicitud.getNaturalDe());
			
			//Actualizo la persona
			update(beanPersonaTmp, beanPersona);			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
		return beanPersonaTmp;
	}
	
	/**
	 * Obtiene los clientes (colegiados o no) para una institución y
	 * un formulario de datos (BusquedaClientesModalForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public Paginador getPersonas(String idInstitucion, BusquedaClientesForm formulario) throws ClsExceptions, SIGAException {

		Vector salida = null;
		String sqlClientes = "";
	  	
	  	// Acceso a BBDD
		RowsContainer rcClientes = null;
		try { 
		    
		    boolean bBusqueda  = UtilidadesString.stringToBoolean(formulario.getChkBusqueda());
			
			String nombre = formulario.getNombrePersona();
			String apellido1 = formulario.getApellido1();
			String apellido2 = formulario.getApellido2();
			String nif = formulario.getNif();
			
			//Tabla cen_persona
			String P_APELLIDOS1=CenPersonaBean.C_APELLIDOS1;
			String P_APELLIDOS2=CenPersonaBean.C_APELLIDOS2;
			String P_NOMBRE=CenPersonaBean.C_NOMBRE;
			String P_NIF=CenPersonaBean.C_NIFCIF;
			
			String sql;
		
			sql=" SELECT "+ CenPersonaBean.T_NOMBRETABLA+".*, decode((select '1'"+
                 "                                                    from dual "+
                 "                                                    where "+CenPersonaBean.C_IDPERSONA+">1000),'1','1',2) ORDENAR"+
		
			    " from "+CenPersonaBean.T_NOMBRETABLA;
		
			
			
	       	if (!bBusqueda) {
	       		sql += (nombre   != null && !nombre.equals("")   )? " WHERE "+ ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),P_NOMBRE) : " WHERE 1=1";
	       	}
	       	else {
	       		sql += (nombre   != null && !nombre.equals("")   )? " WHERE (" + P_NOMBRE     + " " + ComodinBusquedas.prepararSentenciaExacta(nombre.trim())    + ") " : " WHERE 1=1";
	       	}
	       	
	       	
//          Criterio de busqueda especial para los campos Apellido1 y Apellido2
		       if (!formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
		       	if (bBusqueda) {
		       		sql += " AND ((("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido1().trim() )+")"+
		       		               " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido2().trim() )+"))"+
								   " OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido1().trim()+" "+formulario.getApellido2().trim() )+"))";
		       	}else{
		       		sql += " AND ((("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1 )+")"+
		                           " AND ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2)+"))"+
					               " OR ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim()+"* "+formulario.getApellido2().trim()+"* " ,CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1 )+"))";
		       		
		       	}
		       }else if (!formulario.getApellido1().trim().equals("")&&(formulario.getApellido2().trim().equals(""))) {//Apellido1 relleno
		       	if (bBusqueda) {
		       		sql += " AND (("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido1())+") OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" LIKE '"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+" %'))";
		       	}else{
		       		sql += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1 )+" OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+"||' '||"+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" LIKE '%"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+"%')) ";
		       		
		       	}
		       }else if (formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {//Apellido2 relleno
		       	if (bBusqueda) {
		       		sql += " AND (("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido2().trim() )+")"+
					               " OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" LIKE '% "+UtilidadesBDAdm.validateChars(formulario.getApellido2().trim()) +"' )) ";
		       		 
		       	}else{
		       		sql += " AND (("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2)+") "+
					               " OR ("+ComodinBusquedas.prepararSentenciaCompleta(" "+formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1)+ ")  )";
		       	}
		       	
		       }

			//Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso
			// de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
			// la consulta adecuada.				
			if (nif!=null && !nif.equals("")){
				if (!bBusqueda) {
					if (ComodinBusquedas.hasComodin(nif)){
						
						sql +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(nif.trim(),P_NIF);
					}else{
						sql +=" AND "+ComodinBusquedas.prepararSentenciaNIF(nif,"UPPER(" + P_NIF + ")");
					}
		       	}
		       	else {
		       		sql += " and (UPPER(" + P_NIF     + ") " + ComodinBusquedas.prepararSentenciaExacta(nif.trim().toUpperCase())    + ") ";
		       	}
				
				
				
				
			}
	        
			sql+= " ORDER BY ORDENAR, "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+", "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE;
		
	       Paginador paginador = new Paginador(sql);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}else{
				int registrosPorPagina = paginador.getNumeroRegistrosPorPagina();	    		
	    		Vector datos = paginador.obtenerPagina(1);
	    	
			}
		
			
			return paginador;
		} 
//		catch (SIGAException e) { 
//			throw e; 	
//		}
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes "); 
		}
	}

	
	/**
	 * Obtiene los clientes (colegiados o no) de un consejo. Esto incluye los colegiados de sus colegios
	 * y los no colegiados del consejo. Se filtra segun un formulario de datos (BusquedaClientesModalForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public Paginador getClientesConsejo(String idInstitucion, BusquedaClientesForm formulario) throws ClsExceptions, SIGAException {

		Vector salida = null;
		String sqlClientes = "";
	  	
	  	// Acceso a BBDD
		RowsContainer rcClientes = null;
		try { 
		    
		    boolean bBusqueda  = UtilidadesString.stringToBoolean(formulario.getChkBusqueda());
			
			String nombre = formulario.getNombrePersona();
			String apellido1 = formulario.getApellido1();
			String apellido2 = formulario.getApellido2();
			String nif = formulario.getNif();
			
			//Tabla cen_persona
			String P_APELLIDOS1=CenPersonaBean.C_APELLIDOS1;
			String P_APELLIDOS2=CenPersonaBean.C_APELLIDOS2;
			String P_NOMBRE=CenPersonaBean.C_NOMBRE;
			String P_NIF=CenPersonaBean.C_NIFCIF;
			
			
			/* SELECT cen_persona.*
                          FROM CEN_PERSONA, CEN_CLIENTE, CEN_COLEGIADO
                         WHERE CEN_PERSONA.IDPERSONA = CEN_CLIENTE.IDPERSONA
                           AND CEN_CLIENTE.IDPERSONA = CEN_COLEGIADO.IDPERSONA
                           AND CEN_CLIENTE.IDINSTITUCION =
                               CEN_COLEGIADO.IDINSTITUCION
                           AND (CEN_CLIENTE.IDINSTITUCION = 3006 OR
                               CEN_CLIENTE.CARACTER <> 'P')
                           AND CEN_CLIENTE.IDINSTITUCION in
                               (3006, 2078, 2082, 2067, 2040, 2060, 2065, 2009, 2013, 2054)
                        
                        union
                        
                        SELECT cen_persona.*
                          FROM CEN_PERSONA, CEN_CLIENTE, CEN_NOCOLEGIADO
                         WHERE NOT EXISTS
                         (SELECT 1
                                  FROM CEN_COLEGIADO
                                 WHERE CEN_COLEGIADO.IDPERSONA =
                                       CEN_CLIENTE.IDPERSONA
                                   AND CEN_COLEGIADO.IDINSTITUCION =
                                       CEN_CLIENTE.IDINSTITUCION)
                           AND CEN_PERSONA.IDPERSONA = CEN_CLIENTE.IDPERSONA
                           AND (CEN_CLIENTE.IDINSTITUCION = 3006 OR CEN_CLIENTE.CARACTER <> 'P')
                           AND CEN_CLIENTE.IDINSTITUCION = CEN_NOCOLEGIADO.IDINSTITUCION(+)
                           AND CEN_CLIENTE.IDPERSONA = CEN_NOCOLEGIADO.IDPERSONA(+)
                           AND CEN_CLIENTE.LETRADO = 0
                           AND CEN_CLIENTE.IDINSTITUCION = 3006
                           AND EXISTS
                         (SELECT CEN_NOCOLEGIADO.TIPO
                                  FROM CEN_NOCOLEGIADO
                                 WHERE CEN_NOCOLEGIADO.IDINSTITUCION = CEN_CLIENTE.IDINSTITUCION
                                   AND CEN_NOCOLEGIADO.IDPERSONA = CEN_CLIENTE.IDPERSONA
                                   AND CEN_NOCOLEGIADO.TIPO = 1))
             */
			
			
			String consulta, busquedaColegiados = "", busquedaNoColegiados = "";
			
			// Criterios de busqueda de colegiados de colegios del consejo
			busquedaColegiados = " SELECT "+ CenPersonaBean.T_NOMBRETABLA+ ".* " 
								+ ", 1 AS ORDENACION "
								+ "FROM " +  CenPersonaBean.T_NOMBRETABLA  + ", " + CenClienteBean.T_NOMBRETABLA + ", " + CenColegiadoBean.T_NOMBRETABLA;
			
			busquedaColegiados += " WHERE " + CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA + " = " + CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA 
								+ " AND " + CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA
								+ " AND " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION + " = " + CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION
								+ " AND(" + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION + " = " + idInstitucion
								+ " OR "  + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARACTER + " <> 'P' )" 
								+ " AND " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION + " IN (" 
									+ " SELECT " + CenInstitucionBean.T_NOMBRETABLA+"."+CenInstitucionBean.C_IDINSTITUCION
									+ " FROM " + CenInstitucionBean.T_NOMBRETABLA 
									+ " WHERE " + CenInstitucionBean.T_NOMBRETABLA+"."+CenInstitucionBean.C_CEN_INST_IDINSTITUCION + " = " + idInstitucion + ")";
			
			// Criterios de busqueda de no colegiados del consejo
			busquedaNoColegiados = " SELECT "+ CenPersonaBean.T_NOMBRETABLA+ ".* "
								+ ", 2 AS ORDENACION "
								+ "FROM " +  CenPersonaBean.T_NOMBRETABLA  + ", " + CenClienteBean.T_NOMBRETABLA + ", " + CenNoColegiadoBean.T_NOMBRETABLA;

			busquedaNoColegiados += " WHERE NOT EXISTS ( SELECT 1 FROM " + CenColegiadoBean.T_NOMBRETABLA  
									+ " WHERE " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION + " = " + CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION
									+ " AND " + CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA + " ) "
									
								+ " AND " + CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA 
								+ " AND(" + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION + " = " + idInstitucion
								+ " OR "  + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARACTER + " <> 'P' )"
								+ " AND " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION + " = " + CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDINSTITUCION + "(+)"
								+ " AND " + CenClienteBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA + " = " + CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDPERSONA  + "(+)"
								+ " AND " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_LETRADO + " = " + "0"
								+ " AND " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION + " = " + idInstitucion
								+ " AND EXISTS (SELECT " + CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_TIPO
									+ " FROM " + CenNoColegiadoBean.T_NOMBRETABLA
									+ " WHERE " + CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDINSTITUCION + " = " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION
									+ " AND " + CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_IDPERSONA + " = " + CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA
									+ " AND " + CenNoColegiadoBean.T_NOMBRETABLA+"."+CenNoColegiadoBean.C_TIPO + " = '1')";
			
			/* *
			 * Criterios de busqueda procedentes del formulario
			 * */
			String condiciones = "";
			// Criterio de busqueda especial para el nombre
			if (!bBusqueda) {
				condiciones += (nombre   != null && !nombre.equals("")   )? " AND "+ ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),P_NOMBRE) : " AND 1=1";
	       	}
	       	else {
	       		condiciones += (nombre   != null && !nombre.equals("")   )? " AND (" + P_NOMBRE     + " " + ComodinBusquedas.prepararSentenciaExacta(nombre.trim())    + ") " : " AND 1=1";
	       	}
			// Criterio de busqueda especial para los campos Apellido1 y Apellido2
			if (!formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {// Los dos campos rellenos
				if (bBusqueda) {
					condiciones += " AND ((("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido1().trim() )+")"+
					               " AND ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido2().trim() )+"))"+
								   " OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido1().trim()+" "+formulario.getApellido2().trim() )+"))";
				}else{
					condiciones += " AND ((("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1 )+")"+
				                   " AND ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2)+"))"+
					               " OR ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim()+"% %"+formulario.getApellido2().trim() ,CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1 )+"))";
				   	}
				   }else if (!formulario.getApellido1().trim().equals("")&&(formulario.getApellido2().trim().equals(""))) {//Apellido1 relleno
				if (bBusqueda) {
					condiciones += " AND (("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido1())+") OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" LIKE '"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+" %'))";
				}else{
					condiciones += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido1().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1 )+" OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+"||' '||"+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" LIKE '%"+UtilidadesBDAdm.validateChars(formulario.getApellido1().trim())+"%')) ";
				   	}
				   }else if (formulario.getApellido1().trim().equals("")&&(!formulario.getApellido2().trim().equals(""))) {//Apellido2 relleno
				if (bBusqueda) {
					condiciones += " AND (("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" "+ComodinBusquedas.prepararSentenciaExacta(formulario.getApellido2().trim() )+")"+
					               " OR ("+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" LIKE '% "+UtilidadesBDAdm.validateChars(formulario.getApellido2().trim()) +"' )) ";
				}else{
					condiciones += " AND (("+ComodinBusquedas.prepararSentenciaCompleta(formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2)+") "+
					               " OR ("+ComodinBusquedas.prepararSentenciaCompleta(" "+formulario.getApellido2().trim(),CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1)+ ")  )";
				}
			   	
			}
			//Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso
			// de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
			// la consulta adecuada.				
			if (nif!=null && !nif.equals("")){
				if (!bBusqueda) {
					if (ComodinBusquedas.hasComodin(nif)){
						
						condiciones +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(nif.trim(),P_NIF);
					}else{
						condiciones +=" AND "+ComodinBusquedas.prepararSentenciaNIF(nif,"UPPER(" + P_NIF + ")");
					}
		       	}
		       	else {
		       		condiciones += " and (UPPER(" + P_NIF     + ") " + ComodinBusquedas.prepararSentenciaExacta(nif.trim().toUpperCase())    + ") ";
		       	}
				
				
				
				
			}
			
			
			
			
			
			String orderBy =" ORDER BY ORDENACION, 3, 4, 2"; // ordenacion, apellidos1, apellidos2, nombre
		
			consulta = busquedaColegiados + condiciones + " union " + busquedaNoColegiados + condiciones + orderBy;
			
			Paginador paginador = new Paginador(consulta);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}else{
				int registrosPorPagina = paginador.getNumeroRegistrosPorPagina();	    		
	    		Vector datos = paginador.obtenerPagina(1);
	    	
			}
		
			
			return paginador;
		} 
//		catch (SIGAException e) { 
//			throw e; 	
//		}
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes "); 
		}
	}
	
	public Vector getDatosPersonaTag(String idInstitucion, String idPersona) throws ClsExceptions {
		Vector salida = new Vector();
		String consulta = new String();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		consulta = " select p.idpersona, p.apellidos1 || ' ' || p.apellidos2 || ', ' || p.nombre as NOMBRE, " +
			" f_siga_calculoncolegiado(c.idinstitucion,c.idpersona) as NCOLEGIADO " + 
			" from cen_persona p, cen_cliente c " +
			" where p.idpersona = c.idpersona and c.idinstitucion=:1";
		codigos.put(new Integer(2),idPersona);
		consulta += " and c.idpersona=:2";
		
		try {
			salida = this.selectGenericoBind(consulta,codigos);
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error en consulta de getDatosPersonaTag");
		}
		return salida;
	}
	public Long getIdPersona(String nifCif) throws ClsExceptions, SIGAException {
		Long idPersona = null;
		String consulta = new String();

		consulta = " SELECT IDPERSONA FROM CEN_PERSONA where " +
				" ltrim(UPPER(" +CenPersonaBean.C_NIFCIF+"),'0')='"+UtilidadesString.LTrim(nifCif.toUpperCase(),"0")+"'";
		RowsContainer rc = new RowsContainer(); 
        if (rc.find(consulta)) {

			if(rc.size()>1){
				throw new SIGAException("messages.general.errorUsuarioEfectivoDuplicado");
				
			}else if(rc.size()==1){
				Row fila = (Row) rc.get(0);
				idPersona=UtilidadesHash.getLong(fila.getRow(),CenPersonaBean.C_IDPERSONA);
	    	}
        }
        if(idPersona==null)
        	idPersona=new Long(-1);
		return idPersona;
	}
	public Hashtable getPersonYnColegiado (String idPersona, Integer idInstitucion) 
	{
	    Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion.toString());
		String sql = "Select p.idpersona, p.nombre, p.apellidos1, p.apellidos2, f_siga_calculoncolegiado(:1, p.idpersona) as NCOLEGIADO " +
	   		         " from cen_persona p ";
		codigos.put(new Integer(2),idPersona);
		sql += " where p.idpersona = :2";
		
	   Vector v;
		try {
			v = this.selectGenericoBind(sql,codigos);
			if (v != null && v.size() == 1) {
				Hashtable htCliente =(Hashtable) v.get(0);
				StringBuffer nombreCompleto = new StringBuffer();
				nombreCompleto.append(htCliente.get("NOMBRE"));
				nombreCompleto.append(" ");
				nombreCompleto.append(htCliente.get("APELLIDOS1"));
				nombreCompleto.append(" ");
				nombreCompleto.append(htCliente.get("APELLIDOS2"));
				
				
				htCliente.put("NOMCOLEGIADO", nombreCompleto.toString());
			   	return htCliente;
			}
		} 
		catch (ClsExceptions e) {
			e.printStackTrace();
		} 
		catch (SIGAException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * HAY QUE COMPLETAR ESTE METODO CON TODOS LOS CAMPOS(lA SELECT HABRIA QUE HACERLA CON EL JOIN DE CEN_COLEGIADO Y CEN_PERSONA) 
	 * 
	 * @param idPersona
	 * @param idInstitucion
	 * @return
	 */
	public CenPersonaBean getPersonaColegiado (Long idPersona, Integer idInstitucion) 
	{
	    Hashtable codigos = new Hashtable();
	    
	    
		codigos.put(new Integer(1),idInstitucion.toString());
		String sql = "Select p.idpersona, p.nombre, p.apellidos1, p.apellidos2, f_siga_calculoncolegiado(:1, p.idpersona) as NCOLEGIADO " +
	   		         " from cen_persona p ";
		codigos.put(new Integer(2),idPersona);
		sql += " where p.idpersona = :2";
		
		CenPersonaBean personaBean = null;
		CenColegiadoBean colegiadoBean;
		try {
			Vector v = this.selectGenericoBind(sql,codigos);
			if (v != null && v.size() == 1) {
				personaBean =  new CenPersonaBean();
				colegiadoBean =  new CenColegiadoBean();
				personaBean.setColegiado(colegiadoBean);
				Hashtable htCliente =(Hashtable) v.get(0);
				personaBean.setNombre((String)htCliente.get("NOMBRE"));
				personaBean.setApellido1((String)htCliente.get("APELLIDOS1"));
				personaBean.setApellido2((String)htCliente.get("APELLIDOS2"));
				colegiadoBean.setNColegiado((String)htCliente.get("NCOLEGIADO"));
				
			}
		} 
		catch (ClsExceptions e) {
			e.printStackTrace();
		} 
		catch (SIGAException e) {
			e.printStackTrace();
		}
		return personaBean;
	}
	
	
	/**
	 * Obtiene clientes repetidos segun los criterios de entrada
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public Vector getPersonasSimilares(MantenimientoDuplicadosForm formulario) throws ClsExceptions, SIGAException {

		Vector salida = null;
		String sqlClientes = "";
	  	
	  	// Acceso a BBDD
		RowsContainer rcClientes = null;
		try { 
		    
			String nombre = formulario.getNombre();
			String apellido1 = formulario.getApellido1();
			String apellido2 = formulario.getApellido2();
			String nif = formulario.getNifcif();
			
			//Tabla cen_persona
			String P_APELLIDOS1=CenPersonaBean.C_APELLIDOS1;
			String P_APELLIDOS2=CenPersonaBean.C_APELLIDOS2;
			String P_NOMBRE=CenPersonaBean.C_NOMBRE;
			String P_NIF=CenPersonaBean.C_NIFCIF;
			
			StringBuffer sqlSelect = new StringBuffer();
			sqlSelect.append("select "); 
			sqlSelect.append("p1.idpersona idpersona1, p1.nombre nombre1, p1.apellidos1 apellido11, p1.apellidos2 apellido21, p1.nifcif nifcif1, c1.ncolegiado ncolegiado1, ");
			sqlSelect.append("p2.idpersona idpersona2, p2.nombre nombre2, p2.apellidos1 apellido12, p2.apellidos2 apellido22, p2.nifcif nifcif2, c2.ncolegiado ncolegiado2 ");

			StringBuffer sqlFrom = new StringBuffer();
			sqlFrom.append(" from cen_persona p1, cen_persona p2, cen_colegiado c1, cen_colegiado c2 ");

			StringBuffer sqlWhere = new StringBuffer();
			sqlWhere.append(" where p1.idpersona < p2.idpersona "); 
			
			// Outerjoin con la tabla de colegiados (no solo buscamos duplicados de colegiados)
	       	sqlWhere.append(" and c1.idpersona(+) = p1.idpersona ");
	       	sqlWhere.append(" and c2.idpersona(+) = p2.idpersona ");
	       	
	       	// Colegiados del mismo colegio, con el mismo nColegiado, pero distinta persona
	       	if(formulario.getChkNumColegiado()){
		       	sqlWhere.append(" and c1.ncolegiado=c2.ncolegiado ");
		       	sqlWhere.append(" and c1.idinstitucion=c2.idinstitucion ");
	       	}
	       	
	       	String sql=sqlSelect.toString()+sqlFrom.toString()+sqlWhere.toString();
	        
			Vector similares=this.selectGenerico(sql);
	       	
			return similares;
		} 
//		catch (SIGAException e) { 
//			throw e; 	
//		}
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes "); 
		}
	}
	
	
	/**
	 * Devuelve una persona a partir de su idPersona  
	 * @version 1	 
	 * @param nifcif de la persona a buscar 
	 */	
	public CenPersonaBean getPersonaPorId (String idPersona) throws ClsExceptions, SIGAException{

		
		try {
			CenPersonaBean perBean = null;
			
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),idPersona);
			Vector personas = selectBind("WHERE idpersona = :1",codigos);
			
			if ((personas != null) && personas.size() == 1) {
				perBean = (CenPersonaBean)personas.get(0);
			}
			
			return perBean;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
	
}
	
