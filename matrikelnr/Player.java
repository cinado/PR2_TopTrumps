package matrikelnr;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Player implements Comparable<Player>{
	private String name;
	private Queue<VehicleCard> deck = new ArrayDeque<>();
	
	public Player(final String name) throws IllegalArgumentException{
		if(name == null) {
			throw new IllegalArgumentException("Player's name cannot be null!");
		}
		if(name.isEmpty()) {
			throw new IllegalArgumentException("Player's name cannot empty!");
		}
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getScore() {
		int sum = 0;
		for(var x:deck) {
			sum += x.totalBonus();
		}
		return sum;
	}
	
	public void addCards(final Collection<VehicleCard> cards) {
		if(cards == null || cards.contains(null)) {
			throw new IllegalArgumentException("Cards or card in cards cannot be null!");
		}
		deck.addAll(cards);
	}
	
	public void addCard(final VehicleCard card) throws IllegalArgumentException {
		if(card == null) {
			throw new IllegalArgumentException("Card cannot be null!");
		}
		deck.add(card);
	}
	
	public void clearDeck() {
		deck.clear();
	}
	
	public List<VehicleCard> getDeck(){
		return new ArrayList<VehicleCard>(this.deck);
	}
	
	protected VehicleCard peekNextCard() {
		return deck.peek();
	}
	
	public VehicleCard playNextCard () {
		return deck.remove();
	}
	
	@Override
	public int compareTo(final Player other) {
		if(name.equalsIgnoreCase(other.getName())) {
			return 1;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name.toLowerCase());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(name.toLowerCase(), other.name.toLowerCase()); 
	}

	@Override
	public String toString() {
		String res = "";
		res += name + " (" + getScore() + ")\n";
		for(var x:deck) {
			res += x.toString() + "\n";
		}
		return res;
	}
	
	public boolean challengePlayer(Player p) throws IllegalArgumentException{
		if(p == null) {
			throw new IllegalArgumentException("Challenged player cannot be null!");
		}
		if(p.equals(this)) {
			throw new IllegalArgumentException("Challenged Player cannot be the same as the this-object!");
		}
		Queue<VehicleCard> player1PlayedCards = new ArrayDeque<VehicleCard>();
		Queue<VehicleCard> player2PlayedCards = new ArrayDeque<VehicleCard>();
		while(!this.deck.isEmpty() || !p.deck.isEmpty()) {
			if(this.deck.isEmpty() || p.getDeck().isEmpty()) {
                this.addCards(player1PlayedCards);
                p.addCards(player2PlayedCards);
                return false;
            }
			VehicleCard v1 = this.playNextCard();
			VehicleCard v2 = p.playNextCard();
			player1PlayedCards.add(v1);
			player2PlayedCards.add(v2);
			if(v1.compareTo(v2) > 0) {
				this.addCards(player1PlayedCards);
				this.addCards(player2PlayedCards);
				return true;
			}
			else if(v1.compareTo(v2) < 0) {
				p.addCards(player1PlayedCards);
				p.addCards(player2PlayedCards);
				return false;
			}
		}
		this.deck.addAll(player1PlayedCards);
		p.deck.addAll(player2PlayedCards);
		return false;
	}

	public static Comparator<Player> compareByScore() {
		return (p1, p2) -> Integer.compare(p1.getScore(), p2.getScore());
	}
	public static Comparator<Player> compareByDeckSize() {
		return (p1, p2) -> Integer.compare(p1.deck.size(), p2.deck.size());
	}
}
