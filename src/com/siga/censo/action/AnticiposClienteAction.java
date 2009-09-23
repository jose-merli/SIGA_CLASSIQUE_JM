/**
 * @version 28/11/2008 (
 */
package com.siga.censo.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.Row;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysAnticipoLetradoAdm;
import com.siga.beans.PysAnticipoLetradoBean;
import com.siga.beans.PysLineaAnticipoAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysServicioAnticipoAdm;
import com.siga.beans.PysServicioAnticipoBean;
import com.siga.beans.PysServiciosInstitucionAdm;
import com.siga.beans.PysServiciosInstitucionBean;
import com.siga.censo.form.AnticiposClienteForm;
import com.siga.censo.form.FacturasClienteForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action del caso de uso ANTICIPOS CLIENTE
 * @author AtosOrigin 28/11/2008
 */
public class AnticiposClienteAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						borrarPaginador(request, paginadorPenstania);
						mapDestino = abrir(mapping, miForm, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("abrirFacturas")){
						// abrir Facturas
						mapDestino = abrirFacturas(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("abrirServicios")){
						// abrir Facturas
						mapDestino = abrirServicios(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarPor")){
						//buscar Servicios
						mapDestino = buscarServicios(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("guardarServicios")){
						//guardar Servicios
						mapDestino = guardarServicios(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("borrarServicios")){
						//borrar Servicios
						mapDestino = borrarServicios(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("abrirOtra")){
						//borrar Servicios
						mapDestino = abrir(mapping, miForm, request, response);
						
					}else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}

	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirOLD(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {

			AnticiposClienteForm form = (AnticiposClienteForm)formulario;
			
			form.setIdPersona(request.getParameter("idPersona"));
			form.setIdInstitucion(request.getParameter("idInstitucion"));
			form.setAccion(request.getParameter("accion"));

			PysAnticipoLetradoAdm anticiposAdm = new PysAnticipoLetradoAdm(this.getUserBean(request));
			
			Vector resultados = anticiposAdm.getDatosAnticipoLetrado(form.getIdInstitucion(),form.getIdPersona());
			
			//Vector resultados = new Vector();
			request.setAttribute("PysAnticipoLetradoResultados",resultados);

			// para saber si es colegiado
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean beanCol = admCol.getDatosColegiales(new Long(form.getIdPersona()), new Integer(form.getIdInstitucion()));
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
			}

			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.colegiado";
			} else 
			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.noColegiado";
			} else 
			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoColegiado = "censo.tipoCliente.colegiadoBaja";
			} 
			request.setAttribute("CenDatosGeneralesColegiado",tipoColegiado);

			// nombre y apellidos
			String nombreApellidos = "";
			Vector v = clienteAdm.getDatosPersonales(new Long(form.getIdPersona()),new Integer(form.getIdInstitucion()));
			if (v!=null && v.size()>0) {
				Hashtable registro =  (Hashtable)v.get(0);
				if (registro.get("NOMBRE")!=null) nombreApellidos += (String) registro.get("NOMBRE") + "&nbsp;"; 
				if (registro.get("APELLIDOS1")!=null && !registro.get("APELLIDOS1").equals("#NA")) nombreApellidos += (String) registro.get("APELLIDOS1") + "&nbsp;"; 
				if (registro.get("APELLIDOS2")!=null) nombreApellidos += (String) registro.get("APELLIDOS2") + "&nbsp;"; 
			}
			request.setAttribute("CenDatosGeneralesNombreApellidos",nombreApellidos);
					    
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }

		return "abrir";
	}
	
	
	/**
	 * Metodo que implementa el modo nuevo 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
		    AnticiposClienteForm form = (AnticiposClienteForm)formulario;
		    form.setAccion("Nuevo");
			form.setIdPersona(request.getParameter("idPersona"));
			form.setIdInstitucion(request.getParameter("idInstitucion"));
			
			GenParametrosAdm admParam = new GenParametrosAdm(this.getUserBean(request));
			form.setCtaContable(admParam.getValor(this.getUserBean(request).getLocation(),"FAC","CONTAB_ANTICIPOS_CLI","438"));
			
			
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }

		return "editar";
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
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UserTransaction t = null;
		try {
			ClsLogging.writeFileLog("--Dentro del metodo insertar anticipo--",10); 
			t = this.getUserBean(request).getTransaction();
			AnticiposClienteForm form = (AnticiposClienteForm)formulario;
			
			// Fijamos los datos del anticipo
			PysAnticipoLetradoBean bean  = new PysAnticipoLetradoBean();
			PysAnticipoLetradoAdm adm    = new PysAnticipoLetradoAdm(this.getUserBean(request));
			t.begin();
			bean.setIdInstitucion(this.getIDInstitucion(request));
			bean.setCtaContable(form.getCtaContable());
			bean.setIdPersona(new Long(form.getIdPersona()));
			bean.setIdAnticipo(adm.getNuevoId(bean));
			ClsLogging.writeFileLog("---obtenemos un nuevo idAnticipo---",10); 
			bean.setImporteInicial( new Double(UtilidadesNumero.redondea(form.getImporteAnticipado(),2)));
			bean.setFecha("sysdate");
			bean.setDescripcion(form.getDescripcion());

			PysAnticipoLetradoAdm admAnticipo = new PysAnticipoLetradoAdm (this.getUserBean(request));
			
			if (!admAnticipo.insert(bean)) {
				throw new ClsExceptions (admAnticipo.getError());
			}
			ClsLogging.writeFileLog("---insertamos un nuevo idAnticipo---",10); 
			t.commit();
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,t);
	   	 }

		return this.exitoModal("messages.inserted.success", request);
	}	
	
	/**
	 * Metodo que implementa el modo borrar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		/*Borra las entrada de PYSANTICIPOLETRADO con determinado ipPersona, idInstitucion e idAnticipo
		 * y sus entradas asociadas en PYSSERVICIOANTICIPO */
		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			AnticiposClienteForm form = (AnticiposClienteForm)formulario;
			
			// Fijamos los datos del anticipo
			PysAnticipoLetradoBean bean  = new PysAnticipoLetradoBean();
			PysAnticipoLetradoAdm adm    = new PysAnticipoLetradoAdm(this.getUserBean(request));
			
			Vector vOcultos = form.getDatosTablaOcultos(0);
			// obtener idpersona
			String idAnticipo = (String)vOcultos.get(2);
			
			bean.setIdInstitucion(this.getIDInstitucion(request));
			bean.setIdPersona(new Long(form.getIdPersona()));
			bean.setIdAnticipo(new Integer(idAnticipo));
			PysAnticipoLetradoAdm admAnticipo = new PysAnticipoLetradoAdm (this.getUserBean(request));
			t.begin();
			if (!admAnticipo.delete(bean)) {
				throw new ClsExceptions (admAnticipo.getError());
			}
			t.commit();

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,t);
	   	 }

		 return this.exitoRefresco("messages.deleted.success", request);
	}
	
	/**
	 * Metodo que implementa el modo editar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
		    AnticiposClienteForm form = (AnticiposClienteForm)formulario;
		    PysAnticipoLetradoAdm adm=new PysAnticipoLetradoAdm(this.getUserBean(request));
		    PysServicioAnticipoAdm admServicios=new PysServicioAnticipoAdm(this.getUserBean(request));
			Vector vOcultos = form.getDatosTablaOcultos(0);
			// obtener idpersona
			String idAnticipo = (String)vOcultos.get(2);
			
			Hashtable b = adm.getDatosAnticipo(form.getIdInstitucion(), form.getIdPersona(), idAnticipo);
		    request.setAttribute("resultado",b);
		    
		    Vector servicios = admServicios.getServiciosAnticipo(form.getIdInstitucion(), form.getIdPersona(), idAnticipo);
		    request.setAttribute("serviciosAnticipo",servicios);
		    
		    form.setAccion("Editar");
		    

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }

		return "editar";
	}
	
	/**
	 * Metodo que implementa el modo modificar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			AnticiposClienteForm form = (AnticiposClienteForm)formulario;
			PysAnticipoLetradoBean bean  = new PysAnticipoLetradoBean();
			t.begin();
			Hashtable hashAnticipo = new Hashtable();
			PysAnticipoLetradoAdm admAnticipo=new PysAnticipoLetradoAdm(this.getUserBean(request));
			hashAnticipo.put(PysAnticipoLetradoBean.C_IDINSTITUCION,form.getIdInstitucion());
			hashAnticipo.put(PysAnticipoLetradoBean.C_IDPERSONA,form.getIdPersona());
			hashAnticipo.put(PysAnticipoLetradoBean.C_IDANTICIPO,form.getIdAnticipo());
			
			Vector vAnticipo = admAnticipo.selectByPK(hashAnticipo);
			bean = (PysAnticipoLetradoBean) vAnticipo.get(0);
				    
			bean.setDescripcion(form.getDescripcion());
			bean.setCtaContable(form.getCtaContable());

			
			
			if (!admAnticipo.updateDirect(bean)) {
				throw new ClsExceptions (admAnticipo.getError());
			}
			t.commit();
	     } 	
		catch (Exception e) {
			 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,t);
	   	 }

		 return exitoModal("messages.updated.success", request);
	}


	/**
	 * Metodo que implementa el modo abrir Facturas
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String abrirFacturas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
		    AnticiposClienteForm form = (AnticiposClienteForm)formulario;
		    PysAnticipoLetradoAdm adm=new PysAnticipoLetradoAdm(this.getUserBean(request));
		    PysLineaAnticipoAdm admLineas=new PysLineaAnticipoAdm(this.getUserBean(request));
			Vector vOcultos = form.getDatosTablaOcultos(0);
			// obtener idpersona
			String idAnticipo = (String)vOcultos.get(2);
			
			// obtener idinstitucion
		    Hashtable ht = adm.getDatosAnticipo(form.getIdInstitucion(), form.getIdPersona(), idAnticipo);
		    request.setAttribute("resultado",ht);
		    
		    Vector lineas = admLineas.getLineasAnticipo(form.getIdInstitucion(), form.getIdPersona(), idAnticipo);
		    request.setAttribute("lineasAnticipo",lineas);
		    
		    
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }

		return "abrirFacturas";
	}

	
	
	
	
	/**
	 * Metodo que implementa el modo ver 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		
		try{	
		    AnticiposClienteForm form = (AnticiposClienteForm)formulario;
		    PysAnticipoLetradoAdm adm=new PysAnticipoLetradoAdm(this.getUserBean(request));
		    PysServicioAnticipoAdm admServicios=new PysServicioAnticipoAdm(this.getUserBean(request));
		    Vector vOcultos = form.getDatosTablaOcultos(0);
			// obtener idpersona
			String idAnticipo = (String)vOcultos.get(2);
			
		    Hashtable b = adm.getDatosAnticipo(form.getIdInstitucion(), form.getIdPersona(),idAnticipo);
		    request.setAttribute("resultado",b);
		    
		    Vector servicios = admServicios.getServiciosAnticipo(form.getIdInstitucion(), form.getIdPersona(), idAnticipo);
		    request.setAttribute("serviciosAnticipo",servicios);
		    
		    form.setAccion("Ver");

		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	 	 }
		 return "editar";
    }
	
	
	
	/**
	 * Metodo que implementa el modo abrir Servicios
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String abrirServicios(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
		    AnticiposClienteForm form = (AnticiposClienteForm)formulario;
		    form.setCategoriaServicio("");
		    form.setTipoServicio("");
		    form.setNombreServicio("");
		    
		    PysAnticipoLetradoAdm adm=new PysAnticipoLetradoAdm(this.getUserBean(request));
		    PysLineaAnticipoAdm admLineas=new PysLineaAnticipoAdm(this.getUserBean(request));
			

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }

		return "abrirServicios";
	}

	
	/**
	 * Metodo que implementa el modo buscar Servicios
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String buscarServicios(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
		    AnticiposClienteForm form = (AnticiposClienteForm)formulario;
		    PysServiciosInstitucionAdm adm=new PysServiciosInstitucionAdm(this.getUserBean(request));
		    
		    String categoriaServicio = form.getCategoriaServicio();
	    	String tipoServicio =	form.getTipoServicio();
    		String nombreServicio = form.getNombreServicio();
		    
		    Vector resultado = adm.getBuscarServiciosAnticipo(categoriaServicio,tipoServicio,nombreServicio);
		    request.setAttribute("resultado", resultado);

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }

		return "buscarServicios";
	}
	

	/**
	 * Metodo que implementa el modo guardarServicios 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String guardarServicios(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UserTransaction t = null;
		AnticiposClienteForm form = (AnticiposClienteForm)formulario;
		PysServicioAnticipoBean servicioBean = new PysServicioAnticipoBean();
		PysServicioAnticipoAdm servicioAdm = new PysServicioAnticipoAdm(this.getUserBean(request));
		String serviciosSeleccionados = form.getServiciosSeleccionados();
		boolean duplicado = false;
		
	    if(serviciosSeleccionados.endsWith("%%")){
	    	int indice = serviciosSeleccionados.lastIndexOf("%%");
	    	serviciosSeleccionados = serviciosSeleccionados.substring(0,indice);
	    }
		
		try {
			
			t = this.getUserBean(request).getTransaction();
			
			//datos=datos + idinstitucion.value + "##" + idtiposervicio.value + "##" + idservicio.value + "##" + idservicioinstitucion.value + "%%";
	
			GstStringTokenizer st1 = new GstStringTokenizer(serviciosSeleccionados,"%%");
		    while (st1.hasMoreTokens()) {
		        Hashtable ht = new Hashtable();
		        String registro = st1.nextToken();
		        int contador = 0;
		        GstStringTokenizer st = new GstStringTokenizer(registro,"##");
			    while (st.hasMoreTokens()) {
			        String campo = st.nextToken();
			        if (contador==0) ht.put(PysServicioAnticipoBean.C_IDINSTITUCION,campo); 
			        else if (contador==1) ht.put(PysServicioAnticipoBean.C_IDTIPOSERVICIOS,campo); 
			        else if (contador==2) ht.put(PysServicioAnticipoBean.C_IDSERVICIO,campo); 
			        else if (contador==3) ht.put(PysServicioAnticipoBean.C_IDSERVICIOSINSTITUCION,campo);
			        else break;
			        contador ++; 
			    }

			    try{
			    	contador=0;
			    	contador++;
			    	ht.put(PysServicioAnticipoBean.C_IDPERSONA,form.getIdPersona());
			    	ht.put(PysServicioAnticipoBean.C_IDANTICIPO,form.getIdAnticipo());
				    servicioBean.setIdInstitucion(new Integer((String)ht.get(PysServiciosInstitucionBean.C_IDINSTITUCION)));
					servicioBean.setIdTipoServicio(new Integer((String)ht.get(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS)));
					servicioBean.setIdServicio(new Integer((String)ht.get(PysServiciosInstitucionBean.C_IDSERVICIO)));
					servicioBean.setIdServiciosInstitucion(new Integer((String)ht.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION)));				
					servicioBean.setIdPersona(new Long(form.getIdPersona()));
					servicioBean.setIdAnticipo(new Integer(form.getIdAnticipo()));

					
					t.begin();
					
					if ((servicioAdm.selectByPK(ht)).size()==0) {
						if (!servicioAdm.insert(servicioBean)) {
							throw new SIGAException (servicioAdm.getError());
						}
					}else{
						duplicado = true;
					}
					t.commit();
					
			    }catch(Exception e){
			    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
			    }
		    }

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }
		 if(duplicado){
			 return this.exito("Servicio duplicado", request);
		 }else{
			 return this.exitoModal("messages.inserted.success", request);
		 }
		 
	}	


	/**
	 * Metodo que implementa el modo borrar Servicios 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrarServicios(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{

		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			AnticiposClienteForm form = (AnticiposClienteForm)formulario;
			
			// Fijamos los datos del anticipo
			PysServicioAnticipoBean bean  = new PysServicioAnticipoBean();
			PysServicioAnticipoAdm adm    = new PysServicioAnticipoAdm(this.getUserBean(request));
			
			Vector vOcultos = form.getDatosTablaOcultos(0);
			// obtener idtiposervicio, idservicio, idservicioinstitucion
			String idAnticipo = (String)vOcultos.get(2); 
			
			bean.setIdInstitucion(this.getIDInstitucion(request));
			bean.setIdPersona(new Long(form.getIdPersona()));
			bean.setIdAnticipo(new Integer(form.getIdAnticipo()));
			String tipoServicio = vOcultos.get(3).toString();
			String Servicio = vOcultos.get(4).toString();
			String ServicioInstitucion = vOcultos.get(5).toString();
			bean.setIdTipoServicio(new Integer (tipoServicio));
			bean.setIdServicio(new Integer(Servicio));
			bean.setIdServiciosInstitucion(new Integer(ServicioInstitucion));
			
			t.begin();
			if (!adm.delete(bean)) {
				throw new ClsExceptions (adm.getError());
			}
			t.commit();

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }

		 return this.exitoRefresco("messages.deleted.success", request);
	}
		
	

	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		// Paginador ->
		AnticiposClienteForm miform = (AnticiposClienteForm) formulario;
		request.setAttribute(ClsConstants.PARAM_PAGINACION, paginadorPenstania);
		
		// String idPersona = request.getParameter("idPersona");
		//String idInstitucion = request.getParameter("idInstitucion");
		String sBajaLogica = miform.getIncluirRegistrosConBajaLogica();
		if (sBajaLogica == null){
			sBajaLogica = request.getParameter("bIncluirRegistrosConBajaLogica");
		}
		boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(sBajaLogica);

		String accion = request.getParameter("accion");
		
		Long idPersona = (Long) request.getAttribute("idPersona");
		if (idPersona == null){
			idPersona = new Long (request.getParameter("idPersona"));
			if (idPersona == null){
				idPersona = (Long) request.getSession().getAttribute("IDPERSONA");
			}
		}
		
		Integer idInstitucion = (Integer) request.getAttribute("idInstitucion");
		if(idInstitucion == null){
			idInstitucion = new Integer(request.getParameter("idInstitucion"));	
			if(idInstitucion == null){
				idInstitucion = (Integer) request.getSession().getAttribute("IDINSTITUCION");
			}
		}
		
		request.getSession().setAttribute("IDPERSONA",idPersona);
		request.getSession().setAttribute("IDINSTITUCION",idInstitucion);
		request.getSession().setAttribute("MODO",accion);
		
		
		try {
		
			HashMap databackup = getPaginador(request, paginadorPenstania);
			if (databackup != null) {
				
				PaginadorBind paginador = (PaginadorBind) databackup
						.get("paginador");
				Vector datos = new Vector();
				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = (String) request.getParameter("pagina");	

                request.setAttribute("bIncluirRegistrosConBajaLogica",""+bIncluirRegistrosConBajaLogica);

				
				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
							// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador
								.getPaginaActual()));
						
					}
					//String idPersona = (String) request.getSession().getAttribute("IDPERSONA");
					//Miramos si es la primera vez que accede a esta pagina, ya que si es asi hay
					//que actualizar los datos pesados ,PRECIO_SERVICIO,SERVICIO_ANTICIPADO y ESTADOPAGO. 
					//Para ello miramos si existe el dato de estadoPago en el primer registro
					//(no miramos) la fecha efectiva ya que puede ser nula
				}
				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {
				Integer anyosMostrados=null;
				if(!bIncluirRegistrosConBajaLogica){
				  anyosMostrados=new Integer(730);
				}
					
				databackup = new HashMap();
				
				PysAnticipoLetradoAdm anticiposAdm = new PysAnticipoLetradoAdm(this.getUserBean(request));
				
				PaginadorBind paginador = anticiposAdm.getConsultaAnticiposPaginador(idInstitucion,idPersona,anyosMostrados);
				// Paginador paginador = new Paginador(sql);
				int totalRegistros = paginador.getNumeroTotalRegistros();
				if (totalRegistros == 0) {
					paginador = null;
				}
				//String idInstitucion = (String) request.getAttribute("idInstitucionPestanha");
				databackup.put("paginador", paginador);
				if (paginador != null) {
					Vector datos = paginador.obtenerPagina(1);
					//datos = actualizarFacturasPaginados(serviciosAdm,new PysProductosSolicitadosAdm(
					//		this.getUserBean(request)),idPersona,this.getUserBean(request),datos);
					databackup.put("datos", datos);
					setPaginador(request, paginadorPenstania, databackup);
				}

			}
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			String nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona,idInstitucion);
			String numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
		
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			Hashtable datosColegiado = new Hashtable();
			datosColegiado.put("NOMBRECOLEGIADO",nombre);
			datosColegiado.put("NUMEROCOLEGIADO",numero);
			request.getSession().setAttribute("DATOSCOLEGIADO", datosColegiado);
			request.setAttribute("accion", accion);
			request.setAttribute("CenDatosGeneralesNombreApellidos", nombre);
			request.setAttribute("CenDatosGeneralesNoColegiado", numero);
			request.getSession().setAttribute("accion", accion);
			request.getSession().setAttribute("CenDatosGeneralesNombreApellidos", nombre);
			request.getSession().setAttribute("CenDatosGeneralesNoColegiado", numero);
			request.setAttribute("bIncluirRegistrosConBajaLogica", new Boolean(bIncluirRegistrosConBajaLogica).toString());

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}

		return "abrir";
	}
	
	public Hashtable completarHashSalida(Hashtable htSalida, Vector vParcial){
		
		if (vParcial!=null && vParcial.size()>0) {
			Hashtable registro = (Hashtable) vParcial.get(0);
			htSalida.putAll(registro);
		}
	
		return htSalida;
		
	}

}

