import "./Home.css";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { useState } from "react";
import axios from "axios";
function Home() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setUser(null);
    navigate("/");
  };
  const handlePlay = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8083/game/create",
        [1],
      );

      const partie = response.data;

      navigate("/game", {
        state: {
          partieId: partie.id,
          joueurId: 1,
        },
      });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="home">
      <div className="overlay"></div>
      <header>
        <h1 className="bienvenue">
          Bienvenue {user ? user.prenom : "sur UNO Game"}
        </h1>
      </header>
      <div className="container">
        <h1 className="title">UNO Game </h1>
        <div className="menu">
          {!user ? (
            <button className="btn secondary" onClick={handlePlay}>
              Jouer en invité
            </button>
          ) : (
            <button className="btn secondary" onClick={handlePlay}>
              Jouer
            </button>
          )}
          {user ? (
            <></>
          ) : (
            <button
              className="btn secondary"
              onClick={() => navigate("/register")}
            >
              Creer un compte
            </button>
          )}
          <button
            className="btn secondary"
            onClick={() => navigate(user ? "/profile" : "/login")}
          >
            Profil
          </button>
          <div className="divider">OU</div>
          {!user ? (
            <button onClick={() => navigate("/login")}> Se connecter </button>
          ) : (
            <button onClick={handleLogout}> Se déconnecter </button>
          )}

          <button className="btn ghost">Classement</button>
          <button className="btn ghost">Règles du jeu</button>
        </div>
      </div>
    </div>
  );
}

export default Home;
