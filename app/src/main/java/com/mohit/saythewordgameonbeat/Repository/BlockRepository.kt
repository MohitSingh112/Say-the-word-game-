package com.mohit.saythewordgameonbeat.Repository

import com.mohit.saythewordgameonbeat.R
import com.mohit.saythewordgameonbeat.data.Block
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockRepository @Inject constructor() {

    fun getBlocks(difficulty: Difficulty): List<Block> {
        return when(difficulty) {
            Difficulty.Easy -> listOfEasyBlocks.random()
            Difficulty.Medium -> listOfMediumBlocks.random()
            Difficulty.Hard -> listOfHardBlocks.random()
            Difficulty.NightMare -> listOfNightMareBlocks.random()
            else -> emptyList()
        }
    }


    // should have 4 blocks
    private val listOfEasyBlocks = listOf(
        listOf(
            Block(R.drawable.single_player, "Cat"),
            Block(R.drawable.single_player, "Bat"),
            Block(R.drawable.single_player, "Hat"),
            Block(R.drawable.single_player, "Rat")
        ),
        listOf(
            Block(R.drawable.single_player, "Dog"),
            Block(R.drawable.single_player, "Log"),
            Block(R.drawable.single_player, "Frog"),
            Block(R.drawable.single_player, "Cog")
        ) //todo(replace icons and add more)
    )

    // should have 6 blocks
    private val listOfMediumBlocks = listOf(
        listOf(
            Block(R.drawable.single_player, "Cat"),
            Block(R.drawable.single_player, "Bat"),
            Block(R.drawable.single_player, "Hat"),
            Block(R.drawable.single_player, "Rat"),
            Block(R.drawable.single_player, "Mat"),
            Block(R.drawable.single_player, "Sat")
        )//todo(replace icons and add more)
    )

    // should have 8 blocks
    private val listOfHardBlocks = listOf(
        listOf(
            Block(R.drawable.single_player, "Cat"),
            Block(R.drawable.single_player, "Bat"),
            Block(R.drawable.single_player, "Hat"),
            Block(R.drawable.single_player, "Rat"),
            Block(R.drawable.single_player, "Mat"),
            Block(R.drawable.single_player, "Sat"),
            Block(R.drawable.single_player, "Fat"),
            Block(R.drawable.single_player, "Pat")
        )//todo(replace icons and add more)
    )

    // should have 10 blocks
    private val listOfNightMareBlocks = listOf(
        listOf(
            Block(R.drawable.single_player, "Cat"),
            Block(R.drawable.single_player, "Bat"),
            Block(R.drawable.single_player, "Hat"),
            Block(R.drawable.single_player, "Rat"),
            Block(R.drawable.single_player, "Mat"),
            Block(R.drawable.single_player, "Sat"),
            Block(R.drawable.single_player, "Fat"),
            Block(R.drawable.single_player, "Pat"),
            Block(R.drawable.single_player, "Vat"),
            Block(R.drawable.single_player, "Gnat")
        )//todo(replace icons and add more)
    )
}