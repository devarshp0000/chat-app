package clientfx1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class Clientfx1 extends Application {
    Pane root;
    Text t;
    TextField tf;
    Button send;
    
    Socket s;
    
    DataInputStream din;
    DataOutputStream dout;
    
    
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        root = new Pane();
        
        t = new Text();
        
        tf = new TextField();
        tf.setLayoutX(5);
        tf.setLayoutY(10);
        
        send = new Button("SEND");
        send.setLayoutX(tf.getLayoutX()+160);
        send.setLayoutY(tf.getLayoutY());
        
        ScrollPane sp = new ScrollPane(t);
        sp.setMinSize(300, 400);
        sp.setMaxSize(300, 400);
        sp.setLayoutX(5);
        sp.setLayoutY(tf.getLayoutY()+50);
        
        
        s = new Socket("192.168.0.177",6969);
        
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());
        
        
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String s = tf.getText();
                    String msg = t.getText() + "\n" + "you : " + s + "\n";
                    t.setText(msg);
                    dout.writeUTF(s);
                    
                    dout.flush();
                    tf.setText(null);
                } catch (IOException ex) {
                    System.err.println(ex);;
                }
            }
        });
        
        
        root.getChildren().addAll(sp,tf,send);
        Scene scene = new Scene(root, 300, 500);
        
        primaryStage.setTitle("Devarsh's Chat Room");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        connectServer();
    }
    void connectServer() throws IOException{
        
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        if(din.available()!=0){
                            String S = din.readUTF();
                            String msg = t.getText() + "\n" + "Client2 :" + S + "\n";
                            t.setText(msg);
                        }   
                    } catch (IOException ex) {
                        Logger.getLogger(Clientfx1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        }).start();
            
            
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}