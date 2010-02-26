package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CajgConfiguracionAdm;
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
			request.getSession().removeAttribute("DATABACKUP");
			int valorPcajgActivo=CajgConfiguracionAdm.getTipoCAJG(new Integer(usr.getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			
			
			Vector v = new Vector ();
			Hashtable miHash = new Hashtable();		
				
			
			miHash.put("ANIO",request.getParameter("ANIO").toString());
			miHash.put("NUMERO",request.getParameter("NUMERO").toString());
			miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
			miHash.put("IDINSTITUCION",usr.getLocation().toString());	
			
			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
			
			try {			
				v = admEJG.selectPorClave(miHash);
				request.getSession().setAttribute("DATABACKUP",admEJG.beanToHashTable((ScsEJGBean)v.get(0)));			
			} catch (Exception e) {
				   throwExcp("messages.general.error",e,null);
			}
			// Obtengo de la pestanha el anio, numero, idTipoEJG:			
			numero = (String)request.getParameter("NUMERO");
			anio = (String)request.getParameter("ANIO");
			idTipoEJG = (String)request.getParameter("IDTIPOEJG");
			
			String consulta="select decode(ejg.CALIDAD, 'D', 'DEMANDANTE', 'DEMANDADO'),       " +
					" ejg.guardiaturno_idturno IDTURNO, ejg.IDPRETENSION as IDPRETENSION, ejg.IDPRETENSIONINSTITUCION as IDPRETENSIONINSTITUCION, " +
					" ejg.CALIDAD, ejg.OBSERVACIONES, ejg.DELITOS, ejg.IDPROCURADOR, ejg.IDINSTITUCION_PROC, ejg.NUMERO_CAJG," +
					" ejg.ANIOCAJG, ejg.NUMERODILIGENCIA NUMERODILIGENCIA, ejg.NUMEROPROCEDIMIENTO NUMEROPROCEDIMIENTO, ejg.JUZGADO JUZGADO," +
					" ejg.JUZGADOIDINSTITUCION JUZGADOIDINSTITUCION, ejg.COMISARIA COMISARIA, ejg.COMISARIAIDINSTITUCION COMISARIAIDINSTITUCION," +
					" ejg.FECHA_DES_PROC,ejg.IDPRECEPTIVO, ejg.IDSITUACION "+
					"  from scs_ejg            ejg" ;
			consulta += " where "+
			"ejg.idtipoejg = " + idTipoEJG + " and ejg.idinstitucion = " + usr.getLocation() + " and ejg.anio = " + anio + " and ejg.numero = " + numero;			

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
			
			
			// Almaceno los parametros en el formulario:
			miForm.setIdTipoEJG(new Integer(idTipoEJG));
			miForm.setNumero(new Integer(numero));
			miForm.setAnio(new Integer(anio));
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
		Integer anio, numero, idInstitucion, idTipoEJG;
		String idProcurador=null, idInstitucionProcurador=null;

		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsDelitosEJGAdm admDelitoEJG = new ScsDelitosEJGAdm(this.getUserBean(request));
			ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			numero = miForm.getNumero();
			anio = miForm.getAnio();
			idTipoEJG = miForm.getIdTipoEJG();			
			
			Vector vDelitosEJG = admDelitoEJG.getDelitosEJG(idInstitucion,anio,numero,idTipoEJG, usr.getLanguage());
			request.setAttribute("vDelitosEJG",vDelitosEJG);
			
			String consulta="select decode(ejg.CALIDAD, 'D', 'DEMANDANTE', 'DEMANDADO'),       " +
			" ejg.guardiaturno_idturno IDTURNO, "+
			" ejg.CALIDAD, ejg.OBSERVACIONES, ejg.DELITOS, ejg.IDPROCURADOR, ejg.IDINSTITUCION_PROC, ejg.NUMERO_CAJG," +
			" ejg.ANIOCAJG, ejg.NUMERODILIGENCIA NUMERODILIGENCIA, ejg.NUMEROPROCEDIMIENTO NUMEROPROCEDIMIENTO, ejg.JUZGADO JUZGADO," +
			" ejg.JUZGADOIDINSTITUCION JUZGADOIDINSTITUCION, ejg.COMISARIA COMISARIA, ejg.COMISARIAIDINSTITUCION COMISARIAIDINSTITUCION" +
			"  from scs_ejg            ejg" ;
			consulta += " where "+
			"ejg.idtipoejg = " + idTipoEJG + " and ejg.idinstitucion = " + usr.getLocation() + " and ejg.anio = " + anio + " and ejg.numero = " + numero;			
		
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
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
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
			idInstitucion = new Integer(usr.getLocation());
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
			idInstitucion = new Integer(usr.getLocation());
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