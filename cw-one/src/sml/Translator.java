package sml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
public class Translator {

    private static final String PATH = "C:/Users/Gareth/git/SDPSubmissions/cw-one/src/";
    // word + line is the part of the current line that's not yet processed
    // word has no whitespace
    // If word and line are not empty, line begins with whitespace
    private String line = "";
    private Labels labels; // The labels of the program being translated
    private ArrayList<Instruction> program; // The program to be created
    private String fileName; // source file of SML code

    public Translator(String fileName) {
        this.fileName = PATH + fileName;
    }

    // translate the small program in the file into lab (the labels) and
    // prog (the program)
    // return "no errors were detected"
    public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

        try (Scanner sc = new Scanner(new File(fileName))) {
            // Scanner attached to the file chosen by the user
            labels = lab;
            labels.reset();
            program = prog;
            program.clear();

            try {
                line = sc.nextLine();
            } catch (NoSuchElementException ioE) {
                return false;
            }

            // Each iteration processes line and reads the next line into line
            while (line != null) {
                // Store the label in label
                String label = scan();

                if (label.length() > 0) {
                    Instruction ins = getInstruction(label);
                    if (ins != null) {
                        labels.addLabel(label);
                        program.add(ins);
                    }
                }

                try {
                    line = sc.nextLine();
                } catch (NoSuchElementException ioE) {
                    return false;
                }
            }
        } catch (IOException ioE) {
            System.out.println("File: IO error " + ioE.getMessage());
            System.exit(-1);
            return false;
        }
        return true;
    }

    // line should consist of an SML instruction, with its label already
    // removed. Translate line into an instruction with label label
    // and return the instruction
    public Instruction getInstruction(String label) {
        int s1; // Possible operands of the instruction
        int s2;
        int r;
        int x;
        //Label for a bnz instruction.
        String l2;
        
        //Properties object for determining correct instruction.
        Properties properties = new Properties();
        
        //String for holding instruction's class name.
        String instructionClass;
        
        //Constructor for instruction class.
        Constructor constructor;
        
        Class cls;
        
        try {
			properties.load(new FileInputStream("C:/Users/Gareth/git/SDPSubmissions/cw-one/src/sml/instruction.properties"));
		

        if (line.equals(""))
            return null;

        String ins = scan();
        switch (ins) {
            case "add":
                r = scanInt();
                s1 = scanInt();
                s2 = scanInt();
                instructionClass = properties.getProperty("add.class");
                cls = Class.forName(instructionClass); 
                constructor = cls.getConstructor(new Class[] {String.class, int.class, int.class, int.class});
                return (Instruction) constructor.newInstance(new Object[] {label, r, s1, s2});
            case "lin":
                r = scanInt();
                s1 = scanInt();
                instructionClass = properties.getProperty("lin.class");
                cls = Class.forName(instructionClass); 
                constructor = cls.getConstructor(new Class[] {String.class, int.class, int.class});
                return (Instruction) constructor.newInstance(new Object[] {label, r, s1});
            case "sub":
                r = scanInt();
                s1 = scanInt();
                s2 = scanInt();
                instructionClass = properties.getProperty("sub.class");
                cls = Class.forName(instructionClass); 
                constructor = cls.getConstructor(new Class[] {String.class, int.class, int.class, int.class});
                return (Instruction) constructor.newInstance(new Object[] {label, r, s1, s2});
            case "mul":
                r = scanInt();
                s1 = scanInt();
                s2 = scanInt();
                instructionClass = properties.getProperty("mul.class");
                cls = Class.forName(instructionClass); 
                constructor = cls.getConstructor(new Class[] {String.class, int.class, int.class, int.class});
                return (Instruction) constructor.newInstance(new Object[] {label, r, s1, s2});
            case "div":
                r = scanInt();
                s1 = scanInt();
                s2 = scanInt();
                instructionClass = properties.getProperty("div.class");
                cls = Class.forName(instructionClass); 
                constructor = cls.getConstructor(new Class[] {String.class, int.class, int.class, int.class});
                return (Instruction) constructor.newInstance(new Object[] {label, r, s1, s2});
            case "out":
                r = scanInt();
                instructionClass = properties.getProperty("out.class");
                cls = Class.forName(instructionClass); 
                constructor = cls.getConstructor(new Class[] {String.class, int.class});
                return (Instruction) constructor.newInstance(new Object[] {label, r});
            case "bnz":
                r = scanInt();
                l2 = scan();
                instructionClass = properties.getProperty("bnz.class");
                cls = Class.forName(instructionClass); 
                constructor = cls.getConstructor(new Class[] {String.class, int.class, String.class});
                return (Instruction) constructor.newInstance(new Object[] {label, r, l2});
        }

        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {  
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
        
        return null;
    }

    /*
     * Return the first word of line and remove it from line. If there is no
     * word, return ""
     */
    private String scan() {
        line = line.trim();
        if (line.length() == 0)
            return "";

        int i = 0;
        while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
            i = i + 1;
        }
        String word = line.substring(0, i);
        line = line.substring(i);
        return word;
    }

    // Return the first word of line as an integer. If there is
    // any error, return the maximum int
    private int scanInt() {
        String word = scan();
        if (word.length() == 0) {
            return Integer.MAX_VALUE;
        }

        try {
            return Integer.parseInt(word);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
}