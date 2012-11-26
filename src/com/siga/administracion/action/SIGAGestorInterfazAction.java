package com.siga.administracion.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.CLSAdminLog;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.administracion.SIGAConstants;
import com.siga.administracion.SIGAGestorInterfaz;
import com.siga.administracion.form.SIGAGestorInterfazForm;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * @author tomas.narros
 *
 */
public class SIGAGestorInterfazAction extends MasterAction {
	
	
	protected  String abrir(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");		
		String idInstitucion=user.getLocation();
		String result="success";
		
		try
		{
			SIGAGestorInterfaz gestor=new SIGAGestorInterfaz(idInstitucion);
				
			if(request.getParameter("mode")==null)
			{				
				result="listOptions";			
				request.setAttribute(SIGAConstants.COLOR, gestor.getColorCode());
				request.setAttribute(SIGAConstants.FONT, gestor.getFontTypeCode());
				request.setAttribute(SIGAConstants.LOGO, gestor.getLogoImg());
				//Si hemos realizado un cambio debemos refrescar y avisar despues del cambio:
				String refrescar = request.getSession().getAttribute("refrescar")==null?"NO":"SI";
				request.setAttribute("refrescar", refrescar);
				//Borro de sesion el atributo del refresco:
				request.getSession().removeAttribute("refrescar");
			}			
			else
			{
			    SIGAGestorInterfazForm f = (SIGAGestorInterfazForm) formulario;
				//retrieve the query string value
				String queryValue = f.getQueryParam();
       
				//retrieve the file representation
				FormFile theFile = f.getTheFile();
//			    if(theFile==null || theFile.getFileSize()<1)
//			    	throw new SIGAException("messages.general.error.ficheroNoExiste");

				String contentType = "";
			    String size = "";
			    // RGG Si hace falta se pone el id de la institucion
			    //String nombreLogo = user.getLocation();
			    String nombreLogo = gestor.getLogoImg();
			    
			    String anteriorLogo = gestor.getLogoImg();
			    String anteriorLetra = gestor.getFontTypeCode();
			    String anteriorColor = gestor.getColorCode();
			    String mensaje = "";			
					
			    String pathImagenes = "";
			    
			    // obtencion del path app desde tabla parametros
			    GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
				
			    ReadProperties rpSIGA= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			    pathImagenes = rpSIGA.returnProperty("directorios.carpeta.logos")+ File.separator;
			    
			    /* RGG 13/03/2005 cambiado por la funcion de arriba 
			    Hashtable hashParams = new Hashtable();
				UtilidadesHash.set(hashParams, GenParametrosBean.C_IDINSTITUCION, user.getLocation());
				UtilidadesHash.set(hashParams, GenParametrosBean.C_MODULO, ClsConstants.MODULO_CENSO);
				UtilidadesHash.set(hashParams, GenParametrosBean.C_PARAMETRO, ClsConstants.PATH_APP);
				Vector auxV = paramAdm.select(hashParams);
				pathImagenes = ((GenParametrosBean)auxV.get(0)).getValor();
				*/
				
				//pathImagenes += File.separator + ClsConstants.RELATIVE_PATH_LOGOS + File.separator;
			    
			    if(theFile.getFileSize()>0){
			    	contentType=theFile.getContentType();
			    	
			    	
			    	size = (theFile.getFileSize() + " bytes");

			    	String data = null;
			    	InputStream stream =null;
			    	nombreLogo = f.getTheFile().getFileName();

			    	String extension = nombreLogo.substring(nombreLogo.lastIndexOf("."),nombreLogo.length());
			    	// RGG control de la extension
			    	if (extension==null || extension.trim().equals("")
			    			|| (!extension.trim().toUpperCase().equals(".JPG")
						    && !extension.trim().toUpperCase().equals(".GIF")
							&& !extension.trim().toUpperCase().equals(".JPEG"))) {
			    		
			    		throw new SIGAException("messages.error.imagen.tipoNoCorrecto");
			    	}
			    	

			    	// MAV 24/8/2005 Para evitar machacar logos, al nombre le incorporamos ident institución y num. aleatorio y si aun asi coincide lo consideraremos como una senhal del destino
			    	Random aleatorio=new Random();
			    	nombreLogo=user.getLocation()+"_"+aleatorio.nextInt()+"_"+nombreLogo;
			    	
			    	OutputStream bos = null;
			    	try {			
			    		//retrieve the file data
//			    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    		stream = theFile.getInputStream();
			    		//write the file to the file specified

			    		// RGG para asegurar que se crea el path
			    		File aux = new File(pathImagenes);
			    		aux.mkdirs();
			    		
			    		bos = new FileOutputStream(pathImagenes+nombreLogo);
			    		int bytesRead = 0;
			    		byte[] buffer = new byte[8192];
			    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			    			bos.write(buffer, 0, bytesRead);
			    		}			    
			    		data = "The file has been written to \"" + pathImagenes + "\"";
			    	} catch (FileNotFoundException fnfe) {
			    		return null;
			    	}catch (IOException ioe) {
			    		return null;
			    	}
			    	finally	{
//					close the stream
			    		bos.close();
			    		stream.close();
			    	}
			    }//if
            	UserTransaction tx=user.getTransaction();
				try {
					tx.begin();
					gestor.setUserName(user.getUserName());
					gestor.setColorCode(f.getIdColor());					
					gestor.setFontTypeCode(f.getIdTipoLetra());
					gestor.setLogoImg(nombreLogo);					
					if(gestor.actualizarInterfaz()) {
						tx.commit();
						//anotamos el cambio en el fichero log con el mensaje del cambio.
						if(!f.getIdColor().equals(anteriorColor)){
							mensaje = "Se ha modificado el esquema de colores a "+f.getIdColor()+". ";
						}
						if(!f.getIdTipoLetra().equals(anteriorLetra)){		
						    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.INTERFACE);
//							ReadProperties rp=new ReadProperties("interface.properties");
						    String aux =rp.returnProperty("font.style."+f.getIdTipoLetra());
							
							mensaje = mensaje + "Se ha modificado el tipo de letra a "+aux+". ";
						}
						if(!nombreLogo.equals(anteriorLogo)){
							mensaje = mensaje + "Se ha modificado el logo";
						}
						
						CLSAdminLog.escribirLogAdmin(request, mensaje);
						java.util.Properties stylesheet = gestor.getInterfaceOptions();
						request.getSession().setAttribute(SIGAConstants.STYLESHEET_REF, stylesheet);
						// RGG PATH LOGOS String iconsPath="/"+ClsConstants.PATH_DOMAIN+"/"+ClsConstants.RELATIVE_PATH_IMAGES+ "/";
						String iconsPath="/"+ClsConstants.PATH_DOMAIN+"/"+ClsConstants.RELATIVE_PATH_LOGOS+ "/";
						request.getSession().setAttribute(SIGAConstants.PATH_LOGO, iconsPath + nombreLogo);
						//No aviso con un mensaje hasta que no haya refrescado:
						//request.setAttribute("descOperation","messages.updated.success");
						//Aviso para que refresque al recargar los frames:
						request.getSession().setAttribute("refrescar", "SI");
					} else {
						tx.rollback();
						request.setAttribute("descOperation","messages.updated.error"); 
						throw new ClsExceptions ("Error en el proceso de configuración del interfaz.");
					}
				} catch (Exception ex){
				  	throw new ClsExceptions (ex,ex.toString(),"","0","GEN00","15");
				}				

				// RGG para que salte al gestor de interfaz tras salvar
				request.getSession().setAttribute("InterfazInicio","S");

			}
		} catch (Exception e) { 
			 throwExcp("messages.general.error",new String[] {"modulo.administracion"},e,null);		
		}
		return result;
	}

}
