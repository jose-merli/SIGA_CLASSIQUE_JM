package com.siga.gratuita.action;

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
import com.siga.beans.ScsAreaAdm;
import com.siga.beans.ScsAreaBean;
import com.siga.beans.ScsMateriaAdm;
import com.siga.beans.ScsMateriaBean;
import com.siga.beans.ScsMateriaJurisdiccionAdm;
import com.siga.beans.ScsMateriaJurisdiccionBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirAreasMateriasForm;


/*
 * VERSIONES:
 * julio.vicente - 28-12-2004 - Creación
 *	
 */
public class DefinirAreasMateriasAction extends MasterAction {	
	
			public ActionForward executeInternal (ActionMapping mapping,
					      ActionForm formulario,
					      HttpServletRequest request, 
					      HttpServletResponse response) throws SIGAException {
			
			String mapDestino = "exception";
			MasterForm miForm = null;
			
			try { 
			miForm = (MasterForm) formulario;
			
			String accion = miForm.getModo();
			
			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("nuevaJurisdiccionModal")){
			mapDestino = nuevaJurisdiccionModal(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("insertarJurisdiccionModal")){
				mapDestino = insertarJurisdiccionModal(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("borrarJurisdiccion")){
				mapDestino = borrarJurisdiccion(mapping, miForm, request, response);
			} else {
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
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		return null;
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
	 			
		Vector ocultos = formulario.getDatosTablaOcultos(0);
		DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;
		Hashtable miHash = new Hashtable();
		Vector resultado = new Vector();
		String forward="";
		try{
			/* En la sesión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición */
			request.getSession().setAttribute("accion",formulario.getModo());
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
					
			//AREA:
			if (miForm.getAccion().startsWith("area")) {			
				ScsAreaAdm admBeanArea =  new ScsAreaAdm(this.getUserBean(request));
				ScsMateriaAdm admBeanMateria =  new ScsMateriaAdm(this.getUserBean(request));
				
				/* A editar se puede llegar desde varios caminos. Desde una jsp con un <siga:TablaCabecerasFijas...> o desde otra
				 * jsp con una tabla normal. Por tanto, si vienen desde la siga:TablaCabecerasFijas habrá que recoger los hidden con
				 * getDatosOcultos y se almacenan en "ocultos". Si no es así daría un fallo y por tanto en el catch se recojerán de forma normal
				 * recuperandolos del formulario que viene como parámetro*/
				try {
					miHash.put(ScsAreaBean.C_IDINSTITUCION,(ocultos.get(0)));
					miHash.put(ScsAreaBean.C_IDAREA,(ocultos.get(1)));
					
					resultado = admBeanArea.selectPorClave(miHash);
					ScsAreaBean area = (ScsAreaBean)resultado.get(0);
					request.getSession().setAttribute("elegido",admBeanArea.beanToHashTable(area));
					request.getSession().setAttribute("DATABACKUP",area.getOriginalHash());
				
					forward="modificacionAreaOk";
				}
				catch (Exception e){
					miHash.put(ScsAreaBean.C_IDINSTITUCION,miForm.getDatos().get(ScsAreaBean.C_IDINSTITUCION));
					miHash.put(ScsAreaBean.C_IDAREA,miForm.getDatos().get(ScsAreaBean.C_IDAREA));
					
					/* Tanto si se ha editado una materia como si se acaba de insertar un área entrará en catch. Para discriminar
					 * de cuál de las dos ha venido a acción se le puede dar el valor: "areaEdicion" o "area". Si se acaba de insertar
					 * el área entonces el forward será "modificacionAreaOk", para que así que cargue la jsp de mantenimiento del área.
					 * Si la acción es "area", quiere decir que sólo se tendrá que cargar de nuevo la jsp del listado de materias
					 * */
					if (miForm.getAccion().equalsIgnoreCase("areaEdicion")) {										
						forward="modificacionAreaOk";
					}
					else forward="listadoMaterias";
				}			
				
				resultado.clear();
				try {
					resultado = admBeanMateria.select(miHash);			
				}catch(Exception e){
					throwExcp("messages.general.error",e,null);
				}
				
				request.getSession().setAttribute("resultado",resultado);			
			}
			//MATERIA:
			else {
				
				ScsMateriaAdm admBeanMateria =  new ScsMateriaAdm(this.getUserBean(request));
				
				
				
				// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
				try {
					miHash.put(ScsMateriaBean.C_IDAREA,(ocultos.get(0)));
					miHash.put(ScsMateriaBean.C_IDINSTITUCION,(ocultos.get(1)));
					miHash.put(ScsMateriaBean.C_IDMATERIA,(ocultos.get(2)));
					this.buscarJurisdicciones((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)usr.getLanguage(),request);
					
				}catch(Exception e){
					miHash.put(ScsMateriaBean.C_IDAREA,miForm.getDatos().get(ScsMateriaBean.C_IDAREA));
					miHash.put(ScsMateriaBean.C_IDINSTITUCION,miForm.getDatos().get(ScsMateriaBean.C_IDINSTITUCION));
					miHash.put(ScsMateriaBean.C_IDMATERIA,miForm.getDatos().get(ScsMateriaBean.C_IDMATERIA));
					this.buscarJurisdicciones(miForm.getDatos().get(ScsMateriaBean.C_IDAREA).toString(),(String)miForm.getDatos().get(ScsMateriaBean.C_IDINSTITUCION).toString(),miForm.getDatos().get(ScsMateriaBean.C_IDMATERIA).toString(),(String)usr.getLanguage(),request);
					//throwExcp("messages.general.error",e,null);
				}
				resultado = admBeanMateria.selectByPK(miHash);
				ScsMateriaBean materia = (ScsMateriaBean)resultado.get(0);
				
				request.getSession().setAttribute("elegido",admBeanMateria.beanToHashTable(materia));
				request.getSession().setAttribute("DATABACKUP2",materia.getOriginalHash());
				request.getSession().setAttribute("resultado",resultado);
				forward = "modificacionMateriaOk";
			}
			
		}catch(Exception e){
		    throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		

		Vector ocultos = formulario.getDatosTablaOcultos(0);
		DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;
		Hashtable miHash = new Hashtable();
		Vector resultado = new Vector();
		String forward="";
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		/* En la sesión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición */
		request.getSession().setAttribute("accion",formulario.getModo());		
				
		if (miForm.getAccion().equalsIgnoreCase("area")) {			
			ScsAreaAdm admBeanArea =  new ScsAreaAdm(this.getUserBean(request));
			ScsMateriaAdm admBeanMateria =  new ScsMateriaAdm(this.getUserBean(request));
			
			/* A editar se puede llegar desde varios caminos. Desde una jsp con un <siga:TablaCabecerasFijas...> o desde otra
			 * jsp con una tabla normal. Por tanto, si vienen desde la siga:TablaCabecerasFijas habrá que recoger los hidden con
			 * getDatosOcultos y se almacenan en "ocultos". Si no es así daría un fallo y por tanto en el catch se recojerán de formano normal
			 * recuperandolos del formulario que viene como parámetro*/
			try {
				miHash.put(ScsAreaBean.C_IDINSTITUCION,(ocultos.get(0)));
				miHash.put(ScsAreaBean.C_IDAREA,(ocultos.get(1)));
				
				resultado = admBeanArea.selectPorClave(miHash);
				ScsAreaBean area = (ScsAreaBean)resultado.get(0);
				request.getSession().setAttribute("elegido",admBeanArea.beanToHashTable(area));
				request.getSession().setAttribute("DATABACKUP",area.getOriginalHash());
				forward="modificacionAreaOk";
			}
			catch (Exception e){
				miHash.put(ScsAreaBean.C_IDINSTITUCION,miForm.getDatos().get(ScsAreaBean.C_IDINSTITUCION));
				miHash.put(ScsAreaBean.C_IDAREA,miForm.getDatos().get(ScsAreaBean.C_IDAREA));
				forward="listadoMaterias";
			}			
			
			resultado.clear();
			try {
				resultado = admBeanMateria.select(miHash);
			}catch(Exception e){
			    throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			}
			
			request.getSession().setAttribute("resultado",resultado);
			
		}
		else {
			
			ScsMateriaAdm admBeanMateria =  new ScsMateriaAdm(this.getUserBean(request));
			
			miHash.put(ScsMateriaBean.C_IDAREA,(ocultos.get(0)));
			miHash.put(ScsMateriaBean.C_IDINSTITUCION,(ocultos.get(1)));
			miHash.put(ScsMateriaBean.C_IDMATERIA,(ocultos.get(2)));
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			try {
				resultado = admBeanMateria.selectByPK(miHash);
				ScsMateriaBean materia = (ScsMateriaBean)resultado.get(0);		
				this.buscarJurisdicciones((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)usr.getLanguage(),request);
				request.getSession().setAttribute("elegido",admBeanMateria.beanToHashTable(materia));
				request.getSession().setAttribute("DATABACKUP2",materia.getOriginalHash());
				request.getSession().setAttribute("resultado",resultado);
			}catch(Exception e){
			    throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			}
			forward = "modificacionMateriaOk";
		}
		return forward;
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "insertarArea" para que redirija a la pantalla de inserción. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	

		DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;		
		String forward="";
		Hashtable miHash = miForm.getDatos();
		request.getSession().setAttribute("elegido",miHash);	
		
		if (miHash.get("ACCION").toString().equalsIgnoreCase("area")) {
			return "insertarArea";
		} else {
		    return "insertarMateria";
		}
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
		
		DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;		
		String forward="";
		Hashtable miHash = new Hashtable();		
		
		try {					
			miHash = miForm.getDatos();			
			tx=usr.getTransaction();
			
			//AREAS:
			if (miForm.getAccion().equalsIgnoreCase("area")) {
				ScsAreaAdm admBean =  new ScsAreaAdm(this.getUserBean(request));
				admBean.prepararInsert(miHash);
				
				tx.begin();
				if (!admBean.insert(miHash)){
				    throw new ClsExceptions(admBean.getError());
				}
				tx.commit();

				/* Almacenamos en la sesión el registro que se ha insertado en la base de datos para que al recargar
				 * la jsp de inserción de áreas (y también materias) nos cargue esos valores 
				 */
				request.getSession().setAttribute("elegido",miHash);
				
				/* Se almacena en DATABACKUP el registro recién insertado puesto que acontinuación se pasa a la pantalla
				   de mantenimiento y sino se hiciese daría un error en el momento de modificar.
				*/
				Vector resultado = admBean.selectPorClave(miHash);
				ScsAreaBean area = (ScsAreaBean)resultado.get(0);
				request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(area));
				
				Vector v = new Vector ();
				ScsMateriaAdm admBeanMateria =  new ScsMateriaAdm(this.getUserBean(request));
				v = admBeanMateria.select(miHash);
				request.getSession().setAttribute("resultado",v);
				request.getSession().setAttribute("accion","modificar");
				
				forward = exitoRefresco("messages.updated.success",request);
			}
			//MATERIAS:
			else {
				ScsMateriaAdm admBean =  new ScsMateriaAdm(this.getUserBean(request));
				admBean.prepararInsert(miHash);
				
				tx.begin();
				if (!admBean.insert(miHash)) {
				    throw new ClsExceptions(admBean.getError());
				}
				tx.commit();
				forward = exitoModal("messages.updated.success",request);
			}			
			

		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}				
		
		return forward;
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
		
		Hashtable miHash = new Hashtable();
		
		DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;					
		miHash = miForm.getDatos();	
		Vector v;
		String forward = null;
		
		try {	
			//AREA: si es una modificación de un área
			if (miHash.get("ACCION").toString().equalsIgnoreCase("area")) {
				
				Hashtable hashBkp = new Hashtable();
				hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");
				
				
				miHash.put("NOMBRE",(String)miHash.get("NOMBREAREA"));
				ScsAreaAdm admBean =  new ScsAreaAdm(this.getUserBean(request));
				tx=usr.getTransaction();
				
				tx.begin();
				if (admBean.update(miHash,hashBkp)){
		            forward = exito("messages.updated.success",request);
		            
				}else{
		            throw new ClsExceptions(admBean.getError());
		            
				}   
				tx.commit();
				
				request.getSession().removeAttribute("DATABACKUP");
				request.getSession().setAttribute("elegido",miHash);
				
				/* Consultamos en la base de datos para almacenar en DATABACKUP el registro modificado. Esto lo hacemos
				   por si se vuelve a modificar de nuevo el registro antes de salir del a jsp, para que al comprobar la 
				   integridad de los datos no falle.
				*/
    			miHash = miForm.getDatos();			
    			v = admBean.selectPorClave(miHash);
    			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable((ScsAreaBean)v.get(0)));			
				
	    		v.clear();	    		
	    		/* Ahora se consulta en la base de datos las MATERIAS asociadas a esta ÁREA para regenerar la jsp */				
				ScsMateriaAdm admBeanMateria =  new ScsMateriaAdm(this.getUserBean(request));
				v = admBeanMateria.select(miHash);
				request.getSession().setAttribute("resultado",v);
				request.getSession().setAttribute("accion","modificar");
			}
			//MATERIA: si se modifica una MATERIA
			else {
				
				Hashtable hashBkp = new Hashtable();
				hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP2");
				
				
				miHash.put("NOMBRE",(String)miHash.get("NOMBREMATERIA"));
				ScsMateriaAdm admBean =  new ScsMateriaAdm(this.getUserBean(request));
				tx=usr.getTransaction();
				tx.begin();
				if (admBean.update(miHash,hashBkp))
		        {				
					Vector resultado = new Vector();
					resultado = admBean.select(miHash);
					request.getSession().setAttribute("resultado",resultado);
					request.getSession().setAttribute("accion",miForm.getModo());
		            forward = exitoModal("messages.updated.success",request);
		        }	        
		        else
		        {
		            throw new ClsExceptions(admBean.getError());
		        }				
				tx.commit();
				
				request.getSession().removeAttribute("DATABACKUP2");
				
			}
			
			
		}catch(Exception e){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
        
		return forward;
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
		
		Hashtable miHash = new Hashtable();
		String forward = null;
		
		try {
			//MATERIA:
			if (formulario.getDatos().get("ACCION").toString().equalsIgnoreCase("materia")) {

				ScsMateriaJurisdiccionAdm admBeanMateriaJuris =  new ScsMateriaJurisdiccionAdm(this.getUserBean(request));				

				ScsMateriaAdm admBeanMateria =  new ScsMateriaAdm(this.getUserBean(request));				
				miHash.put(ScsMateriaBean.C_IDAREA,(ocultos.get(0)));
				miHash.put(ScsMateriaBean.C_IDINSTITUCION,(ocultos.get(1)));
				miHash.put(ScsMateriaBean.C_IDMATERIA,(ocultos.get(2)));
				
				tx=usr.getTransaction();
				tx.begin();

				String[] claves = {ScsMateriaBean.C_IDINSTITUCION,ScsMateriaBean.C_IDAREA,ScsMateriaBean.C_IDMATERIA};

				// Elimino sus jurisdicciones
				if (!admBeanMateriaJuris.deleteDirect(miHash,claves)) {
				    throw new ClsExceptions(admBeanMateriaJuris.getError());
				}

				// Elimino la materia
				if (!admBeanMateria.delete(miHash)) {
				    throw new ClsExceptions(admBeanMateria.getError());
				}
				tx.commit();
				forward = exitoRefresco("messages.deleted.success",request);
			    
			}
			//AREA:
			else {

				ScsMateriaJurisdiccionAdm admBeanMateriaJuris =  new ScsMateriaJurisdiccionAdm(this.getUserBean(request));				
				ScsMateriaAdm admBeanMateria =  new ScsMateriaAdm(this.getUserBean(request));				

				ScsAreaAdm admBeanArea =  new ScsAreaAdm(this.getUserBean(request));				
				miHash.put(ScsMateriaBean.C_IDINSTITUCION,(ocultos.get(0)));
				miHash.put(ScsMateriaBean.C_IDAREA,(ocultos.get(1)));
				
				tx=usr.getTransaction();
				tx.begin();
				
				String[] claves = {ScsMateriaBean.C_IDINSTITUCION,ScsMateriaBean.C_IDAREA};

				// Elimino sus jurisdicciones
				if (!admBeanMateriaJuris.deleteDirect(miHash,claves)) {
				    throw new ClsExceptions(admBeanMateriaJuris.getError());
				}

				// Elimino sus materias
				if (!admBeanMateria.deleteDirect(miHash,claves)) {
				    throw new ClsExceptions(admBeanMateria.getError());
				}

				// Elimino el area
				if (!admBeanArea.delete(miHash)) {
				    throw new ClsExceptions(admBeanArea.getError());
				}
				    
				tx.commit();
				forward = exitoRefresco("messages.deleted.success",request);
			        
				
			}			
			
		}catch(Exception e){
		    throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}		
        
		return forward;
	}

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
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;		
		ScsAreaAdm admBean =  new ScsAreaAdm(this.getUserBean(request));
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();

		try {
			miHash = miForm.getDatos();			
			v = admBean.select(miHash, true);
			request.getSession().setAttribute("resultado",v);
			/* Se almacenan los datos de la búsqueda en DATOSFORMULARIO para cuando se regrese a la pantalla de búsqueda
			 * pueda hacer el refresco
			 */
			miHash.put("BUSQUEDAREALIZADA","1");
			request.getSession().setAttribute("DATOSFORMULARIO",miHash);
			
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}	
		return "listarAreas";
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATABACKUP2");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("accion");
		return "inicio";
	}
	
	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accion");
		return "busqueda";
	}
	protected String nuevaJurisdiccionModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		try {
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;
			
			ScsMateriaJurisdiccionAdm adm = new ScsMateriaJurisdiccionAdm (this.getUserBean(request));
			
			Vector v = adm.busquedaJurisdiccionMateriaQueNoEstenEnMateria(miForm.getIdMateria(),miForm.getIdArea(),usr.getLanguage() );
			request.setAttribute("JURISDICCIONES", v);
			request.setAttribute("IDINSTITUCION", miForm.getIdInstitucion());
			request.setAttribute("IDMATERIA", miForm.getIdMateria());
			request.setAttribute("IDAREA",miForm.getIdArea());
			
			
		}
		catch (Exception e) {
			
		}

		return "nuevaJurisdiccion";
	}
	protected String insertarJurisdiccionModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction tx = null;
		UsrBean user = null;
		String idInstitucionProcedimiento=null, idProcedimiento=null;
		
