package bda;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailConnection {
	
	private String username;
	private String password;
	
	public EmailConnection(String username, String password) { // Constructor
		this.username = username;
		this.password = password;
	} // Fim do Constructor
	
	private String getTextFromMessage(Message message) throws MessagingException, IOException {
	    String result = "";
	    if (message.isMimeType("text/plain")) {
	        result = message.getContent().toString();
	    } else if (message.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException {
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
	
	public void receiveMail() {
		List<InformationEntry> information_entry_list = new ArrayList<InformationEntry>();
		try {
			Properties properties = new Properties();
			properties.setProperty("mail.store.protocol", "imaps");
			Session emailSession = Session.getDefaultInstance(properties);
			Store emailStore = emailSession.getStore("imaps");
			emailStore.connect("outlook.office365.com", username, password); // outlook.office365.com // imap.gmail.com
			// getting the inbox folder
			Folder emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			Message messages[] = emailFolder.getMessages();
			//System.out.println(emailFolder.getMessageCount());
			
			Date date;
			Service service;
			String writerName;
			String subject;
			String content;
			
			for(int i = messages.length - 1 ; i > messages.length - 4 ; i--) {
				date = messages[i].getSentDate();
				service = Service.EMAIL;
				writerName = messages[i].getFrom()[0].toString();
				subject = messages[i].getSubject();
				content = getTextFromMessage(messages[i]);
				
				information_entry_list.add(new InformationEntry(date, service, writerName, subject, content));
			}
			
			try {
				ReadAndWriteXMLFile.CreateInformationEntryXMLFile(information_entry_list); // require work, it has errors!
				System.out.println("Created the XML file for the Information Entrys.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// closing emailFolder and emailStore
			emailFolder.close(false);
			emailStore.close();
		} catch(NoSuchProviderException nspe) {
			nspe.printStackTrace();
		} catch(MessagingException me) {
			me.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			if(information_entry_list.isEmpty()) {
				information_entry_list = ReadAndWriteXMLFile.ReadInformationEntryXMLFile();
				System.out.println("Loaded the Information Entrys from the XML.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0 ; i < information_entry_list.size() ; i++) { // should return this array instead for it to be displayed on the UI
			System.out.println("Email Number " + (i+1) + ".");
			System.out.println("From: " + information_entry_list.get(i).getWriterName());
			System.out.println("Sent date: " + information_entry_list.get(i).getDate());
			System.out.println("Subject: " + information_entry_list.get(i).getSubject());
			System.out.println("Message: " + information_entry_list.get(i).getContent());
		}
		
	}
	
	public void sendEmail(String sendEmailTo, String subject, String message) {
		try {
			String host = "smtp.office365.com"; // smtp.gmail.com // smtp-mail-outlook.com // smtp.office365.com // mail.protection.outlook.com // m.outlook.com
			boolean sessionDebug = false;
			
			Properties props = System.getProperties();
			
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587"); // 587 // 465 // 25
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");
			
			java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			
			Session mailSession = Session.getDefaultInstance(props, null);
			mailSession.setDebug(sessionDebug);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(username));
			InternetAddress[] address = {new InternetAddress(sendEmailTo)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(message);
			
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			System.out.println("Message sent successfully");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
