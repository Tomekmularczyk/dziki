import { Map } from "./Map/Map";
import { useList } from "react-firebase-hooks/database";
import { database } from "./firebase";
import { NavBar } from "./NavBar/NavBar";

function App() {
  const [snapshots, loading, error] = useList(database.ref("reports"));

  const reports = snapshots.map((s) => s.val());

  return (
    <div className="App">
      <NavBar />
      <Map reports={reports} />
    </div>
  );
}

export default App;
