import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

import { getHand, getTopCard, playCard } from "../services/gameService";

import Card from "../components/card/Card";
import CardView from "../components/card/CardView";
import PlayedCard from "../components/playedCard/PlayedCard";

export default function Game() {
  const location = useLocation();

  const partieId = location.state?.partieId as number;
  const joueurId = location.state?.joueurId as number;

  const [hand, setHand] = useState<Card[]>([]);
  const [topCard, setTopCard] = useState<Card | null>(null);

  useEffect(() => {
    loadGame();
  }, []);

  const convertToCard = (data: any): Card => {
    return new Card({
      digit: data.valeur,
      color: data.couleur?.toLowerCase(),
      action:
        data.type !== "NUMBER"
          ? data.type?.toLowerCase().replace("_", " ")
          : "",
      facedown: false,
    });
  };

  const loadGame = async () => {
    try {
      const handRes = await getHand(joueurId, partieId);

      const handCards: Card[] = handRes.data.map((c: any) => convertToCard(c));

      setHand(handCards);

      const topRes = await getTopCard(partieId);

      setTopCard(convertToCard(topRes.data));
    } catch (error) {
      console.log(error);
    }
  };

  const handlePlay = async (card: Card) => {
    try {
      // find original backend card
      const backendCard = hand.find((c) => c === card);

      if (!backendCard) return;

      // @ts-ignore
      await playCard(joueurId, backendCard.id);

      loadGame();
    } catch (error: any) {
      console.log(error);

      alert(error.response?.data || "Erreur");
    }
  };

  return (
    <div>
      <h1>UNO GAME</h1>

      <h2>Carte sur table</h2>

      <div
        style={{
          display: "flex",
          alignItems: "center",
          gap: "20px",
        }}
      >
        <PlayedCard />

        {topCard && <CardView card={topCard} />}
      </div>

      <h2>Ma main</h2>

      <div
        style={{
          display: "flex",
          gap: "10px",
          flexWrap: "wrap",
        }}
      >
        {hand.map((card, index) => (
          <div
            key={index}
            onClick={() => handlePlay(card)}
            style={{ cursor: "pointer" }}
          >
            <CardView card={card} />
          </div>
        ))}
      </div>
    </div>
  );
}
