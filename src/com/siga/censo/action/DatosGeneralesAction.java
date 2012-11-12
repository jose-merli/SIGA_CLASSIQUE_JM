package com.siga.censo.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.json.JSONObject;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColaCambioLetradoAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenNoColegiadoActividadAdm;
import com.siga.beans.CenNoColegiadoActividadBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenSolicModifExportarFotoAdm;
import com.siga.beans.CenSolicModifExportarFotoBean;
import com.siga.beans.CenSolicitModifDatosBasicosAdm;
import com.siga.beans.CenTipoDireccionAdm;
import com.siga.beans.CenTipoDireccionBean;
import com.siga.beans.CenTipoSociedadAdm;
import com.siga.beans.CenTipoSociedadBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.VleLetradosSigaAdm;
import com.siga.censo.form.BusquedaCensoForm;
import com.siga.censo.form.DatosGeneralesForm;
import com.siga.censo.form.DireccionesForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;



/**
 * Clase action del caso de uso DATOS GENERALES
 * @author raul.ggonzalez 11-01-2005 Creacion
 * @version 31/01/2006 (david.sanchezp): modificaciones para la navegacion a los 2 jsp de datos generales de colegiados y no colegiados.
 * @version 01/02/2006 (david.sanchezp): nuevos metodos de insercion y modificacion para no colegiados.
 */
