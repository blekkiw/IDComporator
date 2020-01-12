import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = pathGenerator();
        String[] files = fileScanner(path);
        if (fromFile(path,files[1]).equals(fromZip(path, files[0],files[1]))) {
            System.out.println("Files is equals");
        } else {
            System.out.println("Files not equals");
        }

    }

    private static String [] fileScanner (String path) {
        File dir = new File(path);
        File [] files = dir.listFiles();
        if (files.length<2||files==null) {
            throw new RuntimeException("Need more than 1");
        }
        String fileAtFolder="", fileAtZip="";
        for (File file : files) {
            String s = file.getName();
            String [] names = s.split("\\.");
            if (names[1].equals("asice")) {
                fileAtZip=s;
            } else {
                fileAtFolder=s;
            }
        }

        return new String[]{fileAtZip,fileAtFolder};
    }

    private static String pathGenerator () {
        if (osIsWin()) {
            return System.getProperty("user.dir")+ "\\input\\";
        } else  return   System.getProperty("user.dir")+ "/input/";
    }

    private static boolean osIsWin () {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    private static String fromFile (String path, String fileName) throws IOException {
        String filePath = path+fileName;
        FileInputStream fileInputStream = new FileInputStream(filePath);
        String result = "";
        for (int c = fileInputStream.read(); c!=-1; c=fileInputStream.read()) {
            result+=c;
        }

return result;
    }


    private static String fromZip (String path, String zipName, String fileName) throws IOException {
        String filePath = path+zipName;
        FileReader fileReader = new FileReader(filePath);
        ZipFile zipFile = new ZipFile(filePath);
        zipFile.getEntry(fileName);

        FileInputStream fileInputStream = new FileInputStream(filePath);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        String result = "";
        while (zipEntry!=null) {
            if (!zipEntry.isDirectory()) {
                if (zipEntry.getName().equals(fileName)) {
                   for (int c = zipInputStream.read(); c!=-1; c= zipInputStream.read()) {
                       result+=c;
                   }
                }
            }
            zipEntry=zipInputStream.getNextEntry();
        }
        fileInputStream.close();
        zipInputStream.close();
return result;
    }
}
