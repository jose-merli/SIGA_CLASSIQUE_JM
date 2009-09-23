//VERSIONES
//raul.ggonzalez 11-01-2005 Creacion
//

package com.siga.censo.action;

import java.util.Hashtable;
import javax.servlet.http.*;
import javax.transaction.*;
import org.apache.struts.action.*;
import com.siga.beans.*;
import com.siga.censo.form.DocumentacionSolicitudForm;
import com.siga.general.*;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import java.util.Vector;


/**
* Clase action del caso de uso DOCUMENTACION SOLICITUD
* @author AtosOrigin 11-01-2005
*/
public class DocumentacionSolicitudAction extends MasterAction {

	// Atributos
	/**   */

	
	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
	   try {

			DocumentacionSolicitudForm miform = (DocumentacionSolicitudForm)formulario;
			miform.reset(mapping,request);
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
	   } 	catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   }

		// COMUN
		return "inicio";

	}



	/**
	 * Metodo que implementa el modo buscarPor 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String result=null;
		Hashtable hashOriginal = new Hashtable(); 		
		UserTransaction tx = null;
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// Obtengo los datos del formulario
			DocumentacionSolicitudForm miForm = (DocumentacionSolicitudForm)formulario;
			Hashtable hash = miForm.getDatos();

			// los seleccionados para esa institucion
			CenDocumentacionSolicitudInstituAdm docAdm = new CenDocumentacionSolicitudInstituAdm(this.getUserBean(request));
			Vector resultado = docAdm.getDocumentacion(hash,usr.getLocation());
			// me hago un vector solo con los ids de documentos
			Vector resultadoIds = new Vector();
			for (int i=0;i<resultado.size();i++) {
				CenDocumentacionSolicitudBean documBean = (CenDocumentacionSolicitudBean) resultado.get(i); 
				resultadoIds.add(documBean.getIdDocumentacion());
			}
			request.setAttribute("CenDocumentacionSolicitudResultados",resultadoIds);

			// todos los documentos
			CenDocumentacionSolicitudAdm todosAdm = new CenDocumentacionSolicitudAdm(this.getUserBean(request));
			Vector todos = todosAdm.select();
			request.setAttribute("CenDocumentacionSolicitudTodosDocumentos",todos);
	   } 	
		catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   }
		return "resultado";			
	}

	/**
	 * Metodo que implementa el modo insertar (graba la insercion)
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String result=null;
		Hashtable hashOriginal = new Hashtable(); 		
		UserTransaction tx = null;
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// Obtengo los datos del formulario
			DocumentacionSolicitudForm miForm = (DocumentacionSolicitudForm)formulario;
			Hashtable hash = miForm.getDatos();

			CenDocumentacionSolicitudInstituAdm docAdm = new CenDocumentacionSolicitudInstituAdm(this.getUserBean(request));
	 		
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	
			
			//String[] documentos = miForm.getDocumentosSolicitados();
			String documentosRecuperados = request.getParameter("documentosSolicitados");
			String[] documentos;
			//Si no tenemos documentos lo pongo a null:
			if (documentosRecuperados!=null && documentosRecuperados.equals(""))
				documentos = null;
			else
				documentos = documentosRecuperados.split("%");
			
			if (!docAdm.insertDocumentacion(hash,documentos,usr.getLocation())) {
				throw new SIGAException(docAdm.getError());
			}
			
			tx.commit();

	   } 	catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
	   }
		
	   return 	this.exitoRefresco("messages.updated.success",request);			
	
	}
}
