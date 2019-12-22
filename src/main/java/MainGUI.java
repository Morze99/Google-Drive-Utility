import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
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
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JFileChooser;
import java.awt.SystemColor;
import javax.swing.JPanel;
import java.awt.Label;

public class MainGUI 
{
	
	private JFrame frmGoogleDriveUtility;
	private JTextField textField;
	
	private static final String APPLICATION_NAME = "Comunismo Ludico Downloader";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = MainGUI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

	/**
	 * Launch the application.
	 */
    
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					MainGUI window = new MainGUI();
					window.frmGoogleDriveUtility.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	
	public MainGUI() throws Exception 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	
	private void initialize() throws Exception 
	{
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	    Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	            .setApplicationName(APPLICATION_NAME)
	            .build();
		ArrayList<File> idList = new ArrayList<File>();
		ArrayList<com.google.api.services.drive.model.Drive> drivesArray= new ArrayList<com.google.api.services.drive.model.Drive>();
		String driveID=null, pageToken=null, query=null;
		
		frmGoogleDriveUtility = new JFrame();
		frmGoogleDriveUtility.setResizable(false);
		frmGoogleDriveUtility.setFont(new Font("Tahoma", Font.PLAIN, 20));
		frmGoogleDriveUtility.setIconImage(Toolkit.getDefaultToolkit().getImage(MainGUI.class.getResource("/javax/swing/plaf/metal/icons/ocean/upFolder.gif")));
		frmGoogleDriveUtility.setTitle("Google Drive Utility");
		frmGoogleDriveUtility.setBounds(100, 100, 917, 618);
		frmGoogleDriveUtility.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGoogleDriveUtility.getContentPane().setLayout(null);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setEnabled(true);
		fileChooser.setVisible(true);
		fileChooser.setFileSelectionMode(1);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setApproveButtonText("Save here");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(SystemColor.control);
		tabbedPane.setBounds(0, 0, 895, 562);
		frmGoogleDriveUtility.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Download", null, panel, null);
		panel.setLayout(null);
		
		java.awt.List list = new java.awt.List();
		list.setMultipleMode(true);
		list.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list.setBounds(15, 47, 427, 413);
		panel.add(list);
		
		java.awt.List list_1 = new java.awt.List();
		list_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list_1.setBounds(473, 49, 402, 413);
		panel.add(list_1);
		
		Methods.listDrives(list_1, service, drivesArray);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(0, 204, 204));
		progressBar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		progressBar.setBounds(201, 466, 241, 45);
		panel.add(progressBar);
		
		JLabel lblNewLabel = new JLabel("Search:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(15, 16, 69, 20);
		panel.add(lblNewLabel);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Search in all drives");
		chckbxNewCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) 
			{
				if(chckbxNewCheckBox.isSelected())
				{
					list_1.setEnabled(false);
					list_1.deselect(list_1.getSelectedIndex());
				}
				else
				{
					list_1.setEnabled(true);
				}
			}
		});
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chckbxNewCheckBox.setBounds(668, 14, 207, 29);
		panel.add(chckbxNewCheckBox);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyReleased(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					new Thread (new Runnable()
					{
						public void run()
						{
							try 
							{
								if(chckbxNewCheckBox.isSelected() | (list_1.getSelectedIndex()==-1 && !chckbxNewCheckBox.isSelected()))
								{
									Methods.searchAllDrives(drivesArray,
															service, 
															textField, 
															list, 
															idList);
								}
								else
								{
									Methods.search(idList, 
												   list, 
												   service, 
												   driveID, 
												   list_1, 
												   drivesArray, 
												   pageToken, 
												   query, 
												   textField);
								}
							} catch (Exception e1) 
							{
								e1.printStackTrace();
							}
						}
					}).start();			
				}	
			}
		});
		
		textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textField.setBounds(99, 15, 213, 26);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				new Thread(new Runnable() 
				{
		            @Override
		            public void run() 
		            {
		            	try 
		            	{
		            		if(chckbxNewCheckBox.isSelected() | (list_1.getSelectedIndex()==-1 && !chckbxNewCheckBox.isSelected()))
		            		{
		            			Methods.searchAllDrives(drivesArray,
		            									service, 
		            									textField, 
		            									list, 
		            									idList);
		            		}
		            		else
		            		{
		            			Methods.search(idList, 
		            						   list, 
		            						   service, 
		            						   driveID, 
		            						   list_1, 
		            						   drivesArray, 
		            						   pageToken, 
		            						   query, 
		            						   textField);
		            		}
		            	} catch (Exception e1) 
		            	{
		            		e1.printStackTrace();
		            	}
		            }
		        }).start();
			}
		});
		
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.setBounds(327, 12, 115, 29);
		panel.add(btnNewButton);
		
		JLabel lblDrivesList = new JLabel("Shared Drives List:");
		lblDrivesList.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDrivesList.setBounds(473, 18, 184, 20);
		panel.add(lblDrivesList);
		
		JButton btnNewButton_1 = new JButton("Download selected files");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Methods.download(btnNewButton_1, fileChooser, list, idList, service, progressBar);
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_1.setBounds(473, 466, 402, 46);
		panel.add(btnNewButton_1);
		
		
		JLabel lblSingleFileProgress = new JLabel("Single file progress:");
		lblSingleFileProgress.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSingleFileProgress.setBounds(15, 466, 184, 46);
		panel.add(lblSingleFileProgress);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Upload", null, panel_1, null);
		panel_1.setLayout(null);
		
		JFileChooser fileChooser_1 = new JFileChooser();
		fileChooser_1.setBorder(null);
		fileChooser_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		fileChooser_1.setMultiSelectionEnabled(true);
		fileChooser_1.setBounds(15, 16, 431, 496);
		panel_1.add(fileChooser_1);
		
		java.awt.List list_2 = new java.awt.List();
		list_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list_2.setBounds(452, 58, 422, 376);
		panel_1.add(list_2);
		
		Methods.listDrives(list_2, service, drivesArray);
		
		Label label = new Label("Drives lists:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label.setBounds(452, 25, 105, 27);
		panel_1.add(label);
		
		JButton btnNewButton_2 = new JButton("Upload");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					Methods.upload(service, list_2, fileChooser_1, drivesArray);
				} catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_2.setBounds(452, 440, 422, 60);
		panel_1.add(btnNewButton_2);
	}
}
