import Card from "../card/Card";
import CardView from "../card/CardView";
import "./CardColumn2.css";

interface CardColumnProps {
  cards: Card[];
}

export default function CardColumn2({ cards }: CardColumnProps) {
  cards.forEach((c) => {
    c.facedown = true;
  });

  return (
    <div className="cardcolumn2">
      {cards.map((c) => (
        <CardView card={c}></CardView>
      ))}
    </div>
  );
}