public class DatosGeneralesAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			miForm = (MasterForm) formulario;
			
			String accion = miForm.getModo();

	  		// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("abrirSolicitud")){
				mapDestino = abrirSolicitud(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("insertarSolicitud")){
				mapDestino = insertarSolicitud(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("insertarSociedad")){
				mapDestino = insertarSociedad(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("altaNoColegiado")){
				mapDestino = altaNoColegiado(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("validarNoColegiado")){
				mapDestino = validarNoColegiado(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("validarNoColegiadoArt27")){
				mapDestino = validarNoColegiadoArt27(mapping, miForm, request, response);				
			} else if (accion.equalsIgnoreCase("insertarNoColegiado")){
				mapDestino = insertarNoColegiado(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("modificarSociedad")){
				mapDestino = modificarSociedad(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("solicitarModificacion")) {
				mapDestino = solicitarModificacion(mapping, miForm, request, response);
			}else if ( accion.equalsIgnoreCase("getIdenHistorico")){
				ClsLogging.writeFileLog("DATOS NO COLEGIALES:getIdenHistorico", 10);
				mapDestino = getIdenHistorico(mapping, miForm, request, response,0,"");
			}else if ( accion.equalsIgnoreCase("getIdNotario")){
				ClsLogging.writeFileLog("DATOS NO COLEGIALES:getIdNotario", 10);
				mapDestino = getIdNotario(mapping, miForm, request, response,0,"");
			}else if ( accion.equalsIgnoreCase("getAjaxTipo")){
				ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxTipo", 10);
				getAjaxTipo(mapping, miForm, request, response);
			}else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("designarArt27")){
				mapDestino = designarPorArticulo27(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("buscarTodosArt27Modal")){
				borrarPaginador(request, paginadorModal);
				mapDestino = buscarTodosArt27Modal(mapping, miForm, request, response);	
			}else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("getAjaxComboDirecciones")){
				ClsLogging.writeFileLog("BUSQUEDA CENSO:getAjaxComboDirecciones", 10);
				getAjaxComboDirecciones(mapping, miForm, request, response);
				return null;
			}else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("getAjaxDirecciones")){
				ClsLogging.writeFileLog("BUSQUEDA CENSO:getAjaxDirecciones", 10);
				getAjaxDirecciones(mapping, miForm, request, response);
				return null;
			}else {
				return super.executeInternal(mapping,
						      formulario,
						      request, 
						      response);
			}			
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
		return mapping.findForward(mapDestino);
	}
	
	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String forward="inicio", accionPestanha=null, idPersona=null, idInstitucion=null;
		UsrBean user = null;
		UserTransaction tx = null;
		CenClienteAdm cliAdm = new CenClienteAdm(user);
		CenInstitucionAdm insAdm = new CenInstitucionAdm(user); 
		
		try {
			DatosGeneralesForm miform = (DatosGeneralesForm)formulario;
			miform.reset(mapping,request);

			// cargo los valores recibidos por paramtros en el FORM
			accionPestanha = request.getParameter("accion");
			miform.setIdInstitucion(request.getParameter("idInstitucion"));			
			miform.setIdPersona(request.getParameter("idPersona"));			
			miform.setAccion(accionPestanha);
			miform.setModo(accionPestanha);
			//Para saber si debemos cargar en la pestanha el jsp de colegiados/personal o el de no colegiados de Sociedad SJ:
			String tipo = request.getParameter("tipo");
			List<CenTipoSociedadBean> alTipos;
			List<CenTipoSociedadBean> alTiposJY;
			CenTipoSociedadAdm admSociedades = new CenTipoSociedadAdm(this.getUserBean(request));
			//Si tipo es 1 su uso es para saber si se muestra la pantalla de editar colegiado o por contra se usa la pantalla de no colegiodos
			//Si su valor no es 1 y es una letra tipo se usa para cargar el combo de Tipo Sociedades
			if (tipo==null) tipo = "";
			if (!tipo.equals("1") && !tipo.equals("LETRADO") && !tipo.equals("")) 
			{
				request.setAttribute("tipo",tipo);
				miform.setTipo(tipo);
				
				alTiposJY = admSociedades.select("where letracif='J' or letracif='Y'");
				miform.setTiposJY(alTiposJY);
				
				if (tipo.equalsIgnoreCase("J") || tipo.equalsIgnoreCase("Y")) {
					alTipos = admSociedades.select("");
				} else if(tipo.equalsIgnoreCase("A") || tipo.equalsIgnoreCase("B") || tipo.equalsIgnoreCase("F") || tipo.equalsIgnoreCase("G")){
					alTipos = admSociedades.select("");
					//alTipos = admSociedades.select("where letracif='" + tipo + "'");
				}
				else
				{
					tipo = "0";
					//alTipos = admSociedades.select("where letracif='" + tipo + "'");
					alTipos = admSociedades.select("");
				}
				if (alTipos == null) {
					alTipos = new ArrayList<CenTipoSociedadBean>();
				}
				//miform.setTipos(new ArrayList<CenTipoSociedadBean>());
				miform.setTipos(alTipos);
			}else{	
				alTiposJY = admSociedades.select("where letracif='J' or letracif='Y'");
				miform.setTiposJY(alTiposJY);
				alTipos = admSociedades.select("");
				miform.setTipos(alTipos);
			}
			request.setAttribute("modoPestanha",accionPestanha);			
			
			// compruebo que vienen idpersona e idinstitucion
			idPersona = miform.getIdPersona();
			idInstitucion = miform.getIdInstitucion();
			
			// Control de edicion de datos generales de persona
			request.setAttribute("BDATOSGENERALESEDITABLES", "false");
			if (idPersona == null || idPersona.equalsIgnoreCase("")) {
				request.setAttribute("BDATOSGENERALESEDITABLES", "true");
			} else if (accionPestanha!=null && accionPestanha.equalsIgnoreCase("NUEVO")) {
				request.setAttribute("BDATOSGENERALESEDITABLES", "true");
			} else {
				int clienteOtraInstitucion = cliAdm.getTipoClienteOtraInstitucionProduccion(Long.parseLong(idPersona), Integer.parseInt(idInstitucion));
				int clienteEstaInstitucion = cliAdm.getTipoClienteProduccion(Long.parseLong(idPersona), Integer.parseInt(idInstitucion));
				
				if (idPersona.length() <= 4) { // Es Institucion
					if (Integer.parseInt(idInstitucion) == ClsConstants.INSTITUCION_CGAE) {
						request.setAttribute("BDATOSGENERALESEDITABLES", "true");
					} else if (insAdm.getIdInstitucion(idPersona) == idInstitucion) {
						request.setAttribute("BDATOSGENERALESEDITABLES", "true");
					}
				} else if (!tipo.equals("LETRADO") && 
						(clienteEstaInstitucion == CenClienteAdm.TIPOCLIENTE_PROD_NOC || 
						clienteEstaInstitucion == CenClienteAdm.TIPOCLIENTE_PROD_NOCPRO)) { // Es No-colegiado
				
					if (clienteOtraInstitucion < CenClienteAdm.TIPOCLIENTE_PROD_NOCPRO) { // No esta en otros colegios en produccion
						request.setAttribute("BDATOSGENERALESEDITABLES", "true");
					}
				} else { // es colegiado o letrado
					if (clienteEstaInstitucion == CenClienteAdm.TIPOCLIENTE_PROD_RESPRO) {
						request.setAttribute("BDATOSGENERALESEDITABLES", "true");
					} else if (clienteOtraInstitucion < CenClienteAdm.TIPOCLIENTE_PROD_COLPRO) { // No esta colegiado en otros colegios
						request.setAttribute("BDATOSGENERALESEDITABLES", "true");
					} else if (clienteOtraInstitucion == CenClienteAdm.TIPOCLIENTE_PROD_COLPRO && // No esta residente en ningun colegio
							Integer.parseInt(idInstitucion) == ClsConstants.INSTITUCION_CGAE) { // Y estamos en CGAE
						request.setAttribute("BDATOSGENERALESEDITABLES", "true");
					}
				}
			}

			//Esto es para para la variable del check observacion
			if (idPersona != null && !idPersona.equals("")) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.getUserBean(request));
				Vector resultado1 = admHis.getHistorico(miform.getIdPersona(),miform.getIdInstitucion(),"100","","");
				if (resultado1.size()>0) {
					request.setAttribute("descripcion",Boolean.TRUE);
				} else {
					request.setAttribute("descripcion",Boolean.FALSE);
				}
			} else {
				request.setAttribute("descripcion",Boolean.FALSE);
			}
		
						
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = user.getTransaction();

			
			// Control de la navegacion de los datos colegiales
			// Compruebo si tengo que ir a crear un nuevo no colegiado
			// Mando por request los datos necesarios al jsp
			if (accionPestanha!=null && accionPestanha.equalsIgnoreCase("NUEVASOCIEDAD")) {				
				//Fecha inicial de alta vacia:
				miform.setFechaAlta("");
				forward = "sociedadSJ_inicio";
				
				//Calculo un nuevo posible numero de registro:
				String [] resultadoPL = new String[3];
				tx.begin();
				//resultadoPL = EjecucionPLs.ejecutarPLCalcularNumRegistro(user.getLocation());
				tx.commit();
				miform.setSociedadSJ(ClsConstants.DB_FALSE);
				miform.setSociedadSP(ClsConstants.DB_FALSE);
				
				/*if (resultadoPL!=null && resultadoPL[1].equals("0")) {
					miform.setNumeroRegistro(resultadoPL[0]);
					miform.setSociedadSJ(ClsConstants.DB_TRUE);
				}*/
				Hashtable hash = new Hashtable();
        		hash.put("IDINSTITUCION",user.getLocation());
        		hash.put("IDCONTADOR",ClsConstants.SOCIEDADSJ);		
        		/*AdmContadorAdm admContador =new AdmContadorAdm(this.getUserName(request));
				Vector vAdmContador=(Vector)(admContador.selectByPK(hash));
				AdmContadorBean modo = (AdmContadorBean) vAdmContador.get(0);*/
        		
        		Integer idinstitucion =new Integer(user.getLocation());
        		GestorContadores gcSP = new GestorContadores(this.getUserBean(request));
        		
        		//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
        			Hashtable contadorTablaHashSP=gcSP.getContador(idinstitucion,ClsConstants.SOCIEDADSP);
        			miform.setLongitudSP(contadorTablaHashSP.get("LONGITUDCONTADOR").toString());
        			
        		if (contadorTablaHashSP.get("MODO").toString().equals("0")){ 
			 		miform.setPrefijoNumRegSP((String)contadorTablaHashSP.get("PREFIJO"));
					miform.setSufijoNumRegSP((String)contadorTablaHashSP.get("SUFIJO"));
					miform.setContadorNumRegSP(contadorTablaHashSP.get("CONTADOR").toString());
					miform.setModoSociedadSP(""+contadorTablaHashSP.get("MODO"));
					
        		}
        		else{
        			miform.setPrefijoNumRegSP((String)contadorTablaHashSP.get("PREFIJO"));
					miform.setSufijoNumRegSP((String)contadorTablaHashSP.get("SUFIJO"));
					miform.setContadorNumRegSP("");
					miform.setModoSociedadSP(""+contadorTablaHashSP.get("MODO"));
        		}
        		GestorContadores gcSJ = new GestorContadores(this.getUserBean(request));
        		
        		//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
        			Hashtable contadorTablaHashSJ=gcSJ.getContador(idinstitucion,ClsConstants.SOCIEDADSJ);
        			miform.setLongitudSJ(contadorTablaHashSJ.get("LONGITUDCONTADOR").toString());
        			
        		if (contadorTablaHashSJ.get("MODO").toString().equals("0")){ 
			 		miform.setPrefijoNumRegSJ((String)contadorTablaHashSJ.get("PREFIJO"));
					miform.setSufijoNumRegSJ((String)contadorTablaHashSJ.get("SUFIJO"));
					miform.setContadorNumRegSJ(contadorTablaHashSJ.get("CONTADOR").toString());
					miform.setModoSociedadSJ(""+contadorTablaHashSJ.get("MODO"));
					
        		}
        		else{
        			miform.setPrefijoNumRegSJ((String)contadorTablaHashSJ.get("PREFIJO"));
					miform.setSufijoNumRegSJ((String)contadorTablaHashSJ.get("SUFIJO"));
					miform.setContadorNumRegSJ("");
					miform.setModoSociedadSJ(""+contadorTablaHashSJ.get("MODO"));
        		}
			} 
			else {
				if (accionPestanha!=null && accionPestanha.equalsIgnoreCase("NUEVO")) {
					forward = "inicio";
				} 
				else {
					//Si es no colegiado, es de tipo  distinto a persona fisica y no es de tipo NINGUNO vamos al jsp especial para estos no colegiados:
					if (tipo!=null && !tipo.equals("") && !tipo.equals(ClsConstants.COMBO_TIPO_PERSONAL) && !tipo.equalsIgnoreCase("NINGUNO") && !tipo.equalsIgnoreCase("LETRADO")) {						
							this.mandarDatosNoColegiado(idInstitucion, idPersona,miform,request);
							forward = "sociedadSJ_inicio";
					} 
					else { 
						// Vamos al jsp comun para colegiados y no colegiados de tipo persona fisica:
						this.mandarDatosColegiado(idInstitucion, idPersona,miform,request);
						forward = "inicio";
					}
				}
			}
		}catch (SIGAException e) {
			throw e;		
	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
		return forward;
	}

	protected String altaNoColegiado (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String forward="inicio", accionPestanha=null, idPersona=null, idInstitucion=null;
		UsrBean user = null;
		
		try {
			DatosGeneralesForm miform = (DatosGeneralesForm)formulario;			
			miform.reset(mapping,request);
			
			CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm (this.getUserBean(request));
			Vector vTipos = new Vector();
			vTipos=cenTipoDirAdm.select("");
			Hashtable hTipoDir=new Hashtable();
			Hashtable hTipoDirSel=new Hashtable();
			if ( (vTipos != null) && (vTipos.size() > 0) )
				for (int i = 1; i <= vTipos.size(); i++) {
					CenTipoDireccionBean tipoDir = (CenTipoDireccionBean) vTipos.get(i-1);
					hTipoDir.put(tipoDir.getIdTipoDireccion(),"N");
				}
			request.setAttribute("vTipos",vTipos);			

			// cargo los valores recibidos por paramtros en el FORM
			accionPestanha = "nuevo";
			miform.setIdInstitucion(request.getParameter("idInstitucion"));			
			miform.setIdPersona(request.getParameter("idPersona"));			
			miform.setAccion(accionPestanha);
			miform.setModo(accionPestanha);
			//Para saber si debemos cargar en la pestanha el jsp de colegiados/personal o el de no colegiados de Sociedad SJ:
			String tipo = request.getParameter("tipo");
			request.setAttribute("modoPestanha",accionPestanha);
			
			ArrayList list = new ArrayList();
			DireccionesForm dirForm = new DireccionesForm();
			dirForm.setNombre("-- Nueva");
			dirForm.setIdDireccion(new Long(-1));
			list.add(dirForm);			
			miform.setDirecciones(list);			

			user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// compruebo que vienen idpersona e idinstitucion
			idPersona = miform.getIdPersona();
			idInstitucion = miform.getIdInstitucion();			
			
			forward = "altaNoColegiado";

	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		return forward;
	}
	protected String validarNoColegiado (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String forward ="exception";
		try {
			// Obtengo los datos del formulario
			DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
			UsrBean usr = this.getUserBean(request);
			
			CenPersonaAdm adminPer=new CenPersonaAdm(usr);
    		CenPersonaBean cenPersona = adminPer.getPersona(miForm.getNumIdentificacion());
    		if(cenPersona!=null){
    			boolean isNombre = ComodinBusquedas.sustituirVocales(cenPersona.getNombre().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getNombre().toUpperCase().trim())); 
    			boolean isApellido1 =ComodinBusquedas.sustituirVocales(cenPersona.getApellido1().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getApellido1().toUpperCase().trim()));
    			boolean isApellido2 =ComodinBusquedas.sustituirVocales(cenPersona.getApellido2().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getApellido2().toUpperCase().trim()));
    			if (!isNombre || ! isApellido1 ||!isApellido2){
    				miForm.setAccion("messages.fichaCliente.mostrarPersonaExistente");
    				forward = "validarNoColegiado";
   				}else{
   					CenClienteAdm clienteAdm = new CenClienteAdm(usr);
   					CenClienteBean cli = clienteAdm.existeCliente(cenPersona.getIdPersona(),new Integer(usr.getLocation()));
   					if (cli==null) {
   						forward =  insertarNoColegiado(mapping, formulario, request, response);
   					}else{
   						//Comprobamos si el cliente que saca esta colegiado para coger su numero
   						CenColegiadoAdm admColegiado = new CenColegiadoAdm(usr);
   						CenColegiadoBean colegiado = admColegiado.getDatosColegiales(cli.getIdPersona(), cli.getIdInstitucion());
   						if(colegiado!=null){
   							if(colegiado.getNComunitario()!=null && !colegiado.getNComunitario().equals(""))
   								miForm.setNumColegiado(colegiado.getNComunitario());
   							else
   								miForm.setNumColegiado(colegiado.getNColegiado());
   						}
   							
   						
   						miForm.setAccion("messages.fichaCliente.clienteExiste");
   						miForm.setIdInstitucion(cli.getIdInstitucion().toString());

   						forward = "validarNoColegiado";
   					}	
   					
   				}
    			
    			miForm.setIdPersona(cenPersona.getIdPersona().toString());
    			miForm.setNombre(cenPersona.getNombre());
				miForm.setApellido1(cenPersona.getApellido1());
				miForm.setApellido2(cenPersona.getApellido2());
    			
				
    			
    			
    				
    		}else{
    			forward =  insertarNoColegiado(mapping, formulario, request, response);
    			
    		}
			
			

	   } catch (Exception e) {
		   throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		return forward;
	}
	
	protected String validarNoColegiadoArt27 (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String forward ="exception";
		try {
			// Obtengo los datos del formulario
			DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
			UsrBean usr = this.getUserBean(request);
			
			CenPersonaAdm adminPer=new CenPersonaAdm(usr);
    		CenPersonaBean cenPersona = adminPer.getPersona(miForm.getNumIdentificacion());
    		if(cenPersona!=null){
    			boolean isNombre = ComodinBusquedas.sustituirVocales(cenPersona.getNombre().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getNombre().toUpperCase().trim())); 
    			boolean isApellido1 =ComodinBusquedas.sustituirVocales(cenPersona.getApellido1().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getApellido1().toUpperCase().trim()));
    			boolean isApellido2 =ComodinBusquedas.sustituirVocales(cenPersona.getApellido2().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getApellido2().toUpperCase().trim()));
    			if (!isNombre || ! isApellido1 ||!isApellido2){
    				miForm.setAccion("messages.fichaCliente.mostrarPersonaExistente");
    				forward = "validarNoColegiadoArt27";
   				}else{
   					CenClienteAdm clienteAdm = new CenClienteAdm(usr);
   					CenClienteBean cli = clienteAdm.existeCliente(cenPersona.getIdPersona(),new Integer(usr.getLocation()));
   					if (cli==null) {
   						forward =  insertarNoColegiado(mapping, formulario, request, response);
   					}else{
   						//Comprobamos si el cliente que saca esta colegiado para coger su numero
   						CenColegiadoAdm admColegiado = new CenColegiadoAdm(usr);
   						CenColegiadoBean colegiado = admColegiado.getDatosColegiales(cli.getIdPersona(), cli.getIdInstitucion());
   						if(colegiado!=null){
   							if(colegiado.getNComunitario()!=null && !colegiado.getNComunitario().equals(""))
   								miForm.setNumColegiado(colegiado.getNComunitario());
   							else
   								miForm.setNumColegiado(colegiado.getNColegiado());
   						}
   							
   						
   						miForm.setAccion("messages.fichaCliente.clienteExiste");
   						miForm.setIdInstitucion(cli.getIdInstitucion().toString());

   						forward = "validarNoColegiado";
   					}	
   					
   				}
    			
    			miForm.setIdPersona(cenPersona.getIdPersona().toString());
    			miForm.setNombre(cenPersona.getNombre());
				miForm.setApellido1(cenPersona.getApellido1());
				miForm.setApellido2(cenPersona.getApellido2());
    			
				
    			
    			
    				
    		}else{
    			forward =  insertarNoColegiado(mapping, formulario, request, response);
    			
    		}
			
			

	   } catch (Exception e) {
		   throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		return forward;
	}
	

	private void mandarDatosColegiado (String idInstitucion, String idPersona, DatosGeneralesForm miform, HttpServletRequest request) throws SIGAException 
	{
		UsrBean user = null;
		
		try {
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			int idInstitucionValor = new Integer(miform.getIdInstitucion()).intValue(); 
			long idPersonaValor = new Long(miform.getIdPersona()).longValue(); 
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionValor,idPersonaValor);

			// RGG 24-06-2005 para controlar que no se muestran datos de persona si no es la institucion creadora
			String location = user.getLocation();
			String consultaPersona = ClsConstants.DB_TRUE;
			String institucionCreadora = new Long(idPersona).toString(); 
			if (institucionCreadora.length()>=10) {
				institucionCreadora = institucionCreadora.substring(0,4); 
				if (location.equals(institucionCreadora)) {
					consultaPersona = ClsConstants.DB_FALSE;
				}
			}
			request.setAttribute("CenDatosPersonalesOtraInstitucion",consultaPersona);
			
			// consulta de los datos generales
			Vector resultado = clienteAdm.getDatosPersonales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
		
			// para los datos anteriores
			Hashtable datosGeneralesAnteriores = new Hashtable();
			if (resultado!=null && resultado.size()>0) {				
				request.setAttribute("CenResultadoDatosGenerales",resultado);
				datosGeneralesAnteriores = (Hashtable) resultado.get(0);
				// guardo el idpersona y el idinstitucion para la consulta de comparacion
				datosGeneralesAnteriores.put(CenPersonaBean.C_IDPERSONA,miform.getIdPersona());
				datosGeneralesAnteriores.put("IDINSTITUCION",miform.getIdInstitucion());
				String primeraLetraCif =(String)datosGeneralesAnteriores.get("NIFCIF");
				primeraLetraCif = primeraLetraCif.substring(0, 1);
				request.setAttribute("primeraLetraCif",primeraLetraCif);
			}
			
			// para saber si es colegiado
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean beanCol = admCol.getDatosColegiales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
				
				// in5755 // Recuperamos el estado colegial para mostrarlo en la cabecera
				CenClienteAdm admCli = new CenClienteAdm(this.getUserBean(request));
				request.setAttribute("ESTADOCOLEGIAL",admCli.getEstadoColegial(idPersona, idInstitucion));
				
				// Comprueba si el usuario reside en algun otro colegio
				request.setAttribute("RESIDENTE", beanCol.getSituacionResidente());
			}
			
			String tipo = request.getParameter("tipo");
			if (tipo != null && tipo.equalsIgnoreCase("LETRADO")) {
				request.setAttribute("TIPO",tipo);
				tipoColegiado = "censo.fichaCliente.literal.letrado";
			} else { 
				if ((tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO))||(tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA))) {
					// inc-6271 Sacamos el estado colegial de la misma forma que en la busqueda para dar mas informacion
					tipoColegiado = clienteAdm .getEstadoColegial(idPersona,idInstitucion);
				} else { 
					if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
						tipoColegiado = "censo.tipoCliente.noColegiado";
						}
					}
				}
			request.setAttribute("CenDatosGeneralesColegiado",tipoColegiado);

			// Si la operacion puede implicar modificacion realizo una copia de los datos originales
			// para el proceso de modificacion en la BBDD
			if (miform.getAccion().equalsIgnoreCase("editar")){
				request.getSession().setAttribute("DATABACKUP",datosGeneralesAnteriores);
			}
			
	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
	}

	
	private void mandarDatosNoColegiado (String idInstitucion, String idPersona, DatosGeneralesForm miform, HttpServletRequest request) throws SIGAException 
	{
		UsrBean user = null;
		
		try {
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			int idInstitucionValor = new Integer(miform.getIdInstitucion()).intValue(); 
			long idPersonaValor = new Long(miform.getIdPersona()).longValue(); 
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionValor,idPersonaValor);

			// RGG 24-06-2005 para controlar que no se muestran datos de persona si no es la institucion creadora
			String location = user.getLocation();
			String consultaPersona = ClsConstants.DB_TRUE;
			String institucionCreadora = new Long(idPersona).toString(); 
			if (institucionCreadora.length()>=10) {
				institucionCreadora = institucionCreadora.substring(0,4); 
				if (location.equals(institucionCreadora)) {
					consultaPersona = ClsConstants.DB_FALSE;
				}
			}
			request.setAttribute("CenDatosPersonalesOtraInstitucion",consultaPersona);
			
			// consulta de los datos generales
			Vector resultado = clienteAdm.getDatosPersonales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
		
			// para los datos anteriores
			Hashtable datosGeneralesAnteriores = new Hashtable();
			if (resultado!=null && resultado.size()>0) {
				request.setAttribute("CenResultadoDatosGenerales",resultado);
				datosGeneralesAnteriores = (Hashtable) resultado.get(0);
				// guardo el idpersona y el idinstitucion para la consulta de comparacion
				datosGeneralesAnteriores.put(CenPersonaBean.C_IDPERSONA,miform.getIdPersona());
				datosGeneralesAnteriores.put("IDINSTITUCION",miform.getIdInstitucion());
				String primeraLetraCif =(String)datosGeneralesAnteriores.get("NIFCIF");
				primeraLetraCif = primeraLetraCif.substring(0, 1);
				request.setAttribute("primeraLetraCif",primeraLetraCif);
			}
			
			// para saber si es colegiado
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean beanCol = admCol.getDatosColegiales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
			}

			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO))
				tipoColegiado = "censo.tipoCliente.colegiado";
			else 
				if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO))
					tipoColegiado = "censo.tipoCliente.noColegiado";
				else
					if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) 
						tipoColegiado = "censo.tipoCliente.colegiadoBaja";
			request.setAttribute("CenDatosGeneralesColegiado",tipoColegiado);
			
			//Consulto los datos no colegiados y los mando en el request:
			CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(this.getUserBean(request));
			CenNoColegiadoBean beanNoColegiado = null;
			String where = " WHERE "+CenNoColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+
						   " AND "+CenNoColegiadoBean.C_IDPERSONA+"="+idPersona;
			Vector vNoColegiados = admNoColegiado.select(where);
			
			if (!vNoColegiados.isEmpty()) {
				beanNoColegiado = (CenNoColegiadoBean)admNoColegiado.select(where).get(0);
				
				GestorContadores gcSJ = new GestorContadores(this.getUserBean(request));
				
				//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
				Hashtable contadorTablaHashSJ=gcSJ.getContador(beanNoColegiado.getIdInstitucion(),ClsConstants.SOCIEDADSJ);
				
				miform.setAnotaciones(beanNoColegiado.getAnotaciones());
				//Eliminamos este campo y se añaden 3 que formaran el nuevo numero de registro
				
				miform.setPrefijoNumRegSJ(beanNoColegiado.getPrefijoNumReg());
				miform.setSufijoNumRegSJ(beanNoColegiado.getSufijoNumReg());

				
				if (beanNoColegiado.getContadorNumReg()!=null){
//					 Se formatea el campo contador rellenando de 0 por la izquierda cuando se muestra
					
					Integer longitudSJ= new Integer(contadorTablaHashSJ.get("LONGITUDCONTADOR").toString());
					int longitudContadorSJ=longitudSJ.intValue();
					String contadorFinalSJ=UtilidadesString.formatea(beanNoColegiado.getContadorNumReg(),longitudContadorSJ,true);
					miform.setContadorNumRegSJ(contadorFinalSJ);
				}
				else{
					if (beanNoColegiado.getContadorNumReg()!=null){
					miform.setContadorNumRegSJ(beanNoColegiado.getContadorNumReg().toString());
					}else{
						//miform.setContadorNumRegSJ("");
						GestorContadores gcSJ2 = new GestorContadores(this.getUserBean(request));
		        		
		        		//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
		        			Hashtable contadorTablaHashSJ2=gcSJ.getContador(Integer.valueOf(miform.getIdInstitucion()),ClsConstants.SOCIEDADSJ);
		        			miform.setLongitudSJ(contadorTablaHashSJ.get("LONGITUDCONTADOR").toString());
		        			
		        		if (contadorTablaHashSJ2.get("MODO").toString().equals("0")){ 
					 		miform.setPrefijoNumRegSJ((String)contadorTablaHashSJ2.get("PREFIJO"));
							miform.setSufijoNumRegSJ((String)contadorTablaHashSJ2.get("SUFIJO"));
							miform.setContadorNumRegSJ(contadorTablaHashSJ2.get("CONTADOR").toString());
							miform.setModoSociedadSJ(""+contadorTablaHashSJ2.get("MODO"));
							
		        		}
		        		else{
		        			miform.setPrefijoNumRegSJ((String)contadorTablaHashSJ2.get("PREFIJO"));
							miform.setSufijoNumRegSJ((String)contadorTablaHashSJ2.get("SUFIJO"));
							miform.setContadorNumRegSJ("");
							miform.setModoSociedadSJ(""+contadorTablaHashSJ2.get("MODO"));
		        		}
					}
				}
				GestorContadores gcSP = new GestorContadores(this.getUserBean(request));
				
				//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
				Hashtable contadorTablaHashSP=gcSP.getContador(beanNoColegiado.getIdInstitucion(),ClsConstants.SOCIEDADSP);
				
			
				//Eliminamos este campo y se añaden 3 que formaran el nuevo numero de registro
				
				miform.setPrefijoNumRegSP(beanNoColegiado.getPrefijoNumRegSP());
				miform.setSufijoNumRegSP(beanNoColegiado.getSufijoNumRegSP());

				
				if (beanNoColegiado.getContadorNumRegSP()!=null){
//					 Se formatea el campo contador rellenando de 0 por la izquierda cuando se muestra
					
					Integer longitudSP= new Integer(contadorTablaHashSP.get("LONGITUDCONTADOR").toString());
					int longitudContadorSP=longitudSP.intValue();
					String contadorFinalSP=UtilidadesString.formatea(beanNoColegiado.getContadorNumRegSP(),longitudContadorSP,true);
					miform.setContadorNumRegSP(contadorFinalSP);
				}
				else{
					if (beanNoColegiado.getContadorNumRegSP()!=null){
					miform.setContadorNumRegSP(beanNoColegiado.getContadorNumRegSP().toString());
					}else{
						//miform.setContadorNumRegSP("");
						GestorContadores gcSP1 = new GestorContadores(this.getUserBean(request));
		        		
		        		//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
		        			Hashtable contadorTablaHashSP1=gcSP1.getContador(Integer.valueOf(miform.getIdInstitucion()),ClsConstants.SOCIEDADSP);
		        			miform.setLongitudSP(contadorTablaHashSP1.get("LONGITUDCONTADOR").toString());
		        			
		        		if (contadorTablaHashSP1.get("MODO").toString().equals("0")){ 
					 		miform.setPrefijoNumRegSP((String)contadorTablaHashSP1.get("PREFIJO"));
							miform.setSufijoNumRegSP((String)contadorTablaHashSP1.get("SUFIJO"));
							miform.setContadorNumRegSP(contadorTablaHashSP1.get("CONTADOR").toString());
							miform.setModoSociedadSP(""+contadorTablaHashSP1.get("MODO"));
							
		        		}
		        		else{
		        			miform.setPrefijoNumRegSP((String)contadorTablaHashSP1.get("PREFIJO"));
							miform.setSufijoNumRegSP((String)contadorTablaHashSP1.get("SUFIJO"));
							miform.setContadorNumRegSP("");
							miform.setModoSociedadSP(""+contadorTablaHashSP1.get("MODO"));
		        		}
					}
				}	
				
				
				/*miform.setSufijoNumRegSP(beanNoColegiado.getSufijoNumRegSP());*/
				/*miform.setSufijoNumRegSJ(beanNoColegiado.getSufijoNumReg());*/
				miform.setSociedadSJ(beanNoColegiado.getSociedadSJ());
				miform.setSociedadSP(beanNoColegiado.getSociedadSP());
				miform.setTipo(beanNoColegiado.getTipo());
				
				miform.setLongitudSP((contadorTablaHashSP.get("LONGITUDCONTADOR") == null)?"":contadorTablaHashSP.get("LONGITUDCONTADOR").toString());
				miform.setModoSociedadSP((contadorTablaHashSP.get("MODO") == null)?"":contadorTablaHashSP.get("MODO").toString());
				miform.setLongitudSJ((contadorTablaHashSJ.get("LONGITUDCONTADOR") == null)?"":contadorTablaHashSJ.get("LONGITUDCONTADOR").toString());
				miform.setModoSociedadSJ((contadorTablaHashSJ.get("MODO") == null)?"":contadorTablaHashSJ.get("MODO").toString());
			}
			Hashtable hashNoColegiadoOriginal = admNoColegiado.beanToHashTable(beanNoColegiado);
			request.getSession().setAttribute("hashNoColegiadoOriginal",hashNoColegiadoOriginal);
			
			//Consulto los datos de persona y los mando en el request:
			CenPersonaAdm admPersona = new CenPersonaAdm(this.getUserBean(request));
			CenPersonaBean beanPersona = null;
			where = " WHERE "+CenNoColegiadoBean.C_IDPERSONA+"="+idPersona;
			Vector vPersonas = admPersona.select(where);
			if (!vPersonas.isEmpty()) {
				beanPersona = (CenPersonaBean)admPersona.select(where).get(0);
				miform.setDenominacion(beanPersona.getNombre());
				if(beanPersona.getApellido1().equals("#NA")){
					miform.setAbreviatura("");
				}else{
					miform.setAbreviatura(beanPersona.getApellido1());
				}				
				miform.setFechaConstitucion(GstDate.getFormatedDateShort(user.getLanguage(),beanPersona.getFechaNacimiento()));
			}
			
			// Si la operacion puede implicar modificacion realizo una copia de los datos originales
			// para el proceso de modificacion en la BBDD
			if (miform.getAccion().equalsIgnoreCase("editar")){
				request.getSession().setAttribute("DATABACKUP",datosGeneralesAnteriores);
			}
		}catch (SIGAException e) {
			throw e;	
	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
	}
	
	
	/*
	 * obtienen la visibilidad para el usuario 
	 */
	private String obtenerVisibilidadUsuario(HttpServletRequest req) throws ClsExceptions 
	{
		UsrBean user = (UsrBean) req.getSession().getAttribute("USRBEAN");
		String idInstitucion=user.getLocation();
		return CenVisibilidad.getVisibilidadInstitucion(idInstitucion);
	}

	/**
	 * Metodo para modificar los datos generales de un no colegiado de tipo personal.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		Hashtable hashOriginal = new Hashtable(); 		
		UserTransaction tx = null;
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenClienteAdm adminCli=new CenClienteAdm(this.getUserBean(request));
			CenPersonaAdm adminPer=new CenPersonaAdm(this.getUserBean(request));
			CenColegiadoAdm admColegiado = new CenColegiadoAdm(this.getUserBean(request));
 			
			// Obtengo los datos del formulario
			DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
			
			if(miForm.getNumIdentificacion()==null || miForm.getNumIdentificacion().equals(""))
			{				
				String numIdent= (String)getIdenHistorico(mapping, miForm, request, response,1,usr.getLocation());
				miForm.setNumIdentificacion(numIdent);
				miForm.setTipoIdentificacion("50");				
			}
			
			// tratamiento del fichero de fotografia
		    String pathImagenes = "";
		    String nombreFoto = "";
		    
		    // obtencion del path app desde tabla parametros
		    GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));    
		    //pathImagenes = paramAdm.getValor(miForm.getIdInstitucion(),ClsConstants.MODULO_CENSO,ClsConstants.PATH_APP, null);
			//pathImagenes += File.separator + ClsConstants.RELATIVE_PATH_FOTOS;
			
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			pathImagenes = rp.returnProperty("directorios.carpeta.fotos"); 
		    
		    
		    FormFile foto = miForm.getFoto();
		    if(foto==null || foto.getFileSize()<1){
//		    	throw new SIGAException("messages.general.error.ficheroNoExiste");
		    }
		    else{
		    	InputStream stream =null;
		    	nombreFoto = miForm.getFoto().getFileName();
		    	String extension = nombreFoto.substring(nombreFoto.lastIndexOf("."),nombreFoto.length());

		    	// RGG control de la extension
		    	if (extension==null || extension.trim().equals("")
	    			|| (!extension.trim().toUpperCase().equals(".JPG")
				    && !extension.trim().toUpperCase().equals(".GIF")
					&& !extension.trim().toUpperCase().equals(".JPEG"))) {
		    		
		    		throw new SIGAException("messages.error.imagen.tipoNoCorrecto");
		    	}
		    	
		    	nombreFoto = miForm.getNumIdentificacion() + extension;
		    	OutputStream bos = null;
		    	try {			
		    		//retrieve the file data
		    		stream = foto.getInputStream();
		    		//write the file to the file specified
		    		String idInstitucion=miForm.getIdInstitucion();
		    		File camino = new File (pathImagenes + File.separator + idInstitucion);
		    		camino.mkdirs();
		    		bos = new FileOutputStream(pathImagenes + File.separator + idInstitucion +File.separator+nombreFoto );
		    		int bytesRead = 0;
		    		byte[] buffer = new byte[8192];
		    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		    			bos.write(buffer, 0, bytesRead);
		    		}			    
		    		
		    		//*****************************
		    		//
		    		// ESTAS EXCEPCIONES SON CLARAMENTE CANDIDATAS A MOSTRAR INFORMACIÓN PARA EL USUARIO
		    	} 
		    	catch (FileNotFoundException fnfe) {
		    		throw new SIGAException("message.err.error.subiendoarchivo.datosgenerales",fnfe);
		    	}
		    	catch (IOException ioe) {
		    		throw new SIGAException("message.err.error.subiendoarchivo.datosgenerales",ioe);
		    	}
		    	finally	{
		    		// close the stream
		    		stream.close();
		    		bos.close();
		    	}
		    }
		    
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = miForm.getDatos();
			hash.put(CenHistoricoBean.C_IDPERSONA, miForm.getIdPersona());
			hash.put(CenHistoricoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			boolean isColegiado = false;
			try {
					isColegiado = admColegiado.existeColegiado(new Integer(miForm.getIdInstitucion()),miForm.getNumColegiado(),miForm.getNumColegiado())!=null;	
			} catch (Exception e) {
				isColegiado = false;
			}
			
			
			

			// guardo en el hash el path de la imagen para grabar en cliente
			if (nombreFoto!=null && !nombreFoto.equals("")) {
	    		hash.put(CenClienteBean.C_FOTOGRAFIA, nombreFoto);			
			} 
			else {
				hash.remove(CenClienteBean.C_FOTOGRAFIA);
			}

			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			hashOriginal=(Hashtable)request.getSession().getAttribute("DATABACKUP");
			
			// primero compruebo la existencia del nif
			// pero solamente cuando ha cambiado el NIF
			String nifAnterior = (String) hashOriginal.get(CenPersonaBean.C_NIFCIF); 
			String nifNuevo = (String)miForm.getNumIdentificacion().toUpperCase();	
			nifAnterior =UtilidadesString.LTrim(nifAnterior,"0");
			nifNuevo =UtilidadesString.LTrim(nifNuevo,"0");			
			//if(nifNuevo.length()<9)
				//nifNuevo = UtilidadesString.relleno("0",9 - nifNuevo.length()) + nifNuevo;  
			if (nifAnterior!=null && !nifAnterior.toUpperCase().equals(nifNuevo)) {
				if (adminPer.existeNifPersona(miForm.getNumIdentificacion(), miForm.getNombre(), miForm.getApellido1(), miForm.getApellido2())) {
				      throw new SIGAException("messages.censo.nifcifExiste");
				}
			}
			
			hash.put(CenPersonaBean.C_NIFCIF,miForm.getNumIdentificacion().toUpperCase());
			
			if(hash.get(CenPersonaBean.C_NOMBRE)!=null)
				hash.put(CenPersonaBean.C_NOMBRE, ((String)hash.get(CenPersonaBean.C_NOMBRE)).trim());
			if(hash.get(CenPersonaBean.C_APELLIDOS1)!=null)
				hash.put(CenPersonaBean.C_APELLIDOS1, ((String)hash.get(CenPersonaBean.C_APELLIDOS1)).trim());
			if(hash.get(CenPersonaBean.C_APELLIDOS2)!=null)
				hash.put(CenPersonaBean.C_APELLIDOS2, ((String)hash.get(CenPersonaBean.C_APELLIDOS2)).trim());
			
			// Adecuo formatos
			hash = this.prepararFormatosFechas(hash);
			hash = this.controlFormatosCheck(hash);

			// Comienzo control de transacciones
			tx = usr.getTransactionPesada();
			tx.begin();	

		
			// insert de la parte de cliente
			
			//**************************************************************
			// SI EL UPDATE DEVUELVE FALSE ES QUE NO EXISTE REGISTRO EN BBDD
			//
			if (!adminCli.update(hash,hashOriginal)) {
				//LMS 21/08/2006
				//Cambio por el nuevo uso de subLiteral en SIGAException.
				//SIGAException exc=new SIGAException("messages.err.noseque.datosgenerales.de.alguien");
				//exc.setSubLiteral("messages.censo.clientenoexiste.bbdd");
				
				SIGAException exc=new SIGAException("messages.censo.clientenoexiste.bbdd");
				throw exc;
			}
			
			// insert de la parte de persona
			if (!adminPer.update(hash,hashOriginal)) {
				//LMS 21/08/2006
				//Cambio por el nuevo uso de subLiteral en SIGAException.
				//SIGAException exc=new SIGAException("messages.err.noseque.datosgenerales.de.alguien");
				//exc.setSubLiteral("messages.censo.personanoexiste.bbdd");
				
				SIGAException exc=new SIGAException("messages.censo.personanoexiste.bbdd");
				throw exc;
			}
			
			
			// insert unico para el historico
			// no hace falta hashHist.put(CenHistoricoBean.C_IDHISTORICO, adminHist.getNuevoID(hash).toString());			
			// Cargo una nueva tabla hash para insertar en la tabla de historico
			Hashtable hashHist = new Hashtable();			
			hashHist.put(CenHistoricoBean.C_MOTIVO, miForm.getMotivo());
			CenHistoricoAdm admHis = new CenHistoricoAdm (this.getUserBean(request));
			
			//Se inserta el histórico de los datos de CenPersona
			if (!admHis.insertCompleto (hashHist, hash, hashOriginal, "CenPersonaBean", CenHistoricoAdm.ACCION_UPDATE, this.getLenguaje(request))) {
				throw new ClsExceptions(admHis.getError());
			}	
			
			//Se inserta el histórico de los datos de CenCliente
			if (!admHis.insertCompleto (hashHist, hash, hashOriginal, "CenClienteBean", CenHistoricoAdm.ACCION_UPDATE, this.getLenguaje(request))) {
				throw new ClsExceptions(admHis.getError());
			}		
			
			String apareceRedAbogacia = (String)hash.get(CenClienteBean.C_NOAPARECERREDABOGACIA);
			String apareceRedAbogaciaOld = (String)hashOriginal.get(CenClienteBean.C_NOAPARECERREDABOGACIA);
			//String is = CenClienteBean.c_t
			if(isColegiado && apareceRedAbogacia!=null && apareceRedAbogaciaOld!=null && !apareceRedAbogaciaOld.equalsIgnoreCase(apareceRedAbogacia)){
				//insertando en la cola de modificacion de datos para Consejos
				CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (this.getUserBean (request));
				if (! colaAdm.insertarCambioEnCola(ClsConstants.COLA_CAMBIO_LETRADO_LOPD,
						new Integer(miForm.getIdInstitucion()), new Long(miForm.getIdPersona()), new Long(apareceRedAbogacia)))
				throw new SIGAException (colaAdm.getError ());
			}
			
			// Lanzamos el proceso de revision de suscripciones del letrado 
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(miForm.getIdInstitucion(),
																					  miForm.getIdPersona(),
																					  "",
																					  ""+this.getUserName(request));
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
			
			tx.commit();

			//Mandamos los datos para refrescar:
			request.setAttribute("mensaje","messages.updated.success");
			request.setAttribute("idPersona",miForm.getIdPersona());
			request.setAttribute("idInstitucion",miForm.getIdInstitucion());
			String tipo = "";
			// Si el tipo es null lo ponemos a personal:
			if (request.getParameter("tipo")!=null && !request.getParameter("tipo").equals("")){
				tipo=request.getParameter("tipo");
			}else{
				tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			}	
			
			request.setAttribute("tipo",tipo);
			//request.setAttribute("error","OK");
	   } catch (SIGAException e) {
		 	throw e;
	   } catch (Exception e) {
	   		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
	   return "exitoInsercion";	   
	   
	}

	/**
	 * Metodo que implementa el modo abrirSolicitud
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirSolicitud (ActionMapping mapping,	MasterForm formulario,	HttpServletRequest request,	HttpServletResponse response) throws SIGAException 
	{
		try {
			DatosGeneralesForm miform = (DatosGeneralesForm)formulario;
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			Hashtable hashCliente = new Hashtable();
			hashCliente.put("IDINSTITUCION",miform.getIdInstitucion());
			hashCliente.put("IDPERSONA",miform.getIdPersona());
			Vector resultado = clienteAdm.select(hashCliente);
			if (resultado!=null) {
				request.setAttribute("CenSolititudDatosGeneralesResultados",resultado);
			}

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserName(request),user,new Integer(miform.getIdInstitucion()).intValue(),new Long(miform.getIdPersona()).longValue());
			Hashtable hashPersona = new Hashtable();
			hashPersona.put("IDPERSONA",miform.getIdPersona());
			Vector resultadoPersona = personaAdm.select(hashPersona);
			if (resultadoPersona!=null) {
				request.setAttribute("CenSolititudDatosGeneralesPersonaResultados",resultadoPersona);
			}

			// para saber si es colegiado
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean beanCol = admCol.getDatosColegiales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
			}
	   } 	catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		
		// COMUN
		return "solicitarModificacion";
	}

	/**
	 * Metodo que implementa el modo insertarSolicitud
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertarSolicitud (ActionMapping mapping,	MasterForm formulario,	HttpServletRequest request,	HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;
	
		try {
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			DatosGeneralesForm miform = (DatosGeneralesForm)formulario;
			Hashtable hash = miform.getDatos();

			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	

			hash = this.controlFormatosCheckSolicitud(hash);
		
			// CAMBIO DE NOMBRE LOS CAMPOS ABONO Y CARGO
			hash.put("ABONOS",(String)hash.get("ABONOSBANCO"));
			hash.put("CARGOS",(String)hash.get("CARGOSBANCO"));
						
			hash.put("IDESTADOSOLIC",new Integer(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE).toString());
			hash.put("FECHAALTA","SYSDATE");

			Long idSolicitud = new Long (ClsMngBBDD.nextSequenceValue("SEQ_SOLICITMODIFDATOSBASICOS"));
			hash.put("IDSOLICITUD",idSolicitud.toString());
				
			CenSolicitModifDatosBasicosAdm admSolic = new CenSolicitModifDatosBasicosAdm (this.getUserBean(request));
			if (!admSolic.insert(hash)) {
				throw new ClsExceptions(admSolic.getError());
			}	
			
			tx.commit();

	   } 	catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }

		return this.exitoModalSinRefresco("messages.censo.solicitudes.exito",request);

	}

	/**
	 * Metodo para insertar un no colegiado de tipo personal.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar (ActionMapping mapping,	MasterForm formulario,	HttpServletRequest request,	HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;		
	
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String forward = "";
 			
			// Obtengo los datos del formulario
			DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
			if(miForm.getNumIdentificacion()==null || miForm.getNumIdentificacion().equals(""))
			{				
				String numIdent= (String)getIdenHistorico(mapping, miForm, request, response,1,usr.getLocation());
				miForm.setNumIdentificacion(numIdent);
				miForm.setTipoIdentificacion("50");
				//request.getSession().setAttribute("MOSTRARMENSAJENOIDENTIFICADOR","El número de identificación sugerido ya existía. Se ha calculado uno nuevo y a continuación podrá consultarlo");
			}
			Hashtable hash = miForm.getDatos();

			//CenPersonaAdm adminPer=new CenPersonaAdm(this.getUserName(request));
			CenClienteAdm adminCli=new CenClienteAdm(this.getUserBean(request));

			// Adecuo formatos
			hash = this.prepararFormatosFechas(hash);
			hash = this.controlFormatosCheck(hash);

    		String numIdentificacion = miForm.getNumIdentificacion();
    		String idInstitucion = usr.getLocation();
    		hash.put("IDINSTITUCION", idInstitucion);

		    String pathImagenes = "";
		    String nombreFoto = "";
		    
    		// tratamiento del fichero de fotografia
		    FormFile foto = miForm.getFoto();
		    if(foto==null || foto.getFileSize()<1){
//		    	throw new SIGAException("messages.general.error.ficheroNoExiste");
	    	}else{

			    // obtencion del path desde tabla parametros
			    GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request)); 
				
			    pathImagenes = paramAdm.getValor(miForm.getIdInstitucion(),ClsConstants.MODULO_CENSO,ClsConstants.PATH_APP, null);
				pathImagenes += File.separator + ClsConstants.RELATIVE_PATH_FOTOS;
		    	
		    	InputStream stream =null;
		    	nombreFoto = miForm.getFoto().getFileName();
		    	String extension = nombreFoto.substring(nombreFoto.lastIndexOf("."),nombreFoto.length());
		    	
		    	// RGG control de la extension
		    	if (extension==null || extension.trim().equals("")
		    			|| (!extension.trim().toUpperCase().equals(".JPG")
					    && !extension.trim().toUpperCase().equals(".GIF")
						&& !extension.trim().toUpperCase().equals(".JPEG"))) {
		    		throw new SIGAException("messages.error.imagen.tipoNoCorrecto");
		    	}
		    	
		    	nombreFoto = numIdentificacion + extension;
		    	OutputStream bos = null;
		    	try {			
		    		//retrieve the file data
		    		stream = foto.getInputStream();
		    		//write the file to the file specified
		    		File camino = new File (pathImagenes + File.separator + idInstitucion);
		    		camino.mkdirs();
		    		bos = new FileOutputStream(pathImagenes + File.separator+ idInstitucion +File.separator+nombreFoto );
		    		int bytesRead = 0;
		    		byte[] buffer = new byte[8192];
		    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		    			bos.write(buffer, 0, bytesRead);
		    		}			    
		    		
		    	} catch (FileNotFoundException fnfe) {
		    		throw fnfe;
		    	}catch (IOException ioe) {
		    		throw ioe;
		    	}
		    	finally	{
		    		// close the stream
		    		stream.close();
		    		bos.close();
		    	}
		    }

			// guardo en el hash el path de la imagen para grabar en cliente
			if (nombreFoto!=null && !nombreFoto.equals("")) {
	    		hash.put(CenClienteBean.C_FOTOGRAFIA, nombreFoto);			
			} else {
				hash.remove(CenClienteBean.C_FOTOGRAFIA);
			}
    		
			// Comienzo control de transacciones
			tx = usr.getTransactionPesada();
			tx.begin();	

			CenPersonaBean perBean = null;
			String idinstitucion = miForm.getIdInstitucion();
			CenPersonaAdm perAdm = new CenPersonaAdm(this.getUserBean(request));
			long idPersonaValor=0;
			Vector personas = new Vector();
			if (numIdentificacion != null) {
				personas = perAdm.select("WHERE UPPER(" + CenPersonaBean.C_NIFCIF + ") = '" + numIdentificacion.toUpperCase() + "'");
				// insert de la parte de cliente paso un solo hash con los datos de cliente y de persona
				// CenClienteBean beanCli = adminCli.insertNoColegiado(hash, request);
				String stipoIdenti= (String)miForm.getTipoIdentificacion();
				if ((personas != null) && personas.size() == 1) {
					if(stipoIdenti.equals(ClsConstants.TIPO_IDENTIFICACION_OTRO))
					{
						String subNumIdentificacion = numIdentificacion.substring(0, 8);
						String p = numIdentificacion.substring(8);
						int num = new Integer(numIdentificacion.substring(8)).intValue();					
						num++;
						String numfinal=new Integer(num).toString();
						while(numfinal.length()<4){
							numfinal="0"+numfinal;
						}
						subNumIdentificacion= subNumIdentificacion + numfinal;
						miForm.setNumIdentificacion(subNumIdentificacion);
						hash.put(CenPersonaBean.C_NIFCIF, subNumIdentificacion);
						request.getSession().setAttribute("MOSTRARMENSAJE","mostrarMensaje");
					}
					
					perBean = (CenPersonaBean) personas.get(0);
					idPersonaValor = new Long(perBean.getIdPersona()).longValue();
					/*
					else
					{
						request.getSession().setAttribute("MOSTRARMENSAJECIFNIF","mostrarMensajeRepetidoCIFNIF");
					}
					*/
				}
			}

			CenClienteBean beanCli = null;
			boolean existenDatos = false;
			boolean existenDatosEnColegio = false;
			
			if (miForm.getContinuarAprobacion().equals("1")) {
				beanCli = adminCli.insertNoColegiado(hash, request);

			} else {

				if (perBean != null) {
					beanCli = adminCli.existeCliente(idPersonaValor, new Integer(idInstitucion));
					if (beanCli != null) {
						existenDatos = false;
						existenDatosEnColegio = true;
						adminCli.setError("messages.error.ora.00001");
					} else {
						beanCli = adminCli.existeClienteOtraInstitucion(idPersonaValor, new Integer(idInstitucion));
						if (beanCli != null) {
							existenDatos = true;
						} else {
							beanCli = adminCli.insertNoColegiado(hash, request);
							existenDatos = false;
						}
					}
				} else {
					beanCli = adminCli.insertNoColegiado(hash, request);
					existenDatos = false;
				}
			}
			
			/**se verifica que bien la persona existe con isExisteDatos que indica que es la misma persona,
			 *  pero que esta dado de alta en otro colegio y preguntara si quiere que también
			 *  se le de de alta en el actual colegio con los valores de datos generales
			 *  que tiene actualmente en el sistema**/
			if(existenDatos){
				String nombrePersona="", apellido1Persona="", apellido2Persona="";
				String nifcif="";
				if ((personas != null) && personas.size() == 1) {
					//perBean = (CenPersonaBean)personas.get(0);
					
					nombrePersona = perBean.getNombre().toUpperCase(); 
					apellido1Persona = perBean.getApellido1().toUpperCase(); 
					apellido2Persona = perBean.getApellido2().toUpperCase();
					nifcif = perBean.getNIFCIF().toUpperCase();
					String msj="";					
					
					int idInstitucionValor = new Integer(idInstitucion).intValue(); 
					CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),usr,idInstitucionValor,idPersonaValor);
					
					/**En la función de getDatosPersonales se verifica si el nocolegiado 
					 * existe en el colegio actual si no existe busca en los diferentes colegios*****/
					Vector resultado = clienteAdm.getDatosPersonales(idPersonaValor, new Integer(idInstitucion));
		              if (resultado!=null && resultado.size()==0){		  
		            	  	/**En la función de getDatosPersonalesOtraInstitucion se verifica si el nocolegiado 
		            	  	 * existe en el otras instituciones y se cogeran los datos en el que 
		            	  	 * se dio primero de alta dicho nocolegiado****/
		            	  
		            	  resultado = clienteAdm.getDatosPersonalesOtraInstitucion(idPersonaValor);
		            	  if (beanCli!=null){
		            		   msj = UtilidadesString.getMensajeIdioma (usr, "messages.censo.nifcifNombreApellidoExiste");	
		                       msj += ".Pulsando Aceptar se actualizará ese registro con: "+" "+nifcif+" "+nombrePersona+" "+apellido1Persona+" "+apellido2Persona+"." + "\u00BFDesea " + "Continuar\u003F";
		            	  }else{
		            		  msj = UtilidadesString.getMensajeIdioma (usr, "messages.censo.nifcifExiste3");	
		            		  msj += "-"+nombrePersona+" "+apellido1Persona+" "+apellido2Persona+". "+ "\u00BFDesea"+"Continuar\u003F";
		            	  }
		            	  // para los datos anteriores
		            	  Hashtable datosGeneralesAnteriores = new Hashtable();
		            	  if (resultado!=null && resultado.size()>0) {
		            		  request.setAttribute("CenResultadoDatosGenerales",resultado);
		            		  request.setAttribute ("msj", msj);					
		            		  forward = "continuarAprobacionNoColegiado";//llama a la pagina donde le pasa los datos.
		            		  tx.rollback ();
		            		  return forward;
		            	  }
		            	  
		              }else{		            	  	
		            	  	msj=UtilidadesString.getMensajeIdioma(usr.getLanguage(), "messages.censo.nifcifExiste4", new String[]{nifcif});		            	  	
		            	  	request.setAttribute("msj",msj);			  	
		            	  	tx.rollback();
		            	  	return "continuarAprobacion";
			
		              }
				}//Fin if 					
				
			  }//Fin beanCli==null||beanCli.isExisteDatos()
			String mensInformacion = "messages.inserted.success"; 
			if (!adminCli.getError().equals("")) {
				mensInformacion = adminCli.getError();
			}
			
			/** Si viene a vacio despues de recuperar los datos de beanCli = adminCli.insertNoColegiado pues no debe de entrar solo si el valor beanCli tiene datos. **/
			if (beanCli!=null && !existenDatosEnColegio){			
				// Inserto los datos del no colegiado en CenNoColegiado:
				CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(this.getUserBean(request));
				Hashtable hashNoColegiado = new Hashtable();
				hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,beanCli.getIdInstitucion());
				hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,beanCli.getIdPersona());
				//El tipo sera siempre Personal:
				hashNoColegiado.put(CenNoColegiadoBean.C_TIPO,ClsConstants.COMBO_TIPO_PERSONAL);
				//No es sociedad SJ:
				hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,ClsConstants.DB_FALSE);
				hashNoColegiado.put(CenNoColegiadoBean.C_USUMODIFICACION,usr.getUserName());
				hashNoColegiado.put(CenNoColegiadoBean.C_FECHAMODIFICACION,"SYSDATE");
				
				  admNoColegiado.insert(hashNoColegiado);
				  
				
				// Lanzamos el proceso de revision de suscripciones del letrado 
				String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(beanCli.getIdInstitucion().toString(),
																						  beanCli.getIdPersona().toString(),
																						  "",
																						  ""+this.getUserName(request));
				if ((resultado == null) || (!resultado[0].equals("0")))
					throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");
	
				tx.commit();
	
				//Mandamos los datos para el refresco:
				request.setAttribute("mensaje",mensInformacion);
				request.setAttribute("idPersona",beanCli.getIdPersona().toString());
				request.setAttribute("idInstitucion",beanCli.getIdInstitucion().toString());
				request.setAttribute("tipo",ClsConstants.COMBO_TIPO_PERSONAL);
			}//fin beanCli
			else
			{
				tx.rollback ();
				request.setAttribute("mensaje",mensInformacion);
				//Mandamos los datos para el refresco:
				request.setAttribute("mensaje",mensInformacion);
				request.setAttribute("idPersona",beanCli.getIdPersona().toString());
				request.setAttribute("idInstitucion",beanCli.getIdInstitucion().toString());
				request.setAttribute("tipo",ClsConstants.COMBO_TIPO_PERSONAL);
			}

	   } 	
	   catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
		return "exitoInsercion";			
	}

	/**
	 * Metodo para insertar un no colegiado de tipo personal.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertarNoColegiado (ActionMapping mapping,	MasterForm formulario,	HttpServletRequest request,	HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = this.getUserBean(request);

 			
			// Obtengo los datos del formulario
			DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
			Hashtable hash = miForm.getDatos();

			//CenPersonaAdm adminPer=new CenPersonaAdm(this.getUserName(request));
			CenClienteAdm adminCli=new CenClienteAdm(usr);

			// Adecuo formatos
			hash = this.prepararFormatosFechas(hash);
			hash = this.controlFormatosCheck(hash);

    		String numIdentificacion = miForm.getNumIdentificacion();
    		String idInstitucion = usr.getLocation();
    		hash.put("IDINSTITUCION", idInstitucion);

		    String pathImagenes = "";
		    String nombreFoto = "";
		    

			hash.remove(CenClienteBean.C_FOTOGRAFIA);
    		
    		/*CenPersonaAdm adminPer=new CenPersonaAdm(usr);
    		CenPersonaBean cenPersona = adminPer.getPersona(miForm.getNumIdentificacion());
    		if(cenPersona!=null){
    			if (!( ( ComodinBusquedas.sustituirVocales(cenPersona.getNombre()).equals(ComodinBusquedas.sustituirVocales(miForm.getNombre().toUpperCase())) && ComodinBusquedas.sustituirVocales(cenPersona.getApellido1()).equals(ComodinBusquedas.sustituirVocales(miForm.getApellido1().toUpperCase())) ) || 
   					 ( ComodinBusquedas.sustituirVocales(cenPersona.getNombre()).equals(ComodinBusquedas.sustituirVocales(miForm.getNombre().toUpperCase())) && ComodinBusquedas.sustituirVocales(cenPersona.getApellido2()).equals(ComodinBusquedas.sustituirVocales(miForm.getApellido2().toUpperCase())) ) ||
   					 ( ComodinBusquedas.sustituirVocales(cenPersona.getApellido1()).equals(ComodinBusquedas.sustituirVocales(miForm.getApellido1().toUpperCase())) && ComodinBusquedas.sustituirVocales(cenPersona.getApellido2()).equals(ComodinBusquedas.sustituirVocales((""+miForm.getApellido2()).toUpperCase())) ) 
   					)) {
    				miForm.setNombre(cenPersona.getNombre());
    				miForm.setApellido1(cenPersona.getApellido1());
    				miForm.setApellido2(cenPersona.getApellido2());
   					 return existePersonaNoColegiado(mapping, formulario, request, response);
   				}
    			
			
				
    		}
			*/
			
			
			// Comienzo control de transacciones
			tx = usr.getTransactionPesada();
			tx.begin();	

			// insert de la parte de cliente
			// paso un solo hash con los datos de cliente y de persona
			CenClienteBean beanCli = adminCli.insertNoColegiado(hash, request);
			String mensInformacion = "messages.inserted.success"; 
			if (!adminCli.getError().equals("")) {
				mensInformacion = adminCli.getError();
			}

			
			// Inserto los datos del no colegiado en CenNoColegiado:
			CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(this.getUserBean(request));
			Hashtable hashNoColegiado = new Hashtable();
			hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,beanCli.getIdInstitucion());
			hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,beanCli.getIdPersona());
			//El tipo sera siempre Personal:
			hashNoColegiado.put(CenNoColegiadoBean.C_TIPO,ClsConstants.COMBO_TIPO_PERSONAL);
			//No es sociedad SJ:
			hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,ClsConstants.DB_FALSE);
			hashNoColegiado.put(CenNoColegiadoBean.C_USUMODIFICACION,usr.getUserName());
			hashNoColegiado.put(CenNoColegiadoBean.C_FECHAMODIFICACION,"SYSDATE");
			
   		    admNoColegiado.insert(hashNoColegiado);
   		    
   		    ///////////////////////////////////////////////////////////////////////////
   		 
   		    if(miForm.getIdTipoDireccion()!=null && !miForm.getIdTipoDireccion().equals("")){
   		    	Direccion direccion = new Direccion();
	   		    CenDireccionesBean beanDir = new CenDireccionesBean ();
				beanDir.setCodigoPostal (miForm.getCodigoPostal ());
				beanDir.setCorreoElectronico (miForm.getCorreoElectronico ());
				beanDir.setDomicilio (miForm.getDomicilio ());
				beanDir.setFax1 (miForm.getFax1 ());
				beanDir.setFax2 (miForm.getFax2 ());
				beanDir.setIdInstitucion(new Integer(idInstitucion));
				beanDir.setIdPais (miForm.getPais ());
				if (miForm.getPais ().equals ("")) {
					miForm.setPais (ClsConstants.ID_PAIS_ESPANA);
				}
				if (miForm.getPais().equals (ClsConstants.ID_PAIS_ESPANA)) {
					beanDir.setIdPoblacion (miForm.getPoblacion ());
					beanDir.setIdProvincia (miForm.getProvincia ());
					beanDir.setPoblacionExtranjera ("");
				} else {
					beanDir.setPoblacionExtranjera (miForm.getPoblacionExt ());
					beanDir.setIdPoblacion ("");
					beanDir.setIdProvincia ("");
				}
				beanDir.setIdPersona (beanCli.getIdPersona());
				beanDir.setMovil (miForm.getMovil ());
				beanDir.setPaginaweb (miForm.getPaginaWeb ());
				beanDir.setPreferente (this.campoPreferenteBooleanToString
						(miForm.getPreferenteMail (), 
						miForm.getPreferenteCorreo (), 
						miForm.getPreferenteFax (),
						miForm.getPreferenteSms ()));
				beanDir.setTelefono1 (miForm.getTelefono1 ());
				beanDir.setTelefono2 (miForm.getTelefono2 ());
				
				//estableciendo los datos del tipo de direccion
				String tiposDir = "";
				if(miForm.getIdTipoDireccion()!= null && !miForm.getIdTipoDireccion().equals("")){
					tiposDir = miForm.getIdTipoDireccion();
				}
				
				// Se llama a la interfaz Direccion para insertar una nueva direccion
				direccion.insertar(beanDir, tiposDir, miForm.getMotivo (), null, usr);
				
				request.setAttribute("idDireccion",beanDir.getIdDireccion().toString());   		    
   		    }
   		    
