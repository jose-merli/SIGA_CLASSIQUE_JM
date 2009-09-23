package com.siga.administracion.action;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.ParametrosGeneralesForm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.GenParametrosBean;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParametrosGeneralesAction extends MasterAction 
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)	throws ClsExceptions, SIGAException
	{
		String esCGAE = CenVisibilidad.getNivelInstitucion(this.getUserBean(request).getLocation());
		if(esCGAE.equals("1"))
			request.setAttribute("esCGAE","true");
				
		return super.abrir(mapping, formulario, request, response);
	}
	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)	throws ClsExceptions, SIGAException 
	{
		ParametrosGeneralesForm form = (ParametrosGeneralesForm) formulario;
		
		boolean parametrosGenerales = form.getCheckParametrosGenerales().equals("1");
		
		GenParametrosAdm adm = new GenParametrosAdm (this.getUserBean(request));
		
		Vector v = adm.getParametrosPorInstitucionMasGenerales(parametrosGenerales?new Integer("0"):this.getIDInstitucion(request), form.getIdModulo());
		
        request.setAttribute("resultados", v);
        request.setAttribute("checkParametrosGenerales", form.getCheckParametrosGenerales());
		return "resultado";
	}
	
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
        UserTransaction tx = this.getUserBean(request).getTransaction();
  
		try {
			ParametrosGeneralesForm form = (ParametrosGeneralesForm) formulario;
			String a = form.getDatosModificados();
			
			if (a.length() < 0) {
				throw new ClsExceptions ("messages.updated.error");
			}

			tx.begin();
			
			GenParametrosAdm adm = new GenParametrosAdm (this.getUserBean(request));
			
			// Esctructura form.getDatosModificados  -->    idInstitucion1#;#idModulo1#;#paramentro1#=#valor1#;;#   ..... 
			String []elementos = a.split("#;;#");
			
			for (int i = 0; i < elementos.length; i++) {
				String []aux = elementos[i].split("#;#");
				String idIntitucion = aux[0];
				String idModulo = aux[1];
				String []key_valor = aux[2].split("#=#");
				String parametro = key_valor[0];
				String valor=key_valor[1];
				valor=UtilidadesString.replaceAllIgnoreCase(valor,"\u00A0","\u0020");	
				if (valor.trim()!=null && !valor.trim().equals("")) {
				 
				  valor = valor.trim();	
				}else{
				 valor="\u0020";
				}

				GenParametrosBean bean = new GenParametrosBean ();
				bean.setIdInstitucion(new Integer(idIntitucion));
				bean.setModulo(idModulo);
				bean.setParametro(parametro);
				bean.setValor(valor);

				// Si esto modificando los parametros generales a todos ....
				if (form.getCheckParametrosGenerales().equals("1")) {
					if (!adm.updateDirect(bean)) {
						throw new ClsExceptions ("messages.updated.error");
					}
				}
				else {
					// Si soy colegio y estoy modificando los parametros generales ....
					if (idIntitucion.equals("" + ClsConstants.INSTITUCION_POR_DEFECTO)) {
						
						Hashtable h = new Hashtable();
						UtilidadesHash.set (h, GenParametrosBean.C_IDINSTITUCION, new Integer(ClsConstants.INSTITUCION_POR_DEFECTO));
						UtilidadesHash.set (h, GenParametrosBean.C_MODULO, bean.getModulo());
						UtilidadesHash.set (h, GenParametrosBean.C_PARAMETRO, bean.getParametro());
						Vector v = adm.selectByPK(h);
						if (v.size() == 1) {
							String idRecurso = ((GenParametrosBean)v.get(0)).getIdRecurso();
							bean.setIdRecurso(idRecurso);
						}
						bean.setIdInstitucion(this.getIDInstitucion(request));
						if (!adm.insert(bean)) {
							throw new ClsExceptions ("messages.updated.error");
						}
					}
					else {
						if (!adm.updateDirect(bean)) {
							throw new ClsExceptions ("messages.updated.error");
						}
					}
				}
			}
			tx.commit();
		}
		catch (Exception e) 
		{
			throwExcp("messages.updated.error",e,tx);
        }

		return this.exitoRefresco("messages.updated.success", request);
	}
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		ParametrosGeneralesForm form = (ParametrosGeneralesForm) formulario;
		Vector ocultos = form.getDatosTablaOcultos(0);
		String modulo        = (String) ocultos.get(0); // Modulo
		String idInstitucion = (String) ocultos.get(1); // Institucion
		String parametro     = (String) ocultos.get(2); // Parametro

		// No se pueden eliminar parametros de la institucion '0'
		if (idInstitucion.equals("" + ClsConstants.INSTITUCION_POR_DEFECTO)) {
			return this.exitoModalSinRefresco("messages.deleted.error", request);
		}
		
		Hashtable h = new Hashtable();
		UtilidadesHash.set (h, GenParametrosBean.C_MODULO,        modulo);
		UtilidadesHash.set (h, GenParametrosBean.C_PARAMETRO,     parametro);
		UtilidadesHash.set (h, GenParametrosBean.C_IDINSTITUCION, idInstitucion);
		
		GenParametrosAdm adm = new GenParametrosAdm (this.getUserBean(request));
		if (!adm.delete(h))
			return this.exitoModalSinRefresco("messages.deleted.error", request);
		
		return this.exitoRefresco("messages.deleted.success", request);
	}
}
 