//Clase: DefinirRatificacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005

package com.siga.gratuita.action;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.*;
import com.siga.ws.CajgConfiguracion;
import com.atos.utils.*;

import org.apache.struts.action.*;

import java.util.*;

import com.siga.beans.*;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirDictamenEJGAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null)
			try {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}catch(Exception e){
				return mapping.findForward("exception");
			}
		else return super.executeInternal(mapping, formulario, request, response); 
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
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
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
			// Actualizamos en la base de datos
			tx=usr.getTransaction();
			tx.begin();
			Hashtable old = (Hashtable)request.getSession().getAttribute("DATABACKUPDICT");
			admEJG.update(nuevos,old);
			tx.commit();
			// En DATABACKUP almacenamos los datos más recientes por si se vuelve a actualizar seguidamente
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
		
		/* "DATABACKUP" se usa habitualmente así que en primer lugar borramos esta variable */		
		//request.getSession().removeAttribute("DATABACKUPDICT");
		
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");		
		DefinirDictamenEJGForm miForm = (DefinirDictamenEJGForm)formulario;
		if(request.getParameter("ANIO")!=null){
			miHash.put("ANIO",request.getParameter("ANIO").toString());
			miHash.put("NUMERO",request.getParameter("NUMERO").toString());
			miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
			miHash.put("IDINSTITUCION",usr.getLocation().toString());
		}else{
			miHash.put("ANIO",miForm.getAnio());
			miHash.put("NUMERO",miForm.getNumero());
			miHash.put("IDTIPOEJG",miForm.getIdTipoEJG());
			miHash.put("IDINSTITUCION",miForm.getIdInstitucion());
			
		}
		
			
		
		ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
		
		try {			
			v = admEJG.selectPorClave(miHash);
			try{
				
				ScsEJGBean ejgBean= (ScsEJGBean)v.get(0);
				
				request.getSession().setAttribute("DATABACKUPDICT",admEJG.beanToHashTable(ejgBean));
				int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
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
				
				Vector informeBeans=adm.obtenerInformesTipo(usr.getLocation().toString(),"EJGCA",null, null);
				if(informeBeans!=null && informeBeans.size()>1){
					informeUnico = ClsConstants.DB_FALSE;
					
				}
					
					
				
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
	 * Funcion que nos dirá si se puede borrar el dictamen o no
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
			// Recuperamos los estados del EJG con un método genérico
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
				// el dictamen no se podrá borrar
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