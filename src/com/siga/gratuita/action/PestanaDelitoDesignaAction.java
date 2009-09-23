package com.siga.gratuita.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsDelitosDesignaAdm;
import com.siga.beans.ScsDelitosDesignaBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.PestanaDelitoDesignaForm;

/**
 * @author david.sanchez
 * @since 25/01/2006
 *
 */
public class PestanaDelitoDesignaAction extends MasterAction {
	
	/**
	 * Accion por defecto: abre el jsp inicial y recupera los datos de la pestanha. 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		PestanaDelitoDesignaForm miForm = (PestanaDelitoDesignaForm)formulario;
		String idDelito = null;			
		String anio=null, numero=null, idInstitucion=null, idTurno=null;

		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			// Obtengo de la pestanha el anio, numero, idTurno:			
			numero = (String)request.getParameter("NUMERO");
			anio = (String)request.getParameter("ANIO");
			idTurno = (String)request.getParameter("IDTURNO");
			
			// Almaceno los parametros en el formulario:			
			miForm.setIdTurno(new Integer(idTurno));
			miForm.setNumero(new Integer(numero));
			miForm.setAnio(new Integer(anio));
			
		} catch (Exception e){
			throwExcp("messages.general.error",e,null);
		}
		return "inicio";
	}


	/**
	 * Busca los delitos de una Designa concreta. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoDesignaForm miForm = (PestanaDelitoDesignaForm)formulario;		
		Integer anio, numero, idInstitucion, idTurno;

		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsDelitosDesignaAdm admDelitoDesigna = new ScsDelitosDesignaAdm(this.getUserBean(request));
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			idTurno = miForm.getIdTurno();			
			
			Vector vDelitosDesigna = admDelitoDesigna.getDelitosDesigna(idInstitucion,anio,numero,idTurno,usr.getLanguage());
			request.setAttribute("vDelitosDesigna",vDelitosDesigna);
		} catch (Exception e){
			throwExcp("messages.general.error",e,null);
		}
		return "lista";
	}

	/**
	 * Mapeo a la ventana modal para crear un nuevo delito de la Designa.
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
		return "nuevo";
	}

	/**
	 * Inserta un nuevo delito a una Designa concreta.	 
	 *  
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * @throws ClsExceptions
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoDesignaForm miForm = (PestanaDelitoDesignaForm)formulario;		
		Integer anio, numero, idInstitucion, idTurno, idDelito;
		UserTransaction tx = null;
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsDelitosDesignaAdm admDelitoDesigna = new ScsDelitosDesignaAdm(this.getUserBean(request));
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			idTurno = miForm.getIdTurno();
			idDelito = miForm.getIdDelito();
			
			ScsDelitosDesignaBean beanDelitoDesigna = new ScsDelitosDesignaBean();
			beanDelitoDesigna.setAnio(anio);
			beanDelitoDesigna.setIdDelito(idDelito);
			beanDelitoDesigna.setIdInstitucion(idInstitucion);
			beanDelitoDesigna.setNumero(numero);
			beanDelitoDesigna.setIdTurno(idTurno);
			beanDelitoDesigna.setFechaMod("SYSDATE");
			beanDelitoDesigna.setUsuMod(new Integer(usr.getUserName()));
						
			tx.begin();
			admDelitoDesigna.insert(beanDelitoDesigna);
			tx.commit();
		} catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return exitoModal("messages.inserted.success",request);
	}

	/**
	 * 
	 * Borra un delito para 1 Designa y un idDelitoDesigna.
	 * 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoDesignaForm miForm = (PestanaDelitoDesignaForm)formulario;
				
		Integer anio, numero, idInstitucion, idTurno, idDelito;
		UserTransaction tx = null;
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsDelitosDesignaAdm admDelitoDesigna = new ScsDelitosDesignaAdm(this.getUserBean(request));
			
			Vector vOcultos = miForm.getDatosTablaOcultos(0);
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			idDelito = new Integer((String)vOcultos.get(0));
			
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			idTurno = miForm.getIdTurno();
			
			ScsDelitosDesignaBean beanDelitoDesigna = new ScsDelitosDesignaBean();
			beanDelitoDesigna.setAnio(anio);
			beanDelitoDesigna.setIdDelito(idDelito);
			beanDelitoDesigna.setIdInstitucion(idInstitucion);
			beanDelitoDesigna.setNumero(numero);
			beanDelitoDesigna.setIdTurno(idTurno);
						
			tx.begin();
			admDelitoDesigna.delete(beanDelitoDesigna);
			tx.commit();
		} catch (Exception e){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return exitoRefresco("messages.deleted.success",request);
	}
}