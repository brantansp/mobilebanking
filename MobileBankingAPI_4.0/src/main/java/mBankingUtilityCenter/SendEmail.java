package mBankingUtilityCenter;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.mail.smtp.SMTPTransport;

/**
 * 
 * @author brantansp
 *Mail
 */

public class SendEmail extends ExtentManager
{

	protected static Log log = LogFactory.getLog(MethodHandles.lookup().lookupClass().getSimpleName());
	public static Properties prop=getProperty();
	
	public static void main(String[] args) {
		
		//sendEmail( "", "");
		System.out.println(lastFileModified(System.getProperty("user.dir")+"\\output\\logs\\20180326\\"));
	}

	public static void sendEmail(String folder, String file) {

		
        final String smtpAuthUserName = prop.getProperty("smtpAuthUserName");
        final String smtpAuthPassword = prop.getProperty("smtpAuthPassword");
        String emailFrom = prop.getProperty("emailFrom");
        String emailTo   = prop.getProperty("emailTo");
        
        Authenticator authenticator = new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(smtpAuthUserName, smtpAuthPassword);
            }
        };
        Properties properties = new Properties();
        properties.setProperty("UseDefaultCredentials", "false");
        properties.setProperty("mail.smtp.host", prop.getProperty("smtphost"));
        properties.setProperty("mail.smtp.port", prop.getProperty("smtpport"));
        properties.setProperty("mail.smtp.auth", "true");
 
        properties.setProperty("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance( properties, authenticator );
        
        Message msg = new MimeMessage(session);
        
        try
        {
            
        	String [] toMail= emailTo.split(",");
        	
        	for (int i=0; i<toMail.length; i++)
        	{
        		System.out.println("mail are : " +toMail[i]);
        	}
        	
            msg.setFrom(new InternetAddress(emailFrom));
          
            InternetAddress[] mailAddress_TO = new InternetAddress [toMail.length] ;
            for(int i=0;i<toMail.length;i++){
                mailAddress_TO[i] = new InternetAddress(toMail[i]);
            }
            msg.addRecipients(Message.RecipientType.TO, mailAddress_TO);
            msg.setSubject("LoB 7 DB : QCT mPAY 4.0 - Automation test result");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Please find the status for the automation test run");

            MimeBodyPart attachmentBodyPart1= new MimeBodyPart();
            //DataSource source = new FileDataSource(System.getProperty("user.dir")+"\\output\\ExtentReport\\24032018\\ExtentReport_220713.html");
            DataSource source = new FileDataSource(System.getProperty("user.dir")+"\\output\\ExtentReport\\"+folder+"\\ExtentReport_"+file+".html");
            attachmentBodyPart1.setDataHandler(new DataHandler(source));
            attachmentBodyPart1.setFileName("Report.html"); // ex : "test.pdf"
            multipart.addBodyPart(textBodyPart);  // add the text part
            multipart.addBodyPart(attachmentBodyPart1); // add the attachement part
            
            MimeBodyPart attachmentBodyPart2 = new MimeBodyPart(); 
            //DataSource source2 = new FileDataSource(System.getProperty("user.dir")+"\\output\\logs\\20180326\\Output_20180326170500.log");
            DataSource source2 = new FileDataSource(System.getProperty("user.dir")+"\\output\\logs\\"+folder+"\\Output_20180326170500.log"); 
            attachmentBodyPart2.setDataHandler( new DataHandler(source2)); 
            attachmentBodyPart2.setFileName("Logs.txt"); 
            multipart.addBodyPart(attachmentBodyPart2); 
            
            msg.setContent(multipart);
            Transport.send(msg);
            
        } catch (MessagingException e) {
        	e.printStackTrace();
        }
   
		
	}

	public static String lastFileModified(String dir) {
	    File fl = new File(dir);
	    File[] files = fl.listFiles();
	    long lastMod = Long.MIN_VALUE;
	    String choice = null;
	    for (File file : files) {
	        if (file.lastModified() > lastMod) {
	            choice = file.getName();
	            lastMod = file.lastModified();
	        }
	    }
	    return choice;
	}
}

