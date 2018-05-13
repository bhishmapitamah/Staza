# Staza
Get the location of your mobile phone just by sending a code using text message.

<h2>Instructions</h2>
<ol>
<li>Open Staza and log in with a chosen password.</li>
<li>After logging in give the required permissions to Staza.</li>
<li>Staza will generate a random code for you. You can change the code if you want.</li>
<li>Remember the code provided to you.</li>
<li>Send the code from any other phone to the phone through text message.</li>
<li>Staza will reply with the lattitude and longitude of the current location of the phone.</li>
<li>You can get the accurate location by searching for the lattitude and longitude in Google Maps.</li>
</ol>

<h2>Implementation Notes</h2>
Staza keeps listenening for messages in the backgound and when a message is received it compares the message with 
the saved code. If the message matches the saved code it forces the user to enable gps. After gps has been enabled, Staza 
retrives the current location and replies to the sender with the location of the phone.<br><br>
The app is not dependent on data usage and works on sms service.
