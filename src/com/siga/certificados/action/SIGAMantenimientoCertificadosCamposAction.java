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
	    return cargarRegistro(mapping, formulario, request, response, true, true);
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosCamposForm form = (SIGAMantenimientoCertificadosCamposForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        CerProducInstiCampCertifAdm admProducto = new CerProducInstiCampCertifAdm(this.getUserBean(request));
	    
	    Hashtable htDatos = new Hashtable();
	    
	    String filas= form.getFilasSelect();
	    Vector tabla = ((Vector)request.getSession().getAttribute(("tablaDatos")));
	   
	    htDatos.put(CerProducInstiCampCertifBean.C_IDINSTITUCION, form.getIdInstitucion());
	    htDatos.put(CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO, form.getIdTipoProducto());
	    htDatos.put(CerProducInstiCampCertifBean.C_IDPRODUCTO, form.getIdProducto());
	    htDatos.put(CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION, form.getIdProductoInstitucion());


	    htDatos.put(CerProducInstiCampCertifBean.C_VALOR, form.getValor().trim());
	    StringTokenizer Tok = new StringTokenizer(filas,",");
	    int n=0;

	    while (Tok.hasMoreElements()){
	    	String datosTotales=(String) Tok.nextElement();
		    StringTokenizer Tok2 = new StringTokenizer(datosTotales,"#");
		    if(Tok2.countTokens()==4){
		    
		    String idFrmt=(String) Tok2.nextElement();
		    String tipoCamp =(String) Tok2.nextElement();
		    String idCampoCertificado=(String) Tok2.nextElement();
	    	idCampoCertificado=(String) Tok2.nextElement();

			    htDatos.put(CerProducInstiCampCertifBean.C_IDFORMATO, idFrmt);
				htDatos.put(CerProducInstiCampCertifBean.C_TIPOCAMPO, tipoCamp);
				htDatos.put(CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO, idCampoCertificado);
				if (admProducto.insert(htDatos))
				    {
				        request.setAttribute("mensaje","messages.updated.success");
				    }
				else
				    {
				        request.setAttribute("mensaje","messages.updated.error");
				    }
			    
		    }
		    if(Tok2.countTokens()==3){
			    
			    Tok2.nextElement();
			    String tipoCamp =(String) Tok2.nextElement();
			    String idCampoCertificado=(String) Tok2.nextElement();
		    	

				    htDatos.put(CerProducInstiCampCertifBean.C_IDFORMATO, "");
					htDatos.put(CerProducInstiCampCertifBean.C_TIPOCAMPO, tipoCamp);
					htDatos.put(CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO, idCampoCertificado);
					if (admProducto.insert(htDatos))
					    {
					        request.setAttribute("mensaje","messages.updated.success");
					    }
					else
					    {
					        request.setAttribute("mensaje","messages.updated.error");
					    }
				    
			    }

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
	    
	    //aalg: sustituir las comas y los espacios al final puesto que hace fallar el hashmap
	    
	    String valor = form.getValor().trim();
	    while (valor.endsWith(",")){
	    	valor = valor.substring(0, valor.length() -1);
	    	valor = valor.trim();
	    }
	    hashNew.put(CerProducInstiCampCertifBean.C_VALOR, valor);
	    
	    
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
	        
	        //aalg: INC_10345_SIGA. si el campo valor tiene comas, en vOcultos aparecen tantos campos como comas haya +1.
	        //se sabe que a partir del elemento 4 corresponde al valor por lo que se agrupan
	        //se sustituyen los caracteres @@ por espacios puesto que se han convertido en la jsp para poder conservarlos.
	        if (vOcultos.size()>=5){
	        	valor = ((String)vOcultos.elementAt(4)).trim().replace("&nbsp", " ");
	        	for (int i=5;i<vOcultos.size();i++)
	            valor += "," + ((String)vOcultos.elementAt(i)).trim().replace("&nbsp", " ");
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
	protected String cargarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosCamposForm form = (SIGAMantenimientoCertificadosCamposForm)formulario;
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
 	    
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
	    
	    
	    CerFormatosAdm adm = new CerFormatosAdm(this.getUserBean(request));
	    String query=" select c1.tipocampo as TIPO, (c1.tipocampo||'#'||c1.idcampocertificado) as ID, f_siga_getrecurso (c1.nombre, " + this.getUserBean(request).getLanguage() + ")  " +
	    		"as DESCRIPCION   from cer_camposcertificados c1 where (c1.idcampocertificado||','||c1.tipocampo) not in " +
	    		"(select (c.idcampocertificado||','||c.tipocampo) from cer_camposcertificados c, cer_producinsticampcertif cp " +
	    		"where cp.idinstitucion=" +sIdInstitucion+
	    		" and cp.idtipoproducto=" +sIdTipoProducto+
	    		" and cp.idproducto=" +sIdProducto+
	    		" and cp.idproductoinstitucion=" +sIdProductoInstitucion+
	    		" and cp.idcampocertificado=c.idcampocertificado and cp.tipocampo=c.tipocampo)  ORDER BY c1.tipocampo,descripcion asc";
	    
	    
	    CerCamposCertificadosAdm admcer = new CerCamposCertificadosAdm(this.getUserBean(request));
	    Vector vcampos = null;

	    try {
			vcampos = adm.selectGenerico(query);
		} catch (SIGAException e) {
			request.setAttribute("mensaje","messages.select.noexiste");
			
		}
	     


        Enumeration vEnum = vcampos.elements(); 
  
        Vector campos = new Vector();
    	Hashtable tablas = new Hashtable();
    	
    	String tipanterior  = "";
    	Hashtable htCampo = new Hashtable();
    	String tip = "";
    	if(!vEnum.hasMoreElements())
    		   request.setAttribute("mensaje","messages.noRecordFound");
    	while(vEnum.hasMoreElements()) {
         	htCampo = (Hashtable) vEnum.nextElement();
        	tip=(String)htCampo.get("TIPO");
        	if(tipanterior!="" && !tip.equalsIgnoreCase(tipanterior)){
        		tablas.put(tipanterior,campos.clone());
        		campos.clear();
        	}
        	campos.add(htCampo);
        	tipanterior=tip;
       }
       tablas.put(tip,campos.clone());
       
	    CerFormatosAdm admc = new CerFormatosAdm(this.getUserBean(request));
        Vector vformatos = null;

 	    	String queryFormato ="select f.tipocampo as TIPO, (f.idformato||'#'||f.tipocampo) as ID, f_siga_getrecurso (f.descripcion,1)  as DESCRIPCION   " +
			"from cer_formatos f order by f.tipocampo";
	    	try {
	    		vformatos=admc.selectGenerico(queryFormato);
			} catch (SIGAException e) {
				request.setAttribute("mensaje","error.messages.deleted");
			}
			Enumeration vEnumF = vformatos.elements(); 
			Hashtable htFmt = new Hashtable();
			Hashtable tablasFmt = new Hashtable();
		    Vector vfmt = new Vector();
		    String tpanterior  = "";
		    String tp = "";
			 while(vEnumF.hasMoreElements()) {
		        htFmt = (Hashtable) vEnumF.nextElement(); 
		        tp=(String)htFmt.get("TIPO");
		    	if(tpanterior!="" && !tp.equalsIgnoreCase(tpanterior)){
		    		tablasFmt.put(tpanterior, vfmt.clone());
		    		vfmt.clear();
		    	}
		        vfmt.add(htFmt);	
		        tpanterior=tp;
			 }
			 tablasFmt.put(tp, vfmt);

        //form.setCertificados(htTodo);
	    datos.add(htDatos);
	    datos.add(tablas);
	    datos.add(tablasFmt);
	    request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        request.setAttribute("nuevo", bNuevo ? "1" : "0");
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDCAMPOCERTIFICADO, idCampo);
            //hashBackUp.put(CerProducInstiCampCertifBean.C_IDFORMATO, idFormato);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDINSTITUCION, sIdInstitucion);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDPRODUCTO, sIdProducto);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDPRODUCTOINSTITUCION, sIdProductoInstitucion);
            hashBackUp.put(CerProducInstiCampCertifBean.C_IDTIPOPRODUCTO, sIdTipoProducto);
            //hashBackUp.put(CerProducInstiCampCertifBean.C_TIPOCAMPO, tipoCampo);
            hashBackUp.put(CerProducInstiCampCertifBean.C_VALOR, valor);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }
        
		return "cargar";
	}

}