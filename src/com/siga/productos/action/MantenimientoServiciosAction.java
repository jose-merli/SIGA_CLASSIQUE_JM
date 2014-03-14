/*
 * VERSIONES:
 * 		miguel.villegas - 3-2-2005 - Creacion
 *	
 */

/**
 * Clase action para el mantenimiento de servicios.<br/>
 * Gestiona la edicion, borrado, consulta y mantenimiento de los servicios y sus precios
 * @version modiifcado por david.sanchezp para incluir el concepto.  
 */

package com.siga.productos.action;


import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesProductosServicios;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ConCampoConsultaAdm;
import com.siga.beans.ConCampoConsultaBean;
import com.siga.beans.ConConsultaAdm;
import com.siga.beans.ConConsultaBean;
import com.siga.beans.ConCriterioConsultaAdm;
import com.siga.beans.ConCriterioConsultaBean;
import com.siga.beans.ConOperacionConsultaAdm;
import com.siga.beans.ConOperacionConsultaBean;
import com.siga.beans.ConTablaConsultaAdm;
import com.siga.beans.ConTablaConsultaBean;
import com.siga.beans.PysFormaPagoAdm;
import com.siga.beans.PysFormaPagoBean;
import com.siga.beans.PysFormaPagoServiciosAdm;
import com.siga.beans.PysFormaPagoServiciosBean;
import com.siga.beans.PysPreciosServiciosAdm;
import com.siga.beans.PysPreciosServiciosBean;
import com.siga.beans.PysServiciosInstitucionAdm;
import com.siga.beans.PysServiciosInstitucionBean;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.productos.form.MantenimientoServiciosForm;


public class MantenimientoServiciosAction extends MasterAction {

