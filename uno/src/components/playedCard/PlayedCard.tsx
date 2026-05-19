import Card from "../card/Card";
import "./PlayedCard.css";

export default function PlayedCard() {
  const card = new Card({ facedown: true });
  return (
    <div className="drawing-card">
      <img className="drawing-card-background" src={card.getOverlayImage()} />
    </div>
  );
}
