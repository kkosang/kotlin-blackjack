package blackjack.model

class GameResult {
    private val dealerResults: MutableMap<Result, Int> = Result.entries.associateWith { 0 }.toMutableMap()
    private val playerResults: MutableMap<Player, Result> = mutableMapOf()

    fun getDealerResults(): MutableMap<Result, Int> {
        return dealerResults
    }

    fun getPlayerResults(): MutableMap<Player, Result> {
        return playerResults
    }

    fun calculateResult(
        dealer: Dealer,
        players: List<Player>,
    ) {
        players.forEach { player ->
            val playerResult = player.judgeBlackState(dealer)
            val dealerResult = dealer.calculateDealerResult(playerResult)
            updateResults(player, dealerResult)
        }
    }

    private fun updateResults(
        player: Player,
        dealerResult: Result,
    ) {
        applyDealerResult(dealerResult)
        applyPlayerResult(player, dealerResult)
    }

    private fun applyPlayerResult(
        player: Player,
        dealerResult: Result,
    ) {
        when (dealerResult) {
            Result.WIN -> playerResults[player] = Result.LOSE
            Result.DRAW -> playerResults[player] = Result.DRAW
            Result.LOSE -> playerResults[player] = Result.WIN
        }
    }

    private fun applyDealerResult(dealerResult: Result) {
        dealerResults[dealerResult] = dealerResults.getOrDefault(dealerResult, DEFAULT_COUNT) + ADD_RESULT_COUNT
    }

    fun calculateProfitResult(): MutableMap<Participant, Double> {
        val dealer = Dealer()
        val profitResult = mutableMapOf<Participant, Double>()
        var dealerProfit = 0.0
        playerResults.forEach { player, result ->
            val profit = player.calculateProfit(result, player.getBlackJackState())
            dealerProfit -= profit
            profitResult[player] = profit
        }
        profitResult[dealer] = dealerProfit

        return profitResult
    }

    companion object {
        private const val DEFAULT_COUNT: Int = 1
        private const val ADD_RESULT_COUNT: Int = 1
    }
}