	static public final String ID_MODULO_PYS="9";
	
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
				}else if (accion.equalsIgnoreCase("nuevoCriterio")){
					mapDestino = nuevoCriterio(mapping, miForm, request, response);					
				}else if (accion.toLowerCase().startsWith("condicionsuscripcionautomatica")){
					mapDestino = condicionSuscripcionAutomatica(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("insertarCondicionSuscripcionAutomatica")){
					mapDestino = insertarCondicionSuscripcionAutomatica(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("insertarCriterio")){
					mapDestino = insertarCriterio(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("insertarCriterioConPrecioPorDefecto")){
					mapDestino = insertarCriterioConPrecioPorDefecto(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("modificarCriterio")){
					mapDestino = modificarCriterio(mapping, miForm, request, response);					
				}else if (accion.equalsIgnoreCase("configurarEliminacion")){
					mapDestino = configurarEliminacion(mapping, miForm, request, response);					
				}else if (accion.equalsIgnoreCase("eliminarSuscripcion")){
					mapDestino = eliminarSuscripcionAutomatica(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("cerrarCriterio")){
					mapDestino = cerrarCriterio(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("fechaEfectiva")){
					mapDestino = "fechaEfectiva";		
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new SIGAException("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		} catch (SIGAException es) { 
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="abrir";
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", idInstitucion);				
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}			
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try{
			//recogemos del formulario el tipo de campo seleccionado
			MantenimientoServiciosForm miform = (MantenimientoServiciosForm)formulario;
			String tipoCampo = (String)miform.getTipoCampo();
			String idCampo = (String)miform.getCampo();
			
			//consultamos en BD la SelectAyuda
			ConCampoConsultaAdm camposAdm = new ConCampoConsultaAdm(this.getUserBean(request));
			String condicion = " where " +ConCampoConsultaBean.C_IDCAMPO +"="+idCampo+ " ";  
			ConCampoConsultaBean campoBean = (ConCampoConsultaBean)((Vector)camposAdm.select(condicion)).get(0);
			String selectAyuda = (String)campoBean.getSelectAyuda();
			Vector datosValor = new Vector(); 
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
				
			//lanzamos la select ayuda para recuperar todos los campos
			try{
				//Si hay SELECT consulto:
				if (selectAyuda!=null && !selectAyuda.equals("")) {
					selectAyuda = selectAyuda.replaceAll("@IDINSTITUCION@",user.getLocation());
					selectAyuda = selectAyuda.replaceAll("@IDIOMA@",user.getLanguage());
					datosValor = (Vector)camposAdm.selectGenerico(selectAyuda);
					
				}
					
				else
					datosValor = null;
			}catch(Exception e){
				//si falla es porque los valores son texto
				datosValor = null;
			}
			
			//lanzamos la consulta que nos recuperará las posibles operaciones
			ConOperacionConsultaAdm operacionAdm = new ConOperacionConsultaAdm(this.getUserBean(request));
			String condicionOper = " where " + ConOperacionConsultaBean.C_TIPOOPERADOR +"='"+campoBean.getTipoCampo()+"' "; 
			Vector datosOperador = (Vector)operacionAdm.select(condicionOper);
			
			//pasamos todos los parametros por la request
			request.setAttribute("tipoCampo", tipoCampo);
			request.setAttribute("datosValor", datosValor);
			request.setAttribute("datosOperador", datosOperador);
			
		}catch(Exception e){}
		return "abrirAvanzada";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		return null;
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
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="";
		String refresco="";
		Vector ocultos=new Vector();
		Vector infoServicio=new Vector();
		Vector precioServicio=new Vector();		
		Vector pagoInt=new Vector();
		Vector pagoComun=new Vector();
		Vector pagoSec=new Vector();
		Vector infoPrecios=new Vector();		
		boolean porDefecto=false;
		
		try {
			
			MantenimientoServiciosForm form = (MantenimientoServiciosForm) formulario;			
			Object remitente=(Object)"modificar";
			request.setAttribute("modelo",remitente);
			request.getSession().setAttribute("modo","modificar");
			request.getSession().setAttribute("modoResultado","modificar");

			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
						
			refresco=form.getRefresco();

			//Actualizar ventana modal (de nuevo a edicion):
			if ((ocultos==null)||(refresco.equalsIgnoreCase("refresco"))){ 
				PysServiciosInstitucionAdm adminPI=new PysServiciosInstitucionAdm(this.getUserBean(request));
				PysPreciosServiciosAdm adminPS=new PysPreciosServiciosAdm(this.getUserBean(request));				
				infoServicio=adminPI.obtenerInfoServicio(form.getIdInstitucion(),form.getIdTipoServicios(),form.getIdServicio(),form.getIdServiciosInstitucion());
				pagoInt=adminPI.obtenerFormasPago(form.getIdInstitucion(),form.getIdTipoServicios(),form.getIdServicio(),form.getIdServiciosInstitucion(),ClsConstants.TIPO_PAGO_INTERNET);
				pagoSec=adminPI.obtenerFormasPago(form.getIdInstitucion(),form.getIdTipoServicios(),form.getIdServicio(),form.getIdServiciosInstitucion(),ClsConstants.TIPO_PAGO_SECRETARIA);			
				pagoComun=adminPI.obtenerFormasPago(form.getIdInstitucion(),form.getIdTipoServicios(),form.getIdServicio(),form.getIdServiciosInstitucion(),ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);
				infoPrecios=adminPS.getPrecios(form.getIdInstitucion(),form.getIdTipoServicios(),form.getIdServicio(),form.getIdServiciosInstitucion());			
				
				// Paso valores originales del registro al session para tratar siempre copn los mismos valores
				// y no los de posibles modificaciones
				request.getSession().setAttribute("DATABACKUP", infoServicio);
				
				// Paso valores para dar valores iniciales al formulario			
				request.setAttribute("container", infoServicio);
				request.setAttribute("container_I", pagoInt);			
				request.setAttribute("container_S", pagoSec);
				request.setAttribute("container_A", pagoComun);
				request.setAttribute("DATESTADO", infoPrecios);
				form.reset(mapping,request);
				
				result="editar";
			}
			else{
				// Si se trata de mostrar datos de los servicios
				if (ocultos.size()==4){
					PysServiciosInstitucionAdm admin=new PysServiciosInstitucionAdm(this.getUserBean(request));
					PysPreciosServiciosAdm adminPS=new PysPreciosServiciosAdm(this.getUserBean(request));				
					infoServicio=admin.obtenerInfoServicio((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3));
					pagoInt=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_INTERNET);
					pagoSec=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_SECRETARIA);			
					pagoComun=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);
					infoPrecios=adminPS.getPrecios((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3));			
					
					// Paso valores originales del registro al session para tratar siempre copn los mismos valores
					// y no los de posibles modificaciones
					request.getSession().setAttribute("DATABACKUP", infoServicio);
					
					// Paso valores para dar valores iniciales al formulario			
					request.setAttribute("container", infoServicio);
					request.setAttribute("container_I", pagoInt);			
					request.setAttribute("container_S", pagoSec);
					request.setAttribute("container_A", pagoComun);
					request.setAttribute("DATESTADO", infoPrecios);
					
					result="editar";
				}
				else{ // si se trata de mostrar datos de los precios
					PysPreciosServiciosAdm admin=new PysPreciosServiciosAdm(this.getUserBean(request));
					Hashtable claves= new Hashtable();
					claves.put(PysPreciosServiciosBean.C_IDINSTITUCION,(String)ocultos.get(0));
					claves.put(PysPreciosServiciosBean.C_IDTIPOSERVICIOS,(String)ocultos.get(1));
					claves.put(PysPreciosServiciosBean.C_IDSERVICIO,(String)ocultos.get(2));
					claves.put(PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION,(String)ocultos.get(3));
					claves.put(PysPreciosServiciosBean.C_IDPERIODICIDAD,(String)ocultos.get(4));
					claves.put(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS,(String)ocultos.get(5));				
					precioServicio=admin.select(claves);	
					
					//Para que la tabla de los criterio que afectan a ese precio se muestre bien,
					//hay que pasarle el resultado de la consulta de criterios en el formato correcto
					//el formato es
					
					//	*conector_campo_operador_valor_idCampo__idOperador_idValor_abrirParentesis_cerrarParentesis_
					 
					//	donde los siguientes campos tambien tienen un formato concreto:
					 
					//	idCampo: 		idCampo,tipoCampo,idTabla
					//	idOperador: 	idOperador,simbolo
					
					//comenzamos recuperando el idConsulta del Bean seleccionado y
					//despues consultado en la tabla los criterios, con ese idCOnsulta
					PysPreciosServiciosBean precioBean = (PysPreciosServiciosBean)precioServicio.get(0);
					ConCriterioConsultaAdm criteriosAdm = new ConCriterioConsultaAdm(this.getUserBean(request));

					//puede que no tenga criterios o la consulta luego metemos la consulta dentro de un try,
					//en cuyo caso no debe devolver ningun String, pero tampoco es un fallo.
					String criteriosString = "";
					String idConsulta=precioBean.getIdConsulta()==null?"0":((Long)precioBean.getIdConsulta()).toString();
					try {
						String consultaCriterios = " select ca."+ConCampoConsultaBean.C_NOMBREENCONSULTA+" CAMPO, ca."
													+ConCampoConsultaBean.C_IDCAMPO+" IDCAMPO, ta."
													+ConTablaConsultaBean.C_IDTABLA+" IDTABLA, ta."
													+ConTablaConsultaBean.C_DESCRIPCION+" TABLA,"+ 
													" ca."+ConCampoConsultaBean.C_TIPOCAMPO+" TIPOCAMPO, op."
													+ConOperacionConsultaBean.C_IDOPERACION+" OPERADOR, op."
													+ConOperacionConsultaBean.C_SIMBOLO+" SIMBOLO, ca."
													+ConCampoConsultaBean.C_SELECTAYUDA+" SELECTAYUDA, cr."
													+ConCriterioConsultaBean.C_VALOR+" VALOR, cr."
													+ConCriterioConsultaBean.C_ABRIRPAR+" ABRIRPAR, cr."
													+ConCriterioConsultaBean.C_CERRARPAR+" CERRARPAR, cr."
													+ConCriterioConsultaBean.C_OPERADOR+" CONECTOR,"
													+UtilidadesMultidioma.getCampoMultidiomaSimple("op."+ConOperacionConsultaBean.C_DESCRIPCION, this.getLenguaje(request))+" DESCRIPCION"+
													" from "+ConCriterioConsultaBean.T_NOMBRETABLA+" cr, "
													+ConTablaConsultaBean.T_NOMBRETABLA+" ta, "
													+ConCampoConsultaBean.T_NOMBRETABLA+" ca, "
													+ConOperacionConsultaBean.T_NOMBRETABLA+" op"+
													" where ca."+ConCampoConsultaBean.C_IDCAMPO+" = cr."+ConCriterioConsultaBean.C_IDCAMPO+
													" and op."+ConOperacionConsultaBean.C_IDOPERACION+" = cr."+ConCriterioConsultaBean.C_IDOPERACION+
													" and ta."+ConTablaConsultaBean.C_IDTABLA+" = ca."+ConCampoConsultaBean.C_IDTABLA+
													" and cr."+ConCriterioConsultaBean.C_IDINSTITUCION+" = "+ (String)ocultos.get(0)+
													" and cr."+ConCriterioConsultaBean.C_IDCONSULTA+" = "+
													//((Long)precioBean.getIdConsulta()).toString()+" ";
													idConsulta+" "+
													" order by cr."+ConCriterioConsultaBean.C_ORDEN+" asc";
						
						Vector vCriterios = (Vector)criteriosAdm.selectGenerico(consultaCriterios);
						//creamos el String
						for (int cont=0; cont<vCriterios.size(); cont++){
							Hashtable criterio = (Hashtable)vCriterios.get(cont);

							//Ahora tenemos todos los campos a falta del idValor
							//Ejecutamos la selectAyuda del campo, y recuperamos el ID 
							//para el valor con descripcion = al valor que ya tenemos
							ConCampoConsultaAdm camposAdm = new ConCampoConsultaAdm(this.getUserBean(request));
						
							//Si tenemos la select buscamos el idvalor: 
							if (criterio.get("SELECTAYUDA")!=null && !((String)criterio.get("SELECTAYUDA")).equals("")) {
								String con = (String)criterio.get("SELECTAYUDA");
								con = con.replaceAll("@IDINSTITUCION@",usr.getLocation());
								con = con.replaceAll("@IDIOMA@" , this.getUserBean(request).getLanguage());
								Vector campos = (Vector)camposAdm.selectGenerico(con);
								for (int contador=0; contador<campos.size(); contador++){
									Hashtable campoHash = (Hashtable)campos.get(contador);
									if (((String)campoHash.get("DESCRIPCION")).equalsIgnoreCase((String)criterio.get("VALOR"))){
										contador = campos.size()+1;
										String aux = (String)campoHash.get("ID");
										
										int p=-1;
										if ((p = aux.indexOf("#"))!=-1) { 
											aux=aux.substring(0,p) + "$" + aux.substring(p+1,aux.length());
										}
										// RGG criterio.put("IDVALOR", (String)campoHash.get("ID"));
										criterio.put("IDVALOR", aux);
									}
								}//Fin del for interior
							}
							
							//para cada uno de los criterios el formato indicado:
							//	*conector_campo_operador_valor_idCampo__idOperador_idValor_
							//	donde los siguientes campos tambien tienen un formato concreto:
							//	idCampo: 		idCampo,tipoCampo,idTabla
							//	idOperador: 	idOperador,simbolo
							String abrirPar = (String)criterio.get("ABRIRPAR");
							String cerrarPar = (String)criterio.get("CERRARPAR");
							if (abrirPar!=null && abrirPar.equals("1")) abrirPar="1"; else abrirPar="0";
							if (cerrarPar!=null && cerrarPar.equals("1")) cerrarPar="1"; else cerrarPar="0";
							
							String conector = (criterio.containsKey("CONECTOR")?(String)criterio.get("CONECTOR"):" ");
							criteriosString += "*" + conector + "_" +
													(String)criterio.get("CAMPO")+"_"+
													(String)criterio.get("DESCRIPCION")+"_"+
													(((String)criterio.get("VALOR")).equalsIgnoreCase("NULL")?"":(String)criterio.get("VALOR"))+"_"+
													(String)criterio.get("IDCAMPO")+","+
													(String)criterio.get("TIPOCAMPO")+","+
													(String)criterio.get("IDTABLA")+"_"+
													(String)criterio.get("OPERADOR")+","+
													(String)criterio.get("SIMBOLO")+"_"+
													(String)criterio.get("IDVALOR")+"_"+
													abrirPar+"_"+
													cerrarPar+"_";
													
													
							
						}//Fin del for principal
					} catch(Exception e){
						e.printStackTrace();
					}
					
					
					// Paso valores originales del registro al session para tratar siempre copn los mismos valores
					// y no los de posibles modificaciones
					request.getSession().setAttribute("DATABACKUP", precioServicio);

					//seleccionamos la consulta para futuras modificaciones
					ConConsultaAdm consultaAdm = new ConConsultaAdm(this.getUserBean(request));
					try{
						String consultaConsulta = " where " + ConConsultaBean.C_IDINSTITUCION + "=" + (String)ocultos.get(0)+
													" and " + ConConsultaBean.C_IDCONSULTA +"= "+ idConsulta+" ";
			            ConConsultaBean consultaBean = (ConConsultaBean)((Vector)consultaAdm.select(consultaConsulta)).get(0);
			            Hashtable consultaHash = (Hashtable)consultaBean.getOriginalHash().clone();
			            request.getSession().setAttribute("DATABACKUPCONSULTA", consultaHash);
					}catch(Exception e){
						//si no hay consulta no debe fallar, simplemente no se mostrarán criterios
						porDefecto = true;
					}
					
					// Paso valores para dar valores iniciales al formulario
					request.setAttribute("porDefecto",precioBean.getPorDefecto());
					if (precioBean.getPorDefecto().equalsIgnoreCase(ClsConstants.DB_TRUE)){
						request.getSession().setAttribute("modoResultado","consulta");
						request.getSession().setAttribute("modo","consulta");
					}
					request.setAttribute("container", precioServicio);
					request.setAttribute("institucion",(String)ocultos.get(0));
					request.setAttribute("tipoServicio",(String)ocultos.get(1));
					request.setAttribute("servicio",(String)ocultos.get(2));
					request.setAttribute("servicioInstitucion",(String)ocultos.get(3));
					// Pasamos el String para presentar los criterios
					request.setAttribute("resultado",criteriosString);
					result="editarCriterio";
				}
			}
			
			if(!infoServicio.isEmpty())
			{
				String idConsulta = (String)(((Row) infoServicio.get(0)).getRow()).get(PysServiciosInstitucionBean.C_IDCONSULTA);
				request.setAttribute("idConsulta", idConsulta);
			}
		}	
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}		
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		String result="";
		Vector ocultos=new Vector();
		Vector infoServ=new Vector();
		Vector infoPrecios=new Vector();		
		Vector precioServicio=new Vector();
		Vector pagoInt=new Vector();
		Vector pagoComun=new Vector();
		Vector pagoSec=new Vector();		
		boolean porDefecto=false;
		
		try {
			
			MantenimientoServiciosForm form = (MantenimientoServiciosForm) formulario;
			Object remitente=(Object)"consulta";
			request.setAttribute("modelo",remitente);
			request.getSession().setAttribute("modo","consulta");
			request.getSession().setAttribute("modoResultado","consulta");
			
			
			// Mostrar valores del formulario en MantenimientoServicios (posible traslado a editar o abrir avanzado)
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
															
			// Si se trata de mostrar datos de los servicios
			if (ocultos.size()==4){
				PysServiciosInstitucionAdm admin=new PysServiciosInstitucionAdm(this.getUserBean(request));
				PysPreciosServiciosAdm adminPS=new PysPreciosServiciosAdm(this.getUserBean(request));				
				infoServ=admin.obtenerInfoServicio((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3));
				pagoInt=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_INTERNET);
				pagoSec=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_SECRETARIA);			
				pagoComun=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);
				infoPrecios=adminPS.getPrecios((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3));			
				
				// Paso valores para dar valores iniciales al formulario			
				request.setAttribute("container", infoServ);
				request.setAttribute("container_I", pagoInt);			
				request.setAttribute("container_S", pagoSec);
				request.setAttribute("container_A", pagoComun);
				request.setAttribute("DATESTADO", infoPrecios);
				
				result="ver";								
			}
			else{ // si se trata de mostrar datos de los precios
				PysPreciosServiciosAdm admin=new PysPreciosServiciosAdm(this.getUserBean(request));
				Hashtable claves= new Hashtable();
				claves.put(PysPreciosServiciosBean.C_IDINSTITUCION,(String)ocultos.get(0));
				claves.put(PysPreciosServiciosBean.C_IDTIPOSERVICIOS,(String)ocultos.get(1));
				claves.put(PysPreciosServiciosBean.C_IDSERVICIO,(String)ocultos.get(2));
				claves.put(PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION,(String)ocultos.get(3));
				claves.put(PysPreciosServiciosBean.C_IDPERIODICIDAD,(String)ocultos.get(4));
				claves.put(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS,(String)ocultos.get(5));				
				precioServicio=admin.select(claves);	
//				PAra que la tabla de los criterio que afectan a ese precio se muestre bien,
				//hay que pasarle el resultado de la consulta de criterios en el formato correcto
				//el formato es
				
				//	*conector_campo_operador_valor_idCampo__idOperador_idValor_
				 
				//	donde los siguientes campos tambien tienen un formato concreto:
				 
				//	idCampo: 		idCampo,tipoCampo,idTabla
				//	idOperador: 	idOperador,simbolo
				
				//comenzamos recuperando el idConsulta del Bean seleccionado y
				//despues consultado en la tabla los criterios, con ese idCOnsulta
				PysPreciosServiciosBean precioBean = (PysPreciosServiciosBean)precioServicio.get(0);
				ConCriterioConsultaAdm criteriosAdm = new ConCriterioConsultaAdm(this.getUserBean(request));
				//puede que no tenga criterios o la consulta luego metemos la consulta dentro de un try,
				//en cuyo caso no debe devolver ningun String, pero tampoco es un fallo.
				String criteriosString = "";
				String idConsulta=precioBean.getIdConsulta()==null?"0":((Long)precioBean.getIdConsulta()).toString();
				try{
					String consultaCriterios = "  select ca."+ConCampoConsultaBean.C_NOMBREENCONSULTA+" CAMPO, ca."
													+ConCampoConsultaBean.C_IDCAMPO+" IDCAMPO, ta."
													+ConTablaConsultaBean.C_IDTABLA+" IDTABLA, ta."
													+ConTablaConsultaBean.C_DESCRIPCION+" TABLA,"+ 
													" ca."+ConCampoConsultaBean.C_TIPOCAMPO+" TIPOCAMPO, op."
													+ConOperacionConsultaBean.C_IDOPERACION+" OPERADOR, op."
													+ConOperacionConsultaBean.C_SIMBOLO+" SIMBOLO, ca."
													+ConCampoConsultaBean.C_SELECTAYUDA+" SELECTAYUDA, cr."
													+ConCriterioConsultaBean.C_VALOR+" VALOR, cr."
													+ConCriterioConsultaBean.C_ABRIRPAR+" ABRIRPAR, cr."
													+ConCriterioConsultaBean.C_CERRARPAR+" CERRARPAR, cr."
													+ConCriterioConsultaBean.C_OPERADOR+" CONECTOR,"
													+UtilidadesMultidioma.getCampoMultidiomaSimple("op."+ConOperacionConsultaBean.C_DESCRIPCION, this.getLenguaje(request))+" DESCRIPCION"+
												" from "+ConCriterioConsultaBean.T_NOMBRETABLA+" cr, "
												+ConTablaConsultaBean.T_NOMBRETABLA+" ta, "
												+ConCampoConsultaBean.T_NOMBRETABLA+" ca, "
												+ConOperacionConsultaBean.T_NOMBRETABLA+" op"+
												" where ca."+ConCampoConsultaBean.C_IDCAMPO+" = cr."+ConCriterioConsultaBean.C_IDCAMPO+
												" and op."+ConOperacionConsultaBean.C_IDOPERACION+" = cr."+ConCriterioConsultaBean.C_IDOPERACION+
												" and ta."+ConTablaConsultaBean.C_IDTABLA+" = ca."+ConCampoConsultaBean.C_IDTABLA+
												" and cr."+ConCriterioConsultaBean.C_IDINSTITUCION+" = "+ (String)ocultos.get(0)+
												" and cr."+ConCriterioConsultaBean.C_IDCONSULTA+" = "+
												//((Long)precioBean.getIdConsulta()).toString()+" ";
												idConsulta+" "+
												" order by cr."+ConCriterioConsultaBean.C_ORDEN+" asc";
					
					Vector vCriterios = (Vector)criteriosAdm.selectGenerico(consultaCriterios);
					//creamos el String
					for (int cont=0; cont<vCriterios.size(); cont++){
						Hashtable criterio = (Hashtable)vCriterios.get(cont);

						//para cada uno de los criterios el formato indicado
						//	*conector_campo_operador_valor_idCampo__idOperador_idValor_
						//	donde los siguientes campos tambien tienen un formato concreto:
						//	idCampo: 		idCampo,tipoCampo,idTabla
						//	idOperador: 	idOperador,simbolo
						String abrirPar = (String)criterio.get("ABRIRPAR");
						String cerrarPar = (String)criterio.get("CERRARPAR");
						if (abrirPar!=null && abrirPar.equals("1")) abrirPar="1"; else abrirPar="0";
						if (cerrarPar!=null && cerrarPar.equals("1")) cerrarPar="1"; else cerrarPar="0";

						String conector = (criterio.containsKey("CONECTOR")?(String)criterio.get("CONECTOR"):" ");
						criteriosString += "*" + conector + "_" +
												(String)criterio.get("CAMPO")+"_"+
												(String)criterio.get("DESCRIPCION")+"_"+
												(((String)criterio.get("VALOR")).equalsIgnoreCase("NULL")?"":(String)criterio.get("VALOR"))+"_"+
												(String)criterio.get("IDCAMPO")+","+
												(String)criterio.get("TIPOCAMPO")+","+
												(String)criterio.get("IDTABLA")+"_"+
												(String)criterio.get("OPERADOR")+","+
												(String)criterio.get("SIMBOLO")+"_"+
												(String)criterio.get("VALOR")+"_" +
												abrirPar+"_"+
												cerrarPar+"_";
												//el idValor no hace falta recuperarlo en la consulta
												//pero para no saltarnos el formato anhadimos el Valor
						
					}
				}
				catch(Exception e){
					ClsLogging.writeFileLogError("",e,1);
				}

				// Paso valores originales del registro al session para tratar siempre copn los mismos valores
				// y no los de posibles modificaciones
				request.getSession().setAttribute("DATABACKUP", precioServicio);
				
//				seleccionamos la consulta para futuras modificaciones
				ConConsultaAdm consultaAdm = new ConConsultaAdm(this.getUserBean(request));
				try{
					String consultaConsulta = " where " + ConConsultaBean.C_IDINSTITUCION + "=" + (String)ocultos.get(0)+
												" and " + ConConsultaBean.C_IDCONSULTA +"= "+ idConsulta+" ";
		            ConConsultaBean consultaBean = (ConConsultaBean)((Vector)consultaAdm.select(consultaConsulta)).get(0);
		            Hashtable consultaHash = (Hashtable)consultaBean.getOriginalHash().clone();
		            request.getSession().setAttribute("DATABACKUPCONSULTA", consultaHash);
				}catch(Exception e){
					//si no hay consulta no debe fallar, simplemente no se mostrarán criterios
					porDefecto = true;
				}
				
				// Paso valores para dar valores iniciales al formulario
				//if (porDefecto) request.setAttribute("porDefecto","SI");
				request.setAttribute("porDefecto",precioBean.getPorDefecto());
				
				// Paso valores para dar valores iniciales al formulario			
				request.setAttribute("container", precioServicio);
				request.setAttribute("institucion",(String)ocultos.get(0));
				request.setAttribute("tipoServicio",(String)ocultos.get(1));
				request.setAttribute("servicio",(String)ocultos.get(2));
				request.setAttribute("servicioInstitucion",(String)ocultos.get(3));
				
//				 Pasamos el String para presentar los criterios
				request.setAttribute("resultado",criteriosString);
				
				result="verCriterio";
			}			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}		
		return (result);
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		PysFormaPagoBean bean=new PysFormaPagoBean ();
		String result="nuevo";
		try{
						
			Object remitente=(Object)"insertar";
			request.setAttribute("modelo",remitente);
		}	
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}		
		return result;
	}

	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub		
		
		String result="";
		String servicio;
		String pago;		
		String tipoServicio;
		String[] pagosInternet;
		String[] pagosSecretaria;		
		ArrayList pagosCom = new ArrayList();		
		Hashtable pagosHash;
		UserTransaction tx = null;
		boolean correcto=true;

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			PysServiciosInstitucionAdm admin=new PysServiciosInstitucionAdm(this.getUserBean(request));
			PysFormaPagoServiciosAdm adminFPS=new PysFormaPagoServiciosAdm(this.getUserBean(request));
			PysFormaPagoAdm adminFP = new PysFormaPagoAdm(this.getUserBean(request));
			PysPreciosServiciosAdm adminPS = new PysPreciosServiciosAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransactionPesada(); 
			// Obtengo los datos del formulario
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			servicio=miForm.getServicio();
			tipoServicio=miForm.getTipoServicio();
			pagosInternet=miForm.getFormaPagoInternet();
			pagosSecretaria=miForm.getFormaPagoSecretaria();
			pagosCom=pagosComunes(pagosInternet,pagosSecretaria);
			// Cargo la tabla hash con los valores del formulario para insertar
			Hashtable hash = formulario.getDatos();
			// Anhado valores que faltan
			hash.put(PysServiciosInstitucionBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS,tipoServicio);			
			hash.put(PysServiciosInstitucionBean.C_IDSERVICIO,servicio);
			
			if (miForm.getNoPondera()!=null && miForm.getNoPondera().equals("1")) {
				hash.put(PysServiciosInstitucionBean.C_FACTURACIONPONDERADA,"0");
			} else {
				hash.put(PysServiciosInstitucionBean.C_FACTURACIONPONDERADA,"1");
			}
			
			// Obtengo el IDSERVICIOINSTITUCION y los campos SOLICITARBAJA y SOLICITARALTA si no fueron seleccionados
			admin.prepararInsert(hash);			
			// Comienzo la transaccion
			tx.begin();		
				
			// Inserto
			correcto=admin.insert(hash); 			
			
			// Creacion de las hash para insertar las diferentes formas de pago de Internet	y Secretaria
			Hashtable hashAux = new Hashtable();
			Vector vectorAux = new Vector();
			
			// Insercion Forma Pago Internet y Secretaria
			if (correcto){
				if (!pagosCom.isEmpty()){			
					int i=0;
					while(i<pagosCom.size()){
						if (((String)pagosCom.get(i)).compareToIgnoreCase("-1")!=0){
							hashAux.put(PysFormaPagoServiciosBean.C_IDINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION));																					
							hashAux.put(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS,hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIO,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO));				
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));				
							hashAux.put(PysFormaPagoServiciosBean.C_IDFORMAPAGO,(String)pagosCom.get(i));
							hashAux.put(PysFormaPagoServiciosBean.C_INTERNET,ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);
							correcto=adminFPS.insert(hashAux);					
						}	
						i++;				
					}
				}	
			}	
				
			// Insercion Forma Pago Internet
			if (correcto){			
				if (pagosInternet!=null){			
					int i=0;
					while(i<pagosInternet.length){
						if ((pagosInternet[i].compareToIgnoreCase("-1")!=0)&&(pagosInternet[i].compareToIgnoreCase("")!=0)&&(!estaPago(pagosInternet[i],pagosCom))){
							hashAux.put(PysFormaPagoServiciosBean.C_IDINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION));																					
							hashAux.put(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS,hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIO,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO));				
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));								
							hashAux.put(PysFormaPagoServiciosBean.C_IDFORMAPAGO,pagosInternet[i]);
							hashAux.put(PysFormaPagoServiciosBean.C_INTERNET,ClsConstants.TIPO_PAGO_INTERNET);
							correcto=adminFPS.insert(hashAux);					
						}	
						i++;				
					}
				}
			}	
			
			// Insercion Forma Pago Secretaria
			if (correcto){			
				if (pagosSecretaria!=null){			
					int j=0;			
					while(j<pagosSecretaria.length){
						if ((pagosSecretaria[j].compareToIgnoreCase("-1")!=0)&&(pagosSecretaria[j].compareToIgnoreCase("")!=0)&&(!estaPago(pagosSecretaria[j],pagosCom))){
							hashAux.put(PysFormaPagoServiciosBean.C_IDINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION));																					
							hashAux.put(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS,hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIO,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO));				
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));								
							hashAux.put(PysFormaPagoServiciosBean.C_IDFORMAPAGO,pagosSecretaria[j]);
							hashAux.put(PysFormaPagoServiciosBean.C_INTERNET,ClsConstants.TIPO_PAGO_SECRETARIA);							
							correcto=adminFPS.insert(hashAux);					
						}	
						j++;				
					}						
				}
			}
			
			// Insercion Precio por defecto
			if (correcto){
				String query = UtilidadesProductosServicios.getQueryPorDefecto();
				query = UtilidadesProductosServicios.reemplazaString("@IDINSTITUCION@", (String)usr.getLocation(),query);
				Hashtable hashPrecio = new Hashtable();
				hashPrecio.put(PysPreciosServiciosBean.C_IDINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION));																					
				hashPrecio.put(PysPreciosServiciosBean.C_IDTIPOSERVICIOS,hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
				hashPrecio.put(PysPreciosServiciosBean.C_IDSERVICIO,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO));				
				hashPrecio.put(PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));
				hashPrecio.put(PysPreciosServiciosBean.C_IDPERIODICIDAD,hash.get(PysPreciosServiciosBean.C_IDPERIODICIDAD));
				hashPrecio.put(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS,adminPS.getIdPreciosServicios((String)hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION),(String)hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS),(String)hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO),(String)hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION),(String)hash.get(PysPreciosServiciosBean.C_IDPERIODICIDAD)));
				hashPrecio.put(PysPreciosServiciosBean.C_VALOR,hash.get(PysPreciosServiciosBean.C_VALOR));
				hashPrecio.put(PysPreciosServiciosBean.C_CRITERIOS, query);
				hashPrecio.put(PysPreciosServiciosBean.C_PORDEFECTO,ClsConstants.DB_TRUE);
				correcto=adminPS.insert(hashPrecio);						
			}
			
			if (correcto){
				tx.commit();
				// Cargo las claves para la "recarga" de la ventana modalen modo edicion
				request.setAttribute("institucion",hash.get(PysServiciosInstitucionBean.C_IDINSTITUCION));
				request.setAttribute("tipoServicio",hash.get(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS));
				request.setAttribute("servicio",hash.get(PysServiciosInstitucionBean.C_IDSERVICIO));
				request.setAttribute("servicioInstitucion",hash.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION));
				result=exitoEspecifico("messages.updated.success",request);			
			}			
		}	
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		}		

		return result;

	}

	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="error";
		String servicio;
		String pago;		
		String paramConsulta;		
		String tipoServicio;
		String idServInst;		
		String[] pagosInternet;
		String[] pagosSecretaria;
		ArrayList pagosCom = new ArrayList();		
		Hashtable pagosHash;
		Hashtable hashOriginal = new Hashtable();
		Enumeration filaOriginal; 		
		Row registroOriginal;
		UserTransaction tx=null;

		// Creacion de las hash para insertar las diferentes formas de pago de Internet	y Secretaria
		Hashtable hashAux = new Hashtable();
		Vector vectorAux = new Vector();
		Vector vectorFP = new Vector();
		Vector camposOcultos = new Vector();
		boolean correcto=true;
		
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			String fechaEfectiva=(String)request.getParameter("fechaEfectiva");
			GstDate gstDate = new GstDate();
			
			//inc-5907: Controlamos que no venga a nulo la fecha efectiva porque hay veces que se llama al proceso automatico
			//de suscripcion y al venir a nulo salta error en la aplicación.
			if (fechaEfectiva==null ||fechaEfectiva.equals("")){
				fechaEfectiva=gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request));
			}
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			PysServiciosInstitucionAdm admin=new PysServiciosInstitucionAdm(this.getUserBean(request));
			PysFormaPagoServiciosAdm adminFPS=new PysFormaPagoServiciosAdm(this.getUserBean(request));
			PysFormaPagoAdm adminFP = new PysFormaPagoAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransactionPesada(); 			
			// Obtengo los datos del formulario
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			servicio=miForm.getIdServicio();
			tipoServicio=miForm.getIdTipoServicios();		
			// Obtengo las diferentes formas de pago "modificadas"
			pagosInternet=miForm.getFormaPagoInternet();
			pagosSecretaria=miForm.getFormaPagoSecretaria();
			pagosCom=pagosComunes(pagosInternet,pagosSecretaria);			
			idServInst=miForm.getIdServInst();			
			// Cargo la tabla hash con los valores del formulario para insertar
			Hashtable hash = formulario.getDatos();			

			String bajaLogica = (String) request.getParameter("bajaLogica"); 
			String automatico = (String) request.getParameter("automatico");
			if (bajaLogica!=null) {
				hash.put("FECHABAJA","SYSDATE");
			} else {
				hash.put("FECHABAJA","");
				//hash.remove("FECHABAJA");
			}

			// Anhado valores que faltan
			hash.put(PysServiciosInstitucionBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS,tipoServicio);			
			hash.put(PysServiciosInstitucionBean.C_IDSERVICIO,servicio);			
			// Obtengo el IDSERVICIOSINSTITUCION y los campos SOLICITARBAJA y SOLICITARALTA si no fueron seleccionados
			admin.prepararInsert(hash);
			// Doy el valor correcto a IDSERVICIOSINSTITUCION
			hash.put(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,idServInst);

			if (miForm.getNoPondera()!=null && miForm.getNoPondera().equals("1")) {
				hash.put(PysServiciosInstitucionBean.C_FACTURACIONPONDERADA,"1");
			} else {
				hash.put(PysServiciosInstitucionBean.C_FACTURACIONPONDERADA,"0");
			}
			

			// Obtengo formas de pago existentes, construyo sentencia where para busqueda y realizo la busqueda
			paramConsulta = " WHERE " +
			  	PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDINSTITUCION + "=" + hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION) +
				" AND " +
				PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS + "=" + hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS) +
				" AND " +
				PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDSERVICIO + "=" + hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO) +
				" AND " +
				PysFormaPagoServiciosBean.T_NOMBRETABLA +"."+ PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION + "=" + hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION);
			
			vectorAux=adminFPS.select(paramConsulta);
			
			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			filaOriginal=((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
			while(filaOriginal.hasMoreElements()){
              	registroOriginal = (Row) filaOriginal.nextElement(); 
				hashOriginal.put(PysFormaPagoServiciosBean.C_IDINSTITUCION,registroOriginal.getString(PysServiciosInstitucionBean.C_IDINSTITUCION));
				hashOriginal.put(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS,registroOriginal.getString(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS));
				hashOriginal.put(PysServiciosInstitucionBean.C_IDSERVICIO,registroOriginal.getString(PysServiciosInstitucionBean.C_IDSERVICIO));
				hashOriginal.put(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,registroOriginal.getString(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION));
				hashOriginal.put(PysServiciosInstitucionBean.C_DESCRIPCION,registroOriginal.getString(PysServiciosInstitucionBean.C_DESCRIPCION));
				hashOriginal.put(PysServiciosInstitucionBean.C_CUENTACONTABLE,registroOriginal.getString(PysServiciosInstitucionBean.C_CUENTACONTABLE));
				hashOriginal.put(PysServiciosInstitucionBean.C_INICIOFINALPONDERADO,registroOriginal.getString(PysServiciosInstitucionBean.C_INICIOFINALPONDERADO));
				hashOriginal.put(PysServiciosInstitucionBean.C_PORCENTAJEIVA,registroOriginal.getString(PysServiciosInstitucionBean.C_PORCENTAJEIVA));
				hashOriginal.put(PysServiciosInstitucionBean.C_SOLICITARBAJA,registroOriginal.getString(PysServiciosInstitucionBean.C_SOLICITARBAJA));
				hashOriginal.put(PysServiciosInstitucionBean.C_SOLICITARALTA,registroOriginal.getString(PysServiciosInstitucionBean.C_SOLICITARALTA));
				hashOriginal.put(PysServiciosInstitucionBean.C_AUTOMATICO,registroOriginal.getString(PysServiciosInstitucionBean.C_AUTOMATICO));
			}
			
			// Comienzo la transaccion
			tx.begin();	
										
			// Actualizo la tabla
			correcto=admin.update(hash,hashOriginal);
			
			if (correcto){
				// paso la consulta realizada a un objeto enumeracion para recorrerlo facilmente
		    	Enumeration enumer = vectorAux.elements();
		    	// Creo un vector con las formas de pago (IDFORMAPAGO) existentes para esas caracteristicas
				while (enumer.hasMoreElements())
				{
					PysFormaPagoServiciosBean fila = (PysFormaPagoServiciosBean) enumer.nextElement();
		           	vectorFP.add(fila.getIdFormaPago().toString());
				}
	
				// Borro todas las formas de pago existentes
				Enumeration idFormasPago = vectorFP.elements();
				while (idFormasPago.hasMoreElements())
				{
					String nuevoPago = (String) idFormasPago.nextElement();
					hashAux.put(PysFormaPagoServiciosBean.C_IDINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION));																					
					hashAux.put(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS,hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
					hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIO,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO));				
					hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));				
					hashAux.put(PysFormaPagoServiciosBean.C_IDFORMAPAGO,nuevoPago);
					correcto=adminFPS.delete(hashAux);
				}	
					
				// Insercion Forma Pago Internet y Secretaria
				if (!pagosCom.isEmpty()){			
					int i=0;
					while(i<pagosCom.size()){
						if (((String)pagosCom.get(i)).compareToIgnoreCase("-1")!=0){							
							hashAux.put(PysFormaPagoServiciosBean.C_IDINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION));																					
							hashAux.put(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS,hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIO,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO));				
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));				
							hashAux.put(PysFormaPagoServiciosBean.C_IDFORMAPAGO,(String)pagosCom.get(i));
							hashAux.put(PysFormaPagoServiciosBean.C_INTERNET,ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);							
							correcto=adminFPS.insert(hashAux);					
						}	
						i++;				
					}
				}	
					
				// Insercion Forma Pago Internet
				if (pagosInternet!=null){			
					int i=0;
					while(i<pagosInternet.length){
						if ((pagosInternet[i].compareToIgnoreCase("-1")!=0)&&(pagosInternet[i].compareToIgnoreCase("")!=0)&&(!estaPago(pagosInternet[i],pagosCom))){
							hashAux.put(PysFormaPagoServiciosBean.C_IDINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION));																					
							hashAux.put(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS,hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIO,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO));				
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));								
							hashAux.put(PysFormaPagoServiciosBean.C_IDFORMAPAGO,pagosInternet[i]);
							hashAux.put(PysFormaPagoServiciosBean.C_INTERNET,ClsConstants.TIPO_PAGO_INTERNET);
							correcto=adminFPS.insert(hashAux);					
						}	
						i++;				
					}
				}	
				
				// Insercion Forma Pago Secretaria
				if (pagosSecretaria!=null){			
					int j=0;			
					while(j<pagosSecretaria.length){
						if ((pagosSecretaria[j].compareToIgnoreCase("-1")!=0)&&(pagosSecretaria[j].compareToIgnoreCase("")!=0)&&(!estaPago(pagosSecretaria[j],pagosCom))){
							hashAux.put(PysFormaPagoServiciosBean.C_IDINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION));																					
							hashAux.put(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS,hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIO,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO));				
							hashAux.put(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));								
							hashAux.put(PysFormaPagoServiciosBean.C_IDFORMAPAGO,pagosSecretaria[j]);
							hashAux.put(PysFormaPagoServiciosBean.C_INTERNET,ClsConstants.TIPO_PAGO_SECRETARIA);
							correcto=adminFPS.insert(hashAux);					
						}	
						j++;				
					}						
				}
			}
			
			if (correcto) {
				// Aplicamos el proceso de suscripcion automatica de un servicio
				if ((bajaLogica == null) && (miForm.getComprobarCondicion().booleanValue())) {
					String [] rc = EjecucionPLs.ejecutarPL_SuscripcionAutomatica((String)hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION),
																				 (String)hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS),
																				 (String)hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO),
																				 (String)hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION),
																			     (String)fechaEfectiva,
																				 ""+this.getUserName(request));
					if ((rc == null) || (!rc[0].equals("0")))
						throw new ClsExceptions ("Error al ejecutar el PL: PKG_SERVICIOS_AUTOMATICOS.PROCESO_SUSCRIPCION_AUTO: "+rc[1]);
					
					else correcto = true;
				}
				else {
					if (bajaLogica != null  && (!miForm.getComprobarCondicionBaja().booleanValue())) {
						String [] rc = EjecucionPLs.ejecutarPL_BajaServicio((String)hash.get(PysFormaPagoServiciosBean.C_IDINSTITUCION),
																		 	(String)hash.get(PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS),
																		 	(String)hash.get(PysFormaPagoServiciosBean.C_IDSERVICIO),
																		 	(String)hash.get(PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION),
																		 	(String)fechaEfectiva);
						if ((rc == null) || (!rc[0].equals("0")))
							throw new ClsExceptions ("Error al ejecutar el PL: PKG_SERVICIOS_AUTOMATICOS.PROCESO_BAJA_SERVICIO: "+rc[1]);
						
						else correcto = true;
					}
				}
			}

			if (correcto){
				tx.commit();				
				//result=exito("messages.updated.success",request);			
				result=exitoEspecifico("messages.updated.success",request);
			}	
			
			if (!correcto){
				tx.rollback();
				result=exitoEspecifico("messages.updated.error",request);
				
			}
		}
		
	    catch (SIGAException siga) {
	      throw siga;
	    }catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		}							
		return (result);		
	}


	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="error";
		int numElementos;		
		boolean correcto;		
		Hashtable hash = new Hashtable();		
		Vector camposVistos = new Vector();
		Vector camposOcultos = new Vector();
		UserTransaction tx=null;
		
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// Comienzo control de transacciones
			tx = usr.getTransaction(); 			
			// Obtengo los datos del formulario		
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;		
			camposVistos = miForm.getDatosTabla();
			numElementos = camposVistos.size();
			camposVistos = miForm.getDatosTablaVisibles(0);
			camposOcultos = miForm.getDatosTablaOcultos(0);
		
			tx.begin();
			
			// Si se trata de eliminar un servicio
			if (camposOcultos.size()==4){			
				PysServiciosInstitucionAdm admin=new PysServiciosInstitucionAdm(this.getUserBean(request));
				// Cargo la tabla hash con los valores del formulario para eliminar		
				hash.put(PysServiciosInstitucionBean.C_IDINSTITUCION,camposOcultos.get(0));
				hash.put(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS,camposOcultos.get(1));
				hash.put(PysServiciosInstitucionBean.C_IDSERVICIO,camposOcultos.get(2));
				hash.put(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,camposOcultos.get(3));						
				// Procedo a borrar en BBDDs
				correcto=admin.delete(hash);
				if (correcto){
					tx.commit();
					result=exitoRefresco("messages.deleted.success",request);				
				    //request.setAttribute("descOperation","messages.deleted.success");					
					//result="refresco";
				}
			}
			else{ // Si se trata de eliminar un precio asociado a un servicio
				PysPreciosServiciosAdm admin=new PysPreciosServiciosAdm(this.getUserBean(request));				
				hash.put(PysPreciosServiciosBean.C_IDINSTITUCION,camposOcultos.get(0));
				hash.put(PysPreciosServiciosBean.C_IDTIPOSERVICIOS,camposOcultos.get(1));
				hash.put(PysPreciosServiciosBean.C_IDSERVICIO,camposOcultos.get(2));
				hash.put(PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION,camposOcultos.get(3));
				hash.put(PysPreciosServiciosBean.C_IDPERIODICIDAD,camposOcultos.get(4));
				hash.put(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS,camposOcultos.get(5));							
				// Procedo a borrar en BBDDs				
				correcto=admin.delete(hash);
				if (correcto){
					tx.commit();
					// Cargo las claves para la "recarga" de la ventana modalen modo edicion
					request.setAttribute("institucion",hash.get(PysServiciosInstitucionBean.C_IDINSTITUCION));
					request.setAttribute("tipoServicio",hash.get(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS));
					request.setAttribute("servicio",hash.get(PysServiciosInstitucionBean.C_IDSERVICIO));
					request.setAttribute("servicioInstitucion",hash.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION));
					result=exitoEspecifico("messages.deleted.success",request);				
				    //request.setAttribute("descOperation","messages.deleted.success");					
					//result="refresco";
				}
			}	
		}
