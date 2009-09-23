//VERSIONES:

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.gratuita.form.BusquedaPorTipoSJCSForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
* @author AtosOrigin 21-04-2006
*/
public class BusquedaPorTipoSJCSAction extends MasterAction 
{
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{		
		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion.equalsIgnoreCase("enviar")) {
						mapDestino = enviar(mapping, miForm, request, response);
					} 
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	
	protected String abrir (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String destino = "";
		try {
		 	// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			BusquedaPorTipoSJCSForm miFormulario = (BusquedaPorTipoSJCSForm)formulario;
			miFormulario.setIdInstitucion(user.getLocation());
			miFormulario.setTipo(miFormulario.getTipo());
			destino = "abrir";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }
		 return destino;
	}

	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			Vector resultado = null;
			
			BusquedaPorTipoSJCSForm miFormulario = (BusquedaPorTipoSJCSForm)formulario;
			do {
				if (miFormulario.getTipo() == null) {
					return "";
				}
				if (miFormulario.getTipo().equalsIgnoreCase("EJG")) {
					resultado = buscarEJG(miFormulario, request);
					break;
				}
				if (miFormulario.getTipo().equalsIgnoreCase("DESIGNA")) {
					resultado = buscarDesigna(miFormulario, request);
					break;
				}
				if (miFormulario.getTipo().equalsIgnoreCase("SOJ")) {
					resultado = buscarSOJ(miFormulario, request);
					break;
				}
				
			} while (false);

			miFormulario.setTipo(miFormulario.getTipo());

