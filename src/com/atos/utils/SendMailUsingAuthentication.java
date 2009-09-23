package com.atos.utils;

/*
Some SMTP servers require a username and password authentication before you
can use their Server for Sending mail. This is most common with couple
of ISP's who provide SMTP Address to Send Mail.

This Program gives any example on how to do SMTP Authentication
(User and Password verification)

*/

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.*;
import javax.mail.Multipart;
/*
  To use this program, change values for the following three constants,

	SMTP_HOST_NAME -- Has your SMTP Host Name
	SMTP_AUTH_USER -- Has your SMTP Authentication UserName
	SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

  Next change values for fields

  emailMsgTxt  -- Message Text for the Email
  emailSubjectTxt  -- Subject for email
  emailFromAddress -- Email Address whose name will appears as "from" address

  Next change value for "emailList".
  This String array has List of all Email Addresses to Email Email needs to be sent to.


*/

public class SendMailUsingAuthentication
{

  private static final String SMTP_HOST_NAME = "mail.madrid.eur.slb.com";
  private static final String SMTP_AUTH_USER = "xxxx.zzzz";
  private static String SMTP_AUTH_PWD  = "";
  private static String mailAttachments= "";
  private static final String emailMsgTxt      = "Si recibes esto es que todo va bien...";
  private static final String emailSubjectTxt  = "Prueba desde Java";
  private static final String emailFromAddress = "xxxx.zzzz@atosorigin.com";

  // Add List of Email address to who email needs to be sent to
  //private static final String[] emailList = {"xxxx.zzzz@atosorigin.com", "vvvv@ssss.es"};
  private static final String[] emailList = {"xxxx.zzzz@atosorigin.com"};

  public static void main(String args[]) throws Exception
  {
  	
  if (args.length < 2)
		{
			System.err.println("Uso: java SendMailUsingAuthentication password file_attachment");
			System.err.println("Ejemplo: java SendMailUsingAuthentication 03ghf c:\\a.txt");
			System.exit(1);
		}  	
    
	SMTP_AUTH_PWD = args[0];
	mailAttachments = args[1];
    
	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
        
	smtpMailSender.postMail(mailAttachments, emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress);
	//System.out.println("Email enviado correctamete a todos los usuarios!");
  }

  public void postMail( String mailAttachments, String recipients[ ], String subject,
							String message , String from) throws MessagingException
  {
	boolean debug = false;

	 //Set the host smtp address
	 Properties props = new Properties();
	 props.put("mail.smtp.host", SMTP_HOST_NAME);
	 props.put("mail.smtp.auth", "true");

	Authenticator auth = new SMTPAuthenticator();
	Session session = Session.getDefaultInstance(props, auth);

	session.setDebug(debug);

	// create a message
	Message msg = new MimeMessage(session);

	// set the from and to address
	InternetAddress addressFrom = new InternetAddress(from);
	msg.setFrom(addressFrom);

	InternetAddress[] addressTo = new InternetAddress[recipients.length];
	for (int i = 0; i < recipients.length; i++)
	{
		addressTo[i] = new InternetAddress(recipients[i]);
	}
	msg.setRecipients(Message.RecipientType.TO, addressTo);
	

	// Setting the Subject and Content Type
	msg.setSubject(subject);
    
	MimeBodyPart mbp1=new MimeBodyPart();
	mbp1.setText(message);
	Multipart mp = new MimeMultipart();
	mp.addBodyPart(mbp1);
	if(mailAttachments!=null){
		MimeBodyPart mbp2=new MimeBodyPart();
		FileDataSource fds=new FileDataSource(mailAttachments);
		mbp2.setDataHandler(new DataHandler(fds));
		
		// INFO
		//System.out.println("Content type: "+fds.getContentType());
		//System.out.println("Name: "+fds.getName());
		// ...
		
		mbp2.setFileName(fds.getName());
		mp.addBodyPart(mbp2);
		}
	msg.setContent(mp);
	msg.setSentDate(new java.util.Date());
    
	//msg.setContent(message, "text/plain");
    
	Transport.send(msg);
 }


/**
* SimpleAuthenticator is used to do simple authentication
* when the SMTP server requires it.
*/
private class SMTPAuthenticator extends javax.mail.Authenticator
{

	public PasswordAuthentication getPasswordAuthentication()
	{
		String username = SMTP_AUTH_USER;
		String password = SMTP_AUTH_PWD;
		return new PasswordAuthentication(username, password);
	}
}

}






