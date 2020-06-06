package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadBathingSitesTask extends AsyncTaskLoader<Boolean> {

    private String downloadUrl;

    public DownloadBathingSitesTask(@NonNull Context context, String downloadUrl) {
        super(context);
        this.downloadUrl = downloadUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Boolean loadInBackground() {
        return downloadBathingSites();
    }

    private boolean downloadBathingSites() {
        File cacheFile = null;
        boolean savedToDatabase = false;
        HttpURLConnection connection = null;
        try {
            URL fileUrl = new URL(downloadUrl);
            connection = (HttpURLConnection) fileUrl.openConnection();
            connection.connect();

            // Get filename from URL
            String fileName = URLUtil.guessFileName(downloadUrl, null, null);
            cacheFile = File.createTempFile(fileName, null, getContext().getCacheDir());

            // Initialize streams
            InputStream input = new BufferedInputStream(fileUrl.openStream());
            OutputStream output = new FileOutputStream(cacheFile);
            byte[] data = new byte[1024];
            int count;
            // Download file
            while((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            savedToDatabase = fileSavedToDatabase(cacheFile);
        } catch (Exception e) {
            Log.d("Exception message", e.toString());
            savedToDatabase = false;
        }
        // Clean up
        finally {
            if(connection != null) {
                connection.disconnect();
            }
            if(cacheFile != null) {
                cacheFile.delete();
            }
        }
        return savedToDatabase;
    }

    private boolean fileSavedToDatabase(File cacheFile) throws IOException {
        BathingSiteDatabase database = BathingSiteDatabase.getInstance(getContext());
        BufferedReader fileReader = new BufferedReader(new FileReader(cacheFile));
        boolean savedToDatabase;
        try{
            String line;

            while ((line = fileReader.readLine()) != null) {
                Log.d("FILE LINE", line);
                String[] dataString = line.split(",", 0);
                for (int i = 0; i < dataString.length; i++) {
                    dataString[i] = dataString[i]
                                        .replace("\"", "")
                                        .replaceAll("\\p{C}", "")
                                        .trim();
                }
                double latitude = Double.parseDouble(dataString[0]);
                double longitude = Double.parseDouble(dataString[1]);
                String name = dataString[2];
                String address = "";
                // If address has been included
                if(dataString.length > 3) {
                    name = dataString[3];
                }
                if(database.bathingSiteDao().checkForDuplicate(latitude, longitude) != 1) {
                    BathingSite bathingSite = new BathingSite(0, name, address, latitude, longitude);
                    database.bathingSiteDao().insertBathingSite(bathingSite);
                }
            }
            savedToDatabase = true;
        } catch (IOException e) {
            savedToDatabase = false;
        } finally {
            fileReader.close();
        }
        return savedToDatabase;
    }
}
