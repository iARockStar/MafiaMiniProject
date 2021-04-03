import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

/*Welcome to my mafia game!
 * the main focus in this project is OOP
 * The first class i wrote is God which handles the main function and functions related to the program as a whole
 */
public class God {
    /*Player[] array to save the participating players(they can be any kind(mafia,villager,etc))*/
    public static Player[] players = new Player[100];
    /*this string contains the player which was saved by the doc*/
    public static String savedByDoc = "";

    public static void main(String[] args) {
        /*here is the place i handled main menu*/
        System.out.println("\t\t\t\t\t\t\t Welcome to Mafia! one of the best strategy games you can play\n\t\t\t\t\t\t  please create a new game by typing \"create_game\" and players' names");
        Scanner in = new Scanner(System.in);

        String names = in.nextLine();
        while (names.equalsIgnoreCase("start_game")) {
            System.out.println("no game created");
            names = in.nextLine();
        }
        String[] namesPlusRoles = new String[20];
        String regex = "(?i)(create_game)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(names);
        /*checking if the player entered create_agme command correctly*/
        while (!matcher.find()) {
            System.out.println("no game created! please use keyword \"create_game\" at the beginning of line and then add players' names");
            names = in.nextLine();
            matcher = pattern.matcher(names);
        }
        String[] splitsNames = names.split(" ");
        System.out.println("Game is ready to be played! just assign the roles of players using \"assign_role\" + full name\nroles are : joker,villager,detective,doctor,bulletproof,mafia,godfather,silencer");
        /*if the player enters start_game command before assigning roles to all players,related error is printed.*/
        for (int i = 0; i < splitsNames.length - 1; i++) {
            String roles = in.nextLine();
            if (roles.equalsIgnoreCase("start_game")) {
                System.out.println("one or more player do not have a role ");
                i -= 1;
                continue;
            }
            String[] splits = roles.split(" ");
            roles = splits[1] + ": " + splits[2];
            namesPlusRoles[i] = roles;


            String[] splitsRoles = roles.split(" ");
            String regex1 = "(?i)(joker|villager|detective|doctor|bulletproof|mafia|godfather|silencer|informer)";
            Pattern pattern1 = Pattern.compile(regex1);
            Matcher matcher1 = pattern1.matcher(roles);
            /*checking if the role entered is correct*/
            while (!matcher1.find()) {
                System.out.println("no roles were found! please read the correct roles or assign a role to the player");
                roles = in.nextLine();
                splits = roles.split(" ");
                roles = splits[1] + ": " + splits[2];
                splitsRoles = roles.split(" ");
                namesPlusRoles[i] = roles;
                matcher1 = pattern1.matcher(roles);
            }
            boolean playerFound = false;
            for (int j = 0; j < splitsNames.length - 1; j++) {
                if ((splitsRoles[0]).equalsIgnoreCase(splitsNames[j + 1] + ":")) {
                    playerFound = true;
                    break;
                }
            }
            /*checking if the player entered is available*/
            if (!playerFound) {
                System.out.println("player not found! please type the correct player to assign your role");
                i -= 1;
                continue;
            }
            /*after checking the exceptions the player is saved in an array of players using playMaker function*/
            players[i] = playerMaker(splits[1], splits[2]);

        }
        /*starting the game...*/
        System.out.println("roles are saved and the game is ready to be played using start_game command");
        String start = in.nextLine();
        while (!start.equalsIgnoreCase("start_game")) {
            System.out.println("game not started. Please type start_game");
            start = in.nextLine();
        }/*prints roles of the players and their names before starting the game.*/
        for (String namesPlusRole : namesPlusRoles) {
            if (namesPlusRole != null)
                System.out.println(namesPlusRole);
        }
        System.out.println("Game is started and the God shall bring the first day");
        /*counter determines the number of the following day and night*/
        int counter = 1;
        System.out.println("When the day starts each player will need to vote for a player using \"voterName + voteeName\" command");
        String vote = "";
        /*isOkayToKill boolean determines if it is okay to kill the player or the votes are equal*/
        boolean isOkayToKill = true;
        /*this while will continue until one of the groups win the game*/
        while (true) {
            /*winnerChecker() checks if the game is over or not after the end of day and night*/
            if (winnerChecker() == 1) {
                System.out.println("Mafia won... they are all over the city!");
                break;
            } else if (winnerChecker() == -1) {
                System.out.println("Villagers won.No members of the mafia can be seen in this area!");
                break;
            }
            System.out.println("Day " + counter);
            /*several things have to be printed after day one like who has been killed or saved or printing information if informer is dead*/
            if (counter > 1) {
                /*here i check whether a player attacked by the mafia is dead.if not,i print why.*/
                for (Player value : players) {
                    if (value != null && value.tried_to_kill && !value.Is_dead_by_mafia_thisNight) {
                        System.out.println("mafia tried to kill someone.");
                        if (value.getName().equalsIgnoreCase(savedByDoc))
                            System.out.println("But he/she was saved by the doc.");
                        else if (value instanceof Bulletproof)
                            System.out.println("but he/she wore a bulletproof vest.");
                        else {
                            System.out.println("But they couldn't decide who to kill.");
                            break;
                        }
                        /*if they killed someone then the name is printed,too.*/
                    } else if (value != null && value.tried_to_kill && value.Is_dead_by_mafia_thisNight)
                        System.out.println("mafia tried to kill " + value.getName() + ".");
                    if (value != null && value.Is_dead_by_mafia_thisNight) {
                        System.out.println(value.getName() + " was killed.");
                        /*checking if the dead person was an informer.
                         * if he/she was an informer then something is shown for the players
                         */
                        if (value instanceof Informer) {
                            /*i created probability of printing an information using Random class*/
                            Random rand = new Random();
                            System.out.println(value.getName() + " was an Informer");
                            /*there are 4 possible information statements so...*/
                            int randNum = rand.nextInt(4);
                            outer:
                            while (true) {
                                if (randNum == 0) {
                                    for (Player player : players) {
                                        if (player instanceof Mafia && !player.Is_dead) {
                                            System.out.println("There is a mafia who’s name starts with " + player.getName().charAt(0));
                                            break outer;
                                        }
                                    }
                                    /*if there were no mafias in the city(not possible but anyway...)*/
                                    randNum++;
                                }
                                if (randNum == 1) {
                                    for (Player player : players) {
                                        if (player != null && player.isTriedToKillMarked() && !player.Is_dead) {
                                            System.out.println(player.getName() + " was voted to be killed");
                                            break outer;
                                        }
                                    }
                                    /*same as the last comment*/
                                    randNum++;
                                }
                                if (randNum == 2) {
                                    mafiaCounter();
                                    break;
                                }
                                if (randNum == 3) {
                                    for (Player player : players) {
                                        if (player instanceof Joker && !player.Is_dead) {
                                            System.out.println("There is a joker who’s name starts with " + player.getName().charAt(0));
                                            break outer;
                                        }
                                    }
                                    randNum -= 3;
                                }
                            }
                        }
                    }
                }
                /*checking whether a player is silenced by the silencer or not*/
                for (Player value : players) {
                    if (value != null && !value.canVote)
                        System.out.println(value.getName() + " silenced");
                }
                /*resetting the information changed last night like trying to kill someone and not succeeding*/
                for (Player player : players) {
                    if (player != null) {
                        player.Is_dead_by_mafia_thisNight = false;
                        player.tried_to_kill = false;
                    }
                }
            }
            outer:
            /*this while shows the day and will continue until the God enters end_vote command*/
            while (!vote.equalsIgnoreCase("end_vote")) {
                vote = in.nextLine();
                /*it's not the time to swap characters!*/
                if (vote.startsWith("swap_character")) {
                    System.out.println("voting in progress");
                    vote = "";
                    continue;
                }
                /*if the God accidentally enters end_night during the day*/
                if (vote.equalsIgnoreCase("end_night")) {
                    System.out.println("it's daytime");
                    vote = "";
                    continue;
                }
                /*checking if the god entered end_vote*/
                if (vote.equalsIgnoreCase("end_vote")) {
                    vote = "";
                    break;
                }
                /*God can get stats about players alive(number of mafias and villagers*/
                if (vote.equalsIgnoreCase("get_game_stat")) {
                    System.out.println(getGameStat());
                    continue;
                }
                /*the game is already started and no need to enter Start_game command*/
                if (vote.equalsIgnoreCase("start_game")) {
                    System.out.println("game has already started");
                    continue;
                }
                /*splitting voter and votee from each other and putting them in a string. */
                String[] splits = vote.split(" ");
                /*these boolean variables check if the players entered are in the game or not.*/
                boolean playerfound1 = false;
                boolean playerfound2 = false;
                /*checks if the player is silenced or he/she can vote during day*/
                for (Player player : players) {
                    if (player != null && player.getName().equalsIgnoreCase(splits[0])) {
                        playerfound1 = true;
                        if (!player.canVote) {
                            System.out.println("voter is silenced");
                            continue outer;
                        }
                    }

                }
                /*following 2 "for"s check if the players entered are dead or not*/
                for (Player player : players) {
                    if (player != null && player.getName().equalsIgnoreCase(splits[0])) {
                        playerfound1 = true;
                        if (player.Is_dead) {
                            System.out.println("voter is dead. user whom voted can not join the voting");
                            continue outer;
                        }
                    }
                }
                for (Player player : players) {
                    if (player != null && player.getName().equalsIgnoreCase(splits[1])) {
                        playerfound2 = true;
                        if (player.Is_dead) {
                            System.out.println("votee already dead");
                            continue outer;
                        }
                    }
                }
                if (!playerfound1 || !playerfound2) {
                    System.out.println("user not found");
                    continue;
                }
                /*now it's time for the god to count the voters' vote.*/
                for (Player player : players) {
                    if (player != null) {
                        if (player.getName().equalsIgnoreCase(splits[1]))
                            player.numOfVotesDuringDay++;
                    }
                }

            }
            int max = players[0].numOfVotesDuringDay;
            Player deadMan = players[0];
            /*this for finds the player with the most votes*/
            for (int k = 1; k < players.length; k++) {
                if (players[k] != null)
                    if (players[k].numOfVotesDuringDay > max) {
                        max = players[k].numOfVotesDuringDay;
                        deadMan = players[k];
                    }
            }
            /*according to the instructions if the votes are even for two or players then nobody is voted for exile.*/
            for (Player value : players) {
                if (value != null)
                    if (deadMan.numOfVotesDuringDay == value.numOfVotesDuringDay && !deadMan.equals(value)) {
                        System.out.println("nobody died");
                        for (Player player : players) {
                            if (player != null)
                                player.numOfVotesDuringDay = 0;
                        }
                        isOkayToKill = false;
                        break;
                    }
            }
            /*if the votes are uneven and the player with the most votes is found
             * then it's time for him/her to see his/her judgement
             */
            if (isOkayToKill) {
                /*if the dead man is joker then the game is over*/
                if (deadMan instanceof Joker) {
                    System.out.println("joker won! the game is over fellas");
                    ((Joker) deadMan).setDeadDaytime(true);
                    break;
                }
                deadMan.Is_dead = true;
                System.out.println(deadMan.getName() + " died");
                for (Player player : players) {
                    if (player != null)
                        player.numOfVotesDuringDay = 0;
                }
            }
            /*resetting the rights of players to vote*/
            for (Player player : players) {
                if (player != null && !player.canVote)
                    player.canVote = true;
            }
            /*checking the winner...*/
            if (winnerChecker() == 1) {
                System.out.println("Mafia won... they are all over the city!");
                break;
            } else if (winnerChecker() == -1) {
                System.out.println("Villagers won.No members of the mafia can be seen in this area!");
                break;
            }
            String input = "";
            System.out.println("night " + counter);
            /*before the start of the night players' names and their roles whom must wake up are printed for the God*/
            for (Player player : players) {
                if (player != null && !player.Is_dead && (player instanceof Mafia || player instanceof Doctor || player instanceof Detective))
                    System.out.println(player.toString());
            }
            /*this while indicates night and will continue until the God enters end_night command*/
            while (!input.equalsIgnoreCase("end_night")) {
                boolean Is_found = false;
                input = in.nextLine();
                /*still not the time to swap...*/
                if (input.startsWith("swap_character")) {
                    System.out.println("can’t swap before end of night");
                    input = "";
                    continue;
                }
                if (input.equalsIgnoreCase("end_night")) {
                    break;
                }
                if (input.equalsIgnoreCase("get_game_stat")) {
                    System.out.println(getGameStat());
                    continue;
                }
                /*no need to start the goddamn game!*/
                if (input.equalsIgnoreCase("start_game")) {
                    System.out.println("game has already started");
                    continue;
                }
                /*splitting names entered...*/
                String[] splits = input.split(" ");
                outer:
                /*now it's time for the players to play their role*/
                for (Player value : players) {
                    if (value != null && value.getName().equalsIgnoreCase(splits[0]) && !value.Is_dead)
                        if (value instanceof Doctor)
                            /*if it's the doc then saved player name is saved in "savedByDoc" string*/
                            for (Player player : players) {
                                if (player != null && player.getName().equalsIgnoreCase(splits[1]))
                                    savedByDoc = player.getName();
                            }
                        else if (value instanceof Detective)
                            /*detective can only ask once.
                             * so at first i checked if he/she asked or not.*/
                            if (!((Detective) value).hasAsked) {
                                ((Detective) value).hasAsked = true;
                                for (Player player : players) {
                                    if (player != null && player.getName().equalsIgnoreCase(splits[1])) {
                                        Is_found = true;
                                        /*if it's the godfather detective asked then it's a NO for him/her!*/
                                        if (player instanceof Mafia && player.getRole() != Role.godfather) {
                                            /*nothing important just checks if the mafia member is alive or not.*/
                                            if (value.Is_dead) {
                                                System.out.println("suspect is dead");
                                                break;
                                            }
                                            System.out.println("YES");
                                        } else System.out.println("NO");
                                    }
                                }
                                /*this boolean checks if the players entered are available or not.*/
                                if (!Is_found) System.out.println("user not found");
                                /*remember that detective can only ask once.*/
                            } else System.out.println("detective has already asked");
                        else if (value instanceof Mafia) {
                            /*the first time silencer votes he/she silences someone and doesn't kill anybody
                             * but the second time it's his/her vote as a mafia member to kill someone*/
                            if (value instanceof Silencer) {
                                if (!((Silencer) value).isHasSilenced()) {
                                    /*first vote of the silencer*/
                                    ((Silencer) value).setHasSilenced(true);
                                    for (Player player : players) {
                                        if (player != null && player.getName().equalsIgnoreCase(splits[1]))
                                            /*well if someone is silenced then he/she can not vote!*/
                                            player.canVote = false;
                                    }
                                } else {
                                    /*second vote of the silencer
                                     */
                                    for (Player player : players) {
                                        if (player != null && player.getName().equalsIgnoreCase(splits[1]) && !((Mafia) value).alreadyVoted) {
                                            /*adding votes for the players*/
                                            player.numOfVotesDuringNight++;
                                            ((Mafia) value).setPlayerMafiaVoted(player.getName());
                                            ((Mafia) value).alreadyVoted = true;
                                            player.setTriedTokKillMarked(true);
                                            break;
                                        } else if (player != null && player.getName().equalsIgnoreCase(splits[1]) && ((Mafia) value).alreadyVoted) {
                                            for (Player item : players) {
                                                if (item != null && item.getName().equalsIgnoreCase(((Mafia) value).getPlayerMafiaVoted())) {
                                                    /*if a mafia member changes his mind then the last vote overwrites*/
                                                    item.numOfVotesDuringNight--;
                                                    item.setTriedTokKillMarked(false);
                                                }
                                            }
                                            /*adding the new vote*/
                                            player.numOfVotesDuringNight++;
                                            ((Mafia) value).setPlayerMafiaVoted(player.getName());
                                            player.setTriedTokKillMarked(true);
                                        }
                                    }
                                }
                            } else {
                                /*voting for the members of the mafia except the Silencer*/
                                for (Player player : players) {
                                    if (player != null && player.getName().equalsIgnoreCase(splits[1]) && !((Mafia) value).alreadyVoted) {
                                        player.numOfVotesDuringNight++;
                                        ((Mafia) value).setPlayerMafiaVoted(player.getName());
                                        ((Mafia) value).alreadyVoted = true;
                                        break outer;
                                    } else if (player != null && player.getName().equalsIgnoreCase(splits[1]) && ((Mafia) value).alreadyVoted) {
                                        for (Player item : players) {
                                            if (item != null && item.getName().equalsIgnoreCase(((Mafia) value).getPlayerMafiaVoted()))
                                                item.numOfVotesDuringNight--;
                                        }
                                        player.numOfVotesDuringNight++;
                                        ((Mafia) value).setPlayerMafiaVoted(player.getName());
                                    }
                                }
                            }
                            /*checking the exceptions which may occur*/
                        } else System.out.println("user can not wake up during night");
                    else if (value != null && value.Is_dead && value.getName().equalsIgnoreCase(splits[0])) {
                        System.out.println("user is dead");
                        break;
                    }
                }

            }
            /*time to count the votes...*/
            int max2 = players[0].numOfVotesDuringNight;
            Player deadManAtNight = players[0];
            for (Player player : players) {
                if (player != null && player.numOfVotesDuringNight > max2) {
                    max2 = player.numOfVotesDuringNight;
                    deadManAtNight = player;
                }
            }
            /*if there was no voting then no one is dead:)
             * actually it's impossible but stops my code from printing garbage :))).
             * deadManAtNight is the dead player whom mafia voted to kill him/her.*/
            if (max2 > 0) {
                deadManAtNight.Is_dead_by_mafia = true;
                deadManAtNight.Is_dead = true;
                deadManAtNight.Is_dead_by_mafia_thisNight = true;
                deadManAtNight.tried_to_kill = true;
            }
            /*this counter is for checking whether two or more players had even and maximum votes for getting killed
             * by the way this instruction seemed kinda weird to me cause mafia don't hesitate they just choose one and kill!*/
            int counterForMafiaVote = 0;
            for (Player player : players) {
                if (player != null)
                    /*at first i see if the votes are equal
                     * if they are equal then no one is dead apparently;
                     * but if doc saves one person then hte other is dead*/
                    if (deadManAtNight.numOfVotesDuringNight == player.numOfVotesDuringNight && !deadManAtNight.equals(player) && deadManAtNight.numOfVotesDuringNight != 0) {
                        counterForMafiaVote++;
                        /*explanations for these boolean variables are in the Player class*/
                        player.Is_dead_by_mafia = true;
                        player.Is_dead = true;
                        player.tried_to_kill = true;
                        player.setTriedTokKillMarked(true);
                        player.Is_dead_by_mafia_thisNight = true;
                    }
            }
            if (counterForMafiaVote > 1) {
                /*if there are more than two players voted for getting killed then no one is dead*/
                for (Player player : players) {
                    if (player != null && player.Is_dead_by_mafia_thisNight) {
                        player.Is_dead_by_mafia = false;
                        player.Is_dead = false;
                        player.Is_dead_by_mafia_thisNight = false;
                    }
                }
            } else if (counterForMafiaVote == 1) {
                /*if there are two players with the same vote
                 * then it depends on doctor saving one of them or not*/
                boolean saved = false;
                /*if he saves one then the other is dead and if he doesn't then both will be alive:|.*/
                for (Player player : players) {
                    if (player != null && player.Is_dead_by_mafia_thisNight)
                        if (player.getName().equalsIgnoreCase(savedByDoc)) {
                            player.Is_dead_by_mafia = false;
                            player.Is_dead = false;
                            player.Is_dead_by_mafia_thisNight = false;
                            saved = true;
                            break;
                        }
                }
                /*if the player is not saved then both are saved i guess.*/
                if (!saved) {
                    for (Player player : players) {
                        if (player != null && player.Is_dead_by_mafia_thisNight) {
                            player.Is_dead_by_mafia = false;
                            player.Is_dead = false;
                            player.Is_dead_by_mafia_thisNight = false;
                        }
                    }
                }
            } else {
                /*if one player is voted for getting killed then there are possibilities*/
                for (Player player : players) {
                    if (player != null && player.Is_dead_by_mafia_thisNight)
                        /*checking whether the voted player is savedByDoc or not*/
                        if (player.getName().equalsIgnoreCase(savedByDoc)) {
                            player.Is_dead_by_mafia = false;
                            player.Is_dead = false;
                            player.Is_dead_by_mafia_thisNight = false;
                            break;
                        }
                }
                for (Player player : players) {
                    /*checking whether the voted player wore a bulletproof vest or not*/
                    if (player instanceof Bulletproof && player.Is_dead_by_mafia_thisNight && !((Bulletproof) player).hasTakenBullet()) {
                        player.Is_dead = false;
                        player.Is_dead_by_mafia = false;
                        player.Is_dead_by_mafia_thisNight = false;
                        ((Bulletproof) player).setTookBullet(true);
                        break;
                    }
                }

            }
            /*adding to the counter for the next day and night*/
            counter++;
            /*resetting anything needed to reset like
             *players permission to ask
             * or the votes of the mafia members
             * or the permission for silencer to silence*/
            isOkayToKill = true;
            for (Player player : players) {
                if (player instanceof Detective)
                    ((Detective) player).hasAsked = false;
            }
            for (Player player : players) {
                if (player != null)
                    player.numOfVotesDuringNight = 0;
            }
            for (Player player : players) {
                if (player instanceof Silencer)
                    ((Silencer) player).setHasSilenced(false);
            }
            /* this boolean checks if the mafia have voted or not
             * it needs to become false for the next night.*/
            for (Player player : players) {
                if (player instanceof Mafia)
                    ((Mafia) player).alreadyVoted = false;
            }

            /*asks the user if he/she wants to swap characters*/
            /*added comment : seems like there is no choice
             * but to swap characters at the end of the night so i commented the part
             * where you choose you'd rather swap or not*/
//                        String swap;
//            System.out.println("Wanna swap characters?(yes,no)");
//            swap = in.nextLine();
//            if (swap.equalsIgnoreCase("yes")) {
//                System.out.println("Please type (\"swap_character\" (first_player) (second_player)) to swap.");
            String swapper = in.nextLine();
            String[] splits = swapper.split(" ");
            while (splits[0].equalsIgnoreCase("swap_character")) {
                System.out.println("wrong command...");
                swapper = in.nextLine();
                splits = swapper.split(" ");
            }
            /*checking if the players are available.*/
            for (int i = 0; i < players.length; i++) {
                if (players[i] != null && players[i].getName().equalsIgnoreCase(splits[1]) && !players[i].Is_dead) {
                    for (int j = 0; j < players.length; j++) {
                        /*swapping the roles and info of the players but not their names*/
                        if (players[j] != null &&
                                players[j].getName().equalsIgnoreCase(splits[2]) && !players[j].Is_dead) {
                            String player2ToSwapName = players[i].getName();
                            Player temp = players[i];
                            players[i] = players[j];
                            players[j] = temp;
                            players[j].setName(players[i].getName());
                            players[i].setName(player2ToSwapName);
                            /*checking the exceptions...*/
                        } else if (players[j] != null && players[j].getName().equalsIgnoreCase(splits[2]) && players[j].Is_dead) {
                            System.out.println("user is dead");
                            break;
                        }
                    }
                } else if (players[i] != null && players[i].getName().equalsIgnoreCase(splits[1]) && players[i].Is_dead) {
                    System.out.println("user is dead");
                    break;
                }
            }

            /*checking the winner...*/
            if (winnerChecker() == 1) {
                System.out.println("Mafia won... they are all over the city!");
                break;
            } else if (winnerChecker() == -1) {
                System.out.println("Villagers won. No members of the mafia can be seen in this area!");
                break;
            }

        }
        /*if the winner is determined then the while loop breaks.
         * user is allowed to see the number of the mafia and villager players
         * or finish the game using "finish" command*/
        System.out.println("type \"get_game_stat\" to see what happened or \"finish\" to finish the game");
        String input2 = in.nextLine();
        while (!input2.equalsIgnoreCase("get_game_stat") && !input2.equalsIgnoreCase("finish")) {
            System.out.println("wrong command...");
            input2 = in.nextLine();
        }
        if (input2.equalsIgnoreCase("get_game_stat")) {
            System.out.println(getGameStat());
            /*checking if the reason of the endgame was the Joker*/
            for (Player player : players) {
                if (player instanceof Joker)
                    if (((Joker) player).isDeadDaytime())
                        System.out.println("Seems like it was the joker whom his/her trap worked!");
            }
        }
        System.out.println("GG");
    }

