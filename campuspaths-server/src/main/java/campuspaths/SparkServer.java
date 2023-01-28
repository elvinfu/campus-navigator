/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import pathfinder.CampusMap;

import java.util.*;

/**
 * This is the main class that runs the server, it includes a main method
 * that sets up the server and allows it to accept requests.
 */
public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        CampusMap campusMap = new CampusMap("campus_buildings.csv", "campus_paths.csv");

        // Returns a JSON of the shortest route between two buildings
        Spark.get("/find-route", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String startString = request.queryParams("start");
                String endString = request.queryParams("end");

                if(startString == null || endString == null) {
                    Spark.halt(400, "must have start and end");
                }
                Path<Point> path = campusMap.findShortestPath(startString, endString);
                Gson gson = new Gson();
                String json = gson.toJson(path);
                return json;
            }
        });

        // Returns a JSON of all of the short building names
        Spark.get("/getBuildings", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Set<String> buildings = campusMap.buildingNames().keySet();
                Gson gson = new Gson();
                String json = gson.toJson(buildings);
                return json;
            }
        });
    }

}
