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

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsContrariosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsEJGDESIGNAAdm;
import com.siga.beans.ScsEJGDESIGNABean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BuscarDesignasForm;
import com.siga.gratuita.form.EjgDesignasForm;
import com.siga.gratuita.form.MaestroDesignasForm;


/**
 * @author ruben.fernandez
 * @since 21/2/2005
 */

/* 
 * Retocado DCG Febrero 2006 
 * 
 */
public class EjgDesignasAction extends MasterAction {
	
protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		String accion = miForm.getModo();
		String mapDestino = "exception";
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}
			
			else if (accion.equalsIgnoreCase("relacionarConEJG")) {
				mapDestino=relacionarConEJGExt (true, miForm, request);
			}		
			else if (accion.equalsIgnoreCase("borrarRelacionConEJG")) {
				mapDestino=relacionarConEJG (false, miForm, request);
		}	
			else return super.executeInternal(mapping, formulario, request, response);
		}
		catch (SIGAException e) {
			throw e;
		} 
		catch(Exception e){
			return mapping.findForward(mapDestino);
		}
		return mapping.findForward(mapDestino);
	}
	
	protected String relacionarConEJGExt (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException
	{
		String mapDestino;
		EjgDesignasForm miForm 	= (EjgDesignasForm)formulario;
		mapDestino=relacionarConEJG (true, miForm, request);
		
		// Recuperamos del formulario los datos del ejg que acabamos de relacionar
		String ejgAnio = miForm.getEjgAnio();
		String ejgInst = miForm.getEjgIdInstitucion();
		String ejgIdTipo = miForm.getEjgIdTipoEjg();
		String ejgNum = miForm.getEjgNumero();
		// Buscamos las posibles relaciones de la designacion con alguna asistencia 
		ScsAsistenciasAdm asisAdm = new ScsAsistenciasAdm(this.getUserBean(request));
		Hashtable datosAsis= new Hashtable();
		datosAsis = asisAdm.getRelacionadoEJG(ejgAnio, ejgNum, ejgInst, ejgIdTipo);
		Hashtable datosAsisDesig= new Hashtable();
		
		
		Hashtable miHash = new Hashtable ();
		UserTransaction tx = null;
		if (datosAsis!=null){
			
			try{
				// Si existe una asistencia relacionada recuperamos su clave
				
				String asisAnio = (String)datosAsis.get(ScsAsistenciasBean.C_ANIO);
				String asisInst = (String)datosAsis.get(ScsAsistenciasBean.C_IDINSTITUCION);
				String asisNum  = (String)datosAsis.get(ScsAsistenciasBean.C_NUMERO);
				tx=this.getUserBean(request).getTransaction();
				
				// Preparamos los datos para actualizar la asistencia
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDINSTITUCION, 	asisInst);
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_ANIO,			asisAnio);
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_NUMERO,			asisNum);
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_ANIO, 	miForm.getAnio());
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_NUMERO, miForm.getNumero());
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_TURNO, 	miForm.getIdTurno());

				tx.begin();
				
				String [] campos = {ScsAsistenciasBean.C_DESIGNA_ANIO, ScsAsistenciasBean.C_DESIGNA_NUMERO, ScsAsistenciasBean.C_DESIGNA_TURNO};
				ScsAsistenciasAdm asiAdm = new ScsAsistenciasAdm(this.getUserBean(request));  
				if (!asiAdm.updateDirect(miHash, null, campos)) {
					throw new ClsExceptions ("Error de la relacion Asistencia - Designa: "+asiAdm.getError() );
				}
				tx.commit();
			}catch (Exception e) 
			{
			    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
			} 
			
		}else{
			datosAsisDesig = asisAdm.getRelacionadoDesigna(miForm.getAnio(), miForm.getNumero(), this.getUserBean(request).getLocation(), miForm.getIdTurno());
			if (datosAsisDesig!=null){
				try{
					String asisAnio1 = (String)datosAsisDesig.get(ScsAsistenciasBean.C_ANIO);
					String asisInst1 = (String)datosAsisDesig.get(ScsAsistenciasBean.C_IDINSTITUCION);
					String asisNum1  = (String)datosAsisDesig.get(ScsAsistenciasBean.C_NUMERO);
					tx=this.getUserBean(request).getTransaction();
					
					// Preparamos los datos para actualizar la asistencia
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDINSTITUCION, 	asisInst1);
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_ANIO,			asisAnio1);
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_NUMERO,			asisNum1);
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGANIO, 	miForm.getEjgAnio());
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGNUMERO, miForm.getEjgNumero());
					UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGIDTIPOEJG, 	miForm.getEjgIdTipoEjg());
	
					tx.begin();
					
					String [] campos = {ScsAsistenciasBean.C_EJGANIO, ScsAsistenciasBean.C_EJGNUMERO, ScsAsistenciasBean.C_EJGIDTIPOEJG};
					ScsAsistenciasAdm asiAdm = new ScsAsistenciasAdm(this.getUserBean(request));  
					if (!asiAdm.updateDirect(miHash, null, campos)) {
						throw new ClsExceptions ("Error de la relacion Asistencia - Ejg: "+asiAdm.getError() );
					}
					tx.commit();
				}catch (Exception e) 
				{
				    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
				} 
			}
			
		}
		return mapDestino;
	}