//		catch(ClsExceptions ex){
//			try {
//				tx.rollback();
//			} 
//			catch (Exception e) {}
//			ex.prepare(request);
//			result=exito("messages.deleted.error",request);
//		}	
		catch (Exception e) { 
			if (camposOcultos.size()==4){
				throwExcp("messages.pys.mantenimientoProductos.errorBorrado",new String[] {"modulo.productos"},e,tx);
			}
			else{
				throwExcp("messages.pys.mantenimientoServicios.errorBorradoPrecio",new String[] {"modulo.productos"},e,tx);
			}
		}				
        
		return (result);
	}
					
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub

		String result="listar";
		Vector v=new Vector();		
		
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			MantenimientoServiciosForm f = (MantenimientoServiciosForm) formulario;
			PysServiciosInstitucionAdm admin=new PysServiciosInstitucionAdm(this.getUserBean(request));
			v = admin.obtenerServiciosInstitucion(f.getBusquedaTipo(),
												  f.getBusquedaCategoria(),
												  f.getBusquedaServicio(),
												  usr.getLocation(),
												  f.getBusquedaPago(),
												  f.getBusquedaEstado());
			request.setAttribute("container", v);
		}	
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}		

		return (result);		
	}
	
	/** 
	 *  Funcion que implementa la accion nuevo criterio
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevoCriterio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		PysFormaPagoBean bean=new PysFormaPagoBean ();
		String result="nuevoCriterio";
		try{
						
			// Obtengo el formulario
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			
			Object remitente=(Object)"insertar";
			request.setAttribute("modelo",remitente);
			request.setAttribute("institucion",miForm.getIdInstitucion());
			request.setAttribute("tipoServicio",miForm.getIdTipoServicios());
			request.setAttribute("servicio",miForm.getIdServicio());
			// Consigo el maximo servicio institucion si se trata de una insercion -> 0
			if (miForm.getIdServiciosInstitucion().equalsIgnoreCase("0")){ 
				PysServiciosInstitucionAdm adm = new PysServiciosInstitucionAdm(this.getUserBean(request));
				request.setAttribute("servicioInstitucion",adm.getMaxIdServiciosInstitucion(miForm.getIdInstitucion(),miForm.getIdTipoServicios(),miForm.getIdServicio()));
			}else{
				request.setAttribute("servicioInstitucion",miForm.getIdServiciosInstitucion());
			}
									
		}	
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}		

		return result;
	}	
	
	
	protected String condicionSuscripcionAutomatica(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result = "condicionSuscripcionAutomatica";
		try{
						
			// Obtengo el formulario
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			
			request.setAttribute("modelo", miForm.getModo());
			request.setAttribute("checkAutomatico", miForm.getComprobarCondicion());
			request.setAttribute("idInstitucion",miForm.getIdInstitucion());
			request.setAttribute("idTipoServicio",miForm.getIdTipoServicios());
			request.setAttribute("idServicio",miForm.getIdServicio());

			// Consigo el maximo servicio institucion si se trata de una insercion -> 0
			String idServicioInstitucion = "";
			if (miForm.getIdServiciosInstitucion().equalsIgnoreCase("0")){ 
				PysServiciosInstitucionAdm adm = new PysServiciosInstitucionAdm(this.getUserBean(request));
				idServicioInstitucion = adm.getMaxIdServiciosInstitucion(miForm.getIdInstitucion(),miForm.getIdTipoServicios(),miForm.getIdServicio());
			}
			else{
				idServicioInstitucion = miForm.getIdServiciosInstitucion();
			}
			request.setAttribute("idServicioInstitucion", idServicioInstitucion);
			
			
			//*************************** mostrar datos de condicion  **********************************
			{ 	
				PysServiciosInstitucionAdm admin = new PysServiciosInstitucionAdm (this.getUserBean(request));
				Hashtable claves = new Hashtable();
				claves.put(PysServiciosInstitucionBean.C_IDINSTITUCION,(String)miForm.getIdInstitucion());
				claves.put(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS,(String)miForm.getIdTipoServicios());
				claves.put(PysServiciosInstitucionBean.C_IDSERVICIO,(String)miForm.getIdServicio());
				claves.put(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION, idServicioInstitucion);
				PysServiciosInstitucionBean beanServInsti = (PysServiciosInstitucionBean) (admin.select(claves)).get(0);	
				
				//Para que la tabla de los criterio que afectan a ese precio se muestre bien,
				//hay que pasarle el resultado de la consulta de criterios en el formato correcto
				//el formato es
				
				//	*conector_campo_operador_valor_idCampo__idOperador_idValor__abrirParentesis_cerrarParentesis_
				 
				//	donde los siguientes campos tambien tienen un formato concreto:
				 
				//	idCampo: 		idCampo,tipoCampo,idTabla
				//	idOperador: 	idOperador,simbolo
				
				//comenzamos recuperando el idConsulta del Bean seleccionado y
				//despues consultado en la tabla los criterios, con ese idCOnsulta
				ConCriterioConsultaAdm criteriosAdm = new ConCriterioConsultaAdm(this.getUserBean(request));
	
				//puede que no tenga criterios o la consulta luego metemos la consulta dentro de un try,
				//en cuyo caso no debe devolver ningun String, pero tampoco es un fallo.
				String criteriosString = "";
				if (beanServInsti.getIdConsulta()!=null) {					
					try {
						String consultaCriterios = 
							" select ca."+ConCampoConsultaBean.C_NOMBREENCONSULTA + " CAMPO, ca."
									+ ConCampoConsultaBean.C_IDCAMPO + " IDCAMPO, ta."
									+ ConTablaConsultaBean.C_IDTABLA + " IDTABLA, ta."
									+ ConTablaConsultaBean.C_DESCRIPCION + " TABLA, ca."
									+ ConCampoConsultaBean.C_TIPOCAMPO + " TIPOCAMPO, op."
									+ ConOperacionConsultaBean.C_IDOPERACION + " OPERADOR, op."
									+ ConOperacionConsultaBean.C_SIMBOLO + " SIMBOLO, ca."
									+ ConCampoConsultaBean.C_SELECTAYUDA + " SELECTAYUDA, cr."
									+ ConCriterioConsultaBean.C_VALOR + " VALOR, cr."
									+ ConCriterioConsultaBean.C_ABRIRPAR+" ABRIRPAR, cr."
									+ ConCriterioConsultaBean.C_CERRARPAR+" CERRARPAR, cr."
									+ ConCriterioConsultaBean.C_OPERADOR + " CONECTOR, " 
									+ UtilidadesMultidioma.getCampoMultidiomaSimple("op." + ConOperacionConsultaBean.C_DESCRIPCION, this.getLenguaje(request)) + " DESCRIPCION" +
							" from "+ ConCriterioConsultaBean.T_NOMBRETABLA + " cr, "
									+ ConTablaConsultaBean.T_NOMBRETABLA + " ta, "
									+ ConCampoConsultaBean.T_NOMBRETABLA + " ca, "
									+ ConOperacionConsultaBean.T_NOMBRETABLA + " op" +
							" where ca." + ConCampoConsultaBean.C_IDCAMPO + " = cr." + ConCriterioConsultaBean.C_IDCAMPO +
								" and op." + ConOperacionConsultaBean.C_IDOPERACION + " = cr." + ConCriterioConsultaBean.C_IDOPERACION +
								" and ta." + ConTablaConsultaBean.C_IDTABLA + " = ca." + ConCampoConsultaBean.C_IDTABLA +
								" and cr." + ConCriterioConsultaBean.C_IDINSTITUCION + " = "+ (String)miForm.getIdInstitucion() +
								" and cr." + ConCriterioConsultaBean.C_IDCONSULTA + " = " + ((Long)beanServInsti.getIdConsulta()).toString() + " "+
							" order by cr."+ConCriterioConsultaBean.C_ORDEN+" asc";
						
						Vector vCriterios = (Vector)criteriosAdm.selectGenerico(consultaCriterios);
						for (int cont = 0; cont < vCriterios.size(); cont++){
							Hashtable criterio = (Hashtable)vCriterios.get(cont);
		
							// Ahora tenemos todos los campos a falta del idValor
							// Ejecutamos la selectAyuda del campo, y recuperamos el ID 
							// para el valor con descripcion = al valor que ya tenemos
							ConCampoConsultaAdm camposAdm = new ConCampoConsultaAdm(this.getUserBean(request));
						
							// Si tenemos la select buscamos el idvalor: 
							if (criterio.get("SELECTAYUDA") != null && !((String)criterio.get("SELECTAYUDA")).equals("")) {
								String con = (String)criterio.get("SELECTAYUDA");
								con = con.replaceAll("@IDINSTITUCION@" , this.getUserBean(request).getLocation());
								con = con.replaceAll("@IDIOMA@" , this.getUserBean(request).getLanguage());
								Vector campos = (Vector)camposAdm.selectGenerico(con);
								for (int contador=0; contador<campos.size(); contador++){
									Hashtable campoHash = (Hashtable)campos.get(contador);
									if (((String)campoHash.get("DESCRIPCION")).equalsIgnoreCase((String)criterio.get("VALOR"))){
										contador = campos.size()+1;
										String aux = (String)campoHash.get("ID");
										
										int p=-1;
										if ((p = aux.indexOf("#"))!=-1) { 
											aux=aux.substring(0,p) + "$" + aux.substring(p+1,aux.length());
										}
										// RGG criterio.put("IDVALOR", (String)campoHash.get("ID"));
										criterio.put("IDVALOR", aux);
									}
								}//Fin del for interior
							}
							
							//para cada uno de los criterios el formato indicado:
							//	*conector_campo_operador_valor_idCampo__idOperador_idValor_
							//	donde los siguientes campos tambien tienen un formato concreto:
							//	idCampo: 		idCampo,tipoCampo,idTabla
							//	idOperador: 	idOperador,simbolo
							String abrirPar = (String)criterio.get("ABRIRPAR");
							String cerrarPar = (String)criterio.get("CERRARPAR");
							if (abrirPar!=null && abrirPar.equals("1")) abrirPar="1"; else abrirPar="0";
							if (cerrarPar!=null && cerrarPar.equals("1")) cerrarPar="1"; else cerrarPar="0";
	
							String conector = (criterio.containsKey("CONECTOR")?(String)criterio.get("CONECTOR"):" ");
							criteriosString += "*" + conector + "_" +
													(String)criterio.get("CAMPO")+"_"+
													(String)criterio.get("DESCRIPCION")+"_"+
													(((String)criterio.get("VALOR")).equalsIgnoreCase("NULL")?"":(String)criterio.get("VALOR"))+"_"+
													(String)criterio.get("IDCAMPO")+","+
													(String)criterio.get("TIPOCAMPO")+","+
													(String)criterio.get("IDTABLA")+"_"+
													(String)criterio.get("OPERADOR")+","+
													(String)criterio.get("SIMBOLO")+"_"+
													(String)criterio.get("IDVALOR")+"_"+
													abrirPar+"_"+
													cerrarPar+"_";
						} // Fin del for principal
					} 
					catch(Exception e) {
						e.printStackTrace();
					}
				}
	
				//seleccionamos la consulta para futuras modificaciones
				ConConsultaAdm consultaAdm = new ConConsultaAdm(this.getUserBean(request));
				try {
					String consultaConsulta = " where " + ConConsultaBean.C_IDINSTITUCION + "=" + miForm.getIdInstitucion()+
												" and " + ConConsultaBean.C_IDCONSULTA +"= "+ ((Long)beanServInsti.getIdConsulta()).toString()+" ";
		            ConConsultaBean consultaBean = (ConConsultaBean)((Vector)consultaAdm.select(consultaConsulta)).get(0);
		            Hashtable consultaHash = (Hashtable)consultaBean.getOriginalHash().clone();
		            request.getSession().setAttribute("DATABACKUPCONSULTA", consultaHash);
				}
				catch (Exception e)
				{
					//si no hay consulta no debe fallar, simplemente no se mostrarán criterios
				}
				
				// Pasamos el String para presentar los criterios
				request.setAttribute("resultado",criteriosString);
			}
		}	
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		}		
		return result;
	}	
	
	/**
	 * La funcion transforma el String construido en la jsp (criterios) y a partir de él 
	 * crea:
	 * 
	 * 0.- un Vector de Hashtables con los criterios 
	 * 1.- un VEctor de ConTablaConsultaBean, con las tablas accedidas con los campos de los criterios 
	 * 2.- un Vector de Hashtables con los identificadores de los campos de los criterios(para insertar despues en Con_CriterioConsulta
	 * 
	 * @param String criterios, con los criterios que devuleve la tabla de la jsp
	 * 			con formato: "*conector1_campo1_operador1_valor1_idCampo1_idOperador1_idValor1_"
	 * @return
	 */
	protected Vector getVectorCriteriosTablas (String criterios, HttpServletRequest request)throws SIGAException{
		
		Vector resultadoCriterios = new Vector();
		Vector resultadoIdCriterios = new Vector();
		Vector resultadoTablasBeans = new Vector();
		Vector resultadoTablas = new Vector();
		Vector resultado = new Vector();
		
		String conector="", campo="", operador="", valor="", idCampo="", idOperador="", idValor="", simbolo="", nombreReal="", formato="";
		String idTabla ="", consultaTablas=" where ", tipoCampo="", abrirPar="", cerrarPar="";

		try {
			boolean seguir= (criterios.indexOf("*")>=0);
			int cont = 0;
			while (seguir) {
				//	recogemos del string cada uno de los criterios 
				//	cada criterio tiene el formato:
				//
				
				//	*conector_campo_operador_valor_idCampo_idOperador_idValor_abrirPar_cerrarPar
				 
				//	donde los siguientes campos tambien tienen un formato concreto:
				 
				//	idCampo: 		idCampo,tipoCampo,idTabla
				//	idOperador: 	idOperador,simbolo
				 
				Hashtable hash = new Hashtable();
				Hashtable hashId = new Hashtable();
				criterios = criterios.substring(1,criterios.length());
				conector = criterios.substring(0,criterios.indexOf("_"));
				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				campo = criterios.substring(0,criterios.indexOf("_"));
				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				operador = criterios.substring(0,criterios.indexOf("_"));
				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				valor = criterios.substring(0,criterios.indexOf("_"));
				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				idCampo = criterios.substring(0,criterios.indexOf("_"));
				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				idOperador = criterios.substring(0,criterios.indexOf("_"));
				simbolo = idOperador.substring(idOperador.indexOf(",")+1, idOperador.length());
				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				idValor = criterios.substring(0,criterios.indexOf("_"));
				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				abrirPar = criterios.substring(0,criterios.indexOf("_"));
				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				cerrarPar = criterios.substring(0,criterios.indexOf("_"));
				 
				
// Antes				simbolo = idOperador.substring(idOperador.indexOf(",")+1, idOperador.length());
//				criterios = criterios.substring(criterios.indexOf("_")+1,criterios.length());
				
				//Caso de numericos que no se introduzca el valor en combo:
				//if (criterios.substring(0,criterios.indexOf("_")).equalsIgnoreCase("NULL"))
				//	idValor = valor;
				//else
				//	idValor = criterios.substring(0,criterios.indexOf("_"));
				if ((idValor).equalsIgnoreCase("NULL"))
					idValor = valor;
					
				tipoCampo = idCampo.substring(idCampo.indexOf(",")+1, idCampo.lastIndexOf(","));
				seguir = (criterios.indexOf("*")>0);
				//tratamos el campo idCampo porque aquí tenemos tambien la informacion de las tablas seleccionadas
				idTabla = idCampo.substring(idCampo.lastIndexOf(",")+1, idCampo.length());
				consultaTablas += (cont>0?" OR ":" ") + ConTablaConsultaBean.C_IDTABLA + "=" + idTabla + " ";
				//si hay mas criterios se prepara el string para la siguiente pasada
				//quitamos el ultimo valor del primer criterio
				if (seguir){
					criterios = criterios.substring(criterios.indexOf("*"),criterios.length());
				}
				//consultamos el nombre real del campo
				String consultaNombreReal = " where " + ConCampoConsultaBean.C_IDCAMPO + "="+ idCampo.substring(0,idCampo.indexOf(",")) + " ";
				ConCampoConsultaAdm campoAdm = new ConCampoConsultaAdm(this.getUserBean(request));
				ConCampoConsultaBean campoBean = (ConCampoConsultaBean)((Vector)campoAdm.select(consultaNombreReal)).get(0);
				nombreReal = (String)campoBean.getNombreReal();
				formato = (String)campoBean.getFormato();

				//Chequeo:
			
				    //si el valor es de tipo fecha
					if (tipoCampo.equalsIgnoreCase(SIGAConstants.TYPE_DATE)){
						 //si el valor es de tipo fecha y el simbolo es null es necesario para añadir si is null o is not null para la sentencia.
						if(simbolo.equals("is null")){							
								if((valor.equals("SI"))||(valor.equals("1"))){
									idValor="1";
									valor="SI";
								}
								else if ((valor.equals("NO"))||(valor.equals("0"))){
									simbolo = "is not null";
									valor="NO";
									idValor="0"; 
								}
								}else if (!valor.equals(""))
										idValor=GstDate.getApplicationFormatDate(this.getLenguaje(request),idValor);	
									  else
										  //este error se muestra si el campo valor es vacio este no puede ser vacio.
										  throwExcp("messages.general.error.tipoDatos", new String[] {campo}, new Exception(),null);
						
						//idValor=GstDate.getApplicationFormatDate(this.getLenguaje(request),idValor);
					}else if (tipoCampo.equalsIgnoreCase(SIGAConstants.TYPE_ALPHANUMERIC)){
						idValor = "'" + idValor + "'";
					} else {
						if (tipoCampo.equalsIgnoreCase(SIGAConstants.TYPE_NUMERIC))					
							try {
	// RGG												int prueba;
								String aux = "";
								double prueba;
								if (idValor!=null && !idValor.equalsIgnoreCase("NULL")) {
	// RGG								prueba = Integer.parseInt(idValor);
									if (idValor.indexOf("$")!=-1) {
										aux = idValor.substring(0,idValor.indexOf("$"));
									} else aux = idValor;
									prueba = Double.parseDouble(aux);
								} else {
	// RGG 								prueba = Integer.parseInt(valor);
									if (valor.indexOf("$")!=-1) {
										aux = valor.substring(0,valor.indexOf("$"));
									} else aux = valor;
									prueba = Double.parseDouble(aux);
								}
							}catch(Exception e)	{
								throwExcp("messages.general.error.tipoDatos", new String[] {campo}, new Exception(),null);
							}
					}
					
				
					if (valor.equals(""))valor="NULL";
					if (simbolo.equalsIgnoreCase("IS "))valor="NULL";
					else {if (valor.equalsIgnoreCase("NULL")) 
							throwExcp("messages.general.error.tipoDatos", new String[] {campo}, new Exception(),null);
					}
					
					if ((!idValor.equals("1"))&&(!idValor.equals("0"))){						
						if (idValor.indexOf("$")==-1) {
							//dando formato si el campo idvalor tiene un valor distinto de 1 o 0 que son simbolos de null de la sentencia.
						idValor = UtilidadesProductosServicios.reemplazaString("%%FORMATO%%", idValor, formato);
						}
					}
					if((idValor.equals("0"))&&(tipoCampo.equalsIgnoreCase(SIGAConstants.TYPE_DATE))){
						idValor="";
					}
				//}
				//construimos la hash con el criterios
				hash.put("OPERADOR", conector);
				hash.put("NOMBREREAL", nombreReal);
				hash.put("OPERACION", simbolo);
				hash.put("VALOR", idValor);
				
				hashId.put(ConCriterioConsultaBean.C_IDCAMPO, idCampo.substring(0,idCampo.indexOf(",")));
				hashId.put(ConCriterioConsultaBean.C_IDOPERACION, idOperador.substring(0, idOperador.indexOf(",")));
				hashId.put(ConCriterioConsultaBean.C_VALOR, valor);
				hashId.put(ConCriterioConsultaBean.C_OPERADOR, conector);
				if (abrirPar!=null && abrirPar.equals("1")) { 
				    hashId.put(ConCriterioConsultaBean.C_ABRIRPAR, abrirPar);
				    hash.put(ConCriterioConsultaBean.C_ABRIRPAR, abrirPar);
				}
				if (cerrarPar!=null && cerrarPar.equals("1")) { 
				    hashId.put(ConCriterioConsultaBean.C_CERRARPAR, cerrarPar);
				    hash.put(ConCriterioConsultaBean.C_CERRARPAR, cerrarPar);
				}

				//anhadimos el criterios al vector
				resultadoCriterios.add(cont, hash.clone());
				resultadoIdCriterios.add(cont, hashId.clone());
				cont++;
			}
		}catch(SIGAException e){
			throw(e);
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null);
		}
		//ahora consultamos todas las tablas accedidas
		try{
			//en consultaTablas tenemos la consulta de todos los idTAblas a las que se va a acceder
			ConTablaConsultaAdm tablasAdm = new ConTablaConsultaAdm(this.getUserBean(request));
			resultadoTablasBeans = (Vector)tablasAdm.select(consultaTablas);
			for (int pos=0; pos<resultadoTablasBeans.size();pos++){
				ConTablaConsultaBean tabla = (ConTablaConsultaBean)resultadoTablasBeans.get(pos);
				resultadoTablas.add(pos, (String)tabla.getDescripcion());
			}
			//en caso de que se hayan elegido campos de la tabla cen_persona
			//se debe hacer un Join con la Tabla Cen_Cliente
			if (((resultadoTablas.contains(CenPersonaBean.T_NOMBRETABLA))&&((!resultadoTablas.contains(CenClienteBean.T_NOMBRETABLA))
																			&&(!resultadoTablas.contains(CenColegiadoBean.T_NOMBRETABLA)))))
			   
				
				
				resultadoTablas.add(CenClienteBean.T_NOMBRETABLA);
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null);
		}
		
		//juntamos los dos vectores en el vector resultado
		resultado.add(0, resultadoCriterios);
		resultado.add(1, resultadoTablas);
		resultado.add(2, resultadoIdCriterios);
		return resultado;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String insertarCriterioConPrecioPorDefecto(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    try {
		    // Como siempre existe un precio por defecto obtenemos sus datos
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			PysPreciosServiciosAdm pServiciosAdm = new PysPreciosServiciosAdm (this.getUserBean(request));
			Vector v = pServiciosAdm.getPreciosPorDefecto(miForm.getIdInstitucion(), miForm.getIdTipoServicios(), miForm.getIdServicio(), miForm.getIdServiciosInstitucion());
			if (v == null || v.size() != 1) {
			    throw new SIGAException ("messages.general.error");
			}
			Hashtable b = (Hashtable)v.get(0);
			
			String mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request), "pys.mantenimientoServicios.literal.precio") + ": " + 
							 UtilidadesHash.getString(b, PysPreciosServiciosBean.C_VALOR) + ClsConstants.CODIGO_EURO + " - " + 
							 UtilidadesString.getMensajeIdioma(this.getUserBean(request), "pys.mantenimientoServicios.literal.periodicidad") + ": " + 
							 UtilidadesHash.getString(b, "PERIODICIDAD");
			
			request.setAttribute("mensajeComprobacionPorDefecto", mensaje);
			return "mensajeInsertarPrecioPorDefecto";
	    }
	    catch (Exception e) {
	        return error(e.getMessage(), new ClsExceptions (e.getMessage()), request);
        }
	}
	
	/** 
	 *  Funcion que implementa la accion insertar criterio
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertarCriterio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String result="";
		String servicio;	
		String tipoServicio;
		String institucion;	
		String servicioInstitucion;		
		UserTransaction tx = null;
		boolean correcto=true;

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			PysPreciosServiciosAdm admin=new PysPreciosServiciosAdm(this.getUserBean(request));
			ConConsultaAdm consultaAdm = new ConConsultaAdm(this.getUserBean(request));
			ConCriterioConsultaAdm criteriosAdm = new ConCriterioConsultaAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			// Obtengo los datos del formulario
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			// Recupero el contenido de la tabla de criterios
			String criterios = (String)miForm.getCriterios();
			Vector resultado = new Vector();
			Vector vCriterios = new Vector();
			Vector vIdCriterios = new Vector();
			Vector vTablas = new Vector();
			if ((criterios != null)&&(!criterios.equals(""))){
				resultado = (Vector)this.getVectorCriteriosTablas(criterios, request);
				vCriterios = (Vector)resultado.get(0);
				vTablas = (Vector)resultado.get(1);
				vIdCriterios = (Vector)resultado.get(2);
			}
			
			
			// Cargo la tabla hash con los valores del formulario para insertar
			Hashtable hash = miForm.getDatos();
			// Anhado valores que faltan
			hash.put(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS,admin.getIdPreciosServicios(miForm.getIdInstitucion(),miForm.getIdTipoServicios(),miForm.getIdServicio(),miForm.getIdServiciosInstitucion(),miForm.getPeriodicidad()));
			
			// RGG 05-10-2005 Esto se cambia por lo de abajo
			//hash.put(PysPreciosServiciosBean.C_PORDEFECTO,ClsConstants.DB_FALSE);
			//si no es la query por defecto se debe insertar el idConsulta

			String precioDefecto = (String) request.getParameter("precioDefecto"); 
			if (precioDefecto!=null) {
				hash.put(PysPreciosServiciosBean.C_PORDEFECTO,ClsConstants.DB_TRUE);
				
				// Solo permitimos un precio por defecto, asi que borramos los que hubiese
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, PysPreciosServiciosBean.C_IDINSTITUCION, miForm.getIdInstitucion());
				UtilidadesHash.set(h, PysPreciosServiciosBean.C_IDTIPOSERVICIOS, miForm.getIdTipoServicios());
				UtilidadesHash.set(h, PysPreciosServiciosBean.C_IDSERVICIO, miForm.getIdServicio());
				UtilidadesHash.set(h, PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION, miForm.getIdServiciosInstitucion());
				UtilidadesHash.set(h, PysPreciosServiciosBean.C_PORDEFECTO, "" + ClsConstants.DB_TRUE);
				
				String campos[] = {PysPreciosServiciosBean.C_IDINSTITUCION, 
				        		   PysPreciosServiciosBean.C_IDTIPOSERVICIOS, 
				        		   PysPreciosServiciosBean.C_IDSERVICIO, 
				        		   PysPreciosServiciosBean.C_IDSERVICIOSINSTITUCION, 
				        		   PysPreciosServiciosBean.C_PORDEFECTO};

				if (!admin.deleteDirect(h, campos)) {
				    throw new SIGAException (admin.getError());
				}
			} 
			else {
				hash.put(PysPreciosServiciosBean.C_PORDEFECTO,ClsConstants.DB_FALSE);
			}


			//construimos la query
			String query = "";
			boolean queryPorDefecto = false;
			//si no hay criterios, cogemos la select por defecto
			Hashtable nuevaConsulta = new Hashtable();
			String nuevoIdConsulta = "";
			nuevoIdConsulta = ((Integer)consultaAdm.getNewIdConsulta(usr.getLocation())).toString();
			
			if ((criterios != null)&&(!criterios.equals(""))){
				query = UtilidadesProductosServicios.getQuery( vTablas, vCriterios);
				if (query.indexOf("@IDGRUPO@")!=-1){
					query=UtilidadesString.replaceAllIgnoreCase(query,"@IDGRUPO@",""+null+",2000");
				}
				
				//cambiamos @IDINSTITUCION@	 por el IdInstitucion correcto
				query = UtilidadesProductosServicios.reemplazaString("@IDINSTITUCION@",(String)usr.getLocation(), query);
				//aplicamos el idConsulta si existia y sino calculamos uno nuevo
				nuevaConsulta.put(ConConsultaBean.C_IDINSTITUCION, (String)usr.getLocation());
				nuevaConsulta.put(ConConsultaBean.C_IDCONSULTA, nuevoIdConsulta);
				nuevaConsulta.put(ConConsultaBean.C_GENERAL, ClsConstants.DB_TRUE);
				nuevaConsulta.put(ConConsultaBean.C_IDMODULO, ID_MODULO_PYS);
				nuevaConsulta.put(ConConsultaBean.C_DESCRIPCION, "Módulo Productos y Servicios");
				nuevaConsulta.put(ConConsultaBean.C_SENTENCIA, query);
				nuevaConsulta.put(ConConsultaBean.C_TIPOCONSULTA, ConConsultaAdm.TIPO_CONSULTA_PYS);
			}else{
				query = UtilidadesProductosServicios.getQueryPorDefecto();
				//cambiamos @IDINSTITUCION@	 por el IdInstitucion correcto
				query = UtilidadesProductosServicios.reemplazaString("@IDINSTITUCION@",(String)usr.getLocation(), query);
				queryPorDefecto = true;
			}
			
			// Anhado valores que faltan
			hash.put(PysPreciosServiciosBean.C_CRITERIOS,query);
			//hash.put(PysPreciosServiciosBean.C_PORDEFECTO,ClsConstants.DB_FALSE);
			
			if(!queryPorDefecto)
				// si no es la query por defecto se debe insertar el idConsulta
				hash.put(PysPreciosServiciosBean.C_IDCONSULTA, nuevoIdConsulta);
			
			else 
				hash.put(PysPreciosServiciosBean.C_IDCONSULTA,"NULL");
			

			// Comienzo la transaccion
			tx.begin();
				
			// Inserto en PYS_PRECIOSSERVICIOS
			correcto=admin.insert(hash);
			// Inserto en CON_CONSULTA, si hace falta
			if (!queryPorDefecto) correcto = consultaAdm.insert(nuevaConsulta);
			// Insertamos en CON_CRITERIOCONSULTA, todos los criterios
			for (int contador=0; contador<vIdCriterios.size();contador++){
				Hashtable criterio = (Hashtable)vIdCriterios.get(contador);
				criterio.put(ConCriterioConsultaBean.C_FECHAMODIFICACION, "sysdate");
				criterio.put(ConCriterioConsultaBean.C_IDCONSULTA, nuevoIdConsulta);
				criterio.put(ConCriterioConsultaBean.C_IDINSTITUCION, (String)usr.getLocation());
				criterio.put(ConCriterioConsultaBean.C_USUMODIFICACION, (String)usr.getUserName());
				criterio.put(ConCriterioConsultaBean.C_ORDEN, ""+(contador+1));
				correcto = criteriosAdm.insert(criterio);
			}
			
			if (correcto){
				tx.commit();
				// Cargo las claves para la "recarga" de la ventana modalen modo edicion
				if (queryPorDefecto) request.setAttribute("porDefecto", "SI");
				request.setAttribute("institucion",hash.get(PysServiciosInstitucionBean.C_IDINSTITUCION));
				request.setAttribute("tipoServicio",hash.get(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS));
				request.setAttribute("servicio",hash.get(PysServiciosInstitucionBean.C_IDSERVICIO));
				request.setAttribute("servicioInstitucion",hash.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION));
				result = exitoModal("messages.updated.success", request);

			}			
		}
		catch (ClsExceptions e){
			throwExcp(e.getMessage(),e,tx);
		}
		catch (SIGAException e){
			throw(e);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		}		
		return result;
	}
	
	
	/** 
	 *  Funcion que implementa la accion modificar criterio
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificarCriterio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="error";
		String servicio;
		String pago;		
		String paramConsulta;		
		String tipoServicio;
		String idServInst;		
		Hashtable hashOriginal = new Hashtable();
		Enumeration filaOriginal; 		
		PysPreciosServiciosBean registroOriginal= new PysPreciosServiciosBean();
		ConConsultaAdm consultaAdm = new ConConsultaAdm(this.getUserBean(request));
		ConCriterioConsultaAdm criteriosAdm = new ConCriterioConsultaAdm(this.getUserBean(request));
		UserTransaction tx=null;

		// Creacion de las hash para insertar las diferentes formas de pago de Internet	y Secretaria
		Hashtable hashAux = new Hashtable();
		Vector vectorAux = new Vector();
		Vector vectorFP = new Vector();
		Vector camposOcultos = new Vector();
		boolean correcto=true;
		
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			PysPreciosServiciosAdm admin=new PysPreciosServiciosAdm(this.getUserBean(request));
			// Obtengo los datos del formulario
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			Hashtable hash = miForm.getDatos();			
			
			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			filaOriginal=((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
			while(filaOriginal.hasMoreElements()){
              	registroOriginal = (PysPreciosServiciosBean) filaOriginal.nextElement(); 
				// Anhado valores que faltan en la hash modificada (idperiodicidad e idpreciosServicios)
				hash.put(PysPreciosServiciosBean.C_IDPERIODICIDAD,registroOriginal.getIdPeriodicidad().toString());
				hash.put(PysPreciosServiciosBean.C_IDPRECIOSSERVICIOS,registroOriginal.getIdPreciosServicios().toString());
				hash.put(PysPreciosServiciosBean.C_CRITERIOS,registroOriginal.getCriterios());
				if (registroOriginal.getIdConsulta()==null){
					hash.put(PysPreciosServiciosBean.C_IDCONSULTA,"");
				}else{
					hash.put(PysPreciosServiciosBean.C_IDCONSULTA,registroOriginal.getIdConsulta().toString());
				}
			}

			// Recupero el contenido de la tabla de criterios
			String criterios = (String)miForm.getCriterios();
			Vector resultado = new Vector();
			Vector vCriterios = new Vector();
			Vector vIdCriterios = new Vector();
			Vector vTablas = new Vector();
			if ((criterios != null)&&(!criterios.equals(""))){
				resultado = (Vector)this.getVectorCriteriosTablas(criterios, request);
				vCriterios = (Vector)resultado.get(0);
				vTablas = (Vector)resultado.get(1);
				vIdCriterios = (Vector)resultado.get(2);
			}

			//construimos la query
			String query = "";
			boolean queryPorDefecto = false;
			//si no hay criterios, cogemos la select por defecto
			Hashtable nuevaConsulta = new Hashtable();
			String nuevoIdConsulta = "";
			boolean esInsercion = false;
			if (((String)hash.get(PysPreciosServiciosBean.C_IDCONSULTA)).equals("")){
				nuevoIdConsulta = ((Integer)consultaAdm.getNewIdConsulta(usr.getLocation())).toString();
				esInsercion = true;
			}
			else nuevoIdConsulta = (String)hash.get(PysPreciosServiciosBean.C_IDCONSULTA);
			if ((criterios != null)&&(!criterios.equals(""))){
				query = UtilidadesProductosServicios.getQuery( vTablas, vCriterios);
				//cambiamos @IDINSTITUCION@	 por el IdInstitucion correcto
				if (query.indexOf("@IDGRUPO@")!=-1){
					query=UtilidadesString.replaceAllIgnoreCase(query,"@IDGRUPO@",""+null+",2000");
				}
				query = UtilidadesProductosServicios.reemplazaString("@IDINSTITUCION@",(String)usr.getLocation(), query);
				//aplicamos el idConsulta si existia y sino calculamos uno nuevo
				nuevaConsulta.put(ConConsultaBean.C_IDINSTITUCION, (String)usr.getLocation());
				nuevaConsulta.put(ConConsultaBean.C_IDCONSULTA, nuevoIdConsulta);
				nuevaConsulta.put(ConConsultaBean.C_GENERAL, ClsConstants.DB_TRUE);
				nuevaConsulta.put(ConConsultaBean.C_IDMODULO, ID_MODULO_PYS);
				nuevaConsulta.put(ConConsultaBean.C_DESCRIPCION, "Módulo Productos y Servicios");
				nuevaConsulta.put(ConConsultaBean.C_SENTENCIA, query);
				nuevaConsulta.put(ConConsultaBean.C_TIPOCONSULTA, ConConsultaAdm.TIPO_CONSULTA_PYS);
			}else{
				query = UtilidadesProductosServicios.getQueryPorDefecto();
				//cambiamos @IDINSTITUCION@	 por el IdInstitucion correcto
				query = UtilidadesProductosServicios.reemplazaString("@IDINSTITUCION@",(String)usr.getLocation(), query);
				queryPorDefecto = true;
			}
			
			// Anhado valores que faltan
			hash.put(PysPreciosServiciosBean.C_CRITERIOS,query);
			//AA> Comentando la linea siguiente se arreglaba la incidencia
			// INC_02761, pero no se si puede dar problemas en el futuro
			//hash.put(PysPreciosServiciosBean.C_PORDEFECTO,ClsConstants.DB_FALSE);
			
			if(!queryPorDefecto)
				// si no es la query por defecto se debe insertar el idConsulta
				hash.put(PysPreciosServiciosBean.C_IDCONSULTA, nuevoIdConsulta);
			
			else 
				hash.put(PysPreciosServiciosBean.C_IDCONSULTA,"NULL");
			

			// Comienzo control de transacciones
			tx = usr.getTransaction(); 			
			// Comienzo la transaccion
			tx.begin();	
										
			// Inserto en CON_CONSULTA, si hace falta
			if (!esInsercion){
				Hashtable viejaConsulta = (Hashtable)request.getSession().getAttribute("DATABACKUPCONSULTA");
				if (!queryPorDefecto) 
					
				
				if (!nuevaConsulta.equals(viejaConsulta)){
					correcto = consultaAdm.update(nuevaConsulta, viejaConsulta);
				}
				else {
					Hashtable aborrar = new Hashtable();
					aborrar.put(ConConsultaBean.C_IDINSTITUCION, (String)usr.getLocation());
					aborrar.put(ConConsultaBean.C_IDCONSULTA, nuevoIdConsulta);
					String camposBorrar[] = new String[] {ConConsultaBean.C_IDINSTITUCION, ConConsultaBean.C_IDCONSULTA};
					consultaAdm.deleteDirect(nuevaConsulta, camposBorrar);
				}
			}else{
				//primero borramos la anterior consulta
				if (!queryPorDefecto) {
					correcto = consultaAdm.insert(nuevaConsulta);
				}
				else {
					consultaAdm.delete(nuevaConsulta);
				}
			}
			// Borramos de CON_CRITERIOCONSULTA, todos los criterios anteriores
			Hashtable hashBorrado = new Hashtable();
			hashBorrado.put(ConCriterioConsultaBean.C_IDINSTITUCION, (String)usr.getLocation());
			hashBorrado.put(ConCriterioConsultaBean.C_IDCONSULTA, nuevoIdConsulta);
			String camposActualizar[] = new String[] {ConCriterioConsultaBean.C_IDINSTITUCION, ConCriterioConsultaBean.C_IDCONSULTA}; 
			correcto = criteriosAdm.deleteDirect(hashBorrado, camposActualizar);
			// Insertamos en CON_CRITERIOCONSULTA, todos los criterios nuevos
			for (int contador=0; contador<vIdCriterios.size();contador++){
				Hashtable criterio = (Hashtable)vIdCriterios.get(contador);
				criterio.put(ConConsultaBean.C_FECHAMODIFICACION, "sysdate");
				criterio.put(ConConsultaBean.C_IDCONSULTA, nuevoIdConsulta);
				criterio.put(ConConsultaBean.C_IDINSTITUCION, (String)usr.getLocation());
				criterio.put(ConConsultaBean.C_USUMODIFICACION, (String)usr.getUserName());
				criterio.put(ConConsultaBean.C_ORDEN, ""+(contador+1));
				correcto = criteriosAdm.insert(criterio);
			}

			
			// Actualizo 
			//correcto=admin.update(hash,hashOriginal);
			correcto=admin.update(hash,registroOriginal.getOriginalHash());
			

			if (correcto){
				tx.commit();
				// Cargo las claves para la "recarga" de la ventana modalen modo edicion
				if (queryPorDefecto) request.setAttribute("porDefecto", "SI");
				request.setAttribute("institucion",hash.get(PysServiciosInstitucionBean.C_IDINSTITUCION));
				request.setAttribute("tipoServicio",hash.get(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS));
				request.setAttribute("servicio",hash.get(PysServiciosInstitucionBean.C_IDSERVICIO));
				request.setAttribute("servicioInstitucion",hash.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION));
				miForm.reset(mapping,request);
				result = exitoModal("messages.updated.success", request);		
			}			
		}
		catch (ClsExceptions e){
			throwExcp(e.getMessage(),e,tx);
		}
		catch (SIGAException e){
			throw(e);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		}
									
		return (result);	
	}


	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String insertarCondicionSuscripcionAutomatica (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String result="";
		String servicio;	
		String tipoServicio;
		String institucion;	
		String servicioInstitucion;		
		UserTransaction tx = null;

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// Obtengo los datos del formulario
			MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();

			// Recupero el contenido de la tabla de criterios
			String criterios    = (String)miForm.getCriterios();
			Vector resultado    = new Vector();
			Vector vCriterios   = new Vector();
			Vector vIdCriterios = new Vector();
			Vector vTablas      = new Vector();
			if ((criterios != null) && (!criterios.equals(""))){
				resultado    = (Vector)this.getVectorCriteriosTablas(criterios, request);
				vCriterios   = (Vector)resultado.get(0);
				vTablas      = (Vector)resultado.get(1);
				vIdCriterios = (Vector)resultado.get(2);
			}

			// Construimos la query
			String query = "";
			boolean queryPorDefecto = false;
			Hashtable nuevaConsulta = new Hashtable();
			Long nuevoIdConsulta, oldIdConsulta = null;

			ConConsultaAdm consultaAdm = new ConConsultaAdm(this.getUserBean(request));
			nuevoIdConsulta = new Long((consultaAdm.getNewIdConsulta(usr.getLocation())).longValue());
			
			// Si no hay criterios, cogemos la select por defecto
			if ((criterios != null)&&(!criterios.equals(""))){
				query = UtilidadesProductosServicios.getQuery(vTablas, vCriterios);
				//cambiamos @IDINSTITUCION@	 por el IdInstitucion correcto
				if (query.indexOf("@IDGRUPO@")!=-1){
					query=UtilidadesString.replaceAllIgnoreCase(query,"@IDGRUPO@",""+null+",2000");
				}
				query = UtilidadesProductosServicios.reemplazaString("@IDINSTITUCION@",(String)usr.getLocation(), query);
				//aplicamos el idConsulta si existia y sino calculamos uno nuevo
				nuevaConsulta.put(ConConsultaBean.C_IDINSTITUCION, (String)usr.getLocation());
				nuevaConsulta.put(ConConsultaBean.C_IDCONSULTA, nuevoIdConsulta);
				nuevaConsulta.put(ConConsultaBean.C_GENERAL, ClsConstants.DB_TRUE);
				nuevaConsulta.put(ConConsultaBean.C_IDMODULO, ID_MODULO_PYS);
				nuevaConsulta.put(ConConsultaBean.C_DESCRIPCION, "Módulo Productos y Servicios - Suscripción Automática");
				nuevaConsulta.put(ConConsultaBean.C_SENTENCIA, query);
				nuevaConsulta.put(ConConsultaBean.C_TIPOCONSULTA, ConConsultaAdm.TIPO_CONSULTA_PYS);
			}
			// Criteros por defecto
			else {
				query = UtilidadesProductosServicios.getQueryPorDefecto();
				//cambiamos @IDINSTITUCION@	 por el IdInstitucion correcto
				query = UtilidadesProductosServicios.reemplazaString("@IDINSTITUCION@",(String)usr.getLocation(), query);
				queryPorDefecto = true;
			}
			
			// Fijo los nuevos valores
			Row fila = (Row)((Vector)request.getSession().getAttribute("DATABACKUP")).get(0);
			Hashtable datosNuevos 	  = fila.getRow();
			UtilidadesHash.set(datosNuevos, PysServiciosInstitucionBean.C_IDINSTITUCION, new Integer(miForm.getIdInstitucion()));
			UtilidadesHash.set(datosNuevos, PysServiciosInstitucionBean.C_IDTIPOSERVICIOS, new Integer(miForm.getIdTipoServicios()));
			UtilidadesHash.set(datosNuevos, PysServiciosInstitucionBean.C_IDSERVICIO, new Long(miForm.getIdServicio()));
			UtilidadesHash.set(datosNuevos, PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION, new Long(miForm.getIdServiciosInstitucion()));
			UtilidadesHash.set(datosNuevos, PysServiciosInstitucionBean.C_CRITERIOS, query);
			
			if(!queryPorDefecto) 
				UtilidadesHash.set(datosNuevos, PysServiciosInstitucionBean.C_IDCONSULTA, nuevoIdConsulta);
			else {
				oldIdConsulta = UtilidadesHash.getLong(datosNuevos, PysServiciosInstitucionBean.C_IDCONSULTA);
				UtilidadesHash.set(datosNuevos, PysServiciosInstitucionBean.C_IDCONSULTA, "NULL");
			}

			PysServiciosInstitucionAdm admServiciosInstitucion  = new PysServiciosInstitucionAdm(this.getUserBean(request));
			ConCriterioConsultaAdm criteriosAdm = new ConCriterioConsultaAdm(this.getUserBean(request));
			
			// Actualizo los datos de Pys_ServiciosInstitucion 
			tx.begin();
			boolean correcto = true;

			String [] campos = {PysServiciosInstitucionBean.C_USUMODIFICACION,
								PysServiciosInstitucionBean.C_FECHAMODIFICACION,
								PysServiciosInstitucionBean.C_IDCONSULTA,
								PysServiciosInstitucionBean.C_CRITERIOS};
			
			if (admServiciosInstitucion.updateDirect(datosNuevos, null, campos)) {
				
				String hayCondicion = "";
				
				// Inserto en CON_CONSULTA, si hace falta
				if (!queryPorDefecto) {
					hayCondicion = "SI_HAY_CONDICION";
					if (!consultaAdm.insert(nuevaConsulta))
						throw new ClsExceptions("Error al insertar el registro en la tabla " + ConConsultaBean.T_NOMBRETABLA);
//					request.setAttribute("idConsulta", nuevoIdConsulta);
				}
				else {
					Hashtable borrar = new Hashtable();
					UtilidadesHash.set(borrar, ConConsultaBean.C_IDINSTITUCION, new Integer(miForm.getIdInstitucion()));
					UtilidadesHash.set(borrar, ConConsultaBean.C_IDCONSULTA, oldIdConsulta);
					consultaAdm.delete(borrar);
//					request.setAttribute("idConsulta", null);
				}
				
				// Insertamos en CON_CRITERIOCONSULTA, todos los criterios
				for (int contador = 0; contador < vIdCriterios.size(); contador++){
					Hashtable criterio = (Hashtable)vIdCriterios.get(contador);
					criterio.put(ConCriterioConsultaBean.C_FECHAMODIFICACION, "sysdate");
					criterio.put(ConCriterioConsultaBean.C_IDCONSULTA, nuevoIdConsulta);
					criterio.put(ConCriterioConsultaBean.C_IDINSTITUCION, (String)usr.getLocation());
					criterio.put(ConCriterioConsultaBean.C_USUMODIFICACION, (String)usr.getUserName());
					criterio.put(ConCriterioConsultaBean.C_ORDEN, ""+(contador+1));
					if (!criteriosAdm.insert(criterio))
						throw new ClsExceptions("Error al insertar el registro en la tabla " + ConCriterioConsultaBean.T_NOMBRETABLA);
				}
				
				tx.commit();
				// Cargo las claves para la "recarga" de la ventana modalen modo edicion
				if (queryPorDefecto) request.setAttribute("porDefecto", "SI");
				request.setAttribute("idInstitucion",         miForm.getIdInstitucion());
				request.setAttribute("idTipoServicio",        miForm.getIdTipoServicios());
				request.setAttribute("idServicio",            miForm.getIdServicio());
				request.setAttribute("idServicioInstitucion", miForm.getIdServiciosInstitucion());
				result = exitoEspecificoModal("messages.updated.success", hayCondicion, request);
			}
		}
		catch (ClsExceptions e){
			throwExcp(e.getMessage(),e,tx);
		}
		catch (SIGAException e){
			throw(e);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		}		
		return result;
	}

	/** 
	 *  Funcion que prepara el formato fecha
	 * @param  fecha - Fecha en formato original
	 * @return  String - Formato actualizado fecha   
	 */	
	protected String prepararFecha(String fecha){
		
		String sInicial=fecha;
		String sFinal="";		
		
		for (int i=0;i<sInicial.length();i++) {
			if (sInicial.charAt(i) == '-') sFinal += '/';
			else sFinal += sInicial.charAt(i);
		}
		sFinal += " 00:00:00";		
		return sFinal;
	}
	
	/** 
	 *  Funcion que recoge formas de pago comunes.
	 * @param  pagosA - Lista de pagos A
	 * @param  pagosB - Lista de pagos B
	 * @return  ArrayList - Lista de pagos comunes  
	 */	
	protected ArrayList pagosComunes(String[] pagosA, String[] pagosB){
		
		int i,j,k;
		ArrayList comunes=new ArrayList();
		
		for (i=0;i<pagosA.length;i++) {
			for (j=0;j<pagosB.length;j++) {
				if (pagosA[i].compareToIgnoreCase(pagosB[j])==0){
				 comunes.add(pagosA[i]);
				}
			}
		}	
		return comunes;
	}
	
	protected boolean estaPago(String pago, ArrayList listaPagos){
				
		return listaPagos.contains(pago);
	}

	/** 
	 *  Funcion que recupera el userbean
	 *  @param HttpServletRequest
	 *  @return UsrBean
	 */
	protected UsrBean getUserBean(HttpServletRequest request) {
		return (UsrBean)(request.getSession().getAttribute(ClsConstants.USERBEAN));
	}

	/** 
	 *  Funcion que recupera el userName
	 *  @param HttpServletRequest
	 *  @return Integer con el userName
	 */
	protected Integer getUserName(HttpServletRequest request) {
		return new Integer (this.getUserBean(request).getUserName());
	}

	/** 
	 *  Funcion que recupera el perfil
	 *  @param HttpServletRequest
	 *  @return String con el perfil
	 */
	/*protected String getProfileName(HttpServletRequest request) {
		return (String) this.getUserBean(request).getProfile();
	}*/

	/** 
	 *  Funcion que recupera la institucion
	 *  @param HttpServletRequest
	 *  @return Integer con el ID de la institucion
	 */
	protected Integer getIDInstitucion(HttpServletRequest request) {
		return new Integer(this.getUserBean(request).getLocation());
	}

	/** 
	 *  Funcion que recupera el idioma
	 *  @param HttpServletRequest
	 *  @return String con el idioma
	 * 
	 */
	protected String getLenguaje(HttpServletRequest request) {
		return (String) this.getUserBean(request).getLanguage();
	}	
	
	/** 
	 *  Funcion que prepara la salida en caso de exito de la ventana modal <br/>
	 *  y refresca nivel modal anterior
	 *  @param mensaje en formato key de recurso
	 *  @param request para envias los datos
	 *  @return String con el forward
	 */
	protected String exitoEspecifico(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("sinrefresco","");
		request.setAttribute("modal",null);
		return "exitoEspecifico"; 
	}	
	
	
	/** 
	 *  Funcion que prepara la salida en caso de exito de la ventana modal <br/>
	 *  y refresca nivel modal
	 *  @param mensaje en formato key de recurso
	 *  @param request para envias los datos
	 *  @return String con el forward
	 */
	protected String exitoRefrescoModal(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("conrefresco","");
		request.setAttribute("modal",null);
		return "exitoModal"; 
	}	
	
	/** 
	 *  Funcion que prepara la salida en caso de exito de la ventana modal <br/>
	 *  y refresca nivel modal anterior
	 *  @param mensaje en formato key de recurso
	 *  @param request para envias los datos
	 *  @return String con el forward
	 */
	protected String exitoBorrado(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("sinrefresco","");
		request.setAttribute("modal",null);
		return "exitoBorrado"; 
	}	
	
	/** 
	 *  Funcion que prepara la salida en caso de exito de la ventana modal <br/>
	 *  y refresco nivel modal anterior con cierre
	 *  @param mensaje en formato key de recurso
	 *  @param request para envias los datos
	 *  @return String con el forward
	 */
	protected String exitoEspecificoModal(String mensaje, String modal, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("sinrefresco","");
		
		if (modal == null) modal = new String ("");
		request.setAttribute("modal",modal);
		
		return "exitoEspecifico"; 
	}
	
	/**
	 * Funcion que cierra la ventana de criteriosCliente
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String cerrarCriterio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String servicio            = (String)request.getSession().getAttribute("AUX_servicio");
		String tipoServicio        = (String)request.getSession().getAttribute("AUX_tipoServicio");
		String institucion         = (String)request.getSession().getAttribute("AUX_institucion");
		String servicioInstitucion = (String)request.getSession().getAttribute("AUX_servicioInstitucion");

		if (servicio != null && !servicio.equals("")) { 
		    request.setAttribute("idServicio", servicio);
		    request.getSession().removeAttribute("AUX_servicio");
		}
		if (tipoServicio != null && !tipoServicio.equals("")) { 
		    request.setAttribute("idTipoServicio", tipoServicio);
		    request.getSession().removeAttribute("AUX_tipoServicio");
		}
		if (institucion != null && !institucion.equals("")) { 
		    request.setAttribute("idInstitucion", institucion);
		    request.getSession().removeAttribute("AUX_institucion");
		}
		if (servicioInstitucion != null && !servicioInstitucion.equals("")) { 
		    request.setAttribute("idServicioInstitucion", servicioInstitucion);
		    request.getSession().removeAttribute("AUX_servicioInstitucion");
		}
	
		// opara que no falle cuando cerrramos la ventana de criteriors cliente con el aspa de la barra del titulo
		return editar(mapping, formulario, request, response);
//		return exitoEspecificoModal("", "", request);	
	}
	
		
	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String eliminarSuscripcionAutomatica(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="";
		UserTransaction tx=null;

		// Creacion de las hash para insertar las diferentes formas de pago de Internet	y Secretaria
	
		boolean correcto=true;
		
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			/*PysServiciosInstitucionAdm admin=new PysServiciosInstitucionAdm(this.getUserBean(request));
			PysFormaPagoServiciosAdm adminFPS=new PysFormaPagoServiciosAdm(this.getUserBean(request));
			PysFormaPagoAdm adminFP = new PysFormaPagoAdm(this.getUserBean(request));*/
			// Comienzo control de transacciones
			tx = usr.getTransactionPesada(); 			
			// Obtengo los datos del formulario
			/*MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
			servicio=miForm.getIdServicio();
			tipoServicio=miForm.getIdTipoServicios();		
			// Obtengo las diferentes formas de pago "modificadas"
			pagosInternet=miForm.getFormaPagoInternet();
			pagosSecretaria=miForm.getFormaPagoSecretaria();
			pagosCom=pagosComunes(pagosInternet,pagosSecretaria);			
			idServInst=miForm.getIdServInst();		*/	
			// Cargo la tabla hash con los valores del formulario para insertar
			Hashtable hash = formulario.getDatos();		
			MantenimientoServiciosForm form = (MantenimientoServiciosForm) formulario;

		
			
			
			// Comienzo la transaccion
			tx.begin();	
										
			// Actualizo la tabla
			
		
			
			
					/*String [] rc = EjecucionPLs.ejecutarPL_EliminarSuscripcion((String)request.getParameter("idInstitucion"),
																				 (String)request.getParameter("idTipoServicios"),
																				 (String)request.getParameter("idServicio"),
																				 (String)request.getParameter("idServicioInstitucion"),
																				 (String)form.getRadioAlta(),
																				 (String)form.getFechaAlta(),
																				 (String)form.getChkSolicitudBaja(),
																				 ""+this.getUserBean(request));*/
			String [] rc = EjecucionPLs.ejecutarPL_EliminarSuscripcion((String)form.getIdInstitucion(),
					 												   (String)form.getIdTipoServicios(),
																	   (String)form.getIdServicio(),
																	   (String)form.getIdServiciosInstitucion(),
																	   (String)form.getRadioAlta(),
																	   (String)form.getFechaAlta(),
																	    (String)form.getChkSolicitudBaja()
																	   );
			if ((rc == null) || (!rc[0].equals("0"))) {
				throw new ClsExceptions ("Error al ejecutar el PL:PKG_SERVICIOS_AUTOMATICOS.PROCESO_ELIMINAR_SUSCRIPCION. "+rc[1]);
			} else {
				correcto = true;
			}

			if (correcto){
				tx.commit();				
				String mensaje=UtilidadesString.getMensajeIdioma(usr,"pys.mantenimientoServicios.EliminarSuscripcionAutomatica.eliminar");	
				mensaje+=rc[2];
				result=exitoBorrado(mensaje,request);
			}			

		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		}							
		return (result);		
	}
	
	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String configurarEliminacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="configurarEliminacion";
		MantenimientoServiciosForm miForm = (MantenimientoServiciosForm)formulario;
		String idInstitucion=request.getParameter("idInstitucion");
		String idTipoServicios=request.getParameter("idTipoServicios");
		String idServicio=request.getParameter("idServicio");
		String idServiciosInstitucion=request.getParameter("idServiciosInstitucion");
		miForm.setIdInstitucion(idInstitucion);
		miForm.setIdTipoServicios(idTipoServicios);
		miForm.setIdServiciosInstitucion(idServiciosInstitucion);
		miForm.setIdServicio(idServicio);
		request.setAttribute("idInstitucion",idInstitucion);
		request.setAttribute("idServiciosInstitucion",idServiciosInstitucion);
		request.setAttribute("idTipoServicios",idTipoServicios);
		request.setAttribute("idServicio",idServicio);
		miForm.setRadioAlta("0");					
		return (result);		
	}
	
}
