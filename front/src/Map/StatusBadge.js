import styled from "styled-components";
import { ReactComponent as CheckmarkIcon } from "./checkmark.svg";

const Badge = styled.div`
  background-color: white;
  border-radius: 25px;
  position: absolute;
  top: 5px;
  left: 5px;
  padding: 4px 6px;
  display: flex;
  justify-content: center;
`;

const StyledCheckmarkIcon = styled(CheckmarkIcon)`
  width: 15px;
  height: 15px;
  margin-right: 5px;
`;

const Text = styled.span`
  font-family: Inter;
  font-style: normal;
  font-weight: 600;
  font-size: 10px;
  line-height: 15px;
  /* identical to box height, or 187% */

  letter-spacing: 0.05em;
  text-transform: uppercase;

  color: #20c3af;
`;

const StatusToText = {
  NEW: "Nowe",
  REPORTED: "Zgłoszone",
  DONE: "Przebadane",
};

export function StatusBadge({ status }) {
  return (
    <Badge>
      <StyledCheckmarkIcon />
      <Text>{StatusToText[status]}</Text>
    </Badge>
  );
}
