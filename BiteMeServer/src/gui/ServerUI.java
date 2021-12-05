package gui;

import ClientServerCommunication.Server;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Server UI logic
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu 
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 * */
public class ServerUI extends Application{ 
	
	/** Saving an instance of the server logic.*/
	public static Server sv;
	final public static int DEFAULT_PORT = 5555;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Method starts our server application gui.
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerGUIController aFrame = new ServerGUIController(); // Create controller
		primaryStage.setResizable(false);
		aFrame.start(primaryStage);
	}
	
	/**
	 * Creating new server connection
	 * 
	 * @param p
	 * */
	public static void runServer(String p) 
	{
		 int port = 0; //Port to listen on

	        try
	        {
	        	port = Integer.parseInt(p); //Set port to 5555
	          
	        }
	        catch(Throwable t)
	        {
	        	System.out.println("ERROR - Could not connect!");
	        }
	    	
	        sv = new Server(port);
	        
	        try 
	        {
	          sv.listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
	          System.out.println("ERROR - Could not listen for clients!");
	        }
	}

}
