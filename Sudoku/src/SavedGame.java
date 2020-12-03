import java.io.*;

class SavedGame implements Serializable {
    private static final long serialVersionUID = 1L;
    //transient SudokuGame game;

    public SavedGame() {
        //SudokuGame savedGame = inputGame;
    }

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