    /*counts number of alive mafias for the informer.*/
    public static void mafiaCounter() {
        int counterForMafia = 0;
        for (Player player : players) {
            if (player instanceof Mafia && !player.Is_dead)
                counterForMafia++;
        }
        System.out.println("Number of alive mafia : " + counterForMafia);
    }

    /*returns a string value which contains number of alive mafia and villager members.*/
    public static String getGameStat() {
        int counterForMafia = 0;
        int counterForVillager = 0;
        for (Player player : players) {
            if (player instanceof Mafia && !player.Is_dead)
                counterForMafia++;
            else if (player instanceof Villager && !player.Is_dead)
                counterForVillager++;
        }
        if (counterForMafia >= counterForVillager)
            return "Mafia: " + counterForMafia + "\n" + "Villager: " + counterForVillager + "\n" + "Seems like mafia took control of the city by numbers!";
        else if (counterForMafia == 0)
            return "Mafia: " + counterForMafia + "\n" + "Villager: " + counterForVillager + "\n" + "Seems like there is no mafia in town anymore!";
        else
            return "Mafia: " + counterForMafia + "\n" + "Villager: " + counterForVillager;
    }

    /*returns an integer value which indicates the winner
     * if it returns "1" then mafia wins
     * else if it returns "-1" then villager wins
     * return value of "0" makes the game continue
     */
    public static int winnerChecker() {
        int counterForMafia = 0;
        int counterForVillager = 0;
        for (Player player : players) {
            if (player instanceof Mafia && !player.Is_dead)
                counterForMafia++;
            else if (player instanceof Villager && !player.Is_dead)
                counterForVillager++;
        }
        if (counterForMafia == 0)
            /*if there is no mafia then villagers win*/
            return -1;
        if (counterForMafia >= counterForVillager)
            /*if the number of mafia members are more or equal to the villagers then mafias win*/
            return 1;
            /*the game continues*/
        else return 0;
    }

