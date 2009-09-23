//Source file: C:\\TEMP\\GENERA\\TechnicalDesign\\pssc\\pki\\PSSCPkiChecker.java

package com.siga.pki;


/**
 * This class implements all method from SIGAPkiInterface.
 * For each one function this class will implement the following:
 * 0.- This function will be called as:
 * PSSCPkiChecker.function ( identifiers[] );
 * 1.-  Gets all signatures for this identifiers and type of function.
 * 2.- For each one of signatures checks if they are right.
 * 2.1.- Gets encripted hash from database for this signature.
 * 2.2.- Gets respective encripted hash for this user, ussing public key.
 * 2.3.- Check both encripted hashes.
 * 3.- If they are correct, add user id to the string to return.
 * 4.- Repeat this process for all signatures got in step 1.
 * 5.- Returns string with users id that has correct signature.
 */
public class SIGAPkiChecker implements SIGAPkiInterface
{

   /**
    * @roseuid 3DAFDDFD03CA
    */
   public SIGAPkiChecker()
   {

   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFE0001
    */
   public String signMovement(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFE0050
    */
   public String signInstallationCertificate(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFE009E
    */
   public String signConformanceCertificate(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFE00EC
    */
   public String signConformanceCertificateGD(String[] sIdentifier)
   {
    return null;
   }

   /**
    * @param sIdentifier
    * @return String
    * @roseuid 3DAFDDFE013A
    */
   public String signConformanceCertificatePOT(String[] sIdentifier)
   {
    return null;
   }
}
