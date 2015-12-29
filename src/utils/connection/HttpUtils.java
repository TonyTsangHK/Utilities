package utils.connection;

import utils.file.FileUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2013-06-05
 * Time: 14:48
 */
public class HttpUtils {
    private HttpUtils() {}

    public static String readUrl(String url) {
        URL resourceUrl;
        HttpURLConnection connection = null;
        try {
            //Create connection
            resourceUrl = new URL(url);
            connection = (HttpURLConnection)resourceUrl.openConnection();

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Get Response
            InputStream is = connection.getInputStream();

            String content = FileUtil.getFileContent(is);

            is.close();

            return content;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
