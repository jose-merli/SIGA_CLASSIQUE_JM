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
import com.siga.beans.ScsPrisionAdm;
import com.siga.beans.ScsPrisionBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MantenimientoPrisionForm;

/**
 * @author david.sanchez
 * @since 17/01/2006
 *
 */
public class MantenimientoPrisionAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoPrisionForm miForm = (MantenimientoPrisionForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			ScsPrisionAdm prisionAdm = new ScsPrisionAdm (this.getUserBean(request));
			
			Vector vPrisiones = prisionAdm.busquedaPrisiones(miForm, user.getLocation());

			request.setAttribute("vPrisiones", vPrisiones);
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
			MantenimientoPrisionForm miForm = (MantenimientoPrisionForm) formulario;
			String	idInstitucionPrision = (String)miForm.getDatosTablaOcultos(0).get(0);
			String	idPrision = (String)miForm.getDatosTablaOcultos(0).get(1);
			
			ScsPrisionAdm prisionAdm = new ScsPrisionAdm (this.getUserBean(request));
			Vector vPrisiones = prisionAdm.busquedaPrision(idInstitucionPrision, idPrision);
			
			if (vPrisiones.size() == 1) {
				Hashtable hashPrisiones = (Hashtable) vPrisiones.get(0);
				miForm.setDatos(hashPrisiones);
				hashPrisiones.remove("POBLACION");
				hashPrisiones.remove("PROVINCIA");
				request.getSession().setAttribute("DATABACKUP", hashPrisiones);
				
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
			MantenimientoPrisionForm miForm = (MantenimientoPrisionForm) formulario;
			ScsPrisionAdm prisionAdm = new ScsPrisionAdm (this.getUserBean(request));

			Hashtable hashPrisionOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			Hashtable hashPrisionModificado =  miForm.getDatos();

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			hashPrisionModificado.put(ScsPrisionBean.C_IDINSTITUCION,user.getLocation());

			String nombreOrig=UtilidadesHash.getString(hashPrisionOriginal,ScsPrisionBean.C_NOMBRE);
			String nombreModif=UtilidadesHash.getString(hashPrisionModificado,ScsPrisionBean.C_NOMBRE);
			boolean modificar=true;
			
			ScsPrisionAdm admPris = new ScsPrisionAdm(this.getUserBean(request));
			if (!miForm.getCodigoExt().trim().equals("") && admPris.comprobarCodigoExt(user.getLocation(),miForm.getIdPrision(),miForm.getCodigoExt())) {
				// true=existe el codigo externo
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.codigoExtDuplicado"); 
			}
			
			
			//si el nombre ha cambiado, comprobamos que no esté repetido
			if(!nombreOrig.equals(nombreModif)){
				modificar=ScsPrisionAdm.comprobarDuplicidad(user.getLocation(),nombreModif);
			}
			if(modificar){
				tx.begin();
				prisionAdm.update(hashPrisionModificado, hashPrisionOriginal);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.prisionDuplicada"); 
			}
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
			MantenimientoPrisionForm miForm = (MantenimientoPrisionForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsPrisionBean beanPrision = new ScsPrisionBean();
			ScsPrisionAdm prisionAdm = new ScsPrisionAdm (this.getUserBean(request));
			
			String idInstitucion=user.getLocation();
			String nombre=miForm.getNombre();
			beanPrision.setIdInstitucion(new Integer(idInstitucion));
			beanPrision.setNombre(nombre);
			beanPrision.setDireccion(miForm.getDireccion());
			beanPrision.setCodigoPostal(miForm.getCodigoPostal());
			beanPrision.setCodigoExt(miForm.getCodigoExt());
			beanPrision.setIdPoblacion(miForm.getIdPoblacion());
			beanPrision.setIdProvincia(miForm.getIdProvincia());
			beanPrision.setTelefono1(miForm.getTelefono1());
			beanPrision.setTelefono2(miForm.getTelefono2());
			beanPrision.setFax1(miForm.getFax1());			
			beanPrision.setIdPrision(prisionAdm.getNuevoIdPrision(user.getLocation()));

			ScsPrisionAdm admPris = new ScsPrisionAdm(this.getUserBean(request));
			if (!miForm.getCodigoExt().trim().equals("") && admPris.comprobarCodigoExt(user.getLocation(),miForm.getIdPrision(),miForm.getCodigoExt())) {
				// true=existe el codigo externo
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.codigoExtDuplicado"); 
			}

			if(ScsPrisionAdm.comprobarDuplicidad(idInstitucion,nombre)){
				tx.begin();
				prisionAdm.insert(beanPrision);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.prisionDuplicada"); 
			}
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
			MantenimientoPrisionForm miForm = (MantenimientoPrisionForm) formulario;
			
			tx = this.getUserBean(request).getTransaction();
						
			String	idInstitucionPrision = (String)miForm.getDatosTablaOcultos(0).get(0);
			Integer idPrision = new Integer ((String)miForm.getDatosTablaOcultos(0).get(1));
			
			ScsPrisionAdm prisionAdm = new ScsPrisionAdm(this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, ScsPrisionBean.C_IDPRISION, idPrision);
			UtilidadesHash.set (claves, ScsPrisionBean.C_IDINSTITUCION, idInstitucionPrision);
			
			tx.begin();
			prisionAdm.delete(claves);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.gratuita.error.eliminarProcedimiento", e, tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
	
}