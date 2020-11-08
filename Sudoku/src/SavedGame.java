import java.io.*;

class SavedGame implements Serializable {
    private static final long serialVersionUID = 1L;
    transient SudokuGame game;

    public SavedGame(SudokuGame inputGame) {
        game = inputGame;
    }

    public void save() {
        SavedGame gameSave = new SavedGame(game);
        String filename = "savedgame.txt";
        try {
            FileOutputStream file = new FileOutputStream(filename); 
            ObjectOutputStream out = new ObjectOutputStream(file); 

            out.writeObject(gameSave); 

            out.close(); 
            file.close(); 

            System.out.println("Object has been serialized\n" + "Data before Deserialization.");
        }

        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
        } 
    }

    public void load() {
        SavedGame gameSave;
        String filename = "savedgame.txt";
        try { 
            // Reading the object from a file 
            FileInputStream file = new FileInputStream (filename); 
            ObjectInputStream in = new ObjectInputStream(file); 
  
            // Method for deserialization of object 
            gameSave = (SavedGame)in.readObject();
  
            in.close(); 
            file.close();

            game = gameSave.game;

            System.out.println("Object has been deserialized\n" + "Data after Deserialization.");
        }

        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
        }
  
        catch (ClassNotFoundException ex) { 
            System.out.println("ClassNotFoundException is caught"); 
        }
    }
}    
