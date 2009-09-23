package com.siga.pki;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.siga.generalRequirements.accessControl.SIGAGrDDBBObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Table;
import com.atos.utils.TableConstants;
import com.atos.utils.UsrBean;
//import com.atos.utils.PSSCUsrBusinessBean;



public class SIGAConforCertificate extends SIGAPkiDataProducer
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


  public static final String STRUCTURE_LINE_INIT="<td class=\"nonEdit\">"+INIT_VALUE;
  public static final String STRUCTURE_LINE_END=END_VALUE+"</td>";

  public static final String BEGIN_BLOCK="<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\"><tr><td width=\"5%\"></td><td>";

  public static final String CERTFICATE_DIV="<div id=\"title\" class=\"Title\">"+"Certificate"+"</div>";
  public static final String CERTFICATE_TABLE_BEGIN="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">"+"<TR>";
  public static final String TABLE_NEW_LINE="</TR><TR>";
  public static final String END_LINE_TABLE_END="</TR></TABLE>";
  public static final String TABLE_END="</TABLE>";



  protected Hashtable hash=new Hashtable();
  protected Vector vec=new Vector();
  protected String lit="";
  public static String[] potNames= new String[] {
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

  public static String[] potFields = new String[] {
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

  public SIGAConforCertificate() {}

  public void setFileName() {
	if(this.sFileName.equals("")){
	  SimpleDateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
	  String nowDate=(String)  df.format(new Date());
	  String sFileName="createCC"+nowDate;
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
//	ses=(HttpSession)params.get("HTTPSESSIONFROMCTS");
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

	  Table gtTable = new Table(null, TableConstants.TABLE_ACCESS_RIGHT,
						"com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");


	lit+=BEGIN_BLOCK;

	if ("cc".compareToIgnoreCase((String)hash.get("TYPE"))==0) {
try {
		if (ses!=null) {
		   Hashtable gdHash=(Hashtable)ses.getAttribute("htTable");
		  if (gdHash==null) {
			  // voy a bd
		  } else {
			  String nqar=(String)gdHash.get("AUTH_NQAR");
			  String datecertif=(String)gdHash.get("CERT_DATE");
			  String companycert=(String)gdHash.get("CERT_COMPANY");
			  String agentcert=(String)gdHash.get("CERT_AGENT");
			  String confirmation=(String)gdHash.get("CONFIRM_STAT");

			  lit+=INIT_CERTIFICATE;
			  lit+=CERTFICATE_DIV;

				lit+=CERTFICATE_TABLE_BEGIN;

				lit+=CERTIFICATE_NQAR_A+nqar+CERTIFICATE_CLOSE_TD;
				lit+=CERTIFICATE_CD+datecertif+CERTIFICATE_CLOSE_TD;
				lit+=TABLE_NEW_LINE;
				lit+=CERTIFICATE_C_C+companycert+CERTIFICATE_CLOSE_TD;
				lit+=CERTIFICATE_CAN+agentcert+CERTIFICATE_CLOSE_TD;
				lit+=TABLE_NEW_LINE;
				lit+=CERTIFICATE_CS+confirmation+CERTIFICATE_CLOSE_TD;
				lit+=END_LINE_TABLE_END;
			  lit+= END_CERTIFICATE;

		  }
	}
} catch (Exception e) {
e.printStackTrace();
}
// Corformity certificate
	// CC
	// General description del de cabecera
	} else {
//		Snapshot
	}


	Vector reta=getVStructure(getValueOf("internal"), gtTable);


	String[] internals=new String[reta.size()];
	String[] pns=new String[reta.size()];
	String[] sns=new String[reta.size()];




	lit+=INIT_STRUCTURE;
	lit+="<div id=\"title\" class=\"Title\">"
				 +"Structure"+"</div>";

	lit+=sBeginStructure();

	for (int i=0;i<reta.size();i++) {
	  SIGAGrDDBBObject obj=(SIGAGrDDBBObject)reta.elementAt(i);
	  internals[i]=obj.getString("INTERNAL");
	  pns[i]=obj.getString("ITEM_PN");
	  sns[i]=obj.getString("ITEM_SN");

	  lit+=getSlineStructure(obj);

	}
	lit+=TABLE_END;
	lit+=END_STRUCTURE;
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
				 +"Modifications implemented"+"</div>";
		lit+=sBeginMods(false);
	/*
	lit+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
	lit+="<tr class=\"tableTitle\">"+
	  "<td width=\"20%\" >Part Number</td>"+
	  "<td width=\"20%\">Serial Number</td>"+
	  "<td width=\"60%\">Modification implemented</td>"+
	"</tr>";
*/
	for (int i=0;i<internals.length;i++) {
	  Vector ret=getVMOD(internals[i], gtTable);
	  if (ret!=null && !ret.isEmpty())  {
		String mods = "";
		for (int v = 0; v < ret.size(); v++) {
		  SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
		  if (v != 0)
			mods += ", ";
		  mods += obj.getString("MOD_NUMBER");

		}
lit +=getSlineModToConcesion(pns[i], sns[i], mods);
/*
		lit += "<tr>";
		lit += STRUCTURE_LINE_INIT + pns[i] + STRUCTURE_LINE_END;
		lit += STRUCTURE_LINE_INIT + sns[i] + STRUCTURE_LINE_END;
		lit += STRUCTURE_LINE_INIT + mods + STRUCTURE_LINE_END;
		lit += "</tr>";
*/
	  }
	}
	lit+="</table>";
	lit+= END_MOD;

	lit+= INIT_TO;

	lit+="<div id=\"title\" class=\"Title\">"
				 +"Technical orders implemented"+"</div>";
	lit+=sBeginTO(false);
/*

	lit+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
	lit+="<tr class=\"tableTitle\">"+
	  "<td width=\"20%\" >Part Number</td>"+
	  "<td width=\"20%\">Serial Number</td>"+
	  "<td width=\"60%\">Technical orders implemented</td>"+
	"</tr>";
*/
	for (int i=0;i<internals.length;i++) {
	  Vector ret=getVTO(internals[i], gtTable);
	  if (ret!=null && !ret.isEmpty())  {
		String mods = "";
		for (int v = 0; v < ret.size(); v++) {
		  SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
		  if (v != 0)
			mods += ", ";
		  mods += obj.getString("EJPSI");

		}
		lit +=getSlineModToConcesion(pns[i], sns[i], mods);

	  }
	}
	lit+="</table>";
	lit+= END_TO;

	lit+= INIT_CONCESSION;
	// Concesiones
	lit+="<div id=\"title\" class=\"Title\">"
				 +"Concession"+"</div>";
		lit+=sBeginConcess(false);
	/*

	lit+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
	lit+="<tr class=\"tableTitle\">"+
	  "<td width=\"20%\" >Part Number</td>"+
	  "<td width=\"20%\">Serial Number</td>"+
	  "<td width=\"60%\">Concessions</td>"+
	"</tr>";
*/
	for (int i=0;i<internals.length;i++) {
	  Vector ret=getVconcession(internals[i], gtTable);
	  if (ret!=null && !ret.isEmpty())  {
		String mods = "";
		for (int v = 0; v < ret.size(); v++) {
		  SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
		  if (v != 0)
			mods += ", ";
		  mods += obj.getString("CONCESS_NUMBER");

		}

		lit +=getSlineModToConcesion(pns[i], sns[i], mods);
		/*
		lit += "<tr>";
		lit += STRUCTURE_LINE_INIT + pns[i] + STRUCTURE_LINE_END;
		lit += STRUCTURE_LINE_INIT + sns[i] + STRUCTURE_LINE_END;
		lit += STRUCTURE_LINE_INIT + mods + STRUCTURE_LINE_END;
		lit += "</tr>";
		*/
	  }
	}
	lit+="</table>";
	lit+= END_CONCESSION;
	lit+= INIT_POT;
	// POT - > Si es motor
//    for (int i=0;i<allInternal.length;i++) {
	  Vector ret=getVPot(internals[0], gtTable);
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

	lit+="</td></tr></table>";

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
	if ("cc".compareToIgnoreCase((String)hash.get("TYPE"))==0) {
		return actionDoneCC(filename, ses);
	} else {
		return actionDoneFoto(filename, ses);
	}
  }

  public String actionDoneCC(String filename, HttpSession ses) throws ClsExceptions{
	  System.out.println("Action done en confom certifucate ");
	  System.out.println("   PN-SN es " + hash.get("PN")+"-"+ hash.get("SN"));
	  System.out.println("   Nombre file " + filename);
	  UsrBean usrbean;
		  UserTransaction tx = null;

		  try{
			   if (((usrbean=(UsrBean)ses.getAttribute("USRBEAN")) != null)){

					Hashtable ht=(Hashtable)ses.getAttribute("htTable");
					Hashtable htOrigin=(Hashtable)ses.getAttribute("htOriginGD");
/*
					PSSCItem pItem=(PSSCItem)ses.getAttribute("pItem");
					Hashtable htOriginItem=null;
					if (pItem.getData()!=null) {
						htOriginItem=(Hashtable)pItem.getData().clone();
					}


					PSSCGeneralDescription gd = new PSSCGeneralDescription();
					
					
					
System.out.println("******************************");

if (ht.get("CERT_DATE")!=null &&!((String)ht.get("CERT_DATE")).equals("")){
ht.put("CERT_DATE",new GstDate().getApplicationFormatDate("", (String)ht.get("CERT_DATE")));
}

if(htOrigin==null){
ht.put("INTERNAL", pItem.getSInternal());
ht.put("USER_MODIFICATION", usrbean.getUserName());
ht.put("DATE_MODIFICATION", "sysdate");
}

					gd.setData(ht);
					gd.setHtorign(htOrigin);
					gd.setBSigned(true);

					pItem.setHtorign(htOriginItem);
					pItem.setSDigitalSign(getFileName());
*/
					tx = usrbean.getTransaction();
					tx.begin();
					/*
					pItem.update();
					

					if(htOrigin==null){
						gd.add();
					}else 	gd.update();
					*/
					tx.commit();
					ses.removeAttribute("htTable");
					ses.removeAttribute("htOriginGD");
					ses.removeAttribute("pItem");

			  }else{
				   throw new ClsExceptions("Any parameter from request or session is null", "movement","84","GEN00","7");
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
					e.printStackTrace();
				  throw new ClsExceptions(e.getMessage(),"movement","84","GEN00","7");
		  }


				  return "";
  }

  public String actionDoneFoto(String filename, HttpSession ses) throws ClsExceptions{
	System.out.println("Action done en confom certifucate ");
	System.out.println("   PN-SN es " + hash.get("PN")+"-"+ hash.get("SN"));
	System.out.println("   Nombre file " + filename);


	UsrBean usrbean;
	UserTransaction tx = null;

/*
	PSSCUsrBusinessBean usrBusiness =null;



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
  public static Vector getVStructure(String sInternal, Table gtTable)throws Exception{
	Vector vStructure=null;
	String query_acc="select a.INTERNAL, a.ITEM_PN, ITEM_SN, NOMENCLATURE, LEVEL " +
		" from SIGA_ITEM a , SIGA_PART_NUMBER b " +
		" where a.ITEM_PN=b.ITEM_PN and a.DELETED='0' " +
		" start with INTERNAL='"+sInternal+"'"+
		" connect by prior internal= ID_NHA ";

//System.out.println("Query " + query_acc);

	vStructure=gtTable.search(query_acc);


	return vStructure;
  }

	public static Vector getVMOD(String sInternal, Table gtTable)throws Exception{

		String query_mod="select MOD_NUMBER from SIGA_ITEM_MOD where INTERNAL = '"+
		sInternal+"' and IMPLEMENTED='1' and DELETED='0'";

		Vector ret=gtTable.search(query_mod);
		return ret;
	}

	public static Vector getVTO(String sInternal,Table gtTable)throws Exception{



		String query_ot="select EJPSI from SIGA_ITEM_TECHNICAL_ORDER where INTERNAL = '"+
			sInternal+"' and IMPLEMENTED='1' and DELETED='0'";

		  Vector ret=gtTable.search(query_ot);
		return ret;
	}

	public static Vector getVconcession(String sInternal, Table gtTable)throws Exception{


		String query_concession="select CONCESS_NUMBER from SIGA_CONCESSION where INTERNAL = '"+
		sInternal+"' and DELETED='0'";

		  Vector ret=gtTable.search(query_concession);
		return ret;
	}


	public static Vector getVPot(String sInternal, Table gtTable)throws Exception{
		String query_pot="select * from SIGA_POT where INTERNAL = '"+
		sInternal+"' and DELETED='0' order by DATE_POT desc";

		Vector ret=gtTable.search(query_pot);
		return ret;
	}



	public static String sBeginStructure(){
		String sStructure="";

		sStructure+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
		sStructure+="<tr class=\"tableTitle\">"+
		  "<td width=\"10%\">Level</td>"+
		  "<td width=\"20%\" >Part Number</td>"+
		  "<td width=\"20%\">Serial Number</td>"+
		  "<td width=\"50%\">Item</td>"+
	"</tr>";
		return sStructure;
	}


	public static String getSlineStructure(SIGAGrDDBBObject obj){
		String sStructure="";
		sStructure+="<tr>";

		sStructure +=STRUCTURE_LINE_INIT+obj.getString("LEVEL")+STRUCTURE_LINE_END;
		sStructure +=STRUCTURE_LINE_INIT+obj.getString("ITEM_PN")+STRUCTURE_LINE_END;
		sStructure +=STRUCTURE_LINE_INIT+obj.getString("ITEM_SN")+STRUCTURE_LINE_END;
		sStructure +=STRUCTURE_LINE_INIT+obj.getString("NOMENCLATURE")+STRUCTURE_LINE_END;
		sStructure+="</tr>";

		return sStructure;
	}

	public static String getSlineModToConcesion( String sPNs, String sSNs, String Smods){
		String sLine="";
		sLine +=getSlinePnSN(sPNs, sSNs);
		sLine +=STRUCTURE_LINE_INIT + Smods + STRUCTURE_LINE_END;
		sLine+="</tr>";
		return sLine;

	}

	public static String getSlinePnSN( String sPNs, String sSNs){
		String sLine="";
		sLine+="<tr>";
		sLine +=STRUCTURE_LINE_INIT + sPNs + STRUCTURE_LINE_END;
		sLine +=STRUCTURE_LINE_INIT + sSNs + STRUCTURE_LINE_END;
	return sLine;
	}


	public static String sBeginMods(boolean check){
		return sBeginTable("Modifications implemented", check);
	}


	public static String sBeginTO(boolean check){
		return sBeginTable("Technical orders implemented", check);
	}

	public static String sBeginConcess(boolean check){
		return sBeginTable("Concessions", check);
	}

	public static String sBeginTable(String sData, boolean check){
		String sStructure="";


sStructure+="<TABLE width=\"95%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#34244F\" style=\"table-layout:fixed\">";
sStructure+="<tr class=\"tableTitleLines\">"+
  "<td width=\"20%\" >Part Number</td>"+
  "<td width=\"20%\">Serial Number</td>";

if (check){
  sStructure+="<td width=\"30%\">"+sData+" sign data</td>";
  sStructure+="<td width=\"30%\">"+sData+" current in database</td>";
} else sStructure+="<td width=\"30%\">"+sData+"</td>";

sStructure+="</tr>";
		return sStructure;
	}


}