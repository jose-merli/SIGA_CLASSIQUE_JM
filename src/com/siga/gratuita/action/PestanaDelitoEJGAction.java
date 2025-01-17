package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsDelitosEJGAdm;
import com.siga.beans.ScsDelitosEJGBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsProcuradorAdm;
import com.siga.beans.ScsProcuradorBean;
import com.siga.beans.ScsEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.PestanaDelitoEJGForm;
import com.siga.ws.CajgConfiguracion;

/**
 * @author david.sanchez
 * @since 24/01/2006
 *
 */
public class PestanaDelitoEJGAction extends MasterAction {
	
	/**
	 * Accion por defecto: abre el jsp inicial y recupera los datos de la pestanha. 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		PestanaDelitoEJGForm miForm = (PestanaDelitoEJGForm)formulario;
		String idDelito = null;			
		String anio=null, numero=null, idInstitucion=null, idTipoEJG=null;
		String idProcurador=null, idInstitucionProcurador=null;
		
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
			ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.getUserBean(request));
//			request.getSession().removeAttribute("DATABACKUP");
			
			
//			Vector v = new Vector ();
			Hashtable miHash = new Hashtable();		
			idInstitucion = 	request.getParameter("IDINSTITUCION").toString();
			
			miHash.put("ANIO",request.getParameter("ANIO").toString());
			miHash.put("NUMERO",request.getParameter("NUMERO").toString());
			miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
			miHash.put("IDINSTITUCION",idInstitucion);	
			
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(idInstitucion));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			
			//TEMPORAL!!!
			GenParametrosAdm admParametros = new GenParametrosAdm(usr);		
			String ejisActivo = admParametros.getValor(idInstitucion, "ECOM", "EJIS_ACTIVO", "0");
			request.setAttribute("EJIS_ACTIVO", ejisActivo);
			String prefijoExpedienteCajg = admParametros.getValor (idInstitucion, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
			request.setAttribute("PREFIJOEXPEDIENTECAJG",prefijoExpedienteCajg);
			
//			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
			
//			try {			
////				v = admEJG.selectPorClave(miHash);
//				request.getSession().setAttribute("DATABACKUP",admEJG.beanToHashTable((ScsEJGBean)v.get(0)));			
//			} catch (Exception e) {
//				   throwExcp("error.general.yanoexiste",e,null);
//			}
			// Obtengo de la pestanha el anio, numero, idTipoEJG:			
			numero = (String)request.getParameter("NUMERO");
			anio = (String)request.getParameter("ANIO");
			idTipoEJG = (String)request.getParameter("IDTIPOEJG");
			
			String consulta="Select (Select Decode(Ejg.Idtipoencalidad, Null,'', f_Siga_Getrecurso(Tipcal.Descripcion, 1)) "+
                              "  From Scs_Tipoencalidad Tipcal Where Tipcal.Idtipoencalidad = Ejg.Idtipoencalidad "+
                              "  And Tipcal.Idinstitucion = Ejg.Calidadidinstitucion) as calidad, Ejg.Idtipoencalidad,Ejg.calidadidinstitucion as CALIDADIDINSTITUCION, "+                              
                            " ejg.guardiaturno_idturno IDTURNO, ejg.IDPRETENSION as IDPRETENSION, ejg.IDINSTITUCION as IDINSTITUCION, " +
					        " ejg.OBSERVACIONES, ejg.DELITOS, ejg.NIG, ejg.IDPROCURADOR, ejg.NUMERODESIGNAPROC, ejg.IDINSTITUCION_PROC, ejg.NUMERO_CAJG," +
					        " ejg.ANIOCAJG, ejg.NUMERODILIGENCIA NUMERODILIGENCIA, ejg.NUMEROPROCEDIMIENTO NUMEROPROCEDIMIENTO,ejg.ANIOPROCEDIMIENTO ANIOPROCEDIMIENTO, ejg.JUZGADO JUZGADO," +
					        " ejg.JUZGADOIDINSTITUCION JUZGADOIDINSTITUCION, ejg.COMISARIA COMISARIA, ejg.COMISARIAIDINSTITUCION COMISARIAIDINSTITUCION," +
					        " ejg.FECHA_DES_PROC,ejg.IDPRECEPTIVO, ejg.IDSITUACION, ejg.IDRENUNCIA "+
					        "  from scs_ejg Ejg " ;
			consulta += " where "+
			"ejg.idtipoejg = " + idTipoEJG + " and ejg.idinstitucion = " + idInstitucion + " and ejg.anio = " + anio + " and ejg.numero = " + numero;	
            
			
			Vector resultado = admBean.selectGenerico(consulta);
			Hashtable ejg = (Hashtable)resultado.get(0);			
			idProcurador=(String)ejg.get("IDPROCURADOR");
			idInstitucionProcurador=(String)ejg.get("IDINSTITUCION_PROC");
			UtilidadesHash.set(ejg,"NUMERO",numero);
			UtilidadesHash.set(ejg,"ANIO",anio);			
			UtilidadesHash.set(ejg,"IDTIPOEJG",idTipoEJG);
			UtilidadesHash.set(ejg,"IDINSTITUCION",idInstitucion);
			
			try {				
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDPROCURADOR, idProcurador);
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDINSTITUCION, idInstitucionProcurador);
				ScsProcuradorBean b = (ScsProcuradorBean)(procuradorAdm.select(h)).get(0);
				UtilidadesHash.set(ejg,"PROCURADOR_NUM_COLEGIADO", b.getNColegiado());
				UtilidadesHash.set(ejg,"PROCURADOR_NOMBRE_COMPLETO", b.getNombre() + " " + b.getApellido1() + " " + b.getApellido2());
				
			} catch (Exception e) {
				
			}
			
			request.getSession().setAttribute("DATABACKUP",ejg);
			
			//CR7 - PROCURADORES DE DESIGNA RELACIONADA				
			Vector procuradoresDES = procuradorAdm.getProcuradoresRelacionadosPorDesigna(idInstitucion, idTipoEJG, anio, numero);
			if(procuradoresDES!=null && procuradoresDES.size() > 0){
				request.setAttribute("ProcuradoresDES", procuradoresDES);
			}else{
				request.setAttribute("ProcuradoresDES", "");
			}
			
			// Almaceno los parametros en el formulario:
			miForm.setIdTipoEJG(new Integer(idTipoEJG));
			miForm.setNumero(new Integer(numero));
			miForm.setAnio(new Integer(anio));
			miForm.setIdInstitucion(idInstitucion);
		} catch (Exception e){
			throwExcp("messages.general.error",e,null);
		}
		return "inicio";
	}


	/**
	 * Busca los delitos de un EJG concreto. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoEJGForm miForm = (PestanaDelitoEJGForm)formulario;		
		Integer anio, numero,  idTipoEJG;
		String idInstitucion = null;
		String idProcurador=null, idInstitucionProcurador=null;

		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsDelitosEJGAdm admDelitoEJG = new ScsDelitosEJGAdm(this.getUserBean(request));
			ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
			// Obtengo los datos seleccionados:
			idInstitucion = miForm.getIdInstitucion();
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			idTipoEJG = miForm.getIdTipoEJG();			
			
			Vector vDelitosEJG = admDelitoEJG.getDelitosEJG(Integer.valueOf(idInstitucion),anio,numero,idTipoEJG, usr.getLanguage());
			request.setAttribute("vDelitosEJG",vDelitosEJG);
			
			String consulta="select (Select Decode(Ejg.Idtipoencalidad, Null,'', f_Siga_Getrecurso(Tipcal.Descripcion,"+ usr.getLanguage()+")) "+
                              "  From Scs_Tipoencalidad Tipcal Where Tipcal.Idtipoencalidad = Ejg.Idtipoencalidad "+
                              "  And Tipcal.Idinstitucion = ejg.Calidadidinstitucion) as calidad, ejg.Idtipoencalidad,ejg.calidadidinstitucion, "+
			" ejg.guardiaturno_idturno IDTURNO, "+
			" ejg.OBSERVACIONES, ejg.DELITOS, ejg.IDPROCURADOR, ejg.IDINSTITUCION_PROC, ejg.NUMERO_CAJG," +
			" ejg.ANIOCAJG, ejg.NUMERODILIGENCIA NUMERODILIGENCIA, ejg.NUMEROPROCEDIMIENTO NUMEROPROCEDIMIENTO, ejg.ANIOPROCEDIMIENTO ANIOPROCEDIMIENTO, ejg.JUZGADO JUZGADO," +
			" ejg.JUZGADOIDINSTITUCION JUZGADOIDINSTITUCION, ejg.COMISARIA COMISARIA, ejg.COMISARIAIDINSTITUCION COMISARIAIDINSTITUCION" +
			"  from scs_ejg            ejg" ;
			consulta += " where "+
			"ejg.idtipoejg = " + idTipoEJG + " and ejg.idinstitucion = " + idInstitucion + " and ejg.anio = " + anio + " and ejg.numero = " + numero;			
		
			Vector resultado = admBean.selectGenerico(consulta);
			Hashtable ejg = (Hashtable)resultado.get(0);

			idProcurador=(String)ejg.get("IDPROCURADOR");
			idInstitucionProcurador=(String)ejg.get("IDINSTITUCION_PROC");
			UtilidadesHash.set(ejg,"NUMERO",numero);
			UtilidadesHash.set(ejg,"ANIO",anio);			
			UtilidadesHash.set(ejg,"IDTIPOEJG",idTipoEJG);
			
			
			
			try {
				ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.getUserBean(request));
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDPROCURADOR, idProcurador);
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDINSTITUCION, idInstitucionProcurador);
				ScsProcuradorBean b = (ScsProcuradorBean)(procuradorAdm.select(h)).get(0);
				UtilidadesHash.set(ejg,"PROCURADOR_NUM_COLEGIADO", b.getNColegiado());
				UtilidadesHash.set(ejg,"PROCURADOR_NOMBRE_COMPLETO", b.getNombre() + " " + b.getApellido1() + " " + b.getApellido2());
			}
			catch (Exception e) {}
			
			request.getSession().setAttribute("DATABACKUP",ejg);			
			
		} catch (Exception e){
			throwExcp("messages.general.error",e,null);
		}
		return "lista";
	}

	/**
	 * Mapeo a la ventana modal para crear un nuevo delito EJG.
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
		return "nuevo";
	}

	/**
	 * Inserta un nuevo delito a un EJG concreto.	 
	 *  
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 * @throws ClsExceptions
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoEJGForm miForm = (PestanaDelitoEJGForm)formulario;		
		Integer anio, numero, idInstitucion, idTipoEJG, idDelito;
		UserTransaction tx = null;
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsDelitosEJGAdm admDelitoEJG = new ScsDelitosEJGAdm(this.getUserBean(request));
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(miForm.getIdInstitucion());
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			idTipoEJG = miForm.getIdTipoEJG();
			idDelito = miForm.getIdDelito();
			
			ScsDelitosEJGBean beanDelitoEJG = new ScsDelitosEJGBean();
			beanDelitoEJG.setAnio(anio);
			beanDelitoEJG.setIdDelito(idDelito);
			beanDelitoEJG.setIdInstitucion(idInstitucion);
			beanDelitoEJG.setNumero(numero);
			beanDelitoEJG.setIdTipoEJG(idTipoEJG);
			beanDelitoEJG.setFechaMod("SYSDATE");
			beanDelitoEJG.setUsuMod(new Integer(usr.getUserName()));
						
			tx.begin();
			admDelitoEJG.insert(beanDelitoEJG);
			tx.commit();
		} catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return exitoModal("messages.inserted.success",request);
	}

	/**
	 * 
	 * Borra un delito para 1 EJG y un idDelitoEJG.
	 * 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PestanaDelitoEJGForm miForm = (PestanaDelitoEJGForm)formulario;
				
		Integer anio, numero, idInstitucion, idTipoEJG, idDelito;
		UserTransaction tx = null;
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsDelitosEJGAdm admDelitoEJG = new ScsDelitosEJGAdm(this.getUserBean(request));
			
			Vector vOcultos = miForm.getDatosTablaOcultos(0);
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(miForm.getIdInstitucion());
			idDelito = new Integer((String)vOcultos.get(0));
			
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			idTipoEJG = miForm.getIdTipoEJG();
			
			ScsDelitosEJGBean beanDelitoEJG = new ScsDelitosEJGBean();
			beanDelitoEJG.setAnio(anio);
			beanDelitoEJG.setIdDelito(idDelito);
			beanDelitoEJG.setIdInstitucion(idInstitucion);
			beanDelitoEJG.setNumero(numero);
			beanDelitoEJG.setIdTipoEJG(idTipoEJG);
						
			tx.begin();
			admDelitoEJG.delete(beanDelitoEJG);
			tx.commit();
		} catch (Exception e){
			throwExcp("messages.deleted.error",e,tx);
		}		
		return exitoRefresco("messages.deleted.success",request);
	}
}