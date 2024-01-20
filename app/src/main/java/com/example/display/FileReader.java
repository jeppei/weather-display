package com.example.display;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {

    public static String readAssetFile(Context context, String fileName) {
        StringBuilder content = new StringBuilder();
        AssetManager assetManager = context.getAssets();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