		try {
			DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;
			user = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = user.getTransaction();
			
			ScsMateriaJurisdiccionAdm materiaJurisdiccionAdm = new ScsMateriaJurisdiccionAdm (this.getUserBean(request));
			ScsMateriaJurisdiccionBean beanMateriaJurisdiccion = new ScsMateriaJurisdiccionBean();
			
			Integer idJurisdiccion =null;
			Integer idArea = miForm.getIdArea();
			Integer idMateria = miForm.getIdMateria();
			Integer idInstitucion =miForm.getIdInstitucion();
			

			tx.begin();

			beanMateriaJurisdiccion.setIdInstitucion(idInstitucion);
			beanMateriaJurisdiccion.setIdArea(idArea);
			beanMateriaJurisdiccion.setIdMateria(idMateria);
			beanMateriaJurisdiccion.setIdJurisdiccion(idJurisdiccion);
			
            String jurisdiccionSel=request.getParameter("jurisdiccion");
			String jurisdiccion[] = jurisdiccionSel.split("%");
			for (int i = 0; i < jurisdiccion.length; i++) {
				idJurisdiccion = new Integer(jurisdiccion[i]);			
				beanMateriaJurisdiccion.setIdJurisdiccion(idJurisdiccion);
				materiaJurisdiccionAdm.insert(beanMateriaJurisdiccion);
			}
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success", request);
	}	

