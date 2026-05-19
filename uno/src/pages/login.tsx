import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Login.css";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();
  const handleSubmit = (e: any) => {
    e.preventDefault();

    axios
      .post("http://localhost:8083/api/auth/login", {
        email,
        password,
      })
      .then((res) => {
        localStorage.setItem("token", res.data.token);
        localStorage.setItem("user", JSON.stringify(res.data));

        navigate("/");
      })

      .catch((err) => {
        console.error(err);
        alert("Email ou mot de passe incorrect ");
      });
  };

  return (
    <div className="login">
      <div className="card">
        <h2>Connexion</h2>

        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Mot de passe"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button type="submit">Se connecter</button>
        </form>
        <p className="register-link">
          Pas encore de compte ?
          <button className="link-btn" onClick={() => navigate("/register")}>
            S'inscrire
          </button>
        </p>
      </div>
    </div>
  );
}

export default Login;
