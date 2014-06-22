
package com.cdyne.ws;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.ws.handler.HandlerResolver;

/*****************************************************************************************************************
** File       : Client.java 
** Desc       : Displays a form that enables the user to enter text. 
*               User can select a word from the text and click on “Get suggestion”
*               Button which would send a request to the web server (using SOAP and XML) requesting 
*               spelling corrections. Web server provides response which consists of set of spelling 
*               correction suggestion for the selected word.
*               
** Author     : Sumanth Lakshminarayana
**Student ID  : 1000830230
** Date       : 10/24/12   
******************************************************************************************************************
**                              Change History                                                
******************************************************************************************************************
**  Version:        Date:			    Author:		               Description:
**  --------        --------            ---------             ----------------------------------
**    0.0           11/24/12       Sumanth Lakshminarayana           Created.
*****************************************************************************************************************/


public class Client implements WindowListener 
{
	JTextArea userInputArea ; /*userInputArea multiline area that displays plain text that enables the user to enter text*/
	private JList jList; /*A component that displays a list of objects and allows the user to select one or more items */
	JFrame userInputWindow; /*Component used to create client window*/
	JLabel userInputAreaLabel,synonymAreaLabel; /*Label to the windows*/
	JScrollPane scrollUserInputArea,scrollSynonymArea;/* Used to converts a browser's default scrollbars*/
	int start,end,length; /*Used to select a word for spell check*/
	String totalInput,selectedInput;/*Used to store information about the total text and selected for spell check*/
	static JTextArea SOAPMonitorWindow = new JTextArea(40, 60);

	/* Method to create client GUI and read user input.
	 * Sets the size, window label, scroll choice
	 */
	synchronized void createClient()
	{
		try
		{
			userInputWindow = new JFrame("User GUI");
			JButton sendBox = new JButton("Get suggestions"); /*Button on click appends the selected word to the request*/
			userInputWindow.setSize(676,730);/*Sets the size of user window*/
			userInputWindow.getContentPane().setLayout(null);
			userInputWindow.getContentPane().setBackground(new Color(205,202,191));
			userInputWindow.getContentPane().setForeground(Color.white);
			userInputWindow.setResizable(false);
			userInputWindow.setLocationRelativeTo(null);

			ImageIcon applicationLogo = new ImageIcon("appLogo.jpg");//Image displayed in user window
			JLabel appLogoLabel = new JLabel(applicationLogo);  
			userInputWindow.getContentPane().add(appLogoLabel);

			userInputAreaLabel = new JLabel("Enter text");// Setting label to "Enter text"
			userInputWindow.getContentPane().add(userInputAreaLabel);
			userInputArea = new JTextArea(30,30);
			scrollUserInputArea = new JScrollPane(userInputArea);// Defining the scroll for user text area
			scrollUserInputArea.setBounds(50, 350, 600, 100);
			userInputWindow.add(scrollUserInputArea);
			userInputAreaLabel.setForeground(Color.blue); // Setting the color for the label font

			jList=new JList(); // Lists the suggestion

			synonymAreaLabel = new JLabel("Following are suggestions for selected text"); // Setting label to "Following are suggestions for selected text"
			userInputWindow.getContentPane().add(synonymAreaLabel);
			scrollSynonymArea = new JScrollPane(jList);
			scrollSynonymArea.setBounds(50, 550, 600, 100);// Defining the size
			userInputWindow.add(scrollSynonymArea);
			synonymAreaLabel.setForeground(Color.blue);// Setting the color for the label font

			userInputWindow.getContentPane().add(sendBox);

			// Sets the bounds of the control to the specified location and size.
			appLogoLabel.setBounds(10,0,650,290);
			sendBox.setBounds(250,470,165,34);
			userInputAreaLabel.setBounds(50,310,500,50);
			synonymAreaLabel.setBounds(50,510,500,50);

			sendBox.addActionListener(new ActionListener()
			{
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 * Method used to send only the selected word for spell check
				 */
				public void actionPerformed(ActionEvent e)
				{
					selectedInput=userInputArea.getSelectedText();
					start=userInputArea.getSelectionStart();// marks the start of the selection
					end=userInputArea.getSelectionEnd();// marks the end of the selection
					length=userInputArea.getText().length();// stores the length of the selected word
					totalInput=userInputArea.getText();// stores the total text entered
					try {
						executeClient();
					} catch (ConnectException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			userInputWindow.setVisible(true);
			userInputWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			userInputWindow.addWindowListener(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(userInputWindow,"Unable to Connect server, please try again later","ERROR",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/*
	 * Method creates client and used to list the suggestion
	 */
	private synchronized void executeClient() throws IOException,ConnectException
	{
		try
		{
			List<String> suggestionsList = getSuggestionsFromWS(selectedInput);

			// If the suggestion is empty display a pop up message "No suggestion found for the selected word!"
			if(suggestionsList.isEmpty())
			{
				Object[] o=new Object[0]; 

				try
				{
					JOptionPane.showMessageDialog(userInputWindow,"No suggestion found for the selected word!","Result",JOptionPane.ERROR_MESSAGE);
					jList.setListData(o);
				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			/*
			 * If suggestions are found then modify  data to user readable format and attach it to jList
			 * so that user can select suggested word for selected text.
			 */
			else
			{
				StringBuilder sb=new StringBuilder();
				for(String suggestion : suggestionsList)
					sb.append(suggestion+",");
						String[] syn=sb.toString().split(",");
						jList.setListData(syn);

						jList.addListSelectionListener(new ListSelectionListener()
						{ 
							public void valueChanged(ListSelectionEvent e)
							{
								JList list = (JList) e.getSource(); 

								String selectedValue = (String) list.getSelectedValue(); 

								try
								{
									String before=totalInput.substring(0,start);
									String after="";
									after=totalInput.substring(end,length);
									String finalResult;
									if(selectedValue==null  || selectedValue.equalsIgnoreCase("null"))
										finalResult=totalInput;
									else
										finalResult=before+selectedValue+after;
									userInputArea.setText(finalResult);
								}
								catch (Exception e1)
								{
									e1.printStackTrace();
								}

							} 
						});
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(userInputWindow,"Unable to Connect server, please try again later","ERROR",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/*
	 * Closes the client window on click of "X"
	 */
	public void windowClosing(WindowEvent e)
	{
		userInputWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	
	/*
	 * Method that uses auto generated stubs to list the spelling 
	 * suggestions from the web server using SOAP and XML 
	 */
	
	List<String> getSuggestionsFromWS(String userInput)
	{
		Check checkSuggestions = new Check();
		HandlerResolver myHandlerResolver = new MyHandlerResolver(); //To print XML SOAP request/response for logging.
		checkSuggestions.setHandlerResolver(myHandlerResolver); 
		CheckSoap checkSuggestionsSoap = checkSuggestions.getCheckSoap();
		DocumentSummary docSummary = checkSuggestionsSoap.checkTextBodyV2(userInput);
		List<String> suggestions=new ArrayList<String>();
		for (Words words : docSummary.getMisspelledWord())
			suggestions=words.getSuggestions();
		return suggestions;
	}
	
	/* Method that shows the SOAP request and response in XML format.*/
	void SOAPMonitor()
	{
		
		JScrollPane sp = new JScrollPane(SOAPMonitorWindow); 
		JFrame window = new JFrame("SOAP monitor");
		SOAPMonitorWindow.setSize(40, 60);
		SOAPMonitorWindow.setEditable(false);
		window.getContentPane().add(sp, "West");
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	public static void main(String[] args)
	{
		Client obj=new Client();
		obj.SOAPMonitor();
		obj.createClient();
	}
}