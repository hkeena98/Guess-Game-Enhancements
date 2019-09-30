package com.example.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import static spark.Spark.halt;

import com.example.appl.PlayerServices;
import com.example.model.GuessGame;

import java.util.Random;

/**
 * The {@code GET /game} route handler.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:jrv@se.rit.edu'>Jim Vallino</a>
 */
public class GetGameRoute implements Route {

  // Values used in the view-model map for rendering the game view.
  static final String GAME_BEGINS_ATTR = "isFirstGuess";
  static final String GUESSES_LEFT_ATTR = "guessesLeft";
  static final String GAME_BOUND_ATTR = "gameBound";
  static final String TITLE = "Number Guess Game";
  static final String VIEW_NAME = "game_form.ftl";

  private final TemplateEngine templateEngine;

  /**
   * The constructor for the {@code GET /game} route handler.
   *
   * @param templateEngine
   *    The {@link TemplateEngine} used for rendering page HTML.
   */
  GetGameRoute(final TemplateEngine templateEngine) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    //
    this.templateEngine = templateEngine;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String handle(Request request, Response response) throws Exception {
    // retrieve the game object and start one if no game is in progress
    final Session httpSession = request.session();
    final PlayerServices playerServices =
        httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    
    String diff = request.queryParams("dif");
    int diffy = Integer.parseInt(diff);
    //int diffy = 3;
    System.out.println(diff);

    /* A null playerServices indicates a timed out session or an illegal request on this URL.
     * In either case, we will redirect back to home.
     */
    if (playerServices != null) {
      GuessGame game = playerServices.currentGame(diffy);
      //GuessGame game = playerServices.setGame(diffy);
      //GuessGame game = new GuessGame(diffy);
      // build the View-Model
      final Map<String, Object> vm = new HashMap<>();
      vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
      vm.put(GAME_BEGINS_ATTR, game.isGameBeginning());
      vm.put(GUESSES_LEFT_ATTR, game.guessesLeft());
      vm.put(GAME_BOUND_ATTR, game.gameBound());
      
      // render the Game Form view
      return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    } else {
      response.redirect(WebServer.HOME_URL);
      halt();
      return null;

    }
  }
}
