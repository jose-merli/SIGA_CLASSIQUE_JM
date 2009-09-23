package com.siga.administracion.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;
import org.apache.struts.action.*;
import com.siga.Utilidades.Paginador;
import com.siga.administracion.form.*;

public class PCAJGListadoTablasMaestrasAction extends MasterAction {
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "abrir";
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			PCAJGListadoTablasMaestrasForm form = (PCAJGListadoTablasMaestrasForm) formulario;

			UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
			/***** PAGINACION*****/
			HashMap databackup = new HashMap();
			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador) databackup.get("paginador");
				Vector datos = new Vector();

				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = request.getParameter("pagina");

				if (pagina != null) {
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				} else {// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);
				request.setAttribute("beanTablaMaestraPCAJG", request.getSession().getAttribute("beanTablaMaestraPCAJGOld"));
			} else {
				databackup = new HashMap();
				Paginador resultado = null;
				Vector datos = null;
				String idTabla = form.getNombreTablaMaestra();

				PCAJGTablasMaestrasAdm tablasMaestrasAdm = new PCAJGTablasMaestrasAdm(this.getUserBean(request));

				Vector datosTablaMaestra = tablasMaestrasAdm.select(" WHERE IDENTIFICADOR = '" + idTabla + "'");

				PCAJGTablasMaestrasBean beanTablaMaestra = (PCAJGTablasMaestrasBean) datosTablaMaestra.elementAt(0);

				resultado = getTablaMaestras(mapping, form, request, response);
				databackup.put("paginador", resultado);
				if (resultado != null) {
					datos = resultado.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
				}

				request.setAttribute("beanTablaMaestraPCAJG", beanTablaMaestra);
				request.getSession().setAttribute("beanTablaMaestraPCAJGOld", request.getAttribute("beanTablaMaestraPCAJG"));
			}

			/******************************/
		} catch (Exception e) {
			this.throwExcp("error.messages.application", e, null);
		}
		return "buscar";
	}

	/**
	 * Obtiene los valores dados de alta en las tablas maestras
	 * @param formulario Formulario de PCAJGListadoTablasMaestrasForm con los datos de busqueda 
	 * @return Paginador 
	 */
	private Paginador getTablaMaestras(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		Paginador paginador = null;
		try {

			PCAJGListadoTablasMaestrasForm form = (PCAJGListadoTablasMaestrasForm) formulario;
			UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));

			String codigo = form.getCodigo().trim();
			String descripcion = form.getDescripcion().trim();			
			String nombreTablaMaestra = form.getNombreTablaMaestra();
			
			PCAJGTablasMaestrasAdm pcMaestrasAdm = new PCAJGTablasMaestrasAdm(this.getUserBean(request));
			String sql = pcMaestrasAdm.getSQLTM(nombreTablaMaestra, codigo, descripcion, userBean.getLanguage(), getIDInstitucion(request).intValue());
			
			paginador = new Paginador(sql);
			int totalRegistros = paginador.getNumeroTotalRegistros();

			if (totalRegistros == 0) {
				paginador = null;
			} 
			
		} catch (Exception e) {
			this.throwExcp("error.messages.application", e, null);
		}

		return paginador;
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salida = "";
		try {
			salida = mostrarRegistro(mapping, formulario, request, response, true, false);
		} catch (Exception e) {
			this.throwExcp("error.messages.application", e, null);
		}
		return salida;
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salida = "";
		try {
			salida = mostrarRegistro(mapping, formulario, request, response, false, false);
		} catch (Exception e) {
			this.throwExcp("error.messages.application", e, null);
		}
		return salida;
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salida = "";
		try {
			salida = mostrarRegistro(mapping, formulario, request, response, true, true);
		} catch (Exception e) {
			this.throwExcp("error.messages.application", e, null);
		}
		return salida;
	}

	protected synchronized String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		UsrBean userBean = null;
		UserTransaction tx = null;

		try {
			PCAJGListadoTablasMaestrasForm form = (PCAJGListadoTablasMaestrasForm) formulario;
			userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
			tx = userBean.getTransaction();

			String nombreTabla = form.getNombreTablaMaestra();
			String codigo = form.getCodigo();
			String descripcion = form.getDescripcion();
			String abreviatura = form.getAbreviatura();
			String campoFKSIGA = form.getCampoFKSIGA();
			String[] campoFKSIGAvalue = form.getCampoFKSIGAvalue();
			String tablaRelacion = form.getTablaRelacion();

			Object[] fields = null;
			Vector vFields = new Vector();

			Row row = new Row();

			String identificadorNuevo = this.getNuevoID(userBean.getLocation(), nombreTabla);

			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, "DESCRIPCION", this.getIDInstitucion(request), identificadorNuevo);

			row.setValue("IDENTIFICADOR", identificadorNuevo);
			row.setValue("CODIGO", codigo);
			row.setValue("DESCRIPCION", idRecurso!=null?String.valueOf(idRecurso):descripcion);
			
			
			row.setValue("IDINSTITUCION", userBean.getLocation());
			row.setValue(MasterBean.C_USUMODIFICACION, userBean.getUserName());
			row.setValue(MasterBean.C_FECHAMODIFICACION, "SYSDATE");

			vFields.add("IDENTIFICADOR");
			vFields.add("CODIGO");
			vFields.add("DESCRIPCION");
			
			vFields.add("IDINSTITUCION");
			vFields.add(MasterBean.C_USUMODIFICACION);
			vFields.add(MasterBean.C_FECHAMODIFICACION);

			
			if (abreviatura != null && !abreviatura.trim().equals("")) {
				row.setValue("ABREVIATURA", abreviatura);
				vFields.add("ABREVIATURA");
			}
			

			fields = vFields.toArray();

			//Chequeo si existe una descripcion igual:
			if (this.existeDescripcion(identificadorNuevo, descripcion, nombreTabla, userBean.getLocation(), userBean.getLanguage())) {
				return error("messages.inserted.descDuplicated", new ClsExceptions("messages.inserted.descDuplicated"), request);
			} else if (this.existeCodigoExterno(identificadorNuevo, codigo, nombreTabla, userBean.getLocation(), userBean.getLanguage())) {
				return error("messages.inserted.codDuplicated", new ClsExceptions("messages.inserted.codDuplicated"), request);
			} else {
				tx.begin();
				if (row.add(nombreTabla, fields) > 0) {
					
					if (campoFKSIGAvalue != null && tablaRelacion != null && !tablaRelacion.trim().equals("")) {						
						String sql = null;
						Row mRow = new Row();
						
						for (int i = 0; i < campoFKSIGAvalue.length; i++) {
							sql = "INSERT INTO " + tablaRelacion + " (IDINSTITUCION, IDENTIFICADOR, " + campoFKSIGA + ", FECHAMODIFICACION, USUMODIFICACION) VALUES" +
							" ("  + getIDInstitucion(request) + ", " + identificadorNuevo + ", '" + campoFKSIGAvalue[i] + "', SYSDATE, " + userBean.getUserName() + ")";
							mRow.insertSQL(sql);
						}
					}
					
					
					
					///////////////////////////////////////////
					// Multiidioma: Insertamos los recursos en gen_recursos_catalogos
					if (idRecurso != null) {
						String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, "DESCRIPCION", this.getIDInstitucion(request),
								identificadorNuevo);
						GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
						GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean();
						recCatalogoBean.setCampoTabla("DESCRIPCION");
						recCatalogoBean.setDescripcion(descripcion);
						recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));

						recCatalogoBean.setIdRecurso(idRecurso);
						recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
						recCatalogoBean.setNombreTabla(nombreTabla);
						if (!admRecCatalogos.insert(recCatalogoBean, userBean.getLanguageInstitucion()))
							throw new SIGAException("messages.inserted.error");
					}
					///////////////////////////////////////////

					tx.commit();
					request.setAttribute("mensaje", "messages.inserted.success");

				} else {
					tx.rollback();
					request.setAttribute("mensaje", "messages.inserted.error");
				}
			}

		} catch (Exception e) {
			throwExcp("error.messages.application", e, tx);
		}
		request.setAttribute("modal", "1");
		return "exito";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean userBean = null;
		UserTransaction tx = null;

		try {
			PCAJGListadoTablasMaestrasForm form = (PCAJGListadoTablasMaestrasForm) formulario;
			userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
			tx = userBean.getTransaction();

			String identificador = form.getIdentificador();
			String codigo = form.getCodigo();
			String descripcion = form.getDescripcion();
			String abreviatura = form.getAbreviatura();
			String nombreTabla = form.getNombreTablaMaestra();	
			String tablaRelacion = form.getTablaRelacion();
			String campoFKSIGA = form.getCampoFKSIGA();
			String[] campoFKSIGAvalue = form.getCampoFKSIGAvalue();			

			String sFechaModificacion = MasterBean.C_FECHAMODIFICACION;
			String sUsuModif = MasterBean.C_USUMODIFICACION;

			String sSQL = "SELECT IDENTIFICADOR, CODIGO, DESCRIPCION, IDINSTITUCION";
			
			List array = new ArrayList();
			array.add("DESCRIPCION");
			array.add("CODIGO");
			array.add("ABREVIATURA");
			array.add(sFechaModificacion);
			array.add(sUsuModif);
			
						
			sSQL += " FROM " + nombreTabla + " WHERE IDENTIFICADOR = '" + identificador + "' AND IDINSTITUCION = " + getIDInstitucion(request);

			Object[] campos = array.toArray();
			
			RowsContainer rc = new RowsContainer();
			Object[] claves = new Object[2];
			claves[0] = "IDENTIFICADOR";
			claves[1] = "IDINSTITUCION";

			

			//Chequeo si existe una descripcion igual:
			if (this.existeDescripcion(identificador, descripcion, nombreTabla, userBean.getLocation(), userBean.getLanguage())) {
				return error("messages.inserted.descDuplicated", new ClsExceptions("messages.inserted.descDuplicated"), request);
			} else if (this.existeCodigoExterno(identificador, codigo, nombreTabla, userBean.getLocation(), userBean.getLanguage())) {
				return error("messages.inserted.codDuplicated", new ClsExceptions("messages.inserted.codDuplicated"), request);
			} else {
				tx.begin();
				if (rc.findForUpdate(sSQL)) {

					Row row = (Row) rc.get(0);
					row.setCompareData(row.getRow());
					Hashtable htNew = (Hashtable) row.getRow().clone();
					htNew.put("CODIGO", codigo);
					htNew.put("ABREVIATURA", abreviatura);
					
					if (campoFKSIGA != null && !campoFKSIGA.trim().equals("")) {						
						
						//borramos las relaciones						
						Row mRow = new Row();
						mRow.setValue("IDENTIFICADOR", identificador);
						mRow.setValue("IDINSTITUCION", getIDInstitucion(request));			
						row.delete(tablaRelacion, claves);						
						
						//insertamos las relaciones						
						if (campoFKSIGAvalue != null) {
							String sql = null;
							for (int i = 0; i < campoFKSIGAvalue.length; i++) {
								if (campoFKSIGAvalue[i] != null && !campoFKSIGAvalue[i].trim().equals("")) { 
									sql = "INSERT INTO " + tablaRelacion + " (IDINSTITUCION, IDENTIFICADOR, " + campoFKSIGA + ", FECHAMODIFICACION, USUMODIFICACION) VALUES" +
										" ("  + getIDInstitucion(request) + ", " + identificador + ", '" + campoFKSIGAvalue[i] + "', SYSDATE, " + userBean.getUserName() + ")";
									mRow.insertSQL(sql);
								}
							}
						}
					}

					String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, "DESCRIPCION", this.getIDInstitucion(request), identificador);
					if (idRecurso == null) {
						htNew.put("DESCRIPCION", descripcion);
					}
					htNew.put(sFechaModificacion, "SYSDATE");
					htNew.put(sUsuModif, userBean.getUserName());
					row.load(htNew);
					if (row.update(nombreTabla, claves, campos) <= 0) {
						throw new SIGAException("messages.updated.error");
					}

					if (idRecurso != null) {
						String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, "DESCRIPCION", this.getIDInstitucion(request),
								identificador);
						GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
						GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean();
						recCatalogoBean.setDescripcion(descripcion);
						recCatalogoBean.setIdRecurso(idRecurso);
						recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
						if (!admRecCatalogos.update(recCatalogoBean, this.getUserBean(request))) {
							throw new SIGAException("messages.updated.error");
						}
					}

					tx.commit();
					request.setAttribute("mensaje", "messages.updated.success");

				} else {
					tx.rollback();
					request.setAttribute("mensaje", "error.messages.deleted");
				}
			}
		} catch (Exception e) {
			throwExcp("messages.updated.error", e, tx);
		}
		//      request.setAttribute("modal","1");
		//    	request.setAttribute("sinrefresco", "1");			    	
		//      return "exito";*/
		return this.exitoModal("messages.updated.success", request);
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException,
			SIGAException {

		try {
			UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
			PCAJGListadoTablasMaestrasForm form = (PCAJGListadoTablasMaestrasForm) formulario;
			
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String identificador = (String) vOcultos.elementAt(0);			
			String nombreTabla = form.getNombreTablaMaestra();
			String tablaRelacion = form.getTablaRelacion();
			
			UserTransaction tx = userBean.getTransaction();
			tx.begin();

			Object[] claves = new Object[] { "IDENTIFICADOR", "IDINSTITUCION" };
			Row row = new Row();
			row.setValue("IDENTIFICADOR", identificador);
			row.setValue("IDINSTITUCION", getIDInstitucion(request));

			//borramos las relaciones			
			row.delete(tablaRelacion, claves);
			
			if (row.delete(nombreTabla, claves) <= 0) {
				throw new SIGAException("error.messages.deleted");
			}
			
			// /////////////////////////////////////////
			// Multiidioma: Borramos los recursos en gen_recursos_catalogos
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, "DESCRIPCION", getIDInstitucion(request), identificador);
			if (idRecurso != null) {
				GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
				GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean();
				recCatalogoBean.setIdRecurso(idRecurso);
				if (!admRecCatalogos.delete(recCatalogoBean)) {
					throw new SIGAException("error.messages.deleted");
				}
			}

			tx.commit();
			request.setAttribute("mensaje", "messages.deleted.success");

		} catch (Exception e) {
			this.throwExcp("error.messages.application", e, null);
		}
		return "exito";
	}

	private String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable,
			boolean bNuevo) throws ClsExceptions {
		
		PCAJGListadoTablasMaestrasForm form = (PCAJGListadoTablasMaestrasForm) formulario;
		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));

		String identificador = "";
		String sNombreTabla = form.getNombreTablaMaestra();
		String sAliasTabla = form.getAliasTabla();
		String campoFKSIGA = null;
		String tablaRelacion = null;

		String sSQL = "SELECT P.IDTABLAMAESTRASIGA, P.TABLARELACION, IDCAMPOCODIGO AS CAMPOFKSIGA" +
				" FROM PCAJG_TABLAS_MAESTRAS P, GEN_TABLAS_MAESTRAS S" +
				" WHERE P.IDENTIFICADOR = '" + sNombreTabla + "'" + " AND P.IDTABLAMAESTRASIGA = S.IDTABLAMAESTRA (+)";
		RowsContainer rcTM = new RowsContainer();
		rcTM.find(sSQL);

		if (rcTM.size() > 0) {
			Row rowTM = (Row) rcTM.get(0);
			String idTablaMaestraSiga = rowTM.getString("IDTABLAMAESTRASIGA");
			tablaRelacion = rowTM.getString("TABLARELACION");
			if (tablaRelacion == null || tablaRelacion.trim().equals("")) {
				tablaRelacion = null;
			}
			
			campoFKSIGA = rowTM.getString("CAMPOFKSIGA");
			if (campoFKSIGA == null || campoFKSIGA.trim().equals("")) {
				campoFKSIGA = null;
			}

			if (idTablaMaestraSiga != null && !idTablaMaestraSiga.trim().equals("") && tablaRelacion != null) {
				Hashtable hash = new Hashtable();
				hash.put("IDTABLAMAESTRA", idTablaMaestraSiga);
				GenTablasMaestrasAdm genTablasMaestrasAdm = new GenTablasMaestrasAdm(userBean);
				Vector vector = genTablasMaestrasAdm.selectByPK(hash);
				if (vector != null && vector.size() > 0) {
					GenTablasMaestrasBean genTablasMaestrasBean = (GenTablasMaestrasBean) vector.get(0);
					sSQL = "SELECT " + genTablasMaestrasBean.getIdCampoCodigo() + " AS ID" + ", F_SIGA_GETRECURSO("
							+ genTablasMaestrasBean.getIdCampoDescripcion() + ", " + userBean.getLanguage() + ")" + " AS DESCRIPCION" +
							" FROM " + genTablasMaestrasBean.getIdTablaMaestra() +
							" WHERE 1=1"
							+ (genTablasMaestrasBean.getLocal().equals("S") ? (" AND IDINSTITUCION = " + userBean.getLocation()) : "") + "ORDER BY "
							+ genTablasMaestrasBean.getIdCampoDescripcion();
					RowsContainer rowsContainer = new RowsContainer();
					rowsContainer.find(sSQL);
					request.setAttribute("selectAsociacionSIGA", rowsContainer);
				}
			}
			
		}

		if (!bNuevo) {
			Vector vOcultos = form.getDatosTablaOcultos(0);
			identificador = (String) vOcultos.elementAt(0);			

			sSQL = "SELECT P.IDENTIFICADOR, P.CODIGO" +
					", F_SIGA_GETRECURSO(P.DESCRIPCION, " + this.getUserBean(request).getLanguage() + ") AS DESCRIPCION" +
					", P.ABREVIATURA" +
//					", " + campoFKSIGA + " AS CAMPOFKSIGAVALUE" + TODO
					" FROM " + sNombreTabla + " P" +
					" WHERE P.IDENTIFICADOR = '" + identificador + "'";

			sSQL += " AND P.IDINSTITUCION = " + userBean.getLocation();

			RowsContainer rc = new RowsContainer();
			rc.find(sSQL);

			Row row = (Row) rc.get(0);
			request.setAttribute("datos", row);
			
			sSQL = "SELECT P." + campoFKSIGA + " AS CAMPOFKSIGAVALUE" +
					" FROM " + tablaRelacion + " P" +
					" WHERE P.IDENTIFICADOR = '" + identificador + "'";
			sSQL += " AND P.IDINSTITUCION = " + userBean.getLocation();
			
			
			List campoFKSIGAValue = new ArrayList();
			rc.find(sSQL);
			Row row2 = (Row) rc.get(0);
			if (row2 != null) {
				for (int i = 0; i < rc.size(); i++) {
					row2 = (Row)rc.get(i);
					campoFKSIGAValue.add(row2.getString("CAMPOFKSIGAVALUE"));
				}
			}
			request.setAttribute("CAMPOFKSIGAVALUE", campoFKSIGAValue);
		}

		request.setAttribute("campoFKSIGA", campoFKSIGA);
		request.setAttribute("tablaRelacion", tablaRelacion);
		request.setAttribute("editable", bEditable ? "1" : "0");
		request.setAttribute("nuevo", bNuevo ? "1" : "0");
		request.setAttribute("nombreTabla", sNombreTabla);
		request.setAttribute("aliasTabla", sAliasTabla);

		return "mostrar";
	}

	//Obtengo un nuevo id de forma secuencial:
	private String getNuevoID(String idInstitucion, String sNombreTabla) throws ClsExceptions {
		String id = null, sSQL = null;

		sSQL = "SELECT NVL(MAX(IDENTIFICADOR),0)+1 AS CODIGONUEVO " + " FROM " + sNombreTabla;
		sSQL += " WHERE IDINSTITUCION = " + idInstitucion;

		RowsContainer rc = new RowsContainer();
		rc.findForUpdate(sSQL);
		Row row = (Row) rc.get(0);
		id = (String) row.getString("CODIGONUEVO");

		return id;
	}

	/**
	 * 
	 * @param identificador
	 * @param descripcion
	 * @param sNombreTabla
	 * @param idInstitucion
	 * @param lenguaje
	 * @return
	 * @throws ClsExceptions
	 */
	private boolean existeDescripcion(String identificador, String descripcion, String sNombreTabla, String idInstitucion, String lenguaje)
			throws ClsExceptions {
		String sSQL = null;
		boolean existe = false;

		sSQL = "SELECT count(1) AS DESCRIPCION" + " FROM " + sNombreTabla + " WHERE upper (f_siga_getrecurso(DESCRIPCION," + lenguaje + ")) = '"
				+ descripcion.toUpperCase().trim() + "'";

		sSQL += " AND IDENTIFICADOR <> '" + identificador + "'";
		sSQL += " AND IDINSTITUCION = " + idInstitucion;

		RowsContainer rc = new RowsContainer();
		rc.findForUpdate(sSQL);
		Row row = (Row) rc.get(0);

		if (row.getString("DESCRIPCION") != null && !row.getString("DESCRIPCION").equals("0"))
			existe = true;

		return existe;
	}

	/**
	 * 
	 * @param identificador
	 * @param codigo
	 * @param sNombreTabla
	 * @param idInstitucion
	 * @param lenguaje
	 * @return
	 * @throws ClsExceptions
	 */

	private boolean existeCodigoExterno(String identificador, String codigo, String sNombreTabla, String idInstitucion, String lenguaje) throws ClsExceptions {
		String sSQL = null;
		boolean existe = false;

		sSQL = "SELECT count(1) AS CODEXT" + " FROM " + sNombreTabla;
		sSQL += " WHERE CODIGO LIKE '" + codigo.trim() + "' ";//Debe ser like pq puede ser un number o un varchar
		sSQL += " AND IDENTIFICADOR <> '" + identificador + "' ";
		sSQL += " AND IDINSTITUCION = " + idInstitucion;

		RowsContainer rc = new RowsContainer();
		rc.find(sSQL);
		Row row = (Row) rc.get(0);

		if (row.getString("CODEXT") != null && !row.getString("CODEXT").equals("0"))
			existe = true;

		return existe;
	}
}