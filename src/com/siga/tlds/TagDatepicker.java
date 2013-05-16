package com.siga.tlds;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

public class TagDatepicker extends TagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7534715684941253242L;
	
	private static final String DATE_FORMAT = "dd/mm/yyyy";
	private static final String DATEPICKER_DATE_FORMAT = "dd/mm/yy";
	
	private String nombreCampo; 
	private String valorInicial;
	private String necesario;
	private String anchoTextField = "10";
	private String styleId;
	private String campoCargarFechaDesde;
	private String disabled;
	private String readOnly;
	private String preFunction;
	private String postFunction;
	private String posicionX;
	private String posicionY;
	
	
	@Override
	public int doStartTag() throws JspException {
		boolean bEditable = true;
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
				bEditable = false;
				cssClass += " boxConsulta";
				if (this.readOnly == null || !UtilidadesString.stringToBoolean(this.readOnly)){
					this.readOnly = "TRUE";
				}
			} else {
				cssClass += " box";
			}
			cssClass += "'";
			sDatepicker += cssClass;
			
			
			if (this.readOnly != null && UtilidadesString.stringToBoolean(this.readOnly)){
				bEditable = false;
				sDatepicker += " readonly = 'readonly'";
			}
			
			//sDatepicker += " readonly = 'readonly'";
			if(this.preFunction!=null && !this.preFunction.equals("")){
				sDatepicker += " onfocus=\"return "+	this.preFunction+"\"";
			}
			if(this.postFunction!=null && !this.postFunction.equals("")){
				sDatepicker += " onchange=\"return "+	this.postFunction+"\"";
			}
			sDatepicker += " />";
			out.print(sDatepicker);
			
			String sJs = "<script type='text/javascript'>";
			sJs += "jQuery(function() {";
			if (this.campoCargarFechaDesde != null && !this.campoCargarFechaDesde.equals("")){
				sJs += "jQuery('#"+this.styleId+"').val(jQuery('#"+this.campoCargarFechaDesde+"').val());";				
			}
			if (bEditable){				
				sJs += "setDatepickerClearBtn('"+UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"general.boton.borrar")+"');";
				sJs += "jQuery('#"+this.styleId+"').datepicker({";
				sJs += "changeMonth: true,";
				sJs += "changeYear: true,";
				sJs += "dateFormat: '"+DATEPICKER_DATE_FORMAT+"',";
				//TODO: SELECCIONAR IDIOMA DEL USUARIO DEFINIDO EN SIGA.JS
				sJs += "regional: 'es',";
				sJs += "showButtonPanel: true,";
				sJs += "showOn: 'both',";
				sJs += "buttonImage: 'button',";
				sJs += "buttonImage: '/SIGA/html/imagenes/calendar.gif',";
				sJs += "buttonImageOnly: true";
				sJs += "}).keydown(function(e) {";
				sJs += "if(e.keyCode == 8 || e.keyCode == 46) {";
				sJs += "jQuery.datepicker._clearDate(this);";
				sJs += "return false;";
				sJs += "}";
				sJs += "});";
				// Se aplicará la máscara del campo desde siga.js
				//sJs += "jQuery.mask.definitions['dd']='99';";
				//sJs += "jQuery.mask.definitions['mm']='99';";
				//sJs += "jQuery.mask.definitions['yyyy']='9999';";
				//sJs += "jQuery('#"+this.styleId+"')";
				//sJs += ".mask('dd/mm/yyyy',{completed:function(){datepickerMaskChanged(jQuery(this));}});";
				
			}
			sJs += "";
			sJs += "});";
			sJs += "</script>";
			out.println(sJs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
	
	
	public String getNombreCampo() {
		return nombreCampo;
	}
	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}
	public String getValorInicial() {
		return valorInicial;
	}
	public void setValorInicial(String valorInicial) {
		this.valorInicial = valorInicial;
	}
	public String getNecesario() {
		return necesario;
	}
	public void setNecesario(String necesario) {
		this.necesario = necesario;
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
	public String getCampoCargarFechaDesde() {
		return campoCargarFechaDesde;
	}
	public void setCampoCargarFechaDesde(String campoCargarFechaDesde) {
		this.campoCargarFechaDesde = campoCargarFechaDesde;
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
	
}
