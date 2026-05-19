type CardColor = "red" | "blue" | "green" | "yellow" | "";
type CardAction = "skip" | "reverse" | "draw two" | "draw4" | "wild" | "";

export interface CardT {
  digit?: number;
  color?: CardColor;
  action?: CardAction;
  facedown: boolean;
}

export default class Card {
  digit?: number;
  color?: CardColor;
  action?: CardAction;
  facedown: boolean;

  constructor({ digit, color, action = "", facedown = false }: CardT) {
    this.digit = digit;
    this.color = color;
    this.action = action;
    this.facedown = facedown;
  }

  isActionCard(): boolean {
    return this.action !== "";
  }

  getOverlayImage(): string {
    if (!this.facedown) {
      if (this.isActionCard()) {
        return `card_assets/${this.action}-${this.color}.png`;
      }

      return `card_assets/front-${this.color}.png`;
    }
    return `card_assets/backside.png`;
  }

  getDisplayValue(): string {
    if (!this.facedown) {
      if (this.isActionCard()) {
        return this.action!;
      }

      return this.digit?.toString() || "";
    }
    return "";
  }
}
