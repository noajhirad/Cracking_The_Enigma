
**The Enigma Machine**

I created this project collaborating with [Noa Jhirad](https://github.com/noajhirad) as part of a Java-based software development course we participated in while pursuing our BSc. in Computer Science at the Academic College of Tel Aviv-Yafo.

In a superficial description, we implemented a generic Enigma Machine used in a client-server architecture (HTTP-based). The project includes three types of clients (UBoat, Allie, and Agent), developed as three independent desktop applications (based on Java FX) and event-oriented methodology.

**Background**

During World War II the German troop used the enigma machine to decrypt and send messages between the military headquarters and submarines, and to coordinate attack on the Allies of World War II.
The Enigma Machine is an electro-mechanic machine that enables encrypting text messages. Using the machine, one can encrypt or decrypt text. This functionality allowed the German submarines to send messages, coordinate their attacks and receive orders down the command chain on the dry land, without discovering their plans to the Allies.
After great efforts by the Allies, they eventually managed to crack the Enigma (thanks to Ellen Turing) and were able to discover the German plans and mislead them. This project simulates the different phases of working with the Enigma machine and the effort of the Allies of World War II to crack it.

**The Project**

The game starts with the **UBoat**, the first desktop client, that initiates a competition. To do so, the UBoat uploads an XML file that describes a machine, and includes details such as the machine's alphabet, a dictionary which the words to be encrypted can be taken from, the difficulty of the game (which you can read about later on) and mechanical details of the machine. You can see an example of an XML file in the /example-files folder. At the end of this document, you can also see the details about the machine Architecture, which plays a big part in the competition later.

<img width="1440" alt="Screenshot 2023-04-20 at 19 08 30" src="https://user-images.githubusercontent.com/76840545/233443410-9ff5bf69-c9b3-4dbd-8622-ca009824144b.png">

<img width="1440" alt="Screenshot 2023-04-20 at 19 08 56" src="https://user-images.githubusercontent.com/76840545/233443418-ec908702-018e-4c50-9f1d-888e1cc5652c.png">


Afterward, the UBoat initiates the task. He specifies how many Allies can enroll in the competition, configures the enigma machine, and encrypt a message. The UBoat can only encrypt words that appear in the dictionary (which he can see in the decryption tab). He can write it himself or click on the dictionary to choose the desired word. Finally, after enough teams enrolled in the competition, he spread his encrypted message and let the competition begin - the winner is the first team to decrypt it. 

<img width="1440" alt="Screenshot 2023-04-20 at 19 09 16" src="https://user-images.githubusercontent.com/76840545/233443347-96ff2cab-1fe2-4827-8df9-b4391a5fe80e.png">
<img width="1440" alt="Screenshot 2023-04-20 at 19 14 15" src="https://user-images.githubusercontent.com/76840545/233443355-fa64227f-dce8-4354-a7d3-f3da5e986521.png">

The second HTTP client is the **Allie**, a team leader in the competition. The Allie has several agents working for him trying to solve the UBoat task. His job is to spread the tasks to the Agents, gather the decryption candidates from them, and later report back to the Uboat. The Allie waits until enough Agents register to work with him, and after he is satisfied with the number of Agents enrolled, he can mark his team as ready - which indicates the server that the competition can start.

<img width="1440" alt="Screenshot 2023-04-20 at 19 14 23" src="https://user-images.githubusercontent.com/76840545/233443502-8f9badb0-7eb2-4ef0-bdaf-eac108e122ba.png">
<img width="1440" alt="Screenshot 2023-04-20 at 19 13 50" src="https://user-images.githubusercontent.com/76840545/233443510-d75b89e5-2fd9-41e1-becc-cd0304efcdb9.png">
<img width="1440" alt="Screenshot 2023-04-20 at 19 13 08" src="https://user-images.githubusercontent.com/76840545/233443514-7484f5f8-2b64-4922-a214-e843ef51101b.png">

The last client is the **Agent**, that works for Allie. First, while enrolling, he chooses an Allie to work with, specifies how many threads he'll use to decrypt the message, and how many strings he'll fetch each time from the Allie. Then, he receives a bunch of machine configurations and must run the given message and start to decrypt it. For every decrypted word: he checks if it’s in the dictionary specified. If the entire message is in the dictionary - a candidate is found, and the agent reports back to Allie. The agents go over the machine configurations in “brute force”.

<img width="1440" alt="Screenshot 2023-04-20 at 19 14 01" src="https://user-images.githubusercontent.com/76840545/233443610-802745a4-8842-4734-8e36-61590ee1f834.png">

Considering the Server-side of the app, we designed an Apache-tomcat server. The server held the Enigma Machine, and all of its logic and was in charge of synchronizing the user’s data. Our logic includes vast multithreading aspects (such as thread pool manipulation, internal blocking queue management, and thread synchronization). These aspects are significant for synchronizing the competition status, receiving all the encryption candidates, and deciding on a winner. Furthermore, managing the tasks taken by different agents under the same Allie and the chat used by all game players also had to be perfectly synchronized to allow the competition to run smoothly. The project included integration and working with 3rd parties like GSON for Json handling and OKHttp for the client app.

**The Enigma architecture:**

First, If you are unfamiliar with the Enigma Machine, I would highly recommend watching [this video](https://www.youtube.com/watch?v=ybkkiGtJmkM&ab_channel=JaredOwen) which explains in detail the mechanic electronic structure of the machine.

The Enigma machine looks like an old-school writing machine, with an extra “keyboard”; where is a light bulb for each character. In the decrypting process, the user presses a char on the keyboard, and the light of the encoded char flashed. The user writes down the char whose bulb flashed, and moves on to the next char, until the end of the message.
The user (on the second end) repeats the same process, with the encrypted text, and gets the decrypted text (original text).

An important feature of the machine is that having it physically isn’t enough. To decrypt a message, one must know the secret and unique code with which it was encrypted to be able decrypt it. The Enigma machine is assembled from numerous mechanical and electronic parts. At its core, there are several conversion wheels called Rotors. The Rotors have X inputs and X outputs (typically 26), and they convert entrance X to exit Y, one-way conversion X->Y and Y->X.

The machine is built with several Rotors, one after another (typically 3) where the first output connected to the following input. The machine has several Rotors the user can choose from and place in it. The user can choose which Rotors he would like to use, decide their orders, and the starting position for each Rotor.

The machine has another element called Reflector. The Reflector is in the machine right after the last Rotor, and its job is to receive an electrical current and return it in a different exit. The Reflector reverse the current and repeats the process in the opposite direction. The machine comes with a set of rotors, and the user needs to choose the one he is going to use.

The Enigma has also a plugs board that isn’t in use in this project (since it will become combinatorially complicated to run on a standard computer).

After this explanation, the machine configuration code is consisted of:
1. The Rotor’s numbers that are used.
1. The Rotors order.
1. The number of Reflector that in used.
1. The plugboard (like mentioned before isn’t in use in this version of our project).

**The difficulties of the competition:**

There are three levels of difficulty for the game: 

\*  Easy: The Rotors in use, their location, and the reflector are all known; the decryption process only includes finding their order.

\*  Intermitted: The Rotors in used and their location are known; the decryption process includes finding their order and the reflector number.

\*  Hard: The Rotors used are known; everything else needs to be discovered.

\*  Impossible: none of the configuration code parts are known; everything needs to be discovered.

**How to run this project**

First, to open the game you’ll need to have Apache-tomcat installed.
You’ll need to deploy the webserver .war to Tomcat; you can find it in the directory Jars/Server\_Web.
After you launched the server, you can run each one of the desktop applications, using the corresponding .bat file that can be found in Jars/{desktopAppName}\_jar.
For example, to run the Uboat you should run Uboat.bat which can be found in Jars/Uboat\_jar.

**What can be improved:**

Due to time limitations, we didn’t have sufficient time to refactor our code as we wished. There are a few duplicated code lines that we think could have been better arranged to make our code more readable.