    /*returns a Player value
     * and adds a player to the array of players in the God class*/
    public static Player playerMaker(String name, String role) {
        if (role.equalsIgnoreCase("joker")) {
            /*there is only one joker in the game*/
            return Joker.getInstance(name, Role.joker);
        } else if (role.equalsIgnoreCase("villager"))
            return new Villager(name, Role.villager);
        else if (role.equalsIgnoreCase("detective"))
            return new Detective(name, Role.detective);
        else if (role.equalsIgnoreCase("doctor"))
            return new Doctor(name, Role.doctor);
        else if (role.equalsIgnoreCase("bulletproof"))
            return new Bulletproof(name, Role.bulletproof);
        else if (role.equalsIgnoreCase("mafia"))
            return new Mafia(name, Role.mafia);
        else if (role.equalsIgnoreCase("godfather"))
            return new Godfather(name, Role.godfather);
        else if (role.equalsIgnoreCase("silencer"))
            return new Silencer(name, Role.silencer);
        return new Informer(name, Role.informer);
    }

}

/*roles of the players in the mafia game*/
enum Role {
    joker, villager, detective, doctor, bulletproof, mafia, godfather, silencer, informer
}

class Player {

    private String name;
    private final Role role;
    /*this boolean checks if the player is dead by any means(day or night)*/
    boolean Is_dead = false;
    /*checks if the player is dead by the mafia last night not every night*/
    boolean Is_dead_by_mafia_thisNight = false;
    /*checks if the player is silenced by the silencer or not*/
    boolean canVote = true;
    /*checks if the player was attacked by the mafia or not*/
    boolean tried_to_kill = false;
    /*checks if the player is dead by the mafia. any time counts*/
    boolean Is_dead_by_mafia = false;
    /*votes for the player to be exiled during the day*/
    public int numOfVotesDuringDay = 0;
    /*votes for the player to be killed by the members of the mafia*/
    public int numOfVotesDuringNight = 0;
    /*checks if the mafia tried to kill the player but he/she didn't get enough votes*/
    private boolean triedTokKillMarked = false;


