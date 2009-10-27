package com.siga.gratuita.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgRemesaResolucionAdm;
import com.siga.beans.CajgRemesaResolucionBean;
import com.siga.beans.CajgRemesaResolucionFicheroAdm;
import com.siga.beans.CajgRemesaResolucionFicheroBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinicionRemesaResolucionesCAJGForm;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.informes.MasterWords;



public class DefinirRemesaResolucionesCAJGAction extends MasterAction {

	private final boolean ELIMINA_DATOS_TABLA_TEMPORAL = true;
	
	private String CONTADOR_REMESARESOLUCIONCAJG = "REMESARESOLUCIONCAJG";
	/**
	 * Funcion que atiende a las peticiones. Segun el valor del parametro modo
	 * del formulario ejecuta distintas acciones
	 * 
	 * @param mapping -
	 *            Mapeo de los struts
	 * @param formulario -
	 *            Action Form asociado a este Action
	 * @param request -
	 *            objeto llamada HTTP
	 * @param response -
	 *            objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception SIGAExceptions
	 *                En cualquier caso de error
	 */

	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
			
			String accion = miForm.getModo();

			// La primera vez que se carga el formulario
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
				mapDestino = abrir(mapping, miForm, request, response);
			
			} else if (accion.equalsIgnoreCase("descargar")) {
				mapDestino = descargar(mapping, miForm, request, response, false);			
			} else if (accion.equalsIgnoreCase("descargarLog")) {
				mapDestino = descargar(mapping, miForm, request, response, true);
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				// mapDestino = "exception";
				if (miForm.getModal().equalsIgnoreCase("TRUE")) {
					request.setAttribute("exceptionTarget", "parent.modal");
				}

				// throw new ClsExceptions("El ActionMapping no puede ser
				// nulo");
				throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
			}

		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}
		return mapping.findForward(mapDestino);
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y realiza la
	 * consulta a partir de esos datos. Almacena un vector con los resultados en
	 * la sesión con el nombre "resultado"
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		Hashtable miHash = new Hashtable();
		String consulta = "";

		try {

			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;
			
			miHash = miForm.getDatos();

			HashMap databackup = new HashMap();

			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador) databackup.get("paginador");
				Vector datos = new Vector();

				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = request.getParameter("pagina");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
						// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {

				databackup = new HashMap();

				// obtengo datos de la consulta
				
				Vector datos = null;

				/*
				 * Construimos la primera parte de la consulta, donde escogemos
				 * los campos a recuperar y las tablas necesarias
				 */
				consulta = "select R." + CajgRemesaResolucionBean.C_IDREMESARESOLUCION +
								"," + "R." + CajgRemesaResolucionBean.C_PREFIJO +
								"," + "R." + CajgRemesaResolucionBean.C_NUMERO +
								"," + "R." + CajgRemesaResolucionBean.C_SUFIJO +
								"," + "R." + CajgRemesaResolucionBean.C_NOMBREFICHERO +
								"," + "R." + CajgRemesaResolucionBean.C_OBSERVACIONES +
								"," + "R." + CajgRemesaResolucionBean.C_FECHACARGA +
								"," + "R." + CajgRemesaResolucionBean.C_FECHARESOLUCION +
								"," + "R." + CajgRemesaResolucionBean.C_LOGGENERADO +
								" FROM " + CajgRemesaResolucionBean.T_NOMBRETABLA + " R" +
								" WHERE R." + CajgRemesaResolucionBean.C_IDINSTITUCION + " = " + this.getIDInstitucion(request);						
						
				
						
				if ((String) miHash.get(CajgRemesaResolucionBean.C_PREFIJO) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_PREFIJO)).equals(""))) {					
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_PREFIJO)).trim(), "r." + CajgRemesaResolucionBean.C_PREFIJO);
				}
				if ((String) miHash.get(CajgRemesaResolucionBean.C_NUMERO) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_NUMERO)).equals(""))) {					
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_NUMERO)).trim(), "r." + CajgRemesaResolucionBean.C_NUMERO);
				}
				if ((String) miHash.get(CajgRemesaResolucionBean.C_SUFIJO) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_SUFIJO)).equals(""))) {
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_SUFIJO)).trim(), "r." + CajgRemesaResolucionBean.C_SUFIJO);					
				}
				if ((String) miHash.get(CajgRemesaResolucionBean.C_OBSERVACIONES) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_OBSERVACIONES)).equals(""))) {
					// consulta +=" and r.descripcion like
					// '%"+(String)miHash.get("DESCRIPCION")+"'";
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_OBSERVACIONES)).trim(), "r." + CajgRemesaResolucionBean.C_OBSERVACIONES);
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.C_NOMBREFICHERO) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_NOMBREFICHERO)).equals(""))) {					
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_NOMBREFICHERO)).trim(), "r." + CajgRemesaResolucionBean.C_NOMBREFICHERO);
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCION) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCION)).equals(""))) {
					consulta += " and R." + CajgRemesaResolucionBean.C_FECHARESOLUCION + " >= '" + (String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCION) + "'";
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCIONHASTA) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCIONHASTA)).equals(""))) {
					consulta += " and R." + CajgRemesaResolucionBean.C_FECHARESOLUCION + " <= '" + (String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCIONHASTA) + "'";
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.C_FECHACARGA) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_FECHACARGA)).equals(""))) {
					consulta += " and R." + CajgRemesaResolucionBean.C_FECHACARGA + " >= '" + (String) miHash.get(CajgRemesaResolucionBean.C_FECHACARGA) + "'";
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.c_FECHACARGAHASTA) != null && (!((String) miHash.get(CajgRemesaResolucionBean.c_FECHACARGAHASTA)).equals(""))) {
					consulta += " and R." + CajgRemesaResolucionBean.C_FECHACARGA + " <= '" + (String) miHash.get(CajgRemesaResolucionBean.c_FECHACARGAHASTA) + "'";
				}
				
				if (((String) miHash.get("ANIO") != null && !((String) miHash.get("ANIO")).equals(""))
						|| ((String) miHash.get("NUMEJG") != null && !((String) miHash.get("NUMEJG")).equals(""))) {
					consulta += " AND (R.IDINSTITUCION, R.IDREMESARESOLUCION) IN (SELECT ER.IDINSTITUCION, ER.IDREMESARESOLUCION " +
							" FROM CAJG_EJGRESOLUCION ER" +
							" WHERE ER.IDINSTITUCION = R.IDINSTITUCION";
					if ((String) miHash.get("ANIO") != null && !((String) miHash.get("ANIO")).equals("")) {
							consulta += " AND ER.ANIO = " +  miHash.get("ANIO");
					}
					
					if ((String) miHash.get("NUMEJG") != null && !((String) miHash.get("NUMEJG")).equals("")) {
						consulta += " AND ER.NUMEJG = " +  miHash.get("NUMEJG");
					}
					
					consulta += ")";
						
				}
				
				consulta += " ORDER BY R." + CajgRemesaResolucionBean.C_PREFIJO + " DESC, R." + CajgRemesaResolucionBean.C_NUMERO + " DESC , R." + CajgRemesaResolucionBean.C_SUFIJO + " DESC";


				PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(consulta);			
				
				
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					int pagina = 1;
					String paginaSeleccionada = request.getParameter("paginaSeleccionada");
					if (paginaSeleccionada != null && !paginaSeleccionada.trim().equals("")) {
						pagina = Integer.parseInt(paginaSeleccionada.trim());
					}
					datos = paginador.obtenerPagina(pagina);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);					
				}

			}

			request.getSession().setAttribute("DATOSFORMULARIO", miHash);

		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "listarResoluciones";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando
	 * esta hash en la sesión con el nombre "elegido"
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;

			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			Vector visibles = miForm.getDatosTablaVisibles(0);
			Hashtable miHash = new Hashtable();

			if ((ocultos != null && visibles != null)) {
				miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, ocultos.get(0));
				miHash.put(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());
				
			} else {
				
				miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, miForm.getIdRemesaResolucion());
				miHash.put(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());

			}
			CajgRemesaResolucionAdm remesaAdm = new CajgRemesaResolucionAdm(this.getUserBean(request));
			CajgRemesaResolucionBean b = new CajgRemesaResolucionBean();
			Vector a = remesaAdm.selectByPK(miHash);
			b = (CajgRemesaResolucionBean) a.get(0);
			Hashtable h = b.getOriginalHash();
			
			request.setAttribute("modo", "editar");

			request.setAttribute("REMESARESOLUCION", h);
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;

			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			Vector visibles = miForm.getDatosTablaVisibles(0);
			Hashtable miHash = new Hashtable();
			miHash.put(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());
			
			if ((ocultos != null && visibles != null)) {
				miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, ocultos.get(0));
			} else {				
				miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, miForm.getIdRemesaResolucion());
			}
			
			CajgRemesaResolucionAdm RemesaAdm = new CajgRemesaResolucionAdm(this.getUserBean(request));
			CajgRemesaResolucionBean b = new CajgRemesaResolucionBean();
			Vector a = RemesaAdm.selectByPK(miHash);
			b = (CajgRemesaResolucionBean) a.get(0);
			Hashtable h = b.getOriginalHash();
			
			request.setAttribute("modo", "consultar");

			request.setAttribute("REMESARESOLUCION", h);
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "
	 *  . .Fiesta" para que redirija a la pantalla de inserción.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// si el usuario logado es letrado consultar en BBDD el nColegiado para
		// mostrar en la jsp
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

		DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;

		try {
			Integer idinstitucion = new Integer(usr.getLocation());
			GestorContadores gcRemesa = new GestorContadores(this.getUserBean(request));
			Hashtable contadorTablaHashRemesa = gcRemesa.getContador(idinstitucion, CONTADOR_REMESARESOLUCIONCAJG);
			
			String siguiente = gcRemesa.getNuevoContador(contadorTablaHashRemesa);
  					
			String prefijo = UtilidadesHash.getString(contadorTablaHashRemesa, "PREFIJO");
			String sufijo = UtilidadesHash.getString(contadorTablaHashRemesa, "SUFIJO");
			String modocontador = contadorTablaHashRemesa.get("MODO").toString();

			miForm.setNumero(siguiente);
			miForm.setPrefijo(prefijo);
			miForm.setSufijo(sufijo);			

			request.setAttribute("modoContador", modocontador);

		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			miForm.setNumero("");
			miForm.setPrefijo("");
			miForm.setSufijo("");			
			request.setAttribute("modoContador", "");
		}
		return "insertarResolucion";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en
	 * la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected synchronized String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UserTransaction tx = null;
		String mensaje = "";

		try {
			
			
			CajgRemesaResolucionBean cajgRemesaResolucionBean = new CajgRemesaResolucionBean();
			HttpSession session = request.getSession();
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;
			String idInstitucion = miForm.getIdInstitucion();
			
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");
			String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");				
			rutaAlmacen += File.separator + idInstitucion + File.separator + "remesaResoluciones";
			
			CajgRemesaResolucionAdm resolucionAdm = new CajgRemesaResolucionAdm(this.getUserBean(request));

			FormFile formFile = miForm.getFile();
			if (formFile.getFileSize() == 0){
				throw new SIGAException("message.cajg.ficheroValido"); 
			}
			
			tx = usr.getTransaction();
			tx.begin();
	    		
			GestorContadores gcRemesa = new GestorContadores(this.getUserBean(request));
			
			Hashtable contadorTablaHashRemesa = gcRemesa.getContador(this.getIDInstitucion(request), CONTADOR_REMESARESOLUCIONCAJG);
			String siguiente = gcRemesa.getNuevoContador(contadorTablaHashRemesa);		
			
			cajgRemesaResolucionBean.setPrefijo(contadorTablaHashRemesa.get("PREFIJO").toString());
			cajgRemesaResolucionBean.setSufijo(contadorTablaHashRemesa.get("SUFIJO").toString());
			cajgRemesaResolucionBean.setNumero(siguiente);
			cajgRemesaResolucionBean.setFechaCarga(GstDate.getApplicationFormatDate("", miForm.getFechaCarga()));
			cajgRemesaResolucionBean.setFechaResolucion(GstDate.getApplicationFormatDate("", miForm.getFechaResolucion()));

			gcRemesa.setContador(contadorTablaHashRemesa, siguiente);

			cajgRemesaResolucionBean.setIdInstitucion(this.getIDInstitucion(request));
			
			String idRemesaResolucion = resolucionAdm.seleccionarMaximo(this.getIDInstitucion(request).toString());
			
			cajgRemesaResolucionBean.setIdRemesaResolucion(Integer.valueOf(idRemesaResolucion));			

			
			
			File parentFile = new File(rutaAlmacen, idRemesaResolucion);
			deleteFiles(parentFile);			
			parentFile.mkdirs();
			
			
	    	InputStream stream = formFile.getInputStream();	    	
	    	
	    	File file = new File(parentFile, formFile.getFileName());
	    	cajgRemesaResolucionBean.setNombreFichero(file.getName());
	    	cajgRemesaResolucionBean.setLogGenerado("1");
    		
    		resolucionAdm.insert(cajgRemesaResolucionBean);
    		
    		boolean generaLog = createZIP(usr, idInstitucion, idRemesaResolucion, file, stream);
    		
    		if (!generaLog) {
    			cajgRemesaResolucionBean.setLogGenerado("0");
    			resolucionAdm.updateDirect(cajgRemesaResolucionBean);
    		}
    		
			tx.commit();

			session.setAttribute("accion", "editar");
			request.setAttribute(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, idRemesaResolucion);
			request.setAttribute(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());
			
			mensaje = "messages.inserted.success";
			
			if (!miForm.getPrefijo().equals(contadorTablaHashRemesa.get("PREFIJO")) || 
					!miForm.getSufijo().equals(contadorTablaHashRemesa.get("SUFIJO")) ||
					!miForm.getNumero().equals(siguiente)) {
						String[] datos = new String[]{contadorTablaHashRemesa.get("PREFIJO")+siguiente+contadorTablaHashRemesa.get("SUFIJO")};						
						mensaje = UtilidadesString.getMensaje("message.cajg.distintoNumRegistro", datos, usr.getLanguage());
			}
				 

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}

		request.setAttribute("mensaje", mensaje);
		request.setAttribute("modal", "");
		
		

		return exitoModal(mensaje, request);
	}

	/**
	 * 
	 * @param parentFile
	 */
	private void deleteFiles(File parentFile) {
		if (parentFile != null) {
			File[] files = parentFile.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}		
			}
		}
	}

	/**
	 * 
	 * @param file
	 * @param stream
	 * @throws IOException
	 * @throws ClsExceptions
	 */
	private boolean createZIP(UsrBean usr, String idInstitucion, String idRemesaResolucion, File file, InputStream stream) throws IOException, ClsExceptions, SIGAException {
		OutputStream bos = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		
		stream.close();
		bos.flush();
		bos.close();
		
		boolean generaLog = callProcedure(usr, idInstitucion, idRemesaResolucion, file);
		
		ArrayList ficheros = new ArrayList();
		ficheros.add(file);
		String nombreZip = file.getAbsolutePath();
		nombreZip = nombreZip.substring(0, nombreZip.lastIndexOf("."));
		MasterWords.doZip(ficheros, nombreZip);
		
		return generaLog;
	}

	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	private boolean callProcedure(UsrBean usr, String idInstitucion, String idRemesaResolucion, File file) throws IOException, SIGAException, ClsExceptions {
		boolean generaLog = false;
		FileReader fileReader = new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);
		String line = null;		
		
		String sql = "SELECT CONSULTA, CABECERA, DELIMITADOR" +
				" FROM CAJG_PROCEDIMIENTOREMESARESOL" +
				" WHERE IDINSTITUCION = " + idInstitucion;
		RowsContainer rc = new RowsContainer();
		if (!rc.find(sql)) {							
			throw new SIGAException("messages.cajg.funcionNoDefinida");
		}

		
		Row row = null;
		String funcion = null;
		String cabecera = null;
		String delimitador = null;
				
		CajgRemesaResolucionFicheroAdm cajgResolucionFicheroAdm = new CajgRemesaResolucionFicheroAdm(usr);
		int idRemesaResolucionFichero = cajgResolucionFicheroAdm.seleccionarMaximo();
		int primerIdRemesaResolucionFichero = idRemesaResolucionFichero;
		
		CajgRemesaResolucionFicheroBean cajgRemesaResolucionFicheroBean = new CajgRemesaResolucionFicheroBean();
		cajgRemesaResolucionFicheroBean.setIdInstitucion(Integer.valueOf(idInstitucion));
		cajgRemesaResolucionFicheroBean.setIdRemesaResolucion(Integer.valueOf(idRemesaResolucion));
		
		int numLinea = 1;
		while ((line = br.readLine()) != null) {
			cajgRemesaResolucionFicheroBean.setNumeroLinea(new Integer(numLinea++));
			cajgRemesaResolucionFicheroBean.setLinea(line);
			cajgRemesaResolucionFicheroBean.setIdRemesaResolucionFichero(new Integer(idRemesaResolucionFichero++));
			cajgResolucionFicheroAdm.insert(cajgRemesaResolucionFicheroBean);							
		}
		
		br.close();
		fileReader.close();
		
		String nombreFichero = file.getName();
		nombreFichero = nombreFichero.substring(0, nombreFichero.lastIndexOf("."));
    	
		for (int j = 0; j < rc.size(); j++) {		
			row = (Row) rc.get(j);
			funcion = row.getString("CONSULTA");
			cabecera = (String) row.getValue("CABECERA");
			delimitador = row.getString("DELIMITADOR");
						
			if (j == 0 && cabecera != null && cabecera.trim().equals("1")) {
				cajgRemesaResolucionFicheroBean = new CajgRemesaResolucionFicheroBean();
				cajgRemesaResolucionFicheroBean.setIdRemesaResolucionFichero(new Integer(primerIdRemesaResolucionFichero));
				cajgResolucionFicheroAdm.delete(cajgRemesaResolucionFicheroBean);
			}
			
			Object[] param_in = new String[]{idInstitucion, idRemesaResolucion, delimitador, nombreFichero, usr.getUserName()};
	    	
	    	ClsMngBBDD.callPLProcedure("{call " + funcion + " (?,?,?,?,?)}", 0, param_in);	 	
	    	
		}
		
		String consulta = "SELECT E.CODIGO, E.DESCRIPCION" +
				", R." + CajgRemesaResolucionFicheroBean.C_PARAMETROSERROR +
				", R." + CajgRemesaResolucionFicheroBean.C_NUMEROLINEA +
				" FROM " + CajgRemesaResolucionFicheroBean.T_NOMBRETABLA + " R, CAJG_ERRORESREMESARESOL E" +
				" WHERE R." + CajgRemesaResolucionFicheroBean.C_IDERRORESREMESARESOL + " = E.IDERRORESREMESARESOL" +
				" AND R." + CajgRemesaResolucionFicheroBean.C_IDINSTITUCION + " = E.IDINSTITUCION" +
				" AND R." + CajgRemesaResolucionFicheroBean.C_IDINSTITUCION + " = " + idInstitucion + 
				" AND R." + CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION + " = " + idRemesaResolucion +
				" ORDER BY R." + CajgRemesaResolucionFicheroBean.C_NUMEROLINEA;
				
		RowsContainer rowsContainer = new RowsContainer();
		rowsContainer.query(consulta);
				
		if (rowsContainer != null && rowsContainer.size() > 0) {
			generaLog = true;
			File logFile = new File(file.getParent(), "log");
			deleteFiles(logFile);
			logFile.mkdirs();
			logFile = new File(logFile, nombreFichero + "_errores.txt");
			
			FileWriter fileWriter = new FileWriter(logFile);
			BufferedWriter bw = new BufferedWriter(fileWriter);
			String descripcion, parametrosError, codigo, numeroLinea;
			String[] params;
			MessageFormat messageFormat;
			
			for (int i = 0; i < rowsContainer.size(); i++) {				
				row = (Row)rowsContainer.get(i);
				codigo = row.getString("CODIGO");
				descripcion = row.getString("DESCRIPCION");
				parametrosError = row.getString(CajgRemesaResolucionFicheroBean.C_PARAMETROSERROR);
				numeroLinea = row.getString(CajgRemesaResolucionFicheroBean.C_NUMEROLINEA);
				params = parametrosError.split(",");
				messageFormat = new MessageFormat(descripcion);				
				
				bw.write("[Línea:" + numeroLinea + "] " + "[" + codigo + "] " + messageFormat.format(params));
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		}
		
		if (ELIMINA_DATOS_TABLA_TEMPORAL && cabecera != null && !cabecera.trim().equals("2")) {
			Hashtable hash = new Hashtable();
			hash.put(CajgRemesaResolucionFicheroBean.C_IDINSTITUCION, idInstitucion);
			hash.put(CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION, idRemesaResolucion);
			cajgResolucionFicheroAdm.deleteDirect(hash, new String[]{CajgRemesaResolucionFicheroBean.C_IDINSTITUCION, CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION});
		}

    	return generaLog;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica
	 * en la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		try {

			Hashtable miHash = new Hashtable();
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;			

			CajgRemesaResolucionAdm remesaAdm = new CajgRemesaResolucionAdm(this.getUserBean(request));

			miHash.put(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, miForm.getIdRemesaResolucion());
			
			miHash.put(CajgRemesaResolucionBean.C_PREFIJO, miForm.getPrefijo());
			miHash.put(CajgRemesaResolucionBean.C_NUMERO, miForm.getNumero());
			miHash.put(CajgRemesaResolucionBean.C_SUFIJO, miForm.getSufijo());			
			
			miHash.put(CajgRemesaResolucionBean.C_NOMBREFICHERO, miForm.getNombreFichero());
			miHash.put(CajgRemesaResolucionBean.C_OBSERVACIONES, miForm.getObservaciones());			
			miHash.put(CajgRemesaResolucionBean.C_FECHACARGA, GstDate.getApplicationFormatDate("", miForm.getFechaCarga()));
			miHash.put(CajgRemesaResolucionBean.C_FECHARESOLUCION, GstDate.getApplicationFormatDate("", miForm.getFechaResolucion()));
			miHash.put(CajgRemesaResolucionBean.C_LOGGENERADO, miForm.getLogGenerado());
			
			tx = usr.getTransaction();
			tx.begin();
			remesaAdm.updateDirect(miHash, null, null);
			tx.commit();

			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}
		return exitoModal("messages.updated.success", request);
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de
	 * la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return exitoRefresco("messages.deleted.success", request);
	}

		
	public static File getFichero(String idInstitucion, String idRemesaResolucion, boolean log) {
		File file = null;
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
			
		rutaAlmacen += File.separator + idInstitucion + File.separator + "remesaResoluciones";		
		rutaAlmacen += File.separator + idRemesaResolucion;
		
		if (log) {
			rutaAlmacen += File.separator + "log";
		}
		
		File dir = new File(rutaAlmacen);
		if (dir.exists()) {
			if (dir.listFiles() != null && dir.listFiles().length > 0) {
				int numFicheros = 0;
				for (int i = 0; i < dir.listFiles().length ; i++) {
					if (dir.listFiles()[i].isFile()){
						file = dir.listFiles()[i];
						numFicheros++;
					}
				}
									
				if (numFicheros > 1) {
//					throw new SIGAException("Existe más de un fichero zip");
				}
			}
		}
		
		return file;
	}

	/**
	 * No implementado
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return buscarPor(mapping, formulario, request, response);		
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String volver = request.getParameter("volver");
		if (volver != null && volver.equalsIgnoreCase("SI")) {
			request.setAttribute("VOLVER", "1");
		} else {
			request.setAttribute("VOLVER", "0");
		}
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accion");
		return "inicio";
	}


	/**
	 * 
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,
			SIGAException {
		DefinirEJGForm miForm = (DefinirEJGForm) formulario;
		request.setAttribute("nombreFichero", miForm.getFicheroDownload());
		request.setAttribute("rutaFichero", miForm.getRutaFicheroDownload());
		request.setAttribute("borrarFichero", miForm.getBorrarFicheroDownload());
		return "descargaFichero";
	}


	private String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean log) throws ClsExceptions,
			SIGAException {

		
		DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;			
		File file = getFichero(getIDInstitucion(request).toString(), miForm.getIdRemesaResolucion(), log);

		if (file == null) {								
			throw new SIGAException("messages.general.error.ficheroNoExiste");
		}				
		
		request.setAttribute("nombreFichero", file.getName());
		request.setAttribute("rutaFichero", file.getAbsolutePath());

		return "descargaFichero";
	}
	
}