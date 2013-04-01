/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseaapp;

import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import myseaapp.Process;
import myseaapp.Simulator;

/**
 *
 * @author admin
 */
public class MySeaApp extends Application {

    //creating a simulator for the managing
    Simulator sm = new Simulator();
    int current_process_id=2;  // to keep track of the current runnning process
    
    
    // for running state
    Rectangle cpu = addCPU();
    Line pc = new Line(522, 305, 630, 305);
    ArrayList<Rectangle> ic_memory = addsm();
    ArrayList<Text> ic_instructions_text = addInstructionText();
    Text ic_cpu = new Text(423, 257, "Instruction ");
    Button ex_in = new Button("execute");
    static int instruction_memory_wrapper = 0;
    static int pc_val = 0;

    // for ready state or blocked state
    ArrayList<Circle> blocked_ready;
    
    
    //for blocked state on io
    
    
    //adding secondary stage for creating processes
    Label process_id_label;
    Label process_priority_label;
    Label Process_starting_address;
    Label Process_length_label;
    
    TextField process_id_text;
    TextField process_priority_text;
    TextField Process_starting_text;
    TextField Process_length_text; 
    
    Button submit = new Button("Submit");
    
    
    
    @Override
    public void start(Stage primaryStage) {

        //creating the processes
        Process p = new Process(2, 0, 0, 10);
        sm.addProcess(p);     
        p = new Process(1,4,2,30);
        
        Pane root = new Pane();
        populateInstructions();
        ex_in.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                execute_instrucition(sm.getProcess(current_process_id));
                pc_val++;
            }
        });

        //adding the children
        root.getChildren().addAll(cpu, pc, ic_cpu, ex_in);
        for (int i = 0; i < 8; i++) {
            root.getChildren().add(ic_memory.get(i));
            root.getChildren().add(ic_instructions_text.get(i));
        }



        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getSceneX() + "  " + event.getSceneY());
            }
        });
        Scene scene = new Scene(root, 1050, 650);
        //scene.setFill(null);
        primaryStage.setTitle("OS SIMULATOR!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        
        //creating secondary stage for creating the process
        process_id_label = new Label("process id :");
        process_priority_label = new Label("priority :");
        Process_length_label = new Label("length id :");
        Process_starting_address = new Label("program counter :");
        
        process_id_text = new TextField();
        process_priority_text = new TextField();
        Process_length_text  = new TextField();
        Process_starting_text = new TextField();
        submit = new Button("submit");
        
        final Stage secondaryStage = new Stage(StageStyle.UTILITY);
        GridPane root2 = new GridPane();
        root2.setVgap(5);
        root2.setHgap(5);
        root2.getChildren().addAll(process_id_label,process_priority_label,
                Process_length_label,Process_starting_address,process_id_text,
                process_priority_text,Process_length_text,Process_starting_text,submit);
        Scene scene2 = new Scene(root2,300,200);
        secondaryStage.setTitle("process creation"); 
       secondaryStage.setScene(scene2);
       secondaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Rectangle addCPU() {
        //adding the cpu to the scence
        Rectangle cpu = new Rectangle();
        cpu.setX(370);
        cpu.setY(200);
        cpu.setWidth(150);
        cpu.setHeight(220);
        cpu.setFill(Color.AQUA);

        return cpu;
    }

    private ArrayList<Rectangle> addsm() {
        //adding main memory
        ArrayList<Rectangle> ic_memory = new ArrayList<Rectangle>();
        Rectangle ic_cell;
        for (int i = 0; i < 8; i++) {
            ic_cell = new Rectangle(647, 185 + i * 32, 60, 32);
            ic_cell.setFill(Color.rgb((int) Math.random() * 255, (int) Math.random() * 255, (int) Math.random() * 255, 0.5));
            ic_cell.setStroke(Color.ALICEBLUE);
            ic_memory.add(ic_cell);
        }

        return ic_memory;

    }

    private void populateInstructions() {
        sm.instrucitons = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                sm.instrucitons.add("io");
            } else {
                sm.instrucitons.add("cpu");
            }
        }
    }

    private ArrayList<Text> addInstructionText() {
        ArrayList<Text> instructions = new ArrayList<Text>();
        Text in_instruction;
        for (int i = 0; i < 8; i++) {
            in_instruction = new Text(654, 199 + i * 32, "  ");
            instructions.add(in_instruction);
        }
        return instructions;
    }

    private void execute_instrucition(int curr_process_id) {
         int pc_val = sm.processesList.get(curr_process_id).getNextInstruction();
         if (pc_val == -1)
         {
             ic_cpu.setText("no more instructions to execute");
             return;
         }
        if (instruction_memory_wrapper == 8) {
           
            for (int i = pc_val, j = 0; j < 8; i++, j++) {
                ic_instructions_text.get(j).setText(sm.instrucitons.get(i));
            }

            instruction_memory_wrapper = 0;
        }
        ic_cpu.setText("process "+sm.processesList.get(curr_process_id).getId()+"   \n"+" Instruciton  " + pc_val + "  " + sm.instrucitons.get(pc_val));
        pc.setEndX(647);
        pc.setEndY(197 + instruction_memory_wrapper * 32);
        instruction_memory_wrapper++;
    }
}
