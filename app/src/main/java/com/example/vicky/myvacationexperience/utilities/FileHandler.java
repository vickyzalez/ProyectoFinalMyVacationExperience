package com.example.vicky.myvacationexperience.utilities;

import android.content.Context;
import android.util.Log;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.Place;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.entities.LayerTrip;

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

    //devuelve un trip en formato file
    private static String readFile(Context context, Integer idTrip)throws IOException {

        File directory = context.getFilesDir();
        File f=new File(directory, String.valueOf(idTrip));
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

    private static Boolean deleteFile(Context context, Integer idTrip)throws IOException {

        File directory = context.getFilesDir();
        File f=new File(directory, String.valueOf(idTrip));
        return f.delete();

    }


    //lee todos los archivos y devuelve los Jsons en formato String
    private static List<String> readFiles(Context context)throws IOException {

        List<String> jsons = new ArrayList<String>();
        File directory = context.getFilesDir();
        File[] files = directory.listFiles();
        for (File file: files){

            jsons.add(readFile(context, Integer.parseInt(file.getName())));
            Log.d("director", readFile(context, Integer.parseInt(file.getName())));
        }

        return jsons;

    }

    //se escribe el trip en el celu
    private static void writeFile(String path, String text, Context context)throws IOException {
        File directory = context.getFilesDir();
        File f=new File(directory, path);
        FileWriter fw=new FileWriter(f);
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write(text);
        Log.d("JSONRECACA", text);
        bw.flush();
        bw.close();
    }

    private static Trip convertFromJson(JSONObject object) throws JSONException {

        Trip trip = new Trip();

        trip.setId(object.getInt("idTrip"));
        trip.setName(object.getString("nameTrip"));
        trip.setFromDate(object.getString("fromDate"));
        trip.setToDate(object.getString("toDate"));
        trip.setPhoto(object.getString("photo"));

        JSONArray arrayJsonLayer = object.getJSONArray("layers");
        List<LayerTrip> layers = new ArrayList<>();
        for(int i=0;i<arrayJsonLayer.length();i++) {

            LayerTrip layer = new LayerTrip();

            JSONObject layerJson = arrayJsonLayer.getJSONObject(i);

            layer.setName(layerJson.getString("nameLayer"));
            layer.setIcon(layerJson.getInt("icon"));
            layer.setVisible(layerJson.getBoolean("visible"));

            JSONArray arrayJsonPlace = layerJson.getJSONArray("places");
            Log.d("LUL", layerJson.toString());

            List<Place> places = new ArrayList<>();
            for(int j=0;j<arrayJsonPlace.length();j++) {

                Place place = new Place();

                JSONObject placeJson = arrayJsonPlace.getJSONObject(j);

                place.setId(placeJson.getInt("idPlace"));
                place.setName(placeJson.getString("namePlace"));
                place.setDescription(placeJson.getString("description"));
                place.setLatitude(placeJson.getDouble("latitude"));
                place.setLongitude(placeJson.getDouble("longitude"));
                places.add(place);


            }
            layer.setPlaces(places);
            layers.add(layer);
        }

        trip.setLayers(layers);
        Log.d("LOL", trip.getName()+" - "+trip.getLayers().toString());

        return trip;
    }
    /*
    Metodos que se usan desde la app
    */

    //devuelve el ID del proximo trip
    public static Integer getIdNextTrip(Context context)throws IOException {

        Integer nextId = 0;

        File directory = context.getFilesDir();
        File[] files = directory.listFiles();
        for (File file: files){
            Integer currentId = Integer.parseInt(file.getName());
            if (nextId < currentId){
                nextId = currentId;
            }

        }

        return nextId+1;

    }

    //devuelve el listado de trips para poder mostrar en pantalla
    public static List<Trip> getTripsToView(Context context) throws JSONException, IOException{

        List<Trip> trips = new ArrayList<Trip>();
        List<String> tripsString = readFiles(context);

        for (String trip: tripsString){

            trips.add(convertFromJson(new JSONObject(trip)));

        }

        return trips;
    }

    //te devuelve un Trip de un string.
    public static Trip getTrip(Context context, Integer idTrip)throws JSONException, IOException {

        String json= readFile(context, idTrip);
        JSONObject object = new JSONObject(json);

        return convertFromJson(object);


    }




    // guardo un nuevo Trip o una modificación del trip
    //TODO testear si se pisa
    public static void saveTrip(Trip trip, Context context)throws JSONException, IOException
    {
        JSONObject json = new JSONObject();
        JSONArray layers = new JSONArray();

        json.put("idTrip", trip.getId());
        json.put("nameTrip", trip.getName());
        json.put("fromDate", trip.getFromDate());
        json.put("toDate", trip.getToDate());
        json.put("photo", trip.getPhoto());

        for(LayerTrip layer: trip.getLayers()) {

            JSONArray places = new JSONArray();
            JSONObject jsonLayer = new JSONObject();

            jsonLayer.put("nameLayer", layer.getName());
            jsonLayer.put("icon", layer.getIcon());
            jsonLayer.put("visible", layer.getVisible());

            for(Place place: layer.getPlaces()){

                JSONObject jsonPlace = new JSONObject();
                jsonPlace.put("idPlace", place.getId());
                jsonPlace.put("namePlace", place.getName());
                jsonPlace.put("description", place.getDescription());
                jsonPlace.put("latitude", place.getLatitude());
                jsonPlace.put("longitude", place.getLongitude());

                places.put(jsonPlace);

            }

            jsonLayer.put("places", places);
            layers.put(jsonLayer);
        }
        json.put("layers", layers);
        Log.d("JSONCACA", json.toString());
        writeFile(String.valueOf(trip.getId()), json.toString(), context);


    }

    public static Boolean deleteTrip(Integer idTrip, Context context) throws IOException {

        return deleteFile(context, idTrip);
    }

}
