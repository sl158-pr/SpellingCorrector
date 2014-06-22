Aim of the Project:

Check the spelling of some text from an existing web server using XML and SOAP, client application will connect to an existing web server that is exposing a service to a client that can be accessed with HTTP, possibly by a client side script in a web browser. Client process will connect to the server and request services from an object on the server that will do the spelling check.

Run client program.
(/SpellingCorrector/src/com/cdyne/ws/Client.java)

Client Process: Displays a form that enables the user to enter text. User can select a word from the text and click on “Get suggestion” Button which would send a request to the web server (using SOAP and XML) requesting spelling corrections. Web server provides response which consists of set of spelling correction suggestion for the selected word.
Running the client also displays another window that shows the SOAP request and response in XML format.

appLogo.jpg is an Image used in user interface for client 

Extra Credit Implementations  

a)	Process the suggestions returned by the server so the user can correct the text.

References:
1.	http://wsf.cdyne.com/SpellChecker/check.asmx?op=CheckTextBodyV2
2.	http://www.coderanch.com
3.	http://stackoverflow.com/
