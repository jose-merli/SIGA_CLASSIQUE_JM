/**
 * 
 */
package com.atos.utils;

import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGAException;
import com.xerox.docushare.DSClass;
import com.xerox.docushare.DSException;
import com.xerox.docushare.DSFactory;
import com.xerox.docushare.DSHandle;
import com.xerox.docushare.DSObject;
import com.xerox.docushare.DSSelectSet;
import com.xerox.docushare.DSServer;
import com.xerox.docushare.DSSession;
import com.xerox.docushare.object.DSCollection;
import com.xerox.docushare.property.DSLinkDesc;
import com.xerox.docushare.property.DSProperties;

/**
 * @author angelcpe
 *
 */
public class DocuShareHelper {

	private static String idModulo = "GEN";

	private static String DOCUSHARE_HOST = "DOCUSHARE_HOST";
	private static String DOCUSHARE_PORT = "DOCUSHARE_PORT";
	private static String DOCUSHARE_DOMAIN = "DOCUSHARE_DOMAIN";

	private static String DOCUSHARE_USER = "DOCUSHARE_USER";
	private static String DOCUSHARE_PASSWORD = "DOCUSHARE_PASSWORD";
	
	private static String DOCUSHARE_USER_ENCRYP = "DOCUSHARE_USER_ENCRYP";
	private static String DOCUSHARE_PASSWORD_ENCRYP = "DOCUSHARE_PASSWORD_ENCRYP";
	

	private static String PATH_DOCUSHARE_EXPEDIENTES = "PATH_DOCUSHARE_EXPEDIENTES";
	private static String PATH_DOCUSHARE_EJG = "PATH_DOCUSHARE_EJG";
	private static String PATH_DOCUSHARE_CENSO = "PATH_DOCUSHARE_CENSO";
	
	private static String ID_DOCUSHARE_EXPEDIENTES = "ID_DOCUSHARE_EXPEDIENTES";
	private static String PATH_DOCUSHARE = "PATH_DOCUSHARE";

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
			dssession = server.createSession(domain, user, password);			

		} catch (Exception e) {
			//Se ha producido un error. No se ha podido conectar con el servidor DocuShare.
			throw new SIGAException("expedientes.docushare.error.conexion",e);
		}
		
	}

	/**
	 * 
	 * @return
	 */
	public String getURLCollection(String handle) throws SIGAException, ClsExceptions {

		String url = null;
//		return "https://documentacion.redabogacia.org/docushare/dsweb/LoginObj/wmke/wmke/Collection-3918";
		createSession();
		
		try {		
			String idInstitucion = usrBean.getLocation();
			DSCollection dsCollection = (DSCollection) dssession.getObject(new DSHandle(handle));
			GenParametrosAdm genParametrosAdm = new GenParametrosAdm(usrBean);
			String pathDS = genParametrosAdm.getValor(idInstitucion, idModulo, PATH_DOCUSHARE, null);
			String userEncrypted = genParametrosAdm.getValor(idInstitucion, idModulo, DOCUSHARE_USER_ENCRYP, null);
			String passwordEncrypted = genParametrosAdm.getValor(idInstitucion, idModulo, DOCUSHARE_PASSWORD_ENCRYP, null);

			if (!pathDS.endsWith("/")) {
				pathDS += "/";
			}
			url = pathDS + userEncrypted + "/" + passwordEncrypted + "/" + dsCollection.toString();
			ClsLogging.writeFileLog("url="+url,7);
			
			close();

		} catch (Exception e) {
			throw new SIGAException("expedientes.docushare.error.obtenerColeccion",e);
		}
		return url;
	}

	/**
	 * 
	 * @param collectionTitle Nombre de la carpeta o collecion que se quiere crear
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public String createCollection(String collectionTitle) throws SIGAException, ClsExceptions, DSException {

		String identificadorDS = null;				
		createSession();
		
		try {

			GenParametrosAdm genParametrosAdm = new GenParametrosAdm(usrBean);
			String idExpedientes = genParametrosAdm.getValor(usrBean.getLocation(), idModulo, ID_DOCUSHARE_EXPEDIENTES, null);			

			DSCollection dsCollectionParent = (DSCollection) dssession.getObject(new DSHandle(idExpedientes));

			DSClass collectionClass = dssession.getDSClass(DSCollection.classname);
			DSProperties collectionPrototype = collectionClass.createPrototype();
			collectionPrototype.setPropValue(DSCollection.title, collectionTitle);

			DSHandle dsHandle = dssession.createObject(collectionPrototype, DSLinkDesc.containment, dsCollectionParent, null, null);
			identificadorDS = dsHandle.toString();

			
			
		} catch (Exception e) {
			throw new SIGAException("expedientes.docushare.error.crearColeccion",e);
		} finally {
			close();
		}

		return identificadorDS;
	}
	
	/**
	 * Busca una collection a partir de una path y un nombre de collection
	 * @param path
	 * @param title
	 * @return
	 * @throws Exception
	 */
	private String buscaCollection(String pathRecibido, String title) throws ClsExceptions, DSException, SIGAException {
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
		if (server != null) {
			server.close();
		}
	}

	public String buscaCollectionEJG(String title) throws ClsExceptions, DSException, SIGAException {
		return buscaCollection(PATH_DOCUSHARE_EJG, title);
	}
	
	public String buscaCollectionCenso(String title) throws ClsExceptions, DSException, SIGAException {
		return buscaCollection(PATH_DOCUSHARE_CENSO, title);
	}

	
}
