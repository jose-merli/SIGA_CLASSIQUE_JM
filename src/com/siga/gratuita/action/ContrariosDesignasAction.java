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
import com.siga.beans.ScsContrariosDesignaAdm;
import com.siga.beans.ScsContrariosDesignaBean;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsProcuradorBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ContrariosDesignasForm;


/**
 * @author ruben.fernandez
 * @since 9/2/2005
 * @version 07/02/2006 (david.sanchezp): modificacion para incluir el combo procurador y arreglos varios.
 */

public class ContrariosDesignasAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		
		try {
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			} else {
				if (miForm.getModo().equalsIgnoreCase("editar2")) 
					return mapping.findForward( this.editar2(mapping, miForm, request,response));
				else
					return super.executeInternal(mapping, formulario, request, response);
			}
		}
		catch(Exception e){
			return mapping.findForward("exception");
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
		ses.removeAttribute("DATOSSOJ");
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
		
		ScsContrariosDesignaAdm contrariosAdm = new ScsContrariosDesignaAdm(this.getUserBean(request));
		String consultaContrarios = " select def.idpersona idpersona, per.nif nif, (per.nombre||' '|| per.apellido1 ||' '|| per.apellido2) nombre, def.nombrerepresentante representante,"+
		 							" def.idabogadocontrario, def.nombreabogadocontrario,"+
		 							"(select  pro.nombre || ' ' || pro.apellidos1 || ' ' || pro.apellidos2  from "+ ScsProcuradorBean.T_NOMBRETABLA+  " pro"+
		 								" where  pro."+ScsProcuradorBean.C_IDINSTITUCION+"= def."+ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR+
		 								" and pro."+ScsProcuradorBean.C_IDPROCURADOR+"= def."+ScsContrariosDesignaBean.C_IDPROCURADOR+") PROCURADOR"+
									" from "+ScsContrariosDesignaBean.T_NOMBRETABLA+" def, scs_personajg per "+
									" where def." + ScsContrariosDesignaBean.C_ANIO +"="+ ((hayDatos)?(String)request.getParameter("ANIO"):anio)+
									" and def." + ScsContrariosDesignaBean.C_NUMERO + " = " + ((hayDatos)?(String)request.getParameter("NUMERO"):numero)+
									" and def." + ScsContrariosDesignaBean.C_IDINSTITUCION + " = " + (String)usr.getLocation()+
									" and def." + ScsContrariosDesignaBean.C_IDTURNO + " = " + ((hayDatos)?(String)request.getParameter("IDTURNO"):idturno)+
									" and def.idinstitucion = per.idinstitucion and def.idpersona = per.idpersona ";
		
		Vector resultado = (Vector)contrariosAdm.ejecutaSelect(consultaContrarios);
		request.setAttribute("resultado",resultado);
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
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {
		return this.mostrarDatos("editar",formulario,request);	
	}

	protected String editar2(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		Vector resultado = null;
		Vector resultadoTF = new Vector();
		String idinstitucion = "";
		String idpersona = "";
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		ContrariosDesignasForm miForm = (ContrariosDesignasForm)formulario;
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
	
		try	{
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
		} catch (Exception e) {
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
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)		throws SIGAException  {	    
		return this.mostrarDatos("ver",formulario,request);
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
		
		request.getSession().removeAttribute("resultado");
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSSOJ");
		Vector resultadoTelefonos = new Vector();
		request.getSession().setAttribute("resultadoTelefonos",resultadoTelefonos);
		request.getSession().setAttribute("accion","Nuevo");
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
		ContrariosDesignasForm miForm = (ContrariosDesignasForm)formulario;
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
			if ((((String)miHash.get(ScsPersonaJGBean.C_IDPAIS))==null) || (((String)miHash.get(ScsPersonaJGBean.C_IDPAIS)).equalsIgnoreCase(""))) 
			{				
				ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
				admBeanTlf.prepararInsert(miHash);		
				tx=usr.getTransaction();
				tx.begin();
				
				if (admBeanTlf.insert(miHash))
		        {				
					tx.commit();
					/* Creamos la select que ejecutaremos para recuperar todos los teléfonos de la persona */
					String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + miHash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + miHash.get(ScsTelefonosPersonaBean.C_IDPERSONA);
					resultado.clear();					
					resultado = admBeanTlf.selectGenerico(sql);
					request.getSession().removeAttribute("resultadoTelefonos");
					request.getSession().setAttribute("resultadoTelefonos",resultado);
					return exitoModal("messages.inserted.success",request);
		        }	        
		        else
		        {
		        	//JTA se estaba saliendo sin cerrar la transaccion
		        	tx.rollback();
		        	return exito("messages.inserted.error",request);
		        }			
			}
			//	Si no es así significa que manos a insertar los datos de una persona
			else 
			{
				ScsPersonaJGAdm admBeanPersona =  new ScsPersonaJGAdm(this.getUserBean(request));
				ScsContrariosDesignaAdm contrariosDesignaAdm =  new ScsContrariosDesignaAdm(this.getUserBean(request));
				Vector designa = new Vector();
				
				admBeanPersona.prepararInsert(miHash);				
				miHash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miHash.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString()));
				
				//Recogemos de la pestanha la designa insertada o la que se quiere consultar
				Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");			

				// Realizamos la consulta
				Hashtable nuevoContrario = new Hashtable();
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_IDINSTITUCION, usr.getLocation());
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_IDTURNO, (String)designaActual.get("IDTURNO"));
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_ANIO, (String)designaActual.get("ANIO"));
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_NUMERO, (String)designaActual.get("NUMERO"));
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_IDPERSONA, (String)miHash.get("IDPERSONA"));
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_FECHAMODIFICACION, "sysdate");
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_USUMODIFICACION, Long.toString(usr.getIdPersona()));
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_OBSERVACIONES, (String)miHash.get("OBSERVACIONES"));
				UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE, (String)miHash.get("NOMBREREPRESENTANTE"));

				// Obtengo el idProcurador y la idInstitucion del procurador:
				Integer idProcurador, idInstitucionProcurador;
				idProcurador = null;
				idInstitucionProcurador = null;			
				String procurador = (String)miHash.get("PROCURADOR");
				if (procurador!=null && !procurador.equals("")){
					idProcurador = new Integer(procurador.substring(0,procurador.indexOf(",")));
					idInstitucionProcurador = new Integer(procurador.substring(procurador.indexOf(",")+1));
					UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_IDPROCURADOR, idProcurador);
					UtilidadesHash.set(nuevoContrario, ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR, idInstitucionProcurador);
				}
				
				tx=usr.getTransaction();
				tx.begin();
					
				String seleccionado = (String) ses.getAttribute("seleccionado");
				ses.removeAttribute("seleccionado");	
				if((seleccionado == null || !seleccionado.equals("si")) && (miForm.getNuevo().equalsIgnoreCase("si"))){
					ok = admBeanPersona.insert(miHash);
					nuevoContrario.put("IDPERSONA",miHash.get("IDPERSONA"));
				}else{
					nuevoContrario.put("IDPERSONA",miForm.getIdPersona());
					miHash.put("IDPERSONA",miForm.getIdPersona());
				}
		        ok2 = contrariosDesignaAdm.insert(nuevoContrario);
				
				tx.commit();
				
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
			
		} catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return exitoModal("messages.inserted.success", request);
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
				
		ContrariosDesignasForm miForm = (ContrariosDesignasForm) formulario;
		ScsPersonaJGAdm admBean =  new ScsPersonaJGAdm(this.getUserBean(request));
		ScsPersonaJGBean personaBean = new ScsPersonaJGBean();
		ScsTelefonosPersonaJGAdm admBeanTlf =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		ScsContrariosDesignaAdm contrariosAdm = new ScsContrariosDesignaAdm(this.getUserBean(request));
		ScsContrariosDesignaBean contrariosBean = new ScsContrariosDesignaBean ();
		
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
			
			
			String consultaContrario = " where "+ScsContrariosDesignaBean.C_IDINSTITUCION +"="+ (String)usr.getLocation()+
										" and anio ="+(String)designaActual.get("ANIO")+
										" and numero = "+(String)designaActual.get("NUMERO")+
										" and idturno = "+(String)designaActual.get("IDTURNO")+
										" and idpersona = "+ (String)hashBkp.get("IDPERSONA")+" ";
			
			personaBean = (ScsPersonaJGBean)((Vector)admBean.select(consultaPersona)).get(0);
			contrariosBean = (ScsContrariosDesignaBean)((Vector)contrariosAdm.select(consultaContrario)).get(0);
			hashContOld = (Hashtable)(contrariosBean.getOriginalHash().clone());
			hashContNew = (Hashtable)hashContOld.clone();
			hashContNew.put(ScsContrariosDesignaBean.C_OBSERVACIONES,miForm.getObservaciones());
			hashContNew.put(ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE,miForm.getNombreRepresentante());

			// Obtengo el idProcurador y la idInstitucion del procurador:
			Integer idProcurador, idInstitucionProcurador;
			idProcurador = null;
			idInstitucionProcurador = null;			
			String procurador = (String)miHash.get("PROCURADOR");
			if (procurador!=null && !procurador.equals("")){
				idProcurador = new Integer(procurador.substring(0,procurador.indexOf(",")));
				idInstitucionProcurador = new Integer(procurador.substring(procurador.indexOf(",")+1));
				UtilidadesHash.set(hashContNew, ScsContrariosDesignaBean.C_IDPROCURADOR, idProcurador);
				UtilidadesHash.set(hashContNew, ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR, idInstitucionProcurador);
			}			
			
			tx=usr.getTransaction();
			
			tx.begin();
			admBean.update(miHash,personaBean.getOriginalHash());
			contrariosAdm.update(hashContNew, hashContOld); 
			tx.commit();
			
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
		} catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
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
		UsrBean usr = null;
		ContrariosDesignasForm miform =  (ContrariosDesignasForm)formulario;
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		Hashtable hash = new Hashtable();
		Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
		UserTransaction tx = null;
		
		try {
			usr = (UsrBean)ses.getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			hash.put(ScsContrariosDesignaBean.C_IDINSTITUCION,designaActual.get("IDINSTITUCION"));
			hash.put(ScsContrariosDesignaBean.C_ANIO,designaActual.get("ANIO"));
			hash.put(ScsContrariosDesignaBean.C_NUMERO,designaActual.get("NUMERO"));
			hash.put(ScsContrariosDesignaBean.C_IDTURNO,designaActual.get("IDTURNO"));
			hash.put(ScsContrariosDesignaBean.C_IDPERSONA,(String)ocultos.get(0));
			ScsContrariosDesignaAdm contrariosAdm = new ScsContrariosDesignaAdm(this.getUserBean(request));			

			tx.begin();
			contrariosAdm.delete(hash);
			tx.commit();
		} catch(Exception e) {
			throwExcp("messages.deleted.error", e, tx);
		}
		return exitoRefresco("messages.deleted.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		
		ContrariosDesignasForm miform = (ContrariosDesignasForm)formulario;
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

	private String mostrarDatos(String accion, MasterForm formulario, HttpServletRequest request) throws SIGAException  {
	    
		HttpSession ses = (HttpSession)request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		ContrariosDesignasForm miform = (ContrariosDesignasForm)formulario;
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		
		String consultaPersona =" SELECT per.nombre nombre, per.idpersona idpersona, per.fechanacimiento fechanacimiento,"+
								" per.idpais idpais, per.nif nif, per.apellido1 apellido1, per.apellido2 apellido2,"+
								" per.direccion direccion, per.codigopostal codigopostal, per.idprofesion idprofesion,"+
								" per.regimen_conyugal regimen_conyugal, per.idprovincia idprovincia, per.idpoblacion idpoblacion,"+
								" per.idestadocivil idestadocivil, cont.nombrerepresentante nombrerepresentante,"+
								" cont.observaciones observaciones,"+
								ScsContrariosDesignaBean.C_IDPROCURADOR+","+
								ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR+
								" FROM scs_personajg per, scs_contrariosdesigna cont"+
								" WHERE per.idinstitucion = "+ usr.getLocation()+
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
		Hashtable hashPersonaContrarioDesigna = new Hashtable();
		try {
			hashPersonaContrarioDesigna = (Hashtable)((Vector)personaAdm.selectGenerico(consultaPersona)).get(0);
			resultadoTelefonos = (Vector)telefonosAdm.selectGenerico(consultaTelefonos);

			ses.setAttribute("accion",accion);
			ses.setAttribute("DATABACKUP",hashPersonaContrarioDesigna);
			ses.setAttribute("DATOSSOJ",hashPersonaContrarioDesigna);
			ses.setAttribute("resultadoTelefonos",resultadoTelefonos);
		} catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		}
		return "nuevo";
	}
	
	
}