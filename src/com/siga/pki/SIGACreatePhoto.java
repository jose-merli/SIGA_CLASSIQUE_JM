package com.siga.pki;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.siga.generalRequirements.accessControl.SIGAGrDDBBObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Table;
import com.atos.utils.TableConstants;
import com.atos.utils.UsrBean;



public class SIGACreatePhoto extends SIGAPkiDataProducer
{
  private String sFileName="";


  //Strings to look for into certificate block
  public static final String CERTIFICATE_NQAR_A="<TD class=\"labelText\" width=\"130\">NQAR Authorization:&nbsp;</TD><TD class=\"nonEdit\" width=\"150\">";
  public static final String CERTIFICATE_CD="<TD class=\"labelText\" width=\"130\">Certification Date:&nbsp;</TD><TD class=\"nonEdit\" width=\"150\">";
  public static final String CERTIFICATE_C_C="<TD class=\"labelText\">Certifying Company:&nbsp;</TD><TD class=\"nonEdit\" width=\"150\">";
  public static final String CERTIFICATE_CAN="<TD class=\"labelText\">Certifying Agent Name:&nbsp;</TD><TD class=\"nonEdit\" width=\"150\">";
  public static final String CERTIFICATE_CS="<TD class=\"labelText\">Confirmation Statement:&nbsp;</TD><TD class=\"nonEdit\" colspan=\"3\">";
  public static final String CERTIFICATE_CLOSE_TD="&nbsp;</TD>";

  public static String INIT_CERTIFICATE = "<!--KeyValueCertificateInit-->";
  public static String END_CERTIFICATE = "<!--KeyValueCertificateEnd-->";
  public static String INIT_STRUCTURE = "<!--KeyValueStructInit-->";
  public static String END_STRUCTURE = "<!--KeyValueStructEnd-->";
  public static String INIT_MOD = "<!--KeyValueModsInit-->";
  public static String END_MOD = "<!--KeyValueModsEnd-->";
  public static String INIT_TO = "<!--KeyValueTOInit-->";
  public static String END_TO = "<!--KeyValueTOEnd-->";
  public static String INIT_CONCESSION = "<!--KeyValueConcessionInit-->";
  public static String END_CONCESSION = "<!--KeyValueConcessionEnd-->";
  public static String INIT_POT = "<!--KeyValuePOTInit-->";
  public static String END_POT = "<!--KeyValuePOTEnd-->";



  protected Hashtable hash=new Hashtable();
  protected Vector vec=new Vector();
  protected String lit="";
  protected String[] potNames= new String[] {
	"testSpecNo",
	"testSpecIssue",
	"testBedId",
	"locationPOT",
	"dtDatePOT",
	"fuelSpec",
	"oilSpec",
	"dryCruiseThrust",
	"dryCruiseSFC",
	"maxDryThrust",
	"maxDrySOT",
	"maxDryTBT",
	"maxDryNL",
	"maxDryNH",
	"rhCruiseSFC",
	"maxRhThrust",
	"maxRhTBT",
	"maxRhNL",
	"maxRhNH",
	"oilConsumption",
	"totalRunningTime",
	"starts"};

  protected String[] potFields = new String[] {
	"TEST_SPEC_NUMBER",
	"TEST_SPEC_ISSUE",
	"TEST_BED_ID",
	"LOCATION_POT",
	"DATE_POT",
	"FUEL_SPEC",
	"OIL_SPEC",
	"DRY_CRUISE_THRUST",
	"DRY_CRUISE_SFC",
	"MAX_DRY_THRUST",
	"MAX_DRY_SOT",
	"MAX_DRY_TBT",
	"MAX_DRY_NL",
	"MAX_DRY_NH",
	"RH_CRUISE_SFC",
	"MAX_RH_THRUST",
	"MAX_RH_TBT",
	"MAX_RH_NL",
	"MAX_RH_NH",
	"OIL_CONSUMPTION",
	"TOTAL_RUN_H",
	"STARTS"
  };

  public SIGACreatePhoto() {}

