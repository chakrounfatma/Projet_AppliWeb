import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import {
  getHand,
  getTopCard,
  playCard
} from "../services/gameService";

export default function Game() {

  const location = useLocation();

 const partieId = location.state?.partieId;
 const joueurId = location.state?.joueurId;

 

  const [hand, setHand] = useState([]);
  const [topCard, setTopCard] = useState(null);

  useEffect(() => {
    loadGame();
  }, []);

  const loadGame = async () => {

    const handRes = await getHand(joueurId, partieId);

    setHand(handRes.data);
    

    const topRes = await getTopCard(partieId);
    setTopCard(topRes.data);
    console.log("HAND =", handRes.data);
    console.log("PLAY RESPONSE", topRes.data);
console.log("TOP =", topRes.data);
  };

  const handlePlay = async (card) => {

  try {

    console.log("CARD =", card);

    await playCard(joueurId, card.id);
    

    loadGame();

  } catch (error) {

    console.log(error);

    alert(error.response?.data || "Erreur");

  }
};
  return (
    <div>

      <h1>UNO GAME</h1>

      <h2>Carte sur table</h2>

      {topCard && (
        <div>
          {topCard.couleur} {topCard.valeur}
        </div>
      )}

      <h2>Ma main</h2>

      <div style={{ display: "flex", gap: "10px" }}>
        {hand.map((card) => (
          <button
            key={card.id}
            onClick={() => handlePlay(card)}
          >
            {card.couleur || "NOIR"}{" "}
{card.type === "NUMBER" ? card.valeur : card.type}
          </button>
        ))}
      </div>

    </div>
  );
}