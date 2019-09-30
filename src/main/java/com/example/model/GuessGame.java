package com.example.model;

import java.util.Random;
import java.util.logging.Logger;

/**
 * A single "guessing game".
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:jrv@se.rit.edu'>Jim Vallino</a>
 */
public class GuessGame {
  private static final Logger LOG = Logger.getLogger(GuessGame.class.getName());

  // The results for a player making a guess.
  public enum GuessResult {INVALID, HIGHER, LOWER, WON, LOST}

  //
  // Constants
  //

  /**
   * The upper bound on the numbers to be guessed. This is public so that other
   * components to read it; especially for unit testing.
   */
  public static final int UPPER_BOUND = 10;
  public static final int UPPER_BOUND2 = 24;
  

  /**
   * The number of guess attempts alloted. This is public so that other
   * components to read it; especially for unit testing.
   */
  public static final int NUM_OF_GUESSES = 3;
  public static final int NUM_OF_GUESSES2 = 5;


  private static final Random RANDOM = new Random();

  //
  // Attributes
  //

  private final int numberToGuess;
  private GuessResult lastResult = null;
  private int howManyGuessesLeft;
  private int difficulty_marker;
  private int game_bound;


  //
  // Constructors
  //

  /**
   * Create a guessing game with a known number.
   *
   * @param numberToGuess
   *          The number to be guessed.
   *
   * @throws IllegalArgumentException
   *    when the {@code numberToGuess} is out of range
   */
  public GuessGame(final int difficult) {
    int bound = 0;
    difficulty_marker = difficult;
    if(difficult == 1)
    {
      bound = UPPER_BOUND;
      howManyGuessesLeft = NUM_OF_GUESSES;
    }
    else if(difficult == 2)
    {
      bound = UPPER_BOUND2;
      howManyGuessesLeft = NUM_OF_GUESSES2;
    }
    else
    {
      bound = UPPER_BOUND2;
      howManyGuessesLeft = NUM_OF_GUESSES;
    }
    final int numberToGuess = RANDOM.nextInt(bound);
    game_bound = bound;
    // validate arguments
    if (numberToGuess < 0 || numberToGuess >= bound) {
      throw new IllegalArgumentException("numberToGuess is out of range");
    }
    //
    LOG.fine("Game created " + numberToGuess);
    this.numberToGuess = numberToGuess;
  }

  /**
   * Create a guessing game with a random number.
   *
   */
  //public GuessGame() {
  //   this(RANDOM.nextInt(UPPER_BOUND));
  //}

  //
  // Public methods
  //

  /**
   * Queries whether the game is at the beginning; meaning no guesses have yet
   * been made.
   *
   * @return true if no guesses have been made, otherwise, false
   */
  public synchronized boolean isGameBeginning() {
    int guesses;
    if(difficulty_marker != 2)
    {
      guesses = NUM_OF_GUESSES;
    }
    else
    {
      guesses = NUM_OF_GUESSES2;
    }
    return howManyGuessesLeft == guesses;
  }

  /**
   * Queries whether the supplied guess is a valid guess. This does not actually
   * issue a guess.
   *
   * @param guess
   *          The hypothetical guess.
   *
   * @return true if the guess falls within the game bounds, otherwise, false
   */
  public boolean isValidGuess(int guess) {
    int bound;
    if(difficulty_marker == 1)
    {
      bound = UPPER_BOUND;
    }
    else
    {
      bound = UPPER_BOUND2;
    }
    return guess >= 0 && guess < bound;
  }

  /**
   * Makes a guess on the game.
   *
   * @param myGuess
   *          The user's guess.
   *
   * @throws IllegalStateException
   *    when a guess is made on a game that is finished
   *
   * @return a {@link GuessResult} indicating the result of this guess
   */
  public synchronized GuessResult makeGuess(final int myGuess) {
    final GuessResult thisResult;
    // validate arguments
    int bound;
    if(difficulty_marker == 1)
    {
      bound = UPPER_BOUND;
    }
    else
    {
      bound = UPPER_BOUND2;
    }
    if (myGuess < 0 || myGuess >= bound) {
      thisResult = GuessResult.INVALID;
    } else {
      // assert that the game isn't over
      if (howManyGuessesLeft == 0) {
        throw new IllegalStateException("No more guesses allowed.");
      }
      // mark this guess
      howManyGuessesLeft--;
      // decide if this game is finished
      final boolean isCorrect = myGuess == numberToGuess;
      if (isCorrect) {
        thisResult = GuessResult.WON;
      } else if (hasMoreGuesses()) {
        if(myGuess < numberToGuess){
          thisResult = GuessResult.HIGHER;
        }
        else{
          thisResult = GuessResult.LOWER;
        }
      } else {
        thisResult = GuessResult.LOST;
      }
    }
    lastResult = thisResult;
    return thisResult;
  }

  /**
   * Queries whether the game is finished.
   *
   * @return true if the game was won or lost with the last guess
   */
  public synchronized boolean isFinished() {
    return GuessResult.WON.equals(lastResult) || GuessResult.LOST.equals(lastResult);
  }

  /**
   * Queries whether or not the game was won
   *
   * @return: true if the game was won
   */
  public synchronized boolean isWon(){
    return GuessResult.WON.equals(lastResult);
  }
  /**
   * Queries whether the user has more guesses for this game.
   *
   * @return true if there are more guesses to be made, otherwise, false
   */
  public synchronized boolean hasMoreGuesses() {
    return howManyGuessesLeft > 0;
  }

  /**
   * Queries for the number of guesses left in the game.
   *
   * @return the number of guesses left in this game
   */
  public synchronized int guessesLeft() {
    return howManyGuessesLeft;
  }

  public synchronized int gameDifficulty()
  {
    return difficulty_marker;
  }

  public synchronized int gameBound()
  {
    return game_bound;
  }

  public synchronized void setDifficulty(int diff)
  {
    difficulty_marker = diff;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized String toString() {
    return "{Game " + numberToGuess + "}";
  }
}
