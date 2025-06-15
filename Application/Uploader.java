import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Uploader {

    // 🔑 Your Dropbox API access token
    // private static final String ACCESS_TOKEN ="sl.u.AFsKUJINNLZ7O7-CPktPUH8UiccTNGP0biOvJSvZMQJ239lXhotIgqubIrPXQNM0m0M407Fz8Y5i22qaOxeliAo0WMOjwpXHJENAJUA8g158DumG4K2g_ZiLAoiGHEB0hvVz5QqCnvnz-od9K0k__ptZVNke7ExYS-iFmoTWe_czRryA-8RBPq5UHUjBYTyN5hkWrMNDaDsvVEUQF-Umoj8LIpQ2rcn7IropH5LVc-QCdCHWL7q9OR6MXNZ2HDeSVlYTddHbQY693RjhuXKesPcX21SnShcqXBHO7cWkKit5oPnrQ6svv-vqGxWvcWxEfJewMuj5t7ECnXqaCtStoSD-SIo3_5zXVNi1siyhMutXvRs_opsJl1HE_tPgraw18QBidFnzfLb7jQnvzhkfkFPVWd_YzfnEi_DC_kTUMyPnPej4LRYDgJ_I2qwCxXoocH15qUDpej2ffKzSRiIiS-pcBqutM2T4c83hqBQ2OfXmynAGz6bc8S7GIy9U4yq33iTx6vplZch8hef-7LM79cG0ryqEPrlRJGcyxBBs7QIpb5i12nScRn1iZHxhIqcrXhiA23jdSeA7YWyGf-H1d1US6vOl0rsr7DuSXtmd5VJtJiXkd62cv0HloSMRS8BaVLvZQc-YTihwgx0szjwzsRZSdXaHEviFKHxpHxj4wjD0b9-9H07NAjEG0Dmbnv1UYqh4a5Idy75khwNA0-2TauJF-FoRFJMf9Bowv6maCvRKWxOmcMIKCublz6DLcO6mMpjA_LRTdrYDG77SYkB_pOZbaqLKrAAzge3Z8LPsrR6_jYwNceYLWr_CRTewvjXE_LjlXwcQ1FthoV8OXFOMEJ-ccoIzsU8Vs1oKVINzclKNGXivFaY4PybbZdT1ROeucxoqyKQIwHQZB_CGkmoVdlt8FOO05REqFEIVsMKY-e1NjCGakdEPVrNXXk3iSGUbaauTpnmr59byvOh0l8kHebVM1bTHlJ-njSsPHZyaItkJFOMyjYvW-6NV1ZGGBY-B6zEU8EJc6-cZF1JVbdnqi71YcELZkFWl2z_ythpXpaOvVfJwUZjCQKRf9jkm_E3C55WJsTJE79CBomVo5MRgaNCimKsLBT3zYuW4iRXLfvbACITPLqX19vVqKKM3QKjTV__4nuF31iCupinQ9_hsZioCTVXNXzo5sHuiWUtxNrQWwYCORjKbAx5SqJIE5dq73aLbFcHsdSIWryP07LfxxG7YcTc0Cfal1hsOeU8H_T857o_wO1CSNxsX8Ja9sryYJzZUiJWAZUWxQNdCpFoQT8aLSQLlml6Cjgu6egTJgp9rJAMuTMn1xY93jnDTRdxGqUhYQzxVWX5GLqpGXvsogeCNHlQOeiLevX9h8iJ3H-cHK4mqqPRM-Th406xv1y-2Fxqnvf_C2z2eKn92XRrIep4sJUA6wZiM0HL0CYm0u4E_1g";

    public static String upload(String filename, String ACCESS_TOKEN) throws Exception {
        // 📄 Local file to upload
        File file = new File(filename);

        // 📁 Target path in Dropbox
        String dropboxPath = "/"+filename;

        // 🔼 Step 1: Upload file
        URL uploadUrl = new URL("https://content.dropboxapi.com/2/files/upload");
        HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
        uploadConn.setRequestMethod("POST");
        uploadConn.setDoOutput(true);
        uploadConn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
        uploadConn.setRequestProperty("Content-Type", "application/octet-stream");
        uploadConn.setRequestProperty("Dropbox-API-Arg", "{\"path\": \"" + dropboxPath + "\",\"mode\": \"overwrite\"}");

        try (OutputStream os = uploadConn.getOutputStream(); FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }

        if (uploadConn.getResponseCode() != 200) {
            throw new IOException("Upload failed. HTTP code: " + uploadConn.getResponseCode());
        }

        // 🌐 Step 2: Create or fetch shared link
        String directLink = "";

        try {
            // Attempt to create shared link
            URL shareUrl = new URL("https://api.dropboxapi.com/2/sharing/create_shared_link_with_settings");
            HttpURLConnection shareConn = (HttpURLConnection) shareUrl.openConnection();
            shareConn.setRequestMethod("POST");
            shareConn.setDoOutput(true);
            shareConn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            shareConn.setRequestProperty("Content-Type", "application/json");

            String jsonBody = "{\"path\":\"" + dropboxPath + "\", \"settings\": {\"requested_visibility\": \"public\"}}";
            try (OutputStream os = shareConn.getOutputStream()) {
                os.write(jsonBody.getBytes("UTF-8"));
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(shareConn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            // Extract link
            String json = response.toString();
            int urlStart = json.indexOf("\"url\": \"") + 8;
            int urlEnd = json.indexOf("\"", urlStart);
            directLink = json.substring(urlStart, urlEnd).replace("dl=0", "dl=1");

        } catch (IOException e) {
            if (e.getMessage().contains("409")) {
                // If link already exists, retrieve it
                URL listUrl = new URL("https://api.dropboxapi.com/2/sharing/list_shared_links");
                HttpURLConnection listConn = (HttpURLConnection) listUrl.openConnection();
                listConn.setRequestMethod("POST");
                listConn.setDoOutput(true);
                listConn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
                listConn.setRequestProperty("Content-Type", "application/json");

                String jsonBody = "{\"path\":\"" + dropboxPath + "\", \"direct_only\": true}";
                try (OutputStream os = listConn.getOutputStream()) {
                    os.write(jsonBody.getBytes("UTF-8"));
                }

                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(listConn.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }

                String json = response.toString();
                int urlStart = json.indexOf("\"url\": \"") + 8;
                int urlEnd = json.indexOf("\"", urlStart);
                directLink = json.substring(urlStart, urlEnd).replace("dl=0", "dl=1");

            } else {
                throw new RuntimeException("Unexpected error: " + e.getMessage());
            }
        }

        // ✅ Final output: Just the direct link
        // System.out.println(directLink);
        return directLink;
    }
}
