//Source file: C:\\TEMP\\GENERA\\TechnicalDesign\\pssc\\pki\\PSSCPkiSaver.java

package com.siga.pki;


/**
 * This class implements all method from SIGAPkiInterface.
 *
 * For each one function this class will implement the following:
 *
 * 0.- This function will be called as:
 *
 *    PSSCPkiSaver.function( identifiers[] )
 *
 * 1.- Get string from:
 *    sSignature = PSSCPki.function(identifiers [] ) ;
 *
 * 2.- The user of the system must be catched.
 *
 * 3.- With the string got in step 1, the system will call entrust software to get
 * hash for this string:
 *
 *     sHash = entrust.getHash( sSignature ) ;
 *
 * 4.- This hash must be encrited using private key.
 *
 *     sHashEnripted = entrust.encryptHash( sHash );
 *
 * 5.- This signature must be done persistent.
 *
 * 6.- Return Value:
 * a empty string if all process run correctly.
 * a string indicating motive of failure.
 */
public class SIGAPkiSaver implements SIGAPkiInterface
{

   /**
    * @roseuid 3DAFDDFD0178
    */
   public SIGAPkiSaver()
   {

   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFD0188
    */
   public String signMovement(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFD01D6
    */
   public String signInstallationCertificate(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFD0224
    */
   public String signConformanceCertificate(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFD0263
    */
   public String signConformanceCertificateGD(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFD02C1
    */
   public String signConformanceCertificatePOT(String[] sIdentifier)
   {
    return null;
   }
}
