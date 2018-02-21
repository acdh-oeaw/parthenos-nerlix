package at.ac.oeaw.helpers;

import java.io.*;

public final class FileReader {


    public static String readFile(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String file = sb.toString();
        return file;
    }

}