////////////////////////////////////////////////////////////////////////////////////////
			tx.commit();

			//Mandamos los datos para el refresco:
			request.setAttribute("mensaje",mensInformacion);
			request.setAttribute("idInstitucion",beanCli.getIdInstitucion().toString());
			request.setAttribute("idPersona",beanCli.getIdPersona().toString());
			request.setAttribute("nColegiado","");
			request.setAttribute("nif",miForm.getNumIdentificacion());
			request.setAttribute("nombre",miForm.getNombre());
			request.setAttribute("apellido1",miForm.getApellido1());
			request.setAttribute("apellido2",miForm.getApellido2());
	   
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, tx);
	    }  catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	    }
	    
	   return "exitoInsercionNoColegiado";			
	}
	private String campoPreferenteBooleanToString (Boolean mail, 
			Boolean correo, 
			Boolean fax,
			Boolean sms)
	throws SIGAException
	{
		String valor = "";

		try
		{
			if (mail!=null && mail.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO;
			if (fax!=null && fax.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_FAX;
			if (correo!=null && correo.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_CORREO;
			if (sms!=null && sms.booleanValue())
				valor += ClsConstants.TIPO_PREFERENTE_SMS;
		}
		catch (Exception e) {
			throwExcp ("messages.general.error", new String[] {"modulo.censo"}, e, null);
		}
		return valor;
	}

	/** 
	 * Adecua los formatos de las fechas para la insercion en BBDD. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	private Hashtable prepararFormatosFechas (Hashtable entrada)throws ClsExceptions 
	{
		String fecha;
				
		try {		
			fecha=(String)entrada.get("FECHANACIMIENTO");
			if ((fecha!=null)&&(!fecha.equalsIgnoreCase(""))){
				entrada.put("FECHANACIMIENTO",GstDate.getApplicationFormatDate("",fecha));	
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al preparar los formatos delas fechas.");		
		}
		
		return entrada;
	}
	
	/** 
	 * Prepara los campos de check para insertar en tabla. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	private Hashtable controlFormatosCheck (Hashtable entrada)throws SIGAException 
	{
		try {
			if (!entrada.containsKey(CenClienteBean.C_COMISIONES)) {
				entrada.put(CenClienteBean.C_COMISIONES,"0");
			}
				
			if (!entrada.containsKey(CenClienteBean.C_PUBLICIDAD)) {
				entrada.put(CenClienteBean.C_PUBLICIDAD,"0");
			}							
	
			if (!entrada.containsKey(CenClienteBean.C_GUIAJUDICIAL)) {
				entrada.put(CenClienteBean.C_GUIAJUDICIAL,"0");
			}
			if (!entrada.containsKey(CenClienteBean.C_NOENVIARREVISTA)) {
				entrada.put(CenClienteBean.C_NOENVIARREVISTA,"0");
			}
				
			if (!entrada.containsKey(CenClienteBean.C_NOAPARECERREDABOGACIA)) {
				entrada.put(CenClienteBean.C_NOAPARECERREDABOGACIA,"0");
			}
			
			if (!entrada.containsKey(CenClienteBean.C_EXPORTARFOTO)) {
				entrada.put(CenClienteBean.C_EXPORTARFOTO,"0");
			}			
				
		}
		catch (Exception e) {
			throw new SIGAException (e);
		}
		return entrada;
	}	
		
	/** 
	 * Prepara los campos de check para insertar en tabla de solicitud de modificación. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	private Hashtable controlFormatosCheckSolicitud (Hashtable entrada) throws SIGAException 
	{
		try {						
			if (!entrada.containsKey(CenClienteBean.C_PUBLICIDAD)) {
				entrada.put(CenClienteBean.C_PUBLICIDAD,"0");
			} else {
				if (entrada.get(CenClienteBean.C_PUBLICIDAD).equals("")) {
					entrada.put(CenClienteBean.C_PUBLICIDAD,"0");
				}
			}
	
			if (!entrada.containsKey(CenClienteBean.C_GUIAJUDICIAL)) {
				entrada.put(CenClienteBean.C_GUIAJUDICIAL,"0");
			} else {
				if (entrada.get(CenClienteBean.C_GUIAJUDICIAL).equals("")) {
					entrada.put(CenClienteBean.C_GUIAJUDICIAL,"0");
				}
			}
		}
		catch (Exception e) {
			throw new SIGAException (e);
		}
		
		return entrada;
	}
		
	/**
	 * Metodo que inserta un nuevo No Colegiado de un tipo sociedad
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertarSociedad (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UsrBean usr = (UsrBean) this.getUserBean(request);
		UserTransaction tx = null;
		
		try {		
			// Obtengo usuario y la transaccion
			tx = usr.getTransactionPesada();
 			
			// Obtengo los datos del formulario
			DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
			Hashtable hash = miForm.getDatos();

			CenClienteAdm adminCli=new CenClienteAdm(usr);

			// Adecuo formatos
			hash = this.prepararFormatosFechas(hash);
			hash = this.controlFormatosCheck(hash);

			// Datos necesarios tambien para la FOTO
    		String numIdentificacion = miForm.getNumIdentificacion();
    		String idInstitucion = usr.getLocation();
    		hash.put("IDINSTITUCION", idInstitucion);


    		//
    		// FOTO: Tratamiento del fichero de fotografia
    		//
		    FormFile foto = miForm.getFoto();
		    String pathImagenes = "";
		    String nombreFoto = "";

		    if(foto==null || foto.getFileSize()<1){
//	    	throw new SIGAException("messages.general.error.ficheroNoExiste");
		    }else{


			    // obtencion del path desde tabla parametros
			    GenParametrosAdm paramAdm = new GenParametrosAdm(usr); 
				
			    pathImagenes = paramAdm.getValor(miForm.getIdInstitucion(),ClsConstants.MODULO_CENSO,ClsConstants.PATH_APP, null);
				pathImagenes += File.separator + ClsConstants.RELATIVE_PATH_FOTOS;
		    	
		    	InputStream stream =null;
		    	nombreFoto = miForm.getFoto().getFileName();
		    	String extension = nombreFoto.substring(nombreFoto.lastIndexOf("."),nombreFoto.length());
		    	
		    	// RGG control de la extension
		    	if (extension==null || extension.trim().equals("")
		    			|| (!extension.trim().toUpperCase().equalsIgnoreCase(".JPG")
					    && !extension.trim().toUpperCase().equalsIgnoreCase(".GIF")
						&& !extension.trim().toUpperCase().equalsIgnoreCase(".JPEG"))) {
		    		throw new SIGAException("messages.error.imagen.tipoNoCorrecto");
		    	}
		    	
		    	nombreFoto = numIdentificacion + extension;
		    	OutputStream bos = null;
		    	try {			
		    		//retrieve the file data
		    		stream = foto.getInputStream();
		    		//write the file to the file specified
		    		File camino = new File (pathImagenes + File.separator + idInstitucion);
		    		camino.mkdirs();
		    		bos = new FileOutputStream(pathImagenes + File.separator+ idInstitucion +File.separator+nombreFoto );
		    		int bytesRead = 0;
		    		byte[] buffer = new byte[8192];
		    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		    			bos.write(buffer, 0, bytesRead);
		    		}			    
		    		
		    	} catch (FileNotFoundException fnfe) {
		    		throw fnfe;
		    	} catch (IOException ioe) {
		    		throw ioe;
		    	} finally	{
		    		// close the stream
		    		stream.close();
		    		bos.close();
		    	}
		    } // FIN del tratamiento del a FOTO
		    

			// guardo en el hash el path de la imagen para grabar en cliente
			if (nombreFoto!=null && !nombreFoto.equals("")) {
	    		hash.put(CenClienteBean.C_FOTOGRAFIA, nombreFoto);			
			} else {
				hash.remove(CenClienteBean.C_FOTOGRAFIA);
			}
    		
			// Comienza la transaccion:			
			tx.begin();	

			// insert de la parte de cliente
			// paso un solo hash con los datos de cliente y de persona

			//Anhado los datos de Denominacion, Abreviatura y Fecha de Constitucion en los campos nombre, Apellidos1 y Fecha Nacimiento:
			hash.put(CenPersonaBean.C_NOMBRE,miForm.getDenominacion());
			if (miForm.getAbreviatura()!=null && !miForm.getAbreviatura().equals("")){
				hash.put(CenPersonaBean.C_APELLIDOS1,miForm.getAbreviatura());
			}else{
				hash.put(CenPersonaBean.C_APELLIDOS1,"#NA");
			}
			
			//hash.put(CenPersonaBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaConstitucion()));           
			CenClienteBean beanCli = adminCli.insertNoColegiado(hash, request);
			String mensInformacion = "messages.inserted.success"; 
			if (!adminCli.getError().equals("")) {
				mensInformacion = adminCli.getError();
			}
			

			// Inserto los datos del no colegiado en CenNoColegiado:
			CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(usr);
			Hashtable hashNoColegiado = new Hashtable();
			hashNoColegiado.put(CenNoColegiadoBean.C_ANOTACIONES,miForm.getAnotaciones());
			hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,beanCli.getIdInstitucion());
			hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,beanCli.getIdPersona());
			
			String sociedadSJ = null;
			String sociedadSP = null;
			if (miForm.getSociedadSJ()!=null && miForm.getSociedadSJ().equals(ClsConstants.DB_TRUE))
				sociedadSJ = ClsConstants.DB_TRUE;
			else
				sociedadSJ = ClsConstants.DB_FALSE;
			if (miForm.getSociedadSP()!=null && miForm.getSociedadSP().equals(ClsConstants.DB_TRUE))
				sociedadSP = ClsConstants.DB_TRUE;
			else
				sociedadSP = ClsConstants.DB_FALSE;
			hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,sociedadSJ);
			hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSP,sociedadSP);
			String tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			String tipoBloqueado = request.getParameter("tipoBloqueado");
			if (miForm.getTipo()==null || miForm.getTipo().equals(""))
			{
				miForm.setTipo(tipoBloqueado);
			}
			if (miForm.getTipo()!=null && !miForm.getTipo().equals(""))
				tipo = miForm.getTipo();
			hashNoColegiado.put(CenNoColegiadoBean.C_TIPO,tipo);
			hashNoColegiado.put(CenNoColegiadoBean.C_USUMODIFICACION,usr.getUserName());
			hashNoColegiado.put(CenNoColegiadoBean.C_FECHAMODIFICACION,"SYSDATE");
			
			GestorContadores gcSJ = new GestorContadores(usr);
		//Obtenemos los datos de la tabla de contadores pasando el sufijo y el prefijo introducidos en el formnulario	
			Hashtable contadorConPrefijoSufijoHashSJ=gcSJ.getContador(beanCli.getIdInstitucion(),ClsConstants.SOCIEDADSJ, miForm.getPrefijoNumRegSJ(), miForm.getSufijoNumRegSJ());
		//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
			Hashtable contadorTablaHashSJ=gcSJ.getContador(beanCli.getIdInstitucion(),ClsConstants.SOCIEDADSJ);
			
			Integer longitudSJ= new Integer((contadorConPrefijoSufijoHashSJ.get("LONGITUDCONTADOR").toString()));
			int longitudContadorSJ=longitudSJ.intValue();
			
			if 	(miForm.getSociedadSJ()!=null && miForm.getSociedadSJ().equals("1")){// Si no es sociedad juridica los campos de numero registro no se validan			
			//Si estamos en modo historico
			//Antes de insertar el numero de registro introducido por el usuario realizamos la validacion
			// de dicho registro.
						
				if (contadorTablaHashSJ.get("MODO").toString().equals("1")){	
					Integer contNumRegSJ =new Integer(miForm.getContadorNumRegSJ());
					
					boolean yaExisteContadorSJ=gcSJ.comprobarUnicidadContador(contNumRegSJ.intValue(),contadorConPrefijoSufijoHashSJ);
					
					  if (!yaExisteContadorSJ){
					  	hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREG,miForm.getPrefijoNumRegSJ());
					  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREG,miForm.getSufijoNumRegSJ());
					  	String contadorFinalSJ=UtilidadesString.formatea(contNumRegSJ,longitudContadorSJ,true);
					  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREG,contadorFinalSJ);
					  	//Actualizamos la tabla Adm_contador
					  	gcSJ.setContador(contadorConPrefijoSufijoHashSJ, String.valueOf(Integer.parseInt(miForm.getContadorNumRegSJ())+1));
					  	
					  }else{
					  	// Se sugiere un valor, siendo este el siguiente numero de registro libre a partir del ultimo numero de contador
					  	// utilizado
					  	
					  	int contadorNuevoSJ=gcSJ.getSiguienteNumContador(contadorConPrefijoSufijoHashSJ);
					  	Integer contadorSugeridoSJ=new Integer(contadorNuevoSJ);
					  	
					  	String contadorFinalSugeridoSJ=UtilidadesString.formatea(contadorSugeridoSJ,longitudContadorSJ,true);
					  	
					  	miForm.setContadorNumRegSJ(contadorFinalSugeridoSJ);
					  	request.setAttribute("error","ERROR"); 
					  	request.setAttribute("contadorSugeridoSJ",contadorFinalSugeridoSJ);
					  	request.setAttribute("mensaje","messages.contador.error.existeContador");
					  	//throw new SIGAException ("messages.contador.error.existeContador");
					  	tx.rollback();
					  	return "exitoInsercion";	
					  }		
				}
				else{//Modo Registro (0), directamente se sugiere y se inserta al grabar un numero de contador
					
					int contadorNuevoSJ=gcSJ.getSiguienteNumContador(contadorTablaHashSJ);
				  	Integer contadorSugeridoSJ=new Integer(contadorNuevoSJ);
				  	
				  	String contadorFinalSugeridoSJ=UtilidadesString.formatea(contadorSugeridoSJ,longitudContadorSJ,true);
				  	
				  	miForm.setContadorNumRegSP(contadorFinalSugeridoSJ);
				  	hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREG,(String)contadorTablaHashSJ.get("PREFIJO"));
				  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREG,(String)contadorTablaHashSJ.get("SUFIJO"));
				  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREG,contadorFinalSugeridoSJ);
				  
				  	gcSJ.setContador(contadorTablaHashSJ,String.valueOf(Integer.parseInt(contadorFinalSugeridoSJ)+1));
				}
			}
			
			
				GestorContadores gcSP = new GestorContadores(this.getUserBean(request));
			//Obtenemos los datos de la tabla de contadores pasando el sufijo y el prefijo introducidos en el formnulario	
				Hashtable contadorConPrefijoSufijoHashSP=gcSP.getContador(beanCli.getIdInstitucion(),ClsConstants.SOCIEDADSP, miForm.getPrefijoNumRegSP(), miForm.getSufijoNumRegSP());
			//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
				Hashtable contadorTablaHashSP=gcSP.getContador(beanCli.getIdInstitucion(),ClsConstants.SOCIEDADSP);
				
				Integer longitudSP= new Integer((contadorConPrefijoSufijoHashSP.get("LONGITUDCONTADOR").toString()));
				int longitudContadorSP=longitudSP.intValue();
				
				if 	(miForm.getSociedadSP()!=null && miForm.getSociedadSP().equals("1")){// Si no es sociedad juridica los campos de numero registro no se validan			
				//Si estamos en modo historico
				//Antes de insertar el numero de registro introducido por el usuario realizamos la validacion
				// de dicho registro.
							
					if (contadorTablaHashSP.get("MODO").toString().equals("1")){	
						Integer contNumRegSP =new Integer(miForm.getContadorNumRegSP());
						
						boolean yaExisteContadorSP=gcSP.comprobarUnicidadContador(contNumRegSP.intValue(),contadorConPrefijoSufijoHashSP);
						
						  if (!yaExisteContadorSP){
						  	hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREGSP,miForm.getPrefijoNumRegSP());
						  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREGSP,miForm.getSufijoNumRegSP());
						  	String contadorFinalSP=UtilidadesString.formatea(contNumRegSP,longitudContadorSP,true);
						  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREGSP,contadorFinalSP);
						  	//Actualizamos la tabla Adm_contador
						  	gcSP.setContador(contadorConPrefijoSufijoHashSP, String.valueOf(Integer.parseInt(miForm.getContadorNumRegSP())+1));
						  	
						  }else{
						  	// Se sugiere un valor, siendo este el siguiente numero de registro libre a partir del ultimo numero de contador
						  	// utilizado
						  	
						  	int contadorNuevoSP=gcSP.getSiguienteNumContador(contadorConPrefijoSufijoHashSP);
						  	Integer contadorSugeridoSP=new Integer(contadorNuevoSP);
						  	
						  	String contadorFinalSugeridoSP=UtilidadesString.formatea(contadorSugeridoSP,longitudContadorSP,true);
						  	
						  	miForm.setContadorNumRegSP(contadorFinalSugeridoSP);
						  	request.setAttribute("error","ERROR"); 
						  	request.setAttribute("contadorSugeridoSP",contadorFinalSugeridoSP);
						  	request.setAttribute("mensaje","messages.contador.error.existeContador");
						  	//throw new SIGAException ("messages.contador.error.existeContador");
						  	tx.rollback();
						  	return "exitoInsercion";	
						  }		
					}
					else{//Modo Registro (0), directamente se sugiere y se inserta al grabar un numero de contador
						
						int contadorNuevoSP=gcSP.getSiguienteNumContador(contadorTablaHashSP);
					  	Integer contadorSugeridoSP=new Integer(contadorNuevoSP);
					  	
					  	String contadorFinalSugeridoSP=UtilidadesString.formatea(contadorSugeridoSP,longitudContadorSP,true);
					  	
					  	miForm.setContadorNumRegSP(contadorFinalSugeridoSP);
					  	hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREGSP,(String)contadorTablaHashSP.get("PREFIJO"));
					  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREGSP,(String)contadorTablaHashSP.get("SUFIJO"));
					  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREGSP,contadorFinalSugeridoSP);
					  
					  	gcSP.setContador(contadorTablaHashSP,String.valueOf(Integer.parseInt(contadorFinalSugeridoSP)+1));
					}
				}
				
		
		
		    //gc.setContador(gc.getContador(beanCli.getIdInstitucion(),ClsConstants.SOCIEDADSJ, miForm.getPrefijoNumReg(), miForm.getSufijoNumReg()), miForm.getContadorNumReg());
			//Antes hay que insertar en cen_clientes
			admNoColegiado.insert(hashNoColegiado);
			
			if(beanCli.isExisteDatos()){
				 mensInformacion = "messages.inserted.existed"; 
			}
			
			CenNoColegiadoActividadBean bean = new CenNoColegiadoActividadBean();
			bean.setIdActividadProfesional(new Integer(1));
			bean.setIdInstitucion(new Integer(idInstitucion));
			bean.setidInstitucionActividad(new Integer(2000));
			bean.setIdPersona(beanCli.getIdPersona());

			CenNoColegiadoActividadAdm admGrupos = new CenNoColegiadoActividadAdm(usr);
			if (admGrupos.insert(bean)) {
				CenHistoricoAdm admHist = new CenHistoricoAdm (usr);
				admHist.insertCompleto((CenHistoricoBean)null, bean, CenHistoricoAdm.ACCION_INSERT, this.getLenguaje(request));
			}
			request.getSession().setAttribute("hashNoColegiadoOriginal",hashNoColegiado);
			
			// Lanzamos el proceso de revision de suscripciones del letrado 
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(beanCli.getIdInstitucion().toString(),
																					  beanCli.getIdPersona().toString(),
																					  "",
																					  ""+usr.getLocation());
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");

			//Termino la transaccion:
			tx.commit();

			//Mandamos los datos para el refresco:
			request.setAttribute("mensaje",mensInformacion);
			request.setAttribute("idPersona",beanCli.getIdPersona().toString());
			request.setAttribute("idInstitucion",beanCli.getIdInstitucion().toString());
			request.setAttribute("tipo",tipo);
			request.setAttribute("error","OK"); 
		}catch (SIGAException e) {
				throw e;
				
	   } catch (Exception e) {
	   		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
	   return "exitoInsercion";			
	} //insertarSociedad ()
	
	@SuppressWarnings("unchecked")
	protected String getIdenHistorico (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response, int bandera,String institucion) throws ClsExceptions, SIGAException ,Exception
			{
		DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
		//Sacamos las guardias si hay algo selccionado en el turno
		String numero= null;
		String insti= miForm.getIdInstitucion();
		if(insti == null || insti.equals(""))
			insti = institucion;
		CenPersonaAdm admCli = new CenPersonaAdm(this.getUserBean(request) );
		numero = admCli.obtenerUltiIdenHistorico(insti);
		JSONObject json = new JSONObject();
		if(numero==null){
			numero="NIHN" + insti + "0001";//[0-9]{4}, donde el ultimo numero sera un max+1. Ej. NIHN2040;0011";
		}else{
			int num = new Integer(numero.substring(8)).intValue();
			num++;
			String numfinal=new Integer(num).toString();
			while(numfinal.length()<4){
				numfinal="0"+numfinal;
			}
			numero="NIHN" + insti + numfinal;
		}
		if(bandera==0)
		{
		json.put("numHistorico", numero);
		 //response.setContentType("text/x-json;charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
		return null;//"completado";
		}
		else
			return numero;
	}
	
	protected String getIdNotario(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response,
			int bandera,
			String institucion) throws ClsExceptions, SIGAException, Exception
	{
		// Variables y controles
		DatosGeneralesForm miForm = (DatosGeneralesForm) formulario;
		CenPersonaAdm perAdm = new CenPersonaAdm(this.getUserBean(request));
		
		// obteniendo la institucion
		String insti = miForm.getIdInstitucion();
		if (insti == null || insti.equals(""))
			insti = institucion;
		
		// calculando el numero de identificacion
		String numero = perAdm.obtenerUltiIdNotario(insti);
		if (numero == null || numero.equals("")) {
			numero = CenPersonaAdm.PREFIJO_IDENT_NOTARIO + insti + "0001";// [0-9]{4}, donde el ultimo numero sera un max+1. Ej. NIHN2040;0011";
		} else {
			Integer num = new Integer(numero);
			num++;
			String numfinal = num.toString();
			while (numfinal.length() < 4) {
				numfinal = "0" + numfinal;
			}
			numero = CenPersonaAdm.PREFIJO_IDENT_NOTARIO + insti + numfinal;
		}
		
		// devolviendo el valor calculado
		if (bandera == 0) { // proviene de Json
			JSONObject json = new JSONObject();
			json.put("numHistorico", numero);
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString());
			return null;// "completado";
		} else
			return numero;
	} // getIdNotario()

	// NO se utiliza. Por si se quiesiera emplear Ajax.
	protected void getAjaxTipo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException, Exception {

		DatosGeneralesForm miForm = (DatosGeneralesForm) formulario;
		// Recogemos el parametro enviado por ajax
		String numIdentificacion = request.getParameter("numIdentificacion");
		// Sacamos los turnos
		CenTipoSociedadAdm admSociedades = new CenTipoSociedadAdm(this.getUserBean(request));
		List<CenTipoSociedadBean> alTipos = null;
		ArrayList<CenTipoSociedadBean> tipos = new ArrayList<CenTipoSociedadBean>();
		if (numIdentificacion.substring(0, 1).equalsIgnoreCase("J")) {
			alTipos = admSociedades.select("where letracif='J' or letracif='Y'");
		} else {
			alTipos = admSociedades.select("where letracif='"+miForm.getTipo()+"'");
		}
		if (alTipos == null) {
			alTipos = new ArrayList<CenTipoSociedadBean>();
		} else {
			for (CenTipoSociedadBean tipo : alTipos) {
				tipos.add(tipo);
				ClsLogging.writeFileLog("DATOS GENERALES:turno:" + tipo.getDescripcion(), 10);
			}
		}
		
		miForm.setTipos(tipos);
		
		ClsLogging.writeFileLog("DATOS GENERALESS:Fin", 10);
		respuestaAjax(new AjaxCollectionXmlBuilder<CenTipoSociedadBean>(), alTipos, response);
	}
	

	/**
	 * Modifica los datos generales de un No Colegiado de un tipo sociedad
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificarSociedad (ActionMapping mapping,	MasterForm formulario,	HttpServletRequest request,	HttpServletResponse response) throws SIGAException 
	{
		Hashtable hashOriginal = new Hashtable();
		UsrBean usr = null;
		UserTransaction tx = null;
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransactionPesada();
			Hashtable hashNoColegiadoOriginal = (Hashtable)request.getSession().getAttribute("hashNoColegiadoOriginal");

			CenClienteAdm adminCli=new CenClienteAdm(this.getUserBean(request));
			CenPersonaAdm adminPer=new CenPersonaAdm(this.getUserBean(request));
 			
			// Obtengo los datos del formulario
			DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
			
			// tratamiento del fichero de fotografia
		    String pathImagenes = "";
		    String nombreFoto = "";
		    
		    String tipoOriginal = request.getParameter("tipoOriginal");
		    String tipoComboOriginal = request.getParameter("tipoIdentificacion");
		    String tipoIdentificacionBloqueada = request.getParameter("tipoIdentificacionBloqueada");
		    
		    if(tipoComboOriginal==null || tipoComboOriginal.equals(""))
		    {
		    	tipoComboOriginal = tipoIdentificacionBloqueada;
		    }
		    
		    //No entiendo que significa bloqueada u original.....
		    if(tipoIdentificacionBloqueada==null || tipoIdentificacionBloqueada.equals("")){
		    	tipoIdentificacionBloqueada = tipoComboOriginal;
		    }				    
		    
		    if(miForm.getTipo()!= null && miForm.getTipo().equals("Y"))
		    	tipoOriginal="Y";
		    else if(miForm.getTipo()!= null && miForm.getTipo().equals("J"))
		    	tipoOriginal="J";

		    // Si tipoComboOriginal es 50 significa que el valor del combo seleccionado es Otro
		    // SI tipoComboOriginal es 20 significa que el valor del combo seleccionado es CIF
		    if(tipoComboOriginal.equals("50"))
		    	tipoOriginal="0";
		    else if(!tipoOriginal.equals("Y"))
		    	tipoOriginal="";
		    
		    if(tipoOriginal==null || tipoOriginal.equals(""))
		    {
		    	Hashtable hashNifCif = miForm.getDatos();
		    	tipoOriginal= (String)hashNifCif.get("NIFCIF");
		    
		    	if(tipoOriginal!=null)
		    	{
		    		tipoOriginal=tipoOriginal.substring(0,1);
		    		if(tipoComboOriginal.equals("20") && miForm.getTipoJY().equals("Y"))
		    			tipoOriginal="Y";
		    		request.setAttribute("tipo",tipoOriginal.toUpperCase());
		    	}
		    }
		    else
		    	request.setAttribute("tipo",tipoOriginal);
		    miForm.setTipo(tipoOriginal);
		    
		    // obtencion del path app desde tabla parametros
		    GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request)); 
		    //pathImagenes = paramAdm.getValor(miForm.getIdInstitucion(),ClsConstants.MODULO_CENSO,ClsConstants.PATH_APP, null);
			//pathImagenes += File.separator + ClsConstants.RELATIVE_PATH_FOTOS;
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			pathImagenes = rp.returnProperty("directorios.carpeta.fotos"); 
		    
		    
		    FormFile foto = miForm.getFoto();
		    if(foto==null || foto.getFileSize()<1){
//	    	throw new SIGAException("messages.general.error.ficheroNoExiste");
		    }else{

		    	InputStream stream =null;
		    	nombreFoto = miForm.getFoto().getFileName();
		    	String extension = nombreFoto.substring(nombreFoto.lastIndexOf("."),nombreFoto.length());

		    	// RGG control de la extension
		    	if (extension==null || extension.trim().equals("")
		    			|| (!extension.trim().toUpperCase().equals(".JPG")
					    && !extension.trim().toUpperCase().equals(".GIF")
						&& !extension.trim().toUpperCase().equals(".JPEG"))) {
		    		
		    		throw new SIGAException("messages.error.imagen.tipoNoCorrecto");
		    	}
		    	
		    	nombreFoto = miForm.getNumIdentificacion() + extension;
		    	OutputStream bos = null;
		    	try {			
		    		//retrieve the file data
		    		stream = foto.getInputStream();
		    		//write the file to the file specified
		    		String idInstitucion=miForm.getIdInstitucion();
		    		File camino = new File (pathImagenes + File.separator + idInstitucion);
		    		camino.mkdirs();
		    		bos = new FileOutputStream(pathImagenes + File.separator + idInstitucion +File.separator+nombreFoto );
		    		int bytesRead = 0;
		    		byte[] buffer = new byte[8192];
		    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		    			bos.write(buffer, 0, bytesRead);
		    		}			    
		    	} catch (FileNotFoundException fnfe) {
		    		throw new SIGAException("message.err.error.subiendoarchivo.datosgenerales",fnfe);
		    	} catch (IOException ioe) {
		    		throw new SIGAException("message.err.error.subiendoarchivo.datosgenerales",ioe);
		    	} finally	{
		    		// close the stream
		    		stream.close();
		    		bos.close();
		    	}
		    }

			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = miForm.getDatos();
			hash.put(CenHistoricoBean.C_IDPERSONA, miForm.getIdPersona());
			hash.put(CenHistoricoBean.C_IDINSTITUCION, miForm.getIdInstitucion());						

			// guardo en el hash el path de la imagen para grabar en cliente
			if (nombreFoto!=null && !nombreFoto.equals("")) {
	    		hash.put(CenClienteBean.C_FOTOGRAFIA, nombreFoto);			
			} else {
				hash.remove(CenClienteBean.C_FOTOGRAFIA);
			}

			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			hashOriginal=(Hashtable)request.getSession().getAttribute("DATABACKUP");
			
			// primero compruebo la existencia del nif
			// pero solamente cuando ha cambiado el NIF
			String nifAnterior = (String) hashOriginal.get(CenPersonaBean.C_NIFCIF); 
			nifAnterior=nifAnterior.trim();
			if (nifAnterior!=null && !nifAnterior.toUpperCase().equals(miForm.getNumIdentificacion().toUpperCase())) {
				if (adminPer.existeNif(miForm.getNumIdentificacion(), miForm.getDenominacion(), miForm.getAbreviatura(), "")) {
				      throw new SIGAException("messages.censo.nifcifExiste");
				}
			}
			
			hash.put(CenPersonaBean.C_NIFCIF,miForm.getNumIdentificacion().toUpperCase());
			//Anhado los datos de Denominacion, Abreviatura y Fecha de Constitucion en los campos nombre, Apellidos1 y Fecha Nacimiento:
			hash.put(CenPersonaBean.C_NOMBRE,miForm.getDenominacion());
			if (miForm.getAbreviatura()!=null && !miForm.getAbreviatura().equals("")){
				hash.put(CenPersonaBean.C_APELLIDOS1,miForm.getAbreviatura());
			}else{
				hash.put(CenPersonaBean.C_APELLIDOS1,"#NA");
			}
			
			if ((miForm.getSociedadSP()!=null && miForm.getSociedadSP().equals("1"))||((miForm.getSociedadSJ()!=null && miForm.getSociedadSJ().equals("1")))){
				hash.put(CenClienteBean.C_CARACTER,"S");
			}	
			//hash.put(CenPersonaBean.C_FECHANACIMIENTO,miForm.getFechaConstitucion());
			
			hash.put(CenPersonaBean.C_IDTIPOIDENTIFICACION,tipoIdentificacionBloqueada);
			
			// Cargo una nueva tabla hash para insertar en la tabla de historico
			Hashtable hashHist = new Hashtable();			
			// Cargo los valores obtenidos del formulario referentes al historico			
			hashHist.put(CenHistoricoBean.C_IDPERSONA, miForm.getIdPersona());			
			hashHist.put(CenHistoricoBean.C_IDINSTITUCION, miForm.getIdInstitucion());			
			hashHist.put(CenHistoricoBean.C_MOTIVO, miForm.getMotivo());
			hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_GENERALES).toString());			
						
			// Adecuo formatos
			hash = this.prepararFormatosFechas(hash);
			hash = this.controlFormatosCheck(hash);

			// Comienzo la transaccion:			
			tx.begin();	

		
			// insert de la parte de cliente
			
			//**************************************************************
			// SI EL UPDATE DEVUELVE FALSE ES QUE NO EXISTE REGISTRO EN BBDD
			//
			if (!adminCli.update(hash,hashOriginal)) {
				//LMS 21/08/2006
				//Cambio por el nuevo uso de subLiteral en SIGAException.
				//SIGAException exc=new SIGAException("messages.err.noseque.datosgenerales.de.alguien");
				//exc.setSubLiteral("messages.censo.clientenoexiste.bbdd");
				
				/*SIGAException exc=new SIGAException("messages.censo.clientenoexiste.bbdd");
				throw exc;*/
				throw new ClsExceptions(adminPer.getError());
			}
			
			// update de la parte de persona
			if (!adminPer.update(hash,hashOriginal)) {
				//LMS 21/08/2006
				//Cambio por el nuevo uso de subLiteral en SIGAException.
				//SIGAException exc=new SIGAException("messages.err.noseque.datosgenerales.de.alguien");
				//exc.setSubLiteral("messages.censo.personanoexiste.bbdd");
				
				/*SIGAException exc=new SIGAException("messages.censo.personanoexiste.bbdd");
				throw exc;*/
				throw new ClsExceptions(adminPer.getError());
			}
		
			// insert unico para el historico
			// no hace falta hashHist.put(CenHistoricoBean.C_IDHISTORICO, adminHist.getNuevoID(hash).toString());			
			hashHist.put(CenHistoricoBean.C_FECHAEFECTIVA,"SYSDATE");
			hashHist.put(CenHistoricoBean.C_FECHAENTRADA,"SYSDATE");								
			CenHistoricoAdm admHis = new CenHistoricoAdm (this.getUserBean(request));
			if (!admHis.insert(hashHist)) {
				throw new ClsExceptions(admHis.getError());
			}
			
			// Modifico los datos del no colegiado en CenNoColegiado:
		
			CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(this.getUserBean(request));
			Hashtable hashNoColegiado = new Hashtable();
			hashNoColegiado.put(CenNoColegiadoBean.C_ANOTACIONES,miForm.getAnotaciones());
			hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,miForm.getIdPersona());
			if(miForm.getTipo()!=null) 
				hashNoColegiado.put(CenNoColegiadoBean.C_TIPO,miForm.getTipo());
			
			
			if (miForm.getSociedadSP()!=null && miForm.getSociedadSP().equals("1")){
				GestorContadores gcSP = new GestorContadores(this.getUserBean(request));
//				Obtenemos los datos de la tabla de contadores pasando el sufijo y el prefijo introducidos en el formnulario	
				Hashtable contadorConPrefijoSufijoHashSP=gcSP.getContador(Integer.valueOf(miForm.getIdInstitucion()),ClsConstants.SOCIEDADSP, miForm.getPrefijoNumRegSP(), miForm.getSufijoNumRegSP());
			     //Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)
				Hashtable contadorTablaHashSP=gcSP.getContador(Integer.valueOf(miForm.getIdInstitucion()),ClsConstants.SOCIEDADSP);
				Integer contNumRegSP =new Integer(miForm.getContadorNumRegSP());
				
				String contadorOldSP=UtilidadesHash.getString(hashNoColegiadoOriginal,CenNoColegiadoBean.C_CONTADOR_NUMREGSP);
				
			    if(contadorOldSP==null ||(contadorOldSP!=null && Integer.parseInt(contadorOldSP)!=contNumRegSP.intValue())){
					Integer longitudSP= new Integer((contadorConPrefijoSufijoHashSP.get("LONGITUDCONTADOR").toString()));
					int longitudContadorSP=longitudSP.intValue();
				 if (contadorTablaHashSP.get("MODO").toString().equals("1")){	
					
					
					 boolean yaExisteContadorSP=gcSP.comprobarUnicidadContador(contNumRegSP.intValue(),contadorConPrefijoSufijoHashSP);
					
					  if (!yaExisteContadorSP){
					  	hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREGSP,miForm.getPrefijoNumRegSP());
					  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREGSP,miForm.getSufijoNumRegSP());
					  	String contadorFinalSP=UtilidadesString.formatea(contNumRegSP,longitudContadorSP,true);
					  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREGSP,contadorFinalSP);
					  	hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSP,ClsConstants.DB_TRUE);
					  	//Actualizamos la tabla Adm_contador
					  	gcSP.setContador(contadorConPrefijoSufijoHashSP, String.valueOf(Integer.parseInt(miForm.getContadorNumRegSP())+1));
					  	
					  }else{
					  	// Se sugiere un valor, siendo este el siguiente numero de registro libre a partir del ultimo numero de contador
					  	// utilizado
					  	
					  	int contadorNuevoSP=gcSP.getSiguienteNumContador(contadorConPrefijoSufijoHashSP);
					  	Integer contadorSugeridoSP=new Integer(contadorNuevoSP);
					  	
					  	String contadorFinalSugeridoSP=UtilidadesString.formatea(contadorSugeridoSP,longitudContadorSP,true);
					  	
					  	miForm.setContadorNumRegSP(contadorFinalSugeridoSP);
					  	request.setAttribute("error","ERROR"); 
					  	request.setAttribute("contadorSugeridoSP",contadorFinalSugeridoSP);
					  	request.setAttribute("mensaje","messages.contador.error.existeContador");
					  	//throw new SIGAException ("messages.contador.error.existeContador");
					  	tx.rollback();
					  	return "exitoInsercion";	
					  }	
					}
				
				else{//Modo Registro (0), directamente se sugiere y se inserta al grabar un numero de contador
					
					int contadorNuevoSP=gcSP.getSiguienteNumContador(contadorTablaHashSP);
				  	Integer contadorSugeridoSP=new Integer(contadorNuevoSP);
				  	
				  	String contadorFinalSugeridoSP=UtilidadesString.formatea(contadorSugeridoSP,longitudContadorSP,true);
				  	
				  	miForm.setContadorNumRegSP(contadorFinalSugeridoSP);
				  	hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREGSP,(String)contadorTablaHashSP.get("PREFIJO"));
				  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREGSP,(String)contadorTablaHashSP.get("SUFIJO"));
				  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREGSP,contadorFinalSugeridoSP);
				  	hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSP,ClsConstants.DB_TRUE);
				  
				  	gcSP.setContador(contadorTablaHashSP,String.valueOf(Integer.parseInt(contadorFinalSugeridoSP)+1));
				}
			}else{
				hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREGSP,miForm.getPrefijoNumRegSP());
			  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREGSP,miForm.getSufijoNumRegSP());
			  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREGSP,miForm.getContadorNumRegSP());
			  	hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSP,ClsConstants.DB_TRUE);
				
			}
			}
			
			
			if (miForm.getSociedadSJ()!=null && miForm.getSociedadSJ().equals("1")){
				GestorContadores gcSJ = new GestorContadores(this.getUserBean(request));
				//Obtenemos los datos de la tabla de contadores pasando el sufijo y el prefijo introducidos en el formnulario	
					Hashtable contadorConPrefijoSufijoHashSJ=gcSJ.getContador(Integer.valueOf(miForm.getIdInstitucion()),ClsConstants.SOCIEDADSJ, miForm.getPrefijoNumRegSJ(), miForm.getSufijoNumRegSJ());
				//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
					Hashtable contadorTablaHashSJ=gcSJ.getContador(Integer.valueOf(miForm.getIdInstitucion()),ClsConstants.SOCIEDADSJ);
					
					Integer longitudSJ= new Integer((contadorConPrefijoSufijoHashSJ.get("LONGITUDCONTADOR").toString()));
					int longitudContadorSJ=longitudSJ.intValue();
					Integer contNumRegSJ =new Integer(miForm.getContadorNumRegSJ());
					String contadorOldSJ=UtilidadesHash.getString(hashNoColegiadoOriginal,CenNoColegiadoBean.C_CONTADOR_NUMREG);
					
			  if(contadorOldSJ==null ||(contadorOldSJ!=null && Integer.parseInt(contadorOldSJ)!=contNumRegSJ.intValue())){
				
				if (contadorTablaHashSJ.get("MODO").toString().equals("1")){	
					
					
					boolean yaExisteContadorSJ=gcSJ.comprobarUnicidadContador(contNumRegSJ.intValue(),contadorConPrefijoSufijoHashSJ);
					
					  if (!yaExisteContadorSJ){
					  	hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREG,miForm.getPrefijoNumRegSJ());
					  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREG,miForm.getSufijoNumRegSJ());
					  	String contadorFinalSJ=UtilidadesString.formatea(contNumRegSJ,longitudContadorSJ,true);
					  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREG,contadorFinalSJ);
					  	//Actualizamos la tabla Adm_contador
					  	gcSJ.setContador(contadorConPrefijoSufijoHashSJ, String.valueOf(Integer.parseInt(miForm.getContadorNumRegSJ())+1));
					  	hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,ClsConstants.DB_TRUE);
					  	
					  }else{
					  	// Se sugiere un valor, siendo este el siguiente numero de registro libre a partir del ultimo numero de contador
					  	// utilizado
					  	
					  	int contadorNuevoSJ=gcSJ.getSiguienteNumContador(contadorConPrefijoSufijoHashSJ);
					  	Integer contadorSugeridoSJ=new Integer(contadorNuevoSJ);
					  	
					  	String contadorFinalSugeridoSJ=UtilidadesString.formatea(contadorSugeridoSJ,longitudContadorSJ,true);
					  	
					  	miForm.setContadorNumRegSJ(contadorFinalSugeridoSJ);
					  	request.setAttribute("error","ERROR"); 
					  	request.setAttribute("contadorSugeridoSJ",contadorFinalSugeridoSJ);
					  	request.setAttribute("mensaje","messages.contador.error.existeContador");
					  	//throw new SIGAException ("messages.contador.error.existeContador");
					  	tx.rollback();
					  	return "exitoInsercion";	
					  }		
				}
				else{//Modo Registro (0), directamente se sugiere y se inserta al grabar un numero de contador
					
					int contadorNuevoSJ=gcSJ.getSiguienteNumContador(contadorTablaHashSJ);
				  	Integer contadorSugeridoSJ=new Integer(contadorNuevoSJ);
				  	
				  	String contadorFinalSugeridoSJ=UtilidadesString.formatea(contadorSugeridoSJ,longitudContadorSJ,true);
				  	
				  	miForm.setContadorNumRegSJ(contadorFinalSugeridoSJ);
				  	hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREG,(String)contadorTablaHashSJ.get("PREFIJO"));
				  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREG,(String)contadorTablaHashSJ.get("SUFIJO"));
				  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREG,contadorFinalSugeridoSJ);
				  	hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,ClsConstants.DB_TRUE);
				  
				  	gcSJ.setContador(contadorTablaHashSJ,String.valueOf(Integer.parseInt(contadorFinalSugeridoSJ)+1));
				}
			   }else{
				hashNoColegiado.put(CenNoColegiadoBean.C_PREFIJO_NUMREG,miForm.getPrefijoNumRegSJ());
			  	hashNoColegiado.put(CenNoColegiadoBean.C_SUFIJO_NUMREG,miForm.getSufijoNumRegSJ());
			  	hashNoColegiado.put(CenNoColegiadoBean.C_CONTADOR_NUMREG,miForm.getContadorNumRegSJ());
			  	hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,ClsConstants.DB_TRUE);
			   }
			}
		
	
			
			admNoColegiado.update(hashNoColegiado,hashNoColegiadoOriginal);

			
			// Lanzamos el proceso de revision de suscripciones del letrado 
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(miForm.getIdInstitucion(),
																					  miForm.getIdPersona(),
																					  "",
																					  ""+this.getUserName(request));
			if ((resultado == null) || (!resultado[0].equals("0")))
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");

			
			// Fin de la transaccion:
			tx.commit();
			
			//Mandamos los datos para refrescar:
			request.setAttribute("mensaje","messages.updated.success");
			request.setAttribute("idPersona",miForm.getIdPersona());
			request.setAttribute("idInstitucion",miForm.getIdInstitucion());
			request.setAttribute("tipo",miForm.getTipo());
			request.setAttribute("error","OK");
	   } catch (SIGAException e) {
		 	throw e;
	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
   	   return "exitoInsercion";
	}
	
	protected String designarPorArticulo27 (ActionMapping mapping,MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions, SIGAException {
				
		String forward="designarArt27", accionPestanha=null;
		UsrBean user = null;
		
		try {
			DatosGeneralesForm miform = (DatosGeneralesForm)formulario;
			miform.reset(mapping,request);
						
			CenTipoDireccionAdm cenTipoDirAdm = new CenTipoDireccionAdm (this.getUserBean(request));
			Vector vTipos = new Vector();
			vTipos=cenTipoDirAdm.select("");
			Hashtable hTipoDir=new Hashtable();
			Hashtable hTipoDirSel=new Hashtable();
			if ( (vTipos != null) && (vTipos.size() > 0) )
				for (int i = 1; i <= vTipos.size(); i++) {
					CenTipoDireccionBean tipoDir = (CenTipoDireccionBean) vTipos.get(i-1);
					hTipoDir.put(tipoDir.getIdTipoDireccion(),"N");
				}
			request.setAttribute("vTipos",vTipos);

			// cargo los valores recibidos por paramtros en el FORM
			accionPestanha = "nuevo";
			//miform.setIdInstitucion(new Integer(request.getParameter("idInstitucion")));			
			//miform.setIdPersona(new Long(request.getParameter("idPersona")));			
			miform.setAccion(accionPestanha);
			miform.setModo(accionPestanha);
			//Para saber si debemos cargar en la pestanha el jsp de colegiados/personal o el de no colegiados de Sociedad SJ:
			String tipo = request.getParameter("tipo");
			ArrayList list = new ArrayList();
			DireccionesForm dirForm = new DireccionesForm();
			dirForm.setNombre("-- Nueva");
			dirForm.setIdDireccion(new Long(-1));
			list.add(dirForm);			
			request.setAttribute("modoPestanha",accionPestanha);

			miform.setDirecciones(list);
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			forward = "designarArt27";

	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		return forward;
		
	}		
	
	@SuppressWarnings("unchecked")
	protected void getAjaxDirecciones (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		BusquedaCensoForm miForm = (BusquedaCensoForm)formulario;
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		//Recogemos el parametro enviado por ajax
		String idInstitucion = request.getParameter("idInstitucion");
		String idPersona = request.getParameter("idPersona");
		String idDireccion = request.getParameter("idDireccion");
		
		if(idDireccion.equals("-1")){
			miForm.setFax1("");
			miForm.setFax2("");
			miForm.setMail("");
			miForm.setPaginaWeb("");
			miForm.setMovil("");
			miForm.setTelefono("");
			miForm.setTelefono2("");
			miForm.setPoblacion("");
			miForm.setProvincia("");
			miForm.setPais("");
			miForm.setDireccion("");
			miForm.setCodPostal("");
			
		}else{
			CenDireccionesAdm dirAdm = new CenDireccionesAdm(user);
			Hashtable direccion = dirAdm.selectDirecciones(new Long(idPersona), new Integer(idInstitucion), new Long(idDireccion));
			
			miForm.setFax1((String)direccion.get("FAX1"));
			miForm.setFax2((String)direccion.get("FAX2"));
			miForm.setMail((String)direccion.get("CORREOELECTRONICO"));
			miForm.setPaginaWeb((String)direccion.get("PAGINAWEB"));
			miForm.setMovil((String)direccion.get("MOVIL"));
			miForm.setTelefono((String)direccion.get("TELEFONO1"));
			miForm.setTelefono2((String)direccion.get("TELEFONO2"));
			miForm.setPoblacion((String)direccion.get("IDPOBLACION"));
			miForm.setProvincia((String)direccion.get("IDPROVINCIA"));
			miForm.setPais((String)direccion.get("IDPAIS"));
			miForm.setDireccion((String)direccion.get("DOMICILIO"));
			miForm.setCodPostal((String)direccion.get("CODIGOPOSTAL"));
		}

		List listaParametros = new ArrayList();
		listaParametros.add(miForm.getFax1());
		listaParametros.add(miForm.getFax2());
		listaParametros.add(miForm.getMail());
		listaParametros.add(miForm.getPaginaWeb());
		listaParametros.add(miForm.getMovil());
		listaParametros.add(miForm.getTelefono());
		listaParametros.add(miForm.getTelefono2());
		listaParametros.add(miForm.getPoblacion());
		listaParametros.add(miForm.getProvincia());
		listaParametros.add(miForm.getPais());
		listaParametros.add(miForm.getDireccion());
		listaParametros.add(miForm.getCodPostal());
		
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response);

	}	
	
	@SuppressWarnings("unchecked")
	protected void getAjaxComboDirecciones (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		DatosGeneralesForm miForm = (DatosGeneralesForm)formulario;
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		//Recogemos el parametro enviado por ajax
		String idInstitucion = request.getParameter("idInstitucion");
		String idPersona = request.getParameter("idPersona");
		
		List alDirecciones = null;
		
		if((idPersona!= null && !idPersona.equals("")) && (idInstitucion != null && !idInstitucion.equals(""))){
			CenDireccionesAdm dirAdm = new CenDireccionesAdm(user);
			alDirecciones = dirAdm.geDireccionesLetrado(idPersona, idInstitucion);
		}else{
			alDirecciones = new ArrayList<DireccionesForm>();
			DireccionesForm dirForm = new DireccionesForm();
			dirForm.setNombre("-- Nueva");
			dirForm.setIdDireccion(new Long(-1));
			alDirecciones.add(dirForm);		
		}
		
		respuestaAjax(new AjaxCollectionXmlBuilder(), alDirecciones,response);

	}	
	
	protected String buscarTodosArt27Modal(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		
		
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		// casting del formulario
		DatosGeneralesForm miFormulario = (DatosGeneralesForm)formulario;
		String idInstitucion = miFormulario.getColegiadoen();	
		// busqueda de clientes
		VleLetradosSigaAdm cliente = new VleLetradosSigaAdm(this.getUserBean(request));
		
		if (request.getParameter("busquedaSancion")!=null && request.getParameter("busquedaSancion").equals("1")){
			request.setAttribute("busquedaSancion","1");
			
		}
		
		request.setAttribute(ClsConstants.PARAM_PAGINACION,paginadorModal);
		request.setAttribute("si",UtilidadesString.getMensajeIdioma(user, "general.yes"));
		request.setAttribute("no",UtilidadesString.getMensajeIdioma(user, "general.no"));
	
		
		
		try {
			HashMap databackup=getPaginador(request, paginadorModal);
			if (databackup!=null){ 

				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					Vector datos=new Vector();
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					 
					request.setAttribute("letradoList", datos);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					databackup.put("paginador",paginador);
					databackup.put("datos",datos);
				}else{
					request.setAttribute("letradoList", new Vector());
					databackup.put("datos",new Vector());
					
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					setPaginador(request, paginadorModal, databackup);
					
				}	
				

			}else{	
				databackup=new HashMap();
				//Haria falta meter los parametros en con ClsConstants

				PaginadorBind paginador = cliente.getClientesCensoArticulo27 (idInstitucion,user.getLocation(),null, user.getLanguage());
				
				
				if (paginador!=null&& paginador.getNumeroTotalRegistros()>0){
					int totalRegistros = paginador.getNumeroTotalRegistros();
					databackup.put("paginador",paginador);
					Vector datos = paginador.obtenerPagina(1);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					request.setAttribute("letradoList", datos);
					databackup.put("datos",datos);
					
					setPaginador(request, paginadorModal, databackup);
				}else{
					databackup.put("datos",new Vector());
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					request.setAttribute("letradoList", new Vector());
					setPaginador(request, paginadorModal, databackup);
					
				} 	


			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "resultadoCenso";
	}

	/** 
	 * Funcion que atiende la accion solicitarModificacion
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String solicitarModificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String modo = "";
		UserTransaction t = null;
		modo = "insertarModificacion";
		
		try	{
			DatosGeneralesForm form = (DatosGeneralesForm) formulario;
			String accion = (String)request.getParameter("accion");				
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			CenSolicModifExportarFotoAdm adm = new CenSolicModifExportarFotoAdm(this.getUserBean(request));
			CenSolicModifExportarFotoBean bean = getDatos(form, request);
			t = this.getUserBean(request).getTransaction();			
			t.begin();	
			
			if(!adm.insert(bean)){
				throw new SIGAException (adm.getError());
			}
			
			t.commit();
			modo = exitoModal("messages.censo.solicitudes.exito",request);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, t);
		}

		return modo;
	} //solicitarModificacion()	
	
	
	/**
	 * @param form
	 * @param request
	 * @return
	 * @throws ClsExceptions
	 */
	protected CenSolicModifExportarFotoBean getDatos (DatosGeneralesForm form, HttpServletRequest request) throws SIGAException {
		CenSolicModifExportarFotoBean bean = null;
		
		try	{			
			bean = new CenSolicModifExportarFotoBean();
			CenSolicModifExportarFotoAdm adm = new CenSolicModifExportarFotoAdm(this.getUserBean(request));
			bean.setIdSolicitud(adm.getNuevoId());
			bean.setIdInstitucion(new Integer(form.getIdInstitucion()));
			if(form.getExportarFoto() != null){
				bean.setExportarFoto(form.getExportarFoto());
			}else{
				bean.setExportarFoto("0");
			}
			bean.setIdPersona(new Long(form.getIdPersona()));
			if(form.getMotivo()!=null && !form.getMotivo().equals("")){
				bean.setMotivo(form.getMotivo());
			}else{
				bean.setMotivo("Solicitud de modificación de la exportación de la foto");
			}
			bean.setIdEstadoSolic(new Integer(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));
			bean.setFechaAlta("sysdate");
		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return bean;
	} //getDatos()

} //modificarSociedad ()
