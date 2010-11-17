package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import javax.transaction.UserTransaction;

import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.HelperInformesAdm;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsCabeceraGuardiasBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsPermutaGuardiasAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ValidarVolantesGuardiasForm;

/**
 * Validacion de volantes de cabeceras de guardias
 * 
 * @author RGG
 * @since 08/1/2008 
 * @version 1.0
 */
public class ValidarVolantesGuardiasAction extends MasterAction {


	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	    String forward = "abrir";
	    try {
	        UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			
		    ValidarVolantesGuardiasForm miForm = (ValidarVolantesGuardiasForm) formulario;
	
	
		} 
		catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		return forward;
	}

	/* Realiza la busqueda de colegiado para el filtro
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	    String forward = "resultadoColegiado";
	    try { 
	        UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			
		    ValidarVolantesGuardiasForm miForm = (ValidarVolantesGuardiasForm) formulario;
		    String nColegiado = miForm.getNumColegiado();
		    if (nColegiado!=null && !nColegiado.equals("")) {
			    CenClienteAdm a = new CenClienteAdm(usr);
			    Vector res = a.getClientePorNColegiado(usr.getLocation(),nColegiado);
			    if (res!=null && res.size()>0) {
			        Hashtable ht = (Hashtable) res.get(0);
			        request.setAttribute("datosCliente",ht);
			    }
		    }
		} 
		catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		return forward;
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	    String forward = "";
	    UserTransaction tx = null;
	    try {
	        UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	        tx=usr.getTransaction();
		    ValidarVolantesGuardiasForm miForm = (ValidarVolantesGuardiasForm) formulario;

		    ScsCabeceraGuardiasAdm adm = new ScsCabeceraGuardiasAdm(usr);
		    String idinstitucion="";
		    String idturno="";
		    String idguardia="";
		    String idcalendarioguardias="";
		    String idpersona="";
		    String fechainicio="";
		    String fechainicioPK="";
		    
		    tx.begin();
		    
		    ///////////////////////////
		    // VALIDAR
		    String datosValidar = miForm.getDatosValidar();
		    if (datosValidar != null && !datosValidar.equals("")) {
			    GstStringTokenizer st = new GstStringTokenizer(datosValidar,"##");
			    while (st.hasMoreElements()) {
			        String reg = (String)st.nextElement();
				    GstStringTokenizer st2 = new GstStringTokenizer(reg,"@@");
				    if (st2.hasMoreElements()) {
				        idinstitucion = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        idturno = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        idguardia = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        idcalendarioguardias = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        idpersona = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        fechainicio = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        fechainicioPK = (String)st2.nextElement();
				    }

				    // Si vengo de VOLANTE_EXPRES
				    if (miForm.getDesde()!=null && miForm.getDesde().equalsIgnoreCase("VOLANTE_EXPRES")) {
						Hashtable h = new Hashtable();
						String fechafin = GstDate.getApplicationFormatDate("", fechainicio);
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDPERSONA, idpersona);
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, fechafin);
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDTURNO, idturno);
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, (idguardia.split(",")[0]));
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idinstitucion);
						ScsGuardiasColegiadoAdm admAux = new ScsGuardiasColegiadoAdm (this.getUserBean(request));
						Vector vGuardias = admAux.select(h);
						if (vGuardias == null && vGuardias.size() != 1) {
				            throw new ClsExceptions("La guardia seleccionada tiene varios calendarios asociados");
						}
						if (vGuardias!=null && vGuardias.size()>0){
						ScsGuardiasColegiadoBean b = (ScsGuardiasColegiadoBean) vGuardias.get(0);
						idcalendarioguardias = ""+b.getIdCalendarioGuardias();
						
						fechainicio = b.getFechaInicio();
						}
				    }
				    
				    // actualizo datos
				    Hashtable ht = new Hashtable();
				    ht.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION,idinstitucion);
				    ht.put(ScsCabeceraGuardiasBean.C_IDTURNO,idturno);
				    ht.put(ScsCabeceraGuardiasBean.C_IDGUARDIA,idguardia);
				    ht.put(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS,idcalendarioguardias);
				    ht.put(ScsCabeceraGuardiasBean.C_IDPERSONA,idpersona);
				    //ht.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,fechainicio);
				    if(fechainicioPK != null)
				    	ht.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,fechainicioPK);
				    else
				    	ht.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,fechainicio);
				    Vector v = adm.selectByPK(ht);
				    if (v!=null && v.size()>0) {
				        ScsCabeceraGuardiasBean b = (ScsCabeceraGuardiasBean) v.get(0);
				        b.setValidado(ClsConstants.DB_TRUE);
				        if (!adm.update(b)) {
				            throw new ClsExceptions("Error al actualizar validado en cabecera de guardias: "+adm.getError());
				        }
				    }
			    }
		    }

		    ///////////////////////////
		    // INVALIDAR
		    String datosInvalidar = miForm.getDatosBorrar();
		    if (datosInvalidar != null && !datosInvalidar.equals("")) {
			    GstStringTokenizer st = new GstStringTokenizer(datosInvalidar,"##");
			    while (st.hasMoreElements()) {
			        String reg = (String)st.nextElement();
				    GstStringTokenizer st2 = new GstStringTokenizer(reg,"@@");
				    if (st2.hasMoreElements()) {
				        idinstitucion = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        idturno = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        idguardia = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        idcalendarioguardias = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        idpersona = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        fechainicio = (String)st2.nextElement();
				    }
				    if (st2.hasMoreElements()) {
				        fechainicioPK = (String)st2.nextElement();
				    }
				    // actualizo datos
				    Hashtable ht = new Hashtable();
				    ht.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION,idinstitucion);
				    ht.put(ScsCabeceraGuardiasBean.C_IDTURNO,idturno);
				    ht.put(ScsCabeceraGuardiasBean.C_IDGUARDIA,idguardia);
				    ht.put(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS,idcalendarioguardias);
				    ht.put(ScsCabeceraGuardiasBean.C_IDPERSONA,idpersona);
				    //ht.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,fechainicio);
				    if(fechainicioPK != null)
				    	ht.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,fechainicioPK);
				    else
				    	ht.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,fechainicio);
				    Vector v = adm.selectByPK(ht);
				    if (v!=null && v.size()>0) {
				        ScsCabeceraGuardiasBean b = (ScsCabeceraGuardiasBean) v.get(0);
				        b.setValidado(ClsConstants.DB_FALSE);
				        if (!adm.update(b)) {
				            throw new ClsExceptions("Error al actualizar validado a false en cabecera de guardias: "+adm.getError());
				        }
				    }
			    }
		    }

		    tx.commit();


		    forward=this.exitoRefresco("messages.updated.success",request);
		} 
		catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
		}
		
		return forward;
	}
	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		Hashtable miHash = new Hashtable();
		UsrBean usr = null;
		String forward = "buscar";
	    ValidarVolantesGuardiasForm miForm = (ValidarVolantesGuardiasForm) formulario;
		ScsGuardiasColegiadoAdm admGuardiaColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm admPermutaguardias = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		try {
		
		
		TreeMap tmResultado = new TreeMap();
		Vector v_guardias = new Vector ();
		
		String pl = ""; //Valor devuelto por el PL de Permutas
		
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			

			//Datos necesarios para la consulta
			String idinstitucion = usr.getLocation();
			String idpersonaParam = miForm.getIdPersona();
			String idTurnoForm = miForm.getIdTurno();
			if (idTurnoForm.indexOf(",")!=-1) {
				idTurnoForm = idTurnoForm.substring(idTurnoForm.indexOf(",")+1,idTurnoForm.length());
			}
			String idGuardia = miForm.getIdGuardia();
			
			String numeroColegiadoParam = miForm.getNumColegiado();
			String fechaDesde = miForm.getBuscarFechaDesde();
			String fechaHasta = miForm.getBuscarFechaHasta();
			String pedienteValidar = (miForm.getPendienteValidar()==null)?"":miForm.getPendienteValidar();
			

			miHash.put("IDINSTITUCION",idinstitucion);
			miHash.put("IDPERSONA",idpersonaParam);
			miHash.put("IDTURNO",idTurnoForm);
			miHash.put("IDGUARDIA",idGuardia);
			miHash.put("NUMCOLEGIADO",numeroColegiadoParam);
			miHash.put("BUSCARFECHADESDE",fechaDesde);
			miHash.put("BUSCARFECHAHASTA",fechaHasta);
			miHash.put("PENDIENTEVALIDAR",pedienteValidar);
			
			//Busqueda de colegiados. Obtengo el nombre, numero de colegiado, observaciones y las fechas de inicio y fin
			v_guardias = admGuardiaColegiado.selectGenerico(admGuardiaColegiado.buscarColegiados(miHash));
			
			
			int i = 0;
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			while (i < v_guardias.size()) {
				Hashtable registro = (Hashtable)v_guardias.get(i);
				String nombre = (String)registro.get(CenPersonaBean.C_NOMBRE);
				String rowId = (String)registro.get("ROWIND");
				//fechaInicio = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_FECHA_INICIO);
				
				String fechaInicioPK = (String)registro.get("FECHA_INICIO_PK");
				String fechaFin = (String)registro.get(ScsCabeceraGuardiasBean.C_FECHA_FIN);
				String idTurno = (String)registro.get(ScsCabeceraGuardiasBean.C_IDTURNO);
				String idguardia = (String)registro.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
				String idpersona = (String)registro.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
				String idCalendarioGuardias = (String)registro.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
				
				String fechaInicioPKBind = GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPK); 
				
				Hashtable htCodigo = new Hashtable();
				htCodigo.put(new Integer(1), idinstitucion);
				htCodigo.put(new Integer(2), idTurno);
				htCodigo.put(new Integer(3), idguardia);
				htCodigo.put(new Integer(4), idCalendarioGuardias);
				htCodigo.put(new Integer(5), idpersona);
				htCodigo.put(new Integer(6), fechaInicioPKBind);
				
							
				
				
				
				//consulta+= " ,F_SIGA_TIENE_ACTS_VALIDADAS(guard.IDINSTITUCION,guard.IDTURNO,guard.IDGUARDIA,guard.IDCALENDARIOGUARDIAS,guard.IDPERSONA,guard.FECHAINICIO) AS ACT_VALIDADAS";
				//ACT_VALIDADAS
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_TIENE_ACTS_VALIDADAS", "ACT_VALIDADAS"));

				//F_SIGA_ES_MODIFICABLE_GUARDIAS(guard.IDINSTITUCION,guard.IDTURNO, guard.IDGUARDIA, guard.IDCALENDARIOGUARDIAS,
                //      guard.IDPERSONA, guard.FECHAINICIO) as ESMODIFICABLE
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_ES_MODIFICABLE_GUARDIAS", "ESMODIFICABLE"));
				
				
				
				
				htCodigo = new Hashtable();
				htCodigo.put(new Integer(1), idinstitucion);
				htCodigo.put(new Integer(2), idTurno);
				htCodigo.put(new Integer(3), idguardia);
				htCodigo.put(new Integer(4), idpersona);
				htCodigo.put(new Integer(5), fechaInicioPKBind);
				htCodigo.put(new Integer(6), idCalendarioGuardias);
				
				//FECHAINICIOPERMUTA
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htCodigo, "F_SIGA_FECHAINISOLICITANTE", "FECHAINICIOPERMUTA"));
				String fInicioPermuta = (String)registro.get("FECHAINICIOPERMUTA");
				//Si la fecha de inicio del solicitante es nula miramos  la fecha de inicio del confirmador
				if(fInicioPermuta==null||fInicioPermuta.trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAINICONFIRMADOR", "FECHAINICIOPERMUTA"));
					fInicioPermuta = (String)registro.get("FECHAINICIOPERMUTA");
					//Si la fecha de inicio del confirmador es nula ponemos como fecha de inicio de la permuta 
					//la fecha de inicio real
					if(fInicioPermuta==null||fInicioPermuta.trim().equals("")){
						fInicioPermuta = fechaInicioPK;
						registro.put("FECHAINICIOPERMUTA", fInicioPermuta);
					}
					
				}
				//FECHAFINPERMUTA
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htCodigo, "F_SIGA_FECHAFINSOLICITANTE", "FECHAFINPERMUTA"));
				String fFinPermuta = (String)registro.get("FECHAFINPERMUTA");
				//Si la fecha de fin del solicitante es nula miramos  la fecha de fin del confirmador
				if(fFinPermuta==null||fFinPermuta.trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAFINCONFIRMADOR", "FECHAFINPERMUTA"));
					fFinPermuta = (String)registro.get("FECHAFINPERMUTA");
					//Si la fecha de fin del confirmador es nula ponemos como fecha de fin de la permuta 
					//la fecha de fin real
					if(fFinPermuta==null||fFinPermuta.trim().equals("")){
						fFinPermuta = fechaFin;
						registro.put("FECHAFINPERMUTA", fFinPermuta);
					}
					
				}
				
				//FECHAINICIO
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htCodigo, "F_SIGA_FECHAORIGSOLICITANTE", "FECHAINICIO"));
				String fInicio = (String)registro.get("FECHAINICIO");
				//Si la fecha de origen del solicitante es nula miramos  la fecha de origen del confirmador
				if(fInicio==null||fInicio.trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAORIGCONFIRMADOR", "FECHAINICIO"));
					fInicio = (String)registro.get("FECHAINICIO");
					//Si la fecha de origen del confirmador es nula ponemos como fecha de origen de la permuta 
					//la fecha de origen real
					if(fInicio==null||fInicio.trim().equals("")){
						fInicio = fechaInicioPK;
						registro.put("FECHAINICIO", fInicio);
					}
					
				}
				//NUMEROPERMUTA 
                helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htCodigo, "F_SIGA_NUMERO", "NUMEROPERMUTA"));

                
				
				htCodigo = new Hashtable();
				htCodigo.put(new Integer(1), idinstitucion);
				htCodigo.put(new Integer(2), idpersona);
				
				//F_SIGA_CALCULONCOLEGIADO(coleg.IDINSTITUCION, coleg.IDPERSONA) as NCOLEGIADO
				//NCOLEGIADO
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htCodigo, "F_SIGA_CALCULONCOLEGIADO", "NCOLEGIADO"));
								
				String numeroColegiado = (String)registro.get(CenColegiadoBean.C_NCOLEGIADO);
				
				String numeroPermuta = (String)registro.get("NUMEROPERMUTA");
				if (numeroPermuta==null || numeroPermuta.equals("")){
					numeroPermuta="NINGUNO";
				}
				
				
				
				
				//Chequeo si existe una permuta de esa persona como solicitante o como confirmador
				//Consulta como solicitante

				//Ejecuto el PL de Permutas que me dice el tipo de Permuta posible:				
				pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idTurno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fInicio));
				if (pl.equals("5")){//si buscando por la fecha de inicio (para el caso en el que todavia no se haya confirmado la solicitud de la permuta) devuelve el valor "5" (pendiente de permutar) volvemos a ejecutar
					                // el procedimiento pasando la fecha de inicio de permuta (para el caso en el que ya se haya 
					                // confirmado la permuta) por si sigue devolviendo "5" o devuelve otro
					                // valor como "3" (guardia permutada), como "2" (Permuta solicitada) o "4" (Pendiente de confirmar).
				
				    pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idTurno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fInicioPermuta));
				} 
				
				//Inserto los datos a visualizar en el JSP
				Hashtable nueva = new Hashtable();
				nueva.put("FECHAINICIO",fInicio);
				nueva.put("FECHA_INICIO_PK",fechaInicioPK);
				nueva.put("FECHAFIN",fechaFin);			
				nueva.put("FECHAINICIOPERMUTA",fInicioPermuta);
				nueva.put("FECHAFINPERMUTA",fFinPermuta);
				//JTA HE MIRADO EN LA JSP Y NO HACE FALTA
				//nueva.put("FECHAPERMUTA",fechaPermuta);
				nueva.put("NUMEROPERMUTA",numeroPermuta);
				nueva.put("NUMEROCOLEGIADO",numeroColegiado);
				nueva.put("NOMBRE",nombre);
				nueva.put("IDPERSONA",idpersona);
				nueva.put("IDINSTITUCION",idinstitucion);
				nueva.put("IDTURNO",idTurno);
				nueva.put("IDGUARDIA",idguardia);
				nueva.put("IDCALENDARIOGUARDIAS",(String)registro.get("IDCALENDARIOGUARDIAS"));
				nueva.put("NOMTURNO",(String)registro.get("NOMTURNO"));
				nueva.put("VALIDADO",(String)registro.get("VALIDADO"));
				nueva.put("NOMGUARDIA",(String)registro.get("NOMGUARDIA"));
				nueva.put("ESMODIFICABLE",(String)registro.get("ESMODIFICABLE"));
				nueva.put("ACT_VALIDADAS",(String)registro.get("ACT_VALIDADAS"));
				
				
				nueva.put("PL",pl);
				String key = fInicioPermuta+rowId;
				if(tmResultado.containsKey(key)) {
					//System.out.println("key:"+key);
				}
				tmResultado.put(key,nueva);	
				i++;	
			}//Fin del while

			request.setAttribute("resultado",new Vector(tmResultado.values()));

			request.setAttribute("IDINSTITUCION",idinstitucion);
			request.setAttribute("IDTURNO",idTurnoForm);
			request.setAttribute("IDGUARDIA",idGuardia);
			//JTA Se pasaba como parametro "". Lo dejo por si no esuviera controlado el NullPointerException
			request.setAttribute("IDCALENDARIOGUARDIAS","");
		} 
		catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		return forward;
	}
	
	protected String buscarOld(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		Hashtable miHash = new Hashtable();
		UsrBean usr = null;
		String forward = "buscar";
	    ValidarVolantesGuardiasForm miForm = (ValidarVolantesGuardiasForm) formulario;
		ScsGuardiasColegiadoAdm admGuardiaColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm admPermutaguardias = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		try {
		

		String idcalendarioguardias="", idinstitucion="", idturno="", idguardia="";
		String idpersona = "", fechaInicio="", fechaFin="", fechaPermuta="", numeroColegiado="";
		String fechaInicioPermuta="", fechaFinPermuta="";
		String fechaInicioPK="";
		Vector v_resultado = new Vector ();
		Vector v_guardias = new Vector ();
		Vector v_solicitante = new Vector();
		Vector v_confirmador = new Vector();
		Hashtable hashSolicitante = new Hashtable();
		Hashtable hashConfirmador = new Hashtable();
		String numeroLetrados="", numeroSustitutos="", numero="";
		String pl = ""; //Valor devuelto por el PL de Permutas
		
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			

			//Datos necesarios para la consulta
			idinstitucion = usr.getLocation();
			String idpersonaParam = miForm.getIdPersona();
			String idTurno = miForm.getIdTurno();
			if (idTurno.indexOf(",")!=-1) {
			    idTurno = idTurno.substring(idTurno.indexOf(",")+1,idTurno.length());
			}
			String idGuardia = miForm.getIdGuardia();
			
			String numeroColegiadoParam = miForm.getNumColegiado();
			String fechaDesde = miForm.getBuscarFechaDesde();
			String fechaHasta = miForm.getBuscarFechaHasta();
			String pedienteValidar = (miForm.getPendienteValidar()==null)?"":miForm.getPendienteValidar();
			

			miHash.put("IDINSTITUCION",idinstitucion);
			miHash.put("IDPERSONA",idpersonaParam);
			miHash.put("IDTURNO",idTurno);
			miHash.put("IDGUARDIA",idGuardia);
			miHash.put("NUMCOLEGIADO",numeroColegiadoParam);
			miHash.put("BUSCARFECHADESDE",fechaDesde);
			miHash.put("BUSCARFECHAHASTA",fechaHasta);
			miHash.put("PENDIENTEVALIDAR",pedienteValidar);
			
			/*
			miHash.put("IDINSTITUCION",idinstitucion);
			miHash.put("IDTURNO",idturno);
			miHash.put("IDGUARDIA",idguardia);
			miHash.put("IDCALENDARIOGUARDIAS",idcalendarioguardias);
			*/
			
			//Busqueda de colegiados. Obtengo el nombre, numero de colegiado, observaciones y las fechas de inicio y fin
			v_guardias = admGuardiaColegiado.selectGenerico(admGuardiaColegiado.buscarColegiadosOld(miHash));
			
			
			int i = 0;
			while (i < v_guardias.size()) {
				String nombre = (String)((Hashtable)v_guardias.get(i)).get(CenPersonaBean.C_NOMBRE);
				//fechaInicio = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_FECHA_INICIO);
				fechaInicio = (String)((Hashtable)v_guardias.get(i)).get("FECHAINICIO");
				fechaInicioPK = (String)((Hashtable)v_guardias.get(i)).get("FECHA_INICIO_PK");
				fechaFin = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_FECHA_FIN);
				
				idturno = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_IDTURNO);
				idguardia = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
				idpersona = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_IDPERSONA);
				
				numeroColegiado = (String)((Hashtable)v_guardias.get(i)).get(CenColegiadoBean.C_NCOLEGIADO);
				
				fechaInicioPermuta = (String)((Hashtable)v_guardias.get(i)).get("FECHAINICIOPERMUTA");
				fechaFinPermuta = (String)((Hashtable)v_guardias.get(i)).get("FECHAFINPERMUTA");
				numero = (String)((Hashtable)v_guardias.get(i)).get("NUMEROPERMUTA");
				if (numero==null || numero.equals("")){
					numero="NINGUNO";
				}
				
				//Chequeo si existe una permuta de esa persona como solicitante o como confirmador
				//Consulta como solicitante

				//Ejecuto el PL de Permutas que me dice el tipo de Permuta posible:				
				pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio));
				if (pl.equals("5")){//si buscando por la fecha de inicio (para el caso en el que todavia no se haya confirmado la solicitud de la permuta) devuelve el valor "5" (pendiente de permutar) volvemos a ejecutar
					                // el procedimiento pasando la fecha de inicio de permuta (para el caso en el que ya se haya 
					                // confirmado la permuta) por si sigue devolviendo "5" o devuelve otro
					                // valor como "3" (guardia permutada), como "2" (Permuta solicitada) o "4" (Pendiente de confirmar).
				
				    pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPermuta));
				} 
				
				//Inserto los datos a visualizar en el JSP
				Hashtable nueva = new Hashtable();
				nueva.put("FECHAINICIO",fechaInicio);
				nueva.put("FECHA_INICIO_PK",fechaInicioPK);
				nueva.put("FECHAFIN",fechaFin);			
				nueva.put("FECHAINICIOPERMUTA",fechaInicioPermuta);
				nueva.put("FECHAFINPERMUTA",fechaFinPermuta);				
				nueva.put("FECHAPERMUTA",fechaPermuta);
				nueva.put("NUMEROPERMUTA",numero);
				nueva.put("NUMEROCOLEGIADO",numeroColegiado);
				nueva.put("NOMBRE",nombre);
				nueva.put("IDPERSONA",idpersona);
				nueva.put("IDINSTITUCION",idinstitucion);
				nueva.put("IDTURNO",idturno);
				nueva.put("IDGUARDIA",idguardia);
				nueva.put("IDCALENDARIOGUARDIAS",(String)((Hashtable)v_guardias.get(i)).get("IDCALENDARIOGUARDIAS"));
				nueva.put("NOMTURNO",(String)((Hashtable)v_guardias.get(i)).get("NOMTURNO"));
				nueva.put("VALIDADO",(String)((Hashtable)v_guardias.get(i)).get("VALIDADO"));
				nueva.put("NOMGUARDIA",(String)((Hashtable)v_guardias.get(i)).get("NOMGUARDIA"));
				nueva.put("ESMODIFICABLE",(String)((Hashtable)v_guardias.get(i)).get("ESMODIFICABLE"));
				nueva.put("ACT_VALIDADAS",(String)((Hashtable)v_guardias.get(i)).get("ACT_VALIDADAS"));
				
				
				nueva.put("PL",pl);
				v_resultado.add(i,nueva);	
				i++;	
			}//Fin del while

			request.setAttribute("resultado",v_resultado);

			request.setAttribute("IDINSTITUCION",idinstitucion);
			request.setAttribute("IDTURNO",idturno);
			request.setAttribute("IDGUARDIA",idguardia);
			request.setAttribute("IDCALENDARIOGUARDIAS",idcalendarioguardias);
		} 
		catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		return forward;
	}

	
}