    public Player(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public void setTriedTokKillMarked(boolean triedTokKillMarked) {
        this.triedTokKillMarked = triedTokKillMarked;
    }

    public boolean isTriedToKillMarked() {
        return triedTokKillMarked;
    }

    @Override
    public String toString() {
        return name + ": " + role;
    }
}

/*there is only one joker in the game*/
class Joker extends Player {
    private static Joker instance;
    private boolean isDeadDaytime = false;

    private Joker(String name, Role role) {
        super(name, role);
    }

    public static Joker getInstance(String name, Role role) {
        if (instance == null) {
            instance = new Joker(name, role);
        }
        return instance;
    }

    public boolean isDeadDaytime() {
        return isDeadDaytime;
    }

    public void setDeadDaytime(boolean deadDaytime) {
        isDeadDaytime = deadDaytime;
    }
}

class Villager extends Player {
    public Villager(String name, Role role) {
        super(name, role);
    }
}

class Doctor extends Villager {
    public Doctor(String name, Role role) {
        super(name, role);
    }
}

class Detective extends Villager {
    public Detective(String name, Role role) {
        super(name, role);
    }

    /*checks if Detective asked the question at night*/
    public boolean hasAsked = false;

}

class Bulletproof extends Villager {
    /*checks if the bulletproof vest took the bullet or not*/
    private boolean tookBullet = false;

