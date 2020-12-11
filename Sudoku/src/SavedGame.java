import java.io.*;
/*****************************************************************
* Class that controls the saving and loading of a Sudoku game.
* @author Ethan Tran, Matthew Davis, and Cole Hyink
* @version 2020.12.10
*****************************************************************/
class SavedGame implements Serializable {
    /* Serial ID for Loading and Saving */
    private static final long serialVersionUID = 1L;
    //transient SudokuGame game;

/*****************************************************************
* Construtor for SaveGame
*****************************************************************/
    public SavedGame() {
        //SudokuGame savedGame = inputGame;
    }

/*****************************************************************
* Saves the game with the given filename.
*
* @param filename desired name to save file to
* @param o Sudoku game board
*****************************************************************/
    public void save(String filename, Object o) {
        try {
            FileOutputStream file = new FileOutputStream(filename); 
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(o);
            out.close();
            //System.out.println("Object has been serialized\n" + "Data before Deserialization.");
        }

        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
        } 
    }

/*****************************************************************
* Loads a Sudoku game from the given filename.
*
* @param filename name of file to load from
*****************************************************************/
    public Object load(String filename) {
        Object LoadedGame = null;
        try { 
            // Reading the object from a file 
            FileInputStream file = new FileInputStream (filename); 
            ObjectInputStream in = new ObjectInputStream(file); 
  
            // Method for deserialization of object, cast to game type
            LoadedGame = in.readObject();
  
            in.close();
            //System.out.println("Object has been deserialized\n" + "Data after Deserialization.");
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
