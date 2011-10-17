/*
 * Created on 13-abr-2005
 *
 */
package com.siga.envios;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.jcraft.jsch.HASH;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.beans.EnvCamposEnviosAdm;
import com.siga.beans.EnvComunicacionMorososAdm;
import com.siga.beans.EnvComunicacionMorososBean;
import com.siga.beans.EnvDestinatariosAdm;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvDocumentosAdm;
import com.siga.beans.EnvDocumentosBean;
import com.siga.beans.EnvDocumentosDestinatariosAdm;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvEstadoEnvioAdm;
import com.siga.beans.EnvListaCorreosBean;
import com.siga.beans.EnvListaCorreosEnviosAdm;
import com.siga.beans.EnvListaCorreosEnviosBean;
import com.siga.beans.EnvPlantillaGeneracionAdm;
import com.siga.beans.ExpAnotacionAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsProcuradorAdm;
import com.siga.beans.ScsProcuradorBean;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.certificados.Plantilla;
import com.siga.general.SIGAException;


/**
 * @author juan.grau
 *
 */

/**
 * @author juan.grau
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Envio
{
	
    public EnvEnviosBean enviosBean;
    private UsrBean usrBean;
    
    private String consulta=null;

	public void setConsulta(String consulta)
	{
		this.consulta=consulta;
	}
	
	public EnvEnviosBean getEnviosBean() {
		return enviosBean;
	}

	public void setEnviosBean(EnvEnviosBean enviosBean) {
		this.enviosBean = enviosBean;
	}

	public String getConsulta()
	{
		return this.consulta;
	}
	
    /**
     * @param enviosBean Bean del envío
     * @param idUsuario id del usuario
     */
    public Envio(EnvEnviosBean enviosBean, UsrBean userBean)
    {        
        this.enviosBean = enviosBean;
        this.usrBean = userBean;	    
    }
    
    /**
     * @param userBean Bean del usuario
     * @param descripcion descripción del envío
     */
    public Envio(UsrBean _userBean, String descripcion) throws SIGAException
    {        
        this.usrBean = _userBean;
        String idInstitucion = this.usrBean.getLocation();
        
        EnvEnviosBean enviosBean = new EnvEnviosBean();
        EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this.usrBean);
        GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrBean);
        
        try {
        	enviosBean.setIdInstitucion(new Integer(idInstitucion));
            enviosBean.setIdEnvio(enviosAdm.getNewIdEnvio(this.usrBean));        
	        enviosBean.setDescripcion(descripcion);
			// trunco la descripción
			if (enviosBean.getDescripcion().length()>200)  enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,99));

	        enviosBean.setFechaCreacion("SYSDATE");
	        enviosBean.setFechaProgramada("SYSDATE");        
            enviosBean.setGenerarDocumento(paramAdm.getValor(idInstitucion,"ENV","GENERAR_DOCUMENTO_ENVIO","C"));
            enviosBean.setImprimirEtiquetas(paramAdm.getValor(idInstitucion,"ENV","IMPRIMIR_ETIQUETAS_ENVIO","1"));
            enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));
            
            //Controlamos el valor preferente:
			String preferencia = paramAdm.getValor(idInstitucion,"ENV","TIPO_ENVIO_PREFERENTE","1");
			Integer valorPreferencia = Envio.calculaTipoEnvio(preferencia);
            enviosBean.setIdTipoEnvios(valorPreferencia);
        } catch (ClsExceptions e1) {
            throw new SIGAException("messages.general.error",e1);
        }
        
        this.enviosBean = enviosBean;
    }
  
    /**
     *  Método que inserta en la tabla de remitentes la institución 
     *  que ha creado el envío
     */
    /*
    public void addRemitentes() throws SIGAException{
        
        try {
            CenInstitucionAdm instAdm = new CenInstitucionAdm(idUsuario);
	        Hashtable htPk = new Hashtable();
	        htPk.put(CenInstitucionBean.C_IDINSTITUCION,enviosBean.getIdInstitucion());
	        CenInstitucionBean instBean = (CenInstitucionBean)instAdm.selectByPK(htPk).firstElement();       
	        
	        Long idPersona = new Long(instBean.getIdPersona().longValue());
	        CenClienteAdm clienteAdm = new CenClienteAdm(idUsuario);
	        Vector direcciones = clienteAdm.getDirecciones(idPersona,enviosBean.getIdInstitucion());                
	        
	        EnvRemitentesBean remBean = new EnvRemitentesBean();
	        remBean.setIdEnvio(enviosBean.getIdEnvio());
	        remBean.setIdInstitucion(enviosBean.getIdInstitucion());
	        remBean.setIdPersona(idPersona);
	        remBean.setDescripcion("");
	        
	        if (direcciones!=null && direcciones.size()>0) {
		        Hashtable htDir = (Hashtable) direcciones.firstElement();
		        remBean.setDomicilio((String)htDir.get(CenDireccionesBean.C_DOMICILIO));
		        remBean.setIdPoblacion((String)htDir.get(CenDireccionesBean.C_IDPOBLACION));
		        remBean.setPoblacionExtranjera((String)htDir.get(CenDireccionesBean.C_POBLACIONEXTRANJERA));
		        remBean.setIdProvincia((String)htDir.get(CenDireccionesBean.C_IDPROVINCIA));
		        remBean.setIdPais((String)htDir.get(CenDireccionesBean.C_IDPAIS));
		        if (remBean.getIdPais().equals("")) remBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
		        remBean.setCodigoPostal((String)htDir.get(CenDireccionesBean.C_CODIGOPOSTAL));
		        remBean.setCorreoElectronico((String)htDir.get(CenDireccionesBean.C_CORREOELECTRONICO));
		        remBean.setFax1((String)htDir.get(CenDireccionesBean.C_FAX1));
		        remBean.setFax2((String)htDir.get(CenDireccionesBean.C_FAX2));
	        }
	        addRemitentes(remBean);
	        
        } catch (SIGAException e1) {
            throw e1;
        } catch (Exception e1) {
            throw new SIGAException("messages.general.error",e1);
        }
    }
    */
    /**
     * Método que adjunta una serie de documentos a un destinatario.
     * Si el destinatario no existe, lo crea.
     * 
     * @param idPersona Persona destinataria
     * @param documentos Vector de objetos Documento
     * @throws SIGAException
     */
    public void addDocumentosDestinatario(String idPersona,String tipoDestinatario, Vector documentos ) throws SIGAException{
        
        try{
            EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.usrBean);
            EnvDestinatariosBean destBean = null;
	        boolean crearDestinatario;
	        //
            if (!destAdm.existeDestinatario(String.valueOf(enviosBean.getIdEnvio()),
                    						String.valueOf(enviosBean.getIdInstitucion()),
                    						idPersona)){                
            
	            
	            destBean = new EnvDestinatariosBean();
		        destBean.setIdEnvio(enviosBean.getIdEnvio());
		        destBean.setIdInstitucion(enviosBean.getIdInstitucion());
		        destBean.setIdPersona(Long.valueOf(idPersona));
		        destBean.setTipoDestinatario(tipoDestinatario);
		        
	            Vector direcciones = null;
	            if(tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA)){
	            	EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this.usrBean);
	            	direcciones = enviosAdm.getDirecciones(String.valueOf(enviosBean.getIdInstitucion()),
							  idPersona,
							  String.valueOf(enviosBean.getIdTipoEnvios()));
	            	
					CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrBean);
					Hashtable htPersona = new Hashtable();
					htPersona.put(CenPersonaBean.C_IDPERSONA, idPersona);
					CenPersonaBean persona= (CenPersonaBean) ((Vector)personaAdm.selectByPK(htPersona)).get(0);
					destBean.setApellidos1(persona.getApellido1());
			        destBean.setApellidos2(persona.getApellido2());
			        destBean.setNombre(persona.getNombre());
			        
					
			        if (direcciones!=null && direcciones.size()>0) {
		            	Hashtable htDir = (Hashtable)direcciones.firstElement();
				        destBean.setDomicilio((String)htDir.get(CenDireccionesBean.C_DOMICILIO));
				        destBean.setIdPoblacion((String)htDir.get(CenDireccionesBean.C_IDPOBLACION));
				        destBean.setPoblacionExtranjera((String)htDir.get(CenDireccionesBean.C_POBLACIONEXTRANJERA));
				        destBean.setIdProvincia((String)htDir.get(CenDireccionesBean.C_IDPROVINCIA));
				        destBean.setIdPais((String)htDir.get(CenDireccionesBean.C_IDPAIS));
				        if (destBean.getIdPais().equals("")) destBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
				        destBean.setCodigoPostal((String)htDir.get(CenDireccionesBean.C_CODIGOPOSTAL));
				        destBean.setCorreoElectronico((String)htDir.get(CenDireccionesBean.C_CORREOELECTRONICO));
				        destBean.setFax1((String)htDir.get(CenDireccionesBean.C_FAX1));
				        destBean.setFax2((String)htDir.get(CenDireccionesBean.C_FAX2));
				        destBean.setMovil((String)htDir.get(CenDireccionesBean.C_MOVIL));
			        }

	    			
	    		}else if(tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)){
	    			ScsPersonaJGAdm personaJGAdm = new ScsPersonaJGAdm(this.usrBean);
	    			ScsPersonaJGBean personaJGBean =  personaJGAdm.getPersonaJG(new Long(idPersona),enviosBean.getIdInstitucion());

					destBean.setApellidos1(personaJGBean.getApellido1());
			        destBean.setApellidos2(personaJGBean.getApellido2());
			        destBean.setNombre(personaJGBean.getNombre());
			        destBean.setNifcif(personaJGBean.getNif());
			        
			        destBean.setDomicilio(personaJGBean.getDireccion());
			        destBean.setIdPoblacion(personaJGBean.getIdPoblacion());
			        destBean.setIdProvincia(personaJGBean.getIdProvincia());
			        destBean.setIdPais(personaJGBean.getIdPais());
			        if (destBean.getIdPais().equals("")) destBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
			        destBean.setCodigoPostal(personaJGBean.getCodigoPostal());
			        destBean.setCorreoElectronico(personaJGBean.getCorreoElectronico());
			        destBean.setFax1(personaJGBean.getFax());
			        Vector vTelefonos = personaJGBean.getTelefonos();
			        if(vTelefonos!=null && vTelefonos.size()>0){
				        for (int i = 0; i < vTelefonos.size(); i++) {
				        	ScsTelefonosPersonaJGBean telefono = (ScsTelefonosPersonaJGBean)vTelefonos.get(i);
				        	if(telefono.getpreferenteSms()!=null && telefono.getpreferenteSms().equals("1")){
				        		destBean.setMovil(telefono.getNumeroTelefono());
				        		break;
				        	}
						}
			        }
			        
	    			
	    			
	    			
	    		}else if(tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPROCURADOR)){
	    			ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(usrBean);
	    			Vector procuradorVector = procuradorAdm.busquedaProcurador(enviosBean.getIdInstitucion().toString(),idPersona);
	    			Hashtable procuradorHashtable = (Hashtable) procuradorVector.get(0);
	    			
					destBean.setApellidos1((String)procuradorHashtable.get(ScsProcuradorBean.C_APELLIDO1));
			        destBean.setApellidos2((String)procuradorHashtable.get(ScsProcuradorBean.C_APELLIDO2));
			        destBean.setNombre((String)procuradorHashtable.get(ScsProcuradorBean.C_NOMBRE));
			        destBean.setNifcif("");
			        
			        destBean.setDomicilio((String)procuradorHashtable.get(ScsProcuradorBean.C_DIRECCION));
			        destBean.setIdPoblacion((String)procuradorHashtable.get(ScsProcuradorBean.C_IDPOBLACION));
			        destBean.setIdProvincia((String)procuradorHashtable.get(ScsProcuradorBean.C_IDPROVINCIA));
			        destBean.setIdPais(ClsConstants.ID_PAIS_ESPANA);
			         
			        destBean.setCodigoPostal((String)procuradorHashtable.get(ScsProcuradorBean.C_CODIGOPOSTAL));
			        destBean.setCorreoElectronico((String)procuradorHashtable.get(ScsProcuradorBean.C_EMAIL));
			        destBean.setFax1((String)procuradorHashtable.get(ScsProcuradorBean.C_FAX1));
			        destBean.setFax2((String)procuradorHashtable.get(ScsProcuradorBean.C_FAX1));
			        destBean.setMovil((String)procuradorHashtable.get(ScsProcuradorBean.C_TELEFONO1));
			        
	    			
	    			
	    			
	    		}else if(tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSJUZGADO)){
	    			//TODO SCS_JUZGADOS
	    		}
	            
			    crearDestinatario=true;

            } else {
                
            	Hashtable htDest = new Hashtable();
                htDest.put(EnvDestinatariosBean.C_IDENVIO,enviosBean.getIdEnvio());
                htDest.put(EnvDestinatariosBean.C_IDINSTITUCION,enviosBean.getIdInstitucion());
                htDest.put(EnvDestinatariosBean.C_IDPERSONA,idPersona);
                destBean = (EnvDestinatariosBean)destAdm.selectByPK(htDest).firstElement();                
                crearDestinatario=false;
                
            }
	        addDestinatarioIndividualDocAdjuntos(destBean,documentos,crearDestinatario);
            // RGG 29-09-2005 se quita para usar en todos los lados la funcion de arriba
	        // addDestinatarioIndividual(destBean,documentos,crearDestinatario);
        } catch (SIGAException e1) {
            throw e1;
        } catch (Exception e1) {
            throw new SIGAException("messages.general.error",e1);
        }        
    }
    
    public void addDocumentosDestinatarioDireccionEspecifica(String idPersona, String idDireccion, Vector documentos) throws SIGAException{
        
        try{
        	//
            EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.usrBean);
            EnvDestinatariosBean destBean = null;
	        boolean crearDestinatario;
            if (!destAdm.existeDestinatario(String.valueOf(enviosBean.getIdEnvio()),
                    						String.valueOf(enviosBean.getIdInstitucion()),
                    						idPersona)){                
            
	            EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this.usrBean);
	            // Esto debería buscar la idrección indicada, y si no es del tipo de envío programado entonces busca una adecuada.
	            Vector direcciones = enviosAdm.getDireccionEspecifica(String.valueOf(enviosBean.getIdInstitucion()),
	                    									  			idPersona,
	                    									  			idDireccion,
	                    									  			String.valueOf(enviosBean.getIdTipoEnvios()));
	            
                destBean = new EnvDestinatariosBean();
		        destBean.setIdEnvio(enviosBean.getIdEnvio());
		        destBean.setIdInstitucion(enviosBean.getIdInstitucion());
		        destBean.setIdPersona(Long.valueOf(idPersona));
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrBean);			
				String nombre = personaAdm.obtenerNombre(String.valueOf(idPersona));
				String apellidos1 = personaAdm.obtenerApellidos1(String.valueOf(idPersona));
				String apellidos2 = personaAdm.obtenerApellidos2(String.valueOf(idPersona));
				destBean.setApellidos1(apellidos1);
		        destBean.setApellidos2(apellidos2);
		        destBean.setNombre(nombre);
				
		        if (direcciones!=null && direcciones.size()>0) {
	            	Hashtable htDir = (Hashtable)direcciones.firstElement();
			        destBean.setDomicilio((String)htDir.get(CenDireccionesBean.C_DOMICILIO));
			        destBean.setIdPoblacion((String)htDir.get(CenDireccionesBean.C_IDPOBLACION));
			        destBean.setPoblacionExtranjera((String)htDir.get(CenDireccionesBean.C_POBLACIONEXTRANJERA));
			        destBean.setIdProvincia((String)htDir.get(CenDireccionesBean.C_IDPROVINCIA));
			        destBean.setIdPais((String)htDir.get(CenDireccionesBean.C_IDPAIS));
			        if (destBean.getIdPais().equals("")) destBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
			        destBean.setCodigoPostal((String)htDir.get(CenDireccionesBean.C_CODIGOPOSTAL));
			        destBean.setCorreoElectronico((String)htDir.get(CenDireccionesBean.C_CORREOELECTRONICO));
			        destBean.setFax1((String)htDir.get(CenDireccionesBean.C_FAX1));
			        destBean.setFax2((String)htDir.get(CenDireccionesBean.C_FAX2));
			        destBean.setMovil((String)htDir.get(CenDireccionesBean.C_MOVIL));
		        }
			    crearDestinatario=true;

            } else {
                
            	Hashtable htDest = new Hashtable();
                htDest.put(EnvDestinatariosBean.C_IDENVIO,enviosBean.getIdEnvio());
                htDest.put(EnvDestinatariosBean.C_IDINSTITUCION,enviosBean.getIdInstitucion());
                htDest.put(EnvDestinatariosBean.C_IDPERSONA,idPersona);
                destBean = (EnvDestinatariosBean)destAdm.selectByPK(htDest).firstElement();                
                crearDestinatario=false;
                
            }
	        addDestinatarioIndividualDocAdjuntos(destBean,documentos,crearDestinatario);
            // RGG 29-09-2005 se quita para usar en todos los lados la funcion de arriba
	        // addDestinatarioIndividual(destBean,documentos,crearDestinatario);
        } catch (SIGAException e1) {
            throw e1;
        } catch (Exception e1) {
            throw new SIGAException("messages.general.error",e1);
        }        
    }
    
    /**
     * Método para el envío simultáneo a un grupo de personas, con 
     * un único documento adjunto para cada una. La correspondencia persona-documento
     * es por orden en los vectores. Si alguna no recibe documento, debe dejarse 
     * su espacio correspondiente
     * 
     * @param vPersonas Vector de idPersona (String)
     * @param vDocumentos Vector de objetos Documento
     * @throws SIGAException
     */
    public void addDestinatarios(Vector vPersonas,String tipoDestinatario, Vector vDocumentos) throws SIGAException{
        
        if (vPersonas!=null && vPersonas.size()>0){
            for (int i=0;i<vPersonas.size();i++){
                String idPersona = (String)vPersonas.elementAt(i);
                Documento doc = (Documento)vDocumentos.elementAt(i);
                Vector vDocumento = new Vector(1);
                vDocumento.add(doc);
                addDocumentosDestinatario(idPersona,tipoDestinatario,vDocumento);
            }
        }
    }
    
    /**
     * Método que genera un envío para un único destinatario, con un grupo de 
     * documentos adjuntos, y la institución como remitente.
     * Si el idPersona es null, sólo se inserta el envio y el remitente.
     * 
     * @param idPersona Persona destinataria del envío
     * @param documentos Vector de objetos Documento para el destinatario
     * @throws SIGAException
     */
    public void generarEnvio(String idPersona,String tipoDestinatario, Vector documentos) throws SIGAException,ClsExceptions
	{
        EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrBean);
        envAdm.insert(enviosBean);

        // Copiamos los datos la plantilla, incluidos los remitentes
        envAdm.copiarCamposPlantilla(enviosBean.getIdInstitucion(), 
				enviosBean.getIdEnvio(), 
				enviosBean.getIdTipoEnvios(),
				enviosBean.getIdPlantillaEnvios());

        if (idPersona!=null) 
        	addDocumentosDestinatario(idPersona,tipoDestinatario,documentos);        
    }
    public void generarEnvio(String idPersona,String tipoDestinatario, Object bean) throws SIGAException,ClsExceptions
	{
        EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrBean);
        envAdm.insert(enviosBean);

        // Copiamos los datos la plantilla, incluidos los remitentes
        envAdm.copiarCamposPlantilla(enviosBean.getIdInstitucion(), 
				enviosBean.getIdEnvio(), 
				enviosBean.getIdTipoEnvios(),
				enviosBean.getIdPlantillaEnvios(),bean);

        if (idPersona!=null) 
        	addDocumentosDestinatario(idPersona,tipoDestinatario,null);        
    }
    
    
    

    public void generarEnvioDireccionEspecifica(String idPersona, String idDireccion, Vector documentos) throws SIGAException,ClsExceptions
	{
        EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrBean);
        envAdm.insert(enviosBean);

        // Copiamos los datos la plantilla, incluidos los remitentes
        envAdm.copiarCamposPlantilla(enviosBean.getIdInstitucion(), 
				enviosBean.getIdEnvio(), 
				enviosBean.getIdTipoEnvios(),
				enviosBean.getIdPlantillaEnvios());

        if (idPersona!=null) 
        	addDocumentosDestinatarioDireccionEspecifica(idPersona, idDireccion, documentos);        
    }
    /**
     * Se ha creado este metodo para el proceso de generacion de envios.
     * La funcionalidad que cumple es crear un unico envio 
     * con todos los documentos para todos los destinatarios  
     * 
     * @param enviosBean
     * @param htPersonas(idPersona,VDocumentos)
     * @throws SIGAException
     * @throws ClsExceptions
     */
    public void generarEnvioOrdinario(EnvEnviosBean enviosBean,Hashtable htPersonas,Hashtable htPersonasJG) throws SIGAException,ClsExceptions
	{
        EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrBean);
        envAdm.insert(enviosBean);

        // Copiamos los datos la plantilla, incluidos los remitentes
        envAdm.copiarCamposPlantilla(enviosBean.getIdInstitucion(), 
				enviosBean.getIdEnvio(), 
				enviosBean.getIdTipoEnvios(),
				enviosBean.getIdPlantillaEnvios());
        Iterator itePersona = htPersonas.keySet().iterator();
        while (itePersona.hasNext()) {
			String idPersona = (String) itePersona.next();
			Vector documentos = (Vector)htPersonas.get(idPersona);
			addDocumentosDestinatario(idPersona,EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,documentos);
			
		}
        Iterator itePersonaJG = htPersonasJG.keySet().iterator();
        while (itePersonaJG.hasNext()) {
			String idPersona = (String) itePersonaJG.next();
			Vector documentos = (Vector)htPersonasJG.get(idPersona);
			addDocumentosDestinatario(idPersona,EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG,documentos);
			
		}
       
                
    }
    
    
    /**
     * Método que genera un envío para al un moroso 
     * documentos adjuntos, y la institución como remitente.
     * Si el idPersona es null, sólo se inserta el envio y el remitente.
     * 
     * @param idPersona Persona destinataria del envío
     * @param documentos Vector de objetos Documento para el destinatario
     * @throws SIGAException
     */
    /*public void generarEnvioMoroso(String idPersona, Vector documentos , ArrayList alFacturas,
    		String idInstitucion, String descripcion) throws SIGAException,ClsExceptions
	{
    	generarEnvio(idPersona,documentos);
    	generarComunicacionMoroso(idPersona, documentos, alFacturas, idInstitucion, descripcion);
    	
                
    }*/
    public void generarComunicacionMoroso(String idPersona, Vector documentos , ArrayList alFacturas,
    		String idInstitucion, String descripcion) throws SIGAException,ClsExceptions
	{
    	
    	EnvComunicacionMorososAdm admEnvComorosos = new EnvComunicacionMorososAdm(this.usrBean);
    	EnvComunicacionMorososBean  beanEnvComorosos = null;
    	for (int i = 0; i < alFacturas.size(); i++) {
    		String idFactura = (String)alFacturas.get(i);
    		beanEnvComorosos = new EnvComunicacionMorososBean();
    		beanEnvComorosos.setIdFactura(idFactura);
        	beanEnvComorosos.setIdPersona(new Integer(idPersona));
        	beanEnvComorosos.setIdInstitucion(new Integer(idInstitucion));
        	beanEnvComorosos.setDescripcion(descripcion);
        	beanEnvComorosos.setFechaEnvio("SYSDATE");
        	Integer idEnvio = admEnvComorosos.getNewIdEnvio(beanEnvComorosos);
        	beanEnvComorosos.setIdEnvio(idEnvio);
        	if (documentos!=null && documentos.size()>0){
    	        for (int j=0;j<documentos.size();j++){
    	        	//Recuperamos el documento:
    	            Documento doc = (Documento)documentos.get(j);
    	            
    	            Object beanAsociado = doc.getBeanAsociado();
    	            if(beanAsociado instanceof EnvDocumentosBean){
    	            	EnvDocumentosBean envDocumentosBean = (EnvDocumentosBean)beanAsociado;
    	            	beanEnvComorosos.setPathDocumento(envDocumentosBean.getPathDocumento());
        	            //beanEnvComorosos.setDescripcion(envDocumentosBean.getDescripcion());
	    	            beanEnvComorosos.setIdDocumento(envDocumentosBean.getIdDocumento());
	    	            beanEnvComorosos.setIdEnvioDoc(envDocumentosBean.getIdEnvio());
    	            }
    	        }
        	}
        	
        	
        	
        	admEnvComorosos.insert(beanEnvComorosos);
			
		}
    	
                
    }
    public static void generarComunicacionExpediente(String idInstitucion, 
			Integer idInstitucionTipoExp, Integer idTipoExp, Integer numeroExpediente, 
			Integer anioExpediente	,String idPersona, UsrBean usrBean) throws SIGAException,ClsExceptions
    		
    		
	{
    	ExpAnotacionAdm admExpAnotacion = new ExpAnotacionAdm(usrBean);
    	admExpAnotacion.insertarAnotacionEnvioComunicacion(idInstitucion, 
    			idInstitucionTipoExp, idTipoExp, numeroExpediente, 
    			anioExpediente	,idPersona,usrBean);
    	
	}
    
    /**
     * Método que genera un envío de certificado a la dirección del destinatario
     * indicada en la solicitud, certificado adjunto, y la institución como remitente.
     * 
     * @param idPersona Persona destinataria del envío
     * @param documentos Vector de objetos Documento para el destinatario
     * @param idSolicitud Solicitud del certificado
     * @return boolean: true si tiene una direccion (se obtiene en addDestinatarioCertificado)
     * @throws SIGAException
     */
    public boolean generarEnvioCertificado(String idPersona, Vector documentos, String idSolicitud, EnvEnviosBean envBean, boolean existePersonaOrigen)	throws ClsExceptions 
	{
		boolean ok = true;
		
		try {
	        EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrBean);
	        //Insertamos el envio:
	        envAdm.insert(enviosBean);
	        
	        // Copiamos los datos la plantilla, incluidos los remitentes
	        envAdm.copiarCamposPlantilla(enviosBean.getIdInstitucion(), 
					enviosBean.getIdEnvio(), 
					enviosBean.getIdTipoEnvios(),
					enviosBean.getIdPlantillaEnvios());
	        
	        //Anhadimos el destinatario y sus documentos:
	        ok = this.addDestinatarioCertificado(idPersona,documentos,idSolicitud, envBean, existePersonaOrigen);
	        
	        /* RGG 20-09-2005 Ahora no cambiamos el estado a falta de una nueva definicion. 
	         * El certificado queda enviado pero no cambia su estado de finalizado. 
	        //Cambiamos el estado de la solicitud de certificado a 'Envio Programado'
	        CerSolicitudCertificadosAdm solAdm = new CerSolicitudCertificadosAdm(idUsuario);
	        Hashtable htPk = new Hashtable();
	        htPk.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,enviosBean.getIdInstitucion());
	        htPk.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
	        CerSolicitudCertificadosBean solBean = (CerSolicitudCertificadosBean)solAdm.selectByPKForUpdate(htPk).firstElement();
	        solBean.setIdEstadoSolicitudCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ENVIOP));
	        solAdm.update(solBean);
	        */
		} 
		catch (ClsExceptions e){
			throw e;
		}
		catch (SIGAException e){
			throw new ClsExceptions(e, e.getLiteral());
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Error en Envio.generarEnvioCertificado");
		}
		return ok;
	}
	
    public boolean generarEnvioFactura(String idPersona, Vector documentos, String idFactura) throws ClsExceptions 
	{
		boolean ok = true;
		
		try {
	        EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrBean);
	        //Insertamos el envio:
	        envAdm.insert(enviosBean);
	        
	        // Copiamos los datos la plantilla, incluidos los remitentes
	        envAdm.copiarCamposPlantilla(enviosBean.getIdInstitucion(), 
					enviosBean.getIdEnvio(), 
					enviosBean.getIdTipoEnvios(),
					enviosBean.getIdPlantillaEnvios());
	        
	        //Anhadimos el destinatario y sus documentos:
	        ok = this.addDestinatarioFactura(idPersona,documentos,idFactura);
	        
		} catch (Exception e){
			throw new ClsExceptions(e,"Error en Envio.generarEnvioCertificado");
		}
		return ok;
	}
	
	/**
	 * Metodo que adjunta documentos (normalmente un certificado) a un destinatario
	 * Siempre crea el destinatario, no existía previamente
	 */
	public boolean addDestinatarioCertificado (String idPersona,
											   Vector documentos,
											   String idSolicitud,
											   EnvEnviosBean envBean,
											   boolean existePersonaOrigen)
			throws SIGAException
	{
		EnvDestinatariosBean destBean;
		CerSolicitudCertificadosAdm solCerAdm;
		CerSolicitudCertificadosBean solCerBean;
		PysProductosInstitucionAdm admProd;
		PysProductosInstitucionBean beanProd;
		CenDireccionesAdm dirAdm;
		CenDireccionesBean dirBean;
		CenPersonaAdm personaAdm;
		
		boolean ok;
		
		try
		{
			//obteniendo bean de solicitud
			solCerAdm = new CerSolicitudCertificadosAdm (this.usrBean);
			Hashtable htPk = new Hashtable();
			htPk.put (CerSolicitudCertificadosBean.C_IDINSTITUCION, enviosBean.getIdInstitucion());
			htPk.put (CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
			solCerBean = (CerSolicitudCertificadosBean) solCerAdm.selectByPK (htPk).firstElement();
			
			//obteniendo bean de producto para saber el tipo
			admProd = new PysProductosInstitucionAdm (this.usrBean);
			Hashtable htPk2 = new Hashtable();
			htPk2.put (PysProductosInstitucionBean.C_IDINSTITUCION, solCerBean.getIdInstitucion());
			htPk2.put (PysProductosInstitucionBean.C_IDTIPOPRODUCTO, solCerBean.getPpn_IdTipoProducto());
			htPk2.put (PysProductosInstitucionBean.C_IDPRODUCTO, solCerBean.getPpn_IdProducto());
			htPk2.put (PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, solCerBean.getPpn_IdProductoInstitucion());
			beanProd = (PysProductosInstitucionBean) admProd.selectByPK (htPk2).firstElement();
			
			//obteniendo nombre y apellidos de la persona para escribir en envio
			personaAdm = new CenPersonaAdm (this.usrBean);
			String nombre = personaAdm.obtenerNombre (String.valueOf (idPersona));
			String apellidos1 = personaAdm.obtenerApellidos1 (String.valueOf (idPersona));
			String apellidos2 = personaAdm.obtenerApellidos2 (String.valueOf (idPersona));
			destBean = new EnvDestinatariosBean();
			destBean.setIdEnvio (enviosBean.getIdEnvio());
			destBean.setIdInstitucion (enviosBean.getIdInstitucion());
			destBean.setIdPersona (Long.valueOf (idPersona));
			destBean.setApellidos1 (apellidos1);
			destBean.setApellidos2 (apellidos2);
			destBean.setNombre (nombre);
			
			//obteniendo la direccion
			dirAdm = new CenDireccionesAdm(this.usrBean);
			if (beanProd.getTipoCertificado().equals ("C") ||
				beanProd.getTipoCertificado().equals ("M") ||
				beanProd.getTipoCertificado().equals ("D"))
			{
				dirBean = dirAdm.obtenerDireccionPorTipo (idPersona,
						solCerBean.getIdInstitucion().toString(),
						envBean.getIdTipoEnvios().toString());
				
				if (dirBean != null) {
					destBean.setDomicilio (dirBean.getDomicilio());
					destBean.setIdPoblacion (dirBean.getIdPoblacion());
					destBean.setIdProvincia (dirBean.getIdProvincia());
					destBean.setIdPais (dirBean.getIdPais());
					if (destBean.getIdPais().equals(""))
						destBean.setIdPais (ClsConstants.ID_PAIS_ESPANA);
					destBean.setPoblacionExtranjera (dirBean.getPoblacionExtranjera());
					destBean.setCodigoPostal (dirBean.getCodigoPostal());
					destBean.setCorreoElectronico (dirBean.getCorreoElectronico());
					destBean.setFax1 (dirBean.getFax1());
					destBean.setFax2 (dirBean.getFax2());
					destBean.setMovil(dirBean.getMovil());
					
					ok = true;
				}
				else
					ok = false;
			}
			else
				ok = false;
			
			//anyadiendo el destinatario y los documentos adjuntos
			this.addDestinatarioIndividualDocAdjuntos (destBean, documentos,
					true /*crearDestinatario*/, true);
			
			return ok;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (ClsExceptions e) {
			throw new SIGAException (e);
		}
		catch (Exception e) {
			throw new SIGAException ("messages.general.error", e);
		}
	} //addDestinatarioCertificado ()
    
    public boolean addDestinatarioFactura(String idPersona, Vector documentos, String idFactura) throws SIGAException {
	    boolean ok = true;
	    
	    try {
	    	
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrBean);			
			String nombre = personaAdm.obtenerNombre(String.valueOf(idPersona));
			String apellidos1 = personaAdm.obtenerApellidos1(String.valueOf(idPersona));
			String apellidos2 = personaAdm.obtenerApellidos2(String.valueOf(idPersona));
	        EnvDestinatariosBean destBean = null;
	        boolean crearDestinatario = true; // El destinatario nunca existe previamente, siempre lo crearemos
	        
		    destBean = new EnvDestinatariosBean();
	        destBean.setIdEnvio(enviosBean.getIdEnvio());
	        destBean.setIdInstitucion(enviosBean.getIdInstitucion());
	        destBean.setIdPersona(Long.valueOf(idPersona));
	        destBean.setApellidos1(apellidos1);
	        destBean.setApellidos2(apellidos2);
	        destBean.setNombre(nombre);
		    //Obtenemos el bean de direccion
		    CenDireccionesAdm dirAdm = new CenDireccionesAdm(this.usrBean);
		    
		    // INC-6205-SIGA jbd 8/7/2009
		    String idInstitucion = enviosBean.getIdInstitucion().toString();
		    String idTipo = enviosBean.getIdTipoEnvios().toString();
		    // String where = " WHERE IDINSTITUCION="+enviosBean.getIdInstitucion()+ 
			//			   " AND IDPERSONA="+idPersona;
		    String where =" WHERE " + 
			CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + idPersona +
			" AND " +
			CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + idInstitucion +
			" AND " + 
			CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDDIRECCION + "=" + 
			    " F_SIGA_GETDIRECCION("+ idInstitucion +","+ idPersona +","+ idTipo +")";
		    
		    Vector v = dirAdm.select(where);
		    if (v!=null && v.size()>0)  {
		    	
			    CenDireccionesBean dirBean = (CenDireccionesBean) v.firstElement();
			 		    
			    destBean.setDomicilio(dirBean.getDomicilio());
		        destBean.setIdPoblacion(dirBean.getIdPoblacion());
		        destBean.setIdProvincia(dirBean.getIdProvincia());
		        destBean.setPoblacionExtranjera(dirBean.getPoblacionExtranjera());
		        destBean.setIdPais(dirBean.getIdPais());
		        if (destBean.getIdPais().equals("")) destBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
		        destBean.setCodigoPostal(dirBean.getCodigoPostal());
		        destBean.setCorreoElectronico(dirBean.getCorreoElectronico());
		        destBean.setFax1(dirBean.getFax1());
		        destBean.setFax2(dirBean.getFax2());
		        destBean.setMovil(dirBean.getMovil());

	    	} else { 
	        	ok = false;
	    	}
	        
	        //Anhadimos el destinatario y los documentos adjuntos:
	        this.addDestinatarioIndividualDocAdjuntos(destBean,documentos,crearDestinatario, true);
	    } 
	    catch (SIGAException e) {
	        throw e;
	    }     
	    catch (Exception e) {
	        throw new SIGAException("messages.general.error",e);
	    }     
	    return ok;
    }

    /**
     *  Anhade el destinatario y documentos adjuntos al destinatario
     *  DE MOMENTO los anhade adjuntos al envio, tiene el mismo comportamiento que addDestinatarioIndividualDocAdjuntos
     *  y habra que cambiarlo (El cambio se basa en crear el documento o duplicarlo con un nombre con formato adecuado 
     * , que incluye el idpersona)
     * @param destBean Destinatario
     * @param vDocumentos Vector de objetos Documento
     */
    public void addDestinatarioIndividual(EnvDestinatariosBean destBean,
            							  Vector vDocumentos,
            							  boolean crearDestinatario)
    	throws SIGAException{
        
        boolean direccion = true;
        if (crearDestinatario){
	        //***** Insertamos Destinatario *******
	        
        	//
	        EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.usrBean);
	        int tipo = enviosBean.getIdTipoEnvios().intValue();
	        
	        if (comprobarDireccion(tipo,destBean)){
	            try {
	                destAdm.insert(destBean);
	            } catch (ClsExceptions e) {
	                throw new SIGAException("messages.envios.error",e);
	            }
	        }else{
	        	// RGG 24-06-2005 cambio para el tema destinatario que no conocemos
	        	direccion = false;
	            //throw new SIGAException("messages.envios.error.direccionIncompleta");
	        }
        }
        //******** Insertamos Documentos *********//
    	// RGG 24-06-2005 cambio para el tema destinatario que no conocemos
        if (direccion) {
	        if (vDocumentos!=null && vDocumentos.size()>0){
	    	    EnvDocumentosDestinatariosAdm docAdm = new EnvDocumentosDestinatariosAdm(this.usrBean);
		        String idInstitucion = String.valueOf(destBean.getIdInstitucion());
		        String idEnvio = String.valueOf(destBean.getIdEnvio());
		        String idPersona = String.valueOf(destBean.getIdPersona());
		        for (int i=0;i<vDocumentos.size();i++){
		            Documento doc = (Documento)vDocumentos.get(i);
		            try{
		            	//Insertamos el documento particular del destinatario:
		                Integer idDoc = docAdm.getNewIdDocumentoDestinatario(idInstitucion,idEnvio,idPersona);
		    	    	guardarDoc(idInstitucion, idEnvio, idPersona, String.valueOf(idDoc), doc);	        
		    	        
		    	    	/* RGG 29-09-2005 Lo quitamos porque si se guarda como doc adjunto al dest. , el nombre 
		    	    	 tiene que tener otro formato
		    	    	EnvDocumentosDestinatariosBean docBean = new EnvDocumentosDestinatariosBean();
		    	        docBean.setIdInstitucion(destBean.getIdInstitucion());
		    	        docBean.setIdEnvio(destBean.getIdEnvio());
		    	        docBean.setIdPersona(destBean.getIdPersona());
		    	        docBean.setIdDocumento(idDoc);
		    	        docBean.setPathDocumento(doc.getDocumento().getName());
		    	        docBean.setDescripcion(doc.getDescripcion()); 
	    	        	docAdm.insert(docBean);
	    	        	*/
		    	    	
	    	        	//Insertamos los documentos anexos: 
	    		        EnvDocumentosBean documentoBean = new EnvDocumentosBean();
	    		        documentoBean.setIdInstitucion(destBean.getIdInstitucion());
	    		        documentoBean.setIdEnvio(destBean.getIdEnvio());
	    		        documentoBean.setIdDocumento(idDoc);
	    		        documentoBean.setPathDocumento(doc.getDocumento().getName());
	    		        documentoBean.setDescripcion(doc.getDescripcion()); 
	    		        EnvDocumentosAdm documentoAdm = new EnvDocumentosAdm(this.usrBean);
	    		        documentoAdm.insert(documentoBean);
		            }
		            catch (SIGAException e){
		                e.setSubLiteral(doc.getDocumento().getPath());
		                throw e;
		            }
		            catch (Exception e){
		                throw new SIGAException("messages.general.error",e);
		            }
		        }
	        }
        } 
    }
    
    /**
     * @param listaBean Lista de correos
     */
    public void addDestinatarioLista(EnvListaCorreosBean listaBean)
    	throws SIGAException,ClsExceptions{
        
        EnvListaCorreosEnviosAdm listasAdm = new EnvListaCorreosEnviosAdm(this.usrBean);
        EnvListaCorreosEnviosBean listaEnviosBean = new EnvListaCorreosEnviosBean();
        
        try {            
		    String idInstitucion = String.valueOf(listaBean.getIdInstitucion());
		    String idListaCorreo = String.valueOf(listaBean.getIdListaCorreo());
		    String idEnvio = String.valueOf(enviosBean.getIdEnvio());		    
		    listaEnviosBean.setIdInstitucion(listaBean.getIdInstitucion());
	        listaEnviosBean.setIdEnvio(enviosBean.getIdEnvio());
	        listaEnviosBean.setIdListaCorreo(listaBean.getIdListaCorreo());
	        
		    //Insertamos bean si no existe en la tabla
		    if (!listasAdm.existeLista(idEnvio,idInstitucion,idListaCorreo)){
		        listasAdm.insert(listaEnviosBean);
		    } else {
		        throw new SIGAException("messages.envios.error.existeelemento");
		    }
        } catch (SIGAException e1) {
            throw e1;
     	} catch (Exception e) {
		   throw new ClsExceptions ("Error general anhadiendo destinatarios a la lista del envío");
//		   throw new SIGAException("messages.general.error",e);
		}
    }
    
    /**
     * @param remBean Remitente
     */
    /*
    public void addRemitentes(EnvRemitentesBean remBean)
		throws SIGAException, ClsExceptions{
    
	    EnvRemitentesAdm remitentesAdm = new EnvRemitentesAdm(idUsuario);
	    try {            
		    String idInstitucion = String.valueOf(remBean.getIdInstitucion());
		    String idPersona = String.valueOf(remBean.getIdPersona());
		    String idEnvio = String.valueOf(enviosBean.getIdEnvio());		    
		    
		    //Insertamos bean si no existe en la tabla
		    if (!remitentesAdm.existeRemitente(idEnvio,idInstitucion,idPersona)){
		        remitentesAdm.insert(remBean);
		    } else {
		        throw new SIGAException("messages.envios.error.existeelemento");
		    } 
		  
        } catch (SIGAException e1) {
            throw e1;
     	} catch (Exception e) {
			throw new ClsExceptions (e, "Error general anhadiendo remitantes a la lista del envío");
//		    throw new SIGAException("messages.general.error",e);
		}
    }
    */

    private boolean comprobarDireccion(int tipo, EnvDestinatariosBean destBean){
        boolean resultado = false;
        switch (tipo) {
        case EnvEnviosAdm.TIPO_CORREO_ELECTRONICO:
            String correoElectronico = destBean.getCorreoElectronico();
            if (correoElectronico!=null && !correoElectronico.equals("")){
                resultado = true;
     		}
            break;
        case EnvEnviosAdm.TIPO_CORREO_ORDINARIO:
            String direccion = destBean.getDomicilio();
        	String idPoblacion = destBean.getIdPoblacion();
        	String poblacionExtranjera = destBean.getPoblacionExtranjera();
        	String cp = destBean.getCodigoPostal();
            if ((direccion!=null && !direccion.equals("")) && 
				((idPoblacion!=null && !idPoblacion.equals("")) || (poblacionExtranjera!=null && !poblacionExtranjera.equals("")) || (cp!=null && !cp.equals(""))     		   	   )
     		   )
            {
                resultado = true;
     		}
            break;
        case EnvEnviosAdm.TIPO_FAX:
            String fax1 = destBean.getFax1();
        	String fax2 = destBean.getFax2();
        	if ((fax1!=null && !fax1.equals(""))||
     		        (fax2!=null && !fax2.equals(""))){
        	    resultado = true;
     		}
            break;
        default:
            resultado = false;
            break;
        }
        return resultado;
    }
    
    private void guardarDoc(String idInstitucion, String idEnvio, 
            				String idPersona, String idDoc, Documento doc) 
    	throws SIGAException, ClsExceptions{
        
        String sNombreFichero="";
		File fDocumento = doc.getDocumento();
		
		//Recuperamos los datos del envio
        Hashtable htPk = new Hashtable();
        htPk.put(EnvDocumentosBean.C_IDINSTITUCION,idInstitucion);
        htPk.put(EnvDocumentosBean.C_IDENVIO,idEnvio);        
		
		EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.usrBean);
		//Vector vEnvio =  envioAdm.selectByPK(htPk);        
		//EnvEnviosBean envioBean =(EnvEnviosBean)vEnvio.firstElement();
        
	    if(fDocumento.exists()) 
	    {
	    	FileInputStream FIStream = null;
	    	OutputStream bos = null;
	    	String sNombreDocumento = idInstitucion + "_" + idEnvio + "_" + idDoc;
	    	
	    	try 
	    	{
	    	    String sDirectorio = envioAdm.getPathEnvio(idInstitucion,idEnvio);		    	
		    	File fDirectorio = new File(sDirectorio);
		    	fDirectorio.mkdirs(); 
		    	
	    		FIStream = new FileInputStream(fDocumento);
	    		sNombreFichero = sDirectorio + File.separator 
	    						+ sNombreDocumento;
	    		bos = new FileOutputStream(sNombreFichero);
	    		int bytesRead = 0;
	    		byte[] buffer = new byte[8192];

	    		while ((bytesRead = FIStream.read(buffer, 0, 8192)) != -1) 
	    		{
	    			bos.write(buffer, 0, bytesRead);
	    		}
	    		
	    		bos.close();
	    	} 
	    	
	    	catch (Exception e) 
	    	{
				throw new ClsExceptions (e, "Error copiando el documento");
//	    	    throw new SIGAException("messages.envios.error.copiarDocumento",e);
	    	} finally {
	    	    try {
	    	        bos.close();
	    	        FIStream.close();
	    	    } catch (Exception eee) {}
	    	}
	    } else {
	        throw new SIGAException("messages.envios.error.noExisteDocumento");
	    }    
	    
    }
