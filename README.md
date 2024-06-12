# Course work

This is the client-server chat application for online communication with bots (now available only 1).

## Navigation
* [How to start](https://github.com/furaizi/coursework-programming-fundamentals#how-to-start)  
* [Start the server](https://github.com/furaizi/coursework-programming-fundamentals#start-the-server)  
* [Start the console client](https://github.com/furaizi/coursework-programming-fundamentals#start-the-console-client)  
* [Start the GUI client](https://github.com/furaizi/coursework-programming-fundamentals#start-the-gui-client)  
* [How to use the application](https://github.com/furaizi/coursework-programming-fundamentals#how-to-use-the-application)  
  * [For console clients](https://github.com/furaizi/coursework-programming-fundamentals#for-console-clients)  
  * [For GUI clients](https://github.com/furaizi/coursework-programming-fundamentals#for-gui-clients)  

## How to start

In order to start the application, you need have installed:
* JDK 21 or above
* Maven
  
  
Clone repo

```
git clone https://github.com/furaizi/coursework-programming-fundamentals.git
```
 
Go to installed directory
```
cd coursework-programming-fundamentals
```

Compile the project
```
mvn compile
```

Start the server
```
mvn exec:java -P run-server
```

Run the client:
* Console
```
mvn exec:java -P run-console-client
```
* GUI
```
mvn exec:java -P run-gui-client
```


## Start the server

Just enter any port up to 65535 and server is ready.

## Start the console client

To start the client, you need to start the server. If you haven't done it, come back here only after setting up the server.

![image](https://github.com/furaizi/coursework-programming-fundamentals/assets/107194668/dd5ffe91-cdbf-4d43-8a0e-ae3f689e3014)

* For 'server address' you need to enter IPv4 address of the server. If you have launched the server on your local machine, just enter 'localhost'.  
* For 'server port' you need to enter the port of the server. You can get it from the server administrator.  
* For 'username' you can choose any nickname you want. It only shouldn't contain spaces. 

## Start the GUI client

To start the client, you need to start the server. If you haven't done it, come back here only after setting up the server.  

* For 'server address' you need to enter IPv4 address of the server. If you have launched the server on your local machine, just enter 'localhost'.  
![Screenshot 2024-06-12 195636](https://github.com/furaizi/coursework-programming-fundamentals/assets/107194668/4b290884-9bb8-4b4c-b5cf-4c2123ed7f73)
  
* For 'server port' you need to enter the port of the server. You can get it from the server administrator.
![image](https://github.com/furaizi/coursework-programming-fundamentals/assets/107194668/2efa5cb4-65ee-44f4-bcf5-ca105a5d42f7)
  
* For 'username' you can choose any nickname you want. It only shouldn't contain spaces.
![image](https://github.com/furaizi/coursework-programming-fundamentals/assets/107194668/4f0bef0b-bc54-4dac-a1a4-b60dca250c0b)  




## How to use the application
### For console clients:

* Send a global message:  
![image](https://github.com/furaizi/coursework-programming-fundamentals/assets/107194668/8da52292-425d-444d-9d2a-3a2d139f1021)

* Send a private message:  
![image](https://github.com/furaizi/coursework-programming-fundamentals/assets/107194668/bb9d1964-fbcf-4639-acad-01b094762b75)  


### For GUI clients:

* Send a global message:  
![image](https://github.com/furaizi/coursework-programming-fundamentals/assets/107194668/eb7db6e8-c224-4b37-9398-851fe2161a68)

* Send a private message:  
![image](https://github.com/furaizi/coursework-programming-fundamentals/assets/107194668/e9bb337f-fabb-4068-8299-b21cfbe5ed7e)

