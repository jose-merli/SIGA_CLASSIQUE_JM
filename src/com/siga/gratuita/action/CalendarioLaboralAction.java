package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsCalendarioLaboralAdm;
import com.siga.beans.ScsCalendarioLaboralBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.CalendarioLaboralForm;


//Clase: CalendarioLaboralAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 20/12/2004
/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_CALENDARIOLABORAL
*/
public class CalendarioLaboralAction extends MasterAction {	
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y realiza la consulta a partir de esos datos. Almacena un vector con los resultados
	 * en la sesión con el nombre "resultado"
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		CalendarioLaboralForm miForm = (CalendarioLaboralForm) formulario;		
		ScsCalendarioLaboralAdm admBean =  new ScsCalendarioLaboralAdm(this.getUserBean(request));
		Vector v = new Vector ();
		try {
			v = admBean.select(miForm.getDatos());
			request.getSession().setAttribute("resultado",v);			
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}		
		return "listarCalendario";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando esta hash en la sesión con el nombre "elegido"
	 *
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);		
			ScsCalendarioLaboralAdm admBean =  new ScsCalendarioLaboralAdm(this.getUserBean(request));		
			Hashtable miHash = new Hashtable();
			miHash.put(ScsCalendarioLaboralBean.C_IDENTIFICATIVO,(ocultos.get(0)));						
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(miHash);
			ScsCalendarioLaboralBean fiesta = (ScsCalendarioLaboralBean)resultado.get(0);		
			miHash.clear();		
			miHash = fiesta.getOriginalHash();
			request.getSession().setAttribute("elegido",miHash);
			request.getSession().setAttribute("DATABACKUP",miHash);		
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}
			
		return "modificacionOk";
	
	}

	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		return null;
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "

.
.Fiesta" para que redirija a la pantalla de inserción. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
			
		return "insertarFiesta";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		CalendarioLaboralForm miForm = (CalendarioLaboralForm) formulario;		
		ScsCalendarioLaboralAdm admBean =  new ScsCalendarioLaboralAdm(this.getUserBean(request));
		String forward="";
		Hashtable miHash = new Hashtable();		
		
		try {					
			miHash = miForm.getDatos();			
			miHash.put(ScsCalendarioLaboralBean.C_FECHA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFecha()));
			
			admBean.prepararInsert(miHash);
			
			tx=usr.getTransaction();
			tx.begin();			
			if (admBean.insert(miHash))
	        {
				tx.commit();
				forward = "exitoModal";					            
	        }	        
	        else forward = "exito";
			
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		if (forward.equalsIgnoreCase("exitoModal")) return exitoModal("messages.inserted.success",request);
		else return exito("messages.inserted.error",request);
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
	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
				
		CalendarioLaboralForm miForm = (CalendarioLaboralForm) formulario;		
		ScsCalendarioLaboralAdm admBean =  new ScsCalendarioLaboralAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();			
		
		try {	
			
			miHash = miForm.getDatos();			
			miHash.put(ScsCalendarioLaboralBean.C_FECHA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFecha()));
			// Si el IDPARTIDO es 0 quiere decir que no se ha seleccionado nada en el combo y por tanto afecta a todo el colegio.
			// Por ello en la hash no metemos nada, para que a la hora de hacer el insert almacene NULL
			if ((String)miHash.get(ScsCalendarioLaboralBean.C_IDPARTIDO) == "0") {
				miHash.put(ScsCalendarioLaboralBean.C_IDPARTIDO,"");
			}
			Hashtable hashBkp = new Hashtable();
			hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			request.getSession().removeAttribute("DATABACKUP");
			tx=usr.getTransaction();
			tx.begin();
			if (admBean.update(miHash,hashBkp)) 
			{
				tx.commit();
				forward = "exitoModal";
			}			
			else forward = "exito";
			
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		if (forward.equalsIgnoreCase("exitoModal")) return exitoModal("messages.updated.success",request);
		else return exito("messages.updated.error",request);
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector visibles = formulario.getDatosTablaVisibles(0);
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsCalendarioLaboralAdm admBean =  new ScsCalendarioLaboralAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();
		
		try {				
			miHash.put(ScsCalendarioLaboralBean.C_IDENTIFICATIVO,(ocultos.get(0)));
			miHash.put(ScsCalendarioLaboralBean.C_IDINSTITUCION,(ocultos.get(1)));
			miHash.put(ScsCalendarioLaboralBean.C_USUMODIFICACION,(ocultos.get(2)));
			miHash.put(ScsCalendarioLaboralBean.C_FECHAMODIFICACION,(ocultos.get(3)));			
			miHash.put(ScsCalendarioLaboralBean.C_FECHA,(visibles.get(0)));
			miHash.put(ScsCalendarioLaboralBean.C_NOMBREFIESTA,(visibles.get(1)));
			miHash.put(ScsCalendarioLaboralBean.C_IDPARTIDO,(visibles.get(2)));							
			
			tx=usr.getTransaction();
			tx.begin();
			if (admBean.delete(miHash))
		    {
				tx.commit();
				forward = "exitoRefresco";        
		    }		    
		    else forward ="exito";
			
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		if (forward.equalsIgnoreCase("exitoRefresco")) return exitoRefresco("messages.deleted.success",request);
		else return exito("messages.deleted.error",request);
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
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		return null;
	}
}