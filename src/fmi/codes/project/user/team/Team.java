package fmi.codes.project.user.team;

import fmi.codes.project.user.User;

public record Team(String teamName, User teamLeader, User[] participants) {
}
