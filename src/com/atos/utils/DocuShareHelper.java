/**
 * 
 */
package com.atos.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.vos.DocuShareObjectVO;
import com.siga.general.SIGAException;
import com.xerox.docushare.DSClass;
import com.xerox.docushare.DSContentElement;
import com.xerox.docushare.DSException;
import com.xerox.docushare.DSFactory;
import com.xerox.docushare.DSHandle;
import com.xerox.docushare.DSInvalidLicenseException;
import com.xerox.docushare.DSObject;
import com.xerox.docushare.DSObjectIterator;
import com.xerox.docushare.DSSelectSet;
import com.xerox.docushare.DSServer;
import com.xerox.docushare.DSSession;
import com.xerox.docushare.FileContentElement;
import com.xerox.docushare.db.DbNoSuchObjectException;
import com.xerox.docushare.object.DSCollection;
import com.xerox.docushare.object.DSDocument;
import com.xerox.docushare.property.DSLinkDesc;
import com.xerox.docushare.property.DSProperties;

/**
 * @author angelcpe
 *
 */
public class DocuShareHelper {
	
	private static boolean MODO_DEBUG_LOCAL = false;

	private static String idModulo = "GEN";

	private static String DOCUSHARE_HOST = "DOCUSHARE_HOST";
	private static String DOCUSHARE_PORT = "DOCUSHARE_PORT";
	private static String DOCUSHARE_DOMAIN = "DOCUSHARE_DOMAIN";

	private static String DOCUSHARE_USER = "DOCUSHARE_USER";
	private static String DOCUSHARE_PASSWORD = "DOCUSHARE_PASSWORD";
	
	private static String PATH_DOCUSHARE_EXPEDIENTES = "PATH_DOCUSHARE_EXPEDIENTES";
	private static String PATH_DOCUSHARE_EJG = "PATH_DOCUSHARE_EJG";
	private static String PATH_DOCUSHARE_CENSO = "PATH_DOCUSHARE_CENSO";
	
	private static String ID_DOCUSHARE_EXPEDIENTES = "ID_DOCUSHARE_EXPEDIENTES";
	private static String ID_DOCUSHARE_EJG = "ID_DOCUSHARE_EJG";
	private static String ID_DOCUSHARE_CENSO = "ID_DOCUSHARE_CENSO";
		
	private static String PATH_DOCUSHARE_DEBUG = "/ds";

	private UsrBean usrBean;
	private DSServer server;
	private DSSession dssession;
	

	/**
	 * Para usar esta clase siempre necesitamos el UsrBean. Por eso es privado el constructor vacío
	 */
	private DocuShareHelper() {

	}

	/**
	 * 
	 * @param usrBean
	 */
	public DocuShareHelper(UsrBean usrBean) throws ClsExceptions, SIGAException {
		this();
		this.usrBean = usrBean;
	}

