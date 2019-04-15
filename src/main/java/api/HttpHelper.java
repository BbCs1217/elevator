package api;

import resources.SendType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpHelper {
    public String send(SendType sendType, String url, Map<String, String> headers, String params) {
        try {
            URL connURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connURL.openConnection();

            if(sendType == SendType.GET) {
                conn.setDoOutput(false);
            } else if(sendType == SendType.POST) {
                conn.setDoOutput(true);
            }
            conn.setRequestMethod(sendType.name());
            if(headers != null) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    conn.addRequestProperty(e.getKey(), e.getValue());
                }
            }

            if(params != null) {
                try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.write(params.getBytes());
                }
            }
            StringBuilder builder;
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                builder = new StringBuilder();

                while((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                }
            }
            return builder.toString();
        } catch (IOException e) {
            return null;
        }
    }
    public String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
