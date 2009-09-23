//Source file: C:\\TEMP\\GENERA\\TechnicalDesign\\pssc\\pki\\PSSCSignatureContainer.java

package com.siga.pki;

import java.util.Date;

public class SIGASignatureContainer
{

   /**
	* This attribute will contain concatenates primary keys that they identify a
	* signature.
	*/
   private String sIdentifier;

   /**
	* This attribute will contain the hash of the signature.
	*/
   private String sHash;
   private Date dtDate;
   private String sUserId;
   private SIGASignatureProcedures procedure;

   /**
	* @roseuid 3DAFDDFC00BD
	*/
   public SIGASignatureContainer()
   {

   }

   /**
	* Access method for the sIdentifier property.
	*
	* @return   the current value of the sIdentifier property
	*/
   public String getSIdentifier()
   {
	  return sIdentifier;
   }

   /**
	* Sets the value of the sIdentifier property.
	*
	* @param aSIdentifier the new value of the sIdentifier property
	*/
   public void setSIdentifier(String aSIdentifier)
   {
	  sIdentifier = aSIdentifier;
   }

   /**
	* Access method for the sHash property.
	*
	* @return   the current value of the sHash property
	*/
   public String getSHash()
   {
	  return sHash;
   }

   /**
	* Sets the value of the sHash property.
	*
	* @param aSHash the new value of the sHash property
	*/
   public void setSHash(String aSHash)
   {
	  sHash = aSHash;
   }

   /**
	* Access method for the dtDate property.
	*
	* @return   the current value of the dtDate property
	*/
   public Date getDtDate()
   {
	  return dtDate;
   }

   /**
	* Sets the value of the dtDate property.
	*
	* @param aDtDate the new value of the dtDate property
	*/
   public void setDtDate(Date aDtDate)
   {
	  dtDate = aDtDate;
   }

   /**
	* Access method for the sUserId property.
	*
	* @return   the current value of the sUserId property
	*/
   public String getSUserId()
   {
	  return sUserId;
   }

   /**
	* Sets the value of the sUserId property.
	*
	* @param aSUserId the new value of the sUserId property
	*/
   public void setSUserId(String aSUserId)
   {
	  sUserId = aSUserId;
   }

   /**
	* Access method for the procedure property.
	*
	* @return   the current value of the procedure property
	*/
   public SIGASignatureProcedures getProcedure()
   {
	  return procedure;
   }

   /**
	* Sets the value of the procedure property.
	*
	* @param aProcedure the new value of the procedure property
	*/
   public void setProcedure(SIGASignatureProcedures aProcedure)
   {
	  procedure = aProcedure;
   }
}