	/**
	 * Crea la conexion con el servidor
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private void createSession() throws ClsExceptions, SIGAException {

		String idInstitucion = usrBean.getLocation();

		GenParametrosAdm genParametrosAdm = new GenParametrosAdm(usrBean);
		String host = genParametrosAdm.getValor(idInstitucion, idModulo, DOCUSHARE_HOST, null);
		String port = genParametrosAdm.getValor(idInstitucion, idModulo, DOCUSHARE_PORT, null);
		String domain = genParametrosAdm.getValor(idInstitucion, idModulo, DOCUSHARE_DOMAIN, null);
		String user = genParametrosAdm.getValor(idInstitucion, idModulo, DOCUSHARE_USER, null);
		String password = genParametrosAdm.getValor(idInstitucion, idModulo, DOCUSHARE_PASSWORD, null);
		
		ClsLogging.writeFileLog("Conexion a DOCUSHARE",7);
		ClsLogging.writeFileLog("host="+host,7);
		ClsLogging.writeFileLog("port="+port,7);
		ClsLogging.writeFileLog("domain="+domain,7);
		ClsLogging.writeFileLog("user="+user,7);
		ClsLogging.writeFileLog("password="+password,7);
		
		int defaultPort = 1099;
		if (port != null) {
			defaultPort = Integer.parseInt(port);
		}

		try {
			server = DSFactory.createServer(host, defaultPort);
			ClsLogging.writeFileLog("Creado server con Docushare correctamente", 3);
			dssession = server.createSession(domain, user, password);
			ClsLogging.writeFileLog("Creada session con Docushare correctamente", 3);

		} catch (Exception e) {
			//Se ha producido un error. No se ha podido conectar con el servidor DocuShare.
			throw new SIGAException("expedientes.docushare.error.conexion",e);
		}
		
	}
	
	/**
	 * 
	 * @param collectionTitle Nombre de la carpeta o collecion que se quiere crear
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public String createCollectionCenso(String collectionTitle) throws SIGAException, ClsExceptions, DSException {
		return createCollection(ID_DOCUSHARE_CENSO, collectionTitle);
	}
	
	/**
	 * 
	 * @param collectionTitle Nombre de la carpeta o collecion que se quiere crear
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public String createCollectionEJG(String collectionTitle) throws SIGAException, ClsExceptions, DSException {
		return createCollection(ID_DOCUSHARE_EJG, collectionTitle);
	}	

	/**
	 * 
	 * @param collectionTitle Nombre de la carpeta o collecion que se quiere crear
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public String createCollectionExpedientes(String collectionTitle) throws SIGAException, ClsExceptions, DSException {
		return createCollection(ID_DOCUSHARE_EXPEDIENTES, collectionTitle);
	}
	
	/**
	 * 
	 * @param collectionTitle Nombre de la carpeta o collecion que se quiere crear
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	private String createCollection(String ID_DOCUSHARE, String collectionTitle) throws SIGAException, ClsExceptions, DSException {

		if (MODO_DEBUG_LOCAL) {
			return createCollectionMODO_DEBUG_LOCAL(collectionTitle);
		}
		
		String identificadorDS = null;				
		createSession();
		
		try {

			GenParametrosAdm genParametrosAdm = new GenParametrosAdm(usrBean);
			String idExpedientes = genParametrosAdm.getValor(usrBean.getLocation(), idModulo, ID_DOCUSHARE, null);			
			ClsLogging.writeFileLog("ID_DOCUSHARE=" + idExpedientes, 3);
			
			DSCollection dsCollectionParent = (DSCollection) dssession.getObject(new DSHandle(idExpedientes));			

			DSClass collectionClass = dssession.getDSClass(DSCollection.classname);
			DSProperties collectionPrototype = collectionClass.createPrototype();
			collectionPrototype.setPropValue(DSCollection.title, collectionTitle);			

			DSHandle dsHandle = dssession.createObject(collectionPrototype, DSLinkDesc.containment, dsCollectionParent, null, null);
			identificadorDS = dsHandle.toString();			
			
		} catch (DbNoSuchObjectException e) {
			throw new SIGAException("messages.censo.regtel.coleccionNoEncontrada", e);
		} catch (Exception e) {
			throw new SIGAException("expedientes.docushare.error.crearColeccion",e);
		} finally {
			close();
		}

		return identificadorDS;
	}
	
	private String createCollectionMODO_DEBUG_LOCAL(String collectionTitle) {
		File file = new File(PATH_DOCUSHARE_DEBUG, collectionTitle);
		file.mkdirs();
		return file.getAbsolutePath();
	}
	
	/**
	 * Busca una collection a partir de una path y un nombre de collection
	 * @param path
	 * @param title
	 * @return
	 * @throws Exception
	 */
	private String buscaCollection(String pathRecibido, String title) throws ClsExceptions, DSException, SIGAException {
		
		if (MODO_DEBUG_LOCAL) {
			return buscaCollectionMODO_DEBUG_LOCAL(PATH_DOCUSHARE_DEBUG, title);
		}
		
		
		String idColl = null;

		createSession();

		try {
			GenParametrosAdm genParametrosAdm = new GenParametrosAdm(usrBean);
			String path = genParametrosAdm.getValor(usrBean.getLocation(), idModulo, pathRecibido, null);

			if (path != null) {
				if (!path.trim().endsWith(";")) {
					path += ";";
				}
				path += title;

				String[] titles = path.split(";");
				DSObject dsObject = dssession.getResolvedObject(DSCollection.title, titles, DSSelectSet.NO_PROPERTIES);
				if (dsObject != null) {
					idColl = dsObject.getHandle().toString();
				}
			}
		} finally {
			close();
		}

		return idColl;
	}
	
	private String buscaCollectionMODO_DEBUG_LOCAL(String parent, String title) {
		File file = new File(parent, title);
		if (file != null && file.exists()) {
			return file.getAbsolutePath();
		} else {
			return null;
		}
	}

	/**
	 * Busca un nombre de collection sobre el path de la collection de expedientes
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public String buscaCollectionExpedientes (String title) throws ClsExceptions, DSException, SIGAException {		
		return buscaCollection(PATH_DOCUSHARE_EXPEDIENTES, title);
	}

	/**
	 * Cerramos la session y la conexion con el servidor DocuShare
	 * @throws DSException
	 */
	private void close() throws DSException {	
		ClsLogging.writeFileLog("Cerrando conexión con Docushare...", 3);
		if (server != null) {
			server.close();
			ClsLogging.writeFileLog("Conexión cerrada con Docushare", 3);
		}
	}

