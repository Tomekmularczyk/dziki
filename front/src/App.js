import { Map } from "./Map";
import { useList } from "react-firebase-hooks/database";
import { database } from "./firebase";

function App() {
  const [snapshots, loading, error] = useList(database.ref("reports"));

  const reports = snapshots.map((s) => s.val());

  return (
    <div className="App">
      <Map reports={reports} />
    </div>
  );
}

export default App;
