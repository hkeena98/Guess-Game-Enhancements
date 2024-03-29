<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
</head>
<body>
    <h1>${title}</h1>
    
    <div class="body">
    
      <h2>Application Stats</h2>
      <p>
        ${gameStatsMessage}
        <br/><br/>
        ${sessionStatsMessage}
      </p>



      <#if newPlayer>
      <p>
          <!--<a href="/game">Want to play a game?!?</a>-->
          <form id="difficultyform" style="text-align:left" action="/game" method="GET">
            <input type="radio" name="dif" id="1" value="1" required> Standard
            <input type="radio" name="dif" id="2" value="2"> Moderate
            <input type="radio" name="dif" id="3" value="3"> Difficult
            <button type="submit">Ok</button>
          </form>
          
      </p>
      <#else>
        <#if youWon>
          <p>
            Congratulations!  You must have read my mind.
            <br/><br/>
            <!--<a href="/game">Do it again</a>-->
            <form id="difficultyform" style="text-align:left" action="/game" method="GET">
              <input type="radio" name="dif" id="1" value="1" required> Standard
              <input type="radio" name="dif" id="2" value="2"> Moderate
              <input type="radio" name="dif" id="3" value="3"> Difficult
              <button type="submit">Ok</button>
            </form>
          </p>
        <#else>
          <p>
            Aww, too bad.  Better luck next time.
            <br/><br/>
            <!--<a href="/game">How about it?</a>-->
            <form id="difficultyform" style="text-align:left" action="/game" method="GET">
              <input type="radio" name="dif" id="1" value="1" required> Standard
              <input type="radio" name="dif" id="2" value="2"> Moderate
              <input type="radio" name="dif" id="3" value="3"> Difficult
              <button type="submit">Ok</button>
            </form>
          </p>
        </#if>
      </#if>
    
    </div>
  </div>
</body>
</html>
