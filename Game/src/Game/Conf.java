package Game;

import java.io.*;
import java.util.ArrayList;

public class Conf {

    private String fileName;
    private String line;
    private ArrayList<String> lines;
    public float confVolume;
    public float confSound;
    public String confMoves;

    public Conf() {
        fileName = "conf.txt";
        line = null;
        lines = new ArrayList<String>();
    }

    public void buildConf() {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '"
                    + fileName + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                    + fileName + "'");
        }
        for (String str : lines) {
            if (str.contains("volume")) {
                confVolume = Float.valueOf(str.substring(8));
                //System.out.println(str.substring(8));
            }
            if (str.contains("sound")) {
                confSound = Float.valueOf(str.substring(7));
                //System.out.println(str.substring(7));
            }
            if (str.contains("moves")) {
                confMoves = str.substring(7);
                //System.out.println(confMoves);
            }
        }
    }

    public void setVolume(Float volume) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            lines = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();

            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();

            BufferedWriter fileOut = new BufferedWriter(new FileWriter(fileName));
            fileOut.write("REWRITE THIS FILE AT YOUR OWN RISK.");
            fileOut.newLine();
            fileOut.write("WE ARE NOT RESPONSIBLE FOR ANY GAME MALFUNCTIONS RESULTING FROM MANUAL CHANGES.");
            fileOut.newLine();
            fileOut.newLine();
            fileOut.write("volume: " + volume);
            fileOut.newLine();
            fileOut.write(lines.get(4));
            fileOut.newLine();
            fileOut.write(lines.get(5));
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    public void setSound(Float volume) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            lines = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();

            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();

            BufferedWriter fileOut = new BufferedWriter(new FileWriter(fileName));
            fileOut.write("REWRITE THIS FILE AT YOUR OWN RISK.");
            fileOut.newLine();
            fileOut.write("WE ARE NOT RESPONSIBLE FOR ANY GAME MALFUNCTIONS RESULTING FROM MANUAL CHANGES.");
            fileOut.newLine();
            fileOut.newLine();
            fileOut.write(lines.get(3));
            fileOut.newLine();
            fileOut.write("sound: " + volume);
            fileOut.newLine();
            fileOut.write(lines.get(5));
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    public void setControls(String moves) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            lines = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();

            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();

            BufferedWriter fileOut = new BufferedWriter(new FileWriter(fileName));
            fileOut.write("REWRITE THIS FILE AT YOUR OWN RISK.");
            fileOut.newLine();
            fileOut.write("WE ARE NOT RESPONSIBLE FOR ANY GAME MALFUNCTIONS RESULTING FROM MANUAL CHANGES.");
            fileOut.newLine();
            fileOut.newLine();
            fileOut.write(lines.get(3));
            fileOut.newLine();
            fileOut.write(lines.get(4));
            fileOut.newLine();
            fileOut.write("moves: " + moves);
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }
}
