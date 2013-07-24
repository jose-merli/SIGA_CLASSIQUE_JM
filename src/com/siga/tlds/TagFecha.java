package com.siga.tlds;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* Tag que implementa un textBox para fechas, con funcion de validacion asociada
* @author JBD
*/
public class TagFecha extends TagSupport {
	
	/**
	 * <siga:FechaBox
	 *		valorInicial="<%=fechaDesde()%>" 
	 *		required="TRUE"
	 *		formularioDatos="SolicitudesCertificadosForm" 
	 *		nombreCampo="fechaDese"
	 *  	/>
	 */
	
	private static final String DATE_FORMAT = "dd/mm/yyyy";
	private static final String DATEPICKER_DATE_FORMAT = "dd/mm/yy";
	
	private String nombreCampo = ""; 
	private String valorInicial = ""; 
	private String anchoTextField = "10";
    private String necesario = ""; // Si fuese necesario no puede llevar el valor ""
    private String styleId;
    private String campoCargarFechaDesde;
    private String disabled;
	private String preFunction;
	private String postFunction;
	private String posicionX;
	private String posicionY;
	private String readOnly;
	private String atributos;
    
	@Override
	public int doStartTag() {
		try{
			pageContext.getResponse().setContentType("text/html");
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			if (usrbean==null){
				usrbean= new UsrBean();
				usrbean.setLanguage("1");
			}
			PrintWriter out = pageContext.getResponse().getWriter();
			
			if (this.nombreCampo == null || "".equals(this.nombreCampo)){
				if (this.styleId == null || this.styleId.equals(""))
					this.nombreCampo = "datepicker";
				else
					this.nombreCampo = this.styleId;
			}
			if(this.styleId == null || this.styleId.equals("")){
				this.styleId = this.nombreCampo;
			}			
			String sDatepicker = "<input type='text' id='"+this.styleId+"' name='"+this.nombreCampo+"' maxlength='10'";
			if (this.atributos != null && !this.atributos.equals("")){
				sDatepicker += " " + this.atributos;
			}
			if(this.anchoTextField!=null && !this.anchoTextField.equals("")){
				sDatepicker += " size='"+anchoTextField+"'";
			} else {
				sDatepicker += " size='10'";
			}
			if (this.valorInicial != null && !this.valorInicial.equals("")){
				try{
					SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
					Date fechaValorInicial = dateFormat.parse(this.valorInicial);
					sDatepicker += " value='"+dateFormat.format(fechaValorInicial)+"'";
				} catch(Exception e){
					// No ponemos valor inicial
				}
			}
			String cssClass = " class='datepicker";
			if (this.disabled != null && UtilidadesString.stringToBoolean(this.disabled)){
				//bEditable = false;
				cssClass += " boxConsulta noEditable";
				sDatepicker += " readonly = 'readonly'";				
			} else {
				cssClass += " box editable";
			}
			cssClass += "'";
			sDatepicker += cssClass;
						
			if(this.preFunction!=null && !this.preFunction.equals("")){
				sDatepicker += " onfocus=\"return "+	this.preFunction+"\"";
			}
			if(this.postFunction!=null && !this.postFunction.equals("")){
				sDatepicker += " onchange=\"return "+	this.postFunction+"\"";
			}
			if (this.campoCargarFechaDesde != null && !this.campoCargarFechaDesde.equals("")){
				sDatepicker += " data-cargarfechadesde=\""+	this.campoCargarFechaDesde+"\"";
			}
			sDatepicker += " data-format=\""+	DATEPICKER_DATE_FORMAT +"\"";
			//TODO: SELECCIONAR IDIOMA DEL USUARIO DEFINIDO EN SIGA.JS
			sDatepicker += " data-regional=\""+	"es" +"\"";
			sDatepicker += " />";
			out.print(sDatepicker);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE; 	
	}
	
	public void setValorInicial(String valorInicial) {
		this.valorInicial = valorInicial;
	}
	public void setNombreCampo(String nombre) {
		this.nombreCampo = nombre;
	}
	public void setNecesario(String necesario) {
		this.necesario = necesario;
	}
	public void setCampoCargarFechaDesde(String campoCargarFechaDesde) {
		this.campoCargarFechaDesde = campoCargarFechaDesde;
	}
    
	public String getAnchoTextField() {
		return anchoTextField;
	}
	public void setAnchoTextField(String anchoTextField) {
		this.anchoTextField = anchoTextField;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	public String getPreFunction() {
		return preFunction;
	}
	public void setPreFunction(String preFunction) {
		this.preFunction = preFunction;
	}
	public String getPostFunction() {
		return postFunction;
	}
	public void setPostFunction(String postFunction) {
		this.postFunction = postFunction;
	}
	public String getPosicionX() {
		return posicionX;
	}
	public void setPosicionX(String posicionX) {
		this.posicionX = posicionX;
	}
	public String getPosicionY() {
		return posicionY;
	}
	public void setPosicionY(String posicionY) {
		this.posicionY = posicionY;
	}

	/**
	 * @return the atributos
	 */
	public String getAtributos() {
		return atributos;
	}

	/**
	 * @param atributos the atributos to set
	 */
	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}

	/**
	 * @return the nombreCampo
	 */
	public String getNombreCampo() {
		return nombreCampo;
	}

	/**
	 * @return the valorInicial
	 */
	public String getValorInicial() {
		return valorInicial;
	}

	/**
	 * @return the necesario
	 */
	public String getNecesario() {
		return necesario;
	}

	/**
	 * @return the campoCargarFechaDesde
	 */
	public String getCampoCargarFechaDesde() {
		return campoCargarFechaDesde;
	}

}