  public void setFileName() {
	if(this.sFileName.equals("")){
	  SimpleDateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
	  String nowDate=(String)  df.format(new Date());
	  String sFileName="createPhoto"+nowDate;
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
	hash = new Hashtable();
	Enumeration e= params.keys();
	while (e.hasMoreElements()) {
	  String key=(String)e.nextElement();
	  hash.put(key.toUpperCase(),params.get(key));
	}
  }

  public String getValueOf(String _key) {
	return (String)hash.get(_key.toUpperCase());
  }

  public void performAction() throws Exception {
	// Tengo PN-SN-internal ->

	if ("cc".compareToIgnoreCase((String)hash.get("TYPE"))==0) {
	  // CC
	  // General description del de cabecera
	}


	// Foto

	Table gtTable = new Table(null, TableConstants.TABLE_ACCESS_RIGHT,
									  "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");

	String query_acc="select a.INTERNAL, a.ITEM_PN, ITEM_SN, NOMENCLATURE, LEVEL " +
					 " from SIGA_ITEM a , SIGA_PART_NUMBER b " +
					 " where a.ITEM_PN=b.ITEM_PN and a.DELETED='0' " +
					 " start with INTERNAL='"+getValueOf("internal") +"'"+
					 " connect by prior internal= ID_NHA ";
	System.out.println("Query " + query_acc);

	Vector reta=gtTable.search(query_acc);
	String[] internals=new String[reta.size()];
	String[] pns=new String[reta.size()];
	String[] sns=new String[reta.size()];

	lit+= INIT_STRUCTURE;
	lit+="<div id=\"title\" class=\"Title\">"
		+"Structure"+"</div>";

	lit+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
	lit+="<tr class=\"tableTitle\">"+
		 "<td width=\"10%\">Level</td>"+
		 "<td width=\"20%\" >Part Number</td>"+
		 "<td width=\"20%\">Serial Number</td>"+
		 "<td width=\"50%\">Item</td>"+
		 "</tr>";

	for (int i=0;i<reta.size();i++) {
	  SIGAGrDDBBObject obj=(SIGAGrDDBBObject)reta.elementAt(i);
	  lit+="<tr>";
	  internals[i]=obj.getString("INTERNAL");
	  pns[i]=obj.getString("ITEM_PN");
	  sns[i]=obj.getString("ITEM_SN");
	  lit +="<td class=\"nonEdit\">"+INIT_VALUE+obj.getString("LEVEL")+END_VALUE+"</td>";
	  lit +="<td class=\"nonEdit\">"+INIT_VALUE+obj.getString("ITEM_PN")+END_VALUE+"</td>";
	  lit +="<td class=\"nonEdit\">"+INIT_VALUE+obj.getString("ITEM_SN")+END_VALUE+"</td>";
	  lit +="<td class=\"nonEdit\">"+INIT_VALUE+obj.getString("NOMENCLATURE")+END_VALUE+"</td>";
	  lit+="</tr>";
	}
	lit+="</table>";
	lit+= END_STRUCTURE;
 /*



	// Structura jerarquica
  select a.INTERNAL f, a.ITEM_PN u, a.ITEM_SN d, b.NOMENCLATURE t, level c
  from SIGA_ITEM a , SIGA_PART_NUMBER b
  where a.ITEM_PN=b.ITEM_PN
  start with INTERNAL='PSS0000000001'
  connect by prior internal= ID_NHA



  */

	// MOD y OT aplicadas
	lit+= INIT_MOD;
	lit+="<div id=\"title\" class=\"Title\">"
		+"Mods"+"</div>";

	lit+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
	lit+="<tr class=\"tableTitle\">"+
		 "<td width=\"20%\" >Part Number</td>"+
		 "<td width=\"20%\">Serial Number</td>"+
		 "<td width=\"60%\">Modification implemented</td>"+
		 "</tr>";

	for (int i=0;i<internals.length;i++) {
	  String query_ot="select MOD_NUMBER from SIGA_ITEM_MOD where INTERNAL = '"+
			   internals[i]+"' and IMPLEMENTED='1' and DELETED='0'";
//   System.out.println("Query " + query_acc);
	  Vector ret=gtTable.search(query_ot);
	  if (ret!=null && !ret.isEmpty())  {
		String mods = "";
		for (int v = 0; v < ret.size(); v++) {
		  SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
		  if (v != 0)
			mods += ",";
		  mods += obj.getString("MOD_NUMBER");

		}
		lit += "<tr>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+pns[i] + END_VALUE+"</td>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+sns[i] + END_VALUE+"</td>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+mods + END_VALUE+"</td>";
		lit += "</tr>";
	  }
	}
	lit+="</table>";
	lit+= END_MOD;
	lit+= INIT_TO;

	lit+="<div id=\"title\" class=\"Title\">"
		+"TO"+"</div>";
	lit+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
	lit+="<tr class=\"tableTitle\">"+
		 "<td width=\"20%\" >Part Number</td>"+
		 "<td width=\"20%\">Serial Number</td>"+
		 "<td width=\"60%\">Technical orders implemented</td>"+
		 "</tr>";
	for (int i=0;i<internals.length;i++) {
	  String query_ot="select EJPSI from SIGA_ITEM_TECHNICAL_ORDER where INTERNAL = '"+
			   internals[i]+"' and IMPLEMENTED='1' and DELETED='0'";
//   System.out.println("Query " + query_ot);
	  Vector ret=gtTable.search(query_ot);
	  if (ret!=null && !ret.isEmpty())  {
		String mods = "";
		for (int v = 0; v < ret.size(); v++) {
		  SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
		  if (v != 0)
			mods += ",";
		  mods += obj.getString("EJPSI");

		}
		lit += "<tr>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+pns[i] + END_VALUE+"</td>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+sns[i] + END_VALUE+"</td>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+mods + END_VALUE+"</td>";
		lit += "</tr>";
	  }
	}
	lit+="</table>";
	lit+= END_TO;
	lit+= INIT_CONCESSION;
	// Concesiones
	lit+="<div id=\"title\" class=\"Title\">"
		+"Concession"+"</div>";
	lit+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
	lit+="<tr class=\"tableTitle\">"+
		 "<td width=\"20%\" >Part Number</td>"+
		 "<td width=\"20%\">Serial Number</td>"+
		 "<td width=\"60%\">Concessions</td>"+
		 "</tr>";

	for (int i=0;i<internals.length;i++) {
	  String query_ot="select CONCESS_NUMBER from SIGA_CONCESSION where INTERNAL = '"+
			   internals[i]+"' and DELETED='0'";
	  Vector ret=gtTable.search(query_ot);
	  if (ret!=null && !ret.isEmpty())  {
		String mods = "";
		for (int v = 0; v < ret.size(); v++) {
		  SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
		  if (v != 0)
			mods += ",";
		  mods += obj.getString("CONCESS_NUMBER");

		}
		lit += "<tr>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+pns[i] + END_VALUE+"</td>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+sns[i] + END_VALUE+"</td>";
		lit += "<td class=\"nonEdit\">" + INIT_VALUE+mods + END_VALUE+"</td>";
		lit += "</tr>";
	  }
	}
	lit+="</table>";
	lit+= END_CONCESSION;
	lit+= INIT_POT;
	// POT - > Si es motor
//    for (int i=0;i<allInternal.length;i++) {
	String query_ot="select * from SIGA_POT where INTERNAL = '"+
					internals[0]+"' and DELETED='0' order by DATE_POT desc";
	Vector ret=gtTable.search(query_ot);
	if (ret!=null && !ret.isEmpty())  {
	  SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(0);
	  Hashtable has = new Hashtable();
	  for (int y=0;y<potNames.length;y++) {
		has.put(potNames[y],obj.getString(potFields[y]));
	  }
	  SIGASignCreatePot pot=new SIGASignCreatePot();
	  pot.setParameters(has);
	  pot.performAction();
	  lit+=pot.getLiteral();
	}
	lit+= END_POT;
	// Vida - Own Life
  }

  public Hashtable getData() {
	return hash;
  }

  public Vector getOrder() {
	return vec;
  }

  public String getLiteral() {
	return lit;
  }


  public String actionDone(String filename, HttpSession ses) throws ClsExceptions{
	System.out.println("Action done en confom certifucate ");
	System.out.println("   PN-SN es " + hash.get("PN")+"-"+ hash.get("SN"));
	System.out.println("   Nombre file " + filename);


	UsrBean usrbean;
	UserTransaction tx = null;

//	PSSCUsrBusinessBean usrBusiness =null;


/*
	try{
	  if (((usrbean=(UsrBean)ses.getAttribute("USRBEAN")) != null)&&
		  (usrBusiness=(PSSCUsrBusinessBean)ses.getAttribute("USRBUSINESSBEAN")) != null){


		PSSCItem pItem=usrBusiness.getItem();

		Hashtable htOrigin=(Hashtable)(pItem.getData()).clone();

		pItem.setHtorign(htOrigin);
		pItem.setSDigitalPhoto(getFileName());
		tx = usrbean.getTransaction();
		tx.begin();
		pItem.update();
		tx.commit();

	  }else{
		throw new ClsExceptions("Any parameter from request or session is null","movement","84","GEN00","7");
	  }

	}catch(ClsExceptions e){
	  throw e;
	}catch(SystemException e){
	  throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(NotSupportedException e){
	  throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(HeuristicMixedException e){
	  throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(HeuristicRollbackException e){
	  throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(RollbackException e){
	  throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(Exception e){
	  throw new ClsExceptions(e.getMessage(),"movement","84","GEN00","7");
	}
	
	*/
	return "";
  }
}