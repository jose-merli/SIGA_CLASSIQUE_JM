package com.siga.pki;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import com.atos.utils.ClsExceptions;

public class SIGASignImplementMod extends SIGAPkiDataProducer
{
  String lit="none";
  String sFileName="";
  Hashtable hash= new Hashtable();
  Vector    vec = new Vector();
  public static String [] keys = new String[] {
	"ACTION",
	"PART_NUMBER",
	"SERIAL_NUMBER",
	"MODNUMBER"
  };

  public SIGASignImplementMod() {
  }

  public void setFileName() {
	if(this.sFileName.equals("")){
	  SimpleDateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
	  String nowDate=(String)  df.format(new Date());
	  String sFileName="implementMod"+nowDate;
	  this.sFileName=sFileName;
	}
  }

  public String getFileName() {
	if(this.sFileName.equals("")){
	  setFileName();
	}
	return this.sFileName;
  }

  public void setParameters(Hashtable params) {
	hash=params;
	Enumeration e=params.keys();
	while (e.hasMoreElements()) {
	  String key=(String)e.nextElement();
	  System.out.println("SampleDataProducer::SetParameters -> " + key + "="+(String)params.get(key));
	}
  }
  public void performAction() throws Exception {
	Enumeration e=hash.keys();
	while (e.hasMoreElements()) vec.add(e.nextElement());

	String lite="<TABLE width='95%' border='0' cellpadding='0' cellspacing='0' bordercolor='#34244F' style='table-layout:fixed'>"
			   +"<TR><TD width='130' class='labelText'>Action&nbsp;</TD>"
			   +"<TD width='100' class='nonEdit'>"+putField(keys[0])+"&nbsp;</TD>"
			   +"<TD width='130' class='labelText'>PartNunmber:&nbsp;</TD>"
			   +"<TD width='100' class='nonEdit'>"+putField(keys[1])+"</TD>"
			   +"<TD width='130' class='labelText'>SerialNunmber:&nbsp;</TD>"
			   +"<TD width='100' class='nonEdit'>"+putField(keys[2])+"</TD>"
			   +"<TD width='130' class='labelText'>&nbsp;</TD>"
			   +"<TD width='100' class='nonEdit'>&nbsp;</TD>"
			   +"</TR>"
			   +"<TR><TD width='130' class='labelText'>Mov. Numb:&nbsp;</TD>"
			   +"<TD width='100' class='nonEdit'>"+putField(keys[3])+"</TD>"
			   +"</TR></TABLE>";
	lit=lite;

  }
  public Hashtable getData() {
	return hash;
  }
  public Vector getOrder() {
	return vec;
  }

  protected String putField(String field) {
	String toRet=SIGASignImplementMod.INIT_VALUE+(String)hash.get(field)+SIGASignImplementMod.END_VALUE;
	return toRet;
  }

  public String getLiteral() {
	return lit;
  }

  public String actionDone(String filename, HttpSession ses) throws ClsExceptions{
	System.out.println("Action done en ImplementMod ");
	System.out.println("   PN-SN es " + hash.get("PN")+"-"+ hash.get("SN"));
	System.out.println("   Nombre file " + filename);
	System.out.println("   movnumber     es " + hash.get("MODNUMBER"));


	ses.setAttribute("firmamode","update");
	ses.setAttribute("firmaFileName",filename);
	ses.setAttribute("firmaSignImplMOD","true");
	return "";
  }
}