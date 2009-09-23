//VERSIONES:
//raul.ggonzalez 23-03-2005 Creacion
//

package com.siga.facturacionSJCS.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.facturacionSJCS.form.RegularizacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
* Clase action del caso de uso REGULARIZACION
* @author AtosOrigin 23-03-2005
*/
public class RegularizacionAction extends MasterAction {
	// Atributos
	/**   */



	/**
	 * Metodo que implementa el modo nuevo 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		
		try {
			RegularizacionForm miform = (RegularizacionForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			miform.setIdInstitucion(user.getLocation());
			
			destino="nuevo";
			
	     } 	
	     catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
	     return destino;
	}


	/**
	 * Metodo que implementa el modo insertar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		UserTransaction tx = null;
		
	     try {
			RegularizacionForm miform = (RegularizacionForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// compruebo si tiene regularizacion abierta (YA NO)
			FcsFacturacionJGAdm admFac = new FcsFacturacionJGAdm(this.getUserBean(request));
			//if (admFac.checkTieneRegularizacionAbierta(miform.getIdInstitucion(),miform.getIdFacturacion())) {
			//	throw new SIGAException("messages.factSJCS.error.regularizacionAbierta");
			//}

			// Comienzo control de transacciones 
			tx = user.getTransactionPesada();			
			tx.begin();			

			// inserto la regularizacion
			Integer idFacturacionRegularizada = admFac.insertarRegularizacion(miform.getIdInstitucion(), miform.getIdFacturacion(), miform.getNombre());
			
			tx.commit();
			
			request.setAttribute("idInstitucion", miform.getIdInstitucion());
			request.setAttribute("idFacturacion", (idFacturacionRegularizada!=null?""+idFacturacionRegularizada.intValue():""));
			destino = this.exitoModalRegularizacion("messages.updated.success",request);
	     } 	
	     catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
	   	 }
	     return destino;
	}
	
	
	protected String exitoModalRegularizacion(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("modal","");
		return "exitoRegularizacion"; 
	}
}
