import Card from "../card/Card";
import CardView from "../card/CardView";
import "./CardRow.css";

interface CardRowProps {
  cards: Card[];
}

export default function CardRow({ cards }: CardRowProps) {
  //   cards.forEach((c) => {
  //     c.facedown = true;
  //   });

  return (
    <div className="cardrow">
      {cards.map((c) => (
        <CardView card={c}></CardView>
      ))}
    </div>
  );
}
