import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Create;
import com.google.api.services.drive.model.DriveList;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
									   ArrayList<File> idList, JButton btnNewButton_1) throws IOException
	{
		btnNewButton_1.setText("Download");
		btnNewButton_1.setForeground(new Color(3355443));
		clearList(list, idList);
		String keyword=searchBox.getText().toString();
		String query="mimeType != 'application/vnd.google-apps.folder' and name contains '".concat(keyword).concat("'");
		String pageToken = null;
		long totalSize=0;
		do
	      {
	        FileList result = null;
			try {
				result = service
						.files()
						.list()
						.setQ(query)
						.setSupportsAllDrives(true)
						.setIncludeItemsFromAllDrives(true)
						.setSpaces("drive")
						.setCorpora("allDrives")
						.setFields("nextPageToken, files(id, name, size, mimeType)")
						.setPageToken(pageToken)
						.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        for (File file : result.getFiles())
	        {
	        	//File name=new File();
	        	//name.setName(file.getName().substring(8));
	        	//service.files().update(file.getId(), name).execute();
	        	list.add(file.getName());
	        	if(file.getSize()!=null)
				{
					totalSize=totalSize+file.getSize();
				}
				idList.add(file);
	        }
	        pageToken = result.getNextPageToken();
	      } while (pageToken != null);
		idList.trimToSize();
		if(idList.size()==0)
		{
			noFileFound(list);
		}
		System.out.println(idList.size());
		System.out.println("Total size (in GiB)"+(((totalSize/1024)/1024)/1024));
	}
	
	public static void searchMyDrives(ArrayList<com.google.api.services.drive.model.Drive> drivesArray, 
									  Drive service, 
									  JTextField searchBox, 
									  java.awt.List list, 
									  ArrayList<File> idList, JButton btnNewButton_1) throws IOException
	{
		btnNewButton_1.setText("Download");
		btnNewButton_1.setForeground(new Color(3355443));
		clearList(list, idList);
		String keyword=searchBox.getText().toString();
		String query="mimeType != 'application/vnd.google-apps.folder' and name contains '".concat(keyword).concat("'");
		String pageToken = null;
		long totalSize=0;
		do
		{
			FileList result = null;
			try {
				result = service
						.files()
						.list()
						.setQ(query)
						.setSpaces("drive")
						.setFields("nextPageToken, files(id, name, size, mimeType)")
						.setPageToken(pageToken)
						.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (File file : result.getFiles())
			{
				//File name=new File();
	        	//name.setName(file.getName().substring(9));
	        	//service.files().update(file.getId(), name).execute();
				list.add(file.getName());
				if(file.getSize()!=null)
				{
					totalSize=totalSize+file.getSize();
				}
				idList.add(file);
			}
			pageToken = result.getNextPageToken();
		} while (pageToken != null);
		idList.trimToSize();
		if(idList.size()==0)
		{
			noFileFound(list);
		}
		System.out.println(idList.size());
		System.out.println("Total size (in GiB)"+(((totalSize/1024)/1024)/1024));
	}

	public static void search(ArrayList<File> idList, 
							  java.awt.List list, 
							  Drive service, 
							  String driveID, 
							  java.awt.List list_1, 
							  ArrayList<com.google.api.services.drive.model.Drive> drivesArray, 
							  String pageToken,
							  String query,
							  JTextField searchBox, JButton btnNewButton_1) throws Exception
	{
		btnNewButton_1.setText("Download");
		btnNewButton_1.setForeground(new Color(3355443));
		clearList(list, idList);
		String keyword=searchBox.getText().toString();
		query="mimeType != 'application/vnd.google-apps.folder' and name contains '".concat(keyword).concat("'");
		System.out.println(query);
		driveID=drivesArray.get(list_1.getSelectedIndex()).getId();
		System.out.println(drivesArray.get(list_1.getSelectedIndex()).getName());
		long totalSize=0;
		do
	      {
	        FileList result = null;
			try {
				result = service
						.files()
						.list()
						.setQ(query)
						.setSupportsAllDrives(true)
						.setDriveId(driveID)
						.setIncludeItemsFromAllDrives(true)
						.setSpaces("drive")
						.setCorpora("drive")
						.setFields("nextPageToken, files(id, name, size, mimeType)")
						.setPageToken(pageToken)
						.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        for (File file : result.getFiles())
	        {
	        	//File name=new File();
	        	//name.setName(file.getName().substring(9));
	        	//service.files().update(file.getId(), name).setSupportsAllDrives(true).execute();
	        	list.add(file.getName());
	        	if(file.getSize()!=null)
				{
					totalSize=totalSize+file.getSize();
				}
	            idList.add(file);
	        }
	        pageToken = result.getNextPageToken();
	      } while (pageToken != null);
		idList.trimToSize();
		if(idList.size()==0)
		{
			noFileFound(list);
		}
		System.out.println(idList.size());
		System.out.println("Total size (in GiB)"+(((totalSize/1024)/1024)/1024));
	}
	
	public static void download(JButton btnNewButton_3,
								JButton btnNewButton_1,
								JButton btnNewButton,
								JFileChooser fileChooser, 
								java.awt.List list, 
								ArrayList<File> idList, 
								Drive service, 
								JProgressBar progressBar,
								JFrame frmGoogleDriveUtility,
								JTextField textField, JButton btnNewButton_4)
	{
		textField.setEnabled(false);
		btnNewButton_1.setText("Download");
		btnNewButton_1.setForeground(new Color(3355443));//3355443
		clearList(list, idList);
		System.out.println(btnNewButton_1.isEnabled());
		FileOutputStream destinatioFile=null;
		BufferedOutputStream buffer= null;
		int result = fileChooser.showSaveDialog(frmGoogleDriveUtility);
		if(result == JFileChooser.CANCEL_OPTION)
		{
			btnNewButton.setEnabled(true);
			btnNewButton_1.setEnabled(true);
			btnNewButton_3.setEnabled(true);
			btnNewButton_4.setEnabled(true);
			textField.setEnabled(true);
			return;
		}
		String path=fileChooser.getSelectedFile().getAbsolutePath().concat("/");
		for(int i=0; i< idList.size(); i++)
		{
			String fileId = idList.get(i).getId();
			String mimeType = idList.get(i).getMimeType();
			System.out.println(mimeType);
			String fileName=idList.get(i).getName();
			String convertToType="";
			boolean export;

			if(mimeType.contentEquals("application/vnd.google-apps.document"))
			{
				System.out.println("docx");
				convertToType=convertToType.concat("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				fileName=fileName.concat(".docx");
				export=true;
			}
			else if(mimeType.contentEquals("application/vnd.google-apps.spreadsheet"))
			{
				System.out.println("xlsx");
				convertToType=convertToType.concat("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				fileName=fileName.concat(".xlsx");
				export=true;
			}
			else if(mimeType.contentEquals("application/vnd.google-apps.presentation"))
			{
				System.out.println("pptx");
				convertToType=convertToType.concat("application/vnd.openxmlformats-officedocument.presentationml.presentation");
				fileName=fileName.concat(".pptx");
				export=true;
			}
			else
			{
				export=false;
			}

			try 
			{
				destinatioFile = new FileOutputStream(path.concat(fileName), true);
			}
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			System.out.println("Chosen file: "+idList.get(i).getName()+"\n"+export);
			long size1=0;
			if(!export)
			{
				size1=(idList.get(i).getSize());
			}
			buffer = new BufferedOutputStream(destinatioFile, 10490000);
			Drive.Files.Get file = null;
			try 
			{
				file = service.files().get(fileId);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			if(export)
			{
				System.out.println("MIMEType detected");
				System.out.println(convertToType+"\n"+fileName);
				try 
				{
					service.files().export(fileId, convertToType)
					.executeMediaAndDownloadTo(buffer);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				progressBar.setValue(0);
				progressBar.setIndeterminate(true);
				file.getMediaHttpDownloader()
				.setDirectDownloadEnabled(false)
				.setProgressListener(new CustomProgressListener(progressBar))
				.setChunkSize(10490000);
				try 
				{
					file.executeMediaAndDownloadTo(buffer);
				}
				catch (SocketTimeoutException e)
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			try {
				buffer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(idList.get(i).getName()+" downloaded");
			try 
			{
				buffer.close();
				System.out.println("OH HO CHIUSO TUTTO");
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			java.io.File fileDownloaded= new java.io.File(path.concat(fileName));
			System.out.println("File size: "+fileDownloaded.length());
			System.out.println("File size drive: "+size1);
			long downloadedFile=fileDownloaded.length();
			long driveSize=size1;
			if(downloadedFile<driveSize)
			{
				System.out.println("File eliminato");
				fileDownloaded.delete();
			}
			else
			{
				System.out.println("OK");
			}
		}
		System.out.println("Enabling download button");
		progressBar.setIndeterminate(false);
		progressBar.setValue(0);
		btnNewButton.setEnabled(true);
		btnNewButton_1.setEnabled(true);
		btnNewButton_1.setText("Completed");
		btnNewButton_1.setForeground(new Color(65280));//3355443
		btnNewButton_3.setEnabled(true);
		btnNewButton_4.setEnabled(true);
		textField.setEnabled(true);
	}
	
	public static void uploadShared(Drive service, 
							  		java.awt.List list_2, 
							  		JFileChooser fileChooser_1, 
							  		ArrayList<com.google.api.services.drive.model.Drive> drivesArray,
							  		JLabel lblNewLabel_1,
							  		JProgressBar progressBar_1) throws Exception
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				lblNewLabel_1.setVisible(false);
				List<java.io.File> files= new ArrayList<java.io.File>();
				files = Arrays.asList(fileChooser_1.getSelectedFiles());
				for(int i=0; i<files.size(); i++)
				{
					File fileMetadata = new File();
					ArrayList<String> idList=new ArrayList<String>();
					idList.add(drivesArray.get(list_2.getSelectedIndex()).getId());
					fileMetadata.setName(files.get(i).getName());
					fileMetadata.setParents(idList);
					java.io.File filePath = new java.io.File(files.get(i).getAbsolutePath());
					FileContent mediaContent = new FileContent(null, filePath);
					Create create=null;
					File file=null;
					try 
					{
						create = service.files()
										.create(fileMetadata, mediaContent);

						progressBar_1.setValue(0);
						progressBar_1.setIndeterminate(true);
						
						create.setFields("id")
							  .setSupportsAllDrives(true)
							  .getMediaHttpUploader()
							  .setProgressListener(new CustomProgressListener(progressBar_1))
							  .setChunkSize(5242880);
						
						file = create.execute();
						
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					System.out.println("File ID: " + file.getId());
				}
				lblNewLabel_1.setVisible(true);
			}
		}).start();
	}

	public static void upload(Drive service, 
							  java.awt.List list_2, 
							  JFileChooser fileChooser_1, 
							  ArrayList<com.google.api.services.drive.model.Drive> drivesArray,
							  JLabel lblNewLabel_1,
							  JProgressBar progressBar_1) throws Exception
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				lblNewLabel_1.setVisible(false);
				List<java.io.File> files= new ArrayList<java.io.File>();
				files = Arrays.asList(fileChooser_1.getSelectedFiles());
				for(int i=0; i<files.size(); i++)
				{
					File fileMetadata = new File();
					fileMetadata.setName(files.get(i).getName());
					java.io.File filePath = new java.io.File(files.get(i).getAbsolutePath());
					FileContent mediaContent = new FileContent(null, filePath);
					Create create=null;
					File file=null;
					try 
					{
						create = service.files()
								 .create(fileMetadata, mediaContent);
						
						progressBar_1.setValue(0);
						progressBar_1.setIndeterminate(true);
						
						create.getMediaHttpUploader()
							  .setProgressListener(new CustomProgressListener(progressBar_1))
							  .setChunkSize(5242880);
						file = create.execute();		
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					System.out.println("File ID: " + file.getId());
				}
				lblNewLabel_1.setVisible(true);
			}
		}).start();
	}
	
	public static void clearList(java.awt.List list,
								 ArrayList<File> idList) 
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				ArrayList<File> supportList=new ArrayList<File>();
				int i=0;
				int[] indexes = list.getSelectedIndexes();
				if(indexes.length==0||(indexes.length==1&&idList.size()==0))
				{
					long start=System.currentTimeMillis();
					list.removeAll();
					idList.clear();
					long stop=System.currentTimeMillis();
					long elaspsedTime=stop-start;
					System.out.println("Time: "+(float)elaspsedTime/1000);
					return;
				}
				else
				{
					long start=System.currentTimeMillis();
					list.removeAll();
					for(i=0;i<indexes.length;i++)
					{
						supportList.add(idList.get(indexes[i]));
						list.add(idList.get(indexes[i]).getName());
						list.select(i);
					}
					idList.clear();
					for(i=0; i<supportList.size();i++)
					{
						idList.add(supportList.get(i));
					}
					long stop=System.currentTimeMillis();
					long elaspsedTime=stop-start;
					System.out.println("Time: "+(float)elaspsedTime/1000);
				}
				idList.trimToSize();
			}
		}).start();
	}

	public static void noFileFound(java.awt.List list)
	{
		list.add("No Results Found");
	}
	
	public static void rename() 
	{
			
	}
}