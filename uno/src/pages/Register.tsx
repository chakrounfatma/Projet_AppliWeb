import { useState } from "react";
import { useNavigate } from "react-router-dom";

import axios from "axios";
import "./Register.css";

function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nom, setNom] = useState("");
  const [prenom, setPrenom] = useState("");

  const navigate = useNavigate();

  const handleSubmit = (e: any) => {
    e.preventDefault();

    axios
      .post("http://localhost:8083/api/auth/register", {
        email: email,
        password: password,
        nom: nom,
        prenom: prenom,
        scoreTotal: 0,
      })
      .then(() => {
        alert("Compte créé avec succès ");
        navigate("/");
      })
      .catch((err) => {
        console.error(err);
        alert("Erreur lors de la création");
      });
  };

  return (
    <div className="register">
      <div className="card">
        <h2>Créer un compte </h2>

        <form onSubmit={handleSubmit}>
          <input
            type="name"
            placeholder="Nom"
            value={nom}
            onChange={(e) => setNom(e.target.value)}
            required
          />
          <input
            type="name"
            placeholder="Prénom"
            value={prenom}
            onChange={(e) => setPrenom(e.target.value)}
            required
          />
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

          <button type="submit">S'inscrire</button>
        </form>
      </div>
    </div>
  );
}

export default Register;
