
/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.informes;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

//import weblogic.management.console.info.AverageAccumulator;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnvioProgramadoBean;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvProgramIRPFBean;
import com.siga.beans.EnvProgramPagosBean;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.HelperInformesAdm;
import com.siga.certificados.Plantilla;
import com.siga.envios.Documento;
import com.siga.envios.Envio;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.form.InformesGenericosForm;


/**
 * Clase de genracion de informes IRPF
 * @author jtacosta
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InformeCertificadoIRPF extends MasterReport {

	private String datosEnvios;
	public File getInformeIRPF (MasterForm formulario,UsrBean usr,boolean isEnviar) 
	throws SIGAException,Exception{
		
		
		
		Date inicio = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO InformesGenericos.irpf",10);
		File ficheroSalida = null;


		//Obtenemos el formulario y sus datos:
		InformesGenericosForm miform = (InformesGenericosForm)formulario;

		Vector informesRes = new Vector(); 
		// Obtiene del campo idInforme los ids separados por ## y devuelve sus beans
		Vector plantillas = this.obtenerPlantillasFormulario(miform, usr);


		String idPersona=null;
		String anyoInformeIRPF=null;
		String idInstitucion=null;
		String idioma = null;
		String datosAEnviar = null;
		//Carga en el doc los datos comunes del informe (Institucion, idfacturacion,fecha)
		if (miform.getDatosInforme() != null && !miform.getDatosInforme().trim().equals("")) {
			// Obtiene del campo datosInforme los campos del formulario primcipal
			// para obtener la clave para el informe. LOs datos se obtienen en una cadena como los ocultos
			// y se sirven como un vector de hashtables por si se trata de datos multiregistro.

			Vector datos = this.obtenerDatosFormulario(miform);
			String idiomaExt = "es";
			// Se ha dejado preparado por si alguna vez se requiere poner los datos seleccionables(por checkbox en tabla)
			//Hasta que no se pida esa funcionalidad datos siempre tendra size 1
			for (int j = 0; j < datos.size(); j++) {
				Hashtable aux  = (Hashtable)datos.get(j);
				if (aux!=null) {

					idPersona= (String)aux.get("idPersona");
					anyoInformeIRPF= (String)aux.get("anyoInformeIRPF");
					idInstitucion= (String)aux.get("idInstitucion");
					idioma = (String)aux.get("idioma");

					//Como el idioma es el mismo para todos
					
					if(isEnviar ){
						datosAEnviar = getDatosAEnviar(idInstitucion, anyoInformeIRPF, idioma,idPersona, plantillas, usr);
					}else{
						if(j==0){
							AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
							idiomaExt = a.getLenguajeExt(idioma).toUpperCase();
						}
						for (int i=0;i<plantillas.size();i++) {
							AdmInformeBean b = (AdmInformeBean) plantillas.get(i);
							File archivo = getInformeIRPF(b, idInstitucion,anyoInformeIRPF, idPersona, idiomaExt,usr);
							informesRes.add(archivo);
								
						}
					}

				}
			}
		}else{
			throw new SIGAException("messages.informes.ningunInformeGenerado");

		}

		if(isEnviar){
			setDatosEnvios(datosAEnviar.toString());

		}else{
			//Si es enviar es que va a generar el fichero luego lo comprimimos en un zip
			if (informesRes.size()!=0) { 
				if (informesRes.size()<2) {
					ficheroSalida = (File) informesRes.get(0);
				} else {
					AdmTipoInformeAdm admT = new AdmTipoInformeAdm(usr);
					AdmTipoInformeBean beanT = admT.obtenerTipoInforme(miform.getIdTipoInforme());
					ArrayList ficherosPDF= new ArrayList();
					for (int i=0;i<informesRes.size();i++) {
						File f = (File) informesRes.get(i);
						ficherosPDF.add(f);
					}
					
					String nombreFicheroZIP=beanT.getDescripcion().trim() + "_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
				    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//					ReadProperties rp = new ReadProperties("SIGA.properties");
					String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
					rutaServidorDescargasZip += ClsConstants.FILE_SEP+miform.getIdInstitucion()+ClsConstants.FILE_SEP+"temp"+ File.separator;
					File ruta = new File(rutaServidorDescargasZip);
					ruta.mkdirs();
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
					ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
				}

			}
			else
				throw new SIGAException("messages.general.error.ficheroNoExiste");
		}


		Date fin = new Date(); 
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN InformesGenericos.irpf",10);
		ClsLogging.writeFileLog(fin.getTime()-inicio.getTime() + " MILISEGUNDOS ,==> SIGA: TIEMPO TOTAL",10);

		return ficheroSalida;	
	}
	private Hashtable anyadePersonaDatosInforme(Hashtable htDatosInforme,String idPersona,UsrBean usr)throws SIGAException,ClsExceptions{
		CenPersonaAdm cenPersonaAdm = new CenPersonaAdm(usr);
		
		Hashtable htPersona = new Hashtable();
		htPersona.put(CenPersonaBean.C_IDPERSONA, idPersona);
		Vector vPersona =  cenPersonaAdm.selectByPK(htPersona);
		CenPersonaBean personaBean = (CenPersonaBean) vPersona.get(0);
		htDatosInforme.put("NIFCIF_PER", personaBean.getNIFCIF() );
		StringBuffer nombreCompleto = new StringBuffer(personaBean.getNombre());
		
		if(personaBean.getApellido1()!=null){
			nombreCompleto.append(" ");
			nombreCompleto.append(personaBean.getApellido1());
		}
		if(personaBean.getApellido2()!=null){
			nombreCompleto.append(" ");
			nombreCompleto.append(personaBean.getApellido2());
		}
		htDatosInforme.put("NOMAPE_PER",nombreCompleto.toString());
		return htDatosInforme;
		
	}
	
	private String getDatosInformePersona(String idPersona,String anyoInformeIRPF,String idInstitucion
			,String idioma,String plantillas){
		StringBuffer sReturn = new StringBuffer();
		sReturn.append("idPersona==");
		sReturn.append(idPersona);
		sReturn.append("##anyoInformeIRPF==");
		sReturn.append(anyoInformeIRPF);
		sReturn.append("##idInstitucion==");
		sReturn.append(idInstitucion);
		sReturn.append("##idioma==");
		sReturn.append(idioma);
		sReturn.append("##plantillas==");
		sReturn.append(plantillas);
		
		sReturn.append("%%%");	
		return sReturn.toString();

	}
	

	private File getInformeIRPF (AdmInformeBean beanInforme, String idInstitucion,String anyoInformeIRPF,
			String idPersona,String idiomaExt,UsrBean usr) 
	throws SIGAException,ClsExceptions{

		
		File ficheroSalida = null;
		// --- acceso a paths y nombres 
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");	
		String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
		String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
		////////////////////////////////////////////////
		// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS

		String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+idInstitucion+ClsConstants.FILE_SEP+beanInforme.getDirectorio()+ClsConstants.FILE_SEP;
		String nombrePlantilla=beanInforme.getNombreFisico()+"_"+idiomaExt+".doc";
		String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+idInstitucion+ClsConstants.FILE_SEP+beanInforme.getDirectorio();

		File crear = new File(rutaAlm);
		if(!crear.exists())
			crear.mkdirs();


		MasterWords masterWord = new MasterWords(rutaPl+nombrePlantilla);
		Document docCertificadoIRPF = masterWord.nuevoDocumento(); 
		
		FacAbonoAdm admFacAbono = new FacAbonoAdm(usr);
		
		Vector vDatosInforme = admFacAbono.getRetencionesIRPF(idInstitucion, idPersona, anyoInformeIRPF);
		
		Hashtable htDatosComunesInforme = new Hashtable();
		htDatosComunesInforme.put("ANIO_E", anyoInformeIRPF);
		HelperInformesAdm helper = new HelperInformesAdm();
		//el metodo getPersonaInstitucion mete tambien el DIA_HOY,MES_HOY,ANYO_HOY 
		//del momento cuando se genera
		helper.completarHashSalida(htDatosComunesInforme, helper.getPersonaInstitucion(idInstitucion));
		
		String identificador = null;
		//Si solo hay una persona ponemos el idpersona en el titulo
		boolean isPersonaUnica = idPersona != null;
		if(isPersonaUnica){
			identificador=idInstitucion+"_"+idPersona+"_"+anyoInformeIRPF+".doc";
		}else{
			identificador=idInstitucion+"_"+anyoInformeIRPF+".doc";
		}
		
		
		Hashtable htRowDatosInforme = null;
		for (int i = 0; i < vDatosInforme.size(); i++) {
			htRowDatosInforme = (Hashtable)vDatosInforme.get(i);
			htRowDatosInforme.putAll(htDatosComunesInforme);
			if(!isPersonaUnica)
				idPersona = (String) htRowDatosInforme.get("IDPERSONASJCS");
			htRowDatosInforme = anyadePersonaDatosInforme(htRowDatosInforme,idPersona,usr);
			htRowDatosInforme = preparaPaginaCertificadoIRPF(htRowDatosInforme);
			
			
			
		}

		docCertificadoIRPF = masterWord.sustituyeRegionDocumento(docCertificadoIRPF,"pagina",vDatosInforme);
		String nombreArchivo = beanInforme.getNombreSalida()+"_"+identificador;
		ficheroSalida = masterWord.grabaDocumento(docCertificadoIRPF,rutaAlm,nombreArchivo);

		
		

		return ficheroSalida;	
	}
	private String getDatosAEnviar(String idInstitucion,String anyoInformeIRPF,String idioma,
			String idPersona,Vector vPlantillas,UsrBean usr)throws SIGAException,Exception{
		
		FacAbonoAdm admFacAbono = new FacAbonoAdm(usr);
		
		Vector vDatosInforme = admFacAbono.getRetencionesIRPF(idInstitucion, idPersona, anyoInformeIRPF);
		
		StringBuffer datosAEnviar = new StringBuffer();
		StringBuffer plantillas = new StringBuffer();
		for (int j = 0; j < vPlantillas.size(); j++) {
			AdmInformeBean b = (AdmInformeBean) vPlantillas.get(j);
			plantillas.append(b.getIdPlantilla());
			if(j!=vPlantillas.size()-1)
				plantillas.append("@@");
		}
		
		Hashtable htRowDatosInforme = null;
		for (int i = 0; i < vDatosInforme.size(); i++) {
			htRowDatosInforme = (Hashtable)vDatosInforme.get(i);
			String idPersonaRow = (String) htRowDatosInforme.get("IDPERSONASJCS");
			datosAEnviar.append(getDatosInformePersona(idPersonaRow,anyoInformeIRPF,idInstitucion,idioma,plantillas.toString()));
			//si es para enviar solo nos va a interesar el path de los archivos
			
			
		}
		return datosAEnviar.toString();
		
	}
	
	
	
	private Hashtable preparaPaginaCertificadoIRPF (Hashtable htDatosInforme) 
	throws SIGAException,ClsExceptions{

		
		htDatosInforme.put("ANIO_E",(String)htDatosInforme.get("ANIO_E"));
		htDatosInforme.put("NIFCIF_PER",(String)htDatosInforme.get("NIFCIF_PER"));
		htDatosInforme.put("NOMAPE_PER",(String)htDatosInforme.get("NOMAPE_PER"));
		htDatosInforme.put("NIFCIF_PAG",(String)htDatosInforme.get("NIFCIFINSTITUCION"));
		htDatosInforme.put("NOMAPE_PAG",(String)htDatosInforme.get("NOMBREINSTITUCION"));
		htDatosInforme.put("IMPORTE_INTEGRO",UtilidadesString.formatoImporte(Double.parseDouble((String)htDatosInforme.get("TOTALIMPORTESJCS"))));
		htDatosInforme.put("RETENCIONES",UtilidadesString.formatoImporte(Double.parseDouble((String)htDatosInforme.get("TOTALIMPORTEIRPF"))));
		htDatosInforme.put("LUGAR",(String)htDatosInforme.get("INSTITUCIONPOBLACION"));
		htDatosInforme.put("DIA_HOY",(String)htDatosInforme.get("DIA_HOY"));
		htDatosInforme.put("MES_HOY",(String)htDatosInforme.get("MES_HOY"));
		htDatosInforme.put("ANIO_HOY",(String)htDatosInforme.get("ANIO_HOY"));

		return htDatosInforme;	
	}
	
	
	
	
	public String getDatosEnvios() {
		return datosEnvios;
	}
	public void setDatosEnvios(String datosEnvios) {
		this.datosEnvios = datosEnvios;
	}
	public Vector getDocumentosAEnviar(String plantillas,String anyoIRPF,String idPersona,String idioma,String idInstitucion,UsrBean usrBean)throws ClsExceptions,SIGAException{
		Vector vPlantillas = getPlantillas(plantillas,idInstitucion,usrBean);
		AdmLenguajesAdm a = new AdmLenguajesAdm(usrBean);
		String idiomaExt = a.getLenguajeExt(idioma).toUpperCase();
		Vector vDocumentos = new Vector();
		for (int i = 0; i < vPlantillas.size(); i++) {
			AdmInformeBean beanInforme = (AdmInformeBean)vPlantillas.get(i);
			File fileDocumento = getInformeIRPF(beanInforme, idInstitucion, anyoIRPF,
					idPersona, idiomaExt, usrBean);
			String pathDocumento=fileDocumento.getPath();
			
			// Creacion documentos
			int indice = pathDocumento.lastIndexOf(ClsConstants.FILE_SEP);
			String descDocumento = "";
			if(indice >0)
				descDocumento = pathDocumento.substring(indice+1);
			
			
			Documento documento = new Documento(pathDocumento,descDocumento);
			
			vDocumentos.add(documento);
			
			
		}
		return vDocumentos;
		
	}
	public void enviarCertificadoIRPFColegiado(UsrBean usrBean, EnvProgramIRPFBean programIRPFBean, 
			EnvEnvioProgramadoBean envioProgramadoBean)throws ClsExceptions,SIGAException{
		
		Envio envio = new Envio(usrBean,envioProgramadoBean.getNombre());

		// Bean envio
		EnvEnviosBean enviosBean = envio.getEnviosBean();
		enviosBean.setDescripcion(enviosBean.getIdEnvio()+" "+enviosBean.getDescripcion());
		// trunco la descripción
		if (enviosBean.getDescripcion().length()>200)  enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,99));

		// Preferencia del tipo de envio si el usuario tiene uno:
		enviosBean.setIdTipoEnvios(envioProgramadoBean.getIdTipoEnvios());

		enviosBean.setIdPlantillaEnvios(envioProgramadoBean.getIdPlantillaEnvios());
		if (envioProgramadoBean.getIdPlantilla()!=null && !envioProgramadoBean.getIdPlantilla().equals("")) {
			enviosBean.setIdPlantilla(envioProgramadoBean.getIdPlantilla());
		} else {
			enviosBean.setIdPlantilla(null);
		}
		
		Vector vDocumentos = getDocumentosAEnviar(programIRPFBean.getPlantillas(), programIRPFBean.getAnyoIRPF().toString(),
				programIRPFBean.getIdPersona().toString(), programIRPFBean.getIdiomaCodigoExt(),programIRPFBean.getIdInstitucion().toString(), usrBean);
		
	
		// Genera el envio:
		envio.generarEnvio(programIRPFBean.getIdPersona().toString(), EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,vDocumentos);
		
	} 
	private Vector getPlantillas(String plantillas, String idInstitucion,UsrBean usr)
	throws ClsExceptions,SIGAException{
		    
        String d[]= plantillas.split("@@");
        Vector vPlantillas = new Vector(); 
        AdmInformeAdm admInforme = new AdmInformeAdm(usr);
        
        for (int i = 0; i < d.length; i++) {
        	String idPlantilla = d[i];
        	AdmInformeBean beanInforme = admInforme.obtenerInforme(idInstitucion, idPlantilla);
        	vPlantillas.add(beanInforme);
			
		}
            
        return vPlantillas;
    
		
		
	} 
	
	
	
	
}