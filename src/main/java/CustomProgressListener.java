import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.DriveList;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JFileChooser;

class CustomProgressListener implements MediaHttpDownloaderProgressListener
{
	private JProgressBar progressBar;
	
	public CustomProgressListener(JProgressBar progressBar)
	{
		super();
		this.progressBar=progressBar;
		
	}
	
  public void progressChanged(MediaHttpDownloader downloader)
  {
    switch (downloader.getDownloadState())
    {
      case MEDIA_IN_PROGRESS:
        //System.out.println(downloader.getName());
        //Comunismo_Ludico_Downloader.clearScreen();
        //progressBar.setValue((int)downloader.getProgress()*100);
    	
        progressBar.setValue((int)(downloader.getProgress()*100)); 
        break;
      case MEDIA_COMPLETE:
    	  progressBar.setValue(100);
    	  try {
              Thread.sleep(2000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
        break;
	default:
		System.out.println("Progress not available");
		break;
    }
  }
public static void cancelSelection(JButton btn)
  {
	  btn.setEnabled(true);
	  Thread.currentThread().interrupt();
  }
}