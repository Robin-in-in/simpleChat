package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.ui.ServerConsole;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	if(String.valueOf(msg).startsWith("#login")||client.getInfo("UID")==null) {
		if(client.getInfo("UID")==null) {
			String[] parts = String.valueOf(msg).split(" ", 2);
	        if (parts.length == 2) {
	            client.setInfo("UID", parts[1]);
	            System.out.println("CurrentUID set to: " + parts[1]);
	            return;
	        } else {
	            System.out.println("Usage: #login <UID>");
	        }
		} else {
			System.out.println("Imporper login. Terminating connection.");	
		}
		try {
			client.close();
		} catch(IOException e) {
			System.out.println("Failed to terminate client.");
		}
		
  	}  else {
  		System.out.println("Message received: " + msg + " from " + client);
  		this.sendToAllClients(String.valueOf(client.getInfo("UID")) + "> " + msg);
  	}   
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    sv.startServerConsole(sv);
  }

	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client connected!");
	}

	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("Client disconnected.");
	}
	
	private void startServerConsole(EchoServer server) {
		//starting with localhost, since the console will run on the same machine as the server itself.
		ServerConsole chat= new ServerConsole("localhost", getPort(), server);
		chat.accept();
	}

}
//End of EchoServer class