	public String buscaCollectionEJG(String title) throws ClsExceptions, DSException, SIGAException {
		return buscaCollection(PATH_DOCUSHARE_EJG, title);
	}
	
	public String buscaCollectionCenso(String title) throws ClsExceptions, DSException, SIGAException {
		return buscaCollection(PATH_DOCUSHARE_CENSO, title);
	}
	
	
	/**
	 * Método para pruebas en local
	 * @param identificadorDS
	 * @return
	 */
	private List<DocuShareObjectVO> getContenidoCollectionMODO_DEBUG_LOCAL(String identificadorDS) throws SIGAException {
		List<DocuShareObjectVO> datos = new ArrayList<DocuShareObjectVO>();
		try {
			File file = new File(identificadorDS);
			
			if (file != null) {				
				List<DocuShareObjectVO> listDir = new ArrayList<DocuShareObjectVO>();
				List<DocuShareObjectVO> listArch = new ArrayList<DocuShareObjectVO>();
				File[] filesHijos = file.listFiles();
					if (filesHijos != null) {
						for (File fileHijo : filesHijos) {
							DocuShareObjectVO dsObj = null;
							if (fileHijo.isDirectory()) {
								dsObj = new DocuShareObjectVO(DocuShareObjectVO.DocuShareTipo.COLLECTION);
								dsObj.setId(fileHijo.getAbsolutePath());
								dsObj.setTitle(fileHijo.getName());
								dsObj.setFechaModificacion(new Date(fileHijo.lastModified()));
								listDir.add(dsObj);
							} else {
								dsObj = new DocuShareObjectVO(DocuShareObjectVO.DocuShareTipo.DOCUMENT);
								dsObj.setId(fileHijo.getAbsolutePath());
								dsObj.setTitle(fileHijo.getName());
								dsObj.setFechaModificacion(new Date(fileHijo.lastModified()));
								dsObj.setSizeKB(fileHijo.length()/1024);
								if (dsObj.getSizeKB() == 0) {
									dsObj.setSizeKB(1);
								}
								listArch.add(dsObj);
							}								
						}
					}
				datos.addAll(listDir);
				datos.addAll(listArch);
				if (false)
					throw new DbNoSuchObjectException("nunca se va a lanzar");
				
			}
		} catch (DbNoSuchObjectException e) {
			throw new SIGAException("messages.censo.regtel.coleccionNoEncontrada", e, null);	
		} catch (Exception e) {
			throw new SIGAException("message.docushare.error.contenidoCollection",e);
		}
		return datos;
		
	}
	
