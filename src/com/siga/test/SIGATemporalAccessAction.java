package com.siga.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.CenInstitucion;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.services.cen.CenInstitucionService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.util.PropertyReader;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.pra.core.filters.security.helper.UsuariosTO;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.administracion.SIGAGestorInterfaz;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenInstitucionLenguajesAdm;
import com.siga.beans.CenInstitucionLenguajesBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGACGAEContextCAS;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class SIGATemporalAccessAction extends Action
{
	public SIGATemporalAccessAction()
	{ }

	public ActionForward execute(ActionMapping mapping, 
	        					 ActionForm form, 
	        					 HttpServletRequest request, 
	        					 HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		final String ENTORNO_PREPRODUCCION ="PREPRODUCCION";
		final String ENTORNO_DESARROLLO ="DESARROLLO";
		final String ENTORNO_INTEGRACION="INTEGRACION";
		
		ReadProperties rproperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String entorno = rproperties.returnProperty("administracion.login.entorno");
		boolean desarrollo = entorno.equalsIgnoreCase(ENTORNO_PREPRODUCCION)||entorno.equalsIgnoreCase(ENTORNO_DESARROLLO)||entorno.equalsIgnoreCase(ENTORNO_INTEGRACION);
		
		String result="";
//		String user=request.getParameter("user");
		String location = request.getParameter("location");
		String menuPosition=request.getParameter("posMenu");
		String sAccess=request.getParameter("access");
		String profile=request.getParameter("profile");
		String profileArray[] = profile.split (",");
		String letrado=request.getParameter("letrado");
		UsrBean usrbean = UsrBean.UsrBeanAutomatico(location);
		// Ya no tenemos certificadoy tendremos que rellenarlo a partir de los datos que tengamos de CAS
		// PERO SOLO EN CASO DE QUE TENGA ROL DE ADMIN
		UsuariosTO certificado = (UsuariosTO) request.getAttribute("USUARIOTO");
		boolean accesoAdmin=true;
		if(!desarrollo){
			String roles=(String)request.getHeader("CAS-roles");
			accesoAdmin=roles.contains("SIGA-Admin");
		}
		
 		if(accesoAdmin&&!desarrollo){
			certificado = new UsuariosTO();
			// Si tiene el rol de SIGA-Admin esta autorizado a entrar por combos
			certificado.setUsu_nif((String)request.getHeader("CAS-username"));
			certificado.setPfiNombre((String)request.getHeader("CAS-nickname"));
			
		}else if(!desarrollo){
			System.out.println("Acceso denegado: !desarrollo en temporal ");
			return mapping.findForward("accesodenegado");
		}

		///////////////////////////////////////////////////
		// Verficamos si el usuario es dado de alta en la insticion seleccionada en los combos
		// Tambien se establece el idUsuario en el UsrBean
		try {
			this.checkCertificadoEnInstitucion_Y_FijaUsuarioEnUsrBean(usrbean, certificado);
		}
		catch (Exception e) {

			String mensaje = "";
			if (e instanceof SIGAException) mensaje = ((SIGAException)e).getLiteral();
			else                            mensaje = e.getMessage();
						
			request.setAttribute("mensaje",mensaje);
			// para pruebas desde local, comentar este return
			System.out.println("Acceso denegado:!checkCertificadoEnInstitucion_Y_FijaUsuarioEnUsrBean");
			return mapping.findForward("accesodenegado");
		}
		///////////////////////////////////////////////////

		// obtengo datos para el userbean
		// para pruebas desde local, comentar esta linea y descomentar la siguiente
		// en la que habra que poner el idusuario del certificado del que prueba
		usrbean.setIdPersona(obtenerIdPersona(location, usrbean));
		//usrbean.setUserName("2048");
		Vector v = obtenerUsuario(location, usrbean);
		AdmUsuariosBean usu = (AdmUsuariosBean) v.get(0);
		CenPersonaBean per = null; 
		CenClienteBean cli = null; 
		if (v.size()>1) {
			per = (CenPersonaBean) v.get(1);
		}
		if (v.size()>2) {
			cli = (CenClienteBean) v.get(2);
		}
		
		//usrbean.setIdRol("CNE");
		// rgg cambio de codigos
		usrbean.setIdRol("2");
		
		//usrbean.setAccessType("FULL");
		//usrbean.setAccessType(sAccess);
		

		usrbean.setProfile(profileArray);
		
		//Comprobamos si es comision multiple
		//Comprobamos si es comision multiple
		BusinessManager bm = BusinessManager.getInstance();
		CenInstitucionService cenInstitucionService = (CenInstitucionService)bm.getService(CenInstitucionService.class);
		CenInstitucion cenInstitucion = new CenInstitucion();
		cenInstitucion.setIdinstitucion(Short.valueOf(location));
		CenInstitucion comision =  cenInstitucionService.getComision(cenInstitucion);
		usrbean.setIdInstitucionComision(comision.getIdinstitucion());
		if(usrbean.isComision()){
			List<CenInstitucion> instituciones =  cenInstitucionService.getInstitucionesComision(comision);
			if( instituciones!=null && instituciones.size()>0 ){
				Short[] institucionesComision  = new Short[instituciones.size()];
				for (int i = 0; i < instituciones.size(); i++) {
					institucionesComision[i]= instituciones.get(i).getIdinstitucion();
				}
				usrbean.setInstitucionesComision(institucionesComision);
			}else{
				usrbean.setInstitucionesComision(new Short[]{Short.parseShort(location)});
			}
			usrbean.setLocation(""+comision.getIdinstitucion());
		}else{
			
			usrbean.setLocation(location);
		}
		usrbean.setLetrado(letrado.equals("S")?true:false);
		//usrbean.setComision(profile.equalsIgnoreCase("CJG")?true:false); //Con la nueva forma de entrar se debe hacer un contains
		//usrbean.setComision(profile.contains("CJG")?true:false);
		
		// obtengo el idioma de la institucion
		String idLenguajeInstitucion = "1";
		Hashtable ht2 = new Hashtable();
		ht2.put(CenInstitucionBean.C_IDINSTITUCION,location);
		CenInstitucionAdm ins = new CenInstitucionAdm(usrbean);
		Vector v4 = ins.selectByPK(ht2);
		Integer idConsejo = ClsConstants.INSTITUCION_CGAE;
		if (v4!=null && v4.size()>0) {
			CenInstitucionBean in = (CenInstitucionBean) v4.get(0);
			idLenguajeInstitucion=in.getIdLenguaje(); 
			idConsejo = in.getCen_inst_idInstitucion();
		}		
		usrbean.setIdConsejo(idConsejo);
		// Marcamos que ha entrado por combos (superusuarios)
		usrbean.setEntradaCombos(true);
		
		// obtengo el idioma del usuario
		String sInsti="";
		String idLenguaje = null; 
		String idLenguajeExt = null; 
		// Si es cliente, obtenemos su idioma, si no, lo obtenemos del definido internamente en el usuario
		idLenguaje = cli!=null ? cli.getIdLenguaje() : usu.getIdLenguaje();

		{	// Verficamos si el idioma del usuario esta traducido
			CenInstitucionLenguajesAdm admLen = new CenInstitucionLenguajesAdm (usrbean);
			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, CenInstitucionLenguajesBean.C_IDINSTITUCION, location);
			UtilidadesHash.set(h, CenInstitucionLenguajesBean.C_IDLENGUAJE, idLenguaje);
			Vector vLen = admLen.selectByPK(h);
			if (vLen == null || vLen.size() != 1) {
				// El idoma del lenguaje no esta traducido, ponemos el idioma de la institucion
				idLenguaje = idLenguajeInstitucion;
			}
		}

		if (idLenguaje!=null) {
			Hashtable ht = new Hashtable();
			ht.put(AdmLenguajesBean.C_IDLENGUAJE,idLenguaje);
			AdmLenguajesAdm len = new AdmLenguajesAdm(usrbean);
			Vector v3 = len.selectByPK(ht);
			if (v3!=null && v3.size()>0) {
				AdmLenguajesBean l = (AdmLenguajesBean) v3.get(0);
				idLenguajeExt = l.getCodigoExt();
			}
		}
		
		usrbean.setLanguage(idLenguaje);
		usrbean.setLanguageExt(idLenguajeExt);
		usrbean.setLanguageInstitucion(idLenguajeInstitucion);
		
		HttpSession ses= request.getSession();
		ses.setAttribute(Globals.LOCALE_KEY, new java.util.Locale(idLenguajeExt.toLowerCase(), "es"));

		//ses.setAttribute(Globals.LOCALE_KEY, new java.util.Locale("es", "es"));
		ClsLogging.writeFileLog("LENGUAJE "+sInsti+" = " + idLenguaje + " ("+idLenguajeExt+")",7);
		
		usrbean.setUserDescription("USUARIO DE PRUEBAS");
		ses.setAttribute("USRBEAN", usrbean);
		if (ses.getAttribute(PARAMETRO.LONGITUD_CODEJG.toString()) == null) {
			GenParametrosService genParametrosService = (GenParametrosService) bm.getService(GenParametrosService.class);
			GenParametros genParametros = new GenParametros();
			genParametros.setIdinstitucion(cenInstitucion.getIdinstitucion());
			genParametros.setModulo(MODULO.SCS.toString());
			genParametros.setParametro(PARAMETRO.LONGITUD_CODEJG.toString());
			genParametros = genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
			if (genParametros != null && genParametros.getValor() != null) {
				ses.setAttribute(PARAMETRO.LONGITUD_CODEJG.toString(), genParametros.getValor());
				ClsLogging.writeFileLog("Tamaño EJGs:" + genParametros.getValor(), 1);
			}
		}
		initStyles(location, ses);
		
		// RGG 13/01/2007 cambio para obtener IP
		String IPServidor = UtilidadesString.obtenerIPServidor(request); 
		request.getSession().setAttribute("IPSERVIDOR",IPServidor);
		ClsLogging.writeFileLog("IP DEL SERVIDOR="+IPServidor,7);
	 	
		if(menuPosition.equals("1"))
		{
			result="leftMenu";
			ses.setAttribute(SIGAConstants.MENU_POSITION_REF, SIGAConstants.MENU_LEFT); 
		}
		else
		{
		    result="topMenu";
			ses.setAttribute(SIGAConstants.MENU_POSITION_REF, SIGAConstants.MENU_TOP);
		}
		
		String idsession = (String)ses.getId();
		long fcses = ses.getCreationTime();
        if (idsession != null && idsession.length() > 0)
        {
        	SimpleDateFormat fm = new SimpleDateFormat("ddMMyyyyhhmmssSSS");
        	String fecha = fm.format(new Date(fcses));
        	String idSesion = idsession.substring(0, 5)+fecha;
            // Put the principal's name into the message diagnostic
            // context. May be shown using %X{username} in the layout
            // pattern.
        	MDC.put("idSesion", idSesion);
            
        }
        
        /*
         * Obtenemos la versión de SIGA.
         * A partir de ahora con despliegues desde Jenkins este dato se almacena en ficheros .properties.
         */
        
        try{
        	GenParametrosAdm paramAdm = new GenParametrosAdm((UsrBean)ses.getAttribute("USRBEAN"));
        	
        	ResourceBundle rb = ResourceBundle.getBundle("versionSIGA");
        	String version = rb.getString("version");
        	String proyecto = rb.getString("proyecto");
        	
        	String entornoDespliegue = (proyecto == null ? "SIGA" : proyecto) + "_" + paramAdm.getValor("0", "ADM", SIGAConstants.PARAMETRO_ENTORNO, "");
        	
        	version = (version == null ? (entornoDespliegue) : (entornoDespliegue + "_" + version));
   
        	request.setAttribute("versionSiga", (version == null ? "" : version));
        }catch (Exception e){
        	request.setAttribute("versionSiga", "");
        	//ClsLogging.writeFileLog("Error al obtener la versión de SIGA desplegada.", 1);
        }
        
		return mapping.findForward(result);
	}	

	/*
	 * Verifica si el usuario del certificado esta dado de alta en la institucion del 
	 * usrbean (la seleccionada pantalla en combos)
	 * Si no esta, se inserta en dicha institucion
	 */
	public String checkCertificadoEnInstitucion_Y_FijaUsuarioEnUsrBean (UsrBean usrBean, UsuariosTO certificado) throws Exception 
	{
		String sql = "";
		
		if (certificado != null) {
			String nif = certificado.getUsu_nif().toUpperCase();

			// Hacemos la comparacion del NIF sin los 0 por la izquierda
			sql = " WHERE LTRIM (" + AdmUsuariosBean.C_NIF + ",'0') = '" + UtilidadesString.LTrim(nif, "0") + "' ";
		}
		else {

			if(usrBean.getLocation().equals("2011")){
				//Para Baleares usuario de Jorge Paez
				sql = " WHERE " + AdmUsuariosBean.C_IDUSUARIO + " = 1342 ";
			}else{
				// Usamos el Usuario comodin para pruebas (Entorno Local)
				sql = " WHERE " + AdmUsuariosBean.C_IDUSUARIO + " = 1 ";
				
			}
		}
		
        sql += " AND " + AdmUsuariosBean.C_IDINSTITUCION + " = " + usrBean.getLocation();  
		
		AdmUsuariosAdm admUsu = new AdmUsuariosAdm(usrBean);
		Vector v = admUsu.select(sql);
		AdmUsuariosBean admBean = null;

		
		// Si existe el usuario
		if (v != null && v.size() == 1) {
			admBean = (AdmUsuariosBean)v.get(0);
		}
		else {
			if (v == null || v.size() == 0) {
				// Si no existe el usuario, el certificado no esta registrado
				
				admBean = insertarUsuarioEnInstitucion (usrBean, certificado.getPfiNombre(), certificado.getUsu_nif().toUpperCase());
				
//				this.printTraza("Certificado no registrado --> Mostramos el mensaje");
//				throw new SIGAException("messages.general.errorCertificadoNoRegistrado");
			}
			else {
				// Hay mas de uno usario con el mismo nif
				throw new SIGAException("messages.general.errorUsuarioDuplicado");
			}
		}
		if (!admBean.getActivo().equalsIgnoreCase("S")) {
			throw new SIGAException("messages.general.errorUsuarioDesactivado");
		}
		
		// Guardo el nombre y el idUsuario
		usrBean.setUserName("" + admBean.getIdUsuario());
		usrBean.setUserDescription(admBean.getDescripcion());
		return usrBean.getUserName();
	}
	
	private AdmUsuariosBean insertarUsuarioEnInstitucion (UsrBean usrBean, String nombre, String nif) throws Exception 
	{
		AdmUsuariosBean beanUsuario = new AdmUsuariosBean();

		try {
			// Obtenemos el nuevo idusuario
			String sql = " select max( " + AdmUsuariosBean.C_IDUSUARIO+") as MAXIDUSER " +
					       " from " + AdmUsuariosBean.T_NOMBRETABLA +
						  " where " + AdmUsuariosBean.C_IDINSTITUCION + " = " + usrBean.getLocation();

			RowsContainer o = new RowsContainer();
			if (o.findForUpdate(sql)) {
				Vector vecto=o.getAll();
				if (vecto==null || vecto.size()==0) {
					beanUsuario.setIdUsuario(new Integer("0"));
				} 
				else {
					Row row=(Row)vecto.elementAt(0);
					String nuevoIdUsuario = row.getString("MAXIDUSER");
					if (nuevoIdUsuario==null || nuevoIdUsuario.trim().equals("")) {
					    nuevoIdUsuario = "0";
					}

					Integer value = new Integer(nuevoIdUsuario);
					beanUsuario.setIdUsuario(new Integer(value.intValue()+1));
				}
			}

			beanUsuario.setActivo("S");
			beanUsuario.setFechaAlta("SYSDATE");
			beanUsuario.setFechaMod("SYSDATE");
			beanUsuario.setIdInstitucion(new Integer(usrBean.getLocation()));
			beanUsuario.setIdLenguaje(ClsConstants.LENGUAJE_ESP);
			beanUsuario.setDescripcion(nombre);
			beanUsuario.setNIF(nif);
			beanUsuario.setUsuMod(new Integer("-1"));

			AdmUsuariosAdm admUsuarios = new AdmUsuariosAdm(usrBean);
			admUsuarios.insert(beanUsuario);
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return beanUsuario;
	}
	
	private long obtenerIdPersona(String sIdInstitucion, UsrBean usr)
	{
	    long idPersona=-1;
	    try
	    {
		    String sWHERE = " WHERE " + AdmUsuariosBean.C_IDUSUARIO + " = " +  usr.getUserName() + 
                       	      " AND " + AdmUsuariosBean.C_IDINSTITUCION + " = " + sIdInstitucion;
		    
		    AdmUsuariosAdm admUsuario = new AdmUsuariosAdm(usr);
		    Vector vUsuario = admUsuario.select(sWHERE);
		    
		    if (vUsuario!=null && vUsuario.size()>0)
		    {
		        AdmUsuariosBean beanUsuario = (AdmUsuariosBean)vUsuario.elementAt(0);
		        String sNIF = beanUsuario.getNIF();
		        
		        // Hacemos la comparacion del NIF sin los 0 por la izquierda
		        sWHERE = " WHERE ltrim(upper(" + CenPersonaBean.C_NIFCIF + "),'0') = '" + UtilidadesString.LTrim(sNIF.toUpperCase(),"0") + "' ";

		        CenPersonaAdm admPersona = new CenPersonaAdm(usr);
		        Vector vPersona = admPersona.select(sWHERE);
		        if (vPersona!=null && vPersona.size()>0)
		        {
		        	//aalg: se tiene que almacenar el idpersona sólo si se trata de un cliente del colegio al que se accede
		        	CenPersonaBean beanPersona = (CenPersonaBean)vPersona.elementAt(0);
		        	if (CenClienteAdm.getEsCliente(beanPersona.getIdPersona().toString(), sIdInstitucion).equalsIgnoreCase("S"))
		        		idPersona = beanPersona.getIdPersona().longValue();
		        	else
		        		ClsLogging.writeFileLog("***** ES PERSONA PERO NO CLIENTE DE ESTE COLEGIO. USRBEAN CONTIENE IDPERSONA=-1 *****",1);
		        }
		        else {
		        	ClsLogging.writeFileLog("***** NO SE HA PODIDO OBTENER EL IDPERSONA. USRBEAN CONTIENE IDPERSONA=-1 *****",1);
		        }
		    }
		    else  {
		    	ClsLogging.writeFileLog("***** NO SE HA PODIDO OBTENER EL IDPERSONA. USRBEAN CONTIENE IDPERSONA=-1 *****",1);
		    }
	    }
	    catch(Exception e) {
	    	ClsLogging.writeFileLog("***** NO SE HA PODIDO OBTENER EL IDPERSONA. USRBEAN CONTIENE IDPERSONA=-1 *****",1);
	    }
	    return idPersona;
	}

	private Vector obtenerUsuario(String sIdInstitucion, UsrBean usr)
	{
	    long idPersona=-1;
	    Vector salida = new Vector();
	    
	    try
	    {
		    String sWHERE = " WHERE " + AdmUsuariosBean.C_IDUSUARIO + " = " + usr.getUserName() +
		                      " AND " + AdmUsuariosBean.C_IDINSTITUCION + " = " + sIdInstitucion;
		    
		    AdmUsuariosAdm admUsuario = new AdmUsuariosAdm(usr);
		    Vector vUsuario = admUsuario.select(sWHERE);
		    
		    if (vUsuario!=null && vUsuario.size()>0)
		    {
		        AdmUsuariosBean beanUsuario = (AdmUsuariosBean)vUsuario.elementAt(0);
		        salida.add(beanUsuario);
		        
		        String sNIF = beanUsuario.getNIF();
		        
		        // Hacemos la comparacion del NIF sin los 0 por la izquierda
		        sWHERE = " WHERE ltrim(upper(" + CenPersonaBean.C_NIFCIF + "),'0') = '" + UtilidadesString.LTrim(sNIF.toUpperCase(),"0") + "'";

		        CenPersonaAdm admPersona = new CenPersonaAdm(usr);
		        Vector vPersona = admPersona.select(sWHERE);
		        
		        if (vPersona!=null && vPersona.size()>0)
		        {
		            CenPersonaBean beanPersona = (CenPersonaBean)vPersona.elementAt(0);
		            salida.add(beanPersona);
			        
			        sWHERE = " WHERE " + CenClienteBean.C_IDPERSONA + " = " + beanPersona.getIdPersona() +
                    		   " AND " + CenClienteBean.C_IDINSTITUCION + " = " + sIdInstitucion;

			        CenClienteAdm admCliente = new CenClienteAdm(usr);
			        
			        Vector vCliente = admCliente.select(sWHERE);
			        
			        if (vCliente!=null && vCliente.size()>0)
			        {
			            CenClienteBean beanCliente = (CenClienteBean)vCliente.elementAt(0);
			            salida.add(beanCliente);
			        }
		            idPersona = beanPersona.getIdPersona().longValue();
		        }
		        else {
		        	ClsLogging.writeFileLog("***** NO SE HA PODIDO OBTENER EL IDPERSONA. USRBEAN CONTIENE IDPERSONA=-1 *****",1);
		        }
		    }
		    else  {
		    	ClsLogging.writeFileLog("***** NO SE HA PODIDO OBTENER EL IDPERSONA. USRBEAN CONTIENE IDPERSONA=-1 *****",1);
		    }
	    }
	    catch(Exception e) {
	    	ClsLogging.writeFileLog("***** NO SE HA PODIDO OBTENER EL IDPERSONA. USRBEAN CONTIENE IDPERSONA=-1 *****",1);
	    }
	    return salida;
	}

	/**
	 * <p>Este método es temporal, consiste en que a partir de la institución 
	 * que se haya elegido al entrar en la aplicación, se selecciona una hoja de estilos u otra,
	 * así como el logotipo correspondiente.</p>
	 * <p>Más adelante, las hojas de estilos se generarán dinámicamente y el logotipo
	 * se cargará desde base de datos en la tabla de instituciones, por lo que éste
	 * método será innecesario.</p>     
	 * @param location Código de localización
	 * @param ses Objeto de Sesion.
	 */
	private void initStyles(String location, HttpSession ses)
	{
		String iconsPath="/"+ClsConstants.PATH_DOMAIN+"/"+ClsConstants.RELATIVE_PATH_LOGOS;
		Properties props = PropertyReader.getProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String cssPath = props.getProperty(SIGAConstants.STYLESHEET_PATH);
		//por defecto los del CGAE
		String icon="logoconsejo2.gif";
		GenParametrosAdm paramAdm = new GenParametrosAdm((UsrBean)ses.getAttribute("USRBEAN"));
		
		try
		{
			SIGAGestorInterfaz interfazGestor=new SIGAGestorInterfaz(location);
			String version=paramAdm.getValor(String.valueOf(ClsConstants.INSTITUCION_CGAE),"GEN",SIGAConstants.VERSIONJS,"0");
			java.util.Properties stylesheet = interfazGestor.getInterfaceOptions();
			icon = interfazGestor.getLogoImg();
			ses.setAttribute(SIGAConstants.STYLESHEET_REF, stylesheet);
			ses.setAttribute(SIGAConstants.PATH_LOGO, iconsPath+"/"+icon);
			// Apuntamos al skin de la institucion
			ses.setAttribute(SIGAConstants.STYLESHEET_SKIN, cssPath + "/skin" +stylesheet.get("color")+"/stylesheet.css?v="+version);
			ses.setAttribute(SIGAConstants.VERSIONJS, version);
		}
		
		catch(com.atos.utils.ClsExceptions ex)
		{
			com.atos.utils.ClsLogging.writeFileLogError(ex.getMessage(), ex, 3);
		}		
	}
}
/*
String[] duplaRoles = request.getAttribute("CAS-roles");

boolean isAdminGenFromCGae = false;
for (int i = 0; i < duplaRoles.length; i++) {
	String duplaRol = duplaRoles[i];
	String[] dupla = duplaRol.split(" ");
	String codExternoInstitucion = dupla[0];
	String rol = dupla[1];
	if((codExternoInstitucion.equals("AC0000") || codExternoInstitucion.equals("AC9999")) && rol.equalsIgnoreCase("SIGA-Admin")){
		isAdminGenFromCGae = true;
		break;
	}
	
	
}*/
