package Comp;
import AST.*;
import java.io.*;

public class Comp {

    public static void main( String []args ) {

        File file;
        FileReader stream;
        int numChRead;
        Program program;
        if ( args.length < 1 ||  args.length > 2 )  {
            System.out.println("Usage:\n   Comp input [output]");
            System.out.println("input is the file to be compiled");
            System.out.println("output is the file where the generated code will be stored");
        }
        else {
           String inputFileName = args[0];
           String resultFileName = inputFileName;

           int lastIndex;
           if ( (lastIndex = inputFileName.lastIndexOf('.')) == -1 )
              lastIndex = inputFileName.length();
           inputFileName = inputFileName.substring(0, lastIndex);
           if ( (lastIndex = inputFileName.lastIndexOf('\\')) != -1 )
              inputFileName = inputFileName.substring(lastIndex + 1);
           String outputFileName;
           if ( args.length == 2 )
              outputFileName = args[1];
           else
              outputFileName = inputFileName + ".htm";
           resultFileName = inputFileName + ".txt";


           PrintWriter outError;
 	       FileOutputStream outputStreamError = null;
           try {
              outputStreamError = new FileOutputStream(resultFileName);
           } catch ( FileNotFoundException  e) {
              System.out.println("Could not create " + resultFileName);
              return ;
           }
           outError = new PrintWriter(outputStreamError);





           file = new File(args[0]);
           if ( ! file.exists() || ! file.canRead() ) {
             String msg = "Either the file " + args[0] + " does not exist or it cannot be read";
             System.out.println(msg);
             outError.println("-1 : " + msg);
             outError.close();
             return ;
           }
           try {
             stream = new FileReader(file);
            } catch ( FileNotFoundException e ) {
                String msg = "Something wrong: file does not exist anymore";
                System.out.println(msg);
                outError.println("-1 : " + msg);
                outError.close();
                throw new RuntimeException();
            }
                // one more character for '\0' at the end that will be added by the
                // compiler
            char []input = new char[ (int ) file.length() + 1 ];

            try {
              numChRead = stream.read( input, 0, (int ) file.length() );
            } catch ( IOException e ) {
                String msg = "Error reading file " + args[0];
                System.out.println(msg);
                outError.println("-1 : " + msg);
                outError.close();
                return ;
            }

            if ( numChRead != file.length() ) {
                System.out.println("Read error");
                outError.println("-1 : Read error");
                outError.close();
                return ;
            }
            try {
              stream.close();
            } catch ( IOException e ) {
                outError.println("-1 : Error closing file");
                outError.close();
                System.out.println("Error in handling the file " + args[0]);
                return ;
            }

            Compiler compiler = new Compiler();
            FileOutputStream  outputStream;
            try {
               outputStream = new FileOutputStream(outputFileName);
            } catch ( IOException e ) {
                String msg = "File " + outputFileName + " was not found";
                System.out.println(msg);
                outError.println("-1 : " + msg);
                outError.close();
                return ;
            }
            PrintWriter printWriter = new PrintWriter(outputStream);
            program = null;
            program  = compiler.compile(input, outError );

            if ( program != null ) {
               PW pw = new PW();
               pw.set(printWriter);
               //program.genHTML( pw );
              program.genK(pw, true);
               if ( printWriter.checkError() ) {
                  System.out.println("There was an error in the output");
               }
               outError.println("0");
            }
            //   outError.println("0");
            printWriter.close();
            outError.close();
        }
   }

}
        