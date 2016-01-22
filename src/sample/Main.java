package sample;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.PublicKey;


public class Main extends Application {
    public static String text;

    private static InetAddress IP;
    private static Socket server;
    private static DefaultListModel listModel;

    private static ServerSocket serverSocket;
    private Label lIP;
    private Label lPort;
    public static TextField tfIP;
    public static TextField tfPort;
    public static Button connect;
    private Rectangle rectangle;
    public static Button send;
    public TextField tfTextInput;
    private static int width;
    private static int height;
    private static Pane root;

    public ListView list;
    private Stage primaryStage;
    private static Socket client;
    private static int flag;
    private static String serverName;
    private static int port;
    private static int portFlag;
    private static int serverNameFlag;
    @Override
    public void start(Stage primaryStage) {
        try {
            Pane root = new Pane();

            Scene scene = new Scene(root,450,650);




            width = 400;
            height = 600;

            Button addNewItem = new Button("Add new item");
            addNewItem.relocate(5, 500);

            lIP = new Label("IP : ");
            lIP.relocate(width-300, height-550);

            lPort = new Label("Port : ");
            lPort.relocate(width-300, height-510);

            tfIP = new TextField();

            tfIP.relocate( width/2-50, height-550);
            tfIP.textProperty().addListener((observable, oldValue, newValue) -> {
                serverName = tfIP.getText();
                System.out.println(serverName+" TF");
                serverNameFlag = 1;
            });

            tfPort = new TextField();
            tfPort.relocate(width/2-50, height-510);
            tfPort.textProperty().addListener((observable, oldValue, newValue) -> {
                String text = tfPort.getText();
                port = Integer.parseInt(text);
                portFlag = 1;
            });

            connect = new Button("Connect");
            connect.relocate(width/2, height-470);

            rectangle = new Rectangle(25,25);
            rectangle.relocate(width/2-30, height-470);
            rectangle.setFill(Color.GREEN);
            rectangle.setStrokeWidth(10);

            list = new ListView();
            list.relocate(100, height-430);

            send = new Button("Send");
            send.relocate(width/2-100,600);



            connect.setOnAction(new EventHandler<ActionEvent>() {

                                    @Override
                                    public void handle(ActionEvent event) {

                                        int port = 2020;

                                        try {
                                            Thread t = new GreetingServer(port);
                                            t.start();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                    }

//					launch(args);





                                    public void sendMessageToServer(String message) {

                                        try {

                                            OutputStream outToServer = client.getOutputStream();

                                            DataOutputStream out = new DataOutputStream(outToServer);

                                            out.writeUTF("Hello from " + client.getLocalSocketAddress() + "! My message is : " + message);

                                        } catch (Exception e) {

                                        }

                                    }

                                }
            );

            tfTextInput = new TextField();
            tfTextInput.relocate(width/2-50, 600);


            send.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    sendMessageToClient(tfTextInput.getText());
                    list.getItems().add("Me: " + tfTextInput.getText());

                }
            });


            root.getChildren().addAll(addNewItem,lIP,lPort,tfIP,tfPort,connect,rectangle,list,tfTextInput,send);

//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public  class GreetingServer extends Thread {
        public String text;
        private ServerSocket serverSocket;
        private InetAddress IP;
        private Socket server;
        private DefaultListModel listModel;
        private ListView list;

        public  GreetingServer(int port) throws IOException, java.lang.IllegalArgumentException {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000000);
        }

        public void run() {
            while (true) {
                try {

                    IP = InetAddress.getLocalHost();
                    System.out.println(IP);
                    System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                    server = serverSocket.accept();
                    System.out.println("Just connected to " + server.getRemoteSocketAddress());
                    while (true) {
                        DataInputStream in = new DataInputStream(server.getInputStream());
                        boolean isConnected = true;
                        while (isConnected) {


                            String messageFromClient = null;
                            try {
                                messageFromClient= in.readUTF();
                            } catch (Exception e) {
                                isConnected = false;
                                messageFromClient = "";
                            }

                            System.out.println("Client says : " + messageFromClient);
                            //			l.setText("Server says : " + messageFromServer);

                            list.getItems().add("Server: " + messageFromClient);


                        }
                        list.getItems().add("Client: " + in.readUTF());
                        System.out.println(in.readUTF());

                    }
                    // out.writeUTF("Thank you for connecting to "
                    // + server.getLocalSocketAddress() + "\nGoodbye!");?]

                } catch (SocketTimeoutException s) {
                    try {
                        server.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("Socket timed out!");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {

        launch(args);



    }

    public static void sendMessageToClient(String message) {

        try {
            OutputStream outToServer = server.getOutputStream();

            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(message);

        } catch (Exception e) {

        }

    }





}