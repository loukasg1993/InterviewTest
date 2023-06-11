package com.betsson.interviewteest.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betsson.interviewteest.api.Repository
import kotlinx.coroutines.launch
import com.betsson.interviewteest.model.Bet

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val bets = ArrayList<Bet>()

    private val _betResults = MutableLiveData<List<Bet>>()
    val betResults: LiveData<List<Bet>>
    get() = _betResults
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
    get() = _errorMessage
    private val _updatedResults = MutableLiveData<List<Bet>>()
    val updatedResults: LiveData<List<Bet>>
    get() = _updatedResults

    fun fetchData() {
        viewModelScope.launch {
            try {
                //This would happen in an actual API call
//                val response = repository.getBets()
//                val sortedResponse = response.sortedBy { it.sellIn}
//                _betResults.value = sortedResponse
                val list = getItemsFromNetwork()
                val sortedList = list.sortedBy { it.sellIn }
                _betResults.postValue(sortedList)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateOdds() {
        val updatedBetsList = calculateOdds()
        val updatedSortedList = updatedBetsList.sortedBy { it.sellIn }
        _updatedResults.postValue(updatedSortedList)
    }
    private fun calculateOdds() : List<Bet> {
        val updatedBets = mutableListOf<Bet>()
        for (i in bets.indices) {

            //Create a copy of the bets list
            val originalBet = bets[i]
            val updatedBet = Bet(originalBet)
            if (updatedBet.odds > 0 && updatedBet.type != "First goal scorer"
                && updatedBet.type != "Total score" && updatedBet.type != "Number of fouls") {
                updatedBet.odds = updatedBet.odds - 1
            } else {
                if (updatedBet.odds < 50) {
                    updatedBet.odds = updatedBet.odds + 1

                    if (updatedBet.type == "Number of fouls") {
                        if (updatedBet.sellIn < 11 && updatedBet.odds < 50) {
                            updatedBet.odds = updatedBet.odds + 1
                        }
                        if (updatedBet.sellIn < 6 && updatedBet.odds < 50) {
                            updatedBet.odds = updatedBet.odds + 1
                        }
                    }
                }
            }

            if (updatedBet.type != "First goal scorer") {
                updatedBet.sellIn = updatedBet.sellIn - 1
            }

            if (updatedBet.sellIn < 0) {
                if (updatedBet.type != "Total score") {
                    if (updatedBet.type != "Number of fouls") {
                        if (updatedBet.odds > 0 && updatedBet.type != "First goal scorer") {
                            updatedBet.odds = updatedBet.odds - 1
                        }
                    } else {
                        updatedBet.odds = 0
                    }
                } else {
                    if (updatedBet.odds < 50) {
                        updatedBet.odds = updatedBet.odds + 1
                    }
                }
            }
            updatedBets.add(updatedBet)

        }
        bets.clear()
        bets.addAll(updatedBets)
        return updatedBets
    }

    private fun getItemsFromNetwork(): List<Bet> {
        bets.add(Bet("Winning team", 10, 20, "https://i.imgur.com/mx66SBD.jpeg"))
        bets.add(Bet("Total score", 2, 0, "https://i.imgur.com/VnPRqcv.jpeg"))
        bets.add(Bet("Player performance", 5, 7, "https://i.imgur.com/Urpc00H.jpeg"))
        bets.add(Bet("First goal scorer", 0, 80, "https://i.imgur.com/Wy94Tt7.jpeg"))
        bets.add(Bet("Number of fouls", 5, 49, "https://i.imgur.com/NMLpcKj.jpeg"))
        bets.add(Bet("Corner kicks", 3, 6, "https://i.imgur.com/TiJ8y5l.jpeg"))
        return bets
    }
}