	protected String borrarJurisdiccion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction tx = null;
		UsrBean user = null;
		
		
		try {
			DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;
			user = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = user.getTransaction();
			
			ScsMateriaJurisdiccionAdm materiaJurisdiccionAdm = new ScsMateriaJurisdiccionAdm (this.getUserBean(request));
			ScsMateriaJurisdiccionBean beanMateriaJurisdiccion = new ScsMateriaJurisdiccionBean();
			
			Integer idJurisdiccion = new Integer(miForm.getIdJurisdiccion());;
			Integer idArea = miForm.getIdArea();
			Integer idMateria = miForm.getIdMateria();
			Integer idInstitucion= miForm.getIdInstitucion();
			
			
			beanMateriaJurisdiccion.setIdInstitucion(idInstitucion);
			beanMateriaJurisdiccion.setIdArea(idArea);
			beanMateriaJurisdiccion.setIdMateria(idMateria);
			beanMateriaJurisdiccion.setIdJurisdiccion(idJurisdiccion);
						
			tx.begin();
			materiaJurisdiccionAdm.delete(beanMateriaJurisdiccion);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.deleted.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoRefresco("messages.deleted.success", request);
	}	
	private void buscarJurisdicciones(String idArea, String idInstitucion,String idMateria, String idioma,HttpServletRequest request) throws ClsExceptions, SIGAException {
		try {
			
			ScsMateriaJurisdiccionAdm materiaJurisdiccionAdm = new ScsMateriaJurisdiccionAdm (this.getUserBean(request));
			Vector vJurisdicciones = materiaJurisdiccionAdm.busquedaJurisdiccionMateria(idArea,idInstitucion, idMateria,idioma);

			request.setAttribute("vJurisdicciones", vJurisdicciones);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		} 		
	}
	/*protected String recargarMateriaModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		DefinirAreasMateriasForm miForm = (DefinirAreasMateriasForm) formulario;
		Integer	idMateria = miForm.getIdMateria();
		String	idInstitucion = this.getUserBean(request).getLocation();
		
		//return editar(modo,formulario,idInstitucionJuzgado,idJuzgado,request);
	}*/
	
}