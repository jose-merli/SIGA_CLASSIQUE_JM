//Source file: C:\\TEMP\\GENERA\\TechnicalDesign\\pssc\\pki\\SIGAPkiInterface.java

package com.siga.pki;


public interface SIGAPkiInterface
{

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFBE46019E
    */
   public String signMovement(String[] sIdentifier);

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFBE800287
    */
   public String signInstallationCertificate(String[] sIdentifier);

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFBEB60247
    */
   public String signConformanceCertificate(String[] sIdentifier);

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFBECD01AA
    */
   public String signConformanceCertificateGD(String[] sIdentifier);

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFBEF00310
    */
   public String signConformanceCertificatePOT(String[] sIdentifier);
}
