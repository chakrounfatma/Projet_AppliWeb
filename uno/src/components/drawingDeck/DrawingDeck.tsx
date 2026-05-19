import Card from "../card/Card";
import "./DrawingDeck.css";

export default function DrawingDeck() {
  const card = new Card({ facedown: true });
  return (
    <div className="drawing-card">
      <img className="drawing-card-background" src={card.getOverlayImage()} />
    </div>
  );
}
