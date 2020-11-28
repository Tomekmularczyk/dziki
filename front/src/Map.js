import React from "react";
import {
  Map as LMap,
  TileLayer,
  Marker,
  Popup,
  LayersControl,
  FeatureGroup,
} from "react-leaflet";
import styled from "styled-components";
import HeatmapLayer from "react-leaflet-heatmap-layer";

const StyledMap = styled(LMap)`
  width: 100%;
  height: 100%;
`;

const Container = styled.div`
  width: 100%;
  height: 80vh;
`;

const sulejowek = [52.25221, 21.26902];

// var Thunderforest_Landscape = L.tileLayer('https://{s}.tile.thunderforest.com/landscape/{z}/{x}/{y}.png?apikey={apikey}', {
// 	attribution: '&copy; <a href="http://www.thunderforest.com/">Thunderforest</a>, &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
// 	apikey: '<your apikey>',
// 	maxZoom: 22
// });

export function Map({ reports }) {
  return (
    <Container>
      <StyledMap center={sulejowek} zoom={13} scrollWheelZoom={false}>
        <LayersControl>
          <LayersControl.BaseLayer name="Base" checked>
            <TileLayer
              url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"
              attribution="&copy; <a href=http://osm.org/copyright>OpenStreetMap</a> contributors"
            />
          </LayersControl.BaseLayer>
          <LayersControl.BaseLayer name="miasta-wsie" checked>
            <TileLayer
              url="https://{s}.tile.thunderforest.com/landscape/{z}/{x}/{y}.png"
              attribution="&copy; <a href=http://osm.org/copyright>OpenStreetMap</a> contributors"
            />
          </LayersControl.BaseLayer>
          <LayersControl.Overlay name="Heatmap" checked>
            <FeatureGroup color="purple">
              <Marker position={[50.05, -0.09]}>
                <Popup>
                  <span>
                    A pretty CSS3 popup.
                    <br /> Easily customizable.{" "}
                  </span>
                </Popup>
              </Marker>
              <HeatmapLayer
                fitBoundsOnLoad
                fitBoundsOnUpdate
                points={reports}
                longitudeExtractor={(m) => m.longitude}
                latitudeExtractor={(m) => m.latitude}
                intensityExtractor={(m) => 5}
              />
            </FeatureGroup>
          </LayersControl.Overlay>
          <LayersControl.Overlay name="Marker">
            <FeatureGroup color="purple">
              {reports.map((report, i) => (
                <Marker position={[report.latitude, report.longitude]} key={i}>
                  <Popup>
                    <span>
                      A pretty CSS3 popup.
                      <br /> Easily customizable.{" "}
                    </span>
                  </Popup>
                </Marker>
              ))}
            </FeatureGroup>
          </LayersControl.Overlay>
        </LayersControl>
      </StyledMap>
    </Container>
  );
}