protected String relacionarConEJG (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
		try {
			EjgDesignasForm miForm 	= (EjgDesignasForm)formulario;
			ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm(this.getUserBean(request)); 
			Hashtable miHash = new Hashtable ();

			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_IDINSTITUCION, 	miForm.getEjgIdInstitucion());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_ANIOEJG,			miForm.getEjgAnio());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_NUMEROEJG,			miForm.getEjgNumero());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_IDTIPOEJG,		miForm.getEjgIdTipoEjg());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_ANIODESIGNA,  	miForm.getAnio());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_NUMERODESIGNA,	miForm.getNumero());
			UtilidadesHash.set(miHash, ScsEJGDESIGNABean.C_IDTURNO,	miForm.getIdTurno());

			if (bCrear) {
				
				ScsDefendidosDesignaAdm defendidosDesignaAdm = new ScsDefendidosDesignaAdm(this.getUserBean(request));
				Hashtable hDefDesig=new Hashtable();
				hDefDesig.put(ScsDefendidosDesignaBean.C_IDINSTITUCION,this.getUserBean(request).getLocation());
				hDefDesig.put(ScsDefendidosDesignaBean.C_ANIO,miForm.getAnio());
				hDefDesig.put(ScsDefendidosDesignaBean.C_NUMERO,miForm.getNumero());
				hDefDesig.put(ScsDefendidosDesignaBean.C_IDTURNO,miForm.getIdTurno());
				
				Vector vDefendidos=defendidosDesignaAdm.select(hDefDesig);
				
				ScsUnidadFamiliarEJGAdm ejgAdm=new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
				
				
				// Los solicitantes del EJG los insertamos como interesados en la Designacion siempre que no existan ya.
				
				
				// Falta hacer la validacion de la persona, si no existe el idpersonajg pero existe una persona con el mismo nif o con el mismo nombre
				// y apellidos no se inserta
				ScsUnidadFamiliarEJGBean UFBean=new ScsUnidadFamiliarEJGBean();
				UFBean.setAnio(new Integer(miForm.getEjgAnio()));
				UFBean.setNumero(new Integer(miForm.getEjgNumero()));
				UFBean.setIdInstitucion(new Integer(miForm.getEjgIdInstitucion()));
				UFBean.setIdTipoEJG(new Integer(miForm.getEjgIdTipoEjg()));
				if (vDefendidos!=null && vDefendidos.size()>0){
					for (int i=0;i<vDefendidos.size();i++){
						UFBean.setIdPersona(((ScsDefendidosDesignaBean)vDefendidos.get(i)).getIdPersona());	
						UFBean.setSolicitante(new Integer(ClsConstants.DB_TRUE));	
				      try{
				      	ejgAdm.insert(UFBean);
				      }catch (Exception e){
				      	;
				      }
					}
				
				} 
				// Creamos la relacion
				
				//String [] campos = {ScsEJGBean.C_IDINSTITUCION,ScsEJGBean.C_ANIO,ScsEJGBean.C_NUMERO,ScsEJGBean.C_IDTIPOEJG,ScsEJGDESIGNABean.C_ANIODESIGNA, ScsEJGDESIGNABean.C_NUMERODESIGNA, ScsEJGDESIGNABean.C_IDTURNO};
				ejgDesignaAdm.insert(miHash);
				
			}
			else {
				// Borramos la relacion
				ejgDesignaAdm.delete(miHash);
			}

			
		}
		catch (Exception e) 
		{
			throw new SIGAException("Error de la relacion Asistencia - EJG",e,new String[] {"modulo.gratuita"});
		} 

		return "exito";
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		try {
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			//Recogemos de la pestanha la designa insertada o la que se quiere consultar
			//y los usamos para la consulta y además hacemos una hashtable y lo guardamos en session
			Hashtable designaActual = new Hashtable();
			Hashtable actual = new Hashtable();
			if ((String)request.getParameter("ANIO")==null)
				actual = (Hashtable)request.getSession().getAttribute("designaActual");
		
		
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_ANIO,(((String)request.getParameter("ANIO")==null)||(((String)request.getParameter("ANIO")).equals(""))?(String)actual.get("ANIO"):(String)request.getParameter("ANIO")));
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_NUMERO,(((String)request.getParameter("NUMERO")==null)||(((String)request.getParameter("NUMERO")).equals(""))?(String)actual.get("NUMERO"):(String)request.getParameter("NUMERO")));
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDTURNO,(((String)request.getParameter("IDTURNO")==null)||(((String)request.getParameter("IDTURNO")).equals(""))?(String)actual.get("IDTURNO"):(String)request.getParameter("IDTURNO")));			
			ScsContrariosDesignaAdm contrariosAdm = new ScsContrariosDesignaAdm(this.getUserBean(request));
			String consultaContrarios = " select ejg.idtipoejg idtipoejg, ejg.numejg codigo,ejg.numero numeroejg, des.anio anio, des.numero numero,des.idturno idturno, des.idinstitucion idinstitucion,"+
										" tur.nombre descripcionturno, "+UtilidadesMultidioma.getCampoMultidiomaSimple("tip.descripcion",this.getUserBean(request).getLanguage()) + " descripciontipoejg, "+UtilidadesMultidioma.getCampoMultidiomaSimple("tdic.descripcion",this.getUserBean(request).getLanguage()) + " estado, ejg.anio anioejg, tur.nombre turno"+
										" from scs_ejg ejg, scs_designa des, scs_turno tur, scs_tipoejg tip, scs_tipodictamenejg tdic,scs_ejgdesigna ejgDes"+
										" where ejgDes.Aniodesigna = des.anio"+
										" and ejgDes.Numerodesigna = des.numero"+
										" and ejgDes.Idturno = des.idturno"+   
										" and ejgDes.Idinstitucion=des.idinstitucion"+  
										" and ejgDes.Idinstitucion=ejg.idinstitucion"+    
										" and ejgDes.Anioejg=ejg.anio"+   
										" and ejgDes.Numeroejg=ejg.numero"+  
										" and ejgDes.Idtipoejg=ejg.idtipoejg"+    
										" and tip.idtipoejg = ejg.idtipoejg"+
										" and tdic.idtipodictamenejg (+) = ejg.idtipodictamenejg " +
										" and tdic.idtipodictamenejg (+) = ejg.idinstitucion " +
										" and tur.idinstitucion (+)= ejg.idinstitucion " +
										" and tur.idturno (+)= ejg.guardiaturno_idturno " +
										" and des.anio =" +(String)designaActual.get("ANIO")+
										" and des.numero ="+(String)designaActual.get("NUMERO")+
										" and des.idturno ="+(String)designaActual.get("IDTURNO")+
										" and des.idinstitucion="+usr.getLocation();
			
			Vector resultado = (Vector)contrariosAdm.ejecutaSelect(consultaContrarios);
			request.getSession().setAttribute("resultado",resultado);
			request.setAttribute("designaActual",designaActual);
			request.getSession().setAttribute("designaActual",designaActual);
			request.setAttribute("modo",(String)request.getParameter("modo"));
		} 
		catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "inicio";
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
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		String consultaDesigna =" where " 	+ ScsDesignaBean.C_IDINSTITUCION+ " = " + (String)usr.getLocation()	+
								" and "   	+ ScsDesignaBean.C_IDTURNO 		+ " = " + (String)ocultos.get(0) 	+	//el idturno
								" and " 	+ ScsDesignaBean.C_ANIO			+ " = " + (String)visibles.get(1)	+	//el anho
								" and "		+ ScsDesignaBean.C_NUMERO		+ " = " + (String)visibles.get(2);
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		ScsDesignaBean designa = (ScsDesignaBean)((Vector)designaAdm.select(consultaDesigna)).get(0);
		Hashtable elegido = (Hashtable)designa.getOriginalHash();
		UtilidadesHash.set(elegido,"MODO","Editar");		// para saber en que modo se mete desde la tabla
		request.setAttribute("idDesigna",elegido);
		return "edicion";
	}


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		return "nuevoRecarga";
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
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		String consultaDesigna =" where " 	+ ScsDesignaBean.C_IDINSTITUCION+ " = " + (String)usr.getLocation()	+
								" and "   	+ ScsDesignaBean.C_IDTURNO 		+ " = " + (String)ocultos.get(0) 	+	//el idturno
								" and " 	+ ScsDesignaBean.C_ANIO			+ " = " + (String)visibles.get(1)	+	//el anho
								" and "		+ ScsDesignaBean.C_NUMERO		+ " = " + (String)visibles.get(2);
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		ScsDesignaBean designa = (ScsDesignaBean)((Vector)designaAdm.select(consultaDesigna)).get(0);
		Hashtable elegido = (Hashtable)designa.getOriginalHash();
		UtilidadesHash.set(elegido,"MODO","Ver");		// para saber en que modo se mete desde la tabla
		request.setAttribute("idDesigna",elegido);
		return "edicion";
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

		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean elegido = new CenColegiadoBean();
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				request.setAttribute("nColegiado",numeroColegiado);
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
		}
		
		EjgDesignasForm miForm = (EjgDesignasForm)formulario;
		Hashtable miHash = (Hashtable)request.getSession().getAttribute("designaActual");
		try {
			request.setAttribute("ANIO",miHash.get("ANIO"));
			request.setAttribute("NUMERO",miHash.get("NUMERO"));
			request.setAttribute("IDTURNO",miHash.get("IDTURNO"));
		}catch (Exception e){
			request.setAttribute("ANIO","");
			request.setAttribute("NUMERO","");
			request.setAttribute("IDTURNO","");
		}
		
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
		
		UsrBean usr = (UsrBean)request.getAttribute("USRBEAN");
		HttpSession ses = (HttpSession)request.getSession();
		BuscarDesignasForm miform = (BuscarDesignasForm)formulario;
		Hashtable hash = (Hashtable)miform.getDatos();
		Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
		ScsDefendidosDesignaAdm defendidoAdm = new ScsDefendidosDesignaAdm(this.getUserBean(request));
		ScsPersonaJGAdm personaAdm = new ScsPersonaJGAdm (this.getUserBean(request));
		boolean ok = false;
		UserTransaction tx =null;
		
		//Comprobamos si la persona JG ya existia
		String hayPersona = (String)hash.get("HAYPERSONA");
		
		
		//Si existe, hay que modificar
		if (hayPersona.equalsIgnoreCase("true")){
			try{
				String consultaPersona= ""; //clausula where para seleccionar a la persona
				ScsPersonaJGBean personaJg = (ScsPersonaJGBean)((Vector)personaAdm.select(consultaPersona)).get(0);
				Hashtable personaModificada = (Hashtable)(personaJg.getOriginalHash()).clone();
				//	resto de campos cambiados en el formulario
				tx = usr.getTransaction();
			    tx.begin();
			    ok = personaAdm.update(personaModificada, personaJg.getOriginalHash());
				// resto de campos para insertar en defendidosDesigna
				ok = defendidoAdm.insert(designaActual);
				tx.commit();
			}catch(Exception e){
				throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
			}
		}
		else{
			try{
				// resto de campos para insertar en BBDD
				tx = usr.getTransaction();
			    tx.begin();
			    ok = personaAdm.insert(hash);
				//resto de campos para insertar en defendidosDesigna
				ok = defendidoAdm.insert(designaActual);
				tx.commit();
			}catch(Exception e){
				throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
			}
		}
		
