package example.com.diary.data.fileutils;


import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    private static final int BUFFER = 2048;

    /**
     * Copy content from srcFile to dstFile
     *
     * @param srcFile - source file
     * @param dstFile - destination file
     * @return true if content was successfully copied, false otherwise
     */
    public static final boolean copyFile(String srcFile, String dstFile) {
        try {
            return copy(srcFile, dstFile, false);
        } catch (Exception e) {
            return false;
        }
    }

    private static final boolean copy(String src, String dst, boolean append) throws IOException {
        if (src == null || dst == null) {
            return false;
        }

        BufferedInputStream is = null;
        BufferedOutputStream os = null;
        try {
            File yourFile = new File(dst);
            yourFile.createNewFile(); // if file already exists will do nothing
            is = new BufferedInputStream(new FileInputStream(src));
            os = new BufferedOutputStream(new FileOutputStream(dst, append));

            // copy file
            byte data[] = new byte[BUFFER];
            int count;
            while ((count = is.read(data, 0, BUFFER)) != -1) {
                os.write(data, 0, count);
            }
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return true;
    }

    //read string from assets file
    public static String readFromfile(String fileName, Context context) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            Log.e("FileUtils", "error with open stream", e);
            throw e;
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                Log.e("FileUtils", "error with closing stream", e2);
                throw e2;
            }
        }
        return stringBuilder.toString();
    }
}
