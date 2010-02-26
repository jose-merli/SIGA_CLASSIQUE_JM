/*
 * Created on Mar 4, 2005
 * @author emilio.grau
 * modified by miguel.villegas 14/04/05 incorpora los metodos deteccionEtiquetas() y deteccionYSustitucionEtiquetas()
 *
 */
package com.siga.certificados;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Options;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenPaisAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPoblacionesAdm;
import com.siga.beans.CenProvinciaAdm;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacLineaFacturaBean;
import com.siga.general.SIGAException;


/**
 * Plantillas para certificados
 */
public class Plantilla {

	private File archivo;
	private UsrBean userbean;
	
	public Plantilla(File archivo,UsrBean us){
		this.archivo=archivo;
		this.userbean=us;
	}
	
	public Plantilla(UsrBean us){
		this.userbean=us;
	}

	
	/** 
	 * Funcion que sustituye etiquetas en un archivo. 
	 * El resultado se guarda en el directorio ./out/ con el nombre que tuviera el fichero inicial
	 * @param  etiquetas Hashtable con los valore 'nombre etiqueta' - 'valor'
	 * @param  archivo File de salida con las etiquetas sustituidas	 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public void sustituirEtiquetas(Hashtable etiquetas, File salida) throws ClsExceptions,SIGAException{
		
	    Writer wArchivo = null;
	    BufferedReader rArchivo = null;
		try{	    
			//Leo el fichero y lo paso a un string
			//Reader rArchivo = new FileReader(this.archivo);
			
			
			try {
				rArchivo = new BufferedReader(new InputStreamReader(new FileInputStream(this.archivo), "8859_1"));
			} catch (Exception e) {
				throw new SIGAException("messages.envios.error.noPlantilla");
			}
			if (!this.archivo.exists()) {
				throw new SIGAException("messages.envios.error.noPlantilla");
			}
		    if (!this.archivo.canRead()){
				throw new ClsExceptions("Error de lectura del fichero de la plantilla: "+this.archivo.getAbsolutePath());
//				throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
		    }

		    StringBuffer sb = new StringBuffer( );
			
	        char[] b = new char[1000];
	        int n;
	        
	        // Read a block. If it gets any chars, append them.
	        while ((n = rArchivo.read(b)) > 0) {
	            sb.append(b, 0, n);
	        }
	        
	        String sArchivo = sb.toString();
			
			if (!etiquetas.isEmpty()){
				String key = "";
				for (Enumeration e = etiquetas.keys(); e.hasMoreElements() ;) {
			         key = (String)e.nextElement();
			         final Pattern pattern = Pattern.compile("%%"+key+"%%");
			         final Matcher matcher = pattern.matcher( sArchivo );
			         
			         
			         sArchivo = matcher.replaceAll( UtilidadesString.formato_ISO_8859_1((String)etiquetas.get(key)));
			         //sArchivo.replaceAll("%%"+key+"%%",(String)etiquetas.get(key));
			    }
			}
			/*
			final Pattern patternBR = Pattern.compile("%%BR%%");
	        final Matcher matcherBR = patternBR.matcher( sArchivo );
			sArchivo = matcherBR.replaceAll("<fo:block  space-before=\"0.5cm\" />");
			*/
			
			///////////////////////
			// Antes sArchivo = sArchivo.replaceAll("BRdummyBR","<fo:block  space-before=\"0.5cm\" />");
			// Ahora
			sArchivo = UtilidadesString.replaceAllIgnoreCase(sArchivo, "BRdummyBR", "<fo:block  space-before=\"0.2cm\" />");
			///////////////////////
			
			
			//Debemos asegurar que existe el directorio destino del fichero de salida
			File fParent = salida.getParentFile();
			if (!fParent.exists()) fParent.mkdirs();
			
			/*Writer wArchivo = new FileWriter(salida);
			wArchivo.write(sArchivo);*/
			
			
	        wArchivo = new BufferedWriter(
	            new OutputStreamWriter(new FileOutputStream(salida), "8859_1"));
	        wArchivo.write(sArchivo);
	        
