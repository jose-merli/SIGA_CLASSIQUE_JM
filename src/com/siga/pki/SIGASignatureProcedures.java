//Source file: C:\\TEMP\\GENERA\\TechnicalDesign\\pssc\\pki\\SIGASignatureProcedures.java

package com.siga.pki;


public class SIGASignatureProcedures
{
   private String sIdProc;
   private String sDescription;
   private SIGACertificateType type;

   /**
    * @roseuid 3DAFDDFC01A7
    */
   public SIGASignatureProcedures()
   {

   }

   /**
    * Access method for the sIdProc property.
    *
    * @return   the current value of the sIdProc property
    */
   public String getSIdProc()
   {
      return sIdProc;
   }

   /**
    * Sets the value of the sIdProc property.
    *
    * @param aSIdProc the new value of the sIdProc property
    */
   public void setSIdProc(String aSIdProc)
   {
      sIdProc = aSIdProc;
   }

   /**
    * Access method for the sDescription property.
    *
    * @return   the current value of the sDescription property
    */
   public String getSDescription()
   {
      return sDescription;
   }

   /**
    * Sets the value of the sDescription property.
    *
    * @param aSDescription the new value of the sDescription property
    */
   public void setSDescription(String aSDescription)
   {
      sDescription = aSDescription;
   }

   /**
    * Access method for the type property.
    *
    * @return   the current value of the type property
    */
   public SIGACertificateType getType()
   {
      return type;
   }

   /**
    * Sets the value of the type property.
    *
    * @param aType the new value of the type property
    */
   public void setType(SIGACertificateType aType)
   {
      type = aType;
   }
}