	/**
	 * Recupera lista de colleccion y de documents de una collection
	 * @param collection
	 * @return
	 * @throws Exception
	 */
	public List<DocuShareObjectVO> getContenidoCollection(String collection) throws Exception {
		
		if (collection == null || collection.trim().equals("")) {
			throw new IllegalArgumentException("El nombre de la colección no puede ser nula o vacía");
		}
		
		if (MODO_DEBUG_LOCAL) {
			return getContenidoCollectionMODO_DEBUG_LOCAL(collection);
		}
		
		List<DocuShareObjectVO> list = new ArrayList<DocuShareObjectVO>();
		
		try {
			createSession();		
			ClsLogging.writeFileLog("Recuperando contenido collection " + collection, 3);
			
			DSHandle dsHandle = new DSHandle(collection);
			DSCollection dsCollectionParent = (DSCollection) dssession.getObject(dsHandle);
			
			if (dsCollectionParent != null) {	
				ClsLogging.writeFileLog("Se ha encontrado la collection " + dsCollectionParent.getHandle(), 3);	
			    DSObjectIterator it = dsCollectionParent.children(null);			    
			    if (it != null) {
			    	ClsLogging.writeFileLog("Número de hijos de la collection " + collection + ": " + dsCollectionParent.getChildCount(), 3);
			    	List<DocuShareObjectVO> listDir = new ArrayList<DocuShareObjectVO>();
			    	List<DocuShareObjectVO> listArch = new ArrayList<DocuShareObjectVO>();			    	
			    	
			    	DSObject dsObject = null;
			    	
			    	while (it.hasNext()) {
			    		dsObject = it.nextObject();
			    		ClsLogging.writeFileLog("Encontrado hijo \"" + dsObject.getHandle() + "\" dentro de la collection " + collection, 3);
			    		if (dsObject instanceof  DSCollection) {			    			
			    			DocuShareObjectVO dsObj = new DocuShareObjectVO(DocuShareObjectVO.DocuShareTipo.COLLECTION);
			    			dsObj.setId(dsObject.getHandle().toString());
			    			dsObj.setTitle(dsObject.getTitle());
			    			dsObj.setFechaModificacion(dsObject.getModifiedDate());
			    			listDir.add(dsObj);
			    		} else if (dsObject instanceof  DSDocument) {			    			
			    			DocuShareObjectVO dsObj = new DocuShareObjectVO(DocuShareObjectVO.DocuShareTipo.DOCUMENT);
			    			dsObj.setId(dsObject.getHandle().toString());
			    			dsObj.setTitle(dsObject.getTitle());
			    			dsObj.setFechaModificacion(dsObject.getModifiedDate());	
			    			int size = ((DSDocument) dsObject).getSize();
			    			if (size > 0) {
			    				dsObj.setSizeKB(size / 1024);
			    			}
			    			listArch.add(dsObj);
			    		}	    		
			    	}
			    	
			    	Collections.sort(listDir);
			    	Collections.sort(listArch);
			    	
			    	list.addAll(listDir);
			    	list.addAll(listArch);
			    }
			}
		} catch (DbNoSuchObjectException e) {
			throw new SIGAException("messages.censo.regtel.coleccionNoEncontrada", e);
		} catch (Exception e) {
			throw new SIGAException("message.docushare.error.contenidoCollection",e);		
		} finally {			
			close();
		}
	    
		return list;
	}
	
		
	/**
	 * Método para pruebas en local
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public File getDocumentMODO_DEBUG_LOCAL(String title) throws Exception {
		File file = new File(title);		
		return file;
	}
	
	/**
	 * Recupera un documento copiandolo en un fichero local
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public File getDocument(String title) throws Exception {
		
		if (MODO_DEBUG_LOCAL) {
			return getDocumentMODO_DEBUG_LOCAL(title);
		}
		
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaDirectorioTemp = rp.returnProperty("sjcs.directorioFisicoTemporalSJCSJava");
		File fileParent = new File(rutaDirectorioTemp);		
		
		File file = null;
		
		try {
		    createSession();	 
		    		    
		    DSDocument dsDocument = (DSDocument) dssession.getObject(new DSHandle(title));	   
		    
		    DSContentElement[] dsContentElements = dsDocument.getContentElements();
		    for (int j = 0; j < dsContentElements.length; j++) {
				if (dsContentElements[j] instanceof FileContentElement) {
					FileContentElement dsContentElement = (FileContentElement) dsContentElements[j];					
					fileParent.mkdirs();
					file = new File(fileParent, dsDocument.getOriginalFileName());
					
					dsContentElement.open();					
					int max = dsContentElement.available();
					ByteArrayOutputStream baos = new ByteArrayOutputStream(max);
					
					baos.write(dsContentElement.read(max));
					baos.flush();
					
					OutputStream outputStream = new FileOutputStream (file);					
					baos.writeTo(outputStream);
					
					baos.close();
					outputStream.close();					
					dsContentElement.close();
				}				
			}
			
			return file;
		} catch (Exception e) {
			throw new SIGAException("message.docushare.error.obtenerDocumento",e);    
		} finally {
			close();	
		}
		
	}

	public static void main2(String[] args) throws DSInvalidLicenseException, DSException {
		//ncolegiado=80010
		DSServer dsServer = DSFactory.createServer("192.168.11.129");
				
		DSSession dssession = dsServer.createSession("docushare", "admin", "1234");
		String idExpedientes = "Collection-10";
		DSCollection dsCollectionParent = (DSCollection) dssession.getObject(new DSHandle(idExpedientes));
		System.out.println(dsCollectionParent.getChildCount());
		
		DSClass collectionClass = dssession.getDSClass(DSCollection.classname);
		DSProperties collectionPrototype = collectionClass.createPrototype();
		collectionPrototype.setPropValue(DSCollection.title, "PruebaSIGA");			

		DSHandle dsHandle = dssession.createObject(collectionPrototype, DSLinkDesc.containment, dsCollectionParent, null, null);
		String identificadorDS = dsHandle.toString();
		
		if (dsServer != null) {
			dsServer.close();
		}

	}
	
	public static String getTitleExpedientes(String tipoExpediente, String anioExpediente, String numExpediente) {
		if (isNull(tipoExpediente) || isNull(anioExpediente) || isNull(numExpediente)) {
			throw new IllegalArgumentException("Al crear o buscar una collection el tipo, año y número no pueden ser nulos o vacíos.");
		}
		return tipoExpediente + " " + anioExpediente + "/" +numExpediente;
	}

	private static boolean isNull(String st) {		
		return st == null || st.trim().equals("");
	}

	public static String getTitleEJG(String anio, String numejg) {
		if (isNull(anio) || isNull(numejg)) {
			throw new IllegalArgumentException("Al crear o buscar una collection el año y el número de EJG no pueden ser nulos o vacíos.");
		}
		return  anio + "/" +numejg;
	}
	
}