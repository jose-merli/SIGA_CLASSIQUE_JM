/*
 * Created on 22-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * modified by miguel.villegas 16-02-2005 setidInstitucionCliente()
 * modified by miguel.villegas 28-02-2005 doStartTag()
 * 
 */
package com.siga.tlds;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpPestanaConfAdm;


import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Carmen.Garcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TagPestanaExt extends TagSupport {
	// procesosinvisibles contiene un array de strings con los idproceso de 
    // las pestanhas que no son visibles. 
    protected String []procesosinvisibles=null;
    protected String iddiv="divid";
	protected String pestanakey=null;
	protected String idTipoExpediente=null;
	protected String idInstitucionTipoExpediente=null;
	protected String target=null;
	protected String parametros=null;
	protected int elementoactivo=1;
	protected int idinstitucionCliente=-1;
	protected int tipoAcceso=0xffffffff;
	private Hashtable htParametros=null;
	private Element elements[]=null;
	public static final String SQL_KEY="pestana";
	//public static final String PROPERTIES_FILE="Pestana.properties";
	private static final String parametroWhere = "@parametro@";
	private static final String PROCESS_KEY="PROCESS";
	private static final String URL_KEY="URL";
	private static final String NAME_KEY="NAME";
	private static final String ACCESO_KEY="ACCESO";
	public static final int FILTRO_LOCALIZACION=4;
	
	private final String PESTANACONF_1="310";
	private final String PESTANACONF_2="311";
	private final String PESTANADENUNCIANTES="301";
	private final String CAMPODENUNCIANTES="9";
	private final String PESTANADENUNCIADOS="312";
	private final String CAMPODENUNCIADOS="16";
	
//	private UsrBean usrbean=null;

	public void setProcesosinvisibles(String[] invisibles){
	    this.procesosinvisibles=invisibles;
	}
	
	public String[] getProcesosinvisibles(){
	    return this.procesosinvisibles;
	}	

	public void setIdTipoExpediente(String dato) 	{
		idTipoExpediente=dato;
	}
	public String getIdTipoExpediente() 	{
		return idTipoExpediente;
	}

	public void setIdInstitucionTipoExpediente(String dato) 	{
		idInstitucionTipoExpediente=dato;
	}
	public String getIdInstitucionTipoExpediente() 	{
		return idInstitucionTipoExpediente;
	}

	public void setPestanaId(String dato) 	{
		pestanakey=dato;
//		HttpSession ses=pageContext.getSession();
//		UsrBean usrbean=(UsrBean)ses.getAttribute("USRBEAN");
	}
	
	public void setTarget(String _target) {
		target=_target;
	}
	
	public void setTipoacceso(String _id) {
		try {
			tipoAcceso=Integer.parseInt(_id);
		} catch (Exception e) {
			tipoAcceso=0xffffffff;
		}
	}
	
	public void setIdInstitucionCliente(String _id) {
		try {
			idinstitucionCliente=Integer.parseInt(_id);
		} catch (Exception e) {
			// miguel.villegas: recibia siempre _id="" y accedia a esta rama provocando que tipoAcceso fuese
			// siempre -1 y en consecuencia eliminaba una rama "if" en ckeckAcceso
			//tipoAcceso=0xffffffff;
		}
	}
	
	public void setParametros(String _parametros) {
		parametros=_parametros;
	}
	public void setElementoactivo(int _elementoactivo) {
		elementoactivo=_elementoactivo;
	}
	
	public int doStartTag()	{
		try {
			pageContext.getResponse().setContentType("text/html");
			HttpSession session = pageContext.getSession();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			String path = request.getContextPath(); 
			if(parametros!=null) htParametros=(Hashtable)request.getAttribute(parametros);
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			PrintWriter out = pageContext.getResponse().getWriter();
			String hidden= "";
/*			if (!this.visible) {
			  hidden=" visibility:hidden ";
			} */
			executeConsulta(usrbean);
			
			out.println("<!-- INICIO: TABLA DE PESTAÑAS -->");

			
			out.println("<script type=\"text/javascript\">");
			out.println("function pulsarId(id, target) {");
			out.println("	var objLink = document.getElementById(id);");
			out.println("	sub();");
			out.println("	return pulsar(objLink,target);");
			out.println("}");
			
			out.println("function pulsar(objLink, target) {");
			out.println("	var dir = objLink.getAttribute('action');");
			out.println("	activateLink(objLink);");
			out.println("	for(i=0; i < window.frames.length; i++){ ");
			out.println("		if(window.frames[i].name == target){ ");
			out.println("			window.frames[i].location=dir;");
			out.println("		} ");
			out.println("	} ");
			out.println("	return false;");
			out.println("}");
			
			out.println("function activateLink(objLink) {");
			out.println("   var links=document.getElementsByTagName('a');");
			out.println("   sub();");
			out.println("   for(i=0; i<links.length;i++) {");
			out.println("       var lnk=links[i];");
			out.println("       if(lnk==objLink) {");
			out.println("           $(lnk).addClass('here');");
			out.println("       } else {");
			out.println("           $(lnk).removeClass('here');");
			out.println("       }");
			out.println("   }");
			out.println("}");

			out.println("function activarPestana() {");
			
			///////// MODIFICACION MAV ///////////////////////////////////////////////////////////////
			// Se incorpora una segunda clausula al if para evitar activar una pestanho no accesible	//
			//if(elementoactivo<=elements.length){													//
			if((elementoactivo<=elements.length)&&(elements[elementoactivo-1].mostrar)){			//
				out.println("pulsarId(\""+elements[elementoactivo-1].name+"\",\""+target+"\");");	//
			}																						//
			//////////////////////////////////////////////////////////////////////////////////////////
			
			out.println("}");
			
			
			out.println("</script>");

			out.println("<div style=\" position:relative; left:0px; width=100%; height=30px; top:0px; "+ hidden +"\" id=\""+iddiv+"\">");
			out.println("<table  class=\"tablaLineaPestanasArriba\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			out.println("<tr>");
			out.println("<td></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<table class=\"pest\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			out.println("<tr>");
			if (elements!=null) {
				for (int i=0;i<elements.length;i++) {		
					if (!elements[i].mostrar) continue;
					out.print("<td class=\"pestanaTD\"><a id=\"");
					out.print(elements[i].name);
					out.print("\" name=\"pestanaExt\" href=\"#\" action=\"");
					out.print(path+"/"+elements[i].url+".do"+"?granotmp="+System.currentTimeMillis());
					if(htParametros!=null && htParametros.size()!=0){
						out.print("&");
						Enumeration parametros=htParametros.keys();
						int size = htParametros.size();
						int temp=0;
						while(parametros.hasMoreElements()){
							Object parametro=parametros.nextElement();
							temp++;
							out.print((String)parametro+"="+(String)htParametros.get(parametro)+(size>temp?"&":""));
						}
					}
					out.println("\" onClick=\"sub();return pulsar(this,'" + target + "')\">");
					out.println(UtilidadesString.getMensajeIdioma(usrbean, elements[i].name));
					out.print("</a></td>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
	
	public int doEndTag() { 
		try {
			String aux = "";
			pageContext.getResponse().setContentType("text/html");
			PrintWriter out = pageContext.getResponse().getWriter();
			out.println("</tr>");
			out.println("</table>");
			out.println("<table  class=\"tablaLineaPestanas\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			out.println("<tr>");
			out.println("<td></td>");
			out.println("</tr>");
			out.println("</table>"); 
			out.println("</div>");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE; 			// continua la ejecucion de la pagina
	}
	
	private void executeConsulta(UsrBean usrbean) throws Exception  {
		int rc = 0;
	    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.QUERY);
		//ReadProperties p = new ReadProperties (PROPERTIES_FILE);
		String consultaSQL = p.returnProperty(SQL_KEY, true);
		if (consultaSQL != null) {
			consultaSQL = ReemplazaParametros(consultaSQL.toLowerCase());
			getDatosConsulta (consultaSQL,usrbean);
		}
	}
	
	private void getDatosConsulta (String consultaSQL,UsrBean usrbean) throws Exception	{ 
		RowsContainer rc = null;
		rc = new RowsContainer();
		if (rc.query(consultaSQL)) {
			elements=new Element[rc.size()];
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);
				elements[i]=new Element();
				elements[i].name=(String)fila.getValue(NAME_KEY);
				elements[i].process=(String)fila.getValue(PROCESS_KEY);
				elements[i].url=(String)fila.getValue(URL_KEY);
				
                try {
                	String acceso=(String)fila.getString(ACCESO_KEY);
                	if (acceso==null || acceso.length()==0) {
                		elements[i].acceso=0xffffffff;
                	} else {
                		elements[i].acceso=Integer.parseInt(acceso);
                	}
				} catch (Exception e) {
					elements[i].acceso=0xffffffff;
				}  
			}
		}
		ckeckAcceso(usrbean); 
	}
	
	private String ReemplazaParametros (String consulta) {
		return consulta.replaceFirst(parametroWhere, pestanakey==null?"":pestanakey);
	}

	
	private void ckeckAcceso (UsrBean usrbean) throws Exception	{
		int location=0;
		try {
			location=Integer.parseInt(usrbean.getLocation());
		} catch (Exception e) {
			location=0;
		}
		for (int i=0;i<elements.length;i++) {
			if (idinstitucionCliente!=-1 && idinstitucionCliente!=location && ((FILTRO_LOCALIZACION&elements[i].acceso)==FILTRO_LOCALIZACION)) {
				elements[i].mostrar=false;
			} 
			if ((elements[i].acceso&tipoAcceso)==0x0) {
				elements[i].mostrar=false;
			} else {
				String access=SIGAConstants.ACCESS_DENY;
				access=usrbean.getAccessForProcessNumber(elements[i].process);
				if (!access.equals(SIGAConstants.ACCESS_READ) && 
						!access.equals(SIGAConstants.ACCESS_FULL)) {
				    // Aqui meto a pelo que se vean las pestañas ya que han aparecido en los permisos (por estar seleccionadas)
				    if (!elements[i].process.equals(this.PESTANACONF_1) && !elements[i].process.equals(this.PESTANACONF_2)) {
				        elements[i].mostrar=false;
				    }
				}
			}
		}
		// Forzamos la invisibilidad de aquellos procesos indicados en for "procesosinvisibles"
		if(procesosinvisibles!=null){
			    for (int i=0;i<procesosinvisibles.length;i++) {
					    for (int z=0;z<elements.length;z++) {
					        if(elements[z].process.equalsIgnoreCase(procesosinvisibles[i])) {
					            elements[z].mostrar=false;
					        }
					    }   
			    }
		}
		
        // RGG compruebo bastante a pelo si es una pestaña configurable.
	    for (int z=0;z<elements.length;z++) {
	        // RGG compruebo bastante a pelo si es una pestaña configurable.
            if (elements[z].process.equals(this.PESTANACONF_1) || elements[z].process.equals(this.PESTANACONF_2)) {
                // Busco su descripción en la tabla de PESTANACONF
                ExpPestanaConfAdm admPes = new ExpPestanaConfAdm(usrbean);
                elements[z].name=admPes.getNombrePestanaDesdeProceso(this.idInstitucionTipoExpediente,this.idTipoExpediente, elements[z].process);
            }else if(elements[z].process.equals(this.PESTANADENUNCIANTES)){
            	ExpCampoTipoExpedienteAdm admTipo = new ExpCampoTipoExpedienteAdm(usrbean);
            	String nombre = admTipo.getNombrePestana(this.idInstitucionTipoExpediente,this.idTipoExpediente, this.CAMPODENUNCIANTES);
            	if(nombre!=null && !nombre.equals(""))
            		elements[z].name= nombre;
            	
            }else if( elements[z].process.equals(this.PESTANADENUNCIADOS)){
            	ExpCampoTipoExpedienteAdm admTipo = new ExpCampoTipoExpedienteAdm(usrbean);
            	String nombre = admTipo.getNombrePestana(this.idInstitucionTipoExpediente,this.idTipoExpediente, this.CAMPODENUNCIADOS);
            	if(nombre!=null && !nombre.equals(""))
            		elements[z].name= nombre; 
            	
            }
            
	    }   
		
	}
	
	private class Element {
		private String process=null;
		private String url=null;
		private String name=null;
		private int    acceso=0xffffffff;
		private boolean mostrar=true;
	}
}
