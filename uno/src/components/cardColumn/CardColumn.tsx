import Card from "../card/Card";
import CardView from "../card/CardView";
import "./CardColumn.css";

interface CardColumnProps {
  cards: Card[];
}

export default function CardColumn({ cards }: CardColumnProps) {
  cards.forEach((c) => {
    c.facedown = true;
  });

  return (
    <div className="cardcolumn">
      {cards.map((c) => (
        <CardView card={c}></CardView>
      ))}
    </div>
  );
}
