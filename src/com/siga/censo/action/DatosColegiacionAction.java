/*
 * VERSIONES:
 * 
 * pilar.duran - 16-03-2007 - Creacion
 *	
 */

/**
 * Clase action para la visualizacion y mantenimiento de los datos colegiales generales.<br/>
 * Gestiona la edicion, borrado, consulta y mantenimiento del historico  
 */

package com.siga.censo.action;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDatosColegialesEstadoAdm;
import com.siga.beans.CenDatosColegialesEstadoBean;
import com.siga.beans.CenEstadoActividadPersonaAdm;
import com.siga.beans.CenEstadoActividadPersonaBean;
import com.siga.beans.CenEstadoColegialBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenTiposSeguroAdm;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.censo.form.DatosColegiacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class DatosColegiacionAction extends MasterAction {

	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {

		    
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("activar")){
					mapDestino=activar(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("darBaja")){
					mapDestino=baja(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("bajaFallecido")){
					mapDestino=fallecido(mapping, miForm, request, response);	
				}else if (accion.equalsIgnoreCase("buscarDatosColegiacion")){
					mapDestino=buscarDatosColegiacion(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("ver")){
					mapDestino=buscarDatosColegiacion(mapping, miForm, request, response);	
				
				}else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		} 
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String numero = "";
		String nombre = "";
		String result = "abrir";
		boolean estadoCertificacion = false;
		String certificadoCorrecto = "";
		CenColegiadoBean datosColegiales;	

		try{
			
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");		
			String accion = (String)request.getParameter("accion");
			String idInstitucion=user.getLocation();
			Integer idInst=new Integer(idInstitucion);
			String fechaActualiz="";
			
	//		 Obtengo el identificador de persona, la accion y el identificador de institucion del cliente
			Long idPersona = new Long(request.getParameter("idPersona"));
			Integer idInstPers = new Integer(request.getParameter("idInstitucion"));
			String idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion")).toString();
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserName(request),user,idInstPers.intValue(),idPersona.longValue());
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));		
			datosColegiales=colegiadoAdm.getDatosColegiales(idPersona,idInstPers);
			numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
		
		    
			
			request.setAttribute("IDPERSONA", idPersona);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("IDINSTITUCIONPERSONA", idInstitucionPersona);	
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);
			request.setAttribute("ACCION", accion);
			
	// Recuperamos la ultima fecha de la situacion introducida y el motivo introducidos por el usuario		
			Hashtable consultaHash=new Hashtable();
			consultaHash.put(CenEstadoActividadPersonaBean.C_IDPERSONA,request.getParameter("idPersona"));
			//consultaHash.put(CenEstadoActividadPersonaBean.C_IDESTADO,ClsConstants.ESTADO_LETRADO_BAJA);
			CenEstadoActividadPersonaAdm estadoActividadAdm=new CenEstadoActividadPersonaAdm(this.getUserBean(request));
			Vector ultimoCodigo=estadoActividadAdm.select(consultaHash);
			if (ultimoCodigo.size()>0){
			  CenEstadoActividadPersonaBean obj = (CenEstadoActividadPersonaBean)ultimoCodigo.get(0);
			  request.setAttribute("MOTIVO",(String)obj.getMotivo());
			  request.setAttribute("FECHAESTADO",(String)obj.getfechaEstado());
			  
			  int idEstado=((Integer)obj.getIdEstado()).intValue();
			  if (idEstado==20){
			    request.setAttribute("ACTIVAR","1");
			  }else{
			  	 request.setAttribute("ACTIVAR","0");
			  }
			}else{
				request.setAttribute("ACTIVAR","0");
			}
			CenClienteAdm clienteAdm = new CenClienteAdm(user);
			Vector datosEstado=clienteAdm.getDatosColegiacion(idPersona,idInstitucion, user.getLanguage());
			ArrayList<String> candidatas = getCertificadosCandidatosCorrectos(datosEstado,idInstitucion,idPersona.toString(), user);

			if (candidatas != null && candidatas.size() > 0){
				if(isCorrectoEstadoCertificacion(candidatas, idPersona, user)){
					estadoCertificacion = true;
				}else{
					estadoCertificacion = false;
					PysProductosInstitucionAdm pysAdm = new PysProductosInstitucionAdm(user);
					certificadoCorrecto = pysAdm.descripcionCertificado(candidatas.get(0),"2000");
				}					
			}else{
				estadoCertificacion = true;
			}
			
			request.setAttribute("CERTIFICADOCORRECTO", certificadoCorrecto);
			request.setAttribute("ESTADOCERTIFICACION", estadoCertificacion);				
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		} 
		return result;
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String activar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result = "abrir";
		UserTransaction tx = null;
		try { 

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			Hashtable hash = new Hashtable();
			CenEstadoActividadPersonaAdm estadoActividadAdm=new CenEstadoActividadPersonaAdm(this.getUserBean(request));
		
			hash.put(CenEstadoActividadPersonaBean.C_IDPERSONA, request.getParameter("idPersona"));			
			hash.put(CenEstadoActividadPersonaBean.C_FECHAESTADO, "sysdate");
			hash.put(CenEstadoActividadPersonaBean.C_IDESTADO, ClsConstants.ESTADO_LETRADO_ACTIVO);
			hash.put(CenEstadoActividadPersonaBean.C_FECHAMODIFICACION,"sysdate");
			hash.put(CenEstadoActividadPersonaBean.C_IDCODIGO,estadoActividadAdm.getIdCodigo(request.getParameter("idPersona")));
			tx = usr.getTransaction();
			tx.begin();	
			if (estadoActividadAdm.insert(hash)){
				CenPersonaAdm personaAdm= new CenPersonaAdm(this.getUserBean(request));
				Hashtable hashPersona=new Hashtable();
				
				CenPersonaBean personaBean = new CenPersonaBean();
				hashPersona.put(CenPersonaBean.C_IDPERSONA, request.getParameter("idPersona"));
				Vector vPersonaOld=personaAdm.selectByPK(hashPersona);
				CenPersonaBean beanPersona = (CenPersonaBean)vPersonaOld.elementAt(0);
				beanPersona.setFallecido(ClsConstants.DB_FALSE);
				
				
				if (personaAdm.update(beanPersona)){
				   tx.commit();
				}   
				
			}else{
			    throw new SIGAException ("Error al insertar la situacion del letrado "+estadoActividadAdm.getError());
		    }
			
			//abrir( mapping, formulario,  request,  response);
		
			request.setAttribute("IDPERSONA", new Long (request.getParameter("idPersona")));
			request.setAttribute("IDINSTITUCION", usr.getLocation());
			request.setAttribute("IDINSTITUCIONPERSONA", request.getParameter("idInstitucionPersona"));	
			request.setAttribute("NOMBRE", request.getParameter("nombre"));
			request.setAttribute("NUMERO", request.getParameter("numero"));
			request.setAttribute("ACCION", request.getParameter("modo"));
			
			request.setAttribute("MOTIVO","");
			request.setAttribute("FECHAESTADO","");
			request.setAttribute("ACTIVAR","0");
					
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		
		return result;
	}
	
	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarDatosColegiacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result = "abrirDatosColegiacion";
		UserTransaction tx = null;
		Vector datosEstado = null;
		Vector vEstado;

		try { 
			DatosColegiacionForm miForm = (DatosColegiacionForm)formulario;
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			String accion = (String)request.getParameter("accion");
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenClienteAdm clienteAdm =new CenClienteAdm(this.getUserBean(request));
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			String idInstitucion=usr.getLocation();
			Long idPersona = new Long(request.getParameter("idPersona"));
			datosEstado=clienteAdm.getDatosColegiacion(idPersona,idInstitucion, usr.getLanguage());
		
			request.setAttribute("ACCION", accion);
			request.setAttribute("DATESTADO", datosEstado);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		
		return result;
	}
	
	
	private boolean isCorrectoEstadoCertificacion(ArrayList<String> candidatas, Long idPersona, UsrBean usr) throws ClsExceptions{
		
		boolean tieneCertificadoCorrecto = false;
		int i = 0;
		CerSolicitudCertificadosAdm cerAdm = new CerSolicitudCertificadosAdm(usr);
		try {
			while(!tieneCertificadoCorrecto && i < candidatas.size()){
				String[] cand = candidatas.get(i).split(",");				
					if(cerAdm.tieneCertificadosPersonaInstitucion("2000", idPersona.toString(), cand[0], cand[1], cand[2])){
						tieneCertificadoCorrecto = true;
					}	
				i++;
			}
		
		} catch (ClsExceptions e) {
			throw e;
		}
		
		return tieneCertificadoCorrecto;
	}
	
	private ArrayList<String> getCertificadosCandidatosCorrectos(Vector datosEstado, String idInstitucion, String idPersona, UsrBean usr) throws ClsExceptions{
		
		ArrayList<String> candidatas = new ArrayList<String>();
		GenParametrosAdm admParametros = new GenParametrosAdm(usr);
		CenDatosColegialesEstadoAdm coleAdm = new CenDatosColegialesEstadoAdm(usr);

		try {
			String anio_control = admParametros.getValor(idInstitucion,"CER", "AÑO_CONTROL_CERTIFICADOS","01/09/2011");
			String cni_certOrdinario = admParametros.getValor(idInstitucion,"CER", "CONTROL_CERTIFICADO_ORDINARIO","0,0,0");
			String cni_certNoEjerciente = admParametros.getValor(idInstitucion,"CER", "CONTROL_CNI_NOEJERCIENTE","0,0,0");
			String cni_certNuevaIncorp = admParametros.getValor(idInstitucion,"CER", "CONTROL_CUOTA_NUEVA_INCORPORACION","0,0,0");
			String cni_certCambioAbogado = admParametros.getValor(idInstitucion,"CER", "CONTROL_CNI_CAMBIO_ABOGADO","0,0,0");
			
			if(datosEstado != null && datosEstado.size()>0){				
				
				for (int i = 0; i < datosEstado.size(); i++) {
					Row fila = (Row) datosEstado.get(i);
					String fechaEstado = GstDate.getFormatedDateShort(usr.getLanguage(),fila.getString("FECHAESTADO"));
					if(fechaEstado != null & !fechaEstado.equals("")){
						GstDate gstDate = new GstDate();
						
						if(gstDate.compararFechas(fechaEstado, anio_control) >= 0){
							Vector datos =  coleAdm.getDatosColegialesPersonaInstitucion(fila.getString("IDINSTITUCION"), idPersona);
							
							switch (Integer.parseInt(fila.getString("IDESTADOCOLEGIAL"))) {
								case ClsConstants.ESTADO_COLEGIAL_EJERCIENTE:
									if (datos != null && datos.size() > 0){
										//Caso 1 de análisis INC_9125_SIGA
										if (datos.size() == 1){
											candidatas.add(cni_certNuevaIncorp);
											
										} else if (datos.size() == 2){ 
											//Caso 3 de análisis INC_9125_SIGA
											if(isEstadoEncontrado(datos,ClsConstants.ESTADO_COLEGIAL_SINEJERCER)){
												candidatas.add(cni_certCambioAbogado);
											}
											
										} else {
											Hashtable registro = (Hashtable)datos.get(0);
											if(registro.get("IDESTADO").equals(""+ClsConstants.ESTADO_COLEGIAL_SINEJERCER)){ //Caso 4 de análisis INC_9125_SIGA	
												candidatas.add(cni_certCambioAbogado);
											} else if (registro.get("IDESTADO").equals(""+ClsConstants.ESTADO_COLEGIAL_EJERCIENTE)){ //Caso 5a de análisis INC_9125_SIGA
												candidatas.add(cni_certOrdinario);
											} else {
												if(isEstadoEncontrado(datos,ClsConstants.ESTADO_COLEGIAL_SINEJERCER)){  //Caso 5b de análisis INC_9125_SIGA
													candidatas.add(cni_certOrdinario);
												}
											}
										}
									}
									
									break;
			
								case ClsConstants.ESTADO_COLEGIAL_SINEJERCER:
									if (datos != null){
										//Caso 2 de análisis INC_9125_SIGA
										if (datos.size() == 1){
											candidatas.add(cni_certNoEjerciente);
										}
									}
									break;
									
								default:
									break;
							}					
						}
					}
				}
			}		
		} catch (ClsExceptions e) {
			throw e;
		}
		
		return candidatas;
	}
	
	private boolean isEstadoEncontrado(Vector datosColegiales, int estadoAEncontrar) {
		boolean encontrado = false;
		int i = 0;
		while(!encontrado && i < datosColegiales.size()){
			Hashtable registro = (Hashtable)datosColegiales.get(i);
			if(registro.get("IDESTADO").equals(""+estadoAEncontrar)){
				encontrado = true;
			}
			i++;
		}
		return encontrado;
	}
	
	protected String baja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result = "abrir";
		UserTransaction tx = null;
		try { 

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			Hashtable hash = new Hashtable();
			DatosColegiacionForm miForm = (DatosColegiacionForm)formulario;
			miForm.getDatos();
			CenEstadoActividadPersonaAdm estadoActividadAdm=new CenEstadoActividadPersonaAdm(this.getUserBean(request));			
			hash.put(CenEstadoActividadPersonaBean.C_IDPERSONA, request.getParameter("idPersona"));			
			hash.put(CenEstadoActividadPersonaBean.C_FECHAESTADO, miForm.getFechaEstado());
			hash.put(CenEstadoActividadPersonaBean.C_IDESTADO, ClsConstants.ESTADO_LETRADO_BAJA);
			hash.put(CenEstadoActividadPersonaBean.C_MOTIVO,miForm.getMotivo());
			hash.put(CenEstadoActividadPersonaBean.C_FECHAMODIFICACION,"sysdate");
			hash.put(CenEstadoActividadPersonaBean.C_IDCODIGO,estadoActividadAdm.getIdCodigo(request.getParameter("idPersona")));
			tx = usr.getTransaction();
			tx.begin();	
			if (estadoActividadAdm.insert(hash)){
				tx.commit(); 
				
			}else{
			    throw new SIGAException ("Error al insertar la situacion del letrado "+estadoActividadAdm.getError());
		    }
			
			  request.setAttribute("IDPERSONA", new Long (request.getParameter("idPersona")));
			  request.setAttribute("IDINSTITUCION", usr.getLocation());
			  request.setAttribute("IDINSTITUCIONPERSONA", request.getParameter("idInstitucionPersona"));	
			  request.setAttribute("NOMBRE", request.getParameter("nombre"));
			  request.setAttribute("NUMERO", request.getParameter("numero"));
			  request.setAttribute("ACCION", request.getParameter("modo"));
		      request.setAttribute("ACTIVAR","1");
			
					
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		
		
		
		return result;
	}

	protected String fallecido(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result = "abrir";
		UserTransaction tx = null;
		try { 

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			Hashtable hash = new Hashtable();
			DatosColegiacionForm miForm = (DatosColegiacionForm)formulario;
			miForm.getDatos();
			CenEstadoActividadPersonaAdm estadoActividadAdm=new CenEstadoActividadPersonaAdm(this.getUserBean(request));			
			hash.put(CenEstadoActividadPersonaBean.C_IDPERSONA, request.getParameter("idPersona"));			
			hash.put(CenEstadoActividadPersonaBean.C_FECHAESTADO, miForm.getFechaEstado());
			hash.put(CenEstadoActividadPersonaBean.C_IDESTADO, ClsConstants.ESTADO_LETRADO_BAJA);
			hash.put(CenEstadoActividadPersonaBean.C_MOTIVO,miForm.getMotivo());
			hash.put(CenEstadoActividadPersonaBean.C_FECHAMODIFICACION,"sysdate");
			hash.put(CenEstadoActividadPersonaBean.C_IDCODIGO,estadoActividadAdm.getIdCodigo(request.getParameter("idPersona")));
			tx = usr.getTransaction();
			tx.begin();	
			if (estadoActividadAdm.insert(hash)){
				
				CenPersonaAdm personaAdm= new CenPersonaAdm(this.getUserBean(request));
				Hashtable hashPersona=new Hashtable();
				
				CenPersonaBean personaBean = new CenPersonaBean();
				hashPersona.put(CenPersonaBean.C_IDPERSONA, request.getParameter("idPersona"));
				Vector vPersonaOld=personaAdm.selectByPK(hashPersona);
				CenPersonaBean beanPersona = (CenPersonaBean)vPersonaOld.elementAt(0);
				beanPersona.setFallecido(ClsConstants.DB_TRUE);
				
				
				if (personaAdm.update(beanPersona)){
					tx.commit(); 
					
				}else{
				    throw new SIGAException ("Error al actualizar el check de fallecido "+personaAdm.getError());
			    }
				
				
			}else{
			    throw new SIGAException ("Error al insertar la situacion del letrado "+estadoActividadAdm.getError());
		    }
			
			
			  request.setAttribute("IDPERSONA", new Long (request.getParameter("idPersona")));
			  request.setAttribute("IDINSTITUCION", usr.getLocation());
			  request.setAttribute("IDINSTITUCIONPERSONA", request.getParameter("idInstitucionPersona"));	
			  request.setAttribute("NOMBRE", request.getParameter("nombre"));
			  request.setAttribute("NUMERO", request.getParameter("numero"));
			  request.setAttribute("ACCION", request.getParameter("modo"));
		      request.setAttribute("ACTIVAR","1");
			
					
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		
		
		
		return result;
	}
  
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result = "nuevo";
		Hashtable original=new Hashtable();	
		Vector datosEstado;	
		try { 
			DatosColegiacionForm form = (DatosColegiacionForm) formulario;

			Vector ocultos = new Vector();
			ocultos = (Vector)formulario.getDatosTablaOcultos(0);
			CenDatosColegialesEstadoBean infoEntrada=new CenDatosColegialesEstadoBean();
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
		
			int idInstitucionPersona;
			long idPersona;
			String nombre="";
			String numero="";
			if (ocultos != null) {
				idPersona  = new Long((String)ocultos.get(0)).longValue();
				idInstitucionPersona = new Integer((String)ocultos.get(1)).intValue();
				nombre=(String)ocultos.get(4);
				numero=(String)ocultos.get(3);
			}
			else {
				idPersona  = new Long(form.getIdPersona()).longValue();
				idInstitucionPersona = new Integer(form.getIdInstitucion()).intValue();
				nombre=form.getNombre();
				numero=form.getNumero();
			}
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			
			CenDatosColegialesEstadoAdm admin=new CenDatosColegialesEstadoAdm(this.getUserName(request),user,idInstitucionPersona,idPersona);			
			
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			
			String idEstado = "";
			try {
			    Vector v = colegiadoAdm.getEstadosColegiales(new Long(idPersona), new Integer(idInstitucionPersona), this.getLenguaje(request));
			    if (v.size()<1)
					request.getSession().setAttribute("DATABACKUP_EST",new Row());
				else
					request.getSession().setAttribute("DATABACKUP_EST",v.firstElement());
			    
			    idEstado = UtilidadesHash.getString (((Row)v.get(0)).getRow(), CenEstadoColegialBean.C_IDESTADO);
			}
			catch (Exception e) {
			    idEstado = "";
            }
			
			//Fecha actual
			String fechaEstado = UtilidadesBDAdm.getFechaBD("");
			
			infoEntrada.setFechaEstado(fechaEstado);
			request.setAttribute("container", infoEntrada);
			
			// Paso de parametros empleando request
			request.setAttribute("IDPERSONA",new Long(idPersona).toString());
			request.setAttribute("IDINSTITUCION", new Integer(idInstitucionPersona).toString());
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);			
			request.setAttribute("ID_ESTADO_ULTIMO", idEstado);			
			
			// Paso el origen
			Object remitente=(Object)"insertar";
			request.setAttribute("modelo",remitente);	
			
			
			
						
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		return (result);
	}

		
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";				
		UserTransaction tx=null;
		CenClienteAdm admCliente = new CenClienteAdm(this.getUserBean(request));
		
		try {
			Hashtable hash = new Hashtable();		
			Vector camposOcultos = new Vector();
			

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenDatosColegialesEstadoAdm admin=new CenDatosColegialesEstadoAdm(this.getUserBean(request));
			CenHistoricoAdm adminHist=new CenHistoricoAdm(this.getUserBean(request));			
 			
			// Obtengo los datos del formulario
			DatosColegiacionForm miForm = (DatosColegiacionForm) formulario;

			camposOcultos = (Vector)formulario.getDatosTablaOcultos(0);		

			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			hash.put(CenDatosColegialesEstadoBean.C_IDPERSONA, camposOcultos.get(0));			
			hash.put(CenDatosColegialesEstadoBean.C_IDINSTITUCION, camposOcultos.get(1));
			hash.put(CenDatosColegialesEstadoBean.C_FECHAESTADO, camposOcultos.get(5));
												
			// Cargo una nueva tabla hash para insertar en la tabla de historico
			Hashtable hashHist = new Hashtable();			

			// Cargo los valores obtenidos del formulario referentes al historico			
			hashHist.put(CenHistoricoBean.C_IDPERSONA, camposOcultos.get(0));			
			hashHist.put(CenHistoricoBean.C_IDINSTITUCION, camposOcultos.get(1));			
			hashHist.put(CenHistoricoBean.C_MOTIVO, ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
			hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_COLEGIALES).toString());
			
			
			//Control de errores:
			int error = admCliente.tieneTrabajosSJCSPendientes(new Long((String)camposOcultos.get(0)), new Integer((String)camposOcultos.get(1)),null,null);
			if (error == 1)
				return exito("error.message.guardiasEstadoColegial", request);
			else if (error == 2)
				return exito("error.message.designasEstadoColegial", request);
			
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	

			// Asigno el IDHISTORICO			
			hashHist.put(CenHistoricoBean.C_IDHISTORICO, adminHist.getNuevoID(hash).toString());			

			if (admin.borrarConHistorico(hash,hashHist, this.getLenguaje(request))){
				tx.commit();
				result=exitoRefresco("messages.deleted.success",request);
			}	
			else{
				throw new SIGAException (admin.getError());
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		return result;					
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		/*String result = "editar";
		try { 
			DatosColegiacionForm form = (DatosColegiacionForm) formulario;

			Vector ocultos = new Vector();
			ocultos = (Vector)formulario.getDatosTablaOcultos(0);
			CenDatosColegialesEstadoBean infoEntrada=new CenDatosColegialesEstadoBean();	
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserName(request));
		
			int idInstitucionPersona = new Integer((String)ocultos.get(1)).intValue();
			long idPersona = new Long((String)ocultos.get(3)).longValue();
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			
			CenDatosColegialesEstadoAdm admin=new CenDatosColegialesEstadoAdm(this.getUserName(request),user,idInstitucionPersona,idPersona);			
			
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			ocultos = (Vector)formulario.getDatosTablaOcultos(0);
			
			// Obtener los datos colegiales del usuario
			infoEntrada=(CenDatosColegialesEstadoBean)admin.obtenerEntradaEstadoColegial((String)ocultos.get(3),(String)ocultos.get(1),(String)ocultos.get(5));			
			
			Vector v = colegiadoAdm.getEstadosColegiales(new Long(idPersona), new Integer(idInstitucionPersona));
		    if (v.size()<1)
				request.getSession().setAttribute("DATABACKUP_EST",new Row());
			else
				request.getSession().setAttribute("DATABACKUP_EST",v.firstElement());
			// Paso valores originales del registro al session para tratar siempre copn los mismos valores
			// y no los de posibles modificaciones
			//request.getSession().setAttribute("DATABACKUP_ESTADOS", infoEntrada);

			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", infoEntrada);
			request.setAttribute("NOMBRE", (String)ocultos.get(4));
			request.setAttribute("NUMERO", (String)ocultos.get(0));			
			
			// Paso el origen			
			Object remitente=(Object)"modificar";
			request.setAttribute("modelo",remitente);			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}					
		return (result);*/
		String numero = "";
		String nombre = "";
		String result = "editar";
		CenColegiadoBean datosColegiales;		
		Vector datosEstado;		
		Vector datosSeguro = null;
		Object remitente = null;
		boolean esResidente = true;

		try {
			DatosColegiacionForm miForm = (DatosColegiacionForm) formulario;

			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			Integer idInst=new Integer(idInstitucion);

			String accion;
			accion = "editar";

			Long idPersona;
			Integer idInstPers;
			Vector ocultos = (Vector)formulario.getDatosTablaOcultos(0);
			if (ocultos == null) {
				
				idPersona  = new Long(miForm.getIdPersona());
				idInstPers = new Integer(miForm.getIdInstitucion());
			}
			else {
				idPersona  = new Long((String)ocultos.get(0));
				idInstPers = new Integer((String)ocultos.get(1));
			}
			
			
			
			// Si he cambiado el tipo de colegiacion de comunitario a espanol se permite modificar el numero de colegiado. Vengo de editar
			if (miForm.getModo() != null && miForm.getModo().equalsIgnoreCase("abrirEditarNColegiado")) {
			    accion = "editar";
			    request.setAttribute("editarNColegiado", "1");
			}
			else {
			  if (miForm.getModo() != null && miForm.getModo().equalsIgnoreCase("editar")) {// Si se hace cualquier otra modificacion no se tiene que dejar
			  	                                                                           // modificar el numero de colegiado. Vengo de editar
					 accion = "editar";
					 
			  }
			}
			
			String idInstitucionPersona = idInstPers.toString();
			String editarNColegiado = request.getParameter("editarNColegiado");
			if (editarNColegiado!=null) {
				request.setAttribute("editarNColegiado", editarNColegiado);
			}else{
				request.setAttribute("editarNColegiado", "");
			}
			// Obtengo manejadores para accesos a las BBDDs (cuidado con ls identificadores de usuario)
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserName(request),user,idInstPers.intValue(),idPersona.longValue());

			//CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserName(request));		
			CenTiposSeguroAdm admSeguro = new CenTiposSeguroAdm(this.getUserBean(request));		

			// Obtengo informacion del colegiado
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));		
			datosColegiales=colegiadoAdm.getDatosColegiales(idPersona,idInstPers);
			numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			datosEstado=colegiadoAdm.getEstadosColegiales(idPersona,idInstPers, this.getLenguaje(request));
			if (datosColegiales.getIdTipoSeguro()!=null){
				datosSeguro=admSeguro.obtenerEntradaTiposSeguro(datosColegiales.getIdTipoSeguro().toString());
			}

            // Recuperamos el estado colegial para mostrarlo en la cabecera
				CenClienteAdm admCli = new CenClienteAdm(this.getUserBean(request));
				String estadocolegial=admCli.getEstadoColegial(idPersona.toString(), idInstPers.toString());				 
				request.setAttribute("ESTADOCOLEGIAL",estadocolegial);
			
			// Comprueba si el usuario reside en algun otro colegio
			if (idPersona.compareTo(new Long(1))>0){
				esResidente=colegiadoAdm.getResidenciaColegio(idPersona.toString(),idInstPers.toString());
			}	
			
			if (esResidente){
				request.setAttribute("RESIDENTE", ClsConstants.DB_TRUE);
			}
			else{
				request.setAttribute("RESIDENTE", ClsConstants.DB_FALSE);
			}
			
			// Si la operacion puede implicar modificacion realizo una copia de los datos originales
			// para el proceso de modificacion en la BBDD
			if (accion.equalsIgnoreCase("edicion")||accion.equalsIgnoreCase("editar")){
				request.getSession().setAttribute("DATABACKUP",datosColegiales);
				if (datosEstado.size()<1)
					request.getSession().setAttribute("DATABACKUP_EST",new Row());
				else
					request.getSession().setAttribute("DATABACKUP_EST",datosEstado.firstElement());
			}			
			
			// Paso de parametros empleando request 
			request.setAttribute("IDPERSONA", idPersona);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("IDINSTITUCIONPERSONA", idInstitucionPersona);			
			request.setAttribute("ACCION", accion);
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);
			request.setAttribute("DATCOLEGIAL", datosColegiales);
			request.setAttribute("DATESTADO", datosEstado);
			request.setAttribute("DATSEGURO", datosSeguro);
			request.setAttribute("PESTANASITUACION", "1");
		
			// Paso el modo
			if (accion.equalsIgnoreCase("edicion")||accion.equalsIgnoreCase("editar")){
				remitente=(Object)"modificar";
			}
			else{
				remitente=(Object)"consulta";				
			}
			request.setAttribute("modelo",remitente);
			
			// DE MOMENTO NO -> idPersona, accion e idInstitucionPersona los guardo en session porque me interesa 
			// acceder a ellos en varios lugares
			
			//request.getSession().setAttribute("IDPERSONA", idPersona);
			//request.getSession().setAttribute("ACCION", accion);		
			//request.getSession().setAttribute("IDINSTITUCIONPERSONA", idInstitucionPersona);		
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		} 									
		return result;

	}
	
	
}


