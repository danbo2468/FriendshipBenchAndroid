/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers;

import android.app.Activity;
import android.util.Log;

import com.hanze.wad.friendshipbench.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileController {
    public static void writeFile(String fileName, String content, Activity activity){
        try{
            FileOutputStream fos = activity.openFileOutput(fileName, activity.getBaseContext().MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String fileName, Activity activity){
        FileInputStream fis = null;
        try {
            fis = activity.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static boolean fileExists(String fileName, Activity activity){
        try {
            activity.openFileInput(fileName);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}
