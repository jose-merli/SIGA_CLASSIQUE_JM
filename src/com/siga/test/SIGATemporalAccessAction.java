package com.siga.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
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
import org.redabogacia.sigaservices.app.util.PropertyReader;
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
import com.siga.general.SIGAException;

public class SIGATemporalAccessAction extends Action
{
	public SIGATemporalAccessAction()
	{ }

	public ActionForward execute(ActionMapping mapping, 
	        					 ActionForm form, 
	        					 HttpServletRequest request, 
	        					 HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		String result="";
//		String user=request.getParameter("user");
		String location = request.getParameter("location");
		String menuPosition=request.getParameter("posMenu");
		String sAccess=request.getParameter("access");
		String profile=request.getParameter("profile");
		String profileArray[] = profile.split (",");
		String letrado=request.getParameter("letrado");
		UsrBean usrbean = UsrBean.UsrBeanAutomatico(location);
		UsuariosTO certificado = (UsuariosTO) request.getAttribute("USUARIOTO");

		///////////////////////////////////////////////////
		// Verficamos si el usuario es de la 2000. Solo puede ser de esta institucion
		try {
			if (certificado != null && !certificado.getZon_codigo().equalsIgnoreCase("2000") ) {
				request.removeAttribute("USUARIOTO");
				return mapping.findForward("accesodenegado");
			}
		}
		catch (Exception e) {
			ClsLogging.writeFileLog("ERR>> No se ha podido leer el Usuario " + 	"del Certificado desde la peticion: UsuariosTO no existe",1);
			e.printStackTrace();
		}
		///////////////////////////////////////////////////

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
		
//		Hashtable htUsuario = new Hashtable();
//		htUsuario.put(AdmUsuariosBean.C_IDINSTITUCION, location);
//		htUsuario.put(AdmUsuariosBean.C_IDUSUARIO, user);
//		AdmUsuariosAdm admUsuario = new AdmUsuariosAdm(usrbean);
//		Vector vUsuario = admUsuario.selectByPK(htUsuario);
//		AdmUsuariosBean beanUsuario = (AdmUsuariosBean)vUsuario.elementAt(0);
//		usrbean.setUserDescription(beanUsuario.getDescripcion());
//		usrbean.setUserName(user);

		usrbean.setProfile(profileArray);
		usrbean.setLocation(location);
		usrbean.setLetrado(letrado.equals("S")?true:false);
		//usrbean.setComision(profile.equalsIgnoreCase("CJG")?true:false); //Con la nueva forma de entrar se debe hacer un contains
		usrbean.setComision(profile.contains("CJG")?true:false);
		
		// obtengo el idioma de la institucion
		String idLenguajeInstitucion = "1";
		Hashtable ht2 = new Hashtable();
		ht2.put(CenInstitucionBean.C_IDINSTITUCION,location);
		CenInstitucionAdm ins = new CenInstitucionAdm(usrbean);
		Vector v4 = ins.selectByPK(ht2);
		if (v4!=null && v4.size()>0) {
			CenInstitucionBean in = (CenInstitucionBean) v4.get(0);
			idLenguajeInstitucion=in.getIdLenguaje();
		}
		
		// obtengo el idioma del usuario
		String sInsti="";
		String idLenguaje = null; 
		String idLenguajeExt = null; 
		boolean isAplicarLOPD = false;
		if (cli!=null) {
			// Es cliente, obtenemos su idioma
			idLenguaje = cli.getIdLenguaje();
			isAplicarLOPD = cli.getNoAparacerRedAbogacia() != null && cli.getNoAparacerRedAbogacia().equals(ClsConstants.DB_TRUE);
			if(isAplicarLOPD){
				request.setAttribute("mensaje","mensaje.error.lopd");
				return mapping.findForward("accesodenegado");
			}
			
		} 
		else {
			//aalg. INC_10707_SIGA. No es cliente, lo obtenemos de la tabla de usuario
			idLenguaje = usu.getIdLenguaje();
			// No es cliente, lo obtenemos de la institucion
//			Hashtable ht2 = new Hashtable();
//			ht2.put(CenInstitucionBean.C_IDINSTITUCION,location);
//			CenInstitucionAdm ins = new CenInstitucionAdm(usrbean);
//			Vector v4 = ins.selectByPK(ht2);
//			if (v4!=null && v4.size()>0) {
//				CenInstitucionBean in = (CenInstitucionBean) v4.get(0);
//				idLenguaje=in.getIdLenguaje();
//				sInsti="(Obtenido de Institucion)";
//			}
			
			//idLenguaje = idLenguajeInstitucion;
			//sInsti = "(Obtenido de Institucion)";
		}

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
		
		return mapping.findForward(result);
	}	

//	private void printTraza (String m) {
//		System.out.println ("----------------------------------------------------------------------");
//		System.out.println (m);
//		System.out.println ("----------------------------------------------------------------------");
//	}
	
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
			// Usamos el Usuario comodin para pruebas (Entorno Local)
			sql = " WHERE " + AdmUsuariosBean.C_IDUSUARIO + " = 1 "; 
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
		/*
		if(location.equals("5"))
		{
			icon="logoMurcia.gif";
		}
		
		else if(location.equals("2"))
		{
			icon="logoGijon.gif";
		}
		
		else if(location.equals("3"))
		{
			icon="logoMalaga.gif";
		}
		
		else if(location.equals("6"))
		{
			icon="logoZaragoza.gif";
		}
		
		else if(location.equals("4"))
		{
			icon="logoMelilla.gif";
		}		
		
		ses.setAttribute(SIGAConstants.LOCATION_ICON_REF, iconsPath+icon);	
		*/
		try
		{
			SIGAGestorInterfaz interfazGestor=new SIGAGestorInterfaz(location);
		
			java.util.Properties stylesheet = interfazGestor.getInterfaceOptions();
			icon = interfazGestor.getLogoImg();
			ses.setAttribute(SIGAConstants.STYLESHEET_REF, stylesheet);
			ses.setAttribute(SIGAConstants.PATH_LOGO, iconsPath+"/"+icon);
			// Apuntamos al skin de la institucion
			ses.setAttribute(SIGAConstants.STYLESHEET_SKIN, cssPath + "/skin" +stylesheet.get("color")+"/stylesheet.css");
		}
		
		catch(com.atos.utils.ClsExceptions ex)
		{
			com.atos.utils.ClsLogging.writeFileLogError(ex.getMessage(), ex, 3);
		}		
	}
}
