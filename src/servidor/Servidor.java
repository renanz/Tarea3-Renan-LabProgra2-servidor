/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * @author Renan
 */
import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Scanner;
public class Servidor{
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Servidor(){}
    void run()
    {
        try{
            //1. creating a server socket
            providerSocket = new ServerSocket(2004, 10);
            //2. Wait for connection
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful\n\nFavor elegir entre las siguientes preguntas.\n\t1. Cual es tu nombre?\n\t2. Cuales son tus hobbies?"
                    + "\n\t3. Cual es tu horoscopo\n\t4. Que hora es?\n\t5.Cuantas clases llevas??");
            
            
            //4. The two parts communicate via the input and output streams
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("client>" + message);
                    System.out.print("server> ");
                    switch (message){
                        case "1" :
                            sendMessage("Hola mi nombre es renan");
                            break;
                        case "2":
                            pickHobbie();
                            break;
                        case "3":
                            sendMessage("Mi horoscopo es Capricornio");
                            break;
                        case "4":
                            Calendar ahorita = Calendar.getInstance();
                            sendMessage(ahorita.getTime().toString());
                            break;
                        case "5":
                            sendMessage("Llevo 4 clases y un laboratorio.\n\t1.Algebra Lineal\n\t2.Calculo I Diferencial\n\t3.Historia de Honduras\n\t4.Programacion 2"
                                    + "\n\tLab de Programacion 2");
                            break;
                                
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(!message.equals("bye"));
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    public static void main(String args[])
    {
        Servidor server = new Servidor();
        while(true){
            server.run();
        }
    }
    public void pickHobbie(){
        int randomNum = 1 + (int)(Math.random() * 4); 
        switch (randomNum){
            case 1:
                sendMessage("Mi hobbie favorita es Jugar futbol.");
                break;
            case 2:
                sendMessage("Mi hobbie favorita es Programar en Java.");
                break;
            case 3:
                sendMessage("Mi hobbie favorita es Jugar videojuegos.");
                break;
            case 4:
                sendMessage("Mi hobbie favorita es pasar tiempo con amigos.");
                break;
        }
    }
}
