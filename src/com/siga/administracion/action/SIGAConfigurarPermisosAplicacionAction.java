package com.siga.administracion.action;

import com.atos.utils.*;
import com.siga.general.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.siga.administracion.form.*;

public class SIGAConfigurarPermisosAplicacionAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAConfigurarPermisosAplicacionForm form = (SIGAConfigurarPermisosAplicacionForm)formulario;
        
        request.getSession().setAttribute("idPerfil", form.getIdPerfil());

        return "buscar";
	}
}