package bda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ReadAndWriteFile {
	
	private void createXMLFile(String fileName) { // Faz um novo ficheiro xml dando o nome do mesmo
		PrintWriter newFile;
		try {
			newFile = new PrintWriter(new File(fileName + ".xml")); // Criar um novo ficheiro;
			newFile.close(); // Fechar o ficheiro;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	} // Fim da funcao createXMLFile(String fileName)
	
	private void writeOnXMLFileAsNewFile(String fileName, String content) {
		PrintWriter pw = null;
		
		try { // Precisa do ficheiro feito apriori
			pw = new PrintWriter(new File(fileName + ".xml"));
			pw.print(content);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} // Fim do try
		
	}
	
	public static void writeOnXMLFileAsNewFile(String fileName, XMLUserConfiguration content) {
		PrintWriter pw = null;
		
		try { // Precisa do ficheiro feito apriori
			pw = new PrintWriter(new File(fileName + ".xml"));
			pw.print(content.toString());
			pw.flush();
			pw.close();
			System.out.println("File created.");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} // Fim do try
		
	}
	
	private void writeOnXMLFileAsNewFile(String fileName, InformationEntry content) {
		PrintWriter pw = null;
		
		try { // Precisa do ficheiro feito apriori
			pw = new PrintWriter(new File(fileName + ".xml"));
			pw.print(content.toString());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} // Fim do try
		
	}
	
	public static XMLUserConfiguration readUserXMLFile(String fileName) throws IOException {
		try (Scanner scanner = new Scanner(new File(fileName + ".xml"))) {
			String line = "";
			String[] lineArray = {};
			boolean informationSaved = false;
			String username = null;
			String password = null;
			XMLUserConfiguration user_config = null;
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				lineArray = line.split(": ", 2);
				//if(line.startsWith("Save Information:")) {
				if(lineArray.length == 2 && lineArray[1] != null) {
					switch (lineArray[0]) {
					case "Save Information":
						if(lineArray[1] == "1") {
							informationSaved = true;
						}
						break;
						
					case "Username":
						username = lineArray[1];
						break;
						
					case "Password":
						password = lineArray[1];
						break;

					default:
						break;
					}
				}
				
			}
			if(username == null || password == null) {
				throw new IOException("The username or password isn't defined.");
			}
			user_config = new XMLUserConfiguration(informationSaved, username, password);
			return user_config;
		} catch (FileNotFoundException e) {
			System.out.println(fileName + ".xml not found.");
		}
		return null;
	}
	
	private InformationEntry readInformationEntryXMLFile(String folder, String fileName) { // provavelmente precisa de se mudar para BufferedReader
		try (Scanner scanner = new Scanner(new File(folder + "/" + fileName + ".xml"))) {
			String line = "";
			String[] lineArray = {};
			
			Date date = null;
			Service service = null;
			String writerName = null;
			String subject = null;
			String content = null;
			InformationEntry informationEntry = null;
			
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				lineArray = line.split(": ", 2);
				//if(line.startsWith("Save Information:")) {
				if(lineArray.length == 2 && lineArray[1] != null) {
					switch (lineArray[0]) {
					case "Date":
						long millisecondsOfDate = Long.valueOf(lineArray[1]).longValue();
						date = new Date(millisecondsOfDate);
						break;
						
					case "Service":
						if(lineArray[1] == "Email") {
							service = Service.EMAIL;
						}
						if(lineArray[1] == "Facebook") {
							service = Service.FACEBOOK;
						}
						if(lineArray[1] == "Twitter") {
							service = Service.TWITTER;
						}
						break;
						
					case "WriterName":
						writerName = lineArray[1];
						break;
						
					case "Subject":
						subject = lineArray[1];
						break;
						
					case "Content":
						content = lineArray[1];
						while(scanner.hasNextLine()) { // ideia
							content += scanner.nextLine();
						}
						break;

					default:
						break;
					}
				}
				
			}
			informationEntry = new InformationEntry(date, service, writerName, subject, content);
			return informationEntry;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<InformationEntry> getInformationEntryArrayFromFolder(String folderName) throws IOException {
		List<String> strlist = new ArrayList<String>();
		List<InformationEntry> informationEntryArray = new ArrayList<InformationEntry>();
		System.out.println("Pasta: " + folderName);
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("./" + folderName);
		
		if(is == null)
			throw new IOException("Directoria invalida ou Directoria vazia");
		
		InputStreamReader in = new InputStreamReader(is);
		try(BufferedReader brdir = new BufferedReader(in);) {
			String fileStr = "";
			while((fileStr = brdir.readLine()) != null) {
				if(fileStr.endsWith(".xml")) {
					strlist.add(fileStr);
				}
			}
			in.close();
			is.close();
		}
		for(String fileS : strlist) {
			InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream(folderName + "/" + fileS);
			try {
				InputStreamReader fileInput = new InputStreamReader(fileStream, "UTF-8");
				
				String line = "";
				String[] lineArray = {};
				
				Date date = null;
				Service service = null;
				String writerName = null;
				String subject = null;
				StringBuilder content = new StringBuilder();
				
				try(BufferedReader brFile = new BufferedReader(fileInput)) {
					while(((line = brFile.readLine()) != null)) {
						lineArray = line.split(": ", 2);
						//if(line.startsWith("Save Information:")) {
						if(lineArray.length == 2 && lineArray[1] != null) {
							switch (lineArray[0]) {
							case "Date":
								long millisecondsOfDate = Long.valueOf(lineArray[1]).longValue();
								date = new Date(millisecondsOfDate);
								break;
								
							case "Service":
								if(lineArray[1] == "Email") {
									service = Service.EMAIL;
								}
								if(lineArray[1] == "Facebook") {
									service = Service.FACEBOOK;
								}
								if(lineArray[1] == "Twitter") {
									service = Service.TWITTER;
								}
								break;
								
							case "WriterName":
								writerName = lineArray[1];
								break;
								
							case "Subject":
								subject = lineArray[1];
								break;
								
							case "Content":
								content.append(lineArray[1] + System.getProperty("line.separator"));
								while(((line = brFile.readLine()) != null)) { // ideia
									content.append(line + System.getProperty("line.separator"));
								}
								break;

							default:
								break;
							}
						}
						
					}
					informationEntryArray.add(new InformationEntry(date, service, writerName, subject, content.toString()));
					fileInput.close();
					fileStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return informationEntryArray;
	}
	
	/*
	// Requires work on to make it read all the files we will generate to display on the UI
	private void loadNewsToList() throws IOException {
		List<String> strlist = new ArrayList<String>();
		System.out.println("Pasta: " + folderName);
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("./" + folderName);
		
		if(is == null)
			throw new IOException("Directoria invalida");
		
		InputStreamReader in = new InputStreamReader(is);
		try(BufferedReader brdir = new BufferedReader(in);) {
		String fileStr = "";
		while((fileStr = brdir.readLine()) != null) {
			if(fileStr.endsWith(".txt")) {
				strlist.add(fileStr);
			}
		}
		in.close();
		is.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String titulo = "";
		for(String fileS : strlist) {
			InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream(folderName + "/" + fileS);
			try {
				InputStreamReader fileInput = new InputStreamReader(fileStream, "UTF-8");
				StringBuilder contentInFile = new StringBuilder();
				try(BufferedReader brFile = new BufferedReader(fileInput)) {
					String fileContent = brFile.readLine();
					if(fileContent != null) {
						titulo = fileContent;
					}
					while(((fileContent = brFile.readLine()) != null)) {
						contentInFile.append(fileContent + "\n");
					}
					noticias.add(new Noticia(titulo, contentInFile.toString()));
					fileInput.close();
					fileStream.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	*/
}
