//Source file: C:\\TEMP\\GENERA\\TechnicalDesign\\pssc\\pki\\PSSCPki.java

package com.siga.pki;


/**
 * Author: Jesús Gutiérrez.
 *
 * This class will contain all the function that obtains strings to be signed.
 *
 * As inputs of each one functions will be identifiers of signature as an array of
 * strings.
 *
 * All methods are statics, so they can be called as:
 *
 * sValueToSign =
 *        PSSCPki.function( {"identifer1", ...,  "identifier n"} );
 */
public class SIGAPki implements SIGAPkiInterface
{

   /**
    * @roseuid 3DAFDDFC0272
    */
   public SIGAPki()
   {

   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFC0292
    */
   public String signMovement(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFC02E0
    */
   public String signInstallationCertificate(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFC031E
    */
   public String signConformanceCertificate(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFC036C
    */
   public String signConformanceCertificateGD(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFC03BB
    */
   public String signConformanceCertificatePOT(String[] sIdentifier)
   {
    return null;
   }
}
