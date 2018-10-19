// information about server communication. This sample webservice is provided by Wikitude and returns random dummy places near given location
var ServerInformation = {
	POIDATA_SERVER: "https://example.wikitude.com/GetSamplePois/",
	POIDATA_SERVER_ARG_LAT: "lat",
	POIDATA_SERVER_ARG_LON: "lon",
	POIDATA_SERVER_ARG_NR_POIS: "nrPois"
};

var poiTypes = [];

// implementation of AR-Experience (aka "World")
var World = {

	//  user's latest known location, accessible via userLocation.latitude, userLocation.longitude, userLocation.altitude
	userLocation: null,

	// you may request new data from server periodically, however: in this sample data is only requested once
	isRequestingData: false,

	// true once data was fetched
	initiallyLoadedData: false,

	// different POI-Marker assets
	markerDrawable_idle: null,
	markerDrawable_selected: null,
	markerDrawable_directionIndicator: null,

	// list of AR.GeoObjects that are currently shown in the scene / World
	markerList: [],

	// The last selected marker
	currentMarker: null,

	locationUpdateCounter: 0,
	updatePlacemarkDistancesEveryXLocationUpdates: 10,

	//Variables para filtros en pois
	selectedFilter: "all",

	// called to inject new POI data
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(poiData) {

		// destroys all existing AR-Objects (markers & radar)
		AR.context.destroyAll();

		// show radar & set click-listener
		PoiRadar.show();
		$('#radarContainer').unbind('click');
		$("#radarContainer").click(PoiRadar.clickedRadar);

		// empty list of visible markers
		World.markerList = [];

		// start loading marker assets
		World.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png");
		World.markerDrawable_selected = new AR.ImageResource("assets/marker_selected.png");
		World.markerDrawable_directionIndicator = new AR.ImageResource("assets/indi.png");

//TODO mapear
		// loop through POI-information and create an AR.GeoObject (=Marker) per POI
		for (var currentPlaceNr = 0; currentPlaceNr < poiData.length; currentPlaceNr++) {

			var singlePoi = {
                "id": currentPlaceNr,
                "latitude": parseFloat(poiData[currentPlaceNr].geometry.location.lat),
                "longitude": parseFloat(poiData[currentPlaceNr].geometry.location.lng),
                "altitude": 0,
                "title": poiData[currentPlaceNr].name,
                "description": poiData[currentPlaceNr].vicinity,
                "placeID": poiData[currentPlaceNr].place_id,
            };




            //Si tiene rating, ponerlo
			if(poiData[currentPlaceNr].rating != undefined){
			    singlePoi.rating = parseFloat(poiData[currentPlaceNr].rating);
			}

            //Si tiene horario, ponerlo
			if(poiData[currentPlaceNr].opening_hours != undefined){
			    singlePoi.open = poiData[currentPlaceNr].opening_hours.open_now;
			}

			//Si tiene icono, ponerlo
            if(poiData[currentPlaceNr].icon != undefined){
                singlePoi.icon = poiData[currentPlaceNr].icon;
            } else {
                singlePoi.icon = "https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png";
            }

			  World.markerList.push(new Marker(singlePoi));


		}

		// updates distance information of all placemarks
		World.updateDistanceToUserValues();

		World.updateStatusMessage(currentPlaceNr + ' places loaded');

		// set distance slider to 100%
		$("#panel-distance-range").val(100);
		$("#panel-distance-range").slider("refresh");
	},

	// sets/updates distances of all makers so they are available way faster than calling (time-consuming) distanceToUser() method all the time
	updateDistanceToUserValues: function updateDistanceToUserValuesFn() {
		for (var i = 0; i < World.markerList.length; i++) {
			World.markerList[i].distanceToUser = World.markerList[i].markerObject.locations[0].distanceToUser();
		}
	},

	// updates status message shown in small "i"-button aligned bottom center
	updateStatusMessage: function updateStatusMessageFn(message, isWarning) {

		var themeToUse = isWarning ? "e" : "c";
		var iconToUse = isWarning ? "alert" : "info";

		$("#status-message").html(message);
		$("#popupInfoButton").buttonMarkup({
			theme: themeToUse
		});
		$("#popupInfoButton").buttonMarkup({
			icon: iconToUse
		});
	},

	/*
		It may make sense to display POI details in your native style. 
		In this sample a very simple native screen opens when user presses the 'More' button in HTML. 
		This demoes the interaction between JavaScript and native code.
	*/
	// user clicked "More" button in POI-detail panel -> fire event to open native screen
	onPoiDetailMoreButtonClicked: function onPoiDetailMoreButtonClickedFn() {
		var currentMarker = World.currentMarker;
		var markerSelectedJSON = {
            action: "present_poi_details",
            id: currentMarker.poiData.id,
            title: currentMarker.poiData.title,
            description: currentMarker.poiData.description
        };
		/*
			The sendJSONObject method can be used to send data from javascript to the native code.
		*/
		AR.platform.sendJSONObject(markerSelectedJSON);
	},

	// location updates, fired every time you call architectView.setLocation() in native environment
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {

		// store user's current location in World.userLocation, so you always know where user is
		World.userLocation = {
			'latitude': lat,
			'longitude': lon,
			'altitude': 0, //TODO ver, va alt
			'accuracy': acc
		};


		// request data if not already present
		if (!World.initiallyLoadedData) {
			World.requestDataFromServer(lat, lon);
			World.initiallyLoadedData = true;
		} else if (World.locationUpdateCounter === 0) {
			// update placemark distance information frequently, you max also update distances only every 10m with some more effort
			World.updateDistanceToUserValues();
		}

		// helper used to update placemark information every now and then (e.g. every 10 location upadtes fired)
		World.locationUpdateCounter = (++World.locationUpdateCounter % World.updatePlacemarkDistancesEveryXLocationUpdates);
	},

	// fired when user pressed maker in cam
	onMarkerSelected: function onMarkerSelectedFn(marker) {
		World.currentMarker = marker;

		//llamar a Details by placeID
		var poiDetail = {
		    "idPoi": 1,
		};

		console.log("HOLIS"+JSON.stringify(marker));
        //Se llama a la api de details por place_id
        var jqxhr = $.getJSON("https://maps.googleapis.com/maps/api/place/details/json?placeid="+marker.poiData.placeID+"&key=AIzaSyAC05uwYBK-r3NF3rqzXPaa22uZPDz9JcU", function(data) {

            if (data.status == "OK"){
                poiDetail.address = data.result.formatted_address;
                poiDetail.phone = data.result.formatted_phone_number;

                if(data.result.international_phone_number != undefined){
                    poiDetail.internationalPhone = data.result.international_phone_number;
                }

                poiDetail.urlMaps = data.result.url;

                if(data.result.website != undefined){
                    poiDetail.website = data.result.website;
                }

                if(data.result.opening_hours != undefined){
                    poiDetail.hours = data.result.opening_hours.weekday_text;
                }

            }
            // update panel values


                $("#poi-detail-title").html(marker.poiData.title);
                $("#poi-detail-description").html(marker.poiData.description);

                if(poiDetail.internationalPhone != undefined){
                    $("#poi-detail-phone").html("<i class='fas fa-phone'></i> "+ poiDetail.internationalPhone);
                } else {
                   $("#poi-detail-phone").html("");
                }


                $("#poi-detail-urlmaps").html("<a href='"+poiDetail.urlMaps+"'><i class='fas fa-map-marker-alt'></i> GoogleMaps</a>");

                if(poiDetail.website != undefined){
                    //$("#poi-detail-website").html("<a href='"+poiDetail.website+"' target='_blank'><i class='fas fa-globe'></i> Website</a>");
                    console.log(poiDetail.website);

                    $("#poi-detail-website").html("<a href='#' onclick='mostrarWebsite(\""+ poiDetail.website+"\")'><i class='fas fa-globe'></i> Website</a>");
                } else {
                    $("#poi-detail-website").html("");
                }


                if(poiDetail.hours != undefined){
                    var stringToChange = poiDetail.hours;
                    var formattedString = stringToChange.toString().split(",").join("<br />");
                    $("#poi-detail-hours").html(formattedString);
                } else {
                    $("#poi-detail-hours").html("");
                }

                if(marker.poiData.open != undefined){
                    //TODO poner esto en el xml de strings
                    if(marker.poiData.open == true){
                        $("#poi-detail-open").html("<i class='fas fa-clock'></i>"+" Abierto");
                    } else {
                            $("#poi-detail-open").html("<i class='fas fa-clock'></i>"+" Cerrado");
                    }
                } else {
                    $("#poi-detail-open").html("");
                }

                if(marker.poiData.rating != undefined){
                    $("#poi-detail-rating").html("<i class='fas fa-star'></i> "+ marker.poiData.rating);
                } else {
                    $("#poi-detail-rating").html("");
                }



                /* It's ok for AR.Location subclass objects to return a distance of `undefined`. In case such a distance was calculated when all distances were queried in `updateDistanceToUserValues`, we recalcualte this specific distance before we update the UI. */
                if( undefined == marker.distanceToUser ) {
                    marker.distanceToUser = marker.markerObject.locations[0].distanceToUser();
                }
                var distanceToUserValue = (marker.distanceToUser > 999) ? ((marker.distanceToUser / 1000).toFixed(2) + " km") : (Math.round(marker.distanceToUser) + " m");

                $("#poi-detail-distance").html(distanceToUserValue);

                // show panel
                $("#panel-poidetail").panel("open", 123);

                $(".ui-panel-dismiss").unbind("mousedown");

                $("#panel-poidetail").on("panelbeforeclose", function(event, ui) {
                    World.currentMarker.setDeselected(World.currentMarker);
                });

        });


    //TODO acá se mapea lo q se ve en el panel

	},

	// screen was clicked but no geo-object was hit
	onScreenClick: function onScreenClickFn() {
		if (World.currentMarker) {
        			World.currentMarker.setDeselected(World.currentMarker);
        		}
	},

	// returns distance in meters of placemark with maxdistance * 1.1
	getMaxDistance: function getMaxDistanceFn() {

		// sort places by distance so the first entry is the one with the maximum distance
		World.markerList.sort(World.sortByDistanceSortingDescending);

		// use distanceToUser to get max-distance
		var maxDistanceMeters = World.markerList[0].distanceToUser;

		// return maximum distance times some factor >1.0 so ther is some room left and small movements of user don't cause places far away to disappear
		return maxDistanceMeters * 1.1;
	},

	// udpates values show in "range panel"
	updateRangeValues: function updateRangeValuesFn() {

		// get current slider value (0..100);
		var slider_value = $("#panel-distance-range").val();

		// max range relative to the maximum distance of all visible places
		var maxRangeMeters = Math.round(World.getMaxDistance() * (slider_value / 100));

		// range in meters including metric m/km
		var maxRangeValue = (maxRangeMeters > 999) ? ((maxRangeMeters / 1000).toFixed(2) + " km") : (Math.round(maxRangeMeters) + " m");

		// number of places within max-range
		var placesInRange = World.getNumberOfVisiblePlacesInRange(maxRangeMeters);

		// update UI labels accordingly
		$("#panel-distance-value").html(maxRangeValue);
		$("#panel-distance-places").html((placesInRange != 1) ? (placesInRange + " Places") : (placesInRange + " Place"));

		// update culling distance, so only places within given range are rendered
		AR.context.scene.cullingDistance = Math.max(maxRangeMeters, 1);

		// update radar's maxDistance so radius of radar is updated too
		PoiRadar.setMaxDistance(Math.max(maxRangeMeters, 1));
	},

	// returns number of places with same or lower distance than given range
	getNumberOfVisiblePlacesInRange: function getNumberOfVisiblePlacesInRangeFn(maxRangeMeters) {

		// sort markers by distance
		World.markerList.sort(World.sortByDistanceSorting);

		// loop through list and stop once a placemark is out of range ( -> very basic implementation )
		for (var i = 0; i < World.markerList.length; i++) {
			if (World.markerList[i].distanceToUser > maxRangeMeters) {
				return i;
			}
		};

		// in case no placemark is out of range -> all are visible
		return World.markerList.length;
	},

	handlePanelMovements: function handlePanelMovementsFn() {

		$("#panel-distance").on("panelclose", function(event, ui) {
			$("#radarContainer").addClass("radarContainer_left");
			$("#radarContainer").removeClass("radarContainer_right");
			PoiRadar.updatePosition();
		});

		$("#panel-distance").on("panelopen", function(event, ui) {
			$("#radarContainer").removeClass("radarContainer_left");
			$("#radarContainer").addClass("radarContainer_right");
			PoiRadar.updatePosition();
		});
	},

	// display range slider
	showRange: function showRangeFn() {
		if (World.markerList.length > 0) {

			// update labels on every range movement
			$('#panel-distance-range').change(function() {
				World.updateRangeValues();
			});

			World.updateRangeValues();
			World.handlePanelMovements();

			// open panel
			$("#panel-distance").trigger("updatelayout");
			$("#panel-distance").panel("open", 1234);
		} else {

			// no places are visible, because the are not loaded yet
			World.updateStatusMessage('No places available yet', true);
		}
	},

	// reload places from content source
      	reloadPlaces: function reloadPlacesFn() {
      		if (!World.isRequestingData) {
      			if (World.userLocation) {
      				World.requestDataFromServer(World.userLocation.latitude, World.userLocation.longitude);
      			} else {
      				World.updateStatusMessage('Unknown user-location.', true);
      			}
      		} else {
      			World.updateStatusMessage('Already requesing places...', true);
      		}
      	},




	// request POI data
	requestDataFromServer: function requestDataFromServerFn(lat, lon) {

		// set helper var to avoid requesting places while loading
		World.isRequestingData = true;
		World.updateStatusMessage('Requesting places from web-service');
        switch(World.selectedFilter) {
           case "all" :
              var serverUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lon + "&radius=1500&key=AIzaSyAC05uwYBK-r3NF3rqzXPaa22uZPDz9JcU";

              break;

           case "stores" : //TODO ver
              var serverUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lon + "&radius=1500&type=store&key=AIzaSyAC05uwYBK-r3NF3rqzXPaa22uZPDz9JcU";

              break;

           case "restaurants" :
             var serverUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lon + "&radius=1500&type=restaurant&key=AIzaSyAC05uwYBK-r3NF3rqzXPaa22uZPDz9JcU";

             break;

           case "visit" : //TODO Ver
              var serverUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lon + "&radius=1500&type=park&key=AIzaSyAC05uwYBK-r3NF3rqzXPaa22uZPDz9JcU";

             break;

           case "cafe" : //TODO Ver
             var serverUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lon + "&radius=1500&type=cafe&key=AIzaSyAC05uwYBK-r3NF3rqzXPaa22uZPDz9JcU";

            break;
        }


		var jqxhr = $.getJSON(serverUrl, function(data) {

           if (data.status == "OK"){
                World.loadPoisFromJsonData(data.results);
            }

			})
			.error(function(err) {
				/*
					In certain circumstances your web service may not be available or other connection issues can occur. 
					To notify the user about connection problems a status message is updated.
					In your own implementation you may e.g. use an info popup or similar.
*/
				World.updateStatusMessage("Invalid web-service response.", true);
				World.isRequestingData = false;
			})
			.complete(function() {
				World.isRequestingData = false;
			});
			World.isRequestingData = false;
	},

	// helper to sort places by distance
	sortByDistanceSorting: function(a, b) {
		return a.distanceToUser - b.distanceToUser;
	},

	// helper to sort places by distance, descending
	sortByDistanceSortingDescending: function(a, b) {
		return b.distanceToUser - a.distanceToUser;
	},

	showPoiList: function(){
        var table = document.getElementById("tablePois");
	    var poiList = World.markerList;

        for (var i = 0; i < World.markerList.length; i++) {
            var row = table.insertRow(i);
            var cellIcon = row.insertCell(0);
            var cellContent = row.insertCell(1);

            cellIcon.innerHTML = "<img src='" + World.markerList[i].poiData.icon + "' width='40' height='40' style='margin-left:15%' >";

            if(World.markerList[i].poiData.rating != undefined){
            var HTML = "<div style='text-align: right;font-weight: bold; margin-right:5%; margin-left:5%'>"+World.markerList[i].poiData.title+"</div><div style='text-align: left;margin-right:5%; margin-left:5%; margin-top:3% ; font-size: 14px'>"+World.markerList[i].poiData.description+"</div><div style='text-align: right; color:#086FA1; margin-right:5%; margin-left:5%; margin-bottom:3%'><i class='fas fa-star'></i> "+World.markerList[i].poiData.rating+"</div>";
            } else {
            var HTML = "<div style='text-align: right;font-weight: bold; margin-right:5%; margin-left:5%'>"+World.markerList[i].poiData.title+"</div><div style='text-align: left;margin-right:5%; margin-left:5%; margin-top:3% ; font-size: 14px'>"+World.markerList[i].poiData.description+"</div>";
            }
            cellContent.innerHTML = HTML;

            function generateFunction(index){
                return function(){
                  console.log("MARKERLIST"+JSON.stringify(World.markerList[index]));
                  World.onMarkerSelected(World.markerList[index]);
                  World.hidePoiList();
                }
            }

              row.onclick = generateFunction(i);

        }

        document.getElementById("poiList").style.display = "block";
        document.getElementById("page1").style.display = "none";

	},

	hidePoiList: function(){
        document.getElementById("poiList").style.display = "none";
        document.getElementById("page1").style.display = "block";

        var table = document.getElementById("tablePois");
        var num = table.getElementsByTagName("tr").length;

        for( i = 0; i < num; i++){
            table.deleteRow(0);
        };

	}
};

function ponerTodos(){
    document.getElementById("all").className = "filtro-opcion filtro-activado"
}

function cambiarFiltro(idButton){

console.log(idButton);
World.selectedFilter = idButton;

World.reloadPlaces();

var opciones = document.getElementsByClassName("filtro-opcion");

//TODO ver porque no anda el cambio de botón al tocar
for(var i=0; i< opciones.length; i++){
    opciones[i].className = "filtro-opcion filtro-desactivado"
}

document.getElementById(idButton).className = "filtro-opcion filtro-activado"

}

function mostrarWebsite(URL){
    window.open(URL,"_self");
}


/* forward locationChanges to custom function */
AR.context.onLocationChanged = World.locationChanged;

/* forward clicks in empty area to World */
AR.context.onScreenClick = World.onScreenClick;