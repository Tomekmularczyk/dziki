import { Map } from "./Map/Map";
import { useList } from "react-firebase-hooks/database";
import { database } from "./firebase";
import navbar from "./navbar.png";
import styled from "styled-components";

const Img = styled.img`
  width: 100%;
  height: 200px;
`;

function App() {
  const [snapshots, loading, error] = useList(database.ref("reports"));

  const reports = snapshots.map((s) => s.val());

  return (
    <div className="App">
      <Img src={navbar} alt="" />
      <Map reports={reports} />
    </div>
  );
}

export default App;