    public Bulletproof(String name, Role role) {
        super(name, role);
    }

    public void setTookBullet(boolean tookBullet) {
        this.tookBullet = tookBullet;
    }

    public boolean hasTakenBullet() {
        return tookBullet;
    }
}

class Mafia extends Player {
    /*the string contains the player a mafia voted.
     * if they changed their it changes, too.
     * */
    private String playerMafiaVoted = "";
    /*checks if the mafia voted or not.(changed their mind or not).*/
    public boolean alreadyVoted = false;

    public Mafia(String name, Role role) {
        super(name, role);
    }

    public void setPlayerMafiaVoted(String playerMafiaVoted) {
        this.playerMafiaVoted = playerMafiaVoted;
    }

    public String getPlayerMafiaVoted() {
        return playerMafiaVoted;
    }
}

class Silencer extends Mafia {
    /*checks if the silencer has silenced the payer or not.
     * so the next command which starts with silencer,
     * it's a vote to kill a player.
     * */
    private boolean hasSilenced = false;

    public Silencer(String name, Role role) {
        super(name, role);
    }

    public boolean isHasSilenced() {
        return hasSilenced;
    }

    public void setHasSilenced(boolean hasSilenced) {
        this.hasSilenced = hasSilenced;
    }
}

class Godfather extends Mafia {
    public Godfather(String name, Role role) {
        super(name, role);
    }
}

class Informer extends Villager {
    public Informer(String name, Role role) {
        super(name, role);
    }
}
