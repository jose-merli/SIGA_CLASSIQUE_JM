package com.siga.general;

//import java.net.MalformedURLException;
//import java.net.URL;
//import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import javax.xml.bind.DatatypeConverter;

import org.apache.struts.action.ActionServlet;
import org.redabogacia.sigaservices.app.AppConstants;
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
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.pra.core.filters.security.helper.UsuariosTO;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.administracion.SIGAGestorInterfaz;
import com.siga.beans.AdmCertificadosAdm;
import com.siga.beans.AdmCertificadosBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmPerfilRolAdm;
import com.siga.beans.AdmPerfilRolBean;
import com.siga.beans.AdmRolAdm;
import com.siga.beans.AdmRolBean;
import com.siga.beans.AdmUsuarioEfectivoAdm;
import com.siga.beans.AdmUsuarioEfectivoBean;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.AdmUsuariosEfectivosPerfilAdm;
import com.siga.beans.AdmUsuariosEfectivosPerfilBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenInstitucionLenguajesAdm;
import com.siga.beans.CenInstitucionLenguajesBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EstUserRegistryAdm;
import com.siga.beans.GenParametrosAdm;

import es.satec.businessManager.BusinessManager;
import io.jsonwebtoken.Jwts;


/**
 * @author esdras.martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SIGACGAEContextCAS {
	
	private static final String ENTORNO_DESARROLLO ="DESARROLLO";
	private static final String ROL_LETRADO ="ABOGADO"; // Es el rol que corresponde a un letrado
	private static final String SECRET_SIGN_KEY = "1234";
	private static final String TOKEN_PREFIX = "Bearer ";

	
	
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getPermisosFromJWTToken(String token) {

		return (HashMap<String, String>) Jwts.parser().setSigningKey(SECRET_SIGN_KEY).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().get("permisos");
	}
	
	public UsrBean rellenaContexto(HttpServletRequest request, ActionServlet config ) throws SIGAException
	{
	    ReadProperties rproperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);

	 // Recogemos el token de la sesion
	    String token =(String)request.getParameter("token");
	    ClsLogging.writeFileLog("TOKEN >>> "+token);
//		ReadProperties rproperties=new ReadProperties("SIGA.properties");
		boolean desarrollo = rproperties.returnProperty("administracion.login.entorno").equalsIgnoreCase(SIGACGAEContextCAS.ENTORNO_DESARROLLO);
		if (desarrollo){
			//Magia
			Properties properties = System.getProperties();
			
			properties.put("http.proxyHost", "localhost");
			properties.put("http.proxyPort", "7001");
			
			System.setProperties(properties);
			//token="eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE1ODQ2OTYzNjAsImlzcyI6IkNPTlNFSk8gR0VORVJBTCBERSBMQSBBQk9HQUNJQSIsInN1YiI6IjcxNTA0MDQxUCIsInBlcm1pc29zIjp7IjJDMSI6IjAiLCIwMCI6IjMiLCIyQzIiOiIwIiwiMDEiOiIwIiwiMDMiOiIwIiwiMDUiOiIwIiwiMDYiOiIwIiwiMDciOiIwIiwiMDgiOiIwIiwiOUExIjoiMyIsIjlBMiI6IjMiLCI5QTMiOiIwIiwiMjMwIjoiMCIsIjIzMSI6IjIiLCIyMzIiOiIwIiwiMjMzIjoiMyIsIjIzNCI6IjMiLCIxMTMiOiIwIiwiMTE0IjoiMCIsIjExNSI6IjAiLCIyMzciOiIwIiwiMTE2IjoiMCIsIjExNyI6IjAiLCIxMTgiOiIwIiwiMTE5IjoiMCIsIjkxMSI6IjAiLCI5MTIiOiIwIiwiOTEzIjoiMCIsIjkxNCI6IjAiLCI5MTYiOiIwIiwiMTAiOiIwIiwiOTE4IjoiMCIsIjkxOSI6IjAiLCIxMUEiOiIwIiwiMTIiOiIzIiwiMTMiOiIwIiwiOTFBIjoiMCIsIjkxQiI6IjAiLCI5MUMiOiIwIiwiOTFEIjoiMCIsIjkxRSI6IjMiLCI5MUYiOiIwIiwiMjQxIjoiMCIsIjkxRyI6IjAiLCIwIjoiMyIsIjkxSCI6IjAiLCI5MUkiOiIwIiwiMSI6IjAiLCIwMDEiOiIwIiwiMDAyIjoiMCIsIjkxSiI6IjAiLCIyIjoiMCIsIjAwMyI6IjAiLCIzIjoiMCIsIjkxTCI6IjAiLCIxMjUiOiIzIiwiNCI6IjAiLCIwMDQiOiIwIiwiOTFNIjoiMCIsIjEyNiI6IjIiLCIwMDUiOiIwIiwiNSI6IjAiLCIxMjciOiIyIiwiOVMwIjoiMiIsIjkxTyI6IjAiLCIxMjgiOiIwIiwiOTIwIjoiMiIsIjlTMSI6IjMiLCIwMDciOiIwIiwiMTI5IjoiMiIsIjlTMiI6IjMiLCI5UzMiOiIyIiwiOTIyIjoiMCIsIjlTNCI6IjMiLCI5UzUiOiIwIiwiOVM2IjoiMiIsIjlTNyI6IjMiLCIyRTEiOiIwIiwiOVM4IjoiMCIsIjlTOSI6IjAiLCIyMSI6IjAiLCI5MVkiOiIwIiwiMTJCIjoiMCIsIjkxWiI6IjAiLCIxMkMiOiIwIiwiMTJEIjoiMCIsIjEyRiI6IjAiLCIxMkciOiIwIiwiOVNBIjoiMiIsIjEySCI6IjMiLCI5U0IiOiIyIiwiMTJJIjoiMyIsIjEySiI6IjMiLCI5U0MiOiIyIiwiOTJDIjoiMiIsIjEySyI6IjIiLCI5U0UiOiIyIiwiOTJEIjoiMiIsIjEyTCI6IjIiLCIxMk0iOiIyIiwiMTJOIjoiMiIsIjI1MCI6IjAiLCI5U0giOiIyIiwiMTJPIjoiMiIsIjEzMCI6IjAiLCIxMzEiOiIwIiwiMDEwIjoiMCIsIjEzMiI6IjAiLCIwMTEiOiIwIiwiMTMzIjoiMCIsIjAxMiI6IjAiLCIwMTMiOiIwIiwiOVNNIjoiMiIsIjAwUyI6IjAiLCIxMzUiOiIwIiwiMDE0IjoiMCIsIjlTTiI6IjIiLCI5U08iOiIyIiwiOVQwIjoiMiIsIjAxNiI6IjAiLCI5VDEiOiIzIiwiOVQyIjoiMyIsIjAxOCI6IjAiLCIxMlgiOiIwIiwiOTMyIjoiMCIsIjJGMCI6IjAiLCI5MzgiOiIwIiwiMTJhIjoiMiIsIjM0IjoiMCIsIjM1IjoiMCIsIjM2IjoiMCIsIjM3IjoiMCIsIjM4IjoiMCIsIjE0MCI6IjIiLCIyNjEiOiIwIiwiMTQxIjoiMiIsIjE0MiI6IjIiLCIxNDMiOiIyIiwiMTQ0IjoiMCIsIjkzTCI6IjAiLCIxNDUiOiIyIiwiMDFTIjoiMCIsIjE0NiI6IjIiLCI5VTAiOiIyIiwiMTQ3IjoiMiIsIjkzTiI6IjAiLCI5NDEiOiIwIiwiMkcwIjoiMCIsIjk0NiI6IjAiLCIyRzEiOiIwIiwiNDAiOiIwIiwiMzdhIjoiMCIsIjJHMiI6IjAiLCI0MSI6IjAiLCI0MiI6IjAiLCI5NDkiOiIzIiwiNDMiOiIwIiwiMkc0IjoiMCIsIjQ0IjoiMCIsIjAyQyI6IjAiLCI0NiI6IjAiLCI0NyI6IjAiLCI0OSI6IjAiLCI5NEEiOiIwIiwiOTRCIjoiMCIsIjk0QyI6IjAiLCI5NEQiOiIwIiwiOTRGIjoiMCIsIjk0RyI6IjAiLCIyR0EiOiIwIiwiMTUwIjoiMCIsIjk0SCI6IjAiLCIxNTEiOiIzIiwiMDMwIjoiMCIsIjk0SSI6IjAiLCIxNTIiOiIwIiwiOTRKIjoiMCIsIjE1MyI6IjAiLCI5NEsiOiIwIiwiMTU0IjoiMCIsIjk0TCI6IjAiLCIxNTUiOiIwIiwiOTRNIjoiMCIsIjE1NiI6IjMiLCI5VjAiOiIyIiwiOTROIjoiMCIsIjE1NyI6IjIiLCIxNTgiOiIyIiwiOTRPIjoiMCIsIjE1OSI6IjIiLCI5NFAiOiIwIiwiOTRRIjoiMCIsIjk1MiI6IjAiLCI5NFIiOiIwIiwiOTUzIjoiMCIsIjk1NCI6IjAiLCI5NFMiOiIwIiwiOTU1IjoiMCIsIjk0VCI6IjAiLCI5NFUiOiIwIiwiMkgwIjoiMCIsIjJIMSI6IjAiLCI5NFYiOiIzIiwiMkgyIjoiMCIsIjk0VyI6IjMiLCI5NFgiOiIwIiwiOTU5IjoiMCIsIjE1QSI6IjAiLCI5NFkiOiIwIiwiOTRaIjoiMCIsIjU4IjoiMCIsIjk1QSI6IjAiLCI5NUIiOiIwIiwiOTVDIjoiMCIsIjk1RCI6IjAiLCI5NUUiOiIwIiwiOTVGIjoiMCIsIjk1RyI6IjAiLCI5NUgiOiIwIiwiOTVJIjoiMCIsIjk1SiI6IjAiLCI5NUsiOiIwIiwiMjg1IjoiMiIsIjk1TCI6IjAiLCIyODYiOiIyIiwiOTVNIjoiMCIsIjI4NyI6IjIiLCI5VzAiOiIyIiwiOTVOIjoiMCIsIjI4OCI6IjIiLCI5NU8iOiIwIiwiMjg5IjoiMiIsIjk1UCI6IjAiLCI5NVEiOiIwIiwiOTVSIjoiMCIsIjYwMCI6IjAiLCI5NVMiOiIwIiwiNjAxIjoiMCIsIjk1VCI6IjAiLCI5NVUiOiIwIiwiNjAzIjoiMCIsIjk1ViI6IjAiLCI2MDQiOiIwIiwiNjAiOiIwIiwiNjEiOiIwIiwiNjIiOiIwIiwiNjA3IjoiMCIsIjYzIjoiMCIsIjk1WiI6IjAiLCI2MDgiOiIwIiwiNjA5IjoiMCIsIjY3IjoiMCIsIjY4IjoiMCIsIjY5IjoiMCIsIjk2QSI6IjAiLCI5NkIiOiIwIiwiOTZDIjoiMCIsIjk2RCI6IjAiLCI5NkUiOiIwIiwiMjkwIjoiMCIsIjk2RiI6IjAiLCI5NkciOiIwIiwiOTZIIjoiMCIsIjA1MSI6IjAiLCI5NkkiOiIwIiwiMDUyIjoiMCIsIjlYMCI6IjIiLCI2MTEiOiIwIiwiMkowIjoiMCIsIjYxMyI6IjAiLCI3MCI6IjAiLCI2MTUiOiIwIiwiNzEiOiIwIiwiNzIiOiIwIiwiNjE2IjoiMCIsIjczIjoiMCIsIjYxNyI6IjAiLCI3NCI6IjAiLCI2MTkiOiIwIiwiNzgiOiIwIiwiNzkiOiIwIiwiOVkwIjoiMiIsIjYyMSI6IjAiLCI1MDAiOiIzIiwiNjIyIjoiMCIsIjk4OCI6IjMiLCIxOEEiOiIwIiwiODIiOiIwIiwiODMiOiIwIiwiODQiOiIwIiwiODciOiIwIiwiNzNhIjoiMCIsIjczYiI6IjAiLCI2MWEiOiIwIiwiNzNjIjoiMCIsIjNNQSI6IjAiLCI3M2QiOiIwIiwiNzNmIjoiMCIsIjczZyI6IjMiLCI3M2giOiIwIiwiNzNpIjoiMCIsIjlaMCI6IjIiLCI5WjEiOiIyIiwiOTkxIjoiMCIsIjk5MiI6IjAiLCI5OTQiOiIyIiwiOVo1IjoiMCIsIjlaNiI6IjAiLCI5OTUiOiIwIiwiNzRTIjoiMCIsIjk5NiI6IjAiLCI5OTgiOiIzIiwiM01TIjoiMCIsIjE4YSI6IjAiLCIxOGIiOiIwIiwiMThjIjoiMCIsIjE4ZCI6IjAiLCI3NGEiOiIwIiwiNzRiIjoiMCIsIjYyYSI6IjAiLCI3NGMiOiIwIiwiNjJiIjoiMCIsIjYyYyI6IjAiLCI3NGUiOiIwIiwiNDAwIjoiMCIsIjQwMSI6IjAiLCI3NVMiOiIwIiwiNzZTIjoiMCIsIjMwMCI6IjAiLCIzMDEiOiIwIiwiMzAyIjoiMCIsIjMwMyI6IjAiLCIzMDQiOiIwIiwiMzA1IjoiMCIsIjMwNiI6IjAiLCIzMDgiOiIwIiwiMzA5IjoiMCIsIjMwQiI6IjAiLCIzMEMiOiIwIiwiMzBEIjoiMCIsIjMwRSI6IjAiLCIzQU0iOiIwIiwiRUVFIjoiMyIsIjIwMCI6IjAiLCIyMDEiOiIwIiwiMjAyIjoiMCIsIjIwMyI6IjAiLCIyMDQiOiIwIiwiMjA1IjoiMCIsIjIwNiI6IjAiLCIyMDciOiIwIiwiMjA4IjoiMCIsIjJBMiI6IjAiLCI0NTAiOiIwIiwiNDUxIjoiMCIsIjIxMyI6IjAiLCIyMTQiOiIwIiwiMjIwIjoiMCIsIjIyMSI6IjIiLCIyMjgiOiIyIiwiMTA3IjoiMCIsIjIyOSI6IjIiLCIxMDgiOiIwIiwiMTA5IjoiMCJ9LCJpbnN0aXR1Y2lvbiI6IjIwMDUiLCJncnVwbyI6IkFCTyIsInBlcmZpbGVzIjpbIidBQkcnIl0sImxldHJhZG8iOiJTIiwiZXhwIjoxNTg0NzgyNzYwfQ.P9rPJcoBbLuauqOpJDB5vPvQKiBAAsiHEIHwc9_0funysecn0aT8bhV573iI4wCLbP9g_BV-SY-PiPQJnOPe6g";
		}
	

		
		UsrBean bean=new UsrBean();
		if(token!=null && !token.equalsIgnoreCase("")){
			try {

				bean = gerUserFromJWTToken(token);
		
				
				BusinessManager bm = BusinessManager.getInstance();
				CenInstitucionService cenInstitucionService = (CenInstitucionService)bm.getService(CenInstitucionService.class);
				CenInstitucion cenInstitucion = new CenInstitucion();
				cenInstitucion.setIdinstitucion(Short.valueOf(bean.getLocation()));
				cenInstitucion = cenInstitucionService.get(cenInstitucion);
				Short idConsejo = AppConstants.IDINSTITUCION_2000;
				if(cenInstitucion.getCenInstIdinstitucion()!=null)
					idConsejo = cenInstitucion.getCenInstIdinstitucion();
				bean.setIdConsejo(idConsejo.intValue());
				CenInstitucion comision =  cenInstitucionService.getComision(cenInstitucion);
				bean.setIdInstitucionComision(comision.getIdinstitucion());
				if(bean.isComision()){
					List<CenInstitucion> instituciones =  cenInstitucionService.getInstitucionesComision(comision);
					if( instituciones!=null && instituciones.size()>0 ){
						Short[] institucionesComision  = new Short[instituciones.size()];
						for (int i = 0; i < instituciones.size(); i++) {
							institucionesComision[i]= instituciones.get(i).getIdinstitucion();
						}
						bean.setInstitucionesComision(institucionesComision);
					}else{
						bean.setInstitucionesComision(new Short[]{Short.parseShort(bean.getLocation())});
					}
					bean.setLocation(""+comision.getIdinstitucion());
				}
	
				
				// insertando registro de acceso en la tabla de estadisticas
				String profile;
				EstUserRegistryAdm userRegistryAdm = new EstUserRegistryAdm(bean);
				if (bean.getProfile() != null) {
					String perfiles = Arrays.toString(bean.getProfile());
					profile = perfiles.substring(1, perfiles.length() - 1);
				} else {
					profile = "-";
				}			
				if(!userRegistryAdm.insertarRegistroUser(profile)){
					ClsLogging.writeFileLog("***** ERROR AL REGISTRAR UN USUARIO EN EL EST_USER_REGISTRY *****",1);
				}
				
				HttpSession ses= request.getSession();
				if(ses!=null && ses.getAttribute(PARAMETRO.LONGITUD_CODEJG.toString())==null){
					GenParametrosService genParametrosService = (GenParametrosService) bm.getService(GenParametrosService.class);
					GenParametros genParametros = new GenParametros();
					genParametros.setIdinstitucion(cenInstitucion.getIdinstitucion());
					genParametros.setModulo(MODULO.SCS.toString());
					genParametros.setParametro(PARAMETRO.LONGITUD_CODEJG.toString());
					genParametros =  genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
					if (genParametros != null && genParametros.getValor() != null) {
						ses.setAttribute(PARAMETRO.LONGITUD_CODEJG.toString(), genParametros.getValor());
						ClsLogging.writeFileLog("Tama�o EJGs:"+genParametros.getValor(),1);
					} 
				}
				
				// Configuramos los estilos
				initStyles(bean.getLocation(), ses);
			}
			
			catch (Exception e) {
				
				if (e instanceof SIGAException) throw (SIGAException)e;
				throw new SIGAException("messages.general.errorAutenticacion",e);
			}
		}else{
			throw new SIGAException("TOKEN NO ENCONTRADO");
		}
		
		return bean;
	}


	public UsrBean gerUserFromJWTToken(String token) throws SIGAException {
		UsrBean user = new UsrBean();
		
		try {
		
			ClsLogging.writeFileLog(" >>> LECTURA DEL TOKEN >>>");
			String dni = Jwts.parser().setSigningKey(SECRET_SIGN_KEY).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
			ClsLogging.writeFileLog(" DNI         - " + dni);
			String institucion = (String) Jwts.parser().setSigningKey(SECRET_SIGN_KEY).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().get("institucion");
			ClsLogging.writeFileLog(" INSTITUCION - " + institucion);
			String grupo = (String) Jwts.parser().setSigningKey(SECRET_SIGN_KEY).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().get("grupo");
			ClsLogging.writeFileLog(" GRUPO       - " + grupo);
			HashMap<String, String> permisos = getPermisosFromJWTToken(token);
			String[] perfiles = getPerfilesFromToken(token);
			ClsLogging.writeFileLog(" PERFILES    - " + showPerfiles(perfiles));
			String letrado = (String) Jwts.parser().setSigningKey(SECRET_SIGN_KEY).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().get("letrado");
			ClsLogging.writeFileLog(" LETRADO     - " + letrado);			
			ClsLogging.writeFileLog(" <<< LECTURA DEL TOKEN <<<");
	
			user.setLocation(institucion);
			
			// Rellenamos el admUserBean con los siguientes datos
			AdmUsuariosBean admUserBean = getAdmUserBean(dni,user);

			// A�adimos al usuario sus perfiles
			user.setProfile(perfiles);
			
			// Le ponemos el idpersona correspondiente si lo tiene y si no se quedara -1
			long idPersona=setPersona(admUserBean.getNIF().toUpperCase(),user);
			// Ponemos el idioma que corresponda
			setIdioma(new Long(idPersona),Integer.valueOf(user.getLocation()),user);
			//
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;//new UserCgae(dni, grupo, institucion, permisos,perfiles,letrado);
	}
	
	private long setPersona(String nif,UsrBean bean) throws Exception {
		long retorno=-1; 
		bean.setIdPersona(-1);// Valor por defecto del retorno y del id de personna
		CenPersonaAdm personaAdm=new CenPersonaAdm(bean);
		Vector vec=personaAdm.select(" where ltrim(UPPER(" +CenPersonaBean.C_NIFCIF+"),'0')='"+UtilidadesString.LTrim(nif.toUpperCase(),"0")+"'");
		// Si no existe el usuario, salimos de la funci�n y pondemos el idusuario a -1
		if (vec!=null&&vec.size()==1) {		
			CenPersonaBean persona=(CenPersonaBean)vec.elementAt(0);
			Long idPersona=persona.getIdPersona();
			bean.setIdPersona(idPersona.longValue());
			retorno=idPersona.longValue();

		} else if (vec.size()>1) { // esto no se debe dar nunca
			throw new SIGAException("messages.general.errorUsuarioEfectivoDuplicado");
		}

		return retorno;
	}
	
	private String showPerfiles(String[] perfiles){
		String perfs = "";
		for(String s : perfiles)
            perfs+=s+" ";
		return perfs;
	}
	
	private String[] getPerfilesFromToken(String token){
		List<String> perfiles = (List<String>) Jwts.parser().setSigningKey(SECRET_SIGN_KEY).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().get("perfiles");
		
		String[] lista = new String[perfiles.size()];
		lista = perfiles.toArray(lista);
        
        return lista;
		
	}


	/**
	 * JBD
	 * @param nif
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	private AdmUsuariosBean getAdmUserBean(String nif, UsrBean bean) throws Exception {
		
		AdmUsuariosAdm rolObj=new AdmUsuariosAdm(bean);
		//Hacemos la comparacion del NIF sin los 0 por la izquierda
		Vector vec=rolObj.select(" where ltrim(" +AdmUsuariosBean.C_NIF +",'0')='"+UtilidadesString.LTrim(nif,"0")+"' AND "+
				AdmUsuariosBean.C_IDINSTITUCION+"="+bean.getLocation());
		
		AdmUsuariosBean admBean=null;
		if (vec.size()==1) { 
			// Si existe el usuario
			admBean=(AdmUsuariosBean)vec.elementAt(0);
		} else {
			// esto no se debe dar nunca
			throw new SIGAException("messages.general.errorUsuarioDuplicado");
		}
		if (!"S".equalsIgnoreCase(admBean.getActivo())) {
			throw new SIGAException("messages.general.errorUsuarioDesactivado");
		}

		return admBean;
	}

	public void initStyles(String location, HttpSession ses)
	{
		String iconsPath="/"+ClsConstants.PATH_DOMAIN+"/"+ClsConstants.RELATIVE_PATH_LOGOS;
		//por defecto los del CGAE
		String icon="logoconsejo2.gif";
		Properties props = PropertyReader.getProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String cssPath = props.getProperty(SIGAConstants.STYLESHEET_PATH);
		GenParametrosAdm paramAdm = new GenParametrosAdm((UsrBean)ses.getAttribute("USRBEAN"));
		
		try
		{
			SIGAGestorInterfaz interfazGestor=new SIGAGestorInterfaz(location);
			String version=paramAdm.getValor(String.valueOf(ClsConstants.INSTITUCION_CGAE),"GEN",SIGAConstants.VERSIONJS,"0");
			java.util.Properties stylesheet = interfazGestor.getInterfaceOptions();
			icon = interfazGestor.getLogoImg();
			ses.setAttribute(SIGAConstants.STYLESHEET_REF, stylesheet);
			ses.setAttribute(SIGAConstants.PATH_LOGO, iconsPath+"/"+icon);
			ses.setAttribute(SIGAConstants.STYLESHEET_SKIN, cssPath + "/skin" + stylesheet.get("color")+"/stylesheet.css?v="+version);
			ses.setAttribute(SIGAConstants.VERSIONJS, version);
		}

		catch(com.atos.utils.ClsExceptions ex)
		{
			com.atos.utils.ClsLogging.writeFileLogError(ex.getMessage(),ex, 3);
		}
	}

	private void setIdioma(Long idPersona, Integer idInstitucion,UsrBean bean) throws Exception{
		/* Si el usuario es cliente de la instituci�n, el idioma que tenga establecido
		 * se utilizar� para la sesi�n
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

		}
	
	}
	
	
}