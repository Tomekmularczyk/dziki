import { format } from "date-fns";
import L from "leaflet";
import * as React from "react";
import { Marker as LMarker, Popup } from "react-leaflet";
import styled, { createGlobalStyle } from "styled-components";
import { ReactComponent as ArrowCircleIcon } from "./arrow-circle.svg";
import alivePigIcon from "./marker-alive.svg";
import deadPigIcon from "./marker-dead.svg";
import { StatusBadge } from "./StatusBadge";

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

const Styles = createGlobalStyle`
  .${MARKER_CLASS} {
    background-color: transparent !important;
  }
`;

const StyledPopup = styled(Popup)`
  .leaflet-popup-content {
    margin: 0;
  }
  .leaflet-popup-content-wrapper {
    padding: 0;
  }
`;

const StyledIcon = styled(ArrowCircleIcon)`
  width: 35px;
  height: 35px;
  position: absolute;
  bottom: 5px;
  right: 5px;
  cursor: pointer;
`;

const Content = styled.div`
  padding: 1rem;
  min-width: 200px;
`;

const ContentImage = styled.img`
  border-radius: 10px 10px 0 0;
`;

const Type = styled.p`
  font-size: 11px;
  margin: 0 !important;
  color: #979797 !important;
  text-transform: uppercase;
`;

const Count = styled.p`
  font-size: 15px;
  font-family: Inter;
  font-style: normal;
  font-weight: normal;

  color: #687089 !important;
  margin: 5px 0 0 0 !important;
`;

const DateText = styled.p`
  font-family: Inter;
  font-style: normal;
  font-weight: 300;
  font-size: 11px;

  margin: 10px 0 0 0 !important;
  color: #979797 !important;
`;

const TypeToText = {
  ALIVE: "Żywy",
  DEAD: "Martwy",
};

const CountToText = {
  SINGLE: "Samotny dzik",
  MANY: "Samica z młodymi",
  HERD: "Stado dzików",
};

export function Marker({ report, onReportClick }) {
  const {
    latitude,
    longitude,
    type,
    photoUrl,
    count,
    timestamp,
    status,
  } = report;

  const date = new Date(timestamp);

  return (
    <>
      <Styles />

      <LMarker
        position={[latitude, longitude]}
        icon={type === "ALIVE" ? aliveIcon : deadIcon}
      >
        <StyledPopup>
          <ContentImage src={photoUrl} />
          {type === "DEAD" && <StatusBadge status={status} />}
          <Content>
            <Type>{TypeToText[type]}</Type>
            <Count>{CountToText[count]}</Count>
            <DateText>{format(date, "HH:mm dd.MM.yyyy")}</DateText>
          </Content>
          <StyledIcon onClick={onReportClick} />
        </StyledPopup>
      </LMarker>
    </>
  );
}
