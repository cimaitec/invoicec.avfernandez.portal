
package com.general.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.sun.mail.util.MailSSLSocketFactory;

public class EmailSender {
	private String from = null;
	private String host = null;
	private Logger log;
	private String user = "vpincay";
	private String password = "vpincay2";
	private String subject = null;
	private String autentificacion = "NONE";
	private String tipoMail	= null;
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	//private BitacoraScp scp;
	//private Connection axis;
	private int firstmail;
	public EmailSender (String host, String from) {
		this.host = host;
		this.from = from;
	}
	
	public EmailSender (Logger log) {
		this.log=log;
	}
	

	public static void main(String[] args) {
		EmailSender eSender = new EmailSender("mail.cimait.com.ec", "vpincay@cimait.com.ec");
		eSender.send("v.pincay@hotmail.com","Subject del msj" ,"Contenido del msj" , "C:/Users/JohnnyZurita/AppData/Local/Temp/1099270137400101002002012262439.xml", "C:/Users/JohnnyZurita/AppData/Local/Temp/1099270137400101002002012262439 - Copy.xml");
	}
	public String send (String to, String subject, String message, String fileAttachXml, String fileAttachPdf) {
		String result = "Enviado";
		Session session=null;
		System.out.println();
		System.out.println("mail.smtp.host::"+(host==null?"NULL":host));
		System.out.println("mail.stmp.user::"+(user==null?"NULL":user));
		System.out.println("mail.smtp.password::"+(password==null?"NULL":password));
		
		try {
			Properties prop = System.getProperties();
			//autentificacion ="SSL";
			if (autentificacion.equals("NONE")){
				prop.put("mail.smtp.host",host);
				prop.put("mail.smtp.socketFactory.port", "25");
				prop.put("mail.smtp.auth", "no");
				session = Session.getDefaultInstance(prop,null);
			}
			if (autentificacion.equals("NORMAL")){
				prop.put("mail.smtp.host",host);
				prop.put("mail.smtp.socketFactory.port", "25");
				prop.put("mail.smtp.auth", "true");
				prop.put("mail.stmp.user" , user);
				prop.put("mail.smtp.password", password);
				//prop.put("mail.smtp.starttls.enable", "true");
				//To use SSL
		        //props.put("mail.smtp.socketFactory.port", "465");
		        //prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		        prop.put("mail.smtp.port", "25");
		        //props.put("mail.smtp.auth", "true");
		        //props.put("mail.smtp.port", "465");
		        session = Session.getInstance(prop,
						  new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(user, password);}});
			}
			if (autentificacion.equals("TLS")){
				prop.put("mail.smtp.host",host);
				prop.put("mail.smtp.socketFactory.port", "25");
				prop.put("mail.smtp.auth", "true");
				prop.put("mail.smtp.starttls.enable", "true");
				prop.put("mail.stmp.user" , user);
				prop.put("mail.smtp.password", password);
				
				//To use SSL
		        //props.put("mail.smtp.socketFactory.port", "465");
		        //prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		        prop.put("mail.smtp.port", "25");
		        //props.put("mail.smtp.auth", "true");
		        //props.put("mail.smtp.port", "465");
		        
		        session = Session.getInstance(prop,
						  new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(user, password);}});
				//session = Session.getDefaultInstance(prop,null);
			}
			if (autentificacion.equals("SSL")){				
				MailSSLSocketFactory sf = null;
		        try {
		            sf = new MailSSLSocketFactory();
		        } catch (Exception e1) {
		            // TODO Auto-generated catch block
		            e1.printStackTrace();
		            throw new Exception("Error en SSL Mail Socket Factory "+e1.getMessage());
		        }		        
		        /*
				MailSSLSocketFactoryMailing sf = null;
		        try {
		            sf = new MailSSLSocketFactoryMailing();
		        } catch (Exception e1) {
		            // TODO Auto-generated catch block
		            e1.printStackTrace();
		        }
		        */
		        sf.setTrustAllHosts(true);
		        prop.put("mail.smtp.ssl.socketFactory", sf);
				
				prop.put("mail.smtp.host",host);
				prop.put("mail.smtp.socketFactory.port", "25");
				prop.put("mail.smtp.auth", "true");
				prop.put("mail.stmp.user" , user);
				prop.put("mail.smtp.password", password);
				prop.put("mail.smtp.starttls.enable", "true");
				//To use SSL
		        prop.put("mail.smtp.socketFactory.port", "465");
		        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		        prop.put("mail.smtp.port", "25");
		        //props.put("mail.smtp.auth", "true");
		        //props.put("mail.smtp.port", "465");
		        session = Session.getInstance(prop,
						  new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(user, password);}});
			}
			
			/*
			  props.put("mail.stmp.user" , "username");

		        //To use TLS
		        props.put("mail.smtp.auth", "true"); 
		        props.put("mail.smtp.starttls.enable", "true");
		        props.put("mail.smtp.password", "password");
		        //To use SSL
		        props.put("mail.smtp.socketFactory.port", "465");
		        props.put("mail.smtp.socketFactory.class", 
		            "javax.net.ssl.SSLSocketFactory");
		        props.put("mail.smtp.auth", "true");
		        props.put("mail.smtp.port", "465");
			 */
			
			
			//Session session = Session.getDefaultInstance(prop,null);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			String[] receivers = to.split(";");
			for (int i=0; i < receivers.length; i++){
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receivers[i]));
			}
			msg.setSubject(subject);
			BodyPart messageBodyPart = new MimeBodyPart();
			BodyPart messageBodyPartFile = new MimeBodyPart();
			BodyPart messageBodyPartFilePdf = new MimeBodyPart();
			if (tipoMail.equals("TEXT"))
			messageBodyPart.setText(message);
			if (tipoMail.equals("HTML"))
				messageBodyPart.setContent(message, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			File fVerifica = new File(fileAttachXml);
			if (fVerifica.exists())
			{
				if ((fileAttachXml!=null)&&(!fileAttachXml.equals("")))
				{
					DataSource source =	new FileDataSource(fileAttachXml);
					messageBodyPartFile.setDataHandler(new DataHandler(source));
					messageBodyPartFile.setFileName(fileAttachXml);
					multipart.addBodyPart(messageBodyPartFile);
				}
			}
			fVerifica = new File(fileAttachPdf);
			if (fVerifica.exists())
			{
				if ((fileAttachPdf!=null)&&(!fileAttachPdf.equals("")))
				{
					DataSource source =	new FileDataSource(fileAttachPdf);
					messageBodyPartFilePdf.setDataHandler(new DataHandler(source));
					messageBodyPartFilePdf.setFileName(fileAttachPdf);
					multipart.addBodyPart(messageBodyPartFilePdf);
				}
			}
			msg.setContent(multipart);
			//Transport.send(msg, msg.getAllRecipients());
			Transport.send(msg);
			
		} catch (javax.mail.MessagingException me) {
			me.printStackTrace();
			
			result = me.toString();
		}catch (Exception e) {
			e.printStackTrace();
			
			result = e.toString();
		}
		
		return result;
	}
	
	public String send (String to, String subject, String message) {
		String result = "Enviado";
		try {
			Properties prop = System.getProperties();
			prop.put("mail.smtp.host",host);
			Session sesion = Session.getDefaultInstance(prop,null);
			MimeMessage msg = new MimeMessage(sesion);
			msg.setFrom(new InternetAddress(from));
			String[] receivers = to.split(";");
			for (int i=0; i < receivers.length; i++){
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receivers[i]));
			}
			msg.setSubject(subject);
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(message);			
			Transport.send(msg);
			
		} catch (javax.mail.MessagingException me) {
			result = me.toString();
		}
		return result;
	}
	

	public int getFirstmail() {
		return firstmail;
	}

	public void setFirstmail(int firstmail) {
		this.firstmail = firstmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAutentificacion() {
		return autentificacion;
	}

	public void setAutentificacion(String autentificacion) {
		this.autentificacion = autentificacion;
	}

	public String getTipoMail() {
		return tipoMail;
	}

	public void setTipoMail(String tipoMail) {
		this.tipoMail = tipoMail;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}	
	
	
}

