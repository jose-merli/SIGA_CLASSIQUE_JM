package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaBean;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefendidosDesignasForm;


/**
 * @author ruben.fernandez
 * @since 9/2/2005
 */

public class DefendidosDesignasAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}else{
				if (miForm.getModo().equalsIgnoreCase("nuevoTelefono")) 
					return mapping.findForward( this.nuevo(mapping, miForm, request,response));
				else if (miForm.getModo().equalsIgnoreCase("editar2")) 
					return mapping.findForward( this.editar2(mapping, miForm, request,response));
				return super.executeInternal(mapping, formulario, request, response);
			}
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
		} 

	}
	
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN"); 
		//Recogemos de la pestanha la designa insertada o la que se quiere consultar
		//y los usamos para la consulta y además hacemos una hashtable y lo guardamos en session
		Hashtable designaActual = new Hashtable();
		UtilidadesHash.set(designaActual,ScsDesignaBean.C_ANIO, 				(String)request.getParameter("ANIO"));
		UtilidadesHash.set(designaActual,ScsDesignaBean.C_NUMERO, 				(String)request.getParameter("NUMERO"));
		UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
		UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDTURNO,				(String)request.getParameter("IDTURNO"));			
		
		String anio="",numero="", idturno="";
		boolean hayDatos= true;
		if((String)request.getParameter("ANIO")==null){
			hayDatos = false;
			designaActual = (Hashtable)ses.getAttribute("designaActual");
			anio = (String)designaActual.get("ANIO");
			numero = (String)designaActual.get("NUMERO");
			idturno = (String)designaActual.get("IDTURNO");
		}
		
		ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(this.getUserBean(request));
		String consultaDefendidos = " select def.idpersona idpersona, per.nif nif, (per.nombre||' '|| per.apellido1 ||' '|| per.apellido2) nombre, "+
									"  (perRepresentante.nombre || ' ' || perRepresentante.apellido1 || ' ' || perRepresentante.apellido2) representante " +
									" from "+ScsDefendidosDesignaBean.T_NOMBRETABLA+" def, scs_personajg per, scs_personajg perRepresentante "+
									" where def." + ScsDefendidosDesignaBean.C_ANIO +"="+ ((hayDatos)?(String)request.getParameter("ANIO"):anio)+
									" and def." + ScsDefendidosDesignaBean.C_NUMERO + " = " + ((hayDatos)?(String)request.getParameter("NUMERO"):numero)+
									" and def." + ScsDefendidosDesignaBean.C_IDINSTITUCION + " = " + (String)usr.getLocation()+
									" and def." + ScsDefendidosDesignaBean.C_IDTURNO + " = " + ((hayDatos)?(String)request.getParameter("IDTURNO"):idturno)+
									" and def.idinstitucion = per.idinstitucion and def.idpersona = per.idpersona and "+
									" per.idrepresentantejg = perRepresentante.idpersona(+) and "+  
									" per.idinstitucion = perRepresentante.Idinstitucion(+)  ";		
		
		Vector resultado = (Vector)defendidosAdm.ejecutaSelect(consultaDefendidos);
		request.setAttribute("resultado",resultado);
		ses.removeAttribute("DATOSSOJ");
		ses.setAttribute("designaActual",designaActual);
		request.setAttribute("modo",(String)request.getParameter("modo"));
		return "inicio";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		return "listadoTelefonos";
		
	}
	
	/**Funcion que transforma los datos de entrada para poder hacer la insercion a BBDD
	 * 
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se necesitan meter en BBDD
	 */
	protected Hashtable prepararHash (Hashtable datos){
		return datos;
	}
	
	/** 
	 *  Funcion que atiende la accion buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, 		
			  MasterForm formulario, 
			  HttpServletRequest request, 
			  HttpServletResponse response) throws ClsExceptions, SIGAException{
    
		        return "listado";
	}

	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {
		
		HttpSession ses = (HttpSession)request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		DefendidosDesignasForm miform = (DefendidosDesignasForm)formulario;
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		String consultaPersona ="select per.nombre nombre, per.idpersona idpersona, per.fechanacimiento fechanacimiento,"+
								" per.idpais idpais, per.nif nif, per.apellido1 apellido1, per.apellido2 apellido2,"+
								" per.direccion direccion, per.codigopostal codigopostal, per.idprofesion idprofesion,"+
								" per.regimen_conyugal regimen_conyugal, per.idprovincia idprovincia, per.idpoblacion idpoblacion,"+
								" per.idestadocivil idestadocivil, cont.nombrerepresentante nombrerepresentante,"+
								" cont.observaciones observaciones"+
								" from scs_personajg per, scs_defendidosdesigna cont"+
								" where per.idinstitucion = "+ usr.getLocation()+
								" and per.idpersona = "+ (String)ocultos.get(0)+
								" and cont.idinstitucion = per.idinstitucion"+
								" and cont.idpersona = per.idpersona";
		
		String consultaTelefonos = " select "+ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO+","+
									ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO+","+
									ScsTelefonosPersonaJGBean.C_IDINSTITUCION+","+
									ScsTelefonosPersonaJGBean.C_IDPERSONA+","+
									ScsTelefonosPersonaJGBean.C_IDTELEFONO+
									" from "+ScsTelefonosPersonaJGBean.T_NOMBRETABLA+
									" where "+ ScsTelefonosPersonaJGBean.C_IDINSTITUCION+" = "+ (String)usr.getLocation()+
									" and "+ ScsTelefonosPersonaJGBean.C_IDPERSONA+" = "+ (String)ocultos.get(0)+" ";
		
		ScsTelefonosPersonaJGAdm telefonosAdm = new ScsTelefonosPersonaJGAdm (this.getUserBean(request));
		ScsPersonaJGAdm personaAdm = new ScsPersonaJGAdm (this.getUserBean(request));
		Vector resultadoTelefonos = new Vector();
		Hashtable personaBean = new Hashtable();
		try{
			personaBean = (Hashtable)((Vector)personaAdm.selectGenerico(consultaPersona)).get(0);
			resultadoTelefonos = (Vector)telefonosAdm.selectGenerico(consultaTelefonos);
		}catch(Exception e){
			//por algun motivo no se ha podido hacer la consulta a la base de datos
		}
		ses.setAttribute("accion","Modificar");
		ses.setAttribute("DATABACKUP",personaBean);
		ses.setAttribute("DATOSSOJ",personaBean);
		ses.setAttribute("resultadoTelefonos",resultadoTelefonos);
		return "nuevo";
	}

	protected String editar2(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		Vector resultado = null;
		Vector resultadoTF = new Vector();
		String idinstitucion = "";
		String idpersona = "";
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		DefendidosDesignasForm miForm = (DefendidosDesignasForm)formulario;
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		
		try
		{
			ScsPersonaJGBean persona = new ScsPersonaJGBean();
			// Metemos los valores.
			persona.setIdPersona(Integer.valueOf(miForm.getIdPersona()));
			persona.setNif(miForm.getNif()); 
			persona.setNombre(miForm.getNombre()); 
			persona.setApellido1(miForm.getApellido1());  
			persona.setApellido2(miForm.getApellido2()); 
			persona.setDireccion(miForm.getDireccion()); 
			persona.setCodigoPostal(miForm.getCodigoPostal());
			// Hay que pasar de formato dd/mm/yyyy a yyyy/mm/aa hh:mm:ss
			String fn = miForm.getFechaNacimiento();
			fn = fn.substring(6,10)+"/"+fn.substring(3,5)+"/"+fn.substring(0,2)+ " 00:00:00";
			persona.setFechaNacimiento(fn);
			if(miForm.getIdEstadoCivilAux()!=null && !miForm.getIdEstadoCivilAux().equalsIgnoreCase("NULL"))
				persona.setIdEstadoCivil(Integer.valueOf(miForm.getIdEstadoCivilAux()));
			if(miForm.getRegimenConyugalAux()!=null && !miForm.getRegimenConyugalAux().equalsIgnoreCase("NULL"))
				persona.setRegimenConyugal(miForm.getRegimenConyugalAux());			
			persona.setIdPais(miForm.getIdPaisAux());
			if(miForm.getIdProvinciaAux()!=null && !miForm.getIdProvinciaAux().equalsIgnoreCase("NULL"))
				persona.setIdProvincia(miForm.getIdProvinciaAux());
			if(miForm.getIdPoblacionAux()!=null && !miForm.getIdPoblacionAux().equalsIgnoreCase("NULL"))
				persona.setIdPoblacion(miForm.getIdPoblacionAux());

			/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
			String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + usr.getLocation() + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miForm.getIdPersona();
			resultadoTF = admBeanTlf.selectGenerico(sql);
			request.setAttribute("resultadoTF",resultadoTF);
			request.setAttribute("resultado",persona);
			request.getSession().setAttribute("resultadoTelefonos",resultadoTF);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "nuevo";
	}

	
	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
	    
		HttpSession ses = (HttpSession)request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		DefendidosDesignasForm miform = (DefendidosDesignasForm)formulario;
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		String consultaPersona ="select per.nombre nombre, per.idpersona idpersona, per.fechanacimiento fechanacimiento,"+
								" per.idpais idpais, per.nif nif, per.apellido1 apellido1, per.apellido2 apellido2,"+
								" per.direccion direccion, per.codigopostal codigopostal, per.idprofesion idprofesion,"+
								" per.regimen_conyugal regimen_conyugal, per.idprovincia idprovincia, per.idpoblacion idpoblacion,"+
								" per.idestadocivil idestadocivil, cont.nombrerepresentante nombrerepresentante,"+
								" cont.observaciones observaciones"+
								" from scs_personajg per, scs_defendidosdesigna cont"+
								" where per.idinstitucion = "+ usr.getLocation()+
								" and per.idpersona = "+ (String)ocultos.get(0)+
								" and cont.idinstitucion = per.idinstitucion"+
								" and cont.idpersona = per.idpersona";
		
		String consultaTelefonos = " select "+ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO+","+
									ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO+","+
									ScsTelefonosPersonaJGBean.C_IDINSTITUCION+","+
									ScsTelefonosPersonaJGBean.C_IDPERSONA+","+
									ScsTelefonosPersonaJGBean.C_IDTELEFONO+
									" from "+ScsTelefonosPersonaJGBean.T_NOMBRETABLA+
									" where "+ ScsTelefonosPersonaJGBean.C_IDINSTITUCION+" = "+ (String)usr.getLocation()+
									" and "+ ScsTelefonosPersonaJGBean.C_IDPERSONA+" = "+ (String)ocultos.get(0)+" ";
		
		ScsTelefonosPersonaJGAdm telefonosAdm = new ScsTelefonosPersonaJGAdm (this.getUserBean(request));
		ScsPersonaJGAdm personaAdm = new ScsPersonaJGAdm (this.getUserBean(request));
		Vector resultadoTelefonos = new Vector();
		Hashtable personaBean = new Hashtable();
		try{
			personaBean = (Hashtable)((Vector)personaAdm.selectGenerico(consultaPersona)).get(0);
			resultadoTelefonos = (Vector)telefonosAdm.selectGenerico(consultaTelefonos);
		}catch(Exception e){
			//por algun motivo no se ha podido hacer la consulta a la base de datos
		}
		ses.setAttribute("accion","ver");
		ses.setAttribute("DATABACKUP",personaBean);
		ses.setAttribute("resultadoTelefonos",resultadoTelefonos);
		return "nuevo";
	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		
		//DefendidosDesignasForm miform = (DefendidosDesignasForm)formulario;
		//if ((miform.getModo()!=null)&&(miform.getModo().equalsIgnoreCase("nuevoTelefono")))return "insertarTelefono";
		
		//else return "nuevo";
		request.getSession().removeAttribute("resultado");
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSSOJ");
		Vector resultadoTelefonos = new Vector();
		request.getSession().setAttribute("resultadoTelefonos",resultadoTelefonos);
		request.getSession().setAttribute("accion","nuevo");
		return "nuevo";
	}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
		UserTransaction tx=null;		
		Hashtable miHash = new Hashtable();		
		DefendidosDesignasForm miForm = (DefendidosDesignasForm)formulario;
		Vector resultado = new Vector();
		ScsPersonaJGBean persona = new ScsPersonaJGBean();		
		boolean ok=false, ok2=false;
		
		try {
		
			miHash = miForm.getDatos();
			// Modif.Carlos
			String elNif = (String)miHash.get("NIF");
			while  (elNif.length() < 9) elNif = "0" + elNif;
			miHash.put("NIF",elNif);
			// Fin Modif.Carlos
			// Si vamos a insertar un teléfono no vendrán los datos de la persona en la hash

			tx=usr.getTransaction();
			tx.begin();

			if ((((String)miHash.get(ScsPersonaJGBean.C_IDPAIS))==null) || (((String)miHash.get(ScsPersonaJGBean.C_IDPAIS)).equalsIgnoreCase(""))) 
			{				
				ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
				admBeanTlf.prepararInsert(miHash);		
				
				if (admBeanTlf.insert(miHash))
		        {				
					/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
					String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaBean.C_IDPERSONA);
					resultado.clear();					
					resultado = admBeanTlf.selectGenerico(sql);
					request.getSession().removeAttribute("resultadoTelefonos");
					request.getSession().setAttribute("resultadoTelefonos",resultado);
		        }	        
		        else
		        {
		        	throw new ClsExceptions("Error al insertar");
		        }			
			}
			//	Si no es así significa que manos a insertar los datos de una persona
			else 
			{
				ScsPersonaJGAdm admBeanPersona =  new ScsPersonaJGAdm(this.getUserBean(request));
				ScsDefendidosDesignaAdm defendidosDesignaAdm =  new ScsDefendidosDesignaAdm(this.getUserBean(request));
				Vector designa = new Vector();
				
				admBeanPersona.prepararInsert(miHash);				
				miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));
				
				//Recogemos de la pestanha la designa insertada o la que se quiere consultar
				Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");			

				// Realizamos la consulta
				Hashtable nuevoDefendido = new Hashtable();
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_IDINSTITUCION, usr.getLocation());
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_IDTURNO, (String)designaActual.get("IDTURNO"));
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_ANIO, (String)designaActual.get("ANIO"));
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_NUMERO, (String)designaActual.get("NUMERO"));
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_IDPERSONA, (String)miHash.get("IDPERSONA"));
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_FECHAMODIFICACION, "sysdate");
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_USUMODIFICACION, Long.toString(usr.getIdPersona()));
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_OBSERVACIONES, (String)miHash.get("OBSERVACIONES"));
				UtilidadesHash.set(nuevoDefendido, ScsDefendidosDesignaBean.C_NOMBREREPRESENTANTE, (String)miHash.get("NOMBREREPRESENTANTE"));
									
				
				String seleccionado = (String) ses.getAttribute("seleccionado");
				ses.removeAttribute("seleccionado");	
				if((seleccionado == null || !seleccionado.equals("si")) && (miForm.getNuevo().equalsIgnoreCase("si"))){
					ok = admBeanPersona.insert(miHash);
					if (!ok) throw new ClsExceptions("Error de inserción");
					nuevoDefendido.put("IDPERSONA",miHash.get("IDPERSONA"));
				}else{
					nuevoDefendido.put("IDPERSONA",miForm.getIdPersona());
					miHash.put("IDPERSONA",miForm.getIdPersona());
				}
		        ok2 = defendidosDesignaAdm.insert(nuevoDefendido);
				if (!ok2) throw new ClsExceptions("Error de inserción");
				
				/* 
				   Se almacena en DATABACKUP el registro recién modificado puesto que permanecemos en la pantalla
				   de mantenimiento y sino se volviese a realizar alguna modificación daría un error.
				*/
				
				resultado.clear();
				resultado = admBeanPersona.selectPorClave(miHash);
				persona = (ScsPersonaJGBean)resultado.get(0);
				
				resultado.clear();			
				
				ses.setAttribute("resultadoTelefonos",resultado);
				ses.setAttribute("accion","Modificar");
				Hashtable databackup = (Hashtable)admBeanPersona.beanToHashTable(persona);
				databackup.put("OBSERVACIONES",miForm.getObservaciones());
				databackup.put("REPRESENTANTE",miForm.getNombreRepresentante());
				ses.setAttribute("DATABACKUP",databackup);		
				ses.setAttribute("DATOSSOJ",admBeanPersona.beanToHashTable(persona));
			}

			
			tx.commit();
			
		}catch(Exception e){
			 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}
		
		return exitoModal("messages.inserted.success", request);
		
		//return "nuevo";
		/*request.setAttribute("modal","1");
		
		return "exito";*/
	}

	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,HttpServletRequest request, HttpServletResponse response)throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
		UserTransaction tx=null;
				
		DefendidosDesignasForm miForm = (DefendidosDesignasForm) formulario;
		ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
		ScsPersonaJGBean personaBean = new ScsPersonaJGBean();
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(this.getUserBean(request));
		ScsDefendidosDesignaBean defendidosBean = new ScsDefendidosDesignaBean ();
		
		Hashtable miHash = new Hashtable();
		
		boolean ok = false, ok2 = false;
		
		try {	
			
			miHash = miForm.getDatos();
			// Modif.Carlos
			String elNif = (String)miHash.get("NIF");
			while  (elNif.length() < 9) elNif = "0" + elNif;
			miHash.put("NIF",elNif);
			// Fin Modif.Carlos
			
			// Convertimos la fecha al formato adecuado
			miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));
			
			Hashtable hashBkp = new Hashtable();
			Hashtable hashContOld = new Hashtable();
			Hashtable hashContNew = new Hashtable();
			Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
			hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			request.getSession().removeAttribute("DATABACKUP");
			
			String consultaPersona = " where idinstitucion="+(String)usr.getLocation()+" and idpersona="+(String)hashBkp.get("IDPERSONA")+" ";
			
			String consultaDefendido = " where "+ScsDefendidosDesignaBean.C_IDINSTITUCION +"="+ (String)usr.getLocation()+
										" and anio ="+(String)designaActual.get("ANIO")+
										" and numero = "+(String)designaActual.get("NUMERO")+
										" and idturno = "+(String)designaActual.get("IDTURNO")+
										" and idpersona = "+ (String)hashBkp.get("IDPERSONA")+" ";
			
			personaBean = (ScsPersonaJGBean)((Vector)admBean.select(consultaPersona)).get(0);
			defendidosBean = (ScsDefendidosDesignaBean)((Vector)defendidosAdm.select(consultaDefendido)).get(0);
			hashContOld = (Hashtable)(defendidosBean.getOriginalHash().clone());
			hashContNew = (Hashtable)hashContOld.clone();
			hashContNew.put(ScsDefendidosDesignaBean.C_OBSERVACIONES,miForm.getObservaciones());
			hashContNew.put(ScsDefendidosDesignaBean.C_NOMBREREPRESENTANTE,miForm.getNombreRepresentante());
			
			tx=usr.getTransaction();
			tx.begin();
			
			ok = admBean.update(miHash,personaBean.getOriginalHash());
			if (!ok) throw new ClsExceptions("Error de actualización");
			ok2 = defendidosAdm.update(hashContNew, hashContOld); 
			if (!ok2) throw new ClsExceptions("Error de actualización");
			
			
			/* Se almacena en DATABACKUP el registro recién modificado puesto que permanecemos en la pantalla
			   de mantenimiento y sino se volviese a realizar alguna modificación daría un error.
			*/
			Vector resultado = admBean.selectPorClave(miHash);
			ScsPersonaJGBean persona = (ScsPersonaJGBean)resultado.get(0);
			
			/* Ahora vamos a recuperar de la base de datos los teléfonos, para eso previamente borramos el vector de resultados */
			resultado.clear();
			/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
			String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA);
			resultado = admBeanTlf.selectGenerico(sql);
			
			request.getSession().setAttribute("resultadoTelefonos",resultado);
			request.getSession().setAttribute("accion","editar");
			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(persona));			
			request.getSession().setAttribute("DATOSSOJ",admBean.beanToHashTable(persona));

			tx.commit();
		
		}catch(Exception e){
			 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}
		
		return exitoModal("messages.updated.success",request);
		

	}

	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
			
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		DefendidosDesignasForm miform =  (DefendidosDesignasForm)formulario;
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		Hashtable hash = new Hashtable();
		Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
		boolean ok = false;
		UserTransaction tx=null;
		try{
			tx=usr.getTransaction();
			tx.begin();

			hash.put(ScsDefendidosDesignaBean.C_IDINSTITUCION,designaActual.get("IDINSTITUCION"));
			hash.put(ScsDefendidosDesignaBean.C_ANIO,designaActual.get("ANIO"));
			hash.put(ScsDefendidosDesignaBean.C_NUMERO,designaActual.get("NUMERO"));
			hash.put(ScsDefendidosDesignaBean.C_IDTURNO,designaActual.get("IDTURNO"));
			hash.put(ScsDefendidosDesignaBean.C_IDPERSONA,(String)ocultos.get(0));
			ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(this.getUserBean(request));
			ok = defendidosAdm.delete(hash);
			if (!ok) throw new ClsExceptions("Error de borrado");
		
			tx.commit();
			
		}catch(Exception e){
			 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}

		return exitoRefresco("messages.deleted.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		
		DefendidosDesignasForm miform = (DefendidosDesignasForm)formulario;
		Vector resultadoNif = new Vector();
		if ((miform.getNif()!=null)&&(miform.getNif().equalsIgnoreCase(""))){
			try{
				ScsPersonaJGAdm personaAdm = new ScsPersonaJGAdm(this.getUserBean(request));
				String condicionPersona = " where "+ ScsPersonaJGBean.C_NIF +"="+miform.getNif()+" ";
				resultadoNif = (Vector)personaAdm.select(condicionPersona);
			}catch(Exception e){
				// No se ha podido realizar un consulta a la BBDD
			}
		}
		request.setAttribute("resultadoNif", resultadoNif);
		return "resultadoNif";
	}

}