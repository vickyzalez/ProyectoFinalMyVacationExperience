package com.example.vicky.myvacationexperience.utilities;

import android.content.Context;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 11/2/2018.
 */

public class FileHandler {

    public static String readFile(Context context, Integer idTrip)throws IOException {

        File directory = context.getFilesDir();
        File f=new File(directory, String.valueOf(idTrip)); //sin el .txt
        FileReader fr=new FileReader(f);
        BufferedReader br=new BufferedReader(fr);
        String text="";
        String line;
        while((line=br.readLine())!=null)
        {
            text+=line;
        }
        br.close();
        return text;
    }

    /*
    //TODO para ver todos los archivos
    public static String readFiles(Context context)throws IOException {

        File directory = context.getFilesDir();
        File f=new File(directory, String.valueOf(idTrip)); //sin el .txt
        FileReader fr=new FileReader(f);
        BufferedReader br=new BufferedReader(fr);
        String text="";
        String line;
        while((line=br.readLine())!=null)
        {
            text+=line;
        }
        br.close();
        return text;
    }
    */


    public static void writeFile(String path, String text, Context context)throws IOException {
        File directory = context.getFilesDir();
        File f=new File(directory, path);
        FileWriter fw=new FileWriter(f);
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write(text);
        bw.flush();
        bw.close();
    }

    //TODO esto deberia ser para un solo trip, hay q cambiarlo

    /*
    public static List<Trip> getTripsJSON(Context context)throws JSONException, IOException {
        String json= readFile(context, idTrip);
        JSONObject objet = new JSONObject(json);
        JSONArray arrayJson = objet.getJSONArray("trips");
        List<Trip> trips = new ArrayList<>();
        for(int i=0;i<arrayJson.length();i++)
        {
            Trip trip = new Trip();
            JSONObject tripJson = arrayJson.getJSONObject(i);
            //TODO mapear los atributos

            trip. = tripJson.getString("nombre");
            trip. = tripJson.getBoolean("hecho");
            trips.add(trip);
        }

        return trips;
    }


    public static void putTripsJSON(List<Trip> trips, Context context)throws JSONException, IOException
    {
        JSONObject json = new JSONObject();
        JSONArray list = new JSONArray();
        for(Trip act:trips)
        {
            JSONObject jsonObject = new JSONObject();
            //TODO mapear
            jsonObject.put("nombre", act.getNombre());
            jsonObject.put("hecho", act.getHecho());
            list.put(jsonObject);
        }
        json.put("trips", list);

    }

    */

}
