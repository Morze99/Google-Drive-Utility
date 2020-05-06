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
	
	private static final String APPLICATION_NAME = "Google Drive Utility";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException 
    {
        // Load client secrets.
        InputStream in = MainGUI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) 
        {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
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
		drivesArray.add(null);
		Methods.listDrives(service, drivesArray);
		String driveID=null, pageToken=null, query=null;
		
		frmGoogleDriveUtility = new JFrame();
		frmGoogleDriveUtility.setResizable(false);
		frmGoogleDriveUtility.setFont(new Font("Tahoma", Font.PLAIN, 20));
		frmGoogleDriveUtility.setTitle("Google Drive Utility");
		frmGoogleDriveUtility.setBounds(100, 100, 900, 584);
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
		tabbedPane.setBounds(0, 0, 895, 549);
		frmGoogleDriveUtility.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Download", null, panel, null);
		panel.setLayout(null);
		
		JButton btnNewButton_4 = new JButton("Select All");
		
		JButton btnNewButton_1 = new JButton("Download");
		
		java.awt.List list = new java.awt.List();
		list.addItemListener(new ItemListener() 
		{
			public void itemStateChanged(ItemEvent e) 
			{
				System.out.println("State changed"+"\n"+list.getSelectedIndexes().length+"="+list.getItemCount());
				if(list.getSelectedIndexes().length>0&&list.getSelectedIndexes().length<list.getItemCount())
				{
					btnNewButton_4.setText("Deselect");
					btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
				}
				else if(list.getSelectedIndexes().length==list.getItemCount())
				{
					btnNewButton_4.setText("Deselect All");
					btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
				}
				else
				{
					btnNewButton_4.setText("Select All");
					btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
				}
			}
		});
		list.setMultipleMode(true);
		list.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list.setBounds(15, 47, 427, 413);
		panel.add(list);
		
		java.awt.List list_1 = new java.awt.List();
		list_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list_1.setBounds(473, 49, 402, 360);
		panel.add(list_1);
		
		list_1.add("MyDrive");
		for(int i = 1; i<drivesArray.size(); i++)
		{
			list_1.add(drivesArray.get(i).getName());
		}
		
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
								if(!chckbxNewCheckBox.isSelected() && list_1.getSelectedIndex()>0)
								{
									System.out.println("search");
									Methods.search(idList, 
											   list, 
											   service, 
											   driveID, 
											   list_1, 
											   drivesArray, 
											   pageToken, 
											   query, 
											   textField,
											   btnNewButton_1);
								}
								else if(list_1.isIndexSelected(0))
								{
									System.out.println("searchMyDrive");
									Methods.searchMyDrives(drivesArray, 
														   service, 
														   textField, 
														   list, 
														   idList,
														   btnNewButton_1);
								}
								else
								{
									System.out.println("searchAllDrives");
									Methods.searchAllDrives(drivesArray,
											service, 
											textField, 
											list, 
											idList,
											btnNewButton_1);
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
		            		if(!chckbxNewCheckBox.isSelected() && list_1.getSelectedIndex()>0)
							{
								System.out.println("search");
								Methods.search(idList, 
										   list, 
										   service, 
										   driveID, 
										   list_1, 
										   drivesArray, 
										   pageToken, 
										   query, 
										   textField, btnNewButton_1);
							}
							else if(list_1.isIndexSelected(0))
							{
								System.out.println("searchMyDrive");
								Methods.searchMyDrives(drivesArray, 
													   service, 
													   textField, 
													   list, 
													   idList, btnNewButton_1);
							}
							else
							{
								System.out.println("searchAllDrives");
								Methods.searchAllDrives(drivesArray,
										service, 
										textField, 
										list, 
										idList, btnNewButton_1);
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
		
		JButton btnNewButton_3 = new JButton("Remove unselected items");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Methods.clearList(list, idList);
					}
				}).start();
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_3.setBounds(472, 463, 402, 46);
		panel.add(btnNewButton_3);
		
		JLabel lblDrivesList = new JLabel("Shared Drives List:");
		lblDrivesList.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDrivesList.setBounds(473, 18, 184, 20);
		panel.add(lblDrivesList);
		
		
		btnNewButton_1.setForeground(Color.DARK_GRAY);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				btnNewButton.setEnabled(false);
				btnNewButton_1.setEnabled(false);
				btnNewButton_3.setEnabled(false);
				btnNewButton_4.setEnabled(false);
				
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Methods.download(btnNewButton_3,
										 btnNewButton_1, 
										 btnNewButton, 
										 fileChooser, 
										 list, 
										 idList, 
										 service, 
										 progressBar, 
										 frmGoogleDriveUtility,
										 textField, btnNewButton_4);
					}
				}).start();
				
				//btnNewButton_1.setEnabled(true);
				//btnNewButton.setEnabled(true);
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_1.setBounds(473, 415, 197, 46);
		panel.add(btnNewButton_1);
		
		
		JLabel lblSingleFileProgress = new JLabel("Single file progress:");
		lblSingleFileProgress.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSingleFileProgress.setBounds(15, 466, 184, 46);
		panel.add(lblSingleFileProgress);
		
		
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				if(list.getSelectedIndexes().length==0&&list.getItemCount()!=0)
				{
					for(int i=0; i<list.getItemCount(); i++)
					{
						list.select(i);
					}
					btnNewButton_4.setText("Deselect All");
					btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
					
					//btnNewButton_4.setBounds(473, 466, 150, 45);
				}
				else if(list.getSelectedIndexes().length==list.getItemCount())
				{
					for(int i=0; i<list.getItemCount(); i++)
					{
						list.deselect(i);
					}
					btnNewButton_4.setText("Select All");
					btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
					//btnNewButton_4.setBounds(473, 466, 128, 45);
				}
				else if(list.getSelectedIndexes().length>0&&list.getSelectedIndexes().length<list.getItemCount())
				{
					int[] selected=new int[list.getSelectedIndexes().length];
					selected=list.getSelectedIndexes();
					for(int i=0; i<selected.length; i++)
					{
						list.deselect(selected[i]);
					}
					btnNewButton_4.setText("Select All");
					btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
				}
			}
		});
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_4.setBounds(678, 415, 197, 46);
		panel.add(btnNewButton_4);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Upload", null, panel_1, null);
		panel_1.setLayout(null);
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		progressBar_1.setForeground(new Color(0, 204, 204));
		progressBar_1.setStringPainted(true);
		progressBar_1.setBounds(634, 417, 240, 38);
		panel_1.add(progressBar_1);
		
		JFileChooser fileChooser_1 = new JFileChooser();
		fileChooser_1.setBorder(null);
		fileChooser_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		fileChooser_1.setMultiSelectionEnabled(true);
		fileChooser_1.setBounds(15, 16, 431, 496);
		panel_1.add(fileChooser_1);
		
		java.awt.List list_2 = new java.awt.List();
		list_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list_2.setBounds(452, 57, 422, 298);
		panel_1.add(list_2);
		
		list_2.add("MyDrive");
		for(int i = 1; i<drivesArray.size(); i++)
		{
			list_2.add(drivesArray.get(i).getName());
		}
		
		Label label = new Label("Shared Drives lists:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label.setBounds(452, 16, 184, 27);
		panel_1.add(label);
		
		JLabel lblNewLabel_1 = new JLabel("All uploads completed");
		lblNewLabel_1.setForeground(new Color(0, 204, 0));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(574, 470, 198, 25);
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setVisible(false);
		
		/*JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Upload to MyDrive");
		chckbxNewCheckBox_1.addItemListener(new ItemListener() 
		{
			public void itemStateChanged(ItemEvent arg0) 
			{
				if(chckbxNewCheckBox_1.isSelected())
				{
					list_2.setEnabled(false);
					list_2.deselect(list_2.getSelectedIndex());
				}
				else
				{
					list_2.setEnabled(true);
				}
			}
		});
		chckbxNewCheckBox_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chckbxNewCheckBox_1.setBounds(670, 16, 204, 29);
		panel_1.add(chckbxNewCheckBox_1);*/
		
		JButton btnNewButton_2 = new JButton("Upload");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(list_2.getSelectedIndexes().length==0||list_2.getSelectedIndex()==0)
				{
					try 
					{
						Methods.upload(service, 
									   list_2, 
									   fileChooser_1, 
									   drivesArray, 
									   lblNewLabel_1, 
									   progressBar_1);
					} 
					catch (Exception e1) 
					{
						e1.printStackTrace();
					}
				}
				else
				{
					try 
					{
						Methods.uploadShared(service, 
											 list_2, 
											 fileChooser_1, 
											 drivesArray, 
											 lblNewLabel_1, 
											 progressBar_1);
					}
				    catch (Exception e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_2.setBounds(452, 361, 422, 43);
		panel_1.add(btnNewButton_2);
		
		JLabel label_1 = new JLabel("Single file progress:");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_1.setBounds(452, 409, 184, 46);
		panel_1.add(label_1);
		
		/*
		 * JPanel panel_2 = new JPanel(); tabbedPane.addTab("Settings", null, panel_2,
		 * null); panel_2.setLayout(null);
		 * 
		 * JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Search only for folders");
		 * chckbxNewCheckBox_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		 * chckbxNewCheckBox_2.setBounds(11, 52, 241, 29);
		 * panel_2.add(chckbxNewCheckBox_2);
		 * 
		 * JCheckBox chckbxSearchForBoth = new
		 * JCheckBox("Search for both files and folders");
		 * chckbxSearchForBoth.setFont(new Font("Tahoma", Font.PLAIN, 20));
		 * chckbxSearchForBoth.setBounds(11, 89, 323, 29);
		 * panel_2.add(chckbxSearchForBoth);
		 * 
		 * JLabel lblNewLabel_2 = new JLabel("Search for file type:");
		 * lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		 * lblNewLabel_2.setBounds(16, 140, 200, 29); panel_2.add(lblNewLabel_2);
		 * 
		 * JLabel lblmimeType = new JLabel("(MIME Type)");
		 * lblmimeType.setForeground(Color.LIGHT_GRAY); lblmimeType.setFont(new
		 * Font("Tahoma", Font.PLAIN, 20)); lblmimeType.setBounds(194, 140, 139, 29);
		 * panel_2.add(lblmimeType);
		 * 
		 * java.awt.List list_3 = new java.awt.List(); list_3.setBounds(16, 175, 318,
		 * 330); panel_2.add(list_3);
		 * 
		 * java.awt.List list_4 = new java.awt.List(); list_4.setBounds(358, 175, 318,
		 * 330); panel_2.add(list_4);
		 * 
		 * JLabel label_2 = new JLabel("Search for file type:"); label_2.setFont(new
		 * Font("Tahoma", Font.PLAIN, 20)); label_2.setBounds(358, 140, 200, 29);
		 * panel_2.add(label_2);
		 * 
		 * JLabel lblfilesExtension = new JLabel("(Extension)");
		 * lblfilesExtension.setForeground(Color.LIGHT_GRAY);
		 * lblfilesExtension.setFont(new Font("Tahoma", Font.PLAIN, 20));
		 * lblfilesExtension.setBounds(537, 140, 139, 29);
		 * panel_2.add(lblfilesExtension);
		 */
		
	}
}
