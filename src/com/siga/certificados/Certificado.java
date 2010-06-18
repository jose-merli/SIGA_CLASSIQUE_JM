package com.siga.certificados;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CerPlantillasAdm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeCertificadosEspeciales;

public class Certificado
{
	public static final String CERT_TIPO_CURSO 		= "NOMBRECURSO"; 
	public static final String CERT_TIPO_TURNO 		= "TURNOS"; 
	public static final String CERT_TIPO_PASANTIA 	= "LETRADOPASANTIAS"; 
	public static final String CERT_TIPO_DESPACHO1 	= "HISTORICO_DESPACHOS";
	public static final String CERT_TIPO_DESPACHO2 	= "HISTORICO_DESPACHOS";	
	public static final String CERT_TIPO_BANCO	 	= "BANCOS";	
	public static final String CERT_TIPO_DIRECCION	= "HISTORICO_DIRECCIONES";
	public static final String CERT_TIPO_COMPONENTES= "COMPONENTES";
	public static final String INI= "INI_";

	
	
    public static void generarCertificadoPDF(String idTipoProducto,
            								 String idProducto,
            								 String idProductoInstitucion,
            								 String idInstitucion,
            								 String idPlantilla,
            								 String idPersona,
            								 File fIn,
            								 File fOut,
            								 String sBaseDir,
            								 String idSolicitud,
											 String idInstitucionOrigen,
											 boolean usarIdInstitucion, UsrBean usr,HttpServletRequest request) throws SIGAException
    {
        try
        {
            CerPlantillasAdm admPlantilla = new CerPlantillasAdm(usr);
            File fPlantilla = admPlantilla.obtenerPlantilla(String.valueOf(idInstitucion), idTipoProducto, idProducto, idProductoInstitucion, idPlantilla);
            
            Plantilla plantilla = new Plantilla(fPlantilla,usr);
            CenClienteAdm admCliente = new CenClienteAdm(usr);
            //Hashtable htDatos = admCliente.getDatosCertificado(new Long(idPersona), new Integer(idInstitucion));
            Hashtable htParametros = new Hashtable();
            // formamos la consulta de datos
            htParametros.put("@idpersona@", ""+idPersona);
            // RGG Se obtiene siempre de la institucion origen si existe, a no ser que nos digan que la cojamos del cgae.
            if (usarIdInstitucion) {
            	htParametros.put("@idinstitucion@", ""+idInstitucion);
            } else {
            	htParametros.put("@idinstitucion@", ""+idInstitucionOrigen);
            }
            htParametros.put("@idsolicitud@", ""+idSolicitud);
            htParametros.put("@idioma@", ""+usr.getLanguage());
            String certEspecial=esCertificadoMultiRegistro( fPlantilla);
            if(certEspecial==null ||certEspecial.equals("")){
            	
                // datos certificado en plantilla
	            Hashtable htDatos = admCliente.getDatosCertificado(htParametros,idInstitucion);
	            htDatos = admPlantilla.darFormatoCampos(String.valueOf(idInstitucion), idTipoProducto, idProducto, idProductoInstitucion, idPlantilla,usr.getLanguageInstitucion(), htDatos);
	            plantilla.sustituirEtiquetas(htDatos, fIn);
	            
	            // proceso de generacion
	            plantilla.convertFO2PDF(fIn, fOut, sBaseDir);
            }else{
            		htParametros.put("TIPOCERTIFICADO",certEspecial);
            		htParametros.put("IDPLANTILLA",idPlantilla);
            		InformeCertificadosEspeciales informe =new InformeCertificadosEspeciales(usr);
            		informe.generarListadoCertificados(request,htParametros, fOut.getParent(),fOut.getName(),fPlantilla.getParent(),fPlantilla.getName(),sBaseDir);
            }
        }
        catch(SIGAException e)
        {
        	throw e;
        }
        catch(Exception e)
        {
            throw new SIGAException("Error al generar el PDF del Certificado.",e);
        }
    }
    
