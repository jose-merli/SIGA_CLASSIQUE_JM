//Clase: DefinirRatificacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificaci�n: 17/02/2005

package com.siga.gratuita.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.services.gen.SelectDataService;
import org.redabogacia.sigaservices.app.util.KeyValue;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEstadoEJGAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirDictamenEJGForm;
import com.siga.ws.CajgConfiguracion;

import es.satec.businessManager.BusinessManager;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirDictamenEJGAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = (MasterForm) formulario;
		String modo = (String) request.getParameter("modo");
		if (miForm == null){
			try {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}catch(Exception e){
				return mapping.findForward("exception");
			}
		}
		else if (modo != null && modo.equalsIgnoreCase("getFundamentosDictamen")){
			try {
				getFundamentosDictamen(mapping, formulario, request, response);
			} catch (Exception e) {
				throw new SIGAException(e);
			}
			return null;
		} else
			return super.executeInternal(mapping, formulario, request, response);
		
	}
	
	private void getFundamentosDictamen(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws JSONException, IOException {
		//Declaraci�n de atributos
				HashMap<String, String> params = new HashMap<String, String>();
				JSONObject objetoJSON = new JSONObject();
				JSONArray listaArrayJSON = new JSONArray();
		    	
				//Obtenemos el usuario de sesi�n
				UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
				//Recogemos el parametro enviado por ajax
				String idTipoDictamenEJG = request.getParameter("idTipoDictamenEJG");
				
				//Introducimos los parametros necesarios para la query
				params.put("idioma", user.getLanguage());
				params.put("idtipodictamenejg", idTipoDictamenEJG);
				params.put("idinstitucion", user.getLocation());
				
				//Llamada al servicio
				SelectDataService service = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
				List<KeyValue> tratamientos = service.getFundamentosCalificacion(params);
				
				//Obtenemos los datos y lo convertimos en objeto json
				for(int i=0;i<tratamientos.size();i++){
					KeyValue tratamientoAux = (KeyValue) tratamientos.get(i);
					objetoJSON.put("id",tratamientoAux.getKey());
					objetoJSON.put("descripcion",tratamientoAux.getValue());
				
					listaArrayJSON.put(objetoJSON);
					objetoJSON = new JSONObject();
					
				}
				
				 response.setHeader("Cache-Control", "no-cache");
				 response.setHeader("Content-Type", "application/json;charset=utf-8"); 
			     response.setHeader("X-JSON", listaArrayJSON.toString());
				 response.getWriter().write(listaArrayJSON.toString());
	}

	/**
	 * No implementado 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		return null;
	}

	/**
	 * No implementado 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		return null;
	
	}

	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		

		return null;
	}

	/**
	 * No implementado 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}

	/**
	 * No implementado 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		Vector v = new Vector ();
		Hashtable nuevos = new Hashtable();		
		UserTransaction tx=null;
		
		try {
			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirDictamenEJGForm miForm = (DefinirDictamenEJGForm)formulario;		
			nuevos = miForm.getDatos();			
			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
			
			//Se realiza el nuevo parseo de IDTIPODICTAMENEJG
			if (!nuevos.get("IDTIPODICTAMENEJG").equals("")) {
				// Ponemos el IDTIPODICTAMENEJG en el formato correcto
				String[] tipoDictamenEJG = nuevos.get("IDTIPODICTAMENEJG").toString().split(",");
				nuevos.put("IDTIPODICTAMENEJG", tipoDictamenEJG[0] );
			}
		
			if (!nuevos.get("FECHADICTAMEN").equals("")) {
				// Ponemos la fecha en el formato correcto
				nuevos.put("FECHADICTAMEN", GstDate.getApplicationFormatDate("",nuevos.get("FECHADICTAMEN").toString()));
			} else {
				// Ponemos la fecha en el formato correcto
				nuevos.put("FECHADICTAMEN", nuevos.get("FECHADICTAMEN").toString());
			}
			
			Hashtable old = (Hashtable)request.getSession().getAttribute("DATABACKUPDICT");

			// Actualizamos en la base de datos
			admEJG.update(nuevos,old);
			
			// En DATABACKUP almacenamos los datos m�s recientes por si se vuelve a actualizar seguidamente
			nuevos.put("FECHAMODIFICACION", "sysdate");
			request.getSession().setAttribute("DATABACKUPDICT",nuevos);			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		return exitoRefresco("messages.updated.success",request);
	}

	/**
	 * No implementado 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
  
		return null;
	}

	/** 
	 * No implementado
	 */	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		return null;		
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		/* "DATABACKUP" se usa habitualmente as� que en primer lugar borramos esta variable */		
		//request.getSession().removeAttribute("DATABACKUPDICT");
		
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");		
		DefinirDictamenEJGForm miForm = (DefinirDictamenEJGForm)formulario;
		String idInstitucion = null;
		if(request.getParameter("ANIO")!=null){
			miHash.put("ANIO",request.getParameter("ANIO").toString());
			miHash.put("NUMERO",request.getParameter("NUMERO").toString());
			miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
			idInstitucion = request.getParameter("IDINSTITUCION").toString();
			miHash.put("IDINSTITUCION",idInstitucion);
		}else{
			miHash.put("ANIO",miForm.getAnio());
			miHash.put("NUMERO",miForm.getNumero());
			miHash.put("IDTIPOEJG",miForm.getIdTipoEJG());
			idInstitucion = miForm.getIdInstitucion();
			miHash.put("IDINSTITUCION",idInstitucion);
			
		}
		
		
		
		ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
		
		try {			
			v = admEJG.selectPorClave(miHash);
			try{
				
				ScsEJGBean ejgBean= (ScsEJGBean)v.get(0);
				
				request.getSession().setAttribute("DATABACKUPDICT",admEJG.beanToHashTable(ejgBean));
				int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(idInstitucion));
				request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
				String informeUnico = ClsConstants.DB_TRUE;
				
				// jbd // inc10949 
				/* Pasamos un parametro para decir si se puede borrar el dictamen o no
				 * Esto depende si tenemos un estado posterior a dictaminado que petenezca 
				 * a la comision.
				 */			
				boolean borrable = ejgBean.getFechaDictamen()!=null && !ejgBean.getFechaDictamen().equalsIgnoreCase("") && isDictamenBorrable(ejgBean, usr);
				request.setAttribute("isBorrable", borrable);
				
				AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
				// mostramos la ventana con la pregunta
				
				Vector informeBeans=adm.obtenerInformesTipo(idInstitucion,"EJGCA",null, null);
				if(informeBeans!=null && informeBeans.size()>1){
					informeUnico = ClsConstants.DB_FALSE;
					
				}
					
//				ScsTipoDictamenEjgService scsTipoDictamenEjgService  = (ScsTipoDictamenEjgService)BusinessManager.getInstance().getService(ScsTipoDictamenEjgService.class);
//				List<ScsTipodictamenejg> listTipodictamenejgs = scsTipoDictamenEjgService.getList(usr.getLanguage(), Short.valueOf(idInstitucion));
//				if(listTipodictamenejgs==null)
//					listTipodictamenejgs = new ArrayList<ScsTipodictamenejg>();
//				request.setAttribute("dictamenEjgList", listTipodictamenejgs);
				
				SelectDataService service = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
				HashMap<String, String> params = new HashMap<String, String>();
				
				//Obtenemos el usuario de sesi�n
				UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
				//Introducimos los parametros necesarios para la query
				params.put("idioma", user.getLanguage());
				params.put("idinstitucion", user.getLocation());
				
				params.put("idtipodictamenejg", ejgBean.getIdTipoDictamenEJG()!=null?""+ejgBean.getIdTipoDictamenEJG():"-1");
				
				List<KeyValue> listTipodictamenejgs = service.getTiposDictamenEjgActivos(params);
				request.setAttribute("dictamenEjgList", listTipodictamenejgs);
				
				GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
				String prefijoExpedienteCajg = paramAdm.getValor (idInstitucion, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
				request.setAttribute("PREFIJOEXPEDIENTECAJG",prefijoExpedienteCajg);
				request.setAttribute("informeUnico", informeUnico);
			}catch (Exception e) {
				throwExcp("error.general.yanoexiste",e,null);
			}
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		
		return "inicio";		
	}
	
	/**
	 * Funcion que nos dir� si se puede borrar el dictamen o no
	 * Un dictamen se puede borrar siempre y cuando no exista un estado visible por la comision
	 * que se haya dado de alta con posterioridad al dictamen.
	 * @param ejg El ejg cuyos estados vamos a comprobar
	 * @param usr El usuario para poder crear el adm
	 * @return true si se puede borrar, false si no se puede borrar el dictamen
	 * @throws SIGAException 
	 */
	private boolean isDictamenBorrable(ScsEJGBean ejg, UsrBean usr) throws SIGAException{
		ScsEstadoEJGAdm estadoAdm = new ScsEstadoEJGAdm(usr);
		boolean borrable = true;
		try {
			// Recuperamos los estados del EJG con un m�todo gen�rico
			Vector<Hashtable> estados = estadoAdm.getEstadosEjg(ejg);
			Hashtable<String, String> e = new Hashtable<String, String>();
			// Inicializamos la posicion del estado dictaminado como el ultimo
			int dictaminado = estados.size();
			for (int i=0;i<estados.size();i++) {
				e=estados.get(i);
				// Cuando encontremos un estado dictaminado lo fijamos como referencia
				if(e.get("IDESTADOEJG").toString().equalsIgnoreCase("6"))
					dictaminado=i;
				// Si no encontramos un estado visible por la comision y posterior al dictamen
				// el dictamen no se podr� borrar
				if(e.get("VISIBLECOMISION").toString().equalsIgnoreCase("1") && (i>=dictaminado))
					borrable=false;
			}
			return borrable;
		} catch (ClsExceptions e) {
			// Si nos encontramos con un problema no dejamos borrar
			return false;
		}
	}
	
	/** 
	 * No implementado
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}
}