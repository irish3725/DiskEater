/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diskeater;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;

/**
 *
 * @author alex
 */
public class DiskEater {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int space = getSpace();
//        System.out.println(space);
        writeFile();
    }

    /**
     * returns remaining disk space.
     *
     * @return disk space in MB
     */
    public static int getSpace() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        for (Path root : FileSystems.getDefault().getRootDirectories()) {

            System.out.print(root + ": ");
            try {
                FileStore store = Files.getFileStore(root);
                System.out.println("available=" + nf.format(store.getUsableSpace())
                        + ", total=" + nf.format(store.getTotalSpace()));
                System.out.println("available = " + (store.getUsableSpace()) / 1000000);
                return (int) (store.getUsableSpace() / 1000000);
            } catch (IOException e) {
                System.out.println("error querying space: " + e.toString());
            }
        }
        return -1;
    }

    /**
     * writes file to fill percent of space.
     */
    public static void writeFile() {
        try {
//            Path root = (Path) FileSystems.getDefault().getRootDirectories();
            File file = new File("newfile.txt");

            if (file.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