			if (resultado == null) 
				resultado = new Vector ();
			request.setAttribute("resultado",resultado);
			return "resultado";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			return "";
	   	}
	}
			
	
	private Vector buscarEJG(BusquedaPorTipoSJCSForm formulario, HttpServletRequest request) throws SIGAException
	{
		try {
			// obtener institucion
			UsrBean user = this.getUserBean(request);
			
			// casting del formulario
			formulario.setIdInstitucion(user.getLocation());

			String sql = " WHERE " + ScsEJGBean.C_IDINSTITUCION + " = " + this.getIDInstitucion(request);
			
			if (formulario.getAnio() != null && !formulario.getAnio().equals(""))
				sql += " AND " + ScsEJGBean.C_ANIO + " = " + formulario.getAnio();

			if (formulario.getNumero() != null && !formulario.getNumero().equals(""))
				sql += " AND " +  ComodinBusquedas.prepararSentenciaCompleta(formulario.getNumero(), ScsEJGBean.C_NUMEJG);
				
			if (formulario.getIdTipoEJG() != null && !formulario.getIdTipoEJG().equals(""))
				sql += " AND " +  ScsEJGBean.C_IDTIPOEJG + " = " + formulario.getIdTipoEJG();

			ScsEJGAdm adm = new ScsEJGAdm(this.getUserBean(request));
			return adm.select(sql);
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			return null;
	   	 }
	}
	private Vector buscarSOJ(BusquedaPorTipoSJCSForm formulario, HttpServletRequest request) throws SIGAException
	{
		try {
			// obtener institucion
			UsrBean user = this.getUserBean(request);
			
			// casting del formulario
			formulario.setIdInstitucion(user.getLocation());

			String sql = " WHERE " + ScsSOJBean.C_IDINSTITUCION + " = " + this.getIDInstitucion(request);
			
			if (formulario.getAnio() != null && !formulario.getAnio().equals(""))
				sql += " AND " + ScsSOJBean.C_ANIO + " = " + formulario.getAnio();

			if (formulario.getNumero() != null && !formulario.getNumero().equals(""))
				sql += " AND " +  ComodinBusquedas.prepararSentenciaCompleta(formulario.getNumero(), ScsSOJBean.C_NUMSOJ);
				
			if (formulario.getIdTipoSOJ() != null && !formulario.getIdTipoSOJ().equals(""))
				sql += " AND " +  ScsSOJBean.C_IDTIPOSOJ + " = " + formulario.getIdTipoSOJ();

			ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm(this.getUserBean(request));
			return adm.select(sql);
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			return null;
	   	 }
	}
	
	private Vector buscarDesigna(BusquedaPorTipoSJCSForm formulario, HttpServletRequest request) throws SIGAException
	{
		try {
			// obtener institucion
			UsrBean user = this.getUserBean(request);
			
			// casting del formulario
			formulario.setIdInstitucion(user.getLocation());

			String sql = " WHERE " + ScsDesignaBean.C_IDINSTITUCION + " = " + this.getIDInstitucion(request);

			if (formulario.getAnio() != null && !formulario.getAnio().equals(""))
				sql += " AND " + ScsDesignaBean.C_ANIO + " = " + formulario.getAnio();

			if (formulario.getNumero() != null && !formulario.getNumero().equals(""))
				sql += " AND " +  ComodinBusquedas.prepararSentenciaCompleta(formulario.getNumero(), ScsDesignaBean.C_CODIGO);

			if (formulario.getTurnoDesigna() != null && !formulario.getTurnoDesigna().equals("")) {
				String turno = formulario.getTurnoDesigna();
				String a[] = turno.split(",");
				sql += " AND " +  ScsDesignaBean.C_IDTURNO + " = " + a[1];
			}

			ScsDesignaAdm adm = new ScsDesignaAdm(this.getUserBean(request));
			return adm.select(sql);
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
			return null;
	   	 }
	}

	protected String enviar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String destino = "";

		try {
			
			Hashtable claves = new Hashtable();
			
			BusquedaPorTipoSJCSForm miForm = (BusquedaPorTipoSJCSForm)formulario;

			Vector vOcultos      = miForm.getDatosTablaOcultos(0);
			String idInstitucion = miForm.getIdInstitucion();
			
			do {
				if (miForm.getTipo() == null) {
					return "";
				}
				if (miForm.getTipo().equalsIgnoreCase("EJG")) {
					UtilidadesHash.set(claves, ScsEJGBean.C_ANIO, (String)vOcultos.get(0));
					UtilidadesHash.set(claves, ScsEJGBean.C_NUMERO, (String)vOcultos.get(1));
					UtilidadesHash.set(claves, ScsEJGBean.C_IDTIPOEJG, (String)vOcultos.get(2));
					UtilidadesHash.set(claves, ScsEJGBean.C_IDINSTITUCION, idInstitucion);
					ScsEJGAdm adm = new ScsEJGAdm(this.getUserBean(request));
					Vector v = adm.selectByPK(claves);
					
					if (v != null && v.size() > 0) {
						ScsEJGBean bean = (ScsEJGBean) v.get(0);
						Vector elemento = new Vector();
						elemento.add ("" + bean.getIdInstitucion());
						elemento.add ("" + bean.getAnio());
						elemento.add ("" + bean.getNumero());
						elemento.add ("" + bean.getIdTipoEJG());
						request.setAttribute("elementoSeleccionado", elemento);		
					}					
					break;
				}
				
				if (miForm.getTipo().equalsIgnoreCase("DESIGNA")) {
					UtilidadesHash.set(claves, ScsDesignaBean.C_ANIO, (String)vOcultos.get(0));
					UtilidadesHash.set(claves, ScsDesignaBean.C_NUMERO, (String)vOcultos.get(1));
					UtilidadesHash.set(claves, ScsDesignaBean.C_IDTURNO, (String)vOcultos.get(2));
					UtilidadesHash.set(claves, ScsDesignaBean.C_IDINSTITUCION, idInstitucion);
					ScsDesignaAdm adm = new ScsDesignaAdm(this.getUserBean(request));
					Vector v = adm.selectByPK(claves);
					
					if (v != null && v.size() > 0) {
						ScsDesignaBean bean = (ScsDesignaBean) v.get(0);
						Vector elemento = new Vector();
						elemento.add ("" + bean.getIdInstitucion());
						elemento.add ("" + bean.getAnio());
						elemento.add ("" + bean.getNumero());
						elemento.add ("" + bean.getIdTurno());
						request.setAttribute("elementoSeleccionado", elemento);		
					}					
					break;
				}
				if (miForm.getTipo().equalsIgnoreCase("SOJ")) {
					UtilidadesHash.set(claves, ScsSOJBean.C_ANIO, (String)vOcultos.get(0));
					UtilidadesHash.set(claves, ScsSOJBean.C_NUMERO, (String)vOcultos.get(1));
					UtilidadesHash.set(claves, ScsSOJBean.C_IDTIPOSOJ, (String)vOcultos.get(2));
					UtilidadesHash.set(claves, ScsSOJBean.C_IDINSTITUCION, idInstitucion);
					ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm(this.getUserBean(request));
					Vector v = adm.selectByPK(claves);
					
					if (v != null && v.size() > 0) {
						ScsSOJBean bean = (ScsSOJBean) v.get(0);
						Vector elemento = new Vector();
						elemento.add ("" + bean.getIdInstitucion());
						elemento.add ("" + bean.getAnio());
						elemento.add ("" + bean.getNumero());
						elemento.add ("" + bean.getIdTipoSOJ());
						request.setAttribute("elementoSeleccionado", elemento);		
					}					
					break;
				}
			} 
			while (false);			

			miForm.setTipo(miForm.getTipo());
			
			destino="seleccion";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }
		 return destino;
	}
	
}
