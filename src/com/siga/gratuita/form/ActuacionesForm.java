package com.siga.gratuita.form;

/**
 * @author carlos.vidal
 */
import com.siga.general.MasterForm;

public class ActuacionesForm extends MasterForm {
	private String aanio            ="aanio";
	private String anumero          ="anumero";
	private String pjgapellido2     ="pjgapellido2";   
	private String pjgapellido1     ="pjgapellido1";   
	private String pjgnif           ="pjgnif";  
	private String pjgnombre        ="pjgnombre";
	private String gtnombre         ="gtnombre";
	private String tnombre          ="tnombre";
	private String cncolegiado      ="cncolegiado";
	private String pnombre          ="pnombre";
	private String papellidos2      ="papellidos2";
	private String papellidos1      ="papellidos1";
	private String acnumero         ="acnumero";
	private String acfecha          ="acfecha";
	private String aclugar          ="aclugar";
	private String acanulacion      ="acanulacion";
	private String acaextrajudicial ="acaextrajudicial";
	private String acfjustificacion ="acfjustificacion";
	private String acidactuacion    ="acidactuacion";
	private String acdbreve         ="acdbreve";
	private String acobservaciones  ="acobservaciones";
	private String acdiadespues     ="acdiadespues";
	private String acnumeroasunto   ="acnumeroasunto";
	private String acojustificacion ="acojustificacion";	
	/*
	 * Metodos SET y GET
	 */
	public void setAanio            (String	valor)	{ this.datos.put(this.aanio, valor);}
	public void setAnumero          (String	valor)	{ this.datos.put(this.anumero, valor);}
	public void setPjgapellido2     (String	valor)	{ this.datos.put(this.pjgapellido2, valor);}   
	public void setPjgapellido1     (String	valor)	{ this.datos.put(this.pjgapellido1, valor);}   
	public void setPjgnif           (String	valor)	{ this.datos.put(this.pjgnif, valor);}  
	public void setPjgnombre        (String	valor)	{ this.datos.put(this.pjgnombre, valor);}
	public void setGtnombre         (String	valor)	{ this.datos.put(this.gtnombre, valor);}
	public void setTnombre          (String	valor)	{ this.datos.put(this.tnombre, valor);}
	public void setCncolegiado      (String	valor)	{ this.datos.put(this.cncolegiado, valor);}
	public void setPnombre          (String	valor)	{ this.datos.put(this.pnombre, valor);}
	public void setPapellidos2      (String	valor)	{ this.datos.put(this.papellidos2, valor);}
	public void setPapellidos1      (String	valor)	{ this.datos.put(this.papellidos1, valor);}
	public void setAcnumero         (String	valor)	{ this.datos.put(this.acnumero, valor);}
	public void setAcfecha          (String	valor)	{ this.datos.put(this.acfecha, valor);}
	public void setAclugar          (String	valor)	{ this.datos.put(this.aclugar, valor);}
	public void setAcanulacion      (String	valor)	{ this.datos.put(this.acanulacion, valor);}
	public void setAcaextrajudicial (String	valor)	{ this.datos.put(this.acaextrajudicial, valor);}
	public void setAcfjustificacion (String	valor)	{ this.datos.put(this.acfjustificacion, valor);}
	public void setAcidactuacion    (String	valor)	{ this.datos.put(this.acidactuacion, valor);}
	public void setAcdbreve         (String	valor)	{ this.datos.put(this.acdbreve, valor);}
	public void setAcobservaciones  (String	valor)	{ this.datos.put(this.acobservaciones, valor);}
	public void setAcdiadespues     (String	valor)	{ this.datos.put(this.acdiadespues, valor);}
	public void setAcnumeroasunto   (String	valor)	{ this.datos.put(this.acnumeroasunto, valor);}
	public void setAcojustificacion (String	valor)	{ this.datos.put(this.acojustificacion, valor);}
	
	public String getAanio            ()	{return (String)this.datos.get(this.aanio);}
	public String getAnumero          ()	{return (String)this.datos.get(this.anumero);}
	public String getPjgapellido2     ()	{return (String)this.datos.get(this.pjgapellido2);}   
	public String getPjgapellido1     ()	{return (String)this.datos.get(this.pjgapellido1);}   
	public String getPjgnif           ()	{return (String)this.datos.get(this.pjgnif);}  
	public String getPjgnombre        ()	{return (String)this.datos.get(this.pjgnombre);}
	public String getGtnombre         ()	{return (String)this.datos.get(this.gtnombre);}
	public String getTnombre          ()	{return (String)this.datos.get(this.tnombre);}
	public String getCncolegiado      ()	{return (String)this.datos.get(this.cncolegiado);}
	public String getPnombre          ()	{return (String)this.datos.get(this.pnombre);}
	public String getPapellidos2      ()	{return (String)this.datos.get(this.papellidos2);}
	public String getPapellidos1      ()	{return (String)this.datos.get(this.papellidos1);}
	public String getAcnumero         ()	{return (String)this.datos.get(this.acnumero);}
	public String getAcfecha          ()	{return (String)this.datos.get(this.acfecha);}
	public String getAclugar          ()	{return (String)this.datos.get(this.aclugar);}
	public String getAcanulacion      ()	{return (String)this.datos.get(this.acanulacion);}
	public String getAcaextrajudicial ()	{return (String)this.datos.get(this.acaextrajudicial);}
	public String getAcfjustificacion ()	{return (String)this.datos.get(this.acfjustificacion);}
	public String getAcidactuacion    ()	{return (String)this.datos.get(this.acidactuacion);}
	public String getAcdbreve         ()	{return (String)this.datos.get(this.acdbreve);}
	public String getAcobservaciones  ()	{return (String)this.datos.get(this.acobservaciones);}
	public String getAcdiadespues     ()	{return (String)this.datos.get(this.acdiadespues);}
	public String getAcnumeroasunto   ()	{return (String)this.datos.get(this.acnumeroasunto);}
	public String getAcojustificacion ()	{return (String)this.datos.get(this.acojustificacion);}

}