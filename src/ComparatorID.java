import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ComparatorID extends Thread {

   private boolean isAlive;
   private boolean identical;

    @Override
    public void run() {
        isAlive = true;
        String path = pathGenerator();
        String[] files = fileScanner(path);

        ArrayList<Boolean> compares  = new ArrayList<>();
        for (int i = 1; i < files.length ; i++) {
            try {
                compares.add(comparator(fromFile(path,files[i]), fromZip(path,files[0],files[i])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!compares.contains(false)) {
            identical = true;
        } else {
            identical = false;
        }
        isAlive=false;

    }

    public boolean isIdentical() {
        return identical;
    }

    public boolean itisAlive() {
        return isAlive;
    }



    private static String [] fileScanner (String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();

        if (files.length<2||files==null) {
            throw new RuntimeException("Need more than 1");
        }
        String [] result = new String[files.length];
        for (File file : files) {
            String [] names = file.getName().split("\\.");
            if (names[1].equals("asice")) {
                result[0]=file.getName();
            } else {
                for (int i = 1; i < result.length ; i++) {
                    if (result[i]==null) {
                        result[i]=file.getName();
                        break;
                    }
                }
            }
        }
        if (result[0]==null) {
            throw new RuntimeException("Not found asice file!");
        }
        return result;

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

    private static boolean comparator (String fromZip, String fromDir) {
        return fromDir.equals(fromZip);
    }


    private static String fromZip (String path, String zipName, String fileName) throws IOException {
        String filePath = path+zipName;
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
