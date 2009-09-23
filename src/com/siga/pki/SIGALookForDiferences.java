package com.siga.pki;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.siga.generalRequirements.accessControl.SIGAGrDDBBObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Table;
import com.atos.utils.TableConstants;

/**
 * <p>Título: </p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

public class SIGALookForDiferences {

		static String sInternal="";
		static String[] internals=null;
		static String[] pns=null;
		static String[] sns=null;
		static Table gtTable=null;
		static String sReult="";
		static boolean bDiferences=false;


	public static String getDiferencesCC(String sLitDB, String sLitPki, HttpServletRequest req) throws ClsExceptions{

		////PSSCUsrBusinessBean usrBusiness = null;
		HttpSession ses = null;


		gtTable = new Table(null, TableConstants.TABLE_ACCESS_RIGHT,
					"com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");


/*
	   if (((ses = req.getSession()) != null)  && ((usrBusiness = (PSSCUsrBusinessBean) ses.getAttribute("USRBUSINESSBEAN")) != null)){

		sInternal = usrBusiness.getId();
	   }
*/

//		System.out.println("+++++++++++");
//		System.out.println("buscando diferencias");

		try{

		sReult=SIGAConforCertificate.BEGIN_BLOCK;



			int init;
			int end;

/*
	   //compare certificate
			init=sLitPki.indexOf(SIGAConforCertificate.INIT_CERTIFICATE);
			end=sLitPki.indexOf(SIGAConforCertificate.END_CERTIFICATE);

			if(init>-1 && end>-1 && init<end){
				String sCertificate= sLitPki.substring(init, end);
	//				System.out.print(sCertificate);

				compareCertificate(sCertificate, req);
			}
*/

			//compare structure
			 init=sLitPki.indexOf(SIGAConforCertificate.INIT_STRUCTURE);
			 end=sLitPki.indexOf(SIGAConforCertificate.END_STRUCTURE);
			 if(init>0 && end>0 && init<end){
				 String sStructure= sLitPki.substring(init, end);
				 Vector reta=SIGAConforCertificate.getVStructure(sInternal, gtTable);
				 compareStructure(sStructure, reta, req);

			}

		//compare MOD
			 init=sLitPki.indexOf(SIGAConforCertificate.INIT_MOD);
			 end=sLitPki.indexOf(SIGAConforCertificate.END_MOD);
			 if(init>0 && end>0 && init<end){
				 String sMod= sLitPki.substring(init, end);
				 compareModifications(sMod, req);
			}


			//compare TO
			 init=sLitPki.indexOf(SIGAConforCertificate.INIT_TO);
			 end=sLitPki.indexOf(SIGAConforCertificate.END_TO);
			 if(init>0 && end>0 && init<end){
				 String sTO= sLitPki.substring(init, end);
				 compareTO(sTO, req);

			}


			//compare concession
			 init=sLitPki.indexOf(SIGAConforCertificate.INIT_CONCESSION);
			 end=sLitPki.indexOf(SIGAConforCertificate.END_CONCESSION);
			 if(init>0 && end>0 && init<end){
				 String sConcession= sLitPki.substring(init, end);
				 compareConcessions(sConcession, req);
			}



			//compare pot
			 init=sLitPki.indexOf(SIGAConforCertificate.INIT_POT);
			 end=sLitPki.indexOf(SIGAConforCertificate.END_POT);
			 if(init>0 && end>0 && init<end){
				 String sPot= sLitPki.substring(init, end);
				 Vector reta=SIGAConforCertificate.getVPot(sInternal, gtTable);
				 comparePot(sPot, reta, req);
			}


		}catch(Exception ex){
			throw new ClsExceptions(ex,"Requested Master Table not found.");
		}

		if(!bDiferences) sReult+="<tr><td class=\"Title\" colspan=\"2\"> There aren´t diferences <td><tr>";

		sReult+="<tr><td>&nbsp;<td><tr></TABLE>";

