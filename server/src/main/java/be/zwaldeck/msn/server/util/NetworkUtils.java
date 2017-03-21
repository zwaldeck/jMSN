package be.zwaldeck.msn.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author Wout Schoovaerts
 */
public class NetworkUtils {

    public static String getExternalIp() {
        String[] whatsmyipSources = {
                "http://checkip.amazonaws.com/",
                "http://ipecho.net/plain"
        };

        for(String source : whatsmyipSources) {
            String ip = getExternalIpFromSource(source);
            if(ip != null) {
                return ip;
            }
        }

        return null;
    }

    private static String getExternalIpFromSource(String source) {
        URL whatismyip = null;
        BufferedReader in = null;
        try {
            whatismyip = new URL(source);
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return in.readLine();
        } catch (Exception ignored) {}
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {}
            }
        }

        return null;
    }
}
