package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsSubZonaPartidoBean;
import com.siga.beans.ScsSubZonaPartidoAdm;
import com.siga.beans.ScsAreaBean;
import com.siga.beans.ScsSubzonaAdm;
import com.siga.beans.ScsSubzonaBean;
import com.siga.beans.ScsZonaAdm;
import com.siga.beans.ScsZonaBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirZonasSubzonasForm;


/*
 * VERSIONES:
 * julio.vicente - 14-01-2005 - Creación
 *	
 */
public class DefinirZonasSubzonasAction extends MasterAction 
{	
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
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		Vector ocultos = formulario.getDatosTablaOcultos(0);
		DefinirZonasSubzonasForm miForm = (DefinirZonasSubzonasForm) formulario;
		Hashtable miHash = new Hashtable();
		Vector resultado = new Vector();
		String forward="";
		
		/* En la sesión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición */
		request.getSession().setAttribute("accion",formulario.getModo());		
				
		if (miForm.getAccion().startsWith("zona")) {			
			ScsZonaAdm admBeanZona =  new ScsZonaAdm(this.getUserBean(request));
			ScsSubzonaAdm admBeanSubzona =  new ScsSubzonaAdm(this.getUserBean(request));
			
			/* A editar se puede llegar desde varios caminos. Desde una jsp con un <siga:TablaCabecerasFijas...> o desde otra
			 * jsp con una tabla normal. Por tanto, si vienen desde la siga:TablaCabecerasFijas habrá que recoger los hidden con
			 * getDatosOcultos y se almacenan en "ocultos". Si no es así daría un fallo y por tanto en el catch se recojerán de formano normal
			 * recuperandolos del formulario que viene como parámetro*/
			try {
				miHash.put(ScsZonaBean.C_IDINSTITUCION,(ocultos.get(0)));
				miHash.put(ScsZonaBean.C_IDZONA,(ocultos.get(1)));
				
				resultado = admBeanZona.selectPorClave(miHash);
				ScsZonaBean zona = (ScsZonaBean)resultado.get(0);
				request.getSession().setAttribute("elegido",admBeanZona.beanToHashTable(zona));
				request.getSession().setAttribute("DATABACKUP",zona.getOriginalHash());
				forward="modificacionZonaOk";
			}
			catch (Exception e){
				miHash.put(ScsZonaBean.C_IDINSTITUCION,miForm.getDatos().get("INSTITUCIONZONA"));
				miHash.put(ScsZonaBean.C_IDZONA,miForm.getDatos().get(ScsZonaBean.C_IDZONA));
				/* Tanto si se ha editado una subzona como si se acaba de insertar un zona entrará en catch. Para discriminar
				 * de cuál de las dos ha venido a acción se le puede dar el valor: "zonaEdicion" o "zona". Si se acaba de insertar
				 * el zona entonces el forward será "modificacionZonaOk", para que así que cargue la jsp de mantenimiento del zona.
				 * Si la acción es "zona", quiere decir que sólo se tendrá que cargar de nuevo la jsp del listado de subzonas
				 * */
				if (miForm.getAccion().equalsIgnoreCase("zonaEdicion")) {										
					forward="modificacionZonaOk";
				}
				else forward="listadoSubzonas";
			}			
			
			resultado.clear();
			try {
				resultado = admBeanSubzona.selectPorClave(miHash);
			}
			catch(Exception e){
				throwExcp("messages.general.error",e,null);
			}
			request.getSession().setAttribute("resultado",resultado);
		}
		else {
			ScsSubzonaAdm admBeanSubzona =  new ScsSubzonaAdm(this.getUserBean(request));
			miHash.put(ScsSubzonaBean.C_IDZONA,(ocultos.get(0)));
			miHash.put(ScsSubzonaBean.C_IDINSTITUCION,(ocultos.get(1)));
			miHash.put(ScsSubzonaBean.C_IDSUBZONA,(ocultos.get(2)));
			Hashtable miHashPJ= new  Hashtable();
			Vector resultadoPJ = new Vector();
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			try {
				resultado = admBeanSubzona.select(miHash);
				resultadoPJ = admBeanSubzona.getPartidosJudiciales(miHash);
				ScsSubzonaBean subzona = (ScsSubzonaBean)resultado.get(0);			
				request.getSession().setAttribute("elegido",admBeanSubzona.beanToHashTable(subzona));
				request.getSession().setAttribute("DATABACKUP2",subzona.getOriginalHash());
				request.getSession().setAttribute("resultado",resultado);
				request.getSession().setAttribute("resultadoPJ",resultadoPJ);
			}
			catch(Exception e){
				throwExcp("messages.general.error",e,null);
			}
			forward = "modificacionSubzonaOk";
		}
		return forward;
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{		
		Vector ocultos = formulario.getDatosTablaOcultos(0);
		DefinirZonasSubzonasForm miForm = (DefinirZonasSubzonasForm) formulario;
		Hashtable miHash = new Hashtable();
		Vector resultado = new Vector();
		String forward="";
		
		/* En la sesión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición */
		request.getSession().setAttribute("accion",formulario.getModo());		
				
		if (miForm.getAccion().equalsIgnoreCase("zona")) {			
			ScsZonaAdm admBeanZona =  new ScsZonaAdm(this.getUserBean(request));
			ScsSubzonaAdm admBeanSubzona =  new ScsSubzonaAdm(this.getUserBean(request));
			
			/* A editar se puede llegar desde varios caminos. Desde una jsp con un <siga:TablaCabecerasFijas...> o desde otra
			 * jsp con una tabla normal. Por tanto, si vienen desde la siga:TablaCabecerasFijas habrá que recoger los hidden con
			 * getDatosOcultos y se almacenan en "ocultos". Si no es así daría un fallo y por tanto en el catch se recojerán de formano normal
			 * recuperandolos del formulario que viene como parámetro*/
			try {
				miHash.put(ScsZonaBean.C_IDINSTITUCION,(ocultos.get(0)));
				miHash.put(ScsZonaBean.C_IDZONA,(ocultos.get(1)));
				
				resultado = admBeanZona.selectPorClave(miHash);
				ScsZonaBean zona = (ScsZonaBean)resultado.get(0);
				request.getSession().setAttribute("elegido",admBeanZona.beanToHashTable(zona));
				request.getSession().setAttribute("DATABACKUP",zona.getOriginalHash());
				forward="modificacionZonaOk";
			}
			catch (Exception e){
				miHash.put(ScsAreaBean.C_IDINSTITUCION,miForm.getDatos().get(ScsZonaBean.C_IDINSTITUCION));
				miHash.put(ScsAreaBean.C_IDAREA,miForm.getDatos().get(ScsZonaBean.C_IDZONA));
				forward="listadoZonas";
			}			
			
			resultado.clear();
			try {
				resultado = admBeanSubzona.select(miHash);
			}
			catch(Exception e){
				throwExcp("messages.general.error",e,null);
			}
			request.getSession().setAttribute("resultado",resultado);
		}
		else {
			ScsSubzonaAdm admBeanSubzona =  new ScsSubzonaAdm(this.getUserBean(request));
			miHash.put(ScsSubzonaBean.C_IDZONA,(ocultos.get(0)));
			miHash.put(ScsSubzonaBean.C_IDINSTITUCION,(ocultos.get(1)));
			miHash.put(ScsSubzonaBean.C_IDSUBZONA,(ocultos.get(2)));
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			try {
				resultado = admBeanSubzona.select(miHash);
				ScsSubzonaBean subzona = (ScsSubzonaBean)resultado.get(0);
				
				Vector resultadoPJ = new Vector();
				resultadoPJ = admBeanSubzona.getPartidosJudiciales(miHash);
				request.getSession().setAttribute("resultadoPJ",resultadoPJ);
				request.getSession().setAttribute("elegido",admBeanSubzona.beanToHashTable(subzona));
				request.getSession().setAttribute("DATABACKUP2",subzona.getOriginalHash());
				request.getSession().setAttribute("resultado",resultado);
			}
			catch(Exception e){
				throwExcp("messages.general.error",e,null);
			}
			forward = "modificacionSubzonaOk";
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		DefinirZonasSubzonasForm miForm = (DefinirZonasSubzonasForm) formulario;		
		String forward="";
		Hashtable miHash = miForm.getDatos();
		request.getSession().setAttribute("elegido",miHash);	
		request.setAttribute("modoZona","insertar");
		
		if (miHash.get("ACCION").toString().equalsIgnoreCase("zona"))
			return "insertarZona";

		else 
			return "insertarSubzona";
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
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		DefinirZonasSubzonasForm miForm = (DefinirZonasSubzonasForm) formulario;		
		String forward="";
		Hashtable miHash = new Hashtable();		
		
		try {					
			miHash = miForm.getDatos();			
			tx=usr.getTransaction();			

			//INSERTAR ZONA:
			if (miForm.getAccion().equalsIgnoreCase("zona")) {
				ScsZonaAdm admBean =  new ScsZonaAdm(this.getUserBean(request));
				admBean.prepararInsert(miHash);

				tx.begin();				
				admBean.insert(miHash);
				tx.commit();
				/* Almacenamos en la sesión el registro que se ha insertado en la base de datos para que al recargar
				 * la jsp de inserción de áreas (y también materias) nos cargue esos valores 
				 */
				request.getSession().setAttribute("elegido",miHash);
				
				/* Se almacena en DATABACKUP el registro recién insertado puesto que acontinuación se pasa a la pantalla
				   de mantenimiento y sino se hiciese daría un error en el momento de modificar.
				*/
				Vector resultado = admBean.selectPorClave(miHash);
				ScsZonaBean zona = (ScsZonaBean)resultado.get(0);
				request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(zona));
				
				Vector v = new Vector ();
				ScsSubzonaAdm admBeanSubzona =  new ScsSubzonaAdm(this.getUserBean(request));
				v = admBeanSubzona.selectPorClave(miHash);
				request.getSession().setAttribute("resultado",v);
				request.getSession().setAttribute("accion","modificar");
				request.setAttribute("modoZona","modificar");
				forward = exitoRefresco("messages.inserted.success",request);
			} 
			//INSERTAR SUBZONA
			else {
				ScsSubzonaAdm admBean =  new ScsSubzonaAdm(this.getUserBean(request));
				miHash.put("IDINSTITUCION",miHash.get("INSTITUCIONSUBZONA"));
				miHash = admBean.prepararInsert(miHash);
				tx.begin();
				miForm.setIdSubzona(new Integer((String)miHash.get(ScsSubzonaBean.C_IDSUBZONA)).intValue());
				if (!admBean.insert(miHash)) {
					throw new ClsExceptions("Error al insertar Zubzona");
				}
				// primero obtengo los partidos judiciales que habia para borrarlos
				ScsSubZonaPartidoAdm admZP = new ScsSubZonaPartidoAdm(this.getUserBean(request));
				Hashtable hashWhere = new Hashtable();	
				hashWhere.put(ScsSubZonaPartidoBean.C_IDINSTITUCION,new Integer(usr.getLocation()));
				hashWhere.put(ScsSubZonaPartidoBean.C_IDSUBZONA,new Integer(miForm.getIdSubzona()));
				hashWhere.put(ScsSubZonaPartidoBean.C_IDZONA,new Integer(miForm.getIdZona()));
				Vector v = admZP.select(hashWhere);

				// borro los partidos que habia
				int i;
				for (i=0;i<v.size();i++) {
					ScsSubZonaPartidoBean beanZP = (ScsSubZonaPartidoBean) v.get(i);
					if (!admZP.delete(beanZP)) {
						//LMS 21/08/2006
						//Cambio por el nuevo uso de subLiteral en SIGAException.
						//SIGAException exc=new SIGAException("messages.err.noseque.datosgenerales.de.alguien");
						//exc.setSubLiteral("messages.censo.gruponoexiste.bbdd");
						//throw new ClsExceptions(admGrupos.getError());
						
						SIGAException exc=new SIGAException("messages.censo.gruponoexiste.bbdd");
					}
				}
				
				// ahora inserto los nuevos
				String partidos[] = miForm.getPartidosJudiciales();
				if (partidos!=null) { 
					for (i=0;i<partidos.length;i++) {
						String idPartido = (String) partidos[i];
						if (!idPartido.equals("")) {
							ScsSubZonaPartidoBean beanZP = new ScsSubZonaPartidoBean();
							
							beanZP.setIdInstitucion(new Integer(usr.getLocation()));
							beanZP.setIdZona(new Integer(miForm.getIdZona()));
							beanZP.setIdSubZona(new Integer(miForm.getIdSubzona()));
							beanZP.setIdPartido(new Integer(idPartido));
							if (!admZP.insert(beanZP)) {
								throw new ClsExceptions(admZP.getError());
							}
						}
					}
				} 				
				
				tx.commit();
				forward = exitoModal("messages.inserted.success",request);
			}			
		}
		catch (Exception e) {
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
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;
		String forward = null;
		
		try {	
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirZonasSubzonasForm miForm = (DefinirZonasSubzonasForm) formulario;					
			Hashtable miHash = new Hashtable();
			miHash = miForm.getDatos();	
			Vector v;
			
			// Si es una modificación de una zona
			if (miHash.get("ACCION").toString().equalsIgnoreCase("zona")) {
				Hashtable hashBkp = new Hashtable();
				hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");
				request.getSession().removeAttribute("DATABACKUP");
				
				miHash.put(ScsZonaBean.C_NOMBRE,(String)miHash.get("NOMBREZONA"));
				miHash.put(ScsZonaBean.C_IDINSTITUCION,(String)miHash.get("INSTITUCIONZONA"));
				ScsZonaAdm admBean =  new ScsZonaAdm(this.getUserBean(request));				

				tx = usr.getTransaction();
				tx.begin();
				admBean.update(miHash,hashBkp);
				tx.commit();
				
				request.getSession().setAttribute("elegido",miHash);
				
				/* Consultamos en la base de datos para almacenar en DATABACKUP el registro modificado. Esto lo hacemos
				   por si se vuelve a modificar de nuevo el registro antes de salir del a jsp, para que al comprobar la 
				   integridad de los datos no falle.
				*/
    			miHash = miForm.getDatos();
    			miHash.put(ScsZonaBean.C_IDINSTITUCION,miHash.get("INSTITUCIONZONA"));
    			v = admBean.selectPorClave(miHash);
    			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable((ScsZonaBean)v.get(0)));			
				
	    		v.clear();	    		
	    		/* Ahora se consulta en la base de datos las SUBZONA asociadas a esta ZONA para regenerar la jsp */				
				ScsSubzonaAdm admBeanSubzona =  new ScsSubzonaAdm(this.getUserBean(request));
				v = admBeanSubzona.selectPorClave(miHash);
				request.getSession().setAttribute("resultado",v);
				request.getSession().setAttribute("accion","modificar");
				request.setAttribute("modoZona","modificar");
				forward = exito("messages.updated.success",request);
			}
			// Si se modifica una SUBZONA
			else {
				Hashtable hashBkp = new Hashtable();
				hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP2");
				request.getSession().removeAttribute("DATABACKUP2");
				
				miHash.put(ScsSubzonaBean.C_NOMBRE,(String)miHash.get("NOMBRESUBZONA"));
				miHash.put(ScsSubzonaBean.C_IDINSTITUCION,(String)miHash.get("INSTITUCIONSUBZONA"));
				ScsSubzonaAdm admBean =  new ScsSubzonaAdm(this.getUserBean(request));
				/* Si el IDPARTIDO es 0 quiere decir que no se ha seleccionado nada en el combo de partidos judiciales
				   Por ello en la hash no metemos nada 
				*/
				if (miHash.get(ScsSubzonaBean.C_IDPARTIDO)==null || ((String)miHash.get(ScsSubzonaBean.C_IDPARTIDO)).equals("0")) {
					miHash.put(ScsSubzonaBean.C_IDPARTIDO,"NULL");
				}
				tx=usr.getTransaction();
				tx.begin();
				admBean.update(miHash,hashBkp);


				Vector resultado = new Vector();
				resultado = admBean.selectPorClave(miHash);
				request.getSession().setAttribute("resultado",resultado);
				request.getSession().setAttribute("accion",miForm.getModo());	            

				// primero obtengo los partidos judiciales que habia para borrarlos
				ScsSubZonaPartidoAdm admZP = new ScsSubZonaPartidoAdm(this.getUserBean(request));
				Hashtable hashWhere = new Hashtable();	
				hashWhere.put(ScsSubZonaPartidoBean.C_IDINSTITUCION,new Integer(usr.getLocation()));
				hashWhere.put(ScsSubZonaPartidoBean.C_IDSUBZONA,new Long(miForm.getIdSubzona()));
				hashWhere.put(ScsSubZonaPartidoBean.C_IDZONA,new Long(miForm.getIdZona()));
				Vector vPartidos = admZP.select(hashWhere);

				// borro los partidos que habia
				int i;
				for (i=0;i<vPartidos.size();i++) {
					ScsSubZonaPartidoBean beanZP = (ScsSubZonaPartidoBean) vPartidos.get(i);
					if (!admZP.delete(beanZP)) {
						//LMS 21/08/2006
						//Cambio por el nuevo uso de subLiteral en SIGAException.
						//SIGAException exc=new SIGAException("messages.err.noseque.datosgenerales.de.alguien");
						//exc.setSubLiteral("messages.censo.gruponoexiste.bbdd");
						//throw new ClsExceptions(admGrupos.getError());
						
						SIGAException exc=new SIGAException("messages.censo.gruponoexiste.bbdd");
					}
				}
				
				// ahora inserto los nuevos
				String partidos[] = miForm.getPartidosJudiciales();
				if (partidos!=null) { 
					for (i=0;i<partidos.length;i++) {
						String idPartido = (String) partidos[i];
						if (!idPartido.equals("")) {
							ScsSubZonaPartidoBean beanZP = new ScsSubZonaPartidoBean();
							beanZP.setIdInstitucion(new Integer(usr.getLocation()));
							beanZP.setIdZona(new Integer(miForm.getIdZona()));
							beanZP.setIdSubZona(new Integer(miForm.getIdSubzona()));
							beanZP.setIdPartido(new Integer(idPartido));
							if (!admZP.insert(beanZP)) {
								throw new ClsExceptions(admZP.getError());
							}
						}   
					}
				}			
				
				tx.commit();
				forward = exitoModal("messages.updated.success",request);
			}
		}
		catch(Exception e){
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
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;
		
		try {				
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			Vector visibles = formulario.getDatosTablaVisibles(0);
			Vector ocultos = formulario.getDatosTablaOcultos(0);		
			Hashtable miHash = new Hashtable();
			
			//SUBZONA
			if (formulario.getDatos().get("ACCION").toString().equalsIgnoreCase("subzona")) {
				
				ScsSubzonaAdm admBeanSubzona =  new ScsSubzonaAdm(this.getUserBean(request));				
				miHash.put(ScsSubzonaBean.C_IDZONA,(ocultos.get(0)));
				miHash.put(ScsSubzonaBean.C_IDINSTITUCION,(ocultos.get(1)));
				miHash.put(ScsSubzonaBean.C_IDSUBZONA,(ocultos.get(2)));

				// primero obtengo los partidos judiciales que habia para borrarlos
				ScsSubZonaPartidoAdm admZP = new ScsSubZonaPartidoAdm(this.getUserBean(request));
				Hashtable hashWhere = new Hashtable();	
				hashWhere.put(ScsSubZonaPartidoBean.C_IDINSTITUCION,new Integer((String)ocultos.get(1)));
				hashWhere.put(ScsSubZonaPartidoBean.C_IDSUBZONA,new Long((String)ocultos.get(2)));
				hashWhere.put(ScsSubZonaPartidoBean.C_IDZONA,new Long((String)ocultos.get(0)));
				Vector vPartidos = admZP.select(hashWhere);

				// borro los partidos que habia
				int i;
				for (i=0;i<vPartidos.size();i++) {
					ScsSubZonaPartidoBean beanZP = (ScsSubZonaPartidoBean) vPartidos.get(i);
					if (!admZP.delete(beanZP)) {
						//LMS 21/08/2006
						//Cambio por el nuevo uso de subLiteral en SIGAException.
						//SIGAException exc=new SIGAException("messages.err.noseque.datosgenerales.de.alguien");
						//exc.setSubLiteral("messages.censo.gruponoexiste.bbdd");
						//throw new ClsExceptions(admGrupos.getError());
						
						SIGAException exc=new SIGAException("messages.censo.gruponoexiste.bbdd");
					}
				}
				
				tx=usr.getTransaction();
				tx.begin();
				admBeanSubzona.delete(miHash);
				tx.commit();
			}
			//ZONA
			else {
				ScsZonaAdm admBeanZona =  new ScsZonaAdm(this.getUserBean(request));				
				miHash.put(ScsZonaBean.C_IDINSTITUCION,(ocultos.get(0)));
				miHash.put(ScsZonaBean.C_IDZONA,(ocultos.get(1)));
				
				tx=usr.getTransaction();
				tx.begin();
				admBeanZona.delete(miHash);
				tx.commit();
			}			
		}
		catch(Exception e) {
			this.throwExcp("gratuita.listadoZonas.literal.errorBorrado",e,null);
		}			
		return exitoRefresco("messages.deleted.success",request);	
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
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		DefinirZonasSubzonasForm miForm = (DefinirZonasSubzonasForm) formulario;		
		ScsZonaAdm admBean =  new ScsZonaAdm(this.getUserBean(request));
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();

		try {
			miHash = miForm.getDatos();
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			miHash.put("idInstitucionZona",usr.getLocation());
			v = admBean.buscarZonas(miHash);
			request.getSession().setAttribute("resultado",v);
			/* Se almacenan los datos de la búsqueda en DATOSFORMULARIO para cuando se regrese a la pantalla de búsqueda
			 * pueda hacer el refresco
			 */
			miHash.put("BUSQUEDAREALIZADA","1");
			request.getSession().setAttribute("DATOSFORMULARIO",miHash);
			
		}
		catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}		
		return "listarZonas";
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATABACKUP2");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("BUSQUEDAREALIZADA");
		request.getSession().removeAttribute("accion");
		return "inicio";
	}
	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accion");
		return "busqueda";
	}
}