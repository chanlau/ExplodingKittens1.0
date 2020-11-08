/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens.EKActions;

public class Card {
    //create variables for the current player playing the card, the target player of the card
    //if there is one, and the cardType as an int
    private int cardType;

    public Card(int cardType){
        this.cardType = cardType;
    }

    public Card(Card orig) {
        this.cardType = orig.getCardType();
    }

    public int getCardType() {
        return cardType;
    }
}

/*Card Index:
0: Exploding Kitten - 3
1:Tacocat - 4
2:BeardCat - 4
3:Hairy Potato Cat - 4
4:Rainbow Cat - 4
5:Cattermelon - 4
6:Attack - 4
7:Shuffle - 4
8:Favor - 4
9:Skip - 4
10:See the Future - 5
11: Nope - 5
12:Defuse - 2 (6 total)
 */