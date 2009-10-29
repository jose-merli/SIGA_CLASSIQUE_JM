package com.siga.administracion.action;

import java.io.*;
import java.net.*;
import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import org.apache.struts.action.*;

import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.SIGAReferences;
import com.siga.administracion.form.*;


public class SIGAListadoCertificadosAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
    	UserTransaction tx = null;
    	String salida = "";
    	try {
	    	SIGAListadoCertificadosForm form = (SIGAListadoCertificadosForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        AdmCertificadosAdm certificadosAdm = new AdmCertificadosAdm (this.getUserBean(request));
	        
	        String institucion = userBean.getLocation();
	        String descripcion = form.getDescripcion();
	        String revocacion = form.getRevocacionConsulta();
	
	        String where = " ";
	        Vector datos = null;
	        
	        
	        HashMap databackup=new HashMap();
			
		if (request.getSession().getAttribute("DATAPAGINADOR")!=null){//Si estamos paginando 
			 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
				     Paginador paginador = (Paginador)databackup.get("paginador");
				      datos=new Vector();
				
				
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				
				 
				
				
				if (pagina!=null){
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}
				
				
				
				
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
				
					
				
				
		  }else{//si es la primera vez que realizamos la busqueda	
				
		  	    databackup=new HashMap();
				
				//obtengo datos de la consulta 			
			Paginador resultado = null;
			
//			
				
				   
			
		  
				
	        
	        
	        
	        
	        where += "C." + AdmCertificadosBean.C_IDINSTITUCION + " = " + institucion;
	        
	        if (descripcion!=null && !descripcion.equals(""))
	        {
		        where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(descripcion.trim(),AdmUsuariosBean.C_DESCRIPCION);
	        }
	
	        where += " AND C." + AdmCertificadosBean.C_IDUSUARIO + " = U." + AdmUsuariosBean.C_IDUSUARIO;
	        where += " AND C." + AdmCertificadosBean.C_IDINSTITUCION + " = U." + AdmUsuariosBean.C_IDINSTITUCION;
	
	        if (revocacion!=null && !revocacion.equals(""))
	        {
	            where += " AND " + AdmCertificadosBean.C_REVOCACION + " = '" + revocacion + "'";            
	        }
	        
	         resultado = certificadosAdm.selectConDescripcionUsuario(where);
	        databackup.put("paginador",resultado);
			if (resultado!=null){ 
			   datos = resultado.obtenerPagina(1);
			   databackup.put("datos",datos);
			   request.getSession().setAttribute("DATAPAGINADOR",databackup);
			}   
		  }
		
	        for (int i=0; datos!=null && i<datos.size(); i++)
	        {
	        	/*Hashtable registro = (Hashtable)datos.elementAt(i);
	        	
	            AdmCertificadosBean beanCertificado = (AdmCertificadosBean)registro.get("datos");*/
	        	Row fila = (Row)datos.elementAt(i);
	    	    Hashtable hashDatos = (Hashtable) fila.getRow();
	            
	            if (hashDatos.get("REVOCACION").equals("S"))
	            {
	                tx = userBean.getTransaction();
	                
	                StringBuffer mensaje = new StringBuffer();
	                
                    String serialNumber =(String) hashDatos.get("NUMSERIE");

                    serialNumber = serialNumber.replaceAll(" ", "");
                    serialNumber = serialNumber.replaceAll(" ", "");
                    
                    int iPasadas = serialNumber.length()/2;
                    
                    for (int j=0; j<iPasadas; j++)
                    {
                        serialNumber = serialNumber.substring(0,j+(2*j)+2)+" "+serialNumber.substring(j+(2*j)+2);
                    }
                    
                    serialNumber = serialNumber.trim();
                    
        		    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//                    ReadProperties rp3 = new ReadProperties("SIGA.properties");
                    
                    String sURLServidor = rp3.returnProperty("ACA.URL.EJB.validador");
                    String sParametroNumeroSerie = rp3.returnProperty("ACA.parametro.numero.serie");
                    
                    String sURL = sURLServidor + "?" + sParametroNumeroSerie + "=";
                    sURL += "\"" + serialNumber + "\"";
	                
	                URL direction = new URL(sURL);
	
	                // Trazamos los siguientes valores: serialNumber, sURLServidor, sParametroNumeroSerie, sURL

					mensaje.append(" BUSQUEDA CERTIFICADOS ACA: serialNumber: " + serialNumber);
					mensaje.append(" + sParametroNumeroSerie: " + sParametroNumeroSerie);
					mensaje.append(" + sURL: " + sURL);
					mensaje.append(" + sURLServidor: " + sURLServidor);
					ClsLogging.writeFileLog(mensaje.toString(), 10);
					
					URLConnection conexion = null;
					
					try {
						conexion = direction.openConnection();
						conexion.setUseCaches(false);
						conexion.connect();
					} catch (Exception e) {
						e.printStackTrace();
						throw new SIGAException("messages.certificados.error.NoConexionHTTP",e);
					}

					int iChar=0;
					String sChar="";
					
					InputStream is = null;
					try {
						is = conexion.getInputStream();
					} catch (Exception e) {
						e.printStackTrace();
						throw new SIGAException("messages.certificados.error.NoConexionHTTP",e);
					}

					if (is==null) {
						throw new SIGAException("messages.certificados.error.NoConexionHTTP");
					}
					
					while ((iChar = is.read())!=-1)
					{
					    sChar += (char)iChar;
					}
	
					is.close();
					
					ClsLogging.writeFileLog("VARIABLE sChar= "+sChar, 10);
					
					String sEtiquetaInicio = rp3.returnProperty("ACA.etiqueta.valor.retorno");
					String sEtiquetaFin = "</" + sEtiquetaInicio.substring(1, sEtiquetaInicio.length());
					int iLongitudEtiqueta = sEtiquetaInicio.length();
					String sRetorno="";
					if (sChar!=null && !sChar.equals("")){
					 sRetorno = sChar.substring(sChar.indexOf(sEtiquetaInicio)+iLongitudEtiqueta, sChar.indexOf(sEtiquetaFin));
					} 
					 boolean bBorrar=false;
					
					if (sRetorno!=null && !sRetorno.trim().equals(""))
					{
						String sCodigosNoValidos = rp3.returnProperty("ACA.codigos.no.validos");
						
						String[] aCodigosNoValidos = sCodigosNoValidos.split(",");
						
						for (int j=0; j<aCodigosNoValidos.length; j++)
						{
						    if (sRetorno.equals(aCodigosNoValidos[j]))
						    {
						        bBorrar=true;
						        j=aCodigosNoValidos.length;
						    }
						}
					}
					
					if (bBorrar)
					{
					    tx.begin();
					    
					    AdmUsuarioEfectivoAdm admUsuarioEfectivo = new AdmUsuarioEfectivoAdm(this.getUserBean(request));
					    
					    Vector vUsuariosEfectivos = admUsuarioEfectivo.select(" WHERE " + AdmUsuarioEfectivoBean.C_IDINSTITUCION + " = " + hashDatos.get("INSTITUCION") + " AND " + AdmUsuarioEfectivoBean.C_IDUSUARIO + " = " + hashDatos.get("IDUSUARIO"));
					    
				        for (int j=0; j<vUsuariosEfectivos.size(); j++)
				        {
				            AdmUsuarioEfectivoBean beanUsuarioEfectivo = (AdmUsuarioEfectivoBean)vUsuariosEfectivos.elementAt(j);
				            beanUsuarioEfectivo.setNumSerie("");
				            
				            admUsuarioEfectivo.delete(beanUsuarioEfectivo);
				        }

					    AdmCertificadosAdm admCertificado = new AdmCertificadosAdm(this.getUserBean(request));
					    
					    if (admCertificado.delete(hashDatos))
					    {
					        datos.remove(i);
					    }

				        tx.commit();
					}
	            }
	        }
	        
	        request.setAttribute("datos", datos);
	        
	        salida = "buscar";

    	} catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
        }
    	
    	return salida;
	        
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
        SIGAListadoCertificadosForm form = (SIGAListadoCertificadosForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmCertificadosAdm certificadosAdm = new AdmCertificadosAdm (this.getUserBean(request));
        
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
        Hashtable hashNew = (Hashtable)hashOld.clone();
	    
	    hashNew.put(AdmCertificadosBean.C_REVOCACION, form.getRevocacion());
	    
        if (certificadosAdm.update(hashNew, hashOld))
        {
            request.setAttribute("mensaje","messages.updated.success");
         
            String mensaje = "El Certificado \"" + hashOld.get(AdmCertificadosBean.C_NUMSERIE) + "\" del Usuario con NIF = \"" + hashOld.get(AdmCertificadosBean.C_NIF) + "\" ha sido ";	

            if (form.getRevocacion().equals("S"))
            {
                mensaje += "puesto en estado \"Pendiente de Revocación\"";
            }
            
            else
            {
                mensaje += "quitado del estado \"Pendiente de Revocación\"";
            }
            
            CLSAdminLog.escribirLogAdmin(request,mensaje);

            request.removeAttribute("DATABACKUP");
        }
        
        else
        {
            request.setAttribute("mensaje","messages.updated.error");
        }

        request.setAttribute("modal","1");
	    
        return "exito";
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAListadoCertificadosForm form = (SIGAListadoCertificadosForm)formulario;
	    
	    form.setModal("false");
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    AdmUsuariosAdm usuariosAdm = new AdmUsuariosAdm (this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    
	    Hashtable hash = new Hashtable();
	    
	    hash.put(AdmUsuariosBean.C_IDUSUARIO, (String)vOcultos.elementAt(0));
	    hash.put(AdmUsuariosBean.C_IDINSTITUCION, userBean.getLocation());
	    
	    if (usuariosAdm.delete(hash))
	    {
	        request.setAttribute("mensaje","messages.deleted.success");
	        
	        String mensaje = "Los Certificados del Usuario con ID=\"" + (String)vOcultos.elementAt(0) + "\"  han sido borrados";
	        
	        CLSAdminLog.escribirLogAdmin(request,mensaje);
	    }
	    
	    else
	    {
	        request.setAttribute("mensaje","error.messages.deleted");
	    }

	    request.setAttribute("urlAction", "/SIGA/ADM_GestionarUsuarios.do?modo=buscar");
        request.setAttribute("hiddenFrame", "1");
	    
	    return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions
	{
        SIGAListadoCertificadosForm form = (SIGAListadoCertificadosForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        AdmCertificadosBean certificadosBean = new AdmCertificadosBean();
        
		Vector vVisibles = form.getDatosTablaVisibles(0);
		Vector vOcultos = form.getDatosTablaOcultos(0);		

        String idUsuario = (String)vOcultos.elementAt(0);
        String idInstitucion = (String)vOcultos.elementAt(1);
        String numSerie = (String)vOcultos.elementAt(2);
        String revocacion = (String)vOcultos.elementAt(3);
        String fechaCad = (String)vOcultos.elementAt(4);
        String descripcion = (String)vOcultos.elementAt(5);
        String nif = (String)vOcultos.elementAt(6);
        String rol = (String)vOcultos.elementAt(7);
        String roles = (String)vOcultos.elementAt(8);
        String usuMod = (String)vOcultos.elementAt(9);
        String fechaMod = (String)vOcultos.elementAt(10);
        
        certificadosBean.setIdUsuario(new Integer(idUsuario));
        certificadosBean.setIdInstitucion(new Integer(idInstitucion));
        certificadosBean.setNumSerie(numSerie);
        certificadosBean.setRevocacion(revocacion);
        certificadosBean.setFechaCad(fechaCad);
        certificadosBean.setNIF(nif);
        certificadosBean.setRol(rol);
        certificadosBean.setRoles(roles);
        certificadosBean.setUsuMod(new Integer(usuMod));
        certificadosBean.setFechaMod(fechaMod);

        Vector datos = new Vector();
        datos.add(certificadosBean);
        datos.add(descripcion);
        
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(AdmCertificadosBean.C_IDUSUARIO, idUsuario);
            hashBackUp.put(AdmCertificadosBean.C_IDINSTITUCION, idInstitucion);
            hashBackUp.put(AdmCertificadosBean.C_NUMSERIE, numSerie);
            hashBackUp.put(AdmCertificadosBean.C_REVOCACION, revocacion);
            hashBackUp.put(AdmCertificadosBean.C_FECHACAD, fechaCad);
            hashBackUp.put(AdmCertificadosBean.C_NIF, nif);
            hashBackUp.put(AdmCertificadosBean.C_ROL, rol);
            hashBackUp.put(AdmCertificadosBean.C_USUMODIFICACION, usuMod);
            hashBackUp.put(AdmCertificadosBean.C_FECHAMODIFICACION, fechaMod);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }

		return "mostrar";
	}
}