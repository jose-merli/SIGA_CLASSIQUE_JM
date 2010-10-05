package com.siga.general;

//import java.net.MalformedURLException;
//import java.net.URL;
//import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import org.apache.struts.action.ActionServlet;
//import pra.axis.services.Certificados_service.CertInfoServiceServiceLocator;
//import pra.axis.services.Certificados_service.CertificadosServiceSoapBindingStub;
import com.pra.core.filters.security.helper.UsuariosTO;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
//import com.pra.core.TO.SesionUsuarioTO;
//import com.pra.core.TO.SesionZonaTO;
//import com.pra.core.context.ContextFactory;
//import com.pra.core.context.IContext;
//import com.pra.core.sesion.SesionTO;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.administracion.SIGAGestorInterfaz;
import com.siga.beans.*;


/**
 * @author esdras.martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SIGACGAEContext {
//	private static final String PARAMETER_ID_OBJECT ="idParameter";
	private static final String ENTORNO_DESARROLLO ="DESARROLLO";
//	private static final String ZONA_ID_OBJECT ="zonaparameter";
	private static final String ROL_LETRADO ="ABOGADO"; // Es el rol que corresponde a un letrado
	private static final String ROL_COMISION ="COMISION"; // Es el rol que corresponde a la comision
	/**
	 *
	 */
	public SIGACGAEContext() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UsrBean rellenaContexto(HttpServletRequest request, ActionServlet config ) throws SIGAException
	{
	    ReadProperties rproperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rproperties=new ReadProperties("SIGA.properties");
		boolean desarrollo = rproperties.returnProperty("administracion.login.entorno").equalsIgnoreCase(SIGACGAEContext.ENTORNO_DESARROLLO);
		if (desarrollo)
		{
//Magia
Properties properties = System.getProperties();

properties.put("http.proxyHost", "localhost");
properties.put("http.proxyPort", "7001");

System.setProperties(properties);
		}

		// Valor del id del Certificado que pasará la plataforma RedAbogacía a
		// las diferentes aplicaciones (SIGA,PasesPrisiones ....)
		UsuariosTO user = (UsuariosTO)request.getAttribute("USUARIOTO");
/*		
	    try
	    {

			ReadProperties rproperties=new ReadProperties("SIGA.properties");
			boolean desarrollo = rproperties.returnProperty("administracion.login.entorno").equalsIgnoreCase(SIGACGAEContext.ENTORNO_DESARROLLO);
			if (desarrollo)
			{
// Magia
Properties properties = System.getProperties();

properties.put("http.proxyHost", "localhost");
properties.put("http.proxyPort", "8080");

System.setProperties(properties);
			}

			// Valor del id del Certificado que pasará la plataforma RedAbogacía a
			// las diferentes aplicaciones (SIGA,PasesPrisiones ....)
			user = (UsuariosTO)request.getAttribute("USUARIOTO");
			String idCodigoCertificado=(String)request.getParameter("idCodigoCertificado");

			if (idCodigoCertificado==null || idCodigoCertificado.trim().equals(""))
			{
				return null;
			}

	    	CertInfoServiceServiceLocator certSL = new CertInfoServiceServiceLocator();

	    	CertificadosServiceSoapBindingStub stub = null;

	    	try
	    	{
	    	    try
	    	    {

					stub = new CertificadosServiceSoapBindingStub(new URL(certSL.getCertificadosServiceAddress()), certSL);

	    	    }

	    	    catch (MalformedURLException e1)
	    	    {
	    	    	ClsLogging.writeFileLogError(e1.getMessage(), e1, 3);
	    	    }

	    	    // Objeto UsuariosTO con toda la información del Usuario de entrada.
	        	//@user = stub.findBuscadorUsuarios(idCodigoCertificado);
				user = stub.findBuscadorUsuarios(idCodigoCertificado);
	    	}

	    	catch (RemoteException e2)
	    	{

	    		ClsLogging.writeFileLogError(e2.getMessage(), e2, 3);
	    	}
	    }

	    catch(Exception e)
	    {
			ClsLogging.writeFileLogError(e.getMessage(), e, 3);
			e.printStackTrace();
	    }
*/
		if (user!=null)
		{
		    ClsLogging.writeFileLog("Objeto user: ", 7);
		    ClsLogging.writeFileLog("*************", 7);
		    ClsLogging.writeFileLog("Accion: " + user.getAccion(), 7);
		    ClsLogging.writeFileLog("ConCertificado: " + user.getConCertificado(), 7);
		    ClsLogging.writeFileLog("Departamento: " + user.getDepartamento(), 7);
		    ClsLogging.writeFileLog("Huella: " + user.getHuella(), 7);
		    ClsLogging.writeFileLog("Num_serie_cert: " + user.getNum_serie_cert(), 7);
		    ClsLogging.writeFileLog("NumColegiado: " + user.getNumColegiado(), 7);
		    ClsLogging.writeFileLog("NumDiasVencimiento: " + user.getNumDiasVencimiento(), 7);

		    ArrayList objPerfiles = user.getPerfilesUsuario();

		    ClsLogging.writeFileLog("PerfilesUsuario: " + user.getPerfilesUsuario(), 7);

		    for (int i=0; i<objPerfiles.size(); i++)
		    {
		        ClsLogging.writeFileLog("\t- Perfil: " + (String)objPerfiles.get(i), 7);
		    }

		    ClsLogging.writeFileLog("Pfi_codigo: " + user.getPfi_codigo(), 7);
		    ClsLogging.writeFileLog("PfiNombre: " + user.getPfiNombre(), 7);
		    ClsLogging.writeFileLog("Poblacion: " + user.getPoblacion(), 7);
		    ClsLogging.writeFileLog("Rol_codigo: " + user.getRol_codigo(), 7);
		    ClsLogging.writeFileLog("RolDesc: " + user.getRolDesc(), 7);
		    ClsLogging.writeFileLog("Stytle: " + user.getStytle(), 7);
		    ClsLogging.writeFileLog("Teu_codigo: " + user.getTeu_codigo(), 7);
		    ClsLogging.writeFileLog("TeuDesc: " + user.getTeuDesc(), 7);
		    ClsLogging.writeFileLog("Usu_alta: " + user.getUsu_alta(), 7);
		    ClsLogging.writeFileLog("Usu_baja: " + user.getUsu_baja(), 7);
		    ClsLogging.writeFileLog("Usu_caducidad: " + user.getUsu_caducidad(), 7);
		    ClsLogging.writeFileLog("Usu_departamento: " + user.getUsu_departamento(), 7);
		    ClsLogging.writeFileLog("Usu_login: " + user.getUsu_login(), 7);
		    ClsLogging.writeFileLog("Usu_login_alta: " + user.getUsu_login_alta(), 7);
		    ClsLogging.writeFileLog("Usu_login_baja: " + user.getUsu_login_baja(), 7);
		    ClsLogging.writeFileLog("Usu_mail: " + user.getUsu_mail(), 7);
		    ClsLogging.writeFileLog("Usu_modificacion: " + user.getUsu_modificacion(), 1);
		    ClsLogging.writeFileLog("Usu_nif: " + user.getUsu_nif(), 7);
		    ClsLogging.writeFileLog("Usu_nombre: " + user.getUsu_nombre(), 7);
		    ClsLogging.writeFileLog("Usu_num_colegiado: " + user.getUsu_num_colegiado(), 7);
		    ClsLogging.writeFileLog("Usu_password: " + user.getUsu_password(), 7);
		    ClsLogging.writeFileLog("Zon_codigo: " + user.getZon_codigo(), 7);
		    ClsLogging.writeFileLog("ZonDesc: " + user.getZonDesc(), 7);
		    ClsLogging.writeFileLog("*************", 7);
		}

		else
		{
			return null;
		}

		//@IContext webappMyContext = ContextFactory.getContexto(config);

        //@ReadProperties properties=new ReadProperties("SIGA.properties");
        //@boolean desarrollo = properties.returnProperty("administracion.login.entorno").equalsIgnoreCase(SIGACGAEContext.ENTORNO_DESARROLLO);

		//@String ValorId = (String)request.getParameter(PARAMETER_ID_OBJECT);
		//@String zonaId= (String)request.getParameter(ZONA_ID_OBJECT);
		//@SesionTO sessionTO=desarrollo?sessionTO=webappMyContext.getObjectAppXML(ValorId):webappMyContext.getObjectApp(ValorId);
		//@SesionTO sessionTO=webappMyContext.getObjectApp(ValorId);
		//@ SesionUsuarioTO obUsu =sessionTO.getUsuario();
		//@SesionZonaTO obzona = sessionTO.getZona();

		UsrBean bean=new UsrBean();
		UserTransaction tx=null;
		try {

			tx=bean.getTransaction();
			tx.begin();
			// Obtenemos la institución a la que se ha conectado
			/*@if (zonaId==null) {
				bean.setLocation(obzona.getZona_codigo());
			} else {
				bean.setLocation(zonaId);
			}*/

			bean.setLocation(user.getZon_codigo());

			// Se establece si el usuario es un letrado
		 	//@String rol=obUsu.getRol().toUpperCase();
			String rol = user.getRolDesc().toUpperCase();
			System.out.println("Sigacgaecontext rol "+rol );
			bean.setLetrado(rol.equalsIgnoreCase(SIGACGAEContext.ROL_LETRADO));
			// Se establece si el usuario pertenece a la comision
			bean.setComision(rol.equalsIgnoreCase(SIGACGAEContext.ROL_COMISION));
			/* Obtenemos el idRol que corresponde al rol del certificado y lo metemos
			 * en el bean del usuario
			*/
			String rolId=setRol(rol,bean);
			

			/* Obtenemos el idusuario que corresponde a la persona que se ha validado.
			 * Si el usuario no existía, lo metemos en la tabla de idusuario.
			 */

			//@setIdUsuario(obUsu.getNif().toUpperCase(),bean,
			//@		obUsu.getLocaleUsuario()==null?"ES":obUsu.getLocaleUsuario().getLanguage().toUpperCase(),
			//@		obUsu.getNombre().toUpperCase());
			setIdUsuario(user.getUsu_nif().toUpperCase(), bean, ClsConstants.LENGUAJE_ESP, "ES", user.getPfiNombre());

			/* Comprobamos si existe el usuario efectivo. Si no existe, lo damos de
			 * alta y lo metemos en el grupo que tenga por defecto el rol del usuario.
			 */
			//@checkUsuarioEfectivo(obUsu,bean,rolId,obUsu.getNumeroSerie());
			checkUsuarioEfectivo(user,bean,rolId,user.getNum_serie_cert().trim());

			/* Obtenemos el los grupos de usuarios a los que pertenece el usuario y lo
		     * metemos el bean del usuario
		     */
			setProfiles(rolId,bean);
			/* Obtenemos idPersona que corresponde al nif y lo mentemos en el bean del
			 * usuario.
			*/
			//@long idPersona=setPersona(obUsu.getNif().toUpperCase(),bean,obUsu.getNombre().toUpperCase());
			long idPersona=setPersona(user.getUsu_nif().toUpperCase(),bean,user.getPfiNombre().toUpperCase());
			/* Obtenemos el lenguaje de inicio de sesion. El bean ya debe tener la
			 * institucion establecida.
			*/
			setIdioma(new Long(idPersona),Integer.valueOf(bean.getLocation()),bean);
			
			tx.commit();
		}
		catch (Exception e) {
			try { if (tx!=null) tx.rollback(); } catch(Exception el) {}

			if (e instanceof SIGAException) throw (SIGAException)e;
			throw new SIGAException("messages.general.errorAutenticacion",e);
		}
		
		return bean;
	}

	private String setRol(String rol,UsrBean bean) throws Exception {
		AdmRolAdm rolObj=new AdmRolAdm(bean);
		Vector vec=rolObj.select(" where UPPER(" +AdmRolBean.C_DESCRIPCION +")='"+rol.toUpperCase()+"' ");
		if (vec==null || vec.size()==0) {
			throw new SIGAException("messages.general.errorRolNoDefinido");
		}
		if (vec.size()!=1) {
			throw new SIGAException("messages.general.errorDescripcionRolDuplicada");
		}
		AdmRolBean rolBean=(AdmRolBean)vec.elementAt(0);
		bean.setIdRol(rolBean.getIdRol());
		return rolBean.getIdRol();
	}


	private void setGrupos(String rol,UsrBean bean)  throws Exception {
		AdmPerfilRolAdm rolObj=new AdmPerfilRolAdm (bean);
		// Buscamos el rol por defecto.
		Vector vec=rolObj.select(" where  " +AdmPerfilRolBean.C_IDINSTITUCION+"='"+bean.getLocation()+"' AND "+
				AdmPerfilRolBean.C_IDROL+"='"+rol+"' AND "+
				AdmPerfilRolBean.C_GRUPO_POR_DEFECTO+"='S'");
//		AdmPerfilRolBean admBean=null;
		if (vec==null || vec.size()==0) {
			// Lanzamos una excepción si no hay ningún grupo por defecto.
			throw new SIGAException("messages.general.errorNoExisteGrupoPorDefecto");
		} else if (vec.size()!=1) { // esto no se debe dar nunca
			throw new SIGAException("messages.general.errorGrupoPorDefectoDuplicado");
		}
		// Metemos el usuario el el grupo por defecto
		for (int i=0;i<vec.size();i++) {
			insertUsuariosPerdil(rol,bean,((AdmPerfilRolBean)vec.elementAt(0)).getIdPerfil());//  getString(AdmPerfilRolBean.C_IDPERFIL));
		}
	}

	private void insertUsuariosPerdil(String rol,UsrBean bean,String perfil)  throws Exception {
		AdmUsuariosEfectivosPerfilAdm rolObj=new AdmUsuariosEfectivosPerfilAdm (bean);
		AdmUsuariosEfectivosPerfilBean admBean=new AdmUsuariosEfectivosPerfilBean();
		admBean.setFechaMod("SYSDATE");
		admBean.setIdInstitucion(bean.getLocation());
		admBean.setIdPerfil(perfil);
		admBean.setIdRol(rol);
		admBean.setIdUsuario(bean.getUserName());
		admBean.setUsuMod(new Integer(-1));
		rolObj.insert(admBean);
	}

	private long setPersona(String nif,UsrBean bean,String _nombre) throws Exception {
		long retorno=-1; bean.setIdPersona(-1);// Valor por defecto del retorno y del id de personna
		CenPersonaAdm personaAdm=new CenPersonaAdm(bean);
		Vector vec=personaAdm.select(" where ltrim(UPPER(" +CenPersonaBean.C_NIFCIF+"),'0')='"+UtilidadesString.LTrim(nif.toUpperCase(),"0")+"'");
		// Si no existe el usuario, salimos de la función y pondemos el idusuario a -1
		if (vec!=null&&vec.size()==1) {
			
			CenPersonaBean persona=(CenPersonaBean)vec.elementAt(0);
			Long idPersona=persona.getIdPersona();
			bean.setIdPersona(idPersona.longValue());
			retorno=idPersona.longValue();

			/* RGG 07-09-2005 Cambio para comprobar solamente el NIF
			
			// Nos aseguramos que hay cierto parecido en los nombres
			CenPersonaBean persona=(CenPersonaBean)vec.elementAt(0);
			String apellido1=persona.getApellido1().toUpperCase();
			String apellido2=persona.getApellido2().toUpperCase();
			String nombre=persona.getNombre().toUpperCase();
			_nombre=_nombre.toUpperCase();
			int parecido=0;
			if(_nombre.indexOf(apellido1)!=-1) parecido++;
			if(_nombre.indexOf(apellido1)!=-1)parecido++;
			if(_nombre.indexOf(nombre)!=-1)parecido++;
			try{
			if (parecido>2){
				Long idPersona=persona.getIdPersona();
				bean.setIdPersona(idPersona.longValue());
				retorno=idPersona.longValue();
			}
			}catch(Exception e){
				// Si se produce una excepción, el usuario quedará en -1
				e.printStackTrace();
			}
			
			*/

		} else if (vec.size()>1) { // esto no se debe dar nunca
			throw new SIGAException("messages.general.errorUsuarioEfectivoDuplicado");
		}


		return retorno;
	}

	//@private AdmUsuarioEfectivoBean checkUsuarioEfectivo(SesionUsuarioTO usu,UsrBean bean, String rol,String numserie) throws Exception {
	private AdmUsuarioEfectivoBean checkUsuarioEfectivo(UsuariosTO usu,UsrBean bean, String rol,String numserie) throws Exception {
		AdmUsuarioEfectivoBean retorno=null;
		AdmUsuarioEfectivoAdm rolObj=new AdmUsuarioEfectivoAdm(bean);
		Vector vec=rolObj.select(" where " +AdmUsuarioEfectivoBean.C_IDUSUARIO +"='"+bean.getUserName()+"' AND "+
				AdmUsuarioEfectivoBean.C_IDINSTITUCION+"="+bean.getLocation()+" AND "+
				AdmUsuarioEfectivoBean.C_IDROL+"='"+rol+"'");
		AdmUsuarioEfectivoBean admBean=null;
		// Si el usuario efectivo no existe, lo insertamos
		if (vec==null || vec.size()==0) {
			admBean=new AdmUsuarioEfectivoBean();
			admBean.setIdInstitucion(new Integer(bean.getLocation()));
			admBean.setIdRol(rol);
			admBean.setFechaMod("SYSDATE");
			admBean.setIdUsuario(new Integer(bean.getUserName()));
			admBean.setUsuMod(new Integer("-1"));
			if (numserie!=null)
				{
					checkCertificado(bean.getLocation(),usu,bean);
					admBean.setNumSerie(numserie); //Metemos el número de serie
				}
			retorno=admBean; // La función devolverá el nuevo bean
			rolObj.insert(admBean);
			setGrupos(rol,bean);

		}else if(vec.size()==1){
			AdmUsuarioEfectivoBean usuefectivo=(AdmUsuarioEfectivoBean)vec.elementAt(0);
			// Si el número de serie es distinto, lo actualizamos
			//@if(!usuefectivo.getNumSerie().equalsIgnoreCase(usu.getNumeroSerie())){
			if(!usuefectivo.getNumSerie().equalsIgnoreCase(usu.getNum_serie_cert().trim())){
				checkCertificado(bean.getLocation(),usu,bean);
				//@usuefectivo.setNumSerie(usu.getNumeroSerie());
				usuefectivo.setNumSerie(usu.getNum_serie_cert().trim());
				rolObj.update(usuefectivo);
			}


		}else { // esto no se debe dar nunca
			throw new SIGAException("messages.general.errorUsuarioEfectivoDuplicado");
		}
		return retorno;
	}

	private String setIdUsuario(String nif,UsrBean bean, String lang, String langExt, String nombre) throws Exception {
		AdmUsuariosAdm rolObj=new AdmUsuariosAdm(bean);
		//Hacemos la comparacion del NIF sin los 0 por la izquierda
		Vector vec=rolObj.select(" where ltrim(" +AdmUsuariosBean.C_NIF +",'0')='"+UtilidadesString.LTrim(nif,"0")+"' AND "+
				AdmUsuariosBean.C_IDINSTITUCION+"="+bean.getLocation());
		AdmUsuariosBean admBean=null;
		// Si no existe el usuario, ejecutamos lo siguiente
		if (vec==null || vec.size()==0) {
			admBean=new AdmUsuariosBean();
			admBean.setActivo("S");
			admBean.setFechaAlta("SYSDATE");
			admBean.setFechaMod("SYSDATE");
			admBean.setIdInstitucion(new Integer(bean.getLocation()));
			admBean.setIdLenguaje(lang);

			admBean.setDescripcion(nombre);
			admBean.setNIF(nif);
			admBean.setUsuMod(new Integer("-1"));
			// Obtenemos el nuevo idusuario
			try {
			String sql="select max("+AdmUsuariosBean.C_IDUSUARIO+") as MAXIDUSER from " +
				AdmUsuariosBean.T_NOMBRETABLA +
				" where " + AdmUsuariosBean.C_IDINSTITUCION +"="+
				bean.getLocation();// + " for update";

			RowsContainer o=new RowsContainer();
			if (o.findForUpdate(sql)) {
				Vector vecto=o.getAll();
				if (vecto==null || vecto.size()==0) {
					admBean.setIdUsuario(new Integer("0"));
				} else {
					Row row=(Row)vecto.elementAt(0);

					String nuevoIdUsuario = row.getString("MAXIDUSER");

					if (nuevoIdUsuario==null || nuevoIdUsuario.trim().equals(""))
					{
					    nuevoIdUsuario="0";
					}

					Integer value=new Integer(nuevoIdUsuario);
					admBean.setIdUsuario(new Integer(value.intValue()+1));
				}
			}
			rolObj.insert(admBean);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

		} else if (vec.size()!=1) { // esto no se debe dar nunca
			throw new SIGAException("messages.general.errorUsuarioDuplicado");
		// Si existe el usuario
		} else {
			admBean=(AdmUsuariosBean)vec.elementAt(0);
		}
		if (!"S".equalsIgnoreCase(admBean.getActivo())) {
			throw new SIGAException("messages.general.errorUsuarioDesactivado");
		}
		
		
		//Añado que guarde el nombre del usuario:
		bean.setUserDescription(nombre);
		
		bean.setUserName(""+admBean.getIdUsuario());
		return ""+admBean.getIdUsuario();
	}

	private String[] setProfiles(String rol,UsrBean bean) throws Exception {
		AdmUsuariosEfectivosPerfilAdm rolObj=new AdmUsuariosEfectivosPerfilAdm (bean);
//		AdmUsuariosEfectivosPerfilBean admBean=null;
		Vector vec=rolObj.select(" where " +AdmUsuariosEfectivosPerfilBean.C_IDROL +"='"+rol+"' AND "+
				AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION+"="+bean.getLocation() + " AND " +
				AdmUsuariosEfectivosPerfilBean.C_IDUSUARIO+"="+bean.getUserName());

		if (vec==null || vec.size()==0) {
			bean.setProfile(new String[0]);
			return new String[0];
		} else {
			String[] profiles=new String[vec.size()];
			for (int i=0;i<vec.size();i++) {
				profiles[i]=((AdmUsuariosEfectivosPerfilBean)vec.elementAt(i)).getIdPerfil();
			}
			bean.setProfile(profiles);
			return profiles;
		}
	}

	public void initStyles(String location, HttpSession ses)
	{
		String iconsPath="/"+ClsConstants.PATH_DOMAIN+"/"+ClsConstants.RELATIVE_PATH_LOGOS;
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
		}

		catch(com.atos.utils.ClsExceptions ex)
		{
			com.atos.utils.ClsLogging.writeFileLogError(ex.getMessage(),ex, 3);
		}
	}

	private  void setIdioma(Long idPersona, Integer idInstitucion,UsrBean bean) throws Exception{
		/* Si el usuario es cliente de la institución, el idioma que tenga establecido
		 * se utilizará para la sesión
		 */
		
		AdmLenguajesAdm adml = new AdmLenguajesAdm(bean);
		CenInstitucionAdm admINS = new CenInstitucionAdm(bean);
		Hashtable ht = new Hashtable();
		ht.put("IDINSTITUCION",idInstitucion.toString());
		Vector v = admINS.selectByPK(ht);
		CenInstitucionBean b = null;
		if (v!=null && v.size()>0) {
		    b=(CenInstitucionBean)v.get(0);
		}
		
		CenClienteAdm clienteAdm=new CenClienteAdm(bean);
		CenClienteBean cliente=clienteAdm.existeCliente(idPersona,idInstitucion);
		if(cliente!=null&&cliente.getIdLenguaje()!=null&&cliente.getIdLenguaje()!=""){
			bean.setLanguage(cliente.getIdLenguaje());
			bean.setLanguageExt(adml.getLenguajeExt(cliente.getIdLenguaje()));
			if (bean!=null) bean.setLanguageInstitucion(b.getIdLenguaje());
        /* Si no tiene idioma por defecto por no ser cliente o no tenerlo establecido
         * obtenemos el idioma por defecto de la institucion
         */
			
			// verificamos si el idioma del usuario esta traducido
			CenInstitucionLenguajesAdm admLen = new CenInstitucionLenguajesAdm (bean);
			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, CenInstitucionLenguajesBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(h, CenInstitucionLenguajesBean.C_IDLENGUAJE, cliente.getIdLenguaje());
			Vector vLen = admLen.selectByPK(h);
			if (vLen == null || vLen.size() != 1) {
				// El idoma del lenguaje no esta traducido, ponemos el idioma de la institucion
			    bean.setLanguage(b.getIdLenguaje());
				bean.setLanguageExt(adml.getLenguajeExt(b.getIdLenguaje()));
			}			
			
		}else{
			bean.setLanguage(b.getIdLenguaje());
			bean.setLanguageExt(adml.getLenguajeExt(b.getIdLenguaje()));
			if (bean!=null) bean.setLanguageInstitucion(b.getIdLenguaje());

			/*CenInstitucionAdm institucionAdm=new CenInstitucionAdm(new Integer(0));
			Vector vec=institucionAdm.select("where "+CenInstitucionBean.C_IDINSTITUCION+"="+idInstitucion);
			if(vec!=null&&vec.size()==1){
//				CenInstitucionBean institucion=(CenInstitucionBean)vec.elementAt(0);
				//if(institucion.getIdLenguaje()!=null) bean.setLanguage(institucion.getIdLenguaje());
			}
*/
		}
	}
	//@private void checkCertificado(String idInstitucion,SesionUsuarioTO usu,UsrBean bean)throws Exception{
	private void checkCertificado(String idInstitucion,UsuariosTO usu,UsrBean bean)throws Exception{
		//Comprobamos si existe el certificado y si no existe, lo insertamos
		AdmCertificadosAdm certificadoAdm=new AdmCertificadosAdm(bean);
		Vector vec=certificadoAdm.select("where "+AdmCertificadosBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+
				//@AdmCertificadosBean.C_NUMSERIE+"='"+usu.getNumeroSerie()+"'");
		        AdmCertificadosBean.C_NUMSERIE+"='"+usu.getNum_serie_cert().trim()+"'");
		if(vec==null||vec.size()==0){
			// Insertamos el certificado
			try{
			AdmCertificadosBean certificado=new AdmCertificadosBean();
			certificado.setIdInstitucion(Integer.valueOf(idInstitucion));
			certificado.setIdUsuario(Integer.valueOf(bean.getUserName()));
			//@certificado.setNIF(usu.getNif());
			certificado.setNIF(usu.getUsu_nif());
			//@certificado.setNumSerie(usu.getNumeroSerie());
			certificado.setNumSerie(usu.getNum_serie_cert().trim());
			//certificado.setFechaCad(GstDate.getApplicationFormatDate("",usu.getFechaCadCertificado()));
			//certificado.setFechaCad("SYSDATE");
			certificado.setFechaCad(GstDate.getApplicationFormatDate("",usu.getUsu_caducidad()));
			certificado.setRevocacion("N");
			//@certificado.setRol(usu.getRol());
			// RGG SE PONE LA DESCRIPCION certificado.setRol(usu.getRol_codigo());
			certificado.setRol(usu.getRolDesc());
			certificadoAdm.insert(certificado);
			}catch(Exception e){
			 throw new SIGAException("messages.general.errorGrupoPorDefectoDuplicado");
			}
		}
	}



}