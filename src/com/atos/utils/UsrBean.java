/**
 * <p>Title: UsrBean </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.CenVisibilidad;


public class UsrBean implements Serializable {

	private String idRol="";
	private long idPersona=-1;
	private String userName = "";
	private String profile[] = null;
	private String language = "";
	private String languageExt = "";
	private String languageInstitucion = "";
	private String access_type = "";
	private String userDescription = "";
	private String location = "";
	private int pkiCertificateType = 1;
	private boolean pki = false;
	private String trans = "javax.transaction.UserTransaction";
	private String descTrans = "";
	private String strutsTrans = "nothing.do";
	private boolean letrado = true; // ACG se deberá inicializar a false
	private boolean comision = false;
	private AccessControl accessControl=null;
	private Hashtable datosUsuario = null;
	private Boolean ocultarHistorico = null;
	private boolean aplicarLOPD = false;
	private static final String ROL_COMISION ="CJG"; // Es el rol que corresponde a la comision

	public UsrBean() {
	    ReadProperties ldapProperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.JNDI);
//		ReadProperties ldapProperties = new ReadProperties("jndi.properties");
		trans = ldapProperties.returnProperty("JNDI.TX");
	}

	public UsrBean(String _usuario, String _lenguaje) {
		try {
		    ReadProperties ldapProperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.JNDI);
//		    ReadProperties ldapProperties = new ReadProperties("jndi.properties");
			trans = ldapProperties.returnProperty("JNDI.TX");
			language = _lenguaje;
			UsrBean uu = new UsrBean();
			uu.setLanguage("1");
			AdmLenguajesAdm admLen = new AdmLenguajesAdm(uu);
			languageExt = admLen.getLenguajeExt(_lenguaje);
			userName = _usuario;
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	static public UsrBean UsrBeanAutomatico(String idInstitucion) {
	    UsrBean us = null;
	    try {
		    us = new UsrBean();
		    UsrBean usAux = new UsrBean();
		    usAux.setLanguage("1");
			AdmLenguajesAdm admLen = new AdmLenguajesAdm(usAux);
			AdmLenguajesBean beanLen = admLen.getLenguajeInstitucion(idInstitucion); 
			us.setLocation(idInstitucion);
			us.setLanguage(beanLen.getIdLenguaje());
			us.setLanguageExt(beanLen.getCodigoExt());
			us.setLanguageInstitucion("1");
			us.setUserName(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString());
		} catch (Exception e) {
		    e.printStackTrace();
		}			
		return us;
	}

	public String getStrutsTrans() {
		return strutsTrans;
	}
	public void setStrutsTrans(String _strutsTrans) {
		strutsTrans = _strutsTrans;
	}

	public String getDescTrans() {
		return descTrans;
	}
	public void setDescTrans(String _descTrans) {
		descTrans = _descTrans;
	}

	public String getUserDescription() {
		return userDescription;
	}
	public void setUserDescription(String _userDescription) {
		userDescription = _userDescription;
	}

	/**
	 *
	 * @return userName
	 */

	public String getUserName() {
		return userName;
	}
	public void setUserName(String usr) {
		userName = usr;
	}

	public String[] getProfile() {
		return profile;
	}
	public void setProfile(String[] prof) {
		profile = prof;
		// Se establece si el usuario pertenece a la comision
		this.setComision(false);
		for (int i = 0; i < prof.length; i++) {	
			if(prof[i].equalsIgnoreCase(ROL_COMISION)){
				this.setComision(true);
			}
		}
	}

	public UserTransaction getTransaction() {
		UserTransaction tx = null;
		try {
			Context ctx = new InitialContext();
			tx = (UserTransaction) ctx.lookup(trans);
		} catch (NamingException ex) {
			ex.printStackTrace();
		}

		return tx;
	}

	public UserTransaction getTransactionLigera() {
		UserTransaction tx = null;
		tx=getTransaction();
		// RGG para limitacion de tiempo de transacciones
		try {
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");
			tx.setTransactionTimeout(new Integer(rp.returnProperty("siga.jta.timeout.ligera")).intValue());
		} catch (SystemException se) {
			ClsLogging.writeFileLogError("Error al establecer JTA timeout LIGERA",se,3);
		}
		return tx;
	}

	public UserTransaction getTransactionPesada() {
		UserTransaction tx = null;
		tx=getTransaction();
		try {
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");
			tx.setTransactionTimeout(new Integer(rp.returnProperty("siga.jta.timeout.pesada")).intValue());
		} catch (SystemException se) {
			ClsLogging.writeFileLogError("Error al establecer JTA timeout PESADA",se,3);
		}
		return tx;
	}

	public void setTransaction(UserTransaction tran) {
		//transaction=tran;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String lan) {
		language = lan;
	}
	public String getLanguageExt() {
		return languageExt;
	}

	public void setLanguageExt(String lan) {
		languageExt = lan;
	}
	public String getAccessType() {
		return access_type;
	}
	public String getLanguageInstitucion() {
		return languageInstitucion;
	}
	public void setLanguageInstitucion(String languageInstitucion) {
		this.languageInstitucion = languageInstitucion;
	}
	public void setAccessType(String acc) {
		access_type = acc;
	}
	public boolean getPki() {
		return pki;
	}
	public void setPki(boolean _pki) {
		pki = _pki;
	}

	public int getCertificationType() {
		return pkiCertificateType;
	}
	public void setCertificationType(int _pki) {
		pkiCertificateType = _pki;
	}
	/**
	 * @return The code for the current User Location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param locationCode The code for the current User Location
	 */
	public void setLocation(String locationCode) {
		location = locationCode;
	}
	public int getLevel(){
		// Hay que implementar el algoritmo de cálculo del nivel de administración
	    try
	    {
	        return Integer.parseInt(CenVisibilidad.getNivelInstitucion(location));
	    }
	    
	    catch(Exception e)
	    {
	        return Integer.MAX_VALUE;
	    }
	}
	public void setAdminLevel(int level){
		}
	
	public boolean getLetrado() {
		return letrado;
	}
	public void setLetrado(boolean _letrado) {
		letrado = _letrado;
	}
	public boolean isLetrado() {
		// Hay que implementar si es letrado cuando se coja del certificado digital
		return  letrado;
	}
	
	public void setComision(boolean _comision) {
		comision = _comision;
	}
	public boolean isComision() {
		return  comision;
	}
	
	public String getAccessForProcessNumber(String process) {
		if (accessControl==null) {
			accessControl=new AccessControl(); 
		}
		try {
			String aa=accessControl.checkAccessByProcessNumber(getProfile(),process,Integer.parseInt(getLocation()));
			setAccessType(aa);
			return getAccessType();
		} catch (Exception e) {
			return SIGAConstants.ACCESS_DENY;
		}
	}
	
	public String getAccessForProcessName(String process) {
		if (accessControl==null) {
			accessControl=new AccessControl(); 
		}
		try {
			String aa=accessControl.checkAccessByProcessName(getProfile(),process,Integer.parseInt(getLocation()));
			
//			 consideramos este caso en particular porque como la busqueda de colegiados, no colegiados y letrados lo gestiona el mismo action (busqueda de clientes)
//           los permisos de busqueda de clientes siempre prevalecian sobre los otros, de esta manera mantendremos los permisos que tenga cada hijo de
//           la busqueda de cliente			
			if (process.equals("CEN_BusquedaClientes")||process.equals("10")){
				                                                                
				return getAccessType(); 
			}
			
			setAccessType(aa);
			return getAccessType();
		} catch (Exception e) {
			return SIGAConstants.ACCESS_DENY;
		}
	}
	public long getIdPersona(){
		return this.idPersona;
	}
	public void setIdPersona(long _idPersona){
		this.idPersona=_idPersona;
	}
	public void setIdRol(String _idrol){
		this.idRol=_idrol;
	}
	public String getIdRol(){
		return this.idRol;
	}
	
	/**
	 * @return Returns the hDatosUsuario.
	 */
	public Hashtable getDatosUsuario() 
	{
		if (this.datosUsuario == null) {
			
			AdmUsuariosAdm usuAdm = new AdmUsuariosAdm(this);
			Vector v = usuAdm.getDatosUsuario(this.getUserName(), this.getLocation());
			
			if ((v == null || v.size() != 1)) { 
				this.datosUsuario = null;
			}
			this.datosUsuario = (Hashtable)v.get(0);
		}
		return this.datosUsuario;
	}
	
	public boolean getOcultarHistorico() 
	{
		if (this.ocultarHistorico == null) {
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this); 
			try {
				String ocultar = paramAdm.getValor(this.getLocation(), ClsConstants.MODULO_CENSO, ClsConstants.OCULTAR_MOTIVO_MODIFICACION, "N");
				this.ocultarHistorico = new Boolean(UtilidadesString.stringToBoolean(ocultar));
			} 
			catch (ClsExceptions e) {
				this.ocultarHistorico = new Boolean(false);
			}
		}
		return this.ocultarHistorico.booleanValue();
	}

	public boolean isAplicarLOPD() {
		return aplicarLOPD;
	}

	public void setAplicarLOPD(boolean aplicarLOPD) {
		this.aplicarLOPD = aplicarLOPD;
	}
	
	public String getPermisoProceso(String process) {
		if (accessControl==null) {
			accessControl=new AccessControl(); 
		}
		try {
			String aa=accessControl.checkAccessByProcessName(getProfile(),process,Integer.parseInt(getLocation()));
			
			if (process.equals("CEN_BusquedaClientes")||process.equals("10")){
				                                                                
				return getAccessType(); 
			}
			return aa;
		} catch (Exception e) {
			return SIGAConstants.ACCESS_DENY;
		}
	}
}