	public static String esCertificadoMultiRegistro(File fichero) throws ClsExceptions,SIGAException{
		
		try{	    
		
			String[] etiquetas = {Certificado.CERT_TIPO_CURSO, 
								  Certificado.CERT_TIPO_PASANTIA, 
								  Certificado.CERT_TIPO_TURNO,
								  Certificado.CERT_TIPO_DESPACHO1,
								  Certificado.CERT_TIPO_DESPACHO2,
								  Certificado.CERT_TIPO_BANCO,
								  Certificado.CERT_TIPO_DIRECCION,
								  Certificado.CERT_TIPO_COMPONENTES};	
			//Leo el fichero y lo paso a un string
			//Reader rArchivo = new FileReader(this.archivo);
			BufferedReader rArchivo = null;
			
			try {
				rArchivo = new BufferedReader(new InputStreamReader(new FileInputStream(fichero), "8859_1"));
			} catch (Exception e) {
				throw new SIGAException("messages.envios.error.noPlantilla");
			}
			if (!fichero.exists()) {
				throw new SIGAException("messages.envios.error.noPlantilla");
			}
		    if (!fichero.canRead()){
				throw new ClsExceptions("Error de lectura del fichero de la plantilla: "+fichero.getAbsolutePath());
//				throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
		    }

		    StringBuffer sb = new StringBuffer( );
			
	        char[] b = new char[1000];
	        int n;
	        
	        // Read a block. If it gets any chars, append them.
	        while ((n = rArchivo.read(b)) > 0) {
	            sb.append(b, 0, n);
	        }
	        
	        rArchivo.close();
	        
	        String sArchivo = sb.toString();
	        
	        for (int i = 0; i < etiquetas.length; i++) {
		        if (sArchivo.indexOf (Certificado.INI+etiquetas[i]) >= 0) {
		        	return etiquetas[i];
		        }
	        }
	       
	       }
		  catch(Exception e)
	        {
	            throw new SIGAException("Error al leer la plantilla del Certificado.",e);
	        }
	       return null;
	   }
	
	/*public File generarCertificadoTurnos(HttpServletRequest request, String rutaServidorTmp, String contenidoPlantilla, String rutaServidorDescargas, String nombreFicheroPDF) throws ClsExceptions 
	{
	
		File ficheroFOP=null;		
		File ficheroPDF=null;
		File rutaTmp=null;
		File rutaPDF=null;
		
		try {
			nombreFicheroPDF = UtilidadesString.validarNombreFichero(nombreFicheroPDF);
			
			//Crea la ruta temporal
			rutaTmp = new File(rutaServidorTmp);
			rutaTmp.mkdirs();
			ficheroFOP = new File(rutaTmp+ClsConstants.FILE_SEP+nombreFicheroPDF+System.currentTimeMillis()+".fo");
			//ficheroFOP = new File(rutaTmp.getParent()+ClsConstants.FILE_SEP+nombreFicheroPDF+System.currentTimeMillis()+".fo");
			
			// Generacion del fichero .FOP para este usuario a partir de la plantilla .FO 						
			String content=reemplazarDatos(request,contenidoPlantilla);
			UtilidadesString.setFileContent(ficheroFOP,content);

			//Crea el fichero. Si no existe el directorio de la institucion para la descarga lo crea.
			rutaPDF = new File(rutaServidorDescargas);
			ficheroPDF = new File(rutaServidorDescargas+ClsConstants.FILE_SEP+nombreFicheroPDF+".pdf");
			
			//Clase para la conversion de FOP a PDF con un directorio base para usar rutas relativas:
			Plantilla plantilla = new Plantilla();
			plantilla.convertFO2PDF(ficheroFOP, ficheroPDF, rutaTmp.getParent());
			
		} catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe: "+e.getLocalizedMessage());
		} finally {
			if (ficheroFOP!=null && ficheroFOP.exists()) {
				ficheroFOP.delete();
			}
			if (rutaTmp!=null && rutaTmp.exists()) {
				rutaTmp.delete();
			}
			
		}
		return ficheroPDF;
	}*/
}