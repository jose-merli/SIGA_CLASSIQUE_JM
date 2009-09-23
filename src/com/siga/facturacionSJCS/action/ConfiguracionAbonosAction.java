/**
 * VERSIONES:
 * 
 * jose.barrientos - 26-02-2008 - Inicio
 */
package com.siga.facturacionSJCS.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.FacBancoInstitucionAdm;
import com.siga.beans.FcsEstadosPagosBean;
import com.siga.beans.FcsPagosEstadosPagosBean;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacionSJCS.form.ConfiguracionAbonosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action para Descargar los ficheros bancarios.<br/>
 * Gestiona abrir y descargar Ficheros
 * @version david.sanchezp: cambios para pedir y tener en cuenta la fecha de cargo.
 */
public class ConfiguracionAbonosAction extends MasterAction{
	
	/**  
     * Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
			
			String accion = miForm.getModo();
			
			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);	
			} else if (accion.equalsIgnoreCase("modificar")){
				mapDestino = modificar(mapping, miForm, request, response);
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	
			{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
			
		}catch (SIGAException es) { 
			throw es; 
		}catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		} 
		
	}
	
	/** 
	 *  Funcion que atiende la accion abrir. Muestra la informacion relativa a la configuracion de un pago
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			
			ConfiguracionAbonosForm miform = (ConfiguracionAbonosForm)formulario;
			FcsPagosJGAdm pagos = new FcsPagosJGAdm(this.getUserBean(request));
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion = user.getLocation();
			String accion = request.getParameter("accion");
			String idPago = miform.getIdPagosJG();
			
			request.setAttribute("modo", accion);
			
			FacBancoInstitucionAdm admBancoFac = new FacBancoInstitucionAdm(this.getUserBean(request));
			Vector datosBancos = admBancoFac.obtenerBancos(idInstitucion);
			request.setAttribute("bancosInstitucion", datosBancos);
			
			// FacBancoInstitucionAbonosAdm bancoInstAdm = new FacBancoInstitucionAbonosAdm(this.getUserBean(request));
			// Hashtable datosPago = bancoInstAdm.getDatosAbono(idInstitucion, idPago);
			// request.setAttribute("concepto", datosPago.get(FacBancoInstitucionAbonosBean.T_NOMBRETABLA + "." + FacBancoInstitucionAbonosBean.C_CONCEPTO));
			// request.setAttribute("cuenta", datosPago.get(FacBancoInstitucionAbonosBean.T_NOMBRETABLA + "." + FacBancoInstitucionAbonosBean.C_BANCOS_CODIGO));
			
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion = (String)institucionAdm.getNombreInstitucion(idInstitucion);
			request.setAttribute("nombreInstitucion",nombreInstitucion);
			
			if((idPago!=null)&&(!idPago.equals(""))){
				//Recuperamos el estado, la fecha y el id del estado del pago:
				Hashtable hashEstado = pagos.getEstadoPago(new Integer(idInstitucion),new Integer(idPago));
				String nombreEstado = (String)hashEstado.get(FcsEstadosPagosBean.C_DESCRIPCION);
				String fechaEstado = (String)hashEstado.get(FcsPagosEstadosPagosBean.C_FECHAESTADO);
				String idEstadoPagosJG = (String)hashEstado.get(FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG);
				request.setAttribute("idEstadoPagosJG",idEstadoPagosJG);
				
				Hashtable hash = new Hashtable();
				hash.put(FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
				hash.put(FcsPagosJGBean.C_IDPAGOSJG, idPago);
				Vector v = pagos.selectByPK(hash);
				if ((v!=null)&&(v.size()>0)){
					FcsPagosJGBean bean = new FcsPagosJGBean();
					bean = (FcsPagosJGBean)v.firstElement();
					request.setAttribute("concepto", bean.getConcepto());
		 		 	request.setAttribute("cuenta", bean.getBancosCodigo());
				}
			}
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			request.setAttribute("paramConcepto", paramAdm.getValor(idInstitucion, "FCS", "CONCEPTO_ABONO", ""));
			request.setAttribute("paramIdCuenta", paramAdm.getValor(idInstitucion, "FCS", "BANCOS_CODIGO_ABONO", ""));
			
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		return "inicio";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UserTransaction tx = null;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ConfiguracionAbonosForm miForm = (ConfiguracionAbonosForm)formulario;
		FcsPagosJGAdm adm = new FcsPagosJGAdm(this.getUserBean(request));
		FcsPagosJGBean bean = new FcsPagosJGBean();
		String idInstitucion = usr.getLocation();
		String idPago = miForm.getIdPagosJG();
		try{
			tx = usr.getTransaction();
			tx.begin();
			
			Hashtable hash = new Hashtable();
			hash.put(FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
			hash.put(FcsPagosJGBean.C_IDPAGOSJG, idPago);
			Vector v = adm.selectByPK(hash);
			if ((v!=null)&&(v.size()>0)){
				FcsPagosJGBean oldBean = new FcsPagosJGBean();
				oldBean = (FcsPagosJGBean)v.firstElement();
				bean.setOriginalHash(oldBean.getOriginalHash());
			}
			bean.setConcepto(miForm.getConcepto());
			bean.setBancosCodigo(miForm.getCuenta());
			bean.setIdInstitucion(new Integer(Integer.parseInt(idInstitucion)));
			bean.setIdPagosJG(new Integer(Integer.parseInt(idPago)));
			
			adm.update(bean);
			tx.commit();
		} catch(Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		return exitoModal("messages.updated.success",request);
	}

}


