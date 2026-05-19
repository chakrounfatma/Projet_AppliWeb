import { useEffect, useState } from "react";
import axios from "axios";

import "./Historique.css";

function Historique() {
  const [data, setData] = useState([]);

  useEffect(() => {
  axios.get("http://localhost:8083/game/historique")
    .then(res => {
      setData(res.data);
      console.log(res.data); // ✅ ici c’est bon
    })
    .catch(err => console.log(err));
}, []);

  return (
    <div className="history">
      <h1>Historique des parties</h1>

      <table>
        <thead>
          <tr>
            <th>Date</th>
            <th>Gagnant</th>
            <th>Partie ID</th>
          </tr>
        </thead>

        <tbody>
  {data.map((item, index) => (
    <tr key={index}>
      <td style={{ color: "white" }}>
        {new Date(item.date).toLocaleString()}
      </td>

      <td style={{ color: "white" }}>
        🏆 {item.joueur}
      </td>

      <td style={{ color: "white" }}>
        {item.partie}
      </td>
    </tr>
  ))}
</tbody>
      </table>
    </div>
  );
}

export default Historique;