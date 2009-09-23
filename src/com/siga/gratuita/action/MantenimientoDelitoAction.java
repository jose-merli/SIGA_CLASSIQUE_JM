package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDelitoAdm;
import com.siga.beans.ScsDelitoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MantenimientoDelitoForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class MantenimientoDelitoAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoDelitoForm miForm = (MantenimientoDelitoForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			ScsDelitoAdm declaracionAdm = new ScsDelitoAdm (this.getUserBean(request));
			
			String descripcion = miForm.getDescripcion();

			String where = " WHERE "+ComodinBusquedas.prepararSentenciaCompleta(descripcion.trim(), "F_SIGA_GETRECURSO("+ScsDelitoBean.C_DESCRIPCION+", " + user.getLanguage() + ")");
			
			Vector vDelitoes = declaracionAdm.select(where);

			request.setAttribute("vDelitoes", vDelitoes);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		} 
		return "buscarPor";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String obtenerDatos(String modo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		
		try {
			MantenimientoDelitoForm miForm = (MantenimientoDelitoForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			String	idDelito = (String)miForm.getDatosTablaOcultos(0).get(0);
			
			ScsDelitoAdm declaracionAdm = new ScsDelitoAdm (this.getUserBean(request));

			String where = " WHERE "+ScsDelitoBean.C_IDDELITO+"="+idDelito;
			
			Vector vDelitoes = declaracionAdm.select(where);
			if (vDelitoes.size() == 1) {
				Hashtable hashDelito = (Hashtable) vDelitoes.get(0);
				
				miForm.setIdDelito((String)hashDelito.get(ScsDelitoBean.C_IDDELITO));
				miForm.setDescripcion((String)hashDelito.get(ScsDelitoBean.C_DESCRIPCION));

				request.getSession().setAttribute("DATABACKUP", hashDelito);
			}
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		return this.obtenerDatos(modo,formulario,request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "ver";
		return this.obtenerDatos(modo,formulario,request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoDelitoForm miForm = (MantenimientoDelitoForm) formulario;
			ScsDelitoAdm declaracionAdm = new ScsDelitoAdm (this.getUserBean(request));

			Hashtable hashDelitoOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			Hashtable hashDelitoModificado = new Hashtable(); 

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			hashDelitoModificado.put(ScsDelitoBean.C_DESCRIPCION,miForm.getDescripcion());
			hashDelitoModificado.put(ScsDelitoBean.C_IDDELITO,miForm.getIdDelito());

			tx.begin();
			declaracionAdm.update(hashDelitoModificado, hashDelitoOriginal);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.updated.success", request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "nuevo";
		try {
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,	HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoDelitoForm miForm = (MantenimientoDelitoForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsDelitoBean beanDelito = new ScsDelitoBean();
			ScsDelitoAdm declaracionAdm = new ScsDelitoAdm (this.getUserBean(request));
			
			beanDelito.setDescripcion(miForm.getDescripcion());
			beanDelito.setIdDelito(declaracionAdm.getNuevoIdDelito());
			
			tx.begin();
			declaracionAdm.insert(beanDelito);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success", request);
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			MantenimientoDelitoForm miForm = (MantenimientoDelitoForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			tx = this.getUserBean(request).getTransaction();
						
			Integer idDelito = new Integer ((String)miForm.getDatosTablaOcultos(0).get(0));
			
			ScsDelitoAdm declaracionAdm = new ScsDelitoAdm(this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, ScsDelitoBean.C_IDDELITO, idDelito);			
			
			tx.begin();
			declaracionAdm.delete(claves);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.pys.mantenimientoProductos.errorBorrado", new SIGAException("messages.pys.mantenimientoProductos.errorBorrado"), tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
	
}