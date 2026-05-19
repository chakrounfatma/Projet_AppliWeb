import "./Rule.css";
import { useNavigate } from "react-router-dom";

export default function Rules() {

  const navigate = useNavigate();

  return (
    <div className="rules-page">

      <div className="overlay"></div>

      <div className="rules-container">

        <h1 className="rules-title">Règles du UNO</h1>

        <div className="rules-card">

          <h2>Objectif du jeu</h2>
          <p>
            Le but est d’être le premier joueur à ne plus avoir de cartes.
          </p>

          <h2>Déroulement</h2>
          <p>
            Chaque joueur joue une carte de même couleur ou de même valeur
            que la carte présente sur la table.
          </p>

          <h2>Cartes spéciales</h2>

          <ul>
            <li>Skip : le joueur suivant passe son tour</li>
            <li>Reverse : inverse le sens du jeu</li>
            <li>+2 : le joueur suivant pioche 2 cartes</li>
            <li> Wild : choisir une couleur</li>
            <li>+4 : choisir une couleur et faire piocher 4 cartes</li>
          </ul>

          <h2>UNO</h2>
          <p>
            Quand il ne reste qu’une seule carte, le joueur doit dire UNO !
          </p>

          <button
            className="back-btn"
            onClick={() => navigate("/")}
          >
            Retour
          </button>

        </div>

      </div>
    </div>
  );
}