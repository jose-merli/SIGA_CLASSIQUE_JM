/*
 * VERSIONES:
 * 
 * miguel.villegas - 22-12-2005 - Creacion
 *	
 */

/**
 * Clase action para el tratamiento de plantillas.<br/>
 * Gestiona elmantenimiento de plantillas  
 */

package com.siga.facturacion.action;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import org.apache.struts.action.*;

import com.atos.utils.UsrBean;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.facturacion.form.PlantillasForm;
import com.siga.certificados.Plantilla;
import java.util.*;


public class PlantillasAction extends MasterAction {
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request -  objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		Vector devoluciones = new Vector();

		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");						
			String idInstitucion=user.getLocation();			
			
			Hashtable criterios =new Hashtable();
			Vector plantillas= new Vector();
			
			// Obtengo las diferentes plantillas existentes para cada institucion
			FacPlantillaFacturacionAdm plantillasAdm = new FacPlantillaFacturacionAdm(this.getUserBean(request));
			criterios.put(FacPlantillaFacturacionBean.C_IDINSTITUCION,idInstitucion);
			plantillas=plantillasAdm.select(criterios);
			
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("container", plantillas);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}				
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		return result;
		
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "buscar";
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
		
		String result="editar";
		Vector ocultos=new Vector();
		Vector datosPlantilla=new Vector();
		Vector modelosPlantilla=new Vector();
		Hashtable criterios=new Hashtable();
		try{
		
			PlantillasForm form = (PlantillasForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			// Obtengo la informacion relacionada con las plantillas
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(this.getUserBean(request));
			criterios.put(FacPlantillaFacturacionBean.C_IDINSTITUCION,(String)ocultos.get(0));
			criterios.put(FacPlantillaFacturacionBean.C_IDPLANTILLA,(String)ocultos.get(1));
			datosPlantilla=plantillaAdm.selectByPK(criterios);
			
			// Obtengol os distintos modelos de plantilla
			Plantilla modeloPlantillaAdm = new Plantilla(this.getUserBean(request));
			modelosPlantilla=modeloPlantillaAdm.obtencionModelosFacturas((String)ocultos.get(0));
					
			// Paso de parametros empleando request
			request.getSession().setAttribute("DATABACKUP", datosPlantilla.firstElement());
			
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", ocultos.get(0));
			request.setAttribute("container", datosPlantilla);
			request.setAttribute("plantillas", modelosPlantilla);
			request.setAttribute("modelo", "modificar");
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="ver";

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

		String result="nuevo";
		String idInstitucion="";
		
		try{		
			PlantillasForm form = (PlantillasForm) formulario;
			idInstitucion=form.getIdInstitucion();
			
			// Obtengol os distintos modelos de plantilla
			Plantilla modeloPlantillaAdm = new Plantilla(this.getUserBean(request));
			Vector modelosPlantilla=modeloPlantillaAdm.obtencionModelosFacturas(idInstitucion);
			
			// Paso de parametros
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("modelo", "insertar");
			request.setAttribute("plantillas", modelosPlantilla);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
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
		
		String result="abrir";
		UserTransaction tx = null;
		Hashtable hash=new Hashtable();
		boolean correcto=true;

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// Comienzo control de transacciones
			tx = usr.getTransaction(); 
			// Obtengo los datos del formulario
			PlantillasForm miForm = (PlantillasForm)formulario;
			
			// Genero la entrada en la tabla
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(this.getUserBean(request));
			hash.put(FacPlantillaFacturacionBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			hash.put(FacPlantillaFacturacionBean.C_IDPLANTILLA,plantillaAdm.getNuevoID(miForm.getIdInstitucion()).toString());
			hash.put(FacPlantillaFacturacionBean.C_DESCRIPCION,miForm.getDescripcion());
			hash.put(FacPlantillaFacturacionBean.C_PLANTILLAPDF,miForm.getPlantillaPDF());
			
			// Comienzo la transaccion
			tx.begin();		
		    		    
			if (plantillaAdm.insert(hash)){
				tx.commit();				
				result=exitoModal("messages.updated.success",request);			
			}

		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
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
		
		String result="modificar";
		UserTransaction tx = null;
		FacPlantillaFacturacionBean beanActualizado=new FacPlantillaFacturacionBean();
		
		try{
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// Comienzo control de transacciones
			tx = usr.getTransaction(); 
			// Obtengo los datos del formulario
			PlantillasForm miForm = (PlantillasForm)formulario;
			
			// Obtengo el bean original
			FacPlantillaFacturacionBean beanOriginal=(FacPlantillaFacturacionBean)request.getSession().getAttribute("DATABACKUP");
					
			// Genero la entrada a modificar en la tabla
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(this.getUserBean(request));
			beanActualizado.setIdInstitucion(beanOriginal.getIdInstitucion());
			beanActualizado.setIdPlantilla(beanOriginal.getIdPlantilla());
			beanActualizado.setDescripcion(miForm.getDescripcion());
			beanActualizado.setPlantillaPDF(miForm.getPlantillaPDF());
			
			// Comienzo la transaccion
			tx.begin();		
		    		    
			if (plantillaAdm.update(beanActualizado,beanOriginal)){
				tx.commit();				
				result=exitoModal("messages.updated.success",request);			
			}
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
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
		
		String result="borrar";
		Vector ocultos=new Vector();
		UserTransaction tx = null;
		Hashtable registro=new Hashtable();
		
		try{
		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// Valores obtenidos del formulario
			PlantillasForm form = (PlantillasForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			
			// Identifico el registro a borrar
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(this.getUserBean(request));
			registro.put(FacPlantillaFacturacionBean.C_IDINSTITUCION,(String)ocultos.get(0));
			registro.put(FacPlantillaFacturacionBean.C_IDPLANTILLA,(String)ocultos.get(1));
			
			// Comienzo la transaccion
			tx.begin();		
		    		    
			if (plantillaAdm.delete(registro)){
				tx.commit();				
				result=exitoRefresco("messages.deleted.success",request);			
			}
						
		} 
		catch (Exception e) { 
			throwExcp("facturacion.consultaPlantillas.literal.errorBorrado",new String[] {"modulo.facturacion"},e,null); 
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

		String result="listar";
		return (result);
	}
	
}

