package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


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
	  /*
	   * //User is attempting to pass command
        
        if(message.charAt(0)=='#') {
        	command = message;
        	//Parsing quit command
        	if(command.equals("#quit")) {
        		client.quit();
        	} 
        	//Parsing logoff command
        	else if(command.equals("#logoff")) {
        		if(loggedIn) {
            		client.closeConnection();
            		loggedIn = false;
        		}
        	} 
        	//Parsing sethost command (must be logged off)
        	else if(command.startsWith("#sethost")) {
        		if(!loggedIn) {
        			String[] parts = command.split(" ", 2);
                    if (parts.length == 2) {
                        client.setHost(parts[1]);
                    } else {
                        System.out.println("Usage: #sethost <host>");
                    }
        		} else {
        			System.out.println("Invalid command- Already logged in.");
        		}
        	}
        	//Parsing setport command (must be logged off)
        	else if(command.startsWith("#setport")) {
        		if(!loggedIn) {
        			String[] parts = command.split(" ", 2);
                    if (parts.length == 2) {
                        client.setPort(Integer.getInteger(parts[1]));
                    } else {
                        System.out.println("Usage: #setport <port>");
                    }
        		} else {
        			System.out.println("Invalid command- Already logged in.");
        		}
        	}
        	//Parsing login command
        	else if(command.equals("#login")) {
        		client.openConnection();
        		loggedIn=true;
        	} 
        	//Parsing getHost command
        	else if(command.equals("#gethost") ) {
        		System.out.println("Current host is: " + client.getHost());
        	}
        	//Parsing getPort command
        	else if(command.equals("#getport")) {
        		System.out.println("Current port number is: " + String.valueOf(client.getPort()));
        	} else {
        		//Pass as message, server will handle if servercommand
        		client.handleMessageFromClientUI(command);
        	}
        }
	   */
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
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
  }

	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client connected!");
	}

	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("Client disconnected.");
	}
}
//End of EchoServer class
