import { Marker as LMarker, Popup } from "react-leaflet";
import alivePigIcon from "./marker-alive.svg";
import deadPigIcon from "./marker-dead.svg";
import L from "leaflet";
import styled from "styled-components";

const MARKER_CLASS = "custom-leaflet-icon";

const MARKER_SIZE = new L.Point(40, 40);

const aliveIcon = new L.Icon({
  iconUrl: alivePigIcon,
  iconRetinaUrl: alivePigIcon,
  iconSize: MARKER_SIZE,
  className: MARKER_CLASS,
});

const deadIcon = new L.Icon({
  iconUrl: deadPigIcon,
  iconRetinaUrl: deadPigIcon,
  iconSize: MARKER_SIZE,
  className: MARKER_CLASS,
});

const Styles = styled.span`
  .${MARKER_CLASS} {
    background-color: transparent !important;
  }
`;

export function Marker({ latitude, longitude, type = "ALIVE" }) {
  return (
    <Styles>
      <LMarker
        position={[latitude, longitude]}
        icon={type === "ALIVE" ? aliveIcon : deadIcon}
      >
        <Popup>
          <span>
            A pretty CSS3 popup.
            <br /> Easily customizable.{" "}
          </span>
        </Popup>
      </LMarker>
    </Styles>
  );
}
