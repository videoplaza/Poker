import random
from com.videoplaza.poker.game.util import PokerUtil


def get_image_url():
    return 'http://disturbingauctions.com/thumbnails/froggolf.jpg'


def get_creator_name():
    return "You"


def get_bot_name():
    return "You"


def to_call(game, me_player):
    """
    Calculate the amount of chips I need to call
    """
    return game.getHighestBet() - me_player.getCurrentBet()


def get_hand_rank(game, me_player):
    """
    Get the rank of your hand, 0-1 where 1 is the best possible hand to make with the cards available on the table.
    """
    if game.getCards().size() < 3:
        raise ValueError("No cards on the table")
    return PokerUtil.getHandRank(me_player.getHoleCards(), game.getCards())


def get_my_cards(me_player):
    """
    Get the cards that I am currently holding (0 or 2 cards)
    """
    cards = me_player.getHoleCards()
    return not cards and [] or cards


def get_cards_on_table(game):
    """
    Get the cards that are currently on the table (0,3,4 or 5 cards)
    """
    return game.getCards()


def raise_game(game, me_player, chips_to_raise, message):
    """
    Find what amount is needed to call and raise that bet with chipsToRaise
    """
    bet = to_call(game, me_player) + chips_to_raise
    if bet < game.getMinimumRaise():
        raise ValueError("Raise is too small (minimum raise is " + str(game.getMinimumRaise()) + ")")
    return message, bet


def call_game(game, me_player, message):
    """
    Find what amount is needed to call
    """
    return message, to_call(game, me_player)


def check_or_fold_game(message):
    """
    Fold if current best is not 0, in that case check
    """
    return message, 0


def all_in_game(me_player, message):
    return me_player.getStackSize(), message


def pre_flop_game(game, me_player):
    cards = get_my_cards(me_player)
    if cards[0].getRank() == cards[1].getRank():
        return all_in_game(me_player, "Pocket pairs")
    handrank = (cards[0].getRank() + cards[1].getRank()) / 28.0
    print("Preflop handrank " + str(handrank))
    if handrank > 0.2:
        return call_game(game, me_player, "Good preflop hand")
    return check_or_fold_game("Bad preflop hand")


def game(game, me_player):
    """
    returns chat_message, bet_value
    """
    try:
        if not len(game.getCards()):
            return pre_flop_game(game, me_player)
        handrank = get_hand_rank(game, me_player)
        print("Handrank " + str(handrank))
        if handrank > 0.5:
            return raise_game(game, me_player, game.getHighestBet() * 2, "Good postflop handrank")
        return check_or_fold_game("Bad postflop handrank")
    except Exception, e:
        print str(e)

