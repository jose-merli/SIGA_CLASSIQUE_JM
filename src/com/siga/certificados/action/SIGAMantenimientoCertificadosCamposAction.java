package com.siga.certificados.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.siga.certificados.form.*;

public class SIGAMantenimientoCertificadosCamposAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosCamposForm form = (SIGAMantenimientoCertificadosCamposForm)formulario;
	    CerProducInstiCampCertifAdm admProducto = new CerProducInstiCampCertifAdm(this.getUserBean(request));

	    String idInstitucion = form.getIdInstitucion();
	    String idTipoProducto = form.getIdTipoProducto();
	    String idProducto = form.getIdProducto();
	    String idProductoInstitucion = form.getIdProductoInstitucion();
	    
	    String sCertificado = form.getCertificado();
	    String sEditable = form.getEditable();
	    
	    String descCertificado = form.getDescripcionCertificado();
	    
	    Vector vDatos = admProducto.obtenerCampos(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion);
	    
	    request.setAttribute("idInstitucion", idInstitucion);
	    request.setAttribute("idTipoProducto", idTipoProducto);
	    request.setAttribute("idProducto", idProducto);
	    request.setAttribute("idProductoInstitucion", idProductoInstitucion);
	    
	    request.setAttribute("certificado", sCertificado);
	    request.setAttribute("editable", sEditable);
	    
	    request.setAttribute("descripcionCertificado", descCertificado);
	    
	    request.setAttribute("datos", vDatos);
	    
		return "abrir";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, false);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false, false);
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, true);
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosCamposForm form = (SIGAMantenimientoCertificadosCamposForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        CerProducInstiCampCertifAdm admProducto = new CerProducInstiCampCertifAdm(this.getUserBean(request));
	    
	    Hashtable htDatos = new Hashtable();
	    
	    htDatos.put(CerProducInstiCampCertifBean.C_IDINSTITUCION, form.getIdInstitucion());
	    htDatos.put(CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO, form.getIdTipoProducto());
	    htDatos.put(CerProducInstiCampCertifBean.C_IDPRODUCTO, form.getIdProducto());
	    htDatos.put(CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION, form.getIdProductoInstitucion());
	    htDatos.put(CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO, form.getIdCampoCertificado());
	    htDatos.put(CerProducInstiCampCertifBean.C_IDFORMATO, form.getIdFormato());
	    htDatos.put(CerProducInstiCampCertifBean.C_TIPOCAMPO, form.getTipoCampo());
	    htDatos.put(CerProducInstiCampCertifBean.C_VALOR, form.getValor());

	    if (admProducto.insert(htDatos))
	    {
	        request.setAttribute("mensaje","messages.updated.success");
	    }
	    
	    else
	    {
	        request.setAttribute("mensaje","messages.updated.error");
	    }
        
        request.setAttribute("modal","1");
	    
	    return "exito";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    SIGAMantenimientoCertificadosCamposForm form = (SIGAMantenimientoCertificadosCamposForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        CerProducInstiCampCertifAdm admProducto = new CerProducInstiCampCertifAdm (this.getUserBean(request));
        
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
        Hashtable hashNew = (Hashtable)hashOld.clone();
	    
	    hashNew.put(CerProducInstiCampCertifBean.C_IDFORMATO, form.getIdFormato());
	    hashNew.put(CerProducInstiCampCertifBean.C_VALOR, form.getValor());
	    
        if (admProducto.update(hashNew, hashOld))
        {
            request.setAttribute("mensaje","messages.updated.success");
         
            //request.removeAttribute("DATABACKUP");
            request.getSession().removeAttribute("DATABACKUP");
        }
        
        else
        {
            request.setAttribute("mensaje","messages.updated.error");
        }

        request.setAttribute("modal","1");
	    
        return "exito";
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosCamposForm form = (SIGAMantenimientoCertificadosCamposForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        CerProducInstiCampCertifAdm admProducto = new CerProducInstiCampCertifAdm (this.getUserBean(request));
        
        Vector vOcultos = form.getDatosTablaOcultos(0);
        
        String sIdInstitucion = form.getIdInstitucion();
        String sIdTipoProducto = form.getIdTipoProducto();
        String sIdProducto = form.getIdProducto();
        String sIdProductoInstitucion = form.getIdProductoInstitucion();
        String idCampo = ((String)vOcultos.elementAt(0)).trim();
        String tipoCampo = ((String)vOcultos.elementAt(2)).trim();

	    Hashtable hash = new Hashtable();
	    
	    hash.put(CerProducInstiCampCertifBean.C_IDINSTITUCION, sIdInstitucion);
	    hash.put(CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO, sIdTipoProducto);
	    hash.put(CerProducInstiCampCertifBean.C_IDPRODUCTO, sIdProducto);
	    hash.put(CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION, sIdProductoInstitucion);
	    hash.put(CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO, idCampo);
	    hash.put(CerProducInstiCampCertifBean.C_TIPOCAMPO, tipoCampo);
	    
	    if (admProducto.delete(hash))
	    {
	        request.setAttribute("mensaje","messages.deleted.success");
	    }
	    
	    else
	    {
	        request.setAttribute("mensaje","error.messages.deleted");
	    }

        return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosCamposForm form = (SIGAMantenimientoCertificadosCamposForm)formulario;
	    
	    String idCampo="";
		String descCampo="";
		String idFormato="";
	    String descFormato="";
	    String tipoCampo="";
	    String capturarDatos="";
	    String valor="";

		String sIdInstitucion=form.getIdInstitucion();
		String sIdTipoProducto=form.getIdTipoProducto();
		String sIdProducto=form.getIdProducto();
		String sIdProductoInstitucion=form.getIdProductoInstitucion();
		
		if (!bNuevo)
		{
		    Vector vVisibles = form.getDatosTablaVisibles(0);
			Vector vOcultos = form.getDatosTablaOcultos(0);		

			descCampo = ((String)vVisibles.elementAt(0)).trim();
		    descFormato = ((String)vVisibles.elementAt(1)).trim();
			
	        idCampo = ((String)vOcultos.elementAt(0)).trim();
	        idFormato = ((String)vOcultos.elementAt(1)).trim();
	        
	        tipoCampo = ((String)vOcultos.elementAt(2)).trim();
	        capturarDatos = ((String)vOcultos.elementAt(3)).trim();
	        
	        if (vOcultos.size()==5)
	        {
	            valor = ((String)vOcultos.elementAt(4)).trim();
	        }
		}

	    Vector datos = new Vector();
	    Hashtable htDatos = new Hashtable();
	    
	    htDatos.put("idCampo", idCampo);
	    htDatos.put("descCampo", descCampo);
	    htDatos.put("idFormato", idFormato);
	    htDatos.put("descFormato", descFormato);

	    htDatos.put("tipoCampo", tipoCampo);
	    htDatos.put("capturarDatos", capturarDatos);
	    htDatos.put("valor", valor);

	    htDatos.put("idInstitucion", sIdInstitucion);
	    htDatos.put("idTipoProducto", sIdTipoProducto);
	    htDatos.put("idProducto", sIdProducto);
	    htDatos.put("idProductoInstitucion", sIdProductoInstitucion);
	    
	    datos.add(htDatos);
	    
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        request.setAttribute("nuevo", bNuevo ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO, idCampo);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDFORMATO, idFormato);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDINSTITUCION, sIdInstitucion);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDPRODUCTO, sIdProducto);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION, sIdProductoInstitucion);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO, sIdTipoProducto);
            hashBackUp.put(CerProducInstiCampCertifBean.C_TIPOCAMPO, tipoCampo);
            hashBackUp.put(CerProducInstiCampCertifBean.C_VALOR, valor);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }
	    
		return "mostrar";
	}
}