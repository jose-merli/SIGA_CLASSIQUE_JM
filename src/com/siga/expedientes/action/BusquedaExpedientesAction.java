/*
 * Created on Dec 27, 2004
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.expedientes.ExpPermisosTiposExpedientes;
import com.siga.expedientes.form.BusquedaExpedientesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action para la búsqueda de expedientes (simple y avanzada)
 */
public class BusquedaExpedientesAction extends MasterAction {
	final String separador = "||";
	protected ActionForward executeInternal (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();

//			La primera vez que se carga el formulario 
//			Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				BusquedaExpedientesForm formExp = (BusquedaExpedientesForm)miForm;
				formExp.reset(new String[]{"registrosSeleccionados","datosPaginador"});
				formExp.reset(mapping,request);
				mapDestino = abrir(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("abrirAvanzada")){
				BusquedaExpedientesForm formExp = (BusquedaExpedientesForm)miForm;
				formExp.reset(new String[]{"registrosSeleccionados","datosPaginador"});
				formExp.reset(mapping,request);
				mapDestino = abrirAvanzada(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("buscarInit")){
				miForm.reset(new String[]{"registrosSeleccionados","datosPaginador"});
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("buscarPor")){
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("buscarInitAvanzada")){
				miForm.reset(new String[]{"registrosSeleccionados","datosPaginador"});
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("buscarPorAvanzada")){
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}
			else if (accion.equalsIgnoreCase("generaExcel")){
				mapDestino = generaExcel(mapping, miForm, request, response);
			}else {
				return super.executeInternal(mapping,
				formulario,
				request, 
				response);
			}

//			Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				if (miForm.getModal().equalsIgnoreCase("TRUE"))
				{
					request.setAttribute("exceptionTarget", "parent.modal");
				}


				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}

		} catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}



	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try{

			//Recuperamos el nombre de la institución local
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			CenInstitucionAdm instAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion=instAdm.getNombreInstitucion(userBean.getLocation());
			request.setAttribute("nombreInstitucion",nombreInstitucion);

			// miro a ver si tengo que ejecutar la busqueda una vez presentada la pagina
			// para la acción de volver
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}

		//	para saber en que tipo de busqueda estoy
		request.getSession().setAttribute("volverAuditoriaExpedientes","N"); // busqueda normal
		return("inicio");
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try{
			//	para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("volverAuditoriaExpedientes","A"); // busqueda avanzada

			//Recuperamos el nombre de la institución local
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			CenInstitucionAdm instAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion=instAdm.getNombreInstitucion(userBean.getLocation());
			request.setAttribute("nombreInstitucion",nombreInstitucion);						

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return "avanzada";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/*protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		try{
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;        
			ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm (this.getUserBean(request));
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

			//NOMBRES COLUMNAS PARA LA JOIN
			//Tabla cen_persona
			String P_APELLIDOS1="P."+CenPersonaBean.C_APELLIDOS1;
			String P_APELLIDOS2="P."+CenPersonaBean.C_APELLIDOS2;
			String P_IDPERSONA="P."+CenPersonaBean.C_IDPERSONA;
			String P_NOMBRE="P."+CenPersonaBean.C_NOMBRE;

			//Tabla exp_tipoexpediente
			String P_IDINSTITUCION="T."+ExpTipoExpedienteBean.C_IDINSTITUCION;
			String P_IDTIPOEXPEDIENTE="T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;

			//Tabla cen_institucion
			String I_IDINSTITUCION="I."+CenInstitucionBean.C_IDINSTITUCION;

			//LMS 08/08/2006
			//Se añade la relación con la tabla de permisos de expedientes.
			//Tabla EXP_PERMISOSTIPOSEXPEDIENTES
			String E_IDINSTITUCION="X."+ExpPermisosTiposExpedientesBean.C_IDINSTITUCION;
			String E_IDPERFIL="X."+ExpPermisosTiposExpedientesBean.C_IDPERFIL;
			String E_IDINSTITUCIONTIPOEXPEDIENTE="X."+ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
			String E_IDTIPOEXPEDIENTE="X."+ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE;

			//Valores recogidos del formulario para la búsqueda
			String comboTipoExp = form.getComboTipoExpediente();

			//getComboTipoExpediente nos está devolviendo (idinstitucion,idtipoexpediente)
			String idinstitucion_tipoexpediente = "";
			if (comboTipoExp!=null && !comboTipoExp.equals("")){
				StringTokenizer st = new StringTokenizer(comboTipoExp,",");
				idinstitucion_tipoexpediente=st.nextToken();
				form.setTipoExpediente(st.nextToken());        	
			}else{
				idinstitucion_tipoexpediente="";
				form.setTipoExpediente("");  
			}

			String tipoExpediente = form.getTipoExpediente();
			String institucion = form.getInstitucion();
			String numeroExpediente = form.getNumeroExpediente();
			String anioExpediente = form.getAnioExpediente();
			String numeroExpDisciplinario = form.getNumeroExpDisciplinario();
			String anioExpDisciplinario = form.getAnioExpDisciplinario();
			String fecha = form.getFecha();
			String nombre = form.getNombre();
			String apellido1 = form.getPrimerApellido();
			String apellido2 = form.getSegundoApellido();
			boolean esGeneral=false;
			if (form.getEsGeneral()!=null){
				esGeneral = form.getEsGeneral().equals("S")?true:false;
			}  

			//POR SI SE NECESITA:
			String instMenorRango = "SELECT "+CenInstitucionBean.C_IDINSTITUCION+" FROM "+CenInstitucionBean.T_NOMBRETABLA+" CONNECT BY PRIOR "+CenInstitucionBean.C_IDINSTITUCION+" = "+CenInstitucionBean.C_CEN_INST_IDINSTITUCION+" START WITH "+CenInstitucionBean.C_IDINSTITUCION+" = "+userBean.getLocation();

			String where = "WHERE ";

			//join de las tablas EXPEDIENTE E, TIPOEXPEDIENTE T, PERSONA P
			where += (institucion!=null && !institucion.equals("")) ? " E." + ExpExpedienteBean.C_IDINSTITUCION + " = " + institucion : " E." + ExpExpedienteBean.C_IDINSTITUCION + " IN (" +instMenorRango+ ")";
			where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+P_IDINSTITUCION;
			where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = "+P_IDTIPOEXPEDIENTE;

			if (esGeneral){
				where += " AND T."+ExpTipoExpedienteBean.C_ESGENERAL+"='S'";
			}

			where += " AND E."+ExpExpedienteBean.C_IDPERSONA+" = "+P_IDPERSONA;
			where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = "+I_IDINSTITUCION;

			//campos de búsqueda
			where += (idinstitucion_tipoexpediente!=null && !idinstitucion_tipoexpediente.equals("")) ? " AND E." + ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = " + idinstitucion_tipoexpediente : "";
			where += (tipoExpediente!=null && !tipoExpediente.equals("")) ? " AND E." + ExpExpedienteBean.C_IDTIPOEXPEDIENTE + " = " + tipoExpediente : "";


			where += (numeroExpediente!=null && !numeroExpediente.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(numeroExpediente.trim(),"E." + ExpExpedienteBean.C_NUMEROEXPEDIENTE ): ""; 


			where += (anioExpediente!=null && !anioExpediente.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(anioExpediente.trim(),"E." + ExpExpedienteBean.C_ANIOEXPEDIENTE ): "";

			where += (numeroExpDisciplinario!=null && !numeroExpDisciplinario.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(numeroExpDisciplinario.trim(),"E." + ExpExpedienteBean.C_NUMEXPDISCIPLINARIO ): "";

			where += (anioExpDisciplinario!=null && !anioExpDisciplinario.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(anioExpDisciplinario.trim(),"E." + ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO ): "";
			where += (fecha!=null && !fecha.equals("")) ? " AND " + GstDate.dateBetween0and24h(ExpExpedienteBean.C_FECHA,fecha) : "";

			where += (nombre!=null && !nombre.equals("")) ? " AND  "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),P_NOMBRE ): "";

			where += (apellido1!=null && !apellido1.equals("")) ? " AND  "+ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(),P_APELLIDOS1 ): "";

			where += (apellido2!=null && !apellido2.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido2.trim(),P_APELLIDOS2 ): "";

			if (esGeneral){
				where += " AND DECODE (E."+ExpExpedienteBean.C_IDINSTITUCION+",'"+userBean.getLocation()+"','S',E."+ExpExpedienteBean.C_ESVISIBLE+")='S'";
			}

			//LMS 08/08/2006
			//Se añade el control de permisos sobre el tipo de expediente.
			//where += " AND " + E_IDINSTITUCIONTIPOEXPEDIENTE + " = " + " E." + ExpExpedienteBean.C_IDINSTITUCION;
			where += " AND " + E_IDINSTITUCIONTIPOEXPEDIENTE + " = " + " E." + ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE;
			where += " AND " + E_IDTIPOEXPEDIENTE + " = " + " E." + ExpExpedienteBean.C_IDTIPOEXPEDIENTE;
			where += " AND "+  E_IDINSTITUCION+" = "+"E."+ExpExpedienteBean.C_IDINSTITUCION;
			where += " AND " + E_IDPERFIL + " IN (";

			String[] aPerfiles = userBean.getProfile();

			for (int i=0; i<aPerfiles.length; i++, where+=",")
			{
				where += "'" + aPerfiles[i] + "'";
			}

			where = where.substring(0,where.length()-1);
			where += ")";

			Vector datos = expedienteAdm.selectBusqExp(where);

			request.setAttribute("datos", datos);

			//para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("volverAuditoriaExpedientes","NB"); // busqueda normal
			//obtenemos los permisos a aplicar

			ExpPermisosTiposExpedientes perm=new ExpPermisosTiposExpedientes(userBean);
			request.setAttribute("permisos",perm);

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}

		return "resultado";
	}*/
	protected String buscarInit(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String forward = "resultado";
		try{
			BusquedaExpedientesForm miFormulario = (BusquedaExpedientesForm)formulario; 
			if(miFormulario.getAvanzada()!=null && miFormulario.getAvanzada().equals(ClsConstants.DB_TRUE)){
				//forward = "resultadoAvanzada";
				request.setAttribute("isBusquedaAvanzada",ClsConstants.DB_TRUE);
			}
			
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			
			ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = request.getParameter("Seleccion");
			
			if (seleccionados != null ) {
				ArrayList alRegistros = actualizarSelecionados(seleccionados, clavesRegSeleccinados);
				if (alRegistros != null) {
					clavesRegSeleccinados = alRegistros;
					miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
				}
			}
			
			
			
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null){ 
				Paginador paginador = (Paginador)databackup.get("paginador");
				Vector datos=new Vector();
	
	
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
	
	
	
				if (paginador!=null){	 
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
	
	
	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
	
	
	
	
			}else{
				
				ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm (this.getUserBean(request));
				databackup=new HashMap();
	
				//obtengo datos de la consulta 			
				Paginador resultado = null;
				Vector datos = null;
				
				resultado = expedienteAdm.getPaginadorAvanzadoExpedientes(miFormulario,user);
				// Paso de parametros empleando la sesion
				databackup.put("paginador",resultado);
				
				
				if (resultado!=null){ 
					//Sacanmos las claves para el check multiregistro
					clavesRegSeleccinados = new ArrayList((Collection)expedienteAdm.selectGenericoNLS(resultado.getQueryInicio()));
					//Inciializamos los registros a sin seleccionar
					clavesRegSeleccinados = getClavesBusqueda(clavesRegSeleccinados,false);
					
					
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
					
				} 
				miFormulario.setDatosPaginador(databackup);
				
				//Inicializamos los registros seleccionados
				if (clavesRegSeleccinados != null) {
					miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
				}else{
					if(miFormulario.getRegistrosSeleccionados()==null)
						miFormulario.setRegistrosSeleccionados(new ArrayList());
					
				}
	
				

			}
			//para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("volverAuditoriaExpedientes","NB"); // busqueda normal
			//obtenemos los permisos a aplicar

			ExpPermisosTiposExpedientes perm=new ExpPermisosTiposExpedientes(user);
			request.setAttribute("permisos",perm);
			
			
			
			
			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		return forward;
	}
	protected ArrayList getClavesBusqueda(ArrayList v,boolean isSeleccionado){
		 
		Hashtable aux=new Hashtable();
		ArrayList claves= new ArrayList();

		for (int k=0;k<v.size();k++){
			aux = (Hashtable) v.get(k);
			
			
			String idInstitucion = (String)aux.get(ExpExpedienteBean.C_IDINSTITUCION);
			String idInstitucionTipoExp = (String)aux.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE);
    		String idTipoExp = (String)aux.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE);
			
			String anio = (String)aux.get(ExpExpedienteBean.C_ANIOEXPEDIENTE);
			
			String numero = (String)aux.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE);
			
			String idPersona = (String)aux.get(ExpExpedienteBean.C_IDPERSONA);
			Hashtable aux2 = getIds( idInstitucion , idInstitucionTipoExp, idTipoExp , anio, numero,idPersona);
					
			if(isSeleccionado)
				aux2.put("SELECCIONADO", "1");
			else
				aux2.put("SELECCIONADO", "0");
			claves.add(aux2);	
		}
		
		return claves;
	}
	
	private Hashtable getIds( String idInstitucion ,String idInstitucionTipoExp,String idTipoExp ,
			String anio,String numero, String idPersona){
		Hashtable aux2= new Hashtable();
		StringBuffer clave = new StringBuffer();
		
		//clave.append(idPersona);
		//clave.append(separador);
		clave.append(idInstitucion);
		clave.append(separador);
		clave.append(idInstitucionTipoExp);
		clave.append(separador);
		clave.append(idTipoExp);
		clave.append(separador);
		clave.append(anio);
		clave.append(separador);
		clave.append(numero);
		clave.append(separador);
		clave.append(idPersona);
		
		
		aux2.put("CLAVE",clave.toString());
		//aux2.put(ScsDesignasLetradoBean.C_IDPERSONA,idPersona );
		aux2.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
		aux2.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucionTipoExp);
		aux2.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExp);
		aux2.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anio);
		aux2.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numero);
		aux2.put(ExpExpedienteBean.C_IDPERSONA,idPersona);
		aux2.put("SELECCIONADO", "1");
		return aux2;
	}

	protected ArrayList actualizarSelecionados(String seleccionados, ArrayList alClaves){
		
    	
		String[] aSeleccionados = null;
		if(seleccionados!=null && !seleccionados.equals("")){
			aSeleccionados = seleccionados.split(",");
		}
    	
    	
    	for (int z=0;z<alClaves.size();z++){
	    	Hashtable htclavesBusqueda = (Hashtable)alClaves.get(z);
	    	String clave = (String)htclavesBusqueda.get("CLAVE");
	    	boolean isEncontrado = false;
	    	if( aSeleccionados!=null){
		    	for (int i = 0; i < aSeleccionados.length; i++) {
		    		String registro = aSeleccionados[i];
		    		String[] ids = UtilidadesString.split(registro, separador);
		    		
		    		//String idPersona = ids[0];
		    		String idInstitucion = ids[0];
		    		String idInstitucionTipoExp = ids[1];
		    		String idTipoExp = ids[2];
		    		String anio = ids[3];
		    		String numero = ids[4];
		    		String idPersona = ids[5];
		    		
		    		
		    		
		    		Hashtable aux2= getIds( idInstitucion , idInstitucionTipoExp, idTipoExp , anio, numero,idPersona);
		    		if(clave.equals(aux2.get("CLAVE"))){
		    			htclavesBusqueda.put("SELECCIONADO", "1");
		    			isEncontrado = true;
		    			break;
		    		}
		    		
		    		
				}
	    	}
	    	
	    	if (!isEncontrado){
	    		htclavesBusqueda.put("SELECCIONADO", "0");
	    	}
	    
    	}
    	
    	
    	return alClaves;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/*protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		try{
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;
			ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm (this.getUserBean(request)); 
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        

			//NOMBRES COLUMNAS PARA LA JOIN
			//Tabla cen_persona
			String P_APELLIDOS1="P."+CenPersonaBean.C_APELLIDOS1;
			String P_APELLIDOS2="P."+CenPersonaBean.C_APELLIDOS2;
			String P_IDPERSONA="P."+CenPersonaBean.C_IDPERSONA;
			String P_NOMBRE="P."+CenPersonaBean.C_NOMBRE;

			//Tabla exp_tipoexpediente
			String T_IDINSTITUCION="T."+ExpTipoExpedienteBean.C_IDINSTITUCION;
			String T_IDTIPOEXPEDIENTE="T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE;

			//Tabla cen_institucion
			String I_IDINSTITUCION="I."+CenInstitucionBean.C_IDINSTITUCION;

			//LMS 08/08/2006
			//Se añade la relación con la tabla de permisos de expedientes.
			//Tabla EXP_PERMISOSTIPOSEXPEDIENTES
			String E_IDINSTITUCION="X."+ExpPermisosTiposExpedientesBean.C_IDINSTITUCION;
			String E_IDPERFIL="X."+ExpPermisosTiposExpedientesBean.C_IDPERFIL;
			String E_IDINSTITUCIONTIPOEXPEDIENTE="X."+ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
			String E_IDTIPOEXPEDIENTE="X."+ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE;

			//Tabla exp_parte
			String PA_IDINSTITUCION="PA."+ExpPartesBean.C_IDINSTITUCION;
			String PA_IDINSTITUCION_TIPOEXPEDIENTE="PA."+ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE;
			String PA_IDTIPOEXPEDIENTE="PA."+ExpPartesBean.C_IDTIPOEXPEDIENTE;
			String PA_NUMEROEXPEDIENTE="PA."+ExpPartesBean.C_NUMEROEXPEDIENTE;
			String PA_ANIOEXPEDIENTE="PA."+ExpPartesBean.C_ANIOEXPEDIENTE;
			String PA_IDROL="PA."+ExpPartesBean.C_IDROL;
			String PA_IDPERSONA="PA."+ExpPartesBean.C_IDPERSONA;

			//Valores recogidos del formulario para la búsqueda

			String comboTipoExp = form.getComboTipoExpediente();
			String comboFases = form.getComboFases();
			String comboEstados = form.getComboEstados();

			//getComboTipoExpediente nos está devolviendo (idinstitucion,idtipoexpediente)
			String idinstitucion_tipoexpediente = "";
			if (comboTipoExp!=null && !comboTipoExp.equals("")){
				StringTokenizer st = new StringTokenizer(comboTipoExp,",");
				idinstitucion_tipoexpediente=st.nextToken();
				form.setTipoExpediente(st.nextToken());        	
			}else{
				idinstitucion_tipoexpediente="";
				form.setTipoExpediente("");  
			}

			//getComboFases nos está devolviendo (idinstitucion,idtipoexpediente,idfase)        
			if (comboFases!=null && !comboFases.equals("")){
				StringTokenizer st = new StringTokenizer(comboFases,",");
				st.nextToken();//idinstitucion_tipoexpediente
				st.nextToken();//idtipoexpediente
				form.setFase(st.nextToken());        	
			}else{        	
				form.setFase("");  
			}

			//getComboEstados nos está devolviendo (idinstitucion,idtipoexpediente,idfase,idestado)        
			if (comboEstados!=null && !comboEstados.equals("")){
				StringTokenizer st = new StringTokenizer(comboEstados,",");
				st.nextToken();//idinstitucion_tipoexpediente
				st.nextToken();//idtipoexpediente
				st.nextToken();//idfase
				form.setEstado(st.nextToken());        	
			}else{        	
				form.setEstado("");  
			}

			String tipoExpediente = form.getTipoExpediente();
			String institucion = form.getInstitucion();
			String numeroExpediente = form.getNumeroExpediente();
			String anioExpediente = form.getAnioExpediente();
			String numeroExpDisciplinario = form.getNumeroExpDisciplinario();
			String anioExpDisciplinario = form.getAnioExpDisciplinario();
			String fecha = form.getFecha();
			String nombreDenunciado = form.getNombre();
			String ap1Denunciado = form.getPrimerApellido();
			String ap2Denunciado = form.getSegundoApellido();
			String fase = form.getFase();
			String estado = form.getEstado();
			String nombreParte = form.getNombreParte();
			String ap1Parte = form.getPrimerApellidoParte();
			String ap2Parte = form.getSegundoApellidoParte();
			String rol = form.getRol();
			boolean hayPartes = (!nombreParte.equals("") || !ap1Parte.equals("") || !ap2Parte.equals("") || !rol.equals(""));
			boolean esGeneral = form.getEsGeneral().equals("S")?true:false;

			//POR SI SE NECESITA:
			String instMenorRango = "SELECT "+CenInstitucionBean.C_IDINSTITUCION+" FROM "+CenInstitucionBean.T_NOMBRETABLA+" CONNECT BY PRIOR "+CenInstitucionBean.C_IDINSTITUCION+" = "+CenInstitucionBean.C_CEN_INST_IDINSTITUCION+" START WITH "+CenInstitucionBean.C_IDINSTITUCION+" = "+userBean.getLocation();

			String where = "WHERE ";

			//join de las tablas EXPEDIENTE E, TIPOEXPEDIENTE T, PERSONA P, ESTADOS ES, INSTITUCION I
			where += (institucion!=null && !institucion.equals("")) ? " E." + ExpExpedienteBean.C_IDINSTITUCION + " = " + institucion : " E." + ExpExpedienteBean.C_IDINSTITUCION + " IN (" +instMenorRango+ ")";
			where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+T_IDINSTITUCION+"(+)";
			where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = "+T_IDTIPOEXPEDIENTE+"(+)";

			if (esGeneral){
				where += " AND T."+ExpTipoExpedienteBean.C_ESGENERAL+"='S'";
			}

			where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = "+I_IDINSTITUCION+"(+)";

			if (hayPartes){
				where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = "+PA_IDINSTITUCION+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+PA_IDINSTITUCION_TIPOEXPEDIENTE+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = "+PA_IDTIPOEXPEDIENTE+"(+)";
				where += " AND E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE+" = "+PA_NUMEROEXPEDIENTE+"(+)";
				where += " AND E."+ExpExpedienteBean.C_ANIOEXPEDIENTE+" = "+PA_ANIOEXPEDIENTE+"(+)";
			}

			//campos de búsqueda
			where += (idinstitucion_tipoexpediente!=null && !idinstitucion_tipoexpediente.equals("")) ? " AND E." + ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = " + idinstitucion_tipoexpediente : "";
			where += (tipoExpediente!=null && !tipoExpediente.equals("")) ? " AND E." + ExpExpedienteBean.C_IDTIPOEXPEDIENTE + " = " + tipoExpediente : "";


			where += (numeroExpediente!=null && !numeroExpediente.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(numeroExpediente.trim(),"E." + ExpExpedienteBean.C_NUMEROEXPEDIENTE ): "";


			where += (anioExpediente!=null && !anioExpediente.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(anioExpediente.trim(),"E." + ExpExpedienteBean.C_ANIOEXPEDIENTE): "";

			where += (numeroExpDisciplinario!=null && !numeroExpDisciplinario.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(numeroExpDisciplinario.trim(),"E." + ExpExpedienteBean.C_NUMEXPDISCIPLINARIO): "";

			where += (anioExpDisciplinario!=null && !anioExpDisciplinario.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(anioExpDisciplinario.trim(),"E." + ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO): "";
			//confirmar que el formato de la fecha es correcto cuando se hagan inserts        
			where += (fecha!=null && !fecha.equals("")) ? " AND " + GstDate.dateBetween0and24h(ExpExpedienteBean.C_FECHA,fecha) : "";
			where += (fase!=null && !fase.equals("")) ? " AND E." + ExpExpedienteBean.C_IDFASE + " = " + fase : "";
			where += (estado!=null && !estado.equals("")) ? " AND E." + ExpExpedienteBean.C_IDESTADO + " = " + estado : "";


			String hay_nombre_denunciado = (nombreDenunciado!=null && !nombreDenunciado.equals("")) ?  ComodinBusquedas.prepararSentenciaCompleta(nombreDenunciado.trim(),CenPersonaBean.C_NOMBRE ): "";


			String hay_ap1_denunciado = (ap1Denunciado!=null && !ap1Denunciado.equals("")) ? ComodinBusquedas.prepararSentenciaCompleta(ap1Denunciado.trim(),CenPersonaBean.C_APELLIDOS1 ): "";

			String hay_ap2_denunciado = (ap2Denunciado!=null && !ap2Denunciado.equals("")) ? ComodinBusquedas.prepararSentenciaCompleta(ap2Denunciado.trim(),CenPersonaBean.C_APELLIDOS2 ): "";
			where += (!hay_nombre_denunciado.equals("")) ? " AND E."+ExpExpedienteBean.C_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_nombre_denunciado+")" : "";

			where += (!hay_ap1_denunciado.equals("")) ? " AND E."+ExpExpedienteBean.C_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_ap1_denunciado+")" : "";
			where += (!hay_ap2_denunciado.equals("")) ? " AND E."+ExpExpedienteBean.C_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_ap2_denunciado+")" : "";


			String hay_nombre_parte = (nombreParte!=null && !nombreParte.equals("")) ? ComodinBusquedas.prepararSentenciaCompleta(nombreParte.trim(),CenPersonaBean.C_NOMBRE ): "";

			String hay_ap1_parte = (ap1Parte!=null && !ap1Parte.equals("")) ? ComodinBusquedas.prepararSentenciaCompleta(ap1Parte.trim(),CenPersonaBean.C_APELLIDOS1 ): "";

			String hay_ap2_parte = (ap2Parte!=null && !ap2Parte.equals("")) ? ComodinBusquedas.prepararSentenciaCompleta(ap2Parte.trim(),CenPersonaBean.C_APELLIDOS2 ): "";
			where += (!hay_nombre_parte.equals("")) ? " AND "+PA_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_nombre_parte+")" : "";
			where += (!hay_ap1_parte.equals("")) ? " AND "+PA_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_ap1_parte+")" : "";
			where += (!hay_ap2_parte.equals("")) ? " AND "+PA_IDPERSONA+" IN (SELECT "+CenPersonaBean.C_IDPERSONA+" FROM "+CenPersonaBean.T_NOMBRETABLA+" WHERE "+hay_ap2_parte+")" : "";

			where += (rol!=null && !rol.equals("")) ? " AND " +PA_IDROL+ " = " + rol : "";

			if (esGeneral){
				//where += " AND T."+ExpTipoExpedienteBean.C_ESGENERAL+"='S'";
				where += " AND DECODE (E."+ExpExpedienteBean.C_IDINSTITUCION+",'"+userBean.getLocation()+"','S',E."+ExpExpedienteBean.C_ESVISIBLE+")='S'";
			}

			//LMS 08/08/2006
			//Se añade el control de permisos sobre el tipo de expediente.
			where += " AND " + E_IDINSTITUCIONTIPOEXPEDIENTE + " = " + " E." + ExpExpedienteBean.C_IDINSTITUCION;
			where += " AND " + E_IDTIPOEXPEDIENTE + " = " + " E." + ExpExpedienteBean.C_IDTIPOEXPEDIENTE;
			where += " AND " + E_IDPERFIL + " IN (";

			String[] aPerfiles = userBean.getProfile();

			for (int i=0; i<aPerfiles.length; i++, where+=",")
			{
				where += "'" + aPerfiles[i] + "'";
			}

			where = where.substring(0,where.length()-1);
			where += ")";

			Vector datos = expedienteAdm.selectBusqExpAvda(where,hayPartes);

			request.setAttribute("datos", datos);

			//para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("volverAuditoriaExpedientes","AB"); // busqueda avanzada

			//obtenemos los permisos a aplicar

			ExpPermisosTiposExpedientes perm = new ExpPermisosTiposExpedientes(userBean);
			request.setAttribute("permisos",perm);
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}

		return "resultadoAvanzada";
	}
*/
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		return mostrarRegistro(mapping,formulario,request,response,true,false);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		return mostrarRegistro(mapping,formulario,request,response,false,false);
	}


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		try{
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;

//			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			ExpExpedienteAdm expAdm = new ExpExpedienteAdm (this.getUserBean(request));

			Vector vOcultos = form.getDatosTablaOcultos(0);
//			String idInstitucion = (String)vOcultos.elementAt(0);

			Hashtable hash = new Hashtable();

			hash.put(ExpExpedienteBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
			hash.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, (String)vOcultos.elementAt(1));
			hash.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE, (String)vOcultos.elementAt(2));	    
			hash.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE, (String)vOcultos.elementAt(3));
			hash.put(ExpExpedienteBean.C_ANIOEXPEDIENTE, (String)vOcultos.elementAt(4));


			/*if (expAdm.delete(hash)){
	        	return exitoRefresco("messages.deleted.success",request);
	        }else{
	        	return exito("messages.deleted.error",request);
	        }*/

			expAdm.delete(hash);

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}

		return exitoRefresco("messages.deleted.success",request);
	}	

	/** 
	 * Funcion que muestra el formulario en modo consulta o edicion
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @param  bEditable
	 * @param  bNuevo 
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws SIGAException{

		try{
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;	
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   						

			if (!bNuevo){
//				Vector vVisibles = form.getDatosTablaVisibles(0);
				Vector vOcultos = form.getDatosTablaOcultos(0);		

				String idInstitucion = (String)vOcultos.elementAt(0);
				String idInstitucion_TipoExpediente = (String)vOcultos.elementAt(1);
				String idTipoExpediente = (String)vOcultos.elementAt(2);
				String numExpediente = (String)vOcultos.elementAt(3);
				String anioExpediente = (String)vOcultos.elementAt(4);
				String nombreTipoExpediente = UtilidadesString.mostrarDatoJSP((String)vOcultos.elementAt(5));

				//Si se intenta editar un expediente de otra institucion,
				//sólo se permitirá modificar las anotaciones (pestanha de seguimiento)
				String soloSeguimiento = "false";
				if (bEditable){
					soloSeguimiento = (!userBean.getLocation().equals(idInstitucion))?"true":"false";	        	
				}

				//Anhadimos parametros para las pestanhas
				Hashtable htParametros=new Hashtable();
				htParametros.put("idInstitucion",idInstitucion);
				htParametros.put("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
				htParametros.put("idTipoExpediente",idTipoExpediente);
				htParametros.put("numeroExpediente",numExpediente);
				htParametros.put("anioExpediente",anioExpediente);
				htParametros.put("nombreTipoExpediente",nombreTipoExpediente);
				htParametros.put("editable", bEditable ? "1" : "0");
				htParametros.put("accion", bEditable ? "edicion" : "consulta");	
				htParametros.put("soloSeguimiento",soloSeguimiento);

				request.setAttribute("expediente", htParametros);

				//Recuperamos las pestanhas ocultas para no mostrarlas
				ExpCampoTipoExpedienteAdm campoAdm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));
				String[] pestanasOcultas = campoAdm.obtenerPestanasOcultas(idInstitucion_TipoExpediente,idTipoExpediente);
				
				request.setAttribute("pestanasOcultas",pestanasOcultas);
				request.setAttribute("idTipoExpediente",idTipoExpediente);
				request.setAttribute("idInstitucionTipoExpediente",idInstitucion_TipoExpediente);
				
				

				// Metemos los datos no editables del expediente en Backup.
				//Los datos particulares se anhadirán a la HashMap en cada caso.
				HashMap datosExpediente = new HashMap();
				Hashtable datosGenerales = new Hashtable();
				datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
				datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
				datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
				datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
				datosExpediente.put("datosGenerales",datosGenerales);
				request.getSession().setAttribute("DATABACKUP",datosExpediente);


			}else{
				String idInstitucion_TipoExpediente = request.getParameter("idInst");
				String idTipoExpediente = request.getParameter("idTipo");
//				String nombreTipoExpediente = request.getParameter("nombreTipo");
				Hashtable htParametros=new Hashtable();
				htParametros.put("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
				htParametros.put("idTipoExpediente",idTipoExpediente);
				htParametros.put("editable", "1");	
				htParametros.put("accion", "nuevo");
				htParametros.put("soloSeguimiento", "false");

				request.setAttribute("expediente", htParametros);
			}

			String nuevo = bNuevo?"true":"false";
			request.setAttribute("nuevo", nuevo);	
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}


		return "editar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException { 	


		return mostrarRegistro(mapping,formulario,request,response,true,true);

	}
	protected String generaExcel(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		
        Vector datos = new Vector();
		
		
		
		try {
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;
	        
			
			ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm (this.getUserBean(request));
			
			
			for (int i = 0; i < form.getDatosTabla().size(); i++) {
				Vector vCampos = form.getDatosTablaOcultos(i);
			
		    	
		    
		    
			    	String idInstitucion = (String) vCampos.get(0);
					String idInstitucionTipoExp =(String) vCampos.get(1);
		    		String idTipoExp = (String) vCampos.get(2);
					String anio = (String) vCampos.get(3);
					String numero = (String) vCampos.get(4);
					datos.addAll(expedienteAdm.getDatosInformeExpediente(idInstitucion, idInstitucionTipoExp, idTipoExp, anio, numero, null, false, false));
		    
		    	
		    
	    	}
	        String[] cabeceras = null;
	        String[] campos = null;
	      
				cabeceras = new String[]{"IDINSTITUCION",
						"IDINSTITUCION_TIPOEXPEDIENTE",
						"IDTIPOEXPEDIENTE",
						"ANIOEXPEDIENTE",
						"NUMEROEXPEDIENTE",
						"FECHAINICIO",
						"ASUNTO",
						"ALERTAGENERADA",
						"ESVISIBLE",
						"ESVISIBLEENFICHA",
						"SANCIONADO",
						"SANCIONPRESCRITA",
						"ACTUACIONESPRESCRITAS",
						"SANCIONFINALIZADA",
						"ANOTACIONESCANCELADAS",
						"ANIOEXPDISCIPLINARIO",
						"NUMEXPDISCIPLINARIO",
						"NUMASUNTO",
						"FECHAINICIALESTADO",
						"FECHAFINALESTADO",
						"FECHAPRORROGAESTADO",
						"DESCRIPCIONRESOLUCION",
						"FECHARESOLUCION",
						"FECHACADUCIDAD",
						"OBSERVACIONES",
						"MINUTA",
						"IDPERSONA",
						"PER_NOMBRE",
						"PER_APELLIDOS1",
						"PER_APELLIDOS2",
						"PER_NIFCIF",
						"TE_NOMBRE",
						"TE_ESGENERAL",
						"CLA_NOMBRE",
						"FASE_NOMBRE",
						"EST_NOMBRE",
						"EST_ESEJECUCIONSANCION",
						"EST_ESFINAL",
						"EST_ESAUTOMATICO",
						"EST_DESCRIPCION",
						"EST_IDFASE_SIGUIENTE",
						"EST_IDESTADO_SIGUIENTE",
						"EST_MENSAJE",
						"EST_PRE_SANCIONADO",
						"EST_PRE_VISIBLE",
						"EST_PRE_VISIBLEENFICHA",
						"EST_POST_ACTPRESCRITAS",
						"EST_POST_SANCIONPRESCRITA",
						"EST_POST_SANCIONFINALIZADA",
						"EST_POST_ANOTCANCELADAS",
						"EST_POST_VISIBLE",
						"EST_POST_VISIBLEENFICHA",
						"IVA_DESCRIPCION",
						"RES_DESCRIPCION",
						"RES_CODIGOEXT",
						"RES_BLOQUEADO",
						"JUZ_NOMBRE",
						"JUZ_DOMICILIO",
						"JUZ_CODIGOPOSTAL",
						"JUZ_IDPOBLACION",
						"JUZ_IDPROVINCIA",
						"JUZ_POBLACION",
						"JUZ_PROVINCIA",
						"JUZ_TELEFONO1",
						"JUZ_TELEFONO2",
						"JUZ_FAX1",
						"JUZ_FECHABAJA",
						"JUZ_CODIGOEXT",
						"JUZ_CODIGOPROCURADOR",
						"PROC_NOMBRE",
						"PROC_PRECIO",
						"PROC_IDJURISDICCION",
						"PROC_CODIGO",
						"PROC_COMPLEMENTO",
						"PROC_VIGENTE",
						"PROC_ORDEN",
						"PRETENSION",
						"OTRASPRETENSIONES",
						"CAMPOCONF11",
						"CAMPOCONF12",
						"CAMPOCONF13",
						"CAMPOCONF14",
						"CAMPOCONF15",
						"CAMPOCONF21",
						"CAMPOCONF22",
						"CAMPOCONF23",
						"CAMPOCONF24",
						"CAMPOCONF25"};	
				campos = new String[]{"IDINSTITUCION",
						"IDINSTITUCION_TIPOEXPEDIENTE",
						"IDTIPOEXPEDIENTE",
						"ANIOEXPEDIENTE",
						"NUMEROEXPEDIENTE",
						"FECHAINICIO",
						"ASUNTO",
						"ALERTAGENERADA",
						"ESVISIBLE",
						"ESVISIBLEENFICHA",
						"SANCIONADO",
						"SANCIONPRESCRITA",
						"ACTUACIONESPRESCRITAS",
						"SANCIONFINALIZADA",
						"ANOTACIONESCANCELADAS",
						"ANIOEXPDISCIPLINARIO",
						"NUMEXPDISCIPLINARIO",
						"NUMASUNTO",
						"FECHAINICIALESTADO",
						"FECHAFINALESTADO",
						"FECHAPRORROGAESTADO",
						"DESCRIPCIONRESOLUCION",
						"FECHARESOLUCION",
						"FECHACADUCIDAD",
						"OBSERVACIONES",
						"MINUTA",
						"IDPERSONA",
						"PER_NOMBRE",
						"PER_APELLIDOS1",
						"PER_APELLIDOS2",
						"PER_NIFCIF",
						"TE_NOMBRE",
						"TE_ESGENERAL",
						"CLA_NOMBRE",
						"FASE_NOMBRE",
						"EST_NOMBRE",
						"EST_ESEJECUCIONSANCION",
						"EST_ESFINAL",
						"EST_ESAUTOMATICO",
						"EST_DESCRIPCION",
						"EST_IDFASE_SIGUIENTE",
						"EST_IDESTADO_SIGUIENTE",
						"EST_MENSAJE",
						"EST_PRE_SANCIONADO",
						"EST_PRE_VISIBLE",
						"EST_PRE_VISIBLEENFICHA",
						"EST_POST_ACTPRESCRITAS",
						"EST_POST_SANCIONPRESCRITA",
						"EST_POST_SANCIONFINALIZADA",
						"EST_POST_ANOTCANCELADAS",
						"EST_POST_VISIBLE",
						"EST_POST_VISIBLEENFICHA",
						"IVA_DESCRIPCION",
						"RES_DESCRIPCION",
						"RES_CODIGOEXT",
						"RES_BLOQUEADO",
						"JUZ_NOMBRE",
						"JUZ_DOMICILIO",
						"JUZ_CODIGOPOSTAL",
						"JUZ_IDPOBLACION",
						"JUZ_IDPROVINCIA",
						"JUZ_POBLACION",
						"JUZ_PROVINCIA",
						"JUZ_TELEFONO1",
						"JUZ_TELEFONO2",
						"JUZ_FAX1",
						"JUZ_FECHABAJA",
						"JUZ_CODIGOEXT",
						"JUZ_CODIGOPROCURADOR",
						"PROC_NOMBRE",
						"PROC_PRECIO",
						"PROC_IDJURISDICCION",
						"PROC_CODIGO",
						"PROC_COMPLEMENTO",
						"PROC_VIGENTE",
						"PROC_ORDEN",
						"PRETENSION",
						"OTRASPRETENSIONES",
						"CAMPOCONF11",
						"CAMPOCONF12",
						"CAMPOCONF13",
						"CAMPOCONF14",
						"CAMPOCONF15",
						"CAMPOCONF21",
						"CAMPOCONF22",
						"CAMPOCONF23",
						"CAMPOCONF24",
						"CAMPOCONF25"};
			
			
				//,"COMUNICACIONES"};
			request.setAttribute("campos",campos);
			request.setAttribute("datos",datos);
			request.setAttribute("cabeceras",cabeceras);
			request.setAttribute("descripcion", user.getLocation()+"_"+this.getUserName(request).toString());
						
			
		} 
		catch (Exception e) { 
			
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return "generaExcel";
	}
	
	
}
