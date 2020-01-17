import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;

import java.io.IOException;

import javax.swing.JProgressBar;

class CustomProgressListener implements MediaHttpDownloaderProgressListener, MediaHttpUploaderProgressListener
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
    	 
    	progressBar.setIndeterminate(false); 
        progressBar.setValue((int)(downloader.getProgress()*100)); 
        break;
        
      case MEDIA_COMPLETE:
    	  
    	progressBar.setValue(100);
        break;
        
      default:
		
		System.out.println("Progress not available");
		break;
    }
  }

  public void progressChanged(MediaHttpUploader uploader) throws IOException 
  {
	  
	    switch (uploader.getUploadState())
	    {
	      case MEDIA_IN_PROGRESS:
	    	
	    	progressBar.setIndeterminate(false);  
	        progressBar.setValue((int)(uploader.getProgress()*100)); 
	        break;
	        
	      case MEDIA_COMPLETE:
	    	  
	    	progressBar.setValue(100);
	        break;
	        
	      default:
			
			System.out.println("Progress not available");
			break;
	    }
  }
}