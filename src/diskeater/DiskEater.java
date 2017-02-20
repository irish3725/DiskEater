/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diskeater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class DiskEater {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double fill = .17;
        long available = -1;
        long total = -1;

        //get storage information
        NumberFormat nf = NumberFormat.getNumberInstance();
        for (Path root : FileSystems.getDefault().getRootDirectories()) {

            System.out.print(root + ": ");
            try {
                FileStore store = Files.getFileStore(root);
                System.out.println("available="
                        + nf.format(store.getUsableSpace())
                        + ", total=" + nf.format(store.getTotalSpace()));
                available = store.getUsableSpace();
                total = store.getTotalSpace();
            } catch (IOException e) {
                System.out.println("error querying space: " + e.toString());
            }
        }

        // remove file if it already exists
        File file = new File("newfile.txt");
        if (file.exists()) {
            file.delete();
        }

        System.out.println("Disk Space Available: " + (double)available/(double)total);
        
        writeFile(available, total, fill);
        getSpace();
    }

    /**
     * returns remaining disk space.
     *
     * @return disk space in MB
     */
    public static long getSpace() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        for (Path root : FileSystems.getDefault().getRootDirectories()) {

            System.out.print(root + ": ");
            try {
                FileStore store = Files.getFileStore(root);
                System.out.println("available="
                        + nf.format(store.getUsableSpace())
                        + ", total=" + nf.format(store.getTotalSpace()));
                return (store.getUsableSpace());
            } catch (IOException e) {
                System.out.println("error querying space: " + e.toString());
            }
        }
        return -1;
    }

    /**
     * writes file to fill percent of space using FileOutputStream.
     */
    public static void writeFile(long available, long total, double fill) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        
        // number of bytes written leaving fill% of disk empty
        long write = available - (long) (total * (1 - fill));
        System.out.println("write = " + nf.format(write));

        try {

            FileOutputStream s = new FileOutputStream("newfile.txt");
            // write by .5GB
            for (int i = 500000000; i < write; write -= 500000000) {
                byte[] buf = new byte[500000000];
                System.out.println("created byte array");
                s.write(buf);
                System.out.println("wrote to file");
                s.flush();
            }
            // write by MB
            for (int i = 1000000; i < write; write -= 1000000) {
                byte[] buf = new byte[1000000];
                s.write(buf);
                s.flush();
            }
            // write by KB
            for (int i = 1000; i < write; write -= 1000) {
                byte[] buf = new byte[1000];
                s.write(buf);
                s.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes file to fill percent of space using RandomAccessFile.
     */
    public static void fastWriteFile(long size, double fill) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        long write = (long) (size * fill);
        System.out.println("write = " + nf.format(write));
//        File file = new File("newfile.txt");
        RandomAccessFile f;
        try {
            f = new RandomAccessFile("newfile.txt", "rws");
            f.setLength(write);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DiskEater.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DiskEater.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

}