//		System.out.println("*************++++++++++++");
		//		System.out.println(sReult);
	//	System.out.println("*************++++++++++++");

		return sReult;
	}


	private static Vector compareCertificate(String sCertificatePKI, HttpServletRequest req)throws ClsExceptions{
		Vector vCertificate=new Vector();
//		System.out.println("+++++++++++");
//		System.out.println("compareCertificate");

/*
		try{

			PSSCGeneralDescription gd= new PSSCGeneralDescription().PSSCGeneralDescriptionInstance(sInternal, req);

			String sNQA=sCertificatePKI.substring(sCertificatePKI.indexOf(SIGAConforCertificate.CERTIFICATE_NQAR_A)+SIGAConforCertificate.CERTIFICATE_NQAR_A.length());
			sNQA=sNQA.substring(0,sNQA.indexOf(SIGAConforCertificate.CERTIFICATE_CLOSE_TD));

			String sC_C=sCertificatePKI.substring(sCertificatePKI.indexOf(SIGAConforCertificate.CERTIFICATE_C_C)+SIGAConforCertificate.CERTIFICATE_C_C.length());
			sC_C=sC_C.substring(0,sC_C.indexOf(SIGAConforCertificate.CERTIFICATE_CLOSE_TD));

			String sCAN=sCertificatePKI.substring(sCertificatePKI.indexOf(SIGAConforCertificate.CERTIFICATE_CAN)+SIGAConforCertificate.CERTIFICATE_CAN.length());
			sCAN=sCAN.substring(0,sCAN.indexOf(SIGAConforCertificate.CERTIFICATE_CLOSE_TD));

			String sCD=sCertificatePKI.substring(sCertificatePKI.indexOf(SIGAConforCertificate.CERTIFICATE_CD)+SIGAConforCertificate.CERTIFICATE_CD.length());
			sCD=sCD.substring(0,sCD.indexOf(SIGAConforCertificate.CERTIFICATE_CLOSE_TD));

			String sCS=sCertificatePKI.substring(sCertificatePKI.indexOf(SIGAConforCertificate.CERTIFICATE_CS)+SIGAConforCertificate.CERTIFICATE_CS.length());
			sCS=sCS.substring(0,sCS.indexOf(SIGAConforCertificate.CERTIFICATE_CLOSE_TD));


			if(!sNQA.equals(gd.getSAuthNQAR())){
				System.out.println("-"+sNQA+" distinto de "+gd.getSAuthNQAR()+"-");
			}

			if(!sC_C.equals(gd.getSCertCompany())){
				System.out.println("-"+sC_C+" distinto de "+gd.getSCertCompany()+"-");
			}

			if(!sCAN.equals(gd.getSCertAgent())){
				System.out.println("-"+sCAN+" distinto de "+gd.getSCertAgent()+"-");
			}

			if(!sCD.equals(GstDate.getFormatedDateShort("EN",gd.getDtCertDate()))){
				System.out.println("-"+sCD+" distinto de "+GstDate.getFormatedDateShort("EN",gd.getDtCertDate()));
			}

			if(!sCS.equals(gd.getSConfirmStat())){
				System.out.println("-"+sCS+" distinto de "+gd.getSConfirmStat()+"-");
			}

		}catch(ClsExceptions e){
			e.printStackTrace();
					ClsLogging.writeFileLogError(e.getMsg(),3);
			throw e;
		}catch(Exception ex){
			ex.printStackTrace();
		ClsLogging.writeFileLogError(ex.getMessage(),3);
		throw new ClsExceptions("problems in compareCertificate.", "","","","");
		}
*/
		return vCertificate;
	}



	private static void compareStructure(String sCertificatePKI, Vector reta, HttpServletRequest req)throws ClsExceptions{
//		//PSSCUsrBusinessBean usrBusiness = null;
		HttpSession ses = null;

//		System.out.println("+++++++++++");
//		System.out.println("compareStructure");
		String sStructureNotSigned="";




		try{


			String sLine="";
			String sFirst="";
			String sLast="";

			internals=new String[reta.size()];
			pns=new String[reta.size()];
			sns=new String[reta.size()];

			for (int i=0;i<reta.size();i++) {
				SIGAGrDDBBObject obj=(SIGAGrDDBBObject)reta.elementAt(i);

				internals[i]=obj.getString("INTERNAL");
				pns[i]=obj.getString("ITEM_PN");
				sns[i]=obj.getString("ITEM_SN");

				sLine=SIGAConforCertificate.getSlineStructure(obj);

				int firstIndex=sCertificatePKI.indexOf(sLine);
				int lastIndex=firstIndex+sLine.length();


				if(lastIndex>0 && firstIndex>0){
					sFirst=sCertificatePKI.substring(0,firstIndex);
					sLast=sCertificatePKI.substring(lastIndex,sCertificatePKI.length());
					sCertificatePKI=sFirst+sLast;
				}else{

					sStructureNotSigned+=sLine;
	//				System.out.println("no esta firmado y si en DB");
				//	System.out.println(sLine);
				}

			}
			if (sCertificatePKI.indexOf(SIGAPkiDataProducer.INIT_VALUE)>0){
	//				System.out.println("datos firmados que no estan en base de datos");
					bDiferences=true;
					String sdiv="</div>";
					int indeDiv=sCertificatePKI.indexOf(sdiv);
					if (indeDiv>0)sCertificatePKI=sCertificatePKI.substring((indeDiv+sdiv.length()),sCertificatePKI.length());


					sReult+="<tr><td class=\"Title\" colspan=\"2\"> Structure signed data aren´t into DB</td></tr>";
					sReult+="<tr><td colspan=\"2\">";
					sReult+=sCertificatePKI;
					sReult+="</td></tr>";
//					System.out.println(sCertificatePKI);
			}else{
//				System.out.println("lo firmado y las datos coinciden");
			}

			if(!sStructureNotSigned.equals("")){
				bDiferences=true;
				sReult+="<tr><td class=\"Title\" colspan=\"2\"> Structure data without sign that are into DB</td></tr>";
					sReult+="<tr><td colspan=\"2\">";
				sReult+=SIGAConforCertificate.sBeginStructure();
				sReult+=sStructureNotSigned;
				sReult+=SIGAConforCertificate.TABLE_END;
				sReult+=SIGAConforCertificate.END_STRUCTURE;
									sReult+="</td></tr>";
			}

/*

		}catch(ClsExceptions e){
			e.printStackTrace();
					ClsLogging.writeFileLogError(e.getMsg(),3);
			throw e;
*/
		}catch(Exception ex){
		    throw new ClsExceptions(ex,"problems in compareCertificate.");
		}


	}


	private static void compareModifications(String sCertificatePKI, HttpServletRequest req)throws ClsExceptions{
//		//PSSCUsrBusinessBean usrBusiness = null;
		HttpSession ses = null;

//		System.out.println("+++++++++++");
//		System.out.println("compareModifications");
		String sDataNotSigned="";

		try{

	String sLine="";
	String sFirst="";
	String sLast="";


	for (int i=0;i<internals.length;i++) {
	 Vector ret=SIGAConforCertificate.getVMOD(internals[i], gtTable);
	  if (ret!=null && !ret.isEmpty())  {

		  String mods = "";
		  for (int v = 0; v < ret.size(); v++) {
			  SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
			  if (v != 0) mods += ", ";
			  mods += obj.getString("MOD_NUMBER");
		  }


		sLine = SIGAConforCertificate.getSlinePnSN(pns[i], sns[i]);
		int firstIndex=sCertificatePKI.indexOf(sLine);
		int lastIndex=firstIndex+sLine.length();



		String sModInDB="";
		if(lastIndex>0 && firstIndex>0){
			sFirst=sCertificatePKI.substring(0,firstIndex);
			sLast=sCertificatePKI.substring(lastIndex,sCertificatePKI.length());

			String sTr="</tr>";
			int iNextTr=sLast.indexOf(sTr);
			int iValueModsEnd=sLast.indexOf(SIGAConforCertificate.END_VALUE);
			int iValueModsInit=sLast.indexOf(SIGAConforCertificate.INIT_VALUE)+SIGAConforCertificate.INIT_VALUE.length();


			if (iValueModsEnd<iNextTr) sModInDB=sLast.substring(iValueModsInit, iValueModsEnd);


//			System.out.println("mods en DB");
//			System.out.println(sModInDB);

			sLast=sLast.substring((iNextTr+sTr.length()), sLast.length());
			sCertificatePKI=sFirst+sLast;

			if (!mods.equals(sModInDB)){
				sDataNotSigned+= sLine;
				sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  sModInDB +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
				sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  mods + "&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
			} //else System.out.println("lo firmado y datos coinciden");

		}else{
			sDataNotSigned+= sLine;
			sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  sModInDB +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
			sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  mods +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
	//		System.out.println("mods  esta firmado y si en DB");
	//		System.out.println(sLine);
		}


	  }

	}

//probar lo que queda

		if(sCertificatePKI.indexOf(SIGAConforCertificate.INIT_VALUE)>0){
			bDiferences=true;
			sDataNotSigned+=getLinesSignNotDB(sCertificatePKI);


		}



			if ((sCertificatePKI.indexOf(SIGAPkiDataProducer.INIT_VALUE)>0) || (!sDataNotSigned.equals(""))){
				bDiferences=true;
				sReult+="<tr><td class=\"Title\" colspan=\"2\"> Modifications </td></tr>";
				sReult+="<tr><td colspan=\"2\">";
				sReult+=SIGAConforCertificate.sBeginMods(true);
				sReult+=sDataNotSigned;
				sReult+=SIGAConforCertificate.TABLE_END;
				sReult+=SIGAConforCertificate.END_MOD;
				sReult+="</td></tr>";




			}




/*

		}catch(ClsExceptions e){
			e.printStackTrace();
					ClsLogging.writeFileLogError(e.getMsg(),3);
			throw e;
*/
		}catch(Exception ex){
		    throw new ClsExceptions(ex,"problems in compareCertificate.");
		}


	}

	private static void compareTO (String sCertificatePKI, HttpServletRequest req)throws ClsExceptions{
//		//PSSCUsrBusinessBean usrBusiness = null;
		HttpSession ses = null;

//	System.out.println("+++++++++++");
	//System.out.println("compareTO");

	try{

		String sLine="";
		String sFirst="";
		String sLast="";
		String sDataNotSigned="";

		for (int i=0;i<internals.length;i++) {
		 Vector ret=SIGAConforCertificate.getVTO(internals[i], gtTable);
		  if (ret!=null && !ret.isEmpty())  {
			String mods = "";
			for (int v = 0; v < ret.size(); v++) {
				SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
				if (v != 0) mods += ", ";
				mods += obj.getString("EJPSI");
			}



		sLine = SIGAConforCertificate.getSlinePnSN(pns[i], sns[i]);
		int firstIndex=sCertificatePKI.indexOf(sLine);
		int lastIndex=firstIndex+sLine.length();



		String sToInDB="";
		if(lastIndex>0 && firstIndex>0){
			sFirst=sCertificatePKI.substring(0,firstIndex);
			sLast=sCertificatePKI.substring(lastIndex,sCertificatePKI.length());

			String sTr="</tr>";
			int iNextTr=sLast.indexOf(sTr);
			int iValueModsEnd=sLast.indexOf(SIGAConforCertificate.END_VALUE);
			int iValueModsInit=sLast.indexOf(SIGAConforCertificate.INIT_VALUE)+SIGAConforCertificate.INIT_VALUE.length();


			if (iValueModsEnd<iNextTr) sToInDB=sLast.substring(iValueModsInit, iValueModsEnd);


	//		System.out.println("To en DB");
	//		System.out.println(sToInDB);

			sLast=sLast.substring((iNextTr+sTr.length()), sLast.length());
			sCertificatePKI=sFirst+sLast;

			if (!mods.equals(sToInDB)){

				sDataNotSigned+= sLine;
				sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  sToInDB +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
				sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  mods +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
			}//else System.out.println("lo firmado y datos coinciden");

		}else{
			sDataNotSigned+= sLine;
			sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  sToInDB +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
			sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  mods +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
	//		System.out.println("TOs  esta firmado y si en DB");
	//		System.out.println(sLine);
		}


	  }

	}

//probar lo que queda

		if(sCertificatePKI.indexOf(SIGAConforCertificate.INIT_VALUE)>0){
			bDiferences=true;
			sDataNotSigned+=getLinesSignNotDB(sCertificatePKI);


		}




			if ((sCertificatePKI.indexOf(SIGAPkiDataProducer.INIT_VALUE)>0) || (!sDataNotSigned.equals(""))){
				bDiferences=true;
				sReult+="<tr><td class=\"Title\" colspan=\"2\"> Tecnical Orders </td></tr>";
				sReult+="<tr><td colspan=\"2\">";
				sReult+=SIGAConforCertificate.sBeginTO(true);
				sReult+=sDataNotSigned;
				sReult+=SIGAConforCertificate.TABLE_END;
				sReult+=SIGAConforCertificate.END_TO;
				sReult+="</td></tr>";

			}




	}catch(Exception ex){
		throw new ClsExceptions(ex,"problems in compareCertificate.");
	}

	}

	private static void compareConcessions (String sCertificatePKI, HttpServletRequest req)throws ClsExceptions{
//		//PSSCUsrBusinessBean usrBusiness = null;
		HttpSession ses = null;

//	System.out.println("+++++++++++");
//	System.out.println("compareConcessions");
	String sDataNotSigned="";

	try{

		String sLine="";
		String sFirst="";
		String sLast="";


		for (int i=0;i<internals.length;i++) {
		Vector ret=SIGAConforCertificate.getVconcession(internals[i], gtTable);
		  if (ret!=null && !ret.isEmpty())  {
			String mods = "";
			for (int v = 0; v < ret.size(); v++) {
				SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(v);
				if (v != 0) mods += ", ";
				mods += obj.getString("CONCESS_NUMBER");
			}


			sLine = SIGAConforCertificate.getSlinePnSN(pns[i], sns[i]);
			int firstIndex=sCertificatePKI.indexOf(sLine);
			int lastIndex=firstIndex+sLine.length();



			String sConInDB="";
			if(lastIndex>0 && firstIndex>0){
				sFirst=sCertificatePKI.substring(0,firstIndex);
				sLast=sCertificatePKI.substring(lastIndex,sCertificatePKI.length());

				String sTr="</tr>";
				int iNextTr=sLast.indexOf(sTr);
				int iValueModsEnd=sLast.indexOf(SIGAConforCertificate.END_VALUE);
				int iValueModsInit=sLast.indexOf(SIGAConforCertificate.INIT_VALUE)+SIGAConforCertificate.INIT_VALUE.length();


				if (iValueModsEnd<iNextTr) sConInDB=sLast.substring(iValueModsInit, iValueModsEnd);


		//		System.out.println("Concessions en DB");
		//		System.out.println(sConInDB);

				sLast=sLast.substring((iNextTr+sTr.length()), sLast.length());
				sCertificatePKI=sFirst+sLast;

				if (!mods.equals(sConInDB)){

					sDataNotSigned+= sLine;
					sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  sConInDB +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
					sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  mods +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
				}//else System.out.println("lo firmado y datos coinciden");

			}else{
				sDataNotSigned+= sLine;
				sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  sConInDB +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
				sDataNotSigned+=SIGAConforCertificate.STRUCTURE_LINE_INIT +  mods +"&nbsp;"+ SIGAConforCertificate.STRUCTURE_LINE_END;
	//			System.out.println("Concessions  esta firmado y si en DB");
		//		System.out.println(sLine);
			}



		  }



		}

//probar lo que queda

		if(sCertificatePKI.indexOf(SIGAConforCertificate.INIT_VALUE)>0){
			bDiferences=true;
			sDataNotSigned+=getLinesSignNotDB(sCertificatePKI);


		}



				if ((sCertificatePKI.indexOf(SIGAPkiDataProducer.INIT_VALUE)>0) || (!sDataNotSigned.equals(""))){
					bDiferences=true;
					sReult+="<tr><td class=\"Title\" colspan=\"2\"> Concessions </td></tr>";
					sReult+="<tr><td colspan=\"2\">";
					sReult+=SIGAConforCertificate.sBeginConcess(true);
					sReult+=sDataNotSigned;
					sReult+=SIGAConforCertificate.TABLE_END;
					sReult+=SIGAConforCertificate.END_CONCESSION;
					sReult+="</td></tr>";




			}



	}catch(Exception ex){
		throw new ClsExceptions(ex,"problems in compareCertificate.");
	}

	}

	private static void comparePot (String sCertificatePKI, Vector ret, HttpServletRequest req)throws ClsExceptions{
		//PSSCUsrBusinessBean usrBusiness = null;
		HttpSession ses = null;

//		System.out.println("+++++++++++");
//		System.out.println("comparePot");

		String sLine="";
		String sFirst="";
		String sLast="";
		String sDataNotSigned="";

		try{

			if (ret!=null && !ret.isEmpty())  {
				SIGAGrDDBBObject obj = (SIGAGrDDBBObject) ret.elementAt(0);
				Hashtable has = new Hashtable();
				for (int y=0;y<SIGAConforCertificate.potNames.length;y++) {
					has.put(SIGAConforCertificate.potNames[y],obj.getString(SIGAConforCertificate.potFields[y]));
				}
				SIGASignCreatePot pot=new SIGASignCreatePot();
				pot.setParameters(has);
				pot.performAction();
				sLine+=pot.getLiteral();
			}

			int firstIndex=sCertificatePKI.indexOf(sLine);

			if(firstIndex>0){
						//		System.out.println("lo firmado y las datos coinciden");
			}else{
			//	System.out.println("lo firmado y las datos NOOOOOO coinciden");
			//	System.out.println(sLine);

		//		sReult+="<tr><td class=\"Title\" colspan=\"2\">  </td></tr>";
				bDiferences=true;
				sReult+="<tr><td colspan=\"2\">";
				sReult+=sLine;
				sReult+="</td></tr>";


			}





		}catch(Exception ex){
			throw new ClsExceptions(ex,"problems in compareCertificate.");
		}


	}

private static String getLinesSignNotDB(String sCertificatePKI){

//	System.out.println("+++++");
//	System.out.println(sCertificatePKI);
	String sResultSignNotDB="";
	while(sCertificatePKI.indexOf(SIGAConforCertificate.INIT_VALUE)>0){

		String  sInitValue="<tr><td class=\"nonEdit\">";
		String sEndValue="<!--KeyValueE--></td></tr>";
		int iValueInit=sCertificatePKI.indexOf(sInitValue);
		int iValueEnd=sCertificatePKI.indexOf(sEndValue)+sEndValue.length();
		String sLine=sCertificatePKI.substring(iValueInit,sCertificatePKI.indexOf(sEndValue));
		sCertificatePKI=sCertificatePKI.substring(0,iValueInit) +sCertificatePKI.substring(iValueEnd,sCertificatePKI.length());
		sResultSignNotDB+=sLine;
		sResultSignNotDB+="<!--KeyValueE--></td>";
		sResultSignNotDB+="<td>&nbsp;</td></tr>";

	}
	return sResultSignNotDB;
	}
}