package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;
import edu.seg2105.edu.server.backend.EchoServer;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ServerConsole.
   */
  ChatClient serverClient;
  
  /*
   * The instance of the server related to this ServerConsole.
   */
  EchoServer server;
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(String host, int port, EchoServer server) 
  {
	this.server = server;
    try 
    {
      serverClient= new ChatClient(host, port, this);
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
        		System.out.println("Terminating ServerConsole.");
        		return;
        	} 
        	//Parsing logoff command
        	else if(command.equals("#stop")) {
        		server.stopListening();
        	} 
        	//Parsing sethost command (must be logged off)
        	else if(command.equals("#close")) {
        		server.close();
        	}
        	//Parsing setport command (must be logged off)
        	else if(command.startsWith("#setport")) {
        		if(server.isClosed()) {
        			String[] parts = command.split(" ", 2);
                    if (parts.length == 2) {
                        server.setPort(Integer.parseInt(parts[1]));
                        System.out.println("Port set to: " + parts[1]);
                    } else {
                        System.out.println("Usage: #setport <port>");
                    }
        		} else {
        			System.out.println("Invalid command- Already logged in.");
        		}
        	}
        	//Parsing login command
        	else if(command.equals("#start")) {
        		if(!server.isListening()) {
        			server.listen();
        		}
        	} 
        	//Parsing getPort command
        	else if(command.equals("#getport")) {
        		System.out.println("Current port number is: " + String.valueOf(server.getPort()));
        	} else {
        		System.out.println("Invalid command.");
        	}
        } else {
        	serverClient.handleMessageFromClientUI(message);
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
}
//End of ServerChat class
