import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class God {
    public static Player[] players = new Player[6];
    public static String savedByDoc = "";

    public static void main(String[] args) {
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
        while (!matcher.find()) {
            System.out.println("no game created! please use keyword \"create_game\" at the beginning of line and then add players' names");
            names = in.nextLine();
            matcher = pattern.matcher(names);
        }
        String[] splitsNames = names.split(" ");
        System.out.println("Game is ready to be played! just assign the roles of players using \"assign_role\" + full name\nroles are : joker,villager,detective,doctor,bulletproof,mafia,godfather,silencer");
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
            String regex1 = "(?i)(joker|villager|detective|doctor|bulletproof|mafia|godfather|silencer)";
            Pattern pattern1 = Pattern.compile(regex1);
            Matcher matcher1 = pattern1.matcher(roles);
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
            if (!playerFound) {
                System.out.println("player not found! please type the correct player to assign your role");
                i -= 1;
                continue;
            }
            players[i] = playerMaker(splits[1], splits[2]);

        }
        System.out.println("roles are saved and the game is ready to be played using start_game command");
        String start = in.nextLine();
        while (!start.equalsIgnoreCase("start_game")) {
            System.out.println("game not started. Please type start_game");
            start = in.nextLine();
        }
        for (String namesPlusRole : namesPlusRoles) {
            if (namesPlusRole != null)
                System.out.println(namesPlusRole);
        }
        System.out.println("Game is started and the God shall bring the first day");
        int counter = 1;
        System.out.println("When the day starts each player will need to vote for a player using \"voterName + voteeName\" command");
        String vote = "";
        boolean isOkayToKill = true;
        while (true) {
            if (winnerChecker() == 1) {
                System.out.println("Mafia won... they are all over the city!");
                break;
            } else if (winnerChecker() == -1) {
                System.out.println("Villagers won.No members of the mafia can be seen in this area!");
                break;
            }
            System.out.println("Day " + counter);
            if (counter > 1) {
                for (Player value : players) {
                    if (value != null && value.tried_to_kill)
                        System.out.println("mafia tried to kill " + value.getName() + ".");
                    if (value!=null && value.Is_dead_by_mafia_thisNight)
                        System.out.println(value.getName() + " was killed.");
                }
                for (Player value : players) {
                    if (value != null && !value.canVote)
                        System.out.println(value.getName() + " silenced");
                }
                for (Player player : players) {
                    if (player != null) {
                        player.Is_dead_by_mafia_thisNight = false;
                        player.tried_to_kill = false;
                    }
                }
            }
            outer:
            while (!vote.equalsIgnoreCase("end_vote")) {
                vote = in.nextLine();
                if (vote.equalsIgnoreCase("end_vote")) {
                    vote = "";
                    break;
                }
                if (vote.equalsIgnoreCase("get_game_stat")) {
                    System.out.println(getGameStat());
                    continue;
                }
                if (vote.equalsIgnoreCase("start_game")) {
                    System.out.println("game has already started");
                    continue;
                }
                String[] splits = vote.split(" ");
                boolean playerfound1 = false;
                boolean playerfound2 = false;

                for (Player player : players) {
                    if (player != null && player.getName().equalsIgnoreCase(splits[0])) {
                        playerfound1 = true;
                        if (!player.canVote) {
                            System.out.println("voter is silenced");
                            continue outer;
                        }
                    }

                }
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
                for (Player player : players) {
                    if (player != null) {
                        if (player.getName().equalsIgnoreCase(splits[1]))
                            player.numOfVotesDuringDay++;
                    }
                }

            }
            int max = players[0].numOfVotesDuringDay;
            Player deadMan = players[0];
            for (int k = 1; k < players.length; k++) {
                if (players[k] != null)
                    if (players[k].numOfVotesDuringDay > max) {
                        max = players[k].numOfVotesDuringDay;
                        deadMan = players[k];
                    }
            }
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

            if (isOkayToKill) {
                if (deadMan.getRole() == Role.joker) {
                    System.out.println("joker won! the game is over fellas");
                    break;
                }
                deadMan.Is_dead = true;
                System.out.println(deadMan.getName() + " died");
                for (Player player : players) {
                    if (player != null)
                        player.numOfVotesDuringDay = 0;
                }
            }
            for (Player player : players) {
                if (player != null && !player.canVote)
                    player.canVote = true;
            }
            if (winnerChecker() == 1) {
                System.out.println("Mafia won... they are all over the city!");
                break;
            } else if (winnerChecker() == -1) {
                System.out.println("Villagers won.No members of the mafia can be seen in this area!");
                break;
            }
            String input = "";
            System.out.println("night " + counter);
            for (Player player : players) {
                if (player != null && !player.Is_dead)
                    System.out.println(player.toString());
            }
            while (!input.equalsIgnoreCase("end_night")) {
                boolean Is_found = false;
                input = in.nextLine();
                if (input.equalsIgnoreCase("end_night")) {
                    break;
                }
                if (input.equalsIgnoreCase("get_game_stat")) {
                    System.out.println(getGameStat());
                    continue;
                }
                if (input.equalsIgnoreCase("start_game")) {
                    System.out.println("game has already started");
                    continue;
                }
                String[] splits = input.split(" ");
                outer:
                for (Player value : players) {
                    if (value != null && value.getName().equalsIgnoreCase(splits[0]) && !value.Is_dead)
                        if (value instanceof Doctor)
                            for (Player player : players) {
                                if (player != null && player.getName().equalsIgnoreCase(splits[1]))
                                    savedByDoc = player.getName();
                            }
                        else if (value instanceof Detective)
                            if (!((Detective) value).hasAsked) {
                                ((Detective) value).hasAsked = true;
                                for (Player player : players) {
                                    if (player != null && player.getName().equalsIgnoreCase(splits[1])) {
                                        Is_found = true;
                                        if (player instanceof Mafia && player.getRole() != Role.godfather) {
                                            if (value.Is_dead) {
                                                System.out.println("suspect is dead");
                                                break;
                                            }
                                            System.out.println("YES");
                                        } else System.out.println("NO");
                                    }
                                }
                                if (!Is_found) System.out.println("user not found");
                            } else System.out.println("detective has already asked");
                        else if (value instanceof Mafia) {
                            if (value instanceof Silencer) {
                                if (!((Silencer) value).isHasSilenced()) {
                                    ((Silencer) value).setHasSilenced(true);
                                    for (Player player : players) {
                                        if (player != null && player.getName().equalsIgnoreCase(splits[1]))
                                            player.canVote = false;
                                    }
                                } else {
                                    for (Player player : players) {
                                        if (player != null && player.getName().equalsIgnoreCase(splits[1]) && !((Mafia) value).alreadyVoted) {
                                            player.numOfVotesDuringNight++;
                                            ((Mafia) value).setPlayerMafiaVoted(player.getName());
                                            ((Mafia) value).alreadyVoted = true;
                                            break;
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
                            } else {
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
                        } else System.out.println("user can not wake up during night");
                    else if (value != null && value.Is_dead && value.getName().equalsIgnoreCase(splits[0])) {
                        System.out.println("user is dead");
                        break;
                    }
                }

            }
            int max2 = players[0].numOfVotesDuringNight;
            Player deadManAtNight = players[0];
            for (Player player : players) {
                if (player != null && player.numOfVotesDuringNight > max2) {
                    max2 = player.numOfVotesDuringNight;
                    deadManAtNight = player;
                }
            }
            deadManAtNight.Is_dead_by_mafia = true;
            deadManAtNight.Is_dead = true;
            deadManAtNight.Is_dead_by_mafia_thisNight = true;
            deadManAtNight.tried_to_kill = true;
            int counterForMafiaVote = 0;
            for (Player player : players) {
                if (player != null)
                    if (deadManAtNight.numOfVotesDuringNight == player.numOfVotesDuringNight && !deadManAtNight.equals(player)) {
                        counterForMafiaVote++;
                        player.Is_dead_by_mafia = true;
                        player.Is_dead = true;
                        player.tried_to_kill = true;
                        player.Is_dead_by_mafia_thisNight = true;
                    }
            }
            if (counterForMafiaVote > 1) {
                for (Player player : players) {
                    if (player != null && player.Is_dead_by_mafia_thisNight) {
                        player.Is_dead_by_mafia = false;
                        player.Is_dead = false;
                        player.Is_dead_by_mafia_thisNight = false;
                    }
                }
            } else if (counterForMafiaVote == 1) {
                for (Player player : players) {
                    if (player != null && player.Is_dead_by_mafia_thisNight)
                        if (player.getName().equalsIgnoreCase(savedByDoc)) {
                            player.Is_dead_by_mafia = false;
                            player.Is_dead = false;
                            player.Is_dead_by_mafia_thisNight = false;
                            savedByDoc = "saved";
                            break;
                        }
                }
                if (!savedByDoc.equalsIgnoreCase("saved")) {
                    for (Player player : players) {
                        if (player.Is_dead_by_mafia_thisNight) {
                            player.Is_dead_by_mafia = false;
                            player.Is_dead = false;
                            player.Is_dead_by_mafia_thisNight = false;
                        }
                    }
                }
            } else {
                for (Player player : players) {
                    if (player != null && player.Is_dead_by_mafia)
                        if (player.getName().equalsIgnoreCase(savedByDoc)) {
                            player.Is_dead_by_mafia = false;
                            player.Is_dead = false;
                            break;
                        }
                }
                for (Player player : players) {
                    if (player instanceof Bulletproof && player.Is_dead_by_mafia && !((Bulletproof) player).hasTakenBullet()) {
                        player.Is_dead = false;
                        player.Is_dead_by_mafia = false;
                        ((Bulletproof) player).setTookBullet(true);
                        break;
                    }
                }

            }
            counter++;
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
            for (Player player : players) {
                if (player instanceof Mafia)
                    ((Mafia) player).alreadyVoted = false;
            }
            if (winnerChecker() == 1) {
                System.out.println("Mafia won... they are all over the city!");
                break;
            } else if (winnerChecker() == -1) {
                System.out.println("Villagers won. No members of the mafia can be seen in this area!");
                break;
            }

        }
        System.out.println("type \"get_game_stat\" to see what happened or \"finish\" to finish the game");
        String input2 = in.nextLine();
        while (!input2.equalsIgnoreCase("get_game_stat") && !input2.equalsIgnoreCase("finish")) {
            System.out.println("wrong command...");
            input2 = in.nextLine();
        }
        if (input2.equalsIgnoreCase("get_game_stat")) {
            System.out.println(getGameStat());
        }
        System.out.println("GG");
    }

    public static String getGameStat() {
        int counterForMafia = 0;
        int counterForVillager = 0;
        for (Player player : players) {
            if (player instanceof Mafia && !player.Is_dead)
                counterForMafia++;
            else if (player instanceof Villager && !player.Is_dead)
                counterForVillager++;
        }
        return "Mafia: " + counterForMafia + "\n" + "Villager: " + counterForVillager + "\n";
    }

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
            return -1;
        if (counterForMafia >= counterForVillager)
            return 1;
        else return 0;
    }

    public static Player playerMaker(String name, String role) {
        if (role.equalsIgnoreCase("joker")) {
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
        return new Player(name, Role.villager);
    }

}

enum Role {
    joker, villager, detective, doctor, bulletproof, mafia, godfather, silencer
}

class Player {
    private final String name;
    private final Role role;
    boolean Is_dead = false;
    boolean Is_dead_by_mafia_thisNight = false;
    boolean canVote = true;
    boolean tried_to_kill = false;
    boolean Is_dead_by_mafia = false;
    public int numOfVotesDuringDay = 0;
    public int numOfVotesDuringNight = 0;

    public Player(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public void setIs_dead(boolean is_dead) {
        Is_dead = is_dead;
    }

    public void setCanVote(boolean canVote) {
        this.canVote = canVote;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return name + ": " + role;
    }
}

class Joker extends Player {
    private static Joker instance;

    private Joker(String name, Role role) {
        super(name, role);
    }

    public static Joker getInstance(String name, Role role) {
        if (instance == null) {
            instance = new Joker(name, role);
        }
        return instance;
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

    public boolean hasAsked = false;

}

class Bulletproof extends Villager {
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
    private String playerMafiaVoted = "";
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
