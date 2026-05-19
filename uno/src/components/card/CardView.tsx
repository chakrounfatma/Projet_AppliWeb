import Card from "./Card";
import "./CardView.css";

interface CardViewProps {
  card: Card;
}

export default function CardView({ card }: CardViewProps) {
  return (
    <div className="card">
      <img className="card-background" src={card.getOverlayImage()} alt="" />

      {!card.isActionCard() && (
        <>
          <span className="card-number_">{card.getDisplayValue()}</span>
          <span className="card-number-topleft">{card.getDisplayValue()}</span>
        </>
      )}
    </div>
  );
}
