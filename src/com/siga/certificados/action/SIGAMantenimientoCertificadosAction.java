package com.siga.certificados.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.siga.certificados.form.*;

public class SIGAMantenimientoCertificadosAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

	protected String abrirConParametros(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosForm form = (SIGAMantenimientoCertificadosForm)formulario;
	    
	    request.setAttribute("certificado", form.getCertificado());
	    
		return "abrir";
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAMantenimientoCertificadosForm form = (SIGAMantenimientoCertificadosForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        PysProductosInstitucionAdm productoAdm = new PysProductosInstitucionAdm(this.getUserBean(request));
        
        String certificado = form.getCertificado();
        String institucion = userBean.getLocation();

        String where = " WHERE ";
        
        where += PysProductosInstitucionBean.C_TIPOCERTIFICADO + " IN ('" + PysProductosInstitucionBean.PI_COMUNICACION_CODIGO + "','" +
        																   PysProductosInstitucionBean.PI_DILIGENCIA_CODIGO + "','" +
        																   PysProductosInstitucionBean.PI_CERTIFICADO_CODIGO + "')";
        where += " AND " + PysProductosInstitucionBean.C_IDINSTITUCION + " = " + institucion;
        where += (certificado!=null && !certificado.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(certificado.trim(),PysProductosInstitucionBean.C_DESCRIPCION): "" ;
        

        Vector datos = productoAdm.selectNLS(where);
        
        request.setAttribute("datos", datos);
        request.setAttribute("certificado", form.getCertificado());
        
        return "buscar";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false);
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosForm form = (SIGAMantenimientoCertificadosForm)formulario;
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    Vector vVisibles = form.getDatosTablaVisibles(0);

        String idInstitucion = (String)vOcultos.elementAt(0);
        String idTipoProducto = (String)vOcultos.elementAt(1);
        String idProducto = (String)vOcultos.elementAt(2);
        String idProductoInstitucion = (String)vOcultos.elementAt(3);
        
        String sCertificado = (String)vVisibles.elementAt(0);

	    Hashtable htDatos = new Hashtable();
	    
	    htDatos.put("idInstitucion", idInstitucion);
	    htDatos.put("idTipoProducto", idTipoProducto);
	    htDatos.put("idProducto", idProducto);
	    htDatos.put("idProductoInstitucion", idProductoInstitucion);
	    htDatos.put("editable", bEditable ? "1" : "0");
	    htDatos.put("certificado", form.getCertificado());
	    htDatos.put("descripcionCertificado", sCertificado);
	    
	    request.setAttribute("htDatos", htDatos);
	    
		return "abrirPestanas";
	}
}