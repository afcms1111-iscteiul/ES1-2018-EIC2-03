package bda;

import java.util.Date;

public class InformationEntry {
	
	private Date date;
	private Service service;
	private String writerName;
	private String subject;
	private String content;
	
	public InformationEntry(Date date, Service service, String writerName, String subject, String content) {
		this.date = date;
		this.service = service;
		this.writerName = writerName;
		this.subject = subject;
		this.content = content;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Service getService() {
		return service;
	}
	
	public String getWriterName() {
		return writerName;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return "Date: " + getDate().getTime() + "" + System.getProperty("line.separator") 
		+ "Service: " + getService().stringFormat() + System.getProperty("line.separator")
		+ "WriterName: " + getWriterName() + System.getProperty("line.separator")
		+ "Subject: " + getSubject() + System.getProperty("line.separator")
		+ "Content: " + getContent() + System.getProperty("line.separator");
	}
	
}
