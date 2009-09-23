package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsHitoFacturableGuardiaAdm;
import com.siga.beans.ScsHitoFacturableGuardiaBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirHitosFacturablesGuardiasForm;


/**
 * @author ruben.fernandez
 * Modificado por david.sanchezp  13-05-2005 para poder modificar el hito con un combo.
 * Modificado por david.sanchezp  30-06-2005 controlar SIGA Exception y refrescar con metodo exito.
 */

public class DefinirHitosPagosGuardiasAction extends MasterAction {
	
	/** Funcion abrir
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
				throws SIGAException
	{	
		try {
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean)request.getSession().getAttribute("DATABACKUPPESTANA");  //recuperar la guardia que está seleccionada desde la sesion
			Hashtable hash = (Hashtable)guardia.getOriginalHash();
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsHitoFacturableGuardiaAdm hFact = new ScsHitoFacturableGuardiaAdm (this.getUserBean(request));
			String consulta= " select "+UtilidadesMultidioma.getCampoMultidioma("h.descripcion",this.getUserBean(request).getLanguage()) + " , hg.preciohito, hg.puntos, hg.idinstitucion IDINSTITUCION, hg.idturno IDTURNO, hg.idguardia IDGUARDIA, hg.idhito IDHITO, hg.pagoofacturacion PAGOFACTURACION"+
							 " from  scs_hitofacturable h,"+
							 " scs_hitofacturableguardia hg"+
							 " where h.idhito = hg.idhito"+						 
							 " and hg.idinstitucion = "+usr.getLocation()+
							 " and hg.idturno ="+(String)hash.get("IDTURNO")+
							 " and hg.idguardia ="+(String)hash.get("IDGUARDIA")+
							 " and hg.pagoofacturacion = 'P'";
			Vector vHitos = (Vector)hFact.ejecutaSelect(consulta);
			request.getSession().setAttribute("pagos","si");
			request.getSession().setAttribute("vHitos",vHitos);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		return "abrir";
	}
    

	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws SIGAException {

		try {
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean)request.getSession().getAttribute("DATABACKUPPESTANA");  //recuperar la guardia que está seleccionada desde la sesion
			Vector vOcultos = (Vector)formulario.getDatosTablaOcultos(0); 
			Hashtable hashGuardia = (Hashtable)guardia.getOriginalHash();
			DefinirHitosFacturablesGuardiasForm miForm = (DefinirHitosFacturablesGuardiasForm) formulario;
			Hashtable hash = new Hashtable();
			
			hash.put("IDINSTITUCION",(String)usr.getLocation());
			hash.put("IDTURNO",(String)hashGuardia.get("IDTURNO"));
			hash.put("IDGUARDIA",(String)hashGuardia.get("IDGUARDIA"));
			hash.put("IDHITO",(String)vOcultos.get(0)); //el IDHITO
			hash.put("PAGOOFACTURACION","P");
			ScsHitoFacturableGuardiaAdm hitoAdm = new ScsHitoFacturableGuardiaAdm (this.getUserBean(request));
			Vector vElegido = hitoAdm.select(hash);
			ScsHitoFacturableGuardiaBean hito = (ScsHitoFacturableGuardiaBean)vElegido.get(0);
			request.getSession().setAttribute("HITO",hito);
			request.setAttribute("idHito",(String)vOcultos.get(0));
			request.setAttribute("nombreHito",((Vector)miForm.getDatosTablaVisibles(0)).get(0));
			request.setAttribute("modo","EDITAR");
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		return "editar";
	}

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    String forward = this.editar(mapping, formulario, request, response);
	    request.setAttribute("modo","VER");
	    return forward;
	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions {
		return "nuevo";
	}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws SIGAException {
		
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsHitoFacturableGuardiaAdm hitoAdm = new ScsHitoFacturableGuardiaAdm(this.getUserBean(request));
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean)request.getSession().getAttribute("DATABACKUPPESTANA");  //recuperar la guardia que está seleccionada desde la sesion
			UserTransaction tx =null;

			try {
				tx = usr.getTransaction();
				tx.begin();
				Hashtable hash = (Hashtable)formulario.getDatos();
				hash.put("USUMODIFICACION",usr.getUserName());
				hash.put("FECHAMODIFICACION","SYSDATE");
				hash.put("IDINSTITUCION",usr.getLocation());
				hash.put("IDTURNO",guardia.getIdTurno().toString());
				hash.put("IDGUARDIA",guardia.getIdGuardia().toString());
				hash.put("PAGOOFACTURACION","P");
			
				try {
					hitoAdm.insert(hash);
					tx.commit();
				} catch (Exception e){
					return exitoModalSinRefresco("messages.inserted.error",request);
				}
			} catch(Exception e){
				throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
			}
			return exitoModal("messages.inserted.success",request);	
	}

	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @throws SIGAException
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		DefinirHitosFacturablesGuardiasForm miForm = (DefinirHitosFacturablesGuardiasForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsHitoFacturableGuardiaAdm hitoAdm = new ScsHitoFacturableGuardiaAdm(this.getUserBean(request));
		ScsHitoFacturableGuardiaBean elegido;
		Hashtable hash = new Hashtable();
		UserTransaction tx =null;
		
		try {
			elegido = (ScsHitoFacturableGuardiaBean)request.getSession().getAttribute("HITO");
			hash = (Hashtable)miForm.getDatos();
			
			hash.put("PAGOOFACTURACION","P");
			hash.put("IDINSTITUCION",usr.getLocation());
			hash.put("IDGUARDIA",elegido.getIdGuardia());
			hash.put("IDTURNO",elegido.getIdTurno());
			hash.put("PUNTOS",elegido.getPuntos());
			hash.put("FECHAMODIFICACION","SYSDATE");
			hash.put("USUMODIFICACION",usr.getUserName());
			
			tx = usr.getTransaction();
			tx.begin();
			//Borro el antiguo registro:
			hitoAdm.delete(elegido.getOriginalHash());
			//Inserto el nuevo:
			hitoAdm.insert(hash);
			tx.commit();
		} catch (Exception e) {
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
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws SIGAException {
			
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsHitoFacturableGuardiaAdm hitoAdm = new ScsHitoFacturableGuardiaAdm(this.getUserBean(request));
		ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean)request.getSession().getAttribute("DATABACKUPPESTANA");  //recuperar la guardia que está seleccionada desde la sesion
		Hashtable hash = new Hashtable();
		
		try{
			hash.put("IDINSTITUCION",usr.getLocation());
			hash.put("IDTURNO",guardia.getIdTurno().toString());
			hash.put("IDGUARDIA",guardia.getIdGuardia().toString());
			hash.put("IDHITO",((Vector)formulario.getDatosTablaOcultos(0)).get(0));
			hash.put("PAGOOFACTURACION","P");

			try {
				hitoAdm.delete(hash);
			} catch (Exception e){
				return exito("messages.deleted.error",request);
	        }	        
		} catch(Exception e){
			throwExcp("messages.deleted.error",e,null);
		}
		return exitoRefresco("messages.deleted.success",request);
	}


}