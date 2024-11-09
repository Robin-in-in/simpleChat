package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(int UID ,String host, int port) 
  {
    try 
    {
      client= new ChatClient(UID, host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
    	String message, command;

      while (true) 
      {
        message = fromConsole.nextLine();
        
        //User is attempting to pass command
        
        if(message.charAt(0)=='#') {
        	command = message;
        	//Parsing quit command
        	if(command.equals("#quit")) {
        		client.quit();
        	} 
        	//Parsing logoff command
        	else if(command.equals("#logoff")) {
        		if(client.isConnected()) {
            		client.closeConnection();
        		}
        	} 
        	//Parsing sethost command (must be logged off)
        	else if(command.startsWith("#sethost")) {
        		if(!client.isConnected()) {
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
        		if(!client.isConnected()) {
        			String[] parts = command.split(" ", 2);
                    if (parts.length == 2) {
                        client.setPort(Integer.parseInt(parts[1]));
                        System.out.println("Port set to: " + parts[1]);
                    } else {
                        System.out.println("Usage: #setport <port>");
                    }
        		} else {
        			System.out.println("Invalid command- Already logged in.");
        		}
        	}
        	//Parsing login command
        	else if(command.startsWith("#login")) {
        		client.openConnection();
        		client.handleMessageFromClientUI(command);
        	} 
        	//Parsing getHost command
        	else if(command.equals("#gethost") ) {
        		System.out.println("Current host is: " + client.getHost());
        	}
        	//Parsing getPort command
        	else if(command.equals("#getport")) {
        		System.out.println("Current port number is: " + String.valueOf(client.getPort()));
        	} else {
        		System.out.println("Invalid command.");
        	}
        } else {
        	client.handleMessageFromClientUI(message);
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;
    int UID=0;
    
    try {
    	UID = Integer.parseInt(args[0]);
    } catch(ArrayIndexOutOfBoundsException e) {
    	System.out.println("Must provide User Id as first argument. Terminating program");
    	return;
    } catch(NumberFormatException e) {
    	System.out.println("Must provide User Id as integer. Terminating program");
    	return;
    }


    try
    {
      host = args[1];
      try {
          port = Integer.parseInt(args[2]);
      }
      catch(NumberFormatException e) {
    	  port = DEFAULT_PORT;
      }
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
      port = DEFAULT_PORT;
    }
    ClientConsole chat= new ClientConsole(UID, host, port);
    chat.accept();//Wait for console data
  }
}
//End of ConsoleChat class
