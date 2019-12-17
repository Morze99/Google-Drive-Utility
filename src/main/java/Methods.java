/*import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;*/
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.http.FileContent;
/*import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.store.FileDataStoreFactory;*/
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Drives;
import com.google.api.services.drive.model.DriveList;
//import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
//import com.google.api.client.googleapis.media.MediaHttpDownloader;
import java.io.FileNotFoundException;
//import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.io.FileOutputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Scanner;
//import java.awt.EventQueue;
//import javax.swing.JFrame;
//import java.awt.Toolkit;
import javax.swing.JTextField;
//import javax.swing.JLabel;
//import javax.swing.JList;
//import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JProgressBar;
/*import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import javax.swing.event.ChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;*/
import javax.swing.JFileChooser;

public class Methods
{
	
	public static void listDrives(java.awt.List list, Drive service, ArrayList<com.google.api.services.drive.model.Drive> drivesArray) throws IOException
	{
		DriveList listDrives=service.drives().list().execute();
	      for (com.google.api.services.drive.model.Drive drive : listDrives.getDrives())
	      {
	        list.add(drive.getName());
	        drivesArray.add(drive);
	      }
	}
	
	public static void searchAllDrives(ArrayList<com.google.api.services.drive.model.Drive> drivesArray, 
									   Drive service, 
									   JTextField searchBox, 
									   java.awt.List list, 
									   ArrayList<File> idList) throws IOException
	{
		list.removeAll();
		idList.clear();
		DriveList listDrives=service.drives().list().execute();
	    for (com.google.api.services.drive.model.Drive drive : listDrives.getDrives())
	    {
	    	drivesArray.add(drive);//, file.getId());
	    }
		String keyword=searchBox.getText().toString();
		String query="name contains '".concat(keyword).concat("'");
		String pageToken = null;
		do
	      {
	        FileList result = null;
			try {
				result = service.files().list()
				.setQ(query)
				.setSupportsAllDrives(true)
				.setIncludeItemsFromAllDrives(true)
				.setSpaces("drive")
				.setCorpora("allDrives")
				.setFields("nextPageToken, files(id, name, size)")
				.setPageToken(pageToken)
				.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        //result;
	        for (File file : result.getFiles())
	        {
	        	list.add(file.getName());
				idList.add(file);
	        }
	        pageToken = result.getNextPageToken();
	      } while (pageToken != null);
	}

	public static void search(ArrayList<File> idList, 
							  java.awt.List list, 
							  Drive service, 
							  String driveID, 
							  java.awt.List list_1, 
							  ArrayList<com.google.api.services.drive.model.Drive> drivesArray, 
							  String pageToken,
							  String query,
							  JTextField searchBox) throws Exception
	{
		list.removeAll();
		idList.clear();
		DriveList listDrives=service.drives().list().execute();
	    for (com.google.api.services.drive.model.Drive drive : listDrives.getDrives())
	    {
	    	drivesArray.add(drive);
	    }
		String keyword=searchBox.getText().toString();
		query="name contains '".concat(keyword).concat("'");
		System.out.println(query);
		driveID=drivesArray.get(list_1.getSelectedIndex()).getId();
		System.out.println(drivesArray.get(list_1.getSelectedIndex()).getName());
		do
	      {
	        FileList result = null;
			try {
				result = service.files().list()
				.setQ(query)
				.setSupportsAllDrives(true)
				.setDriveId(driveID)
				.setIncludeItemsFromAllDrives(true)
				.setSpaces("drive")
				.setCorpora("drive")
				.setFields("nextPageToken, files(id, name, size)")
				.setPageToken(pageToken)
				.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        for (File file : result.getFiles())
	        {
	        	list.add(file.getName());
	            idList.add(file);
	        }
	        pageToken = result.getNextPageToken();
	      } while (pageToken != null);
	}
	
	public static void download(JButton btnNewButton_1, 
								JFileChooser fileChooser, 
								java.awt.List list, 
								ArrayList<File> idList, 
								Drive service, 
								JProgressBar progressBar)
	{
		new Thread(new Runnable() {
            @Override
            public void run() 
            {	
            	btnNewButton_1.setEnabled(false);
            	System.out.println(btnNewButton_1.isEnabled());
            	FileOutputStream destinatioFile=null;
				BufferedOutputStream buffer= null;
				fileChooser.showSaveDialog(null);
				CustomProgressListener.cancelSelection(btnNewButton_1);
				String path=fileChooser.getSelectedFile().getAbsolutePath().concat("/");
				int[] downloadIndexes=list.getSelectedIndexes();
				for(int i=0; i< downloadIndexes.length; i++)
				{
					
						String fileId = idList.get(downloadIndexes[i]).getId();
			          //File file = service.files().get(fileId).setFields("size").execute();
			          //long size=file.getSize();
						String fileName=idList.get(downloadIndexes[i]).getName();
			          try {
						destinatioFile = new FileOutputStream(path.concat(fileName), true);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
			          //clearScreen();
			          System.out.println("Chosen file: "+idList.get(downloadIndexes[i]).getName());//+" size : "+size);
			          long size1=(idList.get(downloadIndexes[i]).getSize());
			          //Integer size =(Integer)((idList.get(downloadIndexes[i]).getSize())/1240000/100);
			          int size=(int)(size1/100);
			          System.out.println(size);
			          if(size<15735000)
			          {
			        	  size=15735000;
			          }
			          buffer = new BufferedOutputStream(destinatioFile, 10490000);
			          Drive.Files.Get file = null;
					try {
						file = service.files().get(fileId);
					} catch (IOException e) {
						e.printStackTrace();
					}
						progressBar.setValue(0);
				        file.getMediaHttpDownloader().setProgressListener(new CustomProgressListener(progressBar)).setChunkSize(10490000);
						try 
						{
							file.executeMediaAndDownloadTo(buffer);
						}
						catch (SocketTimeoutException e)
						{
							try {
								resumeDownload(file, buffer, fileName, path, destinatioFile);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						
			          try {
						buffer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
			          System.out.println(idList.get(downloadIndexes[i]).getName()+" downloaded");
			          try {
							buffer.close();
							System.out.println("OH HO CHIUSO TUTTO");
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
				
				System.out.println("Enabling download button");
				progressBar.setValue(0);
				btnNewButton_1.setEnabled(true);
            }
        }).start();
	}
	
	public static void upload(Drive service, 
							  java.awt.List list_2, 
							  JFileChooser fileChooser_1, 
							  ArrayList<com.google.api.services.drive.model.Drive> drivesArray) throws Exception
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				List<java.io.File> files= new ArrayList();
				files = Arrays.asList(fileChooser_1.getSelectedFiles());
				for(int i=0; i<files.size(); i++)
				{
					File fileMetadata = new File();
					ArrayList<String> idList=new ArrayList();
					idList.add(drivesArray.get(list_2.getSelectedIndex()).getId());
					fileMetadata.setName(files.get(i).getName());
					fileMetadata.setParents(idList);
					java.io.File filePath = new java.io.File(files.get(i).getAbsolutePath());
					FileContent mediaContent = new FileContent(null, filePath);
					File file=null;
					try 
					{
						file = service.files()
									  .create(fileMetadata, mediaContent)
									  .setFields("id")
								      .setSupportsAllDrives(true)
								      .execute();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					System.out.println("File ID: " + file.getId());
				}
			}
		}).start();
	}
	
	public static void resumeDownload(Drive.Files.Get file, 
									  BufferedOutputStream buffer,
									  String fileName, 
									  String path,
									  FileOutputStream destinatioFile) throws IOException
	{
			System.out.println("repeat");
			java.io.File partFile= new java.io.File(path.concat(fileName));
			partFile.length();
			file.getMediaHttpDownloader().setBytesDownloaded(partFile.length());
			try {
				file.executeMediaAndDownloadTo(buffer);
			}catch(SocketTimeoutException e1)
			{
				buffer.close();
				buffer = new BufferedOutputStream(destinatioFile, 10480000);
				resumeDownload(file, buffer, fileName, path, destinatioFile);
			}catch(IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
	}
	
}
