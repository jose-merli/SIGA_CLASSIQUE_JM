package com.siga.administracion.action;

import com.atos.utils.*;
import com.siga.general.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.siga.administracion.form.*;

public class SIGAAsignarUsuariosGrupoAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAAsignarUsuariosGrupoForm form = (SIGAAsignarUsuariosGrupoForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        request.getSession().setAttribute("nombreBusqueda", form.getNombreBusqueda());
        request.getSession().setAttribute("idRolBusqueda", form.getIdRolBusqueda());
        request.getSession().setAttribute("idGrupoBusqueda", form.getIdGrupoBusqueda());
        
        return "buscar";
	}
}