//
//	public boolean generarDocumentoEnvioPDF(Integer idInstitucion, Integer idEnvio) throws SIGAException, ClsExceptions
//	{
//        try
//        {           
//            EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.usrBean);
//
//            Hashtable htEnvio = new Hashtable();
//
//            htEnvio.put(EnvEnviosBean.C_IDINSTITUCION, idInstitucion);
//            htEnvio.put(EnvEnviosBean.C_IDENVIO, idEnvio);
//
//            Vector vEnvio = admEnvio.selectByPK(htEnvio);
//
//            EnvEnviosBean beanEnvio = (EnvEnviosBean)vEnvio.elementAt(0);
//
//            EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm(this.usrBean);
//
//            // Obtenemos el archivo con la plantilla
//            
//            File fPlantilla = admPlantilla.obtenerPlantilla(""+beanEnvio.getIdInstitucion(), 
//                    										""+beanEnvio.getIdTipoEnvios(), 
//                    										""+beanEnvio.getIdPlantillaEnvios(), 
//                    										""+beanEnvio.getIdPlantilla());
//
//            if (fPlantilla==null)
//            {
//                throw new SIGAException("messages.envios.error.noPlantilla");
//            } else {
//            	if (!fPlantilla.exists()) {
//                    throw new SIGAException("messages.envios.error.noPlantilla");
//            	} else
//			    if (!fPlantilla.canRead()){
//					throw new ClsExceptions ("Error de lectura del fichero: "+fPlantilla.getAbsolutePath());
//					//throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
//			    }            		 
//            }
//            
//            Plantilla plantilla = new Plantilla(fPlantilla,this.usrBean);
//
//            Vector vDestinatarios = admEnvio.getDestinatarios(""+idInstitucion, ""+idEnvio, 
//                    											""+beanEnvio.getIdTipoEnvios());
//            EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.usrBean);            
//            Vector vCampos = admCampos.obtenerCamposEnvios(""+idInstitucion, ""+idEnvio, "");
//            
//            if (vDestinatarios!=null) {
//	            for (int i=0; i<vDestinatarios.size(); i++)
//	            {
//	                EnvDestinatariosBean beanDestinatario = (EnvDestinatariosBean)vDestinatarios.elementAt(i);
//		            Hashtable htDatos = admEnvio.getDatosEnvio(beanDestinatario, consulta);
//		            
//					htDatos = admEnvio.darFormatoCampos(idInstitucion, idEnvio, this.usrBean.getLanguage(), htDatos,vCampos);
//	
//		            String path = admEnvio.getPathEnvio(""+idInstitucion, ""+idEnvio) + File.separator + "documentosdest";
//		            
//		            // Generamos el archivo temporal que se obtendrá de sustituir las etiquetas
//		            // de la plantilla
//		            String nombreFin = path + File.separator + idInstitucion + "_" + idEnvio + "_" + beanDestinatario.getIdPersona();
//		            File fIn = new File(nombreFin);
//		            
//		            // fIN contendrá el archivo obtenido de sustituir las etiquetas a la plantilla.
//		            plantilla.sustituirEtiquetas(htDatos, fIn);
//		            
//		            String nombreFout = path + File.separator + beanDestinatario.getIdPersona() + ".pdf";
//		            File fOut = new File(nombreFout);
//	
//		            // El path base para los recursos será al path donde se almacena la plantilla
//		            
//		            //plantilla.convertFO2PDF(fIn, fOut, fPlantilla.getPath());
//		            try {
//		            	plantilla.convertFO2PDF(fIn, fOut, fPlantilla.getParent());
//		            } catch (Exception e) {
//		        		ClsLogging.writeFileLogError("Error convirtiendo PDF.  Mensaje:" + e.getLocalizedMessage(),e,3);
//		            	throw new ClsExceptions(e,"Error al convertir a PDF");
//		            }
//		            // Borramos el temporal
//		            
//		            fIn.delete();	            
//		            
//	            }
//            }
//            return true;
//
//        } catch (SIGAException e1) {
//    		ClsLogging.writeFileLogError("Error generando PDF.  Mensaje:" + e1.getMsg(""),e1,3);
//            throw e1;
//            
//        }catch(Exception e){
//    		ClsLogging.writeFileLogError("Error generando PDF.  Mensaje:" + e.getLocalizedMessage(),e,3);
//			throw new ClsExceptions (e, "Error general generando PDF ");
////            throw new SIGAException("messages.envios.error.generarPDF",e);
//        }
//	}

	
	/**
	 * Procesa un envío individual y genera su log
	 * @throws SIGAException
	 */
	public void procesarEnvio(UserTransaction tx) throws SIGAException, ClsExceptions {

		// OBTENCION DE DESTINATARIOS 
        /////////////////////////////////////
		EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrBean);
		Vector vDestinatarios = null;
		Hashtable htErrores = new Hashtable();
		try {
			vDestinatarios = envAdm.getDestinatarios(this.enviosBean.getIdInstitucion().toString(), this.enviosBean.getIdEnvio().toString(), this.enviosBean.getIdTipoEnvios().toString());			
		} catch (Exception e) {
			SIGAException sigaExc = new SIGAException("messages.envios.errorDestinatarios");
			envAdm.generarLogEnvioExceptionHT(this.enviosBean,sigaExc);
			enviosBean.setIdEstado(Integer.valueOf(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES));
			envAdm.updateDirect(enviosBean);
			throw sigaExc;
		}
		
		
		this.procesarEnvioMasivo(vDestinatarios, htErrores, true, tx);
	}
	
	public void procesarEnvioMasivo(Vector vDestinatarios,Hashtable htErrores,boolean generarLog, UserTransaction tx) throws SIGAException,ClsExceptions{

		EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrBean);
		Integer idTipoEnvio = enviosBean.getIdTipoEnvios();
		
		String estadoEnvio = "";

		try {
			int iTipoEnvio = idTipoEnvio.intValue();
			// Proceso de Correo Electrónico
			switch (iTipoEnvio) {
			
			case EnvEnviosAdm.TIPO_CORREO_ELECTRONICO:
				estadoEnvio = envAdm.enviarCorreoElectronico(enviosBean, vDestinatarios, htErrores, generarLog);
				break;
			case EnvEnviosAdm.TIPO_SMS:
				estadoEnvio = envAdm.enviarSMS(enviosBean, vDestinatarios, htErrores, generarLog);
				break;
			case EnvEnviosAdm.TIPO_BUROSMS:
				estadoEnvio = envAdm.enviarBuroSMS(enviosBean, vDestinatarios, htErrores, generarLog);
				break;
			case EnvEnviosAdm.TIPO_CORREO_ORDINARIO:
				estadoEnvio = envAdm.enviarCorreoOrdinario(enviosBean, vDestinatarios, htErrores, generarLog);
				break;
			case EnvEnviosAdm.TIPO_FAX:
				estadoEnvio = envAdm.enviarFax(enviosBean, vDestinatarios, htErrores, generarLog);

				break;
			default:
				estadoEnvio = EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES;
				break;
				
			
			}
			
			enviosBean.setIdEstado(Integer.valueOf(estadoEnvio));
			envAdm.updateDirect(enviosBean);


		}  catch (SIGAException e) {	                
			enviosBean.setIdEstado(Integer.valueOf(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES));
			envAdm.updateDirect(enviosBean);
			throw e;
			
			
		}catch (ClsExceptions e) {	                
			enviosBean.setIdEstado(Integer.valueOf(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES));
			envAdm.updateDirect(enviosBean);
			throw e;
			
			
		}finally{
			if(enviosBean.getIdEstado().compareTo(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO)==0 ||
					estadoEnvio.equalsIgnoreCase(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL)||
					enviosBean.getIdEstado().equals("")){
//				enviosBean.getOriginalHash().get(EnvEnviosBean.C_IDESTADO);
				enviosBean.setIdEstado(Integer.valueOf(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES));
				envAdm.updateDirect(enviosBean);
			}

		}			
	
	}
	
	/**
	 * Método estático para el procesado automático de envíos pendientes
	 * 
	 * @param idInstitucion
	 * @param idUsuario
	 * @throws SIGAException
	 */
	public static void procesarEnvios(String idInstitucion, UsrBean _usrBean)
		throws SIGAException,ClsExceptions{
	    try {
		    Vector enviosBeans = new Vector();
		    GenParametrosAdm parAdm = new GenParametrosAdm(_usrBean);
		    String bloqueado = parAdm.getValor(idInstitucion,"ENV","BLOQUEO_ENVIOS","0");
		    if (!bloqueado.equals("1")) {
		        
			    EnvEnviosAdm envAdm = new EnvEnviosAdm(_usrBean);
			    enviosBeans = envAdm.getEnviosPendientes(idInstitucion);
			    UsrBean usr = new UsrBean();
			    UserTransaction tx = (UserTransaction)usr.getTransaction();
				
	    		ClsLogging.writeFileLog("### Numero de envios: "+enviosBeans.size(),7);
			    
			    for (int i=0;i<enviosBeans.size();i++){
		    		EnvEnviosBean envBean = (EnvEnviosBean)enviosBeans.elementAt(i);
		    		Hashtable htPk = new Hashtable();
					htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
					htPk.put(EnvEnviosBean.C_IDENVIO,envBean.getIdEnvio());
					
					//obtengo el envio para ver si lo ha generado alguien durante el tiempo que
					// dura el proceso
					envBean = (EnvEnviosBean)envAdm.selectByPKForUpdate(htPk).firstElement();
		    		Envio envio = new Envio(envBean, _usrBean);
		    		
		    		
		    		
		    		try { 
		    			if(envBean.getIdEstado().compareTo(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO)==0){
		    				//Algo esta procesando este envio
		    				//(bien este mismo proceso en otro instante, bien el envio manual)
		    				continue;
							
						}else if(!envBean.getIdEstado().toString().equalsIgnoreCase(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_AUTOMATICO)){
							//Si algun otro proceso lo ha enviado pasamos al siguiente
							continue;
						}else{
							envBean.setIdEstado(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO);
			                envAdm.updateDirect(envBean);
			                
							
						}
			    		
		    			// OBTENCION DE DESTINATARIOS 
		    	        /////////////////////////////////////
		    			Vector vDestinatarios = null;
		    			try {
		    				vDestinatarios = envAdm.getDestinatarios(envBean.getIdInstitucion().toString(), 
				    				envBean.getIdEnvio().toString(), envBean.getIdTipoEnvios().toString());			
		    			} catch (Exception e) {
		    				SIGAException sigaExc = new SIGAException("messages.envios.errorDestinatarios");
		    				//envAdm.generarLogEnvioExceptionHT(envBean,sigaExc);
		    				throw sigaExc;
		    			}
		    			
		    			
		    			
		    			Hashtable htErrores = new Hashtable();
		    			
		    			envio.procesarEnvioMasivo(vDestinatarios,htErrores,false,tx);
		    	
		    			// yo genero el log fuera de la transaccion
		    			envAdm.generarLogEnvioHT(vDestinatarios, htErrores, envBean);
		    			
			    		ClsLogging.writeFileLog("### PROCESADO envío AUTOMATICO " + envBean.getDescripcion() + "(" + envBean.getIdEnvio() + ") ",7);
	
		    		} catch (SIGAException e) {
		    			envAdm.generarLogEnvioExceptionHT(envBean,e);
		    			envBean.setIdEstado(Integer.valueOf(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES));
		                envAdm.updateDirect(envBean);
			    		ClsLogging.writeFileLogError("@@@ Error procesando envío AUTOMATICO " + envBean.getDescripcion() + "(" + envBean.getIdEnvio() + ")  Mensaje:" + e.getMsg(""),e,3);
	
		    		} catch (Exception e) {
		    			envBean.setIdEstado(Integer.valueOf(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES));
		                envAdm.updateDirect(envBean);
		    			envAdm.generarLogEnvioExceptionHT(envBean,e);
			    		ClsLogging.writeFileLogError("@@@ Error procesando envío AUTOMATICO " + envBean.getDescripcion() + "(" + envBean.getIdEnvio() + ")  Mensaje:" + e.getLocalizedMessage(),e,3);
			    	}
			    }
			    
		    } else {
		        ClsLogging.writeFileLog("Proceso automatico envios: INSTITUCIÓN CON ENVIOS AUTOMÁTICOS BLOQUEADOS:"+idInstitucion,10);
		    }

	    } catch (Exception e) {
    		// RGG 13-09-2005 para que siga procesando otros envios automaticos
    		ClsLogging.writeFileLogError("@@@ Error GENERAL procesando envío AUTOMATICO Mensaje:" + e.getLocalizedMessage(),e,3);
	    }
	 }
	
	/**
	 * Calcula a partir de la preferencia de la direccion el tipo de envio.<br>
	 * Los valores que puede tomar prerencia son: E, F, C o una combinacion de ellos.
	 * 
	 * @param preferencia
	 * @return Integer tipo de envio
	 */
	public static Integer calculaTipoEnvio (String preferencia){
	    //Controlamos el valor preferente:
		Integer valorPreferencia = null;
	
		if (preferencia.indexOf("E")!=-1){
			valorPreferencia = new Integer(1);    						
		}
		else{
			if (preferencia.indexOf("C")!=-1){
				valorPreferencia = new Integer(2);    						
			}
			else{
				if (preferencia.indexOf("F")!=-1){
					valorPreferencia = new Integer(3);    						
				}	
				else{
					valorPreferencia = new Integer(1);
				}
			}
		}
		return valorPreferencia;
	}

    public void addDestinatarioIndividualDocAdjuntos(EnvDestinatariosBean destBean,
			  Vector vDocumentos,
			  boolean crearDestinatario) throws ClsExceptions, SIGAException {
    	addDestinatarioIndividualDocAdjuntos(destBean, vDocumentos, crearDestinatario, false);
    }
	
    /**
     * Inserta el destinatario manual y los documentos como documentos adjuntos al envio (general a todo envio).
     * @param destBean Destinatario
     * @param vDocumentos Vector de objetos Documento
     */
    public void addDestinatarioIndividualDocAdjuntos(EnvDestinatariosBean destBean,
            							  Vector vDocumentos,										  
            							  boolean crearDestinatario,
										  boolean crearCertificado)
    	throws ClsExceptions,SIGAException{
        
        

        try {
	        if (crearDestinatario ||crearCertificado){
		        //***** Insertamos Destinatario *******
	        	//
		        EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.usrBean);
		        //EnvEnviosAdm enviosAdm = new EnvEnviosAdm(idUsuario);
		        int tipo = enviosBean.getIdTipoEnvios().intValue();
		        
//		        if (crearCertificado || comprobarDireccion(tipo,destBean)){
		            try {
		                destAdm.insert(destBean);
		            } catch (ClsExceptions e) {
		                throw new SIGAException("messages.general.error",e);
		            }
//		        }
	        }
	
	        //******** Insertamos Documentos *********//
	        if (vDocumentos!=null && vDocumentos.size()>0){
		        String idInstitucion = String.valueOf(destBean.getIdInstitucion());
		        String idEnvio = String.valueOf(destBean.getIdEnvio());
		        String idPersona = String.valueOf(destBean.getIdPersona());
		        
		        EnvDocumentosBean documentoBean = null;
		        EnvDocumentosAdm docAdm = null;
		        
		        for (int i=0;i<vDocumentos.size();i++){
		        	//Recuperamos el documento:
		            Documento doc = (Documento)vDocumentos.get(i);
		            
	            	//Insertar documentos adjuntos:
    		        documentoBean = new EnvDocumentosBean();
    		        docAdm = new EnvDocumentosAdm(this.usrBean);
    		        
    		        //Calculamos el nuevo ID:
    	        	Integer idDoc = docAdm.getNewIdDocumento(idInstitucion,idEnvio);
    	        	
    	        	//Almacenamos el documento:
    	        	guardarDoc(idInstitucion, idEnvio, idPersona, String.valueOf(idDoc), doc);
    	        	
    		        documentoBean.setIdInstitucion(destBean.getIdInstitucion());
    		        documentoBean.setIdEnvio(destBean.getIdEnvio());
    		        documentoBean.setIdDocumento(idDoc);
    		        documentoBean.setPathDocumento(doc.getDocumento().getName());
    		        documentoBean.setDescripcion(doc.getDescripcion()); 
    		        doc.setBeanAsociado(documentoBean);
    		        docAdm.insert(documentoBean);
		        }
	        }
        } catch (SIGAException e){
    		throw e;
        } catch (ClsExceptions e){
    		throw e;
        } catch (Exception e){
    		throw new ClsExceptions(e,"Error en Envio.addDestinatarioIndividualDocAdjuntos");
        }
    }
	
}