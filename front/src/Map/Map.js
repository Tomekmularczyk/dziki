import React from "react";
import {
  Map as LMap,
  TileLayer,
  LayersControl,
  FeatureGroup,
} from "react-leaflet";
import styled from "styled-components";
import HeatmapLayer from "react-leaflet-heatmap-layer";
import { Marker } from "./Marker";
import { Modal } from "./Modal";

const StyledMap = styled(LMap)`
  width: 100%;
  height: 100%;
`;

const Container = styled.div`
  width: 100%;
  height: 80vh;
`;

const sulejowek = [52.25221, 21.26902];

export function Map({ reports }) {
  const [isModalVisible, setIsModalVisible] = React.useState(false);

  return (
    <Container>
      <StyledMap center={sulejowek} zoom={13} scrollWheelZoom>
        <LayersControl>
          <LayersControl.BaseLayer name="Zwykła" checked>
            <TileLayer
              url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"
              attribution="&copy; <a href=http://osm.org/copyright>OpenStreetMap</a> contributors"
            />
          </LayersControl.BaseLayer>
          <LayersControl.BaseLayer name="Administracyjna">
            <TileLayer
              url="https://tiles.wmflabs.org/hikebike/{z}/{x}/{y}.png"
              attribution="&copy; <a href=http://osm.org/copyright>OpenStreetMap</a> contributors"
            />
          </LayersControl.BaseLayer>
          <LayersControl.BaseLayer name="Satelita">
            <TileLayer
              url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}"
              attribution="&copy; <a href=http://osm.org/copyright>OpenStreetMap</a> contributors"
            />
          </LayersControl.BaseLayer>
          <LayersControl.Overlay name="Żywe" checked>
            <FeatureGroup color="purple">
              {reports
                .filter((m) => m.type === "ALIVE")
                .map((report, i) => (
                  <Marker
                    report={report}
                    key={i}
                    onReportClick={() => setIsModalVisible(true)}
                  />
                ))}
            </FeatureGroup>
          </LayersControl.Overlay>
          <LayersControl.Overlay name="Martwe" checked>
            <FeatureGroup color="red">
              {reports
                .filter((m) => m.type === "DEAD")
                .map((report, i) => (
                  <Marker
                    report={report}
                    key={i}
                    onReportClick={() => setIsModalVisible(true)}
                  />
                ))}
            </FeatureGroup>
          </LayersControl.Overlay>
          <LayersControl.Overlay name="Mapa termiczna">
            <FeatureGroup color="purple">
              <HeatmapLayer
                // gradient={{
                //   0.4: "#FEE92A",
                //   0.8: "#E47A17",
                //   1.0: "#C00000",
                // }}
                fitBoundsOnLoad
                fitBoundsOnUpdate
                minOpacity={0.6}
                points={[...reports]}
                longitudeExtractor={(m) => m.longitude}
                latitudeExtractor={(m) => m.latitude}
                intensityExtractor={(m) => {
                  // TODO: Nie działa :(
                  if (m.count === "SINGLE") {
                    return 0.1;
                  }

                  if (m.count === "MANY") {
                    return 0.7;
                  }

                  if (m.count === "HERD") {
                    return 1;
                  }

                  return 0.5;
                }}
              />
            </FeatureGroup>
          </LayersControl.Overlay>
        </LayersControl>
      </StyledMap>
      <Modal
        isVisible={isModalVisible}
        onClose={() => setIsModalVisible(false)}
      />
    </Container>
  );
}