//		hash.put("ANIO",(String)designaActual.get("ANIO"));
//		hash.put("NUMERO",(String)designaActual.get("NUMERO"));
//		hash.put("IDTURNO",(String)designaActual.get("IDTURNO"));
//		hash.put("IDINSTITUCION",usr.getLocation());
		
		if (ok)return exitoModal("",request);
		else return exito("",request);
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
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		Hashtable designaModificada = (Hashtable)miform.getDatos();
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		boolean ok=false;
		String consultaDesigna = " where " +ScsDesignaBean.C_ANIO+"="+(String)designaModificada.get("ANIO")+
									" and "+ScsDesignaBean.C_NUMERO+"="+(String)designaModificada.get("NUMERO")+" ";
		consultaDesigna += " "+GstDate.dateBetween0and24h(ScsDesignaBean.C_FECHAENTRADA,(String)designaModificada.get("FECHA")) +
									" and "+ScsDesignaBean.C_IDTURNO+"="+(String)designaModificada.get("IDTURNO")+" ";
		try{
			ScsDesignaBean designaAntigua = (ScsDesignaBean)((Vector)designaAdm.select(consultaDesigna)).get(0);
			ok=designaAdm.update(designaAntigua.getOriginalHash(),designaModificada);
		}catch(Exception e){
			 throwExcp("err.gen.modifica.colegiado",e,null);
		}
		return exito("",request);
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
		return "exito";
	}


}