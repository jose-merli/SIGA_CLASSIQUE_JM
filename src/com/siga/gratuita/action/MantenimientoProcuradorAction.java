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
import com.siga.beans.ScsProcuradorAdm;
import com.siga.beans.ScsProcuradorBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MantenimientoProcuradorForm;
import com.siga.ws.CajgConfiguracion;

/**
 * @author david.sanchez
 * @since 17/01/2006
 *
 */
public class MantenimientoProcuradorAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoProcuradorForm miForm = (MantenimientoProcuradorForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm (this.getUserBean(request));
			
			String nombre = miForm.getNombre();
			String apellido1 = miForm.getApellido1();
			String apellido2 = miForm.getApellido2();
			String idInstitucion = user.getLocation();
			String codProcurador=miForm.getCodProcurador();
			Vector vProcuradores = procuradorAdm.busquedaProcuradores(nombre, apellido1, apellido2, idInstitucion,codProcurador);

			request.setAttribute("vProcuradores", vProcuradores);
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
			MantenimientoProcuradorForm miForm = (MantenimientoProcuradorForm) formulario;
			
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(this.getUserBean(request).getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			String	idInstitucionProcurador = (String)miForm.getDatosTablaOcultos(0).get(0);
			String	idProcurador = (String)miForm.getDatosTablaOcultos(0).get(1);
			
			ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm (this.getUserBean(request));
			Vector vProcuradores = procuradorAdm.busquedaProcurador(idInstitucionProcurador, idProcurador);
			
			if (vProcuradores.size() == 1) {
				Hashtable hashProcurador = (Hashtable) vProcuradores.get(0);
				miForm.setDatos(hashProcurador);
				hashProcurador.remove("POBLACION");
				hashProcurador.remove("PROVINCIA");
				request.getSession().setAttribute("DATABACKUP", hashProcurador);
				
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
			MantenimientoProcuradorForm miForm = (MantenimientoProcuradorForm) formulario;
			ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm (this.getUserBean(request));

			Hashtable hashProcuradorOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			Hashtable hashProcuradorModificado = miForm.getDatos();

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			hashProcuradorModificado.put(ScsProcuradorBean.C_IDINSTITUCION,user.getLocation());

			String nombreOrig=UtilidadesHash.getString(hashProcuradorOriginal,ScsProcuradorBean.C_NOMBRE);
			String nombreModif=UtilidadesHash.getString(hashProcuradorModificado,ScsProcuradorBean.C_NOMBRE);
			String apellOrig1=UtilidadesHash.getString(hashProcuradorOriginal,ScsProcuradorBean.C_APELLIDO1);
			String apellModif1=UtilidadesHash.getString(hashProcuradorModificado,ScsProcuradorBean.C_APELLIDO1);
			String apellOrig2=UtilidadesHash.getString(hashProcuradorOriginal,ScsProcuradorBean.C_APELLIDO2);
			String apellModif2=UtilidadesHash.getString(hashProcuradorModificado,ScsProcuradorBean.C_APELLIDO2);
			String codProcuradorOrig=UtilidadesHash.getString(hashProcuradorOriginal,ScsProcuradorBean.C_CODPROCURADOR);
			String codProcuradorModif=UtilidadesHash.getString(hashProcuradorModificado,ScsProcuradorBean.C_CODPROCURADOR);
			boolean modificar=true;
			//si alguno de los campos clave ha cambiado, comprobamos que no esté repetido
			if(!nombreOrig.equals(nombreModif) || !apellOrig1.equals(apellModif1) || !apellOrig2.equals(apellModif2)){
				modificar=ScsProcuradorAdm.comprobarDuplicidad(user.getLocation(),nombreModif,apellModif1,apellModif2);
			}
			if(modificar){
				tx.begin();
				procuradorAdm.update(hashProcuradorModificado, hashProcuradorOriginal);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.procuradorDuplicado"); 
			}
		}
		catch (Exception e) { 
			e.printStackTrace();
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
			MantenimientoProcuradorForm miForm = (MantenimientoProcuradorForm) formulario;
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(this.getUserBean(request).getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			miForm.setApellido1("");
			miForm.setApellido2("");
			miForm.setNombre("");
			miForm.setCodProcurador("");
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
			MantenimientoProcuradorForm miForm = (MantenimientoProcuradorForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsProcuradorBean beanProcurador = new ScsProcuradorBean();
			ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm (this.getUserBean(request));
			
			String idInstitucion=user.getLocation();
			String nombre=miForm.getNombre();
			String apellido1=miForm.getApellido1();
			String apellido2=miForm.getApellido2();
			beanProcurador.setIdInstitucion(new Integer(idInstitucion));
			beanProcurador.setNombre(nombre);
			beanProcurador.setApellido1(apellido1);
			beanProcurador.setApellido2(apellido2);
			beanProcurador.setDireccion(miForm.getDireccion());
			beanProcurador.setCodigoPostal(miForm.getCodigoPostal());
			beanProcurador.setIdPoblacion(miForm.getIdPoblacion());
			beanProcurador.setIdProvincia(miForm.getIdProvincia());
			beanProcurador.setTelefono1(miForm.getTelefono1());
			beanProcurador.setTelefono2(miForm.getTelefono2());
			beanProcurador.setFax1(miForm.getFax1());
			beanProcurador.setEmail(miForm.getEmail());
			beanProcurador.setIdProcurador(procuradorAdm.getNuevoIdProcurador(user.getLocation()));
			beanProcurador.setNColegiado(miForm.getNColegiado());
			beanProcurador.setIdColProcurador(miForm.getIdColProcurador());
			beanProcurador.setCodProcurador(miForm.getCodProcurador());
			
			if(ScsProcuradorAdm.comprobarDuplicidad(idInstitucion,nombre,apellido1,apellido2)){
				tx.begin();
				procuradorAdm.insert(beanProcurador);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.procuradorDuplicado"); 
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
			MantenimientoProcuradorForm miForm = (MantenimientoProcuradorForm) formulario;
			
			tx = this.getUserBean(request).getTransaction();
						
			String	idInstitucionProcurador = (String)miForm.getDatosTablaOcultos(0).get(0);
			String	idProcurador = (String)miForm.getDatosTablaOcultos(0).get(1);
			
			ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.getUserBean(request));
			Hashtable hash = new Hashtable ();
			UtilidadesHash.set (hash, ScsProcuradorBean.C_IDPROCURADOR, idProcurador);
			UtilidadesHash.set (hash, ScsProcuradorBean.C_IDINSTITUCION, idInstitucionProcurador);
			UtilidadesHash.set (hash, ScsProcuradorBean.C_FECHABAJA, "sysdate");			
			String[] campos = {ScsProcuradorBean.C_FECHABAJA};
			
			tx.begin();
			//CR7 - AHora no eliminara el registro, simplemente se dará de baja logica
			procuradorAdm.updateDirect(hash, procuradorAdm.getClavesBean(),campos);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.gratuita.error.eliminarProcedimiento", e, tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
	
}