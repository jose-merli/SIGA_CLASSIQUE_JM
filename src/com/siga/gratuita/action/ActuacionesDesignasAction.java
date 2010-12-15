package com.siga.gratuita.action;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.beans.ScsAcreditacionAdm;
import com.siga.beans.ScsAcreditacionBean;
import com.siga.beans.ScsAcreditacionProcedimientoBean;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsActuacionDesignaBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.beans.ScsDesignasLetradoBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsProcedimientosAdm;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActuacionesDesignasForm;


/**
 * @author ruben.fernandez
 * @since 9/2/2005
 * @version 27/01/2006 (david.sanchezp): modificaciones para incluir nuevos combos y retoque de codigo.
 */

public class ActuacionesDesignasAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		String accion = miForm.getModo();
		try{
			if(accion!=null && accion.equalsIgnoreCase("EditarDesdeInforme")){
				request.getSession().removeAttribute("designaActual");
				miForm.setModo("editar");
			}
			if(accion!=null && accion.equalsIgnoreCase("ConsultarDesdeInforme")){
				request.getSession().removeAttribute("designaActual");
				miForm.setModo("ver");
			}
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			
			}else return super.executeInternal(mapping, formulario, request, response);
		
		} catch (SIGAException e) {
			throw e;
		} catch(ClsExceptions e) {
			throw new SIGAException("ClsException no controlada -> " + e.getMessage() ,e);
		} catch (Exception e){
			throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
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
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN"); 
		HttpSession ses = request.getSession();
		Vector resultado = new Vector();
		boolean esFichaColegial = false;
		
		try {
			//Miramos si tenemos un parametro rellenado que indica que venimos del menu de ficha colegial.
			if ((request.getParameter("deDonde")!=null) && (!request.getParameter("deDonde").equals("")))
				esFichaColegial = true; 
			
			//Obtengo los datos de sesion, sino busca en el request:
			Hashtable hash = new Hashtable();
			hash = (Hashtable)request.getSession().getAttribute("designaActual");
			String idTurno="",anio="", numero="", idPersona="", idInstitucion="";
	
			//Si venimos del menu de letrado tengo el idpersona:
			if (esFichaColegial) {
				if (request.getParameter("IDPERSONA")!=null)
					idPersona = request.getParameter("IDPERSONA");
				else
					idPersona = (String)hash.get("IDPERSONA");
			}

			//Obtenemos datos de la pestanha:
			if (request.getParameter("ANIO")!=null) {
				anio = request.getParameter("ANIO");
				ses.setAttribute("DESIGNA_ANIO",request.getParameter("ANIO"));
			} else { 
				anio = (String)hash.get("ANIO");
			}

			if (request.getParameter("NUMERO")!=null) {
				numero = request.getParameter("NUMERO");
				ses.setAttribute("DESIGNA_NUMERO",(String)request.getParameter("NUMERO"));
			} else {
				numero = (String)hash.get("NUMERO");
			}

			if (request.getParameter("IDTURNO")!=null) {
				idTurno = request.getParameter("IDTURNO");
				ses.setAttribute("DESIGNA_IDTURNO",(String)request.getParameter("IDTURNO"));
			} else { 
				idTurno = (String)hash.get("IDTURNO");
			}

			if (request.getParameter("IDINSTITUCION")!=null) {
				idInstitucion = request.getParameter("IDINSTITUCION");
				ses.setAttribute("DESIGNA_IDINSTITUCION",(String)request.getParameter("IDINSTITUCION"));
			} else {
				idInstitucion = (String)(String)hash.get("IDINSTITUCION");
			}

 			
			Hashtable designaActual = new Hashtable();
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_ANIO, 			anio);
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_NUMERO, 			numero);
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDINSTITUCION,	idInstitucion);
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDTURNO,			idTurno);				
			UtilidadesHash.set(designaActual,ScsDesignasLetradoBean.C_IDPERSONA, idPersona);
			
			if (esFichaColegial){// Si accedemos desde Ficha Colegial
	           // Recogemos de la pestanha la designa insertada o la que se quiere consultar
			   //y los usamos para la consulta y además hacemos una hashtable y lo guardamos en session
	
				ScsActuacionDesignaAdm actuacionDesignaAdm = new ScsActuacionDesignaAdm(this.getUserBean(request));
			    String consultaActuacion  = " SELECT  act."+ScsActuacionDesignaBean.C_IDINSTITUCION+" idinstitucion,"+
												     "act."+ScsActuacionDesignaBean.C_IDTURNO+" idturno,"+
												     "act."+ScsActuacionDesignaBean.C_ANIO+" anio,"+
												     "act."+ScsActuacionDesignaBean.C_NUMERO+" numero,"+
												     "act."+ScsActuacionDesignaBean.C_NUMEROASUNTO+" numeroasunto,"+
												     "act."+ScsActuacionDesignaBean.C_FECHA+" fecha,"+
												     "act."+ScsActuacionDesignaBean.C_ANULACION+" anulacion,"+
												     "pro.nombre nombreprocedimiento,"+
												     "pro.idprocedimiento idprocedimiento,"+
												     "acr."+ScsAcreditacionBean.C_IDACREDITACION+" idacreditacion,"+
												     "acr."+ScsAcreditacionBean.C_DESCRIPCION+" nombreacreditacion,"+
												     "act."+ScsActuacionDesignaBean.C_FECHAJUSTIFICACION+" fechajustificacion,"+
												     "act."+ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL+" acuerdoextrajudicial,"+
												     "act."+ScsActuacionDesignaBean.C_IDFACTURACION+" IDFACTURACION,"+
												     /**pdm INC-2618**/
														"(select "+FcsFacturacionJGBean.C_NOMBRE+"||' ('||TO_CHAR("+FcsFacturacionJGBean.C_FECHADESDE+",'DD/MM/YYYY')||'-'||TO_CHAR("+FcsFacturacionJGBean.C_FECHAHASTA+",'DD/MM/YYYY')||')'"+
														" from "+FcsFacturacionJGBean.T_NOMBRETABLA+
													    " where "+FcsFacturacionJGBean.C_IDINSTITUCION+" = "+idInstitucion+
													    "   and "+FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_IDFACTURACION+" = act."+ScsActuacionDesignaBean.C_IDFACTURACION+") nombrefacturacion,"+
													/** pdm INC-xxx1**/
//													 "DECODE(act."+ScsActuacionDesignaBean.C_VALIDADA+",'1','Si','No') validada"+	
													 "act." + ScsActuacionDesignaBean.C_VALIDADA + " validada " +	
													 
											" FROM " + ScsActuacionDesignaBean.T_NOMBRETABLA+" act,"+ScsProcedimientosBean.T_NOMBRETABLA+" pro"+
											" , " + ScsAcreditacionProcedimientoBean.T_NOMBRETABLA+" acp,"+ScsAcreditacionBean.T_NOMBRETABLA+" acr"+
											
											" WHERE act." + ScsActuacionDesignaBean.C_IDINSTITUCION + " = " + idInstitucion + 
	                                          " AND act." + ScsActuacionDesignaBean.C_IDTURNO       + " = " + idTurno +
	                                          " AND act." + ScsActuacionDesignaBean.C_ANIO          + " = " + anio +
	                                          " AND act." + ScsActuacionDesignaBean.C_NUMERO        + " = " + numero +
	                                          " AND act." + ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO + " = " + idPersona +
	
	                                          " AND pro." + ScsProcedimientosBean.C_IDPROCEDIMIENTO + " = acp." + ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO +
	                                          " AND pro." + ScsProcedimientosBean.C_IDINSTITUCION + " = acp." + ScsAcreditacionProcedimientoBean.C_IDINSTITUCION +
	                                          " AND pro." + ScsProcedimientosBean.C_IDINSTITUCION + " = act." + ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO +
	                                          " AND pro." + ScsProcedimientosBean.C_IDPROCEDIMIENTO + " = act." + ScsActuacionDesignaBean.C_IDPROCEDIMIENTO +
	                                          " AND acr." + ScsAcreditacionProcedimientoBean.C_IDACREDITACION + " = act." + ScsActuacionDesignaBean.C_IDACREDITACION +
	                                          " AND acr." + ScsAcreditacionBean.C_IDACREDITACION + " = acp." + ScsAcreditacionProcedimientoBean.C_IDACREDITACION +
											  
											" ORDER BY act." + ScsActuacionDesignaBean.C_FECHA + " DESC, numeroasunto asc  ";
				
				resultado = (Vector)actuacionDesignaAdm.ejecutaSelect(consultaActuacion);
				request.getSession().setAttribute("resultado",resultado);
				request.getSession().setAttribute("Modo","Edicion");
				if ((String)request.getParameter("IDTURNO")!=null)
					ses.setAttribute("designaActual",designaActual);
			} //	Si accedemos desde SJCS
			else{
				ScsActuacionDesignaAdm actuacionDesignaAdm = new ScsActuacionDesignaAdm(this.getUserBean(request));
				String consultaContrarios = " SELECT act."+ScsActuacionDesignaBean.C_IDINSTITUCION+" idinstitucion,"+
													"act."+ScsActuacionDesignaBean.C_IDTURNO+" idturno,"+
													"act."+ScsActuacionDesignaBean.C_ANIO+" anio,"+
													"act."+ScsActuacionDesignaBean.C_NUMERO+" numero,"+
													"act."+ScsActuacionDesignaBean.C_NUMEROASUNTO+" numeroasunto,"+
													"act."+ScsActuacionDesignaBean.C_FECHA+" fecha,"+
													"act."+ScsActuacionDesignaBean.C_ANULACION+" anulacion,"+
													"pro.nombre nombreprocedimiento,"+
													"pro.idprocedimiento idprocedimiento,"+
													"act."+ScsActuacionDesignaBean.C_FECHAJUSTIFICACION+" fechajustificacion,"+
													"act."+ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL+" acuerdoextrajudicial,"+
													"act."+ScsActuacionDesignaBean.C_IDFACTURACION+" IDFACTURACION,"+
													" acre."+ScsAcreditacionBean.C_DESCRIPCION+" AS nombreacreditacion, "+
													"deslet."+ScsDesignasLetradoBean.C_IDPERSONA+" AS IDPERSONA,"+
													/**pdm INC-2618**/
													"(select "+FcsFacturacionJGBean.C_NOMBRE+"||' ('||TO_CHAR("+FcsFacturacionJGBean.C_FECHADESDE+",'DD/MM/YYYY')||'-'||TO_CHAR("+FcsFacturacionJGBean.C_FECHAHASTA+",'DD/MM/YYYY')||')'"+
													" from "+FcsFacturacionJGBean.T_NOMBRETABLA+
												    " where "+FcsFacturacionJGBean.C_IDINSTITUCION+" = "+idInstitucion+
												    "   and "+FcsFacturacionJGBean.C_IDFACTURACION+" = act."+ScsActuacionDesignaBean.C_IDFACTURACION+") nombrefacturacion,"+
													/**/
												    /** pdm INC-xxx1**/
													// "DECODE(act."+ScsActuacionDesignaBean.C_VALIDADA+",'1','Si','No') validada"+	
													 "act." + ScsActuacionDesignaBean.C_VALIDADA + " validada " +	
													/**/
											" FROM "+ScsActuacionDesignaBean.T_NOMBRETABLA+" act,"+ScsProcedimientosBean.T_NOMBRETABLA+" pro,"+
													 ScsDesignasLetradoBean.T_NOMBRETABLA+" deslet,"+
													 ScsAcreditacionBean.T_NOMBRETABLA+" acre," +
													ScsAcreditacionProcedimientoBean.T_NOMBRETABLA+" a"+
											" WHERE  act."+ScsActuacionDesignaBean.C_IDINSTITUCION+" = "+idInstitucion+
												" and act."+ScsActuacionDesignaBean.C_IDTURNO+" = "+idTurno+
												" and act."+ScsActuacionDesignaBean.C_ANIO+" = "+anio+
												" and act."+ScsActuacionDesignaBean.C_NUMERO+" = "+numero+
												" and pro.idinstitucion = act."+ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO + 
												" and pro.idprocedimiento = act."+ScsActuacionDesignaBean.C_IDPROCEDIMIENTO+" " +
												//JOIN
												" and deslet."+ScsDesignasLetradoBean.C_IDINSTITUCION+" = act."+ScsActuacionDesignaBean.C_IDINSTITUCION+" " +
												" and deslet."+ScsDesignasLetradoBean.C_IDTURNO+" = act."+ScsActuacionDesignaBean.C_IDTURNO+" " +
												" and deslet."+ScsDesignasLetradoBean.C_ANIO+" = act."+ScsActuacionDesignaBean.C_ANIO+" " +
												" and deslet."+ScsDesignasLetradoBean.C_NUMERO+" = act."+ScsActuacionDesignaBean.C_NUMERO+" " +
												" and deslet."+ScsDesignasLetradoBean.C_FECHARENUNCIA+" IS NULL"+
												" and acre." + ScsAcreditacionBean.C_IDACREDITACION + " = act." + ScsActuacionDesignaBean.C_IDACREDITACION + " (+) " + 
												" and a."+ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO+" = pro.idprocedimiento "+
												" and a."+ScsAcreditacionProcedimientoBean.C_IDINSTITUCION+" = "+idInstitucion+
												" and a."+ScsAcreditacionProcedimientoBean.C_IDACREDITACION+" = acre."+ScsAcreditacionBean.C_IDACREDITACION +
											" ORDER BY fecha , numeroasunto asc ";
				
				resultado = (Vector)actuacionDesignaAdm.ejecutaSelect(consultaContrarios);
				if (!resultado.isEmpty())
					idPersona = (String)((Hashtable)resultado.get(0)).get("IDPERSONA");
				
				request.getSession().setAttribute("resultado",resultado);
				if ((String)request.getParameter("IDTURNO")!=null)
					ses.setAttribute("designaActual",designaActual);
				request.setAttribute("modo",(String)request.getParameter("modo"));
				String modo=(String)request.getParameter("modo");
			}
			
			// CONSULTAMOS LA DESIGNA PARA VER SI ESTA ANULADA Y SI ES ASI NO MOSTRAR EL BOTÓN NUEVO PARA AÑADIR ACTUACIONES
			ScsDesignaAdm designaAdm = new ScsDesignaAdm(this.getUserBean(request));
			Vector v = designaAdm.selectByPK(designaActual);
			ScsDesignaBean bean = new ScsDesignaBean();
			String anulado="0";
			if(v!=null &&  v.size()>0)
			{
				bean = (ScsDesignaBean)v.get(0);
				if(bean.getFechaAnulacion() != null && !bean.getFechaAnulacion().trim().equals(""))
					anulado = "1";
			}
			request.setAttribute("anulada",anulado);
	
			// CONSULTAMOS LA DESIGNA PARA VER SI ESTA CON FECHA RENUNCIA DISTINTO DE NULL
			//Obtenemos el idpersona de la request o de la select segun vengamos de letrado o SJCS:
			//Nota: Puede que este vacio si no tenemos registros y venimos de SJCS.
			String renuncia="0";
			if (idPersona!=null && !idPersona.equals("")) {
				ScsDesignasLetradoAdm admDesignasLetrado = new ScsDesignasLetradoAdm(this.getUserBean(request)); 
				Vector v2 = admDesignasLetrado.select(designaActual);
				ScsDesignasLetradoBean beanDesignasLetrado = new ScsDesignasLetradoBean();
				if(v2!=null &&  v2.size()>0)
				{
					beanDesignasLetrado = (ScsDesignasLetradoBean)v2.get(0);
					if(beanDesignasLetrado.getFechaRenuncia() != null && !beanDesignasLetrado.getFechaRenuncia().trim().equals(""))
						renuncia = "1";
				}
			}
			request.setAttribute("renuncia",renuncia);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		return "listado";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		return "nuevoRecarga";
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
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)throws SIGAException  {
		request.setAttribute("MODO_ANTERIOR","EDITAR");
		return this.mostrarDatos(formulario,request);
}

	/** 
	 * Metodo que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {
		request.setAttribute("MODO_ANTERIOR","VER");
		return this.mostrarDatos(formulario,request);
	}
	
	
	/**
	 * Prepara los datos para la modal en edicion o consulta:
	 * @param modo: Ver o Editar
	 * @param formulario
	 * @param request
	 * @return
	 * @throws SIGAException
	 */
	private String mostrarDatos(MasterForm formulario,HttpServletRequest request) throws SIGAException  {
	    
		HttpSession ses = (HttpSession)request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		
		try {			
			
			ActuacionesDesignasForm miform = (ActuacionesDesignasForm)formulario;

			String anio = null;
			String idInstitucion = null;
			String idTurno = null;
			String numero = null;
			String numeroAsunto = null;
			Hashtable designaActual = (Hashtable) ses.getAttribute("designaActual");
			if(designaActual!=null){
				Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
				numeroAsunto = (String)visibles.get(1);
				anio = (String)designaActual.get("ANIO");
				idInstitucion= (String)usr.getLocation();
				idTurno = (String)designaActual.get("IDTURNO");
				numero = (String)designaActual.get("NUMERO");
				
			}else{
				numeroAsunto = miform.getNactuacion();
				anio = miform.getAnio();
				idInstitucion= (String)usr.getLocation();
				idTurno = miform.getIdTurno();
				numero = miform.getNumero();
				
				
			}
						

			Hashtable hashEJG = new Hashtable();
			ScsEJGAdm ejgAdm = new ScsEJGAdm (this.getUserBean(request));		
			Hashtable hashDatosDesigna= new Hashtable();			
			UtilidadesHash.set(hashDatosDesigna,ScsDesignaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(hashDatosDesigna,ScsDesignaBean.C_ANIO, anio);
			UtilidadesHash.set(hashDatosDesigna,ScsDesignaBean.C_NUMERO, numero);
			UtilidadesHash.set(hashDatosDesigna,ScsDesignaBean.C_IDTURNO, idTurno);
			UtilidadesHash.set(hashDatosDesigna,"VISIBLE",numeroAsunto);		
			
			ScsActuacionDesignaAdm designaAdm = new ScsActuacionDesignaAdm (this.getUserBean(request));	
			//consultamos las designas
			Hashtable hashDesigna =  (Hashtable)(designaAdm.getConsultaDesigna(hashDatosDesigna, request)).get(0);		
			UtilidadesHash.set(hashEJG,ScsEJGBean.C_IDINSTITUCION,(String)hashDesigna.get("IDINSTITUCION"));
			UtilidadesHash.set(hashEJG,ScsEJGBean.C_NUMERO,(String)hashDesigna.get("NUMEROEJG"));
			UtilidadesHash.set(hashEJG,ScsEJGBean.C_ANIO,(String)hashDesigna.get("ANIOEJG"));
			UtilidadesHash.set(hashEJG,ScsEJGBean.C_IDTIPOEJG,(String)hashDesigna.get("IDTIPOEJG"));
			Vector vEjgRelacionado=(Vector)ejgAdm.selectByPK(hashEJG);
			
		    if ((vEjgRelacionado != null) && (vEjgRelacionado.size() == 1)) {
		
			 UtilidadesHash.set(hashDesigna,ScsEJGBean.C_IDTIPORATIFICACIONEJG,((ScsEJGBean)vEjgRelacionado.get(0)).getIdTipoRatificacionEJG());
			 UtilidadesHash.set(hashDesigna,ScsEJGBean.C_FECHARATIFICACION,((ScsEJGBean)vEjgRelacionado.get(0)).getFechaRatificacion());
			 UtilidadesHash.set(hashDesigna,ScsEJGBean.C_FECHANOTIFICACION,((ScsEJGBean)vEjgRelacionado.get(0)).getFechaNotificacion());
		    }
			//Se muestra todas las Actuaciones de la designa.			
		    Hashtable hashActuacion = (Hashtable)(designaAdm.getConsultaActuacion(hashDatosDesigna, request)).get(0);
		    
		    //Mostrar Las Actuaciones antiguas.
		   Hashtable actuacionAntigua =(Hashtable)(designaAdm.getDesignaActuaciones(hashDatosDesigna, request)).get(0);
		    
			request.setAttribute("hashDesigna",hashDesigna);
			request.setAttribute("hashActuacionActual",hashActuacion);
			ses.setAttribute("hashActuacionAntigua",actuacionAntigua);
			
			String talonario=((String)hashActuacion.get("TALONARIO"));
		    miform.setTalonario(talonario);
			
		    String talon=((String)hashActuacion.get("TALON"));
		    miform.setTalon(talon);
			
		} catch(Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
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
		
		HttpSession ses = request.getSession();		
		UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
		
		ScsActuacionDesignaAdm actuacionDesignaAdm = new ScsActuacionDesignaAdm (this.getUserBean(request));
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		ScsEJGAdm ejgAdm = new ScsEJGAdm (this.getUserBean(request));
		Hashtable designaActual = new Hashtable();
		ScsActuacionDesignaBean actuacionActual = new ScsActuacionDesignaBean();
		Hashtable hash = null;
		String consultaDesigna = null;
		Hashtable hashEJG = new Hashtable();
		try {
			ActuacionesDesignasForm miform = (ActuacionesDesignasForm)formulario;
			if(miform!=null && miform.getIdTurno()!=null &&!miform.getIdTurno().equals("")){
				hash = new Hashtable();
				hash.put(ScsActuacionDesignaBean.C_ANIO, miform.getAnio());
				hash.put(ScsActuacionDesignaBean.C_IDTURNO, miform.getIdTurno());
				hash.put(ScsActuacionDesignaBean.C_NUMERO, miform.getNumero());
//				hash.put(ScsActuacionDesignaBean.C_IDINSTITUCION, miform.getidAnio());
				
			}else{
				hash = (Hashtable)ses.getAttribute("designaActual");
			}
			
	
				consultaDesigna =" SELECT des.anio anio, des.numero numero, des.idturno idturno, des.idinstitucion idinstitucion, des.FECHAANULACION,"+
								" per.nombre nombre, per.apellidos1 apellido1, per.apellidos2 apellido2,"+
								" col.ncolegiado ncolegiado, tur.nombre turno, des.fechaentrada fecha, "+
								" tur." + ScsTurnoBean.C_VALIDARJUSTIFICACIONES + " validarJustificaciones, " +
								" des."+ScsDesignaBean.C_IDINSTITUCIONJUZGADO+","+
								" des."+ScsDesignaBean.C_IDJUZGADO+","+
								" des."+ScsDesignaBean.C_IDPROCEDIMIENTO+","+
								" des."+ScsDesignaBean.C_IDPRETENSION+","+
								" per."+CenPersonaBean.C_IDPERSONA+" AS IDPERSONA, "+
								" (select nombre "+
								" from scs_juzgado juz"+
								" where juz.idinstitucion=des.idinstitucion_juzg "+
								"  and  juz.idjuzgado=des.idjuzgado) nombrejuzgado, "+
								" (select nombre "+
								" from scs_procedimientos proc"+
								" where proc.idinstitucion="+(String)usr.getLocation()+
								"  and  proc.idprocedimiento=des.idprocedimiento) nombreprocedimiento, "+
								" des.codigo codigo, "+
								" ejgdesigna.anioejg anioejg, "+
								" ejgdesigna.idtipoejg idtipoejg, "+
							    " ejgdesigna.numeroejg numeroejg"+
								" FROM scs_designa des, cen_colegiado col, cen_persona per, scs_designasletrado deslet, scs_turno tur,  scs_ejgdesigna ejgdesigna"+
								" WHERE deslet.idinstitucion = des.idinstitucion"+
								" and deslet.anio = des.anio"+
								" and deslet.numero = des.numero"+
								" and deslet.idturno = des.idturno"+
								" and per.idpersona = deslet.idpersona"+
								" and col.idpersona = per.idpersona"+
								" and tur.idinstitucion = des.idinstitucion"+
								" and tur.idturno = des.idturno"+
								" and des."+ ScsDesignaBean.C_IDINSTITUCION +"="+(String)usr.getLocation()+
								" and des."+ ScsDesignaBean.C_ANIO+"="+(String)hash.get("ANIO")+
								" and des."+ ScsDesignaBean.C_NUMERO+"="+(String)hash.get("NUMERO")+
								" and des."+ ScsDesignaBean.C_IDTURNO+"="+(String)hash.get("IDTURNO")+
								" and ejgdesigna.idinstitucion(+)=des.idinstitucion"+
								" and ejgdesigna.idturno(+)=des.idturno"+
								" and ejgdesigna.aniodesigna(+)=des.anio"+
								" and ejgdesigna.numerodesigna(+)=des.numero"+
								" and deslet."+ ScsDesignasLetradoBean.C_FECHARENUNCIA+" IS NULL";

			designaActual = (Hashtable)((Vector)designaAdm.ejecutaSelect(consultaDesigna)).get(0);
			UtilidadesHash.set(hashEJG,ScsEJGBean.C_IDINSTITUCION,(String)designaActual.get("IDINSTITUCION"));
			UtilidadesHash.set(hashEJG,ScsEJGBean.C_NUMERO,(String)designaActual.get("NUMEROEJG"));
			UtilidadesHash.set(hashEJG,ScsEJGBean.C_ANIO,(String)designaActual.get("ANIOEJG"));
			UtilidadesHash.set(hashEJG,ScsEJGBean.C_IDTIPOEJG,(String)designaActual.get("IDTIPOEJG"));
			Vector vEjgRelacionado=(Vector)ejgAdm.selectByPK(hashEJG);
			
		    if ((vEjgRelacionado != null) && (vEjgRelacionado.size() == 1)) {
		
			 UtilidadesHash.set(designaActual,ScsEJGBean.C_IDTIPORATIFICACIONEJG,((ScsEJGBean)vEjgRelacionado.get(0)).getIdTipoRatificacionEJG());
			 UtilidadesHash.set(designaActual,ScsEJGBean.C_FECHARATIFICACION,((ScsEJGBean)vEjgRelacionado.get(0)).getFechaRatificacion());
			 UtilidadesHash.set(designaActual,ScsEJGBean.C_FECHANOTIFICACION,((ScsEJGBean)vEjgRelacionado.get(0)).getFechaNotificacion());
		    }
			designaActual = actuacionDesignaAdm.prepararInsert(designaActual);
			request.setAttribute("hashDesigna",designaActual);
			request.setAttribute("MODO_ANTERIOR","NUEVO");
		} catch(Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		return "edicion";
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
	protected synchronized String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws SIGAException  {
		
		UsrBean usr = null;
		UserTransaction tx = null;
		HttpSession ses = (HttpSession)request.getSession();
		ScsActuacionDesignaAdm actuacionDesignaAdm = new ScsActuacionDesignaAdm(this.getUserBean(request));
		ActuacionesDesignasForm miform = (ActuacionesDesignasForm)formulario;
		String forward = null;
		boolean esFichaColegial = false;
		
		try {
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();

			if ((request.getParameter("deDonde")!=null) && (!request.getParameter("deDonde").equals("")))
				esFichaColegial = true; 		
			
			
			Hashtable hash = prepararInsert (miform.getDatos());
			hash.put(ScsActuacionAsistenciaBean.C_FECHAMODIFICACION,"SYSDATE");
			hash.put(ScsActuacionAsistenciaBean.C_USUMODIFICACION,usr.getUserName());
			hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCION,(String)usr.getLocation());
			
		    // Comprobamos si la fecha de la actuacion es posterior a la de la designa
	        SimpleDateFormat sd2 = new SimpleDateFormat (ClsConstants.DATE_FORMAT_SHORT_SPANISH);
	        Date dActuacion = sd2.parse(miform.getFechaActuacion());			            ;
	        ScsDesignaBean sdb = null;
	        try {
			    ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
			    Vector vD = designaAdm.selectByPK(hash);
			    if ((vD != null) && (vD.size() == 1)) {
			        SimpleDateFormat sd = new SimpleDateFormat (ClsConstants.DATE_FORMAT_JAVA);
			        sdb = (ScsDesignaBean)vD.get(0);
			        Date dDesgina = sd.parse(sdb.getFechaEntrada());
			        
			        if (dActuacion.compareTo(dDesgina) < 0) {
			            return exito("messages.error.acreditacionFechaNoValida",request);			            
			        }
		        }
		    }			    
	        catch (Exception e) { }

			// Obtengo el idPrision y la idInstitucion del Prision:
			Long idPrision=null;
			Integer idInstitucionPrision=null;
			String prision = miform.getPrision();
			if (prision!=null && !prision.equals("")){
				idPrision = new Long(prision.substring(0,prision.indexOf(",")));
				idInstitucionPrision = new Integer(prision.substring(prision.indexOf(",")+1));
				hash.put(ScsActuacionDesignaBean.C_IDPRISION, idPrision);
				hash.put(ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION, idInstitucionPrision);
			} else {
				hash.put(ScsActuacionDesignaBean.C_IDPRISION, "");
				hash.put(ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION, "");
			}
			String pretension = miform.getPretension();
			if (pretension!=null && !pretension.equals("")){
				hash.put(ScsActuacionDesignaBean.C_IDPRETENSION, pretension);
				
			} else {
				hash.put(ScsActuacionDesignaBean.C_IDPRETENSION, "");
				
			}
			
			String talonario= miform.getTalonario();
		    	
			if (talonario!=null && !talonario.equals("")){
					hash.put(ScsActuacionDesignaBean.C_TALONARIO, talonario);
			}else{
				hash.put(ScsActuacionDesignaBean.C_TALONARIO, "");
			}
			
			String talon= miform.getTalon();
			if (talonario!=null && !talon.equals("")){
					hash.put(ScsActuacionDesignaBean.C_TALON, talon);
			}else{
				hash.put(ScsActuacionDesignaBean.C_TALON, "");
			}

			// Obtengo el idJuzgado y la idInstitucion del Juzgado:
			Long idJuzgado=null;
			Integer idInstitucionJuzgado;
			String juzgado = miform.getJuzgado();
			
			if (juzgado!=null && !juzgado.equals("")){
				idJuzgado = new Long(juzgado.substring(0,juzgado.indexOf(",")));
				idInstitucionJuzgado = new Integer(juzgado.substring(juzgado.indexOf(",")+1));
				hash.put(ScsActuacionDesignaBean.C_IDJUZGADO, idJuzgado);
				hash.put(ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO, idInstitucionJuzgado);
			} else {
				hash.put(ScsActuacionDesignaBean.C_IDJUZGADO, "");
				hash.put(ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO, "");
			}
		
			
			// Obtengo el idProcedimiento y la idInstitucion del Procedimiento:
			Integer idProcedimiento = null, idInstitucionProcedimiento = null;
			String procedimiento = miform.getProcedimiento();
			if (procedimiento!=null && !procedimiento.equals("")){
				idProcedimiento = new Integer(procedimiento.substring(0,procedimiento.indexOf(",")));
				idInstitucionProcedimiento = new Integer(procedimiento.substring(procedimiento.indexOf(",")+1));
				hash.put(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO, idProcedimiento.toString());
				hash.put(ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO, idInstitucionProcedimiento);
			} else {
				hash.put(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO, "");
				hash.put(ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO, "");
			}
			
			// Obtengo el idAcreditacion y la idInstitucion del Acreditacion:
			Integer idAcreditacion;
			idAcreditacion = null;					
			String acreditacion = miform.getAcreditacion();
			if (acreditacion!=null && !acreditacion.equals("")){
				idAcreditacion = new Integer(acreditacion);				
				hash.put(ScsActuacionDesignaBean.C_IDACREDITACION, idAcreditacion);				
			} else {
				hash.put(ScsActuacionDesignaBean.C_IDACREDITACION, "");				
			}		

			if (miform.getActuacionValidada() != null) {
				if (UtilidadesString.stringToBoolean(miform.getActuacionValidada())) 
					hash.put(ScsActuacionDesignaBean.C_VALIDADA, "1");
				else 
					hash.put(ScsActuacionDesignaBean.C_VALIDADA, "0");
			}

			//INC_3094_SIGA
			//Obtener el colegiado designdo activo en la fecha de la actuacion
			//en lugar del colegiado designado actualmente	
			ScsDesignasLetradoAdm sdla = new ScsDesignasLetradoAdm(usr);
			Integer idPersonaActuacion = sdla.obtenerColegiadoDesignadoEnFecha(sdb,miform.getFechaActuacion());
			if (idPersonaActuacion == null)
				return exitoModal("messages.error.designacion.sinLetradoAsignado", request); 
			hash.put(ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO, idPersonaActuacion);
			boolean cambiaLetradoDesigna = !miform.getIdPersona().equals(idPersonaActuacion.toString());
					
			
			//Obtengo el tipo del estado de la acreditacion original:
			int nuevoEstado = -1;
			ScsAcreditacionAdm acreditacionAdm = new ScsAcreditacionAdm(this.getUserBean(request));
			String where = " WHERE "+ScsAcreditacionBean.C_IDACREDITACION+"="+idAcreditacion;
			Vector vAcreditaciones = acreditacionAdm.select(where);
			ScsAcreditacionBean beanAcreditaciones = (ScsAcreditacionBean)vAcreditaciones.get(0);
			if (beanAcreditaciones.getIdTipoAcreditacion()!=null && !beanAcreditaciones.getIdTipoAcreditacion().toString().equals(""))
				nuevoEstado = beanAcreditaciones.getIdTipoAcreditacion().intValue();

		    boolean multiplesAcreditaciones = false;
	        boolean bAplicarRestriccionesActuaciones = false;
			{
			    Hashtable claves = new Hashtable();
			    UtilidadesHash.set(claves, ScsProcedimientosBean.C_IDINSTITUCION, idInstitucionProcedimiento);
			    UtilidadesHash.set(claves, ScsProcedimientosBean.C_IDPROCEDIMIENTO, idProcedimiento);

			    ScsProcedimientosAdm procedimientos = new ScsProcedimientosAdm (this.getUserBean(request));
			    Vector v = procedimientos.select(claves);
			    if ((v != null) && (v.size() == 1)) {
			        multiplesAcreditaciones = ((ScsProcedimientosBean)v.get(0)).getComplemento().equals(ClsConstants.DB_TRUE);
			    }

		        // Comprobamos si tenemos que aplicar las resticciones
	        	ScsTurnoAdm turnoAdm = new ScsTurnoAdm (this.getUserBean(request));
	        	Vector vec = turnoAdm.selectByPK(hash);
	        	if (vec != null && vec.size() > 0) {
	        		ScsTurnoBean b = (ScsTurnoBean)vec.get(0);
	        		bAplicarRestriccionesActuaciones = UtilidadesString.stringToBoolean(b.getActivarRestriccionAcreditacion());
		        }
	        	
	        	if (bAplicarRestriccionesActuaciones) {
				    // comprobamos si hay mas de inicio que de fin
				    if((nuevoEstado == ClsConstants.ESTADO_ACREDITACION_FINAL || nuevoEstado == ClsConstants.ESTADO_ACREDITACION_REGULARIZACION) && multiplesAcreditaciones){
				        ScsActuacionDesignaAdm actuaciones = new ScsActuacionDesignaAdm (this.getUserBean(request));
				        int actInicio = actuaciones.getNumeroActuacionesDeTipo(ClsConstants.ESTADO_ACREDITACION_INICIO, miform.getNumero(), (String)usr.getLocation(), miform.getIdTurno(), miform.getAnio(), "" + idProcedimiento, "" + idInstitucionProcedimiento);
				        int actFinal  = actuaciones.getNumeroActuacionesDeTipo(ClsConstants.ESTADO_ACREDITACION_FINAL,  miform.getNumero(), (String)usr.getLocation(), miform.getIdTurno(), miform.getAnio(), "" + idProcedimiento, "" + idInstitucionProcedimiento);
				        if (actFinal >= actInicio) {
				            return exito("messages.error.acreditacionNoValida",request);		
				        }
				    }
	        	}
			}
			
			//Valido que el nuevo estado de la acreditacion es correcto:
			if (multiplesAcreditaciones || this.comprobarAcreditacion(nuevoEstado, false, hash, request, bAplicarRestriccionesActuaciones)) {
				tx.begin();
				actuacionDesignaAdm.insert(hash);
				tx.commit();
				String mensaje = cambiaLetradoDesigna == true ? "gratuita.designas.actuaciones.exitoConCambioLetrado" : "messages.updated.success";
				forward = exitoModal(mensaje,request); 
			} else
				forward = exito("messages.error.acreditacionNoValida",request);			
		} catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return forward;		
	}

	/**
	 * 
	 * @param hashEnt con los datos introducidos en el formulario
	 * @return hashSal con la hash para insertar en la BBDD
	 */
	protected Hashtable prepararInsert(Hashtable hashEnt){
		Hashtable hashSal = new Hashtable();

		//Por defecto toma 0 ya que se elimina de la interfaz:
		UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL , "0");

		UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_ANIO , UtilidadesHash.getString(hashEnt, "ANIO"));
		if ((UtilidadesHash.getString(hashEnt, "ANULACION")!=null)&&(!UtilidadesHash.getString(hashEnt, "ANULACION").equals("")))
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_ANULACION , "1");
		else UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_ANULACION , "0");
		try{
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_FECHA , GstDate.getApplicationFormatDate("",UtilidadesHash.getString(hashEnt, "FECHAACTUACION")));
		}catch(ClsExceptions e){
			//error al pasar a formato de fecha de la aplicacion
		}
		try{
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_FECHAJUSTIFICACION , GstDate.getApplicationFormatDate("",UtilidadesHash.getString(hashEnt, "FECHAJUSTIFICACION")));
		}catch(ClsExceptions e){
			//error al pasar a formato de fecha de la aplicacion
		}
		catch(Exception e){
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_FECHAJUSTIFICACION , "");
		}
		UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_FECHAMODIFICACION , UtilidadesHash.getString(hashEnt, "SYSDATE"));
		UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_IDPROCEDIMIENTO , UtilidadesHash.getString(hashEnt, "PROCEDIMIENTO"));
		UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_IDTURNO , UtilidadesHash.getString(hashEnt, "IDTURNO"));
		try{
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_LUGAR , UtilidadesHash.getString(hashEnt, "LUGAR"));
		}catch(Exception e){
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_LUGAR , "");
		}
		UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_NUMERO , UtilidadesHash.getString(hashEnt, "NUMERO"));
		UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_NUMEROASUNTO , UtilidadesHash.getString(hashEnt, "NACTUACION"));
		try{
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_OBSERVACIONES , UtilidadesHash.getString(hashEnt, "OBSERVACIONES"));
		}catch(Exception e){
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_OBSERVACIONES , "");
		}
		try{
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION , UtilidadesHash.getString(hashEnt, "OBSERVACIONESJUSTIFICACION"));
		}catch(Exception e){
			UtilidadesHash.set(hashSal, ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION , "");
		}
		
		return hashSal;
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
	protected String modificar(ActionMapping mapping,MasterForm formulario,HttpServletRequest request, HttpServletResponse response)throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr = null;
		UserTransaction tx = null;
		ActuacionesDesignasForm miform = (ActuacionesDesignasForm)formulario;
		Hashtable actuacionModificada = (Hashtable)miform.getDatos();	
		Hashtable actuacionAntigua = (Hashtable)ses.getAttribute("hashActuacionAntigua");
		
		String clavesActuaciones[] = {	ScsActuacionDesignaBean.C_IDINSTITUCION,		ScsActuacionDesignaBean.C_IDTURNO,
							ScsActuacionDesignaBean.C_ANIO,					ScsActuacionDesignaBean.C_NUMERO,
							ScsActuacionAsistenciaBean.C_NUMEROASUNTO};
		
		String campos[]={	ScsActuacionDesignaBean.C_IDINSTITUCION,				ScsActuacionDesignaBean.C_IDTURNO,
							ScsActuacionDesignaBean.C_ANIO,							
							ScsActuacionDesignaBean.C_FECHAMODIFICACION,			ScsActuacionDesignaBean.C_USUMODIFICACION,
							ScsActuacionDesignaBean.C_FECHA,						ScsActuacionDesignaBean.C_NUMEROASUNTO,
							ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL,			ScsActuacionDesignaBean.C_ANULACION,
							ScsActuacionDesignaBean.C_IDPROCEDIMIENTO,				ScsActuacionDesignaBean.C_LUGAR,
							ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,	ScsActuacionDesignaBean.C_OBSERVACIONES,			
							ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,			ScsActuacionDesignaBean.C_FACTURADO,
							ScsActuacionDesignaBean.C_PAGADO,
							ScsActuacionDesignaBean.C_IDFACTURACION,				ScsActuacionDesignaBean.C_VALIDADA,
							ScsActuacionDesignaBean.C_IDJUZGADO,				    ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO,
							ScsActuacionDesignaBean.C_IDCOMISARIA,				    ScsActuacionDesignaBean.C_IDINSTITUCIONCOMISARIA,
							ScsActuacionDesignaBean.C_IDPRISION,				    ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION,
							ScsActuacionDesignaBean.C_IDACREDITACION,				ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO,
							ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO,			ScsActuacionDesignaBean.C_IDPRETENSION,
		    				ScsActuacionDesignaBean.C_TALONARIO,					ScsActuacionDesignaBean.C_TALON};
		
		ScsActuacionDesignaAdm actuacionDesignaAdm = new ScsActuacionDesignaAdm(this.getUserBean(request));
		boolean ok = false;
		String forward = null;

		try {
			ScsDesignaBean sdb = null;
	        try {
			    ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
			    Vector vD = designaAdm.selectByPK(actuacionAntigua);
			    if ((vD != null) && (vD.size() == 1)) {
			        SimpleDateFormat sd = new SimpleDateFormat (ClsConstants.DATE_FORMAT_JAVA);
			        sdb = (ScsDesignaBean)vD.get(0);
			        Date dDesgina = sd.parse(sdb.getFechaEntrada());
			        
			        SimpleDateFormat sd2 = new SimpleDateFormat (ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			        Date dActuacion = sd2.parse(miform.getFechaActuacion());			            ;
		        
			        if (dActuacion.compareTo(dDesgina) < 0) {
			            return exito("messages.error.acreditacionFechaNoValida",request);			            
			        }
		        }
		    }			    
	        catch (Exception e) { }

			usr = (UsrBean)ses.getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			actuacionModificada.put(ScsActuacionDesignaBean.C_NUMEROASUNTO,(String)actuacionModificada.get("NACTUACION"));
			actuacionModificada.put(ScsActuacionDesignaBean.C_FECHA,GstDate.getApplicationFormatDate("",(String)actuacionModificada.get("FECHAACTUACION")));
			String fechaJus = "";
			fechaJus = (String)actuacionModificada.get("FECHAJUSTIFICACION");
			if(fechaJus!=null && !fechaJus.equals(""))
			actuacionModificada.put(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,GstDate.getApplicationFormatDate("",fechaJus));
			actuacionModificada.put(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO,(String)actuacionModificada.get("PROCEDIMIENTO"));
			actuacionModificada.put(ScsActuacionDesignaBean.C_IDINSTITUCION,(String)usr.getLocation());
			actuacionModificada.put(ScsActuacionDesignaBean.C_ANULACION,(((String)actuacionModificada.get("ANULACION")!=null)&&((String)actuacionModificada.get("ANULACION")).equalsIgnoreCase("on"))?"1":"0");
			//Por defecto 0 ya que se elimina de la interfaz:
			actuacionModificada.put(ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL,"0");
			
			
			// Obtengo el idPrision y la idInstitucion del Prision:
			Long idPrision=null;
			Integer idInstitucionPrision=null;
			String prision = miform.getPrision();
			if (prision!=null && !prision.equals("")){
				idPrision = new Long(prision.substring(0,prision.indexOf(",")));
				idInstitucionPrision = new Integer(prision.substring(prision.indexOf(",")+1));
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDPRISION, idPrision);
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION, idInstitucionPrision);
			} else {
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDPRISION, "");
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION, "");
			}
			// Obtenemos la Pretension seleccionada
			String idPretension = miform.getPretension();
			if (idPretension!=null && !idPretension.equals("")){
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDPRETENSION, idPretension);
				
			} else {
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDPRETENSION, "");
				
			}
			
			String talonario = miform.getTalonario();
			if (talonario!=null && !talonario.equals("")){
				actuacionModificada.put(ScsActuacionDesignaBean.C_TALONARIO, talonario);
				
			} else {
				actuacionModificada.put(ScsActuacionDesignaBean.C_TALONARIO, "");
				
			}
			
			String talon = miform.getTalon();
			if (talon!=null && !talon.equals("")){
				actuacionModificada.put(ScsActuacionDesignaBean.C_TALON, talon);
				
			} else {
				actuacionModificada.put(ScsActuacionDesignaBean.C_TALON, "");
				
			}

			// Obtengo el idJuzgado y la idInstitucion del Juzgado:
			Long idJuzgado=null;
			Integer idInstitucionJuzgado=null;
			String juzgado = miform.getJuzgado();
			if (juzgado!=null && !juzgado.equals("")){
				idJuzgado = new Long(juzgado.substring(0,juzgado.indexOf(",")));
				idInstitucionJuzgado = new Integer(juzgado.substring(juzgado.indexOf(",")+1));
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDJUZGADO, idJuzgado);
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO, idInstitucionJuzgado);
			} else {
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDJUZGADO, "");
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO, "");
			}
			
			// Obtengo el idProcedimiento y la idInstitucion del Procedimiento:
			Integer idProcedimiento=null, idInstitucionProcedimiento=null;
			String procedimiento = miform.getProcedimiento();
			if (procedimiento!=null && !procedimiento.equals("")){
				idProcedimiento = new Integer(procedimiento.substring(0,procedimiento.indexOf(",")));
				idInstitucionProcedimiento = new Integer(procedimiento.substring(procedimiento.indexOf(",")+1));
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO, idProcedimiento);
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO, idInstitucionProcedimiento);
			} else {
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO, "");
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO, "");
			}

			// Obtengo el idAcreditacion y la idInstitucion del Acreditacion:
			Integer idAcreditacion = null;					
			String acreditacion = miform.getAcreditacion();
			if (acreditacion!=null && !acreditacion.equals("")){
				idAcreditacion = new Integer(acreditacion);				
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDACREDITACION, idAcreditacion);				
			} else {
				actuacionModificada.put(ScsActuacionDesignaBean.C_IDACREDITACION, "");				
			}

			if (miform.getActuacionValidada() != null) {
				if (UtilidadesString.stringToBoolean(miform.getActuacionValidada())) 
					actuacionModificada.put(ScsActuacionDesignaBean.C_VALIDADA, "1");
				else 
					actuacionModificada.put(ScsActuacionDesignaBean.C_VALIDADA, "0");
			}
			
			
			//INC_3094_SIGA
			//Obtener el colegiado designdo activo en la fecha de la actuacion
			//en lugar del colegiado designado actualmente	
			ScsDesignasLetradoAdm sdla = new ScsDesignasLetradoAdm(usr);
			Integer idPersonaActuacion = sdla.obtenerColegiadoDesignadoEnFecha(sdb,miform.getFechaActuacion());
			if (idPersonaActuacion == null)
				return exitoModal("messages.error.designacion.sinLetradoAsignado", request); 
			actuacionModificada.put(ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO, idPersonaActuacion);
			boolean cambiaLetradoDesigna = !actuacionAntigua.get(ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO).equals(idPersonaActuacion.toString());


			//Obtengo el tipo del estado de la acreditacion nueva:
			int nuevoEstado = -1;
			ScsAcreditacionAdm acreditacionAdm = new ScsAcreditacionAdm(this.getUserBean(request));
			String where = " WHERE "+ScsAcreditacionBean.C_IDACREDITACION+"="+idAcreditacion;
			Vector vAcreditaciones = acreditacionAdm.select(where);
			ScsAcreditacionBean beanAcreditaciones = (ScsAcreditacionBean)vAcreditaciones.get(0);
			if (beanAcreditaciones.getIdTipoAcreditacion()!=null && !beanAcreditaciones.getIdTipoAcreditacion().toString().equals(""))
				nuevoEstado = beanAcreditaciones.getIdTipoAcreditacion().intValue();

			
			// Comprobacion cuando se modifica el check de anulacion cuando la acreditacion es de tipo inicio
			  if (nuevoEstado==ClsConstants.ESTADO_ACREDITACION_INICIO){
		      
			   if (!actuacionAntigua.get(ScsActuacionDesignaBean.C_ANULACION).equals(actuacionModificada.get(ScsActuacionDesignaBean.C_ANULACION))){
			  	// Comprobamos que si anulamos una actuacion con acreditacion de inicio no existe 
			  	// una actuacion con acreditacion final para el mismo procedimiento
			   	  if(this.buscarAcreditacionFinal(actuacionAntigua, request)) {
			   	    return exito("messages.error.existeAcreditacionFinal",request);
			   	  }
			  	
			  }
			}  
			// Sólo se comprueba cuando se ha modificado la acreditacion
			if (!actuacionAntigua.get(ScsActuacionDesignaBean.C_IDACREDITACION).equals(""+idAcreditacion))
			{
			    boolean multiplesAcreditaciones = false;
		        boolean bAplicarRestriccionesActuaciones = false;
				{
				    Hashtable claves = new Hashtable();
				    UtilidadesHash.set(claves, ScsProcedimientosBean.C_IDINSTITUCION, idInstitucionProcedimiento);
				    UtilidadesHash.set(claves, ScsProcedimientosBean.C_IDPROCEDIMIENTO, idProcedimiento);

				    ScsProcedimientosAdm procedimientos = new ScsProcedimientosAdm (this.getUserBean(request));
				    Vector v = procedimientos.select(claves);
				    if ((v != null) && (v.size() == 1)) {
				        multiplesAcreditaciones = ((ScsProcedimientosBean)v.get(0)).getComplemento().equals(ClsConstants.DB_TRUE);
				    }
				    
			        // Comprobamos si tenemos que aplicar las resticciones
		        	ScsTurnoAdm turnoAdm = new ScsTurnoAdm (this.getUserBean(request));
		        	Vector vec = turnoAdm.selectByPK(actuacionAntigua);
		        	if (vec != null && vec.size() > 0) {
		        		ScsTurnoBean b = (ScsTurnoBean)vec.get(0);
		        		bAplicarRestriccionesActuaciones = UtilidadesString.stringToBoolean(b.getActivarRestriccionAcreditacion());
			        }
		        	
		        	if (bAplicarRestriccionesActuaciones) {
					    // comprobamos si hay mas de inicio que de fin
					    if((nuevoEstado == ClsConstants.ESTADO_ACREDITACION_FINAL || nuevoEstado == ClsConstants.ESTADO_ACREDITACION_REGULARIZACION) && multiplesAcreditaciones){
					        ScsActuacionDesignaAdm actuaciones = new ScsActuacionDesignaAdm (this.getUserBean(request));
					        int actInicio = actuaciones.getNumeroActuacionesDeTipo(ClsConstants.ESTADO_ACREDITACION_INICIO, miform.getNumero(), (String)usr.getLocation(), miform.getIdTurno(), miform.getAnio(), "" + idProcedimiento, "" + idInstitucionProcedimiento);
					        int actFinal  = actuaciones.getNumeroActuacionesDeTipo(ClsConstants.ESTADO_ACREDITACION_FINAL,  miform.getNumero(), (String)usr.getLocation(), miform.getIdTurno(), miform.getAnio(), "" + idProcedimiento, "" + idInstitucionProcedimiento);
					        if (actFinal >= actInicio) {
					            return exito("messages.error.acreditacionNoValida",request);		
					        }
					    }
		        	}
				}
			    
				// Valido que el nuevo estado de la acreditacion es correcto:
				if (multiplesAcreditaciones || this.comprobarAcreditacion(nuevoEstado, true, actuacionAntigua, request, bAplicarRestriccionesActuaciones)) {
					tx.begin();
					//actuacionDesignaAdm.update(actuacionModificada, actuacionAntigua);
					actuacionDesignaAdm.updateDirect(actuacionModificada,clavesActuaciones,campos);
					tx.commit();
					forward = exitoModal("messages.updated.success",request); 
				} 
				else
					forward = exito("messages.error.acreditacionNoValida",request);
			}
			
			else
			{
				tx.begin();
			//	actuacionDesignaAdm.update(actuacionModificada, actuacionAntigua);
			//	tx.commit();
				actuacionDesignaAdm.updateDirect(actuacionModificada,clavesActuaciones,campos);
				tx.commit();
				String mensaje = cambiaLetradoDesigna == true ? "gratuita.designas.actuaciones.exitoConCambioLetrado" : "messages.updated.success";
				forward = exitoModal(mensaje,request); 
			}
		} catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return forward;		
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
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException  
	{		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		ActuacionesDesignasForm miform = (ActuacionesDesignasForm)formulario;
		UserTransaction tx = null;
		Vector visibles = miform.getDatosTablaVisibles(0);
		Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
		boolean ok = false;
		try {
			Hashtable aBorrar = new Hashtable();
			aBorrar.put("IDINSTITUCION",usr.getLocation());
			aBorrar.put("IDTURNO",(String)designaActual.get("IDTURNO"));
			aBorrar.put("ANIO",(String)designaActual.get("ANIO"));
			aBorrar.put("NUMERO",(String)designaActual.get("NUMERO"));
			aBorrar.put("NUMEROASUNTO",(String)visibles.get(1));

			tx = usr.getTransaction();
			
		    ScsActuacionDesignaAdm actuacionDesignaAdm = new ScsActuacionDesignaAdm (this.getUserBean(request));

		    {
		        // comprobamos si vamos a dejar mas de fin que de inicio
				Vector v = actuacionDesignaAdm.select(aBorrar);
				ScsActuacionDesignaBean beanActuacionDesigna = (ScsActuacionDesignaBean)v.get(0);
	
				ScsAcreditacionAdm acreditacionAdm = new ScsAcreditacionAdm(this.getUserBean(request));
				String where = " WHERE " + ScsAcreditacionBean.C_IDACREDITACION + " = " + beanActuacionDesigna.getIdAcreditacion();
				Vector vAcreditaciones = acreditacionAdm.select(where);
				ScsAcreditacionBean beanAcreditaciones = (ScsAcreditacionBean)vAcreditaciones.get(0);
			    
			    // comprobamos si hay mas de inicio que de fin
			    if(beanAcreditaciones.getIdTipoAcreditacion().intValue() == ClsConstants.ESTADO_ACREDITACION_INICIO){
			        ScsActuacionDesignaAdm actuaciones = new ScsActuacionDesignaAdm (this.getUserBean(request));
			        int actInicio = actuaciones.getNumeroActuacionesDeTipo(ClsConstants.ESTADO_ACREDITACION_INICIO, "" + beanActuacionDesigna.getNumero(), "" + usr.getLocation(), "" + beanActuacionDesigna.getIdTurno(), "" + beanActuacionDesigna.getAnio(), beanActuacionDesigna.getIdProcedimiento(), "" + beanActuacionDesigna.getIdInstitucionProcedimiento());
			        int actFinal  = actuaciones.getNumeroActuacionesDeTipo(ClsConstants.ESTADO_ACREDITACION_FINAL,  "" + beanActuacionDesigna.getNumero(), "" + usr.getLocation(), "" + beanActuacionDesigna.getIdTurno(), "" + beanActuacionDesigna.getAnio(), beanActuacionDesigna.getIdProcedimiento(), "" + beanActuacionDesigna.getIdInstitucionProcedimiento());
			        if (actInicio <= actFinal) {
			            return exito("messages.error.acreditacionBorrar",request);
			        }

			        int actRegularizacion  = actuaciones.getNumeroActuacionesDeTipo(ClsConstants.ESTADO_ACREDITACION_REGULARIZACION,  "" + beanActuacionDesigna.getNumero(), "" + usr.getLocation(), "" + beanActuacionDesigna.getIdTurno(), "" + beanActuacionDesigna.getAnio(), beanActuacionDesigna.getIdProcedimiento(), "" + beanActuacionDesigna.getIdInstitucionProcedimiento());
			        if (actInicio <= actRegularizacion) {
			            return exito("messages.error.acreditacionRegularizacionBorrar",request);
			        }
			    }
			}
			tx.begin();
		    ok = actuacionDesignaAdm.delete(aBorrar);
		    tx.commit();
		}
		catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		if (ok) return exitoRefresco("messages.deleted.success",request);
		else return exito("messages.deleted.error",request);
	}

	// Comprueba si una acreditacion cumple estas condiciones sobre su tipo (1=Inicial / 2=Final / 3=Completa):
	// 1.- Si anhado inicio no puede existir inicio.
	// 2.- Si anhado final debe haber inicio.
	// 3.- Si anhado Completa no puede haber ninguna otra.
	private boolean comprobarAcreditacion (int nuevoEstado, boolean esModificacion, Hashtable hashActuacionDesigna, HttpServletRequest request, boolean bAplicarRestriccionesActuaciones) throws ClsExceptions{
		boolean ok = false;
		boolean tieneEstado = false;
		int estadoAnterior = -1;
		boolean existeAlgunaAcreditacionDeInicio = false;
		boolean existeAlgunaAcreditacionDeInicioFin = false;
		boolean estadoAlgunaAcreditacionCompleta = false;
		
		//Recupero si tiene una acreditacion para esa actuacion con ese procedimiento: 
		int estadoOriginal = -1;
		ScsActuacionDesignaAdm actuacionDesignaAdm = new ScsActuacionDesignaAdm(this.getUserBean(request));
		String where=" WHERE "+ScsActuacionDesignaBean.C_IDINSTITUCION+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDINSTITUCION)+
					 " AND "+ScsActuacionDesignaBean.C_ANIO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_ANIO)+
					 " AND "+ScsActuacionDesignaBean.C_NUMERO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_NUMERO)+
					 " AND "+ScsActuacionDesignaBean.C_IDTURNO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDTURNO)+
					 " AND "+ScsActuacionDesignaBean.C_IDPROCEDIMIENTO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO)+
					 " AND "+ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO)+
					 " AND "+ScsActuacionDesignaBean.C_IDACREDITACION+" IS NOT NULL"+
					 " AND "+ScsActuacionDesignaBean.C_ANULACION+"<>1";
		
		if (esModificacion)
			where += " AND "+ScsActuacionDesignaBean.C_IDACREDITACION+"<>"+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDACREDITACION);
		
		Vector vActuacionesDesigna = actuacionDesignaAdm.select(where);
		
		//Si tiene al menos una acreditacion para ese procedimiento:
		if (!vActuacionesDesigna.isEmpty()) {
			tieneEstado = true;
			//Obtengo si el estado anterior era Inicio:
			existeAlgunaAcreditacionDeInicio = this.buscarEstadoInicio(vActuacionesDesigna, request);
			existeAlgunaAcreditacionDeInicioFin = this.buscarEstadoFinal(vActuacionesDesigna, request);
			estadoAlgunaAcreditacionCompleta = this.buscarEstadoCompleta(vActuacionesDesigna, request);
		// Si no tiene acreditaciones
		} else { 
			tieneEstado = false;
			existeAlgunaAcreditacionDeInicio = false;
		}
		
		switch (nuevoEstado){
			case ClsConstants.ESTADO_ACREDITACION_INICIO: 
					if (!tieneEstado)
						ok = true; //OK
					else
						ok = false; //Error: si anhado final debe haber inicio.
					break;
			case ClsConstants.ESTADO_ACREDITACION_FINAL:
			case ClsConstants.ESTADO_ACREDITACION_REGULARIZACION:
					if ((!bAplicarRestriccionesActuaciones || (tieneEstado && existeAlgunaAcreditacionDeInicio )) && !existeAlgunaAcreditacionDeInicioFin && !estadoAlgunaAcreditacionCompleta)
						ok = true; //OK
					else
						ok = false; //Error: si anhado final debe haber inicio.
					break;
			case ClsConstants.ESTADO_ACREDITACION_COMPLETA: 
					if (tieneEstado)
						ok = false; //Error: si anhado final debe haber inicio.
					else
						ok = true; //OK
					break;
		}
		return ok;
	}
	
	//Busca en un vector de beans de actuaciones de designas con acreditaciones si hay un estado de acreditacion de tipo inicio.
	private boolean buscarEstadoInicio (Vector vActuacionesDesigna, HttpServletRequest request) throws ClsExceptions {		
		boolean encontrado = false;
		
		for (int i=0; !encontrado && (i < vActuacionesDesigna.size()); i++) {
			//Obtengo el idAcreditacion
			ScsActuacionDesignaBean beanActuacionDesigna = (ScsActuacionDesignaBean)vActuacionesDesigna.get(i);
			Integer idAcreditacion = beanActuacionDesigna.getIdAcreditacion();
			
			//Obtengo el estado del tipo de acreditacion:
			ScsAcreditacionAdm acreditacionAdm = new ScsAcreditacionAdm(this.getUserBean(request));
			String where = " WHERE "+ScsAcreditacionBean.C_IDACREDITACION+"="+idAcreditacion;
			               	
			Vector vAcreditaciones = acreditacionAdm.select(where);
			ScsAcreditacionBean beanAcreditacion = (ScsAcreditacionBean)vAcreditaciones.get(0);

			//Si el estado de la acreditacion de ese procedimiento es inicio he terminado:
			if (beanAcreditacion.getIdTipoAcreditacion().intValue()==ClsConstants.ESTADO_ACREDITACION_INICIO)
				encontrado = true;
		}		
		return encontrado;
	}

	//Busca en un vector de beans de actuaciones de designas con acreditaciones si hay un estado de acreditacion de tipo inicio.
	private boolean buscarEstadoFinal (Vector vActuacionesDesigna, HttpServletRequest request) throws ClsExceptions {		
		boolean encontrado = false;
		
		for (int i=0; !encontrado && (i < vActuacionesDesigna.size()); i++) {
			//Obtengo el idAcreditacion
			ScsActuacionDesignaBean beanActuacionDesigna = (ScsActuacionDesignaBean)vActuacionesDesigna.get(i);
			Integer idAcreditacion = beanActuacionDesigna.getIdAcreditacion();
			
			//Obtengo el estado del tipo de acreditacion:
			ScsAcreditacionAdm acreditacionAdm = new ScsAcreditacionAdm(this.getUserBean(request));
			String where = " WHERE "+ScsAcreditacionBean.C_IDACREDITACION+"="+idAcreditacion;						   			
			Vector vAcreditaciones = acreditacionAdm.select(where);
			ScsAcreditacionBean beanAcreditacion = (ScsAcreditacionBean)vAcreditaciones.get(0);

			//Si el estado de la acreditacion de ese procedimiento es inicio he terminado:
			if (beanAcreditacion.getIdTipoAcreditacion().intValue()==ClsConstants.ESTADO_ACREDITACION_FINAL)
				encontrado = true;
		}		
		return encontrado;
	}
	
	//Busca en un vector de beans de actuaciones de designas con acreditaciones si hay un estado de acreditacion de tipo inicio.
	private boolean buscarEstadoCompleta (Vector vActuacionesDesigna, HttpServletRequest request) throws ClsExceptions {		
		boolean encontrado = false;
		
		for (int i=0; !encontrado && (i < vActuacionesDesigna.size()); i++) {
			//Obtengo el idAcreditacion
			ScsActuacionDesignaBean beanActuacionDesigna = (ScsActuacionDesignaBean)vActuacionesDesigna.get(i);
			Integer idAcreditacion = beanActuacionDesigna.getIdAcreditacion();
			
			//Obtengo el estado del tipo de acreditacion:
			ScsAcreditacionAdm acreditacionAdm = new ScsAcreditacionAdm(this.getUserBean(request));
			String where = " WHERE "+ScsAcreditacionBean.C_IDACREDITACION+"="+idAcreditacion;						   			
			Vector vAcreditaciones = acreditacionAdm.select(where);
			ScsAcreditacionBean beanAcreditacion = (ScsAcreditacionBean)vAcreditaciones.get(0);

			//Si el estado de la acreditacion de ese procedimiento es inicio he terminado:
			if (beanAcreditacion.getIdTipoAcreditacion().intValue()==ClsConstants.ESTADO_ACREDITACION_COMPLETA)
				encontrado = true;
		}		
		return encontrado;
	}	
     private boolean buscarAcreditacionFinal ( Hashtable hashActuacionDesigna, HttpServletRequest request) throws ClsExceptions{
			boolean ok = false;
			boolean tieneEstado = false;
			int estadoAnterior = -1;
			boolean estadoAnteriorInicio = false;
			boolean estadoAnteriorFinal = false;
			boolean estadoAnteriorCompleta = false;
			
			//Recupero si tiene una acreditacion para esa actuacion con ese procedimiento: 
			int estadoOriginal = -1;
			ScsActuacionDesignaAdm actuacionDesignaAdm = new ScsActuacionDesignaAdm(this.getUserBean(request));
			String where=" WHERE "+ScsActuacionDesignaBean.C_IDINSTITUCION+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDINSTITUCION)+
						 " AND "+ScsActuacionDesignaBean.C_ANIO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_ANIO)+
						 " AND "+ScsActuacionDesignaBean.C_NUMERO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_NUMERO)+
						 " AND "+ScsActuacionDesignaBean.C_IDTURNO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDTURNO)+
						 " AND "+ScsActuacionDesignaBean.C_IDPROCEDIMIENTO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO)+
						 " AND "+ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO+"="+hashActuacionDesigna.get(ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO)+
						 " AND "+ScsActuacionDesignaBean.C_IDACREDITACION+" IS NOT NULL"+
						 " AND "+ScsActuacionDesignaBean.C_ANULACION+"<>1" +
						 " AND "+ScsActuacionDesignaBean.C_IDACREDITACION+" IN (SELECT "+ScsAcreditacionBean.C_IDACREDITACION+ 
						 "                                                      FROM "+ScsAcreditacionBean.T_NOMBRETABLA+
 						 "                                                      WHERE "+ScsAcreditacionBean.C_IDTIPOACREDITACION+" = "+ClsConstants.ESTADO_ACREDITACION_FINAL+")";
			
			
			
			Vector vActuacionesDesigna = actuacionDesignaAdm.select(where);
			if (!vActuacionesDesigna.isEmpty()) {
			   ok=true;
			}else{
				ok=false;
			}
			return ok;
		    
				
		
     }
}