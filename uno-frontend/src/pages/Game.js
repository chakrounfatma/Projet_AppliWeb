import "./Game.css";
import { useEffect, useState } from "react";
import Confetti from "react-confetti";

import { useLocation } from "react-router-dom";
import {
  getHand,
  getTopCard,
  playCard,
  drawCard,
  getRandomPlayer,
  getGameState
} from "../services/gameService";

export default function Game() {

  const location = useLocation();

  const partieId = location.state?.partieId;
  const joueurId = location.state?.joueurId;
  const [opponentId, setOpponentId] = useState(null);

  const [winner, setWinner] = useState(null);
 

  const [wildCard, setWildCard] = useState(null);

  const [hand1, setHand1] = useState([]);
  const [hand2, setHand2] = useState([]);
  const [topCard, setTopCard] = useState(null);
  const [showHand1, setShowHand1] = useState(false);
  const [showHand2, setShowHand2] = useState(false);

  const [gameState, setGameState] = useState(null);

  const loadGameState = async () => {
  const res = await getGameState(partieId);
  setGameState(res.data);
};

  const loadGame = async (oppId) => {

  const hand1Res = await getHand(joueurId, partieId);
  const hand2Res = await getHand(oppId, partieId);

   console.log("JOUEUR 1:", joueurId);
  console.log("JOUEUR 2:", oppId);

  setHand1(hand1Res.data);
  setHand2(hand2Res.data);

  const topRes = await getTopCard(partieId);
  setTopCard(topRes.data);
};
useEffect(() => {
  if (partieId) {
    loadGameState();
  }
}, [partieId]);
useEffect(() => {
  console.log("GAME STATE:", gameState);
}, [gameState]);

  const handlePlay = async (card,currentPlayerId) => {
    
    console.log("CLICK CARD:", card);
    console.log("JOUEUR ACTUEL:", gameState?.joueurActuel?.id);
    console.log("MOI:", joueurId);
    if (card.type === "WILD" || card.type === "PLUS4") {
    setWildCard(card);
    return;
    }
    

    try {
      await playCard(currentPlayerId, card.id);
      await loadGameState();

      const topRes = await getTopCard(partieId);
      setTopCard(topRes.data);

      const updatedHand = await getHand(currentPlayerId, partieId);

      if (currentPlayerId === joueurId) {
        setHand1(updatedHand.data);
      } else {
        setHand2(updatedHand.data);
      }

    } catch (error) {
      console.log(error.response?.data || error.message);
      alert("Ce n'est pas ton tour !");
    }

};

const chooseColor = async (couleur) => {

  try {

    await playCard(
      joueurId,
      wildCard.id,
      couleur
    );

    const topRes = await getTopCard(partieId);
    setTopCard(topRes.data);

    const handRes = await getHand(joueurId, partieId);
    setHand1(handRes.data);

    setWildCard(null);

  } catch (err) {
    console.log(err);
  }
};

  useEffect(() => {
    console.log(" TOP CHANGE VISUEL:", topCard);
  }, [topCard]);
  
  useEffect(() => {
  if (joueurId && partieId) {
    initPlayers();
    loadGameState();
  }
  }, [joueurId, partieId]);

const initPlayers = async () => {

  let randomRes = await getRandomPlayer();
  let oppId = randomRes.data.id;

  // éviter que le joueur joue contre lui-même
  while (oppId === joueurId) {
    randomRes = await getRandomPlayer();
    oppId = randomRes.data.id;
  }

  setOpponentId(oppId);

  await loadGame(oppId);
};
const handleDraw = async () => {

      try {

          await drawCard(joueurId, partieId);

          const updatedHand = await getHand(joueurId, partieId);

          setHand1(updatedHand.data);

      } catch (err) {
          console.error(err);
      }
  };

useEffect(() => {
  const interval = setInterval(() => {
    fetch(`http://localhost:8083/game/state/${partieId}`)
      .then(res => res.json())
      .then(data => {
        if (data.etat === "terminee") {
          setWinner(data.joueurActuel); 
        }
      });
  }, 1000);

  return () => clearInterval(interval);
}, [partieId]);

const playWinSound = () => {
  const audio = new Audio("/sounds/win.mp3");
  audio.volume = 0.8;
  audio.play();
};

useEffect(() => {
  if (winner) {
    playWinSound();
  }
}, [winner]);

const handleReplay = async () => {
  await fetch(`http://localhost:8083/game/reset/${partieId}`, {
    method: "POST"
  });

  setWinner(null);
  window.location.reload(); // simple reset (tu peux améliorer après)
};

  return (
  <div className="game-container">

    <h1>UNO GAME</h1>

    {/* PIÈCE CENTRALE */}
    <div className="center-table">

      <h2>Carte sur table</h2>

      {topCard && (
        <div className="top-card">
          TOP :
          {topCard.couleur || "NOIR"}
          {topCard.type === "NUMBER"
            ? topCard.valeur
            : topCard.type}
        </div>
      )}

      {/* PIoche */}
      <div
        className="draw-pile"
        onClick={handleDraw}
        style={{
          width: "120px",
          height: "160px",
          backgroundColor: "black",
          color: "white",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          cursor: "pointer",
          margin: "auto",
          borderRadius: "10px"
        }}
      >
        PIOCHE
      </div>

    </div>

    {/* CHOIX COULEUR WILD */}
    {wildCard && (
      <div className="color-picker">
  <h3>Choisis une couleur</h3>

  <div className="color-buttons">
    <button onClick={() => chooseColor("ROUGE")}>🔴 Rouge</button>
    <button onClick={() => chooseColor("BLEU")}>🔵 Bleu</button>
    <button onClick={() => chooseColor("VERT")}>🟢 Vert</button>
    <button onClick={() => chooseColor("JAUNE")}>🟡 Jaune</button>
  </div>
</div>
    )}

    {/* JOUEURS */}
    <div className="players-container">

      {/* JOUEUR 1 */}
      <div className="player-zone">

        <h2>Joueur 1</h2>

        <button onClick={() => setShowHand1(!showHand1)}>
          {showHand1 ? "Cacher" : "Afficher"}
        </button>

        <div className="cards">
          {showHand1 ? (
            hand1.map((card) => (
              <button
                key={card.id}
                disabled={false}
                onClick={() => handlePlay(card,joueurId)}
              >
                {card.couleur || "NOIR"}{" "}
                {card.type === "NUMBER"
                  ? card.valeur
                  : card.type}
              </button>
            ))
          ) : (
            hand1.map((_, i) => (
              <div key={i} className="hidden-card">
                UNO
              </div>
            ))
          )}
        </div>

      </div>

      <div className="player-zone">

        <h2>Joueur 2</h2>

        <button onClick={() => setShowHand2(!showHand2)}>
          {showHand2 ? "Cacher" : "Afficher"}
        </button>

        <div className="cards">
          {showHand2 ? (
            hand2.map((card) => (
              <button
                key={card.id}
                disabled={false}
                onClick={() => handlePlay(card,opponentId)}
              >
                {card.couleur || "NOIR"}{" "}
                {card.type === "NUMBER"
                  ? card.valeur
                  : card.type}
              </button>
            ))
          ) : (
            hand2.map((_, i) => (
              <div key={i} className="hidden-card">
                UNO
              </div>
            ))
          )}
        </div>

      </div>

    </div>
{winner && (
  <>
    <Confetti />
    <div className="winner-overlay">
      <div className="winner-card">
        🏆 {winner.username} WON THE GAME 🏆
        <button className="replay-btn" onClick={handleReplay}>
           Rejouer
        </button>
      </div>
    </div>
  </>
)}
  </div>
);
}