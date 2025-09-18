package util;

import okhttp3.*;

import java.io.IOException;

public class SupabaseClient {
    private static final String SUPABASE_URL = "https://kognqhcifxbpihjrwocg.supabase.co";
    private static final String ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY2MDkzNDEsImV4cCI6MjA3MjE4NTM0MX0.P2thkCIF98_bnuJdcy6Nwxp3-9uOSvbebx1rfkU2j04";

    private static final OkHttpClient client = new OkHttpClient();

    public static String get(String tableName, String queryParams) throws IOException {
        String url = SUPABASE_URL + "/rest/v1/" + tableName;
        if (queryParams != null && !queryParams.isEmpty()) {
            url += "?" + queryParams;
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", ANON_KEY)
                .addHeader("Authorization", "Bearer " + ANON_KEY)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed: " + response);
            }
            return response.body().string();
        }
    }
}