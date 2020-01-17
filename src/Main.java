import com.sun.corba.se.impl.ior.ByteBuffer;

import javax.swing.*;
import java.io.*;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = pathGenerator();
        String[] files = fileScanner(path);
        ArrayList<Boolean> compares = new ArrayList<>();
        for (int i = 1; i < files.length; i++) {
            compares.add(comparator(fromFile(path, files[i]), fromZip(path, files[0], files[i])));
        }

        if (!compares.contains(false)) {
            System.out.println("Files are identical");
        } else {
            System.out.println("Files are different");
        }

    }


    private static String[] fileScanner(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        long timestamp = System.currentTimeMillis();
        if (files.length < 2 || files == null) {
            throw new RuntimeException("Need more than 1");
        }
        String[] result = new String[files.length];
        for (File file : files) {
            String[] names = file.getName().split("\\.");
            if (names[1].equals("asice")) {
                result[0] = file.getName();
            } else {
                for (int i = 1; i < result.length; i++) {
                    if (result[i] == null) {
                        result[i] = file.getName();
                        break;
                    }
                }
            }
        }
        if (result[0] == null) {
            throw new RuntimeException("Not found asice file!");
        }
        System.out.println("file scanner:" + (System.currentTimeMillis() - timestamp));
        return result;

    }

    private static String pathGenerator() {
        if (osIsWin()) {
            return System.getProperty("user.dir") + "\\input\\";
        } else return System.getProperty("user.dir") + "/input/";
    }

    private static boolean osIsWin() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    private static String fromFile(String path, String fileName) throws IOException {
        long timestamp = System.currentTimeMillis();
        String filePath = path + fileName;
        FileInputStream fileInputStream = new FileInputStream(filePath);
        String result = "";
//        for (int c = fileInputStream.read(); c != -1; c = fileInputStream.read()) {
//            result += c;
//        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[100];

        // reading and writing
        while ((bufferedInputStream.read(buffer)) != -1) {

            result+=Arrays.toString(buffer);

        }

bufferedInputStream.close();
        System.out.println("from file:" + (System.currentTimeMillis() - timestamp));
        System.out.println(result);
        return result;
    }

    private static boolean comparator(String fromZip, String fromDir) {
        return fromDir.equals(fromZip);

    }


    private static String fromZip(String path, String zipName, String fileName) throws IOException {
        long timestamp = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream(path + zipName);
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        String result = "";
        while (zipEntry != null) {
            if (!zipEntry.isDirectory()) {
                if (zipEntry.getName().equals(fileName)) {
//                    for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
//                        result += c;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[100];
                    // reading and writing
                    while ((zipInputStream.read(buffer)) != -1) {

                        result+=Arrays.toString(buffer);

                    }
                }
            }
            zipEntry = zipInputStream.getNextEntry();
        }
zipInputStream.closeEntry();
        System.out.println(result);
        fileInputStream.close();
        zipInputStream.close();
        System.out.println("from zip:" + (System.currentTimeMillis() - timestamp));
        return result;
    }
}
