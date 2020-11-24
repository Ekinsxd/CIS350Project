import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SavedGame implements Serializable {
    private static final long serialVersionUID = 1L;
    //transient SudokuGame game;

    public SavedGame() {
        //SudokuGame savedGame = inputGame;
    }

    public void save(String filename, Object o) {
        //SavedGame gameSave = new SavedGame(game);
        //String filename = "savedgame.txt";
        try {
            FileOutputStream file = new FileOutputStream(filename); 
            ObjectOutputStream out = new ObjectOutputStream(file); 

            out.writeObject(o);

            out.close();

            System.out.println("Object has been serialized\n" + "Data before Deserialization.");
        }

        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
        } 
    }

    public Object load(String filename) {
        //SavedGame gameSave;
        //String filename = "savedgame.txt";
        Object LoadedGame = null;
        try { 
            // Reading the object from a file 
            FileInputStream file = new FileInputStream (filename); 
            ObjectInputStream in = new ObjectInputStream(file); 
  
            // Method for deserialization of object, cast to game type
            LoadedGame = in.readObject();
  
            in.close();
            System.out.println("Object has been deserialized\n" + "Data after Deserialization.");
        }

        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
        }
  
        catch (ClassNotFoundException ex) { 
            System.out.println("ClassNotFoundException is caught"); 
        }
        return LoadedGame;
    }
}
