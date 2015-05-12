package com.siga.gratuita.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsDelitoBean;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsDelitosAsistenciaBean;
import com.siga.beans.ScsDelitosEJGAdm;
import com.siga.beans.ScsTipoAsistenciaColegioBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.PestanaDelitoAsistenciaForm;

/**
 * @author david.sanchez
 * @since 25/01/2006
 *
 */
public class PestanaDelitoAsistenciaAction extends MasterAction {
	
	/**
	 * Accion por defecto: abre el jsp inicial y recupera los datos de la pestanha. 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		PestanaDelitoAsistenciaForm miForm = (PestanaDelitoAsistenciaForm)formulario;
		String idDelito = null;			
		String anio=null, numero=null, idInstitucion=null;
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");

		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(this.getUserBean(request));
			
			// Obtengo de la pestanha el anio, numero:			
			numero = (String)request.getParameter("NUMERO");
			anio = (String)request.getParameter("ANIO");
			miForm.setJsonVolver(request.getParameter("jsonVolver"));
			
			//Busco el delito:
			String delito = "";
			String where = " WHERE "+ScsAsistenciasBean.C_ANIO+"="+anio+
						   " AND "+ScsAsistenciasBean.C_NUMERO+"="+numero+
						   " AND "+ScsAsistenciasBean.C_IDINSTITUCION+"="+usr.getLocation();
			Vector vAsistencias = admAsistencias.select(where);
			ScsAsistenciasBean beanAsistencia = (ScsAsistenciasBean)vAsistencias.get(0);
			delito = beanAsistencia.getDelitosImputados();
			Hashtable hashAsistenciaOriginal = beanAsistencia.getOriginalHash();
			request.getSession().setAttribute("hashAsistenciaOriginal",hashAsistenciaOriginal);
			
			// Almaceno los parametros en el formulario:			
			miForm.setNumero(new Integer(numero));
			miForm.setAnio(new Integer(anio));
			miForm.setDelito(delito);
			
		} catch (Exception e){
			throwExcp("messages.general.error",e,null);
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "inicio";
	}

	/**
	 * Accion por defecto: abre el jsp inicial y recupera los datos de la pestanha. 
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		PestanaDelitoAsistenciaForm miForm = (PestanaDelitoAsistenciaForm)formulario;
		String idDelito = null;			
		String anio=null, numero=null, idInstitucion=null;

		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(this.getUserBean(request));
			
			// Obtengo de la pestanha el anio, numero:			
			numero = miForm.getNumero().toString();
			anio = miForm.getAnio().toString();
			
			//Busco el delito:
			String delito = "";
			String where = " WHERE "+ScsAsistenciasBean.C_ANIO+"="+anio+
						   " AND "+ScsAsistenciasBean.C_NUMERO+"="+numero+
						   " AND "+ScsAsistenciasBean.C_IDINSTITUCION+"="+usr.getLocation();
			Vector vAsistencias = admAsistencias.select(where);
			ScsAsistenciasBean beanAsistencia = (ScsAsistenciasBean)vAsistencias.get(0);
			delito = beanAsistencia.getDelitosImputados();
			Hashtable hashAsistenciaOriginal = beanAsistencia.getOriginalHash();
			request.getSession().setAttribute("hashAsistenciaOriginal",hashAsistenciaOriginal);
			
			// Almaceno los parametros en el formulario:			
			miForm.setNumero(new Integer(numero));
			miForm.setAnio(new Integer(anio));
			miForm.setDelito(delito);
		} catch (Exception e){
			throwExcp("messages.general.error",e,null);
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "inicio";
	}

	/**
	 * Busca los delitos de una Asistencia concreta. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoAsistenciaForm miForm = (PestanaDelitoAsistenciaForm)formulario;		
		Integer anio, numero, idInstitucion;

		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsDelitosAsistenciaAdm admDelitoAsistencia = new ScsDelitosAsistenciaAdm(this.getUserBean(request));
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			numero = miForm.getNumero();
			anio = miForm.getAnio();	
			
			Vector vDelitosAsistencia = admDelitoAsistencia.getDelitosAsitencia(idInstitucion,anio,numero,usr.getLanguage());
			request.setAttribute("vDelitosAsistencia",vDelitosAsistencia);
		} catch (Exception e){
			throwExcp("messages.general.error",e,null);
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "lista";
	}

	/**
	 * Mapeo a la ventana modal para crear un nuevo delito de la Asistencia.
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "nuevo";
	}

	/**
	 * Inserta un nuevo delito a una Asistencia concreta.	 
	 *  
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * @throws ClsExceptions
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoAsistenciaForm miForm = (PestanaDelitoAsistenciaForm)formulario;		
		Integer anio, numero, idInstitucion, idDelito;
		UserTransaction tx = null;
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsDelitosAsistenciaAdm admDelitoAsistencia = new ScsDelitosAsistenciaAdm(this.getUserBean(request));
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			idDelito = miForm.getIdDelito();
			
			ScsDelitosAsistenciaBean beanDelitoAsistencia = new ScsDelitosAsistenciaBean();
			beanDelitoAsistencia.setAnio(anio);
			beanDelitoAsistencia.setIdDelito(idDelito);
			beanDelitoAsistencia.setIdInstitucion(idInstitucion);
			beanDelitoAsistencia.setNumero(numero);
			beanDelitoAsistencia.setFechaMod("SYSDATE");
			beanDelitoAsistencia.setUsuMod(new Integer(usr.getUserName()));
						
			
			if(UtilidadesString.stringToBoolean(sEsFichaColegial)){
				Hashtable<String, Object> asistenciaHashtable = new Hashtable<String, Object>();
				asistenciaHashtable.put(ScsAsistenciasBean.C_ANIO,anio);
				asistenciaHashtable.put(ScsAsistenciasBean.C_NUMERO,numero);
				asistenciaHashtable.put(ScsAsistenciasBean.C_IDINSTITUCION,idInstitucion);
				
				asistenciaHashtable.put(ScsDelitosAsistenciaBean.C_IDDELITO,idDelito);
				ScsAsistenciasAdm scsAsistenciaAdm = new ScsAsistenciasAdm(usr);
				Hashtable<String, Object> asistenciaOriginalHashtable = scsAsistenciaAdm.getHashAsistenciaOriginalParaHistorico(asistenciaHashtable,false, usr);
				String[] campos = {ScsDelitosAsistenciaBean.C_IDDELITO};
				Map<String,Hashtable<String, Object>> fksAsistenciaMap = new HashMap<String, Hashtable<String,Object>>(); 
				//Como el turno es obligarotio
				Hashtable<String, Object> fksAsistenciaHashtable = new Hashtable<String, Object>();
				
				fksAsistenciaHashtable.put("TABLA_FK", ScsDelitoBean.T_NOMBRETABLA);
				fksAsistenciaHashtable.put("SALIDA_FK", ScsDelitoBean.C_DESCRIPCION);
				fksAsistenciaHashtable.put(ScsDelitoBean.C_IDINSTITUCION, idInstitucion);
				fksAsistenciaHashtable.put(ScsDelitoBean.C_IDDELITO, idDelito);
				fksAsistenciaMap.put(ScsDelitosAsistenciaBean.C_IDDELITO,fksAsistenciaHashtable);
				
				asistenciaHashtable.put("fks", fksAsistenciaMap);
				tx.begin();
				admDelitoAsistencia.insertConHistorico(Long.valueOf((String)asistenciaOriginalHashtable.get("IDPERSONACOLEGIADO")), beanDelitoAsistencia,null,campos,asistenciaHashtable);
				tx.commit();
			}else
				admDelitoAsistencia.insert(beanDelitoAsistencia);
			
		} catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return exitoModal("messages.inserted.success",request);
	}

	/**
	 * 
	 * Borra un delito para 1 Asistencia y un idDelitoAsistencia.
	 * 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoAsistenciaForm miForm = (PestanaDelitoAsistenciaForm)formulario;
				
		Integer anio, numero, idInstitucion, idDelito;
		UserTransaction tx = null;
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsDelitosAsistenciaAdm admDelitoAsistencia = new ScsDelitosAsistenciaAdm(this.getUserBean(request));
			
			Vector vOcultos = miForm.getDatosTablaOcultos(0);
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			idDelito = new Integer((String)vOcultos.get(0));
			
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			
			ScsDelitosAsistenciaBean beanDelitoAsistencia = new ScsDelitosAsistenciaBean();
			beanDelitoAsistencia.setAnio(anio);
			beanDelitoAsistencia.setIdDelito(idDelito);
			beanDelitoAsistencia.setIdInstitucion(idInstitucion);
			beanDelitoAsistencia.setNumero(numero);
						
			if(UtilidadesString.stringToBoolean(sEsFichaColegial)){
				Hashtable<String, Object> asistenciaHashtable = new Hashtable<String, Object>();
				asistenciaHashtable.put(ScsAsistenciasBean.C_ANIO,anio);
				asistenciaHashtable.put(ScsAsistenciasBean.C_NUMERO,numero);
				asistenciaHashtable.put(ScsAsistenciasBean.C_IDINSTITUCION,idInstitucion);
				
				asistenciaHashtable.put(ScsDelitosAsistenciaBean.C_IDDELITO,idDelito);
				ScsAsistenciasAdm scsAsistenciaAdm = new ScsAsistenciasAdm(usr);
				Hashtable<String, Object> asistenciaOriginalHashtable = scsAsistenciaAdm.getHashAsistenciaOriginalParaHistorico(asistenciaHashtable,false, usr);
				
				String[] campos = {ScsDelitosAsistenciaBean.C_IDDELITO};
				Map<String,Hashtable<String, Object>> fksAsistenciaMap = new HashMap<String, Hashtable<String,Object>>(); 
				//Como el turno es obligarotio
				Hashtable<String, Object> fksAsistenciaHashtable = new Hashtable<String, Object>();
				
				fksAsistenciaHashtable.put("TABLA_FK", ScsDelitoBean.T_NOMBRETABLA);
				fksAsistenciaHashtable.put("SALIDA_FK", ScsDelitoBean.C_DESCRIPCION);
				fksAsistenciaHashtable.put(ScsDelitoBean.C_IDINSTITUCION, idInstitucion);
				fksAsistenciaHashtable.put(ScsDelitoBean.C_IDDELITO, idDelito);
				fksAsistenciaMap.put(ScsDelitosAsistenciaBean.C_IDDELITO,fksAsistenciaHashtable);
				
				asistenciaHashtable.put("fks", fksAsistenciaMap);
				tx.begin();
				admDelitoAsistencia.deleteConHistorico(Long.valueOf((String)asistenciaOriginalHashtable.get("IDPERSONACOLEGIADO")), beanDelitoAsistencia,null,campos,asistenciaHashtable);
				tx.commit();
			}else
				admDelitoAsistencia.delete(beanDelitoAsistencia);
			
		} catch (Exception e){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return exitoRefresco("messages.deleted.success",request);
	}
	
	/**
	 * Modifica el delito de la asistencia relacionada.	 
	 *  
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * @throws ClsExceptions
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoAsistenciaForm miForm = (PestanaDelitoAsistenciaForm)formulario;		
		Integer anio, numero, idInstitucion;
		String delito=null;
		
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		UserTransaction tx = null;
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(this.getUserBean(request));
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			delito = miForm.getDelito();
			
			Hashtable hashAsistencia = new Hashtable();
			hashAsistencia.put(ScsAsistenciasBean.C_ANIO,anio);
			hashAsistencia.put(ScsAsistenciasBean.C_NUMERO,numero);
			hashAsistencia.put(ScsAsistenciasBean.C_IDINSTITUCION,usr.getLocation());
			hashAsistencia.put(ScsAsistenciasBean.C_DELITOSIMPUTADOS,delito);
			
			String[] campos = {ScsAsistenciasBean.C_DELITOSIMPUTADOS};

			if(UtilidadesString.stringToBoolean(sEsFichaColegial)){
				Hashtable<String, Object> asistenciaOriginalHashtable = admAsistencias.getHashAsistenciaOriginalParaHistorico(hashAsistencia,false, usr);
				tx.begin();
				admAsistencias.updateDirectHistorico( Long.valueOf((String)asistenciaOriginalHashtable.get("IDPERSONACOLEGIADO")),hashAsistencia,null,campos,asistenciaOriginalHashtable);
				tx.commit();
			}else
				admAsistencias.updateDirect(hashAsistencia,null,campos);
			
			
			
			
			
		} catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return exitoRefresco("messages.updated.success",request);
	}
	
}