	        wArchivo.close();		    
			rArchivo.close();
			
			
		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al sustituir etiquetas."); 
		} finally {
		    try {
		        wArchivo.close();		    
				rArchivo.close(); 
		    } catch (Exception eee) {}
		}

	}

		
	
	/** 
	 * Funcion que detecta etiquetas de formato %%etiqueta%% en una linea de texto 
	 * @param  entada - texto de entrada	 
	 * @return  Vector - etiquetas localizadas
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector deteccionEtiquetas(String entrada) throws ClsExceptions{
		
		Vector resultado = new Vector();
		
		try{
			String [] particiones=entrada.split("%%");
			if (particiones.length>=2){
				for (int i=0; i<particiones.length; i++){
					if (entrada.indexOf("%%"+particiones[i]+"%%")!=-1){
						resultado.addElement(particiones[i]);
					}
				}
			}			
		} catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al sustituir etiquetas."); 
		}			
		return resultado;	
	}
	
	
	
	
	
	/**
     * Converts an FO file to a PDF file using FOP
     * @param fo the FO file
     * @param pdf the target PDF file
     * @throws IOException In case of an I/O problem
     * @throws FOPException In case of a FOP problem
     */
    public void convertFO2PDF(File fo, File pdf) 
    throws IOException, FOPException, ClsExceptions
    {
    	convertFO2PDF(fo, pdf, fo.getPath());
    }
    
    public synchronized  void convertFO2PDF(File fo, File pdf, String sBaseDir) 
    throws IOException, FOPException, ClsExceptions
    {
    	OutputStream out = null;
    	FileOutputStream fileOut = null;
    	
        try {
        	    		
        	ClsLogging.writeFileLog(">>> baseDir:" + sBaseDir,10);
        	
            org.apache.fop.configuration.Configuration.put("baseDir", sBaseDir);
            org.apache.fop.configuration.Configuration.put("fontBaseDir", SIGAReferences.getReference(SIGAReferences.RESOURCE_FILES.FOP_DIR.getFileName()));
//            new Options(new File(ClsConstants.FOP_CONFIG_FILE));
            new Options(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.FOP));

            ClsLogging.writeFileLog(">>> org.apache.fop.configuration.Configuration",10);
            // Construct driver and setup output format
            Driver driver = new Driver();
            ClsLogging.writeFileLog(">>> Driver driver",10);
            
            driver.setRenderer(Driver.RENDER_PDF);
            ClsLogging.writeFileLog(">>> driver.setRenderer",10);
            
    		ClsLogging.writeFileLog("CONVERT: driver creado.",10);
            
            // Setup output stream.  Note: Using BufferedOutputStream
            // for performance reasons (helpful with FileOutputStreams).
            fileOut = new FileOutputStream(pdf);
            out = new BufferedOutputStream(fileOut);
            driver.setOutputStream(out);

    		ClsLogging.writeFileLog("CONVERT: stream creado.",10);

            // Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            //System.out.println(factory.getClass().getName());
            
            Transformer transformer = factory.newTransformer(); // identity transformer

    		ClsLogging.writeFileLog("CONVERT: transformer creado.",10);
            
            // Setup input stream
            Source src = new StreamSource(fo);
            //Source src = new StreamSource(foFormateado);

    		ClsLogging.writeFileLog("CONVERT: source creado.",10);

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(driver.getContentHandler());

    		ClsLogging.writeFileLog("CONVERT: result creado.",10);

            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);
            
    		ClsLogging.writeFileLog("CONVERT: PDF creado.",10);
            
            //Borramos la cache de ficheros:
            org.apache.fop.image.FopImageFactory.resetCache();
    		ClsLogging.writeFileLog("CONVERT: cache borrada. TODO OK.",10);
    		ClsLogging.writeFileLog("CONVERT: El fichero se almacenara en "+pdf,10);

        } 
        catch (Exception e) {
        	if (this.userbean!=null) {
        	    ClsLogging.writeFileLogError("Error transformando PDF desde FOP - Fichero:"+sBaseDir+" - Mensaje:" +e.getLocalizedMessage(),e,this.userbean,3);
        	} else {
        	    ClsLogging.writeFileLogError("Error transformando PDF desde FOP - Fichero:"+sBaseDir+" - Mensaje:" +e.getLocalizedMessage(),e,3);
        } 
            throw new ClsExceptions (e, "Error transformando PDF desde FOP - Fichero:"+sBaseDir+" - Mensaje:" +e.getLocalizedMessage());
        } 
        finally {
        	try {
	            out.close();
	            fileOut.close();
        	} catch (Exception e) {}
        }
    }
    
	/**  
	 * Obtienen los distintos modelos de factura asociados a una institucion determinada
	 * @param  institucion - identificador de la institucion involucrada	 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtencionModelosFacturas(String institucion) throws ClsExceptions, SIGAException {
		
		File directorioPlantillas=null; 		
		BufferedReader bufferLectura=null;
		Vector modelos = new Vector();
		
		try{			
						
			// Obtencion plantilla factura
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");			
		    String rutaPlantilla = rp.returnProperty("facturacion.directorioFisicoPlantillaFacturaJava")+rp.returnProperty("facturacion.directorioPlantillaFacturaJava");		    
		    String barra="";
    		if (rutaPlantilla.indexOf("/") > -1){ 
    			barra = "/";
    		}
    		if (rutaPlantilla.indexOf("\\") > -1){ 
    			barra = "\\";
    		}
    		rutaPlantilla+=barra+institucion+barra;
    		
    		directorioPlantillas = new File(rutaPlantilla);
			
		    if (!directorioPlantillas.exists()){
				//throw new ClsExceptions("facturacion.nuevoFichero.literal.errorAcceso");
		    	// throw new SIGAException("messages.envios.error.noPlantilla");
		    	throw new SIGAException("messages.envios.error.noPlantilla");
	        } 
		    else{
			    if (!directorioPlantillas.canRead()){
					throw new ClsExceptions("Error de lectura del fichero: "+directorioPlantillas.getAbsolutePath());
//					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    }
			    else{
			    	try {
			    		
			    		// lista de directorios/ficheros
			    		String [] directorios = directorioPlantillas.list();												
						modelos.clear();
						for (int i=0; i<directorios.length; i++){
							File posibleModelo = new File(rutaPlantilla+directorios[i]);
							if (posibleModelo.isDirectory()){
								if (!posibleModelo.getName().equalsIgnoreCase("recursos")){
									modelos.addElement(directorios[i]);
								}
							}
						}
				    							
			    	}catch(Exception _ex) {
						bufferLectura.close();
						throw new ClsExceptions(_ex,"Error de insercion en direcetorio");						
					}
			    }				    	
		    }

		} 
		catch (Exception e) { 
//			throw new ClsExceptions(e,"facturacion.nuevoFichero.literal.errorLectura"); 
			throw new ClsExceptions(e, e.getMessage());
		}
		return modelos;
	}
        
	/** 
	 * Crea una pagina de factura   
	 * @param  fichero - fichero a generar
	 * @param  factura - datos generales de la factura 
	 * @param  desglose - lineas de la factura
	 * @param  rutaPlantilla - ruta de la plantilla especifica 
	 * @param  nombreFichero - plantilla de la pagina especifica de la factura
	 * @param  numeroLineas - numero de lineas a lpasmar en la factura
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
/*	public boolean obtencionPaginaFactura(File fichero, Hashtable factura, Vector desglose, String rutaPlantilla, String nombreFichero, int numeroLineas) throws ClsExceptions, SIGAException {

		File plantillaFactura=null; 
		File plantillaLinea=null;
		int contador=0;
		BufferedReader bufferLectura=null;
		BufferedReader bufferLecturaLinea=null;
		String linea;
		PrintWriter printer=null;
		boolean correcto=true;
		Plantilla plantilla = new Plantilla();
		
		try{    		
			plantillaFactura = new File(rutaPlantilla+nombreFichero);
			
		    if (!plantillaFactura.exists()){
				//throw new ClsExceptions("facturacion.nuevoFichero.literal.errorAcceso");
		    	throw new SIGAException("messages.envios.error.noPlantilla");
	        }
		    else{
			    if (!plantillaFactura.canRead()){
					throw new ClsExceptions("Error de lectura del fichero: "+plantillaFactura.getAbsolutePath());
//					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    }
			    else{
			    	try {
			    		
			    		// Creacion de la plantilla especifica para la factura
						bufferLectura = new BufferedReader(new FileReader(plantillaFactura));												
						printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
						printer.flush();
						// Construccion hashtable
						// Lectura plantilla
						linea=bufferLectura.readLine();
				    	while (linea != null){
				    		Vector detectadas=new Vector();
				    		detectadas=plantilla.deteccionEtiquetas(linea);
				    		if (!detectadas.isEmpty()){
				    			Enumeration listaEtiquetas=detectadas.elements();
				    			while (listaEtiquetas.hasMoreElements()){
				    				String etiqueta=(String)listaEtiquetas.nextElement();
				    				if (etiqueta.equalsIgnoreCase("DESGLOSE")){
				    					// Genero lineas de desglose
				    					Enumeration listaLineas=desglose.elements();
				    					// Abro el fichero con las plantillas de las lineas
			    						plantillaLinea = new File(rutaPlantilla+"plantillaLineaFactura.fo");
			    					    if (!plantillaLinea.exists()){
			    							//throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");
			    					    	throw new SIGAException("messages.envios.error.noPlantilla");
			    				        }
			    					    else{
			    						    if (!plantillaLinea.canRead()){
			    								throw new ClsExceptions("Error de lectura del fichero: "+plantillaLinea.getAbsolutePath());
//			    								throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    						    }
			    						    else{			    						    	
						    					int j=0;
						    					while ((listaLineas.hasMoreElements())&&(j<numeroLineas)){
						    						Hashtable lineaFactura=((Row)listaLineas.nextElement()).getRow();				    						
						    						// Aplicacion de la plantilla de lineas en las facturas
				    								bufferLecturaLinea = new BufferedReader(new FileReader(plantillaLinea));
				    								linea=bufferLecturaLinea.readLine();
				    						    	while (linea != null){
				    						    		Vector detectadasLinea=new Vector();				    						    		
				    						    		detectadasLinea=plantilla.deteccionEtiquetas(linea);
				    						    		if (!detectadasLinea.isEmpty()){
				    						    			Enumeration listaEtiquetasLinea=detectadasLinea.elements();
				    						    			while (listaEtiquetasLinea.hasMoreElements()){
				    						    				String etiquetaLinea=(String)listaEtiquetasLinea.nextElement();
				    					    					linea=sustitucionEtiquetaDesglose(lineaFactura,etiquetaLinea);
				    							    			printer.println(linea);
				    						    			}
				    						    		}
				    						    		else{
			    							    			printer.println(linea);
				    						    		}
					    								linea=bufferLecturaLinea.readLine();
				    						    	}
				    						    	bufferLecturaLinea.close();
						    					    j++;
						    					}
			    						    }
			    					    }
				    				}
				    				else{
				    					linea=sustitucionEtiqueta(factura,etiqueta);
						    			printer.println(linea);
				    				}
				    			}
				    		}
				    		else{
				    			printer.println(linea);
				    		}
				    		contador++;
							linea=bufferLectura.readLine();
				    	}
				        printer.flush();
				        printer.close();
				    	bufferLectura.close();
				    	
					}catch(IOException _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}catch(Exception _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}
			    }				    	
		    }
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de obtención de paginas de facturacion."); 
		}
		
		return correcto;
	}
	*/
	/** 
	 * Sustituye los campos específicos de la factura pasada como parametro en la plantilla  
	 * @param  factura - datos generales de la factura 
	 * @param  desglose - lineas de la factura
	 * @return  String - linea a imprimir  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	/*protected String sustitucionEtiqueta(Hashtable factura, String etiqueta) throws ClsExceptions {

		String resultado="";
		
		try{			
			if (etiqueta.equalsIgnoreCase("NOMBRE_COLEGIO")){
				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(user);
				resultado=institucionAdm.getNombreInstitucion((String)factura.get(CenInstitucionBean.C_IDINSTITUCION));
			} 
			if (etiqueta.equalsIgnoreCase("DIRECCION_COLEGIO")){
				// identificador de la persona/institucion
				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(user);
				String idPersona=institucionAdm.getIdPersona((String)factura.get(CenInstitucionBean.C_IDINSTITUCION));
				// direccion institucion
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(user);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"3");
				if (direccion.get("DOMICILIO")!=null){
					resultado=(String)direccion.get(CenDireccionesBean.C_DOMICILIO);
				}	
			}
			if (etiqueta.equalsIgnoreCase("CODIGO_POSTAL_POBLACION_PROVINCIA_COLEGIO")){
				// identificador de la persona/institucion
				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(user);
				String idPersona=institucionAdm.getIdPersona((String)factura.get(CenInstitucionBean.C_IDINSTITUCION));
				// datos institucion
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(user);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecificaYUbicacion(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"3");
				if ((direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)!=null)&&(direccion.get("POBLACION")!=null) &&(direccion.get("PROVINCIA")!=null)){
					resultado=(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)+", "+(String)direccion.get("POBLACION")+", "+(String)direccion.get("PROVINCIA");
				}	
			}
			if (etiqueta.equalsIgnoreCase("TELEFONOS_EMAIL_COLEGIO")){
				// identificador de la persona/institucion
				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(user);
				String idPersona=institucionAdm.getIdPersona((String)factura.get(CenInstitucionBean.C_IDINSTITUCION));
				// datos institucion
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(user);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"3");
				if (direccion.get("TELEFONO1")!=null){
					if (!((String)direccion.get("TELEFONO1")).equalsIgnoreCase("")){
						resultado=(String)direccion.get(CenDireccionesBean.C_TELEFONO1);
					}	
				}
				if (direccion.get("TELEFONO2")!=null){
					if (!((String)direccion.get("TELEFONO2")).equalsIgnoreCase("")){
						resultado+=", "+(String)direccion.get(CenDireccionesBean.C_TELEFONO2);
					}
				}
				if (direccion.get("MOVIL")!=null){
					if (!((String)direccion.get("MOVIL")).equalsIgnoreCase("")){
						resultado+=", "+(String)direccion.get(CenDireccionesBean.C_MOVIL);
					}
				}
				if (direccion.get(CenDireccionesBean.C_CORREOELECTRONICO)!=null){
					if (!((String)direccion.get(CenDireccionesBean.C_CORREOELECTRONICO)).equalsIgnoreCase("")){
						resultado+="  "+(String)direccion.get("CORREOELECTRONICO");
					}
				}
			}
			if (etiqueta.equalsIgnoreCase("CIF_COLEGIO")){
				// identificador de la persona/institucion
				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(user);
				String idPersona=institucionAdm.getIdPersona((String)factura.get(CenInstitucionBean.C_IDINSTITUCION));
				CenPersonaAdm personaAdm = new CenPersonaAdm(user);
				resultado=personaAdm.obtenerNIF(idPersona);	
			
					
			}
			if (etiqueta.equalsIgnoreCase("NOMBRE_CLIENTE")){
				String idPersona=(String)factura.get(CenInstitucionBean.C_IDPERSONA);
				CenPersonaAdm personaAdm = new CenPersonaAdm(user);
				resultado=personaAdm.obtenerNombreApellidos(idPersona);
			}
			if (etiqueta.equalsIgnoreCase("DIRECCION_CLIENTE")){
				String idPersona=(String)factura.get(CenInstitucionBean.C_IDPERSONA);
				// datos persona
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(user);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"2");
				if (direccion.get(CenDireccionesBean.C_DOMICILIO)!=null&&!((String)direccion.get(CenDireccionesBean.C_DOMICILIO)).equals("")){
					resultado=(String)direccion.get(CenDireccionesBean.C_DOMICILIO);
				}else{
					resultado="-";
				}
			}
			if (etiqueta.equalsIgnoreCase("CODIGO_POSTAL_POBLACION_PROVINCIA_CLIENTE")){
				String idPersona=(String)factura.get(CenInstitucionBean.C_IDPERSONA);
				// datos institucion
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(user);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecificaYUbicacion(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"2");
				if ((direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)!=null)&&(direccion.get("POBLACION")!=null) &&(direccion.get("PROVINCIA")!=null)){
					resultado=(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)+", "+(String)direccion.get("POBLACION")+", "+(String)direccion.get("PROVINCIA");
				}else{
					resultado="-";
				}
			}
			if (etiqueta.equalsIgnoreCase("TELEFONOS_CLIENTE")){
				String idPersona=(String)factura.get(CenInstitucionBean.C_IDPERSONA);
				// datos institucion
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(user);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"2");
				if (direccion.get(CenDireccionesBean.C_TELEFONO1)!=null){
					if (!((String)direccion.get(CenDireccionesBean.C_TELEFONO1)).equalsIgnoreCase("")){
						resultado=(String)direccion.get(CenDireccionesBean.C_TELEFONO1);
					}else{
						resultado="-";
					}
				}else{
					resultado="-";
				}
				if (direccion.get(CenDireccionesBean.C_TELEFONO2)!=null){
					if (!((String)direccion.get(CenDireccionesBean.C_TELEFONO2)).equalsIgnoreCase("")){
						resultado+=", "+(String)direccion.get(CenDireccionesBean.C_TELEFONO2);
					}
				}
				if (direccion.get(CenDireccionesBean.C_MOVIL)!=null){
					if (!((String)direccion.get(CenDireccionesBean.C_MOVIL)).equalsIgnoreCase("")){
						resultado+=", "+(String)direccion.get(CenDireccionesBean.C_MOVIL);
					}
				}				
			}
			if (etiqueta.equalsIgnoreCase("EMAIL_CLIENTE")){
				String idPersona=(String)factura.get(CenInstitucionBean.C_IDPERSONA);
				// datos persona
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(user);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"2");
				if (direccion.get(CenDireccionesBean.C_CORREOELECTRONICO)!=null&&!((String)direccion.get(CenDireccionesBean.C_CORREOELECTRONICO)).equals("")){
					resultado=(String)direccion.get(CenDireccionesBean.C_CORREOELECTRONICO);
				}else{
					resultado="-";
				}
			}
			if (etiqueta.equalsIgnoreCase("NIFCIF_CLIENTE")){
				String idPersona=(String)factura.get(CenInstitucionBean.C_IDPERSONA);
				// datos persona
				CenPersonaAdm personaAdm = new CenPersonaAdm(user);
				resultado=personaAdm.obtenerNIF(idPersona);	
				if (resultado.equalsIgnoreCase("")){
					resultado="-";
				}
					
				
			}
			if (etiqueta.equalsIgnoreCase("NUMERO_FACTURA")){
				resultado=(String)factura.get(FacFacturaBean.C_NUMEROFACTURA);
				if (resultado.equalsIgnoreCase("")){
					resultado="-";
				} 
			}
			if (etiqueta.equalsIgnoreCase("FECHA_FACTURA")){
				resultado=GstDate.getFormatedDateShort("",(String)factura.get(FacFacturaBean.C_FECHAEMISION));
				if (resultado.equalsIgnoreCase("")){
					resultado="-";
				}
			}
			if (etiqueta.equalsIgnoreCase("NUMERO_COLEGIADO_FACTURA")){
				String idPersona=(String)factura.get(CenInstitucionBean.C_IDPERSONA);
				CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(user);
				CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(new Long(idPersona), new Integer((String)factura.get(FacFacturaBean.C_IDINSTITUCION)));
				resultado = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
				if (resultado.equalsIgnoreCase("")){
					resultado="-";
				}
			}
			if (etiqueta.equalsIgnoreCase("FORMA_PAGO_FACTURA")){
				if ((factura.get(FacFacturaBean.C_IDCUENTA)==null)||(((String)factura.get(FacFacturaBean.C_IDCUENTA))).equalsIgnoreCase("")){
					resultado="Pago por Caja/Tarjeta";
				}
				else{
					//resultado=(String)factura.get(CenCuentasBancariasBean.C_CBO_CODIGO)+"-"+(String)factura.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL)+"-"+(String)factura.get(CenCuentasBancariasBean.C_DIGITOCONTROL)+"-"+(String)factura.get(CenCuentasBancariasBean.C_NUMEROCUENTA);
					resultado="********"+(String)factura.get("NUMEROCUENTA");
				}
			}
			if (etiqueta.equalsIgnoreCase("OBSERVACIONES")){
				resultado=(String)factura.get(FacFacturaBean.C_OBSERVACIONES);				
			}
			if (etiqueta.equalsIgnoreCase("ANTICIPADO")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_ANTICIPADO"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("COMPENSADO")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_COMPENSADO"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("POR_CAJA")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADOPORCAJA"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("POR_SOLOCAJA")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADOSOLOCAJA"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("POR_SOLOTARJETA")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADOSOLOTARJETA"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("POR_BANCO")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADOPORBANCO"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("TOTAL_FACTURA")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_FACTURA"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("TOTAL_PAGOS")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADO"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("PENDIENTE_PAGAR")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PENDIENTEPAGAR"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("IMPORTE_NETO")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_NETO"))+" \u20AC";
			}
			if (etiqueta.equalsIgnoreCase("IMPORTE_IVA")){
				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_IVA"))+" \u20AC";
			}
			resultado=UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos en la plantilla"); 
		}
		return resultado;
	}
	
*/	
	
	/** 
	 * Sustituye los campos específicos de la carta de EJG
	 * @param  datos - datos etiquetas 
	 * @param  etiqueta - etiqueta a sistituir
	 * @param  desglose - lineas de la factura
	 * @return  String - linea a imprimir  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String sustitucionEtiquetaGeneradorEtiquetas(EnvDestinatariosBean datos, String etiqueta, boolean espacio, UsrBean usr) throws ClsExceptions {

		String resultado="";
		
		try {

			if (etiqueta.equalsIgnoreCase("NOMBRE_APELLIDOS_ETIQUETA")){
				String nombreApellidos = ((datos.getNombre()==null)?"":(String)datos.getNombre())+" "+((datos.getApellidos1()==null)?"":(String)datos.getApellidos1())+" "+((datos.getApellidos2()==null)?"":(String)datos.getApellidos2()); 
				if (nombreApellidos==null){
					resultado="";	
				}else{
					resultado=nombreApellidos;
				}
			}
			if (etiqueta.equalsIgnoreCase("DIRECCION_ETIQUETA")){
				if (datos.getDomicilio()==null){
					resultado="";	
				}else{
					resultado=(String)datos.getDomicilio();
				}				
			}
			if (etiqueta.equalsIgnoreCase("CP_POBLACION_ETIQUETA")){
				if (((String)datos.getIdPais()).equals(ClsConstants.ID_PAIS_ESPANA) || ((String)datos.getIdPais()).equals("")) {
					if (datos.getCodigoPostal()==null){	
						if (datos.getIdPoblacion()==null){
							resultado="";
						}
						else{
							CenPoblacionesAdm admPob = new CenPoblacionesAdm(usr);
							String poblacion = admPob.getDescripcion(datos.getIdPoblacion());
							resultado=poblacion;
						}
					}else{
						if (datos.getIdPoblacion()==null){
							resultado=(String)datos.getCodigoPostal();
						}
						else{
							CenPoblacionesAdm admPob = new CenPoblacionesAdm(usr);
							String poblacion = admPob.getDescripcion(datos.getIdPoblacion());
							resultado=(String)datos.getCodigoPostal()+ " - " + poblacion;
						}
					}
				} else {
					if (datos.getCodigoPostal()==null){	
						if (datos.getPoblacionExtranjera() == null || datos.getPoblacionExtranjera().equals("") || datos.getPoblacionExtranjera().equalsIgnoreCase("null")){
							resultado="";
						}
						else{
							resultado=(String)datos.getPoblacionExtranjera();
						}
					}else{
						if (datos.getPoblacionExtranjera() == null || datos.getPoblacionExtranjera().equals("") || datos.getPoblacionExtranjera().equalsIgnoreCase("null")){
							resultado=(String)datos.getCodigoPostal();
						}
						else{
							resultado=(String)datos.getCodigoPostal()+ " - " + datos.getPoblacionExtranjera();
						}
					}
				}
			}
			
			if (etiqueta.equalsIgnoreCase("POBLACION_ETIQUETA")){
				if (((String)datos.getIdPais()).equals(ClsConstants.ID_PAIS_ESPANA) || ((String)datos.getIdPais()).equals("")) {
					if(datos.getIdPoblacion()==null){
						resultado="";
								
					}else{
						
						CenPoblacionesAdm poblacionesAdm = new CenPoblacionesAdm(usr);
						String descripcionPoblacion = poblacionesAdm.getDescripcion(datos.getIdPoblacion());
						resultado=descripcionPoblacion;
					
			
					
					}
				} else {
					if (datos.getPoblacionExtranjera() != null  &&  !datos.getPoblacionExtranjera().equalsIgnoreCase("null")){
						resultado=(String)datos.getPoblacionExtranjera();
					}
					else{
						resultado="";
					}
					
				}
				
		}
			if(etiqueta.equalsIgnoreCase("CP_ETIQUETA")){
				if(datos.getCodigoPostal()==null){
					
					resultado="";
					
					
				}else{
					
					resultado = datos.getCodigoPostal();
					
					
				}
				
				
			}
			
			
			if (etiqueta.equalsIgnoreCase("PROVINCIA_ETIQUETA")){
				if (((String)datos.getIdPais()).equals(ClsConstants.ID_PAIS_ESPANA) || ((String)datos.getIdPais()).equals("")) {
					if (datos.getIdProvincia()==null){
						resultado="";	
					}else{
						CenProvinciaAdm admProv = new CenProvinciaAdm(usr);
						String provincia = admProv.getDescripcion(datos.getIdProvincia());
						resultado=provincia;
					}									
				} else {
					if (datos.getIdPais()==null){
						resultado="";	
					}else{
						CenPaisAdm admPais = new CenPaisAdm(usr);
						String pais = admPais.getDescripcion(datos.getIdPais());
						resultado=pais;
					}									
				}
			}

			if (etiqueta.equalsIgnoreCase("TRATAMIENTO")){
				CenClienteAdm admCliente = new CenClienteAdm(usr);
				resultado = admCliente.getTratmiento (datos.getIdPersona(), datos.getIdInstitucion());
			}

			if (etiqueta.equalsIgnoreCase("TIPO_CLIENTE")){
				CenClienteAdm admCliente = new CenClienteAdm(usr);
				resultado = admCliente.getTipoClientePorSexo (datos.getIdPersona(), datos.getIdInstitucion());
			}
			if (etiqueta.equalsIgnoreCase("TIPO_CLIENTE_EJERCIENTE")){
				CenClienteAdm admCliente = new CenClienteAdm(usr);
				resultado = admCliente.getClienteEjercientePorSexo (datos.getIdPersona(), datos.getIdInstitucion());
			}
			if (etiqueta.equalsIgnoreCase("BLANCO")){
				if (espacio){
					resultado="-";					
				}
				else{
					resultado="";
				}
			}
			
			if (resultado == null){
				resultado = "";
			}
			
			resultado = UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos en la plantilla"); 
		}
		return resultado;
	}
		
	/** 
	 * Sustituye los campos específicos de la cabecera de la carta de morosidad referidos al cliente  
	 * @param  idPersona - identificador de la persona 
	 * @param  institucion - institucion a la que pertenece
	 * @param  etiqueta - etiqueta a sustituir
	 * @return  String - linea a imprimir  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String sustitucionEtiquetaDatosClienteCarta(String idPersona, String institucion, String etiqueta, UsrBean usr) throws ClsExceptions {

		String resultado="";
		
		try{			

			if (etiqueta.equalsIgnoreCase("NOMBRE_CLIENTE")){
				CenPersonaAdm personaAdm = new CenPersonaAdm(usr);
				resultado=personaAdm.obtenerNombreApellidos(idPersona);
			}
			if (etiqueta.equalsIgnoreCase("DIRECCION_CLIENTE")){
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(usr);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,institucion,"2");
				if (direccion.get(CenDireccionesBean.C_DOMICILIO)!=null){
					resultado=(String)direccion.get(CenDireccionesBean.C_DOMICILIO);
				}	
			}
			if (etiqueta.equalsIgnoreCase("CODIGO_POSTAL_CLIENTE")){
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(usr);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecificaYUbicacion(idPersona,institucion,"2");
				if (direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)!=null){
					resultado=(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL);
				}	
			}
			if (etiqueta.equalsIgnoreCase("POBLACION_PROVINCIA_CLIENTE")){
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(usr);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecificaYUbicacion(idPersona,institucion,"2");
				if ((direccion.get("POBLACION")!=null) &&(direccion.get("PROVINCIA")!=null)){
					resultado=(String)direccion.get("POBLACION")+", "+(String)direccion.get("PROVINCIA");
				}	
			}

			resultado=UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos en la plantilla"); 
		}
		return resultado;
	}
	
	/** 
	 * Sustituye la etiqueta pasada como parametro por su texto correspondiente en el idioma adecuado   
	 * @param  etiqueta - etiqueta a sustituir 
	 * @param  idioma - idioma a emplear
	 * @return  String - linea a imprimir  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String sustitucionEtiquetaIdioma(String etiqueta, String idioma) throws ClsExceptions {

		String resultado="";
		
		try{			
			if (etiqueta.equalsIgnoreCase("TEXTO_LISTA_MOROSOS")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaMorosos.listaMorosos");
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_NCOLEGIADO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaMorosos.nColegiado");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_NOMBRE_APELLIDOS")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaMorosos.nombreApellidos");
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_NFACTURAS_PENDIENTES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaMorosos.nFacturasPendientes");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_IMPORTE_TOTAL")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaMorosos.importeTotal");
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_TOTAL")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaMorosos.total");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_DESIGNACION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.designacion");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_BLOQUE_1")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.bloque1");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_BLOQUE_2")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.bloque2");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_BLOQUE_3")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.bloque3");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_FECHA_FACTURA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.fecha");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_NFACTURA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.nFactura");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_TOTAL_FACTURA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.totalFactura");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_PENDIENTE_PAGO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.pendiente");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_TOTAL")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.total");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARTA_DESPEDIDA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.despedida");
			}

			// Plantillas de solicitud de asistencia
			if (etiqueta.equalsIgnoreCase("TEXTO_NUM_PAGINA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.numHoja");
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_NUM_EXPEDIENTE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.numExpediente");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_TITULO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.cabecera");
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_DECLARANTE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.apellYNombDecl")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PREVIO_DECLARACION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.preDeclaracion");
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_DECLARACION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.declaro");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_POST_DECLARACION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.postDeclaracion1")+
						  UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.postDeclaracion2")+
						  UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.postDeclaracion3")+
						  UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.postDeclaracion4");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_PERSONALES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosPersonales");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_PROPIOS")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.propio");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_APELLIDOS_NOMBRE_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.apellNombPers")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PROFESION_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.profesion")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_ESTADO_CIVIL_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.estadoCivil")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DIRECCION_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.direccion")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PROVINCIA_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.provincia")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_TELEFONO_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.telefono")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_NIF_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.nif")+":";
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_FECHA_NACIMIENTO_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.nacimiento")+":";
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_REGIMEN_CONYUGAL_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.regimen")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_MUNICIPIO_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.municipio")+":";
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_CP_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.cp")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PAIS_PROPIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.pais")+":";
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.conyuge");
			}						
			if (etiqueta.equalsIgnoreCase("TEXTO_APELLIDOS_NOMBRE_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.apellNombPers")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PROFESION_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.profesion")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DIRECCION_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.direccion")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PROVINCIA_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.provincia")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_TELEFONO_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.telefono")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_NIF_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.nif")+":";
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_FECHA_NACIMIENTO_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.nacimiento")+":";
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_REGIMEN_CONYUGAL_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.regimen")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_MUNICIPIO_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.municipio")+":";
			} 
			if (etiqueta.equalsIgnoreCase("TEXTO_CP_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.cp")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PAIS_CONYUGE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.pais")+":";
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_FAMILIARES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosFamiliares");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_APELLIDOS_NOMBRE_FAMILIAR")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.apellNombPers");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PARENTESCO_FAMILIAR")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.parentesco");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_EDAD_FAMILIAR")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.edad");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_OBSERVACIONES_FAMILIAR")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.observaciones");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_ECONOMICOS")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosEconomicosFamilia");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_INGRESOS")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.ingresosAnuales");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_NOMBRE_PERCEPTOR_INGRESO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.nombrePerceptor");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_IMPORTE_BRUTO_INGRESO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.importeBruto");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CONCEPTO_INGRESO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.concepto");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_BIENES_MUEBLES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.propiedadBienes");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_TITULARES_BIENES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.nombreTitulares");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_TIPO_BIENES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.tipoBien");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_VALORACION_BIENES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.valoracion");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_BIENES_INMUEBLES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.propiedadInmuebles");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CARGAS_BIENES")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.cargas");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_BIENES_OTROS")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.propiedadOtros");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_OTROS")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.otrosDatos");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_DEFENSA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosDefensa");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CONDICION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.condicionesDeclaracion");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PARTE_DEMANDANTE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.parteDemandante");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PARTE_DEMANDADA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.parteDemandada");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_APELLIDOS_NOMBRE_ABOGADO_DESIGNADO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosAbogado")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_APELLIDOS_NOMBRE_PROCURADOR_DESIGNADO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosProcurador")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_JURISDICCION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.jurisdiccion")+":";
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_DIRECCION_NOTIFICACION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.direccionNotificacion")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_PARTIDO_JUDICIAL")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.partidoJudicial")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_ORGANO_JUDICIAL")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.organoJudicial")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_RESUMEN_PRETENSION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.resumen")+":";
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_IDENTIFICACION_PARTE_CONTRARIA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosPartesContrarias")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_PROCURADOR_CONTRARIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.apellYNombProc")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_ASISTENCIA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosAsistencia");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_APELLIDOS_NOMBRE_ABOGADO_DEFENSOR")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.apellYNombDefensor")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DELITO_IMPUTADO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.tipoDelito")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_CENTRO_DETENCION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.centroDetencion")+":";
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_NUM_ATESTADO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.numAtestado")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_FECHA_ASISTENCIA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.fechaAsistencia")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_FECHA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.fecha")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_COMPROMISO_ASISTENCIA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.compromiso");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_FIRMA_DECLARANTE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.firmaDeclarante");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_COLEGIO_EVALUADOR")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.datosColegio");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_NOMBRE_COLEGIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.identificacionColegio")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_RESULTADO_EVALUACION")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.resultadoExamen")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DOCUMENTACION_INCOMPLETA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.documentacionIncompleta");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DOCUMENTACION_COMPLETA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.documentacionCompleta");
			}			
			if (etiqueta.equalsIgnoreCase("TEXTO_DOCUMENTACION_FALTANTE")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.documentacionFalta");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_DATOS_ANEXO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.anexoSolicitud")+":";
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_INTRODUCCION_COMPROMISO_PARRAFO_1")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.parrafo1Anexo");
			}	
			if (etiqueta.equalsIgnoreCase("TEXTO_INTRODUCCION_COMPROMISO_PARRAFO_2")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.parrafo2Anexo");
			}		
			if (etiqueta.equalsIgnoreCase("TEXTO_INTRODUCCION_COMPROMISO_PARRAFO_3")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.parrafo3Anexo")+
				 		  UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.parrafo4Anexo");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_INTRODUCCION_COMPROMISO_PARRAFO_4")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.parrafo5Anexo");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_COMPROMISO_PARRAFO_1")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto1_1Anexo")+
				  		  UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto1_2Anexo");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_COMPROMISO_PARRAFO_2")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto2Anexo");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_COMPROMISO_PARRAFO_3")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto3_1Anexo")+
						  UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto3_2Anexo");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_COMPROMISO_PARRAFO_4")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto4_1Anexo")+
						  UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto4_2Anexo");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_COMPROMISO_PARRAFO_5")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto5Anexo");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_COMPROMISO_PARRAFO_6")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto6Anexo");
			}
			if (etiqueta.equalsIgnoreCase("TEXTO_COMPROMISO_PARRAFO_7")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.solicitudAsistencia.punto7Anexo");
			}
			
			resultado=UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos en la plantilla"); 
		}
		return resultado;
	}
	
	
	/** 
	 * Sustituye la etiqueta pasada como parametro por su texto correspondiente en el idioma adecuado   
	 * @param  etiqueta - etiqueta a sustituir 
	 * @param  idioma - idioma a emplear
	 * @return  String - linea a imprimir  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String sustitucionListaGuardiasIdioma(String etiqueta, String idioma) throws ClsExceptions {

		String resultado="";
		
		try{
			if (etiqueta.equalsIgnoreCase("TEXTO_TURNO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.turno");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_GUARDIA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.guardia");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_FECHA_INI")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.fechaInicio");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_FECHA_FIN")){
					resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.fechaFin");
			}else	
			if (etiqueta.equalsIgnoreCase("TEXTO_FECHA_INICIO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.desde");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_FECHA_FIN")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.hasta");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_LETRADO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.letrado");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_TFNO_OFICINA1")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.tfnoOficina1");
			}else 
			if (etiqueta.equalsIgnoreCase("TEXTO_TFNO_OFICINA2")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.tfnoOficina2");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_TFNO_RESIDENCIA")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.tfnoCasa");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_MOVIL")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.movil");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_FAX1")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.fax1");
			}else
				if (etiqueta.equalsIgnoreCase("TEXTO_FAX2")){
					resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.fax2");
			}else
			if (etiqueta.equalsIgnoreCase("TEXTO_NCOLEGIADO")){
				resultado=UtilidadesString.getMensajeIdioma(idioma,"informes.listaGuardias.nColegiado");
			}
			
			resultado=UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos en la plantilla"); 
		}
		return resultado;
	}
	
	/** 
	 * Sustituye los campos específicos de las lineas en la factura pasada como parametro en la plantilla  
	 * @param  lineaFactura - datos de la linea 
	 * @param  etiqueta - etiqueta detectada
	 * @return  String - linea a imprimir  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String sustitucionEtiquetaDesglose(Hashtable lineaFactura, String etiqueta) throws ClsExceptions {

		String resultado="";
		
		try{			

			if (etiqueta.equalsIgnoreCase("CANTIDAD_LINEA")){
				resultado="                                	"+(String)lineaFactura.get(FacLineaFacturaBean.C_CANTIDAD);
			}
			if (etiqueta.equalsIgnoreCase("DESCRIPCION_LINEA")){
				//if (((String)lineaFactura.get("DESCRIPCION")).length()<50){
					resultado="                                	"+(String)lineaFactura.get(FacLineaFacturaBean.C_DESCRIPCION);
				/*}
				else{
					resultado="                                	"+((String)lineaFactura.get(FacLineaFacturaBean.C_DESCRIPCION)).substring(0,50);
				}*/	
			}
			if (etiqueta.equalsIgnoreCase("PRECIO_LINEA")){
				resultado="                                	"+UtilidadesString.formatoImporte((String)lineaFactura.get(FacLineaFacturaBean.C_PRECIOUNITARIO));
			}
			if (etiqueta.equalsIgnoreCase("NETO_LINEA")){
				resultado="                                	"+UtilidadesString.formatoImporte((String)lineaFactura.get("IMPORTENETO"));
			}
			if (etiqueta.equalsIgnoreCase("IVA_LINEA")){
				resultado="                                	"+(String)lineaFactura.get(FacLineaFacturaBean.C_IVA);				
			}
			if (etiqueta.equalsIgnoreCase("IMPORTE_IVA_LINEA")){
				resultado="                                	"+UtilidadesString.formatoImporte((String)lineaFactura.get("IMPORTEIVA"));
			}
			if (etiqueta.equalsIgnoreCase("TOTAL_LINEA")){
				resultado="                                	"+UtilidadesString.formatoImporte((String)lineaFactura.get("IMPORTETOTAL"));
			}
			resultado=UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos de desglose en la plantilla"); 
		}
		return resultado;
	}
	
	
	
	
	
	
	/** 
	 * Sustituye los campos específicos de las lineas de una comunicacion a morosos  
	 * @param  linea - datos de la linea 
	 * @param  etiqueta - etiqueta detectada
	 * @return  String - linea a imprimir  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String sustitucionEtiquetaDesgloseComunicacionMorosos(Hashtable linea, String etiqueta) throws ClsExceptions {

		String resultado="";
		
		try{			

			if (etiqueta.equalsIgnoreCase("FECHA_FACTURA")){
				resultado="                                	"+GstDate.getFormatedDateShort("",(String)linea.get("FECHAEMISION"));
			}else if (etiqueta.equalsIgnoreCase("NFACTURA")){
				resultado="                                	"+(String)linea.get("NUMEROFACTURA");	
			}else if (etiqueta.equalsIgnoreCase("TOTAL_FACTURA")){
				resultado="                                	"+UtilidadesString.formatoImporte(UtilidadesNumero.redondea((String)linea.get("TOTAL"),2))+ClsConstants.CODIGO_EURO;
			}else if (etiqueta.equalsIgnoreCase("PENDIENTE_PAGO")){
				resultado="                                	"+UtilidadesString.formatoImporte(UtilidadesNumero.redondea((String)linea.get("DEUDA"),2))+ClsConstants.CODIGO_EURO;
			}
			resultado=UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos de desglose en la plantilla"); 
		}
		return resultado;
	}
	
	/** 
	 * Sustituye los campos específicos de las lineas de una lista de morosos  
	 * @param  linea - datos de la linea 
	 * @param  etiqueta - etiqueta detectada
	 * @return  String - linea a imprimir  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String sustitucionEtiquetaDesgloseMorosos(Hashtable linea, String etiqueta) throws ClsExceptions {

		String resultado="";
		
		try{			

			if (etiqueta.equalsIgnoreCase("NCOLEGIADO")){
				resultado="                                	"+(String)linea.get(CenColegiadoBean.C_NCOLEGIADO);
			}
			if (etiqueta.equalsIgnoreCase("NOMBRE_APELLIDOS")){
				if (((String)linea.get("NOMBRE")).length()<50){
					resultado="                                	"+(String)linea.get("NOMBRE");
				}
				else{
					resultado="                                	"+((String)linea.get("NOMBRE")).substring(0,50);
				}	
			}
//			if (etiqueta.equalsIgnoreCase("NFACTURAS_PENDIENTES")){
//				resultado="                                	"+(String)linea.get("COUNT("+FacFacturaBean.C_NUMEROFACTURA+")");
//			}
			if (etiqueta.equalsIgnoreCase("NFACTURAS_PENDIENTES")){
				resultado="                                	"+(String)linea.get(FacFacturaBean.C_NUMEROFACTURA);
			}
			if (etiqueta.equalsIgnoreCase("IMPORTE_TOTAL")){
				resultado="                                	"+UtilidadesString.formatoImporte(UtilidadesNumero.redondea((String)linea.get("DEUDA"),2))+ClsConstants.CODIGO_EURO;
			}
			resultado=UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos de desglose en la plantilla"); 
		}
		return resultado;
	}
	
	/** 
	 * Sustituye los campos específicos de las lineas de una lista de guardias  
	 * @param  linea - datos de la linea 
	 * @param  etiqueta - etiqueta detectada
	 * @param  fechaInicio - fecha inicio periodo 
	 * @param  fechaFin - fecha final periodo
	 * @return  String - linea a imprimir  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String sustitucionEtiquetaDesgloseGuardias(Hashtable linea, String etiqueta, String fechaInicio, String fechaFin) throws ClsExceptions {

		String resultado="";
		
		try{			
            
			if (etiqueta.equalsIgnoreCase("TURNO")){
				if (linea.get("TURNO")!=null){
					if (((String)linea.get("TURNO")).length()<20){
						resultado="                                	"+(String)linea.get("TURNO");
					}
					else{
						resultado="                                	"+(String)linea.get("TURNO");
					}					
				}
			}
			
			if (etiqueta.equalsIgnoreCase("GUARDIA")){
				if (linea.get("GUARDIA")!=null){
					if (((String)linea.get("GUARDIA")).length()<20){
						resultado="                                	"+(String)linea.get("GUARDIA");
					}
					else{
						//resultado="                                	"+((String)linea.get("GUARDIA")).substring(0,20);
						resultado="                                	"+(String)linea.get("GUARDIA");
					}					
				}
			}
			if (etiqueta.equalsIgnoreCase("FECHA_INICIO")){
				if (linea.get("FECHA_INICIO")!=null){
					String fecha=(String)linea.get("FECHA_INICIO");
					if (!fecha.equalsIgnoreCase("")){
						fecha=GstDate.getFormatedDateShort("",fecha);
						if (this.fechaMenorOIgual(fechaInicio,fecha)){
							resultado="                                	"+fecha;
						}
						else{
							resultado="                                	"+fechaInicio;
						}
					}					
				}	
			}
			if (etiqueta.equalsIgnoreCase("FECHA_FIN")){
				if (linea.get("FECHA_FIN")!=null){
					String fecha=(String)linea.get("FECHA_FIN");
					if (!fecha.equalsIgnoreCase("")){
						fecha=GstDate.getFormatedDateShort("",fecha);
						if (this.fechaMenorOIgual(fecha,fechaFin)){
							resultado="                                	"+fecha;
						}
						else{
							resultado="                                	"+fechaFin;
						}
					}					
				}
			}
			if (etiqueta.equalsIgnoreCase("LETRADO")){
				if (linea.get("LETRADO")!=null){
					resultado="                                	"+(String)linea.get("LETRADO");					
				}
			}
			if (etiqueta.equalsIgnoreCase("TFNO_OFICINA1")){
				if (linea.get("OFICINA1")!=null){
					resultado="                                	"+(String)linea.get("OFICINA1");					
				}
			}
			if (etiqueta.equalsIgnoreCase("TFNO_OFICINA2")){
				if (linea.get("OFICINA2")!=null){
					resultado="                                	"+(String)linea.get("OFICINA2");					
				}	
			}
			if (etiqueta.equalsIgnoreCase("TFNO_RESIDENCIA")){
				if (linea.get("RESIDENCIA")!=null){
					resultado="                                	"+(String)linea.get("RESIDENCIA");					
				}
			}
			if (etiqueta.equalsIgnoreCase("MOVIL")){
				if (linea.get("MOVIL")!=null){
					resultado="                                	"+(String)linea.get("MOVIL");					
				}
			}
			if (etiqueta.equalsIgnoreCase("FAX1")){
				if (linea.get("FAX1")!=null){
					resultado="                                	"+(String)linea.get("FAX1");					
				}
			}
			if (etiqueta.equalsIgnoreCase("FAX2")){
				if (linea.get("FAX2")!=null){
					resultado="                                	"+(String)linea.get("FAX2");					
				}
			}
			if (etiqueta.equalsIgnoreCase("NCOLEGIADO")){
				if (linea.get("NCOLEGIADO")!=null){
					resultado="                                	"+(String)linea.get("NCOLEGIADO");					
				}
			}
			resultado=UtilidadesString.formato_ISO_8859_1(resultado);
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de sustitucion de campos de desglose en la plantilla"); 
		}
		return resultado;
	}
	
	/** 
	 * Obtiene una pagina de un listado de morosos   
	 * @param  fichero - fichero a generar
	 * @param  institucion - datos generales de la institucion involucrada 
	 * @param  datos - lineas de la lista
	 * @param  rutaPlantilla - ruta de la plantilla especifica 
	 * @param  nombreFichero - plantilla de la pagina especifica de la factura
	 * @param  numeroLineas - numero de lineas a lpasmar en la factura
	 * @param  idioma - idioma a emplear
	 * @param  total - importe hasta el momento
	 * @return  double - suma importe total (-1 en caso de error)  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public double obtencionPaginaListaMorosos(File fichero, String institucion, Vector datos, String rutaPlantilla, String nombreFichero, int numeroLineas, String idioma, double total) throws ClsExceptions, SIGAException {

		File plantillaFactura=null; 
		File plantillaLinea=null;
		int contador=0;
		BufferedReader bufferLectura=null;
		BufferedReader bufferLecturaLinea=null;
		String linea;
		PrintWriter printer=null;
		Plantilla plantilla = new Plantilla(this.userbean);
		
		try{    		
			plantillaFactura = new File(rutaPlantilla+nombreFichero);
			
		    if (!plantillaFactura.exists()){
				//throw new ClsExceptions("facturacion.nuevoFichero.literal.errorAcceso");
		    	throw new SIGAException("messages.envios.error.noPlantilla");
	        }
		    else{
			    if (!plantillaFactura.canRead()){
					throw new ClsExceptions("Error de lectura del fichero: "+plantillaFactura.getAbsolutePath());
//					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    }
			    else{
			    	try {
			    		
			    		// Creacion de la plantilla especifica para la factura
						bufferLectura = new BufferedReader(new FileReader(plantillaFactura));												
						printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
						printer.flush();
						// Lectura plantilla
						linea=bufferLectura.readLine();
				    	while (linea != null){
				    		Vector detectadas=new Vector();
				    		detectadas=plantilla.deteccionEtiquetas(linea);
				    		if (!detectadas.isEmpty()){
				    			Enumeration listaEtiquetas=detectadas.elements();
				    			while (listaEtiquetas.hasMoreElements()){
				    				String etiqueta=(String)listaEtiquetas.nextElement();
				    				if (etiqueta.equalsIgnoreCase("DESGLOSE")){
				    					// Genero lineas de desglose
				    					Enumeration listaLineas=datos.elements();
				    					// Abro el fichero con las plantillas de las lineas
			    						plantillaLinea = new File(rutaPlantilla+"plantillaLineaMorosos.fo");
			    					    if (!plantillaLinea.exists()){
			    							//throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");
			    					    	throw new SIGAException("messages.envios.error.noPlantilla");
			    				        }
			    					    else{
			    						    if (!plantillaLinea.canRead()){
			    								throw new ClsExceptions("Error de lectura del fichero: "+plantillaLinea.getAbsolutePath());
//			    								throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    						    }
			    						    else{			    						    	
						    					int j=0;
						    					while ((listaLineas.hasMoreElements())&&(j<numeroLineas)){
						    						Hashtable lineaMorosos=((Row)listaLineas.nextElement()).getRow();
						    						// Aplicacion de la plantilla de lineas en las facturas
				    								bufferLecturaLinea = new BufferedReader(new FileReader(plantillaLinea));
				    								linea=bufferLecturaLinea.readLine();
				    						    	while (linea != null){
				    						    		Vector detectadasLinea=new Vector();				    						    		
				    						    		detectadasLinea=plantilla.deteccionEtiquetas(linea);
				    						    		if (!detectadasLinea.isEmpty()){
				    						    			Enumeration listaEtiquetasLinea=detectadasLinea.elements();
				    						    			while (listaEtiquetasLinea.hasMoreElements()){
				    						    				String etiquetaLinea=(String)listaEtiquetasLinea.nextElement();
				    					    					linea=sustitucionEtiquetaDesgloseMorosos(lineaMorosos,etiquetaLinea);
				    					    					if (etiquetaLinea.equalsIgnoreCase("IMPORTE_TOTAL")){
					    					    					total+=new Double(UtilidadesNumero.redondea((String)lineaMorosos.get("DEUDA"),2)).doubleValue();
				    					    					}
				    							    			printer.println(linea);
				    						    			}
				    						    		}
				    						    		else{
			    							    			printer.println(linea);
				    						    		}
					    								linea=bufferLecturaLinea.readLine();
				    						    	}
				    						    	bufferLecturaLinea.close();
						    					    j++;
						    					}
			    						    }
			    					    }
				    				}
				    				else{
				    					if (etiqueta.equalsIgnoreCase("TOTAL")){
							    			printer.println(UtilidadesString.formatoImporte(String.valueOf(UtilidadesNumero.redondea(total,2)))+" &#8364;");
				    					}
				    					else{
					    					linea=sustitucionEtiquetaIdioma(etiqueta,idioma);
							    			printer.println(linea);
				    					}
				    				}
				    			}
				    		}
				    		else{
				    			printer.println(linea);
				    		}
				    		contador++;
							linea=bufferLectura.readLine();
				    	}
				        printer.flush();
				        printer.close();
				    	bufferLectura.close();
				    	
					}catch(IOException _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}catch(Exception _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}
			    }				    	
		    }
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de obtención de paginas de facturacion."); 
		}
		
		return total;
	}
	
	/** 
	 * Genera comunicaciones para morosos en formato FOP   
	 * @param  fichero - fichero a generar
	 * @param  institucion - datos generales de la institucion involucrada 
	 * @param  datos - lineas de la lista
	 * @param  rutaPlantilla - ruta de la plantilla especifica 
	 * @param  nombreFichero - plantilla de la pagina especifica de la factura
	 * @param  idioma - idioma a emplear
	 * @param  idPersona - identificador de la persona
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean obtencionComunicacionMoroso(File fichero, String institucion, Vector datos, String rutaPlantilla, String idioma, String idPersona, UsrBean usr) throws ClsExceptions, SIGAException {
		
		
		
		File plantillaComunicacion=null; 
		//File plantillaLinea=null;
		int contador=0;
		int numeroLineas=15;
		BufferedReader bufferLectura=null;
		//BufferedReader bufferLecturaLinea=null;
		String linea;
		PrintWriter printer=null;
		boolean correcto=true;
		Plantilla plantilla = new Plantilla(this.userbean);
		double totalFacturas=0;
		double pendientePago=0;		
		
		try{    		
			plantillaComunicacion = new File(rutaPlantilla);
			
		    if (!plantillaComunicacion.exists()){
				//throw new ClsExceptions("facturacion.nuevoFichero.literal.errorAcceso");
		    	throw new SIGAException("messages.envios.error.noPlantilla");
	        }
		    else{
			    if (!plantillaComunicacion.canRead()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    }
			    else{
			    	try {
			    		
			    		// Creacion de la plantilla especifica para la factura
						bufferLectura = new BufferedReader(new FileReader(plantillaComunicacion));												
						printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
						printer.flush();
						// Lectura plantilla
						linea=bufferLectura.readLine();
				    	while (linea != null){
				    		Vector detectadas=new Vector();
				    		detectadas=plantilla.deteccionEtiquetas(linea);
				    		if (!detectadas.isEmpty()){
				    			Enumeration listaEtiquetas=detectadas.elements();
				    			while (listaEtiquetas.hasMoreElements()){
				    				String etiqueta=(String)listaEtiquetas.nextElement();
				    				if (etiqueta.equalsIgnoreCase("DESGLOSE")){
				    					// Genero lineas de desglose
				    					Enumeration listaLineas=datos.elements();
				    					// Abro el fichero con las plantillas de las lineas
			    						/*plantillaLinea = new File(rutaPlantilla+"plantillaLineaCartaMorosos.fo");
			    					    if (!plantillaLinea.exists()){
			    							//throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");
			    					    	throw new SIGAException("messages.envios.error.noPlantilla");
			    				        }
			    					    else{*/
			    						   /* if (!plantillaLinea.canRead()){
			    								throw new ClsExceptions("Error de lectura del fichero: "+plantillaLinea.getAbsolutePath());
//			    								throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    						    }
			    						    else{	*/		    						    	
						    					int j=0;
						    					while ((listaLineas.hasMoreElements())&&(j<numeroLineas)){
						    						Hashtable lineaMorosos=((Row)listaLineas.nextElement()).getRow();
						    						// Aplicacion de la plantilla de lineas en las facturas
				    								//bufferLecturaLinea = new BufferedReader(new FileReader(plantillaLinea));
				    								//linea=bufferLecturaLinea.readLine();
						    						linea=bufferLectura.readLine();
				    						    	while (linea != null){
				    						    		Vector detectadasLinea=new Vector();				    						    		
				    						    		detectadasLinea=plantilla.deteccionEtiquetas(linea);
				    						    		if (!detectadasLinea.isEmpty()){
				    						    			Enumeration listaEtiquetasLinea=detectadasLinea.elements();
				    						    			while (listaEtiquetasLinea.hasMoreElements()){
				    						    				String etiquetaLinea=(String)listaEtiquetasLinea.nextElement();
				    					    					linea=sustitucionEtiquetaDesgloseComunicacionMorosos(lineaMorosos,etiquetaLinea);
				    					    					if (etiquetaLinea.equalsIgnoreCase("TOTAL_FACTURA")){
					    					    					totalFacturas+=new Double(UtilidadesNumero.redondea((String)lineaMorosos.get("TOTAL"),2)).doubleValue();
				    					    					}
				    					    					if (etiquetaLinea.equalsIgnoreCase("PENDIENTE_PAGO")){
					    					    					pendientePago+=new Double(UtilidadesNumero.redondea((String)lineaMorosos.get("DEUDA"),2)).doubleValue();
				    					    					}
				    							    			printer.println(linea);
				    						    			}
				    						    		}
				    						    		else{
			    							    			printer.println(linea);
				    						    		}
					    								//linea=bufferLecturaLinea.readLine();
				    						    		linea=bufferLectura.readLine();
				    						    	}
				    						    	//bufferLecturaLinea.close();
						    					    j++;
						    					}
			    						    //}
			    					    //}				    				
			    					}
			    					if (etiqueta.equalsIgnoreCase("GLOBAL_FACTURA")){
						    			printer.println(UtilidadesString.formato_ISO_8859_1(UtilidadesString.formatoImporte(String.valueOf(UtilidadesNumero.redondea(totalFacturas,2))))+" &#8364;");
			    					}
			    					if (etiqueta.equalsIgnoreCase("GLOBAL_PENDIENTE")){
						    			printer.println(UtilidadesString.formato_ISO_8859_1(UtilidadesString.formatoImporte(String.valueOf(UtilidadesNumero.redondea(pendientePago,2))))+" &#8364;");
			    					}
			    					if(etiqueta.indexOf("TEXTO")!=-1){
					    				linea=sustitucionEtiquetaIdioma(etiqueta,idioma);
						    			printer.println(linea);
			    					}
			    					if (etiqueta.indexOf("CLIENTE")!=-1){
				    					linea=sustitucionEtiquetaDatosClienteCarta(idPersona,institucion,etiqueta, usr);
			    						printer.println(linea);
			    					}
			    					if (etiqueta.equalsIgnoreCase("LUGAR_FECHA")){
			    						CenDireccionesAdm direccionAdm = new CenDireccionesAdm(usr);
			    						Hashtable direccion=direccionAdm.getEntradaDireccionEspecificaYUbicacion(idPersona,institucion,"2");
			    						if (direccion.get("POBLACION")!=null){
			    							linea=(String)direccion.get("POBLACION")+", "+UtilidadesBDAdm.getFechaBD("");
			    						}
			    						else{
			    							linea=UtilidadesBDAdm.getFechaBD("");
			    						}
			    						printer.println(UtilidadesString.formato_ISO_8859_1(linea));
			    					}
				    			}
				    		}
				    		else{
				    			printer.println(linea);
				    		}
				    		contador++;
							linea=bufferLectura.readLine();
				    	}
				        printer.flush();
				        printer.close();
				    	bufferLectura.close();
				    	
					}catch(IOException _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}catch(Exception _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}
			    }				    	
		    }
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de obtención de paginas de facturacion."); 
		}
		
		return correcto;
	}

	
	
	/** 
	 * Genera la carta a enviar a los interesados de EJG
	 * @param  institucion - identificador de la institucion involucrada
	 * @param  fichero - fichero FOP a generar
	 * @param  datos - datos informe
	 * @param  nombre - nombre institucion
	 * @param  inicioPerido - inicio de periodo guardias a mostrar
	 * @param  finPeriodo - fin de periodo guardias a mostrar
	 * @param  numeroLineas - numero de lineas que caben por hoja
	 * @param  rutaPlantilla - ruta de la plantilla especifica 
	 * @param  idioma - idioma del usuario
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean obtencionPaginaInformeListaGuardias(String institucion, File fichero, Vector datos,  
									 String nombre, String inicioPeriodo, 
									 String finPeriodo, int numeroLineas,
									 String rutaPlantilla, String idioma, String nombreLista, String lugar, String observaciones) throws ClsExceptions, SIGAException {

		File plantillaSolicitud=null; 
		
		int contador=0;
		BufferedReader bufferLectura=null;
		BufferedReader bufferLecturaLinea=null;
		String linea;
		PrintWriter printer=null;
		boolean correcto=true;
		Plantilla plantilla = new Plantilla(this.userbean);
		String etiquetaCabecera="0";
		
		try{    		
			plantillaSolicitud = new File(rutaPlantilla+"plantillaListaGuardias.fo");
			
		    if (!plantillaSolicitud.exists()){
				//throw new ClsExceptions("facturacion.nuevoFichero.literal.errorAcceso");
		    	throw new SIGAException("messages.envios.error.noPlantilla");
	        }
		    else{
			    if (!plantillaSolicitud.canRead()){
					throw new ClsExceptions("Error de lectura del fichero: "+plantillaSolicitud.getAbsolutePath());
//					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    }
			    else{
			    	try {
			    		
			    		// Creacion de la plantilla especifica para la factura
						bufferLectura = new BufferedReader(new FileReader(plantillaSolicitud));												
						printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
						printer.flush();

						// Lectura plantilla
						linea=bufferLectura.readLine();
				    	while (linea != null){
				    		Vector detectadas=new Vector();
				    		detectadas=plantilla.deteccionEtiquetas(linea);
				    		if (!detectadas.isEmpty()){
				    			Enumeration listaEtiquetas=detectadas.elements();
				    			while (listaEtiquetas.hasMoreElements()){
				    				String etiqueta=(String)listaEtiquetas.nextElement();
				    				etiquetaCabecera="0";
			    					if(etiqueta.indexOf("TEXTO")!=-1){
					    				linea=sustitucionListaGuardiasIdioma(etiqueta,idioma);
						    			printer.println(linea);
			    					}
			    					else{
					    				if (etiqueta.equalsIgnoreCase("DESGLOSE_GUARDIAS")){
					    					
					    					// Genero lineas de desglose
					    					Enumeration listaLineas=datos.elements();
					    					// Abro el fichero con las plantillas de las lineas
				    						File plantillaLinea = new File(rutaPlantilla+"plantillaLineaListaGuardias.fo");
				    					    if (!plantillaLinea.exists()){
				    							//throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");
				    					    	throw new SIGAException("messages.envios.error.noPlantilla");
				    				        }
				    					    else{
				    						    if (!plantillaLinea.canRead()){
				    								throw new ClsExceptions("Error de lectura del fichero: "+plantillaLinea.getAbsolutePath());
//				    								throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
				    						    }
				    						    else{			    						    	
							    					int j=0;
							    					while ((listaLineas.hasMoreElements())&&(j<numeroLineas)){
							    						Hashtable lineaGuardia=(Hashtable)listaLineas.nextElement();
							    						// Aplicacion de la plantilla de lineas en las facturas
					    								bufferLecturaLinea = new BufferedReader(new FileReader(plantillaLinea));
					    								linea=bufferLecturaLinea.readLine();
					    						    	while (linea != null){
					    						    		Vector detectadasLinea=new Vector();				    						    		
					    						    		detectadasLinea=plantilla.deteccionEtiquetas(linea);
					    						    		if (!detectadasLinea.isEmpty()){
					    						    			Enumeration listaEtiquetasLinea=detectadasLinea.elements();
					    						    			while (listaEtiquetasLinea.hasMoreElements()){
					    						    				
					    						    				
					    						    				String etiquetaLinea=(String)listaEtiquetasLinea.nextElement();
					    					    					linea=sustitucionEtiquetaDesgloseGuardias(lineaGuardia,etiquetaLinea,inicioPeriodo,finPeriodo);
					    							    			printer.println(linea);
					    						    			}
					    						    		}
					    						    		else{
				    							    			printer.println(linea);
					    						    		}
						    								linea=bufferLecturaLinea.readLine();
					    						    	}
					    						    	bufferLecturaLinea.close();
							    					    j++;
							    					}
				    						    }
				    					    }
					    				}
					    				else{
					    					etiquetaCabecera="1";
					    					// Se trata de la etiqueta NOMBRE_COLEGIO_PERIODO, por tanto...
					    					
					    					/*if(etiqueta.indexOf("NOMBRE_COLEGIO_PERIODO")!=-1){
							    			 linea=nombre+"  "+inicioPeriodo+" - "+finPeriodo;
							    			 linea=UtilidadesString.formato_ISO_8859_1(linea);
					    					}
					    					if(etiqueta.indexOf("NOMBRE_LISTA_GUARDIA")!=-1){
								    			 linea=nombreLista;
								    			 linea=UtilidadesString.formato_ISO_8859_1(linea);
						    					}
					    					if(etiqueta.indexOf("LUGAR")!=-1){
								    			 linea=lugar;
								    			 linea=UtilidadesString.formato_ISO_8859_1(linea);
						    					}
					    					if(etiqueta.indexOf("OBSERVACIONES")!=-1){
								    			 linea=observaciones;
								    			 linea=UtilidadesString.formato_ISO_8859_1(linea);
						    					}*/
					    						if (etiqueta.indexOf("NOMBRE_COLEGIO_PERIODO")!=-1){
					    							linea=linea.replaceAll("%%NOMBRE_COLEGIO_PERIODO%%",nombre+"  "+inicioPeriodo+" - "+finPeriodo);
					    						}
					    						if (etiqueta.indexOf("NOMBRE_LISTA_GUARDIA")!=-1){
					    							linea=linea.replaceAll("%%NOMBRE_LISTA_GUARDIA%%",nombreLista);
					    						}
					    						if (etiqueta.toString().indexOf("LUGAR")!=-1){
					    							linea=linea.replaceAll("%%LUGAR%%",lugar);
					    						}
					    						if (etiqueta.toString().indexOf("OBSERVACIONES")!=-1){
					    							linea=linea.replaceAll("%%OBSERVACIONES%%",observaciones);
					    						}
					    					
					    					
					    					
					    				}	
					    				
			    					}
			    					
				    			}
				    			if (linea!=null && (etiquetaCabecera.equals("1"))){
				    			 linea=UtilidadesString.formato_ISO_8859_1(linea);
				    			 printer.println(linea);	
				    			}
		    					
				    		}
				    		else{
				    			printer.println(linea);
				    		}
				    		contador++;
							linea=bufferLectura.readLine();
				    	}
				        printer.flush();
				        printer.close();
				    	bufferLectura.close();
				    	
					}catch(IOException _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}catch(Exception _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}
			    }				    	
		    }
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de obtención de cartas de asistencia."); 
		}
		
		return correcto;
	}
	
	
	/** 
	 * Genera la cabecera de un documento FOP a partir delos parametros pasados
	 * @param  fichero - fichero a generar
	 * @param  nombre - nombre documento
	 * @param  margenIzdo - margen izquierdo 
	 * @param  margenDcho - margen derecho
	 * @param  margenSup - margen superior
	 * @param  margenInf - margen inferior 
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean insercionCabeceraSimpleDocumentoFOP(File fichero, String nombre, String margenIzdo, String margenDcho, String margenSup, String margenInf) throws ClsExceptions, SIGAException { 
		
		PrintWriter printer=null;
		boolean correcto=true;
		
		try{    		
	    	try {
	    														
				printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
				printer.flush();

				// escriyura cabecera
				printer.println("<!-- miguel.villegas 17/05/05 Plantilla para crear una carta a los interesados a asistencia juridica-->");
				printer.println("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");
				printer.println("	<!-- Geometria de las paginas -->");
				printer.println("	<fo:layout-master-set>");
				printer.println("		<fo:simple-page-master margin-right=\""+margenDcho+"cm\" margin-left=\""+margenIzdo+"cm\" margin-bottom=\""+margenInf+"cm\" margin-top=\""+margenSup+"cm\"");
				printer.println("							   page-width=\"21cm\" page-height=\"29.7cm\" master-name=\"portada\">");
				printer.println("			<fo:region-body margin-top=\"3cm\"> </fo:region-body>");
				printer.println("           <fo:region-before extent=\"3cm\"/>");
				printer.println("		</fo:simple-page-master>");
				printer.println("		<fo:simple-page-master margin-right=\""+margenDcho+"cm\" margin-left=\""+margenIzdo+"cm\" margin-bottom=\""+margenInf+"cm\" margin-top=\""+margenSup+"cm\"");
				printer.println("							   page-width=\"21cm\" page-height=\"29.7cm\" master-name=\"pagina\">");
				printer.println("			<fo:region-body margin-top=\"3cm\"> </fo:region-body>");
				printer.println("           <fo:region-before extent=\"3cm\"/>");
				printer.println("		</fo:simple-page-master>");
				
				printer.println("		<fo:page-sequence-master master-name=\""+nombre+"\">");
				printer.println("			<fo:repeatable-page-master-alternatives>");
				printer.println("				<fo:conditional-page-master-reference master-reference=\"portada\" page-position=\"first\" />");
				printer.println("				<fo:conditional-page-master-reference master-reference=\"pagina\" page-position=\"rest\" />");
				printer.println("				<fo:conditional-page-master-reference master-reference=\"pagina\" />");
				printer.println("			</fo:repeatable-page-master-alternatives>");
				printer.println("		</fo:page-sequence-master>");
				printer.println("	</fo:layout-master-set>");
				printer.println("	<!-- Estructura de la solicitud -->");
				printer.println("	<fo:page-sequence master-reference=\""+nombre+"\">");
				printer.println("		<fo:flow flow-name=\"xsl-region-body\">");

				printer.flush();
		        printer.close();
		    	
			}catch(IOException _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}catch(Exception _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}				    	
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de insercionde cabecera documento FOP."); 
		}
		
		return correcto;
	}
	
	
	
	/** 
	 * Genera la cabecera del documento de etiquetas de envios ordinarios a partir delos parametros pasados
	 * @param  fichero - fichero a generar
	 * @param  nombre - nombre documento
	 * @param  margenIzdo - margen izquierdo 
	 * @param  margenDcho - margen derecho
	 * @param  margenSup - margen superior
	 * @param  margenInf - margen inferior 
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean insercionCabeceraSimpleEtiquetasFOP(File fichero, String nombre, String margenIzdo, String margenDcho, String margenSup, String margenInf) throws ClsExceptions, SIGAException { 
		
		PrintWriter printer=null;
		boolean correcto=true;
		
		try{    		
	    	try {
	    														
				printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
				printer.flush();

				// escriyura cabecera
				printer.println("<!-- Plantilla para generar Etiquetas de envios ordinarios -->");
				printer.println("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");
				printer.println("	<!-- Geometria de las paginas -->");
				printer.println("	<fo:layout-master-set>");
				printer.println("		<fo:simple-page-master margin-right=\""+margenDcho+"cm\" margin-left=\""+margenIzdo+"cm\" margin-bottom=\""+margenInf+"cm\" margin-top=\""+margenSup+"cm\"");
				printer.println("							   page-width=\"21cm\" page-height=\"29.7cm\" master-name=\"portada\">");
				printer.println("			<fo:region-body margin-top=\"0cm\"> </fo:region-body>");
				printer.println("           <fo:region-before extent=\"0cm\"/>");
				printer.println("		</fo:simple-page-master>");
				printer.println("		<fo:simple-page-master margin-right=\""+margenDcho+"cm\" margin-left=\""+margenIzdo+"cm\" margin-bottom=\""+margenInf+"cm\" margin-top=\""+margenSup+"cm\"");
				printer.println("							   page-width=\"21cm\" page-height=\"29.7cm\" master-name=\"pagina\">");
				printer.println("			<fo:region-body margin-top=\"0cm\"> </fo:region-body>");
				printer.println("           <fo:region-before extent=\"0cm\"/>");
				printer.println("		</fo:simple-page-master>");
				
				printer.println("		<fo:page-sequence-master master-name=\""+nombre+"\">");
				printer.println("			<fo:repeatable-page-master-alternatives>");
				printer.println("				<fo:conditional-page-master-reference master-reference=\"portada\" page-position=\"first\" />");
				printer.println("				<fo:conditional-page-master-reference master-reference=\"pagina\" page-position=\"rest\" />");
				printer.println("				<fo:conditional-page-master-reference master-reference=\"pagina\" />");
				printer.println("			</fo:repeatable-page-master-alternatives>");
				printer.println("		</fo:page-sequence-master>");
				printer.println("	</fo:layout-master-set>");
				printer.println("	<!-- Estructura de la solicitud -->");
				printer.println("	<fo:page-sequence master-reference=\""+nombre+"\">");
				printer.println("		<fo:flow flow-name=\"xsl-region-body\">");

				printer.flush();
		        printer.close();
		    	
			}catch(IOException _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}catch(Exception _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}				    	
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de insercionde cabecera documento FOP."); 
		}
		
		return correcto;
	}
	
	
	/** 
	 * Genera la cabecera de la estructura de un bloque a partir de los parametros pasados
	 * @param  fichero - fichero a manejar
	 * @param  altura - altura etiqueta 
	 * @param  anchura - anchura etiqueta
	 * @param  fila - coordenadas fila 
	 * @param  columna - coordenadas columna 
	 * @param  tabFila - tabulador filas
	 * @param  tabColumna - tabulador columnas
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean insercionCabeceraBloqueFOP(File fichero, float altura, float anchura, int fila, int columna, 
											  float distanciaEntreFil, float distanciaEntreCol) throws ClsExceptions, SIGAException { 
		
		PrintWriter printer=null;
		boolean correcto=true;
		
		try{    		
	    	try {
				printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
				printer.flush();

				// Calculo posicion 
//				float coordenadaX=(new Float(columna).floatValue()*(new Float(anchura).floatValue()+new Float(tabColumna).floatValue()));
//				float coordenadaY=(new Float(fila).floatValue()*(new Float(altura).floatValue()+new Float(tabFila).floatValue()));

				float coordenadaX = columna * (anchura + distanciaEntreCol);
				float coordenadaY = fila    * (altura  + distanciaEntreFil);

				// escritura cabecera
				printer.println("<fo:block-container height=\""+altura+"cm\" width=\""+anchura+"cm\" top=\""+coordenadaY+"cm\" left=\""+coordenadaX+"cm\" position=\"absolute\">");
				printer.flush();
		        printer.close();
			}
	    	catch(IOException _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}
	    	catch(Exception _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}				    	
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de insercion de cabecera de bloque FOP."); 
		}
		
		return correcto;
	}
	
	/** 
	 * Genera el pie de la estructura de un bloque
	 * @param  fichero - fichero a manejar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean insercionPieBloqueFOP(File fichero) throws ClsExceptions, SIGAException { 
		
		PrintWriter printer=null;
		boolean correcto=true;
		
		try{    		
	    	try {
	    														
				printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
				printer.flush();
				
				// escritura cabecera
				printer.println("</fo:block-container>");

				printer.flush();
		        printer.close();
		    	
			}catch(IOException _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}catch(Exception _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}				    	
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de insercion de cabecera de bloque FOP."); 
		}
		
		return correcto;
	}
	
	/** 
	 * Genera la carta a enviar a los interesados de EJG
	 * @param  fichero - fichero FOP a generar
	 * @param  rutaPlantilla - ruta donde se encuantra la plantilla a emplear
	 * @param  datosEtiqueta - información a plasmar en la etiqueta
	 * @param  institucion - identificador de la institucion
	 * @param  envio - identificador del envio 
	 * @param  idioma - idioma del usuario
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean obtencionEtiqueta(File fichero, String rutaPlantilla, EnvDestinatariosBean datosEtiqueta, String institucion, String envio, UsrBean usr) throws ClsExceptions, SIGAException {

		File plantillaSolicitud=null; 
		
		int contador=0;
		BufferedReader bufferLectura=null;
		String linea;
		PrintWriter printer=null;
		boolean correcto=true;
		boolean reservaEspacio=false;
		Plantilla plantilla = new Plantilla(this.userbean);
		
		try{    		
			plantillaSolicitud = new File(rutaPlantilla);
			
		    if (!plantillaSolicitud.exists()){
				//throw new ClsExceptions("facturacion.nuevoFichero.literal.errorAcceso");
		    	throw new SIGAException("messages.envios.error.noPlantilla");
	        }
		    else{
			    if (!plantillaSolicitud.canRead()){
					throw new ClsExceptions("Error de lectura del fichero: "+plantillaSolicitud.getAbsolutePath());
//					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    }
			    else{
			    	try {
			    		
			    		// Creacion de la plantilla especifica para la factura
						bufferLectura = new BufferedReader(new FileReader(plantillaSolicitud));												
						printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
						printer.flush();

						// Lectura plantilla
						linea=bufferLectura.readLine();
				    	while (linea != null){
				    		Vector detectadas=new Vector();
				    		detectadas=plantilla.deteccionEtiquetas(linea);
				    		if (!detectadas.isEmpty()){
				    			Enumeration listaEtiquetas=detectadas.elements();
				    			while (listaEtiquetas.hasMoreElements()){
				    				String etiqueta=(String)listaEtiquetas.nextElement();				    								    				
				    				linea = sustitucionEtiquetaGeneradorEtiquetas(datosEtiqueta,etiqueta,reservaEspacio, usr);
				    				if (linea.equalsIgnoreCase("")){
				    					reservaEspacio=true;
				    				}
				    				else{
				    					reservaEspacio=false;
				    				}
						    		printer.println(linea);			    						
				    			}
				    		}
				    		else{
				    			printer.println(linea);
				    		}
				    		contador++;
							linea=bufferLectura.readLine();
				    	}
				        printer.flush();
				        printer.close();
				    	bufferLectura.close();
				    	
					}catch(IOException _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}catch(Exception _ex) {
						bufferLectura.close();
						printer.flush();
						printer.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
					}
			    }				    	
		    }
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de obtención de etiquetas."); 
		}
		
		return correcto;
	}
	
	/** 
	 * Genera el pìe de documento FOP
	 * @param  fichero - fichero a generar 
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean insercionPieSimpleDocumentoFOP(File fichero) throws ClsExceptions, SIGAException { 
		
		PrintWriter printer=null;
		boolean correcto=true;
		
		try{    		
	    	try {
	    														
				printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
				printer.flush();

				// Escritura pie
			
				printer.println("		</fo:flow>");
				printer.println("	</fo:page-sequence>");
				printer.println("</fo:root>");

				printer.flush();
		        printer.close();
		    	
			}catch(IOException _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}catch(Exception _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}				    	
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de insercion de pìe documento FOP."); 
		}
				
		return correcto;
	}

		
	/** 
	 * Incluye nueva hoja
	 * @param  fichero - fichero a generar 
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	public boolean insercionSimplePaginaNuevaDocumentoFOP(File fichero) throws ClsExceptions, SIGAException { 
		
		PrintWriter printer=null;
		boolean correcto=true;
		
		try{    		
	    	try {
	    														
				printer = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
				printer.flush();

				// Escritura pie
				printer.println("	    <fo:block font-size=\"16pt\" font-family=\"sans-serif\" space-after.optimum=\"15pt\" text-align=\"center\" break-before=\"page\"/>");

				printer.flush();
		        printer.close();
		    	
			}catch(IOException _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}catch(Exception _ex) {
				printer.flush();
				printer.close();
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",_ex);						
			}				    	
		} catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) { 
			throw new ClsExceptions(e,"Ha fallado el proceso de insercion de una pagina nueva documento FOP."); 
		}
				
		return correcto;
	}
	
	/** 
	 * Averigua si la una fecha es menor o igual que otra
	 * @param  fecha1 - fecha a priori menor o igual 
	 * @param  fecha2 - fecha a comparar
	 * @return  boolean - resultado de la operacion  
	 * @exception  ClsException  En cualquier caso de error
	 */
	private boolean fechaMenorOIgual(String fecha1, String fecha2) throws ClsExceptions {
		boolean correcto = false;
		
		//Recupero del String fechaInicial con formato dd/mm/yyyy la fecha como Date
		String jsdf = "dd/MM/yyyy";//Java Short Date Format
		SimpleDateFormat formateo = new SimpleDateFormat(jsdf);
		Date date1 = new Date();
		Date date2 = new Date();
		
		try {
			date1 = formateo.parse(fecha1);
			date2 = formateo.parse(fecha2);

			if (date2.compareTo(date1)==0 || date2.after(date1)) correcto = true;
			else correcto = false;
		} catch (Exception e){
			throw new ClsExceptions(e,"Error al comparar fechas");
		}		
		return correcto;
	}

	public static synchronized  void doZip(String rutaServidorDescargasZip, String nombreFicheroPDF, ArrayList ficherosPDF) throws ClsExceptions	{
	    doZip(rutaServidorDescargasZip,nombreFicheroPDF,ficherosPDF,true);
	}
	
	public static synchronized  void doZip(String rutaServidorDescargasZip, String nombreFicheroPDF, ArrayList ficherosPDF, boolean borrarFicheros) throws ClsExceptions	{
		// Generar Zip
		File ficZip=null;
		byte[] buffer = new byte[8192];
		int leidos;
		
		ZipOutputStream outTemp = null;
		try {
			if ((ficherosPDF!=null) && (ficherosPDF.size()>0)) {
				
				ficZip = new File(rutaServidorDescargasZip +  nombreFicheroPDF + ".zip");
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (int i=0; i<ficherosPDF.size(); i++)
				{
					File auxFile = (File)ficherosPDF.get(i);
					if (auxFile.exists()) {
						ZipEntry ze = new ZipEntry(auxFile.getName());
						outTemp.putNextEntry(ze);
						FileInputStream fis=new FileInputStream(auxFile);
						
						buffer = new byte[8192];
						
						while ((leidos = fis.read(buffer, 0, buffer.length)) > 0)
						{
							outTemp.write(buffer, 0, leidos);
						}
						
						fis.close();
						outTemp.closeEntry();
					}
				}
				
				// borro los ficheros pdf
				// RGG 05/05/2009 Cambio para que no se borren los ficheros generados  
				if (borrarFicheros) {
					for (int i=0; i<ficherosPDF.size(); i++)
					{
						File auxFile = (File)ficherosPDF.get(i);
						if (auxFile.exists()) {
							auxFile.delete();
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		} catch (IOException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		}
		finally {
			try {
				outTemp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static String obtenerPathNormalizado(String path){
		String newPath=path;
		if(path!=null){
			newPath=path.replaceAll(ClsConstants.FILE_SEP_TO_REPLACE,ClsConstants.FILE_SEP_REP);
		}
		return newPath;
	}
	
	  /**
	   * Borra el directorio y todo su contenido que pasemos como parámetro
	   * @param dir Ruta completa
	   * @return True si el proceso ha finalizado correctamente
	   */
	  public static boolean borrarDirectorio(File dir) {
	    if (dir.isDirectory()) {
	      String[] children = dir.list();
	      for (int i=0; i<children.length; i++) {
	        boolean success = borrarDirectorio(new File(dir, children[i]));
	        if (!success) {
	          return false;
	        }
	      }
	    }
	    return dir.delete();
	  }
	
}


