import javafx.util.Pair;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console {
    private static Scanner in = new Scanner(System.in);
    public static void showConsoleMenu() {

        System.out.println("This is Dardery's version of the LZ78 compression assignment");
        System.out.println("--------------------------------");
        char response;
        do {
            System.out.println("1 - Compress a line of text");
            System.out.println("2 - Decompress a line of text");
            System.out.println("3 - Compress a file");
            System.out.println("4 - Decompress a file");
            System.out.println("0 - Stop and exit");
            response = in.next().charAt(0);
            in.nextLine();
            switch (response) {
                case '1':
                    System.out.println("Enter your line of text");
                    String message = in.nextLine();
                    List<Pair<Integer, Character>> compressed = LZ78.compress(message.toCharArray());
                    System.out.println("Ok, here is the result:");
                    for (Pair<Integer, Character> p : compressed) {
                        System.out.print(p.getKey());
                        System.out.print(' ');
                        System.out.println(p.getValue());
                    }
                    System.out.println("Ok, here is the dynamically generated dictionary:");
                    int index = 1;
                    for (String s : LZ78.getLastDictionary()) {
                        System.out.print(index++);
                        System.out.print(" ");
                        System.out.println(s);
                    }
                    break;
                case '2':
                    System.out.println("Ok.. enter the compression tags. Enter each tag in the format: 'INDEX CHARACTER' with no quotes:");
                    System.out.println("Terminate input by inputting 'stop'");

                    try {
                        List<Pair<Integer, Character>> tags = getCompressionTags();
                        in.nextLine();
                        System.out.println(LZ78.decompress(tags));
                    } catch (Exception ex) {
                        System.out.print("Tags are wack.. better luck next time.");
                        System.out.println("Oh you want a formal text of the problem? Okay...");
                        System.out.println(ex.getMessage());
                    }
                    break;
                case '3':
                    System.out.println("Enter a file to compress path: ");
                    String inputC = in.nextLine();
                    System.out.println("Enter an output location: ");
                    String outputC = in.nextLine();
                    try {
                        Files.write(Paths.get(outputC),LZ78.compressToArray(Files.readAllBytes(Paths.get(inputC))));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case '4':

                    System.out.println("Enter a file to decompress path: ");
                    String inputD = in.nextLine();
                    System.out.println("Enter an output location: ");
                    String outputD = in.nextLine();
                    try {
                        Files.write(Paths.get(outputD),LZ78.decompressFromArray(Files.readAllBytes(Paths.get(inputD))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case '0':
                    System.out.println("Alright, peace");
                    break;
                default:
                    System.out.println("Dude.. get things straight, use one of the options.");
            }
        } while (response != '0');

    }

    private static List<Pair<Integer, Character>> getCompressionTags() throws Exception {
        List<Pair<Integer, Character>> tags = new ArrayList<>();
        while (true) {
            String sIndex = in.next();
            if (sIndex.equals("stop")) {
                return tags;
            }

            int nIndex = Integer.parseInt(sIndex);
            if (nIndex < 0) throw new Exception("First parameter of the compression tag cannot be negative.");
            String sCharacter = in.next();
            if (sCharacter.equals("null"))
                sCharacter = "\0";
            if (sCharacter.length() > 1)
                throw new Exception("Second parameter of the compression tag must be a single character.");
            tags.add(new Pair<>(nIndex, sCharacter.charAt(0)));
        }

    }
}
